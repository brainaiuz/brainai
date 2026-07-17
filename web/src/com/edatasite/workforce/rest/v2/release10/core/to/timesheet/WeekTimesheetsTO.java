package com.edatasite.workforce.rest.v2.release10.core.to.timesheet;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class WeekTimesheetsTO extends ResponseData {
    private Integer id;
    private String title;
    private String status;
    private String priority;
    private String relevance_indicator;
    private double[] days_info;

    public WeekTimesheetsTO() {
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRelevance_indicator() {
        return relevance_indicator;
    }

    public void setRelevance_indicator(String relevance_indicator) {
        this.relevance_indicator = relevance_indicator;
    }

    public double[] getDays_info() {
        return days_info;
    }

    public void setDays_info(double[] days_info) {
        this.days_info = days_info;
    }
}
