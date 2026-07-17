package com.edatasite.workforce.gwt.profile.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.FinancialSettingsItem;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public abstract class VATSettingsWidget extends Composite {

    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    public VATSettingsWidget() {
        initWidget(getMainWidget());
        initialize();
    }

    protected abstract Widget getMainWidget();

    protected abstract void initialize();

    public abstract void fillSettings(FinancialSettingsItem settingsItem);

    public abstract FinancialSettingsItem getSettingsData(FinancialSettingsItem settingsItem);

    public abstract boolean validate();

    public abstract void initSpecificSettings(Command command);
}
