package com.edatasite.workforce.gwt.expenses.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpenseReportsSolrItem implements IsSerializable {

    private Integer objectID;
    private String title;
    private Date startDate;
    private Date endDate;
    private SelectItem relatedProject;
    private SelectItem reporter;
    private SelectItem approver;
    private SelectItem approver2;
    private SelectItem status;
    private SelectItem status2;
    private SelectItem fixedAsset;
    private Double orginalAmount;
    private Double paidAmount;
    private Double dueAmount;
    private Double taxAmount;
    private String numbering;
    private Boolean isCompanyExpense;
    private SelectItem currency;
    private List<SelectItem> multiProject = new ArrayList<>();
    private SelectItem supplier;
    private List<Integer> supplierOwnerIds = new ArrayList<>();
    private SelectItem previousApprover;
    private SelectItem previousApproverStatus;
    private SelectItem previousApproverExactEmployee;
    private SelectItem currentApprover;
    private SelectItem currentApproverStatus;
    private SelectItem currentApproverExactEmployee;
    private SelectItem overallStatus;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public SelectItem getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(SelectItem relatedProject) {
        this.relatedProject = relatedProject;
    }

    public SelectItem getReporter() {
        return reporter;
    }

    public void setReporter(SelectItem reporter) {
        this.reporter = reporter;
    }

    public SelectItem getApprover() {
        return approver;
    }

    public void setApprover(SelectItem approver) {
        this.approver = approver;
    }

    public SelectItem getApprover2() {
        return approver2;
    }

    public void setApprover2(SelectItem approver2) {
        this.approver2 = approver2;
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }

    public SelectItem getStatus2() {
        return status2;
    }

    public void setStatus2(SelectItem status2) {
        this.status2 = status2;
    }

    public SelectItem getFixedAsset() {
        return fixedAsset;
    }

    public void setFixedAsset(SelectItem fixedAsset) {
        this.fixedAsset = fixedAsset;
    }

    public Double getOrginalAmount() {
        return orginalAmount;
    }

    public void setOrginalAmount(Double orginalAmount) {
        this.orginalAmount = orginalAmount;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getNumbering() {
        return numbering;
    }

    public void setNumbering(String numbering) {
        this.numbering = numbering;
    }

    public Boolean getCompanyExpense() {
        return isCompanyExpense;
    }

    public void setCompanyExpense(Boolean companyExpense) {
        isCompanyExpense = companyExpense;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public List<SelectItem> getMultiProject() {
        return multiProject;
    }

    public void setMultiProject(List<SelectItem> multiProject) {
        this.multiProject = multiProject;
    }

    public SelectItem getSupplier() {
        return supplier;
    }

    public void setSupplier(SelectItem supplier) {
        this.supplier = supplier;
    }

    public List<Integer> getSupplierOwnerIds() {
        return supplierOwnerIds;
    }

    public void setSupplierOwnerIds(List<Integer> supplierOwnerIds) {
        this.supplierOwnerIds = supplierOwnerIds;
    }

    public SelectItem getPreviousApprover() {
        return previousApprover;
    }

    public void setPreviousApprover(SelectItem previousApprover) {
        this.previousApprover = previousApprover;
    }

    public SelectItem getPreviousApproverStatus() {
        return previousApproverStatus;
    }

    public void setPreviousApproverStatus(SelectItem previousApproverStatus) {
        this.previousApproverStatus = previousApproverStatus;
    }

    public SelectItem getPreviousApproverExactEmployee() {
        return previousApproverExactEmployee;
    }

    public void setPreviousApproverExactEmployee(SelectItem previousApproverExactEmployee) {
        this.previousApproverExactEmployee = previousApproverExactEmployee;
    }

    public SelectItem getCurrentApprover() {
        return currentApprover;
    }

    public void setCurrentApprover(SelectItem currentApprover) {
        this.currentApprover = currentApprover;
    }

    public SelectItem getCurrentApproverStatus() {
        return currentApproverStatus;
    }

    public void setCurrentApproverStatus(SelectItem currentApproverStatus) {
        this.currentApproverStatus = currentApproverStatus;
    }

    public SelectItem getCurrentApproverExactEmployee() {
        return currentApproverExactEmployee;
    }

    public void setCurrentApproverExactEmployee(SelectItem currentApproverExactEmployee) {
        this.currentApproverExactEmployee = currentApproverExactEmployee;
    }

    public SelectItem getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(SelectItem overallStatus) {
        this.overallStatus = overallStatus;
    }
}
