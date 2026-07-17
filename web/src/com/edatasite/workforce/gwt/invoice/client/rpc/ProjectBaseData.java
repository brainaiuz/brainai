package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 14.05.2009
 * Time: 12:51:13
 * To change this template use File | Settings | File Templates.
 */
public class ProjectBaseData implements IsSerializable {

    private Integer employeeId;
    private Integer projectId;
    private Integer taskId;

    private String firstName;
    private String lastName;
    private String name;
    private String clientName;
    private String projectName;
    private String projectDescription;
    private String taskName;
    private String taskDescription;
    private String employeePosition;
    private Integer timesheetEntryId;
    private Integer timespent;
    private Double clientChargeRate;
    private Double wageRate;

    //for monthly based timesheet
    private String timesheetDescription;
    private Integer totalDaysWorked;
    private Integer overtime;
    private BigDecimal overtimeChargeRate;
    private Integer weekendOvertime;
    private BigDecimal weekendChargeRate;
    private Integer holidayOvertime;
    private BigDecimal holidayChargeRate;
    private String monthYear;

    private Integer priceType;
    private Double basicSalary;

    private Date tsEntryDate;

    private Integer[] timesheetEntryIdList;

    private Double discount;
    private boolean fixed;

    private SelectItem currency;
    private boolean ignoreExRate = true;

    public ProjectBaseData() {

    }

    public ProjectBaseData(Integer projectId, Integer employeeId, Integer taskId, String firstName, String lastName,
                           String projectName, String projectDescription, String taskName, String taskDescription,
                           Integer timesheetEntryId, Integer timespent, Date tsEntryDate, Double clientChargeRate, Double wageRate, String employeePosition) {
        this(projectId, employeeId, taskId, firstName, lastName, projectName, projectDescription, taskName, taskDescription, timesheetEntryId, timespent, tsEntryDate, clientChargeRate, wageRate, employeePosition, false);
        /*this.projectId = projectId;
        this.employeeId = employeeId;
        this.taskId = taskId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.timesheetEntryId = timesheetEntryId;
        this.timespent = timespent;
        this.tsEntryDate = tsEntryDate;
        this.clientChargeRate = clientChargeRate;
        this.wageRate = wageRate;
        this.employeePosition = employeePosition;*/
    }

    public ProjectBaseData(Integer projectId, Integer employeeId, Integer taskId, String firstName, String lastName,
                           String projectName, String projectDescription, String taskName, String taskDescription,
                           Integer timesheetEntryId, Integer timespent, Date tsEntryDate, Object clientChargeRate, Double wageRate, String employeePosition, Boolean fixed) {
        this.projectId = projectId;
        this.employeeId = employeeId;
        this.taskId = taskId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.timesheetEntryId = timesheetEntryId;
        this.timespent = timespent;
        this.tsEntryDate = tsEntryDate;

        if (clientChargeRate != null && clientChargeRate instanceof BigDecimal) {
            this.clientChargeRate = ((BigDecimal)clientChargeRate).doubleValue();
        } else {
            this.clientChargeRate = (Double) clientChargeRate;
        }
        this.wageRate = wageRate;
        this.employeePosition = employeePosition;
        this.fixed = fixed;
    }

    public ProjectBaseData(Integer projectId, Integer employeeId, Integer taskId, String firstName, String lastName,
                    String projectName, String projectDescription, String taskName, String taskDescription,
                    Integer timesheetEntryId, Integer timespent, Integer totalDaysWorked, Integer overtime,
                    Integer weekendOvertime, Integer holidayOvertime, String monthYear, Double clientChargeRate) {
        this.projectId = projectId;
        this.employeeId = employeeId;
        this.taskId = taskId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.timesheetEntryId = timesheetEntryId;
        this.timespent = timespent;
        this.totalDaysWorked = totalDaysWorked;
        this.overtime = overtime;
        this.weekendOvertime = weekendOvertime;
        this.holidayOvertime = holidayOvertime;
        this.monthYear = monthYear;
        this.clientChargeRate = clientChargeRate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Integer getTimespent() {
        return timespent;
    }

    public void setTimespent(Integer timespent) {
        this.timespent = timespent;
    }

    public Double getTimespentInHours() {
        return (timespent == null) ? 0.0 : new BigDecimal(timespent.doubleValue()).divide(new BigDecimal(60), 5, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public Double getClientChargeRate() {
        return clientChargeRate;
    }

    public void setClientChargeRate(Double clientChargeRate) {
        this.clientChargeRate = clientChargeRate;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getEmployeeName() {
        return name != null ? name : (getFirstName() != null ? getFirstName() + " " : "") + (getLastName() != null ? getLastName() : "");
    }

    public void setEmployeeName(String name) {
        this.name = name;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTimeSpentInHours() {
        return Utils.formatMinutes(timespent);
    }

    public DateNonConvertable getTsEntryDate() {
        if (tsEntryDate != null) {
            return new DateNonConvertable(tsEntryDate);
        }
        return null;
    }

    public void setTsEntryDate(Date tsEntryDate) {
        this.tsEntryDate = tsEntryDate;
    }

    public Integer getTimesheetEntryId() {
        return timesheetEntryId;
    }

    public void setTimesheetEntryId(Integer timesheetEntryId) {
        this.timesheetEntryId = timesheetEntryId;
    }

    public Integer[] getTimesheetEntryIdList() {
        return timesheetEntryIdList;
    }

    public void setTimesheetEntryIdList(Integer[] timesheetEntryIdList) {
        this.timesheetEntryIdList = timesheetEntryIdList;
    }

    public String getTimesheetDescription() {
        return timesheetDescription;
    }

    public void setTimesheetDescription(String timesheetDescription) {
        this.timesheetDescription = timesheetDescription;
    }

    public Integer getTotalDaysWorked() {
        return totalDaysWorked;
    }

    public void setTotalDaysWorked(Integer totalDaysWorked) {
        this.totalDaysWorked = totalDaysWorked;
    }

    public Integer getOvertime() {
        return overtime;
    }

    public void setOvertime(Integer overtime) {
        this.overtime = overtime;
    }

    public Integer getWeekendOvertime() {
        return weekendOvertime;
    }

    public void setWeekendOvertime(Integer weekendOvertime) {
        this.weekendOvertime = weekendOvertime;
    }

    public Integer getHolidayOvertime() {
        return holidayOvertime;
    }

    public void setHolidayOvertime(Integer holidayOvertime) {
        this.holidayOvertime = holidayOvertime;
    }

    public BigDecimal getOvertimeChargeRate() {
        return overtimeChargeRate;
    }

    public void setOvertimeChargeRate(BigDecimal overtimeChargeRate) {
        this.overtimeChargeRate = overtimeChargeRate;
    }

    public BigDecimal getWeekendChargeRate() {
        return weekendChargeRate;
    }

    public void setWeekendChargeRate(BigDecimal weekendChargeRate) {
        this.weekendChargeRate = weekendChargeRate;
    }

    public BigDecimal getHolidayChargeRate() {
        return holidayChargeRate;
    }

    public void setHolidayChargeRate(BigDecimal holidayChargeRate) {
        this.holidayChargeRate = holidayChargeRate;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public Double getWageRate() {
        return wageRate;
    }

    public void setWageRate(Double wageRate) {
        this.wageRate = wageRate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getEmployeePosition() {
        return employeePosition;
    }

    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public boolean isIgnoreExRate() {
        return ignoreExRate;
    }

    public void setIgnoreExRate(boolean ignoreExRate) {
        this.ignoreExRate = ignoreExRate;
    }
}
