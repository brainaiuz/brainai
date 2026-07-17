package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.HrmsSettingsSinksContainer;

/**
 * User: Ilhombek
 * Date: Mar 29, 2010
 * Time: 16:46:36 PM
 */
public class HrmsSettingsHistoryProcessor implements HistoryProcessor {

    private final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new HrmsSettingsSinksContainer(containerName + strings[0], wfmStrings.hrms(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}