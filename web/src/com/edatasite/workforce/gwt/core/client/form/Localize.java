package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.Operands;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_CHART;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_CHART_CAPTION;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_CREATED_BY;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_CREATION_DATE;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_CSV;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_CSV_CAPTION;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_CURRENT_TIME;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_DESCRIPTION;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_FOLDER;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_MODIFICATION_DATE;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_MODIFIED_BY;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_NAME;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_PDF;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_PDF_CAPTION;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_RULE_NAME;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_XLS;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_XLS_CAPTION;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.WORKFLOW_FORM.EXECUTION_CRITERIA;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.WORKFLOW_FORM.RULE_CRITERIA;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.WORKFLOW_FORM.RULE_INFORMATION;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.Website.IMAGE_PANEL;
import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.MULTI_DEPARTMENT_FORM;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ASSIGNED_GOALS;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.EMPLOYEE_COMPETENCIES;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.INTEGRATION_ID;

/**
 * Created by Hayot on 6/2/2014.
 */
public class Localize implements CustomFormConstants, Operands {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    protected static final ProjectStrings projectStrings = ProjectStrings.App.get();
    protected static final WfmMessages wfmMessages = WfmMessages.App.get();
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();


    private static Localize instance = null;
    private boolean isCashAdvanse = false;

    public static Localize getInstance() {
        if (instance == null) {
            instance = new Localize();
        }
        return instance;
    }

    public String localizeByCode(String code) {
        String[] values = code.split(",");
        code = values.length > 1 ? values[0] : code;
        if (code != null) {
            if (Core.EQUAL.equals(code)) {
                return wfmStrings.equal();
            } else if (Core.NOT_EQUAL.equals(code)) {
                return wfmStrings.notEqual();
            } else if (StringT.CONTAINS.equals(code)) {
                return wfmStrings.contains();
            } else if (StringT.NOT_CONTAINS.equals(code)) {
                return wfmStrings.notContains();
            } else if (StringT.MATCHES.equals(code)) {
                return wfmStrings.matches();
            } else if (NumberT.GREATER.equals(code)) {
                return wfmStrings.greater();
            } else if (NumberT.GREATER_OR_EQUAL.equals(code)) {
                return wfmStrings.greaterOrEqual();
            } else if (NumberT.LOWER.equals(code)) {
                return wfmStrings.lower();
            } else if (NumberT.LOWER_OR_EQUAL.equals(code)) {
                return wfmStrings.lowerOrEqual();
            } else if (DateT.IS.equals(code)) {
                return wfmStrings.is();
            } else if (DateT.IS_NOT.equals(code)) {
                return wfmStrings.isNot();
            } else if (DateT.IS_AFTER.equals(code)) {
                return wfmStrings.isAfter();
            } else if (DateT.IS_BEFORE.equals(code)) {
                return wfmStrings.isBefore();
            } else if (DateT.BETWEEN.equals(code)) {
                return wfmStrings.between();
            } else if (DateT.NOT_BETWEEN.equals(code)) {
                return wfmStrings.notBetween();
            } else if (DateT.TODAY.equals(code)) {
                return wfmStrings.today();
            } else if (DateT.YESTERDAY.equals(code)) {
                return wfmStrings.yesterday();
            } else if (DateT.TOMORROW.equals(code)) {
                return wfmStrings.tomorrow();
            } else if (DateT.AGE_IN_DAYS.equals(code)) {
                return wfmStrings.ageInDays();
            }   else if (DateT.CURRENT_DAY.equals(code)) {
                return wfmStrings.currentDayOnly();
        }    else if (DateT.AGE_IN_HOURS.equals(code)) {
                return wfmStrings.ageInHours();
            } else if (DateT.HAS_DAYS_LEFT.equals(code)) {
                return wfmStrings.hasDaysLeft();
            } else if (CREATED_DATE.equals(code)) {
                return wfmStrings.createdDate();
            } else if (UPDATED_DATE.equals(code)) {
                return wfmStrings.modifiedDate();
            } else if (EMPLOYEE.equals(code)) {
                return wfmStrings.employee();
            } else if (STATUS.equals(code)) {
                return wfmStrings.status();
            } else if (CANDIDAT.equals(code)) {
                return wfmStrings.candidate();
            } else if (EXPIRY_DATE.equals(code)) {
                return wfmStrings.expiryDate();
            } else if (PREV_APPROVER.equals(code)) {
                return wfmStrings.prevApprover();
            } else if (PREV_APPROVER_EMAIL.equals(code)) {
                return wfmStrings.prevApproverEmail();
            } else if (PREV_APPROVER_STATUS.equals(code)) {
                return wfmStrings.prevApproverStatus();
            } else if (CURRENT_APPROVER.equals(code)) {
                return wfmStrings.currentApprover();
            } else if (CURRENT_APPROVER_EMAIL.equals(code)) {
                return wfmStrings.currentApproverEmail();
            } else if (CURRENT_APPROVER_STATUS.equals(code)) {
                return wfmStrings.currentApproverStatus();
            } else if (NEXT_APPROVER.equals(code)) {
                return wfmStrings.nextApprover();
            } else if (NEXT_APPROVER_EMAIL.equals(code)) {
                return wfmStrings.nextApproverEmail();
            } else if (NEXT_APPROVER_STATUS.equals(code)) {
                return wfmStrings.nextApproverStatus();
            } else if (HRMS.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE.equals(code)) {
                return wfmStrings.certificateType();
            } else if (NUMBER.equals(code)) {
                return wfmStrings.number();
            } else if (IS_DEFAULT.equals(code)) {
                return wfmStrings.isDefault();
            } else if (MEMORIZED_TRANSACTION.equals(code)) {
                return wfmStrings.memorizedTransaction();
            } else if (NARRATION.equals(code)) {
                return wfmStrings.narration();
            } else if (REFERENCE.equals(code)) {
                return wfmStrings.reference();
            } else if (DATE.equals(code)) {
                return wfmStrings.date();
            } else if (REPORT_NAME.equals(code)) {
                return wfmStrings.name();
            } else if (REPORT_DESCRIPTION.equals(code)) {
                return wfmStrings.description();
            } else if (REPORT_FOLDER.equals(code)) {
                return wfmStrings.folder();
            } else if (REPORT_CHART.equals(code)) {
                return wfmStrings.chart();
            } else if (REPORT_SUMMARY.equals(code)) {
                return wfmStrings.summaryView();
            } else if (REPORT_PDF.equals(code)) {
                return wfmStrings.pdf();
            } else if (REPORT_XLS.equals(code)) {
                return wfmStrings.fileXLS();
            } else if (REPORT_CSV.equals(code)) {
                return wfmStrings.csv();
            } else if (REPORT_CREATION_DATE.equals(code)) {
                return wfmStrings.createdDate();
            } else if (REPORT_CREATED_BY.equals(code)) {
                return wfmStrings.createdBy();
            } else if (REPORT_MODIFICATION_DATE.equals(code)) {
                return wfmStrings.modifiedDate();
            } else if (REPORT_MODIFIED_BY.equals(code)) {
                return wfmStrings.modifiedBy();
            } else if (REPORT_RULE_NAME.equals(code)) {
                return wfmStrings.ruleName();
            } else if (REPORT_CURRENT_TIME.equals(code)) {
                return wfmStrings.time();
            } else if (REPORT_XLS_CAPTION.equals(code)) {
                return wfmStrings.xlsCaption();
            } else if (REPORT_CSV_CAPTION.equals(code)) {
                return wfmStrings.csvCaption();
            } else if (REPORT_PDF_CAPTION.equals(code)) {
                return wfmStrings.pdfCaption();
            } else if (REPORT_CHART_CAPTION.equals(code)) {
                return wfmStrings.chartCaption();
            } else if (CERTIFICATE_OF_EMPLOYMENT.CONTENT.equals(code)) return wfmStrings.content();
            else if (CERTIFICATE_OF_EMPLOYMENT.DOCUMENT_CELL_TREE.equals(code)) return wfmStrings.documentCellTree();
            else if (GENERAL_INFORMATION.equals(code)) return wfmStrings.generalInformation();
            else if (ATTACHMENTS.equals(code)) return wfmStrings.attachments();
            else if (INFORMATION.equals(code)) return wfmStrings.information();
            else if (REQUESTER.equals(code)) return wfmStrings.requester();
            else if (DATE_PERIOD.equals(code)) return wfmStrings.datePeriod();
            else if (DESCRIPTION.equals(code)) return wfmStrings.description();
            else if (APPROVER.equals(code)) return wfmStrings.approver();
            else if (BENEFIT_TYPE.equals(code)) return wfmStrings.benefitType();
            else if (REQUESTED_QUANTITY.equals(code)) return wfmStrings.requestedQuantity();
            else if (LEFT_QUANTITY.equals(code)) return wfmStrings.leftQuantity();
            else if (INTEGRATION_ID.equals(code)) return wfmStrings.integrationId();
        }
        return values.length >= 2 ? values[1] + " - " + wfmStrings.itemTable() : null;
    }

    public String localizeByFieldID(String formID, String fieldID) {
        if (formID == null) {
            return localizeByCode(fieldID);
        }
        if (fieldID == null) {
            return null;
        }
        if (CustomFormConstants.ADDITIONAL_INFORMATION.equals(fieldID)) {
            return wfmStrings.additionalInformation();
        }

        switch (formID) {
            case LayoutRPC.LEAD_FORM:
            case LayoutRPC.CONTACT_FORM:
            case LayoutRPC.CANDIDATE_FORM:
                return localizeContact(fieldID);
            case LayoutRPC.CASE_FORM:
            case LayoutRPC.TELEGRAM_RECURRENCE_FORM:
                return localizeCase(fieldID);
            case LayoutRPC.MAIL_LIST_FORM:
                return localizeMailList(fieldID);
            case LayoutRPC.MESSAGE_FORM:
            case LayoutRPC.SMS_MESSAGE_FORM:
                return localizeEMMessage(fieldID);
            case LayoutRPC.CRM_WEB_FORM:
                return localizeWebForm(fieldID);
            case LayoutRPC.CRM_WEB_FORM_FOR_VIEW:
                return localizeWebForm(fieldID);
            case LayoutRPC.SOLUTION_FORM:
                return localizeSolution(fieldID);
            case LayoutRPC.ACTIVITY_FORM:
            case LayoutRPC.ACTIVITY_VIEW_FORM:
            case LayoutRPC.LOGACALL_FORM:
            case LayoutRPC.LOGACALL_FORM_VIEW:
            case LayoutRPC.WORKFLOW_EVENT_FORM:
            case LayoutRPC.WORKFLOW_CALL_LOG_FORM:
            case LayoutRPC.WORKFLOW_TELEGRAM_ALERT_FORM:
                return localizeEvent(fieldID);
            case LayoutRPC.OPPORTUNITY_FORM:
                return localizeOpportunity(fieldID);
            case LayoutRPC.CAMPAIGN_FORM:
                return localizeCampaign(fieldID);
            case LayoutRPC.ACCOUNT_FORM:
            case LayoutRPC.CLIENT_FORM:
            case LayoutRPC.SUPPLIER_FORM:
                return localizeCrmAccount(fieldID);
            case LayoutRPC.WORKFLOW_FORM:
            case LayoutRPC.WORKFLOW_ALERT_FORM:
                return localizeWorkflowForm(fieldID);
            case LayoutRPC.SMS_TEMPLATE_FORM:
                return localizeSMS(fieldID);
            case LayoutRPC.SCHEDULED_COURSE_FORM:
            case LayoutRPC.SCHEDULED_COURSE_VIEW_FORM:
                return localizeCourseSchedule(fieldID);
            case LayoutRPC.ONBOARDING_STEP_FORM:
                return localizeOnboardingStep(fieldID);
            case LayoutRPC.BOOKING_ITEM_RESERVATION_VIEW:
            case LayoutRPC.BOOKING_ITEMS_ADD_VIEW:
                return localizeBookingView(fieldID);
            case LayoutRPC.HRMS_EMPLOYEE_FORM:
            case LayoutRPC.PAYROLL_STARTER_FORM:
            case LayoutRPC.PM_EMPLOYEE_FORM:
            case LayoutRPC.SETTINGS_EMPLOYEE_FORM:
            case LayoutRPC.INSTRUCTOR_FORM:
                return localizeEmployee(fieldID);
            case LayoutRPC.SALEQUOTE_FORM:
            case LayoutRPC.PURCHASEORDER_FORM:
            case LayoutRPC.SALEINVOICE_FORM:
            case LayoutRPC.SALEORDER_FORM:
                return localizeSaleQuote(fieldID);
            case LayoutRPC.CASH_ADVANCE_FORM:
            case LayoutRPC.PAYROLL_CASH_ADVANCE_FORM:
                isCashAdvanse = true;
            case LayoutRPC.LEAVE_REQUEST_FORM:
            case LayoutRPC.EXPENSE_CLAIM_FORM:
            case LayoutRPC.DEPENDENT_FORM:
                return localizeCashAdvanceAndLeaveRequest(fieldID, isCashAdvanse);
            case LayoutRPC.PAYROLL_NEW_PAYMENT_CATEGORY_FORM:
            case LayoutRPC.PAYROLL_NEW_DEDUCTION_CATEGORY_FORM:
                return localizePayrollCategory(fieldID);
            case LayoutRPC.PROJECT_FORM:
                return localizeProject(fieldID);
            case "PM_TASKS_ADD":
            case "CRM_TASKS_ADD":
            case LayoutRPC.WORKFLOW_TASK_MIN_FORM:
            case LayoutRPC.TASK_MAX_FORM:
                return localizeTask(fieldID);
            case LayoutRPC.PRODUCT_FORM:
                return localizeProduct(fieldID);
            case LayoutRPC.RENTAL_PRODUCT_FORM:
                return localizeRentalProduct(fieldID);
            case LayoutRPC.RENTAL_ORDER_FORM:
                return localizeRentalOrder(fieldID);
            case LayoutRPC.HRMS_COMPANY_NEWS_FORM:
                return localizeHrmsNews(fieldID);
            case LayoutRPC.VACANCY_FORM:
                return localizeVacancyForm(fieldID);
            case LayoutRPC.PLACEMENT_FORM:
                return localizePlacementForm(fieldID);
            case LayoutRPC.COMPANY_SETTINGS_FORM:
                return localizeCompanySettings(fieldID);
            case LayoutRPC.CONTRACT_FORM:
                return localizeContractForm(fieldID);
            case LayoutRPC.FIXED_ASSET_FORM:
                return localizeFixedAssetForm(fieldID);
            case LayoutRPC.PRODUCT:
                return localizeProductForm(fieldID);
            case LayoutRPC.ISSUE_FORM:
                return localizeIssueForm(fieldID);
            case LayoutRPC.BANK_ACCOUNT_FORM:
                return localizeBankAccount(fieldID);
            case MULTI_DEPARTMENT_FORM:
                return localizeDepartmentForm(fieldID);
            case LayoutRPC.PERSONAL_GOAL_FORM:
                return localizeGoalManagement(fieldID, hrmsStrings.personalGoal(), null);
            case LayoutRPC.GROUP_GOAL_FORM:
                return localizeGroupGoalManagement(fieldID);

            case LayoutRPC.DEPARTMENT_GOAL_FORM:
                return localizeGoalManagement(fieldID, Property.get(Constants.DEPARTMENT_LIST, hrmsStrings.departmentGoal(), wfmStrings.department()), Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));

            case LayoutRPC.PROJECT_GOAL_FORM:
                return localizeGoalManagement(fieldID, hrmsStrings.projectgoal(), Property.get(Constants.PROJECT_GOAL, hrmsStrings.projectgoal()));
            case LayoutRPC.BUSINESS_GOAL_FORM:
                return localizeGoalManagement(fieldID, hrmsStrings.businessGoal(), null);
            case LayoutRPC.COMPANY_GOAL_FORM:
                return localizeGoalManagement(fieldID, hrmsStrings.companyGoal(), null);
            case LayoutRPC.BENEFIT_FORM:
                return localizeBenefitForm(fieldID);
            case LayoutRPC.CUSTOMER_REFUND:
                return localizeCustomerRefundForm(fieldID);
            case LayoutRPC.PREPAYMENT_FORM:
            case LayoutRPC.SUPPLIER_CREDIT_FORM:
                return localizeCustomerPrepaymentForm(fieldID);
            case LayoutRPC.SUPPLIER_REFUND:
                return localizeSupplierRefundForm(fieldID);
            case LayoutRPC.COURSE_FORM:
                return localizeCourseForm(fieldID);
            case LayoutRPC.ADD_COURSE_SUBJECT_VIEW:
                return localizeAddCourseSubjectView(fieldID);
            case LayoutRPC.STUDENT_FORM:
                return localizeStudentForm(fieldID);
            case LayoutRPC.ATTENDENCE_SHEET_FORM:
                return localizeAttendenceSheetForm(fieldID);
            case LayoutRPC.INVOICE_GENERATOR_FORM:
                return localizeInvoiceGeneratorForm(fieldID);
            case LayoutRPC.SCHEDULE_INVOICE_FORM:
                return localizeScheduleInvoiceForm(fieldID);
            case LayoutRPC.COURSE_BOOKING_FORM:
                return localizeCourseBookingForm(fieldID);
            case LayoutRPC.TRAINING_CONTACT_FORM:
                return localizeTrainingContactForm(fieldID);
            case LayoutRPC.CERTIFICATE_FORM:
                return localizeCertificateForm(fieldID);
            case LayoutRPC.HSE_PASSPORT_FORM:
                return localizeHSEPassportForm(fieldID);
            case LayoutRPC.MEETING_MINUTES:
                return localizeByMeetingMinutes(fieldID);
            case LayoutRPC.PRODUCT_CATEGORY_FORM:
                return localizeProductCategoryForm(fieldID);
            case LayoutRPC.DEPARTMENT_FORM:
                return localizeDepartmentForm(fieldID);
            case LayoutRPC.POSITION_FORM:
                return localizePositionForm(fieldID);
            case LayoutRPC.RECEIVE_PAYMENT_FORM:
                return localizeBatchPaymentForm(fieldID);
            case LayoutRPC.PAY_INVOICE_FORM:
                return localizeBatchPaymentForm(fieldID);
            case LayoutRPC.INCIDENT_FORM:
                return localizeIncidentForm(fieldID);
            case LayoutRPC.ADDITIONAL_PAYMENT_FORM:
                return localizeAdditionalPayment(fieldID);
            case LayoutRPC.BRIGADA_FORM:
                return localizeAddBrigadaForm(fieldID);
            case LayoutRPC.SHIFT_FORM:
                return localizeShiftForm(fieldID);
            case LayoutRPC.GROUP_PLACEMENT_FORM:
                return localizeGroupPlacementForm(fieldID);
            case LayoutRPC.ROTATION_FORM:
                return localizeRotationForm(fieldID);
            case LayoutRPC.BRAND_FORM:
                return localizeBrandForm(fieldID);
            case LayoutRPC.MULTI_POSITION_FORM:
                return localizePositionForm(fieldID);
            case LayoutRPC.ASSESSMENT_FORM:
                return localizeSimpleAppraisalForm(fieldID);
            default:
                return localizeByCode(fieldID);
        }
    }

    private String localizeDepartmentForm(String fieldID) {
        if (fieldID != null) {
            if (DEPARTMENT_NAME.equals(fieldID)) {
                return wfmStrings.department();
            } else if (DEPARTMENT_NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (DEPARTMENT_DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if (DEPARTMENT_EMAIL.equals(fieldID)) {
                return wfmStrings.email();
            } else if (DEPARTMENT_SHORT_DESCRIPTION.equals(fieldID)) {
                return wfmStrings.shortDescription();
            } else if (DEPARTMENT_START_DATE.equals(fieldID)) {
                return wfmStrings.startDate();
            } else if (DEPARTMENT_PARENT.equals(fieldID)) {
                return wfmStrings.reportsTo();
            } else if (DEPARTMENT_CREATED_BY.equals(fieldID)) {
                return wfmStrings.createdBy();
            } else if (DEPARTMENT_EMPLOYEES.equals(fieldID)) {
                return Property.get(Constants.DEPARTMENT, wfmStrings.teamMembers(), wfmStrings.department());
            } else if (DEPARTMENT_LEADER.equals(fieldID)) {
                return Property.get(Constants.DEPARTMENT, wfmStrings.departmentLeader(), wfmStrings.department());
            } else if (DEPARTMENT_LEADER2.equals(fieldID)) {
                return Property.get(Constants.DEPARTMENT, wfmStrings.departmentLeader2(), wfmStrings.department());
            } else if (DEPARTMENT_LEADER3.equals(fieldID)) {
                return Property.get(Constants.DEPARTMENT, wfmStrings.departmentLeader3(), wfmStrings.department());
            } else if (DEPARTMENT_LEADER4.equals(fieldID)) {
                return Property.get(Constants.DEPARTMENT, wfmStrings.departmentLeader4(), wfmStrings.department());
            } else if (DEPARTMENT_LEADER5.equals(fieldID)) {
                return Property.get(Constants.DEPARTMENT, wfmStrings.departmentLeader5(), wfmStrings.department());
            } else if (DEPARTMENT_LOCATIONS.equals(fieldID)) {
                return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
            } else if (DEPARTMENT_DETAILS.equals(fieldID)) {
                return wfmStrings.details();
            }
        }
        return null;
    }

    private String localizePositionForm(String fieldID) {
        if (fieldID != null) {
            if (POSITIONS.POSITION_CODE.equals(fieldID)) {
                return wfmStrings.number();
            } else if (POSITIONS.POSITION_TITLE.equals(fieldID)) {
                return wfmStrings.name();
            } else if (POSITIONS.EMPLOYEES.equals(fieldID)) {
                return wfmStrings.employees();
            } else if (POSITIONS.JOB_FAMILY_PANEL.equals(fieldID)) {
                return wfmStrings.jobFamily();
            } else if (POSITIONS.ESTIBLISHED.equals(fieldID)) {
                return wfmStrings.created();
            } else if (CustomFormConstants.COUNT.equals(fieldID)) {
                return wfmStrings.count();
            } else if (POSITIONS.STATUS.equals(fieldID)) {
                return wfmStrings.status();
            } else if (POSITIONS.DEPARTMENT.equals(fieldID)) {
                return wfmStrings.department();
            } else if (POSITIONS.LOCATION.equals(fieldID)) {
                return wfmStrings.location();
            } else if (POSITIONS.INVOLVED_EMPLOYEES.equals(fieldID)) {
                return wfmStrings.involvedEmployees();
            } else if (POSITIONS.BASIC_INFORMATION.equals(fieldID)) {
                return wfmStrings.basicDetails();
            } else if (CustomFormConstants.TYPE.equals(fieldID)) {
                return wfmStrings.type();
            } else if (CustomFormConstants.VACANCY.DETAILED_INFORMATION.equals(fieldID)) {
                return wfmStrings.detailedInformation();
            } else if (POSITIONS.POSITION_DEPARTMENTS.equals(fieldID)) {
                return wfmStrings.departments();
            } else if (POSITIONS.POSITION_DETAILS.equals(fieldID)) {
                return wfmStrings.basicDetails();
            } else if (POSITIONS.POSITION_TREE.equals(fieldID)) {
                return wfmStrings.positions();
            } else if (POSITIONS.SALARY_BASIS.equals(fieldID)) {
                return wfmStrings.salaryBasis();
            } else if (POSITIONS.MEASURING_EMPLOYEE_PERFORMANCE.equals(fieldID)) {
                return wfmStrings.measuringEmployeePerformance();
            } else if (POSITIONS.PERSONAL_QUALITIES.equals(fieldID)) {
                return wfmStrings.personalQualiteis();
            } else if (POSITIONS.KNOWLEDGE.equals(fieldID)) {
                return wfmStrings.knowledge();
            } else if (POSITIONS.JOB_REQUIREMENT.equals(fieldID)) {
                return wfmStrings.jobRequirements();
            } else if (POSITIONS.POSITION_RESPONSIBILITIES.equals(fieldID)) {
                return wfmStrings.responsibilities();
            } else if (POSITIONS.POSITION_DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if (POSITIONS.KNOWLEDE_AND_SKILLS.equals(fieldID)) {
                return wfmStrings.knowledgeAndSkills();
            }else if (POSITIONS.CURRENT_HEAD_COUNT.equals(fieldID)) {
                wfmStrings.headCount();
            }
        }
        return null;
    }

    private String localizeRotationForm(String fieldID) {
        if (fieldID != null) {
            if (BASIC_INFORMATION.equals(fieldID)) {
                return wfmStrings.basicDetails();
            } else if (CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES.equals(fieldID)) {
                return wfmStrings.involvedEmployees();
            }
        }
        return null;
    }

    private String localizeBrandForm(String fieldID) {
        if (fieldID != null) {
            if (TITLE.equals(fieldID)) {
                return wfmStrings.brand();
            } else if (NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if (PARENT.equals(fieldID)) {
                return wfmStrings.parent();
            } else if (IMAGE_PANEL.equals(fieldID)) {
                return wfmStrings.uploadImage();
            }
        }
        return null;
    }

    private String localizeShiftForm(String fieldID) {
        if (fieldID != null) {
            if (BASIC_INFORMATION.equals(fieldID)) {
                return wfmStrings.basicDetails();
            } else if (CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES.equals(fieldID)) {
                return wfmStrings.involvedEmployees();
            } else if (CustomFormConstants.PROJECT.BACKUP_MANAGER.equals(fieldID)) {
                return wfmStrings.backupManagers();
            } else if (CustomFormConstants.PROJECT.MANAGER.equals(fieldID)) {
                return wfmStrings.manager();
            } else if (CustomFormConstants.COMPETENCIES.equals(fieldID)) {
                return  wfmStrings.competencies();
            }
        }
        return null;
    }

    private String localizeSimpleAppraisalForm(String fieldID) {
        if (fieldID != null) {
            if (BASIC_INFORMATION.equals(fieldID)) {
                return wfmStrings.basicDetails();
            } else if (EMPLOYEE_COMPETENCIES.equals(fieldID)) {
                return wfmStrings.employeeCompetencies();
            }
        }
        return null;
    }

    private String localizeIncidentForm(String fieldID) {
        if (fieldID != null) {
            if ("BASIC_INFORMATION".equals(fieldID)) {
                return wfmStrings.basicDetails();
            } else if (ATTACHMENTS.equals(fieldID)) {
                return wfmStrings.attachments();
            }
        }
        return null;
    }

    private String localizeGroupPlacementForm(String fieldID) {
        if (fieldID != null) {
            if (BASIC_INFORMATION.equals(fieldID)) {
                return wfmStrings.basicDetails();
            } else if (CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES.equals(fieldID)) {
                return wfmStrings.involvedEmployees();
            }
        }
        return null;
    }

    private String localizeAdditionalPayment(String fieldID) {
        if (fieldID != null) {
            if ("BASIC_INFORMATION".equals(fieldID)) {
                return wfmStrings.basicDetails();
            } else if ("EMPLOYEES".equals(fieldID)) {
                return wfmStrings.employees();
            } else if (CustomFormConstants.APPROVERS.equals(fieldID)) {
                return wfmStrings.approver();
            } else if (CustomFormConstants.EMPLOYEE.equals(fieldID)) {
                return wfmStrings.employee();
            } else if (CustomFormConstants.AdditionalPaymentImport.PAYMENT_DATE.equals(fieldID)) {
                return wfmStrings.paymentDate();
            } else if (PERIOD.equals(fieldID)) {
                return wfmStrings.period();
            } else if (CustomFormConstants.ACCOUNTING.PAYMENT_TYPE.equals(fieldID)) {
                return wfmStrings.paymentType();
            } else if (CustomFormConstants.AMOUNT.equals(fieldID)) {
                return wfmStrings.fixedAmount();
            } else if (CustomFormConstants.CATEGORY.equals(fieldID)) {
                return wfmStrings.category();
            } else if (REFERENCE.equals(fieldID)) {
                return wfmStrings.reference();
            } else if ("SHOW_PAYSLIP".equals(fieldID)) {
                return wfmStrings.showInPayslip();
            } else if (APPROVERS.equals(fieldID)) {
                return wfmStrings.approver();
            }
        }
        return null;
    }

    private String localizeAddBrigadaForm(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (CustomFormConstants.NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (CustomFormConstants.STATUS.equals(fieldID)) {
                return wfmStrings.status();
            } else if (CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE.equals(fieldID)) {
                return wfmStrings.members();
            } else if (CustomFormConstants.PROJECT.BACKUP_MANAGER.equals(fieldID)) {
                return wfmStrings.backupManagers();
            } else if (CustomFormConstants.PROJECT.MANAGER.equals(fieldID)) {
                return wfmStrings.manager();
            } else if (CustomFormConstants.PROJECT.PROJECT_NOTE.equals(fieldID)) {
                return wfmStrings.notes();
            } else if (BASIC_INFORMATION.equals(fieldID)) {
                return wfmStrings.basicDetails();
            } else if (NOTES_AND_ATTACHMENTS.equals(fieldID)) {
                return wfmStrings.meetingNotesAttachments();
            } else if (INVOLVED_EMPLOYEES.equals(fieldID)) {
                return wfmStrings.involvedEmployees();
            }
        }
        return null;
    }

    private String localizeBatchPaymentForm(String fieldID) {
        if (fieldID != null) {
            if (BATCH_PAYMENT.CUSTOMER.equals(fieldID)) {
                return wfmStrings.customer();
            }
            if (BATCH_PAYMENT.SUPPLIER.equals(fieldID)) {
                return wfmStrings.supplier();
            } else if (BATCH_PAYMENT.PAYMENT_DATE.equals(fieldID)) {
                return wfmStrings.paymentDate();
            } else if (BATCH_PAYMENT.NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (BATCH_PAYMENT.ACCOUNT.equals(fieldID)) {
                return wfmStrings.account();
            } else if (BATCH_PAYMENT.AMOUNT.equals(fieldID)) {
                return wfmStrings.amount();
            } else if (BATCH_PAYMENT.PAYMENT_TYPE.equals(fieldID)) {
                return wfmStrings.paymentType();
            } else if (BATCH_PAYMENT.CURRENCY.equals(fieldID)) {
                return wfmStrings.currency();
            } else if (BATCH_PAYMENT.REFERENCE.equals(fieldID)) {
                return wfmStrings.reference();
            } else if (BATCH_PAYMENT.DEPARTMENT.equals(fieldID)) {
                return wfmStrings.department();
            }
        }
        return null;
    }

    public String localizeHSEPassportForm(String fieldID) {
        if (fieldID != null) {
            if (NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (CERTIFICATE.STUDENT.equals(fieldID)) {
                return wfmStrings.student();
            } else if (TYPE.equals(fieldID)) {
                return wfmStrings.type();
            } else if (STATUS.equals(fieldID)) {
                return wfmStrings.status();
            } else if (LEVEL.equals(fieldID)) {
                return wfmStrings.level();
            } else if (COURSES.equals(fieldID)) {
                return wfmStrings.courses();
            }
        }
        return null;
    }


    public String localizeByMeetingMinutes(String fieldID) {
        if (fieldID != null) {
            if ("AGENDA".equals(fieldID)) {
                return wfmStrings.agendaTopic();
            } else if ("MEETING_M_NUMBER".equals(fieldID)) {
                return wfmStrings.number();
            } else if ("MEETING_M_TITLE".equals(fieldID)) {
                return wfmStrings.title();
            } else if ("MEETING_M_CALLED_BY".equals(fieldID)) {
                return wfmStrings.calledBy();
            } else if ("MEETING_M_PREPARED_BY".equals(fieldID)) {
                return wfmStrings.preparedBy();
            } else if ("MEETING_M_NEXT_DATE".equals(fieldID)) {
                return wfmStrings.nextMeeting();
            } else if ("MEETING_M_MEETING_PERIOD".equals(fieldID)) {
                return wfmStrings.meetingPeriod();
            } else if ("MEETING_M_LOCATION".equals(fieldID)) {
                return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
            } else if ("MEETING_M_TYPE".equals(fieldID)) {
                return wfmStrings.type();
            } else if ("MEETING_M_NOTES".equals(fieldID)) {
                return wfmStrings.notes();
            } else if (Constants.PROJECT.equals(fieldID)) {
                return wfmStrings.project();
            } else if ("MEETING_M_ATTENDEES".equals(fieldID)) {
                return wfmStrings.attendees();
            } else if ("MEETING_M_ABSENT".equals(fieldID)) {
                return wfmStrings.absent();
            } else if ("MEETING_M_PURPOSE".equals(fieldID)) {
                return wfmStrings.purpose();
            } else if ("MEETING_M_ATTACHMENTS".equals(fieldID)) {
                return wfmStrings.attachments();
            } else if ("MEETING_M_AGENDA_PANEL".equals(fieldID)) {
                return wfmStrings.agendaTopic();
            } else if ("MEETING_M_EMAIL_TEMPLATES".equals(fieldID)) {
                return wfmStrings.template();
            } else if ("NOTES_AND_ATTACHMENTS".equals(fieldID)) {
                return wfmStrings.meetingNotesAttachments();
            } else if ("BASIC_INFORMATION".equals(fieldID)) {
                return wfmStrings.basicDetails();
            } else if (NOTES.equals(fieldID)) return wfmStrings.notes();

        }
        return null;
    }

    public String localizeCertificateForm(String fieldID) {
        if (fieldID != null) {
            if (CERTIFICATE.NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (CERTIFICATE.STUDENT.equals(fieldID)) {
                return wfmStrings.student();
            } else if (CERTIFICATE.CERTIFICATE_TYPE.equals(fieldID)) {
                return wfmStrings.certificateType();
            } else if (CERTIFICATE.ITEMS_TABLE.equals(fieldID)) {
                return wfmStrings.itemsTable();
            }
        }
        return null;
    }

    public String localizeTrainingContactForm(String fieldID) {

        if (fieldID != null) {
            if (CONTRACT.GENERAL_DETAILS.equals(fieldID)) {
                return wfmStrings.generalDetails();
            } else if (CONTRACT.ACCOUNT.equals(fieldID)) {
                return wfmStrings.account();
            } else if (CONTRACT.NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (CONTRACT.DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if (CONTRACT.START_DATE.equals(fieldID)) {
                return wfmStrings.startDate();
            } else if (CONTRACT.END_DATE.equals(fieldID)) {
                return wfmStrings.endDate();
            } else if (CONTRACT.PREPAID.equals(fieldID)) {
                return wfmStrings.prePaid();
            } else if (CONTRACT.COURSES_LIST.equals(fieldID)) {
                return wfmStrings.courseList();
            }
        }
        return null;
    }

    public String localizeCourseBookingForm(String fieldID) {

        if (fieldID != null) {
            if (COURSE_BOOKING.CUSTOMER_DETAILS.equals(fieldID)) {
                return wfmStrings.customerDetails();
            } else if (COURSE_BOOKING.COMPANY_NUMBER.equals(fieldID)) {
                return wfmStrings.companyNumber();
            } else if (COURSE_BOOKING.COMPANY_NAME.equals(fieldID)) {
                return wfmStrings.companyName();
            } else if (COURSE_BOOKING.PHONE_NUMBER.equals(fieldID)) {
                return wfmStrings.phone();
            } else if (COURSE_BOOKING.FAX_NUMBER.equals(fieldID)) {
                return wfmStrings.fax();
            } else if (COURSE_BOOKING.CUSTOMER_EMAIL.equals(fieldID)) {
                return wfmStrings.customEmail();
            } else if (COURSE_BOOKING.TRAINING_VENUE.equals(fieldID)) {
                return wfmStrings.traningVenue();
            } else if (COURSE_BOOKING.TYPE.equals(fieldID)) {
                return wfmStrings.type();
            }
        }
        return null;
    }

    public String localizeAttendenceSheetForm(String fieldID) {
        if (fieldID != null) {
            if (ATTENDENCE_SHEET.LOCATION.equals(fieldID)) {
                return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
            } else if (ATTENDENCE_SHEET.DATE.equals(fieldID)) {
                return wfmStrings.date();
            } else if (ATTENDENCE_SHEET.INSTRUCTOR.equals(fieldID)) {
                return wfmStrings.instructor();
            } else if (ATTENDENCE_SHEET.COURSE.equals(fieldID)) {
                return wfmStrings.course();
            } else if (ATTENDENCE_SHEET.INSTRUCTOR_ATTENDENCE.equals(fieldID)) {
                return wfmStrings.instructorAttendence();
            } else if (ATTENDENCE_SHEET.STUDENTS_ATTENDENCE.equals(fieldID)) {
                return wfmStrings.studentAttendence();
            } else if (ATTENDENCE_SHEET.INSTRUCTOR.equals(fieldID)) {
                return wfmStrings.instructor();
            } else if (INFORMATION.equals(fieldID)) {
                return wfmStrings.information();
            }
        }
        return null;
    }

    public String localizeInvoiceGeneratorForm(String fieldID) {
        if (fieldID != null) {
            if (INVOICE_GENERATOR.PERIOD.equals(fieldID)) {
                return Property.get("PERIOD", wfmStrings.period());
            } else if (INVOICE_GENERATOR.END.equals(fieldID)) {
                return wfmStrings.end();
            }
        }
        return null;
    }

    public String localizeScheduleInvoiceForm(String fieldID) {
        if (fieldID != null) {
            if (SCHEDULE_INVOICE.PERIOD.equals(fieldID)) {
                return Property.get("PERIOD", wfmStrings.period());
            } else if (SCHEDULE_INVOICE.END.equals(fieldID)) {
                return Property.get("END", wfmStrings.end());
            }else if (SCHEDULE_INVOICE.END.equals(fieldID)) {
                return Property.get("CUSTOMER", wfmStrings.customer());
            }
        }
        return null;
    }

    public String localizeStudentForm(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DETAILS.equals(fieldID)) {
                return wfmStrings.studentDetails();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME.equals(fieldID)) {
                return wfmStrings.firstName();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER.equals(fieldID)) {
                return wfmStrings.customer();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME.equals(fieldID)) {
                return wfmStrings.lastName();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL.equals(fieldID)) {
                return wfmStrings.email();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE.equals(fieldID)) {
                return wfmStrings.phone();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER.equals(fieldID)) {
                return wfmStrings.companyEmployeeNumber();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE.equals(fieldID)) {
                return wfmStrings.studentDepartmentCode();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP.equals(fieldID)) {
                return wfmStrings.safetyPp();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER.equals(fieldID)) {
                return wfmStrings.refIndNumeber();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS.equals(fieldID)) {
                return wfmStrings.studentStatus();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER.equals(fieldID)) {
                return wfmStrings.studentGender();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY.equals(fieldID)) {
                return wfmStrings.nationality();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.DATE_OF_BIRTH.equals(fieldID)) {
                return wfmStrings.dateOfBirth();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_UPLOAD_IMAGE.equals(fieldID)) {
                return wfmStrings.studentUploadImage();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.ADDRESS_INFORMATION.equals(fieldID)) {
                return wfmStrings.addressInformation();
            } else if (CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_ADDRESS.equals(fieldID)) {
                return wfmStrings.studentAddress();
            }
        }
        return null;
    }

    public String localizeAddCourseSubjectView(String fieldID) {
        if (fieldID != null) {
            if (NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (SUBJECT_PARENT.equals(fieldID)) {
                return wfmStrings.subjectParent();
            } else if (DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            }
        }
        return null;
    }

    public String localizeCourseForm(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.SUBJECT.equals(fieldID)) {
                return wfmStrings.subject();
            } else if (CustomFormConstants.CODE.equals(fieldID)) {
                return wfmStrings.code();
            } else if (CustomFormConstants.NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (CustomFormConstants.DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if (COURSE.DURATION.equals(fieldID)) {
                return wfmStrings.duration();
            } else if (COURSE.VALIDITY.equals(fieldID)) {
                return wfmStrings.validity();
            } else if (COURSE.PRICEPERSTUDENT.equals(fieldID)) {
                return wfmStrings.pricePerStudent();
            } else if (COURSE.OTHER_PREREQUISITE.equals(fieldID)) {
                return wfmStrings.otherPreRequisite();
            } else if (COURSE.COURSE_REQUIREMENTS.equals(fieldID)) {
                return wfmStrings.courseRequirements();
            } else if (COURSE.PREREQUISITE.equals(fieldID)) {
                return wfmStrings.preRequisite();
            }
        }
        return null;
    }

    public String localizeBookingView(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.INFORMATION.equals(fieldID)) {
                return wfmStrings.information();
            } else if (CustomFormConstants.BENEFIT.NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (CustomFormConstants.CATEGORY.equals(fieldID)) {
                return wfmStrings.category();
            } else if (CustomFormConstants.NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if (LOCATION_FIELD.equals(fieldID)) {
                return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
            } else if (RESERVED_BY.equals(fieldID)) {
                return wfmStrings.reservedBy();
            } else if (CATEGORY.equals(fieldID)) {
                return wfmStrings.category();
            } else if (START_DATE.equals(fieldID)) {
                return wfmStrings.startDate();
            } else if (END_DATE.equals(fieldID)) {
                return wfmStrings.endDate();
            } else if (ALL_DATE.equals(fieldID)) {
                return wfmStrings.allDate();
            } else if (RESERVATION_HISTORY.equals(fieldID)) {
                return wfmStrings.reservationHistory();
            } else if (ITEMS.equals(fieldID)) {
                return wfmStrings.items();
            } else if (CustomFormConstants.BENEFIT.TYPE.equals(fieldID)) {
                return wfmStrings.type();
            } else if (CustomFormConstants.BENEFIT.QTY_TYPE.equals(fieldID)) {
                return wfmStrings.quantityType();
            } else if (CustomFormConstants.BENEFIT.CURRENCY.equals(fieldID)) {
                return wfmStrings.currency();
            } else if (CustomFormConstants.BENEFIT.LEAVE_ALLOUNCE_PANEL.equals(fieldID)) {
                return wfmStrings.allowance();
            } else if (CustomFormConstants.BENEFIT.TRANSFERRABLE.equals(fieldID)) {
                return wfmStrings.transferrable();
            } else if (CustomFormConstants.BENEFIT.EXPIRE_DATE.equals(fieldID)) {
                return wfmStrings.expiryDate();
            } else if (CustomFormConstants.BENEFIT.STATUS.equals(fieldID)) {
                return wfmStrings.active();
            } else if (CustomFormConstants.BENEFIT.CREDIT_TO_ACCOUNT.equals(fieldID)) {
                return wfmStrings.creditToAccount();
            } else if (CustomFormConstants.BENEFIT.DEBIT_TO_ACCOUNT.equals(fieldID)) {
                return wfmStrings.debitToAccount();
            } else if (CustomFormConstants.BENEFIT.ACTIVE.equals(fieldID)) {
                return wfmStrings.active();
            }
        }
        return null;
    }

    public String localizeBenefitForm(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.BENEFIT.INFORMATION.equals(fieldID)) {
                return wfmStrings.information();
            } else if (CustomFormConstants.BENEFIT.NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (CustomFormConstants.BENEFIT.TYPE.equals(fieldID)) {
                return wfmStrings.type();
            } else if (CustomFormConstants.BENEFIT.QTY_TYPE.equals(fieldID)) {
                return wfmStrings.quantityType();
            } else if (CustomFormConstants.BENEFIT.CURRENCY.equals(fieldID)) {
                return wfmStrings.currency();
            } else if (CustomFormConstants.BENEFIT.LEAVE_ALLOUNCE_PANEL.equals(fieldID)) {
                return wfmStrings.allowance();
            } else if (CustomFormConstants.BENEFIT.TRANSFERRABLE.equals(fieldID)) {
                return wfmStrings.transferrable();
            } else if (CustomFormConstants.BENEFIT.EXPIRE_DATE.equals(fieldID)) {
                return wfmStrings.expiryDate();
            } else if (CustomFormConstants.BENEFIT.STATUS.equals(fieldID)) {
                return wfmStrings.active();
            } else if (CustomFormConstants.BENEFIT.CREDIT_TO_ACCOUNT.equals(fieldID)) {
                return wfmStrings.creditToAccount();
            } else if (CustomFormConstants.BENEFIT.DEBIT_TO_ACCOUNT.equals(fieldID)) {
                return wfmStrings.debitToAccount();
            } else if (CustomFormConstants.BENEFIT.ACTIVE.equals(fieldID)) {
                return wfmStrings.active();
            } else if (CustomFormConstants.BENEFIT.DESCRIPION.equals(fieldID)) {
                return wfmStrings.description();
            }
        }
        return null;
    }

    public String localizeCustomerRefundForm(String fieldID) {
        if (fieldID != null) {
            if (PREPAYMENT_FORM_TITLE.equals(fieldID)) {
                return wfmStrings.basicDetails();
            } else if (CRM_ACCOUNT_LOOKUP.equals(fieldID)) {
                return wfmStrings.customer();
            } else if (DATE_FIELD.equals(fieldID)) {
                return wfmStrings.date();
            } else if (REFERENCE.equals(fieldID)) {
                return wfmStrings.reference();
            } else if (CURRENCY.equals(fieldID)) {
                return wfmStrings.currency();
            } else if ("BANK_ACCOUNT".equals(fieldID)) {
                return wfmStrings.bankAccount();
            } else if (TYPE.equals(fieldID)) {
                return wfmStrings.type();
            } else if ("ITEMS_TABLE".equals(fieldID)) {
                return wfmStrings.refundItems();
            }
        }
        return null;
    }

    public String localizeCustomerPrepaymentForm(String fieldID) {
        if (fieldID != null) {
            if (PREPAYMENT_FORM_TITLE.equals(fieldID)) {
                return wfmStrings.prepaymentInformation();
            } else if (CRM_ACCOUNT_LOOKUP.equals(fieldID)) {
                return wfmStrings.customer();
            } else if (PAYMENT_ACCOUNT_LOOKUP.equals(fieldID)) {
                return wfmStrings.paidFrom();
            } else if (PREPAYMENT_AMOUNT.equals(fieldID)) {
                return wfmStrings.amount();
            } else if (FEE_TYPE.equals(fieldID)) {
                return wfmStrings.type();
            } else if (AMOUNT_PERCENTAGE.equals(fieldID)) {
                return wfmStrings.amount();
            } else if (ACCOUNTS_RECEIVABLE_PAYABLE.equals(fieldID)) {
                return wfmStrings.accountsPayable();
            } else if (PREPAYMENT_NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP.equals(fieldID)) {
                return wfmStrings.purchaseorder();
            } else if (PROJECT_.equals(fieldID)) {
                return wfmStrings.project();
            } else if (DATE_FIELD.equals(fieldID)) {
                return wfmStrings.date();
            } else if (REFERENCE.equals(fieldID)) {
                return wfmStrings.reference();
            } else if (CURRENCY.equals(fieldID)) {
                return wfmStrings.currency();
            } else if (TYPE.equals(fieldID)) {
                return wfmStrings.type();
            } else if (PAYMENT_TABLE.equals(fieldID)) {
                return wfmStrings.invoiceInformation();
            } else if (REFUND_DETAILS.equals(fieldID)) {
                return wfmStrings.refundItems();
            } else if (INVOICE_DETAILS.equals(fieldID)) {
                return wfmStrings.invoiceDetails();
            } else if (REFUND_TABLE.equals(fieldID)) {
                return wfmStrings.refundTable();
            }
        }
        return null;
    }

    public String localizeSupplierRefundForm(String fieldID) {
        if (fieldID != null) {
            if (PREPAYMENT_FORM_TITLE.equals(fieldID)) {
                return wfmStrings.basicDetails();
            } else if (CRM_ACCOUNT_LOOKUP.equals(fieldID)) {
                return wfmStrings.supplier();
            } else if (DATE_FIELD.equals(fieldID)) {
                return wfmStrings.date();
            } else if (REFERENCE.equals(fieldID)) {
                return wfmStrings.reference();
            } else if (CURRENCY.equals(fieldID)) {
                return wfmStrings.currency();
            } else if ("BANK_ACCOUNT".equals(fieldID)) {
                return wfmStrings.bankAccount();
            } else if (TYPE.equals(fieldID)) {
                return wfmStrings.type();
            } else if ("ITEMS_TABLE".equals(fieldID)) {
                return wfmStrings.refundItems();
            }
        }
        return null;
    }

    public String localizeBankAccount(String fieldID) {
        switch (fieldID) {
            case ACCOUNT_INFORMATION:
                return wfmStrings.basicInfo();
            case BANK_NAME:
                return wfmStrings.bankName();
            case ACCOUNT_NUMBER:
                return wfmStrings.accountNumber();
            case ACCOUNT_CODE:
                return wfmStrings.accountCode();
            case ACCOUNT_NAME:
                return wfmStrings.accountName();
            case BANK_BRANCH:
                return wfmStrings.bankBranch();
            case ADDRESS_INFORMATION:
                return wfmStrings.addressInformation();
            case STREET_ADDRESS:
                return wfmStrings.streetAddress();
            case CITY:
                return wfmStrings.city();
            case COUNTRY:
                return wfmStrings.country();
            case STATE:
                return wfmStrings.state();
            case POST_CODE:
                return wfmStrings.postCode();
            case PHONE_NUMBER:
                return wfmStrings.phone();
            case "CODES":
                return wfmStrings.code();
            case SWIFT_CODE:
                return wfmStrings.swiftCode();
            case SORT_CODE:
                return wfmStrings.sortCode();
            case IBAN_CODE:
                return wfmStrings.ibanCode();
            case ABA_CODE:
                return wfmStrings.abaCode();
            case AGENT_ID:
                return wfmStrings.agentID();
            case FINANCIAL_INFORMATION:
                return wfmStrings.financialInformation();
            case CURRENCY:
                return wfmStrings.currency();
            case OPENING_BALANCE_DATE:
                return wfmStrings.asOfDate();
            case AMOUNT_TABLE:
                return wfmStrings.amount();
            case ATTACHMENTS:
                return wfmStrings.attachments();
            case ADDITIONAL_INFORMATION:
                return wfmStrings.additionalInformation();
        }
        return null;
    }

    public String localizeGoalManagement(String fieldID, String viewName, String departmentOrProject) {
        switch (fieldID) {
            case GOAL_DETAILS:
                return wfmStrings.basicDetails();
            case ATTACHMENTS_TITLE:
            case ATTACHMENTS:
                return wfmStrings.attachments();
            case NOTES:
            case CRM_NOTE:
                return wfmStrings.notes();
            case LINKS2:
                return wfmStrings.links();
            case GOAL_PERSONAL_ASSINESS:
                return wfmStrings.assignee();
            case GOAL_TITLE:
                return wfmStrings.title();
            case GOAL_DESCRIPTION:
                return wfmStrings.description();
            case GOAL_STATUS:
                return wfmStrings.status();
            case GOAL_START_DATE:
                return wfmStrings.period();
            case GOAL_ACTION_STEPS:
                return wfmStrings.actionSteps();
            case GOAL_MEASUREMENT_UNIT:
                return wfmStrings.measurementUnit();
            case DEPARTMENT_TARGET_GOAL_WIDGET:
                return wfmStrings.targetAndActual();
            case DEPARTMENT_GOAL_WEIGHT_WIDGET:
                return wfmStrings.weight();
            case GOAL_CHART:
            case CHART_SECTION:
                return wfmStrings.chart();
            case LOCATIONS:
                return wfmStrings.location();
            case GOAL_WEIGHT:
                return wfmStrings.weight();
            case GOAL_TO_DATE:
                return wfmStrings.dueDate();
            case GOAL_TARGET:
                return wfmStrings.target();
            case GOAL_ACTUAL:
                return wfmStrings.actual();
            case GOAL_VALIDITY_PERIOD:
                return wfmStrings.validityPeriod();
            case GOAL_PROORDEP:
                return departmentOrProject;
            case COMPANY_GOAL:
                return hrmsStrings.companyGoal();
            case GOAL_SCORE_CALCULATION:
                return wfmStrings.scoreCalculation();
            case ADDITIONAL_INFORMATION:
                return wfmStrings.additionalInformation();
            case LINKS:
                return wfmStrings.links();
            case GOAL_ASSIGNEES:
            case ASSIGNEES:
                return wfmStrings.assignees();
            case GOAL_RESOLVER:
                return wfmStrings.resolver();
            case GOAL_OUTCOME:
                return wfmStrings.outcome();
            case GOAL_NUMBER:
                return wfmStrings.code();
        }
        return null;
    }

    private String localizeGroupGoalManagement(String fieldID) {
        switch (fieldID) {
            case GOAL_DETAILS:
                return wfmStrings.basicDetails();
            case EMPLOYEE:
                return wfmStrings.employee();
            case APPROVERS:
                return wfmStrings.approver();
            case GOAL_VALIDITY_PERIOD:
                return wfmStrings.validityPeriod();
            case GOAL_START_DATE:
                return wfmStrings.startDate();
            case GOAL_TO_DATE:
                return wfmStrings.endDate();
            case ASSIGNED_GOALS:
                return hrmsStrings.goals();
            default:
                return null;
        }
    }

    private String localizeContractForm(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.DETAILS.equals(fieldID)) {
                return wfmStrings.contractDetails();
            } else if (CustomFormConstants.REGISTRATION_DATE.equals(fieldID)) {
                return wfmStrings.registeredDate();
            } else if (CustomFormConstants.NUMBER.equals(fieldID)) {
                return wfmStrings.contractNumber();
            } else if (CustomFormConstants.NAME.equals(fieldID)) {
                return wfmStrings.allowancebytheclient();
            } else if (CustomFormConstants.PROJECT.CLIENT.equals(fieldID)) {
                return Property.get(Constants.CLIENT_LIST, wfmStrings.customer());
            } else if (CustomFormConstants.TASK.PROJECT.equals(fieldID)) {
                return Property.get(Constants.PROJECT, wfmStrings.project());
            } else if (CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES.equals(fieldID)) {
                return wfmStrings.requirements();
            } else if (CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE.equals(fieldID)) {
                return Property.get(Constants.PROJECT, wfmStrings.projectEmployees(), wfmStrings.project());
            } else if (CustomFormConstants.CONTRACT.NOTE.equals(fieldID)) {
                return wfmStrings.notes();
            } else if (ATTACHMENTS.equals(fieldID)) {
                return wfmStrings.attachments();
            } else if (CustomFormConstants.PROJECT.DUE_DATE_REMINDER.equals(fieldID)) {
                return wfmStrings.duedatereminder();
            }
        }
        return null;
    }

    private String localizeFixedAssetForm(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.FIXED_ASSET_INFORMATION.equals(fieldID)) {
                return Property.get(Constants.FIXED_ASSETS, wfmStrings.basicDetails(), wfmStrings.fixedAsset());
            } else if (CustomFormConstants.FIXED_ASSET_FINANCING.equals(fieldID)) {
                return Property.get(Constants.FIXED_ASSETS, wfmStrings.financialInformation());
            } else if (CustomFormConstants.PRINT_BARCODE.equals(fieldID)) {
                return Property.get(Constants.PRINT_BARCODE, wfmStrings.printBarcode());
            } else if (CustomFormConstants.BARCODE.equals(fieldID)) {
                return Property.get(Constants.BARCODEE, wfmStrings.barcode());
            } else if (CustomFormConstants.DEPRECIATION_ACCOUNT.equals(fieldID)) {
                return wfmStrings.depreciationAccounts();
            } else if (CustomFormConstants.ADDITIONAL_INFORMATION.equals(fieldID)) {
                return wfmStrings.additionalInformation();
            } else if (CustomFormConstants.CODE.equals(fieldID)) {
                return wfmStrings.number();
            } else if (CustomFormConstants.NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (CustomFormConstants.COST.equals(fieldID)) {
                return wfmStrings.cost();
            } else if (CustomFormConstants.QUANTITY.equals(fieldID)) {
                return wfmStrings.qty();
            } else if (CustomFormConstants.PURCHASE_DATE.equals(fieldID)) {
                return wfmStrings.purchaseDate();
            } else if (CustomFormConstants.TAX_CALC_TYPE.equals(fieldID)) {
                return wfmStrings.amount();
            } else if (CustomFormConstants.OWNER.equals(fieldID)) {
                return wfmStrings.owner();
            } else if (CustomFormConstants.CATEGORY.equals(fieldID)) {
                return wfmStrings.category();
            } else if (CustomFormConstants.USEFUL_LIFE.equals(fieldID)) {
                return wfmStrings.useFulLife();
            } else if (CustomFormConstants.RESIDUAL_VALUE.equals(fieldID)) {
                return wfmStrings.residualValue();
            } else if (CustomFormConstants.TAX_VALUE.equals(fieldID)) {
                return wfmStrings.tax();
            } else if (CustomFormConstants.DEPARTMENT.equals(fieldID)) {
                return Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department());
            } else if (CustomFormConstants.UPLOAD_FORM.equals(fieldID)) {
                return wfmStrings.image();
            } else if (CustomFormConstants.LOCATION_FIELD.equals(fieldID)) {
                return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
            } else if (CustomFormConstants.DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if (CustomFormConstants.ACCOUNT_NAME.equals(fieldID)) {
                return wfmStrings.account();
            } else if (CustomFormConstants.RELATED_ITEM.equals(fieldID)) {
                return wfmStrings.convertedItem();
            } else if (CustomFormConstants.FIXED_ASSET_ACCOUNT.equals(fieldID)) {
                return wfmStrings.fixedAsset();
            } else if (CustomFormConstants.EXPENSE_ACCOUNT.equals(fieldID)) {
                return wfmStrings.expense();
            } else if (CustomFormConstants.SHOW_DESCRIPTION_IN_BARCODE.equals(fieldID)) {
                return wfmStrings.showDescriptionInBarcode();
            } else if (CustomFormConstants.BUTTONS.equals(fieldID)) {
                return wfmStrings.buttons();
            }
        }
        return null;
    }

    private String localizeProductForm(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.PRODUCT_INFORMATION.equals(fieldID)) {
                return wfmStrings.information();
            } else if (CustomFormConstants.FINANCIAL_INFORMATION.equals(fieldID)) {
                return wfmStrings.financialInformation();
            } else if (CustomFormConstants.MULTI_CURRENCY_PRICE_SETTINGS.equals(fieldID)) {
                return wfmStrings.multiCurrencyPriceSettings();
            } else if (CustomFormConstants.BILL_OF_MATERIALS.equals(fieldID)) {
                return wfmStrings.billOfMaterials();
            } else if (CustomFormConstants.INVENTORY_STOCK_INFORMATION.equals(fieldID)) {
                return wfmStrings.stockInformation();
            } else if (CustomFormConstants.PRODUCT_GROUP_ITEMS_INFORMATION.equals(fieldID)) {
                return wfmStrings.productKitItemInfo();
            } else if (CustomFormConstants.PARAMETRS.equals(fieldID)) {
                return wfmStrings.parameters();
            } else if (CustomFormConstants.MORE_OPTIONS.equals(fieldID)) {
                return wfmStrings.moreOptions();
            } else if (CustomFormConstants.ATTACHMENTS.equals(fieldID)) {
                return wfmStrings.attachments();
            } else if (CustomFormConstants.PRODUCT_CUSTOM_FIELDS.equals(fieldID)) {
                return wfmStrings.additionalFields();
            } else if (CustomFormConstants.PRODUCT_CATEGORY_CUSTOM_FIELDS.equals(fieldID)) {
                return wfmStrings.productCategoryCustomFields();
            } else if (CustomFormConstants.NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (CustomFormConstants.TYPE.equals(fieldID)) {
                return wfmStrings.type();
            } else if (CustomFormConstants.ACTIVE.equals(fieldID)) {
                return wfmStrings.active();
            } else if (CustomFormConstants.NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (CustomFormConstants.DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if ("SALES_PRICE".equals(fieldID)) {
                return wfmStrings.sellingPrice();
            } else if (AccountingCustomFormConstants.SALES_ACCOUNT.equals(fieldID)) {
                return wfmStrings.salesAccount();
            } else if (AccountingCustomFormConstants.FROM_PURCHASE_INVOICE.equals(fieldID)) {
                return wfmStrings.purchasedFromSupplier();
            } else if (AccountingCustomFormConstants.PURCHASE_PRICE.equals(fieldID)) {
                return wfmStrings.purchasePrice();
            } else if (AccountingCustomFormConstants.PURCHASE_ACCOUNT.equals(fieldID)) {
                return wfmStrings.purchaseAccount();
            } else if (CustomFormConstants.CATEGORY.equals(fieldID)) {
                return wfmStrings.category();
            } else if (CustomFormConstants.BRAND.equals(fieldID)) {
                return wfmStrings.brand();
            } else if (AccountingCustomFormConstants.INPUT_PRICE_LEVEL.equals(fieldID)) {
                return wfmStrings.priceLevel();
            } else if (CustomFormConstants.DISCOUNT_PANEL.equals(fieldID)) {
                return wfmStrings.discount();
            } else if (Constants.TAX.equals(fieldID)) {
                return wfmStrings.taxRate();
            } else if (CustomFormConstants.ATTACHMENTS.equals(fieldID)) {
                return wfmStrings.attachments();
            } else if ("SERIAL_ITEM_LEFT".equals(fieldID)) {
                return wfmStrings.serialNumberIn();
            } else if ("SERIAL_ITEM_RIGHT".equals(fieldID)) {
                return wfmStrings.serialNumberOut();
            } else if (SKU_NUMBER.equals(fieldID)) {
                return wfmStrings.skuNumber();
            } else if (UPC_NUMBER.equals(fieldID)) {
                return wfmStrings.upcNumber();
            } else if (UNIT_MEASUREMENT.equals(fieldID)) {
                return wfmStrings.unitMeasurement();
            } else if (MANUFACTURER.equals(fieldID)) {
                return wfmStrings.manufacturer();
            } else if (PART_NUMBER.equals(fieldID)) {
                return wfmStrings.partNumber();
            } else if (SUPPLIERS.equals(fieldID)) {
                return wfmStrings.suppliers();
            } else if (BARCODEE.equals(fieldID)) {
                return wfmStrings.barcode();
            } else if (TRACK_BATCHES.equals(fieldID)) {
                return wfmStrings.trackBatches();
            } else if (SOLD_TO_CUSTOMERS.equals(fieldID)) {
                return wfmStrings.soldToCustomers();
            } else if (DISCOUNT_TYPE.equals(fieldID)) {
                return accountingStrings.maximumDiscount();
            } else if (DISCOUNT_AMOUNT.equals(fieldID)) {
                return accountingStrings.discountAmount();
            } else if (MULTI_TABLE_PANEL.equals(fieldID)) {
                return wfmStrings.multiTablePanel();
            } else if (ASSEMBLY_ITEMS.equals(fieldID)) {
                return wfmStrings.assemblyItems();
            } else if (WAREHOUSE_PANEL.equals(fieldID)) {
                return wfmStrings.warehousePanel();
            } else if (PRODUCT_GROUP.equals(fieldID)) {
                return wfmStrings.productGroup();
            } else if (BARCODE.equals(fieldID)) {
                return wfmStrings.barcode();
            } else if (IMAGE_UPLOAD.equals(fieldID)) {
                return wfmStrings.imageUpload();
            } else if (INPUT_CUSTOM_FIELDS.equals(fieldID)) {
                return wfmStrings.inputCustomFields();
            } else if (BATCH_TRACK.equals(fieldID)) {
                return wfmStrings.batchTrack();
            } else if (BATCH_ITEMS.equals(fieldID)) {
                return wfmStrings.batchItems();
            } else if (SERIAL.equals(fieldID)) {
                return wfmStrings.serial();
            }
        }
        return null;
    }

    private String localizeIssueForm(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.DETAILS.equals(fieldID)) {
                return wfmStrings.issueDetails();
            } else if (CustomFormConstants.ASSIGNEE.equals(fieldID)) {
                return wfmStrings.assignees();
            } else if (CustomFormConstants.ATTACHMENTS_TITLE.equals(fieldID)) {
                return wfmStrings.attachments();
            } else if (CustomFormConstants.ATTACHMENTS.equals(fieldID)) {
                return wfmStrings.attachments();
            } else if (CustomFormConstants.LINKS2.equals(fieldID)) {
                return wfmStrings.links();
            } else if (CustomFormConstants.LINKS.equals(fieldID)) {
                return wfmStrings.links();
            } else if (CustomFormConstants.NOTES_TITLE.equals(fieldID)) {
                return wfmStrings.notes();
            } else if (CustomFormConstants.NOTES.equals(fieldID)) {
                return wfmStrings.notes();
            } else if (CustomFormConstants.UPDATES.equals(fieldID)) {
                return wfmStrings.updates();
            } else if (CustomFormConstants.NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (CustomFormConstants.DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if (CustomFormConstants.REPORTED_BY.equals(fieldID)) {
                return wfmStrings.reportedBy();
            } else if (CustomFormConstants.BILLABLE.equals(fieldID)) {
                return wfmStrings.billable();
            } else if (CustomFormConstants.PROJECT_FIELD.equals(fieldID)) {
                return Property.get(Constants.PROJECT, wfmStrings.project());
            } else if (CustomFormConstants.PERIOD.equals(fieldID)) {
                return wfmStrings.period();
            } else if (CustomFormConstants.RESOLVER.equals(fieldID)) {
                return wfmStrings.resolverOwner();
            } else if (CustomFormConstants.ENABLE_TIME_SHEET.equals(fieldID)) {
                return wfmStrings.enableTimesheet();
            } else if (CustomFormConstants.NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (CustomFormConstants.PRIORITY.equals(fieldID)) {
                return wfmStrings.priority();
            } else if (CustomFormConstants.STATUS.equals(fieldID)) {
                return wfmStrings.status();
            } else if (CustomFormConstants.VISIBILITY.equals(fieldID)) {
                return wfmStrings.visibility();
            } else if (CustomFormConstants.CREATED_BY.equals(fieldID)) {
                return wfmStrings.createdBy();
            } else if (CustomFormConstants.CREATED_DATE.equals(fieldID)) {
                return wfmStrings.createdDate();
            } else if (CustomFormConstants.UPDATED_BY.equals(fieldID)) {
                return wfmStrings.modifiedBy();
            } else if (CustomFormConstants.UPDATED_DATE.equals(fieldID)) {
                return wfmStrings.modifiedDate();
            } else if (CustomFormConstants.ASSIGNEES.equals(fieldID)) {
                return wfmStrings.assignees();
            } else if (ATTACHMENTS.equals(fieldID)) return wfmStrings.attachments();
            else if (LINKS.equals(fieldID)) return wfmStrings.links();
            else if (NOTES.equals(fieldID)) return wfmStrings.notes();
        }
        return null;
    }

    private String localizeCompanySettings(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.CS_COMPANY_DETAILS.equals(fieldID)) {
                return wfmStrings.companyDetailsP();
            } else if (CustomFormConstants.CS_COMPANY_NAME.equals(fieldID)) {
                return wfmStrings.companyName();
            } else if (CustomFormConstants.PHONE.equals(fieldID)) {
                return wfmStrings.officeNumber();
            } else if (CustomFormConstants.MOBILE_NUMBER.equals(fieldID)) {
                return wfmStrings.mobileNumber();
            } else if (CustomFormConstants.CS_FAX_NUMBER.equals(fieldID)) {
                return wfmStrings.fax();
            } else if (CustomFormConstants.CS_WEBSITE.equals(fieldID)) {
                return wfmStrings.website();
            } else if (CustomFormConstants.EMAIL.equals(fieldID)) {
                return wfmStrings.email();
            } else if (CustomFormConstants.INDUSTRY.equals(fieldID)) {
                return wfmStrings.industry();
            } else if (CustomFormConstants.CS_NO_OF_EMPLOYEES.equals(fieldID)) {
                return wfmStrings.numberOfEmployees();
            } else if (CustomFormConstants.CS_LICENSE_NO.equals(fieldID)) {
                return wfmStrings.licenseNo();
            } else if (CustomFormConstants.CS_LICENSE_START_DATE.equals(fieldID)) {
                return wfmStrings.licenseStartDate();
            } else if (CustomFormConstants.EXPIRATION_DATE.equals(fieldID)) {
                return wfmStrings.expiryDate();
            } else if (CustomFormConstants.CS_VISA_ALLOWANCE_LIMITS.equals(fieldID)) {  //contact details
                return wfmStrings.visaAllowanceLimit();
            } else if (CustomFormConstants.CS_WPS_LABOR_PAYROLL_ID.equals(fieldID)) {
                return wfmStrings.wpsLaborPayrollID();
            } else if (CustomFormConstants.CS_ACCOUNTING_AUDIT_FILE_DATE.equals(fieldID)) {
                return wfmStrings.accountingAuditFileDate();
            } else if (CustomFormConstants.CS_ACCOUNTING_AUDIT_FREQUENCY.equals(fieldID)) {
                return wfmStrings.accountingAuditFrequency();
            } else if (CustomFormConstants.ADDRESS_INFORMATION.equals(fieldID)) {
                return wfmStrings.addressInformation();
            } else if (CustomFormConstants.BILLING_ADDRESS.equals(fieldID)) {
                return wfmStrings.billingAddress();
            } else if (CompanyConsalidation.BILLING_ADDRESS_LINE1.equals(fieldID)) {//address information
                return wfmStrings.addressLine1();
            } else if (CompanyConsalidation.BILLING_ADDRESS_LINE2.equals(fieldID)) {
                return wfmStrings.addressLine2();
            } else if (CompanyConsalidation.BILLING_CITY.equals(fieldID)) {
                return wfmStrings.city();
            } else if (CompanyConsalidation.BILLING_COUNTRY.equals(fieldID)) {
                return wfmStrings.country();
            } else if (CompanyConsalidation.BILLING_STATE.equals(fieldID)) {
                return wfmStrings.state();
            } else if (CompanyConsalidation.BILLING_POST_CODE.equals(fieldID)) {
                return wfmStrings.postCode();
            } else if (CustomFormConstants.MAILING_ADDRESS.equals(fieldID)) {
                return wfmStrings.mailingShippingAddress();
            } else if (CustomFormConstants.CS_ADDRESS_LINE.equals(fieldID)) {
                return wfmStrings.addressLine1();
            } else if (CustomFormConstants.CS_ADDRESS_LINE2.equals(fieldID)) {
                return wfmStrings.addressLine2();
            } else if (CustomFormConstants.CS_MAILING_ADDRESS_CITY.equals(fieldID)) {
                return wfmStrings.city();
            } else if (CustomFormConstants.CS_MAILING_ADDRESS_COUNTRY.equals(fieldID)) {
                return wfmStrings.country();
            } else if (CustomFormConstants.CS_MAILING_ADDRESS_STATE.equals(fieldID)) {
                return wfmStrings.state();
            } else if (CustomFormConstants.CS_MAILING_ADDRESS_POSTAL_CODE.equals(fieldID)) {
                return wfmStrings.postCode();
            } else if (CustomFormConstants.CS_COMPANY_SETTINGS.equals(fieldID)) {
                return wfmStrings.companySettings();
            } else if (CustomFormConstants.LANGUAGE.equals(fieldID)) {
                return wfmStrings.language();
            } else if (CustomFormConstants.CS_SYTEM_THEME_COLOR.equals(fieldID)) {
                return wfmStrings.companyTheme();
            } else if (CustomFormConstants.CS_WEEK_START_ON.equals(fieldID)) {
                return wfmStrings.weekStartOn();
            } else if (CustomFormConstants.CS_ALTERNATE_CALENDAR.equals(fieldID)) {
                return wfmStrings.alternateCalendar();
            } else if (CustomFormConstants.CS_COMPANY_BBC_EMAIL.equals(fieldID)) {
                return wfmStrings.companyBccEmail();
            } else if (CustomFormConstants.CS_KPI_MODE.equals(fieldID)) {
                return wfmStrings.systemMode();
            } else if (CustomFormConstants.CS_SSL.equals(fieldID)) {
                return wfmStrings.SSLConnectionSettings();
            } else if (CustomFormConstants.CS_COMPANY_TIME_ZONE.equals(fieldID)) {
                return wfmStrings.companyTimezone();
            } else if (CustomFormConstants.CS_SHORT_DATE_FORMAT.equals(fieldID)) {
                return wfmStrings.shortDateFormat();
            } else if (CustomFormConstants.CS_LONG_DATE_FORMAT.equals(fieldID)) {
                return wfmStrings.longDateFormat();
            } else if (CustomFormConstants.CS_PDF_FONT_TYPE.equals(fieldID)) {
                return wfmStrings.pdfFont();
            } else if (CustomFormConstants.SHOW_ACCOUNTING.equals(fieldID)) {
                return wfmStrings.showAccountingSettings();
            } else if (CustomFormConstants.COMPANY_LOGOS.equals(fieldID)) {
                return wfmStrings.logos();
            } else if (CustomFormConstants.SYSTEM_ACCESS_DETAILS.equals(fieldID)) {
                return wfmStrings.systemAccessDetails();
            } else if (CustomFormConstants.IP_ADDRESS.equals(fieldID)) {
                return wfmStrings.ipaddress();
            } else if (CustomFormConstants.PASSWORD_EXPIRES_IN.equals(fieldID)) {
                return wfmStrings.passwordExpirationDayCount();
            } else if (CustomFormConstants.ENABLE_STORAGE_TYPES.equals(fieldID)) {
                return wfmStrings.enableStorageTypes();
            } else if (CustomFormConstants.ADDITIONAL_INFORMATION.equals(fieldID)) {
                return wfmStrings.additionalInformation();
            } else if (CustomFormConstants.CS_FINANCIAL_WIDGET.equals(fieldID)) {
                return wfmStrings.financialSettings();
            } else if (CustomFormConstants.CURRENCY.equals(fieldID)) {
                return wfmStrings.currency();
            } else if (CustomFormConstants.CS_FINANCIAL_YEAR_END.equals(fieldID)) {
                return wfmStrings.financialYearEnd();
            } else if (CustomFormConstants.CS_CONVERSION_DATE.equals(fieldID)) {
                return wfmStrings.conversionDate();
            } else if (CustomFormConstants.COMPANY_LOGO_LABEL.equals(fieldID)) {
                return wfmStrings.companyLogo();
            } else if (CustomFormConstants.COMPANY_LOGO.equals(fieldID)) {
                return wfmStrings.uploadLogo();
            } else if (CustomFormConstants.PDF_LOGO_LABEL.equals(fieldID)) {
                return wfmStrings.companyPdfLogo();
            } else if (CustomFormConstants.PDF_LOGO.equals(fieldID)) {
                return wfmStrings.uploadLogo();
            } else if (CRM_ACCOUNT_BILLING_ADDRESS.equals(fieldID)) {
                return wfmStrings.crmAccountBillingAddress();
            } else if (HIDDEN_FIELD.equals(fieldID)) {
                return wfmStrings.hiddenField();
            } else if (CRM_ACCOUNT_SHIPPING_ADDRESS.equals(fieldID)) {
                return wfmStrings.crmAccountShippingAddress();
            } else if (CS_ADDRESS_SAME.equals(fieldID)) {
                return wfmStrings.csAddressSame();
            } else if (SHOW_POWERED_BY_IN_PDF.equals(fieldID)) {
                return wfmStrings.showPoweredByInPdf();
            }
        }
        return null;
    }

    private String localizePlacementForm(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.PLACEMENT.BASIC_INFORMATION.equals(fieldID)) {
                return wfmStrings.basicDetails();
            } else if (CustomFormConstants.PLACEMENT.PLACEMENT.equals(fieldID)) {
                return wfmStrings.placementDetails();
            } else if (CustomFormConstants.PLACEMENT.PLACEMENT_FILE.equals(fieldID)) {
                return wfmStrings.meetingNotesAttachments();
            } else if (CustomFormConstants.PLACEMENT.PLACEMENT_NOTE.equals(fieldID)) {
                return wfmStrings.notes();
            } else if (CustomFormConstants.PLACEMENT.CANDIDATE.equals(fieldID)) {
                return wfmStrings.candidate();
            } else if (CustomFormConstants.PLACEMENT.CANDIDATE_VACANCIES.equals(fieldID)) {
                return wfmStrings.matchedVacancies();
            } else if (CustomFormConstants.PLACEMENT.DATE_OFFERED.equals(fieldID)) {
                return hrmsStrings.dateOffered();
            } else if (CustomFormConstants.PLACEMENT.DEPARTMENT.equals(fieldID)) {
                return Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department());
            } else if (CustomFormConstants.PLACEMENT.LOCATION.equals(fieldID)) {
                return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
            } else if (CustomFormConstants.PLACEMENT.POSITION.equals(fieldID)) {
                return wfmStrings.position();
            } else if (CustomFormConstants.PLACEMENT.PROJECT.equals(fieldID)) {
                return Property.get(Constants.PROJECT, wfmStrings.project());
            } else if (CustomFormConstants.PLACEMENT.ADDITIONAL_INFORMATION.equals(fieldID)) {
                return wfmStrings.additionalInformation();
            } else if (CustomFormConstants.PLACEMENT.PLACEMENT_CODE.equals(fieldID)) {
                return wfmStrings.code();
            } else if (CustomFormConstants.APPROVERS.equals(fieldID)) {
                return wfmStrings.approver();
            }
        }
        return null;
    }

    private String localizeVacancyForm(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.VACANCY.GENERAL_DETAILS.equals(fieldID)) {
                return wfmStrings.generalDetails();
            } else if (CustomFormConstants.VACANCY.INTERNAL_DETAILS.equals(fieldID)) {
                return wfmStrings.internalDetails();
            } else if (CustomFormConstants.VACANCY.POSITION_INFORMATION.equals(fieldID)) {
                return wfmStrings.positionInformation();
            } else if (CustomFormConstants.VACANCY.VACANCY_ATTACHMENTS.equals(fieldID)) {
                return wfmStrings.meetingNotesAttachments();
            }else if  (CustomFormConstants.VACANCY.VACANCY_QUESTIONS.equals(fieldID)) {
                return wfmStrings.customQuestions();
            } else if (CustomFormConstants.VACANCY.VACANCY_NOTES.equals(fieldID)) {
                return wfmStrings.notes();
            } else if (CustomFormConstants.VACANCY.VACANCY_NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (CustomFormConstants.VACANCY.MANAGER.equals(fieldID)) {
                return wfmStrings.manager();
            } else if (CustomFormConstants.VACANCY.POSITION.equals(fieldID)) {
                return wfmStrings.position();
            } else if (CustomFormConstants.VACANCY.LOCATION.equals(fieldID)) {
                return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
            } else if (CustomFormConstants.VACANCY.JOB_TITLE.equals(fieldID)) {
                return wfmStrings.jobTitle();
            } else if (CustomFormConstants.VACANCY.PROJECT.equals(fieldID)) {
                return Property.get(Constants.PROJECT, wfmStrings.project());
            } else if (CustomFormConstants.VACANCY.GENDER.equals(fieldID)) {
                return wfmStrings.gender();
            } else if (CustomFormConstants.VACANCY.PROPOSED_SALARY.equals(fieldID)) {
                return wfmStrings.proposedSalary();
            } else if (CustomFormConstants.VACANCY.JOB_REQUIREMENT.equals(fieldID)) {
                return wfmStrings.jobRequirements();
            } else if (CustomFormConstants.VACANCY.CONTRACT_PERIOD.equals(fieldID)) {
                return wfmStrings.contractPeriod();
            } else if (CustomFormConstants.VACANCY.VACANCY_TYPE.equals(fieldID)) {
                return wfmStrings.vacancyType();
            } else if (CustomFormConstants.VACANCY.DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if (CustomFormConstants.VACANCY.START_DATE.equals(fieldID)) {
                return wfmStrings.period();
            } else if (CustomFormConstants.VACANCY.END_DATE.equals(fieldID)) {
                return wfmStrings.endDate();
            } else if (CustomFormConstants.VACANCY.STATUS.equals(fieldID)) {
                return wfmStrings.status();
            } else if (CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT.equals(fieldID)) {
                return wfmStrings.vacantPlaceCount();
            } else if (CustomFormConstants.VACANCY.JOB_TYPE.equals(fieldID)) {
                return wfmStrings.jobType();
            } else if (CustomFormConstants.VACANCY.JOB_FAMILY.equals(fieldID)) {
                return wfmStrings.jobFamily();
            } else if (CustomFormConstants.VACANCY.RESPONSIBILITIES.equals(fieldID)) {
                return wfmStrings.responsibilities();
            } else if (CustomFormConstants.VACANCY.REQUIRED_DEGREE.equals(fieldID)) {
                return wfmStrings.requiredDegree();
            } else if (CustomFormConstants.VACANCY.ADDITIONAL_INFORMATION.equals(fieldID)) {
                return wfmStrings.additionalInformation();
            } else if (CustomFormConstants.VACANCY.BASIC_INFORMATION.equals(fieldID)) {
                return wfmStrings.basicDetails();
            } else if (CustomFormConstants.VACANCY.DETAILED_INFORMATION.equals(fieldID)) {
                return wfmStrings.detailedInformation();
            } else if (CustomFormConstants.VACANCY.VACANCY_WATCHER.equals(fieldID)) {
                return wfmStrings.vacancyWatcher();
            } else if (VACANCY.CURRENCY.equals(fieldID)) {
                return wfmStrings.currency();
            } else if (VACANCY.APPROVER.equals(fieldID)) {
                return wfmStrings.approver();
            }
        }
        return null;
    }

    private String localizeProduct(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (CustomFormConstants.NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (CustomFormConstants.DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if (CustomFormConstants.BRAND.equals(fieldID)) {
                return wfmStrings.brand();
            } else if (CustomFormConstants.CATEGORY.equals(fieldID)) {
                return wfmStrings.category();
            } else if (CustomFormConstants.COST_PRICE.equals(fieldID)) {
                return wfmStrings.costPrice();
            } else if (CustomFormConstants.SELLING_PRICE.equals(fieldID)) {
                return wfmStrings.sellingPrice();
            } else if (CustomFormConstants.COGS_ACCOUNT.equals(fieldID)) {
                return wfmStrings.cogsAccount();
            } else if (CustomFormConstants.ASSET_ACCOUNT.equals(fieldID)) {
                return wfmStrings.assetAccount();
            } else if (CustomFormConstants.QUANTITY_ON_HAND.equals(fieldID)) {
                return wfmStrings.qtyOnHand();
            }
        }
        return null;
    }

    private String localizeRentalProduct(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (CustomFormConstants.NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (CustomFormConstants.DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if (CustomFormConstants.BRAND.equals(fieldID)) {
                return wfmStrings.brand();
            } else if (CustomFormConstants.CATEGORY.equals(fieldID)) {
                return wfmStrings.category();
            } else if (CustomFormConstants.COST_PRICE.equals(fieldID)) {
                return wfmStrings.costPrice();
            } else if (CustomFormConstants.SELLING_PRICE.equals(fieldID)) {
                return wfmStrings.sellingPrice();
            } else if (CustomFormConstants.COGS_ACCOUNT.equals(fieldID)) {
                return wfmStrings.cogsAccount();
            } else if (CustomFormConstants.ASSET_ACCOUNT.equals(fieldID)) {
                return wfmStrings.assetAccount();
            } else if (CustomFormConstants.ITEMS.equals(fieldID)) {
                return wfmStrings.rentalPricing();
            } else if (CustomFormConstants.PRODUCT_INFORMATION.equals(fieldID)) {
                return wfmStrings.rentalInformation();
            } else if (CustomFormConstants.QUANTITY_ON_HAND.equals(fieldID)) {
                return wfmStrings.qtyOnHand();
            }
        }
        return null;
    }

    private String localizeRentalOrder(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (CustomFormConstants.ITEMS.equals(fieldID)) {
                return wfmStrings.rentalPricing();
            } else if (CustomFormConstants.DATE.equals(fieldID)) {
                return wfmStrings.expiryDate();
            } else if (CustomFormConstants.NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if ("CUSTOMER".equals(fieldID)) {
                return wfmStrings.customer();
            } else if (CLIENT_INVOICE_TERM.equals(fieldID)) {
                return wfmStrings.paymentTerms();
            } else if (CustomFormConstants.INFORMATION.equals(fieldID)) {
                return wfmStrings.rentalInformation();
            }
        }
        return null;
    }

    private String localizeHrmsNews(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.NEWS_SUBJECT.equals(fieldID)) {
                return wfmStrings.subject();
            } else if (CustomFormConstants.NEWS_SHORT_DESCRIPTION.equals(fieldID)) {
                return wfmStrings.shortDescription();
            } else if (CustomFormConstants.NEWS_FULL_TEXT.equals(fieldID)) {
                return wfmStrings.fullText();
            } else if (CustomFormConstants.NEWS_CATEGORIES.equals(fieldID)) {
                return wfmStrings.category();
            } else if (CustomFormConstants.LOCATION_FIELD.equals(fieldID)) {
                return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
            } else if (CustomFormConstants.NEWS_VISIBILITY.equals(fieldID)) {
                return wfmStrings.visibility();
            } else if (CustomFormConstants.NEWS_UPLOAD_FILE.equals(fieldID)) {
                return wfmStrings.featuredImage();
            } else if (CustomFormConstants.NEWS_PUBLISH_DATE.equals(fieldID)) {
                return wfmStrings.date();
            } else if (CustomFormConstants.NEWS_AUTHOR.equals(fieldID)) {
                return wfmStrings.author();
            } else if (CustomFormConstants.HRMS_ADD_NEWS.equals(fieldID)) {
                return wfmStrings.addNews();
            } else if (CustomFormConstants.HRMS_COMPANY_NEWS.equals(fieldID)) {
                return wfmStrings.basicDetails();
            } else if (CustomFormConstants.COMMENTS.equals(fieldID)) {
                return wfmStrings.comments();
            } else if (CustomFormConstants.COMMENT_BOX.equals(fieldID)) {
                return wfmStrings.comments();
            } else if (CANDIDATE.LOCATION.equals(fieldID))
                return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
        }
        return null;
    }

    private String localizeProject(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.DETAILS.equals(fieldID)) {
                return Property.get(Constants.PROJECT, wfmStrings.basicDetails(), wfmStrings.project());
            } else if (CustomFormConstants.PROJECT.CREATE_CLONE_WIDGET.equals(fieldID)) {
                return wfmStrings.cloneProjectWidget();
            } else if (CustomFormConstants.NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (CustomFormConstants.NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (CustomFormConstants.DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if (CustomFormConstants.START_DATE.equals(fieldID)) {
                return wfmStrings.startDate();
            } else if (CustomFormConstants.DUE_DATE.equals(fieldID)) {
                return wfmStrings.dueDate();
            } else if (CustomFormConstants.STATUS.equals(fieldID)) {
                return wfmStrings.status();
            } else if (CustomFormConstants.PROJECT.COMPLETED.equals(fieldID)) {
                return wfmStrings.completed();
            } else if (CustomFormConstants.PDF_VERSION.equals(fieldID)) {
                return wfmStrings.pdfVersion();
            } else if (CustomFormConstants.PROJECT.DUE_DATE_REMINDER.equals(fieldID)) {
                return wfmStrings.duedatereminder();
            } else if (CustomFormConstants.PARENT.equals(fieldID)) {
                return wfmStrings.parent();
            } else if (CustomFormConstants.PROJECT.CLIENT.equals(fieldID)) {
                return Property.get(Constants.CLIENT_LIST, wfmStrings.customer());
            } else if (CustomFormConstants.PROJECT.LOCATION.equals(fieldID)) {
                return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
            } else if (CustomFormConstants.PROJECT.CONTRACT.equals(fieldID)) {
                return wfmStrings.contract();
            } else if (CustomFormConstants.ADDITIONAL_INFORMATION.equals(fieldID)) {
                return wfmStrings.additionalInformation();
            } else if (CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES.equals(fieldID)) {
                return wfmStrings.involvedEmployees();
            } else if (PROJECT.CHECK_IN_LOCATIONS.equals(fieldID)) {
                return wfmStrings.geolocation();
            } else if (CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE.equals(fieldID)) {
                return wfmStrings.members();
            } else if (CustomFormConstants.PROJECT.ASSIGN_NEW_MEMBERS_TO_PROJECT.equals(fieldID)) {
                return wfmStrings.assignNewMembersToProjectTasks();
            } else if (CustomFormConstants.PROJECT.BACKUP_MANAGER.equals(fieldID)) {
                return wfmStrings.backupManagers();
            } else if (CustomFormConstants.MANAGER.equals(fieldID)) {
                return wfmStrings.manager();
            } else if (CustomFormConstants.PROJECT.ESTIMATED_TIME.equals(fieldID)) {
                return wfmStrings.estimatedTime();
            } else if (CustomFormConstants.PROJECT.TIME_SPENT.equals(fieldID)) {
                return wfmStrings.timeSpentOnly();
            } else if (CustomFormConstants.PROJECT.ESTIMATED_COST.equals(fieldID)) {
                return wfmStrings.costEstimated();
            } else if (CustomFormConstants.PROJECT.ACTUAL_COST.equals(fieldID)) {
                return wfmStrings.actualCost();
            } else if (CustomFormConstants.PROJECT.ACTUAL_START_DATE.equals(fieldID)) {
                return wfmStrings.actualStartDate();
            } else if (CustomFormConstants.PROJECT.ACTUAL_END_DATE.equals(fieldID)) {
                return wfmStrings.actualEndDate();
            } else if (CustomFormConstants.PROJECT.ACTUAL_TIME_SPENT.equals(fieldID)) {
                return wfmStrings.actualTimeSpent();
            } else if (CustomFormConstants.ATTACHMENTS.equals(fieldID)) {
                return wfmStrings.attachments();
            } else if (CustomFormConstants.LINKS.equals(fieldID)) {
                return wfmStrings.links();
            } else if (CustomFormConstants.PROJECT.PROJECT_NOTE.equals(fieldID)) {
                return wfmStrings.notes();
            } else if (CustomFormConstants.PROJECT.BILLIBLE.equals(fieldID)) {
                return wfmStrings.billable();
            } else if (CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT.equals(fieldID)) {
                return wfmStrings.employeeAssignment();
            } else if (CustomFormConstants.PROJECT.PROJECT_CHARTS.equals(fieldID)) {
                return wfmStrings.chart();
            } else if (CustomFormConstants.PROJECT.PROJECT_CHART.equals(fieldID)) {
                return wfmStrings.chart();
            } else if (CustomFormConstants.MORE_DETAILS.equals(fieldID)) {
                return wfmStrings.moreDetails();
            } else if (CustomFormConstants.PROJECT.TASK_DETAILS.equals(fieldID)) {
                return Property.get(Constants.TASK, wfmStrings.taskDetails(), wfmStrings.task());
            } else if (CustomFormConstants.PROJECT.NOT_STARTED_TASKS.equals(fieldID)) {
                return wfmStrings.notStartedTasks();
            } else if (CustomFormConstants.PROJECT.IN_PORGRESS_TASKS.equals(fieldID)) {
                return wfmStrings.inProgressTasks();
            } else if (CustomFormConstants.PROJECT.COMPLETED_TASKS.equals(fieldID)) {
                return wfmStrings.completedTasks();
            } else if (CustomFormConstants.PROJECT.CANCELLED_TASKS.equals(fieldID)) {
                return wfmStrings.cancelledTasks();
            } else if (CustomFormConstants.PROJECT.WAITING_FOR_SOMEONE_ELSE.equals(fieldID)) {
                return wfmStrings.waitingForSomeone();
            } else if (CustomFormConstants.PROJECT.CLOSED_TASKS.equals(fieldID)) {
                return wfmStrings.closedTasks();
            } else if (CustomFormConstants.PROJECT.CREATED_BY.equals(fieldID)) {
                return wfmStrings.createdBy();
            } else if (CustomFormConstants.PROJECT.CREATED_DATE.equals(fieldID)) {
                return wfmStrings.createdDate();
            } else if (CustomFormConstants.PROJECT.UPDATED_BY.equals(fieldID)) {
                return wfmStrings.modifiedBy();
            } else if (CustomFormConstants.PROJECT.UPDATED_DATE.equals(fieldID)) {
                return wfmStrings.modifiedDate();
            } else if (CustomFormConstants.PROJECT.WAITING_HOURS.equals(fieldID)) {
                return wfmStrings.waitingHours();
            } else if (CustomFormConstants.PROJECT.REJECTED_HOURS.equals(fieldID)) {
                return projectStrings.rejectedHours();
            } else if (CustomFormConstants.PROJECT.PROJECT_INVOICES_DETAILS.equals(fieldID)) {
                return "Account Details";
            } else if (CustomFormConstants.PROJECT.PROJECT_INVOICES.equals(fieldID)) {
                return accountingStrings.invoice();
            } else if (CustomFormConstants.PROJECT.LOGGED_TIME_DETAILS.equals(fieldID)) {
                return wfmStrings.loggedTimeDetails();
            } else if (CustomFormConstants.PROJECT.LOGGED_TIMES.equals(fieldID)) {
                return "Logged Time";
            } else if (CustomFormConstants.PROJECT.CLIENT_BALANCE.equals(fieldID)) {
                return Property.get(Constants.CLIENT_LIST, wfmStrings.clientBalance(), wfmStrings.customer());
            } else if (CustomFormConstants.PROJECT.CLIENT_RETAINER.equals(fieldID)) {
                return wfmStrings.clientRetainer();
            } else if (CustomFormConstants.PROJECT.BILLIBLE.equals(fieldID)) {
                return wfmStrings.billable();
            } else if (UPDATES.equals(fieldID)) {
                return wfmStrings.updates();
            }
        }
        return null;
    }

    private String localizeTask(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.TASK.TASK_DETAILS.equals(fieldID)) {
                return Property.get(Constants.TASK, wfmStrings.taskDetails(), wfmStrings.task());
            } else if (CustomFormConstants.TASK.PROJECT.equals(fieldID)) {
                return wfmStrings.project();
            } else if (CustomFormConstants.TASK.DETAILED_FORM.equals(fieldID)) {
                return wfmStrings.showDetails();
            } else if (CustomFormConstants.NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if (START_DATE.equals(fieldID)) {
                return wfmStrings.startDate();
            } else if (DUE_DATE.equals(fieldID)) {
                return wfmStrings.dueDate();
            } else if (WORKFLOW_DATE.equals(fieldID)) {
                return wfmStrings.date();
            } else if (WORKFLOW_TIME_BASED_HEADER.equals(fieldID)) {
                return wfmStrings.timeBasedAction();
            } else if (WORKFLOW_TIME_BASED.equals(fieldID)) {
                return wfmStrings.executionTime();
            } else if (ASSIGNEE.equals(fieldID) || CustomFormConstants.TASK.TASK_ASSIGNEES.equals(fieldID)) {
                return wfmStrings.assignees();
            } else if (CustomFormConstants.TASK.BILLIBLE.equals(fieldID)) {
                return wfmStrings.billable();
            } else if (PRIORITY.equals(fieldID)) {
                return wfmStrings.priority();
            } else if (STATUS.equals(fieldID)) {
                return wfmStrings.status();
            } else if (RECALCULATE_HOURS_ON_RESOURCE_UTIL.equals(fieldID)) {
                return wfmStrings.recalculateHoursOnResourceUtilizationTool();
            } else if (CustomFormConstants.TASK.STOPWATCH.equals(fieldID)) {
                return "";
            } else if (CustomFormConstants.TASK.TIME_SPENT.equals(fieldID)) {
                return wfmStrings.timeSpent();
            } else if (CustomFormConstants.TASK.TASK_AMOUNT.equals(fieldID)) {
                return wfmStrings.taskAmount();
            } else if (CustomFormConstants.TASK.PERCENT.equals(fieldID)) {
                return wfmStrings.completed();
            } else if (CustomFormConstants.TASK.LOG_TIME.equals(fieldID)) {
                return wfmStrings.logTime();
            } else if (ATTACHMENTS.equals(fieldID)) {
                return wfmStrings.attachments();
            } else if (CustomFormConstants.TASK.PARENT_WORKSTREAM.equals(fieldID)) {
                return wfmStrings.parentWorkstream();
            } else if (CustomFormConstants.TASK.PREDECESSOR_TASK.equals(fieldID)) {
                return wfmStrings.predeccessor();
            } else if (CustomFormConstants.TASK.SUCCESSOR_TASK.equals(fieldID)) {
                return wfmStrings.successorTasks();
            } else if (CustomFormConstants.TASK.DUE_DATE_REMINDER.equals(fieldID)) {
                return wfmStrings.duedatereminder();
            } else if (CustomFormConstants.TASK.DUE_DATE_REMINDER_ADD_LINK.equals(fieldID)) {
                return wfmStrings.addReminder();
            } else if (CustomFormConstants.TASK.RECURRENING.equals(fieldID)) {
                return wfmStrings.recurring();
            } else if (CustomFormConstants.TASK.ADVANCED_OPTIONS.equals(fieldID)) {
                return wfmStrings.advancedOptions();
            } else if (CustomFormConstants.ADDITIONAL_INFORMATION.equals(fieldID)) {
                return wfmStrings.additionalInformation();
            } else if (CustomFormConstants.LINKS.equals(fieldID)) {
                return wfmStrings.links();
            } else if (CustomFormConstants.TASK.TASK_NOTE.equals(fieldID)) {
                return wfmStrings.notes();
            } else if (CustomFormConstants.TASK.TIMER.equals(fieldID)) {
                return wfmStrings.timer();
            } else if (CustomFormConstants.PDF_VERSION.equals(fieldID)) {
                return wfmStrings.pdfVersion();
            } else if (CustomFormConstants.TASK.PROJECT_CLIENTS.equals(fieldID)) {
                return Property.get(Constants.CLIENT_LIST, wfmStrings.customer());
            } else if (CustomFormConstants.TASK.DEPENDENCIES.equals(fieldID)) {
                return wfmStrings.dependencies();
            } else if (CustomFormConstants.TASK.CREATED_BY.equals(fieldID)) {
                return wfmStrings.createdBy();
            } else if (CustomFormConstants.TASK.CREATED_DATE.equals(fieldID)) {
                return wfmStrings.createdDate();
            } else if (CustomFormConstants.TASK.UPDATED_BY.equals(fieldID)) {
                return wfmStrings.modifiedBy();
            } else if (CustomFormConstants.TASK.UPDATED_DATE.equals(fieldID)) {
                return wfmStrings.modifiedDate();
            } else if (CustomFormConstants.UPDATES.equals(fieldID)) {
                return wfmStrings.updates();
            } else if (CustomFormConstants.MORE_DETAILS.equals(fieldID)) {
                return wfmStrings.moreDetails();
            } else if (CustomFormConstants.TASK.PM.equals(fieldID)) {
                return wfmStrings.projectManager();
            } else if (CustomFormConstants.TASK.BACKUP_MANAGERS.equals(fieldID)) {
                return wfmStrings.backupManagers();
            } else if (CustomFormConstants.TASK.CLIENT.equals(fieldID)) {
                return Property.get(Constants.CLIENT_LIST, wfmStrings.customer());
            } else if (CustomFormConstants.TASK.ESTIMATED_TIME.equals(fieldID)) {
                return wfmStrings.estimatedTime();
            } else if (CustomFormConstants.TASK.ESTIMATED_COST.equals(fieldID)) {
                return wfmStrings.costEstimated();
            } else if (CustomFormConstants.TASK.ACTUAL_COST.equals(fieldID)) {
                return wfmStrings.actualCost();
            } else if (CustomFormConstants.TASK.ACTUAL_START_DATE.equals(fieldID)) {
                return wfmStrings.actualStartDate();
            } else if (CustomFormConstants.TASK.ACTUAL_END_DATE.equals(fieldID)) {
                return wfmStrings.actualEndDate();
            } else if (CustomFormConstants.TASK.ACTUAL_TIME_SPENT.equals(fieldID)) {
                return wfmStrings.actualTimeSpent();
            } else if (CustomFormConstants.TASK.WAITING_HOURS.equals(fieldID)) {
                return wfmStrings.waitingHours();
            } else if (CustomFormConstants.TASK.REJECTED_HOURS.equals(fieldID)) {
                return projectStrings.rejectedHours();
            } else if (CustomFormConstants.TASK.DEPENDENCIES.equals(fieldID)) {
                return wfmStrings.dependencies();
            } else if (CustomFormConstants.TASK.TYPE.equals(fieldID)) {
                return wfmStrings.type();
            } else if (CustomFormConstants.DETAILS.equals(fieldID)) {
                return wfmStrings.details();
            } else if (CustomFormConstants.APPROVER.equals(fieldID)) {
                return wfmStrings.approver();
            } else if (TASK.TASK_STATUS_HISTORY.equals(fieldID)) {
                return wfmStrings.statusHistory();
            } else if (DETAILS.equals(fieldID)) return wfmStrings.details();
            else if (APPROVER.equals(fieldID)) return wfmStrings.approver();
        }
        return null;
    }

    private String localizeEmployee(String fieldID) {
        if (EMPLOYEE_INFORMATION.equals(fieldID)) {
            return wfmStrings.employeeInformation();
        } else if (CustomFormConstants.FIRST_NAME.equals(fieldID)) {
            return wfmStrings.firstName();
        } else if (CustomFormConstants.LAST_NAME.equals(fieldID)) {
            return wfmStrings.lastName();
        } else if (MIDDLE_NAME.equals(fieldID)) {
            return wfmStrings.middleName();
        } else if (OTHER_NAME.equals(fieldID)) {
            return wfmStrings.otherName();
        } else if (DRIVER_ID.equals(fieldID)) {
            return "Driver ID";
        } else if (BIRTH_DAY.equals(fieldID)) {
            return wfmStrings.dateOfBirth();
        } else if (GENDER.equals(fieldID)) {
            return wfmStrings.gender();
        } else if (NATIONALITY.equals(fieldID)) {
            return wfmStrings.nationality();
        } else if (MARTIAL_STATUS.equals(fieldID)) {
            return wfmStrings.maritalStatus();
        } else if (LANGUAGE.equals(fieldID)) {
            return wfmStrings.language();
        } else if (CONTACT_INFORMATION.equals(fieldID)) {  //contact details
            return Property.get(Constants.Contacts, wfmStrings.contactDetails(), wfmStrings.contact());
        } else if (CustomFormConstants.EMAIL.equals(fieldID)) {
            return wfmStrings.email();
        } else if (CustomFormConstants.PHONE.equals(fieldID)) {
            return wfmStrings.phone();
        } else if (IM_ADDRESS.equals(fieldID)) {
            return wfmStrings.imAddress();
        } else if (WEB_ADDRESS.equals(fieldID)) {
            return wfmStrings.webAddress();
        } else if (ADDRESS_INFORMATION.equals(fieldID)) {//address information
            return wfmStrings.addressInformation();
        } else if (ADDRESS.equals(fieldID)) {
            return wfmStrings.address();
        } else if (EMPLOYMENT_INFORMATION.equals(fieldID)) { //employment information
            return wfmStrings.employmentInformation();
        } else if (EMPLOYEE_CODE.equals(fieldID)) {
            return wfmStrings.employeeCode();
        } else if (CustomFormConstants.DEPARTMENT.equals(fieldID)) {
            return Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department());
        } else if (POSITION.equals(fieldID)) {
            return wfmStrings.position();
        } else if (LOCATION_FIELD.equals(fieldID)) {
            return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
        } else if (SUPERVISOR.equals(fieldID)) {
            return wfmStrings.supervisor();
        } else if (QUALIFICATION.equals(fieldID)) {
            return wfmStrings.qualification();
        } else if (COMPETENCIES.equals(fieldID)) {
            return wfmStrings.competencies();
        } else if (HIRE_DATE.equals(fieldID)) {
            return wfmStrings.hireDate();
        } else if (RESIGNATION_DATE.equals(fieldID)) {
            return wfmStrings.resignationDate();
        } else if (EMPLOYMENT_CONTACT_TERMS.equals(fieldID)) {
            return wfmStrings.employmentContractTerms();
        } else if (EMPLOYMENT_MODE.equals(fieldID)) {
            return wfmStrings.employmentMode();
        } else if (SALARY_GRADE.equals(fieldID)) {
            return wfmStrings.paymentCategory();
        } else if (JOB_TITLE.equals(fieldID)) {
            return wfmStrings.jobTitle();
        } else if (SALARY_AMOUNT.equals(fieldID)) {
            return wfmStrings.basicSalary();
        } else if (EMPLOYMENT_HISTORY.equals(fieldID)) {
            return wfmStrings.employmentHistory();
        } else if (WAGE_RATE.equals(fieldID)) {
            return wfmStrings.wageRate();
        } else if (CLIENT_CHARGE_RATE.equals(fieldID)) {
            return wfmStrings.clientChargeRate();
        } else if (BANK_ACCOUNT_INFORMATION.equals(fieldID)) { //bank information
            return Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccountInformation(), wfmStrings.bankAccount());
        } else if (BANK_NAME.equals(fieldID)) {
            return wfmStrings.bankName();
        } else if (ACCOUNT_NUMBER.equals(fieldID)) {
            return wfmStrings.accountNumber();
        } else if (ACCOUNT_NAME.equals(fieldID)) {
            return wfmStrings.accountName();
        } else if (BANK_ADDRESS.equals(fieldID)) {
            return wfmStrings.bankAddress();
        } else if (SWIFT_CODE.equals(fieldID)) {
            return wfmStrings.swiftCode();
        } else if (SORT_CODE.equals(fieldID)) {
            return wfmStrings.sortCode();
        } else if (IBAN_CODE.equals(fieldID)) {
            return wfmStrings.ibanCode();
        } else if (AGENT_ID.equals(fieldID)) {
            return wfmStrings.agentID();
        } else if (ACCOUNT_INFORMATION.equals(fieldID)) { //account information
            return wfmStrings.basicInfo();
        } else if (ACCOUNT_STATUS.equals(fieldID)) {
            return wfmStrings.accountStatus();
        } else if (ACCOUNT_ROLES.equals(fieldID)) {
            return wfmStrings.roles();
        } else if (NO_ACCESS.equals(fieldID)) {
            return wfmStrings.noAccess();
        } else if (ESS_USER.equals(fieldID)) {
            return wfmStrings.essUser();
        } else if (PERSONAL_IDENTITY_INFORMATION.equals(fieldID)) {//Personal Identity Information
            return wfmStrings.personalIdentityInformation();
        } else if (PASSPORT_NUMBER.equals(fieldID)) {
            return wfmStrings.passportNumber();
        } else if (PASSPORT_ISSUE.equals(fieldID)) { //country
            return wfmStrings.passportIssueBy();
        } else if (PASSPORT_ISSUE_DATE.equals(fieldID)) {
            return wfmStrings.passportIssueDate();
        } else if (PASSPORT_EXPIRY_DATE.equals(fieldID)) {
            return wfmStrings.passportExpireDate();
        } else if (INSURANCE_NUMBER.equals(fieldID)) {
            return wfmStrings.insuranseNumber();
        } else if (VISA_NUMBER.equals(fieldID)) {
            return wfmStrings.visaNumber();
        } else if (VISA_ISSUE_DATE.equals(fieldID)) {
            return wfmStrings.visaIssueDate();
        } else if (VISA_EXPIRATION_DATE.equals(fieldID)) {
            return wfmStrings.visaExpirationDate();
        } else if (VISA_EXPIRATION_DATE_REMINDER.equals(fieldID)) {
            return wfmStrings.setExpirationReminder();
        } else if (ATTACHMENTS.equals(fieldID)) {  //attachments
            return wfmStrings.attachments();
        } else if (ADDITIONAL_INFORMATION.equals(fieldID)) {
            return wfmStrings.additionalInformation();
        } else if (INSTRUCTOR_COURSES.equals(fieldID)) {
            return wfmStrings.courses();
        } else if (INSURANCE_EXPIRY_DATE.equals(fieldID)) {
            return wfmStrings.insuranceExpiryDate();
        } else if (YEAR_LEAVE_DURATION.equals(fieldID)) {
            return wfmStrings.leaveRequestDurationYDT();
        } else if (YEAR_LEAVE_HOUR_DURATION.equals(fieldID)) {
            return wfmStrings.leaveRequestHourDurationYDT();
        } else if (PAYROLL_STARTER.EMPLOYEE_DETAILS.equals(fieldID)) {
            return wfmStrings.employeeInformation();
        } else if (PAYROLL_STARTER.PAYMENT_SETTINGS.equals(fieldID)) {
            return wfmStrings.paymentSettings();
        } else if (PAYROLL_STARTER.PAYROLL_SETTINGS.equals(fieldID)) {
            return wfmStrings.payrollSettings();
        } else if (PAYROLL_STARTER.EMPLOYEE_OVERTIME_SETTINGS.equals(fieldID)) {
            return wfmStrings.overTimeSettings();
        } else if (PAYMENT_DEDUCTION_INFORMATION.equals(fieldID)) {
            return wfmStrings.paymentDeductionCategoryTable();
        } else if (PAYROLL_STARTER.EMPLOYEE_PAYROLL_SETTINGS.equals(fieldID)) {
            return wfmStrings.employeePayrollSettings();
        } else if (PUNISHMENT_PROMOTION.equals(fieldID)) {
            return wfmStrings.punishmentsVSPromotions();
        } else if (INTERNAL_EMPLOYMENT.equals(fieldID)) {
            return wfmStrings.internalEmployments();
        } else if (PAST_EMPLOYMENT.equals(fieldID)) {
            return wfmStrings.pastEmployments();
        } else if (PAYMENT_METHOD.equals(fieldID)) {
            return wfmStrings.salaryLevel();
        } else if (TIMESLOT_HISTORY_LOG.equals(fieldID)) {
            return wfmStrings.historyLog();
        } else if (HOLIDAY.HOLIDAY_HISTORY_LOG.equals(fieldID)) {
            return wfmStrings.historyLog();
        } else if (CONTRACT_START_DATE.equals(fieldID)) {
            return wfmStrings.contractStart();
        } else if (CONTRACT_END_DATE.equals(fieldID)) {
            return wfmStrings.contractEnd();
        } else if (APPROVER.equals(fieldID)) {
            return wfmStrings.approver();
        } else if (PAYROLL_STARTER.EMPLOYEE_IS_PAID.equals(fieldID)) {
            return wfmStrings.employeeIsPaid();
        } else if (PAYROLL_STARTER.NORMAL_RATE.equals(fieldID)) {
            return wfmStrings.normalRate();
        } else if (PAYROLL_STARTER.OVER_TIME_RATE.equals(fieldID)) {
            return wfmStrings.overtimeRate();
        } else if (PAYROLL_STARTER.PAY_PERIOD.equals(fieldID)) {
            return wfmStrings.payPeriod();
        } else if (PAYROLL_STARTER.PAY_METHOD.equals(fieldID)) {
            return wfmStrings.paymentMethod();
        } else if (PAYROLL_STARTER.EMPLOYEE_BONUS.equals(fieldID)) {
            return wfmStrings.bonus();
        } else if (WPS_NUMBER.equals(fieldID)) {
            return !"".equals(Utils.getPersonalID()) ? Utils.getPersonalID() : wfmStrings.wpsNumber();
        } else if (PAYROLL_STARTER.FAMILY_STATUS.equals(fieldID)) {
            return wfmStrings.maritalStatus();
        } else if (PAYROLL_STARTER.JOB_DETAILS.equals(fieldID)) {
            return wfmStrings.jobDetails();
        } else if (PAYROLL_STARTER.OVERTIME_SETTINGS_TABLE.equals(fieldID)) {
            return wfmStrings.overTimeSettings();
        } else if (PAYMENT_TABLE.equals(fieldID)) {
            return wfmStrings.recurringPaymentCategory();
        } else if (DEDUCTION_TABLE.equals(fieldID)) {
            return wfmStrings.recurringDeductionCategory();
        } else if (TAX_TABLE.equals(fieldID)) {
            return wfmStrings.taxes();
        } else if (LOAN_TABLE.equals(fieldID)) {
            return wfmStrings.loans();
        } else if (LR_TYPE_SICK_LEAVE.equals(fieldID)) {
            return wfmStrings.sickLeave();
        } else if (LR_TYPE_STUDY_LEAVE.equals(fieldID)) {
            return wfmStrings.studyLeave();
        } else if (LR_TYPE_ANNUAL_LEAVE.equals(fieldID)) {
            return wfmStrings.annualLeave();
        } else if (LR_TYPE_LATE.equals(fieldID)) {
            return wfmStrings.lateLeave();
        } else if (LR_TYPE_SPECIAL.equals(fieldID)) {
            return wfmStrings.specialLeave();
        } else if (LR_TYPE_UNAUTHORIZED_LEAVE.equals(fieldID)) {
            return wfmStrings.unauthorizedLeave();
        } else if (LR_TYPE_OTHER_LEAVE.equals(fieldID)) {
            return wfmStrings.otherLeave();
        } else if (CustomFormConstants.POSITIONS.APPLY_ALLOUNCE_ALL_EMPLOYEE.equals(fieldID)) {
            return wfmStrings.applyToAllEmployee();
        } else if (OPENING_BALANCE_DATE.equals(fieldID)) {
            return wfmStrings.openingBalanceForAnnualLeave();
        } else if (PROBATION_DAYS.equals(fieldID)) {
            return wfmStrings.probationPeriodDays();
        } else if (PROFILE_PICTURE.equals(fieldID)) {
            return wfmStrings.profilePicture();
        } else if (SALARY_TOTAL_AMOUNT.equals(fieldID)) {
            return wfmStrings.totalSalary();
        } else if (CANDIDATE.LOCATION.equals(fieldID)) {
            return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
        } else if (TELEGRAM.equals(fieldID)) {
            return wfmStrings.telegram();
        } else if (EMPLOYER_CONTRIBUTION.equals(fieldID)) {
            return wfmStrings.employerContribution();
        } else if (EMPLOYEE_DEGREE.equals(fieldID)) {
            return wfmStrings.employeeDegree();
        } else if ("LR_TYPE_RESIGNED".equals(fieldID)) {
            return wfmStrings.resigned();
        } else if (TIMESLOT.equals(fieldID)) {
            return wfmStrings.timeslot();
        } else if (EXPERIENCE.equals(fieldID)) {
            return wfmStrings.workExperience();
        }
        return null;
    }

    private String localizeOnboardingStep(String fieldID) {
        if (ONBOARDING.ONBOARDING_STEP_TITLE.equals(fieldID)) {
            return wfmStrings.onboardingDetails();
        } else if (ONBOARDING.ONBOARDING_STEP_ACTIVITY_NAME.equals(fieldID)) {
            return wfmStrings.onboardingName();
        } else if (ONBOARDING.ONBOARDING_STEP_ACTIVITY_DESCRIPTION.equals(fieldID)) {
            return wfmStrings.description();
        } else if (ONBOARDING.ONBOARDING_STEP_ACTIVITY_PERIOD.equals(fieldID)) {
            return wfmStrings.onboardingPeriod();
        } else if (ONBOARDING.ONBOARDING_STEP_SHOW_IN_EMPLOYEE_PROFILE.equals(fieldID)) {
            return wfmStrings.showInEmployeeProfile();
        } else if (ONBOARDING.ONBOARDING_STEP_CREATE_FORM.equals(fieldID)) {
            return wfmStrings.createOnboardingEntryForm();
        } else if (ONBOARDING.ONBOARDING_STEP_EMPLOYEES.equals(fieldID)) {
            return wfmStrings.responsiblePeople();
        } else if (ONBOARDING.ONBOARDING_STEP_STATUSES.equals(fieldID)) {
            return wfmStrings.onboardingStepStatuses();
        } else if (ONBOARDING.ONBOARDING_STEP_PARENT_STEPS.equals(fieldID)) {
            return wfmStrings.previousStep();
        }
        return null;
    }

    private String localizeCourseSchedule(String fieldID) {

        if (TRAINING_CENTER.LOCATION.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
        } else if (TRAINING_CENTER.COURSE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.course();
        } else if (TRAINING_CENTER.LANGUAGE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.language();
        } else if (TRAINING_CENTER.SCHEDULED_COURSE.START_DATE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.startDate();
        } else if (TRAINING_CENTER.INSTRUCTOR.equalsIgnoreCase(fieldID)) {
            return wfmStrings.instructor();
        } else if (TRAINING_CENTER.ASSESSOR.equalsIgnoreCase(fieldID)) {
            return wfmStrings.assessor();
        } else if (TRAINING_CENTER.NUMBER_OF_SEATS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.numberOfSeats();
        } else if (NUMBER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.number();
        } else if (TRAINING_CENTER.SCHEDULED_COURSE.OVERTIME.equalsIgnoreCase(fieldID)) {
            return wfmStrings.overtime();
        } else if (TRAINING_CENTER.SCHEDULED_COURSE.DURATION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.duration();
        } else if (TRAINING_CENTER.SCHEDULED_COURSE.NUMBER_OF_SEATS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.numberOfSeats();
        } else if (TRAINING_CENTER.SCHEDULED_COURSE.COURSEREQUIREMENTS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.courseRequirements();
        }
        return null;
    }

    private String localizeSaleQuote(String fieldID) {
        String[] values = fieldID.split(",");
        fieldID = values.length > 1 ? values[0] : fieldID;
        if (ACCOUNTING.CUSTOMER.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.CLIENT_LIST, wfmStrings.customer());
        } else if (ACCOUNTING.PROJECT.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.PROJECT, wfmStrings.project());
        } else if (START_DATE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.startDate();
        } else if (INVOICE_NUMBER.equalsIgnoreCase(fieldID) || NUMBER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.invoiceNumber();
        } else if (ACCOUNTING.INVOICE_DATE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.date();
        } else if (ACCOUNTING.DUE_DATE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.dueDate();
        } else if (ACCOUNTING.MANAGER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.manager();
        } else if (ACCOUNTING.REFERENCE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.reference();
        } else if (ACCOUNTING.SQ_NUMBER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.sqNumber();
        } else if (ACCOUNTING.PO_NUMBER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.poNumber();
        } else if (ACCOUNTING.SHIP_VIA.equalsIgnoreCase(fieldID)) {
            return accountingStrings.shipping();
        } else if (ACCOUNTING.SUB_TOTAL.equalsIgnoreCase(fieldID)) {
            return wfmStrings.subtotal();
        } else if (ACCOUNTING.TOTAL.equalsIgnoreCase(fieldID)) {
            return wfmStrings.total();
        } else if (ACCOUNTING.TOTAL_AMOUNT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.totalAmount();
        } else if (ACCOUNTING.PAID_AMOUNT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.paidAmount();
        } else if (ACCOUNTING.PAID_AMOUNT_BASE_CURRENCY.equalsIgnoreCase(fieldID)) {
            return wfmStrings.paidAmountBaseCurrency();
        } else if (ACCOUNTING.DUE_AMOUNT_BASE_CURRENCY.equalsIgnoreCase(fieldID)) {
            return wfmStrings.dueAmountBaseCurrency();
        } else if (ACCOUNTING.DUE_AMOUNT_INVOICE_CURRENCY.equalsIgnoreCase(fieldID)) {
            return wfmStrings.dueAmountInvoiceCurrency();
        } else if (ACCOUNTING.TOTAL_INVOICE_CURRENCY.equalsIgnoreCase(fieldID)) {
            return wfmStrings.totalInvoiceCurrency();
        } else if (ACCOUNTING.TOTAL_DISCOUNT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.discount();
        } else if (ACCOUNTING.STATUS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.status();
        } else if (ACCOUNTING.SUPPLIER.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier());
        } else if (ACCOUNTING.SHIPPING_TERMS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.shippingTerms();
        } else if (ACCOUNTING.PAYMENT_TERMS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.paymentTerms();
        } else if (ACCOUNTING.PAYMENT_TYPE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.paymentType();
        } else if (ACCOUNTING.SALE_QUOTE.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote());
        } else if (CustomFormConstants.INVOICE_TYPE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.invoiceType();
        } else if (CustomFormConstants.AMOUNT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.taxAmount();
        } else if (ACCOUNTING.BANK_ACCOUNT.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccount());
        } else if (ACCOUNTING.INSTRUCTIONS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.paymentInstructions();
        } else if (CURRENCY.equalsIgnoreCase(fieldID)) {
            return wfmStrings.currency();
        } else if (CURRENT_APPROVER.equals(fieldID)) {
            return wfmStrings.currentApprover();
        } else if (CURRENT_APPROVER_EMAIL.equals(fieldID)) {
            return wfmStrings.currentApproverEmail();
        } else if (CURRENT_APPROVER_STATUS.equals(fieldID)) {
            return wfmStrings.currentApproverStatus();
        } else if (NEXT_APPROVER.equals(fieldID)) {
            return wfmStrings.nextApprover();
        } else if (NEXT_APPROVER_EMAIL.equals(fieldID)) {
            return wfmStrings.nextApproverEmail();
        } else if (NEXT_APPROVER_STATUS.equals(fieldID)) {
            return wfmStrings.nextApproverStatus();
        } else if (PREV_APPROVER.equals(fieldID)) {
            return wfmStrings.prevApprover();
        } else if (PREV_APPROVER_EMAIL.equals(fieldID)) {
            return wfmStrings.prevApproverEmail();
        } else if (PREV_APPROVER_STATUS.equals(fieldID)) {
            return wfmStrings.prevApproverStatus();
        }
        return values.length >= 2 ? values[1] + " - " + wfmStrings.itemTable() : null;
    }

    private String localizeCashAdvanceAndLeaveRequest(String fieldID, Boolean isCashAdvanse) {
        String[] values = fieldID.split(",");
        fieldID = values.length > 1 ? values[0] : fieldID;
        if (PREV_APPROVER.equals(fieldID)) {
            return wfmStrings.prevApprover();
        } else if (PREV_APPROVER_EMAIL.equals(fieldID)) {
            return wfmStrings.prevApproverEmail();
        } else if (PREV_APPROVER_STATUS.equals(fieldID)) {
            return wfmStrings.prevApproverStatus();
        } else if (CURRENT_APPROVER.equals(fieldID)) {
            return wfmStrings.currentApprover();
        } else if (CURRENT_APPROVER_EMAIL.equals(fieldID)) {
            return wfmStrings.currentApproverEmail();
        } else if (CURRENT_APPROVER_STATUS.equals(fieldID)) {
            return wfmStrings.currentApproverStatus();
        } else if (NEXT_APPROVER.equals(fieldID)) {
            return wfmStrings.nextApprover();
        } else if (NEXT_APPROVER_EMAIL.equals(fieldID)) {
            return wfmStrings.nextApproverEmail();
        } else if (NEXT_APPROVER_STATUS.equals(fieldID)) {
            return wfmStrings.nextApproverStatus();
        } else if (REGISTERED_BY.equals(fieldID)) {
            return wfmStrings.registeredBy();
        } else if (REGISTERED_BY_EMAIL.equals(fieldID)) {
            return wfmStrings.registeredByEmail();
        } else if (CONFIRM_DATE.equals(fieldID)) {
            return wfmStrings.confirmDate();
        } else if (DESCRIPTION.equals(fieldID)) {
            return wfmStrings.description();
        } else if (DUE_DATE.equals(fieldID)) {
            return wfmStrings.dueDate();
        } else if (DUE_TIME.equals(fieldID)) {
            return wfmStrings.dueTime();
        } else if (OTHER_REASON.equals(fieldID)) {
            return wfmStrings.otherReason();
        } else if (REASON.equals(fieldID)) {
            return wfmStrings.reason();
        } else if (START_DATE.equals(fieldID)) {
            return wfmStrings.startDate();
        } else if (START_TIME.equals(fieldID)) {
            return wfmStrings.startTime();
        } else if (TAKE_LIVE_TYPE.equals(fieldID)) {
            return wfmStrings.leaveType();
        } else if (TYPE.equals(fieldID)) {
            return wfmStrings.type();
        } else if (PAYROLL.CATEGORY.equals(fieldID)) {
            return wfmStrings.category();
        } else if (PAYROLL.DESCRIPTION.equals(fieldID)) {
            return wfmStrings.description();
        } else if (PAYROLL.PAYMENT_METHOD.equals(fieldID)) {
            return wfmStrings.paymentMethod();
        } else if (PAYROLL.REQUESTED_AMOUNT.equals(fieldID)) {
            return wfmStrings.requestedAmount();
        } else if (PAYROLL.STATUS.equals(fieldID)) {
            return wfmStrings.status();
        } else if (PAYROLL.REQUESTER.equals(fieldID)) {
            return wfmStrings.requester();
        } else if (ACCOUNTING.EXPENSE_CLAIM.NUMBER.equals(fieldID)) {
            return wfmStrings.number();
        } else if (ACCOUNTING.EXPENSE_CLAIM.FIXED_ASSET.equals(fieldID)) {
            return wfmStrings.fixedAsset();
        } else if (ACCOUNTING.EXPENSE_CLAIM.REPORT_TITLE.equals(fieldID)) {
            return wfmStrings.reportTitle();
        } else if (ACCOUNTING.EXPENSE_CLAIM.REPORTER.equals(fieldID)) {
            return wfmStrings.reporter();
        } else if (ACCOUNTING.EXPENSE_CLAIM.DATE.equals(fieldID)) {
            return wfmStrings.date();
        } else if (ACCOUNTING.EXPENSE_CLAIM.PROJECT.equals(fieldID)) {
            return Property.get(Constants.PROJECT, wfmStrings.project());
        } else if (ACCOUNTING.EXPENSE_CLAIM.TOTAL.equals(fieldID)) {
            return wfmStrings.total();
        } else if (CREATED_DATE.equals(fieldID)) {
            return wfmStrings.createdDate();
        } else if (DETAILS.equals(fieldID)) {
            return wfmStrings.details();
        } else if (CustomFormConstants.PAYROLL_STARTER.PURPOSE.equals(fieldID)) {
            return wfmStrings.purpose();
        } else if (ATTACHMENTS.equals(fieldID)) {
            return wfmStrings.attachments();
        } else if (DATE_PERIOD.equals(fieldID)) {
            return wfmStrings.leave() + " " + wfmStrings.period();
        } else if (EMPLOYEES.equals(fieldID)) {
            return wfmStrings.employee();
        } else if (PAID.equals(fieldID)) {
            return wfmStrings.paid();
        } else if (CALENDAR.equals(fieldID)) {
            return wfmStrings.calendar();
        } else if (CHART.equals(fieldID)) {
            return wfmStrings.chart();
        } else if (ASSIGNEES.equals(fieldID)) {
            return wfmStrings.assignees();
        } else if (APPROVER.equals(fieldID)) {
            return wfmStrings.approver();
        } else if (GENERAL_INFORMATION.equals(fieldID)) {
            return wfmStrings.generalInformation();
        } else if (SEND_NOTIFICATION.equals(fieldID)) {
            return wfmStrings.sendNotification();
        } else if (SUBMITTED_DATE.equals(fieldID)) {
            return wfmStrings.submittedDate();
        } else if (LAST_NAME.equals(fieldID)) return wfmStrings.lastName();
        else if (FIRST_NAME.equals(fieldID)) return wfmStrings.firstName();
        else if (PHONE.equals(fieldID)) return wfmStrings.phone();
        else if (RELATIONSHIP.equals(fieldID)) return wfmStrings.relationship();
        else if (ADDRESS.equals(fieldID)) return wfmStrings.streetAddress1();
        else if (ADDRESS_2.equals(fieldID)) return wfmStrings.streetAddress2();
        else if (CITY.equals(fieldID)) return wfmStrings.city();
        else if (COUNTRY.equals(fieldID)) return wfmStrings.country();
        else if (MIDDLE_NAME.equals(fieldID)) return wfmStrings.middleName();
        else if (EMPLOYEE.equals(fieldID)) return isCashAdvanse ? wfmStrings.requester() : wfmStrings.employee();
        else if (PAYROLL_STARTER.REQUESTED_DATE.equals(fieldID)) return wfmStrings.requestedDate();
        else if (PAYROLL_STARTER.PAYMENT_AMOUNT.equals(fieldID)) return wfmStrings.paymentAmount();
        else if (EXCHANGE_RATE.equals(fieldID)) return wfmStrings.exchangeRate();
        else if (REFERENCE.equals(fieldID)) return wfmStrings.reference();
        else if (PAYROLL_STARTER.PAY_FROM.equals(fieldID)) return wfmStrings.payFrom();
        else if (PAYROLL_STARTER.PAYMENT_TERMS.equals(fieldID)) return wfmStrings.paymentTerms();
        else if (PAYROLL_STARTER.CASH_ADVANCE_ACCOUNT.equals(fieldID)) return wfmStrings.cashAdvanceAccount();
        return values.length >= 2 ? values[1] + " - " + wfmStrings.itemTable() : null;
    }

    private String localizePayrollCategory(String fieldID) {
        if (CODE.equals(fieldID)) {
            return wfmStrings.code();
        } else if (NAME.equals(fieldID)) {
            return wfmStrings.name();
        } else if (DEBIT_TO_ACCOUNT.equals(fieldID)) {
            return wfmStrings.debitToAccount();
        } else if (CREDIT_TO_ACCOUNT.equals(fieldID)) {
            return wfmStrings.creditToAccount();
        } else if (IS_CASH_ADVANCE.equals(fieldID)) {
            return Property.get(Constants.CASH_ADVANCE_LIST, wfmStrings.isCashAdvance(), wfmStrings.cashAdvance());
        } else if (PAYMENTCATEGORY.equals(fieldID)) {
            return wfmStrings.paymentCategory();
        } else if (PENSION_PURPOSE.equals(fieldID)) {
            return wfmStrings.pensionPurpose();
        } else if (NIC_PURPOSE.equals(fieldID)) {
            return wfmStrings.nICPurposes();
        } else if (PAYE_PURPOSE.equals(fieldID)) {
            return wfmStrings.pAYEPurposes();
        }
        return null;
    }

    private String localizeSMS(String fieldID) {
        if (SMS_INFORMATION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.smsInformation();
        } else if (MODULE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.apps();
        } else if (NAME.equalsIgnoreCase(fieldID)) {
            return wfmStrings.name();
        } else if (CONTENT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.content();
        } else if (IS_DEFAULT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.isDefault();
        }
        return null;
    }

    private String localizeSolution(String fieldID) {
        if (CRM_SOLUTION_INFORMATION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.solutionInformation();
        } else if (CRM_SOLUTION_DESCRIPTION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.solutionDescription();
        } else if (ATTACHMENTS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.attachments();
        }
        return null;
    }

    private String localizeEvent(String fieldID) {
        if (SUBJECT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.subject();
        } else if (START_DATE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.startDate();
        } else if (END_DATE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.endDate();
        } else if (DESCRIPTION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.description();
        } else if (PROJECT.LOCATION.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
        } else if (CREATOR.equalsIgnoreCase(fieldID)) {
            return wfmStrings.createdBy();
        } else if (CALL_TYPE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.callType();
        } else if (WHEN.equalsIgnoreCase(fieldID)) {
            return wfmStrings.when();
        } else if (WHERE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.where();
        } else if (WORKFLOW_TIME_BASED_HEADER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.timeBasedAction();
        } else if (INFORMATION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.information();
        } else if (WORKFLOW_TIME_BASED.equalsIgnoreCase(fieldID)) {
            return wfmStrings.executionTime();
        } else if (EVENT_INFORMATION.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.EVENT_LIST, wfmStrings.basicDetails(), wfmStrings.event());
        } else if (CALL_INFORMATION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.logaCallInformation();
        } else if (CALL_TYPE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.callType();
        } else if (CALL_DURATION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.callDuration();
        } else if (CALL_DETAILS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.callDetails();
        } else if (SHARED_WITH.equalsIgnoreCase(fieldID)) {
            return wfmStrings.sharedWith();
        } else if (ATTACHMENTS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.attachments();
        } else if (ADVANCED.equalsIgnoreCase(fieldID)) {
            return wfmStrings.advanced();
        } else if (GUESTS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.guests();
        } else if (ENABLE_REMINDER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.reminders();
        } else if (RECURRING_WIDGET.equalsIgnoreCase(fieldID)) {
            return wfmStrings.recurrence();
        } else if (CALL_CLONE.equals(fieldID)) {
            return wfmStrings.callClone();
        } else if (INVITATION_RESPONSE.equals(fieldID)) {
            return wfmStrings.invitationResponse();
        } else if (WORKFLOW_TELEGRAM_ALERT_FORM.TELEGRAM_BOT.equals(fieldID)) {
            return wfmStrings.telegramBot();
        } else if (WORKFLOW_TELEGRAM_ALERT_FORM.RECIPIENT_ATTRIBUTES.equals(fieldID)) {
            return wfmStrings.recipientAttributes();
        } else if (WORKFLOW_TELEGRAM_ALERT_FORM.RECEIVER.equals(fieldID)) {
            return wfmStrings.receiver();
        } else if (WORKFLOW_TELEGRAM_ALERT_FORM.CONTENT.equals(fieldID)) {
            return wfmStrings.content();
        }
        return null;
    }

    private String localizeCrmAccount(String fieldID) {
        if (CRM_ACCOUNT_INFORMATION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.basicInfo();
        } else if (CRM_ACCOUNT_ADDRESS_INFORMATION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.addressInformation();
        } else if (CRM_ACTIVITIES.equalsIgnoreCase(fieldID)) {
            return Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.activities());
        } else if (CRM_ACCOUNT_LATEST_CONTACTS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.latestContacts();
        } else if (ATTACHMENTS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.attachments();
        } else if (CRM_ACCOUNT_FINANCIAL_INFORMATION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.financialInformation();
        } else if (CRM_NOTE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.notes();
        } else if (CRM_ACCOUNT_OWNER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.accountEmployee();
        } else if (CRM_ACCOUNT_NAME.equalsIgnoreCase(fieldID) || SUPPLIER_ACCOUNT_NAME.equalsIgnoreCase(fieldID)) {
            return wfmStrings.accountName();
        } else if (CRM_ACCOUNT_PARENT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.parentaccount();
        } else if (CRM_ACCOUNT_NUMBER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.accountNumber();
        } else if (PRIMARY_CONTACT.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.Contacts, wfmStrings.primaryContact(), wfmStrings.contact());
        } else if (CRM_ACCOUNT_TYPE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.accountType();
        } else if (CLIENT_TYPE.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.CLIENT_LIST, wfmStrings.clientType(), wfmStrings.customer());
        } else if (CRM_ACCOUNT_INDUSTRY.equalsIgnoreCase(fieldID)) {
            return wfmStrings.industry();
        } else if (CRM_ACCOUNT_OWNERSHIP.equalsIgnoreCase(fieldID)) {
            return wfmStrings.ownership();
        } else if (CRM_ACCOUNT_EMAIL.equalsIgnoreCase(fieldID)) {
            return wfmStrings.email();
        } else if (CRM_ACCOUNT_PHONE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.phone();
        } else if (CRM_ACCOUNT_WEBSITE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.accountWebsite();
        } else if (CRM_ACCOUNT_FAX.equalsIgnoreCase(fieldID)) {
            return wfmStrings.fax();
        } else if (CRM_ACCOUNT_ORGANIZATION_TYPE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.organizationType();
        } else if (CRM_ACCOUNT_NUMBER_OF_EMPLOYEE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.numberOfEmployees();
        } else if (CRM_ACCOUNT_ANNUAL_REVENUE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.annualRevenue();
        } else if (CRM_ACCOUNT_RATING.equalsIgnoreCase(fieldID)) {
            return wfmStrings.rating();
        } else if (CRM_ACCOUNT_BILLING_ADDRESS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.billingAddress();
        } else if (CRM_ACCOUNT_SHIPPING_ADDRESS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.shippingAddress();
        } else if (CURRENCY.equalsIgnoreCase(fieldID)) {
            return wfmStrings.currency();
        } else if (VAT_NUMBER.equalsIgnoreCase(fieldID)) {
            return Utils.ConvertNiffToRucc(wfmStrings.vatNumber());
        } else if (PAYMENT_METHOD.equalsIgnoreCase(fieldID)) {
            return wfmStrings.paymentMethod();
        } else if (CRM_ACCOUNT_PAYMENT_TYPE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.paymentType();
        } else if (PAY_FOR_NO_SHOWS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.payForNoShows();
        } else if (REGISTRATION_NUMBER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.registrationNumber();
        } else if (CLIENT_AS_OF_DATE.equalsIgnoreCase(fieldID) || SUPPLIER_AS_OF_DATE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.asOfDate();
        } else if (CLIENT_AMOUNT.equalsIgnoreCase(fieldID) || SUPPLIER_AMOUNT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.openingBalance();
        } else if (CLIENT_SUBSIDIARIES.equalsIgnoreCase(fieldID)) {
            return (Utils.hasGenericAccess(GenericSettingsEnum.CONSIGNMENT_FUNCTION_ENABLE) && Utils.isMultiCompanySubsidiary()) ? wfmStrings.headOffice() : wfmStrings.subsidiary()/*wfmStrings.subsidiary()*/;
        } else if (CLIENT_CREDIT_LIMIT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.creditLimit();
        } else if (CLIENT_QUOTE_CREDIT_LIMIT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.quoteCreditLimit();
        } else if (CLIENT_INVOICE_TERM.equalsIgnoreCase(fieldID)) {
            return wfmStrings.terms();
        } else if (SUPPLIER_BANK_ACCOUNT_DETAILS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.bankAccountDetails();
        } else if (SUPPLIER_BANK_NAME.equalsIgnoreCase(fieldID)) {
            return wfmStrings.bankName();
        } else if (SUPPLIER_ACCOUNT_NUMBER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.accountNo();
        } else if (SUPPLIER_SWIFT_CODE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.swiftCode();
        } else if (SUPPLIER_SORT_CODE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.sortCode();
        } else if (SUPPLIER_IBAN_CODE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.ibanCode();
        } else if (SUPPLIER_BRANCH.equalsIgnoreCase(fieldID)) {
            return wfmStrings.branch();
        } else if (SUPPLIER_BANK_ADDRESS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.bankAddress();
        } else if (ADDITIONAL_INFORMATION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.additionalInformation();
        } else if (CLIENT_VAT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.tax();
        } else if (CLIENT_DISCOUNT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.discount();
        } else if (GL_ACCOUNT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.createGLAccount();
        } else if (PRICE_LEVEL.equalsIgnoreCase(fieldID)) {
            return wfmStrings.priceLevel();
        } else if (CRM_ACCOUNT_INFORMATION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.basicInfo();
        } else if (CRM_ACCOUNT_LATEST_CONTACTS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.latestContacts();
        } else if (CRM_ACCOUNT_FINANCIAL_INFORMATION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.financialInformation();
        } else if (CRM_ACCOUNT_PRODUCT_SERVICE.equalsIgnoreCase(fieldID)) {
            return Property.getPluralWithObjectCode(Constants.PRODUCTS_OR_SERVICES, wfmStrings.productsOrServices());
        } else if (CLIENT_BANK_ACCOUNT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.bankAccounts();
        } else if (VAT_CATEGORIES.equalsIgnoreCase(fieldID)) {
            return wfmStrings.taxExemptionReason();
        } else if (SUPPLIER_VAT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.tax();
        } else if (ACCOUNTS_RECEIVABLE_PAYABLE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.accountsPayable();
        } else if (PRIMARY_CONTACT_ADDRESSES.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.Contacts, wfmStrings.primaryContactAddress(), wfmStrings.contact());
        } else if (IMAGE_UPLOAD.equalsIgnoreCase(fieldID)) {
            return wfmStrings.uploadLogo();
        } else if (WAREHOUSE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.defaultWarehouse();
        } else if (DEPARTMENT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.default2() + " " + Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department());
        } else if (CRM_ACCOUNT_ITEM_TABLE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.itemTable();
        } else if (TELEGRAM.equals(fieldID)) {
            return wfmStrings.telegram();
        } else if (CRM_ACCOUNT_TAX_TREATMENT.equals(fieldID)) {
            return wfmStrings.crmAccountTaxTreatment();
        } else if (CRM_ACCOUNT_TRN.equals(fieldID)) {
            return wfmStrings.crmAccountTrn();
        } else if (CRM_ACCOUNT_PLACEOFSUPPLY_COUNTRY.equals(fieldID)) {
            return wfmStrings.crmAccountPlaceOfSupplyCountry();
        } else if (CRM_ACCOUNT_PLACEOFSUPPLY_STATE.equals(fieldID)) {
            return wfmStrings.crmAccountPlaceOfSupplyState();
        } else if (CLIENT_ITEM_TABLE.equals(fieldID)) {
            return wfmStrings.clientItemTable();
        } else if (SUPPLIER_ITEM_TABLE.equals(fieldID)) {
            return wfmStrings.supplierItemTable();
        } else if (SALES_TYPE.equals(fieldID)) {
            return wfmStrings.salesType();
        } else if (PASSPORT_NUM.equals(fieldID)) {
            return wfmStrings.passportNumber();
        } else if (CR_NUMBER.equals(fieldID)) {
            return wfmStrings.crNumber();
        }
        return null;
    }

    private String localizeWorkflowForm(String fieldID) {
        if (RULE_INFORMATION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.ruleInformation();
        } else if (EXECUTION_CRITERIA.equalsIgnoreCase(fieldID)) {
            return wfmStrings.executionCriteria();
        } else if (RULE_CRITERIA.equalsIgnoreCase(fieldID)) {
            return wfmStrings.ruleCriteria();
        } else if (NAME.equals(fieldID)) {
            return wfmStrings.name();
        } else if (MODULE.equals(fieldID)) {
            return wfmStrings.apps();
        } else if (STATUS.equals(fieldID)) {
            return wfmStrings.status();
        } else if (DESCRIPTION.equals(fieldID)) {
            return wfmStrings.description();
        } else if (WORKFLOW_ALERT_FORM.FROM_EMAIL.equals(fieldID)) {
            return wfmStrings.fromEmail();
        } else if (WORKFLOW_ALERT_FORM.RECEPIENT.equals(fieldID)) {
            return wfmStrings.recipient();
        } else if (WORKFLOW_ALERT_FORM.BCC_CC_PANEL.equals(fieldID)) {
            return wfmStrings.bccAndCcPanel();
        } else if (WORKFLOW_ALERT_FORM.INCLUDE_PDF.equals(fieldID)) {
            return wfmStrings.includePDF();
        } else if (WORKFLOW_ALERT_FORM.FROM_NAME.equals(fieldID)) {
            return wfmStrings.fromName();
        } else if (WORKFLOW_ALERT_FORM.TEMPLATE.equals(fieldID)) {
            return wfmStrings.template();
        } else if (WORKFLOW_ALERT_FORM.SUBJECT.equals(fieldID)) {
            return wfmStrings.subject();
        } else if (WORKFLOW_TIME_BASED.equals(fieldID)) {
            return wfmStrings.workflowTimeBased();
        } else if (WORKFLOW_TIME_BASED_HEADER.equals(fieldID)) {
            return wfmStrings.workflowTimeBasedHeader();
        } else if (INFORMATION.equals(fieldID)) {
            return wfmStrings.information();
        } else if (CONTENT.equals(fieldID)) {
            return wfmStrings.content();
        }
        return null;
    }

    private String localizeOpportunity(String fieldID) {
        String[] values = fieldID.split(",");
        fieldID = values.length > 1 ? values[0] : fieldID;
        if (CRM_NOTE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.notes();
        } else if (OPPORTUNITY_INFORMATION.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.Opportunities, wfmStrings.basicDetails(), wfmStrings.opportunity());
        } else if (CRM_OPPORTUNITY_ASSIGNEE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.assignee();
        } else if (CRM_OPPORTUNITY_BACKUP_ASSIGNEE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.backupAssignee();
        } else if (CRM_OPPORTUNITY_NUMBER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.number();
        } else if (CRM_OPPORTUNITY_NAME.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.Opportunities, wfmStrings.opportunityName(), wfmStrings.opportunity());
        } else if (CRM_OPPORTUNITY_ACCOUNT_NAME.equalsIgnoreCase(fieldID)) {
            return wfmStrings.accountName();
        } else if (CRM_OPPORTUNITY_CONTACT_NAME.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.Contacts, wfmStrings.contactName(), wfmStrings.contact());
        } else if (CRM_OPPORTUNITY_TYPE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.type();
        } else if (CRM_OPPORTUNITY_NEXT_STEP.equalsIgnoreCase(fieldID)) {
            return wfmStrings.nextStep();
        } else if (CRM_OPPORTUNITY_AMOUNT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.amount();
        } else if (CURRENCY.equalsIgnoreCase(fieldID)) {
            return wfmStrings.currency();
        } else if (CRM_OPPORTUNITY_CLOSING_DATE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.closingDate();
        } else if (CRM_OPPORTUNITY_STAGE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.stage();
        } else if (CRM_OPPORTUNITY_PROBABILITY.equalsIgnoreCase(fieldID)) {
            return wfmStrings.probability();
        } else if (CRM_OPPORTUNITY_EXPECTED_REVENUE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.expectedRevenue();
        } else if (CRM_OPPORTUNITY_CAMPAIGN_SOURCE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.campaignSource();
        } else if (CRM_OPPORTUNITY_LEAD_SOURCE.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.LEADS, wfmStrings.leadSource(), wfmStrings.lead());
        } else if (CRM_ESTIMETOR.equalsIgnoreCase(fieldID)) {
            return wfmStrings.estimator();
        } else if (CRM_OPPORTUNITY_ATTACHMENTS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.attachments();
        } else if (GET_PRODUCT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.getProduct();
        } else if (CRM_OPPORTUNITY_INCLUDE_ITEMS.equalsIgnoreCase(fieldID)) {
            return Property.getPluralWithObjectCode(Constants.PRODUCTS_OR_SERVICES, wfmStrings.productsOrServices());
        } else if (ADDITIONAL_INFORMATION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.additionalInformation();
        } else if (CRM_OPPORTUNITY_LINKS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.links();
        } else if (CRM_ACTIVITIES.equalsIgnoreCase(fieldID)) {
            return Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, wfmStrings.latestOpenActivities(), wfmStrings.activities());
        } else if (CRM_OPPORTUNITY_STAGE_HISTORY.equalsIgnoreCase(fieldID)) {
            return wfmStrings.stageHistory();
        } else if (CRM_OPPORTUNITY_CONTACT_PRIMARY_EMAIL.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.Contacts, wfmStrings.email(), wfmStrings.contact());
        } else if (CRM_OPPORTUNITY_CONTACT_PRIMARY_PHONE.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.Contacts, wfmStrings.phone(), wfmStrings.contact());
        } else if (CRM_OPPORTUNITY_RFQ.equalsIgnoreCase(fieldID)) {
            return Property.getShortName(Constants.REQUEST_FOR_QUOTE, wfmStrings.relatedRFQ(), wfmStrings.requestForQuote());
        } else if (PROJECT_FIELD.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.PROJECT, wfmStrings.project());
        } else if (CREATED_DATE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.createdDate();
        } else if (ITEM_TABLE_ITEM.equalsIgnoreCase(fieldID)) {
            return wfmStrings.item() + " - " + wfmStrings.itemTable();
        } else if (ITEM_TABLE_DESCRIPTION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.description() + " - " + wfmStrings.itemTable();
        } else if (ITEM_TABLE_MEASUREMENT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.measurement() + " - " + wfmStrings.itemTable();
        } else if (ITEM_TABLE_PRICE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.price() + " - " + wfmStrings.itemTable();
        } else if (ITEM_TABLE_DISCOUNT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.discount() + " - " + wfmStrings.itemTable();
        } else if (ITEM_TABLE_SUPPLIER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.supplier() + " - " + wfmStrings.itemTable();
        } else if (ITEM_TABLE_CATEGORY.equalsIgnoreCase(fieldID)) {
            return wfmStrings.category() + " - " + wfmStrings.itemTable();
        } else if (ITEM_TABLE_BRAND.equalsIgnoreCase(fieldID)) {
            return wfmStrings.brand() + " - " + wfmStrings.itemTable();
        } else if (ITEM_TABLE_TOTAL_AMOUNT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.totalAmount() + " - " + wfmStrings.itemTable();
        } else if (ITEM_TABLE_QTY.equalsIgnoreCase(fieldID)) {
            return wfmStrings.qty() + " - " + wfmStrings.itemTable();
        } else if (ITEM_TABLE_NET_AMOUNT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.netAmount() + " - " + wfmStrings.itemTable();
        } else if (ITEM_TABLE_TAX_RATE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.taxRate() + " - " + wfmStrings.itemTable();
        } else if (ITEM_TABLE_PROJECT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.project() + " - " + wfmStrings.itemTable();
        } else if (TAX_CALC_TYPE.equals(fieldID)) return wfmStrings.taxCalcType();
        return values.length >= 2 ? values[1] + " - " + wfmStrings.itemTable() : null;
    }

    private String localizeCampaign(String fieldID) {
        if (CRM_NOTE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.notes();
        } else if ("CAMPAIGN_INFORMATION".equalsIgnoreCase(fieldID)) {
            return wfmStrings.information();
        } else if (CRM_CAMPAIGN_ASSIGNEE.equals(fieldID)) {
            return wfmStrings.crmCampaignAssignee();
        } else if (CRM_CAMPAIGN_NAME.equals(fieldID)) {
            return wfmStrings.crmCampaignName();
        } else if (CRM_CAMPAIGN_STATUS.equals(fieldID)) {
            return wfmStrings.crmCampaignStatus();
        } else if (CRM_CAMPAIGN_TYPE.equals(fieldID)) {
            return wfmStrings.crmCampaignType();
        } else if (CRM_CAMPAIGN_STARTDATE.equals(fieldID)) {
            return wfmStrings.crmCampignStartDate();
        } else if (CRM_CAMPAIGN_BUDGETCOST.equals(fieldID)) {
            return wfmStrings.crmCampaignBudgetCost();
        } else if (CRM_CAMPAIGN_NUMBERSENT.equals(fieldID)) {
            return wfmStrings.crmCampaignNumberSent();
        } else if (CRM_CAMPAIGN_EXPECTEDRESPONSE.equals(fieldID)) {
            return wfmStrings.crmCampaignExpectedResponse();
        } else if (CRM_CAMPAIGN_EXPECTEDREVENUE.equals(fieldID)) {
            return wfmStrings.crmCampaignExpectedRevenue();
        } else if (CRM_CAMPAIGN_ENDDATE.equals(fieldID)) {
            return wfmStrings.crmCampignEndDate();
        }
        return null;
    }

    private String localizeWebForm(String fieldID) {
        if (WEBFORM.CUSTOM_LAYOUT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.customLayout();
        }
        if (WEBFORM.CUSTOM_LAYOUT_CSS.equals(fieldID)) {
            return wfmStrings.customCss();
        }
        if (WEBFORM.FORM_TYPE.equals(fieldID)) {
            return wfmStrings.fromType();
        }
        if (WEBFORM.TITLE.equals(fieldID)) {
            return wfmStrings.title();
        }
        if (WEBFORM.DESCRIPTION.equals(fieldID)) {
            return wfmStrings.description();
        }
        if (WEBFORM.CONFIRMATION_MESSAGE.equals(fieldID)) {
            return wfmStrings.confirmationMessage();
        }
        if (WEBFORM.REDIRECT.equals(fieldID)) {
            return wfmStrings.pathTo_A_RedirectPage();
        }
        if (WEBFORM.CAPTCHA.equals(fieldID)) {
            return wfmStrings.captcha();
        }
        if (WEBFORM.BUTTON_TEXT.equals(fieldID)) {
            return wfmStrings.buttonText();
        }
        if (WEBFORM.TABLE_OF_FIELDS.equals(fieldID)) {
            return wfmStrings.tableOfFields();
        }
        if (WEBFORM.LAYOUT_PICKER.equals(fieldID)) {
            return wfmStrings.useCustomLayout();
        }
        if ("CONFIGURE_WEB_FORM".equals(fieldID)) {
            return wfmStrings.configureThWebForm();
        }
        if ("CONFIGURE_WEB_FIELD".equals(fieldID)) {
            return wfmStrings.configureThWebField();
        }

        if (WEBFORM.FORM_URL.equals(fieldID)) {
            return wfmStrings.formURL();
        }

        if (WEBFORM.CAPTCHA_LABEL.equals(fieldID)) {
            return wfmStrings.captchaLabel();
        }

        if (WEBFORM.CAPTCHA_DESCRIPTION.equals(fieldID)) {
            return wfmStrings.captchaDescription();
        }

        if (WEBFORM.NOTIFICATION_EMAIL_ADDRESS.equals(fieldID)) {
            return wfmStrings.notificationEmail();
        }
        if (WEBFORM.FORM_PREVIEW.equals(fieldID)) {
            return wfmStrings.formPreview();
        }
        if (WEBFORM.IFRAME_CODE.equals(fieldID)) {
            return wfmStrings.embedCode();
        }

        return null;
    }

    private String localizeMailList(String fieldID) {
        if (MAIL_LIST_INFORMATION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.mailListInformation();
        }
        if (NAME.equals(fieldID)) {
            return wfmStrings.name();
        }
        if (DESCRIPTION.equals(fieldID)) {
            return wfmStrings.description();
        }
        if (STATUS.equals(fieldID)) {
            return wfmStrings.status();
        }
        return null;
    }

    private String localizeEMMessage(String fieldID) {
        if (CRM_MESSAGE_DETAILS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.messageDetails();
        }
        if (CRM_MESSAGE_SUBSCRIPTION_LISTS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.subscriptionLists();
        }
        if (CRM_MESSAGE_SOURCE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.campaign();
        }
        if (CRM_MESSAGE_SUBJECT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.subject();
        }
        if (CRM_MESSAGE_PREHEADER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.preheader();
        }
        if (CRM_MESSAGE_FROM.equalsIgnoreCase(fieldID)) {
            return wfmStrings.fromEmail();
        }
        if (CRM_MESSAGE_FULLNAME.equalsIgnoreCase(fieldID)) {
            return wfmStrings.fromName();
        }
        if (CRM_MESSAGE_REPLYTO.equalsIgnoreCase(fieldID)) {
            return wfmStrings.replyToOnly();
        }
        if (CRM_MESSAGE_DATETABLE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.date();
        }
        if (CRM_MESSAGE_TIMETABLE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.time();
        }
        if (CRM_MESSAGE_CONTENT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.emailContent();
        }
        if (CRM_MESSAGE_FORMAT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.messageFormat();
        }
        if (CRM_MESSAGE_CATEGORY.equalsIgnoreCase(fieldID)) {
            return wfmStrings.chooseTemplate();
        }
        if (CRM_MESSAGE_FIELD.equalsIgnoreCase(fieldID)) {
            return wfmStrings.content();
        }
        if (CRM_MESSAGE_SENT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.testMailTo();
        }
        if (CRM_MESSAGE_ANTI_SPAN.equalsIgnoreCase(fieldID)) {
            return wfmStrings.privacyPolicy();
        }
        if (ATTACHMENTS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.attachments();
        }

        if (SENDER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.sender();
        }
        if (DESCRIPTION.equalsIgnoreCase(fieldID)) {
            return wfmStrings.message();
        } else if (CRM_MESSAGE_STATISTICS.equals(fieldID)) {
            return wfmStrings.crmMessageStatistics();
        } else if (CRM_MESSAGE_ENTITIES_COUNT.equals(fieldID)) {
            return wfmStrings.crmMessageEntitiesCount();
        } else if (CRM_MESSAGE_SENT_COUNT.equals(fieldID)) {
            return wfmStrings.crmMessageSentCount();
        } else if (CRM_MESSAGE_UNSUBSCRIBES_COUNT.equals(fieldID)) {
            return wfmStrings.crmMessageUnsubscribesCount();
        } else if (CRM_MESSAGE_DELIVERY_COUNT.equals(fieldID)) {
            return wfmStrings.crmMessageDeliveryCount();
        } else if (CRM_MESSAGE_DELIVERY_RATE.equals(fieldID)) {
            return wfmStrings.crmMessageDeliveryRate();
        } else if (CRM_MESSAGE_BOUNCED_COUNT.equals(fieldID)) {
            return wfmStrings.crmMessageBouncedCount();
        } else if (CRM_MESSAGE_BOUNCED_RATE.equals(fieldID)) {
            return wfmStrings.crmMessageBouncedRate();
        } else if (CRM_MESSAGE_VIEW_COUNT.equals(fieldID)) {
            return wfmStrings.crmMessageViewCount();
        } else if (CRM_MESSAGE_VIEW_RATE.equals(fieldID)) {
            return wfmStrings.crmMessageViewRate();
        } else if (CRM_MESSAGE_CLICK_COUNT.equals(fieldID)) {
            return wfmStrings.crmMessageClickCount();
        } else if (CRM_MESSAGE_CLICK_RATE.equals(fieldID)) {
            return wfmStrings.crmMessageClickRate();
        }
        return null;
    }

    private String localizeCase(String fieldID) {
        if (CASE_ID.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.CASE_LIST, wfmStrings.caseID(), wfmStrings.crmCase());
        }
        if (SLA_TIMER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.timeLeft();
        }
        if (STATUS_HISTORY.equalsIgnoreCase(fieldID)) {
            return wfmStrings.statusHistory();
        }
        if (CASE_HISTORY_LOG.equalsIgnoreCase(fieldID)) {
            return wfmStrings.historyLog();
        }
        if (CRM_ACTIVITIES.equalsIgnoreCase(fieldID)) {
            return Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, wfmStrings.latestOpenActivities(), wfmStrings.activities());
        }
        if (CRM_TASKS.equalsIgnoreCase(fieldID)) {
            return Property.getPluralWithObjectCode(Constants.TASK, wfmStrings.tasks());
        }
        if (CRM_CASE_TIMER.equalsIgnoreCase(fieldID)) {
            return wfmStrings.timeSpentOnly();
        }
        if (DESCRIPTION.equalsIgnoreCase(fieldID)) {
            return Property.get(Constants.CASE_LIST, wfmStrings.caseDescription(), wfmStrings.crmCase());
        }
        if (CASE_INFORMATION.equals(fieldID)) {
            return Property.get(Constants.CASE_LIST, wfmStrings.basicDetails(), wfmStrings.crmCase());
        }
        if (ATTACHMENTS.equals(fieldID)) {
            return wfmStrings.attachments();
        }
        if (SUBJECT.equals(fieldID)) {
            return wfmStrings.subject();
        }
        if (CRM_NOTE.equals(fieldID)) {
            return wfmStrings.notes();
        }
        if (CASE_DESCRIPTION.equals(fieldID)) {
            return wfmStrings.description();
        }
        if (CASE_ORIGIN.equals(fieldID)) {
            return Property.get(Constants.CASE_LIST, wfmStrings.caseOrigin(), wfmStrings.crmCase());
        }
        if (BILLABLE.equals(fieldID)) {
            return wfmStrings.billable();
        }
        if (STATUS.equals(fieldID)) {
            return wfmStrings.status();
        }
        if (TYPE.equals(fieldID)) {
            return wfmStrings.type();
        }
        if (SLA.equals(fieldID)) {
            return wfmStrings.sla();
        }
        if (REPORTED_BY.equals(fieldID)) {
            return wfmStrings.reportedBy();
        }
        if (PRIORITY.equals(fieldID)) {
            return wfmStrings.priority();
        }
        if (CASE_REASON.equals(fieldID)) {
            return Property.get(Constants.CASE_LIST, wfmStrings.caseReason(), wfmStrings.crmCase());
        }
        if (RESOLVER.equals(fieldID)) {
            return wfmStrings.resolver();
        }
        if (ASSIGNEE.equals(fieldID)) {
            return wfmStrings.assignedTo();
        }
        if (LINKS.equals(fieldID)) {
            return wfmStrings.links();
        }
        if (ADDITIONAL_INFORMATION.equals(fieldID)) {
            return wfmStrings.additionalInformation();
        }
        if (INTERNAL_STATUS.equals(fieldID)) {
            return wfmStrings.internalStatus();
        }
        if (INTERNAL_UPDATED_DATE.equals(fieldID)) {
            return wfmStrings.internalUpdatedDate();
        }
        if (INFORMATION.equals(fieldID)) {
            return wfmStrings.basicDetails();
        }
        if (WORKFLOW_TIME_BASED_HEADER.equals(fieldID)) {
            return wfmStrings.recurrenceType();
        }
        return null;
    }

    public String localizeContact(String fieldID) {
        if (CONTACT_INFORMATION.equals(fieldID)) {
            return Property.get(Constants.Contacts, wfmStrings.contactInformation(), wfmStrings.contact());
        } else if (LEAD_INFORMATION.equals(fieldID)) {
            return Property.get(Constants.LEADS, wfmStrings.basicDetails(), wfmStrings.lead());
        } else if (ADDRESS_INFORMATION.equals(fieldID)) {
            return wfmStrings.addressInformation();
        } else if (ADDITIONAL_INFORMATION.equals(fieldID)) {
            return wfmStrings.additionalInformation();
        } else if (CRM_DETAILS.equals(fieldID)) {
            return wfmStrings.crmDetails();
        } else if (TITLE.equals(fieldID)) {
            return wfmStrings.title();
        } else if (MIDDLE_NAME.equals(fieldID)) {
            return wfmStrings.middleName();
        } else if (FIRST_NAME.equals(fieldID)) {
            return wfmStrings.firstName();
        } else if (OTHER_NAME.equals(fieldID)) {
            return wfmStrings.otherName();
        } else if (LAST_NAME.equals(fieldID)) {
            return wfmStrings.lastName();
        } else if (BIRTH_DAY.equals(fieldID)) {
            return wfmStrings.dateOfBirth();
        } else if (CRM_ACCOUNT_NAME.equals(fieldID)) {
            return wfmStrings.companyName();
        } else if (CRM_ACCOUNT_TYPE.equals(fieldID)) {
            return wfmStrings.accountType();
        } else if (JOB_TITLE.equals(fieldID)) {
            return wfmStrings.jobTitle();
        } else if (CRM_ACCOUNT_ORGANIZATION_TYPE.equals(fieldID)) {
            return wfmStrings.organizationType();
        } else if (DEPARTMENT.equals(fieldID)) {
            return Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department());
        } else if (CRM_ACCOUNT_ANNUAL_REVENUE.equals(fieldID)) {
            return wfmStrings.annualRevenue();
        } else if (REF_IND_NUMBER.equals(fieldID)) {
            return "Ref Ind Number";
        } else if (ASSETS.equals(fieldID)) {
            return wfmStrings.assetsUnderManagement();
        } else if (CRM_ACCOUNT_INDUSTRY.equals(fieldID)) {
            return wfmStrings.industry();
        } else if (CRM_ACCOUNT_OWNERSHIP.equals(fieldID)) {
            return wfmStrings.ownership();
        } else if (CRM_ACCOUNT_NUMBER_OF_EMPLOYEE.equals(fieldID)) {
            return wfmStrings.numberOfEmployees();
        } else if (EMAIL.equals(fieldID)) {
            return wfmStrings.email();
        } else if (PHONE.equals(fieldID)) {
            return wfmStrings.phone();
        } else if (IM_ADDRESS.equals(fieldID)) {
            return wfmStrings.imAddress();
        } else if (WEB_ADDRESS.equals(fieldID)) {
            return wfmStrings.webAddress();
        } else if (LEAD_OWNER.equals(fieldID)) {
            return Property.get(Constants.LEADS, wfmStrings.leadOwner(), wfmStrings.lead());
        } else if (LEAD_NAME.equals(fieldID)) {
            return Property.get(Constants.LEADS, wfmStrings.name(), wfmStrings.lead());
        } else if (ADDRESS.equals(fieldID)) {
            return Property.get(Constants.Contacts, wfmStrings.primaryContactAddress(), wfmStrings.contact());
        } else if (COPY_CRMACCOUNT_ADDRESS.equals(fieldID)) {
            return wfmStrings.accoundAddress();
        } else if (PARENT_ADDRESSES.equals(fieldID)) {
            return wfmStrings.parentAccountAddress();
        } else if (CATEGORY.equals(fieldID)) {
            return wfmStrings.category();
        } else if (RELATIONSHIP.equals(fieldID)) {
            return wfmStrings.relationship();
        } else if (REPORTS_TO.equals(fieldID)) {
            return wfmStrings.supervisor();
        } else if (OWNER.equals(fieldID)) {
            return wfmStrings.owner();
        } else if (CRM_CAMPAIGN_NAME.equals(fieldID)) {
            return wfmStrings.campaign();
        } else if (EMAIL_OPT_OUT.equals(fieldID)) {
            return wfmStrings.emailOptOut();
        } else if (SUBSCRIPTION_LIST.equals(fieldID)) {
            return wfmStrings.subscriptionLists();
        } else if (CRM_ACTIVITIES.equals(fieldID)) {
            return Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, wfmStrings.latestOpenActivities(), wfmStrings.activities());
        } else if (CRM_NOTE.equals(fieldID)) {
            return wfmStrings.notes();
        } else if (ATTACHMENTS.equals(fieldID)) {
            return wfmStrings.attachments();
        } else if (ATTACHMENTS_MINI.equals(fieldID)) {
            return wfmStrings.attachments();
        } else if (LINKS.equals(fieldID)) {
            return wfmStrings.links();
        } else if (LEAD_INFORMATION.equals(fieldID)) {//leads fields start
            return Property.get(Constants.LEADS, wfmStrings.basicDetails(), wfmStrings.lead());
        } else if (LEAD_SOURCE.equals(fieldID)) {
            return wfmStrings.source();
        } else if (STATUS.equals(fieldID)) {
            return wfmStrings.status();
        } else if (ASSIGNEE.equals(fieldID)) {
            return wfmStrings.assignee();
        } else if (BACKUP_ASSIGNEE.equals(fieldID)) {
            return wfmStrings.backupAssignee();
        } else if (RATING.equals(fieldID)) {
            return wfmStrings.rating();
        } else if (NUMBER.equals(fieldID)) {//candidate start
            return wfmStrings.number();
        } else if (CREATED_DATE.equals(fieldID)) {
            return wfmStrings.createdDate();
        } else if (WORK_EXPERIENCE.equals(fieldID)) {
            return wfmStrings.workExperience();
        } else if (CURRENT_EMPLOYER.equals(fieldID)) {
            return wfmStrings.currentEmployer();
        } else if (EXPECTED_SALARY.equals(fieldID)) {
            return wfmStrings.expectedSalary();
        } else if (SKILLS.equals(fieldID)) {
            return wfmStrings.skills();
        } else if (STATUS_HISTORY.equals(fieldID)) {
            return wfmStrings.statusHistory();
        } else if (CustomFormConstants.CANDIDATE.LOCATION.equals(fieldID)) {
            return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
        } else if (VACANCIES.equals(fieldID)) {
            return wfmStrings.matchedVacancies();
        } else if (CustomFormConstants.CANDIDATE.OTHER_INFORMATION.equals(fieldID)) {
            return wfmStrings.otherInformation();
        } else if (IMAGE_UPLOAD.equals(fieldID)) {
            return wfmStrings.uploadImage();
        } else if (CustomFormConstants.CANDIDATE.ALLOWANCES.equals(fieldID)) {
            return wfmStrings.allowances();
        } else if (STREET_ADDRESS1.equals(fieldID)) {
            return wfmStrings.streetAddress1();
        } else if (STREET_ADDRESS2.equals(fieldID)) {
            return wfmStrings.streetAddress2();
        } else if (CITY.equals(fieldID)) {
            return wfmStrings.city();
        } else if (STATE.equals(fieldID)) {
            return wfmStrings.state();
        } else if (COUNTRY.equals(fieldID)) {
            return wfmStrings.country();
        } else if (POST_CODE.equals(fieldID)) {
            return wfmStrings.postCode();
        } else if (CANDIDATE.CANDIDATE_PROJECT.equals(fieldID)) {
            return Property.get(Constants.PROJECT, wfmStrings.project());
        } else if (CANDIDATE.ALLOWANCE_INFORMATION.equals(fieldID)) {
            return wfmStrings.allowanceInformation();
        } else if (PROFILE_PICTURE.equals(fieldID)) {
            return wfmStrings.profilePicture();
        } else if (WORKFLOW_FIELDS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.workstreamfields();
        } else if (UPDATED_DATE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.modifiedDate();
        } else if (CRM_CAMPAIGN_ASSIGNEE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.assignee();
        } else if (CRM_CAMPAIGN_TYPE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.type();
        } else if (CRM_CAMPAIGN_STATUS.equalsIgnoreCase(fieldID)) {
            return wfmStrings.status();
        } else if (CRM_CAMPAIGN_STARTDATE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.startDate();
        } else if (CRM_CAMPAIGN_ENDDATE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.endDate();
        } else if (CRM_CAMPAIGN_EXPECTEDREVENUE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.expectedRevenue();
        } else if (CRM_CAMPAIGN_BUDGETCOST.equalsIgnoreCase(fieldID)) {
            return wfmStrings.budgetCost();
        } else if (CRM_CAMPAIGN_ACTUALCOST.equalsIgnoreCase(fieldID)) {
            return wfmStrings.actualCost();
        } else if (CRM_CAMPAIGN_EXPECTEDRESPONSE.equalsIgnoreCase(fieldID)) {
            return wfmStrings.expectedResponse();
        } else if (CRM_CAMPAIGN_NUMBERSENT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.numberSent();
        } else if (SUBJECT.equalsIgnoreCase(fieldID)) {
            return wfmStrings.subject();
        } else if (TELEGRAM.equals(fieldID)) return wfmStrings.telegram();
        else if (LANGUAGE.equals(fieldID)) return wfmStrings.language();
        else if (CREATED_BY.equals(fieldID)) return wfmStrings.createdBy();
        else if (UPDATED_BY.equals(fieldID)) return wfmStrings.modifiedBy();
        else if (UPDATES.equals(fieldID)) return wfmStrings.updates();
        else if (CRM_LEAD_ITEMS.equals(fieldID)) return wfmStrings.crmLeadItems();
        else if ("ACCESS_ENABLED".equals(fieldID)) return wfmStrings.enableAccess();
        else if (CANDIDATE.CANDIDATE_STATUS_HISTORY.equals(fieldID)) return wfmStrings.statusHistory();
        return null;
    }

    public String localizeProductCategoryForm(String fieldID) {
        if (PRODUCT_CATEGORY_TITLE.equals(fieldID)) {
            return wfmStrings.basicDetails();
        } else if (NAME.equals(fieldID)) {
            return wfmStrings.name();
        } else if (CODE.equals(fieldID)) {
            return wfmStrings.code();
        } else if (CATEGORY.equals(fieldID)) {
            return wfmStrings.category();
        } else if (ORDER.equals(fieldID)) {
            return wfmStrings.order();
        } else if (IMAGE_UPLOAD.equals(fieldID)) {
            return wfmStrings.imageUpload();
        }
        return null;
    }

}
