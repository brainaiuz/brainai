/*
 * Copyright (c) 2022.
 * Bilol Boydedayev
 */

package com.edatasite.workforce.gwt.core.server.app.hmrc.dto;

public class VatObligationsDTO {
    private String start;
    private String end;
    private String due;
    private String status;
    private String periodKey;
    private String received;

    public VatObligationsDTO() {
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
