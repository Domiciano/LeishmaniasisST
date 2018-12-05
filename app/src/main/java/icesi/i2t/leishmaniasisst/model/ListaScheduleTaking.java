package icesi.i2t.leishmaniasisst.model;


import java.util.ArrayList;
import java.util.List;


public class ListaScheduleTaking extends DataXml{

    List<ScheduleTaking> schedulesTaking = new ArrayList<ScheduleTaking>();

    public List<ScheduleTaking> getSchedulesTaking() {
        return schedulesTaking;
    }

    public void setSchedulesTaking(List<ScheduleTaking> schedulesTaking) {
        this.schedulesTaking = schedulesTaking;
    }

    @Override
    public void ParseAttributes() {

    }
}
