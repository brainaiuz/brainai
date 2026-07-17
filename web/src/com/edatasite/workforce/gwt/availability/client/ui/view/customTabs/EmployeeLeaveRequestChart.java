package com.edatasite.workforce.gwt.availability.client.ui.view.customTabs;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.client.ui.MaterialPanel;
import org.moxieapps.gwt.highcharts.client.*;
import org.moxieapps.gwt.highcharts.client.labels.DataLabels;
import org.moxieapps.gwt.highcharts.client.labels.StackLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.ColumnPlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.PlotOptions;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: HRS
 * Date: 06.12.2009
 * Time: 16:24:09
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeLeaveRequestChart extends Composite {

    private Integer selectedYear;
    private Integer reasonID;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private Integer employeeID;
    private MaterialPanel panel;
    private Chart chart;
    private Date startDate;

    public EmployeeLeaveRequestChart() {
    }

    public EmployeeLeaveRequestChart(Integer employeeID, Integer selectedYear) {
        this.employeeID = employeeID;
        if (selectedYear != null) {
            this.selectedYear = selectedYear;
        }
        init();
    }

    public void init() {
        panel = new MaterialPanel("pg_leave__chart-content");
        viewShow();

        initWidget(panel);
    }

    public void viewShow() {
        drawTab();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_APPROVED, EmployeeLeaveRequestChart.this, (sender, args) -> drawTab());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_REJECTED, EmployeeLeaveRequestChart.this, (sender, args) -> drawTab());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_DELETE, EmployeeLeaveRequestChart.this, (sender, args) -> drawTab());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_RQUEST_APPROVED, EmployeeLeaveRequestChart.this, (sender, args) -> drawTab());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_RQUEST_REJECTED, EmployeeLeaveRequestChart.this, (sender, args) -> drawTab());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_ADD, EmployeeLeaveRequestChart.this, (sender, args) -> drawTab());

        MainLayout.get().setSideNavResizeCommand(() -> {
            if (chart != null) {
                Timer timer = new Timer() {
                    @Override
                    public void run() {
                        chart.reflow();
                    }
                };
                timer.schedule(320);
            }
        });
    }

    public void drawTab() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEmployeeId(employeeID);
        fp.setYear(selectedYear);
        fp.setStartDate(startDate == null ? new Date() : startDate);
        fp.setReasonID(this.reasonID);
        LoadingPanel.loading(true);
        DashboardWidgetService.App.get().getLeaveRequestDays(fp, new AbstractAsyncCallback<ChartData>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(ChartData leaveRequestRps) {
                LoadingPanel.loading(false);
                drawChart(leaveRequestRps);
            }
        });
    }

    public String getSVG() {
        String result = null;
        if (chart != null) {
            result = chart.getSVG();
        }
        return result;
    }

    private void drawChart(ChartData chartRpc) {
        panel.clear();
        chart = ChartUtils.generateChart(chartRpc);
        chart.setHeight(475)
                .setChartTitle(new ChartTitle()
                        .setText(chartRpc.getTextInCenter())
                        .setAlign(ChartTitle.Align.CENTER)
                )
                .setLegend(new Legend()
                        .setAlign(Legend.Align.LEFT)
                        .setVerticalAlign(Legend.VerticalAlign.BOTTOM)
                        .setBackgroundColor("#FFFFFF")
                        .setShadow(false)
                        .setItemMarginTop(20)
                ).setCredits(new Credits().setEnabled(false))
                .setToolTip(new ToolTip().setFormatter(toolTipData -> "<b>" + toolTipData.getXAsString() + "</b><br/>" +
                        toolTipData.getSeriesName() + ": " + toolTipData.getYAsLong() + "<br/>")
                );


        chart.setColumnPlotOptions(new ColumnPlotOptions()
                .setDataLabels(new DataLabels()
                        .setEnabled(true)
                        .setColor(ChartUtils.DEFAULT_TEXT_COLOR)
                )
                .setStacking(PlotOptions.Stacking.NORMAL)
        );

        chart.getYAxis()
                .setMin(0)
                .setAxisTitleText(wfmStrings.leaveDays())
                .setStackLabels(new StackLabels()
                        .setEnabled(true)
                        .setStyle(new Style()
                                .setFontWeight("bold")
                                .setColor("gray")
                        )
                );

        panel.add(chart);
    }

    public void setSelectedYear(Integer selectedYear) {
        this.selectedYear = selectedYear;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getReasonID() {
        return reasonID;
    }

    public void setReasonID(Integer reasonID) {
        this.reasonID = reasonID;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}