package com.edatasite.workforce.gwt.client.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/25/11
 * Time: 1:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContactValidationData implements IsSerializable{
    private Integer objectID;
    private String email;

    public ContactValidationData() {
    }

    public ContactValidationData(Integer objectID, String email) {
        this.objectID = objectID;
        this.email = email;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
