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
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import icesi.i2t.leishmaniasisst.R;

/**
 * Created by Domiciano on 07/06/2016.
 */
public class ConfirmacionDialog extends DialogFragment implements View.OnClickListener{

        OnDialogDismiss onDialogDismiss;
        String mensaje, confirmacion;


        public static ConfirmacionDialog newInstance(String msj, String confirmacion) {
            ConfirmacionDialog f = new ConfirmacionDialog();
            Bundle args = new Bundle();
            args.putString("mensaje", msj);
            args.putString("confirmacion", confirmacion);
            f.setArguments(args);
            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mensaje = getArguments().getString("mensaje");
            confirmacion = getArguments().getString("confirmacion");
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

        Button ok;
        TextView TVmsj;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.dialog_confirmacion, container, false);
            ok = (Button) v.findViewById(R.id.confirmacion_dialog);
            ok.setText(confirmacion);
            ok.setOnClickListener(this);
            TVmsj = (TextView) v.findViewById(R.id.dialog_confirmacion_texto);
            TVmsj.setText(mensaje);
            return v;
        }

        @Override
        public void onClick(View v) {
            Button b = (Button) v;
            if(b.equals(ok)){
                dismiss();
                onDialogDismiss.finish("");
            }
        }

        public interface OnDialogDismiss{
            public void finish(String salida);
        }

        public void setOnDialogDismiss(OnDialogDismiss onDialogDismiss){
            this.onDialogDismiss = onDialogDismiss;
        }






}


