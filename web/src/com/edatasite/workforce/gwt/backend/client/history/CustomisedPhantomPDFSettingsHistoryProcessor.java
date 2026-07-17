package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.CustomisedPhantomPDFSettingsAddSinksContainer;
import com.edatasite.workforce.gwt.backend.client.CustomisedPhantomPDFSettingsViewSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * User: Abror Abdukadirov
 * Date: 04.10.2018 15:42
 */
public class CustomisedPhantomPDFSettingsHistoryProcessor implements HistoryProcessor {

    public SinksContainer process(String containerName, String[] strings) {
        return new CustomisedPhantomPDFSettingsViewSinksContainer(containerName + strings[0], "Edit Phantom Pdf", strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new CustomisedPhantomPDFSettingsAddSinksContainer("newpdftemplateadd", "Add Phantom Pdf", params);
    }
}
