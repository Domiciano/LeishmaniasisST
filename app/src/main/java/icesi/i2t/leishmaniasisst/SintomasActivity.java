package icesi.i2t.leishmaniasisst;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import icesi.i2t.leishmaniasisst.dialogs.AmpollasAdministradasDialog;
import icesi.i2t.leishmaniasisst.dialogs.BooleanAnswerDialog;
import icesi.i2t.leishmaniasisst.dialogs.ConfirmacionDialog;



import icesi.i2t.leishmaniasisst.dialogs.SpinnerDialog;

/**
 * Created by Domiciano on 19/05/2016.
 */
public class SintomasActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    int numero_titulo=0;
    int index_medicamento = 0;

    CheckBox fiebre_checkbox, dolor_color_checkbox, malestar_checkbox;
    CheckBox mareo_checkbox, palpitaciones_checkbox, ninguno_checkbox;

    TextView titulo_sintomas_generales, sintomas_descripcion;

    String dias_transcurridos="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sintomas);


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

        fiebre_checkbox = (CheckBox) findViewById(R.id.fiebre_checkbox);
        dolor_color_checkbox = (CheckBox) findViewById(R.id.dolor_color_checkbox);
        malestar_checkbox = (CheckBox) findViewById(R.id.malestar_checkbox);
        mareo_checkbox = (CheckBox) findViewById(R.id.mareo_checkbox);
        palpitaciones_checkbox = (CheckBox) findViewById(R.id.palpitaciones_checkbox);
        ninguno_checkbox = (CheckBox) findViewById(R.id.ninguno_checkbox);



        fiebre_checkbox.setOnCheckedChangeListener(this);
        dolor_color_checkbox.setOnCheckedChangeListener(this);
        malestar_checkbox.setOnCheckedChangeListener(this);
        mareo_checkbox.setOnCheckedChangeListener(this);
        palpitaciones_checkbox.setOnCheckedChangeListener(this);
        ninguno_checkbox.setOnCheckedChangeListener(this);

        ninguno_checkbox.setChecked(true);
        ninguno_checkbox.setEnabled(false);
        deseleccionar_todo();

        salvarCuestionario();
        titulo_sintomas_generales = (TextView) findViewById(R.id.titulo_sintomas_generales);
        titulo_sintomas_generales.setText(numero_titulo+". Síntomas generales");
        sintomas_descripcion = (TextView) findViewById(R.id.sintomas_descripcion);

        String paciente_id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "");
        dias_transcurridos = PreferenceManager.getDefaultSharedPreferences(this).getString("DIAS"+paciente_id,"0");
        sintomas_descripcion.setText("En los últimos "+dias_transcurridos+" días ¿Cuáles de los siguientes síntomas ha presentado el paciente?.");
    }

    public void doSiguiente(View view) {
        guardarCuestionario();

        if(dolor_color_checkbox.isChecked() || malestar_checkbox.isChecked()
                || mareo_checkbox.isChecked() || palpitaciones_checkbox.isChecked()){
            showCuestionarioSintomasGenerales1();
        }else{
            openActivity();
            return;
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
            if (b.equals(fiebre_checkbox)) {
                fiebre_checkbox.setTextColor(Color.WHITE);
                hacerPregunta1Fiebre();
                deseleccionar_ninguno();
            } else if (b.equals(dolor_color_checkbox)) {
                dolor_color_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
            } else if (b.equals(malestar_checkbox)) {
                malestar_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
            } else if (b.equals(mareo_checkbox)) {
                mareo_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
            } else if (b.equals(palpitaciones_checkbox)) {
                palpitaciones_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
            } else if (b.equals(ninguno_checkbox)) {
                ninguno_checkbox.setTextColor(Color.WHITE);
                deseleccionar_todo();
            }
        }else{
            if (b.equals(fiebre_checkbox)) {
                fiebre_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(dolor_color_checkbox)) {
                dolor_color_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(malestar_checkbox)) {
                malestar_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(mareo_checkbox)) {
                mareo_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(palpitaciones_checkbox)) {
                palpitaciones_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(ninguno_checkbox)) {

            }
        }
    }

    private void deseleccionar_ninguno() {
        ninguno_checkbox.setTextColor(Color.rgb(239, 175, 99));
        ninguno_checkbox.setChecked(false);
        ninguno_checkbox.setEnabled(true);
    }

    private void verificar_sintomas() {
        if(!fiebre_checkbox.isChecked()
                && !dolor_color_checkbox.isChecked()
                && !malestar_checkbox.isChecked()
                && !mareo_checkbox.isChecked()
                && !palpitaciones_checkbox.isChecked()){
            seleccionar_ninguno();
        }
    }

    private void seleccionar_ninguno() {
        ninguno_checkbox.setChecked(true);
        ninguno_checkbox.setTextColor(Color.WHITE);
        ninguno_checkbox.setEnabled(false);
    }

    private void deseleccionar_todo() {
        fiebre_checkbox.setChecked(false);
        dolor_color_checkbox.setChecked(false);
        malestar_checkbox.setChecked(false);
        mareo_checkbox.setChecked(false);
        palpitaciones_checkbox.setChecked(false);
        ninguno_checkbox.setEnabled(false);

    }

    String temperatura;
    String duracion;

    private void hacerPregunta1Fiebre() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        final SpinnerDialog fiebreDialog = SpinnerDialog.newInstance("¿Qué temperatura tiene al momento de la evaluación?", "ºC", 35, 42, temperatura, false);
        fiebreDialog.setCancelable(false);
        fiebreDialog.show(ft, "dialog_fiebre");


        fiebreDialog.setOnDialogDismiss(new SpinnerDialog.OnDialogDismiss() {
            @Override
            public void finish(String salida) {
                if(!salida.isEmpty()) {
                    fiebreDialog.dismiss();
                    temperatura = salida;
                    hacerPregunta2Fiebre();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Primero debe elegir una opción",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void hacerPregunta2Fiebre(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        final SpinnerDialog duracionDialog = SpinnerDialog.newInstance("¿Hace cuántos días tiene fiebre?","días", 1, Integer.parseInt(dias_transcurridos), duracion, true);
        duracionDialog.setCancelable(false);
        duracionDialog.show(ft, "dialog_duracion_fiebre");
        duracionDialog.setOnDialogDismiss(new SpinnerDialog.OnDialogDismiss() {
            @Override
            public void finish(String salida) {
                if(!salida.isEmpty()) {
                    duracionDialog.dismiss();
                    duracion = salida;
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Primero debe elegir una opción",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void showCuestionarioSintomasGenerales1(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        BooleanAnswerDialog dialog = BooleanAnswerDialog.newInstance("¿Los síntomas generales " +
                "le han impedido levantarse de la cama?");
        dialog.show(ft, "dialog_duracion_malestar");

        dialog.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
            @Override
            public void finish(String result) {
                if(!result.equals("SI")) showCuestionarioSintomasGenerales2();
                else{
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_generales", 4).commit();
                    openActivity();
                    return;
                }
            }
        });



    }

    private void showCuestionarioSintomasGenerales2(){
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        BooleanAnswerDialog dialog2 = BooleanAnswerDialog.newInstance("¿Los síntomas generales " +
                "le han impedido asearse o bañarse?");
        dialog2.show(ft2, "dialog_duracion_malestar");
        dialog2.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
            @Override
            public void finish(String result) {
                if(!result.equals("SI")) showCuestionarioSintomasGenerales3();
                else{
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_generales", 3).commit();
                    openActivity();
                    return;
                }
            }
        });

    }

    private void showCuestionarioSintomasGenerales3(){
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        BooleanAnswerDialog dialog2 = BooleanAnswerDialog.newInstance("¿Los síntomas generales " +
                "le han impedido salir de su casa?");
        dialog2.show(ft2, "dialog_duracion_malestar");
        dialog2.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
            @Override
            public void finish(String result) {
                if(!result.equals("SI")){
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_generales", 1).commit();
                }
                else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_generales", 2).commit();
                }

                openActivity();
            }

        });

    }


    public void openActivity(){
        aumentarTitulo();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int index_medicamento = prefs.getInt("medicamento", 0);
        Intent i = null;
        if(index_medicamento == 0){
            i = new Intent(getApplicationContext(), SintomasInyeccionActivity.class);
        }
        else if(index_medicamento == 1){
            i = new Intent(getApplicationContext(), SintomasGastrointestinalesActivity.class);
        }
        startActivity(i);
    }

    private void guardarCuestionario() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putBoolean("fiebre", fiebre_checkbox.isChecked());
        edit.putBoolean("dolor", dolor_color_checkbox.isChecked());
        edit.putBoolean("malestar", malestar_checkbox.isChecked());
        edit.putBoolean("mareo", mareo_checkbox.isChecked());
        edit.putBoolean("palpitaciones", palpitaciones_checkbox.isChecked());


        if (fiebre_checkbox.isChecked()){
            edit.putString("duracion_fiebre", duracion);
            edit.putString("temperatura_fiebre", temperatura);
        }
        edit.commit();

    }

    public void aumentarTitulo(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putInt("numero_titulo", numero_titulo+1).commit();
    }

    private void salvarCuestionario() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        numero_titulo = prefs.getInt("numero_titulo", 0);
        index_medicamento = prefs.getInt("medicamento",0);

        boolean fiebre = prefs.getBoolean("fiebre", false);
        boolean dolor = prefs.getBoolean("dolor", false);
        boolean malestar = prefs.getBoolean("malestar", false);
        boolean mareo = prefs.getBoolean("mareo", false);
        boolean palpitaciones = prefs.getBoolean("palpitaciones", false);

        //ESTA INFO LA UTILIZA EL DIALOGO
        this.temperatura = prefs.getString("temperatura_fiebre","");
        this.duracion = prefs.getString("duracion_fiebre","");


        if(fiebre) seleccionar_fiebre(fiebre);
        dolor_color_checkbox.setChecked(dolor);
        malestar_checkbox.setChecked(malestar);
        mareo_checkbox.setChecked(mareo);
        palpitaciones_checkbox.setChecked(palpitaciones);

    }

    private void seleccionar_fiebre(boolean fiebre) {
        fiebre_checkbox.setOnCheckedChangeListener(null);
        fiebre_checkbox.setChecked(fiebre);
        fiebre_checkbox.setOnCheckedChangeListener(this);
        fiebre_checkbox.setTextColor(Color.WHITE);
        deseleccionar_ninguno();
    }

    @Override
    public void onBackPressed() {
        guardarCuestionario();
        super.onBackPressed();
    }
}
