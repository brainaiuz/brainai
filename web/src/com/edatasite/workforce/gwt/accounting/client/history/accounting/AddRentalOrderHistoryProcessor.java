package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.AddRentalOrderSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.RentalOrderViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class AddRentalOrderHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new RentalOrderViewSinksContainer(containerName + strings[0], Property.get(Constants.RENTAL_ORDERS, wfmStrings.summaryView(), accountingStrings.rentalOrder()), strings);

    }

    public SinksContainer processAdd(String[] params) {
        return new AddRentalOrderSinksContainer("rentalorderadd", Property.get(Constants.RENTAL_ORDERS, wfmStrings.addRentalOrder(), accountingStrings.rentalOrder()), params);
    }
}
