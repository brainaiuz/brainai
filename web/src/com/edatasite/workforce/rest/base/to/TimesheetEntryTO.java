package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilsh0d Madrahimov
 * This class for Timesheet API Syc from Excel plugin.
 */

public class TimesheetEntryTO implements IsSerializable {
    Integer id;
    Integer minutes;
    String comment;
    String reference;

    public TimesheetEntryTO() {
    }

    public Integer getMinutes() {
        return minutes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
