package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

public class InitiatedAssessmentItem implements IsSerializable {

    private String encryptedId;
    private Integer id;

    private Integer employeeId;
    private Date date;
    private boolean saveCompetencies;
    private Integer templateID;
    private int competencyPercentINT;
    private int goalPercentINT;
    private boolean goToReview;
    private boolean sendEmailToEmployee;
    private boolean isWeightTable;
    private ArrayList<GoalSkillItem> skillItems;
    private ArrayList<GoalSkillItem> goalItems;
    private Integer validityPeriodId;
    private Integer reviewerId;
    private String status;
    private Date assessmentDate;
    private SelectItem employee;
    private SelectItem reviewer;
    private SkillAssessmentElem[] competencyElements;
    private boolean isFromShift = false;
    private Integer shiftItemId;


    public InitiatedAssessmentItem() {

    }

    public InitiatedAssessmentItem(Integer id, String encryptedId) {
        this.id = id;
        this.encryptedId = encryptedId;
    }

    public String getEncryptedId() {
        return encryptedId;
    }

    public void setEncryptedId(String encryptedId) {
        this.encryptedId = encryptedId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSaveCompetencies() {
        return saveCompetencies;
    }

    public void setSaveCompetencies(boolean saveCompetencies) {
        this.saveCompetencies = saveCompetencies;
    }

    public Integer getTemplateID() {
        return templateID;
    }

    public void setTemplateID(Integer templateID) {
        this.templateID = templateID;
    }

    public int getCompetencyPercentINT() {
        return competencyPercentINT;
    }

    public void setCompetencyPercentINT(int competencyPercentINT) {
        this.competencyPercentINT = competencyPercentINT;
    }

    public int getGoalPercentINT() {
        return goalPercentINT;
    }

    public void setGoalPercentINT(int goalPercentINT) {
        this.goalPercentINT = goalPercentINT;
    }

    public boolean isGoToReview() {
        return goToReview;
    }

    public void setGoToReview(boolean goToReview) {
        this.goToReview = goToReview;
    }

    public boolean isSendEmailToEmployee() {
        return sendEmailToEmployee;
    }

    public void setSendEmailToEmployee(boolean sendEmailToEmployee) {
        this.sendEmailToEmployee = sendEmailToEmployee;
    }

    public ArrayList<GoalSkillItem> getSkillItems() {
        return skillItems;
    }

    public void setSkillItems(ArrayList<GoalSkillItem> skillItems) {
        this.skillItems = skillItems;
    }

    public ArrayList<GoalSkillItem> getGoalItems() {
        return goalItems;
    }

    public void setGoalItems(ArrayList<GoalSkillItem> goalItems) {
        this.goalItems = goalItems;
    }

    public boolean isWeightTable() {
        return isWeightTable;
    }

    public void setWeightTable(boolean weightTable) {
        isWeightTable = weightTable;
    }

    public Integer getValidityPeriodId() {
        return validityPeriodId;
    }

    public void setValidityPeriodId(Integer validityPeriodId) {
        this.validityPeriodId = validityPeriodId;
    }

    public Integer getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Integer reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(Date assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public SelectItem getReviewer() {
        return reviewer;
    }

    public void setReviewer(SelectItem reviewer) {
        this.reviewer = reviewer;
    }

    public SkillAssessmentElem[] getCompetencyElements() {
        return competencyElements;
    }

    public void setCompetencyElements(SkillAssessmentElem[] competencyElements) {
        this.competencyElements = competencyElements;
    }

    public boolean isFromShift() {
        return isFromShift;
    }

    public void setFromShift(boolean fromShift) {
        isFromShift = fromShift;
    }

    public Integer getShiftItemId() {
        return shiftItemId;
    }

    public void setShiftItemId(Integer shiftItemId) {
        this.shiftItemId = shiftItemId;
    }
}
