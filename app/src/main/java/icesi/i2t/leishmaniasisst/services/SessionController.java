package icesi.i2t.leishmaniasisst.services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import icesi.i2t.leishmaniasisst.R;
import icesi.i2t.leishmaniasisst.data.ManejadorBD;
import icesi.i2t.leishmaniasisst.model.Evaluador;


/**
 * Created by Domiciano on 06/04/2016.
 */
public class SessionController {
    Activity activity;
    boolean sync_was_done = false;


    public boolean controller(Object... paramses) {
        boolean isDemoActive = true;
        boolean resultado = false;
        sync_was_done = false;

        activity = (Activity) paramses[1];
        Context contexto = activity.getApplicationContext();
        ManejadorBD db = new ManejadorBD(contexto);

        Evaluador rater = (Evaluador) paramses[0];
        if (rater != null) {
            Evaluador rater_db = db.getMinimizedUser(rater.getNationalId());
            if (rater_db != null) {              //--->Si el rater ya esta en la base de datos
                //SINCRONIZAR
                Evaluador full_rater = db.getFullRater(rater_db);

                if (full_rater != null && isDemoActive == false) {       //-->Si el full rater retono no nulo
                    //Activar el servicio
                    try {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "Enviando información a SND, puede tardar unos minutos...", Toast.LENGTH_SHORT).show();
                            }
                        });
                        synchronousUpload(full_rater);
                        if (sync_was_done) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Se subió la información correctamente. Esperando respuesta de SND", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        Log.e("Error synchro upload", e.getLocalizedMessage());
                    }
                }
            }

            //DESCARGA DEL MODELO
            NetworkController networkController = new NetworkController();
            try {
                Evaluador response = null;
                try {
                    if (isDemoActive) {
                        response = networkController.prueba_emulador_sync(contexto);
                    } else {
                        if (sync_was_done) {
                            //Se espera un tiempo prudencial de 15 segundos, si la sincronización salió bien
                            for (int k = 0; k < 15; k++) {
                                Thread.sleep(1000);
                                System.out.println(">>Tiempo de espera: " + (k + 1) + " segundos");
                            }
                        }

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "Descargando información de SND...", Toast.LENGTH_SHORT).show();
                            }
                        });

                        response = (Evaluador) networkController.postForFullRater(rater, contexto);

                    }
                } catch (Exception e) {
                    Log.e("NO INTERNET", e.getLocalizedMessage());
                }
                if (response != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Sincronizando, por favor espere", Toast.LENGTH_SHORT).show();
                        }
                    });
                    db.setFullRater(response);
                    //Gson g = new Gson();
                    //Log.e("MODELO",""+g.toJson(response));
                }
                //db.printAllUsers();
                //Evaluador ev = db.getFullRater(rater);
                Evaluador ev  = response;
                //Log.e("MODELO",">"+ev);

                if (ev != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Sinronización completada", Toast.LENGTH_SHORT).show();
                        }
                    });

                    resultado = true;
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Ocurrió un problema. Verifique su conexión a internet", Toast.LENGTH_SHORT).show();
                        }
                    });
                    resultado = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return resultado;
    }


    public void synchronousUpload(Evaluador rater) {
        Notification.Builder note;
        NotificationManager mNotifyManager = null;
        String respuesta = "";
        try {
            mNotifyManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            note = new Notification.Builder(activity.getApplicationContext())
                    .setContentTitle("Guaral+ST")
                    .setContentText("Sincronizando...")
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.logo_cabezote);
            mNotifyManager.notify(11, note.build());

            NetworkController nController = new NetworkController();
            respuesta = nController.post(rater, activity.getApplicationContext());

            Intent intent = new Intent(BroadcastConstants.BROADCAST_ACTION).putExtra(BroadcastConstants.EXTENDED_DATA_STATUS, respuesta);
            LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);
            //stopForeground(false);
            //stopService();

            note = new Notification.Builder(activity.getApplicationContext())
                    .setContentTitle("Guaral+ST")
                    .setContentText(respuesta)
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .setSmallIcon(R.mipmap.logo_cabezote);

            mNotifyManager.notify(11, note.build());
            sync_was_done = true;
        } catch (Exception e) {
            Log.e("ERROR SYN SERVICE", e.getLocalizedMessage());
            note = new Notification.Builder(activity.getApplicationContext())
                    .setContentTitle("Guaral+ST")
                    .setContentText(e.getLocalizedMessage())
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .setSmallIcon(R.mipmap.logo_cabezote);
            mNotifyManager.notify(11, note.build());
            sync_was_done = false;
        }

    }

}
