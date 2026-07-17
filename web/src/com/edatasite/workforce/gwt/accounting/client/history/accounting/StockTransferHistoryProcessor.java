package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.StockTransferAddSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.StockTransferViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by Dilshod Madrahimov on 2/26/15.
 */
public class StockTransferHistoryProcessor implements HistoryProcessor{

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new StockTransferViewSinksContainer(containerName + strings[0], accountingStrings.stockTransfer(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new StockTransferAddSinksContainer("stocktransferadd", accountingStrings.stockTransfer(), params);
    }
}
