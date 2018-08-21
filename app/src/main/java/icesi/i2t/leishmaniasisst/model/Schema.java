package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Andres Aguirre on 09/02/2016.
 * Represents an Schema. Has the required annotations to serialize the data and send it to the server.
 */

@Root(name = "SchemaXml")
public class Schema extends DataXml {

    private static final long serialVersionUID = 1L;
    //@Attribute(name = "xsi:type")
    private String classname = "SchemaXml";

    @Element(name = "Date_End", required = false)
    private Date dateEnd;

    @Element(name = "Date_Start", required = false)
    private Date dateStart;

    @Element(name = "Id", required = false)
    private String uuid;

    @Element(name = "isActive", required = false)
    private boolean active;

    @Element(name = "PatientId", required = false)
    private String patientId;

    @Element(name = "RaterId", required = false)
    private String evaluadorId;

    @Element(name="Antecedentes", required = false)
    private Antecedentes antecedentes;

    @Element(name = "DevelopSymptoms" , required = false)
    private ListaSintomas developSymptoms;

    @Element(name = "DailySchemas" , required = false)
    private ListaDailySchemas listaDailySchemas;

    public ListaDailySchemas getListaDailySchemas() {
        return listaDailySchemas;
    }

    public void setListaDailySchemas(ListaDailySchemas listaDailySchemas) {
        this.listaDailySchemas = listaDailySchemas;
    }

    public Antecedentes getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(Antecedentes antecedentes) {
        this.antecedentes = antecedentes;
    }

    public ListaSintomas getDevelopSymptoms() {
        return developSymptoms;
    }

    public void setDevelopSymptoms(ListaSintomas developSymptoms) {
        this.developSymptoms = developSymptoms;
    }

    public Schema() {this.uuid = UUID.randomUUID().toString();}

    public Schema(String uuid, Date dateStart, Date dateEnd, boolean active, String patientId, String evaluadorId) {
        this.uuid = uuid;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.active = active;
        this.patientId = patientId;
        this.evaluadorId = evaluadorId;

    }

    public Schema(Date dateStart, Date dateEnd, boolean active, String patientId, String evaluadorId) {
        this.uuid = UUID.randomUUID().toString();
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.active = active;
        this.patientId = patientId;
        this.evaluadorId = evaluadorId;

    }

    /*public Schema(String uuid, boolean active, String patientId) {
        this.uuid = uuid;
        this.active = active;
        this.patientId = patientId;
        listaDailySchemas = new ArrayList<DailySchema>();
    }*/

    public String getEvaluadorId() {
        return evaluadorId;
    }

    public void setEvaluadorId(String evaluadorId) {
        this.evaluadorId = evaluadorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public void ParseAttributes() {

    }
}
