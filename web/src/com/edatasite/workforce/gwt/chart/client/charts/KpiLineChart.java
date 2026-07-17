package com.edatasite.workforce.gwt.chart.client.charts;

import com.edatasite.workforce.gwt.chart.client.enums.StackedEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import org.moxieapps.gwt.highcharts.client.labels.DataLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.LinePlotOptions;

public class KpiLineChart extends AbstractChart {

    public KpiLineChart(ChartData chartData, DashboardComponentItem dashboardItem) {
        super(chartData, dashboardItem);
    }

    @Override
    protected void onInitialize() {
        getXAxis().setCategories(chartData.getCategories().toArray(new String[]{}));

        if (chartConf.getStacked() != null) {
            setToolTip();
        }

        addSeries();
    }

    @Override
    protected void configureDataLabel() {
        setLinePlotOptions(
                new LinePlotOptions()
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
//        if (is3DViewOption()){
//            setOptions3D(new Options3D().setEnabled(true).setAlpha(10).setBeta(5).setDepth(70));
//        }
    }
}
