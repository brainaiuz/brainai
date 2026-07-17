package com.edatasite.workforce.gwt.dashboardwidget.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.AvailableLeaveRequest;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class HolidayRPC implements IsSerializable {
    private ArrayList<AvailableLeaveRequest> holidays = new ArrayList<>();
    private DateNonConvertable serverDate;

    public ArrayList<AvailableLeaveRequest> getHolidays() {
        return holidays;
    }

    public void setHolidays(ArrayList<AvailableLeaveRequest> holidays) {
        this.holidays = holidays;
    }

    public DateNonConvertable getServerDate() {
        return serverDate;
    }

    public void setServerDate(DateNonConvertable serverDate) {
        this.serverDate = serverDate;
    }
}
