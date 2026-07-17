package com.edatasite.workforce.core.domain.documents;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 13.05.2010
 * Time: 21:06:08
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "folderpermission")
public class EdsDocumentPermission extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    /**
     * Database record version. Used by hibernate, not to be used by user.
     */
    @SuppressWarnings("unused")
    @Version
    private int version;

    /**
     * Read permission.
     */
    private boolean read = false;

    /**
     * Write permission.
     */
    private boolean write = false;

    /**
     * Delete permission.
     */
    private boolean delete = false;

    /**
     * Modify ACL permission.
     */
    private boolean modifyACL = false;

    /**
     * Get the Read permission.
     *
     * @return boolean The Read permission
     */
    public boolean getRead() {
        return read;
    }

    /**
     * Get the Read permission.
     *
     * @return boolean The Read permission
     */
    public boolean hasRead() {
        return read;
    }

    /**
     * Set the Read permission.
     *
     * @param _read boolean The Read permission.
     */
    public void setRead(final boolean _read) {
        read = _read;
    }

    /**
     * Get the Write permission.
     *
     * @return boolean The Write permission
     */
    public boolean getWrite() {
        return write;
    }

    /**
     * Get the Write permission.
     *
     * @return boolean The Write permission
     */
    public boolean hasWrite() {
        return write;
    }

    public boolean hasDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    /**
     * Set the Write permission.
     *
     * @param newWrite the new write permission
     */
    public void setWrite(final boolean newWrite) {
        write = newWrite;
    }

    /**
     * Get the modify ACL permission.
     *
     * @return boolean The modify ACL permission
     */
    public boolean getModifyACL() {
        return modifyACL;
    }

    /**
     * Get the modify ACL permission.
     *
     * @return boolean The modify ACL permission
     */
    public boolean hasModifyACL() {
        return modifyACL;
    }

    /**
     * Set the modify ACL permission.
     *
     * @param newModifyACL the new modify ACL permission
     */
    public void setModifyACL(final boolean newModifyACL) {
        modifyACL = newModifyACL;
    }

    // ********************** Business Methods ********************** //

    /**
     * Merge an initial Permission object with a collection of individual
     * documentPermissions and produce an aggregate Permission object. Aggregate object
     * has a permission if at least one of the individual objects has said
     * permission.
     *
     * @param initialDocumentPermission Permission The initial Permission object
     * @param documentPermissions       Collection Permissions with which to merge
     * @return Permission Aggregate permission object
     */
    public EdsDocumentPermission mergePermissions(final EdsDocumentPermission initialDocumentPermission, final Collection<EdsDocumentPermission> documentPermissions) {
        if (documentPermissions != null) {
            for (final EdsDocumentPermission perm : documentPermissions) {
                initialDocumentPermission.setRead(initialDocumentPermission.hasRead() || perm.hasRead());
                initialDocumentPermission.setDelete(initialDocumentPermission.hasDelete() || perm.hasDelete());
                initialDocumentPermission.setWrite(initialDocumentPermission.hasWrite() || perm.hasWrite());
                initialDocumentPermission.setModifyACL(initialDocumentPermission.hasModifyACL() || perm.hasModifyACL());
            }
        }
        return initialDocumentPermission;
    }

    /**
     * Merge with a collection of individual documentPermissions and produce an
     * aggregate Permission object. Aggregate object has a permission if at
     * least one of the individual objects has said permission.
     *
     * @param documentPermissions Collection Permissions with which to merge
     * @return Permission Aggregate permission object
     */
    public EdsDocumentPermission mergePermissions(final Collection<EdsDocumentPermission> documentPermissions) {
        return mergePermissions(this, documentPermissions);
    }

    /**
     * Merge with a collection of individual documentPermission and produce an
     * aggregate Permission object. Aggregate object has a permission if at
     * least one of the individual objects has said permission.
     *
     * @param perm Collection Permission with which to merge
     * @return Permission Aggregate permission object
     */
    public EdsDocumentPermission mergePermission(final EdsDocumentPermission perm) {
        this.setRead(this.hasRead() || perm.hasRead());
        this.setDelete(this.hasDelete() || perm.hasDelete());
        this.setWrite(this.hasWrite() || perm.hasWrite());
        this.setModifyACL(this.hasModifyACL() || perm.hasModifyACL());
        return this;
    }

    /**
     * Return a new Data Transfer Object for this object.
     *
     * @return a new DTO with the same contents as this object
     */
    public PermissionHolder getDTO() {
        final PermissionHolder p = new PermissionHolder();
        p.setObjectId(objectID);
        p.setDelete(delete);
        p.setRead(read);
        p.setWrite(write);
        p.setModifyACL(modifyACL);
        return p;
    }
}
