package com.edatasite.workforce.gwt.availability.client.history;

import com.edatasite.workforce.gwt.availability.client.AttendanceTerminalAddSinksContainer;
import com.edatasite.workforce.gwt.availability.client.AttendanceTerminalSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class AttendanceTerminalHistoryProcessor implements HistoryProcessor {
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new AttendanceTerminalSinksContainer(containerName + strings[0], wfmStrings.attendanceTerminal(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new AttendanceTerminalAddSinksContainer("attendanceTerminaladd", wfmStrings.attendanceTerminal(), params);
    }
}
