package com.edatasite.workforce.gwt.core.client.rpc.payroll;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/17/15
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class DailyOvertimeData implements IsSerializable {

    private Boolean dayOff;
    private Boolean holiday;
    private Date date;
    private BigDecimal timeslotHour;
    private BigDecimal overtimeHour;
    private BigDecimal absenceHour;
    private BigDecimal timeSheet;


    public Boolean isDayOff() {
        return dayOff != null ? dayOff : Boolean.FALSE;
    }

    public void setDayOff(Boolean dayOff) {
        this.dayOff = dayOff;
    }

    public Boolean isHoliday() {
        return holiday != null ? holiday : Boolean.FALSE;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getOvertimeHour() {
        return overtimeHour;
    }

    public void setOvertimeHour(BigDecimal overtimeHour) {
        this.overtimeHour = overtimeHour;
    }

    public BigDecimal getAbsenceHour() {
        return absenceHour;
    }

    public void setAbsenceHour(BigDecimal absenceHour) {
        this.absenceHour = absenceHour;
    }

    public BigDecimal getTimeslotHour() {
        return timeslotHour;
    }

    public void setTimeslotHour(BigDecimal timeslotHour) {
        this.timeslotHour = timeslotHour;
    }

    public BigDecimal getTimeSheet() {
        return timeSheet;
    }

    public void setTimeSheet(BigDecimal timeSheet) {
        this.timeSheet = timeSheet;
    }
}
