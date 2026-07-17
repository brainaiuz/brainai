package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.AddWageRateSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

/**
 * User : Akhror on 13/03/2024
 */
public class WageRateHistoryProcessor implements HistoryProcessor {
    private static final SettingStrings settingStrings = SettingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] params) {
        return new AddWageRateSinksContainer("wageRateAdd", settingStrings.addWageRate(), params);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new AddWageRateSinksContainer("minimumWageadd", settingStrings.addWageRate(), params);
    }
}
