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
 * User: Fatxulla
 * Date: Feb 11, 2014
 * Time: 12:10:42 AM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "clockhistory")
public class EdsClockHistory extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    // this is business object: crm task, crm case, ...
    @Column(name = "relation")
    private Integer relation;

    // this is business object id: crm task id, crm case id, ... id
    @Column(name = "busObjectId")
    private Integer busObjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId")
    private EdsUser owner;

    private Date date;

    @Column(name = "comment", length = 1000)
    private String comment;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getCumulativeTime() {
        return cumulativeTime;
    }

    public void setCumulativeTime(Integer cumulativeTime) {
        this.cumulativeTime = cumulativeTime;
    }
}
