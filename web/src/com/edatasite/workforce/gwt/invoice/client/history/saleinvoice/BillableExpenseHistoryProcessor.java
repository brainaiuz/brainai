package com.edatasite.workforce.gwt.invoice.client.history.saleinvoice;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.saleinvoice.BillableExpenseViewSinksContainer;

public class BillableExpenseHistoryProcessor implements HistoryProcessor {
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        SinksContainer billable = new BillableExpenseViewSinksContainer(containerName + strings[0], "Billable Expense", strings);
        billable.setCollapsed(true);
        return billable;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
