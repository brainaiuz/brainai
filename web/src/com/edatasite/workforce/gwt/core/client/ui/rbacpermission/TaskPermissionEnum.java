package com.edatasite.workforce.gwt.core.client.ui.rbacpermission;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

/**
 * User: Abdulaziz
 * Date: Mar 5, 2010
 * Time: 8:34:10 PM
 */
public enum TaskPermissionEnum implements Serializable {
    VIEW("view", "VIEW", "View", "Can view task"),
    TIMESHEET_ENTRY_ADD("timesheetEntryAdd", "TIMESHEET_ENTRY_ADD", "Add Time Sheet Entries", "Can add timesheet entries", VIEW),
    ASSIGNEE_VIEW("assigneeView", "ASSIGNEE_VIEW", "View Assignees", "Can view task assignees", VIEW),
    ASSIGNEE_EDIT("assigneeEdit", "ASSIGNEE_EDIT", "Edit Assignees", "Can edit task assignees", VIEW, ASSIGNEE_VIEW),
    STATUS_EDIT("statusEdit", "STATUS_EDIT", "Edit Task Status", "Can edit task status"),
    ASSIGNEE_STATUS_EDIT("assigneeStatusEdit", "ASSIGNEE_STATUS_EDIT", "Assignee Status Edit", "Can Edit Assignee Task Status", VIEW, ASSIGNEE_VIEW),
    EDIT("edit", "EDIT", "Edit", "Can edit task", VIEW, ASSIGNEE_VIEW, ASSIGNEE_EDIT, STATUS_EDIT),
    DELETE("delete", "DELETE", "Delete", "Can delete task"),
    PERMISSIONS_EDIT("permissionsEdit", "PERMISSIONS_EDIT", "Edit permissions", "Can Edit Permissions"),
    FULL_CONTROL("fullControl", "FULL_CONTROL", "Full Control", "Have a full control over task", VIEW, ASSIGNEE_VIEW, ASSIGNEE_EDIT, STATUS_EDIT, ASSIGNEE_STATUS_EDIT, EDIT, DELETE, PERMISSIONS_EDIT);

    private String fieldName;
    private String name;
    private String code;
    private String description;
    private HashSet<TaskPermissionEnum> inclusivePermisisons = new HashSet<>();

    TaskPermissionEnum(String fieldName, String code, String name, String description) {
        this.fieldName = fieldName;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    TaskPermissionEnum(String fieldName, String code, String name, String description, TaskPermissionEnum... inclusives) {
        this.fieldName = fieldName;
        this.code = code;
        this.name = name;
        this.description = description;
        if (inclusives.length > 0) {
            inclusivePermisisons.addAll(Arrays.asList(inclusives));
        }
    }

    private HashSet<TaskPermissionEnum> getInclusives(HashSet<TaskPermissionEnum> parentList, TaskPermissionEnum permission) {
        if (!parentList.contains(permission)) {
            parentList.add(permission);
        }
        if (permission.inclusivePermisisons.size() > 0) {
            for (TaskPermissionEnum tp : permission.inclusivePermisisons) {
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

    public HashSet<TaskPermissionEnum> getInclusivePermisisons() {
        return inclusivePermisisons;
    }

    public void setInclusivePermisisons(HashSet<TaskPermissionEnum> inclusivePermisisons) {
        this.inclusivePermisisons = inclusivePermisisons;
    }
//    public static void main(String... args){
//        for(TaskPermissionEnum tp:FULL_CONTROL.inclusivePermisisons){
//            System.out.println(tp);
//        }
//    }

}
