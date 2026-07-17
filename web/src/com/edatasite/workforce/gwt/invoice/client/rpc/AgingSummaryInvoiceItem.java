package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 8/10/12
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class AgingSummaryInvoiceItem implements IsSerializable {

    private Integer objectID;
    private String typeName;
    private String accountType;
    private DateNonConvertable invoiceDate;
    private DateNonConvertable dueDate;
    private String invoiceNumber;
    private String reference;
    private String customerOrSupplierName;
    private Integer clientOrSupplierId;
    private String terms;
    private Integer aging;
    private BigDecimal amount;
    private BigDecimal exchangeRate;
    private boolean isCreditNote;

    private String journalId;
    private String exchangeRates;
    private String currencyDifference;

    private String currencyName;

    private Date date;
    private Date due_date;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public DateNonConvertable getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(DateNonConvertable invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public DateNonConvertable getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateNonConvertable dueDate) {
        this.dueDate = dueDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCustomerOrSupplierName() {
        return customerOrSupplierName;
    }

    public void setCustomerOrSupplierName(String customerOrSupplierName) {
        this.customerOrSupplierName = customerOrSupplierName;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public Integer getAging() {
        return aging;
    }

    public void setAging(Integer aging) {
        this.aging = aging;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public boolean isCreditNote() {
        return isCreditNote;
    }

    public void setCreditNote(boolean creditNote) {
        isCreditNote = creditNote;
    }

    public void setDate(Date date) {
        this.date = date;
        this.invoiceDate = new DateNonConvertable(date);
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
        this.dueDate = new DateNonConvertable(due_date);
    }

    public Integer getClientOrSupplierId() {
        return clientOrSupplierId;
    }

    public void setClientOrSupplierId(Integer clientOrSupplierId) {
        this.clientOrSupplierId = clientOrSupplierId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getJournalId() {
        return journalId;
    }

    public void setJournalId(String journalId) {
        this.journalId = journalId;
    }

    public String getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(String exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyDifference() {
        return currencyDifference;
    }

    public void setCurrencyDifference(String currencyDifference) {
        this.currencyDifference = currencyDifference;
    }
}
