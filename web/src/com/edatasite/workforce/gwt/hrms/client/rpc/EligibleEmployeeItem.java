package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.assessment.client.rpc.BonusSettingsItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.ScoreItem;
import com.edatasite.workforce.gwt.core.client.rpc.Key;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Sher
 * Date: 7/23/12
 * Time: 3:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class EligibleEmployeeItem implements IsSerializable, Key {

    public static final String EB_EMPLOYEE_NAME = "employeeName";
    public static final String EB_OVERALL_SCORE = "overallScore";
    public static final String EB_OVERALL_GRADE = "overallGrade";
    public static final String EB_BASIC_SALARY = "basicSalary";
    public static final String EB_RECIEVED_BONUS = "recievedBonus";
    public static final String EB_REDISTRIBUTED_BONUS_AMOUNT = "redistributedBonusAmount";

    private Integer objectId;
    private Integer employeeId;
    private String employeeName;
//    private Integer assessmentId;
    private Double bonusAmount;
    private Double redistributedBonusAmount;
    private Double basicSalary;
    private Double averageScore;
    private String averageGrade;
    private ScoreItem scoreItem;
    private BonusSettingsItem bonusSettingsItem;
    private Integer bonusDistributionId;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public Double getBasicSalary() {
        if (basicSalary == null) {
            this.basicSalary = 0d;
        }
        return basicSalary;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public String getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(String averageGrade) {
        this.averageGrade = averageGrade;
    }

//    public Integer getAssessmentId() {
//        return assessmentId;
//    }
//
//    public void setAssessmentId(Integer assessmentId) {
//        this.assessmentId = assessmentId;
//    }

    public ScoreItem getScoreItem() {
        return scoreItem;
    }

    public void setScoreItem(ScoreItem scoreItem) {
        this.scoreItem = scoreItem;
    }

    public Double getRedistributedBonusAmount() {
        return redistributedBonusAmount;
    }

    public void setRedistributedBonusAmount(Double redistributedBonusAmount) {
        this.redistributedBonusAmount = redistributedBonusAmount;
    }

    public BonusSettingsItem getBonusSettingsItem() {
        return bonusSettingsItem;
    }

    public void setBonusSettingsItem(BonusSettingsItem bonusSettingsItem) {
        this.bonusSettingsItem = bonusSettingsItem;
    }

    public Integer getBonusDistributionId() {
        return bonusDistributionId;
    }

    public void setBonusDistributionId(Integer bonusDistributionId) {
        this.bonusDistributionId = bonusDistributionId;
    }

    @Override
    public String getKey() {
        return "" + employeeId;
    }
}
