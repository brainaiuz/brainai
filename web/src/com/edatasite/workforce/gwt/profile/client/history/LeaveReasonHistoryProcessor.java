package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.LeaveReasonAddSinksContainer;
import com.edatasite.workforce.gwt.profile.client.LeaveReasonSinksContainer;

/**
 * @author Hurshid on 12/17/2018
 */
public class LeaveReasonHistoryProcessor implements HistoryProcessor {

    private final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new LeaveReasonSinksContainer(containerName + strings[0], "".equals(strings[0]) ? wfmStrings.reference() : wfmStrings.editReference(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new LeaveReasonAddSinksContainer("leaveReasonadd", wfmStrings.add() + " " + wfmStrings.reason(), params);
    }
}
