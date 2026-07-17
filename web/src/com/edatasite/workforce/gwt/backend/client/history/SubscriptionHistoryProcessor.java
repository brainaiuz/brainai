package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.SubscriptionEditSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * User: admin
 * Date: Jan 16, 2010
 * Time: 12:41:08 PM
 */
public class SubscriptionHistoryProcessor implements HistoryProcessor {

    private static final BackendStrings backendStrings = BackendStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new SubscriptionEditSinksContainer(containerName + strings[0], backendStrings.subscriptionUpdate(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}