package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: ${Dilsh0d}
 * Date: 19-Mar-2010
 * Time: 19:00:41
 */
public class UserSecuritryRpc implements IsSerializable {

    private String siteName;
    private String domainName;
    private Integer userId;
    private Integer companyId;
    private String jdbcUrl;
    private String dbDriverName;
    private String login;
    private String password;
    private Integer maxUserRole;
    private String userRoles;
    private String userFullName;
    private String companyName;
    private String AdminEmail;
    private String AdminFName;
    private String AdminLName;
    private String sessionID;
    private Integer clientId;
    private String timezone;

    public String getAdminEmail() {
        return AdminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        AdminEmail = adminEmail;
    }

    public String getAdminFName() {
        return AdminFName;
    }

    public void setAdminFName(String adminFName) {
        AdminFName = adminFName;
    }

    public String getAdminLName() {
        return AdminLName;
    }

    public void setAdminLName(String adminLName) {
        AdminLName = adminLName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMaxUserRole() {
        return maxUserRole;
    }

    public void setMaxUserRole(Integer maxUserRole) {
        this.maxUserRole = maxUserRole;
    }

    public String getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(String userRoles) {
        this.userRoles = userRoles;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getDbDriverName() {
        return dbDriverName;
    }

    public void setDbDriverName(String dbDriverName) {
        this.dbDriverName = dbDriverName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezone() {
        return timezone;
    }
}
