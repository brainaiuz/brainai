package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Apr 28, 2010
 * Time: 2:50:29 PM
 * this class used for transffering data from "timesheet" table to TimesheetRecurrenceJob class
 */

public class TimesheetItem implements IsSerializable {
    private String employeeName;
    private Integer employeeId;
    private SelectItem[] dayAndTime;
    private ArrayList<SelectItem> dayAndTimeL;
    private String totalTime;

    public TimesheetItem() {

    }

    public TimesheetItem(Integer employeeId, String employeeName, SelectItem[] dayAndTime) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.dayAndTime = dayAndTime;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public SelectItem[] getDayAndTime() {
        return dayAndTime;
    }

    public void setDayAndTime(SelectItem[] dayAndTime) {
        this.dayAndTime = dayAndTime;
    }

    public ArrayList<SelectItem> getDayAndTimeL() {
        return dayAndTimeL;
    }

    public void setDayAndTimeL(ArrayList<SelectItem> dayAndTimeL) {
        this.dayAndTimeL = dayAndTimeL;
    }

    public String getTotalTime() {
        return totalTime != null ? totalTime : "0:00";
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }
}