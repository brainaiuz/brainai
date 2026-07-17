package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsServerContacts;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365BaseList;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Contact;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.contacts.ContactGroupFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 17.11.2008
 * Time: 18:21:21
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleContactsManager extends Manager<EdsServerContacts> {

    EdsServerContacts getGoogleContact(EdsUser user, boolean withCheck, Boolean... isOfficeContact);

    boolean validateUser(EdsUser user);

    boolean validateOfficeUser(EdsUser user);

    ContactFeed getContactFeed(ContactsService service) throws IOException, ServiceException;

    void createContactDetails(String token) throws GeneralSecurityException, IOException, ServiceException;

    boolean existsEmail(String email) throws GeneralSecurityException, IOException, ServiceException;

    ContactsService getLoggedService() throws AuthenticationException, GeneralSecurityException, IOException;

    ContactsService getLoggedService(EdsUser user) throws AuthenticationException, GeneralSecurityException, IOException;

    String createGroupAndGetID(ContactsService service) throws IOException, ServiceException;

    void createContact(ContactsService service, String name, String email, String groupID) throws IOException, ServiceException;

    ContactListItem[] getGoogleContactItems() throws GeneralSecurityException, IOException, ServiceException;

    ContactListItem[] getGoogleContactItems(List<ContactEntry> googleContacts, EdsUser user) throws GeneralSecurityException, IOException, ServiceException;

    void exportWFTContactsToGoogleContacts(ContactListItem[] items, boolean forExport) throws GeneralSecurityException, IOException, ServiceException;

    void exportWFTContactsToGoogleContacts(List<ContactEntry> googleContacts, ContactListItem[] contactItems, EdsUser user, boolean forExport) throws GeneralSecurityException, IOException, ServiceException;

    List<Integer> deleteContact(List<Integer> contactIDs, Integer userId);

    List<Map<String, ContactEntry>> checkingContactsExistsInGoogle(ContactListItem[] wftContacts, List<ContactEntry> googleContacts, List<ContactListItem> contactItems, EdsUser user, boolean forExport) throws IOException, ServiceException;

    ContactGroupFeed getContactGroupFeed(ContactsService service) throws IOException, ServiceException;

    void createOfficeContactDetails(String objectId, Office365AccessTokenDTO token);

    ContactListItem[] convertOffice365ToContactItems(Office365AccessTokenDTO tokenDTO, Office365BaseList<Office365Contact> googleContacts, EdsUser user);

    ContactListItem[] convertOffice365(Office365AccessTokenDTO tokenDTO, Office365BaseList<Office365Contact> googleContacts, Integer kpiFolderId, EdsUser user);
}
