package com.edatasite.workforce.core.domain.goal;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.DepartmentGoalChartSettingsItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Per-goal chart preferences: the chosen chart type and the selected range/period.
 * One active row per goal (see the unique index on goalid in the creating SQL patch).
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "departmentgoalchartsettings")
public class EdsDepartmentGoalChartSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "goalid")
    private Integer goalId;

    @Column(name = "charttype")
    private String chartType;

    @Column(name = "period")
    private String period;

    @Column(name = "customfrom")
    private Date customFrom;

    @Column(name = "customto")
    private Date customTo;

    @Column(name = "targetcolor")
    private String targetColor;

    @Column(name = "actualcolor")
    private String actualColor;

    @Column(name = "legendposition")
    private String legendPosition;

    @Column(name = "showseries")
    private Boolean showSeries;

    @Column(name = "labelformat")
    private String labelFormat;

    @Column(name = "linestyle")
    private String lineStyle;

    @Column(name = "piestyle")
    private String pieStyle;

    @Column(name = "showpie")
    private Boolean showPie;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getGoalId() {
        return goalId;
    }

    public void setGoalId(Integer goalId) {
        this.goalId = goalId;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    public void applyDto(DepartmentGoalChartSettingsItem item) {
        if (item == null) {
            return;
        }

        setChartType(item.getChartType());
        setPeriod(item.getPeriod());
        setCustomFrom(item.getCustomFrom());
        setCustomTo(item.getCustomTo());
        setTargetColor(item.getTargetColor());
        setActualColor(item.getActualColor());
        setLegendPosition(item.getLegendPosition());
        setShowSeries(item.getShowSeries());
        setLabelFormat(item.getLabelFormat());
        setLineStyle(item.getLineStyle());
        setPieStyle(item.getPieStyle());
        setShowPie(item.getShowPie());
    }

    public DepartmentGoalChartSettingsItem toDto() {
        DepartmentGoalChartSettingsItem item = new DepartmentGoalChartSettingsItem();

        item.setChartType(getChartType());
        item.setPeriod(getPeriod());
        item.setCustomFrom(getCustomFrom());
        item.setCustomTo(getCustomTo());
        item.setTargetColor(getTargetColor());
        item.setActualColor(getActualColor());
        item.setLegendPosition(getLegendPosition());
        item.setShowSeries(getShowSeries());
        item.setLabelFormat(getLabelFormat());
        item.setLineStyle(getLineStyle());
        item.setPieStyle(getPieStyle());
        item.setShowPie(getShowPie());

        return item;
    }

}
