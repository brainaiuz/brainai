package com.edatasite.workforce.core.domain.assessment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillCommentItem;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;

import javax.persistence.*;
import java.util.Date;

/**
 * User: Ilhombek
 * Date: 12/12/12
 * Time: 2:34 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "ratingComment")
public class EdsRatingComment extends EdsObject implements ObjectHistory {

    public static final String RATING_COMMENT_TYPE_GOAL = "RATING_COMMENT_TYPE_GOAL";    //rating comment type for goal
    public static final String RATING_COMMENT_TYPE_SKILL = "RATING_COMMENT_TYPE_SKILL";  //rating comment type for skill

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "employeeComment", length = 1000)
    private String employeeComment;

    @Column(name = "reviewerComment", length = 1000)
    private String reviewerComment;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goalRatingID")
    private EdsGoalRating goalRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skillRatingID")
    private EdsSkillRating skillRating;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public void setCreator(EdsUser value) {
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

    @Override
    public void setUpdater(EdsUser user) {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EdsGoalRating getGoalRating() {
        return goalRating;
    }

    public void setGoalRating(EdsGoalRating goalRating) {
        this.goalRating = goalRating;
    }

    public EdsSkillRating getSkillRating() {
        return skillRating;
    }

    public void setSkillRating(EdsSkillRating skillRating) {
        this.skillRating = skillRating;
    }

    //
    public SkillCommentItem getRPC() {
        SkillCommentItem commentItem = new SkillCommentItem();
        commentItem.setEmployeeComment(getEmployeeComment());
        commentItem.setReviewerComment(getReviewerComment());
        commentItem.setCreatedDate(getCreationTime());
        commentItem.setLastUpdateTime(getLastUpdateTime());
        commentItem.setTypeSkill(RATING_COMMENT_TYPE_SKILL.equals(getType()));//rating comment (for skill or goal)
        return commentItem;
    }
}
