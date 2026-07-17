package com.edatasite.workforce.gwt.crm.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 8, 2009
 * Time: 3:25:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CrmMessages extends Messages {

    String syncingGoogleContactMessage(String s);

    String googleContactSyncInProgress();

    String messageText(String name);

    String listBoxLabelText(String name);

    String successfullyDeletedButSomeNot(String updating, String notDeletedContactsCount);

    String doYouWantToChangeStatusTo(String p0);

    String successfullyDeletedButSomeNotAccounts(String p0);

    String warningDuplicateDetected(String p0);

    String pleaseSelectOneRow(String p0);

    String errorWhileRetrivingGroupsFromGoogle();

    String errorWhileRetrivingLocalCategories();

    String matchAtLeastOneCategory();

    String groupNotice();

    String massMailingOverLimit(String p0, String p1);

    String template(String p0);

    String theEmailOutIsEnabled();

    String mailingListCreating();

    String messContactsSucDeleted(String p0);


    class App {
        private static CrmMessages instance;

        public static CrmMessages get() {
            if (instance == null) {
                instance = GWT.create(CrmMessages.class);
            }
            return instance;
        }
    }
}