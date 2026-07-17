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
@SolrDocument(collection = "purchaseOrderCore")
public class PurchaseOrderSolrDoc extends BaseSolrDoc {

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

    @Field("clientOwnerId")
    @Indexed(name = "clientOwnerId", type = "pints", stored = false)
    private List<Integer> clientOwnerId = new ArrayList<>();

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

    @Field("statusIdName")
    @Indexed(name = "statusIdName", type = "string", stored = false)
    private String statusIdName;

    @Field("statusSorder")
    private Integer statusSorder;

    @Field("dueAmount")
    private Double dueAmount;

    @Field("totalInvoiceCurrency")
    private Double totalInvoiceCurrency;

    @Field("invoiceNumber")
    private String invoiceNumber;

    @Field("invoiceDate")
    private Date invoiceDate;

    @Field("dueDate")
    private Date dueDate;

    @Field("relatedProjectId")
    private Integer relatedProjectId;

    @Field("relatedProjectName")
    private String relatedProjectName;

    @Field("relatedProjectIdName")
    @Indexed(name = "relatedProjectIdName", type = "string", stored = false)
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
    @Indexed(name = "multiProjectId", type = "pints", stored = false)
    private List<String> multiProjectIdName = new ArrayList<>();

    @Field("multiProjectNumber")
    @Indexed(name = "multiProjectNumber", type = "strings", stored = false)
    private List<String> multiProjectNumber = new ArrayList<>();

    @Field("multiProjectNumberName")
    @Indexed(name = "multiProjectNumberName", type = "strings")
    private List<String> multiProjectNumberName = new ArrayList<>();

    @Field("creatorId")
    private Integer creatorId;

    @Field("creatorName")
    private String creatorName;

    @Field("creatorIdName")
    @Indexed(name = "creatorIdName", type = "string", stored = false)
    private String creatorIdName;

    @Field("managerId")
    private Integer managerId;

    @Field("managerName")
    private String managerName;

    @Field("managerIdName")
    @Indexed(name = "managerIdName", type = "string", stored = false)
    private String managerIdName;

    @Field("quoteNumber")
    private String quoteNumber;

    @Field("totalTaxes")
    private Double totalTaxes;

    @Field("totalInvoiceBase")
    private Double totalInvoiceBase;

    @Field("itemId")
    @Indexed(name = "itemId", type = "pints", stored = false)
    private List<Integer> itemId = new ArrayList<>();

    @Field("currentApproverId")
    private Integer currentApproverId;

    @Field("currentApproverName")
    private String currentApproverName;

    @Field("currentApproverIdName")
    @Indexed(name = "currentApproverIdName", type = "string", stored = false)
    private String currentApproverIdName;

    @Field("opportunityId")
    private Integer opportunityId;

    @Field("opportunityNumber")
    private String opportunityNumber;

    @Field("createdDate")
    private Date createdDate;

    @Field("updatedDate")
    private Date updatedDate;

    @Field("customerId")
    private Integer customerId;

    @Field("relatedProjectCode")
    private String relatedProjectCode;

    @Field("subTotal")
    private Double subTotal;

    @Field("exchargeRate")
    private Double exchargeRate;

    @Field("taxCalculationType")
    private Integer taxCalculationType;

    @Field("reference")
    private String reference;

    @Field("statusCode")
    private String statusCode;

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

    public Double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public Double getTotalInvoiceCurrency() {
        return totalInvoiceCurrency;
    }

    public void setTotalInvoiceCurrency(Double totalInvoiceCurrency) {
        this.totalInvoiceCurrency = totalInvoiceCurrency;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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

    public String getManagerIdName() {
        return managerIdName;
    }

    public void setManagerIdName(String managerIdName) {
        this.managerIdName = managerIdName;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public Double getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(Double totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public Double getTotalInvoiceBase() {
        return totalInvoiceBase;
    }

    public void setTotalInvoiceBase(Double totalInvoiceBase) {
        this.totalInvoiceBase = totalInvoiceBase;
    }

    public List<Integer> getItemId() {
        return itemId;
    }

    public void setItemId(List<Integer> itemId) {
        this.itemId = itemId;
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

    public Integer getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(Integer opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getOpportunityNumber() {
        return opportunityNumber;
    }

    public void setOpportunityNumber(String opportunityNumber) {
        this.opportunityNumber = opportunityNumber;
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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getRelatedProjectCode() {
        return relatedProjectCode;
    }

    public void setRelatedProjectCode(String relatedProjectCode) {
        this.relatedProjectCode = relatedProjectCode;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getExchargeRate() {
        return exchargeRate;
    }

    public void setExchargeRate(Double exchargeRate) {
        this.exchargeRate = exchargeRate;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<Integer> getClientOwnerId() {
        return clientOwnerId;
    }

    public void setClientOwnerId(List<Integer> clientOwnerId) {
        this.clientOwnerId = clientOwnerId;
    }
}
