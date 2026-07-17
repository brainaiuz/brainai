package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.RecurrenceSettingsSinksContainer;

/**
 * User: Administrator
 * Date: Apr 26, 2010
 * Time: 8:05:05 PM
 */
public class RecurrenceSettingsHistoryProcessor implements HistoryProcessor {

    private final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new RecurrenceSettingsSinksContainer(containerName + strings[0], wfmStrings.recurrence(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}