package com.edatasite.workforce.gwt.invoice.client.history.saleinvoice;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.saleinvoice.ImportCustomInvoicesSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/3/14
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportCustomInvoicesHistoryProcessor implements HistoryProcessor{

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ImportCustomInvoicesSinksContainer("importcustominvoiceadd", "Import Custom Invoices", params);
    }
}
