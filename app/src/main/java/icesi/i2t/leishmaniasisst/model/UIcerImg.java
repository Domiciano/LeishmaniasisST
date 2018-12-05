package icesi.i2t.leishmaniasisst.model;


import java.util.Date;
import java.util.UUID;


public class UIcerImg extends DataXml {

    private  String classname = "UlcerImg";

    private String uuid;

    private String bodyLocation;

    private Date imgDate;

    private String imgFormat;

    private String imgUUID;

    private String UIcerFormId;

    private String injuriesPerLocation;

    public UIcerImg() {this.uuid = UUID.randomUUID().toString();}

    public UIcerImg(String bodyLocation, Date imgDate, String imgFormat, String imgUUID, String injuriesPerLocation, String UIcerFormId) {
        this.uuid = UUID.randomUUID().toString();
        this.bodyLocation = bodyLocation;
        this.imgDate = imgDate;
        this.imgFormat = imgFormat;
        this.imgUUID = imgUUID;
        this.UIcerFormId = UIcerFormId;
        this.injuriesPerLocation = injuriesPerLocation;
    }

    public UIcerImg(String uuid, String bodyLocation, Date imgDate, String imgFormat, String imgUUID, String injuriesPerLocation, String UIcerFormId) {
        this.uuid = uuid;
        this.bodyLocation = bodyLocation;
        this.imgDate = imgDate;
        this.imgFormat = imgFormat;
        this.imgUUID = imgUUID;
        this.UIcerFormId = UIcerFormId;
        this.injuriesPerLocation = injuriesPerLocation;
    }

    public String getInjuriesPerLocation() {
        return injuriesPerLocation;
    }

    public void setInjuriesPerLocation(String injuriesPerLocation) {
        this.injuriesPerLocation = injuriesPerLocation;
    }

    @Override
    public void ParseAttributes() {

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBodyLocation() {
        return bodyLocation;
    }

    public void setBodyLocation(String bodyLocation) {
        this.bodyLocation = bodyLocation;
    }



    public Date getImgDate() {
        return imgDate;
    }

    public void setImgDate(Date imgDate) {
        this.imgDate = imgDate;
    }

    public String getImgFormat() {
        return imgFormat;
    }

    public void setImgFormat(String imgFormat) {
        this.imgFormat = imgFormat;
    }

    public String getImgUUID() {
        return imgUUID;
    }

    public void setImgUUID(String imgUUID) {
        this.imgUUID = imgUUID;
    }

    public String getUIcerFormId() {
        return UIcerFormId;
    }

    public void setUIcerFormId(String UIcerFormId) {
        this.UIcerFormId = UIcerFormId;
    }
}
