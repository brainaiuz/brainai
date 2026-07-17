package com.edatasite.workforce.gwt.core.client.rpc.rbac;

import com.edatasite.workforce.gwt.core.client.ui.Constants;

import java.io.Serializable;

/**
 * User: Abdulaziz
 * Date: May 19, 2010
 * Time: 12:21:10 PM
 */
public class GroupMemberItem implements Serializable {
    private String trusteeName;
    private String trusteeDescription;
    private Integer trusteeType;
    private Integer trusteeID;
    private String type;

    public String getTrusteeName() {
        return trusteeName;
    }

    public void setTrusteeName(String trusteeName) {
        this.trusteeName = trusteeName;
    }

    public String getTrusteeDescription() {
        return trusteeDescription;
    }

    public void setTrusteeDescription(String trusteeDescription) {
        this.trusteeDescription = trusteeDescription;
    }

    public Integer getTrusteeType() {
        return trusteeType;
    }

    public void setTrusteeType(Integer trusteeType) {
        this.trusteeType = trusteeType;
    }

    public Integer getTrusteeID() {
        return trusteeID;
    }

    public void setTrusteeID(Integer trusteeID) {
        this.trusteeID = trusteeID;
    }

    public String getType() {
        if (type == null) {
            type = Constants.IS_EMPLOYEE;
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
