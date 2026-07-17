package com.edatasite.workforce.gwt.core.client.history;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.CustomizeFormSinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class CustomizeFormHistoryProcessor implements HistoryProcessor {

    private WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new CustomizeFormSinksContainer("customizeFormadd", wfmStrings.customizeForm(), params);
    }

}
