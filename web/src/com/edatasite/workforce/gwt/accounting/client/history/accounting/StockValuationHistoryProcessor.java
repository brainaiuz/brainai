package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.StockValuationSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class StockValuationHistoryProcessor implements HistoryProcessor {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new StockValuationSinksContainer(containerName + strings[0], accountingStrings.stockValuation(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
