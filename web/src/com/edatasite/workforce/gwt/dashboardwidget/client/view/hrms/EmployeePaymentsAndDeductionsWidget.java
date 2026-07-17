package com.edatasite.workforce.gwt.dashboardwidget.client.view.hrms;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EmployeePaymentsAndDeductionsWidget  extends DashboardBaseWidget {

    public EmployeePaymentsAndDeductionsWidget(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
    }

    @Override
    protected void getData() {
        setTitle(wfmStrings.payslipByMonth());
        contentPanel.clear();

        LoadingWidgets.get(getCode()).show();
        DashboardWidgetService.App.get().getEmployeePaymentDeductionData(new AsyncCallback<ChartData>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingWidgets.get(getCode()).hide();
                noData();
            }

            @Override
            public void onSuccess(ChartData chartData) {
                LoadingWidgets.get(getCode()).hide();

                if (chartData != null) {
                    chartData.getConf().setSubtitle(gridItemConfig.getComponentCode());
                    chart = ChartUtils.generateChart(chartData);
                    contentPanel.add(chart);
                } else {
                    noData();
                }
            }
        });
    }

    @Override
    protected void getSampleData(boolean nodata) {
        setTitle(wfmStrings.payslipByMonth());

        DashboardWidgetService.App.get().getEmployeePaymentDeductionSampleData(new AsyncCallback<ChartData>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingWidgets.get(getCode()).hide();
                noData();
            }

            @Override
            public void onSuccess(ChartData chartData) {
                LoadingWidgets.get(getCode()).hide();

                if (!nodata) {
                    contentPanel.clear();
                }
                contentPanel.add(chart = ChartUtils.generateChart(chartData));
            }
        });
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.PAYROLL_EMPLOYEE_YTD;
    }
}
