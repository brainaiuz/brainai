package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.AddEditDynamicLoginSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class DynamicLoginHistoryProcessor implements HistoryProcessor {
    private static final BackendStrings backendStrings = BackendStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new AddEditDynamicLoginSinksContainer("dynamicLoginadd", backendStrings.addDynamicLogin(), params);
    }

}
