package com.edatasite.workforce.gwt.accounting.client.history.inventory;

import com.edatasite.workforce.gwt.accounting.client.container.inventory.BrandsListSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 17, 2010
 * Time: 3:09:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class BrandsListHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new BrandsListSinksContainer(containerName + strings[0], accountingStrings.brands(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new BrandsListSinksContainer("brandlist", accountingStrings.brands(), params);
    }
}
