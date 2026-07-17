package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.SignupCompanySettingsSinkContainer;

/**
 * Created by Omonullo on 12/28/2016.
 */
public class SignupCompanyHistoryProcessor implements HistoryProcessor {

    private WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new SignupCompanySettingsSinkContainer(containerName + strings[0], wfmStrings.company(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
