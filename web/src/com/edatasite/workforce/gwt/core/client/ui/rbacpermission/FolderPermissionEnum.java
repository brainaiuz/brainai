package com.edatasite.workforce.gwt.core.client.ui.rbacpermission;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

/**
 * User: Abdulaziz
 * Date: Mar 5, 2010
 * Time: 8:34:10 PM
 */
public enum FolderPermissionEnum implements Serializable {
    READ("read", "READ", "Read", "Can read folder"),
    WRITE("write", "WRITE", "Write file", "Can write file", READ),
    EDIT("edit", "EDIT", "Edit", "Can edit folder", READ, WRITE),
    DELETE("delete", "DELETE", "Delete", "Can delete folder"),
    PERMISSIONS_EDIT("permissionsEdit", "PERMISSIONS_EDIT", "Edit permissions", "Can Edit Permissions"),
    FULL_CONTROL("fullControl", "FULL_CONTROL", "Full Control", "Have a full control over folder", READ, WRITE, EDIT, DELETE, PERMISSIONS_EDIT);

    private String fieldName;
    private String name;
    private String code;
    private String description;
    private HashSet<FolderPermissionEnum> inclusivePermisisons = new HashSet<>();

    FolderPermissionEnum(String fieldName, String code, String name, String description) {
        this.fieldName = fieldName;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    FolderPermissionEnum(String fieldName, String code, String name, String description, FolderPermissionEnum... inclusives) {
        this.fieldName = fieldName;
        this.code = code;
        this.name = name;
        this.description = description;
        if (inclusives.length > 0) {
            inclusivePermisisons.addAll(Arrays.asList(inclusives));
        }
    }

    private HashSet<FolderPermissionEnum> getInclusives(HashSet<FolderPermissionEnum> parentList, FolderPermissionEnum permission) {
        if (!parentList.contains(permission)) {
            parentList.add(permission);
        }
        if (permission.inclusivePermisisons.size() > 0) {
            for (FolderPermissionEnum tp : permission.inclusivePermisisons) {
                getInclusives(parentList, tp);
            }
        }
        return parentList;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public HashSet<FolderPermissionEnum> getInclusivePermisisons() {
        return inclusivePermisisons;
    }

    public void setInclusivePermisisons(HashSet<FolderPermissionEnum> inclusivePermisisons) {
        this.inclusivePermisisons = inclusivePermisisons;
    }
//    public static void main(String... args){
//        for(TaskPermissionEnum tp:FULL_CONTROL.inclusivePermisisons){
//            System.out.println(tp);
//        }
//    }

}