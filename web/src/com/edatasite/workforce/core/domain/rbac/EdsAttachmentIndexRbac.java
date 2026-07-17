package com.edatasite.workforce.core.domain.rbac;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.Permission;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Oct 22, 2009
 * Time: 3:06:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "attachmentindexrbac", uniqueConstraints = @UniqueConstraint(columnNames = {"attachmentid", "userid"/*, "companyid"*/}))
public class EdsAttachmentIndexRbac extends EdsObject implements Permission {
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

    @ManyToOne
    @JoinColumn(name = "attachmentid")
    private EdsAttachment attachment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @Column
    private int permission;

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public EdsAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(EdsAttachment attachment) {
        this.attachment = attachment;
    }
}
