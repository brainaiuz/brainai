package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.RotationAddSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.RotationSummarySinksConatiner;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

public class RotationHistoryProcessor implements HistoryProcessor {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new RotationSummarySinksConatiner("viewRotation", wfmStrings.summaryView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new RotationAddSinksContainer("addRotation", hrmsStrings.addRotation(), params);
    }
}
