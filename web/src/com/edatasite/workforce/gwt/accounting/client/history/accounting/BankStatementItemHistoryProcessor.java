package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.BankStatementItemAddEditSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.BankStatementItemViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 17.05.2010
 * Time: 20:22:58
 * To change this template use File | Settings | File Templates.
 */
public class BankStatementItemHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new BankStatementItemViewSinksContainer(containerName + strings[0], accountingStrings.bankStatementItems(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new BankStatementItemAddEditSinksContainer("bankStatementItem", accountingStrings.bankStatementItems(), params);
    }
}
