package com.edatasite.workforce.gwt.invoice.client.history.purchaseorder;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.purchaseorder.ImportPurchaseOrderSinksContainer;

public class ImportPurchaseOrderHistoryProcessor implements HistoryProcessor {
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ImportPurchaseOrderSinksContainer("importPurchaseOrderAdd", "Import Purchase Orders", params);
    }
}
