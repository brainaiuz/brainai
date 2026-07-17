package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.TransactionItemViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Power
 * Date: 11/29/12
 * Time: 6:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransactionItemViewHistoryProcessor implements HistoryProcessor {

    private final AccountingStrings accountingStrings = AccountingStrings.App.get();


    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new TransactionItemViewSinksContainer(containerName + strings[0], accountingStrings.transaction(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}