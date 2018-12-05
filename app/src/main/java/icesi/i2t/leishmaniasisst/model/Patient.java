package icesi.i2t.leishmaniasisst.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


public class Patient extends DataXml {

    private String UUIDNumber;
    
    private String identification;
    
    private String name;
    
    private String lastName;
    
    private String documentType;
    
    private char genre;
    
    private String address;
    
    private String phone;
    
    private String birthday; //dd/MM/yyyy
    
    private String province;
    
    private String municipality;
   
    private String lane;
    
    private String contactName;
    
    private String contactLastName;
   
    private String contactPhone;
   
    private String contactAddress;
    
    private int injuryWeeks;

    private List<Evaluation> evaluationList;

    public Patient(String identification, String name, String lastName, String documentType, char genre, String address, String phone, String birthday, String province, String municipality, String lane, String contactName, String contactLastName, String contactPhone, String contactAddress, int injuryWeeks) {
        this.UUIDNumber = UUID.randomUUID().toString();
        this.identification = identification;
        this.name = name;
        this.lastName = lastName;
        this.documentType = documentType;
        this.genre = genre;
        this.address = address;
        this.phone = phone;
        this.birthday = birthday;
        this.province = province;
        this.municipality = municipality;
        this.lane = lane;
        this.contactName = contactName;
        this.contactLastName = contactLastName;
        this.contactPhone = contactPhone;
        this.contactAddress = contactAddress;
        this.injuryWeeks = injuryWeeks;
        this.evaluationList = new ArrayList<Evaluation>();
    }

    public Patient(String uuid, String identification, String name, String lastName, String documentType, char genre, String address, String phone, String birthday, String province, String municipality, String lane, String contactName, String contactLastName, String contactPhone, String contactAddress, int injuryWeeks) {
        this.UUIDNumber = uuid;
        this.identification = identification;
        this.name = name;
        this.lastName = lastName;
        this.documentType = documentType;
        this.genre = genre;
        this.address = address;
        this.phone = phone;
        this.birthday = birthday;
        this.province = province;
        this.municipality = municipality;
        this.lane = lane;
        this.contactName = contactName;
        this.contactLastName = contactLastName;
        this.contactPhone = contactPhone;
        this.contactAddress = contactAddress;
        this.injuryWeeks = injuryWeeks;
        this.evaluationList = new ArrayList<Evaluation>();
    }

    public Patient(String uuid, String identification, String name, String lastName) {
        this.UUIDNumber = uuid;
        this.identification = identification;
        this.name = name;
        this.lastName = lastName;
        this.evaluationList = new ArrayList<Evaluation>();
    }

    @Override
    public void ParseAttributes() {
    }

    public String getUUIDNumber() {
        return UUIDNumber;
    }

    public void setUUIDNumber(String UUIDNumber) {
        this.UUIDNumber = UUIDNumber;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public char getGenre() {
        return genre;
    }

    public void setGenre(char genre) {
        this.genre = genre;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public int getInjuryWeeks() {
        return injuryWeeks;
    }

    public void setInjuryWeeks(int injuryWeeks) {
        this.injuryWeeks = injuryWeeks;
    }

    public List<Evaluation> getEvaluationList() {
        return evaluationList;
    }

    /* Adds an evaluation to the evaluations list */
    public boolean addEvaluation(Evaluation evaluation) {
        return evaluationList.add(evaluation);
    }

    /* Returns the evaluation in the i position of the list */
    public Evaluation getEvaluation(int i) {
        return evaluationList.get(i);
    }

    /* Adds a collection of evaluations to the evaluations list */
    public boolean addAllEvaluations(Collection<? extends Evaluation> evaluations) {
        return evaluationList.addAll(evaluations);
    }
}
