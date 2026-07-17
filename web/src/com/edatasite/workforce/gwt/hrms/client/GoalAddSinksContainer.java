package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.DepartmentGoalAddEditView;
import com.edatasite.workforce.gwt.hrms.client.ui.GoalAddEditView2;

import java.util.LinkedList;

public class GoalAddSinksContainer extends SinksContainer {
    public GoalAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {}

    protected void initViews() {
        String type = null;
        boolean fromProjectsModule = false;
        Integer relatedProjectID = null;
        Integer relatedDepartmentID = null;
        Integer departmentLocationID = null;

        if (params.length > 4 && DEPARTMENT_GOAL.equals(params[2])) {
            type = params[2];
            relatedDepartmentID = Integer.valueOf(params[3]);
            departmentLocationID = Integer.valueOf(params[4]);
        } else if (params.length > 3) {
            if ("true".equals(params[1])) {
                fromProjectsModule = true;
                relatedProjectID = Integer.valueOf(params[2]);
                type = params[3];
            } else if (DEPARTMENT_GOAL.equals(params[2])) {
                type = params[2];
                relatedDepartmentID = Integer.valueOf(params[3]);
            }
        } else if (params.length > 2) {
            type = params[2];
        }

        if (fromProjectsModule) {
            addView(new GoalAddEditView2(fromProjectsModule, relatedProjectID, type));
            return;
        }

        if (id == null
                && (
                (PERSONAL_GOAL.equals(type)  && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PERSONAL_GOALS)) ||
                (PROJECT_GOAL.equals(type)   && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PROJECT_GOALS)) ||
                (BUSINESS_GOAL.equals(type)  && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_BUSINESS_GOALS))
        )
        ) {
            addView(new GoalAddEditView2(id, type));
            return;
        }

        if (id != null
                && (
                (PERSONAL_GOAL.equals(type) && Utils.hasPermission(PermissionConstants.HRMS_EDIT_PERSONAL_GOAL)) ||
                (PROJECT_GOAL.equals(type)  && Utils.hasPermission(PermissionConstants.HRMS_PROJECT_GOAL_SUMMARY)) ||
                (BUSINESS_GOAL.equals(type) && Utils.hasPermission(PermissionConstants.HRMS_EDIT_BUSINESS_GOAL))
        )
        ) {
            addView(new GoalAddEditView2(id, type));
        }

        if (DEPARTMENT_GOAL.equals(type)
                && (Utils.hasPermission(PermissionConstants.HRMS_EDIT_DEPARTMENT_GOAL)
                || Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_DEPARTMENT_GOALS))
        ) {
            addView(new DepartmentGoalAddEditView(id, relatedDepartmentID, departmentLocationID));
        }
    }

}
