package com.edatasite.workforce.gwt.core.client.ui.laborPeriod;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.Date;

public class MultiLeaveDTO implements IsSerializable, Serializable {

    private Integer periodId;
    private Integer sickID;
    private String sickRequestType;
    private Date sickRequestStartDate;
    private Date sickRequestEndDate;
    private Date sickRequestRecallDate;
    private int sickRequestDuration;
    private int sickRequestLeftDays;
    private int minLeaveDays;
    private String laborPeriod;

    public Integer getSickID() {
        return sickID;
    }

    public void setSickID(Integer sickID) {
        this.sickID = sickID;
    }

    public Date getSickRequestRecallDate() {
        return sickRequestRecallDate;
    }

    public void setSickRequestRecallDate(Date sickRequestRecallDate) {
        this.sickRequestRecallDate = sickRequestRecallDate;
    }

    public Integer getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }

    public String getSickRequestType() {
        return sickRequestType;
    }

    public void setSickRequestType(String sickRequestType) {
        this.sickRequestType = sickRequestType;
    }

    public Date getSickRequestStartDate() {
        return sickRequestStartDate;
    }

    public void setSickRequestStartDate(Date sickRequestStartDate) {
        this.sickRequestStartDate = sickRequestStartDate;
    }

    public Date getSickRequestEndDate() {
        return sickRequestEndDate;
    }

    public void setSickRequestEndDate(Date sickRequestEndDate) {
        this.sickRequestEndDate = sickRequestEndDate;
    }

    public int getSickRequestDuration() {
        return sickRequestDuration;
    }

    public void setSickRequestDuration(int sickRequestDuration) {
        this.sickRequestDuration = sickRequestDuration;
    }

    public int getSickRequestLeftDays() {
        return sickRequestLeftDays;
    }

    public void setSickRequestLeftDays(int sickRequestLeftDays) {
        this.sickRequestLeftDays = sickRequestLeftDays;
    }

    public int getMinLeaveDays() {
        return minLeaveDays;
    }

    public void setMinLeaveDays(int minLeaveDays) {
        this.minLeaveDays = minLeaveDays;
    }

    public String getLaborPeriod() {
        return laborPeriod;
    }

    public void setLaborPeriod(String laborPeriod) {
        this.laborPeriod = laborPeriod;
    }
}
