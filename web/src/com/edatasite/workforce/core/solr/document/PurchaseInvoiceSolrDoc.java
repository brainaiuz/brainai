package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:30.
 */
@SolrDocument(collection = "purchaseInvoiceCore")
public class PurchaseInvoiceSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("purchaseInvoiceId")
    @Indexed(name = "purchaseInvoiceId", type = "pint", required = true)
    private Integer purchaseInvoiceId;

    @Field("purchaseInvoiceNumber")
    private String purchaseInvoiceNumber;

    @Field("invoiceDate")
    private Date invoiceDate;

    @Field("dueDate")
    private Date dueDate;

    @Field("relatedProjectId")
    private Integer relatedProjectId;

    @Field("relatedProjectName")
    private String relatedProjectName;

    @Field("relatedProjectNumber")
    private String relatedProjectNumber;

    @Field("relatedProjectIdName")
    private String relatedProjectIdName;

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

    @Field("dueAmount")
    private Double dueAmount;

    @Field("paidAmount")
    private Double paidAmount;

    @Field("statusId")
    private Integer statusId;

    @Field("statusName")
    private String statusName;

    @Field("statusIdName")
    @Indexed(name = "statusIdName", type = "string", stored = false)
    private String statusIdName;

    @Field("statusCode")
    private String statusCode;

    @Field("statusSorder")
    private Integer statusSorder;

    @Field("isCreditNote")
    private Boolean isCreditNote;

    @Field("hasPayment")
    private Boolean hasPayment;

    @Field("totalInInvoiceCurrency")
    private Double totalInInvoiceCurrency;

    @Field("creatorId")
    private Integer creatorId;

    @Field("creatorName")
    private String creatorName;

    @Field("creatorIdName")
    @Indexed(name = "creatorIdName", type = "string", stored = false)
    private String creatorIdName;

    @Field("totalInvoiceBase")
    private Double totalInvoiceBase;

    @Field("poNumber")
    private String poNumber;

    @Field("reference")
    private String reference;

    @Field("itemId")
    @Indexed(name = "itemId", type = "pints", stored = false)
    private List<Integer> itemId = new ArrayList<>();

    @Field("warehouseId")
    @Indexed(name = "warehouseId", type = "pints")
    private List<Integer> warehouseId = new ArrayList<>();

    @Field("currentApproverId")
    private Integer currentApproverId;

    @Field("currentApproverName")
    private String currentApproverName;

    @Field("currentApproverIdName")
    @Indexed(name = "currentApproverIdName", type = "string", stored = false)
    private String currentApproverIdName;

    @Field("purchaseInvoiceTotalTaxes")
    private Double purchaseInvoiceTotalTaxes;

    @Field("purchaseInvoiceExchangeRate")
    private Double purchaseInvoiceExchangeRate;

    @Field("purchaseInvoiceTaxCalculationType")
    private Integer purchaseInvoiceTaxCalculationType;

    @Field("purchaseInvoiceRelatedProjectStatusCode")
    private String purchaseInvoiceRelatedProjectStatusCode;

    @Field("purchaseInvoiceSupplierVatNumber")
    private String purchaseInvoiceSupplierVatNumber;

    @Field("purchaseInvoiceSupplierTrn")
    private String purchaseInvoiceSupplierTrn;

    @Field("invoiceType")
    private String invoiceType;

    @Field("createrFullName")
    private String createrFullName;

    @Field("opportunityId")
    private Integer opportunityId;

    @Field("opportunityNumber")
    private String opportunityNumber;

    @Field("createdDate")
    private Date createdDate;

    @Field("updatedDate")
    private Date updatedDate;

    @Field("zatcaStatus")
    private String zatcaStatus;

    @Field("isConverted")
    private Boolean isConverted;

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

    public Integer getPurchaseInvoiceId() {
        return purchaseInvoiceId;
    }

    public void setPurchaseInvoiceId(Integer purchaseInvoiceId) {
        this.purchaseInvoiceId = purchaseInvoiceId;
    }

    public String getPurchaseInvoiceNumber() {
        return purchaseInvoiceNumber;
    }

    public void setPurchaseInvoiceNumber(String purchaseInvoiceNumber) {
        this.purchaseInvoiceNumber = purchaseInvoiceNumber;
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

    public Double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getStatusSorder() {
        return statusSorder;
    }

    public void setStatusSorder(Integer statusSorder) {
        this.statusSorder = statusSorder;
    }

    public Boolean getCreditNote() {
        return isCreditNote != null && isCreditNote;
    }

    public void setCreditNote(Boolean creditNote) {
        isCreditNote = creditNote;
    }

    public Boolean getHasPayment() {
        return hasPayment != null && hasPayment;
    }

    public void setHasPayment(Boolean hasPayment) {
        this.hasPayment = hasPayment;
    }

    public Double getTotalInInvoiceCurrency() {
        return totalInInvoiceCurrency;
    }

    public void setTotalInInvoiceCurrency(Double totalInInvoiceCurrency) {
        this.totalInInvoiceCurrency = totalInInvoiceCurrency;
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

    public Double getTotalInvoiceBase() {
        return totalInvoiceBase;
    }

    public void setTotalInvoiceBase(Double totalInvoiceBase) {
        this.totalInvoiceBase = totalInvoiceBase;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<Integer> getItemId() {
        return itemId;
    }

    public void setItemId(List<Integer> itemId) {
        this.itemId = itemId;
    }

    public List<Integer> getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(List<Integer> warehouseId) {
        this.warehouseId = warehouseId;
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

    public Double getPurchaseInvoiceTotalTaxes() {
        return purchaseInvoiceTotalTaxes;
    }

    public void setPurchaseInvoiceTotalTaxes(Double purchaseInvoiceTotalTaxes) {
        this.purchaseInvoiceTotalTaxes = purchaseInvoiceTotalTaxes;
    }

    public Double getPurchaseInvoiceExchangeRate() {
        return purchaseInvoiceExchangeRate;
    }

    public void setPurchaseInvoiceExchangeRate(Double purchaseInvoiceExchangeRate) {
        this.purchaseInvoiceExchangeRate = purchaseInvoiceExchangeRate;
    }

    public Integer getPurchaseInvoiceTaxCalculationType() {
        return purchaseInvoiceTaxCalculationType;
    }

    public void setPurchaseInvoiceTaxCalculationType(Integer purchaseInvoiceTaxCalculationType) {
        this.purchaseInvoiceTaxCalculationType = purchaseInvoiceTaxCalculationType;
    }

    public String getPurchaseInvoiceRelatedProjectStatusCode() {
        return purchaseInvoiceRelatedProjectStatusCode;
    }

    public void setPurchaseInvoiceRelatedProjectStatusCode(String purchaseInvoiceRelatedProjectStatusCode) {
        this.purchaseInvoiceRelatedProjectStatusCode = purchaseInvoiceRelatedProjectStatusCode;
    }

    public String getPurchaseInvoiceSupplierVatNumber() {
        return purchaseInvoiceSupplierVatNumber;
    }

    public void setPurchaseInvoiceSupplierVatNumber(String purchaseInvoiceSupplierVatNumber) {
        this.purchaseInvoiceSupplierVatNumber = purchaseInvoiceSupplierVatNumber;
    }

    public String getPurchaseInvoiceSupplierTrn() {
        return purchaseInvoiceSupplierTrn;
    }

    public void setPurchaseInvoiceSupplierTrn(String purchaseInvoiceSupplierTrn) {
        this.purchaseInvoiceSupplierTrn = purchaseInvoiceSupplierTrn;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getCreaterFullName() {
        return createrFullName;
    }

    public void setCreaterFullName(String createrFullName) {
        this.createrFullName = createrFullName;
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

    public String getRelatedProjectNumber() {
        return relatedProjectNumber;
    }

    public void setRelatedProjectNumber(String relatedProjectNumber) {
        this.relatedProjectNumber = relatedProjectNumber;
    }

    public List<Integer> getClientOwnerId() {
        return clientOwnerId;
    }

    public void setClientOwnerId(List<Integer> clientOwnerId) {
        this.clientOwnerId = clientOwnerId;
    }

    public String getZatcaStatus() {
        return zatcaStatus;
    }

    public void setZatcaStatus(String zatcaStatus) {
        this.zatcaStatus = zatcaStatus;
    }

    public Boolean isConverted() {
        return isConverted;
    }

    public void setConverted(Boolean converted) {
        isConverted = converted;
    }
}
