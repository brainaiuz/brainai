package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.EmailTemplateViewSinksContainer;
import com.edatasite.workforce.gwt.profile.client.EmailTemplatesAddSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

/**
 * Created by IntelliJ IDEA.
 * User: muratov
 * Date: Mar 17, 2010
 * Time: 9:02:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailTemplateHistoryProcessor implements HistoryProcessor {
    private SettingStrings settingsStrings = SettingStrings.App.get();
    private WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new EmailTemplateViewSinksContainer(containerName + strings[0], settingsStrings.emailTemplateView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new EmailTemplatesAddSinksContainer("templateadd", wfmStrings.addEmailTemplate(), params);
    }
}
