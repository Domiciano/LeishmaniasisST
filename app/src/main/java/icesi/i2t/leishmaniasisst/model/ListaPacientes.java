package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domiciano on 11/04/2016.
 */
@Root(name = "Patients", strict = false)
public class ListaPacientes extends DataXml{

    @ElementList(name="PatientXml", required = false, inline = true)
    List<Paciente> pacientes = new ArrayList<Paciente>();

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    @Override
    public void ParseAttributes() {

    }
}
