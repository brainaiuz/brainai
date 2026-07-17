package com.edatasite.workforce.gwt.chart.client.rpc;

import java.util.Date;

/**
 * User: Abror Abdukadirov
 * Date: 06.05.2019 18:58
 */
public class GanttSerieData extends SerieData {
    private Date actualEndDate;
    private Date actualStartDate;
    private String hourSpent;
    private Integer estimated;
    private String actualHoursSpent;
    private String overallStatusName;

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public String getHourSpent() {
        return hourSpent;
    }

    public void setHourSpent(String hourSpent) {
        this.hourSpent = hourSpent;
    }

    public Integer getEstimated() {
        return estimated;
    }

    public void setEstimated(Integer estimated) {
        this.estimated = estimated;
    }

    public String getActualHoursSpent() {
        return actualHoursSpent;
    }

    public void setActualHoursSpent(String actualHoursSpent) {
        this.actualHoursSpent = actualHoursSpent;
    }

    public String getOverallStatusName() {
        return overallStatusName;
    }

    public void setOverallStatusName(String overallStatusName) {
        this.overallStatusName = overallStatusName;
    }
}
