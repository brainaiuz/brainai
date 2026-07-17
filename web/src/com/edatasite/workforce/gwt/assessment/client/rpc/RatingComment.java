package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RatingComment implements IsSerializable {
    private String name;
    private Double rating;
    private String comment;
    private String teamName;
    private String status;
    private Boolean rateable;
    private Boolean isAnonymous;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Boolean isRateable() {
        return rateable;
    }

    public void setRateable(Boolean rateable) {
        this.rateable = rateable;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmployeeTeam() {
        return teamName;
    }

    public void setEmployeeTeam(String employeeTeam) {
        this.teamName = employeeTeam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

}
