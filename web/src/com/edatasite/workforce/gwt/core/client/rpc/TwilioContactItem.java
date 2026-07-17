package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class TwilioContactItem implements IsSerializable {
    private Integer objectID;
    private String name;
    private String primaryPhone;
    private Integer contactType;
    private ArrayList<String> mobile;
    private String owner;
    private Integer ownerId;
    private String email;
    private String status;
    private String company;
    private Integer companyId;
    private String accountIndustry;
    private String vacancy;
    private OpportunityItemForTwilio opportunity;
    private EmployeeForTwilio employee;
    private ArrayList<ContactTypeForTwilio> otherTypes;

    public TwilioContactItem() {
    }

    public TwilioContactItem(Integer objectID, String name, String primaryPhone, Integer contactType, ArrayList<String> mobile) {
        this.objectID = objectID;
        this.name = name;
        this.primaryPhone = primaryPhone;
        this.contactType = contactType;
        this.mobile = mobile;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public ArrayList<String> getMobile() {
        return mobile;
    }

    public void setMobile(ArrayList<String> mobile) {
        this.mobile = mobile;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<ContactTypeForTwilio> getOtherTypes() {
        return otherTypes;
    }

    public void setOtherTypes(ArrayList<ContactTypeForTwilio> otherTypes) {
        this.otherTypes = otherTypes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public OpportunityItemForTwilio getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(OpportunityItemForTwilio opportunity) {
        this.opportunity = opportunity;
    }

    public String getAccountIndustry() {
        return accountIndustry;
    }

    public void setAccountIndustry(String accountIndustry) {
        this.accountIndustry = accountIndustry;
    }

    public String getVacancy() {
        return vacancy;
    }

    public void setVacancy(String vacancy) {
        this.vacancy = vacancy;
    }

    public EmployeeForTwilio getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeForTwilio employee) {
        this.employee = employee;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
