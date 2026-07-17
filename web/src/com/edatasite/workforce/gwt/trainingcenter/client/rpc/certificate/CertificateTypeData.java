package com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate;

import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class CertificateTypeData implements IsSerializable{
    private Integer objectID;
    private NumberData numberData;
    private String name;
    private String imageURL;
    private Integer fieldsCount;
    private SelectItem[] students;

    public CertificateTypeData() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getFieldsCount() {
        return fieldsCount != null ? fieldsCount : 0;
    }

    public void setFieldsCount(Integer fieldsCount) {
        this.fieldsCount = fieldsCount;
    }

    public SelectItem[] getStudents() {
        return students;
    }

    public void setStudents(SelectItem[] students) {
        this.students = students;
    }
}
