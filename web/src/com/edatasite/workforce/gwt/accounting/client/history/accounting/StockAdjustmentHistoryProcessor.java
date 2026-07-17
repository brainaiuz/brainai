package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.StockAdjustmentAddSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.StockAdjustmentViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/11/13
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class StockAdjustmentHistoryProcessor implements HistoryProcessor{

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new StockAdjustmentViewSinksContainer(containerName + strings[0], accountingStrings.adjustStockQuantity(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new StockAdjustmentAddSinksContainer("stockadjustmentadd", accountingStrings.adjustStockQuantity(), params);
    }
}
