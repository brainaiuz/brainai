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
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 06.03.2009
 * Time: 15:43:50
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseInvoiceHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new PurchaseInvoiceViewSinksContainer(containerName + strings[0], Property.get(Constants.PURCHASE_INVOICE, wfmStrings.summaryView(), wfmStrings.purchaseinvoice()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new PurchaseInvoiceAddSinksContainer("purchaseinvoiceadd", Property.get(Constants.PURCHASE_INVOICE, wfmStrings.addValue(), wfmStrings.purchaseinvoice()), params);
    }
}
