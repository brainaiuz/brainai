package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class ImportPaymentDeductionHistoryProcessor implements HistoryProcessor {
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ImportPaymentDeductionSinksContainer("importpaymentdeductionadd", "Import Payment/Deduction", params);
    }
}
