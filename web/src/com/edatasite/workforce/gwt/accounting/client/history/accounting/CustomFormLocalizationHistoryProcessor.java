package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.CustomFormLocalizationSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class CustomFormLocalizationHistoryProcessor implements HistoryProcessor {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] params) {
        return new CustomFormLocalizationSinksContainer(containerName + params[0], wfmStrings.localization(), params);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
