package com.edatasite.workforce.gwt.profile.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * Created by IntelliJ IDEA.
 * User: Alisher
 * Date: Nov 13, 2009
 * Time: 11:18:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProfileMessages extends Messages {
    String instructionTemplate(String defaultDueDay, String defaultDueDate, String number);

    String smsSettingDeleted();

    String workflowEmployeeSavSuc();

    String workflowEmployeeDeleted();

    String workflowInvoiceSavSuc();

    String workflowInvoiceDeleted();

    String messAreDeleteSMSAccount(String name);

    String messageText(String p0);

    String listBoxLabelText(String p0);

    String weakPasswordWarning(String p0);

    class App {
        public static ProfileMessages get() {
            return (ProfileMessages) GWT.create(ProfileMessages.class);
        }
    }
}
