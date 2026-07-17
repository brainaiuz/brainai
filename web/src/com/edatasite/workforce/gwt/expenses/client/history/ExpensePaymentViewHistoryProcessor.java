package com.edatasite.workforce.gwt.expenses.client.history;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.expenses.client.sinks.ExpensePaymentViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Marat
 * Date: 26.03.12
 * Time: 11:28
 * To change this template use File | Settings | File Templates.
 */
public class ExpensePaymentViewHistoryProcessor implements HistoryProcessor{

    private final AccountingStrings accountingStrings = AccountingStrings.App.get();


    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ExpensePaymentViewSinksContainer(containerName + strings[0], accountingStrings.expenseClaimsPaymentView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
