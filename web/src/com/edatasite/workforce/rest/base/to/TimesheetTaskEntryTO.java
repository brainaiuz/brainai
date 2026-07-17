package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Umidbek.
 */
public class TimesheetTaskEntryTO implements IsSerializable {
    Integer minutes;
    String comment;
    Integer status;

    public TimesheetTaskEntryTO() {
    }

    public TimesheetTaskEntryTO(TimesheetDataItem item) {
        this.comment = item.getComment();
        this.minutes = item.getMinutes();
        this.status = item.getStatus();

        if (item.getMinutes() == 0 && item.getOldMinutes() != null) {
            this.minutes = item.getOldMinutes();
        }
    }

    public Integer getMinutes() {
        return minutes;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
