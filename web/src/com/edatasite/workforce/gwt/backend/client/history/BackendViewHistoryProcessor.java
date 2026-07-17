package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.BackendViewSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: JavaZone
 * Date: Sep 27, 2011
 * Time: 8:31:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class BackendViewHistoryProcessor implements HistoryProcessor {
	private static final BackendStrings backendStrings = BackendStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new BackendViewSinksContainer(containerName + strings[0], backendStrings.companySummary(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}