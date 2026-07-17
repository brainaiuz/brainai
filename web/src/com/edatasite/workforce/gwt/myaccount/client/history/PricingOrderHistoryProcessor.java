package com.edatasite.workforce.gwt.myaccount.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.myaccount.client.PricingOrderSinksContainer;
import com.edatasite.workforce.gwt.myaccount.client.localization.MyAccountStrings;

/**
 * User: Dilshod Madrahimov
 * Date: 1/28/12
 * Time: 4:55 PM
 */
public class PricingOrderHistoryProcessor implements HistoryProcessor {

    private static final MyAccountStrings myAccountStrings = MyAccountStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new PricingOrderSinksContainer(containerName + strings[0], myAccountStrings.orderSummary(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
