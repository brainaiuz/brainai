package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class EmployeeStepSolrItem implements IsSerializable {

    private Integer objectId;
    private Integer workflowId;
    private String onboardingStepFormId;
    private SelectItem onboardingStep;
    private SelectItem employee;
    private String candidateCode;
    private SelectItem employeeLocation;
    private String employeeLocationState;
    private String employeeLocationCity;
    private SelectItem creator;
    private SelectItem status;
    private SelectItem type;
    private SelectItem currentApprover;
    private Integer approverApproveStatusId;
    private Integer approverRejectStatusId;
    private Date creationDate;
    private Date modificationDate;
    private Boolean archived;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Integer workflowId) {
        this.workflowId = workflowId;
    }

    public String getOnboardingStepFormId() {
        return onboardingStepFormId;
    }

    public void setOnboardingStepFormId(String onboardingStepFormId) {
        this.onboardingStepFormId = onboardingStepFormId;
    }

    public SelectItem getOnboardingStep() {
        return onboardingStep;
    }

    public void setOnboardingStep(SelectItem onboardingStep) {
        this.onboardingStep = onboardingStep;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public String getCandidateCode() {
        return candidateCode;
    }

    public void setCandidateCode(String candidateCode) {
        this.candidateCode = candidateCode;
    }

    public SelectItem getEmployeeLocation() {
        return employeeLocation;
    }

    public void setEmployeeLocation(SelectItem employeeLocation) {
        this.employeeLocation = employeeLocation;
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

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }

    public SelectItem getType() {
        return type;
    }

    public void setType(SelectItem type) {
        this.type = type;
    }

    public SelectItem getCurrentApprover() {
        return currentApprover;
    }

    public void setCurrentApprover(SelectItem currentApprover) {
        this.currentApprover = currentApprover;
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
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
}
