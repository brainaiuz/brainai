package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 30.04.2009
 * Time: 18:22:48
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetEntry implements IsSerializable {
    private Integer timeSheetId;
    private Integer timeSpent;
    private Integer employeeId;

    public Integer getTimeSheetId() {

        return timeSheetId;
    }

    public void setTimeSheetId(Integer timeSheetId) {
        this.timeSheetId = timeSheetId;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
