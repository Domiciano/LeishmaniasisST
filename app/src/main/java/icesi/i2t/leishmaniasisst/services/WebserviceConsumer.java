package icesi.i2t.leishmaniasisst.services;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import icesi.i2t.leishmaniasisst.model.Evaluador;
import icesi.i2t.leishmaniasisst.model.Usuario;
import icesi.i2t.leishmaniasisst.util.LeishConstants;

public class WebserviceConsumer {
    public static final String POST_DOCUMENT = "https://i2thub.icesi.edu.co:5443/guaralst/document/a";
    public static final String POST_USER = "https://i2thub.icesi.edu.co:5443/guaralst/user/d";

    public static final String GET_USER_BY_NATID = "https://i2thub.icesi.edu.co:5443/guaralst/user/getbycedula/";

    private static final String GET_ALL_RATERS = "https://i2thub.icesi.edu.co:5443/guaralst/user/getallraters/";

    public interface ServerResponseReceiver {
        void onServerResponse(Object json);

    }

    public static class PostDocument extends Thread {
        private String json;
        private HTTPSWebUtilDomi util;

        ServerResponseReceiver observer;

        public void setObserver(ServerResponseReceiver observer) {
            this.observer = observer;
        }

        public PostDocument(String json) {
            this.json = json;
            util = new HTTPSWebUtilDomi();
        }

        @Override
        public void run() {
            try {
                String respuesta = util.POSTrequest(POST_DOCUMENT, json);
                if (respuesta != null) {
                    //Usuario usuario = gson.fromJson(json, Usuario.class);
                    //observer.onServerResponse(usuario);
                } else {
                    //observer.onServerResponse(null);
                }
            } catch (IOException ex) {
                Log.e("Error",""+ex);
                //observer.onServerResponse(Constants.IOEX);
            }

        }
    }


    public static class PostUser extends Thread {
        private String json;
        private HTTPSWebUtilDomi util;

        ServerResponseReceiver observer;

        public void setObserver(ServerResponseReceiver observer) {
            this.observer = observer;
        }

        public PostUser(String json) {
            this.json = json;
            util = new HTTPSWebUtilDomi();
        }

        @Override
        public void run() {
            try {
                String respuesta = util.POSTrequest(POST_USER, json);
                observer.onServerResponse(LeishConstants.OK);
            } catch (IOException ex) {
                Log.e("Error",""+ex);
                observer.onServerResponse(LeishConstants.IOEX);
            }

        }
    }


    public static class GetUsuarioByCedula extends Thread {
        private Gson gson;
        private String cedula;
        private HTTPSWebUtilDomi util;

        ServerResponseReceiver observer;

        public void setObserver(ServerResponseReceiver observer) {
            this.observer = observer;
        }

        public GetUsuarioByCedula(String cedula) {
            this.cedula = cedula;
            util = new HTTPSWebUtilDomi();
            gson = new Gson();
        }

        @Override
        public void run() {
            try {
                String json = util.GETrequest(GET_USER_BY_NATID + cedula);
                Usuario usuario = gson.fromJson(json, Usuario.class);
                if (usuario.name != null) {
                    observer.onServerResponse(usuario);
                } else {
                    observer.onServerResponse(null);
                }
            } catch (IOException ex) {
                Log.e("Error",""+ex);
                observer.onServerResponse(LeishConstants.IOEX);
            }

        }
    }

    public static class GetAllRaters extends Thread {
        private Gson gson;
        private HTTPSWebUtilDomi util;

        ServerResponseReceiver observer;

        public void setObserver(ServerResponseReceiver observer) {
            this.observer = observer;
        }

        public GetAllRaters() {
            util = new HTTPSWebUtilDomi();
            gson = new Gson();
        }

        @Override
        public void run() {
            try {
                //String json = util.GETrequest(GET_ALL_RATERS );

                Thread.sleep(2000);

                observer.onServerResponse(LeishConstants.OK);

            } catch (Exception ex) {
                Log.e("Error",""+ex);
                observer.onServerResponse(LeishConstants.IOEX);
            }

        }
    }

    public static class GetLastDocumentByRaterCedula extends Thread {
        private Gson gson;
        private String cedula;
        private HTTPSWebUtilDomi util;

        ServerResponseReceiver observer;

        public void setObserver(ServerResponseReceiver observer) {
            this.observer = observer;
        }

        public GetLastDocumentByRaterCedula(String cedula) {
            this.cedula = cedula;
            util = new HTTPSWebUtilDomi();
            gson = new Gson();
        }

        @Override
        public void run() {
            try {
                //String json = util.GETrequest(GET_USER_BY_NATID + cedula);
                Thread.sleep(1000);
                observer.onServerResponse(LeishConstants.OK);
            } catch (Exception ex) {
                Log.e("Error",""+ex);
                observer.onServerResponse(LeishConstants.IOEX);
            }

        }
    }


    public static class GetAllPatients extends Thread {
        private Gson gson;
        private HTTPSWebUtilDomi util;

        ServerResponseReceiver observer;

        public void setObserver(ServerResponseReceiver observer) {
            this.observer = observer;
        }

        public GetAllPatients() {
            util = new HTTPSWebUtilDomi();
            gson = new Gson();
        }

        @Override
        public void run() {
            try {
                //String json = util.GETrequest(GET_ALL_RATERS );

                Thread.sleep(2000);

                observer.onServerResponse(LeishConstants.OK);

            } catch (Exception ex) {
                Log.e("Error",""+ex);
                observer.onServerResponse(LeishConstants.IOEX);
            }

        }
    }

}
