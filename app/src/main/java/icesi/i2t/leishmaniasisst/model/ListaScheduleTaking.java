package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domiciano on 12/04/2016.
 */

@Root(name = "ScheduleTaking", strict = false)
public class ListaScheduleTaking extends DataXml{

    @ElementList(name="ScheduleTakingXml", required = false, inline = true)
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
