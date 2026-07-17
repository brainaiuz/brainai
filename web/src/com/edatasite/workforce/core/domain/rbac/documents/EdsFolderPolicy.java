package com.edatasite.workforce.core.domain.rbac.documents;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.documents.EdsDocumentPermission;
import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;

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
 * User: Sherali
 * Date: 29.05.2010
 * Time: 13:56:14
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "folderpolicy", uniqueConstraints = @UniqueConstraint(columnNames = {"relationid"}))
public class EdsFolderPolicy extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permissionid")
    private EdsDocumentPermission permission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trusteeid")
    private EdsTrustee trustee;// default ownere of this entity default taskreviewers group should be created and all admins should be members

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relationid")
    private EdsRelationship relation;

    @Column
    private int entryType = BUILT_IN;//2 - builtin, 3 - custom

    @Column
    private String description;

    public EdsDocumentPermission getPermission() {
        return permission;
    }

    public void setPermission(EdsDocumentPermission permission) {
        this.permission = permission;
    }

    public EdsTrustee getTrustee() {
        return trustee;
    }

    public void setTrustee(EdsTrustee trustee) {
        this.trustee = trustee;
    }

    public EdsRelationship getRelation() {
        return relation;
    }

    public void setRelation(EdsRelationship relation) {
        this.relation = relation;
    }

    public int getEntryType() {
        return entryType;
    }

    public void setEntryType(int entryType) {
        this.entryType = entryType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }
}
