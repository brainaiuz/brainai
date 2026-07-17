package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

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

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "sick_request_period")
public class EdsSickRequestForPeriod extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Double days;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodId")
    private EdsLabourPeriod period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestId")
    private EdsSickRequest request;

    @Column(name = "taken_date")
    private Date takenDate;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "before_recalled")
    private Double durationBeforeRecalled;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public double getDurationBeforeRecalled() {
        return durationBeforeRecalled;
    }

    public void setDurationBeforeRecalled(double durationBeforeRecalled) {
        this.durationBeforeRecalled = durationBeforeRecalled;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Double getDays() {
        return days;
    }

    public void setDays(Double days) {
        this.days = days;
    }

    public EdsLabourPeriod getPeriod() {
        return period;
    }

    public void setPeriod(EdsLabourPeriod period) {
        this.period = period;
    }

    public EdsSickRequest getRequest() {
        return request;
    }

    public void setRequest(EdsSickRequest request) {
        this.request = request;
    }

    public Date getTakenDate() {
        return takenDate;
    }

    public void setTakenDate(Date takenDate) {
        this.takenDate = takenDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
