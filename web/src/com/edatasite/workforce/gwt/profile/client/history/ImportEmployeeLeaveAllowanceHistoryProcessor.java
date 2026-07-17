package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ImportEmployeeLeaveAllowanceSinksContainer;

public class ImportEmployeeLeaveAllowanceHistoryProcessor implements HistoryProcessor {
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ImportEmployeeLeaveAllowanceSinksContainer("importemployeeallowanceadd", "Import Employee Allowance", params);
    }
}
