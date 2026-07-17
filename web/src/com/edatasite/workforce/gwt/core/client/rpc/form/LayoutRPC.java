package com.edatasite.workforce.gwt.core.client.rpc.form;

import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ValidationType;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * User: Hayot
 * Date: 10/20/11
 * Time: 10:51 AM
 */
public class LayoutRPC implements IsSerializable, LayoutInterface {
    public static final String OPPORTUNITY_FORM = "OPPORTUNITY_FORM";
    public static final String SALEINVOICE_FORM = "SALE_INVOICE_FORM";
    public static final String CREDITNOTE_FORM = "CREDIT_NOTE_FORM";
    public static final String DEBIT_NOTE_FORM = "DEBIT_NOTE_FORM";
    public static final String SALEORDER_FORM = "SALEORDER_FORM";
    public static final String SALEQUOTE_FORM = "SALEQUOTE_FORM";

    public static final String VIEW = "viewForm";
    public static final String EDIT = "editForm";
    public static final String ADD = "addForm";
    public static final String IMPORT = "importForm";
    public static final String LEAD_FORM = "LEAD_FORM";
    public static final String CONTACT_FORM = "CONTACT_FORM";
    public static final String CANDIDATE_FORM = "CANDIDATE_FORM";
    public static final String CASE_FORM = "CASE_FORM";
    public static final String ACTIVITY_FORM = "ACTIVITY_FORM";
    public static final String LOGACALL_FORM = "LOGACALL_FORM";
    public static final String LOGACALL_FORM_VIEW = "LOGACALL_FORM_VIEW";
    public static final String ACTIVITY_VIEW_FORM = "ACTIVITY_VIEW_FORM";
    public static final String CAMPAIGN_FORM = "CAMPAIGN_FORM";
    public static final String ACCOUNT_FORM = "ACCOUNT_FORM";
    public static final String CLIENT_FORM = "CLIENT_FORM";
    public static final String SUPPLIER_FORM = "SUPPLIER_FORM";
    public static final String WEB_FORM = "WEB_FORM";
    public static final String SALE_INVOICE_FORM = "saleinvoice";
    public static final String PURCHASE_INVOICE_FORM = "purchaseinvoice";
    public static final String PURCHASEINVOICE_FORM = "PURCHASE_INVOICE_FORM";
    public static final String SALE_QUOTE_FORM = "salequote";
    public static final String CUSTOMER_REFUND = "CUSTOMER_REFUND";
    public static final String SUPPLIER_REFUND = "SUPPLIER_REFUND";
    public static final String CONVERSION_BALANCE = "CONVERSION_BALANCE";
    public static final String PURCHASE_ORDER = "purchaseorder";
    public static final String PURCHASEORDER_FORM = "PURCHASEORDER_FORM";
    public static final String PRODUCT = "PRODUCT";
    public static final String RENTAL_PRODUCT_FORM = "RENTAL_PRODUCT_FORM";
    public static final String RENTAL_ORDER_FORM = "RENTAL_ORDER_FORM";
    public static final String MULTI_CASH_ADVANCE_FORM = "MULTI_CASH_ADVANCE_FORM";
    public static final String PRODUCT_FORM = "PRODUCT_FORM";
    public static final String EXPENSE_FORM = "EXPENSE_REPORT";
    public static final String CREDIT_NOTE_FORM = "creditnote";
    public static final String MANUAL_JOURNAL_FORM = "manualtransactions";
    public static final String GDN_FORM = "GDN_FORM";
    public static final String PICKLIST_FORM = "PICKLIST_FORM";
    public static final String BANK_TRANSACTION_FORM = "BANK_TRANSACTION_FORM";
    public static final String IMPORT_BANK_TRANSACTION_FORM = "IMPORT_BANK_TRANSACTION_FORM";
    public static final String IMPORT_BUDGET_MANAGER_FORM = "IMPORT_BUDGET_MANAGER_FORM";
    public static final String BANK_ACCOUNT_FORM = "BANK_ACCOUNT_FORM";
    public static final String FIXED_ASSET_FORM = "FIXED_ASSET_FORM";
    public static final String STOCK_TRANSFER_FORM = "STOCK_TRANSFER_FORM";
    public static final String BUILD_ASSEMBLY_FORM = "BUILD_ASSEMBLY_FORM";
    public static final String SOLUTION_FORM = "SOLUTION_FORM";
    public static final String REFERENCE_FORM = "REFERENCE_FORM";
    public static final String LEAVE_REASON_FORM = "LEAVE_REASON_FORM";
    public static final String COUNTRY_SETTINGS_FORM = "COUNTRY_SETTINGS_FORM";
    public static final String MESSAGE_CENTER_FORM = "MESSAGE_CENTER_FORM";
    public static final String EMAIL_COMPOSE_FORM = "EMAIL_COMPOSE_FORM";
    public static final String EMAIL_SUMMARY_COMPOSE_FORM = "EMAIL_SUMMARY_COMPOSE_FORM";
    public static final String ACCOUNTING_EMAIL_COMPOSE_FORM = "ACCOUNTING_EMAIL_COMPOSE_FORM";
    public static final String EXPENSE_EMAIL_COMPOSE_FORM = "EXPENSE_EMAIL_COMPOSE_FORM";
    public static final String TASK_MAX_FORM = "TASK_MAX_FORM";
    public static final String TASK_MIN_FORM = "TASK_MIN_FORM";
    public static final String WORKFLOW_TASK_MIN_FORM = "WORKFLOW_TASK_MIN_FORM";
    public static final String PERSONAL_GOAL_FORM = "PERSONAL_GOAL_FORM";
    public static final String GROUP_GOAL_FORM = "GROUP_GOAL_FORM";
    public static final String DEPARTMENT_GOAL_FORM = "DEPARTMENT_GOAL_FORM";
    public static final String PROJECT_GOAL_FORM = "PROJECT_GOAL_FORM";
    public static final String BUSINESS_GOAL_FORM = "BUSINESS_GOAL_FORM";
    public static final String COMPANY_GOAL_FORM = "COMPANY_GOAL_FORM";
    public static final String MAIL_LIST_FORM = "MAIL_LIST_FORM";
    public static final String MESSAGE_FORM = "MESSAGE_FORM";
    public static final String MEETING_MINUTES = "MEETING_MINUTES";
    public static final String VACANCY_FORM = "VACANCY_FORM";
    public static final String OVERTIME_FORM = "OVERTIME_FORM";
    public static final String SHIFT_FORM = "SHIFT_FORM";
    public static final String BRIGADA_FORM = "BRIGADA_FORM";
    public static final String ROTATION_FORM = "ROTATION_FORM";
    public static final String GROUP_PLACEMENT_FORM = "GROUP_PLACEMENT_FORM";
    public static final String PLACEMENT_FORM = "PLACEMENT_FORM";
    public static final String BONUS_SETTING_FORM = "BONUS_SETTING_FORM";
    public static final String BONUS_DISTRIBUTION_REPORT_FORM = "BONUS_DISTRIBUTION_REPORT_FORM";
    public static final String ELIGIBLE_SETTING_FORM = "ELIGIBLE_SETTING_FORM";
    public static final String APPRAISAL_APPROVAL_FORM = "APPRAISAL_APPROVAL_FORM";
    public static final String STUDENT_FORM = "STUDENT_FORM";
    public static final String ENQUIRY_FORM = "ENQUIRY_FORM";
    public static final String COURSE_FORM = "COURSE_FORM";
    public static final String ATTENDENCE_SHEET_FORM = "ATTENDENCE_SHEET_FORM";
    public static final String INVOICE_GENERATOR_FORM = "INVOICE_GENERATOR_FORM";
    public static final String SCHEDULE_INVOICE_FORM = "SCHEDULE_INVOICE_FORM";
    public static final String SCHEDULED_COURSE_FORM = "SCHEDULED_COURSE_FORM";
    public static final String SCHEDULED_COURSE_VIEW_FORM = "SCHEDULED_COURSE_VIEW_FORM";
    public static final String ADD_COURSE_SUBJECT_VIEW = "ADD_COURSE_SUBJECT_VIEW";
    public static final String BOOKING_ITEMS_ADD_VIEW = "BOOKING_ITEMS_ADD_VIEW";
    public static final String BOOKING_ITEM_RESERVATION_VIEW = "BOOKING_ITEM_RESERVATION_VIEW";
    public static final String CLONE_SCHEDULED_COURSE_FORM = "CLONE_SCHEDULED_COURSE_FORM";
    public static final String ADD_COURSESTUDENT_FORM = "ADD_COURSESTUDENT_FORM";
    public static final String INSTRUCTOR_REASSIGN_FORM = "INSTRUCTOR_REASSIGN_FORM";
    public static final String INSTRUCTOR_FORM = "INSTRUCTOR_FORM";
    public static final String COURSE_BOOKING_FORM = "COURSE_BOOKING_FORM";
    public static final String EMPLOYEE_PROFILE_FORM = "EMPLOYEE_PROFILE_FORM";
    public static final String COMPANY_SETTINGS_FORM = "COMPANY_SETTINGS_FORM";
    public static final String CS_STUDENT_FORM = "CS_STUDENT_FORM";
    public static final String CONSIGNMENT_FORM = "CONSIGNMENT_FORM";
    public static final String TRANSFER_MONEY_VIEW = "TRANSFER_MONEY_VIEW";
    public static final String RECEIVE_PAYMENT_FORM = "RECEIVE_PAYMENT_FORM";
    public static final String PAY_INVOICE_FORM = "PAY_INVOICE_FORM";

    public static final String HRMS_EMPLOYEE_FORM = "HRMS_EMPLOYEE_FORM";
    public static final String PM_EMPLOYEE_FORM = "PM_EMPLOYEE_FORM";
    public static final String SETTINGS_EMPLOYEE_FORM = "SETTINGS_EMPLOYEE_FORM";
    public static final String PAYROLL_EMPLOYEE_FORM = "PAYROLL_EMPLOYEE_FORM";

    public static final String PROFILE_SETTINGS_FORM = "PROFILE_SETTINGS_FORM";
    public static final String STUDENT_ATTENDED_COURSE_BOOKING = "STUDENT_ATTENDED_COURSE_BOOKING";
    public static final String TRAINING_CONTACT_FORM = "TRAINING_CONTACT_FORM";
    public static final String CERTIFICATE_FORM = "CERTIFICATE_FORM";
    public static final String CRM_SETTINGS_FORM = "CRM_SETTINGS_FORM";
    public static final String SMS_SETTINGS_FORM = "SMS_SETTINGS_FORM";
    public static final String TWILIO_SETTINGS_FORM = "TWILIO_SETTINGS_FORM";
    public static final String ASTERISK_SETTINGS_FORM = "ASTERISK_SETTINGS_FORM";
    public static final String ASTERISK_EMPLOYEE_FORM = "ASTERISK_EMPLOYEE_FORM";
    public static final String ASSESSMENT_FORM = "ASSESSMENT_FORM";
    public static final String PRICE_CHANGE_FORM = "PRICE_CHANGE_FORM";
    public static final String HSE_PASSPORT_FORM = "HSE_PASSPORT_FORM";
    public static final String REFERENCE_LOCALE_FORM = "REFERENCE_LOCALE_FORM";
    public static final String BILLABLE_EXPENSE_FORM = "BILLABLE_EXPENSE_FORM";

    public static final String TELEPHONY_SETTINGS_FORM = "TELEPHONY_SETTINGS_FORM";
    public static final String MESSENGER_SETTINGS_FORM = "MESSENGER_SETTINGS_FORM";

    public static final String PROJECT_FORM = "PROJECT_FORM";
    public static final String CONTRACT_FORM = "CONTRACT_FORM";
    public static final String PROJECT_SUMMARY_FORM = "PROJECT_SUMMARY_FORM";
    public static final String CONTACT_CATEGORY_FORM = "CONTACT_CATEGORY_FORM";


    public static final String POSITION_FORM = "POSITION_FORM";
    public static final String MULTI_POSITION_FORM = "MULTI_POSITION_FORM";
    public static final String IMPORT_POSITION_FORM = "IMPORT_POSITION_FORM";
    public static final String IMPORT_DEPARTMENT_FORM = "IMPORT_DEPARTMENT_FORM";
    public static final String EMPLOYEE_LEAVE_TYPES_FORM = "EMPLOYEE_LEAVE_TYPES_FORM";
    public static final String EMPLOYEE_BENEFIT_FORM = "EMPLOYEE_BENEFIT_FORM";
    public static final String POSITION_VIEW = "POSITION_VIEW";
    public static final String DEPARTMENT_FORM = "DEPARTMENT_FORM";
    public static final String MULTI_DEPARTMENT_FORM = "MULTI_DEPARTMENT_FORM";
    public static final String CHART_OF_ACCOUNT_FORM = "CHART_OF_ACCOUNT_FORM";
    public static final String LOCATION_FORM = "LOCATION_FORM";
    public static final String IMPORT_PRODUCT_FORM = "IMPORT_PRODUCT_FORM";
    public static final String SELECT_CANDIDATE_FORM = "SELECT_CANDIDATE_FORM";
    public static final String IMPORT_CRM_ACCOUNT_FORM = "IMPORT_CRM_ACCOUNT_FORM";
    public static final String IMPORT_CLIENT_FORM = "IMPORT_CLIENT_FORM";
    public static final String IMPORT_PURCHASE_ORDERS_FORM = "IMPORT_PURCHASE_ORDERS_FORM";
    public static final String IMPORT_SUPPLIER_FORM = "IMPORT_SUPPLIER_FORM";
    public static final String REQUEST_FOR_QUOTE_FORM = "REQUEST_FOR_QUOTE_FORM";
    public static final String REQUEST_FOR_PURCHASE_FORM = "REQUEST_FOR_PURCHASE_FORM";
    public static final String TELEGRAM_BOT_SETTINGS_FORM = "TELEGRAM_BOT_SETTINGS_FORM";
    public static final String WORKSTREAM_FORM = "WORKSTREAM_FORM";
    public static final String IMPORT_CRM_LEAD_FORM = "IMPORT_CRM_LEAD_FORM";
    public static final String IMPORT_CRM_CONTACT_FORM = "IMPORT_CRM_CONTACT_FORM";
    public static final String IMPORT_HRMS_CANDIDATE_FORM = "IMPORT_HRMS_CANDIDATE_FORM";
    public static final String DONOTAPPLY = "DONOTAPPLY";
    public static final String BOOKINGITEMSRESERVATIONEDIT = "BOOKINGITEMSRESERVATIONEDIT";
    public static final String BOOKINGITEMSRESERVATION = "BOOKINGITEMSRESERVATION";
    public static final String BOOKINGITEMS = "BOOKINGITEMS";
    public static final String ONBOARDING_STEP_FORM = "ONBOARDING_STEP_FORM";
    public static final String ONBOARDING_PERIOD_FORM = "ONBOARDING_PERIOD_FORM";
    public static final String LEAVE_REQUEST_COMMENT_FORM = "LEAVE_REQUEST_COMMENT_FORM";
    public static final String LEAVE_REQUEST_FORM = "LEAVE_REQUEST_FORM";
    public static final String BENEFIT_REQUEST_FORM = "BENEFIT_REQUEST_FORM";
    public static final String CRM_WEB_FORM = "CRM_WEB_FORM";
    public static final String CRM_WEB_FORM_FOR_VIEW = "CRM_WEB_FORM_FOR_VIEW";
    public static final String EMPLOYEE_CLIENT_DOCUMENTS_FORM = "EMPLOYEE_CLIENT_DOCUMENTS_FORM";
    public static final String EMPLOYEE_DOCUMENTS_FORM = "EMPLOYEE_DOCUMENTS_FORM";
    public static final String SMS_MESSAGE_FORM = "SMS_MESSAGE_FORM";
    public static final String CONSOLIDATION_COMPANY_FORM = "CONSOLIDATION_COMPANY_FORM";
    public static final String PERFORMANCE_NOTE_FORM = "PERFORMANCE_NOTE_FORM";
    public static final String INCIDENT_FORM = "INCIDENT_FORM";
    public static final String ISSUE_FORM = "ISSUE_FORM";
    public static final String EXCHANGE_RATE_FORM = "EXCHANGE_RATE_FORM";
    public static final String SALARY_GRADE_FORM = "SALARY_GRADE_FORM";
    public static final String DEPENDENT_FORM = "DEPENDENT_FORM";
    public static final String MULTILEAD_OR_CONTACT_FORM = "MULTILEAD_OR_CONTACT_FORM";
    public static final String TELEGRAM_CHAT_FORM = "TELEGRAM_CHAT_FORM";
    public static final String PHANTOM_PDF_FORM = "PHANTOM_PDF_FORM";
    public static final String ITEXT_PDF_FORM = "ITEXT_PDF_FORM";
    public static final String AIPHANTOM_PDF_FORM = "AIPHANTOM_PDF_FORM";
    //payroll section
    public static final String PAYROLL_STARTER_FORM = "PAYROLL_STARTER_FORM";
    public static final String PAYROLL_PAYSLIP_TABLE_FORM = "PAYROLL_PAYSLIP_TABLE_FORM";

    public static final String PAYROLL_PAYRUN_PAYMENT_FORM = "PAYROLL_PAYRUN_PAYMENT_FORM";
    public static final String PAYROLL_PAYMENT_FORM = "PAYROLL_PAYMENT_FORM";
    public static final String PAYROLL_EOS_CALCULATION_FORM = "PAYROLL_EOS_CALCULATION_FORM";
    public static final String PAYROLL_TAXI_PAYRUN_FORM = "PAYROLL_TAXI_PAYRUN_FORM";
    public static final String PAYROLL_CASH_ADVANCE_FORM = "PAYROLL_CASH_ADVANCE_FORM";
    public static final String PAYROLL_ALLOWANCE_DEDUCTION_SETTINGS_FORM = "PAYROLL_ALLOWANCE_DEDUCTION_SETTINGS_FORM";
    public static final String PAYROLL_COMPANY_SETTINGS_FORM = "PAYROLL_COMPANY_SETTINGS_FORM";
    public static final String PAYROLL_PENSION_PROVIDER_FORM = "PAYROLL_PENSION_PROVIDER_FORM";
    public static final String PAYROLL_PENSION_SCHEMA_FORM = "PAYROLL_PENSION_SCHEMA_FORM";
    public static final String PAYROLL_CATEGORY_FORM = "PAYROLL_CATEGORY_FORM";
    public static final String PAYROLL_NEW_PAYMENT_CATEGORY_FORM = "NEW_PAYMENT_CATEGORY";
    public static final String PAYROLL_NEW_DEDUCTION_CATEGORY_FORM = "NEW_DEDUCTION_CATEGORY";
    public static final String PAYROLL_END_OF_SERVICE_SETTINGS_FORM = "PAYROLL_END_OF_SERVICE_SETTINGS_FORM";
    public static final String PAYROLL_BATCH_FORM = "PAYROLL_BATCH_FORM";
    public static final String TRANSACTION_ITEM_VIEW_FORM = "TRANSACTION_ITEM_VIEW_FORM";
    public static final String TAX_RATE_FORM = "TAX_RATE_FORM";
    public static final String BRAND_FORM = "BRAND_FORM";
    public static final String SHIPPING_METHOD_FORM = "SHIPPING_METHOD_FORM";
    public static final String PRICE_LEVEL_FORM = "PRICE_LEVEL_FORM";
    public static final String TERMS_FORM = "TERMS_FORM";
    public static final String DISCOUNT_FORM = "DISCOUNT_FORM";
    public static final String INTRODUCTION_PAGE_FORM = "INTRODUCTION_PAGE_FORM";
    public static final String MEASURMENT_FORM = "MEASURMENT_FORM";
    public static final String PRODUCT_CATEGORY_FORM = "PRODUCT_CATEGORY_FORM";
    public static final String CURRENCY_FORM = "CURRENCY_FORM";
    public static final String PICK_LIST_FORM = "PICK_LIST_FORM";
    public static final String CASH_ADVANCE_FORM = "CASH_ADVANCE_FORM";
    public static final String EXPENSE_CLAIM_FORM = "EXPENSE_CLAIM_FORM";
    public static final String GENERAL_SETTINGS_FORM = "GENERAL_SETTINGS_FORM";
    public static final String CUSTOMIZE_FORM = "CUSTOMIZE_FORM";
    public static final String IMPORT_BRAND_FORM = "IMPORT_BRAND_FORM";
    public static final String CUSTOM_FORM_LOCALIZATION = "CUSTOM_FORM_LOCALIZATION";
    public static final String PAYRUN_FORM = "PAYRUN_FORM";
    public static final String ADDITIONAL_PAYMENT_FORM = "ADDITIONAL_PAYMENT_FORM";


    //warehouse
    public static final String WAREHOUSE_FORM = "WAREHOUSE_FORM";
    public static final String STOCKADJUSTMENTS_FORM = "STOCKADJUSTMENTS_FORM";

    //settings section
    public static final String EMAIL_TEMPLATE_FORM = "EMAIL_TEMPLATE_FORM";
    public static final String SMS_TEMPLATE_FORM = "SMS_TEMPLATE_FORM";
    public static final String SIGNATURE_FORM = "SIGNATURE_FORM";
    public static final String HOLIDAY_FORM = "HOLIDAY_FORM";
    public static final String TIMESLOT_FORM = "TIMESLOT_FORM";
    public static final String TIMESHEET_SETTINGS_FORM = "TIMESHEET_SETTINGS_FORM";
    public static final String TIMESHEET_REMINDER_FORM = "TIMESHEET_REMINDER_FORM";
    //
    public static final String JOB_DESCRIPTION_FORM = "JOB_DESCRIPTION_FORM";
    public static final String BUDGET_AND_INCUMBENTS_FORM = "BUDGET_AND_INCUMBENTS_FORM";
    //
    public static final String MULTIPLE_APPROVAL_REQUEST_FORM = "MULTIPLE_APPROVAL_REQUEST_FORM";
    public static final String COMPETENCY_FORM = "COMPETENCY_FORM";
    public static final String APPRAISAL_TEMPLATE_FORM = "APPRAISAL_TEMPLATE_FORM";
    public static final String PAST_EMPLOYMENT_FORM = "PAST_EMPLOYMENT_FORM";
    public static final String INTERNAL_EMPLOYMENT_FORM = "INTERNAL_EMPLOYMENT_FORM";
    public static final String EDUCATION_FORM = "EDUCATION_FORM";
    public static final String AWARD_FORM = "AWARD_FORM";
    public static final String GOAL_WEIGHT_EDIT_FORM = "GOAL_WEIGHT_EDIT_FORM";

    //exlib
    public static final String PENALTIES_PROMOTIONS_FORM = "PENALTIES_PROMOTIONS_FORM";
    public static final String EMPLOYEE_PENALTIES_PROMOTIONS_FORM = "EMPLOYEE_PENALTIES_PROMOTIONS_FORM";
    public static final String BONUS_RECOMMENDATIONS_FORM = "BONUS_RECOMMENDATIONS_FORM";

    public static final String TENDER_EXCHANGE_CARD_FORM = "TENDER_EXCHANGE_CARD_FORM";
    public static final String ESTIMATE_OF_CONTRACT_FORM = "ESTIMATE_OF_CONTRACT_FORM";

    public static final String WORKFLOW_FORM = "WORKFLOW_FORM";
    public static final String WORKFLOW_ALERT_FORM = "WORKFLOW_ALERT_FORM";
    public static final String WORKFLOW_TELEGRAM_ALERT_FORM = "WORKFLOW_TELEGRAM_ALERT_FORM";
    public static final String WORKFLOW_WEB_HOOK_FORM = "WORKFLOW_WEB_HOOK_FORM";
    public static final String TELEGRAM_RECURRENCE_FORM = "TELEGRAM_RECURRENCE_FORM";
    public static final String WORKFLOW_UPDATE_FIELD_FORM = "WORKFLOW_UPDATE_FIELD_FORM";
    public static final String WORKFLOW_EMPLOYEE_FORM = "WORKFLOW_EMPLOYEE_FORM";
    public static final String WORKFLOW_INVOICE_FORM = "WORKFLOW_INVOICE_FORM";
    public static final String WORKFLOW_ACTION_FORM = "WORKFLOW_ACTION_FORM";

    //SECTIONS
    public static final String PM_SECTION = "PM_SECTION";
    public static final String CRM_SECTION = "CRM_SECTION";
    public static final String ACCOUNTING_SECTION = "ACCOUNTING_SECTION";
    public static final String HRMS_SECTION = "HRMS_SECTION";
    public static final String DOCUMENT_SECTION = "DOCUMENT_SECTION";
    public static final String SETTINGS_SECTION = "SETTINGS_SECTION";
    public static final String Backend_SECTION = "BACKEND_SECTION";
    public static final String LOGISTICS_SECTION = "LOGISTICS_SECTION";


    public static final String HRMS_COMPANY_NEWS_FORM = "HRMS_COMPANY_NEWS_FORM";
    public static final String HRMS_COMPANY_NEWS_SUMMARY = "HRMS_COMPANY_NEWS_SUMMARY";
    public static final String WORKFLOW_EVENT_FORM = "WORKFLOW_EVENT_FORM";
    public static final String WORKFLOW_CALL_LOG_FORM = "WORKFLOW_CALL_LOG_FORM";
    public static final String TASK_SUMMARY_FORM = "TASK_SUMMARY_FORM";

    //Website
    public static final String WEBSITE_FORM = "WEBSITE_FORM";
    public static final String WEBSITE_LAYOUT_FORM = "WEBSITE_LAYOUT_FORM";
    public static final String WEBSITE_PAGE_FORM = "WEBSITE_PAGE_FORM";
    public static final String WEBSITE_TEMPLATE_FORM = "WEBSITE_TEMPLATE_FORM";
    public static final String WEBSITE_THEME_FORM = "WEBSITE_THEME_FORM";
    public static final String WEBSITE_MENU_FORM = "WEBSITE_MENU_FORM";
    public static final String CERTIFICATE_OF_EMPLOYMENT_FORM = "CERTIFICATE_OF_EMPLOYMENT_FORM";
    public static final String CUSTOMIZE_CERTIFICATE = "CUSTOMIZE_CERTIFICATE";

    //Accounting Settings
    public static final String PRODUCT_NUMBERING_SETTINGS_FORM = "PRODUCT_NUMBERING_SETTINGS_FORM";
    public static final String ACCOUNT_NUMBERING_SETTINGS_FORM = "ACCOUNT_NUMBERING_SETTINGS_FORM";
    public static final String FINANCIAL_SETTINGS_FORM = "FINANCIAL_SETTINGS_FORM";
    public static final String TRANSACTION_LOCKING_FORM = "TRANSACTION_LOCKING_FORM";
    public static final String INVOICE_SETTINGS_FORM = "INVOICE_SETTINGS_FORM";
    public static final String PREPAYMENT_FORM = "PREPAYMENT_FORM";
    public static final String SUPPLIER_CREDIT_FORM = "SUPPLIER_CREDIT_FORM";
    public static final String INVOICE_PAYMENT_VIEW = "INVOICE_PAYMENT_VIEW";
    public static final String IMPORT_PROJECT_FORM = "IMPORT_PROJECT_FORM";
    public static final String IMPORT_GROUP_PAYRUN_FORM = "IMPORT_GROUP_PAYRUN_FORM";
    public static final String IMPORT_PAYMENT_DEDUCTION_FORM = "IMPORT_PAYMENT_DEDUCTION_FORM";
    public static final String IMPORT_PRODUCT_CATEGORIES_FORM = "IMPORT_PRODUCT_CATEGORIES_FORM";
    public static final String BANK_STATEMENT_ITEM_FORM = "BANK_STATEMENT_ITEM_FORM";
    public static final String IMPORT_MANUAL_TRANSACTION_FORM = "IMPORT_MANUAL_TRANSACTION_FORM";
    public static final String IMPORT_MANUAL_TRANSACTION_FORM_TALLY = "IMPORT_MANUAL_TRANSACTION_FORM_TALLY";
    public static final String IMPORT_ADDITIONAL_PAYMENT_FORM = "IMPORT_ADDITIONAL_PAYMENT_FORM";

    public static final String BENEFIT_FORM = "BENEFIT_FORM";
    public static final String EMAIL_SETTINGS_FORM = "EMAIL_SETTINGS_FORM";
    public static final String EMAIL_PARENT_FILTER_FORM = "EMAIL_PARENT_FILTER_FORM";
    public static final String USER_CREDENTIALS_FORM = "USER_CREDENTIALS_FORM";

    public static final String PROJECT_BASE_EXPENSE_FORM = "PROJECT_BASE_EXPENSE_FORM";
    public static final String INTEGRATION_SETTINGS_FORM = "INTEGRATION_SETTINGS_FORM";
    public static final String CUSTOM_FORM_FORM = "CUSTOM_FORM_FORM";
    public static final String SWITCHVOX_FORM = "SWITCHVOX_FORM";
    public static final String GOOGLE_INTEGRATION_FORM = "GOOGLE_INTEGRATION_FORM";
    public static final String MICROSOFT_INTEGRATION_FORM = "MICROSOFT_INTEGRATION_FORM";
    public static final String PAYPAL_PAYMENT_FORM = "PAYPAL_PAYMENT_FORM";
    public static final String DOCUMENT_INTEGRATION_FORM = "DOCUMENT_INTEGRATION_FORM";
    public static final String STRIPE_PAYMENT_FORM = "STRIPE_PAYMENT_FORM";
    public static final String SHOPIFY_INTEGRATION_FORM = "SHOPIFY_INTEGRATION_FORM";
    public static final String DYNAMIC_LOGIN_FORM = "DYNAMIC_LOGIN_FORM";
    public static final String WHITE_LABEL_FORM = "WHITE_LABEL_FORM";
    public static final String SHIFT_SETTINGS_FORM = "SHIFT_SETTINGS_FORM";

    public static final String RECRUITMENT_INTEGRATION_FORM = "RECRUITMENT_INTEGRATION_FORM";
    public static final String IMPORT_LEAVE_ALLOWANCE_FORM = "IMPORT_LEAVE_ALLOWANCE_FORM";
    public static final String SIPUNI_SETTINGS_FORM = "SIPUNI_SETTINGS_FORM";
    public static final String MYCALLS_SETTINGS_FORM = "MYCALLS_SETTINGS_FORM";

    public static final String PAYROLL_RECURRING_DEDUCTION_FORM = "PAYROLL_RECURRING_DEDUCTION_FORM";
    public static final String PAYROLL_RECURRING_PAYMENT_FORM = "PAYROLL_RECURRING_PAYMENT_FORM";
    public static final String BACKUPS_EMPLOYEE_FORM = "BACKUPS_EMPLOYEE_FORM";
    public static final String PAYROLL_ZONE_FORM = "PAYROLL_ZONE_FORM";
    public static final String MINIMUM_WAGE_FORM = "MINIMUM_WAGE_FORM";
    public static final String WAGE_RATE_FORM = "WAGE_RATE_FORM";
    public static final String ATTENDANCE_TERMINAL_FORM = "ATTENDANCE_TERMINAL_FORM";

    public static ArrayList<String> constantArray() {
        ArrayList<String> sections = new ArrayList<>();
        sections.add(PM_SECTION);
        sections.add(CRM_SECTION);
        sections.add(ACCOUNTING_SECTION);
        sections.add(HRMS_SECTION);
        sections.add(DOCUMENT_SECTION);
        sections.add(SETTINGS_SECTION);
        return sections;
    }

    private Integer objectID;
    private String formID;
    private String layout;
    private String customCss;
    private String webFormUrl;
    private String title;
    private boolean active;
    private boolean webForm;
    private boolean addForm;
    private boolean editForm;
    private boolean viewForm;
    private boolean importForm;
    private ArrayList<CustomFormValidation> validations = new ArrayList<>();
    private ArrayList<String> requiredCodes = new ArrayList<>();

    public ArrayList<CustomFormValidation> getValidations() {
        return validations;
    }

    public void setValidations(ArrayList<CustomFormValidation> validations) {
        this.validations = validations;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getCustomCss() {
        return customCss;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
    }

    public boolean isWebForm() {
        return webForm;
    }

    public void setWebForm(boolean webForm) {
        this.webForm = webForm;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getWebFormUrl() {
        return webFormUrl;
    }

    public void setWebFormUrl(String webFormUrl) {
        this.webFormUrl = webFormUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAddForm() {
        return addForm;
    }

    public void setAddForm(boolean addForm) {
        this.addForm = addForm;
    }

    public boolean isEditForm() {
        return editForm;
    }

    public void setEditForm(boolean editForm) {
        this.editForm = editForm;
    }

    public boolean isViewForm() {
        return viewForm;
    }

    public void setViewForm(boolean viewForm) {
        this.viewForm = viewForm;
    }

    public boolean isImportForm() {
        return importForm;
    }

    public void setImportForm(boolean importForm) {
        this.importForm = importForm;
    }

    public static SelectItem[] getFormIDs() {
        int i = 1;
        return new SelectItem[]{
                new SelectItem(i++, LEAD_FORM),
                new SelectItem(i++, CONTACT_FORM),
                new SelectItem(i++, OPPORTUNITY_FORM),
                new SelectItem(i++, CASE_FORM),
                new SelectItem(i++, CAMPAIGN_FORM),
                new SelectItem(i++, ACCOUNT_FORM),
                new SelectItem(i++, WEB_FORM),
                new SelectItem(i++, SALE_INVOICE_FORM),
                new SelectItem(i++, PURCHASE_INVOICE_FORM),
                new SelectItem(i++, SALE_QUOTE_FORM),
                new SelectItem(i++, PURCHASE_ORDER),
                new SelectItem(i++, PRODUCT),
                new SelectItem(i++, EXPENSE_FORM),
                new SelectItem(i++, CREDIT_NOTE_FORM),
                new SelectItem(i++, CLIENT_FORM),
                new SelectItem(i++, SOLUTION_FORM),
                new SelectItem(i++, ACTIVITY_FORM),
                new SelectItem(i++, REFERENCE_FORM),
                new SelectItem(i++, LEAVE_REASON_FORM),
                new SelectItem(i++, MESSAGE_CENTER_FORM),
                new SelectItem(i++, EMAIL_COMPOSE_FORM),
                new SelectItem(i++, ACCOUNTING_EMAIL_COMPOSE_FORM),
                new SelectItem(i++, EXPENSE_EMAIL_COMPOSE_FORM),
                new SelectItem(i++, EMAIL_SUMMARY_COMPOSE_FORM),
                new SelectItem(i++, PERSONAL_GOAL_FORM),
                new SelectItem(i++, DEPARTMENT_GOAL_FORM),
                new SelectItem(i++, PROJECT_GOAL_FORM),
                new SelectItem(i++, BUSINESS_GOAL_FORM),
                new SelectItem(i++, COMPANY_GOAL_FORM),
                new SelectItem(i++, GROUP_GOAL_FORM),
                new SelectItem(i++, TASK_MAX_FORM),
                new SelectItem(i++, TASK_MIN_FORM),
                new SelectItem(i++, MESSAGE_FORM),
                new SelectItem(i++, MEETING_MINUTES),
                new SelectItem(i++, VACANCY_FORM),
                new SelectItem(i++, OVERTIME_FORM),
                new SelectItem(i++, SHIFT_FORM),
                new SelectItem(i++, BRIGADA_FORM),
                new SelectItem(i++, PLACEMENT_FORM),
                new SelectItem(i++, POSITION_FORM),
                new SelectItem(i++, DEPARTMENT_FORM),
                new SelectItem(i++, CHART_OF_ACCOUNT_FORM),
                new SelectItem(i++, LOCATION_FORM),
                new SelectItem(i++, IMPORT_PRODUCT_FORM),
                new SelectItem(i++, SELECT_CANDIDATE_FORM),
                new SelectItem(i++, IMPORT_CRM_LEAD_FORM),
                new SelectItem(i++, IMPORT_CRM_CONTACT_FORM),
                new SelectItem(i++, IMPORT_CRM_ACCOUNT_FORM),
                new SelectItem(i++, IMPORT_CLIENT_FORM),
                new SelectItem(i++, IMPORT_PURCHASE_ORDERS_FORM),
                new SelectItem(i++, IMPORT_SUPPLIER_FORM),
                new SelectItem(i++, MANUAL_JOURNAL_FORM),
                new SelectItem(i++, REQUEST_FOR_QUOTE_FORM),
                new SelectItem(i++, POSITION_VIEW),
                new SelectItem(i++, WORKSTREAM_FORM),
                new SelectItem(i++, BONUS_SETTING_FORM),
                new SelectItem(i++, ELIGIBLE_SETTING_FORM),
                new SelectItem(i++, IMPORT_HRMS_CANDIDATE_FORM),
                new SelectItem(i++, DONOTAPPLY),
                new SelectItem(i++, ONBOARDING_STEP_FORM),
                new SelectItem(i++, ONBOARDING_PERIOD_FORM),
                new SelectItem(i++, APPRAISAL_APPROVAL_FORM),
                new SelectItem(i++, LEAVE_REQUEST_COMMENT_FORM),
                new SelectItem(i++, LEAVE_REQUEST_FORM),
                new SelectItem(i++, CRM_WEB_FORM),
                new SelectItem(i++, CRM_WEB_FORM_FOR_VIEW),
                new SelectItem(i++, ENQUIRY_FORM),
                new SelectItem(i++, ATTENDENCE_SHEET_FORM),
                new SelectItem(i++, PROFILE_SETTINGS_FORM),
                new SelectItem(i++, SMS_SETTINGS_FORM),
                new SelectItem(i++, ASSESSMENT_FORM),
                new SelectItem(i++, EMPLOYEE_CLIENT_DOCUMENTS_FORM),
                new SelectItem(i++, SMS_MESSAGE_FORM),
                new SelectItem(i++, CONSOLIDATION_COMPANY_FORM),
                new SelectItem(i++, PERFORMANCE_NOTE_FORM),
                new SelectItem(i++, INCIDENT_FORM),
                new SelectItem(i++, ISSUE_FORM),
                new SelectItem(i++, BOOKINGITEMS),
                new SelectItem(i++, BOOKINGITEMSRESERVATION),
                new SelectItem(i++, BOOKINGITEMSRESERVATIONEDIT),
                new SelectItem(i++, SALARY_GRADE_FORM),
                new SelectItem(i++, EXCHANGE_RATE_FORM),
                new SelectItem(i++, DEPENDENT_FORM),
                new SelectItem(i++, MULTILEAD_OR_CONTACT_FORM),
                new SelectItem(i++, PAYROLL_STARTER_FORM),
                new SelectItem(i++, EMAIL_TEMPLATE_FORM),
                new SelectItem(i++, JOB_DESCRIPTION_FORM),
                new SelectItem(i++, BUDGET_AND_INCUMBENTS_FORM),
                new SelectItem(i++, MULTIPLE_APPROVAL_REQUEST_FORM),
                new SelectItem(i++, COMPETENCY_FORM),
                new SelectItem(i++, APPRAISAL_TEMPLATE_FORM),
                new SelectItem(i++, PAST_EMPLOYMENT_FORM),
                new SelectItem(i++, INTERNAL_EMPLOYMENT_FORM),
                new SelectItem(i++, EDUCATION_FORM),
                new SelectItem(i++, AWARD_FORM),
                new SelectItem(i++, TIMESLOT_FORM),
                new SelectItem(i++, HOLIDAY_FORM),
                new SelectItem(i++, GOAL_WEIGHT_EDIT_FORM),
                new SelectItem(i++, SIGNATURE_FORM),
                new SelectItem(i++, REQUEST_FOR_PURCHASE_FORM),
                new SelectItem(i++, PENALTIES_PROMOTIONS_FORM),
                new SelectItem(i++, EMPLOYEE_PENALTIES_PROMOTIONS_FORM),
                new SelectItem(i++, BONUS_RECOMMENDATIONS_FORM),
                new SelectItem(i++, TENDER_EXCHANGE_CARD_FORM),
                new SelectItem(i++, ESTIMATE_OF_CONTRACT_FORM),
                new SelectItem(i++, WORKFLOW_FORM),
                new SelectItem(i++, PAYROLL_PAYSLIP_TABLE_FORM),
                new SelectItem(i++, PAYROLL_PAYRUN_PAYMENT_FORM),
                new SelectItem(i++, PAYROLL_PAYMENT_FORM),
                new SelectItem(i++, WORKFLOW_TASK_MIN_FORM),
                new SelectItem(i++, WORKFLOW_ALERT_FORM),
                new SelectItem(i++, HRMS_COMPANY_NEWS_FORM),
                new SelectItem(i++, HRMS_COMPANY_NEWS_SUMMARY),
                new SelectItem(i++, PAYROLL_EOS_CALCULATION_FORM),
                new SelectItem(i++, TIMESHEET_SETTINGS_FORM),
                new SelectItem(i++, TIMESHEET_REMINDER_FORM),
                new SelectItem(i++, HSE_PASSPORT_FORM),
                new SelectItem(i++, BANK_TRANSACTION_FORM),
                new SelectItem(i++, BANK_ACCOUNT_FORM),
                new SelectItem(i++, PAYROLL_TAXI_PAYRUN_FORM),
                new SelectItem(i++, PAYROLL_CASH_ADVANCE_FORM),
                new SelectItem(i++, FIXED_ASSET_FORM),
                new SelectItem(i++, WEBSITE_FORM),
                new SelectItem(i++, WEBSITE_LAYOUT_FORM),
                new SelectItem(i++, WEBSITE_PAGE_FORM),
                new SelectItem(i++, PAYROLL_ALLOWANCE_DEDUCTION_SETTINGS_FORM),
                new SelectItem(i++, WEBSITE_TEMPLATE_FORM),
                new SelectItem(i++, WEBSITE_THEME_FORM),
                new SelectItem(i++, WEBSITE_MENU_FORM),
                new SelectItem(i++, CERTIFICATE_OF_EMPLOYMENT_FORM),
                new SelectItem(i++, CUSTOMIZE_CERTIFICATE),
                new SelectItem(i++, STOCK_TRANSFER_FORM),
                new SelectItem(i++, SMS_TEMPLATE_FORM),
                new SelectItem(i++, EMPLOYEE_LEAVE_TYPES_FORM),
                new SelectItem(i++, CONSIGNMENT_FORM),
                new SelectItem(i++, BENEFIT_FORM),
                new SelectItem(i++, PAYROLL_COMPANY_SETTINGS_FORM),
                new SelectItem(i++, PAYROLL_PENSION_PROVIDER_FORM),
                new SelectItem(i++, PAYROLL_END_OF_SERVICE_SETTINGS_FORM),
                new SelectItem(i++, PRODUCT_NUMBERING_SETTINGS_FORM),
                new SelectItem(i++, ACCOUNT_NUMBERING_SETTINGS_FORM),
                new SelectItem(i++, FINANCIAL_SETTINGS_FORM),
                new SelectItem(i++, INVOICE_SETTINGS_FORM),
                new SelectItem(i++, PAYROLL_PENSION_SCHEMA_FORM),
                new SelectItem(i++, EMPLOYEE_BENEFIT_FORM),
                new SelectItem(i++, BENEFIT_REQUEST_FORM),
                new SelectItem(i++, PREPAYMENT_FORM),
                new SelectItem(i++, HRMS_EMPLOYEE_FORM),
                new SelectItem(i++, INVOICE_PAYMENT_VIEW),
                new SelectItem(i++, WAREHOUSE_FORM),
                new SelectItem(i++, STOCKADJUSTMENTS_FORM),
                new SelectItem(i++, IMPORT_PROJECT_FORM),
                new SelectItem(i++, IMPORT_GROUP_PAYRUN_FORM),
                new SelectItem(i++, IMPORT_PAYMENT_DEDUCTION_FORM),
                new SelectItem(i++, IMPORT_PRODUCT_CATEGORIES_FORM),
                new SelectItem(i++, EMPLOYEE_DOCUMENTS_FORM),
                new SelectItem(i++, STOCKADJUSTMENTS_FORM),
                new SelectItem(i++, IMPORT_PROJECT_FORM),
                new SelectItem(i++, SALEQUOTE_FORM),
                new SelectItem(i++, Constants.BATCH_PAYMENT_FORM),
                new SelectItem(i++, PAYROLL_BATCH_FORM),
                new SelectItem(i++, TRANSACTION_ITEM_VIEW_FORM),
                new SelectItem(i++, TAX_RATE_FORM),
                new SelectItem(i++, BRAND_FORM),
                new SelectItem(i++, SHIPPING_METHOD_FORM),
                new SelectItem(i++, PRICE_LEVEL_FORM),
                new SelectItem(i++, TERMS_FORM),
                new SelectItem(i++, DISCOUNT_FORM),
                new SelectItem(i++, MEASURMENT_FORM),
                new SelectItem(i++, PRODUCT_CATEGORY_FORM),
                new SelectItem(i++, TRANSACTION_ITEM_VIEW_FORM),
                new SelectItem(i++, BANK_STATEMENT_ITEM_FORM),
                new SelectItem(i++, PURCHASEORDER_FORM),
                new SelectItem(i++, COUNTRY_SETTINGS_FORM),
                new SelectItem(i++, CURRENCY_FORM),
                new SelectItem(i++, EMAIL_SETTINGS_FORM),
                new SelectItem(i++, EMAIL_PARENT_FILTER_FORM),
                new SelectItem(i++, PICK_LIST_FORM),
                new SelectItem(i++, WORKFLOW_EMPLOYEE_FORM),
                new SelectItem(i++, BUILD_ASSEMBLY_FORM),
                new SelectItem(i++, WORKFLOW_INVOICE_FORM),
                new SelectItem(i++, PROJECT_BASE_EXPENSE_FORM),
                new SelectItem(i++, INTEGRATION_SETTINGS_FORM),
                new SelectItem(i++, IMPORT_BANK_TRANSACTION_FORM),
                new SelectItem(i++, IMPORT_BUDGET_MANAGER_FORM),
                new SelectItem(i++, TELEGRAM_CHAT_FORM),
                new SelectItem(i++, GENERAL_SETTINGS_FORM),
                new SelectItem(i++, MAIL_LIST_FORM),
                new SelectItem(i++, USER_CREDENTIALS_FORM),
                new SelectItem(i++, CRM_SETTINGS_FORM),
                new SelectItem(i++, CUSTOMIZE_FORM),
                new SelectItem(i++, CUSTOM_FORM_FORM),
                new SelectItem(i++, SWITCHVOX_FORM),
                new SelectItem(i++, PHANTOM_PDF_FORM),
                new SelectItem(i++, BILLABLE_EXPENSE_FORM),
                new SelectItem(i++, ITEXT_PDF_FORM),
                new SelectItem(i++, IMPORT_BRAND_FORM),
                new SelectItem(i++, DYNAMIC_LOGIN_FORM),
                new SelectItem(i++, IMPORT_POSITION_FORM),
                new SelectItem(i++, IMPORT_DEPARTMENT_FORM),
                new SelectItem(i++, IMPORT_POSITION_FORM),
                new SelectItem(i++, WHITE_LABEL_FORM),
                new SelectItem(i++, AIPHANTOM_PDF_FORM)
        };
    }

    public ArrayList<String> getRequiredCodes() {
        if (requiredCodes.size() == 0 && validations.size() > 0) {
            for (CustomFormValidation validation : validations) {
                if (ValidationType.IsEmpty.getId().equals(validation.getValidationTypeID())) {//required
                    requiredCodes.add(validation.getFieldCode());
                }
            }
        }
        return requiredCodes;
    }

    public static HashSet<String> getFieldIDs(String layout) {
        HashSet<String> set = new LinkedHashSet<>();
        if (layout != null && !"".equals(layout)) {
            recursiveAddFields(set, layout);
        }
        return set;
    }

    private static void recursiveAddFields(HashSet<String> set, String layout) {
        if (layout.contains("$$")) {
            layout = layout.substring(layout.indexOf("$$") + 2);
            String field = layout.substring(0, layout.indexOf("$$"));
            layout = layout.substring(layout.indexOf("$$") + 2);
            if (field.matches("^input:.*$")) {
                field = field.split(":")[1];
                set.add(field);
            }
            recursiveAddFields(set, layout);
        }
    }

    public static SelectItem[] getSection() {
        Integer i = 1;
        return new SelectItem[]{
                new SelectItem(i++, PermissionConstants.PM_CONTEXT),
                new SelectItem(i++, PermissionConstants.HRMS_CONTEXT),
                new SelectItem(i++, PermissionConstants.ACCOUNTING_CONTEXT),
                new SelectItem(i++, PermissionConstants.CRM_CONTEXT),
                new SelectItem(i++, PermissionConstants.PAYROLL_CONTEXT),
                new SelectItem(i++, PermissionConstants.WORKSPACE_CONTEXT),
                new SelectItem(i++, PermissionConstants.DOCUMENTS_CONTEXT),
                new SelectItem(i++, PermissionConstants.DASHBOARD_CONTEXT),
                new SelectItem(i++, PermissionConstants.REPORTING),
                new SelectItem(i++, PermissionConstants.SETTINGS_CONTEXT),
                new SelectItem(i++, PermissionConstants.USERMENU_CONTEXT)
        };
    }

    public static SelectItem[] getViewBySection(String section) {
        Integer i = 1;
        if (PermissionConstants.PM_CONTEXT.equals(section)) {
            return new SelectItem[]{
                    new SelectItem(i++, PermissionConstants.PM_WELLCOME_VIEW),
                    new SelectItem(i++, PermissionConstants.PM_TASKS_LIST),
                    new SelectItem(i++, PermissionConstants.PM_TASKS_ADD),
                    new SelectItem(i++, PermissionConstants.PM_TASKS_EDIT),
                    new SelectItem(i++, PermissionConstants.PM_ISSUE_LIST),
                    new SelectItem(i++, PermissionConstants.PM_ISSUE_ADD),
                    new SelectItem(i++, PermissionConstants.PM_ISSUE_EDIT),
                    new SelectItem(i++, PermissionConstants.PM_PROJECT_LIST),
                    new SelectItem(i++, PermissionConstants.PM_PROJECT_ADD),
                    new SelectItem(i++, PermissionConstants.PM_PROJECT_EDIT),
                    new SelectItem(i++, PermissionConstants.PM_EMPLOYEE_LIST),
                    new SelectItem(i++, PermissionConstants.PM_CLIENT_DOCUMENT_LIST),
                    new SelectItem(i++, PermissionConstants.PM_TIMESHEET_APPROVAL),
                    new SelectItem(i++, PermissionConstants.PM_RESOURCE_UTILIZATION_LIST),
                    new SelectItem(i++, PermissionConstants.PM_CUSTOMER_CENTER_LIST),
                    new SelectItem(i++, PermissionConstants.PM_CUSTOMER_ADD_CLIENT),
                    new SelectItem(i++, PermissionConstants.PM_CUSTOMER_EDIT)
            };

        } else if (PermissionConstants.CRM_CONTEXT.equals(section)) {
            return new SelectItem[]{
                    new SelectItem(i++, PermissionConstants.CRM_WELCOME_VIEW),
                    new SelectItem(i++, PermissionConstants.CRM_LEADS_LIST),
                    new SelectItem(i++, PermissionConstants.ADD_NEW_LEAD),
                    new SelectItem(i++, PermissionConstants.CRM_LEAD_EDIT),
                    new SelectItem(i++, PermissionConstants.CRM_OPPORTUNITIES_LIST),
                    new SelectItem(i++, PermissionConstants.CRM_OPPORTUNITIES_ADD),
                    new SelectItem(i++, PermissionConstants.CRM_OPPORTUNITIES_EDIT),
                    new SelectItem(i++, PermissionConstants.CRM_ACCOUNTS_LIST),
                    new SelectItem(i++, PermissionConstants.CRM_ACCOUNT_ADD),
                    new SelectItem(i++, PermissionConstants.CRM_ACCOUNTS_EDIT),
                    new SelectItem(i++, PermissionConstants.CRM_CAMPAIGNS_LIST),
                    new SelectItem(i++, PermissionConstants.CRM_CAMPAIGN_ADD),
                    new SelectItem(i++, PermissionConstants.CRM_CAMPAIGN_EDIT),
                    new SelectItem(i++, PermissionConstants.CRM_TASKS_LIST),
                    new SelectItem(i++, PermissionConstants.CRM_TASKS_ADD),
                    new SelectItem(i++, PermissionConstants.CRM_TASKS_EDIT),
                    new SelectItem(17, PermissionConstants.CRM_SOLUTIONS_LIST),
                    new SelectItem(18, PermissionConstants.CRM_SOLUTION_ADD),
                    new SelectItem(19, PermissionConstants.CRM_SOLUTION_EDIT),
                    new SelectItem(i++, PermissionConstants.CRM_CASES_LIST),
                    new SelectItem(i++, PermissionConstants.CRM_CASE_ADD),
                    new SelectItem(i++, PermissionConstants.CRM_CASE_EDIT),
                    new SelectItem(i++, PermissionConstants.CRM_ACTIVITIES_LIST),
                    new SelectItem(i++, PermissionConstants.CRM_MESSAGE_ADD),
                    new SelectItem(i++, PermissionConstants.CRM_MESSAGE_EDIT),
                    new SelectItem(i++, PermissionConstants.CRM_DRAFT_MESSAGES_LIST),
                    new SelectItem(i++, PermissionConstants.CRM_SENT_MESSAGES_LIST),
                    new SelectItem(i++, PermissionConstants.CRM_QUEUED_MESSAGES_LIST),
                    new SelectItem(i++, PermissionConstants.CRM_MAILING_LIST),
                    new SelectItem(i++, PermissionConstants.CRM_CONTACT_ADD),
                    new SelectItem(i++, PermissionConstants.CRM_CONTACT_EDIT),
                    new SelectItem(i++, PermissionConstants.CRM_WEB_FORM_ADD),
                    new SelectItem(i++, PermissionConstants.CRM_WEB_FORM_EDIT),
                    new SelectItem(i++, PermissionConstants.CRM_MESSAGE_CENTER)
            };
        } else if (PermissionConstants.HRMS_CONTEXT.equals(section)) {
            return new SelectItem[]{
                    new SelectItem(i++, PermissionConstants.HRMS_WELCOME_VIEW),
                    new SelectItem(i++, PermissionConstants.HRMS_EMPLOYEE_LIST),
                    new SelectItem(i++, PermissionConstants.HRMS_DEPARTMENT_LIST),
                    new SelectItem(i++, PermissionConstants.HRMS_DEPARTMENT_ADD),
                    new SelectItem(i++, PermissionConstants.HRMS_DEPARTMENT_EDIT),
                    new SelectItem(i++, PermissionConstants.HRMS_POSITION_LIST),
                    new SelectItem(i++, PermissionConstants.HRMS_POSITION_ADD),
                    new SelectItem(i++, PermissionConstants.HRMS_POSITION_EDIT),
                    new SelectItem(i++, PermissionConstants.HRMS_GRADE_LIST),
                    new SelectItem(i++, PermissionConstants.HRMS_GRADE_ADD),
                    new SelectItem(i++, PermissionConstants.HRMS_GRADE_EDIT),
                    new SelectItem(i++, PermissionConstants.HRMS_LOCATION),
                    new SelectItem(i++, PermissionConstants.HRMS_ADD_NEW_LOCATION),
                    new SelectItem(i++, PermissionConstants.HRMS_EDIT_LOCATION),
                    new SelectItem(i++, PermissionConstants.HRMS_ONBOARDING_LIST),
                    new SelectItem(i++, PermissionConstants.HRMS_ONBOARDING_ADD),
                    new SelectItem(i++, PermissionConstants.HRMS_ONBOARDING_EDIT),
                    new SelectItem(i++, PermissionConstants.HRMS_ONBOARDING_STEP_LIST),
                    new SelectItem(i++, PermissionConstants.HRMS_ONBOARDING_STEP_ADD),
                    new SelectItem(i++, PermissionConstants.HRMS_ONBOARDING_STEP_EDIT),
                    new SelectItem(i++, PermissionConstants.HRMS_PROFILE_DOCUMENT_LIST),
                    new SelectItem(i++, PermissionConstants.HRMS_EMPLOYEE_BONUS_LIST)


            };
        } else if (PermissionConstants.USERMENU_CONTEXT.equals(section)) {
            return new SelectItem[]{
                    new SelectItem(i++, PermissionConstants.WIKI)
            };
        }
        return null;
    }

    public boolean isButtonPanelDisabled() {
        return getLayout() != null && getLayout().contains(CustomFormConstants.DISABLE_BUTTON_PANEL);
    }

    public static String getFormIDByRelationType(String relationType) {
        if (relationType == null) {
            return null;
        }
        if (RelationItem.TYPE_CONTACT.equals(relationType)) {
            return CONTACT_FORM;
        } else if (RelationItem.TYPE_LEAD.equals(relationType)) {
            return LEAD_FORM;
        } else if (RelationItem.TYPE_CANDIDATE.equals(relationType)) {
            return CANDIDATE_FORM;
        } else if (RelationItem.TYPE_CAMPAIGN.equals(relationType)) {
            return CAMPAIGN_FORM;
        } else if (RelationItem.TYPE_CASE.equals(relationType)) {
            return CASE_FORM;
        } else if (RelationItem.TYPE_EVENT.equals(relationType)) {
            return ACTIVITY_FORM;
        } else if (RelationItem.TYPE_COURCE_SCHEDULE.equals(relationType)) {
            return SCHEDULED_COURSE_FORM;
        } else if (RelationItem.TYPE_CS_STUDENT.equals(relationType)) {
            return CS_STUDENT_FORM;
        } else if (RelationItem.TYPE_SALEINVOICE.equals(relationType) || RelationItem.TYPE_CREDIT_NOTE.equals(relationType)) {
            return SALEINVOICE_FORM;
        } else if (RelationItem.TYPE_SALEQUOTE.equals(relationType) || RelationItem.TYPE_SALEORDER.equals(relationType)) {
            return SALEQUOTE_FORM;
        } else if (RelationItem.TYPE_PURCHASE_ORDER.equals(relationType)) {
            return PURCHASEORDER_FORM;
        } else if (RelationItem.TYPE_EMPLOYEE.equals(relationType)) {
            return HRMS_EMPLOYEE_FORM;
        } else if (RelationItem.TYPE_LEAVE_REQUEST.equals(relationType)) {
            return LEAVE_REQUEST_FORM;
        } else if (RelationItem.TYPE_OPPORTUNITY.equals(relationType)) {
            return OPPORTUNITY_FORM;
        } else if (RelationItem.TYPE_CASH_ADVANCE.equals(relationType)) {
            return CASH_ADVANCE_FORM;
        } else if (RelationItem.TYPE_EXPENSE_CLAIM.equals(relationType)) {
            return EXPENSE_CLAIM_FORM;
        } else if (RelationItem.TYPE_CERTIFICATE.equals(relationType)) {
            return CERTIFICATE_FORM;
        } else if (RelationItem.TYPE_PROJECT.equals(relationType)) {
            return PROJECT_FORM;
        } else if (RelationItem.TYPE_PRODUCT.equals(relationType)) {
            return PRODUCT;
        } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
            return ACCOUNT_FORM;
        } else if (RelationItem.TYPE_CERTIFICATE_OF_EMPLOYMENT.equals(relationType)) {
            return CERTIFICATE_OF_EMPLOYMENT_FORM;
        } else if (RelationItem.REQUEST_FOR_PURCHASE.equals(relationType)) {
            return REQUEST_FOR_PURCHASE_FORM;
        } else if (RelationItem.TYPE_MANUAL_JOURNAL.equals(relationType)) {
            return MANUAL_JOURNAL_FORM;
        } else if (RelationItem.TYPE_TASK.equals(relationType)) {
            return TASK_MAX_FORM;
        } else if (RelationItem.TYPE_REQUEST_FOR_QUOTE.equals(relationType)) {
            return REQUEST_FOR_QUOTE_FORM;
        } else if (RelationItem.TYPE_PURCHASE_INVOICE.equals(relationType) || RelationItem.TYPE_DEBIT_NOTE.equals(relationType)) {
            return PURCHASEINVOICE_FORM;
        } else if (RelationItem.TYPE_DEPARTMENT.equals(relationType)) {
            return DEPARTMENT_FORM;
        } else if (RelationItem.TYPE_POSITION.equals(relationType)) {
            return POSITION_FORM;
        } else if (RelationItem.TYPE_RENTAL_ORDER.equals(relationType)) {
            return RENTAL_ORDER_FORM;
        } else if (RelationItem.TYPE_BUILD_ASSEMBLY.equals(relationType)) {
            return BUILD_ASSEMBLY_FORM;
        } else if (RelationItem.TYPE_RENTAL_PRODUCT.equals(relationType)) {
            return RENTAL_ORDER_FORM;
        } else if (RelationItem.TYPE_BATCH_PAYMENT_RECEIVABLE.equals(relationType)) {
            return RECEIVE_PAYMENT_FORM;
        } else if (RelationItem.TYPE_BATCH_PAYMENT_PAYABLE.equals(relationType)) {
            return PAY_INVOICE_FORM;
        } else if (RelationItem.TYPE_INCIDENT.equals(relationType)) {
            return INCIDENT_FORM;
        } else if (RelationItem.TYPE_PLACEMENT.equals(relationType)) {
            return PLACEMENT_FORM;
        } else if (RelationItem.TYPE_ROTATION.equals(relationType)) {
            return ROTATION_FORM;
        } else if (RelationItem.TYPE_GROUP_PLACEMENT.equals(relationType)) {
            return GROUP_PLACEMENT_FORM;
        } else if (RelationItem.TYPE_VACANCY.equals(relationType)) {
            return VACANCY_FORM;
        } else if (RelationItem.TYPE_PRODUCT_CATEGORY.equals(relationType)) {
            return PRODUCT_CATEGORY_FORM;
        } else if (RelationItem.TYPE_COMPANY_SETTINGS.equals(relationType)) {
            return COMPANY_SETTINGS_FORM;
        } else if (RelationItem.TYPE_EMPLOYEE_DOCUMENTS.equals(relationType)) {
            return EMPLOYEE_DOCUMENTS_FORM;
        } else {
            return relationType;
        }
    }
}
