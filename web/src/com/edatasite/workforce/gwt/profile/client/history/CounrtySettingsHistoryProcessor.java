package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.CountrySettingsSinksContainer;

/**
 * User: Faxriddin * Date: 01/26/2016
 */
public class CounrtySettingsHistoryProcessor implements HistoryProcessor {

    private WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new CountrySettingsSinksContainer(containerName + strings[0], wfmStrings.countrySettings(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new CountrySettingsSinksContainer("referenceadd", wfmStrings.addReference(), params);
    }
}
