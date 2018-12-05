package icesi.i2t.leishmaniasisst.model;

import java.util.Date;
import java.util.UUID;

public class DevelopSymptom extends DataXml {

    public String classname = "DevelopSymptom";

    private String uuid;

    private String name;

    private String answer;

    private String medicineName;

    private boolean isActive;

    private Date date_start;

    private Date date_end;

    private String schemaId;

    public DevelopSymptom(){ this.uuid = UUID.randomUUID().toString();}

    public DevelopSymptom(String uuid, String name, String answer, String medicineName, boolean isActive, Date date_start, Date date_end, String schemaId) {
        this.uuid = uuid;
        this.name = name;
        this.answer = answer;
        this.medicineName = medicineName;
        this.isActive = isActive;
        this.date_start = date_start;
        this.date_end = date_end;
        this.schemaId = schemaId;
    }
    public DevelopSymptom(String name, String answer, String medicineName, boolean isActive, Date date_start, Date date_end, String schemaId) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.answer = answer;
        this.medicineName = medicineName;
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
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
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
