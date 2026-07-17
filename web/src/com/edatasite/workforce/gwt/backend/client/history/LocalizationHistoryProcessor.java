package com.edatasite.workforce.gwt.backend.client.history;


import com.edatasite.workforce.gwt.backend.client.LocalizationPropertySinksContainer;
import com.edatasite.workforce.gwt.backend.client.LocalizationSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class LocalizationHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final BackendStrings backendStrings = BackendStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new LocalizationPropertySinksContainer("localizationPropertyView", backendStrings.localizationProperty(), false);
    }

    public SinksContainer processAdd(String[] params) {
        String title = wfmStrings.add();

        if (params.length > 0 && params[1].substring(0, 1).matches("[0-9]")) {
            title = wfmStrings.edit();
        }
        return new LocalizationSinksContainer("localizationadd", title, params);
    }
}