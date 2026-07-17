package com.edatasite.workforce.gwt.contact.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 30.06.11
 * Time: 9:43
 * To change this template use File | Settings | File Templates.
 */
public class GoogleGroupsSetting implements IsSerializable {
    private Integer id;
    private String googleGroupID;
    private Integer wftGroupID;
    private Boolean isOffice365Group;
    private Integer userID;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoogleGroupID() {
        return googleGroupID;
    }

    public void setGoogleGroupID(String googleGroupID) {
        this.googleGroupID = googleGroupID;
    }

    public Integer getWftGroupID() {
        return wftGroupID;
    }

    public void setWftGroupID(Integer wftGroupID) {
        this.wftGroupID = wftGroupID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Boolean getIsOffice365Group() {
        return isOffice365Group;
    }

    public void setIsOffice365Group(Boolean isOffice365Group) {
        this.isOffice365Group = isOffice365Group;
    }
}
