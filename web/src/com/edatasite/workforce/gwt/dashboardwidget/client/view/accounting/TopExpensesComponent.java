package com.edatasite.workforce.gwt.dashboardwidget.client.view.accounting;

import com.edatasite.workforce.gwt.chart.client.charts.KpiDonutChart;
import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.enums.LegendPositionEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FromToDate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class TopExpensesComponent extends DashboardBaseWidget {

    public TopExpensesComponent(DashboardComponentItem componentConf) {
        this.gridItemConfig = componentConf;
    }

    private Date currentDate;
    private Date financialYearStart;
    protected HashMap<Integer, FromToDate> mapDates;
    protected DataListBox dwDateList;

    @Override
    protected void initInternal() {
        currentDate = new Date();
        mapDates = new HashMap<>();

        dwDateList = new DataListBox();
        dwDateList.setWithoutNullLabel(true);
        dwDateList.addValueChangeHandler(vch -> {
            loadComponentData();
        });
        setTitle(new HTML(expenseType()/* + " " + wfmStrings.lastMonth()*/));
        filterPanel.add(dwDateList);

        CommonService.App.get().getFinancialYearStart(new AsyncCallback<DateNonConvertable>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(DateNonConvertable dateNonConvertable) {
                financialYearStart = dateNonConvertable != null ? dateNonConvertable.getNonConvertedDate() : DateUtil.getYearFirstDay(new Date());
                financialYearStart.setYear(currentDate.getYear());

                while (financialYearStart.after(currentDate)) {
                    financialYearStart.setYear(financialYearStart.getYear() - 1);
                }
                initializeDates();
            }
        });
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
        DashboardWidgetService.App.get().getTopExpenses(fromToDate, new AsyncCallback<ChartData>() {
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
                if (chartData != null && chartData.getCategories() != null && !chartData.getCategories().isEmpty()) {
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
    protected void getSampleData(boolean nodata) {
        ChartData chartData = new ChartData();

        ChartConfItem chartConf = new ChartConfItem();
        chartConf.setTitle(expenseType() + " " + wfmStrings.lastMonth());
        chartConf.setType(ChartTypeEnum.DONUT_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);
        chartData.setConf(chartConf);

        LinkedList<String> categories = new LinkedList<>();
        categories.add(accountingStrings.transportation());
        categories.add(accountingStrings.medical());
        categories.add(accountingStrings.auditFree());
        categories.add(accountingStrings.depreciation());
        categories.add(wfmStrings.other());
        chartData.setCategories(categories);

        SerieData topExpensesData = new SerieData();
        topExpensesData.setName("Top Expenses");
        topExpensesData.setValues(new Number[]{3250, 4500, 3900, 5100, 4900});

        LinkedList<SerieData> series = new LinkedList<>();
        series.add(topExpensesData);
        chartData.setSeries(series);

        chartData.getConf().setSubtitle(gridItemConfig.getName());
        chart = ChartUtils.generateChart(chartData);

        if (chart instanceof KpiDonutChart) {
            ((KpiDonutChart) chart).setTextInCenter(29000);
        }

        if (nodata) {
            chart.setColors(ChartUtils.NO_DATA_COLOR);
        } else {
            contentPanel.clear();
        }
        contentPanel.add(chart);
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.TOP_EXPENSES;
    }

    private void initializeDates() {
        Date currentMonthStart = DateUtil.getMonthFirstDay(new Date());
        Date currentMonthEnd = DateUtil.getMonthLastDate(new Date());

        Date lastMonthStart = DateUtil.addMonths(currentMonthStart, -1);
        Date lastMonthEnd = DateUtil.getMonthLastDate((Date) lastMonthStart.clone());

        //Last month expenses
        mapDates.put(0, new FromToDate(new DateNonConvertable(lastMonthStart), new DateNonConvertable(lastMonthEnd)));
        //This month
        mapDates.put(1, new FromToDate(new DateNonConvertable(currentMonthStart), new DateNonConvertable(currentMonthEnd)));

        LinkedList<Date> quarterList = Utils.setupFinancialQuarties(financialYearStart);
        //this quarter
        mapDates.put(2, new FromToDate(new DateNonConvertable(quarterList.get(2)), new DateNonConvertable(quarterList.get(3))));

        //last quarter
        mapDates.put(3, new FromToDate(new DateNonConvertable(quarterList.get(0)), new DateNonConvertable(quarterList.get(1))));

        //this year
        mapDates.put(4, new FromToDate(new DateNonConvertable(financialYearStart), new DateNonConvertable(DateUtil.addDays(DateUtil.addYears(financialYearStart, 1), 1))));

        Date lastYearStart = DateUtil.addYears(financialYearStart, -1);
        mapDates.put(5, new FromToDate(new DateNonConvertable(lastYearStart), new DateNonConvertable(DateUtil.addDays(financialYearStart, -1))));

        dwDateList.setItems(new SelectItem[]{
                new SelectItem(0, wfmStrings.lastMonth()),
                new SelectItem(1, wfmStrings.thisMonth()),
                new SelectItem(2, wfmStrings.thisQuarter()),
                new SelectItem(3, wfmStrings.lastQuarter()),
                new SelectItem(4, wfmStrings.thisYear()),
                new SelectItem(5, wfmStrings.lastYear())
        });
        dwDateList.setSelected(0);//by default last month

    }

    protected String expenseType() {
        return wfmStrings.expense();
    }
}
