package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.HrmsDashboardView;

import java.util.LinkedList;

/**
 * User: Abror Abdukadirov
 * Date: 23.04.2018 12:35
 */
public class HrmsDashboardSinksContainer extends SinksContainer {

    public HrmsDashboardSinksContainer() {
        super("dashboard", wfmStrings.dashboards(), null, NONE);
    }

    @Override
    protected void initViews() {
        if (Hrms.dashboards.size() > 0) {
            for (SelectItem dashboard : Hrms.dashboards) {
                addView(new HrmsDashboardView("dashboard_" + dashboard.getId(), localizeDefaultDashboardNames(dashboard.getName()), dashboard.getId()));
            }
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
