package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.components.dashboard.DashboardContainer;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Abror Abdukadirov
 * Date: 23.04.2018 12:41
 */
public class HrmsDashboardView extends View {

    private Integer dashboardId;
    private DashboardContainer container;

    public HrmsDashboardView(String name, String description, Integer dashboardId) {
        super(name, description);
        this.dashboardId = dashboardId;
        this.viewId = dashboardId;
    }

    @Override
    protected Widget onInitialize() {
        initialize();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MANAGER_SELF_SERVICE_CUSTOMIZE, HrmsDashboardView.this, (sender, args) -> {
            initialize();
        });
        return null;
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
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
