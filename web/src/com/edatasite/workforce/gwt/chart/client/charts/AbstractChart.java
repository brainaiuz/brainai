package com.edatasite.workforce.gwt.chart.client.charts;


import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.enums.LegendPositionEnum;
import com.edatasite.workforce.gwt.chart.client.enums.StackedEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.ColorTransparentCode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Window;
import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.ChartSubtitle;
import org.moxieapps.gwt.highcharts.client.ChartTitle;
import org.moxieapps.gwt.highcharts.client.Credits;
import org.moxieapps.gwt.highcharts.client.Exporting;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.Style;
import org.moxieapps.gwt.highcharts.client.ToolTip;
import org.moxieapps.gwt.highcharts.client.labels.DataLabels;
import org.moxieapps.gwt.highcharts.client.labels.ProportionalDataLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.LinePlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.PlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.SeriesPlotOptions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public abstract class AbstractChart extends Chart {

    protected ChartData chartData;
    protected ChartConfItem chartConf;
    protected DashboardComponentItem dashboardItem;
    protected LinkedList<String> ct;
    protected String selectedOptionType;
    protected Double[] categoryTotals;
    protected String scale;
    private HashMap<String, Integer> countByPointColor;
    protected LinkedHashMap<String, Series> drillSeriesMap = new LinkedHashMap<>();

    public AbstractChart(ChartData chartData, DashboardComponentItem dashboardItem) {
        this(chartData);
        this.dashboardItem = dashboardItem;
    }

    public AbstractChart(ChartData chartData) {
        super();
        this.chartData = chartData;
        this.chartConf = chartData.getConf();
        this.scale = chartData.getConf().getScale() == null || chartData.getConf().getScale().equals("") ? "0" : chartData.getConf().getScale();
        this.ct = chartData.getCategories();
        this.selectedOptionType = chartConf.getChartViewOptionType();
        if (!ChartTypeEnum.GAUGE_CHART.equals(chartData.getConf().getType())) {
            this.categoryTotals = new Double[chartData.getCategories().size()];
        }
        //default color libraries
        setColors(ChartUtils.getDefaultColors());

        //configure chart type
        setType(getHightChartType(chartConf.getType()));
//        setOptions3D(new Options3D().setEnabled(true).setAlpha(45).setBeta(0));


        //configure chart title
        setChartTitle(new ChartTitle()
                .setText(null)
                .setStyle(new Style().setColor(ChartUtils.DEFAULT_TEXT_COLOR))
                .setX(-20)); //set title on center
        setStyle(new Style().setFontFamily("Roboto"));

        setChartSubtitle(new ChartSubtitle()
                .setText(chartConf.getSubtitle() != null ? chartConf.getSubtitle() : "")
                .setStyle(new Style().setColor(ChartUtils.DEFAULT_TEXT_COLOR))
                .setStyle(new Style().setOption("display", "none"))
                .setX(-20)); //set subtitle on center

        if ((ChartTypeEnum.FUNNEL_CHART.equals(chartConf.getType()) || ChartTypeEnum.DONUT_CHART.equals(chartConf.getType()))
                && (chartConf.getAgrigateItemCode() == null)) {
            setTextInCenter(chartData.getTotal());
        } else {
            if ((chartConf.getAgrigateItemCode() != null) && !"not_show".equals(chartConf.getAgrigateItemCode())) {
                setTextInCenter(chartData.getTotal());
            }
        }

        //do not show chart manufacture {in this case hide "Highchart.com"}
        setCredits(new Credits().setEnabled(false));

        setOption("objectId", chartConf.getObjectID() != null ? chartConf.getObjectID().toString() : "");

        getYAxis().setAxisTitle(null);
        setToolTip(new ToolTip().setValueDecimals(Integer.valueOf(scale)));

        setSeriesPlotOptions(new SeriesPlotOptions()
                .setCursor(PlotOptions.Cursor.POINTER)
                .setPointClickEventHandler(pointClickEvent -> {
                    int index = pointClickEvent.getPoint().getX().intValue();
                    String name = ct.get(index);
                    String addSalesInvoice = GWT.getHostPageBaseURL() + "Reporting.html#reporting|stepControl/" + dashboardItem.getReportId() + "/savedreport/" + Utils.encrypt(dashboardItem.getName()) + "/" + null + "/" + Utils.encrypt(name);
                    Window.open(addSalesInvoice, "_blank", "");
                    return false;
                })
        );

        setHeight100();

        //configure legend position
        configureLegend(false);

        //configure chart data label show/hide settings
        configureDataLabel();
        if (!(ChartTypeEnum.GAUGE_CHART.equals(chartConf.getType())
                || ChartTypeEnum.LINE_CHART.equals(chartConf.getType())
                || ChartTypeEnum.NONE.equals(chartConf.getType()) ||
                ChartTypeEnum.FUNNEL_CHART.equals(chartConf.getType()))) {
            setChart3DOption();
        }
        setExporting();
        onInitialize();
        setCategoryTotal();
    }

    protected void setExporting() {
        setExporting(
                new Exporting()
//                        .setUrl(Utils.getHostURL() + "jcaptcha")
                        .setType(Exporting.Type.PNG)
                        .setOption("printMaxWidth", 780)
                        .setScale(2)
        );
    }

    protected abstract void onInitialize();

    /**
     * configure default legend position of the chart
     * <p>
     * but you can also configure legend position for specific chart type by owerriding this method
     */
    public void configureLegend(boolean formatted) {

        if (chartConf.getLegend() != null) {

            Legend legend = null;

            if (LegendPositionEnum.RIGHT.equals(chartConf.getLegend())) {
                legend = new Legend()
                        .setLayout(Legend.Layout.VERTICAL)
                        .setAlign(Legend.Align.RIGHT)
                        .setVerticalAlign(Legend.VerticalAlign.MIDDLE)
                        .setBorderWidth(0)
                        .setItemMarginTop(5);
            } else if (LegendPositionEnum.TOP.equals(chartConf.getLegend())) {
                legend = new Legend()
                        .setLayout(Legend.Layout.HORIZONTAL)
                        .setAlign(Legend.Align.LEFT)
                        .setVerticalAlign(Legend.VerticalAlign.TOP)
                        .setBorderWidth(0);
            } else {
                legend = new Legend()
                        .setLayout(Legend.Layout.HORIZONTAL)
                        .setAlign(Legend.Align.CENTER)
                        .setVerticalAlign(Legend.VerticalAlign.BOTTOM)
                        .setBorderWidth(0)
                        .setItemMarginBottom(5);
            }
            legend.setItemStyle(new Style().setFontWeight("normal").setColor(ChartUtils.DEFAULT_TEXT_COLOR));

            if (formatted) {
                legend.setUseHTML(true)
                        .setLabelsFormatter(legendLabelsData -> ChartHtmlTemplates.getInstance().legend(legendLabelsData.getPointName()).asString());
            }
            setLegend(legend);
        } else {
            setLegend(new Legend().setEnabled(false));
        }
    }

    /**
     * configure default chart data label settings
     */
    protected abstract void configureDataLabel();


    protected void setChart3DOption() {
    }

    protected void setTextInCenter(Number value) {

    }

    /**
     * generate hight chart type by kpi chart type enums
     *
     * @param type
     * @return
     */
    protected Series.Type getHightChartType(ChartTypeEnum type) {
        Series.Type highChartType = Series.Type.LINE;

        if (ChartTypeEnum.VERTICAL_BAR_CHART.equals(type)) {
            highChartType = Series.Type.COLUMN;
        } else if (ChartTypeEnum.HORIZONTAL_BAR_CHART.equals(type)) {
            highChartType = Series.Type.BAR;
        } else if (ChartTypeEnum.LINE_CHART.equals(type)) {
            highChartType = Series.Type.LINE;
        } else if (ChartTypeEnum.AREA_CHART.equals(type)) {
            highChartType = Series.Type.AREA;
        } else if (ChartTypeEnum.PIE_CHART.equals(type) || ChartTypeEnum.DONUT_CHART.equals(type)) {
            highChartType = Series.Type.PIE;
        } else if (ChartTypeEnum.FUNNEL_CHART.equals(type)) {
            highChartType = Series.Type.FUNNEL;
        } else if (ChartTypeEnum.GAUGE_CHART.equals(type)) {
            highChartType = Series.Type.SOLID_GAUGE;
        } else if (ChartTypeEnum.PYRAMID_CHART.equals(type)) {
            highChartType = Series.Type.PYRAMID;
        }
        return highChartType;
    }

    protected void setCategoryTotal() {
        for (SerieData data : chartData.getSeries()) {
            for (int i = 0; i < data.getValues().length; i++) {
                categoryTotals[i] = Optional.ofNullable(categoryTotals[i]).orElse(0d) + (data.getValues()[i] != null ? data.getValues()[i].longValue() : 0L);
            }
        }
    }

    protected void setToolTip() {
        if (!ChartTypeEnum.DONUT_CHART.equals(chartConf.getType()) &&
                !ChartTypeEnum.PIE_CHART.equals(chartConf.getType()) &&
                !ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART.equals(chartConf.getType()) &&
                !ChartTypeEnum.FUNNEL_CHART.equals(chartConf.getType())) {
            LinkedList<String> categories = chartData.getCategories();
            setToolTip(new ToolTip().setFormatter(toolTipData -> {
                String percentage = Utils.formatWithScale(toolTipData.getYAsLong() / categoryTotals[categories.indexOf(toolTipData.getXAsString())] * 100, Integer.valueOf(scale));
                String serieName = toolTipData.getSeriesName();
                String value = Utils.formatWithScale(toolTipData.getYAsDouble(), Integer.valueOf(scale));
                String format = null;

                switch (chartConf.getStacked()) {
                    case BY_PERCENTANDVALUE:
                        format = "<span>" + serieName + "</span>: <b>" + value + "</b> (" + percentage + "%)<br/>";
                        break;
                    case BY_PERCENT:
                        format = "<span>" + serieName + "</span>: <b>" + percentage + "%</b> <br/>";
                        break;
                    case BY_VALUE:
                        format = "<span>" + serieName + "</span>: <b>" + value + "</b><br/>";
                }
                return format;
            }));
        } else {
            if (chartConf.getStacked() == null) {
                return;
            }
            switch (chartConf.getStacked()) {
                case BY_PERCENTANDVALUE:
                    setToolTip(new ToolTip()
                            .setHeaderFormat("").setPointFormat("<b>{point.name}</b>: {point.y:,." + scale + "f} ( {point.percentage:." + scale + "f}% )"));
                    break;
                case BY_PERCENT:
                    setToolTip(new ToolTip()
                            .setHeaderFormat("").setPointFormat("<b>{point.name}</b>: {point.percentage:." + scale + "f}%"));
                    break;
                case BY_VALUE:
                    setToolTip(new ToolTip()
                            .setHeaderFormat("").setPointFormat("<b>{point.name}</b>: {point.y:,." + scale + "f}"));
                    break;
            }
        }
    }

    protected void addSeries() {

        if (ChartConfItem.BY_SERIES.equals(chartConf.getSortBy())) {
            chartData.getSeries().sort((o1, o2) -> {
                if (ChartConfItem.ASC.equals(chartConf.getSortType())) {
                    return o1.getName().compareTo(o2.getName());
                } else {
                    return o2.getName().compareTo(o1.getName());
                }
            });
        }
        for (SerieData data : chartData.getSeries()) {
            //correcting scales
            setScaleToValues(data);

            Series series = createSeries()
                    .setName(data.getName())
                    .setPoints(getPointsByNumberValue(data));

            DataLabels labels = new DataLabels();
            labels.setEnabled(chartConf.getStacked() != null);
            labels.setFormatter(dataLabelsData -> {
                String percentage = Utils.formatWithScale(dataLabelsData.getYAsDouble() / categoryTotals[chartData.getCategories().indexOf(dataLabelsData.getXAsString())] * 100, Integer.valueOf(scale));
                String serieName = dataLabelsData.getSeriesName();
                String value = Utils.formatWithScale(dataLabelsData.getYAsDouble(), Integer.valueOf(scale));
                String format = null;
                if (StackedEnum.BY_PERCENTANDVALUE.equals(chartConf.getStacked())) {
                    format = chartConf.isShowSerie() ? "<span>" + serieName + "</span>: <b>" + value + "</b> (" + percentage + "%)<br/>" : "<b>" + value + "</b> (" + percentage + "%)<br/>";
                } else if (StackedEnum.BY_PERCENT.equals(chartConf.getStacked())) {
                    format = chartConf.isShowSerie() ? "<span>" + serieName + "</span>: <b>" + percentage + "%</b><br/>" : "<b>" + percentage + "%</b><br/>";
                } else if (StackedEnum.BY_VALUE.equals(chartConf.getStacked())) {
                    format = chartConf.isShowSerie() ? "<span>" + serieName + "</span>: <b>" + value + "</b><br/>" : "<b>" + value + "</b><br/>";
                }
                return format;
            });

            LinePlotOptions plotOptions = new LinePlotOptions();
            plotOptions.setDataLabels(labels);
            series.setPlotOptions(plotOptions);

//            if (!Utils.isNullOrEmpty(data.getColor())) {
//                series.setPlotOptions(new ColumnPlotOptions().setColor(data.getColor()));
//            }
            if (data.getSerieType() != null) {
                series.setType(getHightChartType(data.getSerieType()));
            }
            if (data.getStack() != null) {
                series.setStack(data.getStack());
            }
            addSeries(series);
        }
    }

    protected <T extends ProportionalDataLabels> void setLabels(T label) {
        label.setConnectorColor("#536677")
                .setEnabled(chartConf.getStacked() != null && chartConf.getStacked().getId() != -1)
                .setColor(ChartUtils.DEFAULT_TEXT_COLOR);

        if (StackedEnum.BY_PERCENTANDVALUE.equals(chartConf.getStacked())) {
            label.setFormat("{point.name} <br>  {point.y:,." + scale + "f} ( {point.percentage:." + scale + "f}% )");
        } else if (StackedEnum.BY_PERCENT.equals(chartConf.getStacked())) {
            label.setFormat("{point.name} <br> {point.percentage:." + scale + "f}%");
        } else if (StackedEnum.BY_VALUE.equals(chartConf.getStacked())) {
            label.setFormat("{point.name} <br>  {point.y:,." + scale + "f}");
        }
    }

    protected void setScaleToValues(SerieData data) {

        if (data == null || data.getValues() == null || data.getValues().length == 0) {
            return;
        }

        //set scaling
        NumberFormat formatter = Utils.getCalculationNumberFormat();
        for (int i = 0; i < data.getValues().length; i++) {

            if (data.getValues()[i] != null) {
                data.getValues()[i] = formatter.parse(formatter.format(data.getValues()[i]));
            }
        }
    }

    static class ChartHtmlTemplates {

        interface ChartHtmlTemplatesInterface extends SafeHtmlTemplates {
            @Template("<span style='max-width: 100px; display: inline-block; text-overflow: ellipsis; overflow: hidden; white-space: nowrap;' title='{0}'>{0}</span>")
            SafeHtml legend(String value);
        }

        private static ChartHtmlTemplatesInterface instance;

        public static ChartHtmlTemplatesInterface getInstance() {
            if (instance == null) {
                instance = GWT.create(ChartHtmlTemplatesInterface.class);
            }

            return instance;
        }
    }

    protected boolean is3DViewOption() {
        return "3D".equals(chartConf.getChartViewOption());
    }

    public Point[] getPointsByNumberValue(SerieData data) {
        Point[] point = new Point[data.getValues().length];
        HashMap<Number, String> gradientedColorMap = chartConf.getGradientColor() ? getGradientedColorMap(data) : new HashMap<>();
        int i = 0;
        countByPointColor = new HashMap<>();
        for (Number pData : data.getValues()) {
            Number pointValue = pData;
            if (pointValue != null && pointValue.doubleValue() == 0.0) {
                pointValue = null;
            }
            Number chartColorValue = pData;
            if (StackedEnum.BY_PERCENT.equals(chartConf.getStacked()) && data.getValuesForColor() != null) {
                chartColorValue = data.getValuesForColor()[i];
            }
            point[i] = new Point(pointValue);

            if (chartColorValue == null) {
                i++;
                continue;
            }
            String defaultColorValue = "";
            Number number = BigDecimal.valueOf(Utils.universalParse(NumberFormat.getFormat("#0.00"), chartColorValue.toString())).setScale(2, RoundingMode.HALF_UP);
            if (chartConf.getGradientColor() && gradientedColorMap.size() > 0 && chartConf.getSplitBy() == null) {
                if (gradientedColorMap.get(number) != null) {
                    point[i].setOption("color", gradientedColorMap.get(number));
                }
            } else if (data.getPointColor() != null && data.getPointColor().get(number) != null) {
                defaultColorValue = data.getPointColor().get(number);
                Integer count = countByPointColor.get(defaultColorValue) == null ? 0 : countByPointColor.get(defaultColorValue);
                count++;
                countByPointColor.put(defaultColorValue, count);
                point[i].setColor(defaultColorValue);
            }
            i++;
        }
        return point;
    }

    public Series[] generateDrillSeries() {
        drillSeriesMap.clear();
        for (String cat : chartData.getCategories()) {
            if (chartData.getDrillSeries().get(cat) != null) {
                HashMap<String, Double> stringDoubleHashMap = chartData.getDrillSeries().get(cat);
                Point[] points = new Point[stringDoubleHashMap.size()];
                int i = 0;
                for (String ser : stringDoubleHashMap.keySet()) {
                    points[i++] = new Point(ser, stringDoubleHashMap.get(ser));
                }
                drillSeriesMap.put(cat, createSeries().setPoints(points));
            }
        }
        return drillSeriesMap.values().toArray(new Series[]{});
    }


    public HashMap<Number, String> getGradientedColorMap(SerieData data) {
        if (chartConf.getGradientColor() && !Utils.isNullOrEmpty(data.getColor()) && data.getValues() != null) {
            List<Number> numbers = new ArrayList<>();
            Collections.addAll(numbers, data.getValues());
            numbers.sort(new Comparator<Number>() {
                @Override
                public int compare(Number o1, Number o2) {
                    BigDecimal o1Big = BigDecimal.valueOf(o1.doubleValue()).setScale(10, RoundingMode.HALF_UP);
                    BigDecimal o2Big = BigDecimal.valueOf(o2.doubleValue()).setScale(10, RoundingMode.HALF_UP);
                    return o1Big.compareTo(o2Big);
                }
            });
            HashMap<Number, String> map = new HashMap<>();
            int len = 0;
            int percentVal = 100 / numbers.size();
            for (Number num : numbers) {
                len += percentVal;

                Number number = BigDecimal.valueOf(Utils.universalParse(NumberFormat.getFormat("#0.00"), num.toString())).setScale(2, RoundingMode.HALF_UP);
                String color = ColorTransparentCode.convertColorHexCodeWithTransparent((len), data.getColor());
                map.put(number, color.toLowerCase());
            }
            return map;
        } else {
            return new HashMap<>();
        }

    }

    public String[] getGaugeChartStopColors() {
        String[] colors = null;
        SerieData data = chartData.getSeries().size() > 0 ? chartData.getSeries().get(0) : null;
        if (chartConf.getGradientColor() && !Utils.isNullOrEmpty(chartConf.getGaugeConfig().getChartColor())) {
            int len = 0;
            colors = new String[3];
            for (int i = 0; i < 3; i++) {
                len += 33;
                Number num = chartData.getGaugeMaxValue() != null ? chartData.getGaugeMaxValue() : BigDecimal.ZERO;
                Number number = BigDecimal.valueOf(Utils.universalParse(NumberFormat.getFormat("#0.00"), num.toString())).setScale(2, RoundingMode.HALF_UP);
                String color = ColorTransparentCode.convertColorHexCodeWithTransparent((len), chartConf.getGaugeConfig().getChartColor());
                colors[i] = color;
            }
        }
        return colors;
    }

}
