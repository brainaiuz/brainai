package com.finnetlimited.reportservice.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.security.ClientSecurityContext;

/**
 * Created by IntelliJ IDEA.
 * User: ULA
 * Date: Nov 4, 2010
 * Time: 2:58:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserClientUtils {
    public static boolean setSessionID() {
        /***
         * @see com.edatasite.workforce.gwt.core.client.ui.entryPoints.GeneralEntryPoint#onModuleLoad()
         */
        String sessionId = Cookies.getCookie(Constants.SESSION_ID_COOKIE);
        if (sessionId != null && sessionId.length() > 0) {
            ClientSecurityContext.get().setSessionId(sessionId);
            return true;
        } else {
            Cookies.setCookie(com.edatasite.workforce.gwt.core.client.ui.Constants.SECTION_HTML, Window.Location.getPath().replace("/",""));
            Utils.redirect(GWT.getHostPageBaseURL() + "index.html");
            return false;
        }
    }

    public static int getCompanyID() {
        return Integer.parseInt(ClientSecurityContext.get().getSessionId().split("[$]")[1]);
    }
}
