package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.AddProductRentalSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.ProductRentalViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Iftixor
 * Date: 09.08.2021
 * Time: 12:10:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddProductRentalHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new ProductRentalViewSinksContainer(containerName + strings[0], Property.get(Constants.RENTAL_PRODUCTS, wfmStrings.summaryView(), accountingStrings.rentalItem()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new AddProductRentalSinksContainer("productrentaladd", Property.get(Constants.RENTAL_PRODUCTS, accountingStrings.addProduct(), accountingStrings.rentalItem()), params);
    }
}
