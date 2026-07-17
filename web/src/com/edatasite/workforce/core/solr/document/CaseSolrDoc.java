package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:24.
 */
@SolrDocument(collection = "caseCore")
public class CaseSolrDoc extends RelationBaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("caseId")
    @Indexed(name = "caseId", type = "pint", required = true)
    private Integer caseId;

    @Field("caseTrackerId")
    private Integer caseTrackerId;

    @Field("caseEmailId")
    private String caseEmailId;

    @Field("caseEmail")
    private String caseEmail;

    @Field("casePhone")
    private String casePhone;

    @Field("caseSubject")
    private String caseSubject;

    @Field("caseNumber")
    private String caseNumber;

    @Field("caseAssigneeId")
    private Integer caseAssigneeId;

    @Field("caseAssigneeIdName")
    private String caseAssigneeIdName;

    @Field("caseAssignee")
    private String caseAssignee;

    @Field("caseDepartmentId")
    private Integer caseDepartmentId;

    @Field("caseDepartmentIdName")
    private String caseDepartmentIdName;

    @Field("caseDepartment")
    private String caseDepartment;

    @Field("caseOriginId")
    private Integer caseOriginId;

    @Field("caseOriginIdName")
    private String caseOriginIdName;

    @Field("caseOriginName")
    private String caseOriginName;

    @Field("caseOriginCode")
    private String caseOriginCode;

    @Field("caseOriginIdCodeName")
    private String caseOriginIdCodeName;

    @Field("caseTypeId")
    private Integer caseTypeId;

    @Field("caseTypeIdName")
    private String caseTypeIdName;

    @Field("caseTypeName")
    private String caseTypeName;

    @Field("caseTypeCode")
    private String caseTypeCode;

    @Field("caseTypeIdCodeName")
    private String caseTypeIdCodeName;

    @Field("caseReasonId")
    private Integer caseReasonId;

    @Field("caseReasonIdName")
    private String caseReasonIdName;

    @Field("caseReasonName")
    private String caseReasonName;

    @Field("priorityId")
    private Integer priorityId;

    @Field("priorityIdName")
    private String priorityIdName;

    @Field("priorityName")
    private String priorityName;

    @Field("priorityIdCodeName")
    private String priorityIdCodeName;

    @Field("priorityCode")
    private String priorityCode;

    @Field("priorityColor")
    private String priorityColor;

    @Field("prioritySorder")
    private Integer prioritySorder;

    @Field("statusId")
    private Integer statusId;

    @Field("statusIdName")
    private String statusIdName;

    @Field("statusName")
    private String statusName;

    @Field("statusIdCodeName")
    private String statusIdCodeName;

    @Field("statusCode")
    private String statusCode;

    @Field("statusSorder")
    private Integer statusSorder;

    @Field("resolverId")
    private Integer resolverId;

    @Field("resolverIdName")
    private String resolverIdName;

    @Field("resolverName")
    private String resolverName;

    @Field("opportunityId")
    private Integer opportunityId;

    @Field("accountId")
    private Integer accountId;

    @Field("leadId")
    private Integer leadId;

    @Field("relatedToId")
    private Integer relatedToId;

    @Field("entityId")
    private Integer entityId;

    @Field("inTrash")
    private Boolean inTrash;

    @Field("billable")
    private Boolean billable;

    @Field("hasAttachment")
    private Boolean hasAttachment;

    @Field("reportedBy")
    private String reportedBy;

    @Field("createDate")
    private Date createDate;

    @Field("lastUpdatedDate")
    private Date lastUpdatedDate;

    @Field("lastReportedDate")
    private Date lastReportedDate;

    @Field("internalUpdatedDate")
    private Date internalUpdatedDate;

    @Field("internalStatusId")
    private Integer internalStatusId;

    @Field("internalStatusIdName")
    private String internalStatusIdName;

    @Field("internalStatusName")
    private String internalStatusName;

    @Field("internalStatusSorder")
    private Integer internalStatusSorder;

    @Field("kanbanOrder")
    private Long kanbanOrder;

    @Field("dynStringComposite")
    @Indexed(name = "dynStringComposite", type = "strings", stored = false)
    private List<String> dynStringComposite = new ArrayList<>();

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
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

    public Integer getCaseAssigneeId() {
        return caseAssigneeId;
    }

    public void setCaseAssigneeId(Integer caseAssigneeId) {
        this.caseAssigneeId = caseAssigneeId;
    }

    public String getCaseAssigneeIdName() {
        return caseAssigneeIdName;
    }

    public void setCaseAssigneeIdName(String caseAssigneeIdName) {
        this.caseAssigneeIdName = caseAssigneeIdName;
    }

    public String getCaseAssignee() {
        return caseAssignee;
    }

    public void setCaseAssignee(String caseAssignee) {
        this.caseAssignee = caseAssignee;
    }

    public Integer getCaseDepartmentId() {
        return caseDepartmentId;
    }

    public void setCaseDepartmentId(Integer caseDepartmentId) {
        this.caseDepartmentId = caseDepartmentId;
    }

    public String getCaseDepartmentIdName() {
        return caseDepartmentIdName;
    }

    public void setCaseDepartmentIdName(String caseDepartmentIdName) {
        this.caseDepartmentIdName = caseDepartmentIdName;
    }

    public String getCaseDepartment() {
        return caseDepartment;
    }

    public void setCaseDepartment(String caseDepartment) {
        this.caseDepartment = caseDepartment;
    }

    public Integer getCaseOriginId() {
        return caseOriginId;
    }

    public void setCaseOriginId(Integer caseOriginId) {
        this.caseOriginId = caseOriginId;
    }

    public String getCaseOriginIdName() {
        return caseOriginIdName;
    }

    public void setCaseOriginIdName(String caseOriginIdName) {
        this.caseOriginIdName = caseOriginIdName;
    }

    public String getCaseOriginName() {
        return caseOriginName;
    }

    public void setCaseOriginName(String caseOriginName) {
        this.caseOriginName = caseOriginName;
    }

    public String getCaseOriginCode() {
        return caseOriginCode;
    }

    public void setCaseOriginCode(String caseOriginCode) {
        this.caseOriginCode = caseOriginCode;
    }

    public String getCaseOriginIdCodeName() {
        return caseOriginIdCodeName;
    }

    public void setCaseOriginIdCodeName(String caseOriginIdCodeName) {
        this.caseOriginIdCodeName = caseOriginIdCodeName;
    }

    public Integer getCaseTypeId() {
        return caseTypeId;
    }

    public void setCaseTypeId(Integer caseTypeId) {
        this.caseTypeId = caseTypeId;
    }

    public String getCaseTypeIdName() {
        return caseTypeIdName;
    }

    public void setCaseTypeIdName(String caseTypeIdName) {
        this.caseTypeIdName = caseTypeIdName;
    }

    public String getCaseTypeName() {
        return caseTypeName;
    }

    public void setCaseTypeName(String caseTypeName) {
        this.caseTypeName = caseTypeName;
    }

    public String getCaseTypeCode() {
        return caseTypeCode;
    }

    public void setCaseTypeCode(String caseTypeCode) {
        this.caseTypeCode = caseTypeCode;
    }

    public String getCaseTypeIdCodeName() {
        return caseTypeIdCodeName;
    }

    public void setCaseTypeIdCodeName(String caseTypeIdCodeName) {
        this.caseTypeIdCodeName = caseTypeIdCodeName;
    }

    public Integer getCaseReasonId() {
        return caseReasonId;
    }

    public void setCaseReasonId(Integer caseReasonId) {
        this.caseReasonId = caseReasonId;
    }

    public String getCaseReasonIdName() {
        return caseReasonIdName;
    }

    public void setCaseReasonIdName(String caseReasonIdName) {
        this.caseReasonIdName = caseReasonIdName;
    }

    public String getCaseReasonName() {
        return caseReasonName;
    }

    public void setCaseReasonName(String caseReasonName) {
        this.caseReasonName = caseReasonName;
    }

    public Integer getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }

    public String getPriorityIdName() {
        return priorityIdName;
    }

    public void setPriorityIdName(String priorityIdName) {
        this.priorityIdName = priorityIdName;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getPriorityIdCodeName() {
        return priorityIdCodeName;
    }

    public void setPriorityIdCodeName(String priorityIdCodeName) {
        this.priorityIdCodeName = priorityIdCodeName;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }

    public String getPriorityColor() {
        return priorityColor;
    }

    public void setPriorityColor(String priorityColor) {
        this.priorityColor = priorityColor;
    }

    public Integer getPrioritySorder() {
        return prioritySorder;
    }

    public void setPrioritySorder(Integer prioritySorder) {
        this.prioritySorder = prioritySorder;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusIdName() {
        return statusIdName;
    }

    public void setStatusIdName(String statusIdName) {
        this.statusIdName = statusIdName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusIdCodeName() {
        return statusIdCodeName;
    }

    public void setStatusIdCodeName(String statusIdCodeName) {
        this.statusIdCodeName = statusIdCodeName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getStatusSorder() {
        return statusSorder;
    }

    public void setStatusSorder(Integer statusSorder) {
        this.statusSorder = statusSorder;
    }

    public Integer getResolverId() {
        return resolverId;
    }

    public void setResolverId(Integer resolverId) {
        this.resolverId = resolverId;
    }

    public String getResolverIdName() {
        return resolverIdName;
    }

    public void setResolverIdName(String resolverIdName) {
        this.resolverIdName = resolverIdName;
    }

    public String getResolverName() {
        return resolverName;
    }

    public void setResolverName(String resolverName) {
        this.resolverName = resolverName;
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
        return inTrash != null && inTrash;
    }

    public void setInTrash(Boolean inTrash) {
        this.inTrash = inTrash;
    }

    public Boolean getBillable() {
        return billable != null && billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public Boolean getHasAttachment() {
        return hasAttachment != null && hasAttachment;
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

    public Integer getInternalStatusId() {
        return internalStatusId;
    }

    public void setInternalStatusId(Integer internalStatusId) {
        this.internalStatusId = internalStatusId;
    }

    public String getInternalStatusIdName() {
        return internalStatusIdName;
    }

    public void setInternalStatusIdName(String internalStatusIdName) {
        this.internalStatusIdName = internalStatusIdName;
    }

    public String getInternalStatusName() {
        return internalStatusName;
    }

    public void setInternalStatusName(String internalStatusName) {
        this.internalStatusName = internalStatusName;
    }

    public Integer getInternalStatusSorder() {
        return internalStatusSorder;
    }

    public void setInternalStatusSorder(Integer internalStatusSorder) {
        this.internalStatusSorder = internalStatusSorder;
    }

    public Long getKanbanOrder() {
        return kanbanOrder;
    }

    public void setKanbanOrder(Long kanbanOrder) {
        this.kanbanOrder = kanbanOrder;
    }

    public List<String> getDynStringComposite() {
        return dynStringComposite;
    }

    public void setDynStringComposite(List<String> dynStringComposite) {
        this.dynStringComposite = dynStringComposite;
    }
}
