package com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class CertificateData implements IsSerializable{
    public static final String NUMBER = "number";
    public static final String STUDENT = "student";
    public static final String CERTIFICATE_TYPE = "certificatetype";
    public static final String CREATION_DATE = "creationdate";


    private Integer objectID;
//    private NumberData numberData;
    private Integer studentID;
    private String student;
    private CertificateTypeData certificateTypeData;
    private CertificateItemData[] items;
    private Date creationDate;

    private SelectItem[] certificateTypes;

    public CertificateData() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

//    public NumberData getNumberData() {
//        return numberData;
//    }
//
//    public void setNumberData(NumberData numberData) {
//        this.numberData = numberData;
//    }

    public Integer getStudentID() {
        return studentID;
    }

    public void setStudentID(Integer studentID) {
        this.studentID = studentID;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public CertificateTypeData getCertificateTypeData() {
        return certificateTypeData;
    }

    public void setCertificateTypeData(CertificateTypeData certificateTypeData) {
        this.certificateTypeData = certificateTypeData;
    }

    public CertificateItemData[] getItems() {
        return items;
    }

    public void setItems(CertificateItemData[] items) {
        this.items = items;
    }

    public SelectItem[] getCertificateTypes() {
        return certificateTypes;
    }

    public void setCertificateTypes(SelectItem[] certificateTypes) {
        this.certificateTypes = certificateTypes;
    }
}
