package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AssessmentListData implements IsSerializable {

    private Double averageScore = 0d;
    private Double highestScore = 0d;
    private Double lowestScore = 0d;

    private InProgressAssessmentListElem[] assessments;
    private InProgressAssessmentListElem[] yourPendingReviewAssessments;
    private Boolean teamLeader;

    public AssessmentListData() {
    }

    public InProgressAssessmentListElem[] getAssessments() {
        return assessments;
    }

    public void setAssessments(InProgressAssessmentListElem[] assessments) {
        this.assessments = assessments;
    }

    public InProgressAssessmentListElem[] getYourPendingReviewAssessments() {
        return yourPendingReviewAssessments;
    }

    public void setYourPendingReviewAssessments(InProgressAssessmentListElem[] yourPendingReviewAssessments) {
        this.yourPendingReviewAssessments = yourPendingReviewAssessments;
    }

    public Boolean getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(Boolean teamLeader) {
        this.teamLeader = teamLeader;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public Double getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(Double highestScore) {
        this.highestScore = highestScore;
    }

    public Double getLowestScore() {
        return lowestScore;
    }

    public void setLowestScore(Double lowestScore) {
        this.lowestScore = lowestScore;
    }
}
