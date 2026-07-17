package com.edatasite.workforce.gwt.availability.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.AttendanceReportViewSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

public class AttendanceReportHistoryProcessor implements HistoryProcessor {
    private HrmsStrings wfmStrings = HrmsStrings.App.get();
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new AttendanceReportViewSinksContainer(containerName + strings[0], strings[2], strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
