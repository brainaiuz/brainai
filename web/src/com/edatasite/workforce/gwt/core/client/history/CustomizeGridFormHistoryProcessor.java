package com.edatasite.workforce.gwt.core.client.history;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.CustomizeGridFormSinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class CustomizeGridFormHistoryProcessor implements HistoryProcessor {

    private WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new CustomizeGridFormSinksContainer("customizeForm2add", wfmStrings.customizeForm(), params);
    }

}
