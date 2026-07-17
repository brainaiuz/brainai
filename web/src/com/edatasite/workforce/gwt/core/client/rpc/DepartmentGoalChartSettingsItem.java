package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Transport for a goal's chart settings (chart type + selected range/period).
 */
public class DepartmentGoalChartSettingsItem implements IsSerializable {

    // Visual defaults (used when a setting was never saved)
    public static final String DEFAULT_TARGET_COLOR = "#FBA800";
    public static final String DEFAULT_ACTUAL_COLOR = "#158ED0";
    public static final String DEFAULT_LEGEND_POSITION = "BOTTOM";
    public static final boolean DEFAULT_SHOW_SERIES = true;
    public static final String DEFAULT_LABEL_FORMAT = "VALUE";
    public static final String DEFAULT_LINE_STYLE = "SOLID";
    public static final String DEFAULT_PIE_STYLE = "PIE";
    public static final boolean DEFAULT_SHOW_PIE = true;

    private String chartType;
    private String period;
    private Date customFrom;
    private Date customTo;
    private String targetColor;
    private String actualColor;
    private String legendPosition;
    private Boolean showSeries;
    private String labelFormat;
    private String lineStyle;
    private String pieStyle;
    private Boolean showPie;



    public DepartmentGoalChartSettingsItem() {
    }

    public DepartmentGoalChartSettingsItem(String chartType, String period, Date customFrom, Date customTo) {
        this.chartType = chartType;
        this.period = period;
        this.customFrom = customFrom;
        this.customTo = customTo;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Date getCustomFrom() {
        return customFrom;
    }

    public void setCustomFrom(Date customFrom) {
        this.customFrom = customFrom;
    }

    public Date getCustomTo() {
        return customTo;
    }

    public void setCustomTo(Date customTo) {
        this.customTo = customTo;
    }

    public String getTargetColor() {
        return targetColor;
    }

    public void setTargetColor(String targetColor) {
        this.targetColor = targetColor;
    }

    public String getActualColor() {
        return actualColor;
    }

    public void setActualColor(String actualColor) {
        this.actualColor = actualColor;
    }

    public String getLegendPosition() {
        return legendPosition;
    }

    public void setLegendPosition(String legendPosition) {
        this.legendPosition = legendPosition;
    }

    public Boolean getShowSeries() {
        return showSeries;
    }

    public void setShowSeries(Boolean showSeries) {
        this.showSeries = showSeries;
    }

    public String getLabelFormat() {
        return labelFormat;
    }

    public void setLabelFormat(String labelFormat) {
        this.labelFormat = labelFormat;
    }

    public String getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(String lineStyle) {
        this.lineStyle = lineStyle;
    }

    public String getPieStyle() {
        return pieStyle;
    }

    public void setPieStyle(String pieStyle) {
        this.pieStyle = pieStyle;
    }

    public Boolean getShowPie() {
        return showPie;
    }

    public void setShowPie(Boolean showPie) {
        this.showPie = showPie;
    }

    /**
     * Copy with every visual field filled (nulls replaced by defaults). chartType /
     * period / custom window pass through unchanged (the chart applies its own defaults).
     */
    public DepartmentGoalChartSettingsItem withDefaults() {
        DepartmentGoalChartSettingsItem s = new DepartmentGoalChartSettingsItem();
        s.chartType = this.chartType;
        s.period = this.period;
        s.customFrom = this.customFrom;
        s.customTo = this.customTo;
        s.targetColor = this.targetColor != null ? this.targetColor : DEFAULT_TARGET_COLOR;
        s.actualColor = this.actualColor != null ? this.actualColor : DEFAULT_ACTUAL_COLOR;
        s.legendPosition = this.legendPosition != null ? this.legendPosition : DEFAULT_LEGEND_POSITION;
        s.showSeries = this.showSeries != null ? this.showSeries : DEFAULT_SHOW_SERIES;
        s.labelFormat = this.labelFormat != null ? this.labelFormat : DEFAULT_LABEL_FORMAT;
        s.lineStyle = this.lineStyle != null ? this.lineStyle : DEFAULT_LINE_STYLE;
        s.pieStyle = this.pieStyle != null ? this.pieStyle : DEFAULT_PIE_STYLE;
        s.showPie = this.showPie != null ? this.showPie : DEFAULT_SHOW_PIE;
        return s;
    }

    /** Convenience for a possibly-null source (e.g. straight off GoalItem). */
    public static DepartmentGoalChartSettingsItem withDefaults(DepartmentGoalChartSettingsItem source) {
        return (source != null ? source : new DepartmentGoalChartSettingsItem()).withDefaults();
    }
}
