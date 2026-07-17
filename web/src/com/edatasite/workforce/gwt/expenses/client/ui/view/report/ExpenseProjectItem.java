package com.edatasite.workforce.gwt.expenses.client.ui.view.report;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.Date;

/**
 * Created by Normurod on 10/23/2016.
 */
public class ExpenseProjectItem extends SelectItem {

    private Date lastExpenseReportedDate;
    private SelectItem customer;

    public Date getLastExpenseReportedDate() {
        return lastExpenseReportedDate;
    }

    public void setLastExpenseReportedDate(Date lastExpenseReportedDate) {
        this.lastExpenseReportedDate = lastExpenseReportedDate;
    }

    public SelectItem getCustomer() {
        return customer;
    }

    public void setCustomer(SelectItem customer) {
        this.customer = customer;
    }
}
