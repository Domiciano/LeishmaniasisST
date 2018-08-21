package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;


/**
 * Created by Andres Aguirre on 09/02/2016.
 * Represents an patient. Has the required annotations to serialize the data and send it to the server.
 */

@Root(name = "data")
public class Pacientes extends DataXml {

    private static final long serialVersionUID = 1L;
    @Attribute(name = "xsi:type")
    private String classname = "PacienteXml";
    @Element(name = "PacienteId")
    private String uuid;
    @Element(name = "Name")
    private String name;
    @Element(name = "LastName")
    private String lastName;
    @Element(name = "Birthday")
    private Date birthday; // dd/MM/aaaa
    @Element(name = "Cedula")
    private String cedula;
    @Element(name = "DocumentType")
    private String documentType;
    @Element(name = "Gender")
    private String genre;
    @Element(name = "evaluadorId")
    private String evaluadorId;

    public Pacientes(String uuid, String name, String lastName, Date birthday, String cedula, String documentType, String genre, String evaluadorId) {
        this.uuid = uuid;
        this.name = name;
        this.lastName = lastName;
        this.birthday = birthday;
        this.cedula = cedula;
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

    @Override
    public void ParseAttributes() {
    }
}
