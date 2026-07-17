package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeListView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Sherali Pirnafasov
 * Email: sherali.pirnafasov@gmail.com
 * Date: 9/30/11
 * Time: 5:33 PM
 */
public class PayrollEmployeeListView extends EmployeeListView {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();


    public PayrollEmployeeListView(String fromView) {
        super(fromView);
    }

    @Override
    protected Anchor getEmployeeActions(final EmployeeListItem item) {
        int actionItemCount = 0;
        MenuBar menuBar = new MenuBar(true);
        final String employeeName = item.getFirstName() + " " + item.getLastName();
        //================== Payroll Actions =========================//
        //payroll settings
        if (Utils.hasPermission(PermissionConstants.PAYROLL_EMPLOYEES_PAYROLL_SETTINGS)) {
            MenuPopItem payrollSettingItem = new MenuPopItem(wfmStrings.summaryView(), "payroll-settings");
            payrollSettingItem.setCommand(() -> {
                if (item.getEmployeeTemplateID() != null && !Utils.adminOrDirector() && !Utils.hasPermission(PAYROLL_EMPLOYEE_EDIT)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("starter|summary/" + item.getEmployeeTemplateID() + "/fromTemplate/view/" + item.getStatus(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                } else {
                    SinksContainerFactory.entryPoint.onHistoryChanged("starter|summary/" + item.getObjectID() + "/fromEmployeeList/view/" + item.getStatus(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                }
            });
            actionItemCount++;
            menuBar.addItem(payrollSettingItem);
        }
        //edit payroll settings
        MenuPopItem employeeEdit = new MenuPopItem(wfmStrings.edit(), "icon-client-edit-small");
        employeeEdit.ensureDebugId("editPayrollSettings");
        employeeEdit.setCommand(() -> {
            if (item.getEmployeeTemplateID() != null && !Utils.adminOrDirector()) {
                SinksContainerFactory.entryPoint.onHistoryChanged("starter|edit/" + item.getEmployeeTemplateID() + "/fromTemplate", item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged("starter|edit/" + item.getObjectID() + "/fromEmployeeList", item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
            }
        });
        actionItemCount++;
        menuBar.addItem(employeeEdit);
        //payslips
        if (Utils.hasPermission(PermissionConstants.PAYROLL_EMPLOYEES_PAYSLIPS)) {
            MenuPopItem payslips = new MenuPopItem(wfmStrings.payslips(), "payslip-16");
            payslips.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("payrollEmployeeView|" + PAYSLIP_LIST + "/" + item.getObjectID() + "/" + employeeName));
            actionItemCount++;
            menuBar.addItem(payslips);
        }

        final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}