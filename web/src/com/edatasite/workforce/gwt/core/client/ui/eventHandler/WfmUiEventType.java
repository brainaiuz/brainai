package com.edatasite.workforce.gwt.core.client.ui.eventHandler;

/**
 * User: Abdulaziz
 * Date: Sep 26, 2009
 * Time: 7:18:51 PM
 */
public class WfmUiEventType {
    /*
     * Fires when user adds new task
     */
    public static final int ON_TASK_ADD = 1;

    /*
     * Fires wehn user adds new project
     */
    public static final int ON_PROJECT_ADD = 2;

    /*
     * Fires when user adds new Employee
     */
    public static final int ON_EMPLOYEE_ADD = 3;
    /*
     *Fires when new contact added
     */
    public static final int ON_CONTACT_ADD = 4;
    /*
     *Fires when new issue added
     */
    public static final int ON_ISSUE_ADD = 5;

    /*
     *Fires when new demartment added
     */
    public static final int ON_DEPARTMENT_ADD = 6;

    /*
     *Fires when new Client added
     */
    public static final int ON_CLIENT_ADD = 7;

    /*
     *Fires when new Workstream added
     */
    public static final int ON_WORKSTREAM_ADD = 8;
    /*
     *Fires when new PROJECT MEMBER added
     */
    public static final int ON_PROJECT_MEMBER_ADD = 9;

    /*
     *Fires when new Goal added
     */
    public static final int ON_GOAL_ADD = 10;

    /*
     *Fires when new Company Goal added
     */
    public static final int ON_COMPANY_GOAL_ADD = 11;
    /*
     *Fires when update employee profile
     */
    public static final int ON_EMPLOYEE_PROFILE_UPDATE = 12;

    /*
     *Fires when new News added
     */
    public static final int ON_NEWS_ADD = 13;

    /*
     *Fires when new Event added in Calendar
     */
    public static final int ON_CALENDAR_EVENT_ADD = 14;

    /*
     *Fires when new Contact delete in data base
     */
    public static final int ON_CONTACT_DELETE = 15;

    /*
     *Fires when new Note delete in data base
     */
    public static final int ON_NOTE_DELETE = 16;

    /*
     *Fires when new Note delete in data base
     */
    public static final int ON_NEWS_DELETE = 17;

    /*
     * Fires when search is performed
     */
    public static final int ON_SEARCH_CLICKED = 18;

    /*
     * Fires when task edit
     */
    public static final int ON_TASK_EDIT = 19;

    /*
     * Fires when project edit
     */
    public static final int ON_PROJECT_EDIT = 20;

    /*
     * Fires when project edit
     */
    public static final int ON_LOCATION_ADD = 21;
    /*
     * Fires when project edit
     */
    public static final int ON_LOCATION_EDIT = 22;

    /*
     * Fires when task delete
     */
    public static final int ON_TASK_DELETE = 23;

    /*
     * Fires when project delete
     */
    public static final int ON_PROJECT_DELETE = 24;
    /*
     * Fires when salary grade add
     */
    public static final int ON_SALARY_GRADE_ADD = 25;

    /*
     * Fires when news comments add
     */
    public static final int ON_NEWS_COMMENTS_ADD = 26;

    /*
     * Fires when department edit
     */
    public static final int ON_DEPARTMENT_EDIT = 27;

    /*
     * Fires when dependent add/edit
     */
    public static final int ON_DEPENDENT_ADD_EDIT = 28;

    /*
     * Fires when past employment add/edit
     */
    public static final int ON_PASTEMPLOYMENT_ADD_EDIT = 29;

    /*
     * Fires when award add/edit
     */
    public static final int ON_AWARD_ADD_EDIT = 30;
    /*
     * Fires when award add/edit
     */
    public static final int ON_EDUCATION_ADD_EDIT = 31;
    /*
     * Fires when position add/edit
     */
    public static final int ON_POSITION_ADD_EDIT = 32;
    /*
     * Fires when job description add/edit
     */
    public static final int ON_JOBDESCRIPTION_ADD_EDIT = 33;
    /*
     * Fires when payment/deduction add
     */
    public static final int ON_PAYMENT_DEDUCTION_ADD = 34;
    /*
     * Fires when payment/deduction category add
     */
    public static final int ON_PAYMENT_DEDUCTION_CATEGORY_ADD = 35;
    /*
     * Fires when template delete
     */
    public static final int ON_TEMPLATE_ADD_DELETE = 36;
    /*
     * Fire when new Leave Request added
     */
    public static final int ON_LEAVER_REQUEST_ADD = 37;
    /*
     * Fires when  Leave Request deleted
     */
    public static final int ON_LEAVER_REQUEST_DELETE = 38;

    /*
     * Fires when pension provider add
     */
    public static final int ON_ADD_PENSION_PROVIDER = 39;

    /*
     * Fires when pension scheme add
     */
    public static final int ON_ADD_PENSION_SCHEME = 40;
    /*
     * Fires when position delete
     */
    public static final int ON_POSITION_DELETE = 41;
    /*
     * Fires when position delete
     */
    public static final int ON_GRADE_DELETE = 42;
    /*
     *Fires when new Goal added
     */
    public static final int ON_GOAL_DELETE = 43;
    /*
     *Fires when demartment delete
     */
    public static final int ON_DEPARTMENT_DELETE = 44;
    /*
     *Fires when Case added
     */
    public static final int ON_CASE_ADD = 45;
    /*
     *Fires when Case deleted
     */
    public static final int ON_CASE_DELETE = 46;
    /*
     * Fire when  Leave Request approved
     */
    public static final int ON_LEAVER_REQUEST_APPROVED = 47;
    /*
     * Fire when  Leave Request rejected
     */
    public static final int ON_LEAVER_REQUEST_REJECTED = 48;
    /*
     * Fire when Activities (Task) add and edit
     */
    public static final int ON_CRM_TASK_ADD_EDIT = 49;
    /*
     * Fire when Activities (Event) add and edit
     */
    public static final int ON_CRM_EVENT_ADD_EDIT = 50;
    /*
     *  Fire when new Lead added or Lead is updated
     */
    public static final int ON_LEADS_ADD_EDIT = 51;
    /*
     *  Fire when new Lead added
     */
    public static final int ON_LEADS_DELETE = 52;
    /*
     *  Fire when new Accounts added or Account
     */
    public static final int ON_ACCOUNTS_ADD_EDIT = 53;
    /*
     *  Fire when new Opportunity added
     */
    public static final int ON_OPPORTUNITY_ADD_EDIT = 54;
    /*
     * Fire when new Campaign added or Campaign is updated
     */
    public static final int ON_CAMPAIGN_ADD_EDIT = 55;
    /*
     *  Fire when new Solution added or Solution is updated
     */
    public static final int ON_SOLUTION_ADD_EDIT = 56;
    /*
     *Fires when Payrun Payslip generated
     */
    public static final int ON_PAYRUNPAYSLIP_GENERATE = 57;
    /*
     *Fires when Payrun Payment included
     */
    public static final int ON_PAYRUNINCLUDE_PAYMENTS = 58;
    /*
     *Fires when Payrun Payslip saved
     */
    public static final int ON_PAYRUNPAYSLIP_SAVED = 59;

    public static final int ON_SKILL_CHANGED = 60;
    /*
     * Fires when user deletes Employee
     */
    public static final int ON_EMPLOYEE_DELETE = 61;

    /*
     * Fires when user timesheet submitted for approval
     */
    public static final int ON_TIMESHEET_SUBMIT_FOR_APPROVAL = 62;

    /*
     * Fires when user timesheet approval
     */
    public static final int ON_TIMESHEET_APPROVAL = 63;
    /*
     * Fires when user added MailList
     */
    public static final int ON_MAIL_LIST_ADD = 64;
    /*
     * Fires when user edited MailList
     */
    public static final int ON_MAIL_LIST_EDIT = 65;
    /*
     * Fires when user add Message
     */
    public static final int ON_MESSAGE_ADD = 66;
    /*
     * Fires when user edit Message
     */
    public static final int ON_MESSAGE_EDIT = 67;
    /*
     *Fires when new Supplier added
     */
    public static final int ON_SUPPLIER_ADD = 68;

    /*
     *Fires when documents upload files
     */
    public static final int ON_DOCUMENTS_UPLOAD_FILES = 69;
    /*
     *Fires when new Sale Invoice added
     */
    public static final int ON_SALEINVOICE_ADDED = 70;
    /*
     *Fires when new Account saved
     */
    public static final int ON_ACCOUNT_SAVED = 71;
    /*
     *Fires when new Purchase Invoice added
     */
    public static final int ON_PURCHASEINVOICE_ADDED = 72;
    /*
     *Fires when new Purchase Order added
     */
    public static final int ON_PURCHASEORDER_ADDED = 73;
    /*
     *Fires when new Product/Service saved
     */
    public static final int ON_PRODUCTSERVICE_SAVED = 74;
    /*
     *Fires when new Sale Quote added
     */
    public static final int ON_SALEQUOTE_ADDED = 75;
    /*
     *Fires when new Bank Account saved
     */
    public static final int ON_BANKACCOUNT_SAVED = 76;
    /*
     *Fires when new Tax saved
     */
    public static final int ON_TAX_SAVED = 77;
    /*
     *Fires when new Expense added
     */
    public static final int ON_EXPENSEREPORT_SAVED = 78;
    /*
     *Fires when timeslot added
     */
    public static final int ON_TIMESLOT_ADD = 79;
    /*
     *Fires when timeslot edited
     */
    public static final int ON_TIMESLOT_EDIT = 80;
    /*
     *Fires when timeslot deleted
     */
    public static final int ON_TIMESLOT_DELETE = 81;
    /**
     * Fires when simple appraisal initiated
     */
    public static final int ON_SIMPLE_APPRAISAL_INITIATED = 82;
    /**
     * Fires when 360 appraisal initiated
     */
    public static final int ON_360_APPRAISAL_INITIATED = 83;
    /*
     *Fires when Invoice void
     */
    public static final int ON_INVOICE_VOID = 84;
    /*
     *Fires when client edited
     */
    public static final int ON_CLIENT_EDIT = 85;
    /*
     *Fires when client edited
     */
    public static final int ON_SUPPLIER_EDIT = 86;
    /*
     *Fires when money transferred
     */
    public static final int ON_MONEY_TRANSFER = 87;
    /*
     * Fires when past employment add/edit
     */
    public static final int ON_INTERNALEMPLOYMENT_ADD_EDIT = 88;
    /*
     * Fires when past employment add/edit
     */
    public static final int ON_INVOICEPAYMENT_CHANGE = 89;
    /*
     *Fires when note added
     */
    public static final int ON_NOTE_EDIT = 90;

    /*
     *Fires when note added
     */
    public static final int ON_NOTE_ADD_ID_RETURNED = 91;

    /*
     *Fires when CRM ACCOUNT DELETED
     */
    public static final int ON_CRM_ACCOUNT_DELETED = 92;

    /*
     *  Fire when new Solution deleted
     */
    public static final int ON_SOLUTION_DELETED = 93;

    /*
     *  Fire when new Opportunity deleted
     */
    public static final int ON_OPPORTUNITY_DELETED = 94;

    /*
     * Fire when new Campaign deleted
     */
    public static final int ON_CAMPAIGN_DELETED = 95;

    /*
     * Fire when Activities (Task) deleted
     */
    public static final int ON_CRM_ACTIVITY_DELETED = 96;

    /*
     * Fire when Activities (Task) deleted
     */
    public static final int ON_CRM_TASK_DELETED = 97;

    /*
     * Fire when Activities (Event) deleted
     */
    public static final int ON_CRM_EVENT_DELETED = 98;

    /*
     * Fire when new holiday added
     */
    public static final int ON_HOLIDAY_ADD = 99;

    /*
     * Fire when new holiday edited
     */
    public static final int ON_HOLIDAY_EDIT = 100;

    /*
     * Fire when new holiday deleted
     */
    public static final int ON_HOLIDAY_DELETED = 101;
    /*
     *Fires when new note for contact added
     */
    public static final int ON_NOTE_FOR_CONTACT_ADD = 102;

    /*
     * Fire when email template added
     */
    public static final int ON_EMAIL_TEMPLATE_ADD = 103;
    /*
     * Fire when email template added
     */
    public static final int ON_EMAIL_TEMPLATE_EDIT = 104;
    /*
     *Fires when new custom field added
     */
    public static final int ON_CUSTOM_FIELD_ADD = 105;

    /*
     *Fires when new Account deleted
     */
    public static final int ON_ACCOUNT_DELETED = 106;

    /*
     *Fires when client deleted
     */
    public static final int ON_CLIENT_DELETED = 107;

    /*
     *Fires when new note for contact added
     */
    public static final int ON_NOTE_FOR_CRM_DELETE = 108;

    /*
     *Fires when new note for contact added
     */
    public static final int ON_NOTE_FOR_CRM_ADD = 109;

    /*
     *Fires when workstream deleted
     */
    public static final int ON_WORKSTREAM_DELETED = 110;

    /*
     *Fires when supplier deleted
     */
    public static final int ON_SUPPLIER_DELETED = 111;

    /*
     *Fires when manual transaction saved
     */
    public static final int ON_MANUAL_TRANSACTION_SAVED = 112;

    /*
     * Fires when case reply to reporter
     */
    public static final int ON_CASE_REPLY_TO_REPORTER = 113;

    /*
     * Fires when note added
     */
    public static final int ON_NOTE_ADD = 114;


    /*
     *Fires when warehouse saved
     */
    public static final int ON_WAREHOUSE_SAVED = 115;

    /*
     *Fires when Unit Measurement saved
     */
    public static final int ON_UNITMEASUREMENT_SAVED = 116;

    /*
     *Fires when Product Category saved
     */
    public static final int ON_PRODUCTCATEGORY_SAVED = 117;

    /*
     *Fires when new incident added
     */
    public static final int ON_INCIDENT_ADD = 118;

    /*
     *Fires when new performance note added
     */
    public static final int ON_PERFORMANCE_NOTE_ADD = 119;

    public static final int ON_PURCHASEORDER_RECEIVED = 120;

    public static final int ON_WAREHOUSELOCATION_SAVED = 121;

    /*
     * Fires when task members edit
     */
    public static final int ON_TASK_MEMBERS_EDIT = 122;
    /*
     * Fires when task members edit
     */
    public static final int ON_MAIL_COMPOSE = 123;

    public static final int ON_NETWORK_ADD = 124;

    public static final int ON_NETWORK_BLOG_ADD = 125;

    public static final int ON_NETWORK_LOGO_UPDATE = 126;

    public static final int ON_JOINED_TO_NETWORK = 127;

    public static final int ON_NETWORK_CONTACT_ADD = 128;

    public static final int ON_NETWORK_BLOG_DELETE = 129;

    /*
     * Fires when group add/edit
     */
    public static final int ON_GROUP_ADD_EDIT = 130;

    /*
     * Fires when group delete
     */
    public static final int ON_GROUP_DELETE = 131;

    /*
     * Fires when storefront saved
     */
    public static final int ON_STOREFRONT_SAVED = 132;

    public static final int ON_TRANSACTION_RECONCILED = 133;

    /*
     * Fires when add task documents
     */
    public static final int ON_TASK_DOCUMENTS_ADD = 134;

    public static final int ON_SHIPPINGMETHOD_SAVED = 135;

    public static final int ON_BRAND_SAVED = 136;

    public static final int ON_SALEORDER_SHIPPED = 137;

    public static final int ON_SAVE_AS_DRAFT = 138;

    /*
     * Fires when issue delete
     */
    public static final int ON_ISSUE_DELETE = 139;

    /**
     * Fires when timesheet: task status change
     */
    public static final int ON_TIMESHEET_TASK_STATUS_CHANGED = 140;

    public static final int ON_BANK_STATEMENTS_SAVED = 141;

    public static final int ON_NETWORK_BLOG_POSTED = 142;

    public static final int ON_RECONCILESTATUS_CHANGED = 143;

    public static final int ON_WEB_FORM_ADD_EDIT = 144;

    public static final int ON_WEB_FORM_DELETE = 145;

    public static final int ON_CONFIRM_NETWORK_JOIN_INVITATION = 146;

    public static final int ON_CONFIRM_PEER_TO_PEER_CONTACT_INVITATION = 147;

    public static final int ON_VAT_RETURN_REPORT_EFILED = 148;

    public static final int ON_CHOOSE_WORKSTREAM_PARENT_CLOSED = 149;

    /**
     * Fires when contact image added
     */
    public static final int ON_CONTACT_IMAGE_ADD = 150;
    /**
     * Fires when company usage plan added/edited
     */
    public static final int ON_USAGE_PLAN_ADD_EDIT = 151;

    public static final int ON_DEPENDENT_DELETE = 152;

    /**
     * Fires when sale quote converted to sale order
     */
    public static final int ON_SALEQUOTE_CONVERTED_TO_SALEORDER = 153;
    /*
     * Fire when email template lists added
     */
    public static final int ON_EMAIL_TEMPLATES_LIST_ADD = 154;

    public static final int ON_SALESORDER_ADDED = 155;

    /**
     * Fires when Robert's request approved
     */
    public static final int ON_RQUEST_APPROVED = 156;
    /**
     * Fires when Robert's request rejected
     */
    public static final int ON_RQUEST_REJECTED = 157;
    /**
     * Fires when Robert's request deleted
     */
    public static final int ON_RQUEST_DELETED = 158;

    /**
     * Fires when directory saved
     */

    public static final int ON_DIRECTORY_SAVED = 159;

    public static final int ON_WEBSITE_SAVED = 160;

    public static final int ON_WEBSITE_LAYOUT_SAVED = 161;

    public static final int ON_PAGE_LAYOUT_SAVED = 162;

    public static final int ON_WIDGET_SAVED = 163;

    public static final int ON_BLOCK_SAVED = 164;

    public static final int ON_PAGE_SAVED = 165;

    public static final int ON_PAGE_BLOCK_SAVED = 166;

    public static final int ON_WEBSITE_LAYOUT_BLOCK_SAVED = 167;

    public static final int ON_WEBSITE_MENU_SAVED = 168;

    /**
     * Fires when payslip saved or updated
     */
    public static final int ON_PAYSLIP_SAVED = 169;

    public static final int ON_SCHEMA_ADD = 170;

    public static final int ON_NEWS_CATEGORY_SAVED = 171;

    public static final int ON_CONTACT_CATEGORY_ADD = 172;

    public static final int ON_DISCOUNT_SAVED = 173;

    /**
     * Fires when contact career added or edited
     */
    public static final int ON_CONTACT_CAREER_ADD_EDIT = 174;

    /**
     * Fires when contact career deleted
     */
    public static final int ON_CONTACT_CAREER_DELETE = 175;

    /**
     * Fires when contact account add
     */
    public static final int ON_CONTACT_ACCOUNT_ADD = 176;

    /**
     * Fires when past employment delete
     */
    public static final int ON_PASTEMPLOYMENT_DELETE = 177;

    public static final int ON_CONTACT_DETAILS_UPDATE = 178;

    /**
     * Fires when award delete
     */
    public static final int ON_AWARD_DELETE = 179;

    /**
     * Fires when education delete
     */
    public static final int ON_EDUCATION_DELETE = 180;

    /**
     * Fires when user image upload add
     */
    public static final int ON_USER_IMAGE_UPLOAD_ADD = 181;

    // Project Status related constants
    public static final int ON_PROJECT_STATUS_ADD = 182;

    public static final int ON_PROJECT_STATUS_EDIT = 183;
    public static final int ON_PROJECT_STATUS_DELETE = 184;
    public static final int ON_PROJECT_STATUS_IMPORT = 185;
    public static final int ON_RESERVATION_SAVED = 186;

    public static final int ON_PRICE_LEVEL_SAVED = 187;
    /**
     * Fires when saved email notification settings
     */
    public static final int ON_SAVE_COMPANY_EMAIL_NOTIFICATIONS = 188;

    public static final int ON_360_APPRAISAL_REVIEWED_AND_SUBMITTED = 189;

    /**
     * Fires when attendance tracking report import file
     */
    public static final int ON_IMPORT_FILE_FOR_ATTENDANCE_TRACKING_REPORT = 190;

    public static final int ON_XTEMPLATE_SAVED = 191;

    /**
     * Fires when pdf template added or updated
     */
    public static final int ON_PDF_TEMPLATE_SAVED = 192;

    /**
     * Fires when fixed asset added/updated or depreciations posted
     */
    public static final int ON_FIXED_ASSET_SAVED = 193;

    /*
     * Fires wehn user adds new sub project
     */
    public static final int ON_SUB_PROJECT_ADD = 194;
    /*
     *Fires when new Sub PROJECT MEMBER added
     */
    public static final int ON_SUB_PROJECT_MEMBER_ADD = 195;
    /*
     * Fires when sub project edit
     */
    public static final int ON_SUB_PROJECT_EDIT = 196;
    /*
     * Fires when sub project delete
     */
    public static final int ON_SUB_PROJECT_DELETE = 197;
    public static final int ON_ADD_RELATION_TO_MAIL = 198;

    /**
     * Fires when user adds new Competency
     */
    public static final int ON_ADD_COMPETENCY = 199;

    public static final int ON_EMAIL_LIST_CHANGE = 200;

    public static final int ON_ADD_RELATION = 201;

    /**
     * Fires when case forwarded
     */
    public static final int ON_CASE_FORWARDED = 202;

    public static final int ON_EXPENSE_VOID = 203;
    public static final int ON_QUERY_ADD_EDIT = 204;

    public static final int ON_DISCOUNT_DELETED = 205;

    public static final int ON_REPORT_TEMPLATE_ADD_EDIT = 206;

    public static final int ON_DASHBOARD_ADD = 207;

    public static final int ON_DASHLET_ADD = 208;

    public static final int ON_DASHBOARD_DOWNLOAD_LINK_ADD = 209;

    public static final int ON_DASHBOARD_DELETE = 210;

    public static final int ON_DASHLET_DELETE = 211;

    public static final int ON_DASHBOARD_DOWNLOAD_LINK_DELETE = 212;

    public static final int ON_SALE_INVOICE_ADD_CREDIT_NOTE = 213;

    public static final int ON_PURCHASE_INVOICE_ADD_CREDIT_NOTE = 214;

    //    Custom Entity Types
    public static final int ON_CUSTOM_ENTITY_ADD_EDIT = 215;

    public static final int ON_CUSTOM_ENTITY_FIELD_ADD_EDIT = 216;

    public static final int ON_FILTER_DELETED = 217;

    public static final int ON_FILTER_ADDED = 218;

    public static final int ON_CUSTOM_ENTITY_FORM_ADD_EDIT = 219;

    public static final int ON_CONVERTED_LEAD_WITH_OPPORTUNITY = 220;

    public static final int ON_PREPAYMENT_SAVE = 221;

    public static final int ON_FS_SAVE = 222;

    public static final int ON_REPLYPANEL_SELECT = 223;

    public static final int ON_BEFORE_REMOVE_TAB = 224;

    public static final int ON_REMOVE_TAB = 225;

    public static final int ON_SELECTION_CHANGE_TAB = 226;

    public static final int ON_CUSTOM_LIST_ADD_EDIT = 227;

    public static final int ON_CUSTOM_LIST_ITEM_ADD_EDIT = 228;

    public static final int ON_SITE_MAP_SAVED = 229;

    public static final int ON_CUSTOM_ENTITY_EMAIL_TEMPLATE_ADD_EDIT = 230;

    /**
     * Fires when simple/360 appraisal delete
     */
    public static final int ON_APPRAISAL_DELETE = 231;

    public static final int ON_SAVED_REPORT_LIST_REFRESH = 232;

    /**
     * Fires when skill weight change listener
     */
    public static final int ON_SKILL_WEIGHT_CHANGED = 233;
    /**
     * Fires when skill overall competency/goal ratio changed
     */
    public static final int ON_SKILL_COMPETENCY_GOAL_RATIO_CHANGED = 234;

    public static final int ON_SELECTION_LOOKUPCELL = 235;

    public static final int ON_CELL_LISTENER_ADDED = 236;

    public static final int ON_THEME_SAVED = 237;
    /**
     * Fires when backend options added
     */
    public static final int ON_BACKEND_OPTIONS_ADD = 238;

    public static final int ON_BANK_CHECK_SAVED = 239;

    public static final int ON_BANK_CHECK_DELETED = 240;

    public static final int ON_REFERENCE_ADD = 241;
    public static final int ON_SINKS_CONTAINER_VIEW_SELECTED = 242;

    public static final int ON_BOOKING_ITEMS_ADD = 243;
    public static final int ON_BOOKING_ITEMS_EDIT = 244;
    public static final int ON_BOOKING_ITEMS_SAVED = 245;
    public static final int ON_BOOKING_ITEMS_RESERVATION_SAVED = 246;

    public static final int ON_EXPENSE_PAYMENT_DELETE = 247;
    public static final int ON_ROLE_ADD = 248;
    public static final int ON_ROLE_ADD_PERMISSION_CHANGE = 249;
    public static final int ON_MEETING_MINUTES_DELETED = 252;
    public static final int ON_BOOKING_RELATION_OPENED = 253;

    public static final int ON_VACANCY_ADDED = 254;

    /**
     * Fires when meeting minutes add/edit/saved
     */
    public static final int ON_MEETING_MINUTES_ADD = 255;
    public static final int ON_MEETING_MINUTES_EDIT = 256;
    public static final int ON_MEETING_MINUTES_SAVED = 257;
    public static final int ON_OVERTIME_CHANGE = 258;

    /**
     * Fires when placement add/edit
     */
    public static final int ON_PLACEMENT_ADD_EDIT = 259;
    /**
     * Fires when placement delete
     */
    public static final int ON_PLACEMENT_DELETE = 260;
    /**
     * Fires when candidate add/edit
     */
    public static final int ON_CANDIDATE_ADD_EDIT = 261;
    /**
     * Fires when candidate delete
     */
    public static final int ON_CANDIDATE_DELETE = 262;
    /**
     * Fires when candidate selected
     */
    public static final int ON_CANDIDATE_SELECT = 263;

    /**
     * Fires when public event create
     */
    public static final int ON_PUBLIC_EVENT_CREATE = 264;
    /**
     * Fires when vacancy delete
     */
    public static final int ON_VACANCY_DELETE = 265;
    /**
     * Fires when web form save
     */
    public static final int WEB_FORM_SAVED = 266;
    /**
     * Fires when captcha is not entered.
     */
    public static final int CAPTCHA_IS_EMPTY_ERROR = 267;
    /**
     * Fires when captcha must be added to Form.
     */
    public static final int CAPTCHA_ADD_TO_FORM = 268;
    /**
     * Fires when request for quote save
     */
    public static final int ON_REQUEST_FOR_QUOTE_ADDED = 269;
    /**
     * Fires when invoice term save
     */
    public static final int ON_INVOICE_TERM_SAVED = 270;
    /**
     * Fires when invoice term save
     */
    public static final int ON_LEAD_STATUS_CHANGED = 271;

    public static final int ON_VALIDITY_PERIOD_CHANGED = 272;
    public static final int ON_PERIOD_APPRAISAL_CHANGED = 273;

    /**
     * Fires when vacancy matched
     */
    public static final int ON_VACANCY_MATCHED = 274;

    /**
     * Fires when onboarding add/edit
     */
    public static final int ON_ONBOARDING_STEP_ADD_EDIT = 275;

    /**
     * Fires when onboarding add/edit
     */
    public static final int ON_ONBOARDING_PERIOD_ADD_EDIT = 276;

    /**
     * Fires when onboarding delete
     */
    public static final int ON_ONBOARDING_PERIOD_DELETE = 277;

    /**
     * Fires when onboarding stage delete
     */
    public static final int ON_ONBOARDING_STEP_DELETE = 278;

    /**
     * Fires when competency deleted
     */
    public static final int ON_COMPETENCY_DELETE = 279;

    /**
     * Fires when course add/edit/delete
     */
    public static final int ON_COURSE_DELETE = 280;
    public static final int ON_COURSE_ADD_EDIT = 281;
    /**
     * Fires when student add/edit
     */
    public static final int ON_STUDENT_ADD_EDIT = 282;
    /**
     * Fires when student delete
     */
    public static final int ON_STUDENT_DELETE = 283;
    /**
     * Fires when student add
     */
    public static final int ON_ENQUIRY_ADD_EDIT = 284;
    /**
     * Fires when student add
     */
    public static final int ON_ENQUIRY_DELETED = 285;

    public static final int ON_SCHEDULED_COURSE_SAVED = 286;
    /**
     * Fires when remove student from course
     */
    public static final int ON_COURSE_STUDENT_ADD_EDIT = 287;
    /**
     * Fires when register student to course
     */
    public static final int ON_COURSE_STUDENT_DELETE = 288;

    /**
     * Fires when register student to course
     */
    public static final int ON_COURSE_BOOKING_ADD_EDIT = 289;

    /**
     * Fires when register student to course
     */
    public static final int ON_COURSE_BOOKING_DELETE = 290;

    public static final int ON_TRAINING_CONTRACT_ADD_EDIT = 291;
    /**
     * Fires when training contract add/edit
     */

    public static final int ON_TRAINING_CONTRACT_DELETE = 292;
    /**
     * Fires when certificate issued
     */
    public static final int ON_CERTIFICATE_SAVED = 293;

    /**
     * Fires when bonus distributed
     */
    public static final int ON_BONUS_DISTRIBUTED = 294;

    /**
     * Fires generate project number
     */
    public static final int ON_ADD_PROJECT_NUMBER = 295;
    /**
     * Consolidation Company add
     */
    public static final int ON_CONSOLIDATION_COMPANY_ADD = 296;
    /**
     * Fires when performance note delete
     */
    public static final int ON_PERFORMANCE_NOTE_DELETE = 297;
    /**
     * Fires when incident delete
     */
    public static final int ON_INCIDENT_DELETE = 298;

    public static final int ON_SALE_QUOTE_DELETED = 299;

    public static final int ON_CRM_ACCOUNT_IMAGE_ADD = 300;

    public static final int ON_SUPPLIER_OPENNING_BALANCE_TRANSACTION_DELETE = 301;

    public static final int ON_CERTIFICATE_DELETE = 302;
    /**
     * Fires when budget and incumbents edit
     */
    public static final int ON_BUDGET_AND_INCUMBENTS_EDIT = 303;

    public static final int ON_FILE_UPLOAD_STARTED = 304;
    public static final int ON_FILE_UPLOAD_FINISHED = 305;

    public static final int ON_COURSE_SUBJECT_DELETE = 306;

    public static final int ON_ADD_COURSE_SUBJECT = 307;
    /**
     * Fires when add to black list
     */
    public static final int ON_ADD_TO_BLACK_LIST = 308;
    /**
     * Fires when custom form add
     */
    public static final int ON_CUSTOM_FORM_ADD = 309;
    /**
     * Fires when multiple approval request update
     */
    public static final int ON_MULTIPLE_APPROVAL_REQUEST_UPDATE = 310;
    /*
     * Fires when internal employment delete
     */
    public static final int ON_INTERNAL_EMPLOYMENT_DELETE = 311;
    /*
     * Fires when signature add
     */
    public static final int ON_SIGNATURE_ADD = 312;

    /*
     * Fires when stock adjustment saved
     */
    public static final int ON_STOCK_ADJUSTMENT_SAVED = 313;

    /**
     * Fires when help document add/edit
     */
    public static final int ON_HELP_DOCUMENT_FORM_ADD = 314;

    public static final int ON_HELP_DOCUMENT_DELETE = 315;

    public static final int ON_MENU_GROUP_SAVED = 316;

    /**
     * Firres when Request for purchase saved;
     */
    public static final int ON_REQUEST_FOR_PURCHASE_ADD_EDIT = 317;

    public static final int ON_REQUEST_FOR_PURCHASE_DELETE = 318;

    public static final int ON_TRASH_BIN_SAVED = 319;
    /*
     *Fires when new issue added
     */
    public static final int ON_MAILLIST_DELETED = 320;

    /**
     * Fires when punishments/promotions add/edit
     */
    public static final int ON_PUNISHMENTS_PROMOTIONS_ADD_EDIT = 321;
    /**
     * Fires when punishments/promotions delete
     */
    public static final int ON_PUNISHMENTS_PROMOTIONS_DELETE = 322;
    /**
     * Fires when employee punishments/promotions add/edit
     */
    public static final int ON_EMPLOYEE_PUNISHMENTS_PROMOTIONS_ADD_EDIT = 323;
    /**
     * Fires when employee punishments/promotions delete
     */
    public static final int ON_EMPLOYEE_PUNISHMENTS_PROMOTIONS_DELETE = 324;

    public static final int ON_LOCALIZATION_EDITED = 325;

    /*
     *  Fires when Bank Account Transaction deleted
     * */
    public static final int ON_BANK_ACCOUNT_TRANSACTION_DELETED = 326;

    /**
     * Fires when bonus recommendations add/edit
     */
    public static final int ON_BONUS_RECOMMENDATIONS_ADD_EDIT = 327;
    /**
     * Fires when bonus recommendations delete
     */
    public static final int ON_BONUS_RECOMMENDATIONS_DELETE = 328;
    /**
     * Fires when bonus recommendations approve
     */
    public static final int ON_BONUS_RECOMMENDATIONS_APPROVE = 329;
    /**
     * Fires when bonus recommendations reject
     */
    public static final int ON_BONUS_RECOMMENDATIONS_REJECT = 330;
    /**
     * Fires when tender card add/edit
     */
    public static final int ON_TENDER_CARD_ADD_EDIT = 331;
    /**
     * Fires when tender card delete
     */
    public static final int ON_TENDER_CARD_DELETE = 332;
    /**
     * Fires when tender card application add
     */
    public static final int ON_TENDER_CARD_APPLICATION_ADD = 333;
    /**
     * Fires when estimate of contract add/edit
     */
    public static final int ON_ESTIMATE_OF_CONTRACT_ADD_EDIT = 334;
    /**
     * Fires when estimate of contract delete
     */
    public static final int ON_ESTIMATE_OF_CONTRACT_DELETE = 335;

    public static final int ON_APPLY_STOREFRONT_TEMPLATE = 336;

    public static final int ON_ADD_NEW_CAMPAIGN = 337;

    public static final int ON_WORKFLOW_UPDATE = 338;

    public static final int ON_WORKFLOW_DELETE = 339;

    public static final int ON_WORKFLOW_ADD = 340;

    public static final int ON_WORKFLOW_ALERT_ADD = 341;

    public static final int ON_WORKFLOW_ALERT_DELETE = 342;
    public static final int ON_WORKFLOW_ALERT_UPDATE = 343;

    public static final int ON_TASK_PREDECESSOR_CHANGE = 344;

    public static final int ON_EOS_CALCULATION_SAVE = 345;
    public static final int ON_WORKFLOW_UPDATE_FIELD_ADD = 346;
    public static final int ON_WORKFLOW_UPDATE_FIELD_REMOVE = 347;
    public static final int ON_WORKFLOW_UPDATE_FIELD_UPDATE = 348;

    public static final int ON_PASSPORT_SAVED = 349;
    public static final int ON_PASSPORT_DELETE = 350;
    public static final int ON_CASH_SAVED = 351;
    public static final int ON_ADD_REPORTING_FAVOURITY = 352;
    public static final int ON_REMOVE_REPORTING_FAVOURITY = 353;

    public static final int ADD_OR_EDIT_CERTIFICATE = 354;
    public static final int DELETE_CERTIFICATE = 355;
    public static final int UPDATE_CERTIFICATE_TYPE = 356;

    public static final int DELETE_CERTIFICATE_TYPE = 357;
    public static final int ADD_OR_EDIT_CERTIFICATE_TYPE = 358;

    /**
     * Views collapse icon button clicked
     */
    public static final int ON_SPLIT_LAYOUT_PANEL_COLLAPSE_CHANGED = 359;
    public static final int ON_PAGE_CATEGORY_SAVED = 360;
    public static final int ON_CONTACT_AUTO_ACCOUNT_ADDED = 361;
    public static final int ON_OPPORTUNITY_AUTO_ACCOUNT_ADDED = 362;

    public static final int ON_REPORTING_FOLDER_SAVED = 363;
    public static final int ON_STOCK_TRANSFER_SAVED = 364;

    public static final int ON_SMS_TEMPLATE_ADD_EDIT = 365;
    public static final int ON_SMS_TEMPLATE_DELETE = 366;

    public static final int ON_SMS_ALERT_ADD_EDIT = 367;
    public static final int ON_SMS_ALERT_DELETE = 368;

    public static final int ON_BANK_TRANSFER_LIST_UPDATE = 369;
    public static final int ON_CONSIGNMENT_UPDATE = 370;

    public static final int ADD_OR_EDIT_BENEFIT = 371;
    public static final int DELETE_BENEFIT = 372;

    public static final int ON_PENSION_PROVIDER_ADD = 373;
    public static final int ON_EMPLOYER_SETTINGS_UPDATE = 374;
    public static final int ON_EMPLOYEE_STEP_ADD_EDIT_DELETE = 375;
    public static final int ON_SERIAL_NUMBER_IMPORT = 376;
    public static final int ON_EMPLOYEE_BANK_UPDATE = 377;
    public static final int ON_EMPLOYEE_BENEFIT_ALLOWANCE = 378;
    public static final int ON_WORKFLOW_EMPLOYEE_STEP_UPDATE = 379;

    public static final int ON_NOTIFICATION_MSG_LIST_UPDATE = 380;

    public static final int ON_BENEFIT_REQUEST_ADD = 381;
    public static final int ON_BENEFIT_REQUEST_DELETE = 382;

    public static final int ON_MONTHLY_TIMESHEET_ADD = 383;
    public static final int ON_MANUAL_TRANSACTION_DELETED = 384;
    public static final int ON_ASSIGN_EMPLOYEE_TO_PROJECT = 385;

    public static final int ON_CONTRACT_ADD = 386;
    public static final int ON_CONTRACT_EDIT = 387;
    public static final int ON_CONTRACT_DELETE = 388;

    public static final int ON_DOUBLE_DISCOUNT_SAVED = 389;

    public static final int ON_NOTIFICATION_MSG_CHANGE_ENTITY = 390;

    public static final int ON_EMPLOYEE_FILES_UPLOADED = 391;
    public static final int ON_FILE_DELETE = 392;
    public static final int ON_COMPANY_DOC_LISTING_EDIT = 393;
    public static final int ON_EMPLOYEE_DOC_LISTING_EDIT = 394;
    public static final int ON_ACCOUNTING_DASHBOARD_CUSTOMIZE = 395;

    public static final int ON_WORKFLOW_PUSH_NOTIFICATION_CHANGE = 396;

    public static final int ON_WORKFLOW_ACTIVITIES_UPDATE = 397;
    /*
     * Fires when Bank Statement Item add/edit
     */
    public static final int ON_BANK_STATEMENT_ITEM_CHANGE = 398;
    public static final int ON_WORKFLOW_ACTIVITIES_DELETE = 399;

    public static final int ON_WORKFLOW_ACTIVITIES_ADD = 400;
    public static final int ON_PAYROLL_EMPLOYEE_TEMPLATE_SAVED = 401;
    public static final int ON_EMPLOYEE_SELF_SERVICE_CUSTOMIZE = 402;

    public static final int ON_MANAGER_SELF_SERVICE_CUSTOMIZE = 403;

    public static final int ON_COUNTRY_SETTINGS_ADD = 404;
    /*
     * Fires when Attendance Tracking status change to in/out/lunch
     */
    public static final int ON_ATTENDANCE_TRACKING_STATUS_CHANGE = 405;
    /*
     * Fires when payroll batch add
     */
    public static final int ON_PAYROLL_BATCH_ADD = 406;

    public static final int ON_SMS_SETTINGS_ADD_EDIT = 407;
    /*
     * Fires when Currency Exchange Rate add/edit
     * */
    public static final int ON_EXCHANGE_RATE_ADDED = 408;

    public static final int ON_EMAIL_ACCOUNT_MODIFICATION = 409;
    public static final int ON_EMAIL_FOLDERS_MODIFICATION = 410;
    public static final int ON_BOOKING_ITEM_DELETE = 411;
    public static final int ON_EMPLOYE_LIST_EDIT_CELL = 412;
    /**
     * Fired when payment method is manipulated
     */
    public static final int ON_PAYMENT_METHOD_ADD = 413;

    public static final int ON_PAYMENT_METHOD_EDIT = 414;
    public static final int ON_PAYMENT_METHOD_DELETE = 415;
    /**
     * Fired when contactUsButton in Supervisor Structure view is clicked.
     */
    public static final int ON_FEEDBACK_BUTTON_CLICKED = 416;
    public static final int ON_WORKFLOW_EMPLOYEE_MODIFICATION = 417;
    public static final int ON_FINGERPRINT_SETUP_ADD = 418;
    /**
     * Fired when close Sales Quote view tab
     */
    public static final int ON_SALES_QUOTE_TAB_CLOSE = 419;

    /**
     * Fires when request for quote converted
     */
    public static final int ON_REQUEST_FOR_QUOTE_CONVERTED = 421;

    public static final int ON_CASH_REJECTED = 422;
    public static final int ON_PAYMENT_TO_BANK_ACCOUNT = 423;

    public static final int ON_EXPENSE_DELETED = 424;

    public static final int ON_EXPENSE_DECLINED = 425;
    public static final int ON_EXPENSE_CLOSED = 426;
    public static final int ON_EXPENSE_APPROVED = 427;
    public static final int ON_WORKFLOW_INVOICE_MODIFICATION = 428;
    public static final int ON_LOAD_LOGGED_TIMES = 429;
    public static final int ON_APPROVERS_LOADED = 430;
    public static final int ON_STEP_EXPENSEREPORT_SAVED = 431;

    /**
     * Fires when additional payment is manipulated
     */
    public static final int ON_ADDITIONAL_PAYMENT_ADD = 432;
    public static final int ON_ADDITIONAL_PAYMENT_EDIT = 433;
    public static final int ON_ADDITIONAL_PAYMENT_DELETE = 434;

    public static final int ON_EMPLOYE_TREE_WIDGET_REFRESH = 435;
    public static final int ON_LOAD_ITEM_TABLE_COLUMN_CONFIGS = 436;

    public static final int ON_WORKFLOW_ACTIONS_ADD = 437;
    public static final int ON_WORKFLOW_ACTIONS_REMOVE = 438;
    public static final int ON_WORKFLOW_ACTIONS_UPDATE = 439;
    public static final int ON_LEAVE_REQUEST_STATUS_CHANGED = 440;

    public static final int ON_DELETE_ABSTRACTADDCUSTOMFIELDSVIEW = 441;

    public static final int ON_EXPENSE_SUBMITTED = 443;

    public static final int ON_TELEGRAM_CHAT_EDIT = 444;

    public static final int ON_BENEFIT_REQUEST_UPDATE = 445;

    public static final int ON_SIDENAV_RESIZE = 446;

    public static final int ON_TIME_LOGGED = 447;

    public static final int ON_MODULE_DASHBOARD_ADD = 448;

    public static final int ON_USER_OWN_IMAGE_UPLOAD_ADD = 449;

    public static final int ON_TWILIO_SETTINGS_ADD_EDIT = 450;

    public static final int ON_PHONE_CALLED = 451;

    public static final int REMOVE_REPORT = 452;

    public static final int ON_DASHBOARD_SETTINGS_CLICK = 453;

    public static final int ON_DASHBOARD_TASK_REFRESH = 454;

    public static final int ON_DASHBOARD_PROJECT_REFRESH = 455;

    public static final int ON_REPORTING_COLUMN_CHANGE = 456;

    public static final int REPORTING_SEARCH_ICON_VISIBLE = 457;
    /*
     * Fires when user starts/stops timer
     */
    public static final int ON_TIMER_STARTED = 458;

    /*
     *  Fires When Reporting report saved
     */
    public static final int REPORTING_REPOT_SAVED = 459;

    public static final int REPORT_FILTER_CHANGED = 460;

    public static final int ON_DOCUMENT_GROUPS_POPUP_CLOSED = 461;

    public static final int CONNECTION_LOST = 462;

    public static final int CONNECTED = 463;

    public static final int ON_CONTACT_CATEGORY_DELETE = 464;

    public static final int ON_TALENT_PROFILE_CHANGE = 465;

    public static final int ON_SIDE_NAV_POSITION_CHANGE = 466;

    public static final int ON_ADD_TAB = 467;

    public static final int ON_SELECT_TAB = 468;

    public static final int UPDATE_TAB_TITLE = 469;

    public static final int ON_SETTINGS_PDF_TEMPLATE_ADD_EDIT = 473;

    public static final int PICKLIST_RELOAD_PAGE = 474;

    public static final int PURCHASE_ORDER_SUMMARY_RELOAD_PAGE = 475;

    public static final int ON_EMAIL_CHANGE_ENTITY = 476;


    public static final int ON_LEAVE_REASON_UPDATE = 477;

    public static final int ON_EMAILS_CLEARED = 478;

    public static final int ON_TASK_STATUS_CHANGES = 479;

    public static final int ON_RELOAD_DASHBOARD_COMPONENTS = 48000;

    public static final int ON_ASSAMBLY_ITEM_TOTAL_CHANGE = 481;

    public static final int ON_BILLOFENTRY_CREATED = 482;

    public static final int ON_PRODUCT_IMPORT_RELOAD_PAGE = 483;

    public static final int ON_CUSTOMER_IMPORT_RELOAD_PAGE = 484;

    public static final int ON_SUPPLIER_IMPORT_RELOAD_PAGE = 485;

    public static final int ON_ORGANIZE_MODULE_RELOAD_PAGE = 486;

    public static final int ON_VAT_RETURN_FILE_CHANGED = 487;

    public static final int ON_CUSTOM_FORM_BUILD = 488;

    public static final int ON_CUSTOM_FORM_ITEM_UPDATE = 489;

    public static final int ON_DRAG_END = 490;

    public static final int PI_DELETE_FIXEDASSET_RELOAD = 491;

    public static final int FIXEDASSET_DELETE_PI_LIST_RELOAD = 492;

    public static final int ON_GANTT_CHART_COLUMN_SETTINGS_CHANGE = 493;

    public static final int ON_CUSTOM_FORM_SAVE_BUTTON_ENABLE = 494;

    public static final int ON_CUSTOM_FORM_ITEM_APPROVAL = 495;

    public static final int ON_SALES_INVOICE_APPROVAL = 496;

    public static final int ON_GROUP_GOAL_ADD = 497;
    public static final int ON_GROUP_GOAL_APPROVED = 498;
    public static final int ON_GROUP_GOAL_REMOVED = 499;
    public static final int ON_GROUP_GOAL_CLOSE = 500;

    public static final int ON_PURCHASE_INVOICE_APPROVAL = 501;

    public static final int ON_SHIPPING_DATA_ADDED = 502;

    public static final int ON_SALEORDER_APPROVAL = 503;

    public static final int ON_EMAIL_ACCOUNT_FAILURE = 504;
    public static final int ON_EMAIL_ACCOUNT_SUCCESS = 505;

    public static final int ON_GDN_GRN_LIST_RELOAD = 506;

    public static final int ON_SALEORDER_BASE_INVOICE_LOADED = 507;

    public static final int ON_AFTER_LOG_A_CALL_SET_VALUES = 508;

    public static final int ON_ASTERISK_SETTINGS_ADD_EDIT = 509;

    public static final int ON_ASTERISK_EMPLOYEE_ADD_EDIT = 510;

    public static final int ON_OPPORTUNITY_LIST_RELOAD = 511;

    public static final int ON_TELEGRAM_SETTINGS_ADD_EDIT = 512;

    public static final int ON_TELEGRAM_ALERT_ADD_EDIT = 513;

    public static final int ON_RELATED_CONVERTED_FORM = 514;

    public static final int ON_ADD_COMPETENCY_GROUP = 515;

    public static final int ON_OPPORTUNITY_LOAD_STAGE_HISTORY = 516;

    public static final int ON_CASE_LOAD_STAGE_HISTORY = 517;

    public static final int ON_PERMISSION_HISTORY_LIST_RELOAD = 518;

    public static final int ON_BUDGET_SHEET_UPDATE = 519;

    public static final int ON_CHANGE_BUDGET_MANAGERS = 520;

    public static final int ON_WIDGET_DATA_RECEIVED = 521;

    public static final int ON_BUDGET_SHEET_RELOAD_COLUMNS = 522;

    public static final int ON_CONVERSION_BALANCE_RELOAD_PAGE = 523;

    public static final int ON_RENTAL_PRODUCT_ADDED = 524;

    public static final int ON_TIMER_ADDED = 525;

    public static final int ON_RENTAL_ORDER_ADDED = 526;

    public static final int ON_RENTAL_ORDER_CALCULATE_MIN_PRICE = 527;

    public static final int ON_TELEGRAM_REPORTING_RULE_SAVE = 528;

    public static final int ON_WORKFLOW_WEB_HOOK_ADD = 437;
    public static final int ON_WORKFLOW_WEB_HOOK_REMOVE = 438;
    public static final int ON_WORKFLOW_WEB_HOOK_UPDATE = 439;
    public static final int ON_LEAVE_DAYS_INSERTED = 440;
    public static final int ON_LEAVE_DAYS_VALIDATED = 441;

    public static final int ON_PAYRUN_PAYMENT_ADD = 529;
    public static final int ON_PAYRUN_PAYMENT_DELETE = 530;

    /* Should be fired whenever the leave request calendar needs to redraw itself, e.g. when changing month */

    public static final int ON_CALENDAR_DRAW = 531;

    public static final int ON_PAYROLL_PAYMENT_ADD = 532;
    public static final int ON_PAYROLL_PAYMENT_DELETE = 533;
    public static final int ON_TASK_LOAD_STATUS_HISTORY = 534;
    public static final int ON_TASK_LIST_EDIT_CELL = 535;
    public static final int ON_LOCALIZATION_ADD = 536;
    public static final int ON_LOCALIZATION_ADD_FOR_SKILL_NAME = 547;
    public static final int ON_LOCALIZATION_ADD_FOR_SKILL_DESCRIPTION = 548;

    public static final int ON_SHIFT_ADD = 537;
    public static final int ON_SHIFT_DELETE = 538;
    public static final int ON_DYNAMIC_LOGIN_ADD_EDIT = 539;

    public static final int ON_SHIFT_SETTINGS_ADD = 540;
    public static final int ON_SHIFT_SETTINGS_EDIT = 541;
    public static final int ON_SHIFT_SETTINGS_DELETE = 542;

    public static final int ON_ROTATION_ADD = 543;
    public static final int ON_ROTATION_DELETE = 544;
    public static final int ON_SALE_INVOICE_SENDED_TO_ZATCA = 545;

    public static final int ON_REPORT_FILTER_ADD = 546;

    public static final int ON_GROUP_PLACEMENT_ADD = 547;
    public static final int ON_GROUP_PLACEMENT_DELETE = 548;
    public static final int ON_ASSASSMET_DELETED = 549;

    public static final int ON_WHITE_LABEL_ADD_EDIT = 550;
    public static final int ON_VACANCY_REJECTED_OR_APROVED = 551;
    public static final int ON_SIPUNI_SETTINGS_ADD_EDIT = 552;
    public static final int ON_TRANSACTION_LOCKING_EVENT = 553;
    public static final int ON_PAYROLL_RECURRING_PD_ADD = 554;
    public static final int ON_PAYROLL_RECURRING_PD_DELETE = 555;

    public static final int ON_OVERTIME_DELETE = 556;
    public static final int ON_OVERTIME_ADD = 557;

    public static final int ON_BACKUPS_EMPLOYEE_ADD = 558;
    public static final int ON_BACKUPS_EMPLOYEE_DELETE = 559;
    public static final int ON_MY_CALLS_SETTINGS_ADD_EDIT = 560;
    public static final int ON_SALARY_ADD = 561;
    public static final int ON_SALARY_DELETE = 562;

    public static final int ON_MESSAGE_RECEIVED = 563;

    public static final int ON_WHATSAPP_ATTACHMENT_ATTACHED = 564;
    public static final int ON_PAYROLL_ZONE_ADD = 565;
    public static final int ON_MINIMUM_WAGE_ADD = 566;
    public static final int ON_WAGE_RATE_ADD = 567;
    public static final int ON_MATERIAL_AID_ADD = 568;
    public static final int ON_PUSH_NOTIFICATION_POPUP = 569;
    public static final int ON_CHECKING_CHECKOUT_NOTIFICATION = 600;
    public static final int ON_INVOICE_LINE_ITEM_FOCUS = 601;
    public static final int ON_WHATSAPP_TEMPLATE_ADD_EDIT= 602;
    public static final int ON_WHATSAPP_TEMPLATE_DELETE= 603;

    public static final int ON_BANK_TRANSFER_ADD= 604;
    public static final int ON_PRODUCT_QTY_CHANGE = 605;
    public static final int ON_INVOICE_APPROVED = 606;
    public static final int ON_SALES_INVOICE_CONVERT_AND_ADD = 607;
    public static final int ON_RENTAL_ORDER_DELETE = 608;
    public static final int ON_PREPAYMENT_ADDED_FROM_RENTAL_ORDER = 609;
    public static final int ON_TIMESHEET_INVOICE_ADDED = 610;
    public static final int ON_EMPLOYEE_ADDED_TO_DEPARTMENT = 611;
    public static final int ON_SIDE_NAV_CLOSED = 612;
    public static final int ON_DEPARTMETN_GOAL_METRIC_HISTORY_ADD = 613;
    public static final int ON_ORG_BOARD_SETTINGS_UPDATED = 614;
    public static final int ON_ATTENDANCE_TERMINAL_ADD = 615;
    public static final int ON_ATTENDANCE_TERMINAL_EDIT = 616;
    public static final int EXPORT_TO_EXCEL_COMPLETED = 617;
    public static final int ON_CANDIDATE_ADD = 618;
}
