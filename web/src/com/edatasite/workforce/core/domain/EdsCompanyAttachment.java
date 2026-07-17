package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "companyattachment")
public class EdsCompanyAttachment extends EdsUpload {

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser createdBy;

    private Date lastUpdateTime;

    @Column(name = "isDeleted")
    private Boolean deleted = false;

    private String deletedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logoTypeId")
    private EdsReference logoType;

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public EdsUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(EdsUser createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsReference getLogoType() {
        return logoType;
    }

    public void setLogoType(EdsReference logoType) {
        this.logoType = logoType;
    }
}
