package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class AssessmentListItem implements IsSerializable {
    private String employeeName;
    private String assessmentName;
    private Integer assessmentId;
    private String templateName;
    private Date initiationDate;
    private String reviewerName;
    private String initiatorName;
    private String assessmentType;
    private String encryptedID;
    private String status;
    private String collaborator;
    private String collaboratorType;
    private boolean turn;
    private boolean simple;
    private String link;
    private CommentRateItem[] clientComments;
    private CommentRateItem[] managerCoomments;
    private CommentRateItem[] peerComments;

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

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public CommentRateItem[] getClientComments() {
        return clientComments;
    }

    public void setClientComments(CommentRateItem[] clientComments) {
        this.clientComments = clientComments;
    }

    public CommentRateItem[] getManagerCoomments() {
        return managerCoomments;
    }

    public void setManagerCoomments(CommentRateItem[] managerCoomments) {
        this.managerCoomments = managerCoomments;
    }

    public CommentRateItem[] getPeerComments() {
        return peerComments;
    }

    public void setPeerComments(CommentRateItem[] peerComments) {
        this.peerComments = peerComments;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean isSimple() {
        return simple;
    }

    public void setSimple(boolean simple) {
        this.simple = simple;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

