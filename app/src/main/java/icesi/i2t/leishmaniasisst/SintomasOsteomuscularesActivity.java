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

import icesi.i2t.leishmaniasisst.dialogs.BooleanAnswerDialog;
import icesi.i2t.leishmaniasisst.dialogs.ConfirmacionDialog;

/**
 * Created by Domiciano on 24/05/2016.
 */

public class SintomasOsteomuscularesActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {


    CheckBox dolor_muscular_checkbox, dolor_articulaciones_checkbox, ninguno_osteo_checkbox;
    TextView titulo_sintomas_osteo, osteo_descripcion;

    int numero_titulo=0;
    String dias = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sintomas_osteo);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sitomas_activiy);
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

        dolor_muscular_checkbox = (CheckBox) findViewById(R.id.dolor_muscular_checkbox);
        dolor_articulaciones_checkbox = (CheckBox) findViewById(R.id.dolor_articulaciones_checkbox);
        ninguno_osteo_checkbox = (CheckBox) findViewById(R.id.ninguno_osteo_checkbox);

        dolor_muscular_checkbox.setOnCheckedChangeListener(this);
        dolor_articulaciones_checkbox.setOnCheckedChangeListener(this);
        ninguno_osteo_checkbox.setOnCheckedChangeListener(this);

        ninguno_osteo_checkbox.setChecked(true);
        ninguno_osteo_checkbox.setEnabled(false);
        deseleccionar_todo();

        salvarCuestionario();
        titulo_sintomas_osteo = (TextView) findViewById(R.id.titulo_sintomas_osteo);
        titulo_sintomas_osteo.setText(numero_titulo + ". Síntomas osteomusculares");

        osteo_descripcion = (TextView) findViewById(R.id.osteo_descripcion);

        String paciente_id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "");
        dias = PreferenceManager.getDefaultSharedPreferences(this).getString("DIAS"+paciente_id,"0");
        osteo_descripcion.setText("En los últimos "+dias+" días ¿cuáles de los siguientes síntomas corporales ha presentado el paciente?");
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
            if (b.equals(dolor_muscular_checkbox)) {
                dolor_muscular_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
            } else if (b.equals(dolor_articulaciones_checkbox)) {
                dolor_articulaciones_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
            } else if (b.equals(ninguno_osteo_checkbox)) {
                ninguno_osteo_checkbox.setTextColor(Color.WHITE);
                deseleccionar_todo();
            }
        }else{
            if (b.equals(dolor_muscular_checkbox)) {
                dolor_muscular_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(dolor_articulaciones_checkbox)) {
                dolor_articulaciones_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(ninguno_osteo_checkbox)) {

            }
        }
    }

    private void deseleccionar_ninguno() {
        ninguno_osteo_checkbox.setTextColor(Color.rgb(239, 175, 99));
        ninguno_osteo_checkbox.setChecked(false);
        ninguno_osteo_checkbox.setEnabled(true);
    }

    private void verificar_sintomas() {
        if(!dolor_muscular_checkbox.isChecked()
                && !dolor_articulaciones_checkbox.isChecked()){
            seleccionar_ninguno();
        }
    }

    private void seleccionar_ninguno() {
        ninguno_osteo_checkbox.setChecked(true);
        ninguno_osteo_checkbox.setTextColor(Color.WHITE);
        ninguno_osteo_checkbox.setEnabled(false);
    }

    private void deseleccionar_todo() {
        dolor_muscular_checkbox.setChecked(false);
        dolor_articulaciones_checkbox.setChecked(false);
        ninguno_osteo_checkbox.setEnabled(false);
    }

    public void doSiguiente(View v){
        guardarCuestionario();
        if(dolor_muscular_checkbox.isChecked()
                || dolor_articulaciones_checkbox.isChecked()) {
            showCuestionarioSintomasGenerales1();
        }else openActivity();
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


    private void showCuestionarioSintomasGenerales1(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        BooleanAnswerDialog dialog = BooleanAnswerDialog.newInstance("¿Los síntomas osteomusculares " +
                "le han impedido levantarse de la cama?");
        dialog.show(ft, "");

        dialog.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
            @Override
            public void finish(String result) {
                if(!result.equals("SI")) showCuestionarioSintomasGenerales2();
                else{
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_osteo", 4).commit();
                    openActivity();
                    return;
                }
            }
        });



    }

    private void showCuestionarioSintomasGenerales2(){
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        BooleanAnswerDialog dialog2 = BooleanAnswerDialog.newInstance("¿Los síntomas osteomusculares " +
                "le han impedido asearse o bañarse?");
        dialog2.show(ft2, "dialog_duracion_malestar");
        dialog2.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
            @Override
            public void finish(String result) {
                if(!result.equals("SI")) showCuestionarioSintomasGenerales3();
                else{
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_osteo", 3).commit();
                    openActivity();
                    return;
                }
            }
        });

    }

    private void showCuestionarioSintomasGenerales3(){
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        BooleanAnswerDialog dialog2 = BooleanAnswerDialog.newInstance("¿Los síntomas osteomusculares " +
                "le han impedido salir de su casa?");
        dialog2.show(ft2, "dialog_duracion_malestar");
        dialog2.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
            @Override
            public void finish(String result) {
                if(!result.equals("SI")){
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_osteo", 1).commit();
                }
                else{
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_osteo", 2).commit();
                }
                openActivity();
            }

        });

    }



    private void guardarCuestionario() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("dolor_muscular", dolor_muscular_checkbox.isChecked());
        edit.putBoolean("dolor_articulaciones", dolor_articulaciones_checkbox.isChecked());
        edit.commit();

    }

    private void salvarCuestionario() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        numero_titulo = prefs.getInt("numero_titulo", 0);
        boolean dolor_muscular = prefs.getBoolean("dolor_muscular", false);
        boolean dolor_articulaciones = prefs.getBoolean("dolor_articulaciones", false);


        dolor_muscular_checkbox.setChecked(dolor_muscular);
        dolor_articulaciones_checkbox.setChecked(dolor_articulaciones);
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
        Intent i = new Intent(getApplicationContext(), OtrosSintomasActivity.class);
        startActivity(i);
    }


}