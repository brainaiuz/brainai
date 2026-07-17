package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class SubscriptiontypeHistoryProcessor implements HistoryProcessor {

	private static final BackendStrings backendStrings = BackendStrings.App.get();

	public SinksContainer process(String containerName, String[] strings) {
		return new SubscriptiontypeSinksContainer(containerName + strings[0], backendStrings.subscriptionTypeView(), strings);
	}

	public SinksContainer processAdd(String[] params) {
		return new SubscriptionTypeAddSinksContainer("subscriptiontypeadd", backendStrings.addSubscriptionType());
	}
}