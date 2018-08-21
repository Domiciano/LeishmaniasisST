package icesi.i2t.leishmaniasisst.calendario;

import java.util.Calendar;

/**
 * Created by Domiciano on 18/05/2016.
 */
public class DayOnGridModel {

    public static final int NO_EVALUADO=0;
    public static final int MEDICAMENTO_NO_TOMADO=1;
    public static final int MEDICAMENTO_TOMADO=2;

    Calendar calendar;
    int medicamento_tomado=NO_EVALUADO;
    private int unidades_tomadas = 0;

    //---------------------------->

    public int getUnidades_tomadas() {
        return unidades_tomadas;
    }

    public void setUnidades_tomadas(int unidades_tomadas) {
        this.unidades_tomadas = unidades_tomadas;
    }

    public DayOnGridModel(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getMedicamento_tomado() {
        return medicamento_tomado;
    }

    public void setMedicamento_tomado(int medicamento_tomado) {
        this.medicamento_tomado = medicamento_tomado;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
