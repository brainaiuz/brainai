package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.LeadAddSinksContainer;
import com.edatasite.workforce.gwt.crm.client.LeadViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:52:49
 * To change this template use File | Settings | File Templates.
 */
public class LeadHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new LeadViewSinksContainer(containerName + strings[0], Property.get(Constants.LEADS, wfmStrings.summaryView(), wfmStrings.lead()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new LeadAddSinksContainer("leadadd", Property.get(Constants.LEADS, wfmStrings.addMess(), wfmStrings.lead()), params);
    }

}
