package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.UserCredentialsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 17.03.2010
 * Time: 16:22:10
 */
public class UserCredentialsHistoryProcessor implements HistoryProcessor {
    private SettingStrings settingsStrings = SettingStrings.App.get();
    public SinksContainer process(String containerName, String[] strings) {
        return new UserCredentialsSinksContainer(containerName + strings[0], settingsStrings.userCredentials(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
