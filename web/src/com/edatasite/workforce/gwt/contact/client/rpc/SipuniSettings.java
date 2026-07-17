package com.edatasite.workforce.gwt.contact.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SipuniSettings implements IsSerializable {

    private Integer objectID;
    private String sipNumber;
    private String operatorNumber;
    private String secretKey;
    private SelectItem operator;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getSipNumber() {
        return sipNumber;
    }

    public void setSipNumber(String sipNumber) {
        this.sipNumber = sipNumber;
    }

    public String getOperatorNumber() {
        return operatorNumber;
    }

    public void setOperatorNumber(String operatorNumber) {
        this.operatorNumber = operatorNumber;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public SelectItem getOperator() {
        return operator;
    }

    public void setOperator(SelectItem operator) {
        this.operator = operator;
    }
}
