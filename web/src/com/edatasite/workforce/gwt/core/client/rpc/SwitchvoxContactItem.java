package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by user on 1/12/2016.
 */
public class SwitchvoxContactItem implements IsSerializable{

    private Integer objectId;
    private Integer accountObjectId;
    private String name;
    private String workPhone;
    private boolean isLead;
    private String contactType;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getAccountObjectId() {
        return accountObjectId;
    }

    public void setAccountObjectId(Integer accountObjectId) {
        this.accountObjectId = accountObjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public boolean isLead() {
        return isLead;
    }

    public void setIsLead(boolean isLead) {
        this.isLead = isLead;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }
}
