package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.SystemLogsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

public class SystemLogsHistoryProcessor implements HistoryProcessor {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new SystemLogsSinksContainer(containerName + strings[0], settingsStrings.systemLogs(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
