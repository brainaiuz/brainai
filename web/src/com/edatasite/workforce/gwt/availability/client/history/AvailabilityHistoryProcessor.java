package com.edatasite.workforce.gwt.availability.client.history;

import com.edatasite.workforce.gwt.availability.client.AvailabilitySinksContainer;
import com.edatasite.workforce.gwt.availability.client.LeaveRequestAddSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class AvailabilityHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new AvailabilitySinksContainer(containerName + strings[0], wfmStrings.attendanceTracking(), strings);

    }

    public SinksContainer processAdd(String[] params) {
        return new LeaveRequestAddSinksContainer("availabilityadd", wfmStrings.addRequest(), params);
    }
}
