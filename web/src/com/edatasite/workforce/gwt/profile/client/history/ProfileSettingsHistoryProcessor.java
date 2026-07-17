package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ProfileSettingsSinksContainer;

/**
 * User: Ilhombek
 * Date: 17.03.2010
 * Time: 15:29:19
 */
public class ProfileSettingsHistoryProcessor implements HistoryProcessor {
    private final WfmStrings wfmStrings = WfmStrings.App.get();


    public SinksContainer process(String containerName, String[] strings) {
        return new ProfileSettingsSinksContainer(containerName + strings[0], wfmStrings.contactprofile(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
