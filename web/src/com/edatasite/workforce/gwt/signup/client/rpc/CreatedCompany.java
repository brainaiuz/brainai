package com.edatasite.workforce.gwt.signup.client.rpc;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 26.07.2009
 * Time: 16:58:25
 * To change this template use File | Settings | File Templates.
 */
public class CreatedCompany implements Serializable {
    private String adminUserName;
    private String adminPwd;
    private Integer companyId;
    private Integer adminId;
    private Integer currencyId;
    private boolean hasAccount;
    private String selectedApps;
    private String database;

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getAdminPwd() {
        return adminPwd;
    }

    public void setAdminPwd(String adminPwd) {
        this.adminPwd = adminPwd;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public boolean isHasAccount() {
        return hasAccount;
    }

    public void setHasAccount(boolean hasAccount) {
        this.hasAccount = hasAccount;
    }

    public String getSelectedApps() {
        return selectedApps;
    }

    public void setSelectedApps(String selectedApps) {
        this.selectedApps = selectedApps;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
