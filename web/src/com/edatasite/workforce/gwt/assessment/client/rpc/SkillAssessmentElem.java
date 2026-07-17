package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

public class SkillAssessmentElem implements IsSerializable {

    private Integer skillId;
    private Integer skillRatingId;
    private String skillName;
    private String employeeGrade;
    private String managersGrade;
    private Double raiting;
    private Double weight;
    private Double employeeRating;
    private String skillDescription;

    private String employeesComment;
    private String reviewersComment;

    private String savedAsDraftComment;

    private List<SkillCommentItem> ratingCommentItems;
    private SkillCommentItem lastRatingComment;

    private Boolean showRadio;
    private Boolean turn;

    public SkillAssessmentElem() {
    }

    //old

    /**
     * @deprecated
     */
    @Deprecated
    public SkillAssessmentElem(Integer skillRatingId, String skillDescription, String skillName, Double raiting, Double employeeRating,
                               String employeesComment, String reviewersComment, Boolean showRadio, Integer skillId) {
        this.skillRatingId = skillRatingId;
        this.skillDescription = skillDescription;
        this.skillName = skillName;
        this.raiting = raiting;
        this.employeeRating = employeeRating;
        this.employeesComment = employeesComment;
        this.reviewersComment = reviewersComment;
        this.showRadio = showRadio;
        this.skillId = skillId;
    }

    //new
    public SkillAssessmentElem(Integer skillRatingId, String skillDescription, String skillName, Double raiting, Double employeeRating,
                               String employeesComment, String reviewersComment,
                               List<SkillCommentItem> ratingComments, String savedAsDraftComment,
                               Boolean showRadio, Integer skillId, String employeeGrade, String managersGrade) {
        this.skillRatingId = skillRatingId;
        this.skillDescription = skillDescription;
        this.skillName = skillName;
        this.raiting = raiting;
        this.employeeRating = employeeRating;

        this.employeesComment = employeesComment;
        this.reviewersComment = reviewersComment;

        this.ratingCommentItems = ratingComments;
        this.savedAsDraftComment = savedAsDraftComment;

        this.showRadio = showRadio;
        this.skillId = skillId;
        this.employeeGrade = employeeGrade;
        this.managersGrade = managersGrade;
    }

    //old

    /**
     * @deprecated
     */
    @Deprecated
    public SkillAssessmentElem(Integer skillRatingId, String skillDescription, String skillName, Double raiting, Double employeeRating, Double weight,
                               String employeesComment, String reviewersComment, Boolean showRadio, Integer skillId) {
        this.skillRatingId = skillRatingId;
        this.skillDescription = skillDescription;
        this.employeeRating = employeeRating;
        this.skillName = skillName;
        this.skillId = skillId;
        this.raiting = raiting;
        this.employeesComment = employeesComment;
        this.reviewersComment = reviewersComment;
        this.showRadio = showRadio;
        this.weight = weight;
    }

    //new
    public SkillAssessmentElem(Integer skillRatingId, String skillDescription, String skillName, Double raiting, Double employeeRating, Double weight,
                               String employeesComment, String reviewersComment,
                               List<SkillCommentItem> ratingComments, String savedAsDraftComment,
                               Boolean showRadio, Integer skillId, String employeeGrade, String managersGrade) {
        this.skillRatingId = skillRatingId;
        this.skillDescription = skillDescription;
        this.skillName = skillName;
        this.raiting = raiting;
        this.employeeRating = employeeRating;
        this.weight = weight;

        this.employeesComment = employeesComment;
        this.reviewersComment = reviewersComment;

        this.ratingCommentItems = ratingComments;
        this.savedAsDraftComment = savedAsDraftComment;

        this.showRadio = showRadio;
        this.skillId = skillId;
        this.employeeGrade = employeeGrade;
        this.managersGrade = managersGrade;
    }

    public List<SkillCommentItem> getRatingCommentItems() {
        return ratingCommentItems;
    }

    public SkillCommentItem getLastRatingComment() {
        return lastRatingComment;
    }

    public void setLastRatingComment(SkillCommentItem lastRatingComment) {
        this.lastRatingComment = lastRatingComment;
    }

    public Boolean isShowRadio() {
        return showRadio;
    }

    public void setShowRadio(Boolean showRadio) {
        this.showRadio = showRadio;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Double getRaiting() {
        return raiting;
    }

    public void setRaiting(Double raiting) {
        this.raiting = raiting;
    }

    public String getEmployeesComment() {
        return employeesComment;
    }

    public void setEmployeesComment(String employeesComment) {
        this.employeesComment = employeesComment;
    }

    public String getReviewersComment() {
        return reviewersComment;
    }

    public void setReviewersComment(String reviewersComment) {
        this.reviewersComment = reviewersComment;
    }

    public String getSavedAsDraftComment() {
        return savedAsDraftComment;
    }

    public void setSavedAsDraftComment(String savedAsDraftComment) {
        this.savedAsDraftComment = savedAsDraftComment;
    }

    public Integer getSkillRatingId() {
        return skillRatingId;
    }

    public void setSkillRatingId(Integer skillRatingId) {
        this.skillRatingId = skillRatingId;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    public Double getEmployeeRating() {
        return employeeRating;
    }

    public void setEmployeeRating(Double empoyeeRating) {
        this.employeeRating = empoyeeRating;
    }

    public Boolean isTurn() {
        return turn;
    }

    public void setTurn(Boolean turn) {
        this.turn = turn;
    }

    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getEmployeeGrade() {
        return employeeGrade;
    }

    public void setEmployeeGrade(String grade) {
        this.employeeGrade = grade;
    }

    public String getManagersGrade() {
        return managersGrade;
    }

    public void setManagersGrade(String managersGrade) {
        this.managersGrade = managersGrade;
    }
}