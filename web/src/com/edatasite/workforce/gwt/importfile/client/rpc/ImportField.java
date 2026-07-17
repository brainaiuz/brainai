package com.edatasite.workforce.gwt.importfile.client.rpc;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Aug 12, 2010
 * Time: 5:39:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ImportField {
    interface EmployeeLeaveAllowanceFields {
        int FIELD_PINFL = 1;
        int FIELD_START_DATE = 2;
        int FIELD_ALLOWANCE = 3;
        int FIELD_LEFT = 4;
    }

    interface Opportunity {
        int NAME = 1;
        int ASSIGNEE = 2;
        int NUMBER = 3;
        int ACCOUNT_NAME = 4;
        int CONTACT_NAME = 5;
        int TYPE = 6;
        int NEXT_STEP = 7;
        int AMOUNT = 8;
        int CLOSING_DATE = 9;
        int STAGE = 10;
        int PROBABILITY = 11;
        int EXPECTED_REVENUE = 12;
        int CAMPAIGN_SOURCE = 13;
        int LEAD_SOURCE = 14;
        int NOTE = 15;
        // FIELD_CUSTOM_FIELD_START_NUMBER must be always the greatest number (63)
        int FIELD_CUSTOM_FIELD_START_NUMBER = 16;
    }

    interface ContactField {
        int FIELD_OWNER_FROMFILE = 1;
        int FIELD_OWNER = 2;
        int FIELD_FIRSTNAME = 3;
        int FIELD_LASTNAME = 4;
        int FIELD_BIRTHDAY = 5;
        int FIELD_TITLE = 6;
        int FIELD_JOB_TITLE = 7;
        int FIELD_DEPARTMENT = 8;
        int FIELD_ACCOUNT = 9;

        int FIELD_EMAILS = 10;
        int FIELD_HOME_EMAILS = 11;
        int FIELD_WORK_EMAILS = 12;
        int FIELD_OTHER_EMAILS = 13;

        int FIELD_PHONES = 14;
        int FIELD_HOME_PHONES = 15;
        int FIELD_WORK_PHONES = 16;
        int FIELD_MOBILE_PHONES = 17;
        int FIELD_HOMEFAX_PHONES = 18;
        int FIELD_WORKFAX_PHONES = 19;
        int FIELD_PAGER_PHONES = 20;
        int FIELD_OTHER_PHONES = 21;
        int FIELD_EXTENSION = 22;

        int FIELD_IMS = 23;
        int FIELD_IM_GTALKS = 24;
        int FIELD_IM_AIMS = 25;
        int FIELD_IM_YAHOOS = 26;
        int FIELD_IM_SKYPES = 27;
        int FIELD_IM_QQS = 28;
        int FIELD_IM_MSNS = 29;
        int FIELD_IM_ICQS = 30;
        int FIELD_IM_JABBERS = 31;

        int FIELD_WEBS = 32;
        int FIELD_HOME_WEB_ADDRESSES = 33;
        int FIELD_WORK_WEB_ADDRESSES = 34;
        int FIELD_HOMEPAGE_WEB_ADDRESSES = 35;
        int FIELD_FTP_WEB_ADDRESSES = 36;
        int FIELD_BLOG_WEB_ADDRESSES = 37;
        int FIELD_PROFILE_WEB_ADDRESSES = 38;
        int FIELD_OTHER_WEB_ADDRESSES = 39;
        int FIELD_LINKEDIN_WEB_ADDRESSES = 40;
        int FIELD_FACEBOOK_WEB_ADDRESSES = 41;
        int FIELD_TWITTER_WEB_ADDRESSES = 42;
        int FIELD_INSTAGRAM_WEB_ADDRESSES = 43;

        int FIELD_ADDRESSES = 44;
        int FIELD_HOME_ADDRESSES = 45;
        int FIELD_WORK_ADDRESSES = 46;
        int FIELD_OTHER_ADDRESSES = 47;

        int FIELD_CATEGORY_FROMFILE = 48;
        int FIELD_CATEGORY = 49;
        int FIELD_PRIMARY_CONTACT = 50;
        int FIELD_CAMPAIGN_FROMFILE = 51;
        int FIELD_CAMPAIGN = 52;
        int FIELD_EMAIL_OPT = 53;

        int FIELD_LEAD_ASSIGNEE = 54;
        int FIELD_LEAD_BACKUP_ASSIGNEE = 55;
        int FIELD_LEAD_SOURCE = 56;
        int FIELD_LEAD_STATUS = 57;
        int FIELD_LEAD_RATING = 58;

        int FIELD_CANDIDATE_PROJECT = 59;
        int FIELD_CANDIDATE_STATUS = 60;
        int FIELD_CANDIDATE_SOURCE = 61;
        int FIELD_CANDIDATE_CREATED_DATE = 62;
        int FIELD_CANDIDATE_VACANCIES = 63;
        int FIELD_CANDIDATE_WORK_EXPERIENCE = 64;
        int FIELD_CANDIDATE_WORK_EXPERIENCE_MONTH_YEAR = 65;
        int FIELD_CANDIDATE_CURRENT_EMPLOYER = 66;
        int FIELD_CANDIDATE_EXPECTED_SALARY = 67;
        int FIELD_CANDIDATE_LOCATION = 68;
        int FIELD_CANDIDATE_SKILLS = 69;
        int FIELD_ASSIGNEE_FROMFILE = 70;

        // FIELD_CUSTOM_FIELD_START_NUMBER must be always the greatest number
        int FIELD_CUSTOM_FIELD_START_NUMBER = 71;
    }

    interface AdditionalPaymentImportFields {
        Integer FIELD_EMPLOYEE_CODE = 1;
        Integer FIELD_EMPLOYEE_NAME = 2;
        Integer FIELD_AMOUNT = 3;
        Integer FIELD_CATEGORY = 4;
        Integer FIELD_SYSTEM_CATEGORY = 5;
        Integer FIELD_ADDITIONAL_PAYMENT_DATE = 6;
    }

    interface ChartOfAccountsFields {
        int FIELD_ACCOUNT_TYPE = 1;
        int FIELD_CODE = 2;
        int FIELD_NAME = 3;
        int FIELD_DESCRIPTION = 4;
        int FIELD_TAX_RATE = 5;
        int FIELD_SHOW_IN_EXPENSE = 6;
        int FIELD_ENABLE_PAYMENT = 7;

        int SYSTEM_ACCOUNT_TYPE = 8;
        int SYSTEM_TAX_RATE = 9;
        int FIELD_PARENT_CODE = 10;
    }

    interface CrmAccountField {
        int FIELD_NAME = 1;
        int FIELD_NUMBER = 2;
        int FIELD_PARENT = 3;
        int FIELD_TYPE = 4;
        int FIELD_INDUSTRY = 5;

        int FIELD_EMAIL = 6;
        int FIELD_PHONE = 7;
        int FIELD_FAX = 8;
        int FIELD_WEBSITE = 9;

        int FIELD_ADDRESSES = 10;

        int FIELD_CURRENCY = 11;
        int FIELD_VAT_NUMBER = 12;
        int FIELD_PAYMENT_METHOD = 13;
        int FIELD_REGISTRATION_NUMBER = 14;
        int FIELD_CREDIT_LIMIT = 15;
        int FIELD_CLIENT_TYPE = 16;
        int FIELD_BALANCE_DATE = 17;
        int FIELD_BALANCE_AMOUNT = 18;
        int FIELD_TERMS = 19;
        int FIELD_NOTE = 20;

        int FIELD_BANK_NAME = 21;
        int FIELD_ACCOUNT_NAME = 22;
        int FIELD_ACCOUNT_NO = 23;
        int FIELD_SWIFT_CODE = 24;
        int FIELD_SORT_CODE = 25;
        int FIELD_IBAN_CODE = 26;
        int FIELD_BRANCH = 27;
        int FIELD_BANK_ADDRESS = 28;
    }

    interface CustomExpenseImportFields {
        int FIRST_NAME_FIELD = 1;
        int LAST_NAME_FIELD = 2;
        int EXPENSE_DATE_FIELD = 3;
        int REPORT_TITLE_FIELD = 4;
        int DESCRIPTION_FIELD = 5;
        int SUPPLIER_FIELD = 6;
        int RELATED_PROJECT_FIELD = 7;
        int APPROVER_FIELD = 8;
        int CATEGORY_ITEM_FIELD = 9;
        int DESCRIPTION_ITEM_FIELD = 10;
        int UNITS_ITEM_FIELD = 11;
        int COST_UNITS_ITEM_FIELD = 12;
        int TAX_ITEM_FIELD = 13;
        int PURCHASE_ORDER_FIELD = 14;
        int CURRENCY_FIELD = 15;
        int EXCHANGE_RATE_FIELD = 16;
    }

    interface PurchaseOrderImportFields {
        int FIELD_PO_NUMBER = 1;
        int FIELD_PO_DATE = 2;
        int FIELD_PO_VALID_DATE = 3;
        int FIELD_SUPPLIER_NUMBER = 4;
        int FIELD_CURRENCY = 5;
        int FIELD_EXCHANGE_RATE = 6;
        int FIELD_ITEM_NUMBER = 7;
        int FIELD_QTY = 8;
        int FIELD_PRICE = 9;
        int FIELD_TAX_RATE = 10;
        int FIELD_ACCOUNT_CODE = 11;
    }

    interface ReportDataImportFields {
        int CATEGORY_ID = 1;
        int ITEMS = 2;
    }

    interface CustomInvoiceImportFields {
        int FIELD_INVOICE_NUMBER = 1;
        int FIELD_INVOICE_TYPE = 2;
        int FIELD_INVOICE_DATE = 3;

        int FIELD_CUSTOMER_NAME = 4;
        int FIELD_CUSTOMER_STR_ADDRESS = 5;
        int FIELD_CUSTOMER_CITY = 6;
        int FIELD_CUSTOMER_COUNTRY = 7;
        int FIELD_CUSTOMER_POSTCODE = 8;
        int FIELD_CUSTOMER_VAT = 9;

        int FIELD_PRODUCT_NAME = 10;
        int FIELD_PRODUCT_QTY = 11;
        int FIELD_PRODUCT_PRICE = 12;
        int FIELD_PRODUCT_DISCOUNT = 13;
        int FIELD_PRODUCT_TAX = 14;
        int FIELD_BENEFICIARY_ACCOUNT = 15;
        int FIELD_PROJECT = 16;
        int FIELD_DUE_DATE = 17;
        int FIELD_DESCRIPTIOIN = 18;
        int FIELD_REFERENCe = 19;
        int FIELD_PARENT_PROJECT = 20;
    }

    interface EmployeeField {
        int EMPLOYEE_CODE = 1;
        int SALUTATION = 2;
        int FIRST_NAME = 3;
        int LAST_NAME = 4;
        int MIDDLE_NAME = 5;
        int DATE_OF_BIRTH = 6;
        int GENGER = 7;
        int NATIONALITY = 8;
        int MARTIAL_STATUS = 9;
        int SPOKEN_LANGUAGES = 10;

        int EMAIL = 11;
        int PHONE_NUMBER = 12;
        int IM_ADDRESS = 13;
        int WEB_ADDRESS = 14;

        int ADDRESS_NAME = 15;
        int ADDRESS_LINE = 16;
        int ADDRESS_LINE2 = 17;
        int ADDRESS_TYPE = 18;
        int COUNTRY = 19;
        int STATE = 20;
        int CITY = 21;
        int POST_CODE = 22;

        int DEPARTMENT_NAME = 23;
        int POSITION = 24;
        int LOCATION = 25;
        int SUPER_VISER = 26;
        int WAGE_RATE = 27;
        int CLIENT_CHARGE_RATE = 28;
        int HIRE_DATE = 29;
        int RESIGNATION_DATE = 30;
        int EMPLOYMENT_MODE = 31;
        int BASIC_SALARY = 32;
        int QUALIFICATION = 33;

        int BANK_NAME = 34;
        int ACCOUNT_NUMBER = 35;
        int ACCOUNT_NAME = 36;
        int BANK_ADDRESS = 37;
        int SWIFT_BCIC_CODE = 38;
        int SORT_CODE = 39;
        int IBAN_NUMBER = 40;
        int AGENT_ID = 41;

        int PASSPORT_NUMBER = 42;
        int PASSPORT_ISSUE_BY = 43;
        int PASSPORT_ISSUE_DATE = 44;
        int PASSPORT_EXPIRY_DATE = 45;
        int INSURANCE_NUMBER = 46;
        int INSURANCE_EXPIRY_DATE = 47;
        int VISA_NUMBER = 48;
        int VISA_ISSUE_DATE = 49;
        int VISA_EXPIRY_DATE = 50;
        int HAS_ACCESS = 51;
        int USER_ROLE = 52;
        int WPS_NO = 53;
        int EMPLOYMENT_CONTACT_TERMS = 54;
        int SALARY_GRADE = 55;
        int OPENING_BALANCE_DAYS = 56;
        int PROBATION_PERIOD_DAYS = 57;
        int COMPETENCIES = 58;
        int FIELD_CUSTOM_FIELD_START_NUMBER = 59;
    }

    interface ManualTransactionImportFields {
        int FIELD_NUMBER = 1;
        int FIELD_DATE = 2;
        int FIELD_NARRATION = 3;
        int FIELD_REFERENCE = 4;
        int FIELD_ACCOUNT_CODE = 5;
        int FIELD_DEBIT = 6;
        int FIELD_CREDIT = 7;
        int FIELD_DESCRIPTION = 8;
        int FIELD_NAME = 9;
        int FIELD_PROJECT_CODE = 10;
        int FIELD_DEPARTMENT = 11;
        int FIELD_EXCHANGE_RATE = 12;
        int FIELD_CURRENCY = 13;
        int FIELD_PARTICULARS = 14;
        int FIELD_VOUCHER_NUMBER = 15;
        int FIELD_BANK_ACCOUNT = 16;
        int FIELD_CASH_ACCOUNT = 17;
        int FIELD_AMOUNT = 18;
        int FIELD_TAX_CALCULATION_TYPE = 19;
        int FIELD_TAX_RATE = 20;
        int FIELD_EXPECTED_VALUE = 21;
        int FIELD_ACTUAL_VALUE = 22;
    }

    interface NimbleCommerceFields {
        int FIELD_OFFER_ID = 1;
        int FIELD_OFFER_NAME = 2;
        int FIELD_OFFER_PRICE = 3;
        int FIELD_FIRST_NAME = 4;
        int FIELD_LAST_NAME = 5;
        int FIELD_EMAIL = 6;
        int FIELD_PHONE = 7;
        int FIELD_ORDER_NUMBER = 8;
        int FIELD_TRANSACTION_DATE = 9;
        int FIELD_TRANSACTION_TIME = 10;
        int FIELD_QUANTITY = 11;
        int FIELD_MERCHANT_ID = 12;
        int FIELD_TAX = 13;
    }

    interface ProductFields {
        int FIELD_NUMBER = 1;
        int FIELD_NAME = 2;
        int FIELD_DESCRIPTION = 3;
        int FIELD_PRODUCT_TYPE = 4;
        int FIELD_CATEGORY = 5;
        int FIELD_SKU_NUMBER = 6;
        int FIELD_UPC_NUMBER = 7;
        int FIELD_UNIT_MEASUREMENT = 8;
        int FIELD_VENDOR = 9;
        int FIELD_BRAND = 10;
        int FIELD_SELLING_PRICE = 11;
        int FIELD_ACCOUNT = 12;
        int FIELD_COGS_ACCOUNT = 13;
        int FIELD_TAX_RATE = 14;
        int FIELD_COST_PRICE = 15;
        int FIELD_BARCODE = 16;
        int FIELD_PRICELEVEL = 17;
        int FIELD_CUSTOMPRICE = 18;
        //        int FIELD_FREE_SHIPPING = 19;
//        int FIELD_SPECIAL_OFFER = 20;
//        int FIELD_SHOW_ON_HOMEPAGE = 21;
        int FIELD_GLOBAL_REORDER_POINT = 22;
        int FIELD_ASSET_ACCOUNT = 23;
        int FIELD_OPENING_BALANCE_DATE = 24;
        int SYSTEM_CATEGORY_ID = 25;
        int SYSTEM_PRODUCT_TYPE_ID = 26;
        int SYSTEM_UNIT_MEASUREMENT_ID = 27;
        int SYSTEM_VENDOR_ID = 28;
        int SYSTEM_ACCOUNT_ID = 29;
        int SYSTEM_COGS_ACCOUNT_ID = 30;
        int SYSTEM_TAX_RATE_ID = 31;
        int SYSTEM_WAREHOUSE_ID = 32;
        int SYSTEM_ASSET_ACCOUNT_ID = 33;
        int FIELD_MANUFACTURER = 34;
        int FIELD_PART_NUMBER = 35;
        int SP_NAME = 36;
        int PR_NAME = 37;
        int FR_NAME = 38;
        int SP_DESCRIPTION = 39;
        int PR_DESCRIPTION = 40;
        int FR_DESCRIPTION = 41;
        int FIELD_ITEM_ID = 43;
        int FIELD_SELLING_PRICE_USD = 44;
        int FIELD_SELLING_PRICE_AED = 45;
        int FIELD_SELLING_PRICE_GBP = 46;
        int FIELD_SELLING_PRICE_EUR = 47;
        int FIELD_SELLING_PRICE_RUB = 48;
        int FIELD_SELLING_PRICE_SAR = 49;
        int FIELD_SELLING_PRICE_KWD = 50;
        int FIELD_SELLING_PRICE_PKR = 51;

        int FIELD_COST_PRICE_USD = 52;
        int FIELD_COST_PRICE_AED = 53;
        int FIELD_COST_PRICE_GBP = 54;
        int FIELD_COST_PRICE_EUR = 55;
        int FIELD_COST_PRICE_RUB = 56;
        int FIELD_COST_PRICE_SAR = 57;
        int FIELD_COST_PRICE_KWD = 58;
        int FIELD_COST_PRICE_PKR = 59;

        int FIELD_WAREHOUSE = 60;
        int FIELD_QUANTITY = 61;

        int FIELD_CUSTOM_FIELD_START_NUMBER = 62;
        int FIELD_LOCATION =63;
    }

    interface ProjectFields {
        int FIELD_NUMBER = 1;
        int FIELD_NAME = 2;
        int FIELD_DESCRIPTION = 3;
        int FIELD_START_DATE = 4;
        int FIELD_DUE_DATE = 5;
        int FIELD_CLIENT = 6;
        int FIELD_MANAGER = 7;
        int FIELD_STATUS = 8;
        int FIELD_ASSIGNEE = 9;

        int FIELD_CUSTOM_FIELD_START_NUMBER = 10;
    }

    interface GroupPayrunFields {
        int FIELD_EMPLOYEE = 1;
        int FIELD_DATE = 2;
        int FIELD_BASIC_SALARY = 3;
        int FIELD_ALLOWANCE = 4;
        int FIELD_DEDUCTION = 5;
    }

    interface PaymentDeductionFields {
        int FIELD_CATEGORY_NAME = 1;
        int FIELD_CATEGORY_CODE = 2;
        int FIELD_DEBIT_TO_ACCOUNT = 3;
        int FIELD_CREDIT_TO_ACCOUNT = 4;
        int FIELD_USE_IN = 5;
    }

    interface ProductCategoriesFields {
        int FIELD_CODE = 1;
        int FIELD_NAME = 2;
        int FIELD_PARENT_CATEGORY = 3;
        int FIELD_ORDER = 4;
    }

    interface BrandFields {
        int FIELD_NAME = 1;
        int FIELD_PARENT = 2;
        int FIELD_DESCRIPTION = 3;
    }

    interface DepartmentFields {
        int FIELD_NUMBER = 1;
        int FIELD_NAME = 2;
        int FILED_LOCATION = 3;
        int FILED_PARENT = 4;
        int FIELD_START_DATE = 5;
        int FIELD_STATUS = 6;
        int FIELD_DEPARTMENT_AR = 7;
        int FIELD_DEPARTMENT_RU = 8;
        int FIELD_DEPARTMENT_UZ = 9;
        int FIELD_DEPARTMENT_EN = 10;
        int FIELD_CUSTOM_FIELD_START_NUMBER = 11;
    }

    interface PositionFields {
        int FIELD_NUMBER = 1;
        int FIELD_POSITION = 2;
        int FIELD_POSITION_AR = 3;
        int FIELD_POSITION_RU = 4;
        int FIELD_POSITION_UZ = 5;
        int FIELD_LOCATION = 6;
        int FIELD_DEPARTMENT = 7;
        int FIELD_PLACE_COUNT = 8;
        int FIELD_TYPE = 9;
        int FIELD_COEFFICENT = 10;

        int FIELD_CUSTOM_FIELD_START_NUMBER = 11;

    }
}
