package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.workforce.core.domain.EdsSipuniSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactTo;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Attendee;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.RedisSocketObject;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactItemParamsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.SipuniSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceLocal;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_EVENT;

@Tag(name = "Sipuni Webhook Handler")
@RestController
@RequestMapping(value = "/sipuni",
        produces = {MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiSipuniControllerV3 extends BaseApiControllerV3 {
    private static final Logger log = LoggerFactory.getLogger(ApiSipuniControllerV3.class);

    private final Gson gson = new Gson();

    private static final String SIPUNI_EVENT_ANSWERED = "3";
    private static final String SIPUNI_EVENT_HANG_UP = "2";

    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CrmContactItemParamsManager crmContactItemParamsManager;
    @Autowired
    private AllInOneServiceLocal allInOneService;
    @Autowired
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private GoogleCalendarServiceLocal googleCalendarServiceLocal;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private SipuniSettingsManager sipuniSettingsManager;
    @Autowired
    private ContactSolrComponent contactSolrComponent;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;


    @Operation(summary = "Event Handler")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Sipuni Webhook"))
    @RequestMapping(path = "/webhook/{companyId}", method = RequestMethod.GET)
    @Transactional
    public ResultTO<Boolean> webhook(@PathVariable final String companyId, @RequestParam(value = "event", required = false) String event,
                                     @RequestParam(value = "call_id", required = false) String call_id,
                                     @RequestParam(value = "src_num", required = false) String src_num,
                                     @RequestParam(value = "src_type", required = false) String src_type,
                                     @RequestParam(value = "dst_num", required = false) String dst_num,
                                     @RequestParam(value = "dst_type", required = false) String dst_type,
                                     @RequestParam(value = "timestamp", required = false) String timestamp,
                                     @RequestParam(value = "call_start_timestamp", required = false) String call_start_timestamp,
                                     @RequestParam(value = "call_answer_timestamp", required = false) String call_answer_timestamp,
                                     @RequestParam(value = "call_record_link", required = false) String call_record_link,
                                     @RequestParam(value = "status", required = false) String status,
                                     @RequestParam(value = "short_src_num", required = false) String short_src_num,
                                     @RequestParam(value = "short_dst_num", required = false) String short_dst_num) {
        log.info("Sipuni webhook received: companyId={}, event={}, call_id={}, src_num={}, dst_num={}, src_type={}, dst_type={}, status={}, short_src_num={}, short_dst_num={}, timestamp={}, call_start_timestamp={}, call_answer_timestamp={}, call_record_link={}",
                companyId, event, call_id, src_num, dst_num, src_type, dst_type, status, short_src_num, short_dst_num, timestamp, call_start_timestamp, call_answer_timestamp, call_record_link);

        try {
            SecurityContext.getInstance().setCompanyId(companyId);
            EdsSipuniSettings sipuniSettingsBySipNumber = null;
            if (!short_dst_num.isEmpty() && !short_src_num.isEmpty()) {
                sipuniSettingsBySipNumber = sipuniSettingsManager.getSipuniSettingsBySipNumber(src_type.equals("1") ? short_dst_num : short_src_num);
                if (sipuniSettingsBySipNumber == null) {
                    log.error("Sipuni settings not found");
                    return ResultTO.success(false);
                }
                SecurityContext.getInstance().setCompanyId(sipuniSettingsBySipNumber.getCompanyId());
            }
            Integer operatorId = null;
            if (sipuniSettingsBySipNumber != null && sipuniSettingsBySipNumber.getOperator() != null) {
                operatorId = sipuniSettingsBySipNumber.getOperator().getObjectID();
                SecurityContext.getInstance().setStaticUserID(operatorId);
            }
            String phoneNumber = src_type.equals("1") ? src_num : dst_num;

            if (event == null) {
                log.error("Event is null");
                return ResultTO.success(false);
            }

            if (event.equals(SIPUNI_EVENT_ANSWERED)) {
                callAnswered(phoneNumber);
            }
            if (event.equals(SIPUNI_EVENT_HANG_UP) && status != null && (status.equals("ANSWER") || status.equals("NOANSWER"))) {
                callHangUp(call_id, src_type, timestamp, call_start_timestamp, call_record_link, status, phoneNumber, operatorId);
            }

            return ResultTO.success(true);
        } catch (Exception e) {
            log.error("Error processing webhook", e);
            return ResultTO.success(false);
        }
    }

    private void callAnswered(String phoneNumber) {
        ContactTo contactTo = null;
        EdsCrmContact contact = crmContactManager.getByPhone(phoneNumber);
        if (contact == null) {
            contactTo = allInOneServiceLocal.createContactFromCalls(phoneNumber, null);
        } else {
            contactTo = new ContactTo();
            contactTo.setPhone(phoneNumber);
            contactTo.setItem_id(contact.getObjectID());
            contactTo.setLast_name(contact.getLastName());
            contactTo.setContactType(String.valueOf(contact.getContactType()));
            if (contact.getFirstName() == null || !contact.getFirstName().contains("Sipuni")) {
                contactTo.setFirst_name(contact.getFirstName());
            }
            if (contact.getName() == null || !contact.getName().contains("Sipuni")) {
                contactTo.setName(contact.getName());
            }
        }
        Integer user = userManager.getUser().getObjectID();
        WebSocketServerObject message = new WebSocketServerObject();
        message.setUserId(user);
        message.setData(gson.toJson(contactTo));
        message.setEventType(WfmUiEventType.ON_PHONE_CALLED);
        RedisSocketObject redisSocketObject = new RedisSocketObject();
        redisSocketObject.setCompanyId(SecurityContext.getCompanyID());
        redisSocketObject.setWebSocketServerObject(message);
        RedisClient.publish(redisSocketObject);
    }

    private void callHangUp(String callId, String srcType, String timestamp, String callStartTimestamp, String callRecordLink, String status, String phoneNumber, Integer operatorId) {
        String redisKey = "sipuni:hangup:" + SecurityContext.getCompanyID() + ":" + callId;
        if (RedisClient.getKey(redisKey) != null) {
            log.info("Sipuni call_id {} already processed, skipping duplicate", callId);
            return;
        }
        RedisClient.setKey(redisKey, "1", 3600);

        EdsUser user = userManager.getUser();
        String textForSubject = srcType.equals("1") ? "Call from: " : "Call to: ";
        Appointment appointment = toAppointment(srcType, status, phoneNumber, user, textForSubject, operatorId);

        if (callStartTimestamp != null) {
            try {
                // TODO change timestamp parse logic
                long timestampSeconds = Long.parseLong(callStartTimestamp);
                long timestampMillis = timestampSeconds * 1000;

                Instant startInstant = Instant.ofEpochMilli(timestampMillis);
                appointment.setStartDate(Date.from(startInstant));

                long timestampSeconds1 = Long.parseLong(timestamp);
                long timestampMillis1 = timestampSeconds1 * 1000;

                Instant endInstant = Instant.ofEpochMilli(timestampMillis1);
                appointment.setEndDate(Date.from(endInstant));

                long diff = endInstant.toEpochMilli() - startInstant.toEpochMilli();
                long seconds = diff / 1000;
                appointment.setCallDuration(seconds);

            } catch (NumberFormatException e) {
                log.error("Error parsing start timestamp", e);
                // Handle the error as needed
            } catch (NullPointerException e) {
                log.error("Call start timestamp is null", e);
                // Handle the error as needed
            }
        }
//            datetime -> 2020-03-03,11:59:57
        //get details from crm, if no details found, just return phone number
        List<EdsCrmContact> contacts = crmContactManager.getAllByPhone(phoneNumber);
        List<EdsCrmAccount> accounts = crmAccountManager.getAllByPhone(phoneNumber);
        if (contacts.isEmpty() && accounts.isEmpty()) {
            allInOneServiceLocal.createContactFromCalls(phoneNumber, null);
            contacts = crmContactManager.getAllByPhone(phoneNumber);
        }

        if (contacts.isEmpty() && accounts.isEmpty()) {
            EdsCrmContact contact = new EdsCrmContact();
            contact.setFirstName("Sipuni:" + phoneNumber);
            contact.setOwner(user);
            contact.setPrimaryPhone(phoneNumber);
            EdsCrmContactItemParams itemParams = new EdsCrmContactItemParams();
            itemParams.setParam(1);
            itemParams.setRelation(2);
            itemParams.setValue(phoneNumber);
            crmContactItemParamsManager.createOrUpdate(itemParams);
            contact.setItemParams(new ArrayList<>(List.of(itemParams)));
            crmContactManager.createOrUpdate(contact);
            contacts.add(contact);
            try {
                contactSolrComponent.index(contact);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }

        ContactListItem item = new ContactListItem();
        ArrayList<ContactTO> contactTOS = new ArrayList<>();
        ArrayList<RelationItem> relations = new ArrayList<>();
        ContactTO contactTO = new ContactTO();
        contactTO.setPhone(phoneNumber);

        //edit mode
        if (appointment.getObjectID() != null && appointment.getObjectID() > 0) {
            try {
                relationManager.deleteAllRelations(RelationItem.TYPE_EVENT, appointment.getObjectID());
            } catch (Exception e) {
                log.error("Api error occurred while deleting event relations", e);
            }
        }
        if (contacts != null && !contacts.isEmpty()) {
            appointment.setSubject(textForSubject + contacts.get(0).getName() + "(" + phoneNumber + ")");
            for (EdsCrmContact contact : contacts) {
                contactTO = new ContactTO();
                contactTO.setPhone(phoneNumber);
                appointment.setDescription(appointment.getSubject());

                RelationItem relation = new RelationItem();
                relation.setFromType(RelationItem.TYPE_EVENT);
                relation.setToID(contact.getObjectID());
                if (contact.getContactType().equals(CrmConstants.TYPE_CRM_CONTACT)) {
                    relation.setToType(RelationItem.TYPE_CONTACT);
                } else if (contact.getContactType().equals(CrmConstants.TYPE_LEAD_CONTACT)) {
                    relation.setToType(RelationItem.TYPE_LEAD);
                } else if (contact.getContactType().equals(CrmConstants.TYPE_CANDIDATE)) {
                    relation.setToType(RelationItem.TYPE_CANDIDATE);
                }
                relation.setToName(contact.getName());
                if (relations.stream().noneMatch(x -> x.getToID().equals(relation.getToID()))) {
                    relations.add(relation);
                }

                List<EdsOpportunity> opportunities = opportunityManager.getOpportunityByCrmContactID(contact.getObjectID());
                if (opportunities != null && !opportunities.isEmpty()) {
                    for (EdsOpportunity opportunity : opportunities) {
                        RelationItem relationItem = new RelationItem();
                        relationItem.setFromID(appointment.getObjectID());
                        relationItem.setFromType(RelationItem.TYPE_EVENT);
                        relationItem.setToID(opportunity.getObjectID());
                        relationItem.setToType(RelationItem.TYPE_OPPORTUNITY.toUpperCase());
                        relationItem.setToName(opportunity.getName());
                        if (relations.stream().noneMatch(x -> x.getToID().equals(relationItem.getToID()))) {
                            relations.add(relationItem);
                        }
                    }
                }


                //Gather data to send thru socket to user
                contactTO.setItem_id(contact.getObjectID());
                contactTO.setFirst_name(contact.getFirstName() != null && !contact.getFirstName().isEmpty() && contact.getFirstName().contains("Sipuni") ? null : contact.getFirstName());
                contactTO.setLast_name(contact.getLastName());
                contactTO.setName(contact.getName() != null && !contact.getName().isEmpty() && contact.getName().contains("Sipuni") ? null : contact.getName());
                if (contact.getCrmAccount() != null) {
                    contactTO.setCompany(contactServiceLocal.convertCompany(contact.getCrmAccount()));

                    //Add Contacts company as relation
                    RelationItem contactsCompanyRelation = new RelationItem();
                    if (appointment.getObjectID() != null && appointment.getObjectID() > 0) {
                        contactsCompanyRelation.setFromID(appointment.getObjectID());
                    }
                    contactsCompanyRelation.setFromType(RelationItem.TYPE_EVENT);
                    contactsCompanyRelation.setToID(contact.getCrmAccount().getObjectID());
                    contactsCompanyRelation.setToType(RelationItem.TYPE_CRM_ACCOUNT);
                    contactsCompanyRelation.setToName(contact.getCrmAccount().getName());
                    if (relations.stream().noneMatch(x -> x.getToID().equals(contactsCompanyRelation.getToID()))) {
                        relations.add(contactsCompanyRelation);
                    }
                    contactTOS.add(contactTO);
                    item = contact.getRPC(new ListingFilterParameter(false), item);
                    if (!item.getWorkPhone().isEmpty()) {
                        item.getWorkPhone().set(0, phoneNumber);
                    } else {
                        item.getWorkPhone().add(phoneNumber);
                    }
                }
            }
            appointment.setRelations(relations);
        }

        if (accounts != null && !accounts.isEmpty()) {
            for (EdsCrmAccount account : accounts) {
                contactTO = new ContactTO();
                for (ContactTO contactT : contactTOS) {
                    if (contactTO.getItem_id() == null) {
                        appointment.setSubject(textForSubject + account.getName() + " (" + phoneNumber + ")");
                        appointment.setDescription(appointment.getSubject());
                        contactTO.setItem_id(account.getObjectID());
                        contactTO.setName(account.getName());
                    }
                    if (contactTO.getCompany() == null) {
                        contactTO.setCompany(contactServiceLocal.convertCompany(account));
                    }

                    RelationItem relation = new RelationItem();
                    relation.setFromType(RelationItem.TYPE_EVENT);
                    relation.setToID(account.getObjectID());
                    relation.setToType(RelationItem.TYPE_CRM_ACCOUNT);
                    relation.setToName(account.getName());
                    if (relations.stream().noneMatch(x -> x.getToID().equals(relation.getToID()))) {
                        relations.add(relation);
                    }

                }
            }
            appointment.setRelations(relations);
            int candidateCount = 0;
            for (RelationItem relationItem : appointment.getRelations()) {
                if (RelationItem.TYPE_CANDIDATE.equals(relationItem.getToType())) {
                    candidateCount++;
                }
            }
            if (candidateCount == appointment.getRelations().size() && candidateCount > 0) {
                appointment.setCreatedFrom(Appointment.FROM_HRMS);
            } else if (candidateCount > 0 && !appointment.getRelations().isEmpty()) {
                appointment.setCreatedFrom(Appointment.FROM_BOTH);
            }
            appointment.setPhoneNumber(phoneNumber);

        }

        try {
            SelectItem result = googleCalendarServiceLocal.saveCalendarEvent(null, appointment, false);

            ByteArrayOutputStream outputStream;
            URL url = new URL(callRecordLink);
            try (InputStream inputStream = url.openStream()) {
                outputStream = new ByteArrayOutputStream();

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

            }


            log.info("Save Sipuni Call Log result:{} {}", result.getId(), result.getName());
            String filename = callId + ".mp3";
            MultipartFile multipartFile = new MockMultipartFile(filename, filename, "audio/mpeg", outputStream.toByteArray());
            FolderResource folderResource = documentsServiceLocal.getFolderResource(F_EVENT, null);
            documentsServiceLocal.saveDocumentFile(multipartFile, folderResource.getObjectId(), F_EVENT, result.getId(), null);

            if (appointment.getObjectID() != null) {
                eventManager.addToSolr(appointment.getObjectID());
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private static Appointment toAppointment(String srcType, String status, String phoneNumber, EdsUser user, String textForSubject, Integer operatorId) {
        Appointment appointment = new Appointment();
        appointment.setActivityType(Appointment.CALL_LOG);
        appointment.setCreatedFrom(Appointment.FROM_CRM);

        appointment.setStyle(Appointment.AQUA);
        appointment.setAllDay(Boolean.FALSE);
        appointment.setCreatedBy(user.getName());

        appointment.setInboundCall(srcType.equals("1"));
        appointment.setOutboundCall(srcType.equals("2"));
        appointment.setSubject(textForSubject + phoneNumber);
        appointment.setDescription(appointment.getSubject());
        appointment.setComplatedCall(true);
        appointment.setMissedCall(status.equals("NOANSWER"));
        if (operatorId != null) {
            Attendee attendee = new Attendee(operatorId, true);
            appointment.setAttendees(new ArrayList<>(List.of(attendee)));
        }
        return appointment;
    }
}
