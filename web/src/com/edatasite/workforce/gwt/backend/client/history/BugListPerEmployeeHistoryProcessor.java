package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.BugListPerEmployeeSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 09.06.2009
 * Time: 15:18:39
 * To change this template use File | Settings | File Templates.
 */
public class BugListPerEmployeeHistoryProcessor implements HistoryProcessor {

	private static final BackendStrings backendStrings = BackendStrings.App.get();

	public SinksContainer process(String containerName, String[] strings) {
		return new BugListPerEmployeeSinksContainer(containerName + strings[0], backendStrings.bugList(), strings);
	}

	public SinksContainer processAdd(String[] params) {
		return null;
	}
}