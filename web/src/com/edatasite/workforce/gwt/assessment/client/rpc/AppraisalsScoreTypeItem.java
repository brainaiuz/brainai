package com.edatasite.workforce.gwt.assessment.client.rpc;

import java.io.Serializable;

public class AppraisalsScoreTypeItem implements Serializable {

    private Integer objectId;
    private String name;
    private String grade;
    private double rate;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
