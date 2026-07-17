package com.edatasite.workforce.rest.v2.release10.crm;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsFormProperty;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormSection;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsFacetFilter;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsUserFilter;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.actions.CreateAttachmentHandler;
import com.edatasite.workforce.gwt.core.server.actions.CreateDocumentCommand;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormSectionManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FormPropertyManager;
import com.edatasite.workforce.gwt.core.server.db.ModelFieldManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.FacetFilterManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.UserFilterManager;
import com.edatasite.workforce.gwt.core.server.servlets.WfmMultipartFile;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmAccountList;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.note.server.NoteServiceLocal;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.aspects.CheckPermissionException;
import com.edatasite.workforce.rest.base.enums.ContactParamEnum;
import com.edatasite.workforce.rest.base.enums.NoteEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ListingFilterHelper;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.ModelFieldLocalizer;
import com.edatasite.workforce.rest.v2.release10.core.service.crm.ApiLeadService;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CountriesListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.EntityCategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.FieldStateTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.BatchLeadImportRequest;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.BatchLeadItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ChangeLeadStatusRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactAddressAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EntityContactAddressListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EntityContactAddressTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EntityInformationResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.FilteredStatusItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ItemInStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.LeadAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.LeadConvertOpportunityTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.LeadDetailsItemResponseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.LeadDetailsItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.LeadDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.LeadInStatusResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.LeadTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.SearchEmployeeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.note.GeneralNoteTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskBaseInfoTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskDetailInfoTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.ColorTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.FlowSettingsTO;
import com.edatasite.workforce.rest.v2.release10.enums.EntityFieldTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.EntityTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderByEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderFieldEnum;
import com.edatasite.workforce.rest.v2.release10.enums.RequiredEntityFieldName;
import com.edatasite.workforce.rest.v2.release10.enums.TaskPresenceEnum;
import com.edatasite.workforce.rest.v2.release10.enums.TaskPriorityEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.UndeclaredThrowableException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by Dilsh0d on 10/13/2017.
 */
@Tag(name = "Lead", description = "Lead API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiLeadControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiLeadControllerV2.class);

    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private FacetFilterManager facetFilterManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private UserFilterManager userFilterManager;
    @Autowired
    private HttpServletRequest servletRequest;
    @Autowired
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private NoteServiceLocal noteServiceLocal;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private ModelFieldManager modelFieldManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    /*@Autowired
    private CreateAttachmentHandler createAttachmentHandler;*/
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private ApiLeadService leadService;
    @Autowired
    private FormPropertyManager formPropertyManager;
    @Autowired
    private ModelFieldLocalizer modelFieldLocalizer;
    @Autowired
    private CustomFormSectionManager customFormSectionManager;

    private static LinkedHashMap<String, String> getStateRelationMap() {

        LinkedHashMap<String, String> relationMap = new LinkedHashMap<>();
        relationMap.put(CustomFormConstants.STATUS, RequiredEntityFieldName.STATUS.name());
        relationMap.put(CustomFormConstants.EMAIL, RequiredEntityFieldName.EMAIL.name());
        relationMap.put(CustomFormConstants.PHONE, RequiredEntityFieldName.PHONE.name());
        relationMap.put(CustomFormConstants.CRM_ACCOUNT_NAME, RequiredEntityFieldName.COMPANY.name());
        relationMap.put(CustomFormConstants.ADDRESS, RequiredEntityFieldName.CONTACT_ADDRESSES.name());
        relationMap.put(CustomFormConstants.CRM_ACCOUNT_OWNER, RequiredEntityFieldName.ACCOUNT_OWNER.name());
        relationMap.put(CustomFormConstants.CRM_ACCOUNT_NAME, RequiredEntityFieldName.ACCOUNT_NAME.name());
        relationMap.put(CustomFormConstants.CRM_ACCOUNT_PARENT, RequiredEntityFieldName.PARENT_ACCOUNT.name());
        relationMap.put(CustomFormConstants.CRM_ACCOUNT_TYPE, RequiredEntityFieldName.ACCOUNT_TYPE.name());
        relationMap.put(CustomFormConstants.PRIMARY_CONTACT_ADDRESSES, RequiredEntityFieldName.ADDRESS_INFORMATION.name());
        relationMap.put(CustomFormConstants.CRM_ACCOUNT_BILLING_ADDRESS, RequiredEntityFieldName.ADDRESS_INFORMATION.name());
        relationMap.put(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE, RequiredEntityFieldName.ASSIGNEE.name());
        relationMap.put(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE, RequiredEntityFieldName.BACKUP_ASSIGNEE.name());
        relationMap.put(CustomFormConstants.CRM_OPPORTUNITY_NAME, RequiredEntityFieldName.OPPORTUNITY_NAME.name());
        relationMap.put(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT, RequiredEntityFieldName.AMOUNT.name());
        relationMap.put(CustomFormConstants.CURRENCY, RequiredEntityFieldName.CURRENCY.name());
        relationMap.put(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE, RequiredEntityFieldName.CLOSE_DATE.name());
        relationMap.put(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME, RequiredEntityFieldName.CONTACT_NAME.name());
        relationMap.put(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME, RequiredEntityFieldName.COMPANY_NAME.name());
        relationMap.put(CustomFormConstants.CRM_OPPORTUNITY_STAGE, RequiredEntityFieldName.STAGE.name());
        relationMap.put(CustomFormConstants.REPORTS_TO, RequiredEntityFieldName.SUPERVISOR.name());
        relationMap.put(CustomFormConstants.PRIMARY_CONTACT, RequiredEntityFieldName.PRIMARY_CONTACT.name());

        //task
        relationMap.put(CustomFormConstants.TASK.PROJECT, RequiredEntityFieldName.PROJECT.name());
        relationMap.put(CustomFormConstants.NAME, RequiredEntityFieldName.TASK_NAME.name());
        relationMap.put(CustomFormConstants.DESCRIPTION, RequiredEntityFieldName.TASK_DESCRIPTION.name());
        relationMap.put(CustomFormConstants.START_DATE, RequiredEntityFieldName.WHEN.name());
        relationMap.put(CustomFormConstants.DUE_DATE, RequiredEntityFieldName.WHEN.name());
        relationMap.put(CustomFormConstants.PRIORITY, RequiredEntityFieldName.PRIORITY.name());
        relationMap.put(CustomFormConstants.TASK.BILLIBLE, RequiredEntityFieldName.BILLABLE.name());
        relationMap.put(CustomFormConstants.ASSIGNEE, RequiredEntityFieldName.ASSIGNEES.name());
        relationMap.put(CustomFormConstants.WORKSTREAM.PARENT_WORKSTREAM, RequiredEntityFieldName.PARENT_WORKSTREAM.name());
        relationMap.put(CustomFormConstants.WORKSTREAM.DUE_DATE_REMINDER, RequiredEntityFieldName.DATE_REMINDER.name());
        relationMap.put(CustomFormConstants.TASK.PREDECESSOR_TASK, RequiredEntityFieldName.PREDECESSOR.name());
        relationMap.put(CustomFormConstants.TASK.SUCCESSOR_TASK, RequiredEntityFieldName.SUCCESSOR.name());

        return relationMap;
    }

    private static String getFieldStateRelation(String entityType) {
        return getStateRelationMap().get(entityType.toUpperCase());
    }

    @Operation(summary = "Create new Lead")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Lead"))
    @RequestMapping(value = "/lead", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object createLead(@Validated @RequestBody LeadDto leadDto) throws RestException, ParseException {
        if (leadDto.getId() != null) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Lead ID is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        leadService.save(leadDto);
        return successResponse(leadDto);
    }

    @Operation(summary = "Update existing Lead")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Lead"))
    @RequestMapping(value = "/lead", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object updateLead(@Validated @RequestBody LeadDto leadDto) throws RestException, ParseException {
        if (leadDto.getId() == null) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Lead ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        leadService.save(leadDto);
        return successResponse(leadDto);
    }

    @Operation(summary = "Get existing Lead by ID")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Lead"))
    @RequestMapping(path = "/lead/{leadId}", method = RequestMethod.GET)
    public Object getLeadById(@PathVariable final Integer leadId) throws RestException {

        return successResponse(leadService.getById(leadId));
    }

    @Operation(summary = "Get Leads in Statuses", description = "Retrieves filtered users based on is_active filters from quick_filter")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of filtered users"),
            @ApiResponse(responseCode = "400", description = "status_id, offset and count are required"),
            @ApiResponse(responseCode = "422", description = "count and offset should be more than zero")})
    @RequestMapping(value = "/leads/items_in_status", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @CheckPermission(permissions = {PermissionConstants.CRM_LEADS_LIST})
    public Object leadsInStatus(@RequestBody ItemInStatusTO leadInStatus) throws RestException {
        if (leadInStatus.getStatus_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Status id required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (leadInStatus.getOffset() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Offset required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (leadInStatus.getOffset() < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Offset can not be less then zero", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (leadInStatus.getCount() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Count required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (leadInStatus.getCount() < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Count can not be less then zero", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (leadInStatus.getOffset() == 0 && leadInStatus.getCount() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Offset and count can not be zero at the same time", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        OrderFieldEnum orderFieldEnum = null;
        OrderByEnum orderByEnum = null;
        if (leadInStatus.getOrder() != null) {
            if (StringUtils.isNotBlank(leadInStatus.getOrder().getType())) {
                orderFieldEnum = OrderFieldEnum.getOrderField(leadInStatus.getOrder().getType());
                if (orderFieldEnum == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Type field should be one of ID, NAME, DATE, COMPANY", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            if (StringUtils.isNotBlank(leadInStatus.getOrder().getDirection())) {
                orderByEnum = OrderByEnum.getDirection(leadInStatus.getOrder().getDirection());
                if (orderByEnum == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Direction field should be one of ASC or DESC", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
        }
        //As per request from Islom Gulomov and Munir Yamal by default we will set sorting by kanban_order
        if (orderByEnum == null) {
            orderFieldEnum = OrderFieldEnum.KANBAN_ORDER.KANBAN_ORDER;
            orderByEnum = OrderByEnum.ASC;
        }

        EdsReference requestedStatus = null;
        if (leadInStatus.getStatus_id() > 0) {
            requestedStatus = referenceManager.get(leadInStatus.getStatus_id());
            if (requestedStatus == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Status with " + leadInStatus.getStatus_id() + " id not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        }

        ListingFilterParameter filterParameter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.LeadListPanel);

        HashMap<String, FacetContentRpc> facetContentMap = filterParameter.getFacetFilter().getFacetContentMap();

        ArrayList<SelectItem> statusFacetItems = new ArrayList<SelectItem>();
        if (requestedStatus != null) {
            statusFacetItems.add(new SelectItem(requestedStatus.getObjectID(), requestedStatus.getName()));
        } else {
            statusFacetItems.add(new SelectItem(-1, "N/A"));
        }

        facetContentMap.get(FacetContentType.LeadFacetFilter.getContentCode()[2]).setFacetItems(statusFacetItems.toArray(new SelectItem[0]));

        //quickfilter
        ArrayList<SelectItem> assignedToFacetItems = new ArrayList<>();
        EdsUser currentUser = userManager.getUser();
        EdsFacetFilter edsQuickFilter = facetFilterManager.getDefaultUserFacetFilter(ListPanelType.FilteredStatusesForMobile, currentUser);
//        HashMap<Integer, String> activePeople = new HashMap<>();
//        HashMap<Integer, String> activeFilters = new HashMap<>();
        if (edsQuickFilter != null) {
            HashSet<String> colNames = new HashSet<>();
            colNames.add("people");
            colNames.add("categories");
            FacetFilterRpc facetFilterRpc = edsQuickFilter.getFacetFilter(colNames);

            FacetContentRpc p = facetFilterRpc.getFacetContentMap().get("people");
            FacetContentRpc c = facetFilterRpc.getFacetContentMap().get("categories");
            if (p != null) {
                for (SelectItem pI : p.getFacetItems()) {
                    assignedToFacetItems.add(new SelectItem(pI.getId() > 0 ? pI.getId() : -1, ""));
                }
                //Apply Quick Filter by assignedto
                if (!assignedToFacetItems.isEmpty()) {
                    facetContentMap.get(FacetContentType.LeadFacetFilter.getContentCode()[5]).setFacetItems(assignedToFacetItems.toArray(new SelectItem[0]));
                }
            }
            if (c != null) {
                for (SelectItem facetFilter : c.getFacetItems()) {
                    EdsFacetFilter filter = facetFilterManager.getFacetFilter(facetFilter.getId());
                    if (filter != null) {
                        //Important to pass facetcodenames
                        FacetFilterRpc filterRpc = filter.getFacetFilter(new HashSet<>(filterParameter.getFacetFilter().getShowFacetCodeName()));
                        if (filterRpc != null && !filterRpc.getFacetContentMap().isEmpty()) {
                            for (Map.Entry<String, FacetContentRpc> entry : filterRpc.getFacetContentMap().entrySet()) {
                                if (entry.getValue().getFacetItems().length > 0
                                        && !FacetContentType.LeadFacetFilter.getContentCode()[2].equalsIgnoreCase(entry.getKey())
                                    /*&& !FacetContentType.LeadFacetFilter.getContentCode()[5].equalsIgnoreCase(entry.getKey())*/) {
                                    FacetContentRpc existingVals = facetContentMap.get(entry.getKey());
                                    if (existingVals != null) {
                                        ArrayList<SelectItem> existingItems = new ArrayList<SelectItem>(Arrays.asList(existingVals.getFacetItems()));
                                        existingItems.addAll(Arrays.asList(entry.getValue().getFacetItems()));

                                        ArrayList<SelectItem> removedDuplicates = new ArrayList<>(new HashSet<>(existingItems));

                                        existingVals.setFacetItems(removedDuplicates.toArray(new SelectItem[0]));
                                        facetContentMap.put(entry.getKey(), existingVals);
                                    } else {
                                        facetContentMap.put(entry.getKey(), entry.getValue());
                                    }
                                }
                            }

                            //Set Periods
                            if (filterRpc.getStartDate() != null && filterRpc.getEndDate() != null) {
                                filterParameter.getFacetFilter().setStartDate(filterRpc.getStartDate());
                                filterParameter.getFacetFilter().setEndDate(filterRpc.getEndDate());
                                filterParameter.getFacetFilter().setSelectedDateSolrCodeName(filterRpc.getSelectedDateSolrCodeName());
                            }
                            //End Of Set Periods
                        }
                    }
                }
            }
        }
        //end of quickfilter
        //If OneTimeFilter is active
        FacetFilterRpc oneTimeFilter = getOneTimeFilter(initializeDefaultLeadFacetFilter());
        if (oneTimeFilter != null && oneTimeFilter.isFavourFilter()) {
            //Important to pass facetcodenames
            //FacetFilterRpc filterRpc = oneTimeFilter.getFacetFilter(new HashSet<>(filterParameter.getFacetFilter().getShowFacetCodeName()));
            if (!oneTimeFilter.getFacetContentMap().isEmpty()) {
                for (Map.Entry<String, FacetContentRpc> entry : oneTimeFilter.getFacetContentMap().entrySet()) {

                    //ignore status and assignedto fields
                    if (entry.getValue().getFacetItems().length > 0
                            && !FacetContentType.LeadFacetFilter.getContentCode()[2].equalsIgnoreCase(entry.getKey())
                        /* && !FacetContentType.LeadFacetFilter.getContentCode()[5].equalsIgnoreCase(entry.getKey())*/) {
                        FacetContentRpc existingVals = facetContentMap.get(entry.getKey());
                        if (existingVals != null) {
                            ArrayList<SelectItem> existingItems = new ArrayList<SelectItem>(Arrays.asList(existingVals.getFacetItems()));
                            existingItems.addAll(Arrays.asList(entry.getValue().getFacetItems()));

                            ArrayList<SelectItem> removedDuplicates = new ArrayList<>(new HashSet<>(existingItems));

                            existingVals.setFacetItems(removedDuplicates.toArray(new SelectItem[0]));
                            facetContentMap.put(entry.getKey(), existingVals);
                        } else {
                            facetContentMap.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }
            //Set Periods
            if (oneTimeFilter.getStartDate() != null && oneTimeFilter.getEndDate() != null) {
                filterParameter.getFacetFilter().setStartDate(oneTimeFilter.getStartDate());
                filterParameter.getFacetFilter().setEndDate(oneTimeFilter.getEndDate());
                filterParameter.getFacetFilter().setSelectedDateSolrCodeName(oneTimeFilter.getSelectedDateSolrCodeName());
            }
            //End Of Set Periods
        }
        //End of If OneTimeFilter is active


        /*Map<Integer, Integer> savedItems = new HashMap<>();
        savedItems.put(leadInStatus.getStatus_id(), leadInStatus.getStatus_id());
        facetContentMap.get(FacetContentType.LeadFacetFilter.getContentCode()[2]).setSavedItems(savedItems);*/

        filterParameter.getFacetFilter().setFacetContentMap(facetContentMap);

        ArrayList<String> columnCodeNames = ContactListItem.defaultLeadColumnNames;
        ListPanelToolRpc panelTools = new ListPanelToolRpc();
        panelTools.setColumnCodeName(columnCodeNames);
        panelTools.setShowPopup(true);
        filterParameter.setListPanelTool(panelTools);
        filterParameter.setColumnsOfListing(columnCodeNames);

        filterParameter.setStart(leadInStatus.getOffset());
        filterParameter.setLimit(leadInStatus.getCount());
        filterParameter.setSearchButton(false);
        filterParameter.setDetectDuplicates(false);
        filterParameter.setWithImage(true);
        if (orderFieldEnum != null) {
            filterParameter.setSortField(getSortField(orderFieldEnum, ListPanelType.LeadListPanel));
        }
        filterParameter.setAscending(orderByEnum == null || OrderByEnum.ASC.getDirection().equals(orderByEnum.getDirection()));
        filterParameter.setSortDir(orderByEnum != null ? orderByEnum.getId() : OrderByEnum.ASC.getId());

        //Custom fields
        //Custom fields which are facetable
        ArrayList<CompanyCustomFieldItem> leadCustomFields = commonServiceLocal.getCompanyCustomFieldsForListView(ViewName.Lead);

        if (leadCustomFields != null && leadCustomFields.size() > 0) {
            leadCustomFields.forEach(companyCustomFieldItem -> {
                if (companyCustomFieldItem.isFacetable() && StringUtils.isNotBlank(companyCustomFieldItem.getColumnCode())) {
                    //we must add this condition otherwise it will add again and again into map (static block inside other class)
                    if (!filterParameter.getFacetFilter().getShowFacetCodeName().contains(companyCustomFieldItem.getColumnCode())) {
                        filterParameter.getFacetFilter().getShowFacetCodeName().add(companyCustomFieldItem.getColumnCode());
                    }
                    FacetSolrField solrField = new FacetSolrField(companyCustomFieldItem.getColumnCode().toUpperCase(), companyCustomFieldItem.getColumnCode().toUpperCase());
                    solrField.setConditionItemId(true);
                    filterParameter.getFacetFilter().getShowSolrFieldMap().put(companyCustomFieldItem.getColumnCode(), solrField);
                } else {
                    filterParameter.getFacetFilter().getShowFacetCodeName().remove(companyCustomFieldItem.getColumnCode());
                    if (filterParameter.getFacetFilter().getShowSolrFieldMap().get(companyCustomFieldItem.getColumnCode()) != null) {
                        filterParameter.getFacetFilter().getShowSolrFieldMap().remove(companyCustomFieldItem.getColumnCode());
                    }
                }

            });
        }
        //End Of Custom fields


        ListResult<ContactListItem> result;
        try {
            result = crmServiceLocal.getNewLeads(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            if (((UndeclaredThrowableException) e).getUndeclaredThrowable() instanceof CheckPermissionException) {
                throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        LeadInStatusResultTO leadInStatusResult = new LeadInStatusResultTO();
        leadInStatusResult.setStatus_id(leadInStatus.getStatus_id() > 0 ? leadInStatus.getStatus_id() : 0);
        leadInStatusResult.setTotal_count(result.getTotal());
        if (result.getTotal() < (leadInStatus.getCount() + leadInStatus.getOffset())) {
            leadInStatusResult.setLeft(0);
        } else {
            leadInStatusResult.setLeft(result.getTotal() - (leadInStatus.getOffset() + leadInStatus.getCount()));
        }
        leadInStatusResult.setCount(result.getList() != null ? result.getList().size() : 0);
        leadInStatusResult.setOffset(leadInStatus.getOffset());

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        ArrayList<LeadTO> leads = new ArrayList<>();
        for (ContactListItem item : result.getList()) {
            LeadTO lead = new LeadTO();
            lead.setName(item.getName());
            if (item.getCrmAccount() != null) {
                lead.setCompany(item.getCrmAccount().getName());
            }
            lead.setItem_id(item.getObjectId());
            //We set status_id which must be same as we took from request
            if (item.getLeadStatus() != null) {
                lead.setStatus_id(item.getLeadStatus().getId());
            } else {
                lead.setStatus_id(0);
            }
            lead.setDate_added(longDateTimezoneFormat.format(item.getCreatedDate()));
            List<Integer> taskIDs = relationManager.getRelationIDsByType(item.getObjectId(), RelationItem.TYPE_LEAD, RelationItem.TYPE_TASK);
            if (taskIDs != null && !taskIDs.isEmpty()) {
                boolean hasLeadOverdueTasks = taskManager.getOverdueTasksByIDs(taskIDs).size() > 0;
                boolean hasLeadTasks = taskManager.getTasksByIDs(taskIDs).size() > 0;
                if (hasLeadOverdueTasks) {
                    lead.setTasks_presence(TaskPresenceEnum.OVERDUE.getType());
                } else {
                    lead.setTasks_presence(hasLeadTasks ? TaskPresenceEnum.AVAILABLE.getType() : TaskPresenceEnum.NO_TASKS.getType());
                }
            } else {
                lead.setTasks_presence(TaskPresenceEnum.NO_TASKS.getType());
            }
            lead.setAvatar_image(item.getContactImageUrl());
            leads.add(lead);
        }
        leadInStatusResult.setList(leads);

        return successResponse(leadInStatusResult);
    }

    @Transactional
    @Operation(summary = "Change Lead status", description = "Changes lead status based on provided parameters")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with appropriate message"),
            @ApiResponse(responseCode = "400", description = "item_id required"),
            @ApiResponse(responseCode = "422", description = "status_id is required")})
    @RequestMapping(value = "/leads/change_status", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @CheckPermission(permissions = {PermissionConstants.CRM_LEADS_LIST, PermissionConstants.CRM_LEAD_STATUS})
    public Object changeLeadStatus(@RequestBody ChangeLeadStatusRequestTO changeLeadStatusRequestTO) throws RestException {

        if (changeLeadStatusRequestTO.getItem_id() == null || changeLeadStatusRequestTO.getItem_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Item id required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsCrmContact lead = crmContactManager.get(changeLeadStatusRequestTO.getItem_id());
        if (lead == null || !EdsCrmContact.LEAD_CONTACT.equals(lead.getContactType()) || lead.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Lead with " + changeLeadStatusRequestTO.getItem_id() + " id not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        ReferenceItem status = new ReferenceItem();
        if (changeLeadStatusRequestTO.getStatus_id() != null && changeLeadStatusRequestTO.getStatus_id() > 0) {
            EdsReference newLeadStatus = referenceManager.get(changeLeadStatusRequestTO.getStatus_id());
            if (newLeadStatus == null || newLeadStatus.getParent() == null || StringUtils.isBlank(newLeadStatus.getParent().getCode()) || !EdsCrmContact._LEAD_STATUS.equalsIgnoreCase(newLeadStatus.getParent().getCode())) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Status with " + changeLeadStatusRequestTO.getStatus_id() + " id not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            status.setId(newLeadStatus.getAsSelectItem().getId());
            status.setCode(newLeadStatus.getAsSelectItem().getCode());
            status.setName(newLeadStatus.getAsSelectItem().getName());
        }

        ContactListItem item = new ContactListItem();
        item.setObjectId(changeLeadStatusRequestTO.getItem_id());
        item.setLeadStatus(status);

        try {
            contactServiceLocal.saveContactEditCellValue(item, ContactListItem.LEAD_STATUS);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), ERROR_LEAD_MODIFY, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return successResponse(new ResponseData());
    }

    @Operation(summary = "Search Lead", description = "Retrieves leads based on search query")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have leads based on search query"),
            @ApiResponse(responseCode = "400", description = "query is required")})
    @RequestMapping(value = "/leads/search", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.CRM_LEADS_LIST})
    public Object searchLead(@RequestParam(value = "query") String query,
                             @RequestParam(value = "limit", required = false) Integer limit,
                             @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        PagingListResultTO<LeadTO> leadListResult = new PagingListResultTO<>();

        if (StringUtils.isBlank(query)) {
            return successResponse(leadListResult);
        }

        log.info("Api search lead. Search encode text: " + query);
        query = query.replace("%20", " ").trim();
        log.info("Api search lead. Search text: " + query);

        Integer start = (offset != null && offset > 0) ? offset : 0;
        Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(start);
        filterParameter.setLimit(maxLimit);
        filterParameter.setSearchKey(query);
        filterParameter.setDetectDuplicates(false);
        filterParameter.setWithImage(true);
        filterParameter.setSearchButton(true);
        filterParameter.setFromMobile(true);

        ListResult<ContactListItem> result;
        try {
            result = crmServiceLocal.getNewLeads(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        leadListResult.setTotal_count(result.getTotal());
        if (result.getTotal() < (maxLimit + start)) {
            leadListResult.setLeft(0);
        } else {
            leadListResult.setLeft(result.getTotal() - (start + maxLimit));
        }
        leadListResult.setCount(result.getList() != null ? result.getList().size() : 0);
        leadListResult.setOffset(start);

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        ArrayList<LeadTO> leads = new ArrayList<>();
        for (ContactListItem item : result.getList()) {
            LeadTO lead = new LeadTO();
            lead.setName(item.getName());
            if (item.getCrmAccount() != null) {
                lead.setCompany(item.getCrmAccount().getName());
            }
            lead.setItem_id(item.getObjectId());
            //We set status_id which must be same as we took from request
            if (item.getLeadStatus() != null) {
                lead.setStatus_id(item.getLeadStatus().getId());
            } else {
                lead.setStatus_id(0);
            }
            lead.setDate_added(longDateTimezoneFormat.format(item.getCreatedDate()));
            List<Integer> taskIDs = relationManager.getRelationIDsByType(item.getObjectId(), RelationItem.TYPE_LEAD, RelationItem.TYPE_TASK);
            if (taskIDs != null && !taskIDs.isEmpty()) {
                boolean hasLeadOverdueTasks = taskManager.getOverdueTasksByIDs(taskIDs).size() > 0;
                boolean hasLeadTasks = taskManager.getTasksByIDs(taskIDs).size() > 0;
                if (hasLeadOverdueTasks) {
                    lead.setTasks_presence(TaskPresenceEnum.OVERDUE.getType());
                } else {
                    lead.setTasks_presence(hasLeadTasks ? TaskPresenceEnum.AVAILABLE.getType() : TaskPresenceEnum.NO_TASKS.getType());
                }
            } else {
                lead.setTasks_presence(TaskPresenceEnum.NO_TASKS.getType());
            }
            lead.setAvatar_image(item.getContactImageUrl());
            leads.add(lead);
        }

        leadListResult.setList(leads);

        return successResponse(leadListResult);
    }

    private FacetFilterRpc initializeDefaultLeadFacetFilter() {
        //Initialize Facet Filter
        final FacetFilterRpc mainFilter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.LeadListPanel).getFacetFilter();
        //new FacetFilterRpc(ListPanelType.LeadListPanel, showSolrFieldMap, showFacetCodeName);

        //Custom fields which are facetable
        ArrayList<CompanyCustomFieldItem> leadCustomFields = commonServiceLocal.getCompanyCustomFieldsForListView(ViewName.Lead);

        if (leadCustomFields != null && leadCustomFields.size() > 0) {
            leadCustomFields.forEach(companyCustomFieldItem -> {
                if (companyCustomFieldItem.isFacetable() && StringUtils.isNotBlank(companyCustomFieldItem.getColumnCode())) {
                    //we must add this condition otherwise it will add again and again into map (static block inside other class)
                    if (!mainFilter.getShowFacetCodeName().contains(companyCustomFieldItem.getColumnCode())) {
                        mainFilter.getShowFacetCodeName().add(companyCustomFieldItem.getColumnCode());
                    }
                    FacetSolrField solrField = new FacetSolrField(companyCustomFieldItem.getColumnCode().toUpperCase(), companyCustomFieldItem.getColumnCode().toUpperCase());
                    solrField.setConditionItemId(true);
                    mainFilter.getShowSolrFieldMap().put(companyCustomFieldItem.getColumnCode(), solrField);
                } else {
                    mainFilter.getShowFacetCodeName().remove(companyCustomFieldItem.getColumnCode());
                    if (mainFilter.getShowSolrFieldMap().get(companyCustomFieldItem.getColumnCode()) != null) {
                        mainFilter.getShowSolrFieldMap().remove(companyCustomFieldItem.getColumnCode());
                    }
                }

            });
        }
        //End Of main facet filter initialization
        return mainFilter;
    }

    private FacetFilterRpc getOneTimeFilter(FacetFilterRpc defaultFilter) {

        if (defaultFilter != null) {
            defaultFilter.setType(ListPanelType.LeadListPanelOTF);
        }
        defaultFilter.setUserID(userManager.getUser().getObjectID());

        FacetFilterRpc otf = commonServiceLocal.getUserFacetFilter(defaultFilter);
        otf.setName("OTF");
        otf.setDefaultFilter(true);
        otf.setType(ListPanelType.LeadListPanelOTF);

        if (otf.getObjectID() != null) {
            EdsUserFilter edsUserFilter = userFilterManager.getByFacetFilterId(otf.getObjectID());
            if (edsUserFilter != null) {
                otf.setFavourFilter(Boolean.TRUE.equals(edsUserFilter.getFavour()));
            }
        }
        return otf;
    }

    @Operation(summary = "Lead Detail Info")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have lead details"),
            @ApiResponse(responseCode = "400", description = "id is required")})
    @RequestMapping(value = "/leads/{id}/details", method = RequestMethod.GET)
    public Object getLeadDetails(@PathVariable(value = "id") Integer id) throws RestException {
        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsCrmContact edsCrmContact = crmContactManager.get(id);
        if (edsCrmContact == null || edsCrmContact.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Lead with id " + id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        LeadDetailsItemTO leadDetailsItem = new LeadDetailsItemTO();

        //Base Info
        LeadTO leadBaseInfo = new LeadTO();
        leadBaseInfo.setName(edsCrmContact.getName());
        if (edsCrmContact.getCrmAccount() != null) {
            leadBaseInfo.setCompany(edsCrmContact.getCrmAccount().getName());
        }
        leadBaseInfo.setItem_id(edsCrmContact.getObjectID());
        if (edsCrmContact.getLeadStatus() != null) {
            leadBaseInfo.setStatus_id(edsCrmContact.getLeadStatus().getObjectID());
        }
        if (edsCrmContact.getCreationDate() != null) {
            leadBaseInfo.setDate_added(longDateTimezoneFormat.format(edsCrmContact.getCreationDate()));
        }
        if (edsCrmContact.getPhoto() != null) {
            leadBaseInfo.setAvatar_image(commonServiceLocal.getImageUrl(edsCrmContact.getPhoto().getObjectID()));
        }
        //Default value of tasks presence
        leadBaseInfo.setTasks_presence(TaskPresenceEnum.NO_TASKS.getType());

        //Task. One of latest task
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(0);
        filterParameter.setLimit(MAX_LIMIT);
        filterParameter.setSortField(TaskListItem.ID);
        filterParameter.setRelationType(RelationItem.TYPE_LEAD);
        filterParameter.setRelationID(edsCrmContact.getObjectID());
        try {
            TaskList taskList = taskServiceLocal.getTaskList(filterParameter);
            if (taskList != null && taskList.getList() != null && taskList.getList().size() > 0) {
                TaskListItem taskListItem = taskList.getList().get(0);
                TaskDetailInfoTO task = new TaskDetailInfoTO();
                //Task base info
                TaskBaseInfoTO taskBaseInfo = new TaskBaseInfoTO();
                taskBaseInfo.setName(taskListItem.getName());
                taskBaseInfo.setStatus_id(taskListItem.getTaskStatusId());
                taskBaseInfo.setItem_id(taskListItem.getObjectID());
                if (taskListItem.getDueDate() != null) {
                    taskBaseInfo.setDue_date(longDateTimezoneFormat.format(taskListItem.getDueDate()));
                }
                taskBaseInfo.setPriority(TaskPriorityEnum.get(taskListItem.getPriorityCode()));

                task.setBase_info(taskBaseInfo);


                List<TaskListItem> overdueTasks = taskList.getList().stream().filter(taskItem -> (EdsTask.IN_PROGRESS.equals(taskItem.getStatusCode())
                        || EdsTask.NOT_STARTED.equals(taskItem.getStatusCode())
                        || EdsTask.WAITING_FOR_SOMEONE_ELSE.equals(taskItem.getStatusCode()))
                        && taskItem.getDueDate() != null && taskItem.getDueDate().before(new Date())).toList();

                if (overdueTasks.size() > 0) {
                    leadBaseInfo.setTasks_presence(TaskPresenceEnum.OVERDUE.getType());
                } else {
                    leadBaseInfo.setTasks_presence(TaskPresenceEnum.AVAILABLE.getType());
                }

                //Task Status
                FlowSettingsTO status = new FlowSettingsTO();
                EdsReference edsReference = referenceManager.get(taskListItem.getTaskStatusId());
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

                task.setStatus(status);

                leadDetailsItem.setTask(task);
            }
        } catch (Exception e) {
            log.error("Error occurred while getting contact's tasks ", e);
        }

        //One of latest note
        filterParameter = new ListingFilterParameter();
        filterParameter.setRelationID(edsCrmContact.getObjectID());
        filterParameter.setRelationType(RelationItem.TYPE_CONTACT);
        filterParameter.setStart(0);
        filterParameter.setLimit(1);
        try {
            List<EdsNoteHistory> noteHistoryList = noteHistoryManager.getNoteList(filterParameter);
            if (noteHistoryList != null && noteHistoryList.size() > 0) {
                EdsNoteHistory noteHistory = noteHistoryList.get(0);
                GeneralNoteTO note = new GeneralNoteTO();
                note.setId(noteHistory.getObjectID());
                note.setDate(longDateTimezoneFormat.format(noteHistory.getEventDate()));
                if (StringUtils.isNotBlank(noteHistory.getComment())) {
                    note.setNote_content(noteHistory.getComment().replace("\n", " "));
                }
                if (noteHistory.isVisibility() != null) {
                    note.setType(noteHistory.isVisibility() ? NoteEnum.PRIVATE.getCode() : NoteEnum.PUBLIC.getCode());
                } else {
                    note.setType(NoteEnum.INTERNAL.getCode());
                }
                if (noteHistory.getEmployee() != null) {
                    note.setOwner_id(noteHistory.getEmployee().getObjectID());
                    note.setOwner_name(noteHistory.getEmployee().getName());
                    if (noteHistory.getEmployee().getPhoto() != null) {
                        note.setOwner_avatar(commonServiceLocal.getImageUrl(noteHistory.getEmployee().getPhoto().getObjectID()));
                    }
                }
                leadDetailsItem.setNote(note);
            }
        } catch (Exception e) {
            log.error("Error occurred while getting contact's notes ", e);
        }

        //Status
        if (edsCrmContact.getLeadStatus() != null) {
            FilteredStatusItemTO status = new FilteredStatusItemTO();
            status.setStatus_id(edsCrmContact.getLeadStatus().getObjectID());
            status.setStatus_name(edsCrmContact.getLeadStatus().getName());
            if (edsCrmContact.getLeadStatus().getReferenceColor() != null) {
                ColorTO color = new ColorTO();
                color.setId(edsCrmContact.getLeadStatus().getReferenceColor().getObjectID());
                color.setName(edsCrmContact.getLeadStatus().getReferenceColor().getName());
                color.setHex(edsCrmContact.getLeadStatus().getReferenceColor().getHex());
                status.setStatus_color(color);
            }
            status.setOrder_id(edsCrmContact.getLeadStatus().getSorder());
            status.setIs_system(edsCrmContact.getLeadStatus().isSystemReference());

            leadDetailsItem.setStatus(status);
        }

        //Contacts
        ContactsTO contactsTO = new ContactsTO();

        //Contacts Phones
        contactsTO.setPhones(contactServiceLocal.convertToPhoneTO(edsCrmContact));

        //Contacts Emails
        contactsTO.setEmails(contactServiceLocal.convertContactEmails(edsCrmContact));

        leadDetailsItem.setContacts(contactsTO);

        leadDetailsItem.setBase_info(leadBaseInfo);

        LeadDetailsItemResponseTO result = new LeadDetailsItemResponseTO();
        result.setItem(leadDetailsItem);

        return successResponse(result);
    }

    @Transactional
    @Operation(summary = "Create Leads from phone contact", description = "Create Leads from phone contact")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have if successfully added or false if not added successfully with error code"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/{main_entity_path}/batch_import", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @CheckPermission(permissions = {PermissionConstants.CRM_LEADS_LIST, PermissionConstants.ADD_NEW_LEAD})
    public Object batchLeadCreation(
            @PathVariable("main_entity_path") String entityType,
            MultipartRequest multipartRequest,
            @RequestParam(name = "body") String jsonString) throws RestException {

        ObjectMapper mapper = new ObjectMapper();
        BatchLeadImportRequest body;
        try {
            body = mapper.readValue(jsonString, BatchLeadImportRequest.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong: " + e.getMessage(), REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsUser user = userManager.getUser();
        Integer companyId = user.getCompany().getObjectID();
        Integer contactType;

        if (EntityTypeEnum.LEADS.name().equalsIgnoreCase(entityType)) {
            contactType = ContactListItem.LEAD_CONTACT;
        } else if (EntityTypeEnum.CONTACTS.name().equalsIgnoreCase(entityType)) {
            contactType = ContactListItem.CRM_CONTACT;
        } else {
            throw new RestException("main_entity_path incorrect, available options are leads, contacts", "invalid main_entity_path", INVALID, HttpStatus.BAD_REQUEST);
        }

        if (body == null || body.getList() == null || body.getList().size() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "body is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        int fail = 0;
        for (BatchLeadItemTO batchLeadItemTO : body.getList()) {
            ContactListItem item = new ContactListItem();
            item.setContactType(contactType);
            item.setCheckForDuplicates(false);
            if (StringUtils.isNotBlank(batchLeadItemTO.getFirst_name())) {
                item.setFirstName(batchLeadItemTO.getFirst_name());
            }
            if (StringUtils.isNotBlank(batchLeadItemTO.getLast_name())) {
                item.setLastName(batchLeadItemTO.getLast_name());
            }
            item.setOwnerId(user.getObjectID());
            item.setOwner(user.getName());

            ArrayList<String> inValidEmails = new ArrayList<>();
            if (batchLeadItemTO.getEmails() != null && batchLeadItemTO.getEmails().size() > 0) {
                ArrayList<String> validEmails = new ArrayList<>();
                for (String email : batchLeadItemTO.getEmails()) {
                    if (EMAIL_PATTERN.matcher(email).matches()) {
                        validEmails.add(email);
                    } else {
                        inValidEmails.add(email);
                    }
                }
                if (validEmails.size() > 0) {
                    //Set one of as primary
                    item.setPrimaryEmail(validEmails.get(0));

                    HashMap<Integer, ArrayList<String>> emailParam = new HashMap<>();
                    emailParam.put(Constants.G_WORK, validEmails);
                    item.setEmails(emailParam);
                }
            }

            ArrayList<String> inValidPhones = new ArrayList<>();
            if (batchLeadItemTO.getPhones() != null && batchLeadItemTO.getPhones().size() > 0) {
                ArrayList<String> validPhones = new ArrayList<>();
                for (String phone : batchLeadItemTO.getPhones()) {
                    if (phone.matches(Constants.REGEX_PHONE)) {
                        validPhones.add(phone);
                    } else {
                        inValidPhones.add(phone);
                    }
                }

                if (validPhones.size() > 0) {
                    //Set one of as primary
                    item.setPrimaryPhone(validPhones.get(0));

                    HashMap<Integer, ArrayList<String>> phoneParam = new HashMap<>();
                    phoneParam.put(Constants.G_WORK, validPhones);
                    item.setPhones(phoneParam);
                }
            }
            //If lead name is null, set the lead's primary phone as lead name
            if (StringUtils.isBlank(batchLeadItemTO.getFirst_name()) && StringUtils.isBlank(batchLeadItemTO.getLast_name()) && StringUtils.isNotBlank(item.getPrimaryPhone())) {
                item.setFirstName(item.getPrimaryPhone());
            }

            if ((inValidEmails.isEmpty() && inValidPhones.isEmpty()) && (item.getFirstName() != null || item.getPrimaryPhone() != null)) {
                try {
                    Integer contactId = contactServiceLocal.saveContact(item, null, false);
                    //Set Avatars if provided in request (as multipart)
                    if (batchLeadItemTO.getItem_id() != null && multipartRequest.getFileMap().get("id_" + batchLeadItemTO.getItem_id()) != null) {
                        createAttachment(contactId, companyId, multipartRequest.getFileMap().get("id_" + batchLeadItemTO.getItem_id()));
                    }
                } catch (Exception e) {
                    fail++;
                    log.error("", e);
                    //throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                fail++;
            }
        }

        if (fail == 0) {
            return successResponse(new ResponseData());
        } else {
            String word, singular;
            if (fail == 1) {
                word = " lead was";
                singular = " it is";
            } else {
                singular = " they are";
                word = " leads were";
            }
            String errorMessage = fail + " of " + body.getList().size() + word + " not imported because" + singular + " invalid";
            throw new RestException(errorMessage, errorMessage, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void createAttachment(Integer contactId, Integer companyId, MultipartFile multipartFile) {
        try {
            CreateDocumentCommand documentCommand = new CreateDocumentCommand();

            log.info("Api batch lead import multipart file name: ".concat(multipartFile.getName()));
            log.info("Api batch lead import multipart original file name: ".concat(multipartFile.getOriginalFilename()));
            log.info("Api batch lead import multipart file content type: ".concat(multipartFile.getContentType()));

            String contentType = multipartFile.getContentType();
            String extension = contentType.split("/")[1];

            documentCommand.setImgType(extension.toLowerCase(Locale.ENGLISH));

            documentCommand.setCompanyID(companyId);
            documentCommand.setFolderName("static");
            documentCommand.setNotdownloadable("YES");
            WfmMultipartFile wfmMultipartFile = new WfmMultipartFile("", multipartFile);
            documentCommand.addFile(wfmMultipartFile);
            try {
                CreateAttachmentHandler createAttachmentHandler = (CreateAttachmentHandler) ApplicationContextProvider.applicationContext.getBean("createAttachmentHandler");
                createAttachmentHandler.execute(documentCommand);
                String[] result = createAttachmentHandler.getResult();
                if (result != null && result.length > 0) {
                    commonServiceLocal.saveCrmContactImageUrl(Integer.valueOf(result[0]), contactId);
                }
            } catch (Throwable throwable) {
                log.error(throwable.getMessage());
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Operation(summary = "Search assignees for converting", description = "Getting a list of persons to which new opportunity (converted from particular lead) can be assigned by entered characters.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have assignees based on search query"),
            @ApiResponse(responseCode = "400", description = "query is required")})
    @RequestMapping(value = "/leads/{id}/search_assignees", method = RequestMethod.GET)
    public Object searchLeadAssignees(@PathVariable("id") Integer id,
                                      @RequestParam(value = "query") String query,
                                      @RequestParam(value = "limit", required = false) Integer limit,
                                      @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        PagingListResultTO<SearchEmployeeTO> leadAssigneesResult = new PagingListResultTO<>();

        if (StringUtils.isBlank(query)) {
            return successResponse(leadAssigneesResult);
        }

        query = query.replace("%20", " ").trim();

        Integer start = (offset != null && offset > 0) ? offset : 0;
        Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setPermissionCode(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE);
        filterParameter.setStart(start);
        filterParameter.setLimit(maxLimit);
        filterParameter.setSearchKey(query);

        List<EdsEmployee> employees;
        Integer total;
        try {
            employees = employeeManager.getAssignees(filterParameter);
            total = employeeManager.getAssigneesTotalCount(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        leadAssigneesResult.setTotal_count(total);
        if (total < (maxLimit + start)) {
            leadAssigneesResult.setLeft(0);
        } else {
            leadAssigneesResult.setLeft(total - (start + maxLimit));
        }
        leadAssigneesResult.setCount(employees.size());
        leadAssigneesResult.setOffset(start);

        ArrayList<SearchEmployeeTO> assignees = new ArrayList<>();

        for (EdsEmployee employee : employees) {
            SearchEmployeeTO assignee = new SearchEmployeeTO();
            assignee.setId(employee.getObjectID());
            assignee.setName(employee.getName());
            if (employee.getPhoto() != null) {
                assignee.setAvatar_image(commonServiceLocal.getImageUrl(employee.getPhoto().getObjectID()));
            }
            if (employee.getTeam() != null) {
                assignee.setDepartment(new IdNameTO(employee.getTeam().getObjectID(), employee.getTeam().getName()));
            }

            assignees.add(assignee);
        }

        leadAssigneesResult.setList(assignees);

        return successResponse(leadAssigneesResult);
    }

    @Operation(summary = "Delete Lead", description = "Delete particular entity like Lead, Opportunity, Company, Contact etc. Particular entity is described in path, like other requests. Server should check if current user has permissions to delete this particular item, and if no give user message: You don't have permissions to delete this entry. Please contact your administrator")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/leads/{item_id}/delete", method = RequestMethod.DELETE)
    @CheckPermission(permissions = {PermissionConstants.CRM_LEADS_LIST, PermissionConstants.CRM_LEAD_DELETE})
    public Object deleteLead(@PathVariable(value = "item_id") Integer item_id) throws RestException {

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsCrmContact edsCrmContact = crmContactManager.get(item_id);
        if (edsCrmContact == null || edsCrmContact.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Lead with id " + item_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (ServerUtils.hasPermission(PermissionConstants.CRM_LEAD_DELETE)) {
            try {
                ArrayList<Integer> objectIDs = new ArrayList<>();
                objectIDs.add(item_id);
                contactServiceLocal.deleteContacts(objectIDs, null, false);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
        }

        return successResponse(new ResponseData());
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of state of fields")})
    @RequestMapping(value = "/{entity_type}/required_state", method = RequestMethod.GET)
    public Object getEntityFieldsState(@PathVariable("entity_type") String entity_type) throws RestException {

        LinkedHashMap<String, FormProperty> fields = new LinkedHashMap<>();
        EdsFormProperty edsFormProperty = formPropertyManager.getByFormID(FORM_TYPES.get(entity_type));
        if (edsFormProperty != null) {
            Gson gson = new Gson();
            FormProperty[] formFields = gson.fromJson(edsFormProperty.getSettingsJSONData(), FormProperty[].class);
            for (FormProperty formProperty : formFields) {
                if (formProperty != null) {
                    if (formProperty.getDefaultValue() != null && formProperty.getDefaultValue().length() == 0) {
                        formProperty.setDefaultValue(null);
                    }
                    if (formProperty.getRoleEdit() != null && formProperty.getRoleEdit().size() > 0) {
                        if (userManager.getUser().hasEitherRoles(formProperty.getRoleEdit().toArray(new Integer[]{}))) {
                            formProperty.setDisabled(false);
                        }
                    }
                    fields.put(formProperty.getCode(), formProperty);
                }
            }
        }
        ArrayList<FieldStateTO> fieldStates = new ArrayList<>();
        if ("leads".equalsIgnoreCase(entity_type)) {
            ArrayList<String> fieldTypes = new ArrayList<>();
            fieldTypes.add(CustomFormConstants.STATUS);
            fieldTypes.add(CustomFormConstants.PHONE);
            fieldTypes.add(CustomFormConstants.EMAIL);
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_NAME);
            fieldTypes.add(CustomFormConstants.ADDRESS);
            List<EdsModelField> modelFields = modelFieldManager.getSpecificFields(LayoutRPC.LEAD_FORM, fieldTypes);
            if (modelFields != null) {
                modelFields.forEach(edsModelField -> {
                    if (!edsModelField.isHide()) {
                        FieldStateTO fieldStateTO = new FieldStateTO();
                        fieldStateTO.setField(getFieldStateRelation(edsModelField.getField_ID()));
                        fieldStateTO.setRequired(edsModelField.isMandatory() || edsModelField.isSystemMandatory());
                        fieldStates.add(fieldStateTO);
                    }
                });
            }
        } else if ("accounts".equalsIgnoreCase(entity_type)) {
            ArrayList<String> fieldTypes = new ArrayList<>();
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_OWNER);
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_NAME);
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_PARENT);
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_TYPE);
            fieldTypes.add(CustomFormConstants.PRIMARY_CONTACT_ADDRESSES);
            fieldTypes.add(CustomFormConstants.PRIMARY_CONTACT);
            List<EdsModelField> modelFields = modelFieldManager.getSpecificFields(LayoutRPC.ACCOUNT_FORM, fieldTypes);
            if (modelFields != null) {
                modelFields.forEach(edsModelField -> {
                    if (!edsModelField.isHide()) {
                        FieldStateTO fieldStateTO = new FieldStateTO();
                        fieldStateTO.setField(getFieldStateRelation(edsModelField.getField_ID()));
                        fieldStateTO.setRequired(edsModelField.isSystemMandatory() || edsModelField.isMandatory());
                        fieldStates.add(fieldStateTO);
                    }
                });
            }
        } else if ("suppliers".equalsIgnoreCase(entity_type)) {
            ArrayList<String> fieldTypes = new ArrayList<>();
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_OWNER);
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_NAME);
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_PARENT);
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_TYPE);
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_BILLING_ADDRESS);
            fieldTypes.add(CustomFormConstants.PRIMARY_CONTACT);
            List<EdsModelField> modelFields = modelFieldManager.getSpecificFields(LayoutRPC.SUPPLIER_FORM, fieldTypes);
            if (modelFields != null) {
                modelFields.forEach(edsModelField -> {
                    if (!edsModelField.isHide()) {
                        FieldStateTO fieldStateTO = new FieldStateTO();
                        fieldStateTO.setField(getFieldStateRelation(edsModelField.getField_ID()));
                        fieldStateTO.setRequired(edsModelField.isSystemMandatory() || edsModelField.isMandatory());
                        fieldStates.add(fieldStateTO);
                    }
                });
            }
        } else if ("customers".equalsIgnoreCase(entity_type)) {
            ArrayList<String> fieldTypes = new ArrayList<>();
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_OWNER);
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_NAME);
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_PARENT);
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_TYPE);
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_BILLING_ADDRESS);
            fieldTypes.add(CustomFormConstants.PRIMARY_CONTACT);
            List<EdsModelField> modelFields = modelFieldManager.getSpecificFields(LayoutRPC.CLIENT_FORM, fieldTypes);
            if (modelFields != null) {
                modelFields.forEach(edsModelField -> {
                    if (!edsModelField.isHide()) {
                        FieldStateTO fieldStateTO = new FieldStateTO();
                        fieldStateTO.setField(getFieldStateRelation(edsModelField.getField_ID()));
                        fieldStateTO.setRequired(edsModelField.isSystemMandatory() || edsModelField.isMandatory());
                        fieldStates.add(fieldStateTO);
                    }
                });
            }
        } else if ("contacts".equalsIgnoreCase(entity_type)) {
            ArrayList<String> fieldTypes = new ArrayList<>();
            fieldTypes.add(CustomFormConstants.PHONE);
            fieldTypes.add(CustomFormConstants.EMAIL);
            fieldTypes.add(CustomFormConstants.CRM_ACCOUNT_NAME);
            fieldTypes.add(CustomFormConstants.ADDRESS);
            fieldTypes.add(CustomFormConstants.REPORTS_TO);
            List<EdsModelField> modelFields = modelFieldManager.getSpecificFields(LayoutRPC.CONTACT_FORM, fieldTypes);
            if (modelFields != null) {
                modelFields.forEach(edsModelField -> {
                    if (!edsModelField.isHide()) {
                        FieldStateTO fieldStateTO = new FieldStateTO();
                        fieldStateTO.setField(getFieldStateRelation(edsModelField.getField_ID()));
                        fieldStateTO.setRequired(edsModelField.isSystemMandatory() || edsModelField.isMandatory());
                        fieldStates.add(fieldStateTO);
                    }
                });
            }
        } else if ("opportunities".equalsIgnoreCase(entity_type)) {
            ArrayList<String> fieldTypes = new ArrayList<>();
            fieldTypes.add(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE);
//            fieldTypes.add(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE);
            fieldTypes.add(CustomFormConstants.CRM_OPPORTUNITY_NAME);
            fieldTypes.add(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT);
            fieldTypes.add(CustomFormConstants.CURRENCY);
            fieldTypes.add(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE);
            fieldTypes.add(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME);
            fieldTypes.add(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME);
            fieldTypes.add(CustomFormConstants.CRM_OPPORTUNITY_STAGE);
            List<EdsModelField> modelFields = modelFieldManager.getSpecificFields(LayoutRPC.OPPORTUNITY_FORM, fieldTypes);
            List<EdsCustomFormSection> sectionsList = customFormSectionManager.getSections(LayoutRPC.OPPORTUNITY_FORM);
            LinkedHashMap<String, EdsCustomFormSection> sections = new LinkedHashMap<>();
            sectionsList.forEach(sec -> {
                if (sec.getLabel() == null) {
                    sec.setLabel(modelFieldLocalizer.localizeOpportunity(sec.getSection()));
                }
                sections.put(sec.getSection(), sec);
            });
            if (modelFields != null) {
                modelFields.forEach(edsModelField -> {
                    if (!edsModelField.isHide()) {
                        FormProperty formProperty = fields.get(edsModelField.getField_ID());
                        FieldStateTO fieldStateTO = new FieldStateTO();
                        fieldStateTO.setField(getFieldStateRelation(edsModelField.getField_ID()));
                        fieldStateTO.setRequired(edsModelField.isSystemMandatory() || edsModelField.isMandatory() || formProperty.isRequired());
                        fieldStateTO.setCanEdit(!formProperty.isDisabled());
                        fieldStateTO.addProperty("disabled", formProperty.isDisabled());
                        fieldStateTO.addProperty("title", formProperty.isChanged() ? formProperty.getTitle() : modelFieldLocalizer.localizeByFieldID(FORM_TYPES.get(entity_type), edsModelField.getField_ID()));
                        fieldStateTO.addProperty("section", sections.get(edsModelField.getFsection()).getLabel());
                        fieldStateTO.addProperty("colType", edsModelField.getColumnType().name());
                        fieldStateTO.addProperty("order", edsModelField.getForder());
                        fieldStates.add(fieldStateTO);
                    }
                });
            }
        } else if ("tasks".equalsIgnoreCase(entity_type)) {
            ArrayList<String> fieldTypes = new ArrayList<>();
            fieldTypes.add(CustomFormConstants.TASK.PROJECT);
            fieldTypes.add(CustomFormConstants.NAME);
            fieldTypes.add(CustomFormConstants.DESCRIPTION);
            fieldTypes.add(CustomFormConstants.START_DATE);
            fieldTypes.add(CustomFormConstants.DUE_DATE);
            fieldTypes.add(CustomFormConstants.PRIORITY);
            fieldTypes.add(CustomFormConstants.STATUS);
            fieldTypes.add(CustomFormConstants.TASK.BILLIBLE);
            fieldTypes.add(CustomFormConstants.ASSIGNEE);
            fieldTypes.add(CustomFormConstants.TASK.PREDECESSOR_TASK);
            fieldTypes.add(CustomFormConstants.TASK.SUCCESSOR_TASK);
            fieldTypes.add(CustomFormConstants.WORKSTREAM.DUE_DATE_REMINDER);
            fieldTypes.add(CustomFormConstants.WORKSTREAM.PARENT_WORKSTREAM);
            List<EdsModelField> modelFields = modelFieldManager.getSpecificFields(LayoutRPC.TASK_MAX_FORM, fieldTypes);
            if (modelFields != null) {
                modelFields.forEach(edsModelField -> {
                    if (!edsModelField.isHide()) {
                        FieldStateTO fieldStateTO = new FieldStateTO();
                        fieldStateTO.setField(getFieldStateRelation(edsModelField.getField_ID()));
                        fieldStateTO.setRequired(edsModelField.isSystemMandatory() || edsModelField.isMandatory());
                        fieldStates.add(fieldStateTO);
                    }
                });
            }
        } else {
            throw new RestException("entity_type must be one of leads/suppliers/accounts/customers/contacts/opportunities",
                    "entity_type must be one of leads/suppliers/accounts/customers/contacts/opportunities", NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        return successResponse(new ResponseListData<>(fieldStates));
    }

    @Operation(summary = "Get Entity Category List", description = "Get Categories for particular entities like leads, activities")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the list of categories for particular entities")})
    @RequestMapping(value = "/leads/{field_type}/categories", method = RequestMethod.GET)
    public Object getEntityFieldCategories(
            @PathVariable(value = "field_type") String field_type,
            @RequestParam(value = "custom_field_id", required = false) Integer custom_field_id,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        if (StringUtils.isBlank(field_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "field_type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (!EntityFieldTypeEnum.COMPANY.name().equals(field_type) && !EntityFieldTypeEnum.STATUS.name().equals(field_type) && !EntityFieldTypeEnum.CUSTOM.name().equals(field_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "field_type should be one of | COMPANY | STATUS | CUSTOM", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Integer start = (offset != null && offset > 0) ? offset : 0;
        Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;
        EdsUser user = userManager.getUser();
        EntityCategoryTO entityCategories = new EntityCategoryTO();
        ArrayList<CategoryTO> categories = new ArrayList<>();

        if (EntityFieldTypeEnum.COMPANY.name().equals(field_type)) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setObjectId(user.getObjectID());
            filterParameter.setStart(start);
            filterParameter.setLimit(maxLimit);
            filterParameter.setSearchKey(query);
            CrmAccountList crmAccountList;
            try {
                crmAccountList = crmServiceLocal.getCrmAccounts(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            entityCategories.setTotal_count(crmAccountList.getTotal());
            if (crmAccountList.getTotal() < (maxLimit + start)) {
                entityCategories.setLeft(0);
            } else {
                entityCategories.setLeft(crmAccountList.getTotal() - (start + maxLimit));
            }
            entityCategories.setCount(crmAccountList.getList() != null ? crmAccountList.getList().size() : 0);
            entityCategories.setOffset(start);
            if (crmAccountList.getList() != null) {
                crmAccountList.getList().forEach(crmAccountItem -> {
                    categories.add(new CategoryTO(crmAccountItem.getObjectId(), crmAccountItem.getName()));
                });
                entityCategories.setList(categories);
            }
        } else if (EntityFieldTypeEnum.STATUS.name().equals(field_type)) {
            ArrayList<EdsReference> leadStatusList;
            try {
                leadStatusList = (ArrayList) referenceManager.listReferences(EdsCrmContact._LEAD_STATUS);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (leadStatusList != null) {
                if (StringUtils.isNotBlank(query)) {
                    leadStatusList = (ArrayList) leadStatusList.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                }
                entityCategories.setTotal_count(leadStatusList.size());
                if (leadStatusList.size() < (maxLimit + start)) {
                    entityCategories.setLeft(0);
                } else {
                    entityCategories.setLeft(leadStatusList.size() - (start + maxLimit));
                }
                entityCategories.setOffset(start);
                ArrayList<EdsReference> subList = ListUtils.getSublistSmart(leadStatusList, start, maxLimit);
                entityCategories.setCount(subList.size());
                subList.forEach(edsReference -> {
                    CategoryTO category = new CategoryTO();
                    category.setId(edsReference.getObjectID());
                    category.setTitle(edsReference.getName());
                    categories.add(category);
                });
                entityCategories.setList(categories);
            }
        } else if (EntityFieldTypeEnum.CUSTOM.name().equals(field_type)) {
            if (custom_field_id == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "custom_field_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (custom_field_id < GAP_BTW_STATIC_AND_CUSTOM_FIELDS) {
                List<String> predefinedValuesList = getCustomFieldValue(custom_field_id);
                if (predefinedValuesList != null) {
                    if (StringUtils.isNotBlank(query)) {
                        predefinedValuesList = predefinedValuesList.stream().filter(item -> item.toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                    }
                    entityCategories.setTotal_count(predefinedValuesList.size());
                    if (predefinedValuesList.size() < (maxLimit + start)) {
                        entityCategories.setLeft(0);
                    } else {
                        entityCategories.setLeft(predefinedValuesList.size() - (start + maxLimit));
                    }
                    ArrayList<String> stringArrayList = new ArrayList<>(predefinedValuesList);
                    ArrayList<String> sublist = ListUtils.getSublistSmart(stringArrayList, start, maxLimit);
                    entityCategories.setCount(sublist.size());
                    entityCategories.setOffset(start);

                    int id = 0;
                    for (String values : sublist) {
                        if (StringUtils.isNotBlank(values)) {
                            CategoryTO category = new CategoryTO();
                            category.setId(++id);
                            category.setTitle(values);
                            categories.add(category);
                        }
                    }
                    entityCategories.setList(categories);
                }

            } else {
                int real_model_field_id = custom_field_id - GAP_BTW_STATIC_AND_CUSTOM_FIELDS;
                EdsModelField modelField = modelFieldManager.get(real_model_field_id, true);

                if (modelField == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Custom field with id " + custom_field_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                }

                if (CustomFormConstants.LEAD_SOURCE.equals(modelField.getField_ID())) {
                    ArrayList<EdsReference> leadSources;
                    try {
                        leadSources = (ArrayList) referenceManager.listReferences(EdsCrmContact._LEAD_SOURCE);
                    } catch (Exception e) {
                        log.error("", e);
                        throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    if (leadSources != null) {
                        if (StringUtils.isNotBlank(query)) {
                            leadSources = (ArrayList) leadSources.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                        }
                        entityCategories.setTotal_count(leadSources.size());
                        if (leadSources.size() < (maxLimit + start)) {
                            entityCategories.setLeft(0);
                        } else {
                            entityCategories.setLeft(leadSources.size() - (start + maxLimit));
                        }
                        ArrayList<EdsReference> subList = ListUtils.getSublistSmart(leadSources, start, maxLimit);
                        entityCategories.setCount(subList.size());
                        entityCategories.setOffset(start);
                        subList.forEach(edsReference -> {
                            CategoryTO category = new CategoryTO();
                            category.setId(edsReference.getObjectID());
                            category.setTitle(edsReference.getName());
                            categories.add(category);
                        });
                        entityCategories.setList(categories);
                    }
                } else if (CustomFormConstants.CRM_CAMPAIGN_NAME.equals(modelField.getField_ID())) {
                    ListingFilterParameter filterParameters = new ListingFilterParameter();
                    filterParameters.setCRM(true);
                    ArrayList<EdsCampaign> campaignList;
                    try {
                        campaignList = (ArrayList) campaignManager.getCampaignList(filterParameters);
                    } catch (Exception e) {
                        log.error("", e);
                        throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    if (campaignList != null) {
                        if (StringUtils.isNotBlank(query)) {
                            campaignList = (ArrayList) campaignList.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                        }
                        entityCategories.setTotal_count(campaignList.size());
                        if (campaignList.size() < (maxLimit + start)) {
                            entityCategories.setLeft(0);
                        } else {
                            entityCategories.setLeft(campaignList.size() - (start + maxLimit));
                        }
                        ArrayList<EdsCampaign> subList = ListUtils.getSublistSmart(campaignList, start, maxLimit);
                        entityCategories.setCount(subList.size());
                        entityCategories.setOffset(start);
                        subList.forEach(edsCampaign -> {
                            CategoryTO category = new CategoryTO();
                            category.setId(edsCampaign.getObjectID());
                            category.setTitle(edsCampaign.getName());
                            categories.add(category);
                        });
                        entityCategories.setList(categories);
                    }


                } else if (CustomFormConstants.RATING.equals(modelField.getField_ID())) {
                    ArrayList<EdsReference> leadRatings;
                    try {
                        leadRatings = (ArrayList) referenceManager.listReferences(EdsCrmContact._LEAD_RATING);
                    } catch (Exception e) {
                        log.error("", e);
                        throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    if (leadRatings != null) {
                        if (StringUtils.isNotBlank(query)) {
                            leadRatings = (ArrayList) leadRatings.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                        }
                        entityCategories.setTotal_count(leadRatings.size());
                        if (leadRatings.size() < (maxLimit + start)) {
                            entityCategories.setLeft(0);
                        } else {
                            entityCategories.setLeft(leadRatings.size() - (start + maxLimit));
                        }
                        ArrayList<EdsReference> subList = ListUtils.getSublistSmart(leadRatings, start, maxLimit);
                        entityCategories.setCount(subList.size());
                        entityCategories.setOffset(start);
                        subList.forEach(edsReference -> {
                            CategoryTO category = new CategoryTO();
                            category.setId(edsReference.getObjectID());
                            category.setTitle(edsReference.getName());
                            categories.add(category);
                        });
                        entityCategories.setList(categories);
                    }

                } else if (CustomFormConstants.LEAD_OWNER.equals(modelField.getField_ID()) || CustomFormConstants.ASSIGNEE.equals(modelField.getField_ID()) ||
                        CustomFormConstants.BACKUP_ASSIGNEE.equals(modelField.getField_ID())) {
                    SelectItem[] salesPeople = crmServiceLocal.getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE);
                    getLeadItems(query, start, maxLimit, entityCategories, categories, salesPeople);
                } else if (CustomFormConstants.CRM_ACCOUNT_INDUSTRY.equals(modelField.getField_ID())) {
                    SelectItem[] leadItems = contactServiceLocal.getContactSelectItems(Constants._COMPANY_WORKAREA);
                    getLeadItems(query, start, maxLimit, entityCategories, categories, leadItems);
                } else if (CustomFormConstants.CRM_ACCOUNT_ORGANIZATION_TYPE.equals(modelField.getField_ID())) {
                    SelectItem[] organizationTypes = contactServiceLocal.getContactSelectItems(Constants.CONTACT_ORGANIZATION_TYPES);
                    getLeadItems(query, start, maxLimit, entityCategories, categories, organizationTypes);
//                } else if (CustomFormConstants.CRM_ACCOUNT_OWNERSHIP.equals(modelField.getField_ID())) {
//                    SelectItem[] ownerships = contactServiceLocal.getOwnerships();
//                    getLeadItems(query, start, maxLimit, entityCategories, categories, ownerships);
                } else if (CustomFormConstants.CRM_ACCOUNT_ANNUAL_REVENUE.equals(modelField.getField_ID())) {
                    SelectItem[] annualRevenues = contactServiceLocal.getContactSelectItems(Constants.ANNUAL_REVENUE);
                    getLeadItems(query, start, maxLimit, entityCategories, categories, annualRevenues);
                } else if (CustomFormConstants.CRM_ACCOUNT_NUMBER_OF_EMPLOYEE.equals(modelField.getField_ID())) {
                    SelectItem[] annualRevenues = contactServiceLocal.getContactSelectItems(Constants.NUMBER_OF_EMPLOYEES);
                    getLeadItems(query, start, maxLimit, entityCategories, categories, annualRevenues);
                } else if (CustomFormConstants.CRM_ACCOUNT_TYPE.equals(modelField.getField_ID())) {
                    SelectItem[] accountTypes = contactServiceLocal.getAccountTypes();
                    getLeadItems(query, start, maxLimit, entityCategories, categories, accountTypes);
                }
            }
        }
        return successResponse(entityCategories);
    }

    private void getLeadItems(@RequestParam(value = "query", required = false) String query, Integer start, Integer maxLimit, EntityCategoryTO entityCategories, ArrayList<CategoryTO> categories, SelectItem[] leadItems) {
        if (leadItems != null) {
            List<SelectItem> leadItemList = Arrays.asList(leadItems);

            if (StringUtils.isNotBlank(query)) {
                leadItemList = leadItemList.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
            }
            entityCategories.setTotal_count(leadItemList.size());
            if (leadItemList.size() < (maxLimit + start)) {
                entityCategories.setLeft(0);
            } else {
                entityCategories.setLeft(leadItemList.size() - (start + maxLimit));
            }
            ArrayList<SelectItem> stringArrayList = new ArrayList<>(leadItemList);
            ArrayList<SelectItem> sublist = ListUtils.getSublistSmart(stringArrayList, start, maxLimit);
            entityCategories.setCount(sublist.size());
            entityCategories.setOffset(start);
            for (SelectItem item : sublist) {
                if (item != null) {
                    CategoryTO category = new CategoryTO();
                    category.setId(item.getId());
                    category.setTitle(item.getName());
                    categories.add(category);
                }
            }
            entityCategories.setList(categories);
        }
    }

    @Operation(summary = "Convert lead to opportunity", description = "Convert particular lead to new opportunity. id of person to whom opportunity will be assigned")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/leads/{id}/convert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_LEADS_LIST, PermissionConstants.CRM_LEAD_CONVERT})
    public Object convertLeadToOpportunity(@PathVariable(value = "id") Integer id,
                                           @RequestBody LeadConvertOpportunityTO leadConvertOpportunityTO) throws RestException {

        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(leadConvertOpportunityTO.getName())) {
            throw new RestException("Name is required", "name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (leadConvertOpportunityTO.getAssigned_id() == null || leadConvertOpportunityTO.getAssigned_id() <= 0) {
            throw new RestException("Assignee is required", "assigned_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (leadConvertOpportunityTO.getStatus_id() != null && leadConvertOpportunityTO.getStatus_id() == 0) {
            throw new RestException("Status is required", "status_id can not be zero", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        HashMap<Integer, OpportunityListItem> items = new HashMap<>();

        boolean withOpportunity = leadConvertOpportunityTO.getAdd_to_account() != null && leadConvertOpportunityTO.getAdd_to_account();
        if (withOpportunity) {
            OpportunityListItem item = new OpportunityListItem();
            item.setAssigneeId(leadConvertOpportunityTO.getAssigned_id());
            item.setOpportunityName(leadConvertOpportunityTO.getName());
            if (leadConvertOpportunityTO.getAmount() != null) {
                item.setAmount(leadConvertOpportunityTO.getAmount().doubleValue());
            }
            item.setStage(new SelectItem(leadConvertOpportunityTO.getStatus_id()));
            item.setStageId(leadConvertOpportunityTO.getStatus_id());
            item.setCopyLeadDetails(leadConvertOpportunityTO.getCopy_details() == null ? false : leadConvertOpportunityTO.getCopy_details());

            items.put(id, item);
        } else {
            items.put(id, null);
        }
        try {
            crmServiceLocal.convertLead(items, withOpportunity);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage() != null ? e.getMessage() : e.toString(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return successResponse(new ResponseData());
    }

    @Operation(summary = "Create Lead", description = "Request to create new lead. It's multipart request")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/leads/create", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_LEADS_LIST, PermissionConstants.ADD_NEW_LEAD})
    public Object createLead(MultipartRequest multipartRequest, @Parameter(name = "body", description = """
            {
              "first_name": "firstname",
              "last_name": "lastname",
              "status": 1,
              "phone_number": "phone",
              "email": "email",
              "company": 1,
              "contact_addresses": [
                {
                  "name": "name",
                  "line_1": "addr line 1",
                  "line_2": "addr line 2",
                  "city": "city",
                  "post_code": "zipcode",
                  "is_primary": true,
                  "state_code": "statecode",
                  "country_code": "countrycode",
                  "type": "BUSINESS"
                }
              ]
            }""", schema = @Schema(type = "string")) @RequestParam(name = "body") String jsonString) throws RestException {

        LeadAddTO leadAddTO;
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.warn("REQUEST TO LEAD API:");
            log.warn("LEAD REQUEST BODY:" + jsonString);
            leadAddTO = mapper.readValue(jsonString, LeadAddTO.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong.".concat(e.getMessage()), REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(leadAddTO.getFirst_name())) {
            throw new RestException("First name is required", "first_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(leadAddTO.getLast_name())) {
            throw new RestException("Last name is required", "last_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        HashSet<Integer> draftFilesIdSet = new HashSet<>();
        if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
            for (MultipartFile file : multipartRequest.getFileMap().values()) {
                if (file.getName().matches(customFieldFileNameRegex)) {
                    String[] fileName = file.getName().split("_");
                    draftFilesIdSet.add(Integer.valueOf(fileName[2]));
                }
            }
        }

        //Collect custom fields ids
        Set<Integer> customFieldIdSet = new HashSet<>();
        if (leadAddTO.getCustom_fields() != null) {
            for (Object customFieldObject : leadAddTO.getCustom_fields()) {
                LinkedHashMap<Object, Object> customFieldsMap = (LinkedHashMap<Object, Object>) customFieldObject;
                Integer customFieldId = (Integer) customFieldsMap.get("id");
                if (customFieldId != null) {
                    customFieldIdSet.add(customFieldId);
                }
            }
        }

        //Merge custom field ids with draft files ids
        for (Integer draftFileId : draftFilesIdSet) {
            if (!customFieldIdSet.contains(draftFileId)) {
                LinkedHashMap<Object, Object> customFieldsMap = new LinkedHashMap<>();
                customFieldsMap.put("id", draftFileId);
                customFieldsMap.put("draft_files", new ArrayList<>());
                leadAddTO.getCustom_fields().add(customFieldsMap);
            }
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        ContactListItem item = new ContactListItem();
        item.setContactType(CrmConstants.TYPE_LEAD_CONTACT);
        item.setCheckForDuplicates(false);

        item.setFirstName(leadAddTO.getFirst_name());
        item.setLastName(leadAddTO.getLast_name());

        if (StringUtils.isNotBlank(leadAddTO.getEmail())) {
            if (!EMAIL_PATTERN.matcher(leadAddTO.getEmail()).matches()) {
                throw new RestException("Invalid email address", "Invalid email address", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            item.setPrimaryEmail(leadAddTO.getEmail());
            HashMap<Integer, ArrayList<String>> emailParam = new HashMap<>();
            ArrayList<String> emails = new ArrayList<>();
            emails.add(leadAddTO.getEmail());
            emailParam.put(Constants.G_WORK, emails);
            item.setEmails(emailParam);
        }

        if (StringUtils.isNotBlank(leadAddTO.getPhone_number())) {
            item.setPrimaryPhone(leadAddTO.getPhone_number());
            HashMap<Integer, ArrayList<String>> phoneParam = new HashMap<>();
            ArrayList<String> phones = new ArrayList<>();
            phones.add(leadAddTO.getPhone_number());
            phoneParam.put(Constants.G_WORK, phones);
            item.setPhones(phoneParam);
        }

        if (leadAddTO.getStatus() != null && leadAddTO.getStatus() > 0) {
            EdsReference status = (EdsReference) referenceManager.get(EdsReference.class, leadAddTO.getStatus());
            if (status != null) {
                item.setLeadStatus(status.getAsSelectItem());
            }
        }

        CrmAccountItem crmAccount = new CrmAccountItem();
        if (leadAddTO.getCompany() != null && leadAddTO.getCompany() > 0) {
            EdsCrmAccount edsCrmAccount = (EdsCrmAccount) crmAccountManager.get(EdsCrmAccount.class, leadAddTO.getCompany());
            if (edsCrmAccount != null) {
                crmAccount.setObjectId(edsCrmAccount.getObjectID());
                crmAccount.setName(edsCrmAccount.getName());
                item.setCrmAccount(crmAccount);
            }
        }

        if (leadAddTO.getContact_addresses() != null && leadAddTO.getContact_addresses().size() > 0) {
            for (ContactAddressAddTO addressTO : leadAddTO.getContact_addresses()) {
                Address address = new Address();
                address.setName(addressTO.getName());
                address.setAddress(addressTO.getLine_1());
                address.setAddressb(addressTO.getLine_2());
                address.setCity(addressTO.getCity());
                if (addressTO.getCountry() != null) {
                    address.setCountry(addressTO.getCountry().getTitle());
                    address.setCountryId(addressTO.getCountry().getId());
                }
                if (addressTO.getState() != null) {
                    address.setState(addressTO.getState().getTitle());
                    address.setStateId(addressTO.getState().getId());
                }
                address.setZipCode(addressTO.getPost_code());
                address.setPrimary(addressTO.getIs_primary() != null ? addressTO.getIs_primary() : false);

                address.setEntityType(EdsAddress.ENTITY_TYPE_CONTACT);
                if (ContactParamEnum.HOME.getCode().equals(addressTO.getType())) {
                    address.setRelationType(EdsAddress.HOME);
                } else if (ContactParamEnum.WORK.getCode().equals(addressTO.getType())) {
                    address.setRelationType(EdsAddress.WORK);
                } else if (ContactParamEnum.OTHER.getCode().equals(addressTO.getType())) {
                    address.setRelationType(EdsAddress.OTHER);
                }
                address.setPrimary(addressTO.getIs_primary() != null ? addressTO.getIs_primary() : false);

                item.getAddresses().add(address);
            }
        }

        EdsUser user = userFilterManager.getUser();

        Integer attachmentModelFieldId = null;
        //Find lead avatar
        MultipartFile leadAvatarMultipartFile = null;
        //Map key is model field id that related to attachment, and value is attachment
        LinkedHashMap<Integer, ArrayList<MultipartFile>> attachmentsMap = new LinkedHashMap<>();
        ArrayList<MultipartFile> leadAttachments = new ArrayList<>();
        if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
            for (MultipartFile file : multipartRequest.getFileMap().values()) {
                if (file.getName().equals("avatar")) {//Find lead avatar image
                    leadAvatarMultipartFile = file;
                } else if (file.getName().matches(customFieldFileNameRegex)) {
                    String[] fileName = file.getName().split("_");
                    Integer id = Integer.valueOf(fileName[2]);
                    if (id > GAP_BTW_STATIC_AND_CUSTOM_FIELDS) {
                        id = id - GAP_BTW_STATIC_AND_CUSTOM_FIELDS;
                        ArrayList<MultipartFile> files;
                        if (attachmentsMap.get(id) == null) {
                            files = new ArrayList<>();
                            files.add(file);
                            attachmentsMap.put(id, files);
                        } else {
                            attachmentsMap.get(id).add(file);
                        }
                    }
                }
            }
        }

        //Get model fields by entity type
        List<ModelField> modelFields;
        try {
            modelFields = modelFieldManager.getFields(FORM_TYPES.get(EntityTypeEnum.LEADS.name().toLowerCase()));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //Map key is model field id and man value is model field field_id. e.g 1,CRM_OPPORTUNITY_STAGE
        LinkedHashMap<Integer, String> modelFieldsMap = new LinkedHashMap<>();
        if (modelFields != null && modelFields.size() > 0) {
            modelFields.forEach(modelField -> modelFieldsMap.put(modelField.getObjectID(), modelField.getField_ID()));
        }

        //Map key is model field field_id and value is field value. e.g. CRM_OPPORTUNITY_STAGE, Close Won
        LinkedHashMap<String, Object> modelFieldValueMap = new LinkedHashMap<>();

        //Separate real custom fields and fields that are given as custom field by GAP_BTW_STATIC_AND_CUSTOM_FIELDS
        ArrayList<Object> customFieldObjects = new ArrayList<>();
        if (leadAddTO.getCustom_fields() != null && !leadAddTO.getCustom_fields().isEmpty()) {
            for (Object customFieldObject : leadAddTO.getCustom_fields()) {
                LinkedHashMap<Object, Object> customFieldsMap = (LinkedHashMap<Object, Object>) customFieldObject;
                if (customFieldsMap.get("id") != null) {
                    if ((Integer) customFieldsMap.get("id") < GAP_BTW_STATIC_AND_CUSTOM_FIELDS) {//it means real custom field
                        customFieldObjects.add(customFieldsMap);
                    } else {
                        String fieldID = modelFieldsMap.get((Integer) customFieldsMap.get("id") - GAP_BTW_STATIC_AND_CUSTOM_FIELDS);//it means model field
                        if (StringUtils.isNotBlank(fieldID)) {
                            if (StringUtils.isNotBlank((String) customFieldsMap.get("text"))) {//for text fields
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("text"));
                            } else if (customFieldsMap.get("value") != null) {//for number fields
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("value"));
                            } else if (customFieldsMap.get("category_id") != null) {//for drop down fields
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("category_id"));
                            } else if (customFieldsMap.get("choosed_ids") != null) {//for multi drop down fields
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("choosed_ids"));
                            } else if (customFieldsMap.get("date") != null) {//for date fields
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("date"));
                            } else if (customFieldsMap.get("draft_files") != null) {//for attachments
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("draft_files"));
                                attachmentModelFieldId = (Integer) customFieldsMap.get("id") - GAP_BTW_STATIC_AND_CUSTOM_FIELDS;
                            }
                        }
                    }
                }
            }
        }

        ArrayList<CompanyCustomFieldItem> customFieldItems;
        if (customFieldObjects.size() > 0) {
            customFieldItems = convertCustomFields(customFieldObjects, multipartRequest);
            if (customFieldItems != null && customFieldItems.size() > 0) {
                item.setCustomFields(customFieldItems);
            }
        }

        HistoryListItem note = null;
        for (String fieldID : modelFieldValueMap.keySet()) {
            if (CustomFormConstants.TITLE.equals(fieldID)) {
                item.setTitle((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.MIDDLE_NAME.equals(fieldID)) {
                item.setMiddleName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.FIRST_NAME.equals(fieldID)) {
                item.setFirstName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.OTHER_NAME.equals(fieldID)) {
                item.setOtherName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.LAST_NAME.equals(fieldID)) {
                item.setLastName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.BIRTH_DAY.equals(fieldID)) {
                try {
                    item.setBirthDate(new DateNonConvertable(longDateTimezoneFormat.parse((String) modelFieldValueMap.get(fieldID))));
                } catch (ParseException e) {
                    log.error("", e);
                }
            } else if (CustomFormConstants.CRM_ACCOUNT_NAME.equals(fieldID)) {
                item.setObjectId((Integer) modelFieldValueMap.get(fieldID));

            } else if (CustomFormConstants.CRM_ACCOUNT_TYPE.equals(fieldID)) {
                ArrayList<Integer> accountTypeIDs = (ArrayList<Integer>) modelFieldValueMap.get(fieldID);
                ArrayList<SelectItem> accountTypes = new ArrayList<>();
                for (Integer accountTypeID : accountTypeIDs) {
                    EdsReference edsReference = referenceManager.get(accountTypeID);
                    SelectItem accountType = new SelectItem(accountTypeID, true);
                    accountType.setReferenceCode(edsReference != null ? edsReference.getCode() : null);
                    accountTypes.add(accountType);
                }
                crmAccount.setAccountTypes(accountTypes.toArray(new SelectItem[0]));
            } else if (CustomFormConstants.JOB_TITLE.equals(fieldID)) {
                item.setJobTitle((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_ORGANIZATION_TYPE.equals(fieldID)) {
                crmAccount.setOrganizationTypeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.DEPARTMENT.equals(fieldID)) {
                item.setDepartmentID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_ANNUAL_REVENUE.equals(fieldID)) {
                crmAccount.setAnnualRevenueID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.REF_IND_NUMBER.equals(fieldID)) {
                item.setRefIndNumber((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.ASSETS.equals(fieldID)) {
                item.setAssets((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_INDUSTRY.equals(fieldID)) {
                crmAccount.setIndustryID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_OWNERSHIP.equals(fieldID)) {
                crmAccount.setOwnershipId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_NUMBER_OF_EMPLOYEE.equals(fieldID)) {
                crmAccount.setNumberOfEmployeeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.LEAD_OWNER.equals(fieldID)) {
                item.setOwnerId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.LEAD_NAME.equals(fieldID)) {
                item.setFirstName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CATEGORY.equals(fieldID)) {
                ArrayList<SelectItem> selectedCategories = new ArrayList<>();
                ArrayList<Integer> categoryIDs = (ArrayList<Integer>) modelFieldValueMap.get(fieldID);
                for (Integer categoryId : categoryIDs) {
                    selectedCategories.add(new SelectItem(categoryId));
                }
                item.setSelectedCategories(selectedCategories);
            } else if (CustomFormConstants.RELATIONSHIP.equals(fieldID)) {
                if (item.getRelationships() != null && item.getRelationships().length > 0) {
                    SelectItem relationship = item.getRelationships()[0];
                    relationship.setName((String) modelFieldValueMap.get(fieldID));
                    relationship.setDescription((String) modelFieldValueMap.get(fieldID));
                    ArrayList<SelectItem> selectedRelationships = new ArrayList<>();
                    selectedRelationships.add(relationship);
                    item.setSelectedRelationships(selectedRelationships);
                }
            } else if (CustomFormConstants.REPORTS_TO.equals(fieldID)) {
                item.setReportsToId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.OWNER.equals(fieldID)) {
                item.setOwnerId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_CAMPAIGN_NAME.equals(fieldID)) {
                item.setCampaignId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.EMAIL_OPT_OUT.equals(fieldID)) {
                item.setEmailOptOut((Boolean) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.ASSIGNEE.equals(fieldID)) {
                item.setLeadAssigneeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.BACKUP_ASSIGNEE.equals(fieldID)) {
                item.setLeadBackupAssigneeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.LEAD_SOURCE.equals(fieldID)) {
                item.setLeadSourceID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.STATUS.equals(fieldID)) {
                item.setLeadStatusID((Integer) modelFieldValueMap.get(fieldID));
                item.setLeadStatus(new SelectItem((Integer) modelFieldValueMap.get(fieldID)));
            } else if (CustomFormConstants.RATING.equals(fieldID)) {
                item.setLeadRatingID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.IM_ADDRESS.equals(fieldID)) {
                HashMap<Integer, ArrayList<String>> paramMap = new HashMap<>();
                ArrayList<String> gtalk = new ArrayList<>();
                gtalk.add((String) modelFieldValueMap.get(fieldID));
                paramMap.put(ContactParamEnum.GOOGLE_TALK.getId(), gtalk);
                item.setImAddresses(paramMap);
            } else if (CustomFormConstants.WEB_ADDRESS.equals(fieldID)) {
                HashMap<Integer, ArrayList<String>> paramMap = new HashMap<>();
                ArrayList<String> workAddress = new ArrayList<>();
                workAddress.add((String) modelFieldValueMap.get(fieldID));
                paramMap.put(ContactParamEnum.WORK.getId(), workAddress);
                item.setWebAddresses(paramMap);
            } else if (CustomFormConstants.NOTES.equals(fieldID) || CustomFormConstants.CRM_NOTE.equals(fieldID)) {
                if (StringUtils.isNotBlank((String) modelFieldValueMap.get(fieldID))) {
                    note = new HistoryListItem((String) modelFieldValueMap.get(fieldID));
                }
            } else if (CustomFormConstants.ATTACHMENTS.equals(fieldID)) {
                if (attachmentModelFieldId != null && attachmentsMap.get(attachmentModelFieldId) != null) {
                    leadAttachments.addAll(attachmentsMap.get(attachmentModelFieldId));
                }
            }
        }

        item.setCrmAccount(crmAccount);

        Integer leadId;
        try {
            leadId = crmServiceLocal.saveLead(item, null);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (note != null) {
            try {
                note.setRelatedToId(EdsNoteHistory.CRM_LEAD);
                note.setRelatedId(leadId);
                noteServiceLocal.saveNote(note);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


        if (leadAvatarMultipartFile != null) {
            CreateDocumentCommand documentCommand = new CreateDocumentCommand();
            documentCommand.setImgType(leadAvatarMultipartFile.getOriginalFilename().substring(leadAvatarMultipartFile.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase(Locale.ENGLISH));
            documentCommand.setCompanyID(user.getCompany().getObjectID());
            documentCommand.setFolderName("static");
            documentCommand.setNotdownloadable("YES");
            WfmMultipartFile multipartFile = new WfmMultipartFile("", leadAvatarMultipartFile);
            documentCommand.addFile(multipartFile);
            try {
                CreateAttachmentHandler createAttachmentHandler = (CreateAttachmentHandler) ApplicationContextProvider.applicationContext.getBean("createAttachmentHandler");
                createAttachmentHandler.execute(documentCommand);
                String[] result = createAttachmentHandler.getResult();
                if (result != null && result.length > 0) {
                    commonServiceLocal.saveCrmContactImageUrl(Integer.valueOf(result[0]), leadId);
                }
            } catch (Throwable throwable) {
                log.error(throwable.getMessage());
            }
        }

        if (leadAttachments.size() > 0) {
            FolderResource folderResource = documentsServiceLocal.getFolderResource(Constants.F_LEAD, leadId);
            for (MultipartFile multipartFile : leadAttachments) {
                try {
                    documentsServiceLocal.saveDocumentFile(multipartFile, folderResource.getObjectId(), folderResource.getFileType(), leadId, null);
                } catch (Exception e) {
                    log.error("", e);
                    throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }

        return successResponse(new IdNameTO(leadId, null));
    }

    @Operation(summary = "Update lead information", description = "It's multipart request.It will have part called avatar for lead avatar image, if user will set any.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/leads/{item_id}/information", method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_LEADS_LIST, PermissionConstants.CRM_LEAD_EDIT})
    public Object updateLead(MultipartRequest multipartRequest,
                             @PathVariable(value = "item_id") Integer item_id,
                             @RequestParam(name = "body") String jsonString) throws RestException {

        LeadAddTO leadAddTO;
        ObjectMapper mapper = new ObjectMapper();
        try {
            leadAddTO = mapper.readValue(jsonString, LeadAddTO.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong.", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ContactListItem item = crmServiceLocal.editLead(item_id, null);
        item.setCheckForDuplicates(false);
        item.setObjectId(item_id);
        if (StringUtils.isNotBlank(leadAddTO.getFirst_name())) {
            item.setFirstName(leadAddTO.getFirst_name());
        }
        if (StringUtils.isNotBlank(leadAddTO.getLast_name())) {
            item.setLastName(leadAddTO.getLast_name());
        }

        if (leadAddTO.getStatus() != null && leadAddTO.getStatus() > 0) {
            EdsReference status = (EdsReference) referenceManager.get(EdsReference.class, leadAddTO.getStatus());
            if (status != null) {
                item.setLeadStatus(status.getAsSelectItem());
            }
        }

        if (leadAddTO.getCompany() != null && leadAddTO.getCompany() > 0) {
            EdsCrmAccount edsCrmAccount = (EdsCrmAccount) crmAccountManager.get(EdsCrmAccount.class, leadAddTO.getCompany());
            if (edsCrmAccount != null) {
                CrmAccountItem crmAccount = new CrmAccountItem();
                crmAccount.setObjectId(edsCrmAccount.getObjectID());
                crmAccount.setName(edsCrmAccount.getName());
                item.setCrmAccount(crmAccount);
            }
        }

        if (StringUtils.isNotBlank(leadAddTO.getEmail())) {
            if (!EMAIL_PATTERN.matcher(leadAddTO.getEmail()).matches()) {
                throw new RestException("Invalid email address", "Invalid email address", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            String primaryEmail = null;
            if (StringUtils.isNotBlank(item.getPrimaryEmail())) {
                primaryEmail = item.getPrimaryEmail();
            }
            HashMap<Integer, ArrayList<String>> emailParams;
            if (StringUtils.isBlank(primaryEmail)) {//if there is no lead primary email, set api provided email as WORK email
                emailParams = new HashMap<>();
                ArrayList<String> emails = new ArrayList<>();
                emails.add(leadAddTO.getEmail());
                emailParams.put(Constants.G_WORK, emails);
                item.setEmails(emailParams);
            } else {
                //this lead has primary email, find the primary email type (WORK,HOME,OTHER) and update the email to api provided email
                // employee old primary email will be updated
                emailParams = ContactListItem.getItemParamsAsMap(item, Constants.CONTACT_EMAILS);

                for (HashMap.Entry<Integer, ArrayList<String>> entry : emailParams.entrySet()) {
                    if (entry != null) {
                        switch (entry.getKey()) {
                            case Constants.G_HOME -> {
                                ArrayList<String> homeEmails = entry.getValue();
                                for (int i = 0; i < homeEmails.size(); i++) {
                                    if (homeEmails.get(i).equals(primaryEmail)) {
                                        homeEmails.set(i, leadAddTO.getEmail());
                                        break;
                                    }
                                }
                                emailParams.put(Constants.G_HOME, homeEmails);
                            }
                            case Constants.G_WORK -> {
                                ArrayList<String> workEmails = entry.getValue();
                                for (int i = 0; i < workEmails.size(); i++) {
                                    if (workEmails.get(i).equals(primaryEmail)) {
                                        workEmails.set(i, leadAddTO.getEmail());
                                        break;
                                    }
                                }
                                emailParams.put(Constants.G_WORK, workEmails);
                            }
                            case Constants.G_OTHER -> {
                                ArrayList<String> otherEmails = entry.getValue();
                                for (int i = 0; i < otherEmails.size(); i++) {
                                    if (otherEmails.get(i).equals(primaryEmail)) {
                                        otherEmails.set(i, leadAddTO.getEmail());
                                        break;
                                    }
                                }
                                emailParams.put(Constants.G_OTHER, otherEmails);
                            }
                        }
                    }
                }
            }

            item.setPrimaryEmail(leadAddTO.getEmail());
            item.setEmails(emailParams);
        }

        if (StringUtils.isNotBlank(leadAddTO.getPhone_number())) {

            HashMap<Integer, ArrayList<String>> phoneParams;

            String primaryPhone = null;
            if (StringUtils.isNotBlank(item.getPrimaryPhone())) {
                primaryPhone = item.getPrimaryPhone();
            }
            if (primaryPhone == null) {//if there is no lead primary phone, set api provided phone as MOBILE email
                phoneParams = new HashMap<>();
                ArrayList<String> phonesPhones = new ArrayList<>();
                phonesPhones.add(leadAddTO.getPhone_number());
                phoneParams.put(Constants.G_WORK, phonesPhones);
                item.setPhones(phoneParams);
            } else {
                phoneParams = ContactListItem.getItemParamsAsMap(item, Constants.CONTACT_PHONES);
                //this user has primary phone, find the primary phone type (WORK,WORK,MOBILE) and update the phone to api provided phone
                // employee old primary email will be update
                for (HashMap.Entry<Integer, ArrayList<String>> entry : phoneParams.entrySet()) {
                    if (entry != null) {
                        switch (entry.getKey()) {
                            case Constants.G_HOME -> {
                                ArrayList<String> homePhones = entry.getValue();
                                for (int i = 0; i < homePhones.size(); i++) {
                                    if (homePhones.get(i).equals(primaryPhone)) {
                                        homePhones.set(i, leadAddTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_HOME, homePhones);
                            }
                            case Constants.G_WORK -> {
                                ArrayList<String> workPhones = entry.getValue();
                                for (int i = 0; i < workPhones.size(); i++) {
                                    if (workPhones.get(i).equals(primaryPhone)) {
                                        workPhones.set(i, leadAddTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_WORK, workPhones);
                            }
                            case Constants.G_MOBILE -> {
                                ArrayList<String> mobilePhones = entry.getValue();
                                for (int i = 0; i < mobilePhones.size(); i++) {
                                    if (mobilePhones.get(i).equals(primaryPhone)) {
                                        mobilePhones.set(i, leadAddTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_MOBILE, mobilePhones);
                            }
                            case Constants.G_HOME_FAX -> {
                                ArrayList<String> homeFax = entry.getValue();
                                for (int i = 0; i < homeFax.size(); i++) {
                                    if (homeFax.get(i).equals(primaryPhone)) {
                                        homeFax.set(i, leadAddTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_HOME_FAX, homeFax);
                            }
                            case Constants.G_WORK_FAX -> {
                                ArrayList<String> workFax = entry.getValue();
                                for (int i = 0; i < workFax.size(); i++) {
                                    if (workFax.get(i).equals(primaryPhone)) {
                                        workFax.set(i, leadAddTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_WORK_FAX, workFax);
                            }
                            case Constants.G_PAGER -> {
                                ArrayList<String> pagers = entry.getValue();
                                for (int i = 0; i < pagers.size(); i++) {
                                    if (pagers.get(i).equals(primaryPhone)) {
                                        pagers.set(i, leadAddTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_PAGER, pagers);
                            }
                            case Constants.G_OTHER -> {
                                ArrayList<String> others = entry.getValue();
                                for (int i = 0; i < others.size(); i++) {
                                    if (others.get(i).equals(primaryPhone)) {
                                        others.set(i, leadAddTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_OTHER, others);
                            }
                            case Constants.G_EXTENSION -> {
                                ArrayList<String> extentions = entry.getValue();
                                for (int i = 0; i < extentions.size(); i++) {
                                    if (extentions.get(i).equals(primaryPhone)) {
                                        extentions.set(i, leadAddTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_EXTENSION, extentions);
                            }
                            case Constants.G_FAX -> {
                                ArrayList<String> faxes = entry.getValue();
                                for (int i = 0; i < faxes.size(); i++) {
                                    if (faxes.get(i).equals(primaryPhone)) {
                                        faxes.set(i, leadAddTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_FAX, faxes);
                            }
                            case Constants.G_WHATS_APP -> {
                                ArrayList<String> whatsApps = entry.getValue();
                                for (int i = 0; i < whatsApps.size(); i++) {
                                    if (whatsApps.get(i).equals(primaryPhone)) {
                                        whatsApps.set(i, leadAddTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_WHATS_APP, whatsApps);
                            }
                            case Constants.G_TELEGRAM -> {
                                ArrayList<String> telegram = entry.getValue();
                                for (int i = 0; i < telegram.size(); i++) {
                                    if (telegram.get(i).equals(primaryPhone)) {
                                        telegram.set(i, leadAddTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_TELEGRAM, telegram);
                            }
                            case Constants.G_VIBER -> {
                                ArrayList<String> vibers = entry.getValue();
                                for (int i = 0; i < vibers.size(); i++) {
                                    if (vibers.get(i).equals(primaryPhone)) {
                                        vibers.set(i, leadAddTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_VIBER, vibers);
                            }
                        }
                    }
                }
            }
            item.setPrimaryPhone(leadAddTO.getPhone_number());
            item.setPhones(phoneParams);
        }
        /*if (leadAddTO.getNotes() != null && leadAddTO.getNotes().size() > 0) {
            HistoryList historyList = new HistoryList();
            ArrayList<HistoryListItem> newNotes = new ArrayList<>();
                for (NoteDto note : leadAddTO.getNotes()) {
                    if (note.getId() != null && note.getId() > 0) {
                        for (HistoryListItem historyListItem : item.getHistory().getResult()) {
                            if (historyListItem.getObjectID().equals(note.getId())) {
                                if (note.getText() != null) {
                                    historyListItem.setComment(note.getText());
                                }
                                if (note.getVisibility() != null) {
                                    if (NoteEnum.PRIVATE.getName().equalsIgnoreCase(note.getVisibility())) {
                                        historyListItem.setVisibility(true);
                                    } else if (NoteEnum.PUBLIC.getName().equalsIgnoreCase(note.getVisibility())) {
                                        historyListItem.setVisibility(false);
                                    }
                                }
                                historyListItem.setEntityID(item_id);
                            }
                        }
                    }
                        HistoryListItem historyListItem = new HistoryListItem();
                        historyListItem.setEntityID(item_id);
                        historyListItem.setComment(note.getText());
//                        historyListItem.setObjectID(note.getId());
                        if (NoteEnum.PRIVATE.getName().equalsIgnoreCase(note.getVisibility())) {
                            historyListItem.setVisibility(true);
                        } else if (NoteEnum.PUBLIC.getName().equalsIgnoreCase(note.getVisibility())) {
                        historyListItem.setVisibility(false);
                        }
                        newNotes.add(historyListItem);

                }
            HistoryListItem[] notesArray = new HistoryListItem[newNotes.size()];
            newNotes.toArray(notesArray);
            historyList.setResult(notesArray);
            item.setHistory(historyList);
        }*/

        if (leadAddTO.getNotes() != null && leadAddTO.getNotes().size() > 0) {
            try {
                for (NoteDto noteDto : leadAddTO.getNotes()) {
                    HistoryListItem note = new HistoryListItem();
                    note.setRelatedToId(EdsNoteHistory.CRM_LEAD);
                    note.setRelatedId(item_id);
                    note.setComment(noteDto.getText());
                    if (noteDto.getId() != null) {
                        note.setObjectID(noteDto.getId());
                    }
                    if (noteDto.getVisibility() != null) {
                        if (NoteEnum.PRIVATE.getName().equalsIgnoreCase(noteDto.getVisibility())) {
                            note.setVisibility(true);
                        } else if (NoteEnum.PUBLIC.getName().equalsIgnoreCase(noteDto.getVisibility())) {
                            note.setVisibility(false);
                        }
                    }
                    noteServiceLocal.saveNote(note);
                }
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


        Integer leadId;
        try {
            leadId = contactServiceLocal.saveContact(item, null, false);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
            for (MultipartFile file : multipartRequest.getFileMap().values()) {
                if (file.getName().equals("avatar")) {//Find lead avatar image
                    CreateDocumentCommand documentCommand = new CreateDocumentCommand();
                    documentCommand.setImgType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase(Locale.ENGLISH));
                    documentCommand.setCompanyID(userManager.getUser().getCompany().getObjectID());
                    documentCommand.setFolderName("static");
                    documentCommand.setNotdownloadable("YES");
                    WfmMultipartFile multipartFile = new WfmMultipartFile("", file);
                    documentCommand.addFile(multipartFile);
                    try {
                        CreateAttachmentHandler createAttachmentHandler = (CreateAttachmentHandler) ApplicationContextProvider.applicationContext.getBean("createAttachmentHandler");
                        createAttachmentHandler.execute(documentCommand);
                        String[] result = createAttachmentHandler.getResult();
                        if (result != null && result.length > 0) {
                            commonServiceLocal.saveCrmContactImageUrl(Integer.valueOf(result[0]), leadId);
                        }
                    } catch (Throwable throwable) {
                        log.error(throwable.getMessage());
                    }
                    break;
                }
            }
        }


        return successResponse(new ResponseData());
    }


    @Operation(summary = "Update Lead Additional Information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/leads/{item_id}/additional_information",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_LEADS_LIST, PermissionConstants.CRM_LEAD_EDIT})
    public Object updateLeadAdditionalInformation(
            @PathVariable(value = "item_id") Integer item_id,
            MultipartRequest multipartRequest,
            @RequestParam(name = "body") String jsonString) throws RestException {


        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        LinkedHashMap<Object, ArrayList<Object>> customFieldsObjectMap;
        ObjectMapper mapper = new ObjectMapper();
        try {
            customFieldsObjectMap = (LinkedHashMap<Object, ArrayList<Object>>) mapper.readValue(jsonString, Object.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong. ".concat(e.getMessage()), REQUIRED, HttpStatus.BAD_REQUEST);
        }

        //Get model fields by entity type
        List<ModelField> modelFields;
        try {
            modelFields = modelFieldManager.getFields(LayoutRPC.LEAD_FORM);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ContactListItem contactListItem = crmServiceLocal.editLead(item_id, null);

        //Map key is model field id and man value is model field field_id. e.g 1,CRM_OPPORTUNITY_STAGE
        LinkedHashMap<Integer, String> modelFieldsMap = new LinkedHashMap<>();
        if (modelFields != null && modelFields.size() > 0) {
            modelFields.forEach(modelField -> modelFieldsMap.put(modelField.getObjectID(), modelField.getField_ID()));
        }

        HashSet<Integer> draftFilesIdSet = new HashSet<>();
        if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
            for (MultipartFile file : multipartRequest.getFileMap().values()) {
                if (file.getName().matches(customFieldFileNameRegex)) {
                    String[] fileName = file.getName().split("_");
                    draftFilesIdSet.add(Integer.valueOf(fileName[2]));
                }
            }
        }

        //Collect custom fields ids
        Set<Integer> customFieldIdSet = new HashSet<>();
        for (Object customFieldObject : customFieldsObjectMap.get("list")) {
            LinkedHashMap<Object, Object> customFieldsMap = (LinkedHashMap<Object, Object>) customFieldObject;
            Integer customFieldId = null;
            if (customFieldsMap != null) {
                customFieldId = (Integer) customFieldsMap.get("id");
            }
            if (customFieldId != null) {
                customFieldIdSet.add(customFieldId);
            }
        }

        //Merge custom field ids with draft files ids
        for (Integer draftFileId : draftFilesIdSet) {
            if (!customFieldIdSet.contains(draftFileId)) {
                LinkedHashMap<Object, Object> customFieldsMap = new LinkedHashMap<>();
                customFieldsMap.put("id", draftFileId);
                customFieldsMap.put("draft_files", new ArrayList<>());
                customFieldsObjectMap.get("list").add(customFieldsMap);
            }
        }

        //Map key is model field field_id and value is field value. e.g. CRM_OPPORTUNITY_STAGE, Close Won
        LinkedHashMap<String, Object> modelFieldValueMap = new LinkedHashMap<>();

        //Separate real custom fields and fields that are given as custom field by GAP_BTW_STATIC_AND_CUSTOM_FIELDS
        ArrayList<Object> customFieldObjects = new ArrayList<>();

        separateCustomFields(customFieldsObjectMap, modelFieldsMap, customFieldObjects, modelFieldValueMap);

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        ArrayList<CompanyCustomFieldItem> customFieldItems = null;
        LinkedHashMap<Integer, ArrayList<AttachmentTO>> customFieldDraftAttachmentMap = new LinkedHashMap<>();
        if (customFieldObjects.size() > 0) {
            customFieldItems = convertCustomFields(customFieldObjects, customFieldDraftAttachmentMap);
        }

        //Create Map of MultipartFiles with custom field Id as key
        TreeMap<Integer, ArrayList<MultipartFile>> customFieldAttachmentsMap = getCustomFieldAttachmentsMap(multipartRequest, customFieldFileNameRegex);

        contactListItem.setCheckForDuplicates(false);

        //Compare draft files to old files by unique keys: filename & file size. If there is a difference between them by name or size, delete the differ old files
        // but keep other non changed files
        ArrayList<FileResource> oldAttachments = new ArrayList<>();
        HashSet<Integer> deleteIDs = new HashSet<>();

        if (contactListItem.getCustomFields() != null && contactListItem.getCustomFields().size() > 0) {
            for (CompanyCustomFieldItem companyCustomFieldItem : contactListItem.getCustomFields()) {
                if (Constants.UI_TYPE_FILE_UPLOAD_WIDGET.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_FILE_UPLOAD_ITEM.equals(companyCustomFieldItem.getUiType())) {
                    ArrayList<FileResource> fileResources = documentsServiceLocal.getFileResources(Constants.F_CUSTOM_FIELD_ITEM, companyCustomFieldItem.getEntityId(), companyCustomFieldItem.getObjectId());
                    if (fileResources != null && fileResources.size() > 0) {
                        oldAttachments.addAll(fileResources);
                    }
                }
            }
        }

        //if draft attachments are empty, remove all old custom field attachments.
        if (customFieldDraftAttachmentMap.isEmpty()) {
            if (oldAttachments.size() > 0) {
                List<Integer> oldAttachmentIDs = new ArrayList<>();
                for (FileResource fileResource : oldAttachments) {
                    oldAttachmentIDs.add(fileResource.getObjectId());
                }
                try {
                    documentsServiceLocal.deleteFiles(oldAttachmentIDs);
                    oldAttachments.clear();
                } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                    log.error("", e);
                }
            }
        } else {//if draft attachments do not match with cash advance old attachments by filename and file size, delete not matched old attachments
            if (oldAttachments.size() > 0) {
                LinkedHashMap<String, String> draftAttachmentMap = new LinkedHashMap<>();
                for (ArrayList<AttachmentTO> draftAttachments : customFieldDraftAttachmentMap.values()) {
                    for (AttachmentTO draftAttachment : draftAttachments) {
                        draftAttachmentMap.put(draftAttachment.getFile_name(), draftAttachment.getFile_name());
                    }
                }
                for (FileResource oldAttachment : oldAttachments) {
                    String draftFilename = draftAttachmentMap.get(oldAttachment.getFileName());
                    if (StringUtils.isNotBlank(draftFilename)) {
                        FileResource fileResource = documentsServiceLocal.getFileResourceByFileTypeAndName(Constants.F_CUSTOM_FIELD_ITEM, draftFilename);
                        if (fileResource != null && !fileResource.getContentLength().equals(oldAttachment.getContentLength())) {
                            deleteIDs.add(oldAttachment.getObjectId());
                        }
                    } else {
                        deleteIDs.add(oldAttachment.getObjectId());
                    }
                }
                if (deleteIDs.size() > 0) {
                    try {
                        documentsServiceLocal.deleteFiles(new ArrayList<>(deleteIDs));
                    } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                        log.error("", e);
                    }
                }

                //after delete old attachments, get not deleted attachment as old attachments
                oldAttachments.clear();
                if (contactListItem.getCustomFields() != null && contactListItem.getCustomFields().size() > 0) {
                    for (CompanyCustomFieldItem companyCustomFieldItem : contactListItem.getCustomFields()) {
                        ArrayList<FileResource> fileResources = documentsServiceLocal.getFileResources(Constants.F_CUSTOM_FIELD_ITEM, companyCustomFieldItem.getEntityId(), companyCustomFieldItem.getObjectId());
                        if (fileResources != null && fileResources.size() > 0) {
                            oldAttachments.addAll(fileResources);
                        }
                    }
                }
            }
        }

        EdsUser user = userManager.getUser();
        FolderResource tempFolder = documentsServiceLocal.getTempFolderByCompany(user.getCompany().getObjectID());

        try {
            if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
                //if old files are empty, upload new files
                if (oldAttachments.size() == 0) {
                    if (customFieldItems != null && customFieldItems.size() > 0) {
                        for (CompanyCustomFieldItem companyCustomFieldItem : customFieldItems) {
                            if (Constants.UI_TYPE_FILE_UPLOAD_WIDGET.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_FILE_UPLOAD_ITEM.equals(companyCustomFieldItem.getUiType())) {
                                ArrayList<FileItem> attachments = new ArrayList<>();
                                for (MultipartFile multipartFile : customFieldAttachmentsMap.get(companyCustomFieldItem.getEntityId())) {
                                    FileResource fileResource = documentsServiceLocal.saveDocumentFile(multipartFile, tempFolder.getObjectId(), Constants.F_CUSTOM_FIELD_ITEM, null, "");
                                    FileItem fileItem = new FileItem();
                                    fileItem.setId(fileResource.getObjectId());
                                    fileItem.setFileName(fileResource.getFileName());
                                    attachments.add(fileItem);
                                }
                                companyCustomFieldItem.setAttachments(attachments.toArray(new FileItem[]{}));
                            }
                        }
                    }
                } else {//If old files aren't empty, merge old and new files
                    deleteIDs = new HashSet<>();
                    LinkedHashMap<String, FileResource> oldFilesMap = new LinkedHashMap<>();
                    for (FileResource file : oldAttachments) {
                        oldFilesMap.put(file.getFileName(), file);
                    }

                    for (MultipartFile multipartFile : multipartRequest.getFileMap().values()) {
                        FileResource oldFile = oldFilesMap.get(multipartFile.getOriginalFilename());
                        if (oldFile != null) {
                            deleteIDs.add(oldFile.getObjectId());
                        }
                    }

                    if (deleteIDs.size() > 0) {
                        try {
                            documentsServiceLocal.deleteFiles(new ArrayList<>(deleteIDs));
                        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                            log.error("", e);
                        }
                    }

                    if (customFieldItems != null && customFieldItems.size() > 0) {
                        for (CompanyCustomFieldItem companyCustomFieldItem : customFieldItems) {
                            if (Constants.UI_TYPE_FILE_UPLOAD_WIDGET.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_FILE_UPLOAD_ITEM.equals(companyCustomFieldItem.getUiType())) {
                                ArrayList<FileItem> attachments = new ArrayList<>();
                                for (MultipartFile multipartFile : customFieldAttachmentsMap.get(companyCustomFieldItem.getEntityId())) {
                                    FileResource fileResource = documentsServiceLocal.saveDocumentFile(multipartFile, tempFolder.getObjectId(), Constants.F_CUSTOM_FIELD_ITEM, null, "");
                                    FileItem fileItem = new FileItem();
                                    fileItem.setId(fileResource.getObjectId());
                                    fileItem.setFileName(fileResource.getFileName());
                                    attachments.add(fileItem);
                                }
                                companyCustomFieldItem.setAttachments(attachments.toArray(new FileItem[]{}));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }


        if (customFieldItems != null && customFieldItems.size() > 0) {
            contactListItem.setCustomFields(customFieldItems);
        }

        CrmAccountItem account = contactListItem.getCrmAccount();

        for (String fieldID : modelFieldValueMap.keySet()) {
            if (CustomFormConstants.TITLE.equals(fieldID)) {
                contactListItem.setTitle((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.MIDDLE_NAME.equals(fieldID)) {
                contactListItem.setMiddleName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.FIRST_NAME.equals(fieldID)) {
                contactListItem.setFirstName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.OTHER_NAME.equals(fieldID)) {
                contactListItem.setOtherName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.LAST_NAME.equals(fieldID)) {
                contactListItem.setLastName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.BIRTH_DAY.equals(fieldID)) {
                try {
                    contactListItem.setBirthDate(new DateNonConvertable(longDateTimezoneFormat.parse((String) modelFieldValueMap.get(fieldID))));
                } catch (ParseException e) {
                    log.error("", e);
                }
            } else if (CustomFormConstants.CRM_ACCOUNT_NAME.equals(fieldID)) {
                account.setObjectId((Integer) modelFieldValueMap.get(fieldID));

            } else if (CustomFormConstants.CRM_ACCOUNT_TYPE.equals(fieldID)) {
                ArrayList<Integer> accountTypeIDs = (ArrayList<Integer>) modelFieldValueMap.get(fieldID);
                ArrayList<SelectItem> accountTypes = new ArrayList<>();
                for (Integer accountTypeID : accountTypeIDs) {
                    EdsReference edsReference = referenceManager.get(accountTypeID);
                    SelectItem accountType = new SelectItem(accountTypeID, true);
                    accountType.setReferenceCode(edsReference != null ? edsReference.getCode() : null);
                    accountTypes.add(accountType);
                }
                account.setAccountTypes(accountTypes.toArray(new SelectItem[0]));
            } else if (CustomFormConstants.JOB_TITLE.equals(fieldID)) {
                contactListItem.setJobTitle((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_ORGANIZATION_TYPE.equals(fieldID)) {
                account.setOrganizationTypeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.DEPARTMENT.equals(fieldID)) {
                contactListItem.setDepartmentID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_ANNUAL_REVENUE.equals(fieldID)) {
                account.setAnnualRevenueID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.REF_IND_NUMBER.equals(fieldID)) {
                contactListItem.setRefIndNumber((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.ASSETS.equals(fieldID)) {
                contactListItem.setAssets((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_INDUSTRY.equals(fieldID)) {
                account.setIndustryID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_OWNERSHIP.equals(fieldID)) {
                account.setOwnershipId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_NUMBER_OF_EMPLOYEE.equals(fieldID)) {
                account.setNumberOfEmployeeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.LEAD_OWNER.equals(fieldID)) {
                contactListItem.setOwnerId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.LEAD_NAME.equals(fieldID)) {
                contactListItem.setFirstName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CATEGORY.equals(fieldID)) {
                ArrayList<SelectItem> selectedCategories = new ArrayList<>();
                ArrayList<Integer> categoryIDs = (ArrayList<Integer>) modelFieldValueMap.get(fieldID);
                for (Integer categoryId : categoryIDs) {
                    selectedCategories.add(new SelectItem(categoryId));
                }
                contactListItem.setSelectedCategories(selectedCategories);
            } else if (CustomFormConstants.RELATIONSHIP.equals(fieldID)) {
                if (contactListItem.getRelationships() != null && contactListItem.getRelationships().length > 0) {
                    SelectItem relationship = contactListItem.getRelationships()[0];
                    relationship.setName((String) modelFieldValueMap.get(fieldID));
                    relationship.setDescription((String) modelFieldValueMap.get(fieldID));
                    ArrayList<SelectItem> selectedRelationships = new ArrayList<>();
                    selectedRelationships.add(relationship);
                    contactListItem.setSelectedRelationships(selectedRelationships);
                }
            } else if (CustomFormConstants.REPORTS_TO.equals(fieldID)) {
                contactListItem.setReportsToId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.OWNER.equals(fieldID)) {
                contactListItem.setOwnerId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_CAMPAIGN_NAME.equals(fieldID)) {
                contactListItem.setCampaignId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.EMAIL_OPT_OUT.equals(fieldID)) {
                contactListItem.setEmailOptOut((Boolean) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.ASSIGNEE.equals(fieldID)) {
                contactListItem.setLeadAssigneeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.BACKUP_ASSIGNEE.equals(fieldID)) {
                contactListItem.setLeadBackupAssigneeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.LEAD_SOURCE.equals(fieldID)) {
                contactListItem.setLeadSourceID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.STATUS.equals(fieldID)) {
                contactListItem.setLeadStatusID((Integer) modelFieldValueMap.get(fieldID));
                contactListItem.setLeadStatus(new SelectItem((Integer) modelFieldValueMap.get(fieldID)));
            } else if (CustomFormConstants.RATING.equals(fieldID)) {
                contactListItem.setLeadRatingID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.IM_ADDRESS.equals(fieldID)) {
                HashMap<Integer, ArrayList<String>> paramMap = new HashMap<>();
                ArrayList<String> gtalk = new ArrayList<>();
                gtalk.add((String) modelFieldValueMap.get(fieldID));
                paramMap.put(ContactParamEnum.GOOGLE_TALK.getId(), gtalk);
                contactListItem.setImAddresses(paramMap);
            } else if (CustomFormConstants.WEB_ADDRESS.equals(fieldID)) {
                HashMap<Integer, ArrayList<String>> paramMap = new HashMap<>();
                ArrayList<String> workAddress = new ArrayList<>();
                workAddress.add((String) modelFieldValueMap.get(fieldID));
                paramMap.put(ContactParamEnum.WORK.getId(), workAddress);
                contactListItem.setWebAddresses(paramMap);
            }
        }

        contactListItem.setCrmAccount(account);

        try {
            crmServiceLocal.saveLead(contactListItem, null);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return successResponse(new ResponseData());

    }


    @Operation(summary = "Get Lead Addresses")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have lead addresses"),
            @ApiResponse(responseCode = "400", description = "item_id is required")})
    @RequestMapping(value = "/leads/{item_id}/addresses", method = RequestMethod.GET)
    public Object getLeadAddresses(@PathVariable(value = "item_id") Integer item_id) throws RestException {
        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsCrmContact lead;
        try {
            lead = crmContactManager.get(item_id);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ArrayList<EntityContactAddressTO> entityAddresses = new ArrayList<>();
        if (lead != null) {
            List<EdsAddress> addresses = lead.getAddresses();
            if (addresses != null) {
                addresses.forEach(address -> {
                    EntityContactAddressTO entityAddress = new EntityContactAddressTO();
                    entityAddress.setItem_id(address.getObjectID());
                    if (ContactParamEnum.HOME.getId().equals(address.getRelationType())) {
                        entityAddress.setType(ContactParamEnum.HOME.getCode());
                    } else if (ContactParamEnum.WORK.getId().equals(address.getRelationType())) {
                        entityAddress.setType(ContactParamEnum.WORK.getCode());
                    } else {
                        entityAddress.setType(ContactParamEnum.OTHER.getCode());
                    }
                    if (StringUtils.isNotBlank(address.getName())) {
                        entityAddress.setName(address.getName());
                    }
                    if (StringUtils.isNotBlank(address.getAddress())) {
                        entityAddress.setLine_1(address.getAddress());
                    }
                    if (StringUtils.isNotBlank(address.getAddressb())) {
                        entityAddress.setLine_2(address.getAddressb());
                    }
                    if (StringUtils.isNotBlank(address.getCity())) {
                        entityAddress.setCity(address.getCity());
                    }
                    if (StringUtils.isNotBlank(address.getZipCode())) {
                        entityAddress.setPost_code(address.getZipCode());
                    }
                    entityAddress.setIs_primary(address.isPrimary());
                    if (address.getCountry() != null) {
                        CountriesListTO country = new CountriesListTO();
                        country.setId(address.getCountry().getObjectID());
                        country.setTitle(address.getCountry().getName());
                        country.setHas_states(address.getCountry().getStates() != null && address.getCountry().getStates().size() > 0);

                        entityAddress.setCountry(country);
                    }
                    if (address.getState() != null) {
                        entityAddress.setState(new CategoryTO(address.getState().getObjectID(), address.getState().getName()));
                    }
                    entityAddresses.add(entityAddress);
                });
            }
        }
        return successResponse(new ResponseListData<>(entityAddresses));
    }

    @Operation(summary = "Update Lead Addresses")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/leads/{item_id}/addresses", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_LEADS_LIST})
    public Object updateLeadAddresses(@PathVariable(value = "item_id") Integer item_id,
                                      @RequestBody EntityContactAddressListTO updateEntityContactAddressTO) throws RestException {

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ContactListItem contactListItem;
        try {
            contactListItem = crmServiceLocal.editLead(item_id, null);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<Address> addressList = new ArrayList<>();

        for (EntityContactAddressTO addressTO : updateEntityContactAddressTO.getList()) {
            Address address = new Address();
            //address.setObjectID(addressTO.getItem_id());
            address.setName(addressTO.getName());
            address.setAddress(addressTO.getLine_1());
            address.setAddressb(addressTO.getLine_2());
            address.setPrimary(addressTO.getIs_primary() != null ? addressTO.getIs_primary() : false);
            address.setCity(addressTO.getCity());
            if (addressTO.getCountry() != null) {
                address.setCountryId(addressTO.getCountry().getId());
                address.setCountry(addressTO.getCountry().getTitle());
            }
            if (addressTO.getState() != null) {
                address.setStateId(addressTO.getState().getId());
                address.setState(addressTO.getState().getTitle());
            }
            address.setZipCode(addressTO.getPost_code());
            address.setEntityID(item_id);
            address.setEntityType(EdsAddress.ENTITY_TYPE_CONTACT);
            Integer relationType = ContactParamEnum.getParamIdByCode(addressTO.getType());
            address.setRelationType(relationType != null ? relationType : EdsAddress.HOME);
            address.setPrimary(addressTO.getIs_primary() != null ? addressTO.getIs_primary() : false);

            addressList.add(address);
        }

        contactListItem.setAddresses(addressList);
        try {
            contactServiceLocal.saveContact(contactListItem, null, true);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return successResponse(new ResponseData());
    }

    @Operation(summary = "Get Lead Information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have lead information"),
            @ApiResponse(responseCode = "400", description = "item_id is required")})
    @RequestMapping(value = "/leads/{item_id}/information", method = RequestMethod.GET)
    public Object getLeadInformation(@PathVariable(value = "item_id") Integer item_id) throws RestException {
        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ContactListItem lead;
        try {
            lead = crmServiceLocal.getLead(item_id);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LeadAddTO leadInformation = new LeadAddTO();
        if (lead != null) {
            leadInformation.setFirst_name(lead.getFirstName());
            leadInformation.setLast_name(lead.getLastName());
            if (lead.getLeadStatusID() != null) {
                leadInformation.setStatus(lead.getLeadStatusID());
            }
            if (StringUtils.isNotBlank(lead.getPrimaryPhone())) {
                leadInformation.setPhone_number(lead.getPrimaryPhone());
            }
            if (StringUtils.isNotBlank(lead.getPrimaryEmail())) {
                leadInformation.setEmail(lead.getPrimaryEmail());
            }
            if (lead.getCrmAccount() != null) {
                leadInformation.setCompany(lead.getCrmAccount().getObjectId());
            }
            /*if (lead.getHistory() != null && lead.getHistory().getResult().length > 0) {
                ArrayList<NoteDto> notes = new ArrayList<>();
                for (HistoryListItem item: lead.getHistory().getResult()) {
                    NoteDto note = new NoteDto();
                    note.setText(item.getComment());
                    note.setId(item.getObjectID());
                    if (item.isVisibility() == null) {
                        note.setVisibility("INTERNAL");
                    } else if (item.isVisibility()) {
                        note.setVisibility("PRIVATE");
                    } else {
                        note.setVisibility("PUBLIC");
                    }
                }
                leadInformation.setNotes(notes);
            }*/
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setRelationType(RelationItem.TYPE_LEAD);
            fp.setRelationID(item_id);
            List<EdsNoteHistory> notes = noteHistoryManager.getNoteList(fp);
            if (notes != null && notes.size() > 0) {
                ArrayList<NoteDto> newNotes = new ArrayList<>();
                for (EdsNoteHistory noteHistory : notes) {
                    NoteDto noteDto = new NoteDto();
                    noteDto.setText(noteHistory.getComment());
                    noteDto.setEntityId(noteHistory.getEntityID());
                    noteDto.setId(noteHistory.getObjectID());
                    if (noteHistory.isVisibility() == null) {
                        noteDto.setVisibility("INTERNAL");
                    } else if (noteHistory.isVisibility()) {
                        noteDto.setVisibility("PRIVATE");
                    } else {
                        noteDto.setVisibility("PUBLIC");
                    }
                    newNotes.add(noteDto);
                }
                leadInformation.setNotes(newNotes);
            }
        }
        return successResponse(new EntityInformationResultTO(leadInformation));
    }

}
