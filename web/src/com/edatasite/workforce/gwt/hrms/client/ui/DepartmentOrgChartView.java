package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.hrms.client.ui.orgchart.HrTeamOrgChart;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by dilsh0d on 21.03.16.
 */
public class DepartmentOrgChartView extends View implements FittedContent {

    private HrTeamOrgChart teamOrgChart;
    private static HrmsStrings hrmsStrings = HrmsStrings.App.get();

    public DepartmentOrgChartView() {
        super("departmentOrgChartView");
        setDescription(property.getPlural(hrmsStrings.organizationChart()));
    }

    @Override
    protected Widget onInitialize() {
        teamOrgChart = new HrTeamOrgChart();
        add(teamOrgChart);
        return null;
    }

    @Override
    public String getIconStyle() {
        return "org-chartPie";
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

    @Override
    protected void onAttach() {
        super.onAttach();
        MainLayout.get().mutateBodyWithFrameContent2(true);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        MainLayout.get().mutateBodyWithFrameContent2(false);
    }

    public String getPropertyCode() {
        return "departmentOrgChartView";
    }
}
