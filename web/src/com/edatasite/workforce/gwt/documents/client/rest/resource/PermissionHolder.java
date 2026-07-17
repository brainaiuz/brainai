package com.edatasite.workforce.gwt.documents.client.rest.resource;

import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;

import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 14.05.2010
 * Time: 15:34:29
 * To change this template use File | Settings | File Templates.
 */
public class PermissionHolder implements Serializable {

    private Integer objectId;
    private UserResource user;
    private GroupMembersViewItem group;
    private boolean read;
    private boolean delete;
    private boolean write;
    private boolean modifyACL;
    private String role;
    private boolean canChange = true;
    private String relationship;

    /**
     * Retrieve the user.
     *
     * @return the user
     */
    public UserResource getUser() {
        return user;
    }

    /**
     * Modify the user.
     *
     * @param aUser the user to set
     */
    public void setUser(UserResource aUser) {
        user = aUser;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    /**
     * Retrieve the group.
     *
     * @return the group
     */
    public GroupMembersViewItem getGroup() {
        return group;
    }

    /**
     * Modify the group.
     *
     * @param aGroup the group to set
     */
    public void setGroup(GroupMembersViewItem aGroup) {
        group = aGroup;
    }

    /**
     * Retrieve the read.
     *
     * @return the read
     */
    public boolean isRead() {
        return read;
    }

    /**
     * Modify the read.
     *
     * @param aRead the read to set
     */
    public void setRead(boolean aRead) {
        read = aRead;
    }

    /**
     * Retrieve the write.
     *
     * @return the write
     */
    public boolean isWrite() {
        return write;
    }

    /**
     * Modify the write.
     *
     * @param aWrite the write to set
     */
    public void setWrite(boolean aWrite) {
        write = aWrite;
    }

    /**
     * Retrieve the modifyACL.
     *
     * @return the modifyACL
     */
    public boolean isModifyACL() {
        return modifyACL;
    }

    /**
     * Modify the modifyACL.
     *
     * @param aModifyACL the modifyACL to set
     */
    public void setModifyACL(boolean aModifyACL) {
        modifyACL = aModifyACL;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isCanChange() {
        return canChange;
    }

    public void setCanChange(boolean canChange) {
        this.canChange = canChange;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
