package com.edatasite.workforce.gwt.pm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.pm.client.ui.PMDashboardView;

import java.util.LinkedList;

/**
 * User: Abror Abdukadirov
 * Date: 28.04.2018 16:55
 */
public class PMDashboardSinksContainer extends SinksContainer {

    public PMDashboardSinksContainer() {
        super("dashboard", wfmStrings.dashboards(), null, NONE);
    }

    @Override
    protected void initViews() {
        if (com.edatasite.workforce.gwt.pm.client.PM.dashboards.size() > 0) {
            for (SelectItem dashboard : com.edatasite.workforce.gwt.pm.client.PM.dashboards) {
                addView(new PMDashboardView("dashboard_" + dashboard.getId(), localizeDefaultDashboardNames(dashboard.getName()), dashboard.getId()));
            }
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
