package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.UUID;

/**
 * Created by Andres Aguirre on 09/02/2016.
 * Represents an Prescripcion. Has the required annotations to serialize the data and send it to the server.
 */

@Root(name = "PrescriptionFormXml")
public class Prescripcion extends DataXml {

    private static final long serialVersionUID = 1L;
    //@Attribute(name = "xsi:type")
    private String classname = "PrescripcionXml";

    @Element(name = "Id", required = false)
    private String uuid;

    @Element(name = "Name", required = false)
    private String name;

    @Element(name = "Dose", required = false)
    private String dosis;

    @Element(name = "Batch", required = false)
    private String numeroLote;

    @Element(name = "Comments", required = false)
    private String comentarios;

    @Element(name = "DailySchemaId", required = false)
    private String dailySchemaId;

    @Element(name = "BasicAdverseEvent", required = false)
    BasicAdverseEvent eventosBasicos;

    @Element(name = "ScheduleTaking", required = false)
    ListaScheduleTaking dosisTomadas;

    public Prescripcion() {this.uuid = UUID.randomUUID().toString();}
    public Prescripcion(String name, String dosis, String numeroLote, String comentarios, String dailySchemaId) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.dosis = dosis;
        this.numeroLote = numeroLote;
        this.comentarios = comentarios;
        this.dailySchemaId = dailySchemaId;

    }

    public Prescripcion(String uuid, String name, String dosis, String numeroLote, String comentarios, String dailySchemaId) {
        this.uuid = uuid;
        this.name = name;
        this.dosis = dosis;
        this.numeroLote = numeroLote;
        this.comentarios = comentarios;
        this.dailySchemaId = dailySchemaId;

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

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getDailySchemaId() {
        return dailySchemaId;
    }

    public void setDailySchemaId(String dailySchemaId) {
        this.dailySchemaId = dailySchemaId;
    }

    public ListaScheduleTaking getDosisTomadas() {
        return dosisTomadas;
    }

    public void setDosisTomadas(ListaScheduleTaking dosisTomadas) {
        this.dosisTomadas = dosisTomadas;
    }

    public BasicAdverseEvent getEventosBasicos() {
        return eventosBasicos;
    }

    public void setEventosBasicos(BasicAdverseEvent eventosBasicos) {
        this.eventosBasicos = eventosBasicos;
    }

    @Override
    public void ParseAttributes() {

    }
}
