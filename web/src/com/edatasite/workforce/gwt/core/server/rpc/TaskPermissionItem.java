package com.edatasite.workforce.gwt.core.server.rpc;

import com.edatasite.workforce.gwt.core.client.ui.rbacpermission.TaskPermissionEnum;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskPermissionItem implements Serializable {

    private boolean view = false;
    private boolean timesheetEntryAdd = false;
    private boolean assigneeView = false;
    private boolean assigneeEdit = false;
    private boolean statusEdit = false;
    private boolean assigneeStatusEdit = false;
    private boolean edit = false;
    private boolean delete = false;
    private boolean permissionsEdit = false;
    private boolean fullControl = false;

    public TaskPermissionItem() {
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean isTimesheetEntryAdd() {
        return timesheetEntryAdd;
    }

    public void setTimesheetEntryAdd(boolean timesheetEntryAdd) {
        this.timesheetEntryAdd = timesheetEntryAdd;
    }

    public boolean isAssigneeView() {
        return assigneeView;
    }

    public void setAssigneeView(boolean assigneeView) {
        this.assigneeView = assigneeView;
    }

    public boolean isAssigneeEdit() {
        return assigneeEdit;
    }

    public void setAssigneeEdit(boolean assigneeEdit) {
        this.assigneeEdit = assigneeEdit;
    }

    public boolean isStatusEdit() {
        return statusEdit;
    }

    public void setStatusEdit(boolean statusEdit) {
        this.statusEdit = statusEdit;
    }

    public boolean isAssigneeStatusEdit() {
        return assigneeStatusEdit;
    }

    public void setAssigneeStatusEdit(boolean assigneeStatusEdit) {
        this.assigneeStatusEdit = assigneeStatusEdit;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isPermissionsEdit() {
        return permissionsEdit;
    }

    public void setPermissionsEdit(boolean permissionsEdit) {
        this.permissionsEdit = permissionsEdit;
    }

    public boolean isFullControl() {
        return fullControl;
    }

    public void setFullControl(boolean fullControl) {
        this.fullControl = fullControl;
    }

    public ArrayList<String> getPemissionAsStringList() {
        ArrayList<String> permissions = new ArrayList<>();
        if (view) {
            permissions.add(TaskPermissionEnum.VIEW.getCode());
        }
        if (timesheetEntryAdd) {
            permissions.add(TaskPermissionEnum.TIMESHEET_ENTRY_ADD.getCode());
        }
        if (assigneeView) {
            permissions.add(TaskPermissionEnum.ASSIGNEE_VIEW.getCode());
        }
        if (assigneeEdit) {
            permissions.add(TaskPermissionEnum.ASSIGNEE_EDIT.getCode());
        }
        if (statusEdit) {
            permissions.add(TaskPermissionEnum.STATUS_EDIT.getCode());
        }
        if (assigneeStatusEdit) {
            permissions.add(TaskPermissionEnum.ASSIGNEE_STATUS_EDIT.getCode());
        }
        if (edit) {
            permissions.add(TaskPermissionEnum.EDIT.getCode());
        }
        if (delete) {
            permissions.add(TaskPermissionEnum.DELETE.getCode());
        }
        if (fullControl) {
            permissions.add(TaskPermissionEnum.FULL_CONTROL.getCode());
        }
        return permissions;
    }
}
