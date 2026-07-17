package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RentalOrderDto {

    private Integer objectID;
    private String number;
    private SelectItem status;
    private String statusCode;
    private NumberData numberData;
    private Date expiration;
    private SelectItem customer;
    private SelectItem paymentTerms;
    private Integer taxCalculationType;
    private Date createdDate;
    private Date updatedDate;
    private ArrayList<RentalOrderItem> rentalOrderItems;
    private ColumnConfigs[] itemColumns;
    private SelectItem[] templates;
    private BigDecimal taxAmount;
    private BigDecimal subTotal;
    private BigDecimal total;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private Double supplierCustomerBalance;
    private SelectItem invoiceItem;
    private TreeSelectItem[] productCategories;
    private SelectItem[] productBrands;
    private SelectItem creator;
    private SelectItem approver;
    private boolean approveProcessEnabled;
    private ArrayList<RelationItem> relationItems;
    private List<? extends CustomFieldRequest> customFields;

    public Integer getObjectID() {
        return this.objectID;
    }

    public void setObjectID(final Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public SelectItem getStatus() {
        return this.status;
    }

    public void setStatus(final SelectItem status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public NumberData getNumberData() {
        return this.numberData;
    }

    public void setNumberData(final NumberData numberData) {
        this.numberData = numberData;
    }

    public Date getExpiration() {
        return this.expiration;
    }

    public void setExpiration(final Date expiration) {
        this.expiration = expiration;
    }

    public SelectItem getCustomer() {
        return this.customer;
    }

    public void setCustomer(final SelectItem customer) {
        this.customer = customer;
    }

    public SelectItem getPaymentTerms() {
        return this.paymentTerms;
    }

    public void setPaymentTerms(final SelectItem paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public Integer getTaxCalculationType() {
        return this.taxCalculationType;
    }

    public void setTaxCalculationType(final Integer taxCalculationType) {
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


    public ArrayList<RentalOrderItem> getRentalOrderItems() {
        return this.rentalOrderItems;
    }

    public void setRentalOrderItems(final ArrayList<RentalOrderItem> rentalOrderItems) {
        this.rentalOrderItems = rentalOrderItems;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public void setTaxAmount(final BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getSubTotal() {
        return this.subTotal;
    }

    public void setSubTotal(final BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public void setTotal(final BigDecimal total) {
        this.total = total;
    }

    public ColumnConfigs[] getItemColumns() {
        return this.itemColumns;
    }

    public void setItemColumns(final ColumnConfigs[] itemColumns) {
        this.itemColumns = itemColumns;
    }

    public SelectItem[] getTemplates() {
        return this.templates;
    }

    public void setTemplates(final SelectItem[] templates) {
        this.templates = templates;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public TreeSelectItem[] getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(TreeSelectItem[] productCategories) {
        this.productCategories = productCategories;
    }

    public SelectItem[] getProductBrands() {
        return productBrands;
    }

    public void setProductBrands(SelectItem[] productBrands) {
        this.productBrands = productBrands;
    }

    public Double getSupplierCustomerBalance() {
        return supplierCustomerBalance != null ? supplierCustomerBalance : 0d;
    }

    public void setSupplierCustomerBalance(Double supplierCustomerBalance) {
        this.supplierCustomerBalance = supplierCustomerBalance;
    }

    public SelectItem getInvoiceItem() {
        return invoiceItem;
    }

    public void setInvoiceItem(SelectItem invoiceItem) {
        this.invoiceItem = invoiceItem;
    }

    public SelectItem getCreator() {
        return this.creator;
    }

    public void setCreator(final SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getApprover() {
        return this.approver;
    }

    public void setApprover(final SelectItem approver) {
        this.approver = approver;
    }

    public boolean isApproveProcessEnabled() {
        return this.approveProcessEnabled;
    }

    public void setApproveProcessEnabled(final boolean approveProcessEnabled) {
        this.approveProcessEnabled = approveProcessEnabled;
    }

    public ArrayList<RelationItem> getRelationItems() {
        return relationItems;
    }

    public void setRelationItems(ArrayList<RelationItem> relationItems) {
        this.relationItems = relationItems;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    @Override
    public String toString() {
        return "RentalOrderDto{" +
                "objectID=" + objectID +
                ", number='" + number + '\'' +
                ", status=" + status +
                ", statusCode='" + statusCode + '\'' +
                ", numberData=" + numberData +
                ", expiration=" + expiration +
                ", customer=" + customer +
                ", paymentTerms=" + paymentTerms +
                ", taxCalculationType=" + taxCalculationType +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", rentalOrderItems=" + rentalOrderItems +
                ", itemColumns=" + Arrays.toString(itemColumns) +
                ", templates=" + Arrays.toString(templates) +
                ", taxAmount=" + taxAmount +
                ", subTotal=" + subTotal +
                ", total=" + total +
                ", customFieldItems=" + customFieldItems +
                ", supplierCustomerBalance=" + supplierCustomerBalance +
                ", invoiceItem=" + invoiceItem +
                ", productCategories=" + Arrays.toString(productCategories) +
                ", productBrands=" + Arrays.toString(productBrands) +
                ", creator=" + creator +
                ", approver=" + approver +
                ", approveProcessEnabled=" + approveProcessEnabled +
                ", relationItems=" + relationItems +
                ", customFields=" + customFields +
                '}';
    }
}
