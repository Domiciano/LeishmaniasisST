package icesi.i2t.leishmaniasisst.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import icesi.i2t.leishmaniasisst.DatosPacienteActivity;
import icesi.i2t.leishmaniasisst.R;
import icesi.i2t.leishmaniasisst.cloudinary.CloudinaryHandler;
import icesi.i2t.leishmaniasisst.data.ManejadorBD;
import icesi.i2t.leishmaniasisst.listapacientes.ListaPacientesAdapter;
import icesi.i2t.leishmaniasisst.listapacientes.PacienteListModel;
import icesi.i2t.leishmaniasisst.model.DailySchema;
import icesi.i2t.leishmaniasisst.model.Evaluador;
import icesi.i2t.leishmaniasisst.model.Paciente;
import icesi.i2t.leishmaniasisst.model.Prescripcion;
import icesi.i2t.leishmaniasisst.model.Schema;
import icesi.i2t.leishmaniasisst.xml.IOXmlFile;

/**
 * Created by Domiciano on 17/05/2016.
 */
public class FragmentEvaluacion extends Fragment{

    ProgressDialog dialog;

    ListView lista_pacientes;
    ArrayList<PacienteListModel> pacientes;
    ListaPacientesAdapter adapter;

    ManejadorBD db;
    Evaluador rater;

    String html;
    TextView tv;

    private boolean allPacientsWasEvaluated() {
        boolean respuesta = true;
        if(!hiloIsAlive){
            for(PacienteListModel p : pacientes){
                if(p.isEvaluated() == false) return false;
            }
        }
        return respuesta;
    }

    private boolean atLeastOnePacientsWasEvaluated() {
        boolean respuesta = false;
        if(!hiloIsAlive){
            for(PacienteListModel p : pacientes){
                if(p.isEvaluated() == true) return true;
            }
        }
        return respuesta;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dialog = new ProgressDialog(getContext(), R.style.ProgressStyle);
        dialog.setMessage("Espera mientras se llena la lista");
        dialog.setCancelable(false);

        View view = inflater.inflate(R.layout.skin_fragment_evaluacion, container, false);
        tv = (TextView) view.findViewById(R.id.bienvenida_rater);
        tv.setText("Espere mientras se llena la lista...");

        lista_pacientes = (ListView) view.findViewById(R.id.lista_pacientes);
        pacientes = new ArrayList<>();
        adapter = new ListaPacientesAdapter(getContext(), pacientes);
        lista_pacientes.setAdapter(adapter);

        db = new ManejadorBD(getContext());


        //Evaluador rater = new Evaluador(UUID.randomUUID().toString(), "Juan David", "", "00001111", Calendar.getInstance().getTime());
        rater = db.getRater(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("rater_id",""));



        String nombre = rater.getName()+ " "+rater.getLastName();
        //Poner leyenda con nombre del evaluador

        //String nombre = "Juan David";
        html = "Hola <b>"+nombre+"</b>.\nA continuación se listan los pacientes asignados para evaluación";
        tv.setText(Html.fromHtml(html));

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Evaluador ev = db.getFullRater(rater);
                IOXmlFile io = new IOXmlFile();
                try {
                    io.WriteFileXml(ev, new File(Environment.getExternalStorageDirectory()+"/LeishST/prueba.xml"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intentService = new Intent(getContext(), SyncService.class);
                intentService.putExtra("rater", rater);
                getActivity().startService(intentService);

                Intent ser = new Intent(getContext(), CloudinaryHandler.class);
                getActivity().startService(ser);
                */
            }
        });

        //Agregar pacientes a la lista
        /*
        List<Paciente> lista_pacientes_db = db.getListaPacientes(rater.getUuid());


        for(Paciente p : lista_pacientes_db){
            pacientes.add(new PacienteListModel(
                    p.getCedula(),
                    p.getName()+" "+p.getLastName(),
                    db.getDayofTreatment(p),
                    db.getTotalDiasEvaluacionPorMedicamento(p),
                    db.thereIsEvaluationToday(p),
                    db.isEvaluationComplete(p)));
        }
        //pacientes.add(new PacienteListModel("Susanita Perez", ""+8, ""+28, true, false));
        adapter.notifyDataSetChanged();

        */
        if(thisFragmentIsShownFirst) {
            thisFragmentIsShownFirst=false;
            //GetListaPacientes h = new GetListaPacientes();
            //h.execute();
            HiloListaPacientes h = new HiloListaPacientes();
            h.start();
        }

        lista_pacientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(pacientes.get(position).isEvaluated()){
                    Toast.makeText(getContext(), "Hoy ya evaluó a este paciente", Toast.LENGTH_LONG).show();
                    return;
                }

                if(pacientes.get(position).isActive()){
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    prefs.edit().putString("paciente_id", pacientes.get(position).getId())
                            .commit();



                    Intent i = new Intent(getContext(), DatosPacienteActivity.class);
                    i.putExtra("tipo_ventana",0);

                    startActivity(i);
                }else{
                    Toast.makeText(getContext(), "El paciente no tiene evaluaciones programadas para hoy", Toast.LENGTH_LONG).show();
                }
            }
        });

        if(getActivity().getIntent().getExtras() != null){
            boolean evaluacion_terminada = getActivity().getIntent().getExtras().getBoolean("evaluacion_terminada", false);
            //if(evaluacion_terminada) ponerChulo();

        }


        //Intent ser = new Intent(getContext(), CloudinaryHandler.class);
        //getActivity().startService(ser);

        return view;
    }

    public void ponerChulo() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String paciente_id = prefs.getString("paciente_id","");

        for(int i=0 ; i<pacientes.size() ; i++){
            if(paciente_id.equals(pacientes.get(i).getId())){
                pacientes.get(i).setEvaluated(true);
                adapter.notifyDataSetChanged();
                break;
            }
        }

        prefs.edit().remove("paciente_id").commit();
    }

    public boolean isCedulaInShared(String cedula){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean id = prefs.getBoolean(cedula,false);
        return id;
    }

    boolean thisFragmentIsShownFirst=false;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && pacientes == null){
            thisFragmentIsShownFirst = true;
            return;
        }

        if(isVisibleToUser && pacientes.size() == 0){
            HiloListaPacientes h = new HiloListaPacientes();
            h.start();
        }
    }

    boolean hiloIsAlive=false;
    public class HiloListaPacientes extends Thread{
        @Override
        public void run() {
            hiloIsAlive = true;
            List<Paciente> lista_pacientes_db = db.getListaPacientes(rater.getUuid());

            for(Paciente p : lista_pacientes_db){
                /*
                pacientes.add(new PacienteListModel(
                        p.getCedula(),
                        p.getName()+" "+p.getLastName(),
                        db.getDayofTreatment(p),
                        db.getTotalDiasEvaluacionPorMedicamento(p),
                        db.thereIsEvaluationToday(p),
                        db.isEvaluationComplete(p)));
                */
                pacientes.add(new PacienteListModel(
                        p.getCedula(),
                        p.getName()+" "+p.getLastName(),
                        db.getDayofTreatment(p),
                        ""+db.getTotalDays(p),
                        db.thereIsEvaluationToday(p),
                        db.isEvaluationComplete(p)));
                        lista_pacientes.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
            }
            hiloIsAlive = false;
        }
    }

    public class GetListaPacientes extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            pacientes.clear();
        }

        @Override
        protected String doInBackground(String... params) {
            List<Paciente> lista_pacientes_db = db.getListaPacientes(rater.getUuid());

            for(Paciente p : lista_pacientes_db){
                /*
                pacientes.add(new PacienteListModel(
                        p.getCedula(),
                        p.getName()+" "+p.getLastName(),
                        db.getDayofTreatment(p),
                        db.getTotalDiasEvaluacionPorMedicamento(p),
                        db.thereIsEvaluationToday(p),
                        db.isEvaluationComplete(p)));
                */
                pacientes.add(new PacienteListModel(
                        p.getCedula(),
                        p.getName()+" "+p.getLastName(),
                        db.getDayofTreatment(p),
                        ""+db.getTotalDays(p),
                        db.thereIsEvaluationToday(p),
                        db.isEvaluationComplete(p)));
                        publishProgress("");
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            super.onPostExecute(s);
        }
    }


}
