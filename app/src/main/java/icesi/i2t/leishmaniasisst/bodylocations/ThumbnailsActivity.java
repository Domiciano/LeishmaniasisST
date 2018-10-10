package icesi.i2t.leishmaniasisst.bodylocations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import icesi.i2t.leishmaniasisst.CuerpoHumanoActivity;
import icesi.i2t.leishmaniasisst.Evaluacion;
import icesi.i2t.leishmaniasisst.R;
import icesi.i2t.leishmaniasisst.data.ManejadorBD;
import icesi.i2t.leishmaniasisst.dialogs.BooleanAnswerDialog;
import icesi.i2t.leishmaniasisst.model.DailySchema;
import icesi.i2t.leishmaniasisst.model.Paciente;
import icesi.i2t.leishmaniasisst.model.Schema;
import icesi.i2t.leishmaniasisst.model.UIcerImg;
import icesi.i2t.leishmaniasisst.model.UlcerForm;
import icesi.i2t.leishmaniasisst.thumbnails.ThumbsAdapter;
import icesi.i2t.leishmaniasisst.util.GeneralUtils;
import icesi.i2t.leishmaniasisst.util.ImageUtils;

/**
 * Created by Domiciano on 02/06/2016.
 */
public class ThumbnailsActivity extends AppCompatActivity{

    int display_height;
    int display_width;
    GridView thumbs;
    TextView titulo_thumbs;
    File carpeta;

    int id_zona=-1;


    ThumbsAdapter adapter;

    ManejadorBD db;
    Paciente paciente;
    UlcerForm ulcerForm;

    SimpleDateFormat format_filter;
    String fecha_prueba;

    SimpleDateFormat format;
    String fecha_fotos="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thumbnails_activity);
        carpeta = new File(Environment.getExternalStorageDirectory()+"/LeishST");
        db = new ManejadorBD(this);

        format_filter = new SimpleDateFormat("yyyy-MM-dd");

        try{
            String id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "");
            paciente = db.buscarPaciente(id);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            fecha_prueba = format.format(Calendar.getInstance().getTime());
            Schema s = db.buscarSchemaActivoDelPaciente(paciente);
            DailySchema ds = db.buscarDailySchema(s.getUuid(), format.parse(fecha_prueba));
            if(ds != null) ulcerForm = db.getListaImagenesForm(ds.getUuid()).get(0);
        }catch (ParseException e){
            Log.e("ERROR",e.getLocalizedMessage());
        }

        titulo_thumbs = (TextView) findViewById(R.id.titulo_thumbs);
        titulo_thumbs.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("parte_actual","NO_TIT"));

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        id_zona = PreferenceManager.getDefaultSharedPreferences(this).getInt("id_zona",-1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_thumbs);
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

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        display_height = displayMetrics.heightPixels;
        display_width = displayMetrics.widthPixels;

        File carpeta = new File(Environment.getExternalStorageDirectory()+"/LeishST/");


        adapter = new ThumbsAdapter(this);
        thumbs = (GridView) findViewById(R.id.gridview_thumbs);
        thumbs.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        thumbs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openPicture(position);
            }
        });

        LoadThumbs hilo = new LoadThumbs();
        hilo.execute(carpeta);
    }

    private void openPicture(int position) {
        Intent i = new Intent(this, VistaPreviaFotoActivity.class);
        i.putExtra("foto_path", adapter.getRuta(position));
        i.putExtra("modo_eliminar_foto", true);
        startActivityForResult(i,11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == RESULT_OK){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String path = preferences.getString("last_foto","NO_FOTO");
            if(!path.equals("NO_FOTO")) {
                Intent i = new Intent(this, VistaPreviaFotoActivity.class);
                i.putExtra("foto_path", path);
                startActivity(i);
            }
        }else if(requestCode == 11 && resultCode == RESULT_OK){
            refresh();
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

    public class LoadThumbs extends AsyncTask<File, String, String>{
        @Override
        protected String doInBackground(File... params) {

            String cedula = paciente.getCedula();
            int id_zona = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("id_zona",-1);

            File carpeta = params[0];
            String[] archivos = carpeta.list();

            for(int i=0 ; i<archivos.length ; i++){
                if(archivos[i].contains("CC"+cedula) && archivos[i].contains("BP"+id_zona) && archivos[i].contains("DT"+fecha_prueba)){
                    String ruta = carpeta + "/" + archivos[i];
                    Bitmap b = ImageUtils.decodeSampledBitmapFromResource(ruta, 75, 75);
                    Bitmap croppedBitmap = null;
                    if (b.getHeight() >= b.getWidth()) {
                        croppedBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getWidth());
                    } else {
                        croppedBitmap = Bitmap.createBitmap(b, 0, 0, b.getHeight(), b.getHeight());
                    }
                    Bitmap b_scaled = Bitmap.createScaledBitmap(croppedBitmap, 128, 128, false);
                    adapter.addBitmap(b_scaled, ruta);
                    publishProgress("");
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //refresh();
    }

    public void refresh(){

        File carpeta = new File(Environment.getExternalStorageDirectory()+"/LeishST/");
        adapter.clear();
        LoadThumbs hilo = new LoadThumbs();
        hilo.execute(carpeta);
    }

    public void nuevaFoto(View view){
        openCamera(null);
    }

    String foto_code="error";
    File foto=null;
    public void openCamera(View view) {

        String cedula = paciente.getCedula();
        format = new SimpleDateFormat("yyyy-MM-dd");
        fecha_fotos = format.format(Calendar.getInstance().getTime());
        foto_code = "DT"+ fecha_fotos +"DT"+"CC"+cedula+"CC_"+"BP"+id_zona+"BP_"+UUID.randomUUID().toString();
        foto = new File(carpeta+"/"+foto_code+".jpg");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putString("last_foto",foto.toString())
                .putInt("id_zona",id_zona)
                .putString("foto_code",foto_code)
                .commit();

        Uri uri = Uri.fromFile(foto);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(i, 10);

    }

    public void doVolver(View v){

        finish();
        Intent intent = new Intent(getApplicationContext(), CuerpoHumanoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        PreferenceManager.getDefaultSharedPreferences(this).edit().remove("modo_nueva_lesion").remove("id_zona").commit();

        //ArrayList<UlcerForm> forms = db.getVentanaForms(paciente);

        //for(UlcerForm form : forms) {
            /*
            //1. Eliminar todas las UIcelImage que tengan el numero de lesion dado para el UlcerForm especifico
            List<UIcerImg> imagenes = db.getListaImagenesPorBodyLocation(form.getUiid(), id_zona);
            //2. Eliminar todas las fotos de ese dia, para luego agregar las nuevas.
            for (UIcerImg i : imagenes) db.deleteUlcerImage(i);
            */
        //}

        if(adapter.getCount() > 0) {
            //Incluir las que se hayan tomado
            /*
            String[] lista_files = carpeta.list();
            for (int i = 0; i < lista_files.length; i++) {
                //Solo añade las del paciente con CC, las de la zona id_zona y en la fecha fecha_prueba
                if (lista_files[i].contains("CC" + paciente.getCedula()) && lista_files[i].contains("BP" + id_zona) && lista_files[i].contains("DT" + fecha_prueba)) {
                    for (UlcerForm form : forms) {
                        String uuid = UUID.randomUUID().toString();
                        UIcerImg imagen = new UIcerImg(uuid, "" + id_zona, Calendar.getInstance().getTime(), "JPG", carpeta.list()[i], "1", form.getUiid());
                        db.agregarUIcerImg(imagen);
                    }
                }
            }
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("BP" + id_zona, true).commit();
            */

        }else{



            /*
            //Dejar una foto con el IMGUUID=00000000-0000-0000-0000-000000000000
            if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("modo_nueva_lesion",false)){
                UIcerImg marca = new UIcerImg(UUID.randomUUID().toString(), "" + id_zona, Calendar.getInstance().getTime(), "JPG", "00000000-0000-0000-0000-000000000000", "1", ulcerForm.getUiid());
                db.agregarUIcerImg(marca);
            }
            PreferenceManager.getDefaultSharedPreferences(this).edit().remove("BP"+id_zona).commit();

            if(id_zona >= 1 && id_zona <= 18)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("c_ok", false).commit();
            else if(id_zona >= 19 && id_zona <= 34)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("bd_ok", false).commit();
            else if(id_zona >= 35 && id_zona <= 50)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("bi_ok", false).commit();
            else if(id_zona >= 51 && id_zona <= 74)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("t_ok", false).commit();
            else if(id_zona >= 75 && id_zona <= 90)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("pd_ok", false).commit();
            else if(id_zona >= 91 && id_zona <= 106)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("pi_ok", false).commit();
            else if(id_zona >= 107 && id_zona <= 120)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("md_ok", false).commit();
            else if(id_zona >= 121 && id_zona <= 134)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("mi_ok", false).commit();
            */
        }

        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("modo_nueva_lesion",false)
                && adapter.getCount() > 0) {
            /*
            Set<String> nuevas_lesiones = PreferenceManager.getDefaultSharedPreferences(this).getStringSet("nuevas_lesiones", null);
            if(nuevas_lesiones == null){
                ArrayList<String> set = new ArrayList<>();
                PreferenceManager.getDefaultSharedPreferences(this).edit().putStringSet("nuevas_lesiones", GeneralUtils.arrayList2Set(set)).commit();
            }
            */

            /*
            String[] lista_files = carpeta.list();
            for(int i=0 ; i<lista_files.length ; i++) {
                if(lista_files[i].contains("CC"+paciente.getCedula()) && lista_files[i].contains("BP"+id_zona) && lista_files[i].contains("DT"+fecha_prueba)) {

                    for(UlcerForm form : forms) {
                        String uuid = UUID.randomUUID().toString();
                        UIcerImg imagen = new UIcerImg(uuid, "" + id_zona, Calendar.getInstance().getTime(), "JPG", carpeta.list()[i], "1", form.getUiid());
                        db.agregarUIcerImg(imagen);
                    }


                    Set<String> set = PreferenceManager.getDefaultSharedPreferences(this).getStringSet("nuevas_lesiones", null);
                    ArrayList<String> array = GeneralUtils.set2ArrayList(set);
                    if(!array.contains(uuid)) array.add(uuid);
                    Set<String> new_set = GeneralUtils.arrayList2Set(array);
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putStringSet("nuevas_lesiones", new_set).commit();

                }
            }
            */

            /*
            if(id_zona >= 1 && id_zona <= 18) {
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("c_ok", true).commit();
            }
            else if(id_zona >= 19 && id_zona <= 34) {
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("bd_ok", true).commit();
            }
            else if(id_zona >= 35 && id_zona <= 50) {
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("bi_ok", true).commit();
            }

            else if(id_zona >= 51 && id_zona <= 74) {
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("t_ok", true).commit();
            }

            else if(id_zona >= 75 && id_zona <= 90) {
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("pd_ok", true).commit();
            }
            else if(id_zona >= 91 && id_zona <= 106) {
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("pi_ok", true).commit();
            }
            else if(id_zona >= 107 && id_zona <= 120) {
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("md_ok", true).commit();
            }
            else if(id_zona >= 121 && id_zona <= 134) {
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("mi_ok", true).commit();
            }
            finish();
            Intent intent = new Intent(getApplicationContext(), CuerpoHumanoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            PreferenceManager.getDefaultSharedPreferences(this).edit().remove("modo_nueva_lesion").remove("id_zona").commit();
            return;
            */
        }

    }

    @Override
    public void onBackPressed() {
        doVolver(null);
    }

    @Override
    protected void onDestroy() {
        CuerpoHumanoActivity.eliminarFotosNoGuardadas();
        super.onDestroy();
    }
}
