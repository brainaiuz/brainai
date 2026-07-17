package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 23.02.2009
 * Time: 17:28:10
 * To change this template use File | Settings | File Templates.
 */
public class AccountTypesByCategory implements IsSerializable {
    private SelectItem[] assets;
    private SelectItem[] liabilities;
    private SelectItem[] equity;
    private SelectItem[] revenue;
    private SelectItem[] expenses;
    private SelectItem[] creditCard;
    private SelectItem defaultAccount;

    public SelectItem[] getAssets() {
        return assets;
    }

    public void setAssets(SelectItem[] assets) {
        this.assets = assets;
    }

    public SelectItem[] getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(SelectItem[] liabilities) {
        this.liabilities = liabilities;
    }

    public SelectItem[] getEquity() {
        return equity;
    }

    public void setEquity(SelectItem[] equity) {
        this.equity = equity;
    }

    public SelectItem[] getRevenue() {
        return revenue;
    }

    public void setRevenue(SelectItem[] revenue) {
        this.revenue = revenue;
    }

    public SelectItem[] getExpenses() {
        return expenses;
    }

    public void setExpenses(SelectItem[] expenses) {
        this.expenses = expenses;
    }

    public SelectItem[] getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(SelectItem[] creditCard) {
        this.creditCard = creditCard;
    }

    public SelectItem getDefaultAccount() {
        return this.defaultAccount;
    }

    public void setDefaultAccount(final SelectItem defaultAccount) {
        this.defaultAccount = defaultAccount;
    }

    //    public SelectItem[] getAllItems(){
//        int size=assets.length+liabilities.length+equity.length+revenue.length+expenses.length;
//        SelectItem[] items=new SelectItem[size];
//        int k=0;
//        for(int i=0;i<assets.length;i++){
//            items[k]=assets[i];
//            k++;
//        }
//        for(int i=0;i<liabilities.length;i++){
//            items[k]=liabilities[i];
//            k++;
//        }
//        for(int i=0;i<equity.length;i++){
//            items[k]=equity[i];
//            k++;
//        }
//        for(int i=0;i<revenue.length;i++){
//            items[k]=revenue[i];
//            k++;
//        }
//        for(int i=0;i<expenses.length;i++){
//            items[k]=expenses[i];
//            k++;
//        }
//        return items;
//    }
}
