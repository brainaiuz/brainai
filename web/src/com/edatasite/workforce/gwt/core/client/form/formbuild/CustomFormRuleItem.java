package com.edatasite.workforce.gwt.core.client.form.formbuild;

import java.io.Serializable;

/**
 * Created by Muhammadrizo 07/12/2021
 */
public class CustomFormRuleItem implements Serializable {
    private String conditionType;
    private String range;
    private Long startDate;
    private Long endDate;
    private Integer conditionValue;

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Integer getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(Integer conditionValue) {
        this.conditionValue = conditionValue;
    }
}
