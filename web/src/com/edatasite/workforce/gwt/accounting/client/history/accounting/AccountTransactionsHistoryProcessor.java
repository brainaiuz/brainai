package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.AccountTransactionsSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 15.05.2010
 * Time: 18:02:20
 * To change this template use File | Settings | File Templates.
 */
public class AccountTransactionsHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new AccountTransactionsSinksContainer(containerName + strings[0], Property.get(Constants.BANKACCOUNT, accountingStrings.bankAccountTransactions(), wfmStrings.bankAccount()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
