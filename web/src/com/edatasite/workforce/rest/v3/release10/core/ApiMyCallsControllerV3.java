package com.edatasite.workforce.rest.v3.release10.core;


import com.edatasite.workforce.core.domain.EdsMyCallsSettings;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.solr.component.CrmAccountSolrComponent;
import com.edatasite.workforce.core.solr.component.OpportunitySolrComponent;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactTo;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
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
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.MyCallsSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceLocal;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.WebhookEventDTO;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

@Tag(name = "My Calls Webhook Handler")
@RestController
@RequestMapping(value = "/mycalls",
        produces = {MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiMyCallsControllerV3 extends BaseApiControllerV3 {

    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyCallsSettingsManager myCallsSettingsManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
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
    private ContactSolrComponent contactSolrComponent;
    @Autowired
    private CrmAccountSolrComponent crmAccountSolrComponent;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CrmContactItemParamsManager crmContactItemParamsManager;
    @Autowired
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private OpportunitySolrComponent opportunitySolrComponent;
    @Autowired
    private CrmServiceLocal crmServiceLocal;


    private static final Logger log = LoggerFactory.getLogger(ApiSipuniControllerV3.class);

    @Operation(summary = "Event Handler")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "My Calls Webhook"))
    @RequestMapping(path = "/webhook/{clusterType}", method = RequestMethod.POST)
    @Transactional
    public ResultTO<Boolean> webhook(@PathVariable final String clusterType, @RequestBody WebhookEventDTO dto) {

        try {
            SecurityContext.getInstance().setDatabase(clusterType);
            EdsMyCallsSettings myCallsSettings = null;

            if (dto.getWebhook() != null && dto.getWebhook().getUser_login() != null) {
                myCallsSettings = myCallsSettingsManager.getMyCallsSettingsBySipNumber(dto.getWebhook().getUser_login());
            }
            if (myCallsSettings != null && myCallsSettings.getOperator() != null) {
                SecurityContext.getInstance().setCompanyId(myCallsSettings.getCompanyId());
                SecurityContext.getInstance().setStaticUserID(myCallsSettings.getOperator().getObjectID());
            }
            String phoneNumber = dto.getEvent() != null ? dto.getEvent().getClient_number() : null;

            if (myCallsSettings == null) {
                log.error("My Call Settings is null");
                return ResultTO.success(false);
            }

            if (dto.getEvent().getEvent_type() == 1) {
                List<EdsCrmContact> contacts = crmContactManager.getAllByPhone(phoneNumber);
                List<EdsCrmAccount> accounts = crmAccountManager.getAllByPhone(phoneNumber);
                if (contacts.isEmpty() && accounts.isEmpty()) {
                    allInOneServiceLocal.createContactFromCalls(phoneNumber, dto.getEvent().getClient_name());
                }
            }

            if (dto.getEvent().getEvent_type() == 2){
                List<EdsCrmContact> contacts = crmContactManager.getAllByPhone(phoneNumber);
                List<EdsCrmAccount> accounts = crmAccountManager.getAllByPhone(phoneNumber);
                if (dto.getEvent().getClient_name() != null && !dto.getEvent().getClient_name().isEmpty()){
                    if (contacts != null && contacts.size() > 0 ){
                        EdsCrmContact contact = contacts.get(0);
                        contact.setFirstName(dto.getEvent().getClient_name());
                        crmContactManager.createOrUpdate(contact);
                        contactSolrComponent.index(contact);
                    }
                    if (accounts != null && accounts.size() > 0){
                        EdsCrmAccount account = accounts.get(0);
                        account.setName(dto.getEvent().getClient_name());
                        crmAccountManager.createOrUpdate(account);
                        crmAccountSolrComponent.index(account);
                    }
                }

                ContactTo contactTo = null;
                EdsCrmContact contact = contacts != null ? contacts.get(0) : null;
                EdsCrmAccount account = accounts != null ? accounts.get(0) : null;
                contactTo = new ContactTo();
                contactTo.setPhone(phoneNumber);
                contactTo.setItem_id(contact != null ? contact.getObjectID() : account.getObjectID());
                contactTo.setFirst_name(contact != null  ? contact.getFirstName() : account.getName());
                contactTo.setLast_name(contact != null ? contact.getLastName() : null);
                contactTo.setName(contact != null ? contact.getName() : account.getName());
                contactTo.setContactType(contact != null ? String.valueOf(contact.getContactType()) : String.valueOf(CrmConstants.TYPE_ACCOUNT));

                try {
                    Integer user = userManager.getUser().getObjectID();
                    WebSocketServerObject message = new WebSocketServerObject();
                    message.setUserId(user);
                    message.setData(new Gson().toJson(contactTo));
                    message.setEventType(WfmUiEventType.ON_PHONE_CALLED);
                    RedisSocketObject redisSocketObject = new RedisSocketObject();
                    redisSocketObject.setCompanyId(Integer.parseInt(SecurityContext.getInstance().getCompanyId()));
                    redisSocketObject.setWebSocketServerObject(message);
                    RedisClient.publish(redisSocketObject);
                } catch (NumberFormatException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (dto.getEvent().getEvent_type() == 4) {
                EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();


                Appointment appointment = new Appointment();
                appointment.setActivityType(Appointment.CALL_LOG);
                appointment.setCreatedFrom(Appointment.FROM_CRM);

                appointment.setStyle(Appointment.AQUA);
                appointment.setAllDay(Boolean.FALSE);
                appointment.setCreatedBy(userManager.getUser().getName());

                appointment.setInboundCall(dto.getEvent().getDirection() == 0);
                appointment.setOutboundCall(dto.getEvent().getDirection() == 1);
                String textForSubject = dto.getEvent().getDirection() == 0 ? "Call from: " : "Call to: ";
                appointment.setSubject(textForSubject + phoneNumber);
                appointment.setDescription(appointment.getSubject());
                appointment.setComplatedCall(true);
                appointment.setMissedCall(dto.getEvent().getAnswered() == 0);
                Integer assigneeId = eventManager.getUser().getObjectID();
                appointment.setOwnerID(SecurityContext.getInstance().getStaticUserID());
                if (myCallsSettings != null && myCallsSettings.getOperator() != null) {
                    assigneeId = myCallsSettings.getOperator().getObjectID();
                    ArrayList<Attendee> attendees = new ArrayList<>();
                    Attendee attendee = new Attendee();
                    attendee.setID(assigneeId);
                    attendees.add(attendee);
                    appointment.setAttendees(attendees);
                }

                try {
                    long timestampSeconds = dto.getEvent().getAnswer_time() != 0 ? dto.getEvent().getAnswer_time() : dto.getEvent().getStart_time();
                    long timestampMillis = timestampSeconds * 1000;

                    Instant startInstant = Instant.ofEpochMilli(timestampMillis);
                    appointment.setStartDate(Date.from(startInstant));

                    long timestampSeconds1 = dto.getEvent().getEnd_time();
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

//            datetime -> 2020-03-03,11:59:57
                //get details from crm, if no details found, just return phone number
                List<EdsCrmContact> contacts = crmContactManager.getAllByPhone(phoneNumber);
                List<EdsCrmAccount> accounts = crmAccountManager.getAllByPhone(phoneNumber);


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
                    appointment.setSubject(textForSubject + contacts.get(0).getName());
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
                        if (opportunities != null && opportunities.size() > 0) {
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
                        contactTO.setFirst_name( contact.getFirstName());
                        contactTO.setLast_name(contact.getLastName());
                        contactTO.setName( contact.getName());
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
                            if (item.getWorkPhone().size() > 0) {
                                item.getWorkPhone().set(0, phoneNumber);
                            } else {
                                item.getWorkPhone().add(phoneNumber);
                            }
                        }
                    }
                    appointment.setRelations(relations);
                }

                if (accounts != null && !accounts.isEmpty()) {
                    appointment.setSubject(textForSubject + accounts.get(0).getName());
                    for (EdsCrmAccount account : accounts) {
                        contactTO = new ContactTO();
                        contactTO.setPhone(phoneNumber);
                        appointment.setDescription(appointment.getSubject());

                        RelationItem relation = new RelationItem();
                        relation.setFromType(RelationItem.TYPE_EVENT);
                        relation.setToID(account.getObjectID());
                        relation.setToName(account.getName());
                        relation.setToType(RelationItem.TYPE_CRM_ACCOUNT);
                        if (relations.stream().noneMatch(x -> x.getToID().equals(relation.getToID()))) {
                            relations.add(relation);
                        }

                        List<EdsOpportunity> opportunities = opportunityManager.getOpportunityByCrmAccountID(account.getObjectID());
                        if (opportunities != null && opportunities.size() > 0) {
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
                        } else {
                            EdsOpportunity opportunity = new EdsOpportunity();
                            opportunity.setName(account.getName());
                            opportunity.setAssignee(employeeManager.get(account.getOwners().get(0).getObjectID()));
                            opportunity.setStage(companySettings.getOpportunityStageId() != null  ? referenceManager.get(companySettings.getOpportunityStageId()) : null);
                            opportunity.setLeadSource(companySettings.getOpportunitySourceId() != null  ? referenceManager.get(companySettings.getOpportunitySourceId()) : null);
                            opportunity.setCrmAccount(account);
                            NumberData numberData = crmServiceLocal.generateOpportunityNumber();
                            opportunity.setNumber(numberData.getNumberString());
                            opportunity.setIntNumber(numberData.getIntNumber());

                            opportunityManager.create(opportunity);
                            RelationItem relationItem = new RelationItem();
                            relationItem.setFromID(appointment.getObjectID());
                            relationItem.setFromType(RelationItem.TYPE_EVENT);
                            relationItem.setToID(opportunity.getObjectID());
                            relationItem.setToType(RelationItem.TYPE_OPPORTUNITY.toUpperCase());
                            relationItem.setToName(opportunity.getName());
                            if (relations.stream().noneMatch(x -> x.getToID().equals(relationItem.getToID()))) {
                                relations.add(relationItem);
                            }
                            try {
                                opportunitySolrComponent.index(opportunity);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }


                        //Gather data to send thru socket to user
                        contactTO.setItem_id(account.getObjectID());
                        contactTO.setFirst_name( account.getName());
                        contactTO.setName( account.getName());

                    }
                    appointment.setRelations(relations);

                }

                try {
                    SelectItem result = googleCalendarServiceLocal.saveCalendarEvent(appointment.getOwnerID(), appointment, false);

                    if (dto.getEvent().getRecording() != null && !dto.getEvent().getRecording().isEmpty()){
                        ByteArrayOutputStream outputStream;
                        URL url = new URL(dto.getEvent().getRecording());
                        try (InputStream inputStream = url.openStream()) {
                            outputStream = new ByteArrayOutputStream();

                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }

                        }
                        log.info("Save My Calls Log result:" + result.getId() + " " + result.getName());
                        MultipartFile multipartFile = new MockMultipartFile(dto.getEvent().getDb_call_id() + ".mp3", dto.getEvent().getDb_call_id() + ".mp3", "audio/mpeg", outputStream.toByteArray());
                        FolderResource folderResource = documentsServiceLocal.getFolderResource(F_EVENT, null);
                        documentsServiceLocal.saveDocumentFile(multipartFile, folderResource.getObjectId(), F_EVENT, result.getId(), null);
                    }


                    if (appointment.getObjectID() != null) {
                        eventManager.addToSolr(appointment.getObjectID());
                    }
                } catch (Exception e) {
                    log.error("", e);
                }


            }


            return ResultTO.success(true);
        } catch (Exception e) {
            log.error("Error processing webhook", e);
            return ResultTO.success(false);
        }
    }
}
