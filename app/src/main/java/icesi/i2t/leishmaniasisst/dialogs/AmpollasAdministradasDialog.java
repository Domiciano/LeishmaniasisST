package icesi.i2t.leishmaniasisst.dialogs;

/**
 * Created by Domiciano on 18/05/2016.
 */

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import icesi.i2t.leishmaniasisst.R;

/**
 * Created by Domiciano on 17/05/2016.
 */
public class AmpollasAdministradasDialog extends DialogFragment implements View.OnClickListener{


    OnDialogDismiss onDialogDismiss;

    int dia;
    String mes;
    int ampollas;

    int numeroDeAmpollas=0;


    public static AmpollasAdministradasDialog newInstance(int dia, String mes, int ampollas) {
        AmpollasAdministradasDialog f = new AmpollasAdministradasDialog();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("dia", dia);
        args.putString("mes", mes);
        args.putInt("ampollas", ampollas);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dia = getArguments().getInt("dia");
        mes = getArguments().getString("mes");
        ampollas = getArguments().getInt("ampollas");
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

    TextView dialog_ampollas_texto;
    Button ampolla1, ampolla2, ampolla3, ampolla4, ninguna_ampolla, dialog_ampollas_ok;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_ampollas_administradas, container, false);
        dialog_ampollas_texto = (TextView) v.findViewById(R.id.dialog_ampollas_texto);
        ampolla1 = (Button) v.findViewById(R.id.ampolla1);
        ampolla2 = (Button) v.findViewById(R.id.ampolla2);
        ampolla3 = (Button) v.findViewById(R.id.ampolla3);
        ampolla4 = (Button) v.findViewById(R.id.ampolla4);
        ninguna_ampolla = (Button) v.findViewById(R.id.ninguna_ampolla);
        dialog_ampollas_ok = (Button) v.findViewById(R.id.dialog_ampollas_ok);

        ampolla1.setOnClickListener(this);
        ampolla2.setOnClickListener(this);
        ampolla3.setOnClickListener(this);
        ampolla4.setOnClickListener(this);

        if(ampollas<=1) ampolla2.setVisibility(View.GONE);
        if(ampollas<=2) ampolla3.setVisibility(View.GONE);
        if(ampollas<=3) ampolla4.setVisibility(View.GONE);

        ninguna_ampolla.setOnClickListener(this);
        dialog_ampollas_ok.setOnClickListener(this);

        String html = "Seleccione el número de ampollas  administradas el día <b>"+dia+" de "+mes+"</b>";
        dialog_ampollas_texto.setText(Html.fromHtml(html));
        return v;
    }

    @Override
    public void onClick(View v) {
        final Button b = (Button) v;

        b.animate().setDuration(100).scaleX(1.1f).scaleY(1.1f).withEndAction(new Runnable() {
            @Override
            public void run() {
                b.animate().setDuration(100).scaleX(1).scaleY(1);
            }
        });

        if(b.equals(ampolla1)){
            ampolla1.setBackgroundResource(R.mipmap.img_ampolla_seleccionada);
            ampolla2.setBackgroundResource(R.mipmap.img_ampolladeseleccionada);
            ampolla3.setBackgroundResource(R.mipmap.img_ampolladeseleccionada);
            ampolla4.setBackgroundResource(R.mipmap.img_ampolladeseleccionada);
            ninguna_ampolla.setBackgroundResource(R.mipmap.img_ninguna_grande_deseleccionada);
            numeroDeAmpollas = 1;
        }else if(b.equals(ampolla2)){
            ampolla1.setBackgroundResource(R.mipmap.img_ampolla_seleccionada);
            ampolla2.setBackgroundResource(R.mipmap.img_ampolla_seleccionada);
            ampolla3.setBackgroundResource(R.mipmap.img_ampolladeseleccionada);
            ampolla4.setBackgroundResource(R.mipmap.img_ampolladeseleccionada);
            ninguna_ampolla.setBackgroundResource(R.mipmap.img_ninguna_grande_deseleccionada);
            numeroDeAmpollas = 2;
        }else if(b.equals(ampolla3)){
            ampolla1.setBackgroundResource(R.mipmap.img_ampolla_seleccionada);
            ampolla2.setBackgroundResource(R.mipmap.img_ampolla_seleccionada);
            ampolla3.setBackgroundResource(R.mipmap.img_ampolla_seleccionada);
            ampolla4.setBackgroundResource(R.mipmap.img_ampolladeseleccionada);
            ninguna_ampolla.setBackgroundResource(R.mipmap.img_ninguna_grande_deseleccionada);
            numeroDeAmpollas = 3;

        }else if(b.equals(ampolla4)){
            ampolla1.setBackgroundResource(R.mipmap.img_ampolla_seleccionada);
            ampolla2.setBackgroundResource(R.mipmap.img_ampolla_seleccionada);
            ampolla3.setBackgroundResource(R.mipmap.img_ampolla_seleccionada);
            ampolla4.setBackgroundResource(R.mipmap.img_ampolla_seleccionada);
            ninguna_ampolla.setBackgroundResource(R.mipmap.img_ninguna_grande_deseleccionada);
            numeroDeAmpollas = 4;

        }else if(b.equals(ninguna_ampolla)){
            ampolla1.setBackgroundResource(R.mipmap.img_ampolladeseleccionada);
            ampolla2.setBackgroundResource(R.mipmap.img_ampolladeseleccionada);
            ampolla3.setBackgroundResource(R.mipmap.img_ampolladeseleccionada);
            ampolla4.setBackgroundResource(R.mipmap.img_ampolladeseleccionada);
            ninguna_ampolla.setBackgroundResource(R.mipmap.img_ninguna_grande_seleccionada);
            numeroDeAmpollas = 0;
        }
        else if(b.equals(dialog_ampollas_ok)){
            dismiss();
            //Resultados de las pastillas
            this.onDialogDismiss.finish(""+numeroDeAmpollas);
        }
    }

    public interface OnDialogDismiss{
        void finish(String resultado);
    }
    public void setOnDialogDismiss(OnDialogDismiss onDialogDismiss){
        this.onDialogDismiss = onDialogDismiss;
    }

}
