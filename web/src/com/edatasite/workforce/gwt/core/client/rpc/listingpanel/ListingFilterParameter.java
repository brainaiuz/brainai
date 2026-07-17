package com.edatasite.workforce.gwt.core.client.rpc.listingpanel;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractRpcMap;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.*;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 24-Aug-2010
 * Time: 19:22:20
 */
public class ListingFilterParameter extends AbstractRpcMap implements IsSerializable {

    private HashMap<String, String> valueMap;
    private ArrayList<Integer> roleIds;
    private ArrayList<Integer> locationIds;
    private boolean withVacand;

    public HashMap<String, String> getValueMap() {
        return getInstance();
    }

     /*public void setValueMap(HashMap<String, String> values) {
         this.valueMap = values;
     }*/

    //------------------------------------------------------------------------------------------------------------------
    private static final String ASSIGNED_ITEMS = "ASSIGNED_ITEMS";
    private static final String CHECK_NUMBER = "CHECK_NUMBER";
    private static final String CHECK_NAME = "CHECK_NAME";
    private static final String NEW_TYPE = "NEW_TYPE";
    private static final String ACTIVE = "ACTIVE";
    private static final String PRESENT_ACTIVE = "PRESENT_ACTIVE";
    private static final String ONLY_ISSUE_TASKS = "ONLY_ISSUE_TASKS";
    private static final String NUMBER = "NUMBER";
    private static final String CLEAR_AND_RECALCULATE = "CLEAR_AND_RECALCULATE";
    private static final String FACET_FILTER_JSON = "FACET_FILTER_JSON";
    private static final String LIST_PANEL_JSON = "LIST_PANEL_JSON";
    private static final String MESSAGE_STATUS = "MESSAGE_STATUS";
    private static final String FROM_OUTLOOK = "FROM_OUTLOOK";
    private static final String USER_ID = "USER_ID";
    private static final String FOLDER = "FOLDER";
    private static final String COUNTRY = "COUNTRY";
    private static final String CAMPAIGN = "CAMPAIGN";
    private static final String PROJECT = "PROJECT";
    private static final String EMPLOYEE = "EMPLOYEE";
    private static final String EMPLOYEE_IDS = "EMPLOYEE_IDS";
    private static final String DEPARTMENT = "DEPARTMENT";
    private static final String DEPARTMENT_ID = "DEPARTMENT_ID";
    private static final String QUALIFICATION = "QUALIFICATION";
    private static final String QUALIFICATION_ID = "QUALIFICATION_ID";
    private static final String CLIENT = "CLIENT";
    private static final String VIEWAS = "VIEWAS";
    private static final String PROJECTSTATUS = "PROJECTSTATUS";
    private static final String LOCATION = "LOCATION";
    private static final String SUPERVISOR = "SUPERVISOR";
    private static final String ISSUESTATUS = "ISSUESTATUS";
    private static final String ISSUEPRIORITY = "ISSUEPRIORITY";
    private static final String CRMENTITY = "CRMENTITY";
    private static final String CRMACCOUNT = "CRMACCOUNT";
    private static final String QUOTE = "QUOTE";
    private static final String CRMCONTACT = "CRMCONTACT";
    private static final String CRMOPPARTUNITY = "CRMOPPARTUNITY";
    private static final String CRMLEAD = "CRMLEAD";
    private static final String INVOICESTATUS = "INVOICESTATUS";
    private static final String INVOICECLIENT = "INVOICECLIENT";
    private static final String WEB_FORM_ID = "WEB_FORM_ID";
    private static final String RELATION = "RELATION";
    private static final String RELATION_TO = "RELATION_TO";
    private static final String COMPANY = "COMPANY";
    private static final String OBJECT_ID = "OBJECT_ID";
    private static final String CRMTASK_LIST = "CRMTASK_LIST";
    private static final String SORT_FIELD = "SORT_FIELD";
    private static final String SEARCH_KEY = "SEARCH_KEY";
    private static final String CLEAN_THE_LIST = "CLEAN_THE_LIST";
    private static final String FOR_EXPORT_ONLY = "FOR_EXPORT_ONLY";
    private static final String WITH_ENCRYPTED_LINK = "WITH_ENCRYPTED_LINK";
    private static final String ALL_FILTER = "ALL_FILTER";
    private static final String ALL_EMPLOYEES = "ALL_EMPLOYEES";
    private static final String IDS_ONLY = "IDS_ONLY";
    private static final String START_DATE = "START_DATE";
    private static final String DATE = "DATE";
    private static final String SELECTED_YEAR = "SELECTED_YEAR";
    private static final String SELECTED_MONTH = "SELECTED_MONTH";
    private static final String SELECTED_DAY = "SELECTED_DAY";
    private static final String END_DATE = "END_DATE";
    private static final String RELATION_TYPE = "RELATION_TYPE";
    private static final String EXCLUDED_TYPE = "EXCLUDED_TYPE";
    private static final String FOLDER_TYPE = "FOLDER_TYPE";
    private static final String TRASH_RESOURCE = "TRASH_RESOURCE";
    private static final String OTHER_RESOURCE = "OTHER_RESOURCE";
    private static final String OTHER_SHARED_RESOURCE = "OTHER_SHARED_RESOURCE";
    private static final String SHARED_RESOURCE = "SHARED_RESOURCE";
    private static final String DELETED = "DELETED";
    private static final String CORPORATE = "CORPORATE";
    private static final String ENTITY_NAME = "ENTITY_NAME";
    private static final String COLUMN_CODE = "COLUMN_CODE";
    private static final String SHOW_IN_LISTING = "SHOW_IN_LISTING";
    private static final String SHOW_IN_FILTER_GROUPING = "SHOW_IN_FILTER_GROUPING";
    private static final String FOLDER_NAME = "FOLDER_NAME";
    private static final String STATUS_VALUES = "STATUS_VALUES";
    private static final String LOOKUP = "LOOKUP";
    private static final String ACTUAL_DUE = "ACTUAL_DUE";
    private static final String TASK_STATUS = "TASK_STATUS";
    private static final String TIMESHEET_APPROVAL_SESSION_STATUS_ID = "TIMESHEET_APPROVAL_SESSION_STATUS_ID";
    private static final String CLIENT_CONTACT_ID = "CLIENT_CONTACT_ID";
    private static final String CONTACT_TYPE = "CONTACT_TYPE";
    private static final String SUPPLIER_CONTACT_ID = "SUPPLIER_CONTACT_ID";
    private static final String SUPPLIER = "SUPPLIER";
    private static final String CRM_CASE = "CRM_CASE";
    private static final String EMAIL = "EMAIL";
    private static final String ISSUE_ID = "ISSUE_ID";
    private static final String DUE_DATE = "DUE_DATE";
    private static final String TASK_PRIORITY = "TASK_PRIORITY";
    private static final String TYPE = "TYPE";
    private static final String QUANTITY_START_VALUE = "QUANTITY_START_VALUE";
    private static final String QUANTITY_END_VALUE = "QUANTITY_END_VALUE";
    private static final String PRICE_START_VALUE = "PRICE_START_VALUE";
    private static final String PRICE_END_VALUE = "PRICE_END_VALUE";
    private static final String PARAMS = "PARAMS";
    private static final String ROLES = "ROLES";
    private static final String BACKEND_USERS_ID = "BACKEND_USERS_ID";
    private static final String BUG_PRIORITY_ID = "BUG_PRIORITY_ID";
    private static final String BUG_STATUS_ID = "BUG_STATUS_ID";
    private static final String BUG_ASSIGNEE_ID = "BUG_ASSIGNEE_ID";
    private static final String SORTDIR = "SORTDIR";
    private static final String INVOICE_TYPE = "INVOICE_TYPE";
    private static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
    private static final String PRODUCT_TYPE = "PRODUCT_TYPE";
    private static final String INTERVAL = "INTERVAL";
    private static final String INTERVAL_LIMIT = "INTERVAL_LIMIT";
    private static final String UNIT_MEASUREMENT = "UNIT_MEASUREMENT";
    private static final String ISSUE_RELATED_TO = "ISSUE_RELATED_TO";
    private static final String SHOW_BUDGET = "SHOW_BUDGET";
    private static final String SHOW_SUMMARY = "SHOW_SUMMARY";
    private static final String SHOWYTD = "SHOWYTD";
    private static final String SICK_REQUEST_START_DATE = "SICK_REQUEST_START_DATE";
    private static final String SICK_REQUEST_END_DATE = "SICK_REQUEST_END_DATE";
    private static final String DEPARTMENT_IDS = "DEPARTMENT_IDS";
    private static final String REASON_IDS = "REASON_IDS";
    private static final String PROJECT_IDS = "PROJECT_IDS";
    private static final String LEAVE_DAY_CATEGORY = "LEAVE_DAY_CATEGORY";
    private static final String LEAVE_REQ_CATEGORY = "LEAVE_REQ_CATEGORY";
    private static final String SHOW_PROJECT = "SHOW_PROJECT";
    private static final String MESSAGE_ID = "MESSAGE_ID";
    private static final String MAIL_LIST_ID = "MAIL_LIST_ID";
    private static final String ENTITY_ID = "ENTITY_ID";
    private static final String SHOW_EMPLOYEE = "SHOW_EMPLOYEE";
    private static final String SHOW_DEPARTMENT = "SHOW_DEPARTMENT";
    private static final String SEARCH_TYPE = "SEARCH_TYPE";
    private static final String SHOW_ACTIVE = "SHOW_ACTIVE";
    private static final String SHOW_FILLED_CELLS = "SHOW_FILLED_CELLS";
    private static final String SHOW_ARCHIVED = "SHOW_ARCHIVED";
    private static final String SHOW_EVENT = "SHOW_EVENT";
    private static final String SHOW_ISSUE = "SHOW_ISSUE";
    private static final String SHOW_TASKS = "SHOW_TASKS";
    private static final String SHOW_LEAVE_REQUEST = "SHOW_LEAVE_REQUEST";
    private static final String ANNUAL_LEAVE_REQUEST = "ANNUAL_LEAVE_REQUEST";
    private static final String SHOW_PA = "SHOW_PA";
    private static final String SHOW_HEAD_OFFICE = "SHOW_HEAD_OFFICE";
    private static final String SHOW_HOLIDAYS = "SHOW_HOLIDAYS";
    private static final String RESOURCE_ID_NULL = "RESOURCE_ID_NULL";
    private static final String CF_COLUMN_CODE = "CF_COLUMN_CODE";
    private static final String CF_COLUMN_VALUE = "CF_COLUMN_VALUE";
    private static final String WORKSTREAM_ID = "WORKSTREAM_ID";
    private static final String WORKSTREAM_NAME = "WORKSTREAM_NAME";
    private static final String WAREHOUSE_ID = "WAREHOUSE_ID";
    private static final String FROM_COO = "FROM_COO";
    private static final String FROM_BUDGET_SHEET = "FROM_BUDGET_SHEET";
    private static final String FROM_EXCEL_PDF = "FROM_EXCEL_PDF";
    private static final String FROM_SIF_FILE = "FROM_SIF_FILE";
    private static final String COUNTRY_CODE = "COUNTRY_CODE";
    private static final String FOR_CSV_ONLY = "FOR_CSV_ONLY";
    private static final String SELECT_ITEM = "SELECT_ITEM";
    private static final String STOREFRONT_ID = "STOREFRONT_ID";
    private static final String CATEGORY_ID = "CATEGORY_ID";
    private static final String BRAND_ID = "BRAND_ID";
    private static final String CATEGORY = "CATEGORY";
    private static final String SHOW_CHILD = "SHOW_CHILD";
    private static final String DO_NOT_SEARCH = "DO_NOT_SEARCH";
    private static final String DO_NOT_EXPORT_TO_QB = "DO_NOT_EXPORT_TO_QB";
    private static final String CLIENT_NAME = "CLIENT_NAME";
    private static final String CRM = "CRM";
    private static final String HRMS = "HRMS";
    private static final String FILTIRIZE = "FILTIRIZE";
    private static final String WITH_IMAGE = "WITH_IMAGE";
    private static final String LOOK_UP_BY = "LOOK_UP_BY";
    private static final String CUSTOM_FIELDS_SHOWN = "CUSTOM_FIELDS_SHOWN";
    private static final String FOR_CHANGING = "FOR_CHANGING";
    private static final String INVOICE_ONLY = "INVOICE_ONLY";
    private static final String QUOTES_ONLY = "QUOTES_ONLY";
    private static final String STATUS_ID = "STATUS_ID";
    private static final String IGNORE_ID = "IGNORE_ID";
    private static final String FROM_MOBILE = "FROM_MOBILE";
    private static final String BASE_CURRENCY_ID = "BASE_CURRENCY_ID";
    private static final String CURRENCY_ID = "CURRENCY_ID";
    private static final String EXCHANGE_RATE = "EXCHANGE_RATE";
    private static final String ACCOUNT_CODE = "ACCOUNT_CODE";
    private static final String WITHOUT_TYPE = "WITHOUT_TYPE";
    private static final String SHOW_VARIATIONS = "SHOW_VARIATIONS";
    private static final String SEARCH_BY_PARENT = "SEARCH_BY_PARENT";
    private static final String FEATURED = "FEATURED";
    private static final String SPECIAL_OFFER = "SPECIAL_OFFER";
    private static final String SHOW_ON_OPPORTUNITY = "SHOW_ON_OPPORTUNITY";
    private static final String ITEM_ID = "ITEM_ID";
    private static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String SHOW_WEBSITE_PRODUCTS = "SHOW_WEBSITE_PRODUCTS";
    private static final String WEBSITE_ID = "WEBSITE_ID";
    private static final String DETECT_DUPLICATES = "DETECT_DUPLICATES";
    private static final String ALL_GOALS = "ALL_GOALS";
    private static final String ROOT_ID = "ROOT_ID";
    private static final String EMAIL_TYPE = "EMAIL_TYPE";
    private static final String TRACKER_ID = "TRACKER_ID";
    private static final String PM = "PM";
    private static final String SALE_ORDER = "SALE_ORDER";
    private static final String SYSTEM_SUB_FOLDER = "SYSTEM_SUB_FOLDER";
    private static final String USERTIME_HOURS_OFFSET = "USERTIME_HOURS_OFFSET";
    private static final String HAS_ONLY_CLIENT_ACCESS = "HAS_ONLY_CLIENT_ACCESS";
    private static final String HAS_ONLY_SALES_PERSON_ROLE = "HAS_ONLY_SALES_PERSON_ROLE";
    private static final String USE_AND_OPERATOR = "USE_AND_OPERATOR";
    private static final String IS_LIBRARY = "IS_LIBRARY";
    private static final String YEAR = "YEAR";
    private static final String DEVICE_ID = "DEVICE_ID";
    private static final String PARENT_ID = "PARENT_ID";
    private static final String SELECTED = "SELECTED";
    private static final String NAME = "NAME";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String PRIORITY = "PRIORITY";
    private static final String REASON = "REASON";
    private static final String EMAIL_FOLDER_ID = "EMAIL_FOLDER_ID";
    private static final String DATA_TYPE = "DATA_TYPE";
    private static final String RELATION_NAME = "RELATION_NAME";
    private static final String INCLUDE_EMAIL = "INCLUDE_EMAIL";
    private static final String INCLUDE_DAY_OFF = "INCLUDE_DAY_OFF";
    private static final String EVENT_TYPE = "EVENT_TYPE";
    private static final String CREATED_FROM = "CREATED_FROM";
    private static final String ALL_FILES = "ALL_FILES";
    private static final String IS_SHORT_LIST = "IS_SHORT_LIST";
    private static final String IS_SELECT_CANDIDATE = "IS_SELECT_CANDIDATE";
    private static final String VALIDITY_PERIOD_ID = "VALIDITY_PERIOD_ID";
    private static final String STATUS_CODE = "STATUS_CODE";
    private static final String REVIEWER_ID = "REVIEWER_ID";
    private static final String SHOW_COMPLETED_TASK = "SHOW_COMPLETED_TASK";
    private static final String USE_SELECTED_DATE = "USE_SELECTED_DATE";
    private static final String IS_SIMPILIFIED_REPORT_TEMPLATE = "IS_SIMPILIFIED_REPORT_TEMPLATE";
    private static final String ENTITY_BASED_ATTACHMENT_LIST = "ENTITY_BASED_ATTACHMENT_LIST";
    private static final String URL = "URL";
    private static final String SCHEDULED_COURSE_ID = "SCHEDULED_COURSE_ID";
    private static final String IS_INSTRUCTOR_LIST = "IS_INSTRUCTOR_LIST";
    private static final String SORT_FIELD_TYPE = "SORT_FIELD_TYPE";
    private static final String GUID = "GUID";
    private static final String COURSE_ID = "COURSE_ID";
    private static final String RESERVATION_IDS = "RESERVATION_IDS";
    private static final String LANGUAGE_ID = "LANGUAGE_ID";
    private static final String LANGUAGE_IDS = "LANGUAGE_IDS";
    private static final String LANGUAGE = "LANGUAGE";
    private static final String TRAINING_CENTER = "TRAINING_CENTER";
    private static final String BLOCK_EXTERNAL_GUID = "BLOCK_EXTERNAL_GUID";
    private static final String DO_NOT_INCLUDE_TASKS_FROM_TO_DO_LIST = "DO_NOT_INCLUDE_TASKS_FROM_TO_DO_LIST";
    private static final String INCLUDE_RESIGNED_EMPLOYEES = "INCLUDE_RESIGNED_EMPLOYEES";
    private static final String SHOW_EMPLOYEES_WITH_RESIGNATION_DATE = "SHOW_EMPLOYEES_WITH_RESIGNATION_DATE";
    private static final String EMPLOYEE_LIST_FOR_VACANT = "EMPLOYEE_LIST_FOR_VACANT";
    private static final String IS_INCIDENT = "IS_INCIDENT";
    private static final String TASK_ID = "TASK_ID";
    private static final String ISSUE_IDS = "ISSUE_IDS";
    private static final String VALIDATE_CHILD_ACCOUNTS = "VALIDATE_CHILD_ACCOUNTS";
    private static final String REPORTED_BY_ID = "REPORTED_BY_ID";
    private static final String RESOLVER_ID = "RESOLVER_ID";
    private static final String VIEW_TYPE = "VIEW_TYPE";
    private static final String SECTION = "SECTION";
    private static final String START_DATE_NC = "START_DATE_NC";
    private static final String END_DATE_NC = "END_DATE_NC";
    private static final String TASK_IDS = "TASK_IDS";
    private static final String WITH_ALL_TASK_NOTES = "WITH_ALL_TASK_NOTES";
    private static final String SHOW_UNSUBSCRIBEDS = "SHOW_UNSUBSCRIBEDS";
    private static final String IS_CONVERTED_LEAD = "IS_CONVERTED_LEAD";
    private static final String CONVERTED_LEAD_ID = "CONVERTED_LEAD_ID";
    private static final String MODULE = "MODULE";
    private static final String FORM = "FORM";
    private static final String COLUMN = "COLUMN";
    private static final String COLOPER = "COLOPER";
    private static final String SOURCE = "SOURCE";
    private static final String WORFLOW_TASK_LIST = "WORFLOW_TASK_LIST";
    private static final String WORFLOW_EVENT_LIST = "WORFLOW_EVENT_LIST";
    private static final String WORKFLOW_ID = "WORKFLOW_ID";
    private static final String WORKFLOW_ACTION_ID = "WORKFLOW_ACTION_ID";
    private static final String EMPLOYEE_STATUS = "EMPLOYEE_STATUS";
    private static final String EMPLOYEE_LIST = "EMPLOYEE_LIST";
    private static final String APPROVER_ID = "APPROVER_ID";
    private static final String POSITION = "POSITION";
    private static final String BRIGADA = "BRIGADA";
    private static final String CLASS_NAME = "CLASS_NAME";
    private static final String IS_PRODUCT_PI = "IS_PRODUCT_PI";
    private static final String SYSTEM = "SYSTEM";
    private static final String SYSTEM_ACCOUNT_CODES = "SYSTEM_ACCOUNT_CODES";
    private static final String CONTRACT_CLIENT = "CONTRACT_CLIENT";
    private static final String CANDIDATE_LIST = "CANDIDATE_LIST";
    private static final String ESS_USER = "ESS_USER";

    private static final String START = "START";

    private static final String CURRENT_PAGE = "CURRENT_PAGE";
    private static final String LIMIT = "LIMIT";
    private static final String FROM_DATE = "FROM_DATE";
    private static final String TO_DATE = "TO_DATE";
    private static final String ASC = "ASC";
    private static final String BRIEFLY = "BRIEFLY";
    private static final String SHOW_HIDDEN = "SHOW_HIDDEN";
    private static final String IS_SEARCH_BUTTON = "IS_SEARCH_BUTTON";
    private static final String RECURRENCE_STATUS = "RECURRENCE_STATUS";
    private static final String IS_WAGE_RATE = "IS_WAGE_RATE";
    private static final String IS_WITH_TAX = "IS_WITH_TAX";
    private static final String ACCOUNT_TRANSACTION_STATUS = "ACCOUNT_TRANSACTION_STATUS";
    private static final String STEP_ID = "STEP_ID";
    private static final String SKILL_IDS = "SKILL_IDS";
    private static final String POSITION_IDS = "POSITION_IDS";
    private static final String BRIGADA_IDS = "BRIGADA_IDS";
    private static final String NO_POSITION = "NO_POSITION";
    private static final String AGENT_ID = "AGENT_ID";
    private static final String PAYROLL_BATCH_ID = "PAYROLL_BATCH_ID";
    private static final String FROM_AMOUNT = "FROM_AMOUNT";
    private static final String TO_AMOUNT = "TO_AMOUNT";
    private static final String ROLE_ID = "ROLE_ID";
    private static final String MONTH_ID = "MONTH_ID";
    private static final String MONTH_NAME = "MONTH_NAME";
    private static final String MONTH_WITH_YEAR = "MONTH_WITH_YEAR";
    private static final String DAY = "DAY";
    private static final String DISCLUDED_SCHEMA_ID = "DISCLUDED_SCHEMA_ID";
    private static final String USER_DATE = "USER_DATE";
    private static final String WITH_CODE = "WITH_CODE";
    private static final String WITHOUT_CODE = "WITHOUT_CODE";
    private static final String IS_RECEIVABLE = "IS_RECEIVABLE";
    private static final String FROM_REGISTRATION_DATE = "FROM_REGISTRATION_DATE";
    private static final String TO_REGISTRATION_DATE = "TO_REGISTRATION_DATE";
    private static final String FROM_EXPIRATION_DATE = "FROM_EXPIRATION_DATE";
    private static final String TO_EXPIRATION_DATE = "TO_EXPIRATION_DATE";
    private static final String SUBSCRIPTION_TYPE = "SUBSCRIPTION_TYPE";

    private static final String SERIAL_NUMBER = "SERIAL_NUMBER";
    private static final String FROM_EXPIRY_DATE = "FROM_EXPIRY_DATE";
    private static final String TO_EXPIRY_DATE = "TO_EXPIRY_DATE";
    private static final String BATCH_LIST_TYPE = "BATCH_LIST_TYPE";
    private static final String WAREHOUSE = "WAREHOUSE";

    private static final String BANK_ACCOUNT_CODE = "BANK_ACCOUNT_CODE";
    private static final String BANK_ACCOUNT_NAME = "BANK_ACCOUNT_NAME";
    private static final String BANK_ACCOUNT_NUMBER = "BANK_ACCOUNT_NUMBER";
    private static final String BANK_ACCOUNT_CURRENCY_ID = "BANK_ACCOUNT_CURRENCY_ID";

    private static final String START_DATE_STR = "START_DATE_STR";
    private static final String END_DATE_STR = "END_DATE_STR";
    private static final String SHOW_SUB_ACCOUNT_TRANSACTION = "SHOW_SUB_ACCOUNT_TRANSACTION";
    private static final String INCLUDE_PREV_PAGES_BALANCE = "INCLUDE_PREV_PAGES_BALANCE";
    private static final String EXCLUDE_NUMBER = "EXCLUDE_NUMBER";

    private static final String PAYMENTS_TOTAL = "PAYMENTS_TOTAL";
    private static final String DAILY_RATE_BY_EMPLOYER_SETTINGS = "DAILY_RATE_BY_EMPLOYER_SETTINGS";
    private static final String LEAVE_DAYS_IMPACT = "LEAVE_DAYS_IMPACT";
    private static final String PERIOD_ID = "PERIOD_ID";
    private static final String PAYRUN_ID = "PAYRUN_ID";
    private static final String TEMPLATE_ID = "TEMPLATE_ID";
    private static final String AVOID_TYPE = "AVOAVOID_TYPEID_TYPE";
    private static final String FORM_TYPE = "FORM_TYPE";
    private static final String AVOID_ID = "AVOID_ID";
    private static final String PAYMENT_METHOD_ID = "PAYMENT_METHOD_ID";
    private static final String RELATED_PROJECT = "RELATED_PROJECT";
    private static final String JOURNAL_ID = "JOURNAL_ID";
    private static final String FOR_BANK = "FOR_BANK";
    private static final String AVOID_ZERO = "AVOID_ZERO";
    private static final String IS_CREDIT_NOTE = "IS_CREDIT_NOTE";
    private static final String PAID = "PAID";
    private static final String SHOW_ALL = "SHOW_ALL";
    private static final String SHOW_IN_BASE = "SHOW_IN_BASE";
    private static final String EXCLUDE_PRE_PAYMENTS = "EXCLUDE_PRE_PAYMENTS";
    private static final String JOB_FAMILY_ID = "JOB_FAMILY_ID";
    private static final String COLUMN_METADATA_ID = "COLUMN_METADATA_ID";
    private static final String IS_GDN = "IS_GDN";
    private static final String PERMISSION_CODE = "PERMISSION_CODE";

    private static final String ACCESS_ENABLED = "ACCESS_ENABLED";
    private static final String SHOW_PRODUCT_BATCHES = "SHOW_PRODUCT_BATCHES";
    private static final String GROUP_PAYRUN_ID = "GROUP_PAYRUN_ID";
    private static final String EMP_CODE_ADJOINED = "EMP_CODE_ADJOINED";
    private static final String IS_FAVOURITE = "IS_FAVOURITE";
    private static final String IS_LETTER_SEARCH = "IS_LETTER_SEARCH";
    private static final String IS_WIDGET_SEARCH = "IS_WIDGET_SEARCH";
    private static final String IS_LANDSCAPE = "IS_LANDSCAPE";
    private static final String PROPERTY_CODE = "PROPERTY_CODE";
    private static final String EXCLUDE_EXEMPT_OUTOFSCOPE = "EXCLUDE_EXEMPT_OUTOFSCOPE";
    private static final String ATTACHMENT_ID = "ATTACHMENT_ID";
    private static final String IS_COPY = "IS_COPY";
    private static final String IS_OVERPAYMENT = "IS_OVERPAYMENT";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String BANK_NAME = "BANK_NAME";
    private static final String PROVIDER_ID = "PROVIDER_ID";
    private static final String CALCULATE_DEPRECIATION = "CALCULATE_DEPRECIATION";
    private static final String SHIFT_TYPE = "SHIFT_TYPE";
    private static final String CALCULATE_BY_LAST_MONTH = "CALCULATE_BY_LAST_MONTH";
    //------------------------------------------------------------------------------------------------------------------
    private FacetFilterRpc facetFilter;
    private ListPanelToolRpc listPanelTool;
    private Integer[] categories;
    private Integer[] statusIDs;
    private String[] statusCodes;
    private HashMap<String, String> customFields;
    private ArrayList<String> columnsOfListing;
    private ArrayList<Integer> objectIDs;
    private ArrayList<Integer> clientIds;
    private boolean hasFullListAccess;
    private boolean isOrderByDepartment;
    private boolean isOrderByPosition;
    private boolean ignoreAllCurrencyValidation;
    private boolean isApplyForSubDepartment;

    public ListingFilterParameter(final boolean briefly) {
        this.addBool(ListingFilterParameter.BRIEFLY, briefly);
    }

    private ArrayList<Integer> projectIdList;
    private Integer[] compaines;
    private String[] parameters;
    private ArrayList<String> accountTypes;
    private ArrayList<String> relationTypes;
    private ArrayList<Date> dates;
    private boolean fromListing;
    private Boolean isStockAdjustment;
    private String reasonCode;
    private ArrayList<String> options;
    private boolean withBlockedAccount;
    private boolean prepayment;
    private boolean isShowView;
    private boolean isLevelActive;
    private Integer showMembersForOrgChart;
    private Integer showAllSubMembersForOrgChart;
    private Integer departmentDoubleClickId;
    private Integer levelOptionList;
    private Integer levelOptionListForSprvs;
    private String objectsIds;
    private String collapsed;
    private String shownObjects;
    private boolean basedOnTimesheet;
    private boolean allDay;
    private boolean isFromCase;
    private boolean isFromCandidate;
    private boolean checkBeforeSelected;
    private Integer beforeSelectedId;
    private boolean hasAccessToChange = true;
    private boolean isFromProduct;
    private boolean isBasicPlusAllowancePaymentType = false;
    private ArrayList<PaymentDeductionSelectItem> paymentCategories;
    private boolean isPayment = false;
    private String manualEntryType;
    private String propertyCode;
    private boolean isFromEmployeeProfile;
    private boolean isFromShift;
    private boolean isFromPartnerBackend;
    private Integer supervisorId;
    private boolean fromMultiDepartment;
    private Date shiftPeriod;
    private boolean fromPositionBulkUpdate;
    private boolean fromDepartmentBulkUpdate;

    private boolean enablePayments = false;

    private boolean takeByPeriod = false;
    private boolean isVisableAll = false;
    private boolean isthisMonthEmployees = false;
    private boolean allByProjectGoal = false;
    private boolean itemTable;
    private Boolean isFromTerminal;
    //------------------------------------------------------------------------------------------------------------------

    public ListingFilterParameter() {
    }

    public ListingFilterParameter(final Integer clientId, final Integer projectId,
                                  final Integer departmentId, final Integer employeeId, final Integer viewAsId) {
        this.setClientId(clientId);
        this.setProjectId(projectId);
        this.setDepartmentId(departmentId);
        this.setEmployeeId(employeeId);
        this.setViewAsId(viewAsId);
    }

    public boolean isTakeByPeriod() {
        return takeByPeriod;
    }

    public void setTakeByPeriod(boolean takeByPeriod) {
        this.takeByPeriod = takeByPeriod;
    }

    public static String getStartDateStr() {
        return ListingFilterParameter.START_DATE_STR;
    }

    public void setStartDateStr(final String dateStr) {
        this.addString(ListingFilterParameter.START_DATE_STR, dateStr);
    }

    public static String getEndDateStr() {
        return ListingFilterParameter.END_DATE_STR;
    }

    public void setEndDateStr(final String dateStr) {
        this.addString(ListingFilterParameter.END_DATE_STR, dateStr);
    }

    protected HashMap<String, String> getInstance() {
        return this.valueMap = this.valueMap == null ? new HashMap<>() : this.valueMap;
    }

    public String getObjectsIds() {
        return this.objectsIds;
    }

    public void setObjectsIds(final String objectsIds) {
        this.objectsIds = objectsIds;
    }

    public boolean isLandscape() {
        return this.getBool(ListingFilterParameter.IS_LANDSCAPE);
    }

    public void setLandscape(final boolean value) {
        this.addBool(ListingFilterParameter.IS_LANDSCAPE, value);
    }

    public boolean isShowProductBatches() {
        return this.getBool(ListingFilterParameter.SHOW_PRODUCT_BATCHES);
    }

    public void setShowProductBatches(final boolean value) {
        this.addBool(ListingFilterParameter.SHOW_PRODUCT_BATCHES, value);
    }

    public boolean isCrmTaskList() {
        return this.getBool(ListingFilterParameter.CRMTASK_LIST);
    }

    public void setCrmTaskList(final boolean crmTaskList) {
        this.addBool(ListingFilterParameter.CRMTASK_LIST, crmTaskList);
    }

    public boolean isWorkflowTaskList() {
        return this.getBool(ListingFilterParameter.WORFLOW_TASK_LIST);
    }

    public void setWorflowTaskList(final boolean crmTaskList) {
        this.addBool(ListingFilterParameter.WORFLOW_TASK_LIST, crmTaskList);
    }

    public boolean isWorkflowEventList() {
        return this.getBool(ListingFilterParameter.WORFLOW_EVENT_LIST);
    }

    public void setWorflowEventList(final boolean crmTaskList) {
        this.addBool(ListingFilterParameter.WORFLOW_EVENT_LIST, crmTaskList);
    }

    public boolean isSystem() {
        return this.getBool(ListingFilterParameter.SYSTEM);
    }

    public void setSystem(final boolean system) {
        this.addBool(ListingFilterParameter.SYSTEM, system);
    }

    public String getSystemAccountCodes() {
        return this.getString(ListingFilterParameter.SYSTEM_ACCOUNT_CODES);
    }

    public void setSystemAccountCodes(final String systemAccountCodes) {
        this.addString(ListingFilterParameter.SYSTEM_ACCOUNT_CODES, systemAccountCodes);
    }

    public Integer getWorkflowID() {
        return this.getInteger(ListingFilterParameter.WORKFLOW_ID);
    }

    public void setWorkflowID(final Integer workflowID) {
        this.addInteger(ListingFilterParameter.WORKFLOW_ID, workflowID);
    }

    public Integer getWorkflowActionId() {
        return this.getInteger(ListingFilterParameter.WORKFLOW_ACTION_ID);
    }

    public void setWorkflowActionId(final Integer workflowActionId) {
        this.addInteger(ListingFilterParameter.WORKFLOW_ACTION_ID, workflowActionId);
    }

    public boolean isBriefly() {
        return !Boolean.FALSE.equals(this.getBoolean(ListingFilterParameter.BRIEFLY));
    }

    public void setBriefly(final boolean briefly) {
        this.addBool(ListingFilterParameter.BRIEFLY, briefly);
    }

    public boolean isShowHidden() {
        return Boolean.TRUE.equals(this.getBoolean(ListingFilterParameter.SHOW_HIDDEN));
    }

    public void setShowHidden(final boolean v) {
        this.addBool(ListingFilterParameter.SHOW_HIDDEN, v);
    }

    public Integer getStart() {
        return this.getInt(ListingFilterParameter.START);
    }

    public void setStart(final Integer start) {
        this.addInteger(ListingFilterParameter.START, start);
    }

    public Integer getCurrentPage() {
        return this.getInt(ListingFilterParameter.CURRENT_PAGE);
    }

    public void setCurrentPage(final Integer currentPage) {
        this.addInteger(ListingFilterParameter.CURRENT_PAGE, currentPage);
    }

    public Integer getLimit() {
        return this.getInteger(ListingFilterParameter.LIMIT) == null ? 0 : this.getInteger(ListingFilterParameter.LIMIT);
    }

    public void setLimit(final int limit) {
        this.addInt(ListingFilterParameter.LIMIT, limit);
    }

    public boolean isFromOutlook() {
        return this.getBool(ListingFilterParameter.FROM_OUTLOOK);
    }

    public void setFromOutlook(final boolean fromOutlook) {
        this.addBool(ListingFilterParameter.FROM_OUTLOOK, fromOutlook);
    }

    public String getSortField() {
        return this.getString(ListingFilterParameter.SORT_FIELD);
    }

    public void setSortField(final String sortField) {
        this.addString(ListingFilterParameter.SORT_FIELD, sortField);
    }

    public String getSearchKey() {
        return this.getString(ListingFilterParameter.SEARCH_KEY);
    }

    public void setSearchKey(final String searchKey) {
        this.addString(ListingFilterParameter.SEARCH_KEY, searchKey);
    }

    public String getSection() {
        return this.getString(ListingFilterParameter.SECTION);
    }

    public void setSection(final String section) {
        this.addString(ListingFilterParameter.SECTION, section);
    }

    public String getStartDateNC() {
        return this.getString(ListingFilterParameter.START_DATE_NC);
    }

    public void setStartDateNC(final String startDateNC) {
        this.addString(ListingFilterParameter.START_DATE_NC, startDateNC);
    }

    public String getEndDateNC() {
        return this.getString(ListingFilterParameter.END_DATE_NC);
    }

    public void setEndDateNC(final String endDateNC) {
        this.addString(ListingFilterParameter.END_DATE_NC, endDateNC);
    }

    public boolean isEssUser() {
        return this.getBool(ListingFilterParameter.ESS_USER);
    }

    public void setEssUser(final boolean isEssUser) {
        this.addBool(ListingFilterParameter.ESS_USER, isEssUser);
    }

    public Boolean isStockAdjustment() {
        return isStockAdjustment;
    }

    public void setStockAdjustment(Boolean stockAdjustment) {
        isStockAdjustment = stockAdjustment;
    }

    public String getSqlSearchKey() {
        if (this.isValidSearchKey()) {
            return ((this.isLookUp() ? "" : " ") + this.getSearchKey().trim() + " ").replace("'", "''").replace(" ", "%").toLowerCase();
        } else {
            return null;
        }
    }

    public boolean isValidSearchKey() {
        if (getSearchKey() == null || "".equals(getSearchKey())) {
            return false;
        }
        if (getSearchKey().trim().length() > 20) {
            this.setSearchKey(this.getSearchKey().substring(0, 20));
        }
        return (
                getSearchKey() != null
                        && !getSearchKey().trim().equals("")
        );
    }

    public boolean isAscending() {
        return !Boolean.FALSE.equals(this.getBoolean(ListingFilterParameter.ASC));
    }

    public void setAscending(final boolean ascending) {
        this.addBool(ListingFilterParameter.ASC, ascending);
    }

    public boolean isSearchButton() {
        return this.getBool(ListingFilterParameter.IS_SEARCH_BUTTON);
    }

    public void setSearchButton(final boolean searchButton) {
        this.addBool(ListingFilterParameter.IS_SEARCH_BUTTON, searchButton);
    }

    public boolean isCleanTheList() {
        return this.getBool(ListingFilterParameter.CLEAN_THE_LIST);
    }

    public void setCleanTheList(final boolean cleanTheList) {
        this.addBool(ListingFilterParameter.CLEAN_THE_LIST, cleanTheList);
    }

    public FacetFilterRpc getFacetFilter() {
        return this.facetFilter;
    }

    public void setFacetFilter(final FacetFilterRpc facetFilter) {
        this.facetFilter = facetFilter;
    }

    public ListPanelToolRpc getListPanelTool() {
        return this.listPanelTool;
    }

    public void setListPanelTool(final ListPanelToolRpc listPanelTool) {
        this.listPanelTool = listPanelTool;
    }

    public Integer getObjectId() {
        return this.getInteger(ListingFilterParameter.OBJECT_ID);
    }

    public void setObjectId(final Integer objectId) {
        this.addInteger(ListingFilterParameter.OBJECT_ID, objectId);
    }

    public Integer getTrackerID() {
        return this.getInteger(ListingFilterParameter.TRACKER_ID);
    }

    public void setTrackerID(final Integer objectID) {
        this.addInteger(ListingFilterParameter.TRACKER_ID, objectID);
    }

    public Integer getUserID() {
        return this.getInteger(ListingFilterParameter.USER_ID);
    }

    public void setUserID(final Integer userId) {
        this.addInteger(ListingFilterParameter.USER_ID, userId);
    }

    public Integer getProjectId() {
        return this.getInteger(ListingFilterParameter.PROJECT);
    }

    public void setProjectId(final Integer projectId) {
        this.addInteger(ListingFilterParameter.PROJECT, projectId);
    }

    public Integer getEmployeeId() {
        return this.getInteger(ListingFilterParameter.EMPLOYEE);
    }

    public void setEmployeeId(final Integer employeeId) {
        this.addInteger(ListingFilterParameter.EMPLOYEE, employeeId);
    }

    public String getEmployeeIDs() {
        return this.getString(ListingFilterParameter.EMPLOYEE_IDS);
    }

    public void setEmployeeIDs(final String employeeId) {
        this.addString(ListingFilterParameter.EMPLOYEE_IDS, employeeId);
    }

    public Integer getGroupPayrunID() {
        return this.getInteger(ListingFilterParameter.GROUP_PAYRUN_ID);
    }

    public void setGroupPayrunID(final Integer payrunID) {
        this.addInteger(ListingFilterParameter.GROUP_PAYRUN_ID, payrunID);
    }

    public boolean isEmpCodeAdjoined() {
        return Optional.ofNullable(getBoolean(ListingFilterParameter.EMP_CODE_ADJOINED)).orElse(false);
    }

    public void setEmpCodeAdjoined(final Boolean empCodeAdjoined) {
        addBoolean(ListingFilterParameter.EMP_CODE_ADJOINED, empCodeAdjoined);
    }

    public Integer getDepartmentId() {
        return this.getInteger(ListingFilterParameter.DEPARTMENT_ID);
    }

    public void setDepartmentId(final Integer departmentId) {
        this.addInteger(ListingFilterParameter.DEPARTMENT_ID, departmentId);
    }

    public Integer getQualificationId() {
        return this.getInteger(ListingFilterParameter.QUALIFICATION_ID);
    }

    public void setQualificationId(final Integer qualificationId) {
        this.addInteger(ListingFilterParameter.QUALIFICATION_ID, qualificationId);
    }


    public Integer getClientId() {
        return this.getInteger(ListingFilterParameter.CLIENT);
    }

    public void setClientId(final Integer clientId) {
        this.addInteger(ListingFilterParameter.CLIENT, clientId);
    }

    public Integer getSupplierId() {
        return this.getInteger(ListingFilterParameter.SUPPLIER);
    }

    public void setSupplierId(final Integer supplierId) {
        this.addInteger(ListingFilterParameter.SUPPLIER, supplierId);
    }

    public Integer getContractClientId() {
        return this.getInteger(ListingFilterParameter.CONTRACT_CLIENT);
    }

    public void setContractClientId(final Integer contractClientId) {
        this.addInteger(ListingFilterParameter.CONTRACT_CLIENT, contractClientId);
    }

    public Integer getViewAsId() {
        return this.getInteger(ListingFilterParameter.VIEWAS);
    }

    public void setViewAsId(final Integer viewAsId) {
        this.addInteger(ListingFilterParameter.VIEWAS, viewAsId);
    }

    public Integer getProjectStatusId() {
        return this.getInteger(ListingFilterParameter.PROJECTSTATUS);
    }

    public void setProjectStatusId(final Integer projectStatusId) {
        this.addInteger(ListingFilterParameter.PROJECTSTATUS, projectStatusId);
    }

    public Integer getLocationId() {
        return this.getInteger(ListingFilterParameter.LOCATION);
    }

    public void setLocationId(final Integer locationId) {
        this.addInteger(ListingFilterParameter.LOCATION, locationId);
    }

    public Integer getSupervisorId() {
        return this.getInteger(ListingFilterParameter.SUPERVISOR);
    }

    public void setSupervisorId(final Integer supervisorId) {
        this.addInteger(ListingFilterParameter.SUPERVISOR, supervisorId);
    }

    public Integer getIssueStatusId() {
        return this.getInteger(ListingFilterParameter.ISSUESTATUS);
    }

    public void setIssueStatusId(final Integer issueId) {
        this.addInteger(ListingFilterParameter.ISSUESTATUS, issueId);
    }

    public Integer getIssuePriorityId() {
        return this.getInteger(ListingFilterParameter.ISSUEPRIORITY);
    }

    public void setIssuePriorityId(final Integer issuePriorityId) {
        this.addInteger(ListingFilterParameter.ISSUEPRIORITY, issuePriorityId);
    }

    public Integer getCrmEntityId() {
        return this.getInteger(ListingFilterParameter.CRMENTITY);
    }

    public void setCrmEntityId(final Integer crmEntityId) {
        this.addInteger(ListingFilterParameter.CRMENTITY, crmEntityId);
    }

    public Integer getCrmAccountId() {
        return this.getInteger(ListingFilterParameter.CRMACCOUNT);
    }

    public void setCrmAccountId(final Integer crmAccountId) {
        this.addInteger(ListingFilterParameter.CRMACCOUNT, crmAccountId);
    }

    public Integer getCrmContactId() {
        return this.getInteger(ListingFilterParameter.CRMCONTACT);
    }

    public void setCrmContactId(final Integer crmContactId) {
        this.addInteger(ListingFilterParameter.CRMCONTACT, crmContactId);
    }

    public Integer getQuoteId() {
        return this.getInteger(ListingFilterParameter.QUOTE);
    }

    public void setQuoteId(final Integer quoteId) {
        this.addInteger(ListingFilterParameter.QUOTE, quoteId);
    }

    public Integer getCrmOppartunityId() {
        return this.getInteger(ListingFilterParameter.CRMOPPARTUNITY);
    }

    public void setCrmOppartunityId(final Integer crmOppartunityId) {
        this.addInteger(ListingFilterParameter.CRMOPPARTUNITY, crmOppartunityId);
    }

    public Integer getCrmLeadId() {
        return this.getInteger(ListingFilterParameter.CRMLEAD);
    }

    public void setCrmLeadId(final Integer crmLeadId) {
        this.addInteger(ListingFilterParameter.CRMLEAD, crmLeadId);
    }

    public Integer getInvoiceStatusId() {
        return this.getInteger(ListingFilterParameter.INVOICESTATUS);
    }

    public void setInvoiceStatusId(final Integer invoiceStatusId) {
        this.addInteger(ListingFilterParameter.INVOICESTATUS, invoiceStatusId);
    }

    public Integer getInvoiceClientId() {
        return this.getInteger(ListingFilterParameter.INVOICECLIENT);
    }

    public void setInvoiceClientId(final Integer invoiceClientId) {
        this.addInteger(ListingFilterParameter.INVOICECLIENT, invoiceClientId);
    }

    public boolean isForExportOnly() {
        return this.getBool(ListingFilterParameter.FOR_EXPORT_ONLY);
    }

    public void setForExportOnly(final boolean forExportOnly) {
        this.addBool(ListingFilterParameter.FOR_EXPORT_ONLY, forExportOnly);
    }

    public boolean isWithEncryptedLink() {
        return this.getBoolean(ListingFilterParameter.WITH_ENCRYPTED_LINK) != null && this.getBool(ListingFilterParameter.WITH_ENCRYPTED_LINK);
    }

    public void setWithEncryptedLink(final boolean withEncryptedLink) {
        this.addBool(ListingFilterParameter.WITH_ENCRYPTED_LINK, withEncryptedLink);
    }

    public String getFacetFilterJson() {
        return this.getString(ListingFilterParameter.FACET_FILTER_JSON);
    }

    public void setFacetFilterJson(final String facetFilterJson) {
        this.addString(ListingFilterParameter.FACET_FILTER_JSON, facetFilterJson);
    }

    public String getListPanelToolJson() {
        return this.getString(ListingFilterParameter.LIST_PANEL_JSON);
    }

    public void setListPanelToolJson(final String listPanelToolJson) {
        this.addString(ListingFilterParameter.LIST_PANEL_JSON, listPanelToolJson);
    }

    protected String getAsString(final Object value) {
        return value == null ? null : value.toString();
    }

    public String getMessageStatus() {
        return this.getString(ListingFilterParameter.MESSAGE_STATUS);
    }

    public void setMessageStatus(final String messageStatus) {
        this.addString(ListingFilterParameter.MESSAGE_STATUS, messageStatus);
    }

    public Integer getEmployeeStatusID() {
        return this.getInteger(ListingFilterParameter.EMPLOYEE_STATUS);
    }

    public void setEmployeeStatusID(final Integer statusID) {
        this.addInteger(ListingFilterParameter.EMPLOYEE_STATUS, statusID);
    }

    public Integer getPositionID() {
        return this.getInteger(ListingFilterParameter.POSITION);
    }

    public void setPositionID(final Integer positionID) {
        this.addInteger(ListingFilterParameter.POSITION, positionID);
    }

    public Integer getBrigadaID() {
        return this.getInteger(ListingFilterParameter.BRIGADA);
    }

    public void setBrigadaID(final Integer brigadaID) {
        this.addInteger(ListingFilterParameter.BRIGADA, brigadaID);
    }

    public String getClassName() {
        return this.getString(ListingFilterParameter.CLASS_NAME);
    }

    public void setClassName(final String className) {
        this.addString(ListingFilterParameter.CLASS_NAME, className);
    }

    public HashMap<String, String> getRequestParams(final int... tmp) {

        final HashMap<String, String> parametersMap = new HashMap<>();

        parametersMap.put("unitMeasurement", this.getAsString(this.getUnitMeasurementId()));
        parametersMap.put("name", this.getAsString(this.getName()));
        parametersMap.put("createdFrom", this.getAsString(this.getCreatedFrom()));
        parametersMap.put("backendUsersId", this.getAsString(this.getBackendUsersId()));
        parametersMap.put("bugPriorityId", this.getAsString(this.getBugPriorityId()));
        parametersMap.put("bugStatusId", this.getAsString(this.getBugStatusId()));
        parametersMap.put("bugAssigneeId", this.getAsString(this.getBugAssigneeId()));
        parametersMap.put("clientId", this.getAsString(this.getClientId()));
        parametersMap.put("departmentId", this.getAsString(this.getDepartmentId()));
        parametersMap.put("employeeId", this.getAsString(this.getEmployeeId()));
        parametersMap.put("issueId", this.getAsString(this.getIssueId()));
        parametersMap.put("endDate", this.getAsString(this.getEndDate() != null ? this.getEndDate().getTime() : null));
        parametersMap.put("invoiceClientId", this.getAsString(this.getInvoiceClientId()));
        parametersMap.put("invoiceStatusId", this.getAsString(this.getInvoiceStatusId()));
        parametersMap.put("warehouseId", this.getAsString(this.getWarehouseID()));
        parametersMap.put("priceStartValue", this.getAsString(this.getPriceStartValue()));
        parametersMap.put("projectId", this.getAsString(this.getProjectId()));
        parametersMap.put("projectStatusId", this.getAsString(this.getProjectStatusId()));
        parametersMap.put("quantityEndValue", this.getAsString(this.getQuantityEndValue()));
        parametersMap.put("quantityStartValue", this.getAsString(this.getQuantityStartValue()));
        parametersMap.put("searchKey", this.getAsString(this.getSearchKey()));
        parametersMap.put("params", this.getAsString(this.getParams()));
        parametersMap.put("roles", this.getAsString(this.getRoles()));
        parametersMap.put("startDate", this.getAsString(this.getStartDate() != null ? this.getStartDate().getTime() : null));
        parametersMap.put("taskPriorityId", this.getAsString(this.getTaskPriorityId()));
        parametersMap.put("taskStatusId", this.getAsString(this.getTaskStatusId()));
        parametersMap.put("taskID", this.getAsString(this.getTaskID()));
        parametersMap.put("timeSheetApprovalSessionStatusId", this.getAsString(this.getTimeSheetApprovalSessionStatusId()));
        parametersMap.put("type", this.getAsString(this.getType()));
        parametersMap.put("viewAsId", this.getAsString(this.getViewAsId()));
        parametersMap.put("actualDue", this.getAsString(this.isActualDue()));
        parametersMap.put("actualStart", this.getAsString(this.isActualStart()));
        parametersMap.put("plannedDue", this.getAsString(this.isPlannedDue()));
        parametersMap.put("plannedStart", this.getAsString(this.isPlannedStart()));
        parametersMap.put("featured", this.getAsString(this.isFeatured()));
        parametersMap.put("priceEndValue", this.getAsString(this.getPriceEndValue()));
        parametersMap.put("start", this.getAsString(this.getStart()));
        parametersMap.put("limit", this.getAsString(this.getLimit()));
        parametersMap.put("currentPage", this.getAsString(this.getCurrentPage()));
        parametersMap.put("sortField", this.getAsString(this.getSortField()));
        parametersMap.put("sortDir", this.getAsString(this.getSortDir()));
        parametersMap.put("invoiceType", this.getAsString(this.getInvoiceType()));
        parametersMap.put("dueDate", this.getAsString(this.getDueDate() != null ? this.getDueDate().getTime() : null));
        parametersMap.put("fromDate", this.getAsString(this.getFromDate()));
        parametersMap.put("toDate", this.getAsString(this.getToDate()));
        parametersMap.put("showBudget", this.getAsString(this.isShowBudget()));
        parametersMap.put("summaryView", this.getAsString(this.isSummaryView()));
        parametersMap.put("showYTD", this.getAsString(this.isShowYTD()));
        parametersMap.put("showInBase", this.getAsString(this.isShowInBase()));
        parametersMap.put("sickRequestStartDate", this.getAsString(this.getSickRequestStartDate() != null ? this.getSickRequestStartDate().getTime() : null));
        parametersMap.put("sickRequestEndDate", this.getAsString(this.getSickRequestEndDate() != null ? this.getSickRequestEndDate().getTime() : null));
        parametersMap.put("departmentIds", this.getAsString(this.getDepartmentIds()));
        parametersMap.put("projectIds", this.getAsString(this.getProjectIds()));
        parametersMap.put("leaveDayCategory", this.getAsString(this.getLeaveDayCategory()));
        parametersMap.put("leaveReqCategory", this.getAsString(this.getLeaveReqCategory()));
        parametersMap.put("showProject", this.getAsString(this.isShowProject()));
        parametersMap.put("accountType", this.getAsString(this.getAccountType()));
        parametersMap.put("showEvent", this.getAsString(this.isShowEvent()));
        parametersMap.put("showTasks", this.getAsString(this.isShowTasks()));
        parametersMap.put("showIssues", this.getAsString(this.isShowIssues()));
        parametersMap.put("showLeaveRequest", this.getAsString(this.isShowLeaveRequest()));
        parametersMap.put("showPA", this.getAsString(this.isShowPA()));
        parametersMap.put("showHolidays", this.getAsString(this.isShowHolidays()));
        parametersMap.put("issueStatusId", this.getAsString(this.getIssueStatusId()));
        parametersMap.put("issuePriorityId", this.getAsString(this.getIssuePriorityId()));
        parametersMap.put("cfColumnCode", this.getAsString(this.getCfColumnCode()));
        parametersMap.put("cfColumnValue", this.getAsString(this.getCfColumnValue()));
        parametersMap.put("groupByName", this.getAsString(this.getGroupByName()));
        parametersMap.put("searchType", this.getAsString(this.getSearchType()));
        parametersMap.put("statusValues", this.getAsString(this.getStatusValues()));
        parametersMap.put("accountID", this.getAsString(this.getAccountID()));
        parametersMap.put("allByFilter", this.getAsString(this.isAllByFilter()));
        parametersMap.put("clientName", this.getAsString(this.getClientName()));
        parametersMap.put("forCSVonly", this.getAsString(this.isForCSVonly()));
        parametersMap.put("asSelectItem", this.getAsString(this.isAsSelectItem()));
        parametersMap.put("doNotSearch", this.getAsString(this.isDoNotSearch()));
        parametersMap.put("facetFilterJSON", this.getAsString(this.getFacetFilterJson()));
        parametersMap.put("customFieldsShown", this.getAsString(this.isCustomFieldsShown()));
        parametersMap.put("messageID", this.getAsString(this.getMessageId()));
        parametersMap.put("caseID", this.getAsString(this.getCaseID()));
        parametersMap.put("messageStatus", this.getMessageStatus());
        parametersMap.put("relationId", this.getAsString(this.getRelationID()));
        parametersMap.put("relationType", this.getAsString(this.getRelationType()));
        parametersMap.put("relationToId", this.getAsString(this.getRelationToID()));
        parametersMap.put("relationName", this.getAsString(this.getRelationName()));

        parametersMap.put("interval", this.getAsString(this.getInterval()));
        parametersMap.put("intervalLimit", this.getAsString(this.getIntervalLimit()));
        parametersMap.put("ascending", this.getAsString(this.isAscending()));
        parametersMap.put("forExportOnly", this.getAsString(this.isForExportOnly()));
        parametersMap.put("listPanelToolJson", this.getAsString(this.getListPanelToolJson()));
        parametersMap.put("objectId", this.getAsString(this.getObjectId()));
        parametersMap.put("objectID", this.getAsString(this.getObjectId()));
        parametersMap.put("crmEntityId", this.getAsString(this.getCrmEntityId()));
        parametersMap.put("crmAccountId", this.getAsString(this.getCrmAccountId()));
        parametersMap.put("crmContactId", this.getAsString(this.getCrmContactId()));
        parametersMap.put("crmLeadId", this.getAsString(this.getCrmLeadId()));
        parametersMap.put("crmOppartunityId", this.getAsString(this.getCrmOppartunityId()));
        parametersMap.put("IDsOnly", this.getAsString(this.isIDsOnly()));
        parametersMap.put("isShowActive", this.getAsString(this.isShowActive()));
        parametersMap.put("isShowArchived", this.getAsString(this.isShowArchived()));
        parametersMap.put("hasOnlyClientAccess", this.getAsString(this.hasOnlyClientAccess()));
        parametersMap.put("accessEnabled", this.getAsString(this.isAccessEnabled()));
        parametersMap.put("campaignID", this.getAsString(this.getCampaignID()));
        parametersMap.put("scheduleCourseId", this.getAsString(this.getScheduledCourseID()));
        parametersMap.put("viewType", this.getAsString(this.getViewType()));
        parametersMap.put("startDate_nc", this.getAsString(this.getStartDateNC()));
        parametersMap.put("endDate_nc", this.getAsString(this.getEndDateNC()));
        parametersMap.put("employeeStatus", this.getAsString(this.getEmployeeStatusID()));
        parametersMap.put("position", this.getAsString(this.getPositionID()));
        parametersMap.put("brigada", this.getAsString(this.getBrigadaID()));
        parametersMap.put("isWageRate", this.getAsString(this.isWageRate()));
        parametersMap.put("isWithTax", this.getAsString(this.isWithTax()));
        parametersMap.put("statusID", this.getAsString(this.getStatusID()));
        parametersMap.put("statusCode", this.getAsString(this.getStatusCode()));
        parametersMap.put("categoryID", this.getAsString(this.getCategoryID()));
        parametersMap.put("accountTransactionStatus", this.getAsString(this.getAccountTransactionStatus()));
        parametersMap.put("recurrenceStatus", this.getAsString(this.getRecurrenceStatus()));
        parametersMap.put("agentID", this.getAsString(this.getAgentId()));
        parametersMap.put("reasonId", this.getAsString(this.getReasonID()));
        parametersMap.put("payrollBatchId", this.getAsString(this.getPayrollBatchID()));
        parametersMap.put("locationID", this.getAsString(this.getLocationId()));
        parametersMap.put("courseID", this.getAsString(this.getCourseID()));
        parametersMap.put("languageID", this.getAsString(this.getLanguageID()));
        parametersMap.put("dataType", this.getAsString(this.getDataType()));
        parametersMap.put("currencyID", this.getAsString(this.getCurrencyID()));
        parametersMap.put("contactClientId", this.getAsString(this.getContractClientId()));
        parametersMap.put("stepID", this.getAsString(this.getStepID()));

        parametersMap.put("monthName", this.getAsString(this.getMonthName()));
        parametersMap.put("monthWithYear", this.getAsString(this.getMonthWithYear()));
        parametersMap.put("day", this.getAsString(this.getDay()));
        parametersMap.put("year", this.getAsString(this.getYear()));
        parametersMap.put("selectedMonth", this.getAsString(this.getSelectedMonth()));
        parametersMap.put("countryCode", this.getAsString(this.getCountryCode()));
        parametersMap.put("isCorporate", this.getAsString(this.isCorporate()));
        parametersMap.put("positionIDs", this.getAsString(this.getPositionIDs()));
        parametersMap.put("brigadaIDs", this.getAsString(this.getBrigadaIDs()));
        parametersMap.put("noPosition", this.getAsString(this.getNoPosition()));
        parametersMap.put("isShowFilledCells", this.getAsString(this.isShowFilledCells()));
        parametersMap.put("checkNumber", this.getAsString(this.isCheckNumber()));
        parametersMap.put("showSubAccountT", this.getAsString(this.isShowSubAccountTransaction()));
        parametersMap.put("fromExcelPDF", this.getAsString(this.isFromExcelPDF()));
        parametersMap.put("isShortList", this.getAsString(this.isShortList()));
        parametersMap.put(ListingFilterParameter.IS_LANDSCAPE, this.getAsString(this.isLandscape()));
        parametersMap.put(ListingFilterParameter.PROPERTY_CODE, this.getAsString(this.getPropertyCode()));
        parametersMap.put("templateID", this.getAsString(this.getTemplateID()));
        parametersMap.put("CATEGORY", this.getAsString(this.getCategory()));
        parametersMap.put("payrunId", this.getAsString(this.getPayrunID()));
        parametersMap.put("paymentMethodId", this.getAsString(this.getPaymentMethodId()));
        parametersMap.put("isEssUser", this.getAsString(this.isEssUser()));
        parametersMap.put("fromAmount", this.getAsString(this.getFromAmount()));
        parametersMap.put("toAmount", this.getAsString(this.getToAmount()));
        parametersMap.put("relatedProject", this.getAsString(this.getRelatedProject()));
        parametersMap.put("journalID", this.getAsString(this.getJournalID()));
        parametersMap.put("forBank", this.getAsString(this.isForBank()));
        parametersMap.put("isZeroAvoided", this.getAsString(this.isZeroAvoided()));
        parametersMap.put("isExcludePrePayments", this.getAsString(this.isExcludePrePayments()));
        parametersMap.put("isGdn", this.getAsString(this.isGdn()));
        parametersMap.put("fromRegistrationDate", this.getAsString(this.getFromRegistrationDate()));
        parametersMap.put("toRegistrationDate", this.getAsString(this.getToRegistrationDate()));
        parametersMap.put("fromExpireDate", this.getAsString(this.getFromExpirationDate()));
        parametersMap.put("toExpireDate", this.getAsString(this.getToExpirationDate()));
        parametersMap.put("isPaid", this.getAsString(this.isPaid()));
        parametersMap.put("form", this.getAsString(this.getForm()));
        parametersMap.put("productId", this.getAsString(this.getProductId()));
        parametersMap.put("serialNumber", this.getAsString(this.getSerialNumber()));
        parametersMap.put("fromExpiryDate", this.getAsString(this.getFromExpiryDate()));
        parametersMap.put("toExpiryDate", this.getAsString(this.getToExpiryDate()));
        parametersMap.put("batchType", this.getAsString(this.getBatchHistoryType()));
        parametersMap.put("warehouseID", this.getAsString(this.getWarehouseId()));
        parametersMap.put("objectsIds", this.getAsString(this.getObjectsIds()));
        parametersMap.put("collapsed", this.getAsString(this.getCollapsed()));
        parametersMap.put("shownObjects", this.getAsString(this.getShownObjects()));
        parametersMap.put("levelOptionId", this.getAsString(this.getLevelOptionList()));
        parametersMap.put("levelOptionForSprvsId", this.getAsString(this.getLevelOptionListForSprvs()));
        parametersMap.put("isShowViewId", this.getAsString(this.isShowView()));
        parametersMap.put("showMembersId", this.getAsString(this.getShowMembersForOrgChart()));
        parametersMap.put("showAllSubMembersId", this.getAsString(this.getShowAllSubMembersForOrgChart()));
        parametersMap.put("departmentDoubleClickId", this.getAsString(this.getDepartmentDoubleClickId()));
        parametersMap.put("isHrms", getAsString(isHRMS()));
        parametersMap.put("isBasicPlusAllowancePaymentType", this.getAsString(this.isBasicPlusAllowancePaymentType()));
        parametersMap.put("isPayment", this.getAsString(this.isPayment()));
        parametersMap.put("propertyCode", this.getAsString(this.getPropertyCode()));
        parametersMap.put("supervisorId", this.getAsString(this.getSupervisorId()));
        parametersMap.put("isOrderByDepartment", this.getAsString(this.isOrderByDepartment()));
        parametersMap.put("isOrderByPosition", this.getAsString(this.isOrderByDepartment()));
        parametersMap.put(SHIFT_TYPE, this.getAsString(this.getShiftType()));
        return parametersMap;
    }

    public void setRequestParams(final HashMap<String, String> parametersMap) {
        for (final Map.Entry<String, String> entry : parametersMap.entrySet()) {

            switch (entry.getKey()) {
                case "backendUsersId":
                    if (entry.getValue() != null) {
                        try {
                            this.setBackendUsersId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "createdFrom":
                    if (entry.getValue() != null) {
                        try {
                            this.setCreatedFrom(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "bugPriorityId":
                    if (entry.getValue() != null) {
                        try {
                            this.setBugPriorityId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "bugStatusId":
                    if (entry.getValue() != null) {
                        try {
                            this.setBugStatusId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "bugAssigneeId":
                    if (entry.getValue() != null) {
                        try {
                            this.setBugAssigneeId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "clientId":
                    if (entry.getValue() != null) {
                        try {
                            this.setClientId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "contactClientId":
                    if (entry.getValue() != null) {
                        try {
                            this.setContractClientId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "caseID":
                    if (entry.getValue() != null) {
                        try {
                            this.setCaseID(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "departmentId":
                    if (entry.getValue() != null) {
                        try {
                            this.setDepartmentId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "employeeId":
                    if (entry.getValue() != null) {
                        try {
                            this.setEmployeeId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "issueId":
                    if (entry.getValue() != null) {
                        try {
                            this.setIssueId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "endDate":
                    if (entry.getValue() != null) {
                        try {
                            this.setEndDate(new Date(Long.parseLong(entry.getValue())));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "invoiceClientId":
                    if (entry.getValue() != null) {
                        try {
                            this.setInvoiceClientId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "invoiceStatusId":
                    if (entry.getValue() != null) {
                        try {
                            this.setInvoiceStatusId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "warehouseId":
                    if (entry.getValue() != null) {
                        try {
                            this.setWarehouseID(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "priceStartValue":
                    if (entry.getValue() != null) {
                        this.setPriceStartValue(entry.getValue());
                    }
                    break;

                case "projectId":
                    if (entry.getValue() != null) {
                        try {
                            this.setProjectId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "projectStatusId":
                    if (entry.getValue() != null) {
                        try {
                            this.setProjectStatusId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "quantityEndValue":
                    if (entry.getValue() != null) {
                        this.setQuantityEndValue(entry.getValue());
                    }
                    break;
                case "quantityStartValue":
                    if (entry.getValue() != null) {
                        this.setQuantityStartValue(entry.getValue());
                    }
                    break;
                case "searchKey":
                    if (entry.getValue() != null) {
                        this.setSearchKey(entry.getValue());
                    }
                    break;
                case "params":
                    if (entry.getValue() != null) {
                        this.setParams(entry.getValue());
                    }
                    break;
                case "roles":
                    if (entry.getValue() != null) {
                        this.setRoles(entry.getValue());
                    }
                    break;
                case "startDate":
                    if (entry.getValue() != null) {
                        try {
                            this.setStartDate(new Date(Long.parseLong(entry.getValue())));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "taskPriorityId":
                    if (entry.getValue() != null) {
                        try {
                            this.setTaskPriorityId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "taskStatusId":
                    if (entry.getValue() != null) {
                        try {
                            this.setTaskStatusId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "taskID":
                    if (entry.getValue() != null) {
                        try {
                            this.setTaskID(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "timeSheetApprovalSessionStatusId":
                    if (entry.getValue() != null) {
                        try {
                            this.setTimeSheetApprovalSessionStatusId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "type":
                    if (entry.getValue() != null) {
                        try {
                            this.setType(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "unitMeasurement":
                    if (entry.getValue() != null) {
                        try {
                            this.setUnitMeasurementId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "viewAsId":
                    if (entry.getValue() != null) {
                        try {
                            this.setViewAsId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "actualDue":
                    if (entry.getValue() != null) {
                        try {
                            this.setActualDue(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "actualStart":
                    if (entry.getValue() != null) {
                        try {
                            this.setActualStart(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "plannedDue":
                    if (entry.getValue() != null) {
                        try {
                            this.setPlannedDue(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "plannedStart":
                    if (entry.getValue() != null) {
                        try {
                            this.setPlannedStart(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "featured":
                    if (entry.getValue() != null) {
                        try {
                            this.setFeatured(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "priceEndValue":
                    if (entry.getValue() != null) {
                        this.setPriceEndValue(entry.getValue());
                    }
                    break;
                case "start":
                    if (entry.getValue() != null) {
                        try {
                            this.setStart(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "limit":
                    if (entry.getValue() != null) {
                        try {
                            this.setLimit(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "currentPage":
                    if (entry.getValue() != null) {
                        try {
                            this.setCurrentPage(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "sortField":
                    if (entry.getValue() != null) {
                        this.setSortField(entry.getValue());
                    }
                    break;
                case "sortDir":
                    if (entry.getValue() != null) {
                        try {
                            this.setSortDir(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "invoiceType":
                    if (entry.getValue() != null) {
                        this.setInvoiceType(entry.getValue());
                    }
                    break;
                case "dueDate":
                    if (entry.getValue() != null) {
                        try {
                            this.setDueDate(new Date(Long.parseLong(entry.getValue())));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "fromDate":
                    if (entry.getValue() != null) {
                        try {
                            this.setFromDate(Long.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "toDate":
                    if (entry.getValue() != null) {
                        try {
                            this.setToDate(Long.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "showBudget":
                    if (entry.getValue() != null) {
                        try {
                            this.setShowBudget(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "summaryView":
                    if (entry.getValue() != null) {
                        try {
                            this.setShowSummaryView(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "showYTD":
                    if (entry.getValue() != null) {
                        try {
                            this.setShowYTD(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "showInBase":
                    if (entry.getValue() != null) {
                        try {
                            this.setShowInBase(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "sickRequestStartDate":
                    if (entry.getValue() != null) {
                        try {
                            this.setSickRequestStartDate(new Date(Long.parseLong(entry.getValue())));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "sickRequestEndDate":
                    if (entry.getValue() != null) {
                        try {
                            this.setSickRequestEndDate(new Date(Long.parseLong(entry.getValue())));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "departmentIds":
                    if (entry.getValue() != null) {
                        this.setDepartmentIds(entry.getValue());
                    }
                    break;
                case "reasonIds":
                    if (entry.getValue() != null) {
                        this.setReasonIds(entry.getValue());
                    }
                    break;
                case "projectIds":
                    if (entry.getValue() != null) {
                        this.setProjectIds(entry.getValue());
                    }
                    break;
                case "leaveDayCategory":
                    if (entry.getValue() != null) {
                        try {
                            this.setLeaveDayCategory(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "leaveReqCategory":
                    if (entry.getValue() != null) {
                        try {
                            this.setLeaveReqCategory(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "showProject":
                    if (entry.getValue() != null) {
                        try {
                            this.setShowProject(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "accountType":
                    if (entry.getValue() != null) {
                        this.setAccountType(entry.getValue());
                    }
                    break;
                case "showEvent":
                    if (entry.getValue() != null) {
                        try {
                            this.setShowEvent(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "showTasks":
                    if (entry.getValue() != null) {
                        try {
                            this.setShowTasks(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "showIssues":
                    if (entry.getValue() != null) {
                        try {
                            this.setShowIssues(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "showLeaveRequest":
                    if (entry.getValue() != null) {
                        try {
                            this.setShowLeaveRequest(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "showPA":
                    if (entry.getValue() != null) {
                        try {
                            this.setShowPA(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "showHolidays":
                    if (entry.getValue() != null) {
                        try {
                            this.setShowHolidays(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "issueStatusId":
                    if (entry.getValue() != null) {
                        try {
                            this.setIssueStatusId((Integer.valueOf(entry.getValue())));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "issuePriorityId":
                    if (entry.getValue() != null) {
                        try {
                            this.setIssuePriorityId(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "cfColumnCode":
                    if (entry.getValue() != null) {
                        this.setCfColumnCode(entry.getValue());
                    }
                    break;
                case "cfColumnValue":
                    if (entry.getValue() != null) {
                        this.setCfColumnValue(entry.getValue());
                    }
                    break;
                case "groupByName":
                    if (entry.getValue() != null) {
                        this.setGroupByName(entry.getValue());
                    }
                    break;
                case "searchType":
                    if (entry.getValue() != null) {
                        try {
                            this.setSearchType(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "statusValues":
                    if (entry.getValue() != null) {
                        this.setStatusValues(entry.getValue());
                    }
                    break;
                case "allByFilter":
                    if (entry.getValue() != null) {
                        this.setAllByFilter(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "clientName":
                    if (entry.getValue() != null) {
                        this.setClientName(entry.getValue());
                    }
                    break;
                case "forCSVonly":
                    if (entry.getValue() != null) {
                        this.setForCSVonly(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "asSelectItem":
                    if (entry.getValue() != null) {
                        this.setAsSelectItem(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "doNotSearch":
                    if (entry.getValue() != null) {
                        this.setDoNotSearch(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "accountID":
                    if (entry.getValue() != null) {
                        try {
                            this.setAccountID(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "customFieldsShown":
                    if (entry.getValue() != null) {
                        this.setCustomFieldsShown(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "ascending":
                    if (entry.getValue() != null) {
                        try {
                            this.setAscending(Boolean.valueOf(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "forExportOnly":
                    if (entry.getValue() != null) {
                        try {
                            this.setForExportOnly(Boolean.valueOf(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "listPanelToolJson":
                    if (entry.getValue() != null) {
                        this.setListPanelToolJson(entry.getValue());
                    }
                    break;
                case "objectId":
                case "objectID":
                    if (entry.getValue() != null && entry.getValue().matches(Constants.REGEX_INTEGER_POSITIVE)) {
                        this.setObjectId(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "crmEntityId":
                    if (entry.getValue() != null && entry.getValue().matches(Constants.REGEX_INTEGER_POSITIVE)) {
                        this.setCrmEntityId(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "crmAccountId":
                    if (entry.getValue() != null && entry.getValue().matches(Constants.REGEX_INTEGER_POSITIVE)) {
                        this.setCrmAccountId(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "salequote":
                    if (entry.getValue() != null && entry.getValue().matches(Constants.REGEX_INTEGER_POSITIVE)) {
                        this.setQuoteId(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "purchaseorder":
                    if (entry.getValue() != null && entry.getValue().matches(Constants.REGEX_INTEGER_POSITIVE)) {
                        this.setQuoteId(Integer.valueOf(entry.getValue()));
                    }
                    break;

                case "crmContactId":
                    if (entry.getValue() != null && entry.getValue().matches(Constants.REGEX_INTEGER_POSITIVE)) {
                        this.setCrmContactId(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "crmLeadId":
                    if (entry.getValue() != null && entry.getValue().matches(Constants.REGEX_INTEGER_POSITIVE)) {
                        this.setCrmLeadId(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "crmOppartunityId":
                    if (entry.getValue() != null && entry.getValue().matches(Constants.REGEX_INTEGER_POSITIVE)) {
                        this.setCrmOppartunityId(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "IDsOnly":
                    if (entry.getValue() != null && entry.getValue().matches(Constants.REGEX_INTEGER_POSITIVE)) {
                        this.setIDsOnly(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "isShowActive":
                    if (entry.getValue() != null || entry.getValue().matches(Constants.REGEX_INTEGER_POSITIVE)) {
                        this.setShowActive(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "isShowArchived":
                    if (entry.getValue() != null) {
                        this.setShowArchived(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "scheduleCourseId":
                    if (entry.getValue() != null && entry.getValue().matches(Constants.REGEX_INTEGER_POSITIVE)) {
                        this.setScheduledCourseID(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "viewType":
                    if (entry.getValue() != null) {
                        this.setViewType(entry.getValue());
                    }
                    break;
                case "hasOnlyClientAccess":
                    if (entry.getValue() != null) {
                        this.setHasOnlyClientAccess(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "facetFilterJSON":
                    if (entry.getValue() != null) {
                        this.setFacetFilterJson(entry.getValue());
                    }
                    break;
                case "messageID":
                    if (entry.getValue() != null) {
                        this.setMessageId(Integer.parseInt(entry.getValue()));
                    }
                    break;
                case "messageStatus":
                    if (entry.getValue() != null) {
                        this.setMessageStatus(entry.getValue());
                    }
                    break;
                case "relationId":
                    if (entry.getValue() != null) {
                        this.setRelationID(Integer.parseInt(entry.getValue()));
                    }
                    break;
                case "relationType":
                    if (entry.getValue() != null) {
                        this.setRelationType(entry.getValue());
                    }
                    break;
                case "relationName":
                    if (entry.getValue() != null) {
                        this.setRelationName(entry.getValue());
                    }
                    break;
                case "countryCode":
                    if (entry.getValue() != null) {
                        this.setCountryCode(entry.getValue());
                    }
                    break;
                case "isCorporate":
                    if (entry.getValue() != null) {
                        this.setCorporate(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "campaignID":
                    if (entry.getValue() != null) {
                        this.setCampaignID(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "startDate_nc":
                    if (entry.getValue() != null) {
                        this.setStartDateNC(entry.getValue());
                    }
                    break;
                case "endDate_nc":
                    if (entry.getValue() != null) {
                        this.setEndDateNC(entry.getValue());
                    }
                    break;
                case "employeeStatus":
                    if (entry.getValue() != null) {
                        try {
                            this.setEmployeeStatusID(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "agentID":
                    if (entry.getValue() != null) {
                        try {
                            this.setAgentID(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "stepID":
                    if (entry.getValue() != null) {
                        try {
                            this.setStepID(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "position":
                    if (entry.getValue() != null) {
                        try {
                            this.setPositionID(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "brigada":
                    if (entry.getValue() != null) {
                        try {
                            this.setBrigadaID(Integer.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "isWageRate":
                    if (entry.getValue() != null) {
                        try {
                            this.setWageRate(Boolean.valueOf(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "isWithTax":
                    if (entry.getValue() != null) {
                        try {
                            this.setWithTax(Boolean.valueOf(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "categoryID":
                    if (entry.getValue() != null) {
                        this.setCategoryID(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "statusID":
                    if (entry.getValue() != null) {
                        this.setStatusID(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "statusCode":
                    if (entry.getValue() != null) {
                        this.setStatusCode(entry.getValue());
                    }
                    break;
                case "reasonId":
                    if (entry.getValue() != null) {
                        this.setReasonID(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "payrollBatchId":
                    if (entry.getValue() != null) {
                        this.setPayrollBatchID(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "locationID":
                    if (entry.getValue() != null) {
                        this.setLocationId(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "supervisorId":
                    if (entry.getValue() != null) {
                        this.setSupervisorId(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "courseID":
                    if (entry.getValue() != null) {
                        this.setCourseID(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "languageID":
                    if (entry.getValue() != null) {
                        this.setLanguageID(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "currencyID":
                    if (entry.getValue() != null) {
                        this.setCurrencyID(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "selectedMonth":
                    if (entry.getValue() != null) {
                        this.setSelectedMonth(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "year":
                    if (entry.getValue() != null) {
                        this.setYear(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "positionIDs":
                    if (entry.getValue() != null && !entry.getValue().equals("")) {
                        this.setPositionIDs(entry.getValue());
                    }
                    break;
                case "brigadaIDs":
                    if (entry.getValue() != null && !entry.getValue().equals("")) {
                        this.setBrigadaIDs(entry.getValue());
                    }
                    break;
                case "noPosition":
                    if (entry.getValue() != null) {
                        this.setNoPosition(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "isShowFilledCells":
                    if (entry.getValue() != null) {
                        this.setShowFilledCells(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "dataType":
                    if (entry.getValue() != null) {
                        this.setDataType(entry.getValue());
                    }
                    break;
                case "checkNumber":
                    if (entry.getValue() != null) {
                        try {
                            this.setCheckNumber(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "showSubAccountT":
                    if (entry.getValue() != null) {
                        this.setShowSubAccountTransaction(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "fromExcelPDF":
                    if (entry.getValue() != null) {
                        this.setFromExcelPDF(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "isShortList":
                    if (entry.getValue() != null) {
                        this.setShortList(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case ListingFilterParameter.IS_LANDSCAPE:
                    if (entry.getValue() != null) {
                        this.setLandscape(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case ListingFilterParameter.PROPERTY_CODE:
                    if (entry.getValue() != null) {
                        this.setPropertyCode(entry.getValue());
                    }
                    break;
                case "templateID":
                    if (entry.getValue() != null) {
                        this.setTemplateID(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "workstreamID":
                    if (entry.getValue() != null) {
                        this.setWorkstreamID(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "CATEGORY":
                    if (entry.getValue() != null) {
                        this.setCategory(entry.getValue());
                    }
                    break;
                case "payrunId":
                    if (entry.getValue() != null) {
                        this.setPayrunID(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "paymentMethodId":
                    if (entry.getValue() != null) {
                        this.setPaymentMethodId(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "monthName":
                    if (entry.getValue() != null) {
                        this.setMonthName(entry.getValue());
                    }
                    break;
                case "isEssUser":
                    if (entry.getValue() != null) {
                        this.setEssUser(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "day":
                    if (entry.getValue() != null) {
                        this.setDay(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "fromAmount":
                    if (entry.getValue() != null) {
                        try {
                            this.setFromAmount(Double.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "toAmount":
                    if (entry.getValue() != null) {
                        try {
                            this.setToAmount(Double.valueOf(entry.getValue()));
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "relatedProject":
                    if (entry.getValue() != null) {
                        this.setRelatedProject(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "journalID":
                    if (entry.getValue() != null) {
                        this.setJournalID(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "forBank":
                    if (entry.getValue() != null) {
                        this.setForBank(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "isZeroAvoided":
                    if (entry.getValue() != null) {
                        this.setAvoidZero(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "isExcludePrePayments":
                    if (entry.getValue() != null) {
                        this.setExcludePrePayments(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "isGdn":
                    if (entry.getValue() != null) {
                        this.setIsGdn(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "fromRegistrationDate":
                    if (entry.getValue() != null) {
                        try {
                            this.setFromRegistrationDate(new Date(Long.parseLong(entry.getValue())));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "toRegistrationDate":
                    if (entry.getValue() != null) {
                        try {
                            this.setToRegistrationDate(new Date(Long.parseLong(entry.getValue())));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "fromExpireDate":
                    if (entry.getValue() != null) {
                        try {
                            this.setFromExpirationDate(new Date(Long.parseLong(entry.getValue())));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "toExpireDate":
                    if (entry.getValue() != null) {
                        try {
                            this.setToExpirationDate(new Date(Long.parseLong(entry.getValue())));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "isPaid":
                    if (entry.getValue() != null) {
                        this.setPaid(Boolean.valueOf(entry.getValue()));
                    }
                    break;
                case "form":
                    if (entry.getValue() != null) {
                        this.setForm(entry.getValue());
                    }
                    break;
                case "productId":
                    if (entry.getValue() != null) {
                        this.setProductId(Integer.valueOf(entry.getValue()));
                    }
                    break;
                case "serialNumber":
                    if (entry.getValue() != null) {
                        try {
                            this.setSerialNumber(entry.getValue());
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "fromExpiryDate":
                    if (entry.getValue() != null) {
                        try {
                            this.setFromExpiryDate(new Date(Long.parseLong(entry.getValue())));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "toExpiryDate":
                    if (entry.getValue() != null) {
                        try {
                            this.setToExpiryDate(new Date(Long.parseLong(entry.getValue())));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "batchType":
                    if (entry.getValue() != null) {
                        try {
                            this.setBatchHistoryType(entry.getValue());
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "warehouseID":
                    if (entry.getValue() != null) {
                        try {
                            this.setWarehouseId(Integer.parseInt(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "objectsIds":
                    if (entry.getValue() != null) {
                        try {
                            this.setObjectsIds(entry.getValue());
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "collapsed":
                    if (entry.getValue() != null) {
                        try {
                            this.setCollapsed(entry.getValue());
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "shownObjects":
                    if (entry.getValue() != null) {
                        try {
                            this.setShownObjects(entry.getValue());
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "propertyCode":
                    if (entry.getValue() != null) {
                        try {
                            this.setPropertyCode(entry.getValue());
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "levelOptionId":
                    if (entry.getValue() != null) {
                        try {
                            this.setLevelOptionList(Integer.valueOf(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "levelOptionForSprvsId":
                    if (entry.getValue() != null) {
                        try {
                            this.setLevelOptionListForSprvs(Integer.valueOf(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "isShowViewId":
                    if (entry.getValue() != null) {
                        try {
                            this.setShowView(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "showMembersId":
                    if (entry.getValue() != null) {
                        try {
                            this.setShowMembersForOrgChart(Integer.valueOf(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "showAllSubMembersId":
                    if (entry.getValue() != null) {
                        try {
                            this.setShowAllSubMembersForOrgChart(Integer.valueOf(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "departmentDoubleClickId":
                    if (entry.getValue() != null) {
                        try {
                            this.setDepartmentDoubleClickId(Integer.valueOf(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "showEmployeeId":
                    if (entry.getValue() != null) {
                        try {
                            this.setShowEmployee(Boolean.parseBoolean(entry.getValue()));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "isHrms":
                    if (entry.getValue() != null) {
                        try {
                            setHRMS(Boolean.parseBoolean(entry.getValue()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "isOrderByDepartment":
                    if (entry.getValue() != null) {
                        try {
                            setOrderByDepartment(Boolean.parseBoolean(entry.getValue()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "isOrderByPosition":
                    if (entry.getValue() != null) {
                        try {
                            setOrderByPosition(Boolean.parseBoolean(entry.getValue()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case SHIFT_TYPE:
                    if (entry.getValue() != null) {
                        try {
                            setShiftType(entry.getValue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void setShiftType(String value) {
        this.addString(SHIFT_TYPE, value);
    }

    public String getShiftType() {
        return this.getString(SHIFT_TYPE);
    }

    public void setAllByFilter(final Boolean allByFilter) {
        this.addBoolean(ListingFilterParameter.ALL_FILTER, allByFilter);
    }

    public Boolean isAllByFilter() {
        return this.getBoolean(ListingFilterParameter.ALL_FILTER) != null && this.getBool(ListingFilterParameter.ALL_FILTER);
    }

    public boolean isAllEmployees() {
        return this.getBoolean(ListingFilterParameter.ALL_EMPLOYEES) != null && this.getBool(ListingFilterParameter.ALL_EMPLOYEES);
    }

    public void setAllEmployees(final boolean allEmployees) {
        this.addBoolean(ListingFilterParameter.ALL_EMPLOYEES, allEmployees);
    }

    public ListLoadConfig asConfig(final int... tmp) {
        final ListLoadConfig config = new ListLoadConfig();
        config.setSortField(this.getSortField());
        config.setStart(this.getStart());
        config.setLimit(this.getLimit());
        config.setSortDir(this.isAscending() ? 1 : 2);
        return config;
    }

    public boolean isIDsOnly() {
        return this.getBool(ListingFilterParameter.IDS_ONLY);
    }

    public void setIDsOnly(final boolean IDsOnly) {
        this.addBool(ListingFilterParameter.IDS_ONLY, IDsOnly);
    }

    public Date getStartDate() {
        return this.getDate(ListingFilterParameter.START_DATE);
    }

    public void setStartDate(final Date startDate) {
        this.addDate(ListingFilterParameter.START_DATE, startDate, true);
    }

    public Date getNonConvertibleStartDate() {
        return this.getNonConvertibleDate(ListingFilterParameter.START_DATE);
    }

    public void setStartDateWithoutOffset(final Date startDate) {
        this.addDate(ListingFilterParameter.START_DATE, startDate, false);
    }

    public Date getNonConvertibleEndDate() {
        return this.getNonConvertibleDate(ListingFilterParameter.END_DATE);
    }

    public Date getDate() {
        return this.getDate(ListingFilterParameter.DATE);
    }

    public void setDate(final Date date) {
        this.addDate(ListingFilterParameter.DATE, date, true);
    }

    public void setEndDateWithoutOffset(final Date date) {
        this.addDate(ListingFilterParameter.END_DATE, date, false);
    }

    public Date getEndDate() {
        return this.getDate(ListingFilterParameter.END_DATE);
    }

    public void setEndDate(final Date endDate) {
        this.addDate(ListingFilterParameter.END_DATE, endDate, true);
    }

    public String getRelationType() {
        return this.getString(ListingFilterParameter.RELATION_TYPE);
    }

    public void setRelationType(final String relationType) {
        this.addString(ListingFilterParameter.RELATION_TYPE, relationType);
    }

    public String getExcludedType() {
        return this.getString(ListingFilterParameter.EXCLUDED_TYPE);
    }

    public void setExcludedType(final String exlcudedType) {
        this.addString(ListingFilterParameter.EXCLUDED_TYPE, exlcudedType);
    }

    public String getEmailType() {
        return this.getString(ListingFilterParameter.EMAIL_TYPE);
    }

    public void setEmailType(final String type) {
        this.addString(ListingFilterParameter.EMAIL_TYPE, type);
    }

    public Integer getRelationID() {
        return this.getInteger(ListingFilterParameter.RELATION);
    }

    public void setRelationID(final Integer relationID) {
        this.addInteger(ListingFilterParameter.RELATION, relationID);
    }

    public Integer getBankID() {
        return this.getInteger(ListingFilterParameter.BANK_NAME);
    }

    public void setBankID(final Integer bankID) {
        this.addInteger(ListingFilterParameter.BANK_NAME, bankID);
    }

    public Integer getProviderID() {
        return this.getInteger(ListingFilterParameter.PROVIDER_ID);
    }

    public void setProviderID(final Integer providerID) {
        this.addInteger(ListingFilterParameter.PROVIDER_ID, providerID);
    }

    public Integer getWebFormID() {
        return this.getInteger(ListingFilterParameter.WEB_FORM_ID);
    }

    public void setWebFormID(final Integer webFormID) {
        this.addInteger(ListingFilterParameter.WEB_FORM_ID, webFormID);
    }

    public Integer getRelationToID() {
        return this.getInteger(ListingFilterParameter.RELATION_TO);
    }

    public void setRelationToID(final Integer relationToID) {
        this.addInteger(ListingFilterParameter.RELATION_TO, relationToID);
    }

    public Integer getFolderId() {
        return this.getInteger(ListingFilterParameter.FOLDER);
    }

    public void setFolderId(final Integer folderId) {
        this.addInteger(ListingFilterParameter.FOLDER, folderId);
    }

    public Integer getFolderType() {
        return this.getInteger(ListingFilterParameter.FOLDER_TYPE);
    }

    public void setFolderType(final Integer folderType) {
        this.addInteger(ListingFilterParameter.FOLDER_TYPE, folderType);
    }

    public boolean isTrashResource() {
        return this.getBool(ListingFilterParameter.TRASH_RESOURCE);
    }

    public void setTrashResource(final boolean trashResource) {
        this.addBool(ListingFilterParameter.TRASH_RESOURCE, trashResource);
    }

    public boolean isOtherResource() {
        return this.getBool(ListingFilterParameter.OTHER_RESOURCE);
    }

    public void setOtherResource(final boolean otherResource) {
        this.addBool(ListingFilterParameter.OTHER_RESOURCE, otherResource);
    }

    public boolean isAllFilesResource() {
        return this.getBool(ListingFilterParameter.ALL_FILES);
    }

    public void setAllFilesResource(final boolean allFilesResource) {
        this.addBool(ListingFilterParameter.ALL_FILES, allFilesResource);
    }

    public boolean isOtherSharedResource() {
        return this.getBool(ListingFilterParameter.OTHER_SHARED_RESOURCE);
    }

    public void setOtherSharedResource(final boolean otherSharedResource) {
        this.addBool(ListingFilterParameter.OTHER_SHARED_RESOURCE, otherSharedResource);
    }

    public boolean isSharedResource() {
        return this.getBool(ListingFilterParameter.SHARED_RESOURCE);
    }

    public void setSharedResource(final boolean sharedResource) {
        this.addBool(ListingFilterParameter.SHARED_RESOURCE, sharedResource);
    }

    public boolean isDeleted() {
        return this.getBool(ListingFilterParameter.DELETED);
    }

    public void setDeleted(final boolean deleted) {
        this.addBool(ListingFilterParameter.DELETED, deleted);
    }

    public boolean isCorporate() {
        return this.getBool(ListingFilterParameter.CORPORATE);
    }

    public void setCorporate(final boolean corporate) {
        this.addBool(ListingFilterParameter.CORPORATE, corporate);
    }

    public String getEntityName() {
        return this.getString(ListingFilterParameter.ENTITY_NAME);
    }

    public void setEntityName(final String entityName) {
        this.addString(ListingFilterParameter.ENTITY_NAME, entityName);
    }

    public String getColumnCode() {
        return this.getString(ListingFilterParameter.COLUMN_CODE);
    }

    public void setColumnCode(final String columnCode) {
        this.addString(ListingFilterParameter.COLUMN_CODE, columnCode);
    }

    public Boolean getShowInListing() {
        return this.getBoolean(ListingFilterParameter.SHOW_IN_LISTING);
    }

    public void setShowInListing(final Boolean showInListing) {
        this.addBoolean(ListingFilterParameter.SHOW_IN_LISTING, showInListing);
    }

    public Boolean getShowInFilterGrouping() {
        return this.getBoolean(ListingFilterParameter.SHOW_IN_FILTER_GROUPING);
    }

    public void setShowInFilterGrouping(final Boolean showInFilterGrouping) {
        this.addBoolean(ListingFilterParameter.SHOW_IN_FILTER_GROUPING, showInFilterGrouping);
    }

    public Integer getCompanyID() {
        return this.getInteger(ListingFilterParameter.COMPANY);
    }

    public void setCompanyID(final Integer companyId) {
        this.addInteger(ListingFilterParameter.COMPANY, companyId);
    }

    public String getFolderName() {
        return this.getString(ListingFilterParameter.FOLDER_NAME);
    }

    public void setFolderName(final String folderName) {
        this.addString(ListingFilterParameter.FOLDER_NAME, folderName);
    }

    public Integer getEmailFolderID() {
        return this.getInteger(ListingFilterParameter.EMAIL_FOLDER_ID);
    }

    public void setEmailFolderID(final Integer folderID) {
        this.addInteger(ListingFilterParameter.EMAIL_FOLDER_ID, folderID);
    }

    public Integer getCountryId() {
        return this.getInteger(ListingFilterParameter.COUNTRY);
    }

    public void setCountryId(final Integer countryId) {
        this.addInteger(ListingFilterParameter.COUNTRY, countryId);
    }

    public String getStatusValues() {
        return this.getString(ListingFilterParameter.STATUS_VALUES);
    }

    ///////////////////////////////////////////////

    public void setStatusValues(final String statusValues) {
        this.addString(ListingFilterParameter.STATUS_VALUES, statusValues);
    }

    public Integer getCampaignID() {
        return this.getInteger(ListingFilterParameter.CAMPAIGN);
    }

    public void setCampaignID(final Integer campaignID) {
        this.addInteger(ListingFilterParameter.CAMPAIGN, campaignID);
    }

    public Integer getTimeSlotID() {
        return this.getInteger(ListingFilterParameter.TIMESLOT_ID);
    }

    public void setTimeSlotID(final Integer timeSlotID) {
        this.addInteger(ListingFilterParameter.TIMESLOT_ID, timeSlotID);
    }

    public Integer getGroupById() {
        return this.getInteger(ListingFilterParameter.GROUP_BY_ID);
    }

    public void setGroupById(final Integer groupById) {
        this.addInteger(ListingFilterParameter.GROUP_BY_ID, groupById);
    }

    public String getGroupByName() {
        return this.getString(ListingFilterParameter.GROUP_BY_NAME);
    }

    public void setGroupByName(final String groupByName) {
        this.addString(ListingFilterParameter.GROUP_BY_NAME, groupByName);
    }

    public boolean isCreditNote() {
        return this.getBool(ListingFilterParameter.IS_CREDIT_NOTE);
    }

    public void setCreditNote(final boolean isCreditNote) {
        this.addBool(ListingFilterParameter.IS_CREDIT_NOTE, isCreditNote);
    }

    public boolean isPlannedStart() {
        return this.getBool(ListingFilterParameter.PLANNED_START);
    }

    public void setPlannedStart(final boolean plannedStart) {
        this.addBool(ListingFilterParameter.PLANNED_START, plannedStart);
    }

    private static final String TIMESLOT_ID = "TIMESLOT_ID";
    private static final String GROUP_BY_ID = "GROUP_BY_ID";
    private static final String GROUP_BY_NAME = "GROUP_BY_NAME";
    private static final String PLANNED_START = "PLANNED_START";
    private static final String PLANNED_DUE = "PLANNED_DUE";
    private static final String ACTUAL_START = "ACTUAL_START";

    public boolean isPlannedDue() {
        return this.getBool(ListingFilterParameter.PLANNED_START);
    }

    public void setPlannedDue(final boolean plannedDue) {
        this.addBool(ListingFilterParameter.PLANNED_DUE, plannedDue);
    }

    public boolean setPlannedDue() {
        return this.getBool(ListingFilterParameter.PLANNED_DUE);
    }

    public boolean isActualStart() {
        return this.getBool(ListingFilterParameter.ACTUAL_START);
    }

    public void setActualStart(final boolean actualStart) {
        this.addBool(ListingFilterParameter.ACTUAL_START, actualStart);
    }

    public boolean isActualDue() {
        return this.getBool(ListingFilterParameter.ACTUAL_DUE);
    }

    public void setActualDue(final boolean actualDue) {
        this.addBool(ListingFilterParameter.ACTUAL_DUE, actualDue);
    }

    public Integer getTaskStatusId() {
        return this.getInteger(ListingFilterParameter.TASK_STATUS);
    }

    public void setTaskStatusId(final Integer taskStatusId) {
        this.addInteger(ListingFilterParameter.TASK_STATUS, taskStatusId);
    }

    public Integer getTaskID() {
        return this.getInteger(ListingFilterParameter.TASK_ID);
    }

    public void setTaskID(final Integer taskID) {
        this.addInteger(ListingFilterParameter.TASK_ID, taskID);
    }

    public String getIssueIDs() {
        return this.getString(ListingFilterParameter.ISSUE_IDS);
    }

    public void setIssueIDs(final String issueIDs) {
        this.addString(ListingFilterParameter.ISSUE_IDS, issueIDs);
    }

    public Integer getReportedByID() {
        return this.getInteger(ListingFilterParameter.REPORTED_BY_ID);
    }

    public void setReportedByID(final Integer reportedByID) {
        this.addInteger(ListingFilterParameter.REPORTED_BY_ID, reportedByID);
    }

    public Integer getResolverID() {
        return this.getInteger(ListingFilterParameter.RESOLVER_ID);
    }

    public void setResolverID(final Integer resolverID) {
        this.addInteger(ListingFilterParameter.RESOLVER_ID, resolverID);
    }

    public Integer getRoleID() {
        return this.getInteger(ListingFilterParameter.ROLE_ID);
    }

    public void setRoleID(final Integer roleID) {
        this.addInteger(ListingFilterParameter.ROLE_ID, roleID);
    }

    public Integer getMonthId() {
        return this.getInteger(ListingFilterParameter.MONTH_ID);
    }

    public void setMonthId(final Integer monthId) {
        this.addInteger(ListingFilterParameter.MONTH_ID, monthId);
    }

    public String getMonthName() {
        return this.getString(ListingFilterParameter.MONTH_NAME);
    }

    public void setMonthName(final String monthName) {
        this.addString(ListingFilterParameter.MONTH_NAME, monthName);
    }

    public String getMonthWithYear() {
        return this.getString(ListingFilterParameter.MONTH_WITH_YEAR);
    }

    public void setMonthWithYear(final String monthWithYear) {
        this.addString(ListingFilterParameter.MONTH_WITH_YEAR, monthWithYear);
    }

    public Integer getDay() {
        return this.getInteger(ListingFilterParameter.DAY);
    }

    public void setDay(final Integer day) {
        this.addInteger(ListingFilterParameter.DAY, day);
    }

    public Integer getTimeSheetApprovalSessionStatusId() {
        return this.getInteger(ListingFilterParameter.TIMESHEET_APPROVAL_SESSION_STATUS_ID);
    }

    public void setTimeSheetApprovalSessionStatusId(final Integer timeSheetApprovalSessionStatusId) {
        this.addInteger(ListingFilterParameter.TIMESHEET_APPROVAL_SESSION_STATUS_ID, timeSheetApprovalSessionStatusId);
    }

    public Integer getClientContactId() {
        return this.getInteger(ListingFilterParameter.CLIENT_CONTACT_ID);
    }

    public void setClientContactId(final Integer clientContactId) {
        this.addInteger(ListingFilterParameter.CLIENT_CONTACT_ID, clientContactId);
    }

    public Integer getContactType() {
        return this.getInteger(ListingFilterParameter.CONTACT_TYPE);
    }

    public void setContactType(final Integer contactType) {
        this.addInteger(ListingFilterParameter.CONTACT_TYPE, contactType);
    }

    public Integer getSupplierContactId() {
        return this.getInteger(ListingFilterParameter.SUPPLIER_CONTACT_ID);
    }

    public void setSupplierContactId(final Integer supplierContactId) {
        this.addInteger(ListingFilterParameter.SUPPLIER_CONTACT_ID, supplierContactId);
    }

    public Integer getCaseID() {
        return this.getInteger(ListingFilterParameter.CRM_CASE);
    }

    public void setCaseID(final Integer caseID) {
        this.addInteger(ListingFilterParameter.CRM_CASE, caseID);
    }

    public String getEmail() {
        return this.getString(ListingFilterParameter.EMAIL);
    }

    public void setEmail(final String email) {
        this.addString(ListingFilterParameter.EMAIL, email);
    }

    public Integer getIssueId() {
        return this.getInteger(ListingFilterParameter.ISSUE_ID);
    }

    public void setIssueId(final Integer issueId) {
        this.addInteger(ListingFilterParameter.ISSUE_ID, issueId);
    }

    public Date getDueDate() {
        return this.getDate(ListingFilterParameter.DUE_DATE);
    }

    public void setDueDate(final Date dueDate) {
        this.addDate(ListingFilterParameter.DUE_DATE, dueDate, true);
    }

    public Integer getTaskPriorityId() {
        return this.getInteger(ListingFilterParameter.TASK_PRIORITY);
    }

    public void setTaskPriorityId(final Integer taskPriorityId) {
        this.addInteger(ListingFilterParameter.TASK_PRIORITY, taskPriorityId);
    }

    public Integer getPriorityID() {
        return this.getInteger(ListingFilterParameter.PRIORITY);
    }

    public void setPriorityID(final Integer priorityID) {
        this.addInteger(ListingFilterParameter.PRIORITY, priorityID);
    }

    public Integer getReasonID() {
        return this.getInteger(ListingFilterParameter.REASON);
    }

    public void setReasonID(final Integer reasonID) {
        this.addInteger(ListingFilterParameter.REASON, reasonID);
    }

    public Integer getType() {
        return this.getInteger(ListingFilterParameter.TYPE);
    }

    public void setType(final Integer type) {
        this.addInteger(ListingFilterParameter.TYPE, type);
    }

    public String getQuantityStartValue() {
        return this.getString(ListingFilterParameter.QUANTITY_START_VALUE);
    }

    public void setQuantityStartValue(final String quantityStartValue) {
        this.addString(ListingFilterParameter.QUANTITY_START_VALUE, quantityStartValue);
    }

    public String getQuantityEndValue() {
        return this.getString(ListingFilterParameter.QUANTITY_END_VALUE);
    }

    public void setQuantityEndValue(final String quantityEndValue) {
        this.addString(ListingFilterParameter.QUANTITY_END_VALUE, quantityEndValue);
    }

    public String getPriceStartValue() {
        return this.getString(ListingFilterParameter.PRICE_START_VALUE);
    }

    public void setPriceStartValue(final String priceStartValue) {
        this.addString(ListingFilterParameter.PRICE_START_VALUE, priceStartValue);
    }

    public String getPriceEndValue() {
        return this.getString(ListingFilterParameter.PRICE_END_VALUE);
    }

    public void setPriceEndValue(final String priceEndValue) {
        this.addString(ListingFilterParameter.PRICE_END_VALUE, priceEndValue);
    }

    public String getParams() {
        return this.getString(ListingFilterParameter.PARAMS);
    }

    public void setParams(final String params) {
        this.addString(ListingFilterParameter.PARAMS, params);
    }

    public String getRoles() {
        return this.getString(ListingFilterParameter.ROLES);
    }

    public void setRoles(final String roles) {
        this.addString(ListingFilterParameter.ROLES, roles);
    }

    public Integer getBackendUsersId() {
        return this.getInteger(ListingFilterParameter.BACKEND_USERS_ID);
    }

    public void setBackendUsersId(final Integer backendUsersId) {
        this.addInteger(ListingFilterParameter.BACKEND_USERS_ID, backendUsersId);
    }

    public Integer getBugPriorityId() {
        return this.getInteger(ListingFilterParameter.BUG_PRIORITY_ID);
    }

    public void setBugPriorityId(final Integer bugPriorityId) {
        this.addInteger(ListingFilterParameter.BUG_PRIORITY_ID, bugPriorityId);
    }

    public Integer getBugStatusId() {
        return this.getInteger(ListingFilterParameter.BUG_STATUS_ID);
    }

    public void setBugStatusId(final Integer bugStatusId) {
        this.addInteger(ListingFilterParameter.BUG_STATUS_ID, bugStatusId);
    }

    public Integer getBugAssigneeId() {
        return this.getInteger(ListingFilterParameter.BUG_ASSIGNEE_ID);
    }

    public void setBugAssigneeId(final Integer bugAssigneeId) {
        this.addInteger(ListingFilterParameter.BUG_ASSIGNEE_ID, bugAssigneeId);
    }

    public Integer getSortDir() {
        return this.getInteger(ListingFilterParameter.SORTDIR) == null ? this.isAscending() ? 1 : 2 : this.getInteger(ListingFilterParameter.SORTDIR);
    }

    public void setSortDir(final Integer sortDir) {
        this.addInteger(ListingFilterParameter.SORTDIR, sortDir);
    }

    public String getInvoiceType() {
        return this.getString(ListingFilterParameter.INVOICE_TYPE);
    }

    public void setInvoiceType(final String invoiceType) {
        this.addString(ListingFilterParameter.INVOICE_TYPE, invoiceType);
    }

    public String getAccountType() {
        return this.getString(ListingFilterParameter.ACCOUNT_TYPE);
    }

    public void setAccountType(final String accountType) {
        this.addString(ListingFilterParameter.ACCOUNT_TYPE, accountType);
    }

    public Integer getProductType() {
        return this.getInteger(ListingFilterParameter.PRODUCT_TYPE);
    }

    public void setProductType(final Integer productType) {
        this.addInteger(ListingFilterParameter.PRODUCT_TYPE, productType);
    }

    public Integer getInterval() {
        return this.getInteger(ListingFilterParameter.INTERVAL);
    }

    public void setInterval(final Integer interval) {
        this.addInteger(ListingFilterParameter.INTERVAL, interval);
    }

    public Integer getIntervalLimit() {
        return this.getInteger(ListingFilterParameter.INTERVAL_LIMIT);
    }

    public void setIntervalLimit(final Integer intervalLimit) {
        this.addInteger(ListingFilterParameter.INTERVAL_LIMIT, intervalLimit);
    }

    public String getIssueRelatedTo() {
        return this.getString(ListingFilterParameter.ISSUE_RELATED_TO);
    }

    public void setIssueRelatedTo(final String issueRelatedTo) {
        this.addString(ListingFilterParameter.ISSUE_RELATED_TO, issueRelatedTo);
    }

    public long getFromDate() {
        return this.get_long(ListingFilterParameter.FROM_DATE);
    }

    public void setFromDate(final long fromDate) {
        this.add_long(ListingFilterParameter.FROM_DATE, fromDate);
    }

    public long getToDate() {
        return this.get_long(ListingFilterParameter.TO_DATE);
    }

    public void setToDate(final long toDate) {
        this.add_long(ListingFilterParameter.TO_DATE, toDate);
    }

    public boolean isShowBudget() {
        return this.getBool(ListingFilterParameter.SHOW_BUDGET);
    }

    public void setShowBudget(final boolean showBudget) {
        this.addBool(ListingFilterParameter.SHOW_BUDGET, showBudget);
    }

    public boolean isSummaryView() {
        return this.getBool(ListingFilterParameter.SHOW_SUMMARY);
    }

    public void setShowSummaryView(final boolean showSummaryView) {
        this.addBool(ListingFilterParameter.SHOW_SUMMARY, showSummaryView);
    }

    public boolean isShowYTD() {
        return this.getBool(ListingFilterParameter.SHOWYTD);
    }

    public void setShowYTD(final boolean showYTD) {
        this.addBool(ListingFilterParameter.SHOWYTD, showYTD);
    }

    public boolean isShowInBase() {
        return this.getBool(ListingFilterParameter.SHOW_IN_BASE);
    }

    public void setShowInBase(final boolean showInBase) {
        this.addBool(ListingFilterParameter.SHOW_IN_BASE, showInBase);
    }

    public Date getSickRequestStartDate() {
        return this.getDate(ListingFilterParameter.SICK_REQUEST_START_DATE);
    }

    public void setSickRequestStartDate(final Date sickRequestStartDate) {
        this.addDate(ListingFilterParameter.SICK_REQUEST_START_DATE, sickRequestStartDate, true);
    }

    public Date getSickRequestEndDate() {
        return this.getDate(ListingFilterParameter.SICK_REQUEST_END_DATE);
    }

    public void setSickRequestEndDate(final Date sickRequestEndDate) {
        this.addDate(ListingFilterParameter.SICK_REQUEST_END_DATE, sickRequestEndDate, true);
    }

    public String getDepartmentIds() {
        return this.getString(ListingFilterParameter.DEPARTMENT_IDS);
    }

    public void setDepartmentIds(final String departmentIds) {
        this.addString(ListingFilterParameter.DEPARTMENT_IDS, departmentIds);
    }

    public String getReasonIds() {
        return this.getString(ListingFilterParameter.REASON_IDS);
    }

    public void setReasonIds(final String reasonIds) {
        this.addString(ListingFilterParameter.REASON_IDS, reasonIds);
    }

    public String getProjectIds() {
        return this.getString(ListingFilterParameter.PROJECT_IDS);
    }

    public void setProjectIds(final String projectIds) {
        this.addString(ListingFilterParameter.PROJECT_IDS, projectIds);
    }

    public String getTaskIds() {
        return this.getString(ListingFilterParameter.TASK_IDS);
    }

    public void setTaskIds(final String taskIds) {
        this.addString(ListingFilterParameter.TASK_IDS, taskIds);
    }

    public boolean isWithAllTaskNotes() {
        return this.getBool(ListingFilterParameter.WITH_ALL_TASK_NOTES);
    }

    public void setWithAllTaskNotes(final boolean isWithAllTaskNotes) {
        this.addBool(ListingFilterParameter.WITH_ALL_TASK_NOTES, isWithAllTaskNotes);
    }

    public Integer getLeaveDayCategory() {
        return this.getInteger(ListingFilterParameter.LEAVE_DAY_CATEGORY);
    }

    public void setLeaveDayCategory(final Integer leaveDayCategory) {
        this.addInteger(ListingFilterParameter.LEAVE_DAY_CATEGORY, leaveDayCategory);
    }

    public Integer getLeaveReqCategory() {
        return this.getInteger(ListingFilterParameter.LEAVE_REQ_CATEGORY);
    }

    public void setLeaveReqCategory(final Integer leaveReqCategory) {
        this.addInteger(ListingFilterParameter.LEAVE_REQ_CATEGORY, leaveReqCategory);
    }

    public boolean isShowProject() {
        return this.getBool(ListingFilterParameter.SHOW_PROJECT);
    }

    public void setShowProject(final boolean showProject) {
        this.addBool(ListingFilterParameter.SHOW_PROJECT, showProject);
    }

    public Integer getContactID() {
        return this.getCrmContactId();
    }

    public void setContactID(final Integer contactID) {
        this.setCrmContactId(contactID);
    }

    public Integer getLeadID() {
        return this.getCrmLeadId();
    }

    public void setLeadID(final Integer leadID) {
        this.setCrmLeadId(leadID);
    }

    public Integer getMessageId() {
        return this.getInteger(ListingFilterParameter.MESSAGE_ID);
    }

    public void setMessageId(final Integer messageId) {
        this.addInteger(ListingFilterParameter.MESSAGE_ID, messageId);
    }

    public Integer getMailListID() {
        return this.getInteger(ListingFilterParameter.MAIL_LIST_ID);
    }

    public void setMailListID(final Integer mailListID) {
        this.addInteger(ListingFilterParameter.MAIL_LIST_ID, mailListID);
    }

    public Integer getAccountID() {
        return this.getCrmAccountId();
    }

    public void setAccountID(final Integer accountID) {
        this.setCrmAccountId(accountID);
    }

    public Integer getEntityID() {
        return this.getInteger(ListingFilterParameter.ENTITY_ID);
    }

    public void setEntityID(final Integer entityID) {
        this.addInteger(ListingFilterParameter.ENTITY_ID, entityID);
    }

    public Integer getOpportunityID() {
        return this.getCrmOppartunityId();
    }

    public void setOpportunityID(final Integer opportunityID) {
        this.setCrmOppartunityId(opportunityID);
    }

    public boolean isShowEmployee() {
        return this.getBool(ListingFilterParameter.SHOW_EMPLOYEE);
    }

    public void setShowEmployee(final boolean showEmployee) {
        this.addBool(ListingFilterParameter.SHOW_EMPLOYEE, showEmployee);
    }

    public boolean isShowDepartment() {
        return this.getBool(ListingFilterParameter.SHOW_DEPARTMENT);
    }

    public void setShowDepartment(final boolean showDepartment) {
        this.addBool(ListingFilterParameter.SHOW_DEPARTMENT, showDepartment);
    }

    public int getSearchType() {
        return this.getInt(ListingFilterParameter.SEARCH_TYPE);
    }

    public void setSearchType(final int searchType) {
        this.addInt(ListingFilterParameter.SEARCH_TYPE, searchType);
    }

    public boolean isShowActive() {
        return this.getBool(ListingFilterParameter.SHOW_ACTIVE);
    }

    public void setShowActive(final boolean showUnsbcribeds) {
        this.addBool(ListingFilterParameter.SHOW_ACTIVE, showUnsbcribeds);
    }

    public boolean isShowFilledCells() {
        return this.getBool(ListingFilterParameter.SHOW_FILLED_CELLS);
    }

    public void setShowFilledCells(final boolean showFilleCells) {
        this.addBool(ListingFilterParameter.SHOW_FILLED_CELLS, showFilleCells);
    }

    public boolean isShowUnsbcribeds() {
        return this.getBool(ListingFilterParameter.SHOW_UNSUBSCRIBEDS);
    }

    public void setShowUnsbcribeds(final boolean showUnsbcribeds) {
        this.addBool(ListingFilterParameter.SHOW_UNSUBSCRIBEDS, showUnsbcribeds);
    }

    public boolean isShowEvent() {
        return this.getBool(ListingFilterParameter.SHOW_EVENT);
    }

    public void setShowEvent(final boolean showEvent) {
        this.addBool(ListingFilterParameter.SHOW_EVENT, showEvent);
    }

    public boolean isShowIssues() {
        return this.getBool(ListingFilterParameter.SHOW_ISSUE);
    }

    public void setShowIssues(final boolean showIssues) {
        this.addBool(ListingFilterParameter.SHOW_ISSUE, showIssues);
    }

    public boolean isShowTasks() {
        return this.getBool(ListingFilterParameter.SHOW_TASKS);
    }

    public void setShowTasks(final boolean showTasks) {
        this.addBool(ListingFilterParameter.SHOW_TASKS, showTasks);
    }

    public boolean isShowLeaveRequest() {
        return this.getBool(ListingFilterParameter.SHOW_LEAVE_REQUEST);
    }

    public void setShowLeaveRequest(final boolean showLeaveRequest) {
        this.addBool(ListingFilterParameter.SHOW_LEAVE_REQUEST, showLeaveRequest);
    }

    public boolean isAnnualLeave() {
        return this.getBool(ListingFilterParameter.ANNUAL_LEAVE_REQUEST);
    }

    public void setAnnualLeave(final boolean annualLeave) {
        this.addBoolean(ListingFilterParameter.ANNUAL_LEAVE_REQUEST, annualLeave);
    }

    public boolean isShowPA() {
        return this.getBool(ListingFilterParameter.SHOW_PA);
    }

    public void setShowPA(final boolean showPA) {
        this.addBool(ListingFilterParameter.SHOW_PA, showPA);
    }

    public boolean isShowHeadOffice() {
        return this.getBool(ListingFilterParameter.SHOW_HEAD_OFFICE);
    }

    public void setShowHeadOffice(final boolean showHeadOffice) {
        this.addBool(ListingFilterParameter.SHOW_HEAD_OFFICE, showHeadOffice);
    }

    public boolean isShowHolidays() {
        return this.getBool(ListingFilterParameter.SHOW_HOLIDAYS);
    }

    public void setShowHolidays(final boolean showHolidays) {
        this.addBool(ListingFilterParameter.SHOW_HOLIDAYS, showHolidays);
    }

    public boolean isResourceIdNull() {
        return this.getBool(ListingFilterParameter.RESOURCE_ID_NULL);
    }

    public void setResourceIdNull(final boolean resourceIdNull) {
        this.addBool(ListingFilterParameter.RESOURCE_ID_NULL, resourceIdNull);
    }

    public String getCfColumnCode() {
        return this.getString(ListingFilterParameter.CF_COLUMN_CODE);
    }

    public void setCfColumnCode(final String cfColumnCode) {
        this.addString(ListingFilterParameter.CF_COLUMN_CODE, cfColumnCode);
    }

    public String getCfColumnValue() {
        return this.getString(ListingFilterParameter.CF_COLUMN_VALUE);
    }

    public void setCfColumnValue(final String cfColumnValue) {
        this.addString(ListingFilterParameter.CF_COLUMN_VALUE, cfColumnValue);
    }

    public Integer getWorkstreamID() {
        return this.getInteger(ListingFilterParameter.WORKSTREAM_ID);
    }

    public void setWorkstreamID(final Integer workstreamID) {
        this.addInteger(ListingFilterParameter.WORKSTREAM_ID, workstreamID);
    }

    public String getWorkstreamName() {
        return this.getString(ListingFilterParameter.WORKSTREAM_NAME);
    }

    public void setWorkstreamName(final String workstreamName) {
        this.addString(ListingFilterParameter.WORKSTREAM_NAME, workstreamName);
    }

    public Integer getWarehouseID() {
        return this.getInteger(ListingFilterParameter.WAREHOUSE_ID);
    }

    public void setWarehouseID(final Integer warehouseID) {
        this.addInteger(ListingFilterParameter.WAREHOUSE_ID, warehouseID);
    }

    public boolean isFromCoo() {
        return this.getBool(ListingFilterParameter.FROM_COO);
    }

    public void setFromCoo(final boolean fromCoo) {
        this.addBool(ListingFilterParameter.FROM_COO, fromCoo);
    }

    public boolean isFromExcelPDF() {
        return this.getBool(ListingFilterParameter.FROM_EXCEL_PDF);
    }

    public void setFromExcelPDF(final boolean fromExcelPDF) {
        this.addBool(ListingFilterParameter.FROM_EXCEL_PDF, fromExcelPDF);
    }

    public String getCountryCode() {
        return this.getString(ListingFilterParameter.COUNTRY_CODE);
    }

    public void setCountryCode(final String countryCode) {
        this.addString(ListingFilterParameter.COUNTRY_CODE, countryCode);
    }

    public Boolean isForCSVonly() {
        return this.getBoolean(ListingFilterParameter.FOR_CSV_ONLY) == null ? Boolean.FALSE : this.getBool(ListingFilterParameter.FOR_CSV_ONLY);
    }

    public void setForCSVonly(final Boolean forCSVonly) {
        this.addBoolean(ListingFilterParameter.FOR_CSV_ONLY, forCSVonly);
    }

    public void setAsSelectItem(final Boolean asSelectItem) {
        this.addBoolean(ListingFilterParameter.SELECT_ITEM, asSelectItem);
    }

    public Boolean isAsSelectItem() {
        return this.getBoolean(ListingFilterParameter.SELECT_ITEM) != null && this.getBool(ListingFilterParameter.SELECT_ITEM);
    }

    public Integer getStorefrontID() {
        return this.getInteger(ListingFilterParameter.STOREFRONT_ID);
    }

    public void setStorefrontID(final Integer storefrontID) {
        this.addInteger(ListingFilterParameter.STOREFRONT_ID, storefrontID);
    }

    public Integer getCategoryID() {
        return this.getInteger(ListingFilterParameter.CATEGORY_ID);
    }

    public void setCategoryID(final Integer categoryID) {
        this.addInteger(ListingFilterParameter.CATEGORY_ID, categoryID);
    }

    public Integer getBrandID() {
        return this.getInteger(ListingFilterParameter.BRAND_ID);
    }

    public void setBrandID(final Integer brandID) {
        this.addInteger(ListingFilterParameter.BRAND_ID, brandID);
    }

    public Integer[] getCategories() {
        return this.categories;
    }

    public void setCategories(final Integer[] categories) {
        this.categories = categories;
    }

    public void setDoNotSearch(final Boolean doNotSearch) {
        this.addBoolean(ListingFilterParameter.DO_NOT_SEARCH, doNotSearch);
    }

    public Boolean isDoNotSearch() {
        return this.getBoolean(ListingFilterParameter.DO_NOT_SEARCH) != null && this.getBool(ListingFilterParameter.DO_NOT_SEARCH);
    }

    public Boolean isDoNotExportToQB() {
        return this.getBoolean(ListingFilterParameter.DO_NOT_EXPORT_TO_QB) != null && this.getBool(ListingFilterParameter.DO_NOT_EXPORT_TO_QB);
    }

    public void setDoNotExportToQB(final Boolean doNotExportToQB) {
        this.addBoolean(ListingFilterParameter.DO_NOT_EXPORT_TO_QB, doNotExportToQB);
    }

    public void setListLoadConfig(final ListLoadConfig config) {
        this.setSortField(config.getSortField());
        this.setStart(config.getStart());
        this.setLimit(config.getLimit());
        this.setSortDir(config.getSortDir());
    }

    public String getClientName() {
        return this.getString(ListingFilterParameter.CLIENT_NAME);
    }

    public void setClientName(final String clientName) {
        this.addString(ListingFilterParameter.CLIENT_NAME, clientName);
    }

    public boolean isCRM() {
        return this.getBool(ListingFilterParameter.CRM);
    }

    public void setCRM(final boolean crm) {
        this.addBool(ListingFilterParameter.CRM, crm);
    }

    public boolean isHRMS() {
        return this.getBool(ListingFilterParameter.HRMS);
    }

    public void setHRMS(final boolean hrms) {
        this.addBool(ListingFilterParameter.HRMS, hrms);
    }

    public boolean isFiltirize() {
        return this.getBoolean(ListingFilterParameter.FILTIRIZE) == null || this.getBool(ListingFilterParameter.FILTIRIZE);
    }

    public void setFiltirize(final boolean r) {
        this.addBool(ListingFilterParameter.FILTIRIZE, r);
    }

    public boolean isTrainingCenter() {
        return this.getBool(ListingFilterParameter.TRAINING_CENTER);
    }

    public void setTrainingCenter(final boolean bool) {
        this.addBool(ListingFilterParameter.TRAINING_CENTER, bool);
    }

    public boolean isPM() {
        return this.getBool(ListingFilterParameter.PM);
    }

    public void setPM(final boolean pm) {
        this.addBool(ListingFilterParameter.PM, pm);
    }

    public boolean isSaleOrder() {
        return this.getBool(ListingFilterParameter.SALE_ORDER);
    }

    public void setSaleOrder(final boolean saleOrder) {
        this.addBool(ListingFilterParameter.SALE_ORDER, saleOrder);
    }

    public boolean isWithImage() {
        return this.getBoolean(ListingFilterParameter.WITH_IMAGE) != null && this.getBool(ListingFilterParameter.WITH_IMAGE);
    }

    public void setWithImage(final boolean withImage) {
        this.addBool(ListingFilterParameter.WITH_IMAGE, withImage);
    }

    public boolean isLookUp() {
        return this.getBool(ListingFilterParameter.LOOKUP);
    }

    public void setLookUp(final boolean lookUp) {
        this.addBool(ListingFilterParameter.LOOKUP, lookUp);
    }

    public String getLookUpBy() {
        return this.getString(ListingFilterParameter.LOOK_UP_BY);
    }

    public void setLookUpBy(final String lookUpBy) {
        this.addString(ListingFilterParameter.LOOK_UP_BY, lookUpBy);
    }

    public void setCustomFieldsShown(final Boolean customFieldsShown) {
        this.addBoolean(ListingFilterParameter.CUSTOM_FIELDS_SHOWN, customFieldsShown);
    }

    public boolean isForChanging() {
        return this.getBoolean(ListingFilterParameter.FOR_CHANGING) != null && this.getBool(ListingFilterParameter.FOR_CHANGING);
    }

    public void setForChanging(final boolean forChanging) {
        this.addBool(ListingFilterParameter.FOR_CHANGING, forChanging);
    }

    public boolean isInvoicesOnly() {
        return this.getBoolean(ListingFilterParameter.INVOICE_ONLY) != null && this.getBool(ListingFilterParameter.INVOICE_ONLY);
    }

    public void setInvoicesOnly(final boolean invoicesOnly) {
        this.addBool(ListingFilterParameter.INVOICE_ONLY, invoicesOnly);
    }

    public boolean isQuotesOnly() {
        return this.getBoolean(ListingFilterParameter.QUOTES_ONLY) != null && this.getBool(ListingFilterParameter.QUOTES_ONLY);
    }

    public void setQuotesOnly(final boolean quotesOnly) {
        this.addBool(ListingFilterParameter.QUOTES_ONLY, quotesOnly);
    }

    public Integer getStatusID() {
        return this.getInteger(ListingFilterParameter.STATUS_ID);
    }

    public void setStatusID(final Integer statusID) {
        this.addInteger(ListingFilterParameter.STATUS_ID, statusID);
    }

    public Integer[] getStatusIDs() {
        return this.statusIDs;
    }

    public void setStatusIDs(final Integer[] statusIDs) {
        this.statusIDs = statusIDs;
    }

    public Integer getIgnoreID() {
        return this.getInteger(ListingFilterParameter.IGNORE_ID);
    }

    public void setIgnoreID(final Integer ignoreID) {
        this.addInteger(ListingFilterParameter.IGNORE_ID, ignoreID);
    }

    public Boolean isCustomFieldsShown() {
        return this.getBoolean(ListingFilterParameter.CUSTOM_FIELDS_SHOWN) != null && this.getBool(ListingFilterParameter.CUSTOM_FIELDS_SHOWN);
    }

    public ArrayList<String> getColumnsOfListing() {
        return this.columnsOfListing;
    }

    public void setColumnsOfListing(final ArrayList<String> columnsOfListing) {
        this.columnsOfListing = columnsOfListing;
    }

    public boolean isFromMobile() {
        return this.getBoolean(ListingFilterParameter.FROM_MOBILE) != null && this.getBool(ListingFilterParameter.FROM_MOBILE);
    }

    public void setFromMobile(final boolean fromMobile) {
        this.addBool(ListingFilterParameter.FROM_MOBILE, fromMobile);
    }

    public Integer getCurrencyID() {
        return this.getInteger(ListingFilterParameter.CURRENCY_ID);
    }

    public void setCurrencyID(final Integer currencyID) {
        this.addInteger(ListingFilterParameter.CURRENCY_ID, currencyID);
    }

    public Integer getBaseCurrencyID() {
        return this.getInteger(ListingFilterParameter.BASE_CURRENCY_ID);
    }

    public void setBaseCurrencyID(final Integer currencyID) {
        this.addInteger(ListingFilterParameter.BASE_CURRENCY_ID, currencyID);
    }

    public BigDecimal getExchangeRate() {
        return this.getBigDecimal(ListingFilterParameter.EXCHANGE_RATE);
    }

    public void setExchangeRate(final BigDecimal exchangeRate) {
        this.addBigDecimal(ListingFilterParameter.EXCHANGE_RATE, exchangeRate);
    }

    public String getAccountCode() {
        return this.getString(ListingFilterParameter.ACCOUNT_CODE);
    }

    public void setAccountCode(final String accountCode) {
        this.addString(ListingFilterParameter.ACCOUNT_CODE, accountCode);
    }

    public HashMap<String, String> getCustomFields() {
        return this.customFields;
    }

    public void setCustomFields(final HashMap<String, String> customFields) {
        this.customFields = customFields;
    }

    public Integer getWithoutType() {
        return this.getInteger(ListingFilterParameter.WITHOUT_TYPE);
    }

    public void setWithoutType(final Integer withoutType) {
        this.addInteger(ListingFilterParameter.WITHOUT_TYPE, withoutType);
    }

    public Boolean getShowVariations() {
        return this.getBoolean(ListingFilterParameter.SHOW_VARIATIONS);
    }

    public void setShowVariations(final Boolean showVariations) {
        this.addBoolean(ListingFilterParameter.SHOW_VARIATIONS, showVariations);
    }

    public boolean isSearchByParent() {
        return this.getBool(ListingFilterParameter.SEARCH_BY_PARENT);
    }

    public void setSearchByParent(final boolean searchByParent) {
        this.addBool(ListingFilterParameter.SEARCH_BY_PARENT, searchByParent);
    }

//    public Boolean isShowOnOpportunity() {
//        return getBoolean(SHOW_ON_OPPORTUNITY);
//    }

//    public void setShowOnOpportunity(Boolean showOnOpportunity) {
//        addBoolean(SHOW_ON_OPPORTUNITY, showOnOpportunity);
//    }

    public boolean isFeatured() {
        return this.getBool(ListingFilterParameter.FEATURED);
    }

    public void setFeatured(final boolean featured) {
        this.addBool(ListingFilterParameter.FEATURED, featured);
    }

    public boolean isSpecialOffer() {
        return this.getBool(ListingFilterParameter.SPECIAL_OFFER);
    }

    public void setSpecialOffer(final boolean specialOffer) {
        this.addBool(ListingFilterParameter.SPECIAL_OFFER, specialOffer);
    }

    public Integer getItemId() {
        return this.getInteger(ListingFilterParameter.ITEM_ID);
    }

    public void setItemId(final Integer itemId) {
        this.addInteger(ListingFilterParameter.ITEM_ID, itemId);
    }

    public Integer getProductId() {
        return this.getInteger(ListingFilterParameter.PRODUCT_ID);
    }

    public void setProductId(final Integer productId) {
        this.addInteger(ListingFilterParameter.PRODUCT_ID, productId);
    }

    public boolean isShowWebsiteProducts() {
        return this.getBoolean(ListingFilterParameter.SHOW_WEBSITE_PRODUCTS) != null && this.getBool(ListingFilterParameter.SHOW_WEBSITE_PRODUCTS);
    }

    public void setShowWebsiteProducts(final boolean showWebsiteProducts) {
        this.addBool(ListingFilterParameter.SHOW_WEBSITE_PRODUCTS, showWebsiteProducts);
    }

    public Integer getWebsiteID() {
        return this.getInteger(ListingFilterParameter.WEBSITE_ID);
    }

    public void setWebsiteID(final Integer websiteID) {
        this.addInteger(ListingFilterParameter.WEBSITE_ID, websiteID);
    }

    public String getLookUpSearchKey() {
        if (this.isValidSearchKey()) {
            return (this.getSearchKey().trim() + " ").replace("'", "''")
                    .replace(" ", "%")
                    .toLowerCase();
        }
        return null;
    }

    public String getDateAsString(final Date date) {
        if (date != null) {
            date.toString();
        }
        return null;
    }

    public boolean isDetectDuplicates() {
        return this.getBool(ListingFilterParameter.DETECT_DUPLICATES);
    }

    public void setDetectDuplicates(final boolean detectDuplicates) {
        this.addBool(ListingFilterParameter.DETECT_DUPLICATES, detectDuplicates);
    }

    public ArrayList<Integer> getObjectIDs() {
        return this.objectIDs;
    }

    public void setObjectIDs(final ArrayList<Integer> objectIDs) {
        this.objectIDs = objectIDs;
    }

    public boolean isAllGoals() {
        return this.getBool(ListingFilterParameter.ALL_GOALS);
    }

    public void setAllGoals(final boolean allGoals) {
        this.addBool(ListingFilterParameter.ALL_GOALS, allGoals);
    }

    public boolean isCheckNumber() {
        return this.getBool(ListingFilterParameter.CHECK_NUMBER);
    }

    public void setCheckNumber(final boolean checkNumber) {
        this.addBool(ListingFilterParameter.CHECK_NUMBER, checkNumber);
    }

    public boolean isCheckName() {
        return this.getBool(ListingFilterParameter.CHECK_NAME);
    }

    public void setCheckName(final boolean checkName) {
        this.addBool(ListingFilterParameter.CHECK_NAME, checkName);
    }

    public boolean isAssignedItems() {
        return this.getBool(ListingFilterParameter.ASSIGNED_ITEMS);
    }

    public void setAssignedItems(final boolean assigned) {
        this.addBool(ListingFilterParameter.ASSIGNED_ITEMS, assigned);
    }

    public boolean isNewType() {
        return this.getBool(ListingFilterParameter.NEW_TYPE);
    }

    public void setNewType(final boolean newType) {
        this.addBool(ListingFilterParameter.NEW_TYPE, newType);
    }

    public boolean isActive() {
        return !Boolean.FALSE.equals(this.getBoolean(ListingFilterParameter.ACTIVE));
    }

    public void setActive(final boolean active) {
        this.addBool(ListingFilterParameter.ACTIVE, active);
    }

    public boolean isOnlyIssueTasks() {
        return this.getBool(ListingFilterParameter.ONLY_ISSUE_TASKS);
    }

    public void setOnlyIssueTasks(final boolean onlyIssueTasks) {
        this.addBool(ListingFilterParameter.ONLY_ISSUE_TASKS, onlyIssueTasks);
    }

    public String getNumber() {
        return this.getString(ListingFilterParameter.NUMBER);
    }

    public void setNumber(final String number) {
        this.addString(ListingFilterParameter.NUMBER, number);
    }

    public Integer getRootID() {
        return this.getInteger(ListingFilterParameter.ROOT_ID);
    }

    public void setRootID(final Integer rootID) {
        this.addInteger(ListingFilterParameter.ROOT_ID, rootID);
    }

    public boolean isSystemSubFolder() {
        return this.getBool(ListingFilterParameter.SYSTEM_SUB_FOLDER);
    }

    public void setSystemSubFolder(final boolean systemSubFolder) {
        this.addBool(ListingFilterParameter.SYSTEM_SUB_FOLDER, systemSubFolder);
    }

    public int getUsertimeHoursOffset() {
        return this.getInt(ListingFilterParameter.USERTIME_HOURS_OFFSET);
    }

    public void setUsertimeHoursOffset(final int usertimeHoursOffset) {
        this.addInt(ListingFilterParameter.USERTIME_HOURS_OFFSET, usertimeHoursOffset);
    }

    public boolean hasOnlyClientAccess() {
        return this.getBool(ListingFilterParameter.HAS_ONLY_CLIENT_ACCESS);
    }

    public void setHasOnlyClientAccess(final boolean clientView) {
        this.addBool(ListingFilterParameter.HAS_ONLY_CLIENT_ACCESS, clientView);
    }

    public boolean isAccessEnabled() {
        return this.getBool(ListingFilterParameter.ACCESS_ENABLED);
    }

    public void setAccessEnabled(final boolean accessEnabled) {
        this.addBool(ListingFilterParameter.ACCESS_ENABLED, accessEnabled);
    }

    public boolean hasOnlySalesPersonRole() {
        return this.getBool(ListingFilterParameter.HAS_ONLY_SALES_PERSON_ROLE);
    }

    public void setHasOnlySalesPersonRole(final boolean salesPersonView) {
        this.addBool(ListingFilterParameter.HAS_ONLY_SALES_PERSON_ROLE, salesPersonView);
    }

    public void setUseAndOperator(final boolean useAndOperator) {
        this.addBool(ListingFilterParameter.USE_AND_OPERATOR, useAndOperator);
    }

    public boolean useAndOperator() {
        return this.getBool(ListingFilterParameter.USE_AND_OPERATOR);
    }

    public Boolean getLibrary() {
        return this.getBoolean(ListingFilterParameter.IS_LIBRARY);
    }

    public void setLibrary(final Boolean isLibrary) {
        this.addBoolean(ListingFilterParameter.IS_LIBRARY, isLibrary);
    }

    public Integer getYear() {
        return this.getInteger(ListingFilterParameter.YEAR);
    }

    public void setYear(final Integer year) {
        this.addInteger(ListingFilterParameter.YEAR, year);
    }

    public String getDeviceID() {
        return this.getString(ListingFilterParameter.DEVICE_ID);
    }

    public void setDeviceID(final String deviceID) {
        this.addString(ListingFilterParameter.DEVICE_ID, deviceID);
    }

    public Integer[] getCompaines() {
        return compaines;
    }

    public void setCompaines(final Integer[] compaines) {
        this.compaines = compaines;
    }

    public String[] getParameters() {
        return this.parameters;
    }

    public void setParameters(final String[] parameters) {
        this.parameters = parameters;
    }

    public Integer getParentID() {
        return this.getInteger(ListingFilterParameter.PARENT_ID);
    }

    public void setParentID(final Integer parentID) {
        this.addInteger(ListingFilterParameter.PARENT_ID, parentID);
    }

    public void setSelected(final Boolean selected) {
        this.addBoolean(ListingFilterParameter.SELECTED, selected);
    }

    public Boolean isSelected() {
        return this.getBoolean(ListingFilterParameter.SELECTED);
    }

    public String getName() {
        return this.getString(ListingFilterParameter.NAME);
    }

    public void setName(final String name) {
        this.addString(ListingFilterParameter.NAME, name);
    }

    public String getDescription() {
        return this.getString(ListingFilterParameter.DESCRIPTION);
    }

    public void setDescription(final String description) {
        this.addString(ListingFilterParameter.DESCRIPTION, description);
    }

    public String getDataType() {
        return this.getString(ListingFilterParameter.DATA_TYPE);
    }

    public void setDataType(final String dataType) {
        this.addString(ListingFilterParameter.DATA_TYPE, dataType);
    }

    public boolean isListDepartments() {
        return this.getBool(ListingFilterParameter.DEPARTMENT);
    }

    public void setListDepartments(final boolean listsDepartments) {
        this.addBool(ListingFilterParameter.DEPARTMENT, listsDepartments);
    }

    public boolean isListEmployees() {
        return this.getBool(ListingFilterParameter.EMPLOYEE_LIST);
    }

    public void setListEmployees(final boolean listsEmployees) {
        this.addBool(ListingFilterParameter.EMPLOYEE_LIST, listsEmployees);
    }

    public String getRelationName() {
        return this.getString(ListingFilterParameter.RELATION_NAME);
    }

    public void setRelationName(final String relationName) {
        this.addString(ListingFilterParameter.RELATION_NAME, relationName);
    }

    public boolean isEmailIncluded() {
        return this.getBoolean(ListingFilterParameter.INCLUDE_EMAIL) == null || this.getBoolean(ListingFilterParameter.INCLUDE_EMAIL);
    }

    public void setEmailIncluded(final boolean includeEmail) {
        this.addBoolean(ListingFilterParameter.INCLUDE_EMAIL, includeEmail);
    }

    public boolean isIncludeDayOff() {
        return this.getBoolean(ListingFilterParameter.INCLUDE_DAY_OFF) == null || this.getBoolean(ListingFilterParameter.INCLUDE_DAY_OFF);
    }

    public void setIncludeDayOff(final boolean includeDayOff) {
        this.addBoolean(ListingFilterParameter.INCLUDE_DAY_OFF, includeDayOff);
    }

    public boolean isShowCompletedTasks() {
        return this.getBool(ListingFilterParameter.SHOW_COMPLETED_TASK);
    }

    public void setShowCompletedTasks(final boolean showCompletedTasks) {
        this.addBoolean(ListingFilterParameter.SHOW_COMPLETED_TASK, showCompletedTasks);
    }

    public boolean useSelectedDate() {
        return this.getBool(ListingFilterParameter.USE_SELECTED_DATE);
    }

    public void setUseSelectedDate(final boolean useSelectedDate) {
        this.addBoolean(ListingFilterParameter.USE_SELECTED_DATE, useSelectedDate);
    }

    public Integer getEventType() {
        return this.getInteger(ListingFilterParameter.EVENT_TYPE);
    }

    public void setEventType(final Integer eventType) {
        this.addInteger(ListingFilterParameter.EVENT_TYPE, eventType);
    }

    public Integer getCreatedFrom() {
        return this.getInteger(ListingFilterParameter.CREATED_FROM);
    }

    public void setCreatedFrom(final Integer createdFrom) {
        this.addInteger(ListingFilterParameter.CREATED_FROM, createdFrom);
    }

    public Integer getApproverID() {
        return this.getInteger(ListingFilterParameter.APPROVER_ID);
    }

    public void setApproverID(final Integer integer) {
        this.addInteger(ListingFilterParameter.APPROVER_ID, integer);
    }

    public boolean isResignedEmployeesIncluded() {
        return this.getBoolean(ListingFilterParameter.INCLUDE_RESIGNED_EMPLOYEES) == null || this.getBoolean(ListingFilterParameter.INCLUDE_RESIGNED_EMPLOYEES);
    }

    public void setResignedEmployeesIncluded(final boolean includeResignedEmployees) {
        this.addBoolean(ListingFilterParameter.INCLUDE_RESIGNED_EMPLOYEES, includeResignedEmployees);
    }

    public boolean isShowEmployeesWithResignationDate() {
        return this.getBoolean(ListingFilterParameter.SHOW_EMPLOYEES_WITH_RESIGNATION_DATE) != null ? this.getBoolean(ListingFilterParameter.SHOW_EMPLOYEES_WITH_RESIGNATION_DATE) : false;
    }

    public void setShowEmployeesWithResignationDate(final boolean includeResignedEmployees) {
        this.addBoolean(ListingFilterParameter.SHOW_EMPLOYEES_WITH_RESIGNATION_DATE, includeResignedEmployees);
    }

    public boolean isEmployeeListForVacant() {
        return this.getBoolean(ListingFilterParameter.EMPLOYEE_LIST_FOR_VACANT) != null ? this.getBoolean(ListingFilterParameter.EMPLOYEE_LIST_FOR_VACANT) : false;
    }

    public void setEmployeeListForVacant(final boolean employeeListForVacant) {
        this.addBoolean(ListingFilterParameter.EMPLOYEE_LIST_FOR_VACANT, employeeListForVacant);
    }

    public boolean isDoNotIncludeTasksFromToDoList() {
        return this.getBool(ListingFilterParameter.DO_NOT_INCLUDE_TASKS_FROM_TO_DO_LIST);
    }

    public void setDoNotIncludeTasksFromToDoList(final boolean doNotIncludeTasksFromToDoList) {
        this.addBool(ListingFilterParameter.DO_NOT_INCLUDE_TASKS_FROM_TO_DO_LIST, doNotIncludeTasksFromToDoList);
    }

    public boolean isShortList() {
        return this.getBool(ListingFilterParameter.IS_SHORT_LIST);
    }

    public void setShortList(final boolean shortList) {
        this.addBool(ListingFilterParameter.IS_SHORT_LIST, shortList);
    }

    public boolean isSelectCandidate() {
        return this.getBool(ListingFilterParameter.IS_SELECT_CANDIDATE);
    }

    public void setSelectCandidate(final boolean shortList) {
        this.addBool(ListingFilterParameter.IS_SELECT_CANDIDATE, shortList);
    }

    public boolean isIncident() {
        return this.getBool(ListingFilterParameter.IS_INCIDENT);
    }

    public void setIncident(final boolean incident) {
        this.addBool(ListingFilterParameter.IS_INCIDENT, incident);
    }

    public Integer getValidityPeriodId() {
        return this.getInteger(ListingFilterParameter.VALIDITY_PERIOD_ID);
    }

    public void setValidityPeriodId(final Integer periodId) {
        this.addInteger(ListingFilterParameter.VALIDITY_PERIOD_ID, periodId);
    }

    public String getStatusCode() {
        return this.getString(ListingFilterParameter.STATUS_CODE);
    }

    public void setStatusCode(final String statusCode) {
        this.addString(ListingFilterParameter.STATUS_CODE, statusCode);
    }

    public String[] getStatusCodes() {
        return this.statusCodes;
    }

    public void setStatusCodes(final String[] statusCodes) {
        this.statusCodes = statusCodes;
    }

    public Boolean isReviewer() {
        return this.getBoolean(ListingFilterParameter.REVIEWER_ID);
    }

    public void setIsReviewer(final Boolean is) {
        this.addBoolean(ListingFilterParameter.REVIEWER_ID, is);
    }

    public boolean isClearAndRecalculate() {
        return this.getBool(ListingFilterParameter.CLEAR_AND_RECALCULATE);
    }

    public void setClearAndRecalculate(final boolean clearAndRecalculate) {
        this.addBool(ListingFilterParameter.CLEAR_AND_RECALCULATE, clearAndRecalculate);
    }

    public boolean isSimpilifiedReportTemplate() {
        return this.getBool(ListingFilterParameter.IS_SIMPILIFIED_REPORT_TEMPLATE);
    }

    public void setIsSimpilifiedReportTemplate(final boolean value) {
        this.addBool(ListingFilterParameter.IS_SIMPILIFIED_REPORT_TEMPLATE, value);
    }

    public boolean isEntityBasedAttachmentList() {
        return this.getBool(ListingFilterParameter.ENTITY_BASED_ATTACHMENT_LIST);
    }

    public void setEntityBasedAttachmentList(final boolean value) {
        this.addBool(ListingFilterParameter.ENTITY_BASED_ATTACHMENT_LIST, value);
    }

    public String getURL() {
        return this.getString(ListingFilterParameter.URL) == null ? Constants.KPI_PUBLIC_WORKSPACE : this.getString(ListingFilterParameter.URL);
    }

    public void setURL(final String url) {
        this.addString(ListingFilterParameter.URL, url);
    }

    public String getBlockExternalGUID() {
        return this.getString(ListingFilterParameter.BLOCK_EXTERNAL_GUID);
    }

    public void setBlockExternalGUID(final String block_external_guid) {
        this.addString(ListingFilterParameter.BLOCK_EXTERNAL_GUID, block_external_guid);
    }

    public Integer getScheduledCourseID() {
        return this.getInteger(ListingFilterParameter.SCHEDULED_COURSE_ID);
    }

    public void setScheduledCourseID(final Integer scheduledCourseID) {
        this.addInteger(ListingFilterParameter.SCHEDULED_COURSE_ID, scheduledCourseID);
    }

    public String getViewType() {
        return this.getString(ListingFilterParameter.VIEW_TYPE);
    }

    public void setViewType(final String viewType) {
        this.addString(ListingFilterParameter.VIEW_TYPE, viewType);
    }

    public Integer getCourseID() {
        return this.getInteger(ListingFilterParameter.COURSE_ID);
    }

    public void setCourseID(final Integer courseID) {
        this.addInteger(ListingFilterParameter.COURSE_ID, courseID);
    }

    public String getReservationIds() {
        return this.getString(ListingFilterParameter.RESERVATION_IDS);
    }

    public void setReservationIds(final String reservationIds) {
        this.addString(ListingFilterParameter.RESERVATION_IDS, reservationIds);
    }

    public Integer getLanguageID() {
        return this.getInteger(ListingFilterParameter.LANGUAGE_ID);
    }

    public void setLanguageID(final Integer languageID) {
        this.addInteger(ListingFilterParameter.LANGUAGE_ID, languageID);
    }

    public String getLanguageIDs() {
        return this.getString(ListingFilterParameter.LANGUAGE_IDS);
    }

    public void setLanguageIDs(final String languageIDs) {
        this.addString(ListingFilterParameter.LANGUAGE_IDS, languageIDs);
    }

    public String getLanguage() {
        return this.getString(ListingFilterParameter.LANGUAGE);
    }

    public void setLanguage(final String language) {
        this.addString(ListingFilterParameter.LANGUAGE, language);
    }

    public boolean isValidateChildAccounts() {
        return this.getBool(ListingFilterParameter.VALIDATE_CHILD_ACCOUNTS);
    }

    public void setValidateChildAccounts(final boolean validateChildAccounts) {
        this.addBool(ListingFilterParameter.VALIDATE_CHILD_ACCOUNTS, validateChildAccounts);
    }

    public boolean isInstructorList() {
        return this.getBool(ListingFilterParameter.IS_INSTRUCTOR_LIST);
    }

    public void setInstructorList(final boolean isInstructorList) {
        this.addBool(ListingFilterParameter.IS_INSTRUCTOR_LIST, isInstructorList);
    }

    public String getSortFieldType() {
        return this.getString(ListingFilterParameter.SORT_FIELD_TYPE);
    }

    public void setSortFieldType(final String sortFieldType) {
        this.addString(ListingFilterParameter.SORT_FIELD_TYPE, sortFieldType);
    }

    public String getGUID() {
        return this.getString(ListingFilterParameter.GUID);
    }

    public void setGUID(final String guid) {
        this.addString(ListingFilterParameter.GUID, guid);
    }

    public String getModule() {
        return this.getString(ListingFilterParameter.MODULE);
    }

    public void setModule(final String module) {
        this.addString(ListingFilterParameter.MODULE, module);
    }

    public String getSource() {
        return this.getString(ListingFilterParameter.SOURCE);
    }

    public void setSource(final String source) {
        this.addString(ListingFilterParameter.SOURCE, source);
    }

    public String getForm() {
        return this.getString(ListingFilterParameter.FORM);
    }

    public void setForm(final String form) {
        this.addString(ListingFilterParameter.FORM, form);
    }

    public String getColumn() {
        return this.getString(ListingFilterParameter.COLUMN);
    }

    public void setColumn(final String column) {
        this.addString(ListingFilterParameter.COLUMN, column);
    }

    public boolean isConvertedLead() {
        return this.getBool(ListingFilterParameter.IS_CONVERTED_LEAD);
    }

    public void setConvertedLead(final boolean isConvertedLead) {
        this.addBool(ListingFilterParameter.IS_CONVERTED_LEAD, isConvertedLead);
    }

    public Integer getConvertedLeadId() {
        return this.getInteger(ListingFilterParameter.CONVERTED_LEAD_ID);
    }

    public void setConvertedLeadId(final Integer convertedLeadId) {
        this.addInteger(ListingFilterParameter.CONVERTED_LEAD_ID, convertedLeadId);
    }

    public Integer getSelectedYear() {
        return this.getInteger(ListingFilterParameter.SELECTED_YEAR);
    }

    public void setSelectedYear(final Integer selectedYear) {
        this.addInteger(ListingFilterParameter.SELECTED_YEAR, selectedYear);
    }

    public Integer getSelectedMonth() {
        return this.getInteger(ListingFilterParameter.SELECTED_MONTH);
    }

    public void setSelectedMonth(final Integer selectedMonth) {
        this.addInteger(ListingFilterParameter.SELECTED_MONTH, selectedMonth);
    }

    public Integer getSelectedDay() {
        return this.getInteger(ListingFilterParameter.SELECTED_DAY);
    }

    public void setSelectedDay(final Integer selectedDay) {
        this.addInteger(ListingFilterParameter.SELECTED_DAY, selectedDay);
    }

    public String getRecurrenceStatus() {
        return this.getString(ListingFilterParameter.RECURRENCE_STATUS);
    }

    public void setRecurrenceStatus(final String recurrenceStatus) {
        this.addString(ListingFilterParameter.RECURRENCE_STATUS, recurrenceStatus);
    }

    public boolean isWageRate() {
        return this.getBool(ListingFilterParameter.IS_WAGE_RATE);
    }

    public void setWageRate(final boolean wageRate) {
        this.addBool(ListingFilterParameter.IS_WAGE_RATE, wageRate);
    }

    public boolean isWithTax() {
        return this.getBool(ListingFilterParameter.IS_WITH_TAX);
    }

    public void setWithTax(final boolean withTax) {
        this.addBool(ListingFilterParameter.IS_WITH_TAX, withTax);
    }

    public boolean isExcludeExemptAndOutOfScope() {
        return this.getBool(ListingFilterParameter.EXCLUDE_EXEMPT_OUTOFSCOPE);
    }

    public void setExcludeExemptAndOutOfScope(final boolean exclude) {
        this.addBool(ListingFilterParameter.EXCLUDE_EXEMPT_OUTOFSCOPE, exclude);
    }

    public String getAccountTransactionStatus() {
        return this.getString(ListingFilterParameter.ACCOUNT_TRANSACTION_STATUS);
    }

    public void setAccountTransactionStatus(final String accountTransactionStatus) {
        this.addString(ListingFilterParameter.ACCOUNT_TRANSACTION_STATUS, accountTransactionStatus);
    }

    public Integer getStepID() {
        return this.getInteger(ListingFilterParameter.STEP_ID);
    }

    public void setStepID(final Integer stepID) {
        this.addInteger(ListingFilterParameter.STEP_ID, stepID);
    }

    public String getSkillIDs() {
        return this.getString(ListingFilterParameter.SKILL_IDS);
    }

    public void setSkillIDs(final String skillIDs) {
        this.addString(ListingFilterParameter.SKILL_IDS, skillIDs);
    }

    public String getPositionIDs() {
        return this.getString(ListingFilterParameter.POSITION_IDS);
    }

    public void setPositionIDs(final String positionIDs) {
        this.addString(ListingFilterParameter.POSITION_IDS, positionIDs);
    }

    public String getBrigadaIDs() {
        return this.getString(ListingFilterParameter.BRIGADA_IDS);
    }

    public void setBrigadaIDs(final String brigadaIDs) {
        this.addString(ListingFilterParameter.BRIGADA_IDS, brigadaIDs);
    }

    public boolean getNoPosition() {
        return this.getBool(ListingFilterParameter.NO_POSITION);
    }

    public void setNoPosition(final boolean showArchived) {
        this.addBool(ListingFilterParameter.NO_POSITION, showArchived);
    }

    public void setAgentID(final Integer agentID) {
        this.addInteger(ListingFilterParameter.AGENT_ID, agentID);
    }

    public Integer getAgentId() {
        return this.getInteger(ListingFilterParameter.AGENT_ID);
    }

    public Integer getPayrollBatchID() {
        return this.getInteger(ListingFilterParameter.PAYROLL_BATCH_ID);
    }

    public void setPayrollBatchID(final Integer payrollBatchID) {
        this.addInteger(ListingFilterParameter.PAYROLL_BATCH_ID, payrollBatchID);
    }

    public Double getFromAmount() {
        return this.getDouble(ListingFilterParameter.FROM_AMOUNT);
    }

    public void setFromAmount(final Double fromAmount) {
        this.addDouble(ListingFilterParameter.FROM_AMOUNT, fromAmount);
    }

    public Double getToAmount() {
        return this.getDouble(ListingFilterParameter.TO_AMOUNT);
    }

    public void setToAmount(final Double toAmount) {
        this.addDouble(ListingFilterParameter.TO_AMOUNT, toAmount);
    }

    public String getCategory() {
        return this.getString(ListingFilterParameter.CATEGORY);
    }

    public void setCategory(final String category) {
        this.addString(ListingFilterParameter.CATEGORY, category);
    }

    public Integer getDiscludedSchemaID() {
        return this.getInteger(ListingFilterParameter.DISCLUDED_SCHEMA_ID);
    }

    public void setDiscludedSchemaID(final Integer discludedSchemaID) {
        this.addInteger(ListingFilterParameter.DISCLUDED_SCHEMA_ID, discludedSchemaID);
    }

    public boolean isShowArchived() {
        return this.getBool(ListingFilterParameter.SHOW_ARCHIVED);
    }

    public void setShowArchived(final boolean showArchived) {
        this.addBool(ListingFilterParameter.SHOW_ARCHIVED, showArchived);
    }

    public boolean isListCandidates() {
        return this.getBool(ListingFilterParameter.CANDIDATE_LIST);
    }

    public void setListCandidates(final boolean listCandidates) {
        this.addBool(ListingFilterParameter.CANDIDATE_LIST, listCandidates);
    }

    public boolean isWithCode() {
        return this.getBool(ListingFilterParameter.WITH_CODE);
    }

    public void setWithCode(final boolean withCode) {
        this.addBool(ListingFilterParameter.WITH_CODE, withCode);
    }

    public boolean isWithoutCode() {
        return this.getBool(ListingFilterParameter.WITHOUT_CODE);
    }

    public void setWithoutCode(final boolean withoutCode) {
        this.addBool(ListingFilterParameter.WITHOUT_CODE, withoutCode);
    }

    public boolean isReceivable() {
        return this.getBool(ListingFilterParameter.IS_RECEIVABLE);
    }

    public void setReceivable(final boolean isReceivable) {
        this.addBool(ListingFilterParameter.IS_RECEIVABLE, isReceivable);
    }

    public Date getFromRegistrationDate() {
        return this.getDate(ListingFilterParameter.FROM_REGISTRATION_DATE);
    }

    public void setFromRegistrationDate(final Date fromRegistrationDate) {
        this.addDate(ListingFilterParameter.FROM_REGISTRATION_DATE, fromRegistrationDate, true);
    }

    public Date getToRegistrationDate() {
        return this.getDate(ListingFilterParameter.TO_REGISTRATION_DATE);
    }

    public void setToRegistrationDate(final Date toRegistrationDate) {
        this.addDate(ListingFilterParameter.TO_REGISTRATION_DATE, toRegistrationDate, true);
    }

    public Date getFromExpirationDate() {
        return this.getDate(ListingFilterParameter.FROM_EXPIRATION_DATE);
    }

    public void setFromExpirationDate(final Date fromExpirationDate) {
        this.addDate(ListingFilterParameter.FROM_EXPIRATION_DATE, fromExpirationDate, true);
    }

    public Date getToExpirationDate() {
        return this.getDate(ListingFilterParameter.TO_EXPIRATION_DATE);
    }

    public void setToExpirationDate(final Date toExpirationDate) {
        this.addDate(ListingFilterParameter.TO_EXPIRATION_DATE, toExpirationDate, true);
    }

    public String getSubscriptionTypeName() {
        return this.getString(ListingFilterParameter.SUBSCRIPTION_TYPE);
    }

    public void setSubscriptionTypeName(final String subscriptionTypeName) {
        this.addString(ListingFilterParameter.SUBSCRIPTION_TYPE, subscriptionTypeName);
    }

    public String getSerialNumber() {
        return this.getString(ListingFilterParameter.SERIAL_NUMBER);
    }

    public void setSerialNumber(final String serialNumber) {
        this.addString(ListingFilterParameter.SERIAL_NUMBER, serialNumber);
    }

    public Date getFromExpiryDate() {
        return this.getDate(ListingFilterParameter.FROM_EXPIRY_DATE);
    }

    public void setFromExpiryDate(final Date fromExpiryDate) {
        this.addDate(ListingFilterParameter.FROM_EXPIRY_DATE, fromExpiryDate, true);
    }

    public Date getToExpiryDate() {
        return this.getDate(ListingFilterParameter.TO_EXPIRY_DATE);
    }

    public void setToExpiryDate(final Date toExpiryDate) {
        this.addDate(ListingFilterParameter.TO_EXPIRY_DATE, toExpiryDate, true);
    }

    public String getBatchHistoryType() {
        return this.getString(ListingFilterParameter.BATCH_LIST_TYPE);
    }

    public void setBatchHistoryType(final String batchListType) {
        this.addString(ListingFilterParameter.BATCH_LIST_TYPE, batchListType);
    }

    public Integer getWarehouseId() {
        return this.getInteger(ListingFilterParameter.WAREHOUSE);
    }

    public void setWarehouseId(final Integer warehouse) {
        this.addInteger(ListingFilterParameter.WAREHOUSE, warehouse);
    }

    public String getAvoidType() {
        return this.getString(ListingFilterParameter.AVOID_TYPE);
    }

    public void setAvoidType(final String avoidType) {
        this.addString(ListingFilterParameter.AVOID_TYPE, avoidType);
    }

    public void setFormType(final String formType) {
        this.addString(ListingFilterParameter.FORM_TYPE, formType);
    }

    public String getFormtype() {
        return this.getString(ListingFilterParameter.FORM_TYPE);
    }


    public ArrayList<String> getAccountTypes() {
        return this.accountTypes;
    }

    public void setAccountTypes(final ArrayList<String> relationTypes) {
        accountTypes = relationTypes;
    }

    public ArrayList<String> getRelationTypes() {
        return this.relationTypes;
    }

    public void setRelationTypes(final ArrayList<String> accountTypes) {
        relationTypes = accountTypes;
    }

    public ArrayList<Date> getDates() {
        return this.dates;
    }

    public void setDates(final ArrayList<Date> dates) {
        this.dates = dates;
    }

    public boolean isFromListing() {
        return this.fromListing;
    }

    public void setFromListing(final boolean fromListing) {
        this.fromListing = fromListing;
    }

    public boolean isShowSubAccountTransaction() {
        return this.getBool(ListingFilterParameter.SHOW_SUB_ACCOUNT_TRANSACTION);
    }

    public void setShowSubAccountTransaction(final boolean showSubAccountT) {
        this.addBoolean(ListingFilterParameter.SHOW_SUB_ACCOUNT_TRANSACTION, showSubAccountT);
    }

    public boolean isExcludeNumber() {
        return this.getBool(ListingFilterParameter.EXCLUDE_NUMBER);
    }

    public void setExcludeNumber(final boolean excludeNumber) {
        this.addBool(ListingFilterParameter.EXCLUDE_NUMBER, excludeNumber);
    }

    public BigDecimal getPaymentsTotal() {
        return this.getBigDecimal(ListingFilterParameter.PAYMENTS_TOTAL);
    }

    public void setPaymentsTotal(final BigDecimal paymentsTotal) {
        this.addBigDecimal(ListingFilterParameter.PAYMENTS_TOTAL, paymentsTotal);
    }

    public boolean isDailyRateByEmployerSettings() {
        return this.getBool(ListingFilterParameter.DAILY_RATE_BY_EMPLOYER_SETTINGS);
    }

    public void setDailyRateByEmployerSettings(final Boolean value) {
        this.addBoolean(ListingFilterParameter.DAILY_RATE_BY_EMPLOYER_SETTINGS, value);
    }

    public boolean isLeaveDaysImpact() {
        return this.getBool(ListingFilterParameter.LEAVE_DAYS_IMPACT);
    }

    public void setLeaveDaysImpact(final Boolean value) {
        this.addBoolean(ListingFilterParameter.LEAVE_DAYS_IMPACT, value);
    }

    public Integer getPayrunID() {
        return this.getInteger(ListingFilterParameter.PAYRUN_ID);
    }

    public void setPayrunID(final Integer payrunID) {
        this.addInteger(ListingFilterParameter.PAYRUN_ID, payrunID);
    }

    public Integer getPeriodId() {
        return this.getInteger(ListingFilterParameter.PERIOD_ID);
    }

    public void setPeriodId(final Integer periodId) {
        this.addInteger(ListingFilterParameter.PERIOD_ID, periodId);
    }

    public Integer getTemplateID() {
        return this.getInteger(ListingFilterParameter.TEMPLATE_ID);
    }

    public void setTemplateID(final Integer templateID) {
        this.addInteger(ListingFilterParameter.TEMPLATE_ID, templateID);
    }

    public Integer getAvoidId() {
        return this.getInteger(ListingFilterParameter.AVOID_ID);
    }

    public void setAvoidId(final Integer id) {
        this.addInteger(ListingFilterParameter.AVOID_ID, id);
    }

    public ArrayList<Integer> getClientIds() {
        return this.clientIds;
    }

    public void setClientIds(final ArrayList<Integer> clientIds) {
        this.clientIds = clientIds;
    }

    public ArrayList<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(ArrayList<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public ArrayList<Integer> getProjectIdList() {
        return this.projectIdList;
    }

    public void setProjectIdList(final ArrayList<Integer> projectIdList) {
        this.projectIdList = projectIdList;
    }

    public Integer getPaymentMethodId() {
        return this.getInteger(ListingFilterParameter.PAYMENT_METHOD_ID);
    }

    public void setPaymentMethodId(final Integer paymentMethodId) {
        this.addInteger(ListingFilterParameter.PAYMENT_METHOD_ID, paymentMethodId);
    }

    public boolean isFromSifFile() {
        return this.getBool(ListingFilterParameter.FROM_SIF_FILE);
    }

    public void setFromSifFile(final boolean sifFile) {
        this.addBool(ListingFilterParameter.FROM_SIF_FILE, sifFile);
    }

    public Integer getRelatedProject() {
        return this.getInteger(ListingFilterParameter.RELATED_PROJECT);
    }

    public void setRelatedProject(final Integer relatedProject) {
        this.addInteger(ListingFilterParameter.RELATED_PROJECT, relatedProject);
    }

    public Integer getJournalID() {
        return this.getInteger(ListingFilterParameter.JOURNAL_ID);
    }

    public void setJournalID(final Integer journalID) {
        this.addInteger(ListingFilterParameter.JOURNAL_ID, journalID);
    }

    public boolean isForBank() {
        return this.getBool(ListingFilterParameter.FOR_BANK);
    }

    public void setForBank(final boolean forBank) {
        this.addBool(ListingFilterParameter.FOR_BANK, forBank);
    }

    public boolean isZeroAvoided() {
        return this.getBool(ListingFilterParameter.AVOID_ZERO);
    }

    public void setAvoidZero(final boolean avoidZero) {
        this.addBool(ListingFilterParameter.AVOID_ZERO, avoidZero);
    }

    public boolean isFromBudgetSheet() {
        return this.getBool(ListingFilterParameter.FROM_BUDGET_SHEET);
    }

    public void setFromBudgetSheet(final boolean fromBudgetSheet) {
        this.addBool(ListingFilterParameter.FROM_BUDGET_SHEET, fromBudgetSheet);
    }

    public Boolean isPaid() {
        return this.getBoolean(ListingFilterParameter.PAID);
    }

    public void setPaid(final Boolean paid) {
        this.addBoolean(ListingFilterParameter.PAID, paid);
    }

    public Boolean isShowAll() {
        return this.getBoolean(ListingFilterParameter.SHOW_ALL);
    }

    public void setShowAll(final Boolean showAll) {
        this.addBoolean(ListingFilterParameter.SHOW_ALL, showAll);
    }

    public Boolean isPresentActive() {
        return this.getBoolean(ListingFilterParameter.PRESENT_ACTIVE);
    }

    public void setPresentActive(final Boolean presentActive) {
        this.addBoolean(ListingFilterParameter.PRESENT_ACTIVE, presentActive);
    }

    public Boolean isShowChild() {
        return this.getBoolean(ListingFilterParameter.SHOW_CHILD);
    }

    public void setShowChild(final Boolean showChild) {
        this.addBoolean(ListingFilterParameter.SHOW_CHILD, showChild);
    }

    public boolean isExcludePrePayments() {
        return this.getBool(ListingFilterParameter.EXCLUDE_PRE_PAYMENTS);
    }

    public void setExcludePrePayments(final boolean excludePrePayments) {
        this.addBool(ListingFilterParameter.EXCLUDE_PRE_PAYMENTS, excludePrePayments);
    }

    public Integer getUnitMeasurementId() {
        return this.getInteger(ListingFilterParameter.UNIT_MEASUREMENT);
    }

    public void setUnitMeasurementId(final Integer unitMeasurementId) {
        this.addInteger(ListingFilterParameter.UNIT_MEASUREMENT, unitMeasurementId);
    }

    public String getUnitMeasurementName() {
        return this.getString(ListingFilterParameter.UNIT_MEASUREMENT);
    }

    public void setUnitMeasurementName(final String unitMeasurementName) {
        this.addString(ListingFilterParameter.UNIT_MEASUREMENT, unitMeasurementName);
    }

    public String getReasonCode() {
        return this.reasonCode;
    }

    public void setReasonCode(final String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public ArrayList<String> getOptions() {
        return this.options;
    }

    public void setOptions(final ArrayList<String> options) {
        this.options = options;
    }

    public Integer getJobFamilyID() {
        return this.getInteger(ListingFilterParameter.JOB_FAMILY_ID);
    }

    public void setJobFamilyID(final Integer jobFamilyID) {
        this.addInteger(ListingFilterParameter.JOB_FAMILY_ID, jobFamilyID);
    }

    public Integer getColumnMetadataId() {
        return this.getInteger(ListingFilterParameter.COLUMN_METADATA_ID);
    }

    public void setColumnMetadataId(final Integer columnMetadata) {
        this.addInteger(ListingFilterParameter.COLUMN_METADATA_ID, columnMetadata);
    }

    public void setIsGdn(final Boolean isGdn) {
        addBoolean(ListingFilterParameter.IS_GDN, isGdn);
    }

    public Boolean isGdn() {
        return Optional.ofNullable(getBoolean(ListingFilterParameter.IS_GDN)).orElse(false);
    }

    public String getPermissionCode() {
        return this.getString(ListingFilterParameter.PERMISSION_CODE);
    }

    public void setPermissionCode(final String permissionCode) {
        this.addString(ListingFilterParameter.PERMISSION_CODE, permissionCode);
    }

    public boolean isFavourite() {
        return this.getBool(ListingFilterParameter.IS_FAVOURITE);
    }

    public void setFavourite(final Boolean isFavourite) {
        addBoolean(ListingFilterParameter.IS_FAVOURITE, isFavourite);
    }

    public boolean isLetterSearch() {
        return this.getBool(ListingFilterParameter.IS_LETTER_SEARCH);
    }

    public void setLetterSearch(final Boolean isLetterSearch) {
        addBoolean(ListingFilterParameter.IS_LETTER_SEARCH, isLetterSearch);
    }

    public boolean isWidgetSearch() {
        return this.getBool(ListingFilterParameter.IS_WIDGET_SEARCH);
    }

    public void setWidgetSearch(final Boolean isWidgetSearch) {
        addBoolean(ListingFilterParameter.IS_WIDGET_SEARCH, isWidgetSearch);
    }

    public String getColOper() {
        return getString(ListingFilterParameter.COLOPER);
    }

    public void setColOper(final String colOper) {
        addString(ListingFilterParameter.COLOPER, colOper);
    }

    public Integer getAttachmentId() {
        return this.getInteger(ListingFilterParameter.ATTACHMENT_ID);
    }

    public void setAttachmentId(final Integer attachmentId) {
        addInteger(ListingFilterParameter.ATTACHMENT_ID, attachmentId);
    }

    public String getManualEntryType() {
        return manualEntryType;
    }

    public void setManualEntryType(String manualEntryType) {
        this.manualEntryType = manualEntryType;
    }

    public boolean isCopy() {
        return this.getBool(ListingFilterParameter.IS_COPY);
    }

    public void setCopy(final boolean value) {
        this.addBool(ListingFilterParameter.IS_COPY, value);
    }

    public void setIsOverpayment(final boolean value) {
        this.addBool(ListingFilterParameter.IS_OVERPAYMENT, value);
    }

    public boolean isOverpayment() {
        return this.getBool(ListingFilterParameter.IS_OVERPAYMENT);
    }

    public String getAccessToken() {
        return this.getString(ListingFilterParameter.ACCESS_TOKEN);
    }

    public void setAccessToken(final String accessToken) {
        this.addString(ListingFilterParameter.ACCESS_TOKEN, accessToken);
    }

    public boolean isWithBlockedAccount() {
        return withBlockedAccount;
    }

    public void setWithBlockedAccount(boolean withBlockedAccount) {
        this.withBlockedAccount = withBlockedAccount;
    }

    public boolean isPrepayment() {
        return prepayment;
    }

    public void setPrepayment(boolean prepayment) {
        this.prepayment = prepayment;
    }

    public String getCollapsed() {
        return this.collapsed;
    }

    public void setCollapsed(final String collapsed) {
        this.collapsed = collapsed;
    }

    public String getShownObjects() {
        return this.shownObjects;
    }

    public void setShownObjects(final String shownObjects) {
        this.shownObjects = shownObjects;
    }

    public boolean isBasedOnTimesheet() {
        return basedOnTimesheet;
    }

    public void setBasedOnTimesheet(boolean basedOnTimesheet) {
        this.basedOnTimesheet = basedOnTimesheet;
    }

    public boolean isAllDay() {
        return this.allDay;
    }

    public void setAllDay(final boolean allDay) {
        this.allDay = allDay;
    }

    public boolean isFromCase() {
        return isFromCase;
    }

    public void setFromCase(boolean fromCase) {
        isFromCase = fromCase;
    }

    public Integer getCalculateDepreciation() {
        return this.getInteger(ListingFilterParameter.CALCULATE_DEPRECIATION);
    }

    public void setCalculateDepreciation(final Integer calculateDepreciation) {
        addInteger(ListingFilterParameter.CALCULATE_DEPRECIATION, calculateDepreciation);
    }

    public boolean isCheckBeforeSelected() {
        return checkBeforeSelected;
    }

    public void setCheckBeforeSelected(boolean checkBeforeSelected) {
        this.checkBeforeSelected = checkBeforeSelected;
    }

    public Integer getBeforeSelectedId() {
        return beforeSelectedId;
    }

    public void setBeforeSelectedId(Integer beforeSelectedId) {
        this.beforeSelectedId = beforeSelectedId;
    }

    public boolean isHasAccessToChange() {
        return hasAccessToChange;
    }

    public void setHasAccessToChange(boolean hasAccessToChange) {
        this.hasAccessToChange = hasAccessToChange;
    }

    public boolean isFromProduct() {
        return isFromProduct;
    }

    public void setFromProduct(boolean fromProduct) {
        isFromProduct = fromProduct;
    }

    public boolean isHasFullListAccess() {
        return hasFullListAccess;
    }

    public void setHasFullListAccess(boolean hasFullListAccess) {
        this.hasFullListAccess = hasFullListAccess;
    }

    public boolean isShowView() {
        return isShowView;
    }

    public void setShowView(boolean showView) {
        isShowView = showView;
    }

    public boolean isLevelActive() {
        return isLevelActive;
    }

    public void setLevelActive(boolean levelActive) {
        isLevelActive = levelActive;
    }

    public Integer getLevelOptionList() {
        return levelOptionList;
    }

    public void setLevelOptionList(Integer levelOptionList) {
        this.levelOptionList = levelOptionList;
    }

    public Integer getLevelOptionListForSprvs() {
        return levelOptionListForSprvs;
    }

    public void setLevelOptionListForSprvs(Integer levelOptionList) {
        this.levelOptionListForSprvs = levelOptionList;
    }

    public Integer getShowMembersForOrgChart() {
        return showMembersForOrgChart;
    }

    public void setShowMembersForOrgChart(Integer showMembersForOrgChart) {
        this.showMembersForOrgChart = showMembersForOrgChart;
    }

    public Integer getShowAllSubMembersForOrgChart() {
        return showAllSubMembersForOrgChart;
    }

    public void setShowAllSubMembersForOrgChart(Integer showAllSubMembersForOrgChart) {
        this.showAllSubMembersForOrgChart = showAllSubMembersForOrgChart;
    }

    public Integer getDepartmentDoubleClickId() {
        return departmentDoubleClickId;
    }

    public void setDepartmentDoubleClickId(Integer departmentDoubleClickId) {
        this.departmentDoubleClickId = departmentDoubleClickId;
    }

    public String getBankAccountName() {
        return this.getString(ListingFilterParameter.BANK_ACCOUNT_NAME);
    }

    public void setBankAccountName(String bankAccountName) {
        this.addString(ListingFilterParameter.BANK_ACCOUNT_NAME, bankAccountName);
    }

    public String getBankAccountNumber() {
        return this.getString(ListingFilterParameter.BANK_ACCOUNT_NUMBER);
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.addString(ListingFilterParameter.BANK_ACCOUNT_NUMBER, bankAccountNumber);
    }

    public String getBankAccountCode() {
        return this.getString(ListingFilterParameter.BANK_ACCOUNT_CODE);
    }

    public void setBankAccountCode(String bankAccountCode) {
        this.addString(ListingFilterParameter.BANK_ACCOUNT_CODE, bankAccountCode);
    }

    public Integer getBankAccountCurrencyId() {
        return this.getInteger(ListingFilterParameter.BANK_ACCOUNT_CURRENCY_ID);
    }

    public void setBankAccountCurrencyId(Integer bankAccountCurrencyId) {
        this.addInteger(ListingFilterParameter.BANK_ACCOUNT_CURRENCY_ID, bankAccountCurrencyId);
    }

    public void setWithVacand(boolean withVacand) {
        this.withVacand = withVacand;
    }

    public boolean getWithVacand() {
        return withVacand;
    }

    public boolean isBasicPlusAllowancePaymentType() {
        return isBasicPlusAllowancePaymentType;
    }

    public void setBasicPlusAllowancePaymentType(boolean basicPlusAllowancePaymentType) {
        isBasicPlusAllowancePaymentType = basicPlusAllowancePaymentType;
    }

    public ArrayList<PaymentDeductionSelectItem> getPaymentCategories() {
        if (paymentCategories == null)
            paymentCategories = new ArrayList<>();
        return paymentCategories;
    }

    public void setPaymentCategories(ArrayList<PaymentDeductionSelectItem> paymentCategories) {
        this.paymentCategories = paymentCategories;
    }

    public boolean isPayment() {
        return isPayment;
    }

    public void setPayment(boolean payment) {
        isPayment = payment;
    }

    public String getPropertyCode() {
        return this.getString(ListingFilterParameter.PROPERTY_CODE);
    }

    public void setFromEmployeeProfile(boolean isFromEmployeeProfile) {
        this.isFromEmployeeProfile = isFromEmployeeProfile;
    }

    public boolean isFromEmployeeProfile() {
        return isFromEmployeeProfile;
    }

    public boolean isFromPartnerBackend() {
        return isFromPartnerBackend;
    }

    public void setFromPartnerBackend(boolean fromPartnerBackend) {
        isFromPartnerBackend = fromPartnerBackend;
    }

    public boolean isFromShift() {
        return isFromShift;
    }

    public void setFromShift(boolean fromShift) {
        isFromShift = fromShift;
    }

    public boolean isEnablePayments() {
        return enablePayments;
    }

    public void setEnablePayments(boolean enablePayments) {
        this.enablePayments = enablePayments;
    }

    public void setPropertyCode(String propertyCode) {
        this.addString(ListingFilterParameter.PROPERTY_CODE, propertyCode);
    }

    public boolean isFromMultiDepartment() {
        return fromMultiDepartment;
    }

    public void setFromMultiDepartment(boolean fromMultiDepartment) {
        this.fromMultiDepartment = fromMultiDepartment;
    }

    public boolean isOrderByDepartment() {
        return isOrderByDepartment;
    }

    public void setOrderByDepartment(boolean orderByDepartment) {
        isOrderByDepartment = orderByDepartment;
    }

    public boolean isOrderByPosition() {
        return isOrderByPosition;
    }

    public void setOrderByPosition(boolean orderByPosition) {
        isOrderByPosition = orderByPosition;
    }

    public boolean isVisableAll() {
        return isVisableAll;
    }

    public void setVisableAll(boolean visableAll) {
        this.isVisableAll = visableAll;
    }

    public boolean isThisMonthEmployees() {
        return isthisMonthEmployees;
    }

    public void setThisMonthEmployees(boolean thisMonthEmployees) {
        this.isthisMonthEmployees = thisMonthEmployees;
    }

    public boolean isIgnoreAllCurrencyValidation() {
        return ignoreAllCurrencyValidation;
    }

    public void setIgnoreAllCurrencyValidation(boolean ignoreAllCurrencyValidation) {
        this.ignoreAllCurrencyValidation = ignoreAllCurrencyValidation;
    }

    public Date getShiftPeriod() {
        return shiftPeriod;
    }

    public void setShiftPeriod(Date shiftPeriod) {
        this.shiftPeriod = shiftPeriod;
    }

    public boolean isAllByProjectGoal() {
        return allByProjectGoal;
    }

    public void setAllByProjectGoal(boolean allByProjectGoal) {
        this.allByProjectGoal = allByProjectGoal;
    }

    public boolean isFromPositionBulkUpdate() {
        return fromPositionBulkUpdate;
    }

    public void setFromPositionBulkUpdate(boolean fromPositionBulkUpdate) {
        this.fromPositionBulkUpdate = fromPositionBulkUpdate;
    }

    public boolean isFromDepartmentBulkUpdate() {
        return fromDepartmentBulkUpdate;
    }

    public void setFromDepartmentBulkUpdate(boolean fromDepartmentBulkUpdate) {
        this.fromDepartmentBulkUpdate = fromDepartmentBulkUpdate;
    }

    public boolean isCalculateByLastMonth() {
        return this.getBoolean(ListingFilterParameter.CALCULATE_BY_LAST_MONTH) == null || this.getBoolean(ListingFilterParameter.CALCULATE_BY_LAST_MONTH);
    }

    public void setCalculateByLastMonth(final boolean calculateByLastMonth) {
        this.addBoolean(ListingFilterParameter.CALCULATE_BY_LAST_MONTH, calculateByLastMonth);
    }

    public boolean isApplyForSubDepartment() {
        return isApplyForSubDepartment;
    }

    public void setApplyForSubDepartment(boolean applyForSubDepartment) {
        isApplyForSubDepartment = applyForSubDepartment;
    }

    public boolean isFromCandidate() {
        return isFromCandidate;
    }

    public void setFromCandidate(boolean fromCandidate) {
        isFromCandidate = fromCandidate;
    }

    public boolean isItemTable() {
        return itemTable;
    }

    public void setItemTable(boolean itemTable) {
        this.itemTable = itemTable;
    }

    public ArrayList<Integer> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(ArrayList<Integer> locationIds) {
        this.locationIds = locationIds;
    }
    public Boolean getFromTerminal() {
        return isFromTerminal != null ? isFromTerminal : Boolean.FALSE;
    }

    public void setFromTerminal(Boolean fromTerminal) {
        isFromTerminal = fromTerminal;
    }
}
