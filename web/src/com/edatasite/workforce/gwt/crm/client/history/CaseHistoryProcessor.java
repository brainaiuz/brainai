package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.CaseAddSinksContainer;
import com.edatasite.workforce.gwt.crm.client.CaseViewSinksContainer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:32:57
 * To change this template use File | Settings | File Templates.
 */
public class CaseHistoryProcessor implements HistoryProcessor {
    private final CrmStrings crmStrings = CrmStrings.App.get();
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new CaseViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new CaseAddSinksContainer("caseadd", params != null && params.length > 1 ? Property.get(Constants.CASE_LIST, crmStrings.editCase(), wfmStrings.caseID()) : Property.get(Constants.CASE_LIST, wfmStrings.addMess(), wfmStrings.caseID()), params);
    }
}