package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.MoreMenuUpdateViewSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 4/22/11
 * Time: 12:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class MoreMenuUpdateViewHistoryProcessor implements HistoryProcessor {

	private static final BackendStrings backendStrings = BackendStrings.App.get();

	@Override
	public SinksContainer process(String containerName, String[] strings) {
		return new MoreMenuUpdateViewSinksContainer(containerName + strings[0], backendStrings.updateMoreMenu(), strings);
	}

	@Override
	public SinksContainer processAdd(String[] params) {
		return null;
	}
}