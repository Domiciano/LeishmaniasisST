package icesi.i2t.leishmaniasisst.model;

import java.util.UUID;

public class Prescripcion extends DataXml {

    private String uuid;

    private String name;

    private String dosis;

    private String numeroLote;

    private String comentarios;

    private String dailySchemaId;

    BasicAdverseEvent eventosBasicos;

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
