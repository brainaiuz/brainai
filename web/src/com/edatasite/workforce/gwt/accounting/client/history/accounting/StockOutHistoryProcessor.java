package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.StockOutAddSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.StockOutViewSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;


public class StockOutHistoryProcessor implements HistoryProcessor{

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new StockOutViewSinksContainer(containerName + strings[0], wfmStrings.stockOut(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new StockOutAddSinksContainer("stockoutadd", wfmStrings.stockOut(), params);
    }
}
