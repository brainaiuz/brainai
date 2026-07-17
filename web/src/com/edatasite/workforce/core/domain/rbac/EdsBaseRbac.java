package com.edatasite.workforce.core.domain.rbac;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsUser;

import javax.persistence.*;

/**
 * User: Anvarbek
 * Date: Feb 22, 2010
 * Time: 6:35:51 PM
 */
@MappedSuperclass
public class EdsBaseRbac extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private int entryType = INHERITED;// 3 Custom, 4 - inherited     if entry custom permissions are valid, if inherited to read permission from relationship

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupid")
    private EdsGroup group;

    private Integer trusteeType;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "trusteeid")
//    private EdsTrustee trustee;

//    private String permissions;


    private String relationship;

    private Integer relationRank;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }


    public int getEntryType() {
        return entryType;
    }

    public void setEntryType(int entryType) {
        this.entryType = entryType;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public EdsGroup getGroup() {
        return group;
    }

    public void setGroup(EdsGroup group) {
        this.group = group;
    }

    public Integer getTrusteeType() {
        return trusteeType;
    }

    public void setTrusteeType(Integer trusteeType) {
        this.trusteeType = trusteeType;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Integer getRelationRank() {
        return relationRank;
    }

    public void setRelationRank(Integer relationRank) {
        this.relationRank = relationRank;
    }
}
