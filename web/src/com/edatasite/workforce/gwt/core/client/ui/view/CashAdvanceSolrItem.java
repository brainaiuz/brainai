package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CashAdvanceSolrItem implements IsSerializable {

    private Integer objectId;
    private Integer cashAdvanceId;
    private SelectItem employee;
    private String driverNumber;
    private SelectItem employeeProfile;
    private String driverId;
    private SelectItem status;
    private SelectItem paymentMethod;
    private BigDecimal totalAmount;
    private BigDecimal paymentAmount;
    private Double percent;
    private Date requestDate;
    private Date approvedDate;
    private Date lastUpdate;
    private String type;
    private String purpose;
    private String composite;
    private String number;
    private Double remainingAmount;
    private SelectItem currency;
    private SelectItem approver;
    private ApproverItem previousApprover;
    private ApproverItem currentApprover;
    private SelectItem overallStatus;
    private List<Integer> payrollBatches = new ArrayList<>();

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getCashAdvanceId() {
        return cashAdvanceId;
    }

    public void setCashAdvanceId(Integer cashAdvanceId) {
        this.cashAdvanceId = cashAdvanceId;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public SelectItem getEmployeeProfile() {
        return employeeProfile;
    }

    public void setEmployeeProfile(SelectItem employeeProfile) {
        this.employeeProfile = employeeProfile;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public SelectItem getApprover() {
        return approver;
    }

    public void setApprover(SelectItem approver) {
        this.approver = approver;
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }

    public SelectItem getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(SelectItem paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getComposite() {
        return composite;
    }

    public void setComposite(String composite) {
        this.composite = composite;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public ApproverItem getPreviousApprover() {
        return previousApprover;
    }

    public void setPreviousApprover(ApproverItem previousApprover) {
        this.previousApprover = previousApprover;
    }

    public ApproverItem getCurrentApprover() {
        return currentApprover;
    }

    public void setCurrentApprover(ApproverItem currentApprover) {
        this.currentApprover = currentApprover;
    }

    public SelectItem getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(SelectItem overallStatus) {
        this.overallStatus = overallStatus;
    }

    public List<Integer> getPayrollBatches() {
        return payrollBatches;
    }

    public void setPayrollBatches(List<Integer> payrollBatches) {
        this.payrollBatches = payrollBatches;
    }
}
