package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 02.03.2009
 * Time: 19:38:28
 * To change this template use File | Settings | File Templates.
 */
public class ProfitLoss implements IsSerializable {
    private String[] headers;
    private ProfitLossItem income;
    private ProfitLossItem lessCostOfSales;
    private ProfitLossInnerItem grossProfit;
    private ProfitLossItem lessOperatingExpenses;
    private ProfitLossInnerItem netProfit;

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public ProfitLossItem getIncome() {
        return income;
    }

    public void setIncome(ProfitLossItem income) {
        this.income = income;
    }

    public ProfitLossItem getLessCostOfSales() {
        return lessCostOfSales;
    }

    public void setLessCostOfSales(ProfitLossItem lessCostOfSales) {
        this.lessCostOfSales = lessCostOfSales;
    }

    public ProfitLossInnerItem getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(ProfitLossInnerItem grossProfit) {
        this.grossProfit = grossProfit;
    }

    public ProfitLossItem getLessOperatingExpenses() {
        return lessOperatingExpenses;
    }

    public void setLessOperatingExpenses(ProfitLossItem lessOperatingExpenses) {
        this.lessOperatingExpenses = lessOperatingExpenses;
    }

    public ProfitLossInnerItem getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(ProfitLossInnerItem netProfit) {
        this.netProfit = netProfit;
    }
}
