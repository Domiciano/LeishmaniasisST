package icesi.i2t.leishmaniasisst;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import icesi.i2t.leishmaniasisst.cloudinary.CloudinaryHandler;
import icesi.i2t.leishmaniasisst.data.ManejadorBD;
import icesi.i2t.leishmaniasisst.model.Evaluador;
import icesi.i2t.leishmaniasisst.services.SessionController;
import icesi.i2t.leishmaniasisst.util.LeishConstants;

public class MainActivity extends Activity {

    EditText login_password;
    private ManejadorBD db;

    ProgressDialog dialog;
    RelativeLayout main_padre;
    ImageView main_logo;

    boolean control=true;

    Button b;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(control){
            control=false;

            //deleteDatabase("leishmaniasis");

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(main_padre.getWidth()-100, (int)((main_padre.getWidth()-100)/3.28));
            //p.setMargins(0,0,0,200);
            main_logo.setLayoutParams(p);
            main_logo.setY((int)(main_padre.getHeight()*0.18));


            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED

                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED

                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET},
                        11);
            }else{
                File f = new File(Environment.getExternalStorageDirectory().toString() + "/"+LeishConstants.FOLDER);
                if(!f.exists()) f.mkdirs();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 11: {
                Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show();
                File f = new File(Environment.getExternalStorageDirectory() + "/"+LeishConstants.FOLDER+"/");
                if(!f.exists()) f.mkdirs();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
        //PreferenceManager.getDefaultSharedPreferences(this).edit().remove("paciente_id").clear().commit();
        Log.e("<<2>>",PreferenceManager.getDefaultSharedPreferences(this).getString("DIAS7509612", "-"));
        Log.e("<--SP-->",PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id","-"));



        dialog = new ProgressDialog(this, R.style.ProgressStyle);
        dialog.setMessage("Cargando información, por favor espera...");
        dialog.setCancelable(false);

        b = (Button) findViewById(R.id.buttonEnter);

        //login_cedula = (EditText)findViewById(R.id.login_cedula);
        login_password = (EditText)findViewById(R.id.login_password);

        //login_cedula.setHintTextColor(Color.rgb(111,111,111));
        login_password.setHintTextColor(Color.rgb(111,111,111));

        Typeface roboto_ligth = Typeface.createFromAsset(getAssets(), "fonts/roboto-light.ttf");
        Typeface roboto_bold = Typeface.createFromAsset(getAssets(), "fonts/roboto-bold.ttf");

        //login_cedula.setTypeface(roboto_ligth);
        login_password.setTypeface(roboto_ligth);

        db = new ManejadorBD(this);
        /*
        db.delete("patients");
        db.delete("evaluadores");
        db.delete("schemas");
        db.delete("daily_schemas");
        db.delete("prescripciones");
        db.delete("antecedentes");
        db.delete("develop_symptom");
        db.delete("schedule_taking");
        db.delete("basic_adverse_effect");
        db.delete("uicer_form");
        db.delete("uicer_img");
        */
        //PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();

        main_padre = (RelativeLayout) findViewById(R.id.main_padre);
        main_logo  = (ImageView) findViewById(R.id.main_logo);

        //PreferenceManager.getDefaultSharedPreferences(this).edit().clear();
        //Intent ser = new Intent(this, CloudinaryHandler.class);
        //getApplicationContext().startService(ser);



    }

    public void doEntrar(View view) {

            Evaluador rater = new Evaluador(UUID.randomUUID().toString(), "", "", login_password.getText().toString(), Calendar.getInstance().getTime());
            //Evaluador rater = new Evaluador("fe791bb2-99e0-45b3-bfb2-908f722b278d", "Domiciano", "Rincon", "1143848922", Calendar.getInstance().getTime());
            //Evaluador rater = new Evaluador("441a3fea-ff0b-497f-b166-da011d486d15", "mabel", "castillo", "31573941", Calendar.getInstance().getTime());

            if(rater.getCedula()!="" || (rater.getName()!="" && rater.getLastName()!="")){
                SessionControllerAsyncTask session = new SessionControllerAsyncTask();
                session.execute(rater, this);
            }else{
                Toast.makeText(getApplicationContext(), "Escriba su nombre y apellido, junto con su número de cédula.", Toast.LENGTH_SHORT).show();
            }

    }


    private class SessionControllerAsyncTask extends AsyncTask<Object, String, Evaluador> {
        @Override
        protected void onPreExecute() {
            dialog.show();

            b.setAlpha(0.5f);
            b.setText("Cargando");
            b.setEnabled(false);
        }

        /*if(paramses.length == 2 && paramses[0] instanceof Evaluador && paramses[1] instanceof Activity)*/
        @Override
        protected Evaluador doInBackground(Object... paramses) {
            if(paramses.length == 2 && paramses[0] instanceof Evaluador && paramses[1] instanceof Activity){
                SessionController sessionController = new SessionController();
                boolean sesion = sessionController.controller(paramses);
                if(sesion && paramses[0] != null){
                    return (Evaluador) paramses[0];
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Evaluador rater) {
                if(dialog != null)
                dialog.dismiss();

            if (rater != null) {
                Evaluador user = db.getRater(rater.getCedula());
                if(user != null){
                    Toast.makeText(getApplicationContext(), "Sus datos han sido validados correctamente.", Toast.LENGTH_SHORT).show();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("rater_id",rater.getCedula()).commit();
                    Intent intent = new Intent(getApplicationContext(), Evaluacion.class);
                    intent.putExtra("evaluador", user);
                    startActivity(intent);

                    Intent ser = new Intent(getApplicationContext(), CloudinaryHandler.class);
                    //getApplicationContext().startService(ser);

                    finish();
                }else {
                    Button b = (Button) findViewById(R.id.buttonEnter);
                    b.setEnabled(true);
                    b.setAlpha(1);
                    b.setText("ENTRAR");
                    //Toast.makeText(getApplicationContext(), "El usuario no existe o la contraseña está mal escrita", Toast.LENGTH_SHORT).show();
                }

            } else {
                Button b = (Button) findViewById(R.id.buttonEnter);
                b.setEnabled(true);
                b.setAlpha(1);
                b.setText("ENTRAR");
                //Toast.makeText(getApplicationContext(), "El usuario no existe o la contraseña está mal escrita", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void abrirActividad(Evaluador rater){
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("rater_id",rater.getCedula()).commit();
        Intent i = new Intent(this, Evaluacion.class);
        startActivity(i);
        finish();
    }




}
