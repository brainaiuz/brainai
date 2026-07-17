package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.AddEditWhiteLabelSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class WhiteLabelHistoryProcessor implements HistoryProcessor {
    private static final BackendStrings backendStrings = BackendStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new AddEditWhiteLabelSinksContainer(containerName + strings[1], "whiteLabel", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new AddEditWhiteLabelSinksContainer("whiteLabeladd", "White Label Add", params);
    }
}
