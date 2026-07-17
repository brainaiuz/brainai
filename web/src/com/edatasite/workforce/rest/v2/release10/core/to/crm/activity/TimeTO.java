package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
/**
 * Created by Abdurakhmonov Farrukh on 03/17/2018.
 */
public class TimeTO extends ResponseData {
    private Integer hour;
    private Integer minute;

    public TimeTO() {
    }

    public TimeTO(Integer hour, Integer minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }
}
