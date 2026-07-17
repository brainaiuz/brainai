package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.TelegramBotSettingAddSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

public class TelegramBotSettingHistoryProcessor implements HistoryProcessor {
    private SettingStrings settingsStrings = SettingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new TelegramBotSettingAddSinksContainer("telegramsettingadd", params.length > 1 ? settingsStrings.editTelegramBot() : settingsStrings.addTelegramBot(), params);
    }
}
