package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.TaxAddSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 25.02.2009
 * Time: 16:13:43
 * To change this template use File | Settings | File Templates.
 */
public class TaxHistoryProcessor implements HistoryProcessor {

	private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

	public SinksContainer process(String containerName, String[] strings) {//must be ---> strings.length<=3
		return null;
	}

	public SinksContainer processAdd(String[] params) {
        return new TaxAddSinksContainer("taxadd", wfmStrings.addTaxRate());
	}
}