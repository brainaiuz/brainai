/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/13 6:50:14                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.OpportunityAddSinksContainer;
import com.edatasite.workforce.gwt.crm.client.OpportunityViewSinksContainer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:52:49
 * To change this template use File | Settings | File Templates.
 */
public class OpportunityHistoryProcessor implements HistoryProcessor {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new OpportunityViewSinksContainer(containerName + strings[0], Property.get(Constants.Opportunities, wfmStrings.summaryView(), wfmStrings.opportunity()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        String desc = Property.get(Constants.Opportunities, wfmStrings.addMess(), wfmStrings.opportunity());
        if (params != null && params.length > 1 && params[1] != null && params[1].matches(Constants.REGEX_INTEGER_POSITIVE)) {
            if (params.length < 3 || !Constants.COPY.equals(params[2])) {
                desc = Property.get(Constants.Opportunities, wfmStrings.edit(), wfmStrings.opportunity());
            }
        }
        return new OpportunityAddSinksContainer("opportunityadd", desc, params);
    }
}