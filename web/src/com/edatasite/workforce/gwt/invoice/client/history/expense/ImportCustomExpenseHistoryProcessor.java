package com.edatasite.workforce.gwt.invoice.client.history.expense;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.expense.ImportCustomExpenseSinksContainer;

/**
 * Created by Khasan on 14.08.14.
 */
public class ImportCustomExpenseHistoryProcessor implements HistoryProcessor {


    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ImportCustomExpenseSinksContainer("importcustomexpenseadd", WfmStrings.App.get().importExpense(), params);
    }
}
