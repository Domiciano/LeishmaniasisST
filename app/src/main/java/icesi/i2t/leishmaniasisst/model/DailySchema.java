package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Andres Aguirre on 09/02/2016.
 * Represents an DailySchema. Has the required annotations to serialize the data and send it to the server.
 */

@Root(name = "DailySchemaXml")
public class DailySchema extends DataXml {

    private static final long serialVersionUID = 1L;
    //@Attribute(name = "xsi:type")
    private String classname = "DailySchemaXml";

    @Element(name = "Id", required = false)
    private String uuid;

    @Element(name = "DayOfTreatment", required = false)
    private String dayOfTreatment;

    @Element(name = "DateTreatment", required = false)
    private Date dateOfTreatment;

    @Element(name = "SchemaId", required = false)
    private String schemaId;

    @Element(name = "Flag", required = false)
    private boolean flag;

    @Element(name="Prescriptions", required = false)
    private ListaPrescripciones prescripciones;

    @Element(name="UlcerForms", required = false)
    private ListaUlcerForms imagenes;


    public DailySchema(){ this.uuid = UUID.randomUUID().toString();}

    public ListaPrescripciones getPrescripciones() {
        return prescripciones;
    }

    public void setPrescripciones(ListaPrescripciones prescripciones) {
        this.prescripciones = prescripciones;
    }

    public ListaUlcerForms getImagenes() {
        return imagenes;
    }

    public void setImagenes(ListaUlcerForms imagenes) {
        this.imagenes = imagenes;
    }

    public DailySchema(String uuid, String dayOfTreatment, Date dateOfTreatment, boolean flag, String schemaId) {
        this.uuid = uuid;
        this.dayOfTreatment = dayOfTreatment;
        this.dateOfTreatment = dateOfTreatment;
        this.flag = flag;
        this.schemaId = schemaId;

    }
    public DailySchema(String dayOfTreatment, Date dateOfTreatment, boolean flag, String schemaId) {
        this.uuid = UUID.randomUUID().toString();
        this.dayOfTreatment = dayOfTreatment;
        this.dateOfTreatment = dateOfTreatment;
        this.flag = flag;
        this.schemaId = schemaId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getDayOfTreatment() {
        return dayOfTreatment;
    }

    public void setDayOfTreatment(String dayOfTreatment) {
        this.dayOfTreatment = dayOfTreatment;
    }

    public Date getDateOfTreatment() {
        return dateOfTreatment;
    }

    public void setDateOfTreatment(Date dateOfTreatment) {
        this.dateOfTreatment = dateOfTreatment;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    @Override
    public void ParseAttributes() {

    }
}
