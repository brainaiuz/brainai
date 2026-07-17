package com.edatasite.workforce.gwt.core.client.history;

import com.edatasite.workforce.gwt.core.client.WebhookListSinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class WebhookListHistoryProcessor implements HistoryProcessor {
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new WebhookListSinksContainer("webhooklist", "workflowWebHooks", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new WebhookListSinksContainer("webhooklist", "workflowWebHooks", params);
    }
}
