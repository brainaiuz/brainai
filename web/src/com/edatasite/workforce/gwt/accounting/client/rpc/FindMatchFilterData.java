package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/6/14
 * Time: 1:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class FindMatchFilterData implements IsSerializable{
    private Integer glAccountID;
    private Integer bankStatementItemID;
    private Integer projectID;
    private Boolean isDebitCredit;
    private BigDecimal transactionAmount;

    private String searchKey;
    private Integer baseCurrencyID;
    private BigDecimal startAmount;
    private BigDecimal endAmount;
    private DateNonConvertable startDate;
    private DateNonConvertable endDate;
    private Integer receivablePayableID;
    private boolean parentReceivablePayable;
    private BigDecimal credit;
    private BigDecimal debit;
    private BigDecimal balance;
    private String sortField;
    private String sortDirection;
    private boolean reconsiled;
    private boolean isMultiEnabled;
    private boolean isPaymentDiffCurrency;

    public FindMatchFilterData() {
    }

    public Integer getGlAccountID() {
        return glAccountID;
    }

    public void setGlAccountID(Integer glAccountID) {
        this.glAccountID = glAccountID;
    }

    public Boolean isDebitCredit() {
        return isDebitCredit!=null ? isDebitCredit : false;
    }

    public void setDebitCredit(Boolean debitCredit) {
        isDebitCredit = debitCredit;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public BigDecimal getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(BigDecimal startAmount) {
        this.startAmount = startAmount;
    }

    public BigDecimal getEndAmount() {
        return endAmount;
    }

    public void setEndAmount(BigDecimal endAmount) {
        this.endAmount = endAmount;
    }

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public DateNonConvertable getEndDate() {
        return endDate;
    }

    public void setEndDate(DateNonConvertable endDate) {
        this.endDate = endDate;
    }

    public Integer getReceivablePayableID() {
        return receivablePayableID;
    }

    public void setReceivablePayableID(Integer receivablePayableID) {
        this.receivablePayableID = receivablePayableID;
    }

    public boolean isParentReceivablePayable() {
        return parentReceivablePayable;
    }

    public void setParentReceivablePayable(boolean parentReceivablePayable) {
        this.parentReceivablePayable = parentReceivablePayable;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public boolean isReconsiled() {
        return reconsiled;
    }

    public void setReconsiled(boolean reconsiled) {
        this.reconsiled = reconsiled;
    }

    public boolean isMultiEnabled() {
        return isMultiEnabled;
    }

    public void setMultiEnabled(boolean multiEnabled) {
        isMultiEnabled = multiEnabled;
    }

    public boolean isPaymentDiffCurrency() {
        return isPaymentDiffCurrency;
    }

    public void setPaymentDiffCurrency(boolean paymentDiffCurrency) {
        isPaymentDiffCurrency = paymentDiffCurrency;
    }

    public Integer getBaseCurrencyID() {
        return baseCurrencyID;
    }

    public void setBaseCurrencyID(Integer baseCurrencyID) {
        this.baseCurrencyID = baseCurrencyID;
    }

    public Integer getBankStatementItemID() {
        return bankStatementItemID;
    }

    public void setBankStatementItemID(Integer bankStatementItemID) {
        this.bankStatementItemID = bankStatementItemID;
    }
}
