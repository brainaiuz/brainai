package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollDashboardView;

import java.util.LinkedList;

public class PayrollDashboardSinksContainer extends SinksContainer {

    public PayrollDashboardSinksContainer() {
        super("dashboard", wfmStrings.dashboards(), null, NONE);
    }

    @Override
    protected void initViews() {
        if (Payroll.dashboards.size() > 0) {
            for (SelectItem dashboard : Payroll.dashboards) {
                addView(new PayrollDashboardView("dashboard_" + dashboard.getId(), localizeDefaultDashboardNames(dashboard.getName()), dashboard.getId()));
            }
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
