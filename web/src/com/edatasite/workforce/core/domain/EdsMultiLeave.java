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
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "multi_leave")
public class EdsMultiLeave extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "labor_period")
    private EdsLabourPeriod laborPeriod;

    @OneToOne(fetch = FetchType.LAZY)
    private EdsSickRequest childSickRequest;

    @Column(name = "sick_request_type")
    private String sickRequestType;

    @Column(name = "sick_request_duration")
    private Double sickRequestDuration;

    @Column(name = "before_recalled")
    private Double durationBeforeRecalled;

    @Column(name = "modified_date")
    private Date modifiedDate;


    public Double getDurationBeforeRecalled() {
        return durationBeforeRecalled;
    }

    public void setDurationBeforeRecalled(Double durationBeforeRecalled) {
        this.durationBeforeRecalled = durationBeforeRecalled;
    }

    public EdsSickRequest getChildSickRequest() {
        return childSickRequest;
    }

    public void setChildSickRequest(EdsSickRequest childSickRequest) {
        this.childSickRequest = childSickRequest;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

//    public EdsSickRequestForPeriod getPeriod() {
//        return period;
//    }
//
//    public void setPeriod(EdsSickRequestForPeriod labourPeriod) {
//        this.period = labourPeriod;
//    }

    public String getSickRequestType() {
        return sickRequestType;
    }

    public void setSickRequestType(String sickRequestType) {
        this.sickRequestType = sickRequestType;
    }

    public Double getSickRequestDuration() {
        return sickRequestDuration;
    }

    public void setSickRequestDuration(Double sickRequestDuration) {
        this.sickRequestDuration = sickRequestDuration;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public EdsLabourPeriod getLaborPeriod() {
        return laborPeriod;
    }

    public void setLaborPeriod(EdsLabourPeriod laborPeriod) {
        this.laborPeriod = laborPeriod;
    }
}
