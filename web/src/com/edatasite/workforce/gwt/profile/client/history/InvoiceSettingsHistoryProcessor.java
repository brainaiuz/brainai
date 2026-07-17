package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.InvoiceSettingsSinksContainer;

/**
 * User: Ilhombek
 * Date: 17.03.2010
 * Time: 15:55:40
 */
public class InvoiceSettingsHistoryProcessor implements HistoryProcessor {
    private WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new InvoiceSettingsSinksContainer(containerName + strings[0], wfmStrings.accounting(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
