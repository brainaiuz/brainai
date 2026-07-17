package com.edatasite.workforce.gwt.payroll.client.factory;

import com.edatasite.workforce.gwt.accounting.client.history.report.ClickedReportHistoryProcessor;
import com.edatasite.workforce.gwt.availability.client.history.AvailabilityHistoryProcessor;
import com.edatasite.workforce.gwt.availability.client.history.BenefitRequestHistoryProcessor;
import com.edatasite.workforce.gwt.availability.client.history.IncidentHistoryProcessor;
import com.edatasite.workforce.gwt.availability.client.ui.view.GlobalEmployeeLeaveRequestView;
import com.edatasite.workforce.gwt.availability.client.ui.view.IncidentListView;
import com.edatasite.workforce.gwt.core.client.DynamicSinksContainer;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.form.AddCustomFormItemView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemListView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemView;
import com.edatasite.workforce.gwt.core.client.history.CustomFormItemHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.SearchHistoryProcessor;
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
import com.edatasite.workforce.gwt.core.client.ui.view.multiCashAdvance.MultiCashAdvanceHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.view.multiCashAdvance.MultiCashAdvanceListView;
import com.edatasite.workforce.gwt.core.client.ui.view.recurring.RecurringDeductionHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.view.recurring.RecurringPayDeductionListView;
import com.edatasite.workforce.gwt.core.client.ui.view.recurring.RecurringPaymentHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.view.recurring.custom.RecurringCustomDeductionHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.ImportEmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.history.EmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.history.SingleEmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeListView;
import com.edatasite.workforce.gwt.googlecalendar.client.history.GoogleCalendarHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.CertificateHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.DependentHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.EducationHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.EmployeeDocumentHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.GoalHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.HrmsExpenseReportHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileEnum;
import com.edatasite.workforce.gwt.hrms.client.ui.CertificatesListView;
import com.edatasite.workforce.gwt.hrms.client.ui.DependentListView;
import com.edatasite.workforce.gwt.hrms.client.ui.EmployeeDocumentsListView;
import com.edatasite.workforce.gwt.hrms.client.ui.EmployeesGoalListView;
import com.edatasite.workforce.gwt.messagecenter.client.history.EmailHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.ImportGroupPayrunHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.Payroll;
import com.edatasite.workforce.gwt.payroll.client.PayrollDashboardSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PayrollReportSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PayrollSettingsSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.history.AdditionalDeductionHisotryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.AdditionalPaymentHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.EmployerSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.EndOfServiceHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.GroupPayrunHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.ImportAdditionalPaymentHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.NiTaxCodeChangesHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.OvertimeHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.PayrollBatchHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.PayrollCategoryV2HistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.PayrollEmployeeViewHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.PayrollGlobalSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.PayrollPaymentHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.PayrollSettingsProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.PayrunPaymentHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.PayrunPaymentListHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.PensionProviderHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.PensionSchemeHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.SinglePayrunHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.StarterHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.ui.view.AdditionalPaymentItemListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.AdditionalPaymentListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.EndOfServiceGratuityListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.OvertimeListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollEmployeeListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollEmployeeTemplateListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayslipTableListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.SinglePayrunListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.report.cashAdvanceReport.CashAdvanceReportView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.report.endOfServiceReport.EndOfServiceReportView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.report.pensionReport.PensionContributionReportView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.report.salaryDetailedReport.SalaryDetailedReportView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.report.salaryReport.SalaryReportView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.report.wpsReport.WpsReportView;
import com.edatasite.workforce.gwt.profile.client.history.CustomizationSettingsHistoryProcessor;
import com.google.gwt.core.client.GWT;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.LOCALE_PAYROLL;


/**
 * User: Admin
 * Date: 13.10.2008
 * Time: 15:18:42
 */
public class PayrollSinksContainerFactory extends SinksContainerFactory {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private boolean isFirstContener = true;


    public PayrollSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
        setDefaultContainer("payroll");
    }

    @Override
    public void initDefaultContainers() {
        if (Payroll.dashboards.size() > 0) {
            PayrollDashboardSinksContainer dashboardSinksContainer = new PayrollDashboardSinksContainer();
            dashboardSinksContainer.setPreparedView("dashboard_" + dashboardSinksContainer.normalizeName(Payroll.defaultDashboardName));
            putContainer(dashboardSinksContainer);
            setDefaultContainer(dashboardSinksContainer.getName());
            setDashboardContainer(dashboardSinksContainer);
        }


//        if (Utils.hasPermission(PermissionConstants.PAYROLL_MAIN_CONTENT)) {
//            PayrollSinksContainer payroll = new PayrollSinksContainer("payroll", payrollStrings.payroll());
//            payroll.setPreparedView("employee");
//            setSinksContainer(payroll);
//        } else {
//            PayrollEmployeeViewSinksContainer mypayroll = new PayrollEmployeeViewSinksContainer("payroll", payrollStrings.myPayroll(), new String[]{String.valueOf(Utils.getUserID())});
//            mypayroll.setPreparedView("payroll");
//            setSinksContainer(mypayroll);
//        }
//        if (Utils.hasPermission(PermissionConstants.PAYROLL_REPORTS)) {
//            createPayrollReportSinksContainer();
//        }

        if (Utils.getPropertyListingMap() != null && Utils.getPropertyListingMap().size() > 0) {
            setPayrollPropertyListingsMap(Utils.getPropertyListingMap());
        }
        if (Payroll.dashboards.size() == 1) {
            openSecondContainer();
        }
    }

    public void createPayrollSettingsSinksContainer() {
        SinksContainer settings = new PayrollSettingsSinksContainer("settings", wfmStrings.settings());
        settings.setPreparedView("employersettings");
        setSinksContainer(settings);
    }

    public void createPayrollReportSinksContainer() {
        SinksContainer reports = new PayrollReportSinksContainer("report", wfmStrings.reports());
        reports.setPreparedView("wpsReport");
        setSinksContainer(reports);
    }

    @Override
    public void registerProcessors() {
        registerHistoryProcessor(SEARCH, new SearchHistoryProcessor());// History processor for search tab
        registerHistoryProcessor("payrollGlobalSettings", new PayrollGlobalSettingsHistoryProcessor());
        registerHistoryProcessor("payrollBatch", new PayrollBatchHistoryProcessor());
        registerHistoryProcessor("payrollCategory", new PayrollCategoryV2HistoryProcessor());
        registerHistoryProcessor("singlePayrun", new SinglePayrunHistoryProcessor());// Single Payrun processor
        registerHistoryProcessor("employee", new EmployeeHistoryProcessor());
        registerHistoryProcessor("importemployee", new ImportEmployeeHistoryProcessor());
        registerHistoryProcessor("importgrouppayrun", new ImportGroupPayrunHistoryProcessor());
        registerHistoryProcessor("starter", new StarterHistoryProcessor());
        registerHistoryProcessor("employersettings", new EmployerSettingsHistoryProcessor());
        registerHistoryProcessor("pensionscheme", new PensionSchemeHistoryProcessor());
        registerHistoryProcessor("pensionprovider", new PensionProviderHistoryProcessor());
        registerHistoryProcessor("payrollEmployeeView", new PayrollEmployeeViewHistoryProcessor());
        registerHistoryProcessor("nitaxcodechanges", new NiTaxCodeChangesHistoryProcessor());
//        registerHistoryProcessor("payrollpaymentcategory", new PayrollPaymentCategoryHistoryProcessor());
//        registerHistoryProcessor("importpaymentdeduction", new ImportPaymentDeductionHistoryProcessor());
        registerHistoryProcessor("singleemployee", new SingleEmployeeHistoryProcessor());
//        registerHistoryProcessor("payrolldeductioncategory", new PayrollDeductionCategoryHistoryProcessor());
//        registerHistoryProcessor("employercontribution", new EmployerContributionHistoryProcessor());
//        registerHistoryProcessor("paymentDeductionCategories", new PaymentDeductionCategoryListHistoryProcessor());
        registerHistoryProcessor("payrollSettings", new PayrollSettingsProcessor());
        registerHistoryProcessor("payslipTable", new GroupPayrunHistoryProcessor());
        registerHistoryProcessor("endOfService", new EndOfServiceHistoryProcessor());
        registerHistoryProcessor("cashAdvance", new CashAdvanceHistoryProcessor());
        registerHistoryProcessor("multiCashAdvance", new MultiCashAdvanceHistoryProcessor());
        registerHistoryProcessor("additionalPayment", new AdditionalPaymentHistoryProcessor());
        registerHistoryProcessor("importAdditionalPayment", new ImportAdditionalPaymentHistoryProcessor());
        registerHistoryProcessor("additionalDeduction", new AdditionalDeductionHisotryProcessor());
        registerHistoryProcessor("calendar", new GoogleCalendarHistoryProcessor());
        registerHistoryProcessor("email", new EmailHistoryProcessor());
        registerHistoryProcessor("benefitRequest", new BenefitRequestHistoryProcessor());
        registerHistoryProcessor(ITEM_LIST, new CustomFormItemHistoryProcessor());
        registerHistoryProcessor("customizationSettings", new CustomizationSettingsHistoryProcessor());
        registerHistoryProcessor("expenseReports", new HrmsExpenseReportHistoryProcessor());
        registerHistoryProcessor("goal", new GoalHistoryProcessor());
        registerHistoryProcessor("employeeDocument", new EmployeeDocumentHistoryProcessor());
        registerHistoryProcessor("certificate", new CertificateHistoryProcessor());
        registerHistoryProcessor("availability", new AvailabilityHistoryProcessor());
        registerHistoryProcessor(TalentProfileEnum.EDUCATION.name().toLowerCase(), new EducationHistoryProcessor());
        registerHistoryProcessor("incident", new IncidentHistoryProcessor());
        registerHistoryProcessor("dependent", new DependentHistoryProcessor());
        registerHistoryProcessor("payrunPayment", new PayrunPaymentHistoryProcessor());
        registerHistoryProcessor("payrollPayment", new PayrollPaymentHistoryProcessor());
        registerHistoryProcessor("payrunPaymentList", new PayrunPaymentListHistoryProcessor());
        registerHistoryProcessor("recurringDeduction", new RecurringDeductionHistoryProcessor());
        registerHistoryProcessor("recurringPayment", new RecurringPaymentHistoryProcessor());
        registerHistoryProcessor("recurringCustomDeduction", new RecurringCustomDeductionHistoryProcessor());
        registerHistoryProcessor("overtime", new OvertimeHistoryProcessor());
        registerHistoryProcessor("clickedreport", new ClickedReportHistoryProcessor());

    }

    public void registerMenuItems() {
        /*if (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(PM)) {
            addNewMenuItem(wfmStrings.employee(), "starter|add/add");
        }*/

        if (Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_ADD)) {
            addNewMenuItem(Property.getPluralWithObjectCode(SINGLE_PAYRUN_LIST, wfmStrings.payslips()), "singlePayrun|add/add");
        }

        if (Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_ADD)) {
            addNewMenuItem(Property.getPluralWithObjectCode(PAYSLIP_TABLE_LIST, payrollStrings.groupPayruns()), "payslipTable|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_SETTINGS_PENSION_PROVIDERS)) {
            addNewMenuItem(payrollStrings.pensionProvider(), "pensionprovider|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_SETTINGS_PENSION_SCHEMES)) {
            addNewMenuItem(wfmStrings.pensionScheme(), "pensionscheme|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_SETTINGS_PAYMENT_CATEGORIES)) {
            addNewMenuItem(wfmStrings.addCategory(), "payrollcategory|add/add");
        }

        if (Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_ADD)) {
            addNewMenuItem(Property.getPluralWithObjectCode(CASH_ADVANCE_LIST, wfmStrings.cashAdvance()), "cashAdvance|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_ADD)) {
            addNewMenuItem(Property.get(ADDITIONAL_PAYMENT_LIST, wfmStrings.additionalPayment()), "additionalPayment|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_OVERTIME_ADD) && Utils.hasGenericAccess(LOCALE_PAYROLL)) {
            addNewMenuItem(wfmStrings.overtime(), "overtime|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.ADD_BENEFIT_REQUEST)) {
            addNewMenuItem(hrmsStrings.benefitRequest(), "benefitRequest|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_EXPENCE_REPORT)) {
            addNewMenuItem(wfmStrings.expense(), "expenseReports|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_REQUEST)) {
            addNewMenuItem(wfmStrings.leaveRequest(), "availability|add/add");
        }

    }

    private void setPayrollPropertyListingsMap(LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap) {
        for (SelectItem selectItem : propertyListingsMap.keySet()) {
            LinkedList<View> viewList = new LinkedList<>();
            if (selectItem.getDescription().contains(ModuleEnum.PAYROLL.getCode())) {
                LinkedList<PropertyItem> propertyItemList = propertyListingsMap.get(selectItem);
                for (PropertyItem propertyItem : propertyItemList) {
                    if (propertyItem != null) {
                        switch (propertyItem.getObjectName()) {
                            case EMLOYEE_LIST:
                                viewList.add(new PayrollEmployeeListView(EmployeeListView.FROM_PAYROLL));
                                break;
                            case SINGLE_PAYRUN_LIST:
                                viewList.add(new SinglePayrunListView());
                                break;
                            case PAYSLIP_TABLE_LIST:
                                viewList.add(new PayslipTableListView());
                                break;
                            case CASH_ADVANCE_LIST:
                                viewList.add(new CashAdvanceListView());
                                break;
                            case MULTI_CASH_ADVANCE_LIST:
                                viewList.add(new MultiCashAdvanceListView());
                                break;
                            case BENEFIT_REQUESTS:
                                viewList.add(new BenefitRequestListView(Utils.getUserID()));
                                break;
                            case EMPLOYEE_TEMPLATE_LIST:
                                viewList.add(new PayrollEmployeeTemplateListView());
                                break;
                            case RECURRING_PAY_DEDUCTION_LIST:
                                viewList.add(new RecurringPayDeductionListView());
                                break;
                            case ADDITIONAL_PAYMENT_LIST:
                                viewList.add(new AdditionalPaymentListView());
                                break;
                            case OVERTIME:
                                if (Utils.hasGenericAccess(LOCALE_PAYROLL)) {
                                    viewList.add(new OvertimeListView());
                                }
                                break;
                            case ADDITIONAL_PAYMENT_ITEM_LIST:
                                viewList.add(new AdditionalPaymentItemListView(Utils.getUserID()));
                                break;
                            case END_OF_SERVICE_GRATUITY:
                                viewList.add(new EndOfServiceGratuityListView());
                                break;
                            case WPS_REPORT:
                                if (Utils.isArabicCompany()) {
                                    viewList.add(new WpsReportView());
                                }
                                break;
                            case END_OF_SERVICE_REPORT:
                                if (Utils.isArabicCompany()) {
                                    viewList.add(new EndOfServiceReportView());
                                }
                                break;
                            case PENSION_CONTRIBUTION_REPORT:
                                if (Utils.isArabicCompany()) {
                                    viewList.add(new PensionContributionReportView());
                                }
                                break;
                            case CASH_ADVANCE_REPORT:
                                viewList.add(new CashAdvanceReportView());
                                break;
                            case SALARY_REPORT:
                                viewList.add(new SalaryReportView());
                                break;
                            case SALARY_TRANSACTIONS:
                                viewList.add(new SalaryDetailedReportView());
                                break;
                            case PERSONAL_GOAL + GOAL:
                                if (propertyItem.getContainer() != null && "employee".equals(propertyItem.getContainer().getCode())) {
                                    viewList.add(new EmployeesGoalListView(Utils.getUserID()));
                                } else {
                                    viewList.add(new GoalsListView());
                                }
                                break;
                            case EMPLOYEE_DOCUMENTS:
                                if (propertyItem.getContainer() != null && "employee".equals(propertyItem.getContainer().getCode())) {
                                    viewList.add(new EmployeeDocumentsListView(Utils.getUserID(), false));
                                } else {
                                    viewList.add(new EmployeeDocumentsListView(null, false));
                                }
                                break;
                            case CERTIFICATES_LIST:
                                viewList.add(new CertificatesListView());
                                break;
                            case "my_attendance":
                                if (propertyItem.getContainer() != null && "availability".equals(propertyItem.getContainer().getCode())) {
                                    viewList.add(new GlobalEmployeeLeaveRequestView(null, Utils.getUserID(), null, "availability", wfmStrings.myAttendance(), GlobalEmployeeLeaveRequestView.FROM_IN_OUT_ATTENDANCE_TRACKING_VIEW));
                                } else {
                                    viewList.add(new GlobalEmployeeLeaveRequestView(null, null, "hrmsleaveRequests", (wfmStrings.leaveRequests())));
                                }
                                break;
                            case "incidentList":
                                if (propertyItem.getContainer() != null && "employee".equals(propertyItem.getContainer().getCode())) {
                                    viewList.add(new IncidentListView(Utils.getUserID()));
                                } else {
                                    viewList.add(new IncidentListView(true));
                                }
                                break;
                            case DEPENDENT_LIST:
                                viewList.add(new DependentListView(Utils.getUserID(),false));
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
            DynamicSinksContainer dynamicSC = new DynamicSinksContainer(selectItem.getCode(), selectItem.getName(), viewList);
            dynamicSC.setPreparedView(selectItem.getCategory());
            if (isFirstContener) {
                setSelection(dynamicSC);
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
