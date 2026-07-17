package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 16.03.2009
 * Time: 17:54:38
 * To change this template use File | Settings | File Templates.
 */
public class ExpensesAndRevenue implements IsSerializable {

    private AccountItemsByAccountType revenue;
    private AccountItemsByAccountType sale;
    private AccountItemsByAccountType expense;
    private AccountItemsByAccountType directCosts;
    private AccountItemsByAccountType depreciation;
    private AccountItemsByAccountType overhead;
    private AccountItemsByAccountType otherIncome;
    private AccountItemsByAccountType incomeTax;
    private LinkedList<AccountItemsByAccountType> items;
    private SelectItem currency;
    ;


    //Gross/Net data when show budget is disabled
    private ArrayList<BudgetInDate> grossProfit;
    private ArrayList<BudgetInDate> netProfitBeforeIncomeTax;
    private ArrayList<BudgetInDate> netProfit;


    //Gross/Net data when show budget is enabled
    private ActBudVar grossVariance;
    private ActBudVar grossYTDvariance;
    private ActBudVar netVarianceBeforeIncomeTax;
    private ActBudVar netVariance;
    private ActBudVar netYTDvariance;


    public AccountItemsByAccountType getRevenue() {
        return revenue;
    }

    public void setRevenue(AccountItemsByAccountType revenue) {
        this.revenue = revenue;
    }

    public AccountItemsByAccountType getSale() {
        return sale;
    }

    public void setSale(AccountItemsByAccountType sale) {
        this.sale = sale;
    }

    public AccountItemsByAccountType getExpense() {
        return expense;
    }

    public void setExpense(AccountItemsByAccountType expense) {
        this.expense = expense;
    }

    public AccountItemsByAccountType getDirectCosts() {
        return directCosts;
    }

    public void setDirectCosts(AccountItemsByAccountType directCosts) {
        this.directCosts = directCosts;
    }

    public AccountItemsByAccountType getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(AccountItemsByAccountType depreciation) {
        this.depreciation = depreciation;
    }

    public AccountItemsByAccountType getOverhead() {
        return overhead;
    }

    public void setOverhead(AccountItemsByAccountType overhead) {
        this.overhead = overhead;
    }

    public AccountItemsByAccountType getOtherIncome() {
        return otherIncome;
    }

    public void setOtherIncome(AccountItemsByAccountType otherIncome) {
        this.otherIncome = otherIncome;
    }

    public AccountItemsByAccountType getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(AccountItemsByAccountType incomeTax) {
        this.incomeTax = incomeTax;
    }

    public ArrayList<BudgetInDate> getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(ArrayList<BudgetInDate> grossProfit) {
        this.grossProfit = grossProfit;
    }

    public ArrayList<BudgetInDate> getNetProfitBeforeIncomeTax() {
        return netProfitBeforeIncomeTax;
    }

    public void setNetProfitBeforeIncomeTax(ArrayList<BudgetInDate> netProfitBeforeIncomeTax) {
        this.netProfitBeforeIncomeTax = netProfitBeforeIncomeTax;
    }

    public ArrayList<BudgetInDate> getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(ArrayList<BudgetInDate> netProfit) {
        this.netProfit = netProfit;
    }

    public ActBudVar getGrossVariance() {
        return grossVariance;
    }

    public void setGrossVariance(ActBudVar grossVariance) {
        this.grossVariance = grossVariance;
    }

    public ActBudVar getGrossYTDvariance() {
        return grossYTDvariance;
    }

    public void setGrossYTDvariance(ActBudVar grossYTDvariance) {
        this.grossYTDvariance = grossYTDvariance;
    }

    public ActBudVar getNetVarianceBeforeIncomeTax() {
        return netVarianceBeforeIncomeTax;
    }

    public void setNetVarianceBeforeIncomeTax(ActBudVar netVarianceBeforeIncomeTax) {
        this.netVarianceBeforeIncomeTax = netVarianceBeforeIncomeTax;
    }

    public ActBudVar getNetVariance() {
        return netVariance;
    }

    public void setNetVariance(ActBudVar netVariance) {
        this.netVariance = netVariance;
    }

    public ActBudVar getNetYTDvariance() {
        return netYTDvariance;
    }

    public void setNetYTDvariance(ActBudVar netYTDvariance) {
        this.netYTDvariance = netYTDvariance;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public LinkedList<AccountItemsByAccountType> getItems() {
        return this.items;
    }

    public void setItems(final LinkedList<AccountItemsByAccountType> items) {
        this.items = items;
    }
}
