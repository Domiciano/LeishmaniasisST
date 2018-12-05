package icesi.i2t.leishmaniasisst.model;

import java.util.Date;
import java.util.UUID;


public class UlcerForm extends DataXml {

    public String classname = "UlcerForm";

    private String uiid;

    private String noteGeneral;

    private Date date;

    private String dailySchemaId;

    ListaUlcerImages ulcerImages;

    public ListaUlcerImages getUlcerImages() {
        return ulcerImages;
    }

    public void setUlcerImages(ListaUlcerImages ulcerImages) {
        this.ulcerImages = ulcerImages;
    }

    public UlcerForm() {
    }

    public UlcerForm(String noteGeneral, Date date, String dailySchemaId) {
        this.uiid = UUID.randomUUID().toString();
        this.noteGeneral = noteGeneral;
        this.date = date;
        this.dailySchemaId = dailySchemaId;

    }

    public UlcerForm(String uiid, String noteGeneral, Date date, String dailySchemaId) {
        this.uiid = uiid;
        this.noteGeneral = noteGeneral;
        this.date = date;
        this.dailySchemaId = dailySchemaId;

    }

    public String getUiid() {
        return uiid;
    }

    public void setUiid(String uiid) {
        this.uiid = uiid;
    }

    public String getNoteGeneral() {
        return noteGeneral;
    }

    public void setNoteGeneral(String noteGeneral) {
        this.noteGeneral = noteGeneral;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDailySchemaId() {
        return dailySchemaId;
    }

    public void setDailySchemaId(String dailySchemaId) {
        this.dailySchemaId = dailySchemaId;
    }

    @Override
    public void ParseAttributes() {

    }
}
