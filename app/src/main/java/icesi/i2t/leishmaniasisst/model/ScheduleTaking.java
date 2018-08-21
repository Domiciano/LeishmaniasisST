package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Andres Aguirre on 09/02/2016.
 * Represents an ScheduleTaking. Has the required annotations to serialize the data and send it to the server.
 */

@Root(name = "ScheduleTakingXml")
public class ScheduleTaking extends DataXml {

    private static final long serialVersionUID = 1L;
    //@Attribute(name = "xsi:type")
    private String classname = "ScheduleTakingXml";

    @Element(name = "Id",required = false)
    private String uuid;

    @Element(name = "PrescriptionId",required = false)
    private String prescriptionId;

    @Element(name = "Date",required = false)
    private Date date;

    @Element(name = "Time",required = false)
    private String time;

    public ScheduleTaking(String uuid, String time, Date date, String prescriptionId) {
        this.uuid = uuid;
        this.time = time;
        this.date = date;
        this.prescriptionId = prescriptionId;
    }

    public ScheduleTaking(String time, Date date, String prescriptionId) {
        this.uuid = UUID.randomUUID().toString();
        this.time = time;
        this.date = date;
        this.prescriptionId = prescriptionId;
    }

    public ScheduleTaking() {this.uuid = UUID.randomUUID().toString();}

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
