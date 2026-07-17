package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.AIPhantomPDFSettingsAddSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class AIPhantomPDFSettingsHistoryProcessor implements HistoryProcessor {

    public SinksContainer process(String containerName, String[] strings) {
        return new AIPhantomPDFSettingsAddSinksContainer("pdftemplatewAIadd", "Add AI Phantom Pdf", strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new AIPhantomPDFSettingsAddSinksContainer("pdftemplatewAIadd", "Add AI Phantom Pdf", params);
    }
}
