package com.edatasite.workforce.gwt.employee.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.ui.PMEmployeeEditForm;
import com.edatasite.workforce.gwt.employee.client.ui.PMNewEmployeeSummaryView;
import com.edatasite.workforce.gwt.issue.client.ui.IssueListView;
import com.google.gwt.user.client.Command;

import java.util.LinkedList;

public class EmployeeViewSinksContainer extends SinksContainer {

    public EmployeeViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    protected void checkForAccess(Command grantAccess, Command accessDenied) {

        renderSinksContainer();
        return;

//        if (Utils.hasRole(ADMIN)) {
//            grantAccess.execute();
//            return;
//        }
//        String permissionContext;
//        if (Utils.isPM()) {
//            permissionContext = PermissionConstants.PM_CONTEXT;
//        } else {
//            permissionContext = PermissionConstants.HRMS_CONTEXT;
//        }
//        EmployeeService.App.get().getEmployeeSpecificPermission(id, permissionContext, new AbstractAsyncCallback<HashSet<String>>() {
//            @Override
//            public void failure(Throwable throwable) {
//                grantAccess.execute();
//            }
//
//            @Override
//            public void success(HashSet<String> result) {
//                Utils.setUserPermissions(result);
//                grantAccess.execute();
//                showPrepared();
//                MainLayout.get().addDynamicContainer(EmployeeViewSinksContainer.this);
//            }
//        });
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }


    protected void initViews() {
        String summaryPermission;
        String editPermission;
        String editOwnPermission;
        if (Utils.isPM()) {
            summaryPermission = PermissionConstants.PM_EMPLOYEE_SUMMARY;
            editPermission = PermissionConstants.PM_EMPLOYEE_EDIT;
            editOwnPermission = PermissionConstants.PM_EMPLOYEE_EDIT_OWN_PROFILE;
        } else {
            summaryPermission = PermissionConstants.HRMS_EMPLOYEE_PROFILE;
            editPermission = PermissionConstants.HRMS_EDIT_PROFILE;
            editOwnPermission = PermissionConstants.HRMS_EDIT_OWN_PROFILE;
        }

        if (Utils.hasRole(ADMIN) || Utils.hasPermission(summaryPermission)) {
            super.addView(new PMNewEmployeeSummaryView(this.id));
        }

        if (Utils.hasRole(ADMIN) || Utils.hasPermission(editPermission) || (this.id != null && Utils.getUserID().equals(this.id) && Utils.hasPermission(editOwnPermission))) {
            PMEmployeeEditForm employeeEditView = new PMEmployeeEditForm(this.id);
            super.addView(employeeEditView);
        }

        if (id != null && Utils.hasPermission(PermissionConstants.PM_ISSUE_LIST)) {
            addView(new IssueListView(id, RelationItem.TYPE_EMPLOYEE));
        }
        if (id != null) {
            addDynamicView(CustomFieldLookUpTypeEnum.EMPLOYEE, id);
        }
    }
}
