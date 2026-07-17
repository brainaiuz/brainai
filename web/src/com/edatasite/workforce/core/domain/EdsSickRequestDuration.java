package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;
import java.util.Date;

/**
 * User: Ilhombek
 * Date: 4/18/13
 * Time: 6:26 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "SickRequestDuration",
    indexes = {@Index(columnList = "date", name = "sickrequest_duration_date_idx")})
public class EdsSickRequestDuration extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Temporal(TemporalType.DATE)
    private Date date;//references EdsDate from public schema

    @Column(nullable = false, columnDefinition = "int4 default 0")
    private Integer durationTime = 0;//leave request duration minutes

    @Column(name = "day", columnDefinition = "Decimal(10,2) default 0.00")
    private Double day = 0d;

    @Column(name = "lastYearDay", columnDefinition = "Decimal(10,2) default 0.00")
    private Double lastYearDay = 0d;

    @Column(name = "lastyearminutes", nullable = false, columnDefinition = "int4 default 0")
    private Integer lastYearMinutes = 0;

    private Integer sickRequestID;//references EdsSickRequest from private schema

    private Integer periodID;//references EdsSickRequest from private schema

    private String dayType;//references EdsSickRequest from private schema

    @Column(name = "isPaid", columnDefinition = "boolean default true")
    private boolean isPaid;

    @Column(nullable = false, columnDefinition = "int4 default 0")
    private Integer timeSlot = 0;//time slot minutes for the day

    @Column(name = "dayOff", columnDefinition = "boolean default false")
    private Boolean dayOff = false;//whether the day is day off

    @Column(name = "holiday", columnDefinition = "boolean default false")
    private Boolean holiday = false;//whether the day is holiday

    @Column(name = "holidayfromannualleave", columnDefinition = "boolean default false")
    private Boolean holidayFromAnnualLeave = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
    }

    public Double getDay() {
        if (day == null) {
            day = 0d;
        }
        return day;
    }

    public Double getLastYearDay() {
        if (lastYearDay == null) {
            lastYearDay = 0d;
        }
        return lastYearDay;
    }

    public void setLastYearDay(Double lastYearDay) {
        this.lastYearDay = lastYearDay;
    }

    public void setDay(Double day) {
        this.day = day;
    }

    public Integer getSickRequestID() {
        return sickRequestID;
    }

    public void setSickRequestID(Integer sickRequestID) {
        this.sickRequestID = sickRequestID;
    }

    public Integer getPeriodID() {
        return periodID;
    }

    public void setPeriodID(Integer periodID) {
        this.periodID = periodID;
    }

    public String getDayType() {
        return dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public Integer getLastYearMinutes() {
        return lastYearMinutes;
    }

    public void setLastYearMinutes(Integer lastYearMinutes) {
        this.lastYearMinutes = lastYearMinutes;
    }

    public Integer getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(Integer timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Boolean getDayOff() {
        return dayOff;
    }

    public void setDayOff(Boolean dayOff) {
        this.dayOff = dayOff;
    }

    public Boolean getHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }

    public Boolean getHolidayFromAnnualLeave() {
        return holidayFromAnnualLeave;
    }

    public void setHolidayFromAnnualLeave(Boolean holidayFromAnnualLeave) {
        this.holidayFromAnnualLeave = holidayFromAnnualLeave;
    }
}
