package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.BenefitRequestListView;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceListView;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.AdditionalPaymentItemListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.AdditionalPaymentListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.EndOfServiceGratuityListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollEmployeeListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollEmployeeTemplateListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayslipTableListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.SinglePayrunListView;

import java.util.LinkedList;

public class PayrollSinksContainer extends SinksContainer {

    public PayrollSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        /*if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_OLD_PAYSLIPS) && Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_LIST)) {
            PayslipListView payslipListView = new PayslipListView();
            addView(payslipListView);
        }*/
        if (Utils.hasPermission(PermissionConstants.PAYROLL_EMPLOYEES_LIST)) {
            addView(new PayrollEmployeeListView(EmployeeListView.FROM_PAYROLL));
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_LIST)) {
            SinglePayrunListView singlePayrunListView = new SinglePayrunListView();
            addView(singlePayrunListView);
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_LIST)) {
            PayslipTableListView payslipListView = new PayslipTableListView();
            addView(payslipListView);
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_LIST)) {
            addView(new CashAdvanceListView());
        }
        if (Utils.hasPermission(PermissionConstants.MY_BENEFIT_REQUEST_LIST)) {
            addView(new BenefitRequestListView(this.id));
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST)) {
            addView(new AdditionalPaymentItemListView(null));
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_LIST)) {
            addView(new AdditionalPaymentListView());
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_PENDING_CHANGES)) {
            addView(new PayrollEmployeeTemplateListView());
        }
        if (Utils.isArabicCompany() && Utils.hasPermission(PermissionConstants.END_OF_SERVICE_GRATUITY_LIST)) {
            addView(new EndOfServiceGratuityListView());
        }
    }

}
