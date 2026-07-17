package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateEditSinksContainer;

/**
 * Created with IntelliJ IDEA.
 * User: Java6
 * Date: 08.02.13
 * Time: 14:55
 * To change this template use File | Settings | File Templates.
 */
public class EmailTemplateEditHistoryProcessor implements HistoryProcessor {
    private SettingStrings settingsStrings = SettingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new EmailTemplateEditSinksContainer(containerName + strings[0], settingsStrings.editEmailTemplate(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
