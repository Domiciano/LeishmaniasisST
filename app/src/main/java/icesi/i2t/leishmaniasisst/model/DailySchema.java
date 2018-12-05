package icesi.i2t.leishmaniasisst.model;

import java.util.Date;
import java.util.UUID;



public class DailySchema extends DataXml {

    public String uuid;

   
    public String dayOfTreatment;

    
    public Date dateOfTreatment;

   
    public String schemaId;

    
    public boolean flag;

    
    public ListaPrescripciones prescripciones;

    
    public ListaUlcerForms imagenes;


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
