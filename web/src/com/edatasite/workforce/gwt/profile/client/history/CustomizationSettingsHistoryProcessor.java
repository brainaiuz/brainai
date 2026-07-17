package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.CustomizationSettingsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

public class CustomizationSettingsHistoryProcessor implements HistoryProcessor {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new CustomizationSettingsSinksContainer(containerName + strings[0], settingsStrings.customization(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}