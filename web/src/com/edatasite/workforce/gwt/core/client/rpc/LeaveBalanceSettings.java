package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

public class LeaveBalanceSettings implements IsSerializable {
    private Integer id;
    private SelectItem leaveReason;
    private BigDecimal days;
    private Integer from;
    private Integer to;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SelectItem getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(SelectItem leaveReason) {
        this.leaveReason = leaveReason;
    }

    public BigDecimal getDays() {
        return days;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }
}
