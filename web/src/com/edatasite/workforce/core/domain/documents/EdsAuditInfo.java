package com.edatasite.workforce.core.domain.documents;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.documents.client.rest.resource.AuditInfoResource;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 13.05.2010
 * Time: 21:09:03
 * To change this template use File | Settings | File Templates.
 */
@Embeddable
public class EdsAuditInfo implements Serializable {

    /**
     * The date the associated object was created. We can never change it after
     * creation.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false, columnDefinition = "timestamp DEFAULT current_timestamp")
    private Date creationDate;

    /**
     * The user that created the associated object. We can never change it after
     * creation.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    private EdsUser createdBy;
    @Column(insertable = false, updatable = false)
    private Integer createdBy_id;

    @Column(name = "isSuperUser", columnDefinition = "boolean default false")
    private boolean isSuperUser = false;

    /**
     * The date the associated object was modified.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "timestamp DEFAULT current_timestamp")
    private Date modificationDate;

    /**
     * The user that modified the associated object.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private EdsUser modifiedBy;

    /**
     * Retrieve the creation date for the associated object.
     *
     * @return the date of creation
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Modify the creation date for the associated object.
     *
     * @param _creationDate the new date of creation
     */
    public void setCreationDate(final Date _creationDate) {
        creationDate = _creationDate;
    }

    /**
     * Retrieve the user that created the associated object.
     *
     * @return the user that created the associated object
     */
    public EdsUser getCreatedBy() {
        return createdBy;
    }

    /**
     * Modify the user that created the associated object.
     *
     * @param _createdBy the new user that created the associated object
     */
    public void setCreatedBy(final EdsUser _createdBy) {
        createdBy = _createdBy;
    }

    /**
     * Retrieve the modification date for the associated object.
     *
     * @return the date of modification
     */
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * Modify the modification date for the associated object.
     *
     * @param _modificationDate the new date of modification
     */
    public void setModificationDate(final Date _modificationDate) {
        modificationDate = _modificationDate;
    }

    /**
     * Retrieve the user that modified the associated object.
     *
     * @return the user that modified the associated object
     */
    public EdsUser getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Modify the user that modified the associated object.
     *
     * @param _modifiedBy the new user that modified the associated object
     */
    public void setModifiedBy(final EdsUser _modifiedBy) {
        modifiedBy = _modifiedBy;
    }


    public boolean isSuperUser() {
        return isSuperUser;
    }

    public void setSuperUser(boolean superUser) {
        this.isSuperUser = superUser;
    }

    /**
     * Retrieve the Data Transfer Object for this audit info, that pays
     * particular attention to avoid any instances of java.sql.Timestamp in the
     * creation and modification dates.
     *
     * @return a DTO object with audit info
     */
    public AuditInfoResource getDTO() {
        final AuditInfoResource dto = new AuditInfoResource();
        dto.setCreatedBy(createdBy != null ? createdBy.getDTO() : null);
        dto.setCreationDate(creationDate != null ? new Date(creationDate.getTime()) : null);
        dto.setModificationDate(modificationDate != null ? new Date(modificationDate.getTime()) : null);
        dto.setModifiedBy(modifiedBy != null ? modifiedBy.getDTO() : null);
        dto.setSuperUser(isSuperUser());
        if (isSuperUser()) {
            dto.getModifiedBy().setName(Constants.defaultSupportName);
            dto.getCreatedBy().setName(Constants.defaultSupportName);
        }
        return dto;
    }

}
