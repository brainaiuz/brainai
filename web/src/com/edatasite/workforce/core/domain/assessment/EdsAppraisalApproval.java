package com.edatasite.workforce.core.domain.assessment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsValidityPeriod;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Sher
 * Date: 8/16/12
 * Time: 12:11 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "appraisal_approval")
public class EdsAppraisalApproval extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    /**
     * Department
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private EdsDepartment department;

    /**
     * Validity Period
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id")
    private EdsValidityPeriod validityPeriod;

    /**
     * Date sent for approval
     */
    @Column(name = "date")
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater_id")
    private EdsUser updater;

    @Column(name = "lastModifiedDate")
    private Date lastModifiedDate;

    /**
     * Appraisal Approval Status
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private EdsReference status;

    @Column(name = "comments")
    private String comments;

    /**
     * Is this appraisal approval temporarily deleted?
     */
    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean deleted = false;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "appraisal_approval_assessment",
            joinColumns = {@JoinColumn(name = "appraisal_approval_id")},
            inverseJoinColumns = {@JoinColumn(name = "assessments_id")})
    private Set<EdsAssessment> assessments = new HashSet<>();

    /**
     * rejection reason comment
     */
    @Column(name = "rejectionReasonComment")
    private String rejectionReasonComment;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public EdsValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(EdsValidityPeriod validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<EdsAssessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(Set<EdsAssessment> assessments) {
        this.assessments = assessments;
    }

    public String getRejectionReasonComment() {
        return rejectionReasonComment;
    }

    public void setRejectionReasonComment(String rejectionReasonComment) {
        this.rejectionReasonComment = rejectionReasonComment;
    }
}
