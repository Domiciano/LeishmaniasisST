package icesi.i2t.leishmaniasisst.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import icesi.i2t.leishmaniasisst.DatosPacienteActivity;
import icesi.i2t.leishmaniasisst.R;
import icesi.i2t.leishmaniasisst.data.ManejadorBD;
import icesi.i2t.leishmaniasisst.listapacientes.ListaPacientesAdapter;
import icesi.i2t.leishmaniasisst.listapacientes.PacienteListModel;
import icesi.i2t.leishmaniasisst.model.DailySchema;
import icesi.i2t.leishmaniasisst.model.Evaluador;
import icesi.i2t.leishmaniasisst.model.Paciente;
import icesi.i2t.leishmaniasisst.model.Prescripcion;
import icesi.i2t.leishmaniasisst.model.Schema;
import icesi.i2t.leishmaniasisst.model.UIcerImg;
import icesi.i2t.leishmaniasisst.model.UlcerForm;

/**
 * Created by Domiciano on 17/05/2016.
 */
public class FragmentTomaDeFotos extends Fragment {

    ProgressDialog dialog;

    ListView lista_pacientes;
    ArrayList<PacienteListModel> pacientes;
    ManejadorBD db;
    Evaluador rater;
    ListaPacientesAdapter adapter;
    TextView tv;
    String html;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skin_fragment_toma_fotos, container, false);
        tv = (TextView) view.findViewById(R.id.bienvenida_rater_fotos);

        dialog = new ProgressDialog(getContext(), R.style.ProgressStyle);
        dialog.setMessage("Espera mientras se llena la lista");
        dialog.setCancelable(false);

        lista_pacientes = (ListView) view.findViewById(R.id.lista_pacientes_fotos);
        pacientes = new ArrayList<>();
        adapter = new ListaPacientesAdapter(getContext(), pacientes);
        lista_pacientes.setAdapter(adapter);
        db = new ManejadorBD(getContext());
        //Agregar pacientes a la lista

        rater = db.getRater(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("rater_id", ""));

        html = "Hola <b>"+rater.getName()+" "+rater.getLastName()+"</b>. A continuación se listan los pacientes asignados para toma de fotos";
        tv.setText(Html.fromHtml(html));


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
                if(pacientes.get(position).isActive()){
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    prefs.edit().putString("paciente_id", pacientes.get(position).getId()).putBoolean("toma_fotos_terminada", true).commit();
                    Intent i = new Intent(getContext(), DatosPacienteActivity.class);
                    //0->Evaluacion de 0 a 28 dias
                    //1->Toma de fotos
                    i.putExtra("tipo_ventana",1);
                    startActivity(i);
                }else{
                    Toast.makeText(getContext(), "El paciente no tiene toma de fotos para el día de hoy", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
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

    public boolean isEvaluationComplete(Paciente p){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String fecha_prueba = format.format(Calendar.getInstance().getTime());
            Schema s = db.buscarSchemaActivoDelPaciente(p);
            if(s == null) return false;
            DailySchema ds = db.buscarDailySchema(s.getUuid(), format.parse(fecha_prueba));
            if(ds == null) return false;
            UlcerForm ulcerForm = db.getListaImagenesForm(ds.getUuid()).get(0);
            if(ulcerForm == null) return false;
            List<UIcerImg> imgs = db.getListaImagenes(ulcerForm.getUiid());
            if(imgs == null) return false;
            if(imgs.size() == 0) return false;
            else {
                for(UIcerImg i: imgs){
                    if(i.getImgUUID().equals("00000000-0000-0000-0000-000000000000")) return false;

                }
                return true;
            }
        }catch (ParseException e){
            Log.e("",e.getLocalizedMessage());
        }
        return false;
    }



    public class HiloListaPacientes extends Thread{
        @Override
        public void run() {
            List<Paciente> lista_pacientes_db = db.getListaPacientes(rater.getUuid());
            for(Paciente p : lista_pacientes_db){
                pacientes.add(new PacienteListModel(p.getCedula(),
                        p.getName()+" "+p.getLastName(),
                        db.getNumeroUlcerFormsEnDailySchemas(p),
                        db.getTotalDiasDeFotos(p),
                        true,
                        isEvaluationComplete(p))
                );
                lista_pacientes.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }




    public class GetListaPacientes extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(getContext(), "INICIO", Toast.LENGTH_SHORT).show();
            dialog.show();
            pacientes.clear();
        }

        @Override
        protected String doInBackground(String... params) {

            List<Paciente> lista_pacientes_db = db.getListaPacientes(rater.getUuid());
            for(Paciente p : lista_pacientes_db){
                pacientes.add(new PacienteListModel(p.getCedula(),
                        p.getName()+" "+p.getLastName(),
                        db.getNumeroUlcerFormsEnDailySchemas(p),
                        db.getTotalDiasDeFotos(p),
                        db.pacienteTieneFotosPendientes(p),
                        isEvaluationComplete(p)));
            }
            //pacientes.add(new PacienteListModel("Susanita Perez", ""+8, ""+28, true, false));


            publishProgress("");

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
        }
    }

}
