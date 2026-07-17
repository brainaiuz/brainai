package com.edatasite.workforce.gwt.assessment.client.ui.history;

import com.edatasite.workforce.gwt.assessment.client.InitiateAddSinksContainer;
import com.edatasite.workforce.gwt.assessment.client.InitiateSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

public class InitiateHistoryProcessor implements HistoryProcessor {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new InitiateSinksContainer(containerName + strings[0], hrmsStrings.initiate());

    }

    public SinksContainer processAdd(String[] params) {
        return new InitiateAddSinksContainer("initiateadd", hrmsStrings.initiate(), params);
    }

}
