package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.TransferMoneyAddSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.TransferMoneyViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Feb 24, 2010
 * Time: 6:40:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransferMoneyHistoryProcessor implements HistoryProcessor {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new TransferMoneyViewSinksContainer(containerName + strings[0], accountingStrings.transferMoney(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new TransferMoneyAddSinksContainer("transferadd", accountingStrings.transferMoney(), params);
    }
}
