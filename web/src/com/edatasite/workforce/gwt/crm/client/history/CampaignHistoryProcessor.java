package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.CampaignAddSinksContainer;
import com.edatasite.workforce.gwt.crm.client.CampaignViewSinksContainer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:52:49
 * To change this template use File | Settings | File Templates.
 */
public class CampaignHistoryProcessor implements HistoryProcessor {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new CampaignViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new CampaignAddSinksContainer("campaignadd", crmStrings.addCampaign(), params);
    }
}