package com.edatasite.workforce.rest.base.helpers;

import java.text.SimpleDateFormat;

/**
 * Created by Dilsh0d on 5/1/2017.
 */
public interface ApiConstants {

    SimpleDateFormat dateFormatFilter = new SimpleDateFormat("ddMMyyyy HH:mm:ss");
    String FORMAT_WITH_DATETIME_AND_TIMEZONE = "dd-MM-yyyy'T'HH:mm:ssZ";
    String FORMAT_WITH_DATETIME = "yyyy-MM-dd HH:mm:ss";

    int INCORRECT_USERNAME_OR_PASSWORD = 3001;
    int EMAIL_OR_PHONE_EXIST = 3002;
    int EXPIRED = 3003;
    int ACCESS_DENIED = 3004;
    int NOT_FOUND = 3005;
    int INVALID = 3006;
    int REQUIRED = 3007;
    int SERVER_ERROR = 3008;
    int CONFLICT = 3009;
    int ERROR_LEAD_MODIFY = 3010;
    int NO_ITEMS_FOUND = 3011;
    int CONFIRMATION = 3012;

    String SUCCESS = "success";
    String ERROR = "error";
    String DATA = "data";
    String ERROR_MESSAGE = "Sorry, something went wrong. We are looking into it. Please check back later.";
    String GENERAL_ERROR_MESSAGE = "Something went wrong, please try back later";
    String PERMISSION_MESSAGE = "You do not have necessary permission. Please contact your administrator";//"You don't have permissions to delete this entry. Please contact your administrator";
    String IN_VALID_DATA = "Invalid Data.";

    String SESSION_ID = "sessionId";
    String ACCESS_TOKEN = "accessToken";
    String VERSION = "version";
    String X_AUTH = "x-auth";

    int SESSION_LIVE_TIME_15_MINUTE = 15;
    long MILLISECONDS_IN_HOUR = 1000 * 60 * 60;

    String SALES_INVOICE = "salesInvoice";
    String PURCHASE_INVOICE = "purchaseInvoice";
    String PURCHASE_ORDER = "purchaseOrder";
    String SALES_ORDER = "salesOrder";
    String SALES_QUOTE = "salesQuote";
    String CREDIT_NOTE = "creditNote";
    String CUSTOMER = "customer";
    String SUPPLIER = "supplier";
    String OPPORTUNITY = "opportunity";
    String LEAD = "lead";
    String CONTACT = "contact";
    String SALES_INVOICE_ITEM = "salesInvoiceItem";
    String PURCHASE_INVOICE_ITEM = "purchaseInvoiceItem";
    String PURCHASE_ORDER_ITEM = "purchaseOrderItem";
    String SALES_QUOTE_ITEM = "salesQuoteItem";
    String SALES_ORDER_ITEM = "salesOrderItem";
    String MOBILE_CALL_LOG = "mobileCallLog";
    String EVENT = "event";
    String CALL_LOG = "callLog";
    String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    String ACTIVE_COMPANY = "active";
    String FREE_COMPANY = "free";
    String EXPIRED_COMPANY = "expired";

    interface PROPS {
        String EMAIL = "email";
        String DRIVER_NUMBER = "driverNumber";
    }
}
