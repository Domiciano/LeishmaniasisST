package icesi.i2t.leishmaniasisst.calendario;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import icesi.i2t.leishmaniasisst.R;

/**
 * Created by Domiciano on 18/05/2016.
 */
public class GridCalendarioAdapter extends BaseAdapter {

    ArrayList<DayOnGridModel> dias;
    Context context;
    LayoutInflater inflater=null;

    public GridCalendarioAdapter(Context context, ArrayList<DayOnGridModel> dias) {
        this.dias = dias;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return dias.size();
    }

    @Override
    public Object getItem(int position) {
        return dias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(vi == null) vi = inflater.inflate(R.layout.calendar_item, null);

        //ARMAR TODOS LOS
        TextView lista_pacientes_nombre = (TextView) vi.findViewById(R.id.calendar_item_day);

        //CARGAR INFROMACION

        Date date = dias.get(position).getCalendar().getTime();
        Calendar c = Calendar.getInstance();

        if(isToday(dias.get(position).getCalendar().getTime(), c.getTime())){
            lista_pacientes_nombre.setText(""+dias.get(position).getCalendar().get(Calendar.DAY_OF_MONTH));
            lista_pacientes_nombre.setTextColor(Color.rgb(226,97,0));
            vi.setBackgroundColor(Color.rgb(239, 175, 99));
        }else if(date.before(c.getTime())){
            lista_pacientes_nombre.setText(""+dias.get(position).getCalendar().get(Calendar.DAY_OF_MONTH));
            lista_pacientes_nombre.setTextColor(Color.rgb(226,97,0));
            vi.setBackgroundColor(Color.rgb(239, 175, 99));
        }else if(date.after(c.getTime())){
            lista_pacientes_nombre.setText(""+dias.get(position).getCalendar().get(Calendar.DAY_OF_MONTH));
        }

        if(isToday(dias.get(position).getCalendar().getTime(), c.getTime()) || date.before(c.getTime())){
            if(dias.get(position).getMedicamento_tomado() == DayOnGridModel.MEDICAMENTO_NO_TOMADO) {
                lista_pacientes_nombre.setBackgroundResource(R.mipmap.img_dosis_no_tomada);
                lista_pacientes_nombre.setText("");
            }
            else if(dias.get(position).getMedicamento_tomado() == DayOnGridModel.MEDICAMENTO_TOMADO) {
                lista_pacientes_nombre.setBackgroundResource(R.mipmap.img_dosis_tomada);
                lista_pacientes_nombre.setText("");
            }
        }

        return vi;
    }

    public static boolean isToday(Date hoy, Date date){
        Calendar hoy_c = Calendar.getInstance();
        hoy_c.setTime(hoy);

        Calendar date_c = Calendar.getInstance();
        date_c.setTime(date);

        boolean a = hoy_c.get(Calendar.YEAR) == date_c.get(Calendar.YEAR);
        boolean b = hoy_c.get(Calendar.MONTH) == date_c.get(Calendar.MONTH);
        boolean c = hoy_c.get(Calendar.DAY_OF_MONTH) == date_c.get(Calendar.DAY_OF_MONTH);

        return a && b && c;
    }

}
