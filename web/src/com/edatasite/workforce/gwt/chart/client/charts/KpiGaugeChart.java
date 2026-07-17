package com.edatasite.workforce.gwt.chart.client.charts;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import org.moxieapps.gwt.highcharts.client.*;
import org.moxieapps.gwt.highcharts.client.labels.*;
import org.moxieapps.gwt.highcharts.client.plotOptions.SolidGaugePlotOptions;


public class KpiGaugeChart extends AbstractChart {

    public KpiGaugeChart(ChartData chartData, DashboardComponentItem dashboardItem) {
        super(chartData, dashboardItem);
    }

    @Override
    protected void onInitialize() {
        NumberFormat formatter = Utils.getCalculationNumberFormat();
        String[] colors = chartConf.getGaugeConfig() != null ? getGaugeChartStopColors() : null;
        JSONArray minValColor = new JSONArray();
        Number resultValColor = calculatePartOfValue(formatter.parse(formatter.format(chartData.getGaugeMaxValue())), formatter.parse(formatter.format(chartData.getGaugeActValue())));
        minValColor.set(0, new JSONNumber(0.1));
        minValColor.set(1, new JSONString(colors != null ? colors[0] : getDefaultColor(chartConf, "#55BF3B")));

        JSONArray normValColor = new JSONArray();
        normValColor.set(0, new JSONNumber(0.5));
        normValColor.set(1, new JSONString(colors != null ? colors[1] : getDefaultColor(chartConf, "#DDDF0D")));

        JSONArray maxValColor = new JSONArray();
        maxValColor.set(0, new JSONNumber(0.9));
        maxValColor.set(1, new JSONString(colors != null ? colors[2] : getDefaultColor(chartConf, "#DF5353")));

        JSONArray stops = new JSONArray();
        stops.set(0, minValColor);
        stops.set(1, normValColor);
        stops.set(2, maxValColor);

        setPane(new Pane()
                .setCenter("50%", "85%")
                .setSize("100%")
                .setStartAngle(-90)
                .setEndAngle(90)
                .setBackground(new PaneBackground()
                        .setInnerRadius("60%")
                        .setOuterRadius("100%")
                        .setShape(PaneBackground.Shape.ARC)));

        Number minValue = formatter.parse(formatter.format(chartData.getGaugeMinValue()));
        Number maxValue = formatter.parse(formatter.format(chartData.getGaugeMaxValue()));
        Number actValue = formatter.parse(formatter.format(chartData.getGaugeActValue()));

        getYAxis()
                .setMin(minValue)
                .setMax(maxValue)
                .setLineWidth(0)
                .setMinorTickInterval(null)
                .setOption("tickAmount", 0)
                .setTickPixelInterval(0)
                .setTickInterval(0.1)
                .setTickWidth(0)
                .setOption("stops", stops)
                .setAxisTitle(new AxisTitle().setY(-70))
                .setLabels(ChartUtils.numFormat());
        setToolTip(new ToolTip().setEnabled(false));

        Series series = createSeries();
        series.setType(Series.Type.SOLID_GAUGE);
        series.setName("Speed");
        series.setPoints(new Number[]{actValue});
        series.setPlotOptions(new SolidGaugePlotOptions().setDataLabels(ChartUtils.dataLabels4ActVal()));
        addSeries(series);

        //this serie drawing a gauge arrow
        Series series1 = createSeries();
        series1.setType(Series.Type.GAUGE);
        series1.setName("Speed");
        series1.setPoints(new Number[]{actValue});
        addSeries(series1);
    }

    @Override
    protected void configureDataLabel() {

    }

    private Number calculatePartOfValue(Number maxVal, Number actVal) {
        return actVal.doubleValue() / maxVal.doubleValue();
    }

    private String getDefaultColor(ChartConfItem data, String defaultColor) {
        if (data != null && data.getGaugeConfig() != null && data.getGaugeConfig().getChartColor() != null) {
            return data.getGaugeConfig().getChartColor();
        }
        return defaultColor;
    }

    @Override
    protected void setChart3DOption() {
//        if (is3DViewOption()){
//            setOptions3D(new Options3D().setEnabled(true).setAlpha(10).setBeta(5).setDepth(70));
//        }
    }
}
