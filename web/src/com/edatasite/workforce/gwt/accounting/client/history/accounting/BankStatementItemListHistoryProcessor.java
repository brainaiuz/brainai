package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.BankStatementItemListSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * User: Dilshod Madrahimov
 * Date: 28.11.2015
 */
public class BankStatementItemListHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new BankStatementItemListSinksContainer(containerName + strings[0], accountingStrings.bankStatementItems(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new BankStatementItemListSinksContainer("bankStatementItemList", accountingStrings.bankStatementItems(), params);
    }
}
