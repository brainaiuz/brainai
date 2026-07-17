package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.container.SubscriptionManagementViewSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * User: Ilhombek
 * Date: 4/18/12
 * Time: 6:33 PM
 */
public class SubscriptionManagementViewHistoryProcessor implements HistoryProcessor {

    private static final BackendStrings backendStrings = BackendStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new SubscriptionManagementViewSinksContainer(containerName + strings[0], backendStrings.subscriptionSummary(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}