package icesi.i2t.leishmaniasisst.services;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import icesi.i2t.leishmaniasisst.R;
import icesi.i2t.leishmaniasisst.model.DataXml;
import icesi.i2t.leishmaniasisst.model.Evaluador;
import icesi.i2t.leishmaniasisst.xml.IOXmlFile;


/**
 * Created by Juan.
 */
public class NetworkController {

    private NetworkUtilities utilities;
    private List<String> contentStrings;
    private String authContentString;
    private List<String> logsResponse;
    private Calendar calendar;
    private SimpleDateFormat sdf;

    private static String LOG = "Log.txt";

    /**
     * Constructor del controlador de sincronización
     */
    public NetworkController() {
        utilities = new NetworkUtilities();
        contentStrings = new ArrayList<String>();
        logsResponse = new ArrayList<String>();
        authContentString = "";
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("HH:mm:ss");
    }

    /**
     * Envía cada XML uno activity uno activity la URL especificada.
     *
     * @throws ClientProtocolException
     * @throws IOException
     * @throws AuthenticationException
     * @throws SynchronizationException
     */
    public void syncContent() throws ClientProtocolException, IOException, AuthenticationException, SynchronizationException {
        HttpResponse responseHTTP = utilities.HttpPost(authContentString);
        int code = responseHTTP.getStatusLine().getStatusCode();
        String respuesta = EntityUtils.toString(responseHTTP.getEntity());
        if (code == 200) {
            logsResponse.add(sdf.format(calendar.getTime()) + " [" + respuesta + "]");
            if (respuesta.contains("&lt;url&gt;") == true && respuesta.contains("&lt;/url&gt;") == true) {

                String url = respuesta.substring(respuesta.indexOf("&lt;url&gt;"), respuesta.indexOf("&lt;/url&gt;")).replace("&lt;url&gt;", "");
                utilities.setStringURL(url);

                for (String content : contentStrings) {

                    responseHTTP = utilities.HttpPost(content);
                    code = responseHTTP.getStatusLine().getStatusCode();
                    respuesta = utilities.toStringResponse(responseHTTP);
                    logsResponse.add(sdf.format(calendar.getTime()) + " [" + respuesta + "]");
                    Log.d("SND response", respuesta);
                    if (code != 200)
                        throw new SynchronizationException("Ha ocurrido un error en la sincronización");
                }
            } else {
                throw new AuthenticationException("El usuario no ha superado el proceso de autenticación:\n\n" + respuesta);
            }
        } else {
            throw new SynchronizationException("Ha ocurrido un error en la sincronización:\n\n" + respuesta);
        }
    }

    public String SummarySyncResponses() {
        int ok = 0, bad = 0;
        int senden = contentStrings.size() + 1;
        String total = "";
        for (String respuestas : getLogsResponse()) {
            if (respuestas.contains("True") || respuestas.contains("true") || respuestas.contains("url"))
                ok++;
            else
                bad++;
        }

        //String message = ok == senden ? ok + " de " + senden + " enviado(s)" : "No se enviaron " + bad + " registros";
        String message = ok + " archivo(s) enviado(s)";
        total = "La sincronización ha finalizado:\n " + message;
        addToLog(getLogsResponse());

        return total;
    }

    /* LOG OPERATIONS */

    /* Creates and adds info to the log file*/
    public void addToLog(List<String> info){
        String fileName = LOG;
        File root = Environment.getExternalStorageDirectory();
        File outDir = new File(root.getAbsolutePath() + File.separator + fileName);
        if (!outDir.isDirectory()) {
            outDir.mkdir();
        }
        BufferedWriter writer = null;
        try {
            File outputFile = new File(outDir, fileName);
            writer = new BufferedWriter(new FileWriter(outputFile));
            for (String respuesta : info) {
                respuesta+= "\n";
                writer.write(respuesta.toCharArray());
            }
            //Toast.makeText(Context.getApplicationContext(),"Report successfully saved to: " + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            writer.flush(); writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendLog();
    }

    /* Send the log file */
    public void sendLog(){
        String name = LOG;
        //Send by sync
    }

    /**
     * Sincronización de objetos apartir de serialización.
     *
     * @param data objeto DataXml serializado activity envíar.
     * @throws IOException
     */
    public void synContent(DataXml data) throws IOException {
        HttpEntity entity = new SerializableEntity(data, true);
        utilities.HttpPost(entity);
    }

    /**
     * Toma un objeto DataXml y lo añade activity la lista interna de XML activity enviar cuando se realice la sincronización.
     *
     * @param data    Objeto DataXml activity enviar.
     * @param context Clase que representa el contexto de la aplicación
     * @throws Exception
     */
    public void parseToXml(DataXml data, Context context) throws Exception {

        String filename = UUID.randomUUID().toString();
        String content = "TEMP";

        FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        outputStream.write(content.getBytes());
        outputStream.close();

        File file = new File(context.getFilesDir().getAbsolutePath(), filename);

        if (file.exists()) {
            try {
                IOXmlFile io = new IOXmlFile();
                io.WriteFileXml(data, file);
                content = getFileContents(file);
                if (content.contains("LiderComunitarioXml"))
                    authContentString = content;
                else
                    contentStrings.add(content);
            } finally {
                file.delete();
            }
        }
    }

    /**
     * Lee el archivo recibido por parametros, contruye una cadena y elimina ese archivo.
     *
     * @param file <span/> archivo activity leer y eliminar.
     * @return cadena que representa el contenido del archivo.
     * @throws IOException
     */
    private String getFileContents(final File file) throws IOException {
        final InputStream inputStream = new FileInputStream(file);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder stringBuilder = new StringBuilder();

        boolean done = false;
        while (!done) {
            final String line = reader.readLine();
            done = (line == null);

            if (line != null) {
                stringBuilder.append(line);
            }
        }

        reader.close();
        inputStream.close();
        //file.delete();
        return stringBuilder.toString();
    }

    private String getResourceContents(final InputStream inputStream) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder stringBuilder = new StringBuilder();

        boolean done = false;
        while (!done) {
            final String line = reader.readLine();
            done = (line == null);

            if (line != null) {
                stringBuilder.append(line);
            }
        }

        reader.close();
        inputStream.close();
        //file.delete();
        return stringBuilder.toString();
    }


    public List<String> getLogsResponse() {
        return logsResponse;
    }


    public String parseObjectToXml(DataXml data, Context context) throws Exception {

        String response = "";
        String filename = UUID.randomUUID().toString();
        String content = "TEMP";

        FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        outputStream.write(content.getBytes());
        outputStream.close();

        File file = new File(context.getFilesDir().getAbsolutePath(), filename);

        if (file.exists()) {
            try {
                IOXmlFile io = new IOXmlFile();
                io.WriteFileXml(data, file);
                content = getFileContents(file);
                response = content;
            } finally {
                //file.delete();
            }
        }
        return response;
    }


    public DataXml postForFullRater(DataXml content, Context context) throws Exception {
        if(utilities.isNetworkAvailable(context) == true) {

            utilities.setStringURL("http://sndservices.azurewebsites.net/Modules/LeishmaniasisApp.svc/xml/Schemas");

            boolean xmlORjson = true;
            String str_content = "";
            if(xmlORjson) {
                str_content = this.parseObjectToXml(content, context);
            }else {
                str_content = utilities.parseInJson(content);
            }
            HttpResponse responseHTTP = utilities.HttpPost(str_content);
            int code = responseHTTP.getStatusLine().getStatusCode();

            String respuesta = EntityUtils.toString(responseHTTP.getEntity());
            if (code == 200) {
                logsResponse.add(sdf.format(calendar.getTime()) + " [" + respuesta + "]");
                Log.d("postForFullRater", "CODE: "+code+"\nRESPUESTA: "+respuesta);
                if(xmlORjson){
                    DataXml data = this.parseXmlToObject(respuesta, context);
                    return data;
                }else{
                    DataXml data = utilities.parseFromJson(respuesta);
                    return data;
                }

            } else {
                throw new SynchronizationException("Ha ocurrido un error en la sincronización:\n\n" + respuesta);
            }
        }else{
            throw new SynchronizationException("La red no se encuentra disponible, verifique su conexión activity la red antes.");
        }
    }

    public String post(DataXml content, Context context) throws Exception {
        if(utilities.isNetworkAvailable(context)==true) {
            boolean xmlORjson = false;
            String str_content = "";
            if(xmlORjson) {
                str_content = this.parseObjectToXml(content, context);
            }else {
                str_content = utilities.parseInJson(content);
            }
            utilities.setStringURL("http://sndservices.azurewebsites.net/Modules/LeishmaniasisApp.svc/json/Save");
            HttpResponse responseHTTP = utilities.HttpPost(str_content);
            int code = responseHTTP.getStatusLine().getStatusCode();

            String respuesta = EntityUtils.toString(responseHTTP.getEntity());
            if (code == 200) {
                Log.d("NetworkController.post", "CODE: "+code+"\nRESPUESTA: "+respuesta);
                return respuesta;
            } else {
                throw new SynchronizationException("Ha ocurrido un error en la sincronización:\n\n" + respuesta);
            }
        }else{
            throw new SynchronizationException("La red no se encuentra disponible, verifique su conexión activity la red antes.");
        }
    }



    public DataXml parseXmlToObject(String content, Context contexto) throws Exception {

        String filename = UUID.randomUUID().toString();

        FileOutputStream outputStream = contexto.openFileOutput(filename, Context.MODE_PRIVATE);
        outputStream.write(content.getBytes());
        outputStream.close();

        File file = new File(contexto.getFilesDir().getAbsolutePath(), filename);

        Evaluador data = new Evaluador(UUID.randomUUID().toString(), "", "", "", Calendar.getInstance().getTime());

        if (file.exists()) {
            try {
                IOXmlFile io = new IOXmlFile();
                data = (Evaluador) io.ReadFileXml(data, file);
            } finally {
                //file.delete();
                //System.out.println("H");
            }
        }
        return data;
    }

    public Evaluador prueba_emulador_sync(Context context){
        String xml = "";
        try {
            InputStream is = context.getResources().openRawResource(R.raw.prueba_daily);
            xml = getResourceContents(is);
            Evaluador eval = (Evaluador) parseXmlToObject(xml, context);
            return eval;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERROR PARSING XML", e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR PARSING XML", e.getLocalizedMessage());
        }
        return null;

    }



}
