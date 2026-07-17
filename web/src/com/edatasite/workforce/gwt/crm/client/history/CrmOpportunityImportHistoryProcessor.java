package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.CrmOpportunityImportSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Sep 24, 2012
 * Time: 11:37:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrmOpportunityImportHistoryProcessor implements HistoryProcessor {
    private WfmStrings wfmStrings = WfmStrings.App.get();
    private WfmMessages messages = WfmMessages.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new CrmOpportunityImportSinksContainer("importopportunityadd", messages.importEntity(wfmStrings.opportunities()), params);
    }
}
