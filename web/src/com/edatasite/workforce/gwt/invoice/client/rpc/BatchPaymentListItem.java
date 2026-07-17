package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by Sherzod on 7/6/2015.
 */
public class BatchPaymentListItem implements ListingCustomFields, IsSerializable {

    public static final String ACTION = "action";
    public static final String NUMBER = "number";
    public static final String CRM_ACCOUNT = "crmAccount";
    public static final String DATE = "date";
    public static final String REFERENCE = "reference";
    public static final String ACCOUNT = "account";
    public static final String AMOUNT = "amount";
    public static final String PAYMENT_TYPE = "paymentType";
    public static final String CURRENCY = "currency";
    public static final String PROJECT = "project";
    public static final String CREATOR = "creator";
    public static final String DEPARTMENT = "department";

    private Integer objectID;
    private String number;
    private SelectItem crmAccount;
    private SelectItem account;
    private SelectItem currency;
    private String project;
    private BigDecimal exchangeRate;
    private BigDecimal totalAmount;
    private String reference;
    private String creator;
    private DateNonConvertable date;
    private SelectItem paymentMethod;//Cash, Credit Card, Debit Card, ...
    private Integer paymentTarget;//Invoice or Manual Journal
    private String type;//RECEIVABLE OR PAYABLE
    private String department;//RECEIVABLE OR PAYABLE
    private boolean reversed;
    private HashMap<String, Object> customFieldsMap;


    public BatchPaymentListItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public SelectItem getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(SelectItem crmAccount) {
        this.crmAccount = crmAccount;
    }

    public SelectItem getAccount() {
        return account;
    }

    public void setAccount(SelectItem account) {
        this.account = account;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public SelectItem getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(SelectItem paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getPaymentTarget() {
        return paymentTarget;
    }

    public void setPaymentTarget(Integer paymentTarget) {
        this.paymentTarget = paymentTarget;
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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
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

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }
}