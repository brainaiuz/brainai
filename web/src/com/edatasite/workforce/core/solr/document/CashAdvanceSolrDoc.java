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
@SolrDocument(collection = "cashAdvanceCore")
public class CashAdvanceSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    private Integer companyId;

    @Field("cashAdvanceId")
    @Indexed(name = "cashAdvanceId", type = "pint", required = true)
    private Integer cashAdvanceId;

    @Field("employeeId")
    private Integer employeeId;

    @Field("employeeName")
    private String employeeName;

    @Field("employeeIdName")
    @Indexed(name = "employeeIdName", type = "string", stored = false)
    private String employeeIdName;

    @Field("employeeCode")
    private String employeeCode;

    @Field("driverId")
    private String driverId;

    @Field("approverId")
    private Integer approverId;

    @Field("approverName")
    private String approverName;

    @Field("approverIdName")
    @Indexed(name = "approverIdName", type = "string", stored = false)
    private String approverIdName;

    @Field("statusId")
    private Integer statusId;

    @Field("statusName")
    private String statusName;

    @Field("statusCode")
    private String statusCode;

    @Field("statusIdName")
    private String statusIdName;

    @Field("paymentMethodId")
    private Integer paymentMethodId;

    @Field("paymentMethodName")
    private String paymentMethodName;

    @Field("paymentMethodCode")
    private String paymentMethodCode;

    @Field("totalAmount")
    private Double totalAmount;

    @Field("paymentAmount")
    private Double paymentAmount;

    @Field("percent")
    private Double percent;

    @Field("requestDate")
    private Date requestDate;

    @Field("approvedDate")
    private Date approvedDate;

    @Field("lastUpdate")
    private Date lastUpdate;

    @Field("type")
    private String type;

    @Field("purpose")
    private String purpose;

    @Field("payrollBatchId")
    @Indexed(name = "payrollBatchId", type = "pints")
    private List<Integer> payrollBatchId = new ArrayList<>();

    @Field("composite")
    private String composite;

    @Field("number")
    private String number;

    @Field("remainingAmount")
    private Double remainingAmount;

    @Field("currencyId")
    private Integer currencyId;

    @Field("currencyName")
    private String currencyName;

    @Field("previousApproverId")
    private Integer previousApproverId;

    @Field("previousApproverName")
    private String previousApproverName;

    @Field("previousApproverIdName")
    @Indexed(name = "previousApproverIdName", type = "string", stored = false)
    private String previousApproverIdName;

    @Field("previousApproverStatusId")
    private Integer previousApproverStatusId;

    @Field("previousApproverStatusCode")
    private String previousApproverStatusCode;

    @Field("previousApproverExactEmployeeId")
    private Integer previousApproverExactEmployeeId;

    @Field("previousApproverExactEmployeeName")
    private String previousApproverExactEmployeeName;

    @Field("currentApproverId")
    private Integer currentApproverId;

    @Field("currentApproverName")
    private String currentApproverName;

    @Field("currentApproverIdName")
    @Indexed(name = "currentApproverIdName", type = "string", stored = false)
    private String currentApproverIdName;

    @Field("currentApproverStatusId")
    private Integer currentApproverStatusId;

    @Field("currentApproverStatusCode")
    private String currentApproverStatusCode;

    @Field("currentApproverExactEmployeeId")
    private Integer currentApproverExactEmployeeId;

    @Field("currentApproverExactEmployeeName")
    private String currentApproverExactEmployeeName;

    @Field("overallStatusId")
    private Integer overallStatusId;

    @Field("overallStatusName")
    private String overallStatusName;

    @Field("overallStatusCode")
    private String overallStatusCode;

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

    public Integer getCashAdvanceId() {
        return cashAdvanceId;
    }

    public void setCashAdvanceId(Integer cashAdvanceId) {
        this.cashAdvanceId = cashAdvanceId;
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

    public String getEmployeeIdName() {
        return employeeIdName;
    }

    public void setEmployeeIdName(String employeeIdName) {
        this.employeeIdName = employeeIdName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
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

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public void setPaymentMethodCode(String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
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

    public List<Integer> getPayrollBatchId() {
        return payrollBatchId;
    }

    public void setPayrollBatchId(List<Integer> payrollBatchId) {
        this.payrollBatchId = payrollBatchId;
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

    public Integer getPreviousApproverId() {
        return previousApproverId;
    }

    public void setPreviousApproverId(Integer previousApproverId) {
        this.previousApproverId = previousApproverId;
    }

    public String getPreviousApproverName() {
        return previousApproverName;
    }

    public void setPreviousApproverName(String previousApproverName) {
        this.previousApproverName = previousApproverName;
    }

    public String getPreviousApproverIdName() {
        return previousApproverIdName;
    }

    public void setPreviousApproverIdName(String previousApproverIdName) {
        this.previousApproverIdName = previousApproverIdName;
    }

    public Integer getPreviousApproverStatusId() {
        return previousApproverStatusId;
    }

    public void setPreviousApproverStatusId(Integer previousApproverStatusId) {
        this.previousApproverStatusId = previousApproverStatusId;
    }

    public String getPreviousApproverStatusCode() {
        return previousApproverStatusCode;
    }

    public void setPreviousApproverStatusCode(String previousApproverStatusCode) {
        this.previousApproverStatusCode = previousApproverStatusCode;
    }

    public Integer getPreviousApproverExactEmployeeId() {
        return previousApproverExactEmployeeId;
    }

    public void setPreviousApproverExactEmployeeId(Integer previousApproverExactEmployeeId) {
        this.previousApproverExactEmployeeId = previousApproverExactEmployeeId;
    }

    public String getPreviousApproverExactEmployeeName() {
        return previousApproverExactEmployeeName;
    }

    public void setPreviousApproverExactEmployeeName(String previousApproverExactEmployeeName) {
        this.previousApproverExactEmployeeName = previousApproverExactEmployeeName;
    }

    public Integer getCurrentApproverId() {
        return currentApproverId;
    }

    public void setCurrentApproverId(Integer currentApproverId) {
        this.currentApproverId = currentApproverId;
    }

    public String getCurrentApproverName() {
        return currentApproverName;
    }

    public void setCurrentApproverName(String currentApproverName) {
        this.currentApproverName = currentApproverName;
    }

    public String getCurrentApproverIdName() {
        return currentApproverIdName;
    }

    public void setCurrentApproverIdName(String currentApproverIdName) {
        this.currentApproverIdName = currentApproverIdName;
    }

    public Integer getCurrentApproverStatusId() {
        return currentApproverStatusId;
    }

    public void setCurrentApproverStatusId(Integer currentApproverStatusId) {
        this.currentApproverStatusId = currentApproverStatusId;
    }

    public String getCurrentApproverStatusCode() {
        return currentApproverStatusCode;
    }

    public void setCurrentApproverStatusCode(String currentApproverStatusCode) {
        this.currentApproverStatusCode = currentApproverStatusCode;
    }

    public Integer getCurrentApproverExactEmployeeId() {
        return currentApproverExactEmployeeId;
    }

    public void setCurrentApproverExactEmployeeId(Integer currentApproverExactEmployeeId) {
        this.currentApproverExactEmployeeId = currentApproverExactEmployeeId;
    }

    public String getCurrentApproverExactEmployeeName() {
        return currentApproverExactEmployeeName;
    }

    public void setCurrentApproverExactEmployeeName(String currentApproverExactEmployeeName) {
        this.currentApproverExactEmployeeName = currentApproverExactEmployeeName;
    }

    public Integer getOverallStatusId() {
        return overallStatusId;
    }

    public void setOverallStatusId(Integer overallStatusId) {
        this.overallStatusId = overallStatusId;
    }

    public String getOverallStatusName() {
        return overallStatusName;
    }

    public void setOverallStatusName(String overallStatusName) {
        this.overallStatusName = overallStatusName;
    }

    public String getOverallStatusCode() {
        return overallStatusCode;
    }

    public void setOverallStatusCode(String overallStatusCode) {
        this.overallStatusCode = overallStatusCode;
    }
}
