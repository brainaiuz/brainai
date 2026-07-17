package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 16.09.2009
 * Time: 13:32:09
 * To change this template use File | Settings | File Templates.
 */
public class ErrorPageListItem implements IsSerializable {
    public static final String USERNAME = "User Name";
    public static final String ACTION = "action";
    public static final String URL = "url";
    public static final String IP = "ip";
    public static final String DATE = "date";
    public static final String BROWSER = "browser";
    public static final String ERRORPAGENAME = "error Page Name";
    private Integer objectID;
    private String userName;
    private Date time;
    private String ipAddress;
    private String errorPageName;
    private String action;
    private String userAgent;
    private String companyName;
    private String urlName;

    public ErrorPageListItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getErrorPageName() {
        return errorPageName;
    }

    public void setErrorPageName(String errorPageName) {
        this.errorPageName = errorPageName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }
}
