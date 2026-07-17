package com.edatasite.workforce.core.domain.rbac;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.Permission;
import com.edatasite.workforce.core.domain.assessment.EdsAssessment;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * User: Abdulaziz
 * Date: Nov 17, 2009
 * Time: 9:12:42 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "assessmentindexrbac", uniqueConstraints = @UniqueConstraint(columnNames = {"assessmentid", "userid"/*, "companyid"*/}))
public class EdsAssessmentIndexRbac extends EdsObject implements Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @ManyToOne
    @JoinColumn(name = "assessmentid")
    private EdsAssessment assessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeassessmentid")
    private EdsEmployeeAssessment employeeassessment;

    @Column
    private int permission;

    public EdsAssessment getAssessment() {
        return assessment;
    }

    public void setAssessment(EdsAssessment assessment) {
        this.assessment = assessment;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public EdsEmployeeAssessment getEmployeeassessment() {
        return employeeassessment;
    }

    public void setEmployeeassessment(EdsEmployeeAssessment employeeassessment) {
        this.employeeassessment = employeeassessment;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
