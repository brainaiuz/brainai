package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.EdsGoogleWFTGroups;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.RolePermissionServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.googlegroups.GoogleGroupsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.CrmLeadEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.CrmContactCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.office365.resources.*;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365AuthService;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365ContactService;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gdata.client.Query;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.contacts.*;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 17.11.2008
 * Time: 18:22:22
 * To change this template use File | Settings | File Templates.
 */
@Repository("googleContactsManager")
public class GoogleContactsManagerImpl extends BaseManager<EdsServerContacts> implements GoogleContactsManager, Constants {

    private static final String GOOGLE_CONTACTS_URL = "https://www.google.com/m8/feeds/contacts/default/full";
    private static final String GOOGLE_CONTACTS_GROUP_NAME = "GoogleContactsGroup";
    private static final String DEFAULT_PROJECTION = "thin";

    @Autowired
    private GoogleManager googleManager;
    @Autowired
    private CrmContactItemParamsManager contactItemParamsManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    protected UserManager userManager;
    @Autowired
    protected RoleManager roleManager;
    @Autowired
    private GoogleGroupsManager googleGroupsManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private RolePermissionServiceLocal rolePermissionServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private WorkflowRuleManager workflowRuleManager;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private Office365AuthService office365AuthService;
    @Autowired
    private Office365ContactService office365ContactService;


    public GoogleContactsManagerImpl() {
        super(EdsServerContacts.class);
    }

    public EdsServerContacts getGoogleContact(EdsUser user, boolean withCheck, Boolean... isOfficeContact) {
        String sss = "(gc.isOfficeContact <> true or gc.isOfficeContact=null)";
        if (isOfficeContact.length > 0 && isOfficeContact[0] != null && isOfficeContact[0]) {
            sss = "gc.isOfficeContact = true";
        }
        return (EdsServerContacts) findSingle("from EdsServerContacts gc where gc.user=? and " + sss + " " + (withCheck ? " and (gc.active is null or gc.active=true)" : ""), user);
    }

    public boolean validateUser(EdsUser user) {
        EdsServerContacts contacts = getGoogleContact(user, true);
        if (contacts != null) {
            return contacts.getToken() != null;
        } else {
            return false;
        }
    }

    public boolean validateOfficeUser(EdsUser user) {
        EdsServerContacts contacts = getGoogleContact(user, true, true);
        if (contacts != null) {
            return contacts.getToken() != null;
        } else {
            return false;
        }
    }

    private boolean login(ContactsService service, EdsServerContacts googleContacts) throws AuthenticationException, GeneralSecurityException, IOException {
        if (googleContacts == null) {
            return false;
        }

        return googleManager.loginAuth(service, googleContacts.getToken());
    }

    private ContactsService getService() {
        return new ContactsService(APPLICATION_NAME, PROTOCOL, DOMAIN_NAME);
    }

    public ContactsService getLoggedService() throws AuthenticationException, GeneralSecurityException, IOException {
        EdsUser user = getUser();
        return getLoggedService(user);
    }

    public ContactsService getLoggedService(EdsUser user) throws AuthenticationException, GeneralSecurityException, IOException {
        ContactsService service = getService();
        if (!login(service, getGoogleContact(user, true))) {
            return null;
        }

        return service;
    }

    public ContactFeed getContactFeed(ContactsService service) throws IOException, ServiceException {
        URL url = googleManager.getURL(GOOGLE_CONTACTS_URL);
        Query query = new Query(url);
        query.setMaxResults(CONTACTS_LIMIT);
        return googleManager.getFeed(service, query, ContactFeed.class);
    }

    public ContactGroupFeed getContactGroupFeed(ContactsService service) throws IOException, ServiceException {
        EdsUser user = userManager.getUser();
        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(user.getCompany().getObjectID());
        String googleContactGroupUrl = "https://www.google.com/m8/feeds/groups/default/full?max-results=40";
        return googleManager.getFeed(service, googleContactGroupUrl, ContactGroupFeed.class);
    }

    @Override
    public void createOfficeContactDetails(String objectId, Office365AccessTokenDTO token) {
        EdsUser user = getUser();
        EdsServerContacts googleContact = getGoogleContact(user, false, true);
        if (googleContact == null) {
            googleContact = new EdsServerContacts();
            googleContact.setUser(user);
        }

        Office365AccessTokenDTO tokenDTO = office365AuthService.assureAccessToken(EdsContextParams.getHost(), token, OFFICE_365);
        if (tokenDTO == null) {
            return;
        }
        googleContact.setToken(tokenDTO.getAccessToken());
        googleContact.setGoogleID(objectId);
        googleContact.setActive(true);
        googleContact.setAttempts(0);
        googleContact.setReason(null);
        googleContact.setIsOfficeContact(true);
        if (googleContact.getObjectID() == null) {
            create(googleContact);
        } else {
            update(googleContact);
        }
    }

    @Override
    public ContactListItem[] convertOffice365ToContactItems(Office365AccessTokenDTO tokenDTO, Office365BaseList<Office365Contact> office365Contacts, EdsUser user) {
        int i = 0;
        if (user == null) {
            user = getUser();
        }

        List<ContactListItem> items = new ArrayList<>();
        if (office365Contacts == null || office365Contacts.size() == 0) {
            return null;
        }
        for (Office365Contact entry : office365Contacts) {
            try {
                ContactListItem item = new ContactListItem();
                // set contact full name
                item.setTitle(entry.getTitle());
                item.setFirstName(entry.getGivenName());
                item.setLastName(entry.getSurname());
                item.setOtherName(entry.getNickName());
                item.setMiddleName(entry.getMiddleName());

                item.setGoogleId(entry.getId());

                if (entry.getBirthday() != null) {
                    item.setBirthDate(new DateNonConvertable(entry.getBirthday()));
                }

                item.setUpdatedDate(entry.getDateTimeLastModified());

                int j = 0;
                if (entry.getHomePhones() != null && !entry.getHomePhones().isEmpty()) {
                    SelectItem[] phones = new SelectItem[entry.getHomePhones().size()];
                    for (String phone : entry.getHomePhones()) {
                        item.getHomePhone().add(phone);
                        phones[j] = new SelectItem(j, phone);
                    }
                    item.setPhoneNumbers(phones);
                }
                if (entry.getBusinessPhones() != null && !entry.getBusinessPhones().isEmpty()) {
                    SelectItem[] phones = new SelectItem[entry.getBusinessPhones().size()];
                    for (String phone : entry.getBusinessPhones()) {
                        item.getWorkPhone().add(phone);
                        phones[j] = new SelectItem(j, phone);
                    }
                    item.setPhoneNumbers(phones);
                }
                if (entry.getMobilePhone() != null && !"".equals(entry.getMobilePhone())) {
                    SelectItem[] phones = new SelectItem[1];
                    item.getMobile().add(entry.getMobilePhone());
                    phones[j] = new SelectItem(j, entry.getMobilePhone());
                }

                if (item.getPhoneNumbers() != null && item.getPhoneNumbers().length > 0) {
                    item.setPrimaryPhone(item.getPhoneNumbers()[0].getName());
                }

                j = 0;
                if (entry.getEmailAddresses() != null && !entry.getEmailAddresses().isEmpty()) {
                    SelectItem[] emails = new SelectItem[entry.getEmailAddresses().size()];
                    for (Office365EmailAddress mail : entry.getEmailAddresses()) {
                        if (mail != null) {
                            String emailAddress = mail.getAddress();
                            emails[j] = new SelectItem(j, mail.getAddress() != null ? mail.getAddress() : "");
                            item.getWorkEmail().add(emailAddress);
                            j++;
                        }
                    }
                    if (entry.getEmailAddresses() != null && !entry.getEmailAddresses().isEmpty()) {
                        item.setPrimaryEmail(entry.getEmailAddresses().get(0).getAddress());
                    }
                    item.setEmails(emails);
                }

                boolean addToList = false;
                List<EdsGoogleWFTGroups> groupSettings = googleGroupsManager.getGroupSettings(userManager.getUser(), true);
                if (groupSettings != null && !groupSettings.isEmpty()) {
                    ArrayList<SelectItem> groups = new ArrayList<>();
                    for (EdsGoogleWFTGroups setting : groupSettings) {
                        SelectItem selectItem = new SelectItem();
                        ArrayList<String> officeMembers = office365ContactService.getContactGroupMembers(tokenDTO, setting.getGoogleGroupID());
                        if (officeMembers != null && officeMembers.contains(item.getPrimaryEmail())) {
                            selectItem.setId(setting.getWftGroupID());
                            groups.add(selectItem);
                            addToList = true;
                        }
                    }
                    item.setSelectedCategories(groups);
                }

                // set contact Structured Postal Addresses
                j = 0;
//                int itemsize = entry.getOtherAddress().size();
                int itemsize = entry.getOtherAddress() != null ? 1 : 0;

                if (entry.getHomeAddress() != null) {
                    itemsize = itemsize + 1;
                }
                if (entry.getBusinessAddress() != null && !"".equals(entry.getBusinessAddress())) {
                    itemsize = itemsize + 1;
                }
                SelectItem[] structuredPostalAddresses = new SelectItem[itemsize];
                SelectItem[] countries = new SelectItem[itemsize];
                SelectItem[] cities = new SelectItem[itemsize];
                SelectItem[] regions = new SelectItem[itemsize];
                SelectItem[] postCods = new SelectItem[itemsize];

                if (entry.getHomeAddress() != null) {
                    Office365PhysicalAddress homeaddress = entry.getHomeAddress();
                    structuredPostalAddresses[j] = new SelectItem(j, homeaddress.getStreet());
                    countries[j] = new SelectItem(j, homeaddress.getCountryOrRegion());
                    cities[j] = new SelectItem(j, homeaddress.getCity());
                    regions[j] = new SelectItem(j, homeaddress.getState());
                    postCods[j] = new SelectItem(j, homeaddress.getPostalCode());

                    Address address = new Address();
                    address.setAddress(homeaddress.getStreet());
                    address.setCountry(homeaddress.getCountryOrRegion());
                    address.setCity(homeaddress.getCity());
                    address.setState(homeaddress.getState());
                    address.setZipCode(homeaddress.getPostalCode());
                    address.setRelationType(AddressReference.HOME.getId());
                    item.getAddresses().add(address);
                    j++;
                }
                if (entry.getBusinessAddress() != null) {
                    Office365PhysicalAddress workAdress = entry.getBusinessAddress();
                    structuredPostalAddresses[j] = new SelectItem(j, workAdress.getStreet());
                    countries[j] = new SelectItem(j, workAdress.getCountryOrRegion());
                    cities[j] = new SelectItem(j, workAdress.getCity());
                    regions[j] = new SelectItem(j, workAdress.getState());
                    postCods[j] = new SelectItem(j, workAdress.getPostalCode());

                    Address address = new Address();
                    address.setAddress(workAdress.getStreet());
                    address.setCountry(workAdress.getCountryOrRegion());
                    address.setCity(workAdress.getCity());
                    address.setState(workAdress.getState());
                    address.setZipCode(workAdress.getPostalCode());
                    address.setRelationType(AddressReference.WORK.getId());
                    item.getAddresses().add(address);
                    j++;
                }

                if (entry.getOtherAddress() != null /*&& entry.getOtherAddress().size() > 0*/) {
//                    for (Office365PhysicalAddress otherAddress : entry.getOtherAddress()) {
                    Office365PhysicalAddress otherAddress = entry.getOtherAddress();
                    structuredPostalAddresses[j] = new SelectItem(j, otherAddress.getStreet());
                    countries[j] = new SelectItem(j, otherAddress.getCountryOrRegion());
                    cities[j] = new SelectItem(j, otherAddress.getCity());
                    regions[j] = new SelectItem(j, otherAddress.getState());
                    postCods[j] = new SelectItem(j, otherAddress.getPostalCode());

                    Address address = new Address();
                    address.setAddress(otherAddress.getStreet());
                    address.setCountry(otherAddress.getCountryOrRegion());
                    address.setCity(otherAddress.getCity());
                    address.setState(otherAddress.getState());
                    address.setZipCode(otherAddress.getPostalCode());
                    address.setRelationType(AddressReference.OTHER.getId());
                    item.getAddresses().add(address);
                    j++;
//                    }
                }
                item.setAddress(structuredPostalAddresses);
                item.setCountries(countries);
                item.setCities(cities);
                item.setStates(regions);
                item.setPostCods(postCods);

                // set contact Organization and Position name
                if (entry.getCompanyName() != null && !"".equals(entry.getCompanyName())) {
                    item.getCrmAccount().setName(entry.getCompanyName());

                }
                if (entry.getJobTitle() != null && !"".equals(entry.getJobTitle())) {
                    item.setJobTitle(entry.getJobTitle());
                }
                if (entry.getDepartment() != null && !"".equals(entry.getDepartment())) {
                    item.setDepartment(entry.getDepartment());
                }

                j = 0;
                if (entry.getImAddresses() != null && !entry.getImAddresses().isEmpty()) {
                    SelectItem[] imAddresses = new SelectItem[entry.getImAddresses().size()];
                    for (String address : entry.getImAddresses()) {
                        imAddresses[j] = new SelectItem(j, address);
                        item.getAIM().add(address);
                        j++;
                    }
                    item.setImAddress(imAddresses);
                }

                if (entry.getPersonalNotes() != null) {
                    item.setNote(entry.getPersonalNotes());
                }
                item.setContactType(1);    // Contact type : CRM Contact
                if (addToList) {
                    items.add(item);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return items.toArray(new ContactListItem[]{});
    }

    @Override
    public ContactListItem[] convertOffice365(Office365AccessTokenDTO tokenDTO, Office365BaseList<Office365Contact> office365Contacts, Integer kpiFolderId, EdsUser user) {

        List<ContactListItem> items = new ArrayList<>();
        if (office365Contacts == null || office365Contacts.isEmpty()) {
            return null;
        }
        for (Office365Contact entry : office365Contacts) {
            try {
                ContactListItem item = new ContactListItem();
                // set contact full name
                item.setTitle(entry.getTitle());
                item.setFirstName(entry.getGivenName());
                item.setLastName(entry.getSurname());
                item.setOtherName(entry.getNickName());
                item.setMiddleName(entry.getMiddleName());

                item.setGoogleId(entry.getId());

                if (entry.getBirthday() != null) {
                    item.setBirthDate(new DateNonConvertable(entry.getBirthday()));
                }

                item.setUpdatedDate(entry.getDateTimeLastModified());

                int j = 0;
                if (entry.getHomePhones() != null && !entry.getHomePhones().isEmpty()) {
                    SelectItem[] phones = new SelectItem[entry.getHomePhones().size()];
                    for (String phone : entry.getHomePhones()) {
                        item.getHomePhone().add(phone);
                        phones[j] = new SelectItem(j, phone);
                    }
                    item.setPhoneNumbers(phones);
                }
                if (entry.getBusinessPhones() != null && !entry.getBusinessPhones().isEmpty()) {
                    SelectItem[] phones = new SelectItem[entry.getBusinessPhones().size()];
                    for (String phone : entry.getBusinessPhones()) {
                        item.getWorkPhone().add(phone);
                        phones[j] = new SelectItem(j, phone);
                    }
                    item.setPhoneNumbers(phones);
                }
                if (entry.getMobilePhone() != null && !"".equals(entry.getMobilePhone())) {
                    SelectItem[] phones = new SelectItem[1];
                    item.getMobile().add(entry.getMobilePhone());
                    phones[j] = new SelectItem(j, entry.getMobilePhone());
                }

                if (item.getPhoneNumbers() != null && item.getPhoneNumbers().length > 0) {
                    item.setPrimaryPhone(item.getPhoneNumbers()[0].getName());
                }

                j = 0;
                if (entry.getEmailAddresses() != null && !entry.getEmailAddresses().isEmpty()) {
                    SelectItem[] emails = new SelectItem[entry.getEmailAddresses().size()];
                    for (Office365EmailAddress mail : entry.getEmailAddresses()) {
                        if (mail != null) {
                            String emailAddress = mail.getAddress();
                            emails[j] = new SelectItem(j, mail.getAddress() != null ? mail.getAddress() : "");
                            item.getWorkEmail().add(emailAddress);
                            j++;
                        }
                    }
                    if (entry.getEmailAddresses() != null && !entry.getEmailAddresses().isEmpty()) {
                        item.setPrimaryEmail(entry.getEmailAddresses().get(0).getAddress());
                    }
                    item.setEmails(emails);
                }

                if (kpiFolderId != null) {
                    SelectItem categoryItem = new SelectItem();
                    categoryItem.setId(kpiFolderId);
                    ArrayList<SelectItem> folders = new ArrayList<>();
                    folders.add(categoryItem);
                    item.setSelectedCategories(folders);
                }

                // set contact Structured Postal Addresses
                j = 0;
//                int itemsize = entry.getOtherAddress().size();
                int itemsize = entry.getOtherAddress() != null ? 1 : 0;
                if (entry.getHomeAddress() != null) {
                    itemsize = itemsize + 1;
                }
                if (entry.getBusinessAddress() != null) {
                    itemsize = itemsize + 1;
                }
                SelectItem[] structuredPostalAddresses = new SelectItem[itemsize];
                SelectItem[] countries = new SelectItem[itemsize];
                SelectItem[] cities = new SelectItem[itemsize];
                SelectItem[] regions = new SelectItem[itemsize];
                SelectItem[] postCods = new SelectItem[itemsize];

                if (entry.getHomeAddress() != null) {
                    Office365PhysicalAddress homeaddress = entry.getHomeAddress();
                    structuredPostalAddresses[j] = new SelectItem(j, homeaddress.getStreet());
                    countries[j] = new SelectItem(j, homeaddress.getCountryOrRegion());
                    cities[j] = new SelectItem(j, homeaddress.getCity());
                    regions[j] = new SelectItem(j, homeaddress.getState());
                    postCods[j] = new SelectItem(j, homeaddress.getPostalCode());

                    Address address = new Address();
                    address.setAddress(homeaddress.getStreet());
                    address.setCountry(homeaddress.getCountryOrRegion());
                    address.setCity(homeaddress.getCity());
                    address.setState(homeaddress.getState());
                    address.setZipCode(homeaddress.getPostalCode());
                    address.setRelationType(AddressReference.HOME.getId());
                    item.getAddresses().add(address);
                    j++;
                }
                if (entry.getBusinessAddress() != null) {
                    Office365PhysicalAddress workAdress = entry.getBusinessAddress();
                    structuredPostalAddresses[j] = new SelectItem(j, workAdress.getStreet());
                    countries[j] = new SelectItem(j, workAdress.getCountryOrRegion());
                    cities[j] = new SelectItem(j, workAdress.getCity());
                    regions[j] = new SelectItem(j, workAdress.getState());
                    postCods[j] = new SelectItem(j, workAdress.getPostalCode());

                    Address address = new Address();
                    address.setAddress(workAdress.getStreet());
                    address.setCountry(workAdress.getCountryOrRegion());
                    address.setCity(workAdress.getCity());
                    address.setState(workAdress.getState());
                    address.setZipCode(workAdress.getPostalCode());
                    address.setRelationType(AddressReference.WORK.getId());
                    item.getAddresses().add(address);
                    j++;
                }

                if (entry.getOtherAddress() != null /*&& !entry.getOtherAddress().isEmpty()*/) {
//                    for (Office365PhysicalAddress otherAddress : entry.getOtherAddress()) {
                    Office365PhysicalAddress otherAddress = entry.getOtherAddress();
                    structuredPostalAddresses[j] = new SelectItem(j, otherAddress.getStreet());
                    countries[j] = new SelectItem(j, otherAddress.getCountryOrRegion());
                    cities[j] = new SelectItem(j, otherAddress.getCity());
                    regions[j] = new SelectItem(j, otherAddress.getState());
                    postCods[j] = new SelectItem(j, otherAddress.getPostalCode());

                    Address address = new Address();
                    address.setAddress(otherAddress.getStreet());
                    address.setCountry(otherAddress.getCountryOrRegion());
                    address.setCity(otherAddress.getCity());
                    address.setState(otherAddress.getState());
                    address.setZipCode(otherAddress.getPostalCode());
                    address.setRelationType(AddressReference.OTHER.getId());
                    item.getAddresses().add(address);
                    j++;
//                    }
                }
                item.setAddress(structuredPostalAddresses);
                item.setCountries(countries);
                item.setCities(cities);
                item.setStates(regions);
                item.setPostCods(postCods);

                // set contact Organization and Position name
                if (StringUtils.isNotBlank(entry.getCompanyName())) {
                    item.getCrmAccount().setName(entry.getCompanyName());

                }
                if (StringUtils.isNotBlank(entry.getJobTitle())) {
                    item.setJobTitle(entry.getJobTitle());
                }
                if (StringUtils.isNotBlank(entry.getDepartment())) {
                    item.setDepartment(entry.getDepartment());
                }

                j = 0;
                if (entry.getImAddresses() != null && !entry.getImAddresses().isEmpty()) {
                    SelectItem[] imAddresses = new SelectItem[entry.getImAddresses().size()];
                    for (String address : entry.getImAddresses()) {
                        imAddresses[j] = new SelectItem(j, address);
                        item.getAIM().add(address);
                        j++;
                    }
                    item.setImAddress(imAddresses);
                }

                if (entry.getPersonalNotes() != null) {
                    item.setNote(entry.getPersonalNotes());
                }

                item.setContactType(1);    // Contact type : CRM Contact
                items.add(item);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return items.toArray(new ContactListItem[]{});
    }

    public void createContactDetails(String token) throws GeneralSecurityException, IOException, ServiceException {
        EdsUser user = getUser();
        if (!validateUser(user)) {
            EdsServerContacts googleContact = getGoogleContact(user, false);
            if (googleContact == null) {
                googleContact = new EdsServerContacts();
                googleContact.setUser(user);
            }

            ContactsService service = getService();
            if (!googleManager.loginAuth(service, token)) {
                return;
            }
            googleContact.setToken(token);
            googleContact.setGoogleID(getGoogleID(service));
            googleContact.setActive(true);
            googleContact.setAttempts(0);
            googleContact.setReason(null);
            if (googleContact.getObjectID() == null) {
                create(googleContact);
            } else {
                update(googleContact);
            }
        }
    }

    private String getGoogleID(ContactsService service) throws IOException, ServiceException {
        return googleManager.getFeed(service, GOOGLE_CONTACTS_URL, ContactFeed.class).getAuthors().get(0).getEmail();
    }

    public boolean existsEmail(String email) throws GeneralSecurityException, IOException, ServiceException {
        ContactFeed feed = getContactFeed(getLoggedService());
        for (ContactEntry entry : feed.getEntries()) {
            for (Email mail : entry.getEmailAddresses()) {
                if (mail.getAddress().equalsIgnoreCase(email)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String createGroupAndGetID(ContactsService service) throws IOException, ServiceException {
        ContactGroupEntry groupEntry = null;
        ContactGroupFeed feed = getContactGroupFeed(service);
        for (ContactGroupEntry entry : feed.getEntries()) {
            if (entry.getTitle().getPlainText().equals(GOOGLE_CONTACTS_GROUP_NAME)) {
                groupEntry = entry;
            }
        }

        if (groupEntry == null) {
            groupEntry = new ContactGroupEntry();
            groupEntry.setTitle(new PlainTextConstruct(GOOGLE_CONTACTS_GROUP_NAME));
            EdsUser user = userManager.getUser();
            EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(user.getCompany().getObjectID());
            String googleContactGroupUrl = "https://www.google.com/m8/feeds/groups/default/full?max-results=40";
            groupEntry = googleManager.insert(service, googleContactGroupUrl, groupEntry);
        }

        return groupEntry.getId();
    }

    public void createContact(ContactsService service, String name, String email, String groupID) throws IOException, ServiceException {
        ContactEntry contactEntry = new ContactEntry();
        contactEntry.setTitle(new PlainTextConstruct(name));
        Email mail = new Email();
        mail.setAddress(email);
        mail.setRel(Email.Rel.OTHER);
        contactEntry.addEmailAddress(mail);
        contactEntry.addGroupMembershipInfo(new GroupMembershipInfo(false, groupID));

        googleManager.insert(service, GOOGLE_CONTACTS_URL, contactEntry);
    }

    public ContactListItem[] getGoogleContactItems() throws GeneralSecurityException, IOException, ServiceException {
        EdsUser user = getUser();
        List<ContactEntry> entries = getContactFeed(getLoggedService(user)).getEntries();
        return getGoogleContactItems(entries, user);
    }

    public ContactListItem[] getGoogleContactItems(List<ContactEntry> googleContacts, EdsUser user) throws GeneralSecurityException, IOException, ServiceException {
        int i = 0;
        List<ContactEntry> entries;
        if (user == null) {
            user = getUser();
        }
        if (googleContacts == null) {
            entries = getContactFeed(getLoggedService(user)).getEntries();
        } else {
            entries = googleContacts;
        }

        List<ContactListItem> items = new ArrayList<>();
        if (entries == null || entries.size() == 0) {
            return null;
        }
        for (ContactEntry entry : entries) {
            try {
                ContactListItem item = new ContactListItem();
                // set contact full name
                if (entry.getTitle() != null && !"".equals(entry.getTitle()) && entry.getTitle().getPlainText() != null && !"".equals(entry.getTitle().getPlainText())) {
                    item.setContactName(entry.getTitle().getPlainText());
                    String fullName = entry.getTitle().getPlainText();
                    if (fullName.contains(".")) {
                        fullName.replace(".", " ");
                    }
                    if (fullName.contains(" ")) {
                        String[] s = fullName.split(" ");
                        if (s[0] != null) {
                            item.setFirstName(s[0]);
                        }
                        StringBuilder lastName = new StringBuilder("");
                        if (s.length > 1) {
                            for (int j = 1; j < s.length; j++) {
                                if (s[j] != null) {
                                    lastName.append(s[j] + " ");
                                }
                            }
                            item.setLastName(lastName.toString());
                        }
                    } else {
                        item.setFirstName(fullName);
                    }
                }
                if (entry.getNickname() != null) {
                    item.setOtherName(entry.getNickname().getValue());
                }

                if (entry.getName() != null) {
                    if (entry.getName().getGivenName() != null && entry.getName().getGivenName().getValue() != null) {
                        item.setFirstName(entry.getName().getGivenName().getValue());
                    }

                    if (entry.getName().getFamilyName() != null && entry.getName().getFamilyName().getValue() != null) {
                        item.setLastName(entry.getName().getFamilyName().getValue());
                    }

                    if (entry.getName().getAdditionalName() != null && entry.getName().getAdditionalName().getValue() != null) {
                        item.setMiddleName(entry.getName().getAdditionalName().getValue());
                    }
                }

                if (entry.getName() != null && entry.getName().getNamePrefix() != null) {
                    item.setTitle(entry.getName().getNamePrefix().getValue());
                }
                boolean addToList = false;
                List<EdsGoogleWFTGroups> groupSettings = googleGroupsManager.getGroupSettings(userManager.getUser(), false);
                if (groupSettings != null && groupSettings.size() != 0) {
                    ArrayList<SelectItem> groups = new ArrayList<>();
                    if (entry.getGroupMembershipInfos() != null && entry.getGroupMembershipInfos().size() != 0) {
                        for (GroupMembershipInfo group : entry.getGroupMembershipInfos()) {
                            for (EdsGoogleWFTGroups setting : groupSettings) {
                                SelectItem selectItem = new SelectItem();
                                if (setting.getGoogleGroupID().equals((group.getHref().split("/")[entry.getId().split("/").length - 1].hashCode() + ""))) {
                                    selectItem.setId(setting.getWftGroupID());
                                    groups.add(selectItem);
                                    addToList = true;
                                }
                            }
                        }
                    }
                    item.setSelectedCategories(groups);
                }

                // set google contact unique id
                item.setGoogleId(entry.getId());

                // set contact birth date
                if (entry.getBirthday() != null) {
                    String birthDay = entry.getBirthday().getValue();
                    Calendar calendar;
                    if (birthDay.startsWith("--")) {//Birthday date, given in format YYYY-MM-DD (with the year), or --MM-DD (without the year).
                        calendar = new GregorianCalendar();
                        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
                        calendar.set(Calendar.MONTH, Integer.parseInt(birthDay.substring(2, 4)) - 1);
                        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(birthDay.substring(5, 7)));
                    } else {
                        calendar = new GregorianCalendar();
                        calendar.set(Calendar.YEAR, Integer.parseInt(birthDay.substring(0, 4)));
                        calendar.set(Calendar.MONTH, Integer.parseInt(birthDay.substring(5, 7)) - 1);
                        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(birthDay.substring(8, 10)));
                    }
                    Date bdate = calendar.getTime();
                    item.setBirthDate(new DateNonConvertable(bdate));
                }

                item.setUpdatedDate(new Date(entry.getUpdated().getValue()));
                // set contact phone numbers
                int j = 0;
                if (entry.getPhoneNumbers() != null && entry.getPhoneNumbers().size() > 0) {
                    SelectItem[] phones = new SelectItem[entry.getPhoneNumbers().size()];
                    for (PhoneNumber phone : entry.getPhoneNumbers()) {
                        if (phone != null) {
                            if (phone.getRel() != null) {
                                String matchNumber = phone.getRel();
                                String phoneType = matchNumber.substring(matchNumber.lastIndexOf("#") + 1, matchNumber.length());
                                phones[j] = new SelectItem(j, phone.getPhoneNumber(), phoneType);
                                if (phoneType.equals("home")) {
                                    item.getHomePhone().add(phone.getPhoneNumber());
                                }
                                if (phoneType.equals("work")) {
                                    item.getWorkPhone().add(phone.getPhoneNumber());
                                }
                                if (phoneType.equals("mobile")) {
                                    item.getMobile().add(phone.getPhoneNumber());
                                }
                                if (phoneType.equals("home_fax")) {
                                    item.getHomeFax().add(phone.getPhoneNumber());
                                }
                                if (phoneType.equals("work_fax")) {
                                    item.getWorkFax().add(phone.getPhoneNumber());
                                }
                                if (phoneType.equals("pager")) {
                                    item.getPager().add(phone.getPhoneNumber());
                                }
                                if (phoneType.equals("other")) {
                                    item.getOtherPhone().add(phone.getPhoneNumber());
                                }
                                j++;
                            } else if (phone.getLabel() != null && phone.getLabel().equals(Constants.G_EXTENSION_STR)) {
                                phones[j] = new SelectItem(j, phone.getPhoneNumber(), phone.getLabel());
                                item.getExtension().add(phone.getPhoneNumber());
                                j++;
                            }
                        }
                    }
                    item.setPhoneNumbers(phones);
                    if (item.getPhoneNumbers() != null && item.getPhoneNumbers().length > 0) {
                        item.setPrimaryPhone(item.getPhoneNumbers()[0].getName());
                    }
                }

                // set contact website
                j = 0;
                if (entry.getWebsites() != null && entry.getWebsites().size() > 0) {
                    SelectItem[] websites = new SelectItem[entry.getWebsites().size()];
                    for (Website website : entry.getWebsites()) {
                        if (website != null && website.getRel() != null) {
                            String relation = website.getRel().toString();
                            String websiteLink = website.getHref();
                            websites[j] = new SelectItem(j, website.getHref(), website.getRel().toString());
                            if (relation.equals("HOME")) {
                                item.getHomeWebSite().add(websiteLink);
                            }
                            if (relation.equals("WORK")) {
                                item.getWorkWebSite().add(websiteLink);
                            }
                            if (relation.equals("HOME_PAGE")) {
                                item.getHomePage().add(websiteLink);
                            }
                            if (relation.equals("FTP")) {
                                item.getFtp().add(websiteLink);
                            }
                            if (relation.equals("BLOG")) {
                                item.getBlog().add(websiteLink);
                            }
                            if (relation.equals("PROFILE")) {
                                item.getProfileWebSite().add(websiteLink);
                            }
                            if (relation.equals("OTHER")) {
                                item.getOtherWebSite().add(websiteLink);
                            }
                            j++;
                        }
                    }
                    item.setWebSites(websites);
                }

                // set contact Email Addresses
                j = 0;
                if (entry.getEmailAddresses() != null && entry.getEmailAddresses().size() > 0) {
                    SelectItem[] emails = new SelectItem[entry.getEmailAddresses().size()];
                    for (Email mail : entry.getEmailAddresses()) {
                        if (mail != null) {
                            String matchNumber = mail.getRel();
                            String emailAddress = mail.getAddress();
                            String mailType = "";
                            if (matchNumber != null) {
                                mailType = matchNumber.substring(matchNumber.lastIndexOf("#") + 1, matchNumber.length());
                            }
                            emails[j] = new SelectItem(j, mail.getAddress() != null ? mail.getAddress() : "", mailType);
                            if (mailType.equals("home")) {
                                item.getHomeEmail().add(emailAddress);
                            }
                            if (mailType.equals("work")) {
                                item.getWorkEmail().add(emailAddress);
                            }
                            if (mailType.equals("other") || mail.getRel() == null) {
                                item.getOtherEmail().add(emailAddress);
                            }
                            j++;
                        }
                    }
                    if (entry.getEmailAddresses() != null && entry.getEmailAddresses().size() > 0) {
                        item.setPrimaryEmail(entry.getEmailAddresses().get(0).getAddress());
                    }
                    item.setEmails(emails);
                }

                // set contact Structured Postal Addresses
                j = 0;
                if (entry.getStructuredPostalAddresses() != null && entry.getStructuredPostalAddresses().size() > 0) {
                    SelectItem[] structuredPostalAddresses = new SelectItem[entry.getStructuredPostalAddresses().size()];
                    SelectItem[] countries = new SelectItem[entry.getStructuredPostalAddresses().size()];
                    SelectItem[] cities = new SelectItem[entry.getStructuredPostalAddresses().size()];
                    SelectItem[] regions = new SelectItem[entry.getStructuredPostalAddresses().size()];
                    SelectItem[] postCods = new SelectItem[entry.getStructuredPostalAddresses().size()];
                    for (StructuredPostalAddress structuredPostalAddress : entry.getStructuredPostalAddresses()) {
                        if (structuredPostalAddress != null && structuredPostalAddress.getRel() != null) {
                            String matchType = structuredPostalAddress.getRel();
                            String addressType = matchType.substring(matchType.lastIndexOf("#") + 1, matchType.length());
                            String streetAddress = structuredPostalAddress.getStreet() != null ? structuredPostalAddress.getStreet().getValue() : "";
                            structuredPostalAddresses[j] = new SelectItem(j, streetAddress, addressType);
                            if (structuredPostalAddress.getCountry() != null) {
                                countries[j] = new SelectItem(j, structuredPostalAddress.getCountry().getValue(), addressType);
                            }
                            if (structuredPostalAddress.getCity() != null) {
                                cities[j] = new SelectItem(j, structuredPostalAddress.getCity().getValue(), addressType);
                            }
                            if (structuredPostalAddress.getRegion() != null) {
                                regions[j] = new SelectItem(j, structuredPostalAddress.getRegion().getValue(), addressType);
                            }
                            if (structuredPostalAddress.getPostcode() != null) {
                                postCods[j] = new SelectItem(j, structuredPostalAddress.getPostcode().getValue(), addressType);
                            }

                            Address address = new Address();
                            address.setAddress(streetAddress);

                            if (structuredPostalAddress.getCountry() != null) {
                                address.setCountry(structuredPostalAddress.getCountry().getValue());
                            }
                            if (structuredPostalAddress.getCity() != null) {
                                address.setCity(structuredPostalAddress.getCity().getValue());
                            }

                            if (structuredPostalAddress.getRegion() != null) {
                                address.setState(structuredPostalAddress.getRegion().getValue());
                            }

                            if (structuredPostalAddress.getPostcode() != null) {
                                address.setZipCode(structuredPostalAddress.getPostcode().getValue());
                            }

                            if (addressType.equals("home")) {
                                address.setRelationType(AddressReference.HOME.getId());
                            }
                            if (addressType.equals("work")) {
                                address.setRelationType(AddressReference.WORK.getId());
                            }
                            if (addressType.equals("other")) {
                                address.setRelationType(AddressReference.OTHER.getId());
                            }
                            item.getAddresses().add(address);

                            j++;
                        }
                    }
                    item.setAddress(structuredPostalAddresses);
                    item.setCountries(countries);
                    item.setCities(cities);
                    item.setStates(regions);
                    item.setPostCods(postCods);
                }

                // set contact Organization and Position name
                if (entry.getOrganizations() != null && entry.getOrganizations().size() > 0) {
                    Organization company = entry.getOrganizations().get(entry.getOrganizations().size() - 1);
                    if (company.getOrgName() != null && company.getOrgName().getValue() != null &&
                            !"".equals(company.getOrgName().getValue())) {
                        item.getCrmAccount().setName(company.getOrgName().getValue());

                    }
                    if (company.getOrgTitle() != null && company.getOrgTitle().getValue() != null &&
                            !"".equals(company.getOrgTitle().getValue())) {
                        item.setJobTitle(company.getOrgTitle().getValue());
                    }
                    if (company.getOrgDepartment() != null && company.getOrgDepartment().getValue() != null &&
                            !"".equals(company.getOrgDepartment().getValue())) {
                        item.setDepartment(company.getOrgDepartment().getValue());
                    }
                }

                // set contact IM Addresses
                j = 0;
                if (entry.getImAddresses() != null && entry.getImAddresses().size() > 0) {
                    SelectItem[] imAddresses = new SelectItem[entry.getImAddresses().size()];
                    for (Im address : entry.getImAddresses()) {
                        if (address != null && address.getProtocol() != null) {
                            String imAddressType = entry.getImAddresses().get(j).getProtocol() != null ? entry.getImAddresses().get(j).getProtocol() : "";
                            String addressType = imAddressType.substring(imAddressType.lastIndexOf("#") + 1, imAddressType.length());
                            String imAddress = address.getAddress();
                            imAddresses[j] = new SelectItem(j, address.getAddress(), imAddressType.substring(imAddressType.lastIndexOf("#") + 1, imAddressType.length()));
                            if (addressType.equals("GOOGLE_TALK")) {
                                item.getgTalk().add(imAddress);
                            }
                            if (addressType.equals("AIM")) {
                                item.getAIM().add(imAddress);
                            }
                            if (addressType.equals("YAHOO")) {
                                item.getYahoo().add(imAddress);
                            }
                            if (addressType.equals("SKYPE")) {
                                item.getSkype().add(imAddress);
                            }
                            if (addressType.equals("QQ")) {
                                item.getQQ().add(imAddress);
                            }
                            if (addressType.equals("MSN")) {
                                item.getMSN().add(imAddress);
                            }
                            if (addressType.equals("ICQ")) {
                                item.getICQ().add(imAddress);
                            }
                            if (addressType.equals("JABBER")) {
                                item.getJabber().add(imAddress);
                            }
                            j++;
                        }
                    }
                    item.setImAddress(imAddresses);
                }

                if (entry.getContent() != null) {
                    item.setNote(entry.getTextContent().getContent().getPlainText());
                }
                item.setContactType(1);    // Contact type : CRM Contact
                if (addToList) {
                    items.add(item);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return items.toArray(new ContactListItem[]{});
    }

    public List<Map<String, ContactEntry>> checkingContactsExistsInGoogle(ContactListItem[] wftContacts, List<ContactEntry> googleContacts, List<ContactListItem> contactItems, EdsUser user, boolean forExport) throws IOException, ServiceException {
        Map<String, ContactEntry> googleContactsMap1 = new HashMap<>();
        Map<String, ContactEntry> googleContactsMap2 = new HashMap<>();
        List<Map<String, ContactEntry>> result = new ArrayList<>();
        if (googleContacts != null && googleContacts.size() > 0) {
            for (ContactEntry entry : googleContacts) {
                googleContactsMap1.put(entry.getId(), entry);
                StringBuilder info = new StringBuilder();
                if (entry.getTitle() != null && !"".equals(entry.getTitle()) && entry.getTitle().getPlainText() != null && !"".equals(entry.getTitle().getPlainText())) {
                    info.append(entry.getTitle().getPlainText().replace(" ", ""));
                }
                if (entry.getEmailAddresses() != null && entry.getEmailAddresses().size() > 0 && !"".equals(entry.getEmailAddresses().get(0).getAddress())) {
                    info.append(entry.getEmailAddresses().get(0).getAddress());
                }
                if (info != null && !"".equals(info.toString())) {
                    Integer hash = info.toString().hashCode();
                    if (!googleContactsMap2.containsKey(hash.toString())) {
                        googleContactsMap2.put(hash.toString(), entry);
                    }
                }
            }
        }

        for (ContactListItem item : wftContacts) {
            if (item.getGoogleId() != null && !"".equals(item.getGoogleId())) {
                if (!googleContactsMap1.containsKey(item.getGoogleId())) {
                    if (!forExport) {
                        deleteContact(Collections.singletonList(item.getObjectId()), user.getObjectID());
                    }
                } else {
                    if (!forExport && item.getUpdatedDate() != null && googleContactsMap1.get(item.getGoogleId()).getUpdated() != null &&
                            item.getUpdatedDate().getTime() > googleContactsMap1.get(item.getGoogleId()).getUpdated().getValue()) {
                        contactItems.add(item);
                    }
                }
            } else {
                StringBuilder fullName = new StringBuilder();
                if (item.getFirstName() != null && !"".equals(item.getFirstName())) {
                    fullName.append(item.getFirstName().trim());
                }
                if (item.getLastName() != null && !"".equals(item.getLastName())) {
                    fullName.append(item.getLastName().trim());
                }
                if (item.getPrimaryEmail() != null && !"".equals(item.getPrimaryEmail())) {
                    fullName.append(item.getPrimaryEmail().trim());
                }
                if (fullName != null && !"".equals(fullName.toString())) {
                    Integer hash = fullName.toString().replace(" ", "").hashCode();
                    if (!googleContactsMap2.containsKey(hash.toString())) {
                        contactItems.add(item);
                    } else {
                        if (!forExport && item.getUpdatedDate() != null && googleContactsMap2.get(hash.toString()).getUpdated() != null &&
                                item.getUpdatedDate().getTime() > googleContactsMap2.get(hash.toString()).getUpdated().getValue()) {
                            contactItems.add(item);
                        }
                    }
                }
            }
        }
        result.add(googleContactsMap1);
        result.add(googleContactsMap2);
        return result;
    }

    public List<Integer> deleteContact(List<Integer> contactIDs, Integer userId) {
        Set<Integer> ids = new HashSet<>();
        EdsCrmContact firstContact = null;
        boolean firstContactSet = false;
        EdsUser user = userManager.getUser();
        if (user == null && userId != null) {
            user = userManager.get(userId);
        }
        EdsCrmContact contact = null;
        for (Integer contactID : contactIDs) {
            contact = crmContactManager.get(contactID);
            if (contact != null) {
                if (contact.is(EdsCrmContact.CANDIDATE) && !ServerUtils.hasPermission(PermissionConstants.HRMS_DELETE_CANDIDATE, rolePermissionServiceLocal.checkForArtificateRoles(contactID))) {
                    continue;
                }
                if (!firstContactSet && contact != null) {
                    firstContact = contact;
                    firstContactSet = true;
                }
                boolean isAdmin = user.hasRole(roleManager.get(EdsRole.ADMIN));
                Integer ownerID = contact.getOwner() != null ? contact.getOwner().getObjectID() : null;
                boolean leadAndCanDelete = contact.is(EdsCrmContact.LEAD_CONTACT) && (isAdmin || user.hasRole(roleManager.get(EdsRole.SALESMAN)) || user.getObjectID().equals(ownerID));
                boolean candidateAndCanBeDeleted = contact.is(EdsCrmContact.CANDIDATE) && (isAdmin || user.hasRole(roleManager.get(EdsRole.HR)) || user.getObjectID().equals(ownerID));
                boolean canDeleteHasPermission = ServerUtils.hasPermission(PermissionConstants.CRM_LEAD_DELETE, user);
                if (contact != null && (canDeleteHasPermission || leadAndCanDelete || candidateAndCanBeDeleted || ((isAdmin || (crmContactManager.canDelete(contact, user))) && !contact.is(EdsCrmContact.EMPLOYEE_CONTACT) && !contact.is(EdsCrmContact.STUDENT_CONTACT)))) {
                    if (candidateAndCanBeDeleted) {
                        contact.getVacancies().clear();
                    }
                    Integer inactiveID = referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_INACTIVE).getObjectID();
                    crmContactManager.deleteContact(contactID, inactiveID);
                    ids.add(contactID);

                    List<Integer> recurrenceAlertIds = workflowRuleManager.getRecurrenceAlertIdsByContactId(contactID);
                    if (recurrenceAlertIds != null && recurrenceAlertIds.size() > 0) {
                        for (Integer recurrenceId : recurrenceAlertIds) {
                            if (recurrenceId != null) {
                                recurrenceManager.nativelyRemoveRecurrence(recurrenceId);
                            }
                        }
                    }
                    baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, contact, user);
                }
            }
        }
        if (contactIDs.size() == 1) {
            if (contact.is(EdsCrmContact.LEAD_CONTACT)) {
                baseEventPostProcessor.registerEvent(CrmLeadEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, contact, user);
            }
        }
        if (ids.size() > 0 && firstContact != null && user != null) {
            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(CrmContactCustomEventListenerImpl.TYPE, CrmContactCustomEventListenerImpl.EVENT_DELETE_CRM_CONTACT_FROM_SOLR_BATCH, firstContact, user);
            event.setCustomStringField(ServerUtils.getAsCommoDelimited(new ArrayList<>(ids), "0"));
        }
        return new ArrayList<>(ids);
    }


    /**
     * This method used for export WFT contacts to Google Contacts
     *
     * @param contactItems - WFT contact items array
     */
    public void exportWFTContactsToGoogleContacts(ContactListItem[] contactItems, boolean forExport) throws GeneralSecurityException, IOException, ServiceException {
        EdsUser user = getUser();
        ContactsService myService = getLoggedService(user);
        List<ContactEntry> googleEntries = getContactFeed(myService).getEntries();
        exportWFTContactsToGoogleContacts(googleEntries, contactItems, user, forExport);
    }

    public void exportWFTContactsToGoogleContacts(List<ContactEntry> googleContacts, ContactListItem[] wftContacts, EdsUser user, boolean forExport) throws GeneralSecurityException, IOException, ServiceException {
        ContactsService myService = getLoggedService(user);
        List<ContactListItem> wftContactItems = new ArrayList<>();
        List<Map<String, ContactEntry>> googleContactsMap = checkingContactsExistsInGoogle(wftContacts, googleContacts, wftContactItems, user, forExport);
        boolean contactEntryExists = false;
        if (myService != null && wftContactItems != null && wftContactItems.size() > 0) {
            for (ContactListItem contactItem : wftContactItems) {
                if (contactItem.getOwnerId() != null && contactItem.getOwnerId().equals(user.getObjectID())) {
                    try {
                        StringBuilder fullName = new StringBuilder();
                        if (contactItem.getFirstName() != null && !"".equals(contactItem.getFirstName())) {
                            fullName.append(contactItem.getFirstName().trim());
                        }
                        if (contactItem.getLastName() != null && !"".equals(contactItem.getLastName())) {
                            fullName.append(contactItem.getLastName().trim());
                        }
                        if (contactItem.getPrimaryEmail() != null && !"".equals(contactItem.getPrimaryEmail())) {
                            fullName.append(contactItem.getPrimaryEmail().trim());
                        }
                        ContactEntry contactEntry = null;
                        if (contactItem.getGoogleId() != null && !"".equals(contactItem.getGoogleId())) {
                            contactEntry = googleContactsMap.get(0).get(contactItem.getGoogleId());
                        } else if (fullName != null && !"".equals(fullName.toString())) {
                            Integer hash = fullName.toString().replace(" ", "").hashCode();
                            contactEntry = googleContactsMap.get(1).get(hash.toString());
                        }

                        URL postUrl = null;
                        try {
                            postUrl = new URL("https://www.google.com/m8/feeds/contacts/" + getGoogleContact(user, true).getGoogleID() + "/full");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            continue;
                        }
                        if (contactEntry == null) {
                            contactEntry = new ContactEntry();
                            contactEntryExists = false;
                        } else {
                            contactEntryExists = true;
                        }
                        // set Contact fullName ------------------------------------------------------------------------------------
                        Name name = new Name();
                        TextConstruct title = null;
                        if (contactItem.getContactName() != null) {
                            FullName contactFullName = new FullName(contactItem.getContactName(), null);
                            name.setFullName(contactFullName);
                            title = new PlainTextConstruct(contactItem.getContactName());
                            contactEntry.setTitle(title);
                        }

                        if (contactItem.getTitle() != null && !contactItem.getTitle().equals("")) {
                            name.setNamePrefix(new NamePrefix(contactItem.getTitle()));
                        }
                        if (contactItem.getOtherName() != null && !contactItem.getOtherName().equals("")) {
                            contactEntry.setNickname(new Nickname(contactItem.getOtherName()));
                        }
                        contactEntry.setName(name);
                        // set contact company -------------------------------------------------------------------------------------
                        if (contactEntry.getOrganizations() != null && contactEntry.getOrganizations().size() > 0 && contactEntry.getOrganizations().get(0) != null) {
                            if (contactItem.getCrmAccount().getName() != null && !"".equals(contactItem.getCrmAccount().getName())) {
                                OrgName orgName = new OrgName(contactItem.getCrmAccount().getName());
                                contactEntry.getOrganizations().get(0).setOrgName(orgName);
                            }
                            if (contactItem.getJobTitle() != null && !"".equals(contactItem.getJobTitle())) {
                                OrgTitle orgTitle = new OrgTitle(contactItem.getJobTitle());
                                contactEntry.getOrganizations().get(0).setOrgTitle(orgTitle);
                            }
                            if (contactItem.getDepartment() != null && !"".equals(contactItem.getDepartment())) {
                                OrgDepartment orgDepartment = new OrgDepartment();
                                contactEntry.getOrganizations().get(0).setOrgDepartment(orgDepartment);
                            }
                            if (contactEntry.getOrganizations().get(0).getRel() == null) {
                                contactEntry.getOrganizations().get(0).setRel(Organization.Rel.WORK);
                            }
                        } else {
                            Organization organization = new Organization();
                            if (contactItem.getCrmAccount().getName() != null && !"".equals(contactItem.getCrmAccount().getName())) {
                                OrgName orgName = new OrgName(contactItem.getCrmAccount().getName());
                                organization.setOrgName(orgName);
                            }
                            if (contactItem.getJobTitle() != null && !"".equals(contactItem.getJobTitle())) {
                                OrgTitle orgTitle = new OrgTitle(contactItem.getJobTitle());
                                organization.setOrgTitle(orgTitle);
                            }
                            if (contactItem.getDepartment() != null && !contactItem.getDepartment().equals("")) {
                                OrgDepartment orgDepartment = new OrgDepartment();
                                organization.setOrgDepartment(orgDepartment);
                            }
                            organization.setRel(Organization.Rel.WORK);
                            contactEntry.addOrganization(organization);
                        }

                        // set contact birthday ------------------------------------------------------------------------------------
                        if (contactItem.getBirthDate() != null) {
                            Birthday birthDay = new Birthday();
                            Date birthDate = contactItem.getBirthDate().getNonConvertedDate();
                            String month = "01";
                            if ((birthDate.getMonth()) < 9) {
                                month = "0" + (birthDate.getMonth() + 1);
                            } else {
                                month = Integer.toString(birthDate.getMonth() + 1);
                            }
                            String date = "01";
                            if ((birthDate.getDate()) <= 9) {
                                date = "0" + birthDate.getDate();
                            } else {
                                date = Integer.toString(birthDate.getDate());
                            }
                            String bDate = (birthDate.getYear() + 1900) + "-" + month + "-" + date;
                            birthDay.setWhen(bDate);
                            contactEntry.setBirthday(birthDay);
                        }

                        // set contact phone numbers
                        setContactPhonesToGoogle(contactItem, contactEntry);
                        // set contact websites
                        setContactWebSitesToGoogle(contactItem, contactEntry);
                        // set contact email addresses
                        setContactEmailsToGoogle(contactItem, contactEntry);
                        // set contact structured postal addresses
                        setContactAddressesToGoogle(contactItem, contactEntry);
                        // set contact IM addresses
                        setContactIMAddressesToGoogle(contactItem, contactEntry);

                        // set contact Notes -----------------------------------------------------------------------------------
                        String[] notes = null;
                        StringBuilder strBuffer = new StringBuilder();
                        if (contactEntry.getContent() != null && contactEntry.getTextContent().getContent() != null && contactEntry.getTextContent().getContent().getPlainText() != null) {
                            notes = contactEntry.getTextContent().getContent().getPlainText().split("\n");
                            strBuffer.append(contactEntry.getTextContent().getContent().getPlainText() + "\n");
                        }
                        List<String> contactNotes = new ArrayList<>();
                        if (notes != null && notes.length > 0) {
                            contactNotes.addAll(Arrays.asList(notes));
                        }

                        HistoryList historyList = contactItem.getHistory();
                        if (historyList != null) {
                            HistoryListItem[] items = historyList.getResult();
                            if (items != null) {
                                if (items != null && items.length > 0) {
                                    for (HistoryListItem note : items) {
                                        if (!contactNotes.contains(note.getComment()) && !"".equals(note.getComment())) {
                                            strBuffer.append(note.getComment() + "\n");
                                        }
                                    }
                                }

                                if (strBuffer != null && !"".equals(strBuffer.toString())) {
                                    contactEntry.setContent(new PlainTextConstruct(strBuffer.toString()));
                                }
                            }
                        }
                        // check has contact in google contacts : if has contact in google, then update this contact in google, else export contact to google
                        if (contactEntryExists) {
                            if (contactItem.getUpdatedDate().after(new Date(contactEntry.getUpdated().getValue()))) {
                                URL editUrl = new URL(contactEntry.getEditLink().getHref());
                                try {
                                    myService.update(editUrl, contactEntry);
                                } catch (IOException | ServiceException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            try {
                                myService.insert(postUrl, contactEntry);
                            } catch (IOException | ServiceException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void setContactIMAddressesToGoogle(ContactListItem contactListItem, ContactEntry contact) {
        List<EdsCrmContactItemParams> imAddresses = contactItemParamsManager.getContactParams(contactListItem.getObjectId(), CONTACT_IMADDRESSES);
        if (imAddresses != null && imAddresses.size() > 0) {
            // if has google contact's IM addresses, first remove IM addresses, then add IM addresses to this google contact
            contact.getImAddresses().removeAll(contact.getImAddresses());
            for (EdsCrmContactItemParams imAddress1 : imAddresses) {
                Im imAddress = new Im();
                if (imAddress1 != null && !imAddress1.getValue().equals("") && imAddress1.getRelation() != null) {
                    imAddress.setAddress(imAddress1.getValue());
                    imAddress.setLabel(imAddress1.getValue());
                    switch (imAddress1.getRelation()) {
                        case G_GOOGLE_TALK -> imAddress.setProtocol(Im.Protocol.GOOGLE_TALK);
                        case G_AIM -> imAddress.setProtocol(Im.Protocol.AIM);
                        case G_YAHOO -> imAddress.setProtocol(Im.Protocol.YAHOO);
                        case G_SKYPE -> imAddress.setProtocol(Im.Protocol.SKYPE);
                        case G_QQ -> imAddress.setProtocol(Im.Protocol.QQ);
                        case G_MSN -> imAddress.setProtocol(Im.Protocol.MSN);
                        case G_ICQ -> imAddress.setProtocol(Im.Protocol.ICQ);
                        case G_JABBER -> imAddress.setProtocol(Im.Protocol.JABBER);
                    }
                    contact.addImAddress(imAddress);
                }
            }
        }
    }

    private void setContactAddressesToGoogle(ContactListItem contactListItem, ContactEntry contact) {
        List<EdsAddress> addresses = addressManager.getContactAddresses(contactListItem.getObjectId());
        if (addresses != null && addresses.size() > 0) {
            // if has google contact's Postal Addresses, first remove Postal Addresses, then add Postal Addresses to this google contact
            contact.getStructuredPostalAddresses().removeAll(contact.getStructuredPostalAddresses());
            for (EdsAddress address1 : addresses) {
                StructuredPostalAddress address = new StructuredPostalAddress();
                if (address1.getCountry() != null) {
                    Country country = new Country(address1.getCountry().getCode(), address1.getCountry().getName());
                    address.setCountry(country);
                }
                if (address1.getCity() != null && !address1.getCity().equals("")) {
                    City city = new City(address1.getCity());
                    address.setCity(city);
                }
                if (address1.getState() != null) {
                    Region region = new Region(address1.getState().getName());
                    address.setRegion(region);
                }
                if (address1.getZipCode() != null) {
                    PostCode postCode = new PostCode(address1.getZipCode());
                    address.setPostcode(postCode);
                }

                if (address1.getAddress() != null && !address1.getAddress().equals("") && address1.getRelationType() != null) {
                    FormattedAddress formattedAddress = new FormattedAddress();
                    formattedAddress.setValue(address1.getAddress());
                    address.setFormattedAddress(formattedAddress);
                    if (address1.getRelationType() == AddressReference.HOME.getId()) {
                        address.setRel(StructuredPostalAddress.Rel.HOME);
                    } else if (address1.getRelationType() == AddressReference.WORK.getId()) {
                        address.setRel(StructuredPostalAddress.Rel.WORK);
                    } else if (address1.getRelationType() == AddressReference.OTHER.getId()) {
                        address.setRel(StructuredPostalAddress.Rel.OTHER);
                    } else {
                        address.setRel(StructuredPostalAddress.Rel.WORK);
                    }
                    contact.addStructuredPostalAddress(address);
                }
            }
        }
    }

    private void setContactEmailsToGoogle(ContactListItem contactListItem, ContactEntry contact) {
        List<EdsCrmContactItemParams> emails = contactItemParamsManager.getContactParams(contactListItem.getObjectId(), CONTACT_EMAILS);
        if (emails != null && emails.size() > 0) {
            // if has google contact's Email Addresses, first remove Email Addresses, then add Email Addresses to this google contact
            contact.getEmailAddresses().removeAll(contact.getEmailAddresses());
            for (EdsCrmContactItemParams email : emails) {
                Email mail = new Email();
                if (email.getValue() != null && !email.getValue().equals("") && email.getRelation() != null) {
                    mail.setAddress(email.getValue());
                    switch (email.getRelation()) {
                        case G_HOME -> mail.setRel(Email.Rel.HOME);
                        case G_WORK -> mail.setRel(Email.Rel.WORK);
                        case G_OTHER -> mail.setRel(Email.Rel.OTHER);
                    }
                    contact.addEmailAddress(mail);
                }
            }
        }
    }

    private void setContactPhonesToGoogle(ContactListItem contactListItem, ContactEntry contact) {
        if (contactListItem.getObjectId() != null) {
            List<EdsCrmContactItemParams> phones = contactItemParamsManager.getContactParams(contactListItem.getObjectId(), CONTACT_PHONES);
            if (phones != null && phones.size() > 0) {
                // if has google contact's phone number, first remove phones, then add phones to this google contact
                contact.getPhoneNumbers().removeAll(contact.getPhoneNumbers());
                for (EdsCrmContactItemParams phone : phones) {
                    PhoneNumber phoneNumber = new PhoneNumber();
                    String number = phone.getValue() != null && !"".equals(phone.getValue().trim()) ? phone.getValue().trim() : null;
                    if (phone.getRelation() != null && number != null) {
                        phoneNumber.setPhoneNumber(number);
                        switch (phone.getRelation()) {
                            case G_HOME -> phoneNumber.setRel(PhoneNumber.Rel.HOME);
                            case G_WORK -> phoneNumber.setRel(PhoneNumber.Rel.WORK);
                            case G_MOBILE -> phoneNumber.setRel(PhoneNumber.Rel.MOBILE);
                            case G_HOME_FAX -> phoneNumber.setRel(PhoneNumber.Rel.HOME_FAX);
                            case G_WORK_FAX -> phoneNumber.setRel(PhoneNumber.Rel.WORK_FAX);
                            case G_PAGER -> phoneNumber.setRel(PhoneNumber.Rel.PAGER);
                            case G_OTHER -> phoneNumber.setRel(PhoneNumber.Rel.OTHER);
                        }
                        contact.addPhoneNumber(phoneNumber);
                    }
                }
            }
        }
    }

    private void setContactWebSitesToGoogle(ContactListItem contactListItem, ContactEntry contact) {
        if (contactListItem.getObjectId() != null) {
            List<EdsCrmContactItemParams> webSites = contactItemParamsManager.getContactParams(contactListItem.getObjectId(), CONTACT_WEBSITES);
            if (webSites != null && webSites.size() > 0) {
                // if has google contact's web sites, first remove web sites, then add web sites to this google contact
                contact.getWebsites().removeAll(contact.getWebsites());
                for (EdsCrmContactItemParams webSite : webSites) {
                    if (webSite.getValue() != null && !webSite.getValue().equals("") && webSite.getRelation() != null) {
                        Website website = new Website();
                        website.setHref(webSite.getValue());
                        switch (webSite.getRelation()) {
                            case G_HOME -> website.setRel(Website.Rel.HOME);
                            case G_WORK -> website.setRel(Website.Rel.WORK);
                            case G_HOME_PAGE -> website.setRel(Website.Rel.HOME_PAGE);
                            case G_FTP -> website.setRel(Website.Rel.FTP);
                            case G_BLOG -> website.setRel(Website.Rel.BLOG);
                            case G_PROFILE -> website.setRel(Website.Rel.PROFILE);
                            case G_OTHER -> website.setRel(Website.Rel.OTHER);
                        }
                        contact.addWebsite(website);
                    }
                }
            }
        }
    }
}
