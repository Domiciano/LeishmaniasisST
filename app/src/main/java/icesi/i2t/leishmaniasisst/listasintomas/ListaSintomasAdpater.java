package icesi.i2t.leishmaniasisst.listasintomas;

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
import icesi.i2t.leishmaniasisst.data.ManejadorBD;
import icesi.i2t.leishmaniasisst.dialogs.BooleanAnswerDialog;

/**
 * Created by Domiciano on 24/05/2016.
 */



/**
 * Created by Domiciano on 17/05/2016.
 */
public class ListaSintomasAdpater extends BaseAdapter {

    ArrayList<String> sintomas;
    Context context;
    LayoutInflater inflater=null;
    AppCompatActivity activity;

    OnDeleteMember listenerMember;


    public interface OnDeleteMember{
        void onDelete(String result);
    }
    public void setOnDeleteMember(OnDeleteMember onDeleteMember){
        listenerMember = onDeleteMember;
    }


    public ListaSintomasAdpater(AppCompatActivity activity, Context context, ArrayList<String> sintomas) {
        this.sintomas = sintomas;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return sintomas.size();
    }

    @Override
    public Object getItem(int position) {
        return sintomas.get(position);
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
                BooleanAnswerDialog dialog = BooleanAnswerDialog.newInstance("¿Está seguro que desea eliminar "+sintomas.get(position)+"?");
                dialog.show(ft, "dialog_fiebre");
                dialog.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
                    @Override
                    public void finish(String salida) {
                        switch (salida){
                            case "SI":
                                listenerMember.onDelete(sintomas.remove(position));
                                notifyDataSetChanged();
                                break;
                            case "NO":

                        }
                    }
                });
            }
        });

        //CARGAR INFROMACION
        text_item_sintoma.setText(sintomas.get(position));


        return vi;
    }

    public boolean agregar(String sintoma) {
        if(sintoma.trim().isEmpty()){
            Toast.makeText(context, "¡Escriba algo en la casilla!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(sintomas.contains(sintoma.trim())){
            Toast.makeText(context, "Este Sintoma/Enfermedad ya está en la lista", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            sintomas.add(sintoma.trim());
            notifyDataSetChanged();
            return true;
        }
    }

    public void clear_sintomas() {
        this.sintomas.clear();
    }
}
