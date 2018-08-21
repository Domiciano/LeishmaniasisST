package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domiciano on 12/04/2016.
 */

@Root(name = "DevelopSymptoms", strict = false)
public class ListaSintomas extends DataXml{

    @ElementList(name = "DevelopSymptomXml", required = false, inline = true)
    List<DevelopSymptom> sintomas = new ArrayList<DevelopSymptom>();

    public List<DevelopSymptom> getSintomas() {
        return sintomas;
    }

    public void setSintomas(List<DevelopSymptom> sintomas) {
        this.sintomas = sintomas;
    }

    @Override
    public void ParseAttributes() {

    }
}
