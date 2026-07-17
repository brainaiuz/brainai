package com.edatasite.workforce.gwt.accounting.client.rpc.consignment;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Omonullo on 5/18/2017.
 */
public class TrialBalanceFilter implements IsSerializable {
    DateNonConvertable startDate;
    DateNonConvertable toDate;
    String sortField;
    Integer showValues;
    Integer departmentID;
    String sortDirection;
    Integer currencyId;
    boolean consolidation;
    boolean summary;

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public DateNonConvertable getToDate() {
        return toDate;
    }

    public void setToDate(DateNonConvertable toDate) {
        this.toDate = toDate;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public boolean isConsolidation() {
        return consolidation;
    }

    public void setConsolidation(boolean consolidation) {
        this.consolidation = consolidation;
    }

    public Integer getShowValues() {
        return showValues;
    }

    public void setShowValues(Integer showValues) {
        this.showValues = showValues;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public boolean isSummary() {
        return summary;
    }

    public void setSummary(boolean summary) {
        this.summary = summary;
    }
}
