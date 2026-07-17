package com.edatasite.workforce.gwt.core.client.ui.lookup;

/**
 * User: Abdullo
 * Date: 9/13/11
 * Time: 5:38 PM
 */
public interface LookUpConstants {

    String CRM_ACCOUNT = "account";
    String CLIENT = "client";
    String SUPPLIER = "supplier";
    String VENDOR = "vendor";
    String CRM_CASE = "case";
    String CRM_CONTACT = "contact";
    String CANDIDATE = "candidate";
    String CRM_LEAD = "lead";
    String CRM_OPPORTUNITY = "opportunity";
    String CAMPAIGN = "campaign";
    String CRM_RFQ = "requestforquote";
    String TIMESLOT = "timeSlot";

    String CRM_EVENT = "event";
    String CRM_EVENT_CALLOG = "callLog";


    String SENT = "sent";

    String INBOUND = "inbound";

    String OUTBOUND = "outbound";
    String CRM_EVENT_INTERVIEW = "INTERVIEW";
    String CRM_TASK = "task";
    String CRM_TASK_SHORTEN = "crmtask2";
    String COURCE_SCHEDULE = "COURCE_SCHEDULE";
    String COURSE_PASSED_STUDENT = "COURSE_PASSED_STUDENT";
    String STUDENT = "STUDENT";
    String EMAIL_FILTER = "EMAIL_FILTER";

    String SMS = "SMS";
    String MASS_MAIL = "MASS_MAIL";
    String PROJECT = "PROJECT";
    String TASK_ASSIGNEE = "TASK_ASSIGNEE";
    String TASK = "TASK";
    String ISSUE = "ISSUE";
    String EMAIL = "EMAIL";
    String EMPLOYEE = "employee";
    String HRMS_EMPLOYEE = "HRMS_EMPLOYEE";
    String BOOKING = "BOOKING";
    String SALEQUOTE = "salequote";
    String PURCHASEINVOICE = "purchaseinvoice";
    String PURCHASEORDER = "purchaseorder";
    String EXPENSECLAIM = "expenseclaim";
    String REQUEST_FOR_PURCHASE = "REQUEST_FOR_PURCHASE";
    String REQUEST_FOR_QUOTE = "REQUEST_FOR_QUOTE";
    String STOCK_TRANSFER = "STOCK_TRANSFER";
    String STOCK_ADJUSTMENT = "STOCK_ADJUSTMENT";
    String SALEINVOICE = "saleinvoice";
    String SALESINVOICE = "salesinvoice";
    String SALEORDER = "saleorder";
    String PRODUCT = "product";
    String DEPARTMENT = "department";
    String POSITION = "position";
    String ROLE = "role";
    String TYPE_PURCHASE_ORDER = "TYPE_PURCHASE_ORDER";
    String TYPE_CUSTOMER_PREPAYMENTS = "TYPE_CUSTOMER_PREPAYMENTS";
    String TYPE_SUPPLIER_PREPAYMENTS = "TYPE_SUPPLIER_PREPAYMENTS";
    String TYPE_GDN = "TYPE_GDN";
    String TYPE_SHIPPING_DATA = "TYPE_SHIPPING_DATA";
    String TYPE_PURCHASE_INVOICE = "TYPE_PURCHASE_INVOICE";
    String PROFIT = "profit";
    String COST = "cost";
    String LEAD_RESOURCE = "LEAD_RESOURCE";
    String COUNTRY = "COUNTRY";
    String CRM_OPPORTUNITY_ASSIGNEE = "CRM_OPPORTUNITY_ASSIGNEE";
    String CRM_OPPORTUNITY_CONTACT_NAME = "CRM_OPPORTUNITY_CONTACT_NAME";
    String CRM_OPPORTUNITY_ACCOUNT_NAME = "CRM_OPPORTUNITY_ACCOUNT_NAME";
    String LEAD_COMPANY = "LEAD_COMPANY";
    String FIXEDASSET_OWNERS = "FIXEDASSET_OWNERS";
    String PRODUCT_SALES_ACCOUNT = "PRODUCT_SALES_ACCOUNT";
    String PRODUCT_PURCHASE_ACCOUNT = "PRODUCT_PURCHASE_ACCOUNT";
    String PRODUCT_ASSETS_ACCOUNT = "PRODUCT_ASSETS_ACCOUNT";
    String PRODUCT_UNIT_MEASUREMENT = "PRODUCT_UNIT_MEASUREMENT";
    String FIXEDASSET_ACCOUNT = "FIXEDASSET_ACCOUNT";
    String FIXEDASSET_EXPENSE_ACCOUNT = "FIXEDASSET_EXPENSE_ACCOUNT";
    String FIXEDASSET_FINANCE_BY_ACCOUNT = "FIXEDASSET_FINANCE_BY_ACCOUNT";

    //Type Ids
    //CRM Type Ids, there are lead, account, contact, case etc. types;
    int CRM_LEAD_ID = 1;
    int CRM_ACCOUNT_ID = 2;
    int CRM_CONTACT_ID = 3;
    int CANDIDATE_ID = 31;
    int CRM_OPPORTUNITY_ID = 4;
    int CRM_CAMPAIGN_ID = 5;
    int CRM_CASE_ID = 8;

    int CLIENT_ID = 21;
    int SUPPLIER_ID = 22;

    //PM Type Ids, there are project, task, issue etc. types;
    int PM_PROJECT_ID = 7;
    int PM_TASK_ID = 6; //it's free now ;)
    int PM_EMPLOYEE_ID = 9; //it's free now ;)

    int RESERVATION_ID = 23;
    int CRM_EVENT_ID = 24;

    int SALE_QUOTE = 25;
    int _PRODUCT = 26;
    int COURCE_SCHEDULE_ID = 27;

    int PM_DEPARTMENT_ID = 28;
    int PM_ISSUE_ID = 29;
    //Training Center student type
    int STUDENT_ID = 30;
    int PURCHASE_ORDER = 31;
    int COURSE_PASSED_STUDENT_ID = 32;
    int COURSE_ID = 33;
    int INSTRUCTOR_ID = 34;
    int LANGUAGE_ID = 35;
    int ASSESSOR_ID = 36;
    int EMPLOYEE_ID = 37;
    int PURCHASE_INVOICE = 38;
    int PM_TASK_ASSIGNEE_ID = 39;
    int SALE_ORDER = 40;
    int POSITION_ID = 41;
    int LOCATION_ID = 42;
    int LEAD_SOURCE_ID = 43;
    int HR_DEPARTMENT_ID = 44;
    int BRIGADA_ID = 45;
    int SHIFT_ID = 46;
    int OVERTIME = 47;

    //Custom Item Table Relation Type
    int CI_EMPLOYEE = 48;
    int CI_POSITION = 49;
    int CI_DEPARTMENT = 50;
    Integer LOCATION_OWNERS = 51;
    int VACANCY_ID = 37;
    Integer VACANCIES = 52;
    int COUNTRY_ID = 53;
    int CRM_OPPORTUNITY_ASSIGNEE_ID = 54;
    int FIXEDASSET_OWNERS_ID = 55;
    int PRODUCT_SALES_ACCOUNT_ID = 56;
    int PRODUCT_PURCHASE_ACCOUNT_ID = 57;
    int PRODUCT_ASSETS_ACCOUNT_ID = 58;
    int PRODUCT_UNIT_MEASUREMENT_ID = 59;
    int FIXEDASSET_ACCOUNT_ID = 60;
    int FIXEDASSET_EXPENSE_ACCOUNT_ID = 61;
    int FIXEDASSET_FINANCE_BY_ACCOUNT_ID = 62;
    int TIMESLOT_ID = 63;
}
