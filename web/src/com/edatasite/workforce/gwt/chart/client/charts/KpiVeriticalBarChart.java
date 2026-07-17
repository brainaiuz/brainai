package com.edatasite.workforce.gwt.chart.client.charts;

import com.edatasite.workforce.gwt.chart.client.enums.StackedEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieData;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import org.moxieapps.gwt.highcharts.client.Options3D;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.labels.DataLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.ColumnPlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.PlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.SeriesPlotOptions;

import java.util.Optional;

public class KpiVeriticalBarChart extends AbstractChart {

    public KpiVeriticalBarChart(ChartData chartData, DashboardComponentItem dashboardItem) {
        super(chartData, dashboardItem);
    }

    @Override
    protected void onInitialize() {
        if (chartConf.isShowStacked()) {
            setSeriesPlotOptions(new SeriesPlotOptions()
                    .setStacking(StackedEnum.BY_PERCENT.equals(chartConf.getStacked()) ? PlotOptions.Stacking.PERCENT : PlotOptions.Stacking.NORMAL)
                    .setCursor(PlotOptions.Cursor.POINTER)
                    .setPointClickEventHandler(pointClickEvent -> {
                        int index = pointClickEvent.getPoint().getX().intValue();
                        String name = ct.get(index);
                        String addSalesInvoice = GWT.getHostPageBaseURL() + "Reporting.html#reporting|stepControl/" + dashboardItem.getReportId() + "/savedreport/" + Utils.encrypt(dashboardItem.getName()) + "/" + null + "/" + Utils.encrypt(name);
                        Window.open(addSalesInvoice, "_blank", "");
                        return false;
                    })
            );
        }

        getXAxis().setCategories(chartData.getCategories().toArray(new String[]{}));
        if (chartConf.getStacked() != null) {
            setToolTip();
        }

        // Prevent visual overlap: Reserve 35% empty space on the right side of the canvas
        // so the extending horizontal bars never collide with the pie chart.
        if (chartConf.isShowPieChart()) {
            getYAxis().setMaxPadding(0.35);
        }

        addSeries();

        // We must override the root series colors here so the legend matches the custom bar data colors.
        if (chartData.getSeries() != null) {
            org.moxieapps.gwt.highcharts.client.Series[] activeSeries = this.getSeries();
            for (int i = 0; i < activeSeries.length; i++) {
                if (i < chartData.getSeries().size()) {
                    String hexColor = chartData.getSeries().get(i).getColor();
                    if (hexColor != null && !hexColor.isEmpty()) {
                        activeSeries[i].setOption("color", hexColor);
                    }
                }
            }
        }

        // Render the nested Pie Chart summary overlay if enabled in the UI
        if (chartConf.isShowPieChart()) {
            org.moxieapps.gwt.highcharts.client.Series pieSeries = this.createSeries();
            pieSeries.setType(org.moxieapps.gwt.highcharts.client.Series.Type.PIE);
            pieSeries.setName("Total Breakdown");

            // Default to Left
            String hPos = "15%";
            if ("CENTER".equals(chartConf.getPieChartPosition())) {
                hPos = "50%";
            } else if ("RIGHT".equals(chartConf.getPieChartPosition())) {
                hPos = "85%";
            }

// X changes based on user selection, Y stays locked safely in the top headroom (18%)
            pieSeries.setOption("center", new String[]{hPos, "18%"});

            pieSeries.setOption("size", "25%");
            pieSeries.setOption("showInLegend", false);

            // Pre-calculate the total sum of all data points to accurately compute slice percentages
            double pieTotal = 0.0;
            if (chartData.getSeries() != null) {
                for (SerieData sData : chartData.getSeries()) {
                    double aggregateValue = 0.0;
                    if (sData.getValues() != null) {
                        for (Number value : sData.getValues()) {
                            if (value != null) {
                                aggregateValue += value.doubleValue();
                            }
                        }
                    }
                    if (aggregateValue > 0) {
                        pieTotal += aggregateValue;
                    }
                }
            }

            final double finalPieTotal = pieTotal;

            // Check if the user selected a label format (Value, Percent, or Both)
            boolean isLabelSelected = chartConf.getStacked() != null && chartConf.getStacked().getId() != -1;

            // Configure pie data labels to respect the standard "Show Serie" and "Show Label" widget configurations
            SeriesPlotOptions piePlotOptions = new SeriesPlotOptions();
            piePlotOptions.setDataLabels(new DataLabels()
                    .setEnabled(isLabelSelected) // Hides labels completely if nothing is selected
                    .setFormatter(labelFormatter -> {
                        if (!isLabelSelected) {
                            return null;
                        }

                        String pointName = labelFormatter.getPointName();
                        String value = Utils.formatWithScale(labelFormatter.getYAsDouble(), Integer.valueOf(scale));
                        String percentage = finalPieTotal > 0
                                ? Utils.formatWithScale((labelFormatter.getYAsDouble() / finalPieTotal) * 100, Integer.valueOf(scale))
                                : "0";

                        // Apply the correct text format based on user selection
                        String labelMetrics;
                        if (StackedEnum.BY_PERCENTANDVALUE.equals(chartConf.getStacked())) {
                            labelMetrics = "<b>" + value + "</b> (" + percentage + "%)";
                        } else if (StackedEnum.BY_PERCENT.equals(chartConf.getStacked())) {
                            labelMetrics = "<b>" + percentage + "%</b>";
                        } else {
                            labelMetrics = "<b>" + value + "</b>";
                        }

                        // Prepend the category name if "Show Serie" is enabled
                        if (chartConf.isShowSerie()) {
                            return "<span>" + pointName + "</span>: " + labelMetrics;
                        } else {
                            return labelMetrics;
                        }
                    })
            );
            pieSeries.setPlotOptions(piePlotOptions);

            // Map the data points into the pie chart
            if (chartData.getSeries() != null) {
                for (SerieData sData : chartData.getSeries()) {
                    double aggregateValue = 0.0;
                    if (sData.getValues() != null) {
                        for (Number value : sData.getValues()) {
                            if (value != null) {
                                aggregateValue += value.doubleValue();
                            }
                        }
                    }
                    if (aggregateValue > 0) {
                        Point piePoint = new org.moxieapps.gwt.highcharts.client.Point(sData.getName(), aggregateValue);

                        // Sync Colors: Bind the exact hex color from the parent bar series to the pie slice
                        if (sData.getColor() != null && !sData.getColor().isEmpty()) {
                            piePoint.setColor(sData.getColor());
                        }

                        pieSeries.addPoint(piePoint);
                    }
                }
            }
            this.addSeries(pieSeries);
        }
    }

    @Override
    protected void configureDataLabel() {
        String scale = chartConf.getScale() == null || chartConf.getScale().equals("") ? "0" : chartConf.getScale();
        Double[] categoryTotals = new Double[chartData.getCategories().size()];
        for (SerieData data : chartData.getSeries()) {
            for (int i = 0; i < data.getValues().length; i++) {
                categoryTotals[i] = Optional.ofNullable(categoryTotals[i]).orElse(0d) + (data.getValues()[i] != null ? data.getValues()[i].longValue() : 0L);
            }
        }

        setColumnPlotOptions(
                new ColumnPlotOptions()
                        .setDataLabels(
                                new DataLabels()
                                        .setFormatter(labelFormatter -> {
                                            String percentage = Utils.formatWithScale(labelFormatter.getYAsDouble() / categoryTotals[chartData.getCategories().indexOf(labelFormatter.getXAsString())] * 100, Integer.valueOf(scale));
                                            String serieName = labelFormatter.getSeriesName();
                                            String value = Utils.formatWithScale(labelFormatter.getYAsDouble(), Integer.valueOf(scale));
                                            String format;
                                            if (StackedEnum.BY_PERCENTANDVALUE.equals(chartConf.getStacked())) {
                                                format = chartConf.isShowSerie() ? "<span>" + serieName + "</span>: <b>" + value + "</b> (" + percentage + "%)<br/>" : "<b>" + value + "</b> (" + percentage + "%)<br/>";
                                            } else if (StackedEnum.BY_PERCENT.equals(chartConf.getStacked())) {
                                                format = chartConf.isShowSerie() ? "<span>" + serieName + "</span>: <b>" + percentage + "%</b><br/>" : "<b>" + percentage + "%</b><br/>";
                                            } else {
                                                format = chartConf.isShowSerie() ? "<span>" + serieName + "</span>: <b>" + value + "</b><br/>" : "<b>" + value + "</b><br/>";
                                            }
                                            return format;
                                        })
                        )
        );
    }

    @Override
    protected void setChart3DOption() {
        if (is3DViewOption()) {
            setOptions3D(new Options3D().setEnabled(true).setAlpha(30));
            if ("CYLINDER".equals(selectedOptionType)) {
                setOption("/chart/type", "cylinder");
            } else if ("LINE_STACKING".equals(selectedOptionType)) {
                setOption("/chart/type", "column");
                setColumnPlotOptions(new ColumnPlotOptions().setStacking(PlotOptions.Stacking.NORMAL));
            } else if ("CYLINDER_STACKING".equals(selectedOptionType)) {
                setOption("/chart/type", "cylinder");
                setSeriesPlotOptions(new SeriesPlotOptions().setStacking(PlotOptions.Stacking.NORMAL));
            } else {
                setOption("/chart/type", "column");
            }
        }
    }
}
