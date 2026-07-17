<%@page import="com.edatasite.workforce.gwt.core.client.ui.Constants" %>
<%@ page import="com.edatasite.workforce.gwt.core.client.CommandConstants" %>
<%--
  User: jamshid.asatillayev
  Date: Jun 13, 2011
  Time: 2:38:35 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div>
    <input type="hidden" id="<%=Constants.USER_FULLNAME%>" value="<%=request.getAttribute(Constants.USER_FULLNAME)%>">
    <input type="hidden" id="<%=Constants.USER_INITIALNAME%>" value="<%=request.getAttribute(Constants.USER_INITIALNAME)%>">
    <input type="hidden" id="<%=Constants.FULL_NAME%>" value="<%=request.getAttribute(Constants.FULL_NAME)%>">
    <input type="hidden" id="<%=Constants.FIRST_NAME%>" value="<%=request.getAttribute(Constants.FIRST_NAME)%>">
    <input type="hidden" id="<%=Constants.USER_NAME%>" value="<%=request.getAttribute(Constants.USER_NAME)%>">
    <input type="hidden" id="<%=Constants.EMAIL%>" value="<%=request.getAttribute(Constants.EMAIL)%>">
    <%--<input type="hidden" id="<%=Constants.USER_ID%>" value="<%=request.getAttribute(Constants.USER_ID)%>">--%>
    <input type="hidden" id="<%=Constants.ACCESS_GRANTED%>" value="<%=request.getAttribute(Constants.ACCESS_GRANTED)%>">
    <input type="hidden" id="<%=Constants.COMPANY_NAME%>" value="<%=request.getAttribute(Constants.COMPANY_NAME)%>">
    <%--<input type="hidden" id="<%=Constants.ROLES%>" value="<%=request.getAttribute(Constants.ROLES)%>">--%>
    <input type="hidden" id="<%=Constants.COMPANY_ID%>" value="<%=request.getAttribute(Constants.COMPANY_ID)%>">
    <input type="hidden" id="<%=Constants.WITHOUT_ENCRYPTED_COMPANY_ID%>"
           value="<%=request.getAttribute(Constants.WITHOUT_ENCRYPTED_COMPANY_ID)%>">
    <input type="hidden" id="<%=Constants.INITIAL_URL%>" value="<%=request.getAttribute(Constants.INITIAL_URL)%>">
    <input type="hidden" id="<%=Constants.SESSION_TRACK_ID%>"
           value="<%=request.getAttribute(Constants.SESSION_TRACK_ID)%>">

    <input type="hidden" id="<%=Constants.IS_SETUP_SUPPROJECT%>"
           value="<%=request.getAttribute(Constants.IS_SETUP_SUPPROJECT)%>">

    <input type="hidden" id="<%=Constants.IS_SETUP_SUPPROJECT_TWO_LEVEL%>"
           value="<%=request.getAttribute(Constants.IS_SETUP_SUPPROJECT_TWO_LEVEL)%>">


    <input type="hidden" id="<%=Constants.PM_IS_SETUP%>" value="<%=request.getAttribute(Constants.PM_IS_SETUP)%>">
    <input type="hidden" id="<%=Constants.ACCOUNTING_IS_SETUP%>"
           value="<%=request.getAttribute(Constants.ACCOUNTING_IS_SETUP)%>">
    <input type="hidden" id="<%=Constants.MULTI_COMPANY_SUBSIDIARY%>"
           value="<%=request.getAttribute(Constants.MULTI_COMPANY_SUBSIDIARY)%>">
    <input type="hidden" id="<%=Constants.MULTIWAREHOUSE_ENABLED%>"
           value="<%=request.getAttribute(Constants.MULTIWAREHOUSE_ENABLED)%>">
    <input type="hidden" id="<%=Constants.ACCOUNTING_CALCULATION_SCALE%>"
           value="<%=request.getAttribute(Constants.ACCOUNTING_CALCULATION_SCALE)%>">
    <input type="hidden" id="<%=Constants.ACCOUNTING_TAX_RATE_SCALE%>"
           value="<%=request.getAttribute(Constants.ACCOUNTING_TAX_RATE_SCALE)%>">
    <input type="hidden" id="<%=Constants.ACCOUNTING_CUSTOM_QUANTITY_SCALE%>"
           value="<%=request.getAttribute(Constants.ACCOUNTING_CUSTOM_QUANTITY_SCALE)%>">
    <input type="hidden" id="<%=Constants.ACCOUNTING_CUSTOM_PRICE_SCALE%>"
           value="<%=request.getAttribute(Constants.ACCOUNTING_CUSTOM_PRICE_SCALE)%>">
    <input type="hidden" id="<%=Constants.ACCOUNTING_CUSTOM_EXRATE_SCALE%>"
           value="<%=request.getAttribute(Constants.ACCOUNTING_CUSTOM_EXRATE_SCALE)%>">
    <input type="hidden" id="<%=Constants.TRANSACTION_LOCKING_DATE%>"
           value="<%=request.getAttribute(Constants.TRANSACTION_LOCKING_DATE)%>">
    <input type="hidden" id="<%=Constants.TRANSACTION_LOCKING_SALES%>"
           value="<%=request.getAttribute(Constants.TRANSACTION_LOCKING_SALES)%>">
    <input type="hidden" id="<%=Constants.TRANSACTION_LOCKING_PURCHASES%>"
           value="<%=request.getAttribute(Constants.TRANSACTION_LOCKING_PURCHASES)%>">
    <input type="hidden" id="<%=Constants.TRANSACTION_LOCKING_BANKING%>"
           value="<%=request.getAttribute(Constants.TRANSACTION_LOCKING_BANKING)%>">
    <input type="hidden" id="<%=Constants.TRANSACTION_LOCKING_EMPLOYEES%>"
           value="<%=request.getAttribute(Constants.TRANSACTION_LOCKING_EMPLOYEES)%>">
    <input type="hidden" id="<%=Constants.TRANSACTION_LOCKING_ATTENDANCE%>"
           value="<%=request.getAttribute(Constants.TRANSACTION_LOCKING_ATTENDANCE)%>">
    <input type="hidden" id="<%=Constants.TRANSACTION_LOCKING_RECRUITMENT%>"
           value="<%=request.getAttribute(Constants.TRANSACTION_LOCKING_RECRUITMENT)%>">
    <input type="hidden" id="<%=Constants.TRANSACTION_LOCKING_PAYSLIPS%>"
           value="<%=request.getAttribute(Constants.TRANSACTION_LOCKING_PAYSLIPS)%>">
    <input type="hidden" id="<%=Constants.TRANSACTION_LOCKING_CASHADVANCES%>"
           value="<%=request.getAttribute(Constants.TRANSACTION_LOCKING_CASHADVANCES)%>">
    <input type="hidden" id="<%=Constants.TRANSACTION_LOCKING_ADDITIONALPAYMENTS%>"
           value="<%=request.getAttribute(Constants.TRANSACTION_LOCKING_ADDITIONALPAYMENTS)%>">
    <input type="hidden" id="<%=Constants.PO_IGNORE_MANAGER_APPROVAL%>"
           value="<%=request.getAttribute(Constants.PO_IGNORE_MANAGER_APPROVAL)%>">
    <input type="hidden" id="<%=Constants.CUSTOM_TAX_NAME%>"
           value="<%=request.getAttribute(Constants.CUSTOM_TAX_NAME)%>">
    <input type="hidden" id="<%=Constants.IS_SUPPLIER%>"
           value="<%=request.getAttribute(Constants.IS_SUPPLIER)%>">
    <input type="hidden" id="<%=Constants.IS_CLIENT_CONTACT%>"
           value="<%=request.getAttribute(Constants.IS_CLIENT_CONTACT)%>">
    <input type="hidden" id="<%=Constants.BASE_CURRENCY%>" value="<%=request.getAttribute(Constants.BASE_CURRENCY)%>">
    <input type="hidden" id="<%=Constants.INVOICE_FIRST_VIEW%>"
           value="<%=request.getAttribute(Constants.INVOICE_FIRST_VIEW)%>">
    <input type="hidden" id="<%=Constants.PM_FIRST_VIEW%>" value="<%=request.getAttribute(Constants.PM_FIRST_VIEW)%>">
    <input type="hidden" id="<%=Constants.PA_FIRST_VIEW%>" value="<%=request.getAttribute(Constants.PA_FIRST_VIEW)%>">
    <input type="hidden" id="<%=Constants.SHORT_DATE_FORMAT%>"
           value="<%=request.getAttribute(Constants.SHORT_DATE_FORMAT)%>">
    <input type="hidden" id="<%=Constants.LONG_DATE_FORMAT%>"
           value="<%=request.getAttribute(Constants.LONG_DATE_FORMAT)%>">
    <input type="hidden" id="<%=Constants.GOOGLE_APP_DOMAIN%>"
           value="<%=request.getAttribute(Constants.GOOGLE_APP_DOMAIN)%>">
    <input type="hidden" id="<%=Constants.GOOGLE_MARKETPLACE_USERS_IMPORT_POPUP_SHOW%>"
           value="<%=request.getAttribute(Constants.GOOGLE_MARKETPLACE_USERS_IMPORT_POPUP_SHOW)%>">
    <input type="hidden" id="<%=Constants.FACEBOOK_API_KEY%>"
           value="<%=request.getAttribute(Constants.FACEBOOK_API_KEY)%>">
    <input type="hidden" id="<%=Constants.IS_LIVE_ENVIRONMENT%>"
           value="<%=request.getAttribute(Constants.IS_LIVE_ENVIRONMENT)%>">
    <input type="hidden" id="<%=Constants.PAYPAL_ACCOUNT%>"
           value="<%=request.getAttribute(Constants.PAYPAL_ACCOUNT)%>">
    <input type="hidden" id="<%=Constants.STRIPE_PUBLIC_KEY%>"
           value="<%=request.getAttribute(Constants.STRIPE_PUBLIC_KEY)%>">
    <input type="hidden" id="<%=Constants.PRODUCT_NAME%>"
           value="<%=request.getAttribute(Constants.PRODUCT_NAME)%>">
    <input type="hidden" id="<%=Constants.UPLOAD_DIR%>"
           value="<%=request.getAttribute(Constants.UPLOAD_DIR)%>">
    <input type="hidden" id="<%=Constants.UPLOAD_TYPE%>"
           value="<%=request.getAttribute(Constants.UPLOAD_TYPE)%>">
    <input type="hidden" id="<%=CommandConstants.UPLOAD_TYPE_PARAM_NAME%>"
           value="<%=request.getAttribute(CommandConstants.UPLOAD_TYPE_PARAM_NAME)%>">

    <input type="hidden" id="<%=Constants.HOST_NAME_VALUE%>"
           value="<%=request.getAttribute(Constants.HOST_NAME_VALUE)%>">

    <input type="hidden" id="<%=Constants.VAT_RATE_VALUE%>"
           value="<%=request.getAttribute(Constants.VAT_RATE_VALUE)%>">

    <input type="hidden" id="<%=Constants.HELP_HOST%>"
           value="<%=request.getAttribute(Constants.HELP_HOST)%>">

    <input type="hidden" id="<%=Constants.SUPPORT_EMAIL%>"
           value="<%=request.getAttribute(Constants.SUPPORT_EMAIL)%>">

    <input type="hidden" id="<%=Constants.PHONE%>"
           value="<%=request.getAttribute(Constants.PHONE)%>">

    <input type="hidden" id="<%=Constants.ISAUTOMATIC%>"
           value="<%=request.getAttribute(Constants.ISAUTOMATIC)%>">

    <input type="hidden" id="<%=Constants.ISAUTOMATICAPPROVAL%>"
           value="<%=request.getAttribute(Constants.ISAUTOMATICAPPROVAL)%>">

    <input type="hidden" id="<%=Constants.ISAUTOMATICWAITINGFORAPPROVAL%>"
           value="<%=request.getAttribute(Constants.ISAUTOMATICWAITINGFORAPPROVAL)%>">

    <input type="hidden" id="<%=Constants.VALIDATE_TASK_START%>"
           value="<%=request.getAttribute(Constants.VALIDATE_TASK_START)%>">

    <input type="hidden" id="<%=Constants.VALIDATE_TASK_END%>"
           value="<%=request.getAttribute(Constants.VALIDATE_TASK_END)%>">

    <input type="hidden" id="<%=Constants.VALIDATE_MAXIMUM_HOURS%>"
           value="<%=request.getAttribute(Constants.VALIDATE_MAXIMUM_HOURS)%>">

    <input type="hidden" id="<%=Constants.VALIDATE_DAY_OFF%>"
           value="<%=request.getAttribute(Constants.VALIDATE_DAY_OFF)%>">

    <input type="hidden" id="<%=Constants.MAXIMUM_HOURS%>"
           value="<%=request.getAttribute(Constants.MAXIMUM_HOURS)%>">

    <input type="hidden" id="<%=Constants.VALIDATE_PAST_TIMSHEET%>"
           value="<%=request.getAttribute(Constants.VALIDATE_PAST_TIMSHEET)%>">

    <input type="hidden" id="<%=Constants.PAST_TIMSHEET_DAYS%>"
           value="<%=request.getAttribute(Constants.PAST_TIMSHEET_DAYS)%>">

    <input type="hidden" id="<%=Constants.VALIDATE_FUTURE_TIMESHEET%>"
           value="<%=request.getAttribute(Constants.VALIDATE_FUTURE_TIMESHEET)%>">

    <input type="hidden" id="<%=Constants.FUTURE_TIMESHEET_DAYS%>"
           value="<%=request.getAttribute(Constants.FUTURE_TIMESHEET_DAYS)%>">

    <input type="hidden" id="<%=Constants.VALIDATE_TIMESLOT%>"
           value="<%=request.getAttribute(Constants.VALIDATE_TIMESLOT)%>">

    <input type="hidden" id="<%=Constants.VALIDATE_HOLIDAY%>"
           value="<%=request.getAttribute(Constants.VALIDATE_HOLIDAY)%>">

    <input type="hidden" id="<%=Constants.VALIDATE_lEAVE_REQUEST%>"
           value="<%=request.getAttribute(Constants.VALIDATE_lEAVE_REQUEST)%>">

    <input type="hidden" id="<%=Constants.TIMESHEET_COMMENT_REQUIRED%>"
           value="<%=request.getAttribute(Constants.TIMESHEET_COMMENT_REQUIRED)%>">

    <input type="hidden" id="<%=Constants.TIMESHEET_WEEK_START%>"
           value="<%=request.getAttribute(Constants.TIMESHEET_WEEK_START)%>">

    <input type="hidden" id="<%=Constants.OVERALL_DATE_PICKER_WEEK_START%>"
           value="<%=request.getAttribute(Constants.OVERALL_DATE_PICKER_WEEK_START)%>">

    <input type="hidden" id="<%=Constants.SHOW_COMPLETED_TASKS%>"
           value="<%=request.getAttribute(Constants.SHOW_COMPLETED_TASKS)%>">

    <input type="hidden" id="<%=Constants.SHOW_HOUR_TYPE_DROPDOWN%>"
           value="<%=request.getAttribute(Constants.SHOW_HOUR_TYPE_DROPDOWN)%>">

    <input type="hidden" id="<%=Constants.ENABLE_MULTIPLE_TIMER_INTSTANCES%>"
           value="<%=request.getAttribute(Constants.ENABLE_MULTIPLE_TIMER_INTSTANCES)%>">

    <input type="hidden" id="<%=Constants.SAVE_TIMER_INTO_TIMESHEET_AUTOMATICALLY%>"
           value="<%=request.getAttribute(Constants.SAVE_TIMER_INTO_TIMESHEET_AUTOMATICALLY)%>">

    <input type="hidden" id="<%=Constants.MESSAGE_CENTER_ENABLED%>"
           value="<%=request.getAttribute(Constants.MESSAGE_CENTER_ENABLED)%>">
    <input type="hidden" id="<%=Constants.THEME_FOR_SYSTEM%>"
           value="<%=request.getAttribute(Constants.THEME_FOR_SYSTEM)%>">
    <input type="hidden" id="<%=Constants.LANGUAGE_FOR_USER%>"
           value="<%=request.getAttribute(Constants.LANGUAGE_FOR_USER)%>">
    <input type="hidden" id="<%=Constants.LATEST_SERVER_UPLOAD_VERSION%>"
           value="<%=request.getAttribute(Constants.LATEST_SERVER_UPLOAD_VERSION)%>"/>
    <input type="hidden" id="<%=Constants.SESSION_LENGTH%>"
           value="<%=request.getAttribute(Constants.SESSION_LENGTH)%>">
    <input type="hidden" id="<%=Constants.FREE_TRIAL_DAYS_LEFT%>"
           value="<%=request.getAttribute(Constants.FREE_TRIAL_DAYS_LEFT)%>">
    <input type="hidden" id="<%=Constants.IS_PAID_COMPANY%>"
           value="<%=request.getAttribute(Constants.IS_PAID_COMPANY)%>">
    <input type="hidden" id="<%=Constants.DEFAULT_CURRENCY_CODE%>"
           value="<%=request.getAttribute(Constants.DEFAULT_CURRENCY_CODE)%>">
    <input type="hidden" id="<%=Constants.ENABLE_SALES_BACKEND_FOR_USER%>"
           value="<%=request.getAttribute(Constants.ENABLE_SALES_BACKEND_FOR_USER)%>">
    <input type="hidden" id="<%=Constants.ENABLE_SUPPORT_BACKEND_FOR_USER%>"
           value="<%=request.getAttribute(Constants.ENABLE_SUPPORT_BACKEND_FOR_USER)%>">
    <input type="hidden" id="<%=Constants.ENABLE_ADMIN_BACKEND_FOR_USER%>"
           value="<%=request.getAttribute(Constants.ENABLE_ADMIN_BACKEND_FOR_USER)%>">
    <input type="hidden" id="<%=Constants.ENABLE_PARTNER_ADMIN_BACKEND_FOR_USER%>"
           value="<%=request.getAttribute(Constants.ENABLE_PARTNER_ADMIN_BACKEND_FOR_USER)%>">
    <%--<input type="hidden" id="<%=Constants.ENABLE_PDF_BACKEND_FOR_USER%>"
           value="<%=request.getAttribute(Constants.ENABLE_PDF_BACKEND_FOR_USER)%>">--%>
    <input type="hidden" id="<%=Constants.ENABLE_DEVELOPER_BACKEND_FOR_USER%>"
           value="<%=request.getAttribute(Constants.ENABLE_DEVELOPER_BACKEND_FOR_USER)%>">
    <input type="hidden" id="<%=Constants.PROMOTIONAL_CODE%>"
           value="<%=request.getAttribute(Constants.PROMOTIONAL_CODE)%>">
    <input type="hidden" id="<%=Constants.IS_ACTIVE_MEETING_MINUTES%>"
           value="<%=request.getAttribute(Constants.IS_ACTIVE_MEETING_MINUTES)%>">
    <input type="hidden" id="<%=Constants.SHOW_GOOGLE_TALK_CHAT%>"
           value="<%=request.getAttribute(Constants.SHOW_GOOGLE_TALK_CHAT)%>">
    <input type="hidden" id="<%=Constants.RESOURCE_UTILIZATION_ENABLED%>"
           value="<%=request.getAttribute(Constants.RESOURCE_UTILIZATION_ENABLED)%>">
    <input type="hidden" id="<%=Constants.SHOW_SCORE_CALCULATION%>"
           value="<%=request.getAttribute(Constants.SHOW_SCORE_CALCULATION)%>">
    <input type="hidden" id="<%=Constants.CUSTOM_RATE_ENABLE%>"
           value="<%=request.getAttribute(Constants.CUSTOM_RATE_ENABLE)%>">
    <input type="hidden" id="<%=Constants.TRAINING_CENTER_ENABLED%>"
           value="<%=request.getAttribute(Constants.TRAINING_CENTER_ENABLED)%>">
    <input type="hidden" id="<%=Constants.USER_CITY%>"
           value="<%=request.getAttribute(Constants.USER_CITY)%>">
    <input type="hidden" id="<%=Constants.USER_COUNTRY%>"
           value="<%=request.getAttribute(Constants.USER_COUNTRY)%>">
    <input type="hidden" id="<%=Constants.IS_EMPLOYEE%>"
           value="<%=request.getAttribute(Constants.IS_EMPLOYEE)%>">
    <input type="hidden" id="<%=Constants.COMPANY_COUNTRY_CODE%>"
           value="<%=request.getAttribute(Constants.COMPANY_COUNTRY_CODE)%>">
    <input type="hidden" id="<%=Constants.DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_START_TIME%>"
           value="<%=request.getAttribute(Constants.DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_START_TIME)%>">
    <input type="hidden" id="<%=Constants.DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_END_TIME%>"
           value="<%=request.getAttribute(Constants.DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_END_TIME)%>">
    <input type="hidden" id="<%=Constants.SUPER_USER%>"
           value="<%=request.getAttribute(Constants.SUPER_USER)%>">
    <input type="hidden" id="<%=Constants.TIMESHEET_DF%>"
           value="<%=request.getAttribute(Constants.TIMESHEET_DF)%>">
    <input type="hidden" id="<%=Constants.TIMESHEET_VALIDATE_EST%>"
           value="<%=request.getAttribute(Constants.TIMESHEET_VALIDATE_EST)%>">
    <input type="hidden" id="<%=Constants.ACCOUNTING_VAT_RETURN_REPORT%>"
           value="<%=request.getAttribute(Constants.ACCOUNTING_VAT_RETURN_REPORT)%>">
    <input type="hidden" id="<%=Constants.DISABLE_PAYROLL_TRANSACTIONS%>"
           value="<%=request.getAttribute(Constants.DISABLE_PAYROLL_TRANSACTIONS)%>">

    <input type="hidden" id="<%=Constants.ALTERNATIVE_CALENDAR_ID%>"
           value="<%=request.getAttribute(Constants.ALTERNATIVE_CALENDAR_ID)%>">
    <input type="hidden" id="<%=Constants.ENABLE_WORLDPAY%>" value="true">
    <input type="hidden" id="<%=Constants.MONTHLY_TIMESHEET%>"
           value="<%=request.getAttribute(Constants.MONTHLY_TIMESHEET)%>">
    <input type="hidden" id="<%=Constants.STOREFRONT%>"
           value="<%=request.getAttribute(Constants.STOREFRONT)%>">
    <input type="hidden" id="<%=Constants.DOUBLE_MESSAGE_ENABLE%>"
           value="<%=request.getAttribute(Constants.DOUBLE_MESSAGE_ENABLE)%>">
    <input type="hidden" id="<%=Constants.MULTIPLE_SALES_PRICE_ENABLED%>"
           value="<%=request.getAttribute(Constants.MULTIPLE_SALES_PRICE_ENABLED)%>">
    <%--<input type="hidden" id="<%=Constants.PRODUCT_TABLE_CUSTOMIZATION%>"
           value="<%=request.getAttribute(Constants.PRODUCT_TABLE_CUSTOMIZATION)%>">--%>
    <input type="hidden" id="<%=Constants.ACCOUNTING_DISCOUNT_SCALE%>"
           value="<%=request.getAttribute(Constants.ACCOUNTING_DISCOUNT_SCALE)%>">
    <input type="hidden" id="<%=Constants.ACCOUNTING_PROGRESS_INVOICING_AMOUNT_SCALE%>"
           value="<%=request.getAttribute(Constants.ACCOUNTING_PROGRESS_INVOICING_AMOUNT_SCALE)%>">
    <input type="hidden" id="<%=Constants.EMPLOYEE_FORM_PERSONAL_ID%>"
           value="<%=request.getAttribute(Constants.EMPLOYEE_FORM_PERSONAL_ID)%>">
    <input type="hidden" id="<%=Constants.FACEBOOK_APP_ID%>"
           value="<%=request.getAttribute(Constants.FACEBOOK_APP_ID)%>">
    <input type="hidden" id="<%=Constants.LOGISTICS%>"
           value="<%=request.getAttribute(Constants.LOGISTICS)%>">
    <input type="hidden" id="<%=Constants.ACCOUNTING_MODULE%>"
           value="<%=request.getAttribute(Constants.ACCOUNTING_MODULE)%>">
    <input type="hidden" id="<%=Constants.ANY_DATA_MISSING%>"
           value="<%=request.getAttribute(Constants.ANY_DATA_MISSING)%>">
    <input type="hidden" id="<%=Constants.TAWK_TO_SITE_ID%>"
           value="<%=request.getAttribute(Constants.TAWK_TO_SITE_ID)%>">
    <input type="hidden" id="<%=Constants.HRMS_DOCUMENTS%>"
           value="<%=request.getAttribute(Constants.HRMS_DOCUMENTS)%>">
    <input type="hidden" id="<%=Constants.SETTINGS_ACCOUNTING_SETTINGS%>"
           value="<%=request.getAttribute(Constants.SETTINGS_ACCOUNTING_SETTINGS)%>">
    <input type="hidden" id="<%=Constants.PURCHASE_ORDER_CUSTOM_ITEM_RECIEVE_IMPORT%>"
           value="<%=request.getAttribute(Constants.PURCHASE_ORDER_CUSTOM_ITEM_RECIEVE_IMPORT)%>">
    <input type="hidden" id="<%=Constants.ENABLE_SWITCHABLE_LAYOUT%>"
           value="<%=request.getAttribute(Constants.ENABLE_SWITCHABLE_LAYOUT)%>">
    <input type="hidden" id="<%=Constants.IS_TEST_COMPANY%>"
           value="<%=request.getAttribute(Constants.IS_TEST_COMPANY)%>">
    <input type="hidden" id="<%=Constants.ENABLE_MONTLY_PLAN%>"
           value="<%=request.getAttribute(Constants.ENABLE_MONTLY_PLAN)%>">
    <input type="hidden" id="<%=Constants.PRORATA_BASED_ANNUAL_LEAVE%>"
           value="<%=request.getAttribute(Constants.PRORATA_BASED_ANNUAL_LEAVE)%>">
    <input type="hidden" id="<%=Constants.VAT_REGISTERED%>"
           value="<%=request.getAttribute(Constants.VAT_REGISTERED)%>">
    <input type="hidden" id="<%=Constants.VAT_ACCOUNTING_BASIS%>"
           value="<%=request.getAttribute(Constants.VAT_ACCOUNTING_BASIS)%>">
    <input type="hidden" id="<%=Constants.MULTI_QUOTE_CONVERTER_GROUP_BY_ITEM_CONFIG%>"
           value="<%=request.getAttribute(Constants.MULTI_QUOTE_CONVERTER_GROUP_BY_ITEM_CONFIG)%>">

    <input type="hidden" id="<%=Constants.DASHBOARD_WIDGETS_MAX_LIMIT%>"
           value="<%=request.getAttribute(Constants.DASHBOARD_WIDGETS_MAX_LIMIT)%>">
    <input type="hidden" id="<%=Constants.EMAIL_ACCOUNT_SET_UP%>"
           value="<%=request.getAttribute(Constants.EMAIL_ACCOUNT_SET_UP)%>">
</div>
