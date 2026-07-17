package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Umidbek.
 */
public class TimesheetDayTO implements IsSerializable {
    Long date;
    Integer plannedTime;

    Boolean isHoliday;
    Boolean isLeaveRequested;

    public TimesheetDayTO() {
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Integer getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(Integer plannedTime) {
        this.plannedTime = plannedTime;
    }

    public Boolean getIsHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(Boolean isHoliday) {
        this.isHoliday = isHoliday;
    }

    public Boolean getIsLeaveRequested() {
        return isLeaveRequested;
    }

    public void setIsLeaveRequested(Boolean isLeaveRequested) {
        this.isLeaveRequested = isLeaveRequested;
    }
}
