package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AttendanceReportLogItem implements IsSerializable {

    private String text;

    private String date;

    private Integer timeslotId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(Integer timeslotId) {
        this.timeslotId = timeslotId;
    }
}
