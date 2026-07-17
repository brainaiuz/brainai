package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.components.dashboard.DashboardContainer;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by dilshod on 12/15/2015.
 */
public class AccountingDashboardView extends View {
    private Integer dashboardId;
    private DashboardContainer container;

    public AccountingDashboardView(String name, String description, Integer dashboardId) {
        super(name, description);
        this.dashboardId = dashboardId;
        this.viewId = dashboardId;
    }

    @Override
    protected Widget onInitialize() {
        initialize();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ACCOUNTING_DASHBOARD_CUSTOMIZE, AccountingDashboardView.this, (sender, args) -> {
            initialize();
        });
        return null;
    }

    @Override
    public String getIconStyle() {
        return "accountMark accounting-dashboard";
    }

    private void initialize() {
        clear();
        container = new DashboardContainer(dashboardId);
        setWidgetDataList(container.getKpiWidgetDataList());
        setDashboards(container.getDashboards());
        add(container);
    }

    @Override
    public void reInitialize() {
        container.reInitializeWidgets();
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

}
