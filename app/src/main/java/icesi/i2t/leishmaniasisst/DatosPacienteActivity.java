package icesi.i2t.leishmaniasisst;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import icesi.i2t.leishmaniasisst.data.ManejadorBD;
import icesi.i2t.leishmaniasisst.dialogs.BooleanAnswerDialog;
import icesi.i2t.leishmaniasisst.dialogs.ConfirmacionDialog;
import icesi.i2t.leishmaniasisst.model.DailySchema;
import icesi.i2t.leishmaniasisst.model.Paciente;
import icesi.i2t.leishmaniasisst.model.Schema;

public class DatosPacienteActivity extends AppCompatActivity {

    static final int MEDICAMENTO_GLUCANTIME=0;
    static final int MEDICAMENTO_MILTEFOSINE=1;

    static final int OVERVIEW_EVALUACION=0;
    static final int OVERVIEW_FOTOS=1;

    public static final String[] meses_del_anio = new String[]{
                            "Enero","Febrero","Marzo","Abril",
                            "Mayo","Junio","Julio","Agosto",
                            "Septiembre","Octubre","Noviembre","Diciembref"};

    String nombre="Susanita Perez", edad="38", sexo="Mujer",
            documento="1143848922", residencia="Villa Hermosa";

    String medicamento="Glucantime", tipo_administracion="Intramuscular",
            dosis="4 ampollas de 5cc", fecha_inicio="30 de Enero/2016";

    int dia_actual=8, semana=1, total_dias=20;

    TextView datos_paciente_nombre, datos_paciente_edad, datos_paciente_sexo, datos_paciente_documento;
    TextView datos_paciente_dia_actual, datos_paciente_semana_actual, datos_paciente_medicamento, datos_paciente_tipo_administracion, datos_paciente_dosis, datos_paciente_fecha_inicio;

    TextView tipo_admin_o_info, datos_paciente_dosis_subtitulo;
    Button datos_paciente_boton_ok;

    String[] tipos = new String[]{"Tipo de administración:","Informacion adicional:"};
    int tipo_ventana = 0;
    ManejadorBD db;

    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(this, Evaluacion.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_paciente);


        if (getIntent().getExtras() != null) {
            tipo_ventana = getIntent().getExtras().getInt("tipo_ventana", 0);
        }

        try{

            Log.e("<--SP-->",PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id","-"));

            db = new ManejadorBD(this);
            String id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "");
            Paciente p = db.buscarPaciente(id);

            Log.e("<--VAR-->", id);
            nombre = p.getName() + " " + p.getLastName();
            edad = date2edad(p.getBirthday());
            sexo = p.getGenre();
            documento = p.getCedula();
            medicamento = db.buscarMedicamentosDelPaciente(p.getUuid());
            if (!medicamento.equals("Miltefosine")) tipo_administracion = "Intramuscular";
            else tipo_administracion = "Vía Oral";

            Date fecha_cero = db.getFechaDeInicioPaciente(p.getUuid());
            Calendar c = Calendar.getInstance();
            c.setTime(fecha_cero);
            fecha_inicio = c.get(Calendar.DAY_OF_MONTH) + " de "
                    + meses_del_anio[c.get(Calendar.MONTH)] + "/" + c.get(Calendar.YEAR);

            String dosis_raw = db.getDosisPaciente(p.getUuid());
            String[] dosis_detail = dosis_raw.split("@");

            dosis = dosis_detail[2];

            if(tipo_ventana==1){
                dia_actual = Integer.parseInt(db.getNumeroUlcerFormsEnDailySchemas(p));
                total_dias = Integer.parseInt(db.getTotalDiasDeFotos(p));
            }
            else{
                dia_actual = Integer.parseInt(db.getDayofTreatment(p));
                total_dias = db.getTotalDays(p);
            }
            semana = 1 + ((dia_actual - 1) / 7);

        }catch (Exception e){
            Log.e("ERROR",e.getLocalizedMessage());
        }

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_datos_paciete);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        datos_paciente_nombre = (TextView) findViewById(R.id.datos_paciente_nombre);
        datos_paciente_edad = (TextView) findViewById(R.id.datos_paciente_edad);
        datos_paciente_sexo = (TextView) findViewById(R.id.datos_paciente_sexo);
        datos_paciente_documento = (TextView) findViewById(R.id.datos_paciente_documento);


        datos_paciente_dia_actual = (TextView) findViewById(R.id.datos_paciente_dia_actual);
        datos_paciente_semana_actual = (TextView) findViewById(R.id.datos_paciente_semana_actual);
        datos_paciente_medicamento = (TextView) findViewById(R.id.datos_paciente_medicamento);
        datos_paciente_tipo_administracion = (TextView) findViewById(R.id.datos_paciente_tipo_administracion);



        datos_paciente_dosis = (TextView) findViewById(R.id.datos_paciente_dosis);
        datos_paciente_fecha_inicio = (TextView) findViewById(R.id.datos_paciente_fecha_inicio);


        tipo_admin_o_info = (TextView) findViewById(R.id.tipo_admin_o_info);
        datos_paciente_dosis_subtitulo = (TextView) findViewById(R.id.datos_paciente_dosis_subtitulo);

        datos_paciente_boton_ok = (Button) findViewById(R.id.datos_paciente_boton_ok);
        //Recuperar datos del paciente


        datos_paciente_nombre.setText(this.nombre);
        datos_paciente_edad.setText(this.edad+" años");
        datos_paciente_sexo.setText(this.sexo);
        datos_paciente_documento.setText(this.documento);



        String dia_actual_html = "<b>Día "+this.dia_actual+"</b> de "+this.total_dias;
        datos_paciente_dia_actual.setText(Html.fromHtml(dia_actual_html));
        datos_paciente_semana_actual.setText("Semana "+this.semana);
        datos_paciente_medicamento.setText(this.medicamento);
        datos_paciente_tipo_administracion.setText(this.tipo_administracion);
        datos_paciente_dosis.setText(this.dosis);
        datos_paciente_fecha_inicio.setText(this.fecha_inicio);

        tipo_admin_o_info.setText(tipos[tipo_ventana]);
        if(tipo_ventana == 1){
            datos_paciente_dosis.setVisibility(View.GONE);
            datos_paciente_dosis_subtitulo.setVisibility(View.GONE);
            datos_paciente_boton_ok.setText("EMPEZAR TOMA DE FOTOS");
        }
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
                    if(salida.equals("SI")) {
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



    public void doEmpezarEvaluacion(View view) {


        if(PreferenceManager.getDefaultSharedPreferences(this).getInt("dias_transcurridos_calculados",-1) == -1)
            PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("dias_transcurridos_calculados", 0).commit();
        if (tipo_ventana == OVERVIEW_EVALUACION){
            Intent i = new Intent(this, CalendarioRevisionActivity.class);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            if(medicamento.equalsIgnoreCase("Glucantime"))
                prefs.edit().putInt("medicamento" , MEDICAMENTO_GLUCANTIME)
                        .putInt("numero_titulo", 1)
                        .commit();
            else if (medicamento.equalsIgnoreCase("Miltefosine"))
                prefs.edit().putInt("medicamento" , MEDICAMENTO_MILTEFOSINE)
                        .putInt("numero_titulo", 1)
                        .commit();

            startActivity(i);
        }else if(tipo_ventana == OVERVIEW_FOTOS){
            Intent i = new Intent(this, CuerpoHumanoActivity.class);
            startActivity(i);
        }
    }



    boolean control=true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(control){
            control = false;
            LinearLayout recueadro_datos = (LinearLayout) findViewById(R.id.recuadro_datos_pacientes);
            recueadro_datos.setPadding(40,datos_paciente_nombre.getHeight()+30,40,40);

            LinearLayout subtitulo_dias_semanas = (LinearLayout) findViewById(R.id.subtitulo_dias_semanas);
            LinearLayout recuadro_datos_tratamiento = (LinearLayout) findViewById(R.id.recuadro_datos_tratamiento);
            recuadro_datos_tratamiento.setPadding(40,subtitulo_dias_semanas.getHeight()+30, 40, 40);

        }
    }

    public String date2edad(Date date){
        Calendar c = Calendar.getInstance();
        int anio_actual = c.get(Calendar.YEAR);
        c.setTime(date);
        int anio_nacimiento = c.get(Calendar.YEAR);
        return ""+(anio_actual-anio_nacimiento);
    }

}
