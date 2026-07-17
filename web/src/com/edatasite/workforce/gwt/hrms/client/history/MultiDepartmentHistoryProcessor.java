package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.MultiDepartmentSinksContainer;

public class MultiDepartmentHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new MultiDepartmentSinksContainer(containerName + strings[0], wfmStrings.add() + " " + wfmStrings.department(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new MultiDepartmentSinksContainer("multidepartmentadd", wfmStrings.add() + " " + wfmStrings.department(), params);
    }
}
