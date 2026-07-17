package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.BaseListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class InProgressAssessmentListElem extends BaseListItem implements IsSerializable {

    public static final String AVERAGE = "average";
    public static final String COLLABORATOR = "collaborator";
    public static final String COLLABORATOR_TYPE = "collabaratorType";
    public static final String INITIATION_DATE = "initiationDate";
    public static final String EMPLOYEE_NAME = "employeeName";
    public static final String EMPLOYEE_PONG = "employeePong";
    public static final String STATUS = "status";
    public static final String TEMPLATE_NAME = "templateName";

    private String assessmentType;
    private Integer average;
    private String collaborator;
    private String collaboratorType;
    private String collaboratorUsername;
    private Date completedDate;
    private Integer employeeAssessmentId;
    private Integer employeeId;
    private String employeeName;
    private Integer employeePong;
    private String employeeUsername;
    private Date initiationDate;
    private String initiatorName;
    private Integer initiatorID;
    private boolean isReviewer;
    private String manager;
    private Integer managerPong;
    private Double overAllRate;
    private Integer reviewerID;
    private String reviewerName;
    private String reviewerUsername;
    private Integer lastUpdaterID;
    private String lastUpdaterName;
    private String status;
    private String statusName;
    private Integer templateId;
    private String templateName;
    private boolean turn;
    private Integer userId;

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean isShowReview() {
        return employeeId.equals(userId) && (Constants.RATED.equals(status) || Constants.INITIATED.equals(status));
    }

    public boolean isShowRate() {
        return ((Constants.REVIEWED_BY_EMPLOYEE.equals(status) || Constants.RATED.equals(status)));
    }

    // && employeeAssessment.getReviewer().equals(getUser())
    public boolean isShowApprove() {
        return Constants.RATED.equals(status);
    }

    // && employeeAssessment.getReviewer().equals(getUser())
    public boolean isShowView() {
        return Constants.APPROVED_BY_MANAGER.equals(status) || Constants.APPROVED.equals(status);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEmployeeAssessmentId() {
        return employeeAssessmentId;
    }

    public void setEmployeeAssessmentId(Integer employeeAssessmentId) {
        this.employeeAssessmentId = employeeAssessmentId;
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

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Date getInitiationDate() {
        return initiationDate;
    }

    public void setInitiationDate(Date initiationDate) {
        this.initiationDate = initiationDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getEmployeePong() {
        return employeePong;
    }

    public void setEmployeePong(Integer employeePong) {
        this.employeePong = employeePong;
    }

    public Integer getManagerPong() {
        return managerPong;
    }

    public void setManagerPong(Integer managerPong) {
        this.managerPong = managerPong;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getAverage() {
        return average;
    }

    public void setAverage(Integer average) {
        this.average = average;
    }

    public Double getOverAllRate() {
        return overAllRate;
    }

    public void setOverAllRate(Double overAllRate) {
        this.overAllRate = overAllRate;
    }

    public String getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(String collaborator) {
        this.collaborator = collaborator;
    }

    public String getCollaboratorType() {
        return collaboratorType;
    }

    public void setCollaboratorType(String collaboratorType) {
        this.collaboratorType = collaboratorType;
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

    public Integer getInitiatorID() {
        return initiatorID;
    }

    public void setInitiatorID(Integer initiatorID) {
        this.initiatorID = initiatorID;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public void setEmployeeUsername(String employeeUsername) {
        this.employeeUsername = employeeUsername;
    }

    public String getCollaboratorUsername() {
        return collaboratorUsername;
    }

    public void setCollaboratorUsername(String collaboratorUsername) {
        this.collaboratorUsername = collaboratorUsername;
    }

    public String getReviewerUsername() {
        return reviewerUsername;
    }

    public void setReviewerUsername(String reviewerUsername) {
        this.reviewerUsername = reviewerUsername;
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

    public boolean isReviewer() {
        return isReviewer;
    }

    public void setReviewer(boolean reviewer) {
        isReviewer = reviewer;
    }

    @Override
    public Integer getRelationID() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getRelationType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getRelationName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
