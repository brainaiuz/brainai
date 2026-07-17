package com.edatasite.workforce.gwt.hrms.client.factory;

import com.edatasite.workforce.gwt.accounting.client.history.report.ClickedReportHistoryProcessor;
import com.edatasite.workforce.gwt.assessment.client.ui.history.*;
import com.edatasite.workforce.gwt.assessment.client.ui.view.*;
import com.edatasite.workforce.gwt.availability.client.history.*;
import com.edatasite.workforce.gwt.availability.client.ui.view.*;
import com.edatasite.workforce.gwt.availability.client.ui.view.editFingerprint.FingerprintAdjustmentPopup;
import com.edatasite.workforce.gwt.client.client.history.ClientHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.DynamicSinksContainer;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.form.AddCustomFormItemView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemListView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemView;
import com.edatasite.workforce.gwt.core.client.history.*;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.GoalsListView;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.view.BenefitRequestListView;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.CrmTaskListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.EventListView;
import com.edatasite.workforce.gwt.employee.client.ImportEmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.history.BackupsEmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.history.EmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.history.SingleEmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeListView;
import com.edatasite.workforce.gwt.expenses.client.history.ExpensePaymentViewHistoryProcessor;
import com.edatasite.workforce.gwt.googlecalendar.client.history.EventHistoryProcessor;
import com.edatasite.workforce.gwt.googlecalendar.client.history.GoogleCalendarHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.Hrms;
import com.edatasite.workforce.gwt.hrms.client.HrmsDashboardSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.HrmsDocumentsSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.OnboardingSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.history.*;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileEnum;
import com.edatasite.workforce.gwt.hrms.client.ui.*;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.CandidatesListView;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.PlacementListView;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.VacancyListView;
import com.edatasite.workforce.gwt.hrms.client.ui.talentprofile.TalentProfileListView;
import com.edatasite.workforce.gwt.issue.client.history.IssueHistoryProcessor;
import com.edatasite.workforce.gwt.location.client.history.LocationHistoryProcessor;
import com.edatasite.workforce.gwt.meetingMinutes.client.history.MeetingMinutesHistoryProcessor;
import com.edatasite.workforce.gwt.meetingMinutes.client.ui.MeetingMinutesListView;
import com.edatasite.workforce.gwt.messagecenter.client.history.EmailComposeHistoryProcessor;
import com.edatasite.workforce.gwt.messagecenter.client.history.EmailHistoryProcessor;
import com.edatasite.workforce.gwt.news.client.history.NewsHistoryProcessor;
import com.edatasite.workforce.gwt.news.client.news.NewsListView;
import com.edatasite.workforce.gwt.payroll.client.history.SinglePayrunHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.ui.view.SinglePayrunListView;
import com.edatasite.workforce.gwt.profile.client.history.CustomizationSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.project.client.history.ImportProjectHistoryProcessor;
import com.edatasite.workforce.gwt.project.client.history.ProjectHistoryProcessor;
import com.edatasite.workforce.gwt.project.client.ui.ProjectListView;
import com.edatasite.workforce.gwt.task.client.history.MultiTaskHistoryProcessor;
import com.edatasite.workforce.gwt.task.client.history.TaskHistoryProcessor;
import com.edatasite.workforce.gwt.task.client.history.WorkstreamHistoryProcessor;
import com.edatasite.workforce.gwt.team.client.history.TeamHistoryProcessor;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * User: Ruslan Muhammadov
 * Date: 13.11.2008
 * Time: 20:05:15
 */

public class HrmsSinksContainerFactory extends SinksContainerFactory implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    public Integer id;
    private SinksContainer selectedContener;
    private boolean isFirstContener = true;

    public HrmsSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
        if (Utils.hasPermission(PermissionConstants.HRMS_SECTION)) {
            setDefaultContainer(HRMS_MAIN);
        } else {
            setDefaultContainer("hrmsemployee");
        }
    }

    @Override
    public void initDefaultContainers() {
        if (!Hrms.dashboards.isEmpty()) {
            HrmsDashboardSinksContainer dashboardContainer = new HrmsDashboardSinksContainer();
            dashboardContainer.setPreparedView("dashboard_" + dashboardContainer.normalizeName(Hrms.dashboards.get(0).getName()));
            //dashboardContainer.setPreparedView("dashboard_" + dashboardContainer.normalizeName(Hrms.defaultDashboardName));
            putContainer(dashboardContainer);
            setDefaultContainer(dashboardContainer.getName());
            setDashboardContainer(dashboardContainer);
        }

        if (Utils.getPropertyListingMap() != null && !Utils.getPropertyListingMap().isEmpty()) {
            setHRMSPropertyListingsMap(Utils.getPropertyListingMap());
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_ONBOARDING_MANAGEMENT)) {
            SinksContainer onboard = new OnboardingSinksContainer("onboarding", wfmStrings.onboarding(), null);
            onboard.setPreparedView(ONBOARDING_STEP);
            setSinksContainer(onboard);
            if (selectedContener == null) {
                selectedContener = onboard;
                setSelection(selectedContener);
            }
        }

        if (Utils.isHrmsDocumentEnabled() && Utils.hasPermission(PermissionConstants.HRMS_DOCUMENTS_MANAGEMENT)) {
            SinksContainer hrmsDocuments = new HrmsDocumentsSinksContainer("documents", wfmStrings.documents());
            hrmsDocuments.setPreparedView("hrmsdocuments");
            setSinksContainer(hrmsDocuments);
            if (selectedContener == null) {
                selectedContener = hrmsDocuments;
                setSelection(selectedContener);
            }
        }

        //As Munir asked we need to open second container if dashboard has only one view
        if (Hrms.dashboards.size() == 1) {
            openSecondContainer();
        }
    }

    @Override
    public void registerProcessors() {
        //register history processors
        registerHistoryProcessor(SEARCH, new SearchHistoryProcessor());// History processor for search tab
        registerHistoryProcessor(TASK, new TaskHistoryProcessor());
        registerHistoryProcessor(PROJECT, new ProjectHistoryProcessor());
        registerHistoryProcessor(WORKSTREAM, new WorkstreamHistoryProcessor());
        registerHistoryProcessor("multitask", new MultiTaskHistoryProcessor());
        registerHistoryProcessor("client", new ClientHistoryProcessor(Constants.PROJECT_MANAGEMENT_PAGE));
        registerHistoryProcessor("dependent", new DependentHistoryProcessor());
        registerHistoryProcessor("groupgoal", new GroupGoalHistoryProcessor());
        registerHistoryProcessor("goal", new GoalHistoryProcessor());
        registerHistoryProcessor("goaledit", new GoalEditHistoryProcessor());
        registerHistoryProcessor("projectgoal", new ProjectGoalHistoryProcessor());
        registerHistoryProcessor("departmentgoal", new DepartmentGoalHistoryProcessor());
        registerHistoryProcessor("busingoal", new BusinGoalHistoryProcessor());
        registerHistoryProcessor("companygoal", new CompanyGoalHistoryProcessor());
        registerHistoryProcessor("goalWeightEdit", new GoalWeightEditHistoryProcessor());
        registerHistoryProcessor(DEPARTMENT, new TeamHistoryProcessor());

        registerHistoryProcessor("employeeProfile", new EmployeeProfileHistoryProcessor());
        registerHistoryProcessor("editemployeeProfile", new EmployeeProfileEditHistoryProcessor());

        registerHistoryProcessor("hrmsemployee", new HrmsEmployeeHistoryProcessor());

        registerHistoryProcessor("expenseReports", new HrmsExpenseReportHistoryProcessor());
        registerHistoryProcessor("employeeLeaveTypes", new EmployeeLeaveTypesHistoryProcessor());
        registerHistoryProcessor("employeeDocument", new EmployeeDocumentHistoryProcessor());
        registerHistoryProcessor("companyDocument", new CompanyDocumentsHistoryProcessor());

        registerHistoryProcessor("employee", new EmployeeHistoryProcessor());
        registerHistoryProcessor("singleemployee", new SingleEmployeeHistoryProcessor());
        registerHistoryProcessor("issue", new IssueHistoryProcessor());

        //Attendance
        registerHistoryProcessor("availability", new AvailabilityHistoryProcessor());
        registerHistoryProcessor("benefitRequest", new BenefitRequestHistoryProcessor());
        registerHistoryProcessor("employee", new EmployeeHistoryProcessor());
        registerHistoryProcessor("leaverequest", new LeaveRequestViewHistoryProcessor());
        registerHistoryProcessor("shift", new ShiftHistoryProcessor());
        registerHistoryProcessor("brigada", new BrigadaHistoryProcessor());
        registerHistoryProcessor("attendanceReport", new AttendanceReportHistoryProcessor());
        //Recruitment
        registerHistoryProcessor("vacancy", new VacancyHistoryProcessor());
        registerHistoryProcessor("candidate", new CandidateHistoryProcessor());
        registerHistoryProcessor("importcandidate", new CandidateImportHistoryProcessor());
        registerHistoryProcessor("candidateedit", new CandidateEditHistoryProcessor());
        registerHistoryProcessor(POSITION1, new HrmsPositionHistoryProcessor());
//        registerHistoryProcessor("candidateSelect", new SelectCandidateHistoryProcessor());
        registerHistoryProcessor("placement", new PlacementHistoryProcessor());
        registerHistoryProcessor(GROUP_PLACEMENT, new GroupPlacementHistoryProcessor());
        registerHistoryProcessor("event", new EventHistoryProcessor());
        registerHistoryProcessor("rotation", new RotationHistoryProcessor());
        //Onboarding
        registerHistoryProcessor(ONBOARDING_PERIOD, new OnboardingPeriodHistoryProcessor());
        registerHistoryProcessor(ONBOARDING_STEP, new OnboardingStepHistoryProcessor());
        registerHistoryProcessor(ONBOARDING_CHECK, new OnboardingCheckListHistoryProcessor());
        registerHistoryProcessor(EMPLOYEE_STEP, new EmployeeStepHistoryProcessor());
        //for Xenel do not delete

        registerHistoryProcessor("news", new NewsHistoryProcessor());
        registerHistoryProcessor("certificate", new CertificateHistoryProcessor());
        registerHistoryProcessor("customizeCertificate", new CustomizeCertificateHistoryProcessor());
        registerHistoryProcessor("importemployee", new ImportEmployeeHistoryProcessor());
        registerHistoryProcessor("cashAdvance", new CashAdvanceHistoryProcessor());
        registerHistoryProcessor("telegramchatedit", new TelegramChatEditHistoryProcessor());
        registerHistoryProcessor("employeeleaverequest", new EmployeeLeaveRequestHistoryProcessor());
        registerHistoryProcessor("meetingMinutes", new MeetingMinutesHistoryProcessor());
        registerHistoryProcessor("incident", new IncidentHistoryProcessor());
        registerHistoryProcessor("initiate", new InitiateHistoryProcessor());
        registerHistoryProcessor("addTemplate", new TemplateHistoryProcessor());

        registerHistoryProcessor(TalentProfileEnum.EDUCATION.name().toLowerCase(), new EducationHistoryProcessor());
        registerHistoryProcessor(TalentProfileEnum.AWARD.name().toLowerCase(), new AwardHistoryProcessor());

        registerHistoryProcessor("addSkill", new SkillHistoryProcessor());
        registerHistoryProcessor("editskill", new SkillEditHistoryProcessor());
        registerHistoryProcessor("performancenote", new NoteHistoryProcessor());
        registerHistoryProcessor("assessment", new AssessHistoryProcessor());
        registerHistoryProcessor("appraisal_approval", new AppraisalApprovalHistoryProcessor());
        registerHistoryProcessor("calendar", new GoogleCalendarHistoryProcessor());
        registerHistoryProcessor(ITEM_LIST, new CustomFormItemHistoryProcessor());
        registerHistoryProcessor("customizationSettings", new CustomizationSettingsHistoryProcessor());
        registerHistoryProcessor("email", new EmailHistoryProcessor());
        registerHistoryProcessor("singlePayrun", new SinglePayrunHistoryProcessor());
        registerHistoryProcessor("expensepayment", new ExpensePaymentViewHistoryProcessor());
        registerHistoryProcessor("clickedreport", new ClickedReportHistoryProcessor());
        registerHistoryProcessor("emailcompose", new EmailComposeHistoryProcessor());
        registerHistoryProcessor("location", new LocationHistoryProcessor());
        registerHistoryProcessor("positionsummary", new PositionSummaryViewHistoryProcessor());
        registerHistoryProcessor("positions", new HrmsPositionHistoryProcessor());
        registerHistoryProcessor("webhooklist", new WebhookListHistoryProcessor());
        registerHistoryProcessor("webhook", new WorkflowWebHookHistoryProcessor());
        registerHistoryProcessor("webhookEdit", new WorkflowWebHookEditHistoryProcessor());
        registerHistoryProcessor("backupsEmployee", new BackupsEmployeeHistoryProcessor());
        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_LIST)) {
            registerHistoryProcessor("importproject", new ImportProjectHistoryProcessor());
        }
        registerHistoryProcessor("attendanceTerminal", new AttendanceTerminalHistoryProcessor());
    }

    @Override
    public void registerMenuItems() {

        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EMPLOYEE)) {
            addNewMenuItem(wfmStrings.employee(), "singleemployee|add/add/" + FROM_HRMS);
        }

        //register leave request (add new) Attendance //tod o - Robert's company MServices
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_REQUEST)) {
            addNewMenuItem(wfmStrings.leaveRequest(), "availability|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_LEAVE_PLANNER)) {
            addNewMenuItem(wfmStrings.leavePlanner(), "availability|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_EXPENCE_REPORT)) {
            addNewMenuItem(wfmStrings.expense(), "expenseReports|add/add");
        }
        //register recruitment add news
        if (Utils.hasPermission(PermissionConstants.HRMS_RECRUITMENT)) {
            //register vacancy (add new)
            if (Utils.hasPermission(PermissionConstants.HRMS_ADD_VACANCY)) {
                addNewMenuItem(wfmStrings.vacancy(), "vacancy|add/add");
            }
            //register candidate (add new)
            if (Utils.hasPermission(PermissionConstants.HRMS_ADD_CANDIDATE)) {
                addNewMenuItem(wfmStrings.candidate(), "candidate|add/add");
            }
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_COMPANY_NEWS_ADD)) {
            addNewMenuItem(wfmStrings.companyNews(), "news|add/add");
        }

        if (Utils.hasPermission(PermissionConstants.ADD_BENEFIT_REQUEST)) {
            addNewMenuItem(hrmsStrings.benefitRequest(), "benefitRequest|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_BACKUPS_EMPLOYEE_ADD)) {
            addNewMenuItem(wfmStrings.backupEmployee(), "backupsEmployee|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_ATTENDANCE_TABLE_DATA)) {
            addNewMenuItem(wfmStrings.timer(), event -> new FingerprintAdjustmentPopup().open());
        }
    }

    private void setHRMSPropertyListingsMap(LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap) {

        for (SelectItem selectItem : propertyListingsMap.keySet()) {
            LinkedList<View> viewList = new LinkedList<>();
            if (ModuleEnum.HRMS.getCode().equals(selectItem.getDescription())) {
                LinkedList<PropertyItem> propertyItemList = propertyListingsMap.get(selectItem);
                for (PropertyItem propertyItem : propertyItemList) {
                    if (propertyItem != null) {
                        switch (propertyItem.getObjectName()) {
                            case EMLOYEE_LIST:
                                viewList.add(new EmployeeListView(EmployeeListView.FROM_HRMS));
                                break;
                            case EMPLOYEE_DOCUMENTS:
                                if (propertyItem.getContainer() != null && "employee".equals(propertyItem.getContainer().getCode())) {
                                    viewList.add(new EmployeeDocumentsListView(Utils.getUserID(), false));
                                } else {
                                    viewList.add(new EmployeeDocumentsListView(null, false));
                                }
                                break;
                            case COMPANY_DOCUMENTS:
                                viewList.add(new CompanyDocumentsListView());
                                break;
                            case BENEFIT_REQUESTS:
                                viewList.add(new BenefitRequestListView(null));
                                break;
                            case "meeting":
                                viewList.add(new MeetingMinutesListView(true));
                                break;
                            case TASK:
                                viewList.add(new CrmTaskListView(null, null, false));
                                break;
                            case PROJECT:
                                viewList.add(new ProjectListView(null, null, "HRMS"));
                                break;
                            case CERTIFICATES_LIST:
                                viewList.add(new CertificatesListView());
                                break;
                            case "incidentList":
                                if (propertyItem.getContainer() != null && "employee".equals(propertyItem.getContainer().getCode())) {
                                    viewList.add(new IncidentListView(Utils.getUserID()));
                                } else {
                                    viewList.add(new IncidentListView(true));
                                }
                                break;
                            case "organizationChart":
                                viewList.add(new OrgChartView());

                                break;
                            case "departmentOrgChartView":
                                viewList.add(new DepartmentOrgChartView());
                                break;
                            case NEW_FLAME_ORG_CHART:
                                viewList.add(new OrgBoardView());
                                break;
                            case "notifications":
                                viewList.add(new NotificationListView());
                                break;
                            case NEWS_LIST:
                                viewList.add(new NewsListView());
                                break;
                            case EMPLOYEE_PROFILE_VIEW:
                                viewList.add(new NewHrmsEmployeeSummaryForm(Utils.getUserID(), false, "employee"));
                                break;
                            case "leave_request_list":
                            case LEAVE_REQUEST_LIST:
                                if (Utils.hasPermission(PermissionConstants.HRMS_LIVE_REQUEST)) {
                                    viewList.add(new LeaveRequestListView());
                                }
                                break;
                            case "leave_planner_list":
                                if (Utils.hasGenericAccess(GenericSettingsEnum.HRMS_LEAVE_PLANNER)) {
                                    viewList.add(new LeavePlannerListView(this));
                                }
                                break;
                            case "my_attendance":
                                if (propertyItem.getContainer() != null && "availability".equals(propertyItem.getContainer().getCode())) {
                                    viewList.add(new GlobalEmployeeLeaveRequestView(null, Utils.getUserID(), null, "availability", wfmStrings.myAttendance(), GlobalEmployeeLeaveRequestView.FROM_IN_OUT_ATTENDANCE_TRACKING_VIEW));
                                } else {
                                    viewList.add(new GlobalEmployeeLeaveRequestView(null, null, "hrmsleaveRequests", (wfmStrings.leaveRequests())));
                                }
                                break;
                            case EXPENSES_CLAIM:
                                viewList.add(new HrmsExpenseReportListView(Utils.getUserID()));

                                break;
                            case CASH_ADVANCE_LIST:
                                viewList.add(new CashAdvanceListView(Utils.getUserID(), true));
                                break;
                            case PAYSLIP_LIST:
                                viewList.add(new EmployeePayslipListView(Utils.getUserID()));
                                break;
                            case DEPENDENT_LIST:
                                viewList.add(new DependentListView(Utils.getUserID(), false));
                                break;
                            case PERSONAL_GOAL + GOAL:
                                if (propertyItem.getContainer() != null && "employee".equals(propertyItem.getContainer().getCode())) {
                                    viewList.add(new EmployeesGoalListView(Utils.getUserID()));
                                } else {
                                    viewList.add(new GoalsListView());
                                }
                                break;
                            case TALENT_PROFILE:
                                viewList.add(new TalentProfileListView(Utils.getUserID(), false));
                                break;
                            case TEAM_AVAILABILITY_VIEW:
                                viewList.add(new TeamAvailabilityView());
                                break;
                            case "attendanceReport":
                                viewList.add(new AttendanceReportView2());
                                break;
                            case "attendanceMarksList":
                                viewList.add(new AttendanceMarksListView());
                                break;
                            case "terminalAttendance":
                                viewList.add(new TerminalAttendance());
                                break;
                            case "shift":
                                viewList.add(new ShiftListView());
                                break;
                            case "brigada":
                                viewList.add(new BrigadaListView());
                                break;
                            case "rotation":
                                viewList.add(new RotationListView());
                                break;
                            case "annualLeaveBalance":
                                if (Utils.isEnableProrataBasedAnnualLeave()) {
                                    viewList.add(new AnnualLeaveBalanceReportProrataBased());
                                } else {
                                    viewList.add(new AnnualBalanceReport());
                                }
                                break;
                            case VACANCY:
                                viewList.add(new VacancyListView());
                                break;
                            case CANDIDATE:
                                viewList.add(new CandidatesListView());
                                break;
                            case PLACEMENT:
                                viewList.add(new PlacementListView());
                                break;
                            case GROUP_PLACEMENT:
                                viewList.add(new GroupPlacementListView());
                                break;
                            case POSITION1:
                                viewList.add(new PositionListView());
                                break;
                            case EVENT_LIST:
                                viewList.add(new EventListView(null));
                                break;
                            case GROUP_GOAL:
                                viewList.add(new GroupGoalListView());
                                break;
                            case DEPARTMENT_GOAL:
                                viewList.add(new GoalsListView(DEPARTMENT_GOAL));
                                break;
                            case PROJECT_GOAL:
                                viewList.add(new GoalsListView(PROJECT_GOAL));
                                break;
                            case BUSINESS_GOAL + GOAL:
                                viewList.add(new GoalsListView(BUSINESS_GOAL));
                                break;
                            case COMPANY_GOAL + GOAL:
                                viewList.add(new CompanyGoalListView());
                                break;
//                            case "simpleAppraisal":
//                                viewList.add(new SimpleAppraisalsHomeView());
//                                break;
                            case "appraisalsArchive":
                                viewList.add(new NewSimpleAppraisalsListView());
                                break;
                            case "appraisalTemplate":
                                viewList.add(new AppraisalTemplateListView());
                                break;
                            case "competencesView":
                                viewList.add(new CompetencyListView());
                                break;
                            case "competencesGroupView":
                                viewList.add(new CompetencyGroupListView());
                                break;
                            case "performanceNote":
                                viewList.add(new PerformanceNoteListView());
                                break;
                            case "singlePayrunList":
                                viewList.add(new SinglePayrunListView());
                                break;
                            case "subscriptionvendors":
                                viewList.add(new VendorListView());
                                break;
                            case "subscriptions":
                                viewList.add(new SubscriptionListView());
                                break;
                            case "subscriptionusages":
                                viewList.add(new SubscriptionUsageListView());
                                break;
                            case BACKUPS_EMPLOYEE:
                                viewList.add(new BackupsEmployeeListView());
                                break;
                            default:
                                if (propertyItem.isCustom()) {
                                    if (Constants.PAGE.equals(propertyItem.getType())) {
                                        if (propertyItem.getSelectedItemID() != null && Utils.hasPermission(propertyItem.getFormID() + "_SUMMARY_" + Utils.getCompanyID())) {
                                            viewList.add(new CustomFormItemView(propertyItem.getSelectedItemID(), propertyItem.getfID(), propertyItem.getFormID(), getLocalizedPlural(propertyItem), true));
                                        } else if (propertyItem.getSelectedItemID() != null && Utils.hasPermission(propertyItem.getFormID() + "_EDIT_" + Utils.getCompanyID()) || Utils.hasPermission(propertyItem.getFormID() + "_ADD_" + Utils.getCompanyID())) {
                                            viewList.add(new AddCustomFormItemView(propertyItem.getSelectedItemID(), propertyItem.getfID(), propertyItem.getFormID(), getLocalizedPlural(propertyItem), true));
                                        }
                                    } else {
                                        viewList.add(new CustomFormItemListView(propertyItem.getfID(), getLocalizedPlural(propertyItem), propertyItem.getFormID()));
                                    }
                                }
                        }
                    }
                }
            }

            if ("availability".equals(selectItem.getCode()) && Utils.hasPermission(PermissionConstants.HRMS_ATTENDANCE_MARKS)) {
                viewList.add(new AttendanceMarksListView());
            }
            if ("availability".equals(selectItem.getCode()) && Utils.hasPermission(PermissionConstants.HRMS_ATTENDANCE_TERMINAL_LIST)) {
                viewList.add(new AttendanceTerminalListView());
            }

            DynamicSinksContainer dynamicSC = new DynamicSinksContainer(selectItem.getCode(), selectItem.getName(), viewList);
            dynamicSC.setPreparedView(selectItem.getCategory());
            if (isFirstContener) {
                selectedContener = dynamicSC;
                setSelection(selectedContener);
                isFirstContener = false;
            }
            setSinksContainer(dynamicSC);
        }
    }

    private String getLocalizedPlural(PropertyItem propertyItem) {
        if (propertyItem.getlPlural() != null) {
            switch (Utils.getUserLanguage()) {
                case "en":
                    return propertyItem.getlPlural().getEnglishName();
                case "ar":
                    return propertyItem.getlPlural().getArabicName();
                case "ru":
                    return propertyItem.getlPlural().getRussianName();
                case "uz":
                    return propertyItem.getlPlural().getUzbekName();
            }
        }
        return propertyItem.getPlural();
    }
}
