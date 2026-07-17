package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.HRMS.PLACCEMENT.CURRENT_USER;

/**
 * User: Ilhombek
 * Date: 21.07.2010
 * Time: 18:45:43
 */
public interface EmailTemplateConstants {

    //email template modules
    String _EMAIL_TEMPLATE_MODULE = "_EMAIL_TEMPLATE_MODULE";
    String ET_LEAD_MODULE = "ET_LEAD_MODULE";
    String ET_CASE_MODULE = "ET_CASE_MODULE";
    String ET_CONTACT_MODULE = "ET_CONTACT_MODULE";
    String ET_CRM_ACCOUNT_MODULE = "ET_CRM_ACCOUNT_MODULE";
    String ET_OPPORTUNITY_MODULE = "ET_OPPORTUNITY_MODULE";
    String ET_EVENT_MODULE = "ET_EVENT_MODULE";
    String ET_CALL_MODULE = "ET_CALL_MODULE";
    String ET_EXPENSE_MODULE = "ET_EXPENSE_MODULE";
    String ET_INVOICE_MODULE = "ET_INVOICE_MODULE";
    String ET_PURCHASE_ORDER_MODULE = "ET_PURCHASE_ORDER_MODULE";
    String ET_SALES_QUOTE_MODULE = "ET_SALES_QUOTE_MODULE";
    String ET_RFP_MODULE = "ET_RFP_MODULE";
    String ET_PROJECT_MODULE = "ET_PROJECT_MODULE";
    String ET_PAYROLL_MODULE = "ET_PAYROLL_MODULE";
    String ET_CASH_ADVANCE_MODULE = "ET_CASH_ADVANCE_MODULE";
    String ET_SMS_MODULE = "ET_SMS_MODULE";
    String ET_EMPLOYEE_MODULE = "ET_EMPLOYEE_MODULE";
    String ET_ADDITIONAL_PAYMENT_MODULE = "ET_ADDITIONAL_PAYMENT_MODULE";
    String ET_TASK_MODULE = "ET_TASK_MODULE";
    String ET_RFQ_MODULE = "ET_RFQ_MODULE";
    String ET_LR_MODULE = "ET_LR_MODULE";
    String ET_CANDIDATE_MODULE = "ET_CANDIDATE_MODULE";
    String DEVICE_TOKEN = "DEVICE_TOKEN";
    String ET_PLACEMENT_MODULE = "ET_PLACEMENT_MODULE";

    /// ////////////////////// ET_ -- EMAIL TEMPLATE FIELDS /////////////////////////

    String DEFAULT_EMAIL_TEMPLATE = "DEFAULT_EMAIL_TEMPLATE";
    String COMPANY_EMAIL_TEMPLATE = "COMPANY_EMAIL_TEMPLATE";

    /// ////////////////////// PUBLIC RELATED /////////////////////////
    String ET_FIRST_NAME = "${first_name}";
    String ET_LAST_NAME = "${last_name}";
    String ET_COMPANY_NAME = "${company_name}";
    String ET_COMPANY_ID = "${company_id}";
    String ET_EMAIL = "${email}";
    String ET_START_DATE = "${start_date}";
    String ET_END_DATE = "${end_date}";
    String ET_DUE_DATE = "${due_date}";
    String ET_NAME = "${name}";
    String ET_LOCATION = "${location}";
    String ET_CREATOR = "${creator}";
    String ET_URL = "${url}";
    String ET_DATE = "${date}";
    String ET_PRIORITY = "${priority}";
    String ET_STATUS = "${status}";
    String ET_USER_NAME = "${username}";
    String ET_RELATED_TO = "${relatedto}";
    String ET_REPORTED_BY = "${reportedby}";
    String ET_PERIOD = "${period}";
    String ET_ITEMS = "${items}";
    String ET_CONTACT_TELEGRAM = "${contact_telegram}";
    String ET_EMPLOYEE_TELEGRAM = "${employee_telegram}";
    String ET_LEAD_TELEGRAM = "${lead_telegram}";
    String ET_OWNER_TELEGRAM = "${owner_telegram}";
    String ET_ASSIGNEE_TELEGRAM = "${assignee_telegram}";
    String ET_ASSIGNEE_TO_TELEGRAM = "${assignee_to_telegram}";
    String ET_MANAGER_TELEGRAM = "${manager_telegram}";
    String ET_RESOLVER_TELEGRAM = "${resolver_telegram}";
    String ET_APPROVER_TELEGRAM = "${approver_telegram}";
    String ET_SHARE_TELEGRAM = "${share_telegram}";
    String ET_REQUESTER_TELEGRAM = "${requester_telegram}";
    String ET_CREATED_BY_TELEGRAM = "${created_by_telegram}";
    String ET_UPDATED_BY_TELEGRAM = "${updated_by_telegram}";
    String ET_INVOLVED_EMPLOYEES_TELEGRAM = "${involved_employees_telegram}";
    String ET_BACKUP_ASSIGNEE_TELEGRAM = "${backup_assignee_telegram}";
    String ET_BACKUP_MANAGER_TELEGRAM = "${backup_manager_telegram}";
    String ET_CANDIDATE_TELEGRAM = "${candidate_telegram}";
    String ET_FIRST_APPROVER_TELEGRAM = "${first_approver_telegram}";
    String ET_SECOND_APPROVER_TELEGRAM = "${second_approver_telegram}";
    String ET_THIRD_APPROVER_TELEGRAM = "${third_approver_telegram}";
    String ET_FOURTH_APPROVER_TELEGRAM = "${fourth_approver_telegram}";
    String ET_FIFTH_APPROVER_TELEGRAM = "${fifth_approver_telegram}";
    String ET_LAST_APPROVER_TELEGRAM = "${last_approver_telegram}";
    String ET_CURRENT_APPROVER_TELEGRAM = "${current_approver_telegram}";
    String ET_NEXT_APPROVER_TELEGRAM = "${next_approver_telegram}";
    String ET_PREVIOUS_APPROVER_TELEGRAM = "${previous_approver_telegram}";
    String ET_PROJECT_MANAGER_TELEGRAM = "${project_manager_telegram}";
    String OBJECT_CLASS = "${object_class}";

    /// ////////////////////// INVOICE RELATED /////////////////////////
    String ET_CUSTOMER = "${customer}";
    String ET_SUPPLIER = "${supplier}";
    String ET_NUMBER = "${invoice_number}";
    String ET_QUOTE_VIEW_LINK = "%CUSTOM%viewUrlForQuote%CUSTOM%";
    String ET_QUOTE_HAS_ACCESS_LINK = "${hasaccesslink}";
    String ET_TOTAL_AMOUNT = "${total_amount}";
    String ET_PAID_AMOUNT = "${paid_amount}";
    String ET_SUPPLIER_BALANCE = "${supplierBalance}";
    String ET_CUSTOMER_BALANCE = "${customerBalance}";
    String ET_TRANSACTION_ID = "${transactionId}";
    String ET_CUSTOMER_TELEGRAM = "${customer_telegram}";
    String ET_CUSTOMER_PHONE = "${customer_phone}";
    String ET_SUPPLIER_TELEGRAM = "${supplier_telegram}";
    String ET_SUPPLIER_INTEGRATION_ID = "${supplier_integration_id}";
    String ET_ACCOUNT_TELEGRAM = "${account_telegram}";

    /// ////////////////////// EXPENSE CLAIM RELATED /////////////////////////

    String ET_EXPENSE_REPORTER_FIRST_NAME = "${reporterfirstname}";
    String ET_EXPENSE_REPORTER_LAST_NAME = "${reporterlastname}";
    String ET_EXPENSE_APPROVER_FIRST_NAME = "${approverfirstname}";
    String ET_EXPENSE_APPROVER_LAST_NAME = "${approverlastname}";
    String ET_EXPENSE_REPORT_TITLE = "${reporttitle}";
    String ET_EXPENSE_REPORT_AMOUNT = "${expenseamount}";

    /// ////////////////////// BATCH PAYMENT RELATED ////////////////////////

    String ET_PAYMENT_APPROVER_FIRST_NAME = "${approverfirstname}";
    String ET_PAYMENT_APPROVER_LAST_NAME = "${approverlastname}";
    String ET_PAYMENT_APPROVER_EMAIL = "${email}";
    String ET_PAYMENT_COMPANY_NAME = "${paymentcompanyname}";
    String ET_PAYMENT_ACCOUNT_NAME = "${paymentaccountname}";
    String ET_PAYMENT_DATE = "${paymentdate}";
    String ET_PAYMENT_AMOUNT = "${paymentamount}";
    String ET_PAYMENT_TYPE = "${paymenttype}";
    String ET_PAYMENT_LINK = "${paymentlink}";
    String ET_PAYMENT_NUMBER = "${paymentnumber}";
    String ET_PAYMENT_PRODUCT = "${paymentproduct}";
    String ET_PAYMENT_CUSTOMER = "${paymentcustomer}";

    String PAYMENT_DATE = "${payment_date}";
    String PAYMENT_TYPE = "${payment_type}";
    String PAYMENT_AMOUNT = "${amount}";
    String PAYMENT_ACCOUNT = "${account}";
    String PAYMENT_DEPARTMENT = "${department}";

    String ET_EXPENSE_HOST = "${host}";
    String ET_LINK = "${link}";
    String ET_EXPENSE_SHORT_LINK = "${reportlink}";
    String ET_EXPENSE_USERURL = "${userurl}";

    String ET_EXPENSE_SUBMIT_DATE = "${submitdate}";
    String ET_EXPENSE_RESUBMIT_DATE = "${resubmitdate}";
    String ET_EXPENSE_DECLINE_DATE = "$(declinedate)";

    /// ////////////////////// CASE REPLIED RELATED /////////////////////////

    String ET_CASE_NUMBER = "${case_number}";
    String ET_CASE_SUBJECT = "${subject}";
    String ET_DESCRIPTION = "${description}";
    String ET_CASE_TYPE = "${type}";
    String ET_CASE_ORIGIN = "${case_origin}";
    String ET_CASE_REASON = "${case_reason}";
    String ET_CASE_ASSIGNEE = "${assignee}";
    String CLICK_LINK = "${click_link}";
    String PAYME_LINK = "${payme_link}";
    String PAYPAL_LINK = "${paypal_link}";
    String REVOLUT_LINK = "${revolut_link}";


    String ET_CASE_REPORTER = "${reporter}";
    String ET_CASE_REPLIER_FIRST_NAME = "${replierfirstname}";
    String ET_CASE_REPLIER_LAST_NAME = "${replierlastname}";

    String ET_CASE_REPLIER_COMPANY = "${repliercompany}";
    String ET_CASE_REPLIER_EMAIL = "${replieremail}";

    /// ////////////////////// CALENDAR EVENTS RELATED /////////////////////////
    String ET_WHEN = "${when}";
    String ET_SHARED_EMPLOYEES = "${sharedemployees}";
    String ET_GUESTS = "${guests}";

    /// ////////////////////// TASK/PROJECT RELATED /////////////////////////
    String ET_TASK_NUMBER = "${tasknumber}";
    String ET_TASK_NAME = "${taskname}";
    String ET_ISSUE_NUMBER = "${issuenumber}";
    String ET_ISSUE_NAME = "${issuename}";
    String ET_RESOLVER_NAME = "${resolvername}";
    String ET_PROJECT_NUMBER = "${projectnumber}";
    String ET_PROJECT_NAME = "${projectname}";
    String ET_COMPLETED = "${completed}";
    String ET_ASSIGNEES = "${assignees}";
    String ET_ESTIMATED_TIME = "${estimatedtime}";
    String ET_MANAGER_NAME = "${managername}";
    String ET_TASKS = "${tasks}";

    /// ////////////////////// MESSAGE CENTER(OTHER) RELATED /////////////////////////
    String ET_RECIPIENT_TITLE = "${recipienttitle}";
    String ET_RECIPIENT_FULL_NAME = "${recipientfullname}";
    String ET_RECIPIENT_FIRST_NAME = "${recipientfirstname}";
    String ET_RECIPIENT_LAST_NAME = "${recipientlastname}";
    String ET_RECIPIENT_EMAIL = "${recipientemail}";
    String ET_RECIPIENT_COMPANY_NAME = "${recipientcompanyname}";
    String ET_RECIPIENT_PHONE = "${recipientphone}";
    String ET_RECIPIENT_MOBILE = "${recipientmobile}";
    String ET_SENDER_TITLE = "${sendertitle}";
    String ET_SENDER_FIRST_NAME = "${senderfirstname}";
    String ET_SENDER_LAST_NAME = "${senderlastname}";
    String ET_SENDER_EMAIL = "${senderemail}";
    String ET_SENDER_COMPANY_NAME = "${sendercompanyname}";
    String ET_SENDER_MOBILE_PHONE = "${sendermobilephone}";
    String ET_SENDER_PRIMARY_PHONE = "${senderprimaryphone}";
    String ET_SENDER_JOB_TITLE = "${senderjobtitle}";

    /// //////////////////////  GOAL /////////////////////////////////
    String ET_VALIDITY_PERIOD = "${validityPeriod}";

    /// ////////////////////// LEAVE REQUEST RELATED /////////////////////////////////
    String ET_REQUEST_EMPLOYEE_FULLNAME = "${employeeFullName}";
    String ET_SENDER_EMPLOYEE_FULLNAME = "${creatorFullName}";
    String ET_REQUEST_APPROVER_FULLNAME = "${managerFullName}";
    String ET_RECEIVER_FULL_NAME = "${receiverFullName}";
    String ET_QUEST_TYPE = "${requestType}";
    String ET_REQUEST_REASON_NAME = "${reason}";
    String ET_REQUEST_OTHEER_REASON = "${otherReason}";
    String ET_REQUEST_DESCRIPTION = "${description}";
    String ET_CONFIRMDATE = "${confirmdate}";
    String ET_COMPANY_INFO = "${companyInfo}";
    String ET_PRODUCT_NAME = "${productName}";
    String ET_ACCESS_TYPE = "${accessType}";
    String ET_SUBJECT = "${subject}";

    /// /////////////////    EMPLOYEE ACTIVATION REALATED ////////////////////////////////
    String ET_EMPLOYEE_USER_FULLNAME = "${fullname}";
    String ET_EMPLOYEE_USER_NAME = "${username}";
    String ET_EMPLOYEE_USER_PASSWORD = "${password}";
    String ET_ACTIVATION_LINK = "${activationlink}";
    String ET_LOGIN_PAGE_LINK = "${loginpagelink}";

    /// /////////////////    Sent Message Related         ////////////////////////////////
    String ET_TITLE = "${title}";
    String ET_MOBILE = "${mobile}";
    String ET_WEEK_MONTH_NUMBER = "${weekmonthnumber}";

    /// /////////////////    SMS        //////////////////////
    String ET_RECEIVE_FULL_NAME = "${receiveFullName}";
    String ET_EMPLOYEE = "${employee}";
    String ET_SENDER = "${sender}";

    /// ////////////////// DOCUMENT UPLOAD RELATED ////////////////////////////////
    String ET_FILE_NAME = "${fileName}";

    /// ////////////////// HR REMINDER ITEMS ////////////////////////////////
    String REMINDER_TYPE = "${remindertype}";
    String REMINDER_FIELD_VALUE = "${reminderfieldvalue}";
    String REMINDER_EMPLOYEES = "${reminderemployees}";


    String USER = "${user}";
    String DATE_CURRENT = "${currentDate}";
    String ET_SIGNATURE = "${signature}";
    String ET_APPROVERS = "${approvers}";
    String EXTENSION_PHONE = "${extension_phone}";

    String CUSTOM_FIELD_PREFIX = "_CUSTOM_FIELD_PREFIX";

    String ID = "${id}";
    String INTEGRATION_ID = "INTEGRATION_ID";
    String OBJECT_KEY = "${object_key}";

    interface HRMS {
        String PREV_APPROVER = "${prev_approver}";
        String PREV_APPROVER_EMAIL = "${prev_approver_email}";
        String PREV_APPROVER_STATUS = "${prev_approver_status}";
        String CURRENT_APPROVER = "${current_approver}";
        String CURRENT_APPROVER_EMAIL = "${current_approver_email}";
        String CURRENT_APPROVER_STATUS = "${current_approver_status}";
        String NEXT_APPROVER = "${next_approver}";
        String NEXT_APPROVER_EMAIL = "${next_approver_email}";
        String NEXT_APPROVER_STATUS = "${next_approver_status}";
        String EMPLOYEE_CODE = "${employee_code}";
        String SUBMITTED_DATE = "${submitted_date}";
        String UNIQUE_ID = "${unique_id}";
        String PARENT_UNIQUE_ID = "${parent_unique_id}";
        String ENGLISH_NAME = "${english_name}";
        String RUSSIAN_NAME = "${russian_name}";
        String UZBEK_NAME = "${uzbek_name}";
        String ARABIC_NAME = "${arabic_name}";

        interface _ROTATION {
            String ROTATION_ID = "${id}";
            String CURRENT_USER = "${current_user}";
            SelectItem[] ROTATION_ATTRIBUTES = {
                    new SelectItem(0, "Id", ROTATION_ID),
                    new SelectItem(1, "Current User", CURRENT_USER)
            };
        }

        interface LEAVE_REQUEST {
            String LEAVE_REQUEST_APPROVE_LINK = "${approve_link}";
            String LEAVE_REQUEST_APPROVE_LINK_WITH_ACCESS = "${approve_link_with_access}";
            String RELATED_EMPLOYEE_NAME = "${related_employee_name}";
            String RELATED_EMPLOYEE_ID = "${related_employee_id}";
            String RELATED_EMPLOYEE_EMAIL = "${related_employee_email}";
            String RELATED_EMPLOYEE_PHONE = "${related_employee_phone}";
            String RELATED_EMPLOYEE_CODE = "${related_employee_code}";
            String RELATED_EMPLOYEE_DEPARTMENT_RUSSIAN = "${related_employee_department_russian}";
            String RELATED_EMPLOYEE_DEPARTMENT_ENGLISH = "${related_employee_department_english}";
            String RELATED_EMPLOYEE_DEPARTMENT_UZBEK = "${related_employee_department_uzbek}";
            String RELATED_EMPLOYEE_DEPARTMENT_ARABIC = "${related_employee_department_arabic}";
            String CREATED_DATE = "${created_date}";
            String DATE_PERIOD = "${date_period}";
            String PREV_APPROVER_PHONE = "${prev_approver_phone}";
            String CURRENT_APPROVER_PHONE = "${current_approver_phone}";
            String NEXT_APPROVER_PHONE = "${next_approver_phone}";
            String SUPERVISOR_NAME = "${supervisor_name}";
            String SUPERVISOR_EMAIL = "${supervisor_email}";
            String SUPERVISOR_PHONE = "${supervisor_phone}";
            String CREATOR_NAME = "${creator_name}";
            String CREATOR_EMAIL = "${creator_email}";
            String CREATOR_PHONE = "${creator_phone}";
            String ET_LINK_WITH_ACCESS = "${link_with_access}";
            String REJECTION_REASON = "${rejection_reason}";
            String REASON_CODE = "${reason_code}";
            String CURRENT_USER_USERNAME = "${current_user_username}";
            SelectItem[] ATTRIBUTES = {
                    new SelectItem(0, "Summary Link", ET_LINK),
                    new SelectItem(1, "Approve Link", LEAVE_REQUEST_APPROVE_LINK),
                    new SelectItem(2, "Related Employee Name", RELATED_EMPLOYEE_NAME),
                    new SelectItem(3, "Related Employee Email", RELATED_EMPLOYEE_EMAIL),
                    new SelectItem(4, "Related Employee Phone", RELATED_EMPLOYEE_PHONE),
                    new SelectItem(5, "Previous Approver Name", PREV_APPROVER),
                    new SelectItem(6, "Previous Approver Email", PREV_APPROVER_EMAIL),
                    new SelectItem(7, "Previous Approver Phone", PREV_APPROVER_PHONE),
                    new SelectItem(8, "Current Approver Name", CURRENT_APPROVER),
                    new SelectItem(9, "Current Approver Email", CURRENT_APPROVER_EMAIL),
                    new SelectItem(10, "Current Approver Phone", CURRENT_APPROVER_PHONE),
                    new SelectItem(11, "Next Approver Name", NEXT_APPROVER),
                    new SelectItem(12, "Next Approver Email", NEXT_APPROVER_EMAIL),
                    new SelectItem(13, "Next Approver Phone", NEXT_APPROVER_PHONE),
                    new SelectItem(14, "Employee Supervisor Name", SUPERVISOR_NAME),
                    new SelectItem(15, "Employee Supervisor Email", SUPERVISOR_EMAIL),
                    new SelectItem(16, "Employee Supervisor Phone", SUPERVISOR_PHONE),
                    new SelectItem(17, "Date Period", DATE_PERIOD),
                    new SelectItem(18, "Employee Telegram", ET_EMPLOYEE_TELEGRAM),
                    new SelectItem(19, "Previous Approver Telegram", ET_PREVIOUS_APPROVER_TELEGRAM),
                    new SelectItem(20, "Current Approver Telegram", ET_CURRENT_APPROVER_TELEGRAM),
                    new SelectItem(21, "Next Approver Telegram", ET_NEXT_APPROVER_TELEGRAM),
                    new SelectItem(22, "Supervisor Name", SUPERVISOR_NAME),
                    new SelectItem(23, "Supervisor Email", SUPERVISOR_EMAIL),
                    new SelectItem(24, "Supervisor Phone", SUPERVISOR_PHONE),
                    new SelectItem(25, "Creator Name", CREATOR_NAME),
                    new SelectItem(26, "Creator Email", CREATOR_EMAIL),
                    new SelectItem(27, "Creator Phone", CREATOR_PHONE),
                    new SelectItem(28, "Summary Link with access", ET_LINK_WITH_ACCESS),
                    new SelectItem(29, "Rejection Reason", REJECTION_REASON),
                    new SelectItem(30, "Approve Link with access", LEAVE_REQUEST_APPROVE_LINK_WITH_ACCESS),
                    new SelectItem(31, "ID", ID),
                    new SelectItem(32, "Reason Code", REASON_CODE),
                    new SelectItem(33, "Current User Username", CURRENT_USER_USERNAME),
                    new SelectItem(34, "Related Employee ID", RELATED_EMPLOYEE_ID),
                    new SelectItem(35, "Related Employee Department Russian", RELATED_EMPLOYEE_DEPARTMENT_RUSSIAN),
                    new SelectItem(36, "Related Employee Department English", RELATED_EMPLOYEE_DEPARTMENT_ENGLISH),
                    new SelectItem(37, "Related Employee Department Uzbek", RELATED_EMPLOYEE_DEPARTMENT_UZBEK),
                    new SelectItem(38, "Related Employee Department Arabic", RELATED_EMPLOYEE_DEPARTMENT_ARABIC),
                    new SelectItem(39, "Device Token", DEVICE_TOKEN)
            };
        }

        interface EMPLOYEE_STEP {
            SelectItem[] ATTRIBUTES = {
                    new SelectItem(0, "Employee Email", ET_EMAIL),
                    new SelectItem(1, "Employee Code", EMPLOYEE_CODE),
                    new SelectItem(4, "First Approver Email", PAYROLL.FIRST_APPROVER_EMAIL),
                    new SelectItem(5, "Second Approver Email", PAYROLL.SECOND_APPROVER_EMAIL),
                    new SelectItem(6, "Third Approver Email", PAYROLL.THIRD_APPROVER_EMAIL),
                    new SelectItem(7, "Fourth Approver Email", PAYROLL.FOURTH_APPROVER_EMAIL),
                    new SelectItem(8, "Fifth Approver Email", PAYROLL.FIFTH_APPROVER_EMAIL),
                    new SelectItem(9, "Link", ET_LINK),
                    new SelectItem(10, "Created Date", ACCOUNTING.CREATED_DATE),
                    new SelectItem(11, "Previous Approver", PREV_APPROVER),
                    new SelectItem(12, "Previous Approver Email", PREV_APPROVER_EMAIL),
                    new SelectItem(13, "Previous Approver Status", PREV_APPROVER_STATUS),
                    new SelectItem(14, "Current Approver", CURRENT_APPROVER),
                    new SelectItem(15, "Current Approver Email", CURRENT_APPROVER_EMAIL),
                    new SelectItem(16, "Current Approver Status", CURRENT_APPROVER_STATUS),
                    new SelectItem(17, "Next Approver", NEXT_APPROVER),
                    new SelectItem(18, "Next Approver Email", NEXT_APPROVER_EMAIL),
                    new SelectItem(19, "Next Approver Status", NEXT_APPROVER_STATUS),
                    new SelectItem(20, "Employee code", EMPLOYEE_CODE)
            };
        }

        interface EMPLOYEE_CERTIFICATE {
            String CERTIFICATE_TYPE = "${certificate_type}";
            String REJECTION_NOTE = "${rejection_note}";
            SelectItem[] ATTRIBUTES = {
                    new SelectItem(0, "Approvers", ET_APPROVERS),
                    new SelectItem(1, "Employee Email", ET_EMAIL),
                    new SelectItem(2, "Employee Code", EMPLOYEE_CODE),
                    new SelectItem(3, "Creator Email", CRM.EVENT.CREATOR_EMAIL),
                    new SelectItem(4, "Creator Name", PENALTIES_PROMOTIONS.PP_CREATOR_NAME),
                    new SelectItem(5, "Rejection Note", REJECTION_NOTE),
                    new SelectItem(6, "Link", ET_LINK)
            };
        }

        interface CANDIDATE {
            String PROJECT_NUMBER = "${candidate_project_number}";
            String MATCHED_VACANCIES = "${candidate_matched_vacancies}";
            String ATTACHMENTS = "${attachments}";
            String VACANCY_MANAGER = "${vacancy_manager}";
            String VACANCY_MANAGER_EMAIL = "${vacancy_manager_email}";
            String VACANCY_MANAGER_TELEGRAM = "${vacancy_manager_telegram}";
            String VACANCY_MANAGER_PHONE = "${vacancy_manager_phone}";
            String MIDDLE_NAME = "${middle_name}";
            SelectItem[] ATTRIBUTES = {
                    new SelectItem(0, "Project Number", PROJECT_NUMBER),
                    new SelectItem(1, "Matched Vacancies", MATCHED_VACANCIES),
                    new SelectItem(2, "Owner Telegram", ET_OWNER_TELEGRAM),
                    new SelectItem(3, "Vacancy Manager", VACANCY_MANAGER),
                    new SelectItem(4, "Vacancy Manager Email", VACANCY_MANAGER_EMAIL),
                    new SelectItem(5, "Vacancy Manager Telegram", VACANCY_MANAGER_TELEGRAM),
                    new SelectItem(6, "Vacancy Manager Phone", VACANCY_MANAGER_PHONE),
                    new SelectItem(7, "Object Key", OBJECT_KEY),
                    new SelectItem(8, "Middle Name", MIDDLE_NAME)
            };
        }

        interface PLACCEMENT {
            String CURRENT_USER = "${current_user}";
            String CANDIDATE_EMAIL = "${candidate_email}";
            SelectItem[] ATTRIBUTES = {
                    new SelectItem(0, "Id", ID),
                    new SelectItem(1, "Current user", CURRENT_USER),
                    new SelectItem(2, "Candidate email", CANDIDATE_EMAIL)
            };
        }

        interface EMPLOYEE {
            String AGENT_ID = "${agent_id}";
            String BANK_NAME = "${bank_name}";
            String ACCOUNT_NUMBER = "${account_number}";
            String ACCOUNT_NAME = "${account_name}";
            String BANK_ADDRESS = "${bank_address}";
            String SWIFT_CODE = "${swift_code}";
            String SORT_CODE = "${sort_code}";
            String IBAN_CODE = "${iban_code}";
            String LOGIN_LINK_WITH_ACCESS = "${login_link_with_access}";
            String SUPERVISOR_LOGIN_LINK_WITH_ACCESS = "${supervisor_login_link_with_access}";
            String ADDITIONAL_PAYMENT_LINK_WITH_ACCESS = "${additional_payment_link_with_access}";
            String SUPERVISOR_EMAIL = "${supervisor_email}";
            String SUPERVISOR_PHONE = "${supervisor_phone}";
            String PHOTO_BYTES = "${photo_bytes}";
            String PHOTO_EXTENSION = "${photo_extension}";
            String CURRENT_USER = "${current_user}";
            String USERNAME = "${username}";
            String DEPARTMENT_UNIQUE_ID = "${department_unique_id}";
            String POSITION_UNIQUE_ID = "${position_unique_id}";
            String POSITION_ID = "${position_id}";
            String UUID = "${uuid}";
            String CURRENT_DATE = "${current_date}";
            String PLACEMENT_ID = "${placement_id}";
            String FINGERPRINT_DEVICE_UUIDS = "${fingerprint_device_uuids}";
            SelectItem[] ATTRIBUTES = {
                    new SelectItem(0, "Modifier Name", PROJECT.MODIFIER_NAME),
                    new SelectItem(1, "Modifier Email", PROJECT.MODIFIER_EMAIL),
                    new SelectItem(2, "Link", ET_LINK),
                    new SelectItem(3, "Login Link With Access", LOGIN_LINK_WITH_ACCESS),
                    new SelectItem(4, "Supervisor Email", SUPERVISOR_EMAIL),
                    new SelectItem(5, "Supervisor Phone", SUPERVISOR_PHONE),
                    new SelectItem(6, "Supervisor Login Link With Access", SUPERVISOR_LOGIN_LINK_WITH_ACCESS),
                    new SelectItem(7, "Photo Bytes", PHOTO_BYTES),
                    new SelectItem(8, "Photo Extension", PHOTO_EXTENSION),
                    new SelectItem(9, "Additional Payment Link With Access", ADDITIONAL_PAYMENT_LINK_WITH_ACCESS),
                    new SelectItem(10, "Current User", CURRENT_USER),
                    new SelectItem(11, "Username", USERNAME),
                    new SelectItem(12, "Department Unique Id", DEPARTMENT_UNIQUE_ID),
                    new SelectItem(13, "Position Unique Id", POSITION_UNIQUE_ID),
                    new SelectItem(14, "Extension Phone Number", EXTENSION_PHONE),
                    new SelectItem(15, "UUID", UUID),
                    new SelectItem(16, "Current Date", CURRENT_DATE),
                    new SelectItem(17, "Placement ID", PLACEMENT_ID),
                    new SelectItem(18, "ID", ID),
                    new SelectItem(19, "Position ID", POSITION_ID),
                    new SelectItem(20, "Fingerprint Device Uuids", FINGERPRINT_DEVICE_UUIDS)
            };
        }

        interface DEPARTMENT {
            SelectItem[] ATTRIBUTES = {
                    new SelectItem(0, "Unique ID", UNIQUE_ID),
                    new SelectItem(1, "Parent Unique ID", PARENT_UNIQUE_ID),
                    new SelectItem(2, "English Name", ENGLISH_NAME),
                    new SelectItem(3, "Russian Name", RUSSIAN_NAME),
                    new SelectItem(4, "Uzbek Name", UZBEK_NAME),
                    new SelectItem(5, "Arabic Name", ARABIC_NAME),
            };
        }

        interface POSITION {
            SelectItem[] ATTRIBUTES = {
                    new SelectItem(0, "Unique ID", UNIQUE_ID),
                    new SelectItem(2, "English Name", ENGLISH_NAME),
                    new SelectItem(3, "Russian Name", RUSSIAN_NAME),
                    new SelectItem(4, "Uzbek Name", UZBEK_NAME),
                    new SelectItem(5, "Arabic Name", ARABIC_NAME),
                    new SelectItem(6, "ID", ID),
            };
        }

        interface INCIDENT {
            String RELATED_EMPLOYEE_EMAIL = "${related_employee_email}";
            String REPORTED_BY_EMAIL = "${reported_by_email}";
            String RELATED_EMPLOYEE_PHONE = "${related_employee_phone}";
            String REPORTED_BY_PHONE = "${reported_by_phone}";
            String START_DATE = "${start_date}";
            String END_DATE = "${end_date}";
            SelectItem[] ATTRIBUTES = {
                    new SelectItem(0, "Related Employee Email", RELATED_EMPLOYEE_EMAIL),
                    new SelectItem(1, "Reported By Email", REPORTED_BY_EMAIL),
                    new SelectItem(2, "Related Employee Phone", RELATED_EMPLOYEE_PHONE),
                    new SelectItem(3, "Reported By Phone", REPORTED_BY_PHONE),
                    new SelectItem(4, "Start Date", START_DATE),
                    new SelectItem(5, "End Date", END_DATE)
            };
        }

        interface SHIFT {
            SelectItem[] ATTRIBUTES = {
                    new SelectItem(0, "ID", ID),
                    new SelectItem(1, "Current User", CURRENT_USER)
            };
        }

        interface VACANCY {
            String HH_ACCESS_TOKEN = "${hh_access_token}";
            String NAME_EN = "${name_en}";
            String NAME_RU = "${name_ru}";
            String NAME_AR = "${name_ar}";
            String NAME_UZ = "${name_uz}";
            String LOCATION_ID = "${location_id}";
            String LOCATION_UZ = "${location_uz}";
            String LOCATION_RU = "${location_ru}";
            String LOCATION_EN = "${location_en}";
            String BRANCH_ID = "${branch_id}";
            String BRANCH_UZ = "${branch_uz}";
            String BRANCH_RU = "${branch_ru}";
            String BRANCH_EN = "${branch_en}";
            String DESCRIPTION_UZ = "${description_uz}";
            String DESCRIPTION_EN = "${description_en}";
            String DESCRIPTION_RU = "${description_ru}";
            String REQUIREMENTS_UZ = "${requirements_uz}";
            String REQUIREMENTS_EN = "${requirements_en}";
            String REQUIREMENTS_RU = "${requirements_ru}";
            String RESPONSIBILITIES_UZ = "${responsibilities_uz}";
            String RESPONSIBILITIES_EN = "${responsibilities_en}";
            String RESPONSIBILITIES_RU = "${responsibilities_ru}";
            String VACANCY_NUMBER = "${vacancy_number}";
            String VACANCY_SUMMARY_LINK = "${vacancy_summary_link}";
            String VACANCY_APPROVER = "${vacancy_approver}";
            String CODE = "${code}";
            String MANAGER_NAME = "${manager_name}";
            String MANAGER_EMAIL = "${manager_email}";
            String MANAGER_PHONE = "${manager_phone}";
            String EMPLOYMENT = "${employment}";
            String POSITION_ID = "${position_id}";
            String POSITION_NAME = "${position_name}";
            String STATUS = "${status}";
            String TIME_SLOTS = "${timeslots}";
            String CUSTOM_QUESTIONS = "${custom_questions}";
            SelectItem[] ATTRIBUTES = {
                    new SelectItem(0, "ID", ID),
                    new SelectItem(1, "HH Access Token", HH_ACCESS_TOKEN),
                    new SelectItem(2, "English Name", NAME_EN),
                    new SelectItem(3, "Russian Name", NAME_RU),
                    new SelectItem(4, "Arabic Name", NAME_AR),
                    new SelectItem(5, "Uzbek Name", NAME_UZ),
                    new SelectItem(6, "Code", CODE),
                    new SelectItem(7, "Manager Name", MANAGER_NAME),
                    new SelectItem(8, "Manager Email", MANAGER_EMAIL),
                    new SelectItem(9, "Manager Phone", MANAGER_PHONE),
                    new SelectItem(10, "Employment", EMPLOYMENT),
                    new SelectItem(11, "Vacancy Number", VACANCY_NUMBER),
                    new SelectItem(12, "Vacancy Summery Link", VACANCY_SUMMARY_LINK),
                    new SelectItem(13, "Vacancy Approver", VACANCY_APPROVER),
                    new SelectItem(14, "Location Id", LOCATION_ID),
                    new SelectItem(15, "Location Uz", LOCATION_UZ),
                    new SelectItem(16, "Location Ru", LOCATION_RU),
                    new SelectItem(17, "Location En", LOCATION_EN),
                    new SelectItem(18, "Company Id", ET_COMPANY_ID),
                    new SelectItem(19, "Description Uz", DESCRIPTION_UZ),
                    new SelectItem(20, "Description En", DESCRIPTION_EN),
                    new SelectItem(21, "Description Ru", DESCRIPTION_RU),
                    new SelectItem(22, "Requirements Uz", REQUIREMENTS_UZ),
                    new SelectItem(23, "Requirements En", REQUIREMENTS_EN),
                    new SelectItem(24, "Requirements Ru", REQUIREMENTS_RU),
                    new SelectItem(25, "Responsibilities Uz", RESPONSIBILITIES_UZ),
                    new SelectItem(26, "Responsibilities En", RESPONSIBILITIES_EN),
                    new SelectItem(27, "Responsibilities Ru", RESPONSIBILITIES_RU),
                    new SelectItem(28, "Posistion Id", POSITION_ID),
                    new SelectItem(29, "Status", STATUS),
                    new SelectItem(30, "Brnach Id", BRANCH_ID),
                    new SelectItem(31, "Branch Uz", BRANCH_UZ),
                    new SelectItem(32, "Branch Ru", BRANCH_RU),
                    new SelectItem(33, "Branch En", BRANCH_EN),
                    new SelectItem(34, "TimeSlots", TIME_SLOTS),
                    new SelectItem(35, "Posistion Name", POSITION_NAME),
                    new SelectItem(36, "Custom Questions", CUSTOM_QUESTIONS),

            };
        }

        interface EmployeeDocuments {
            String EXPIRY_DATE = "${expiryDate}";
            String EMPLOYEE_NAME = "${employeeFullName}";
            String DOCUMENT_TYPE = "${documentType}";
            SelectItem[] EMPLOYEE_DOCUMENTS_ATTRIBUTES = {
                    new SelectItem(0, "File Name", ET_FILE_NAME),
                    new SelectItem(1, "Employee Name", EMPLOYEE_NAME),
                    new SelectItem(2, "Expiry Date", EXPIRY_DATE),
                    new SelectItem(3, "Document Type", DOCUMENT_TYPE),
            };
        }
    }

    interface PROJECT {
        String MANAGER_EMAIL = "${manager_email}";
        String BACKUP_MANAGER = "${backup_manager}";
        String BACKUP_MANAGERS_EMAILS = "${backup_managers_emails}";
        String INVOLVED_EMPLOYEES_EMAILS = "${involved_employees_emails}";
        String INVOLVED_EMPLOYEES_NAMES = "${involved_employees_names}";
        String CLIENTS_EMAILS = "${clients_emails}";
        String CLIENT = "${client}";
        String MODIFIER_NAME = "${modifier_name}";
        String MODIFIER_EMAIL = "${modifier_email}";
        SelectItem[] ATTRIBUTES = {
                new SelectItem(0, "Manager email", MANAGER_EMAIL),
                new SelectItem(1, "Backup manager(s) email(s)", BACKUP_MANAGERS_EMAILS),
                new SelectItem(2, "Involved employee(s) name(s)", INVOLVED_EMPLOYEES_NAMES),
                new SelectItem(3, "Involved employee(s) email(s)", INVOLVED_EMPLOYEES_EMAILS),
                new SelectItem(4, "Client(s) email(s)", CLIENTS_EMAILS),
                new SelectItem(5, "Creator Email", CRM.EVENT.CREATOR_EMAIL),
                new SelectItem(6, "Creator Name", PENALTIES_PROMOTIONS.PP_CREATOR_NAME),
                new SelectItem(7, "Modifier Name", MODIFIER_NAME),
                new SelectItem(8, "Modifier Email", MODIFIER_EMAIL),
                new SelectItem(9, "Project link", ET_LINK),
                new SelectItem(10, "Company name", ET_COMPANY_NAME),
                new SelectItem(11, "Customer Telegram", ET_CUSTOMER_TELEGRAM),
                new SelectItem(12, "Involved Employees Telegram", ET_INVOLVED_EMPLOYEES_TELEGRAM),
                new SelectItem(13, "Manager Telegram", ET_MANAGER_TELEGRAM),
                new SelectItem(14, "Backup Manager Telegram", ET_BACKUP_MANAGER_TELEGRAM),
                new SelectItem(15, "Created By Telegram", ET_CREATED_BY_TELEGRAM),
                new SelectItem(16, "Updated By Telegram", ET_UPDATED_BY_TELEGRAM)
        };
    }

    interface TASK {
        String COMPLETED = "${completed}";
        String ASSIGNEES = "${assignees}";
        String ASSIGNEES_EMAILS = "${assignees_emails}";
        String PROJECT_CLIENT = "${project_client}";
        String PROJECT_CLIENT_EMAIL = "${project_client_email}";
        String PROJECT_MANAGER = "${project_manager}";
        String PROJECT_MANAGER_EMAIL = "${project_manager_email}";
        String MODIFIER_NAME = "${modifier_name}";
        String MODIFIER_EMAIL = "${modifier_email}";
        String BACKUP_MANAGERS = "${backup_managers}";
        String BACKUP_MANAGER_EMAILS = "${backup_manager_emails}";
        String DURATION = "${duration}";
        String DIRECTORS = "${directors}";
        String DIRECTORS_EMAILS = "${directors_emails}";
        String CLIENTS = "${clients}";
        String PREDECESSOR_TASKS = "${predecessor_tasks}";
        String SUCCESSOR_TASKS = "${successor_tasks}";
        String WORKSTREAM = "${workstream}";
        String ESTIMATED_TIME = "${estimated_time}";
        String DEVICE_TOKEN = "${device_token}";
        String FIREBASE_ASSERTION_TOKEN = "${firebase_assertion_token}";
        String FIREBASE_OAUTH2_TOKEN = "${firebase_oauth2_token}";
        String PROJECT_NUMBER = "${project_number}";
        String PROJECT_ID = "${project_id}";
        String TASK_NUMBER = "${task_number}";
        String TASK_NAME = "${task_name}";
        String TASK_DESCRIPTION = "${task_description}";
        String TASK_DUE_DATE = "${task_due_date}";
        String INDIVIDUAL_ASSIGNEES = "${individual_assignees}";
        SelectItem[] ATTRIBUTES = {
                new SelectItem(1, "Completed", COMPLETED),
                new SelectItem(5, "Creator Email", CRM.EVENT.CREATOR_EMAIL),
                new SelectItem(6, "Creator Name", PENALTIES_PROMOTIONS.PP_CREATOR_NAME),
                new SelectItem(9, "Assignees", ASSIGNEES),
                new SelectItem(10, "Assignees Emails", ASSIGNEES_EMAILS),
                new SelectItem(11, "Project Client", PROJECT_CLIENT),
                new SelectItem(12, "Project Client Email", PROJECT_CLIENT_EMAIL),
                new SelectItem(13, "Project Manager", PROJECT_MANAGER),
                new SelectItem(14, "Project Manager Email", PROJECT_MANAGER_EMAIL),
                new SelectItem(15, "Modifier Name", MODIFIER_NAME),
                new SelectItem(16, "Modifier Email", MODIFIER_EMAIL),
                new SelectItem(17, "Clients", CLIENTS),
                new SelectItem(18, "Clients emails", EmailTemplateConstants.PROJECT.CLIENTS_EMAILS),
                new SelectItem(19, "Backup Managers", BACKUP_MANAGERS),
                new SelectItem(20, "Backup Managers Emails", BACKUP_MANAGER_EMAILS),
                new SelectItem(21, "Directors", DIRECTORS),
                new SelectItem(22, "Directors Emails", DIRECTORS_EMAILS),
                new SelectItem(23, "Predecessor Tasks", PREDECESSOR_TASKS),
                new SelectItem(24, "Successor Tasks", SUCCESSOR_TASKS),
                new SelectItem(25, "Estimated Time", ESTIMATED_TIME),
                new SelectItem(26, "Task link", ET_LINK),
                new SelectItem(27, "Assignee Telegram", ET_ASSIGNEE_TELEGRAM),
                new SelectItem(28, "Project Manager Telegram", ET_PROJECT_MANAGER_TELEGRAM),
                new SelectItem(29, "Device Token", DEVICE_TOKEN),
                new SelectItem(30, "Projact number", PROJECT_NUMBER),
                new SelectItem(31, "Firebase Assertion Token", FIREBASE_ASSERTION_TOKEN),
                new SelectItem(32, "Firebase OAuth2 Token", FIREBASE_OAUTH2_TOKEN),
                new SelectItem(33, "Id", ID),
                new SelectItem(34, "Project Id", PROJECT_ID),
                new SelectItem(35, "Duration", DURATION),
                new SelectItem(36, "Task Number", TASK_NUMBER),
                new SelectItem(37, "Task Name", TASK_NAME),
                new SelectItem(38, "Task Descripton", TASK_DESCRIPTION),
                new SelectItem(39, "Task Due Date", TASK_DUE_DATE),
                new SelectItem(40, "Individual Assignees", INDIVIDUAL_ASSIGNEES)
        };
    }

    interface CRM {
        String ASSIGNEE = "${assignee}";
        String RESOLVER = "${resover}";
        String TYPE = "${type}";
        String CRM_ACCOUNT = "${crmAccount}";
        String CONTACT = "${contact}";
        String LEAD = "${lead}";
        String CASE = "${case}";
        String OWNER_EMAIL = "${owner_email}";
        String ASSIGNEE_EMAIL = "${assignee_email}";
        String RESOLVER_EMAIL = "${resolver_email}";
        String CAMPAIGN = "${campaign}";
        String PHONE = "${phone}";
        String MOBILE = "${mobile}";
        String CREATION_DATE = "${creation_date}";
        String MODIFIED_DATE = "${modified_date}";
        String LAST_NOTE = "${last_note}";

        interface CRM_CASE {
            String CASE_NUMBER = "${case_number}";
            String CASE_REPORTER = "${reporter}";
            String CASE_REPORTER_EMAIL = "${reporter_email}";
            String CASE_LINK = "${case_link}";
            String CASE_ACCOUNT = "${crm_account}";
            String CASE_RESOLVER = "${resolver}";
            String CASE_SUPPORT_PERSON_EMAIL = "${support_person_email}";
            String ASSIGNEE_PHONE = "${assignee_phone}";
            String RESOLVER_PHONE = "${resolver_phone}";
            String CONTACT_PHONE = "${contact_phone}";
            String LEAD_PHONE = "${lead_phone}";
            SelectItem[] CASE_ATTRIBUTES = {
                    new SelectItem(0, "Case Number", CASE_NUMBER),
                    new SelectItem(1, "Reporter", CASE_REPORTER),
                    new SelectItem(2, "Reporter Email", CASE_REPORTER_EMAIL),
                    new SelectItem(3, "Crm Account", CASE_ACCOUNT),
                    new SelectItem(4, "Assignee Email", ASSIGNEE_EMAIL),
                    new SelectItem(5, "Resolver Email", RESOLVER_EMAIL),
                    new SelectItem(6, "Creation Date", CREATION_DATE),
                    new SelectItem(7, "Modified Date", MODIFIED_DATE),
                    new SelectItem(8, "Last Note", LAST_NOTE),
                    new SelectItem(9, "Case Link", CASE_LINK),
                    new SelectItem(10, "Assignee Phone", ASSIGNEE_PHONE),
                    new SelectItem(11, "Resolver Phone", RESOLVER_PHONE),
                    new SelectItem(12, "Contact Phone", CONTACT_PHONE),
                    new SelectItem(13, "Lead Phone", LEAD_PHONE),
                    new SelectItem(14, "Resolver Telegram", ET_RESOLVER_TELEGRAM),
                    new SelectItem(15, "Assigned To Telegram", ET_ASSIGNEE_TO_TELEGRAM)
            };
        }

        interface CRM_LEAD {
            String LEAD_ADDRESS = "${address}";
            String LEAD_BACKUP_ASSIGNEE = "${backup_assignee}";
            String LEAD_CAMPAIGN_NAME = "${crm_campaign_name}";
            String LEAD_COMPANY_NAME = "${crm_account_name}";
            String LEAD_JOB_TITLE = "${job_title}";
            String LEAD_OWNER = "${lead_owner}";
            String LEAD_RATING = "${rating}";
            String LEAD_SOURCE = "${lead_source}";
            String LEAD_LINK = "${lead_link}";
            String MODIFIED_BY_FIRSTNAME = "${modified_by_firstname}";
            String MODIFIED_BY_LASTNAME = "${modified_by_lastname}";
            String MODIFIED_BY_MIDDLENAME = "${modified_by_middlename}";
            SelectItem[] LEAD_ATTRIBUTES = {
                    new SelectItem(0, "Owner Email", OWNER_EMAIL),
                    new SelectItem(1, "Assignee Email", ASSIGNEE_EMAIL),
                    new SelectItem(2, "Creation Date", CREATION_DATE),
                    new SelectItem(3, "Modified Date", MODIFIED_DATE),
                    new SelectItem(4, "Lead Email", ET_EMAIL),
                    new SelectItem(5, "Lead Phone", PHONE),
                    new SelectItem(6, "Lead Mobile", MOBILE),
                    new SelectItem(7, "Lead Link", LEAD_LINK),
                    new SelectItem(8, "Assignee Telegram", ET_ASSIGNEE_TELEGRAM),
                    new SelectItem(9, "Owner Telegram", ET_OWNER_TELEGRAM),
                    new SelectItem(10, "Backup Assignee Telegram", ET_BACKUP_ASSIGNEE_TELEGRAM),
                    new SelectItem(11, "Modified By Firstname", MODIFIED_BY_FIRSTNAME),
                    new SelectItem(12, "Modified By Lastname", MODIFIED_BY_LASTNAME),
                    new SelectItem(13, "Modified By Middlename", MODIFIED_BY_MIDDLENAME),
                    new SelectItem(14, "ID", ID)
            };
        }

        interface CRM_CONTACT {
            String CONTACT_ADDRESS = "${address}";
            String CONTACT_CAMPAIGN_NAME = "${crm_campaign_name}";
            String CONTACT_COMPANY_NAME = "${crm_account_name}";
            String CONTACT_JOB_TITLE = "${job_title}";
            String CONTACT_OWNER = "${owner}";
            String UUID = "${uuid}";
            String EVENT_START_DATE = "${start_date}";
            String EVENT_END_DATE = "${end_date}";
            String CONTACT_VACANCIES = "${vacancies}";
            String ZOOM_LINK = "${zoom}";
            SelectItem[] CONTACT_ATTRIBUTES = {
                    new SelectItem(0, "Owner Email", OWNER_EMAIL),
                    new SelectItem(1, "Creation Date", CREATION_DATE),
                    new SelectItem(2, "Modified Date", MODIFIED_DATE),
                    new SelectItem(3, "Contact Phone", PHONE),
                    new SelectItem(4, "Contact Mobile", MOBILE),
                    new SelectItem(5, "Owner Telegram", ET_OWNER_TELEGRAM),
                    new SelectItem(6, "Extension Phone", EXTENSION_PHONE),
                    new SelectItem(7, "UUID", UUID),
                    new SelectItem(8, "Event Start Date", EVENT_START_DATE),
                    new SelectItem(9, "Event End Date", EVENT_END_DATE),
                    new SelectItem(10, "Vacancies", CONTACT_VACANCIES),
                    new SelectItem(11, "Zoom Link", ZOOM_LINK),
            };
        }

        interface _CRM_ACCOUNT {
            String BANK_ACCOUNT = "${bank_account}";
            String OWNERS = "${owners}";
            String PAYMENT_METHOD = "${payment_method}";
            String TERMS_ID = "${client_invoice_term_id}";
            String TERMS_DAYS = "${term_days}";
            String TAX = "${tax}";
            String SHIPPING_ADDRESS = "${shipping_address}";
            String BILLING_ADDRESS = "${billing_address}";
            String INDUSTRY = "${crm_account_industry}";
            String BANK_DETAILS = "${bank_details}";
            String COUNTRY = "${country}";
            String STATE = "${state}";
            String CITY = "${city}";
            String POSTAL_CODE = "${postal_code}";
            String ADDRESS_NAME = "${address_name}";
            String ADDRESS_LINE = "${address_line}";
            String ADDRESS_LINE_2 = "${address_line_2}";
            String PLOT_ITENTIFICATION = "${plot_identification}";
            String CITY_SUBDIVISION = "${city_subdivision}";
            String BUILDING_NUMBER = "${building_number}";
            String TAX_NUMBER = "${tax_number}";
            String FAI_UUID = "${fai_uuid}";
            String FAI_CITY_ID = "${fai_city_id}";
            String SALES_TYPE = "${sales_type}";
            String CR_NUMBER = "${cr_number}";
            String VAT_REGISTERED = "${vat_registered}";

            SelectItem[] CRM_ACCOUNT_ATTRIBUTES = {
                    new SelectItem(0, "Object Key", OBJECT_KEY),
                    new SelectItem(1, "ID", ID),
                    new SelectItem(2, "Bank Account", BANK_ACCOUNT),
                    new SelectItem(3, "Owners", OWNERS),
                    new SelectItem(4, "Payment method", PAYMENT_METHOD),
                    new SelectItem(5, "Terms ID", TERMS_ID),
                    new SelectItem(6, "Tax", TAX),
                    new SelectItem(7, "Shipping address", SHIPPING_ADDRESS),
                    new SelectItem(8, "Billing address", BILLING_ADDRESS),
                    new SelectItem(9, "Term Days", TERMS_DAYS),
                    new SelectItem(10, "Industry", INDUSTRY),
                    new SelectItem(11, "Bank Details", BANK_DETAILS),
                    new SelectItem(12, "Country", COUNTRY),
                    new SelectItem(13, "State", STATE),
                    new SelectItem(14, "City", CITY),
                    new SelectItem(15, "Postal Code", POSTAL_CODE),
                    new SelectItem(16, "Address Name", ADDRESS_NAME),
                    new SelectItem(17, "Address Line", ADDRESS_LINE),
                    new SelectItem(18, "Address Line 2", ADDRESS_LINE_2),
                    new SelectItem(19, "Plot Identification", PLOT_ITENTIFICATION),
                    new SelectItem(20, "City Subdivision", CITY_SUBDIVISION),
                    new SelectItem(21, "Building Number", BUILDING_NUMBER),
                    new SelectItem(22, "Tax Number", TAX_NUMBER),
                    new SelectItem(23, "Fai City id", FAI_CITY_ID),
                    new SelectItem(24, "Sales Type", SALES_TYPE),
                    new SelectItem(24, "Cr Number", CR_NUMBER)
            };
        }


        interface OPPORTUNITY {
            String NUMBER = "${opportunityNumber}";
            String NAME = "${opportunityName}";
            String OPPORTUNITY_CONTACT_EMAIL = "${contact_email}";
            String OPPORTUNITY_ASSIGNEE_EMAIL = "${assignee_email}";
            String OPPORTUNITY_ASSIGNEE = "${assignee}";
            String OPPORTUNITY_BACKUP_ASSIGNEE_EMAIL = "${backup_assignee_email}";
            String OPPORTUNITY_BACKUP_ASSIGNEE = "${backup_assignee}";
            String OPPORTUNITY_BACKUP_ASSIGNEE_NUMBER = "${backup_assignee_number}";
            String OPPORTUNITY_BACKUP_ASSIGNEE_PASSPORT_NUMBER = "${backup_assignee_passport_number}";
            String OPPORTUNITY_ASSIGNEE_MOBILE_NUMBER = "${assignee_mobile_number}";
            String OPPORTUNITY_BACKUP_ASSIGNEE_MOBILE_NUMBER = "${backup_assignee_mobile_number}";
            String OPPORTUNITY_CONTACT_MOBILE_NUMBER = "${contact_mobile_number}";
            String OPPORTUNITY_REJECT_REASON = "${reject_reason}";
            String OPPORTUNITY_NOTES = "${opportunity_notes}";
            String OPPORTUNITY_ATTACHMENTS = "${opportunity_attachments}";
            String OPPORTUNITY_SUB_CATEGORIES = "${opportunity_sub_categories}";
            String OPPORTUNITY_BASE_AMOUNT = "${base_amount}";
            String OPPORTUNITY_CURRENCY = "${currency}";
            String OPPORTUNITY_STAGE = "${stage}";
            String OPPORTUNITY_CLOSE_DATE = "${close_date}";
            String OPPORTUNITY_CAMPAIGN = "${campaign}";
            String OPPORTUNITY_COUNTRY = "${country}";
            String OPPORTUNITY_CONTACT_NAME = "${contact_name}";
            String OPPORTUNITY_CUSTOMER_NAME = "${customer_name}";
            String OPPORTUNITY_CUSTOMER_EMAIL = "${customer_email}";
            String OPPORTUNITY_CUSTOMER_PHONE = "${customer_phone}";
            String OPPORTUNITY_PREVIUS_STAGE = "${previus_stage}";
            // Opportunity item name and item quantity for Telegram Workflow bot
            String OPPORTUNITY_ITEM_NAME = "${opportunity_item_name}";
            String OPPORTUNITY_ITEM_QTY = "${opportunity_item_qty}";
            String OPPORTUNITY_SUMMARY_LINK = "${opportunity_summary_link}";
            String CREATED_BY = "${created_by}";
            String MODIFIED_BY = "${modified_by}";

            SelectItem[] OPPORTUNITY_ATTRIBUTES = {
                    new SelectItem(0, "Creation Date", CREATION_DATE),
                    new SelectItem(1, "Modified Date", MODIFIED_DATE),
                    new SelectItem(2, "Assignee Email", OPPORTUNITY_ASSIGNEE_EMAIL),
                    new SelectItem(3, "Backup Assignee Email", OPPORTUNITY_BACKUP_ASSIGNEE_EMAIL),
                    new SelectItem(4, "Contact Email", OPPORTUNITY_CONTACT_EMAIL),
                    new SelectItem(5, "Assignee Mobile Number", OPPORTUNITY_ASSIGNEE_MOBILE_NUMBER),
                    new SelectItem(6, "Backup Assignee Mobile Number", OPPORTUNITY_BACKUP_ASSIGNEE_MOBILE_NUMBER),
                    new SelectItem(7, "Contact Mobile Number", OPPORTUNITY_CONTACT_MOBILE_NUMBER),
                    new SelectItem(8, "Reject Reason", OPPORTUNITY_REJECT_REASON),
                    new SelectItem(9, "Notes", OPPORTUNITY_NOTES),
                    new SelectItem(9, "Product Categories", OPPORTUNITY_SUB_CATEGORIES),
                    new SelectItem(10, "Attachments", OPPORTUNITY_ATTACHMENTS),
                    new SelectItem(11, "Base Amount", OPPORTUNITY_BASE_AMOUNT),
                    new SelectItem(12, "Assignee", OPPORTUNITY_ASSIGNEE),
                    new SelectItem(13, "Currency Code", OPPORTUNITY_CURRENCY),
                    new SelectItem(14, "Customer Telegram", ET_CUSTOMER_TELEGRAM),
                    new SelectItem(15, "Contact Telegram", ET_CONTACT_TELEGRAM),
                    new SelectItem(16, "Assignee Telegram", ET_ASSIGNEE_TELEGRAM),
                    new SelectItem(17, "Backup Assignee Telegram", ET_BACKUP_ASSIGNEE_TELEGRAM),
                    new SelectItem(18, "Opportunity Name", NAME),
                    new SelectItem(19, "Opportunity Number", NUMBER),
                    new SelectItem(20, "Opportunity Close Date", OPPORTUNITY_CLOSE_DATE),
                    new SelectItem(21, "Opportunity Campaign", OPPORTUNITY_CAMPAIGN),
                    new SelectItem(22, "Opportunity Stage", OPPORTUNITY_STAGE),
                    new SelectItem(23, "Opportunity Contact FullName", OPPORTUNITY_CONTACT_NAME),
                    new SelectItem(24, "Opportunity Customer FullName", OPPORTUNITY_CUSTOMER_NAME),
                    new SelectItem(25, "Opportunity Email", OPPORTUNITY_CUSTOMER_EMAIL),
                    new SelectItem(26, "Opportunity Customer Phone", OPPORTUNITY_CUSTOMER_PHONE),
                    new SelectItem(27, "Opportunity Item Name", OPPORTUNITY_ITEM_NAME),
                    new SelectItem(28, "Opportunity Item Quantity", OPPORTUNITY_ITEM_QTY),
                    new SelectItem(29, "ID", ID),
                    new SelectItem(30, "Opportunity Backup Assignee Number", OPPORTUNITY_BACKUP_ASSIGNEE_NUMBER),
                    new SelectItem(31, "Opportunity Backup Assignee Passport Number", OPPORTUNITY_BACKUP_ASSIGNEE_PASSPORT_NUMBER),
                    new SelectItem(32, "Previus Stage", OPPORTUNITY_PREVIUS_STAGE),
                    new SelectItem(33, "Opportunity Summary Link", OPPORTUNITY_SUMMARY_LINK),
                    new SelectItem(34, "Created by", CREATED_BY),
                    new SelectItem(35, "Modified by", MODIFIED_BY)
            };
        }

        interface EVENT {
            String SHARED_EMPLOYEES_EMAILS = "${shared_employee_emails}";
            String CREATOR = "${creator}";
            String CREATOR_EMAIL = "${creator_email}";
            String GUESTS_EMAILS = "${guests_emails}";
            String EVENT_LINK = "${event_link}";
            String START_DATE = "${start_date}";
            String CALL_DURATION_TIME = "${duration}";
            String CALL_TYPE = "${call_type}";
            String CALL_DETAILS = "${call_details}";
            String OPPORTUNITY_ID = "${opportunity_id}";
            String ZOOM = "${zoom}";
            SelectItem[] EVENT_ATTRIBUTES = {
                    new SelectItem(0, "Shared Employee's Emails", SHARED_EMPLOYEES_EMAILS),
                    new SelectItem(1, "Creation Date", CREATION_DATE),
                    new SelectItem(2, "Modified Date", MODIFIED_DATE),
                    new SelectItem(3, "Creator Email", CREATOR_EMAIL),
                    new SelectItem(4, "Guest's Emails", GUESTS_EMAILS),
                    new SelectItem(5, "Shared Employees", ET_SHARED_EMPLOYEES),
                    new SelectItem(6, "Guests", ET_GUESTS),
                    new SelectItem(7, "When", ET_WHEN),
                    new SelectItem(8, "Event Link", EVENT_LINK),
                    new SelectItem(9, "Share Telegram", ET_SHARE_TELEGRAM),
                    new SelectItem(10, "Related Opportunity Id", OPPORTUNITY_ID),
                    new SelectItem(11, "Zoom", ZOOM)
            };

            SelectItem[] CALL_ATTRIBUTES = {
                    new SelectItem(0, "Created date", CREATION_DATE),
                    new SelectItem(1, "Updated date", MODIFIED_DATE),
                    new SelectItem(2, "Related Opportunity Id", OPPORTUNITY_ID)
            };
        }
    }

    interface ACCOUNTING {
        String CURRENCY = "${currency}";
        String SUPPLIER = "${supplier}";
        String PROJECT = "${project}";
        String INVOICE_NUMBER = "${invoice_number}";
        String INVOICE_DATE = "${invoice_date}";
        String REFERENCE = "${reference}";
        String PO_NUMBER = "${po_number}";
        String SHIP_VIA = "${ship_via}";
        String PURCHASE_ORDER_MANAGER_EMAIL = "${purchase_order_manager_email}";
        String PROJECT_MANAGER_EMAIL = "${project_manager_email}";
        String TOTAL = "${total_bc}";
        String TOTAL_INVOICE = "${total_ic}";
        String SUB_TOTAL = "${sub_total}";
        String TOTAL_INVOICE_CURRENCY = "${total_invoice_currency}";
        String TOTAL_DISCOUNT = "${total_discount}";
        String SALE_QUOTE = "${sale_quote}";
        String CREATED_DATE = "${created_date}";
        String UPDATED_DATE = "${updated_date}";
        String SALE_QUOTE_LINK = "${sale_quote_link}";
        String RFP_LINK = "${rfp_link}";
        String PURCHASE_ORDER_LINK = "${purchase_order_link}";
        String REPORTER_EMAIL = "${reporter_email}";
        String DECLINE_DATE = "${decline_date}";
        String DATE = "${date}";
        String REJECTION_NOTE = "${rejection_note}";
        String SUBMITTED_DATE = "${submitted_date}";
        String PRIMARY_CONTACT_PHONE = "${primary_contact_phone}";
        String PAID_AMOUNT_BASE_CURRENCY = "${paid_amount_base_currency}";
        String CUSTOMER_OWNER_EMAIL = "${customer_owner_email}";
        String CUSTOMER_INTEGRATION_ID = "${customer_integration_id}";
        String CUSTOMER_EMAIL = "${customer_email}";
        String CUSTOMER_ID = "${customer_id}";
        String CUSTOMER_CODE = "${customer_code}";
        String REMAINING_BALANCE = "${remaining_balance}";
        String CREDIT_AMOUNT = "${credit_amount}";

        String CUSTOMER_COUNTRY = "${customer_country}";
        String CUSTOMER_PHONE_NUMBER = "${customer_phone_number}";
        String CUSTOMER_NAME = "${customer_name}";
        String CUSTOMER_PRIMARY_MAILING_ADDRESS = "${customer_primary_mailing_address}";
        String DUE_AMOUNT_BASE_CURRENCY = "${due_amount_base_currency}";
        String DUE_AMOUNT_INVOICE_CURRENCY = "${due_amount_invoice_currency}";
        String CURRENT_APPROVER_CONTACT_PHONE = "${current_approver_contact_phone}";
        String NEXT_APPROVER_CONTACT_PHONE = "${next_approver_contact_phone}";
        String CURRENT_APPROVER = "${current_approver}";
        String CURRENT_APPROVER_EMAIL = "${current_approver_email}";
        String ACTIVE = "${active}";
        String TYPE = "${type}";
        String BANK_ACCOUNT = "${bank_account}";
        String EXCHANGE_RATE = "${exchange_rate}";
        String PRICE_LEVEL = "${price_level}";
        String TAX_CALC_TYPE = "${tax_calc_type}";
        String ITEMS = "${items}";
        String ITEM_DATA = "${item_data}";
        String FIRST_ITEM_CODE = "${first_item_code}";
        String FIRST_ITEM_NAME = "${first_item_name}";
        String TRANSFER_NAME = "${transfername}";
        String NUMBER = "${number}";
        String APPROVERS = "${approvers}";
        String CREATOR = "${creator}";
        String IS_CREDIT_NOTE = "${is_credit_note}";
        String ACCOUNTS_RECEIVABLE = "${accounts_receivable}";
        String PAYMENTS = "${payments}";
        String QUOTE_NUMBER = "${quote_number}";
        String CONVERTED_ITEM = "${converted_item}";
        String PAID_AMOUNT = "${paid_amount_bc}";
        String PAID_AMOUNT_INVOICE = "${paid_amount_ic}";
        String DUE_AMOUNT = "${due_amount}";
        String REFUNDS = "${refunds}";
        String PICK_DATE = "${pick_date}";
        String PACK_DATE = "${pack_date}";
        String SHIP_DATE = "${ship_date}";
        String EXPECTED_DATE = "${expected_date}";
        String GROSS_WEIGHT = "${gross_weight}";
        String TERMS = "${terms}";
        String DUE_DATE_TYPE = "${due_date_type}";
        String STATUS_CODE = "${status_code}";
        String OPPORTUNITY_ID = "${opportunity_id}";
        String CREATOR_EMAIL = "${creator_email}";
        String REJECTION_REASON = "${rejection_reason}";
        String FINANCIAL_YEAR = "${financial_year}";
        String NOTE = "${note}";
        String BILLING_REF = "${billing_ref}";
        String ACTUAL_DELIVERY_DATE = "${actual_delivery_date}";
        String APPROVED_TIME = "${approved_time}";
        String INSTRUCTION_NOTE_REASON = "${instruction_note_reason}";
        String INVOICE_TYPE_CD = "${invoice_type_cd}";
        String STR_NAME = "${str_name}";
        String PLOT_IDENTIFICATION = "${plot_identification}";
        String BUILDING_NUMBER = "${building_number}";
        String CITY = "${city}";
        String POST_ZONE = "${post_zone}";
        String SUB_DIVISION_NAME = "${sub_division_name}";
        String TIN_NUMBER = "${tin_number}";
        String REGNAME = "${regname}";
        String ITEM_QUANTITY = "${item_qty}";
        String ITEM_LINE = "${item_line}";
        String CUSTOMER_PASSPORT_NUMBER = "${customer_passport_number}";
        String APP_PROFILE = "${application_profile}";
        String TOTAL_NET_AMOUNT = "${total_net_amount}";
        String UUID = "${uuid}";
        String NOMINAL_INVOICE = "${nominal_invoice}";
        String EXPORT_INVOICE = "${export_invoice}";
        String TOTAL_DISCOUNT_AMT = "${total_discount_amt}";
        String SELLER_VAT_NUMBER = "${seller_vat_number}";
        String BUYER_VAT_NUMBER = "${buyer_vat_number}";
        String SELLER_NAME = "${seller_name}";
        String BUYER_NAME = "${buyer_name}";
        String SELLER_BUILDING_NUMBER = "${seller_building_number}";
        String BUYER_BUILDING_NUMBER = "${buyer_building_number}";
        String SELLER_STREET_NAME = "${seller_street_name}";
        String BUYER_STREET_NAME = "${buyer_street_name}";
        String SELLER_DISTRICT_NAME = "${seller_district_name}";
        String BUYER_DISTRICT_NAME = "${buyer_district_name}";
        String SELLER_CITY_NAME = "${seller_city_name}";
        String BUYER_CITY_NAME = "${buyer_city_name}";
        String SELLER_COUNTRY_CODE = "${seller_country_code}";
        String BUYER_COUNTRY_CODE = "${buyer_country_code}";
        String SELLER_POSTAL_CODE = "${seller_postal_code}";
        String BUYER_POSTAL_CODE = "${buyer_postal_code}";
        String BUYER_PLOT_IDENTIFICATION = "${buyer_plot_identification}";
        String CLIENT_NUMBER = "${client_number}";
        String TAXILLA_ACCESS_TOKEN = "${taxilla_access_token}";
        String INVOICE_ITEMS = "${invoice_items}";
        String INVOICE_ITEM_NAME = "${invoice_item_name}";
        String INVOICE_ITEM_PRICE = "${invoice_item_price}";
        String INVOICE_ITEM_QUANTITY = "${invoice_item_qty}";
        String INVOICE_ITEM_VAT_RATE_ID = "${invoice_item_vat_rate_id}";
        String INVOICE_ITEM_FAI_CATEGORY = "${invoice_item_fai_category}";
        String INVOICE_ITEM_DISCOUNT = "${invoice_item_discount}";
        String CREDIT_NOTE_INVOICE_INTEGRATION_ID = "${credit_note_invoice_integration_id}";
        String CREDIT_NOTE_INVOICE_REFERENCE = "${credit_note_invoice_reference}";
        String API_INVOICE_ITEMS = "${api_inovice_items}";
        String REPORTED_DATE = "${reported_date}";
        String ITEM_LINE_UAE = "${item_line_uae}";
        String TOTAL_TAX_AMOUNT = "${total_tax_amount}";
        String SELLER_IBAN = "${seller_iban}";

        SelectItem[] SALE_INVOICE_ATTRIBUTES = {
                new SelectItem(0, "Contact First Name", ET_FIRST_NAME),
                new SelectItem(1, "Contact Last Name", ET_LAST_NAME),
                new SelectItem(2, "Contact E-mail", ET_EMAIL),
                new SelectItem(3, "Contact Phone", PRIMARY_CONTACT_PHONE),
                new SelectItem(4, "Customer Email", CUSTOMER_EMAIL),
                new SelectItem(4, "Customer Name", CUSTOMER_NAME),
                new SelectItem(5, "Customer Phone Number", CUSTOMER_PHONE_NUMBER),
                new SelectItem(6, "Customer Primary Mailing Address", CUSTOMER_PRIMARY_MAILING_ADDRESS),
                new SelectItem(7, "Customer Owner Email", CUSTOMER_OWNER_EMAIL),
                new SelectItem(8, "Number", ET_NUMBER),
                new SelectItem(9, "Current Approver", CURRENT_APPROVER),
                new SelectItem(10, "Current Approver Email", CURRENT_APPROVER_EMAIL),
                new SelectItem(11, "Customer Telegram", ET_CUSTOMER_TELEGRAM),
                new SelectItem(12, "ID", ID),
                new SelectItem(13, "Object Key", OBJECT_KEY),
                new SelectItem(14, "Bank Account", BANK_ACCOUNT),
                new SelectItem(15, "Exchange Rate", EXCHANGE_RATE),
                new SelectItem(16, "Price Level", PRICE_LEVEL),
                new SelectItem(17, "Tax Calculation Type", TAX_CALC_TYPE),
                new SelectItem(18, "Items", ITEMS),
                new SelectItem(18, "First Item Number", FIRST_ITEM_CODE),
                new SelectItem(19, "Is Credit Note", IS_CREDIT_NOTE),
                new SelectItem(20, "Accounts Receivable", ACCOUNTS_RECEIVABLE),
                new SelectItem(21, "Payments", PAYMENTS),
                new SelectItem(22, "Quote Number", QUOTE_NUMBER),
                new SelectItem(23, "Converted Item", CONVERTED_ITEM),
                new SelectItem(25, "Due Amount", DUE_AMOUNT),
                new SelectItem(26, "Refunds", REFUNDS),
                new SelectItem(27, "Terms", TERMS),
                new SelectItem(28, "Due Date Type", DUE_DATE_TYPE),
                new SelectItem(29, "Status Code", STATUS_CODE),
                new SelectItem(30, "Customer Name", CUSTOMER_NAME),
                new SelectItem(31, "First Item Number", FIRST_ITEM_CODE),
                new SelectItem(32, "First Item Name", FIRST_ITEM_NAME),
                new SelectItem(33, "Creator", CREATOR),
                new SelectItem(34, "Financial Year", FINANCIAL_YEAR),
                new SelectItem(35, "Note", NOTE),
                new SelectItem(36, "Billing Ref", BILLING_REF),
                new SelectItem(37, "Actual Delivery Date", ACTUAL_DELIVERY_DATE),
                new SelectItem(38, "Instruction Note Reason", INSTRUCTION_NOTE_REASON),
                new SelectItem(39, "Invoice type Cd", INVOICE_TYPE_CD),
                new SelectItem(40, "Street name", STR_NAME),
                new SelectItem(41, "Plot Identification", PLOT_IDENTIFICATION),
                new SelectItem(42, "Building Number", BUILDING_NUMBER),
                new SelectItem(43, "City", CITY),
                new SelectItem(44, "Post Zone", POST_ZONE),
                new SelectItem(45, "Sub Division Name", SUB_DIVISION_NAME),
                new SelectItem(46, "Tin Number", TIN_NUMBER),
                new SelectItem(47, "Regname", REGNAME),
                new SelectItem(48, "Item Quantity", ITEM_QUANTITY),
                new SelectItem(49, "Item Line", ITEM_LINE),
                new SelectItem(50, "Total Net Amount", TOTAL_NET_AMOUNT),
                new SelectItem(51, "Approved Time", APPROVED_TIME),
                new SelectItem(52, "Application Profile", APP_PROFILE),
                new SelectItem(53, "Invoice item price", INVOICE_ITEM_PRICE),
                new SelectItem(54, "Invoice item quantity", INVOICE_ITEM_QUANTITY),
                new SelectItem(55, "Invoice item var rate id", INVOICE_ITEM_VAT_RATE_ID),
                new SelectItem(56, "Customer Integration Id", CUSTOMER_INTEGRATION_ID),
                new SelectItem(57, "Customer Passport Number", CUSTOMER_PASSPORT_NUMBER),
                new SelectItem(58, "Item Data", ITEM_DATA),
                new SelectItem(59, "Fai Item Category", INVOICE_ITEM_FAI_CATEGORY),
                new SelectItem(60, "Invoice item name", INVOICE_ITEM_NAME),
                new SelectItem(61, "Credit note invoice reference", CREDIT_NOTE_INVOICE_REFERENCE),
                new SelectItem(62, "Invoice Items (API)", API_INVOICE_ITEMS),
                new SelectItem(63, "Customer ID", CUSTOMER_ID),
                new SelectItem(64, "Customer Code", CUSTOMER_CODE),
                new SelectItem(65, "Item Line (UAE)", ITEM_LINE_UAE),
                new SelectItem(66, "Total Tax Amount", TOTAL_TAX_AMOUNT),
                new SelectItem(67, "Seller IBAN", SELLER_IBAN)
        };
        SelectItem[] RECEIVE_PAYMENT_ATTRIBUTES = {
                new SelectItem(1, "Customer Balance", ET_CUSTOMER_BALANCE),
                new SelectItem(2, "Customer Phone", CUSTOMER_PHONE_NUMBER),
                new SelectItem(3, "Customer ID", CUSTOMER_ID),
                new SelectItem(4, "ID", ID),
                new SelectItem(5, "Remaining Balance", REMAINING_BALANCE),
                new SelectItem(6, "Credit Amount", CREDIT_AMOUNT)
        };

        SelectItem[] SALE_ORDER_ATTRIBUTES = {
                new SelectItem(0, "Number", ET_NUMBER),
                new SelectItem(1, "Customer Telegram", ET_CUSTOMER_TELEGRAM),
                new SelectItem(2, "ID", ID),
                new SelectItem(3, "Object Key", OBJECT_KEY),
                new SelectItem(4, "Bank Account", BANK_ACCOUNT),
                new SelectItem(5, "Currency", CURRENCY),
                new SelectItem(6, "Exchange Rate", EXCHANGE_RATE),
                new SelectItem(7, "Price Level", PRICE_LEVEL),
                new SelectItem(8, "Tax Calculation Type", TAX_CALC_TYPE),
                new SelectItem(9, "Items", ITEMS),
                new SelectItem(10, "Terms", TERMS),
                new SelectItem(11, "Due Date Type", DUE_DATE_TYPE),
                new SelectItem(12, "Status Code", STATUS_CODE),
                new SelectItem(13, "Opportunity ID", OPPORTUNITY_ID),
                new SelectItem(14, "Rejection Reason", REJECTION_REASON),
                new SelectItem(15, "Rejection Note", REJECTION_NOTE),
                new SelectItem(16, "Customer Phone", ET_CUSTOMER_PHONE),
                new SelectItem(17, "First Item Number", FIRST_ITEM_CODE)

        };
        SelectItem[] SALE_QUOTE_ATTRIBUTES = {
                new SelectItem(0, "Company Name", ET_COMPANY_NAME),
                new SelectItem(1, "Email", ET_EMAIL),
                new SelectItem(2, "First Name", ET_FIRST_NAME),
                new SelectItem(3, "Last Name", ET_LAST_NAME),
                new SelectItem(4, "Created Date", CREATED_DATE),
                new SelectItem(5, "Updated Date", UPDATED_DATE),
                new SelectItem(6, "Sale Quote link", SALE_QUOTE_LINK),
                new SelectItem(7, "Number", ET_NUMBER),
                new SelectItem(8, "Customer Telegram", ET_CUSTOMER_TELEGRAM),
                new SelectItem(9, "Manager Telegram", ET_MANAGER_TELEGRAM),
                new SelectItem(10, "ID", ID),
                new SelectItem(11, "Object Key", OBJECT_KEY),
                new SelectItem(12, "Bank Account", BANK_ACCOUNT),
                new SelectItem(13, "Currency", CURRENCY),
                new SelectItem(14, "Exchange Rate", EXCHANGE_RATE),
                new SelectItem(15, "Price Level", PRICE_LEVEL),
                new SelectItem(16, "Tax Calculation Type", TAX_CALC_TYPE),
                new SelectItem(17, "Items", ITEMS),
                new SelectItem(18, "Terms", TERMS),
                new SelectItem(19, "Due Date Type", DUE_DATE_TYPE),
                new SelectItem(20, "Status Code", STATUS_CODE),
                new SelectItem(21, "Creator email", CREATOR_EMAIL),
                new SelectItem(22, "Creator", CREATOR),
                new SelectItem(23, "Rejection Reason", REJECTION_REASON),
                new SelectItem(24, "Rejection Note", REJECTION_NOTE)
        };
        SelectItem[] RFP_ATTRIBUTES = {
                new SelectItem(0, "Company Name", ET_COMPANY_NAME),
                new SelectItem(1, "Email", ET_EMAIL),
                new SelectItem(2, "First Name", ET_FIRST_NAME),
                new SelectItem(3, "Last Name", ET_LAST_NAME),
                new SelectItem(4, "Created Date", CREATED_DATE),
                new SelectItem(6, "RFP link", RFP_LINK),
                new SelectItem(7, "Number", ET_NUMBER),
                new SelectItem(8, "Customer Telegram", ET_CUSTOMER_TELEGRAM),
                new SelectItem(9, "Approver Telegram", ET_APPROVER_TELEGRAM),
        };
        SelectItem[] PURCHASE_ORDER_ATTRIBUTES = {
                new SelectItem(0, "Company Name", ET_COMPANY_NAME),
                new SelectItem(1, "Creator Email", ET_EMAIL),
                new SelectItem(1, "Purchase Order Manager Email", PURCHASE_ORDER_MANAGER_EMAIL),
                new SelectItem(1, "Project Manager Email", PROJECT_MANAGER_EMAIL),
                new SelectItem(2, "First Name", ET_FIRST_NAME),
                new SelectItem(3, "Last Name", ET_LAST_NAME),
                new SelectItem(4, "Created Date", CREATED_DATE),
                new SelectItem(5, "Updated Date", UPDATED_DATE),
                new SelectItem(6, "Purchase Order link", PURCHASE_ORDER_LINK),
                new SelectItem(7, "Number", ET_NUMBER),
                new SelectItem(8, "Current Approver", CURRENT_APPROVER),
                new SelectItem(9, "Current Approver Email", CURRENT_APPROVER_EMAIL),
                new SelectItem(10, "Supplier Telegram", ET_SUPPLIER_TELEGRAM),
                new SelectItem(11, "ID", ID),
                new SelectItem(12, "Object Key", OBJECT_KEY),
                new SelectItem(13, "Bank Account", BANK_ACCOUNT),
                new SelectItem(14, "Currency", CURRENCY),
                new SelectItem(15, "Exchange Rate", EXCHANGE_RATE),
                new SelectItem(16, "Price Level", PRICE_LEVEL),
                new SelectItem(17, "Tax Calculation Type", TAX_CALC_TYPE),
                new SelectItem(18, "Items", ITEMS),
                new SelectItem(19, "Terms", TERMS),
                new SelectItem(20, "Due Date Type", DUE_DATE_TYPE),
                new SelectItem(21, "Status Code", STATUS_CODE)
        };
        SelectItem[] EXPENSE_CLAIM_ATTRIBUTES = {
                new SelectItem(1, "Link", ET_LINK),
                new SelectItem(2, "Decline Date", DECLINE_DATE),
                new SelectItem(3, "Rejection Note", REJECTION_NOTE),
                new SelectItem(4, "Reporter Email", REPORTER_EMAIL),
                new SelectItem(5, "Submitted Date", SUBMITTED_DATE),
                new SelectItem(6, "Reporter", ET_CASE_REPORTER),
                new SelectItem(7, "Current Approver Contact Phone", CURRENT_APPROVER_CONTACT_PHONE),
                new SelectItem(8, "Next Approver Contact Phone", NEXT_APPROVER_CONTACT_PHONE),
                new SelectItem(9, "Currency", CURRENCY),
                new SelectItem(10, "Supplier Telegram", ET_SUPPLIER_TELEGRAM),
                new SelectItem(11, "Current Approver Telegram", ET_CURRENT_APPROVER_TELEGRAM),
                new SelectItem(12, "Next Approver Telegram", ET_NEXT_APPROVER_TELEGRAM),
                new SelectItem(19, "Expense item name", INVOICE_ITEM_NAME)
        };
        SelectItem[] RFQ_ATTRIBUTES = {
                new SelectItem(1, "Link", ET_LINK),
                new SelectItem(2, "Company Name", ET_COMPANY_NAME),
                new SelectItem(3, "Customer Telegram", ET_CUSTOMER_TELEGRAM)
        };
        SelectItem[] MANUAL_ENTRY_ATTRIBUTES = {
                new SelectItem(1, "Link", ET_LINK)
        };
        SelectItem[] GDN_ATTRIBUTES = {
                new SelectItem(1, "Number", EmailTemplateConstants.ET_NUMBER),
                new SelectItem(2, "Link", ET_LINK),
                new SelectItem(3, "Created Date", CREATED_DATE),
                new SelectItem(4, "Updated Date", UPDATED_DATE),
                new SelectItem(5, "Creator", EmailTemplateConstants.ET_CREATOR)
        };
        SelectItem[] PICKLIST_ATTRIBUTES = {
                new SelectItem(1, "Number", EmailTemplateConstants.ET_NUMBER),
                new SelectItem(2, "Link", ET_LINK),
                new SelectItem(3, "Created Date", CREATED_DATE),
                new SelectItem(4, "Updated Date", UPDATED_DATE),
                new SelectItem(5, "Creator", ET_CREATOR),
                new SelectItem(6, "Pick Date", PICK_DATE),
                new SelectItem(7, "Pack Date", PACK_DATE),
                new SelectItem(8, "Ship Date", SHIP_DATE),
                new SelectItem(9, "Gross Weight", GROSS_WEIGHT),
                new SelectItem(10, "Expected Date", EXPECTED_DATE)
        };
        SelectItem[] PURCHASE_INVOICE_ATTRIBUTES = {
                new SelectItem(1, "Link", ET_LINK),
                new SelectItem(2, "Supplier Telegram", ET_SUPPLIER_TELEGRAM),
                new SelectItem(3, "ID", ID),
                new SelectItem(4, "Object Key", OBJECT_KEY),
                new SelectItem(5, "Bank Account", BANK_ACCOUNT),
                new SelectItem(6, "Exchange Rate", EXCHANGE_RATE),
                new SelectItem(7, "Price Level", PRICE_LEVEL),
                new SelectItem(8, "Tax Calculation Type", TAX_CALC_TYPE),
                new SelectItem(9, "Items", ITEMS),
                new SelectItem(10, "Is Credit Note", IS_CREDIT_NOTE),
                new SelectItem(11, "Accounts Receivable", ACCOUNTS_RECEIVABLE),
                new SelectItem(12, "Payments", PAYMENTS),
                new SelectItem(14, "Converted Item", CONVERTED_ITEM),
                new SelectItem(15, "Due Amount", DUE_AMOUNT),
                new SelectItem(16, "Refunds", REFUNDS),
                new SelectItem(17, "Terms", TERMS),
                new SelectItem(18, "Due Date Type", DUE_DATE_TYPE),
                new SelectItem(19, "Status Code", STATUS_CODE),
                new SelectItem(20, "Credit note Invoice Integration Id", CREDIT_NOTE_INVOICE_INTEGRATION_ID),
                new SelectItem(21, "Credit note Invoice Reference", CREDIT_NOTE_INVOICE_REFERENCE),
                new SelectItem(22, "Supplier Integration Id", ET_SUPPLIER_INTEGRATION_ID),
                new SelectItem(23, "Invoice item price", INVOICE_ITEM_PRICE),
                new SelectItem(24, "Invoice item quantity", INVOICE_ITEM_QUANTITY),
                new SelectItem(25, "Invoice item var rate id", INVOICE_ITEM_VAT_RATE_ID),
                new SelectItem(26, "Invoice item fai category", INVOICE_ITEM_FAI_CATEGORY),
                new SelectItem(27, "Invoice item name", INVOICE_ITEM_NAME)
        };
        SelectItem[] STOCK_TRANSFER_ATTRIBUTES = {
                new SelectItem(0, "Transfer Name", TRANSFER_NAME),
                new SelectItem(1, "Number", NUMBER),
                new SelectItem(2, "Approvers", APPROVERS),
                new SelectItem(2, "Creator", CREATOR),

        };

        interface ProductCategory {
            String ORDER = "${order}";
            String PARENT_CATEGORY_ID = "${parent_category_id}";
            String PARENT_CATEGORY_NAME = "${parent_category_name}";
            SelectItem[] PRODUCT_CATEGORY_ATTRIBUTES = {
                    new SelectItem(0, "id", ID),
                    new SelectItem(1, "Name", ET_NAME),
                    new SelectItem(2, "Description", ET_DESCRIPTION),
                    new SelectItem(3, "Number", NUMBER),
                    new SelectItem(4, "Order", ORDER),
                    new SelectItem(5, "Status", ET_STATUS),
                    new SelectItem(6, "Updated Date", UPDATED_DATE),
                    new SelectItem(7, "Parent Category Id", PARENT_CATEGORY_ID),
                    new SelectItem(8, "Parent Category Name", PARENT_CATEGORY_ID)
            };
        }


        interface PRODUCT {
            String NAME = "${name}";
            String NUMBER = "${number}";
            String SALES_ACCOUNT_ID = "${sales_account_id}";
            String SALES_ACCOUNT_NAME = "${sales_account_name}";
            String SALES_ACCOUNT_CODE = "${sales_account_code}";
            String COGS_ACCOUNT_ID = "${cogs_account_id}";
            String COGS_ACCOUNT_CODE = "${cogs_account_code}";
            String PURCHASED_FROM_SUPPLIER = "${purchased_from_supplier}";
            String CATEGORY_ID = "${category_id}";
            String BRAND_ID = "${brand_id}";
            String TAX_ID = "${tax_id}";
            String SKU_NUMBER = "${sku_number}";
            String UNIT_MEASUREMENT_ID = "${unit_measurement_id}";
            String UNIT_MEASUREMENT_CODE = "${unit_measurement_code}";
            String PART_NUMBER = "${part_number}";
            String UPC_NUMBER = "${upc_number}";
            String MANUFACTURER = "${manufacturer}";
            String INVENTORY_STOCK_AS_OF_DATE = "${inventory_stock_as_of_date}";
            String INVENTORY_STOCK_BATCH_TRACKING = "${inventory_stock_batch_tracking}";
            String INVENTORY_ASSET_ACCOUNT = "${inventory_asset_account}";
            String INVENTORY_TRACKING = "${inventory_tracking}";
            String TRACK_BATCH = "${track_batch}";
            String TOTAL_VALUE = "${total_value}";
            String LOCATION = "${location}";
            String SUPPLIER = "${supplier}";
            String DISCOUNT = "${discount}";
            String QUANTITY = "${quantity}";
            String IMAGE_URL = "${image_url}";
            String WAREHOUSE = "${warehouse}";
            String SELLING_PRICE = "${selling_price}";
            String PURCHASE_PRICE = "${purchase_price}";
            String QUANTITY_ON_HAND = "${quantity_on_hand}";
            SelectItem[] PRODUCT_ATTRIBUTES = {
                    new SelectItem(1, "Supplier Telegram", ET_SUPPLIER_TELEGRAM),
                    new SelectItem(2, "ID", ID),
                    new SelectItem(3, "Object Key", OBJECT_KEY),
                    new SelectItem(4, "Active", ACTIVE),
                    new SelectItem(5, "Type", TYPE),
                    new SelectItem(6, "Sales Account ID", SALES_ACCOUNT_ID),
                    new SelectItem(7, "Sales Account Name", SALES_ACCOUNT_NAME),
                    new SelectItem(8, "Sales Account Code", SALES_ACCOUNT_CODE),
                    new SelectItem(9, "Purchase Account ID", COGS_ACCOUNT_ID),
                    new SelectItem(10, "Purchase Account Code", COGS_ACCOUNT_CODE),
                    new SelectItem(11, "Purchased From Supplier", PURCHASED_FROM_SUPPLIER),
                    new SelectItem(12, "Category ID", CATEGORY_ID),
                    new SelectItem(14, "Brand ID", BRAND_ID),
                    new SelectItem(16, "Tax ID", TAX_ID),
                    new SelectItem(17, "Sku Number", SKU_NUMBER),
                    new SelectItem(18, "Unit Measurement ID", UNIT_MEASUREMENT_ID),
                    new SelectItem(19, "Unit Measurement Code", UNIT_MEASUREMENT_CODE),
                    new SelectItem(20, "Part Number", PART_NUMBER),
                    new SelectItem(21, "Upc Number", UPC_NUMBER),
                    new SelectItem(22, "Manufacturer", MANUFACTURER),
                    new SelectItem(23, "Inventory Stock As Of Date", INVENTORY_STOCK_AS_OF_DATE),
                    new SelectItem(24, "Inventory Stock Batch Tracking", INVENTORY_STOCK_BATCH_TRACKING),
                    new SelectItem(25, "Inventory Tracking", INVENTORY_TRACKING),
                    new SelectItem(26, "Track Batch", TRACK_BATCH),
                    new SelectItem(27, "Total Value", TOTAL_VALUE),
                    new SelectItem(28, "Location", LOCATION),
                    new SelectItem(29, "Supplier", SUPPLIER),
                    new SelectItem(30, "Discount", DISCOUNT),
                    new SelectItem(31, "Quantity", QUANTITY),
                    new SelectItem(32, "Image URL", IMAGE_URL),
                    new SelectItem(33, "Warehouse", WAREHOUSE),
                    new SelectItem(34, "Price Level", PRICE_LEVEL),
                    new SelectItem(35, "Selling Price", SELLING_PRICE),
                    new SelectItem(36, "Quantity on hand", QUANTITY_ON_HAND),
                    new SelectItem(37, "Purchase Price", PURCHASE_PRICE)
            };
        }

        String DIDOX_TOKEN = "";

        interface CLIENT {
            SelectItem[] CLIENT_ATTRIBUTES = {
                    new SelectItem(1, "Didox Token", DIDOX_TOKEN)
            };
        }
    }

    interface PAYROLL {
        String REQUESTER_EMAIL = "${requester_email}";
        String CASH_ADVANCE_LINK = "${cash_advance_link}";
        String FIRST_APPROVER_EMAIL = "${first_approver_email}";
        String SECOND_APPROVER_EMAIL = "${second_approver_email}";
        String THIRD_APPROVER_EMAIL = "${third_approver_email}";
        String FOURTH_APPROVER_EMAIL = "${fourth_approver_email}";
        String FIFTH_APPROVER_EMAIL = "${fifth_approver_email}";
        String CURRENT_APPROVER = "${current_approver}";
        String CURRENT_APPROVER_EMAIL = "${current_approver_email}";
        String PAYMENT_REFERENCE = "${payment_reference}";
        String CREATOR = "${creator}";
        String DATE = "${date}";
        String APPROVER = "${approver}";
        String TOTAL = "${total}";
        String TOTAL_IN_BASE = "${totalinbase}";
        String CURRENCY = "${currency}";
        String PAYMENT_METHOD = "${paymentmethod}";
        String PROCESS_DATE = "${processdate}";
        String BASIC_SALARY = "${basicsalary}";
        String ALLOWANCE = "${allowance}";
        String PENSION = "${pension}";
        String DEDUCTION = "${deduction}";
        String TAX = "${tax}";
        String EXPENSE = "${expense}";
        String CATEGORY = "${category}";
        String TYPE = "${type}";
        String STATUS = "${status}";
        String REFERENCE = "${reference}";
        String MONTH = "${month}";
        String YEAR = "${year}";
        String PAYROLL_BATCH = "${payroll_batch}";
        String EMPLOYEE = "${employee}";
        String APPROVERS = "${approvers}";
        String ITEMS = "${items}";
        String SHOW_IN_PAYSLIP = "${show_in_payslip}";
        String PAYMENT_TYPE = "${payment_type}";
        SelectItem[] CASH_ADVANCE_ATTRIBUTES = {
                new SelectItem(0, "Requester Email", REQUESTER_EMAIL),
                new SelectItem(1, "Cash Advance Link", CASH_ADVANCE_LINK),
                new SelectItem(2, "First Approver Email", FIRST_APPROVER_EMAIL),
                new SelectItem(3, "Second Approver Email", SECOND_APPROVER_EMAIL),
                new SelectItem(4, "Third Approver Email", THIRD_APPROVER_EMAIL),
                new SelectItem(5, "Fourth Approver Email", FOURTH_APPROVER_EMAIL),
                new SelectItem(6, "Fifth Approver Email", FIFTH_APPROVER_EMAIL),
                new SelectItem(7, "Requester Telegram", ET_REQUEST_DESCRIPTION),
                new SelectItem(8, "First Approver Telegram", ET_FIRST_APPROVER_TELEGRAM),
                new SelectItem(9, "Second Approver Telegram", ET_SECOND_APPROVER_TELEGRAM),
                new SelectItem(10, "Third Approver Telegram", ET_THIRD_APPROVER_TELEGRAM),
                new SelectItem(11, "Fourth Approver Telegram", ET_FOURTH_APPROVER_TELEGRAM),
                new SelectItem(12, "Fifth Approver Telegram", ET_FIFTH_APPROVER_TELEGRAM),
                new SelectItem(13, "Current Approver Telegram", ET_CURRENT_APPROVER_TELEGRAM),
                new SelectItem(14, "Next Approver Telegram", ET_NEXT_APPROVER_TELEGRAM),
                new SelectItem(15, "Previous Approver Telegram", ET_PREVIOUS_APPROVER_TELEGRAM)
        };
        SelectItem[] PAYRUN_ATTRIBUTES = {
                new SelectItem(0, "Approver", APPROVER),
                new SelectItem(1, "Total", TOTAL),
                new SelectItem(2, "Total in Base", TOTAL_IN_BASE),
                new SelectItem(3, "Currency", CURRENCY),
                new SelectItem(4, "Payment Method", PAYMENT_METHOD),
                new SelectItem(5, "Process Date", PROCESS_DATE),
                new SelectItem(6, "Basic Salary", BASIC_SALARY),
                new SelectItem(7, "Allowance", ALLOWANCE),
                new SelectItem(8, "Pension", PENSION),
                new SelectItem(9, "Deduction", DEDUCTION),
                new SelectItem(10, "Expense", EXPENSE),
        };

        SelectItem[] ADDITIONAL_PAYMENT_ATTRIBUTES = {
                new SelectItem(0, "ID", ID),
                new SelectItem(1, "Category", CATEGORY),
                new SelectItem(2, "Type", TYPE),
                new SelectItem(3, "Status", STATUS),
                new SelectItem(4, "Reference", REFERENCE),
                new SelectItem(5, "Month", MONTH),
                new SelectItem(6, "Year", YEAR),
                new SelectItem(7, "Payroll Batch", PAYROLL_BATCH),
                new SelectItem(8, "Employee", EMPLOYEE),
                new SelectItem(9, "Approvers", APPROVERS),
                new SelectItem(10, "Items", ITEMS),
                new SelectItem(11, "Show in payslip", SHOW_IN_PAYSLIP),
                new SelectItem(12, "Payment Type", PAYMENT_TYPE),
        };

        interface PAYSLIP_APPROVED {
            String EMP_FIRST_NAME = "${employeefirstname}";
            String EMP_LAST_NAME = "${employeelastname}";
            String APPROVER_FIRST_NAME = "${approverfirstname}";
            String APPROVER_LAST_NAME = "${approverlastname}";
            String MONTH = "${month}";
            String YEAR = "${year}";
            String APPROVED_DATE = "${approveddate}";
            String PROCESS_DATE = "${processdate}";
            String TYPE = "${type}";
        }
    }

    interface TRAINING_CENTER {
        String COURSE_BOOKING_LINK = "${course_booking_link}";

        interface SCHEDULED_COURSE_STUDENT {
            String STUDENT_NAME = "${student_name}";
            String STUDENT_EMAIL = "${student_email}";
            String STUDENT_PHONE = "${student_phone}";
            String STUDENT_NEW_CS = "${training_code}";
            String STUDENT_NEW_DATE = "${new_date}";
            SelectItem[] CSS_ATTRIBUTES = {
                    new SelectItem(0, "Student Name", STUDENT_NAME),
                    new SelectItem(1, "Student E-mail", STUDENT_EMAIL),
                    new SelectItem(2, "Student Phone", STUDENT_PHONE),
                    new SelectItem(3, "New Training #", STUDENT_NEW_CS),
                    new SelectItem(4, "New Date", STUDENT_NEW_DATE)};
        }

        interface SCHEDULED_COURSE {
            String CS_STUDENTS = "${students}";
            String CS_STUDENT_EMAILS = "${student_emails}";
            String CS_STUDENT_PHONES = "${student_phones}";
            String ASSESSOR = "${assessor}";
            String COURSE = "${course}";
            String INSTRUCTOR = "${instructor}";
            String LANGUAGE = "${language}";
            String LOCATION = "${location}";
            String NUMBER_OF_SEATS = "${number_of_seats}";
            String START_DATE = "${start_date}";
            SelectItem[] CS_ATTRIBUTES = {
                    new SelectItem(0, "Course Schedule Students", CS_STUDENTS),
                    new SelectItem(1, "CS Student Emails", CS_STUDENT_EMAILS),
                    new SelectItem(2, "CS Student Phones", CS_STUDENT_PHONES)};
        }

        interface CERTIFICATE {
            String STUDENT = "${student}";
            String STUDENT_EMAIL = "${student_email}";
            String CERTIFICATE_TYPE = "${certificate_type}";
            String CREATION_DATE = "${creation_date}";
            String STUDENT_PHONE = "${student_phone}";
            SelectItem[] ATTRIBUTES = {
                    new SelectItem(0, "Link", ET_LINK),
                    new SelectItem(1, "Creation Date", CREATION_DATE),
                    new SelectItem(2, "Number", ET_NUMBER),
                    new SelectItem(3, "Student", STUDENT),
                    new SelectItem(4, "Student Email", STUDENT_EMAIL),
                    new SelectItem(5, "Certificate Type", CERTIFICATE_TYPE),
                    new SelectItem(6, "Student Phone", STUDENT_PHONE),
                    new SelectItem(7, "Employee Telegram", ET_EMPLOYEE_TELEGRAM)
            };
        }
    }

    interface MEETING_MINUTES {
        String MT_TITLE = "${title}";
        String MT_CALLEDBY = "${calledBy}";
        String MT_PURPOSE = "${purpose}";
        String MT_LOCATION = "${location}";
        String MT_RECIPIENT = "${recipientname}";
    }

    interface PENALTIES_PROMOTIONS {
        String PP_EMPLOYEE_NAME = "${employeename}";
        String PP_CREATOR_NAME = "${creator_name}";
        String PP_EFFECTIVE_DATE = "${effectivedate}";
        String PP_PROJECT_NAME = "${projectname}";
        String PP_MYEMAIL = "${assigner_email}";
        String PP_REPLYEMAIL = "${replyemail}";
        String PP_POINT_AMOUNT = "${pointamount}";
        String PP_PENALTY_PROMOTION_TYPE_NAME = "${penalty_promotion}";
        String PP_NAME = "${penaltypromotion_name}";
        String PP_AMOUNT = "${penaltyamount}";
        String PP_ASSIGNED_DATE = "${assigned_date}";
    }

    interface CONTRACT_REMINDER_VALUES {
        String CC_EMPLOYEE_NAME = "${receiving_employeename}";
        String CC_CONTRACT_CREATOR = "${contract_creator}";
        String CC_CONTRACT_NUMBER = "${contract_number}";
        String CC_CONTRACT_LEFT_TIME_TO_DUE_DATE = "${left_time_to_due_date}";
        String CC_CONTRACT_DUE_DATE = "${contract_due_date}";
        String CC_CONTRACT_CUSTOMER = "${contract_customer}";
    }

    interface TASK_REMINDER_VALUES {
        String OVERTIME = "${overtime}";
        String HOST = "${host}";
        String EMPLOYEE_TASK = "${employeetask}";
        String USER = "${user}";
        String PROJECT_NAME = "${projectname}";
        String ASSIGNEES = "${assignees}";
        String DATE = "${date}";
        String ESTIMATED_TIME = "${estimatedtime}";
        String COMPLETED = "${completed}";
    }

    interface BILL_OF_MATERIALS_VALUES {
        String RECEPIENT = "${recipientname}";
        String CURRENT_USER = "${currentUser}";
        String REJECTION_REASON = "${rejectionReason}";
        String LINK = "${link}";
        String PROJECT_NAME = "${projectName}";
        String START_DATE = "${startdate}";
        String DATE = "${date}";
    }

    interface PAYMENT_VALUES {
        String CUSTOMER = "${customerName}";
        String INVOICE_TOTAL_AMOUNT = "${invoiceTotalAmount}";
        String CURRENCY = "${currency}";
        String SUPPORT_EMAIL = "${supportEmail}";
        String USERS_COUNT = "${usersCount}";
        String USERS_PRICE = "${usersPrice}";
        String ESS_USERS_COUNT = "${essUsersCount}";
        String ESS_USERS_PRICE = "${essUsersPrice}";
        String NON_USERS_COUNT = "${nonUsersCount}";
        String NON_USERS_PRICE = "${nonUsersPrice}";
        String TOTAL_SUBSCRIPTION = "${totalSubscription}";
        String USERS_DISCOUNT = "${usersDiscount}";
        String TOTAL_ADD_ONS = "${totalAddOn}";
        String APPS_COUNT = "${appsCount}";
        String APPS = "${apps}";
        String DESCRIPTION = "${description}";
        String PAYMENT_DATE = "${paymentDate}";
        String CURRENT_YEAR = "${currentYear}";
        String LOGO = "${logo}";
        String HOST = "${host}";
        String COMPANY_NAME = "${companyName}";
        String COMPANY_ID = "${companyId}";
        String SUBSCRIPTION_OPERATION = "${subscriptionOperation}";
        //ADD-ONS
        String ONLINE_TRAINING_QTY = "${onlineTrainingQty}";
        String ONLINE_TRAINING_TOTAL = "${onlineTrainingTotal}";
        String INITIAL_SETUP_QTY = "${initialSetupQty}";
        String INITIAL_SETUP_TOTAL = "${initialSetupTotal}";
        String DEDICATED_ACCOUNT_MANAGER_QTY = "${dedicatedAccountManagerQty}";
        String DEDICATED_ACCOUNT_MANAGER_TOTAL = "${dedicatedAccountManagerTotal}";
        String CUSTOM_PDF_TEMPLATE_QTY = "${customPDFTemplateQty}";
        String CUSTOM_PDF_TEMPLATE_TOTAL = "${customPDFTemplateTotal}";
        String EXTRA_STORAGE_QTY = "${extraStorageQty}";
        String EXTRA_STORAGE_TOTAL = "${extraStorageTotal}";
        String DEDICATED_DEVELOPER_QTY = "${dedicatedDeveloperQty}";
        String DEDICATED_DEVELOPER_TOTAL = "${dedicatedDeveloperTotal}";
    }

    interface CustomForm {
        String ET_UPDATER = "${updater}";
        String PDF_LINK = "${pdf_link}";
        SelectItem[] ATTRIBUTES = {
                new SelectItem(0, "Link", ET_LINK),
                new SelectItem(1, "Created By", ET_CREATOR),
                new SelectItem(2, "Updated By", ET_UPDATER),
                new SelectItem(3, "ID", ID),
                new SelectItem(4, "PDF Link", PDF_LINK)
        };
    }


    interface CompanyForm {
        String COMPANY_ID = "${company_id}";
        String NAME = "${name}";
        String EMAIL = "${email}";
        String WEBSITE = "${website}";
        String OFFICE_PHONE_NUMBER = "${office_phone_number}";
        String MOBILE_PHONE_NUMBER = "${mobile_phone_number}";
        String FULL_ADDRESS = "${address}";
        String ADDRESS_NAME = "${address_name}";
        String ADDRESS_LINE_1 = "${address_line_1}";
        String ADDRESS_LINE_2 = "${address_line_2}";
        String ADDRESS_CITY = "${address_city}";
        String ADDRESS_STATE = "${address_state}";
        String ADDRESS_ZIPCODE = "${address_zipcode}";
        String ADDRESS_COUNTRY = "${address_country}";
        String LANGUAGE = "${language}";
        String TIME_ZONE = "${time_zone}";
        String TIME_SLOT = "${timeslots}";

        SelectItem[] ATTRIBUTES = {
                new SelectItem(0, "Company Id", COMPANY_ID),
                new SelectItem(1, "Name", NAME),
                new SelectItem(2, "Email", EMAIL),
                new SelectItem(3, "Website", WEBSITE),
                new SelectItem(4, "Office Phone Number", OFFICE_PHONE_NUMBER),
                new SelectItem(5, "Mobile Phone Number", MOBILE_PHONE_NUMBER),
                new SelectItem(6, "Full Address", FULL_ADDRESS),
                new SelectItem(7, "Address Name", ADDRESS_NAME),
                new SelectItem(8, "Address Line 1", ADDRESS_LINE_1),
                new SelectItem(9, "Address Line 2", ADDRESS_LINE_2),
                new SelectItem(10, "Address City", ADDRESS_CITY),
                new SelectItem(11, "Address State", ADDRESS_STATE),
                new SelectItem(12, "Address Zipcode", ADDRESS_ZIPCODE),
                new SelectItem(13, "Address Country", ADDRESS_COUNTRY),
                new SelectItem(14, "Language", LANGUAGE),
                new SelectItem(15, "Time Zone", TIME_ZONE),
                new SelectItem(16, "Time Slots", TIME_SLOT)
        };
    }

}
