package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:27.
 */
@SolrDocument(collection = "expenseReportClaims")
public class ExpenseReportClaimsSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("reportId")
    @Indexed(name = "reportId", type = "pint", required = true)
    private Integer reportId;

    @Field("title")
    @Indexed(name = "title", type = "string")
    private String title;

    @Field("startDate")
    private Date startDate;

    @Field("endDate")
    private Date endDate;

    @Field("relatedProjectId")
    @Indexed(name = "relatedProjectId", type = "pint", stored = false)
    private Integer relatedProjectId;

    @Field("relatedProjectName")
    private String relatedProjectName;

    @Field("relatedProjectIdName")
    @Indexed(name = "relatedProjectIdName", type = "string", stored = false)
    private String relatedProjectIdName;

    @Field("relatedProjectNumber")
    private String relatedProjectNumber;

    @Field("relatedProjectNumberName")
    private String relatedProjectNumberName;

    @Field("reporterId")
    private Integer reporterId;

    @Field("reporterName")
    private String reporterName;

    @Field("reporterIdName")
    @Indexed(name = "reporterIdName", type = "string", stored = false)
    private String reporterIdName;

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
    @Indexed(name = "statusId", type = "pint", stored = false)
    private Integer statusId;

    @Field("statusCode")
    private String statusCode;

    @Field("statusName")
    private String statusName;

    @Field("statusIdName")
    @Indexed(name = "statusIdName", type = "string", stored = false)
    private String statusIdName;

    @Field("status2Id")
    @Indexed(name = "status2Id", type = "pint", stored = false)
    private Integer status2Id;

    @Field("status2Code")
    private String status2Code;

    @Field("status2Name")
    private String status2Name;

    @Field("status2IdName")
    @Indexed(name = "status2IdName", type = "string", stored = false)
    private String status2IdName;

    @Field("fixedAssetId")
    private Integer fixedAssetId;

    @Field("fixedAssetName")
    private String fixedAssetName;

    @Field("fixedAssetIdName")
    @Indexed(name = "fixedAssetIdName", type = "string", stored = false)
    private String fixedAssetIdName;

    @Field("orginalAmount")
    private Double orginalAmount;

    @Field("paidAmount")
    private Double paidAmount;

    @Field("dueAmount")
    private Double dueAmount;

    @Field("taxAmount")
    private Double taxAmount;

    @Field("numbering")
    private String numbering;

    @Field("isCompanyExpense")
    private Boolean isCompanyExpense;

    @Field("currencyId")
    private Integer currencyId;

    @Field("currencyName")
    private String currencyName;

    @Field("currencyIdName")
    @Indexed(name = "currencyIdName", type = "string", stored = false)
    private String currencyIdName;

    @Field("multiProjectId")
    @Indexed(name = "multiProjectId", type = "pints", stored = false)
    private List<Integer> multiProjectId = new ArrayList<>();

    @Field("multiProjectName")
    @Indexed(name = "multiProjectName", type = "strings")
    private List<String> multiProjectName = new ArrayList<>();

    @Field("multiProjectIdName")
    @Indexed(name = "multiProjectId", type = "strings", stored = false)
    private List<String> multiProjectIdName = new ArrayList<>();

    @Field("multiProjectNumber")
    @Indexed(name = "multiProjectNumber", type = "strings", stored = false)
    private List<String> multiProjectNumber = new ArrayList<>();

    @Field("multiProjectNumberName")
    @Indexed(name = "multiProjectNumberName", type = "strings")
    private List<String> multiProjectNumberName = new ArrayList<>();

    @Field("supplierId")
    private Integer supplierId;

    @Field("supplierName")
    private String supplierName;

    @Field("supplierIdName")
    @Indexed(name = "supplierIdName", type = "string", stored = false)
    private String supplierIdName;

    @Field("supplierOwnerId")
    @Indexed(name = "supplierOwnerId", type = "pints", stored = false)
    private List<Integer> supplierOwnerId = new ArrayList<>();

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

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
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

    public Integer getRelatedProjectId() {
        return relatedProjectId;
    }

    public void setRelatedProjectId(Integer relatedProjectId) {
        this.relatedProjectId = relatedProjectId;
    }

    public String getRelatedProjectName() {
        return relatedProjectName;
    }

    public void setRelatedProjectName(String relatedProjectName) {
        this.relatedProjectName = relatedProjectName;
    }

    public String getRelatedProjectIdName() {
        return relatedProjectIdName;
    }

    public void setRelatedProjectIdName(String relatedProjectIdName) {
        this.relatedProjectIdName = relatedProjectIdName;
    }

    public String getRelatedProjectNumber() {
        return relatedProjectNumber;
    }

    public void setRelatedProjectNumber(String relatedProjectNumber) {
        this.relatedProjectNumber = relatedProjectNumber;
    }

    public String getRelatedProjectNumberName() {
        return relatedProjectNumberName;
    }

    public void setRelatedProjectNumberName(String relatedProjectNumberName) {
        this.relatedProjectNumberName = relatedProjectNumberName;
    }

    public Integer getReporterId() {
        return reporterId;
    }

    public void setReporterId(Integer reporterId) {
        this.reporterId = reporterId;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporterIdName() {
        return reporterIdName;
    }

    public void setReporterIdName(String reporterIdName) {
        this.reporterIdName = reporterIdName;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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

    public String getStatus2Code() {
        return status2Code;
    }

    public void setStatus2Code(String status2Code) {
        this.status2Code = status2Code;
    }

    public String getStatus2Name() {
        return status2Name;
    }

    public void setStatus2Name(String status2Name) {
        this.status2Name = status2Name;
    }

    public String getStatus2IdName() {
        return status2IdName;
    }

    public void setStatus2IdName(String status2IdName) {
        this.status2IdName = status2IdName;
    }

    public Integer getFixedAssetId() {
        return fixedAssetId;
    }

    public void setFixedAssetId(Integer fixedAssetId) {
        this.fixedAssetId = fixedAssetId;
    }

    public String getFixedAssetName() {
        return fixedAssetName;
    }

    public void setFixedAssetName(String fixedAssetName) {
        this.fixedAssetName = fixedAssetName;
    }

    public String getFixedAssetIdName() {
        return fixedAssetIdName;
    }

    public void setFixedAssetIdName(String fixedAssetIdName) {
        this.fixedAssetIdName = fixedAssetIdName;
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
        return isCompanyExpense != null && isCompanyExpense;
    }

    public void setCompanyExpense(Boolean companyExpense) {
        isCompanyExpense = companyExpense;
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

    public List<Integer> getMultiProjectId() {
        return multiProjectId;
    }

    public void setMultiProjectId(List<Integer> multiProjectId) {
        this.multiProjectId = multiProjectId;
    }

    public List<String> getMultiProjectName() {
        return multiProjectName;
    }

    public void setMultiProjectName(List<String> multiProjectName) {
        this.multiProjectName = multiProjectName;
    }

    public List<String> getMultiProjectIdName() {
        return multiProjectIdName;
    }

    public void setMultiProjectIdName(List<String> multiProjectIdName) {
        this.multiProjectIdName = multiProjectIdName;
    }

    public List<String> getMultiProjectNumber() {
        return multiProjectNumber;
    }

    public void setMultiProjectNumber(List<String> multiProjectNumber) {
        this.multiProjectNumber = multiProjectNumber;
    }

    public List<String> getMultiProjectNumberName() {
        return multiProjectNumberName;
    }

    public void setMultiProjectNumberName(List<String> multiProjectNumberName) {
        this.multiProjectNumberName = multiProjectNumberName;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierIdName() {
        return supplierIdName;
    }

    public void setSupplierIdName(String supplierIdName) {
        this.supplierIdName = supplierIdName;
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

    public List<Integer> getSupplierOwnerId() {
        return supplierOwnerId;
    }

    public void setSupplierOwnerId(List<Integer> supplierOwnerId) {
        this.supplierOwnerId = supplierOwnerId;
    }
}
