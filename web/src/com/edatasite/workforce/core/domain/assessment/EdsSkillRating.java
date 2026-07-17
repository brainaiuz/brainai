package com.edatasite.workforce.core.domain.assessment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsSkill;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillCommentItem;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * User: izaynutdinov
 * Date: 19.06.2007
 * Time: 10:11:44
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "skillRating")
public class EdsSkillRating extends EdsObject {

    public static final String ASSESSMENT_RATINGS = "_ASSESSMENT_RATINGS";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goalId")
    private EdsSkill skill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skillAssessmentId")
    private EdsSkillAssessment skillAssessment;

    private Double weight;
    private Boolean showSlider = true;

    private Double rating = 0d;
    private Double employeeRating = 0d;

    private String reviewersComment;
    private String employeesComment;
    @Column(name = "employee_grade")
    private String employeeGrade;

    @Column(name = "manager_grade")
    private String managerGrade;

    @Column(name = "savedAsDraftComment", length = 1000)
    private String savedAsDraftComment;//bu har safar assessment ni statusi "saved as draft" bo'lganda yangilanib turadi!!!

    //employee via reviewer comments
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "skillRating")
    @Where(clause = "type = '" + EdsRatingComment.RATING_COMMENT_TYPE_SKILL + "' and skillRatingID is not null")
    @OrderBy(value = "creationTime desc ")
    private List<EdsRatingComment> ratingComments = new ArrayList<>();

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Double getWeight() {
        if (weight == null) {
            return 0d;
        }
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Boolean getShowSlider() {
        return showSlider;
    }

    public void setShowSlider(Boolean showSlider) {
        this.showSlider = showSlider;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReviewersComment() {
        return reviewersComment;
    }

    public void setReviewersComment(String reviewersComment) {
        this.reviewersComment = reviewersComment;
    }

    public String getEmployeesComment() {
        return employeesComment;
    }

    public void setEmployeesComment(String employeesComment) {
        this.employeesComment = employeesComment;
    }

    public String getSavedAsDraftComment() {
        return savedAsDraftComment;
    }

    public void setSavedAsDraftComment(String savedAsDraftComment) {
        this.savedAsDraftComment = savedAsDraftComment;
    }

    public List<EdsRatingComment> getRatingComments() {
        return ratingComments;
    }

    public void setRatingComments(List<EdsRatingComment> ratingComments) {
        this.ratingComments = ratingComments;
    }

    public EdsSkillAssessment getSkillAssessment() {
        return skillAssessment;
    }

    public void setSkillAssessment(EdsSkillAssessment skillAssessment) {
        this.skillAssessment = skillAssessment;
    }

    public EdsSkill getSkill() {
        return skill;
    }

    public void setSkill(EdsSkill skill) {
        this.skill = skill;
    }

    public Double getEmployeeRating() {
        return employeeRating;
    }

    public void setEmployeeRating(Double employeeRating) {
        this.employeeRating = employeeRating;
    }

    //skill employee via reviewer comments
    private ArrayList<SkillCommentItem> getAllRatingCommentItems() {
        ArrayList<SkillCommentItem> skillCommentItems = new ArrayList<>();
        List<EdsRatingComment> ratingCommentSet = getRatingComments();
        if (ratingCommentSet != null && ratingCommentSet.size() > 0) {
            for (EdsRatingComment ratingComment : ratingCommentSet) {
                SkillCommentItem rpc = ratingComment.getRPC();
                rpc.setSkillID(getSkill().getObjectID());
                skillCommentItems.add(rpc);
            }
        }
        return skillCommentItems;
    }

    //skill employee via reviewer comments
    public ArrayList<SkillCommentItem> getRatingCommentItems() {
        boolean isRemoveLastComment = true;
        boolean isRemoveLasReviewComment = isRemoveLastComment;

        ArrayList<SkillCommentItem> skillCommentItems = new ArrayList<>();
        List<EdsRatingComment> ratingCommentSet = getRatingComments();
        if (ratingCommentSet != null && ratingCommentSet.size() > 0) {
            for (EdsRatingComment aRatingCommentSet : ratingCommentSet) {
                if (isRemoveLastComment && aRatingCommentSet.getEmployeeComment() != null && !aRatingCommentSet.getEmployeeComment().isEmpty()) {
                    //merge comment
                    if (isRemoveLasReviewComment && aRatingCommentSet.getReviewerComment() != null && !aRatingCommentSet.getReviewerComment().isEmpty()) {
                        isRemoveLasReviewComment = false;
                    }
                    isRemoveLastComment = false;
                    continue;
                }

                if (isRemoveLasReviewComment && aRatingCommentSet.getReviewerComment() != null && !aRatingCommentSet.getReviewerComment().isEmpty()) {
                    isRemoveLasReviewComment = false;
                    continue;
                }

                SkillCommentItem rpc = aRatingCommentSet.getRPC();
                rpc.setSkillID(getSkill().getObjectID());
                skillCommentItems.add(rpc);
            }
        }

        return skillCommentItems;
    }

    //skill last employee via reviewer comment
    public SkillCommentItem getLastRatingComment() {
        ArrayList<SkillCommentItem> ratingCommentSet = getAllRatingCommentItems();
        if (ratingCommentSet != null && ratingCommentSet.size() > 0) {
            return ratingCommentSet.get(0);
        }
        return null;
    }

    //skill last employee comment
    public String getLastEmployeeComment() {
        String lastEmployeeComment = null;
        ArrayList<SkillCommentItem> ratingCommentItems = getAllRatingCommentItems();
        for (SkillCommentItem commentItem : ratingCommentItems) {
            if (commentItem.getEmployeeComment() != null && !"".equals(commentItem.getEmployeeComment())) {
                lastEmployeeComment = commentItem.getEmployeeComment();
                break;
            }
        }
        return lastEmployeeComment;
    }

    //skill last reviewer comment
    public String getLastReviewerComment() {
        String lastReviewerComment = null;
        ArrayList<SkillCommentItem> ratingCommentItems = getAllRatingCommentItems();
        for (SkillCommentItem commentItem : ratingCommentItems) {
            if (commentItem.getReviewerComment() != null && !"".equals(commentItem.getReviewerComment())) {
                lastReviewerComment = commentItem.getReviewerComment();
                break;
            }
        }
        return lastReviewerComment;
    }

    public String getEmployeeGrade() {
        return employeeGrade;
    }

    public void setEmployeeGrade(String grade) {
        this.employeeGrade = grade;
    }

    public String getManagerGrade() {
        return managerGrade;
    }

    public void setManagerGrade(String managerGrade) {
        this.managerGrade = managerGrade;
    }
}
