package com.edatasite.workforce.core.domain.crm.contact;

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
 * User: Hayot
 * Date: 13.05.2010
 * Time: 21:06:08
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "contactpermission")
public class EdsContactPermission extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

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

    // ********************** Business Methods ********************** //

    /**
     * Merge an initial Permission object with a collection of individual
     * contactPermissions and produce an aggregate Permission object. Aggregate object
     * has a permission if at least one of the individual objects has said
     * permission.
     *
     * @param initialDocumentPermission Permission The initial Permission object
     * @param contactPermissions        Collection Permissions with which to merge
     * @return Permission Aggregate permission object
     */
    public EdsContactPermission mergePermissions(final EdsContactPermission initialDocumentPermission, final Collection<EdsContactPermission> contactPermissions) {
        if (contactPermissions != null) {
            for (final EdsContactPermission perm : contactPermissions) {
                initialDocumentPermission.setRead(initialDocumentPermission.isRead() || perm.isRead());
                initialDocumentPermission.setDelete(initialDocumentPermission.isDelete() || perm.isDelete());
                initialDocumentPermission.setWrite(initialDocumentPermission.isWrite() || perm.isWrite());
                initialDocumentPermission.setModifyACL(initialDocumentPermission.isModifyACL() || perm.isModifyACL());
            }
        }
        return initialDocumentPermission;
    }

    /**
     * Merge with a collection of individual documentPermission and produce an
     * aggregate Permission object. Aggregate object has a permission if at
     * least one of the individual objects has said permission.
     *
     * @param perm Collection Permission with which to merge
     * @return Permission Aggregate permission object
     */
    public EdsContactPermission mergePermission(final EdsContactPermission perm) {
        this.setRead(this.isRead() || perm.isRead());
        this.setDelete(this.isDelete() || perm.isDelete());
        this.setWrite(this.isWrite() || perm.isWrite());
        this.setModifyACL(this.isModifyACL() || perm.isModifyACL());
        return this;
    }

    /**
     * Return a new Data Transfer Object for this object.
     *
     * @return a new DTO with the same contents as this object
     */
    public PermissionHolder getDTO() {
        final PermissionHolder p = new PermissionHolder();
        p.setObjectId(getObjectID());
        p.setDelete(isDelete());
        p.setRead(isRead());
        p.setWrite(isWrite());
        p.setModifyACL(isModifyACL());
        return p;
    }

    /**
     * Return a new Data Transfer Object for this object.
     *
     * @return a new DTO with the same contents as this object
     */
    public static EdsContactPermission getOwnersEdsPermission() {
        final EdsContactPermission p = new EdsContactPermission();
        p.setDelete(true);
        p.setRead(true);
        p.setWrite(true);
        p.setModifyACL(true);
        return p;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isModifyACL() {
        return modifyACL;
    }

    public void setModifyACL(boolean modifyACL) {
        this.modifyACL = modifyACL;
    }
}
