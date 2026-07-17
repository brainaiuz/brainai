package com.edatasite.workforce.gwt.accounting.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 23.03.2010
 * Time: 21:16:08
 * To change this template use File | Settings | File Templates.
 */
public interface AccountingConstants {
    BigDecimal ZERO = new BigDecimal("0.00");
    BigDecimal ONE = new BigDecimal("1.00");
    BigDecimal HUNDRED = new BigDecimal("100.00");
    boolean DEBIT = true;
    boolean CREDIT = false;
    Integer systemCalculationScale = 5;

//    public static final Integer SALES = 1;
//    public static final Integer PURCHASES = 2;
//    public static final Integer EXEMPT_SALES = 3;
//    public static final Integer EXEMPT_PURCHASES = 4;
//    public static final Integer EC_SALES = 5;
//    public static final Integer EC_PURCHASES = 6;
//    public static final Integer CAPITAL_SALES = 7;
//    public static final Integer CAPITAL_PURCHASES = 8;

    Integer TAX_SALES = 0;
    Integer TAX_PURCHASE = 1;

    Integer TAX_SIMPLE = 1;
    Integer TAX_EXEMPT = 3;
    Integer TAX_EC = 5;
    Integer TAX_CAPITAL = 7;
    Integer TAX_GROUP = 100;

    String T_NOT_RATED = "T-Not Rated";

    String MONTHLY1 = "1MONTHLY";
    String MONTHLY2 = "2MONTHLY";
    String QUARTERLY = "QUARTERLY";
    String MONTHLY4 = "4MONTHLY";
    String MONTHLY5 = "5MONTHLY";
    String MONTHLY6 = "6MONTHLY";
    String ANNUAL = "ANNUAL";
    String CUSTOM_RANGE = "CUSTOM_RANGE";

    String ACCRUAL = "ACCRUAL";
    String CASH = "CASH";
    String FLAT_ACCRUAL = "FLAT_ACCRUAL";
    String FLAT_CASH = "FLAT_CASH";

    String YES = "Yes";
    String NO = "No";

    //This is used for tax permissions
    Integer NON_DELETABLE = 1;
    Integer NON_EDITABLE = 2;

    //This is used in invoice add forms to show how to calculate taxes (inclusive,exclusive,notax)
    Integer NO_TAX_CALCULATION = 0;
    Integer TAX_CALCULATION_INCLUSIVE = 1;
    Integer TAX_CALCULATION_EXCLUSIVE = 2;

    //This is used in bank account spend/receive/cash repeipt/cash payment money forms to differentiate spend and receive transactions
    Integer RECEIVE_MONEY = 0;  // Receive Money = Bank Receipt
    Integer SPEND_MONEY = 1;    // Spend Money = Bank Payment
    Integer CASH_RECEIPT = 2;   // Cash Receive = Cash Receipt
    Integer CASH_PAYMENT = 3;   // Cash Spend = Cash Payment


    String RECEIVE_MONEY_STR = "RECEIVE_MONEY";
    String SPEND_MONEY_STR = "SPEND_MONEY";
    String CASH_RECEIPT_STR = "CASH_RECEIPT";
    String CASH_PAYMENT_STR = "CASH_PAYMENT";
    String MANUAL_ENTRY = "MANUAL_ENTRY";
    String BATCH_RECEIVE_PAYMENT = "BATCH_RECEIVE_PAYMENT";
    String BATCH_PAY_BILL = "BATCH_PAY_BILL";

    String ADD_FORM = "ADD_FORM";
    String EDIT_FORM = "EDIT_FORM";
    String VIEW_FORM = "VIEW_FORM";

    Integer SPEND_RECEIVE_FORM = 0;       //Spend/Receive Form
    Integer CREATE_TRANSACTION_FORM = 1; //ImportTransactionsView.java Create New Transaction

    String CUSTOMER_PREPAYMENT = "CUSTOMER_PREPAYMENT";
    String SUPPLIER_PREPAYMENT = "supplierPrepayment";
    String CHECK_LIST = "checkList";
    String WAREHOUSE_LIST = "warehouseList";


    Integer COPY_FROM_EXISTING_DATA = 1;//All
    Integer COPY_INVOICE_TO_CREDITNOTE = 2;//All
    Integer COPY_FROM_CLIENT_SUPPLIER = 3;//All
    Integer COPY_FROM_CRM_ACCOUNT = 4;//All
    Integer COPY_FROM_SQ_SO_TO_PO = 5;//All
    Integer COPY_FROM_SI_TO_PO = 6;//All
    Integer PROGRESS_INVOICING = 7;//SQ,SI
    Integer COPY_FROM_FIXED_ASSET = 8;//PI,SI
    Integer COPY_FROM_PRODUCT_LIST = 9;//SQ,PO
    Integer CONVERT_TO_INVOICE = 10;//SQ,SI,PO,PI
    Integer CONVERT_RFP_TO_PO = 11;//SQ,SI,PO,PI
    Integer CONVERT_MULTI_QUOTE_TO_INVOICE = 12;
    Integer COPY_PO_TO_PI = 13;
    Integer COPY_FROM_OPPORTUNITY_TO_PO = 14;
    Integer COPY_PO_TO_SQ = 15;
    Integer COPY_FROM_SO_TO_SQ = 16;
    Integer FROM_DISBURSEMENT = 17;
    Integer FROM_INTERNAL_INVOICE = 18;
    Integer CONVERT_RFP_TO_RFQ = 19;
    Integer CONVERT_TO_INVOICE_FROM_GRN = 20;//PO,PI
    Integer COPY_FROM_SI_TO_PI = 21;
    Integer COPY_FROM_PI_TO_SI = 22;
    Integer CONVERT_TO_INVOICE_FROM_RENTAL_ORDER = 23;

    String BY_PERCENTAGE = "byPercent";
    String BY_AMOUNT = "byAmount";
    String BY_ITEM = "byItem";
    String BY_CUSTOM_PERCENTAGE = "byCustom";
    String BY_MULTI_PROGRESS = "byMultiProgress";
    String NEW = "new";
    int ROUNDING_PRICE = 4;
    int ROUNDING_QUANTITY = 2;
    int ROUNDING_EXCH_RATE = 4;

    String ACTION = "action";
    String PLACEMENT = "placement";
    String ITEM_COLUMN = "item";
    String INVOICE_NUMBER_COLUMN = "invoiceNumber";
    String INVOICE_DATE_COLUMN = "invoiceDate";
    String DUE_DATE_COLUMN = "dueDate";
    String PROJECT_COLUMN = "project";
    String CLIENT_COLUMN = "client";
    String CURRENCY_COLUMN = "currency";
    String DUE_AMOUNT_COLUMN = "dueAmount";
    String TAX_AMOUNT_COLUMN = "taxAmount";
    String PAID_AMOUNT_COLUMN = "paidAmount";
    String STATUS_COLUMN = "status";
    String APPROVER_STATUS_COLUMN = "approverStatus";
    String ORIGINAL_AMOUNT_COLUMN = "originalAmount";
    String TAX_RATE_COLUMN = "taxRate";
    String QTY_COLUMN = "qty";
    String UNIT_PRICE_COLUMN = "unitPrice";
    String DESCRIPTION_COLUMN = "description";
    String ACCOUNT_COLUMN = "account";
    String ATTACHMENT_PANEL = "attachmentPanel";
    String RELATED_PO = "relatedPO";

    String DATE_COLUMN = "date";
    String DEPARTMENT_COLUMN = "department";
    String BANK_TRANSFER_DESCRIPTION_COLUMN = "bankTransferDescription";
    String REFERENCE_COLUMN = "reference";
    String SPENT_COLUMN = "spent";
    String RECEIVED_COLUMN = "received";
    String PAYMENT_AMOUNT = "paymentAmount";
    String REFUND_AMOUNT = "refundAmount";
    String CLOSE_AMOUNT = "closeAmount";
    String UNDER_PAYMENT_AMOUNT = "underPaymentAmount";
    String BANK_FEE_TAX_RATE_COLUMN = "bankFeeTaxRate";
    String BANK_FEE_TAX_AMOUNT_COLUMN = "bankFeeTaxAmount";
    String CLOSE_COLUMN = "close";
    String ENABLE_COLUMN = "enable";
    String CLIENT_SUPPLIER = "clientSupplier";

    String CODE_COLUMN = "code";
    String NAME_COLUMN = "name";
    String TYPE_COLUMN = "type";
    String ACTIVE_COLUMN = "active";

    String CONTACT_NAME_COLUMN = "contactName";
    String CONTACT_PHONE_NUMBER_COLUMN = "contactPhoneNumber";
    String ADDRESS_COLUMN = "address";
    String COUNTRY_COLUMN = "country";
    String STATE_COLUMN = "state";

    String PLCASE_COLUMN = "plcase";
    String REPEATS_COLUMN = "repeats";
    String NEXT_INVOICE_DATE_COLUMN = "nextInvoiceDate";
    String END_DATE_COLUMN = "endDate";

    String TITLE_COLUMN = "title";
    String PERIOD_COLUMN = "period";
    String REPORTER_COLUMN = "reporter";
    String APPROVER_COLUMN = "approver";
    String ACCOUNTANT_COLUMN = "accountant";

    String NUMBER_COLUMN = "number";
    String AMOUNT_COLUMN = "amount";

    String RATE_COLUMN = "rate";
    String YTD_COLUMN = "balance";

    Integer SUBMISSION_COMPLETED = 1;
    Integer SUBMISSION_PENDING = 2;
    Integer SUBMISSION_FAILED = 3;
    Integer SUBMISSION_SAVED = 4;
    Integer SUBMISSION_PAID = 5;

    //
    Integer CASH_TYPE = 0;
    Integer MASTER_CARD_TYPE = 1;

    //Expense Report List Params
    String MY_EXPENSE_REPORTS = "My Expense Reports";
    String AWAITING_APPROVAL = "Waiting for Approval";
    String AWAITING_PAYMENT = "Waiting for Payment";
    String ALL_EXPENSES = "All Expense claims";

    /*----------- Inventory(Product) Item Types -----------*/
    Integer INVENTORY_ITEM = 1;
    Integer NON_INVENTORY_ITEM = 2;
    Integer SERVICE = 3;
    Integer ASSEMBLY_ITEM = 4;
    Integer OTHER_CHARGE = 5;
    Integer PRODUCT_KIT = 6;
    Integer RENTAL_ITEM = 7;

    String STOCK_OUT_TYPE = "STOCK_OUT";
    String STOCK_ADJUSTMENT_TYPE = "STOCK_ADJUSTMENT";

    String INVENTORY_ITEM_STR = "Inventory Item";
    String NON_INVENTORY_ITEM_STR = "Non-Inventory Item";
    String SERVICE_STR = "Service";
    String ASSEMBLY_ITEM_STR = "Assembly Item";
    String OTHER_CHARGE_STR = "Other Charge";
    String PRODUCT_KIT_STR = "Product Group";
    String RENTAL_ITEM_STR = "Rental Item";
    String RENT_ITEM = "RENT_ITEM";
    String SENT_TO_TEXTILE_FINDS = "SENT_TO_TEXTILE_FINDS";

    // PRODUCT Discount Types
    Integer FIXED_AMOUNT = 1;
    Integer PERCENTAGE  = 2;

    String FIXED_AMOUNT_STR = "Fixed Amount";
    String PERCENTAGE_STR  = "Percentage";

    SelectItem[] PRODUCT_DISCOUNT_TYPES = new SelectItem[]{
            new SelectItem(FIXED_AMOUNT, FIXED_AMOUNT_STR),
            new SelectItem(PERCENTAGE, PERCENTAGE_STR)
    };
    String SUPPLIER_PREPAYMENTS = "Supplier Prepayment";
    String CUSTOMER_PREPAYMENTS = "Customer Prepayment";
    String SUPPLIER_COLUMN = "supplier";
    String PAY_ACCOUNT_COLUMN = "payaccount";
    String CUSTOMER_COLUMN = "customer";
    String CREATOR_COLUMN = "creator";
    String REMAIN_COLUMN = "remain";
    String ASSEMBLY_ITEMS = "ASSEMBLY_ITEMS";
    String PRODUCT_GROUP = "PRODUCT_GROUP";

    //PDF Naming Parameters
    String PDF_PREFIX = "PREFIX";
    String PDF_TYPE = "TYPE";
    String PDF_COMPANY_NAME = "COMPANYNAME";
    String PDF_CLIENT = "CLIENT";
    String PDF_CLIENT_CODE = "CLIENTCODE";
    String PDF_NUMBER = "NUMBER";
    String PDF_GENERATED_DATE = "GENERATEDDATE";
    String PDF_USER_NAME = "USERNAME";

    SelectItem[] PRODUCT_TYPES = new SelectItem[]{
            new SelectItem(INVENTORY_ITEM, INVENTORY_ITEM_STR),
            new SelectItem(NON_INVENTORY_ITEM, NON_INVENTORY_ITEM_STR),
            new SelectItem(SERVICE, SERVICE_STR),
            new SelectItem(ASSEMBLY_ITEM, ASSEMBLY_ITEM_STR),
            new SelectItem(OTHER_CHARGE, OTHER_CHARGE_STR),
            new SelectItem(PRODUCT_KIT, PRODUCT_KIT_STR)
            //new SelectItem(RENTAL_ITEM, RENTAL_ITEM_STR)
    };

    //reservation types
    Integer RESERVATION_TYPE_PRODUCT = 1;
    Integer RESERVATION_TYPE_EVENT = 2;

    String FINANCED_BY = "FINANCED_BY";

    String RECEIVABLE_OVERPAYMENT = "RECEIVABLE_OVERPAYMENT";
    String PAYABLE_OVERPAYMENT = "PAYABLE_OVERPAYMENT";

    String RECEIVABLE_PREPAYMENT = "RECEIVABLE_PREPAYMENT";
    String RECEIVABLE_PREPAYMENT_SHARE = "APPLIED_PREPAYMENT_SHARE";
    String PAYABLE_SUPPLIER_CREDIT = "PAYABLE_SUPPLIER_CREDIT";
    String PAYABLE_SUPPLIER_CREDIT_SHARE = "APPLIED_SUPPLIER_CREDIT_SHARE";
    String VATRETURN_PAYMENT_RECEIVABLE = "VATRETURN_PAYMENT_RECEIVABLE";
    String VATRETURN_PAYMENT_PAYABLE = "VATRETURN_PAYMENT_PAYABLE";
    String PAYABLE_BANK_CHECK_SHARE = "PAYABLE_BANK_CHECK_SHARE";
    String RECEIVABLE_CRM_ACCOUNT_CREDIT = "RECEIVABLE_CRM_ACCOUNT_CREDIT";
    String PAYABLE_CRM_ACCOUNT_CREDIT = "PAYABLE_CRM_ACCOUNT_CREDIT";
    String RECEIVABLE_MANUAL_CREDIT = "RECEIVABLE_MANUAL_CREDIT";
    String PAYABLE_MANUAL_CREDIT = "PAYABLE_MANUAL_CREDIT";
    String RECEIVABLE_BANKTRANSFER_CREDIT = "RECEIVABLE_BANKTRANSFER_CREDIT";
    String PAYABLE_BANKTRANSFER_CREDIT = "PAYABLE_BANKTRANSFER_CREDIT";

    String RECEIVABLE_PREPAYMENT_REFUND = "RECEIVABLE_PREPAYMENT_REFUND";
    String PAYABLE_PREPAYMENT_REFUND = "PAYABLE_PREPAYMENT_REFUND";
    String RECEIVABLE_CREDIT_REFUND = "RECEIVABLE_CREDIT_REFUND";
    String PAYABLE_DEBIT_REFUND = "PAYABLE_DEBIT_REFUND";

    String PRE_PAYMENT_OPEN_STATUS = "PRE_PAYMENT_OPEN_STATUS";
    String PRE_PAYMENT_PARTIAL_APPLIED_STATUS = "PRE_PAYMENT_PARTIAL_APPLIED_STATUS";
    String PRE_PAYMENT_APPLIED_STATUS = "PRE_PAYMENT_APPLIED_STATUS";
    String VOID = "VOID";
    int SMALL_SIZE = 1;
    int MEDIUM_SIZE = 2;
    int LARGE_SIZE = 3;

    int SMALL = 120;
    int MEDIUM = 230;
    int LARGE = 350;

    SelectItem[] QR_CODE_SIZES = new SelectItem[]{
            new SelectItem(SMALL_SIZE, "small"),
            new SelectItem(MEDIUM_SIZE, "medium"),
            new SelectItem(LARGE_SIZE, "large")
    };
    SelectItem[] PRODUCT_CONDITIONS = new SelectItem[]{
            new SelectItem(Constants.PRODUCT_NEW, "New Product"),
            new SelectItem(Constants.PRODUCT_USED, "Used"),
            new SelectItem(Constants.PRODUCT_REFURBISHED, "Refurbished")
    };

    String INVOICE_ID = "INVOICE_ID";
    String FIXEDASSET_ID = "FIXEDASSET_ID";

    Integer ALLOCATE_BY_QTY = 0;
    Integer ALLOCATE_BY_AMOUNT = 1;
    Integer ALLOCATE_MANUALLY = 2;
    Integer DONOT_ALLOCATE = 3;
    Integer ALLOCATE_RECEIVE_ITEM_BY_QTY = 4;
    Integer ALLOCATE_RECEIVE_ITEM_BY_AMOUNT = 5;

    //SAASU Entity types
    Integer SAASU_ENTITY_CUSTOMER = 0;
    Integer SAASU_ENTITY_SUPPLIER = 1;
    Integer SAASU_ENTITY_EMPLOYEE = 2;
    Integer SAASU_ENTITY_ACCOUNT = 3;
    Integer SAASU_ENTITY_PRODUCT = 4;
    Integer SAASU_ENTITY_COMBOITEM = 5;
    Integer SAASU_ENTITY_SALEQUOTE = 6;
    Integer SAASU_ENTITY_SALEINVOICE = 7;
    Integer SAASU_ENTITY_BILL = 8;
    Integer SAASU_ENTITY_PURCHASE_ORDER = 9;
    //End Of SAASU Entity types

    Integer DISPOSE_CASH = 0;
    Integer DISPOSE_ACCOUNTS_RECEIVABLE = 1;
    Integer DISPOSE_JUST = 2;

    String PRODUCT = "PRODUCT";
    String PRODUCT_SUMMARY = "PRODUCT_SUMMARY";
    String PRICE_LEVEL = "PRICE_LEVEL";

    Integer PRODUCT_INVOICE_TYPE = 0;
    Integer SERVICE_INVOICE_TYPE = 1;

    String STOCK_ADJUSTMENT = "STOCK_ADJUSTMENT";
    String STOCK_OUT = "STOCK_OUT";
    String STOCK_TRANSFER = "STOCK_TRANSFER";
    String CONSIGNMENT = "consignment";

    String BANK_CHECK = "BANK_CHECK";

    String BANK_ACCOUNT = "BANK_ACCOUNT";
    String DEFAULT_PAYMENT_ACCOUNT = "DEFAULT_PAYMENT_ACCOUNT";

    String ACCOUNT_TYPE_BANK = "BANK";

    Integer ACCOUNTS_RECEIVABLE_KEY = 100;
    Integer ACCOUNTS_PAYABLE_KEY = 2100;
    Integer VAT_LIABILITY_KEY = 2202;

    Integer SALARY_PAYABLE = 9996;

    Integer UNEARNED_REVENUE_KEY = 2021;
    Integer PREPAID_EXPANSES_KEY = 1011;

    Integer DUE_TYPE = 0;
    Integer TERMS_TYPE = 1;

    Integer PO_DATE_TYPE = 0;
    Integer RECEIVE_DATE_TYPE = 1;

    //Shipping Label Constants
    Integer POSTCARD = 0;
    Integer LETTER = 1;
    Integer LARGE_ENVELOPE = 2;
    Integer PACKAGE = 3;
    Integer LARGE_PACKAGE = 4;

    interface SHIPPING_SERVICE {
        String FIRST_CLASS = "FIRST CLASS";
        String FIRST_CLASS_COMMERCIAL = "FIRST CLASS COMMERCIAL";
        String FIRST_CLASS_HFP_COMMERCIAL = "FIRST CLASS HFP COMMERCIAL";
        String PRIORITY = "PRIORITY";
        String PRIORITY_COMMERCIAL = "PRIORITY COMMERCIAL";
        String PRIORITY_HFP_COMMERCIAL = "PRIORITY HFP COMMERCIAL";
        String EXPRESS = "EXPRESS";
        String EXPRESS_COMMERCIAL = "EXPRESS COMMERCIAL";
        String EXPRESS_SH = "EXPRESS SH";
        String EXPRESS_SH_COMMERCIAL = "EXPRESS SH COMMERCIAL";
        String EXPRESS_HFP = "EXPRESS HFP";
        String EXPRESS_HFP_COMMERCIAL = "EXPRESS HFP COMMERCIAL";
        String PARCEL = "PARCEL";
        String MEDIA = "MEDIA";
        String LIBRARY = "LIBRARY";
        String ALL = "ALL";
        String ONLINE = "ONLINE";

        String[] SERVICES = new String[]{
                FIRST_CLASS,
                FIRST_CLASS_COMMERCIAL,
                FIRST_CLASS_HFP_COMMERCIAL,
                PRIORITY,
                PRIORITY_COMMERCIAL,
                PRIORITY_HFP_COMMERCIAL
        };
    }

    interface FIRST_MAIL_CLASS_TYPE {
        String LETTER = "LETTER";
        String FLAT = "FLAT";
        String PARCEL = "PARCEL";
        String POSTCARD = "POSTCARD";
        String PACKAGE_SERVICE = "PACKAGE SERVICE";
    }

    String ACCOUNTS_RECEIVABLE_INTERCOMPANY = "ACCOUNTS_RECEIVABLE_INTERCOMPANY";
    String ACCOUNTS_PAYABLE_INTERCOMPANY = "ACCOUNTS_PAYABLE_INTERCOMPANY";

    String PAYMENT_TARGET_INVOICE = "INVOICE";
    String PAYMENT_TARGET_MANUAL_JOURNAL = "MANUAL_JOURNAL";
    String PAYMENT_TARGET_EXPENSE = "EXPENSE";
    String PAYMENT_TARGET_INVOICE_AND_MANUAL_JOURNAL = "PAYMENT_TARGET_INVOICE_AND_MANUAL_JOURNAL";

    String CUSTOM_ASSEMBLY_ITEMS_DESCRIPTION = "CUSTOM_ASSEMBLY_ITEMS_DESCRIPTION";


    String FIXED_ASSET_DRAFT = "DRAFT";
    String FIXED_ASSET_APPROVED = "APPROVED";

    String PROJECT_EXPENSE = "PROJECT_EXPENSE";

    String BARCODE = "BARCODE";
    String QRCODE = "QRCODE";
    String QRCODE_GOOGLE = "QRCODE_GOOGLE";

    String CASH_STR = "Cash";
    String CREDIT_STR = "Credit";

    //Product Import Constants
    String FROM_CSV = "FROM_CSV";
    String FROM_PARENT = "FROM_PARENT";

    Integer NUMBER_EXISTS = -132114; //13-N, 21-U, 14-M (so we get NUM)

    Integer ALL_ACCOUNTS = 3;

    String ASSETS = "ASSETS";
    String FIXED_ASSET = "FIXED_ASSET";
    String CURRENT_ASSET = "CURRENT_ASSET";
    String NON_CURRENT_ASSET = "NON_CURRENT_ASSET";
    String BANK = "BANK";
    String PREPAYMENT = "PREPAYMENT";
    String TOTAL_ASSET = "TOTAL_ASSET";

    String EQUITY_LIABILITIES = "EQUITY_LIABILITIES";
    String LIABILITY = "LIABILITY";
    String CURRENT_LIABILITY = "CURRENT_LIABILITY";
    String LONG_TERM_LIABILITY = "LONG_TERM_LIABILITY";
    String EQUITY = "EQUITY";
    String TOTAL_LIABILITY = "TOTAL_LIABILITY";

    String SUPPLIER_CREDIT = "SUPPLIER_CREDIT";
    String RFQ = "RFQ";
    String DASHBOARD_CHARTS = "DASHBOARD_CHARTS";
    String RFP = "RFP";
    String AGED_RECEIVABLE = "AGED_RECEIVABLE";
    String AGED_PAYABLE = "AGED_PAYABLE";
    String LEAVE_REQUEST = "LEAVE_REQUEST";
    String GOODS_DELIVERED_NOTES = "GOODS_DELIVERED_NOTES";
    String GOODS_RECEIVED_NOTES = "GOODS_RECEIVED_NOTES";
    String CUSTOM_FORM_ITEM_VIEW = "CUSTOM_FORM_ITEM_VIEW";
    String CUSTOMER_SUPPLIER_BALANCE = "CUSTOMER_SUPPLIER_BALANCE";
    String PICK_LIST_VIEW = "PICK_LIST_VIEW";
    String EMPLOYEE_PROFILE = "EMPLOYEE_PROFILE";
    String RENTAL_ORDER = "RENTAL_ORDER";
    String RENTAL_PRODUCT = "RENTAL_PRODUCT";

    interface TAX_PERIOD {
        String MONTHLY = "MONTHLY";
        String QUARTERLY = "QUARTERLY";
        String ANNUAL = "QUARTERLY";
        String CUSTOM = "CUSTOM";
    }

    interface VAT_RETURN_STATUS {
        String FILED = "FILED";
        String UNFIELD = "UNFIELD";
    }
}

