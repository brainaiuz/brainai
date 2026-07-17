package com.edatasite.workforce.gwt.reportingsystem.client.ui.widget;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;
import org.moxieapps.gwt.highcharts.client.*;
import org.moxieapps.gwt.highcharts.client.labels.StackLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.ColumnPlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.ColumnRangePlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.PlotOptions;

/**
 * Created by Virus on 8/23/14.
 */
public class ReportingCategory extends Composite {
    interface ReportingCategoryUiBinder extends UiBinder<HTMLPanel, ReportingCategory> {
    }

    private static final WfmStrings wfmStringss = WfmStrings.App.get();
    private static ReportingCategoryUiBinder ourUiBinder = GWT.create(ReportingCategoryUiBinder.class);

    @UiField
    Span title;
    @UiField
    Div reportItems;
    @UiField
    Div headerDiv;
    @UiField
    HTMLPanel li;
    @UiField
    Div chartPanel;
    private FolderRpc folderRpc;
    private boolean active;

    public ReportingCategory(FolderRpc folderRpc, boolean active) {
        initWidget(ourUiBinder.createAndBindUi(this));
        title.setText(folderRpc.getName());
        this.folderRpc = folderRpc;
        addReports();
        if (active) {
            headerDiv.addStyleName("active");
            li.addStyleName("active");
        } else {
            headerDiv.removeStyleName("active");
            li.removeStyleName("active");
        }
        headerDiv.addClickHandler((e) -> {
            if (headerDiv.getStyleName().contains("active")) {
                headerDiv.removeStyleName("active");
                li.removeStyleName("active");
            } else {
                headerDiv.addStyleName("active");
                li.addStyleName("active");
            }
        });
        drawChart();
    }

    public void addReports() {
        int count = 0;
        for (SelectListRpc rpc : folderRpc.getReports()) {
            reportItems.add(new ReportMetaWidget(new SelectItem(rpc.getId(), rpc.getName())));
            count++;
            if (count == 6) {
                reportItems.add(new WfmButton2(wfmStringss.viewAll(), "btn btn--default btn-block hasicon--center", "ficon--menu", (e) -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("folder|view/" + folderRpc.getName() + "/" + folderRpc.getCategoryId());
                }));
                break;
            }
        }
    }

    private void drawChart() {
        final Chart chart = new Chart()
                .setType(Series.Type.COLUMN)
                .setChartTitle(new ChartTitle()
                        .setText("Title Name")
                )
                .setColumnPlotOptions(new ColumnPlotOptions()
                        .setStacking(PlotOptions.Stacking.NORMAL)

                )
                .setLegend(new Legend()
                        .setBackgroundColor("#FFFFFF")
                        .setShadow(true)
                ).setCredits(new Credits().setEnabled(false))
                .setToolTip(new ToolTip().setFormatter(toolTipData -> "<b>" + toolTipData.getXAsString() + "</b><br/>" +
                        toolTipData.getSeriesName() + ": " + toolTipData.getYAsLong() + "<br/>")
                );
        chart.setBackgroundColor("#f2f5f7");
        chart.setHeight(280);
//        chart.getElement().getStyle().setHeight(100, com.google.gwt.dom.client.Style.Unit.PCT);
        chart.getXAxis()
                .setCategories("category 1", "category 2", "category 3");

        chart.getYAxis()
                .setMin(0)
                .setAxisTitleText("Axis title")
                .setStackLabels(new StackLabels()
                        .setEnabled(true)
                        .setStyle(new Style()
                                .setFontWeight("bold")
                                .setColor("gray")
                        )
                );

        Integer[] data = new Integer[]{1, 2, 3};

        chart.addSeries(chart.createSeries()
                .setPlotOptions(new ColumnRangePlotOptions().setColor("#FD504F"))
                .setName("series 1")
                .setPoints(data)
        );

        chart.addSeries(chart.createSeries()
                .setPlotOptions(new ColumnRangePlotOptions().setColor("#FED851"))
                .setName("series 2")
                .setPoints(data)
        );

        chart.addSeries(chart.createSeries()
                .setPlotOptions(new ColumnRangePlotOptions().setColor("#86CA40"))
                .setName("series 3")
                .setPoints(data)
        );
        chartPanel.add(chart);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SIDENAV_RESIZE, ReportingCategory.this, ((sender, args) -> {
           chart.reflow();
        }));
    }

    /**
     * <button type="button" class="btn btn--default btn-block hasicon--center">
     * <i class="ficon--menu"></i>
     * <span>View All</span>
     * </button>
     */

    public String getTitle() {
        return title.getText();
    }

}