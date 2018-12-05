package icesi.i2t.leishmaniasisst.cloudinary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import icesi.i2t.leishmaniasisst.R;
import icesi.i2t.leishmaniasisst.util.LeishConstants;


public class CloudinaryHandler extends Service {
    Cloudinary cloudinary;
    File carpeta, carpeta_subidas;
    NotificationCompat.Builder noteBuilder;
    NotificationManager notificationManager;

    public static int notifyID = 10;
    final static String CHANNEL_ID = "CANAL_LEISH";
    final static CharSequence name = "Leishmaniasis";
    public static int importance = NotificationManager.IMPORTANCE_DEFAULT;

    public void startService() {
        Task k = new Task();
        k.execute();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Cloud de prueba
        Map config = new HashMap();
        //config.put("cloud_name", "universidad-icesi");
        //config.put("api_key", "117557741559213");
        //config.put("api_secret", "ilsEylOyiEKhq1dajOCHeTqIASA");
        config.put("cloud_name", "dodcgaskp");
        config.put("api_key", "451634862117428");
        config.put("api_secret", "Kmf6g1YhjqBts_a3_F-pjxp3PqE");

        cloudinary = new Cloudinary(config);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // The user-visible name of the channel.
            NotificationChannel miCanal = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(miCanal);
        }else{
            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        noteBuilder = new NotificationCompat.Builder(CloudinaryHandler.this, CHANNEL_ID)
                .setContentTitle("Guaral+ST")
                .setContentText("Subiendo fotos...")
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.logo_cabezote);


        startForeground(10, noteBuilder.build());

        startService();

        return (START_NOT_STICKY);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class Task extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            noteBuilder.setProgress(100, 0, false);
            notificationManager.notify(notifyID, noteBuilder.build());
        }

        @Override
        protected String doInBackground(String... params) {

            File carpeta = new File(Environment.getExternalStorageDirectory().toString() + "/" + LeishConstants.FOLDER +"/");
            ArrayList<File> archivos = getFiles(carpeta);

            if (archivos.size() == 0) {
                stopForeground(true);
                return "NO_UPLOADS";
            }

            for (int i = 0; i < archivos.size(); i++) {
                try {
                    Map par = ObjectUtils.asMap("use_filename", true,
                            "unique_filename", false);
                    cloudinary.uploader().upload(archivos.get(i), par);
                    //archivos.get(i).delete();
                    moverArchivo(archivos.get(i));
                    publishProgress("" + (((i + 1) / (double) archivos.size()) * 100));
                } catch (Exception e) {
                    Log.e("ERROR_SERVICE", e.getLocalizedMessage());
                }
            }
            return "SUCCESS";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            int progreso = (int) (Double.parseDouble(values[0]));
            noteBuilder.setProgress(100, progreso, false);
            noteBuilder.setContentText("Subiendo fotos, espere un momento");
            notificationManager.notify(notifyID, noteBuilder.build());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("SUCCESS")) {

                stopForeground(false);
                noteBuilder = new NotificationCompat.Builder(CloudinaryHandler.this, CHANNEL_ID)
                        .setContentText("La carga de imágenes ha finalizado")
                        .setAutoCancel(true)
                        .setOngoing(false)
                        .setSmallIcon(R.mipmap.logo_cabezote);
                notificationManager.notify(notifyID, noteBuilder.build());

            } else if (s.equals("NO_UPLOADS")) {
                stopForeground(false);
                noteBuilder = new NotificationCompat.Builder(CloudinaryHandler.this, CHANNEL_ID)
                        .setContentText("No hay imagenes para sincronizar")
                        .setAutoCancel(true)
                        .setOngoing(false)
                        .setSmallIcon(R.mipmap.logo_cabezote);
                notificationManager.notify(notifyID, noteBuilder.build());
            }
            //stopSelf();
        }

        public ArrayList<File> getFiles(File directory) {



            //Conteo de archivos
            ArrayList<File> array = new ArrayList<>();

            if(!directory.isDirectory()) return array;
            if(directory.list() == null) return array;

            for (int i = 0; i < directory.list().length; i++) {
                if (!(new File(directory + "/" + directory.list()[i])).isDirectory()) {
                    array.add(new File(directory + "/" + directory.list()[i]));
                }
            }
            return array;
        }
    }

    private void moverArchivo(File file) {
        String name = file.getName();
        File folder = new File(Environment.getExternalStorageDirectory()+"/"+LeishConstants.FOLDERUPDLOAD+"/");
        if(!folder.exists()){
            folder.mkdirs();
        }
        file.renameTo(new File(Environment.getExternalStorageDirectory()+"/"+LeishConstants.FOLDERUPDLOAD+"/"+name));
    }
}
