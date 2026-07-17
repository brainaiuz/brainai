package com.edatasite.workforce.rest.v2.release10.crm;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager;
import com.edatasite.workforce.rest.base.enums.ApiActionEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.FilteredStatusItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.StageHistoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.StatusHistoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.ColorTO;
import com.edatasite.workforce.rest.v2.release10.enums.EntityTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderByEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderFieldEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Dilshod Madrahimov on 02/26/2018.
 */
@Tag(name = "Status History", description = "Status History API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiStatusHistoryControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiStatusHistoryControllerV2.class);

    private final SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;


    @Operation(summary = "Retreive Statuses change history")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have status  history"),
            @ApiResponse(responseCode = "400", description = "id is required")})
    @RequestMapping(value = "/history/statuses/{main_entity_path}/{entity_id}/changes", method = RequestMethod.GET)
    public Object getStatusHistory(@PathVariable(value = "main_entity_path") String entityType,
                                   @PathVariable(value = "entity_id") Integer entityId,
                                   @RequestParam(value = "sorting", required = false) String sorting,
                                   @RequestParam(value = "direction", required = false) String direction,
                                   @RequestParam(value = "limit", required = false) Integer limit) throws RestException {

        if (StringUtils.isBlank(entityType)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_path is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (entityId == null || entityId <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "entity_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ArrayList<StatusHistoryTO> statusHistoryList = new ArrayList<>();
        StatusHistoryTO statusHistory;

        if (EntityTypeEnum.OPPORTUNITIES.name().equalsIgnoreCase(entityType)) {
            statusHistoryList = getOpportunityUpdatesHistory(entityId);
        } else if (EntityTypeEnum.LEADS.name().equalsIgnoreCase(entityType) || EntityTypeEnum.CONTACTS.name().equalsIgnoreCase(entityType)) {
            statusHistoryList = getContactLeadUpdatesHistory(entityId, EntityTypeEnum.LEADS.name().equalsIgnoreCase(entityType));
        } else if (EntityTypeEnum.COMPANIES.name().equalsIgnoreCase(entityType)) {
            statusHistory = getCrmAccountUpdatesHistory(entityId);
            if (statusHistory != null) {
                statusHistoryList.add(statusHistory);
            }
        } else if (EntityTypeEnum.ACTIVITIES.name().equalsIgnoreCase(entityType)) {
            statusHistory = getActivityUpdatesHistory(entityId);
            if (statusHistory != null) {
                statusHistoryList.add(statusHistory);
            }
        } else if (EntityTypeEnum.TASKS.name().equalsIgnoreCase(entityType)) {
            statusHistory = getTaskUpdatesHistory(entityId);
            if (statusHistory != null) {
                statusHistoryList.add(statusHistory);
            }
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_path should be one of | leads | opportunities | tasks | companies | contacts | activities", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }


        if (statusHistoryList != null && statusHistoryList.size() > 0) {
            //Sorting
            if (StringUtils.isBlank(sorting) || OrderFieldEnum.DATE.getField().equalsIgnoreCase(sorting)) {
                statusHistoryList.sort((o1, o2) -> {
                    if (OrderByEnum.ASC.name().equalsIgnoreCase(direction) || StringUtils.isBlank(direction)) {
                        return o1.getUpdateDate().compareTo(o2.getUpdateDate());
                    } else {
                        return o2.getUpdateDate().compareTo(o1.getUpdateDate());
                    }
                });
            } else if (OrderFieldEnum.ID.getField().equalsIgnoreCase(sorting)) {
                statusHistoryList.sort((o1, o2) -> {
                    if (OrderByEnum.ASC.name().equalsIgnoreCase(direction) || StringUtils.isBlank(direction)) {
                        return o1.getStatus() != null && o2.getStatus() != null ? o1.getStatus().getStatus_id().compareTo(o2.getStatus().getStatus_id()) : -1;
                    } else {
                        return o1.getStatus() != null && o2.getStatus() != null ? o2.getStatus().getStatus_id().compareTo(o1.getStatus().getStatus_id()) : -1;
                    }
                });
            } else if (OrderFieldEnum.NAME.getField().equalsIgnoreCase(sorting)) {
                statusHistoryList.sort((o1, o2) -> {
                    if (OrderByEnum.ASC.name().equalsIgnoreCase(direction) || StringUtils.isBlank(direction)) {
                        return o1.getStatus() != null && o2.getStatus() != null ? o1.getStatus().getStatus_name().compareTo(o2.getStatus().getStatus_name()) : -1;

                    } else {
                        return o1.getStatus() != null && o2.getStatus() != null ? o2.getStatus().getStatus_name().compareTo(o1.getStatus().getStatus_name()) : -1;
                    }
                });
            }

            if (limit != null) {
                return successResponse(new ResponseListData<>(ListUtils.getSublistSmart(statusHistoryList, 0, limit)));
            }
        }

        return successResponse(new ResponseListData<>(statusHistoryList));
    }

    private ArrayList<StatusHistoryTO> getOpportunityUpdatesHistory(Integer entityId) throws RestException {

        EdsOpportunity edsOpportunity = opportunityManager.get(entityId);
        if (edsOpportunity == null || edsOpportunity.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Opportunity with id " + entityId + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        ArrayList<StatusHistoryTO> statusHistoryList = new ArrayList<>();
        //Opportunity status history
        edsOpportunity.getSubOpportunities().sort(Comparator.comparing(EdsOpportunity::getObjectID));

        if (edsOpportunity.getSubOpportunities() != null && edsOpportunity.getSubOpportunities().size() > 0) {
            int index = 0;
            Integer oldStatusId = edsOpportunity.getSubOpportunities().get(0).getStage() != null ? edsOpportunity.getSubOpportunities().get(0).getStage().getObjectID() : null;
            for (EdsOpportunity subOpportunity : edsOpportunity.getSubOpportunities()) {
                StatusHistoryTO statusHistory = new StatusHistoryTO();

                if (index == 0) {
                    statusHistory.setApplied_action(ApiActionEnum.CREATED.getCode());
                } else if (oldStatusId != null && subOpportunity.getStage() != null && oldStatusId.equals(subOpportunity.getStage().getObjectID())) {
                    statusHistory.setApplied_action(ApiActionEnum.UPDATED.getCode());
                } else {
                    statusHistory.setApplied_action(ApiActionEnum.MOVED.getCode());
                }

                if (subOpportunity.getStage() != null) {
                    FilteredStatusItemTO status = new FilteredStatusItemTO();
                    status.setStatus_id(subOpportunity.getStage().getObjectID());
                    status.setStatus_name(subOpportunity.getStage().getName());
                    status.setIs_system(subOpportunity.getStage().isSystemReference());
                    status.setOrder_id(subOpportunity.getStage().getSorder());
                    if (subOpportunity.getStage().getReferenceColor() != null) {
                        ColorTO color = new ColorTO();
                        color.setId(subOpportunity.getStage().getReferenceColor().getObjectID());
                        color.setName(subOpportunity.getStage().getReferenceColor().getName());
                        color.setHex(subOpportunity.getStage().getReferenceColor().getHex());
                        status.setStatus_color(color);
                    }
                    statusHistory.setStatus(status);
                } else {
                    statusHistory.setStatus(getDefaultStatus());
                }
                if (subOpportunity.getAuditInfo() != null) {
                    if (subOpportunity.getAuditInfo().getModificationDate() != null) {
                        statusHistory.setDate(longDateTimezoneFormat.format(subOpportunity.getAuditInfo().getModificationDate()));
                    }
                    statusHistory.setUpdateDate(subOpportunity.getAuditInfo().getModificationDate());
                    if (subOpportunity.getAuditInfo().getModifiedBy() != null) {
                        statusHistory.setModifier(getModifier(subOpportunity.getAuditInfo().getModifiedBy()));
                    }
                }

                statusHistoryList.add(statusHistory);
                index++;
                oldStatusId = subOpportunity.getStage() != null ? subOpportunity.getStage().getObjectID() : null;
            }
        } else {
            StatusHistoryTO statusHistory = new StatusHistoryTO();
            statusHistory.setApplied_action(ApiActionEnum.CREATED.getCode());
            if (edsOpportunity.getStage() != null) {
                FilteredStatusItemTO status = new FilteredStatusItemTO();
                status.setStatus_id(edsOpportunity.getStage().getObjectID());
                status.setStatus_name(edsOpportunity.getStage().getName());
                status.setIs_system(edsOpportunity.getStage().isSystemReference());
                status.setOrder_id(edsOpportunity.getStage().getSorder());
                if (edsOpportunity.getStage().getReferenceColor() != null) {
                    ColorTO color = new ColorTO();
                    color.setId(edsOpportunity.getStage().getReferenceColor().getObjectID());
                    color.setName(edsOpportunity.getStage().getReferenceColor().getName());
                    color.setHex(edsOpportunity.getStage().getReferenceColor().getHex());
                    status.setStatus_color(color);
                }
                statusHistory.setStatus(status);
            } else {
                statusHistory.setStatus(getDefaultStatus());
            }
            if (edsOpportunity.getAuditInfo() != null) {
                if (edsOpportunity.getAuditInfo().getCreationDate() != null) {
                    statusHistory.setDate(longDateTimezoneFormat.format(edsOpportunity.getAuditInfo().getCreationDate()));
                    statusHistory.setUpdateDate(edsOpportunity.getAuditInfo().getCreationDate());
                }
                statusHistory.setModifier(getModifier(edsOpportunity.getAuditInfo().getCreatedBy()));
            }

            statusHistoryList.add(statusHistory);
        }

        return statusHistoryList;
    }


    private ArrayList<StatusHistoryTO> getContactLeadUpdatesHistory(Integer entityId, boolean isLead) throws RestException {

        EdsCrmContact edsCrmContact = crmContactManager.get(entityId);
        if (edsCrmContact == null || edsCrmContact.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, (isLead ? "Lead" : "Contact").concat(" with id " + entityId + " is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        ArrayList<StatusHistoryTO> statusHistoryList = new ArrayList<>();
        EdsMyUpdate edsMyUpdate;
        try {
            edsMyUpdate = myUpdateManager.getUpdate(edsCrmContact.getObjectID(), isLead ? MyUpdateTypeManager.LEAD_ADD : MyUpdateTypeManager.CONTACT_ADD);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //Created log
        if (edsMyUpdate != null) {
            StatusHistoryTO statusHistory = new StatusHistoryTO();
            //status for lead
            if (isLead) {
                if (StringUtils.isNotBlank(edsMyUpdate.getStatusCode())) {
                    EdsReference edsReference = referenceManager.getByCode(edsMyUpdate.getStatusCode());
                    if (edsReference != null) {
                        FilteredStatusItemTO status = new FilteredStatusItemTO();
                        status.setStatus_id(edsReference.getObjectID());
                        status.setStatus_name(edsReference.getName());
                        if (edsReference.getReferenceColor() != null) {
                            ColorTO color = new ColorTO();
                            color.setId(edsReference.getReferenceColor().getObjectID());
                            color.setName(edsReference.getReferenceColor().getName());
                            color.setHex(edsReference.getReferenceColor().getHex());
                            status.setStatus_color(color);
                        }
                        status.setOrder_id(edsReference.getSorder());
                        status.setIs_system(edsReference.isSystemReference());
                        statusHistory.setStatus(status);
                    } else {
                        statusHistory.setStatus(getDefaultStatus());
                    }
                } else {
                    statusHistory.setStatus(getDefaultStatus());
                }
            }

            //applied_action
            statusHistory.setApplied_action(ApiActionEnum.CREATED.getCode());

            //modifier
            if (edsMyUpdate.getInducerID() != null) {
                statusHistory.setModifier(getModifier(userManager.get(edsMyUpdate.getInducerID())));
            }
            //date
            if (edsMyUpdate.getDate() != null) {
                statusHistory.setDate(longDateTimezoneFormat.format(edsMyUpdate.getDate()));
                statusHistory.setUpdateDate(edsMyUpdate.getDate());
            }

            statusHistoryList.add(statusHistory);

        } else {
            StatusHistoryTO statusHistory = new StatusHistoryTO();
            //status for lead
            if (isLead) {
                /*EdsReference edsReference = edsCrmContact.getLeadStatus();
                if (edsReference != null) {
                    FilteredStatusItemTO status = new FilteredStatusItemTO();
                    status.setStatus_id(edsReference.getObjectID());
                    status.setStatus_name(edsReference.getName());
                    if (edsReference.getReferenceColor() != null) {
                        ColorTO color = new ColorTO();
                        color.setId(edsReference.getReferenceColor().getObjectID());
                        color.setName(edsReference.getReferenceColor().getName());
                        color.setHex(edsReference.getReferenceColor().getHex());
                        status.setStatus_color(color);
                    }
                    status.setOrder_id(edsReference.getSorder());
                    status.setIs_system(edsReference.isSystemReference());
                    statusHistory.setStatus(status);
                } else {
                    statusHistory.setStatus(getDefaultStatus());
                }*/
                statusHistory.setStatus(getDefaultStatus());
            }

            //applied_action
            statusHistory.setApplied_action(ApiActionEnum.CREATED.getCode());

            //modifier
            if (edsCrmContact.getAuditInfo().getCreatedBy() != null) {
                statusHistory.setModifier(getModifier(edsCrmContact.getAuditInfo().getCreatedBy()));
            }

            //date
            if (edsCrmContact.getAuditInfo().getCreationDate() != null) {
                statusHistory.setDate(longDateTimezoneFormat.format(edsCrmContact.getAuditInfo().getCreationDate()));
                statusHistory.setUpdateDate(edsCrmContact.getAuditInfo().getCreationDate());
            }

            statusHistoryList.add(statusHistory);
        }

        //Status change history logs
        try {
            ContactListItem[] contactListItems = contactServiceLocal.getStatusHistory(edsCrmContact.getObjectID(), isLead ? CrmConstants.TYPE_LEAD_CONTACT : CrmConstants.TYPE_CRM_CONTACT, !isLead);
            if (contactListItems != null) {
                for (ContactListItem item : contactListItems) {
                    StatusHistoryTO statusHistory = new StatusHistoryTO();
                    if (item.getUpdatedDate() != null) {
                        statusHistory.setDate(longDateTimezoneFormat.format(item.getUpdatedDate()));
                        statusHistory.setUpdateDate(item.getUpdatedDate());
                    }
                    if (isLead) {
                        if (item.getLeadStatus() != null) {
                            EdsReference edsReference = referenceManager.get(item.getLeadStatus().getObjectID());
                            if (edsReference != null) {
                                FilteredStatusItemTO status = new FilteredStatusItemTO();
                                status.setStatus_id(edsReference.getObjectID());
                                status.setStatus_name(edsReference.getName());
                                status.setOrder_id(edsReference.getSorder());
                                status.setIs_system(edsReference.isSystemReference());
                                if (edsReference.getReferenceColor() != null) {
                                    ColorTO color = new ColorTO();
                                    color.setId(edsReference.getReferenceColor().getObjectID());
                                    color.setName(edsReference.getReferenceColor().getName());
                                    color.setHex(edsReference.getReferenceColor().getHex());
                                    status.setStatus_color(color);
                                }

                                statusHistory.setStatus(status);
                            } else {
                                statusHistory.setStatus(getDefaultStatus());
                            }
                        } else {
                            statusHistory.setStatus(getDefaultStatus());
                        }
                    }

                    statusHistory.setApplied_action(ApiActionEnum.MOVED.getCode());
                    if (item.getOwnerId() != null) {
                        statusHistory.setModifier(getModifier(userManager.get(item.getOwnerId())));
                    }

                    statusHistoryList.add(statusHistory);
                }
            }
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return statusHistoryList;
    }


    private StatusHistoryTO getCrmAccountUpdatesHistory(Integer entityId) throws RestException {
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(entityId);
        if (edsCrmAccount == null || edsCrmAccount.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Company with id " + entityId + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        EdsMyUpdate edsMyUpdate;
        try {
            edsMyUpdate = myUpdateManager.getUpdate(edsCrmAccount.getObjectID(), MyUpdateTypeManager.CRM_ACCOUNT_ADD);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        StatusHistoryTO statusHistory = new StatusHistoryTO();
        //applied_action
        statusHistory.setApplied_action(ApiActionEnum.CREATED.getCode());

        if (edsMyUpdate != null) {
            //modifier
            statusHistory.setModifier(getModifier(userManager.get(edsMyUpdate.getInducerID())));
            //date
            if (edsMyUpdate.getDate() != null) {
                statusHistory.setDate(longDateTimezoneFormat.format(edsMyUpdate.getDate()));
                statusHistory.setUpdateDate(edsMyUpdate.getDate());
            }

        } else {
            //modifier
            statusHistory.setModifier(getModifier(edsCrmAccount.getCreator()));
            //date
            if (edsCrmAccount.getCreationTime() != null) {
                statusHistory.setDate(longDateTimezoneFormat.format(edsCrmAccount.getCreationTime()));
                statusHistory.setUpdateDate(edsCrmAccount.getCreationTime());
            }
        }

        return statusHistory;
    }

    private StatusHistoryTO getActivityUpdatesHistory(Integer entityId) throws RestException {
        EdsEvent edsEvent = eventManager.get(entityId);
        if (edsEvent == null || edsEvent.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Activity with id " + entityId + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        EdsMyUpdate edsMyUpdate;
        try {
            edsMyUpdate = myUpdateManager.getUpdate(edsEvent.getObjectID(), MyUpdateTypeManager.EVENT_ADD);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        StatusHistoryTO statusHistory = new StatusHistoryTO();
        //applied_action
        statusHistory.setApplied_action(ApiActionEnum.CREATED.getCode());

        if (edsMyUpdate != null) {
            //modifier
            if (edsMyUpdate.getInducerID() != null) {
                statusHistory.setModifier(getModifier(userManager.get(edsMyUpdate.getInducerID())));
            }
            //date
            if (edsMyUpdate.getDate() != null) {
                statusHistory.setDate(longDateTimezoneFormat.format(edsMyUpdate.getDate()));
                statusHistory.setUpdateDate(edsMyUpdate.getDate());
            }
        } else {
            //modifier
            if (edsEvent.getCreatedFrom() != null) {
                statusHistory.setModifier(getModifier(userManager.get(edsEvent.getCreatedFrom())));
            }
            //date
            if (edsEvent.getCreationTime() != null) {
                statusHistory.setDate(longDateTimezoneFormat.format(edsEvent.getCreationTime()));
                statusHistory.setUpdateDate(edsEvent.getCreationTime());
            }
        }

        return statusHistory;

    }

    private StatusHistoryTO getTaskUpdatesHistory(Integer entityId) throws RestException {

        EdsTask edsTask = taskManager.get(entityId);

        if (edsTask == null || edsTask.getDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Task with id " + entityId + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        StatusHistoryTO statusHistory = new StatusHistoryTO();
        //status
        if (edsTask.getStatus() != null) {
            FilteredStatusItemTO status = new FilteredStatusItemTO();
            status.setStatus_id(edsTask.getStatus().getObjectID());
            status.setStatus_name(edsTask.getStatus().getName());
            if (edsTask.getStatus().getReferenceColor() != null) {
                ColorTO color = new ColorTO();
                color.setId(edsTask.getStatus().getReferenceColor().getObjectID());
                color.setName(edsTask.getStatus().getReferenceColor().getName());
                color.setHex(edsTask.getStatus().getReferenceColor().getHex());
                status.setStatus_color(color);
            }
            status.setOrder_id(edsTask.getStatus().getSorder());
            status.setIs_system(edsTask.getStatus().isSystemReference());
            statusHistory.setStatus(status);
        } else {
            statusHistory.setStatus(getDefaultStatus());
        }

        //applied_action
        statusHistory.setApplied_action(ApiActionEnum.CREATED.getCode());

        //modifier
        statusHistory.setModifier(getModifier(edsTask.getCreator()));

        //date
        if (edsTask.getCreationTime() != null) {
            statusHistory.setDate(longDateTimezoneFormat.format(edsTask.getCreationTime()));
            statusHistory.setUpdateDate(edsTask.getCreationTime());
        }


        return statusHistory;
    }

    private ContactTO getModifier(EdsUser edsUser) {
        if (edsUser != null) {
            ContactTO modifier = new ContactTO();
            modifier.setItem_id(edsUser.getObjectID());
            modifier.setName(edsUser.getName());
            if (edsUser.getPhoto() != null) {
                modifier.setAvatar_image(commonServiceLocal.getImageUrl(edsUser.getPhoto().getObjectID()));
            }
            if (edsUser instanceof EdsEmployee employee) {
                if (employee.getContact() != null) {
                    ContactsTO contactsTO = new ContactsTO();

                    //Phones
                    contactsTO.setPhones(contactServiceLocal.convertToPhoneTO(employee.getContact()));

                    //Emails
                    contactsTO.setEmails(contactServiceLocal.convertContactEmails(employee.getContact()));

                    modifier.setContacts(contactsTO);

                    //Company
                    modifier.setCompany(contactServiceLocal.convertCompany(employee.getContact().getCrmAccount()));
                }
            }
            return modifier;
        }
        return null;
    }


    @Operation(summary = "Stage History for items")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have status history"),
            @ApiResponse(responseCode = "400", description = "id is required")})
    @RequestMapping(value = "/history/stages/{main_entity_path}/{entity_id}/changes", method = RequestMethod.GET)
    public Object getStageStatusHistory(@PathVariable(value = "main_entity_path") String entityType,
                                        @PathVariable(value = "entity_id") Integer entityId,
                                        @RequestParam(value = "sorting", required = false) String sorting,
                                        @RequestParam(value = "direction", required = false) String direction,
                                        @RequestParam(value = "limit", required = false) Integer limit) throws RestException {

        if (StringUtils.isBlank(entityType)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_path is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (entityId == null || entityId <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "entity_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (!EntityTypeEnum.OPPORTUNITIES.name().equalsIgnoreCase(entityType)) {//It's works for only opportunities
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_path should be opportunities", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsOpportunity edsOpportunity = opportunityManager.get(entityId);
        if (edsOpportunity == null || edsOpportunity.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Opportunity with id " + entityId + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        String baseCurrency = currencyServiceLocal.getBaseCurrency().getName();
        ArrayList<StageHistoryTO> stageHistoryList = new ArrayList<>();

        //Opportunity status history
        edsOpportunity.getSubOpportunities().sort(Comparator.comparing(EdsOpportunity::getObjectID));

        if (edsOpportunity.getSubOpportunities() != null && edsOpportunity.getSubOpportunities().size() > 0) {
            int index = 0;
            Integer oldStatusId = edsOpportunity.getSubOpportunities().get(0).getStage() != null ? edsOpportunity.getSubOpportunities().get(0).getStage().getObjectID() : null;
            for (EdsOpportunity subOpportunity : edsOpportunity.getSubOpportunities()) {

                StageHistoryTO statusHistory = new StageHistoryTO();
                statusHistory.setName(subOpportunity.getName());
                if (index == 0) {
                    statusHistory.setAction(ApiActionEnum.CREATED.getCode());
                } else if (oldStatusId != null && subOpportunity.getStage() != null && oldStatusId.equals(subOpportunity.getStage().getObjectID())) {
                    statusHistory.setAction(ApiActionEnum.UPDATED.getCode());
                } else {
                    statusHistory.setAction(ApiActionEnum.MOVED.getCode());
                }

                if (subOpportunity.getStage() != null) {
                    FilteredStatusItemTO status = new FilteredStatusItemTO();
                    status.setStatus_id(subOpportunity.getStage().getObjectID());
                    status.setStatus_name(subOpportunity.getStage().getName());
                    status.setIs_system(subOpportunity.getStage().isSystemReference());
                    status.setOrder_id(subOpportunity.getStage().getSorder());
                    if (subOpportunity.getStage().getReferenceColor() != null) {
                        ColorTO color = new ColorTO();
                        color.setId(subOpportunity.getStage().getReferenceColor().getObjectID());
                        color.setName(subOpportunity.getStage().getReferenceColor().getName());
                        color.setHex(subOpportunity.getStage().getReferenceColor().getHex());
                        status.setStatus_color(color);
                    }
                    statusHistory.setStatus(status);
                } else {
                    statusHistory.setStatus(getDefaultStatus());
                }

                if (subOpportunity.getAmount() != null) {
                    String currency = subOpportunity.getCurrency() != null ? subOpportunity.getCurrency().getName() : baseCurrency;
                    statusHistory.setAmount(new CurrencyValueTO((BigDecimal.valueOf(subOpportunity.getAmount()).setScale(2, RoundingMode.HALF_UP)), currency));
                }
                statusHistory.setProbability(subOpportunity.getProbability());
                if (subOpportunity.getExpectedRevenue() != null) {
                    statusHistory.setRevenue(BigDecimal.valueOf(subOpportunity.getExpectedRevenue()).setScale(2, RoundingMode.HALF_UP));
                }
                if (subOpportunity.getClosingDate() != null) {
                    statusHistory.setClosed_date(longDateTimezoneFormat.format(subOpportunity.getClosingDate()));
                }

                if (subOpportunity.getAuditInfo() != null) {
                    if (subOpportunity.getAuditInfo().getModificationDate() != null) {
                        statusHistory.setUpdated_date(longDateTimezoneFormat.format(subOpportunity.getAuditInfo().getModificationDate()));
                    }
                    statusHistory.setUpdateDate(subOpportunity.getAuditInfo().getModificationDate());
                    if (subOpportunity.getAuditInfo().getModifiedBy() != null) {
                        statusHistory.setModifier(getModifier(subOpportunity.getAuditInfo().getModifiedBy()));
                    }
                }

                stageHistoryList.add(statusHistory);
                index++;
                oldStatusId = subOpportunity.getStage() != null ? subOpportunity.getStage().getObjectID() : null;
            }
        } else {
            StageHistoryTO statusHistory = new StageHistoryTO();
            statusHistory.setAction(ApiActionEnum.CREATED.getCode());
            statusHistory.setName(edsOpportunity.getName());
            if (edsOpportunity.getStage() != null) {
                FilteredStatusItemTO status = new FilteredStatusItemTO();
                status.setStatus_id(edsOpportunity.getStage().getObjectID());
                status.setStatus_name(edsOpportunity.getStage().getName());
                status.setIs_system(edsOpportunity.getStage().isSystemReference());
                status.setOrder_id(edsOpportunity.getStage().getSorder());
                if (edsOpportunity.getStage().getReferenceColor() != null) {
                    ColorTO color = new ColorTO();
                    color.setId(edsOpportunity.getStage().getReferenceColor().getObjectID());
                    color.setName(edsOpportunity.getStage().getReferenceColor().getName());
                    color.setHex(edsOpportunity.getStage().getReferenceColor().getHex());
                    status.setStatus_color(color);
                }
                statusHistory.setStatus(status);
            } else {
                statusHistory.setStatus(getDefaultStatus());
            }

            if (edsOpportunity.getAmount() != null) {
                String currency = edsOpportunity.getCurrency() != null ? edsOpportunity.getCurrency().getName() : baseCurrency;
                statusHistory.setAmount(new CurrencyValueTO((BigDecimal.valueOf(edsOpportunity.getAmount()).setScale(2, RoundingMode.HALF_UP)), currency));
            }
            statusHistory.setProbability(edsOpportunity.getProbability());
            if (edsOpportunity.getExpectedRevenue() != null) {
                statusHistory.setRevenue(BigDecimal.valueOf(edsOpportunity.getExpectedRevenue()).setScale(2, RoundingMode.HALF_UP));
            }
            if (edsOpportunity.getClosingDate() != null) {
                statusHistory.setClosed_date(longDateTimezoneFormat.format(edsOpportunity.getClosingDate()));
            }

            if (edsOpportunity.getAuditInfo() != null) {
                if (edsOpportunity.getAuditInfo().getModificationDate() != null) {
                    statusHistory.setUpdated_date(longDateTimezoneFormat.format(edsOpportunity.getAuditInfo().getModificationDate()));
                }
                statusHistory.setUpdateDate(edsOpportunity.getAuditInfo().getModificationDate());
                if (edsOpportunity.getAuditInfo().getModifiedBy() != null) {
                    statusHistory.setModifier(getModifier(edsOpportunity.getAuditInfo().getModifiedBy()));
                }
            }

            stageHistoryList.add(statusHistory);
        }

        if (stageHistoryList.size() > 0) {
            //Sorting
            if (StringUtils.isBlank(sorting) || OrderFieldEnum.DATE.getField().equalsIgnoreCase(sorting)) {
                stageHistoryList.sort((o1, o2) -> {
                    if (OrderByEnum.ASC.name().equalsIgnoreCase(direction) || StringUtils.isBlank(direction)) {
                        return o1.getUpdateDate().compareTo(o2.getUpdateDate());
                    } else {
                        return o2.getUpdateDate().compareTo(o1.getUpdateDate());
                    }
                });
            } else if (OrderFieldEnum.ID.getField().equalsIgnoreCase(sorting)) {
                stageHistoryList.sort((o1, o2) -> {
                    if (OrderByEnum.ASC.name().equalsIgnoreCase(direction) || StringUtils.isBlank(direction)) {
                        return o1.getStatus() != null && o2.getStatus() != null ? o1.getStatus().getStatus_id().compareTo(o2.getStatus().getStatus_id()) : -1;
                    } else {
                        return o1.getStatus() != null && o2.getStatus() != null ? o2.getStatus().getStatus_id().compareTo(o1.getStatus().getStatus_id()) : -1;
                    }
                });
            } else if (OrderFieldEnum.NAME.getField().equalsIgnoreCase(sorting)) {
                stageHistoryList.sort((o1, o2) -> {
                    if (OrderByEnum.ASC.name().equalsIgnoreCase(direction) || StringUtils.isBlank(direction)) {
                        return o1.getStatus() != null && o2.getStatus() != null ? o1.getStatus().getStatus_name().compareTo(o2.getStatus().getStatus_name()) : -1;

                    } else {
                        return o1.getStatus() != null && o2.getStatus() != null ? o2.getStatus().getStatus_name().compareTo(o1.getStatus().getStatus_name()) : -1;
                    }
                });
            }

            if (limit != null) {
                return successResponse(new ResponseListData<>(ListUtils.getSublistSmart(stageHistoryList, 0, limit)));
            }
        }

        return successResponse(new ResponseListData<>(stageHistoryList));

    }


}
