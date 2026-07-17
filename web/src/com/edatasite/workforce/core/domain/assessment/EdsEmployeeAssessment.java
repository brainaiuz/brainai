package com.edatasite.workforce.core.domain.assessment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employeeAssessment")
public class EdsEmployeeAssessment extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessmentId")
    private EdsAssessment assessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId")
    private EdsEmployee employee;

    ////////////////////360* related //////////////
    @ManyToOne
    @JoinColumn(name = "collaboratorId")
    private EdsUser collaborator;// The Assessment helper for 360* review

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeId")
    private EdsReference type;
    ///////////////////////////////////////////////

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "skillAssessmentId")
    private EdsSkillAssessment skillAssessment = new EdsSkillAssessment();

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "goalAssessmentId")
    private EdsGoalAssessment goalAssessment = new EdsGoalAssessment();

    @Column(name = "date")
    private Date date; // Set when assessment approved

    @Column(name = "averageType")
    private Integer averageType = 1; //1 - Computed average, 2 - Custom
    // average
    @Column(name = "customAverage")
    private Integer customAverage;

    @Column(name = "computedAverage")
    private Integer computedAverage; //Transient field not saved into

    // database!
    @Column(name = "comments")
    private String comments;

    @Column(name = "completed")
    private Boolean completed = false;
    @Column(name = "isAnonymous")
    private Boolean isAnonymous = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusId")
    private EdsReference status;//employee status

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contactId")
    private AssessmentContact contact;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "employeePing")
    private Integer employeePing = 0;

    @Column(name = "managerPong")
    private Integer managerPong = 0;

    public boolean isPeer() {
        return (type.getCode().equals(EdsAssessment.PEER));
    }

    public boolean isManager() {
        return (type.getCode().equals(EdsAssessment.CT_MANAGER));
    }

    public boolean isClient() {
        return (type.getCode().equals(EdsAssessment.CT_CLIENT));
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getAverageType() {
        return averageType;
    }

    public void setAverageType(Integer averageType) {
        this.averageType = averageType;
    }

    public Boolean getAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        isAnonymous = anonymous;
    }

    public Integer getCustomAverage() {
        return customAverage;
    }

    public void setCustomAverage(Integer customAverage) {
        this.customAverage = customAverage;
    }

    public Integer getComputedAverage() {
        return computedAverage;
    }

    public void setComputedAverage(Integer computedAverage) {
        this.computedAverage = computedAverage;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getSkillAverage() {
        if (Constants.RATED.equals(getStatus().getCode()) || Constants.APPROVED_BY_MANAGER.equals(getStatus().getCode()) || Constants.APPROVE.equals(getStatus().getCode())) {
            return getSkillAssessment().getAverage();
        }
        return null;
    }

    public Integer getAverage() {
        return getSkillAverage();
    }

    public EdsAssessment getAssessment() {
        return assessment;
    }

    public void setAssessment(EdsAssessment assessment) {
        this.assessment = assessment;
    }

    public AssessmentContact getContact() {
        return contact;
    }

    public void setContact(AssessmentContact contact) {
        this.contact = contact;
    }

    public Integer getEmployeePing() {
        return employeePing;
    }

    public void setEmployeePing(Integer employeePing) {
        this.employeePing = employeePing;
    }

    public Integer getManagerPong() {
        return managerPong;
    }

    public void setManagerPong(Integer managerPong) {
        this.managerPong = managerPong;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public EdsSkillAssessment getSkillAssessment() {
        return skillAssessment;
    }

    public void setSkillAssessment(EdsSkillAssessment skillAssessment) {
        this.skillAssessment = skillAssessment;
    }

    public EdsGoalAssessment getGoalAssessment() {
        return goalAssessment;
    }

    public void setGoalAssessment(EdsGoalAssessment goalAssessment) {
        this.goalAssessment = goalAssessment;
    }

    public EdsUser getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(EdsUser collaborator) {
        this.collaborator = collaborator;
    }

    public EdsReference getType() {
        return type;
    }

    public void setType(EdsReference type) {
        this.type = type;
    }

    public Boolean hasAccess(EdsUser user) {
        return (user.getCompany().getObjectID().equals(getEmployee().getCompany().getObjectID()) ||
                user.getCompany().getObjectID().equals(getAssessment().getReviewer().getCompany().getObjectID()));
    }

    public Double getGoalAssessmentCalculatedAverage() {//Goals overall score
        if (goalAssessment != null) {
            boolean isWeighTable = assessment.isWeightable() != null ? assessment.isWeightable() : false;
            if (isWeighTable) {
                return goalAssessment.getCalculatedAverageRate(assessment.isWeightable(), assessment.getGoalsWeightPercent());
            } else {
                return goalAssessment.getCalculatedAverageRate() != null ? goalAssessment.getCalculatedAverageRate() : 0;
            }
        }
        return 0d;
    }

    public Double getSkillAssessmentCalculatedAverage() {//Competencies overall score
        if (skillAssessment != null) {
            boolean isWeighTable = assessment.isWeightable() != null ? assessment.isWeightable() : false;
            if (isWeighTable) {
                return skillAssessment.getCalculatedAverageRate(assessment.isWeightable(), assessment.getGoalsWeightPercent());
            } else {
                return skillAssessment.getCalculatedAverageRate() != null ? skillAssessment.getCalculatedAverageRate() : 0;
            }
        }
        return 0d;
    }

    public Double getOverAllRate() {//Performance Appraisal Overall Score
        Double overallRate;
        boolean isWeighTable = assessment.isWeightable() != null ? assessment.isWeightable() : false;
        Double goalOverall = getGoalAssessmentCalculatedAverage();
        Double skillOverall = getSkillAssessmentCalculatedAverage();
        if (goalOverall == null) {
            goalOverall = 0d;
        }
        if (skillOverall == null) {
            skillOverall = 0d;
        }
        if (goalOverall > 0 && !isWeighTable) {
            overallRate = (skillOverall + goalOverall) / 2;
        } else if (goalOverall > 0 && isWeighTable) {
            overallRate = skillOverall + goalOverall;
        } else {
            overallRate = skillOverall;
        }
        return overallRate > 0 ? overallRate : null;
    }
}
