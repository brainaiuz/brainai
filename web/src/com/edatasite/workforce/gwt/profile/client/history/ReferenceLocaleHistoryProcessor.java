package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ReferenceLocaleSinksContainer;

public class ReferenceLocaleHistoryProcessor implements HistoryProcessor {
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ReferenceLocaleSinksContainer(containerName + strings[0],"Reference Locale", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
