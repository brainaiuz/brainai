package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.BankStatementSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 17.05.2010
 * Time: 20:21:35
 * To change this template use File | Settings | File Templates.
 */
public class BankStatementHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new BankStatementSinksContainer(containerName + strings[0], accountStrings.bankStatements(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
