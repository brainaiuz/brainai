package com.edatasite.workforce.gwt.invoice.client.history.saleorderbaseinvoice;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.saleorderbaseinvoice.SaleOrderBaseInvoiceAddSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 13.05.2009
 * Time: 13:10:08
 * To change this template use File | Settings | File Templates.
 */
public class SaleOrderBaseInvoiceHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new SaleOrderBaseInvoiceAddSinksContainer("saleorderBaseInvoiceadd", "Sale Order Base Invoice", params);
    }
}
