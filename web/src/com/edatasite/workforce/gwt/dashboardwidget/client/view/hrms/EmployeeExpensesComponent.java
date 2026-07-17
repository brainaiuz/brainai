package com.edatasite.workforce.gwt.dashboardwidget.client.view.hrms;

import com.edatasite.workforce.gwt.chart.client.charts.KpiDonutChart;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FromToDate;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.accounting.TopExpensesComponent;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.math.BigDecimal;
import java.util.Date;

public class EmployeeExpensesComponent extends TopExpensesComponent {


    public EmployeeExpensesComponent(DashboardComponentItem componentConf) {
        super(componentConf);
    }

    @Override
    protected void initInternal() {
        super.initInternal();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXPENSEREPORT_SAVED, EmployeeExpensesComponent.this, (sender, args) -> loadComponentData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXPENSE_DELETED, EmployeeExpensesComponent.this, (sender, args) -> loadComponentData());
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.EMPLOYEE_TOP_EXPENSES;
    }

    @Override
    protected void getData() {
        //if this method is in progress
        if (busy) {
            return;
        }
        busy = true;
        contentPanel.clear();

        FromToDate fromToDate = mapDates.get(dwDateList.getSelectedId());

        if (fromToDate == null) {
            Date lastMonthStart = DateUtil.addMonths(DateUtil.getMonthFirstDay(new Date()), -1);
            Date lastMonthEnd = DateUtil.getMonthLastDate((Date) lastMonthStart.clone());

            fromToDate = new FromToDate(new DateNonConvertable(lastMonthStart), new DateNonConvertable(lastMonthEnd));
        }

        LoadingWidgets.get(getCode()).show();
        DashboardWidgetService.App.get().getEmployeeTopExpenses(fromToDate, new AsyncCallback<ChartData>() {
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

                if (chartData != null && chartData.getCategories() != null && !chartData.getCategories().isEmpty()) {
                    chartData.getConf().setSubtitle(gridItemConfig.getName());
                    chart = ChartUtils.generateChart(chartData);

                    BigDecimal total = BigDecimal.ZERO;
                    for (Number value : chartData.getSeries().get(0).getValues()) {
                        total = total.add(new BigDecimal(value.doubleValue()));
                    }

                    if (chart instanceof KpiDonutChart) {
                        ((KpiDonutChart) chart).setTextInCenter(total);
                    }
                    if (chartData.getCategories().size() > 2) {
                        chart.configureLegend(true);
                    }
                    contentPanel.add(chart);
                } else {
                    noData();
                }
            }
        });

    }

    @Override
    protected String expenseType() {
        return wfmStrings.myExpenses();
    }
}
