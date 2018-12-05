package icesi.i2t.leishmaniasisst.model;

import java.util.ArrayList;
import java.util.List;

public class ListaSintomas extends DataXml{

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
