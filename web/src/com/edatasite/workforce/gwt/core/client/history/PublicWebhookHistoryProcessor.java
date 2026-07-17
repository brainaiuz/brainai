package com.edatasite.workforce.gwt.core.client.history;

import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.PublicWebHookAddSinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.PublicWebHookViewSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

/**
 * User : Akhror
 * Date : 13.03.2025
 */
public class PublicWebhookHistoryProcessor implements HistoryProcessor {
    private final SettingStrings settingsStrings = SettingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new PublicWebHookViewSinksContainer(containerName + strings[0], "View Webhook", strings);
    }

    public SinksContainer processAdd(String[] params) {
        String description;
        if (params != null && params.length > 2 && ("null").equals(params[1])) {
            description = settingsStrings.addWorkflowWebHook();
        } else {
            description = settingsStrings.editWorkflowWebHook();
        }
        return new PublicWebHookAddSinksContainer("webhookadd", description, params);
    }
}
