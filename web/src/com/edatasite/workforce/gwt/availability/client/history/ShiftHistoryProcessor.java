package com.edatasite.workforce.gwt.availability.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ShiftAddSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ShiftViewSinksContainer;


public class ShiftHistoryProcessor implements HistoryProcessor {
    private WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ShiftViewSinksContainer(containerName + strings[1], wfmStrings.shift(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ShiftAddSinksContainer("shiftadd", wfmStrings.shift(), params);
    }
}
