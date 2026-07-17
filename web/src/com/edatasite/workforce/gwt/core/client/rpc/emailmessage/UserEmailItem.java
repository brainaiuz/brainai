package com.edatasite.workforce.gwt.core.client.rpc.emailmessage;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.gwt.documents.client.rest.resource.UserResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 05.01.12
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
public class UserEmailItem extends SelectItem implements IsSerializable {

    private UserResource owner;
    private ArrayList<GroupMembersViewItem> groups;
    private ArrayList<TeamEmployees> teamEmployees;
    protected PermissionHolder permission = new PermissionHolder();
    protected HashSet<PermissionHolder> permissions = new HashSet<>();

    public UserResource getOwner() {
        return owner;
    }

    public void setOwner(UserResource owner) {
        this.owner = owner;
    }

    public ArrayList<TeamEmployees> getTeamEmployees() {
        return teamEmployees;
    }

    public void setTeamEmployees(ArrayList<TeamEmployees> teamEmployees) {
        this.teamEmployees = teamEmployees;
    }

    public ArrayList<GroupMembersViewItem> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<GroupMembersViewItem> groups) {
        this.groups = groups;
    }

    public PermissionHolder getPermission() {
        return permission;
    }

    public void setPermission(PermissionHolder permission) {
        this.permission = permission;
    }

    public HashSet<PermissionHolder> getPermissions() {
        return permissions;
    }

    public void setPermissions(HashSet<PermissionHolder> permissions) {
        this.permissions = permissions;
    }
}
