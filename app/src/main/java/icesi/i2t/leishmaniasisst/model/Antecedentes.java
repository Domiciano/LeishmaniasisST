package icesi.i2t.leishmaniasisst.model;

import java.util.ArrayList;
import java.util.List;


public class Antecedentes extends DataXml{

    public Antecedentes(){}

    public List<AntecedenteXml> antecedentes = new ArrayList<AntecedenteXml>();

    public List<AntecedenteXml> getAntecedentes() {
            return antecedentes;
        }

    public void setAntecedentes(List<AntecedenteXml> antecedentes) {
        this.antecedentes = antecedentes;
    }

    @Override
    public void ParseAttributes() {

    }
}
