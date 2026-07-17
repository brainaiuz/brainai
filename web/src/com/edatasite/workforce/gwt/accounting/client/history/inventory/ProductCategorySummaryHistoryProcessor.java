package com.edatasite.workforce.gwt.accounting.client.history.inventory;

import com.edatasite.workforce.gwt.accounting.client.container.inventory.ProductCategorySummarySinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class ProductCategorySummaryHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(final String containerName, final String[] strings)//must be ---> strings.length<=3
    {
        return new ProductCategorySummarySinksContainer(containerName + strings[0], accountingStrings.categoryView(), strings);
    }

    public SinksContainer processAdd(final String[] params) {
        return null;
    }
}