package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilsh0d Madrahimov
 * This class for Timesheet API Syc from Excel plugin.
 */
public class TaskMiniTO implements IsSerializable {
    Integer id;
    String name;
    String number;
    String description;
    TimesheetEntryTO timesheetEntry;

    public TaskMiniTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TimesheetEntryTO getTimesheetEntry() {
        return timesheetEntry;
    }

    public void setTimesheetEntry(TimesheetEntryTO timesheetEntry) {
        this.timesheetEntry = timesheetEntry;
    }
}
