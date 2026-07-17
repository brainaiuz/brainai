package com.edatasite.workforce.gwt.expenses.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.expenses.client.ExpenseAddSinksContainer;
import com.edatasite.workforce.gwt.expenses.client.ExpenseEditSinksContainer;

public class ExpenseReportHistoryProcessor implements HistoryProcessor {

    private final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {

        return new ExpenseEditSinksContainer(containerName + strings[0], Property.getPluralWithObjectCode(Constants.EXPENSES_CLAIM, wfmStrings.expenseClaims()), strings);
    }

    public SinksContainer processAdd(String[] params) {

        return new ExpenseAddSinksContainer("expenseReportsadd", Property.get(Constants.EXPENSES_CLAIM, wfmStrings.addMess(), wfmStrings.expenseClaim()), params);
    }

}
