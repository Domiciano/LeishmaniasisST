package icesi.i2t.leishmaniasisst.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domiciano on 12/04/2016.
 */


public class ListaPrescripciones extends DataXml{


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
