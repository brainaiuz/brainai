package com.edatasite.workforce.core.domain.goal;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsValidityPeriod;
import com.edatasite.workforce.core.domain.customfields.EdsGoalCustomFields;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 26, 2009
 * Time: 1:04:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "businessgoal")
public class EdsBusinessGoal extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "title")
    private String title;

    @Column(name = "description", length = 10000)
    private String description;

    @Column(name = "outcome", length = 10000)
    private String outcome;

    @Column(name = "fromDate")
    private Date fromDate;

    @Column(name = "toDate")
    private Date toDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private EdsReference status;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goalcustomfieldsid")
    private EdsGoalCustomFields goalCustomFields;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id")
    private EdsValidityPeriod validityPeriod;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(EdsValidityPeriod validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public EdsGoalCustomFields getGoalCustomFields() {
        return goalCustomFields;
    }

    public void setGoalCustomFields(EdsGoalCustomFields goalCustomFields) {
        this.goalCustomFields = goalCustomFields;
    }
}
