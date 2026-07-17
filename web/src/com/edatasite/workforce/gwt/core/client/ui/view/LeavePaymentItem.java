package com.edatasite.workforce.gwt.core.client.ui.view;


import com.google.gwt.user.client.rpc.IsSerializable;

public class LeavePaymentItem implements IsSerializable {

    private Integer leaveMinutes;
    private Integer leavePaymentYear;
    private Integer leaveDays;
    public LeavePaymentItem() {

    }
    public LeavePaymentItem(Integer leavePaymentYear, Integer leaveDays) {
        this.leavePaymentYear = leavePaymentYear;
        this.leaveDays = leaveDays;
    }

    public Integer getLeaveMinutes() {
        return leaveMinutes;
    }

    public void setLeaveMinutes(Integer leaveMinutes) {
        this.leaveMinutes = leaveMinutes;
    }

    public Integer getLeavePaymentYear() {
        return leavePaymentYear;
    }

    public void setLeavePaymentYear(Integer leavePaymentYear) {
        this.leavePaymentYear = leavePaymentYear;
    }

    public Integer getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Integer leaveDays) {
        this.leaveDays = leaveDays;
    }
}
