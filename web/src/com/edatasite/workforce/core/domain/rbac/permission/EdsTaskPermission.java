package com.edatasite.workforce.core.domain.rbac.permission;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.server.rpc.TaskPermissionItem;
import com.edatasite.workforce.gwt.core.client.ui.rbacpermission.TaskPermissionEnum;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Abdulaziz
 * Date: Mar 3, 2010
 * Time: 4:45:55 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "taskpermission")
public class EdsTaskPermission extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

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

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public TaskPermissionItem getMergedPermissions(final TaskPermissionItem initialPermission, final Collection<EdsTaskPermission> permissions) {
        if (permissions != null) {
            for (final EdsTaskPermission perm : permissions) {
                initialPermission.setView(initialPermission.isView() || perm.isView());
                initialPermission.setTimesheetEntryAdd(initialPermission.isTimesheetEntryAdd() || perm.isTimesheetEntryAdd());
                initialPermission.setAssigneeView(initialPermission.isAssigneeView() || perm.isAssigneeView());
                initialPermission.setAssigneeEdit(initialPermission.isAssigneeEdit() || perm.isAssigneeEdit());
                initialPermission.setStatusEdit(initialPermission.isStatusEdit() || perm.isStatusEdit());
                initialPermission.setAssigneeStatusEdit(initialPermission.isAssigneeStatusEdit() || perm.isAssigneeStatusEdit());
                initialPermission.setEdit(initialPermission.isEdit() || perm.isEdit());
                initialPermission.setDelete(initialPermission.isDelete() || perm.isDelete());
                initialPermission.setPermissionsEdit(initialPermission.isPermissionsEdit() || perm.isPermissionsEdit());
                initialPermission.setFullControl(initialPermission.isFullControl() || perm.isFullControl());
            }
        }
        return initialPermission;
    }

    /*public EdsTaskPermission getMergedPermissions(final Collection<EdsTaskPermission> permissions) {
        EdsTaskPermission transientPermission = new EdsTaskPermission();
        if (permissions != null) {
            return getMergedPermissions(transientPermission, permissions);
        }
        return transientPermission;
    }*/

    public List<String> getPemissionAsStringList() {
        List<String> permissions = new ArrayList<>();
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

    public TaskPermissionItem getAsTaskPermissionItem() {
        TaskPermissionItem permissionItem = new TaskPermissionItem();

        permissionItem.setView(view);
        permissionItem.setTimesheetEntryAdd(timesheetEntryAdd);
        permissionItem.setAssigneeView(assigneeView);
        permissionItem.setAssigneeEdit(assigneeEdit);
        permissionItem.setStatusEdit(statusEdit);
        permissionItem.setAssigneeStatusEdit(assigneeStatusEdit);
        permissionItem.setEdit(edit);
        permissionItem.setDelete(delete);
        permissionItem.setPermissionsEdit(permissionsEdit);
        permissionItem.setFullControl(fullControl);

        return permissionItem;
    }
}
