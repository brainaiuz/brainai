package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.inventory.PaymentMethodListSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by Dilsh0d Madrahimov on 9/16/2016 9:47 PM.
 */
public class PaymentMethodListHistoryProcessor implements HistoryProcessor {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new PaymentMethodListSinksContainer(containerName + strings[0], accountingStrings.paymentMethods(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new PaymentMethodListSinksContainer("paymentmethodlist", accountingStrings.paymentMethods(), params);
    }
}
