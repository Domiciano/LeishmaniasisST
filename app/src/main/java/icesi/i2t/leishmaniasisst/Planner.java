package icesi.i2t.leishmaniasisst;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import icesi.i2t.leishmaniasisst.model.BasicAdverseEvent;
import icesi.i2t.leishmaniasisst.model.DailySchema;
import icesi.i2t.leishmaniasisst.model.Evaluador;
import icesi.i2t.leishmaniasisst.model.ListaDailySchemas;
import icesi.i2t.leishmaniasisst.model.ListaPacientes;
import icesi.i2t.leishmaniasisst.model.ListaPrescripciones;
import icesi.i2t.leishmaniasisst.model.ListaScheduleTaking;
import icesi.i2t.leishmaniasisst.model.ListaSchemas;
import icesi.i2t.leishmaniasisst.model.ListaUlcerForms;
import icesi.i2t.leishmaniasisst.model.Paciente;
import icesi.i2t.leishmaniasisst.model.Prescripcion;
import icesi.i2t.leishmaniasisst.model.ScheduleTaking;
import icesi.i2t.leishmaniasisst.model.Schema;
import icesi.i2t.leishmaniasisst.model.UlcerForm;
import icesi.i2t.leishmaniasisst.model.Usuario;
import icesi.i2t.leishmaniasisst.services.WebserviceConsumer;
import icesi.i2t.leishmaniasisst.util.LeishConstants;
import icesi.i2t.leishmaniasisst.view.LargeListView;

public class Planner extends AppCompatActivity {

    Spinner spinerPacientes, spinerMedicamentos, spinner_evaluadores, spinner_unidades;

    ArrayList<String> arrayRaters;
    ArrayAdapter<String> adapterSpinnerPacientes, adapterSpinnerRaters, adapterUnidades;
    ArrayList<String> arrayPacientes;
    Button selecfechas, ok_planner;
    ScrollView scrollPlanner;
    int dias_tratamiento = -1;
    Date date_inicio;
    String fecha_inicio = "";
    String fecha_fin = "";

    //PACIENTES
    ArrayList<String> ownPacientes, arrayUnidades;
    LargeListView lista_pacientes;
    PacientesAdapter pacientesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        arrayUnidades = new ArrayList<>();
        arrayUnidades.add("Seleccione una opción");
        adapterUnidades = new ArrayAdapter<>(this, R.layout.simple_list_item_1, arrayUnidades);
        spinner_unidades = findViewById(R.id.spinner_unidades);
        spinner_unidades.setAdapter(adapterUnidades);
        spinner_unidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    refreshOKButton();
                    scrollDown();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_evaluadores = findViewById(R.id.spinner_evaluadores);
        arrayRaters = new ArrayList<>();
        arrayRaters.add("Cargando...");
        adapterSpinnerRaters = new ArrayAdapter<String>(this, R.layout.simple_list_item_1_orange, arrayRaters);

        //LISTVIEW DE PACIENTES
        ownPacientes = new ArrayList<>();
        ownPacientes.add("Cargando...");
        lista_pacientes = findViewById(R.id.lista_pacientes);
        pacientesListAdapter = new PacientesAdapter(this, ownPacientes);
        lista_pacientes.setAdapter(pacientesListAdapter);
        spinner_evaluadores.setAdapter(adapterSpinnerRaters);

        arrayPacientes = new ArrayList<>();
        arrayPacientes.add("Cargando...");
        adapterSpinnerPacientes = new ArrayAdapter<>(this, R.layout.simple_list_item_1, arrayPacientes);


        //PASO 1. LLamar al getAllRaters
        WebserviceConsumer.GetAllRaters getAllRaters = new WebserviceConsumer.GetAllRaters();
        getAllRaters.setObserver(new WebserviceConsumer.ServerResponseReceiver() {
            @Override
            public void onServerResponse(Object json) {
                //PASO 2. Llenar el primer spiner con evaluadores
                runOnUiThread(() -> {
                    loadRaters();
                    ((TextView) findViewById(R.id.labelmenos1)).setText("Seleccione un evaluador de la lista a continuación");
                });
                scrollDown();
            }
        });
        getAllRaters.start();

        spinner_evaluadores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    //PASO 3. Consultar el documento para el paciente seleccionado
                    WebserviceConsumer.GetLastDocumentByRaterCedula getLastDocument = new WebserviceConsumer.GetLastDocumentByRaterCedula("cedula");
                    getLastDocument.setObserver(new WebserviceConsumer.ServerResponseReceiver() {
                        @Override
                        public void onServerResponse(Object json) {
                            runOnUiThread(() -> {
                                lista_pacientes.setVisibility(View.VISIBLE);
                                if (pacientesListAdapter.pacientes.size() > 0) {
                                    findViewById(R.id.label0).setVisibility(View.VISIBLE);
                                    findViewById(R.id.label01).setVisibility(View.VISIBLE);
                                }

                                spinerPacientes.setVisibility(View.VISIBLE);
                                findViewById(R.id.label1).setVisibility(View.VISIBLE);
                                findViewById(R.id.label11).setVisibility(View.VISIBLE);
                                findViewById(R.id.label12).setVisibility(View.VISIBLE);

                                ownPacientes.clear();
                                ownPacientes.add("Yor Castaño");
                                ownPacientes.add("William Cruz");
                                ownPacientes.add("Fabián Lasso");
                                pacientesListAdapter.notifyDataSetChanged();

                                scrollDown();

                                //PASO 4. Consultar todos los pacientes
                                WebserviceConsumer.GetAllPatients getAllPatients = new WebserviceConsumer.GetAllPatients();
                                getAllPatients.setObserver(new WebserviceConsumer.ServerResponseReceiver() {
                                    @Override
                                    public void onServerResponse(Object json) {
                                        runOnUiThread(() -> {
                                            //Cargamos sólo los pacientes que no tienen la bandera
                                            arrayPacientes.clear();
                                            arrayPacientes.add("Seleccione una opción");
                                            arrayPacientes.add("Omega");
                                            adapterSpinnerPacientes.notifyDataSetChanged();
                                            scrollDown();
                                        });

                                    }
                                });
                                getAllPatients.start();

                            });
                        }
                    });
                    getLastDocument.start();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ok_planner = findViewById(R.id.ok_planner);
        ok_planner.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Planner.this)
                    .setTitle("Confirmar")
                    .setMessage("Si está seguro de la prescripción oprima INSCRIBIR, de lo contrario oprima CANCELAR")
                    .setPositiveButton("INSCRIBIR", (dialogInterface, i) -> {
                        construirPrescripcion();
                        dialogInterface.dismiss();
                        dialogInterface.cancel();
                    })
                    .setNegativeButton("CANCELAR", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        dialogInterface.cancel();
                    });
            builder.create().show();

        });

        scrollPlanner = findViewById(R.id.scrollPlanner);

        selecfechas = findViewById(R.id.selecfechas);
        selecfechas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendar();
            }
        });


        spinerMedicamentos = findViewById(R.id.listamedicamentos);
        spinerMedicamentos.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_list_item_1, new String[]{"Seleccione una opción", "Glucantime", "Miltefosine"}));
        spinerMedicamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    findViewById(R.id.label3).setVisibility(View.VISIBLE);
                    spinner_unidades.setVisibility(View.VISIBLE);
                    findViewById(R.id.label4).setVisibility(View.VISIBLE);
                    findViewById(R.id.calendar_container).setVisibility(View.VISIBLE);

                }
                if (i == 1) {
                    arrayUnidades.clear();
                    arrayUnidades.add("Cuántas ampollas");
                    arrayUnidades.add("1");
                    arrayUnidades.add("2");
                    arrayUnidades.add("3");
                    arrayUnidades.add("4");
                } else if (i == 2) {
                    arrayUnidades.clear();
                    arrayUnidades.add("Cuántas cápsulas");
                    arrayUnidades.add("1");
                    arrayUnidades.add("2");
                    arrayUnidades.add("3");
                    arrayUnidades.add("4");
                    arrayUnidades.add("5");
                    arrayUnidades.add("6");
                    arrayUnidades.add("7");
                    arrayUnidades.add("8");
                    arrayUnidades.add("9");
                }
                refreshOKButton();
                scrollDown();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                refreshOKButton();
            }
        });

        spinerPacientes = findViewById(R.id.listapacientes);
        spinerPacientes.setAdapter(adapterSpinnerPacientes);
        spinerPacientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    return;
                } else {
                    findViewById(R.id.label2).setVisibility(View.VISIBLE);
                    spinerMedicamentos.setVisibility(View.VISIBLE);
                    scrollDown();
                }
                refreshOKButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                refreshOKButton();
            }
        });
    }

    private void scrollDown() {

        scrollPlanner.post(
                () -> scrollPlanner.smoothScrollTo(0, scrollPlanner.getBottom() + 200)
        );


    }

    private void loadRaters() {
        arrayRaters.clear();
        arrayRaters.add("Seleccione un evaluador");
        arrayRaters.add("Andrés Navarro");
        arrayRaters.add("Domiciano Rincon");
        adapterSpinnerRaters.notifyDataSetChanged();

    }

    private void construirPrescripcion() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        Usuario usuario = new Usuario();
        usuario.id = UUID.randomUUID().toString();
        usuario.creatorUserId = UUID.randomUUID().toString();
        usuario.email = "";
        usuario.lastname = "Rincon";
        usuario.name = "Domiciano";
        usuario.writer = true;
        usuario.reader = true;
        usuario.updaterUserId = UUID.randomUUID().toString();
        usuario.nationalId = "1143848922";
        usuario.lastUpdatedDate = sdf.format(Calendar.getInstance().getTime());

        Evaluador evaluador = new Evaluador();
        evaluador.setNationalId(usuario.nationalId);
        try {
            evaluador.setLastLogin(sdf.parse(usuario.lastUpdatedDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        evaluador.setLastName(usuario.lastname);
        evaluador.setName(usuario.name);
        evaluador.setUuid(usuario.id);

        Paciente paciente = new Paciente();
        paciente.setBirthday(Calendar.getInstance().getTime());
        paciente.setCedula("11110000");
        paciente.setDocumentType("Cedula");
        paciente.setEvaluadorId(evaluador.getUuid());
        //paciente.setEvaluadorId(evaluador.getNationalId());
        paciente.setGenre("Maculino");
        paciente.setLastName("Cruz");
        paciente.setName("William");
        paciente.setUuid(UUID.randomUUID().toString());

        String plan = PlannerUtilities.crearDocumentoST(dias_tratamiento, date_inicio, evaluador, paciente);

        Log.e("PLAN", "" + plan);

        //1. Conseguir usuario por cedula
        WebserviceConsumer.GetUsuarioByCedula getUser = new WebserviceConsumer.GetUsuarioByCedula(evaluador.getNationalId());
        getUser.setObserver(obj -> {
            //2. No existe el usuario, entonces se agrega el usuario
            if (obj == null) {
                WebserviceConsumer.PostUser posteruser = new WebserviceConsumer.PostUser(new Gson().toJson(usuario));
                posteruser.setObserver(json -> {
                    if (json instanceof String) {
                        String code = (String) json;
                        switch (code) {
                            //4. Si se recibe un OK, el usuario ya exitste y se postea el docuemnto
                            case LeishConstants.OK:
                                WebserviceConsumer.PostDocument posterdoc = new WebserviceConsumer.PostDocument(plan);
                                posterdoc.setObserver(json1 -> {
                                    //FIN DEL PROCESO
                                });
                                posterdoc.start();
                                break;
                            //5. El proceso falla, enotnces se cancela
                            case LeishConstants.IOEX:
                                runOnUiThread(() -> Toast.makeText(Planner.this, "Ocurrió un problema", Toast.LENGTH_SHORT).show());
                                break;
                        }
                    }
                });
                posteruser.start();
            }
            //3. Si existe el usuario, entonces se postea el documento
            if (obj instanceof Usuario) {
                WebserviceConsumer.PostDocument poster = new WebserviceConsumer.PostDocument(plan);
                poster.setObserver(json -> {
                    //FIN DEL PROCESO
                });
                poster.start();
            }
        });
        getUser.start();


    }

    public void showCalendar() {
        DatePickerBuilder builder = new DatePickerBuilder(this, new OnSelectDateListener() {
            @Override
            public void onSelect(List<Calendar> calendars) {
                if (calendars.size() == 1) {
                    showCalendar();
                    Toast.makeText(Planner.this, "Seleccione un rango mayor a un sólo día", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar inicio = calendars.get(0);
                    Calendar fin = calendars.get(calendars.size() - 1);
                    dias_tratamiento = calendars.size();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    date_inicio = inicio.getTime();
                    fecha_inicio = sdf.format(inicio.getTime());
                    fecha_fin = sdf.format(fin.getTime());

                    ((TextView) findViewById(R.id.fecha_inicio)).setText("Inicio: " + fecha_inicio);
                    ((TextView) findViewById(R.id.fecha_fin)).setText("Fin: " + fecha_fin);
                    ((TextView) findViewById(R.id.totaldias)).setText(dias_tratamiento + " días");
                }
                refreshOKButton();
            }
        }).pickerType(CalendarView.RANGE_PICKER)
                .minimumDate(Calendar.getInstance())
                .headerColor(R.color.orange_background);
        DatePicker datePicker = builder.build();
        datePicker.show();
    }

    public void refreshOKButton() {
        String paciente = (String) spinerPacientes.getSelectedItem();
        String medicamento = (String) spinerMedicamentos.getSelectedItem();
        String unidades = (String) spinner_unidades.getSelectedItem();

        if (paciente.startsWith("Seleccione") || medicamento.startsWith("Seleccione") || unidades.startsWith("Cuántas") || dias_tratamiento <= 0) {
            findViewById(R.id.label5).setVisibility(View.GONE);
            findViewById(R.id.resumen).setVisibility(View.GONE);
            findViewById(R.id.ok_planner).setEnabled(false);
        } else {
            findViewById(R.id.label5).setVisibility(View.VISIBLE);
            findViewById(R.id.resumen).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.resumen)).setText("El tratamiento escogido durará " + dias_tratamiento + " días. Serán " + unidades + " unidades de " + medicamento + " empezando desde el día " + fecha_inicio + " y terminiando el día " + fecha_fin);
            findViewById(R.id.ok_planner).setEnabled(true);
            scrollDown();

        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage("¿Desea salir y perder el avance?")
                .setTitle("Salir")
                .setPositiveButton("Salir", (dialogInterface, i) -> {
                    finish();
                    dialogInterface.dismiss();
                    dialogInterface.cancel();
                })
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    dialogInterface.cancel();
                });
        builder.create().show();
    }

    public static class PlannerUtilities {

        private static String crearDocumentoST(int diasTratamiento, Date inicio, Evaluador evaluador, Paciente paciente) {
            int DIAS_TOTALES = 182;
            int UNIDADES = 4;
            String MEDICAMENTO = "Glucantime";

            //El rater no tiene Schemas
            Calendar c = Calendar.getInstance();
            c.setTime(inicio);
            c.add(Calendar.DATE, diasTratamiento);
            Date finTratamiento = c.getTime();

            c.setTime(inicio);
            c.add(Calendar.DATE, DIAS_TOTALES);
            Date finTotal = c.getTime();


            Schema schema = new Schema();
            schema.setDateStart(inicio);
            schema.setDateEnd(finTotal);
            schema.setPatientId(paciente.getUuid());
            schema.setEvaluadorId(evaluador.getUuid());
            schema.setActive("true");
            schema.setUuid(UUID.randomUUID().toString());

            ListaDailySchemas listaDailySchemas = new ListaDailySchemas();
            ArrayList<DailySchema> arrayDailySchemas = new ArrayList<>();
            for (int i = 0; i < DIAS_TOTALES; i++) {
                if (i < diasTratamiento) {
                    DailySchema daily = new DailySchema();
                    c.setTime(inicio);
                    c.add(Calendar.DATE, i);
                    daily.setDateOfTreatment(c.getTime());
                    daily.setDayOfTreatment("" + i);
                    daily.setFlag(false);
                    daily.setUuid(UUID.randomUUID().toString());
                    daily.setSchemaId(schema.getUuid());

                    //Prescripción
                    ListaPrescripciones listaPrescripciones = new ListaPrescripciones();
                    ArrayList<Prescripcion> arrayPrescripciones = new ArrayList<>();
                    Prescripcion prescripcion = new Prescripcion();
                    prescripcion.setComentarios("NULL");
                    prescripcion.setDailySchemaId(daily.getUuid());
                    prescripcion.setDosis(UNIDADES + "@Ampollas@" + UNIDADES + " " + ((MEDICAMENTO.equalsIgnoreCase("glucantime")) ? "Ampollas" : "Cápsulas") + " de 5ml por administración vía intramuscular");
                    prescripcion.setName(MEDICAMENTO);
                    prescripcion.setNumeroLote("NULL");
                    prescripcion.setUuid(UUID.randomUUID().toString());


                    //ScheduleTaking
                    ListaScheduleTaking listaScheduleTaking = new ListaScheduleTaking();
                    ArrayList<ScheduleTaking> arrayScheduleTaking = new ArrayList<>();
                    ScheduleTaking scheduleTaking = new ScheduleTaking();
                    scheduleTaking.setDate(c.getTime());
                    scheduleTaking.setUuid(UUID.randomUUID().toString());
                    scheduleTaking.setTime("16:00:00");
                    scheduleTaking.setPrescriptionId(prescripcion.getUuid());

                    arrayScheduleTaking.add(scheduleTaking);
                    listaScheduleTaking.setSchedulesTaking(arrayScheduleTaking);
                    prescripcion.setDosisTomadas(listaScheduleTaking);

                    //BasicAdverseEvents
                    BasicAdverseEvent eventosBasicos = new BasicAdverseEvent();
                    eventosBasicos.setDiarrea(0);
                    eventosBasicos.setDolorAbdominal(0);
                    eventosBasicos.setDolorArticulaciones(0);
                    eventosBasicos.setDolorCabeza(0);
                    eventosBasicos.setDolorMuscular(0);
                    eventosBasicos.setFiebre(0);
                    eventosBasicos.setUuid(UUID.randomUUID().toString());
                    eventosBasicos.setInfeccionSitio(0);
                    eventosBasicos.setMalestarGeneral(0);
                    eventosBasicos.setMareo(0);
                    eventosBasicos.setName("NULL");
                    eventosBasicos.setNauseas(0);
                    eventosBasicos.setPalpitaciones(0);
                    eventosBasicos.setPerdidaApetito(0);
                    eventosBasicos.setPrescriptionId(prescripcion.getUuid());
                    eventosBasicos.setVomito(0);

                    prescripcion.setEventosBasicos(eventosBasicos);

                    arrayPrescripciones.add(prescripcion);
                    listaPrescripciones.setPrescripciones(arrayPrescripciones);
                    daily.setPrescripciones(listaPrescripciones);


                    ListaUlcerForms listaUlcerForms = new ListaUlcerForms();
                    ArrayList<UlcerForm> arrayUlcerForms = new ArrayList<>();
                    UlcerForm ulcerForm = new UlcerForm();
                    ulcerForm.setDailySchemaId(daily.getUuid());
                    ulcerForm.setDate(c.getTime());
                    ulcerForm.setNoteGeneral("NULL");
                    ulcerForm.setUiid(UUID.randomUUID().toString());

                    arrayUlcerForms.add(ulcerForm);
                    listaUlcerForms.setUlcerForms(arrayUlcerForms);
                    daily.setImagenes(listaUlcerForms);

                    arrayDailySchemas.add(daily);
                } else {
                    DailySchema daily = new DailySchema();
                    c.setTime(inicio);
                    c.add(Calendar.DATE, i);
                    daily.setDateOfTreatment(c.getTime());
                    daily.setDayOfTreatment("" + i);
                    daily.setFlag(false);
                    daily.setUuid(UUID.randomUUID().toString());
                    daily.setSchemaId(schema.getUuid());

                    ListaUlcerForms listaUlcerForms = new ListaUlcerForms();
                    ArrayList<UlcerForm> arrayUlcerForms = new ArrayList<>();
                    UlcerForm ulcerForm = new UlcerForm();
                    ulcerForm.setDailySchemaId(daily.getUuid());
                    ulcerForm.setDate(c.getTime());
                    ulcerForm.setNoteGeneral("NULL");
                    ulcerForm.setUiid(UUID.randomUUID().toString());

                    arrayUlcerForms.add(ulcerForm);
                    listaUlcerForms.setUlcerForms(arrayUlcerForms);
                    daily.setImagenes(listaUlcerForms);

                    arrayDailySchemas.add(daily);
                }
            }
            listaDailySchemas.setDailySchemas(arrayDailySchemas);

            schema.setListaDailySchemas(listaDailySchemas);

            ListaSchemas listaSchemas = new ListaSchemas();
            ArrayList<Schema> arraySchemas = new ArrayList<>();
            arraySchemas.add(schema);
            listaSchemas.setSchemas(arraySchemas);
            paciente.setListaSchemas(listaSchemas);

            ListaPacientes listaPacientes = new ListaPacientes();
            ArrayList<Paciente> arrayPacientes = new ArrayList<>();
            arrayPacientes.add(paciente);
            listaPacientes.setPacientes(arrayPacientes);
            evaluador.setPacienteLista(listaPacientes);


            GsonBuilder build = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
            Gson gson = build.create();
            String json = gson.toJson(evaluador);
            return json;
        }

    }

    public class PacientesAdapter extends BaseAdapter {
        ArrayList<String> pacientes;
        Context context;

        public PacientesAdapter(Context context, ArrayList<String> pacientes) {
            this.context = context;
            this.pacientes = pacientes;
        }

        @Override
        public int getCount() {
            return pacientes.size();
        }

        @Override
        public Object getItem(int i) {
            return pacientes.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.simple_list_item_1_action, null, false);
            TextView txtPaciente = v.findViewById(R.id.txt_paciente);
            txtPaciente.setText(pacientes.get(i));
            TextView btnEliminar = v.findViewById(R.id.btn_eliminar);
            btnEliminar.setOnClickListener(view1 -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Confirmar")
                        .setMessage("¿Está seguro que desea eliminar de la lista al paciente " + pacientes.get(i) + "?")
                        .setNegativeButton("No", (dialogInterface, i1) -> {
                            dialogInterface.dismiss();
                            dialogInterface.cancel();
                        })
                        .setPositiveButton("Si", (dialogInterface, i12) -> {
                            pacientes.remove(i);
                            notifyDataSetChanged();
                        });
                builder.show();
            });
            return v;
        }
    }

}
