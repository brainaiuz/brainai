package com.edatasite.workforce.core.domain.rbac.contact;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactPermission;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * User: Hayot
 * Date: 29.05.2010
 * Time: 12:11:14
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "contactcategoryrbac")
public class EdsContactCategoryRbac extends EdsObject implements Constants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsContactCategory contactCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsCrmContact contact;

    private int entryType = INHERITED;// 4 Custom, 3 - inherited     if entry custom permissions are valid, if inherited to read permission from relationship

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "contactPermissionId")
    private EdsContactPermission contactPermission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupid")
    private EdsGroup group;

    private Integer trusteeType;

    private int contactCategoryType = F_DEFAULT;

    private Integer entityId;

    private String relationship;

    private Integer relationRank;

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean isContact = false;

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

    public int getContactCategoryType() {
        return contactCategoryType;
    }

    public void setContactCategoryType(int contactCategoryType) {
        this.contactCategoryType = contactCategoryType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public EdsContactCategory getContactCategory() {
        return contactCategory;
    }

    public void setContactCategory(EdsContactCategory contactCategory) {
        this.contactCategory = contactCategory;
    }

    public EdsCrmContact getContact() {
        return contact;
    }

    public void setContact(EdsCrmContact contact) {
        this.contact = contact;
    }

    public EdsContactPermission getContactPermission() {
        return contactPermission;
    }

    public void setContactPermission(EdsContactPermission contactPermission) {
        this.contactPermission = contactPermission;
    }

    public boolean isContact() {
        return isContact;
    }

    public void setContact(boolean contact) {
        isContact = contact;
    }

    public EdsContactCategoryRbac copyRbac(EdsContactCategory contactCategory) {
        EdsContactCategoryRbac newRbac = new EdsContactCategoryRbac();
        newRbac.setContact(getContact());
        newRbac.setContactCategory(contactCategory);
        newRbac.setEntityId(getEntityId());
        newRbac.setGroup(getGroup());
        newRbac.setTrusteeType(getTrusteeType());
        newRbac.setContact(isContact());
        newRbac.setEntryType(getEntryType());
        newRbac.setRelationRank(getRelationRank());
        newRbac.setRelationship(getRelationship());
        newRbac.setContactCategoryType(getContactCategoryType());
        newRbac.setUser(getUser());
        newRbac.setContactPermission(getContactPermission());
        return newRbac;
    }
}
