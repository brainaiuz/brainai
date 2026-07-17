package com.edatasite.workforce.gwt.dashboardwidget.client.view.dynamicwidget;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetItem;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.reportingWidgets.KpiReportingWidget;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;

public class DynamicComponent extends DashboardBaseWidget {

    private ChartConfItem chartConf;
    private final boolean isCustomize;
    private final Integer id;

    public DynamicComponent(DashboardComponentItem gridItemConfig, boolean isCustomize, Integer id) {
        this.gridItemConfig = gridItemConfig;
        this.isCustomize = isCustomize;
        this.id = id;
    }

    @Override
    protected void initInternal() {
        super.initInternal();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_RELOAD_DASHBOARD_COMPONENTS + id, DynamicComponent.this, (sender, args) -> loadComponentData());
    }

    @Override
    protected void getData() {

        if (gridItemConfig.getReportId() != null && gridItemConfig.getReportWidgetId() == null) {
            //if this method is in progress
            if (busy) {
                return;
            }
            busy = true;
            LoadingWidgets.get(getCode()).show();
            DashboardWidgetService.App.get().getDynamicComponentTitle(gridItemConfig.getReportId(), new AsyncCallback<ChartConfItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                    busy = false;
                    LoadingWidgets.get(getCode()).hide();
                    clearPanel();
                }

                @Override
                public void onSuccess(ChartConfItem chartConfItem) {
                    String chartTitle = chartConfItem.getTitle();
                    if (chartTitle != null) {
                        Heading heading = new Heading(HeadingSize.H3);
                        heading.setText(chartTitle);
                        heading.setTitle(chartTitle);
                        if (!isCustomize) {
                            MaterialLink runChartReportLink = new MaterialLink();
                            runChartReportLink.add(heading);
                            runChartReportLink.addClickHandler(ch -> {
                                if (chartConfItem.isHasPermission()) {
                                    String addSalesInvoice = GWT.getHostPageBaseURL() + "Reporting.html#reporting|stepControl/" + gridItemConfig.getReportId() + "/savedreport/" + Utils.encrypt(heading.getText());
                                    Window.open(addSalesInvoice, "_blank", "");
                                } else {
                                    Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                                }
                            });
                            setTitle(runChartReportLink);
                        } else {
                            setTitle(heading);
                        }
                    }
                }
            });

            DashboardWidgetService.App.get().getDynamicComponentData(gridItemConfig, new AsyncCallback<ChartData>() {
                @Override
                public void onFailure(Throwable throwable) {
                    busy = false;
                    LoadingWidgets.get(getCode()).hide();
                    clearPanel();
                }

                @Override
                public void onSuccess(ChartData chartData) {
                    busy = false;
                    LoadingWidgets.get(getCode()).hide();
                    contentPanel.clear();
                    if (chartData != null) {
                        chartData.getConf().setSubtitle(chartData.getConf().getTitle());
                        chartData.getConf().setObjectID(gridItemConfig.getReportId());
                    }
                    if (chartData != null && (chartData.getCategories() != null && !chartData.getCategories().isEmpty()
                            || chartData.getConf() != null && ChartTypeEnum.GAUGE_CHART.equals(chartData.getConf().getType()))) {
                        chart = ChartUtils.generateChart(chartData, gridItemConfig);

                        if (!ChartTypeEnum.GAUGE_CHART.equals(chartData.getConf().getType()) && chartData.getCategories().size() > 2) {
                            chart.configureLegend(true);
                        }
                        contentPanel.add(chart);
                    } else if (chartData != null && chartData.getConf() != null) {
                        chartConf = chartData.getConf();
                        noData();
                    }
                }
            });
        } else if (gridItemConfig.getReportWidgetId() != null) {
            if (busy) {
                return;
            }
            busy = true;
            LoadingWidgets.get(getCode()).show();

            DashboardWidgetService.App.get().getDynamicWidgetTitle(gridItemConfig.getReportId(), new AsyncCallback<ReportRpc>() {
                @Override
                public void onFailure(Throwable throwable) {
                    busy = false;
                    LoadingWidgets.get(getCode()).hide();
                    clearPanel();
                }

                @Override
                public void onSuccess(ReportRpc reportRpc) {
                    if (reportRpc == null) return;
                    KpiWidgetItem kpiWidget = reportRpc.getKpiWidgetItem();
                    boolean permission = reportRpc.getChartConf() != null && reportRpc.getChartConf().isHasPermission();
                    String widgetTitle = kpiWidget != null ? kpiWidget.getKpiWidgetTitle() : "No Widget Title";
                    gridItemConfig.setName(widgetTitle);
                    Heading heading = new Heading(HeadingSize.H3);
                    heading.setText(widgetTitle);
                    heading.setTitle(widgetTitle);
                    if (!isCustomize) {
                        heading.getElement().addClassName("has-link");
                        heading.addClickHandler(ch -> {
                            if (permission) {
                                String addSalesInvoice = GWT.getHostPageBaseURL() + "Reporting.html#reporting|stepControl/" + gridItemConfig.getReportId() + "/savedreport/" + Utils.encrypt(heading.getText());
                                Window.open(addSalesInvoice, "_blank", "");
                            } else {
                                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                            }
                        });

                    }
                    setTitle(heading);
                }
            });

            DashboardWidgetService.App.get().getDynamicWidgetComponentData(gridItemConfig, new AsyncCallback<KpiWidgetData>() {
                @Override
                public void onFailure(Throwable throwable) {
                    busy = false;
                    LoadingWidgets.get(getCode()).hide();
                    clearPanel();
                }

                @Override
                public void onSuccess(KpiWidgetData kpiWidgetData) {
                    if (kpiWidgetData != null) {
                        kpiWidgetData.setObjectId(gridItemConfig.getReportId());
                    }
                    WidgetRequest request = new WidgetRequest(kpiWidgetData, id);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WIDGET_DATA_RECEIVED, request, DynamicComponent.this);
                    busy = false;
                    LoadingWidgets.get(getCode()).hide();
                    contentPanel.clear();

                    if (kpiWidgetData != null) {
                        KpiReportingWidget widget = new KpiReportingWidget(false);
                        widget.setData(kpiWidgetData);
                        widget.init();
                        contentPanel.add(widget);
                    } else {
                        Div noDataContent = new Div("chart-no-data");
                        noDataContent.getElement().setInnerHTML(wfmStrings.noDataAvailable());
                        contentPanel.add(noDataContent);
                    }
                }
            });
        }
    }

    @Override
    protected void getSampleData(boolean nodata) {
        getData();
    }

    @Override
    protected void noData() {
        contentPanel.clear();

        Div noDataContent = new Div("chart-no-data");
        noDataContent.getElement().setInnerHTML(wfmStrings.noDataAvailable());
        contentPanel.add(noDataContent);

        if (chartConf != null) {
            Heading heading = new Heading(HeadingSize.H3);
            heading.setText(chartConf.getTitle());
            heading.setTitle(chartConf.getTitle());
            if (!isCustomize) {
                heading.getElement().addClassName("has-link");
                heading.addClickHandler(ch -> {
                    if (chartConf.isHasPermission()) {
                        String addSalesInvoice = GWT.getHostPageBaseURL() + "Reporting.html#reporting|stepControl/" + gridItemConfig.getReportId() + "/savedreport/" + Utils.encrypt(heading.getText());
                        Window.open(addSalesInvoice, "_blank", "");
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    }
                });

            }
            setTitle(heading);

            ChartData chartData = null;


            if (ChartTypeEnum.LINE_CHART.equals(chartConf.getType())) {
                chartData = ChartUtils.getDefaultLineChartData();
            } else if (ChartTypeEnum.AREA_CHART.equals(chartConf.getType())) {
                chartData = ChartUtils.getDefaultAreaChartData();
            } else if (ChartTypeEnum.VERTICAL_BAR_CHART.equals(chartConf.getType())) {
                chartData = ChartUtils.getDefaultBarChartData(chartConf.getType(), chartConf.getStacked() != null ? chartConf.getStacked().getId() : null);
            } else if (ChartTypeEnum.HORIZONTAL_BAR_CHART.equals(chartConf.getType())) {
                chartData = ChartUtils.getDefaultBarChartData(chartConf.getType(), chartConf.getStacked() != null ? chartConf.getStacked().getId() : null);
            } else if (ChartTypeEnum.PIE_CHART.equals(chartConf.getType())) {
                chartData = ChartUtils.getDefaultDataForOtherCharts(chartConf.getType());
            } else if (ChartTypeEnum.DONUT_CHART.equals(chartConf.getType())) {
                chartData = ChartUtils.getDefaultDataForOtherCharts(chartConf.getType());
            } else if (ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART.equals(chartConf.getType())) {
                chartData = ChartUtils.getDefaultDataForOtherCharts(chartConf.getType());
            } else if (ChartTypeEnum.FUNNEL_CHART.equals(chartConf.getType())) {
                chartData = ChartUtils.getDefaultDataForOtherCharts(chartConf.getType());
            } else if (ChartTypeEnum.PYRAMID_CHART.equals(chartConf.getType())) {
                chartData = ChartUtils.getDefaultDataForOtherCharts(chartConf.getType());
            }

            if (chartData != null) {
                chartData.setConf(chartConf);
                chart = ChartUtils.generateChart(chartData);
                chart.setColors(ChartUtils.NO_DATA_COLOR);
                contentPanel.add(chart);
            }
        }
    }

    @Override
    public String getCode() {
        return gridItemConfig.getComponentCode();
    }
}
