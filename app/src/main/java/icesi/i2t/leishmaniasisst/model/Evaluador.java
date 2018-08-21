package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Andres Aguirre on 09/02/2016.
 * Represents an Evaluador. Has the required annotations to serialize the data and send it to the server.
 */

@Root(name = "RaterXml")
public class Evaluador extends DataXml {

    private static final long serialVersionUID = 1L;
    //@Attribute(name = "RaterXml")
    private String classname = "RaterXml";
    @Element(name = "Id", required = false)
    private String uuid;

    @Element(name = "Name", required = false)
    private String name;

    @Element(name = "Lastname", required = false)
    private String lastName;

    @Element(name = "Cedula", required = false)
    private String cedula;
    //@Element(name = "LastLoginDate")
    private Date lastLogin; // dd/MM/aaaa

    @Element(name="Patients", required = false)
    private ListaPacientes pacienteLista;

    @Element(name="Schemas", required = false)
    private ListaSchemas listaSchemas;

    public ListaSchemas getListaSchemas() {
        return listaSchemas;
    }

    public void setListaSchemas(ListaSchemas listaSchemas) {
        this.listaSchemas = listaSchemas;
    }

    public Evaluador() { this.uuid = UUID.randomUUID().toString(); }

    public ListaPacientes getPacienteLista() {
        return pacienteLista;
    }

    public void setPacienteLista(ListaPacientes pacienteLista) {
        this.pacienteLista = pacienteLista;
    }

    public Evaluador(String uuid, String name, String last_name, String cedula, Date last_login) {
        this.uuid = uuid;
        this.name = name;
        this.lastName = last_name;
        this.cedula = cedula;
        this.lastLogin = last_login;


    }

    public Evaluador(String name, String last_name, String cedula, Date last_login) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        boolean hasName = this.name.equals("");
        boolean hasLastname = this.lastName.equals("");
        boolean hasCedula = this.cedula.equals("");
        boolean hasID = this.uuid.equals("");
        return hasName && hasLastname && hasCedula && hasID;
    }
}
