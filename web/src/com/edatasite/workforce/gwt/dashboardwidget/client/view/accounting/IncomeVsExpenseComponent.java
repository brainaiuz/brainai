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
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.google.gwt.user.client.rpc.AsyncCallback;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.Date;
import java.util.LinkedList;

public class IncomeVsExpenseComponent extends DashboardBaseWidget {

    private DatePicker datePicker;

    public IncomeVsExpenseComponent(DashboardComponentItem componentConf) {
        this.gridItemConfig = componentConf;
    }

    @Override
    protected void initInternal() {
        setTitle(wfmStrings.incomeVsExpenceYTD());

        Div calendarActionDiv = new Div("widget-heading__action");
        Div calendarBoxDiv = new Div("calendar-box");
        Div calendarBoxInputDiv = new Div("calendar-box__input");
        datePicker = new DatePicker();
        datePicker.setDate(DateUtil.resetTime(new Date()));
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

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXPENSEREPORT_SAVED, IncomeVsExpenseComponent.this, (sender, args) -> loadComponentData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXPENSE_DELETED, IncomeVsExpenseComponent.this, (sender, args) -> loadComponentData());
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
        DashboardWidgetService.App.get().getIncomeExpensesData(new DateNonConvertable(datePicker.getDate()), new AsyncCallback<ChartData>() {
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
                chartData.getConf().setSubtitle(gridItemConfig.getName());
                if (chartData != null) {
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
        chartConf.setTitle(wfmStrings.incomeVsExpenceYTD());
        chartConf.setType(ChartTypeEnum.VERTICAL_BAR_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);
        chartData.setConf(chartConf);

        LinkedList<String> categories = new LinkedList<>();
        categories.add("Jan 18");
        categories.add("Fev 18");
        categories.add("Mar 18");
        categories.add("Apr 18");
        categories.add("May 18");
        chartData.setCategories(categories);

        SerieData incomeData = new SerieData();
        incomeData.setName(accountingStrings.income());
        incomeData.setColor(nodata ? ChartUtils.NO_DATA_COLOR : "#8ACD00");
        incomeData.setValues(new Number[]{3250, 4500, 3900, 5100, 4900});

        SerieData expenseData = new SerieData();
        expenseData.setName(wfmStrings.expense());
        expenseData.setColor(nodata ? ChartUtils.NO_DATA_COLOR : "#FF0022");
        expenseData.setValues(new Number[]{3000, 2500, 1900, 3900, 2800});

        SerieData netData = new SerieData();
        netData.setName(wfmStrings.profit());
        netData.setColor(nodata ? ChartUtils.NO_DATA_COLOR : "#FBA800");
        netData.setSerieType(ChartTypeEnum.LINE_CHART);
        netData.setValues(new Number[]{250, 2000, 2000, 1200, 2100});

        LinkedList<SerieData> series = new LinkedList<>();
        series.add(incomeData);
        series.add(expenseData);
        series.add(netData);
        chartData.setSeries(series);

        setTitle(chartData.getConf().getTitle());
        chartData.getConf().setSubtitle(gridItemConfig.getName());

        if (!nodata) {
            contentPanel.clear();
        }
        contentPanel.add(chart = ChartUtils.generateChart(chartData));
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.INCOME_VS_EXPENSE;
    }
}
