package com.edatasite.workforce.rest.v2.release10.core.to.timesheet;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class TotalTimesheetsTO extends ResponseData {
    private Integer id;
    private String title;
    private String status;
    private String priority;
    private String relevance_indicator;
    private Float completion_percentage;
    private Double estimate;
    private Double total;

    public TotalTimesheetsTO() {
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

    public Float getCompletion_percentage() {
        return completion_percentage;
    }

    public void setCompletion_percentage(Float completion_percentage) {
        this.completion_percentage = completion_percentage;
    }

    public Double getEstimate() {
        return estimate;
    }

    public void setEstimate(Double estimate) {
        this.estimate = estimate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
