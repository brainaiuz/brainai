package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EmployeePresentItem implements IsSerializable {
    private Integer employeeId;
    private DateNonConvertable dateItem;
    private DateNonConvertable from;
    private DateNonConvertable to;
    private Integer reasonID;
    private Integer shiftId;
    private Integer timeSlotId;

    public EmployeePresentItem(Integer employeeId, DateNonConvertable dateItem, DateNonConvertable from, DateNonConvertable to, Integer reasonID, Integer shiftId, Integer timeSlotId) {
        this.employeeId = employeeId;
        this.dateItem = dateItem;
        this.from = from;
        this.to = to;
        this.reasonID = reasonID;
        this.shiftId = shiftId;
        this.timeSlotId = timeSlotId;
    }

    public EmployeePresentItem() {
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public DateNonConvertable getDateItem() {
        return dateItem;
    }

    public void setDateItem(DateNonConvertable dateItem) {
        this.dateItem = dateItem;
    }

    public DateNonConvertable getFrom() {
        return from;
    }

    public void setFrom(DateNonConvertable from) {
        this.from = from;
    }

    public DateNonConvertable getTo() {
        return to;
    }

    public void setTo(DateNonConvertable to) {
        this.to = to;
    }

    public Integer getReasonID() {
        return reasonID;
    }

    public void setReasonID(Integer reasonID) {
        this.reasonID = reasonID;
    }

    public Integer getShiftId() {
        return shiftId;
    }

    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }

    public Integer getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Integer timeSlotId) {
        this.timeSlotId = timeSlotId;
    }
}