package com.edatasite.workforce.gwt.documents.client.rest.resource;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 15.05.2010
 * Time: 22:21:32
 * To change this template use File | Settings | File Templates.
 */
public class AuditInfoResource implements IsSerializable, Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The date the associated object was created.
     */
    private Date creationDate;

    /**
     * The user that created the associated object.
     */
    private UserResource createdBy;

    /**
     * The date the associated object was modified.
     */
    private Date modificationDate;

    /**
     * The user that modified the associated object.
     */
    private UserResource modifiedBy;

    private boolean isSuperUser = false;

    /**
     * A default constructor for serialization.
     */
    public AuditInfoResource() {
        // Do nothing.
    }

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
     * @param newCreationDate the new date of creation
     */
    public void setCreationDate(final Date newCreationDate) {
        creationDate = newCreationDate;
    }

    /**
     * Retrieve the user that created the associated object.
     *
     * @return the user that created the associated object
     */
    public UserResource getCreatedBy() {
        return createdBy;
    }

    /**
     * Modify the user that created the associated object.
     *
     * @param newCreatedBy the new user that created the associated object
     */
    public void setCreatedBy(final UserResource newCreatedBy) {
        createdBy = newCreatedBy;
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
     * @param newModificationDate the new date of modification
     */
    public void setModificationDate(final Date newModificationDate) {
        modificationDate = newModificationDate;
    }

    /**
     * Retrieve the user that modified the associated object.
     *
     * @return the user that modified the associated object
     */
    public UserResource getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Modify the user that modified the associated object.
     *
     * @param newModifiedBy the new user that modified the associated object
     */
    public void setModifiedBy(final UserResource newModifiedBy) {
        modifiedBy = newModifiedBy;
    }

    public boolean isSuperUser() {
        return isSuperUser;
    }

    public void setSuperUser(boolean superUser) {
        this.isSuperUser = superUser;
    }
}
