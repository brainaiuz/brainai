package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.BackendSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class BackendHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

	public SinksContainer process(String containerName, String[] strings) {
        return new BackendSinksContainer(containerName + strings[0], wfmStrings.backend());
	}

	public SinksContainer processAdd(String[] params) {
		return null;
	}
}