package com.edatasite.workforce.gwt.core.client.ui.view.multiCashAdvance;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.List;

public class MultiCashAdvanceItem extends HasApprovers implements IsSerializable {

    public static final String ACTION = "action";
    public static final String DATE = "date";
    public static final String APPROVER = "approver";
    public static final String AMOUNT = "amount";
    public static final String STATUS = "status";
    public static final String NUMBER = "number";
    public static final String REMAINING_AMOUNT = "remainingAmount";

    public static final String NUMBER_EXISTS = "NUMBER_EXISTS";
    public static final String NOT_SUFFICIENT_AMOUNT = "NOT_SUFFICIENT_AMOUNT";
    private Integer objectID;
    private SelectItem approver;
    private SelectItem employee;
    private String amountType;
    private String type;
    private BigDecimal paymentAmount;
    private BigDecimal totalAmount;
    private BigDecimal totalInBaseAmount;
    private DateNonConvertable date;
    private DateNonConvertable approvedDate;
    private DateNonConvertable creationDate;
    private SelectItem paymentMethod;
    private SelectItem paymentTerms;
    private BigDecimal paymentTermsAmount;
    private SelectItem[] paymentMethods;
    private SelectItem status;
    private BigDecimal fixedAmount;
    private SelectItem paidFromAccount;
    private SelectItem cashAdvanceAccount;
    private SelectItem categoryItem;
    private CurrencyItem currency;
    private BigDecimal exchangeRate;
    private Boolean enabledMultiCurrency;
    private Integer currentUserId;
    private BankTransferNumberData numberData;
    private String number;
    private Integer intNumber;
    private BigDecimal remainingAmount;
    private List<CashAdvanceItem> cashAdvanceItems;
    private Boolean doubleConfirmationEnabled;

    public Integer getObjectID() {
        return this.objectID;
    }

    public void setObjectID(final Integer objectID) {
        this.objectID = objectID;
    }

    public SelectItem getApprover() {
        return this.approver;
    }

    public void setApprover(final SelectItem approver) {
        this.approver = approver;
    }

    public SelectItem getEmployee() {
        return this.employee;
    }

    public void setEmployee(final SelectItem employee) {
        this.employee = employee;
    }

    public String getAmountType() {
        return this.amountType;
    }

    public void setAmountType(final String amountType) {
        this.amountType = amountType;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public BigDecimal getPaymentAmount() {
        return this.paymentAmount;
    }

    public void setPaymentAmount(final BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(final BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalInBaseAmount() {
        return this.totalInBaseAmount;
    }

    public void setTotalInBaseAmount(final BigDecimal totalInBaseAmount) {
        this.totalInBaseAmount = totalInBaseAmount;
    }

    public DateNonConvertable getDate() {
        return this.date;
    }

    public void setDate(final DateNonConvertable date) {
        this.date = date;
    }

    public DateNonConvertable getApprovedDate() {
        return this.approvedDate;
    }

    public void setApprovedDate(final DateNonConvertable approvedDate) {
        this.approvedDate = approvedDate;
    }

    public DateNonConvertable getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(final DateNonConvertable creationDate) {
        this.creationDate = creationDate;
    }

    public SelectItem getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(final SelectItem paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public SelectItem[] getPaymentMethods() {
        return this.paymentMethods;
    }

    public void setPaymentMethods(final SelectItem[] paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public SelectItem getPaymentTerms() {
        return this.paymentTerms;
    }

    public void setPaymentTerms(final SelectItem paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public BigDecimal getPaymentTermsAmount() {
        return this.paymentTermsAmount;
    }

    public void setPaymentTermsAmount(final BigDecimal paymentTermsAmount) {
        this.paymentTermsAmount = paymentTermsAmount;
    }

    public SelectItem getStatus() {
        return this.status;
    }

    public void setStatus(final SelectItem status) {
        this.status = status;
    }

    public BigDecimal getFixedAmount() {
        return this.fixedAmount;
    }

    public void setFixedAmount(final BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public SelectItem getPaidFromAccount() {
        return this.paidFromAccount;
    }

    public void setPaidFromAccount(final SelectItem paidFromAccount) {
        this.paidFromAccount = paidFromAccount;
    }

    public SelectItem getCashAdvanceAccount() {
        return this.cashAdvanceAccount;
    }

    public void setCashAdvanceAccount(final SelectItem cashAdvanceAccount) {
        this.cashAdvanceAccount = cashAdvanceAccount;
    }

    public SelectItem getCategoryItem() {
        return this.categoryItem;
    }

    public void setCategoryItem(final SelectItem categoryItem) {
        this.categoryItem = categoryItem;
    }

    public CurrencyItem getCurrency() {
        return this.currency;
    }

    public void setCurrency(final CurrencyItem currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return this.exchangeRate;
    }

    public void setExchangeRate(final BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Boolean getEnabledMultiCurrency() {
        return this.enabledMultiCurrency;
    }

    public void setEnabledMultiCurrency(final Boolean enabledMultiCurrency) {
        this.enabledMultiCurrency = enabledMultiCurrency;
    }

    public Integer getCurrentUserId() {
        return this.currentUserId;
    }

    public void setCurrentUserId(final Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public BankTransferNumberData getNumberData() {
        return this.numberData;
    }

    public void setNumberData(final BankTransferNumberData numberData) {
        this.numberData = numberData;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public Integer getIntNumber() {
        return this.intNumber;
    }

    public void setIntNumber(final Integer intNumber) {
        this.intNumber = intNumber;
    }

    public BigDecimal getRemainingAmount() {
        return this.remainingAmount;
    }

    public void setRemainingAmount(final BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public List<CashAdvanceItem> getCashAdvanceItems() {
        return this.cashAdvanceItems;
    }

    public void setCashAdvanceItems(final List<CashAdvanceItem> cashAdvanceItems) {
        this.cashAdvanceItems = cashAdvanceItems;
    }

    public Boolean getDoubleConfirmationEnabled() {
        return doubleConfirmationEnabled != null ? doubleConfirmationEnabled : false;
    }

    public void setDoubleConfirmationEnabled(Boolean doubleConfirmationEnabled) {
        this.doubleConfirmationEnabled = doubleConfirmationEnabled;
    }
}
