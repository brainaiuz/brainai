package com.edatasite.workforce.core.domain.rbac.policy;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.core.domain.rbac.permission.EdsTaskPermission;

import javax.persistence.*;

/**
 * User: Abdulaziz
 * Date: Mar 4, 2010
 * Time: 3:35:15 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "taskpolicy")
public class EdsTaskPolicy extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permissionid")
    private EdsTaskPermission permission;

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

    public EdsTaskPermission getPermission() {
        return permission;
    }

    public void setPermission(EdsTaskPermission permission) {
        this.permission = permission;
    }

//    public EdsCompany getCompany() {
//        return company;
//    }
//
//    public void setCompany(EdsCompany company) {
//        this.company = company;
//    }

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
