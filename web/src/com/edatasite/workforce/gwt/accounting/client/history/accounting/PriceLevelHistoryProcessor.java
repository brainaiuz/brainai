package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.PriceLevelAddSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.PriceLevelSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 25, 2011
 * Time: 11:33:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class PriceLevelHistoryProcessor implements HistoryProcessor {

    private static AccountingStrings accountingStrings = AccountingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new PriceLevelSinksContainer(containerName + strings[0], accountingStrings.priceLevelDetails(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new PriceLevelAddSinksContainer("priceLeveladd", accountingStrings.addPriceLevel(), params);
    }
}
