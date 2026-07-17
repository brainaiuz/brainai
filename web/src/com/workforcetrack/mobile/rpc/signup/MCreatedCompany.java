package com.workforcetrack.mobile.rpc.signup;

import com.edatasite.workforce.gwt.signup.client.rpc.CreatedCompany;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/12/11
 * Time: 10:48 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MCreatedCompany {
    private String adminUserName;
    private String adminPwd;
    private Integer companyID;
    private Integer adminID;
    private boolean hasAccount;


    public MCreatedCompany() {
    }

    public MCreatedCompany(CreatedCompany createdCompany) {
        if (createdCompany != null) {
            this.adminID = createdCompany.getAdminId();
            this.adminPwd = createdCompany.getAdminPwd();
            this.companyID = createdCompany.getCompanyId();
            this.adminUserName = createdCompany.getAdminUserName();
            this.hasAccount = createdCompany.isHasAccount();

        }
    }

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

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getAdminID() {
        return adminID;
    }

    public void setAdminID(Integer adminID) {
        this.adminID = adminID;
    }

    public boolean isHasAccount() {
        return hasAccount;
    }

    public void setHasAccount(boolean hasAccount) {
        this.hasAccount = hasAccount;
    }
}
