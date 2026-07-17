package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * User: Abror Abdukadirov
 * Date: 20.06.2019 18:52
 */
public class TimeSheetReportTO implements IsSerializable {
    private String emloyeeName;
    private Integer totalHours = 0;
    private Boolean leaveRequest = false;
    private String leaveRequestPeriod;
    private ArrayList<TimeSheetReportItemTO> items = new ArrayList<>();

    public String getEmloyeeName() {
        return emloyeeName;
    }

    public void setEmloyeeName(String emloyeeName) {
        this.emloyeeName = emloyeeName;
    }

    public void addTotalHours(Integer hours) {
        if (hours == null) {
            return;
        }
        this.totalHours += hours;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }

    public Boolean getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(Boolean leaveRequest) {
        this.leaveRequest = leaveRequest;
    }

    public String getLeaveRequestPeriod() {
        return leaveRequestPeriod;
    }

    public void setLeaveRequestPeriod(String leaveRequestPeriod) {
        this.leaveRequestPeriod = leaveRequestPeriod;
    }

    public ArrayList<TimeSheetReportItemTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<TimeSheetReportItemTO> items) {
        this.items = items;
    }
}
