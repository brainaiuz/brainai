package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.SetTestCompanySinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: SherzodMuratov
 * Date: 28.02.2009
 * Time: 10:50:22
 * To change this template use File | Settings | File Templates.
 */
public class SetTestCompanyHistoryProcessor implements HistoryProcessor {

	private static final BackendStrings backendStrings = BackendStrings.App.get();

	public SinksContainer process(String containerName, String[] strings) {//must be ---> strings.length<=3
		return new SetTestCompanySinksContainer(containerName + strings[0], backendStrings.setTestCompany(), strings);
	}

	public SinksContainer processAdd(String[] params) {
		return null;
	}
}