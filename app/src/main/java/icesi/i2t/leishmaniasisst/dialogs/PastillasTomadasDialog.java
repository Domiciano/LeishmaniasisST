package icesi.i2t.leishmaniasisst.dialogs;

/**
 * Created by Domiciano on 18/05/2016.
 */

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import icesi.i2t.leishmaniasisst.R;
import icesi.i2t.leishmaniasisst.SintomasActivity;

/**
 * Created by Domiciano on 17/05/2016.
 */
public class PastillasTomadasDialog extends DialogFragment implements View.OnClickListener{

    int dia;
    String mes;
    OnDialogDismiss onDialogDismiss;
    int dosis;

    int numeroPastillasManana=0,numeroPastillasTarde=0, numeroPastillasNoche=0;

    public static PastillasTomadasDialog newInstance(int dia, String mes, int dosis) {
        PastillasTomadasDialog f = new PastillasTomadasDialog();
        Bundle args = new Bundle();
        args.putInt("dia", dia);
        args.putString("mes", mes);
        args.putInt("dosis", dosis);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dia = getArguments().getInt("dia");
        mes = getArguments().getString("mes");
        dosis = getArguments().getInt("dosis");
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

    Button pastilla_manana0, pastilla_manana1, pastilla_manana2;
    Button pastilla_tarde0, pastilla_tarde1, pastilla_tarde2;
    Button pastilla_noche0, pastilla_noche1, pastilla_noche2;
    Button pastilla_ninguna;
    Button dialog_pastillas_ok;

    LinearLayout renglon3_pastillas, renglon2_pastillas, renglon1_pastillas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_pastillas_tomadas, container, false);

        renglon1_pastillas = (LinearLayout) v.findViewById(R.id.renglon1_pastillas);
        renglon2_pastillas = (LinearLayout) v.findViewById(R.id.renglon2_pastillas);
        renglon3_pastillas = (LinearLayout) v.findViewById(R.id.renglon3_pastillas);

        pastilla_manana0 = (Button) v.findViewById(R.id.pastilla_manana0);
        pastilla_manana1 = (Button) v.findViewById(R.id.pastilla_manana1);
        pastilla_manana2 = (Button) v.findViewById(R.id.pastilla_manana2);


        pastilla_tarde0 = (Button) v.findViewById(R.id.pastilla_tarde0);
        pastilla_tarde1 = (Button) v.findViewById(R.id.pastilla_tarde1);
        pastilla_tarde2 = (Button) v.findViewById(R.id.pastilla_tarde2);


        pastilla_noche0 = (Button) v.findViewById(R.id.pastilla_noche0);
        pastilla_noche1 = (Button) v.findViewById(R.id.pastilla_noche1);
        pastilla_noche2 = (Button) v.findViewById(R.id.pastilla_noche2);
        pastilla_ninguna = (Button) v.findViewById(R.id.pastilla_ninguna);
        pastilla_ninguna.setBackgroundResource(R.mipmap.img_ninguna_peque_seleccionada);

        dialog_pastillas_ok = (Button) v.findViewById(R.id.dialog_pastillas_ok);

        pastilla_manana0.setOnClickListener(this);
        pastilla_manana1.setOnClickListener(this);
        pastilla_manana2.setOnClickListener(this);

        pastilla_tarde0.setOnClickListener(this);
        pastilla_tarde1.setOnClickListener(this);
        pastilla_tarde2.setOnClickListener(this);

        pastilla_noche0.setOnClickListener(this);
        pastilla_noche1.setOnClickListener(this);
        pastilla_noche2.setOnClickListener(this);

        pastilla_ninguna.setOnClickListener(this);

        dialog_pastillas_ok.setOnClickListener(this);

        TextView dialog_pastillas_texto = (TextView) v.findViewById(R.id.dialog_pastillas_texto);
        String html = "Seleccione el número de pastillas tomadas el día <b>"+dia+" de "+mes+"</b>";
        dialog_pastillas_texto.setText(Html.fromHtml(html));


        setNumeroPastillas();

        return v;
    }

    private void setNumeroPastillas() {
        if (dosis == 8){
            pastilla_noche2.setVisibility(View.GONE);
        }else if(dosis == 7){
            pastilla_noche1.setVisibility(View.GONE);
            pastilla_noche2.setVisibility(View.GONE);
        }else if(dosis == 6){
            renglon3_pastillas.setVisibility(View.GONE);
            pastilla_noche0.setVisibility(View.GONE);
            pastilla_noche1.setVisibility(View.GONE);
            pastilla_noche2.setVisibility(View.GONE);
        }else if(dosis == 5){
            pastilla_tarde2.setVisibility(View.GONE);
            renglon3_pastillas.setVisibility(View.GONE);
            pastilla_noche0.setVisibility(View.GONE);
            pastilla_noche1.setVisibility(View.GONE);
            pastilla_noche2.setVisibility(View.GONE);
        }else if(dosis == 4){
            pastilla_tarde1.setVisibility(View.GONE);
            pastilla_tarde2.setVisibility(View.GONE);
            renglon3_pastillas.setVisibility(View.GONE);
            pastilla_noche0.setVisibility(View.GONE);
            pastilla_noche1.setVisibility(View.GONE);
            pastilla_noche2.setVisibility(View.GONE);
        }else if(dosis == 3){
            renglon2_pastillas.setVisibility(View.GONE);
            pastilla_tarde0.setVisibility(View.GONE);
            pastilla_tarde1.setVisibility(View.GONE);
            pastilla_tarde2.setVisibility(View.GONE);
            renglon3_pastillas.setVisibility(View.GONE);
            pastilla_noche0.setVisibility(View.GONE);
            pastilla_noche1.setVisibility(View.GONE);
            pastilla_noche2.setVisibility(View.GONE);
        }else if(dosis == 2){
            pastilla_manana2.setVisibility(View.GONE);
            renglon2_pastillas.setVisibility(View.GONE);
            pastilla_tarde0.setVisibility(View.GONE);
            pastilla_tarde1.setVisibility(View.GONE);
            pastilla_tarde2.setVisibility(View.GONE);
            renglon3_pastillas.setVisibility(View.GONE);
            pastilla_noche0.setVisibility(View.GONE);
            pastilla_noche1.setVisibility(View.GONE);
            pastilla_noche2.setVisibility(View.GONE);
        }else if(dosis == 1){
            pastilla_manana1.setVisibility(View.GONE);
            pastilla_manana2.setVisibility(View.GONE);
            renglon2_pastillas.setVisibility(View.GONE);
            pastilla_tarde0.setVisibility(View.GONE);
            pastilla_tarde1.setVisibility(View.GONE);
            pastilla_tarde2.setVisibility(View.GONE);
            renglon3_pastillas.setVisibility(View.GONE);
            pastilla_noche0.setVisibility(View.GONE);
            pastilla_noche1.setVisibility(View.GONE);
            pastilla_noche2.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {
        final Button button = (Button) v;
        button.animate().setDuration(100).scaleX(1.1f).scaleY(1.1f).withEndAction(new Runnable() {
            @Override
            public void run() {
                button.animate().setDuration(100).scaleX(1).scaleY(1);
            }
        });

        if(button.equals(pastilla_manana0)){
            pastilla_manana0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana1.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_manana2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_tarde0.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_tarde1.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_tarde2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche0.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche1.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_ninguna.setBackgroundResource(R.mipmap.img_ninguna_peque_deseleccionada);
            numeroPastillasManana = 1;
        }
        else if(button.equals(pastilla_manana1)){
            pastilla_manana0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana1.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_tarde0.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_tarde1.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_tarde2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche0.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche1.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_ninguna.setBackgroundResource(R.mipmap.img_ninguna_peque_deseleccionada);
            numeroPastillasManana = 2;
        }else if(button.equals(pastilla_manana2)){
            pastilla_manana0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana1.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana2.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde0.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_tarde1.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_tarde2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche0.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche1.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_ninguna.setBackgroundResource(R.mipmap.img_ninguna_peque_deseleccionada);
            numeroPastillasManana = 3;
        }else if(button.equals(pastilla_tarde0)){
            pastilla_manana0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana1.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana2.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde1.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_tarde2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche0.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche1.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_ninguna.setBackgroundResource(R.mipmap.img_ninguna_peque_deseleccionada);
            numeroPastillasManana = 4;
        }else if(button.equals(pastilla_tarde1)){
            pastilla_manana0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana1.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana2.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde1.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche0.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche1.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_ninguna.setBackgroundResource(R.mipmap.img_ninguna_peque_deseleccionada);
            numeroPastillasManana = 5;
        }else if(button.equals(pastilla_tarde2)){
            pastilla_manana0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana1.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana2.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde1.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde2.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_noche0.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche1.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_ninguna.setBackgroundResource(R.mipmap.img_ninguna_peque_deseleccionada);
            numeroPastillasManana = 6;
        }else if(button.equals(pastilla_noche0)){
            pastilla_manana0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana1.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana2.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde1.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde2.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_noche0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_noche1.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_ninguna.setBackgroundResource(R.mipmap.img_ninguna_peque_deseleccionada);
            numeroPastillasManana = 7;
        }else if(button.equals(pastilla_noche1)){
            pastilla_manana0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana1.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana2.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde1.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde2.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_noche0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_noche1.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_noche2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_ninguna.setBackgroundResource(R.mipmap.img_ninguna_peque_deseleccionada);
            numeroPastillasManana = 8;
        }
        else if(button.equals(pastilla_noche2)){
            pastilla_manana0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana1.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_manana2.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde1.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_tarde2.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_noche0.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_noche1.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_noche2.setBackgroundResource(R.mipmap.img_pastilla_seleccionada);
            pastilla_ninguna.setBackgroundResource(R.mipmap.img_ninguna_peque_deseleccionada);
            numeroPastillasManana = 9;
        }
        else if(button.equals(pastilla_ninguna)){
            pastilla_manana0.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_manana1.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_manana2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_tarde0.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_tarde1.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_tarde2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche0.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche1.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_noche2.setBackgroundResource(R.mipmap.img_pastilla_deseleccionada);
            pastilla_ninguna.setBackgroundResource(R.mipmap.img_ninguna_peque_seleccionada);
            numeroPastillasManana = 0;
        }
        else if(button.equals(dialog_pastillas_ok)){
            onDialogDismiss.finish("" + numeroPastillasManana+numeroPastillasTarde+numeroPastillasNoche);
            dismiss();
        }

    }


    public interface OnDialogDismiss{
        void finish(String resultado);
    }
    public void setOnDialogDismiss(OnDialogDismiss onDialogDismiss){
        this.onDialogDismiss = onDialogDismiss;
    }
}