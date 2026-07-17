package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.io.Serializable;
import java.util.HashMap;

public class ShiftItems implements Serializable {

    private Integer id;
    private Integer timeSlotId;
    private String timeSlotShortName;
    private String key;
    private SelectItem selectedGroup;
    private Integer rowId;
    private Double rate;
    private Integer assessmentId;
    private String positionName;
    private HashMap<String, SelectItem> dayAndSelectedTimeSlotS = new HashMap<>();
    private HashMap<String, Integer> dayAndShiftItemId = new HashMap<>();
    private HashMap<Integer, String> leaveDays = new HashMap<>();
    private HashMap<String, Double> rates = new HashMap<>();
    private HashMap<String, Integer> assesments = new HashMap<>();

    public SelectItem getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(SelectItem selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public HashMap<String, SelectItem> getDayAndSelectedTimeSlotS() {
        return dayAndSelectedTimeSlotS;
    }

    public void setDayAndSelectedTimeSlotS(HashMap<String, SelectItem> dayAndSelectedTimeSlotS) {
        this.dayAndSelectedTimeSlotS = dayAndSelectedTimeSlotS;
    }

    public HashMap<String, Integer> getDayAndShiftItemId() {
        return dayAndShiftItemId;
    }

    public void setDayAndShiftItemId(HashMap<String, Integer> dayAndShiftItemId) {
        this.dayAndShiftItemId = dayAndShiftItemId;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Integer timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public String getTimeSlotShortName() {
        return timeSlotShortName;
    }

    public void setTimeSlotShortName(String timeSlotShortName) {
        this.timeSlotShortName = timeSlotShortName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public HashMap<Integer, String> getLeaveDays() {
        return leaveDays;
    }
    public void setLeaveDays(HashMap<Integer, String> leaveDays) {
        this.leaveDays = leaveDays;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Integer assessmentId) {
        this.assessmentId = assessmentId;
    }

    public HashMap<String, Double> getRates() {
        return rates;
    }

    public void setRates(HashMap<String, Double> rates) {
        this.rates = rates;
    }

    public HashMap<String, Integer> getAssesments() {
        return assesments;
    }

    public void setAssesments(HashMap<String, Integer> assesments) {
        this.assesments = assesments;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
