package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 11.03.2009
 * Time: 17:30:14
 * To change this template use File | Settings | File Templates.
 */
public class TransactionsBetweenDatesInAccount implements IsSerializable {
    private Date balanceDate;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private BigDecimal totalBalance;
    private BigDecimal balanceDebit;
    private BigDecimal balanceCredit;
    private BigDecimal totalBeginningBalance;
    private CurrencyItem currency;
    private boolean foreignAccount;
    private String accountType;
    private BigDecimal balanceStart;//for multi page lists
    private ArrayList<Transaction> transactions;
    private int totalCount;

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Date getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    public BigDecimal getBalanceDebit() {
        return balanceDebit;
    }

    public void setBalanceDebit(BigDecimal balanceDebit) {
        this.balanceDebit = balanceDebit;
    }

    public BigDecimal getBalanceCredit() {
        return balanceCredit;
    }

    public void setBalanceCredit(BigDecimal balanceCredit) {
        this.balanceCredit = balanceCredit;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getTotalBeginningBalance() {
        return totalBeginningBalance;
    }

    public void setTotalBeginningBalance(BigDecimal totalBeginningBalance) {
        this.totalBeginningBalance = totalBeginningBalance;
    }

    public BigDecimal getBalanceStart() {
        return balanceStart;
    }

    public void setBalanceStart(BigDecimal balanceStart) {
        this.balanceStart = balanceStart;
    }

    public boolean isForeignAccount() {
        return foreignAccount;
    }

    public void setForeignAccount(boolean foreignAccount) {
        this.foreignAccount = foreignAccount;
    }
}
