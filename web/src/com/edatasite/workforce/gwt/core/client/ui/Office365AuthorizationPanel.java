package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.rpc.LoginServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

/**
 * Created by Faxriddin on 2-10-16
 */
public class Office365AuthorizationPanel implements CommandConstants, Constants {

    private String officedataType;
    private String section;
    private static final LoginServiceAsync loginService = LoginService.App.get();

    public Office365AuthorizationPanel(String officedataType, boolean autoRedirect) {
        this.officedataType = officedataType;
        if (autoRedirect) {
            autoRedirect();
        }
    }

    private void autoRedirect() {
        setGoogleCookies(officedataType);
        if (GWT.getHostPageBaseURL().endsWith("/")) {
            Utils.redirect(GWT.getHostPageBaseURL() + "office365/auth/link");
        } else {
            Utils.redirect(GWT.getHostPageBaseURL() + "/office365/auth/link");
        }
    }

    public static void setGoogleCookies(String serviceType) {
        Cookies.removeCookie(OFFICE_365_DATA_COKIE);
        Cookies.removeCookie(OFFICE_365_DRIVE_COKIE);
        Cookies.removeCookie(WEBSITE_URL_COOKIE);
        Cookies.removeCookie(OFFICE_365_CONTACT_COKIE);
        if (OFFICE_365_EVENTS.equals(serviceType)) {
            Cookies.setCookie(OFFICE_365_DATA_COKIE, serviceType);
        } else if (OFFICE_365_CONTACTS.equals(serviceType)) {
            Cookies.setCookie(OFFICE_365_CONTACT_COKIE, Utils.getLocationString().toString());
        } else if (OFFICE_365_DOCUMENTS.equals(serviceType)) {
            Cookies.setCookie(OFFICE_365_DRIVE_COKIE, Utils.getLocationString().toString());
        }
    }
}
