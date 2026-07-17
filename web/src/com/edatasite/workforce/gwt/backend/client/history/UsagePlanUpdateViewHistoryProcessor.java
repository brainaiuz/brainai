package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.UsagePlanUpdateViewSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * User: Ilhombek
 * Date: 24.08.2010
 * Time: 19:09:12
 */
public class UsagePlanUpdateViewHistoryProcessor implements HistoryProcessor {

    private static final BackendStrings backendStrings = BackendStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new UsagePlanUpdateViewSinksContainer(containerName + strings[0], backendStrings.usagePlan(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}