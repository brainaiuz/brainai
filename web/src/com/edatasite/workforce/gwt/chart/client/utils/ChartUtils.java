package com.edatasite.workforce.gwt.chart.client.utils;

import com.edatasite.workforce.gwt.chart.client.charts.AbstractChart;
import com.edatasite.workforce.gwt.chart.client.charts.KpiAreaChart;
import com.edatasite.workforce.gwt.chart.client.charts.KpiDonutChart;
import com.edatasite.workforce.gwt.chart.client.charts.KpiFunnelChart;
import com.edatasite.workforce.gwt.chart.client.charts.KpiGaugeChart;
import com.edatasite.workforce.gwt.chart.client.charts.KpiHorizontalBarChart;
import com.edatasite.workforce.gwt.chart.client.charts.KpiLineChart;
import com.edatasite.workforce.gwt.chart.client.charts.KpiPieChart;
import com.edatasite.workforce.gwt.chart.client.charts.KpiPyramidChart;
import com.edatasite.workforce.gwt.chart.client.charts.KpiSemiCircleDonutChart;
import com.edatasite.workforce.gwt.chart.client.charts.KpiVeriticalBarChart;
import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.enums.LegendPositionEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieData;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.google.gwt.user.client.rpc.IsSerializable;
import org.moxieapps.gwt.highcharts.client.Style;
import org.moxieapps.gwt.highcharts.client.labels.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import static com.edatasite.workforce.gwt.chart.client.enums.StackedEnum.BY_PERCENT;

/**
 * Created by Virus on 9/26/14.
 */
public class ChartUtils implements IsSerializable {
    public static final String DEFAULT_TEXT_COLOR = "#536677";
    public static final String NO_DATA_COLOR = "#ECEFF3";

    public static String[] getDefaultColors() {
        return new String[]
                {
                        "#FBA800",
                        "#158ED0",
                        "#8ACD00",
                        "#8F4AA5",
                        "#E43A9D",
                        "#FF0022",
                        "#00BC6B",
                        "#2261AB",
                        "#0F9834",
                        "#7446AE",
                        "#ECA900",
                        "#9D75B3",
                        "#CC6BC0",
                        "#749152",
                        "#00C836",
                        "#6D1789",
                        "#E0CC4F",
                        "#C6AC1D",
                        "#12805C",
                        "#8097C8"
                };
    }

    public static String[] getDefaultColorsWithLowOpacity() {
        return new String[]
                {
                        "rgba(250, 167, 0, 0.5)",
                        "rgba(21, 142, 208, 0.5)",
                        "rgba(138, 205, 0, 0.5)",
                        "rgba(143, 74, 165, 0.5)",
                        "rgba(228, 58, 157, 0.5)",
                        "rgba(255, 0, 34, 0.5)",
                        "rgba(0, 188, 107, 0.5)",
                        "rgba(34, 97, 171, 0.5)",
                        "rgba(15, 152, 52, 0.5)",
                        "rgba(116, 70, 174, 0.5)",
                        "rgba(236, 169, 0, 0.5)",
                        "rgba(157, 117, 179, 0.5)",
                        "rgba(204, 107, 192, 0.5)",
                        "rgba(116, 145, 82, 0.5)",
                        "rgba(0, 199, 53, 1)",
                        "rgba(109, 23, 137, 0.5)",
                        "rgba(224, 204, 79, 0.5)",
                        "rgba(198, 172, 29, 0.5)",
                        "rgba(18, 128, 92, 0.5)",
                        "rgba(128, 151, 200, 0.5)",
                };
    }

    public static AbstractChart generateChart(ChartData generateChart) {
        return generateChart(generateChart, null);
    }

    public static YAxisLabels numFormat() {
        return new org.moxieapps.gwt.highcharts.client.labels.YAxisLabels()
                .setY(16)
                .setFormatter(axisLabelsData -> getStringFormat(axisLabelsData.getValueAsDouble()));
    }

    public static DataLabels dataLabels4ActVal() {
        return new DataLabels()
                .setEnabled(true)
                .setY(25)
                .setBorderWidth(0)
                .setUseHTML(true)
                .setFormatter((DataLabelsData data) -> getStringFormat(data.getYAsDouble()));
    }

    private static String getStringFormat(double value) {
        double scaled;
        String suffix;
        if (value >= 1_000_000_000) {
            scaled = value / 1_000_000_000d;
            suffix = " bln";
        } else if (value >= 1_000_000) {
            scaled = value / 1_000_000d;
            suffix = " mln";
        } else if (value >= 1_000) {
            scaled = value / 1_000d;
            suffix = " k";
        } else {
            scaled = value;
            suffix = "";
        }
        if (Math.abs(Math.round(scaled * 100.0) / 100.0 - Math.rint(Math.round(scaled * 100.0) / 100.0)) < 0.00001)
            return ((int) Math.round(scaled * 100.0) / 100.0) + suffix;
        else return Math.round(scaled * 100.0) / 100.0 + suffix;
    }

    public static AbstractChart generateChart(ChartData chartData, DashboardComponentItem dashboardItem) {
        AbstractChart chart = null;

        if (ChartTypeEnum.FUNNEL_CHART.equals(chartData.getConf().getType()) && chartData.getConf().getSelectedchartTypeId() != -1) {
            chartData.getConf().setType(ChartTypeEnum.getById(chartData.getConf().getSelectedchartTypeId()) != null ?
                    ChartTypeEnum.getById(chartData.getConf().getSelectedchartTypeId()) : chartData.getConf().getType());
        }

        if (ChartTypeEnum.LINE_CHART.equals(chartData.getConf().getType())) {
            chart = new KpiLineChart(chartData, dashboardItem);
        } else if (ChartTypeEnum.AREA_CHART.equals(chartData.getConf().getType())) {
            chart = new KpiAreaChart(chartData, dashboardItem);
        } else if (ChartTypeEnum.VERTICAL_BAR_CHART.equals(chartData.getConf().getType())) {
            chart = new KpiVeriticalBarChart(chartData, dashboardItem);
        } else if (ChartTypeEnum.HORIZONTAL_BAR_CHART.equals(chartData.getConf().getType())) {
            chart = new KpiHorizontalBarChart(chartData, dashboardItem);
        } else if (ChartTypeEnum.PIE_CHART.equals(chartData.getConf().getType())) {
            chart = new KpiPieChart(chartData, dashboardItem);
        } else if (ChartTypeEnum.DONUT_CHART.equals(chartData.getConf().getType())) {
            chart = new KpiDonutChart(chartData, dashboardItem);
        } else if (ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART.equals(chartData.getConf().getType())) {
            chart = new KpiSemiCircleDonutChart(chartData, dashboardItem);
        } else if (ChartTypeEnum.FUNNEL_CHART.equals(chartData.getConf().getType())) {
            chart = new KpiFunnelChart(chartData, dashboardItem);
        } else if (ChartTypeEnum.GAUGE_CHART.equals(chartData.getConf().getType())) {
            chart = new KpiGaugeChart(chartData, dashboardItem);
        } else if (ChartTypeEnum.PYRAMID_CHART.equals(chartData.getConf().getType())) {
            chart = new KpiPyramidChart(chartData, dashboardItem);
        }
        boolean isStacked = chartData.getConf().getStacked() != null;

        chart.getYAxis()
                .setStackLabels(new StackLabels()
                        .setEnabled(isStacked)
                        .setVerticalAlign(Labels.VerticalAlign.TOP)
                        .setFormatter(stackLabelsData -> Utils.formatWithScale(stackLabelsData.getTotal(), chartData.getConf().getScale() != null ? Integer.valueOf(chartData.getConf().getScale()) : 0))
                        .setOption("allowOverlap", true)
                        .setOption("overflow", "allow")
                        .setOption("crop", false)
                        .setStyle(new Style().setOption("visibility", "hidden"))
                )
                .setReversed(false);

        return chart;
    }

    public static ChartData getDefaultLineChartData() {
        ChartData data = new ChartData();

        ChartConfItem confItem = new ChartConfItem();
        confItem.setType(ChartTypeEnum.LINE_CHART);
        confItem.setTitle("Yearly Average Temperature");
        confItem.setLegend(LegendPositionEnum.RIGHT);
        data.setConf(confItem);
        data.setCategories(getCategories());

        SerieData series = new SerieData();
        series.setName("Serie-1");
        series.setValues(new Number[]{
                -0.2, 0.8, 5.7, 11.3, 17.0, 24.8, 14.1, 2.5
        });
        data.getSeries().add(series);

        series = new SerieData();
        series.setName("Serie-2");
        series.setValues(new Number[]{
                -0.9, 0.6, 8.4, 18.6, 14.3, 9.0, 3.9, 1.0
        });
        data.getSeries().add(series);

        series = new SerieData();
        series.setName("Serie-3");
        series.setValues(new Number[]{
                3.9, 8.5, 15.2, 17.0, 14.2, 10.3, 6.6, 4.8
        });
        data.getSeries().add(series);

        return data;
    }

    public static void createCustomBenchmarkSeria(String funcVal, ChartData chartData) {
        if (funcVal == null || funcVal.isEmpty()) {
            return;
        }
        String aggrigateFuncCode = funcVal;
        SerieData serieData = new SerieData();
        serieData.setName("Benchmark for y-axis");
        HashMap<Integer, ArrayList<Number>> seriesByCategory = new HashMap<>();
        for (int i = 0; i < chartData.getCategories().size(); i++) {
            ArrayList<Number> numberList = seriesByCategory.get(i);
            if (numberList == null) {
                numberList = new ArrayList<>();
            }
            for (SerieData item : chartData.getSeries()) {
                numberList.add(item.getValues()[i]);
            }
            seriesByCategory.put(i, numberList);
        }
        serieData.setValues(createSerieBenchmarkAggFunc(aggrigateFuncCode, seriesByCategory));
        serieData.setSerieType(ChartTypeEnum.LINE_CHART);
        chartData.getSeries().add(serieData);
    }

    private static Number[] createSerieBenchmarkAggFunc(String aggrigateFuncCode, HashMap<Integer, ArrayList<Number>> seriesByCategory) {

        ArrayList<Number> values = new ArrayList<>();
        for (Integer index : seriesByCategory.keySet()) {
            values.add(calculateTotalByAgrigateFunction(aggrigateFuncCode, seriesByCategory.get(index).toArray(new Number[]{})));
        }
        return values.toArray(new Number[]{});
    }

    private static BigDecimal calculateTotalByAgrigateFunction(String functionCode, Number[] numbers) {
        int scale = 5;
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal sum = BigDecimal.ZERO.setScale(scale);
        BigDecimal min = BigDecimal.valueOf(Long.MAX_VALUE).setScale(scale);
        BigDecimal max = BigDecimal.valueOf(Long.MIN_VALUE).setScale(scale);
        int count = 0;
        for (Number num : numbers) {
            if (num != null) {
                BigDecimal numVal = BigDecimal.valueOf(num.doubleValue()).setScale(scale);
                sum = sum.add(numVal).setScale(scale);
                min = (numVal).min(min);
                max = (numVal).max(max);
            }
            count++;

        }
        switch (functionCode.toLowerCase()) {
            case "sum":
                result = sum;
                break;
            case "count":
                result = BigDecimal.valueOf(count);
                break;
            case "avg":
                result = sum.divide(BigDecimal.valueOf(count), scale);
                break;
            case "max":
                result = max;
                break;
            case "min":
                result = min;
                break;
        }
        return result.setScale(5);
    }

    public static ChartData getDefaultAreaChartData() {
        ChartData data = new ChartData();

        ChartConfItem confItem = new ChartConfItem();
        confItem.setType(ChartTypeEnum.AREA_CHART);
        confItem.setTitle("Yearly Average Temperature");
        confItem.setLegend(LegendPositionEnum.RIGHT);
        data.setConf(confItem);
        data.setCategories(getCategories());

        SerieData series = new SerieData();
        series.setName("Serie-1");
        series.setValues(new Number[]{
                5, 3, 4, 7, 4, 2, 5, 8
        });
        data.getSeries().add(series);

        series = new SerieData();
        series.setName("Serie-2");
        series.setValues(new Number[]{
                3, 4, 4, -2, 3, 5, 7, 1
        });
        data.getSeries().add(series);

        return data;
    }

    /**
     * this default data for the Horizontal/Vertical bar charts
     *
     * @param type in {Horizontal/Vertical bar}
     * @return
     */
    public static ChartData getDefaultBarChartData(ChartTypeEnum type, Integer stacked) {
        ChartData data = new ChartData();
        ChartConfItem confItem = new ChartConfItem();
        confItem.setType(type);
        confItem.setTitle("Yearly Average Temperature");
        confItem.setLegend(LegendPositionEnum.BOTTOM);
        confItem.setShowLabel(true);
        data.setConf(confItem);

        LinkedList<String> categories = new LinkedList<>();
        categories.add("Category-1");
        categories.add("Category-2");
        categories.add("Category-3");
        categories.add("Category-4");
        data.setCategories(categories);

        SerieData series = new SerieData();
        series.setName("Serie-1");
        if (stacked != null && stacked.equals(BY_PERCENT.getId())) {
            series.setValues(new Number[]{
                    23.52, 11.15, 42.45, 19.56
            });
        } else {
            series.setValues(new Number[]{
                    107, 31, 635, 203
            });
        }
        data.getSeries().add(series);

        series = new SerieData();
        series.setName("Serie-2");
        if (stacked != null && stacked.equals(BY_PERCENT.getId())) {
            series.setValues(new Number[]{
                    29.23, 56.12, 36.56, 39.30
            });
        } else {
            series.setValues(new Number[]{
                    133, 156, 547, 408
            });
        }
        data.getSeries().add(series);

        series = new SerieData();
        series.setName("Serie-3");
        if (stacked != null && stacked.equals(BY_PERCENT.getId())) {
            series.setValues(new Number[]{
                    47.25, 32.73, 20.99, 41.14
            });
        } else {
            series.setValues(new Number[]{
                    215, 91, 314, 427
            });
        }
        data.getSeries().add(series);

        return data;
    }

    /**
     * this default data for the gauge bar chart
     */
    public static ChartData getDefaultGaugeChartData() {
        ChartData data = new ChartData();
        ChartConfItem confItem = new ChartConfItem();
        confItem.setType(ChartTypeEnum.GAUGE_CHART);
        confItem.setTitle("Sample Gauge Chart");
        data.setConf(confItem);

        data.setGaugeMinValue(0);
        data.setGaugeMaxValue(200);
        data.setGaugeActValue(8);
        return data;
    }

    /**
     * this default data for the Pie, Donut, Funnel charts
     *
     * @param type in {Pie, Donut, Funnel}
     * @return
     */
    public static ChartData getDefaultDataForOtherCharts(ChartTypeEnum type) {
        ChartData data = new ChartData();
        ChartConfItem confItem = new ChartConfItem();
        confItem.setType(type);
        confItem.setTitle("Yearly Average Temperature");
        confItem.setLegend(LegendPositionEnum.BOTTOM);
        confItem.setShowLabel(true);
        data.setConf(confItem);

        LinkedList<String> categories = new LinkedList<>();
        categories.add("Category-1");
        categories.add("Category-2");
        categories.add("Category-3");
        categories.add("Category-4");
        categories.add("Category-5");
        data.setCategories(categories);

        SerieData series = new SerieData();
        series.setName("Serie-1");
        series.setValues(new Number[]{
                24, 7, 43, 10, 16
        });
        data.getSeries().add(series);
        data.setTotal(100);

        return data;
    }

    private static LinkedList<String> getCategories() {
        Integer currentYear = DateUtil.getYear(new Date());

        LinkedList<String> categories = new LinkedList<>();
        for (int i = 0; i < 8; i++) {
            categories.add(String.valueOf(currentYear + i - 7));
        }

        return categories;
    }
}
