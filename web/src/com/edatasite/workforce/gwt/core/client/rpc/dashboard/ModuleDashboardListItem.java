package com.edatasite.workforce.gwt.core.client.rpc.dashboard;

import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * User: Abror Abdukadirov
 * Date: 10.04.2018 15:04
 */
public class ModuleDashboardListItem implements IsSerializable {
    public static final String DASHBOARD_NAME = "DASHBOARD_NAME";
    public static final String MODULE = "MODULE";
    public static final String IS_ACTIVE = "IS_ACTIVE";
    public static final String IS_DEFAULT = "IS_DEFAULT";
    public static final String IS_SYSTEM = "IS_SYSTEM";
    public static final String CREATOR = "CREATOR";
    public static final String CREATION_DATE = "CREATION_DATE";
    public static final String UPDATOR = "UPDATOR";
    public static final String UPDATED_DATE = "UPDATED_DATE";

    private Integer objectId;
    private String name;
    private ModuleEnum module;
    private boolean isActive;
    private boolean isDefault;
    private boolean isSystem;
    private SelectItem creator;
    private SelectItem updator;
    private Date creationDate;
    private Date updatedDate;
    private ArrayList<Integer> selectedRoleIds = new ArrayList<>();
    private ArrayList<SelectItem> roles;
    private LinkedHashMap<Integer, Boolean> roleMap;
    private String numberOfWidgets;

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

    public ModuleEnum getModule() {
        return module;
    }

    public void setModule(ModuleEnum module) {
        this.module = module;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getUpdator() {
        return updator;
    }

    public void setUpdator(SelectItem updator) {
        this.updator = updator;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public ArrayList<Integer> getSelectedRoleIds() {
        return selectedRoleIds;
    }

    public void setSelectedRoleIds(ArrayList<Integer> selectedRoleIds) {
        this.selectedRoleIds = selectedRoleIds;
    }

    public ArrayList<SelectItem> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<SelectItem> roles) {
        this.roles = roles;
    }

    public LinkedHashMap<Integer, Boolean> getRoleMap() {
        return roleMap;
    }

    public void setRoleMap(LinkedHashMap<Integer, Boolean> roleMap) {
        this.roleMap = roleMap;
    }

    public String getNumberOfWidgets() {
        return numberOfWidgets;
    }

    public void setNumberOfWidgets(String numberOfWidgets) {
        this.numberOfWidgets = numberOfWidgets;
    }
}
