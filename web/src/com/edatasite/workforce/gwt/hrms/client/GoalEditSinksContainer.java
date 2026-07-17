package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.DepartmentGoalAddEditView;
import com.edatasite.workforce.gwt.hrms.client.ui.EditGoalForm;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: romeo
 * Date: 5/24/12
 * Time: 2:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class GoalEditSinksContainer extends SinksContainer {
    public GoalEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        String type = params[1];
        String permission = PermissionConstants.HRMS_EDIT_PERSONAL_GOAL;
        if (BUSINESS_GOAL.equals(type)) {
            permission = PermissionConstants.HRMS_EDIT_BUSINESS_GOAL;
        } else if (DEPARTMENT_GOAL.equals(type)) {
            permission = PermissionConstants.HRMS_EDIT_DEPARTMENT_GOAL;
        } else if (PROJECT_GOAL.equals(type)) {
            permission = PermissionConstants.HRMS_EDIT_PROJECT_GOAL;
        }

        if (Utils.hasPermission(permission)) {
            if (DEPARTMENT_GOAL.equals(type)) {
                addView(new DepartmentGoalAddEditView(id));
            } else {
                addView(new EditGoalForm(id, params));
            }
        }
    }

}
