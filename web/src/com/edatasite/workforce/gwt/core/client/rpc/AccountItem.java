package com.edatasite.workforce.gwt.core.client.rpc;

import java.io.Serializable;

public class AccountItem implements Serializable {
    private Integer companyId;
    private String companyName;
    private String login;
    private String role;
    private String password;
    private String userFullName;
    private String signedUpPage;
    private Boolean advancedPassEnabled;

    public String getSignedUpPage() {
        return signedUpPage;
    }

    public void setSignedUpPage(String signedUpPage) {
        this.signedUpPage = signedUpPage;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Boolean getAdvancedPassEnabled() {
        return advancedPassEnabled;
    }

    public void setAdvancedPassEnabled(Boolean advancedPassEnabled) {
        this.advancedPassEnabled = advancedPassEnabled;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
