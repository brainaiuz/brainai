package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.WFTFooterPdfSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * User: Ilhombek
 * Date: 24.09.2010
 * Time: 16:56:16
 */
public class WFTFooterPdfHistoryProcessor implements HistoryProcessor {

	private static final BackendStrings backendStrings = BackendStrings.App.get();

	public SinksContainer process(String containerName, String[] strings) {
		return new WFTFooterPdfSinksContainer(containerName + strings[0], backendStrings.updatePDFs(), strings);
	}

	public SinksContainer processAdd(String[] params) {
		return null;
	}
}