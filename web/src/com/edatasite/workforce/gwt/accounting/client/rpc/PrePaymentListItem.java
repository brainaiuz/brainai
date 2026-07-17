package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 12/23/11
 * Time: 4:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrePaymentListItem implements ListingCustomFields, IsSerializable {
    public static String CUSTOMER = "customer";
    public static String SUPPLIER = "supplier";
    public static String CODE = "code";
    public static String PAY_ACCOUNT = "payaccount";
    public static String NOTE = "note";
    public static String REFERENCE = "reference";
    public static String AMOUNT = "amount";
    public static String REMAIN = "remain";
    public static String PROJECT = "project";
    public static String STATUS = "status";
    public static String DATE = "date";
    public static String CURRENCY = "currency";
    public static String NUMBER = "number";
    public static String CREATOR = "creator";
    public static String DEPARTMENT = "department";
    public static String SALE_QUOTE = "salequote";
    public static String SALE_INVOICE = "saleinvoice";
    public static String PURCHASE_ORDER = "purchaseorder";
    public static String REMAINING_BALANCE = "remainingBalance";

    private Integer objectID;
    private String customerName;
    private String accountNumber;
    private String payAccount;
    private String note;
    private String reference;
    private SelectItem saleQuote;
    private SelectItem rentalOrder;
    private SelectItem saleInvoice;
    private SelectItem purchaseOrder;
    private BigDecimal amount;
    private BigDecimal appliedAmount;
    private String project;
    private String status;
    private DateNonConvertable date;
    private String currency;
    private String number;
    private String creator;
    private String department;
    private Integer accountID;

    private boolean editable;
    private HashMap<String, Object> customFieldsMap;

    public PrePaymentListItem() {

    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getAmount() {
        return amount != null ? amount : BigDecimal.ZERO;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAppliedAmount() {
        return appliedAmount != null ? appliedAmount : BigDecimal.ZERO;
    }

    public void setAppliedAmount(BigDecimal appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    public BigDecimal getRemainingBalance() {
        return getAmount().subtract(getAppliedAmount());
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public SelectItem getSaleQuote() {
        return saleQuote;
    }

    public void setSaleQuote(SelectItem saleQuote) {
        this.saleQuote = saleQuote;
    }

    public SelectItem getRentalOrder() {
        return rentalOrder;
    }

    public void setRentalOrder(SelectItem rentalOrder) {
        this.rentalOrder = rentalOrder;
    }

    public SelectItem getSaleInvoice() {
        return saleInvoice;
    }

    public void setSaleInvoice(SelectItem saleInvoice) {
        this.saleInvoice = saleInvoice;
    }

    public SelectItem getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(SelectItem purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap() != null ? getCustomFieldsMap().get(columnCodeKey) : null;
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        if (customFieldsMap == null) {
            customFieldsMap = new HashMap<>();
        }
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }
}
