package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.CrmAccountAddSinksContainer;
import com.edatasite.workforce.gwt.crm.client.CrmAccountViewSinksContainer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:32:57
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountHistoryProcessor implements HistoryProcessor {
      private WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new CrmAccountViewSinksContainer(containerName + strings[0], Property.get(Constants.CRM_ACCOUNT_LIST, wfmStrings.summaryView(), wfmStrings.company()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new CrmAccountAddSinksContainer("accountadd", wfmStrings.addAccount(), params);
    }
}
