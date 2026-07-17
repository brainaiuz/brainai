package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class SinglePayrunSolrItem implements IsSerializable {

    private Integer ObjectID;
    private SelectItem preparer;
    private SelectItem employee;
    private SelectItem approver;
    private SelectItem approver2;
    private ReferenceItem status;
    private ReferenceItem status2;
    private Integer frequency;
    private Integer payslipTableId;
    private Integer payrollBatchId;
    private String description;
    private String basicSalary;
    private String dailyRate;
    private String actualMonthPay;
    private String allowance;
    private String additionalPay;
    private String deduction;
    private String expense;
    private String total;
    private SelectItem currency;
    private String currencySymbol;
    private String usedPertol;
    private String comission;
    private String collection;
    private String pensionRate;
    private String nonLocalPensionRate;
    private String pensionValueType;
    private Integer companyPensionType;
    private String pensionAmount;
    private String companyPensionAmount;
    private String companyPensionRate;
    private String companyNonLocalPensionRate;
    private Long daysWorked;
    private Date fromDate;
    private Date toDate;
    private Date processDate;
    private Date creationDate;
    private Date approvedDate;
    private String pdfTemplateId;
    private Integer year;
    private SelectItem month;
    private Boolean approved;
    private Boolean transacted;
    private Boolean sendEmail;
    private Boolean fromEndOfService;
    private String rejectionNote;
    private String paymentPolicy;
    private Long driverId;
    private Boolean deleted;
    private Date lastUpdate;
    private String sortableEmployeeName;
    private String sortablePreparerName;
    private String sortableApproverName;
    private String sortableCurrencyName;
    private Integer sortableDriverId;
    private Integer paymentMethod;

    public Integer getObjectID() {
        return ObjectID;
    }

    public void setObjectID(Integer objectID) {
        ObjectID = objectID;
    }

    public SelectItem getPreparer() {
        return preparer;
    }

    public void setPreparer(SelectItem preparer) {
        this.preparer = preparer;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
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

    public ReferenceItem getStatus() {
        return status;
    }

    public void setStatus(ReferenceItem status) {
        this.status = status;
    }

    public ReferenceItem getStatus2() {
        return status2;
    }

    public void setStatus2(ReferenceItem status2) {
        this.status2 = status2;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getPayslipTableId() {
        return payslipTableId;
    }

    public void setPayslipTableId(Integer payslipTableId) {
        this.payslipTableId = payslipTableId;
    }

    public Integer getPayrollBatchId() {
        return payrollBatchId;
    }

    public void setPayrollBatchId(Integer payrollBatchId) {
        this.payrollBatchId = payrollBatchId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(String basicSalary) {
        this.basicSalary = basicSalary;
    }

    public String getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(String dailyRate) {
        this.dailyRate = dailyRate;
    }

    public String getActualMonthPay() {
        return actualMonthPay;
    }

    public void setActualMonthPay(String actualMonthPay) {
        this.actualMonthPay = actualMonthPay;
    }

    public String getAllowance() {
        return allowance;
    }

    public void setAllowance(String allowance) {
        this.allowance = allowance;
    }

    public String getAdditionalPay() {
        return additionalPay;
    }

    public void setAdditionalPay(String additionalPay) {
        this.additionalPay = additionalPay;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getUsedPertol() {
        return usedPertol;
    }

    public void setUsedPertol(String usedPertol) {
        this.usedPertol = usedPertol;
    }

    public String getComission() {
        return comission;
    }

    public void setComission(String comission) {
        this.comission = comission;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getPensionRate() {
        return pensionRate;
    }

    public void setPensionRate(String pensionRate) {
        this.pensionRate = pensionRate;
    }

    public String getNonLocalPensionRate() {
        return nonLocalPensionRate;
    }

    public void setNonLocalPensionRate(String nonLocalPensionRate) {
        this.nonLocalPensionRate = nonLocalPensionRate;
    }

    public String getPensionValueType() {
        return pensionValueType;
    }

    public void setPensionValueType(String pensionValueType) {
        this.pensionValueType = pensionValueType;
    }

    public Integer getCompanyPensionType() {
        return companyPensionType;
    }

    public void setCompanyPensionType(Integer companyPensionType) {
        this.companyPensionType = companyPensionType;
    }

    public String getPensionAmount() {
        return pensionAmount;
    }

    public void setPensionAmount(String pensionAmount) {
        this.pensionAmount = pensionAmount;
    }

    public String getCompanyPensionAmount() {
        return companyPensionAmount;
    }

    public void setCompanyPensionAmount(String companyPensionAmount) {
        this.companyPensionAmount = companyPensionAmount;
    }

    public String getCompanyPensionRate() {
        return companyPensionRate;
    }

    public void setCompanyPensionRate(String companyPensionRate) {
        this.companyPensionRate = companyPensionRate;
    }

    public String getCompanyNonLocalPensionRate() {
        return companyNonLocalPensionRate;
    }

    public void setCompanyNonLocalPensionRate(String companyNonLocalPensionRate) {
        this.companyNonLocalPensionRate = companyNonLocalPensionRate;
    }

    public Long getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(Long daysWorked) {
        this.daysWorked = daysWorked;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getPdfTemplateId() {
        return pdfTemplateId;
    }

    public void setPdfTemplateId(String pdfTemplateId) {
        this.pdfTemplateId = pdfTemplateId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public SelectItem getMonth() {
        return month;
    }

    public void setMonth(SelectItem month) {
        this.month = month;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Boolean getTransacted() {
        return transacted;
    }

    public void setTransacted(Boolean transacted) {
        this.transacted = transacted;
    }

    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Boolean getFromEndOfService() {
        return fromEndOfService;
    }

    public void setFromEndOfService(Boolean fromEndOfService) {
        this.fromEndOfService = fromEndOfService;
    }

    public String getRejectionNote() {
        return rejectionNote;
    }

    public void setRejectionNote(String rejectionNote) {
        this.rejectionNote = rejectionNote;
    }

    public String getPaymentPolicy() {
        return paymentPolicy;
    }

    public void setPaymentPolicy(String paymentPolicy) {
        this.paymentPolicy = paymentPolicy;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getSortableEmployeeName() {
        return sortableEmployeeName;
    }

    public void setSortableEmployeeName(String sortableEmployeeName) {
        this.sortableEmployeeName = sortableEmployeeName;
    }

    public String getSortablePreparerName() {
        return sortablePreparerName;
    }

    public void setSortablePreparerName(String sortablePreparerName) {
        this.sortablePreparerName = sortablePreparerName;
    }

    public String getSortableApproverName() {
        return sortableApproverName;
    }

    public void setSortableApproverName(String sortableApproverName) {
        this.sortableApproverName = sortableApproverName;
    }

    public String getSortableCurrencyName() {
        return sortableCurrencyName;
    }

    public void setSortableCurrencyName(String sortableCurrencyName) {
        this.sortableCurrencyName = sortableCurrencyName;
    }

    public Integer getSortableDriverId() {
        return sortableDriverId;
    }

    public void setSortableDriverId(Integer sortableDriverId) {
        this.sortableDriverId = sortableDriverId;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
