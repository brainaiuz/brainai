package com.edatasite.workforce.gwt.crmcase.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class CaseSolrItem implements IsSerializable {

    private Integer objectID;
    private Integer caseTrackerId;
    private String caseEmailId;
    private String caseEmail;
    private String casePhone;
    private String caseSubject;
    private String caseNumber;
    private SelectItem caseAssignee;
    private SelectItem caseDepartment;
    private SelectItem caseOrigin;
    private SelectItem caseType;
    private SelectItem caseReason;
    private SelectItem priority;
    private ReferenceItem status;
    private SelectItem resolver;
    private Integer opportunityId;
    private Integer accountId;
    private Integer leadId;
    private Integer relatedToId;
    private Integer entityId;
    private Boolean inTrash;
    private Boolean billable;
    private Boolean hasAttachment;
    private String reportedBy;
    private Date createDate;
    private Date lastUpdatedDate;
    private Date lastReportedDate;
    private Date internalUpdatedDate;
    private ReferenceItem internalStatus;
    private Long kanbanOrder;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCaseTrackerId() {
        return caseTrackerId;
    }

    public void setCaseTrackerId(Integer caseTrackerId) {
        this.caseTrackerId = caseTrackerId;
    }

    public String getCaseEmailId() {
        return caseEmailId;
    }

    public void setCaseEmailId(String caseEmailId) {
        this.caseEmailId = caseEmailId;
    }

    public String getCaseEmail() {
        return caseEmail;
    }

    public void setCaseEmail(String caseEmail) {
        this.caseEmail = caseEmail;
    }

    public String getCasePhone() {
        return casePhone;
    }

    public void setCasePhone(String casePhone) {
        this.casePhone = casePhone;
    }

    public String getCaseSubject() {
        return caseSubject;
    }

    public void setCaseSubject(String caseSubject) {
        this.caseSubject = caseSubject;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public SelectItem getCaseAssignee() {
        return caseAssignee;
    }

    public void setCaseAssignee(SelectItem caseAssignee) {
        this.caseAssignee = caseAssignee;
    }

    public SelectItem getCaseDepartment() {
        return caseDepartment;
    }

    public void setCaseDepartment(SelectItem caseDepartment) {
        this.caseDepartment = caseDepartment;
    }

    public SelectItem getCaseOrigin() {
        return caseOrigin;
    }

    public void setCaseOrigin(SelectItem caseOrigin) {
        this.caseOrigin = caseOrigin;
    }

    public SelectItem getCaseType() {
        return caseType;
    }

    public void setCaseType(SelectItem caseType) {
        this.caseType = caseType;
    }

    public SelectItem getCaseReason() {
        return caseReason;
    }

    public void setCaseReason(SelectItem caseReason) {
        this.caseReason = caseReason;
    }

    public SelectItem getPriority() {
        return priority;
    }

    public void setPriority(SelectItem priority) {
        this.priority = priority;
    }

    public ReferenceItem getStatus() {
        return status;
    }

    public void setStatus(ReferenceItem status) {
        this.status = status;
    }

    public SelectItem getResolver() {
        return resolver;
    }

    public void setResolver(SelectItem resolver) {
        this.resolver = resolver;
    }

    public Integer getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(Integer opportunityId) {
        this.opportunityId = opportunityId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public Integer getRelatedToId() {
        return relatedToId;
    }

    public void setRelatedToId(Integer relatedToId) {
        this.relatedToId = relatedToId;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Boolean getInTrash() {
        return inTrash;
    }

    public void setInTrash(Boolean inTrash) {
        this.inTrash = inTrash;
    }

    public Boolean getBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public Boolean getHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(Boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Date getLastReportedDate() {
        return lastReportedDate;
    }

    public void setLastReportedDate(Date lastReportedDate) {
        this.lastReportedDate = lastReportedDate;
    }

    public Date getInternalUpdatedDate() {
        return internalUpdatedDate;
    }

    public void setInternalUpdatedDate(Date internalUpdatedDate) {
        this.internalUpdatedDate = internalUpdatedDate;
    }

    public ReferenceItem getInternalStatus() {
        return internalStatus;
    }

    public void setInternalStatus(ReferenceItem internalStatus) {
        this.internalStatus = internalStatus;
    }

    public Long getKanbanOrder() {
        return kanbanOrder;
    }

    public void setKanbanOrder(Long kanbanOrder) {
        this.kanbanOrder = kanbanOrder;
    }
}
