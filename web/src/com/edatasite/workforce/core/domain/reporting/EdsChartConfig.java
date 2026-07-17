package com.edatasite.workforce.core.domain.reporting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;
import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.enums.LegendPositionEnum;
import com.edatasite.workforce.gwt.chart.client.enums.StackedEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.GaugeChartConfig;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieColumn;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieConfItem;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "chart_config")
public class EdsChartConfig extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    /**
     * Chart type {Pie, Bar, Column, Line, ...}
     */
    @Enumerated(value = EnumType.STRING)
    private ChartTypeEnum type;

    private String title;

    private String scale;

    @Column(length = 500)
    private String subtitle;

    @Column(length = 500)
    private String chartViewOption;

    @Column(length = 500)
    private String chartViewOptionType;
    private boolean showLabel;
    @Column(name = "showstacked", columnDefinition = "boolean default false")
    private boolean showStacked;
    @Column(name = "showserie", columnDefinition = "boolean default false")
    private boolean showSerie;
    @Column(name = "showpiechart", columnDefinition = "boolean default false")
    private boolean showPieChart;

    /**
     * page size is a count of shown series in chart
     */
    private Integer pageSize;

    private String sortBy;
    private String sortType;
    private String dateSortPeriodType;

    @Enumerated(value = EnumType.STRING)
    private LegendPositionEnum legend;

    /**
     * show/hide series value or show by value or by percent for stacked charts
     */
    @Enumerated(value = EnumType.STRING)
    private StackedEnum stacked;

    /**
     * X axis column
     */
    @Type(type = "text")
    @Column(name = "xaxis_json")
    private String xAxisJson;

    @Type(type = "text")
    @Column(name = "drillxaxis_json")
    private String drillxAxisJson;

    @Type(type = "text")
    @Column(name = "serieconfs_json")
    private String serieConfsJson;

    @Type(type = "text")
    @Column(name = "modules_json")
    private String modulesJson;

    /**
     * this field contains gauge chart configurations
     */
    @Type(type = "text")
    @Column(name = "gaugeconfig_json")
    private String gaugeConfigJson;

    @Type(type = "text")
    private String splitBy;
    @Column(precision = 25, scale = 5)
    private BigDecimal benchmarkValue;
    @Type(type = "text")
    private String customSorderColumn;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customFormLocalizationId")
    @NotFound(action = NotFoundAction.IGNORE)
    private EdsCustomFormLocalization customFormLocalization;

    @Column(length = 500)
    private String totalFieldName;
    @Column
    private String agrigateItemCode;
    @Column
    private String benchmarkAggFuncVal;
    @Column
    private Boolean isGradientColor;
    @Column(name = "pie_chart_position", length = 50)
    private String pieChartPosition;

    @Override
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

    public boolean isShowLabel() {
        return showLabel;
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    public boolean isShowStacked() {
        return showStacked;
    }

    public void setShowStacked(boolean showStacked) {
        this.showStacked = showStacked;
    }

    public boolean isShowSerie() {
        return showSerie;
    }

    public void setShowSerie(boolean showSerie) {
        this.showSerie = showSerie;
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

    public String getDateSortPeriodType() {
        return dateSortPeriodType;
    }

    public void setDateSortPeriodType(String dateSortPeriodType) {
        this.dateSortPeriodType = dateSortPeriodType;
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

    public SerieColumn getxAxis() {
        return StringUtils.isNotEmpty(xAxisJson) ? new Gson().fromJson(xAxisJson, SerieColumn.class) : null;
    }

    public String getPieChartPosition() {
        return pieChartPosition;
    }

    public void setPieChartPosition(String pieChartPosition) {
        this.pieChartPosition = pieChartPosition;
    }

    public void setxAxis(SerieColumn xAxis) {

        if (xAxis != null) {
            this.xAxisJson = new Gson().toJson(xAxis);
        } else {
            this.xAxisJson = null;
        }
    }

    public boolean isShowPieChart() {
        return showPieChart;
    }

    public void setShowPieChart(boolean showPieChart) {
        this.showPieChart = showPieChart;
    }

    public SerieColumn getDrillxAxis() {
        return StringUtils.isNotEmpty(drillxAxisJson) ? new Gson().fromJson(drillxAxisJson, SerieColumn.class) : null;
    }

    public void setDrillxAxis(SerieColumn drillxAxis) {

        if (drillxAxis != null) {
            this.drillxAxisJson = new Gson().toJson(drillxAxis);
        } else {
            this.drillxAxisJson = null;
        }
    }

    public LinkedList<SerieConfItem> getSerieConfs() {
        return StringUtils.isNotEmpty(serieConfsJson) ? new Gson().fromJson(serieConfsJson, new TypeToken<LinkedList<SerieConfItem>>() {
        }.getType()) : null;
    }

    public void setSerieConfs(LinkedList<SerieConfItem> serieConfs) {

        if (serieConfs != null && !serieConfs.isEmpty()) {
            this.serieConfsJson = new Gson().toJson(serieConfs);
        } else {
            this.serieConfsJson = null;
        }
    }

    public ArrayList<ModuleEnum> getModules() {
        return StringUtils.isNotEmpty(modulesJson) ? new Gson().fromJson(modulesJson, new TypeToken<ArrayList<ModuleEnum>>() {
        }.getType()) : null;
    }

    public void setModules(ArrayList<ModuleEnum> modules) {

        if (modules != null && !modules.isEmpty()) {
            this.modulesJson = new Gson().toJson(modules);
        } else {
            this.modulesJson = null;
        }
    }

    public GaugeChartConfig getGaugeConfig() {
        return StringUtils.isNotEmpty(gaugeConfigJson) ? new Gson().fromJson(gaugeConfigJson, GaugeChartConfig.class) : null;
    }

    public void setGaugeConfig(GaugeChartConfig gaugeConfig) {

        if (gaugeConfig != null) {
            this.gaugeConfigJson = new Gson().toJson(gaugeConfig);
        } else {
            this.gaugeConfigJson = null;
        }
    }

    public SerieColumn getSplitBy() {
        return StringUtils.isNotEmpty(splitBy) ? new Gson().fromJson(splitBy, SerieColumn.class) : null;
    }

    public void setSplitBy(SerieColumn splitBy) {
        if (splitBy != null) {
            this.splitBy = new Gson().toJson(splitBy);
        } else {
            this.splitBy = null;
        }
    }

    public SerieColumn getCustomSorderColumn() {
        return StringUtils.isNotEmpty(customSorderColumn) ? new Gson().fromJson(customSorderColumn, SerieColumn.class) : null;
    }

    public void setCustomSorderColumn(SerieColumn customSorderColumn) {
        if (customSorderColumn != null) {
            this.customSorderColumn = new Gson().toJson(customSorderColumn);
        } else {
            this.customSorderColumn = null;
        }
    }

    public BigDecimal getBenchmarkValue() {
        return benchmarkValue;
    }

    public void setBenchmarkValue(BigDecimal benchmarkValue) {
        this.benchmarkValue = benchmarkValue;
    }

    public ChartConfItem getRPC() {
        ChartConfItem confItem = new ChartConfItem();
        confItem.setObjectID(getObjectID());
        confItem.setType(getType());
        confItem.setTitle(getTitle());
        confItem.setScale(getScale() != null ? getScale() : BigDecimal.ZERO.toString());
        confItem.setPageSize(getPageSize());
        confItem.setSortBy(getSortBy());
        confItem.setSortType(getSortType());
        confItem.setDateSortPeriodType(getDateSortPeriodType());
        confItem.setSplitBy(getSplitBy());
        confItem.setChartViewOption(getChartViewOption());
        confItem.setChartViewOptionType(getChartViewOptionType());
        confItem.setCustomSortColumn(getCustomSorderColumn());
        confItem.setBenchmarkValue(getBenchmarkValue());
        confItem.setModules(getModules());
        confItem.setxAxis(getxAxis());
        confItem.setSeries(getSerieConfs());
        confItem.setShowLabel(isShowLabel());
        confItem.setShowSerie(isShowSerie());
        confItem.setShowStacked(isShowStacked());
        confItem.setStacked(getStacked());
        confItem.setLegend(getLegend());
        confItem.setGaugeConfig(getGaugeConfig());
        confItem.setBenchmarkAggFuncVal(getBenchmarkAggFuncVal());
        confItem.setAgrigateItemCode(getAgrigateItemCode());
        confItem.setTotalFieldName(getTotalFieldName());
        confItem.setGradientColor(getGradientColor());
        confItem.setDrillxAxis(getDrillxAxis());
        confItem.setShowPieChart(isShowPieChart());
        confItem.setPieChartPosition(getPieChartPosition());
        if (getCustomFormLocalization() != null) {
            confItem.setLocalization(getCustomFormLocalization().getRPC());
        }
        return confItem;
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

    public EdsChartConfig getNew(EdsChartConfig config) {
        config = config != null ? config : new EdsChartConfig();

        config.type = type;
        config.title = title;
        config.scale = scale;
        config.subtitle = subtitle;
        config.showLabel = showLabel;
        config.showStacked = showStacked;
        config.showSerie = showSerie;
        config.pageSize = pageSize;
        config.sortBy = sortBy;
        config.sortType = sortType;
        config.dateSortPeriodType = dateSortPeriodType;
        config.legend = legend;
        config.stacked = stacked;
        config.xAxisJson = xAxisJson;
        config.drillxAxisJson = drillxAxisJson;
        config.serieConfsJson = serieConfsJson;
        config.modulesJson = modulesJson;
        config.gaugeConfigJson = gaugeConfigJson;
        config.splitBy = splitBy;
        config.benchmarkValue = benchmarkValue;
        config.customSorderColumn = customSorderColumn;
        config.chartViewOption = chartViewOption;
        config.benchmarkAggFuncVal = benchmarkAggFuncVal;
        config.agrigateItemCode = agrigateItemCode;
        config.isGradientColor = isGradientColor;
        config.showPieChart = showPieChart;
        config.pieChartPosition = pieChartPosition;

        return config;
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

    public EdsCustomFormLocalization getCustomFormLocalization() {
        return customFormLocalization;
    }

    public void setCustomFormLocalization(EdsCustomFormLocalization customFormLocalization) {
        this.customFormLocalization = customFormLocalization;
    }

    public String getBenchmarkAggFuncVal() {
        return benchmarkAggFuncVal;
    }

    public void setBenchmarkAggFuncVal(String benchmarkAggFuncVal) {
        this.benchmarkAggFuncVal = benchmarkAggFuncVal;
    }

    public Boolean getGradientColor() {
        return isGradientColor;
    }

    public void setGradientColor(Boolean gradientColor) {
        isGradientColor = gradientColor;
    }
}
