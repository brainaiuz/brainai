package com.edatasite.workforce.gwt.core.client.rpc.googlecalendar;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CalendarEventReminder implements IsSerializable, Comparable<CalendarEventReminder> {

    private String name;
    private Integer value;
    private Integer reminderTimes;

    public CalendarEventReminder() {
    }

    public CalendarEventReminder(String name, Integer reminderTimes) {
        this.name = name;
        this.reminderTimes = reminderTimes;
    }

    public CalendarEventReminder(Integer value, Integer reminderTimes) {
        this.value = value;
        this.reminderTimes = reminderTimes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getReminderTimes() {
        return reminderTimes;
    }

    public void setReminderTimes(Integer reminderTimes) {
        this.reminderTimes = reminderTimes;
    }

    @Override
    public int compareTo(CalendarEventReminder o) {
        return (this.reminderTimes).compareTo(o.getReminderTimes());
    }
}