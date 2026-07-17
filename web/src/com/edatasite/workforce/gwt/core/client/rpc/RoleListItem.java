package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 16.05.12
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
public class RoleListItem implements IsSerializable {

    public static final String IS_SYSTEM = "issystem";
    public static final String NAME = "name";
    public static final String ACTIVE = "active";
    public static final String ROLE_ID = "id";

    private Integer objectID;
    private String name;
    private String description;
    private String code;
    private Boolean isSystem;
    private boolean active;
    private HashSet<String> moduleCode;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getSystem() {
        return isSystem;
    }

    public void setSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public HashSet<String> getModuleCode() {
        if (moduleCode == null) {
            moduleCode = new HashSet<>();
        }
        return moduleCode;
    }

    public void setModuleCode(HashSet<String> moduleCode) {
        this.moduleCode = moduleCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleListItem listItem = (RoleListItem) o;
        return Objects.equals(objectID, listItem.objectID) &&
                Objects.equals(code, listItem.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectID, code);
    }
}
