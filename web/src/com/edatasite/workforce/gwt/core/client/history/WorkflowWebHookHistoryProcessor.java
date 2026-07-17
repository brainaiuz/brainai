package com.edatasite.workforce.gwt.core.client.history;

import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WorkflowWebHookAddSinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WorkflowWebHookViewSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

/**
 * User : Akhror
 * Date : 10.01.2022
 */
public class WorkflowWebHookHistoryProcessor implements HistoryProcessor {
    private final SettingStrings settingsStrings = SettingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new WorkflowWebHookViewSinksContainer(containerName + strings[0], "View Workflow Webhook", strings);
    }

    public SinksContainer processAdd(String[] params) {
        String description;
        if (params != null && params.length > 2 && ("null").equals(params[1])) {
            description = settingsStrings.addWorkflowWebHook();
        } else {
            description = settingsStrings.editWorkflowWebHook();
        }
        return new WorkflowWebHookAddSinksContainer("webhookadd", description, params);
    }
}
