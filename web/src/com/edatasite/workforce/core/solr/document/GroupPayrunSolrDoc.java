package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:28.
 */
@SolrDocument(collection = "groupPayrunCore")
public class GroupPayrunSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("groupPayrunId")
    @Indexed(name = "groupPayrunId", type = "pint", required = true)
    private Integer groupPayrunId;

    @Field("creationDate")
    private Date creationDate;

    @Field("preparerId")
    private Integer preparerId;

    @Field("preparerName")
    private String preparerName;

    @Field("preparerIdName")
    @Indexed(name = "preparerIdName", type = "string", stored = false)
    private String preparerIdName;

    @Field("statusId")
    private Integer statusId;

    @Field("statusName")
    private String statusName;

    @Field("statusCode")
    private String statusCode;

    @Field("statusIdName")
    @Indexed(name = "statusIdName", type = "string", stored = false)
    private String statusIdName;

    @Field("approverId")
    private Integer approverId;

    @Field("approverName")
    private String approverName;

    @Field("approverIdName")
    @Indexed(name = "approverIdName", type = "string", stored = false)
    private String approverIdName;

    @Field("lastUpdate")
    private Date lastUpdate;

    @Field("approvedDate")
    private Date approvedDate;

    @Field("processDate")
    private Date processDate;

    @Field("monthId")
    private Integer monthId;

    @Field("monthName")
    private String monthName;

    @Field("monthIdName")
    @Indexed(name = "monthIdName", type = "string", stored = false)
    private String monthIdName;

    @Field("year")
    private Integer year;

    @Field("yearIdName")
    @Indexed(name = "yearIdName", type = "string", stored = false)
    private String yearIdName;

    @Field("total")
    private Double total;

    @Field("totalInBase")
    private Double totalInBase;

    @Field("payrollBatchId")
    private Integer payrollBatchId;

    @Field("payrollBatchName")
    private String payrollBatchName;

    @Field("paymentMethodName")
    private String paymentMethodName;

    @Field("foreignCurrencyName")
    private String foreignCurrencyName;

    @Field("projectId")
    private Integer projectId;

    @Field("projectName")
    private String projectName;

    @Field("deleted")
    private Boolean deleted;

    @Field("frequency")
    private Integer frequency;

    @Field("expense")
    private Double expense;

    @Field("basicSalary")
    private Double basicSalary;

    @Field("allowance")
    private Double allowance;

    @Field("pension")
    private Double pension;

    @Field("deduction")
    private Double deduction;

    @Field("locationId")
    private Integer locationId;

    @Field("locationName")
    private String locationName;

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

    public Integer getGroupPayrunId() {
        return groupPayrunId;
    }

    public void setGroupPayrunId(Integer groupPayrunId) {
        this.groupPayrunId = groupPayrunId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    public String getPreparerIdName() {
        return preparerIdName;
    }

    public void setPreparerIdName(String preparerIdName) {
        this.preparerIdName = preparerIdName;
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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public Integer getMonthId() {
        return monthId;
    }

    public void setMonthId(Integer monthId) {
        this.monthId = monthId;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getTotalInBase() {
        return totalInBase;
    }

    public void setTotalInBase(Double totalInBase) {
        this.totalInBase = totalInBase;
    }

    public Integer getPayrollBatchId() {
        return payrollBatchId;
    }

    public void setPayrollBatchId(Integer payrollBatchId) {
        this.payrollBatchId = payrollBatchId;
    }

    public String getPayrollBatchName() {
        return payrollBatchName;
    }

    public void setPayrollBatchName(String payrollBatchName) {
        this.payrollBatchName = payrollBatchName;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getForeignCurrencyName() {
        return foreignCurrencyName;
    }

    public void setForeignCurrencyName(String foreignCurrencyName) {
        this.foreignCurrencyName = foreignCurrencyName;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Boolean getDeleted() {
        return deleted != null && deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Double getExpense() {
        return expense;
    }

    public void setExpense(Double expense) {
        this.expense = expense;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public Double getAllowance() {
        return allowance;
    }

    public void setAllowance(Double allowance) {
        this.allowance = allowance;
    }

    public Double getPension() {
        return pension;
    }

    public void setPension(Double pension) {
        this.pension = pension;
    }

    public Double getDeduction() {
        return deduction;
    }

    public void setDeduction(Double deduction) {
        this.deduction = deduction;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
