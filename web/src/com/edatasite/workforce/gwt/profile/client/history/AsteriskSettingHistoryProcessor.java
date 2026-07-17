package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.AsteriskSettingAddSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

/**
 * Created by Anvar Akramov on 7/4/2020.
 */
public class AsteriskSettingHistoryProcessor implements HistoryProcessor {
    private SettingStrings settingsStrings = SettingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new AsteriskSettingAddSinksContainer("asteriskSettingadd", params.length > 1 ? settingsStrings.editAsteriskAccount() : settingsStrings.addAsteriskAccount(), params);
    }
}
