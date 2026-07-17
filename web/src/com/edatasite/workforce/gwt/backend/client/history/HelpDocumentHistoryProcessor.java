package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.HelpDocumentsSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * User: Dilshod Madrahimov
 * Date: 3/1/13
 * Time: 2:49 PM
 */
public class HelpDocumentHistoryProcessor implements HistoryProcessor {

    private static final BackendStrings backendString = BackendStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new HelpDocumentsSinksContainer("helpDocumentView", backendString.helpDocuments());
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
