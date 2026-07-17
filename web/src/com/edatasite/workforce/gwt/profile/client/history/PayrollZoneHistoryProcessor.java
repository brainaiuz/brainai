package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.AddPayrollZoneSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

/**
 * User : Akhror on 11/03/2024
 */
public class PayrollZoneHistoryProcessor implements HistoryProcessor {
    private static final SettingStrings settingStrings = SettingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] params) {
        return new AddPayrollZoneSinksContainer("payrollzoneadd", settingStrings.addPayrollZone(), params);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new AddPayrollZoneSinksContainer("payrollzoneadd", settingStrings.addPayrollZone(), params);
    }
}