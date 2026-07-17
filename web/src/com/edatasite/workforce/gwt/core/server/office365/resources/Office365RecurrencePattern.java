package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

import java.util.ArrayList;

/**
 * Created by umakarimov on 10/6/15.
 */
public class Office365RecurrencePattern extends Office365BaseResource {
    private Integer interval;
    private Integer month;
    private Integer dayOfMonth;

    private Index index;

    private DayOfWeek firstDayOfWeek;
    private ArrayList<DayOfWeek> daysOfWeek;

    private Type type;


    /**
     * @see https://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/recurrencepattern.htm
     */
    public Office365RecurrencePattern() {
    }

    public enum Type {
        daily, weekly, absoluteMonthly, relativeMonthly, absoluteYearly, relativeYearly
    }

    public enum Index {
        first, second, third, fourth, last
    }

    public enum DayOfWeek {
        sunday, monday, tuesday, wednesday, thursday, friday, saturday
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public DayOfWeek getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(DayOfWeek firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public ArrayList<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(ArrayList<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
