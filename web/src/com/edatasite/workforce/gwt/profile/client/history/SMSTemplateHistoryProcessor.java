package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.SMSTemplateAddSinksContainer;
import com.edatasite.workforce.gwt.profile.client.SMSTemplateViewSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

/**
 * Created by Azazello on 4/21/15.
 */
public class SMSTemplateHistoryProcessor implements HistoryProcessor {
    private final SettingStrings settingsStrings = SettingStrings.App.get();
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new SMSTemplateViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new SMSTemplateAddSinksContainer("smstemplateadd", settingsStrings.addSMSTemplate(), params);
    }
}
