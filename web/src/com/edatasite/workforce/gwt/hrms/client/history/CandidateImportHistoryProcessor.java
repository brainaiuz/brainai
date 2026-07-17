package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.CandidateImportSinksContainer;

/**
 * User: hayot
 * Date: 7/3/12
 * Time: 4:54 PM
 */
public class CandidateImportHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WfmMessages messages = WfmMessages.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new CandidateImportSinksContainer("importcandidateadd", messages.importEntity(wfmStrings.candidate()), params);
    }
}