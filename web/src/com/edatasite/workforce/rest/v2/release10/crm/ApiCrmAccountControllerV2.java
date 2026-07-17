package com.edatasite.workforce.rest.v2.release10.crm;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsFacetFilter;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsUserFilter;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.client.client.rpc.ClientServiceLocal;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
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
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ModelFieldManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.FacetFilterManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.UserFilterManager;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmAccountList;
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
import com.edatasite.workforce.rest.v2.release10.core.to.auth.PhoneTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CountriesListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CrmFieldStateTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.EntityCategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ItemTypeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldCategoryChooseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldTextTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CompanyAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CompanyInformationTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CompanyInformationUpdateTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CrmAccountDetailsItemResponseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CrmAccountDetailsItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CrmAccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EntityContactAddressListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EntityContactAddressTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EntityInformationResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.FilteredStatusesRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.note.GeneralNoteTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.CustomFieldsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskBaseInfoTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskDetailInfoTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.ColorTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.FlowSettingsTO;
import com.edatasite.workforce.rest.v2.release10.enums.CustomFieldCategoryEnum;
import com.edatasite.workforce.rest.v2.release10.enums.EntityFieldTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.EntityTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderByEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderFieldEnum;
import com.edatasite.workforce.rest.v2.release10.enums.TaskPriorityEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.ATTACHMENTS;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.CATEGORY;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.CRM_ACCOUNT_TYPE;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.CRM_OPPORTUNITY_ATTACHMENTS;

/**
 * Created by Abdurakhmonov Farrukh on 01/30/2018.
 */
@Tag(name = "CRM Account", description = "CRM Account API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiCrmAccountControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiCrmAccountControllerV2.class);
    private boolean isSupplier = true;

    @Autowired
    private FacetFilterManager facetFilterManager;
    @Autowired
    private HttpServletRequest servletRequest;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private UserFilterManager userFilterManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ModelFieldManager modelFieldManager;
    @Autowired
    private ClientServiceLocal clientServiceLocal;
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private ModelFieldLocalizer modelFieldLocalizer;
    @Autowired
    private NoteServiceLocal noteServiceLocal;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private EmployeeManager employeeManager;


    @Operation(summary = "Get company types list for company creation")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have company details"),
            @ApiResponse(responseCode = "400", description = "id is required")})
    @RequestMapping(value = "/{company_type}/company_types", method = RequestMethod.GET)
    public Object getCompanyTypes(@PathVariable(value = "company_type") String company_type) throws RestException {

        if (!"suppliers".equalsIgnoreCase(company_type) && !"customers".equalsIgnoreCase(company_type) && !"accounts".equalsIgnoreCase(company_type)) {
            throw new RestException("company_type must be one of suppliers/customers/accounts", "company_type must be one of suppliers/customers/accounts", NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        List<EdsReference> accountTypes = referenceManager.listReferences(EdsCrmAccount._CRM_ACCOUNT_TYPE);

        if (accountTypes == null) {
            return successResponse(new ResponseListData<>());
        }

        List<ItemTypeTO> result = accountTypes.stream().map(type -> new ItemTypeTO(type.getObjectID(), type.getCode(), type.getName())).collect(Collectors.toList());

        return successResponse(new ResponseListData<ItemTypeTO>(result));
    }

    @Operation(summary = "Company Detail Info")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have company details"),
            @ApiResponse(responseCode = "400", description = "id is required")})
    @RequestMapping(value = "/companies/{id}/details", method = RequestMethod.GET)
    public Object getCompanyDetails(@PathVariable(value = "id") Integer id) throws RestException {

        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsCrmAccount edsCrmAccount = crmAccountManager.get(id);
        if (edsCrmAccount == null || edsCrmAccount.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "No company found with + " + id + " id", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        CrmAccountDetailsItemTO crmAccountDetailsItem = new CrmAccountDetailsItemTO();

        //base_info
        CrmAccountTO crmAccountBaseInfo = new CrmAccountTO();
        crmAccountBaseInfo.setName(edsCrmAccount.getName());
        crmAccountBaseInfo.setItem_id(edsCrmAccount.getObjectID());
        if (edsCrmAccount.getLogo() != null) {
            crmAccountBaseInfo.setAvatar_image(commonServiceLocal.getImageUrl(edsCrmAccount.getLogo().getObjectID()));
        }
        crmAccountDetailsItem.setBase_info(crmAccountBaseInfo);

        if (edsCrmAccount.getPrimaryContact() != null) {
            //primary_contact
            ContactTO primaryContact = new ContactTO();
            primaryContact.setName(edsCrmAccount.getPrimaryContact().getName());
            primaryContact.setItem_id(edsCrmAccount.getPrimaryContact().getObjectID());
            if (edsCrmAccount.getPrimaryContact().getPhoto() != null) {
                primaryContact.setAvatar_image(commonServiceLocal.getImageUrl(edsCrmAccount.getPrimaryContact().getPhoto().getObjectID()));
            }

            ContactsTO contactsTO = new ContactsTO();
            //primary_contact Phones
            contactsTO.setPhones(contactServiceLocal.convertToPhoneTO(edsCrmAccount.getPrimaryContact()));

            //primary_contact Emails
            contactsTO.setEmails(contactServiceLocal.convertContactEmails(edsCrmAccount.getPrimaryContact()));

            primaryContact.setContacts(contactsTO);
            primaryContact.setCompany(crmAccountBaseInfo);

            crmAccountDetailsItem.setPrimary_contact(primaryContact);
        }

        //task
        //One of latest task
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(0);
        filterParameter.setLimit(1);
        filterParameter.setSortField(TaskListItem.ID);
        filterParameter.setRelationType(RelationItem.TYPE_CRM_ACCOUNT);
        filterParameter.setRelationID(edsCrmAccount.getObjectID());
        try {
            TaskList taskList = taskServiceLocal.getTaskList(filterParameter);
            if (taskList != null && taskList.getList() != null && taskList.getList().size() > 0) {
                TaskListItem taskListItem = taskList.getList().get(0);
                TaskDetailInfoTO task = new TaskDetailInfoTO();
                //base_info
                TaskBaseInfoTO taskBaseInfo = new TaskBaseInfoTO();
                taskBaseInfo.setName(taskListItem.getName());
                taskBaseInfo.setStatus_id(taskListItem.getTaskStatusId());
                taskBaseInfo.setItem_id(taskListItem.getObjectID());
                if (taskListItem.getDueDate() != null) {
                    taskBaseInfo.setDue_date(longDateTimezoneFormat.format(taskListItem.getDueDate()));
                }
                taskBaseInfo.setPriority(TaskPriorityEnum.get(taskListItem.getPriorityCode()));

                task.setBase_info(taskBaseInfo);

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

                crmAccountDetailsItem.setTask(task);
            }
        } catch (Exception e) {
            log.error("Error occurred while getting contact's tasks ", e);
        }

        //contacts
        ContactsTO contacts = new ContactsTO();

        ArrayList<PhoneTO> phones = new ArrayList<>();
        if (StringUtils.isNotBlank(edsCrmAccount.getPhone())) {
            String phoneNumber = edsCrmAccount.getPhone();
            if (phoneNumber.toLowerCase().startsWith("+")) {
                for (String phoneCode : getPhoneCountryCodes()) {
                    if (StringUtils.isNotBlank(phoneCode) && phoneNumber.startsWith(phoneCode)) {
                        phoneNumber = phoneNumber.replace(phoneCode, "");
                        if (StringUtils.isBlank(phoneNumber)) {
                            break;
                        }
                        PhoneTO phone = new PhoneTO();
                        phone.setPhone_number(phoneNumber);
                        phone.setCountry_code(phoneCode);
                        phones.add(phone);
                        break;
                    }
                }
            } else {
                PhoneTO phone = new PhoneTO();
                phone.setPhone_number(phoneNumber);
                phones.add(phone);
            }
        }
        contacts.setPhones(phones);

        ArrayList<String> emails = new ArrayList<>();

        if (StringUtils.isNotBlank(edsCrmAccount.getEmail())) {
            emails.add(edsCrmAccount.getEmail());
        }
        contacts.setEmails(emails);

        crmAccountDetailsItem.setContacts(contacts);

        //One of latest note
        filterParameter = new ListingFilterParameter();
        filterParameter.setRelationType(RelationItem.TYPE_CRM_ACCOUNT);
        filterParameter.setRelationID(id);
        filterParameter.setSortField(HistoryListItem.date);
        filterParameter.setAscending(false);
        filterParameter.setStart(0);
        filterParameter.setLimit(1);


        ListResult<HistoryListItem> noteHistoryList = null;
        try {
            noteHistoryList = noteServiceLocal.noteList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
        }

        if (noteHistoryList != null && noteHistoryList.getList() != null) {
            for (HistoryListItem noteHistory : noteHistoryList.getList()) {
                GeneralNoteTO generalNoteTO = new GeneralNoteTO();
                generalNoteTO.setId(noteHistory.getObjectID());
                generalNoteTO.setDate(longDateTimezoneFormat.format(noteHistory.getEventDate()));
                generalNoteTO.setNote_content(noteHistory.getComment());

                if (noteHistory.isVisibility() != null) {
                    generalNoteTO.setType(noteHistory.isVisibility() ? NoteEnum.PRIVATE.getCode() : NoteEnum.PUBLIC.getCode());
                } else {
                    generalNoteTO.setType(NoteEnum.INTERNAL.getCode());
                }
                if (noteHistory.getEmployee() != null) {

                    generalNoteTO.setOwner_id(noteHistory.getEmployeeID());
                    generalNoteTO.setOwner_name(noteHistory.getEmployee());

                    if (noteHistory.getEmployeeID() != null) {
                        EdsEmployee employee = employeeManager.get(noteHistory.getEmployeeID());
                        if (employee != null && employee.getPhoto() != null) {
                            generalNoteTO.setOwner_avatar(commonServiceLocal.getImageUrl(employee.getPhoto().getObjectID()));
                        }
                    }
                }
                crmAccountDetailsItem.setNote(generalNoteTO);
            }

        }

        CrmAccountDetailsItemResponseTO result = new CrmAccountDetailsItemResponseTO();
        result.setItem(crmAccountDetailsItem);

        return successResponse(result);
    }

    @RequestMapping(value = "/companies/items", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @CheckPermission(permissions = {PermissionConstants.CRM_ACCOUNTS_LIST})
    public Object getCompaniesList(@RequestBody ContactRequestTO requestTO) throws RestException {
        if (requestTO.getOffset() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Offset required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestTO.getOffset() < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Offset can not be less then zero", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestTO.getCount() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Count required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestTO.getCount() < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Count can not be less then zero", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestTO.getOffset() == 0 && requestTO.getCount() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Offset and count can not be zero at the same time", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        OrderFieldEnum orderFieldEnum = null;
        OrderByEnum orderByEnum = null;
        if (requestTO.getOrder() != null) {
            if (StringUtils.isNotBlank(requestTO.getOrder().getType())) {
                orderFieldEnum = OrderFieldEnum.getOrderField(requestTO.getOrder().getType());
                if (orderFieldEnum == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Type field should be one of ID, NAME, DATE, COMPANY", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            if (StringUtils.isNotBlank(requestTO.getOrder().getDirection())) {
                orderByEnum = OrderByEnum.getDirection(requestTO.getOrder().getDirection());
                if (orderByEnum == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Direction field should be one of ASC or DESC", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
        }

        final ArrayList<SelectItem> people = new ArrayList<>();
        final ArrayList<SelectItem> categories = new ArrayList<>();

        if (requestTO.getFilter() == null) {
            requestTO.setFilter(new FilteredStatusesRequestTO());
        }

        FacetFilterRpc mainMergedFilter = initializeDefaultCrmAccountFacetFilter();

        //Categories (Filters)
        if (requestTO.getFilter().getCategories_filter_id() != null && requestTO.getFilter().getCategories_filter_id().size() > 0) {

            for (Integer facetFilterId : requestTO.getFilter().getCategories_filter_id()) {
                //fill list to save it later
                categories.add(new SelectItem(facetFilterId, ""));
                EdsFacetFilter filter = facetFilterManager.getFacetFilter(facetFilterId);

                if (filter != null) {

                    FacetFilterRpc filterRpc = filter.getFacetFilter(new HashSet<>(mainMergedFilter.getShowFacetCodeName()));

                    if (filterRpc != null && !filterRpc.getFacetContentMap().isEmpty()) {
                        for (Map.Entry<String, FacetContentRpc> entry : filterRpc.getFacetContentMap().entrySet()) {
                            //Ignore assigned to filter because we expecting it to come from request
                            if (entry.getValue().getFacetItems().length > 0
                                /*&& !FacetContentType.CrmAccountFacetFilter.getContentCode()[0].equalsIgnoreCase(entry.getKey())*/) {
                                FacetContentRpc existingVals = mainMergedFilter.getFacetContentMap().get(entry.getKey());
                                if (existingVals != null) {
                                    ArrayList<SelectItem> existingItems = new ArrayList<>(Arrays.asList(existingVals.getFacetItems()));
                                    existingItems.addAll(Arrays.asList(entry.getValue().getFacetItems()));
                                    existingVals.setFacetItems(existingItems.toArray(new SelectItem[0]));
                                    mainMergedFilter.getFacetContentMap().put(entry.getKey(), existingVals);
                                } else {
                                    mainMergedFilter.getFacetContentMap().put(entry.getKey(), entry.getValue());
                                }
                            }
                        }
                    }
                }
            }
        }
        //AssignedTo
        if (requestTO.getFilter().getPeople_filter_id() != null && requestTO.getFilter().getPeople_filter_id().size() > 0) {

            FacetContentRpc existingAssignedTo = null;
            if (mainMergedFilter != null) {
                existingAssignedTo = mainMergedFilter.getFacetContentMap().get(FacetContentType.CrmAccountFacetFilter.getContentCode()[2]);
            }

            if (existingAssignedTo == null) {
                existingAssignedTo = new FacetContentRpc();
            }

            ArrayList<SelectItem> peopleItems = new ArrayList<>(Arrays.asList(existingAssignedTo.getFacetItems()));

            for (Integer assigneeId : requestTO.getFilter().getPeople_filter_id()) {
                SelectItem assignee = new SelectItem(assigneeId > 0 ? assigneeId : -1, "");
                peopleItems.add(assignee);
                //fill list to save it later
                people.add(assignee);
            }
            existingAssignedTo.setFacetItems(peopleItems.toArray(new SelectItem[0]));
            if (mainMergedFilter != null) {
                mainMergedFilter.getFacetContentMap().put(FacetContentType.CrmAccountFacetFilter.getContentCode()[2], existingAssignedTo);
            }
        }

        saveQuickFilter(people, categories);


        //If OneTimeFilter is active
        FacetFilterRpc oneTimeFilter = getOneTimeFilter(initializeDefaultCrmAccountFacetFilter());
        if (oneTimeFilter != null && oneTimeFilter.isFavourFilter()) {
            //Important to pass facetcodenames
            //FacetFilterRpc filterRpc = oneTimeFilter.getFacetFilter(new HashSet<>(filterParameter.getFacetFilter().getShowFacetCodeName()));
            if (!oneTimeFilter.getFacetContentMap().isEmpty()) {
                for (Map.Entry<String, FacetContentRpc> entry : oneTimeFilter.getFacetContentMap().entrySet()) {

                    //ignore status and assignedto fields
                    if (entry.getValue().getFacetItems().length > 0
                        /*&& !FacetContentType.OpportunityFacetFilter.getContentCode()[0].equalsIgnoreCase(entry.getKey())*/
                        /* && !FacetContentType.LeadFacetFilter.getContentCode()[5].equalsIgnoreCase(entry.getKey())*/) {
                        FacetContentRpc existingVals = mainMergedFilter.getFacetContentMap().get(entry.getKey());
                        if (existingVals != null) {
                            ArrayList<SelectItem> existingItems = new ArrayList<SelectItem>(Arrays.asList(existingVals.getFacetItems()));
                            existingItems.addAll(Arrays.asList(entry.getValue().getFacetItems()));

                            ArrayList<SelectItem> removedDuplicates = new ArrayList<>(new HashSet<>(existingItems));

                            existingVals.setFacetItems(removedDuplicates.toArray(new SelectItem[0]));
                            mainMergedFilter.getFacetContentMap().put(entry.getKey(), existingVals);
                        } else {
                            mainMergedFilter.getFacetContentMap().put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }
            //Set Periods
            if (oneTimeFilter.getStartDate() != null && oneTimeFilter.getEndDate() != null) {
                mainMergedFilter.setStartDate(oneTimeFilter.getStartDate());
                mainMergedFilter.setEndDate(oneTimeFilter.getEndDate());
                mainMergedFilter.setSelectedDateSolrCodeName(oneTimeFilter.getSelectedDateSolrCodeName());
            }
            //End Of Set Periods
        }
        //End of If OneTimeFilter is active

        ListingFilterParameter filterParameter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.CrmAccountListPanel);
        ArrayList<String> columnCodeNames = CrmAccountItem.defaultColumnNames;
        ListPanelToolRpc panelTools = new ListPanelToolRpc();
        panelTools.setColumnCodeName(columnCodeNames);
        panelTools.setShowPopup(true);
        filterParameter.setListPanelTool(panelTools);
        filterParameter.setColumnsOfListing(columnCodeNames);
        filterParameter.setFacetFilter(mainMergedFilter);

        filterParameter.setStart(requestTO.getOffset());
        filterParameter.setLimit(requestTO.getCount());
        filterParameter.setSearchButton(false);
        filterParameter.setDetectDuplicates(false);
        filterParameter.setWithImage(true);
        if (orderFieldEnum != null) {
            filterParameter.setSortField(getSortField(orderFieldEnum, ListPanelType.CrmAccountListPanel));
        }
        filterParameter.setAscending(orderByEnum == null || OrderByEnum.ASC.getDirection().equals(orderByEnum.getDirection()));
        filterParameter.setSortDir(orderByEnum != null ? orderByEnum.getId() : OrderByEnum.ASC.getId());

        ArrayList<CompanyCustomFieldItem> customFieldItems = commonServiceLocal.getCompanyCustomFieldsForListView(ViewName.CrmAccount);
        if (customFieldItems != null && customFieldItems.size() > 0) {
            customFieldItems.forEach(companyCustomFieldItem -> {
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

        CrmAccountList crmAccountList;
        try {
            crmAccountList = crmServiceLocal.getCrmAccounts(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            if (((UndeclaredThrowableException) e).getUndeclaredThrowable() instanceof CheckPermissionException) {
                throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        PagingListResultTO<ContactTO> crmAccountResult = new PagingListResultTO<>();
        if (crmAccountList != null) {
            crmAccountResult.setTotal_count(crmAccountList.getTotal());
            crmAccountResult.setOffset(requestTO.getOffset());
            crmAccountResult.setCount(crmAccountList.getList() != null ? crmAccountList.getList().size() : 0);
            if (crmAccountList.getTotal() < (requestTO.getCount() + requestTO.getOffset())) {
                crmAccountResult.setLeft(0);
            } else {
                crmAccountResult.setLeft(crmAccountList.getTotal() - (requestTO.getOffset() + requestTO.getCount()));
            }

            ArrayList<ContactTO> items = new ArrayList<>();
            if (crmAccountList.getList() != null) {
                crmAccountList.getList().forEach(crmAccountItem -> {
                    ContactTO crmItem = new ContactTO();
                    crmItem.setName(crmAccountItem.getName());
                    crmItem.setItem_id(crmAccountItem.getObjectId());
                    crmItem.setAvatar_image(crmAccountItem.getLogoUrl());
                    items.add(crmItem);
                });
                crmAccountResult.setList(items);
            }
        }
        return successResponse(crmAccountResult);
    }

    private FacetFilterRpc getOneTimeFilter(FacetFilterRpc defaultFilter) {

        defaultFilter.setType(ListPanelType.CrmAccountListPanelOTF);
        defaultFilter.setUserID(userManager.getUser().getObjectID());

        FacetFilterRpc otf = commonServiceLocal.getUserFacetFilter(defaultFilter);
        otf.setName("OTF");
        otf.setDefaultFilter(true);
        otf.setType(ListPanelType.CrmAccountListPanelOTF);

        if (otf.getObjectID() != null) {
            EdsUserFilter edsUserFilter = userFilterManager.getByFacetFilterId(otf.getObjectID());
            if (edsUserFilter != null) {
                otf.setFavourFilter(Boolean.TRUE.equals(edsUserFilter.getFavour()));
            }
        }
        return otf;
    }

    private FacetFilterRpc initializeDefaultCrmAccountFacetFilter() {
        FacetFilterRpc mainMergedFilter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.CrmAccountListPanel).getFacetFilter();

        //Custom fields which are facetable
        ArrayList<CompanyCustomFieldItem> crmAccountCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.CrmAccount);

        if (crmAccountCustomFields != null && crmAccountCustomFields.size() > 0) {
            crmAccountCustomFields.forEach(companyCustomFieldItem -> {
                if (companyCustomFieldItem.isFacetable() && StringUtils.isNotBlank(companyCustomFieldItem.getColumnCode())) {
                    //we must add this condition otherwise it will add again and again into map (static block inside other class)
                    if (!mainMergedFilter.getShowFacetCodeName().contains(companyCustomFieldItem.getColumnCode())) {
                        mainMergedFilter.getShowFacetCodeName().add(companyCustomFieldItem.getColumnCode());
                    }
                    FacetSolrField solrField = new FacetSolrField(companyCustomFieldItem.getColumnCode().toUpperCase(), companyCustomFieldItem.getColumnCode().toUpperCase());
                    solrField.setConditionItemId(true);
                    mainMergedFilter.getShowSolrFieldMap().put(companyCustomFieldItem.getColumnCode(), solrField);
                } else {
                    mainMergedFilter.getShowFacetCodeName().remove(companyCustomFieldItem.getColumnCode());
                    if (mainMergedFilter.getShowSolrFieldMap().get(companyCustomFieldItem.getColumnCode()) != null) {
                        mainMergedFilter.getShowSolrFieldMap().remove(companyCustomFieldItem.getColumnCode());
                    }
                }

            });
        }
        //End Of main facet filter initialization
        return mainMergedFilter;
    }

    private Integer saveQuickFilter(ArrayList<SelectItem> people, ArrayList<SelectItem> categories) {
        FacetFilterRpc facetFilterToSave = new FacetFilterRpc();
        facetFilterToSave.setType(ListPanelType.CrmAccountQuickFilterForMobile);

//            EdsFacetFilter edsFacetFilter = facetFilterManager.getDefaultUserFacetFilter(ListPanelType.FilteredStatusesForMobile, userManager.getUser());
        facetFilterToSave = commonServiceLocal.getUserFacetFilter(facetFilterToSave);
        facetFilterToSave.setDefaultFilter(true);

        FacetContentRpc peopleContentRpc = new FacetContentRpc();
        peopleContentRpc.setFacetItems(people.toArray(new SelectItem[0]));
        facetFilterToSave.getFacetContentMap().put("people", peopleContentRpc);

        FacetContentRpc categoriesContentRpc = new FacetContentRpc();
        categoriesContentRpc.setFacetItems(categories.toArray(new SelectItem[0]));
        facetFilterToSave.getFacetContentMap().put("categories", categoriesContentRpc);

        facetFilterToSave.setUserID(userManager.getUser().getObjectID());
        facetFilterToSave.setDefaultFilter(true);
        return commonServiceLocal.saveFacetFilter(facetFilterToSave, ListPanelType.CrmAccountQuickFilterForMobile);
    }

    @Operation(summary = "Search Companies", description = "Getting a list of companies (items) on the entered characters.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have companies based on search query"),
            @ApiResponse(responseCode = "400", description = "query is required")})
    @RequestMapping(value = "/companies/search", method = RequestMethod.GET)
    //@CheckPermission(permissions = {PermissionConstants.CRM_ACCOUNTS_LIST})
    public Object searchCompanies(@RequestParam(value = "query") String query,
                                  @RequestParam(value = "limit", required = false) Integer limit,
                                  @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        PagingListResultTO<CrmAccountTO> crmAccountListResult = new PagingListResultTO<>();

        if (StringUtils.isBlank(query)) {
            return successResponse(crmAccountListResult);
        }
        query = query.replace("%20", " ").trim();

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
        filterParameter.setCRM(true);

        CrmAccountList result;
        try {
            result = crmServiceLocal.getCrmAccounts(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        crmAccountListResult.setTotal_count(result.getTotal());
        if (result.getTotal() < (maxLimit + start)) {
            crmAccountListResult.setLeft(0);
        } else {
            crmAccountListResult.setLeft(result.getTotal() - (start + maxLimit));
        }
        crmAccountListResult.setCount(result.getList() != null ? result.getList().size() : 0);
        crmAccountListResult.setOffset(start);

        ArrayList<CrmAccountTO> accountList = new ArrayList<>();
        for (CrmAccountItem accountListItem : result.getList()) {
            CrmAccountTO accountItem = new CrmAccountTO();
            if (StringUtils.isNotBlank(accountListItem.getName())) {
                accountItem.setName(accountListItem.getName().trim());
            }
            accountItem.setItem_id(accountListItem.getObjectId());
            accountItem.setAvatar_image(accountListItem.getLogoUrl());

            accountList.add(accountItem);
        }

        crmAccountListResult.setList(accountList);

        return successResponse(crmAccountListResult);
    }

    @Operation(summary = "Delete Company", description = "Delete particular entity like Lead, Opportunity, Company, Contact etc. Particular entity is described in path, like other requests. Server should check if current user has permissions to delete this particular item, and if no give user message: You don't have permissions to delete this entry. Please contact your administrator")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/companies/{item_id}/delete", method = RequestMethod.DELETE)
    @CheckPermission(permissions = {PermissionConstants.CRM_ACCOUNTS_LIST, PermissionConstants.CRM_ACCOUNTS_DELETE})
    public Object deleteCompany(@PathVariable(value = "item_id") Integer item_id) throws RestException {

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsCrmAccount edsCrmAccount = crmAccountManager.get(item_id);
        if (edsCrmAccount == null || edsCrmAccount.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Company with id " + item_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (ServerUtils.hasPermission(PermissionConstants.CRM_ACCOUNTS_DELETE)) {
            try {
                ArrayList<Integer> objectIDs = new ArrayList<>();
                objectIDs.add(item_id);
                crmServiceLocal.deleteCrmAccount(objectIDs, false);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
        }

        return successResponse(new ResponseData());
    }

    @Operation(summary = "Get Company Addresses")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Company addresses"),
            @ApiResponse(responseCode = "400", description = "item_id is required")})
    @RequestMapping(value = "/companies/{item_id}/addresses", method = RequestMethod.GET)
    public Object getCompanyAddresses(@PathVariable(value = "item_id") Integer item_id) throws RestException {
        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsCrmAccount edsCrmAccount;
        try {
            edsCrmAccount = crmAccountManager.get(item_id);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ArrayList<EntityContactAddressTO> entityAddresses = new ArrayList<>();
        if (edsCrmAccount != null) {
            List<EdsAddress> addresses = edsCrmAccount.getAddresses();
            if (addresses != null) {
                addresses.forEach(address -> {
                    if (EdsAddress.BILLING_ADDRESS.equals(address.getRelationType())) {
                        EntityContactAddressTO entityAddress = new EntityContactAddressTO();
                        entityAddress.setItem_id(address.getObjectID());
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
                    }
                });
            }
        }
        return successResponse(new ResponseListData<>(entityAddresses));
    }

    @Operation(summary = "Update Company Addresses")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/companies/{item_id}/addresses", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_ACCOUNTS_LIST, PermissionConstants.CRM_ACCOUNTS_EDIT})
    public Object updateCompanyAddresses(@PathVariable(value = "item_id") Integer item_id,
                                         @RequestBody EntityContactAddressListTO updateEntityContactAddressTO) throws RestException {

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        CrmAccountItem crmAccountItem;
        try {
            crmAccountItem = clientServiceLocal.editAccount(item_id, null);
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

        crmAccountItem.setBillAddresses(addressList.toArray(new Address[]{}));
        try {
            crmServiceLocal.saveAccount(crmAccountItem, null, null, false, false, false, true);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return successResponse(new ResponseData());
    }

    @Operation(summary = "Update company information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/companies/{item_id}/information", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_ACCOUNTS_LIST, PermissionConstants.CRM_ACCOUNTS_EDIT})
    public Object updateCompanyInformation(@PathVariable(value = "item_id") Integer item_id,
                                           @RequestBody CompanyInformationUpdateTO companyInformationUpdateTO) throws RestException {

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (companyInformationUpdateTO == null || StringUtils.isBlank(companyInformationUpdateTO.getAccount_name())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "account_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        CrmAccountItem crmAccountItem;
        try {
            crmAccountItem = clientServiceLocal.editAccount(item_id, null);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        crmAccountItem.setObjectId(item_id);
        crmAccountItem.setName(companyInformationUpdateTO.getAccount_name());
        //crmAccountItem.setOwnerID(companyInformationUpdateTO.getAccount_owner());
        crmAccountItem.setSelectedOwners(Lists.newArrayList(new SelectItem(companyInformationUpdateTO.getAccount_owner())));

        CrmAccountItem parent = new CrmAccountItem();
        parent.setObjectId(companyInformationUpdateTO.getParent_account());
        crmAccountItem.setParent(parent);

        if (companyInformationUpdateTO.getPrimary_contact() != null) {
            EdsCrmContact edsCrmContact = (EdsCrmContact) crmContactManager.get(EdsCrmContact.class, companyInformationUpdateTO.getPrimary_contact());
            if (edsCrmContact != null) {
                crmAccountItem.setPrimaryContact(crmServiceLocal.makePrimaryContact(item_id, edsCrmContact.getObjectID()));
            }
        }

        if (companyInformationUpdateTO.getAccount_types() != null && companyInformationUpdateTO.getAccount_types().size() > 0) {
            List<EdsReference> referenceList = referenceManager.listReferences(EdsCrmAccount._CRM_ACCOUNT_TYPE);
            LinkedHashMap<String, EdsReference> accountTypesMap = new LinkedHashMap<>();
            for (EdsReference reference : referenceList) {
                accountTypesMap.put(reference.getCode(), reference);
            }
            ArrayList<SelectItem> accountTypes = new ArrayList<>();
            for (String accountType : companyInformationUpdateTO.getAccount_types()) {
                if (accountTypesMap.get(accountType) != null) {
                    SelectItem selectItem = accountTypesMap.get(accountType).getAsSelectItem();
                    selectItem.setSelected(true);
                    accountTypes.add(selectItem);
                }
            }
            crmAccountItem.setAccountTypes(accountTypes.toArray(new SelectItem[0]));
        } else {
            crmAccountItem.setAccountTypes(null);
        }

        try {
            crmServiceLocal.saveAccount(crmAccountItem, null, null, false, false, false, true);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return successResponse(new ResponseData());
    }

    @Operation(summary = "Update Crm Account Additional Information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/companies/{item_id}/additional_information",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_ACCOUNTS_LIST, PermissionConstants.CRM_ACCOUNTS_EDIT})
    public Object updateCompanyAdditionalInformation(
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
            modelFields = modelFieldManager.getFields(LayoutRPC.ACCOUNT_FORM);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //Map key is model field id and man value is model field field_id. e.g 1,CRM_OPPORTUNITY_STAGE
        LinkedHashMap<Integer, String> modelFieldsMap = new LinkedHashMap<>();
        if (modelFields != null && modelFields.size() > 0) {
            modelFields.forEach(modelField -> modelFieldsMap.put(modelField.getObjectID(), modelField.getField_ID()));
        }

        CrmAccountItem crmAccountItem = crmServiceLocal.editAccount(item_id, null);

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

        ArrayList<CompanyCustomFieldItem> customFieldItems = null;
        LinkedHashMap<Integer, ArrayList<AttachmentTO>> customFieldDraftAttachmentMap = new LinkedHashMap<>();
        if (customFieldObjects.size() > 0) {
            customFieldItems = convertCustomFields(customFieldObjects, customFieldDraftAttachmentMap);
        }

        //Create Map of MultipartFiles with custom field Id as key
        TreeMap<Integer, ArrayList<MultipartFile>> customFieldAttachmentsMap = getCustomFieldAttachmentsMap(multipartRequest, customFieldFileNameRegex);


        //Compare draft files to old files by unique keys: filename & file size. If there is a difference between them by name or size, delete the differ old files
        // but keep other non changed files
        ArrayList<FileResource> oldAttachments = new ArrayList<>();
        HashSet<Integer> deleteIDs = new HashSet<>();

        if (crmAccountItem.getCustomFields() != null && crmAccountItem.getCustomFields().size() > 0) {
            for (CompanyCustomFieldItem companyCustomFieldItem : crmAccountItem.getCustomFields()) {
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
                if (crmAccountItem.getCustomFields() != null && crmAccountItem.getCustomFields().size() > 0) {
                    for (CompanyCustomFieldItem companyCustomFieldItem : crmAccountItem.getCustomFields()) {
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
            crmAccountItem.setCustomFields(customFieldItems);
        }
        HistoryListItem note = null;

        for (String fieldID : modelFieldValueMap.keySet()) {
            if (CustomFormConstants.CRM_ACCOUNT_OWNER.equals(fieldID)) {
                //crmAccountItem.setOwnerID((Integer) modelFieldValueMap.get(fieldID));
                crmAccountItem.setSelectedOwners(Lists.newArrayList(new SelectItem((Integer) modelFieldValueMap.get(fieldID))));
            } else if (CustomFormConstants.CRM_ACCOUNT_NUMBER.equals(fieldID)) {
                crmAccountItem.setNumber((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_PARENT.equals(fieldID)) {
                crmAccountItem.setParentID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.PRIMARY_CONTACT.equals(fieldID)) {
                ContactListItem primaryContact = new ContactListItem();
                primaryContact.setObjectId((Integer) modelFieldValueMap.get(fieldID));
                crmAccountItem.setPrimaryContact(primaryContact);
            } else if (CustomFormConstants.CRM_ACCOUNT_OWNERSHIP.equals(fieldID)) {
                crmAccountItem.setOwnershipId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_INDUSTRY.equals(fieldID)) {
                crmAccountItem.setIndustryID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_EMAIL.equals(fieldID)) {
                crmAccountItem.setEmail((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_PHONE.equals(fieldID)) {
                crmAccountItem.setPhone((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_FAX.equals(fieldID)) {
                crmAccountItem.setFax((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_WEBSITE.equals(fieldID)) {
                crmAccountItem.setWebsite((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_ORGANIZATION_TYPE.equals(fieldID)) {
                crmAccountItem.setOrganizationTypeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_NUMBER_OF_EMPLOYEE.equals(fieldID)) {
                crmAccountItem.setNumberOfEmployeeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_ANNUAL_REVENUE.equals(fieldID)) {
                crmAccountItem.setAnnualRevenueID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_RATING.equals(fieldID)) {
                crmAccountItem.setRatingId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CURRENCY.equals(fieldID)) {
                crmAccountItem.setCurrencyId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.VAT_NUMBER.equals(fieldID)) {
                crmAccountItem.setVatNumber((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CLIENT_VAT.equals(fieldID)) {
                crmAccountItem.setVat(new TaxItem((Integer) modelFieldValueMap.get(fieldID), ""));
            } else if (CustomFormConstants.PAYMENT_METHOD.equals(fieldID)) {
                crmAccountItem.setPaymentMethodId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.REGISTRATION_NUMBER.equals(fieldID)) {
                crmAccountItem.setRegistrationNumber((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CLIENT_INVOICE_TERM.equals(fieldID)) {
                crmAccountItem.setTermsItem(new SelectItem((Integer) modelFieldValueMap.get(fieldID)));
            } else if (CustomFormConstants.SUPPLIER_BANK_NAME.equals(fieldID)) {
                crmAccountItem.setBankName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.SUPPLIER_ACCOUNT_NAME.equals(fieldID)) {
                crmAccountItem.setAccountName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER.equals(fieldID)) {
                crmAccountItem.setAccountNo((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.SUPPLIER_SWIFT_CODE.equals(fieldID)) {
                crmAccountItem.setSwiftCode((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.SUPPLIER_IBAN_CODE.equals(fieldID)) {
                crmAccountItem.setIbanCode((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.SUPPLIER_BRANCH.equals(fieldID)) {
                crmAccountItem.setBranch((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.SUPPLIER_BANK_ADDRESS.equals(fieldID)) {
                crmAccountItem.setBankAddress((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.SUPPLIER_VAT.equals(fieldID)) {
                crmAccountItem.setVat(new TaxItem((Integer) modelFieldValueMap.get(fieldID), ""));
            } else if (CustomFormConstants.NOTES.equals(fieldID) || CustomFormConstants.CRM_NOTE.equals(fieldID)) {
                if (StringUtils.isNotBlank((String) modelFieldValueMap.get(fieldID))) {
                    note = new HistoryListItem((String) modelFieldValueMap.get(fieldID));
                }

            }
        }

        Integer companyId;
        try {
            companyId = crmServiceLocal.saveAccount(crmAccountItem, null, null, false, false, false, true);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (companyId < 0) {
            if (companyId == -1) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Company name ".concat(crmAccountItem.getName()).concat(" is already exist"), CONFLICT, HttpStatus.CONFLICT);
            }
            if (companyId == -2) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Company number ".concat(crmAccountItem.getNumber()).concat(" is already exist"), CONFLICT, HttpStatus.CONFLICT);
            }
        }

        if (crmAccountItem.getPrimaryContact() != null) {
            try {
                crmServiceLocal.makePrimaryContact(companyId, crmAccountItem.getPrimaryContact().getObjectId());
            } catch (Exception e) {
                log.error("Api error occurred while assigning primary contact to company", e);
            }
        }

        if (note != null) {
            try {
                note.setRelatedToId(EdsNoteHistory.CRM_ACCOUNT);
                note.setRelatedId(companyId);
                noteServiceLocal.saveNote(note);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return successResponse(new ResponseData());
    }

    @Operation(summary = "Update Company Financial Information", description = "It's multipart request")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/companies/{item_id}/financial_information",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_ACCOUNTS_LIST, PermissionConstants.CRM_ACCOUNTS_EDIT})
    public Object updateCompanyFinancialInformation(
            @PathVariable(value = "item_id") Integer item_id,
            MultipartRequest multipartRequest,
            @Parameter(name = "body", description = """
                    <pre>{
                      "list": [
                        {
                          "id": 1,
                          "value": 2
                        },
                        {
                          "id": 2,
                          "text": "somevalue if its text input type"
                        }
                      ]
                    }
                    </pre>""", schema = @Schema(type = "string")) @RequestParam(name = "body") String jsonString) throws RestException {

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        LinkedHashMap<Object, ArrayList<Object>> customFieldsObjectMap;
        ObjectMapper mapper = new ObjectMapper();
        try {
            customFieldsObjectMap = (LinkedHashMap<Object, ArrayList<Object>>) mapper.readValue(jsonString, Object.class);
        } catch (Exception e) {
            log.error("Error: ", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong.", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        //Get model fields by entity type
        List<ModelField> modelFields;
        try {
            modelFields = modelFieldManager.getFields(FORM_TYPES.get(EntityTypeEnum.ACCOUNTS.name().toLowerCase()));
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
        if (customFieldsObjectMap != null && !customFieldsObjectMap.isEmpty()) {
            for (Object customFieldObject : customFieldsObjectMap.get("list")) {
                LinkedHashMap<Object, Object> customFieldsMap = (LinkedHashMap<Object, Object>) customFieldObject;
                if (customFieldsMap.get("id") != null) {
                    String fieldID = modelFieldsMap.get(((Integer) customFieldsMap.get("id")) - GAP_BTW_STATIC_AND_CUSTOM_FIELDS);//it means model field
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
                        }
                    }
                }
            }
        }

        CrmAccountItem account = clientServiceLocal.editAccount(item_id, null);

        for (String fieldID : modelFieldValueMap.keySet()) {
            if (CustomFormConstants.CURRENCY.equals(fieldID)) {
                account.setCurrencyId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.VAT_NUMBER.equals(fieldID)) {
                account.setVatNumber((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CLIENT_VAT.equals(fieldID)) {
                account.setVat(new TaxItem((Integer) modelFieldValueMap.get(fieldID), ""));
            } else if (CustomFormConstants.PAYMENT_METHOD.equals(fieldID)) {
                account.setPaymentMethodId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.REGISTRATION_NUMBER.equals(fieldID)) {
                account.setRegistrationNumber((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CLIENT_INVOICE_TERM.equals(fieldID)) {
                account.setTermsItem(new SelectItem((Integer) modelFieldValueMap.get(fieldID)));
            }
        }

        try {
            crmServiceLocal.saveAccount(account, null, null, false, false, false, true);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return successResponse(new ResponseData());


    }

    @Operation(summary = "Create Company")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Request to create new Company. It's multipart request.")})
    @RequestMapping(value = "/{company_type}/create", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_ACCOUNT_ADD})
    public Object createCompany(MultipartRequest multipartRequest,
                                @PathVariable("company_type") String company_type,
                                @Parameter(name = "body", description = """
                                        <pre>{
                                          "account_owner": 1,
                                          "account_name": "companyname",
                                          "parent_account": 1,
                                          "account_types": [
                                            "CUSTOMER"
                                          ],
                                          "address_information": [
                                            {
                                              "line_1": "Address Line 1",
                                              "line_2": "Address Line 2",
                                              "city": "City",
                                              "post_code": "postcode",
                                              "is_primary": true,
                                              "state_code": "AL",
                                              "country_code": "US"
                                            }
                                          ],
                                          "custom_fields": [
                                            {
                                              "id": 1,
                                              "text": "For text fields",
                                              "value": 1,
                                              "category_id": 1,
                                              "choosed_ids": "1,2,3",
                                              "date": "dd-MM-yyyy'T'HH:mm:ssZ"
                                            }
                                          ]
                                        }
                                        </pre>""", schema = @Schema(type = "string")) @RequestParam(name = "body") String jsonString) throws RestException {

        HashSet<String> accountTypes = new HashSet<>();

        if (EntityTypeEnum.SUPPLIERS.name().equalsIgnoreCase(company_type)) {
            accountTypes.add(EdsCrmAccount.SUPPLIER);
        } else if (EntityTypeEnum.CUSTOMERS.name().equalsIgnoreCase(company_type)) {
            accountTypes.add(EdsCrmAccount.CUSTOMER);
        } else {
            company_type = EntityTypeEnum.ACCOUNTS.name();
        }

        CompanyAddTO companyAddTO;
        ObjectMapper mapper = new ObjectMapper();

        try {
            companyAddTO = mapper.readValue(jsonString, CompanyAddTO.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong.".concat(e.getMessage()), REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(companyAddTO.getAccount_name())) {
            throw new RestException("Account Name is required", "Account Name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        } else {
            Integer result = crmAccountManager.isAccountNameOrNumberAlreadyExists(companyAddTO.getAccount_name(), null, 0);
            if (result != 0) {
                throw new RestException("Company with this name already exist", "Company with this name already exist", CONFLICT, HttpStatus.BAD_REQUEST);
            }
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
        if (companyAddTO.getCustom_fields() != null) {
            Set<Integer> customFieldIdSet = new HashSet<>();
            for (Object customFieldObject : companyAddTO.getCustom_fields()) {
                LinkedHashMap<Object, Object> customFieldsMap = (LinkedHashMap<Object, Object>) customFieldObject;
                Integer customFieldId = (Integer) customFieldsMap.get("id");
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
                    companyAddTO.getCustom_fields().add(customFieldsMap);
                }
            }
        }


        CrmAccountItem crmAccountItem = new CrmAccountItem();
        crmAccountItem.setName(companyAddTO.getAccount_name());
        crmAccountItem.setEmail(companyAddTO.getEmail());
        crmAccountItem.setPhone(companyAddTO.getPhone());

        if (companyAddTO.getAccount_owner() != null && companyAddTO.getAccount_owner() > 0) {
            //crmAccountItem.setOwnerID(companyAddTO.getAccount_owner());
            crmAccountItem.setSelectedOwners(Lists.newArrayList(new SelectItem(companyAddTO.getAccount_owner())));
        } else {
            crmAccountItem.setSelectedOwners(Lists.newArrayList(new SelectItem(userManager.getUser().getObjectID(), userManager.getUser().getFirstName())));
            //crmAccountItem.setOwnerID(userManager.getUser().getObjectID());
            //crmAccountItem.setOwnerName(userManager.getUser().getFirstName());
        }
        if (companyAddTO.getParent_account() != null && companyAddTO.getParent_account() > 0) {
            CrmAccountItem parent = new CrmAccountItem();
            parent.setObjectId(companyAddTO.getParent_account());
            crmAccountItem.setParent(parent);
        }
        //Account Types
        if (companyAddTO.getAccount_types() != null) {
            accountTypes.addAll(companyAddTO.getAccount_types());
        }
        ArrayList<SelectItem> types = new ArrayList<>();
        accountTypes.forEach(accountTypeCode -> {
                    EdsReference type = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, accountTypeCode);
                    if (type != null) {
                        SelectItem selectItem = type.getAsSelectItem();
                        selectItem.setSelected(true);
                        types.add(selectItem);
                    }
                }
        );
        crmAccountItem.setAccountTypes(types.toArray(new SelectItem[0]));
        //End Of Account Types
        //Address
        if (companyAddTO.getAddress_information() != null && !companyAddTO.getAddress_information().isEmpty()) {

            ArrayList<Address> addresses = new ArrayList<>();

            companyAddTO.getAddress_information().forEach(addressItem -> {
                Address address = new Address();
                address.setAddress(addressItem.getLine_1());
                address.setAddressb(addressItem.getLine_2());
                address.setCity(addressItem.getCity());
                address.setName(addressItem.getName());
                address.setZipCode(addressItem.getPost_code());
                address.setPrimary(Boolean.TRUE.equals(addressItem.getIs_primary()));

                if (addressItem.getCountry() != null) {
                    if (addressItem.getCountry().getId() != null) {
                        address.setCountryId(addressItem.getCountry().getId());
                    }
                    address.setCountry(addressItem.getCountry().getTitle());
                }
                if (addressItem.getState() != null) {
                    if (addressItem.getState().getId() != null) {
                        address.setStateId(addressItem.getState().getId());
                    }
                    address.setState(addressItem.getState().getTitle());
                }
                address.setEntityType(EdsAddress.ENTITY_TYPE_COMPANY);
                address.setRelationType(EdsAddress.HOME);

                addresses.add(address);
            });
            crmAccountItem.setBillAddresses(addresses.toArray(new Address[0]));
        }
        //End Of Address

        Integer attachmentModelFieldId = null;
        HistoryListItem note = null;
        //Map key is model field id that related to attachment, and value is attachment
        LinkedHashMap<Integer, ArrayList<MultipartFile>> attachmentsMap = new LinkedHashMap<>();
        ArrayList<MultipartFile> companyAttachments = new ArrayList<>();
        if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
            for (MultipartFile file : multipartRequest.getFileMap().values()) {
                if (file.getName().matches(customFieldFileNameRegex)) {
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
            modelFields = modelFieldManager.getFields(FORM_TYPES.get(company_type.toLowerCase()));
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
        if (companyAddTO.getCustom_fields() != null && !companyAddTO.getCustom_fields().isEmpty()) {
            for (Object customFieldObject : companyAddTO.getCustom_fields()) {
                LinkedHashMap<Object, Object> customFieldsMap = (LinkedHashMap<Object, Object>) customFieldObject;
                if (customFieldsMap.get("id") != null) {
                    if (((Integer) customFieldsMap.get("id")) < GAP_BTW_STATIC_AND_CUSTOM_FIELDS) {//it means real custom field
                        customFieldObjects.add(customFieldsMap);
                    } else {
                        String fieldID = modelFieldsMap.get(((Integer) customFieldsMap.get("id")) - GAP_BTW_STATIC_AND_CUSTOM_FIELDS);//it means model field
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
                                attachmentModelFieldId = ((Integer) customFieldsMap.get("id")) - GAP_BTW_STATIC_AND_CUSTOM_FIELDS;
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
                crmAccountItem.setCustomFields(customFieldItems);
            }
        }

        for (String fieldID : modelFieldValueMap.keySet()) {
            if (CustomFormConstants.CRM_ACCOUNT_OWNER.equals(fieldID)) {
                //crmAccountItem.setOwnerID((Integer) modelFieldValueMap.get(fieldID));
                crmAccountItem.setSelectedOwners(Lists.newArrayList(new SelectItem((Integer) modelFieldValueMap.get(fieldID))));
            } else if (CustomFormConstants.CRM_ACCOUNT_NUMBER.equals(fieldID)) {
                crmAccountItem.setNumber((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_PARENT.equals(fieldID)) {
                crmAccountItem.setParentID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.PRIMARY_CONTACT.equals(fieldID)) {
                ContactListItem primaryContact = new ContactListItem();
                primaryContact.setObjectId((Integer) modelFieldValueMap.get(fieldID));
                crmAccountItem.setPrimaryContact(primaryContact);
            } else if (CustomFormConstants.CRM_ACCOUNT_OWNERSHIP.equals(fieldID)) {
                crmAccountItem.setOwnershipId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_INDUSTRY.equals(fieldID)) {
                crmAccountItem.setIndustryID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_EMAIL.equals(fieldID)) {
                crmAccountItem.setEmail((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_PHONE.equals(fieldID)) {
                crmAccountItem.setPhone((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_FAX.equals(fieldID)) {
                crmAccountItem.setFax((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_WEBSITE.equals(fieldID)) {
                crmAccountItem.setWebsite((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_ORGANIZATION_TYPE.equals(fieldID)) {
                crmAccountItem.setOrganizationTypeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_NUMBER_OF_EMPLOYEE.equals(fieldID)) {
                crmAccountItem.setNumberOfEmployeeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_ANNUAL_REVENUE.equals(fieldID)) {
                crmAccountItem.setAnnualRevenueID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_RATING.equals(fieldID)) {
                crmAccountItem.setRatingId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CURRENCY.equals(fieldID)) {
                crmAccountItem.setCurrencyId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.VAT_NUMBER.equals(fieldID)) {
                crmAccountItem.setVatNumber((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CLIENT_VAT.equals(fieldID)) {
                crmAccountItem.setVat(new TaxItem((Integer) modelFieldValueMap.get(fieldID), ""));
            } else if (CustomFormConstants.PAYMENT_METHOD.equals(fieldID)) {
                crmAccountItem.setPaymentMethodId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.REGISTRATION_NUMBER.equals(fieldID)) {
                crmAccountItem.setRegistrationNumber((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CLIENT_INVOICE_TERM.equals(fieldID)) {
                crmAccountItem.setTermsItem(new SelectItem((Integer) modelFieldValueMap.get(fieldID)));
            } else if (CustomFormConstants.SUPPLIER_BANK_NAME.equals(fieldID)) {
                crmAccountItem.setBankName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.SUPPLIER_ACCOUNT_NAME.equals(fieldID)) {
                crmAccountItem.setAccountName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER.equals(fieldID)) {
                crmAccountItem.setAccountNo((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.SUPPLIER_SWIFT_CODE.equals(fieldID)) {
                crmAccountItem.setSwiftCode((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.SUPPLIER_IBAN_CODE.equals(fieldID)) {
                crmAccountItem.setIbanCode((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.SUPPLIER_BRANCH.equals(fieldID)) {
                crmAccountItem.setBranch((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.SUPPLIER_BANK_ADDRESS.equals(fieldID)) {
                crmAccountItem.setBankAddress((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.SUPPLIER_VAT.equals(fieldID)) {
                crmAccountItem.setVat(new TaxItem((Integer) modelFieldValueMap.get(fieldID), ""));
            } else if (CustomFormConstants.NOTES.equals(fieldID) || CustomFormConstants.CRM_NOTE.equals(fieldID)) {
                if (StringUtils.isNotBlank((String) modelFieldValueMap.get(fieldID))) {
                    note = new HistoryListItem((String) modelFieldValueMap.get(fieldID));
                }
            } else if (CustomFormConstants.ATTACHMENTS.equals(fieldID)) {
                if (attachmentModelFieldId != null && attachmentsMap.get(attachmentModelFieldId) != null) {
                    companyAttachments.addAll(attachmentsMap.get(attachmentModelFieldId));
                }
            }
        }

        Integer companyId;
        try {
            companyId = crmServiceLocal.saveAccount(crmAccountItem, null, null, false, false, false, true);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (companyId < 0) {
            if (companyId == -1) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Company name ".concat(crmAccountItem.getName()).concat(" is already exist"), CONFLICT, HttpStatus.CONFLICT);
            }
            if (companyId == -2) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Company number ".concat(crmAccountItem.getNumber()).concat(" is already exist"), CONFLICT, HttpStatus.CONFLICT);
            }
        }

        if (crmAccountItem.getPrimaryContact() != null) {
            try {
                crmServiceLocal.makePrimaryContact(companyId, crmAccountItem.getPrimaryContact().getObjectId());
            } catch (Exception e) {
                log.error("Api error occurred while assigning primary contact to company", e);
            }
        }

        if (note != null) {
            try {
                note.setRelatedToId(EdsNoteHistory.CRM_ACCOUNT);
                note.setRelatedId(companyId);
                noteServiceLocal.saveNote(note);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        if (companyAttachments.size() > 0) {
            FolderResource folderResource = documentsServiceLocal.getFolderResource(Constants.F_CRM_ACCOUNT, companyId);

            for (MultipartFile file : companyAttachments) {
                try {
                    documentsServiceLocal.saveDocumentFile(file, folderResource.getObjectId(), folderResource.getFileType(), companyId, null);
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }

        return successResponse(new IdNameTO(companyId, null));
    }

    @Operation(summary = "Get Entity Category List", description = "Get Categories for particular entities like leads, activities, opportunities and companies")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the list of particular entities like leads, activities, opportunities and companies")})
    @RequestMapping(value = "/{entity_path}/{field_type}/categories", method = RequestMethod.GET)
    public Object getEntityFieldCategories(
            @PathVariable(value = "entity_path") String entity_path,
            @PathVariable(value = "field_type") String field_type,
            @RequestParam(value = "custom_field_id", required = false) Integer custom_field_id,
            @RequestParam(value = "dependency_id", required = false) Integer dependency_id,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        if (StringUtils.isBlank(field_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "field_type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (!EntityFieldTypeEnum.ACCOUNT_OWNER.name().equals(field_type) && !EntityFieldTypeEnum.PARENT_ACCOUNT.name().equals(field_type) && !EntityFieldTypeEnum.CUSTOM.name().equals(field_type)
                && !EntityFieldTypeEnum.PRIMARY_CONTACT.name().equals(field_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "field_type should be one of PARENT_ACCOUNT | ACCOUNT_OWNER | CUSTOM | PRIMARY_CONTACT", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (StringUtils.isBlank(entity_path)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "entity should be one of accounts | suppliers | customers", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Integer start = (offset != null && offset > 0) ? offset : 0;
        Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;
        EdsUser user = userManager.getUser();
        EntityCategoryTO entityCategories = new EntityCategoryTO();
        ArrayList<CategoryTO> categories = new ArrayList<>();

        if (EntityFieldTypeEnum.PARENT_ACCOUNT.name().equals(field_type)) {
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
                if (((UndeclaredThrowableException) e).getUndeclaredThrowable() instanceof CheckPermissionException) {
                    throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
                } else {
                    throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
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
                crmAccountList.getList().forEach(crmAccountItem -> categories.add(new CategoryTO(crmAccountItem.getObjectId(), crmAccountItem.getName())));
                entityCategories.setList(categories);
            }
        } else if (EntityFieldTypeEnum.ACCOUNT_OWNER.name().equals(field_type)) {
            SelectItem[] salesPeople = crmServiceLocal.getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE);
            getCrmAccountSelectItems(query, start, maxLimit, entityCategories, categories, salesPeople);
        } else if (EntityFieldTypeEnum.PRIMARY_CONTACT.name().equals(field_type)) {
            if (dependency_id == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "dependency_id is required to get company contacts", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.isFiltirize();
            filterParameter.setAccountID(dependency_id);
            filterParameter.setStart(start);
            filterParameter.setLimit(maxLimit);
            filterParameter.setSearchKey(query);
            ListResult<ContactListItem> result;
            try {
                result = contactServiceLocal.getNewContactList(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                if (((UndeclaredThrowableException) e).getUndeclaredThrowable() instanceof CheckPermissionException) {
                    throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
                } else {
                    throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            if (result != null) {
                entityCategories.setTotal_count(result.getTotal());
                if (result.getTotal() < (maxLimit + start)) {
                    entityCategories.setLeft(0);
                } else {
                    entityCategories.setLeft(result.getTotal() - (start + maxLimit));
                }
                entityCategories.setCount(result.getList() != null ? result.getList().size() : 0);
                entityCategories.setOffset(start);
                if (result.getList() != null) {
                    result.getList().forEach(contactListItem -> {
                        CategoryTO category = new CategoryTO();
                        category.setId(contactListItem.getObjectId());
                        category.setTitle(contactListItem.getName());
                        categories.add(category);
                    });
                    entityCategories.setList(categories);
                }
            }
        } else if (EntityFieldTypeEnum.CUSTOM.name().equals(field_type)) {
            if (custom_field_id == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "custom_field_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (custom_field_id < GAP_BTW_STATIC_AND_CUSTOM_FIELDS) {
                List<String> predefinedValuesList = getCustomFieldValue(custom_field_id);
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
            } else {
                int real_model_field_id = custom_field_id - GAP_BTW_STATIC_AND_CUSTOM_FIELDS;
                EdsModelField modelField = modelFieldManager.get(real_model_field_id, true);
                if (modelField == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Custom field with id " + custom_field_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                }
                if (CustomFormConstants.CRM_ACCOUNT_INDUSTRY.equals(modelField.getField_ID())) {
                    ArrayList<EdsReference> industries;
                    try {
                        industries = (ArrayList) referenceManager.listReferences("_COMPANY_WORKAREA");
                    } catch (Exception e) {
                        log.error("", e);
                        throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    getReferenceItems(query, start, maxLimit, entityCategories, categories, industries);
                } else if (CustomFormConstants.CRM_ACCOUNT_OWNERSHIP.equals(modelField.getField_ID())) {
                    ArrayList<EdsReference> industries;
                    try {
                        industries = (ArrayList) referenceManager.listReferences("_OWNERSHIP");
                    } catch (Exception e) {
                        log.error("", e);
                        throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    getReferenceItems(query, start, maxLimit, entityCategories, categories, industries);
                } else if (CustomFormConstants.CRM_ACCOUNT_ORGANIZATION_TYPE.equals(modelField.getField_ID())) {
                    ArrayList<EdsReference> industries;
                    try {
                        industries = (ArrayList) referenceManager.listReferences("CONTACT_ORGANIZATION_TYPES");
                    } catch (Exception e) {
                        log.error("", e);
                        throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    getReferenceItems(query, start, maxLimit, entityCategories, categories, industries);
                } else if (CustomFormConstants.CRM_ACCOUNT_NUMBER_OF_EMPLOYEE.equals(modelField.getField_ID())) {
                    ArrayList<EdsReference> industries;
                    try {
                        industries = (ArrayList) referenceManager.listReferences("CONTACT_NUMBER_OF_EMPLOYEES");
                    } catch (Exception e) {
                        log.error("", e);
                        throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    getReferenceItems(query, start, maxLimit, entityCategories, categories, industries);
                } else if (CustomFormConstants.CLIENT_TYPE.equals(modelField.getField_ID())) {
                    ArrayList<EdsReference> industries;
                    try {
                        industries = (ArrayList) referenceManager.listReferences("CLIENT_TYPES");
                    } catch (Exception e) {
                        log.error("", e);
                        throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    getReferenceItems(query, start, maxLimit, entityCategories, categories, industries);
                } else if (CustomFormConstants.CRM_ACCOUNT_ANNUAL_REVENUE.equals(modelField.getField_ID())) {
                    ArrayList<EdsReference> industries;
                    try {
                        industries = (ArrayList) referenceManager.listReferences("CONTACT_ANNUAL_REVENUE");
                    } catch (Exception e) {
                        log.error("", e);
                        throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    getReferenceItems(query, start, maxLimit, entityCategories, categories, industries);
                } else if (CustomFormConstants.CRM_ACCOUNT_RATING.equals(modelField.getField_ID())) {
                    ArrayList<EdsReference> industries;
                    try {
                        industries = (ArrayList) referenceManager.listReferences(EdsCrmContact._LEAD_RATING);
                    } catch (Exception e) {
                        log.error("", e);
                        throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    getReferenceItems(query, start, maxLimit, entityCategories, categories, industries);
                } else if (CustomFormConstants.CLIENT_SUBSIDIARIES.equals(modelField.getField_ID())) {
                    SelectItem[] crmAccountSelectItems = clientServiceLocal.getSubsidiaries(new ListingFilterParameter());
                    getCrmAccountSelectItems(query, start, maxLimit, entityCategories, categories, crmAccountSelectItems);
                } else if (CustomFormConstants.PAYMENT_METHOD.equals(modelField.getField_ID())) {
                    SelectItem[] crmAccountSelectItems = allInOneServiceLocal.getPaymentMethodList();
                    getCrmAccountSelectItems(query, start, maxLimit, entityCategories, categories, crmAccountSelectItems);
                } else if (CustomFormConstants.CRM_ACCOUNT_OWNER.equals(modelField.getField_ID())) {
                    SelectItem[] crmAccountSelectItems = crmServiceLocal.getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE);
                    getCrmAccountSelectItems(query, start, maxLimit, entityCategories, categories, crmAccountSelectItems);
                } else if (CustomFormConstants.CLIENT_BANK_ACCOUNT.equals(modelField.getField_ID())) {
                    SelectItem[] crmAccountSelectItems = accountingServiceLocal.getBankAccountItems();
                    getCrmAccountSelectItems(query, start, maxLimit, entityCategories, categories, crmAccountSelectItems);
                } else if (CustomFormConstants.CURRENCY.equals(modelField.getField_ID())) {
                    SelectItem[] crmAccountSelectItems = crmServiceLocal.getCurrencies();
                    getCrmAccountSelectItems(query, start, maxLimit, entityCategories, categories, crmAccountSelectItems);
                } else if (CustomFormConstants.SUPPLIER_VAT.equals(modelField.getField_ID())) {
                    ListingFilterParameter filterParameter = new ListingFilterParameter();
                    filterParameter.setLookUp(true);
                    filterParameter.setInvoiceType(Constants.RECEIVABLE);
                    TaxItem[] taxItems = accountingServiceLocal.getCompanyTaxesWithFilter(filterParameter);
                    if (taxItems != null) {
                        List<TaxItem> crmAccountItemsList = Arrays.asList(taxItems);

                        if (StringUtils.isNotBlank(query)) {
                            crmAccountItemsList = crmAccountItemsList.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                        }
                        entityCategories.setTotal_count(crmAccountItemsList.size());
                        if (crmAccountItemsList.size() < (maxLimit + start)) {
                            entityCategories.setLeft(0);
                        } else {
                            entityCategories.setLeft(crmAccountItemsList.size() - (start + maxLimit));
                        }
                        ArrayList<TaxItem> stringArrayList = new ArrayList<>(crmAccountItemsList);
                        ArrayList<TaxItem> sublist = ListUtils.getSublistSmart(stringArrayList, start, maxLimit);
                        entityCategories.setCount(sublist.size());
                        entityCategories.setOffset(start);
                        for (TaxItem item : sublist) {
                            if (item != null) {
                                CategoryTO category = new CategoryTO();
                                category.setId(item.getId());
                                category.setTitle(item.getName());
                                categories.add(category);
                            }
                        }
                        entityCategories.setList(categories);
                    }
                } else if (CustomFormConstants.CLIENT_VAT.equals(modelField.getField_ID())) {
                    ListingFilterParameter filterParameter = new ListingFilterParameter();
                    filterParameter.setLookUp(true);
                    filterParameter.setInvoiceType(Constants.RECEIVABLE);
                    TaxItem[] taxItems = accountingServiceLocal.getCompanyTaxesWithFilter(filterParameter);
                    if (taxItems != null) {
                        List<TaxItem> crmAccountItemsList = Arrays.asList(taxItems);

                        if (StringUtils.isNotBlank(query)) {
                            crmAccountItemsList = crmAccountItemsList.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                        }
                        entityCategories.setTotal_count(crmAccountItemsList.size());
                        if (crmAccountItemsList.size() < (maxLimit + start)) {
                            entityCategories.setLeft(0);
                        } else {
                            entityCategories.setLeft(crmAccountItemsList.size() - (start + maxLimit));
                        }
                        ArrayList<TaxItem> stringArrayList = new ArrayList<>(crmAccountItemsList);
                        ArrayList<TaxItem> sublist = ListUtils.getSublistSmart(stringArrayList, start, maxLimit);
                        entityCategories.setCount(sublist.size());
                        entityCategories.setOffset(start);
                        for (TaxItem item : sublist) {
                            if (item != null) {
                                CategoryTO category = new CategoryTO();
                                category.setId(item.getId());
                                category.setTitle(item.getName());
                                categories.add(category);
                            }
                        }
                        entityCategories.setList(categories);
                    }
                } else if (CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE.equals(modelField.getField_ID())) {
                    isSupplier = true;
                    getAccountPayable(query, start, maxLimit, entityCategories, categories);
                } else if (CustomFormConstants.CLIENT_INVOICE_TERM.equals(modelField.getField_ID())) {
                    InvoiceTermsItem[] invoiceTermsItems = clientServiceLocal.getInvoiceTermsForLookUp(new ListingFilterParameter());
                    if (invoiceTermsItems != null) {
                        List<InvoiceTermsItem> invoiceTermsItemList = Arrays.asList(invoiceTermsItems);

                        if (StringUtils.isNotBlank(query)) {
                            invoiceTermsItemList = invoiceTermsItemList.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                        }
                        entityCategories.setTotal_count(invoiceTermsItemList.size());
                        if (invoiceTermsItemList.size() < (maxLimit + start)) {
                            entityCategories.setLeft(0);
                        } else {
                            entityCategories.setLeft(invoiceTermsItemList.size() - (start + maxLimit));
                        }
                        ArrayList<InvoiceTermsItem> stringArrayList = new ArrayList<>(invoiceTermsItemList);
                        ArrayList<InvoiceTermsItem> sublist = ListUtils.getSublistSmart(stringArrayList, start, maxLimit);
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
            }
        }
        return successResponse(entityCategories);
    }

    private void getReferenceItems(String query, Integer start, Integer maxLimit, EntityCategoryTO entityCategories, ArrayList<CategoryTO> categories, ArrayList<EdsReference> industries) {
        if (industries != null) {
            if (StringUtils.isNotBlank(query)) {
                industries = (ArrayList) industries.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
            }
            entityCategories.setTotal_count(industries.size());
            if (industries.size() < (maxLimit + start)) {
                entityCategories.setLeft(0);
            } else {
                entityCategories.setLeft(industries.size() - (start + maxLimit));
            }
            ArrayList<EdsReference> subList = ListUtils.getSublistSmart(industries, start, maxLimit);
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
    }

    @Operation(summary = "Get Company Information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have company information"),
            @ApiResponse(responseCode = "400", description = "item_id is required")})
    @RequestMapping(value = "/companies/{item_id}/information", method = RequestMethod.GET)
    public Object getCompanyInformation(@PathVariable(value = "item_id") Integer item_id) throws RestException {
        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(item_id);
        if (edsCrmAccount == null || edsCrmAccount.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Company with id ".concat(item_id.toString()).concat(" is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        CompanyInformationTO companyInformation = new CompanyInformationTO();
        /*if (edsCrmAccount.getOwner() != null) {
            companyInformation.setAccount_owner(new CategoryTO(edsCrmAccount.getOwner().getObjectID(), edsCrmAccount.getOwner().getName()));
        }*/
        if (edsCrmAccount.getOwners() != null && edsCrmAccount.getOwners().size() > 0) {
            companyInformation.setAccount_owner(new CategoryTO(edsCrmAccount.getOwners().get(0).getObjectID(), edsCrmAccount.getOwners().get(0).getName()));
        }
        companyInformation.setAccount_name(edsCrmAccount.getName());
        if (edsCrmAccount.getParent() != null) {
            companyInformation.setParent_account(new CategoryTO(edsCrmAccount.getParent().getObjectID(), edsCrmAccount.getParent().getName()));
        }
        if (edsCrmAccount.getPrimaryContact() != null) {
            companyInformation.setPrimary_contact(new CategoryTO(edsCrmAccount.getPrimaryContact().getObjectID(), edsCrmAccount.getPrimaryContact().getName()));
        }
        ArrayList<ItemTypeTO> accountTypes = new ArrayList<>();
        if (edsCrmAccount.getAccountTypes() != null) {
            edsCrmAccount.getAccountTypes().forEach(edsAccountType -> {
                ItemTypeTO accountType = new ItemTypeTO();
                accountType.setId(edsAccountType.getObjectID());
                accountType.setCode(edsAccountType.getCode());
                accountType.setTitle(edsAccountType.getName());
                accountTypes.add(accountType);
            });
        }
        companyInformation.setAccount_types(accountTypes);
        return successResponse(new EntityInformationResultTO(companyInformation));
    }

    @Operation(summary = "Get Financial Information Fields")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Financial Information Fields")})
    @RequestMapping(value = "/companies/{item_id}/financial_information_fields", method = RequestMethod.GET)
    public Object getFinancialInformationFields(@PathVariable(value = "item_id") Integer item_id) throws RestException {
        if (item_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        ArrayList<CrmFieldStateTO> fieldStates = new ArrayList<>();
        ArrayList<String> fieldTypes = new ArrayList<>();
        fieldTypes.add(CustomFormConstants.CURRENCY);
        fieldTypes.add(CustomFormConstants.VAT_NUMBER);
        fieldTypes.add(CustomFormConstants.PAYMENT_METHOD);
        fieldTypes.add(CustomFormConstants.REGISTRATION_NUMBER);
        fieldTypes.add(CustomFormConstants.CLIENT_INVOICE_TERM);

        EdsCrmAccount edsCrmAccount = crmAccountManager.get(item_id);
        if (edsCrmAccount == null || edsCrmAccount.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Company with id ".concat(item_id.toString()).concat(" is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        List<EdsModelField> modelFields;
        EdsModelField modelField = null;
        if (edsCrmAccount.isSupplier()) {
            modelField = modelFieldManager.getByFieldID(LayoutRPC.SUPPLIER_FORM, CustomFormConstants.SUPPLIER_VAT);
        } else if (edsCrmAccount.isClient()) {
            modelField = modelFieldManager.getByFieldID(LayoutRPC.CLIENT_FORM, CustomFormConstants.CLIENT_VAT);
        }
        if (modelField != null && !modelField.isHide()) {
            CrmFieldStateTO fieldState = new CrmFieldStateTO();
            fieldState.setId(modelField.getObjectID() + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
            fieldState.setRequired(modelField.isSystemMandatory() || modelField.isMandatory());
            fieldState.setTitle(modelFieldLocalizer.localizeCrmAccount(modelField.getField_ID()));
            fieldState.setField_type(getFieldType(modelField.getWidget(), modelField.getField_ID()));
            fieldStates.add(fieldState);
        }
        modelFields = modelFieldManager.getSpecificFields(LayoutRPC.ACCOUNT_FORM, fieldTypes);
        getFinancialInformationFields(fieldStates, modelFields);

        return successResponse(new ResponseListData<>(fieldStates));
    }

    private void getFinancialInformationFields(ArrayList<CrmFieldStateTO> fieldStates, List<EdsModelField> modelFields) {
        if (modelFields != null && modelFields.size() > 0) {
            modelFields.forEach(edsModelField -> {
                CrmFieldStateTO fieldState = new CrmFieldStateTO();
                fieldState.setId(edsModelField.getObjectID() + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                fieldState.setRequired(edsModelField.isSystemMandatory() || edsModelField.isMandatory());
                fieldState.setTitle(modelFieldLocalizer.localizeCrmAccount(edsModelField.getField_ID()));
                fieldState.setField_type(getFieldType(edsModelField.getWidget(), edsModelField.getField_ID()));
                fieldStates.add(fieldState);
            });
        }
    }

    private String getFieldType(String widgetType, String fieldId) {
        if ((CRM_ACCOUNT_TYPE.equals(fieldId) && "MULTITABLE".equalsIgnoreCase(widgetType))
                || (CATEGORY.equals(fieldId) && "UNKNOWN".equalsIgnoreCase(widgetType))) {
            return CustomFieldCategoryEnum.MULTIPLY_CHOOSE.getCategory();
        } else if ((ATTACHMENTS.equals(fieldId) || CRM_OPPORTUNITY_ATTACHMENTS.equals(fieldId)) && "UNKNOWN".equalsIgnoreCase(widgetType)) {
            return CustomFieldCategoryEnum.FILE_UPLOAD.getCategory();
        } else if ("TextBox".equalsIgnoreCase(widgetType) || "TextArea".equalsIgnoreCase(widgetType)
                || "NoteWidget".equalsIgnoreCase(widgetType) || "MULTITABLE".equalsIgnoreCase(widgetType)) {
            return CustomFieldCategoryEnum.TEXT_INPUT.getCategory();
        } else if ("DropDown".equalsIgnoreCase(widgetType)
                || "LOOKUP".equalsIgnoreCase(widgetType)) {
            return CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory();
        } else if ("DatePicker".equalsIgnoreCase(widgetType)) {
            return CustomFieldCategoryEnum.DATE.getCategory();
        } else {
            return null;
        }
    }

    @Operation(summary = "Get Company Financial Information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have company financial information"),
            @ApiResponse(responseCode = "400", description = "item_id is required")})
    @RequestMapping(value = "/companies/{item_id}/financial_information", method = RequestMethod.GET)
    public Object getCompanyFinancialInformation(@PathVariable(value = "item_id") Integer item_id) throws RestException {
        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        ResponseListData<CustomFieldsTO> entityCustomFields = new ResponseListData<>();
        ArrayList<CustomFieldsTO> customFields = new ArrayList<>();
        CustomFieldsTO customField;
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(item_id);
        if (edsCrmAccount == null || edsCrmAccount.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Company with id ".concat(item_id.toString()).concat(" is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        LinkedHashMap<String, Integer> modelFieldMap = new LinkedHashMap<>();
        List<ModelField> leadModelFields = modelFieldManager.getFields(LayoutRPC.ACCOUNT_FORM);
        for (ModelField modelField : leadModelFields) {
            modelFieldMap.put(modelField.getField_ID(), modelField.getObjectID());
        }
        getFinancialInformation(customFields, edsCrmAccount, modelFieldMap);

        if (edsCrmAccount.isClient() || edsCrmAccount.isSupplier()) {
            EdsModelField modelField = null;
            if (edsCrmAccount.isSupplier()) {
                modelField = modelFieldManager.getByFieldID(LayoutRPC.SUPPLIER_FORM, CustomFormConstants.SUPPLIER_VAT);
            } else if (edsCrmAccount.isClient()) {
                modelField = modelFieldManager.getByFieldID(LayoutRPC.CLIENT_FORM, CustomFormConstants.CLIENT_VAT);
            }
            if (edsCrmAccount.getVat() != null && modelField != null && !modelField.isHide()) {
                customField = new CustomFieldsTO();
                customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                CategoryTO category = new CategoryTO(edsCrmAccount.getVat().getObjectID(), edsCrmAccount.getVat().getName());
                CustomFieldCategoryChooseTO categoryChoose = new CustomFieldCategoryChooseTO();
                if (edsCrmAccount.isSupplier()) {
                    categoryChoose.setId(modelField.getObjectID() + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                    categoryChoose.setTitle(modelFieldLocalizer.localizeCrmAccount(CustomFormConstants.SUPPLIER_VAT));
                } else {
                    categoryChoose.setId(modelField.getObjectID() + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                    categoryChoose.setTitle(modelFieldLocalizer.localizeCrmAccount(CustomFormConstants.CLIENT_VAT));
                }
                categoryChoose.setCategory(category);
                customField.setObject(categoryChoose);
                customFields.add(customField);
            }
        }
        entityCustomFields.setList(customFields);


        return successResponse(entityCustomFields);
    }

    private void getFinancialInformation(ArrayList<CustomFieldsTO> customFields, EdsCrmAccount edsCrmAccount, LinkedHashMap<String, Integer> modelFieldMap) {
        CustomFieldsTO customField;
        ArrayList<String> fieldTypes = new ArrayList<>();
        fieldTypes.add(CustomFormConstants.CURRENCY);
        fieldTypes.add(CustomFormConstants.VAT_NUMBER);
        fieldTypes.add(CustomFormConstants.PAYMENT_METHOD);
        fieldTypes.add(CustomFormConstants.REGISTRATION_NUMBER);
        fieldTypes.add(CustomFormConstants.CLIENT_INVOICE_TERM);

        List<EdsModelField> modelFields = modelFieldManager.getSpecificFields(LayoutRPC.ACCOUNT_FORM, fieldTypes);
        if (modelFields != null) {
            for (EdsModelField modelField : modelFields) {
                if (edsCrmAccount.getCurrency() != null && modelField.getField_ID().equals(CustomFormConstants.CURRENCY)) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                    CategoryTO category = new CategoryTO(edsCrmAccount.getCurrency().getObjectID(), edsCrmAccount.getCurrency().getName());
                    customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CustomFormConstants.CURRENCY) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeCrmAccount(CustomFormConstants.CURRENCY), category));
                    customFields.add(customField);
                }
                if (StringUtils.isNotBlank(edsCrmAccount.getVatNumber()) && modelField.getField_ID().equals(CustomFormConstants.VAT_NUMBER)) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                    CustomFieldTextTO text = new CustomFieldTextTO();
                    text.setId(modelFieldMap.get(CustomFormConstants.VAT_NUMBER) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                    text.setTitle(modelFieldLocalizer.localizeCrmAccount(CustomFormConstants.VAT_NUMBER));
                    text.setText(edsCrmAccount.getVatNumber());
                    customField.setObject(text);

                    customFields.add(customField);
                }
                if (edsCrmAccount.getPaymentMethod() != null && modelField.getField_ID().equals(CustomFormConstants.PAYMENT_METHOD)) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                    CategoryTO category = new CategoryTO(edsCrmAccount.getPaymentMethod().getObjectID(), edsCrmAccount.getPaymentMethod().getName());
                    customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CustomFormConstants.PAYMENT_METHOD) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeCrmAccount(CustomFormConstants.PAYMENT_METHOD), category));
                    customFields.add(customField);
                }
                if (StringUtils.isNotBlank(edsCrmAccount.getRegistrationNumber()) && modelField.getField_ID().equals(CustomFormConstants.REGISTRATION_NUMBER)) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                    CustomFieldTextTO text = new CustomFieldTextTO();
                    text.setId(modelFieldMap.get(CustomFormConstants.REGISTRATION_NUMBER) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                    text.setTitle(modelFieldLocalizer.localizeCrmAccount(CustomFormConstants.REGISTRATION_NUMBER));
                    text.setText(edsCrmAccount.getRegistrationNumber());
                    customField.setObject(text);

                    customFields.add(customField);
                }
                if (edsCrmAccount.getTerms() != null && modelField.getField_ID().equals(CustomFormConstants.CLIENT_INVOICE_TERM)) {
                    customField = new CustomFieldsTO();
                    customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                    CategoryTO category = new CategoryTO(edsCrmAccount.getTerms().getObjectID(), edsCrmAccount.getTerms().getName());
                    customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CustomFormConstants.CLIENT_INVOICE_TERM) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeCrmAccount(CustomFormConstants.CLIENT_INVOICE_TERM), category));
                    customFields.add(customField);
                }
            }
        }
    }

    private void getAccountPayable(String query, Integer start, Integer maxLimit, EntityCategoryTO entityCategories, ArrayList<CategoryTO> categories) {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        if (isSupplier) {
            filterParameter.setAccountType(Constants.PAYABLE);
        } else {
            filterParameter.setAccountType(Constants.RECEIVABLE);
        }
        ArrayList<AccountItem> accountItems = accountingServiceLocal.getAccountsReceivablePayable(filterParameter);
        if (accountItems != null) {
            if (StringUtils.isNotBlank(query)) {
                accountItems = (ArrayList) accountItems.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
            }
            entityCategories.setTotal_count(accountItems.size());
            if (accountItems.size() < (maxLimit + start)) {
                entityCategories.setLeft(0);
            } else {
                entityCategories.setLeft(accountItems.size() - (start + maxLimit));
            }
            ArrayList<AccountItem> subList = ListUtils.getSublistSmart(accountItems, start, maxLimit);
            entityCategories.setCount(subList.size());
            entityCategories.setOffset(start);
            subList.forEach(accountItem -> {
                CategoryTO category = new CategoryTO();
                category.setId(accountItem.getId());
                category.setTitle(accountItem.getName());
                categories.add(category);
            });
            entityCategories.setList(categories);
        }
    }

    private void getCrmAccountSelectItems(String query, Integer start, Integer maxLimit, EntityCategoryTO entityCategories, ArrayList<CategoryTO> categories, SelectItem[] crmAccountSelectItems) {
        if (crmAccountSelectItems != null) {
            List<SelectItem> crmAccountItemsList = Arrays.asList(crmAccountSelectItems);

            if (StringUtils.isNotBlank(query)) {
                crmAccountItemsList = crmAccountItemsList.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
            }
            entityCategories.setTotal_count(crmAccountItemsList.size());
            if (crmAccountItemsList.size() < (maxLimit + start)) {
                entityCategories.setLeft(0);
            } else {
                entityCategories.setLeft(crmAccountItemsList.size() - (start + maxLimit));
            }
            ArrayList<SelectItem> stringArrayList = new ArrayList<>(crmAccountItemsList);
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
}
