package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: Faxriddin Taslimov
 * Date: 18.03.2017
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "linktrackdate")
public class EdsLinkTrackDate extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Integer linktrackId;

    @Column(name = "trackDate")
    private Date trackDate;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public Integer getLinktrackId() {
        return linktrackId;
    }

    public void setLinktrackId(Integer linktrackId) {
        this.linktrackId = linktrackId;
    }

    public Date getTrackDate() {
        return trackDate;
    }

    public void setTrackDate(Date trackDate) {
        this.trackDate = trackDate;
    }
}
