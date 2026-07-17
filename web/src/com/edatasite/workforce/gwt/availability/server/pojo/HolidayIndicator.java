package com.edatasite.workforce.gwt.availability.server.pojo;

public class HolidayIndicator {
    private Holiday holiday;
    private int indicator = 0;

    public HolidayIndicator(Holiday holiday, int indicator) {
        this.holiday = holiday;
        this.indicator = indicator;
    }

    public Holiday getHoliday() {
        return holiday;
    }

    public void setHoliday(Holiday holiday) {
        this.holiday = holiday;
    }

    public int getIndicator() {
        return indicator;
    }

    public void setIndicator(int indicator) {
        this.indicator = indicator;
    }
}
