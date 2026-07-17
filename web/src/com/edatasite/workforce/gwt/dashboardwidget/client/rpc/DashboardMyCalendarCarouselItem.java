package com.edatasite.workforce.gwt.dashboardwidget.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * User: Abror Abdukadirov
 * Date: 25.05.2018 14:33
 */
public class DashboardMyCalendarCarouselItem implements IsSerializable {

    public static final String TASK = "TASK";
    public static final String MEETING = "MEETING";
    public static final String CALL = "CALL";

    private DateNonConvertable date;
    private int year;
    private int month;
    private int day;
    private int weekDay;
    private boolean isMeeting;
    private boolean isCall;
    private boolean isTask;

    private ArrayList<DashboardMyCalendarCarouselItem> days = new ArrayList<>();

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public boolean isMeeting() {
        return isMeeting;
    }

    public void setMeeting(boolean meeting) {
        isMeeting = meeting;
    }

    public boolean isCall() {
        return isCall;
    }

    public void setCall(boolean call) {
        isCall = call;
    }

    public boolean isTask() {
        return isTask;
    }

    public void setTask(boolean task) {
        isTask = task;
    }

    public ArrayList<DashboardMyCalendarCarouselItem> getDays() {
        if (days == null) {
            days = new ArrayList<>();
        }
        return days;
    }

    public void setDays(ArrayList<DashboardMyCalendarCarouselItem> days) {
        this.days = days;
    }
}
