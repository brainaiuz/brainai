package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.assessment.client.ui.view.NewSimpleAppraisalsListView;
import com.edatasite.workforce.gwt.availability.client.ui.view.GlobalEmployeeLeaveRequestView;
import com.edatasite.workforce.gwt.availability.client.ui.view.IncidentListView;
import com.edatasite.workforce.gwt.availability.client.ui.view.TerminalAttendanceEmployeeTab;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WebHookResponseListView;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.SalaryHistoryListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.ChatListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.EventListView;
import com.edatasite.workforce.gwt.hrms.client.ui.AddEditOnboardingCheckView;
import com.edatasite.workforce.gwt.hrms.client.ui.CertificatesListView;
import com.edatasite.workforce.gwt.hrms.client.ui.DependentListView;
import com.edatasite.workforce.gwt.hrms.client.ui.EmployeeDocumentsListView;
import com.edatasite.workforce.gwt.hrms.client.ui.EmployeePayslipListView;
import com.edatasite.workforce.gwt.hrms.client.ui.EmployeesGoalListView;
import com.edatasite.workforce.gwt.hrms.client.ui.HrmsExpenseReportListView;
import com.edatasite.workforce.gwt.hrms.client.ui.LogHistoryListView;
import com.edatasite.workforce.gwt.hrms.client.ui.NewHrmsEmployeeSummaryForm;
import com.edatasite.workforce.gwt.hrms.client.ui.talentprofile.TalentProfileListView;

import java.util.LinkedList;

/**
 * User: unni
 * Date: Oct 26, 2009
 * Time: 8:46:10 PM
 */
public class EmployeeProfileViewSinksContainer extends SinksContainer {


    public EmployeeProfileViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new NewHrmsEmployeeSummaryForm(id, true, "employee"));


        String selectedYear = null;
        if (params.length > 1) {
            selectedYear = params[1];
        }
        String description = (wfmStrings.leaveRequests());
        String name = "hrmsleaveRequests";
        if (!Utils.isSettings() && Utils.hasPermission(PermissionConstants.HRMS_LIVE_REQUEST)) {
            super.addView(selectedYear != null ? new GlobalEmployeeLeaveRequestView(null, id, selectedYear, name, description, null) :
                    new GlobalEmployeeLeaveRequestView(null, id, name, description));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_TERMINAL_REPORT)) {
            addView(new TerminalAttendanceEmployeeTab(id));
        }
        if (!Utils.isSettings() && Utils.hasPermission(PermissionConstants.HRMS_SINGLE_PAYRUN_LIST)) {
            addView(new EmployeePayslipListView(this.id));
        }
        if (!Utils.isSettings() && Utils.hasPermission(PermissionConstants.ADD_SALARY_HISTORY)) {
            addView(new SalaryHistoryListView(this.id));
        }
        //employee expense reports
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
        if (Utils.hasPermission(PermissionConstants.HRMS_SIMPLE_APPRAISALS)) {
            addView(new NewSimpleAppraisalsListView(this.id));
        }
        //employee goals
        if (!Utils.isSettings() && Utils.hasPermission(PermissionConstants.HRMS_PERSONAL_GOALS)) {
            super.addView(new EmployeesGoalListView(id));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_LIST)) {
            addView(new TalentProfileListView(this.id, false));
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_WHATSAPP)) {
            addView(new ChatListView(id, "employee"));
        }

        //employee incident list
        if (Utils.hasPermission(PermissionConstants.HRMS_INCIDENT_LIST)) {
            addView(new IncidentListView(this.id));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_DEPENDENT)) {
            addView(new DependentListView(id, false));
        }

        if (Utils.hasPermission(PermissionConstants.WEBHOOK_RESPONSE_TAB_VIEW)) {
            addView(new WebHookResponseListView(this.id, RelationItem.TYPE_EMPLOYEE));
        }

        //employee onboarding checklist
        if (!Utils.isSettings() && (Utils.hasPermission(PermissionConstants.HRMS_ONBOARDING_CHECKLIST_EDIT) || Utils.hasPermission(PermissionConstants.HRMS_ONBOARDING_CHECKLIST_VIEW))) {
            super.addView(new AddEditOnboardingCheckView(id));
        }
        if (!Utils.isSettings() && Utils.hasPermission(PermissionConstants.HRMS_VIEW_EMPLOYEE_CHANGE_LOG)) {
            addView(new LogHistoryListView(this.id));
        }
        if (id != null) {
            addDynamicView(CustomFieldLookUpTypeEnum.EMPLOYEE, id);
        }
    }
}