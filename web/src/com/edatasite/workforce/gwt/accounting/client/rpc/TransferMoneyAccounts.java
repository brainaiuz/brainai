package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Feb 26, 2010
 * Time: 7:02:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransferMoneyAccounts implements IsSerializable {
    private TransferMoneyAccountItem[] bankAccounts;
    private TransferMoneyAccountItem[] assets;
    private TransferMoneyAccountItem[] equity;
    private TransferMoneyAccountItem[] expenses;
    private TransferMoneyAccountItem[] revenue;

    public TransferMoneyAccountItem[] getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(TransferMoneyAccountItem[] bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public TransferMoneyAccountItem[] getAssets() {
        return assets;
    }

    public void setAssets(TransferMoneyAccountItem[] assets) {
        this.assets = assets;
    }

    public TransferMoneyAccountItem[] getEquity() {
        return equity;
    }

    public void setEquity(TransferMoneyAccountItem[] equity) {
        this.equity = equity;
    }

    public TransferMoneyAccountItem[] getExpenses() {
        return expenses;
    }

    public void setExpenses(TransferMoneyAccountItem[] expenses) {
        this.expenses = expenses;
    }

    public TransferMoneyAccountItem[] getRevenue() {
        return revenue;
    }

    public void setRevenue(TransferMoneyAccountItem[] revenue) {
        this.revenue = revenue;
    }
}
