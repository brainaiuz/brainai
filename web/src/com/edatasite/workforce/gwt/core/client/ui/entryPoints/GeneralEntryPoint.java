package com.edatasite.workforce.gwt.core.client.ui.entryPoints;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.PermissionSettings;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionService;
import com.edatasite.workforce.gwt.core.client.rpc.module.WfmModuleSetting;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.UiSettings;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.security.ClientSecurityContext;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Jun 4, 2010
 * Time: 5:31:59 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class GeneralEntryPoint implements EntryPoint, Constants {

    private static final String LOADING_MESSAGE = "Loading-Message";

    public WfmModuleSetting moduleSetting;

    protected SinksContainerFactory containerFactory;
    protected String historyToken;
    protected MainLayout mainLayout;

    public static void onLogOut() {
        removeAllCookies();
    }

    public static void removeAllCookies() {
        ClientSecurityContext.get().setSessionId(null);
        Cookies.removeCookie(USER_NAME_COOKIE);
        Cookies.removeCookie(USER_PASSWORD_COOKIE);
        Cookies.removeCookie(SESSION_ID_COOKIE);
        Cookies.removeCookie(SERVICE_ID_COOKIE);
        Cookies.removeCookie(HASH_LINK_COOKIE);
        Cookies.removeCookie(USER_AVAILABILITY);
        Cookies.removeCookie(USER_FULLNAME);
        Cookies.removeCookie(IS_MULTI_COMPANY);
        Cookies.removeCookie(SECTION_HTML);
        RootPanel.get().clear();
        History.newItem("");
        Utils.userSettings.clear();
        Utils.redirect(GWT.getHostPageBaseURL() + "index.html");
    }

    public SinksContainerFactory getContainerFactory() {
        return containerFactory;
    }

    /**
     * It defines the theme of the system and applies it to the system.
     *
     * @return
     */
    public String getThemeStyle() {
        String style = Utils.userSettings.get(THEME_FOR_SYSTEM);
        if (style == null || style.equals("null") || style.isEmpty()) {
            style = UiSettings.THEMES[0].getDescription();
        }
        return style;
    }

    public MainLayout getMainLayout() {
        return mainLayout;
    }

    public abstract void initSinksContainerFactory();

    public SinksContainer onHistoryChanged(String historyToken, String tabName, String tabTitle) {
        History.newItem(historyToken, false);
        SinksContainer container = containerFactory.getSinksContainer(historyToken);
        if (!Utils.isNullOrEmpty(tabName)) {
            container.setDescription(tabName);
        }
        if (!Utils.isNullOrEmpty(tabTitle)) {
            container.setTitle(tabTitle);
        }
        if (!Utils.isNullOrEmpty(tabName) || !Utils.isNullOrEmpty(tabTitle)) {
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.UPDATE_TAB_TITLE, container, mainLayout.getNavToolBar());
        }
        return container;
    }

    public SinksContainer onHistoryChanged(String historyToken, String tabName) {
        return this.onHistoryChanged(historyToken, tabName, null);
    }

    public SinksContainer onHistoryChanged(String historyToken) {
        return this.onHistoryChanged(historyToken, null, null);
    }
    /**public SinksContainer onHistoryChanged(String historyToken, String title) {
        History.newItem(historyToken, false);
        SinksContainer container = containerFactory.getSinksContainer(historyToken);
        container.setDescription(title);

        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SELECT_TAB, container, mainLayout.getNavToolBar());
        return container;
    }

    public SinksContainer onHistoryChanged(String historyToken) {
        History.newItem(historyToken, false);
        SinksContainer container = containerFactory.getSinksContainer(historyToken);
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SELECT_TAB, container, mainLayout.getNavToolBar());
        return container;
    }*/

    public void onModuleLoad() {
        try {//Provide exception handling within the onModuleLoad method(by default handling works only when onModuleLoad method returns)
            Cookies.removeCookie(LAST_REQUEST_TIME);//There should be no rpc before this!
            initWfmCustomParams();

            mainLayout = MainLayout.get();
            String sessionId = Cookies.getCookie(SESSION_ID_COOKIE);

            if (sessionId != null && !sessionId.isEmpty()) {
                ClientSecurityContext.get().setSessionId(sessionId);

                obtainUserSettings();
                loadUserPermissions();

                String[] uriArray = Utils.getPathName().split("/");
                if (!"/Myaccount.html".equals(Utils.getPathName())) {
                    Cookies.setCookie(SECTION_HTML, uriArray[uriArray.length - 1]);
                }
            } else {
                Utils.redirect(GWT.getHostPageBaseURL() + "index.html");
            }

            History.addValueChangeHandler(event -> {
                historyToken = event.getValue().replace("%7C", "|");
                containerFactory.getSinksContainer(historyToken);
            });
        } catch (RuntimeException e) {

            if (!GWT.isScript()) {//if Web mode
                throw e;//ignoring try - catch block in Hosted Mode
            }
        }
    }

    protected void loadUserPermissions() {
        //Load current user's permissions, all permissions of his roles are accumulated
        RolePermissionService.App.get().getPermissionSettings(GWT.getModuleName(), new AbstractAsyncCallback<PermissionSettings>() {
            @Override
            public void failure(Throwable t) {
                initDefaultUserSettings();
            }

            @Override
            public void success(PermissionSettings settings) {
                Utils.setSettings(settings);
                initUserSettings();
            }
        });
    }

    protected void initUserSettings() {
        if (Utils.isHRMS() && Utils.hasPermission(PermissionConstants.HRMS_ONBOARDING_MANAGEMENT)) {
            loadOnboardingCustomSteps();
        } else if (Utils.isMC()) {
            loadEmailFolders();
        } /*else if (Utils.isDocuments()) {
            loadDocumentFolders();
        } */ else {
            initDefaultUserSettings();
        }
    }

    protected void loadOnboardingCustomSteps() {
    }

    protected void loadEmailFolders() {
    }

    protected void loadUserDashboards() {

    }

    /**
     * Get specific data from container which gwt loaded
     */
    protected void obtainUserSettings() {
        Utils.userSettings.put(USER_FULLNAME, getElementValue(USER_FULLNAME));
        Utils.userSettings.put(USER_INITIALNAME, getElementValue(USER_INITIALNAME));
        Utils.userSettings.put(FULL_NAME, getElementValue(FULL_NAME));
        Utils.userSettings.put(FIRST_NAME, getElementValue(FIRST_NAME));
        Utils.userSettings.put(USER_NAME, getElementValue(USER_NAME));
        Utils.userSettings.put(EMAIL, getElementValue(EMAIL));
        Utils.userSettings.put(USER_ID, getElementValue(USER_ID));
        Utils.userSettings.put(ACCESS_GRANTED, getElementValue(ACCESS_GRANTED));
        Utils.userSettings.put(COMPANY_NAME, getElementValue(COMPANY_NAME));
        Utils.userSettings.put(COMPANY_ID, getElementValue(COMPANY_ID));
        Utils.userSettings.put(WITHOUT_ENCRYPTED_COMPANY_ID, getElementValue(WITHOUT_ENCRYPTED_COMPANY_ID));
        Utils.userSettings.put(SESSION_TRACK_ID, getElementValue(SESSION_TRACK_ID));
        //setup
        Utils.userSettings.put(INITIAL_URL, getElementValue(INITIAL_URL));
        Utils.userSettings.put(PM_IS_SETUP, getElementValue(PM_IS_SETUP));
        Utils.userSettings.put(ACCOUNTING_IS_SETUP, getElementValue(ACCOUNTING_IS_SETUP));
        Utils.userSettings.put(MULTI_COMPANY_SUBSIDIARY, getElementValue(MULTI_COMPANY_SUBSIDIARY));
        Utils.userSettings.put(MULTIWAREHOUSE_ENABLED, getElementValue(MULTIWAREHOUSE_ENABLED));
        Utils.userSettings.put(PRODUCTION_ENABLED, getElementValue(PRODUCTION_ENABLED));
        Utils.userSettings.put(PO_IGNORE_MANAGER_APPROVAL, getElementValue(PO_IGNORE_MANAGER_APPROVAL));

        Utils.userSettings.put(ACCOUNTING_CALCULATION_SCALE, getElementValue(ACCOUNTING_CALCULATION_SCALE));
        Utils.userSettings.put(ACCOUNTING_CUSTOM_QUANTITY_SCALE, getElementValue(ACCOUNTING_CUSTOM_QUANTITY_SCALE));
        Utils.userSettings.put(ACCOUNTING_CUSTOM_PRICE_SCALE, getElementValue(ACCOUNTING_CUSTOM_PRICE_SCALE));
        Utils.userSettings.put(ACCOUNTING_CUSTOM_EXRATE_SCALE, getElementValue(ACCOUNTING_CUSTOM_EXRATE_SCALE));
        Utils.userSettings.put(ACCOUNTING_TAX_RATE_SCALE, getElementValue(ACCOUNTING_TAX_RATE_SCALE));
        Utils.userSettings.put(ACCOUNTING_DISCOUNT_SCALE, getElementValue(ACCOUNTING_DISCOUNT_SCALE));
        Utils.userSettings.put(ACCOUNTING_PROGRESS_INVOICING_AMOUNT_SCALE, getElementValue(ACCOUNTING_PROGRESS_INVOICING_AMOUNT_SCALE));
        Utils.userSettings.put(TRANSACTION_LOCKING_DATE, getElementValue(TRANSACTION_LOCKING_DATE));
        Utils.userSettings.put(TRANSACTION_LOCKING_SALES, getElementValue(TRANSACTION_LOCKING_SALES));
        Utils.userSettings.put(TRANSACTION_LOCKING_PURCHASES, getElementValue(TRANSACTION_LOCKING_PURCHASES));
        Utils.userSettings.put(TRANSACTION_LOCKING_BANKING, getElementValue(TRANSACTION_LOCKING_BANKING));
        Utils.userSettings.put(TRANSACTION_LOCKING_EMPLOYEES, getElementValue(TRANSACTION_LOCKING_EMPLOYEES));
        Utils.userSettings.put(TRANSACTION_LOCKING_ATTENDANCE, getElementValue(TRANSACTION_LOCKING_ATTENDANCE));
        Utils.userSettings.put(TRANSACTION_LOCKING_RECRUITMENT, getElementValue(TRANSACTION_LOCKING_RECRUITMENT));
        Utils.userSettings.put(TRANSACTION_LOCKING_PAYSLIPS, getElementValue(TRANSACTION_LOCKING_PAYSLIPS));
        Utils.userSettings.put(TRANSACTION_LOCKING_CASHADVANCES, getElementValue(TRANSACTION_LOCKING_CASHADVANCES));
        Utils.userSettings.put(TRANSACTION_LOCKING_ADDITIONALPAYMENTS, getElementValue(TRANSACTION_LOCKING_ADDITIONALPAYMENTS));
        Utils.userSettings.put(CUSTOM_TAX_NAME, getElementValue(CUSTOM_TAX_NAME));

        Utils.userSettings.put(IS_SUPPLIER, getElementValue(IS_SUPPLIER));
        Utils.userSettings.put(IS_CLIENT_CONTACT, getElementValue(IS_CLIENT_CONTACT));
        Utils.userSettings.put(BASE_CURRENCY, getElementValue(BASE_CURRENCY));
        Utils.userSettings.put(INVOICE_FIRST_VIEW, getElementValue(INVOICE_FIRST_VIEW));
        Utils.userSettings.put(PM_FIRST_VIEW, getElementValue(PM_FIRST_VIEW));
        Utils.userSettings.put(PA_FIRST_VIEW, getElementValue(PA_FIRST_VIEW));
        Utils.userSettings.put(LONG_DATE_FORMAT, getElementValue(LONG_DATE_FORMAT));//"MMM dd, yyyy [HH:mm]");
        Utils.userSettings.put(SHORT_DATE_FORMAT, getElementValue(SHORT_DATE_FORMAT));//"MMM dd, yyyy");
        Utils.userSettings.put(GOOGLE_APP_DOMAIN, getElementValue(GOOGLE_APP_DOMAIN));
        Utils.userSettings.put(GOOGLE_MARKETPLACE_USERS_IMPORT_POPUP_SHOW, getElementValue(GOOGLE_MARKETPLACE_USERS_IMPORT_POPUP_SHOW));
        Utils.userSettings.put(FACEBOOK_API_KEY, getElementValue(FACEBOOK_API_KEY));
        Utils.userSettings.put(IS_LIVE_ENVIRONMENT, getElementValue(IS_LIVE_ENVIRONMENT));
        Utils.userSettings.put(PAYPAL_ACCOUNT, getElementValue(PAYPAL_ACCOUNT));
        Utils.userSettings.put(STRIPE_PUBLIC_KEY, getElementValue(STRIPE_PUBLIC_KEY));
        Utils.userSettings.put(PRODUCT_NAME, getElementValue(PRODUCT_NAME));
        Utils.userSettings.put(UPLOAD_DIR, getElementValue(UPLOAD_DIR));
        Utils.userSettings.put(UPLOAD_TYPE, getElementValue(UPLOAD_TYPE));
        Utils.userSettings.put(CommandConstants.UPLOAD_TYPE_PARAM_NAME, getElementValue(CommandConstants.UPLOAD_TYPE_PARAM_NAME));
        Utils.userSettings.put(HOST_NAME_VALUE, getElementValue(HOST_NAME_VALUE));
        Utils.userSettings.put(VAT_RATE_VALUE, getElementValue(VAT_RATE_VALUE));
        Utils.userSettings.put(HELP_HOST, getElementValue(HELP_HOST));
        Utils.userSettings.put(SUPPORT_EMAIL, getElementValue(SUPPORT_EMAIL));
        Utils.userSettings.put(PHONE, getElementValue(PHONE));
        Utils.userSettings.put(ISAUTOMATIC, getElementValue(ISAUTOMATIC));
        Utils.userSettings.put(ISAUTOMATICAPPROVAL, getElementValue(ISAUTOMATICAPPROVAL));
        Utils.userSettings.put(ISAUTOMATICWAITINGFORAPPROVAL, getElementValue(ISAUTOMATICWAITINGFORAPPROVAL));
        Utils.userSettings.put(TIMESHEET_COMMENT_REQUIRED, getElementValue(TIMESHEET_COMMENT_REQUIRED));
        Utils.userSettings.put(VALIDATE_TASK_START, getElementValue(VALIDATE_TASK_START));
        Utils.userSettings.put(VALIDATE_TASK_END, getElementValue(VALIDATE_TASK_END));
        Utils.userSettings.put(VALIDATE_HOLIDAY, getElementValue(VALIDATE_HOLIDAY));
        Utils.userSettings.put(VALIDATE_lEAVE_REQUEST, getElementValue(VALIDATE_lEAVE_REQUEST));
        Utils.userSettings.put(VALIDATE_TIMESLOT, getElementValue(VALIDATE_TIMESLOT));
        Utils.userSettings.put(VALIDATE_MAXIMUM_HOURS, getElementValue(VALIDATE_MAXIMUM_HOURS));
        Utils.userSettings.put(VALIDATE_DAY_OFF, getElementValue(VALIDATE_DAY_OFF));
        Utils.userSettings.put(VALIDATE_PAST_TIMSHEET, getElementValue(VALIDATE_PAST_TIMSHEET));
        Utils.userSettings.put(PAST_TIMSHEET_DAYS, getElementValue(PAST_TIMSHEET_DAYS));
        Utils.userSettings.put(VALIDATE_FUTURE_TIMESHEET, getElementValue(VALIDATE_FUTURE_TIMESHEET));
        Utils.userSettings.put(FUTURE_TIMESHEET_DAYS, getElementValue(FUTURE_TIMESHEET_DAYS));
        Utils.userSettings.put(TIMESHEET_WEEK_START, getElementValue(TIMESHEET_WEEK_START));
        Utils.userSettings.put(OVERALL_DATE_PICKER_WEEK_START, getElementValue(OVERALL_DATE_PICKER_WEEK_START));
        Utils.userSettings.put(SHOW_COMPLETED_TASKS, getElementValue(SHOW_COMPLETED_TASKS));
        Utils.userSettings.put(SHOW_HOUR_TYPE_DROPDOWN, getElementValue(SHOW_HOUR_TYPE_DROPDOWN));
        Utils.userSettings.put(ENABLE_MULTIPLE_TIMER_INTSTANCES, getElementValue(ENABLE_MULTIPLE_TIMER_INTSTANCES));
        Utils.userSettings.put(SAVE_TIMER_INTO_TIMESHEET_AUTOMATICALLY, getElementValue(SAVE_TIMER_INTO_TIMESHEET_AUTOMATICALLY));
        Utils.userSettings.put(MAXIMUM_HOURS, getElementValue(MAXIMUM_HOURS));
        Utils.userSettings.put(MESSAGE_CENTER_ENABLED, getElementValue(MESSAGE_CENTER_ENABLED));
        Utils.userSettings.put(LANGUAGE_FOR_USER, getElementValue(LANGUAGE_FOR_USER));
        Utils.userSettings.put(LATEST_SERVER_UPLOAD_VERSION, getElementValue(LATEST_SERVER_UPLOAD_VERSION));
        Utils.userSettings.put(SESSION_LENGTH, getElementValue(SESSION_LENGTH));
        Utils.userSettings.put(IS_SETUP_SUPPROJECT, getElementValue(IS_SETUP_SUPPROJECT));
        Utils.userSettings.put(IS_SETUP_SUPPROJECT_TWO_LEVEL, getElementValue(IS_SETUP_SUPPROJECT_TWO_LEVEL));
        Utils.userSettings.put(IS_ACTIVE_MEETING_MINUTES, getElementValue(IS_ACTIVE_MEETING_MINUTES));
        Utils.userSettings.put(ENABLE_SALES_BACKEND_FOR_USER, getElementValue(ENABLE_SALES_BACKEND_FOR_USER));
        Utils.userSettings.put(ENABLE_SUPPORT_BACKEND_FOR_USER, getElementValue(ENABLE_SUPPORT_BACKEND_FOR_USER));
        Utils.userSettings.put(ENABLE_ADMIN_BACKEND_FOR_USER, getElementValue(ENABLE_ADMIN_BACKEND_FOR_USER));
        Utils.userSettings.put(ENABLE_PARTNER_ADMIN_BACKEND_FOR_USER, getElementValue(ENABLE_PARTNER_ADMIN_BACKEND_FOR_USER));
        Utils.userSettings.put(ENABLE_DEVELOPER_BACKEND_FOR_USER, getElementValue(ENABLE_DEVELOPER_BACKEND_FOR_USER));
        Utils.userSettings.put(PROMOTIONAL_CODE, getElementValue(PROMOTIONAL_CODE));
        Utils.userSettings.put(RESOURCE_UTILIZATION_ENABLED, getElementValue(RESOURCE_UTILIZATION_ENABLED));
        Utils.userSettings.put(SHOW_GOOGLE_TALK_CHAT, getElementValue(SHOW_GOOGLE_TALK_CHAT));
        if (moduleSetting != null && moduleSetting.getModuleStyle() != null) {
            Utils.userSettings.put(THEME_FOR_SYSTEM, moduleSetting.getModuleStyle());
        }
        Utils.userSettings.put(FREE_TRIAL_DAYS_LEFT, getElementValue(FREE_TRIAL_DAYS_LEFT));
        Utils.userSettings.put(IS_PAID_COMPANY, getElementValue(IS_PAID_COMPANY));
        Utils.userSettings.put(SHOW_SCORE_CALCULATION, getElementValue(SHOW_SCORE_CALCULATION));
        Utils.userSettings.put(CUSTOM_RATE_ENABLE, getElementValue(CUSTOM_RATE_ENABLE));
        Utils.userSettings.put(USER_CITY, getElementValue(USER_CITY));
        Utils.userSettings.put(USER_COUNTRY, getElementValue(USER_COUNTRY));
        Utils.userSettings.put(IS_EMPLOYEE, getElementValue(IS_EMPLOYEE));
        Utils.userSettings.put(COMPANY_COUNTRY_CODE, getElementValue(COMPANY_COUNTRY_CODE));
        Utils.userSettings.put(DEFAULT_CURRENCY_CODE, getElementValue(DEFAULT_CURRENCY_CODE));

        //Remove all settings elements;
        Utils.userSettings.put(TRAINING_CENTER_ENABLED, getElementValue(TRAINING_CENTER_ENABLED));
        Utils.userSettings.put(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_START_TIME, getElementValue(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_START_TIME));
        Utils.userSettings.put(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_END_TIME, getElementValue(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_END_TIME));
        Utils.userSettings.put(SUPER_USER, getElementValue(SUPER_USER));
        Utils.userSettings.put(TIMESHEET_DF, getElementValue(TIMESHEET_DF));
        Utils.userSettings.put(TIMESHEET_VALIDATE_EST, getElementValue(TIMESHEET_VALIDATE_EST));
        Utils.userSettings.put(ACCOUNTING_VAT_RETURN_REPORT, getElementValue(ACCOUNTING_VAT_RETURN_REPORT));
        Utils.userSettings.put(ALTERNATIVE_CALENDAR_ID, getElementValue(ALTERNATIVE_CALENDAR_ID));
        Utils.userSettings.put(FACEBOOK_APP_ID, getElementValue(FACEBOOK_APP_ID));
        Utils.userSettings.put(ENABLE_WORLDPAY, getElementValue(ENABLE_WORLDPAY));
        Utils.userSettings.put(MONTHLY_TIMESHEET, getElementValue(MONTHLY_TIMESHEET));
        Utils.userSettings.put(STOREFRONT, getElementValue(STOREFRONT));
        Utils.userSettings.put(DOUBLE_MESSAGE_ENABLE, getElementValue(DOUBLE_MESSAGE_ENABLE));
        Utils.userSettings.put(MULTIPLE_SALES_PRICE_ENABLED, getElementValue(MULTIPLE_SALES_PRICE_ENABLED));
//        Utils.userSettings.put(PRODUCT_TABLE_CUSTOMIZATION, getElementValue(PRODUCT_TABLE_CUSTOMIZATION));
        Utils.userSettings.put(DISABLE_PAYROLL_TRANSACTIONS, getElementValue(DISABLE_PAYROLL_TRANSACTIONS));
        Utils.userSettings.put(LOGISTICS, getElementValue(LOGISTICS));
        Utils.userSettings.put(EMPLOYEE_FORM_PERSONAL_ID, getElementValue(EMPLOYEE_FORM_PERSONAL_ID));
        Utils.userSettings.put(ANY_DATA_MISSING, getElementValue(ANY_DATA_MISSING));
        Utils.userSettings.put(TAWK_TO_SITE_ID, getElementValue(TAWK_TO_SITE_ID));
        Utils.userSettings.put(SETTINGS_ACCOUNTING_SETTINGS, getElementValue(SETTINGS_ACCOUNTING_SETTINGS));
        Utils.userSettings.put(HRMS_DOCUMENTS, getElementValue(HRMS_DOCUMENTS));
        Utils.userSettings.put(PURCHASE_ORDER_CUSTOM_ITEM_RECIEVE_IMPORT, getElementValue(PURCHASE_ORDER_CUSTOM_ITEM_RECIEVE_IMPORT));
        Utils.userSettings.put(ENABLE_SWITCHABLE_LAYOUT, getElementValue(ENABLE_SWITCHABLE_LAYOUT));
        Utils.userSettings.put(PRORATA_BASED_ANNUAL_LEAVE, getElementValue(PRORATA_BASED_ANNUAL_LEAVE));
        Utils.userSettings.put(IS_TEST_COMPANY, getElementValue(IS_TEST_COMPANY));
        Utils.userSettings.put(ENABLE_MONTLY_PLAN, getElementValue(ENABLE_MONTLY_PLAN));
        Utils.userSettings.put(ACCOUNTING_MODULE, getElementValue(ACCOUNTING_MODULE));
        Utils.userSettings.put(VAT_REGISTERED, getElementValue(VAT_REGISTERED));
        Utils.userSettings.put(VAT_ACCOUNTING_BASIS, getElementValue(VAT_ACCOUNTING_BASIS));
        Utils.userSettings.put(MULTI_QUOTE_CONVERTER_GROUP_BY_ITEM_CONFIG, getElementValue(MULTI_QUOTE_CONVERTER_GROUP_BY_ITEM_CONFIG));
        Utils.userSettings.put(DASHBOARD_WIDGETS_MAX_LIMIT, getElementValue(DASHBOARD_WIDGETS_MAX_LIMIT));
        Utils.userSettings.put(EMAIL_ACCOUNT_SET_UP, getElementValue(EMAIL_ACCOUNT_SET_UP));
    }

    protected void addTabListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BEFORE_REMOVE_TAB, mainLayout.getNavToolBar(), (sender, args) -> {
            SinksContainer container = (SinksContainer) args;
            container.removeWfmUiEvents();
            getContainerFactory().removeFromContainer(container.getName());
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SELECTION_CHANGE_TAB, mainLayout.getNavToolBar(), (sender, args) -> {
            SinksContainer container = (SinksContainer) args;

            MainLayout.get().clearDynamicTabsConfigs();
            History.newItem(container.getHistoryToken(), false);
            container.reInit();

            if (container.getWorkarea().getCurrentView() != null) {
                container.show(container.getWorkarea().getCurrentView().getName());
            } else {
                MainLayout.get().getSideNavBar().setSelection(container);
            }
            if (container.isDynamic()) {
                MainLayout.get().addDynamicContainer(container, false);
            }
        });
    }

    protected void initDefaultUserSettings() {
    }

    /**
     * Get specific data from container which gwt loaded
     */

    protected void removeFakeModules() {
        if (RootPanel.get(Constants.FAKE_MODULES) != null) {
            RootPanel.get().getElement().removeChild(RootPanel.get(FAKE_MODULES).getElement());
        }
        if (RootPanel.get(FAKE_MODULES) != null) {
            DOM.removeChild(RootPanel.get().getElement(), RootPanel.get(FAKE_MODULES).getElement());
        }
    }

    protected void removeLoadingBar() {
        if (RootPanel.get(LOADING_MESSAGE) != null) {
            RootPanel.get().getElement().removeChild(RootPanel.get(LOADING_MESSAGE).getElement());
        }
        if (RootPanel.get(LOADING_MESSAGE) != null) {
            DOM.removeChild(RootPanel.get().getElement(), RootPanel.get(LOADING_MESSAGE).getElement());
        }
    }


    protected String getElementValue(String elementId) {
        String value = null;
        if (DOM.getElementById(elementId) != null) {
            value = DOM.getElementProperty(DOM.getElementById(elementId), "value");
        }

        return value;
    }

    /**
     * <i>... This is method read wfm custom template settings ...</i>
     * <br/>
     * <i>... Write developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Created Date {14:28 11/05/2011} ...</i>
     * <br/>
     * <b>... if are you not understanding whay uses that mudule setting say with Dilshod.T ...</b>
     */
    protected void initWfmCustomParams() {
//        moduleSetting = Utils.getWfmCustomParams(GWT.getModuleName());
        moduleSetting = new WfmModuleSetting();
    }
}
