package com.edatasite.workforce.gwt.availability.client.history;

import com.edatasite.workforce.gwt.availability.client.TimeslotAddSinksContainer;
import com.edatasite.workforce.gwt.availability.client.TimeslotSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class TimeslotHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new TimeslotSinksContainer(containerName + strings[0], hrmsStrings.timeslotSummary(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new TimeslotAddSinksContainer("timeslotadd", hrmsStrings.addTimeSlot(), null);
    }
}