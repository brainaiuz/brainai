package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;

/**
 * User : Akhror on 03/03/2023
 */
public class AttendanceDto {
    private int day;
    private boolean isHoliday;
    private boolean isDayOff;
    private long hours;
    private long overtime;
    private String timeslot;
    private ItemDto leave;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean holiday) {
        this.isHoliday = holiday;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public ItemDto getLeave() {
        return leave;
    }

    public void setLeave(ItemDto leave) {
        this.leave = leave;
    }

    public boolean isDayOff() {
        return isDayOff;
    }

    public void setDayOff(boolean dayOff) {
        isDayOff = dayOff;
    }

    public long getOvertime() {
        return overtime;
    }

    public void setOvertime(long overtime) {
        this.overtime = overtime;
    }
}
