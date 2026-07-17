package com.edatasite.workforce.gwt.myaccount.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * Created by IntelliJ IDEA.
 * User: HRS
 * Date: 13.11.2008
 * Time: 17:04:31
 * To change this template use File | Settings | File Templates.
 */

public interface MyAccountMessages extends Messages {

    String totalcurrency(String str);

    String securePageDesc1(String p1);

    String nonUserTooltip(String fullHost, String productName);

    String clickPayNowToProceed(String p0, String p1);

    class App {
        public static MyAccountMessages get() {
            return (MyAccountMessages) GWT.create(MyAccountMessages.class);
        }
    }
}
