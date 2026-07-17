package com.edatasite.workforce.gwt.googlecontacts.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 13.11.2008
 * Time: 20:44:45
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleContactsServiceAsync {

    void getImportedContacts(AsyncCallback<ContactListItem[]> callback);
    void getUserTeamName(AsyncCallback<String> callback);

    void importContacts(ContactListItem[] contactsItem, AsyncCallback callback);

    void validateCurrentUser(AsyncCallback<Boolean> callback);

    void validateCurrentOfficeUser(AsyncCallback<Boolean> callback);

    void saveToken(String token, AsyncCallback callback);

    void deleteGoogleContactToken(AsyncCallback async);
}