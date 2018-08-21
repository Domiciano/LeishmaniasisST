package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.UUID;

/**
 * Created by Andres Aguirre on 09/02/2016.
 * Represents an BasicAdverseEvent. Has the required annotations to serialize the data and send it to the server.
 */

@Root(name = "BasicAdverseEventXml")
public class BasicAdverseEvent extends DataXml {

    private static final long serialVersionUID = 1L;
    //@Attribute(name = "xsi:type")
    private String classname = "BasicAdverseEventXml";

    @Element(name = "Id", required = false)
    private String uuid;


    @Element(name = "Name", required = false)
    private String name;

    @Element(name = "DolorSitio", required = false)
    private int dolorSitio;

    @Element(name = "InfeccionSitio", required = false)
    private int infeccionSitio;

    @Element(name = "MalestarGeneral", required = false)
    private int malestarGeneral;

    @Element(name = "DolorMuscular", required = false)
    private int dolorMuscular;

    @Element(name = "DolorArticulaciones", required = false)
    private int dolorArticulaciones;

    @Element(name = "DolorCabeza", required = false)
    private int dolorCabeza;

    @Element(name = "Fiebre", required = false)
    private int fiebre;

    @Element(name = "Nauseas", required = false)
    private int nauseas;

    @Element(name = "Vomito", required = false)
    private int vomito;

    @Element(name = "Diarrea", required = false)
    private int diarrea;

    @Element(name = "DolorAbdominal", required = false)
    private int dolorAbdominal;

    @Element(name = "PerdidaApetito", required = false)
    private int perdidaApetito;

    @Element(name = "Mareo", required = false)
    private int mareo;

    @Element(name = "Palpitaciones", required = false)
    private int palpitaciones;

    @Element(name = "PrescriptionId", required = false)
    private String prescriptionId;

    public BasicAdverseEvent() { this.uuid = UUID.randomUUID().toString();}

    public BasicAdverseEvent(String uuid, String name,
                             int dolorSitio, int infeccionSitio, int malestarGeneral,
                             int dolorMuscular, int dolorArticulaciones, int dolorCabeza,
                             int fiebre, int nauseas, int vomito, int diarrea, int dolorAbdominal,
                             int perdidaApetito, int mareo, int palpitaciones,
                             String prescriptionId) {
        this.uuid = uuid;
        this.name = name;
        this.dolorSitio = dolorSitio;
        this.infeccionSitio = infeccionSitio;
        this.malestarGeneral = malestarGeneral;
        this.dolorMuscular = dolorMuscular;
        this.dolorArticulaciones = dolorArticulaciones;
        this.dolorCabeza = dolorCabeza;
        this.fiebre = fiebre;
        this.nauseas = nauseas;
        this.vomito = vomito;
        this.diarrea = diarrea;
        this.dolorAbdominal = dolorAbdominal;
        this.perdidaApetito = perdidaApetito;
        this.mareo = mareo;
        this.palpitaciones = palpitaciones;
        this.prescriptionId = prescriptionId;

    }
    public BasicAdverseEvent(String name,
                             int dolorSitio, int infeccionSitio, int malestarGeneral,
                             int dolorMuscular, int dolorArticulaciones, int dolorCabeza,
                             int fiebre, int nauseas, int vomito, int diarrea, int dolorAbdominal,
                             int perdidaApetito, int mareo, int palpitaciones,
                             String prescriptionId) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.dolorSitio = dolorSitio;
        this.infeccionSitio = infeccionSitio;
        this.malestarGeneral = malestarGeneral;
        this.dolorMuscular = dolorMuscular;
        this.dolorArticulaciones = dolorArticulaciones;
        this.dolorCabeza = dolorCabeza;
        this.fiebre = fiebre;
        this.nauseas = nauseas;
        this.vomito = vomito;
        this.diarrea = diarrea;
        this.dolorAbdominal = dolorAbdominal;
        this.perdidaApetito = perdidaApetito;
        this.mareo = mareo;
        this.palpitaciones = palpitaciones;
        this.prescriptionId = prescriptionId;

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

    public int getDolorSitio() {
        return dolorSitio;
    }

    public void setDolorSitio(int dolorSitio) {
        this.dolorSitio = dolorSitio;
    }

    public int getInfeccionSitio() {
        return infeccionSitio;
    }

    public void setInfeccionSitio(int infeccionSitio) {
        this.infeccionSitio = infeccionSitio;
    }

    public int getMalestarGeneral() {
        return malestarGeneral;
    }

    public void setMalestarGeneral(int malestarGeneral) {
        this.malestarGeneral = malestarGeneral;
    }

    public int getDolorMuscular() {
        return dolorMuscular;
    }

    public void setDolorMuscular(int dolorMuscular) {
        this.dolorMuscular = dolorMuscular;
    }

    public int getDolorArticulaciones() {
        return dolorArticulaciones;
    }

    public void setDolorArticulaciones(int dolorArticulaciones) {
        this.dolorArticulaciones = dolorArticulaciones;
    }

    public int getDolorCabeza() {
        return dolorCabeza;
    }

    public void setDolorCabeza(int dolorCabeza) {
        this.dolorCabeza = dolorCabeza;
    }

    public int getFiebre() {
        return fiebre;
    }

    public void setFiebre(int fiebre) {
        this.fiebre = fiebre;
    }

    public int getNauseas() {
        return nauseas;
    }

    public void setNauseas(int nauseas) {
        this.nauseas = nauseas;
    }

    public int getVomito() {
        return vomito;
    }

    public void setVomito(int vomito) {
        this.vomito = vomito;
    }

    public int getDiarrea() {
        return diarrea;
    }

    public void setDiarrea(int diarrea) {
        this.diarrea = diarrea;
    }

    public int getDolorAbdominal() {
        return dolorAbdominal;
    }

    public void setDolorAbdominal(int dolorAbdominal) {
        this.dolorAbdominal = dolorAbdominal;
    }

    public int getPerdidaApetito() {
        return perdidaApetito;
    }

    public void setPerdidaApetito(int perdidaApetito) {
        this.perdidaApetito = perdidaApetito;
    }

    public int getMareo() {
        return mareo;
    }

    public void setMareo(int mareo) {
        this.mareo = mareo;
    }

    public int getPalpitaciones() {
        return palpitaciones;
    }

    public void setPalpitaciones(int palpitaciones) {
        this.palpitaciones = palpitaciones;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    @Override
    public void ParseAttributes() {

    }
}
