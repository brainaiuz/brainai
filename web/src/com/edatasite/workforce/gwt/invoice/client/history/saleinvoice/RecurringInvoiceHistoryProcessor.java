package com.edatasite.workforce.gwt.invoice.client.history.saleinvoice;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.saleinvoice.SaleInvoiceAddSinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.saleinvoice.SaleInvoiceViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 01.05.2010
 * Time: 19:35:40
 * To change this template use File | Settings | File Templates.
 */
public class RecurringInvoiceHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new SaleInvoiceViewSinksContainer(containerName + strings[0], Property.get(Constants.RECURRING_INVOICE, accountingStrings.recurringInvoice()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new SaleInvoiceAddSinksContainer("recurringinvoiceadd", Property.get(Constants.RECURRING_INVOICE, wfmStrings.addMess(), accountingStrings.recurringInvoice()), params);
    }
}
