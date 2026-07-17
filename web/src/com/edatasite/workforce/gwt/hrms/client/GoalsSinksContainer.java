package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.DepartmentGoalEmployeeMetricHistoryListView;
import com.edatasite.workforce.gwt.hrms.client.ui.EmployeesGoalListView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 23, 2009
 * Time: 4:33:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoalsSinksContainer extends SinksContainer implements Constants {
    public GoalsSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        String type = params[1];

        switch (type) {

            case BUSINESS_GOAL:
                if (Utils.hasPermission(PermissionConstants.HRMS_BUSINESS_GOAL_SUMMARY)) {
                    super.addView(new ViewGoalForm(id, params));
                }
                break;

            case PROJECT_GOAL:
                if (Utils.hasPermission(PermissionConstants.HRMS_PROJECT_GOAL_SUMMARY)) {
                    super.addView(new ViewGoalForm(id, params));
                    super.addView(new EmployeesGoalListView(Utils.getUserID(), id, true));
                }
                break;

            case PERSONAL_GOAL:
                if (Utils.hasPermission(PermissionConstants.HRMS_PERSONAL_GOAL_SUMMARY)) {
                    super.addView(new ViewGoalForm(id, params));
                }
                break;

            case DEPARTMENT_GOAL:
                if (Utils.hasPermission(PermissionConstants.HRMS_DEPARTMENT_GOAL_SUMMARY)) {
                    super.addView(new DepartmentGoalView(id));
                    addView(new DepartmentGoalEmployeeMetricHistoryListView(id));
                }
                break;
            default:
                break;
        }
    }

}
