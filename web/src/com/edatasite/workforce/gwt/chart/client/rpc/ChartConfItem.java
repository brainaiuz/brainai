package com.edatasite.workforce.gwt.chart.client.rpc;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.enums.LegendPositionEnum;
import com.edatasite.workforce.gwt.chart.client.enums.StackedEnum;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

public class ChartConfItem implements IsSerializable {

    public static final String BY_CATEGORY = "BY_CATEGORY";
    public static final String BY_SERIES = "BY_SERIES";
    public static final String BY_CUSTOM = "BY_CUSTOM";

    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    private Integer objectID;
    private ChartTypeEnum type;
    private String title;
    private String subtitle;
    private Integer pageSize;

    private String sortBy; //{BY_CATEGORY, BY_SERIES}
    private String sortType; //{ASC, DESC}

    private LegendPositionEnum legend;
    private StackedEnum stacked;
    private boolean showLabel;
    private boolean showStacked;
    private boolean showSerie;
    private SerieColumn xAxis;
    private SerieColumn drillxAxis;
    private LinkedList<SerieConfItem> series;

    private ArrayList<ModuleEnum> modules;

    private GaugeChartConfig gaugeConfig;

    private String dateSortPeriodType; //{Daily, Weekly, Monthly, Quarterly, Yearly}
    private SerieColumn splitBy;
    private SerieColumn customSortColumn;
    private BigDecimal benchmarkValue;
    private String benchmarkAggFuncVal;
    private boolean hasPermission;

    private String chartViewOption;
    private String chartViewOptionType;
    private String scale;
    private Integer localizationId;
    private CustomFormLocalization localization;
    private String totalFieldName;
    private String agrigateItemCode;
    private Boolean isGradientColor;

    private int selectedchartTypeId;

    private boolean showPieChart;

    private String pieChartPosition;

    public boolean isShowPieChart() {
        return showPieChart;
    }

    public void setShowPieChart(boolean showPieChart) {
        this.showPieChart = showPieChart;
    }

    public String getPieChartPosition() {
        return pieChartPosition;
    }

    public void setPieChartPosition(String pieChartPosition) {
        this.pieChartPosition = pieChartPosition;
    }
    public SerieColumn getDrillxAxis() {
        return drillxAxis;
    }

    public void setDrillxAxis(SerieColumn drillxAxis) {
        this.drillxAxis = drillxAxis;
    }

    public int getSelectedchartTypeId() {
        return selectedchartTypeId;
    }

    public void setSelectedchartTypeId(int selectedchartTypeId) {
        this.selectedchartTypeId = selectedchartTypeId;
    }

    public boolean isHasPermission() {
        return hasPermission;
    }

    public void setHasPermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }

    public ChartConfItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public ChartTypeEnum getType() {
        return type;
    }

    public void setType(ChartTypeEnum type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public LegendPositionEnum getLegend() {
        return legend;
    }

    public void setLegend(LegendPositionEnum legend) {
        this.legend = legend;
    }

    public StackedEnum getStacked() {
        return stacked;
    }

    public void setStacked(StackedEnum stacked) {
        this.stacked = stacked;
    }

    public boolean isShowLabel() {
        return showLabel;
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    public SerieColumn getxAxis() {
        return xAxis;
    }

    public void setxAxis(SerieColumn xAxis) {
        this.xAxis = xAxis;
    }

    public LinkedList<SerieConfItem> getSeries() {
        return series;
    }

    public void setSeries(LinkedList<SerieConfItem> series) {
        this.series = series;
    }

    public ArrayList<ModuleEnum> getModules() {
        return modules;
    }

    public void setModules(ArrayList<ModuleEnum> modules) {
        this.modules = modules;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public GaugeChartConfig getGaugeConfig() {
        return gaugeConfig;
    }

    public void setGaugeConfig(GaugeChartConfig gaugeConfig) {
        this.gaugeConfig = gaugeConfig;
    }

    public String getDateSortPeriodType() {
        return dateSortPeriodType;
    }

    public void setDateSortPeriodType(String dateSortPeriodType) {
        this.dateSortPeriodType = dateSortPeriodType;
    }

    public void setSplitBy(SerieColumn splitBy) {
        this.splitBy = splitBy;
    }

    public SerieColumn getSplitBy() {
        return splitBy;
    }

    public SerieColumn getCustomSortColumn() {
        return customSortColumn;
    }

    public void setCustomSortColumn(SerieColumn customSortColumn) {
        this.customSortColumn = customSortColumn;
    }

    public void setBenchmarkValue(BigDecimal benchmarkValue) {
        this.benchmarkValue = benchmarkValue;
    }

    public BigDecimal getBenchmarkValue() {
        return benchmarkValue;
    }

    public String getChartViewOption() {
        return chartViewOption;
    }

    public void setChartViewOption(String chartViewOption) {
        this.chartViewOption = chartViewOption;
    }

    public String getChartViewOptionType() {
        return chartViewOptionType;
    }

    public void setChartViewOptionType(String chartViewOptionType) {
        this.chartViewOptionType = chartViewOptionType;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public Integer getLocalizationId() {
        return localizationId;
    }

    public void setLocalizationId(Integer localizationId) {
        this.localizationId = localizationId;
    }

    public CustomFormLocalization getLocalization() {
        return localization;
    }

    public void setLocalization(CustomFormLocalization localization) {
        this.localization = localization;
    }

    public boolean isShowStacked() {
        return showStacked;
    }

    public void setShowStacked(boolean showStacked) {
        this.showStacked = showStacked;
    }

    public String getTotalFieldName() {
        return totalFieldName;
    }

    public void setTotalFieldName(String totalFieldName) {
        this.totalFieldName = totalFieldName;
    }

    public String getAgrigateItemCode() {
        return agrigateItemCode;
    }

    public void setAgrigateItemCode(String agrigateItemCode) {
        this.agrigateItemCode = agrigateItemCode;
    }

    public boolean isShowSerie() {
        return showSerie;
    }

    public void setShowSerie(boolean showSerie) {
        this.showSerie = showSerie;
    }

    public String getBenchmarkAggFuncVal() {
        return benchmarkAggFuncVal;
    }

    public void setBenchmarkAggFuncVal(String benchmarkAggFuncVal) {
        this.benchmarkAggFuncVal = benchmarkAggFuncVal;
    }

    public Boolean getGradientColor() {
        return (isGradientColor != null && isGradientColor);
    }

    public void setGradientColor(Boolean gradientColor) {
        isGradientColor = gradientColor;
    }
}
