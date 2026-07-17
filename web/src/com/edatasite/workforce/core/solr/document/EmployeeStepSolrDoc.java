package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:27.
 */
@SolrDocument(collection = "employeeStepCore")
public class EmployeeStepSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("stepId")
    @Indexed(name = "stepId", type = "pint", required = true)
    private Integer stepId;

    @Field("workflowId")
    @Indexed(name = "workflowId", type = "pint", stored = false)
    private Integer workflowId;

    @Field("onboardingStepId")
    private Integer onboardingStepId;

    @Field("onboardingStepFormId")
    private String onboardingStepFormId;

    @Field("onboardingStepName")
    private String onboardingStepName;

    @Field("onboardingStepIdName")
    @Indexed(name = "onboardingStepIdName", type = "string", stored = false)
    private String onboardingStepIdName;

    @Field("employeeId")
    private Integer employeeId;

    @Field("employeeName")
    private String employeeName;

    @Field("employeeCode")
    private String employeeCode;

    @Field("candidateCode")
    private String candidateCode;

    @Field("employeeIdName")
    @Indexed(name = "employeeIdName", type = "string", stored = false)
    private String employeeIdName;

    @Field("employeeLocationId")
    @Indexed(name = "employeeLocationId", type = "pint", stored = false)
    private Integer employeeLocationId;

    @Field("employeeLocationName")
    private String employeeLocationName;

    @Field("employeeLocationIdName")
    @Indexed(name = "employeeLocationIdName", type = "string", stored = false)
    private String employeeLocationIdName;

    @Field("employeeLocationState")
    @Indexed(name = "employeeLocationState", type = "string", stored = false)
    private String employeeLocationState;

    @Field("employeeLocationCity")
    @Indexed(name = "employeeLocationCity", type = "string", stored = false)
    private String employeeLocationCity;

    @Field("creatorId")
    @Indexed(name = "creatorId", type = "pint", stored = false)
    private Integer creatorId;

    @Field("creatorName")
    private String creatorName;

    @Field("creatorIdName")
    @Indexed(name = "creatorIdName", type = "string", stored = false)
    private String creatorIdName;

    @Field("statusId")
    private Integer statusId;

    @Field("statusName")
    private String statusName;

    @Field("statusIdName")
    @Indexed(name = "statusIdName", type = "string", stored = false)
    private String statusIdName;

    @Field("typeId")
    @Indexed(name = "typeId", type = "pint", stored = false)
    private Integer typeId;

    @Field("typeCode")
    private String typeCode;

    @Field("typeName")
    private String typeName;

    @Field("typeIdName")
    @Indexed(name = "typeIdName", type = "string", stored = false)
    private String typeIdName;

    @Field("currentApproverId")
    private Integer currentApproverId;

    @Field("approverApproveStatusId")
    private Integer approverApproveStatusId;

    @Field("approverRejectStatusId")
    private Integer approverRejectStatusId;

    @Field("creationDate")
    private Date creationDate;

    @Field("modificationDate")
    private Date modificationDate;

    @Field("archived")
    private Boolean archived;

    @Field("composite")
    private String composite;

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

    public Integer getStepId() {
        return stepId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public Integer getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Integer workflowId) {
        this.workflowId = workflowId;
    }

    public Integer getOnboardingStepId() {
        return onboardingStepId;
    }

    public void setOnboardingStepId(Integer onboardingStepId) {
        this.onboardingStepId = onboardingStepId;
    }

    public String getOnboardingStepFormId() {
        return onboardingStepFormId;
    }

    public void setOnboardingStepFormId(String onboardingStepFormId) {
        this.onboardingStepFormId = onboardingStepFormId;
    }

    public String getOnboardingStepName() {
        return onboardingStepName;
    }

    public void setOnboardingStepName(String onboardingStepName) {
        this.onboardingStepName = onboardingStepName;
    }

    public String getOnboardingStepIdName() {
        return onboardingStepIdName;
    }

    public void setOnboardingStepIdName(String onboardingStepIdName) {
        this.onboardingStepIdName = onboardingStepIdName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getCandidateCode() {
        return candidateCode;
    }

    public void setCandidateCode(String candidateCode) {
        this.candidateCode = candidateCode;
    }

    public String getEmployeeIdName() {
        return employeeIdName;
    }

    public void setEmployeeIdName(String employeeIdName) {
        this.employeeIdName = employeeIdName;
    }

    public Integer getEmployeeLocationId() {
        return employeeLocationId;
    }

    public void setEmployeeLocationId(Integer employeeLocationId) {
        this.employeeLocationId = employeeLocationId;
    }

    public String getEmployeeLocationName() {
        return employeeLocationName;
    }

    public void setEmployeeLocationName(String employeeLocationName) {
        this.employeeLocationName = employeeLocationName;
    }

    public String getEmployeeLocationIdName() {
        return employeeLocationIdName;
    }

    public void setEmployeeLocationIdName(String employeeLocationIdName) {
        this.employeeLocationIdName = employeeLocationIdName;
    }

    public String getEmployeeLocationState() {
        return employeeLocationState;
    }

    public void setEmployeeLocationState(String employeeLocationState) {
        this.employeeLocationState = employeeLocationState;
    }

    public String getEmployeeLocationCity() {
        return employeeLocationCity;
    }

    public void setEmployeeLocationCity(String employeeLocationCity) {
        this.employeeLocationCity = employeeLocationCity;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorIdName() {
        return creatorIdName;
    }

    public void setCreatorIdName(String creatorIdName) {
        this.creatorIdName = creatorIdName;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusIdName() {
        return statusIdName;
    }

    public void setStatusIdName(String statusIdName) {
        this.statusIdName = statusIdName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeIdName() {
        return typeIdName;
    }

    public void setTypeIdName(String typeIdName) {
        this.typeIdName = typeIdName;
    }

    public Integer getCurrentApproverId() {
        return currentApproverId;
    }

    public void setCurrentApproverId(Integer currentApproverId) {
        this.currentApproverId = currentApproverId;
    }

    public Integer getApproverApproveStatusId() {
        return approverApproveStatusId;
    }

    public void setApproverApproveStatusId(Integer approverApproveStatusId) {
        this.approverApproveStatusId = approverApproveStatusId;
    }

    public Integer getApproverRejectStatusId() {
        return approverRejectStatusId;
    }

    public void setApproverRejectStatusId(Integer approverRejectStatusId) {
        this.approverRejectStatusId = approverRejectStatusId;
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

    public Boolean getArchived() {
        return archived != null && archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public String getComposite() {
        return composite;
    }

    public void setComposite(String composite) {
        this.composite = composite;
    }
}
