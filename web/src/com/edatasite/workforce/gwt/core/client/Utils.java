package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk.FraudPreventionData;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.module.WfmContainer;
import com.edatasite.workforce.gwt.core.client.rpc.module.WfmModuleSetting;
import com.edatasite.workforce.gwt.core.client.rpc.module.WfmModuleSettingConstants;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.enums.FileUploadType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.hijri.HijriCalc;
import com.edatasite.workforce.gwt.core.client.ui.hijri.SimpleHijriDate;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.xml.client.XMLParser;
import com.googlecode.gwt.crypto.bouncycastle.DataLengthException;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import gwt.material.design.client.constants.CssName;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.jquery.client.api.JQuery;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class Utils implements Constants {

    public static final String SCROLL_EVENT = "scrollToThis";
    public static final SelectItem[] THEMES = UiSettings.THEMES;
    private static Integer userDepartment;
    private static SelectItem userDepartmentAsSelectItem;
    public static Map<String, PropertyItem> properties = new HashMap<>();
    public static LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingMap = new LinkedHashMap<>();
    public static HashMap<String, String> moduleLocalizeMap = new LinkedHashMap<>();
    public static HashSet<String> enabledModules = new HashSet<>();
    private static final HashMap<String, HashMap<Integer, String>> relationViews = new HashMap<>();
    private static final HashMap<String, HashMap<String, ArrayList<Integer>>> relationQueue = new HashMap<>();

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final DateTimeFormat fpDateFormat = DateTimeFormat.getFormat("ddMMyyyy HH:mm:ss");
    public static boolean correctFormat = true;
    public static LinkedHashMap<String, String> userSettings = new LinkedHashMap<>();
    private static HashSet<String> userPermissions;
    private static HashSet<GenericSettingsEnum> userGenericSettings;
    private static NumberFormat numberFormat;
    private static final PseudoContainerRPC pseudoContainer = new PseudoContainerRPC();
    private static int defaultProjectID = 0;
    private static Integer maxFileUploadSize;
    private static final List<String> list;
    private static List<SelectItem> twilioNumbers;
    private static SelectItem userlocationAsSelectItem;
    private static List<AsteriskSettings> asteriskSettings;
    private static boolean offline = false;
    private static String userLanguage;
    private static Double maxStorage;
    private static Double usedStorage;

    static {
        THEMES[0].setName(wfmStrings.blue());
        THEMES[1].setName(wfmStrings.violet());
        THEMES[2].setName(wfmStrings.grey());
        THEMES[3].setName(wfmStrings.green());
        THEMES[4].setName(wfmStrings.maroon());
        THEMES[5].setName(wfmStrings.mediacom());
        THEMES[6].setName(wfmStrings.mediacom2());
        THEMES[7].setName(wfmStrings.orange());
        THEMES[8].setName(wfmStrings.teletech());

        list = new ArrayList<>();
        list.add(Constants.UI_TYPE_DROPDOWN);
        list.add(Constants.UI_TYPE_CHECKBOX);
        list.add(Constants.UI_TYPE_RADIOBUTTON);
    }

    public static PseudoContainerRPC getPseudoContainer() {
        return pseudoContainer;
    }

    public static boolean hashAccessForPMRole() {
        return Utils.hasRole(PM) && !(CompanyConstants.C7619.equals(Utils.getEncryptedCompanyID())); /*|| Integer.valueOf(22026).equals(Utils.getCompanyID()) || Integer.valueOf(24021).equals(Utils.getCompanyID()));*/
    }

    public static boolean hasRolesForAccounting() {
        return Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ACCOUNTANT) || hashAccessForPMRole() || Utils.hasRole(CLIENT);
    }

    public static boolean hasCrmRole() {
        return hasCrmRole(true) || hasCrmRole(false);
    }

    public static boolean hasCrmRole(boolean maxRoles) {
        return maxRoles ? hasRole(DR) || hasRole(ADMIN) || hasRole(SALESMAN) : hasRole(CUSTOMER_SERVICE_REPRESENTATIVE) || hasRole(SALESPERSON);
    }

    public static NumberFormat getNumberFormat() {
        if (numberFormat == null) {
            numberFormat = NumberFormat.getFormat(",##0.00");
        }

        return numberFormat;
    }

    public static String getUserName() {
        return userSettings.get(USER_NAME);
    }

    public static String getUserEmail() {
        return userSettings.get(EMAIL);
    }

    public static boolean showScoreCalculation() {
        return userSettings.get(SHOW_SCORE_CALCULATION) != null && (Boolean.parseBoolean(userSettings.get(SHOW_SCORE_CALCULATION)));
    }

    public static boolean isCustomRateEnable() {
        return userSettings.get(CUSTOM_RATE_ENABLE) != null && (Boolean.parseBoolean(userSettings.get(CUSTOM_RATE_ENABLE)));
    }

    public static boolean isEnableBonnardCustomization() {
        return hasGenericAccess(GenericSettingsEnum.ENABLE_BONNARD_CUSTOMIZATION);
    }

    public static boolean isEnablePaymentDepartment() {
        return hasGenericAccess(GenericSettingsEnum.PAYMENT_DEPARTMENT_ENABLED);
    }

    //Do not use this method!!! Serious security vulnerability. Instead get if this is the current user from server side
    @Deprecated
    public static Integer getUserID() {
        String userId = userSettings.get(USER_ID);
        if (userId == null) {
            return 0;
        }
        return Integer.parseInt(userId);
    }

    public static String getUserFullName() {
        return userSettings.get(USER_FULLNAME);
    }

    public static String getUserInitialName() {
        return userSettings.get(USER_INITIALNAME);
    }

    /**
     * Returns Encrypted company ID
     * For example for company number 1, RzmFEdQD/dk= is returned
     * See CompanyConstants
     *
     * @return
     */
    public static String getEncryptedCompanyID() {
        return String.valueOf(Utils.userSettings.get(COMPANY_ID));
    }

    public static String getCompanyID() {
        return Utils.userSettings.get(WITHOUT_ENCRYPTED_COMPANY_ID);
    }

    public static String getCompanyName() {
        return userSettings.get(COMPANY_NAME);
    }

    public static String getFullName() {
        return userSettings.get(FULL_NAME);
    }

    public static String getFirstName() {
        return userSettings.get(FIRST_NAME);
    }

    public static String getUserCity() {
        return userSettings.get(USER_CITY);
    }

    public static String getUserCountry() {
        return userSettings.get(USER_COUNTRY);
    }

    public static boolean isEmployee() {
        return userSettings.get(IS_EMPLOYEE) != null && Boolean.parseBoolean(userSettings.get(IS_EMPLOYEE));
    }

    public static String getCompanyrCountryCode() {
        return userSettings.get(COMPANY_COUNTRY_CODE);
    }

    public static String getParam(String module) {
        return userSettings.get(module);
    }

    public static boolean isAnyDataMissing() {
        return userSettings.get(ANY_DATA_MISSING) != null && Boolean.parseBoolean(userSettings.get(ANY_DATA_MISSING));
    }

    public static void logScroll(int x, int y) {

        if (MainLayout.get().getCurrentContainer() != null && MainLayout.get().getCurrentContainer().getWorkarea() != null && MainLayout.get().getCurrentContainer().getWorkarea().getCurrentView() != null) {
            MainLayout.get().getCurrentContainer().getWorkarea().getCurrentView().setLastScrollTop(y);
        }
    }

    public static String getFirstPage(String page) {
        if (hasRole(CLIENT)) {
            return HOME_PAGE;
        }

        return getParam(page) == null ? LANDING_PAGE : getParam(page);
    }

    public static String normalize(String s) {
        return s == null ? "" : s.replaceAll("[\\r\\n]", "").trim();
    }

    public static boolean adminOrDirector() {
        return hasRole(ADMIN) || hasRole(DR);
    }

    public static boolean isAdmin() {
        return hasRole(ADMIN);
    }

    //Do not use this method, instead use Permission Management system. See Aziz
    @Deprecated
    public static boolean hasRole(Integer role) {
        String roles = userSettings.get(ROLES);
        return containsInString(roles, role);
    }

    public static boolean hasOnlyRole(Integer role) {
        String roles = userSettings.get(ROLES);
        String[] roleArray = roles != null ? roles.split(",") : new String[0];
        return roleArray.length == 1 && roleArray[0] != null && roleArray[0].matches(Constants.REGEX_INTEGER) && Integer.valueOf(roleArray[0]).equals(role);
    }

    //Do not use this method, instead use Permission Management system. Aziz
    @Deprecated
    public static boolean hasRoles(Integer... rolesArr) {
        String roles = userSettings.get(ROLES);
        for (Integer role : rolesArr) {
            if (containsInString(roles, role)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasEitherRole(String roleCode) {
        String roles = userSettings.get(ROLE_CODES);
        return containsInString(roles, roleCode);
    }

    public static boolean isArabicLanguage() {
        return LocaleInfo.getCurrentLocale() != null && LocaleInfo.getCurrentLocale().isRTL();
    }

    public static HasHorizontalAlignment.HorizontalAlignmentConstant HORIZONTAL_ALIGNMENT_LEFT() {
        return LocaleInfo.getCurrentLocale().isRTL() ? HasHorizontalAlignment.ALIGN_RIGHT : HasHorizontalAlignment.ALIGN_LEFT;
    }

    public static boolean isDemoAccount() {
        return userSettings.get(USER_NAME) != null && userSettings.get(USER_NAME).equals(DEMOACCOUNT);
    }

    public static boolean isShowVatReturnReport() {
        return userSettings.get(ACCOUNTING_VAT_RETURN_REPORT) != null && Boolean.parseBoolean(userSettings.get(ACCOUNTING_VAT_RETURN_REPORT));
    }

    public static boolean isFromCRM() {
        return getPathName().contains("Crm.html");
    }

    // Incapsulates our rpc services mapping on server-side

    public static boolean isLocalhost() {
        return getLocationString().toString().startsWith("http://localhost") || getLocationString().toString().startsWith("http://127.0.0.1");
    }

    public static boolean isDevhost() {
        return getLocationString().toString().startsWith("https://dev.kpi.com");
    }

    public static String getRpcBaseUrl() {
        return GWT.getHostPageBaseURL() + "rpc";
    }

    public static native String getHostDomain() /*-{
        var hostname = $doc.location.hostname; // Get the hostname (e.g., apps.kpi.uz)
        return hostname; // Return the full hostname
    }-*/;

    // for cooconnect
    public static native String getHostNameURL() /*-{
        var port = $doc.location.port;
        if (port != "80") {
            port = ":" + port;
        }
        return $doc.location.protocol + "//" + $doc.location.hostname + port + "/";

    }-*/;

    public static native String getHostSubName() /*-{
        var hostname = $doc.location.hostname; // Get the hostname (e.g., subdomain.domain.com)
        var subdomain = hostname.split(".")[0]; // Extract the subdomain
        return subdomain; // Return only the subdomain
    }-*/;

    public static native void log(String log) /*-{
        if (console) {
            console.log(log)
        }
    }-*/;

    public static native void log(Object obj) /*-{
        if (console) {
            console.log(obj)
        }
    }-*/;

    public static native boolean validateEmail(String email, boolean isMulti) /*-{
        var reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        var reg_multi = /(((?:(?:\r\n)?[ \t])*(?:(?:(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*))*@(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*))*|(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*)*\<(?:(?:\r\n)?[ \t])*(?:@(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*))*(?:,@(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*))*)*:(?:(?:\r\n)?[ \t])*)?(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*))*@(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*))*\>(?:(?:\r\n)?[ \t])*)|(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*)*:(?:(?:\r\n)?[ \t])*(?:(?:(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*))*@(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*))*|(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*)*\<(?:(?:\r\n)?[ \t])*(?:@(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*))*(?:,@(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*))*)*:(?:(?:\r\n)?[ \t])*)?(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*))*@(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*))*\>(?:(?:\r\n)?[ \t])*)(?:,\s*(?:(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*))*@(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*))*|(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*)*\<(?:(?:\r\n)?[ \t])*(?:@(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*))*(?:,@(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*))*)*:(?:(?:\r\n)?[ \t])*)?(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|"(?:[^\"\r\\]|\\.|(?:(?:\r\n)?[ \t]))*"(?:(?:\r\n)?[ \t])*))*@(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*)(?:\.(?:(?:\r\n)?[ \t])*(?:[^()<>@,;:\\".\[\] \000-\031]+(?:(?:(?:\r\n)?[ \t])+|\Z|(?=[\["()<>@,;:\\".\[\]]))|\[([^\[\]\r\\]|\\.)*\](?:(?:\r\n)?[ \t])*))*\>(?:(?:\r\n)?[ \t])*))*)?;\s*))(, ?)?)+/;
        return (!isMulti && reg.test(email)) || (isMulti && reg_multi.test(email));
    }-*/;

    public static String formatMinutes(Integer minutes) {
        if (minutes == null) {
            return "00:00";
        }

        int hours = minutes / 60;
        int mins = minutes % 60;

        String strHours = Math.abs(hours) < 10 ? (hours < 0 ? "-" : "") + "0" + Math.abs(hours) : "" + hours;
        String strMinutes = Math.abs(mins) < 10 ? "0" + Math.abs(mins) : "" + Math.abs(mins);

        return strHours + ":" + strMinutes;
    }

    public static String formatMinutes(int minutes) {
        return formatMinutes(Integer.valueOf(minutes));
    }

    public static String getFirstTwoLetters(String name) {
        if (name != null) {
            StringBuilder result = new StringBuilder();
            name = name.replaceAll("\\s+", " ");
            String[] myName = name.split(" ");
            for (int i = 0; i < myName.length && i < 2; i++) {
                result.append(myName[i].toUpperCase().charAt(0));
            }
            return result.toString();
        } else {
            return "";
        }
    }

    public static Integer parseMinutes(String minutes) throws NumberFormatException, StringIndexOutOfBoundsException {
        if (minutes == null || minutes.equals("")) {
            correctFormat = true;
            return 0;
        }
        String[] parts = new String[2];
        int h = 0;
        int m = 0;
        int qw = 0;
        char[] splitters = new char[]{':', '.', ',', ' '};

        char splitter = 0;
        for (char splitter1 : splitters) {
            if (minutes.indexOf(splitter1) != -1) {
                splitter = splitter1;
                qw++;
                if (qw == 2) {
                    h = 0;
                    correctFormat = false;
                    return 0;
                }
            }
        }
        try {
            if (splitter != 0) {
                minutes = minutes.replace(splitter, ':');
                splitter = ':';
                int k = 0;
                for (int i = 0; i < minutes.length(); i++) {
                    if (":,. ".indexOf(minutes.charAt(i)) != -1) {
                        k++;
                        if (k > 1 || i == 0 || i == (minutes.length() - 1)) { // ex->   :45 || 5: || (9:2: || 9::2)
                            m = Integer.parseInt(minutes); // for exeption
                        }
                    }
                }
                parts = minutes.split(String.valueOf(splitter));
                if (parts.length > 2) {
                    m = Integer.parseInt(minutes); // for exeption
                }

            } else if (minutes.contains("h") || minutes.contains("m")) {

                int minIndex = minutes.indexOf("m");
                int hourIndex = minutes.indexOf("h");

                if (hourIndex != -1 && minIndex != -1) {
                    int r = 0;
                    for (int i = 0; i < minutes.length(); i++) {
                        if ("hm".indexOf(minutes.charAt(i)) != -1) {  //ex-> 2hh8m
                            r++;
                            if (r > 2) {
                                m = Integer.parseInt(minutes); // for exeption
                            }
                        }
                    }
                    if ((hourIndex < minIndex) && hourIndex != 0) {
                        h = Integer.parseInt(minutes.substring(0, hourIndex));
                        m = Integer.parseInt(minutes.substring(hourIndex + 1, minIndex));
                    } else {
                        m = Integer.parseInt(minutes); // for exeption
                    }
                } else if (hourIndex != -1) {
                    if (hourIndex != 0 && ((hourIndex + 1) == minutes.length())) {
                        h = Integer.parseInt(minutes.substring(0, hourIndex));
                    } else {
                        m = Integer.parseInt(minutes); // for exeption
                    }
                } else {
                    if (minIndex != 0 && ((minIndex + 1) == minutes.length())) {
                        m = Integer.parseInt(minutes.substring(0, minIndex));
                    } else {
                        m = Integer.parseInt(minutes); // for exeption
                    }
                }
            } else {
                try {
                    h = Integer.parseInt(minutes);
                } catch (NumberFormatException exc) {
                    correctFormat = false;
                    return 0;
                }
            }

            if (parts[0] != null && parts[1] != null) {

                for (int i = 0; i < parts[0].length(); i++) {
                    if ("0123456789".indexOf(parts[0].charAt(i)) == -1) {
                        m = Integer.parseInt(minutes); // for exeption
                    }
                }

                h = Integer.parseInt(parts[0]);

                for (int i = 0; i < parts[1].length(); i++) {
                    if ("0123456789".indexOf(parts[1].charAt(i)) == -1) {
                        m = Integer.parseInt(minutes);   // for exeption
                    }
                }
                m = Integer.parseInt(parts[1]);

                if (m >= 60) {
                    h += m / 60;
                    m = m % 60;
                }
            }

        } catch (StringIndexOutOfBoundsException | NumberFormatException exc) {
            correctFormat = false;
            return 0;
        }
        correctFormat = true;
        return h * 60 + m;
    }

    public static AnchorParam parseAnchorParam(String string) {
        if (string == null || "".equals(string)) {
            return null;
        }
        String[] ray = string.split("/");
        String[] tokensArray = new String[ray.length - 1];
        System.arraycopy(ray, 1, tokensArray, 0, ray.length - 1);
        if (tokensArray.length == 0) {
            tokensArray = new String[]{""};
        }
        return new AnchorParam(ray[0], tokensArray);
    }

    public static String getSinksContainer(String string) {
        if (string != null) {
            string = string.replace("%7C", "|");
        }
        if (string != null && string.contains("|")) {
            return string.substring(0, string.indexOf("|"));
        } else if (string != null && string.contains("/")) {
            return string.substring(0, string.indexOf("/"));
        } else {
            return string;
        }
    }

    public static String getSinkName(String string) {
        if (string != null) {
            string = string.replace("%7C", "|");
        }
        if (string != null && string.contains("|") && string.indexOf("|") <= string.length()) {
            if (string.contains("/") && (string.indexOf("|") < string.indexOf("/"))) {
                return string.substring(string.indexOf("|") + 1, string.indexOf("/"));
            } else {
                return string.substring(string.indexOf("|") + 1);
            }
        } else {
            return "";
        }
    }

    public static native String getParamString() /*-{
        return $wnd.location.search;
    }-*/;

    public static native void setShowContextMenuOnlyInWorkspaceAndCrm(boolean b) /*-{
        return $wnd.setShowContextMenu(b);
    }-*/;

    public static native String getAnchorString()/*-{
        return $wnd.location.hash
    }-*/;

    public static native JavaScriptObject getLocationString()/*-{
        return $wnd.location
    }-*/;

    /**
     * Returns only the name of the section like 'Hrms.html, Documents.html'
     *
     * @return
     */
    public static native String getPathName()/*-{
        return $wnd.location.pathname
    }-*/;

    public static native void redirect(String url) /*-{
        $wnd.location = url;
    }-*/;

    public static native void openURLCommon(String url) /*-{
        $wnd.open(url, "_blank");
    }-*/;

    public static native JavaScriptObject openPopupWindow(String url) /*-{
        return $wnd.open(url, "popUpWindow", "height=430,width=430,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes");
    }-*/;

    public static native void closePopupWindow(JavaScriptObject window) /*-{
        var popupWindow = window;
        if (false === popupWindow.closed) {
            popupWindow.close();
        }
    }-*/;

    public static void openURL(String url) {
        if (Utils.isSafari()) {
            Utils.openURLSafari(url);
        } else {
            Utils.openURLCommon(url);
        }
    }

    public static native void openURLSafari(String url) /*-{

        $wnd.location = url;
    }-*/;

    public static native void openURLCurrentTab(String url) /*-{
        $wnd.open(url, "_self", "");
    }-*/;

    public static native int getScreenWidth() /*-{
        return $wnd.screen.width;
    }-*/;

    public static native int getIEScreenWidth() /*-{
        return $doc.body.offsetWidth;
    }-*/;

    public static native void triggerCustomJSEvent(String customEventName) /*-{
        var event = new Event(customEventName);
        $wnd.dispatchEvent(event);
    }-*/;

    public static String getFirstAvailableSectionName() {
        String result = null;
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
            result = "Accounting.html";
            return result;
        } else if (Utils.hasPermission(PermissionConstants.HRMS_MAIN_MENU)) {
            result = "Hrms.html";
            return result;
        } else if (Utils.hasPermission(PermissionConstants.CRM_MAIN_MENU)) {
            result = "Crm.html";
            return result;
        } else if (Utils.hasPermission(PermissionConstants.DOCUMENTS_MAIN_MENU)) {
            result = "Documents.html";
            return result;
        } else if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            result = "ProjectManagement.html";
            return result;
        } else if (Utils.hasPermission(PermissionConstants.SETTINGS_MAIN_MENU)) {
            result = "Settings.html";
            return result;
        }
        return result;
    }

    public static Integer getDefaultProjectID() {
        return defaultProjectID;
    }

    public static void setDefaultProjectID(Integer defaultProjectID) {
        Utils.defaultProjectID = defaultProjectID;
    }

    public static Integer getMaxFileUploadSize() {
        return maxFileUploadSize;
    }

    public static void setMaxFileUploadSize(Integer maxFileUploadSize) {
        Utils.maxFileUploadSize = maxFileUploadSize;
    }

    public static native void scrollToTop() /*-{
        $wnd.scroll(0, 0);
    }-*/;

    /**
     * Close browser window.
     */
    public static native void closeBrowser() /*-{
        $wnd.close();
    }-*/;

    public static boolean containsInArray(Object[] array, Object item) {
        if (array == null) {
            return false;
        }
        for (Object anArray : array) {
            if (anArray.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsInString(String roles, Integer role) {
        if (roles == null) {
            return false;
        }
        String[] roleArray = roles.split(",");
        return containsInArray(roleArray, role.toString());
    }

    public static boolean containsInString(String roles, String roleCode) {
        if (roles == null) {
            return false;
        }
        String[] splited = roles.split(",");
        String[] roleArray = new String[splited.length];
        int i = 0;
        for (String role : splited) {
            roleArray[i++] = role.replace("'", "");
        }
        return containsInArray(roleArray, roleCode);
    }

    /**
     * Formats given double value to generally accepted standart.
     * Rounds number.
     * Returns formatted string. Ex. 1000000.0000 = 1,000,000.00.
     *
     * @param value
     * @return
     */
    public static String formatDouble(double value) {
        return getNumberFormat().format(value);
    }

    /**
     * Formats given double value to generally accepted standart.
     * Rounds number.
     * Returns formatted string. Ex. 1000000.0000 = 1,000,000.00.
     *
     * @param value
     * @return
     */
    public static String formatDouble(Double value) {
        return getNumberFormat().format(value.doubleValue());
    }

    /**
     * Returns double value formatted by formatDouble(double value) function.
     *
     * @param value
     * @return
     */
    public static double parseFormatted(String value) throws IllegalArgumentException {
        if (value == null || "".equals(value)) {
            throw new IllegalArgumentException("Value can not be null or empty.");
        }

        return getNumberFormat().parse(value);
    }

    /**
     * Used for sending request with parameters.
     * Mainly to PDF Handlers.
     *
     * @param complexPanel panel should be the part of your View.(HorizontalPanel for example)
     * @param url          handler or servlet path
     * @param parameters   parameters that you want path to server.
     * @param target       target of post. ("_blank")
     */
    public static void sendPDFOrExcelRequest(ComplexPanel complexPanel, String url, HashMap<String, String> parameters, String target) {
        if (isEnablePdfPreView()) {
            LoadingPanel.loading(true);
            PdfViewer.ensurePrintFrame(complexPanel);
            target = "pdfPrintFrame";
        }

        final PostFormPanel post = new PostFormPanel(url, target);
        setPostData(post, parameters);
        complexPanel.add(post);
        post.submit();

        final ComplexPanel finalComplexPanel = complexPanel;
        post.addSubmitCompleteHandler(event -> finalComplexPanel.remove(post));
        finalComplexPanel.remove(post);
    }

    public static void sendPDFOrExcelRequest(final ComplexPanel complexPanel, String url, LinkedHashMap<String, String> parameters, String target) {
        final PostFormPanel post = new PostFormPanel(url, target);
        setPostData(post, parameters);
        complexPanel.add(post);
        post.submit();
        post.addSubmitCompleteHandler(event -> complexPanel.remove(post));
        complexPanel.remove(post);
    }

    /**
     * Used for sending request with parameters.
     * Mainly to CSV Handlers.
     *
     * @param complexPanel panel should be the part of your View.(HorizontalPanel for example)
     * @param url          handler or servlet path
     * @param parameters   parameters that you want path to server.
     * @param target       target of post. ("_blank")
     */
    public static void sendCSVRequest(final ComplexPanel complexPanel, String url, HashMap<String, String> parameters, String target) {
        sendPDFOrExcelRequest(complexPanel, url, parameters, target);
    }

    /**
     * Inserts parameter map to PostFormPanel.
     *
     * @param postFormPanel
     * @param parameterMap
     */
    private static void setPostData(PostFormPanel postFormPanel, Map<String, String> parameterMap) {
        for (String key : parameterMap.keySet()) {
            if (parameterMap.get(key) != null) {
                String parameter = parameterMap.get(key);
                postFormPanel.setParameter(key, parameter);
            }
        }
    }

    private static void setPostData(PostFormPanel postFormPanel, LinkedHashMap<String, String> parameterMap) {
        for (String key : parameterMap.keySet()) {
            if (parameterMap.get(key) != null) {
                String parameter = parameterMap.get(key);
                postFormPanel.setParameter(key, parameter);
            }
        }
    }

    public static String getAssigneesCommaSep(String[] assignees) {
        if (assignees == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < assignees.length; i++) {
            String assign = assignees[i];
            sb.append(assign);
            if (i != assignees.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public static native String getCurrentYear()/*-{
        var d = new Date();
        return d.getFullYear()
    }-*/;

    public static native String getUserAgent() /*-{
        return $wnd.navigator.userAgent.toLowerCase();
    }-*/;

    public static String getPayPalLink() {

        if (isLiveEnvironment()) {
            return paypal_LINK_Live;
        } else {
            return paypal_LINK_Test;
        }
    }

    public static String getPayPalAccount() {

        if (userSettings.get(PAYPAL_ACCOUNT) != null) {
            return userSettings.get(PAYPAL_ACCOUNT);
        }
        return paypal_ACCOUNT_Live;
    }

    public static String getStripePublicKey() {
        return userSettings.get(STRIPE_PUBLIC_KEY);
    }

    public static String getWorldPayLink() {
        return WORLDPAY_LINK_Live;
    }

    public static String getWorldPayTestModeValue() {
        return "0";
    }

    public static String getWorldPayAccount() {
        return WORLDPAY_ACCOUNT_Live;
    }

    public static Boolean showWorldPay() {
        return true;
    }

    public static SelectItem[] sortSelectItemByName(SelectItem[] items) {
        if (items != null && items.length > 0) {
            for (int i = items.length; --i >= 0; ) {
                for (int j = 0; j < i; j++) {
                    int checker = items[j].getName().compareTo(items[j + 1].getName());
                    if (checker > 0) {
                        SelectItem t = items[j];
                        items[j] = items[j + 1];
                        items[j + 1] = t;
                    }
                }
            }
        }
        return items;
    }

    public static HTML getPhoneCallFormat2(String phNumber) {
        if (phNumber != null && !"".equals(phNumber)) {
            if ("n/a".equals(phNumber.toLowerCase().trim())) {
                phNumber = "";
            }
        }
        String s = cleanPhoneNumber(phNumber);
        if (phNumber != null && s != null) {
            phNumber = phNumber.replace("(", "");
            phNumber = phNumber.replace(")", "");
            phNumber = phNumber.replace(" ", "");
            phNumber = phNumber.replace(":", "");
            phNumber = phNumber.replace(";", "");
            phNumber = phNumber.replace(",", "");
            phNumber = phNumber.replace(".", "");
            phNumber = phNumber.replace("|", "");
            phNumber = phNumber.replaceAll("||", "");
            phNumber = phNumber.replace("+", "");
            if (!phNumber.startsWith("+")) {
                phNumber = "+" + phNumber;
            }
            return new HTML("<a href=\"callto://" + s + "\"><i class=\"ficon--phone\"></i>&nbsp<span>" + phNumber + "</span></a>");
        } else {
            return new HTML();
        }
    }

    public static String getPhoneCallFormatForListing(String phone) {
        if (phone != null && !"".equals(phone)) {
            if ("n/a".equals(phone.toLowerCase().trim())) {
                phone = "";
            }
        }
        if (phone != null && !"".equals(phone)) {
            if (phone.contains("|")) {
                String[] codes = phone.split("\\|");
                ArrayList<String> tels = new ArrayList<>();
                for (String s : codes) {
                    if (!"".equals(s.trim())) {
                        tels.add(s);
                    }
                }
                StringBuilder tel = new StringBuilder();
                String replacedTel = "";
                for (int i = 0; i < tels.size(); i++) {
                    replacedTel = cleanPhoneNumberNew(tels.get(i));
                    tel.append(i == 0 ? "+" + replacedTel : "-" + replacedTel);
                }
                if (tel.length() > 20) {
                    tel = new StringBuilder(tel.substring(0, 20));
                }
                return tel.toString().trim();
            } else {
                phone = cleanPhoneNumberNew(phone);
                if (phone.length() > 20) {
                    phone = phone.substring(0, 20);
                }
                return "+" + phone.trim();
            }
        }
        return phone;
    }

    public static String cleanPhoneNumberNew(String phone) {
        if (phone == null || "".equals(phone)) {
            return null;
        }
        phone = phone.replace("(", "");
        phone = phone.replace(")", "");
        phone = phone.replace("-", "");
        phone = phone.replace(" ", "");
        phone = phone.replace(":", "");
        phone = phone.replace(";", "");
        phone = phone.replace(",", "");
        phone = phone.replace(".", "");
        phone = phone.replace("|", "");
        phone = phone.replaceAll("||", "");
        phone = phone.replace("+", "");

        return phone;
    }

    public static String cleanPhoneNumber(String phone) {

        if (phone == null || "".equals(phone)) {
            return null;
        }
        phone = phone.replace("(", "");
        phone = phone.replace(")", "");
        phone = phone.replace("-", "");
        phone = phone.replace(" ", "");
        phone = phone.replace(":", "");
        phone = phone.replace(";", "");
        phone = phone.replace(",", "");
        phone = phone.replace(".", "");
        phone = phone.replace("|", "");
        phone = phone.replaceAll("||", "");
        phone = phone.replace("+", "");
        phone = "+" + phone;

        return phone;
    }

    public static String getThemeStyle() {
        String style = userSettings.get(THEME_FOR_SYSTEM);
        if (style == null || style.equals("null")) {
            style = UiSettings.THEMES[0].getDescription();
        }
        return style;
    }

    public static String getThemeName() {
        String style = userSettings.get(THEME_FOR_SYSTEM);
        if (style == null || style.equals("null")) {
            return UiSettings.THEMES[0].getName();
        }
        if (style.equals(THEMES[0].getDescription())) {
            return THEMES[0].getName();
        }
        if (style.equals(THEMES[1].getDescription())) {
            return THEMES[1].getName();
        }
        if (style.equals(THEMES[2].getDescription())) {
            return THEMES[2].getName();
        }
        if (style.equals(THEMES[8].getDescription())) {
            return THEMES[8].getName();
        }
        return style;
    }

    public static String getHostURL() {
        return GWT.getHostPageBaseURL();
    }

    //Use Permission Management system instead of this method!
    @Deprecated
    public static boolean hasUserMaxRoleID(Integer roleID) {
        return roleID.equals(getUserMaxRoleID());
    }

    public static Integer getUserMaxRoleID() {
        String roles = userSettings.get(ROLES);
        return getMaxRoleID(roles);
    }

    private static Integer getMaxRoleID(String roles) {
        String[] roleArray = roles.split(",");
        java.util.List<Integer> rolle = new ArrayList<>();
        for (String aRoleArray : roleArray) {
            rolle.add(Integer.valueOf(aRoleArray));
        }
        java.util.List<Integer> roleList = role(rolle);
        if (roleList.size() > 0) {
            return roleList.get(0);
        }
        return null;
    }

    public static java.util.List<Integer> role(java.util.List<Integer> roles) {
        Integer[] sortRoles = new Integer[]{ADMIN, DR, HR, ACCOUNTANT, ADMIN_LOCATION, SALESMAN,
                CUSTOMER_SERVICE_REPRESENTATIVE, SALESPERSON, TL, PM, MEM, CALENDAR_EDITOR, CALENDAR_VIEWER, CLIENT, ONE_OFF};
        java.util.List<Integer> userRolesId = new ArrayList<>();
        for (Integer sortRole : sortRoles) {
            if (roles.contains(sortRole)) {
                userRolesId.add(sortRole);
            }
        }
        return userRolesId;
    }

    public static String getShortDateFormat() {
        return userSettings.get(SHORT_DATE_FORMAT) != null ? userSettings.get(SHORT_DATE_FORMAT) : SHORT_DATE_FORMAT_1;
    }

    public static String getLongDateFormat() {
        return userSettings.get(LONG_DATE_FORMAT) != null ? userSettings.get(LONG_DATE_FORMAT) : LONG_DATE_FORMAT_1;
    }

    public static String getDefaultCurrentUserTimeSlotStartTIME() {//default current employee timeSlot start time
        return userSettings.get(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_START_TIME) != null &&
                !"00:00".equals(userSettings.get(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_START_TIME)) ?
                userSettings.get(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_START_TIME) : "09:30";
    }

    public static String getDefaultCurrentUserTimeSlotEndTIME() {//default current employee timeSlot end time
        return userSettings.get(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_END_TIME) != null && !"null".equals(userSettings.get(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_END_TIME)) &&
                !"00:00".equals(userSettings.get(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_END_TIME)) ?
                userSettings.get(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_END_TIME) : "18:00";
    }


    public static Boolean isEnableAccountingModule() {
        return "true".equals(userSettings.get(ACCOUNTING_MODULE));
    }

    public static Boolean isEnableMessageCenter() {
        return "true".equals(userSettings.get(MESSAGE_CENTER_ENABLED));
    }

    public static Boolean isEnableProrataBasedAnnualLeave() {
        return "true".equals(userSettings.get(PRORATA_BASED_ANNUAL_LEAVE));
    }

    public static boolean isLockCompletedProjecItems() {
        return hasGenericAccess(GenericSettingsEnum.LOCK_COMPLETED_PROJECT_ITEMS);
    }

    public static boolean isEmployeeAssignmentEnable() {
        return hasGenericAccess(GenericSettingsEnum.EMPLOYEE_ASSIGNMENT_ENABLE);
    }

    public static Boolean isSetupSubProject() {
        return "true".equals(Utils.userSettings.get(Constants.IS_SETUP_SUPPROJECT));
    }

    public static Boolean isSetupSubProjectTwoLevel() {
        return "true".equals(Utils.userSettings.get(Constants.IS_SETUP_SUPPROJECT_TWO_LEVEL));
    }

    public static boolean isProjectInLineItemEnable() {
        return hasGenericAccess(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);
    }

    public static boolean isInventoryTrackingEnable() {
        return hasGenericAccess(GenericSettingsEnum.INVENTORY_TRACKING_ENABLED);
    }

    public static boolean isBatchSerialEnable() {
        return hasGenericAccess(GenericSettingsEnum.PRODUCT_SERIAL_ENABLED);
    }

    public static Boolean isDoubleMessageEnable() {
        return "true".equals(userSettings.get(DOUBLE_MESSAGE_ENABLE));
    }

    public static Boolean isMultipleSalesPriceEnable() {
        return "true".equals(userSettings.get(MULTIPLE_SALES_PRICE_ENABLED));
    }

    public static Boolean isMonthlyTimeSheetEnable() {
        return "true".equals(userSettings.get(MONTHLY_TIMESHEET));
    }

    public static Boolean isPOCustomImportEnambled() {
        return "true".equals(userSettings.get(PURCHASE_ORDER_CUSTOM_ITEM_RECIEVE_IMPORT));
    }

    /**
     * <h1>... This is method FacetFilterRpc object Convert to Json Data ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {18:22 13/06/2011} ...</h3>
     *
     * @param facetFilter
     * @return
     */
    public static String facetFilterRpcToJsonString(FacetFilterRpc facetFilter) {
        if (facetFilter != null) {
            JSONObject parentJSON = new JSONObject();
            JSONObject innerJSON = new JSONObject();
            JSONObject facetListJSON = new JSONObject();
            JSONObject customListJSON = new JSONObject();
            JSONObject solrMapJSON = new JSONObject();

            parentJSON.put("facetFilter", innerJSON);
            if (facetFilter.getObjectID() != null) {
                innerJSON.put("objectID", new JSONString(facetFilter.getObjectID().toString()));
            }

            innerJSON.put("type", new JSONString(facetFilter.getType().name()));

            innerJSON.put("filterChanges", new JSONString(String.valueOf(facetFilter.isFilterChanges())));

            if (!isNullOrEmpty(facetFilter.getSelectedDateSolrCodeName())) {
                innerJSON.put("selectedDateSolrCode", new JSONString(facetFilter.getSelectedDateSolrCodeName()));
            }

            if (facetFilter.getTypeId() != null) {
                innerJSON.put("typeId", new JSONString(String.valueOf(facetFilter.getTypeId())));
            }
            if (facetFilter.getStartDate() != null) {
                innerJSON.put("startDate", new JSONString(DateUtils.getDateAndTimeFormatFull(facetFilter.getStartDate())));
            }
            if (facetFilter.getEndDate() != null) {
                innerJSON.put("endDate", new JSONString(DateUtils.getDateAndTimeFormatFull(facetFilter.getEndDate())));
            }
            innerJSON.put("filterChanges", new JSONString(String.valueOf(facetFilter.isFilterChanges())));
            innerJSON.put("facetContentList", facetListJSON);
            HashMap<String, FacetContentRpc> items = facetFilter.getFacetContentMap();

            for (String key : items.keySet()) {
                JSONObject itemArrayJSON = new JSONObject();
                facetListJSON.put(key, itemArrayJSON);
                if (items.get(key) != null) {
                    SelectItem[] itemArray = items.get(key).getFacetItems();

                    if (itemArray != null) {
                        for (int j = 0; j < itemArray.length; j++) {
                            JSONObject itemJSON = new JSONObject();
                            if (itemArray[j].getId() != null) {
                                itemJSON.put("id", new JSONString(itemArray[j].getId().toString()));
                            }
                            itemJSON.put("name", new JSONString(itemArray[j].getName() != null ? itemArray[j].getName() : ""));
                            itemJSON.put("description", new JSONString(itemArray[j].getDescription() == null ? "" : itemArray[j].getDescription()));
                            itemArrayJSON.put(String.valueOf(j), itemJSON);
                        }
                    }
                }
            }

            innerJSON.put("facetCustomList", customListJSON);
            HashMap<String, String> customFieldMap = facetFilter.getCustomData();
            for (String key : customFieldMap.keySet()) {
                JSONString customFieldJSON = new JSONString(customFieldMap.get(key));
                customListJSON.put(key, customFieldJSON);
            }
            innerJSON.put("solrFieldList", solrMapJSON);

            HashMap<String, FacetSolrField> solrMap = facetFilter.getShowSolrFieldMap();
            for (String key : solrMap.keySet()) {
                FacetSolrField solrField = solrMap.get(key);
                JSONObject solrFildJSON = new JSONObject();
                solrFildJSON.put("criteriaName", new JSONString(solrField.getSolrFieldCriteriaName()));
                solrFildJSON.put("facetField", new JSONString(solrField.getSolrFieldCriteriaName()));
                solrFildJSON.put("isCondationId", new JSONString(String.valueOf(solrField.isConditionItemId())));
                solrFildJSON.put("isWithID", new JSONString(String.valueOf(solrField.isWithID())));
                solrMapJSON.put(key, solrFildJSON);
            }

            return parentJSON.toString();
        }
        return null;
    }

    /**
     * <h1>... This is method FacetFilterRpc object Convert to Json Data ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {18:22 13/06/2011} ...</h3>
     *
     * @param listPanelTool
     * @return
     */
    public static String listPanelToolRpcConvertJsonData(ListPanelToolRpc listPanelTool) {
        if (listPanelTool != null) {
            JSONObject parentJSON = new JSONObject();
            JSONObject innerJSON = new JSONObject();
            parentJSON.put("listPanelTool", innerJSON);
            if (listPanelTool.getType() != null) {
                innerJSON.put("type", new JSONString(listPanelTool.getType().name()));
            }
            if (listPanelTool.getTypeId() != null) {
                innerJSON.put("typeId", new JSONString(listPanelTool.getTypeId().toString()));
            }
            if (listPanelTool.getColumnCodeName() != null) {
                JSONObject columnCodeJSON = new JSONObject();
                innerJSON.put("columnCode", columnCodeJSON);
                int k = 0;
                for (String columnCode : listPanelTool.getColumnCodeName()) {
                    columnCodeJSON.put(String.valueOf(k++), new JSONString(columnCode));
                }
            }
            if (listPanelTool.getListViewCustomFields() != null) {
                JSONObject customFieldListJSON = new JSONObject();
                innerJSON.put("customFieldList", customFieldListJSON);
                for (int i = 0; i < listPanelTool.getListViewCustomFields().size(); i++) {
                    CompanyCustomFieldItem customField = listPanelTool.getListViewCustomFields().get(i);
                    JSONObject customFieldJSON = new JSONObject();
                    if (customField.getUiType() != null) {
                        customFieldJSON.put("uiType", new JSONString(customField.getUiType()));
                    }
                    if (customField.getDataType() != null) {
                        customFieldJSON.put("dataType", new JSONString(customField.getDataType()));
                    }
                    if (customField.getFieldName() != null) {
                        customFieldJSON.put("fieldName", new JSONString(customField.getFieldName()));
                    }
                    if (customField.getEntityName() != null) {
                        customFieldJSON.put("entityName", new JSONString(customField.getEntityName()));
                    }
                    if (customField.getColumnCode() != null) {
                        customFieldJSON.put("columnCode", new JSONString(customField.getColumnCode()));
                    }
                    customFieldListJSON.put(String.valueOf(i), customFieldJSON);
                }
            }
            return parentJSON.toString();
        }
        return null;
    }

    public static Boolean isLiveEnvironment() {
        return "true".equals(userSettings.get(IS_LIVE_ENVIRONMENT));
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static SelectItem[] getAsSelectItem(String value, String delimitr) {
        if (value != null && !"".equals(value)) {
            String[] values = value.split(delimitr);
            SelectItem[] items = new SelectItem[values.length];
            int i = 0;
            for (String value_ : values) {
                if (value_ != null && !"".equals(value_)) {
                    items[i++] = new SelectItem(null, value_);
                }
            }
            return items;
        }
        return new SelectItem[0];
    }

    public static HorizontalPanel getInHorizontalPanel(int spacing, int firstColumnSize, boolean fullWidth, Widget... widgets) {
        HorizontalPanel hp = new HorizontalPanel();
        hp.addStyleName(DEFAULT_WIDTH);
        hp.setStyleName("lookupIconDelete");
        hp.setSpacing(spacing);
        boolean firstWidget = false;
        if (widgets != null && widgets.length > 0) {
            for (Widget widget : widgets) {
                hp.add(widget);
                hp.setCellVerticalAlignment(widget, HasVerticalAlignment.ALIGN_MIDDLE);
                if (!firstWidget) {
                    if (firstColumnSize > -1) {
                        hp.setCellWidth(widget, firstColumnSize + "px");
                    }
                    if (fullWidth) {
                        hp.setCellWidth(widget, "100%");
                    }
                    firstWidget = true;
                }
            }
        }
        return hp;
    }

    /**
     * Create button method
     *
     * @param image       - icon image
     * @param title       - title
     * @param marginLeft  - marginLeft
     * @param actionImage - icon action image
     * @return button
     */
    public static String getButtonText(String image, String title, int marginLeft, String... actionImage) {
        String string = "&nbsp;";
        string = actionImage != null && actionImage.length > 0 && actionImage[0] != null ? string.concat(actionImage[0]) : string;
        return "<table class='docs-button-table' style='font-size:100%;margin-left:" + marginLeft + "px;'><tr>" +
                "<td  class='left_bdr'>&nbsp;</td>" +
                "<td>&nbsp;</td>" +
                "<td>" + image + "</td>" +
                "<td>&nbsp;" + title + "</td>" +
                "<td>" + string + "</td>" +
                "<td class='right_bdr'>&nbsp;</td></tr></table>";
    }

    /**
     * doesn't work IE9 beta (or IE9 running compatibility mode)-  isIE(), isChrome(), isFireFox(), isOpera(), isSafari()
     *
     * @return browser type
     */

    public static native boolean isIE() /*-{
        return (/MSIE (\d+\.\d+);/.test(navigator.userAgent));
    }-*/;

    public static native boolean isIE7() /*-{
        return (/MSIE 7 (\d+\.\d+);/.test(navigator.userAgent));
    }-*/;

    public static native boolean isChrome() /*-{
        return (/Chrome\/(\d+(\.\d+)+)/.test(navigator.userAgent));
    }-*/;

    public static native boolean isFireFox() /*-{
        return (/Firefox\/(\d+(.\d+)?)/.test(navigator.userAgent));
    }-*/;

    public static native boolean isOpera() /*-{
        return (/Opera\/? ?(\d+(\.\d+)?)/.test(navigator.userAgent));
    }-*/;

    public static native boolean isSafari() /*-{
        return (/Version\/(\d+(\.\d+)+)/.test(navigator.userAgent));
    }-*/;

    public static boolean isMac() {
        String userAgent = getUserAgent();
        return userAgent.contains("macintosh") || userAgent.contains("mac os x");
    }

    public static boolean isLocalhostOrLochin(String email) {
        return "aknbdev@gmail.com".equals(Utils.getParam(USER_NAME)) || email.equals(Utils.getParam(USER_NAME)) || Utils.isLocalhost();
    }

    public static boolean enableSalesBackend() {
        String sales = Utils.userSettings.get(ENABLE_SALES_BACKEND_FOR_USER);
        return "true".equals(sales);
    }

    public static boolean enableSupportBackend() {
        String support = Utils.userSettings.get(ENABLE_SUPPORT_BACKEND_FOR_USER);
        return "true".equals(support);
    }

    public static boolean enableAdminBackend() {
        String system = Utils.userSettings.get(ENABLE_ADMIN_BACKEND_FOR_USER);
        return "true".equals(system);
    }

    public static boolean enablePartnerAdminBackend() {
        return "true".equals(Utils.userSettings.get(ENABLE_PARTNER_ADMIN_BACKEND_FOR_USER));
    }

    public static boolean enableDeveloperBackend() {
        String system = Utils.userSettings.get(ENABLE_DEVELOPER_BACKEND_FOR_USER);
        return "true".equals(system);
    }

    /**
     * <h1>... THIS IS METHOD PARSE IN READ WFM MODULE CUSTOMISE XML DOC ...</h1>
     * <br/>
     * <h2>... WRITER BY DEVELOPER {DILSHOD.T} ...</h2>
     * <br/>
     * <h3>... CREATE DATE {20:28 20/06/2011} ....</h3>
     * <br/>
     *
     * @param moduleName
     * @return
     */
    private static WfmModuleSetting getXmlParam(String moduleName) {
        LinkedHashMap<String, WfmModuleSetting> moduleMap = new LinkedHashMap<>();

        com.google.gwt.dom.client.Element wfmMudoleSettings = Document.get().getElementById(WfmModuleSettingConstants.MUDULESETTING.getName());
        if (wfmMudoleSettings != null) {
            com.google.gwt.xml.client.Document doc = XMLParser.parse(wfmMudoleSettings.getInnerHTML());

            com.google.gwt.xml.client.NodeList modules = doc.getElementsByTagName(WfmModuleSettingConstants.MODULE.getName());
            if (modules == null || modules.getLength() == 0) {
                modules = doc.getElementsByTagName(WfmModuleSettingConstants.MODULE.getName().toUpperCase());
            }

            if (modules != null && modules.getLength() != 0) {
                for (int i = 0; i < modules.getLength(); i++) {
                    parseWfmCustomiseParams(moduleMap, (com.google.gwt.xml.client.Element) modules.item(i));
                }
            }
        }
        if (moduleMap.containsKey(moduleName)) {
            WfmModuleSetting moduleSetting = moduleMap.get(moduleName);
            moduleSetting.setCustomise(true);
            return moduleSetting;
        }
        return new WfmModuleSetting();
    }

    /**
     * <h1>... THIS IS METHOD PARSE IN READ WFM MODULE CUSTOMISE XML DOC ...</h1>
     * <br/>
     * <h2>... WRITER BY DEVELOPER {DILSHOD.T} ...</h2>
     * <br/>
     * <h3>... CREATE DATE {21:27 10/05/2011} ....</h3>
     * <br/>
     *
     * @param moduleMap
     * @param moduleElem
     */
    private static void parseWfmCustomiseParams(Map<String, WfmModuleSetting> moduleMap, com.google.gwt.xml.client.Element moduleElem) {
        WfmModuleSetting moduleSetting = new WfmModuleSetting();
        moduleSetting.setModuleName(moduleElem.getAttribute(WfmModuleSettingConstants.MODULENAME.getName()) != null ? moduleElem.getAttribute(WfmModuleSettingConstants.MODULENAME.getName()) : GWT.getModuleName());
        moduleSetting.setRootId(moduleElem.getAttribute(WfmModuleSettingConstants.ROOTDIVID.getName()));
        moduleSetting.setWidth(moduleElem.getAttribute(WfmModuleSettingConstants.WIDTH.getName()));
        moduleSetting.setHeight(moduleElem.getAttribute(WfmModuleSettingConstants.HEIGHT.getName()));
        moduleSetting.setShowHeader(moduleElem.getAttribute(WfmModuleSettingConstants.SHOWHEADER.getName()));
        moduleSetting.setWftListLimit(moduleElem.getAttribute(WfmModuleSettingConstants.LISTPANEL_LIMIT.getName()));
        moduleSetting.setWftListPanelShowPaging(moduleElem.getAttribute(WfmModuleSettingConstants.LISTPANEL_SHOW_PAGING.getName()));
        moduleSetting.setShowAllContainer(moduleElem.getAttribute(WfmModuleSettingConstants.SHOWALLCONTAINER.getName()));
        moduleSetting.setModuleStyle(moduleElem.getAttribute(WfmModuleSettingConstants.STYLE.getName()));
        moduleSetting.setEnableWFTListing(moduleElem.getAttribute(WfmModuleSettingConstants.ENABLE_WFT_LISTING.getName()));
        if (moduleElem.getAttribute(WfmModuleSettingConstants.SHOWBUTTONS.getName()) != null && !"".equals(moduleElem.getAttribute(WfmModuleSettingConstants.SHOWBUTTONS.getName()))) {
            moduleSetting.setShowButtons(moduleElem.getAttribute(WfmModuleSettingConstants.SHOWBUTTONS.getName()));
        }

        if (moduleElem.getAttribute(WfmModuleSettingConstants.SHOWSTEPS.getName()) != null && !"".equals(moduleElem.getAttribute(WfmModuleSettingConstants.SHOWSTEPS.getName()))) {
            moduleSetting.setShowSteps(moduleElem.getAttribute(WfmModuleSettingConstants.SHOWSTEPS.getName()));
        }

        if (moduleElem.getAttribute(WfmModuleSettingConstants.CUSTOMDASHBOARDID.getName()) != null && !"".equals(moduleElem.getAttribute(WfmModuleSettingConstants.CUSTOMDASHBOARDID.getName()))) {
            moduleSetting.setCustomDashboardId(moduleElem.getAttribute(WfmModuleSettingConstants.CUSTOMDASHBOARDID.getName()));
        }

        if (moduleElem.getAttribute(WfmModuleSettingConstants.ACTIVESTEPS.getName()) != null && !"".equals(moduleElem.getAttribute(WfmModuleSettingConstants.ACTIVESTEPS.getName()))) {
            moduleSetting.setActiveSteps(moduleElem.getAttribute(WfmModuleSettingConstants.ACTIVESTEPS.getName()));
        }
        if (moduleElem.getAttribute(WfmModuleSettingConstants.FIRSTSTEP.getName()) != null && !"".equals(moduleElem.getAttribute(WfmModuleSettingConstants.FIRSTSTEP.getName()))) {
            moduleSetting.setFirstStep(moduleElem.getAttribute(WfmModuleSettingConstants.FIRSTSTEP.getName()));
        }
        if (moduleElem.getAttribute(WfmModuleSettingConstants.ACTIVEPAGERS.getName()) != null && !"".equals(moduleElem.getAttribute(WfmModuleSettingConstants.ACTIVEPAGERS.getName()))) {
            moduleSetting.setActivePagers(moduleElem.getAttribute(WfmModuleSettingConstants.ACTIVEPAGERS.getName()));
        }


        // read containers settings
        if (!moduleSetting.isShowAllContainer() && moduleElem.getChildNodes() != null && moduleElem.getChildNodes().getLength() != 0) {
            com.google.gwt.xml.client.NodeList containers = moduleElem.getElementsByTagName(WfmModuleSettingConstants.CONTAINERS.getName());
            if (containers == null || containers.getLength() == 0) {
                containers = moduleElem.getElementsByTagName(WfmModuleSettingConstants.CONTAINERS.getName().toUpperCase());
            }

            com.google.gwt.xml.client.Element containerParent = (com.google.gwt.xml.client.Element) containers.item(0);
            if (containerParent != null) {
                com.google.gwt.xml.client.NodeList containerChaild = containerParent.getElementsByTagName(WfmModuleSettingConstants.CONTAINER.getName());
                if (containerChaild == null || containerChaild.getLength() == 0) {
                    containerChaild = containerParent.getElementsByTagName(WfmModuleSettingConstants.CONTAINER.getName().toUpperCase());
                }

                for (int j = 0; j < containerChaild.getLength(); j++) {
                    parseWfmContainerParams(moduleSetting, (com.google.gwt.xml.client.Element) containerChaild.item(j));
                }
            }
        }


        //read module params
        com.google.gwt.xml.client.NodeList params = moduleElem.getElementsByTagName(WfmModuleSettingConstants.PARAMS.getName());
        if (params == null || params.getLength() == 0) {
            params = moduleElem.getElementsByTagName(WfmModuleSettingConstants.PARAMS.getName().toUpperCase());
        }

        com.google.gwt.xml.client.Element paramParanet = (com.google.gwt.xml.client.Element) params.item(0);
        if (paramParanet != null) {
            com.google.gwt.xml.client.NodeList paramChaild = paramParanet.getElementsByTagName(WfmModuleSettingConstants.PARAM.getName());
            if (paramChaild == null || paramChaild.getLength() == 0) {
                paramChaild = paramParanet.getElementsByTagName(WfmModuleSettingConstants.PARAM.getName());
            }

            if (paramChaild != null) {
                LinkedHashMap<String, String> moduleParam = new LinkedHashMap<>();
                for (int j = 0; j < paramChaild.getLength(); j++) {
                    String paramName = ((com.google.gwt.xml.client.Element) paramChaild.item(j)).getAttribute("name");
                    String paramValue = ((com.google.gwt.xml.client.Element) paramChaild.item(j)).getAttribute("value");
                    moduleParam.put(paramName, paramValue);
                }
                moduleSetting.setParams(moduleParam);
            }
        }
        moduleMap.put(moduleSetting.getModuleName(), moduleSetting);
    }

    /**
     * <h1>... THIS IS METHOD PARSE IN READ WFM CONTAINER&SECTION CUSTOMISE XML DOC ...</h1>
     * <br/>
     * <h2>... WRITER BY DEVELOPER {DILSHOD.T} ...</h2>
     * <br/>
     * <h3>... CREATE DATE {22:34 10/05/2011} ....</h3>
     * <br/>
     *
     * @param moduleSetting
     * @param containerElem
     */
    private static void parseWfmContainerParams(WfmModuleSetting moduleSetting, com.google.gwt.xml.client.Element containerElem) {
        WfmContainer wfmContainer = new WfmContainer();
        wfmContainer.setHistoryName(containerElem.getAttribute(WfmModuleSettingConstants.HISTORYNAME.getName()));
        wfmContainer.setShowLeftMenu(containerElem.getAttribute(WfmModuleSettingConstants.SHOWLEFTMENU.getName()));
        String sections = containerElem.getAttribute(WfmModuleSettingConstants.SECTIONS.getName());
        if (sections == null || "all".equals(sections)) {
            wfmContainer.setShowAllView(true);
        } else {
            wfmContainer.setShowAllView(false);
            String[] sectionNames = sections.split("[,]");
            for (String sectionName : sectionNames) {
                wfmContainer.getSectionHistoryName().add(sectionName);
            }
        }
        moduleSetting.getContainers().put(wfmContainer.getHistoryName(), wfmContainer);
    }

    /**
     * This method used for refactoring custom fields values in summary view
     *
     * @param obj
     * @return
     */
    public static String refactor(Object obj) {
        return refactor(obj, true);
    }

    public static String refactor(Object obj, boolean isBold) {
        String s = null;
        if (obj != null) {
            if (obj instanceof Date) {
                s = DateUtils.format((Date) obj);
            } else {
                s = obj.toString();
            }
        }
        if (s != null && !"".equals(s)) {
            if (isBold) {
                return "<b>" + s + "</b>";
            } else {
                return s;
            }
        }
        return "";
    }

    public static String refactorDateTime(Object obj, boolean isBold) {
        String s = null;
        if (obj != null) {
            if (obj instanceof Date) {
                s = DateUtils.formatInternal((Date) obj);
            } else {
                s = obj.toString();
            }
        }
        if (s != null && !"".equals(s)) {
            if (isBold) {
                return "<b>" + s + "</b>";
            } else {
                return s;
            }
        }
        return "";
    }

    public static String getFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return getSize(size, 1024D) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return getSize(size, (1024D * 1024D)) + " MB";
        }
        return getSize(size, (1024D * 1024D * 1024D)) + " GB";
    }

    private static String getSize(Long size, Double division) {
        Double res = Double.valueOf(size.toString()) / division;
        NumberFormat nf = NumberFormat.getFormat("######.#");
        return nf.format(res);
    }

    public static boolean marketplaceShowMenu(String section) {
        String fromMarketplace = Cookies.getCookie(FROM_MARKETPLACE);
        if (fromMarketplace == null || "".equals(fromMarketplace) || !Boolean.valueOf(fromMarketplace)) {
            return true;
        }
        return Cookies.getCookie(SECTION_HTML).contains(section + ".html");
    }

    public static native void setContentToIFrame(String elementID, String s_) /*-{
        var iframe = $doc.getElementById(elementID);
        var doc;
        if (!iframe) {
            return;
        }
        if (iframe.contentWindow) {
            doc = iframe.contentWindow;
        } else if (iframe.contentDocument && iframe.contentDocument.document) {
            doc = iframe.contentWindow.document;
        } else {
            doc = iframe.contentDocument;
        }
        if (doc.documentElement) {
            doc.documentElement.innerHTML = "";
            doc.documentElement.write(s_);
        }
        if (doc.document) {
            doc.document.innerHTML = "";
            doc.document.write(s_);
        }
        try {
            minimumHeight = 300;
            if (doc.body.offsetHeight) {
                minimumHeight = doc.body.offsetHeight + 20;
            }
            iframe.height = minimumHeight;
            iframe.scrolling = 'no';
        } catch (err) {
            // do nothing
            iframe.scrolling = 'yes';
        }
    }-*/;

    public static boolean isPM() {
        return "pm".equals(GWT.getModuleName());
    }

    public static boolean isCRM() {
        return "crm".equals(GWT.getModuleName());
    }

    public static boolean isReporting() {
        return "reportingsystem".equals(GWT.getModuleName());
    }

    public static boolean isWebForm() {
        return "webforms".equals(GWT.getModuleName());
    }

    public static boolean isAccounting() {
        return "accounting".equals(GWT.getModuleName());
    }

    public static boolean isSettings() {
        return "settings".equals(GWT.getModuleName());
    }

    public static void setSettings(PermissionSettings settings) {
        setUserPermissions(settings.getPermissions());
        setUserGenericSettings(settings.getGenericSettings());
        properties = settings.getPropertyItemMap();
        enabledModules = settings.getEnabledModules();
        twilioNumbers = settings.getTwilioNumbers();
        asteriskSettings = settings.getAsteriskSettings();
        if (settings.getUserID() != null) {
            userSettings.put(USER_ID, settings.getUserID());
        }
        if (settings.getUserLocation() != null) {
            setUserlocationAsSelectItem(settings.getUserLocation());
        }
        if (settings.getUserDepartmentID() != null) {
            setUserDepartment(settings.getUserDepartmentID());
        }
        if (settings.getUserDepartmentAsSelectItem() != null) {
            setUserDepartmentAsSelectItem(settings.getUserDepartmentAsSelectItem());
        }
        if (settings.getRoles() != null) {
            userSettings.put(ROLES, settings.getRoles());
            userSettings.put(ROLE_CODES, settings.getRolesCodes());
        }
        propertyListingMap = settings.getPropertyListingsMap();
        moduleLocalizeMap = settings.getModuleLocalizeMap();
        userLanguage = settings.getUserLanguage();
    }


    public static List<SelectItem> getTwilioNumbers() {
        return twilioNumbers;
    }

    public static List<AsteriskSettings> getAsteriskSettings() {
        return asteriskSettings;
    }

    public static boolean isTrainingCenter() {
        return "trainingcenter".equals(GWT.getModuleName());
    }

    public static boolean isDocuments() {
        return "documents".equals(GWT.getModuleName());
    }

    public static boolean isHRMS() {
        return "hrms".equals(GWT.getModuleName());
    }

    public static boolean isBACKEND() {
        return "backend".equals(GWT.getModuleName());
    }

    public static boolean isLogistics() {
        return "logistics".equals(GWT.getModuleName());
    }

    public static boolean isMC() {
        return "messagecenter".equals(GWT.getModuleName());
    }

    @SafeVarargs
    public static <T extends Serializable> ArrayList<T> asArrayList(T... t) {
        return new ArrayList<>(Arrays.asList(t));
    }

    public static String getTimeZone() {
        Date date = new Date();
        Integer minutes = date.getTimezoneOffset();
        String gmt = "GMT-";
        String timeZone = gmt + Utils.formatMinutes(minutes);
        if (minutes < 0) {
            gmt = "GMT+";
            minutes = (-1) * minutes;
            timeZone = gmt + Utils.formatMinutes(minutes);
        } else if (minutes == 0) {
            timeZone = "GMT";
        }
        return timeZone;
    }

    /**
     * Returns the viewports size.
     *
     * @return the size
     */
    public static native Size getViewportSize() /*-{
        var vw;
        var vh;
        if (typeof $wnd.innerWidth != 'undefined') {
            vw = $wnd.innerWidth;
            vh = $wnd.innerHeight;
        } else if (typeof $doc.documentElement != 'undefined'
            && typeof $doc.documentElement.clientWidth !=
            'undefined' && $doc.documentElement.clientWidth != 0) {
            vw = $doc.documentElement.clientWidth;
            vh = $doc.documentElement.clientHeight;
        } else {
            vw = $doc.getElementsByTagName('body')[0].clientWidth;
            vh = $doc.getElementsByTagName('body')[0].clientHeight;
        }
        var size = @com.edatasite.workforce.gwt.core.client.ui.Size::newInstance(II)(vw, vh);
        return size;

    }-*/;

    /**
     * Returns the element's bounds.
     *
     * @param elem the element
     * @return the elements bounds
     */
    public static Rectangle getBounds(Element elem) {
        int x = DOM.getAbsoluteLeft(elem);
        int y = DOM.getAbsoluteTop(elem);
        int width = getWidth(elem);
        int height = getHeight(elem);

        width = Math.max(0, width);
        height = Math.max(0, height);
        return new Rectangle(x, y, width, height);
    }

    /**
     * Returns the element's offset width in pixels. This is the total width of
     * the element, including decorations such as border, margin, and padding.
     *
     * @param elem the element
     * @return the element's offset width
     */
    public static int getWidth(Element elem) {
        return DOM.getElementPropertyInt(elem, "offsetWidth");
    }

    /**
     * Returns the element's offset height in pixels. This is the total height of
     * the object, including decorations such as border, margin, and padding.
     *
     * @param elem the element
     * @return the element's offset height
     */
    public static int getHeight(Element elem) {
        return DOM.getElementPropertyInt(elem, "offsetHeight");
    }

    /**
     * Sets the elements size and location to the specified rectangle.
     *
     * @param elem the element
     * @param rect the rectangle
     */
    public static void setBounds(Element elem, Rectangle rect) {
        setLocation(elem, rect.x, rect.y);
        setSize(elem, rect.width, rect.height);
    }

    /**
     * Sets the element position using page coordinates.
     *
     * @param elem the element
     * @param x    the x coordinate value
     * @param y    the y coordinate value
     */
    public static void setLocation(Element elem, int x, int y) {
        setX(elem, x);
        setY(elem, y);
    }

    /**
     * Set the x position of the element.
     *
     * @param elem the element
     * @param x    the x coordinate
     */
    public static void setX(Element elem, int x) {
        makePositionable(elem);
        int l = DOM.getIntStyleAttribute(elem, "left");
        x = x - DOM.getAbsoluteLeft(elem) + l;
        DOM.setStyleAttribute(elem, "left", x + "px");
    }

    /**
     * Makes an element positionable.
     *
     * @param elem the element
     */
    public static void makePositionable(Element elem) {
        String position = DOM.getStyleAttribute(elem, "position");
        if (position.equals("") || position.equals("static")) {
            DOM.setStyleAttribute(elem, "position", "relative");
        }
    }

    /**
     * Set the y position of an html element in page coordinates, regardless of
     * how the element is positioned. The element must be part of the DOM tree to
     * have page coordinates.
     *
     * @param elem the element
     * @param y    the y coordinate
     */
    public static void setY(Element elem, int y) {
        makePositionable(elem);
        int t = DOM.getIntStyleAttribute(elem, "top");
        y = y - DOM.getAbsoluteTop(elem) + t;
        DOM.setStyleAttribute(elem, "top", y + "px");
    }

    /**
     * Set the size of the element. Values equal to My.DEFAULT are ignored.
     *
     * @param elem   the element
     * @param width  the new width
     * @param height the new height
     */
    public static void setSize(Element elem, int width, int height) {
        DOM.setStyleAttribute(elem, "height", height + "px");
        DOM.setStyleAttribute(elem, "width", width + "px");
    }

    /**
     * Sets the focus state of the element.
     *
     * @param focused the new focus state
     */
    public static native void setFocus(Element elem, boolean focused) /*-{
        try {
            if (focused)
                elem.focus();
            else
                elem.blur();
        } catch (err) {
        }
    }-*/;

    public static boolean isNumberKey(int code) {
        return code > 47 && code < 58 || code > 95 && code < 106;
    }

    public static String getProductName() {
        return userSettings.get(PRODUCT_NAME);
    }

    public static String getUploadDir() {
        return userSettings.get(UPLOAD_DIR);
    }

    public static String getUploadType() {
        return userSettings.get(UPLOAD_TYPE);
    }

    public static FileUploadType getFileUploadType() {
        switch (getUploadType()) {
            case Constants.GOOGLE:
                return FileUploadType.GOOGLE_DOCUMENTS;
            case Constants.OFFICE_365:
                return FileUploadType.OFFICE_DOCUMENTS;
            case Constants.UPLOAD_SHARE_POINT:
                return FileUploadType.OFFICE_SHARE_POINT_DOCUMENTS;
            case Constants.LOCAL:
                return FileUploadType.LOCAL;
            case Constants.MINIO:
                return FileUploadType.MINIO;
            case Constants.AMAZON:
            default:
                return FileUploadType.AMAZON;
        }
    }

    public static String getUploadTypeParam() {
        return userSettings.get(CommandConstants.UPLOAD_TYPE_PARAM_NAME);
    }

    public static String getHostName() {
        return userSettings.get(HOST_NAME_VALUE);
    }

    public static Double getVAT_RATE() {
        return userSettings.get(VAT_RATE_VALUE) != null && !"".equals(userSettings.get(VAT_RATE_VALUE)) && !"null".equals(userSettings.get(VAT_RATE_VALUE)) ? Double.valueOf(userSettings.get(VAT_RATE_VALUE)) : null;
    }

    public static String getHelpHost() {
        return userSettings.get(HELP_HOST);
    }

    public static String getSupportEmail() {
        return userSettings.get(SUPPORT_EMAIL);
    }

    public static String getPhone() {
        return userSettings.get(PHONE);
    }

    /**
     * Return HTML code for currency
     *
     * @param currencyCODE - currency code
     * @return - html code
     */
    public static String getHTMLCODESForCurrency(String currencyCODE) {
        if ("USD".equals(currencyCODE)) {//currency code -- Dollar
            return "$";//symbol -- Dollar -- $
        } else if ("GBP".equals(currencyCODE)) {//currency code -- Pound Sterling
            return "&pound;";//symbol -- Pound Sterling -- £
        } else if ("EUR".equals(currencyCODE)) {//currency code -- Euro
            return "&euro;";//symbol -- Euro -- €
        } else if ("INR".equals(currencyCODE)) {//currency code -- Indian rupee
            return "₹";//symbol -- Indian rupee -- ₹
        }

        return "$";//default currency symbol -- Dollar -- $
    }

    public static String getCurrencyCODEbyHOST() {
        return userSettings.get(DEFAULT_CURRENCY_CODE) != null && !"".equals(userSettings.get(DEFAULT_CURRENCY_CODE)) && !"null".equals(userSettings.get(DEFAULT_CURRENCY_CODE)) ? userSettings.get(DEFAULT_CURRENCY_CODE) : "USD";
    }

    /**
     * Sets the focus state of the element.
     * <p/>
     * focused the new focus state
     */
    public static native void addStyleTag(String css) /*-{
        var style = $doc.createElement('style');
        style.type = 'text/css';
        if (style.styleSheet) {
            style.styleSheet.cssText = css;
        } else {
            style.appendChild($doc.createTextNode(css));
        }
        var head = $doc.head || $doc.getElementsByTagName('head')[0];
        if (head) {
            head.appendChild(style);
        }
    }-*/;

    /**
     * Sets the focus state of the element.
     * <p/>
     * focused the new focus state
     */
    public static native void reloadPage() /*-{
        $wnd.location.reload();// = $wnd.location.href;
    }-*/;

    public static void setUserPermissions(HashSet<String> userPerms) {
        userPermissions = userPerms;
    }

    public static boolean hasPermission(String code) {
        if (userPermissions != null) {
            return userPermissions.contains(code);
        }

        return hasRole(Constants.ADMIN);
    }

    public static boolean hasModuleEnabled(String code) {
        return enabledModules.contains(code);
    }

    public static PropertyItem getProperTy(String code) {
        if (properties != null) {
            return properties.get(code);
        }
        return null;
    }

    public static void setUserGenericSettings(HashSet<GenericSettingsEnum> genericSettings) {
        userGenericSettings = genericSettings;
    }

    public static boolean hasTwilioAccess(GenericSettingsEnum key) {
        return true;
    }

    public static boolean hasGenericAccess(GenericSettingsEnum key) {
        if (GenericSettingsEnum.MULTICURRENCY_ENABLED.equals(key)) {
            return true; //We'll remove this option later
        }

        if (userGenericSettings != null) {
            return userGenericSettings.contains(key);
        }
        return false;
    }

    public static BigDecimal parseToBigDecimal(String text) {
        if (text != null && text.length() > 0) {
            String currencyCode = Utils.getParam(Utils.BASE_CURRENCY);
            if (currencyCode != null && text.startsWith(currencyCode)) {
                return BigDecimal.valueOf(numberFormat.parse(text.replace(currencyCode, "")));
            }
            return BigDecimal.valueOf(numberFormat.parse(text));
        }
        return BigDecimal.ZERO;
    }

    public static double universalParse(NumberFormat nFormat, String nText) {
        if (!LocaleInfo.getCurrentLocale().getLocaleName().equals("es") &&
                !LocaleInfo.getCurrentLocale().getLocaleName().equals("de") &&
                !LocaleInfo.getCurrentLocale().getLocaleName().equals("it")) {
            if (!LocaleInfo.getCurrentLocale().getLocaleName().equals("ru") && !LocaleInfo.getCurrentLocale().getLocaleName().equals("uz")) {
                nText = nText.replace(",", LocaleInfo.getCurrentLocale().getNumberConstants().groupingSeparator());
            }
            nText = nText.replace(".", LocaleInfo.getCurrentLocale().getNumberConstants().decimalSeparator());
        }
        return nFormat.parse(nText);
    }

    public static String getCurrentLocale() {
        return LocaleInfo.getCurrentLocale().getLocaleName();
    }

    public static boolean hasPermission(String... code) {
        if (code != null) {
            Boolean hasPermission = false;
            if (userPermissions != null) {
                for (String permission : code) {
                    hasPermission = userPermissions.contains(permission);
                    if (hasPermission) {
                        return hasPermission;
                    }
                }
                return hasPermission;
            }
        } else {
            return hasRole(Constants.ADMIN);
        }
        return false;
    }

    public static boolean isAccountingSetup() {
        return "true".equals(userSettings.get(ACCOUNTING_IS_SETUP));
    }

    public static boolean isMultiWarehouseEnabled() {
        return "true".equals(userSettings.get(MULTIWAREHOUSE_ENABLED));
    }

    public static boolean isMultiCompanySubsidiary() {
        return "true".equals(userSettings.get(MULTI_COMPANY_SUBSIDIARY));
    }

    public static boolean isProductionEnabled() {
        return "true".equals(userSettings.get(PRODUCTION_ENABLED));
    }

    public static boolean isPOIgnoreManagerApproval() {
        return "true".equals(userSettings.get(PO_IGNORE_MANAGER_APPROVAL));
    }

    public static boolean isPayrollTransactionsDisabled() {
        return "true".equals(userSettings.get(DISABLE_PAYROLL_TRANSACTIONS));
    }

    public static boolean isHrmsDocumentEnabled() {
        return "true".equals(userSettings.get(HRMS_DOCUMENTS));
    }

    public static String getTawToSiteId() {
        return !"null".equals(userSettings.get(TAWK_TO_SITE_ID)) ? userSettings.get(TAWK_TO_SITE_ID) : null;
    }

    public static boolean isAccountingSettingsTabEnabled() {
        return "true".equals(userSettings.get(SETTINGS_ACCOUNTING_SETTINGS));
    }

    public static Integer getAccountingCalculationScale() {
        if (userSettings.get(ACCOUNTING_CALCULATION_SCALE) != null && !"null".equals(userSettings.get(ACCOUNTING_CALCULATION_SCALE).trim())) {
            return Integer.parseInt(userSettings.get(ACCOUNTING_CALCULATION_SCALE));
        }
        return null;
    }

    public static Integer getAccountingTaxRateScale() {
        if (userSettings.get(ACCOUNTING_TAX_RATE_SCALE) != null && !"null".equals(userSettings.get(ACCOUNTING_TAX_RATE_SCALE).trim())) {
            return Integer.parseInt(userSettings.get(ACCOUNTING_TAX_RATE_SCALE));
        }
        return null;
    }

    public static String escapeHtml(String value) {
        if (value == null) {
            return "";
        } else {
            return value
                    .replace("\u001F", "")
                    .replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;");
        }
    }

    public static Integer getAccountingCustomQtyScale() {
        if (userSettings.get(ACCOUNTING_CUSTOM_QUANTITY_SCALE) != null && !"null".equals(userSettings.get(ACCOUNTING_CUSTOM_QUANTITY_SCALE).trim())) {
            return Integer.parseInt(userSettings.get(ACCOUNTING_CUSTOM_QUANTITY_SCALE));
        }
        return null;
    }

    public static Integer getAccountingCustomPriceScale() {
        if (userSettings.get(ACCOUNTING_CUSTOM_PRICE_SCALE) != null && !"null".equals(userSettings.get(ACCOUNTING_CUSTOM_PRICE_SCALE).trim())) {
            return Integer.parseInt(userSettings.get(ACCOUNTING_CUSTOM_PRICE_SCALE));
        }
        return null;
    }

    public static Integer getAccountingCustomExRateScale() {
        if (userSettings.get(ACCOUNTING_CUSTOM_EXRATE_SCALE) != null && !"null".equals(userSettings.get(ACCOUNTING_CUSTOM_EXRATE_SCALE).trim())) {
            return Integer.parseInt(userSettings.get(ACCOUNTING_CUSTOM_EXRATE_SCALE));
        }
        return null;
    }

    public static Integer getAccountingDiscountScale() {
        if (userSettings.get(ACCOUNTING_DISCOUNT_SCALE) != null && !"null".equals(userSettings.get(ACCOUNTING_DISCOUNT_SCALE).trim())) {
            return Integer.parseInt(userSettings.get(ACCOUNTING_DISCOUNT_SCALE));
        }
        return 2;
    }

    // progress invoicing...
    public static Integer getAccountingProgressInvoiceingAmountScale() {
        if (userSettings.get(ACCOUNTING_PROGRESS_INVOICING_AMOUNT_SCALE) != null && !"null".equals(userSettings.get(ACCOUNTING_PROGRESS_INVOICING_AMOUNT_SCALE).trim())) {
            return Integer.parseInt(userSettings.get(ACCOUNTING_PROGRESS_INVOICING_AMOUNT_SCALE));
        }
        return null;
    }

    public static String getTransactionLockDate() {
        if (userSettings.get(TRANSACTION_LOCKING_DATE) != null && !"null".equals(userSettings.get(TRANSACTION_LOCKING_DATE).trim())) {
            return userSettings.get(TRANSACTION_LOCKING_DATE);
        }
        return null;
    }

    public static void setTransactionLockDate(String lockDate) {
        userSettings.put(TRANSACTION_LOCKING_DATE, lockDate);
    }

    public static boolean isSalesLocked() {
        return "true".equals(userSettings.get(TRANSACTION_LOCKING_SALES));
    }
    public static boolean isPurchasesLocked() {
        return "true".equals(userSettings.get(TRANSACTION_LOCKING_PURCHASES));
    }
    public static boolean isBankingLocked() {
        return "true".equals(userSettings.get(TRANSACTION_LOCKING_BANKING));
    }
    public static boolean isInventoryLocked() {//todo
        return "true".equals(userSettings.get(TRANSACTION_LOCKING_BANKING));
    }
    public static boolean isExpensesLocked() {//todo
        return "true".equals(userSettings.get(TRANSACTION_LOCKING_BANKING));
    }
    public static boolean isEmployeesLocked() {
        return "true".equals(userSettings.get(TRANSACTION_LOCKING_EMPLOYEES));
    }
    public static boolean isAttendanceLocked() {
        return "true".equals(userSettings.get(TRANSACTION_LOCKING_ATTENDANCE));
    }
    public static boolean isRecruitmentLocked() {
        return "true".equals(userSettings.get(TRANSACTION_LOCKING_RECRUITMENT));
    }
    public static boolean isPayslipsLocked() {
        return "true".equals(userSettings.get(TRANSACTION_LOCKING_PAYSLIPS));
    }
    public static boolean isCashAdvancesLocked() {
        return "true".equals(userSettings.get(TRANSACTION_LOCKING_CASHADVANCES));
    }
    public static boolean isAdditionalPaymentsLocked() {
        return "true".equals(userSettings.get(TRANSACTION_LOCKING_ADDITIONALPAYMENTS));
    }
    public static boolean isEndOfServiceLocked() {//todo
        return "true".equals(userSettings.get(TRANSACTION_LOCKING_ADDITIONALPAYMENTS));
    }

    public static void setConfirmationEnable(Boolean enable) {
        userSettings.put(DOUBLE_MESSAGE_ENABLE, enable != null ? enable + "" : "false");
    }

    public static void setMultiplePriceEnable(Boolean enable) {
        userSettings.put(MULTIPLE_SALES_PRICE_ENABLED, enable != null ? enable + "" : "false");
    }

    public static void setPayrollTransactionDisabled(Boolean enable) {
        userSettings.put(DISABLE_PAYROLL_TRANSACTIONS, enable != null ? enable + "" : "false");
    }

    public static NumberFormat getCalculationNumberFormat() {

        Integer calculationScale = getAccountingCalculationScale();
        if (calculationScale == null) {
            calculationScale = 2;
        }
        if (calculationScale == 0) {
            return NumberFormat.getFormat(",##0");
        } else {
            String s = ".";
            for (int i = 0; i < calculationScale; i++) {
                s = s.concat("0");
            }
            return NumberFormat.getFormat(",##0" + s);
        }
    }

    public static NumberFormat getCalculationNumberFormatWithCustomScale(Integer calculationScale) {
        if (calculationScale == 0) {
            return NumberFormat.getFormat(",##0");
        } else {
            String s = ".";
            for (int i = 0; i < calculationScale; i++) {
                s = s.concat("0");
            }
            return NumberFormat.getFormat(",##0" + s);
        }
    }

    public static String getCustomTaxName() {
        return userSettings.get(CUSTOM_TAX_NAME);
    }

    public static boolean isSupplier() {
        return "true".equals(userSettings.get(IS_SUPPLIER));
    }

    public static boolean isClientContact() {
        return "true".equals(userSettings.get(IS_CLIENT_CONTACT));
    }

    public static Integer getWebFormID() {
        return Utils.userSettings != null && Utils.userSettings.get(Constants.WEBFORM_ID) != null && Utils.userSettings.get(Constants.WEBFORM_ID).matches(Constants.REGEX_INTEGER) ? Integer.valueOf(Utils.userSettings.get(Constants.WEBFORM_ID)) : null;
    }

    public static void setWebFormID(Integer webFormID) {
        if (webFormID == null) {
            return;
        }
        if (Utils.userSettings == null) {
            userSettings = new LinkedHashMap<>();
        }
        userSettings.put(WEBFORM_ID, webFormID.toString());
    }

    public static String getAntibot() {
        return Utils.userSettings.get("antibot");
    }

    public static void setAntibot(String text) {
        Utils.userSettings.put("antibot", text + "|" + Cookies.getCookie("JSESSIONID"));
    }

    /**
     * Bu method hozircha faqat webformalar uchun ishlaydi.
     * captcha kiritilganligini tekshiradi.
     *
     * @return
     */
    public static int validateCaptcha() {
        if (!isWebForm() || (getAntibot() != null && Constants.NO_CAPTCHA_USED.equals(getAntibot())) || (getAntibot() != null && !"".equals(getAntibot()))) {
            return 0;
        }
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.CAPTCHA_IS_EMPTY_ERROR, 1, null);
        return 1;
    }

    public static String getButtonText() {
        return userSettings.get("buttonText");
    }

    public static void setButtonText(String buttonText) {
        userSettings.put("buttonText", buttonText);
    }

    public static boolean isTrainingCenterEnabled() {
        return "true".equals(userSettings.get(TRAINING_CENTER_ENABLED));
    }

//    public static boolean isKnowledgeGrid() {
//        String companyID = Utils.getEncryptedCompanyID();
//        return (CompanyConstants.C31287.equals(companyID));
//    }

//    public static boolean isAlRaha() {
//        return (CompanyConstants.C47229.equals(Utils.getEncryptedCompanyID()));
//    }

//    public static boolean isMediaCom() {
//        return CompanyConstants.C8687.equals(Utils.getEncryptedCompanyID());
//    }

    /**
     * All relational rpcs must be registred, data collection
     *
     * @param relationType
     * @param relationID
     * @param relationName
     */
    public static void registrRelation(String relationType, Integer relationID, String relationName) {
        if (!relationViews.containsKey(relationType)) {
            relationViews.put(relationType, new HashMap<>());
        }
        relationViews.get(relationType).put(relationID, relationName);
    }

    /**
     * this method queues the relation for adding to the @fromRelationType(the add new form's relation type) form
     *
     * @param fromRelationType
     * @param toRelationType
     * @param toRelationID
     */
    public static void fireRelationEvent(String fromRelationType, String toRelationType, Integer toRelationID) {
        if (fromRelationType != null && toRelationType != null && toRelationID != null) {
            relationQueue.computeIfAbsent(fromRelationType, k -> new HashMap<>());
            relationQueue.get(fromRelationType).computeIfAbsent(toRelationType, k -> new ArrayList<>());
            relationQueue.get(fromRelationType).get(toRelationType).add(toRelationID);
        }
    }

    public static void registrRelation(Relational item) {
        if (item != null) {
            registrRelation(item.getRelationType(), item.getRelationID(), item.getRelationName());
        }
    }

    public static boolean hasAccessToDefaultEmployeeRate(Integer employeeID) {
        if (Utils.isPM() && hasPermission(PermissionConstants.PM_EMPLOYEE_RATE)) {
            return true;
        } else //own wage rate
            if (isHRMS() && employeeID != null && !getUserID().equals(employeeID)) {
                return hasPermission(PermissionConstants.HRMS_EMPLOYEE_WAGE_RATE);
            } else
                return isHRMS() && hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE_WAGE_RATE);
    }

    public static String ConvertNiffToRucc(String taxinspanish) {
        if ((Constants.PANAMA_CITY.equals(Utils.getUserCity()) || Constants.PANAMA_CITY.equals(Utils.getUserCountry())) && "NIF".equals(taxinspanish)) {
            return taxinspanish.replace("NIF", "R.U.C.");
        }
        return taxinspanish;
    }

    public static void setZIndex(Element elem, int index) {
        DOM.setIntStyleAttribute(elem, "zIndex", index);
    }

    public static Date getDateEndTime(Date date) {
        return date != null ? DateUtil.getDayLastTime(date) : null;
    }

    public static DateNonConvertable getStartDateNC(Date date) {
        return date != null ? new DateNonConvertable(DateUtil.resetTime(date)) : null;
    }

    public static DateNonConvertable getEndDateNC(Date date) {
        return date != null ? new DateNonConvertable(DateUtil.getDayLastTime(date)) : null;
    }

    public static String getStartDateNCForFilter(Date date) {
        return date != null ? fpDateFormat.format(DateUtil.resetTime(date)) : "";
    }

    public static String getEndDateNCForFilter(Date date) {
        return date != null ? fpDateFormat.format(DateUtil.getDayLastTime(date)) : "";
    }

    public static String getStartDateNCForFilter(DateNonConvertable date) {
        return (date != null && date.getNonConvertedDate() != null) ? fpDateFormat.format(DateUtil.resetTime(date.getNonConvertedDate())) : "";
    }

    public static String getEndDateNCForFilter(DateNonConvertable date) {
        return (date != null && date.getNonConvertedDate() != null) ? fpDateFormat.format(DateUtil.getDayLastTime(date.getNonConvertedDate())) : "";
    }

    public static boolean isSuperUser() {
        return "true".equals(userSettings.get(SUPER_USER));
    }

    public static void scrollIntoView(Element elem) {
        if (!(isArabicLanguage() && isChrome())) {
            elem.scrollIntoView();
        }
        JQuery.$(elem).trigger(SCROLL_EVENT, null);
    }

    public static boolean timesheetEstimateExceedsValidation() {
        return "true".equals(Utils.userSettings.get(TIMESHEET_VALIDATE_EST));
    }

    public static String getTimesheetDateFormat() {
        return userSettings.get(TIMESHEET_DF);
    }

    public static void showImageOrDownloadFile(FileResource file, boolean download) {
        //common folderdan download qilyatganda page not foundga boryatgani uchun
        String action = file.getDownloadUrl() != null && file.getDownloadUrl().startsWith("common") ? ("/" + file.getDownloadUrl()) : file.getDownloadUrl();
        showImageOrDownloadFile(file, download, action);
    }

    public static void showImageOrDownloadFile(FileResource file, boolean download, FileResource[] fileResources) {
        //common folderdan download qilyatganda page not foundga boryatgani uchun
        String action = file.getDownloadUrl() != null && file.getDownloadUrl().startsWith("common") ? ("/" + file.getDownloadUrl()) : file.getDownloadUrl();
        showImageOrDownloadFile(file, download, action, fileResources);
    }

    public static void showImageOrDownloadFile(FileResource file, boolean download, String action) {
        showImageOrDownloadFile(file, download, action, null);
    }

    public static void showImageOrDownloadFile(FileResource file, boolean download, String action, FileResource[] fileResources) {
        try {
            if (file.getBodyId() != null) {
                if (!download && file.getName() != null && !Constants.GOOGLE.equals(file.getUploadType()) && !Constants.OFFICE_365.equals(file.getUploadType()) && !Constants.OFFICE_365_SHARE_POINT.equals(file.getUploadType())) {
                    if (isImage(file)) {
                        ImageViewerSlidePopup slidePopup = new ImageViewerSlidePopup(file, fileResources);
                        slidePopup.open();
                        return;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        if (file.getContentType() != null && file.getContentType().endsWith("/pdf") || file.getName().toLowerCase().endsWith(".pdf")) {
            if (isEnablePdfViewWithoutDownload()) {
                new PdfViewerPopup(file.getName(), file.getBodyId(), action, true);
            } else {
                Window.open(action, "_blank", "");
            }
        } else if ((file.getContentType() != null
                && (file.getContentType().endsWith("/ogg") || file.getContentType().endsWith("/mpeg") || file.getContentType().endsWith("/mp3")))
                || file.getName().toLowerCase().endsWith(".ogg") || file.getName().toLowerCase().endsWith(".mpeg")
                || file.getName().toLowerCase().endsWith(".mp3")) {
            AudioViewerPopup audioViewerPopup = new AudioViewerPopup(file.getName(), action);
            audioViewerPopup.open();
            audioViewerPopup.playAudioTag();
        } else {
            Window.open(action, "_blank", "");
        }
    }

    public static Boolean isImage(FileResource file) {
        String fileName = file.getName().toLowerCase();
        return file.getContentType() != null && file.getContentType().startsWith("image/") ||
                fileName.endsWith(".jpe") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                fileName.endsWith(".ico") || fileName.endsWith(".png") || fileName.endsWith(".bmp") || fileName.endsWith(".gif");
    }

    public static Boolean isArabicCompany() {
        return (isUAECompany() || isSaudiCompany() || isOmanCompany() || isQatarCompany() || isBahrainCompany() || isKuwaitCompany());
    }

    public static String getPersonalID() {
        return userSettings.get(EMPLOYEE_FORM_PERSONAL_ID) != null && !"null".equals(userSettings.get(EMPLOYEE_FORM_PERSONAL_ID)) ? userSettings.get(EMPLOYEE_FORM_PERSONAL_ID) : "";
    }

    public static String getFacebookAppId() {
        return userSettings.get(FACEBOOK_APP_ID) != null && !"null".equals(userSettings.get(FACEBOOK_APP_ID)) ? userSettings.get(FACEBOOK_APP_ID) : "";
    }

    public static Boolean isUKCompany() {
        return "GB".equals(Utils.getCompanyrCountryCode());
    }

    public static Boolean isUKVATRegistered() {
        return isUKCompany() && isVatRegistered();
    }

    public static Boolean isUAECompany() {
        return "AE".equals(Utils.getCompanyrCountryCode());
    }

    public static Boolean isSaudiCompany() {
        return "SA".equals(Utils.getCompanyrCountryCode());
    }

    public static Boolean isOmanCompany() {
        return "OM".equals(Utils.getCompanyrCountryCode());
    }

    public static Boolean isQatarCompany() {
        return "QA".equals(Utils.getCompanyrCountryCode());
    }

    public static Boolean isBahrainCompany() {
        return "BH".equals(Utils.getCompanyrCountryCode());
    }

    public static Boolean isKuwaitCompany() {
        return "KW".equals(Utils.getCompanyrCountryCode());
    }

    public static String getAsCommoDelimited(List collection, String returnIfNull, String... delimitrs) {
        if (collection == null || collection.size() == 0) {
            return returnIfNull;
        }
        String delimitr = ",";
        if (delimitrs != null && delimitrs.length > 0) {
            delimitr = delimitrs[0];
        }
        StringBuilder ids = new StringBuilder();
        String delim = "";
        for (Object element : collection) {
            ids.append(delim).append(element.toString());
            delim = delimitr;
        }
        return ids.toString();
    }

    public static native NodeList getElementsByName(String name) /*-{
        return $doc.elementsByName(name);
    }-*/;

    public static native boolean hasFocus(Element element) /*-{
        return element.ownerDocument.activeElement == element;
    }-*/;

    public static boolean isAlternativeCalendar() {
        return !isNullOrEmpty(userSettings.get(ALTERNATIVE_CALENDAR_ID)) && !"null".equals(userSettings.get(ALTERNATIVE_CALENDAR_ID));
    }

    public static String getHijriDate(Date date) {
        if (isAlternativeCalendar() && date != null) {
            SimpleHijriDate simpleHijriDate = HijriCalc.toHijri(date);
            return simpleHijriDate.getDatePickerCurrentDateShortFormat();
        }
        return "";
    }

    public static native void table__frame_affix_init() /*-{
        $wnd.table__frame_affix_init();
    }-*/;

    public static native void frame_affix() /*-{
        $wnd.frame_affix();
    }-*/;

    public static native void frame_affix_fixed_top() /*-{
        $wnd.table_report_sections_fixed_top();
    }-*/;

    public static native void scrollTables_afterUpdate2() /*-{
        $wnd.scrollTables_afterUpdate2();
    }-*/;

    public static native void scrollTables_afterUpdate() /*-{
        $wnd.scrollTables_afterUpdate();
    }-*/;

    public static native void scrollTo(int y) /*-{
        $wnd.scrollToKpi(y);
    }-*/;

    public static native void hideDropDownLookUp() /*-{
        $wnd.hide_lookup_dropdown_while_scrolling();
    }-*/;

    public static int[] convertHexToRGB(String hexCode) {
        int[] result = new int[3];
        if (hexCode != null && !"".equals(hexCode)) {
            int index = 0;
            while (index < hexCode.length()) {
                result[index / 2] = hex2decimal((hexCode.substring(index, Math.min(index + 2, hexCode.length()))));
                index += 2;
            }
            return result;
        } else {
            return null;
        }
    }

    private static Integer hex2decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        Integer val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }

    public static String encrypt(String msg) {
        TripleDesCipher cipher = new TripleDesCipher();
        cipher.setKey(GWT_DES_KEY);

        String enc = "";
        try {
            enc = cipher.encrypt(String.valueOf(msg));
        } catch (DataLengthException | InvalidCipherTextException | IllegalStateException e1) {
            e1.printStackTrace();
        }

        return enc;
    }

    public static String decrypt(String msg) {
        TripleDesCipher cipher = new TripleDesCipher();
        cipher.setKey(GWT_DES_KEY);
        String dec = "";
        try {
            dec = cipher.decrypt(msg);
        } catch (DataLengthException | InvalidCipherTextException | IllegalStateException e) {
            e.printStackTrace();
        }

        return dec;
    }

    public static String generateDecimalByScale(int scale) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < scale; i++) {
            builder.append("0");
        }

        return !builder.toString().isEmpty() ? "." + builder : "";
    }


    public static String invertColor(String hexTripletColor) {
        int[] _colur = convertHexToRGB(hexTripletColor);          // convert to integer
        if (0.299 * _colur[0] + 0.587 * _colur[1] + 0.114 * _colur[2] <= 186) {
            return "ffffff";
        } else {
            return "000000";
        }
    }

    /**
     * Check obj if it's null and if the size of the collections and maps
     *
     * @param obj
     * @return boolean
     */
    public static boolean isOk(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).size() > 0;
        }
        if (obj instanceof Map) {
            return ((Map) obj).size() > 0;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj.getClass().isArray()) {
            return true;
        }
        return true;
    }

    public static boolean hideProductPrice() {
        return Utils.hasPermission(PermissionConstants.HIDE_PRODUCT_PRICE) && !Utils.hasRole(ADMIN);
    }

    public static Integer getColumnWidth(Integer columnWidth, Integer defaultWidth) {
        if (columnWidth == null || columnWidth == 0) {
            return defaultWidth;
        }
        return columnWidth;
    }

    public static SelectItem[] getLimit() {
        SelectItem[] items = new SelectItem[8];
        items[0] = new SelectItem(10, "10");
        items[1] = new SelectItem(20, "20");
        items[2] = new SelectItem(30, "30");
        items[3] = new SelectItem(40, "40");
        items[4] = new SelectItem(50, "50");
        items[5] = new SelectItem(100, "100");
        items[6] = new SelectItem(200, "200");
        items[7] = new SelectItem(500, "500");
        return items;
    }

    public static void openParentSection(Widget widget) {

        if (JQuery.$(widget.getElement()).parents(".collapsible-body")
                .parent()
                .find(".collapsible-header").addClass(CssName.ACTIVE)
                .parent().addClass(CssName.ACTIVE)
                .get(0) != null) {
            JQuery.$(widget.getElement()).parents(".collapsible-body")
                    .parent()
                    .find(".collapsible-header").addClass(CssName.ACTIVE)
                    .parent().addClass(CssName.ACTIVE)
                    .get(0).scrollIntoView();
        }
    }

    public static native double getElementTop(Element element)
        /*-{
            var rectObject = element.getBoundingClientRect();
            return rectObject.top;
        }-*/;

    public static LinkedList<Date> setupFinancialQuarties(Date financialYearStart) {
        return setupFinancialQuarties(null, financialYearStart);
    }

    public static LinkedList<Date> setupFinancialQuarties(Date currentDate, Date financialYearStart) {
        LinkedList<Date> financialQuartiesList = new LinkedList<>();

        if (currentDate == null) {
            currentDate = new Date();
        }
        Date currentBeganQuarter = new Date();
        Date currentEndQuarter = new Date();
        int whichQuarter = -1;
        // find current quarter
        if (currentDate.getYear() > financialYearStart.getYear()) {
            whichQuarter = (12 - (financialYearStart.getMonth() + 1) + currentDate.getMonth() + 1) / 3;
            if ((12 - (financialYearStart.getMonth() + 1) + currentDate.getMonth() + 1) % 3 != 0) {
                whichQuarter++;
            }
        } else {
            whichQuarter = ((currentDate.getMonth() + 1) - (financialYearStart.getMonth() + 1)) / 3;
            if (currentDate.getMonth() == financialYearStart.getMonth() || ((currentDate.getMonth() + 1) - (financialYearStart.getMonth() + 1)) % 3 != 0) {
                whichQuarter++;
            }
        }
        switch (whichQuarter) {
            case 1: {
                currentBeganQuarter = financialYearStart;
                currentEndQuarter = DateUtil.addDays(DateUtil.addMonths(financialYearStart, 3), -1);
                break;
            }
            case 2: {
                currentBeganQuarter = DateUtil.addMonths(financialYearStart, 3);
                currentEndQuarter = DateUtil.addDays(DateUtil.addMonths(financialYearStart, 6), -1);
                break;
            }
            case 3: {
                currentBeganQuarter = DateUtil.addMonths(financialYearStart, 6);
                currentEndQuarter = DateUtil.addDays(DateUtil.addMonths(financialYearStart, 9), -1);
                break;
            }
            case 4: {
                currentBeganQuarter = DateUtil.addMonths(financialYearStart, 9);
                currentEndQuarter = DateUtil.addDays(DateUtil.addYears(financialYearStart, 1), -1);
                break;
            }
        }
        financialQuartiesList.add(DateUtil.addMonths(currentBeganQuarter, -3)); // Last Financial Quarter began date
        financialQuartiesList.add(DateUtil.addDays(currentBeganQuarter, -1)); // Last Financial Quarter end date
        financialQuartiesList.add(currentBeganQuarter);  // Current Financial Quarter began date
        financialQuartiesList.add(currentEndQuarter);  // Current Financial Quarter end date
        financialQuartiesList.add(DateUtil.addDays(currentEndQuarter, 1));  // Next Financial Quarter began date
        financialQuartiesList.add(DateUtil.addMonths(currentEndQuarter, 3));  // Next Financial Quarter end date

        for (Date quarterDate : financialQuartiesList) { // normalization quaartes date for understood this code please could you will see @DateUtil.addMonths() method
            if (DateUtil.getDateInMonth(quarterDate.getYear(), quarterDate.getMonth()) < financialYearStart.getDate()
                    || (financialYearStart.getDate() > 28 && DateUtil.getDateInMonth(quarterDate.getYear(), quarterDate.getMonth()) > 28)) {
                quarterDate.setDate(DateUtil.getDateInMonth(financialQuartiesList.get(0).getYear(), quarterDate.getMonth()));
            }
        }

        return financialQuartiesList;
    }

    public static Div div(String className) {
        return new Div(className);
    }

    public static Div div(String className, Serializable... serializables) {
        Div d = div(className);
        if (serializables != null && serializables.length > 0) {
            for (Serializable a : serializables) {
                if (a != null) {
                    d.getElement().setInnerHTML(a.toString());
                }
            }
        }
        return d;
    }

    public static Div div(String className, Widget... widgets) {
        Div d = div(className);
        if (widgets != null && widgets.length > 0) {
            for (Widget a : widgets) {
                if (a != null) {
                    d.add(a);
                }
            }
        }
        return d;
    }

    public static Span span(String className) {
        Span s = new Span();
        if (className != null) {
            s.addStyleName(className);
        }
        return s;
    }

    public static Span span(String className, Serializable a) {
        Span d = span(className);
        if (a != null) {
            d.setText(a.toString());
        }
        return d;
    }

    public static Span span(String className, Widget... widgets) {
        Span d = span(className);
        if (widgets != null) {
            if (widgets != null && widgets.length > 0) {
                for (Widget a : widgets) {
                    if (a != null) {
                        d.add(a);
                    }
                }
            }
        }
        return d;
    }

    public static Icon icon(String className) {
        Icon icon = new Icon();
        if (className != null && !"".equalsIgnoreCase(className)) {
            icon.addStyleName(className);
        }
        return icon;
    }

    public static String getLongAsMinuteAndSecond(long l) {
        int duration = (int) (l / 1000);
        int minute = duration / 60;
        int seconds = duration % 60;
        NumberFormat format = NumberFormat.getFormat("00");
        return format.format(minute) + ":" + format.format(seconds);
    }

    public static String getTimeAsHourAndMinute(Date date) {
        if (date != null) {
            int minute = date.getHours();
            int seconds = date.getMinutes();
            NumberFormat format = NumberFormat.getFormat("00");
            return format.format(minute) + ":" + format.format(seconds);
        }
        return "";
    }

    public static boolean isTestCompany() {
        return "true".equals(userSettings.get(IS_TEST_COMPANY));
    }

    public static boolean isValidFolderName(String folderName) {
        return !isNullOrEmpty(folderName) && !folderName.contains("\\") && !folderName.contains("/")
                && !folderName.contains("*") && !folderName.contains("?")
                && !folderName.contains("<") && !folderName.contains(">")
                && !folderName.contains(":") && !folderName.contains("|");
    }

    public static boolean isCustomField(String columnCode) {
        return columnCode.startsWith("double_value") || columnCode.startsWith("string_value") || columnCode.startsWith("date_value");
    }

    public static String getUploadVersion() {
        return !isNullOrEmpty(userSettings.get(LATEST_SERVER_UPLOAD_VERSION)) ? userSettings.get(LATEST_SERVER_UPLOAD_VERSION) : UUID.uuid(7);
    }

    public static boolean isVatRegistered() {
        return "true".equals(userSettings.get(VAT_REGISTERED));
    }

    public static boolean isVATCashBased() {
        return Utils.isVatRegistered() && AccountingConstants.CASH.equals(userSettings.get(
                VAT_ACCOUNTING_BASIS));
    }

    public static void enableLeftMenu(boolean enable) {
        if (enable) {
            if (RootPanel.getBodyElement().getClassName().contains("customize-open")) {
                RootPanel.getBodyElement().addClassName("left-menu-open");
                RootPanel.getBodyElement().removeClassName("left-menu-closed");
                RootPanel.getBodyElement().removeClassName("customize-open");
            }
        } else {
            RootPanel.getBodyElement().removeClassName("customize-open");
            if (RootPanel.getBodyElement().getClassName().contains("left-menu-open")) {
                RootPanel.getBodyElement().addClassName("left-menu-closed customize-open");
                RootPanel.getBodyElement().removeClassName("left-menu-open");
            }
        }
    }

    public static boolean hasPredefinedValue(String uiType) {
        return list.contains(uiType);
    }

    public static String setTextInCenter(Number value) {
        NumberFormat formatter = Utils.getCalculationNumberFormat();
        if (value == null) {
            return "0.00";
        }
        // Handle negative numbers
        boolean isNegative = false;
        if (value.doubleValue() < 0) {
            isNegative = true;
            value = Math.abs(value.doubleValue());
        }

        if (value instanceof Integer || value instanceof Long) {
            formatter = NumberFormat.getFormat(",##0");
        }

        String count = formatter.format(value);

        if (value.doubleValue() >= 1000000000) {
            count = formatter.format(value.doubleValue() / 1000000000) + wfmStrings.billionShort();
        } else if (value.doubleValue() >= 1000000) {
            count = formatter.format(value.doubleValue() / 1000000) + wfmStrings.millionShort();
        } else if (value.doubleValue() >= 100000) {
            count = formatter.format(value.doubleValue() / 1000) + wfmStrings.thousandShort();
        }

        if (isNegative) {
            count = "-" + count;  // Add '-' sign and adjust the rest of the string
        }

        return count;
    }

    public static String formatWithScale(Number value, Integer scale) {
        if (value.doubleValue() >= 0 && value.doubleValue() < 100000) {
            String s = scale > 0 ? "." : "";
            for (int i = 0; i < scale; i++) {
                s = s.concat("0");
            }
            NumberFormat numberFormat1 = NumberFormat.getFormat(",##0" + s);
            return numberFormat1.format(value);
        } else {
            return setTextInCenter(value);
        }
    }


    public static boolean isEnablePdfViewWithoutDownload() {
        return hasGenericAccess(GenericSettingsEnum.ENABLE_PDF_VIEW_WITHOUT_DOWNLOAD);
    }

    public static boolean isEnablePdfPreView() {
        return hasGenericAccess(GenericSettingsEnum.ENABLE_PDF_PREVIEW);
    }

    public static native void makeDraggable(String elementId) /*-{


        var elmnt = $doc.getElementById(elementId);

        var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;

        elmnt.onmousedown = dragMouseDown;


        function dragMouseDown(e) {
            e = e || window.event;
            e.preventDefault();
            // get the mouse cursor position at startup:
            pos3 = e.clientX;
            pos4 = e.clientY;
            $doc.onmouseup = closeDragElement;
            // call a function whenever the cursor moves:
            $doc.onmousemove = elementDrag;
        }

        function elementDrag(e) {
            e = e || window.event;
            e.preventDefault();
            // calculate the new cursor position:
            pos1 = pos3 - e.clientX;
            pos2 = pos4 - e.clientY;
            pos3 = e.clientX;
            pos4 = e.clientY;
            // set the element's new position:
            elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
            elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
            elmnt.style.bottom = null;
            elmnt.style.right = null;
        }

        function closeDragElement() {
            // stop moving when mouse button is released:
            $doc.onmouseup = null;
            $doc.onmousemove = null;
        }

    }-*/;

    public static int dashboardWidgetsMaxLimit() {
        if (userSettings.get(DASHBOARD_WIDGETS_MAX_LIMIT) != null && !"null".equals(userSettings.get(DASHBOARD_WIDGETS_MAX_LIMIT))) {
            return Integer.parseInt(userSettings.get(DASHBOARD_WIDGETS_MAX_LIMIT));
        }
        return 24;
    }

    public static LinkedHashMap<SelectItem, LinkedList<PropertyItem>> getPropertyListingMap() {
        return propertyListingMap;
    }

    public static HashMap<String, String> getModuleLocalizeMap() {
        return moduleLocalizeMap;
    }

    public static boolean isOffline() {
        return offline;
    }

    public static void onOffline() {
        Info.warn(wfmStrings.checkYourInternetConnection(), Info.Position.BOTTOM_RIGHT);
        Utils.offline = true;
    }

    public static String getUserLanguage() {
        return userLanguage;
    }

    public static void onOnline() {
        Info.show(wfmStrings.yourInternetConnectionBack(), Info.Position.BOTTOM_RIGHT);
        Utils.offline = false;
    }

    public static String getKanbanItemValueFromObject(String relatedFieldCode, Object obj) {
        String result = "";
        try {
            if (relatedFieldCode.contains("string_value")) {
                result = (obj != null ? (String) obj : "");
            } else if (relatedFieldCode.contains("date_value")) {
                if (obj instanceof String) {
                    result = (String) obj;
                } else if (obj instanceof DateNonConvertable) {
                    result = DateUtils.getDateFormatShort(((DateNonConvertable) obj).getDate());
                } else if (obj instanceof Date) {
                    result = DateUtils.getDateFormatShort(((Date) obj));
                } else {
                    result = "";
                }
            } else if (relatedFieldCode.contains("double_value")) {
                if (obj instanceof String) {
                    result = (String) obj;
                } else if (obj instanceof Number) {
                    result = NumberFormat.getFormat(",##0.000").format((Number) obj);
                } else {
                    result = "";
                }
            }
        } catch (Exception ex) {
            result = "";
        }
        GWT.log("field code ===> " + relatedFieldCode + " result " + result);
        return result;
    }

    public static boolean isEmailAccountSetup() {
        return "true".equals(userSettings.get(EMAIL_ACCOUNT_SET_UP));
    }

    public static native void forceReload() /*-{
        $wnd.location.reload(true);
    }-*/;

    public static native void copyToClipBoard(String text) /*-{
        var textArea = document.createElement("textarea");
        textArea.value = text;

        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();
        try {
            var successful = document.execCommand('copy');
            var msg = successful ? 'successful' : 'unsuccessful';
            console.log('Fallback: Copying text command was ' + msg);
        } catch (err) {
            console.error('Fallback: Oops, unable to copy', err);
        }
        document.body.removeChild(textArea);
    }-*/;

    public static native Boolean getDoNotTrack() /*-{
        return navigator.doNotTrack === true || navigator.doNotTrack === '1';
    }-*/;

    private static String getOrGenerateDeviceId() {
        String userDeviceId = Cookies.getCookie("udi");
        if (userDeviceId == null) {
            userDeviceId = com.edatasite.workforce.gwt.documents.client.gwtupload.UUID.uuid();
            Cookies.setCookie("udi", userDeviceId);
        }
        return userDeviceId;
    }

    public static native String getClientPublicIp() /*-{
      return $wnd.localStorage.getItem("public-ip");
    }-*/;


    public static native double getDevicePixelRatio() /*-{
        return $wnd.devicePixelRatio || 1;
    }-*/;

    public static native int getColorDepth() /*-{
        return $wnd.screen.colorDepth || 24;
    }-*/;

    public static native int getScreenHeight() /*-{
        return $wnd.screen.height;
    }-*/;

    public static String getScreensDetails() {
        int screenWidth = getScreenWidth();
        int screenHeight = getScreenHeight();
        double scalingFactor = getDevicePixelRatio();
        int colorDepth = getColorDepth();
        return "width=" + screenWidth + "&height=" + screenHeight + "&scaling-factor=" + scalingFactor + "&colour-depth=" + colorDepth;
    }

    public static native String getWindowSize() /*-{
        var width = $wnd.innerWidth;
        var height = $wnd.innerHeight;
        return "width=" + width + "&height=" + height;
    }-*/;

    private static String getUTCTimeZone() {
        Integer minutes = JsDate.create().getTimezoneOffset();
        return minutes < 0
                ? "UTC+" + Utils.formatMinutes(-1 * minutes)
                : "UTC-" + Utils.formatMinutes(minutes);
    }

    public static FraudPreventionData getFraudPreventionData() {
        FraudPreventionData fraudPreventionData = new FraudPreventionData();
        fraudPreventionData.setGovClientBrowserDoNotTrack(getDoNotTrack());
        fraudPreventionData.setGovClientBrowserJSUserAgent(getUserAgent());
        fraudPreventionData.setGovClientDeviceID(getOrGenerateDeviceId());
        fraudPreventionData.setGovClientPublicIP(getClientPublicIp());
        fraudPreventionData.setGovClientPublicIpTimestamp(generateUTCTimestamp());
        fraudPreventionData.setGovClientScreens(getScreensDetails());
        fraudPreventionData.setGovClientTimezone(getUTCTimeZone());
        fraudPreventionData.setGovClientWindowSize(getWindowSize());
        return fraudPreventionData;
    }

    public static String generateUTCTimestamp() {
        Date date = new Date();
        com.google.gwt.i18n.client.TimeZone timeZone = com.google.gwt.i18n.client.TimeZone.createTimeZone(0); // set the timezone to UTC
        DateTimeFormat format = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return format.format(date, timeZone);
    }

    public static Double getMaxStorage() {
        return maxStorage;
    }

    public static void setMaxStorage(Double maxStorage) {
        Utils.maxStorage = maxStorage;
    }

    public static Double getUsedStorage() {
        return usedStorage;
    }

    public static void setUsedStorage(Double usedStorage) {
        Utils.usedStorage = usedStorage;
    }

    public static String textFormat(String format, final String... args) {
        String retVal = format;
        for (final String current : args) {
            retVal = retVal.replaceFirst("[%][s]", current);
        }
        return retVal;
    }

    public static SelectItem getUserlocationAsSelectItem() {
        return userlocationAsSelectItem;
    }

    public static void setUserlocationAsSelectItem(SelectItem userlocationAsSelectItem) {
        Utils.userlocationAsSelectItem = userlocationAsSelectItem;
    }

    public static Integer getUserDepartment() {
        return userDepartment;
    }

    public static void setUserDepartment(Integer userDepartment) {
        Utils.userDepartment = userDepartment;
    }

    public static SelectItem getUserDepartmentAsSelectItem() {
        return userDepartmentAsSelectItem;
    }

    public static void setUserDepartmentAsSelectItem(SelectItem userDepartmentAsSelectItem) {
        Utils.userDepartmentAsSelectItem = userDepartmentAsSelectItem;
    }

    public static boolean isMaterialAidCategoryEnable() {
        return hasGenericAccess(GenericSettingsEnum.MATERIAL_AID_CATEGORY_ENABLED);
    }

    public static boolean isBrain(){
        return getHostDomain().contains("brainbm") || getHostDomain().equals("brain");
    }
}
