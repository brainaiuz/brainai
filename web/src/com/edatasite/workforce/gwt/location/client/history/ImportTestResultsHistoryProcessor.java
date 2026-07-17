package com.edatasite.workforce.gwt.location.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.location.client.ImportTestResultsSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 17.09.12
 * Time: 21:12
 * To change this template use File | Settings | File Templates.
 */

public class ImportTestResultsHistoryProcessor implements HistoryProcessor {

	private static final TCStrings tcStrings = TCStrings.App.get();

	@Override
	public SinksContainer process(String containerName, String[] strings) {
		return null;//new ImportTestResultsSinksContainer("xmlimport", "Import XML file", strings);
	}

	@Override
	public SinksContainer processAdd(String[] params) {
		return new ImportTestResultsSinksContainer("xmlimportadd", tcStrings.importXMLFile(), params);
	}
}
