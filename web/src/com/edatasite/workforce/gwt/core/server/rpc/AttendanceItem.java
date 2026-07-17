package com.edatasite.workforce.gwt.core.server.rpc;

public class AttendanceItem {

    private Integer timeslot = 0;
    private Integer timeSheet = 0;
    private Integer timeSheetPending = 0;
    private int leave = 0;
    private boolean dayOff;
    private boolean holiday;

    public Integer getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Integer timeslot) {
        this.timeslot = timeslot;
    }

    public Integer getTimeSheet() {
        return timeSheet;
    }

    public void setTimeSheet(Integer timeSheet) {
        this.timeSheet = timeSheet;
    }

    public Integer getTimeSheetPending() {
        return timeSheetPending;
    }

    public void setTimeSheetPending(Integer timeSheetPending) {
        this.timeSheetPending = timeSheetPending;
    }

    public int getLeave() {
        return leave;
    }

    public void setLeave(int leave) {
        this.leave = leave;
    }

    public boolean isDayOff() {
        return dayOff;
    }

    public void setDayOff(boolean dayOff) {
        this.dayOff = dayOff;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }
}
