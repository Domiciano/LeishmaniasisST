package icesi.i2t.leishmaniasisst.dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import icesi.i2t.leishmaniasisst.R;

/**
 * Created by Domiciano on 26/05/2016.
 */
public class ListDialog extends DialogFragment implements View.OnClickListener{

    String texto;
    String[] lista;
    int dias;

    int numeroDeAmpollas=0;


    ArrayAdapter<String> adapter;

    OnDialogDismiss onDialogDismiss;
    Button ok;
    TextView list_dialog_texto;
    Spinner medicamento;

    public static ListDialog newInstance(String texto, String[] lista, int dias) {
        ListDialog f = new ListDialog();

        Bundle args = new Bundle();
        args.putString("texto", texto);
        args.putStringArray("lista", lista);
        args.putInt("dias", dias);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.texto = getArguments().getString("texto");
        this.lista = getArguments().getStringArray("lista");
        this.dias = getArguments().getInt("dias");
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
        return d;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_lista, container, false);
        ok = (Button) v.findViewById(R.id.ok_dialog_lista);

        list_dialog_texto = (TextView) v.findViewById(R.id.list_dialog_texto);

        this.texto = this.texto.replace("(1?)",""+this.dias);

        list_dialog_texto.setText(this.texto);

        ok.setOnClickListener(this);

        medicamento = (Spinner) v.findViewById(R.id.sp_dialog_lista);
        adapter = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, this.lista);
        medicamento.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return v;
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        if(b.equals(ok)){
            onDialogDismiss.finish(medicamento.getSelectedItem().toString());
        }
    }

    public interface OnDialogDismiss{
        void finish(String sintoma);
    }

    public void setOnDialogDismiss(OnDialogDismiss onDialogDismiss){
        this.onDialogDismiss = onDialogDismiss;
    }
}

