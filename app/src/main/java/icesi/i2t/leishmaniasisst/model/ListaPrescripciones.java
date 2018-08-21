package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domiciano on 12/04/2016.
 */

@Root(name = "Prescriptions", strict = false)
public class ListaPrescripciones extends DataXml{

    @ElementList(name="PrescriptionFormXml", required = false, inline = true)
    List<Prescripcion> prescripciones = new ArrayList<Prescripcion>();

    public List<Prescripcion> getPrescripciones() {
        return prescripciones;
    }

    public void setPrescripciones(List<Prescripcion> prescripciones) {
        this.prescripciones = prescripciones;
    }

    @Override
    public void ParseAttributes() {

    }
}
