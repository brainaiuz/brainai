package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.WorkflowSettingsSinksContainer;

/**
 * User: Hayot
 * Date: 28.02.2014
 * Time: 15:44:41
 */
public class WorkflowSettingsHistoryProcessor implements HistoryProcessor {
    private SettingStrings settingsStrings = SettingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new WorkflowSettingsSinksContainer(containerName + strings[0], settingsStrings.workflowSettings(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
