package com.edatasite.workforce.gwt.chart.client.charts;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import org.moxieapps.gwt.highcharts.client.ChartTitle;
import org.moxieapps.gwt.highcharts.client.Drilldown;
import org.moxieapps.gwt.highcharts.client.Options3D;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Style;
import org.moxieapps.gwt.highcharts.client.labels.PieDataLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.PiePlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.PlotOptions;

public class KpiPieChart extends AbstractChart {

    public KpiPieChart(ChartData chartData, DashboardComponentItem dashboardItem) {
        super(chartData, dashboardItem);
    }

    @Override
    protected void onInitialize() {
        if (chartData.getDrillSeries() != null && chartData.getDrillSeries().size() > 0) {
            setDrilldown(new Drilldown().setSeries(generateDrillSeries()));
        }
        //correcting scales
        setScaleToValues(chartData.getSeries().get(0));
        Point[] points = getPointsByNumberValue(chartData.getSeries().get(0));
        for (int i = 0; i < chartData.getCategories().size() && i < points.length; i++) {
            points[i].setName(chartData.getCategories().get(i));
            if (chartData.getDrillSeries() != null && chartData.getDrillSeries().size() > 0) {
                points[i].setDrilldownSeries(drillSeriesMap.get(chartData.getCategories().get(i)));
            }
        }
        setToolTip();
        addSeries(createSeries().setPoints(points));
    }

    @Override
    protected void configureDataLabel() {
        PieDataLabels labels = new PieDataLabels();
        setLabels(labels);

        setPiePlotOptions(new PiePlotOptions()
                .setAllowPointSelect(true)
                .setCursor(PlotOptions.Cursor.POINTER)
                .setDepth(45)
                .setPieDataLabels(labels)
                .setShowInLegend(chartConf.getLegend() != null)
                .setAllowPointSelect(true)
        );
    }

    @Override
    protected void setChart3DOption() {
        if (is3DViewOption()) {
            setOptions3D(new Options3D().setEnabled(true).setAlpha(45).setBeta(0));
        }
    }

    @Override
    public void setTextInCenter(Number value) {
        if (value == null) {
            return;
        }
        setTextInCenter(value, null);
    }

    public void setTextInCenter(Number value, String subtitle) {
        NumberFormat formatter = Utils.getCalculationNumberFormat();

        if (value instanceof Integer || value instanceof Long) {
            formatter = NumberFormat.getFormat(",##0");
        }

        String scale = chartConf.getScale() == null || chartConf.getScale().equals("") ? "0" : chartConf.getScale();
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
                .setAlign(ChartTitle.Align.RIGHT)
                .setVerticalAlign(ChartTitle.VerticalAlign.BOTTOM)
                .setX(-10));
    }
}
