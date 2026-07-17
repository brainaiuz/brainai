package com.edatasite.workforce.gwt.core.client.ui.communication;

import com.edatasite.workforce.gwt.core.client.rpc.ContactTypeForTwilio;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeForTwilio;
import com.edatasite.workforce.gwt.core.client.rpc.OpportunityItemForTwilio;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.communication.widgets.CallModal;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

public class ContactDetailsItem implements IsSerializable {
    private String phoneNumber;
    private String name;
    private Integer ownerId;
    private String owner;
    private String mobile;
    private String primaryEmail;
    private CallModal.Command taskCommand;
    private CallModal.Command callCommand;
    private CallModal.Command eventCommand;
    private CallModal.Command smsCommand;
    private List<RelationItem> relations;
    private Integer contactType;
    private Integer id;
    private String status;
    private String company;
    private Integer companyId;
    private String accountIndustry;
    private String vacancy;
    private OpportunityItemForTwilio opportunity;
    private EmployeeForTwilio employee;
    private List<ContactTypeForTwilio> otherFields;
    private Integer accountId;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public CallModal.Command getTaskCommand() {
        return this.taskCommand;
    }

    public void setTaskCommand(final CallModal.Command taskCommand) {
        this.taskCommand = taskCommand;
    }

    public CallModal.Command getCallCommand() {
        return this.callCommand;
    }

    public void setCallCommand(final CallModal.Command callCommand) {
        this.callCommand = callCommand;
    }

    public CallModal.Command getEventCommand() {
        return this.eventCommand;
    }

    public void setEventCommand(final CallModal.Command eventCommand) {
        this.eventCommand = eventCommand;
    }

    public CallModal.Command getSmsCommand() {
        return this.smsCommand;
    }

    public void setSmsCommand(final CallModal.Command smsCommand) {
        this.smsCommand = smsCommand;
    }

    public List<RelationItem> getRelations() {
        return this.relations;
    }

    public void setRelations(final List<RelationItem> relations) {
        this.relations = relations;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ContactTypeForTwilio> getOtherFields() {
        return otherFields;
    }

    public void setOtherFields(List<ContactTypeForTwilio> otherFields) {
        this.otherFields = otherFields;
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

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
