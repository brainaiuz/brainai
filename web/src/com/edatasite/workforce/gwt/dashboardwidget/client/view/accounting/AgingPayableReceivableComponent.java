package com.edatasite.workforce.gwt.dashboardwidget.client.view.accounting;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.enums.LegendPositionEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.google.gwt.user.client.rpc.AsyncCallback;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.Date;
import java.util.LinkedList;

public class AgingPayableReceivableComponent extends DashboardBaseWidget {

    private DatePicker datePicker;

    public AgingPayableReceivableComponent(DashboardComponentItem componentConf) {
        this.gridItemConfig = componentConf;
    }

    @Override
    protected void initInternal() {
        setTitle(wfmStrings.agedReports());
        Div calendarActionDiv = new Div("widget-heading__action");
        Div calendarBoxDiv = new Div("calendar-box");
        Div calendarBoxInputDiv = new Div("calendar-box__input");
        datePicker = new DatePicker();
        datePicker.setDate(new Date());
        calendarBoxInputDiv.add(datePicker);
        datePicker.addChangeHandler(changeEvent -> {
            loadComponentData();
        });
        calendarBoxDiv.add(calendarBoxInputDiv);

        Div calendarBoxIconDiv = new Div("calendar-box__icon");
        Icon calendarIcon = new Icon();
        calendarIcon.setStyleName("ficon--calendar2");
        calendarIcon.addClickHandler(event -> datePicker.showPopupCalendar());
        calendarBoxIconDiv.add(calendarIcon);
        calendarBoxDiv.add(calendarBoxIconDiv);
        calendarActionDiv.add(calendarBoxDiv);
        actionPanel.add(calendarActionDiv);

    }

    @Override
    protected void getData() {

        //if this method is in progress
        if (busy) {
            return;
        }
        busy = true;
        contentPanel.clear();

        LoadingWidgets.get(getCode()).show();
        Date dd = datePicker.getDate() != null ? datePicker.getDate() : new Date();
        DashboardWidgetService.App.get().getAgingData(new DateNonConvertable(dd), new AsyncCallback<ChartData>() {
            @Override
            public void onFailure(Throwable throwable) {
                busy = false;
                LoadingWidgets.get(getCode()).hide();
                noData();
            }

            @Override
            public void onSuccess(ChartData chartData) {
                busy = false;
                LoadingWidgets.get(getCode()).hide();
                contentPanel.clear();

                if (chartData != null && chartData.getSeries() != null && !chartData.getSeries().isEmpty()) {
                    chartData.getConf().setSubtitle(gridItemConfig.getName());
                    contentPanel.add(chart = ChartUtils.generateChart(chartData));
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
        chartConf.setTitle(wfmStrings.agedReports());
        chartConf.setType(ChartTypeEnum.VERTICAL_BAR_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);
        chartData.setConf(chartConf);

        LinkedList<String> categories = new LinkedList<>();
        categories.add(wfmStrings.current());
        categories.add("1 - 30");
        categories.add("31 - 60");
        categories.add("61 - 90");
        categories.add("> 90");
        chartData.setCategories(categories);

        SerieData payableData = new SerieData();
        payableData.setName(wfmStrings.payable());
        payableData.setValues(new Number[]{3000, 2500, 1900, 3900, 2800});

        SerieData receivableData = new SerieData();
        receivableData.setName(accountingStrings.receivable());
        receivableData.setValues(new Number[]{3250, 4500, 3900, 5100, 4900});

        LinkedList<SerieData> series = new LinkedList<>();
        series.add(payableData);
        series.add(receivableData);
        chartData.setSeries(series);

        setTitle(chartData.getConf().getTitle());
        chartData.getConf().setTitle(null);

        chart = ChartUtils.generateChart(chartData);

        if (nodata) {
            chart.setColors(ChartUtils.NO_DATA_COLOR);
        } else {
            contentPanel.clear();
        }
        contentPanel.add(chart);
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.AGED_REPORTS;
    }
}
