package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OpportunitySolrItem implements IsSerializable {

    private SelectItem opportunity;
    private Integer opportunityIntNumber;
    private String opportunityStringNumber;
    private SelectItem owner;
    private SelectItem assignee;
    private SelectItem backupAssignee;
    private Date closingDate;
    private Date creationDate;
    private Date modificationDate;
    private SelectItem creator;
    private SelectItem crmAccount;
    private SelectItem crmContact;
    private String crmContactPrimaryEmail;
    private Boolean crmContactEmailAllowed;
    private String crmContactPrimaryPhone;
    private SelectItem opportunityStage;
    private ReferenceLocale stageLocale;
    private Integer opportunityStageSorder;
    private Boolean opportunityConvertProject;
    private Boolean convertedFromLead;
    private Double amount;
    private Double amountBaseCurrency;
    private Double expectedRevenue;
    private SelectItem crmAccountCountry;
    private SelectItem campaign;
    private SelectItem currency;
    private SelectItem type;
    private SelectItem leadSource;
    private String nextStep;
    private Float probability;
    private Integer estimatorId;
    private Long opportunityKanbanOrder;
    private SelectItem relatedProject;
    private List<SelectItem> multiProject = new ArrayList<>();
    private Boolean hasAttachment;

    public SelectItem getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(SelectItem opportunity) {
        this.opportunity = opportunity;
    }

    public Integer getOpportunityIntNumber() {
        return opportunityIntNumber;
    }

    public void setOpportunityIntNumber(Integer opportunityIntNumber) {
        this.opportunityIntNumber = opportunityIntNumber;
    }

    public String getOpportunityStringNumber() {
        return opportunityStringNumber;
    }

    public void setOpportunityStringNumber(String opportunityStringNumber) {
        this.opportunityStringNumber = opportunityStringNumber;
    }

    public SelectItem getOwner() {
        return owner;
    }

    public void setOwner(SelectItem owner) {
        this.owner = owner;
    }

    public SelectItem getAssignee() {
        return assignee;
    }

    public void setAssignee(SelectItem assignee) {
        this.assignee = assignee;
    }

    public SelectItem getBackupAssignee() {
        return backupAssignee;
    }

    public void setBackupAssignee(SelectItem backupAssignee) {
        this.backupAssignee = backupAssignee;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(SelectItem crmAccount) {
        this.crmAccount = crmAccount;
    }

    public SelectItem getCrmContact() {
        return crmContact;
    }

    public void setCrmContact(SelectItem crmContact) {
        this.crmContact = crmContact;
    }

    public String getCrmContactPrimaryEmail() {
        return crmContactPrimaryEmail;
    }

    public void setCrmContactPrimaryEmail(String crmContactPrimaryEmail) {
        this.crmContactPrimaryEmail = crmContactPrimaryEmail;
    }

    public Boolean getCrmContactEmailAllowed() {
        return crmContactEmailAllowed;
    }

    public void setCrmContactEmailAllowed(Boolean crmContactEmailAllowed) {
        this.crmContactEmailAllowed = crmContactEmailAllowed;
    }

    public String getCrmContactPrimaryPhone() {
        return crmContactPrimaryPhone;
    }

    public void setCrmContactPrimaryPhone(String crmContactPrimaryPhone) {
        this.crmContactPrimaryPhone = crmContactPrimaryPhone;
    }

    public SelectItem getOpportunityStage() {
        return opportunityStage;
    }

    public void setOpportunityStage(SelectItem opportunityStage) {
        this.opportunityStage = opportunityStage;
    }

    public ReferenceLocale getStageLocale() {
        return stageLocale;
    }

    public void setStageLocale(ReferenceLocale stageLocale) {
        this.stageLocale = stageLocale;
    }

    public Integer getOpportunityStageSorder() {
        return opportunityStageSorder;
    }

    public void setOpportunityStageSorder(Integer opportunityStageSorder) {
        this.opportunityStageSorder = opportunityStageSorder;
    }

    public Boolean getOpportunityConvertProject() {
        return opportunityConvertProject;
    }

    public void setOpportunityConvertProject(Boolean opportunityConvertProject) {
        this.opportunityConvertProject = opportunityConvertProject;
    }

    public Boolean getConvertedFromLead() {
        return convertedFromLead;
    }

    public void setConvertedFromLead(Boolean convertedFromLead) {
        this.convertedFromLead = convertedFromLead;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmountBaseCurrency() {
        return amountBaseCurrency;
    }

    public void setAmountBaseCurrency(Double amountBaseCurrency) {
        this.amountBaseCurrency = amountBaseCurrency;
    }

    public Double getExpectedRevenue() {
        return expectedRevenue;
    }

    public void setExpectedRevenue(Double expectedRevenue) {
        this.expectedRevenue = expectedRevenue;
    }

    public SelectItem getCrmAccountCountry() {
        return crmAccountCountry;
    }

    public void setCrmAccountCountry(SelectItem crmAccountCountry) {
        this.crmAccountCountry = crmAccountCountry;
    }

    public SelectItem getCampaign() {
        return campaign;
    }

    public void setCampaign(SelectItem campaign) {
        this.campaign = campaign;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public SelectItem getType() {
        return type;
    }

    public void setType(SelectItem type) {
        this.type = type;
    }

    public SelectItem getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(SelectItem leadSource) {
        this.leadSource = leadSource;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public Float getProbability() {
        return probability;
    }

    public void setProbability(Float probability) {
        this.probability = probability;
    }

    public Integer getEstimatorId() {
        return estimatorId;
    }

    public void setEstimatorId(Integer estimatorId) {
        this.estimatorId = estimatorId;
    }

    public Long getOpportunityKanbanOrder() {
        return opportunityKanbanOrder;
    }

    public void setOpportunityKanbanOrder(Long opportunityKanbanOrder) {
        this.opportunityKanbanOrder = opportunityKanbanOrder;
    }

    public SelectItem getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(SelectItem relatedProject) {
        this.relatedProject = relatedProject;
    }

    public List<SelectItem> getMultiProject() {
        return multiProject;
    }

    public void setMultiProject(List<SelectItem> multiProject) {
        this.multiProject = multiProject;
    }

    public Boolean getHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(Boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }
}
