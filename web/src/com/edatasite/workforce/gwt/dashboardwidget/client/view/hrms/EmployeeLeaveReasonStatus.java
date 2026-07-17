package com.edatasite.workforce.gwt.dashboardwidget.client.view.hrms;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.enums.LegendPositionEnum;
import com.edatasite.workforce.gwt.chart.client.enums.StackedEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;
import java.util.LinkedList;

public class EmployeeLeaveReasonStatus extends DashboardBaseWidget {


    public EmployeeLeaveReasonStatus(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
    }

    @Override
    public Widget onInitialize() {
        Widget result = super.onInitialize();
        if (title != null) {
            setTitle(wfmStrings.leaveStatus());
        }
        return result;
    }

    @Override
    protected void getData() {
        ListingFilterParameter fp = new ListingFilterParameter();
        DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");
        fp.setYear(Integer.parseInt(yearFormat.format(new Date())));
        fp.setStartDate(new Date());
        DashboardWidgetService.App.get().getLeaveRequestDays(fp, new AsyncCallback<ChartData>() {
            @Override
            public void onFailure(Throwable throwable) {
                noData();
            }

            @Override
            public void onSuccess(ChartData chartData) {
                drawChart(chartData);
            }
        });
    }

    private void drawChart(ChartData chartData) {
        contentPanel.clear();
        chart = ChartUtils.generateChart(chartData);
        contentPanel.add(chart);
    }

    @Override
    protected void getSampleData(boolean nodata) {
        if (title != null) {
            setTitle(wfmStrings.leaveStatus());
        }
        ChartData result = new ChartData();

        ChartConfItem config = new ChartConfItem();
        config.setType(ChartTypeEnum.VERTICAL_BAR_CHART);
        config.setLegend(LegendPositionEnum.BOTTOM);
        result.setConf(config);
        LinkedList<String> categories = new LinkedList<>();
        categories.add(wfmStrings.annualLeave());
        categories.add(wfmStrings.sickLeave());
        categories.add(wfmStrings.lateLeave());
        result.setCategories(categories);
        config.setStacked(StackedEnum.BY_VALUE);
        LinkedList<SerieData> series = new LinkedList<>();
        SerieData exceed = new SerieData();
        exceed.setName(wfmStrings.exceeded());
        exceed.setValues(new Number[]{10, 0, 1});
        exceed.setColor("#FF0022");
        series.add(exceed);

        SerieData left = new SerieData();
        left.setName(wfmStrings.leftDays());
        left.setValues(new Number[]{0, 3, 0});
        left.setColor("#FBA800");
        series.add(left);

        SerieData taken = new SerieData();
        taken.setName(wfmStrings.taken());
        taken.setValues(new Number[]{22, 2, 21});
        taken.setColor("#00C836");
        series.add(taken);

        result.setSeries(series);
        drawChart(result);
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.LEAVE_REASON_STATUS;
    }
}
