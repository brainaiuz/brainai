package com.edatasite.workforce.gwt.timesheet.client.ui.view;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by azizjon on 7/31/2015.
 */
public class MonthlyTimesheetItem implements IsSerializable {

    private Integer employeeID;
    private Integer projectEmployeeID;
    private String employeeName;
    private Long employeeNumber;
    private Integer month;
    private Integer year;
    private Double workedHours;
    private Double totalWorkedDays;
    private Double overtimeHours;
    private Double holidayOvertimeHours;
    private Double weekendOvertimeHours;
    private String projectName;
    private String contractStart;
    private String contractEnd;

    public MonthlyTimesheetItem() {
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Long getSortEmployeeNumber() {
        return employeeNumber != null ? employeeNumber : 0L;
    }

    public Long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public Integer getProjectEmployeeID() {
        return projectEmployeeID;
    }

    public void setProjectEmployeeID(Integer projectEmployeeID) {
        this.projectEmployeeID = projectEmployeeID;
    }

    public Double getWorkedHours() {
        return workedHours;
    }

    public void setWorkedHours(Double workedHours) {
        this.workedHours = workedHours;
    }

    public Double getTotalWorkedDays() {
        return totalWorkedDays;
    }

    public void setTotalWorkedDays(Double totalWorkedDays) {
        this.totalWorkedDays = totalWorkedDays;
    }

    public Double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(Double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public Double getHolidayOvertimeHours() {
        return holidayOvertimeHours;
    }

    public void setHolidayOvertimeHours(Double holidayOvertimeHours) {
        this.holidayOvertimeHours = holidayOvertimeHours;
    }

    public Double getWeekendOvertimeHours() {
        return weekendOvertimeHours;
    }

    public void setWeekendOvertimeHours(Double weekendOvertimeHours) {
        this.weekendOvertimeHours = weekendOvertimeHours;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getContractStart() {
        return contractStart;
    }

    public void setContractStart(String contractStart) {
        this.contractStart = contractStart;
    }

    public String getContractEnd() {
        return contractEnd;
    }

    public void setContractEnd(String contractEnd) {
        this.contractEnd = contractEnd;
    }
}
