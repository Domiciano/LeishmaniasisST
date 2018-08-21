package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Andres Aguirre on 09/02/2016.
 * Represents an UlcerForm. Has the required annotations to serialize the data and send it to the server.
 */

@Root(name = "UlcerFormXml")
public class UlcerForm extends DataXml {

    private static final long serialVersionUID = 1L;
    //@Attribute(name = "xsi:type")
    private String classname = "UlcerFormXml";


    @Element(name = "Id", required = false)
    private String uiid;


    @Element(name = "NoteGeneral", required = false)
    private String noteGeneral;

    @Element(name = "Date", required = false)
    private Date date;

    @Element(name = "DailySchemaId", required = false)
    private String dailySchemaId;

    @Element(name = "UlcerImages", required = false)
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
