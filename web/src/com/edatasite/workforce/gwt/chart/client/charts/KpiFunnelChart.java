package com.edatasite.workforce.gwt.chart.client.charts;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import org.moxieapps.gwt.highcharts.client.BaseChart;
import org.moxieapps.gwt.highcharts.client.ChartTitle;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Style;
import org.moxieapps.gwt.highcharts.client.labels.FunnelDataLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.FunnelPlotOptions;

public class KpiFunnelChart extends AbstractChart {

    public KpiFunnelChart(ChartData chartData, DashboardComponentItem dashboardItem) {
        super(chartData, dashboardItem);
    }

    @Override
    protected void onInitialize() {
        setZoomType(BaseChart.ZoomType.X_AND_Y);
        //corricting scales
        setScaleToValues(chartData.getSeries().get(0));
        Point[] points = getPointsByNumberValue(chartData.getSeries().get(0));
        for (int i = 0; i < chartData.getCategories().size() && i < points.length; i++) {
            points[i].setName(chartData.getCategories().get(i));
        }
        setToolTip();
        addSeries(createSeries().setPoints(points));
    }

    @Override
    protected void configureDataLabel() {
        FunnelDataLabels labels = new FunnelDataLabels();
        setLabels(labels);

        setFunnelPlotOptions(new FunnelPlotOptions()
                .setFunnelDataLabels(labels)
                .setCenter(.4, .5)
                .setDepth(45)
                .setNeckWidth("10%")
                .setNeckHeight("15%")
                .setWidth("40%"));
    }

    @Override
    protected void setChart3DOption() {
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

        String strValue = Utils.formatWithScale(value, Integer.valueOf(scale));
        String count = strValue;

        String bString = "b";
        String totalStr = "Total : ";
        if (chartConf.getTotalFieldName() != null && !chartConf.getTotalFieldName().isEmpty()) {
            totalStr = chartConf.getTotalFieldName() + " : ";
        } else {
            if (LocaleInfo.getCurrentLocale().getLocaleName().equals("ru")) {
                totalStr = "Итого : ";
            } else if (LocaleInfo.getCurrentLocale().getLocaleName().equals("uz")) {
                totalStr = "Jami : ";
            }
        }
        if (LocaleInfo.getCurrentLocale().getLocaleName().equals("ru")) {
            bString = "млрд";
        } else if (LocaleInfo.getCurrentLocale().getLocaleName().equals("uz")) {
            bString = "mlrd";
        }

        if (value.doubleValue() >= 1000000000) {
            count = formatter.format(value.doubleValue() / 1000000000) + bString;
        } else if (value.doubleValue() >= 1000000) {
            count = formatter.format(value.doubleValue() / 1000000) + "m";
        } else if (value.doubleValue() >= 100000) {
            count = formatter.format(value.doubleValue() / 1000) + "k";
        }
        setChartTitle(new ChartTitle()
                .setOption("useHTML", true)
                .setText("<dl title=" + strValue + " class=\"chart--circle-text\">\n" +
                        " <dt>" + totalStr + count + "</dt>\n" +
                        "</dl>")
                .setStyle(new Style().setColor(ChartUtils.DEFAULT_TEXT_COLOR))
                .setAlign(ChartTitle.Align.RIGHT)
                .setVerticalAlign(ChartTitle.VerticalAlign.BOTTOM)
                .setX(-10));
    }

}
