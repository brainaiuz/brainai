package com.edatasite.workforce.gwt.availability.client.history;

import com.edatasite.workforce.gwt.availability.client.ShiftSettingsAddSinksContainer;
import com.edatasite.workforce.gwt.availability.client.ShiftSettingsSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

public class ShiftSettingsHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ShiftSettingsSinksContainer("shiftsettings", hrmsStrings.shiftSettings(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ShiftSettingsAddSinksContainer("shiftsettingsadd", hrmsStrings.shiftSettings(), params);
    }
}