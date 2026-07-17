package com.edatasite.workforce.gwt.invoice.client.history.expense;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.expense.ProjectBaseExpenseAddSinksContainer;

/**
 * Created by Normurod on 10/23/2016.
 */
public class ProjectBaseExpenseHistoryProcessor implements HistoryProcessor {

    private final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ProjectBaseExpenseAddSinksContainer("projectBaseExpenseadd", wfmStrings.projectBaseExpense(), params);
    }
}
