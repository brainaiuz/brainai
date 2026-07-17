package com.edatasite.workforce.gwt.core.client.history;

import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WorkflowWebHookEditSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

public class WorkflowWebHookEditHistoryProcessor implements HistoryProcessor{
    private final SettingStrings settingsStrings = SettingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new WorkflowWebHookEditSinksContainer(containerName + strings[0], settingsStrings.editWorkflowWebHook(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
