package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 09-Jul-2009
 * Time: 18:22:40
 * To change this template use File | Settings | File Templates.
 */
public class MailListItem implements IsSerializable {

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String MEMBERS_COUNT = "membersCount";
    public static final String ACTIVE = "active";
    public static final String CREATION_TIME = "creationtime";

    private Integer objectId;
    private String name;
    private String description;
    private Integer membersCount;
    private ContactListItem[] members;
    private Date createdDate;
    private boolean active = true;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(Integer membersCount) {
        this.membersCount = membersCount;
    }

    public ContactListItem[] getMembers() {
        return members;
    }

    public void setMembers(ContactListItem[] members) {
        this.members = members;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}