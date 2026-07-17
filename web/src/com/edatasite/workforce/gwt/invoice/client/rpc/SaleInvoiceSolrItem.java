package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaleInvoiceSolrItem implements IsSerializable {

    private Integer objectID;
    private String invoiceNumber;
    private Date invoiceDate;
    private SelectItem client;
    private SelectItem customClient;
    private SelectItem clientContact;
    private String clientContactEmail;
    private List<Integer> customerOwnerIds = new ArrayList<>();
    private List<Integer> invoiceFromQuoteCreatorIds = new ArrayList<>();
    private String poNumber;
    private SelectItem currency;
    private ReferenceItem status;
    private BigDecimal paidAmount;
    private BigDecimal dueAmount;
    private BigDecimal totalTaxes;
    private BigDecimal exchangeRate;
    private BigDecimal totalInvoiceCurrency;
    private BigDecimal totalInvoiceBase;
    private SelectItem shippingMethod;
    private Boolean isCreditNode;
    private Date dueDate;
    private SelectItem relatedProject;
    private String relatedProjectStatusCode;
    private List<SelectItem> multiProjects = new ArrayList<>();
    private SelectItem creator;
    private String reference;
    private String quoteNumber;
    private Boolean inTarget;
    private Boolean hasPayment;
    private SelectItem currentApprover;
    private List<Integer> itemsIds = new ArrayList<>();
    private List<Integer> warehouseIds = new ArrayList<>();
    private Integer opportunity;
    private String clientVat;
    private String clientTrn;
    private String introduction;
    private Double quotePercent;
    private Boolean projectBased;
    private BigDecimal subTotal;
    private Integer pdfTemplateId;
    private Integer taxCalculationType;
    private Date createdDate;
    private Date updatedDate;
    private List<Integer> productIdsFromInvoice = new ArrayList<>();
    private List<String> productNames = new ArrayList<>();
    private String zatcaStatus;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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

    public SelectItem getClient() {
        return client;
    }

    public void setClient(SelectItem client) {
        this.client = client;
    }

    public SelectItem getCustomClient() {
        return customClient;
    }

    public void setCustomClient(SelectItem customClient) {
        this.customClient = customClient;
    }

    public SelectItem getClientContact() {
        return clientContact;
    }

    public void setClientContact(SelectItem clientContact) {
        this.clientContact = clientContact;
    }

    public String getClientContactEmail() {
        return clientContactEmail;
    }

    public void setClientContactEmail(String clientContactEmail) {
        this.clientContactEmail = clientContactEmail;
    }

    public List<Integer> getCustomerOwnerIds() {
        return customerOwnerIds;
    }

    public void setCustomerOwnerIds(List<Integer> customerOwnerIds) {
        this.customerOwnerIds = customerOwnerIds;
    }

    public List<Integer> getInvoiceFromQuoteCreatorIds() {
        return invoiceFromQuoteCreatorIds;
    }

    public void setInvoiceFromQuoteCreatorIds(List<Integer> invoiceFromQuoteCreatorIds) {
        this.invoiceFromQuoteCreatorIds = invoiceFromQuoteCreatorIds;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public ReferenceItem getStatus() {
        return status;
    }

    public void setStatus(ReferenceItem status) {
        this.status = status;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    public BigDecimal getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(BigDecimal totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getTotalInvoiceCurrency() {
        return totalInvoiceCurrency;
    }

    public void setTotalInvoiceCurrency(BigDecimal totalInvoiceCurrency) {
        this.totalInvoiceCurrency = totalInvoiceCurrency;
    }

    public BigDecimal getTotalInvoiceBase() {
        return totalInvoiceBase;
    }

    public void setTotalInvoiceBase(BigDecimal totalInvoiceBase) {
        this.totalInvoiceBase = totalInvoiceBase;
    }

    public SelectItem getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(SelectItem shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public Boolean getCreditNode() {
        return isCreditNode;
    }

    public void setCreditNode(Boolean creditNode) {
        isCreditNode = creditNode;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public SelectItem getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(SelectItem relatedProject) {
        this.relatedProject = relatedProject;
    }

    public String getRelatedProjectStatusCode() {
        return relatedProjectStatusCode;
    }

    public void setRelatedProjectStatusCode(String relatedProjectStatusCode) {
        this.relatedProjectStatusCode = relatedProjectStatusCode;
    }

    public List<SelectItem> getMultiProjects() {
        return multiProjects;
    }

    public void setMultiProjects(List<SelectItem> multiProjects) {
        this.multiProjects = multiProjects;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public Boolean getInTarget() {
        return inTarget;
    }

    public void setInTarget(Boolean inTarget) {
        this.inTarget = inTarget;
    }

    public Boolean getHasPayment() {
        return hasPayment;
    }

    public void setHasPayment(Boolean hasPayment) {
        this.hasPayment = hasPayment;
    }

    public SelectItem getCurrentApprover() {
        return currentApprover;
    }

    public void setCurrentApprover(SelectItem currentApprover) {
        this.currentApprover = currentApprover;
    }

    public List<Integer> getItemsIds() {
        return itemsIds;
    }

    public void setItemsIds(List<Integer> itemsIds) {
        this.itemsIds = itemsIds;
    }

    public List<Integer> getWarehouseIds() {
        return warehouseIds;
    }

    public void setWarehouseIds(List<Integer> warehouseIds) {
        this.warehouseIds = warehouseIds;
    }

    public Integer getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(Integer opportunity) {
        this.opportunity = opportunity;
    }

    public String getClientVat() {
        return clientVat;
    }

    public void setClientVat(String clientVat) {
        this.clientVat = clientVat;
    }

    public String getClientTrn() {
        return clientTrn;
    }

    public void setClientTrn(String clientTrn) {
        this.clientTrn = clientTrn;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Double getQuotePercent() {
        return quotePercent;
    }

    public void setQuotePercent(Double quotePercent) {
        this.quotePercent = quotePercent;
    }

    public Boolean getProjectBased() {
        return projectBased;
    }

    public void setProjectBased(Boolean projectBased) {
        this.projectBased = projectBased;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Integer getPdfTemplateId() {
        return pdfTemplateId;
    }

    public void setPdfTemplateId(Integer pdfTemplateId) {
        this.pdfTemplateId = pdfTemplateId;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
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

    public List<Integer> getProductIdsFromInvoice() {
        return productIdsFromInvoice;
    }

    public void setProductIdsFromInvoice(List<Integer> productIdsFromInvoice) {
        this.productIdsFromInvoice = productIdsFromInvoice;
    }

    public List<String> getProductNames() {
        return productNames;
    }

    public void setProductNames(List<String> productNames) {
        this.productNames = productNames;
    }

    public String getZatcaStatus() {
        return zatcaStatus;
    }

    public void setZatcaStatus(String zatcaStatus) {
        this.zatcaStatus = zatcaStatus;
    }
}
