package com.edatasite.workforce.gwt.dashboardwidget.client.view.hrms;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.enums.LegendPositionEnum;
import com.edatasite.workforce.gwt.chart.client.enums.StackedEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.LinkedList;

public class GenderRatioChart extends DashboardBaseWidget {

    public GenderRatioChart(DashboardComponentItem componentItem) {
        this.gridItemConfig = componentItem;
    }

    @Override
    protected void getData() {
        LoadingWidgets.get(getCode()).show();
        DashboardWidgetService.App.get().getEmployeesGenderRatio(new AsyncCallback<ChartData>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingWidgets.get(getCode()).hide();
                noData();
            }

            @Override
            public void onSuccess(ChartData chartData) {
                LoadingWidgets.get(getCode()).hide();
                if (chartData != null) {
                    setTitle(wfmStrings.genderRatio());
                    createChart(chartData, false);
                } else {
                    noData();
                }
            }
        });
    }

    @Override
    protected void getSampleData(boolean nodata) {
        if (super.title != null) {
            setTitle(wfmStrings.genderRatio());
        }
        ChartData chartData = new ChartData();
        chartData.setTitle(100L);
        ChartConfItem chartConf = new ChartConfItem();
        chartConf.setTitle(wfmStrings.genderRatio());
        chartConf.setType(ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);
        chartConf.setStacked(StackedEnum.BY_PERCENTANDVALUE);
        chartData.setConf(chartConf);

        LinkedList<String> categories = new LinkedList<>();
        categories.add(wfmStrings.male());
        categories.add(wfmStrings.female());
        chartData.setCategories(categories);

        SerieData genderData = new SerieData();
        genderData.setColor("#FBA800");
        genderData.setSerieType(ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART);
        genderData.setValues(new Number[]{65L, 35L, 0L});
        LinkedList<SerieData> series = new LinkedList<>();
        series.add(genderData);
        chartData.setSeries(series);
        createChart(chartData, true);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
    }

    private void createChart(ChartData chartData, boolean sample) {
        chartData.getConf().setShowLabel(true);
        chartData.setTextInCenter("<dl title=" + chartData.getTitle() + " class=\"chart--circle-text\">\n" +
                "<dt>" + chartData.getTitle() + "</dt>" +
                " <dd>" + wfmStrings.total() + "</dd>\n" +
                "</dl>");
        Number[] values = chartData.getSeries().get(0).getValues();
        LinkedList<Number> v_ = new LinkedList<>();
        LinkedList<String> colors = new LinkedList<>();
        LinkedList<String> categories = new LinkedList<>();

        chartData.setCategories(categories);
        if ((Long)values[0] > 0) { // Male
            v_.add(values[0]);
            categories.add(wfmStrings.male());
            colors.add("#FBA800");
        }
        if ((Long)values[1] > 0) { // Female
            v_.add(values[1]);
            categories.add(wfmStrings.female());
            colors.add("#158ED0");
        }
        if ((Long)values[2] > 0) { // Hezim
            v_.add(values[2]);
            categories.add(wfmStrings.notAvailable());
            colors.add(ChartUtils.NO_DATA_COLOR);
        }
        chartData.getSeries().get(0).setValues(v_.toArray(new Number[]{}));
        chart = ChartUtils.generateChart(chartData);
        chart.setColors(colors.toArray(new String[]{}));
        if (!sample) {
            contentPanel.clear();
        }

        contentPanel.add(chart);
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.GENDER_RATIO;
    }
}
