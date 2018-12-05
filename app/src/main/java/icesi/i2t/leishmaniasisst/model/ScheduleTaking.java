package icesi.i2t.leishmaniasisst.model;

import java.util.Date;
import java.util.UUID;

public class ScheduleTaking extends DataXml {
	
    private String uuid;

    private String prescriptionId;

    private Date date;

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
