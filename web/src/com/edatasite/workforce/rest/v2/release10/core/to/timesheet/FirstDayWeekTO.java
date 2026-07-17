package com.edatasite.workforce.rest.v2.release10.core.to.timesheet;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class FirstDayWeekTO extends ResponseData {
    private String first_day;

    public FirstDayWeekTO() {
    }

    public FirstDayWeekTO(String first_day) {
        this.first_day = first_day;
    }

    public String getFirst_day() {
        return first_day;
    }

    public void setFirst_day(String first_day) {
        this.first_day = first_day;
    }
}
