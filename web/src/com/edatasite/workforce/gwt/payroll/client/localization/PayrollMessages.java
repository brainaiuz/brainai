package com.edatasite.workforce.gwt.payroll.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/22/14
 * Time: 6:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PayrollMessages extends Messages {

    String duplicateValidationError(String s);

    String areYouSureWantToDelete(String p0);

    class App {
        private static PayrollMessages instance;

        public static PayrollMessages get() {
            if (instance == null) {
                instance = GWT.create(PayrollMessages.class);
            }
            return instance;
        }
    }
}
