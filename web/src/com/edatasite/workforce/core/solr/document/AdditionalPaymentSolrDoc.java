package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:23.
 */
@SolrDocument(collection = "additionalPaymentCore")
public class AdditionalPaymentSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("additionalPaymentId")
    @Indexed(name = "additionalPaymentId", type = "pint", required = true)
    private Integer additionalPaymentId;

    @Field("reference")
    private String reference;

    @Field("approverId")
    private Integer approverId;

    @Field("approverName")
    private String approverName;

    @Field("approverIdName")
    @Indexed(name = "approverIdName", type = "string", stored = false)
    private String approverIdName;

    @Field("creatorId")
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

    @Field("statusCode")
    private String statusCode;

    @Field("statusIdName")
    @Indexed(name = "statusIdName", type = "string", stored = false)
    private String statusIdName;

    @Field("type")
    private String type;

    @Field("entityType")
    private String entityType;

    @Field("paymentType")
    private String paymentType;

    @Field("totalAmount")
    private Double totalAmount;

    @Field("payrollGroupId")
    private Integer payrollGroupId;

    @Field("payrollGroupName")
    private String payrollGroupName;

    @Field("processDate")
    private Date processDate;

    @Field("creationDate")
    private Date creationDate;

    @Field("approvedDate")
    private Date approvedDate;

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

    @Field("pdfTemplateId")
    private Integer pdfTemplateId;

    @Field("lastUpdate")
    private Date lastUpdate;

    @Field("paymentCategory")
    private String paymentCategory;

    @Field("deleted")
    private Boolean deleted;

    @Field("payrollDepartmentId")
    private Integer payrollDepartmentId;

    @Field("payrollDepartmentName")
    private String payrollDepartmentName;

    @Field("updaterId")
    private Integer updaterId;


    @Field("updaterIdName")
    @Indexed(name = "updaterIdName", type = "string", stored = false)
    private String updaterIdName;
    @Field("updaterName")
    private String updaterName;

    @Field("categoryLookupName")
    private String categoryLookupName;

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

    public Integer getAdditionalPaymentId() {
        return additionalPaymentId;
    }

    public void setAdditionalPaymentId(Integer additionalPaymentId) {
        this.additionalPaymentId = additionalPaymentId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getPayrollGroupId() {
        return payrollGroupId;
    }

    public void setPayrollGroupId(Integer payrollGroupId) {
        this.payrollGroupId = payrollGroupId;
    }

    public String getPayrollGroupName() {
        return payrollGroupName;
    }

    public void setPayrollGroupName(String payrollGroupName) {
        this.payrollGroupName = payrollGroupName;
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

    public Integer getPdfTemplateId() {
        return pdfTemplateId;
    }

    public void setPdfTemplateId(Integer pdfTemplateId) {
        this.pdfTemplateId = pdfTemplateId;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(String paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public Boolean getDeleted() {
        return deleted != null && deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getPayrollDepartmentId() {
        return payrollDepartmentId;
    }

    public void setPayrollDepartmentId(Integer payrollDepartmentId) {
        this.payrollDepartmentId = payrollDepartmentId;
    }

    public String getPayrollDepartmentName() {
        return payrollDepartmentName;
    }

    public void setPayrollDepartmentName(String payrollDepartmentName) {
        this.payrollDepartmentName = payrollDepartmentName;
    }

    public Integer getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Integer updaterId) {
        this.updaterId = updaterId;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public String getUpdaterIdName() {
        return updaterIdName;
    }

    public void setUpdaterIdName(String updaterIdName) {
        this.updaterIdName = updaterIdName;
    }

    public String getCategoryLookupName() {
        return categoryLookupName;
    }

    public void setCategoryLookupName(String categoryLookupName) {
        this.categoryLookupName = categoryLookupName;
    }
}
