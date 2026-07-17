package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.ProductionSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class ProductionHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new ProductionSinksContainer(containerName + strings[0], accountingStrings.production());
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }


}
