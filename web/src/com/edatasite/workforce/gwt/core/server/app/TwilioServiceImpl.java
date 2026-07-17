package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsAsteriskSettings;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsMyCallsSettings;
import com.edatasite.workforce.core.domain.EdsSipuniSettings;
import com.edatasite.workforce.core.domain.EdsTwilioSettings;
import com.edatasite.workforce.core.domain.crm.EdsSmsSendItem;
import com.edatasite.workforce.core.domain.settings.EdsGenericSettings;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.MyCallsSettings;
import com.edatasite.workforce.gwt.contact.client.rpc.SipuniSettings;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioContact;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioContactItem;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioService;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioSettings;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEventRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.edatasite.workforce.gwt.core.server.db.AsteriskSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.MyCallsSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.SipuniSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.SmsSendItemManager;
import com.edatasite.workforce.gwt.core.server.db.TwilioSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.edatasite.workforce.gwt.crm.client.rpc.LeadList;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.jwt.client.ClientCapability;
import com.twilio.jwt.client.IncomingClientScope;
import com.twilio.jwt.client.OutgoingClientScope;
import com.twilio.jwt.client.Scope;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.Recording;
import com.twilio.type.PhoneNumber;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * User: Hayot
 * Date: May 1, 2018
 * Time: 5:06:47 PM
 */
@Transactional
@org.springframework.stereotype.Service("twilioService")
public class TwilioServiceImpl implements TwilioService, Constants {
    public static final String TWILIO_ACCOUNT_SID = "AC8f9be29bbecd385fd478f1c1f4bd7daf";
    public static final String TWILIO_AUTH_TOKEN = "d33a52004f283388ad90b446e5d51e99";
    public static final String TWILIO_APPLICATION_SID = "AP7fb8e7a8dde384c69c0f2ab599d0cb3b";//https://www.twilio.com/console/phone-numbers/runtime/twiml-apps/AP7fb8e7a8dde384c69c0f2ab599d0cb3b

    @Autowired
    private TwilioSettingsManager twilioSettingsManager;
    @Autowired
    private AsteriskSettingsManager asteriskSettingsManager;
    @Autowired
    private SipuniSettingsManager sipuniSettingsManager;
    @Autowired
    private MyCallsSettingsManager myCallsSettingsManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private SmsSendItemManager smsSendItemManager;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    ContactService contactService;
    @Autowired
    private UserManager userManager;

    @Override
    public String getCountryNameByPhoneNumber(String phoneNumber) {
        String result = "";
        if (StringUtils.isBlank(phoneNumber)) {
            return result;
        }
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            // phone must begin with '+'
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, "");
            int countryCode = numberProto.getCountryCode();
            EdsCountry edsCountry = countryManager.getCountryByCallCode(String.valueOf(countryCode));
            if (edsCountry != null) {
                result = edsCountry.getName();
            }
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e);
            result = "Uzbekistan";
        }
        return result;
    }

    @Override
    public ArrayList<Appointment> getRecentCallLogs(int activityType, String relationType, Integer relationID) {
        ArrayList<Appointment> result = new ArrayList<>();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEventType(activityType);
        fp.setCreatedFrom(Appointment.FROM_CRM);
        fp.setSortField(SolrEventRepresenter.FIELD_CREATION_DATE);
        fp.setSortDir(2);
        fp.setAscending(false);
        fp.setLimit(10);
        fp.setRelationType(relationType);
        fp.setRelationID(relationID);
        ListResult<EventItem> r = crmServiceLocal.getEventList(fp);
        if (r != null && r.getList() != null && r.getList().size() > 0) {
            for (EventItem item : r.getList()) {
                Appointment appointment = new Appointment();
                appointment.setObjectID(item.getObjectID());
                appointment.setStartDate(item.getStartDate());
                appointment.setEndDate(item.getEndDate());
                appointment.setCallDuration(item.getCallDuration());
                appointment.setSubject(item.getSubject());
                appointment.setDescription(item.getDescription());
                if (item.getEndDate() != null && item.getStartDate() != null) {
                    appointment.setCallDuration(item.getEndDate().getTime() - item.getStartDate().getTime());
                }
                result.add(appointment);
            }
        }
        return result;
    }

    @Override
    public ListResult<TwilioSettings> list(ListingFilterParameter filterParameter) {
        ArrayList<TwilioSettings> items = twilioSettingsManager.list(filterParameter).stream().map(EdsTwilioSettings::getRPC).collect(Collectors.toCollection(ArrayList::new));
        int totalCount = twilioSettingsManager.listCount(filterParameter);
        return new ListResult<>(items, totalCount);
    }

    @Override
    public ListResult<AsteriskSettings> listAsteriskSettings(ListingFilterParameter filterParameter) {
        ArrayList<AsteriskSettings> items = asteriskSettingsManager.list(filterParameter).stream()
                .map(EdsAsteriskSettings::getRPC)
                .collect(Collectors.toCollection(ArrayList::new));
        int totalCount = asteriskSettingsManager.listCount(filterParameter);
        return new ListResult<>(items, totalCount);
    }

    @Override
    public ListResult<SipuniSettings> listSipuniSettings(ListingFilterParameter listingFilterParameter) {
        ArrayList<SipuniSettings> items = sipuniSettingsManager.list(listingFilterParameter).stream()
                .map(EdsSipuniSettings::getRPC)
                .collect(Collectors.toCollection(ArrayList::new));
        int totalCount = sipuniSettingsManager.listCount(listingFilterParameter);
        return new ListResult<>(items, totalCount);
    }

    @Override
    public Integer save(TwilioSettings settings) {
        if (settings != null) {
            EdsTwilioSettings edsSettings = settings.getObjectID() != null ? twilioSettingsManager.get(settings.getObjectID()) : new EdsTwilioSettings();
            edsSettings = edsSettings == null ? new EdsTwilioSettings() : edsSettings;
            edsSettings.fromRPC(settings);
            twilioSettingsManager.createOrUpdate(edsSettings);
            if (!genericSettingsManager.exists(GenericSettingsEnum.ENABLE_TWILIO)) {
                EdsGenericSettings genericSettings = new EdsGenericSettings();
                genericSettings.setKey(GenericSettingsEnum.ENABLE_TWILIO);
                genericSettings.setValue(EdsGenericSettings.YES);
                genericSettingsManager.createOrUpdate(genericSettings);
            }
            return edsSettings.getObjectID();
        }
        return null;
    }

    @Override
    public Integer save(AsteriskSettings settings) {
        if (settings != null) {
            EdsAsteriskSettings edsSettings = settings.getId() != null ? asteriskSettingsManager.get(settings.getId()) : new EdsAsteriskSettings();
            edsSettings = edsSettings == null ? new EdsAsteriskSettings() : edsSettings;
            edsSettings.fromRPC(settings);
            asteriskSettingsManager.createOrUpdate(edsSettings);
            if (!genericSettingsManager.exists(GenericSettingsEnum.ENABLE_ASTERISK)) {
                EdsGenericSettings genericSettings = new EdsGenericSettings();
                genericSettings.setKey(GenericSettingsEnum.ENABLE_ASTERISK);
                genericSettings.setValue(EdsGenericSettings.YES);
                genericSettingsManager.createOrUpdate(genericSettings);
            }
            return edsSettings.getObjectID();
        }
        return null;
    }

    @Override
    public Integer save(SipuniSettings settings) {
        if (settings != null) {
            EdsSipuniSettings edsSettings = settings.getObjectID() != null ? sipuniSettingsManager.get(settings.getObjectID()) : new EdsSipuniSettings();
            edsSettings = edsSettings == null ? new EdsSipuniSettings() : edsSettings;
            if (settings.getOperator() != null) {
                edsSettings.setOperator(userManager.get(settings.getOperator().getId()));
            }
            edsSettings.setOperatorNumber(settings.getOperatorNumber());
            edsSettings.setSipNumber(settings.getSipNumber());
            edsSettings.setSecretKey(settings.getSecretKey());
            edsSettings.setCompanyId(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
            sipuniSettingsManager.createOrUpdate(edsSettings);
            return edsSettings.getObjectID();
        }
        return null;
    }

    @Override
    public Boolean delete(Integer settingsId) {
        if (settingsId != null) {
            EdsTwilioSettings edsSettings = twilioSettingsManager.get(settingsId);
            edsSettings.setDeleted(true);
            twilioSettingsManager.createOrUpdate(edsSettings);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean deleteAsteriskSettings(Integer settingsId) {
        if (settingsId != null) {
            EdsAsteriskSettings edsSettings = asteriskSettingsManager.get(settingsId);
            edsSettings.setDeleted(true);
            asteriskSettingsManager.createOrUpdate(edsSettings);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean deleteSipuniSettings(Integer settingsId) {
        if (settingsId != null) {
            EdsSipuniSettings edsSettings = sipuniSettingsManager.get(settingsId);
            edsSettings.setDeleted(true);
            sipuniSettingsManager.createOrUpdate(edsSettings);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public TwilioSettings get(Integer id) {
        return getRPC(twilioSettingsManager.get(id));
    }

    @Override
    public AsteriskSettings getAsteriskSettings(Integer id) {
        return getRPC(asteriskSettingsManager.get(id));
    }

    @Override
    public SipuniSettings getSipuniSettings(Integer id) {
        return getRPC(sipuniSettingsManager.get(id));
    }

    @Override
    public TwilioSettings getByNumber(String number) {
        return getRPC(twilioSettingsManager.getByNumber(number));
    }

    private TwilioSettings getRPC(EdsTwilioSettings settings) {
        if (settings != null) {
            return settings.getRPC();
        }
        return null;
    }

    private AsteriskSettings getRPC(EdsAsteriskSettings settings) {
        if (settings != null) {
            return settings.getRPC();
        }
        return null;
    }

    private SipuniSettings getRPC(EdsSipuniSettings settings) {
        if (settings != null) {
            return settings.getRPC();
        }
        return null;
    }

    private MyCallsSettings getRPC(EdsMyCallsSettings settings) {
        if (settings != null) {
            return settings.getRPC();
        }
        return null;
    }

    @Override
    public String encrypt(String number) {
        if (number == null || "".equalsIgnoreCase(number)) {
            return null;
        }
        StringBuffer f = new StringBuffer();
        number = "+" + number.replaceAll("[^0-9]", "");
        return String.format("%s/services/api/v2/twilio/voice?cid=%s&number=%s", EdsContextParams.getHost(), EncryptionHelper.encrypt(ServerSecurityContext.getInstance().getCompanyId()), EncryptionHelper.encrypt(number));
    }

    @Transactional
    public String getTwilioToken(Integer twilioSettingId) {
        String accountSID = TWILIO_ACCOUNT_SID;
        String applicationSID = TWILIO_APPLICATION_SID;
        String authToken = TWILIO_AUTH_TOKEN;
        /*List<EdsTwilioSettings> settings = twilioSettingsManager.list(new ListingFilterParameter());
        if (settings != null && settings.size() > 0 && settings.get(0) != null) {
            EdsTwilioSettings setting = settings.get(0);
            if (setting != null) {
                accountSID = setting.getAccountSid();
                applicationSID = setting.getApplicationSid();
                authToken = setting.getAuthToken();
            }
        }*/
        EdsTwilioSettings settings = twilioSettingsManager.get(twilioSettingId);
        if (settings != null) {
            if (settings != null) {
                accountSID = settings.getAccountSid();
                applicationSID = settings.getApplicationSid();
                authToken = settings.getAuthToken();
            }
        }
        OutgoingClientScope outgoingScope = new OutgoingClientScope.Builder(applicationSID).build();
        IncomingClientScope incomingScope = new IncomingClientScope("kpi");
        List<Scope> scopes = Lists.newArrayList(outgoingScope, incomingScope);
        ClientCapability capability = new ClientCapability.Builder(accountSID, authToken)
                .scopes(scopes)
                .build();
        return capability.toJwt();
    }

    @Transactional
    public String makeACall(String fromNumber, String toNumber) {
        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);


        Call call = null;
        try {
            call = Call.creator(new PhoneNumber(toNumber), new PhoneNumber(fromNumber),
                    new URI("http://demo.twilio.com/docs/voice.xml")).create();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        System.out.println(call.getSid());
        return call.getSid();
    }

    @Transactional
    public String sendSms(String toNumber, String fromNumber, String content) {
        init();
        Message message = Message.creator(new PhoneNumber(toNumber), new PhoneNumber(fromNumber), content).create();
        System.out.println(message.getSid());
        return message.getSid();
    }

    @Transactional
    public ArrayList<String> getRecordUrls(String callSid) {
        init();
        ResourceSet<Recording> recordings = Recording.reader()
                .setCallSid(callSid)
                .read();
        ArrayList<String> result = new ArrayList<>();
        for (Recording recording : recordings) {
            result.add(recording.getUri().replace("\\.json", ".wav"));
        }
        return result;
    }

    private String init() {
        List<EdsTwilioSettings> settings = twilioSettingsManager.list(new ListingFilterParameter());
        if (settings != null && settings.size() > 0 && settings.get(0) != null) {
            EdsTwilioSettings setting = settings.get(0);
            Twilio.init(setting.getAccountSid(), setting.getAuthToken());
            return setting.getNumber();
        }
        return null;
    }

    @Override
    public TwilioContactItem[] getContactList(ListingFilterParameter filterParametrs, ListLoadConfig config) {
        TwilioContact result = new TwilioContact();
        ContactList contactList = contactService.getContactList(filterParametrs, config);
        result.setTotalCount(contactList.getTotalCount());
        if (contactList.getContactListItems() != null && contactList.getContactListItems().length > 0) {
            ArrayList<TwilioContactItem> twItems = new ArrayList<>();
            for (ContactListItem citem : contactList.getContactListItems()) {
                twItems.add(new TwilioContactItem(citem.getObjectId(), citem.getName(), citem.getPrimaryPhone(), citem.getContactType(), citem.getMobile()));
            }
            result.setContactListItems(twItems.toArray(new TwilioContactItem[]{}));
        }
        return result.getContactListItems();
    }

    @Override
    public ListResult<MyCallsSettings> listMyCallsSettings(ListingFilterParameter listingFilterParameter) {
        ArrayList<MyCallsSettings> items = myCallsSettingsManager.list(listingFilterParameter).stream()
                .map(EdsMyCallsSettings::getRPC)
                .collect(Collectors.toCollection(ArrayList::new));
        int totalCount = myCallsSettingsManager.listCount(listingFilterParameter);
        return new ListResult<>(items, totalCount);
    }

    @Override
    public Integer save(MyCallsSettings settings) {
        if (settings != null) {
            EdsMyCallsSettings edsSettings = settings.getObjectID() != null ? myCallsSettingsManager.get(settings.getObjectID()) : new EdsMyCallsSettings();
            edsSettings = edsSettings == null ? new EdsMyCallsSettings() : edsSettings;
            if (settings.getOperator() != null) {
                edsSettings.setOperator(userManager.get(settings.getOperator().getId()));
            }
            edsSettings.setUserLogin(settings.getUserLogin());
            edsSettings.setSecretKey(settings.getSecretKey());
            edsSettings.setCompanyId(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
            myCallsSettingsManager.createOrUpdate(edsSettings);
            return edsSettings.getObjectID();
        }
        return null;
    }

    @Override
    public Boolean deleteMyCallsSettings(Integer settingsId) {
        if (settingsId != null) {
            EdsMyCallsSettings edsSettings = myCallsSettingsManager.get(settingsId);
            edsSettings.setDeleted(true);
            myCallsSettingsManager.createOrUpdate(edsSettings);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public MyCallsSettings getMyCallsSettings(Integer id) {
        return getRPC(myCallsSettingsManager.get(id));
    }

    @Override
    public ArrayList<TwilioContactItem> getContactByContactNumber(String lastNumber) {
        ArrayList<TwilioContactItem> result = new ArrayList<>();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(lastNumber);
        ContactList contactList = contactService.getContactList(fp, new ListLoadConfig(10));

        if (contactList != null && contactList.getContactListItems() != null) {
            for (ContactListItem contactListItem : contactList.getContactListItems()) {
                result.add(new TwilioContactItem(contactListItem.getObjectId(), contactListItem.getName(), contactListItem.getPrimaryPhone(), contactListItem.getContactType(), contactListItem.getMobile()));
            }
            if (contactList.getContactListItems().length < 10) {
                LeadList leadList = crmServiceLocal.getLeadList(fp, new ListLoadConfig(10 - contactList.getContactListItems().length));
                if (leadList != null && leadList.getLeadListItems() != null) {
                    for (ContactListItem contactListItem : leadList.getLeadListItems()) {
                        result.add(new TwilioContactItem(contactListItem.getObjectId(), contactListItem.getName(), contactListItem.getPrimaryPhone(), contactListItem.getContactType(), contactListItem.getMobile()));
                    }
                }
            }
        }
        return result;
    }

    @Override
    public ArrayList<SmsSendItem> getSmsByNumber(String relationType, Integer relationID, String number) {
        ArrayList<SmsSendItem> result = new ArrayList<>();
//        smsSendItemManager.getSmsList()
        return result;
    }

    @Override
    public SmsSendItem sendSms(String number, String text, RelationItem relationItem) {
        String fromNumber = init();
        if (fromNumber != null) {
//            Message message = Message
//                    .creator(new PhoneNumber(number), // to
//                            new PhoneNumber(fromNumber), // from
//                            text)
//                    .create();
            EdsSmsSendItem item = new EdsSmsSendItem();
            item.setToNumber(number);
            item.setSentDate(new Date());
            item.setMessageText(text);
//            item.setTwilioMessage(true);
//            if (message.getSid() != null) {
//                smsSendItemManager.create(item);
//                if(relationItem != null && relationItem.getToID() != null && relationItem.getToType() != null){
//                    ArrayList<RelationItem> relations = new ArrayList<>();
//                    relations.add(relationItem);
//                    allInOneServiceLocal.saveRelations(RelationItem.TYPE_SMS, item.getObjectID(), item.getToNumber(), relations);
//                }
//            }
            return item.getRPC(null);
        }
        return null;
    }
}
