package icesi.i2t.leishmaniasisst.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domiciano on 11/04/2016.
 */

public class ListaPacientes extends DataXml{

   
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
