package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EditSubscription implements IsSerializable {
    private Integer taskLimit;
    private Integer projectLimit;
    private Integer employeeLimit;
    private Integer objectID;
    private String name;
    private Integer departmentLimit;
    private Integer attachmentsFileSizePerUserLimit;
    private Integer attachmentsSizePerCompany;
    private Integer appraisalsLimit;
    private Integer appraisals360Limit;
    private Integer invoiceLimit;


    public Integer getTaskLimit() {
        return taskLimit;
    }

    public void setTaskLimit(Integer taskLimit) {
        this.taskLimit = taskLimit;
    }

    public Integer getProjectLimit() {
        return projectLimit;
    }

    public void setProjectLimit(Integer projectLimit) {
        this.projectLimit = projectLimit;
    }

    public Integer getEmployeeLimit() {
        return employeeLimit;
    }

    public void setEmployeeLimit(Integer employeeLimit) {
        this.employeeLimit = employeeLimit;
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

    public Integer getDepartmentLimit() {
        return departmentLimit;
    }

    public void setDepartmentLimit(Integer departmentLimit) {
        this.departmentLimit = departmentLimit;
    }

    public Integer getAttachmentsFileSizePerUserLimit() {
        return attachmentsFileSizePerUserLimit;
    }

    public void setAttachmentsFileSizePerUserLimit(
            Integer attachmentsFileSizePerUserLimit) {
        this.attachmentsFileSizePerUserLimit = attachmentsFileSizePerUserLimit;
    }

    public Integer getAttachmentsSizePerCompany() {
        return attachmentsSizePerCompany;
    }

    public void setAttachmentsSizePerCompany(Integer attachmentsSizePerCompany) {
        this.attachmentsSizePerCompany = attachmentsSizePerCompany;
    }

    public Integer getAppraisalsLimit() {
        return appraisalsLimit;
    }

    public void setAppraisalsLimit(Integer appraisalsLimit) {
        this.appraisalsLimit = appraisalsLimit;
    }

    public Integer getAppraisals360Limit() {
        return appraisals360Limit;
    }

    public void setAppraisals360Limit(Integer appraisals360Limit) {
        this.appraisals360Limit = appraisals360Limit;
    }

    public Integer getInvoiceLimit() {
        return invoiceLimit;
    }

    public void setInvoiceLimit(Integer invoiceLimit) {
        this.invoiceLimit = invoiceLimit;
    }


}



