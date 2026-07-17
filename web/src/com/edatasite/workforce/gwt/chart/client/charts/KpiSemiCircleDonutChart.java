package com.edatasite.workforce.gwt.chart.client.charts;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import org.moxieapps.gwt.highcharts.client.*;
import org.moxieapps.gwt.highcharts.client.labels.DataLabels;
import org.moxieapps.gwt.highcharts.client.labels.PieDataLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.PiePlotOptions;

/**
 * Created by Hurshid on 5/10/2018.
 */
public class KpiSemiCircleDonutChart extends AbstractChart {

    public KpiSemiCircleDonutChart(ChartData chartData, DashboardComponentItem dashboardItem) {
        super(chartData, dashboardItem);
    }

    @Override
    protected void onInitialize() {
        //corricting scales
        setScaleToValues(chartData.getSeries().get(0));
        Point[] points = getPointsByNumberValue(chartData.getSeries().get(0));
        for (int i = 0; i < chartData.getCategories().size() && i < points.length; i++) {
            points[i].setName(chartData.getCategories().get(i));
        }
//        if (chartData.getTextInCenter() != null) {
//            setTextInCenter(chartData.getTextInCenter());
//        }
        setColors(ChartUtils.getDefaultColors());
        setToolTip();

        PieDataLabels labels = new PieDataLabels();
        setLabels(labels);

        addSeries(createSeries()
                .setType(Series.Type.PIE)
                .setPlotOptions(new PiePlotOptions()
                        .setSize(1.4)
                        .setInnerSize(0.75)
                        .setDataLabels(new DataLabels()
                                .setEnabled(true)
                                .setOption("distance", "-50"))
                        .setStartAngle(-90)
                        .setOption("endAngle", 90)
                        .setCenter(0.5, 1)
                        .setDepth(45)
                        .setPieDataLabels(labels)
                        .setShowInLegend(chartConf.getLegend() != null)

                )
                .setPoints(points));
    }

    @Override
    protected void configureDataLabel() {

    }

    @Override
    protected void setChart3DOption() {
        if (is3DViewOption()){
            setOptions3D(new Options3D().setEnabled(true).setAlpha(45).setBeta(0));
        }
    }

    public void setTextInCenter(String textInCenter) {

        setChartTitle(new ChartTitle()
                .setOption("useHTML", true)
                .setText(textInCenter)
                .setStyle(new Style().setColor(ChartUtils.DEFAULT_TEXT_COLOR)
                        .setFontSize("3.15rem !important"))
                .setAlign(ChartTitle.Align.CENTER)
                .setVerticalAlign(ChartTitle.VerticalAlign.MIDDLE)
                .setY(40));
    }

    @Override
    public void setTextInCenter(Number value) {
        setTextInCenter(value, null);
    }

    public void setTextInCenter(Number value, String subtitle) {
        NumberFormat formatter = Utils.getCalculationNumberFormat();

        if (value instanceof Integer || value instanceof Long) {
            formatter = NumberFormat.getFormat(",##0");
        }

        String totalValueName = "";
        if (chartConf.getTotalFieldName() != null && !chartConf.getTotalFieldName().isEmpty()) {
            totalValueName = chartConf.getTotalFieldName() + " : ";
        }
        String strValue = "";
        if (value != null) {
            strValue = Utils.formatWithScale(value, Integer.valueOf(scale));
        }
        String count = strValue;

        String bString = "b";
        if (LocaleInfo.getCurrentLocale().getLocaleName().equals("ru")) {
            bString = "млрд";
        } else if (LocaleInfo.getCurrentLocale().getLocaleName().equals("uz")) {
            bString = "mlrd";
        }

        if (value != null && value.doubleValue() >= 1000000000) {
            count = formatter.format(value.doubleValue() / 1000000000) + bString;
        } else if (value != null && value.doubleValue() >= 1000000) {
            count = formatter.format(value.doubleValue() / 1000000) + "m";
        } else if (value != null && value.doubleValue() >= 100000) {
            count = formatter.format(value.doubleValue() / 1000) + "k";
        }
        setChartTitle(new ChartTitle()
                .setOption("useHTML", true)
                .setText("<dl title=" + strValue + " class=\"chart--circle-text\">\n" +
                        " <dt>" + totalValueName + count + "</dt>\n" +
                        (subtitle != null && subtitle.trim().length() != 0 ? " <dd>" + subtitle + "</dd>\n" : "") +
                        "</dl>")
                .setStyle(new Style().setColor(ChartUtils.DEFAULT_TEXT_COLOR))
                .setAlign(ChartTitle.Align.CENTER)
                .setVerticalAlign(ChartTitle.VerticalAlign.BOTTOM));
    }
}
