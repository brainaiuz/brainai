package com.edatasite.workforce.gwt.contact.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

public class MyCallsSettings implements IsSerializable {

    private Integer objectID;

    private String userLogin;
    private String secretKey;
    private SelectItem operator;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
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
