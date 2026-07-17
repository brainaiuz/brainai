package com.edatasite.workforce.gwt.dashboardwidget.client.view.pm;

import com.edatasite.workforce.gwt.chart.client.charts.KpiDonutChart;
import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.enums.LegendPositionEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.LinkedList;

/**
 * Created by Hurshid on 5/8/2018.
 */
public class DashboardProjectOverview extends DashboardBaseWidget {

    public DashboardProjectOverview(DashboardComponentItem componentConf) {
        this.gridItemConfig = componentConf;
    }

    @Override
    protected void initInternal() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_ADD, DashboardProjectOverview.this, (sender, args) -> loadComponentData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_EDIT, DashboardProjectOverview.this, (sender, args) -> loadComponentData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_DELETE, DashboardProjectOverview.this, (sender, args) -> loadComponentData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DASHBOARD_PROJECT_REFRESH, DashboardProjectOverview.this, (sender, args) -> loadComponentData());
    }

    @Override
    protected void getData() {
        DashboardWidgetService.App.get().geProjectOverviewData(new AsyncCallback<ChartData>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ChartData chartData) {
                LoadingWidgets.get(getCode()).hide();
                contentPanel.clear();

                if (chartData != null) {
                    setTitle(wfmStrings.ProjectByStatus());
                    chartData.getConf().setSubtitle(gridItemConfig.getComponentCode());
                    chart = ChartUtils.generateChart(chartData);
                    if (chart instanceof KpiDonutChart) {
                        ((KpiDonutChart) chart).setTextInCenter(chartData.getTitle(), wfmStrings.total());
                    }
                    contentPanel.add(chart);
                } else {
                    noData();
                }
            }
        });
    }

    @Override
    protected void getSampleData(boolean nodata) {
        ChartData chartData = new ChartData();

        ChartConfItem chartConf = new ChartConfItem();
        chartConf.setTitle("Projects");
        chartConf.setType(ChartTypeEnum.DONUT_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);
        chartData.setConf(chartConf);

        LinkedList<String> categories = new LinkedList<>();
        categories.add("Ongoing (9)");
        categories.add("Completed (27)");
        categories.add("Not Started (21)");

        chartData.setCategories(categories);

        SerieData payableData = new SerieData();
        payableData.setValues(new Number[]{9, 27, 21});

        LinkedList<SerieData> series = new LinkedList<>();
        series.add(payableData);
        chartData.setSeries(series);

        setTitle(chartData.getConf().getTitle());

        chart = ChartUtils.generateChart(chartData);
        ((KpiDonutChart) chart).setTextInCenter(57L, "TOTAL");

        if (nodata) {
            chart.setColors(ChartUtils.NO_DATA_COLOR);
        } else {
            contentPanel.clear();
        }
        contentPanel.add(chart);
    }

    @Override
    public String getCode() {
        return gridItemConfig.getComponentCode();
    }

}
