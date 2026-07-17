package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.ui.HelpDocumentAddFromSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * User: Dilshod Madrahimov
 * Date: 2/28/13
 * Time: 12:28 PM
 */
public class HelpDocumentAddHistoryProcessor implements HistoryProcessor {

    private static final BackendStrings backendStrings = BackendStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        Integer objectID =null;
        if(params.length >1){
         objectID = params[1] != null && params[1].matches(Constants.REGEX_INTEGER) ? Integer.valueOf(params[1]) : null;
        }
        return new HelpDocumentAddFromSinksContainer("helpDocumentadd", objectID != null ? backendStrings.editHelpDocument() : backendStrings.addHelpDocument(), params);
    }
}
