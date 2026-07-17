package com.finnetlimited.reportservice.core.server.utils;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.GaugeChartConfig;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetItem;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static com.edatasite.workforce.gwt.chart.client.enums.StackedEnum.BY_PERCENT;
import static com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.ReportingWidget.POSITIVE;

/**
 * User: ${Dilsh0d}
 * Date: 03-Apr-2010
 * Time: 15:06:15
 */
public class JdbcUtil {

    private static final Logger log = LoggerFactory.getLogger(JdbcUtil.class);

    private static final String GREATER_THEN = "GREATER_THEN";
    private static final String EQUAL_TO = "EQUAL_TO";
    private static final String BETWEEN = "BETWEEN";
    private static final String LESS_THAN = "LESS_THAN";
    ///  color type
    private static final String AUTOMATIC = "AUTOMATIC";
    private static final String CUSTOM_COLOR = "CUSTOM_COLOR";

    public static ChartData getChartData(ResultSet rs, ReportRpc report, ResultSet drillRS) {
        ChartData chartData = new ChartData();
        chartData.setConf(report.getChartConf());
        chartData.setCategories(new LinkedList<>());
        chartData.setDrillSeries(new HashMap<>());

        HashMap<Integer, LinkedList<Number>> seriesDataMap = new HashMap<>();
        for (int i = 0; i < chartData.getConf().getSeries().size(); i++) {
            seriesDataMap.put(i, new LinkedList<>());
        }

        try {
            if (drillRS != null) {
                while (drillRS.next()) {
                    String drillCategory = drillRS.getString(1);
                    String drillSeriName = ServerUtils.isNullOrEmpty(drillRS.getString(2)) ? "n/a" : drillRS.getString(2);
                    String drillSerivalue = drillRS.getString(3);
                    if (chartData.getDrillSeries().get(drillCategory) != null) {
                        chartData.getDrillSeries().get(drillCategory).put(drillSeriName, getValue(drillSerivalue));
                    } else {
                        HashMap<String, Double> serieMap = new HashMap<>();
                        serieMap.put(drillSeriName, getValue(drillSerivalue));
                        chartData.getDrillSeries().put(drillCategory, serieMap);
                    }
                }
            }
            boolean nodata = true;
            Number total = 0;
            while (rs.next()) {

                String category = rs.getString(1);

                if (category == null) {
                    continue;
                }

                chartData.getCategories().add(category);

                for (int i = 0; i < report.getChartConf().getSeries().size(); i++) {

                    SerieConfItem serieConf = report.getChartConf().getSeries().get(i);
                    Double aDouble = getValue(rs.getString(i + 2));

                    if ("time".equals(serieConf.getSerieColumn().getColumnFormat())
                            && Arrays.asList("sum", "avg", "max", "min").contains(serieConf.getAggrType().getFunction())) {

                        aDouble = aDouble / 60;
                        final DecimalFormat numberFormat = new DecimalFormat("###0.00");
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                        symbols.setDecimalSeparator('.');
                        numberFormat.setDecimalFormatSymbols(symbols);
                        aDouble = Double.valueOf(numberFormat.format(aDouble));
                    }
                    seriesDataMap.get(i).add(aDouble);
                    total = total.doubleValue() + aDouble;
                }
                nodata = false;
            }
            chartData.setTotal(total);

            if (!nodata) {
                for (Integer key : seriesDataMap.keySet()) {
                    SerieData serieData = new SerieData();
                    serieData.setName(chartData.getConf().getSeries().get(key).getSerieName());
                    serieData.setValues(seriesDataMap.get(key).toArray(new Number[]{}));
                    if (chartData.getConf().getGradientColor()) {
                        LinkedList<ColumnColor> colorLinkedList = report.getChartConf().getSeries().get(key).getColorList();
                        if (colorLinkedList.size() > 0) {
                            serieData.setColor(colorLinkedList.get(0).getColor());
                        }
                    }
                    serieData.setPointColor(getSeriePointColor(seriesDataMap.get(key), report.getChartConf().getSeries().get(key)));
//                    serieData.setColor(getColorCode());
                    chartData.getSeries().add(serieData);
                }
                if (chartData.getConf() != null && chartData.getConf().getStacked() != null && chartData.getConf().getStacked().equals(BY_PERCENT)) {
                    SerieData data = chartData.getSeries().get(0);

                    for (int j = 0; j < chartData.getSeries().size(); j++) {
                        chartData.getSeries().get(j).setValuesForColor(chartData.getSeries().get(j).getValues());
                        chartData.getSeries().get(j).setPointColor(getSeriePointColor(new LinkedList<>(Arrays.asList(chartData.getSeries().get(j).getValues())), report.getChartConf().getSeries().get(j)));
                    }
                    if (chartData.getSeries().size() > 1) {
                        for (int i = 0; i < data.getValues().length; i++) {
                            Number totalAmount = 0;
                            totalAmount = totalAmount.doubleValue() + data.getValues()[i].doubleValue();
                            for (int j = 1; j < chartData.getSeries().size(); j++) {
                                totalAmount = totalAmount.doubleValue() + chartData.getSeries().get(j).getValues()[i].doubleValue();
                            }
                            data.getValues()[i] = data.getValues()[i].doubleValue() / totalAmount.doubleValue() * 100;
                            for (int j = 1; j < chartData.getSeries().size(); j++) {
                                chartData.getSeries().get(j).getValues()[i] = chartData.getSeries().get(j).getValues()[i].doubleValue() / totalAmount.doubleValue() * 100;
                            }
                        }
                    }
                }
                if (report.getChartConf().getBenchmarkValue() != null && report.getChartConf().getBenchmarkValue().compareTo(BigDecimal.ZERO) != 0) {
                    Number[] values = new Number[chartData.getCategories().size()];
                    int i = 0;
                    for (String ignored : chartData.getCategories()) {
                        values[i] = report.getChartConf().getBenchmarkValue();
                        i++;
                    }
                    SerieData serieData = new SerieData();
                    serieData.setName("Benchmark for y-axis");
                    serieData.setValues(values);
                    serieData.setSerieType(ChartTypeEnum.LINE_CHART);

                    chartData.getSeries().add(serieData);
                } else if (!ServerUtils.isNullOrEmpty(report.getChartConf().getBenchmarkAggFuncVal())) {
                    createCustomBenchmarkSeria(report, chartData);
                }
                ChartTypeEnum chartType = report.getChartConf().getType();
                if ((ChartTypeEnum.PIE_CHART.equals(chartType) || ChartTypeEnum.DONUT_CHART.equals(chartType)
                        || ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART.equals(chartType) || ChartTypeEnum.FUNNEL_CHART.equals(chartType)
                        || ChartTypeEnum.GAUGE_CHART.equals(chartType) || ChartTypeEnum.PYRAMID_CHART.equals(chartType))) {
                    if (!ServerUtils.isNullOrEmpty(report.getChartConf().getAgrigateItemCode())) {
                        chartData.setTotal(calculateTotalByAgrigateFunction(report.getChartConf().getAgrigateItemCode(), chartData.getSeries().get(0).getValues()));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chartData;
    }

    private static void createCustomBenchmarkSeria(ReportRpc report, ChartData chartData) {
        String aggrigateFuncCode = report.getChartConf().getBenchmarkAggFuncVal();
        SerieData serieData = new SerieData();
        serieData.setName("Benchmark for y-axis");
        Integer scale = report.getChartConf().getScale() != null && !report.getChartConf().getScale().isEmpty() ? Integer.valueOf(report.getChartConf().getScale()) : 0;
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

    private static BigDecimal calculateTotalByAgrigateFunction(String functionCode, Number[] numbers) {
        int scale = 5;
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal sum = BigDecimal.ZERO.setScale(scale);
        BigDecimal min = BigDecimal.valueOf(Long.MAX_VALUE).setScale(scale);
        BigDecimal max = BigDecimal.valueOf(Long.MIN_VALUE).setScale(scale);
        int count = 0;
        for (Number num : numbers) {
            if (num != null) {
                BigDecimal numVal = BigDecimal.valueOf(num.doubleValue()).setScale(scale, RoundingMode.HALF_UP);
                sum = sum.add(numVal).setScale(scale);
                min = (numVal).min(min);
                max = (numVal).max(max);
            }
            count++;

        }
        switch (functionCode.toLowerCase()) {
            case "sum" -> result = sum;
            case "count" -> result = BigDecimal.valueOf(count);
            case "avg" -> result = sum.divide(BigDecimal.valueOf(count), scale, RoundingMode.HALF_UP);
            case "max" -> result = max;
            case "min" -> result = min;
        }
        return result.setScale(5);
    }

    private static HashMap<BigDecimal, String> getSeriePointColor(LinkedList<Number> numbers, SerieConfItem serieConfItem) {
        HashMap<BigDecimal, String> pointColorMap = new HashMap<>();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#0.00", otherSymbols);
        if (numbers != null && serieConfItem != null && serieConfItem.getColorList() != null) {
            for (Number num : numbers) {
                for (ColumnColor color : serieConfItem.getColorList()) {
                    String val = getColorByPoint(color, num);
                    num = BigDecimal.valueOf(num.doubleValue()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal n = new BigDecimal(decimalFormat.format(num.doubleValue()));
                    if (val == null && pointColorMap.get(n) != null) {
                        continue;
                    }
                    pointColorMap.put(n, val);
                }
            }
        }
        return pointColorMap;
    }

    public static String getColorByPoint(ColumnColor color, Number number) {
        String colorCode = null;
        if (color == null || number == null) {
            return colorCode;
        }
        if (CUSTOM_COLOR.equalsIgnoreCase(color.getType())) {
            try {
                return switch (color.getTarget()) {
                    case GREATER_THEN ->
                            number.doubleValue() >= Double.parseDouble(color.getCondition()) ? color.getColor() : null;
                    case LESS_THAN ->
                            number.doubleValue() <= Double.parseDouble(color.getCondition()) ? color.getColor() : null;
                    case BETWEEN ->
                            number.doubleValue() >= Double.parseDouble(color.getCondition()) && number.doubleValue() <= Double.parseDouble(color.getBetweenSecondValue()) ? color.getColor() : null;
                    case EQUAL_TO ->
                            number.doubleValue() == Double.parseDouble(color.getCondition()) ? color.getColor() : null;
                    default -> null;
                };
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static BigDecimal getKpiWidgetData(ResultSet rs) {
        try {
            while (rs.next()) {
                String value = rs.getString(1);
                if (value == null) {
                    return BigDecimal.ZERO;
                }
                return new BigDecimal(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public static ChartData getChartDataForGauge(ResultSet rs, ReportRpc report) {
        ChartData chartData = new ChartData();
        chartData.setConf(report.getChartConf());
        GaugeChartConfig gaugeConfig = report.getChartConf().getGaugeConfig();

        try {
            rs.next();
            String actValue = rs.getString(1);

            if (actValue == null) {
                return null;
            }
            chartData.setGaugeActValue(getValue(actValue));

            if (gaugeConfig.getGaugeMinColumn() != null && gaugeConfig.getGaugeMaxColumn() != null) {
                chartData.setGaugeMinValue(getValue(rs.getString(2)));
                chartData.setGaugeMaxValue(getValue(rs.getString(3)));
            } else if (gaugeConfig.getGaugeMinColumn() != null) {
                chartData.setGaugeMinValue(getValue(rs.getString(2)));
            } else if (gaugeConfig.getGaugeMaxColumn() != null) {
                chartData.setGaugeMaxValue(getValue(rs.getString(2)));
            }

            if (chartData.getGaugeMinValue() == null) {
                chartData.setGaugeMinValue(gaugeConfig.getGaugeMinValue());
            }
            if (chartData.getGaugeMaxValue() == null) {
                chartData.setGaugeMaxValue(gaugeConfig.getGaugeMaxValue());
            }
            if (gaugeConfig.getGaugeSerie() != null) {
                String colorCode = null;
                LinkedList<ColumnColor> colorLinkedList = gaugeConfig.getGaugeSerie().getColorList();
                if (colorLinkedList != null) {
                    for (ColumnColor color : colorLinkedList) {
                        if (color.getGradient()) {
                            colorCode = color.getColor();
                        } else {
                            String defaultColor = getColorByPoint(color, getValue(actValue));
                            if (defaultColor != null) {
                                colorCode = defaultColor;
                            }
                        }
                    }
                }
                if (colorCode != null) {
                    gaugeConfig.setChartColor(colorCode);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chartData;
    }

    public static LinkedList<SelectItem> getFilterSelectList(ResultSet rs) {
        LinkedList<SelectItem> list = new LinkedList<>();
        try {
            int i = 1;
            while (rs.next()) {
                Integer id = i;
                String value = rs.getString(1);
                if (value != null) {
                    value = Jsoup.parse(value).text();
                    if (value.contains("#####")) {
                        String[] temp = value.split("#####");
                        if (temp.length > 1 || !(temp[0] == null || temp[0].trim().isEmpty())) {
                            id = Integer.valueOf(temp[0].trim());
                        }
                        if (temp.length > 1 && !(temp[1] == null || temp[1].trim().isEmpty())) {
                            value = temp[1];
                        }
                    }
                    list.add(new SelectItem(id, value));
                    i++;
                }
            }
        } catch (SQLException e) {
            log.error("getFilterSelectList problem ", e);
            e.printStackTrace();
        }
        return list;
    }

    private static Double getValue(String text) {

        if (StringUtils.isEmpty(text)) {
            return 0.0;
        }
        try {
            return Double.parseDouble(text.replace(" ", "").replace(",", ""));
        } catch (NumberFormatException e) {
            log.error("in getValue method NumberFormatException ", e);
            e.printStackTrace();
        }
        return 0.0;
    }

    public static ChartData getSplitedChartData(ResultSet rs, ReportRpc report) {
        ChartData chartData = new ChartData();
        chartData.setConf(report.getChartConf());
//        if (report.getChartConf() != null && report.getChartConf().getLegend() == null) {
//            chartData.getConf().setLegend(LegendPositionEnum.BOTTOM);
//        }
        chartData.setCategories(new LinkedList<>());

        LinkedList<String> categoriesList = new LinkedList<>();
        LinkedHashMap<Integer, LinkedHashMap<String, LinkedHashMap<String, Number>>> allSeriesSplitMap = new LinkedHashMap<>();
        /// this map for collect all seria color lists by index
        LinkedHashMap<Integer, LinkedList<ColumnColor>> seriaColorListByIndex = new LinkedHashMap<>();
        try {
            boolean nodata = true;
            while (rs.next()) {

                String category = rs.getString(1);

                if (category == null) {
                    continue;
                }

                if (!categoriesList.contains(category)) {
                    categoriesList.add(category);
                    chartData.getCategories().add(category);
                }

                String serie = rs.getString(2);
                if (serie == null) serie = "N/A";
                if (serie.length() > 20) {
                    serie = serie.substring(0, 20) + "...";
                }

                for (int i = 0; i < report.getChartConf().getSeries().size(); i++) {

                    SerieConfItem serieConf = report.getChartConf().getSeries().get(i);
                    Double aDouble = getValue(rs.getString(i + 3));

                    if ("time".equals(serieConf.getSerieColumn().getColumnFormat()) && Arrays.asList("sum", "avg", "max", "min").contains(serieConf.getAggrType().getFunction())) {

                        aDouble = aDouble / 60;
                        final DecimalFormat numberFormat = new DecimalFormat("###0.00");
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                        symbols.setDecimalSeparator('.');
                        numberFormat.setDecimalFormatSymbols(symbols);
                        aDouble = Double.valueOf(numberFormat.format(aDouble));
                    }
                    if (allSeriesSplitMap.get(i) != null) {
                        if (allSeriesSplitMap.get(i).get(serie) != null) {
                            allSeriesSplitMap.get(i).get(serie).put(category, aDouble);
                        } else {
                            LinkedHashMap<String, Number> valueList = new LinkedHashMap<>();
                            valueList.put(category, aDouble);
                            allSeriesSplitMap.get(i).put(serie, valueList);
                        }
                    } else {
                        LinkedHashMap<String, Number> valueList = new LinkedHashMap<>();
                        valueList.put(category, aDouble);
                        LinkedHashMap<String, LinkedHashMap<String, Number>> seriesMap = new LinkedHashMap<>();
                        seriesMap.put(serie, valueList);
                        allSeriesSplitMap.put(i, seriesMap);

                    }
                    seriaColorListByIndex.put(i, serieConf.getColorList() != null ? serieConf.getColorList() : new LinkedList<>());
                }
                nodata = false;
            }
            Number total = 0;
            LinkedHashMap<Integer, LinkedHashMap<String, Number[]>> seriesDataMap = new LinkedHashMap<>();
            for (Integer seriesIndex : allSeriesSplitMap.keySet()) {
                LinkedHashMap<String, Number[]> seriaMap = new LinkedHashMap<>();
                for (String seriesKey : allSeriesSplitMap.get(seriesIndex).keySet()) {
                    Number[] serieValues = new Number[categoriesList.size()];
                    int i = 0;
                    for (String category : categoriesList) {
                        Number value = allSeriesSplitMap.get(seriesIndex).get(seriesKey).get(category);
                        serieValues[i] = value != null ? value : 0d;
                        total = total.floatValue() + serieValues[i].floatValue();
                        i++;
                    }
                    seriaMap.put(seriesKey, serieValues);
                }
                seriesDataMap.put(seriesIndex, seriaMap);
            }
            chartData.setTotal(total);
            if (!nodata) {
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                for (Integer seriaIndex : seriesDataMap.keySet()) {
                    for (String key : seriesDataMap.get(seriaIndex).keySet()) {
                        SerieData serieData = new SerieData();
                        serieData.setName(key);
                        serieData.setValues(seriesDataMap.get(seriaIndex).get(key));
                        serieData.setValuesForColor(serieData.getValues());
                        serieData.setStack("paid");
                        LinkedList<ColumnColor> colorList = seriaColorListByIndex.get(seriaIndex);
                        HashMap<BigDecimal, String> pointColorMap = new HashMap<>();
                        if (colorList != null) {
                            int counter = 0;
                            for (Number num : serieData.getValues()) {
                                for (ColumnColor color : colorList) {
                                    if (num != null) {
                                        String val = getColorByPoint(color, num);
                                        BigDecimal n = new BigDecimal(decimalFormat.format(num).replace(",", "."));
                                        if (val == null && pointColorMap.get(n) != null) {
                                            continue;
                                        }
                                        pointColorMap.put(n, val);
                                    } else {
                                        serieData.getValues()[counter] = BigDecimal.ZERO;
                                    }
                                }
                                counter++;
                            }
                        }
                        if (chartData.getConf().getGradientColor()) {
                            if (colorList.size() > 0) {
                                serieData.setColor(colorList.get(0).getColor());
                            }
                        }
                        serieData.setPointColor(pointColorMap);
                        chartData.getSeries().add(serieData);
                    }
                }

                if (report.getChartConf().getBenchmarkValue() != null && report.getChartConf().getBenchmarkValue().compareTo(BigDecimal.ZERO) != 0) {
                    Number[] values = new Number[chartData.getCategories().size()];
                    for (int i = 0; i < chartData.getCategories().size(); i++) {
                        values[i] = report.getChartConf().getBenchmarkValue();
                    }
                    SerieData serieData = new SerieData();
                    serieData.setName("Benchmark for y-axis");
                    serieData.setValues(values);
                    serieData.setSerieType(ChartTypeEnum.LINE_CHART);

                    chartData.getSeries().add(serieData);
                } else if (!ServerUtils.isNullOrEmpty(report.getChartConf().getBenchmarkAggFuncVal())) {
                    createCustomBenchmarkSeria(report, chartData);
                }
                ChartTypeEnum chartType = report.getChartConf().getType();
                if ((ChartTypeEnum.PIE_CHART.equals(chartType) || ChartTypeEnum.DONUT_CHART.equals(chartType)
                        || ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART.equals(chartType) || ChartTypeEnum.FUNNEL_CHART.equals(chartType)
                        || ChartTypeEnum.GAUGE_CHART.equals(chartType))) {
                    if (!ServerUtils.isNullOrEmpty(report.getChartConf().getAgrigateItemCode())) {
                        chartData.setTotal(calculateTotalByAgrigateFunction(report.getChartConf().getAgrigateItemCode(), chartData.getSeries().get(0).getValues()));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chartData;
    }

    private static Number[] createSerieBenchmarkAggFunc(String aggrigateFuncCode, HashMap<Integer, ArrayList<Number>> seriesByCategory) {

        ArrayList<Number> values = new ArrayList<>();
        for (Integer index : seriesByCategory.keySet()) {
            values.add(calculateTotalByAgrigateFunction(aggrigateFuncCode, seriesByCategory.get(index).toArray(new Number[]{})));
        }
        return values.toArray(new Number[]{});
    }

    public static ArrayList<SelectItem> getKpiRankingData(ResultSet rs, KpiWidgetItem widgetItem) {

        ArrayList<SelectItem> rankingTableItems = new ArrayList<>();
        String serieName = widgetItem.getKpiWidgetMetric() != null ? widgetItem.getKpiWidgetMetric().getSerieName() : "";
//        if (serieName.length() > 25) {
//            serieName = serieName.substring(0, 25) + "...";
//        }
        SelectItem titleItem = new SelectItem(0, widgetItem.getGroupingColumn().getColumnTitle(), serieName);
        rankingTableItems.add(titleItem);
        try {
            int i = 1;
            Double otherTotalAmount = Double.valueOf(0);
            while (rs.next()) {

                SelectItem tableItem = new SelectItem();

                String category = rs.getString(1);

                if (category == null) {
                    continue;
                }
//                if (category.length() > 25) {
//                    category = category.substring(0, 25) + "...";
//                }

                tableItem.setName(category);

                if (widgetItem.getKpiWidgetMetric() != null && widgetItem.getKpiWidgetMetric().getSerieColumn().getColumn() != null) {
                    Double aDouble = getValue(rs.getString(2));
                    String color = null;
                    if (widgetItem.getKpiWidgetMetric() != null && widgetItem.getKpiWidgetMetric().getColorList() != null) {
                        for (ColumnColor item : widgetItem.getKpiWidgetMetric().getColorList()) {
                            color = getColorByPoint(item, aDouble);
                            if (color != null) {
                                widgetItem.setKpiWidgetTitleColor(color);
                            }
                        }
                    }

                    if ("time".equals(widgetItem.getKpiWidgetMetric().getSerieColumn().getColumnFormat())
                            && Arrays.asList("sum", "avg", "max", "min").contains(widgetItem.getKpiWidgetMetric().getAggrType().getFunction())) {

                        aDouble = aDouble / 60;
                        final DecimalFormat numberFormat = new DecimalFormat("###0.00");
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                        symbols.setDecimalSeparator('.');
                        numberFormat.setDecimalFormatSymbols(symbols);
                        aDouble = Double.valueOf(numberFormat.format(aDouble));
                    }
                    if (widgetItem.isOtherItems() && i >= widgetItem.getPageSizeWithCustom()) {
                        otherTotalAmount += aDouble;
                    } else {
                        tableItem.setTotalAmount(aDouble);
                        rankingTableItems.add(tableItem);
                    }
                } else {
                    rankingTableItems.add(tableItem);
                }
                i++;

            }

            if (widgetItem.isOtherItems() && i > widgetItem.getPageSizeWithCustom()) {
                SelectItem tableItem = new SelectItem();
                tableItem.setName("Others");
                tableItem.setTotalAmount(otherTotalAmount);
                rankingTableItems.add(tableItem);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rankingTableItems;
    }

    public static void getActualPercentValue(KpiWidgetData kpiWidgetData) {
        BigDecimal percontValue = getPercentValue(kpiWidgetData);
        boolean isIncrease = false;
        if (percontValue.compareTo(BigDecimal.ZERO) < 0) {
            percontValue = percontValue.abs();
            percontValue = percentValByType(kpiWidgetData, percontValue, !isIncrease);
        } else {
            isIncrease = true;
            percontValue = percentValByType(kpiWidgetData, percontValue, !isIncrease);
        }
        kpiWidgetData.setRoseUp(isIncrease);
        kpiWidgetData.setPercentVal(checkIfIncrease(kpiWidgetData, percontValue));
    }

    public static BigDecimal getPercentValue(KpiWidgetData kpiWidgetData) {
        BigDecimal percontValue;
        if (kpiWidgetData.getCurrent() == null) {
            kpiWidgetData.setCurrent(BigDecimal.ZERO);
        }
        if (kpiWidgetData.getComparision() == null) {
            kpiWidgetData.setComparision(BigDecimal.ZERO);
        }
        if (kpiWidgetData.getComparision().compareTo(BigDecimal.ZERO) == 0 && kpiWidgetData.getCurrent().compareTo(BigDecimal.ZERO) == 0) {
            percontValue = BigDecimal.ZERO;
        } else if (kpiWidgetData.getComparision().compareTo(BigDecimal.ZERO) == 0 && kpiWidgetData.getCurrent().compareTo(BigDecimal.ZERO) != 0) {
            percontValue = kpiWidgetData.getCurrent().compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.valueOf(-1 * 100) : BigDecimal.valueOf(100);
        } else {
            percontValue = (kpiWidgetData.getCurrent().subtract(kpiWidgetData.getComparision())).multiply(BigDecimal.valueOf(100)).divide(kpiWidgetData.getComparision(), 2, RoundingMode.HALF_UP);
        }
        return percontValue;
    }

    private static BigDecimal checkIfIncrease(KpiWidgetData kpiWidgetData, BigDecimal percentValue) {
        if (kpiWidgetData.getComparision() != null &&
                kpiWidgetData.getComparision().compareTo(kpiWidgetData.getCurrent()) < 0 &&
                kpiWidgetData.isRoseUp()) {
            if (POSITIVE.equals(kpiWidgetData.getNegAndPosType())) {
                percentValue = percentValue.add(BigDecimal.valueOf(100));
            } else {
                percentValue = BigDecimal.ZERO;
            }
        }
        return percentValue;
    }

    private static BigDecimal percentValByType(KpiWidgetData kpiWidgetData, BigDecimal percentVal, boolean isNotIncrease) {
        if (isNotIncrease) {
            if (POSITIVE.equals(kpiWidgetData.getNegAndPosType())) {
                return getSubstractedValFromHundred(percentVal);
            } else {
                return percentVal;
            }
        } else {
            if (POSITIVE.equals(kpiWidgetData.getNegAndPosType())) {
                return percentVal;
            } else {
                return getSubstractedValFromHundred(percentVal);
            }
        }
    }

    private static BigDecimal getSubstractedValFromHundred(BigDecimal val) {
        return BigDecimal.valueOf(100).subtract(val).abs();
    }

}
