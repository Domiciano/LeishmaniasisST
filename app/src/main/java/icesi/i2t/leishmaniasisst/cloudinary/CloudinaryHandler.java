package icesi.i2t.leishmaniasisst.cloudinary;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import icesi.i2t.leishmaniasisst.R;
import icesi.i2t.leishmaniasisst.util.UriUtils;


public class CloudinaryHandler extends Service{
    Cloudinary cloudinary;
    File carpeta, carpeta_subidas;
    Notification.Builder note;
    NotificationManager mNotifyManager;

    public void startService(){
        Task k = new Task();
        k.execute();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Map config = new HashMap();
        config.put("cloud_name", "universidad-icesi");
        config.put("api_key", "117557741559213");
        config.put("api_secret", "ilsEylOyiEKhq1dajOCHeTqIASA");
        cloudinary = new Cloudinary(config);


        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        note=new Notification.Builder(this)
                .setContentTitle("Guaral+ST")
                .setContentText("Subiendo fotos...")
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.logo_cabezote);


        startForeground(10, note.build());

        startService();

        return(START_NOT_STICKY);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class Task extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            note.setProgress(100,0, false);
            mNotifyManager.notify(10, note.build());
        }

        @Override
        protected String doInBackground(String... params) {

            File carpeta = new File(Environment.getExternalStorageDirectory().toString() + "/LeishST");
            ArrayList<File> archivos = getFiles(carpeta);

            if(archivos.size() == 0){
                stopForeground(true);
                return "NO_UPLOADS";
            }

            for(int i=0 ; i<archivos.size() ; i++){
                try {
                    Map par = ObjectUtils.asMap("use_filename", true,
                                                "unique_filename", false);
                    cloudinary.uploader().upload(archivos.get(i), par);
                    archivos.get(i).delete();
                    publishProgress(""+(((i+1)/(double)archivos.size())*100));
                }catch (Exception e){
                    Log.e("ERROR_SERVICE",e.getLocalizedMessage());
                }
            }
            return "SUCCESS";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            int progreso = (int)(Double.parseDouble(values[0]));
            note.setProgress(100,progreso, false);
            note.setContentTitle("Guaral+ST");
            note.setContentText("Subiendo fotos, espere un momento");
            mNotifyManager.notify(10, note.build());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("SUCCESS")) {

                stopForeground(false);
                note = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Guaral+ST")
                        .setContentText("La carga de imágenes ha finalizado")
                        .setAutoCancel(true)
                        .setOngoing(false)
                        .setSmallIcon(R.mipmap.logo_cabezote);

                mNotifyManager.notify(10, note.build());
            }else if(s.equals("NO_UPLODAS")){

            }
        }

        public ArrayList<File> getFiles(File directory){
            //Conteo de archivos
            ArrayList<File> array = new ArrayList<>();
            for(int i=0 ; i<directory.list().length ; i++){
                if(!(new File(directory+"/"+directory.list()[i])).isDirectory()){
                    array.add(new File(directory+"/"+directory.list()[i]));
                }
            }
            return array;
        }
    }
}
