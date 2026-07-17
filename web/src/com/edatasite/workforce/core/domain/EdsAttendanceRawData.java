package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * User: Farhod
 * Date: 02/03/12
 * Time: 11:58
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "attendancerawdata",
        indexes = {
                @Index(columnList = "employeeId", name = "attendancerawdata_employeeId_idx"),
                @Index(columnList = "date", name = "attendancerawdata_date_idx")
        })
public class EdsAttendanceRawData extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId")
    private EdsEmployee employee;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(nullable = false, columnDefinition = "int4 default 0")
    private Integer timeSlot = 0;//time slot minutes for the day

    @Column(nullable = false, columnDefinition = "int4 default 0")
    private Integer timeSheet = 0;//time sheet minutes for the day >> only approved

    @Column(nullable = false, columnDefinition = "int4 default 0")
    private Integer timeSheetPending = 0;//time sheet minutes for the day >> only pending

    @Column(nullable = false, columnDefinition = "int4 default 0")
    private Integer inTime = 0;//in minutes for the day

    @Column(nullable = false, columnDefinition = "int4 default 0")
    private Integer leave = 0;//leave minutes for the day

    @Column(nullable = false, columnDefinition = "int4 default 0")
    private Integer leaveDenied = 0;//denied leave minutes for the day

    @Column(nullable = false, columnDefinition = "int4 default 0")
    private Integer leavePending = 0;//pending leave minutes for the day

    @Column(nullable = false, columnDefinition = "int4 default 0")
    private Integer overTime = 0;//over time minutes for the day

    @Column(nullable = false, columnDefinition = "int4 default 0")// tes
    private Integer budgeted = 0;//budgeted hours for the day in minutes >> in - leave

    private Double wage;//wage rate per hour

    @Column(name = "dayOff", columnDefinition = "boolean default false")
    private Boolean dayOff = false;//whether the day is day off

    @Column(name = "holiday", columnDefinition = "boolean default false")
    private Boolean holiday = false;//whether the day is holiday

    @Column(nullable = false, columnDefinition = "int4 default 0")
    private Integer paidTime = 0;//leave request type

    @Column(nullable = false, columnDefinition = "int4 default 0")
    private Integer fromAnnualLeaveTime = 0;//leave request from annual leave time

    @Column(name = "holidayfromannualleave", columnDefinition = "boolean default false")
    private Boolean holidayFromAnnualLeave = false;

    public Integer getObjectID() {
        return objectID;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    /*public Integer getDateID() {
        return dateID;
    }

    public void setDateID(Integer dateID) {
        this.dateID = dateID;
    }*/

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(Integer timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Integer getTimeSheet() {
        return timeSheet;
    }

    public void setTimeSheet(Integer timeSheet) {
        this.timeSheet = timeSheet;
    }

    public Integer getTimeSheetPending() {
        return timeSheetPending;
    }

    public void setTimeSheetPending(Integer timeSheetPending) {
        this.timeSheetPending = timeSheetPending;
    }

    public Integer getInTime() {
        return inTime;
    }

    public void setInTime(Integer inTime) {
        this.inTime = inTime;
    }

    public Integer getLeave() {
        return leave;
    }

    public void setLeave(Integer leave) {
        this.leave = leave;
    }

    public Integer getLeaveDenied() {
        return leaveDenied;
    }

    public void setLeaveDenied(Integer leaveDenied) {
        this.leaveDenied = leaveDenied;
    }

    public Integer getLeavePending() {
        return leavePending;
    }

    public void setLeavePending(Integer leavePending) {
        this.leavePending = leavePending;
    }

    public Integer getOverTime() {
        return overTime;
    }

    public void setOverTime(Integer overTime) {
        this.overTime = overTime;
    }

    public Integer getBudgeted() {
        return budgeted;
    }

    public void setBudgeted(Integer budgeted) {
        this.budgeted = budgeted;
    }

    public Double getWage() {
        return wage;
    }

    public void setWage(Double wage) {
        this.wage = wage;
    }

    public Boolean getDayOff() {
        return dayOff == null ? false : dayOff;
    }

    public void setDayOff(Boolean dayOff) {
        this.dayOff = dayOff;
    }

    public Boolean getHoliday() {
        return holiday == null ? false : holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }

    public Integer getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Integer paidTime) {
        if (paidTime > leave) {
            throw new RuntimeException("Paid time cannot be more than leave time!");
        }
        this.paidTime = paidTime;
    }

    public Integer getFromAnnualLeaveTime() {
        return fromAnnualLeaveTime;
    }

    public void setFromAnnualLeaveTime(Integer fromAnnualLeaveTime) {
        this.fromAnnualLeaveTime = fromAnnualLeaveTime;
    }

    public Boolean getHolidayFromAnnualLeave() {
        return holidayFromAnnualLeave;
    }

    public void setHolidayFromAnnualLeave(Boolean holidayFromAnnualLeave) {
        this.holidayFromAnnualLeave = holidayFromAnnualLeave;
    }
}
