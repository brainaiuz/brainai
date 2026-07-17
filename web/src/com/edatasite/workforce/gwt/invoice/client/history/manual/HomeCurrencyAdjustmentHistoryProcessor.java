package com.edatasite.workforce.gwt.invoice.client.history.manual;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.manual.HomeCurrencyAdjustmentSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/3/12
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class HomeCurrencyAdjustmentHistoryProcessor implements HistoryProcessor{

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new HomeCurrencyAdjustmentSinksContainer("homecurrencyadjustmentadd", accountingStrings.homeCurrencyAdjustment(), params);
    }
}
