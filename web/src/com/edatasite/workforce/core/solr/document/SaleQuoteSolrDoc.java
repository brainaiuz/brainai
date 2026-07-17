package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:31.
 */
@SolrDocument(collection = "saleQuoteCore")
public class SaleQuoteSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("saleInvoiceId")
    @Indexed(name = "saleInvoiceId", type = "pint", required = true)
    private Integer saleInvoiceId;

    @Field("clientId")
    private Integer clientId;

    @Field("clientName")
    private String clientName;

    @Field("clientIdName")
    @Indexed(name = "clientIdName", type = "string", stored = false)
    private String clientIdName;

    @Field("clientContactId")
    @Indexed(name = "clientContactId", type = "pint", stored = false)
    private Integer clientContactId;

    @Field("clientContactEmail")
    private String clientContactEmail;

    @Field("customerOwnerId")
    @Indexed(name = "customerOwnerId", type = "pints", stored = false)
    private List<Integer> customerOwnerId = new ArrayList<>();


    @Field("clientContactIdEmail")
    @Indexed(name = "clientContactIdEmail", type = "string", stored = false)
    private String clientContactIdEmail;

    @Field("currencyId")
    private Integer currencyId;

    @Field("currencyName")
    private String currencyName;

    @Field("currencyIdName")
    @Indexed(name = "currencyIdName", type = "string", stored = false)
    private String currencyIdName;

    @Field("statusId")
    private Integer statusId;

    @Field("statusName")
    private String statusName;

    @Field("statusCode")
    private String statusCode;

    @Field("statusIdName")
    private String statusIdName;

    @Field("statusSorder")
    @Indexed(name = "statusSorder", type = "pint", stored = false)
    private Integer statusSorder;

    @Field("isSalesOrder")
    private Boolean isSalesOrder;

    @Field("project")
    private String project;

    @Field("projectId")
    private Integer projectId;

    @Field("paidAmount")
    private Double paidAmount;

    @Field("dueAmount")
    private Double dueAmount;

    @Field("totalTaxes")
    private Double totalTaxes;

    @Field("totalInvoiceCurrency")
    private Double totalInvoiceCurrency;

    @Field("exchargeRate")
    private Double exchargeRate;

    @Field("subTotal")
    private Double subTotal;

    @Field("netAmountTotal")
    private Double netAmountTotal;

    @Field("totalInvoiceBase")
    private Double totalInvoiceBase;

    @Field("shippingMethodId")
    @Indexed(name = "shippingMethodId", type = "pint", stored = false)
    private Integer shppingmethodId;

    @Field("shippingMethodName")
    private String shppingmethodName;

    @Field("shippingMethodIdName")
    @Indexed(name = "shippingMethodIdName", type = "string", stored = false)
    private String shppingmethodIdName;

    @Field("invoiceNumber")
    private String invoiceNumber;

    @Field("opportunityNumber")
    private String opportunityNumber;

    @Field("isCreditnode")
    private Boolean isCreditnode;

    @Field("isProgressInvoicing")
    private Boolean isProgressInvoicing;

    @Field("invoiceDate")
    private Date invoiceDate;

    @Field("dueDate")
    private Date dueDate;

    @Field("relatedProjectId")
    private Integer relatedProjectId;

    @Field("relatedProjectName")
    private String relatedProjectName;

    @Field("relatedProjectCode")
    private String relatedProjectCode;

    @Field("relatedProjectIdName")
    private String relatedProjectIdName;

    @Field("relatedProjectNumber")
    private String relatedProjectNumber;

    @Field("multiProjectId")
    @Indexed(name = "multiProjectId", type = "pints", stored = false)
    private List<Integer> multiProjectId = new ArrayList<>();

    @Field("multiProjectName")
    @Indexed(name = "multiProjectName", type = "strings")
    private List<String> multiProjectName = new ArrayList<>();

    @Field("multiProjectIdName")
    @Indexed(name = "multiProjectIdName", type = "strings", stored = false)
    private List<String> multiProjectIdName = new ArrayList<>();

    @Field("multiProjectNumber")
    @Indexed(name = "multiProjectNumber", type = "strings", stored = false)
    private List<String> multiProjectNumber = new ArrayList<>();

    @Field("multiProjectNumberName")
    @Indexed(name = "multiProjectNumberName", type = "strings")
    private List<String> multiProjectNumberName = new ArrayList<>();

    @Field("opportunityId")
    private Integer opportunityId;

    @Field("creatorId")
    private Integer creatorId;

    @Field("creatorName")
    private String creatorName;

    @Field("creatorIdName")
    @Indexed(name = "creatorIdName", type = "string", stored = false)
    private String creatorIdName;

    @Field("picklistId")
    private Integer picklistId;
    @Field("projectManagerId")
    @Indexed(name = "projectManagerId", type = "pints", stored = false)
    private List<Integer> projectidsFromEmployeeId = new ArrayList<>();
    @Field("poNumber")
    private String poNumber;

    @Field("managerId")
    private Integer managerId;

    @Field("managerName")
    private String managerName;

    @Field("introduction")
    private String introduction;

    @Field("reference")
    private String reference;


    @Field("currentApproverName")
    private String currentApproverName;

    @Field("currentApproverIdName")
    @Indexed(name = "currentApproverIdName", type = "string", stored = false)
    private String currentApproverIdName;

    @Field("itemId")
    @Indexed(name = "itemId", type = "pints", stored = false)
    private List<Integer> itemId = new ArrayList<>();

    @Field("createdDate")
    private Date createdDate;

    @Field("updatedDate")
    private Date updatedDate;
    @Field("currentApproverId")
    private Integer currentApproverId;

    @Field("taxCalculationType")
    private Integer taxCalculationType;

    @Field("pdfTemplateId")
    private Integer pdfTemplateId;

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

    public Integer getSaleInvoiceId() {
        return saleInvoiceId;
    }

    public void setSaleInvoiceId(Integer saleInvoiceId) {
        this.saleInvoiceId = saleInvoiceId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientIdName() {
        return clientIdName;
    }

    public void setClientIdName(String clientIdName) {
        this.clientIdName = clientIdName;
    }

    public Integer getClientContactId() {
        return clientContactId;
    }

    public void setClientContactId(Integer clientContactId) {
        this.clientContactId = clientContactId;
    }

    public String getClientContactEmail() {
        return clientContactEmail;
    }

    public void setClientContactEmail(String clientContactEmail) {
        this.clientContactEmail = clientContactEmail;
    }

    public String getClientContactIdEmail() {
        return clientContactIdEmail;
    }

    public void setClientContactIdEmail(String clientContactIdEmail) {
        this.clientContactIdEmail = clientContactIdEmail;
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

    public Integer getStatusSorder() {
        return statusSorder;
    }

    public void setStatusSorder(Integer statusSorder) {
        this.statusSorder = statusSorder;
    }

    public Boolean getSalesOrder() {
        return isSalesOrder != null && isSalesOrder;
    }

    public void setSalesOrder(Boolean salesOrder) {
        isSalesOrder = salesOrder;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
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

    public Double getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(Double totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public Double getTotalInvoiceCurrency() {
        return totalInvoiceCurrency;
    }

    public void setTotalInvoiceCurrency(Double totalInvoiceCurrency) {
        this.totalInvoiceCurrency = totalInvoiceCurrency;
    }

    public Double getExchargeRate() {
        return exchargeRate;
    }

    public void setExchargeRate(Double exchargeRate) {
        this.exchargeRate = exchargeRate;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getNetAmountTotal() {
        return netAmountTotal;
    }

    public void setNetAmountTotal(Double netAmountTotal) {
        this.netAmountTotal = netAmountTotal;
    }

    public Double getTotalInvoiceBase() {
        return totalInvoiceBase;
    }

    public void setTotalInvoiceBase(Double totalInvoiceBase) {
        this.totalInvoiceBase = totalInvoiceBase;
    }

    public Integer getShppingmethodId() {
        return shppingmethodId;
    }

    public void setShppingmethodId(Integer shppingmethodId) {
        this.shppingmethodId = shppingmethodId;
    }

    public String getShppingmethodName() {
        return shppingmethodName;
    }

    public void setShppingmethodName(String shppingmethodName) {
        this.shppingmethodName = shppingmethodName;
    }

    public String getShppingmethodIdName() {
        return shppingmethodIdName;
    }

    public void setShppingmethodIdName(String shppingmethodIdName) {
        this.shppingmethodIdName = shppingmethodIdName;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getOpportunityNumber() {
        return opportunityNumber;
    }

    public void setOpportunityNumber(String opportunityNumber) {
        this.opportunityNumber = opportunityNumber;
    }

    public Boolean getCreditnode() {
        return isCreditnode != null && isCreditnode;
    }

    public void setCreditnode(Boolean creditnode) {
        isCreditnode = creditnode;
    }

    public Boolean getProgressInvoicing() {
        return isProgressInvoicing != null && isProgressInvoicing;
    }

    public void setProgressInvoicing(Boolean progressInvoicing) {
        isProgressInvoicing = progressInvoicing;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
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

    public String getRelatedProjectCode() {
        return relatedProjectCode;
    }

    public void setRelatedProjectCode(String relatedProjectCode) {
        this.relatedProjectCode = relatedProjectCode;
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

    public Integer getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(Integer opportunityId) {
        this.opportunityId = opportunityId;
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

    public Integer getPicklistId() {
        return picklistId;
    }

    public void setPicklistId(Integer picklistId) {
        this.picklistId = picklistId;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public List<Integer> getItemId() {
        return itemId;
    }

    public void setItemId(List<Integer> itemId) {
        this.itemId = itemId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public Integer getPdfTemplateId() {
        return pdfTemplateId;
    }

    public void setPdfTemplateId(Integer pdfTemplateId) {
        this.pdfTemplateId = pdfTemplateId;
    }

    public List<Integer> getCustomerOwnerId() {
        return customerOwnerId;
    }

    public void setCustomerOwnerId(List<Integer> customerOwnerId) {
        this.customerOwnerId = customerOwnerId;
    }

    public List<Integer> getProjectidsFromEmployeeId() {
        return projectidsFromEmployeeId;
    }

    public void setProjectidsFromEmployeeId(List<Integer> projectidsFromEmployeeId) {
        this.projectidsFromEmployeeId = projectidsFromEmployeeId;
    }
}
