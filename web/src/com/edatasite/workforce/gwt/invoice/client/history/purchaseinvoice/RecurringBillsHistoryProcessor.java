package com.edatasite.workforce.gwt.invoice.client.history.purchaseinvoice;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.purchaseinvoice.PurchaseInvoiceAddSinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.purchaseinvoice.PurchaseInvoiceViewSinksContainer;

/**
 * Created by Sherzod on 6/18/2015.
 */
public class RecurringBillsHistoryProcessor implements HistoryProcessor{

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new PurchaseInvoiceViewSinksContainer(containerName + strings[0], Property.getPluralWithObjectCode(Constants.RECURRING_BILL, wfmStrings.recurringBills()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new PurchaseInvoiceAddSinksContainer("recurringbilladd", Property.get(Constants.RECURRING_BILL, wfmStrings.addMess(), accountingStrings.recurringBill()), params);
    }
}