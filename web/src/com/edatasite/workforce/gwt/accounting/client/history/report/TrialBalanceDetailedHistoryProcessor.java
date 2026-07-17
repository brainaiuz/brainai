package com.edatasite.workforce.gwt.accounting.client.history.report;

import com.edatasite.workforce.gwt.accounting.client.container.report.TrialBalanceDetailedSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class TrialBalanceDetailedHistoryProcessor implements HistoryProcessor {
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new TrialBalanceDetailedSinksContainer(containerName + strings[0], "Trial Balance (Detailed)", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
