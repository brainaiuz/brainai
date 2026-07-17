package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AssessmentRatingsComments implements IsSerializable {

    private Integer skillID;
    private String skillName;
    private String skillDescription;
    private Integer keySkillRatingId;
    private Double employeeRating;
    private Double managerRating;

    private String employeeComment;
    private String managerComment;

    /*private SkillCommentItem lastRatingCommentItem;
    private ArrayList<SkillCommentItem> ratingCommentItems;*/

    private Boolean rateable;
    private double calculatedAverage;
    private RatingComment[] clients;
    private RatingComment[] managers;
    private RatingComment[] peers;
    //private float overalAverage;

    public double getCalculatedAverage() {
        return calculatedAverage;
    }

    public void setCalculatedAverage(double calculatedAverage) {
        this.calculatedAverage = calculatedAverage;
    }

    public Boolean getRateable() {
        return rateable;
    }

    public void setRateable(Boolean rateable) {
        this.rateable = rateable;
    }

    public Integer getSkillID() {
        return skillID;
    }

    public void setSkillID(Integer skillID) {
        this.skillID = skillID;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    public RatingComment[] getClients() {
        return clients;
    }

    public void setClients(RatingComment[] clients) {
        this.clients = clients;
    }

    public RatingComment[] getManagers() {
        return managers;
    }

    public void setManagers(RatingComment[] managers) {
        this.managers = managers;
    }

    public RatingComment[] getPeers() {
        return peers;
    }

    public void setPeers(RatingComment[] peers) {
        this.peers = peers;
    }

    public Integer getKeySkillRatingId() {
        return keySkillRatingId;
    }

    public void setKeySkillRatingId(Integer keySkillRatingId) {
        this.keySkillRatingId = keySkillRatingId;
    }

    public String getEmployeeComment() {
        return employeeComment;
    }

    public void setEmployeeComment(String employeeComment) {
        this.employeeComment = employeeComment;
    }

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }

    /*public SkillCommentItem getLastRatingCommentItem() {
        return lastRatingCommentItem;
    }

    public void setLastRatingCommentItem(SkillCommentItem lastRatingCommentItem) {
        this.lastRatingCommentItem = lastRatingCommentItem;
    }

    public ArrayList<SkillCommentItem> getRatingCommentItems() {
        return ratingCommentItems;
    }

    public void setRatingCommentItems(ArrayList<SkillCommentItem> ratingCommentItems) {
        this.ratingCommentItems = ratingCommentItems;
    }*/

    public Double getEmployeeRating() {
        return employeeRating;
    }

    public void setEmployeeRating(Double employeeRating) {
        this.employeeRating = employeeRating;
    }

    public Double getManagerRating() {
        return managerRating;
    }

    public void setManagerRating(Double managerRating) {
        this.managerRating = managerRating;
    }
}