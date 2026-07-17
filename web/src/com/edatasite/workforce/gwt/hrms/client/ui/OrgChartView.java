package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.hrms.client.ui.orgchart.HrEmployeeOrgChart;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by User on 09.03.16.
 */
public class OrgChartView extends View implements FittedContent {
    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private HrEmployeeOrgChart employeeOrgChart;

    public OrgChartView() {
        super("organizationChart");
        setDescription(property.getPlural(hrmsStrings.supervisorStructure()));
    }

    @Override
    protected Widget onInitialize() {
        employeeOrgChart = new HrEmployeeOrgChart();
        add(employeeOrgChart);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_ADD, OrgChartView.this, (sender, args) -> employeeOrgChart.refreshChart());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_PROFILE_UPDATE, OrgChartView.this, (sender, args) -> employeeOrgChart.refreshChart());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYE_LIST_EDIT_CELL, OrgChartView.this, (sender, args) -> employeeOrgChart.refreshChart());

        return null;
    }

    @Override
    public String getIconStyle() {
        return "org-chart";
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    public String getPropertyCode() {
        return "organizationChart";
    }
}
