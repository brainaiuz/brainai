package com.edatasite.workforce.gwt.googlecontacts.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 13.11.2008
 * Time: 20:44:17
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleContactsService extends RemoteService {

    ContactListItem[] getImportedContacts() throws Exception;
    String getUserTeamName();
    void importContacts(ContactListItem[] contactsItem) throws Exception;
    boolean validateCurrentUser();

    boolean validateCurrentOfficeUser();
    void saveToken(String token) throws Exception;
    void deleteGoogleContactToken();

    class App {
        public static GoogleContactsServiceAsync get() {
            ServiceDefTarget target = GWT.create(GoogleContactsService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/googlecontacts");
            return (GoogleContactsServiceAsync) target;
        }
    }
}