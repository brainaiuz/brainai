package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;

public class TypeItem extends SelectItem {

    private static final String CURRENCY_ID = "currencyID";
    private static final String PAYMENT_TYPE_ID = "paymentTypeID";
    private static final String PAYMENT_TYPE = "paymentType";
    private static final String CURRENCY = "currency";
    private static final String DUE_AMOUNT = "dueAmount";
    private static final String STATUS = "status";
    private static final String ENCRYPTED_LINK = "encryptedLink";
    private static final String BILL_ADDRESS_ID = "billAddressID";
    private static final String MAIL_ADDRESS_ID = "mailAddressID";
    private static final String SHIPPING_METHOD_ID = "shippingMethodId";
    private static final String DROP_SHIP_TO_MAIL_ADDRESS_HTML = "dropShipToMailAddressHTML";

    private DateNonConvertable dueDate;

    private InvoiceTermsItem termsItem;

    private TaxItem taxItem;

    private Integer bankAccountID;
    private Double supplierCustomerBalance;
    private AccountItem accountsReceivablePayable;
    private Boolean isSubsidiary;
    private boolean isReverseChargeApplicable;
    private SelectItem defaultWarehouse;
    private SelectItem defaultDepartment;

    private SelectItem taxTreatment;
    private SelectItem placeOfSupply;

    public TypeItem() {

    }

    public TypeItem(Integer id, String name, String code) {
        this(id, name, code, null);
    }

    public TypeItem(Integer id, String name, String code, Integer currencyID) {
        this(id, name, code, currencyID, null);
    }

    public TypeItem(Integer id, String name, String code, Integer currencyID, Integer paymentTypeID) {
        super(id, name);
        setCode(code);
        setCurrencyID(currencyID);
        setPaymentTypeID(paymentTypeID);
    }

    public Integer getCurrencyID() {
        return getInteger(CURRENCY_ID);
    }

    public void setCurrencyID(Integer currencyID) {
        addInteger(CURRENCY_ID, currencyID);
    }

    public Integer getPaymentTypeID() {
        return getInteger(PAYMENT_TYPE_ID);
    }

    public void setPaymentTypeID(Integer paymentTypeID) {
        addInteger(PAYMENT_TYPE_ID, paymentTypeID);
    }

    public String getPaymentType() {
        return getString(PAYMENT_TYPE);
    }

    public void setPaymentType(String paymentType) {
        addString(PAYMENT_TYPE, paymentType);
    }

    public String getCurrency() {
        return getString(CURRENCY);
    }

    public void setCurrency(String currency) {
        addString(CURRENCY, currency);
    }

    public DateNonConvertable getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateNonConvertable dueDate) {
        this.dueDate = dueDate;
    }

    public Double getDueAmount() {
        return getDouble(DUE_AMOUNT);
    }

    public void setDueAmount(Double dueAmount) {
        addDouble(DUE_AMOUNT, dueAmount);
    }

    public String getStatus() {
        return getString(STATUS);
    }

    public void setStatus(String status) {
        addString(STATUS, status);
    }

    public String getEncryptedLink() {
        return getString(ENCRYPTED_LINK);
    }

    public void setEncryptedLink(String encryptedLink) {
        addString(ENCRYPTED_LINK, encryptedLink);
    }

    public Integer getBillAddressID() {
        return getInteger(BILL_ADDRESS_ID);
    }

    public void setBillAddressID(Integer billAddressID) {
        addInteger(BILL_ADDRESS_ID, billAddressID);
    }

    public Integer getMailAddressID() {
        return getInteger(MAIL_ADDRESS_ID);
    }

    public void setMailAddressID(Integer mailAddressID) {
        addInteger(MAIL_ADDRESS_ID, mailAddressID);
    }

    public Integer getShippingMethodId() {
        return getInteger(SHIPPING_METHOD_ID);
    }

    public void setShippingMethodId(Integer shippingMethodId) {
        addInteger(SHIPPING_METHOD_ID, shippingMethodId);
    }

    public String getDropShipToMailAddressHTML() {
        return getString(DROP_SHIP_TO_MAIL_ADDRESS_HTML);
    }

    public void setDropShipToMailAddressHTML(String dropShipToMailAddressHTML) {
        addString(DROP_SHIP_TO_MAIL_ADDRESS_HTML, dropShipToMailAddressHTML);
    }

    public InvoiceTermsItem getTermsItem() {
        return termsItem;
    }

    public void setTermsItem(InvoiceTermsItem termsItem) {
        this.termsItem = termsItem;
    }

    public TaxItem getTaxItem() {
        return taxItem;
    }

    public void setTaxItem(TaxItem taxItem) {
        this.taxItem = taxItem;
    }

    public Integer getBankAccountID() {
        return bankAccountID;
    }

    public void setBankAccountID(Integer bankAccountID) {
        this.bankAccountID = bankAccountID;
    }

    public Double getSupplierCustomerBalance() {
        return supplierCustomerBalance != null ? supplierCustomerBalance : 0d;
    }

    public void setSupplierCustomerBalance(Double supplierCustomerBalance) {
        this.supplierCustomerBalance = supplierCustomerBalance;
    }

    public AccountItem getAccountsReceivablePayable() {
        return accountsReceivablePayable;
    }

    public void setAccountsReceivablePayable(AccountItem accountsReceivablePayable) {
        this.accountsReceivablePayable = accountsReceivablePayable;
    }

    public Boolean isSubsidiary() {
        return Boolean.TRUE.equals(isSubsidiary);
    }

    public void setSubsidiary(Boolean subsidiary) {
        isSubsidiary = subsidiary;
    }

    public boolean isReverseChargeApplicable() {
        return isReverseChargeApplicable;
    }

    public void setReverseChargeApplicable(boolean reverseChargeApplicable) {
        isReverseChargeApplicable = reverseChargeApplicable;
    }

    public SelectItem getDefaultWarehouse() {
        return defaultWarehouse;
    }

    public void setDefaultWarehouse(SelectItem defaultWarehouse) {
        this.defaultWarehouse = defaultWarehouse;
    }

    public SelectItem getDefaultDepartment() {
        return defaultDepartment;
    }

    public void setDefaultDepartment(SelectItem defaultDepartment) {
        this.defaultDepartment = defaultDepartment;
    }

    public SelectItem getTaxTreatment() {
        return taxTreatment;
    }

    public void setTaxTreatment(SelectItem taxTreatment) {
        this.taxTreatment = taxTreatment;
    }

    public SelectItem getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(SelectItem placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }
}
