package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domiciano on 12/04/2016.
 */

@Root(name = "Antecedentes", strict = false)
public class Antecedentes extends DataXml{

    @ElementList(name="AntecedenteXml", required = false, inline = true)
    List<AntecedenteXml> antecedentes = new ArrayList<AntecedenteXml>();

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
