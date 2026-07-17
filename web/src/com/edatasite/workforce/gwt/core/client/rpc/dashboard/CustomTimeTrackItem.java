package com.edatasite.workforce.gwt.core.client.rpc.dashboard;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: leo
 * Date: 4/4/12
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomTimeTrackItem implements IsSerializable {
    private Integer timeTrackId;
    private String startTime;
    private String endTime;
    private Date startDate;
    private Date endDate;

    public CustomTimeTrackItem() {
        this.timeTrackId = 0;
        this.startTime = "";
        this.endTime = "";
    }

    public CustomTimeTrackItem(Integer timeTrackId, String startTime, String endTime) {
        this.timeTrackId = timeTrackId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public CustomTimeTrackItem(Integer timeTrackId, Date endDate, Date startDate) {
        this.timeTrackId = timeTrackId;
        this.endDate = endDate;
        this.startDate = startDate;
    }

    public Integer getTimeTrackId() {
        return timeTrackId;
    }

    public void setTimeTrackId(Integer timeTrackId) {
        this.timeTrackId = timeTrackId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}

