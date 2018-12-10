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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import icesi.i2t.leishmaniasisst.data.ManejadorBD;
import icesi.i2t.leishmaniasisst.dialogs.BooleanAnswerDialog;
import icesi.i2t.leishmaniasisst.dialogs.ConfirmacionDialog;


import icesi.i2t.leishmaniasisst.dialogs.EntryDialog;
import icesi.i2t.leishmaniasisst.listasintomas.ListaSintomasAdpater;
import icesi.i2t.leishmaniasisst.model.DevelopSymptom;
import icesi.i2t.leishmaniasisst.model.Paciente;
import icesi.i2t.leishmaniasisst.model.Schema;

/**
 * Created by Domiciano on 24/05/2016.
 */
public class OtrosSintomasActivity extends AppCompatActivity {

    int numero_titulo = 0;

    ListView lista_sintomas;
    ListaSintomasAdpater adapter;
    ArrayList<String> sintomas;

    TextView titulo_otros_sintomas, otros_sintomas_descripcion;

    ManejadorBD db;
    Paciente paciente;

    String dias = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otros_sintomas);

        db = new ManejadorBD(this);
        String id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id","");
        paciente = db.buscarPaciente(id);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sitomas_otros_activiy);
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

        lista_sintomas = (ListView) findViewById(R.id.lista_sintomas);
        sintomas = new ArrayList<>();
        adapter = new ListaSintomasAdpater(this, this.getApplicationContext(), sintomas);
        adapter.setOnDeleteMember(new ListaSintomasAdpater.OnDeleteMember() {
            @Override
            public void onDelete(String result) {
                eliminarSintoma(result);
            }
        });

        lista_sintomas.setAdapter(adapter);



        salvar_info();
        titulo_otros_sintomas = (TextView) findViewById(R.id.titulo_otros_sintomas);
        titulo_otros_sintomas.setText(numero_titulo+". Otros síntomas y/o\nenfermedades");

        otros_sintomas_descripcion = (TextView) findViewById(R.id.otros_sintomas_descripcion);

        String paciente_id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "");
        dias = PreferenceManager.getDefaultSharedPreferences(this).getString("DIAS"+paciente_id,"0");
        otros_sintomas_descripcion.setText("Registre aquí si el paciente ha presentado otros síntomas o enfermedades en los últimos "+dias+" días.");
    }

    private void eliminarSintoma(String result) {
        Schema activo = db.buscarSchemaActivoDelPaciente(paciente);
        List<DevelopSymptom> lista = db.getListaSintomas(activo.getUuid());
        for(int i=0 ; i<lista.size() ; i++){
            if(lista.get(i).getName().equals(result)){
                //db.deleteDevelopSymptom(lista.get(i).getUuid());
                DevelopSymptom ds = lista.get(i);
                ds.setDate_end(Calendar.getInstance().getTime());
                db.editarDevelopedDisease(ds);
            }
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

    public void doSiguiente(View v){
        openActivity();
    }

    public void registrarNuevoSintoma(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //int dias=5;
        final EntryDialog nuevoSintomaDialog = EntryDialog.newInstance("¿Qué otro síntoma y/o enfermedad ha tenido en los últimos (1?) días?", Integer.parseInt(dias), "REGISTRAR");
        nuevoSintomaDialog.show(ft, "dialog_fiebre");

        nuevoSintomaDialog.setOnDialogDismiss(new EntryDialog.OnDialogDismiss() {
            @Override
            public void finish(String sintoma) {
                boolean success = adapter.agregar(sintoma);
                if(success){
                    nuevoSintomaDialog.dismiss();
                    guardar_sintoma(sintoma);
                }
            }
        });
    }

    private void guardar_sintoma(String sintoma) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fecha_nula = "1800-01-01";

            Date date_fecha_nula = sdf.parse(fecha_nula);


            Schema activo = db.buscarSchemaActivoDelPaciente(paciente);
            DevelopSymptom developSymptom = new DevelopSymptom(UUID.randomUUID().toString(),
                    sintoma, "NULL", "NULL", true, Calendar.getInstance().getTime(), date_fecha_nula, activo.getUuid());
            db.agregarDevelopedDisease(developSymptom);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    public void guardar_info() {
        Schema activo = db.buscarSchemaActivoDelPaciente(paciente);
        for(int i=0 ; i<sintomas.size() ; i++){
            DevelopSymptom developSymptom = new DevelopSymptom(UUID.randomUUID().toString(),
                    sintomas.get(i), "NULL", "NULL", true, Calendar.getInstance().getTime(), null, activo.getUuid());
            db.agregarDevelopedDisease(developSymptom);
        }

    }
    */


    public void salvar_info(){
        //Set<String> default_list = new HashSet<String>(new ArrayList<String>());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Set<String> enfermedades = prefs.getStringSet("enfermedades", default_list);
        numero_titulo = prefs.getInt("numero_titulo",0);

        /*
        ArrayList<String> arrayList = new ArrayList<String>(enfermedades);
        adapterSpinnerPacientes.clear_sintomas();
        for(int i=0 ; i<arrayList.size() ; i++){
            adapterSpinnerPacientes.agregar(arrayList.get(i));
        }
        adapterSpinnerPacientes.notifyDataSetChanged();
        */

        Schema schema = db.buscarSchemaActivoDelPaciente(paciente);
        List<DevelopSymptom> lista_sintomas = db.getListDevelopedDisease(schema.getUuid());
        for(int i=0 ; i<lista_sintomas.size() ; i++){
            if(lista_sintomas.get(i).getName() == null) continue;
            if(lista_sintomas.get(i).getName().equals("NULL")) continue;
            if(lista_sintomas.get(i).getDate_end().before(lista_sintomas.get(i).getDate_start())) {
                adapter.agregar(lista_sintomas.get(i).getName());
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void aumentarTitulo(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putInt("numero_titulo", numero_titulo+1).commit();
    }

    public void openActivity(){
        aumentarTitulo();
        Intent i = new Intent(getApplicationContext(), OtrosMedicamentosActivity.class);
        startActivity(i);
    }


}
