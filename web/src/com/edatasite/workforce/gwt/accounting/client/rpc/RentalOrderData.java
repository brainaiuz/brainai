package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RentalOrderData extends HasApprovers implements Serializable, ListingCustomFields {
    public static final String NUMBER = "number";
    public static final String EXPIRATION = "expiration";
    public static final String CREATOR = "creator";
    public static final String MANAGER = "manager";
    public static final String RELATED_PROJECT = "relatedProject";
    public static final String LOCATION = "location";
    public static final String STATUS = "status";
    public static final String CREATED_DATE = "created_date";
    public static final String CUSTOMER = "customer";
    public static final String APPROVERS = "approvers";

    private Integer objectID;
    private String number;
    private SelectItem status;
    private String statusCode;
    private NumberData numberData;
    private Date startDate;
    private Date expirationDate;
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
    private HashMap<String, Object> customFieldValues;
    private Double supplierCustomerBalance;
    private SelectItem invoiceItem;
    private TreeSelectItem[] productCategories;
    private SelectItem[] productBrands;
    private SelectItem creator;
    private SelectItem approver;
    private boolean approveProcessEnabled;
    private ArrayList<RelationItem> relationItems;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
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

    public HashMap<String, Object> getCustomFieldValuesItems() {
        return customFieldValues;
    }

    public void setCustomFieldValuesItems(HashMap<String, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return customFieldValues.get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        customFieldValues.put(columnCodeKey, cellValue);
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
}
