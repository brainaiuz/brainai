package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:32.
 */
@SolrDocument(collection = "singlePayrunCore")
public class SinglePayrunSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    private Integer companyId;

    @Field("singlePayrunId")
    private Integer singlePayrunId;

    @Field("preparerId")
    private Integer preparerId;

    @Field("preparerName")
    private String preparerName;

    @Field("employeeId")
    private Integer employeeId;

    @Field("employeeName")
    private String employeeName;

    @Field("employeeCode")
    private String employeeCode;

    @Field("employeeIntegerNumber")
    private Integer employeeIntegerNumber;

    @Field("employeeIdName")
    @Indexed(name = "employeeIdName", type = "string", stored = false)
    private String employeeIdName;

    @Field("approverId")
    private Integer approverId;

    @Field("approverName")
    private String approverName;

    @Field("approverIdName")
    @Indexed(name = "approverIdName", type = "string", stored = false)
    private String approverIdName;

    @Field("approver2Id")
    private Integer approver2Id;

    @Field("approver2Name")
    private String approver2Name;

    @Field("approver2IdName")
    @Indexed(name = "approver2IdName", type = "string", stored = false)
    private String approver2IdName;

    @Field("statusId")
    private Integer statusId;

    @Field("statusName")
    private String statusName;

    @Field("statusCode")
    private String statusCode;

    @Field("statusIdName")
    private String statusIdName;

    @Field("status2Id")
    private Integer status2Id;

    @Field("status2Name")
    private String status2Name;

    @Field("status2Code")
    private String status2Code;

    @Field("status2IdName")
    @Indexed(name = "status2IdName", type = "string", stored = false)
    private String status2IdName;

    @Field("frequency")
    private Integer frequency;

    @Field("payslipTableId")
    private Integer payslipTableId;

    @Field("payrollBatchId")
    private Integer payrollBatchId;

    @Field("description")
    private String description;

    @Field("basicSalary")
    private String basicSalary;

    @Field("dailyRate")
    private String dailyRate;

    @Field("actualMonthPay")
    private String actualMonthPay;

    @Field("allowance")
    private String allowance;

    @Field("additionalPay")
    private String additionalPay;

    @Field("deduction")
    private String deduction;

    @Field("expense")
    private String expense;

    @Field("total")
    private String total;

    @Field("currencyId")
    private Integer currencyId;

    @Field("currencyName")
    private String currencyName;

    @Field("currencyIdName")
    @Indexed(name = "currencyIdName", type = "string", stored = false)
    private String currencyIdName;

    @Field("currencySymbol")
    private String currencySymbol;

    @Field("usedPertol")
    private String usedPertol;

    @Field("comission")
    private String comission;

    @Field("collection")
    private String collection;

    @Field("pensionRate")
    private String pensionRate;

    @Field("nonLocalPensionRate")
    private String nonLocalPensionRate;

    @Field("pensionValueType")
    private String pensionValueType;

    @Field("companyPensionType")
    private Integer companyPensionType;

    @Field("pensionAmount")
    private String pensionAmount;

    @Field("companyPensionAmount")
    private String companyPensionAmount;

    @Field("companyPensionRate")
    private String companyPensionRate;

    @Field("companyNonLocalPensionRate")
    private String companyNonLocalPensionRate;

    @Field("daysWorked")
    private Long daysWorked;

    @Field("fromDate")
    private Date fromDate;

    @Field("toDate")
    private Date toDate;

    @Field("processDate")
    private Date processDate;

    @Field("creationDate")
    private Date creationDate;

    @Field("approvedDate")
    private Date approvedDate;

    @Field("monthId")
    private Integer monthId;

    @Field("pdfTemplateId")
    private String pdfTemplateId;

    @Field("year")
    private Integer year;

    @Field("yearIdName")
    @Indexed(name = "yearIdName", type = "string", stored = false)
    private String yearIdName;

    @Field("monthName")
    private String monthName;

    @Field("monthIdName")
    @Indexed(name = "monthIdName", type = "string", stored = false)
    private String monthIdName;

    @Field("approved")
    private Boolean approved;

    @Field("transacted")
    private Boolean transacted;

    @Field("sendEmail")
    private Boolean sendEmail;

    @Field("fromEndOfService")
    private Boolean fromEndOfService;

    @Field("rejectionNote")
    private String rejectionNote;

    @Field("paymentPolicy")
    private String paymentPolicy;

    @Field("driverId")
    private Long driverId;

    @Field("deleted")
    private Boolean deleted;

    @Field("lastUpdate")
    private Date lastUpdate;

    @Field("sortableEmployeeName")
    private String sortableEmployeeName;

    @Field("sortablePreparerName")
    private String sortablePreparerName;

    @Field("sortableApproverName")
    private String sortableApproverName;

    @Field("sortableCurrencyName")
    private String sortableCurrencyName;

    @Field("sortableDriverId")
    private Integer sortableDriverId;

    @Field("paymentMethod")
    private Integer paymentMethod;

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

    public Integer getSinglePayrunId() {
        return singlePayrunId;
    }

    public void setSinglePayrunId(Integer singlePayrunId) {
        this.singlePayrunId = singlePayrunId;
    }

    public Integer getPreparerId() {
        return preparerId;
    }

    public void setPreparerId(Integer preparerId) {
        this.preparerId = preparerId;
    }

    public String getPreparerName() {
        return preparerName;
    }

    public void setPreparerName(String preparerName) {
        this.preparerName = preparerName;
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

    public Integer getEmployeeIntegerNumber() {
        return employeeIntegerNumber;
    }

    public void setEmployeeIntegerNumber(Integer employeeIntegerNumber) {
        this.employeeIntegerNumber = employeeIntegerNumber;
    }

    public String getEmployeeIdName() {
        return employeeIdName;
    }

    public void setEmployeeIdName(String employeeIdName) {
        this.employeeIdName = employeeIdName;
    }

    public Integer getApproverId() {
        return approverId;
    }

    public void setApproverId(Integer approverId) {
        this.approverId = approverId;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public String getApproverIdName() {
        return approverIdName;
    }

    public void setApproverIdName(String approverIdName) {
        this.approverIdName = approverIdName;
    }

    public Integer getApprover2Id() {
        return approver2Id;
    }

    public void setApprover2Id(Integer approver2Id) {
        this.approver2Id = approver2Id;
    }

    public String getApprover2Name() {
        return approver2Name;
    }

    public void setApprover2Name(String approver2Name) {
        this.approver2Name = approver2Name;
    }

    public String getApprover2IdName() {
        return approver2IdName;
    }

    public void setApprover2IdName(String approver2IdName) {
        this.approver2IdName = approver2IdName;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusIdName() {
        return statusIdName;
    }

    public void setStatusIdName(String statusIdName) {
        this.statusIdName = statusIdName;
    }

    public Integer getStatus2Id() {
        return status2Id;
    }

    public void setStatus2Id(Integer status2Id) {
        this.status2Id = status2Id;
    }

    public String getStatus2Name() {
        return status2Name;
    }

    public void setStatus2Name(String status2Name) {
        this.status2Name = status2Name;
    }

    public String getStatus2Code() {
        return status2Code;
    }

    public void setStatus2Code(String status2Code) {
        this.status2Code = status2Code;
    }

    public String getStatus2IdName() {
        return status2IdName;
    }

    public void setStatus2IdName(String status2IdName) {
        this.status2IdName = status2IdName;
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

    public String getPensionValueType() {
        return pensionValueType;
    }

    public void setPensionValueType(String pensionValueType) {
        this.pensionValueType = pensionValueType;
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

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyIdName() {
        return currencyIdName;
    }

    public void setCurrencyIdName(String currencyIdName) {
        this.currencyIdName = currencyIdName;
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
        return processDate != null ? processDate : toDate;
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

    public Integer getMonthId() {
        return monthId;
    }

    public void setMonthId(Integer monthId) {
        this.monthId = monthId;
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

    public String getYearIdName() {
        return yearIdName;
    }

    public void setYearIdName(String yearIdName) {
        this.yearIdName = yearIdName;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getMonthIdName() {
        return monthIdName;
    }

    public void setMonthIdName(String monthIdName) {
        this.monthIdName = monthIdName;
    }

    public Boolean getApproved() {
        return approved != null && approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Boolean getTransacted() {
        return transacted != null && transacted;
    }

    public void setTransacted(Boolean transacted) {
        this.transacted = transacted;
    }

    public Boolean getSendEmail() {
        return sendEmail != null && sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Boolean getFromEndOfService() {
        return fromEndOfService != null && fromEndOfService;
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
        return deleted != null && deleted;
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
