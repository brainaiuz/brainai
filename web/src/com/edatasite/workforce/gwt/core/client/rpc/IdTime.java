package com.edatasite.workforce.gwt.core.client.rpc;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * User: Admin
 * Date: 14.11.2008
 * Time: 20:52:45
 */
public class IdTime implements Serializable {
    private Integer id;
    private Integer time;       // set estimated time
    private Integer actualTime; // for import tasks from MS Project file : set actually time
    private BigDecimal taskAmount; //fixed amount funtion
    private Integer statusId;
    private Float percent;
    private String GoogleID;
    private String employeeName;
    private Boolean changeEstimateTime = false; //estimate time changed or not
    private Boolean startResourceCalculationForNewAssigneesFromToday; //start Resource calculation for new assignees from today

    public IdTime() {
    }

    public IdTime(Integer id, Integer time) {
        this.id = id;
        this.time = time;
    }

    public IdTime(Integer id, Integer time, Integer statusId) {
        this.id = id;
        this.time = time;
        this.statusId = statusId;
    }

    public IdTime(Integer id, Integer time, Float percent, Integer statusId) {
        this.id = id;
        this.time = time;
        this.statusId = statusId;
        this.percent = percent;
    }

    public IdTime(Integer id, Integer time, Float percent) {
        this.id = id;
        this.time = time;
        this.percent = percent;
    }

    public IdTime(Integer id, Integer time, Integer actualTime, Float percent) {
        this.id = id;
        this.time = time;
        this.actualTime = actualTime;
        this.percent = percent;
    }

    public IdTime(Integer id, Integer time, Integer actualTime, Float percent, Integer statusId) {
        this.id = id;
        this.time = time;
        this.actualTime = actualTime;
        this.percent = percent;
        this.statusId = statusId;
    }

    public IdTime(Integer id, Integer time, Integer actualTime, Float percent, Integer statusId, String GoogleId) {
        this.id = id;
        this.time = time;
        this.actualTime = actualTime;
        this.percent = percent;
        this.statusId = statusId;
        this.GoogleID = GoogleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public String getGoogleID() {
        return GoogleID;
    }

    public void setGoogleID(String googleID) {
        GoogleID = googleID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Boolean getChangeEstimateTime() {
        return changeEstimateTime;
    }

    public void setChangeEstimateTime(Boolean changeEstimateTime) {
        this.changeEstimateTime = changeEstimateTime;
    }

    public Boolean getStartResourceCalculationForNewAssigneesFromToday() {
        return startResourceCalculationForNewAssigneesFromToday;
    }

    public void setStartResourceCalculationForNewAssigneesFromToday(Boolean startResourceCalculationForNewAssigneesFromToday) {
        this.startResourceCalculationForNewAssigneesFromToday = startResourceCalculationForNewAssigneesFromToday;
    }

    public BigDecimal getTaskAmount() {
        return taskAmount;
    }

    public void setTaskAmount(BigDecimal taskAmount) {
        this.taskAmount = taskAmount;
    }

    @Override
    public boolean equals(Object obj) {
        IdTime it = (IdTime) obj;
        return (this.id != null && it.id != null && this.id.equals(it.id));
    }
}