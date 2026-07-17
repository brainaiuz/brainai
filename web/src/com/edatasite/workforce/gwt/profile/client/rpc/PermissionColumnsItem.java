package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.PermissionItem;
import com.edatasite.workforce.gwt.core.client.rpc.RoleListItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 24.05.12
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
public class PermissionColumnsItem implements IsSerializable {

    public ArrayList<RoleListItem> roleList;
    public ArrayList<PermissionItem> sectionList;

    public ArrayList<RoleListItem> getRoleList() {
        return roleList;
    }

    public void setRoleList(ArrayList<RoleListItem> roleList) {
        this.roleList = roleList;
    }

    public ArrayList<PermissionItem> getSectionList() {
        return sectionList;
    }

    public void setSectionList(ArrayList<PermissionItem> sectionList) {
        this.sectionList = sectionList;
    }
}
