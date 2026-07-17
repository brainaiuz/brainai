package com.finnetlimited.reportservice.core.client.ui;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 10.06.2010
 * Time: 17:04:01
 * To change this template use File | Settings | File Templates.
 */
public interface Constants {

    ////////////COOKIES//////////////////
    String SESSION_ID_COOKIE = "SESSION_ID";
    String SECTION_HTML = "SECTION_HTML";
    String VIEW = "view";
    String ID = "id";

    String _UPLOAD_TYPE = "_UPLOAD_TYPE";
    String LOCAL = "LOCAL";
    String AMAZON = "AMAZON";
    String GOOGLE = "GOOGLE";

    ////////////////////COMPANY RELATED/////////////////////
    String COMPANY_NAME = "COMPANY_NAME";
    String USER_ID = "USER_ID";
    String COMPANY_ID = "COMPANY_ID";
    String BASE_CURRENCY = "BASE_CURRENCY";
    ////////////////EMPLOYEE RELATED//////////////////////
    String FIRST_NAME = "FIRST_NAME";
    String LAST_NAME = "LAST_NAME";
    String EMAIL = "EMAIL";

    ////////////////////////////////LANDING PAGE/////////////////////////
    String PA_FIRST_VIEW = "paFirstView";
    String AVAILABILITY_FIRST_VIEW = "availabilityFirstView";

    ////////////////////////////////USER SETTINGS RELATED////////////////
    String ROLES = "ROLES";
    String USER_FULLNAME = "USER_FULLNAME";
    String USER_NAME = "USER_NAME";
    String FULL_NAME = "FULL_NAME";
    String ACCESS_GRANTED = "ACCESS_GRANTED";
    String TRUE = "TRUE";
    String FALSE = "FALSE";
    String HOME_PAGE = "HOME_PAGE";
    String LANDING_PAGE = "LANDING_PAGE";
    String USER = "user";
    String LINK = "link";
    String ACCOUNTING_IS_SETUP = "ACCOUNTING_IS_SETUP";
    String PM_IS_SETUP = "PM_IS_SETUP";
    String LONG_DATE_FORMAT = "LONG_DATE_FORMAT";
    String SHORT_DATE_FORMAT = "SHORT_DATE_FORMAT";
    String DATE_PATTERN = "MM/dd/yyyy";

    Integer DR = 1;
    Integer TL = 2;
    Integer PM = 3;
    Integer HR = 4;
    Integer ADMIN = 5;
    Integer MEM = 6;
    Integer CLIENT = 7;
    Integer ACCOUNTANT = 9;
    Integer SALESMAN = 10;//Salesman
    Integer CUSTOMER_SERVICE_REPRESENTATIVE = 11;//Customer Service Representative
    Integer SALESPERSON = 12;
    Integer ADMIN_LOCATION = 13;
    Integer CALENDAR_EDITOR = 14;

    /////////////VIEWS NAME///////////////////////
    String TASK_LIST = "taskList";
    String TIMESHEET = "timesheet";
    String DEPARTMENT_LIST = "departmentList";

    //////////////CONTAINERS NAME///////////////
    String TASK = "task";
    String SEARCH = "search";
    String WORKSTREAM = "workstream";
    String PROJECT = "project";
    String DEPARTMENT = "department";

    //////////////////////////////INVOICING/////////////////////////////
    String INVOICE = "invoice";

    String INVOICE_STATUS = "INVOICE_STATUS";
    String DRAFT = "DRAFT";
    String OPEN = "OPEN";
    String APPROVE = "APPROVE";
    String CLIENT_APPROVE = "CLIENT_APPROVE";
    String REJECT = "REJECT";
    String REVERSED = "REVERSED";
    String OVER_DUE = "OVER_DUE";
    String PAID = "PAID";
    String CONVERTED = "CONVERTED";
    String RECEIVED = "RECEIVED";
    String SALE_ORDER = "SALE_ORDER";
    String PICKED = "PICKED";
    String PACKED = "PACKED";
    String SHIPPED = "SHIPPED";

    String DUPLICATE = "DUPLICATE";
    String UPDATED = "UPDATED";


    String FIXED = "FIXED";

    String GOOGLE_APP_DOMAIN = "GOOGLE_APP_DOMAIN";

    ///////////////ASSESSMENT//////////////////////
    String ASSESSMENT_360 = "ASSESSMENT_360";

    String INITIATED = "INITIATED";
    String REVIEWED = "REVIEWED";
    String RATED = "RATED";
    String APPROVED = "APPROVED";

    String IS_CLIENT = "IS_CLIENT";
    String IS_EMPLOYEE = "IS_EMPLOYEE";


    String NULL = "null";

    String HOST_LIVE = "reporting.workforcetrack.com";
    String GOOGLE_DOCUMENTS = "googledocuments";
    Integer E_MAIL = 1;
    Integer ALERT = 2;
    Integer SMS = 3;
    Integer ALL = 4;


    ////////////////EXPENSE/////////////////////////////////////
    String EXPENSE_STATUS = "EXPENSE_STATUS";
    String EXPENSE_DRAFT = "EXPENSE_DRAFT";
    String EXPENSE_SUBMITTED = "EXPENSE_SUBMITTED";
    String EXPENSE_APPROVED = "EXPENSE_APPROVED";
    String EXPENSE_DECLINED = "EXPENSE_DECLINED";
    String EXPENSE_PAID = "EXPENSE_PAID";

    String EXPENSE_VIEW = "EXPENSE_VIEW";
    String EVENT = "EVENT";
    String COMMENT = "COMMENT";

    ///////////////Landing Pages//////////////////////////////////
    String PA = "pa";
    String INV = "inv";
    String ERP = "erp";
    ///////////////Roles//////////
    int READ = 1;
    int EDIT = 2;

    //////////////STATUS_MESSAGES///////////////
    int INFO = 0;
    int SUCCESS = 1;
    int WARNING = 2;
    int ERROR = 3;

    String FREE_TRIAL = "FREE_TRIAL";
    String THREE_MONTH_15 = "THREE_MONTH_15";
    String SIX_MONTH_20 = "SIX_MONTH_20";
    String TWELVE_MONTH_TWENTY_30 = "TWELVE_MONTH_TWENTY_30";
    String TWO_YEARS_45 = "TWO_YEARS_45";


    String SUBSCRIPTION_ADD = "add";
    int NOT_STARTED = 2;
    int COMPLETED = 79;

    /* Project status */
    int ONGOING = 74;


    Integer PAYMENT = 1;
    Integer DEDUCTION = 0;

    String TAX_CODE = "TAX_CODE";

    String PENSION_SCHEME = "PENSION_SCHEME";

    String PAY_METHOD = "PAY_METHOD";

    String ALLOWANCE = "ALLOWANCE";
    String SALARY = "SALARY";

    /**
     * ********************************************Accounting*******************************************************
     */
    String SALE_INVOICE = "saleinvoice";//receivable
    String RECURRING_INVOICE = "recurringinvoice";
    String SALE_QUOTE = "salequote";
    String SALE_ORDER_CODE = "saleorder";
    String PURCHASE_INVOICE = "purchaseinvoice";//payable
    String PURCHASE_ORDER = "purchaseorder";

    String RECEIVABLE = "RECEIVABLE";//For sales types.
    String PAYABLE = "PAYABLE";//For purchases type.


    ////////////////////////////////////////////////Account Type///////////////////////////////////////////////////////
    String ASSETS = "ASSETS";
    String LIABILITIES = "LIABILITIES";
    String EQUITY = "EQUITY";
    String EXPENSES = "EXPENSES";
    String REVENUE = "REVENUE";


    /////////////////////////////ISSUE RELATED////////////////////////////////
    String EMPLOYEE_ISSUE = "Employee";

    String PROJECT_ISSUE = "Project";


    /////////////////////////////////////REPORT FITERS/////////////////////////
    String FILTER_DEPARTMENT = "Department";
    String FILTER_EMPLOYEE = "Employee";

    ///////////////////////////////////// TIMETRACK STATUSES /////////////////////////
    String AVAILABLE = "AVAILABLE";
    String NOT_AVAILABLE = "NOT_AVAILABLE";

    ///////////////////////////////////EMPLOYEE STATUS//////////////////////////////
    String EMPLOYEE_STATUS_ACTIVE = "Active";
    String EMPLOYEE_STATUS_INACTIVE = "Inactive";

    //Transaction Types
    String INVOICE_TRANSACTION = "INVOICE_TRANSACTION";
    String INVOICEPAYMENT_TRANSACTION = "INVOICEPAYMENT_TRANSACTION";
    String EXPENSE_TRANSACTION = "EXPENSE_TRANSACTION";
    String EXPENSEPAYMENT_TRANSACTION = "EXPENSEPAYMENT_TRANSACTION";
    String MANUAL_TRANSACTION = "MANUAL_TRANSACTION";

    String commonParamForUrl = "menubar=no,location=no,resizable=yes,scrollbars=yes,status=yes,height=600,width=800";//http://www.linkedin.com/pub/dir/?first=asdf&last=asgasdfg&search=Search
    String CRM_CONTACT = "crm_contact";
    String CRM_ACCOUNT = "crm_account";
    String CRM_LEAD = "crm_lead";

    String CONTACT = "globalcontact";

    String NOTE = "note";

    String NEWS = "news";


    /////////////////////////CountryCodes//////////////////////////////
    String UK = "GB";

    int EMPLOYEE_CHART = 1;

    String DEPARTMENT_GOAL = "department";
    String PERSONAL_GOAL = "personal";
    String PROJECT_GOAL = "project";
    String BUSINESS_GOAL = "business";

    String HRMS_EDIT_PROFILE = "editprofile";

    String DEFAULT_WIDTH = "200px";

    String HRMS_URL = "Hrms.html";

    ///////////////////// EMAIL TEMPLATE CATEGORIES ///////////////////////
    String SALES_INVOICE_CATEGORY = "SALES_INVOICE_CATEGORY";
    String SALES_QUOTE_CATEGORY = "SALES_QUOTE_CATEGORY";
    String PURCHASE_ORDER_CATEGORY = "PURCHASE_ORDER_CATEGORY";
    String RECEIPT_CATEGORY = "RECEIPT_CATEGORY";
    String EXPENSE_CLAIM_CATEGORY_SUBMIT = "EXPENSE_CLAIM_CATEGORY_SUBMIT";
    String EXPENSE_CLAIM_CATEGORY_RESUBMIT = "EXPENSE_CLAIM_CATEGORY_RESUBMIT";
    String CASE_REPLIED_CATEGORY = "CASE_REPLIED_CATEGORY";
    String CASE_AUTO_RESPONSE_CATEGORY = "CASE_AUTO_RESPONSE_CATEGORY";

    String CHECKBOX = "CheckBox";

    String TEXT = "Text";
    String NUMBER = "Number";
    String DATE = "Date";

    //File entity type
    int F_DEFAULT = 0;
    int F_PROJECT = 2;
    int F_TASK = 3;
    int F_PR_ISSUE = 4;
    int F_SALE_INV = 10;
    int F_CRM_CONTACT = 15;
    int F_LEAD = 16;
    int F_CASE = 17;

}
