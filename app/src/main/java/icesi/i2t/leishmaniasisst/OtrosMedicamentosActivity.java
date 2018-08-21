package icesi.i2t.leishmaniasisst;

/**
 * Created by Domiciano on 26/05/2016.
 */

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import icesi.i2t.leishmaniasisst.data.ManejadorBD;
import icesi.i2t.leishmaniasisst.dialogs.BooleanAnswerDialog;
import icesi.i2t.leishmaniasisst.dialogs.ConfirmacionDialog;


import icesi.i2t.leishmaniasisst.dialogs.ListDialog;
import icesi.i2t.leishmaniasisst.listamedicamentos.ListaMedicamentosAdapter;
import icesi.i2t.leishmaniasisst.listasintomas.ListaSintomasAdpater;
import icesi.i2t.leishmaniasisst.model.DevelopSymptom;
import icesi.i2t.leishmaniasisst.model.Paciente;
import icesi.i2t.leishmaniasisst.model.Schema;

/**
 * Created by Domiciano on 24/05/2016.
 */
public class OtrosMedicamentosActivity extends AppCompatActivity {

    int numero_titulo=0;

    ListView lista_medicamentos;
    ListaMedicamentosAdapter adapter;
    ArrayList<String> medicamentos;


    ManejadorBD db;
    Paciente paciente;

    String[] medicamentos_strings = new String[]{
            "Seleccione uno","Acetaminofén","Ibuprofeno",
            "Dipirona", "Aspirina", "Naproxeno", "Diclofenalco",
            "Buscapina", "Omeprazol", "Ranitidina", "Hidroxicina",
            "Cetiricina", "Loratadina", "Metocarbamol", "Cefalexina",
            "Ampicilina", "Dicloxacina", "Coartem", "Mefloquina",
            "Clindamicina", "Aralen", "Primaquina", "Ambramicina",
            "Complejo B"};

    TextView titulo_otros_medicamentos, medicamentos_descripcion;
    String dias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otros_medicamentos);

        db = new ManejadorBD(this);
        String id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id","");
        paciente = db.buscarPaciente(id);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_medicamentos_otros_activiy);
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

        lista_medicamentos = (ListView) findViewById(R.id.lista_medicamentos);
        medicamentos = new ArrayList<>();
        adapter = new ListaMedicamentosAdapter(this, this.getApplicationContext(), medicamentos);
        adapter.setOnDeleteMember(new ListaMedicamentosAdapter.OnDeleteMember() {
            @Override
            public void onDelete(String result) {
                eliminarMedicamento(result);
            }
        });


        lista_medicamentos.setAdapter(adapter);

        salvar_info();
        titulo_otros_medicamentos = (TextView) findViewById(R.id.titulo_otros_medicamentos);
        titulo_otros_medicamentos.setText(numero_titulo+". Otros medicamentos");

        medicamentos_descripcion = (TextView) findViewById(R.id.medicamentos_descripcion);

        String paciente_id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "");
        dias = PreferenceManager.getDefaultSharedPreferences(this).getString("DIAS"+paciente_id, "0");
        medicamentos_descripcion.setText("Registre aquí si el paciente en los últimos "+dias+" días ha tomado otros medicamentos además del glucantime.");
    }

    private void eliminarMedicamento(String result) {
            Schema activo = db.buscarSchemaActivoDelPaciente(paciente);
            List<DevelopSymptom> lista = db.getListaSintomas(activo.getUuid());
            for(int i=0 ; i<lista.size() ; i++){
                if(lista.get(i).getMedicineName().equals(result)){
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




    public void registrarNuevoMedicamento(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        final ListDialog nuevoMedicamentoDialog = ListDialog.newInstance("¿Qué otro medicamento ha tomado en los últimos (1?) días?", medicamentos_strings, Integer.parseInt(dias));
        nuevoMedicamentoDialog.show(ft, "dialog_fiebre");

        nuevoMedicamentoDialog.setOnDialogDismiss(new ListDialog.OnDialogDismiss() {
            @Override
            public void finish(String medicamento) {
                boolean success = adapter.agregar(medicamento);
                if(success){
                    nuevoMedicamentoDialog.dismiss();
                    guardar_medicamento(medicamento);
                }
            }
        });

    }

    /*
    public void guardar_info(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> medicamentos = new HashSet<String>(this.medicamentos);
        editor.putStringSet("medicamentos", medicamentos);
        editor.commit();
    }
    */


    public void salvar_info(){
        //Set<String> default_list = new HashSet<String>(new ArrayList<String>());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Set<String> enfermedades = prefs.getStringSet("enfermedades", default_list);
        numero_titulo = prefs.getInt("numero_titulo",0);

        /*
        ArrayList<String> arrayList = new ArrayList<String>(enfermedades);
        adapter.clear_sintomas();
        for(int i=0 ; i<arrayList.size() ; i++){
            adapter.agregar(arrayList.get(i));
        }
        adapter.notifyDataSetChanged();
        */

        Schema schema = db.buscarSchemaActivoDelPaciente(paciente);
        List<DevelopSymptom> lista_sintomas = db.getListDevelopedDisease(schema.getUuid());
        for(int i=0 ; i<lista_sintomas.size() ; i++){

            if(lista_sintomas.get(i).getMedicineName() == null) continue;
            if(lista_sintomas.get(i).getMedicineName().equals("NULL")) continue;
            if(lista_sintomas.get(i).getDate_end().before(lista_sintomas.get(i).getDate_start())) {
                adapter.agregar(lista_sintomas.get(i).getMedicineName());
            }
        }
        adapter.notifyDataSetChanged();


    }


    /*
    public void salvar_info(){
        Set<String> default_list = new HashSet<String>(new ArrayList<String>());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> medicamentos = prefs.getStringSet("medicamentos", default_list);
        numero_titulo = prefs.getInt("numero_titulo",0);

        ArrayList<String> arrayList = new ArrayList<String>(medicamentos);
        adapter.clear_sintomas();
        for(int i=0 ; i<arrayList.size() ; i++){
            adapter.agregar(arrayList.get(i));
        }
        adapter.notifyDataSetChanged();
    }
    */

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
        Intent i = new Intent(getApplicationContext(), ObservacionesGeneralesActivity.class);
        startActivity(i);
    }


    private void guardar_medicamento(String medicamento) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fecha_nula = "1800-01-01";

            Date date_fecha_nula = sdf.parse(fecha_nula);
            Schema activo = db.buscarSchemaActivoDelPaciente(paciente);
            DevelopSymptom developSymptom = new DevelopSymptom(UUID.randomUUID().toString(),
                    "NULL", "NULL", medicamento, true, Calendar.getInstance().getTime(), date_fecha_nula, activo.getUuid());
            db.agregarDevelopedDisease(developSymptom);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}



