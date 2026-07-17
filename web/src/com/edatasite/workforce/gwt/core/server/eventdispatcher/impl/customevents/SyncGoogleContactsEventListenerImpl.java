package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.EdsGoogleWFTGroups;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaOperations;
import com.edatasite.workforce.gwt.core.server.db.ContactCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleContactsManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.googlegroups.GoogleGroupsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.GoogleContactsManagerImpl;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365BaseList;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Contact;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365EmailAddress;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365PhysicalAddress;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365AuthService;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365ContactService;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.mail.EdsSubjects;
import com.edatasite.workforce.mail.EdsTemplates;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.contacts.Birthday;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactGroupEntry;
import com.google.gdata.data.contacts.GroupMembershipInfo;
import com.google.gdata.data.contacts.Nickname;
import com.google.gdata.data.contacts.Website;
import com.google.gdata.data.extensions.AdditionalName;
import com.google.gdata.data.extensions.City;
import com.google.gdata.data.extensions.Country;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.FormattedAddress;
import com.google.gdata.data.extensions.FullName;
import com.google.gdata.data.extensions.GivenName;
import com.google.gdata.data.extensions.Im;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.data.extensions.NamePrefix;
import com.google.gdata.data.extensions.OrgName;
import com.google.gdata.data.extensions.OrgTitle;
import com.google.gdata.data.extensions.Organization;
import com.google.gdata.data.extensions.PhoneNumber;
import com.google.gdata.data.extensions.PostCode;
import com.google.gdata.data.extensions.Region;
import com.google.gdata.data.extensions.Street;
import com.google.gdata.data.extensions.StructuredPostalAddress;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Sep 8, 2010
 * Time: 1:54:46 AM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class SyncGoogleContactsEventListenerImpl extends CustomBusinessEventListenerAdapter implements CommandConstants, Constants {
    public static WfmType<EdsUser> TYPE = new WfmType<>(EventTypes.syncGoogleContactsEventListener);
    public static final String EVENT_SYNC_CONTACT = "SYNC_CONTACT";
    public static final String EVENT_SYNC_CONTACT_OFFICE = "SYNC_CONTACT_OFFICE";

    @Autowired
    private UserManager userManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ContactCategoryManager contactCategoryManager;
    @Autowired
    private GoogleContactsManager googleContactsManager;
    @Autowired
    private WfmJpaOperations jpaTemplate;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private GoogleGroupsManager googleGroupsManager;
    @Autowired
    private Office365AuthService office365AuthService;
    @Autowired
    private Office365ContactService office365ContactService;
    @Autowired
    ContactServiceLocal contactServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private ContactSolrComponent contactSolrComponent;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_SYNC_CONTACT.equals(event.getEventType()) || EVENT_SYNC_CONTACT_OFFICE.equals(event.getEventType())) {
            EntityManager em = jpaTemplate.getHibernateEntityManager();
            try (org.hibernate.Session session = em.unwrap(Session.class)) {
                if (session.getTransaction() != null && !session.getTransaction().isActive()) {
                    session.beginTransaction();
                }
                EdsBusinessEvent e = (EdsBusinessEvent) session.get(EdsBusinessEvent.class, event.getObjectID());
                e.setStatus(RUNNING);
                session.flush();

                onSyncServerContacts(event);
            } finally {
                em.close();
            }
        }
    }

    private void onSyncServerContacts(EdsBusinessEvent event) {
        System.out.println("Contact Sync Boshlandi " + event.getObjectID() + "  <==> userID = " + event.getEntityID());

        ServerSecurityContext.getInstance().setStaticUserID(event.getEntityID());
        EdsUser user = userManager.get(event.getEntityID());



        boolean exportContactPhoneWithOutPlus = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.EXPORT_CONTACT_PHONE_WITHOUT_PLUS);

        if (EVENT_SYNC_CONTACT.equals(event.getEventType())) {
            List<EdsCrmContact> wftContactList = crmContactManager.getByMyOwnCrmContacts(user);//.list(new ListingFilterParameter(), user);

            System.out.println("Contact Sync crmContactManager.list() finished at " + (new Date()).toString());
            //GOOGLE SYNC
            googleContactSync(user, wftContactList, exportContactPhoneWithOutPlus);
        } else if (EVENT_SYNC_CONTACT_OFFICE.equals(event.getEventType())) {
            //OFFICE 365 SYNC
            office365ContactSync(user, exportContactPhoneWithOutPlus);
        }

        try {
            Map<String, Object> values = new HashMap<>();
            SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateInstance();
            if (user.getCompany().getCompanySettings() != null) {
                formatter.applyPattern(user.getCompany().getCompanySettings().getLongDateFormat());
            } else {
                formatter.applyPattern("dddd, dd MMMM yyyy h:mm tt");
            }
            values.put("HOST", EdsContextParams.getHost());
            values.put("productName", EdsContextParams.getProductName());
            values.put("username", user.getFullName());
            values.put("time", formatter.format(user.getUserDate()));
            sendConfirmation(values, user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
        event.setStatus(EventStatus.COMPLETED.name());
        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void googleContactSync(EdsUser user, List<EdsCrmContact> wftContactList, boolean exportContactPhoneWithOutPlus) {
        try {
            ContactsService myService = googleContactsManager.getLoggedService(user);
            if (myService != null) {
                List<ContactEntry> googleContacts = googleContactsManager.getContactFeed(myService).getEntries();

                ContactListItem[] serverContactItems = googleContactsManager.getGoogleContactItems(googleContacts, user);

                System.out.println("Google Contact Sync googleContactsManager.getGoogleContactItems(googleContacts, user) finished at " + (new Date()).toString());
                try {
                    if (serverContactItems != null && serverContactItems.length > 0) {
                        System.out.println("Import from GOOGLE has been started at " + (new Date()).toString());
                        importContactsFromThirdParty(wftContactList, serverContactItems, GOOGLE, user);
                        System.out.println("Import from GOOGLE finished at " + (new Date()).toString());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                try {
                    if (wftContactList != null && wftContactList.size() > 0) {
                        System.out.println("Export from THE SYSTEM has been started at " + (new Date()).toString());
                        exportWFTContactsToGoogleContacts(googleContacts, wftContactList, user, exportContactPhoneWithOutPlus);
                        System.out.println("Export from THE SYSTEM finished at " + (new Date()).toString());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void office365ContactSync(EdsUser user, boolean exportContactPhoneWithOutPlus) {
        try {
            Office365AccessTokenDTO tokenDTO = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), OFFICE_365);

            List<EdsGoogleWFTGroups> folderMappings = googleGroupsManager.getGroupSettings(userManager.getUser(), true);
            HashMap<String, Integer> mappedCategories = new HashMap<>();

            if (folderMappings != null && !folderMappings.isEmpty()) {
                for (EdsGoogleWFTGroups folder : folderMappings) {
                    if (folder.getWftGroupID() != null && StringUtils.isNotBlank(folder.getGoogleGroupID())) {
                        mappedCategories.put(folder.getGoogleGroupID(), folder.getWftGroupID());
                    }
                }
            }

            if (tokenDTO != null) {
                //SYNC Contacts in default category
                EdsContactCategory defaultCategory = contactCategoryManager.getDefaultCategoryByContactType(ContactListItem.CRM_CONTACT);

                office365ContactFolderSync(tokenDTO, (defaultCategory!=null ? defaultCategory.getObjectID() : null), null, user, exportContactPhoneWithOutPlus);
                //THEN EACH MAPPED FOLDER
                for (Map.Entry<String, Integer> mappedFolder : mappedCategories.entrySet()) {
                    office365ContactFolderSync(tokenDTO, mappedFolder.getValue(), mappedFolder.getKey(), user, exportContactPhoneWithOutPlus);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void office365ContactFolderSync(Office365AccessTokenDTO tokenDTO, Integer kpiContactCategoryId, String officeFolderId, EdsUser user, boolean exportContactPhoneWithOutPlus) {


        System.out.println("Office365 Contact Sync googleContactsManager.convertOffice365ToContactItems(office365Contacts, user) finished at " + (new Date()).toString());
        try {
            List<EdsCrmContact> kpiContactList = crmContactManager.getMyContactsByFolderId(user, kpiContactCategoryId);
            //Retrieve Contacts from Microsoft
            Office365BaseList<Office365Contact> office365Contacts = office365ContactService.getContactCollection(tokenDTO, officeFolderId);
            //Convert Office365 Contacts to internal KPI type
            ContactListItem[] office365ContactItems = googleContactsManager.convertOffice365(tokenDTO, office365Contacts, kpiContactCategoryId, user);

            if (office365ContactItems != null && office365ContactItems.length > 0) {
                System.out.println("Import from Office365 has been started at " + (new Date()).toString());
                //IMPORT CONTACTS FROM OFFICE 365
                importContactsFromThirdParty(kpiContactList, office365ContactItems, OFFICE_365, user);

                System.out.println("Import from Office365 finished at " + (new Date()).toString());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            List<EdsCrmContact> kpiContactList = crmContactManager.getMyContactsByFolderId(user, kpiContactCategoryId);
            //Retrieve Contacts from Microsoft
            Office365BaseList<Office365Contact> office365Contacts = office365ContactService.getContactCollection(tokenDTO, officeFolderId);
            //Convert Office365 Contacts to internal KPI type
            ContactListItem[] office365ContactItems = googleContactsManager.convertOffice365(tokenDTO, office365Contacts, kpiContactCategoryId, user);

            if (kpiContactList != null && !kpiContactList.isEmpty()) {
                System.out.println("Export from THE SYSTEM has been started at " + (new Date()).toString());
                //EXPORT CONTACTS FROM OFFICE 365
                exportWFTContactsToOffice365Contacts(officeFolderId, office365Contacts, kpiContactList, user, exportContactPhoneWithOutPlus);

                System.out.println("Export from THE SYSTEM finished at " + (new Date()).toString());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void exportWFTContactsToOffice365Contacts(String office365FolderId, Office365BaseList<Office365Contact> office365Contacts, List<EdsCrmContact> wftContactList, EdsUser user, boolean exportContactPhoneWithOutPlus) {
        //Create GOOGLE Service
        boolean isKpiMaster = false;
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        if (userSettings.getOfficeContactSyncType() != null && SERVERMASTER.equals(userSettings.getOfficeContactSyncType())) {
            return;
        } else if (userSettings.getOfficeContactSyncType() != null && KPIMASTER.equals(userSettings.getOfficeContactSyncType())) {
            isKpiMaster = true;
        }

        Office365AccessTokenDTO tokenDTO = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), OFFICE_365);

        List<EdsCrmContact> wftContactItems = new ArrayList<>();
        List<Map<String, Office365Contact>> googleContactsMap = checkingContactsExistsInOffice365(wftContactList, office365Contacts, wftContactItems, user, isKpiMaster);
        boolean contactEntryExists = false;

        if (tokenDTO != null && wftContactItems != null && !wftContactItems.isEmpty()) {

            int flushLimit = 50;
            int flushingCount = 0;
            int contactsCount = 0;
            EntityManager em = jpaTemplate.getHibernateEntityManager();
            try (org.hibernate.Session session = em.unwrap(Session.class)) {
                if (session.getTransaction() != null && !session.getTransaction().isActive()) {
                    session.beginTransaction();
                }
                for (EdsCrmContact wftItem : wftContactItems) {
                    if (wftItem.getOwner().getObjectID() != null && wftItem.getOwner().getObjectID().equals(user.getObjectID()) && contactsCount <= CONTACTS_LIMIT) {
                        contactsCount++;
                        EdsCrmContact contactItem = (EdsCrmContact) session.get(EdsCrmContact.class, wftItem.getObjectID());
                        flushingCount++;
                        StringBuilder fullName = new StringBuilder();
                        if (contactItem.getFirstName() != null && !"".equals(contactItem.getFirstName())) {
                            fullName.append(contactItem.getFirstName().trim());
                        }
                        if (contactItem.getMiddleName() != null && !"".equals(contactItem.getMiddleName())) {
                            fullName.append(contactItem.getMiddleName().trim());
                        }
                        if (contactItem.getLastName() != null && !"".equals(contactItem.getLastName())) {
                            fullName.append(contactItem.getLastName().trim());
                        }

                        if (contactItem.getPrimaryEmailFromAll() != null && !"".equals(contactItem.getPrimaryEmailFromAll())) {
                            fullName.append(contactItem.getPrimaryEmailFromAll().trim());
                        }
                        if (contactItem.getPrimaryEmail() != null && !"".equals(contactItem.getPrimaryEmail())) {
                            fullName.append(contactItem.getPrimaryEmail().trim());
                        }
                        Office365Contact contactEntry = null;
                        if (StringUtils.isNotBlank(contactItem.getGoogleId())) {
                            contactEntry = googleContactsMap.get(0).get(contactItem.getGoogleId());
                        } else if (fullName != null && !"".equals(fullName.toString())) {
                            Integer hash = fullName.toString().replace(" ", "").hashCode();
                            contactEntry = googleContactsMap.get(1).get(hash.toString());
                        }

                        if (contactEntry == null) {
                            contactEntry = new Office365Contact();
                            contactEntryExists = false;
                        } else {
                            contactEntryExists = true;
                        }

                        contactEntry.setParentFolderId(office365FolderId);
                        contactEntry.setTitle(contactItem.getTitle());
                        contactEntry.setNickName(contactItem.getOtherName());
                        contactEntry.setGivenName(contactItem.getFirstName());
                        contactEntry.setSurname(contactItem.getLastName());
                        contactEntry.setMiddleName(contactItem.getMiddleName());
                        if (contactItem.getCrmAccount() != null) {
                            EdsCrmAccount a = (EdsCrmAccount) session.get(EdsCrmAccount.class, contactItem.getCrmAccount().getObjectID());
                            if (a != null && a.getName() != null && !"".equals(a.getName())) {
                                contactEntry.setCompanyName(contactItem.getCrmAccount().getName());
                            }
                        }
                        contactEntry.setJobTitle(contactItem.getJobTitles());
                        contactEntry.setDepartment(contactItem.getDepartment());

                        Date birthDate = null;
                        if (contactItem.getDateOfBirth() != null) {
                            birthDate = new DateNonConvertable(contactItem.getDateOfBirth()).getNonConvertedDate();
                            contactEntry.setBirthday(birthDate);
                        }
                        contactEntry.setPersonalNotes(contactItem.getNote());


                        setContactPhonesToOffice365(contactItem, contactEntry, exportContactPhoneWithOutPlus, session);
                        flushingCount++;
                        setContactEmailsToOffice365(contactItem, contactEntry, session);
                        flushingCount++;
                        setContactAddressesToOffice(contactItem, contactEntry, session);
                        flushingCount++;
                        setContactIMAddressesToOffice(contactItem, contactEntry, session);
                        flushingCount++;

                        if (contactEntryExists) {
                            Office365Contact oficeContact = office365ContactService.updateAContact(tokenDTO, contactEntry, office365FolderId);
                            contactItem.setGoogleId(oficeContact.getId());
                            contactItem.getAuditInfo().setModificationDate(oficeContact.getDateTimeLastModified());
                        } else {
                            Office365Contact oficeContact = office365ContactService.createAContact(tokenDTO, contactEntry, office365FolderId);
                            if (oficeContact != null) {
                                contactItem.setGoogleId(oficeContact.getId());
                                contactItem.getAuditInfo().setModificationDate(oficeContact.getDateTimeLastModified());
                                try {
                                    contactSolrComponent.index(contactItem);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    if (flushingCount > flushLimit) {
                        session.flush();
                        session.clear();
                        flushingCount = 0;
                    }
                }
            } finally {
                em.close();
            }
        }
    }

    private void setContactIMAddressesToOffice(EdsCrmContact contactListItem, Office365Contact contactEntry, Session session) {
        if (contactEntry.getImAddresses() != null) {
            contactEntry.getImAddresses().clear();
        }
        List<EdsCrmContactItemParams> imAddresses = getContactParams(contactListItem.getObjectID(), GoogleContactsManagerImpl.CONTACT_IMADDRESSES, session);
        if (imAddresses != null && !imAddresses.isEmpty()) {
            ArrayList<String> iimadress = new ArrayList<>();
            for (EdsCrmContactItemParams imAddress1 : imAddresses) {
                if (imAddress1 != null && !imAddress1.getValue().equals("") && imAddress1.getRelation() != null) {
                    iimadress.add(imAddress1.getValue());
                }
            }
            contactEntry.setImAddresses(iimadress);
        }
    }

    private void setContactAddressesToOffice(EdsCrmContact contactListItem, Office365Contact contactEntry, Session session) {
        List<EdsAddress> addresses = session.createQuery("FROM EdsAddress WHERE deleted=false AND entityType = '" + EdsAddress.ENTITY_TYPE_CONTACT + "' and entityID = :contactID").setParameter("contactID", contactListItem.getObjectID()).list();
        if (addresses != null && !addresses.isEmpty()) {
            ArrayList<Office365PhysicalAddress> homeAddress = new ArrayList<>();
            ArrayList<Office365PhysicalAddress> businessAddress = new ArrayList<>();
            Office365PhysicalAddress otherAddress = null;
            for (EdsAddress address1 : addresses) {
                Office365PhysicalAddress address = new Office365PhysicalAddress();
                if (address1.getCountry() != null) {
                    address.setCountryOrRegion(address1.getCountry().getName());
                } else if (address1.getCountryName() != null && !"".equals(address1.getCountryName())) {
                    address.setCountryOrRegion(address1.getCountryName());
                }
                if (address1.getCity() != null && !address1.getCity().equals("")) {
                    address.setCity(address1.getCity());
                }
                if (address1.getState() != null) {
                    address.setState(address1.getState().getName());
                }
                if (address1.getZipCode() != null && !"".equals(address1.getZipCode())) {
                    address.setPostalCode(address1.getZipCode());
                }
                if (address1.getAddress() != null) {
                    address.setStreet(address1.getAddress());
                }

                if (address1.getRelationType() != null && address1.getAddress() != null) {
                    if (address1.getRelationType() == AddressReference.HOME.getId()) {
                        homeAddress.add(address);
                    } else if (address1.getRelationType() == AddressReference.WORK.getId()) {
                        businessAddress.add(address);
                    } else {
                        otherAddress = address;
                    }
                }
            }
            if (!businessAddress.isEmpty()) {
                contactEntry.setBusinessAddress(businessAddress.get(0));
            }
            if (!homeAddress.isEmpty()) {
                contactEntry.setHomeAddress(homeAddress.get(0));
            }
            contactEntry.setOtherAddress(otherAddress);
        }
    }

    private void setContactEmailsToOffice365(EdsCrmContact contactListItem, Office365Contact contact, Session session) {
        if (contact.getEmailAddresses() != null) {
            contact.getEmailAddresses().clear();
        }
        List<EdsCrmContactItemParams> emails = getContactParams(contactListItem.getObjectID(), GoogleContactsManagerImpl.CONTACT_EMAILS, session);
        if (emails != null && emails.size() > 0) {
            ArrayList<Office365EmailAddress> emailAddresses = new ArrayList<>();
            for (EdsCrmContactItemParams email : emails) {
                Email mail = new Email();
                if (email.getValue() != null && !email.getValue().equals("") && email.getRelation() != null) {
                    Office365EmailAddress emailAddress = new Office365EmailAddress();
                    emailAddress.setName(email.getValue());
                    emailAddress.setAddress(email.getValue());
                    emailAddresses.add(emailAddress);
                }
            }
            contact.setEmailAddresses(emailAddresses);
        }
    }

    private void setContactPhonesToOffice365(EdsCrmContact contactListItem, Office365Contact contact, boolean exportContactPhoneWithOutPlus, Session session) {
        if (contactListItem.getObjectID() != null) {
            if (contact.getHomePhones() != null) {
                contact.getHomePhones().clear();
            }
            if (contact.getBusinessPhones() != null) {
                contact.getBusinessPhones().clear();
            }
            List<EdsCrmContactItemParams> phones = getContactParams(contactListItem.getObjectID(), GoogleContactsManagerImpl.CONTACT_PHONES, session);
            if (phones != null && !phones.isEmpty()) {
                ArrayList<String> mobilePhones = new ArrayList<>();
                ArrayList<String> homePhones = new ArrayList<>();
                ArrayList<String> businessPhones = new ArrayList<>();
                for (EdsCrmContactItemParams phone : phones) {
                    PhoneNumber phoneNumber = new PhoneNumber();
                    if (phone.getValue() != null && !phone.getValue().replace("|", "").trim().equals("") && phone.getRelation() != null) {
                        String newPhone = phone.getValue();
                        newPhone = newPhone.replace("+", "");
                        newPhone = newPhone.replace("|", "").trim();
                        if (!exportContactPhoneWithOutPlus) {
                            phoneNumber.setPhoneNumber("+" + newPhone);
                        }

                        if (phone.getRelation().equals(GoogleContactsManagerImpl.G_MOBILE)) {
                            mobilePhones.add(phoneNumber.getPhoneNumber());
                        } else if (phone.getRelation().equals(GoogleContactsManagerImpl.G_WORK) || phone.getRelation().equals(GoogleContactsManagerImpl.G_WORK_FAX)) {
                            businessPhones.add(phoneNumber.getPhoneNumber());
                        } else {
                            homePhones.add(phoneNumber.getPhoneNumber());
                        }
                    }
                }
                if (!mobilePhones.isEmpty()) {
                    contact.setMobilePhone(mobilePhones.get(0));
                }
                contact.setHomePhones(homePhones);
                contact.setBusinessPhones(businessPhones);
            }
        }
    }

    public boolean importContactsFromThirdParty(List<EdsCrmContact> wftContactItems, ContactListItem[] apiContactItems, String thirdPartyAPI, EdsUser user) {

        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        String contactSynType = thirdPartyAPI.equals(GOOGLE) ? userSettings.getContactSyncType() : userSettings.getOfficeContactSyncType();

        if (KPIMASTER.equals(contactSynType)) {
            return true;
        }

        System.out.println("SyncGoogleContactsEventListenerImpl.getDifferentContactsByAnvar() started at " + (new Date()).toString());

        Map<Integer, ContactListItem> differentContacts = getDifferentContactsByAnvar(apiContactItems, wftContactItems, user);

        System.out.println("SyncGoogleContactsEventListenerImpl.getDifferentContactsByAnvar() finished at " + (new Date()).toString());

        Collection<ContactListItem> collection = differentContacts.values();
        ContactListItem[] newContactItems = new ContactListItem[collection.size()];

        if (!differentContacts.isEmpty()) {
            newContactItems = collection.toArray(new ContactListItem[]{});
        }

        if (wftContactItems.isEmpty()) {
            newContactItems = apiContactItems;
        }
        System.out.println("SyncGoogleContactsEventListenerImpl.saveContacts() started save " + newContactItems.length + " contacts at " + (new Date()).toString());
        return saveContacts(newContactItems, user, thirdPartyAPI);
    }

    private Map<Integer, ContactListItem> getDifferentContactsByAnvar(ContactListItem[] apiContactItems, List<EdsCrmContact> wftContactItems, EdsUser user) {
        Map<Integer, EdsCrmContact> wftContactsMapByHashCode = new HashMap<>(); // contacts map by hashCode contacts full name
        Map<String, EdsCrmContact> wftContactsByGoogleeId = new HashMap<>();   // contacts map by contact googleeId
        Map<Integer, ContactListItem> result = new HashMap<>();

        if (wftContactItems != null && !wftContactItems.isEmpty()) {
            //for (int j = 0; j < wftContactItems.size(); j++) {
            for (EdsCrmContact wfmContactItem : wftContactItems) {
                // put contact to contacts map by googleId
                if (StringUtils.isNotBlank(wfmContactItem.getGoogleId())) {
                    wftContactsByGoogleeId.put(wfmContactItem.getGoogleId(), wfmContactItem);
                }
                // put contact to contacts map by contacts fullName hashCode
                StringBuilder contactInfo = new StringBuilder();
                if (StringUtils.isNotBlank(wfmContactItem.getFirstName())) {
                    contactInfo.append(wfmContactItem.getFirstName().replace(" ", ""));
                }
                if (StringUtils.isNotBlank(wfmContactItem.getLastName())) {
                    contactInfo.append(wfmContactItem.getLastName().replace(" ", ""));
                }
                if (StringUtils.isNotBlank(wfmContactItem.getPrimaryEmail())) {
                    contactInfo.append(wfmContactItem.getPrimaryEmail().replace(" ", ""));
                }
                if (StringUtils.isNotBlank(contactInfo.toString())) {
                    Integer contactHash = contactInfo.toString().trim().hashCode();
                    wftContactsMapByHashCode.put(contactHash, wfmContactItem);
                }
            }
        }
        if (apiContactItems != null && apiContactItems.length > 0) {
            int i = 0;
            for (ContactListItem item : apiContactItems) {
                EdsCrmContact contactListItem = wftContactsByGoogleeId.get(item.getGoogleId());

                if (!wftContactsByGoogleeId.containsKey(item.getGoogleId())) {
                    boolean hasInResult = false;
                    StringBuilder info = new StringBuilder();
                    if (StringUtils.isNotBlank(item.getFirstName())) {
                        info.append(item.getFirstName().replace(" ", ""));
                    }
                    if (StringUtils.isNotBlank(item.getLastName())) {
                        info.append(item.getLastName().replace(" ", ""));
                    }
                    if (StringUtils.isNotBlank(item.getPrimaryEmail())) {
                        info.append(item.getPrimaryEmail().replace(" ", ""));
                    }
                    if (StringUtils.isNotBlank(info.toString())) {
                        Integer hash = info.toString().trim().replace(" ", "").hashCode();
                        if (!wftContactsMapByHashCode.containsKey(hash)) {
                            if (result.containsKey(hash)) {
                                result.put(hash + i, item);
                            } else {
                                result.put(hash, item);
                            }
                            hasInResult = true;
                        } else {
                            hasInResult = true;
                            if (wftContactsMapByHashCode.get(hash).getOwner().getObjectID() != null && wftContactsMapByHashCode.get(hash).getOwner().getObjectID().equals(user.getObjectID())) {
                                item.setObjectId(wftContactsMapByHashCode.get(hash).getObjectID());
                            }
                        }
                    }
                    if (!hasInResult) {
                        result.put(i, item);
                    }
                } else if (contactListItem.getOwner().getObjectID() != null && contactListItem.getOwner().getObjectID().equals(user.getObjectID())) {
                    item.setObjectId(contactListItem.getObjectID());
                    //Mark it as to import only if Office365 chages were made after kpi changes
                    if(item.getUpdatedDate()==null || (contactListItem.getAuditInfo()==null || contactListItem.getAuditInfo().getModificationDate()==null)
                            || item.getUpdatedDate().after(contactListItem.getAuditInfo().getModificationDate())) {
                        result.put(i, item);
                    }
                }
                i++;
            }
        }
        return result;
    }

    @Transactional
    public boolean saveContacts(ContactListItem[] itemsToSave, EdsUser user, String thirdPartyAPI) {
        if (user == null) {
            user = crmContactManager.getUser();
        }
        boolean result = true;
        List<Integer> contactIDs = new ArrayList<>();
        final long timeOfSyncing = new Date().getTime();
        for (int k = 0; k < itemsToSave.length; k++) {
            itemsToSave[k].setCheckForDuplicates(false);
            itemsToSave[k].setSyncID(timeOfSyncing);
            itemsToSave[k].setCreatedFrom(ContactListItem.REQUEST_FROM_CONTACT_SYNC);
            if(GOOGLE.equalsIgnoreCase(thirdPartyAPI)) {
                contactIDs.add(contactServiceLocal.saveContact(itemsToSave[k], null, user, false, true));
            } else {
                contactIDs.add(contactServiceLocal.saveOffice365Contact(itemsToSave[k], user));
            }
            if (k % 50 == 0) {
                crmContactManager.flushAndClear();
                System.out.println("50 new contacts added to WFT at " + new Date().toString());
            }
        }
        if (!contactIDs.isEmpty()) {
            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(ImportCustomEventListenerImpl.TYPE, ImportCustomEventListenerImpl.EVENT_SOLR_SYNC_CONTACT, null, user);
            event.setCustomStringField(ServerUtils.getAsCommoDelimited(contactIDs, "0", ","));
        }
        return result;
    }


    public void exportWFTContactsToGoogleContacts(List<ContactEntry> googleContacts, List<EdsCrmContact> wftContacts, EdsUser user, boolean exportContactPhoneWithOutPlus) throws AuthenticationException, GeneralSecurityException, IOException, ServiceException {
        //Create GOOGLE Service
        boolean isKpiMaster = false;
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        if (userSettings.getContactSyncType() != null && SERVERMASTER.equals(userSettings.getContactSyncType())) {
            return;
        } else if (userSettings.getContactSyncType() != null && KPIMASTER.equals(userSettings.getContactSyncType())) {
            isKpiMaster = true;
        }

        ContactsService myService = googleContactsManager.getLoggedService(user);

        List<EdsCrmContact> wftContactItems = new ArrayList<>();
        System.out.println("SyncGoogleContactsEventListenerImpl.checkingContactsExistsInGoogle(wftContacts, googleContacts, wftContactItems, user, forExport); has been started at " + (new Date()).toString());
        List<Map<String, ContactEntry>> googleContactsMap = checkingContactsExistsInGoogle(wftContacts, googleContacts, wftContactItems, user, isKpiMaster);
        System.out.println("SyncGoogleContactsEventListenerImpl.checkingContactsExistsInGoogle(wftContacts, googleContacts, wftContactItems, user, forExport); has been finished at " + (new Date()).toString());
        boolean contactEntryExists = false;
        System.out.println("SyncGoogleContactsEventListenerImpl started to export " + wftContactItems.size() + " contacts into GOOGLE at " + (new Date()).toString());

        if (myService != null && wftContactItems != null && wftContactItems.size() > 0) {

            String userGoogleID = googleContactsManager.getGoogleContact(user, true).getGoogleID();
            int flushLimit = 50;
            int flushingCount = 0;
            int contactsCount = 0;
            EntityManager em = jpaTemplate.getHibernateEntityManager();
            try (org.hibernate.Session session = em.unwrap(Session.class)) {
                if (session.getTransaction() != null && !session.getTransaction().isActive()) {
                    session.beginTransaction();
                }
                List<ContactGroupEntry> googleContactGroups = googleContactsManager.getContactGroupFeed(myService).getEntries();
                List<EdsGoogleWFTGroups> groupSettings = googleGroupsManager.getGroupSettings(userManager.getUser(), false);
                for (EdsCrmContact wftItem : wftContactItems) {
                    if (wftItem.getOwner().getObjectID() != null && wftItem.getOwner().getObjectID().equals(user.getObjectID()) && contactsCount <= CONTACTS_LIMIT) {
                        contactsCount++;
                        EdsCrmContact contactItem = (EdsCrmContact) session.get(EdsCrmContact.class, wftItem.getObjectID());//wftItem;//
                        flushingCount++;
                        try {
                            StringBuilder fullName = new StringBuilder();
                            if (contactItem.getFirstName() != null && !"".equals(contactItem.getFirstName())) {
                                fullName.append(contactItem.getFirstName().trim());
                            }
                            if (contactItem.getMiddleName() != null && !"".equals(contactItem.getMiddleName())) {
                                fullName.append(contactItem.getMiddleName().trim());
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
                                postUrl = new URL("https://www.google.com/m8/feeds/contacts/" + userGoogleID + "/full");
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

                            List<EdsContactCategory> contactCategories = crmContactManager.getContactCategoriesByContactID(wftItem.getObjectID());
                            if (contactCategories != null && contactCategories.size() > 0 && groupSettings != null && groupSettings.size() != 0 && googleContactGroups != null && googleContactGroups.size() != 0) {
                                Map<Integer, Set<String>> groups = new HashMap<>();
                                for (ContactGroupEntry group : googleContactGroups) {
                                    for (EdsGoogleWFTGroups setting : groupSettings) {
                                        if (setting.getGoogleGroupID().equals((group.getId().split("/")[group.getId().split("/").length - 1].hashCode() + ""))) {
                                            if (groups.containsKey(setting.getWftGroupID())) {
                                                groups.get(setting.getWftGroupID()).add(group.getId());
                                                break;
                                            } else {
                                                Set<String> newSet = new HashSet<>();
                                                newSet.add(group.getId());
                                                groups.put(setting.getWftGroupID(), newSet);
                                                break;
                                            }
                                        }
                                    }

                                }
                                contactEntry.getGroupMembershipInfos().clear();
                                if (groups.size() > 0) {
                                    for (EdsContactCategory contactCategory : contactCategories) {
                                        if (groups.containsKey(contactCategory.getObjectID())) {
                                            for (String href : groups.get(contactCategory.getObjectID())) {
                                                GroupMembershipInfo g = new GroupMembershipInfo();
                                                g.setHref(href);
                                                contactEntry.getGroupMembershipInfos().add(g);
                                            }
                                        }
                                    }
                                }
                            }

                            if (contactEntry.getGroupMembershipInfos().size() < 1) {
                                System.out.println("Export from WORKFORCETRACK skipped contact id " + wftItem.getObjectID() + " as its category is not chosen to sync " + (new Date()).toString());
                                continue;
                            }

                            // set Contact fullName ------------------------------------------------------------------------------------
                            Name name = new Name();
                            TextConstruct title = null;
                            if (contactItem.getName() != null) {
                                FullName contactFullName = new FullName(contactItem.getName(), null);
                                name.setFullName(contactFullName);
                                title = new PlainTextConstruct(contactItem.getTitle());
                                contactEntry.setTitle(title);
                            }

                            if (contactItem.getTitle() != null && !contactItem.getTitle().equals("")) {
                                name.setNamePrefix(new NamePrefix(contactItem.getTitle()));
                            }
                            if (contactItem.getOtherName() != null && !contactItem.getOtherName().equals("")) {
                                contactEntry.setNickname(new Nickname(contactItem.getOtherName()));
                            }
                            if (contactItem.getLastName() != null && !"".equals(contactItem.getLastName())) {
                                name.setFamilyName(new com.google.gdata.data.extensions.FamilyName(contactItem.getLastName(), ""));
                            }
                            if (contactItem.getFirstName() != null && !"".equals(contactItem.getFirstName())) {
                                name.setGivenName(new GivenName(contactItem.getFirstName(), ""));
                            }
                            if (contactItem.getMiddleName() != null && !"".equals(contactItem.getMiddleName())) {
                                name.setAdditionalName(new AdditionalName(contactItem.getMiddleName(), ""));
                            }

                            contactEntry.setName(name);

                            // set contact company -------------------------------------------------------------------------------------
                            if (contactEntry.getOrganizations() != null && contactEntry.getOrganizations().size() > 0 && contactEntry.getOrganizations().get(0) != null) {
                                if (contactItem.getCrmAccount() != null) {
                                    EdsCrmAccount a = (EdsCrmAccount) session.get(EdsCrmAccount.class, contactItem.getCrmAccount().getObjectID());
                                    if (a != null && a.getName() != null && !"".equals(a.getName())) {
                                        OrgName orgName = new OrgName(contactItem.getCrmAccount().getName());
                                        contactEntry.getOrganizations().get(0).setOrgName(orgName);
                                    }
                                }
                                if (contactItem.getJobTitles() != null && !"".equals(contactItem.getJobTitles())) {
                                    OrgTitle orgTitle = new OrgTitle(contactItem.getJobTitles());
                                    contactEntry.getOrganizations().get(0).setOrgTitle(orgTitle);
                                }
                                if (contactEntry.getOrganizations().get(0).getRel() == null) {
                                    contactEntry.getOrganizations().get(0).setRel(Organization.Rel.WORK);
                                }
                            } else {
                                Organization organization = new Organization();
                                if (contactItem.getCrmAccount() != null) {
                                    EdsCrmAccount a = (EdsCrmAccount) session.get(EdsCrmAccount.class, contactItem.getCrmAccount().getObjectID());
                                    if (a != null && a.getName() != null && !"".equals(a.getName())) {
                                        OrgName orgName = new OrgName(contactItem.getCrmAccount().getName());
                                        organization.setOrgName(orgName);
                                        organization.setOrgTitle(new OrgTitle(contactItem.getJobTitles()));
                                    }
                                }
                                organization.setRel(Organization.Rel.WORK);
                                contactEntry.addOrganization(organization);
                            }

                            // set contact birthday ------------------------------------------------------------------------------------
                            if (contactItem.getDateOfBirth() != null) {
                                Birthday birthDay = new Birthday();
                                Date birthDate = new DateNonConvertable(contactItem.getDateOfBirth()).getNonConvertedDate();
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
                            setContactPhonesToGoogle(contactItem, contactEntry, exportContactPhoneWithOutPlus, session);
                            flushingCount++;
                            // set contact websites
                            setContactWebSitesToGoogle(contactItem, contactEntry, session);
                            flushingCount++;
                            // set contact email addresses
                            setContactEmailsToGoogle(contactItem, contactEntry, session);
                            flushingCount++;
                            // set contact structured postal addresses
                            setContactAddressesToGoogle(contactItem, contactEntry, session);
                            flushingCount++;
                            // set contact IM addresses
                            setContactIMAddressesToGoogle(contactItem, contactEntry, session);
                            flushingCount++;

                            //check has contact in google contacts : if has contact in google, then update this contact in google, else export contact to google
                            if (contactEntryExists) {
                           /* if (contactItem.getAuditInfo().getModificationDate().after(new Date(contactEntry.getUpdated().getValue()))) {
                            }*/
                                URL editUrl = new URL(contactEntry.getEditLink().getHref());
                                try {
                                    ContactEntry updated = myService.update(editUrl, contactEntry);
                                    contactItem.setGoogleId(updated.getId());
                                    contactItem.getAuditInfo().setModificationDate(new Date(updated.getUpdated().getValue()));
                                } catch (IOException | ServiceException e) {
                                    e.printStackTrace();
                                    continue;
                                }
                            } else {
                                try {
                                    ContactEntry inserted = myService.insert(postUrl, contactEntry);
                                    contactItem.setGoogleId(inserted.getId());
                                    contactItem.getAuditInfo().setModificationDate(new Date(inserted.getUpdated().getValue()));

                                } catch (IOException | ServiceException e) {
                                    e.printStackTrace();
                                    continue;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                    if (flushingCount > flushLimit) {
                        session.flush();
                        session.clear();
                        flushingCount = 0;
                    }
                }
            } finally {
                em.close();
            }
        }
    }

    private List<EdsCrmContactItemParams> getContactParams(Integer contactId, Integer paramType, org.hibernate.Session session) {
        return (List<EdsCrmContactItemParams>) session.createQuery("FROM EdsCrmContactItemParams WHERE contact.objectID = :contactID AND param = :param")
                .setParameter("contactID", contactId)
                .setParameter("param", paramType).list();
    }

    private void setContactPhonesToGoogle(EdsCrmContact contactListItem, ContactEntry contact, Boolean exportContactPhoneWithOutPlus, org.hibernate.Session session) {
        if (contactListItem.getObjectID() != null) {
            List<EdsCrmContactItemParams> phones = getContactParams(contactListItem.getObjectID(), GoogleContactsManagerImpl.CONTACT_PHONES, session);

            if (phones != null && phones.size() > 0) {
                // if has google contact's phone number, first remove phones, then add phones to this google contact
                contact.getPhoneNumbers().removeAll(contact.getPhoneNumbers());
                for (EdsCrmContactItemParams phone : phones) {
                    PhoneNumber phoneNumber = new PhoneNumber();
                    if (phone.getValue() != null && !phone.getValue().replace("|", "").trim().equals("") && phone.getRelation() != null) {
                        String newPhone = phone.getValue();
                        newPhone = newPhone.replace("+", "");
                        newPhone = newPhone.replace("|", "").trim();
                        if (!exportContactPhoneWithOutPlus) {
                            phoneNumber.setPhoneNumber("+" + newPhone);
                        }
                        switch (phone.getRelation()) {
                            case GoogleContactsManagerImpl.G_HOME -> phoneNumber.setRel(PhoneNumber.Rel.HOME);
                            case GoogleContactsManagerImpl.G_WORK -> phoneNumber.setRel(PhoneNumber.Rel.WORK);
                            case GoogleContactsManagerImpl.G_MOBILE -> phoneNumber.setRel(PhoneNumber.Rel.MOBILE);
                            case GoogleContactsManagerImpl.G_HOME_FAX -> phoneNumber.setRel(PhoneNumber.Rel.HOME_FAX);
                            case GoogleContactsManagerImpl.G_WORK_FAX -> phoneNumber.setRel(PhoneNumber.Rel.WORK_FAX);
                            case GoogleContactsManagerImpl.G_PAGER -> phoneNumber.setRel(PhoneNumber.Rel.PAGER);
                            case GoogleContactsManagerImpl.G_OTHER -> phoneNumber.setRel(PhoneNumber.Rel.OTHER);
                            case GoogleContactsManagerImpl.G_EXTENSION -> phoneNumber.setLabel(G_EXTENSION_STR);
                        }
                        contact.addPhoneNumber(phoneNumber);
                    }
                }
            }
        }
    }

    private void setContactWebSitesToGoogle(EdsCrmContact contactListItem, ContactEntry contact, org.hibernate.Session session) {
        if (contactListItem.getObjectID() != null) {

            List<EdsCrmContactItemParams> webSites = getContactParams(contactListItem.getObjectID(), GoogleContactsManagerImpl.CONTACT_WEBSITES, session);

            if (webSites != null && webSites.size() > 0) {
                // if has google contact's web sites, first remove web sites, then add web sites to this google contact
                contact.getWebsites().removeAll(contact.getWebsites());
                for (EdsCrmContactItemParams webSite : webSites) {
                    if (webSite.getValue() != null && !webSite.getValue().trim().equals("") && webSite.getRelation() != null) {
                        Website website = new Website();
                        website.setHref(webSite.getValue());
                        switch (webSite.getRelation()) {
                            case GoogleContactsManagerImpl.G_HOME -> website.setRel(Website.Rel.HOME);
                            case GoogleContactsManagerImpl.G_WORK -> website.setRel(Website.Rel.WORK);
                            case GoogleContactsManagerImpl.G_HOME_PAGE -> website.setRel(Website.Rel.HOME_PAGE);
                            case GoogleContactsManagerImpl.G_FTP -> website.setRel(Website.Rel.FTP);
                            case GoogleContactsManagerImpl.G_BLOG -> website.setRel(Website.Rel.BLOG);
                            case GoogleContactsManagerImpl.G_PROFILE -> website.setRel(Website.Rel.PROFILE);
                            case GoogleContactsManagerImpl.G_OTHER -> website.setRel(Website.Rel.OTHER);
                        }
                        contact.addWebsite(website);
                    }
                }
            }
        }
    }

    private void setContactEmailsToGoogle(EdsCrmContact contactListItem, ContactEntry contact, org.hibernate.Session session) {
        List<EdsCrmContactItemParams> emails = getContactParams(contactListItem.getObjectID(), GoogleContactsManagerImpl.CONTACT_EMAILS, session);

        if (emails != null && emails.size() > 0) {
            // if has google contact's Email Addresses, first remove Email Addresses, then add Email Addresses to this google contact
            contact.getEmailAddresses().removeAll(contact.getEmailAddresses());
            for (EdsCrmContactItemParams email : emails) {
                Email mail = new Email();
                if (email.getValue() != null && !email.getValue().equals("") && email.getRelation() != null) {
                    mail.setAddress(email.getValue());
                    switch (email.getRelation()) {
                        case GoogleContactsManagerImpl.G_HOME -> mail.setRel(Email.Rel.HOME);
                        case GoogleContactsManagerImpl.G_WORK -> mail.setRel(Email.Rel.WORK);
                        case GoogleContactsManagerImpl.G_OTHER -> mail.setRel(Email.Rel.OTHER);
                    }
                    contact.addEmailAddress(mail);
                }
            }
        }
    }

    private void setContactAddressesToGoogle(EdsCrmContact contactListItem, ContactEntry contact, org.hibernate.Session session) {
        List<EdsAddress> addresses = session.createQuery("FROM EdsAddress WHERE entityType = '" + EdsAddress.ENTITY_TYPE_CONTACT + "' and entityID = :contactID").setParameter("contactID", contactListItem.getObjectID()).list();
        if (addresses != null && addresses.size() > 0) {
            // if has google contact's Postal Addresses, first remove Postal Addresses, then add Postal Addresses to this google contact
            contact.getStructuredPostalAddresses().removeAll(contact.getStructuredPostalAddresses());
            for (EdsAddress address1 : addresses) {
                StructuredPostalAddress address = new StructuredPostalAddress();
                if (address1.getCountry() != null) {
                    Country country = new Country(address1.getCountry().getCode(), address1.getCountry().getName());
                    address.setCountry(country);
                } else if (address1.getCountryName() != null && !"".equals(address1.getCountryName())) {
                    int lenth = address1.getCountryName().length() > 3 ? 3 : address1.getCountryName().length();
                    Country country = new Country(address1.getCountryName().toUpperCase().substring(0, lenth), address1.getCountryName());
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
                if (address1.getZipCode() != null && !"".equals(address1.getZipCode())) {
                    PostCode postCode = new PostCode(address1.getZipCode());
                    address.setPostcode(postCode);
                }

                if (address1.getAddress() != null && !address1.getAddress().equals("") && address1.getRelationType() != null) {
                    FormattedAddress formattedAddress = new FormattedAddress();
                    formattedAddress.setValue(address1.getAddress());
                    //address.setFormattedAddress(formattedAddress);
                    Street street = new Street(address1.getAddress());
                    address.setStreet(street);
                    if (address1.getRelationType() == AddressReference.HOME.getId()) {
                        address.setRel(StructuredPostalAddress.Rel.HOME);
                    } else if (address1.getRelationType() == AddressReference.WORK.getId()) {
                        address.setRel(StructuredPostalAddress.Rel.WORK);
                    } else {
                        address.setRel(StructuredPostalAddress.Rel.OTHER);
                    }
                    contact.addStructuredPostalAddress(address);
                }
            }
        }
    }

    private void setContactIMAddressesToGoogle(EdsCrmContact contactListItem, ContactEntry contact, org.hibernate.Session session) {
        List<EdsCrmContactItemParams> imAddresses = getContactParams(contactListItem.getObjectID(), GoogleContactsManagerImpl.CONTACT_IMADDRESSES, session);
        if (imAddresses != null && imAddresses.size() > 0) {
            // if has google contact's IM addresses, first remove IM addresses, then add IM addresses to this google contact
            contact.getImAddresses().removeAll(contact.getImAddresses());
            for (EdsCrmContactItemParams imAddress1 : imAddresses) {
                Im imAddress = new Im();
                if (imAddress1 != null && !imAddress1.getValue().equals("") && imAddress1.getRelation() != null) {
                    imAddress.setAddress(imAddress1.getValue());
                    imAddress.setLabel(imAddress1.getValue());
                    switch (imAddress1.getRelation()) {
                        case GoogleContactsManagerImpl.G_GOOGLE_TALK -> imAddress.setProtocol(Im.Protocol.GOOGLE_TALK);
                        case GoogleContactsManagerImpl.G_AIM -> imAddress.setProtocol(Im.Protocol.AIM);
                        case GoogleContactsManagerImpl.G_YAHOO -> imAddress.setProtocol(Im.Protocol.YAHOO);
                        case GoogleContactsManagerImpl.G_SKYPE -> imAddress.setProtocol(Im.Protocol.SKYPE);
                        case GoogleContactsManagerImpl.G_QQ -> imAddress.setProtocol(Im.Protocol.QQ);
                        case GoogleContactsManagerImpl.G_MSN -> imAddress.setProtocol(Im.Protocol.MSN);
                        case GoogleContactsManagerImpl.G_ICQ -> imAddress.setProtocol(Im.Protocol.ICQ);
                        case GoogleContactsManagerImpl.G_JABBER -> imAddress.setProtocol(Im.Protocol.JABBER);
                    }
                    contact.addImAddress(imAddress);
                }
            }
        }
    }

    public List<Map<String, ContactEntry>> checkingContactsExistsInGoogle(List<EdsCrmContact> wftContacts, List<ContactEntry> googleContacts, List<EdsCrmContact> contactItems, EdsUser user, boolean isKpiMaster) throws IOException, ServiceException {
        Map<String, ContactEntry> googleContactsMapByGoogleID = new HashMap<>(); // google contacts map by googleID contacts full name
        Map<String, ContactEntry> googleContactsMapByHashCode = new HashMap<>();// google contacts map by hashCode contacts full name
        List<Map<String, ContactEntry>> result = new ArrayList<>();
        if (googleContacts != null && googleContacts.size() > 0) {
            for (ContactEntry entry : googleContacts) {
                googleContactsMapByGoogleID.put(entry.getId(), entry);
                StringBuilder info = new StringBuilder();
                if (entry.getTitle() != null && !"".equals(entry.getTitle()) && entry.getTitle().getPlainText() != null && !"".equals(entry.getTitle().getPlainText())) {
                    info.append(entry.getTitle().getPlainText().replace(" ", ""));
                }
                if (entry.getEmailAddresses() != null && entry.getEmailAddresses().size() > 0 && !"".equals(entry.getEmailAddresses().get(0).getAddress())) {
                    info.append(entry.getEmailAddresses().get(0).getAddress());
                }
                if (info != null && !"".equals(info.toString())) {
                    Integer hash = info.toString().hashCode();
                    /*if (true*//*!googleContactsMapByHashCode.containsKey(hash.toString())*//*) {
                    }*/
                    googleContactsMapByHashCode.put(hash.toString(), entry);
                }
            }
        }

        for (EdsCrmContact item : wftContacts) {
            if (StringUtils.isNotBlank(item.getGoogleId())) {
                if (!googleContactsMapByGoogleID.containsKey(item.getGoogleId()) && !isKpiMaster) {
                    if (/*!forExport && */item.getOwner().getObjectID() != null && item.getOwner().getObjectID().equals(user.getObjectID())) {
                        //do not delete Supplier, Client and Employee Contact
                        if (!item.getCategories().contains(contactCategoryManager.get(EdsContactCategory.SUPPLIER_CONTACT_CATEGORY))
                                && !item.getCategories().contains(contactCategoryManager.get(EdsContactCategory.CLIENT_CONTACT_CATEGORY))
                                && !item.getCategories().contains(contactCategoryManager.get(EdsContactCategory.EMPLOYEE_CONTACT_CATEGORY))) {
                            deleteContact(item.getObjectID(), user.getObjectID());
                        }
                    }
                } else {
                    if (/*!forExport && */item.getOwner().getObjectID().equals(user.getObjectID())/* && item.getAuditInfo().getModificationDate() != null && googleContactsMapByGoogleID.get(item.getGoogleId()) != null && googleContactsMapByGoogleID.get(item.getGoogleId()).getUpdated() != null &&
                    item.getAuditInfo().getModificationDate().getTime() > googleContactsMapByGoogleID.get(item.getGoogleId()).getUpdated().getValue() */) {
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
                    if (!googleContactsMapByHashCode.containsKey(hash.toString()) && item.getOwner().getObjectID().equals(user.getObjectID())) {
                        contactItems.add(item);
                    } else {
                        if (/*!forExport && */item.getOwner().getObjectID().equals(user.getObjectID()) /*&& item.getAuditInfo().getModificationDate() != null && googleContactsMapByHashCode.get(hash.toString()) != null
                                && googleContactsMapByHashCode.get(hash.toString()).getUpdated() != null && item.getAuditInfo().getModificationDate().getTime() > googleContactsMapByHashCode.get(hash.toString()).getUpdated().getValue()*/) {
                            contactItems.add(item);
                        }
                    }
                }
            }
        }
        result.add(googleContactsMapByGoogleID);
        result.add(googleContactsMapByHashCode);
        return result;
    }

    public List<Map<String, Office365Contact>> checkingContactsExistsInOffice365(List<EdsCrmContact> wftContacts, Office365BaseList<Office365Contact> office365Contacts, List<EdsCrmContact> contactItems, EdsUser user, boolean isKpiMaster) {
        Map<String, Office365Contact> office365ContactsMapByGoogleID = new HashMap<>(); // google contacts map by googleID contacts full name
        Map<String, Office365Contact> office365ContactsMapByHashCode = new HashMap<>();// google contacts map by hashCode contacts full name
        List<Map<String, Office365Contact>> result = new ArrayList<>();
        if (office365Contacts != null && !office365Contacts.isEmpty()) {
            for (Office365Contact entry : office365Contacts) {
                //Store office365 id
                office365ContactsMapByGoogleID.put(entry.getId(), entry);

                StringBuilder info = new StringBuilder();
                if (StringUtils.isNotBlank(entry.getGivenName())) {
                    info.append(entry.getGivenName());
                }
                if (StringUtils.isNotBlank(entry.getSurname())) {
                    info.append(entry.getSurname());
                }
                if (entry.getEmailAddresses() != null && !entry.getEmailAddresses().isEmpty() && StringUtils.isNotBlank(entry.getEmailAddresses().get(0).getAddress())) {
                    info.append(entry.getEmailAddresses().get(0).getAddress());
                }
                if (StringUtils.isNotBlank(info)) {
                    Integer hash = info.toString().replace(" ", "").hashCode();
                    office365ContactsMapByHashCode.put(hash.toString(), entry);
                }
            }
        }

        for (EdsCrmContact item : wftContacts) {
            if (StringUtils.isNotBlank(item.getGoogleId())) {
                if (!office365ContactsMapByGoogleID.containsKey(item.getGoogleId()) && !isKpiMaster) {
                    if (/*!forExport && */item.getOwner().getObjectID() != null && item.getOwner().getObjectID().equals(user.getObjectID())) {
                        if (!item.getCategories().contains(contactCategoryManager.get(EdsContactCategory.SUPPLIER_CONTACT_CATEGORY))
                                && !item.getCategories().contains(contactCategoryManager.get(EdsContactCategory.CLIENT_CONTACT_CATEGORY))
                                && !item.getCategories().contains(contactCategoryManager.get(EdsContactCategory.EMPLOYEE_CONTACT_CATEGORY))) {
                            deleteContact(item.getObjectID(), user.getObjectID());
                        }
                    }
                } else {
                    if (/*!forExport && */item.getOwner().getObjectID().equals(user.getObjectID())) {
                        //Mark it as to import only if KPI chages were made after Office365 changes
                        Office365Contact office365Contact = office365ContactsMapByGoogleID.get(item.getGoogleId());
                        if(office365Contact.getDateTimeLastModified()==null || (item.getAuditInfo()==null || item.getAuditInfo().getModificationDate()==null)
                                || office365Contact.getDateTimeLastModified().before(item.getAuditInfo().getModificationDate())) {
                            contactItems.add(item);
                        }

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
                if (StringUtils.isNotBlank(fullName)) {
                    Integer hash = fullName.toString().replace(" ", "").hashCode();
                    if (!office365ContactsMapByHashCode.containsKey(hash.toString()) && item.getOwner().getObjectID().equals(user.getObjectID())) {
                        contactItems.add(item);
                    } /*else {
                        if (item.getOwner().getObjectID().equals(user.getObjectID())) {
                            contactItems.add(item);
                        }
                    }*/
                }
            }
        }
        result.add(office365ContactsMapByGoogleID);
        result.add(office365ContactsMapByHashCode);
        return result;
    }

    public void deleteContact(Integer contactID, Integer userId) {
        EdsCrmContact contact = crmContactManager.get(contactID);
        if (contact != null && contact.getOwner().getObjectID().equals(userId)) {
            Integer inactiveID = referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_INACTIVE).getObjectID();
            crmContactManager.deleteContact(contactID, inactiveID);
            try {
                solrManager.removeCompanyCrmContactBuIds(contactID);
            } catch (Exception e) {
                e.printStackTrace();
                baseEventPostProcessor.registerEvent(CrmContactCustomEventListenerImpl.TYPE, CrmContactCustomEventListenerImpl.EVENT_DELETE_CRM_CONTACT_FROM_SOLR, crmContactManager.get(contactID), crmContactManager.getUser());
            }
        }
    }

    private void sendConfirmation(Map<String, Object> values, String toEmail) {
        try {
            String text = EdsTemplates.processTemplate(values, EdsTemplates.GOOGLE_CONTACT_SYNC);
            messageManager.sendMessageFromUser(null, toEmail, null, null, commonLocalizer.localize(EdsSubjects.GOOGLE_CONTACT_SYNC), text, false, null, null, false, null, null, null);
        } catch (Exception e) {
            System.out.println("Couldn't send GOOGLE contact synced confirmation ");
            e.printStackTrace();
        }

    }
}

