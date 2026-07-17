package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 12, 2009
 * Time: 7:15:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class PensionProviderData implements IsSerializable {
    //Provider Details
    private Integer objectID;
    private String providerName;
    private String providerAccountRef;
    private String providerOtherRef;
    private String providerAddress;
    private String providerTownCity;
    private String providerCounty;
    private String providerPostCode;
    private String providerTelNo;
    private String providerFaxNo;
    private String providerEmail;
    private String providerCPName;
    private String providerCPMobile;
    private Date lastPayment;
    private Date nextPayment;
    //Provider Bank Details
    private String bankName;
    private String branchName;
    private String bankAddress;
    private String bankTownCity;
    private String bankCounty;
    private String bankPostCode;
    private String bankCPName;
    private String bankTelNo;
    private String bankFaxNo;
    private String bankEmail;
    private String sortCode;
    private String accountNo;
    private String nameShownOnAccount;
    private String bankAccountRef;
    private String bankOtherRefNo;
    private SelectItem providerCountry;
    private SelectItem bankCountry;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderAccountRef() {
        return providerAccountRef;
    }

    public void setProviderAccountRef(String providerAccountRef) {
        this.providerAccountRef = providerAccountRef;
    }

    public String getProviderOtherRef() {
        return providerOtherRef;
    }

    public void setProviderOtherRef(String providerOtherRef) {
        this.providerOtherRef = providerOtherRef;
    }

    public String getProviderAddress() {
        return providerAddress;
    }

    public void setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
    }

    public String getProviderTownCity() {
        return providerTownCity;
    }

    public void setProviderTownCity(String providerTownCity) {
        this.providerTownCity = providerTownCity;
    }

    public String getProviderCounty() {
        return providerCounty;
    }

    public void setProviderCounty(String providerCounty) {
        this.providerCounty = providerCounty;
    }

    public String getProviderPostCode() {
        return providerPostCode;
    }

    public void setProviderPostCode(String providerPostCode) {
        this.providerPostCode = providerPostCode;
    }

    public SelectItem getProviderCountry() {
        return providerCountry;
    }

    public void setProviderCountry(SelectItem providerCountry) {
        this.providerCountry = providerCountry;
    }

    public String getProviderTelNo() {
        return providerTelNo;
    }

    public void setProviderTelNo(String providerTelNo) {
        this.providerTelNo = providerTelNo;
    }

    public String getProviderFaxNo() {
        return providerFaxNo;
    }

    public void setProviderFaxNo(String providerFaxNo) {
        this.providerFaxNo = providerFaxNo;
    }

    public String getProviderEmail() {
        return providerEmail;
    }

    public void setProviderEmail(String providerEmail) {
        this.providerEmail = providerEmail;
    }

    public String getProviderCPName() {
        return providerCPName;
    }

    public void setProviderCPName(String providerCPName) {
        this.providerCPName = providerCPName;
    }

    public String getProviderCPMobile() {
        return providerCPMobile;
    }

    public void setProviderCPMobile(String providerCPMobile) {
        this.providerCPMobile = providerCPMobile;
    }

    public Date getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(Date lastPayment) {
        this.lastPayment = lastPayment;
    }

    public Date getNextPayment() {
        return nextPayment;
    }

    public void setNextPayment(Date nextPayment) {
        this.nextPayment = nextPayment;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankTownCity() {
        return bankTownCity;
    }

    public void setBankTownCity(String bankTownCity) {
        this.bankTownCity = bankTownCity;
    }

    public String getBankCounty() {
        return bankCounty;
    }

    public void setBankCounty(String bankCounty) {
        this.bankCounty = bankCounty;
    }

    public String getBankPostCode() {
        return bankPostCode;
    }

    public void setBankPostCode(String bankPostCode) {
        this.bankPostCode = bankPostCode;
    }

    public SelectItem getBankCountry() {
        return bankCountry;
    }

    public void setBankCountry(SelectItem bankCountry) {
        this.bankCountry = bankCountry;
    }

    public String getBankCPName() {
        return bankCPName;
    }

    public void setBankCPName(String bankCPName) {
        this.bankCPName = bankCPName;
    }

    public String getBankTelNo() {
        return bankTelNo;
    }

    public void setBankTelNo(String bankTelNo) {
        this.bankTelNo = bankTelNo;
    }

    public String getBankFaxNo() {
        return bankFaxNo;
    }

    public void setBankFaxNo(String bankFaxNo) {
        this.bankFaxNo = bankFaxNo;
    }

    public String getBankEmail() {
        return bankEmail;
    }

    public void setBankEmail(String bankEmail) {
        this.bankEmail = bankEmail;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getNameShownOnAccount() {
        return nameShownOnAccount;
    }

    public void setNameShownOnAccount(String nameShownOnAccount) {
        this.nameShownOnAccount = nameShownOnAccount;
    }

    public String getBankAccountRef() {
        return bankAccountRef;
    }

    public void setBankAccountRef(String bankAccountRef) {
        this.bankAccountRef = bankAccountRef;
    }

    public String getBankOtherRefNo() {
        return bankOtherRefNo;
    }

    public void setBankOtherRefNo(String bankOtherRefNo) {
        this.bankOtherRefNo = bankOtherRefNo;
    }
}
