package icesi.i2t.leishmaniasisst.model;

import java.util.Date;

public class Pacientes extends DataXml {

    private String uuid;

    private String name;

    private String lastName;

    private Date birthday; // dd/MM/aaaa

    private String cedula;

    private String documentType;

    private String genre;

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
