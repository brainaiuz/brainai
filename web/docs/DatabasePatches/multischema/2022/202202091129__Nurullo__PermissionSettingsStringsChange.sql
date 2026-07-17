update permission
set context='PAYROLL'
where code = 'SETTINGS_EMPLOYEE_LIST';


update permission
set parent=0
where code = 'SETTINGS_MAIN_MENU';

update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_COMPANY_SETTINGS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_PROFILE_SETTINGS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_USER_CREDENTIALS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_ACCOUNTING_SETTINGS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'ACCOUNTING_CURRENCY_RATES_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'ACCOUNTING_PRICE_LEVELS_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'ACCOUNTING_DISCOUNTS_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'ACCOUNTING_UNIT_MEASUREMENTS_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'ACCOUNTING_PRODUCT_CATEGORIES_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'ACCOUNTING_BRANDS_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'ACCOUNTING_SHIPPING_METHODS_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'ACCOUNTING_PAYMENT_METHOD_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'ACCOUNTING_ACCOUNT_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'ACCOUNTING_PRODUCT_TABLE_SETTINGS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_CRM_SETTINGS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_HRMS_SETTINGS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'REFERENCE_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'TIMESLOT_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'HOLIDAY_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_APPRAISAL_SETTINGS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_VALIDITY_PERIODS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'BENEFIT_TYPE';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'HRMS_DEPARTMENT';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'HRMS_POSITION';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'HRMS_LOCATION';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'HRMS_COUNTRY_SETTINGS_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'NOTIFICATION_CONFIG_EVENT';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'CETIFICATE_TEMPLATE_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'HRMS_COMPANY_NEWS_CATEGORIES';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'HRMS_VIEW_EMPLOYEE_CHANGE_LOG';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_BENEFIT_ALLOWANCE';

update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_PROJECT_MANAGEMENT_SETTINGS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'PAYROLL_SETTINGS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'PAYROLL_SETTINGS_EMPLOYER_SETTINGS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'PAYROLL_GROUP_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'PAYROLL_PAYMENT_DEDUCATION_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'PAYROLL_SETTINGS_PENSION_PROVIDERS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'PAYROLL_SETTINGS_PENSION_SCHEMES';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'PAYROLL_SETTINGS_PAYMENT_CATEGORIES';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_DASHBOARD_LIST';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_CUSTOMIZATION';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'CUSTOM_FIELD_SETTINGS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_RECURRENCE_SETTINGS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_WORKFLOW';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'ADD_SYSTEM_FILTER';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_EMAIL_SETTINGS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'PERMISSION_MANAGEMENT';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_INTEGRATION';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SYSTEM_LOGS';
update permission
set parent=(select id from permission where code = 'SETTINGS_MAIN_MENU')
where code = 'SETTINGS_HRMS_SETTINGS_PUNISHMENTS_PROMOTIONS';


/* parent */
update permission
set sorder=1
where code = 'SETTINGS_COMPANY_SETTINGS';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'SETTINGS_COMPANY_SETTINGS'),
    name='Disable IP restriction'
where code = 'ACCESS_FOR_IP';

/* Parent*/
update permission
set sorder=2
where code = 'SETTINGS_PROFILE_SETTINGS';

/* Parent*/
update permission
set sorder=3
where code = 'SETTINGS_USER_CREDENTIALS';

/* Parent*/
update permission
set sorder=4
where code = 'SETTINGS_ACCOUNTING_SETTINGS';
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'SETTINGS_ACCOUNTING_SETTINGS'),
    name='Invoice Settings'
where code = 'ACCOUNTING_INVOICE_SETTINGS';
/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'SETTINGS_ACCOUNTING_SETTINGS'),
    name='Financial Settings'
where code = 'ACCOUNTING_FINANCIAL_SETTINGS';
/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'SETTINGS_ACCOUNTING_SETTINGS'),
    name='Account Numbering'
where code = 'ACCOUNTING_ACCOUNT_NUMBERING';
/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'SETTINGS_ACCOUNTING_SETTINGS'),
    name='Numbering Settings'
where code = 'ACCOUNTING_NUMBERING_SETTINGS';
/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'SETTINGS_ACCOUNTING_SETTINGS'),
    name='Tax Rates'
where code = 'ACCOUNTING_TAX_RATES_LIST';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'SETTINGS_ACCOUNTING_SETTINGS'),
    name='Accounts Terms'
where code = 'ACCOUNTING_TERMS_LIST';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'SETTINGS_ACCOUNTING_SETTINGS'),
    name='Conversion Balance'
where code = 'ACCOUNTING_CONVERSION_BALANCE';


/* Parent*/
update permission
set sorder=5
where code = 'ACCOUNTING_CURRENCY_RATES_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_CURRENCY_RATES_LIST'),
    name='Edit'
where code = 'ACCOUNTING_CURRENCY_RATE_EDIT';
/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_CURRENCY_RATES_LIST'),
    name='Multi company exchange rate'
where code = 'ACCOUNTING_SETTINGSE_EXCHANGE_RATE';

/* Parent*/
update permission
set sorder=6
where code = 'ACCOUNTING_PRICE_LEVELS_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_PRICE_LEVELS_LIST'),
    name='Add'
where code = 'ACCOUNTING_PRICE_LEVEL_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_PRICE_LEVELS_LIST'),
    name='Edit'
where code = 'ACCOUNTING_PRICE_LEVEL_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_PRICE_LEVELS_LIST'),
    name='Delete'
where code = 'ACCOUNTING_PRICE_LEVEL_DELETE';


/* Parent*/
update permission
set sorder=8
where code = 'ACCOUNTING_DISCOUNTS_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_DISCOUNTS_LIST'),
    name='Add'
where code = 'ACCOUNTING_DISCOUNT_ADD';

/* Parent*/
update permission
set sorder=9
where code = 'ACCOUNTING_UNIT_MEASUREMENTS_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_UNIT_MEASUREMENTS_LIST'),
    name='Add'
where code = 'ACCOUNTING_UNIT_MEASUREMENTS_ADD';

/* Parent*/
update permission
set sorder=10
where code = 'ACCOUNTING_PRODUCT_CATEGORIES_LIST';
/* Parent*/
update permission
set sorder=11
where code = 'ACCOUNTING_BRANDS_LIST';
/* Parent*/
update permission
set sorder=12
where code = 'ACCOUNTING_SHIPPING_METHODS_LIST';
/* Parent*/
update permission
set sorder=13
where code = 'ACCOUNTING_PAYMENT_METHOD_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_PAYMENT_METHOD_LIST'),
    name='Add'
where code = 'ACCOUNTING_PAYMENT_METHOD_ADD';
/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_PAYMENT_METHOD_LIST'),
    name='Edit'
where code = 'ACCOUNTING_PAYMENT_METHOD_EDIT';
/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_PAYMENT_METHOD_LIST'),
    name='Delete'
where code = 'ACCOUNTING_PAYMENT_METHOD_DELETE';

/* Parent*/
update permission
set sorder=14
where code = 'ACCOUNTING_PRODUCT_TABLE_SETTINGS';

/* Parent*/
update permission
set sorder=15
where code = 'ACCOUNTING_ACCOUNT_LIST';
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_ACCOUNT_LIST'),
    name='Add'
where code = 'ACCOUNTING_ACCOUNT_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_ACCOUNT_LIST'),
    name='Edit'
where code = 'ACCOUNTING_ACCOUNT_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_ACCOUNT_LIST'),
    name='Delete'
where code = 'ACCOUNTING_ACCOUNT_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_ACCOUNT_LIST'),
    name='Summary'
where code = 'ACCOUNTING_ACCOUNT_SUMMARY';

/* Parent*/
update permission
set sorder=16
where code = 'SETTINGS_CRM_SETTINGS';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'SETTINGS_CRM_SETTINGS'),
    name='Company email settings'
where code = 'SETTINGS_COMPANY_EMAL_SETTINGS';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'SETTINGS_CRM_SETTINGS'),
    name='Contact Categories'
where code = 'CRM_CONTACT_CATEGORY_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'CRM_CONTACT_CATEGORY_LIST'),
    name='Add'
where code = 'CRM_CONTACT_CATEGORY_ADD';
/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'CRM_CONTACT_CATEGORY_LIST'),
    name='Edit'
where code = 'CRM_CONTACT_CATEGORY_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'CRM_CONTACT_CATEGORY_LIST'),
    name='Delete'
where code = 'CRM_CONTACT_CATEGORY_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'CRM_CONTACT_CATEGORY_LIST'),
    name='Share'
where code = 'CRM_CONTACT_CATEGORY_SHARE';

/* Parent*/
update permission
set sorder=17
where code = 'SETTINGS_HRMS_SETTINGS';

/* Parent*/
update permission
set sorder=18,
    name='Leave Reasons'
where code = 'REFERENCE_LIST';
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'REFERENCE_LIST'),
    name='Add'
where code = 'REFERENCE_ADD';
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'REFERENCE_LIST'),
    name='Edit'
where code = 'REFERENCE_EDIT';
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'REFERENCE_LIST'),
    name='Delete'
where code = 'REFERENCE_DELETE';

/* Parent*/
update permission
set sorder=19
where code = 'TIMESLOT_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'TIMESLOT_LIST'),
    name='Add'
where code = 'SETTINGS_HRMS_SETTINGS_ADD_TIMESLOT';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'TIMESLOT_LIST'),
    name='Edit'
where code = 'SETTINGS_HRMS_SETTINGS_EDIT_TIMESLOT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'TIMESLOT_LIST'),
    name='Delete'
where code = 'SETTINGS_HRMS_SETTINGS_DELETE_TIMESLOT';

/* Parent*/
update permission
set sorder=20
where code = 'HOLIDAY_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'HOLIDAY_LIST'),
    name='Add'
where code = 'SETTINGS_HRMS_SETTINGS_ADD_HOLIDAY';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'HOLIDAY_LIST'),
    name='Edit'
where code = 'SETTINGS_HRMS_SETTINGS_EDIT_HOLIDAY';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'HOLIDAY_LIST'),
    name='Delete'
where code = 'SETTINGS_HRMS_SETTINGS_DELETE_HOLIDAY';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'HOLIDAY_LIST'),
    name='Leave allowances'
where code = 'HRMS_ANNUAL_ALLOWANCE';

/* Parent*/
update permission
set sorder=21
where code = 'SETTINGS_APPRAISAL_SETTINGS';

/* Parent*/
update permission
set sorder=22
where code = 'SETTINGS_VALIDITY_PERIODS';

/* Parent*/
update permission
set sorder=23
where code = 'BENEFIT_TYPE';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'BENEFIT_TYPE'),
    name='Add'
where code = 'BENEFIT_TYPE_ADD';

/* Parent*/
update permission
set sorder=24,
    name='Departments'
where code = 'HRMS_DEPARTMENT';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'HRMS_DEPARTMENT'),
    name='Add'
where code = 'HRMS_ADD_NEW_DEPARTMENT';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'HRMS_DEPARTMENT'),
    name='Edit'
where code = 'HRMS_EDIT_DEPARTMENT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'HRMS_DEPARTMENT'),
    name='Delete'
where code = 'HRMS_DEPARTMENT_REMOVE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'HRMS_DEPARTMENT'),
    name='Summary'
where code = 'HRMS_DEPARTMENT_SUMMARY_VIEW';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'HRMS_DEPARTMENT'),
    name='See All departments'
where code = 'HRMS_SEE_ALL_DEPARTMENT_LIST';

/* Parent*/
update permission
set sorder=25
where code = 'HRMS_POSITION';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'HRMS_POSITION'),
    name='Rate'
where code = 'HRMS_POSITION_RATES';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'HRMS_POSITION'),
    name='Add'
where code = 'HRMS_ADD_NEW_POSITION';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'HRMS_POSITION'),
    name='Edit'
where code = 'HRMS_POSITION_EDIT';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'HRMS_POSITION'),
    name='Delete'
where code = 'HRMS_POSITION_REMOVE';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'HRMS_POSITION'),
    name='Summary'
where code = 'HRMS_POSITION_SUMMARRY';

/* Parent*/
update permission
set sorder=26
where code = 'HRMS_LOCATION';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'HRMS_LOCATION'),
    name='Add'
where code = 'HRMS_ADD_NEW_LOCATION';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'HRMS_LOCATION'),
    name='Edit'
where code = 'HRMS_EDIT_LOCATION';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'HRMS_LOCATION'),
    name='Delete'
where code = 'HRMS_REMOVE_LOCATION';

/* Parent*/
update permission
set sorder=27,
    name='Country Settings'
where code = 'HRMS_COUNTRY_SETTINGS_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'HRMS_COUNTRY_SETTINGS_LIST'),
    name='Add/Edit'
where code = 'HRMS_COUNTRY_SETTINGS_ADD_EDIT';


/* Parent*/
update permission
set sorder=28
where code = 'NOTIFICATION_CONFIG_EVENT';

/* Parent*/
update permission
set sorder=29,
    name  = 'Certificate Templates'
where code = 'CETIFICATE_TEMPLATE_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'CETIFICATE_TEMPLATE_LIST'),
    name='Add'
where code = 'CETIFICATE_TEMPLATE_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'CETIFICATE_TEMPLATE_LIST'),
    name='Edit'
where code = 'CETIFICATE_TEMPLATE_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'CETIFICATE_TEMPLATE_LIST'),
    name='Delete'
where code = 'CETIFICATE_TEMPLATE_DELETE';

/* Parent*/
update permission
set sorder=30
where code = 'HRMS_COMPANY_NEWS_CATEGORIES';

/* Parent*/
update permission
set sorder=31
where code = 'HRMS_VIEW_EMPLOYEE_CHANGE_LOG';

/* Parent*/
update permission
set sorder=32
where code = 'SETTINGS_BENEFIT_ALLOWANCE';

/* Parent*/
update permission
set sorder=33
where code = 'SETTINGS_PROJECT_MANAGEMENT_SETTINGS';

/* Parent*/
update permission
set sorder=34
where code = 'PAYROLL_SETTINGS';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'PAYROLL_SETTINGS'),
    name='Delete'
where code = 'PAYROLL_SETTINGS_EMPLOYER_SETTINGS';


/* Parent*/
update permission
set sorder=35
where code = 'PAYROLL_SETTINGS_EMPLOYER_SETTINGS';

/* Parent*/
update permission
set sorder=36,
    name  = 'Payroll Groups'
where code = 'PAYROLL_GROUP_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'PAYROLL_GROUP_LIST'),
    name='Add'
where code = 'PAYROLL_GROUP_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'PAYROLL_GROUP_LIST'),
    name='Edit'
where code = 'PAYROLL_GROUP_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'PAYROLL_GROUP_LIST'),
    name='Delete'
where code = 'PAYROLL_GROUP_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'PAYROLL_GROUP_LIST'),
    name='See All'
where code = 'PAYROLL_GROUP_SEE_ALL';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'PAYROLL_GROUP_LIST'),
    name='See Own'
where code = 'PAYROLL_GROUP_SEE_OWN';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'PAYROLL_GROUP_LIST'),
    name='Full Access'
where code = 'PAYROLL_GROUP_FULL_ACCESS';

/* Parent*/
update permission
set sorder=37
where code = 'PAYROLL_PAYMENT_DEDUCATION_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'PAYROLL_PAYMENT_DEDUCATION_LIST'),
    name='Add'
where code = 'PAYROLL_PAYMENT_DEDUCATION_ADD';


/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'PAYROLL_PAYMENT_DEDUCATION_LIST'),
    name='View'
where code = 'PAYROLL_PAYMENT_DEDUCATION_VIEW';

/* Parent*/
update permission
set sorder=38
where code = 'PAYROLL_SETTINGS_PENSION_PROVIDERS';

/* Parent*/
update permission
set sorder=39
where code = 'PAYROLL_SETTINGS_PENSION_SCHEMES';

/* Parent*/
update permission
set sorder=40
where code = 'PAYROLL_SETTINGS_PAYMENT_CATEGORIES';

/* Parent*/
update permission
set sorder=41
where code = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES'),
    name='Add'
where code = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES'),
    name='Edit'
where code = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES'),
    name='Delete'
where code = 'PAYROLL_SETTINGS_DEDUCATION_CATEGORIES_DELETE';

/* Parent*/
update permission
set sorder=42,
    name  = 'DASHBOARDS'
where code = 'SETTINGS_DASHBOARD_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'SETTINGS_DASHBOARD_LIST'),
    name='Add'
where code = 'SETTINGS_DASHBOARD_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'SETTINGS_DASHBOARD_LIST'),
    name='Edit'
where code = 'SETTINGS_DASHBOARD_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'SETTINGS_DASHBOARD_LIST'),
    name='Delete'
where code = 'SETTINGS_DASHBOARD_DELETE';


/* Parent*/
update permission
set sorder=43,
    name  = 'CUSTOMIZATION'
where code = 'SETTINGS_CUSTOMIZATION';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'SETTINGS_CUSTOMIZATION'),
    name='Reference'
where code = 'SETTINGS_CUSTOMIZATION_REFERENCE';

/* Parent*/
update permission
set sorder=44
where code = 'CUSTOM_FIELD_SETTINGS';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'CUSTOM_FIELD_SETTINGS'),
    name='Edit disabled field'
where code = 'CUSTOM_FIELD_DISABLED_FIELD';

/* Parent*/
update permission
set sorder=45
where code = 'SETTINGS_EMAIL_SETTINGS';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'SETTINGS_EMAIL_SETTINGS'),
    name='Email Templates'
where code = 'SETTINGS_EMAIL_TEMPALTE_LIST';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'SETTINGS_EMAIL_SETTINGS'),
    name='Signature'
where code = 'SETTINGS_SIGNATURE_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'SETTINGS_SIGNATURE_LIST'),
    name='Add'
where code = 'SETTINGS_SIGNATURE_ADD';

/* Parent*/
update permission
set sorder=46,
    name='AUTOMATION'
where code = 'SETTINGS_WORKFLOW';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'SETTINGS_WORKFLOW'),
    name='View'
where code = 'SETTINGS_SUMMARY_WORKFLOW';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'SETTINGS_WORKFLOW'),
    name='Add'
where code = 'SETTINGS_ADD_WORKFLOW';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'SETTINGS_WORKFLOW'),
    name='Edit'
where code = 'SETTINGS_EDIT_WORKFLOW';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'SETTINGS_WORKFLOW'),
    name='Delete'
where code = 'SETTINGS_REMOVE_WORKFLOW';

/* Parent*/
update permission
set sorder=47
where code = 'SETTINGS_RECURRENCE_SETTINGS';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'SETTINGS_RECURRENCE_SETTINGS'),
    name='Timesheet Reminder'
where code = 'TIMESHEET_REMINDER';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'TIMESHEET_REMINDER'),
    name='Default Timesheet Reminder'
where code = 'DEFAULT_TIMESHEET_REMINDER';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'SETTINGS_RECURRENCE_SETTINGS'),
    name='Sync with Calendar'
where code = 'SYNC_WITH_GOOGLE_CALENDAR';

/* Parent*/
update permission
set sorder=48
where code = 'ADD_SYSTEM_FILTER';

/* Parent*/
update permission
set sorder=49
where code = 'PERMISSION_MANAGEMENT';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'PERMISSION_MANAGEMENT'),
    name='Roles'
where code = 'SETTINGS_ROLE_LIST';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'PERMISSION_MANAGEMENT'),
    name='Permissions'
where code = 'SETTINGS_MANAGE_ROLE';

/* Parent*/
update permission
set sorder=50
where code = 'SETTINGS_INTEGRATION';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'SETTINGS_INTEGRATION'),
    name='Twilio Settings'
where code = 'SETTINGS_TWILIO_LIST';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'SETTINGS_INTEGRATION'),
    name='Asterisk Settings'
where code = 'SETTINGS_ASTERISK_LIST';

/* Parent*/
update permission
set sorder=51
where code = 'SYSTEM_LOGS';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'SYSTEM_LOGS'),
    name='Permission Logs'
where code = 'PERMISSION_LOGS';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'SYSTEM_LOGS'),
    name='Send Notification Panel'
where code = 'HRMS_LEAVE_REQUEST_SEND_NOTIFICATION';

/* Parent*/
update permission
set sorder=52
where code = 'SETTINGS_HRMS_SETTINGS_PUNISHMENTS_PROMOTIONS';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'SETTINGS_HRMS_SETTINGS_PUNISHMENTS_PROMOTIONS'),
    name='Add'
where code = 'SETTINGS_HRMS_SETTINGS_ADD_NEW_PUNISHMENTS_PROMOTIONS';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'SETTINGS_HRMS_SETTINGS_PUNISHMENTS_PROMOTIONS'),
    name='Edit'
where code = 'SETTINGS_HRMS_SETTINGS_PUNISHMENTS_PROMOTIONS_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'SETTINGS_HRMS_SETTINGS_PUNISHMENTS_PROMOTIONS'),
    name='Delete'
where code = 'SETTINGS_HRMS_SETTINGS_PUNISHMENTS_PROMOTIONS_REMOVE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'SETTINGS_HRMS_SETTINGS_PUNISHMENTS_PROMOTIONS'),
    name='Summary'
where code = 'SETTINGS_HRMS_SETTINGS_PUNISHMENTS_PROMOTIONS_SUMMARY';
