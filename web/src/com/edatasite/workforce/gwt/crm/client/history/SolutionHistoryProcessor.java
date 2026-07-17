package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.SolutionAddSinksContainer;
import com.edatasite.workforce.gwt.crm.client.SolutionViewSinksContainer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:32:57
 * To change this template use File | Settings | File Templates.
 */
public class SolutionHistoryProcessor implements HistoryProcessor {
    private final CrmStrings crmStrings = CrmStrings.App.get();
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new SolutionViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new SolutionAddSinksContainer("solutionadd", crmStrings.editSolution(), params);
    }
}