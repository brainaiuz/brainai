package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.PMSettingsSinksContainer;

/**
 * User: Ilhombek
 * Date: 12.07.2010
 * Time: 13:10:32
 */
public class PMSettingsHistoryProcessor implements HistoryProcessor {
    private WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new PMSettingsSinksContainer(containerName + strings[0], wfmStrings.projectManagementSettings(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
