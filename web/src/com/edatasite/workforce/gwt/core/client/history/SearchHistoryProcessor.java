package com.edatasite.workforce.gwt.core.client.history;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.search.SearchViewSinksContainer;


public class SearchHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new SearchViewSinksContainer(containerName + strings[0], wfmStrings.search(), strings);

    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}