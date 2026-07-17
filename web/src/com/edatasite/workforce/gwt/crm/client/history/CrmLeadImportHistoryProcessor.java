package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.CrmLeadImportSinksContainer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 24, 2009
 * Time: 11:37:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrmLeadImportHistoryProcessor implements HistoryProcessor {
    private final CrmStrings crmStrings = CrmStrings.App.get();
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new CrmLeadImportSinksContainer("importleadadd", Property.getPluralWithObjectCodeWithReplace(Constants.LEADS, crmStrings.importLeads(), wfmStrings.leads()), params);
    }
}
