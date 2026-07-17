package com.edatasite.workforce.gwt.core.client.rpc;

/**
 * User: Sherali
 * Date: Oct 27, 2009
 * Time: 1:39:45 PM
 */
public class GoalAssigneeItem extends SelectItem {

    private Integer objectId;
    private Double weight = 0.d;
    private Double avaWeight = 100.d;
    private Integer monthlyTarget = 0;//tmp
    private Integer departmentId;
    private String departmentName;
    private boolean isAssignee = false;
    private boolean isMyself = false;
    private Double target = 0.d;
    private Double actual = 0.d;

    private Integer validityPeriodId;

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getMonthlyTarget() {
        return monthlyTarget;
    }

    public void setMonthlyTarget(Integer monthlyTarget) {
        this.monthlyTarget = monthlyTarget;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public boolean isAssignee() {
        return isAssignee;
    }

    public void setAssignee(boolean assignee) {
        isAssignee = assignee;
    }

    public boolean isMyself() {
        return isMyself;
    }

    public void setMyself(boolean myself) {
        isMyself = myself;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Double getAvaWeight() {
        if (isAssignee()) {
            return avaWeight + weight;
        }
        return avaWeight;
    }

    public void setAvaWeight(Double avaWeight) {
        this.avaWeight = avaWeight;
    }

    public Double getTarget() {
        return target;
    }

    public void setTarget(Double target) {
        this.target = target;
    }

    public Double getActual() {
        return actual;
    }

    public void setActual(Double actual) {
        this.actual = actual;
    }

    public Double getScore(String scoreCalculation) {
        Double score = null;
        if (actual != null && target != null && actual > 0 && target > 0) {
            if ("MAXIMIZE".equals(scoreCalculation)) {
                score = (actual / target) * 100;
            } else if ("MINIMIZE".equals(scoreCalculation)) {
                score = (target / actual) * 100;
            }
        }
        return score;
    }

    public Double getFinalScore(String scoreCalculation) {
        Double score = getScore(scoreCalculation);
        Double finalScore = null;
        if (score != null) {
            finalScore = (score * weight) / 100;
        }
        return finalScore;
    }

    public Integer getValidityPeriodId() {
        return validityPeriodId;
    }

    public void setValidityPeriodId(Integer validityPeriodId) {
        this.validityPeriodId = validityPeriodId;
    }
}