package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.contact.client.ui.HrmsEmployeeEditForm;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: unni
 * Date: Oct 26, 2009
 * Time: 8:53:51 PM
 */
public class EmployeeProfileEditSinksContainer extends SinksContainer {

    public EmployeeProfileEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        boolean hasPermission = Utils.hasPermission(Utils.isPM() ? PermissionConstants.PM_EMPLOYEE_EDIT : PermissionConstants.HRMS_EDIT_PROFILE);
        boolean own = Utils.hasPermission(Utils.isPM() ? PermissionConstants.PM_EMPLOYEE_EDIT_OWN_PROFILE : PermissionConstants.HRMS_EDIT_OWN_PROFILE);
        if (hasPermission || (Utils.getUserID().equals(id) && own) || Utils.isSettings()) {
            super.addView(new HrmsEmployeeEditForm(id));
        }
    }
}