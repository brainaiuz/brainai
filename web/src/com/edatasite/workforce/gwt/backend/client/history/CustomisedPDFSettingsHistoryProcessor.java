package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.CustomisedPDFSettingsAddSinksContainer;
import com.edatasite.workforce.gwt.backend.client.CustomisedPDFSettingsViewSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 18, 2011
 * Time: 5:54:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomisedPDFSettingsHistoryProcessor implements HistoryProcessor {

	private static final BackendStrings backendStrings = BackendStrings.App.get();

	public SinksContainer process(String containerName, String[] strings) {
		return new CustomisedPDFSettingsViewSinksContainer(containerName + strings[0], backendStrings.editPDFTemplate(), strings);
	}

	public SinksContainer processAdd(String[] params) {
		return new CustomisedPDFSettingsAddSinksContainer("pdftemplateadd", backendStrings.addPDFTemplate(), params);
	}
}