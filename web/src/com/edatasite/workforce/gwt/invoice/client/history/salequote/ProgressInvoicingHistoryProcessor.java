package com.edatasite.workforce.gwt.invoice.client.history.salequote;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.salequote.ProgressInvoicingSinksContainer;
import com.google.gwt.core.client.GWT;

public class ProgressInvoicingHistoryProcessor implements HistoryProcessor {
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        GWT.log("name : " + containerName + strings[0]);
        return new ProgressInvoicingSinksContainer(containerName + strings[0], "Progress Invoicing", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
