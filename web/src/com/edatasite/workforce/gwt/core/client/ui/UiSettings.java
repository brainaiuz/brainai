package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 28.02.2011
 * Time: 9:25:39
 * To change this template use File | Settings | File Templates.
 */

/**
 * Current class  is responsible  to store ui  related settings and
 * constants. Storing all ui settings here makes development easier
 * and more understandable. For the first phase here will be stored
 * localization  or  internationalization settings and color schema
 * constants.
 * <p/>
 * For further info please refer Ruslan Muhammadov.
 */
public class UiSettings implements Constants {

    //----------------- Themes for SYSTEM -------------------------------
    public static final String BLUE_THEME = "workforce";  /*"Blue"*/
    public static final String GREEN_THEME = "green";     /*"Green"*/
    public static final String GREY_THEME = "atm";        /*"Grey"*/
    public static final String MAROON_THEME = "maroon";   /*"Maroon"*/
    public static final String VIOLET_THEME = "coo";      /*"Violet"*/
    public static final String MEDIACOM_THEME = "mediacom";      /*"Mediacom"*/
    public static final String MEDIACOM2_THEME = "mediacom2";      /*"Mediacom2"*/
    public static final String ORANGE_TM_THEME = "orange";      /*"orange"*/
    public static final String TELE_THEME = "tele";      /*"Tele"*/

    //----------------- Internationalization for SYSTEM -------------------------------
    public static final String ARABIC = "ar";
    public static final String ENGLISH = "en";
    public static final String RUSSIAN = "ru";
    public static final String UZBEK = "uz";
    public static final String ITALIAN = "it";
    public static final String TURKEY = "tr";
    public static final String PORTUGUESE = "pt";
    public static final String FRENCH = "fr";
    public static final String SPANISH = "es";
    public static final String DUTCH = "nl";
    public static final String THAI = "th";
    public static final String ENGLISHRIGHT = "he";
    public static final String ENGLISHLAWSON = "en_GB";


    ///////////////////////ACCESS CODE STATUS///////////////////////
    public static final String BLOCKED = "BLOCKED";
    public static final String UNBLOCKED = "UNBLOCKED";
    ///////////////////////// URL //////////////////////////////////
    public final String ACCOUNTING = ACCOUNTING_URL;
    public final String CRM = CRM_URL;
    public final String DASHBOARD = DASHBOARD_URL;
    public final String DOCUMENTS = DOCUMENTS_URL;//GoogleDocuments.html
    public final String HRMS = HRMS_URL;
    public final String MYACCOUNT = MYACCOUNT_URL;
    public final String PAYROLL = PAYROLL_URL;
    public final String PM = PM_URL;
    public final String SETTINGS = SETTINGS_URL;
    public final String WEBSITE = WEBSITE_URL;
    public final String REPORTING = REPORTING_URL;
    public final String REPORTING_SYSTEM = REPORTING_SYSTEM_URL;
    public final String BACKEND = BACKEND_URL;
    public final String TC = TC_URL;
    public final String LOGISTICS = LOGISTICS_URL;
//    public final String EXLIB = EXLIB_URL;

    /**
     * SelectItem's description stores the name of the style.
     */
    public static final SelectItem[] THEMES = new SelectItem[]{
            new SelectItem(0, "Blue", BLUE_THEME),
            new SelectItem(1, "Violet", VIOLET_THEME),
            new SelectItem(2, "Grey", GREY_THEME),
            new SelectItem(3, "Green", GREEN_THEME),
            new SelectItem(4, "Maroon", MAROON_THEME),
            new SelectItem(5, "Mediacom", MEDIACOM_THEME),
            new SelectItem(6, "Mediacom2", MEDIACOM2_THEME),
            new SelectItem(7, "Orange", ORANGE_TM_THEME),
            new SelectItem(8, "Teletech", TELE_THEME)
    };

    public static final SelectItem[] LANGUAGES = new SelectItem[]{
            new SelectItem(0, "English", ENGLISH),
            new SelectItem(1, "عربى", ARABIC),
            new SelectItem(2, "Русский", RUSSIAN),
            new SelectItem(3, "O'zbek", UZBEK)
    };

    public static final SelectItem[] NAME_FORMATS = new SelectItem[]{
            new SelectItem(0, "First Middle Last", "FIRST_MIDDLE_LAST"),
            new SelectItem(1, "First Last Middle", "FIRST_LAST_MIDDLE"),
            new SelectItem(2, "Middle First Last", "MIDDLE_FIRST_LAST"),
            new SelectItem(3, "Middle Last First", "MIDDLE_LAST_FIRST"),
            new SelectItem(4, "Last First Middle", "LAST_FIRST_MIDDLE"),
            new SelectItem(5, "Last Middle First", "LAST_MIDDLE_FIRST"),
            new SelectItem(6, "First Last", "FIRST_LAST"),
            new SelectItem(7, "Last First", "LAST_FIRST")
    };


    public static final SelectItem[] LANGUAGES_FOR_SIGNUP = new SelectItem[]{
            new SelectItem(0, "English", ENGLISH),
            new SelectItem(1, "عربى", ARABIC),
            new SelectItem(2, "Русский", RUSSIAN),
            new SelectItem(3, "O'zbek", UZBEK)
    };

    public static final SelectItem[] MODULES = new SelectItem[]{
            new SelectItem(0, "Accounts", PermissionConstants.ACCOUNTING_MODULE, "", ""),
            new SelectItem(1, "Projects", PermissionConstants.PM_MODULE, "", ""),
            new SelectItem(2, "Humans", PermissionConstants.HRMS_MODULE, "", ""),
            new SelectItem(3, "Sales", PermissionConstants.CRM_MODULE, "", ""),
            new SelectItem(4, "Payroll", PermissionConstants.PAYROLL, "", ""),
            new SelectItem(5, "Documents", PermissionConstants.DOCUMENTS_CONTEXT, "", ""),
            new SelectItem(6, "Iphone Apps", PermissionConstants.IPHONE_APPS, "", ""),
            new SelectItem(7, "Android Apps", PermissionConstants.ANDROID_APPS, "", ""),
            new SelectItem(8, "All", PermissionConstants.ALL, "", "")
    };

    public static final SelectItem[] ACCESS_TOKEN_STATUS = new SelectItem[]{
            new SelectItem(0, "Block", BLOCKED, "", ""),
            new SelectItem(1, "Unblock", UNBLOCKED, "", ""),
    };


    private UiSettings() {
    }

    public static UiSettings getInstance() {
        return new UiSettings();
    }
}
