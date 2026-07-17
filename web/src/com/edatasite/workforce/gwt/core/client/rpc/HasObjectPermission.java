package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.ArrayList;

public class HasObjectPermission extends SelectItem {
    private boolean denied = false;
    private String uniqueId;
    private ArrayList<SelectItem> roles = new ArrayList<>();

    public boolean isDenied() {
        return denied;
    }

    public void setDenied(boolean denied) {
        this.denied = denied;
    }

    public ArrayList<SelectItem> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<SelectItem> roles) {
        this.roles = roles;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
