package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.CompanySettingsSinksContainer;

/**
 * User: Ilhombek
 * Date: 17.03.2010
 * Time: 15:44:41
 */
public class CompanySettingsHistoryProcessor implements HistoryProcessor {
    private WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new CompanySettingsSinksContainer(containerName + strings[0], wfmStrings.company(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
