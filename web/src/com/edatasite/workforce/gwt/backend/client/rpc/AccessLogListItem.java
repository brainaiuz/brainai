package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 12.06.2009
 * Time: 22:55:39
 * To change this template use File | Settings | File Templates.
 */
public class AccessLogListItem implements IsSerializable {

    public static final String COMPANY_ID = "company id";
    public static final String COMPANY_NAME = "company name";
    public static final String USERS = "user";
    public static final String EMAIL = "email";
    public static final String DATE_ACCESSED = "date accessed";
    public static final String BROWSER_TYPE_VERSION = "browser type/version";
    public static final String ACCESSED_SECTION = "accessed section";
     public static final String IP = "ip";

    private Integer objectID;
    private String companyName;
    private String userName;
    private String email;
    private Date lastAccessDate;
    private String browserType;
    private String accessedSection;
    private UserSessionHistoryItem[] userSessionHistory;
    private String clientIpAddress;
    private Integer companyid;

    public AccessLogListItem() {
    }

    public AccessLogListItem(AccessLogListItem[] result, int totalCount) {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getAccessedSection() {
        return accessedSection;
    }

    public void setAccessedSection(String accessedSection) {
        this.accessedSection = accessedSection;
    }

    public UserSessionHistoryItem[] getUserSessionHistory() {
        return userSessionHistory;
    }

    public void setUserSessionHistory(UserSessionHistoryItem[] userSessionHistory) {
        this.userSessionHistory = userSessionHistory;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }
}
