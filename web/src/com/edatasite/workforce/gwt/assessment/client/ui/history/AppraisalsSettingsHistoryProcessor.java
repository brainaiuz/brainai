package com.edatasite.workforce.gwt.assessment.client.ui.history;

import com.edatasite.workforce.gwt.assessment.client.AppraisalsSettingsAddSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * User: Sherali Pirnafasov
 */
public class AppraisalsSettingsHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new AppraisalsSettingsAddSinksContainer("appraisalssettingsadd", wfmStrings.appraisalsSettings());
    }
}
