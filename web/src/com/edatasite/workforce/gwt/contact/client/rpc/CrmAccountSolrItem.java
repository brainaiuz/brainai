package com.edatasite.workforce.gwt.contact.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CrmAccountSolrItem implements IsSerializable {
    private Integer objectId;
    private Boolean doNonShow;
    private Boolean blocked = false;
    private SelectItem crmAccountParent;
    private SelectItem crmAccount;
    private List<SelectItem> type = new ArrayList<>();
    private List<SelectItem> owner = new ArrayList<>();
    private SelectItem ownership;
    private SelectItem industry;
    private String email;
    private String phone;
    private String fax;
    private String website;
    private SelectItem organizationType;
    private SelectItem numberOfEmployees;
    private SelectItem annualRevenue;
    private SelectItem rating;
    private Address billingAddress;
    private Address mailingAddress;
    private SelectItem currency;
    private SelectItem term;
    private String vatNumber;
    private String trnNumber;
    private String registrationNumber;
    private SelectItem paymentMethod;
    private SelectItem campaign;
    private Date creationDate;
    private Date lastUpdateDate;
    private Double clientBalance;
    private Double supplierBalance;
    private Double creditLimit;
    private String bankName;
    private SelectItem tax;
    private Boolean inTarget;
    private Date balanceDate;
    private Date supplierBalanceDate;
    private String saasuGuid;
    private Date saasuUpdatedDate;
    private String saasuUpdatedUid;
    private SelectItem contact;
    private String contactEmail;
    private SelectItem salesType;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Boolean getDoNonShow() {
        return doNonShow;
    }

    public void setDoNonShow(Boolean doNonShow) {
        this.doNonShow = doNonShow;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public SelectItem getCrmAccountParent() {
        return crmAccountParent;
    }

    public void setCrmAccountParent(SelectItem crmAccountParent) {
        this.crmAccountParent = crmAccountParent;
    }

    public SelectItem getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(SelectItem crmAccount) {
        this.crmAccount = crmAccount;
    }

    public List<SelectItem> getType() {
        return type;
    }

    public void setType(List<SelectItem> type) {
        this.type = type;
    }

    public List<SelectItem> getOwner() {
        return owner;
    }

    public void setOwner(List<SelectItem> owner) {
        this.owner = owner;
    }

    public SelectItem getOwnership() {
        return ownership;
    }

    public void setOwnership(SelectItem ownership) {
        this.ownership = ownership;
    }

    public SelectItem getIndustry() {
        return industry;
    }

    public void setIndustry(SelectItem industry) {
        this.industry = industry;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public SelectItem getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(SelectItem organizationType) {
        this.organizationType = organizationType;
    }

    public SelectItem getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(SelectItem numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public SelectItem getAnnualRevenue() {
        return annualRevenue;
    }

    public void setAnnualRevenue(SelectItem annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public SelectItem getRating() {
        return rating;
    }

    public void setRating(SelectItem rating) {
        this.rating = rating;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public SelectItem getTerm() {
        return term;
    }

    public void setTerm(SelectItem term) {
        this.term = term;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getTrnNumber() {
        return trnNumber;
    }

    public void setTrnNumber(String trnNumber) {
        this.trnNumber = trnNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public SelectItem getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(SelectItem paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public SelectItem getCampaign() {
        return campaign;
    }

    public void setCampaign(SelectItem campaign) {
        this.campaign = campaign;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Double getClientBalance() {
        return clientBalance;
    }

    public void setClientBalance(Double clientBalance) {
        this.clientBalance = clientBalance;
    }

    public Double getSupplierBalance() {
        return supplierBalance;
    }

    public void setSupplierBalance(Double supplierBalance) {
        this.supplierBalance = supplierBalance;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public SelectItem getTax() {
        return tax;
    }

    public void setTax(SelectItem tax) {
        this.tax = tax;
    }

    public Boolean getInTarget() {
        return inTarget;
    }

    public void setInTarget(Boolean inTarget) {
        this.inTarget = inTarget;
    }

    public Date getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    public Date getSupplierBalanceDate() {
        return supplierBalanceDate;
    }

    public void setSupplierBalanceDate(Date supplierBalanceDate) {
        this.supplierBalanceDate = supplierBalanceDate;
    }

    public String getSaasuGuid() {
        return saasuGuid;
    }

    public void setSaasuGuid(String saasuGuid) {
        this.saasuGuid = saasuGuid;
    }

    public Date getSaasuUpdatedDate() {
        return saasuUpdatedDate;
    }

    public void setSaasuUpdatedDate(Date saasuUpdatedDate) {
        this.saasuUpdatedDate = saasuUpdatedDate;
    }

    public String getSaasuUpdatedUid() {
        return saasuUpdatedUid;
    }

    public void setSaasuUpdatedUid(String saasuUpdatedUid) {
        this.saasuUpdatedUid = saasuUpdatedUid;
    }

    public SelectItem getContact() {
        return contact;
    }

    public void setContact(SelectItem contact) {
        this.contact = contact;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public SelectItem getSalesType() {
        return salesType;
    }

    public void setSalesType(SelectItem salesType) {
        this.salesType = salesType;
    }
}
