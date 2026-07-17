package com.edatasite.workforce.gwt.accounting.client.history.report;

import com.edatasite.workforce.gwt.accounting.client.container.report.VATReturnReportSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class VATReturnHistoryProcessor implements HistoryProcessor {
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new VATReturnReportSinksContainer(containerName + strings[0], "VAT Return", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
