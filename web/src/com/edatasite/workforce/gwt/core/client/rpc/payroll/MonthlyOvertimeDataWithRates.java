package com.edatasite.workforce.gwt.core.client.rpc.payroll;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by Shohruh on 06-Jul-16.
 */
public class MonthlyOvertimeDataWithRates implements IsSerializable {
    Integer rateType;
    BigDecimal rate;
    BigDecimal overtimeRate;
    BigDecimal weekendOvertimeRate;
    BigDecimal holidayOvertimeRate;
    Integer daysOfPresence;
    BigDecimal workedHours;
    BigDecimal plannedHours;
    BigDecimal overtimeHours;
    BigDecimal weekendOvertimeHours;
    BigDecimal holidayOvertimeHours;
    Integer clientId;

    public Integer getRateType() {
        return rateType;
    }

    public void setRateType(Integer rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getOvertimeRate() {
        return overtimeRate;
    }

    public void setOvertimeRate(BigDecimal overtimeRate) {
        this.overtimeRate = overtimeRate;
    }

    public BigDecimal getWeekendOvertimeRate() {
        return weekendOvertimeRate;
    }

    public void setWeekendOvertimeRate(BigDecimal weekendOvertimeRate) {
        this.weekendOvertimeRate = weekendOvertimeRate;
    }

    public BigDecimal getHolidayOvertimeRate() {
        return holidayOvertimeRate;
    }

    public void setHolidayOvertimeRate(BigDecimal holidayOvertimeRate) {
        this.holidayOvertimeRate = holidayOvertimeRate;
    }

    public Integer getDaysOfPresence() {
        return daysOfPresence;
    }

    public void setDaysOfPresence(Integer daysOfPresence) {
        this.daysOfPresence = daysOfPresence;
    }

    public BigDecimal getWorkedHours() {
        return workedHours;
    }

    public void setWorkedHours(BigDecimal workedHours) {
        this.workedHours = workedHours;
    }

    public BigDecimal getPlannedHours() {
        return plannedHours;
    }

    public void setPlannedHours(BigDecimal plannedHours) {
        this.plannedHours = plannedHours;
    }

    public BigDecimal getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(BigDecimal overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public BigDecimal getWeekendOvertimeHours() {
        return weekendOvertimeHours;
    }

    public void setWeekendOvertimeHours(BigDecimal weekendOvertimeHours) {
        this.weekendOvertimeHours = weekendOvertimeHours;
    }

    public BigDecimal getHolidayOvertimeHours() {
        return holidayOvertimeHours;
    }

    public void setHolidayOvertimeHours(BigDecimal holidayOvertimeHours) {
        this.holidayOvertimeHours = holidayOvertimeHours;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClienttId(Integer clientId) {
        this.clientId = clientId;
    }
}
