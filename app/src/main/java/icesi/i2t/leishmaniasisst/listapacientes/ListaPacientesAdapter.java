package icesi.i2t.leishmaniasisst.listapacientes;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import icesi.i2t.leishmaniasisst.R;

/**
 * Created by Domiciano on 17/05/2016.
 */
public class ListaPacientesAdapter extends BaseAdapter{

    ArrayList<PacienteListModel> pacientes;
    Context context;
    LayoutInflater inflater=null;

    public ListaPacientesAdapter(Context context, ArrayList<PacienteListModel> pacientes) {
        this.pacientes = pacientes;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return pacientes.size();
    }

    @Override
    public Object getItem(int position) {
        return pacientes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(vi == null) vi = inflater.inflate(R.layout.item_lista_pacientes, null);

        //ARMAR TODOS LOS PACIENTES
        TextView lista_pacientes_nombre = (TextView) vi.findViewById(R.id.lista_pacientes_nombre);
        TextView lista_pacientes_dia = (TextView) vi.findViewById(R.id.lista_pacientes_dia);
        TextView lista_pacientes_total = (TextView) vi.findViewById(R.id.lista_pacientes_total);
        TextView img_paciente_evaluado = (TextView) vi.findViewById(R.id.img_evaluated);

        img_paciente_evaluado.setVisibility(View.GONE);

        //CARGAR INFROMACION
        lista_pacientes_nombre.setText(pacientes.get(position).getNombre());
        lista_pacientes_dia.setText(pacientes.get(position).getDia());
        lista_pacientes_total.setText("/"+pacientes.get(position).getTotalTratamiento());


        if(!pacientes.get(position).isActive()) {

            lista_pacientes_nombre.setTextColor(ContextCompat.getColor(vi.getContext(), R.color.light_text));
            lista_pacientes_dia.setTextColor(ContextCompat.getColor(vi.getContext(), R.color.light_text));
            lista_pacientes_total.setTextColor(ContextCompat.getColor(vi.getContext(), R.color.light_text));
        }else{
            lista_pacientes_nombre.setTextColor(Color.BLACK);
            lista_pacientes_dia.setTextColor(Color.BLACK);
            lista_pacientes_total.setTextColor(Color.BLACK);
        }

        if (pacientes.get(position).isEvaluated()){
            img_paciente_evaluado.setVisibility(View.VISIBLE);
            lista_pacientes_dia.setVisibility(View.GONE);
            lista_pacientes_total.setVisibility(View.GONE);
        }else{
            img_paciente_evaluado.setVisibility(View.GONE);
            lista_pacientes_dia.setVisibility(View.VISIBLE);
            lista_pacientes_total.setVisibility(View.VISIBLE);
        }

        return vi;
    }

    public void setEvaluated(int pos){
        pacientes.get(pos).setEvaluated(true);
        notifyDataSetChanged();
    }



}
