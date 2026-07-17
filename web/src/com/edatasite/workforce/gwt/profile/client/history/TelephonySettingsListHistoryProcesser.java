package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.TelephonySettingsListSinksContainer;

/**
 * User: Humoyun Hayitov
 * Date: 26.07.2020
 */
public class TelephonySettingsListHistoryProcesser implements HistoryProcessor {

    @Override
    public SinksContainer process(String containerName, String[] strings) {

        return new TelephonySettingsListSinksContainer(containerName, strings[0], null);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }

}
