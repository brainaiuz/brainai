package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.DiscountAddSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.DiscountSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 3, 2010
 * Time: 6:37:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscountHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new DiscountSinksContainer(containerName + strings[0], accountingStrings.discountDetails(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new DiscountAddSinksContainer("discountadd", accountingStrings.addDiscount(), params);
    }
}
