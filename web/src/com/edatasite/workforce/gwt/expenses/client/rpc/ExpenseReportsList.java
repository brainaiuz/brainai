package com.edatasite.workforce.gwt.expenses.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ExpenseReportsList implements IsSerializable {

    private ExpenseReportsListItem[] result;
    private int totalCount;

    public ExpenseReportsList() {

    }

    public ExpenseReportsListItem[] getResult() {
        return result;
    }

    public void setResult(ExpenseReportsListItem[] result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ExpenseReportsList(ExpenseReportsListItem[] result, int totalCount) {
        this.result = result;
        this.totalCount = totalCount;
    }

    public ListData getListData() {
        return new ListData(result, totalCount);
    }
}
