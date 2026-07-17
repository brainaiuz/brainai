package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Mar 12, 2009
 * Time: 1:16:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "courseAttachment")
public class EdsCourseAttachment extends EdsUpload implements ObjectHistory {
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseid")
    private EdsCourse course;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    private Date lastUpdateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    private Date creationTime;

    @Column(name = "isDeleted")
    private Boolean deleted = false;

    private String deletedBy;

    private Long size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EdsCourse getCourse() {
        return course;
    }

    public void setCourse(EdsCourse course) {
        this.course = course;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
