package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.EmployeeLeaveTypesSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * User: Admin
 * Date: 16.12.2009
 * Time: 16:34:54
 */
public class EmployeeLeaveTypesHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new EmployeeLeaveTypesSinksContainer(containerName + strings[0], hrmsStrings.employeeLeaveTypes(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new EmployeeLeaveTypesSinksContainer("employeetypeview", hrmsStrings.employeeLeaveTypes(), params);
    }
}