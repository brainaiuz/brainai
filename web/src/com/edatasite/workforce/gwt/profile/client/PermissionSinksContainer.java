package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.ManageRolePermission;
import com.edatasite.workforce.gwt.profile.client.ui.view.RoleListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.SettingEmployeeListView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 16.05.12
 * Time: 18:13
 * To change this template use File | Settings | File Templates.
 */
public class PermissionSinksContainer extends SinksContainer {

    public PermissionSinksContainer(String name, String description, String[] params) {
        super(name, description, params, Constants.NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.SETTINGS_EMPLOYEE_LIST)) {
        }
        if (Utils.hasPermission(PermissionConstants.SETTINGS_ROLE_LIST)) {
            addView(new RoleListView());
        }
        addView(new SettingEmployeeListView(EmployeeListView.FROM_HRMS));
        if (Utils.hasPermission(PermissionConstants.SETTINGS_MANAGE_ROLE)) {
            addView(new ManageRolePermission());
        }
    }
}