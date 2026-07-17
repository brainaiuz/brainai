package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.WorkflowTelegramAlertAddSinksContainer;

public class WorkflowTelegramAlertHistoryProcessor implements HistoryProcessor {
    private SettingStrings settingsStrings = SettingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        String description = "";
        if (params != null && params.length > 2 && ("null").equals(params[1])) {
            description = settingsStrings.addTelegramAlert();
        } else {
            description = settingsStrings.editTelegramAlert();
        }
        return new WorkflowTelegramAlertAddSinksContainer("workflowtelegramalertadd", description, params);
    }
}
