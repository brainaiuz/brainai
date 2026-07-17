package com.edatasite.workforce.gwt.project.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.ContractAddSinksContainer;
import com.edatasite.workforce.gwt.project.client.ContractViewSinksContainer;

public class ContractHistoryProcessor implements HistoryProcessor {

    private WfmStrings wfmStrings = WfmStrings.App.get();

    public ContractHistoryProcessor() {
    }

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ContractViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ContractAddSinksContainer("contractadd", wfmStrings.addContract(), params);
    }
}