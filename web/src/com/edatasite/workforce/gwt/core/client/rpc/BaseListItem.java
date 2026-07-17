package com.edatasite.workforce.gwt.core.client.rpc;

/**
 * Created by IntelliJ IDEA.
 * User: mansur
 * Date: Jan 8, 2008
 * Time: 3:42:33 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseListItem extends Relational {

    private Integer objectID;
    private String encryptedID;

    public String getEncryptedID() {
        return encryptedID;
    }

    public void setEncryptedID(String encryptedID) {
        this.encryptedID = encryptedID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }
}
