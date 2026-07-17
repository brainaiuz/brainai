package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class AssessmentsListElem implements IsSerializable {
    public static final String ACTION = "action";
    public static final String ASSESSMENT_NAME = "employeeName";
    public static final String DEPARTMENT_NAME = "departmentName";
    public static final String TEMPLATE_NAME = "templateName";
    public static final String INITIATION_DATE = "initiationDate";
    public static final String REVIEWER_NAME = "reviewerName";
    public static final String INITIATOR_NAME = "initiatorName";
    public static final String ASSESSMENT_TYPE = "assessmentType";
    public static final String ASSESSMENT_STATUS = "assessmentStatus";
    public static final String PDF_VERSION = "pdfVersion";
    public static final String VALIDITY_PERIOD = "validityPeriod";
    public static final String OVERALL_SCORE = "overallScore";
    public static final String APPRAISAL_CYCLE = "appraisalCycle";
    public static final String COMMENTS = "comments";
    public static final String SUBMIT_FOR_APPROVAL_DATE = "submitForApprovalDate";
    public static final String OVERALL_GRADE = "overallGrade";

    private String assessmentName;
    private Integer assessmentId;
    private String employeeName;
    private String departmentName;
    private Integer employeeID;

    private String templateName;
    private Date initiationDate;
    private String reviewerName;
    private String initiatorName;
    private Integer initiatorID;
    private String assessmentType;
    private String assessmentTypeCode;
    private String encryptedID;
    private String status;
    private String statusCode;
    private Integer employeeAssessmentId;
    private boolean isCollaborator;

    private String validityPeriod;
    private Double overallScore;
    private int appraisalCycle;
    private Date submitForApprovalDate;
    private String comments;
    private BonusSettingsItem bonusSettingsItem;
    private String rejectionReasonComment;

    public String getEncryptedID() {
        return encryptedID;
    }

    public void setEncryptedID(String encryptedID) {
        this.encryptedID = encryptedID;
    }

    public Integer getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Integer assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
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

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public Integer getInitiatorID() {
        return initiatorID;
    }

    public void setInitiatorID(Integer initiatorID) {
        this.initiatorID = initiatorID;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String getAssessmentTypeCode() {
        return assessmentTypeCode;
    }

    public void setAssessmentTypeCode(String assessmentTypeCode) {
        this.assessmentTypeCode = assessmentTypeCode;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getEmployeeAssessmentId() {
        return employeeAssessmentId;
    }

    public void setEmployeeAssessmentId(Integer employeeAssessmentId) {
        this.employeeAssessmentId = employeeAssessmentId;
    }

    public boolean isCollaborator() {
        return isCollaborator;
    }

    public void setCollaborator(boolean collaborator) {
        isCollaborator = collaborator;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public Double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }

    public int getAppraisalCycle() {
        return appraisalCycle;
    }

    public void setAppraisalCycle(int appraisalCycle) {
        this.appraisalCycle = appraisalCycle;
    }

    public Date getSubmitForApprovalDate() {
        return submitForApprovalDate;
    }

    public void setSubmitForApprovalDate(Date submitForApprovalDate) {
        this.submitForApprovalDate = submitForApprovalDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public BonusSettingsItem getBonusSettingsItem() {
        return bonusSettingsItem;
    }

    public void setBonusSettingsItem(BonusSettingsItem bonusSettingsItem) {
        this.bonusSettingsItem = bonusSettingsItem;
    }

    public String getRejectionReasonComment() {
        return rejectionReasonComment;
    }

    public void setRejectionReasonComment(String rejectionReasonComment) {
        this.rejectionReasonComment = rejectionReasonComment;
    }
}