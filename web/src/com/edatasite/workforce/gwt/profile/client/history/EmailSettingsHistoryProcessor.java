package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.EmailSettingsSinksContainer;

/**
 * Created  IntelliJ IDEA.
 * User: Admin
 * Date: 15.03.2010
 * Time: 12:50:11
 */
public class EmailSettingsHistoryProcessor implements HistoryProcessor {
    private WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new EmailSettingsSinksContainer(containerName + strings[0], wfmStrings.emailTemplates(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
