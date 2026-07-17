package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class CalendarItems implements IsSerializable {

    private CalendarItemRpc[] yearly;
    private CalendarItemRpc[] monthly;
    private CalendarItemRpc[] weekly;
    private CalendarItemRpc[] unAva;
    private CalendarItemRpc[] offDays;
    private CalendarItemRpc[] defaultHolidayDays;
    private CalendarItemRpc[] leaveReqByMoney;
    private CalendarItemRpc[] pendingLeaves;
    private ArrayList<Integer> daysToCountAsLeave;
    private boolean exceptionalTimeSlot;

    public CalendarItemRpc[] getYearly() {
        return yearly;
    }

    public void setYearly(CalendarItemRpc[] yearly) {
        this.yearly = yearly;
    }

    public CalendarItemRpc[] getMonthly() {
        return monthly;
    }

    public void setMonthly(CalendarItemRpc[] monthly) {
        this.monthly = monthly;
    }

    public CalendarItemRpc[] getWeekly() {
        return weekly;
    }

    public void setWeekly(CalendarItemRpc[] weekly) {
        this.weekly = weekly;
    }

    public CalendarItemRpc[] getUnAva() {
        return unAva;
    }

    public void setUnAva(CalendarItemRpc[] unAva) {
        this.unAva = unAva;
    }

    public CalendarItemRpc[] getOffDays() {
        return offDays;
    }

    public void setOffDays(CalendarItemRpc[] offDays) {
        this.offDays = offDays;
    }

    public CalendarItemRpc[] getDefaultHolidayDays() {
        return defaultHolidayDays;
    }

    public void setDefaultHolidayDays(CalendarItemRpc[] defaultHolidayDays) {
        this.defaultHolidayDays = defaultHolidayDays;
    }

    public CalendarItemRpc[] getLeaveReqByMoney() {
        return leaveReqByMoney;
    }

    public void setLeaveReqByMoney(CalendarItemRpc[] leaveReqByMoney) {
        this.leaveReqByMoney = leaveReqByMoney;
    }

    public CalendarItemRpc[] getPendingLeaves() {
        return pendingLeaves;
    }

    public void setPendingLeaves(CalendarItemRpc[] pendingLeaves) {
        this.pendingLeaves = pendingLeaves;
    }

    public ArrayList<Integer> getDaysToCountAsLeave() {
        return daysToCountAsLeave;
    }

    public void setDaysToCountAsLeave(ArrayList<Integer> daysToCountAsLeave) {
        this.daysToCountAsLeave = daysToCountAsLeave;
    }

    public boolean isExceptionalTimeSlot() {
        return exceptionalTimeSlot;
    }

    public void setExceptionalTimeSlot(boolean exceptionalTimeSlot) {
        this.exceptionalTimeSlot = exceptionalTimeSlot;
    }
}
