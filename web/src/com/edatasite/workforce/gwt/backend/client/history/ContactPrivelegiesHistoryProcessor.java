package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.ContactPrivelegiesSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 13.08.2010
 * Time: 15:47:44
 * To change this template use File | Settings | File Templates.
 */
public class ContactPrivelegiesHistoryProcessor implements HistoryProcessor {

	private static final BackendStrings backendStrings = BackendStrings.App.get();

	public SinksContainer process(String containerName, String[] strings) {
		return new ContactPrivelegiesSinksContainer(containerName + strings[0], backendStrings.contactPrivelegies(), strings);
	}

	public SinksContainer processAdd(String[] params) {
		return null;
	}
}