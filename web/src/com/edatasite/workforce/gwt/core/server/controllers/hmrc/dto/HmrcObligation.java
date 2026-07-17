package com.edatasite.workforce.gwt.core.server.controllers.hmrc.dto;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class HmrcObligation extends ResponseData {

    private String start;// "2017-01-01"
    private String end; //"2017-03-31"
    private String due;// //"2017-05-07"
    private String status;// //"F"
    private String periodKey;// "18A1"
    private String received;// "2017-05-06"

    public HmrcObligation() {
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPeriodKey() {
        return periodKey;
    }

    public void setPeriodKey(String periodKey) {
        this.periodKey = periodKey;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }
}
