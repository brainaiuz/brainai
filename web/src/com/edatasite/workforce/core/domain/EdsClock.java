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
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Aug 11, 2010
 * Time: 6:45:42 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "clock")
public class EdsClock extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    // this is business object: crm task, crm event, ...
    @Column(name = "relation")
    private Integer relation;

    // this is business object id: crm task id, crm event id, ... id
    @Column(name = "busObjectId")
    private Integer busObjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId")
    private EdsUser owner;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "estimate")
    private Integer estimate;

    @Column(name = "actualTime")
    private Integer actualTime;

    @Column(name = "comment", length = 1000)
    private String comment;

    @Column(name = "started")
    private boolean started = false;

    @Column(name = "reset")
    private boolean reset = true;

    @Column(name = "cumulativeTime")
    private Integer cumulativeTime;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public Integer getBusObjectId() {
        return busObjectId;
    }

    public void setBusObjectId(Integer busObjectId) {
        this.busObjectId = busObjectId;
    }

    public EdsUser getOwner() {
        return owner;
    }

    public void setOwner(EdsUser owner) {
        this.owner = owner;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getEstimate() {
        return estimate;
    }

    public void setEstimate(Integer estimate) {
        this.estimate = estimate;
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActual(Integer actual) {
        this.actualTime = actual;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public Integer getCumulativeTime() {
        return cumulativeTime;
    }

    public void setCumulativeTime(Integer cumulativeTime) {
        this.cumulativeTime = cumulativeTime;
    }
}
