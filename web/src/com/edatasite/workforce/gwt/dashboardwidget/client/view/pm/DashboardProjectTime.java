package com.edatasite.workforce.gwt.dashboardwidget.client.view.pm;

import com.edatasite.workforce.gwt.chart.client.charts.KpiSemiCircleDonutChart;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by Hurshid on 5/9/2018.
 */
public class DashboardProjectTime extends DashboardBaseWidget {

    public DashboardProjectTime(DashboardComponentItem componentConf) {
        this.gridItemConfig = componentConf;
    }

    @Override
    protected void getData() {
        DashboardWidgetService.App.get().geProjectTime(new AsyncCallback<ChartData>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ChartData chartData) {
                LoadingWidgets.get(getCode()).hide();
                contentPanel.clear();

                if (chartData != null) {
                    setTitle(wfmStrings.time());
                    chart = ChartUtils.generateChart(chartData);
                    if (chart instanceof KpiSemiCircleDonutChart) {
//                        ((KpiSemiCircleDonutChart) chart).setTextInCenter(chartData.getTitle());
                    }
                    contentPanel.add(chart);
                }
            }
        });
    }

    @Override
    protected void getSampleData(boolean nodata) {

    }

    @Override
    public String getCode() {
        return gridItemConfig.getComponentCode();
    }
}
