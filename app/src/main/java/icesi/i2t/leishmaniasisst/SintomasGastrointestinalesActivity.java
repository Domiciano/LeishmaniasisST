package icesi.i2t.leishmaniasisst;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import icesi.i2t.leishmaniasisst.dialogs.BooleanAnswerDialog;
import icesi.i2t.leishmaniasisst.dialogs.ConfirmacionDialog;
import icesi.i2t.leishmaniasisst.dialogs.SpinnerDialog;


/**
 * Created by Domiciano on 19/05/2016.
 */
public class SintomasGastrointestinalesActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    int numero_titulo=0;
    CheckBox nauseas_checkbox, vomito_checkbox, perdida_apetito_checkbox;
    CheckBox dolor_abdominal_checkbox, diarrea_checkbox, ninguno_gastro_checkbox;

    TextView titulo_sintomas_gastro, gastro_descripcion;

    int dias=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sintomas_gastro);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sitomas_gastro);
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

        nauseas_checkbox = (CheckBox) findViewById(R.id.nauseas_checkbox);
        vomito_checkbox = (CheckBox) findViewById(R.id.vomito_checkbox);
        perdida_apetito_checkbox = (CheckBox) findViewById(R.id.perdida_apetito_checkbox);
        dolor_abdominal_checkbox = (CheckBox) findViewById(R.id.dolor_abdominal_checkbox);
        diarrea_checkbox = (CheckBox) findViewById(R.id.diarrea_checkbox);
        ninguno_gastro_checkbox = (CheckBox) findViewById(R.id.ninguno_gastro_checkbox);

        nauseas_checkbox.setOnCheckedChangeListener(this);
        vomito_checkbox.setOnCheckedChangeListener(this);
        perdida_apetito_checkbox.setOnCheckedChangeListener(this);
        dolor_abdominal_checkbox.setOnCheckedChangeListener(this);
        diarrea_checkbox.setOnCheckedChangeListener(this);
        ninguno_gastro_checkbox.setOnCheckedChangeListener(this);

        ninguno_gastro_checkbox.setChecked(true);
        ninguno_gastro_checkbox.setEnabled(false);
        deseleccionar_todo();

        salvarCuestionario();
        titulo_sintomas_gastro = (TextView) findViewById(R.id.titulo_sintomas_gastro);
        titulo_sintomas_gastro.setText(numero_titulo + ". Síntomas gastrointestinales");

        gastro_descripcion = (TextView) findViewById(R.id.gastro_descripcion);


        String paciente_id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "");
        dias = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("DIAS"+paciente_id,"0"));
        gastro_descripcion.setText("En los últimos "+dias+" días ¿Cuáles de los siguientes síntomas ha presentado el paciente?.");
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        final CheckBox b = (CheckBox) buttonView;
        b.animate().setDuration(100).alpha(0.7f).withEndAction(new Runnable() {
            @Override
            public void run() {
                b.animate().setDuration(100).alpha(1).scaleY(1);
            }
        });

        if(isChecked) {
            if (b.equals(nauseas_checkbox)) {
                nauseas_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
            } else if (b.equals(vomito_checkbox)) {
                vomito_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
                hacerPregunta1Vomito();
            } else if (b.equals(perdida_apetito_checkbox)) {
                perdida_apetito_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
            } else if (b.equals(dolor_abdominal_checkbox)) {
                dolor_abdominal_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
            } else if (b.equals(diarrea_checkbox)) {
                diarrea_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
                hacerPregunta1Diarrea();
            } else if (b.equals(ninguno_gastro_checkbox)) {
                ninguno_gastro_checkbox.setTextColor(Color.WHITE);
                deseleccionar_todo();
            }
        }else{
            if (b.equals(nauseas_checkbox)) {
                nauseas_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(vomito_checkbox)) {
                vomito_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(perdida_apetito_checkbox)) {
                perdida_apetito_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(dolor_abdominal_checkbox)) {
                dolor_abdominal_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(diarrea_checkbox)) {
                diarrea_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(ninguno_gastro_checkbox)) {}
        }
    }

    String veces_vomito;
    String dias_vomito;



    private void hacerPregunta1Vomito() {
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        final SpinnerDialog dialog1 = SpinnerDialog.newInstance("¿Cuántas veces vomita, en promedio, cada día?", "veces", 1,6, veces_vomito, false);
        dialog1.setCancelable(false);
        dialog1.show(ft1, "dialog_duracion_malestar");
        dialog1.setOnDialogDismiss(new SpinnerDialog.OnDialogDismiss() {
            @Override
            public void finish(String salida) {
                if(!salida.isEmpty()){
                    veces_vomito = salida;
                    dialog1.dismiss();
                    hacerPregunta2Vomito();
                }else{
                    Toast.makeText(getApplicationContext(),
                        "Primero debe elegir una opción",
                        Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void hacerPregunta2Vomito() {
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        final SpinnerDialog dialog1 = SpinnerDialog.newInstance("¿Hace cuántos días presenta vómito constante?", "días", 1,dias, dias_vomito, true);
        dialog1.setCancelable(false);
        dialog1.show(ft1, "dialog_duracion_malestar");
        dialog1.setOnDialogDismiss(new SpinnerDialog.OnDialogDismiss() {
            @Override
            public void finish(String salida) {
                if(!salida.isEmpty()) {
                    dias_vomito = salida;
                    dialog1.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Primero debe elegir una opción",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    String veces_diarrea;
    String dias_diarrea;


    private void hacerPregunta1Diarrea() {
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        final SpinnerDialog dialog1 = SpinnerDialog.newInstance("¿Cuántas veces además de las normales, usted va al baño cada día?", "veces", 1,7, veces_diarrea, false);
        dialog1.setCancelable(false);
        dialog1.show(ft1, "dialog_duracion_malestar");
        dialog1.setOnDialogDismiss(new SpinnerDialog.OnDialogDismiss() {
            @Override
            public void finish(String salida) {
                if(!salida.isEmpty()){
                    veces_diarrea = salida;
                    dialog1.dismiss();
                    hacerPregunta2Diarrea();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Primero debe elegir una opción",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void hacerPregunta2Diarrea() {
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        final SpinnerDialog dialog1 = SpinnerDialog.newInstance("¿Hace cuantos días tiene diarrea?", "días", 1, dias, dias_diarrea, true);
        dialog1.setCancelable(false);
        dialog1.show(ft1, "dialog_duracion_malestar");
        dialog1.setOnDialogDismiss(new SpinnerDialog.OnDialogDismiss() {
            @Override
            public void finish(String salida) {
                if(!salida.isEmpty()){
                    dias_diarrea = salida;
                    dialog1.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Primero debe elegir una opción",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void deseleccionar_ninguno() {
        ninguno_gastro_checkbox.setTextColor(Color.rgb(239, 175, 99));
        ninguno_gastro_checkbox.setChecked(false);
        ninguno_gastro_checkbox.setEnabled(true);
    }

    private void verificar_sintomas() {
        if(!nauseas_checkbox.isChecked()
                && !vomito_checkbox.isChecked()
                && !perdida_apetito_checkbox.isChecked()
                && !dolor_abdominal_checkbox.isChecked()
                && !diarrea_checkbox.isChecked()){
            seleccionar_ninguno();
        }
    }

    private void seleccionar_ninguno() {
        ninguno_gastro_checkbox.setChecked(true);
        ninguno_gastro_checkbox.setTextColor(Color.WHITE);
        ninguno_gastro_checkbox.setEnabled(false);
    }

    private void deseleccionar_todo() {
        nauseas_checkbox.setChecked(false);
        vomito_checkbox.setChecked(false);
        perdida_apetito_checkbox.setChecked(false);
        dolor_abdominal_checkbox.setChecked(false);
        diarrea_checkbox.setChecked(false);
        ninguno_gastro_checkbox.setEnabled(false);
    }

    public void doSiguiente(View v){
        guardarCuestionario();
        if(nauseas_checkbox.isChecked()
                || perdida_apetito_checkbox.isChecked()
                || dolor_abdominal_checkbox.isChecked()) {
            showCuestionarioSintomasGenerales1();
        }else openActivity();
    }


    private void showCuestionarioSintomasGenerales1(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        BooleanAnswerDialog dialog = BooleanAnswerDialog.newInstance("¿Los síntomas gastrointestinales " +
                "le han impedido levantarse de la cama?");
        dialog.show(ft, "");

        dialog.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
            @Override
            public void finish(String result) {
                if(!result.equals("SI")) showCuestionarioSintomasGenerales2();
                else{
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_gastro", 4).commit();
                    openActivity();
                    return;
                }
            }
        });



    }

    private void showCuestionarioSintomasGenerales2(){
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        BooleanAnswerDialog dialog2 = BooleanAnswerDialog.newInstance("¿Los síntomas gastrointestinales " +
                "le han impedido asearse o bañarse?");
        dialog2.show(ft2, "dialog_duracion_malestar");
        dialog2.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
            @Override
            public void finish(String result) {
                if(!result.equals("SI")) showCuestionarioSintomasGenerales3();
                else{
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_gastro", 3).commit();
                    openActivity();
                    return;
                }
            }
        });

    }

    private void showCuestionarioSintomasGenerales3(){
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        BooleanAnswerDialog dialog2 = BooleanAnswerDialog.newInstance("¿Los síntomas gastrointestinales " +
                "le han impedido salir de su casa?");
        dialog2.show(ft2, "dialog_duracion_malestar");
        dialog2.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
            @Override
            public void finish(String result) {
                if(!result.equals("SI")){
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_gastro", 1).commit();
                }
                else{
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_gastro", 2).commit();
                }
                openActivity();
            }

        });

    }




    private void guardarCuestionario() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("nauseas", nauseas_checkbox.isChecked());
        edit.putBoolean("vomito", vomito_checkbox.isChecked());
        edit.putBoolean("perdida_apetito", perdida_apetito_checkbox.isChecked());
        edit.putBoolean("dolor_abdominal", dolor_abdominal_checkbox.isChecked());
        edit.putBoolean("diarrea", diarrea_checkbox.isChecked());

        if (vomito_checkbox.isChecked()){
            edit.putString("veces_vomito", veces_vomito);
            edit.putString("dias_vomito", dias_vomito);
        }

        if (diarrea_checkbox.isChecked()){
            edit.putString("veces_diarrea", veces_diarrea);
            edit.putString("dias_diarrea", dias_diarrea);
        }

        edit.commit();

    }

    private void salvarCuestionario() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        numero_titulo = prefs.getInt("numero_titulo", 0);
        boolean nauseas = prefs.getBoolean("nauseas", false);
        boolean vomito = prefs.getBoolean("vomito", false);
        boolean perdida_apetito = prefs.getBoolean("perdida_apetito", false);
        boolean dolor_abdominal = prefs.getBoolean("dolor_abdominal", false);
        boolean diarrea = prefs.getBoolean("diarrea", false);

        //ESTA INFO LA UTILIZA EL DIALOGO
        this.veces_diarrea = prefs.getString("veces_diarrea","");
        this.dias_diarrea = prefs.getString("dias_diarrea","");
        this.veces_vomito = prefs.getString("veces_vomito","");
        this.dias_vomito = prefs.getString("dias_vomito","");

        nauseas_checkbox.setChecked(nauseas);
        if(vomito) seleccionar_vomito(vomito);
        perdida_apetito_checkbox.setChecked(perdida_apetito);
        dolor_abdominal_checkbox.setChecked(dolor_abdominal);
        if(diarrea) seleccionar_diarrea(diarrea);

    }

    private void seleccionar_vomito(boolean vomito) {
        vomito_checkbox.setOnCheckedChangeListener(null);
        vomito_checkbox.setChecked(vomito);
        vomito_checkbox.setOnCheckedChangeListener(this);
        vomito_checkbox.setTextColor(Color.WHITE);
        deseleccionar_ninguno();
    }

    private void seleccionar_diarrea(boolean diarrea) {
        diarrea_checkbox.setOnCheckedChangeListener(null);
        diarrea_checkbox.setChecked(diarrea);
        diarrea_checkbox.setOnCheckedChangeListener(this);
        diarrea_checkbox.setTextColor(Color.WHITE);
        deseleccionar_ninguno();
    }

    @Override
    public void onBackPressed() {
        guardarCuestionario();
        super.onBackPressed();
    }


    public void aumentarTitulo(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putInt("numero_titulo", numero_titulo+1).commit();
    }

    public void openActivity(){
        aumentarTitulo();
        Intent i = new Intent(getApplicationContext(), SintomasOsteomuscularesActivity.class);
        startActivity(i);
    }

}