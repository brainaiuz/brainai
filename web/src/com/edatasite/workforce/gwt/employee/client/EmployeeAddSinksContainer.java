package com.edatasite.workforce.gwt.employee.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.ui.AddEmployeeView;

import java.util.LinkedList;

public class EmployeeAddSinksContainer extends SinksContainer {

    public EmployeeAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_ADD)) {
            if (params != null && "add".equals(params[0])) {
                AddEmployeeView addEmployeeView = new AddEmployeeView(params.length > 1 ? params[1] : null);
                addView(addEmployeeView);
            }
        }
    }
}
