package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 27.02.2009
 * Time: 18:00:15
 * To change this template use File | Settings | File Templates.
 */
public class TrialBalance implements IsSerializable {

    private TrialBalanceItem[] revenue;
    private TrialBalanceItem[] expenses;
    private TrialBalanceItem[] assets;
    private TrialBalanceItem[] liabilities;
    private TrialBalanceItem[] equity;

    public TrialBalance() {
    }

    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private BigDecimal totalBeginningBalance;
    private BigDecimal totalBeginningDebit;
    private BigDecimal totalBeginningCredit;
    private BigDecimal totalEndingBalance;
    private BigDecimal totalEndingDebit;
    private BigDecimal totalEndingCredit;

    public TrialBalanceItem[] getRevenue() {
        return revenue;
    }

    public void setRevenue(TrialBalanceItem[] revenue) {
        this.revenue = revenue;
    }

    public TrialBalanceItem[] getExpenses() {
        return expenses;
    }

    public void setExpenses(TrialBalanceItem[] expenses) {
        this.expenses = expenses;
    }

    public TrialBalanceItem[] getAssets() {
        return assets;
    }

    public void setAssets(TrialBalanceItem[] assets) {
        this.assets = assets;
    }

    public TrialBalanceItem[] getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(TrialBalanceItem[] liabilities) {
        this.liabilities = liabilities;
    }

    public TrialBalanceItem[] getEquity() {
        return equity;
    }

    public void setEquity(TrialBalanceItem[] equity) {
        this.equity = equity;
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

    public BigDecimal getTotalBeginningBalance() {
        return totalBeginningBalance;
    }

    public void setTotalBeginningBalance(BigDecimal totalBeginningBalance) {
        this.totalBeginningBalance = totalBeginningBalance;
    }

    public BigDecimal getTotalEndingBalance() {
        return totalEndingBalance;
    }

    public void setTotalEndingBalance(BigDecimal totalEndingBalance) {
        this.totalEndingBalance = totalEndingBalance;
    }

    public BigDecimal getTotalBeginningDebit() {
        return totalBeginningDebit;
    }

    public void setTotalBeginningDebit(BigDecimal totalBeginningDebit) {
        this.totalBeginningDebit = totalBeginningDebit;
    }

    public BigDecimal getTotalBeginningCredit() {
        return totalBeginningCredit;
    }

    public void setTotalBeginningCredit(BigDecimal totalBeginningCredit) {
        this.totalBeginningCredit = totalBeginningCredit;
    }

    public BigDecimal getTotalEndingDebit() {
        return totalEndingDebit;
    }

    public void setTotalEndingDebit(BigDecimal totalEndingDebit) {
        this.totalEndingDebit = totalEndingDebit;
    }

    public BigDecimal getTotalEndingCredit() {
        return totalEndingCredit;
    }

    public void setTotalEndingCredit(BigDecimal totalEndingCredit) {
        this.totalEndingCredit = totalEndingCredit;
    }
}
