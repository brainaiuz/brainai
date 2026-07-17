package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PurchaseOrderSolrItem implements IsSerializable {

    private Integer orderId;
    private SelectItem client;
    private List<Integer> clientOwnerIds = new ArrayList<>();
    private SelectItem currency;
    private ReferenceItem status;
    private BigDecimal dueAmount;
    private BigDecimal totalInvoiceCurrency;
    private String invoiceNumber;
    private Date invoiceDate;
    private Date dueDate;
    private SelectItem relatedProject;
    private List<SelectItem> multiProject = new ArrayList<>();
    private SelectItem creator;
    private SelectItem manager;
    private String quoteNumber;
    private BigDecimal totalTaxes;
    private BigDecimal totalInvoiceBase;
    private List<Integer> itemIds = new ArrayList<>();
    private SelectItem currentApprover;
    private SelectItem opportunity;
    private Date createdDate;
    private Date updatedDate;
    private Integer customerId;
    private BigDecimal subTotal;
    private BigDecimal exchangeRate;
    private Integer taxCalculationType;
    private String reference;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public SelectItem getClient() {
        return client;
    }

    public void setClient(SelectItem client) {
        this.client = client;
    }

    public List<Integer> getClientOwnerIds() {
        return clientOwnerIds;
    }

    public void setClientOwnerIds(List<Integer> clientOwnerIds) {
        this.clientOwnerIds = clientOwnerIds;
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

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    public BigDecimal getTotalInvoiceCurrency() {
        return totalInvoiceCurrency;
    }

    public void setTotalInvoiceCurrency(BigDecimal totalInvoiceCurrency) {
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

    public SelectItem getManager() {
        return manager;
    }

    public void setManager(SelectItem manager) {
        this.manager = manager;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public BigDecimal getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(BigDecimal totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public BigDecimal getTotalInvoiceBase() {
        return totalInvoiceBase;
    }

    public void setTotalInvoiceBase(BigDecimal totalInvoiceBase) {
        this.totalInvoiceBase = totalInvoiceBase;
    }

    public List<Integer> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Integer> itemIds) {
        this.itemIds = itemIds;
    }

    public SelectItem getCurrentApprover() {
        return currentApprover;
    }

    public void setCurrentApprover(SelectItem currentApprover) {
        this.currentApprover = currentApprover;
    }

    public SelectItem getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(SelectItem opportunity) {
        this.opportunity = opportunity;
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

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
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
}
