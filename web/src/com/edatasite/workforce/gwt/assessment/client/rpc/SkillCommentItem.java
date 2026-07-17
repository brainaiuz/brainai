package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * User: Ilhombek
 * Date: 12/10/12
 * Time: 6:08 PM
 */
public class SkillCommentItem implements IsSerializable {

    private Date createdDate;

    private String employeeComment;
    private String reviewerComment;

    private Date lastUpdateTime;
    private Integer skillID;
    private boolean typeSkill;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getEmployeeComment() {
        return employeeComment;
    }

    public void setEmployeeComment(String employeeComment) {
        this.employeeComment = employeeComment;
    }

    public String getReviewerComment() {
        return reviewerComment;
    }

    public void setReviewerComment(String reviewerComment) {
        this.reviewerComment = reviewerComment;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getSkillID() {
        return skillID;
    }

    public void setSkillID(Integer skillID) {
        this.skillID = skillID;
    }

    public boolean isTypeSkill() {
        return typeSkill;
    }

    public void setTypeSkill(boolean typeSkill) {
        this.typeSkill = typeSkill;
    }
}