package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 18, 2010
 * Time: 1:30:11 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "productpicture")
public class EdsProductPicture extends EdsUpload {

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private EdsItem product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser createdBy;

    private Date lastUpdateTime;

    private Date lastSyncTime;

    @Column(name = "isDeleted")
    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deletedBy")
    private EdsUser deletedBy;

    @Column(name = "defaultPicture")
    private Boolean defaultPicture;

    private Integer parentId;

    private Integer fileSizeType;

    private String magentoFile;

    public EdsUser getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(EdsUser deletedBy) {
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

    public Date getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(Date lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsItem getProduct() {
        return product;
    }

    public void setProduct(EdsItem product) {
        this.product = product;
    }

    public Boolean isDefaultPicture() {
        return defaultPicture != null ? defaultPicture : false;
    }

    public void setDefaultPicture(Boolean defaultPicture) {
        this.defaultPicture = defaultPicture;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getFileSizeType() {
        return fileSizeType;
    }

    public void setFileSizeType(Integer fileSizeType) {
        this.fileSizeType = fileSizeType;
    }

    public String getMagentoFile() {
        return magentoFile;
    }

    public void setMagentoFile(String magentoFile) {
        this.magentoFile = magentoFile;
    }
}
