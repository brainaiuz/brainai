package com.edatasite.workforce.gwt.core.server.rpc;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 1/26/11
 * Time: 11:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class AuthDetails implements Serializable {

    private Integer companyID;
    private Integer userID;
    private String userAgent;
    private String ipAddress;
    private String sessionID;
    private String serviceID;
    private String database;
    private Boolean superUser = false;
    private boolean openIDSignIn = false;

    public AuthDetails() {
    }

    public AuthDetails(Integer companyID, Integer userID, String database) {
        this.companyID = companyID;
        this.userID = userID;
        this.database = database;
    }

    public AuthDetails(Integer companyID, Integer userID, String database, String serviceID) {
        this.companyID = companyID;
        this.userID = userID;
        this.database = database;
        this.serviceID = serviceID;
    }

//    public AuthDetails(Integer companyID, Integer userID) {
//        this.companyID = companyID;
//        this.userID = userID;
//    }



    public AuthDetails(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public Boolean isSuperUser() {
        if (superUser != null)
            return superUser;
        return false;
    }

    public void setSuperUser(Boolean superUser) {
        this.superUser = superUser;
    }


    public void setOpenIDSignIn(boolean openIDSignIn) {
        this.openIDSignIn = openIDSignIn;
    }

    public boolean isOpenIDSignIn() {
        return openIDSignIn;
    }
}
