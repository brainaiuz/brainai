package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 30.03.11
 * Time: 14:05
 * To change this template use File | Settings | File Templates.
 */

public class RecurrenceLogItem implements IsSerializable {

    public static final String JOBNAME = "jobName";
    public static final String JOBTYPE = "jobType";
    public static final String CRONEXPRESSION = "cronExpression";
    public static final String NORMALFIRETIME = "normalFireTime";
    public static final String LATEFIRETIME = "lateFireTime";
    public static final String ISFIRED = "isFired";
    public static final String RECURRENCEID = "recurrenceID";
    public static final String COMPANYID = "companyID";
    public static final String DOWNTIMEFROM = "downTimeFrom";
    public static final String DOWNTIMETO = "downTimeTo";
    public static final String CATCHUP = "catchUp";
    public static final String LATERECCOUNT = "lateRecCount";

    // Recurrence history related fields
    private Integer objectID;
    private Integer recurrenceID;
    private Integer companyID;
    private String jobName;
    private String jobType;
    private String cronExpression;
    private Date normalFireTime;
    private Date lateFireTime;
    private Boolean isFired;

    // Server history related fields
    private Date downTimeFrom;
    private Date downTimeTo;
    private Boolean isCatchUp;
    private Integer lateRecurrenceCount;

    public RecurrenceLogItem() {

    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getRecurrenceID() {
        return recurrenceID;
    }

    public void setRecurrenceID(Integer recurrenceID) {
        this.recurrenceID = recurrenceID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Date getNormalFireTime() {
        return normalFireTime;
    }

    public void setNormalFireTime(Date normalFireTime) {
        this.normalFireTime = normalFireTime;
    }

    public Date getLateFireTime() {
        return lateFireTime;
    }

    public void setLateFireTime(Date lateFireTime) {
        this.lateFireTime = lateFireTime;
    }

    public Boolean getFired() {
        return isFired;
    }

    public void setFired(Boolean fired) {
        isFired = fired;
    }

    public Date getDownTimeFrom() {
        return downTimeFrom;
    }

    public void setDownTimeFrom(Date downTimeFrom) {
        this.downTimeFrom = downTimeFrom;
    }

    public Date getDownTimeTo() {
        return downTimeTo;
    }

    public void setDownTimeTo(Date downTimeTo) {
        this.downTimeTo = downTimeTo;
    }

    public Boolean getCatchUp() {
        return isCatchUp;
    }

    public void setCatchUp(Boolean catchUp) {
        isCatchUp = catchUp;
    }

    public Integer getLateRecurrenceCount() {
        return lateRecurrenceCount;
    }

    public void setLateRecurrenceCount(Integer lateRecurrenceCount) {
        this.lateRecurrenceCount = lateRecurrenceCount;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }
}
