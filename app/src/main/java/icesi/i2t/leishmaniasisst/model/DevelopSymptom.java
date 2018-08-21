package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Andres Aguirre on 09/02/2016.
 * Represents an DevelopSymptom. Has the required annotations to serialize the data and send it to the server.
 */

@Root(name = "DevelopSymptomXml")
public class DevelopSymptom extends DataXml {

    private static final long serialVersionUID = 1L;
    //@Attribute(name = "xsi:type")
    private String classname = "DevelopSymptomXml";

    @Element(name = "Id", required = false)
    private String uuid;

    @Element(name = "Name", required = false)
    private String name;

    //ventana@vacio
    @Element(name = "Answer", required = false)
    private String answer;

    @Element(name = "MedicineName", required = false)
    private String MedicineName;

    @Element(name = "isActive", required = false)
    private boolean isActive;

    @Element(name = "Date_Start", required = false)
    private Date date_start;

    @Element(name = "Date_End", required = false)
    private Date date_end;

    @Element(name = "SchemaId", required = false)
    private String schemaId;


    public DevelopSymptom(){ this.uuid = UUID.randomUUID().toString();}

    public DevelopSymptom(String uuid, String name, String answer, String medicineName, boolean isActive, Date date_start, Date date_end, String schemaId) {
        this.uuid = uuid;
        this.name = name;
        this.answer = answer;
        MedicineName = medicineName;
        this.isActive = isActive;
        this.date_start = date_start;
        this.date_end = date_end;
        this.schemaId = schemaId;
    }
    public DevelopSymptom(String name, String answer, String medicineName, boolean isActive, Date date_start, Date date_end, String schemaId) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.answer = answer;
        MedicineName = medicineName;
        this.isActive = isActive;
        this.date_start = date_start;
        this.date_end = date_end;
        this.schemaId = schemaId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public Date getDate_start() {
        return date_start;
    }

    public void setDate_start(Date date_start) {
        this.date_start = date_start;
    }

    public Date getDate_end() {
        return date_end;
    }

    public void setDate_end(Date date_end) {
        this.date_end = date_end;
    }

    @Override
    public void ParseAttributes() {

    }
}
