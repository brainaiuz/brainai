package com.edatasite.workforce.gwt.pm.client;

import com.edatasite.workforce.gwt.client.client.ui.view.NewClientListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.AddCustomFormItemView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemListView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemView;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.ui.CompanyConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.resourceUtil.ResourceUtilizationView;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeListView;
import com.edatasite.workforce.gwt.expenses.client.ui.view.ExpenseListView;
import com.edatasite.workforce.gwt.issue.client.ui.IssueListView;
import com.edatasite.workforce.gwt.project.client.ui.BookingItemsListView;
import com.edatasite.workforce.gwt.project.client.ui.ContractListView;
import com.edatasite.workforce.gwt.project.client.ui.ProjectListView;
import com.edatasite.workforce.gwt.task.client.ui.TaskListView;
import com.edatasite.workforce.gwt.timesheet.client.ui.TimesheetApprovalListView;
import com.edatasite.workforce.gwt.timesheet.client.ui.view.FastTimesheet;
import com.edatasite.workforce.gwt.timesheet.client.ui.view.FastTimesheetCustom;
import com.edatasite.workforce.gwt.timesheet.client.ui.view.MonthlyTimesheetView;

import java.util.LinkedList;

public class PMSinksContainer extends SinksContainer implements PermissionConstants {

    public PMSinksContainer(String name, String description, Boolean isDo) {
        super(name, description, null, NONE);
    }

    protected void initViews() {

        if (Utils.hasPermission(PM_TASKS_LIST)) {
            addView(new TaskListView());
        }
        if (Utils.hasPermission(PM_ISSUE_LIST)) {
            addView(new IssueListView());
        }

        if (Utils.hasPermission(PM_TIMESHEET)) {
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_PROJECT_TO_TIMESHEET)) {
                addView(new FastTimesheetCustom());
            } else {
                addView(new FastTimesheet());
            }

            boolean isMonthlyTimesheetEnabled = Boolean.valueOf(Utils.userSettings.get(Constants.MONTHLY_TIMESHEET));
            if (isMonthlyTimesheetEnabled && Utils.hasPermission(PermissionConstants.MONTHLY_TIMESHEET)) {
                addView(new MonthlyTimesheetView());
            }
        }

        if (Utils.hasPermission(PM_TIMESHEET_APPROVAL)) {
            addView(new TimesheetApprovalListView());
        }

        if (Utils.hasPermission(PM_PROJECT_LIST)) {
            addView(new ProjectListView());
        }

        if (Utils.hasPermission(PM_CUSTOMER_LIST)) {
            addView(new NewClientListView(false));
        }
        if (!CompanyConstants.C22240.equals(Utils.getEncryptedCompanyID())) {
            if (Utils.hasPermission(PM_EMPLOYEE_LIST)) {
                addView(new EmployeeListView(EmployeeListView.FROM_PM));
            }
        }
        if (Utils.hasPermission(PM_BOOKING_ITEMS) && Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_BOOKING_ITEMS)) {
            addView(new BookingItemsListView(PM_CONTEXT));
        }

        if ("true".equals(Utils.userSettings.get(RESOURCE_UTILIZATION_ENABLED)) && Utils.hasPermission(PM_RESOURCE_UTILIZATION_LIST)) {
            addView(new ResourceUtilizationView(null));
        }
        if (Utils.isEmployeeAssignmentEnable() && Utils.hasPermission(PM_CONTRACT_LIST)) {
            addView(new ContractListView());
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EXPENSE_USE_AS_INTERNAL_INVOICE) && Utils.hasPermission(PM_PROJECT_EXPENSE_CLAIMS) && Utils.hasPermission(ACCOUNTING_MAIN_MENU)) {
            addView(new ExpenseListView(false));
        }

        if (Utils.properties.size() > 0) {
            for (PropertyItem item : Utils.properties.values()) {
                if (item.getfID() != null && item.isCustom() && Utils.hasPermission(item.getFormID() + "_" + Utils.getCompanyID())) {
                    if (Constants.PAGE.equals(item.getType())) {
                        if (item.getSelectedItemID() != null && Utils.hasPermission(item.getFormID() + "_SUMMARY_" + Utils.getCompanyID())) {
                            addView(new CustomFormItemView(item.getSelectedItemID(), item.getfID(), item.getFormID(), item.getPlural(), true));
                        } else if (item.getSelectedItemID() != null && Utils.hasPermission(item.getFormID() + "_EDIT_" + Utils.getCompanyID()) || Utils.hasPermission(item.getFormID() + "_ADD_" + Utils.getCompanyID())) {
                            addView(new AddCustomFormItemView(item.getSelectedItemID(), item.getfID(), item.getFormID(), item.getPlural(), true));
                        }
                    } else {
                        addView(new CustomFormItemListView(item.getfID(), item.getPlural(), item.getFormID()));
                    }
                }
            }
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
