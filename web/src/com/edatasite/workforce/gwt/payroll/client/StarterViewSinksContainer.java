package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.GlobalEmployeeLeaveRequestView;
import com.edatasite.workforce.gwt.availability.client.ui.view.IncidentListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.BenefitRequestListView;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceListView;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.SalaryHistoryListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.EventListView;
import com.edatasite.workforce.gwt.hrms.client.ui.AddEditOnboardingCheckView;
import com.edatasite.workforce.gwt.hrms.client.ui.CertificatesListView;
import com.edatasite.workforce.gwt.hrms.client.ui.DependentListView;
import com.edatasite.workforce.gwt.hrms.client.ui.EmployeeDocumentsListView;
import com.edatasite.workforce.gwt.hrms.client.ui.EmployeesGoalListView;
import com.edatasite.workforce.gwt.hrms.client.ui.HrmsExpenseReportListView;
import com.edatasite.workforce.gwt.hrms.client.ui.LogHistoryListView;
import com.edatasite.workforce.gwt.hrms.client.ui.talentprofile.TalentProfileListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.AdditionalPaymentItemListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollEmployeeEditForm;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollEmployeeViewForm;
import com.edatasite.workforce.gwt.payroll.client.ui.view.SinglePayrunListView;

import java.util.LinkedList;

public class StarterViewSinksContainer extends SinksContainer {

    public StarterViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params[1].equals("fromTemplate")) {
            if (params.length > 2) {
                addView(new PayrollEmployeeViewForm(id, true, params[3]));
            } else {
                addView(new PayrollEmployeeEditForm(id, true));
            }
        } else {
            if (params.length > 2) {
                addView(new PayrollEmployeeViewForm(id));
            } else {
                addView(new PayrollEmployeeEditForm(id));
            }
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_LIST)) addView(new SinglePayrunListView(id));

        if (Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_LIST))
            addView(new CashAdvanceListView(id, false, true));

        if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST))
            addView(new AdditionalPaymentItemListView(id));

        if (Utils.hasPermission(PermissionConstants.MY_BENEFIT_REQUEST_LIST)) {
            addView(new BenefitRequestListView(id));
        }


        String description = (wfmStrings.leaveRequests());
        String name = "hrmsleaveRequests";
        if (!Utils.isSettings() && Utils.hasPermission(PermissionConstants.HRMS_LIVE_REQUEST)) {
            addView(new GlobalEmployeeLeaveRequestView(null, id, name, description));
        }

        if (!Utils.isSettings() && (Utils.hasPermission(PermissionConstants.HRMS_EXPENCE_REPORT))) {
            super.addView(new HrmsExpenseReportListView(id));
        }
        if (!Utils.isSettings() && Utils.hasPermission(PermissionConstants.EMPLOYEE_DOCUMENTS_LIST)) {
            addView(new EmployeeDocumentsListView(id, true));
        }
        if (Utils.hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_LIST)) {
            addView(new CertificatesListView(id));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_ACTIVITIES_VIEW)) {
            addView(new EventListView(null, id, RelationItem.TYPE_EMPLOYEE));
        }
        //employee goals
        if (!Utils.isSettings() && Utils.hasPermission(PermissionConstants.HRMS_PERSONAL_GOALS)) {
            super.addView(new EmployeesGoalListView(id));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_LIST)) {
            addView(new TalentProfileListView(this.id,false));
        }
        //employee incident list
        if (Utils.hasPermission(PermissionConstants.HRMS_INCIDENT_LIST)) {
            addView(new IncidentListView(this.id));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_DEPENDENT)) {
            addView(new DependentListView(id,false));
        }
        //employee onboarding checklist
        if (!Utils.isSettings() && (Utils.hasPermission(PermissionConstants.HRMS_ONBOARDING_CHECKLIST_EDIT) || Utils.hasPermission(PermissionConstants.HRMS_ONBOARDING_CHECKLIST_VIEW))) {
            super.addView(new AddEditOnboardingCheckView(id));
        }
        if (!Utils.isSettings() && Utils.hasPermission(PermissionConstants.HRMS_VIEW_EMPLOYEE_CHANGE_LOG)) {
            addView(new LogHistoryListView(this.id));
        }
        if (!Utils.isSettings() && Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_LIST)) {
            addView(new SalaryHistoryListView(this.id));
        }

        if (id != null) {
            addDynamicView(CustomFieldLookUpTypeEnum.EMPLOYEE, id);
        }

    }
}