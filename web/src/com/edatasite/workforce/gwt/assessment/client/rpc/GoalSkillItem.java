package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Sher
 * Date: 8/7/12
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoalSkillItem implements IsSerializable {

    private Integer objectId;
    private Double weight = 0d;
    private Double givenScore = 0.d;
    private Boolean showSlider;

    public GoalSkillItem() {
    }

    public GoalSkillItem(Integer objectId, Double weight, Double givenScore, Boolean showSlider) {
        this.objectId = objectId;
        this.weight = weight;
        this.givenScore = givenScore;
        this.showSlider = showSlider;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getGivenScore() {
        return givenScore;
    }

    public void setGivenScore(Double givenScore) {
        this.givenScore = givenScore;
    }

    public Boolean getShowSlider() {
        return showSlider;
    }

    public void setShowSlider(Boolean showSlider) {
        this.showSlider = showSlider;
    }
}
