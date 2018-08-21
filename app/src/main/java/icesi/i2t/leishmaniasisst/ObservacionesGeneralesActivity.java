package icesi.i2t.leishmaniasisst;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import icesi.i2t.leishmaniasisst.data.ManejadorBD;
import icesi.i2t.leishmaniasisst.dialogs.BooleanAnswerDialog;
import icesi.i2t.leishmaniasisst.dialogs.ConfirmacionDialog;
import icesi.i2t.leishmaniasisst.model.BasicAdverseEvent;
import icesi.i2t.leishmaniasisst.model.DailySchema;
import icesi.i2t.leishmaniasisst.model.DevelopSymptom;
import icesi.i2t.leishmaniasisst.model.Evaluador;
import icesi.i2t.leishmaniasisst.model.Paciente;
import icesi.i2t.leishmaniasisst.model.Prescripcion;
import icesi.i2t.leishmaniasisst.model.Schema;
import icesi.i2t.leishmaniasisst.services.SyncService;

/**
 * Created by Domiciano on 26/05/2016.
 */
public class ObservacionesGeneralesActivity extends AppCompatActivity{

    int numero_titulo = 0;
    EditText ET_observaciones;
    TextView titulo_observaciones;
    ManejadorBD db;
    Paciente paciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observaciones_generales);

        db = new ManejadorBD(this);
        String id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id","");
        paciente = db.buscarPaciente(id);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_observaciones_activiy);
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

        ET_observaciones = (EditText) findViewById(R.id.ET_observaciones);

        cargar_info();
        titulo_observaciones = (TextView) findViewById(R.id.titulo_observaciones);
        titulo_observaciones.setText(numero_titulo+". Observaciones generales");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_evaluacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
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

    public void doSiguiente(View v){
        //METER TODA LA INFO EN LA BASE DE DATOS INTERNA
        try {
            salvarInforEnDB();
        }catch (Exception e){
            Log.e("ERROR", e.getLocalizedMessage());
        }



        Evaluador ev = db.getRaterByUUID(paciente.getEvaluadorId());
        //Evaluador full_rater = db.getFullRater(ev);
        Intent intentService = new Intent(this, SyncService.class);
        intentService.putExtra("rater", ev);
        startService(intentService);


        Intent intent = new Intent(getApplicationContext(), Evaluacion.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("evaluacion_terminada", true);
        startActivity(intent);
    }

    int calcular_severidad_fiebre(int duracion, int temperatura){
        if(temperatura <=39 && duracion == 1){
            return 1;
        }else if(temperatura ==40 && duracion == 1){
            return 2;
        }else if(temperatura >=41 && duracion == 1){
            return 3;
        }else if(temperatura >=41 && duracion >= 2){
            return 4;
        }
        return 0;
    }

    int calcular_dolor_sitio(boolean enrojecimiento, boolean pus, boolean calentura, boolean hinchazon, boolean dolor_zona, int severidad_inyeccion){
        String value = "";
        int int_enrrojecimiento = enrojecimiento ? severidad_inyeccion : 0;
        int int_pus = pus ? severidad_inyeccion : 0;
        int int_calentura = calentura ? severidad_inyeccion : 0;
        int int_hinchazon = hinchazon ? severidad_inyeccion : 0;
        int int_dolor_zona = dolor_zona ? severidad_inyeccion : 0;

        value += int_enrrojecimiento + "" + int_pus + "" + int_calentura + "" +int_hinchazon + "" + int_dolor_zona;

        return Integer.parseInt(value);
    }

    private int calcular_severidad_vomito(int int_veces_vomito, int int_dias_vomito) {
        if(int_veces_vomito == 1 || int_veces_vomito == 2) return 1;
        if(int_veces_vomito >= 3 && int_veces_vomito <= 5) return 2;
        if(int_veces_vomito >= 6) return 3;
        return 0;
    }

    private int calcular_severidad_diarrea(int int_veces_diarrea, int int_dias_diarrea) {
        if(int_veces_diarrea < 4) return 1;
        if(int_veces_diarrea >= 4 && int_veces_diarrea <= 6) return 2;
        if(int_veces_diarrea >= 7) return 3;
        return 0;
    }

    private void salvarInforEnDB() {

        try {
            String numero_unidades = PreferenceManager.getDefaultSharedPreferences(this).getString("numero_unidades", "");
            String fecha_inicio = PreferenceManager.getDefaultSharedPreferences(this).getString("fecha_inicio", "0000-00-00");
            String fecha_fin = PreferenceManager.getDefaultSharedPreferences(this).getString("fecha_fin", "0000-00-00");
            Schema activo = db.buscarSchemaActivoDelPaciente(paciente);
            ArrayList<DailySchema> lista = db.getDailySchemaListByDates(activo.getUuid(), fecha_inicio, fecha_fin);

            String duracion_fiebre = PreferenceManager.getDefaultSharedPreferences(this).getString("duracion_fiebre","0 dias");
                int int_duracion_fiebre = Integer.parseInt(duracion_fiebre.substring(0, duracion_fiebre.length()-5));
            String temperatura_fiebre = PreferenceManager.getDefaultSharedPreferences(this).getString("temperatura_fiebre","35 ºC");
                int int_temperatura_fiebre = Integer.parseInt(temperatura_fiebre.substring(0, 2));
            int severidad_generales = PreferenceManager.getDefaultSharedPreferences(this).getInt("severidad_generales",0);
            boolean fiebre = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("fiebre",false);
            boolean dolor = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dolor",false);
            boolean malestar = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("malestar",false);
            boolean mareo = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("mareo",false);
            boolean palpitaciones = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("palpitaciones",false);

            boolean enrojecimiento = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("enrojecimiento",false);
            boolean pus = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pus",false);
            boolean calentura = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("calentura",false);
            boolean hinchazon = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("hinchazon",false);
            boolean dolor_zona = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dolor_zona",false);
            int severidad_inyeccion = PreferenceManager.getDefaultSharedPreferences(this).getInt("severidad_inyeccion",0);

            boolean nauseas = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("nauseas",false);
            boolean vomito = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("vomito",false);
            boolean perdida_apetito = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("perdida_apetito",false);
            boolean dolor_abdominal = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dolor_abdominal",false);
            boolean diarrea = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("diarrea",false);
            String veces_vomito = PreferenceManager.getDefaultSharedPreferences(this).getString("veces_vomito","0 veces");
                int int_veces_vomito = Integer.parseInt(veces_vomito.substring(0, 1));
            String dias_vomito = PreferenceManager.getDefaultSharedPreferences(this).getString("dias_vomito","0 dias");
                int int_dias_vomito = Integer.parseInt(dias_vomito.substring(0, duracion_fiebre.length()-5));
            String veces_diarrea = PreferenceManager.getDefaultSharedPreferences(this).getString("veces_diarrea","0 veces");
                int int_veces_diarrea = Integer.parseInt(veces_diarrea.substring(0, 1));
            String dias_diarrea = PreferenceManager.getDefaultSharedPreferences(this).getString("dias_diarrea","0 dias");
                int int_dias_diarrea = Integer.parseInt(dias_diarrea.substring(0, duracion_fiebre.length()-5));
            int severidad_gastro = PreferenceManager.getDefaultSharedPreferences(this).getInt("severidad_gastro",0);

            boolean dolor_muscular = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dolor_muscular",false);
            boolean dolor_articulaciones = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dolor_articulaciones",false);
            int severidad_osteo = PreferenceManager.getDefaultSharedPreferences(this).getInt("severidad_osteo",0);

            //String observaciones_generales = PreferenceManager.getDefaultSharedPreferences(this).getString("observaciones_generales","");


            for(int i=0 ; i<lista.size() ; i++){
                DailySchema d = lista.get(i);
                List<Prescripcion> lista_p = db.getListaPrescripciones(d.getUuid());
                if(lista_p == null) continue;
                if(lista_p.size() == 0) continue;
                Prescripcion prescripcion = lista_p.get(0);
                BasicAdverseEvent bae = db.buscarBasicAdverseEvent(prescripcion.getUuid());

                if(bae == null){
                    Log.d("Error BasicAdverseEvent","Uno de los basicAdverseEvent está nulo");
                    continue;
                }

                    bae.setFiebre(fiebre ? calcular_severidad_fiebre(int_duracion_fiebre, int_temperatura_fiebre) : 0);
                    bae.setDolorCabeza(dolor?severidad_generales:0);
                    bae.setMalestarGeneral(malestar?severidad_generales:0);
                    bae.setMareo(mareo?severidad_generales:0);
                    bae.setPalpitaciones(palpitaciones?severidad_generales:0);

                    bae.setDolorSitio(calcular_dolor_sitio(enrojecimiento, pus, calentura, hinchazon, dolor_zona, severidad_inyeccion));
                    bae.setInfeccionSitio(calcular_infeccion_sitio(enrojecimiento, pus, calentura, hinchazon, dolor_zona, severidad_inyeccion));

                    bae.setVomito(vomito ? calcular_severidad_vomito(int_veces_vomito, int_dias_vomito) : 0);
                    bae.setDiarrea(diarrea ? calcular_severidad_diarrea(int_veces_diarrea, int_dias_diarrea) : 0);
                    bae.setNauseas(nauseas?severidad_gastro:0);
                    bae.setPerdidaApetito(perdida_apetito?severidad_gastro:0);
                    bae.setDolorAbdominal(dolor_abdominal?severidad_gastro:0);

                    bae.setDolorMuscular(dolor_muscular ? severidad_osteo : 0);
                    bae.setDolorArticulaciones(dolor_articulaciones ? severidad_osteo : 0);
                db.editarBasicAdverseEvent(bae);

                prescripcion.setComentarios(ET_observaciones.getText().toString());
                //prescripcion.setNumeroLote(numero_unidades);
                //db.editarPrescripcion(prescripcion);



            }




        }catch (Exception e){
            Log.e("ERROR", e.getLocalizedMessage());
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("numero_unidades");
        editor.remove("fiebre");

        editor.remove("dolor");
        editor.remove("malestar");
        editor.remove("mareo");
        editor.remove("palpitaciones");
        editor.remove("duracion_fiebre");
        editor.remove("temperatura_fiebre");

        editor.remove("enrojecimiento");
        editor.remove("pus");
        editor.remove("calentura");
        editor.remove("hinchazon");
        editor.remove("dolor_zona");

        editor.remove("nauseas");
        editor.remove("vomito");
        editor.remove("perdida_apetito");
        editor.remove("dolor_abdominal");
        editor.remove("diarrea");
        editor.remove("veces_vomito");
        editor.remove("dias_vomito");
        editor.remove("veces_diarrea");
        editor.remove("dias_diarrea");

        editor.remove("dolor_muscular");
        editor.remove("dolor_articulaciones");

        editor.remove("enfermedades");

        editor.remove("medicamentos");
        editor.remove("medicamento");
        editor.remove("fecha_inicio");
        editor.remove("fecha_fin");

        editor.remove("observaciones_generales");

        String paciente_id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "");
        editor.remove("DIAS"+paciente_id);
        editor.remove("paciente_id");
        editor.remove("numero_titulo");
        editor.remove("dias_transcurridos_calculados");
        //editor.remove("rater_id");

        //editor.putBoolean(paciente_id, true);

        editor.commit();





    }

    private int calcular_infeccion_sitio(boolean enrojecimiento, boolean pus, boolean calentura, boolean hinchazon, boolean dolor_zona, int severidad_inyeccion) {
        int acumulador = 0;
        if(enrojecimiento) acumulador+=severidad_inyeccion;
        if(pus) acumulador+=severidad_inyeccion;
        if(calentura) acumulador+=severidad_inyeccion;
        if(hinchazon) acumulador+=severidad_inyeccion;
        if(dolor_zona) acumulador+=severidad_inyeccion;

        return acumulador/5;
    }


    public void cargar_info(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String texto = prefs.getString("observaciones_generales","");
        numero_titulo = prefs.getInt("numero_titulo",0);
        ET_observaciones.setText(texto);
    }

    public void guardar_info(){
        String texto = ET_observaciones.getText().toString();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("observaciones_generales",texto);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        guardar_info();
        super.onBackPressed();
    }
}
