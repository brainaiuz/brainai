package com.edatasite.workforce.gwt.employee.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.EmployeeAddSinksContainer;
import com.edatasite.workforce.gwt.employee.client.EmployeeViewSinksContainer;

public class EmployeeHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new EmployeeViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new EmployeeAddSinksContainer("employeeadd", wfmStrings.addEmployee(), params);
    }

}
