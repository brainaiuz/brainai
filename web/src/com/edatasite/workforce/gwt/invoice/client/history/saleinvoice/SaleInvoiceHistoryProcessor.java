package com.edatasite.workforce.gwt.invoice.client.history.saleinvoice;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.saleinvoice.SaleInvoiceAddSinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.saleinvoice.SaleInvoiceViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 24.02.2009
 * Time: 14:19:09
 * To change this template use File | Settings | File Templates.
 */
public class SaleInvoiceHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new SaleInvoiceViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new SaleInvoiceAddSinksContainer("saleinvoiceadd", Property.get(Constants.SALE_INVOICE, wfmStrings.addMess(), wfmStrings.salesInvoice()), params);
    }
}
