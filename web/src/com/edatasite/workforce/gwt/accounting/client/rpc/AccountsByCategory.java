package com.edatasite.workforce.gwt.accounting.client.rpc;


import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 23.02.2009
 * Time: 12:18:19
 * To change this template use File | Settings | File Templates.
 */
public class AccountsByCategory implements Serializable {

    private AccountItem[] assets;
    private AccountItem[] liabilities;
    private AccountItem[] equity;
    private AccountItem[] revenue;
    private AccountItem[] overhead;
    private AccountItem[] expenses;

    private Integer revenueProductSalesCode;
    private Integer expensesMaterialsPurchased;

    public AccountItem[] getAssets() {
        return assets;
    }

    public void setAssets(AccountItem[] assets) {
        this.assets = assets;
    }

    public AccountItem[] getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(AccountItem[] liabilities) {
        this.liabilities = liabilities;
    }

    public AccountItem[] getEquity() {
        return equity;
    }

    public void setEquity(AccountItem[] equity) {
        this.equity = equity;
    }

    public AccountItem[] getRevenue() {
        return revenue;
    }

    public void setRevenue(AccountItem[] revenue) {
        this.revenue = revenue;
    }

    public AccountItem[] getOverhead() {
        return overhead;
    }

    public void setOverhead(AccountItem[] revenue) {
        this.overhead = revenue;
    }

    public AccountItem[] getExpenses() {
        return expenses;
    }

    public void setExpenses(AccountItem[] expenses) {
        this.expenses = expenses;
    }

    public Integer getRevenueProductSalesCode() {
        return revenueProductSalesCode;
    }

    public void setRevenueProductSalesCode(Integer revenueProductSalesCode) {
        this.revenueProductSalesCode = revenueProductSalesCode;
    }

    public Integer getExpensesMaterialsPurchased() {
        return expensesMaterialsPurchased;
    }

    public void setExpensesMaterialsPurchased(Integer expensesMaterialsPurchased) {
        this.expensesMaterialsPurchased = expensesMaterialsPurchased;
    }
}
