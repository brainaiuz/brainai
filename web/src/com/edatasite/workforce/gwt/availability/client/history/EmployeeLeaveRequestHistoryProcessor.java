package com.edatasite.workforce.gwt.availability.client.history;

import com.edatasite.workforce.gwt.availability.client.EmployeeLeaveRequestSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * User: Abror Abdukadirov
 * Date: 17.03.2018 14:36
 */
public class EmployeeLeaveRequestHistoryProcessor implements HistoryProcessor {

    
    private static WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new EmployeeLeaveRequestSinksContainer(containerName + strings[0], wfmStrings.leaveRequests(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
