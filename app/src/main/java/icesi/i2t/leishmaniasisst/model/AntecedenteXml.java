package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.UUID;

/**
 * Created by Andres Aguirre on 09/02/2016.
 * Represents an AntecedenteXml. Has the required annotations to serialize the data and send it to the server.
 */

@Root(name = "AntecedenteXml")
public class AntecedenteXml extends DataXml {

    private static final long serialVersionUID = 1L;
    //@Attribute(name = "xsi:type")

    public String classname = "AntecedentesXml";

    @Element(name = "Id", required = false)
    public String uuid;

    @Element(name = "Name", required = false)
    public String name;

    @Element(name = "MedicineName", required = false)
    public String medicineName;

    @Element(name = "SchemaId", required = false)
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
