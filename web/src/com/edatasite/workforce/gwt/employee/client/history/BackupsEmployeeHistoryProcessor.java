package com.edatasite.workforce.gwt.employee.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.BackupsEmployeeAddSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.BackupsEmployeeViewSinksContainer;

public class BackupsEmployeeHistoryProcessor implements HistoryProcessor {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new BackupsEmployeeViewSinksContainer("backupsemployeeview", wfmStrings.backupEmployee(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new BackupsEmployeeAddSinksContainer("backupsemployeeadd", wfmStrings.backupEmployee(), params);
    }
}
