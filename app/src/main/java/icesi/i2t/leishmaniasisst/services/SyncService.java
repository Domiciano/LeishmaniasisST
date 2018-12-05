package icesi.i2t.leishmaniasisst.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import icesi.i2t.leishmaniasisst.R;
import icesi.i2t.leishmaniasisst.cloudinary.CloudinaryHandler;
import icesi.i2t.leishmaniasisst.data.ManejadorBD;
import icesi.i2t.leishmaniasisst.model.Evaluador;

import static java.security.AccessController.getContext;


/**
 * Created by Juan.
 */

public class SyncService extends Service {

    Notification.Builder note;
    NotificationManager mNotifyManager;
    ManejadorBD db;

    public SyncService() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        note=new Notification.Builder(this)
                .setContentTitle("Guaral+ST")
                .setContentText("Sincronizando...")
                .setAutoCancel(true)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.logo_cabezote);


        try {
            db = new ManejadorBD(this);
            Evaluador min_rater = (Evaluador) (intent.getSerializableExtra("rater"));
            Evaluador rater = db.getFullRater(min_rater);

            if (rater != null) {
                startForeground(11, note.build());
                startSync(rater);
            } else {
                stopForeground(false);
                stopService();
            }
        }catch (Exception e){
            Log.e("ERROR SYN SERVICE", e.getLocalizedMessage());
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startSync(Evaluador rater) {
        try {
            new SendFileXmlTask().execute(rater);
        }catch (Exception e){
            Log.e("ERROR SYN SERVICE", e.getLocalizedMessage());
        }
    }

    public void stopService() {
        try {
            this.stopSelf();
        }catch (Exception e){
            Log.e("ERROR SYN SERVICE", e.getLocalizedMessage());
        }
    }

    private class SendFileXmlTask extends AsyncTask<Evaluador, String, String> {

        @Override
        protected String doInBackground(Evaluador... params) {
            String respuesta = "";
            try {
                NetworkController nController = new NetworkController();
                respuesta = nController.post(params[0], SyncService.this);
                return respuesta;
            } catch (Exception e) {
                respuesta = e.getMessage();
                Log.e("NO INTERNET", e.getLocalizedMessage());
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Intent intent = new Intent(BroadcastConstants.BROADCAST_ACTION).putExtra(BroadcastConstants.EXTENDED_DATA_STATUS, result);
                LocalBroadcastManager.getInstance(SyncService.this).sendBroadcast(intent);
                stopForeground(false);
                stopService();

                note=new Notification.Builder(getApplicationContext())
                        .setContentTitle("Guaral+ST")
                        .setContentText(result)
                        .setOngoing(false)
                        .setSmallIcon(R.mipmap.logo_cabezote);

                mNotifyManager.notify(11, note.build());

                Intent ser = new Intent(SyncService.this, CloudinaryHandler.class);
                SyncService.this.startService(ser);

            } catch (Exception e) {
                Log.e("ERROR SYN SERVICE", e.getLocalizedMessage());
            }
        }
    }
}
