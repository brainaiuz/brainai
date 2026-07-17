package com.edatasite.workforce.gwt.assessment.client.ui.history;

import com.edatasite.workforce.gwt.assessment.client.AssessSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class AssessHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] params) {
        return new AssessSinksContainer(containerName + params[0], wfmStrings.assessment(), params);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }

}
