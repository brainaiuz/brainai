package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.BlackListAddSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * User: admin
 * Date: Jan 5, 2010
 * Time: 5:55:14 PM
 */
public class BlackListHistoryProcessor implements HistoryProcessor {

    private static final BackendStrings backendStrings = BackendStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new BlackListAddSinksContainer("blackListadd", backendStrings.addToBlackList(), params);
    }
}