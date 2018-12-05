package icesi.i2t.leishmaniasisst;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import icesi.i2t.leishmaniasisst.data.ManejadorBD;
import icesi.i2t.leishmaniasisst.dialogs.AmpollasAdministradasDialog;
import icesi.i2t.leishmaniasisst.dialogs.BooleanAnswerDialog;
import icesi.i2t.leishmaniasisst.dialogs.ConfirmacionDialog;
import icesi.i2t.leishmaniasisst.dialogs.PastillasTomadasDialog;
import icesi.i2t.leishmaniasisst.calendario.DayOnGridModel;
import icesi.i2t.leishmaniasisst.calendario.GridCalendarioAdapter;
import icesi.i2t.leishmaniasisst.model.DailySchema;
import icesi.i2t.leishmaniasisst.model.ListaScheduleTaking;
import icesi.i2t.leishmaniasisst.model.Paciente;
import icesi.i2t.leishmaniasisst.model.Prescripcion;
import icesi.i2t.leishmaniasisst.model.ScheduleTaking;
import icesi.i2t.leishmaniasisst.model.Schema;

public class CalendarioRevisionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    int numero_titulo = 0;
    int index_medicamento = 0;

    static final String[] MESES_DEL_ANIO = new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Obtubre", "Noviembre", "Diciembre"};

    String fecha_inicio = "2016-10-01";
    String fecha_fin = "2016-11-11";


    Button siguiente_calendario;

    ScrollView calendar_content;
    TextView calendario_titulo;
    String[] medicamento = new String[]{"Glucantime", "Miltefosine"};

    int display_width = 0, display_height = 0;

    ManejadorBD db;

    Paciente paciente;

    List<DailySchema> lista_dayliSchemas;

    String dosis;

    SimpleDateFormat formato;
    ArrayList<DayOnGridModel> dias;

    HashMap<Button, Integer> botones;
    HashMap<Integer, Button> senotob;

    LinearLayout padre_calendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canlendario_revision);


        Log.e("<--SP-->", PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "-"));


        formato = new SimpleDateFormat("yyyy-MM-dd");
        dias = new ArrayList<>();
        botones = new HashMap<>();
        senotob = new HashMap<>();

        db = new ManejadorBD(this);
        String id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "");
        paciente = db.buscarPaciente(id);

        Schema schema_activo = db.buscarSchemaActivoDelPaciente(paciente);
        lista_dayliSchemas = new ArrayList<>();

        String dosis_raw = db.getDosisPaciente(paciente.getUuid());
        String[] dosis_detail = dosis_raw.split("@");
        dosis = dosis_detail[0];
        //Toast.makeText(this, dosis, Toast.LENGTH_SHORT).show();

        if (dosis.isEmpty()) {
            dosis = "1";
        }

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        cargar_info();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_calendario_revision);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        display_height = displayMetrics.heightPixels;
        display_width = displayMetrics.widthPixels;

        siguiente_calendario = (Button) findViewById(R.id.siguiente_calendario);
        calendar_content = (ScrollView) findViewById(R.id.calendar_content);
        calendario_titulo = (TextView) findViewById(R.id.calendario_titulo);
        padre_calendario = (LinearLayout) findViewById(R.id.padre_calendario);
        calendario_titulo.setText(numero_titulo + ". Administración de " + medicamento[index_medicamento]);

        hide_siguiente();
        getFechaInicio();
        generarCalendario();


        //Set flags
        setFlagsDStoCalendars();
        show_siguiente();


    }

    private void generarCalendario() {


        int mes_actual = -1;
        int contador_dias = 0;
        LinearLayout fila_actual = null;
        int iteraciones = 0;
        boolean etiqueta = false;

        int indice = 0;

        try {

            //INSTANCIA DEL PADRE
            LinearLayout padre_calendario = (LinearLayout) findViewById(R.id.padre_calendario);

            //FECHA
            Date d = formato.parse(fecha_inicio);
            Calendar c = Calendar.getInstance();
            c.setTime(d);

            Calendar fin = Calendar.getInstance();
            fin.setTime(formato.parse(fecha_fin));

            Schema schema = db.buscarSchemaActivoDelPaciente(paciente);

            while (c.before(fin) || mismoDia(c.getTime(), fin.getTime())) {
                if (mes_actual != c.get(Calendar.MONTH)) {
                    mes_actual = c.get(Calendar.MONTH);
                    //PONER ETIQUETA
                    TextView tv = new TextView(this);
                    tv.setText(MESES_DEL_ANIO[c.get(Calendar.MONTH)] + "/" + c.get(Calendar.YEAR));
                    tv.setTypeface(null, Typeface.ITALIC);
                    tv.setTextColor(Color.WHITE);
                    tv.setTextSize(16);
                    LinearLayout.LayoutParams params_tv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params_tv.setMargins(80, 0, 0, 0);
                    tv.setLayoutParams(params_tv);
                    padre_calendario.addView(tv);
                    etiqueta = true;
                }

                if (contador_dias == 0 || etiqueta) {
                    //PONER FILA
                    etiqueta = false;
                    contador_dias = 0;

                    LinearLayout l = new LinearLayout(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(50, 0, 50, 0);
                    l.setLayoutParams(params);
                    fila_actual = l;
                    padre_calendario.addView(l);
                }

                //PONER CADA BOTON
                Button b = new Button(this);
                b.setText("" + c.get(Calendar.DAY_OF_MONTH));
                LinearLayout.LayoutParams params_b = new LinearLayout.LayoutParams(((display_width - 100) / 5) - 10, ((display_width - 100) / 5) - 10);
                params_b.setMargins(5, 5, 5, 5);
                b.setLayoutParams(params_b);
                b.setTextSize(20);

                if (mismoDia(c.getTime(), Calendar.getInstance().getTime())) {
                    b.setTextColor(Color.rgb(226, 97, 0));
                    b.setBackgroundColor(Color.rgb(239, 175, 99));
                } else if (c.before(Calendar.getInstance())) {
                    b.setTextColor(Color.rgb(226, 97, 0));
                    b.setBackgroundColor(Color.rgb(239, 175, 99));
                } else if (c.after(Calendar.getInstance())) {
                    b.setBackgroundColor(Color.rgb(0xEA, 0x97, 0x33));
                    b.setTextColor(Color.rgb(0xEE, 0xAC, 0x5C));
                }

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Button b = (Button) v;
                        int index = botones.get(b);
                        DayOnGridModel day = dias.get(index);

                        //if(day.getMedicamento_tomado() == DayOnGridModel.NO_EVALUADO){
                        Date time = day.getCalendar().getTime();
                        Calendar hoy = Calendar.getInstance();

                        if (time.before(hoy.getTime()) || mismoDia(day.getCalendar().getTime(), hoy.getTime())) {
                            if (index_medicamento == 0) {
                                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                AmpollasAdministradasDialog dialog =
                                        AmpollasAdministradasDialog
                                                .newInstance(day.getCalendar().get(Calendar.DAY_OF_MONTH),
                                                        MESES_DEL_ANIO[day.getCalendar().get(Calendar.MONTH)],
                                                        Integer.parseInt(dosis));
                                dialog.show(ft, "dialog_ampollas");

                                final DayOnGridModel finalDay = day;
                                dialog.setOnDialogDismiss(new AmpollasAdministradasDialog.OnDialogDismiss() {
                                    @Override
                                    public void finish(String resultado) {
                                        int ampollas = Integer.parseInt(resultado);
                                        finalDay.setUnidades_tomadas(ampollas);
                                        if (ampollas > 0) {
                                            finalDay.setMedicamento_tomado(DayOnGridModel.MEDICAMENTO_TOMADO);
                                        } else {
                                            finalDay.setMedicamento_tomado(DayOnGridModel.MEDICAMENTO_NO_TOMADO);
                                        }
                                        refreshCalendar();
                                        show_siguiente();
                                    }
                                });
                            } else if (index_medicamento == 1) {
                                final DayOnGridModel finalDay = day;
                                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                PastillasTomadasDialog dialog = PastillasTomadasDialog.newInstance(day.getCalendar().get(Calendar.DAY_OF_MONTH), MESES_DEL_ANIO[day.getCalendar().get(Calendar.MONTH)], Integer.parseInt(dosis));
                                dialog.show(ft, "dialog_pastillas");
                                dialog.setOnDialogDismiss(new PastillasTomadasDialog.OnDialogDismiss() {
                                    @Override
                                    public void finish(String resultado) {
                                        int pastillas = Integer.parseInt(resultado);
                                        finalDay.setUnidades_tomadas(pastillas);
                                        if (pastillas > 0) {
                                            finalDay.setMedicamento_tomado(DayOnGridModel.MEDICAMENTO_TOMADO);
                                        } else {
                                            finalDay.setMedicamento_tomado(DayOnGridModel.MEDICAMENTO_NO_TOMADO);
                                        }
                                        refreshCalendar();
                                        show_siguiente();

                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "No puede evaluar este día", Toast.LENGTH_SHORT).show();
                        }

                        /*
                        }else {
                            Toast.makeText(getApplicationContext(), "Ya evaluó este día", Toast.LENGTH_SHORT).show();
                        }
                        */

                    }
                });

                fila_actual.addView(b);

                botones.put(b, indice);
                senotob.put(indice, b);
                lista_dayliSchemas.add(db.buscarDailySchema(schema.getUuid(), c.getTime()));

                indice++;
                dias.add(new DayOnGridModel((Calendar) c.clone()));
                c.add(Calendar.DAY_OF_MONTH, 1);


                contador_dias++;
                if (contador_dias == 5)
                    contador_dias = 0;


                iteraciones++;
            }

            View v = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 20, 20, 20);
            v.setLayoutParams(params);
            padre_calendario.addView(v);


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void refreshCalendar() {
        for (int i = 0; i < dias.size(); i++) {
            Date date = dias.get(i).getCalendar().getTime();
            Calendar c = Calendar.getInstance();
            if (mismoDia(dias.get(i).getCalendar().getTime(), c.getTime())) {
                senotob.get(i).setText("" + dias.get(i).getCalendar().get(Calendar.DAY_OF_MONTH));
                senotob.get(i).setTextColor(Color.rgb(226, 97, 0));
                senotob.get(i).setBackgroundColor(Color.rgb(239, 175, 99));
            } else if (date.before(c.getTime())) {
                senotob.get(i).setText("" + dias.get(i).getCalendar().get(Calendar.DAY_OF_MONTH));
                senotob.get(i).setTextColor(Color.rgb(226, 97, 0));
                senotob.get(i).setBackgroundColor(Color.rgb(239, 175, 99));
            } else if (date.after(c.getTime())) {
                senotob.get(i).setText("" + dias.get(i).getCalendar().get(Calendar.DAY_OF_MONTH));
            }

            if (mismoDia(dias.get(i).getCalendar().getTime(), c.getTime()) || date.before(c.getTime())) {
                if (dias.get(i).getMedicamento_tomado() == DayOnGridModel.MEDICAMENTO_NO_TOMADO) {
                    senotob.get(i).setBackgroundResource(R.mipmap.img_dosis_no_tomada);
                    senotob.get(i).setText("");
                } else if (dias.get(i).getMedicamento_tomado() == DayOnGridModel.MEDICAMENTO_TOMADO) {
                    senotob.get(i).setBackgroundResource(R.mipmap.img_dosis_tomada);
                    senotob.get(i).setText("");
                }
            }
        }
    }

    private boolean mismoDia(Date hoy, Date date) {
        Calendar hoy_c = Calendar.getInstance();
        hoy_c.setTime(hoy);

        Calendar date_c = Calendar.getInstance();
        date_c.setTime(date);

        boolean a = hoy_c.get(Calendar.YEAR) == date_c.get(Calendar.YEAR);
        boolean b = hoy_c.get(Calendar.MONTH) == date_c.get(Calendar.MONTH);
        boolean c = hoy_c.get(Calendar.DAY_OF_MONTH) == date_c.get(Calendar.DAY_OF_MONTH);

        return a && b && c;

    }

    private void cargar_info() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        index_medicamento = prefs.getInt("medicamento", 0);
        numero_titulo = prefs.getInt("numero_titulo", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_evaluacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cancel_eval) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            BooleanAnswerDialog dialog = BooleanAnswerDialog.newInstance("¿Está seguro que desea cancelar la evaluación en curso?");
            dialog.show(ft, "dialog_fiebre");
            dialog.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
                @Override
                public void finish(String salida) {
                    if (salida.equals("SI")) {
                        Intent intent = new Intent(getApplicationContext(), Evaluacion.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DayOnGridModel day = null;

        /*
        if(parent.getAdapter().equals(adaptador1)){
            day = (DayOnGridModel) adaptador1.getItem(position);
        }else if(parent.getAdapter().equals(adaptador2)){
            day = (DayOnGridModel) adaptador2.getItem(position);
        }
        */


    }

    public void doSiguiente(View v) {


        Intent i = new Intent(this, SintomasActivity.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putInt("numero_titulo", numero_titulo + 1).commit();
        startActivity(i);

        //borrarSchedulesTakings();
        setInfoToDS2();

    }

    private void calcularDiasTranscurridos() {

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Schema activo = db.buscarSchemaActivoDelPaciente(paciente);

        Calendar c = Calendar.getInstance();
        DailySchema ds = db.buscarDailySchema(activo.getUuid(), c.getTime());
        DayOnGridModel day = getDayModelByDS(ds);
        int index = dias.indexOf(day);

        //TODO: Revisar esto
        String paciente_id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "");
        if (PreferenceManager.getDefaultSharedPreferences(this).getString("DIAS" + paciente_id, "-").equals("-")) {
            int numero = 0;
            for (int i = 0; i <= index; i++) {
                if (dias.get(i).getMedicamento_tomado() == DayOnGridModel.NO_EVALUADO)
                    numero++;
            }

            if (numero == 0) numero = 1;

            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("DIAS" + paciente_id, "" + numero).commit();
        }

        Log.e("<<1>>", "DIAS" + paciente_id);
        Log.e("<<2>>", PreferenceManager.getDefaultSharedPreferences(this).getString("DIAS" + paciente_id, "-"));

    }

    public void show_siguiente() {

        boolean revision_medicamento_completa = true;
        Calendar c = Calendar.getInstance();

        for (int i = 0; i < dias.size(); i++) {
            if (dias.get(i).getCalendar().before(c)
                    || GridCalendarioAdapter.isToday(dias.get(i).getCalendar().getTime(), c.getTime())) {

                if (dias.get(i).getMedicamento_tomado() == DayOnGridModel.NO_EVALUADO) {
                    revision_medicamento_completa = false;
                    break;
                } else {
                    continue;
                }
            }
        }


        if (revision_medicamento_completa) {
            siguiente_calendario.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) calendar_content.getLayoutParams();
            params.setMargins(0, 110, 0, 110);
            calendar_content.setLayoutParams(params);
        }


    }

    public void hide_siguiente() {
        siguiente_calendario.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) calendar_content.getLayoutParams();
        params.setMargins(0, 110, 0, 0);
        calendar_content.setLayoutParams(params);
    }

    boolean control = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (control) {
            control = false;

            //calcularNumeroColumnas();
            refreshCalendar();
            calcularDiasTranscurridos();
            calcularFechaInicio();
        }
    }


    public void setFlagsDStoCalendars() {
        for (int i = 0; i < lista_dayliSchemas.size(); i++) {
            final DayOnGridModel d = getDayModelByDS(lista_dayliSchemas.get(i));
            if (d == null) {
                Log.e("ERROR", "El dailySchema de ID:" + lista_dayliSchemas.get(i).getUuid() + " no tiene modelo gráfico!");
                return;
            }
            if (!lista_dayliSchemas.get(i).isFlag()) {
                d.setMedicamento_tomado(DayOnGridModel.NO_EVALUADO);
            } else {
                List<Prescripcion> lista_p = db.getListaPrescripciones(lista_dayliSchemas.get(i).getUuid());
                if (lista_p != null) {
                    if (lista_p.size() != 0) {
                        Prescripcion prescripcion = lista_p.get(0);
                        if (prescripcion != null) {
                            List<ScheduleTaking> lista_st = db.getListaSheduleTaking(prescripcion.getUuid());
                            if (lista_st.size() != 0) {
                                ScheduleTaking scheduleTaking = lista_st.get(0);
                                if (scheduleTaking == null)
                                    d.setMedicamento_tomado(DayOnGridModel.MEDICAMENTO_NO_TOMADO);
                                else d.setMedicamento_tomado(DayOnGridModel.MEDICAMENTO_TOMADO);
                            } else {
                                d.setMedicamento_tomado(DayOnGridModel.MEDICAMENTO_NO_TOMADO);
                            }
                        } else {
                            d.setMedicamento_tomado(DayOnGridModel.MEDICAMENTO_NO_TOMADO);
                        }
                    } else {
                        d.setMedicamento_tomado(DayOnGridModel.MEDICAMENTO_NO_TOMADO);
                    }
                }
            }
        }


    }

    public ArrayList<DailySchema> setInfoToDS() {
        ArrayList<DailySchema> dailySchemas = new ArrayList<>();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");

        Schema schema_activo = db.buscarSchemaActivoDelPaciente(paciente);
        Calendar hoy = Calendar.getInstance();

        //boolean flag = false;
        while (true) {
            DailySchema d = db.buscarDailySchema(schema_activo.getUuid(), hoy.getTime());
            if (d == null) {
                break;
            }
            if (!d.isFlag()) {
                dailySchemas.add(d);
                DayOnGridModel day = getDayModelByDS(d);
                if (day == null) break;
                if (day.getMedicamento_tomado() == DayOnGridModel.MEDICAMENTO_TOMADO) {
                    List<Prescripcion> lista_p = db.getListaPrescripciones(d.getUuid());
                    if (lista_p == null) {
                        hoy.add(Calendar.DAY_OF_MONTH, -1);
                        continue;
                    } else if (lista_p.size() == 0) {
                        hoy.add(Calendar.DAY_OF_MONTH, -1);
                        continue;
                    }

                    Prescripcion p = d.getPrescripciones().getPrescripciones().get(0);
                    p.setNumeroLote("" + day.getUnidades_tomadas());
                    db.editarPrescripcion(p);

                    PreferenceManager.getDefaultSharedPreferences(this).edit().putString("numero_unidades", "" + day.getUnidades_tomadas()).commit();

                    ScheduleTaking st = new ScheduleTaking(UUID.randomUUID().toString(), hora.format(Calendar.getInstance().getTime()), d.getDateOfTreatment(), lista_p.get(0).getUuid());
                    db.agregarScheduleTaking(st);


                    //PRUEBAS
                    //ScheduleTaking prueba = db.buscarScheduleTaking(st.getUuid());
                    //DailySchema d_prueba = db.buscarDailySchema(schema_activo.getUuid(), hoy.getTime());
                    //String a = prueba.getTime();
                }
                db.setDailySchemaFlag(d.getUuid());
            }
            hoy.add(Calendar.DAY_OF_MONTH, -1);
        }
        return dailySchemas;
    }


    public void limpiarSchedules() {
        Schema schema_activo = db.buscarSchemaActivoDelPaciente(paciente);
        Calendar hoy = Calendar.getInstance();

        while (true) {
            DailySchema d = db.buscarDailySchema(schema_activo.getUuid(), hoy.getTime());

            if (d == null) {
                break;
            }

            List<Prescripcion> lista_p = db.getListaPrescripciones(d.getUuid());
            if (lista_p == null) {
                hoy.add(Calendar.DAY_OF_MONTH, -1);
                continue;
            }
            if (lista_p.size() == 0) {
                hoy.add(Calendar.DAY_OF_MONTH, -1);
                continue;
            }
            if (lista_p.get(0) == null) {
                hoy.add(Calendar.DAY_OF_MONTH, -1);
                continue;
            }

            List<ScheduleTaking> sts = db.getListaSheduleTaking(lista_p.get(0).getUuid());

            if (sts == null) {
                hoy.add(Calendar.DAY_OF_MONTH, -1);
                continue;
            }
            if (sts.size() == 0) {
                hoy.add(Calendar.DAY_OF_MONTH, -1);
                continue;
            }
            if (sts.get(0) == null) {
                hoy.add(Calendar.DAY_OF_MONTH, -1);
                continue;
            }

            db.deleteScheduleTaking(sts.get(0).getUuid());
            hoy.add(Calendar.DAY_OF_MONTH, -1);
        }
    }


    public ArrayList<DailySchema> setInfoToDS2() {

        limpiarSchedules();

        ArrayList<DailySchema> dailySchemas = new ArrayList<>();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");

        Schema schema_activo = db.buscarSchemaActivoDelPaciente(paciente);
        Calendar hoy = Calendar.getInstance();

        //boolean flag = false;
        while (true) {
            DailySchema d = db.buscarDailySchema(schema_activo.getUuid(), hoy.getTime());
            if (d == null) {
                break;
            }

            dailySchemas.add(d);
            DayOnGridModel day = getDayModelByDS(d);
            if (day == null) break;
            if (day.getMedicamento_tomado() == DayOnGridModel.MEDICAMENTO_TOMADO) {
                List<Prescripcion> lista_p = db.getListaPrescripciones(d.getUuid());
                if (lista_p == null) {
                    hoy.add(Calendar.DAY_OF_MONTH, -1);
                    continue;
                } else if (lista_p.size() == 0) {
                    hoy.add(Calendar.DAY_OF_MONTH, -1);
                    continue;
                }

                Prescripcion p = d.getPrescripciones().getPrescripciones().get(0);
                p.setNumeroLote("" + day.getUnidades_tomadas());
                db.editarPrescripcion(p);

                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("numero_unidades", "" + day.getUnidades_tomadas()).commit();

                ScheduleTaking st = new ScheduleTaking(UUID.randomUUID().toString(), hora.format(Calendar.getInstance().getTime()), d.getDateOfTreatment(), lista_p.get(0).getUuid());
                db.agregarScheduleTaking(st);


                //PRUEBAS
                //ScheduleTaking prueba = db.buscarScheduleTaking(st.getUuid());
                //DailySchema d_prueba = db.buscarDailySchema(schema_activo.getUuid(), hoy.getTime());
                //String a = prueba.getTime();
            }
            db.setDailySchemaFlag(d.getUuid());

            hoy.add(Calendar.DAY_OF_MONTH, -1);
        }
        return dailySchemas;
    }

    private DayOnGridModel getDayModelByDS(DailySchema d) {
        for (int i = 0; i < dias.size(); i++) {
            Date dia = dias.get(i).getCalendar().getTime();
            String dia_str = formato.format(dia);
            if (formato.format(d.getDateOfTreatment()).equals(dia_str)) {
                return dias.get(i);
            }
        }

        return null;
    }

    public void calcularFechaInicio() {


        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        String fecha_fin = formato.format(c.getTime());
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("fecha_fin", fecha_fin).commit();

        String id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "");
        String dias_transcurridos = PreferenceManager.getDefaultSharedPreferences(this).getString("DIAS" + id, "0");
        int int_dias_transcurridos = Integer.parseInt(dias_transcurridos);

        c.add(Calendar.DAY_OF_MONTH, -int_dias_transcurridos + 1);
        String fecha_inicio = formato.format(c.getTime());
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("fecha_inicio", fecha_inicio).commit();


    }

    public void getFechaInicio() {

        Schema activo = db.buscarSchemaActivoDelPaciente(paciente);
        List<DailySchema> lista = db.getListaDailySchemas(activo.getUuid());

        int index_cut = 0;

        for (int i = 0; i < lista.size(); i++) {
            DailySchema ds = lista.get(i);
            List<Prescripcion> p = db.getListaPrescripciones(ds.getUuid());
            if (p.size() == 0) continue;
            else {
                fecha_inicio = formato.format(ds.getDateOfTreatment());
                index_cut = i;
                break;
            }
        }

        for (int i = index_cut; i < lista.size(); i++) {
            DailySchema ds = lista.get(i);
            List<Prescripcion> p = db.getListaPrescripciones(ds.getUuid());
            if (p.size() != 0) continue;
            else {
                fecha_fin = formato.format(ds.getDateOfTreatment());
                break;
            }
        }


    }


}
