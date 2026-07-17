package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.AsteriskEmployeeAddSinksContainer;

/**
 * Created by Anvar Akramov on 7/4/2020.
 */
public class AsteriskEmployeeHistoryProcessor implements HistoryProcessor {
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new AsteriskEmployeeAddSinksContainer("asteriskEmployeeSettings", wfmStrings.settings(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new AsteriskEmployeeAddSinksContainer("asteriskEmployeeSettingsadd", wfmStrings.settings(), params);
    }
}
