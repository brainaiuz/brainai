package com.edatasite.workforce.core.domain.assessment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsValidityPeriod;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: izaynutdinov
 * Date: 19.06.2007
 * Time: 10:17:13
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "assessment")
public class EdsAssessment extends EdsObject {

    public static final String _COLLABORATOR_TYPE = "_COLLABORATOR_TYPE";
    public static final String PEER = "PEER";
    public static final String CT_MANAGER = "CT_MANAGER";
    public static final String SUBORDINATE = "SUBORDINATE";
    public static final String CT_CLIENT = "CT_CLIENT";
    public static final String _ASSESSMENT_TYPE = "_ASSESSMENT_TYPE";
    public static final String ASSESSMENT_360 = "ASSESSMENT_360";
    public static final String ASSESSMENT_SIMPLE = "ASSESSMENT_SIMPLE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "weightable")
    private Boolean weightable = false;

    @Column(name = "skillsWeightPercent")
    private Integer skillsWeightPercent = 50;

    @Column(name = "goalsWeightPercent")
    private Integer goalsWeightPercent = 50;

    @Column(name = "overallRate")
    private Double overallRate; //available only after when assessment is approved!  Its rate of collaborators and initiator rates average

    @Column(name = "employeeSelfRatesAverage")
    private Double employeeSelfRatesAverage; // belong only to Employee, available after employee RATED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "templateId")
    private EdsAssessmentTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewerId")
    private EdsUser reviewer;

    /*@ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "reviewerStatusId")
     private EdsReference reviewerStatus;//reviewer status*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiatorId")
    private EdsUser initiator;

    /*@ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "initiatorStatusId")
     private EdsReference initiatorStatus;//initiator status*/

    @Column(name = "inititateDate")
    private Date inititateDate;

    @Column(name = "assessmentDay")
    private Date assessmentDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lastUpdater")
    private EdsUser lastUpdater;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessmentType")
    private EdsReference assessmentType;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessmentId")
    private Set<EdsEmployeeAssessment> employeeAssessments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyEmployeeAssessmentId")
    private EdsEmployeeAssessment keyEmployeeAssessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id")
    private EdsValidityPeriod validityPeriod;

    @Column(name = "generalComment")
    @Type(type = "text")
    private String generalComment;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public boolean is360() {
        return ASSESSMENT_360.equals(assessmentType.getCode());
    }
    //In case the assessment will be more than 2

    public boolean isSimple() {
        return ASSESSMENT_SIMPLE.equals(assessmentType.getCode());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EdsAssessmentTemplate getTemplate() {
        return template;
    }

    public void setTemplate(EdsAssessmentTemplate template) {
        this.template = template;
    }

    public EdsUser getReviewer() {
        return reviewer;
    }

    public void setReviewer(EdsUser reviewer) {
        this.reviewer = reviewer;
    }

    /*public EdsReference getReviewerStatus() {
         return reviewerStatus;
     }

     public void setReviewerStatus(EdsReference reviewerStatus) {
         this.reviewerStatus = reviewerStatus;
     }*/

    public Date getInititateDate() {
        return inititateDate;
    }

    public void setInititateDate(Date inititateDate) {
        this.inititateDate = inititateDate;
    }

    public EdsUser getLastUpdater() {
        return lastUpdater;
    }

    public void setLastUpdater(EdsUser lastUpdater) {
        this.lastUpdater = lastUpdater;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Set<EdsEmployeeAssessment> getEmployeeAssessments() {
        return employeeAssessments;
    }

    public void setEmployeeAssessments(Set<EdsEmployeeAssessment> employeeAssessments) {
        this.employeeAssessments = employeeAssessments;
    }

    public void addEmployeeAssessment(EdsEmployeeAssessment employeeAssessment) {
//        setTemplate(employeeAssessment.getAssessment().getTemplate());
        employeeAssessment.setAssessment(this);
        // getEmployeeAssessments().add(employeeAssessment);
    }

    public Double getOverallRate() {
        return overallRate;
    }

    public void setOverallRate(Double overallRate) {
        this.overallRate = overallRate;
    }

    public Double getEmployeeSelfRatesAverage() {
        return employeeSelfRatesAverage;
    }

    public void setEmployeeSelfRatesAverage(Double employeeSelfRatesAverage) {
        this.employeeSelfRatesAverage = employeeSelfRatesAverage;
    }

    public EdsUser getInitiator() {
        return initiator;
    }

    public void setInitiator(EdsUser initiator) {
        this.initiator = initiator;
    }

    /*public EdsReference getInitiatorStatus() {
         return initiatorStatus;
     }

     public void setInitiatorStatus(EdsReference initiatorStatus) {
         this.initiatorStatus = initiatorStatus;
     }*/

    public EdsReference getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(EdsReference assessmenttype) {
        this.assessmentType = assessmenttype;
    }

    public EdsEmployeeAssessment getKeyEmployeeAssessment() {
        return keyEmployeeAssessment;
    }

    public void setKeyEmployeeAssessment(EdsEmployeeAssessment keyEmployeeAssessment) {
        this.keyEmployeeAssessment = keyEmployeeAssessment;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isWeightable() {
        return weightable;
    }

    public void setWeightable(Boolean weightable) {
        this.weightable = weightable;
    }

    public Integer getSkillsWeightPercent() {
        return skillsWeightPercent;
    }

    public void setSkillsWeightPercent(Integer skillsWeightPercent) {
        this.skillsWeightPercent = skillsWeightPercent;
    }

    public Integer getGoalsWeightPercent() {
        return goalsWeightPercent;
    }

    public void setGoalsWeightPercent(Integer goalsWeightPercent) {
        this.goalsWeightPercent = goalsWeightPercent;
    }

    public EdsValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(EdsValidityPeriod validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getGeneralComment() {
        return generalComment;
    }

    public void setGeneralComment(String generalComment) {
        this.generalComment = generalComment;
    }

    public Date getAssessmentDay() {
        return assessmentDay;
    }

    public void setAssessmentDay(Date assessmentDay) {
        this.assessmentDay = assessmentDay;
    }
}
