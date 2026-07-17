package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.CURRENCY;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.CUSTOMER_COUNTRY;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.CUSTOMER_EMAIL;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.CUSTOMER_PHONE_NUMBER;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.PAID_AMOUNT_INVOICE;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.TOTAL;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.TOTAL_INVOICE;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.CONTRACT_REMINDER_VALUES.CC_CONTRACT_CUSTOMER;

/**
 * User: Ilhombek
 * Date: 21.07.2010
 * Time: 18:44:43
 */
public class EmailTemplateUtils implements EmailTemplateConstants {

    /**
     * Related accounting base category fields
     *
     * @return Map
     */
    public static Map<String, String> getAccountingBaseFields() {
        Map<String, String> personalAttrMap = new TreeMap<>();

        personalAttrMap.put(ET_FIRST_NAME, "Client Contact First Name");
        personalAttrMap.put(ET_LAST_NAME, "Client Contact Last Name");
        personalAttrMap.put(ET_COMPANY_NAME, "Company Name");
        personalAttrMap.put(ET_CUSTOMER, "Client Name");
        personalAttrMap.put(ET_EMAIL, "Contact employee email");
        personalAttrMap.put(ET_PAID_AMOUNT, "Invoice amount");
        personalAttrMap.put(PAID_AMOUNT_INVOICE, "Invoice amount");
        personalAttrMap.put(ET_NUMBER, "Number");
        personalAttrMap.put(ET_SIGNATURE, "Your signature");
        personalAttrMap.put(ACCOUNTING.PO_NUMBER, "PO Number");
        personalAttrMap.put(ET_DUE_DATE, "Due Date");
        personalAttrMap.put(CUSTOMER_PHONE_NUMBER, "Customer phone number");
        personalAttrMap.put(CUSTOMER_EMAIL, "Customer email");
        personalAttrMap.put(CUSTOMER_COUNTRY, "customer country");
        personalAttrMap.put(CURRENCY, "invoice currency");
        personalAttrMap.put(CLICK_LINK, "click link");
        personalAttrMap.put(PAYME_LINK, "payme link");
        personalAttrMap.put(TOTAL, "total");
        personalAttrMap.put(TOTAL_INVOICE, "total invoice");
        personalAttrMap.put(PAYPAL_LINK, "paypalLink");
        return personalAttrMap;
    }

    /**
     * Related Sale Invoice category fields
     *
     * @return Map
     */
    public static Map<String, String> getSaleInvoiceCategoryFields() {
        Map<String, String> personalAttrMap = getAccountingBaseFields();
        personalAttrMap.put(ET_START_DATE, "Invoice Date");
        return personalAttrMap;
    }

    /**
     * Related Customer Balance category fields
     *
     * @return Map
     */
    public static Map<String, String> getCustomerBalanceFields() {
        Map<String, String> values = new TreeMap<>();

        values.put(ET_COMPANY_NAME, "Company Name");
        values.put(ET_CUSTOMER, "Cliet Name");
        values.put(ET_CUSTOMER_BALANCE, "Customer Balance");

        return values;
    }

    /**
     * Related Supplier Balance category fields
     *
     * @return Map
     */
    public static Map<String, String> getSupplierBalanceFields() {
        Map<String, String> values = new TreeMap<>();

        values.put(ET_COMPANY_NAME, "Company Name");
        values.put(ET_SUPPLIER, "Supplier Name");
        values.put(ET_SUPPLIER_BALANCE, "Customer Balance");

        return values;
    }

    /**
     * Related Expense claim category fields
     *
     * @return Map
     */
    public static Map<String, String> getExpencesClaimCategoryFields() {
        Map<String, String> personalAttMap = new LinkedHashMap<>();
        personalAttMap.put(ET_EXPENSE_REPORTER_FIRST_NAME, "Reporter First Name");
        personalAttMap.put(ET_EXPENSE_REPORTER_LAST_NAME, "Reporter Last Name");
        personalAttMap.put(ET_EXPENSE_APPROVER_FIRST_NAME, "Approver First Name");
        personalAttMap.put(ET_EXPENSE_APPROVER_LAST_NAME, "Approver Last Name");
        personalAttMap.put(ET_EXPENSE_REPORT_TITLE, "Report Title");
        personalAttMap.put(ET_EXPENSE_REPORT_AMOUNT, "Expense Amount");
        personalAttMap.put(ET_START_DATE, "Start Date");
        personalAttMap.put(ET_END_DATE, "End Date");
        personalAttMap.put(ET_EXPENSE_SUBMIT_DATE, "Submit Date");
        personalAttMap.put(ET_EXPENSE_SHORT_LINK, "Report Link");
        personalAttMap.put(ET_PRODUCT_NAME, "Product name");
        personalAttMap.put(ET_SIGNATURE, "Your signature");
//        personalAttMap.put("<a href='"+ ET_EXPENSE_SHORT_LINK + "'>View Expense Claims</a>", "Short Link");
        return personalAttMap;
    }

    /**
     * Related Batch Payment category fields
     *
     * @return Map
     */
    public static Map<String, String> getBatchPaymentCategoryFields() {
        Map<String, String> personalAttMap = new LinkedHashMap<>();
        personalAttMap.put(ET_PAYMENT_APPROVER_FIRST_NAME, "Approver First Name");
        personalAttMap.put(ET_PAYMENT_APPROVER_LAST_NAME, "Approver Last Name");
        personalAttMap.put(ET_PAYMENT_APPROVER_EMAIL, "Approver Email");
        personalAttMap.put(ET_PAYMENT_COMPANY_NAME, "Company Name");
        personalAttMap.put(ET_PAYMENT_ACCOUNT_NAME, "Expense Amount");
        personalAttMap.put(ET_PAYMENT_DATE, "Payment Date");
        personalAttMap.put(ET_PAYMENT_AMOUNT, "Payment Amount");
        personalAttMap.put(ET_PAYMENT_TYPE, "Payment Type");
        personalAttMap.put(ET_PAYMENT_LINK, "Payment Link");
        personalAttMap.put(ET_PAYMENT_NUMBER, "Payment Number");
        personalAttMap.put(ET_PAYMENT_PRODUCT, "Payment Product");
        return personalAttMap;
    }

    /**
     * Related Sale quote category fields
     *
     * @return Map
     */
    public static Map<String, String> getSaleQuoteCategoryFields() {
        Map<String, String> personalAttrMap = getAccountingBaseFields();
        personalAttrMap.put(ET_START_DATE, "Quote Date");
        personalAttrMap.put(ACCOUNTING.REFERENCE, "Reference");
        return personalAttrMap;
    }

    /**
     * Related Sale order category fields
     *
     * @return Map
     */
    public static Map<String, String> getSaleOrderCategoryFields() {
        Map<String, String> personalAttrMap = getAccountingBaseFields();
        personalAttrMap.put(ET_START_DATE, "Order Date");
        return personalAttrMap;
    }

    /**
     * Related Sale quote category fields
     *
     * @return Map
     */
    public static Map<String, String> getSaleQuoteManagerCategoryFields() {
        Map<String, String> personalAttrMap = getAccountingBaseFields();
        personalAttrMap.put(ET_START_DATE, "Quote Date");
        personalAttrMap.put(ACCOUNTING.REFERENCE, "Reference");
        return personalAttrMap;
    }

    /**
     * Related Purchace order category fields
     *
     * @return Map
     */
    public static Map<String, String> getPuchaseOrderCategoryFields() {
        Map<String, String> personalAttrMap = getAccountingBaseFields();
        personalAttrMap.remove(ACCOUNTING.PO_NUMBER);
        personalAttrMap.put(ET_START_DATE, "Order Date");
        personalAttrMap.put(ET_NAME, "Contact employee name");
        personalAttrMap.put(ET_PROJECT_NUMBER, "Project Number");
        personalAttrMap.put(ET_SUPPLIER_BALANCE, "Supplier Balance");
        personalAttrMap.put(ET_LINK, "Link");
        return personalAttrMap;
    }

    /**
     * Related Purchace order Manager category fields
     *
     * @return Map
     */
    public static Map<String, String> getPuchaseOrderManagerCategoryFields() {
        Map<String, String> personalAttrMap = getAccountingBaseFields();
        personalAttrMap.remove(ET_CUSTOMER);
        personalAttrMap.put(ACCOUNTING.SUPPLIER, "Supplier Name");
        personalAttrMap.put(ET_START_DATE, "Order Date");
        personalAttrMap.put(ET_NAME, "Contact employee name");
        personalAttrMap.put(ET_PROJECT_NUMBER, "Project Number");
        personalAttrMap.put(ET_SUPPLIER_BALANCE, "Supplier Balance");
        personalAttrMap.put(ET_TOTAL_AMOUNT, "Total Amount");
        return personalAttrMap;
    }

    /**
     * Related Credit Note category fields
     *
     * @return Map
     */
    public static Map<String, String> getCreditNoteCategoryFields() {
        Map<String, String> personalAttrMap = getAccountingBaseFields();
        personalAttrMap.put(ET_START_DATE, "Credit Note Date");
        return personalAttrMap;
    }

    /**
     * Related Receipt category fields
     *
     * @return Map
     */
    public static Map<String, String> getReceiptCategoryFields() {
        Map<String, String> personalAttrMap = getAccountingBaseFields();
        personalAttrMap.put(ET_START_DATE, "Invoice Date");
        personalAttrMap.put(ET_PAID_AMOUNT, "paidAmount");
        return personalAttrMap;
    }

    /**
     * Related Replied case category fields
     *
     * @return Map
     */
    public static Map<String, String> getCaseModuleFields() {
        Map<String, String> personalAttMap = new LinkedHashMap<>();
        personalAttMap.put(ET_CASE_NUMBER, "Case Number");
        personalAttMap.put(ET_CASE_SUBJECT, "Case Subject");
        personalAttMap.put(ET_CASE_TYPE, "Case Type");
        personalAttMap.put(ET_CASE_ORIGIN, "Case Origin");
        personalAttMap.put(ET_CASE_REASON, "Case Reason");
        personalAttMap.put(ET_CASE_ASSIGNEE, "Case Assignee");
        personalAttMap.put(ET_CASE_REPORTER, "Reporter Name");
        personalAttMap.put(ET_SIGNATURE, "Your signature");
        return personalAttMap;
    }

    public static Map<String, String> getCourseScheduleFields() {
        Map<String, String> personalAttMap = new LinkedHashMap<>();
        personalAttMap.put(TRAINING_CENTER.SCHEDULED_COURSE.CS_STUDENT_EMAILS, "CS Student Emails");
        personalAttMap.put(TRAINING_CENTER.SCHEDULED_COURSE.CS_STUDENT_PHONES, "CS Student Phones");
        personalAttMap.put(TRAINING_CENTER.SCHEDULED_COURSE.CS_STUDENTS, "Course Schedule Students");
        personalAttMap.put(TRAINING_CENTER.SCHEDULED_COURSE.ASSESSOR, "Assessor");
        personalAttMap.put(TRAINING_CENTER.SCHEDULED_COURSE.COURSE, "Course");
        personalAttMap.put(TRAINING_CENTER.SCHEDULED_COURSE.INSTRUCTOR, "Instructor");
        personalAttMap.put(TRAINING_CENTER.SCHEDULED_COURSE.LANGUAGE, "Language");
        personalAttMap.put(TRAINING_CENTER.SCHEDULED_COURSE.LOCATION, "Location");
        personalAttMap.put(TRAINING_CENTER.SCHEDULED_COURSE.NUMBER_OF_SEATS, "Number of seats");
        personalAttMap.put(TRAINING_CENTER.SCHEDULED_COURSE.START_DATE, "Start Date");
        personalAttMap.put(ET_SIGNATURE, "Your signature");
        return personalAttMap;
    }

    public static Map<String, String> getCourseBookingConfirmationFields() {
        Map<String, String> personalAttMap = new LinkedHashMap<>();
        personalAttMap.put(ET_EMPLOYEE_USER_FULLNAME, "Course Booking Manager");
        personalAttMap.put(TRAINING_CENTER.COURSE_BOOKING_LINK, "Course Booking Link");
        personalAttMap.put(ET_SIGNATURE, "Your signature");
        return personalAttMap;
    }

    public static Map<String, String> getCalendarEventCategoryFields() {
        Map<String, String> addEventAttMap = getCalendarEventDeletedCategoryFields();
        addEventAttMap.put(ET_CREATOR, "Creator");
        addEventAttMap.put(ET_SHARED_EMPLOYEES, "Shared employees");
        addEventAttMap.put(ET_GUESTS, "Guests");
        addEventAttMap.put(ET_URL, "Event URL");
        addEventAttMap.put(ET_SIGNATURE, "Your signature");
        return addEventAttMap;
    }

    /**
     * Related Calendar event update category fields
     *
     * @return Map
     */
    public static Map<String, String> getCalendarEventUpdateCategoryFields() {
        Map<String, String> addEventAttMap = getCalendarEventDeletedCategoryFields();
        addEventAttMap.put(ET_CREATOR, "Creator");
        addEventAttMap.put(ET_SHARED_EMPLOYEES, "Shared employees");
        addEventAttMap.put(ET_URL, "Event URL");
        addEventAttMap.put(ET_SIGNATURE, "Your signature");
        return addEventAttMap;
    }

    /**
     * Related Calendar event reminder category fields
     *
     * @return Map
     */
    public static Map<String, String> getCalendarEventReminderCategoryFields() {
        Map<String, String> addEventAttMap = getCalendarEventDeletedCategoryFields();
        addEventAttMap.put(ET_URL, "Event URL");
        addEventAttMap.put(ET_SIGNATURE, "Your signature");
        return addEventAttMap;
    }

    /**
     * Related Calendar event delete category fields
     *
     * @return Map
     */
    public static Map<String, String> getCalendarEventDeletedCategoryFields() {
        Map<String, String> addEventAttMap = new LinkedHashMap<>();
        addEventAttMap.put(ET_NAME, "Event name");
        addEventAttMap.put(ET_DESCRIPTION, "Description");
        addEventAttMap.put(ET_LOCATION, "Location");
        addEventAttMap.put(ET_WHEN, "When");
        addEventAttMap.put(ET_SIGNATURE, "Your signature");
        return addEventAttMap;
    }

    /**
     * Related Task assignees category fields
     *
     * @return Map
     */
    public static Map<String, String> getTaskAssignCategoryFields() {
        Map<String, String> taskAssignAttMap = new LinkedHashMap<>();
        taskAssignAttMap.put(ET_TASK_NUMBER, "Task Number");
        taskAssignAttMap.put(ET_FIRST_NAME, "First name");
        taskAssignAttMap.put(ET_LAST_NAME, "Last name");
        taskAssignAttMap.put(ET_TASK_NAME, "Task name");
        taskAssignAttMap.put(ET_DESCRIPTION, "Description");
        taskAssignAttMap.put(ET_PRIORITY, "Priority");
        taskAssignAttMap.put(ET_STATUS, "Status");
        taskAssignAttMap.put(ET_COMPLETED, "% completed");
        taskAssignAttMap.put(ET_CREATOR, "Creator");
        taskAssignAttMap.put(ET_PROJECT_NAME, "Project name");
        taskAssignAttMap.put(ET_CUSTOMER, "Client");
        taskAssignAttMap.put(ET_ASSIGNEES, "Assignee(s)");
        taskAssignAttMap.put(ET_START_DATE, "Start date");
        taskAssignAttMap.put(ET_DUE_DATE, "Due date");
        taskAssignAttMap.put(ET_DATE, "Creation date");
        taskAssignAttMap.put(ET_ESTIMATED_TIME, "Estimated time");
        taskAssignAttMap.put(ET_URL, "Task URL");
        taskAssignAttMap.put(ET_SIGNATURE, "Your signature");
        return taskAssignAttMap;
    }

    /**
     * Related Project assignees category fields
     *
     * @return Map
     */
    public static Map<String, String> getProjectAssignCategoryFields() {
        Map<String, String> taskAssignAttMap = new LinkedHashMap<>();
        taskAssignAttMap.put(ET_PROJECT_NUMBER, "Project Number");
        taskAssignAttMap.put(ET_FIRST_NAME, "Assignee First name");
        taskAssignAttMap.put(ET_LAST_NAME, "Assignee Last name");
        taskAssignAttMap.put(ET_PROJECT_NAME, "Project name");
        taskAssignAttMap.put(ET_MANAGER_NAME, "Manager name");
        taskAssignAttMap.put(ET_CREATOR, "Creator");
        taskAssignAttMap.put(ET_DATE, "Creation date");
        taskAssignAttMap.put(ET_URL, "Project URL");
        taskAssignAttMap.put(ET_PRODUCT_NAME, "Product name");
        taskAssignAttMap.put(ET_COMPANY_INFO, "Company info");
        taskAssignAttMap.put(ET_SIGNATURE, "Your signature");
        return taskAssignAttMap;
    }

    /**
     * Related Project Client Approvement (Genesis) category fields
     *
     * @return Map
     */
    public static Map<String, String> getProjectClientApproveCategoryFields() {
        Map<String, String> taskAssignAttMap = new LinkedHashMap<>();
        taskAssignAttMap.put(ET_PROJECT_NUMBER, "Project Number");
        taskAssignAttMap.put(ET_NAME, "Client Name");
        taskAssignAttMap.put(ET_PROJECT_NAME, "Project name");
        taskAssignAttMap.put(ET_MANAGER_NAME, "Manager name");
        taskAssignAttMap.put(ET_RECIPIENT_EMAIL, "Client Primary Email");
        taskAssignAttMap.put(ET_CREATOR, "Creator");
        taskAssignAttMap.put(ET_DATE, "Approve/Reject date");
        taskAssignAttMap.put(ET_URL, "Project URL");
        taskAssignAttMap.put(ET_DESCRIPTION, "Description");
        taskAssignAttMap.put(ET_PRODUCT_NAME, "Product name");
        taskAssignAttMap.put(ET_COMPANY_INFO, "Company info");
        taskAssignAttMap.put(ET_SIGNATURE, "Your signature");
        return taskAssignAttMap;
    }

    /**
     * Related Message Center category fields
     *
     * @return Map
     */
    public static Map<String, String> getMessageCenterCategoryFields() {
        Map<String, String> taskAssignAttMap = new LinkedHashMap<>();
        taskAssignAttMap.put(ET_RECIPIENT_FIRST_NAME, "Recipient first name");
        taskAssignAttMap.put(ET_RECIPIENT_LAST_NAME, "Recipient last name");
        taskAssignAttMap.put(ET_RECIPIENT_EMAIL, "Recipient email");
        taskAssignAttMap.put(ET_RECIPIENT_COMPANY_NAME, "Recipient Company name");
        taskAssignAttMap.put(ET_SENDER_FIRST_NAME, "Sender first name");
        taskAssignAttMap.put(ET_SENDER_LAST_NAME, "Sender last name");
        taskAssignAttMap.put(ET_SENDER_EMAIL, "Sender email");
        taskAssignAttMap.put(ET_SENDER_COMPANY_NAME, "Sender Company name");
        taskAssignAttMap.put(ET_SENDER_PRIMARY_PHONE, "Sender Primary phone");
        taskAssignAttMap.put(ET_SENDER_MOBILE_PHONE, "Sender Mobile phone");
        taskAssignAttMap.put(ET_SENDER_JOB_TITLE, "Sender Job title");
        taskAssignAttMap.put(ET_SIGNATURE, "Your signature");
        return taskAssignAttMap;
    }

    /**
     * Related Google Contact Sync category fields
     *
     * @return Map
     */
    public static Map<String, String> getGoogleContactSynCategoryFields() {
        Map<String, String> googleContactSyn = new LinkedHashMap<>();
        googleContactSyn.put(ET_USER_NAME, "User name");
        googleContactSyn.put(ET_PRODUCT_NAME, "Product name");
        googleContactSyn.put(ET_SIGNATURE, "Your signature");
        return googleContactSyn;
    }

    /*
      Related Leave request to employee category fields
      @return Map
     Send to approver notification has been moved to Workflow while creating Leave Request - Faxriddin Taslimov
     */

//    public static Map<String, String> getLeaveRequestToEmployeeCategoryFields() {
//        Map<String, String> values = new LinkedHashMap<String, String>();
//        values.put(EmailTemplateUtils.ET_SENDER_EMPLOYEE_FULLNAME, "Sender");
//        values.put(EmailTemplateUtils.ET_REQUEST_EMPLOYEE_FULLNAME, "Employee");
//        values.put(EmailTemplateUtils.ET_REQUEST_APPROVER_FULLNAME, "Manager");
//        values.put(EmailTemplateUtils.ET_STATUS, "Status");
//        values.put(EmailTemplateUtils.ET_QUEST_TYPE, "Request Type");
//        values.put(EmailTemplateUtils.ET_REQUEST_REASON_NAME, "Reason");
//        values.put(EmailTemplateUtils.ET_REQUEST_DESCRIPTION, "Description");
//        values.put(EmailTemplateUtils.ET_START_DATE, "Start date");
//        values.put(EmailTemplateUtils.ET_END_DATE, "End date");
//        values.put(EmailTemplateUtils.ET_LINK, "Link");
//        values.put(ET_SIGNATURE, "Your signature");
//        return values;
//    }

    /*
     * Related Leave Request to admin category fields
     *
     * @return Map
     *  Send to approver notification has been moved to Workflow while creating Leave Request - Faxriddin Taslimov
     */
//    public static Map<String, String> getLeaveRequestToAdminCategoryFields() {
//        Map<String, String> values = new LinkedHashMap<String, String>();
//        values.put(EmailTemplateUtils.ET_REQUEST_APPROVER_FULLNAME, "Manager");
//        values.put(EmailTemplateUtils.ET_SENDER_EMPLOYEE_FULLNAME, "Creator");
//        values.put(EmailTemplateUtils.ET_REQUEST_EMPLOYEE_FULLNAME, "Employee");
//        values.put(EmailTemplateUtils.ET_QUEST_TYPE, "Request Type");
//        values.put(EmailTemplateUtils.ET_REQUEST_REASON_NAME, "Reason");
//        values.put(EmailTemplateUtils.ET_REQUEST_DESCRIPTION, "Description");
//        values.put(EmailTemplateUtils.ET_START_DATE, "Start date");
//        values.put(EmailTemplateUtils.ET_END_DATE, "End date");
//        values.put(EmailTemplateUtils.ET_LINK, "Link");
//        values.put(EmailTemplateUtils.ET_COMPANY_NAME, "companyName");
//        values.put(ET_SIGNATURE, "Your signature");
//        return values;
//    }

    /**
     * Related Client activation email to new user category fields
     *
     * @return Map
     */
    public static Map<String, String> getClientActivationNewUserCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateUtils.ET_FIRST_NAME, "firstName");
        values.put(EmailTemplateUtils.ET_LAST_NAME, "lastName");
        values.put(EmailTemplateUtils.ET_MANAGER_NAME, "managerFullName");
        values.put(EmailTemplateUtils.ET_ACTIVATION_LINK, "Activation Link");
        values.put(EmailTemplateUtils.ET_COMPANY_NAME, "companyName");
        values.put(EmailTemplateUtils.ET_PRODUCT_NAME, "Product name");
        values.put(EmailTemplateUtils.ET_COMPANY_INFO, "companyInfo");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    /**
     * Related Client activation email to existing user category fields
     *
     * @return Map
     */
    public static Map<String, String> getClientActivationExistingUserCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateUtils.ET_FIRST_NAME, "firstName");
        values.put(EmailTemplateUtils.ET_LAST_NAME, "lastName");
        values.put(EmailTemplateUtils.ET_MANAGER_NAME, "managerFullName");
        values.put(EmailTemplateUtils.ET_LOGIN_PAGE_LINK, "link");
        values.put(EmailTemplateUtils.ET_COMPANY_NAME, "companyName");
        values.put(EmailTemplateUtils.ET_COMPANY_INFO, "companyInfo");
        values.put(EmailTemplateUtils.ET_PRODUCT_NAME, "Product Name");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    /**
     * Related Employee activation email to new user category fields
     *
     * @return Map
     */
    public static Map<String, String> getEmployeeActivationNewUserCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateUtils.ET_FIRST_NAME, "firstName");
        values.put(EmailTemplateUtils.ET_LAST_NAME, "lastName");
        values.put(EmailTemplateUtils.ET_MANAGER_NAME, "managerFullName");
        values.put(EmailTemplateUtils.ET_ACTIVATION_LINK, "Activation link");
        values.put(EmailTemplateUtils.ET_COMPANY_NAME, "companyName");
        values.put(EmailTemplateUtils.ET_COMPANY_INFO, "companyInfo");
        values.put(EmailTemplateUtils.ET_LOGIN_PAGE_LINK, "Login Page link");
        values.put(EmailTemplateUtils.ET_PRODUCT_NAME, "Product Name");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    /**
     * Related Employee activation email to existing user category fields
     *
     * @return Map
     */
    public static Map<String, String> getEmployeeActivationExistingUserCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateUtils.ET_FIRST_NAME, "firstName");
        values.put(EmailTemplateUtils.ET_LAST_NAME, "lastName");
        values.put(EmailTemplateUtils.ET_MANAGER_NAME, "managerFullName");
        values.put(EmailTemplateUtils.ET_LOGIN_PAGE_LINK, "host");
        values.put(EmailTemplateUtils.ET_COMPANY_NAME, "companyName");
        values.put(EmailTemplateUtils.ET_COMPANY_INFO, "companyInfo");
        values.put(EmailTemplateUtils.ET_PRODUCT_NAME, "productname");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    /**
     * Related User account confirmation category fields
     *
     * @return Map
     */
    public static Map<String, String> getUserAccountConfirmationCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateUtils.ET_FIRST_NAME, "firstName");
        values.put(EmailTemplateUtils.ET_LAST_NAME, "lastName");
        values.put(EmailTemplateUtils.ET_EMPLOYEE_USER_NAME, "User name");
        values.put(EmailTemplateUtils.ET_EMPLOYEE_USER_PASSWORD, "Password");
        values.put(EmailTemplateUtils.ET_LOGIN_PAGE_LINK, "host");
        values.put(EmailTemplateUtils.ET_COMPANY_NAME, "companyName");
        values.put(EmailTemplateUtils.ET_PRODUCT_NAME, "Product Name");
        values.put(EmailTemplateUtils.ET_COMPANY_INFO, "companyInfo");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    /**
     * Related Employee activated by manager category fields
     *
     * @return Map
     */
    public static Map<String, String> getEmployeeActivatedByManagerCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateUtils.ET_FIRST_NAME, "firstName");
        values.put(EmailTemplateUtils.ET_LAST_NAME, "lastName");
        values.put(EmailTemplateUtils.ET_MANAGER_NAME, "managerFullName");
        values.put(EmailTemplateUtils.ET_USER_NAME, "userName");
        values.put(EmailTemplateUtils.ET_EMPLOYEE_USER_PASSWORD, "password");
        values.put(EmailTemplateUtils.ET_LOGIN_PAGE_LINK, "link");
        values.put(EmailTemplateUtils.ET_COMPANY_NAME, "companyName");
        values.put(EmailTemplateUtils.ET_COMPANY_INFO, "companyInfo");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    /**
     * Related Overdue reminder for client category fields
     *
     * @return Map
     */
    public static Map<String, String> getOverdueReminderCategoryForClientFields() {

        Map<String, String> values = new LinkedHashMap<>();
        values.put(ET_CUSTOMER, "clientName");
        values.put(ET_COMPANY_NAME, "companyName");
        values.put(ET_NUMBER, "invoiceNumber");
        values.put(ET_START_DATE, "date");
        values.put(ET_DUE_DATE, "dueDate");
        values.put(ET_TOTAL_AMOUNT, "totalAmount");
        values.put(ET_PAID_AMOUNT, "paidAmount");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    /**
     * Related Issue add assign update category fields
     *
     * @return Map
     */
    public static Map<String, String> getIssueAddAssignUpdateCategoryFields() {
        Map<String, String> values = getIssueDeleteCategoryFields();
        values.put(EmailTemplateUtils.ET_URL, "url");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    /**
     * Related Issue delete category fields
     *
     * @return Map
     */
    public static Map<String, String> getIssueDeleteCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateUtils.ET_ISSUE_NUMBER, "issueNumber");
        values.put(EmailTemplateUtils.ET_USER_NAME, "userName");
        values.put(EmailTemplateUtils.ET_RESOLVER_NAME, "resolverName");
        values.put(EmailTemplateUtils.ET_CREATOR, "creator");
        values.put(EmailTemplateUtils.ET_ISSUE_NAME, "issueName");
        values.put(EmailTemplateUtils.ET_DESCRIPTION, "description");
        values.put(EmailTemplateUtils.ET_START_DATE, "startDate");
        values.put(EmailTemplateUtils.ET_END_DATE, "endDate");
        values.put(EmailTemplateUtils.ET_STATUS, "issueStatus");
        values.put(EmailTemplateUtils.ET_PRIORITY, "issuePriority");
        values.put(EmailTemplateUtils.ET_REPORTED_BY, "issueReportedBy");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    /**
     * Related Web form category fields
     *
     * @return Map
     */
    public static Map<String, String> getWebFormCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateUtils.ET_FIRST_NAME, "submitterFirstName");
        values.put(EmailTemplateUtils.ET_LAST_NAME, "submitterLastName");
        values.put(EmailTemplateUtils.ET_EMAIL, "submitterEmail");
        values.put(EmailTemplateUtils.ET_COMPANY_NAME, "companyName");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    /**
     * Related Multi task assign category fields
     *
     * @return Map
     */
    public static Map<String, String> getMultTaskAssignCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateUtils.ET_FIRST_NAME, "receiverFirstName");
        values.put(EmailTemplateUtils.ET_LAST_NAME, "receiverLastName");
        values.put(EmailTemplateUtils.ET_EMAIL, "receiverEmail");
        values.put(EmailTemplateUtils.ET_CREATOR, "creator");
        values.put(EmailTemplateUtils.ET_DATE, "date");
        values.put(EmailTemplateUtils.ET_TASKS, "tasks");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    public static Map<String, String> getActualTimeReachedFields() {
        Map<String, String> taskAssignAttMap = new LinkedHashMap<>();
        taskAssignAttMap.put(ET_TASK_NUMBER, "Task Number");
        taskAssignAttMap.put(ET_TASK_NAME, "Task name");
        taskAssignAttMap.put(ET_RECEIVER_FULL_NAME, "Receiver Name");
        taskAssignAttMap.put(ET_DESCRIPTION, "Description");
        taskAssignAttMap.put(ET_PRIORITY, "Priority");
        taskAssignAttMap.put(ET_STATUS, "Status");
        taskAssignAttMap.put(ET_COMPLETED, "% completed");
        taskAssignAttMap.put(ET_CREATOR, "Creator");
        taskAssignAttMap.put(ET_PROJECT_NAME, "Project name");
        taskAssignAttMap.put(ET_CUSTOMER, "Client");
        taskAssignAttMap.put(ET_START_DATE, "Start date");
        taskAssignAttMap.put(ET_DUE_DATE, "Due date");
        taskAssignAttMap.put(ET_DATE, "Creation date");
        taskAssignAttMap.put(ET_ESTIMATED_TIME, "Estimated time");
        taskAssignAttMap.put(ET_URL, "Task URL");
        taskAssignAttMap.put(ET_SIGNATURE, "Your signature");
        return taskAssignAttMap;
    }

    /**
     * Related Calendar invitation add category fields
     *
     * @return Map
     */
    public static Map<String, String> getCalendarInvitationAddCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("${USER}", "userFullName");
        values.put("${EVENT_NAME}", "eventName");
        values.put("${SUBJECT}", "subject");
        values.put("${DESCRIPTION}", "description");
        values.put("${WHERE}", "where");
        values.put("${DATE}", "date");
        values.put("${CREATOR_NAME}", "creatorName");
        values.put("${SHARED_WITH}", "sharedWith");
        values.put("${GUESTS}", "guests");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    /**
     * Related Calendar invitation edit category fields
     *
     * @return Map
     */
    public static Map<String, String> getCalendarInvitationEditCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("${USER}", "userFullName");
        values.put("${EVENT_NAME}", "eventName");
        values.put("${SUBJECT}", "subject");
        values.put("${DESCRIPTION}", "description");
        values.put("${WHERE}", "where");
        values.put("${DATE}", "date");
        values.put("${CREATOR_NAME}", "creatorName");
        values.put("${SHARED_WITH}", "sharedWith");
        values.put("${GUESTS}", "guests");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    /**
     * Related Calendar invitation delete category fields
     *
     * @return Map
     */
    public static Map<String, String> getCalendarInvitationDeleteCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("${USER}", "userFullName");
        values.put("${EVENT_NAME}", "eventName");
        values.put("${SUBJECT}", "subject");
        values.put("${DESCRIPTION}", "description");
        values.put("${WHERE}", "where");
        values.put("${DATE}", "date");
        values.put("${CREATOR_NAME}", "creatorName");
        values.put("${SHARED_WITH}", "sharedWith");
        values.put("${GUESTS}", "guests");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    /**
     * Related Sms template category fields
     *
     * @return Map
     */
    public static Map<String, String> getSmsTemplate() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateUtils.ET_SENDER_COMPANY_NAME, "senderCompanyName");
        values.put(EmailTemplateUtils.ET_SENDER_EMPLOYEE_FULLNAME, "senderFullName");
        values.put(EmailTemplateUtils.ET_RECEIVE_FULL_NAME, "receiveFullName");
        values.put(EmailTemplateUtils.ET_CREATOR, "creator");
        return values;
    }

    /**
     * Related document upload category fields
     *
     * @return Map
     */
    public static Map<String, String> getDocumentUploadCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateUtils.ET_USER_NAME, "receiverName");
        values.put(EmailTemplateUtils.ET_CREATOR, "creatorName");
        values.put(EmailTemplateUtils.ET_DATE, "creationDate");
        values.put(EmailTemplateUtils.ET_FILE_NAME, "fileName");
        values.put(EmailTemplateUtils.ET_DESCRIPTION, "description");
        values.put(EmailTemplateUtils.ET_RELATED_TO, "relatedToName");
        values.put(EmailTemplateUtils.ET_URL, "linkURL");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    /**
     * Related Crm mass mail list category fields
     *
     * @return Map
     */
    public static Map<String, String> getCrmMailListCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateUtils.ET_SENDER_TITLE, "Sender`s Title");
        values.put(EmailTemplateUtils.ET_SENDER_FIRST_NAME, "Sender`s First Name");
        values.put(EmailTemplateUtils.ET_SENDER_LAST_NAME, "Sender`s Last Name");
        values.put(EmailTemplateUtils.ET_SENDER_COMPANY_NAME, "Sender`s Company Name");
        values.put(EmailTemplateUtils.ET_SENDER_EMAIL, "Sender`s E-mail");
        values.put(EmailTemplateUtils.ET_RECIPIENT_TITLE, "Recipient`s Title");
        values.put(EmailTemplateUtils.ET_RECIPIENT_MOBILE, "Recipient`s Mobile");
        values.put(EmailTemplateUtils.ET_RECIPIENT_FIRST_NAME, "Recipient`s First Name");
        values.put(EmailTemplateUtils.ET_RECIPIENT_LAST_NAME, "Recipient`s Last Name");
        values.put(EmailTemplateUtils.ET_RECIPIENT_FULL_NAME, "Recipient`s Full Name");
        values.put(EmailTemplateUtils.ET_RECIPIENT_COMPANY_NAME, "Recipient`s Company Name");
        values.put(EmailTemplateUtils.ET_RECIPIENT_EMAIL, "Recipient`s E-mail");
        values.put(EmailTemplateUtils.ET_RECIPIENT_PHONE, "Recipient`s Phone");
        return values;
    }

    public static Map<String, String> getCustomerSupplierBalanceCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateUtils.ET_RECIPIENT_FIRST_NAME, "recipientFirstName");
        values.put(EmailTemplateUtils.ET_RECIPIENT_LAST_NAME, "recipientLastName");
        values.put(EmailTemplateUtils.ET_RECIPIENT_EMAIL, "recipientEmail");
        values.put(EmailTemplateUtils.ET_RECIPIENT_COMPANY_NAME, "recipientCompanyName");
        values.put(EmailTemplateUtils.ET_SENDER_FIRST_NAME, "senderFirstName");
        values.put(EmailTemplateUtils.ET_SENDER_LAST_NAME, "senderLastName");
        values.put(EmailTemplateUtils.ET_SENDER_EMAIL, "senderEmail");
        values.put(EmailTemplateUtils.ET_SENDER_COMPANY_NAME, "senderCompanyName");
        values.put(EmailTemplateUtils.ET_SENDER_PRIMARY_PHONE, "senderPrimaryPhone");
        values.put(EmailTemplateUtils.ET_SENDER_MOBILE_PHONE, "senderMobilePhone");
        values.put(EmailTemplateUtils.ET_SENDER_JOB_TITLE, "senderJobTitle");
        values.put(EmailTemplateUtils.ET_CUSTOMER_BALANCE, "customerBalance");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    public static Map<String, String> getSupplierBalanceCategoryFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateUtils.ET_RECIPIENT_FIRST_NAME, "recipientFirstName");
        values.put(EmailTemplateUtils.ET_RECIPIENT_LAST_NAME, "recipientLastName");
        values.put(EmailTemplateUtils.ET_RECIPIENT_EMAIL, "recipientEmail");
        values.put(EmailTemplateUtils.ET_RECIPIENT_COMPANY_NAME, "recipientCompanyName");
        values.put(EmailTemplateUtils.ET_SENDER_FIRST_NAME, "senderFirstName");
        values.put(EmailTemplateUtils.ET_SENDER_LAST_NAME, "senderLastName");
        values.put(EmailTemplateUtils.ET_SENDER_EMAIL, "senderEmail");
        values.put(EmailTemplateUtils.ET_SENDER_COMPANY_NAME, "senderCompanyName");
        values.put(EmailTemplateUtils.ET_SENDER_PRIMARY_PHONE, "senderPrimaryPhone");
        values.put(EmailTemplateUtils.ET_SENDER_MOBILE_PHONE, "senderMobilePhone");
        values.put(EmailTemplateUtils.ET_SENDER_JOB_TITLE, "senderJobTitle");
        values.put(EmailTemplateUtils.ET_SUPPLIER_BALANCE, "supplierBalance");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    public static Map<String, String> getHRReminderCategoryFields() {

        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateUtils.REMINDER_TYPE, "reminderType");
        values.put(EmailTemplateUtils.REMINDER_FIELD_VALUE, "reminderfieldValue");
        values.put(EmailTemplateUtils.REMINDER_EMPLOYEES, "reminderEmployees");
        values.put(EmailTemplateUtils.ET_RECEIVER_FULL_NAME, "receiverFullName");
        values.put(EmailTemplateUtils.ET_RECIPIENT_EMAIL, "receiverEmail");
        values.put(EmailTemplateUtils.ET_COMPANY_NAME, "companyname");
        values.put(ET_SIGNATURE, "Your signature");
        return values;
    }

    public static Map<String, String> getMeetingMinutesNotificationFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(MEETING_MINUTES.MT_RECIPIENT, "Recipient`s Full Name");
        values.put(ET_NUMBER, "Meeting Minutes Number");
        values.put(MEETING_MINUTES.MT_TITLE, "Meeting Minutes Title");
        values.put(MEETING_MINUTES.MT_CALLEDBY, "Meeting Minutes Called By");
        values.put(MEETING_MINUTES.MT_LOCATION, "Meeting Minutes Location");
        values.put(MEETING_MINUTES.MT_PURPOSE, "Meeting Minutes Purpose");
        values.put(ET_START_DATE, "Meeting Minutes Start Date");
        values.put(ET_END_DATE, "Meeting Minutes End Date");
        values.put(ET_LINK, "Meeting Minutes Link");
        return values;
    }

    public static Map<String, String> getPenaltyFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateConstants.PENALTIES_PROMOTIONS.PP_EMPLOYEE_NAME, "Employee Name");
        values.put(EmailTemplateConstants.PENALTIES_PROMOTIONS.PP_CREATOR_NAME, "Penalty Creator Name");
        values.put(EmailTemplateConstants.PENALTIES_PROMOTIONS.PP_EFFECTIVE_DATE, "Effective Date");
        values.put(EmailTemplateConstants.PENALTIES_PROMOTIONS.PP_PROJECT_NAME, "Name of Project");
        values.put(EmailTemplateConstants.PENALTIES_PROMOTIONS.PP_MYEMAIL, "Email address current user");
        values.put(EmailTemplateConstants.PENALTIES_PROMOTIONS.PP_REPLYEMAIL, "Email address to reply");
        values.put(EmailTemplateConstants.PENALTIES_PROMOTIONS.PP_POINT_AMOUNT, "Amount of points");
        values.put(PENALTIES_PROMOTIONS.PP_PENALTY_PROMOTION_TYPE_NAME, "word Penalties or Promotions");
        values.put(PENALTIES_PROMOTIONS.PP_NAME, "Name of Penalty");
        values.put(EmailTemplateConstants.PENALTIES_PROMOTIONS.PP_AMOUNT, "Amount of Penalty");
        values.put(PENALTIES_PROMOTIONS.PP_ASSIGNED_DATE, "penalty/promotion assigned date");
        return values;
    }

    public static Map<String, String> getPromotionFields() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateConstants.PENALTIES_PROMOTIONS.PP_EMPLOYEE_NAME, "Employee Name");
        values.put(EmailTemplateConstants.PENALTIES_PROMOTIONS.PP_CREATOR_NAME, "Promotion Creator Name");
        values.put(EmailTemplateConstants.PENALTIES_PROMOTIONS.PP_EFFECTIVE_DATE, "Effective Date");
        values.put(EmailTemplateConstants.PENALTIES_PROMOTIONS.PP_PROJECT_NAME, "Name of Project");
        values.put(EmailTemplateConstants.PENALTIES_PROMOTIONS.PP_MYEMAIL, "Email address current user");
        values.put(EmailTemplateConstants.PENALTIES_PROMOTIONS.PP_REPLYEMAIL, "Email address to reply");
        values.put(EmailTemplateConstants.PENALTIES_PROMOTIONS.PP_POINT_AMOUNT, "Amount of points");
        values.put(PENALTIES_PROMOTIONS.PP_PENALTY_PROMOTION_TYPE_NAME, "word Penalties or Promotions");
        values.put(PENALTIES_PROMOTIONS.PP_NAME, "Name of penalty or promotion");
        values.put(PENALTIES_PROMOTIONS.PP_ASSIGNED_DATE, "penalty/promotion assigned date");
        return values;
    }

    public static Map<String, String> getContractReminderAttributes() {
        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put(CONTRACT_REMINDER_VALUES.CC_CONTRACT_CREATOR, "Contract Creator Name");
        attributes.put(CONTRACT_REMINDER_VALUES.CC_CONTRACT_NUMBER, "Contract Number");
        attributes.put(CONTRACT_REMINDER_VALUES.CC_EMPLOYEE_NAME, "Contract Reminder Receiver Name");
        attributes.put(CONTRACT_REMINDER_VALUES.CC_CONTRACT_LEFT_TIME_TO_DUE_DATE, "Contract Left Time To Due Date");
        attributes.put(CONTRACT_REMINDER_VALUES.CC_CONTRACT_DUE_DATE, "Contract Due Date");
        attributes.put(ET_LINK, "Link to contract summary");
        attributes.put(CC_CONTRACT_CUSTOMER, "Contract Customer");
        return attributes;
    }

    public static Map<String, String> getTaskReminderAttributes() {
        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put(ET_STATUS, "Status");
        attributes.put(ET_PRIORITY, "Priority");
        attributes.put(TASK_REMINDER_VALUES.OVERTIME, "Overtime");
        attributes.put(TASK_REMINDER_VALUES.HOST, "Host");
        attributes.put(TASK_REMINDER_VALUES.EMPLOYEE_TASK, "Employee Task");
        attributes.put(TASK_REMINDER_VALUES.USER, "User");
        attributes.put(TASK_REMINDER_VALUES.PROJECT_NAME, "Project Name");
        attributes.put(ET_LINK, "Link");
        attributes.put(TASK_REMINDER_VALUES.ASSIGNEES, "Assignees");
        attributes.put(ET_START_DATE, "Start Date");
        attributes.put(ET_DUE_DATE, "Due Date");
        attributes.put(TASK_REMINDER_VALUES.DATE, "Date");
        attributes.put(TASK_REMINDER_VALUES.ESTIMATED_TIME, "Estimated Date");
        attributes.put(TASK_REMINDER_VALUES.COMPLETED, "Completed");
        attributes.put(ET_CUSTOMER, "Client Name");

        return attributes;
    }

    public static Map<String, String> getPayslipApprovedAttributes() {
        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put(PAYROLL.PAYSLIP_APPROVED.EMP_FIRST_NAME, "Employee First Name");
        attributes.put(PAYROLL.PAYSLIP_APPROVED.EMP_LAST_NAME, "Employee Last Name");
        attributes.put(PAYROLL.PAYSLIP_APPROVED.APPROVER_FIRST_NAME, "Approver First Name");
        attributes.put(PAYROLL.PAYSLIP_APPROVED.APPROVER_LAST_NAME, "Approver Last Name");
        attributes.put(PAYROLL.PAYSLIP_APPROVED.MONTH, "Month");
        attributes.put(PAYROLL.PAYSLIP_APPROVED.YEAR, "Year");
        attributes.put(PAYROLL.PAYSLIP_APPROVED.APPROVED_DATE, "Approved Date");
        return attributes;
    }

    public static Map<String, String> getBillOfMaterialsAttributes() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(EmailTemplateConstants.BILL_OF_MATERIALS_VALUES.CURRENT_USER, "currentUser");
        values.put(EmailTemplateConstants.BILL_OF_MATERIALS_VALUES.RECEPIENT, "recipientname");
        values.put(EmailTemplateConstants.BILL_OF_MATERIALS_VALUES.DATE, "date");
        values.put(EmailTemplateConstants.BILL_OF_MATERIALS_VALUES.PROJECT_NAME, "projectName");
        values.put(EmailTemplateConstants.BILL_OF_MATERIALS_VALUES.REJECTION_REASON, "rejectionReason");
        values.put(EmailTemplateConstants.BILL_OF_MATERIALS_VALUES.START_DATE, "startdate");
        return values;
    }

}