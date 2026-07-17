package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.rpc.LoginServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 17.11.2008
 * Time: 12:48:19
 * To change this template use File | Settings | File Templates.
 */
public class GoogleAuthorizationPanel implements Constants, CommandConstants {

    private static JavaScriptObject window;
    private String gdataType;
    private String section;
    private static final LoginServiceAsync loginService = LoginService.App.get();

    public GoogleAuthorizationPanel(String gdataType) {
        this(gdataType, false);
    }

    public GoogleAuthorizationPanel(String gdataType, boolean autoRedirect) {
        this.gdataType = gdataType;
        if (autoRedirect) {
            autoRedirect();
        } else {
            redirectToGoogleCalendarAuthPage(gdataType);
        }
    }

    /*This Constructor only used for (CRM/Workspace sections) ImportExportGoogleContactView.java class AND not used Some sections*/

    public GoogleAuthorizationPanel(String gdataType, boolean autoRedirect, String section, int synchronizeContacts) {
        this.gdataType = gdataType;
        this.section = section;

        if (autoRedirect) {
            getCookieOnlyContact(section, synchronizeContacts);
            autoRedirect();
        } else {
            redirectToGoogleCalendarAuthPage(gdataType);
        }
    }

    private void autoRedirect() {
        setGoogleCookies(gdataType);
        loginService.getAuthSubURL(GWT.getHostPageBaseURL(), gdataType, new AbstractAsyncCallback<String>() {
            public void success(String authURL) {
                if (authURL != null) {
                    Window.open(authURL, "_blank"/*"_self"*/, "");
                }
            }
        });
    }

    private void getCookieOnlyContact(String section, int synchronizeContacts) {
        String sectionName = "";
        Cookies.setUriEncode(false);
        if ("crm".equals(section))
            sectionName = "Crm.html";
        if (synchronizeContacts == 1) {     // import contacts 1
            Cookies.setCookie(SECTION_HTML, sectionName + "#gcontact|add/add");
        } else if (synchronizeContacts == 2) {   // export contacts 2
            Cookies.setCookie(SECTION_HTML, sectionName + "#gcontact|edit/0");
        } else if (synchronizeContacts == 3) {   // synchronize contacts 3
            Cookies.setCookie(SECTION_HTML, sectionName + "#crmWelcome|contactList");
        }
    }

    public static void redirectToGoogleCalendarAuthPage(String section) {
        setGoogleCookies(GOOGLE_CALENDAR);
        if (section != null && !"".equals(section)) {
                Cookies.setCookie(SECTION_HTML, "Crm.html" + "#" + WORKSPACE_CALENDAR);
        }
        loginService.getAuthSubURL(GWT.getHostPageBaseURL(), GOOGLE_CALENDAR, new AbstractAsyncCallback<String>() {
            public void success(String authURL) {
                Utils.redirect(authURL);
            }
        });
    }

    public static void redirectToGoogleAnalyticsAuthPage() {
        setGoogleCookies(GOOGLE_ANALYTICS);
        loginService.getAuthSubURL(GWT.getHostPageBaseURL(), GOOGLE_ANALYTICS, new AbstractAsyncCallback<String>() {
            public void success(String authURL) {
                Utils.redirect(authURL);
            }
        });
    }

    public static void redirectToGoogleMailAuthPage() {
        setGoogleCookies(GOOGLE_MAIL);
        loginService.getAuthSubURL(GWT.getHostPageBaseURL(), GOOGLE_MAIL, new AbstractAsyncCallback<String>() {
            public void success(String authURL) {
                window = Utils.openPopupWindow(authURL);
            }
        });
    }

    public static void closePopupWindow() {
        Utils.closePopupWindow(window);
    }

    public static void setGoogleCookies(String serviceType) {
        Cookies.setCookie(GOOGLE_DATA_COKIE, serviceType);
        Cookies.setCookie(PROTOCOL, GWT.getHostPageBaseURL().startsWith("https://") ? "https" : "http");
    }
}
