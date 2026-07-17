package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class SkillAssessmentElemsStruct implements IsSerializable {
    private SkillAssessmentElem[] elems;
    private Integer initiatorID;
    private String initiator;
    private String employeeName;
    private String assessmentType;
    private Integer reviewerID;
    private String reviewerName;
    private String reviewerType;
    private String status;
    private String templateName;
    private String departmentName;
    private String companyName;
    private int skillWeigthPercent;
    private int goalWeigthPercent;
    private boolean currentUserReviewer = false;
    private boolean currentUserEmployee = false;
    private boolean currentUserInitiator = false;
    private boolean currentUserLastUpdater = false;
    private boolean currentUserSupervisor = false;

    private Integer employeeId;
    private String employeeStatus;
    private Boolean weightable = false;

    private int employeePong;
    private int managerPong;
    private double calculatedAverage;
    private Integer lastUpdaterID;
    private String lastUpdaterName;
    private Integer validityPeriodId;
    private BonusSettingsItem bonusSettingsItem;
    private String generalComment;
    private Date assessmentDate;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getEmployeePong() {
        return employeePong;
    }

    public void setEmployeePong(int employeePong) {
        this.employeePong = employeePong;
    }

    public int getManagerPong() {
        return managerPong;
    }

    public void setManagerPong(int managerPong) {
        this.managerPong = managerPong;
    }

    public double getCalculatedAverage() {
        return calculatedAverage;
    }

    public void setCalculatedAverage(double calculatedAverage) {
        this.calculatedAverage = calculatedAverage;
    }

    //for 360 degree assessment only
    private boolean turn;

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public Integer getReviewerID() {
        return reviewerID;
    }

    public void setReviewerID(Integer reviewerID) {
        this.reviewerID = reviewerID;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewerType() {
        return reviewerType;
    }

    public void setReviewerType(String reviewerType) {
        this.reviewerType = reviewerType;
    }

    public SkillAssessmentElem[] getElems() {
        return elems;
    }

    public void setElems(SkillAssessmentElem[] elems) {
        this.elems = elems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getInitiatorID() {
        return initiatorID;
    }

    public void setInitiatorID(Integer initiatorID) {
        this.initiatorID = initiatorID;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public Boolean isWeightable() {
        return weightable;
    }

    public void setWeightable(Boolean weightable) {
        this.weightable = weightable;
    }

    public int getSkillWeigthPercent() {
        return skillWeigthPercent;
    }

    public void setSkillWeigthPercent(int skillWeigthPercent) {
        this.skillWeigthPercent = skillWeigthPercent;
    }

    public int getGoalWeigthPercent() {
        return goalWeigthPercent;
    }

    public void setGoalWeigthPercent(int goalWeigthPercent) {
        this.goalWeigthPercent = goalWeigthPercent;
    }

    public Integer getLastUpdaterID() {
        return lastUpdaterID;
    }

    public void setLastUpdaterID(Integer lastUpdaterID) {
        this.lastUpdaterID = lastUpdaterID;
    }

    public String getLastUpdaterName() {
        return lastUpdaterName;
    }

    public void setLastUpdaterName(String lastUpdaterName) {
        this.lastUpdaterName = lastUpdaterName;
    }

    public Integer getValidityPeriodId() {
        return validityPeriodId;
    }

    public void setValidityPeriodId(Integer validityPeriodId) {
        this.validityPeriodId = validityPeriodId;
    }

    public BonusSettingsItem getBonusSettingsItem() {
        return bonusSettingsItem;
    }

    public void setBonusSettingsItem(BonusSettingsItem bonusSettingsItem) {
        this.bonusSettingsItem = bonusSettingsItem;
    }

    public String getGeneralComment() {
        return generalComment;
    }

    public void setGeneralComment(String generalComment) {
        this.generalComment = generalComment;
    }

    public boolean isCurrentUserReviewer() {
        return currentUserReviewer;
    }

    public void setCurrentUserReviewer(boolean is) {
        this.currentUserReviewer = is;
    }

    public boolean isCurrentUserEmployee() {
        return currentUserEmployee;
    }

    public void setCurrentUserEmployee(boolean is) {
        this.currentUserEmployee = is;
    }

    public boolean isCurrentUserInitiator() {
        return currentUserInitiator;
    }

    public void setCurrentUserInitiator(boolean is) {
        this.currentUserInitiator = is;
    }

    public boolean isCurrentUserLastUpdater() {
        return currentUserLastUpdater;
    }

    public void setCurrentUserLastUpdater(boolean is) {
        this.currentUserLastUpdater = is;
    }

    public boolean isCurrentUserSupervisor() {
        return currentUserSupervisor;
    }

    public void setCurrentUserSupervisor(boolean currentUserSupervisor) {
        this.currentUserSupervisor = currentUserSupervisor;
    }

    public Date getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(Date assessmentDate) {
        this.assessmentDate = assessmentDate;
    }
}