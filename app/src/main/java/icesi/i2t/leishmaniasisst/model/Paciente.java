package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by Andres Aguirre on 09/02/2016.
 * Represents an patient. Has the required annotations to serialize the data and send it to the server.
 */

@Root(name = "PatientXml")
public class Paciente extends DataXml {

    private static final long serialVersionUID = 1L;
    //@Attribute(name = "xsi:type")


    @Element(name = "Id", required = false)
    private String uuid;

    @Element(name = "Name", required = false)
    private String name;

    @Element(name = "Lastname", required = false)
    private String lastName;

    @Element(name = "Birthday", required = false)
    private Date birthday; // dd/MM/aaaa

    @Element(name = "Cedula", required = false)
    private String cedula;

    @Element(name = "DocumentType", required = false)
    private String documentType;

    @Element(name = "Gender", required = false)
    private String genre;

    @Element(name = "RaterId", required = false)
    private String evaluadorId;

    @Element(name = "Schemas", required = false)
    private ListaSchemas listaSchemas;

    public Paciente(){ this.uuid = UUID.randomUUID().toString();}

    public Paciente(String name, String lastName, Date birthday, String cedula, String documentType, String genre, String evaluadorId) {
        this.uuid = uuid;
        this.name = name;
        this.lastName = lastName;
        this.birthday = birthday;
        this.cedula = cedula;
        this.documentType = documentType;
        this.genre = genre;
        this.evaluadorId = evaluadorId;
    }

    public Paciente(String uuid, String name, String lastName, Date birthday, String cedula, String documentType, String genre, String evaluadorId, List<Schema> listaSchemas) {
        this.uuid = uuid;
        this.name = name;
        this.lastName = lastName;
        this.birthday = birthday;
        this.cedula = cedula;
        this.documentType = documentType;
        this.genre = genre;
        this.evaluadorId = evaluadorId;
    }

    public Paciente(String uuid, String name, String lastName, String cedula, Date birthday, String documentType, String genre, String evaluadorId) {
        this.uuid = uuid;
        this.name = name;
        this.lastName = lastName;
        this.cedula = cedula;
        this.birthday = birthday;
        this.documentType = documentType;
        this.genre = genre;
        this.evaluadorId = evaluadorId;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getEvaluadorId() {
        return evaluadorId;
    }

    public void setEvaluadorId(String evaluadorId) {
        this.evaluadorId = evaluadorId;
    }

    public ListaSchemas getListaSchemas() {
        return listaSchemas;
    }

    public void setListaSchemas(ListaSchemas listaSchemas) {
        this.listaSchemas = listaSchemas;
    }

    @Override
    public void ParseAttributes() {
    }
}
