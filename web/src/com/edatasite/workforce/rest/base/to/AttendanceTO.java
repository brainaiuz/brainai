package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilsh0d Madrahimov.
 * This class for Timesheet API Syc from Excel plugin
 */
public class AttendanceTO implements IsSerializable {

    Integer employeeId;
    Long startTime; //check in
    Long endTime;  // check out

    public AttendanceTO() {
    }

    public AttendanceTO(Integer employeeId, Long startTime, Long endTime) {
        this.employeeId = employeeId;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }


}
