package com.edatasite.workforce.gwt.core.client.rpc.rbac;

import com.edatasite.workforce.gwt.core.client.ui.Constants;

import java.io.Serializable;

/**
 * User: Abdulaziz
 * Date: May 19, 2010
 * Time: 12:09:03 PM
 */
public class GroupMembersViewItem implements Serializable, Constants {

    private Integer groupID;
    private String groupName;
    private String groupDescription;
    private String groupConstantName;
    private boolean isDefault = false;
    private GroupMemberItem[] members;
    private Integer groupEntryType;
    private boolean canChange = false;
    private String type = IS_EMPLOYEE;//

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupConstantName() {
        return groupConstantName;
    }

    public void setGroupConstantName(String groupConstantName) {
        this.groupConstantName = groupConstantName;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public GroupMemberItem[] getMembers() {
        return members;
    }

    public void setMembers(GroupMemberItem[] members) {
        this.members = members;
    }

    public Integer getGroupEntryType() {
        return groupEntryType;
    }

    public void setGroupEntryType(Integer groupEntryType) {
        this.groupEntryType = groupEntryType;
    }

    public boolean isCanChange() {
        return canChange;
    }

    public void setCanChange(boolean canChange) {
        this.canChange = canChange;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
