package com.edatasite.workforce.gwt.expenses.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.expenses.client.ui.view.report.ExpenseAddEditView;

import java.util.LinkedList;

public class ExpenseAddSinksContainer extends SinksContainer {

    public ExpenseAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (Utils.isAccounting() && Utils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_ADD)
                || (Utils.isAccounting() && Utils.hasPermission(PermissionConstants.ACCOUNTING_COMPANY_EXPENSE_ADD)
                || (Utils.isHRMS() && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EXPENSE_CLAIM)))) {
            addView(new ExpenseAddEditView(params));
        } else if (!(Utils.isHRMS() || Utils.isAccounting())) {
            addView(new ExpenseAddEditView(params));
        }
    }
}
