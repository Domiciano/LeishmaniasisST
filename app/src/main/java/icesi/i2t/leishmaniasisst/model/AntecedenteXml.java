package icesi.i2t.leishmaniasisst.model;


import java.util.UUID;


public class AntecedenteXml extends DataXml {

    public String uuid;

    public String name;

    public String medicineName;

    public String schemaId;

    public AntecedenteXml(){ this.uuid = UUID.randomUUID().toString();}

    public AntecedenteXml(String uuid, String name, String medicineName, String schemaId) {

        this.uuid = uuid;
        this.name = name;
        this.medicineName = medicineName;
        this.schemaId = schemaId;
    }
    public AntecedenteXml(String name, String medicineName, String schemaId) {

        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.medicineName = medicineName;
        this.schemaId = schemaId;
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

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void ParseAttributes() {

    }
}
