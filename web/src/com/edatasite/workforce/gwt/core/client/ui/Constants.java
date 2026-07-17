package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface Constants {
    String defaultSupportName = "KPI Support";
    String defaultSupportEmail = "support@kpi.com";

    String KPI_PUBLIC_WORKSPACE = "https://s3.amazonaws.com/workforcetrack/000000000000/public/workspace/sample/kpi_public_workspace.xml";
    String ACTIVRA_PUBLIC_WORKSPACE = "https://s3.amazonaws.com/workforcetrack/000000000000/public/workspace/sample/activira_public_workspace.xml";
    String COMPANY_NO_LOGO = "https://s3.amazonaws.com/workforcetrack/000000000000/public/api-no-logo.png";
    String DEFAULT_USER_PROFILE_PHOTO = "https://s3.amazonaws.com/workforcetrack/000000000000/api-public/documents/no-photo.gif";
    String SHORTENER_API = "https://api.short.io/links";
    String SHORTENER_DOMAIN = "kpicom.short.gy";
    String TELEGRAM_BASE_URL = "https://api.telegram.org/bot";
    String PASS_PHRASE = "auSLl-x-GR2pin3/mk=";
    String BACKUP_BUCKED_NAME = "backups_jkfr39sdf99e3y";
    String AMAZON_BACKUP_ROOT_FOLDER = "0000000backup";
    String XML_BACKUP_ROOT_FOLDER = "xml_backup";

    String VALID_NAME_REGEX = "^[\\p{L} .'-]+$";

    String DEFAULT_SPRING_PROFILES = "local";
    String SESSION_REGEX = "(.+)(\\$)(\\d+)(\\$)(.+)";
    String DELIMITR = "#_#";
    String NO_CAPTCHA_USED = "NO_CAPTCHA_USED";
    String ERROR_FORM_STYLE = "x-form-invalid";
    String MULTIVALUE_SEPARATOR = "\\|";
    String DEFAULT_SEPARATOR = ",";
    int DAY_MONDAY = 1;

    ////////////COOKIES//////////////////
    String SESSION_ID_COOKIE = "SESSION_ID";
    String SERVICE_ID_COOKIE = "SERVICE_ID";
    String LAST_REQUEST_TIME = "LRT";
    String SESSION_ID = "SESSION_ID";
    String USER_NAME_COOKIE = "USER_NAME";
    String USER_PASSWORD_COOKIE = "USER_PASSWORD";
    String SECTION_HTML = "SECTION_HTML";
    String USER_AVAILABILITY = "USER_AVAILABILITY";
    String USER_AGENT = "user-agent";
    String WEBAUTHTOKEN = "webauthtoken"; //Live ID cookie
    String LEAD_ID_COOKIE = "LEAD_ID";
    String FROM_MARKETPLACE = "FROM_MARKETPLACE";
    String AUTH_TOKEN = "AUTH_TOKEN";

    String VIEW = "view";
    String ID = "id";
    String SESSION_TRACK_ID = "SESSION_TRACK_ID";

    String USER_NAME_PARAMETER = "USER_NAME";
    String USER_PASSWORD_PARAMETER = "USER_PASSWORD";
    String REMEMBER_ME_PARAMETER = "REMEMBER_ME";
    byte[] GWT_DES_KEY = "antiBotEncryption".getBytes();

    String _UPLOAD_TYPE = "_UPLOAD_TYPE";
    String LOCAL = "LOCAL";
    String KPI_STORAGE = "KPI_STORAGE";
    String AMAZON = "AMAZON";
    String GOOGLE = "GOOGLE";
    String OFFICE_365 = "OFFICE_365";

    String MINIO = "MINIO";
    String DEFAULTT = "DEFAULT";
    String YANDEX = "YANDEX";
    String OFFICE_365_SHARE_POINT = "OFFICE_365_SHARE_POINT";
    String UPLOAD_SHARE_POINT = "UPLOAD_SHARE_POINT";
    String LINK_TO_SHARE_POINT = "LINK_TO_SHARE_POINT";
    String DEMOACCOUNT = "demo";
    String NO_DATA = "NO_DATA";
    String AFFILIATE = "aff";
    String COMPAING = "kcpn";
    String REFERER = "referer";
    String GTALK_ACCOUNTS = "ochil_sim_sim_ochil";


    ////////////////////COMPANY RELATED/////////////////////
    String COMPANY_NAME = "COMPANY_NAME";
    String USER_ID = "USER_ID";
    String WITHOUT_ENCRYPTED_COMPANY_ID = "WITHOUT_ENCRYPTED_COMPANY_ID";
    String COMPANY_ID = "COMPANY_ID";
    String LOGO_URL = "LOGO_URL";
    String BASE_CURRENCY = "BASE_CURRENCY";
    String EXCHANGE_RATE = "EXCHANGE_RATE";
    String PAYPAL_ACCOUNT = "PAYPAL_ACCOUNT";
    String STRIPE_PUBLIC_KEY = "STRIPE_PUBLIC_KEY";
    String STRIPE_SECRET_KEY = "STRIPE_SECRET_KEY";
    String FACEBOOK_API_KEY = "FACEBOOK_API_KEY";
    String LINKEDIN_API_KEY = "LINKEDIN_SECRET_KEY";
    String LINKEDIN_SECRET_KEY = "LINKEDIN_SECRET_KEY";
    String IS_LIVE_ENVIRONMENT = "IS_LIVE_ENVIRONMENT";
    String PRODUCT_NAME = "PRODUCT_NAME";
    String HELP_HOST = "HELP_HOST";
    String UPLOAD_DIR = "UPLOAD_DIR";
    String UPLOAD_TYPE = "UPLOAD_TYPE";
    String HOST_NAME_VALUE = "HOST_NAME_VALUE";
    String VAT_RATE_VALUE = "VAT_RATE_VALUE";
    String SUPPORT_EMAIL = "SUPPORT_EMAIL";
    String PHONE = "PHONE";
    String USER_CITY = "USER_CITY";
    String PANAMA_CITY = "Panama";
    String USER_COUNTRY = "USER_COUNTRY";
    String COMPANY_COUNTRY_CODE = "COMPANY_COUNTRY_CODE";
    String ALTERNATIVE_CALENDAR_ID = "ALTERNATIVE_CALENDAR_ID";
    String ISAUTOMATIC = "ISAUTOMATIC";//calculation method of project/task/emoloyee task percentage
    String ISAUTOMATICAPPROVAL = "ISAUTOMATICAPPROVAL";//approval method of timesheet hours
    String ISAUTOMATICWAITINGFORAPPROVAL = "ISAUTOMATICWAITINGFORAPPROVAL";//approval method of timesheet hours
    String VALIDATE_TASK_START = "VALIDATE_TASK_START";
    String TIMESHEET_COMMENT_REQUIRED = "TIMESHEET_COMMENT_REQUIRED";
    String VALIDATE_TASK_END = "VALIDATE_TASK_END";
    String VALIDATE_TIMESLOT = "VALIDATE_TIMESLOT";
    String VALIDATE_MAXIMUM_HOURS = "VALIDATE_MAXIMUM_HOURS";
    String VALIDATE_DAY_OFF = "VALIDATE_DAY_OFF";
    String HH_CLIENT_ID = "HH_CLIENT_ID";
    String HH_CLIENT_SECRET = "HH_CLIENT_SECRET";
    String HH_SAVE_BUTTON = "HH_SAVE_BUTTON";
    String ZOOM_CLIENT_ID = "ZOOM_CLIENT_ID";
    String ZOOM_CLIENT_SECRET = "ZOOM_CLIENT_SECRET";
    String ZOOM_SAVE_BUTTON = "ZOOM_SAVE_BUTTON";
    String TELEGRAM_BOT_TOKEN = "TELEGRAM_BOT_TOKEN";
    String TELEGRAM_BOT_USERNAME = "TELEGRAM_BOT_USERNAME";
    String TELEGRAM_BOT_SAVE_BUTTON = "TELEGRAM_BOT_SAVE_BUTTON";
    String LINKEDIN_CLIENT_ID = "LINKEDIN_CLIENT_ID";
    String LINKEDIN_CLIENT_SECRET = "LINKEDIN_CLIENT_SECRET";
    String LINKEDIN_SAVE_BUTTON = "LINKEDIN_SAVE_BUTTON";
    String MEHNAT_UZ_CLIENT_ID = "MEHNAT_UZ_CLIENT_ID";
    String MEHNAT_UZ_CLIENT_SECRET = "MEHNAT_UZ_CLIENT_SECRET";
    String MEHNAT_UZ_SAVE_BUTTON = "MEHNAT_UZ_SAVE_BUTTON";
    String MAXIMUM_HOURS = "MAXIMUM_HOURS";
    String VALIDATE_PAST_TIMSHEET = "VALIDATE_PAST_TIMSHEET";
    String PAST_TIMSHEET_DAYS = "PAST_TIMSHEET_DAYS";
    String VALIDATE_FUTURE_TIMESHEET = "VALIDATE_FUTURE_TIMESHEET";
    String FUTURE_TIMESHEET_DAYS = "FUTURE_TIMESHEET_DAYS";
    String VALIDATE_HOLIDAY = "VALIDATE_HOLIDAY";
    String VALIDATE_lEAVE_REQUEST = "VALIDATE_lEAVE_REQUEST";
    String TIMESHEET_WEEK_START = "TIMESHEET_WEEK_START";
    String OVERALL_DATE_PICKER_WEEK_START = "OVERALL_DATE_PICKER_WEEK_START";
    String SHOW_COMPLETED_TASKS = "SHOW_COMPLETED_TASKS";
    String SHOW_HOUR_TYPE_DROPDOWN = "SHOW_HOUR_TYPE_DROPDOWN";
    String ENABLE_MULTIPLE_TIMER_INTSTANCES = "ENABLE_MULTIPLE_TIMER_INTSTANCES";
    String SAVE_TIMER_INTO_TIMESHEET_AUTOMATICALLY = "SAVE_TIMER_INTO_TIMESHEET_AUTOMATICALLY";
    String C_ID = "cid";// company id
    String U_ID = "uid";// user id
    String D_ID = "did";//database id
    String S_ID = "sid";//service id
    String TG_ID = "tgid";//telegram chat id
    String TG_CHAT_NAME = "tgChatName";//telegram chat name
    String ACCOUNT_TYPE = "ACCOUNT_TYPE";//Superuser or ordinary user
    String SUPER_USER = "SUPER_USER";//Superuser or ordinary user
    Integer MAX_PDF_OR_EXCEL_LIMIT = 2000;
    Integer ANTIBOT_ERROR = -1012;
    String DOUBLE_MESSAGE_ENABLE = "DOUBLE_MESSAGE_ENABLE";
    String MULTIPLE_SALES_PRICE_ENABLED = "MULTIPLE_SALES_PRICE_ENABLED";
    //    String PRODUCT_TABLE_CUSTOMIZATION = "PRODUCT_TABLE_CUSTOMIZATION";
    String LOGISTICS = "LOGISTICS";
    String ACCOUNTING_MODULE = "ACCOUNTING_MODULE";
    String SETTINGS_ACCOUNTING_SETTINGS = "SETTINGS_ACCOUNTING_SETTINGS";
    String MULTIWAREHOUSE_ENABLED = "MULTIWAREHOUSE_ENABLED";
    String KPI_ADVANCED_MODE = "KPI_ADVANCED_MODE";
    String PRODUCTION_ENABLED = "PRODUCTION_ENABLED";
    String ENABLE_SWITCHABLE_LAYOUT = "ENABLE_SWITCHABLE_LAYOUT";
    String PRORATA_BASED_ANNUAL_LEAVE = "PRORATA_BASED_ANNUAL_LEAVE";
    String EMAIL_ACCOUNT_SET_UP = "EMAIL_ACCOUNT_SET_UP";
    ////////////////EMPLOYEE RELATED//////////////////////
    String FIRST_NAME = "FIRST_NAME";
    String LAST_NAME = "LAST_NAME";
    String EMAIL = "EMAIL";
    String MULTI_QUOTE_CONVERTER_GROUP_BY_ITEM_CONFIG = "MULTI_QUOTE_CONVERTER_GROUP_BY_ITEM_CONFIG";

    //////////////MAGENTO SYNC RELATED//////////////////////
    String SIMPLE = "SIMPLE";
    String CONFIGURED = "CONFIGURED";
    //////////////BMT RELATED FOR COO//////////////////////

    ////////////////////////////////LANDING PAGE/////////////////////////
    String INVOICE_FIRST_VIEW = "invoiceFirstView";
    String PM_FIRST_VIEW = "pmFirstView";
    String PA_FIRST_VIEW = "paFirstView";
    String AVAILABILITY_FIRST_VIEW = "availabilityFirstView";
    String EXPENSES_FIRST_VIEW = "expensesFirstView";

    ////////////////////////////////USER SETTINGS RELATED////////////////
    String DEFAULT_COMPANY_NAME = "DEMO_COMPANY";
    String ANY_DATA_MISSING = "ANY_DATA_MISSING";
    String ROLES = "ROLES";
    String ROLE_CODES = "ROLE_CODES";
    String USER_FULLNAME = "USER_FULLNAME";
    String USER_INITIALNAME = "USER_INITIALNAME";
    //String EMPLOYEE_NUMBER = "EMPLOYEE_NUMBER";
    String USER_NAME = "USER_NAME";
    String FULL_NAME = "FULL_NAME";
    String INITIAL_URL = "INITIAL_URL";
    String ACCESS_GRANTED = "ACCESS_GRANTED";
    String TRUE = "TRUE";
    String FALSE = "FALSE";
    String HOME_PAGE = "HOME_PAGE";
    String LANDING_PAGE = "LANDING_PAGE";
    String HASH_LINK_COOKIE = "HASH_LINK_COOKIE";
    String HASH_COMPANYID_COOKIE = "HASH_COMPANYID_COOKIE";
    String LINK = "link";
    String BITLY = "bitly";
    String ONE_OFF_ASSESSMENT_ID = "ONE_OFF_ASSESSMENT_ID";
    String ACCOUNTING_IS_SETUP = "ACCOUNTING_IS_SETUP";
    String MULTI_COMPANY_SUBSIDIARY = "MULTI_COMPANY_SUBSIDIARY";
    String HAS_THE_SAME_LR = "HAS_THE_SAME_LR";

    String ACCOUNTING_CALCULATION_SCALE = "ACCOUNTING_CALCULATION_SCALE";
    String ACCOUNTING_CUSTOM_PRICE_SCALE = "ACCOUNTING_CUSTOM_PRICE_SCALE";
    String ACCOUNTING_CUSTOM_QUANTITY_SCALE = "ACCOUNTING_CUSTOM_QUANTITY_SCALE";
    String ACCOUNTING_CUSTOM_EXRATE_SCALE = "ACCOUNTING_CUSTOM_EXRATE_SCALE";
    String ACCOUNTING_TAX_RATE_SCALE = "ACCOUNTING_TAX_RATE_SCALE";
    String ACCOUNTING_DISCOUNT_SCALE = "ACCOUNTING_DISCOUNT_SCALE";
    String ACCOUNTING_PROGRESS_INVOICING_AMOUNT_SCALE = "ACCOUNTING_PROGRESS_INVOICING_AMOUNT_SCALE";
    String TRANSACTION_LOCKING_DATE = "TRANSACTION_LOCKING_DATE";
    String TRANSACTION_LOCKING_SALES = "TRANSACTION_LOCKING_SALES";
    String TRANSACTION_LOCKING_PURCHASES = "TRANSACTION_LOCKING_PURCHASES";
    String TRANSACTION_LOCKING_BANKING = "TRANSACTION_LOCKING_BANKING";
    String TRANSACTION_LOCKING_EMPLOYEES = "TRANSACTION_LOCKING_EMPLOYEES";
    String TRANSACTION_LOCKING_ATTENDANCE = "TRANSACTION_LOCKING_ATTENDANCE";
    String TRANSACTION_LOCKING_RECRUITMENT = "TRANSACTION_LOCKING_RECRUITMENT";
    String TRANSACTION_LOCKING_PAYSLIPS = "TRANSACTION_LOCKING_PAYSLIPS";
    String TRANSACTION_LOCKING_CASHADVANCES = "TRANSACTION_LOCKING_CASHADVANCES";
    String TRANSACTION_LOCKING_ADDITIONALPAYMENTS = "TRANSACTION_LOCKING_ADDITIONALPAYMENTS";
    String CUSTOM_TAX_NAME = "CUSTOM_TAX_NAME";
    String PO_IGNORE_MANAGER_APPROVAL = "PO_IGNORE_MANAGER_APPROVAL";

    String MONTHLY_TIMESHEET = "MONTHLY_TIMESHEET";
    String STOREFRONT = "STOREFRONT";
    String IS_SUPPLIER = "IS_SUPPLIER";
    String IS_CLIENT_CONTACT = "IS_CLIENT_CONTACT";
    String PM_IS_SETUP = "PM_IS_SETUP";
    String IS_SETUP_SUPPROJECT = "IS_SETUP_SUPPROJECT";
    String IS_SETUP_SUPPROJECT_TWO_LEVEL = "IS_SETUP_SUPPROJECT_TWO_LEVEL";
    String _COMPANY_WORKAREA = "_COMPANY_WORKAREA";
    String _COMPANY_INDUSTRY = "_COMPANY_INDUSTRY";
    String LONG_DATE_FORMAT = "LONG_DATE_FORMAT";
    String  RENT_ITEM_STATUS = "RENT_ITEM_STATUS";
    String SHORT_DATE_FORMAT = "SHORT_DATE_FORMAT";
    String DATE_PATTERN = "MM/dd/yyyy";
    String MESSAGE_CENTER_ENABLED = "MESSAGE_CENTER_ENABLED";
    String THEME_FOR_SYSTEM = "THEME_FOR_THE_SYSTEM";
    String LANGUAGE_FOR_USER = "LANGUAGE_FOR_USER";
    String SIDE_NAV_STYLE = "SNS";
    String PROFILE_CONTENT = "PROFILE_CONTENT";
    String MODULE_PERMSISIONS = "MODULE_PERMSISIONS";
    String LATEST_SERVER_UPLOAD_VERSION = "LATEST_SERVER_UPLOAD_VERSION";
    String SESSION_LENGTH = "SESSION_LENGTH";
    String FREE_TRIAL_DAYS_LEFT = "FREE_TRIAL_DAYS_LEFT";
    String IS_PAID_COMPANY = "IS_PAID_COMPANY";
    String DEFAULT_CURRENCY_CODE = "DEFAULT_CURRENCY_CODE";
    String ENABLE_SALES_BACKEND_FOR_USER = "ENABLE_SALES_BACKEND_FOR_USER";
    String ENABLE_SUPPORT_BACKEND_FOR_USER = "ENABLE_SUPPORT_BACKEND_FOR_USER";
    String ENABLE_ADMIN_BACKEND_FOR_USER = "ENABLE_ADMIN_BACKEND_FOR_USER";
    String ENABLE_PARTNER_ADMIN_BACKEND_FOR_USER = "ENABLE_PARTNER_ADMIN_BACKEND_FOR_USER";
    //    String ENABLE_PDF_BACKEND_FOR_USER = "ENABLE_PDF_BACKEND_FOR_USER";
    String ENABLE_DEVELOPER_BACKEND_FOR_USER = "ENABLE_DEVELOPER_BACKEND_FOR_USER";
    String PROMOTIONAL_CODE = "PROMOTIONAL_CODE";
    String IS_MULTI_COMPANY = "IS_MULTI_COMPANY";//if current company -- multi company!
    String IS_TEST_COMPANY = "IS_TEST_COMPANY";
    String REDIRECT_URI = "redirect_uri";
    String RESOURCE_UTILIZATION_ENABLED = "RESOURCE_UTILIZATION_ENABLED";
    String SHOW_GOOGLE_TALK_CHAT = "SHOW_GOOGLE_TALK_CHAT";
    String SHOW_SCORE_CALCULATION = "SHOW_SCORE_CALCULATION";
    String CUSTOM_RATE_ENABLE = "CUSTOM_RATE_ENABLE";
    String TRAINING_CENTER_ENABLED = "TRAINING_CENTER_ENABLED";
    String PURCHASE_ORDER_CUSTOM_ITEM_RECIEVE_IMPORT = "PURCHASE_ORDER_CUSTOM_ITEM_RECIEVE_IMPORT";
    String ENABLE_TO_SHOW_SAMPLE_DATA = "ENABLE_TO_SHOW_SAMPLE_DATA";
    //for project -> begin

    String ENABLE_MONTLY_PLAN = "ENABLE_MONTLY_PLAN";


    String DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_START_TIME = "DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_START_TIME";
    //default current employee timeSlot end time
    String DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_END_TIME = "DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_END_TIME";
    String TIMESHEET_DF = "TIMESHEET_DF";
    String TIMESHEET_VALIDATE_EST = "TIMESHEET_VALIDATE_EST";
    //for default description character limit;
    int DEFAULT_DESCRIPTION_CHARACTER_LIMIT = 10000;
    int DEFAULT_TEXT_AREA_LIMIT = 1000;
    int USER_TYPE_WFT_CORE_USER = 1;
    Integer USER_TYPE_BMT_RESPONDENT = 3;

    //CONTACT IM ADDRESS
    String GTALK = "GTALK";
    String AIM = "AIM";
    String YAHOO = "YAHOO";
    String SKYPE = "SKYPE";
    String QQ = "QQ";
    String MSN = "MSN";
    String JABBER = "JABBER";

    /////////////////////// TIMESHEET////////////////////////////////////////
    int TIMESHEET_ENTRY_NOTSUBMITTED = 0;
    int TIMESHEET_ENTRY_APPROVED = 1;
    int TIMESHEET_ENTRY_REJECTED = 2;
    int TIMESHEET_ENTRY_WAITING = 3;
    int TIMESHEET_ENTRY_APPLYING_UPDATE = 4;
    int TIMESHEET_ENTRY_FAILED = 5;

    Integer TIMESHEET_SUBMIT_FOR_APPROVAL_FORM = 0;
    Integer TIMESHEET_APPROVAL_FORM = 1;

    Integer DR = 1;
    String DR_CODE = "DR";//Director
    Integer TL = 2;
    String TL_CODE = "TL";//Department leader
    Integer PM = 3;
    String PM_CODE = "PM";//Project manager
    Integer HR = 4;
    String HR_CODE = "HR";//Hr manager
    Integer ADMIN = 5;
    String ADMIN_CODE = "ADMIN";//Administrator
    Integer MEM = 6;
    String MEM_CODE = "MEM";//Member
    Integer CLIENT = 7;
    String CLIENT_CODE = "CLIENT";//Client
    Integer ACCOUNTANT = 9;
    String ACCOUNTANT_CODE = "ACCOUNTANT";//Accountant
    Integer ONE_OFF = 8;
    Integer SALESMAN = 10;//Salesmanager
    String SALESMAN_CODE = "SALESMAN";//Salesmanager
    Integer CUSTOMER_SERVICE_REPRESENTATIVE = 11;//Customer Service Representative
    String CUSTOMER_SERVICE_REPRESENTATIVE_CODE = "CUSTOMER_SERVICE_REPRESENTATIVE";//Customer Service Representative
    Integer SALESPERSON = 12;
    String SALESPERSON_CODE = "SALESPERSON";//Sales person
    Integer ADMIN_LOCATION = 13;
    String ADMIN_LOCATION_CODE = "ADMIN_LOCATION";//Admin location
    Integer CALENDAR_EDITOR = 14;
    String CALENDAR_EDITOR_CODE = "CALENDAR_EDITOR";//Calendar editor
    Integer CALENDAR_VIEWER = 15;
    String CALENDAR_VIEWER_CODE = "CALENDAR_VIEWER";//Calendar viewer
    Integer CHAT_EXPERT = 16;
    String CHAT_EXPERT_CODE = "CHAT_EXPERT";//Chat expert
    Integer TIMESHEET_EDITOR = 17;
    String TIMESHEET_EDITOR_CODE = "TIMESHEET_EDITOR";//TimeSheet editor
    Integer GUEST = 18;
    String GUEST_CODE = "GUEST";//Guest
    String CUSTOM_MEMBER_CODE = "CUSTOM_MEMBER";//Custom member
    String PMOFPR = "PMOFPR"; //Project Manager of the project -- 20
    String DLOFPR = "DLOFPR"; //Department leader of the project -- 21
    String DLOFPR2 = "DLOFPR2"; //DL (Specific) 2
    String DLOFPR3 = "DLOFPR3"; //DL (Specific) 3
    String DLOFPR4 = "DLOFPR4"; //DL (Specific) 4
    String DLOFPR5 = "DLOFPR5"; //DL (Specific) 5
    String BMOFPR = "BMOFPR"; //Backup Manager of the project -- 22
    String CREATOR = "CREATOR"; // -- 23
    String PROJECTS_DIRECTOR_CODE = "PROJECTS_DIRECTOR"; //Projects Director
    Integer AUDITOR = 25; //Auditor
    String AUDITOR_CODE = "AUDITOR"; //Auditor
    String CUSTOMER_SERVICE_MANAGER_CODE = "CUSTOMER_SERVICE_MANAGER";//Customer service manager
    String SUPPLIER = "SUPPLIER"; //Supplier
    String CUSTOMER = "CUSTOMER";
    Integer INSTRUCTOR = 28; //Instructor
    String INSTRUCTOR_CODE = "INSTRUCTOR";//Instructor
    String SUPERVISOR_CODE = "SUPERVISOR";//Supervisor
    Integer ESS_USER = 29;//Supervisor
    String ESS_USER_CODE = "ESS_USER";//Supervisor
    String PARTNER_ADMIN_CODE = "PARTNER_ADMIN";//Partner Admin
    String CRM_ACCOUNT_TYPE = "CRM_ACCOUNT";


    //TAX TREATMENT CONSTANTS
    String _TAX_TREATMENT = "_TAX_TREATMENT";

    String _UK_TAX_TREATMENTS = "_UK_TAX_TREATMENTS";
    String VAT_REGISTERED = "VAT_REGISTERED";

    String VAT_ACCOUNTING_BASIS = "VAT_ACCOUNTING_BASIS";
    String NON_VAT_REGISTERED = "NON_VAT_REGISTERED";
    String GCC_VAT_REGISTERED = "GCC_VAT_REGISTERED";
    String GCC_NON_VAT_REGISTERED = "GCC_NON_VAT_REGISTERED";
    String NON_GCC = "NON_GCC";
    String VAT_REGISTERED_DESIGNATED_ZONE = "VAT_REGISTERED_DESIGNATED_ZONE";
    String NON_VAT_REGISTERED_DESIGNATED_ZONE = "NON_VAT_REGISTERED_DESIGNATED_ZONE";
    String NON_VAT = "NON_VAT";

    String OVERSEAS = "OVERSEAS";
    String OUT_OF_SCOPE = "OUT_OF_SCOPE";

    String FROM_TRAINING_CENTER = "FROM_TRAINING_CENTER";

    String EDITABLE = "EDITABLE";
    String FROM_SYSTEM = "FROM_SYSTEM";

    /////////////VIEWS NAME///////////////////////
    String GETTING_STARTED = "gettingstarted";
    String TASK_LIST = "taskList";
    String TIMESHEET = "timesheet";
    String MONTHLYTIMESHEET = "monthlyTimesheet";
    String WEEKLY_TIMESHEET = "weeklyTimesheet";
    String PROJECT_LIST = "projectList";
    String SHIFT_LIST = "ShiftList";
    String BRIGADA_LIST = "birgadaList";
    String VACANCY_LIST = "vacancyList";
    String ROTATION_LIST = "rotation";
    String GROUP_PLACEMENT_LIST = "groupPlacement";
    String PO_LIST = "poList";
    String BOOKINGITEMS_LIST = "bookingItemsList";
    String SUB_PROJECT_LIST = "subProjectList";
    String DEPARTMENT_LIST = "departmentList";
    String TIMESHEET_APPROVAL_LIST = "timesheetApproval";
    String ISSUE_LIST = "issueList";
    String CLIENT_LIST = "clientList";
    String VAT_CATEGORY_LIST = "vatCategoryList";
    String SUPPLIER_LIST = "supplierList";
    String EMLOYEE_LIST = "employee";
    String TC_EMLOYEE_LIST = "tc_employee";
    String LOCATION_LIST = "location";
    String SCHEMA_LIST = "schemalist";
    String SOLR_CORE = "solrcore";
    String CERTIFICATES_LIST = "certificateslist";
    String CERTIFICATE_TYPES_LIST = "certificatetypeslist";
    String CONTRACT_LIST = "contractList";
    String EMPLOYEE_UPDATES_LIST = "employeeUpdatesList";
    String PRODUCT_UPDATES_LIST = "productUpdatesList";
    String OPPORTUNITY_UPDATES_LIST = "opportunityUpdatesList";
    String PERMISSION_HISTORY_LIST = "permissionHistoryList";
    String AUDIT_LOG_LIST = "auditLogList";
    String PAYBILLS_LIST = "payBillsList";
    String COURSE_SCHEDULES = "scheduledcourses";
    String RESOURCE_UTIL = "resourceUtil";

    String ISSUE = "issue";
    Integer REPORTING_DEFAULT_EXCELMAXROWCOUNT = 64000;

    //////////////CONTAINERS NAME///////////////
    String MYWORKSPACE = "myworkspace";
    String TASK = "task";
    String SEARCH = "search";
    String WORKSTREAM = "workstream";
    String PROJECT = "project";
    String PROJECT_GOAL_LIST_VIEW = "projectgoal";
    String SHIFT = "shift";
    String BRIGADA = "brigada";
    String ROTATION = "rotation";
    String GROUP_PLACEMENT = "groupPlacement";
    String ENABLED_DEPARTMENT = "ENABLED_DEPARTMENT";
    //    public static final String CONTRACT = "contract";
    String DEPARTMENT = "department";

    //////////////////////////////INVOICING/////////////////////////////
    String INVOICE = "invoice";

    String INVOICE_STATUS = "INVOICE_STATUS";
    String RELATED_INVOICE_NUMBER = "RELATED_INVOICE_NUMBER";
    String RELATED_INVOICE_DATE = "RELATED_INVOICE_DATE";
    String DRAFT = "DRAFT";
    String OPEN = "OPEN";
    String APPROVE = "APPROVE";
    String FAILED = "FAILED";
    String POST = "POST";
    String MANAGER_REJECT = "MANAGER_REJECT";
    String SUBMITTED_TO_MANAGER = "SUBMITTED_TO_MANAGER";
    String CLIENT_APPROVE = "CLIENT_APPROVE";
    String REJECT = "REJECT";
    String REVERSED = "REVERSED";
    String OVER_DUE = "OVER_DUE";
    String PAID = "PAID";
    String CONVERTED = "CONVERTED";
    String PARTIAL_CONVERTED = "PARTIAL_CONVERTED";
    String RECEIVED = "RECEIVED";
    String PARTIAL_RECEIVED = "PARTIAL_RECEIVED";
    String SALE_ORDER = "SALE_ORDER";
    String PICKED = "PICKED";
    String PACKED = "PACKED";
    String SHIPPED = "SHIPPED";
    String SHIPPING = "SHIPPING";
    String PARTIAL_SHIPPED = "PARTIAL_SHIPPED";
    String INVOICED = "INVOICE_STATUS_INVOICED";
    String PARTIAL_INVOICED = "PARTIAL_INVOICED";
    String INVOICE_STATUS_PENDING = "INVOICE_STATUS_PENDING";
    String APPROVE_AND_SEND = "APPROVE_AND_SEND";
    String INVOICE_STATUS_CLOSED = "INVOICE_STATUS_CLOSED";
    String APPROVE_MARK_AS_OPEN = "APPROVE_MARK_AS_OPEN";
    String EDIT_OPTION = "EDIT_OPTION";
    String ADD_COMPANY_EXPENSE = "ADD_COMPANY_EXPENSE";
    String ADD_EMPLOYEE_EXPENSE = "ADD_EMPLOYEE_EXPENSE";
    String REPORTED_ZATCA_STATUS = "REPORTED";


    String RENTAL_STATUS = "RENTAL_STATUS";
    String RENTAL_APPROVED = "RENTAL_APPROVED";
    String RENTAL_REJECTED = "RENTAL_REJECTED";
    String RENTAL_SUBMITTED = "RENTAL_SUBMITTED";
    String RENTAL_DELIVERY = "RENTAL_DELIVERY";
    String RENTAL_RETURNED = "RENTAL_RETURNED";
    String RENTAL_INVOICED = "RENTAL_INVOICED";

    String BUILD_ASSEMBLY_STATUS = "BUILD_ASSEMBLY_STATUS";
    String BUILD_ASSEMBLY_STATUS_DRAFT = "BUILD_ASSEMBLY_STATUS_DRAFT";
    String BUILD_ASSEMBLY_STATUS_SUBMITTED = "BUILD_ASSEMBLY_STATUS_SUBMITTED";
    String BUILD_ASSEMBLY_STATUS_APPROVED = "BUILD_ASSEMBLY_STATUS_APPROVED";
    String BUILD_ASSEMBLY_STATUS_REJECTED = "BUILD_ASSEMBLY_STATUS_REJECTED";
    String BUILD_ASSEMBLY_STATUS_UNBUILD = "BUILD_ASSEMBLY_STATUS_UNBUILD";

    String ORDER = "Sales Order";
    String QUOTE = "Quote";
    String PURCHASE_O = "Purchase Order";


    String INVOICE_CUSTOM_TYPE = "INVOICE_CUSTOM_TYPE";
    String PRODUCT_INVOICE = "PRODUCT_INVOICE";
    String DOLLAR_INVOICE = "DOLLAR_INVOICE";
    String SERVICE_INVOICE = "SERVICE_INVOICE";


    String DUPLICATE = "DUPLICATE";
    String UPDATED = "UPDATED";

    String NO_BILLING_ADDRESS = "You have not registered billing address for this client."
            + "Please register it by clicking on Add Address";

    String PERCENTAGE = "PERCENTAGE";
    String FIXED = "FIXED";

    String PAYROLL_CATEGORY_LIST = "payrollCategoryList";
    String PAYROLL_ZONE_LIST = "payrollZoneList";
    String MINIMUM_WAGE_LIST = "minimumWageList";
    String WAGE_RATE_LIST = "wageRateList";
    String PAYMENT_LIST = "paymentList";
    String PAYROLL_BATCH = "payrollBatchList";
    String PAYSLIP_LIST = "payslipList";
    String SINGLE_PAYRUN_LIST = "singlePayrunList";
    String PAYSLIP_TABLE_LIST = "payslipTableList";
    String TAXI_PAYRUN_LIST = "taxiPayrunList";
    String CASH_ADVANCE_LIST = "cashadvanceList";
    String PAYRUN_PAYMENT_LIST = "payrunPaymentList";
    String MULTI_CASH_ADVANCE_LIST = "multiCashadvanceList";
    String RECURRING_PAY_DEDUCTION_LIST = "recurringPayDeductionList";
    String ADDITIONAL_PAYMENT_LIST = "additionalpaymentList";
    String ADDITIONAL_PAYMENT_ITEM_LIST = "additionalpaymentItemList";
    String PENSION_PROVIDER_LIST = "pensionProviderList";
    String EMPLOYEE_TEMPLATE_LIST = "employeeTemplateList";
    String OVERTIME = "overtime";
    String EXPENSES_HOME = "expensesHomePage";
    String BANK_TRANSACTION_FORM = "BANK_TRANSACTION_FORM"; // Spend money or Receive money
    String EMPLOYEE_CONTRIBUTION = "EMPLOYEE_CONTRIBUTION"; // Spend money or Receive money
    String EMPLOYEER_CONTRIBUTION = "EMPLOYEER_CONTRIBUTION"; // Spend money or Receive money
    String BATCH_PAYMENT_FORM = "BATCH_PAYMENT_FORM"; //Revice Payment or Pay Bill form
    String BANK_CHECK_FORM = "BANK_CHECK_FORM"; //Check form
    String SINGLE_PAYRUN = "singlePayrun";
    String ADDITIONAL_PAYMENT_TEMPLATE = "additionalPayment";
    String CUSTOM_FORM_ITEM_VIEW = "CUSTOM_FORM_ITEM_VIEW";
    String PAYROLL_HOME = "payroll";
    String MY_PAYROLL_HOME = "myPayroll";
    String PAYROLL_REPORT_HOME = "payrollReports";
    String VENDOR_LIST = "vendorList";
    String SUBSCRIPTION_LIST = "subscriptionList";
    String USAGE_LIST = "usageList";

    String ADDITIONAL_PAYMENT_TYPE = "ADDITIONAL_PAYMENT";
    String BY_COMMISION_TYPE = "BY_COMMISION";


    ////////////// COMMONS//////////////////////
    String EMPLOYEE_CREATED_FROM_PM_GETTING_STARTED = "EMPLOYEE_CREATED_FROM_PM_GETTING_STARTED";

    String EMPLOYEE_CREATED_GOOGLE_MARKET_PLACE = "EMPLOYEE_CREATED_GOOGLE_MARKET_PLACE";

    /////////////////////////Contact Cateory From //////////////////////////////////
    String FROM_SIGNUP_CREATED = "FROM_SIGNUP_CREATED";
    String FROM_HIRED_PLACEMENT_CANDIDATE = "FROM_HIRED_PLACEMENT_CANDIDATE";
    String FROM_CRM_CONTACT_CATEGORY = "FROM_CRM_CONTACT_CATEGORY";
    String FROM_CONTACT = "fromContact";
    //////////////////////////////////////////////////////////////////////////////

    ///Company sign up related///////////////////////////////
    String SIGNED_UP_FROM_GOOGLE_MARKETPLACE = "SIGNED_UP_FROM_GOOGLE_MARKETPLACE";
    String SIGNED_UP_FROM_IPHONE = "SIGNED_UP_FROM_IPHONE";
    String SIGNED_UP_FROM_ANDROID = "SIGNED_UP_FROM_ANDROID";
    String SIGNED_UP_FROM_OPENID = "SIGNED_UP_FROM_OPENID";
    String SIGNED_UP_FROM_SUBSIDIARIES = "SIGNED_UP_FROM_SUBSIDIARIES";

    String GOOGLE_APP_DOMAIN = "GOOGLE_APP_DOMAIN";
    String GOOGLE_MARKETPLACE_USERS_IMPORT_POPUP_SHOW = "GOOGLE_MARKETPLACE_USERS_IMPORT_POPUP_SHOW";


    String EMPLOYEE_CREATED_FROM_ASSESSMENT = "EMPLOYEE_CREATED_FROM_ASSESSMENT";

    ///////////////ASSESSMENT//////////////////////
    String ASSESSMENT_360 = "ASSESSMENT_360";
    String ASSESSMENT_SIMPLE = "ASSESSMENT_SIMPLE";
    int ASSESSMENT_SKILLS_SIMPLE = 0;
    int ASSESSMENT_SKILLS_360 = 360;
    String PAWELLCOME = "pawelcomepage";
    String PA_HOME_VIEW = "home";
    String PA_CONATAINER_NAME = "pa";
    String ASSESSMENT_360_HOME = "home360";
    String PA_ARCHIVE = "viewStatus";
    String PA_PERIOD_LIST = "periodlist";
    String PA_APPROVAL_LIST = "approvallist";
    String VALIDITY_PERIOD_LIST = "validityperiodlist";

    String PA_360_SIMPLE_VIEW = "aSimple";
    String PA_360_MANAGER_VIEW = "aMan";

    String ASSESSMENT_STATUS = "ASSESSMENT_STATUS";
    String INITIATED = "INITIATED";
    String REVIEWED_BY_MANAGER = "REVIEWED_BY_MANAGER";
    String REVIEWED_BY_EMPLOYEE = "REVIEWED_BY_EMPLOYEE";
    String RATED = "RATED";
    String APPROVED_BY_MANAGER = "APPROVED_BY_MANAGER";
    String SAVED_AS_DRAFT = "SAVED_AS_DRAFT";
    String APPROVED_BY_HR = "APPROVED_BY_HR";
    String APPROVED = "APPROVED";
    String LR_STATUS_APPROVED = "SS_APPROVED";
    String REJECTED = "REJECTED";
    String SUBMITTED = "SUBMITTED";
    String PENDING = "PENDING";
    //Email Flags
    String FLAG_DELETED = "FLAG_DELETED";
    String FLAG_READ = "FLAG_READ";
    String FLAG_UNREAD = "FLAG_UNREAD";
    String VIEW_READ = "VIEW_READ";

    String DEFAULT_MAILER = "DEFAULT_MAILER";

    String WEBSOCKETS_CHANEL = "WEBSOCKETS_CHANEL";

    int MANAGERS_ONLY = 1;
    int CLIENTS_ONLY = 2;
    int PEERS_ONLY = 3;
    int NONE_COLLABORATORS = 4;

    String EMPLOYEES = "EMPLOYEES";
    String ADD_EMPLOYEES_WITH_TEAM = "EMPLOYEES_WITH_TEAM";
    String ADD_EMPLOYEES_WITH_LOCATION = "EMPLOYEES_WITH_LOCATION";
    String ADD_EMPLOYEES_WITH_TEAM_WITH_SALE = "EMPLOYEES_WITH_TEAM_WITH_SALE";
    String ADD_EMPLOYEES_WITH_LOCATION_WITH_SALE = "EMPLOYEES_WITH_LOCATION_WITH_SALE";

    boolean MANAGER_TURN = Boolean.TRUE;
    boolean EMPLOYEE_TURN = Boolean.FALSE;

    String PA_NOT_AVAILABLE_STRING = "N/A";
    String PA_UNACCEPTABLE_STRING = "UNA";
    String PA_VERY_WEAK_STRING = "VWK";
    String PA_WEAK_STRING = "WK";
    String PA_SATISFACTORY_STRING = "SAT";
    String PA_GOOD_STRING = "GD";
    String PA_VERY_GOOD_STRING = "VGD";
    String PA_EXCELLENT_STRING = "EXL";

    Integer PA_NOT_AVAILABLE_INT = 0;
    Integer PA_UNACCEPTABLE_INT = 1;
    Integer PA_VERY_WEAK_INT = 2;
    Integer PA_WEAK_INT = 3;
    Integer PA_SATISFACTORY_INT = 4;
    Integer PA_GOOD_INT = 5;
    Integer PA_VERY_GOOD_INT = 6;
    Integer PA_EXCELLENT_INT = 7;

    String IS_PEER = "IS_PEER";
    String IS_CLIENT = "IS_CLIENT";
    String IS_MANAGER = "IS_MANAGER";
    String IS_EMPLOYEE = "IS_EMPLOYEE";

    ////////////////ROLES NAME/////////////////////
    String DEPARTMENT_LEADER_STRING = "Department Leader";
    String EMPLOYEE_STRING = "Employee";

    String DEFAULT_SECTION = "Settings";

    ///////////////AVAILABILITY//////////////////////approveLeaveRequestView
    String AVAILA_HOME_VIEW = "availabilityHome";
    String TEAM_AVAILABILITY_VIEW = "teamAvailabilityView";
    String APPROVE_LEAVE_REQUEST_VIEW = "approveLeaveRequestView";
    String AVAILA_LANDING_VIEW = "availabilityLandingPage";
    String NULL = "null";
    String TIMESLOT = "timeslot";
    String HOLIDAY = "holiday";
    String ANNUAL_LEAVE_ALLOWANCE_LIST_VIEW = "annualLeaveAllownaceListView";
    String EMPLOYEE_BENEFIT_ALLOWANCE_LIST_VIEW = "employeeBenefitAllownaceListView";
    String FINGERPRINT_SETUP = "fingerprintSetup";
    String TERMINAL = "attendanceTerminal";

    String WORKFORCETRACK = "Workforcetrack";

    String DOMEN = "http://app.workforcetrack.com/";
    String DRUPAL_DOMEN = "http://www.workforcetrack.com/";

    String HOST_LIVE = "apps.kpi.com";
    String HOST_AWS = "dev.kpi.com";
    String HOST_MAILFORCETRACK = "mailforcetrack.com";
    String HOST_START_WORD_AWS = "aws";

    /**
     * Current constant is changed from googlecalendar to calendar. If some errors
     * occurs due to the change, please refer to Ilhom Jumayev for further info.
     */
    String GOOGLE_CALENDAR = "calendar";
    String GOOGLE_DOCUMENTS = "googledocuments";
    String GOOGLE_CONTACTS = "googlecontacts";
    String GOOGLE_ANALYTICS = "googleanalytics";
    String GOOGLE_MAIL = "googlemail";
    String USER_INFO = "userinfo";
    String GOOGLE_CALENDAR_CONTACTS = "googleCalendarContacts";
    String GOOGLE_DATA_COKIE = "googleDataCookie";
    String OFFICE_365_DATA_COKIE = "office365DataCookie";
    String OFFICE_365_DRIVE_COKIE = "office365DriveCookie";
    String OFFICE_365_CONTACT_COKIE = "office365ContactCookie";
    String OFFICE_365_MAIL = "office365mail";
    String WEBSITE_URL_COOKIE = "website_url";
    String OFFICE_365_EVENTS = "officeevents";
    Integer E_MAIL = 1;
    Integer ALERT = 2;
    Integer SMS = 3;
    Integer ALL = 4;
    Integer PUSH_NOTIFICATION = 5;

    String KPIMASTER = "KPIMASTER";
    String SERVERMASTER = "SERVERMASTER";

    String OFFICE_365_DOCUMENTS = "officedocuments";
    String OFFICE_365_CONTACTS = "officecontacts";


    //Used in google API services
    String APPLICATION_NAME = "wft-application";
    String PROTOCOL = "HTTP";
    String DOMAIN_NAME = "wft-domain";

    ////////////////EXPENSE/////////////////////////////////////
    String EXPENSE_STATUS = "EXPENSE_STATUS";
    String EXPENSE_DRAFT = "EXPENSE_DRAFT";
    String EXPENSE_SUBMITTED = "EXPENSE_SUBMITTED";
    String EXPENSE_APPROVED = "EXPENSE_APPROVED";
    String EXPENSE_DECLINED = "EXPENSE_DECLINED";
    String EXPENSE_PAID = "EXPENSE_PAID";
    String EXPENSE_REVERSED = "EXPENSE_REVERSED";
    String EXPENSE_CLOSED = "EXPENSE_CLOSED";
    String PARTIALLY_PAID = "PARTIALLY_PAID";

    String STOCK_TRANSFER_STATUS = "STOCK_TRANSFER_STATUS";
    String STOCK_TRANSFER_SUBMITTED = "SUBMITTED";
    String STOCK_TRANSFER_APPROVED = "APPROVED";
    String STOCK_TRANSFER_PENDING = "PENDING";
    String STOCK_TRANSFER_TRANSFERRED = "TRANSFERRED";
    String STOCK_TRANSFER_DECLINED = "DECLINED";
    String STOCK_TRANSFER_DRAFT = "DRAFT";


    String RFQ_STATUS = "RFQ_STATUS";
    String RFQ_SUBMITTED = "SUBMITTED";
    String RFQ_APPROVED = "APPROVED";
    String RFQ_DRAFT = "DRAFT";
    //    String RFQ_OPEN = "OPEN";
    String RFQ_PARTIAL_CONVERTED = "PARTIAL_CONVERTED";
    String RFQ_CONVERTED = "CONVERTED";
    String RFQ_DECLINED = "DECLINED";

    String EXPENSE_VIEW = "EXPENSE_VIEW";
    String EXPENSE_EDIT = "EXPENSE_EDIT";
    String IMPORT_EXPENSE_CLAIMS = "IMPORT_EXPENSE_CLAIMS";
    String IMPORT_COMPANY_EXPENSE_CLAIMS = "IMPORT_COMPANY_EXPENSES";

    String EVENT = "EVENT";
    String COMMENT = "COMMENT";

    String STOCK_ADJUSTMENT_STATUS = "STOCK_ADJUSTMENT_STATUS";
    String STOCK_ADJUSTMENT_SUBMITTED = "SUBMITTED";
    String STOCK_ADJUSTMENT_APPROVED = "APPROVED";
    String STOCK_ADJUSTMENT_PENDING = "PENDING";
    String STOCK_ADJUSTMENT_DECLINED = "DECLINED";
    String STOCK_ADJUSTMENT_DRAFT = "DRAFT";

    String PLACEMENT_STATUS = "_PLACEMENT_STATUS";
    String PLACEMENT_STATUS_SAVE_AS_DRAFT = "PLACEMENT_STATUS_SAVE_AS_DRAFT";
    String PLACEMENT_STATUS_APPROVED = "PLACEMENT_STATUS_APPROVED";
    String PLACEMENT_STATUS_REJECTED = "PLACEMENT_STATUS_REJECTED";
    String PLACEMENT_STATUS_HIRED = "PLACEMENT_STATUS_HIRED";
    String PLACEMENT_STATUS_SUBMITTED = "PLACEMENT_STATUS_SUBMITTED";

    ////////////////Manual Journal Status/////////////////////////////////////
    String MANUAL_JOURNAL_STATUS = "MANUAL_JOURNAL_STATUS";

    ///////////////Landing Pages//////////////////////////////////
    String PRM = "pm";
    String PRM2 = "pm2";
    String PA = "pa";
    String PA2 = "pa2";
    String AVA = "ava";
    String EXP = "exp";
    String INV = "inv";
    String ACC = "acc";
    String ERP = "erp";
    String MY_WORKSPACE = "my_workpsce";

    String ALL_SERVICES = "all_service";
    String MOBILE_SERVICE = "mobile_service";
    ///////////////Roles//////////
    int READ = 1;
    int EDIT = 2;
    String COPY = "COPY";

    //////////////STATUS_MESSAGES///////////////
    int INFO = 0;
    int SUCCESS = 1;
    int WARNING = 2;
    int ERROR = 3;
    int VALIDATION = 4;
    int LIMIT_EXCEEDED = 5;

    ////////////CostCalculation paramentrs/////////////////////////////////
    double VAT_RATE = 0.20;

    String FREE_TRIAL = "FREE_TRIAL";
    String IS_PAID = "IS_PAID";

    String ONE_MONTH_0 = "ONE_MONTH_0";
    String THREE_MONTH_15 = "THREE_MONTH_15";
    String SIX_MONTH_20 = "SIX_MONTH_20";
    String TWELVE_MONTH_TWENTY_30 = "TWELVE_MONTH_TWENTY_30";
    String TWO_YEARS_45 = "TWO_YEARS_45";

    float perStorageCost = 2.00f;
    String paypal_LINK_Live = "www.paypal.com/cgi-bin/webscr";
    String paypal_LINK_Test = "www.sandbox.paypal.com/cgi-bin/webscr";

    String paypal_ACCOUNT_Live = "payments@kpi.com";
    String paypal_ACCOUNT_Test = "sheral_1228381642_biz@gmail.com";//pass: 12345678        sher.p_1227768170_per@gmail.com

    String PAYME_DOMAIN_LIVE = "https://checkout.paycom.uz";
    String PAYME_DOMAIN_TEST = "https://test.paycom.uz";

    String CLICK_DOMAIN_LIVE = "https://my.click.uz/services/pay";

    int PERIOD_YEAR_TO_DATE = 0;
    int PERIOD_MONTH_TO_DATE = 1;
    int PERIOD_WEEK_TO_DATE = 2;
    int PERIOD_DAY_TO_DATE = 3;
    int PERIOD_PER_YEAR = 4;
    int PERIOD_PER_MONTH = 5;
    int PERIOD_PER_WEEK = 7;
    int PERIOD_PER_COMPANY_START_DATE = 8;


    String SUBSCRIPTION_UPG = "upg";
    String SUBSCRIPTION_ADD = "add";
    String SUBSCRIPTION_SF = "sf"; //subscription storefront

    String SUBSCR_CANCEL = "subscr_cancel";
    String SUBSCR_SIGNUP = "subscr_signup";
    String SUBSCR_MODIFY = "subscr_modify";
    String SUBSCR_PAYMENT = "subscr_payment";
    String durationDays = " coalesce(SUM(CASE WHEN sickR.includeDayOffs then s.day WHEN s.holiday = false THEN s.day ELSE (CASE WHEN s.holidayfromannualleave = true THEN 1 ELSE 0 END) END), 0) as days ";
    String DAY = "DAY";
    String MONEY = "MONEY";
    String USED_ANOHTER_LEAVE_OR_RECALL = "USED_ANOHTER_LEAVE_OR_RECALL";

    //WorldPay

    String WORLDPAY_LINK_Live = "https://secure.worldpay.com/wcc/purchase";

    String WORLDPAY_ACCOUNT_Live = "1026342";
    String WORLDPAY_ACCOUNT_TEST = "1026342";

    String MERCHANT_CANCELLED = "Merchant Cancelled";
    String CUSTOMER_CANCELLED = "Customer Cancelled";
    String WORLDPAY_TRANSACTION_SUCCESSFULL = "Y";


    String MYSELF = "Myself";
    int NOT_STARTED = 2;
    int COMPLETED = 79;

    /* Project status */
    int ONGOING = 74;
    String PS_ONGOING = "ONGOING";
    String PS_COMPLETED = "PS_COMPLETED";
    String PS_CLOSED = "PS_CLOSED";
    String PS_CLIENT_APPROVED = "APPROVED_BY_CLIENT";
    String PS_CLIENT_REJECTED = "REJECTED_BY_CLIENT";

    /**
     * ********************************************Accounting*******************************************************
     */
    String ACCOUNTING_SECTION = "ACCOUNTING_SECTION";

    Integer PAYMENT = 1;
    Integer DEDUCTION = 0;
    Integer LOAN = 2;


    String TARGET_URL = "TARGET_URL";
    String TARGET_USERNAME = "TARGET_USERNAME";
    String TARGET_PASSWORD = "TARGET_PASSWORD";
    String TARGET_CONTROLLER = "TARGET_CONTROLLER";

    String TAWK_TO_SITE_ID = "TAWK_TO_SITE_ID";

    String SEX = "SEX";
    //Payroll Settings
    String PAYROL_SETTINGS = "PAYROL_SETTINGS";
    String FORMS = "FORMS";

    String PREV_TAX_CODE = "PREV_TAX_CODE";
    String PREV_WK1MTH1 = "PREV_WK1MTH1";
    String WEEK_MONTH = "WEEK_MONTH";
    String WEEK_MONTH_TYPE = "WEEK_MONTH_TYPE";
    String WEEK_MONTH_NUMBER = "WEEK_MONTH_NUMBER";
    String HAVE_ANOTHER_JOB = "HAS_ANOTHER_JOB";
    String FAMILY_STATUS = "FAMILY_STATUS";
    String NI_NUMBER = "NI_NUMBER";
    String NI_TABLE_LETTER = "NI_TABLE_LETTER";
    String TAX_CODE = "TAX_CODE";
    String WK1MNTH1 = "WK1MNTH1";
    String NOT_MEMBER_OF_COOPS = "NOT_MEMBER_OF_COOPS";
    String PENSION_SCHEME = "PENSION_SCHEME";
    String TOTAL_PAY_TO_DATE = "TOTAL_PAY_TO_DATE";
    String TOTAL_TAX_TO_DATE = "TOTAL_TAX_TO_DATE";
    String PAY_FREQUENCY = "PAY_PERIOD";
    String PAY_METHOD = "PAY_METHOD";
    String STUDENT_LOAN = "STUDENT_LOAN";
    String EMPLOYEE_BONUS_TYPE = "EMPLOYEE_BONUS_TYPE";
    String EMPLOYEE_BONUS_VALUE = "EMPLOYEE_BONUS_VALUE";
    String EMPLOYEE_COMMISSION = "EMPLOYEE_COMMISSION";

    String EMPLOYEE_RESIGNATION = "EMPLOYEE_RESIGNATION";
    String CONTRACT_TERMINATION = "CONTRACT_TERMINATION";
    String END_OF_SERVICE = "END_OF_SERVICE";
    String END_OF_SERVICE_GRATUITY = "endOfServiceGratuity";
    String WPS_REPORT = "wpsReport";
    String END_OF_SERVICE_REPORT = "endOfServiceReport";
    String PENSION_CONTRIBUTION_REPORT = "pensionContributionReport";
    String CASH_ADVANCE_REPORT = "cashAdvanceReport";
    String SALARY_REPORT = "salaryReport";
    String SALARY_TRANSACTIONS = "salaryTransactions";
    String DISCRETIONARY_ALLOWANCE = "DISCRETIONARY_ALLOWANCE";
    String PETROL_LIMIT_EXCESS = "PETROL_LIMIT_EXCESS";
    Integer PAYMENT_TYPE = 0;
    String PAYRUN_STATUS = "PAYRUN_STATUS";
    String SALARY_CATEGORY = "SALARY_CATEGORY";
    Integer DEFAULT_NUMBER_OF_WORK_DAYS = 30;
    Integer DEFAULT_START_DATE_VALUE = 1;
    String CONTRACT_START_DATE = "CONTRACT_START_DATE";
    String CONTRACT_END_DATE = "CONTRACT_END_DATE";

    String GREATER_THEN = "GREATER_THEN";
    String EQUAL_TO = "EQUAL_TO";
    String BETWEEN = "BETWEEN";
    String LESS_THAN = "LESS_THAN";
    String BASIC_SALARY = "BASIC_SALARY";
    String ALLOWANCE = "ALLOWANCE";
    String BONUS = "BONUS";
    String MONTHLY_INCOME = "MONTHLY_INCOME";
    String GROSS_COMMISSION = "GROSS_COMMISSION";
    String USED_PETROL = "USED_PETROL";
    String ADVANCE_HOLIDAY = "ADVANCE_HOLIDAY";
    String STATUTORY_SICK_PAY = "SSP";
    String STATUTORY_ADOPTION_PAY = "SAP";
    String STATUTORY_MATERNITY_PAY = "SMP";
    String STATUTORY_PATERNITY_PAY = "SPP";
    String STATUTORY_PATERNITY_PAY_ADOPT = "SPPA";
    String EMPLOYER_SUBSIDY_PAY = "EMPLOYER_SUBSIDY_PAY";
    String EXPENSE_REPORT = "EXPENSE_REPORT";
    String EXPENSE_PAYMENT_FORM = "EXPENSE_PAYMENT_FORM";
    String BILLABLE_EXPENSE_FORM = "BILLABLE_EXPENSE_FORM";

    String INVOICE_PAYMENT = "INVOICE_PAYMENT";

    String SUPPORT_PERSON = "SUPPORT_PERSON";

    //Deductions
    String INCOME_TAX = "INCOME_TAX";
    String EMPLOYEE_NI = "EMPLOYEE_NI";
    String EMPLOYER_NI = "EMPLOYER_NI";
    String EMPLOYEE_PENSION_DEDUCTION = "EMPLOYEE_PENSION_DEDUCTION";
    String EMPLOYER_PENSION_DEDUCTION = "EMPLOYER_PENSION_DEDUCTION";
    String PENSION_DEDUCTION = "PENSION_DEDUCTION";
    String STUDENT_LOAN_DEDUCTION = "STUDENT_LOAN_DEDUCTION";
    String ATTACHMENTS_OF_EARNINGS_ORDER = "ATTACHMENTS_OF_EARNINGS_ORDER";
    String CASH_ADVANCE = "CASH_ADVANCE";

    String NI_TAX = "NI_TAX";


    int PENSION_DEDUCTION_TYPE_FIXED_RATE_CONTRIBUTION = 0;
    int PENSION_DEDUCTION_TYPE_PERCENTAGE_RATE_CONTRIBUTION = 1;
    int PENSION_DEDUCT_FROM_GROSS_PAY = 0;
    int INSUFFICIENT_WAGES_DEDUCT_AS_MUCH_AVAILABLE = 0;
    int INSUFFICIENT_WAGES_NO_DEDUCTIOIN_MADE = 1;
    int INSUFFICIENT_WAGES_DEDUCT_FULL_CONTRIBUTION_FROM_EMPLOYER_SUIBSIDY = 2;

    String PAYSLIP_STATUS = "PAYSLIP_STATUS";
    String PAYSLIP_STATUS_DRAFT = "PS_DRAFT";
    String PAYSLIP_STATUS_COMMITED = "COMMITED";
    String PAYRUN_STATUS_DRAFT = "PY_DRAFT";
    String PAYRUN_STATUS_SUBMITTED = "PY_SUBMITTED_TO_MANAGER";
    String PAYRUN_STATUS_OPEN = "PY_OPEN";
    String PAYRUN_STATUS_APPROVED = "PY_APPROVED";
    String PAYRUN_STATUS_REJECTED = "PY_REJECTED";
    String PAYRUN_STATUS_PROCESSING = "PY_PROCESSING";
    String PAYRUN_STATUS_PENDING = "PY_PENDING";
    String PAYRUN_STATUS_PARTIAL_PAID = "PY_PARTIAL_PAID";
    String PAYRUN_STATUS_PAID = "PY_PAID";

    String PAYMENT_STATUS = "PAYMENT_STATUS";
    String PAYMENT_STATUS_DRAFT = "PAYMENT_DRAFT";
    String PAYMENT_STATUS_SUBMITTED = "PAYMENT_SUBMITTED";
    String PAYMENT_STATUS_APPROVED = "PAYMENT_APPROVED";
    String PAYMENT_STATUS_REJECTED = "PAYMENT_REJECTED";

    String PAYMENT_STATUS_PARTIAL_PAID = "PAYMENT_PARTIAL_PAID";

    String PAYMENT_STATUS_PAID = "PAYMENT_PAID";

    String WAREHOUSE_ID = "WAREHOUSE_ID";

    //==================================================================================================================

    String JOB_TYPE = "JOB_TYPE";//Payment Settings
    String JOB_TYPE_D = "JOB_TYPE_D";
    // public static final String JOB_TITLE = "JOB_TITLE";
    String JOB_TITLE_TEXT = "JOB_TITLE_TEXT";
    String SALARY = "SALARY";
    String OVERTIME_RATE = "OVERTIME_RATE";
    String NORMAL_RATE = "NORMAL_RATE";
    String SALARY_GROSS_NET = "SALARY_GROSS_NET";
    String RATE_TYPE = "RATE_TYPE";
    String FIXED_RATE = "FIXED_RATE";
    String TIMESHEET_ONLY_RATE = "TIMESHEET_ONLY_RATE";
    String FIXED_TIMESHEET_OVERTIME_RATE = "FIXED_TIMESHEET_OVERTIME_RATE";
    String FIXED_ATTENDANCE_REPORT_OVERTIME_RATE = "FIXED_ATTENDANCE_REPORT_OVERTIME_RATE";
    String FIXED_HRMS_OVERTIME_RATE = "FIXED_HRMS_OVERTIME_RATE";
    String FIXED_OVERTIME_RATE = "FIXED_OVERTIME_RATE";
    String HRMS_HOURS_RATE = "HRMS_HOURS_RATE";
    String TIMESHEET_RATE = "TIMESHEET_RATE";

    /*Employer Payroll Settings*/
    String OFFICE_NAME = "OFFICE_NAME";
    String OFFICE_NUMBER = "OFFICE_NUMBER";
    String PAYE_REF_NUMBER = "PAYE_REF_NUMBER";
    String AUTHORIZED_TAX_REFUND = "AUTHORIZED_TAX_REFUND";
    String GATEWAY_USER_ID = "GATEWAY_USER_ID";
    String GATEWAY_USER_PASSWORD = "GATEWAY_USER_PASSWORD";
    String VAT_REGISTRATION_NUMBER = "VAT_REGISTRATION_NUMBER";
    String COUNTRY_ID = "COUNTRY_ID";
    String COUNTRY_NAME = "COUNTRY_NAME";
    String COMPANY_CODE = "COMPANY_CODE";
    String WPS_NO = "WPS_NO";
    String ADDRESS1 = "ADDRESS1";
    String ADDRESS2 = "ADDRESS2";
    String CR_NO = "CR_NO";
    String GOSI_NO = "GOSI_NO";
    String LICENSE_NO = "LICENSE_NO";
    String WEBSITE = "WEBSITE";
    String NUMBER_OF_EMPLOYEE_ID = "NUMBER_OF_EMPLOYEE_ID";
    String INDUSTRY_ID = "INDUSTRY_ID";
    String VISA_ALLOWANCE_LIMITS = "VISA_ALLOWANCE_LIMITS";
    String ACCOUNTING_AUDIT_FILE_DATE = "ACCOUNTING_AUDIT_FILE_DATE";
    String LICENSE_START_DATE = "LICENSE_START_DATE";
    String PHONE_NO = "PHONE_NO";
    String GOSI_EXPIRY_DATE = "GOSI_EXPIRY_DATE";
    String CR_EXPIRY_DATE = "CR_EXPIRY_DATE";
    String LICENSE_EXPIRY_DATE = "LICENSE_EXPIRY_DATE";
    String BANK_ACCOUNT_ID = "BANK_ACCOUNT_ID";
    String BANK_ACCOUNT_NAME = "BANK_ACCOUNT_NAME";
    String B_ACCOUNT_CODE = "B_ACCOUNT_CODE";
    String B_ACCOUNT_NUMBER = "B_ACCOUNT_NUMBER";
    String CASH_ACCOUNT_NAME = "CASH_ACCOUNT_NAME";
    String PAYMENT_POLICY = "PAYMENT_POLICY";
    String EXPENSE_PAID_ACCOUNT = "EXPENSE_PAID_ACCOUNT";
    String EXPENSE_PAID_ACCOUNT_NAME = "EXPENSE_PAID_ACCOUNT_NAME";
    String ENABLED_DOUBLE_APPROVER_PAYRUN = "ENABLED_DOUBLE_APPROVER_PAYRUN";
    String ENABLED_LEAVE_DEDUCTIONS = "ENABLED_LEAVE_DEDUCTIONS";
    String ENABLED_LEAVE_PAYMENTS = "ENABLED_LEAVE_PAYMENTS";
    String DISABLE_PAYROLL_TRANSACTIONS = "DISABLE_PAYROLL_TRANSACTIONS";
    String NUMBER_OF_WORK_DAYS = "NUMBER_OF_WORK_DAYS";
    String DEFAULT_START_DATE = "DEFAULT_START_DATE";
    String DEDUCT_TYPE = "DEDUCT_TYPE";
    String TIMESHEET_HOURS_CALCUTATION_TYPE = "TIMESHEET_HOURS_CALCUTATION_TYPE";
    String LEAVE_DAILY_PAYMENT_TYPE = "LEAVE_DAILY_PAYMENT_TYPE";
    String LEAVE_MONEY_PAYMENT_TYPE = "LEAVE_MONEY_PAYMENT_TYPE";
    String DEDUCT_ALLOWANCES = "DEDUCT_ALLOWANCES";
    String TIMESHEET_HOURS_ALLOWANCES = "TIMESHEET_HOURS_ALLOWANCES";
    String LEAVE_DAILY_ALLOWANCES = "LEAVE_DAILY_ALLOWANCES";
    String LEAVE_MONEY_ALLOWANCES = "LEAVE_MONEY_ALLOWANCES";
    String ADDITIONAL_PAYMENT_ALLOWANCES = "ADDITIONAL_PAYMENT_ALLOWANCES";
    String LEAVE_DEDUCTIONS = "LEAVE_DEDUCTIONS";
    String LEAVE_ENCHASHMENT = "LEAVE_ENCHASHMENT";
    String LEAVE_SALARY = "LEAVE_SALARY";
    String VACATION_PAY = "214";
    String STUDY_LEAVE_PAY = "216";
    String TAKE_BY_MONEY_PAY = "175";
    String SICK_LEAVE_PAYMENT_CODE = "213";
    String MATERNITY_LEAVE_PAYMENT_CODE = "213";
    String SICK_LEAVE_PAYMENT = "SICK_LEAVE_PAYMENT";
    String LEAVE_MONEY_TYPE_CATEGORY = "LEAVE_MONEY_TYPE_CATEGORY";
    String BENEFIT_PAYMENT = "BENEFIT_PAYMENT";
    String REGULAR_OVERTIME = "REGULAR_OVERTIME";
    String WEEKEND_OVERTIME = "WEEKEND_OVERTIME";
    String HOLIDAY_OVERTIME = "HOLIDAY_OVERTIME";
    String REGULAR_OVERTIME_RATE = "REGULAR_OVERTIME_RATE";
    String REGULAR_OVERTIME_RATE_TYPE = "REGULAR_OVERTIME_RATE_TYPE";
    String REGULAR_OVERTIME_CATEGORY_ID = "REGULAR_OVERTIME_CATEGORY_ID";
    String REGULAR_OVERTIME_CATEGORY_NAME = "REGULAR_OVERTIME_CATEGORY_NAME";
    String WEEKEND_OVERTIME_RATE = "WEEKEND_OVERTIME_RATE";
    String WEEKEND_OVERTIME_RATE_TYPE = "WEEKEND_OVERTIME_RATE_TYPE";
    String WEEKEND_OVERTIME_CATEGORY_ID = "WEEKEND_OVERTIME_CATEGORY_ID";
    String WEEKEND_OVERTIME_CATEGORY_NAME = "WEEKEND_OVERTIME_CATEGORY_NAME";
    String HOLIDAY_OVERTIME_RATE = "HOLIDAY_OVERTIME_RATE";
    String HOLIDAY_OVERTIME_RATE_TYPE = "HOLIDAY_OVERTIME_RATE_TYPE";
    String HOLIDAY_OVERTIME_CATEGORY_ID = "HOLIDAY_OVERTIME_CATEGORY_ID";
    String HOLIDAY_OVERTIME_CATEGORY_NAME = "HOLIDAY_OVERTIME_CATEGORY_NAME";
    String HOUSING_ALLOWANCE = "HOUSING_ALLOWANCE";
    String FOOD_ALLOWANCE = "FOOD_ALLOWANCE";
    String ADDITIONAL_PAYMENT = "ADDITIONAL_PAYMENT";
    String REMAINING_PREV_MONTH_PAYMENT = "REMAINING_PREV_MONTH_PAYMENT";
    String ABSENCE_DEDUCTIONS = "ABSENCE_DEDUCTIONS";
    String REMAINING_PREV_MONTH = "REMAINING_PREV_MONTH_";
    String REMAINING_PREV_MONTH_PAYMENT_ONE = "REMAINING_PREV_MONTH_PAYMENT_ONE";
    String REMAINING_PREV_MONTH_PAYMENT_TWO = "REMAINING_PREV_MONTH_PAYMENT_TWO";
    String REMAINING_PREV_MONTH_DEDUCTION_ONE = "REMAINING_PREV_MONTH_DEDUCTION_ONE";
    String REMAINING_PREV_MONTH_DEDUCTION_TWO = "REMAINING_PREV_MONTH_DEDUCTION_TWO";
    String DOUBLE_CONFIRMATION = "DOUBLE_CONFIRMATION";
    String DAILY_RATE_BY_EMPLOYER_SETTINGS = "DAILY_RATE_BY_EMPLOYER_SETTINGS";
    String OVERTIME_RATE_BY_EMPLOYER_SETTINGS = "OVERTIME_RATE_BY_EMPLOYER_SETTINGS";
    String OVERTIME_FIXED = "OVERTIME_FIXED";
    String ALLOWANCE_LIVING = "ALLOWANCE_LIVING";
    String COST_LIVING_ALLOWANCE = "COST_LIVING_ALLOWANCE";
    String TRANSPORTATION_ALLOWANCE = "TRANSPORTATION ALLOWANCE";
    String SALARY_CURRENCY = "SALARY_CURRENCY";
    String SALARY_CURRENCY_SYMBOL = "SALARY_CURRENCY_SYMBOL";
    String DAYS_OF_MONTH = "DAYS_OF_MONTH";
    String CURRENCY_NAME = "CURRENCY_NAME";
    String SALARY_RATE = "SALARY_RATE";
    String SALARY_RATE_TYPE = "SALARY_RATE_TYPE";
    String WORKED_DAYS = "WORKED_DAYS";
    String WORKED_HOURS = "WORKED_HOURS";
    String MONTHLY_SALIK = "MONTHLY_SALIK";
    String CLIENT_NAME = "CLIENT_NAME";
    String BY_DEFAULT_EMAIL_NOTIFICATION = "BY_DEFAULT_EMAIL_NOTIFICATION";
    String MULTI_CURRENCY_FOR_PAYROLL = "MULTI_CURRENCY_FOR_PAYROLL";
    String LEAVE_DAYS_IMPACT = "LEAVE_DAYS_IMPACT";
    String NON_PAID_LEAVE_DAYS_IMPACT = "NON_PAID_LEAVE_DAYS_IMPACT";
    String SHOW_YEAR_TO_DATE = "SHOW_YEAR_TO_DATE";
    String ALLOW_COPY_ALLOWANCE_FROM_PREVIOUS = "ALLOW_COPY_ALLOWANCE_FROM_PREVIOUS";
    String PREVIOUS_ALLOWANCE_COPY_PCT = "PREVIOUS_ALLOWANCE_COPY_PCT";
    String USAGE_DEADLINE = "ALLOWANCE_USAGE_DEADLINE";
    String PAY_REMAINING_ALLOWANCE = "PAY_REMAINING_ALLOWANCE";
    String BACKUPS_EMPLOYEE = "backupsEmployee";
    /**/
    String WEEKORMONTH_START_DATE = "FROM_DATE";
    String WEEKORMONTH_END_DATE = "END_DATE";

    String PAY_FREQUENCY_WEEKLY = "Weekly";
    String PAY_FREQUENCY_MONTHLY = "Monthly";
    String PAY_FREQUENCY_ANNUAL = "Annual";
    String PAY_FREQUENCY_2_WEEKLY = "2 Weekly";
    String PAY_FREQUENCY_4_WEEKLY = "4 Weekly";

    SelectItem[] PEOPLE_TITLE_OTHER = new SelectItem[]{
            new SelectItem(0, "Mr."),
            new SelectItem(1, "Mrs."),
            new SelectItem(2, "Miss."),
            new SelectItem(3, "Ms."),
            new SelectItem(4, "Dr."),
            new SelectItem(5, "Other")};

    /**
     * ********************************************Accounting*******************************************************
     */
    String ACCOUNTING_WELCOME = "accountingWelcome";
    String SALE_INVOICE = "saleinvoice";//receivable
    String POSITION1 = "position";//receivable
    String SALE_INVOICE_INTR = "saleinvoiceintr";
    String SALE_QUOTE_INTR = "salequoteintr";
    String SALE_ORDER_INTR = "saleorderintr";
    String REQUEST_FOR_QUOTE_INTR = "rfqintr";
    String PACKING_SLIP = "packingslip";
    String SO_PACKING_SLIP = "sopackingslip";
    String PROJECT_BASED_INVOICE = "projectbasedinvoice";
    String RECURRING_INVOICE = "recurringinvoice";
    String SALE_QUOTE = "salequote";
    String SALE_ORDER_CODE = "saleorder";
    String PURCHASE_INVOICE = "purchaseinvoice";
    String RECURRING_BILL = "recurringbill";
    String REQUEST_FOR_QUOTE = "requestforquote";
    String REQUEST_FOR_PURCHASE = "requestforpurchase";
    String PURCHASE_ORDER = "purchaseorder";
    String RECEIVABLE_CREDIT_NOTE = "receivablecreditnote";
    String PAYABLE_CREDIT_NOTE = "payablecreditnote";
    String MANUAL_JOURNAL = "manualjournal";
    String CREDIT_NOTE = "creditnote";
    String FIXED_ASSETS = "fixedassets";
    String INVENTORY_ITEMS = "inventoryitems";
    String RENTAL_PRODUCTS = "rentalProducts";
    String RENTAL_ORDERS = "rentalOrders";
    String ASSEMBLY_PRODUCTS = "assemblyItems";
    String BUILD_ASSEMBLY_PRODUCTS = "buildAssemblyItem";
    String GROUP_PAYRUN = "grouppayrun";
    String SHIPPING_DATA = "shippingdata";
    String PRODUCTS_OR_SERVICES = "productsOrServices";
    String PRODUCTS = "PRODUCTS";
    String IVENTORY_STOCK_ADJUSTMENT = "iventoryStockAdjustment";
    String BANKACCOUNT = "bankaccount";
    String CUSTOMER_PREPAYMENT = "customerPrepayment";
    String TRASH_BIN = "trashBin";
    String EXPENSE_REPORTS = "expenseReports";
    String SALES_QUOTE_RENTAL_ORDER = "salesQuoteRentalOrder";

    String RECEIVABLE = "RECEIVABLE";//For sales types.
    String PAYABLE = "PAYABLE";//For purchases type.
    String RECEIVABLE_PAYABLE = "RECEIVABLE_PAYABLE";

    String PRICE_LEVEL_LIST = "priceLevelList";
    String DISCOUNT_LIST = "discountList";
    String BUG_LIST = "bugList";
    String BLACK_LIST = "blackList";

    String INVOICE_QUOTE_FORM = "INVOICE_QUOTE_FORM";
    String MANUAL_TRANSACTIONS = "manualtransactions";

    //////////////////////////////////////////////Accounting//////////////////////////////////////////////////////////
    String JOURNAL_ID = "journalId";
    String JOURNAL_DATE = "journalDate";
    String ACC_NAME = "name";
    String ACC_CODE = "code";

    String RECONCILED = "RECONCILED";
    String UNRECONCILED = "UNRECONCILED";
    String MARKED_AS_RECONCILED = "MARKED_AS_RECONCILED";
    String PARTLY_RECONCILED = "PARTLY_RECONCILED";
    // CHECKLIST STATUS //
    String POST_DATED = "POST_DATED";
    String POSTED = "POSTED";
    // MANUAL JOURNAL TYPE //
    String SINGLE = "SINGLE";
    String RECURRING = "RECURRING";

    ////////////////////////////////////////////////Account Type///////////////////////////////////////////////////////
    String ASSETS = "ASSETS";
    String LIABILITIES = "LIABILITIES";
    String EQUITY = "EQUITY";
    String EXPENSES = "EXPENSES";
    String REVENUE = "REVENUE";
    String CURRENT_ASSET = "CURRENT_ASSET";
    String TAX = "TAX";

    String COST_OF_SALES = "COST_OF_SALES";

    String PURCHASES_STR = "PURCHASES";

    String EXPENSES_AND_CURRENT_ASSET = "EXPENSES_AND_CURRENT_ASSET";

    ////////////////////////////////////TASK RELATED/////////////////////////////
    Integer ALL_DUE_TASKS = 99999;
    String _COMPLETED = "COMPLETED";
    String CANCELLED = "CANCELLED";
    String ON_HOLD = "ON_HOLD";
    String _CLOSED = "CLOSED";

    String ACTIVE_MENU = "active_menu";
    String PROJECT_MANAGEMENT_PAGE = "ProjectManagement";
    String DASHBOARD_PAGE = "Dashboard";
    String REPORTING_PAGE = "Reporting";
    String SETTINGS_PAGE = "Settings";
    String TRAINING_CENTER_PAGE = "TrainingCenter";
    String ACCOUNTING_PAGE = "Accounting";
    String PAYROLL_PAGE = "Payroll";
    String MYACCOUNT_PAGE = "Myaccount";
    String CRM_PAGE = "Crm";
    String WORKSPACE_PAGE = "Workspace";
    String HRMS_PAGE = "Hrms";
    String LOGISTICS_PAGE = "Logistics";
    String MC_PAGE = "MessageCenter";
    String MY_WORKSPACE_PAGE = "Workspace";
    String DOC_MY_FOLDER = "Documents";
    String DOC_INTRANET = "Intranet";//for robert
    /////////////////////////////ISSUE RELATED////////////////////////////////
    String EMPLOYEE_ISSUE = "EMPLOYEE_ISSUE";
    String PROJECT_ISSUE = "PROJECT_ISSUE";

    String PUBLIC_ISSUE = "PUBLIC";
    String PRIVATE_ISSUE = "PRIVATE";
    String INTERNAL_ISSUE = "INTERNAL";

    /////////////////////////////////////REPORT FITERS/////////////////////////
    String FILTER_PROJECT = "Project";
    String FILTER_DEPARTMENT = "Department";
    String FILTER_CLIENT = "Client";
    String FILTER_EMPLOYEE = "Employee";
    String FILTER_WORKSTREAM = "Workstream";
    String FILTER_DATE = "Date";
    String FILTER_LEAVEREQUEST_TYPE = "Type";
    String FILTER_LEAVEREQUEST_REASON = "Reason";

    ///////////////////////////////////// TIMETRACK STATUSES /////////////////////////
    String TIME_TRACK_STATUS = "_TIMETRACK_STATUS";
    String AVAILABLE = "AVAILABLE";
    String BREAK = "BREAK";
    String ON_DUTY = "ON_DUTY";
    String ON_LEAVE = "ON_LEAVE";
    String NOT_AVAILABLE = "NOT_AVAILABLE";

    ///////////////////////////////////EMPLOYEE STATUS//////////////////////////////
    String EMPLOYEE_STATUS = "_EMPLOYEE_STATUS";
    String EMPLOYEE_STATUS_ACTIVE = "ACTIVE_EMPLOYEE";
    String EMPLOYEE_STATUS_PENDING = "PENDING_EMPLOYEE";
    String EMPLOYEE_STATUS_INACTIVE = "INACTIVE_EMPLOYEE";
    String EMPLOYEE_STATUS_NO_ACCCESS = "NO_ACCESS_EMPLOYEE";
    String EMPLOYEE_STATUS_RESIGNED = "RESIGNED_EMPLOYEE";

    ///////////////////////////////////DOCUMENTS CONTENT_TYPE//////////////////////////////
    String DOC_EXCEL = "application/vnd.ms-excel";
    String DOC_PDF = "application/pdf";

    String UNDEFINED_USER_AGENT = "undefinedUserAgent";

    //Transaction Types
    String INVOICE_TRANSACTION = "INVOICE_TRANSACTION";
    String INVOICEPAYMENT_TRANSACTION = "INVOICEPAYMENT_TRANSACTION";
    String PAYMENTREFUND_CLOSED_TRANSACTION = "PAYMENTREFUND_CLOSED_TRANSACTION";
    String CREDITEDINVOICE_TRANSACTION = "CREDITEDINVOICE_TRANSACTION";
    String EXPENSE_TRANSACTION = "EXPENSE_TRANSACTION";
    String EXPENSEPAYMENT_TRANSACTION = "EXPENSEPAYMENT_TRANSACTION";
    String PAYMENT_TRANSACTION = "PAYMENT_TRANSACTION";
    String OVER_PAYMENT_TRANSACTION = "OVER_PAYMENT_TRANSACTION";
    String PAYMENT_REFUND_TRANSACTION = "PAYMENT_REFUND_TRANSACTION";
    String MANUAL_TRANSACTION = "MANUAL_TRANSACTION";
    String MANUAL_TRANSACTION_TALLY = "MANUAL_TRANSACTION_TALLY";
    String BANK_OPENING_BALANCE_TRANSACTION = "BANK_OPENING_BALANCE_TRANSACTION";
    String BANK_MONEY_TRANSFER_TRANSACTION = "BANK_MONEY_TRANSFER_TRANSACTION";
    String BANK_TRANSFER_TRANSACTION = "BANK_TRANSFER_TRANSACTION";
    String BANK_CHECK_TRANSACTION = "BANK_CHECK_TRANSACTION";
    String INVENTORY_TRANSACTION = "INVENTORY_TRANSACTION";
    String ADJUSTMENT_TRANSACTION = "ADJUSTMENT_TRANSACTION";
    String STOCK_TRANSFER_TRANSACTION = "STOCK_TRANSFER_TRANSACTION";
    String GOODS_RECEIVED_TRANSACTION = "GOODS_RECEIVED_TRANSACTION";
    String GOODS_DELIVERED_TRANSACTION = "GOODS_DELIVERED_TRANSACTION";
    String PAYSLIP_TRANSACTION = "PAYSLIP_TRANSACTION";
    String CUSTOMER_TRANSACTION = "CUSTOMER_TRANSACTION";
    String SUPPLIER_TRANSACTION = "SUPPLIER_TRANSACTION";
    String CUSTOMER_SUPPLIER_PAYMENT_TRANSACTION = "CUSTOMER_SUPPLIER_PAYMENT_TRANSACTION";
    String FIXED_ASSET_TRANSACTION = "FIXED_ASSET_TRANSACTION";
    String DISPOSAL_TRANSACTION = "DISPOSAL_TRANSACTION";
    String DEPRECIATION_TRANSACTION = "DEPRECIATION_TRANSACTION";
    String PAYSLIP_TABLE_TRANSACTION = "PAYSLIP_TABLE_TRANSACTION";
    String SINGLE_PAYRUN_TRANSACTION = "SINGLE_PAYRUN_TRANSACTION";
    String SINGLE_PAYRUN_PAYMENT_TRANSACTION = "SINGLE_PAYRUN_PAYMENT_TRANSACTION";
    String PAYROLL_PAYMENT_TRANSACTION = "PAYROLL_PAYMENT_TRANSACTION";
    String CASH_ADVANCE_TRANSACTION = "CASH_ADVANCE_TRANSACTION";
    String TAXI_TRANSACTION = "TAXI_TRANSACTION";
    String RETAINED_EARNINGS_TRANSACTION = "RETAINED_EARNINGS_TRANSACTION";
    String CASH_ADVANCE_PAYMENT_TRANSACTION = "CASH_ADVANCE_PAYMENT_TRANSACTION";
    String ADDITIONAL_PAYMENT_TRANSACTION = "ADDITIONAL_PAYMENT_TRANSACTION";
    String DEFERRED_TRANSACTION = "DEFERRED_TRANSACTION";
    //MyUpdateConstant
    String BANK_TRANSFER = "BANK_TRANSFER";
    String BATCH_PAYMENT = "BATCH_PAYMENT";

    //CRM
    String EMAIL_ACCOUNT_LIST = "emailAccountList";
    String SMS_SETTINGS_LIST = "smsSettingsList";
    String TELEGRAM_SETTINGS_LIST = "telegramSettingsList";
    String TWILIO_SETTINGS_LIST = "twilioSettingsList";
    String ASTERISK_SETTINGS_LIST = "asteriskSettingsList";
    String SIPUNI_SETTINGS_LIST = "sipuniSettingsList";
    String MY_CALLS_SETTINGS_LIST = "myCallsSettingsList";
    String ASTERISK_EMPLOYEE_LIST = "asteriskEmployeeList";
    String WORKFLOW_RULES_LIST = "workflowRuleList";
    String WORKFLOW_ACTIVITIES_LIST = "workflowActivitiesList";
    String CAMPAIGN_LIST = "campaignList";
    String CRM_ACCOUNT_LIST = "accountList";
    String CRM_ACCOUNT_LIST_2 = "accountList_2";
    String CRM_CONTACT_LIST = "contactList";
    String CRM_CONTACT_LIST_2 = "contactList_2";
    String LEAD_LIST = "leadList";
    String OPPORTUNITY_LIST = "opportunityList";
    String OPPORTUNITY = "opportunity";
    String CRM_WELCOME = "crmWelcome";
    String CASE_LIST = "caseList";
    String SOLUTION_LIST = "solutionList";
    String SLA_LIST = "SLAList";
    String ACTIVITY_LIST = "activityList";
    String EVENT_LIST = "eventList";
    String LOGACALL = "logCall";
    String WEBFORM_ID = "WEBFORM_ID";
    String WEBFORM_LIST = "webFormsList";
    String googleRootUrl = "http://www.google.com/";
    String hooversRootUrl = "http://www.hoovers.com/search/people-search-results/100003766-1.html?type=person&term="; //search by company name function was changed in this site
    String hooversRootCompanyUrl = "http://www.hoovers.com/company-information/company-search.html?term=";
    String hooversRootContactUrl = "http://www.hoovers.com/company-information/cs/people-search.html?term=";
    String commonParamForUrl = "menubar=no,location=no,resizable=yes,scrollbars=yes,status=yes,height=600,width=800";//http://www.linkedin.com/pub/dir/?first=asdf&last=asgasdfg&search=Search
    String linkedinRootUrl = "http://www.linkedin.com/";
    String facebookRootUrl = "http://www.facebook.com/";
    String twitterRootUrl = "http://twitter.com/";
    String googlePlusRootUrl = "http://www.plus.google.com/";
    String CRM_CONTACT = "crm_contact";
    String CRM_ACCOUNT = "crm_account";
    String CRM_LEAD = "crm_lead";
    String WFM_PRODUCTS = "wfm_products";
    String NIMBLE_COMMERCE = "nimble_commerce";
    String CUSTOM_INVOICE_IMPORT = "custom_invoice_import";
    String CHART_OF_ACCOUNTS = "chart_of_accounts";
    String CHART_OF_ACCOUNT = "CHART_OF_ACCOUNT";
    String EMAIL_MARKETING = "emailMarketing";
    String MAIL_LIST = "mailList";
    String ACCOUNTING_VAT_RETURN_REPORT = "ACCOUNTING_VAT_RETURN_REPORT";
    String EXPENSE_IMPORT = "expense";
    String MONTHLY_TIMESHEET_IMPORT = "monthlytimesheet";
    String EMPLOYEE_IMPORT = "employee_import";
    String LEADS = "leads";
    String BUDGET_MANAGER = "budget_manager";
    String Contacts = "contacts";
    String Opportunities = "opportunities";
    String Asterisk = "asterisk";

    String CONTACT_DEPARTMENTS = "CONTACT_DEPARTMENTS";
    String INDUSTRIES = "CONTACT_INDUSTRIES";
    String NUMBER_OF_EMPLOYEES = "CONTACT_NUMBER_OF_EMPLOYEES";
    String ANNUAL_REVENUE = "CONTACT_ANNUAL_REVENUE";
    String CONTACT_ORGANIZATION_TYPES = "CONTACT_ORGANIZATION_TYPES";
    String _TITLE = "_TITLE";

    String VCARD_CONTACTS = "vcard_contact";

    String CONTACT = "globalcontact";
    String GLOBAL_CONTACT_LIST = "glocontactList";

    String NOTE = "note";
    String NOTE_LIST = "note_list";

    /*button constants*/
    String DELETE_PREPAYMENT_BTN = "deleteprepaymentButton";
    String CANCEL_BTN = "cancelButton";
    String CLOSE_BTN = "closeButton";
    String EDIT_BTN = "editButton";
    String VOID_BTN = "voidButton";
    String DELETE_BTN = "deleteButton";
    String PDF_VERSION_BTN = "pdfVersionButton";
    String SAVE_AND_CLOSE_BTN = "saveAndCloseButton";
    String SAVE_BTN = "saveButton";
    String APPROVE_ALL_BTN = "approveAllButton";

    String NEWS = "news";
    String NEWS_LIST = "news_list";
    String NEWS_CATEGORY_LIST = "news_category_list";
    String BENEFITS = "benefits";
    String BENEFIT_REQUESTS = "benefit_requests";
    String EMPLOYEE_DOCUMENTS = "employeeDocuemnts";
    String COMPANY_DOCUMENTS = "companyDocuemnts";
    String FOLDER_NAME = "FOLDER_NAME";
    String MEETING = "meetingMinutes";
    String INCIDENT_LIST = "incidentList";
    String ORG_CHART = "organizationChart";
    String DEPT_ORG_CHART = "departmentOrgChartView";
    String NOTIFICATIONS = "notifications";
    String EMPLOYEE_GOAL = "employeegoal";
    String LEAVE_REQUEST_LIST = "leaveRequestListView";
    String ATTENDANCE_REPORT = "attendanceReport";
    String TERMIANL_REPORT = "terminalAttendance";
    String NEW_FLAME_ORG_CHART = "newFlameOrgChart";
    String ANNUALE_LEAVE_BALANCE = "annualLeaveBalance";
    String SHORT_LIST = "shortListView";
    String AP_ARCHIEVE = "appraisalsArchive";
    String AP_TEMPLATE = "appraisalTemplate";
    /////////////////////////APPRASIALS//////////////////////////////
    String EMPLOYEE_NAME = "EMPLOYEE_NAME";
    String MANAGER_NAME = "MANAGER_NAME";
    String OVERALL_COMMENTS = "OVERALL_COMMENTS";
    String APPRAISIAL_FOR = "APPRAISIAL_FOR";
    String TEMPLATE_NAME = "TEMPLATE_NAME";
    String OVERALL_RATE = "OVERALL_RATE";
    String EMPLOYEE_COMPETENCIES = "EMPLOYEE_COMPETENCIES";
    String ASSIGNED_GOALS = "ASSIGNED_GOALS";
    String ASSESSMENT_DATE = "ASSESSMENT_DATE";
    /////////////////////////MY WORKSPACE//////////////////////////////
    String WORKSPACE_CALENDAR = "calendar|/";
    /////////////////////////CountryCodes//////////////////////////////
    String UK = "GB";
    String AE = "AE"; // ARABIC
    String SA = "SA"; // ARABIC
    String BH = "BH"; // ARABIC
    String KW = "KW"; // ARABIC
    String OM = "OM"; // ARABIC
    String QA = "QA";

    String UZ = "UZ";

    String RUS = "RU";
    String KZ = "KZ";
    String TJ = "TJ";
    String KG = "KG";
    String UA = "UA";
    String BY = "BY";

    List<String> GCC_COUNTRIES = Arrays.asList(AE, SA, BH, KW, OM, QA);
    List<String> CIS_COUNTRIES = Arrays.asList(RUS, KZ, TJ, KG, UA, BY);
    List<String> GCC_REGISTERED = Arrays.asList(AE, SA, BH);
    List<String> VAT_COUNTRIES = Arrays.asList(AE, SA, BH, KW, OM, QA, UK);

    /* Chart contants */
    int PROJECT_CHART = 0;
    int EMPLOYEE_CHART = 1;
    int DEPARTMENT_CHART = 2;
    int PROJECTEMPLOYEE_CHART = 3;
    int DEPARTMENTEMPLOYEE_CHART = 4;
    int OVERALL_CHART = 8;

    /////////////////////////HRMS////////////////////////////////////////
    //Employee Bonus
    String EMPLOYEE_BONUSES = "employeeBonuses";

    ///onboarding
    String EMPLOYEE_STEP = "employeeStep";
    String ONBOARDING_STEP = "onboardingStep";
    String ONBOARDING_PERIOD = "onboardingPeriod";
    String ONBOARDING_CHECK = "onboardingCheck";
    String GOAL = "goal";
    //    String SALARY_GRADE_LIST = "salaryGrades";
    String DEPENDENT_LIST = "dependents";
    //public static final String BASIC_INFORMATION = "basicInformation";
    String EMPLOYMENT_HISTORY_LIST = "employmentHistories";
    String HRMS_MAIN = "hrmsMain";
    String PCMS_JOBS_LIVE = "jobs.app.workforcetrack.com";
    String PCMS_JOBS_TEST = "jobs.aws.workforcetrack.com";
    String PCMS_JOBS = "jobsPcms";
    String TALENT_PROFILE = "talentProfile";

    String COMPANY_GOAL = "company";
    String DEPARTMENT_GOAL = "departmentgoal";
    String PERSONAL_GOAL = "personal";
    String PROJECT_GOAL = "projectgoal";
    String BUSINESS_GOAL = "business";
    String GROUP_GOAL = "GROUP_GOAL";

    String HRMS_EMPLOYEES_LIST = "hrmsEmployeesList";
    String EMPLOYEE_PROFILE_VIEW = "employeeProfileView";
    String HRMS_EXPENSE_REPORT_LIST = "expenseReportList";
    String HRMS_EDIT_PROFILE = "editprofile";
    String MEETING_MINUTES_NOTIFICATION = "MEETING_MINUTES_NOTIFICATION";

    String CATEGORY_PROMOTION = "CATEGORY_PROMOTION";
    String CATEGORY_PENALTY = "CATEGORY_PENALTY";

    String PM_CONTRACT_REMINDER = "PM_CONTRACT_REMINDER";
    String TASK_REMINDER = "TASK_REMINDER";
    String TC_INSTRUCTOR_LIST = "tcInstructorList";
    String TC_INSTRUCTOR_ADD_FORM = "TC_INSTRUCTOR_ADD_FORM";
    //Recruitment
    String VACANCY = "vacancy";
    String CANDIDATE = "candidate";
    String PLACEMENT = "placement";

    String ITEM_LIST = "itemList";


    //Q_QUALIFICATION
    String Q_QUALIFICATION = "Q_QUALIFICATION";                     //Qualification

    //opportunity stage
    //stage
    String ON_TENDER = "ON_TENDER";
    String ESTIMATED = "ESTIMATED";
    String NEEDS_ESTIMATE = "NEEDS_ESTIMATE";

    String POSITION_TITLES = "POSITION_TITLES";

    String DEPARTMENT_TITLES = "DEPARTMENT_TITLES";


    /**
     * ********************************************** - PROJECT_SOURCE - ***********************************************
     */
    String PROJECT_SOURCE_COPY_FROM_PROJECT = "PROJECT_SOURCE_COPY_FROM_PROJECT_"; //project source -> copy from project
    String PROJECT_SOURCE_CONVERT_FROM_OPPORTUNITY = "PROJECT_SOURCE_CONVERT_FROM_OPPORTUNITY_"; //project source -> convert from opportunity
    String PROJECT_SOURCE_COPY_TO_PROJECT_FROM_CASE = "PROJECT_SOURCE_COPY_TO_PROJECT_FROM_CASE_"; //project source -> copy to project from case
    String PROJECT_SOURCE_IMPORT_FROM_QUICK_BOOK = "PROJECT_SOURCE_IMPORT_FROM_QUICK_BOOK_"; //project source -> import project from quick book
    String PROJECT_SOURCE_CONVERT_FROM_SALES_QUOTE = "PROJECT_SOURCE_CONVERT_FROM_SALES_QUOTE_"; //project source -> convert from sales quote
    String PROJECT_SOURCE_CONVERT_FROM_MEETING_MINUTES = "PROJECT_SOURCE_CONVERT_FROM_MEETING_MINUTES_"; //project source -> convert from sales quote
    /**
     * ********************************************** - PROJECT_SOURCE - ***********************************************
     */

    //Settings st(string) email template
    String ST_EMAIL_TEMPLATE = "ST_EMAIL_TEMPLATE";

    //Workspace: meeting minutes
    String MEETING_MINUTES = "MEETING_MINUTES";

    String PM_ISSUE = "PM_ISSUE";

    String LOCALE = "locale";
    String BROWSER_SUPPORT = "browser_support";
    String GOOGLE_CLIENT_ID = "GOOGLE_CLIENT_ID";

    String TIME_TYPES = "TIME_TYPES";
    String REG_TEMP = "REG_TEMP";
    String POS_STATUS = "POS_STATUS";
    String MAX_DEFAULT_WIDTH = "300px";
    String SHORT_WIDTH = "100px";
    String NORMAL_WIDTH = "150px";
    String MIN_DEFAULT_WIDTH = "200px";
    String SHORT_HEIGHT = "100px";

    String DEFAULT_WIDTH = "default-width";

    ///////////////////////// SOLR RELATED VARIABLES/////////////////
//    public static final String SOLR_CORE0 = "core0";
    String SOLR_TASK_CORE = "taskCore";
    String SOLR_PROJECT_CORE = "projectCore";
    String SOLR_PURCHASE_ORDER_CORE = "purchaseOrderCore";
    String SOLR_REQUEST_FOR_QUOTE_CORE = "requestForQuoteCore";
    String SOLR_FOLDER_CORE = "folderCore";
    String SOLR_CASE_CORE = "caseCore";
    String SOLR_CONTACT_CORE = "contactCore";
    String SOLR_CRM_ACCOUNT_CORE = "crmAccountCore";
    String SOLR_SALEINVOICE_CORE = "saleInvoiceCore";
    String SOLR_SALEQUOTE_CORE = "saleQuoteCore";
    String SOLR_SHIPPING_DATA_CORE = "shippingDataCore";
    String SOLR_NEWS_CORE = "newsCore";
    String SOLR_COURSE_SCHEDULE_CORE = "courseScheduleCore";
    String SOLR_OPPORTUNITY_CORE = "opportunityCore";
    String SOLR_EVENT_CORE = "eventCore";
    String SOLR_PRODUCTS_SERVICES_CORE = "productsServicesCore";
    String SOLR_PURCHASE_INVOICE_CORE = "purchaseInvoiceCore";
    String SOLR_EXPENSE_REPORT_CLAIMS_CORE = "expenseReportClaims";
    String SOLR_COURSE_BOOKING_CORE = "courseBookingCore";
    String SOLR_EMPLOYEE_CORE = "employeeCore";
    String SOLR_SINGLE_PAYRUN_CORE = "singlePayrunCore";
    String SOLR_GROUP_PAYRUN_CORE = "groupPayrunCore";
    String SOLR_CASH_ADVANCE_CORE = "cashAdvanceCore";
    String SOLR_ADDITIONAL_PAYMENT_CORE = "additionalPaymentCore";
    String SOLR_VACANCY_CORE = "vacancyCore";
    String SOLR_EMPLOYEE_STEP_CORE = "employeeStepCore";
    String SOLR_CHART_OF_ACCOUNT_CORE = "chartOfAccountCore";
    String SOLR_LEAVE_REQUEST_CORE = "leaveRequestCore";
    String SOLR_CUSTOM_FORM_ITEM_CORE = "customFormItemCore";
    String SOLR_CERTIFICATE_CORE = "certificateCore";
    String SOLR_POSITION_CORE = "positionCore";

    String SOLR_DEPARTMENT_CORE = "departmentCore";
    int SOLR_LIMIT = 100;
    int WAREHOUSE_ID_PART = 1000;
    int PRICELEVEL_ID_PART = 10000;

    ///////////////////////// SOLR CORES ID ////////////////////////
    int ALL_CORES_ID = 0;
    int TASK_CORE_ID = 1;
    int INVOICE_CORE_ID = 2;
    int NEWS_CORE_ID = 3;
    int LEADS_CORE_ID = 4;
    int CONTACTS_CORE_ID = 5;
    int FILES_CORE_ID = 6;
    int FOLDERS_CORE_ID = 7;
    /*public static final int NETWORK_NEWS_CORE_ID = 8;*/
    /* public static final int NETWORK_CORE_ID = 9;*/
    int PROJECT_CORE_ID = 10;
    int CRM_ACCOUNT_CORE_ID = 11;
    int CASE_CORE_ID = 12;
    int QUOTES_CORE_ID = 13;
    int PURCHASE_ORDER_ID = 14;
    int OPPORTUNITY_ID = 15;
    int EVENT_ID = 16;
    int PRODUCT_SERVICE_CORE_ID = 17;
    int PURCHASE_INVOICE_CORE_ID = 18;
    int EXPENSE_REPORT_CLAIMS_CORE_ID = 19;
    int CANDIDATES_CORE_ID = 20;
    int SYSTEM_FOLDER_ID = 21;
    int COURSE_BOOKING_CORE_ID = 22;
    int COURSE_SCHEDULE_CORE_ID = 23;
    int EMPLOYEE_CORE_ID = 24;
    int SINGLE_PAYRUN_CORE_ID = 25;
    int GROUP_PAYRUN_CORE_ID = 26;
    int CASH_ADVANCE_CORE_ID = 27;
    int VACANCY_CORE_ID = 28;
    int EMPLOYEE_STEP_CORE_ID = 29;
    int ADDITIONAL_PAYMENT_CORE_ID = 30;
    int CHART_OF_ACCOUNT_CORE_ID = 31;
    int LEAVE_REQUEST_CORE_ID = 32;
    int CUSTOM_FORM_CORE_ID = 33;
    int SHIPPING_DATA_CORE_ID = 34;
    int REQUEST_FOR_QUOTE_CORE_ID = 35;
    int CERTIFICATE_CORE_ID = 36;
    int POSITION_CORE_ID = 37;
    int DEPARTMENT_CORE_ID = 38;

    ///////////////////////// SIGN UP 2 ////////////////////////////
    String PLUS_SIGN = "plus";
    String PREMIUM_SIGN = "premium";
    String MAX_SIGN = "max";

    String MINI_SIGN = "mini";
    String SMALL_SIGN = "small";
    String STANDARD_SIGN = "standard";
    String SILVER_SIGN = "silver";
    String ENTERPRISE_SIGN = "enterprise";
    String ESSENTIAL_SIGN = "essential";

    ////////////FOR ACTIVIRA////////////
    String MINI_SIGN_ANNUAL = "mini_annual";
    String SMALL_SIGN_ANNUAL = "small_annual";
    String STANDARD_SIGN_ANNUAL = "standard_annual";
    String SILVER_SIGN_ANNUAL = "silver_annual";
    String ENTERPRISE_SIGN_ANNUAL = "enterprise_annual";
    ////////////////////ACTIVIRA Pricing package/////////////////////////
    String PP_MINI_ANNUAL = "PP_MINI_ANNUAL"; //mini
    String PP_SMALL_ANNUAL = "PP_SMALL_ANNUAL"; //small
    String PP_STANDART_ANNUAL = "PP_STANDART_ANNUAL"; //standart
    String PP_SILVER_ANNUAL = "PP_SILVER_ANNUAL"; //silver
    String PP_ENTERPRISE2_ANNUAL = "PP_ENTERPRISE2_ANNUAL"; //enterprice


    ////////////////////Pricing package/////////////////////////
    String PP_SMALL_BUSINESS = "PP_SMALL_BUSINESS"; //plus
    String PP_KPI_PRO = "PP_KPI_PRO";               //premium
    String PP_ENTERPRISE = "PP_ENTERPRISE";         //max
    ////////////////////Pricing package/////////////////////////
    String PP_MINI = "PP_MINI"; //mini
    String PP_SMALL = "PP_SMALL"; //small
    String PP_STANDART = "PP_STANDART"; //standart
    String PP_SILVER = "PP_SILVER"; //silver
    String PP_ENTERPRISE2 = "PP_ENTERPRISE2"; //enterprice
    String PP_ESENTIAL = "PP_ESENTIAL"; //essential
    String PP_BY_YEAR = "PP_BY_YEAR";
    String PP_BY_HALF_YEAR = "PP_BY_HALF_YEAR";
    String PP_BY_QUARTER = "PP_BY_QUARTER";
    String PP_BY_MONTH = "PP_BY_MONTH";

    //new pricing packages
    String PP_BRONZE_ = "PP_BRONZE";
    String PP_SILVER_ = "PP_SILVER";
    String PP_GOLDEN = "PP_GOLDEN";
    String PP_CUSTOM = "PP_CUSTOM";

    ////////////////////Support package/////////////////////////
    String SP_BASIC = "SP_BASIC";         //-- supportPackageID = 0
    String SP_STANDARD = "SP_STANDARD";   //-- supportPackageID = 1
    String SP_BRONZE = "SP_BRONZE";       //-- supportPackageID = 2
    String SP_SILVER = "SP_SILVER";       //-- supportPackageID = 3
    String SP_GOLD = "SP_GOLD";           //-- supportPackageID = 4
    String SP_PLATINUM = "SP_PLATINUM";   //-- supportPackageID = 5
    String SP_DIAMOND = "SP_DIAMOND";     //-- supportPackageID = 6

    String SP_CONTRACTOR = "SP_CONTRACTOR";             //-- supportPackageID = 0
    String SP_SMALL_BUSINESS = "SP_SMALL_BUSINESS";     //-- supportPackageID = 1
    String SP_PROFESSIONAL = "SP_PROFESSIONAL";         //-- supportPackageID = 2
    String SP_ENTERPRISE = "SP_ENTERPRISE";             //-- supportPackageID = 3
    ////////////////////Support package/////////////////////////

    ///////////Moduls/////////////
    String MODULE_PM = ModuleEnum.PM.getCode();
    String MODULE_HRMS = ModuleEnum.HRMS.getCode();
    String MODULE_ACCOUNTING = ModuleEnum.ACCOUNTING.getCode();
    String MODULE_CRM = ModuleEnum.CRM.getCode();
    String MODULE_PAYROLL = ModuleEnum.PAYROLL.getCode();
    String MODULE_DOCUMENTS = ModuleEnum.DOCUMENTS.getCode();
    String MODULE_REPORTING = ModuleEnum.REPORTING.getCode();
    String MODULE_SETTINGS = ModuleEnum.SETTINGS.getCode();
    String MODULE_BACKEND = ModuleEnum.BACKEND.getCode();
    String MODULE_MYACCOUNT = ModuleEnum.MYACCOUNT.getCode();
    String MODULE_MC = ModuleEnum.MC.getCode();
    String MODULE_WORKSPACE = ModuleEnum.MYWORKSPACE.getCode();
    String MODULE_TC = ModuleEnum.TC.getCode();
    ///////////Modules/////////////

    String WHATSAPP = "WHATSAPP";


    //////////////////////// GOOGLE CHECKOUT VAR //////////////////
    String google_checkout_LINK_Live = "https://checkout.google.com/checkout/api/checkout/v2/checkoutForm/Merchant/";
    String google_checkout_LINK_Test = "https://sandbox.google.com/checkout/api/checkout/v2/checkoutForm/Merchant/";

    //////////////////////// GOOGLE CHECKOUT VAR //////////////////
    String ELAVON_LINK_Live = "https://www.myvirtualmerchant.com/VirtualMerchant/process.do";
    String ELAVON_LINK_Test = "https://demo.myvirtualmerchant.com/VirtualMerchantDemo/process.do";
    String ELAVON_XML_LINK_Live = "https://www.myvirtualmerchant.com/VirtualMerchant/processxml.do";
    String ELAVON_XML_LINK_Test = "https://demo.myvirtualmerchant.com/VirtualMerchantDemo/processxml.do";

    /////////////////////// Chat Variables //////////////////////////
//    public static final String CHAT_DOMAIN_LOCALHOST = "62.252.53.33";
    String CHAT_HTTP_BIND_PATH = "http-bind/";

    String FAKE_MODULES = "FAKE_MODULES";

    ///////////////////////// URL //////////////////////////////////
    String PM_URL = "ProjectManagement.html";
    String AVAILABILITY_URL = "Availability.html";
    String CRM_URL = "Crm.html";
    String ACCOUNTING_URL = "Accounting.html";
    String PAYROLL_URL = "Payroll.html";
    String DOCUMENTS_URL = "Documents.html";//GoogleDocuments.html
    String DASHBOARD_URL = "Dashboard.html";
    String MYACCOUNT_URL = "Myaccount.html";
    String HRMS_URL = "Hrms.html";
    String BROKERS_URL = "Brokers.html";
    String WEBSITE_URL = "Website.html";
    String SETTINGS_URL = "Settings.html";
    String REPORTING_URL = "Reporting.html";
    String REPORTING_SYSTEM_URL = "ReportingSystem.html";
    String TC_URL = "TrainingCenter.html";
    String BACKEND_URL = "Backend.html";
    String BACKEND_PAGE = "Backend";
    String LOGISTICS_URL = "Logistics.html";
    String MYWORKSPACE_URL = "Workspace.html";
    String MESSAGECENTER_URL = "MessageCenter.html";
//    public final static String EXLIB_URL = "Exlib.html";

    ///////////////////// EMAIL TEMPLATE CATEGORIES ///////////////////////

    String WIDGET_PREFIX = "prefixP";
    String WIDGET_ALL_DATE = "p-date-title";
    String WIDGET_DATE_YEAR = "dateYearP";
    String WIDGET_DATE_MONTH = "dateMonthP";
    String WIDGET_DATE_DAY = "dateDayP";
    String WIDGET_CLIENT_CODE = "clientCodeP";
    String WIDGET_NUMBERS = "numbersP";
    String WIDGET_SUFFIX = "suffixP";
    String SAV_NUM_DEL = "savNumDel";
    String WIDGET_PROJECT_NUMBER = "projectNumber";
    String WIDGET_RESTART_NUMBER_EACH_PROJECT = "restartNumber";
    String WIDGET_UNIQUE_NUMBER_ALL_PROJECT = "uniqueNumber";

    ///////////////////// EMAIL TEMPLATE CATEGORIES ///////////////////////
    String SALES_INVOICE_CATEGORY = "SALES_INVOICE_CATEGORY";
    String PROJECT_BASE_INVOICE_CATEGORY = "PROJECT_BASE_INVOICE_CATEGORY";
    String RECURRING_INVOICE_CATEGORY = "RECURRING_INVOICE_CATEGORY";
    String SALES_QUOTE_MANAGER_CATEGORY = "SALES_QUOTE_MANAGER_CATEGORY";
    String SALES_QUOTE_CATEGORY = "SALES_QUOTE_CATEGORY";
    String SALES_ORDER_CATEGORY = "SALES_ORDER_CATEGORY";
    String PURCHASE_ORDER_CATEGORY = "PURCHASE_ORDER_CATEGORY";
    String PURCHASE_ORDER_MANAGER_CATEGORY = "PURCHASE_ORDER_MANAGER_CATEGORY";
    String RECEIPT_CATEGORY = "RECEIPT_CATEGORY";
    String EXPENSE_CLAIM_CATEGORY_SUBMIT = "EXPENSE_CLAIM_CATEGORY_SUBMIT";
    String EXPENSE_CLAIM_CATEGORY_RESUBMIT = "EXPENSE_CLAIM_CATEGORY_RESUBMIT";
    String RECEIVE_PAYMENT_CATEGORY = "RECEIVE_PAYMENT_CATEGORY";
    String CASE_REPLIED_CATEGORY = "CASE_REPLIED_CATEGORY";
    String CASE_AUTO_RESPONSE_CATEGORY = "CASE_AUTO_RESPONSE_CATEGORY";
    String OPPORTUNITY_CREATED_CATEGORY = "OPPORTUNITY_CREATED_CATEGORY";
    String OPPORTUNITY_ASSIGNED_CATEGORY = "OPPORTUNITY_ASSIGNED_CATEGORY";
    String GOOGLE_CONTACT_SYNC_CATEGORY = "GOOGLE_CONTACT_SYNC_CATEGORY";
    String CREDIT_NOTE_CATEGORY = "CREDIT_NOTE_CATEGORY";
    String SUPPLIER_BALANCE_CATEGORY = "SUPPLIER_BALANCE_CATEGORY";
    String CUSTOMER_BALANCE_CATEGORY = "CUSTOMER_BALANCE_CATEGORY";
    String HR_REMINDERS_CATEGORY = "_HR_REMINDERS_CATEGORY";
    String REQUEST_FOR_QUOTE_CATEGORY = "REQUEST_FOR_QUOTE_CATEGORY";

    String CALENDAR_EVENT_ADD_CATEGORY = "CALENDAR_EVENT_ADD_CATEGORY";
    String CALENDAR_INVITATION_TO_GUESTS_ADD_CATEGORY = "CALENDAR_INVITATION_TO_GUESTS_ADD_CATEGORY";
    String CALENDAR_INVITATION_TO_GUESTS_EDIT_CATEGORY = "CALENDAR_INVITATION_TO_GUESTS_EDIT_CATEGORY";
    String CALENDAR_INVITATION_TO_GUESTS_DELETE_CATEGORY = "CALENDAR_INVITATION_TO_GUESTS_DELETE_CATEGORY";
    String CALENDAR_EVENT_EDIT_CATEGORY = "CALENDAR_EVENT_EDIT_CATEGORY";
    String CALENDAR_EVENT_DELETE_CATEGORY = "CALENDAR_EVENT_DELETE_CATEGORY";

    String CALENDAR_EVENT_SHARE_CATEGORY = "CALENDAR_EVENT_SHARE_CATEGORY";
    String CALENDAR_EVENT_SHARE_EDIT_CATEGORY = "CALENDAR_EVENT_SHARE_EDIT_CATEGORY";
    String CALENDAR_EVENT_REMINDER_CATEGORY = "CALENDAR_EVENT_REMINDER_CATEGORY";

    String REPORT_REMINDER_CATEGORY = "REPORT_REMINDER_CATEGORY";
    String REPORTER_QRCODE = "REPORTER_QR_CODE";

    String PERSONAL_GOAL_ASSIGN_CATEGORY = "PERSONAL_GOAL_ASSIGN_CATEGORY";
    String DEPARTMENT_GOAL_ASSIGN_CATEGORY = "DEPARTMENT_GOAL_ASSIGN_CATEGORY";
    String PROJECT_GOAL_ASSIGN_CATEGORY = "PROJECT_GOAL_ASSIGN_CATEGORY";
    String BUSINESS_GOAL_ASSIGN_CATEGORY = "BUSINESS_GOAL_ASSIGN_CATEGORY";

    String PRODUCT_STOCK_CATEGORY = "PRODUCT_STOCK_CATEGORY";
    String PRODUCTS_SERVICES_CRM = "PRODUCTS_SERVICES_CRM";

    String TASK_ASSIGN_CATEGORY = "TASK_ASSIGN_CATEGORY";
    String MULTI_TASK_ASSIGN_CATEGORY = "MULTI_TASK_ASSIGN_CATEGORY";
    String ACTUAL_TIME_REACHED_TO_ESTIMATED = "ACTUAL_TIME_REACHED_TO_ESTIMATED";
    String TASK_DELETE_CATEGORY = "TASK_DELETE_CATEGORY";
    String TASK_UPDATE_CATEGORY = "TASK_UPDATE_CATEGORY";
    String TASK_COMPLETED_PREDECESSOR_CATEGORY = "TASK_COMPLETED_PREDECESSOR_CATEGORY";
    String TASK_COMPLETED_CATEGORY = "TASK_COMPLETED_CATEGORY";

    String PROJECT_ADD_CATEGORY = "PROJECT_ADD_CATEGORY";
    String PROJECT_DELETE_CATEGORY = "PROJECT_DELETE_CATEGORY";
    String PROJECT_UPDATE_CATEGORY = "PROJECT_UPDATE_CATEGORY";
    String PROJECT_ASSIGN_CATEGORY = "PROJECT_ASSIGN_CATEGORY";
    String PROJECT_MANAGER_ASSIGN_CATEGORY = "PROJECT_MANAGER_ASSIGN_CATEGORY";
    String BACKUP_MANAGER_ASSIGN_CATEGORY = "BACKUP_MANAGER_ASSIGN_CATEGORY";
    String MESSAGE_CENTER_CATEGORY = "MESSAGE_CENTER_CATEGORY";
    String PROJECT_CLIENT_APPROVE_CATEGORY = "PROJECT_CLIENT_APPROVE_CATEGORY";
    String PROJECT_CLIENT_REJECT_CATEGORY = "PROJECT_CLIENT_REJECT_CATEGORY";
    String PROJECT_CLIENT_SUBMIT_CATEGORY = "PROJECT_CLIENT_SUBMIT_CATEGORY";

    String CLIENT_ACTIVATION_NEW_USER_CATEGORY = "CLIENT_ACTIVATION_NEW_USER_CATEGORY";
    String CLIENT_ACTIVATION_EXISTING_USER_CATEGORY = "CLIENT_ACTIVATION_EXISTING_USER_CATEGORY";
    String EMPLOYEE_ACTIVATION_NEW_USER_CATEGORY = "EMPLOYEE_ACTIVATION_NEW_USER_CATEGORY";
    String EMPLOYEE_ACTIVATION_EXISTING_USER_CATEGORY = "EMPLOYEE_ACTIVATION_EXISTING_USER_CATEGORY";
    String USER_ACCOUNT_CONFIRMATION_CATEGORY = "USER_ACCOUNT_CONFIRMATION_CATEGORY";
    String EMPLOYEE_ACTIVATED_BY_MANAGER_CATEGORY = "EMPLOYEE_ACTIVATED_BY_MANAGER_CATEGORY";

    String REQUEST_FOR_PURCHASE_SEND_TO_MANAGER_CATEGORY = "REQUEST_FOR_PURCHASE_SEND_TO_MANAGER_CATEGORY";
    String REQUEST_FOR_PURCHASE_SEND_TO_EMPLOYEE_FROM_MANAGER_CATEGORY = "REQUEST_FOR_PURCHASE_SEND_TO_EMPLOYEE_FROM_MANAGER_CATEGORY";

    //
    String OVERDUE_INVOICE_REMINDER_FOR_CLIENT_CATEGORY = "OVERDUE_INVOICE_REMINDER_FOR_CLIENT_CATEGORY";
    String NEW_PAYSLIP_CATEGORY = "NEW_EMPLOYEE_PAYSLIP_CATEGORY";
    String PAYSLIP_APPROVED_TO_EMPLOYEE = "PAYSLIP_APPROVED_TO_EMPLOYEE";

    String ISSUE_ADD_CATEGORY = "ISSUE_ADD_CATEGORY";
    String ISSUE_ASSIGN_CATEGORY = "ISSUE_ASSIGN_CATEGORY";
    String ISSUE_DELETE_CATEGORY = "ISSUE_DELETE_CATEGORY";
    String ISSUE_UPDATE_CATEGORY = "ISSUE_UPDATE_CATEGORY";
    //crm web forms category
    String CRM_WEB_FORM_CATEGORY = "CRM_WEB_FORM_CATEGORY";
    String EMPLOYEE_EVENT_CATEGORY = "EMPLOYEE_EVENT_CATEGORY";
    ///SMS TEMPLATE CATEGORY
    String _SMS_TEMPLATE = "_SMS_TEMPLATE";
    String SMS_TEMPLATE_CATEGORY = "SMS_TEMPLATE_CATEGORY";
    String SMS_TEMPLATE_CUSTOMER_BALANSE = "SMS_TEMPLATE_CUSTOMER_BALANSE";
    String SMS_TEMPLATE_SUPPLIER_BALANSE = "SMS_TEMPLATE_SUPPLIER_BALANSE";

    ///CASE NOTIFICATIONS CATEGORIES
    String CASE_CLOSE_NOTIFICATION_CATEGORY = "CASE_CLOSE_NOTIFICATION_CATEGORY";
    String CASE_CLOSE_NOTIFICATION_CATEGORY_FOR_REPORTER = "CASE_CLOSE_NOTIFICATION_CATEGORY_FOR_REPORTER";

    //DOCUMENT UPLOAD TO ... CATEGORY
    String DOC_UPLOAD_TO_TASK_CATEGORY = "DOC_UPLOAD_TO_TASK_CATEGORY";
    String DOC_UPLOAD_TO_PROJECT_CATEGORY = "DOC_UPLOAD_TO_PROJECT_CATEGORY";
    String DOC_UPLOAD_TO_ISSUE_CATEGORY = "DOC_UPLOAD_TO_ISSUE_CATEGORY";

    //Training Center category
    String COURSE_BOOKING_CONFIRMATION_CATEGORY = "COURSE_BOOKING_CONFIRMATION_CATEGORY";
    String STUDENT_COURSE_BOOKING_CONFIRMATION_CATEGORY = "STUDENT_COURSE_BOOKING_CONFIRMATION_CATEGORY";
    String STUDENT_COURSE_BOOKING_CONFIRMATION_CATEGORY_WITHOUT_MAP = "STUDENT_COURSE_BOOKING_CONFIRMATION_CATEGORY_WITHOUT_MAP";

    String COURSE_SCHEDULE_CATEGORY = "COURSE_SCHEDULE_CATEGORY";

    String CRM_MASS_MAILING_CATEGORY = "CRM_MASS_MAILING_CATEGORY";

    String UI_TYPE_TEXTBOX = "TextBox";
    String UI_TYPE_TEXTBOX_EMAIL = "Email";
    String UI_TYPE_DROPDOWN = "DropDown";
    String UI_TYPE_ENTITY_DROPDOWN = "EntityDropDown";
    String TYPE_ENTITY_LOOKUP = "EntityLookUp";
    String TYPE_ENTITY_MULTI_LOOKUP = "EntityMultiLookUp";
    String UI_TYPE_DATEPICKER = "DatePicker";
    String UI_TYPE_DATEPICKER_TIME = "DateTime";
    String UI_TYPE_FILE_UPLOAD_WIDGET = "FileUploadWidget";
    String UI_TYPE_FILE_UPLOAD_ITEM = "FileUploadItem";
    String UI_TYPE_PROFILE_IMAGE_WIDGET = "ProfileImageWidget";
    String UI_TYPE_CHECKBOX = "CheckBox";
    String UI_TYPE_RADIOBUTTON = "RadioButton";
    String UI_TYPE_MULTITABLE = "MULTITABLE";
    String UI_TYPE_PHONENUMBER = "PHONENUMBER";
    String UI_TYPE_LOOKUP = "LOOKUP";
    String UI_TYPE_MULTI_LOOKUP = "MultiLookup";
    String UI_TYPE_ITEM_WITH_DESCRIPTION = "Item";
    String UI_TYPE_HTML_TEXTAREA = "HtmlTextArea";
    String UI_TYPE_PERCENTAGE = "Percentage";
    String UI_TYPE_URL = "Url";
    String UI_TYPE_TEXTAREA = "TextArea";
    String UI_TYPE_ITEM_TABLE = "ITEM_TABLE";
    String UI_TYPE_CURRENCY = "Currency";
    String UI_TYPE_AUTONUMBER = "AutoNumber";
    String UI_TYPE_APPROVAL_PROCESS = "ApprovalProcess";
    String UI_TYPE_COMMITBOX = "CommitBox";
    String SYSTEM = "System";

    String DATA_TYPE_TEXT = "Text";
    String DATA_TYPE_NUMBER = "Number";
    String DATA_TYPE_DATE = "Date";
    String DATA_TYPE_PROFILE_IMAGE = "Profile Image";
    String DATA_TYPE_FILE_UPLOAD = "File Upload";
    String CF_COMING_SOON = "Coming Soon";

    String SECTION_STYLE = "slideDown-box  group expand hideCustomField";
    String FIELDSET_STYLE = "slideDown-content group labelLine";
    String HALFSET_STYLE = "halfSet-1 left";
    String ROW_STYLE = "row hideCustomField";
    String FIELD_STYLE = "field";

    String DATE = "Date";
    String NUMBER = "Number";
    String COMPLATE = "COMPLATE";
    String COMPAN_ADDRESS = "COMPANY_ADDRESS";
    int FIELD_LIMIT = 50;
    int STRING_FIELD_LIMIT = 150;
    int DOULE_FIELD_LIMIT = 100;

    String CFLEAD = "Lead";
    String CFEMPLOYEE = "Employee";
    String CFCONTACT = "Contact";
    String CFOPPORTUNITY = "Opportunity";
    String CFCRMACCOUNT = "CrmAccount";
    String CFCRMCASE = "CrmCase";

    /* Resource Type Name */
    String RESOURCE_EMPLOYEE = "Employee";

    //Bank Account
    int TRANSACTION_DATE = 1;
    int TRANSACTION_DESCRIPTION = 2;
    int TRANSACTION_DEBIT = 3;
    int TRANSACTION_CREDIT = 4;
    int TRANSACTION_BALANCE = 5;
    int TRANSACTION_ACCOUNT_CODE = 6;
    int TRANSACTION_NAME = 7;
    int TRANSACTION_EXCHANGE_RATE = 8;
    int TRANSACTION_AMOUNT = 9;
    int TRANSACTION_REFERENCE = 10;

    String TRANSACTION_DATE_STR = "Transaction Date";
    String TRANSACTION_DESCRIPTION_STR = "Description";
    String TRANSACTION_REFERENCE_STR = "Reference";
    String TRANSACTION_TYPE = "Transaction Type";
    String TRANSACTION_CHECK_NO_STR = "Transaction Check No";
    String FIND_AND_MATCH_STR = "Find and Match";

    String MATCH_FOUND = "Matched";
    String MATCH_TRANSACTION = "Matched transaction";

    String CREATE_TRANSACTION_STR = "Create Transaction";
    String TRANSACTION_DEBIT_STR = "Debit";
    String TRANSACTION_CREDIT_STR = "Credit";
    String TRANSACTION_BALANCE_STR = "Balance";
    String TRANSACTION_ACCOUNT_CODE_STR = "Account Code";
    String TRANSACTION_DATE_FORMAT_STR = "Transaction Date Format";
    String TRANSACTION_NAME_STR = "Name";
    String TRANSACTION_EXCHANGE_RATE_STR = "Exchange Rate";
    String TRANSACTION_AMOUNT_STR = "Amount";

    String NO_MATCH_FOUND = "NO_MATCH_FOUND";
    String FIND_AND_MATCH = "FIND_AND_MATCH";
    String HAS_ENTRIES = "HAS_ENTRIES";

    /*Short Date Format - Begin*/
    String SHORT_DATE_FORMAT_1 = "MM/dd/yyyy";// e.g. 01/31/2010;
    String SHORT_DATE_FORMAT_2 = "dd/MM/yyyy";// e.g. 31/01/2010;
    String SHORT_DATE_FORMAT_3 = "yyyy/MM/dd";// e.g. 2010/01/31;
    String SHORT_DATE_FORMAT_4 = "yyyy/dd/MM";// e.g. 2010/31/01;

    String SHORT_DATE_FORMAT_5 = "MM-dd-yyyy";// e.g. 01-31-2010;
    String SHORT_DATE_FORMAT_6 = "dd-MM-yyyy";// e.g. 31-01-2010;
    String SHORT_DATE_FORMAT_7 = "yyyy-MM-dd";// e.g. 2010-01-31;
    String SHORT_DATE_FORMAT_8 = "yyyy-dd-MM";// e.g. 2010-31-01;

    String SHORT_DATE_FORMAT_9 = "MM.dd.yyyy";// e.g. 01.31.2010;
    String SHORT_DATE_FORMAT_10 = "dd.MM.yyyy";// e.g. 31.01.2010;
    String SHORT_DATE_FORMAT_11 = "yyyy.MM.dd";// e.g 2010.01.31;
    String SHORT_DATE_FORMAT_12 = "yyyy.dd.MM";// e.g. 2010.31.01;

    String SHORT_DATE_FORMAT_13 = "MMM dd, yyyy";// e.g. Jan 31, 2010;
    String SHORT_DATE_FORMAT_14 = "dd MMM, yyyy";// e.g 31 Jan, 2010;
    /*Short Date Format - End*/

    /*Long Date Format - Begin*/
    String LONG_DATE_FORMAT_1 = "MM/dd/yyyy HH:mm";// e.g. 01/31/2010 08:30;
    String LONG_DATE_FORMAT_2 = "dd/MM/yyyy HH:mm";// e.g. 31/01/2010 08:30;
    String LONG_DATE_FORMAT_3 = "yyyy/MM/dd HH:mm";// e.g. 2010/01/31 08:30;
    String LONG_DATE_FORMAT_4 = "yyyy/dd/MM HH:mm";// e.g. 2010/31/01 08:30;

    String LONG_DATE_FORMAT_5 = "MM-dd-yyyy HH:mm";// e.g. 01-31-2010 08:30;
    String LONG_DATE_FORMAT_6 = "dd-MM-yyyy HH:mm";// e.g. 31-01-2010 08:30;
    String LONG_DATE_FORMAT_7 = "yyyy-MM-dd HH:mm";// e.g. 2010-01-31 08:30;
    String LONG_DATE_FORMAT_8 = "yyyy-dd-MM HH:mm";// e.g. 2010-31-01 08:30;

    String LONG_DATE_FORMAT_9 = "MM.dd.yyyy HH:mm";// e.g. 01.31.2010 08:30;
    String LONG_DATE_FORMAT_10 = "dd.MM.yyyy HH:mm";// e.g. 31.01.2010 08:30;
    String LONG_DATE_FORMAT_11 = "yyyy.MM.dd HH:mm";// e.g 2010.01.31 08:30;
    String LONG_DATE_FORMAT_12 = "yyyy.dd.MM HH:mm";// e.g. 2010.31.01 08:30;

    String LONG_DATE_FORMAT_13 = "MMM dd, yyyy [HH:mm]";// e.g. Jan 31, 2010 [08:30];
    String LONG_DATE_FORMAT_14 = "dd MMM, yyyy [HH:mm]";// e.g 31 Jan, 2010 [08:30];

    String LONG_DATE_FORMAT_15 = "MM/dd/yyyy hh:mm a";// e.g. 01/31/2010 08:30;
    String LONG_DATE_FORMAT_16 = "dd/MM/yyyy hh:mm a";// e.g. 31/01/2010 08:30;
    String LONG_DATE_FORMAT_17 = "yyyy/MM/dd hh:mm a";// e.g. 2010/01/31 08:30;
    String LONG_DATE_FORMAT_18 = "yyyy/dd/MM hh:mm a";// e.g. 2010/31/01 08:30;

    String LONG_DATE_FORMAT_19 = "MM-dd-yyyy hh:mm a";// e.g. 01-31-2010 08:30;
    String LONG_DATE_FORMAT_20 = "dd-MM-yyyy hh:mm a";// e.g. 31-01-2010 08:30;
    String LONG_DATE_FORMAT_21 = "yyyy-MM-dd hh:mm a";// e.g. 2010-01-31 08:30;
    String LONG_DATE_FORMAT_22 = "yyyy-dd-MM hh:mm a";// e.g. 2010-31-01 08:30;

    String LONG_DATE_FORMAT_23 = "MM.dd.yyyy hh:mm a";// e.g. 01.31.2010 08:30;
    String LONG_DATE_FORMAT_24 = "dd.MM.yyyy hh:mm a";// e.g. 31.01.2010 08:30;
    String LONG_DATE_FORMAT_25 = "yyyy.MM.dd hh:mm a";// e.g 2010.01.31 08:30;
    String LONG_DATE_FORMAT_26 = "yyyy.dd.MM hh:mm a";// e.g. 2010.31.01 08:30;

    String LONG_DATE_FORMAT_27 = "MMM dd, yyyy [hh:mm a]";// e.g. Jan 31, 2010 [08:30];
    String LONG_DATE_FORMAT_28 = "dd MMM, yyyy [hh:mm a]";// e.g 31 Jan, 2010 [08:30];
    /*Long Date Format - End*/

    String OTHER = "Other";


    String REPORTING = "Reporting";
    String COO_CONNECT = "COOConnect";
    String WFP_LAYOUT = "WFP_LAYOUT";
    String REPORTING_WFP = "REPORTING_WFP";

    /**
     * **************************** NETWORK TYPES **********************************************************
     */
    String PRIVATE_INVITATION_TO_JOIN = "Private (Invitation to Join)";

    ////////////////////////////// Network Updates //////////////////////////////////////////
    String NETWORK = "NETWORK";
    String NETWORK_ADD = "NETWORK_ADD";
    String NETWORK_EDIT = "NETWORK_EDIT";
    String NETWORK_EDIT_CONFIRM = "NETWORK_EDIT_CONFIRM";
    String NETWORK_DELETE = "NETWORK_DELETE";
    String NETWORK_CONTACT_ADD = "NETWORK_CONTACT_ADD";
    String NETWORK_CONTACT_DELETE = "NETWORK_CONTACT_DELETE";
    String NETWORK_BLOG_ADD = "NETWORK_BLOG_ADD";
    String NETWORK_BLOG_EDIT = "NETWORK_BLOG_EDIT";
    String NETWORK_BLOG_DELETE = "NETWORK_BLOG_DELETE";
    String NETWORK_BLOG_COMMENTED = "NETWORK_BLOG_COMMENTED";
    String NETWORK_BLOG_RATED = "NETWORK_BLOG_RATED";
    String PEER_TO_PEER_CONTACT_ADD = "PEER_TO_PEER_CONTACT_ADD";
    String NETWORK_CONTACT_JOIN = "NETWORK_CONTACT_JOIN";

    //////////////////////////// Network Message Constants ////////////////////////////////
    String TO = "TO";
    String CC = "CC";
    String BCC = "BCC";


    int BUILT_IN = 2; // built in object indicates that object has been creaeted by built-in system settings

    //File entity type
    int F_DEFAULT = 0;
    int F_PROJECT_ROOT = 1;
    int F_PROJECT = 2;
    int F_TASK = 3;
    int F_PR_ISSUE = 4;
    int F_PA_ROOT = 5;
    int F_PA = 6;
    int F_360 = 7;
    int F_PA_ISSUE = 8;
    int F_AF_ROOT = 9;//Accounting & Finance root
    int F_SALE_INV = 10;
    int F_PUR_INV = 11;
    int F_EXP = 12; //Expense Line item file upload folder
    int F_AF_ISSUE = 13;
    int F_CRM_ROOT = 14;
    int F_CRM_CONTACT = 15;
    int F_LEAD = 16;
    int F_CASE = 17;
    int F_CLIENT = 18;
    int F_NEWS = 19;

    int F_HRMS_ROOT = 19;
    int F_PERS_GOAL = 20;
    int F_DEP_GOAL = 21;
    int F_PROJ_GOAL = 22;
    int F_BUSS_GOAL = 23;
    int F_COMP_GOAL = 24;

    int F_WORKSPACE_ROOT = 27;
    int F_EVENT = 28; // for Calendar Event

    int F_WEBSITE_ROOT = 29;
    int F_WEBSITE_BLOCK = 30;

    int F_MANUAL_TRANSACTION = 31;

    int F_EMPLOYEE_PROFILE = 32;//in HRMS ROOT
    int F_OPPORTUNITY = 33;//in CRM ROOT
    int F_SALE_QUOTE = 34;//in A&F ROOT   Sales Quote, Sales Invoice, Purchase Order, Purchase Invoice, Expense Claim
    int F_PUR_ORDER = 35;//in A&F ROOT

    int F_EXP_DOC = 36;//in Expense main file upload folder
    int F_SOLUTION = 37;//in CRM ROOT
    int F_CRM_ACCOUNT = 38;//in CRM ROOT
    int F_MEETING_MINUTES = 39;//for Meeting Minutes
    int F_MASS_MAILING = 40;//for Mass Mailing
    int F_COMPANY_PUBLIC_ROOT = 41;//for Company's Public folder
    int F_VACANCY = 42;//for Hrms Recruitment: Vacancy
    int F_CANDIDATE = 43;//for Hrms Recruitment: Candidate
    int F_PLACEMENT = 44;//for Hrms Recruitment: Placement

    int F_LEAVE_REQUEST = 45;//for Hrms, Leave Request
    int F_BACKUPS_ROOT = 46;//for DB Backup files (received from Amazon)
    int F_INCIDENT = 47;//for Hrms, Incident

    int F_SETTINGS_ROOT = 48;//for Settings root
    int F_EMAIL_TEMPLATE = 49;//for Settings, Email template
    int F_PRODUCTS_SERVICES = 50;//for Products/Services
    int F_PAST_EMPLOYMENT = 51;//for Hrms, past employment
    int F_INTERNAL_EMPLOYMENT = 52;//for Hrms, internal employment

    int F_BANK_TRANSFER = 54; //for Cash Payment,Cash Receipt, Bank Payment,Bank Receipt
    int F_BATCH_PAYMENT = 55;// for Invoice Payments,Paid Bills
    int F_CUSTOM_FIELD_ROOT = 56;
    int F_CUSTOM_FIELD_ITEM = 57;
    int F_COMPANY_DOCUMENTS = 58;
    int F_DEPENDENTS = 59;
    int F_STOCK_TRANSFER = 60;
    int F_CONTRACT = 61;
    int F_RFQ = 62;
    int F_STOCK_ADJUSTMENT = 63;
    int F_BANK_ACCOUNT = 64;

    int F_PAYROLL_ROOT = 65;
    int F_CASH_ADVANCE = 66;

    int F_NOTE_ROOT = 67;
    int F_NOTE = 68;
    int F_SALE_QUOTE_ITEM = 69;

    int F_PREPAYMENT = 70;
    int F_RFP = 71;
    int F_RFQ_1 = 72;
    int F_EXP_PAYMENT = 73;
    int F_XML_BACKUPS_ROOT = 74;//for Xml DB Backup files
    int F_TELEGRAM = 75;
    int F_WHITE_LABEL_LOGO = 76;
    int F_WHITE_LABEL_FAVICON = 77;
    int F_ADDITIONAL_PAYMENT = 78;
    int F_WHATSAPP_MEDIA = 79;
    int F_EMPLOYEE_ATTENDANCE = 80;
    int F_AIPHANTOM_PDF = 81;

    //Add Product Cases
    String BY_CATEGORY = "by_category";
    String BY_STOREFRONT = "by_storefront";
    String COPY_FROM_EXISTING = "copyFromExisting";
    String FROM_INVENTORY = "from_inventory";
    String FROM_ASSEMBLY = "from_assembly";
    /*public static final String BY_PRODUCT_KIT = "by_product_kit";*/

    Integer PRODUCT_NEW = 1;
    Integer PRODUCT_USED = 2;
    Integer PRODUCT_REFURBISHED = 3;

    //public static final Integer PRODUCT = 1;
    //public static final Integer SERVICE = 2;
    //public static final Integer PRODUCT_KIT = 3;

    Integer PRODUCT_RENTAL_PERIOD_HOUR = 1;
    Integer PRODUCT_RENTAL_PERIOD_DAY = 2;
    Integer PRODUCT_RENTAL_PERIOD_WEEK = 3;
    Integer PRODUCT_RENTAL_PERIOD_MONTH = 4;


    Integer RESERVATION_STATUS_PENDING = 1;
    Integer RESERVATION_STATUS_RESERVED = 2;
    Integer RESERVATION_STATUS_STARTED = 3;
    Integer RESERVATION_STATUS_CLOSED = 4;
    Integer RESERVATION_STATUS_CANCELED = 5;

    //////////////////////////////////// GOOGLE CALENDAR RELATED ////////////////////////
    String CALENDAR_WEEK = "calendarWeek";
    String CALENDAR_MONTH = "calendarMonth";

    String EDIT_THIS_INSTANCE = "edit_this_instance";
    String EDIT_ALL_SERIES = "edit_all_series";
    String EDIT_ALL_FOLLOWING = "edit_all_following";

    String DELETE_THIS_INSTANCE = "delete_this_instance";
    String DELETE_ALL_SERIES = "delete_all_series";
    String DELETE_ALL_FOLLOWING = "delete_all_following";

    String EVENT_GUEST_STATUS_PENDING = "Pending";

    //////////////////////////////////// CRM Contacts related constants /////////////////
    int CONTACT_PHONES = 1;
    int CONTACT_EMAILS = 2;
    int CONTACT_WEBSITES = 4;
    int CONTACT_IMADDRESSES = 5;
    int CONTACT_RELATIONSHIPS = 6;
    int CONTACT_TELEGRAMS = 7;

    // relations
    int G_HOME = 1;
    int G_WORK = 2;
    int G_MOBILE = 3;
    int G_HOME_FAX = 4;
    int G_WORK_FAX = 5;
    int G_PAGER = 6;
    int G_OTHER = 7;
    int G_EXTENSION = 30;
    int G_HOME_PAGE = 8;
    int G_FTP = 9;
    int G_BLOG = 10;
    int G_PROFILE = 11;
    int G_GOOGLE_TALK = 12;
    int G_AIM = 13;
    int G_YAHOO = 14;
    int G_SKYPE = 15;
    int G_QQ = 16;
    int G_MSN = 17;
    int G_ICQ = 18;
    int G_JABBER = 19;
    int G_SPOUSE = 20;
    int G_CHILD = 21;
    int G_MOTHER = 22;
    int G_FATHER = 23;
    int G_PARENT = 24;
    int G_BROTHER = 25;
    int G_SISTER = 26;
    int G_FRIEND = 27;
    int G_RELATIVE = 28;
    int G_DOMESTIC_PARTNER = 29;
    int G_LINKEDIN = 31;
    int G_FACEBOOK = 32;
    int G_TWITTER = 33;
    int G_INSTAGRAM = 34;
    int G_FAX = 35;
    int G_WHATS_APP = 36;
    int G_TELEGRAM = 37;
    int G_VIBER = 38;
    int TG_USER = 39;

    // references
    String G_HOME_PAGE_STR = "Home Page";
    String G_FTP_STR = "FTP";
    String G_BLOG_STR = "Blog";
    String G_PROFILE_STR = "Profile";
    String G_GOOGLE_TALK_STR = "Google Talk";
    String G_AIM_STR = "AIM";
    String G_YAHOO_STR = "Yahoo";
    String G_SKYPE_STR = "Skype";
    String G_QQ_STR = "QQ";
    String G_MSN_STR = "MSN";
    String G_ICQ_STR = "ICQ";
    String G_JABBER_STR = "Jabber";
    String G_EXTENSION_STR = "Extension";
    String G_LINKEDIN_STR = "LinkedIn";
    String G_FACEBOOK_STR = "Facebook";
    String G_TWITTER_STR = "Twitter";
    String G_INSTAGRAM_STR = "Instagram";
    String G_FAX_STR = "Fax";
    String G_WHATS_APP_STR = "WhatsApp";
    String G_TELEGRAM_STR = "Telegram";
    String G_VIBER_STR = "Viber";
    String EMPLOYEE_REJECTION_REASON = "EMPLOYEE_REJECTION_REASON";


    /*MONTH and YEAR*/
    String MONTH = "Month";

    String TEXT_LEFT = "text-left";
    String TEXT_RIGHT = "text-right";

    String RIGHT_ALIGN_CELL = "right-align-Cell";
    String LEFT_ALIGN_CELL = "left-align-Cell";
    String CENTER_ALIGN_CELL = "center-align-Cell";

    String REGEX_URL = "\\b(https?://|ftp://|file://|www.)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    String REGEX_IDS_COMMA_DELIMITED = "(\\d+(\\,)?)+";
    String REGEX_EMAIL_SERVERSIDEONLY = "(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*:(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)(?:,\\s*(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*))*)?;\\s*)";
    String REGEX_INTEGER_POSITIVE = "[0-9]+";
    String REGEX_INTEGER = "(\\-)?(" + REGEX_INTEGER_POSITIVE + ")";
    String REGEX_REAL_NUMBERS_WITH_DOT = "(" + REGEX_INTEGER + ")*(\\.)?(" + REGEX_INTEGER_POSITIVE + ")";
    String REGEX_REAL_NUMBERS_WITH_COMMA = "(" + REGEX_INTEGER + ")*(,)?(" + REGEX_INTEGER_POSITIVE + ")";
    String REGEX_PHONE = "\\+*\\-*(\\([0-9]+\\))*\\-*\\.* *.*([0-9]*\\-*)*[0-9]+";

    String BTN_DEFAULT = "btn btn--default";
    String BTN_PRIMARY = "btn btn--primary";
    String BTN_SUCCESS = "btn btn--success";
    String BTN_WHITE = "btn btn--white";
    String BTN_DEFAULT_OUTLINE = "btn btn--white btn--outline";
    String BTN_REJECT = "btn btn--danger";


    //Clock Component Constants
    int CRM_TASK = 1;
    int PM_TASK = 2;
    int CRM_CASE = 3;
    int PM_ISSUE_TIMER = 4;

    Integer FILE_SIZE_DEFAULT = 0;
    Integer FILE_SIZE_MEDIUM = 1;
    Integer FILE_SIZE_SMALL = 2;
    Integer FILE_SIZE_ORIGINAL = 4;

    //QuickBook Synchronization Constants
    int QB_ADD_RQ = 1;
    int QB_MODIFY_RQ = 2;
    int QB_CLIENT_SYNCH = 1;
    int QB_EMPLOYEE_SYNCH = 2;
    int QB_TASK_SYNCH = 3;
    int QB_TIMESHEET_SYNCH = 4;
    int QB_SUPPLIER_SYNCH = 5;
    int QB_ITEM_SYNCH = 6;
    int QB_ACCOUNT_SYNCH = 7;
    int QB_PRICE_LEVEL_SYNCH = 8;
    int QB_CHECK_SYNCH = 9;
    int QB_SALEQUOTE_SYNCH = 10;
    int QB_PURCHASE_INVOICE_SYNCH = 11;
    int QB_SALES_INVOICE_SYNCH = 12;

    String QB_FIXED_PERCENTAGE = "FixedPercentage";
    String QB_SYNCH_COMPLETED = "Completed";
    String QB_SYNCH_COMPLETED_MESSAGE = "Data successfully synched";

    String QB_APP_ID = "333019560";//"188910977";

    //Messsage Center Constants
    String DRAFTS = "Drafts";
    String INBOX = "Inbox";
    String OUTBOX = "SENT";
    String LABEL = "Label";

    //Message Center Label Criteria Parameters
    String RECIPIENT = "Recipient";
    String SUBJECT = "Subject";

    String AND = "And";
    String OR = "Or";

    //EML message paramentrs
    String ZIP_WITH_EML_FILE = "ZIP_WITH_EML_FILE";

    /*--------------------START DISCOUNT CONSTANTS-----------------*/
    //DISCOUNT TYPES
    Integer SIMPLE_DISCOUNT = 1;
    Integer MULTI_RANGE_DISCOUNT = 2;

    //MULTI-RAGE TYPES
    Integer QUANTITY = 1;
    Integer TOTAL_PURCHASE_AMOUNT = 2;

    String QUANTITY_STR = "Quantity";
    String TOTAL_PURCHASE_AMOUNT_STR = "Total Purchase Amount";


    //Invoice Default Discounts
    Integer ONE_OFF_DISCOUNT = 0;
    String ONE_OFF_DISCOUNT_STR = "Percentage";
    Integer ONE_OFF_FIXED_AMOUNT = 1;
    String ONE_OFF_FIXED_AMOUNT_STR = "Fixed Amount";
    /*--------------------END DISCOUNT CONSTANTS-----------------*/

    /*--------------- Price Level Constants Start ---------------*/
    Integer FIXED_PERCENTAGE = 1;
    Integer PER_PRODUCT = 2;
    Integer BY_BRAND = 3;

    Integer DECREASE = 1;
    Integer INCREASE = 2;

    /*--------------- Price Level Constants End ---------------*/

    Integer DEFAULT_LIMIT = 20;

    /*-------------------MASS MAILING CONSTANTS------------------*/
    String MASS_MAIL_HEADER_DATABASE_TYPE = "WfmDatabaseType";
    String MASS_MAIL_HEADER_COMPANYID = "WfmCompanyId";
    String MASS_MAIL_HEADER_MESSAGEID = "WfmMessageId";
    String MASS_MAIL_HEADER_ENTITYID = "WfmEntityId";

    //////////////////// WFT More Menu Actions ////////////////////
    //E-commerce
    String MORE_MENU_STOREFRONT = "MORE_MENU_STOREFRONT";
    String MORE_MENU_WEB_SITES = "MORE_MENU_WEB_SITES";
    String MORE_MENU_CATALOG_AND_DIRECTORIES = "MORE_MENU_CATALOG_AND_DIRECTORIES";
    //Custom forms
    String MORE_MENU_WEB_FORMS = "MORE_MENU_WEB_FORMS";
    String MORE_MENU_APPROVAL_FORMS = "MORE_MENU_APPROVAL_FORMS";
    String MORE_MENU_SURVEYS_AND_POLLS = "MORE_MENU_SURVEYS_AND_POLLS";
    //Marketing
    String MORE_MENU_MASS_MAILING = "MORE_MENU_MASS_MAILING";
    String MORE_MENU_LEAD_CAPTURE_FORMS = "MORE_MENU_LEAD_CAPTURE_FORMS";
    String MORE_MENU_GOOGLE_ANALYTICS = "MORE_MENU_GOOGLE_ANALYTICS";
    //Plugin
    String MORE_MENU_OUTLOOK_PLUGIN = "MORE_MENU_OUTLOOK_PLUGIN";
    String MORE_MENU_EXCEL_PLUGIN = "MORE_MENU_EXCEL_PLUGIN";
    //Mobile Apps
    String MORE_MENU_ANDROID = "MORE_MENU_ANDROID";
    String MORE_MENU_IPHONE = "MORE_MENU_IPHONE";

    String MORE_MENU_REPORTING_DASHBOARD = "MORE_MENU_REPORTING_DASHBOARD";

    //StockValuation Transaction type TT => Transaction Type
    Integer TT_STOCK_ADJUSTMENT = 1;
    Integer TT_INVOICE = 2;
    Integer TT_PURCHASE = 3;
    Integer TT_CUSTOMER_CREDIT_NOTE = 4;
    Integer TT_SUPPLIER_CREDIT_NOTE = 5;
    Integer TT_GOODS_RECEIVED = 6;
    Integer TT_OPENING_BALANCE = 7;
    Integer TT_BUILD_ASSEMBLY = 8;
    Integer TT_GOODS_DELIVERED = 9;
    Integer TT_STOCK_TRANSFER = 10;


    String TT_STOCK_ADJUSTMENT_STR = "Stock Adjustment";
    String TT_INVOICE_STR = "Invoice";
    String TT_PURCHASE_STR = "Purchase";
    String TT_CUSTOMER_CREDIT_NOTE_STR = "Customer Credit Note";
    String TT_SUPPLIER_CREDIT_NOTE_STR = "Supplier Credit Note";
    String TT_GOODS_RECEIVED_STR = "Goods Received";
    String TT_OPENING_BALANCE_STR = "Opening Balance";
    String TT_BUILD_ASSEMBLY_STR = "Build Assembly";
    String TT_GOODS_DELIVERED_STR = "Goods Delivered";

    // TC => Transaction Code
    String TC_IN = "IN";
    String TC_OUT = "OUT";

    String CLIENT_STR = "CLIENT";
    String SUPPLIER_STR = "SUPPLIER";

    //Database
    String DATABASE_FREE = "FREE";
    String DATABASE_PAID = "PAID";
    String DATABASE_PAID1 = "PAID1";
    String DATABASE_GLOBAL_AUTH = "GLOBAL_AUTH";
    String DATABASE_AWS_FREE = "AWS_FREE";
    String DATABASE_AWS_PAID = "AWS_PAID";

    //Bug Status, Bug Priority AND Bug Label
    //bug status
    String BUG_STATUS_NEW = "BS_NEW";
    String BUG_STATUS_RESOLVED = "RESOLVED";
    String BUG_STATUS_UNDER_INVESTIGATION = "BS_UNDER_INVESTIGATION";
    String BUG_STATUS_IN_PROGRESS = "BS_IN_PROGRESS";
    String BUG_STATUS_IGNORED = "IGNORED";
    String BUG_STATUS_DONE = "DONE";
    //bug priority
    String BUG_PRIORITY_CRITICAL = "BP_CRITICAL";
    String BUG_PRIORITY_HIGH = "BP_HIGH";
    String BUG_PRIORITY_MEDIUM = "BP_MEDIUM";
    String BUG_PRIORITY_LOW = "BP_LOW";

    //bug label
    String BUG_LABEL_NEW_ISSUE = "NEW_ISSUE";
    String BUG_LABEL_REPEATED_ISSUE = "REPEATED_ISSUE";
    String BUG_LABEL_CUSTOMIZATION = "CUSTOMIZATION";
    String BUG_LABEL_IMPROVEMENT = "IMPROVEMENT";

    String USER = "user";

    //Opportunity Converting To = {Project, Quote}
    Integer CT_PROJECT = 1;

    //Opportunity Items Converted Types CT => Converted Type
    Integer CT_TASK = 1;
    Integer CT_SUB_PROJECT = 2;
    String PREFERRED_LOCALE_KEY = "locale";
    String BY_NAME = "BY_NAME";
    String BY_EMAIL = "BY_EMAIL";
    String BY_BOTH = "BY_BOTH";
    // Uses in entity delete servlet
    String ENTITY_TYPE = "entitytype";
    String PROJECT_STATUS = "projectstatus";
    // This is the max number of contacts we receive from and sent to google
    int CONTACTS_LIMIT = 5000;


    String URL_SMS_CLICKATELL = "http://api.clickatell.com/http/sendmsg?user=<username>&password=<password>&api_id=<apiid>&to=<phone>&text=<message>";
    String URL_SMS_CLICKATELL_FOR_USA = "http://api.clickatell.com/http/sendmsg?mo=1&user=<username>&password=<password>&api_id=<apiid>&from=<senderID>&to=<phone>&text=<message>";
    String URL_SMS_MVAAYOO = "http://api.mVaayoo.com/mvaayooapi/MessageCompose?user=<username>:<password>&senderID=<senderID>&receipientno=<phone>&msgtxt=<message>&state=4";
    String URL_SUNCELLULAR = "http://mcpro1.sun-solutions.ph/mc/send.aspx?user=<username>&pass=<password>&session=<sessionID>&from=<from>&to=<phone>&msg=<message>";
    String URL_EZYTEXTPRO = "http://sms.ezytextpro.com/app/smsapi/index.php?key=<apikey>&campaign=<campaignID>&routeid=<routeID>&type=text&contacts=<phone>&senderid=<senderID>&msg=<message>";
    String URL_OURSMS = "http://www.OurSms.net/api/sendsms.php?username=<username>&password=<password>&message=<message>&numbers=<phone>&sender=<senderID>&unicode=e&Rmduplicated=1&return=string";

    String FACET_FILTER_DEFAULT_PARAM_ALL_TASKS = "All Tasks";
    String FACET_FILTER_DEFAULT_PARAM_CRM_TASKS = "CRM Tasks";
    String MERGE = "merge";


    String IS_ACTIVE_MEETING_MINUTES = "IS_ACTIVE_MEETING_MINUTES";

//    public static final String OTHER_CONTACTS = "Other Contacts"; --google's Other Contacts will not be included in the contact sync and sync setting

    //to take/not to take from annual leave allowance option
    String TO_TAKE_FROM_ALLOWANCE = "TO_TAKE_FROM_ALLOWANCE";
    String NOT_TO_TAKE_FROM_ALLOWANCE = "NOT_TO_TAKE_FROM_ALLOWANCE";

    //convert to task from case
    String CONVERT_TO_TASK_FROM_CASE = "CONVERT_TO_TASK_FROM_CASE";

    //Relationship contansts
    String EMAIL_OWNER = "EMAIL_OWNER";

    //OUTLOOK ACTION CODES
    int OPEN_WRITE_NOTE = 1;
    int OPEN_LOG_CALL = 2;
    int OPEN_ADD_TASK = 3;
    int OPEN_ADD_EVENT = 4;

    //Leave Request statistics constants
    int total = 1;
    int paid = 2;
    int non_paid = 3;
    int HOURS_APPROVED = 0;
    int DAYS_APPROVED = 1;

    int DAYS_FROM_ANNUAL = 7;

    //Users type
    int ACTIVE = 0;
    int NO_ACCESS = 1;
    int ESS = 2;

    String ATTACHMENT_WIDTH = "400px";

    String FORMAT_A4 = "A4";
    String FORMAT_LETTER = "LETTER";

    SelectItem[] XTEMPLATE_TYPES = new SelectItem[]{
            new SelectItem(1, "STOREFRONT"),
            new SelectItem(2, "EVENT"),
            new SelectItem(3, "WEBSITE")
    };

    String PROJECT_BUDGET_ACCOUNT = "PROJECT_BUDGET_ACCOUNT";
    String TOTAL_BUDGET = "TOTAL_BUDGET";
    String TOTAL_ACTUAL = "TOTAL_ACTUAL";
    String VARIANCE_AMOUNT = "VARIANCE_AMOUNT";
    String VARIANCE_PERCENT = "VARIANCE_PERCENT";

    String SESSION_STATUS = "_SESSION_STATUS";
    String LANGUAGES = "_LANGUAGES";
    String LANGUAGE_LEVELS = "_LANGUAGE_LEVELS";
    String BOOKING_ITEM_CATEGORY = "_BOOKING_ITEM_CATEGORY";
    String BOOKING_ROOM_CATEGORY = "ROOM_CATEGORY";

    String COURSE_BOOKING_STATUS = "_COURSE_BOOKING_STATUS";
    String BOOKING_DRAFT = "BOOKING_DRAFT";
    String BOOKING_SUBMITTED_TO_MANAGER = "BOOKING_SUBMITTED_TO_MANAGER";
    String BOOKING_APPROVED = "BOOKING_APPROVED";
    String BOOKING_REJECTED = "BOOKING_REJECTED";
    String BOOKING_CONFIRMED = "BOOKING_CONFIRMED";
    String BOOKING_PAID = "BOOKING_PAID";

    String COURSE_BOOKING_TYPE = "_COURSE_BOOKING_TYPE";
    String BOOKING_BY_APPROVAL = "BOOKING_BY_APPROVAL";
    String BOOKING_PAY_ONLINE = "BOOKING_PAY_ONLINE";
    String BOOKING_PAY_UPON_ARRIVAL = "BOOKING_PAY_UPON_ARRIVAL";

    String STUDENT_COURSE_SCHEDULE_ATTENDED = "_STUDENT_COURSE_SCHEDULE_ATTENDED";

    String STUDENT_ATTENDED_STATUS = "_STUDENT_ATTENDED_STATUS";
    String STUDENT_ATTENDED = "STUDENT_ATTENDED";
    String STUDENT_NO_SHOW = "STUDENT_NO_SHOW";

    String CS_DELIVERED_STATUS = "_CS_DELIVERED_STATUS"; //CS=Course Schedule
    String CS_NOT_STARTED = "CS_NOT_STARTED";
    String CS_DELIVERED = "CS_DELIVERED";
    //Campaign
    String CS_INACTIVE = "CS_INACTIVE";

    int TYPE_SALEQUOTE = 25;
    int TYPE_PRODUCT = 26;
    int TYPE_SALEORDER = 40;

    //Localization edit qila oladigan polyalarni Code i Start
    int CODE = 1;
    int DEFAULT_TEXT = 2;
    int EN = 3;
    int RU = 4;
    int ARABIC = 5;
    int TURKISH = 6;
    int GER = 7;
    int SPA = 8;
    int FR = 9;
    int POR = 10;
    int NEDER = 11;
    int ITA = 12;
    int THAI = 13;

    //Localization edit qila oladigan polyalarni Code i End

    int TABLE = 0;
    int CHART = 1;
    int TABLE_CHART = 2;

    int NONE = 0;
    int ASC = 1;
    int DESC = 2;
    int CLOSE = 1 << 1;

    String ASC_STR = "ASC";
    String DESC_STR = "DESC";

    String IMAGE_SIZE_SMALL = "SMALL";
    String IMAGE_SIZE_MEDIUM = "MEDIUM";
    String IMAGE_SIZE_LARGE = "LARGE";
    String IMAGE_SIZE_ORIGINAL = "ORIGINAL";


    String STRIPE_PAYMENT = "STRIPE_PAYMENT";
    String MASTERCARD_PAYMENT = "MASTERCARD_PAYMENT";
    String PAYPAL_PAYMENT = "PAYPAL_PAYMENT";
    String GOOGLE_CHECKOUT_PAYMENT = "GOOGLE_CHECKOUT_PAYMENT";
    String ELAVON_PAYMENT = "ELAVON_PAYMENT";
    String WORKFLOW = "WORKFLOW";
    String ADD = "ADD";
    String SUBTRACT = "SUBTRACT";
    String CURRENT = "CURRENT";
    String TOTAL_AMOUNT = "TOTAL_AMOUNT";
    String CLIENT_BALANCE = "CLIENT_BALANCE";

    String[] moduls = {"PM", "HRMS", "CRM", "CASE", "ACCOUNTING", "OTHERS"};
    String KEY_WORDS = ",pm,project,task,workstream,timesheet,employee,department,location,crm,lead,opportunity,contact,campaign,account,crmaccount,event,activity,case,solution,forms,calendar,message,message center,email,mail,hrms,profile,goal,appraisal,competency,skill,attendance,leave,request,recruitment,candidate,vacancy,";
    String ENABLE_WORLDPAY = "enable_worldpay";
    String CUSTOM_VIEW = "CUSTOM_VIEW_";
    String ONBOARDING_STEP_FORM = "STEP_FORM_";
    String ITEM_TABLE = "ITEM_TABLE_";

    String LISTING = "LISTING";
    String PAGE = "PAGE";
    String PRINT_BARCODE = "PRINT_BARCODE";
    String BARCODEE = "BARCODEE";

    //Custom PDF constants
    String HEADER_LEFT = "HEADER_LEFT";
    String HEADER_RIGHT = "HEADER_RIGHT";
    String HEADER_CENTER = "HEADER_CENTER";
    String FOOTER_LEFT = "FOOTER_LEFT";
    String FOOTER_CENTER = "FOOTER_CENTER";
    String FOOTER_RIGHT = "FOOTER_RIGHT";
    String DEFAULT_ = "DEFAULT_";

    interface HRMS {
        interface RECRUITMENT {
            String CANDIDATE = "CANDIDATE";
            String VACANCY = "VACANCY";
        }
    }

    //Project Budget constants
    Integer PROJECT_ACTUAL_COST = 0;
    Integer PROJECT_ACTUAL_CLIENT_CHARGE = 1;
    Integer PROJECT_ACTUAL_TIME_SPENT = 2;
    Integer PROJECT_ESTIMATED_COST = 3;
    Integer PROJECT_ESTIMATED_CLIENT_CHARGE = 4;
    Integer PROJECT_ESTIMATED_TIME_SPENT = 5;
    Integer PROJECT_HOURS_SPENT = 6;

    //Task Budget constants
    Integer TASK_ACTUAL_TIME_SPENT = 2;
    Integer TASK_ESTIMATED_TIME_SPENT = 5;
    Integer TASK_HOUR_SPENT = 6;

    String HELP_DOCUMENT = "helpDocument";

    String STARTDATE_NC = "startDate_nc";
    String ENDDATE_NC = "endDate_nc";

    String BUTTON_LOCATION_TOP_BAR = "BUTTON_LOCATION_TOP_BAR";

    //Csv Template Types
    String CSV_TEMPLATE_IMPORT_BANK_STATEMENT = "IMPORT_BANK_STATEMENT";

    // Reminder Types
    Integer PROJECT_REMINDER = 1;
    Integer WORKSTREAM_REMINDER = 2;
    Integer CONTRACT_REMINDER = 3;
    //Trash Bin
    String TRASH_BIN_PENDING = "PENDING";
    String TRASH_BIN_RESTORED = "RESTORED";
    String TRASH_BIN_REMOVED = "REMOVED";

    String REQUEST_TYPE = "_REQUEST_TYPE";
    String LEAVE_REQUEST = "LEAVE_REQUEST";
    String BENEFIT_REQUEST = "BENEFIT_REQUEST";
    String OTHER_REQUEST = "OTHER_REQUEST";
    String CASH_ADVANCED = "CASH_ADVANCED";
    String EXPENSES_CLAIM = "EXPENSES_CLAIM";
    String LEAVE_PLANNER = "LEAVE_PLANNER";

    interface WORKFLOW_START_TIME {
        String TRIGGER_TIME = "TRIGGER_TIME";
        String ENTITY_CREATION_TIME = "ENTITY_CREATION_TIME";
        String ENTITY_MODIFICATION_TIME = "ENTITY_MODIFICATION_TIME";
        String BY_ATTRIBUTES = "BY_ATTRIBUTES";
    }

    interface TIME_GRANULARITY {
        String MINUTES = "MINUTES";
        String HOURS = "HOURS";
        String DAYS = "DAYS";
        String WEEKS = "WEEKS";
        String MONTHS = "MONTHS";
    }

    interface RENT_ITEMS {
        String OCCUPIED = "OCCUPIED";
        String AVAILABLE = "AVAILABLE";
        String CLEANING = "CLEANING";
        String REPAIRING = "REPAIRING";
    }
    interface PAYMENT_TYPES {
        String PAYME = "PAYME";
        String PAYME_BANK = "PAYME_BANK";
        String PAYME_EXPENSE = "PAYME_EXPENSE";
        String CLICK = "CLICK";
        String REVOLUT_BANK = "REVOLUT_BANK";
        String REVOLUT_EXPENSE = "REVOLUT_EXPENSE";
    }

    String MY_FAVOURITE_REPORTS = "My Favourite Reports";
    String MY_CUSTOM_REPORTS = "My Custom Reports";
    String OTHER_REPORTS = "Other Reports";

    String REPORTING_CATEGORY = "category";
    String REPORTING_HOME = "home";
    int MASSMAIL_LIMIT = 1000;
    String ACCOUTING_REPORT = "Reporting.html#category/home/-7/Accounting & Finance";
    String SQLSERVER = "sqlserver";
    String FINANCIALSETTINGS_NOT_FOUND = "FINANCIALSETTINGS_NOT_FOUND";

    String LEAVE_REQUEST_TYPE = "_LEAVE_REQUEST_TYPE";
    String LR_TYPE_UNAUTHORIZED_LEAVE = "LR_TYPE_UNAUTHORIZED_LEAVE";
    String LR_TYPE_ANNUAL_LEAVE = "LR_TYPE_ANNUAL_LEAVE";
    String LR_STATUS_SS_APPROVED = "SS_APPROVED";
    String LR_STATUS_SS_DENIED = "DENIED";
    String LR_STATUS_NOT_DEFINED = "NOT_DEFINED";

    String BR_WAITING_FOR_APPROVAL = "BR_WAITING_FOR_APPROVAL";
    String BR_APPROVED = "BR_APPROVED";
    String BR_REJECTED = "BR_REJECTED";
    String PRODUCT_ADD = "PRODUCT_ADD";
    String PRODUCT_EDIT = "PRODUCT_EDIT";
    String PRODUCT_TYPE = "PRODUCT_TYPE";

    String FROM_PAYROLL = "FROM_PAYROLL";
    String FROM_HRMS = "FROM_HRMS";
    String FROM_PRICING = "FROM_PRICING";
    String FROM_PM = "FROM_PM_SECTION";
    String FROM_SETTINGS_PROFILE = "FROM_SETTINGS_PROFILE";
    String FROM_TC_INSTRUCTOR = "FROM_TC_INSTRUCTOR";
    String FROM_SINGLE_EMPLOYEE_ADD = "FROM_SINGLE_EMPLOYEE_ADD";

    String POS_STATUS_FROZEN = "POS_STATUS_FROZEN";
    String POS_STATUS_ACTIVE = "POS_STATUS_ACTIVE";
    String POS_STATUS_OPEN = "POS_STATUS_OPEN";
    String EMPLOYEE_FORM_PERSONAL_ID = "EMPLOYEE_FORM_PERSONAL_ID";
    String FACEBOOK_APP_ID = "FACEBOOK_APP_ID";

    String PRODUCT_NUMBERING_SETTINGS_FORM = "PRODUCT_NUMBERING_SETTINGS_FORM";
    String PAYROLL_NUMBERING_SETTINGS_FORM = "PAYROLL_NUMBERING_SETTINGS_FORM";

    //Gender type
    String MALE = "Male";
    String FEMALE = "Female";
    String IRRELEVANT_GENDER = "IrrelevantGender";
    String VACANT = "Vacant";
    String YES = "Yes";
    String No = "No";

    interface DASHBOARD_WIDGET_CODE {
        String ATTENDENCE = "ATTENDENCE";
        String EMPLOYEE_DOCUMENT = "EMPLOYEE_DOCUMENT";
        String COMPANY_DOCUMENT = "COMPANY_DOCUMENT";
        String HOLIDAY = "HOLIDAY";
        String TIMESLOT = "TIMESLOT";
        String EMPLOYEE_PROFILE = "EMPLOYEE_PROFILE";
        String NEWS = "NEWS";
        String TODO_LIST = "TODO_LIST";
        String WAITING_FOR_APPROVAL = "WAITING_FOR_APPROVAL";
        String UNAVAILABLE_EMPLOYEES = "UNAVAILABLE_EMPLOYEES";
        String UNAVAILABLE_EMPLOYEES_SUPERVISION = "UNAVAILABLE_EMPLOYEES_SUPERVISION";
        String IN_OUT = "IN_OUT";
        String DOCUMENT_EXPIRY = "DOCUMENT_EXPIRY";
        String INCIDENT = "INCIDENT";
        String PAYROLL = "PAYROLL";
        String CANDIDATE_PER_VACANCY = "CANDIDATE_PER_VACANCY";
        String HEADCOUNT = "HEADCOUNT";
        String ONBOARDING = "ONBOARDING";
        //Accounting Dashboard Widgets
        String PURCHASE_TRANSACTION = "PURCHASE_TRANSACTION";
        String TOP_EXPENSES = "TOP_EXPENSES";
        String EMPLOYEE_TOP_EXPENSES = "EMPLOYEE_TOP_EXPENSES";
        String SALE_PURCHASE_TRANSACTIONS = "SALE_PURCHASE_TRANSACTIONS";
        String AGING_RECEIVABLE = "AGING_RECEIVABLE";
        String AGED_REPORTS = "AGED_REPORTS";
        String INCOME_VS_EXPENSE = "INCOME_VS_EXPENSE";
        //HR custom widgets
        String EXPENSES_BY_CATEGORIES = "EXPENSES_BY_CATEGORIES";
        String OLD_NEW_EXPENSES = "OLD_NEW_EXPENSES";
        String LEAVES = "LEAVES";
        String SALARY = "SALARY";
        String INCENTIVES = "INCENTIVES";
        String EXPIRED_DOCUMENTS = "EXPIRED_DOCUMENTS";
        String EMPLOYEE_BY_STATUS = "EMPLOYEE_BY_STATUS";
        String NEW_EMPLOYEE_JOINING = "NEW_EMPLOYEE_JOINING";
        String SALARY_RATIO = "SALARY_RATIO";
        String MY_UPDATES = "MY_UPDATES";
        String MY_CONTACTS = "MY_CONTACTS";
        String PROJET_OVERVIEW = "PROJET_OVERVIEW";
        String PROJECT_TIME = "PROJECT_TIME";
        String PROJECT_BUDGET = "PROJECT_BUDGET";
        String PROJECT_DUE_THIS_MONTH = "PROJECT_DUE_THIS_MONTH";
        String TASKS_DUE_TODAY = "TASKS_DUE_TODAY";
        String GETTING_STARTED = "GETTING_STARTED";
        String MY_CALENDAR = "MY_CALENDAR";
        String GENDER_RATIO = "GENDER_RATIO";
        String EXPIRY_DOCUMENTS = "EXPIRY_DOCUMENTS";
        String PAYROLL_YTD = "PAYROLL_YTD";
        String PAYROLL_EMPLOYEE_YTD = "PAYROLL_EMPLOYEE_YTD";
        String COMBO = "COMBO";
        String LEAVE_REASON_STATUS = "LEAVE_REASON_STATUS";
        String HRMS_MY_FILES = "HRMS_MY_FILES";
        String HRMS_EMPLOYEE_CALENDAR = "HRMS_EMPLOYEE_CALENDAR";
        String MY_FAVOURITE_REPORTS = "MY_FAVOURITE_REPORTS";
    }

    interface DASHBOARD_GETTING_STARTED {
        String COMPANY_SETUP = "COMPANY_SETUP";
        String INVITE_USER = "INVITE_USER";
        String USER_PROFILE = "USER_PROFILE";
        String DATA_MIGRATION = "DATA_MIGRATION";
        String CONFIGURE_EMAIL = "CONFIGURE_EMAIL";
    }

    interface DASHBOARD_VIEW {
        String EMPLOYEE_SELF_SERVICE = "EMPLOYEE_SELF_SERVICE";
        String MANAGER_SELF_SERVICE = "MANAGER_SELF_SERVICE";
        String ACCOUNTING_DASHBOARD = "ACCOUNTING_DASHBOARD";
    }

    interface ESSPdfParams {
        String MANAGER_SELF_SERVICE_VIEW_TYPE = "managerSelfServiceViewType";
        String CURRENT_EMPLOYEE_ID = "currentEmployeeId";
        String MANAGER_DATA_TYPE = "managerDataType";
        String HOLIDAY_YEAR = "holiday_year";
        String WFA_CATEGORY_ID = "wfaCategoryId";
        String UE_REASON_ID = "ueReasonId";
        String UE_REASON_NAME = "ueReasonName";
        String IN_OUT_DEPARTMENT_ID = "inOutDepartmentId";
        String IN_OUT_DEPARTMENT_NAME = "inOutDepartmentName";
        String INCIDENT_MONTH = "incidentMonth";
        String INCIDENT_MONTH_NAME = "incidentMonthName";
        String PAYROLL_DEDUCTION = "payrollDeduction";
        String PAYROLL_YEAR = "payrolYear";
        String PAYROLL_YEAR_NAME = "payrolYearName";
        String PAYROLL_MONTH = "payrollMonth";
        String PAYROLL_MONTH_NAME = "payrollMonthName";
        String HEAD_COUNT_CATEGORY_ID = "headCountCategoryID";
        String HEAD_COUNT_CATEGORY_NAME = "headCountCategoryNAME";
        String ONBOARDING_CATEGORY_ID = "onboardingCategoryID";
        String ONBOARDING_CATEGORY_NAME = "onboardingCategoryName";
        String EBC_MONTH = "ebcMonth";
        String EBC_MONTH_NAME = "ebcMonthName";
        String EBC_YEAR = "ebcYear";
        String EBC_YEAR_NAME = "ebcYearName";
        String EBC_DEPARTMENT_ID = "ebcDepartmentId";
        String EBC_DEPARTMENT_NAME = "ebcDepartmentNAME";
        String EBC_LOCATION_ID = "ebcLocationtId";
        String EBC_LOCATION_NAME = "ebcLocationtName";
        String EBC_EMPLOYEE_ID = "ebcEmployeetId";
        String EBC_EMPLOYEE_NAME = "ebcEmployeetName";
        String ONE_MONTH = "oneMonth";
        String ONE_MONTH_NAME = "oneMonthName";
        String ONE_YEAR = "oneYear";
        String ONE_YEAR_NAME = "oneYearName";
        String ONE_DEPARTMENT_ID = "oneDepartmentId";
        String ONE_DEPARTMENT_NAME = "oneDepartmentName";
        String ONE_LOCATION_ID = "oneLocationtId";
        String ONE_LOCATION_NAME = "oneLocationtName";
        String LEAVES_MONTH = "leavesMonth";
        String LEAVES_MONTH_NAME = "leavesMonthName";
        String LEAVES_YEAR = "leavesYear";
        String LEAVES_YEAR_NAME = "leavesYearName";
        String LEAVES_DEPARTMENT_ID = "leavesDepartmentId";
        String LEAVES_DEPARTMENT_NAME = "leavesDepartmentName";
        String LEAVES_EMPLOYEE_ID = "leavesEmployeeId";
        String LEAVES_EMPLOYEE_NAME = "leavesEmployeeName";
        String LEAVES_LOCATION_ID = "leavesLocationId";
        String LEAVES_LOCATION_NAME = "leavesLocationName";
        String INCENTIVES_MONTH = "incentivesMonth";
        String INCENTIVES_MONTH_NAME = "incentivesMonthName";
        String INCENTIVES_YEAR = "incentivesYear";
        String INCENTIVES_YEAR_NAME = "incentivesYearName";
        String INCENTIVES_LOCATION_ID = "incentivesLocationId";
        String INCENTIVES_LOCATION_NAME = "incentivesLocationName";
        String SALARY_TYPE = "salaryType";
        String SALARY_MONTH = "salaryMonth";
        String SALARY_MONTH_NAME = "salaryMonthName";
        String SALARY_YEAR = "salaryYear";
        String SALARY_YEAR_NAME = "salaryYearName";
        String SALARY_LOCATION_ID = "salaryLocationId";
        String SALARY_LOCATION_NAME = "salaryLocationName";
        String SALARY_DEPARTMENT_ID = "salaryDepartmentId";
        String SALARY_DEPARTMENT_NAME = "salaryDepartmentName";
        String EMPLOYEEBS_LOCATION_ID = "employeeBSLocationtId";
        String EMPLOYEEBS_LOCATION_NAME = "employeeBSLocationtName";
        String EMPLOYEEBS_DEPARTMENT_ID = "employeeBSDepartmentId";
        String EMPLOYEEBS_DEPARTMENT_NAME = "employeeBSDepartmentName";
        String DE_FOLDER_TYPE = "deFolderType";
        String DE_END_DATE = "deEndDate";
        String DE_LOCATION_ID = "deLocationId";
        String DE_LOCATION_NAME = "deLocationName";
        String DE_MONTH_NAME = "deMonthName";
        String DE_YEAR_NAME = "deYearName";
        String DE_DOC_TYPE_NAME = "deDocTypeName";
        String DE_DEPARTMENT_ID = "deDepartmentId";
        String DE_DEPARTMENT_NAME = "deDepartmentName";
        String DE_DATA_TYPE = "deDataType";
        String SR_MONTH = "srMonth";
        String SR_MONTH_NAME = "srMonthName";
        String SR_YEAR = "srYear";
        String SR_YEAR_NAME = "srYearName";
        String SR_LOCATION_ID = "srLocationId";
        String SR_LOCATION_NAME = "srLocationName";
        String SR_DEPARTMENT_ID = "srDepartmentId";
        String SR_DEPARTMENT_NAME = "srDepartmentName";
        String SR_EMPLOYEE_ID = "srEmployeeId";
        String SR_EMPLOYEE_NAME = "srEmployeeName";
        String NEJ_MODULE = "nejModule";
        String NEJ_SELECTED_MONTH = "nejSelectedMonth";
        String NEJ_SELECTED_MONTH_NAME = "nejSelectedMonthName";
        String NEJ_SELECTED_YEAR = "nejSelectedYear";
        String NEJ_SELECTED_YEAR_NAME = "nejSelectedYearName";
        String NEJ_SELECTED_DAY = "nejSelectedDay";
        String NEJ_LOCATION_ID = "nejLocationId";
        String NEJ_LOCATION_NAME = "nejLocationName";
        String NEJ_DEPARTMENT_ID = "nejDepartmentId";
        String NEJ_DEPARTMENT_NAME = "nejDepartmentName";
        String NEJ_DATA_TYPE = "nejDataType";
    }

    String MANAGE_ALL = "MANAGE_ALL";//Manage widgets for all users
    String MANAGE_MYSELF = "MANAGE_MYSELF"; //Manage widgets for myself


    //Request for Quote
    Integer COMPANY_SUPPLIERS = 0;

    // Color Strings
    String DEFAULT_FONT_COLOR = "548CE7"; //Prepend # to use as css color;

    String STOCK_VALUATION_REPORT = "STOCK_VALUATION_REPORT";
    String USED_IN_INVOICE = "USED_IN_INVOICE";

    String HRMS_DOCUMENTS = "HRMS_DOCUMENTS";
    String CATEGORY_DEDUCTION = "Deduction";

    String _DOCUMENT_TYPES = "_DOCUMENT_TYPES";
    String INSURANCE = "DT_INSURANCE";

    String _COMPANY_DOCUMENT_TYPES = "_COMPANY_DOCUMENT_TYPES";

    String CERTIFICATE_OF_EMPLOYMENT_STATUS = "CERTIFICATE_OF_EMPLOYMENT_STATUS";
    String CERTIFICATE_OF_EMPLOYMENT_STATUS_APPROVED = "CERTIFICATE_OF_EMPLOYMENT_STATUS_APPROVED";
    String CERTIFICATE_OF_EMPLOYMENT_STATUS_REJECTED = "CERTIFICATE_OF_EMPLOYMENT_STATUS_REJECTED";
    String CERTIFICATE_OF_EMPLOYMENT_STATUS_SUBMITTED = "CERTIFICATE_OF_EMPLOYMENT_STATUS_SUBMITTED";
    String CERTIFICATE_OF_EMPLOYMENT_STATUS_DRAFT = "CERTIFICATE_OF_EMPLOYMENT_STATUS_DRAFT";
    String INV_ITEM = "INV_ITEM";
    String QUOTE_ITEM = "QUOTE_ITEM";
    String OPPORTUNITY_ITEM = "OPPORTUNITY_ITEM";
    Integer EXTREME_EXPRESS = 62442;

    String BILL_OF_MATERIALS_SUBMITTED = "BILL_OF_MATERIALS_SUBMITTED";
    String BILL_OF_MATERIALS_APPROVED = "BILL_OF_MATERIALS_APPROVED";
    String BILL_OF_MATERIALS_REJECTED = "BILL_OF_MATERIALS_REJECTED";

    String CUSTOM_FORM_ITEM_STATUS = "CUSTOM_FORM_ITEM_STATUS";
    String CUSTOM_FORM_ITEM_STATUS_APPROVED = "CUSTOM_FORM_ITEM_STATUS_APPROVED";
    String CUSTOM_FORM_ITEM_STATUS_REJECTED = "CUSTOM_FORM_ITEM_STATUS_REJECTED";
    String CUSTOM_FORM_ITEM_STATUS_SUBMITTED = "CUSTOM_FORM_ITEM_STATUS_SUBMITTED";
    String CUSTOM_FORM_ITEM_STATUS_DRAFT = "CUSTOM_FORM_ITEM_STATUS_DRAFT";

    String ENGLISH = "ENGLISH";
    String RUSSIAN = "RUSSIAN";
    String ARABICBOX = "ARABIC";
    String UZBEK = "UZBEK";

    String CUSTOM_FORM = "CUSTOM_FORM";

    String CUTOM_FORM_SECTION_SPLIT = "<-->";

    int BATCH_LIMIT = 1000;

    String NUMBER_EXIST = "NUMBER_EXIST";
    String ACTIVE_STRING = "ACTIVE";
    String TARIFF_GRID = "TARIFF_GRID";
    String FIXED_AMOUNT = "FIXED_AMOUNT";
    String BY_ATTENDANCE_REPORT = "ATTENDANCE_REPORT";

    interface BarcodeType {
        String CODABAR = "CODABAR";
        String CODE39 = "CODE39";
        String CODE128 = "CODE128";
        String DATAMATRIX = "DATAMATRIX";
        String EAN8 = "EAN8";
        String EAN13 = "EAN13";
        String EAN128 = "EAN128";
        String INTELLIGENTMAIL = "INTELLIGENTMAIL";
        String INTERLEAVED2OF5 = "INTERLEAVED2OF5";
        String ITF14 = "ITF14";
        String PDF417 = "PDF417";
        String POSTNET = "POSTNET";
        String ROYALMAIL = "ROYALMAIL";
        String UPCA = "UPCA";
        String UPCE = "UPCE";
    }

    interface WorkflowActionConstants {
        int CASH_ADVANCE = 1;
        int ADDITIONAL_PAYMENT = 2;
        int ADDITIONAL_DEDUCTION = 3;
        int CERTIFICATE = 4;

        interface Type {
            int TEXT = 0;
            int NUMERIC = 1;
            int DATE = 2;
            int CASH_ADVANCE_CATEGORY = 3;
            int EMP_LOOKUP = 4;
            int APPROVER_LOOKUP = 5;
            int PAYROLL_CATEGORY_PAYMENT = 6;
            int PAYROLL_CATEGORY_DEDUCTION = 7;
            int ACCOUNT_LOOKUP = 8;
            int CERTIFICATE_LIST_BOX = 9;
            int TEXT_2 = 10;
        }

        interface CashAdvance {
            int FIELD_DATE = 2;
            int FIELD_STATUS = 3;
            int FIELD_CATEGORY = 4;
            int FIELD_REQUESTED_AMOUNT = 5;
            int FIELD_PAYMENT_TERMS = 6;
            int FIELD_PAYMENT_AMOUNT = 7;
            int FIELD_PAYMENT_METHOD = 8;
            int FIELD_APPROVER = 9;
            int FIELD_PURPOSE = 10;
            int FIELD_FROM_ACCOUNT = 11;
            int FIELD_ACCOUNT = 12;
        }

        interface AdditionalPayment {
            int FIELD_REFERENCE = 1;
            int FIELD_CATEGORY = 3;
            int FIELD_MONTH = 4;
            int FIELD_YEAR = 5;
            int FIELD_APPROVER = 6;
            int FIELD_STATUS = 7;
            int FIELD_PAYMENT_AMOUNT = 8;
        }

        interface Certificate {
            int FIELD_REFERENCE = 1;
            int FIELD_TYPE = 3;
        }
    }

    interface LISTING_ACTION {
        String COLUMN_CODE = "action";
        int COLUMN_WIDTH = 80;
        int INPUT_COLUMN_WIDTH = 47;
    }

    //    Documents Section Constants
    String DOCUMENTS_FOLDER_ALL = "all";
    String DOCUMENTS_FOLDER_SYSTEM = "system";
    String DOCUMENTS_FOLDER_MYFOLDERS = "myfolders";
    String DOCUMENTS_FOLDER_PUBLIC = "public";
    String DOCUMENTS_FOLDER_SHARED = "shared";
    String DOCUMENTS_FOLDER_OTHERS = "others";
    String DOCUMENTS_FOLDER_TRASH = "trash";
    String DOCUMENTS_CUSTOM_MENU = "custom_menu";

    String SUPERVISOR_STRUCTURE = "SUPERVISOR_STRUCTURE";
    Integer FROM_SUNDAY = 1;
    Integer FROM_MONDAY = 2;
    Integer FROM_SATURDAY = 7;

    interface PRICING_ORDER {
        String SUBSCRIPTION_ADD = "SUBSCRIPTION_ADD";
        String SUBSCRIPTION_UPGRADE = "SUBSCRIPTION_UPGRADE";
    }

    interface DASHBOARD_NAMES {
        String DASHBOARD = "Dashboard";
        String HR_DASHBOARD = "HR Dashboard";
        String EMPLOYEE_PORTAL = "Employee Portal";
    }

    String US_CODE = "US";
    String US_CALL_CODE = "+1";

    interface PLACEOFSUPPLY_CATEGORY {
        String REGION = "REGION";
        String COUNTRY = "COUNTRY";
    }

    interface UAE_STATES {
        String AB = "AB";
        String AJ = "AJ";
        String DU = "DU";
        String FU = "FU";
        String RA = "RA";
        String SH = "SH";
        String UM = "UM";
    }

    interface RecurrenceType {
        Integer MANUAL = 1;
        Integer DAY = 2;
        Integer MONTH = 3;
    }

    interface PDFDownloadType {
        String INLINE = "inline;";
        String ATTACHMENT = "attachment;";
    }

    int MAX_LIMIT_PRICE_LEVEL_PER_PRODUCT = 50;
    int DEFAULT_THREAD_COUNT_FOR_MULTI_PROCESS = 5;

    String DASHBOARD_WIDGETS_MAX_LIMIT = "DASHBOARD_WIDGETS_MAX_LIMIT";
    String TELEGRAM_RECURRENCE = "TELEGRAM_RECURRENCE";

    int SECTION = 1;
    int FIELD = 2;
    int PREDEFINED = 3;
    int ITEM_FIELD_PREDEFINED = 4;

    String VACANCY_FORM = "VACANCY";
    //vacancy approval status
    String VACANCY_APPROVAL_STATUS = "VACANCY_APPROVAL_STATUS";
    //children
    String VACANCY_APPROVAL_STATUS_REJECTED = "VACANCY_APPROVAL_STATUS_REJECTED";
    String VACANCY_APPROVAL_STATUS_SUBMITTED = "VACANCY_APPROVAL_STATUS_SUBMITTED";
    String VACANCY_APPROVAL_STATUS_APPROVED = "VACANCY_APPROVAL_STATUS_APPROVED";
    String VACANCY_APPROVAL_STATUS_DRAFT = "VACANCY_APPROVAL_STATUS_DRAFT";

    String TASK_UPDATES_LIST = "taskUpdatesList";
    String TASK_CREATOR = "TASK_CREATOR";
    String TASK_EMPLOYEES = "TASK_EMPLOYEES";
    String _WORKFLOW_MODULE_TASK = "_WORKFLOW_MODULE_TASK";
    String CANDIDATE_UPDATES_LIST = "candidateUpdatesList";

    //Shift
    String SHIFT_STATUS = "SHIFT_STATUS";
    String SHIFT_REJECTED = "SHIFT_REJECTED";
    String SHIFT_SUBMITTED = "SHIFT_SUBMITTED";
    String SHIFT_APPROVED = "SHIFT_APPROVED";
    String SHIFT_DRAFT = "SHIFT_DRAFT";

    //Rotation
    String ROTATION_STATUS = "ROTATION_STATUS";
    String ROTATION_REJECTED = "ROTATION_REJECTED";
    String ROTATION_SUBMITTED = "ROTATION_SUBMITTED";
    String ROTATION_APPROVED = "ROTATION_APPROVED";
    String ROTATION_DRAFT = "ROTATION_DRAFT";

    //Group Placement
    String GROUP_PLACEMENT_STATUS = "GROUP_PLACEMENT_STATUS";
    String GROUP_PLACEMENT_REJECTED = "GROUP_PLACEMENT_REJECTED";
    String GROUP_PLACEMENT_SUBMITTED = "GROUP_PLACEMENT_SUBMITTED";
    String GROUP_PLACEMENT_APPROVED = "GROUP_PLACEMENT_APPROVED";
    String GROUP_PLACEMENT_DRAFT = "GROUP_PLACEMENT_DRAFT";

    String OVERTIME_STATUS = "OVERTIME_STATUS";
    String OVERTIME_REJECTED = "OVERTIME_REJECTED";
    String OVERTIME_SUBMITTED = "OVERTIME_SUBMITTED";
    String OVERTIME_APPROVED = "OVERTIME_APPROVED";
    String OVERTIME_DRAFT = "OVERTIME_DRAFT";

    String OPPORTUNITY_STATUS = "OPPORTUNITY_STATUS";
    String OPPORTUNITY_REJECTED = "OPPORTUNITY_REJECTED";
    String OPPORTUNITY_SUBMITTED = "OPPORTUNITY_SUBMITTED";
    String OPPORTUNITY_APPROVED = "OPPORTUNITY_APPROVED";
    String OPPORTUNITY_DRAFT = "OPPORTUNITY_DRAFT";

    String HMRC_AUTH_URL = "hmrc/auth/authorize";


    String LOCATION_PROPERTY_OBJECTNAME = "LocListView";

    //Canidate type

    int SIMPLE_CANDIDATE = 130;
    int INTERVAL_CANDIDATE = 131;

    int HIBERNATE_CHUNK_SIZE = 500; // DO NOT CHANGE THIS PROPERTY
    int CORE_POOL_SIZE = 6; // DO NOT CHANGE THIS PROPERTY
    int MAX_POOL_SIZE = 30; // DO NOT CHANGE THIS PROPERTY

    int TTL_10_DAYS= 864000;

    List<String> QUICK_ADD_FORMS = Collections.singletonList(CustomFieldSection.Task.name());

    String FULL_TIME = "FULL_TIME";
    String S_075_TIME = "075_TIME";
    String PART_TIME = "PART_TIME";
    String QUARTER_TIME = "QUARTER_TIME";


    String SUB_TYPE_ADDED = "added";
    String SUB_TYPE_EDITED = "edited";
    String SUB_TYPE_DELETED = "deleted";
    String SUB_TYPE_SUBMITTED = "submitted";
    String SUB_TYPE_APPROVED = "approved";
    String SUB_TYPE_DECLINED = "declined";

    String BACKUPS_EMPLOYEE_STATUS = "BACKUPS_EMPLOYEE_STATUS";
    String BACKUPS_EMPLOYEE_REJECTED = "BACKUPS_EMPLOYEE_REJECTED";
    String BACKUPS_EMPLOYEE_SUBMITTED = "BACKUPS_EMPLOYEE_SUBMITTED";
    String BACKUPS_EMPLOYEE_APPROVED = "BACKUPS_EMPLOYEE_APPROVED";
    String BACKUPS_EMPLOYEE_DRAFT = "BACKUPS_EMPLOYEE_DRAFT";

    String INTERNAL = "INTERNAL";
    String GOVERNMENTAL = "GOVERNMENTAL";
    String BRAIN_UZ_DOMAIN="brainbm";
    String BRAIN_UZ2_DOMAIN="brain";

    interface WEEK_DAYS {
        String MONDAY = "0";
        String TUESDAY = "1";
        String WEDNESDAY = "2";
        String THURSDAY = "3";
        String FRIDAY = "4";
        String SATURDAY = "5";
        String SUNDAY = "6";
    }

}
