package icesi.i2t.leishmaniasisst;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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

import org.w3c.dom.Text;

import java.util.Calendar;

import icesi.i2t.leishmaniasisst.dialogs.AmpollasAdministradasDialog;
import icesi.i2t.leishmaniasisst.dialogs.BooleanAnswerDialog;
import icesi.i2t.leishmaniasisst.dialogs.ConfirmacionDialog;


/**
 * Created by Domiciano on 19/05/2016.
 */
public class SintomasInyeccionActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    int numero_titulo;

    CheckBox enrrojecimiento_checkbox, pus_checkbox, calentura_checkbox;
    CheckBox hinchazon_checkbox, dolor_zona_checkbox, ninguno_inyeccion_checkbox;

    String dias;
    TextView titulo_sintomas_inyeccion, desc_sintomas_inyeccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sintomas_inyeccion);


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sitomas_inyeccion_activiy);
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


        enrrojecimiento_checkbox = (CheckBox) findViewById(R.id.enrrojecimiento_checkbox);
        pus_checkbox = (CheckBox) findViewById(R.id.pus_checkbox);
        calentura_checkbox = (CheckBox) findViewById(R.id.calentura_checkbox);
        hinchazon_checkbox = (CheckBox) findViewById(R.id.hinchazon_checkbox);
        dolor_zona_checkbox = (CheckBox) findViewById(R.id.dolor_zona_checkbox);
        ninguno_inyeccion_checkbox = (CheckBox) findViewById(R.id.ninguno_inyeccion_checkbox);

        enrrojecimiento_checkbox.setOnCheckedChangeListener(this);
        pus_checkbox.setOnCheckedChangeListener(this);
        calentura_checkbox.setOnCheckedChangeListener(this);
        hinchazon_checkbox.setOnCheckedChangeListener(this);
        dolor_zona_checkbox.setOnCheckedChangeListener(this);
        ninguno_inyeccion_checkbox.setOnCheckedChangeListener(this);

        ninguno_inyeccion_checkbox.setChecked(true);
        ninguno_inyeccion_checkbox.setEnabled(false);
        deseleccionar_todo();

        salvarCuestionario();
        titulo_sintomas_inyeccion = (TextView) findViewById(R.id.titulo_sintomas_inyeccion);
        titulo_sintomas_inyeccion.setText(numero_titulo+". Síntomas en el lugar \nde la inyección");


        String paciente_id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "");
        dias = PreferenceManager.getDefaultSharedPreferences(this).getString("DIAS"+paciente_id,"0");
        desc_sintomas_inyeccion = (TextView) findViewById(R.id.desc_sintomas_inyeccion);
        desc_sintomas_inyeccion.setText("En los últimos "+dias+" días ¿cuáles de los siguientes síntomas ha presentado en el lugar de la inyección?");

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
            if (b.equals(enrrojecimiento_checkbox)) {
                enrrojecimiento_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
            } else if (b.equals(pus_checkbox)) {
                pus_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
            } else if (b.equals(calentura_checkbox)) {
                calentura_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
            } else if (b.equals(hinchazon_checkbox)) {
                hinchazon_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
            } else if (b.equals(dolor_zona_checkbox)) {
                dolor_zona_checkbox.setTextColor(Color.WHITE);
                deseleccionar_ninguno();
            } else if (b.equals(ninguno_inyeccion_checkbox)) {
                ninguno_inyeccion_checkbox.setTextColor(Color.WHITE);
                deseleccionar_todo();
            }
        }else{
            if (b.equals(enrrojecimiento_checkbox)) {
                enrrojecimiento_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(pus_checkbox)) {
                pus_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(calentura_checkbox)) {
                calentura_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(hinchazon_checkbox)) {
                hinchazon_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(dolor_zona_checkbox)) {
                dolor_zona_checkbox.setTextColor(Color.rgb(239, 175, 99));
                verificar_sintomas();
            } else if (b.equals(ninguno_inyeccion_checkbox)) {

            }
        }
    }

    private void deseleccionar_ninguno() {
        ninguno_inyeccion_checkbox.setTextColor(Color.rgb(239, 175, 99));
        ninguno_inyeccion_checkbox.setChecked(false);
        ninguno_inyeccion_checkbox.setEnabled(true);
    }

    private void verificar_sintomas() {
        if(!enrrojecimiento_checkbox.isChecked()
                && !pus_checkbox.isChecked()
                && !calentura_checkbox.isChecked()
                && !hinchazon_checkbox.isChecked()
                && !dolor_zona_checkbox.isChecked()){
            seleccionar_ninguno();
        }
    }

    private void seleccionar_ninguno() {
        ninguno_inyeccion_checkbox.setChecked(true);
        ninguno_inyeccion_checkbox.setTextColor(Color.WHITE);
        ninguno_inyeccion_checkbox.setEnabled(false);
    }

    private void deseleccionar_todo() {
        enrrojecimiento_checkbox.setChecked(false);
        pus_checkbox.setChecked(false);
        calentura_checkbox.setChecked(false);
        hinchazon_checkbox.setChecked(false);
        dolor_zona_checkbox.setChecked(false);
        ninguno_inyeccion_checkbox.setEnabled(false);
    }


    public void doSiguiente(View v){
        guardarCuestionario();
        if(enrrojecimiento_checkbox.isChecked()
                || pus_checkbox.isChecked()
                || calentura_checkbox.isChecked()
                || hinchazon_checkbox.isChecked()
                || dolor_zona_checkbox.isChecked()){
            showCuestionarioSintomasGenerales1();
        }else openActivity();
    }

    private void showCuestionarioSintomasGenerales1(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        BooleanAnswerDialog dialog = BooleanAnswerDialog.newInstance("¿Los síntomas en el lugar de la inyección " +
                "le han impedido levantarse de la cama?");
        dialog.show(ft, "");

        dialog.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
            @Override
            public void finish(String result) {
                if(!result.equals("SI")) showCuestionarioSintomasGenerales2();
                else{
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_inyeccion", 4).commit();
                    openActivity();
                    return;
                }
            }
        });



    }

    private void showCuestionarioSintomasGenerales2(){
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        BooleanAnswerDialog dialog2 = BooleanAnswerDialog.newInstance("¿Los síntomas en el lugar de la inyección " +
                "le han impedido asearse o bañarse?");
        dialog2.show(ft2, "dialog_duracion_malestar");
        dialog2.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
            @Override
            public void finish(String result) {
                if(!result.equals("SI")) showCuestionarioSintomasGenerales3();
                else{
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_inyeccion", 3).commit();
                    openActivity();
                    return;
                }
            }
        });

    }

    private void showCuestionarioSintomasGenerales3(){
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        BooleanAnswerDialog dialog2 = BooleanAnswerDialog.newInstance("¿Los síntomas en el lugar de la inyección " +
                "le han impedido salir de su casa?");
        dialog2.show(ft2, "dialog_duracion_malestar");
        dialog2.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
            @Override
            public void finish(String result) {
                if(!result.equals("SI")){
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_inyeccion", 1).commit();
                }
                else{
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putInt("severidad_inyeccion", 2).commit();
                }
                openActivity();
            }

        });

    }

    public void openActivity(){
        aumentarTitulo();
        Intent i = new Intent(getApplicationContext(), SintomasGastrointestinalesActivity.class);
        startActivity(i);
    }

    private void guardarCuestionario() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("enrojecimiento", enrrojecimiento_checkbox.isChecked());
        edit.putBoolean("pus", pus_checkbox.isChecked());
        edit.putBoolean("calentura", calentura_checkbox.isChecked());
        edit.putBoolean("hinchazon", hinchazon_checkbox.isChecked());
        edit.putBoolean("dolor_zona", dolor_zona_checkbox.isChecked());

        edit.commit();

    }

    private void salvarCuestionario() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        numero_titulo = prefs.getInt("numero_titulo", 0);
        boolean enrojecimiento = prefs.getBoolean("enrojecimiento", false);
        boolean pus = prefs.getBoolean("pus", false);
        boolean calentura = prefs.getBoolean("calentura", false);
        boolean hinchazon = prefs.getBoolean("hinchazon", false);
        boolean dolor_zona = prefs.getBoolean("dolor_zona", false);

        enrrojecimiento_checkbox.setChecked(enrojecimiento);
        pus_checkbox.setChecked(pus);
        calentura_checkbox.setChecked(calentura);
        hinchazon_checkbox.setChecked(hinchazon);
        dolor_zona_checkbox.setChecked(dolor_zona);

    }

    public void aumentarTitulo(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putInt("numero_titulo", numero_titulo+1).commit();
    }

    @Override
    public void onBackPressed() {
        guardarCuestionario();
        super.onBackPressed();
    }





}
