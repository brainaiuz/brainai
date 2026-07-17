package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 1/21/11
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserCompanyDTO implements IsSerializable, Serializable {

    private Integer authId;
    private Integer userID;
    private String fullName;
    private String userName;
    private Integer companyID;
    private String sessionID;
    private String companyName;
    private String domain;
    private String subdomainCompany;
    private String companyDescription;
    private String logo;
    private Integer clusterID;
    private String clusterURL;
    private String clusterDbName;
    private String serviceID;
    private boolean isMaintance;
    private boolean active;
    private Date modificationDate;
    private Integer passwordExpirationDayCount;
    private String shortDateFormat;
    private String longDateFormat;
    private String uitype;
    private boolean current;
    private String status;
    private String statusName;

    public UserCompanyDTO() {
    }

    public UserCompanyDTO(Integer userID, Integer companyID) {
        this.userID = userID;
        this.companyID = companyID;
    }

    public UserCompanyDTO(Integer companyId, String name, boolean active) {
        this.companyID = companyId;
        this.companyName = name;
        this.active=active;
    }

    public UserCompanyDTO(Integer userID, String userName, Integer companyID, String companyName) {
        this.userID = userID;
        this.userName = userName;
        this.companyID = companyID;
        this.companyName = companyName;
    }

    public UserCompanyDTO(Integer clusterID, String clusterDbName, String serviceID, Integer userID, Integer companyID, String subdomainCompany, String userName, Integer authId, Date modificationDate, String uitype, Boolean ismaintenance) {
        this.clusterID = clusterID;
        this.clusterDbName = clusterDbName;
        this.serviceID = serviceID;
        this.userID = userID;
        this.companyID = companyID;
        this.subdomainCompany = subdomainCompany;
        this.userName = userName;
        this.authId = authId;
        this.modificationDate = modificationDate;
        this.uitype = uitype;
        this.isMaintance = ismaintenance;
    }


    public String getClusterDbName() {
        return clusterDbName;
    }

    public void setClusterDbName(String clusterDbName) {
        this.clusterDbName = clusterDbName;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName == null ? "DEMO_COMPANY" : companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSubdomainCompany() {
        return subdomainCompany;
    }

    public void setSubdomainCompany(String subdomainCompany) {
        this.subdomainCompany = subdomainCompany;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Integer getClusterID() {
        return clusterID;
    }

    public void setClusterID(Integer clusterID) {
        this.clusterID = clusterID;
    }

    public String getClusterURL() {
        return clusterURL;
    }

    public void setClusterURL(String clusterURL) {
        this.clusterURL = clusterURL;
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

    public boolean isMaintance() {
        return isMaintance;
    }

    public void setMaintance(boolean maintance) {
        isMaintance = maintance;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }

    public Integer getPasswordExpirationDayCount() {
        return passwordExpirationDayCount;
    }

    public void setPasswordExpirationDayCount(Integer passwordExpirationDayCount) {
        this.passwordExpirationDayCount = passwordExpirationDayCount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortDateFormat() {
        return shortDateFormat;
    }

    public void setShortDateFormat(String shortDateFormat) {
        this.shortDateFormat = shortDateFormat;
    }

    public String getLongDateFormat() {
        return longDateFormat;
    }

    public void setLongDateFormat(String longDateFormat) {
        this.longDateFormat = longDateFormat;
    }

    public String getUitype() {
        return uitype;
    }

    public void setUitype(String uitype) {
        this.uitype = uitype;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}