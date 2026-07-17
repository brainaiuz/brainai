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
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.google.gwt.user.client.rpc.AsyncCallback;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.Date;
import java.util.LinkedList;

public class SalesAndPurchaseComponent extends DashboardBaseWidget {

    private DatePicker datePicker;

    public SalesAndPurchaseComponent(DashboardComponentItem componentConf) {
        this.gridItemConfig = componentConf;
    }

    @Override
    protected void initInternal() {
        setTitle(wfmStrings.salesAndPurchasesYTD());

        Div calendarActionDiv = new Div("widget-heading__action");
        Div calendarBoxDiv = new Div("calendar-box");
        Div calendarBoxInputDiv = new Div("calendar-box__input");
        datePicker = new DatePicker();
        datePicker.setDate(DateUtil.getMonthLastDate(DateUtil.resetTime(new Date())));
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
        DashboardWidgetService.App.get().getSalesPurchaseData(new DateNonConvertable(datePicker.getDate()), new AsyncCallback<ChartData>() {
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

                if (chartData != null) {
                    chartData.getConf().setSubtitle(gridItemConfig.getName());
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
        ChartData chartData = new ChartData();

        ChartConfItem chartConf = new ChartConfItem();
        chartConf.setTitle(wfmStrings.salesAndPurchasesYTD());
        chartConf.setType(ChartTypeEnum.LINE_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);
        chartData.setConf(chartConf);

        LinkedList<String> categories = new LinkedList<>();
        categories.add("Jan 18");
        categories.add("Fev 18");
        categories.add("Mar 18");
        categories.add("Apr 18");
        categories.add("May 18");
        chartData.setCategories(categories);

        SerieData salesData = new SerieData();
        salesData.setName(wfmStrings.sales());
        salesData.setValues(new Number[]{3250, 4500, 3900, 5100, 9900});

        SerieData purchaseData = new SerieData();
        purchaseData.setName(wfmStrings.purchases());
        purchaseData.setValues(new Number[]{1000, 2500, 5900, 3900, 2800});

        LinkedList<SerieData> series = new LinkedList<>();
        series.add(salesData);
        series.add(purchaseData);
        chartData.setSeries(series);

        setTitle(chartData.getConf().getTitle());
        chartData.getConf().setSubtitle(gridItemConfig.getName());

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
        return DASHBOARD_WIDGET_CODE.SALE_PURCHASE_TRANSACTIONS;
    }
}
