package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * User: Sher
 * Date: 8/4/12
 * Time: 2:11 PM
 */
public class ScoreItem implements IsSerializable {

    private Integer objectId;
    private String name;
    private BigDecimal bonusDistribution;
    private BigDecimal fromScore;
    private BigDecimal toScore;
    private BigDecimal employeePercentage;
    private BigDecimal remainderBonusDistribution;

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

    public BigDecimal getBonusDistribution() {
        return bonusDistribution;
    }

    public void setBonusDistribution(BigDecimal bonusDistribution) {
        this.bonusDistribution = bonusDistribution;
    }

    public BigDecimal getFromScore() {
        return fromScore;
    }

    public void setFromScore(BigDecimal fromScore) {
        this.fromScore = fromScore;
    }

    public BigDecimal getToScore() {
        return toScore;
    }

    public void setToScore(BigDecimal toScore) {
        this.toScore = toScore;
    }

    public BigDecimal getEmployeePercentage() {
        return employeePercentage;
    }

    public void setEmployeePercentage(BigDecimal employeePercentage) {
        this.employeePercentage = employeePercentage;
    }

    public BigDecimal getRemainderBonusDistribution() {
        return remainderBonusDistribution;
    }

    public void setRemainderBonusDistribution(BigDecimal remainderBonusDistribution) {
        this.remainderBonusDistribution = remainderBonusDistribution;
    }
}