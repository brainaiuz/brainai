package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.utils.OvertimeAddSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.utils.OvertimeViewSinksContainer;

public class OvertimeHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new OvertimeViewSinksContainer(containerName + strings[0], wfmStrings.overtime() + " " + wfmStrings.viewOnly(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new OvertimeAddSinksContainer("overtimeadd", wfmStrings.add() + " " + wfmStrings.overtime(), params);
    }
}
