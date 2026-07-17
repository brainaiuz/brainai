package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.Accounting;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AccountingDashboardView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Abror Abdukadirov
 * Date: 16.04.2018 15:24
 */
public class AccountingDashboardSinksContainer extends SinksContainer {

    public AccountingDashboardSinksContainer() {
        super("dashboard", wfmStrings.dashboards(), null, NONE);
    }

    @Override
    protected void initViews() {

        if (Accounting.dashboards.size() > 0) {
            for (SelectItem dashboard : Accounting.dashboards) {
                addView(new AccountingDashboardView("dashboard_" + dashboard.getId(), localizeDefaultDashboardNames(dashboard.getName()), dashboard.getId()));
            }
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
