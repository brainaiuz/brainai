package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.BugListSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Dec 11, 2008
 * Time: 2:20:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class BugListHistoryProcessor implements HistoryProcessor {

	private static final BackendStrings backendStrings = BackendStrings.App.get();

	public SinksContainer process(String containerName, String[] strings) {//must be ---> strings.length<=3
		return new BugListSinksContainer(containerName + strings[0], backendStrings.bugList(), strings);
	}

	public SinksContainer processAdd(String[] params) {
		return null;
	}
}