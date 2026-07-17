package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.WorkflowEmployeeStepSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * Created by Azazello on 7/19/15.
 */
public class WorkflowEmployeeStepHistoryProcessor implements HistoryProcessor {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        String name = null;
        if (params.length > 4) {
            name = params[3];
        } else if (params.length > 5) {
            name = params[4];
        }
        return new WorkflowEmployeeStepSinksContainer("employeeStepadd", name != null ? name : hrmsStrings.employeeStep(), params);
    }
}
