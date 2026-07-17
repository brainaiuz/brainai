package com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 10/11/2017.
 */
public class LeaveReasonStateTO extends ResponseData {
    private String title;
    private Double hours;
    private Double days;

    public LeaveReasonStateTO() {
    }

    public LeaveReasonStateTO(String title, Double hours, Double days) {
        this.title = title;
        this.hours = hours;
        this.days = days;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public Double getDays() {
        return days;
    }

    public void setDays(Double days) {
        this.days = days;
    }
}
