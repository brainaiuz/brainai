package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Oct 26, 2010
 * Time: 3:21:45 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "newsattachments")
public class EdsNewsAttachments extends EdsUpload {
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private EdsUser createdBy;

    @Column(name = "created_time")
    private Date createdTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EdsUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(EdsUser createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
