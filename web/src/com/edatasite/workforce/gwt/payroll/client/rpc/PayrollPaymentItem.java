package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

public class PayrollPaymentItem implements IsSerializable {

    private Integer objectID;

    private Integer employeeID;
    private String employee;

    private Integer additionalPaymentItemID;

    private Integer paidFromAccountID;
    private SelectItem paidFromAccount;
    private Integer paidToAccountID;
    private SelectItem paidToAccount;

    private String reference;
    private String bankAccount;
    private String details;

    private DateNonConvertable dueDate;
    private DateNonConvertable paymentDate;
    private CurrencyItem currency;
    private BigDecimal exchangeRate;

    private BigDecimal dueAmount;
    private BigDecimal paymentAmount;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public Integer getAdditionalPaymentItemID() {
        return additionalPaymentItemID;
    }

    public void setAdditionalPaymentItemID(Integer additionalPaymentItemID) {
        this.additionalPaymentItemID = additionalPaymentItemID;
    }

    public Integer getPaidFromAccountID() {
        return paidFromAccountID;
    }

    public void setPaidFromAccountID(Integer paidFromAccountID) {
        this.paidFromAccountID = paidFromAccountID;
    }

    public SelectItem getPaidFromAccount() {
        return paidFromAccount;
    }

    public void setPaidFromAccount(SelectItem paidFromAccount) {
        this.paidFromAccount = paidFromAccount;
    }

    public Integer getPaidToAccountID() {
        return paidToAccountID;
    }

    public void setPaidToAccountID(Integer paidToAccountID) {
        this.paidToAccountID = paidToAccountID;
    }

    public SelectItem getPaidToAccount() {
        return paidToAccount;
    }

    public void setPaidToAccount(SelectItem paidToAccount) {
        this.paidToAccount = paidToAccount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public DateNonConvertable getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateNonConvertable dueDate) {
        this.dueDate = dueDate;
    }

    public DateNonConvertable getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(DateNonConvertable paymentDate) {
        this.paymentDate = paymentDate;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
