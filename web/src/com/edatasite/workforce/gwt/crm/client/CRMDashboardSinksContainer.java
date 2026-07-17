package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.CRMDashboardView;

import java.util.LinkedList;

/**
 * User: Abror Abdukadirov
 * Date: 22.05.2018 17:45
 */
public class CRMDashboardSinksContainer extends SinksContainer {

    public CRMDashboardSinksContainer() {
        super("dashboard", wfmStrings.dashboards(), null, NONE);
    }

    @Override
    protected void initViews() {
        if (CRM.dashboards.size() > 0) {
            for (SelectItem dashboard : CRM.dashboards) {
                addView(new CRMDashboardView("dashboard_" + dashboard.getId(), localizeDefaultDashboardNames(dashboard.getName()), dashboard.getId()));
            }
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
