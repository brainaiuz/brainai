package com.edatasite.workforce.gwt.task.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: Abdulaziz
 * Date: Jun 28, 2010
 * Time: 7:01:28 AM
 */
public class PermissionListItem implements IsSerializable {
    private ArrayList<String> permissions;

    public PermissionListItem() {

    }

    public PermissionListItem(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public PermissionListItem(String[] uPermissions) {
        permissions = new ArrayList<>();
        permissions.addAll(Arrays.asList(uPermissions));
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }
}
