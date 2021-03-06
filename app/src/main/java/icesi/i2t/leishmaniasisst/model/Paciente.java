package icesi.i2t.leishmaniasisst.model;


import java.util.Date;
import java.util.List;
import java.util.UUID;


public class Paciente extends DataXml {

    private String uuid;

    private String Name;

    private String lastName;

    private Date birthday; // dd/MM/aaaa

    private String nationalId;

    private String documentType;

    private String genre;

    private String evaluadorId;

    private ListaSchemas listaSchemas;

    public Paciente(){ this.uuid = UUID.randomUUID().toString();}

    public Paciente(String name, String lastName, Date birthday, String cedula, String documentType, String genre, String evaluadorId) {
        this.uuid = uuid;
        this.Name = name;
        this.lastName = lastName;
        this.birthday = birthday;
        this.nationalId = cedula;
        this.documentType = documentType;
        this.genre = genre;
        this.evaluadorId = evaluadorId;
    }

    public Paciente(String uuid, String name, String lastName, Date birthday, String cedula, String documentType, String genre, String evaluadorId, List<Schema> listaSchemas) {
        this.uuid = uuid;
        this.Name = name;
        this.lastName = lastName;
        this.birthday = birthday;
        this.nationalId = cedula;
        this.documentType = documentType;
        this.genre = genre;
        this.evaluadorId = evaluadorId;
    }

    public Paciente(String uuid, String name, String lastName, String cedula, Date birthday, String documentType, String genre, String evaluadorId) {
        this.uuid = uuid;
        this.Name = name;
        this.lastName = lastName;
        this.nationalId = cedula;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCedula() {
        return nationalId;
    }

    public void setCedula(String cedula) {
        this.nationalId = cedula;
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
