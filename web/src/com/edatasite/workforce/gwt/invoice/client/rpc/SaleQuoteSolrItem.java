package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaleQuoteSolrItem implements IsSerializable {

    private Integer objectID;
    private SelectItem client;
    private SelectItem clientContact;
    private List<Integer> customerOwnerIds = new ArrayList<>();
    private SelectItem currency;
    private ReferenceItem status;
    private Boolean salesOrder;
    private SelectItem project;
    private BigDecimal paidAmount;
    private BigDecimal dueAmount;
    private BigDecimal totalTaxes;
    private BigDecimal totalInvoiceCurrency;
    private BigDecimal exchangeRate;
    private BigDecimal subTotal;
    private BigDecimal netAmountTotal;
    private BigDecimal totalInvoiceBase;
    private SelectItem shippingMethod;
    private String invoiceNumber;
    private SelectItem opportunity;
    private Boolean isCreditNode;
    private Boolean progressInvoicing;
    private Date invoiceDate;
    private Date dueDate;
    private SelectItem relatedProject;
    private List<SelectItem> multiProject = new ArrayList<>();
    private SelectItem creator;
    private String creatorName;
    private String creatorIdName;
    private List<Integer> projectidsFromEmployeeId = new ArrayList<>();
    private String poNumber;
    private SelectItem manager;
    private String introduction;
    private String reference;
    private Date updatedDate;
    private SelectItem currentApprover;
    private List<Integer> itemIds = new ArrayList<>();
    private Date createdDate;
    private Integer taxCalculationType;
    private Integer pdfTemplateId;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public SelectItem getClient() {
        return client;
    }

    public void setClient(SelectItem client) {
        this.client = client;
    }

    public SelectItem getClientContact() {
        return clientContact;
    }

    public void setClientContact(SelectItem clientContact) {
        this.clientContact = clientContact;
    }

    public List<Integer> getCustomerOwnerIds() {
        return customerOwnerIds;
    }

    public void setCustomerOwnerIds(List<Integer> customerOwnerIds) {
        this.customerOwnerIds = customerOwnerIds;
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

    public Boolean isSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(Boolean salesOrder) {
        this.salesOrder = salesOrder;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
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

    public BigDecimal getTotalInvoiceCurrency() {
        return totalInvoiceCurrency;
    }

    public void setTotalInvoiceCurrency(BigDecimal totalInvoiceCurrency) {
        this.totalInvoiceCurrency = totalInvoiceCurrency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getNetAmountTotal() {
        return netAmountTotal;
    }

    public void setNetAmountTotal(BigDecimal netAmountTotal) {
        this.netAmountTotal = netAmountTotal;
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public SelectItem getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(SelectItem opportunity) {
        this.opportunity = opportunity;
    }

    public Boolean getCreditNode() {
        return isCreditNode;
    }

    public void setCreditNode(Boolean creditNode) {
        isCreditNode = creditNode;
    }

    public Boolean isProgressInvoicing() {
        return progressInvoicing;
    }

    public void setProgressInvoicing(Boolean progressInvoicing) {
        this.progressInvoicing = progressInvoicing;
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

    public SelectItem getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(SelectItem relatedProject) {
        this.relatedProject = relatedProject;
    }

    public List<SelectItem> getMultiProject() {
        return multiProject;
    }

    public void setMultiProject(List<SelectItem> multiProject) {
        this.multiProject = multiProject;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
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

    public List<Integer> getProjectidsFromEmployeeId() {
        return projectidsFromEmployeeId;
    }

    public void setProjectidsFromEmployeeId(List<Integer> projectidsFromEmployeeId) {
        this.projectidsFromEmployeeId = projectidsFromEmployeeId;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public SelectItem getManager() {
        return manager;
    }

    public void setManager(SelectItem manager) {
        this.manager = manager;
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

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public SelectItem getCurrentApprover() {
        return currentApprover;
    }

    public void setCurrentApprover(SelectItem currentApprover) {
        this.currentApprover = currentApprover;
    }

    public List<Integer> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Integer> itemIds) {
        this.itemIds = itemIds;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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
}
