package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 18.12.2008
 * Time: 16:09:53
 * To change this template use File | Settings | File Templates.
 */
public class ExistingAndNewTaskMembers implements IsSerializable, UserGrant {

    private PositionsSelectItem[] existingMembers;
    private PositionsSelectItem[] newMembers;
    private int permission;

    public PositionsSelectItem[] getExistingMembers() {
        return existingMembers;
    }

    public void setExistingMembers(PositionsSelectItem[] existingMembers) {
        this.existingMembers = existingMembers;
    }

    public PositionsSelectItem[] getNewMembers() {
        return newMembers;
    }

    public void setNewMembers(PositionsSelectItem[] newMembers) {
        this.newMembers = newMembers;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int b) {
        this.permission = b;
    }
}
