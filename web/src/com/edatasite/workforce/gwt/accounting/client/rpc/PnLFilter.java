package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.FromToDate;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by Omonullo on 6/25/2017.
 */
public class PnLFilter implements IsSerializable {
    FromToDate main;
    FromToDate[] compareTo;
    boolean showBudget;
    boolean showYearToDate;
    String sortField;
    String sortDirection;
    Boolean cosolidation;
    Integer departmentID;
    Integer currencyId;
    Integer projectID;
    String departmentAndTreeChildIDs;

    BigDecimal exchangeRate;
    Integer exchangeRateScale;

    boolean consolidation;
    boolean compCurParentCompCurEquals;

    public FromToDate getMain() {
        return main;
    }

    public void setMain(FromToDate main) {
        this.main = main;
    }

    public FromToDate[] getCompareTo() {
        return compareTo;
    }

    public void setCompareTo(FromToDate[] compareTo) {
        this.compareTo = compareTo;
    }

    public boolean isShowBudget() {
        return showBudget;
    }

    public void setShowBudget(boolean showBudget) {
        this.showBudget = showBudget;
    }

    public boolean isShowYearToDate() {
        return showYearToDate;
    }

    public void setShowYearToDate(boolean showYearToDate) {
        this.showYearToDate = showYearToDate;
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

    public Boolean getCosolidation() {
        return cosolidation;
    }

    public void setCosolidation(Boolean cosolidation) {
        this.cosolidation = cosolidation;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public String getDepartmentAndTreeChildIDs() {
        return departmentAndTreeChildIDs;
    }

    public void setDepartmentAndTreeChildIDs(String departmentAndTreeChildIDs) {
        this.departmentAndTreeChildIDs = departmentAndTreeChildIDs;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Integer getExchangeRateScale() {
        return exchangeRateScale;
    }

    public void setExchangeRateScale(Integer exchangeRateScale) {
        this.exchangeRateScale = exchangeRateScale;
    }

    public boolean isConsolidation() {
        return consolidation;
    }

    public void setConsolidation(boolean consolidation) {
        this.consolidation = consolidation;
    }

    public boolean isCompCurParentCompCurEquals() {
        return compCurParentCompCurEquals;
    }

    public void setCompCurParentCompCurEquals(boolean compCurParentCompCurEquals) {
        this.compCurParentCompCurEquals = compCurParentCompCurEquals;
    }
}
