package com.edatasite.workforce.gwt.availability.client.rpc;

import java.util.Date;

public class AttendanceStatusDto {
    private Date day;
    private String code;
    private String title;
    private Integer dow;
    private Boolean hasShift;

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDow() {
        return dow;
    }

    public void setDow(Integer dow) {
        this.dow = dow;
    }

    public Boolean getHasShift() {
        return hasShift;
    }

    public void setHasShift(Boolean hasShift) {
        this.hasShift = hasShift;
    }
}
