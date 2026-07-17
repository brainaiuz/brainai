package com.edatasite.workforce.gwt.availability.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.LRSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

public class LeaveSettingsItem implements IsSerializable{
    private LRSettingsItem lrSettingsItem;
    private SelectItem[] timeSlots;
    private Long currentDate;
    private boolean isProrata;

    public LRSettingsItem getLrSettingsItem() {
        return lrSettingsItem;
    }

    public void setLrSettingsItem(LRSettingsItem lrSettingsItem) {
        this.lrSettingsItem = lrSettingsItem;
    }

    public SelectItem[] getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(SelectItem[] timeSlots) {
        this.timeSlots = timeSlots;
    }

    public Long getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Long currentDate) {
        this.currentDate = currentDate;
    }

    public boolean isProrata() {
        return isProrata;
    }

    public void setProrata(boolean prorata) {
        isProrata = prorata;
    }
}
