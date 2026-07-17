package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.PaymentMethodItem;
import com.edatasite.workforce.gwt.core.client.rpc.LocationItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.math.BigDecimal;
import java.util.Date;

public class PayslipTable {
    private Integer id;
    private Date creationDate;
    private SelectItem preparer;
    private ReferenceItem status;
    private SelectItem approver;
    private Date lastUpdateTime;
    private Date approvedDate;
    private Date processDate;
    private SelectItem month;
    private Integer year;
    private BigDecimal totalAmount;
    private BigDecimal totalInBase;
    private SelectItem payrollBatch;
    private PaymentMethodItem paymentMethod;
    private SelectItem currency;
    private SelectItem project;
    private LocationItem location;
    private Boolean deleted;
    private Integer frequency;
    private BigDecimal pension;
    private BigDecimal deduction;
    private BigDecimal expense;

    public PayslipTable() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setPreparer(SelectItem preparer) {
        this.preparer = preparer;
    }

    public SelectItem getPreparer() {
        return preparer;
    }

    public void setStatus(ReferenceItem status) {
        this.status = status;
    }

    public ReferenceItem getStatus() {
        return status;
    }

    public void setApprover(SelectItem approver) {
        this.approver = approver;
    }

    public SelectItem getApprover() {
        return approver;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setMonth(SelectItem month) {
        this.month = month;
    }

    public SelectItem getMonth() {
        return month;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getYear() {
        return year;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalInBase(BigDecimal totalInBase) {
        this.totalInBase = totalInBase;
    }

    public BigDecimal getTotalInBase() {
        return totalInBase;
    }

    public void setPayrollBatch(SelectItem payrollBatch) {
        this.payrollBatch = payrollBatch;
    }

    public SelectItem getPayrollBatch() {
        return payrollBatch;
    }

    public void setPaymentMethod(PaymentMethodItem paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethodItem getPaymentMethod() {
        return paymentMethod;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setLocation(LocationItem location) {
        this.location = location;
    }

    public LocationItem getLocation() {
        return location;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setPension(BigDecimal pension) {
        this.pension = pension;
    }

    public BigDecimal getPension() {
        return pension;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public BigDecimal getExpense() {
        return expense;
    }
}
