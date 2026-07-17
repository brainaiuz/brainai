package com.edatasite.workforce.gwt.core.client.rpc.payroll;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/4/15
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class MonthlyOvertimeData implements IsSerializable {

    private Integer objectID;
    private BigDecimal regularOvertimeHours;
    private BigDecimal weeklyOvertimeHours;
    private BigDecimal holidayOvertimeHours;
    private BigDecimal accomodationPayDays;
    private BigDecimal foodPayDays;
    private BigDecimal positionSalary;
    private BigDecimal totalWorkedDays;
    private BigDecimal totalDaysOfMonth;

    private String monthYear;
    private Integer projectEmployeeID;

    public BigDecimal getRegularOvertimeHours() {
        return regularOvertimeHours;
    }

    public void setRegularOvertimeHours(BigDecimal regularOvertimeHours) {
        this.regularOvertimeHours = regularOvertimeHours;
    }

    public BigDecimal getWeeklyOvertimeHours() {
        return weeklyOvertimeHours;
    }

    public void setWeeklyOvertimeHours(BigDecimal weeklyOvertimeHours) {
        this.weeklyOvertimeHours = weeklyOvertimeHours;
    }

    public BigDecimal getHolidayOvertimeHours() {
        return holidayOvertimeHours;
    }

    public void setHolidayOvertimeHours(BigDecimal holidayOvertimeHours) {
        this.holidayOvertimeHours = holidayOvertimeHours;
    }

    public BigDecimal getAccomodationPayDays() {
        return accomodationPayDays;
    }

    public void setAccomodationPayDays(BigDecimal accomodationPayDays) {
        this.accomodationPayDays = accomodationPayDays;
    }

    public BigDecimal getFoodPayDays() {
        return foodPayDays;
    }

    public void setFoodPayDays(BigDecimal foodPayDays) {
        this.foodPayDays = foodPayDays;
    }

    public BigDecimal getPositionSalary() {
        return positionSalary;
    }

    public void setPositionSalary(BigDecimal positionSalary) {
        this.positionSalary = positionSalary;
    }

    public BigDecimal getTotalWorkedDays() {
        return totalWorkedDays;
    }

    public void setTotalWorkedDays(BigDecimal totalWorkedDays) {
        this.totalWorkedDays = totalWorkedDays;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public BigDecimal getTotalDaysOfMonth() {
        return totalDaysOfMonth;
    }

    public void setTotalDaysOfMonth(BigDecimal totalDaysOfMonth) {
        this.totalDaysOfMonth = totalDaysOfMonth;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public Integer getProjectEmployeeID() {
        return projectEmployeeID;
    }

    public void setProjectEmployeeID(Integer projectEmployeeID) {
        this.projectEmployeeID = projectEmployeeID;
    }
}
