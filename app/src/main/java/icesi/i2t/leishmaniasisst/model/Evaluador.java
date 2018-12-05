package icesi.i2t.leishmaniasisst.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Andres Aguirre on 09/02/2016.
 * Represents an Evaluador. Has the required annotations to serialize the data and send it to the server.
 */

public class Evaluador extends DataXml {

    private String uuid;


    private String Name;

    private String lastName;

    private String cedula;

    private Date lastLogin; // dd/MM/aaaa

    private ListaPacientes pacientes;

    private ListaSchemas listaSchemas;

    public ListaSchemas getListaSchemas() {
        return listaSchemas;
    }

    public void setListaSchemas(ListaSchemas listaSchemas) {
        this.listaSchemas = listaSchemas;
    }

    public Evaluador() { this.uuid = UUID.randomUUID().toString(); }

    public ListaPacientes getPacientes() {
        return pacientes;
    }

    public void setPacientes(ListaPacientes pacientes) {
        this.pacientes = pacientes;
    }

    public Evaluador(String uuid, String name, String last_name, String cedula, Date last_login) {
        this.uuid = uuid;
        this.Name = name;
        this.lastName = last_name;
        this.cedula = cedula;
        this.lastLogin = last_login;


    }

    public Evaluador(String name, String last_name, String cedula, Date last_login) {
        this.uuid = UUID.randomUUID().toString();
        this.Name = name;
        this.lastName = last_name;
        this.cedula = cedula;
        this.lastLogin = last_login;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }








    @Override
    public void ParseAttributes() {

    }


    public boolean isValid(){
        boolean hasName = this.Name.equals("");
        boolean hasLastname = this.lastName.equals("");
        boolean hasCedula = this.cedula.equals("");
        boolean hasID = this.uuid.equals("");
        return hasName && hasLastname && hasCedula && hasID;
    }
}
