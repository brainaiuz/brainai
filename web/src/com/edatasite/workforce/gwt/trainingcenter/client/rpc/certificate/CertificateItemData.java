package com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CertificateItemData implements IsSerializable{

    private Integer objectID;
    private Integer sorder;
    private String values;
    private Integer color;

    public CertificateItemData() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}
