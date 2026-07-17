package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.LinkedList;

/**
 * Created by Sherzod on 1/14/2016.
 */
public class CashFlow implements IsSerializable{

    //Operating Activities
    private BigDecimal netProfit;
    private LinkedList<CashFlowItem> currentAssets;
    private LinkedList<CashFlowItem> nonCurrentAssets;
    private LinkedList<CashFlowItem> prepayments;
    private LinkedList<CashFlowItem> currentLiabilities;
    private BigDecimal netOperatingActivities;

    //Investing Activities
    private LinkedList<CashFlowItem> accumulatedDepreciations;
    private LinkedList<CashFlowItem> fixedAssets;
    private LinkedList<CashFlowItem> liabilities;
    private BigDecimal netInvestingActivities;

    //Financing Activities
    private LinkedList<CashFlowItem> longTermLiabilities;
    private LinkedList<CashFlowItem> equities;
    private BigDecimal netFinancingActivities;

    private BigDecimal netIncreaseDecreaseForPeriod;
    private BigDecimal cashAtTheBeginningOfPeriod;
    private BigDecimal cashAtTheEndOfPeriod;

    public CashFlow() {
    }

    public BigDecimal getNetProfit() {
        return netProfit != null ? netProfit : BigDecimal.ZERO;
    }

    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }

    public LinkedList<CashFlowItem> getCurrentAssets() {
        return currentAssets;
    }

    public void setCurrentAssets(LinkedList<CashFlowItem> currentAssets) {
        this.currentAssets = currentAssets;
    }

    public LinkedList<CashFlowItem> getNonCurrentAssets() {
        return this.nonCurrentAssets;
    }

    public void setNonCurrentAssets(final LinkedList<CashFlowItem> nonCurrentAssets) {
        this.nonCurrentAssets = nonCurrentAssets;
    }

    public LinkedList<CashFlowItem> getPrepayments() {
        return prepayments;
    }

    public void setPrepayments(LinkedList<CashFlowItem> prepayments) {
        this.prepayments = prepayments;
    }

    public LinkedList<CashFlowItem> getCurrentLiabilities() {
        return currentLiabilities;
    }

    public void setCurrentLiabilities(LinkedList<CashFlowItem> currentLiabilities) {
        this.currentLiabilities = currentLiabilities;
    }

    public BigDecimal getNetOperatingActivities() {
        return netOperatingActivities != null ? netOperatingActivities : BigDecimal.ZERO;
    }

    public void setNetOperatingActivities(BigDecimal netOperatingActivities) {
        this.netOperatingActivities = netOperatingActivities;
    }

    public LinkedList<CashFlowItem> getAccumulatedDepreciations() {
        return accumulatedDepreciations;
    }

    public void setAccumulatedDepreciations(LinkedList<CashFlowItem> accumulatedDepreciations) {
        this.accumulatedDepreciations = accumulatedDepreciations;
    }

    public LinkedList<CashFlowItem> getFixedAssets() {
        return fixedAssets;
    }

    public void setFixedAssets(LinkedList<CashFlowItem> fixedAssets) {
        this.fixedAssets = fixedAssets;
    }

    public LinkedList<CashFlowItem> getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(LinkedList<CashFlowItem> liabilities) {
        this.liabilities = liabilities;
    }

    public BigDecimal getNetInvestingActivities() {
        return netInvestingActivities != null ? netInvestingActivities : BigDecimal.ZERO;
    }

    public void setNetInvestingActivities(BigDecimal netInvestingActivities) {
        this.netInvestingActivities = netInvestingActivities;
    }

    public LinkedList<CashFlowItem> getLongTermLiabilities() {
        return longTermLiabilities;
    }

    public void setLongTermLiabilities(LinkedList<CashFlowItem> longTermLiabilities) {
        this.longTermLiabilities = longTermLiabilities;
    }

    public LinkedList<CashFlowItem> getEquities() {
        return equities;
    }

    public void setEquities(LinkedList<CashFlowItem> equities) {
        this.equities = equities;
    }

    public BigDecimal getNetFinancingActivities() {
        return netFinancingActivities != null ? netFinancingActivities : BigDecimal.ZERO;
    }

    public void setNetFinancingActivities(BigDecimal netFinancingActivities) {
        this.netFinancingActivities = netFinancingActivities;
    }

    public BigDecimal getNetIncreaseDecreaseForPeriod() {
        return netIncreaseDecreaseForPeriod != null ? netIncreaseDecreaseForPeriod : BigDecimal.ZERO;
    }

    public void setNetIncreaseDecreaseForPeriod(BigDecimal netIncreaseDecreaseForPeriod) {
        this.netIncreaseDecreaseForPeriod = netIncreaseDecreaseForPeriod;
    }

    public BigDecimal getCashAtTheBeginningOfPeriod() {
        return cashAtTheBeginningOfPeriod != null ? cashAtTheBeginningOfPeriod : BigDecimal.ZERO;
    }

    public void setCashAtTheBeginningOfPeriod(BigDecimal cashAtTheBeginningOfPeriod) {
        this.cashAtTheBeginningOfPeriod = cashAtTheBeginningOfPeriod;
    }

    public BigDecimal getCashAtTheEndOfPeriod() {
        return cashAtTheEndOfPeriod != null ? cashAtTheEndOfPeriod : BigDecimal.ZERO;
    }

    public void setCashAtTheEndOfPeriod(BigDecimal cashAtTheEndOfPeriod) {
        this.cashAtTheEndOfPeriod = cashAtTheEndOfPeriod;
    }
}
