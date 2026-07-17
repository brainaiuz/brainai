package com.edatasite.workforce.gwt.backend.client.history;


import com.edatasite.workforce.gwt.backend.client.RemoveSinksContainer;
import com.edatasite.workforce.gwt.backend.client.RemoveTestEmailSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class RemovedHistoryProcessor implements HistoryProcessor {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

	public SinksContainer process(String containerName, String[] strings) {
        return new RemoveSinksContainer(containerName + strings[0], wfmStrings.delete());
	}

	public SinksContainer processAdd(String[] params) {
		return new RemoveTestEmailSinksContainer("removeadd", backendStrings.removeTestEmails());
	}
}