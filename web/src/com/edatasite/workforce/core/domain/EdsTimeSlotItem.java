package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsAudit;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.enums.HistoryType;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * User: Ula
 * Date: Jul 16, 2007
 * Time: 12:36:03 PM
 */
//@Indexed
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "timeslotitem")
public class EdsTimeSlotItem extends EdsAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "coffeeEnd")
    private Integer coffeeEnd;
    @Column(name = "coffeeStart")
    private Integer coffeeStart;

    private Integer day;
    private Integer dayOfWeek;

    @Column(name = "exceptionalDate")
    private Date exceptionalDate; //exceptional date for exceptional cases

    private Integer endTime;
    private Integer startTime;

    @Column(name = "lunchEnd")
    private Integer lunchEnd;
    @Column(name = "lunchStart")
    private Integer lunchStart;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslotid")
    private EdsTimeSlot timeSlot;

    @Column(name = "type")
    private String type = "WEEKLY";

    @Override
    public HistoryType getHistoryType() {
        return HistoryType.TIMESLOT_ITEM;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCoffeeEnd() {
        return coffeeEnd;
    }

    public void setCoffeeEnd(Integer coffeeEnd) {
        if (!Objects.equals(this.coffeeEnd, coffeeEnd)) {
            addHistoryChange("Coffee End", this.coffeeEnd + "", coffeeEnd + "");
        }
        this.coffeeEnd = coffeeEnd;
    }

    public Integer getCoffeeStart() {
        return coffeeStart;
    }

    public void setCoffeeStart(Integer coffeeStart) {
        if (!Objects.equals(this.coffeeStart, coffeeStart)) {
            addHistoryChange("Coffee Start", this.coffeeStart + "", coffeeStart + "");
        }
        this.coffeeStart = coffeeStart;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        if (!Objects.equals(this.day, day)) {
            addHistoryChange("Day", this.day + "", day + "");
        }
        this.day = day;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Date getExceptionalDate() {
        return exceptionalDate;
    }

    public void setExceptionalDate(Date exceptionalDate) {
        if (!ServerUtils.equalsDate(this.exceptionalDate, exceptionalDate)) {
            addHistoryChange("Exceptional Date", this.exceptionalDate, exceptionalDate);
        }
        this.exceptionalDate = exceptionalDate;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        if (!Objects.equals(this.endTime, endTime)) {
            addHistoryChange("End Time", this.endTime + "", endTime + "");
        }
        this.endTime = endTime;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        if (!Objects.equals(this.startTime, startTime)) {
            addHistoryChange("Start Time", this.startTime + "", startTime + "");
        }
        this.startTime = startTime;
    }

    public Integer getLunchEnd() {
        return lunchEnd;
    }

    public void setLunchEnd(Integer lunchEnd) {
        if (!Objects.equals(this.lunchEnd, lunchEnd)) {
            addHistoryChange("Lunch Time", this.lunchEnd + "", lunchEnd + "");
        }
        this.lunchEnd = lunchEnd;
    }

    public Integer getLunchStart() {
        return lunchStart;
    }

    public void setLunchStart(Integer lunchStart) {
        if (!Objects.equals(this.lunchStart, lunchStart)) {
            addHistoryChange("Lunch Start", this.lunchStart + "", lunchStart + "");
        }
        this.lunchStart = lunchStart;
    }

    public EdsTimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(EdsTimeSlot timeSlot) {
        if (!ServerUtils.equalsEdsObject(this.timeSlot, timeSlot)) {
            addHistoryChange("Timeslot", this.timeSlot, timeSlot);
        }
        this.timeSlot = timeSlot;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMinutes() {
        return getTimeSlotItemMinutes(this);
        //return (getEndTime() - getStartTime()) - (getLunchEnd() - getLunchStart()) - (getCoffeeEnd() - getCoffeeStart());
    }

    /**
     *  Timeslot could be negative in same cases. For example:
     *  Work start date 09:00, Work end date: 18:00. Working hours 9:00
     *  Lunch start date 10:00, Lunch end date: 18:00. Lunch hours: 8
     *  Coffee break start date 13:00, Coffee break end date: 14:30. Coffee break hours: 1:30
     * In this case, timeslot = 9 - 8 - 1:30 = -30. Negative. So the negative amount will be appeared on timesheet footer data
     * If timeslot start date and end date are zero, it means day off.
     * @param edsTimeSlotItem
     * @return TimeSlotItemMinutes
     */
    public static Integer getTimeSlotItemMinutes(EdsTimeSlotItem edsTimeSlotItem) {
        if (edsTimeSlotItem.getStartTime().equals(edsTimeSlotItem.getEndTime()) && edsTimeSlotItem.getStartTime() == 0) {
            return 0;
        }
        Date date = new Date();
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(date);
        startDate.set(Calendar.HOUR_OF_DAY, edsTimeSlotItem.getStartTime() / 60);
        startDate.set(Calendar.MINUTE, edsTimeSlotItem.getStartTime() % 60);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(date);
        endDate.set(Calendar.HOUR_OF_DAY, edsTimeSlotItem.getEndTime() / 60);
        endDate.set(Calendar.MINUTE, edsTimeSlotItem.getEndTime() % 60);
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MILLISECOND, 0);

        Calendar lunchStartDate = Calendar.getInstance();
        lunchStartDate.setTime(date);
        lunchStartDate.set(Calendar.HOUR_OF_DAY, edsTimeSlotItem.getLunchStart() / 60);
        lunchStartDate.set(Calendar.MINUTE, edsTimeSlotItem.getLunchStart() % 60);
        lunchStartDate.set(Calendar.SECOND, 0);
        lunchStartDate.set(Calendar.MILLISECOND, 0);

        Calendar lunchEndDate = Calendar.getInstance();
        lunchEndDate.setTime(date);
        lunchEndDate.set(Calendar.HOUR_OF_DAY, edsTimeSlotItem.getLunchEnd() / 60);
        lunchEndDate.set(Calendar.MINUTE, edsTimeSlotItem.getLunchEnd() % 60);
        lunchEndDate.set(Calendar.SECOND, 0);
        lunchEndDate.set(Calendar.MILLISECOND, 0);

        Calendar coffeeBreakStartDate = Calendar.getInstance();
        coffeeBreakStartDate.setTime(date);
        coffeeBreakStartDate.set(Calendar.HOUR_OF_DAY, edsTimeSlotItem.getCoffeeStart() / 60);
        coffeeBreakStartDate.set(Calendar.MINUTE, edsTimeSlotItem.getCoffeeStart() % 60);
        coffeeBreakStartDate.set(Calendar.SECOND, 0);
        coffeeBreakStartDate.set(Calendar.MILLISECOND, 0);

        Calendar coffeeBreakEndDate = Calendar.getInstance();
        coffeeBreakEndDate.setTime(date);
        coffeeBreakEndDate.set(Calendar.HOUR_OF_DAY, edsTimeSlotItem.getCoffeeEnd() / 60);
        coffeeBreakEndDate.set(Calendar.MINUTE, edsTimeSlotItem.getCoffeeEnd() % 60);
        coffeeBreakEndDate.set(Calendar.SECOND, 0);
        coffeeBreakEndDate.set(Calendar.MILLISECOND, 0);

        int timeSlotItemMinutes;
        //if lunch time includes coffee break time, do not consider coffee break time. eg: Lunch time: 9:00 - 18:00 and coffee break time 14:00 - 14:30
        if (lunchStartDate.before(coffeeBreakStartDate) && lunchEndDate.after(coffeeBreakEndDate)) {
            timeSlotItemMinutes = (edsTimeSlotItem.getEndTime() - edsTimeSlotItem.getStartTime()) - (edsTimeSlotItem.getLunchEnd() - edsTimeSlotItem.getLunchStart());
        } else if (coffeeBreakStartDate.before(lunchStartDate) && coffeeBreakEndDate.after(lunchEndDate)) {//if coffee break time includes lunk time, do not consider lunk time. eg: Coffee break time: 9:00 - 18:00 and lunch time 12:00 - 13:00
            timeSlotItemMinutes = (edsTimeSlotItem.getEndTime() - edsTimeSlotItem.getStartTime()) - (edsTimeSlotItem.getCoffeeEnd() - edsTimeSlotItem.getCoffeeStart());
        } else {
            timeSlotItemMinutes = (edsTimeSlotItem.getEndTime() - edsTimeSlotItem.getStartTime()) - (edsTimeSlotItem.getLunchEnd() - edsTimeSlotItem.getLunchStart()) - (edsTimeSlotItem.getCoffeeEnd() - edsTimeSlotItem.getCoffeeStart());
        }
        return timeSlotItemMinutes;
    }
}
