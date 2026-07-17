package com.edatasite.workforce.gwt.chart.client.rpc;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

public class SerieData implements IsSerializable {

    private String name;

    /**
     * Fustom color for the specific serie {Example: color = "#FBA800"}
     *
     * If color property is null then it takes default color from global color settings
     *
     * By default color will bee null
     */
    private String color;

    /**
     * This property mostly used for the charts that generated from the system not dynamic{not from reporting} one
     */
    private ChartTypeEnum serieType;

    /**
     * This property mostly used for stacked group charts
     * */
    private String stack;

    private Number[] values;
    private Number[] valuesForColor;

    /**
     * This properties are used for gantt chart
     * */

    private String id;
    private String parent;
    private String dependency;
    private Date start;
    private Date end;
    private Boolean milestone;
    private BigDecimal percent;
    private Boolean collapsed = false;
    private HashMap<BigDecimal, String> pointColor;

    public boolean getCollapsed() {
        return this.collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public SerieData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ChartTypeEnum getSerieType() {
        return serieType;
    }

    public void setSerieType(ChartTypeEnum serieType) {
        this.serieType = serieType;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public Number[] getValues() {
        return values;
    }

    public void setValues(Number[] values) {
        this.values = values;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Boolean getMilestone() {
        return milestone;
    }

    public void setMilestone(Boolean milestone) {
        this.milestone = milestone;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public HashMap<BigDecimal, String> getPointColor() {
        if (pointColor == null) {
            pointColor = new HashMap<>();
        }
        return pointColor;
    }

    public void setPointColor(HashMap<BigDecimal, String> pointColor) {
        this.pointColor = pointColor;
    }

    public Number[] getValuesForColor() {
        return valuesForColor;
    }

    public void setValuesForColor(Number[] valuesForColor) {
        this.valuesForColor = valuesForColor;
    }
}
