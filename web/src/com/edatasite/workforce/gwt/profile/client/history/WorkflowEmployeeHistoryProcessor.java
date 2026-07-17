package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.WorkflowEmployeeSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

/**
 * Created by Azazello on 4/26/16.
 */
public class WorkflowEmployeeHistoryProcessor implements HistoryProcessor {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new WorkflowEmployeeSinksContainer("workflowEmployeeadd", params.length > 2 ? settingsStrings.editWorkflowEmployee() : settingsStrings.addWorkflowEmployee(), params);
    }
}
