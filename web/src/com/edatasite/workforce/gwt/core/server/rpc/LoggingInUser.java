package com.edatasite.workforce.gwt.core.server.rpc;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 02.07.2009
 * Time: 15:00:27
 * To change this template use File | Settings | File Templates.
 */
public class LoggingInUser implements Serializable {
    private boolean isActivated;
    private boolean isCompanyActivated;
    private boolean isCompanySetup;
    private boolean isDeleted;
    private String login;
    private Integer companyID;
    private Integer userID;
    private boolean companyDataMissing;

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public boolean isCompanyActivated() {
        return isCompanyActivated;
    }

    public void setCompanyActivated(boolean companyActivated) {
        isCompanyActivated = companyActivated;
    }

    public boolean isCompanySetup() {
        return isCompanySetup;
    }

    public void setCompanySetup(boolean companySetup) {
        isCompanySetup = companySetup;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public boolean isCompanyDataMissing() {
        return companyDataMissing;
    }

    public void setCompanyDataMissing(boolean companyDataMissing) {
        this.companyDataMissing = companyDataMissing;
    }
}
