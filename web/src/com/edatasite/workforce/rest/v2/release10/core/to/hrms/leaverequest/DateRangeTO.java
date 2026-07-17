package com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class DateRangeTO extends ResponseData {
    private String from;
    private String to;
    private Double hours;
    private Double days;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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
