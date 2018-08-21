package icesi.i2t.leishmaniasisst.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import icesi.i2t.leishmaniasisst.R;

/**
 * Created by Domiciano on 19/05/2016.
 */
public class SpinnerDialog extends DialogFragment implements View.OnClickListener{

    int dia_max = 5;
    String[] strings;
    OnDialogDismiss onDialogDismiss;

    String texto, unidad, opcion_inicial;
    int min, max;
    boolean acotado;

    TextView dialog_spinner_texto;
    Button ok;
    Spinner spinner_opciones;


    public static SpinnerDialog newInstance(String texto, String unidad, int min, int max, String opcion_inicial, boolean acotado) {
        SpinnerDialog f = new SpinnerDialog();
        // Supply num input as an argument.
        Bundle args = new Bundle();
            args.putString("texto", texto);
            args.putString("unidad", unidad);
            args.putInt("min", min);
            args.putInt("max", max);
            args.putString("opcion_inicial", opcion_inicial);
            args.putBoolean("acotado", acotado);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.texto = getArguments().getString("texto");
        this.unidad = getArguments().getString("unidad");
        this.min = getArguments().getInt("min");
        this.max = getArguments().getInt("max");
        this.opcion_inicial = getArguments().getString("opcion_inicial");
        this.acotado = getArguments().getBoolean("acotado");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;


        d.getWindow().setLayout((int)(screenWidth*0.8), 650);
        d.setCanceledOnTouchOutside(false);
        return d;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_spinner, container, false);
        spinner_opciones = (Spinner) v.findViewById(R.id.spinner_opciones);
        ok = (Button) v.findViewById(R.id.dialog_spinner_ok);
        ok.setOnClickListener(this);
        dialog_spinner_texto = (TextView) v.findViewById(R.id.dialog_spinner_texto);
        dialog_spinner_texto.setText(this.texto);

        strings = new String[2+max-min];

        strings[0] = "Elija un valor";

        for(int i=0 ; i<strings.length-1 ; i++){
            strings[i+1] = (min+i)+" "+this.unidad;
        }

        if(!acotado)
            strings[strings.length-1] += " o más";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, strings);
        spinner_opciones.setAdapter(adapter);
        spinner_opciones.setSelection(buscarOpcion(opcion_inicial));
        return v;
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        if(b.equals(ok)){
            if(spinner_opciones.getSelectedItemPosition() == 0)
                onDialogDismiss.finish("");
            else{
                onDialogDismiss.finish(spinner_opciones.getSelectedItem().toString());
            }
        }
    }

    public interface OnDialogDismiss{
        void finish(String salida);
    }

    public void setOnDialogDismiss(OnDialogDismiss onDialogDismiss){
        this.onDialogDismiss = onDialogDismiss;
    }

    public int buscarOpcion(String opcion){
        for(int i=0 ; i<strings.length ; i++){
            if (strings[i].equals(opcion)) return i;
        }
        return 0;
    }

}
