package com.edatasite.workforce.gwt.accounting.client.ui.view.balancesheet;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Normurod on 2/22/2017.
 */
public class BalancesheetSettings implements IsSerializable {

    private boolean includeCurrentYearInRetained;
    private BalancesheetSettingsItem[] settings;

    public BalancesheetSettings() {
    }

    public boolean isIncludeCurrentYearInRetained() {
        return includeCurrentYearInRetained;
    }

    public void setIncludeCurrentYearInRetained(boolean includeCurrentYearInRetained) {
        this.includeCurrentYearInRetained = includeCurrentYearInRetained;
    }

    public BalancesheetSettingsItem[] getSettings() {
        return settings;
    }

    public void setSettings(BalancesheetSettingsItem[] settings) {
        this.settings = settings;
    }
}
