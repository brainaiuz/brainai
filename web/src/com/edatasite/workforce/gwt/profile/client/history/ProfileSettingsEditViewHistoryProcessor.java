package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ProfileSettingsEditViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 8/2/11
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfileSettingsEditViewHistoryProcessor implements HistoryProcessor {
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ProfileSettingsEditViewSinksContainer(containerName + strings[0], "Profile Settings Edit View", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
