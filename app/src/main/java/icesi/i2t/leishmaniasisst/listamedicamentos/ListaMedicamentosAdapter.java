package icesi.i2t.leishmaniasisst.listamedicamentos;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import icesi.i2t.leishmaniasisst.R;
import icesi.i2t.leishmaniasisst.dialogs.BooleanAnswerDialog;

/**
 * Created by Domiciano on 26/05/2016.
 */
public class ListaMedicamentosAdapter extends BaseAdapter{
    ArrayList<String> medicamentos;
    Context context;
    AppCompatActivity activity;
    LayoutInflater inflater=null;


    OnDeleteMember listenerMember;

    public interface OnDeleteMember{
        void onDelete(String result);
    }
    public void setOnDeleteMember(OnDeleteMember onDeleteMember){
        listenerMember = onDeleteMember;
    }



    public ListaMedicamentosAdapter(AppCompatActivity activity, Context context, ArrayList<String> medicamentos) {
        this.medicamentos = medicamentos;
        this.context = context;
        this.activity = activity;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return medicamentos.size();
    }

    @Override
    public Object getItem(int position) {
        return medicamentos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(vi == null) vi = inflater.inflate(R.layout.item_lista_sintomas, null);

        //ARMAR TODOS LOS
        TextView text_item_sintoma = (TextView) vi.findViewById(R.id.text_item_sintoma);
        Button eliminar_sintoma_boton = (Button) vi.findViewById(R.id.eliminar_sintoma_boton);

        eliminar_sintoma_boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                BooleanAnswerDialog dialog = BooleanAnswerDialog.newInstance("¿Está seguro que desea eliminar "+medicamentos.get(position)+"?");
                dialog.show(ft, "dialog_fiebre");
                dialog.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
                    @Override
                    public void finish(String salida){
                        switch (salida){
                            case "SI":
                                listenerMember.onDelete(medicamentos.remove(position));
                                notifyDataSetChanged();
                                break;
                            case "NO":

                        }
                    }
                });



            }
        });

        //CARGAR INFROMACION
        text_item_sintoma.setText(medicamentos.get(position));


        return vi;
    }

    public boolean agregar(String medicamento) {
        for(int i=0 ; i<medicamentos.size() ; i++){
            if(medicamentos.get(i).equals(medicamento)){
                Toast.makeText(context, "¡"+medicamento+" ya está en la lista!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if(medicamento.equals("Seleccione uno")){
            Toast.makeText(context, "¡Seleccione un medicamento!", Toast.LENGTH_SHORT).show();
            return false;
        }
        this.medicamentos.add(medicamento);
        notifyDataSetChanged();
        return true;
    }

    public void clear_sintomas() {
        this.medicamentos.clear();
    }
}
