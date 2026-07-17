package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.ui.AddEmployeeView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Dec 18, 2009
 * Time: 7:59:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class HrmsEmployeeAddSinksContainer extends SinksContainer {

    public HrmsEmployeeAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if ((params != null && "add".equals(params[0])) && (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EMPLOYEE) || Utils.isSettings())) {
            AddEmployeeView addEmployeeView = new AddEmployeeView(params.length > 1 ? params[1] : null);
            addView(addEmployeeView);
        }
    }
}
