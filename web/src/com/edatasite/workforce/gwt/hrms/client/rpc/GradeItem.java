package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: unni
 * Date: Oct 20, 2009
 * Time: 9:39:12 PM
 */
public class GradeItem implements IsSerializable {

    public static final String NAME = "grade";
    public static final String DESCRIPTION = "description";
    public static final String ACTION = "action";

    private Integer objectId;
    private String gradeCode;
    private String gradeLevel;
    private String description;
    private Integer positionId;
    private Double hourlyMin;
    private Double weeklyMin;
    private Double monthlyMin;
    private Double annualMin;
    private Double hourlyMid;
    private Double monthlyMid;
    private Double annualMid;
    private Double hourlyMax;
    private Double monthlyMax;
    private Double annualMax;

    private CurrencyItem currencyItem;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Double getHourlyMin() {
        return hourlyMin;
    }

    public void setHourlyMin(Double hourlyMin) {
        this.hourlyMin = hourlyMin;
    }

    public Double getWeeklyMin() {
        return weeklyMin;
    }

    public void setWeeklyMin(Double weeklyMin) {
        this.weeklyMin = weeklyMin;
    }

    public Double getMonthlyMin() {
        return monthlyMin;
    }

    public void setMonthlyMin(Double monthlyMin) {
        this.monthlyMin = monthlyMin;
    }

    public Double getAnnualMin() {
        return annualMin;
    }

    public void setAnnualMin(Double annualMin) {
        this.annualMin = annualMin;
    }

    public Double getAnnualMax() {
        return annualMax;
    }

    public void setAnnualMax(Double annualMax) {
        this.annualMax = annualMax;
    }

    public Double getHourlyMid() {
        return hourlyMid;
    }

    public void setHourlyMid(Double hourlyMid) {
        this.hourlyMid = hourlyMid;
    }

    public Double getMonthlyMid() {
        return monthlyMid;
    }

    public void setMonthlyMid(Double monthlyMid) {
        this.monthlyMid = monthlyMid;
    }

    public Double getHourlyMax() {
        return hourlyMax;
    }

    public Double getAnnualMid() {
        return annualMid;
    }

    public void setAnnualMid(Double annualMid) {
        this.annualMid = annualMid;
    }

    public void setHourlyMax(Double hourlyMax) {
        this.hourlyMax = hourlyMax;
    }

    public Double getMonthlyMax() {
        return monthlyMax;
    }

    public void setMonthlyMax(Double monthlyMax) {
        this.monthlyMax = monthlyMax;
    }

    public CurrencyItem getCurrencyItem() {
        return currencyItem;
    }

    public void setCurrencyItem(CurrencyItem currencyItem) {
        this.currencyItem = currencyItem;
    }
}