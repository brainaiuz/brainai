package com.edatasite.workforce.gwt.payroll.client.ui.view.dailyRateCalculation;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DailyRateSettings implements IsSerializable {

    public static final String TYPE_CALENDAR = "TYPE_CALENDAR";
    public static final String TYPE_FORMULA = "TYPE_FORMULA";
    public static final String TYPE_EMPLOYER_SETTINGS = "TYPE_EMPLOYER_SETTINGS";

    private Boolean excludeHoliday;
    private Boolean excludeDayOffs;
    private Integer workDaysInMonth;

    private String dailyRateType;

    public boolean isExcludeHoliday() {
        return excludeHoliday != null && excludeHoliday;
    }

    public void setExcludeHoliday(Boolean excludeHoliday) {
        this.excludeHoliday = excludeHoliday;
    }

    public Boolean isExcludeDayOffs() {
        return excludeDayOffs != null && excludeDayOffs;
    }

    public void setExcludeDayOffs(Boolean excludeDayOffs) {
        this.excludeDayOffs = excludeDayOffs;
    }

    public Integer getWorkDaysInMonth() {
        return workDaysInMonth;
    }

    public void setWorkDaysInMonth(Integer workDaysInMonth) {
        this.workDaysInMonth = workDaysInMonth;
    }

    public String getDailyRateType() {
        return dailyRateType;
    }

    public void setDailyRateType(String dailyRateType) {
        this.dailyRateType = dailyRateType;
    }
}
