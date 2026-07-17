package com.edatasite.workforce.rest.v2.release10.crm;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsFacetFilter;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsUserFilter;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.contactcategory.server.ContactCategoryServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
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
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.server.actions.CreateAttachmentHandler;
import com.edatasite.workforce.gwt.core.server.actions.CreateDocumentCommand;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.ModelFieldManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.FacetFilterManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.UserFilterManager;
import com.edatasite.workforce.gwt.core.server.servlets.WfmMultipartFile;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmAccountList;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
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
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApiResult;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CountriesListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.EntityCategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactAddressAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactDetailsItemResponseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactDetailsItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactEditTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CrmAccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EmailDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EntityContactAddressListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EntityContactAddressTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EntityInformationResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EntityInformationTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.FilteredStatusItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.FilteredStatusesRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.PhoneDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.AddContactTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.AddZapierContactTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.NewContactTO;
import com.edatasite.workforce.rest.v2.release10.core.to.note.GeneralNoteTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskBaseInfoTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskDetailInfoTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.ColorTO;
import com.edatasite.workforce.rest.v2.release10.enums.EntityFieldTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.EntityTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderByEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderFieldEnum;
import com.edatasite.workforce.rest.v2.release10.enums.TaskPriorityEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.G_WORK;

/**
 * Created by Abdurakhmonov Farrukh on 01/29/2018.
 */
@Tag(name = "Contacts", description = "Contacts API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiContactsControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiContactsControllerV2.class);
    private final Integer GAP_BTW_STATIC_AND_CUSTOM_FIELDS = 100000;
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private HttpServletRequest servletRequest;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private NoteServiceLocal noteServiceLocal;
    @Autowired
    private FacetFilterManager facetFilterManager;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private ContactCategoryServiceLocal contactCategoryServiceLocal;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private UserFilterManager userFilterManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ModelFieldManager modelFieldManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private WfmCommandServiceLocal wfmCommandServiceLocal;

    @RequestMapping(value = "/contacts/create_contact", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_CONTACTS_LIST, PermissionConstants.CRM_ADD_NEW_CONTACT})
    public Object createContact(@RequestBody NewContactTO newContactTO) throws RestException {

        if (StringUtils.isBlank(newContactTO.getFirst_name())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "first_name field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(newContactTO.getLast_name())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "last_name field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(newContactTO.getEmail())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "email field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        ContactListItem contactListItem = new ContactListItem();
        contactListItem.setFirstName(newContactTO.getFirst_name());
        contactListItem.setLastName(newContactTO.getLast_name());
        contactListItem.setHomeEmail(newContactTO.getEmail());
        contactListItem.setMobile(newContactTO.getMobile());
        try {
            contactServiceLocal.saveContact(contactListItem, null, true);
            return successResponse(new ResponseData());
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@Operation(summary = "Quick Filters", description = "Retrieves data on quick filters.\n\nQuick filters are of two types: \n\n" +
            "PEOPLE - by people, all employees are displayed on which there are leads in all statuses (which are listed as \"Assigned to\").\n\n" +
            "CATEGORIES - by category, these are filters created by the user.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of filtered people or category")})
    @RequestMapping(value = "/contacts/quick_filter", method = RequestMethod.GET)
    public Object quickFilter() throws RestException {

        HashMap<String, FacetSolrField> showSolrFieldMap = new HashMap<>();
        FacetSolrField contactOwnerField = new FacetSolrField(SolrContactRepresenter.FIELD_OWNER_ID, SolrContactRepresenter.FIELD_OWNER_ID_NAME);
        showSolrFieldMap.put(FacetContentType.ContactFacetFilter.getContentCode()[4], contactOwnerField);

        ArrayList<String> showFacetCodeName = new ArrayList<>();
        showFacetCodeName.add(FacetContentType.ContactFacetFilter.getContentCode()[4]);

        FacetFilterRpc contactOwnerFacetFilter = new FacetFilterRpc(ListPanelType.ContactListPanel, showSolrFieldMap, showFacetCodeName);
        //Important if we not set below to true then it will use default filter
        contactOwnerFacetFilter.setFilterChanges(true);
        //Retrieve Data from solr
        contactOwnerFacetFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_CONTACT, contactOwnerFacetFilter);

        EdsUser currentUser = userManager.getUser();

        EdsFacetFilter edsFacetFilter = facetFilterManager.getDefaultUserFacetFilter(ListPanelType.ContactsQuickFilterForMobile, currentUser);
        HashMap<Integer, String> activePeople = new HashMap<>();
        HashMap<Integer, String> activeFilters = new HashMap<>();
        if (edsFacetFilter != null) {
            HashSet colNames = new HashSet<>();
            colNames.add("people");
            colNames.add("categories");
            FacetFilterRpc facetFilterRpc = edsFacetFilter.getFacetFilter(colNames);

            FacetContentRpc p = facetFilterRpc.getFacetContentMap().get("people");
            FacetContentRpc c = facetFilterRpc.getFacetContentMap().get("categories");
            if (p != null) {
                for (SelectItem pI : p.getFacetItems()) {
                    activePeople.put(pI.getId() != null && pI.getId() > 0 ? pI.getId() : 0, "");
                }
            }
            if (c != null) {
                for (SelectItem cI : c.getFacetItems()) {
                    activeFilters.put(cI.getId(), "");
                }
            }
        }

        ArrayList<PeopleTO> peoples = new ArrayList<>();
        ArrayList<SavedFilterTO> categories = new ArrayList<>();
        PeopleTO na = null;
        PeopleTO me = null;
        if (contactOwnerFacetFilter.getFacetContentMap() != null) {
            FacetContentRpc contentRpc = contactOwnerFacetFilter.getFacetContentMap().get(FacetContentType.ContactFacetFilter.getContentCode()[4]);
            if (contentRpc != null && contentRpc.getFacetItems() != null) {
                for (SelectItem selectItem : contentRpc.getFacetItems()) {
                    //if id = -1 then make it 0
                    if (selectItem.getId() == -1) {
                        selectItem.setId(0);
                    }
                    PeopleTO peopleTO = new PeopleTO();
                    peopleTO.setPeople_name(selectItem.getName());
                    peopleTO.setPeople_is_active(activePeople.get(selectItem.getId()) != null);

                    if (selectItem.getId() != null && selectItem.getId() > 0) {
                        peopleTO.setPeople_id(selectItem.getId());
                        EdsUser assignee = userManager.get(selectItem.getId());
                        if (assignee != null && assignee.getPhoto() != null) {
                            String imageUrl = commonServiceLocal.getImageUrl(assignee.getPhoto().getObjectID());
                            if (StringUtils.isNotBlank(imageUrl)) {
                                peopleTO.setPeople_avatar(imageUrl);
                            }
                        }

                        if (currentUser.getObjectID().equals(selectItem.getId())) {
                            peopleTO.setPeople_type(PeopleTypeEnum.ME.getType());
                        } else {
                            peopleTO.setPeople_type(PeopleTypeEnum.USUAL.getType());
                        }
                    } else {
                        peopleTO.setPeople_id(0);
                        peopleTO.setPeople_type(PeopleTypeEnum.N_A.getType());
                    }
                    if (PeopleTypeEnum.N_A.getType().equalsIgnoreCase(peopleTO.getPeople_type())) {
                        na = peopleTO;
                    } else if (PeopleTypeEnum.ME.getType().equalsIgnoreCase(peopleTO.getPeople_type())) {
                        me = peopleTO;
                    } else {
                        peoples.add(peopleTO);
                    }
                }
            }
        }
        //As per Stepans request we need to return ME type first and N/A type second
        if (me != null) {
            peoples.add(0, me);
        }
        if (na != null) {
            peoples.add(me != null ? 1 : 0, na);
        }

        SaveFilterSelectItems filterSelectList = commonServiceLocal.getSavedFacetFilterList(ListPanelType.ContactListPanel, null);
        if (filterSelectList.getItems() != null && filterSelectList.getItems().length > 0) {
            //Integer defaultFilterId = filterSelectList.getDefaultFilterID();
            for (SelectItem item : filterSelectList.getItems()) {
                SavedFilterTO savedFilterTO = new SavedFilterTO();
                savedFilterTO.setCategory_id(item.getId());
                savedFilterTO.setCategory_name(item.getName());
                savedFilterTO.setCategory_is_active(activeFilters.get(item.getId()) != null);

                categories.add(savedFilterTO);
            }
        }

        QuickFiltersTO quickFilters = new QuickFiltersTO();
        quickFilters.setPeople(peoples);
        quickFilters.setCategories(categories);

        //Consider OTF active after we apply it with some values
        FacetFilterRpc defaultFilter = initializeDefaultContactFacetFilter();
        //Retrieve FACET FILTER data
        defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_CONTACT, defaultFilter);
        FacetFilterRpc otf = getOneTimeFilter(defaultFilter);
        quickFilters.setOtf_is_active(isOTFActive(otf, activeFilters));

        if (peoples.isEmpty()) {
            throw new RestException("Currently, you do not have any contacts in this criteria", "Currently, you do not have any contacts in this criteria", NO_ITEMS_FOUND, HttpStatus.NOT_FOUND);
        }

        return successResponse(quickFilters);
    }*/

    @Operation(summary = "Search Contacts", description = "Getting the list of contacts (items, items) on the entered characters.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have contacts based on search query"),
            @ApiResponse(responseCode = "400", description = "query is required")})
    @RequestMapping(value = "/contacts/search", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.CRM_CONTACTS_LIST})
    public Object searchContact(@RequestParam(value = "query") String query,
                                @RequestParam(value = "limit", required = false) Integer limit,
                                @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        PagingListResultTO<ContactTO> contactListResult = new PagingListResultTO<>();
        if (StringUtils.isBlank(query)) {
            return successResponse(contactListResult);
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
        filterParameter.setLookUp(true);

        ListResult<ContactListItem> result;
        try {
            result = contactServiceLocal.getNewContactList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        contactListResult.setTotal_count(result.getTotal());
        if (result.getTotal() < (maxLimit + start)) {
            contactListResult.setLeft(0);
        } else {
            contactListResult.setLeft(result.getTotal() - (start + maxLimit));
        }
        contactListResult.setCount(result.getList() != null ? result.getList().size() : 0);
        contactListResult.setOffset(start);

        ArrayList<ContactTO> contactList = new ArrayList<>();
        for (ContactListItem contactListItem : result.getList()) {
            ContactTO contactItem = new ContactTO();
            if (StringUtils.isNotBlank(contactListItem.getName())) {
                contactItem.setName(contactListItem.getName().trim());
            }
            contactItem.setItem_id(contactListItem.getObjectId());
            contactItem.setAvatar_image(contactListItem.getContactImageUrl());
            if (contactListItem.getCrmAccount() != null) {
                CrmAccountTO company = new CrmAccountTO();
                company.setItem_id(contactListItem.getCrmAccount().getObjectId());
                company.setName(contactListItem.getCrmAccount().getName());
                company.setAvatar_image(contactListItem.getCrmAccount().getLogoUrl());
                contactItem.setCompany(company);
            }
            EdsCrmContact edsCrmContact = crmContactManager.get(contactListItem.getObjectId());
            ContactsTO contactsTO = new ContactsTO();
            if (edsCrmContact != null) {
                //Phones
                contactsTO.setPhones(contactServiceLocal.convertToPhoneTO(edsCrmContact));

                //Emails
                contactsTO.setEmails(contactServiceLocal.convertContactEmails(edsCrmContact));
            }
            contactItem.setContacts(contactsTO);
            contactList.add(contactItem);
        }

        contactListResult.setList(contactList);

        return successResponse(contactListResult);
    }

    @RequestMapping(value = "/contacts/items_zapier", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @CheckPermission(permissions = {PermissionConstants.CRM_CONTACTS_LIST})
    public Object getContactsListForZapier(@RequestBody ContactRequestTO requestTO) throws RestException {
        /*ApiResult result = (ApiResult) getContactsList(requestTO);
        return ((PagingListResultTO) result.getData()).getList();*/
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

        ListingFilterParameter filterParameter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.ContactListPanel);

        ArrayList<String> columnCodeNames = ContactListItem.defaultContactColumnNames;
        ListPanelToolRpc panelTools = new ListPanelToolRpc();
        panelTools.setColumnCodeName(columnCodeNames);
        panelTools.setShowPopup(true);
        filterParameter.setListPanelTool(panelTools);
        filterParameter.setColumnsOfListing(columnCodeNames);

        filterParameter.setStart(requestTO.getOffset());
        filterParameter.setLimit(requestTO.getCount());
        filterParameter.setSearchButton(false);
        filterParameter.setDetectDuplicates(false);
        filterParameter.setWithImage(true);
        if (orderFieldEnum != null) {
            filterParameter.setSortField(getSortField(orderFieldEnum, ListPanelType.ContactListPanel));
        }
        filterParameter.setAscending(orderByEnum == null || OrderByEnum.ASC.getDirection().equals(orderByEnum.getDirection()));
        filterParameter.setSortDir(orderByEnum != null ? orderByEnum.getId() : OrderByEnum.ASC.getId());

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
        ArrayList<AddZapierContactTO> contactItems = new ArrayList<>();
        if (result != null) {


            if (result.getList() != null && !result.getList().isEmpty()) {
                for (ContactListItem contactListItem : result.getList()) {

                    EdsCrmContact edsCrmContact = crmContactManager.get(contactListItem.getObjectId());

                    if (edsCrmContact != null) {
                        contactItems.add(contactServiceLocal.convertToContact(edsCrmContact));
                    }

                }
            }
        }
        return contactItems;
    }

    @RequestMapping(value = "/contacts/items", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//    @CheckPermission(permissions = {PermissionConstants.CRM_CONTACTS_LIST})
    public Object getContactsList(@RequestBody ContactRequestTO requestTO) throws RestException {
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

//        HashMap<String, FacetContentRpc> facetContentMap = filterParameter.getFacetFilter().getFacetContentMap();

        FacetFilterRpc mainMergedFilter = initializeDefaultContactFacetFilter();

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
                                /*&& !FacetContentType.ContactFacetFilter.getContentCode()[4].equalsIgnoreCase(entry.getKey())*/) {
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
        if (requestTO.getFilter().getPeople_filter_id() != null && !requestTO.getFilter().getPeople_filter_id().isEmpty()) {

            FacetContentRpc existingAssignedTo = null;
            if (mainMergedFilter != null) {
                existingAssignedTo = mainMergedFilter.getFacetContentMap().get(FacetContentType.ContactFacetFilter.getContentCode()[4]);
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
                mainMergedFilter.getFacetContentMap().put(FacetContentType.ContactFacetFilter.getContentCode()[4], existingAssignedTo);
            }
        }

        saveQuickFilter(people, categories);


        //If OneTimeFilter is active
        FacetFilterRpc oneTimeFilter = getOneTimeFilter(initializeDefaultContactFacetFilter());
        if (oneTimeFilter.isFavourFilter()) {
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


        ListingFilterParameter filterParameter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.ContactListPanel);

        ArrayList<String> columnCodeNames = ContactListItem.defaultContactColumnNames;
        ListPanelToolRpc panelTools = new ListPanelToolRpc();
        panelTools.setColumnCodeName(columnCodeNames);
        panelTools.setShowPopup(true);
        filterParameter.setListPanelTool(panelTools);
        filterParameter.setColumnsOfListing(columnCodeNames);
        filterParameter.setFacetFilter(mainMergedFilter);

        filterParameter.setStart(requestTO.getOffset());
        filterParameter.setLimit(requestTO.getCount());
        filterParameter.setAccountID(requestTO.getCrmAccountId());
        filterParameter.setSearchButton(false);
        filterParameter.setDetectDuplicates(false);
        filterParameter.setWithImage(true);
        if (orderFieldEnum != null) {
            filterParameter.setSortField(getSortField(orderFieldEnum, ListPanelType.ContactListPanel));
        }
        filterParameter.setAscending(orderByEnum == null || OrderByEnum.ASC.getDirection().equals(orderByEnum.getDirection()));
        filterParameter.setSortDir(orderByEnum != null ? orderByEnum.getId() : OrderByEnum.ASC.getId());

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
        PagingListResultTO<ContactTO> contactsResult = new PagingListResultTO<>();
        if (result != null) {
            contactsResult.setTotal_count(result.getTotal());
            contactsResult.setOffset(requestTO.getOffset());
            contactsResult.setCount(result.getList() != null ? result.getList().size() : 0);
            if (result.getTotal() < (requestTO.getCount() + requestTO.getOffset())) {
                contactsResult.setLeft(0);
            } else {
                contactsResult.setLeft(result.getTotal() - (requestTO.getOffset() + requestTO.getCount()));
            }
            ArrayList<ContactTO> contactItems = new ArrayList<>();
            if (result.getList() != null && !result.getList().isEmpty()) {
                for (ContactListItem contactListItem : result.getList()) {
                    ContactTO contactItem = new ContactTO();
                    if (StringUtils.isNotBlank(contactListItem.getName())) {
                        contactItem.setName(contactListItem.getName());
                    }
                    if (StringUtils.isNotBlank(contactListItem.getFirstName())) {
                        contactItem.setFirst_name(contactListItem.getFirstName());
                    }
                    if (StringUtils.isNotBlank(contactListItem.getLastName())) {
                        contactItem.setLast_name(contactListItem.getLastName());
                    }
                    contactItem.setItem_id(contactListItem.getObjectId());
                    contactItem.setAvatar_image(contactListItem.getContactImageUrl());
                    EdsCrmContact edsCrmContact = crmContactManager.get(contactListItem.getObjectId());
                    ContactsTO contactsTO = new ContactsTO();

                    if (edsCrmContact != null) {
                        //Phones
                        contactsTO.setPhones(contactServiceLocal.convertToPhoneTO(edsCrmContact));

                        //If there is no contact name, set the contact one of phones as contact name
                        if (contactItem.getName() == null && !contactsTO.getPhones().isEmpty()) {
                            if (contactsTO.getPhones().get(0).getCountry_code() != null) {
                                contactItem.setName(contactsTO.getPhones().get(0).getCountry_code().concat(contactsTO.getPhones().get(0).getPhone_number()));
                            } else {
                                contactItem.setName(contactsTO.getPhones().get(0).getPhone_number());
                            }
                        }
                        //Emails
                        contactsTO.setEmails(contactServiceLocal.convertContactEmails(edsCrmContact));
                        //Company
                        contactItem.setCompany(contactServiceLocal.convertCompany(edsCrmContact.getCrmAccount()));
                        //Addresses
                        contactItem.setEntityAddresses(contactServiceLocal.convertAddresses(edsCrmContact));
                    }
                    contactItem.setContacts(contactsTO);

                    contactItems.add(contactItem);
                }
            }
            contactsResult.setList(contactItems);
        }

        return successResponse(contactsResult);
    }

    @Operation(summary = "Detail Contact Info")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have contact details"),
            @ApiResponse(responseCode = "400", description = "id is required")})
    @RequestMapping(value = "/contacts/{id}/details", method = RequestMethod.GET)
    public Object getContactDetails(@PathVariable(value = "id") Integer id) throws RestException {
        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsCrmContact edsCrmContact = crmContactManager.get(id);
        if (edsCrmContact == null || edsCrmContact.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Contact with id " + id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        ContactDetailsItemTO contactDetailsItem = new ContactDetailsItemTO();

        //Base Info
        ContactTO contactBaseInfo = contactServiceLocal.convertToContactTO(edsCrmContact);

        contactDetailsItem.setBase_info(contactBaseInfo);

        //One of latest task
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(0);
        filterParameter.setLimit(1);
        filterParameter.setSortField(TaskListItem.ID);
        filterParameter.setRelationType(RelationItem.TYPE_CONTACT);
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

                //Task Status
                FilteredStatusItemTO status = new FilteredStatusItemTO();
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

                //Task
                contactDetailsItem.setTask(task);
            }
        } catch (Exception e) {
            log.error("Error occurred while getting contact's tasks ", e);
        }

        //Company
        contactDetailsItem.setCompany(contactBaseInfo.getCompany());

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
                contactDetailsItem.setNote(note);
            }
        } catch (Exception e) {
            log.error("Error occurred while getting contact's notes ", e);
        }

        ContactDetailsItemResponseTO result = new ContactDetailsItemResponseTO();
        result.setItem(contactDetailsItem);

        return successResponse(result);
    }


    @Operation(summary = "Company Contacts")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have contacts related to a particular company"),
            @ApiResponse(responseCode = "400", description = "item_id, sort_type and direction are required")})
    @RequestMapping(value = "/companies/{item_id}/contacts", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.CRM_CONTACTS_LIST})
    public Object getCompanyContacts(
            @PathVariable(value = "item_id") Integer item_id,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "sort_type", required = false) String sort_type,
            @RequestParam(value = "direction", required = false) String direction) throws RestException {

        if (item_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id should be more than zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(item_id);
        if (edsCrmAccount == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "No company found with provided id", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (direction != null && !OrderByEnum.ASC.getDirection().equals(direction) && !OrderByEnum.DESC.getDirection().equals(direction)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "direction should be ASC or DESC", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (sort_type != null && !OrderFieldEnum.NAME.getField().equalsIgnoreCase(sort_type) && !OrderFieldEnum.ID.getField().equalsIgnoreCase(sort_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "sort_type should be name or id", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }


        ArrayList<ContactTO> companyContacts = new ArrayList<>();

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.isFiltirize();
        filterParameter.setAccountID(item_id);
        filterParameter.setStart(0);
        filterParameter.setLimit(limit != null ? limit : MAX_LIMIT);
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
            result.getList().forEach(contactListItem -> {
                ContactTO companyContact = new ContactTO();
                companyContact.setName(contactListItem.getName());
                companyContact.setItem_id(contactListItem.getObjectId());
                EdsCrmContact crmContact = crmContactManager.get(contactListItem.getObjectId());
                if (crmContact.getPhoto() != null) {
                    companyContact.setAvatar_image(commonServiceLocal.getImageUrl(crmContact.getPhoto().getObjectID()));
                }
                ContactsTO contactsTO = new ContactsTO();

                //Phones
                contactsTO.setPhones(contactServiceLocal.convertToPhoneTO(crmContact));

                //Emails
                contactsTO.setEmails(contactServiceLocal.convertContactEmails(crmContact));

                companyContact.setContacts(contactsTO);

                //Contact's Company
                companyContact.setCompany(contactServiceLocal.convertCompany(crmContact.getCrmAccount()));

                companyContacts.add(companyContact);
            });
        }
        if (OrderFieldEnum.NAME.getField().equalsIgnoreCase(sort_type)) {
            companyContacts.sort((o1, o2) -> {
                if (OrderByEnum.ASC.getDirection().equals(direction)) {
                    return o1.getName().compareTo(o2.getName());
                } else {
                    return o2.getName().compareTo(o1.getName());
                }
            });
        } else {
            companyContacts.sort((o1, o2) -> {
                if (OrderByEnum.ASC.getDirection().equals(direction)) {
                    return o1.getItem_id().compareTo(o2.getItem_id());
                } else {
                    return o2.getItem_id().compareTo(o1.getItem_id());
                }
            });
        }
        return successResponse(new ResponseListData<>(companyContacts));
    }


    @Operation(summary = "Delete Contact", description = "Delete particular entity like Lead, Opportunity, Company, Contact etc. Particular entity is described in path, like other requests. Server should check if current user has permissions to delete this particular item, and if no give user message: You don't have permissions to delete this entry. Please contact your administrator")
    @ApiResponses(value = @ApiResponse(responseCode = "200"))
    @RequestMapping(value = "/contacts/{item_id}/delete", method = RequestMethod.DELETE)
    public ApiResult deleteContact(@PathVariable(value = "item_id") Integer itemId) throws RestException {
        if (itemId == null || itemId <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsCrmContact edsCrmContact = crmContactManager.get(itemId);
        EdsUser user = userManager.getUser();
        if (edsCrmContact == null || edsCrmContact.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Contact with id " + itemId + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (!ServerUtils.hasPermission(PermissionConstants.CRM_CONTACTS_LIST) || !ServerUtils.hasPermission(PermissionConstants.CRM_REMOVE_CONTACT)) {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
        if (edsCrmContact.getContactType() != null && CrmConstants.TYPE_EMPLOYEE_CONTACT == edsCrmContact.getContactType()) {
            throw new RestException("In order to delete the contact, first delete the employee corresponding to the contact", "In order to delete the contact, first delete the employee corresponding to the contact", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
        PermissionHolder permissionHolder = contactServiceLocal.getContactPermission(itemId);
        if ((permissionHolder == null || !permissionHolder.isDelete()) && !user.hasRole(EdsRole.ADMIN_CODE) && (edsCrmContact.getOwner() == null || !user.getObjectID().equals(edsCrmContact.getOwner().getObjectID()))) {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
        ArrayList<Integer> objectIDs = new ArrayList<>();
        objectIDs.add(itemId);
        ArrayList<Integer> result;
        try {
            result = contactServiceLocal.deleteContacts(objectIDs, edsCrmContact.getOwner() != null ? edsCrmContact.getOwner().getObjectID() : null, false);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!result.isEmpty()) {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Get Entity Category List", description = "Get Categories for particular entities like leads, activities, opportunities")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the list of particular entities like leads, activities, opportunities")})
    @RequestMapping(value = "/contacts/{field_type}/categories", method = RequestMethod.GET)
    public Object getEntityFieldCategories(
            @PathVariable(value = "field_type") String field_type,
            @RequestParam(value = "custom_field_id", required = false) Integer custom_field_id,
            @RequestParam(value = "dependency_id", required = false) Integer dependency_id,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        if (StringUtils.isBlank(field_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "field_type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (!EntityFieldTypeEnum.COMPANY.name().equals(field_type) && !EntityFieldTypeEnum.CUSTOM.name().equals(field_type) && !EntityFieldTypeEnum.SUPERVISOR.name().equals(field_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "field_type should be one of COMPANY | SUPERVISOR | CUSTOM", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
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
        } else if (EntityFieldTypeEnum.SUPERVISOR.name().equals(field_type)) {
            if (dependency_id == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "dependency_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            SelectItem[] supervisors = crmServiceLocal.getContactsByAccount(dependency_id, null);
            getContactSelectItems(query, start, maxLimit, entityCategories, categories, supervisors);
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

                if (CustomFormConstants.OWNER.equals(modelField.getField_ID())) {
                    SelectItem[] contactItems = crmServiceLocal.getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE);
                    getContactSelectItems(query, start, maxLimit, entityCategories, categories, contactItems);
                } else if (CustomFormConstants.CRM_ACCOUNT_INDUSTRY.equals(modelField.getField_ID())) {
                    SelectItem[] contactItems = contactServiceLocal.getContactSelectItems(Constants._COMPANY_WORKAREA);
                    getContactSelectItems(query, start, maxLimit, entityCategories, categories, contactItems);
                } else if (CustomFormConstants.CRM_ACCOUNT_ORGANIZATION_TYPE.equals(modelField.getField_ID())) {
                    SelectItem[] contactItems = contactServiceLocal.getContactSelectItems(Constants.CONTACT_ORGANIZATION_TYPES);
                    getContactSelectItems(query, start, maxLimit, entityCategories, categories, contactItems);
                } else if (CustomFormConstants.CRM_ACCOUNT_ANNUAL_REVENUE.equals(modelField.getField_ID())) {
                    SelectItem[] contactItems = contactServiceLocal.getContactSelectItems(Constants.ANNUAL_REVENUE);
                    getContactSelectItems(query, start, maxLimit, entityCategories, categories, contactItems);
                } /*else if (CustomFormConstants.CRM_ACCOUNT_OWNERSHIP.equals(modelField.getField_ID())) {
                    SelectItem[] contactItems = contactServiceLocal.getOwnerships();
                    getContactSelectItems(query, start, maxLimit, entityCategories, categories, contactItems);
                }*/ else if (CustomFormConstants.CRM_CAMPAIGN_NAME.equals(modelField.getField_ID())) {
                    ListingFilterParameter filterParameter = new ListingFilterParameter();
                    filterParameter.setCRM(true);
                    SelectItem[] contactItems = allInOneServiceLocal.getLookUpItems(filterParameter, LookUpConstants.CRM_CAMPAIGN_ID, null);
                    getContactSelectItems(query, start, maxLimit, entityCategories, categories, contactItems);
                } else if (CustomFormConstants.CRM_ACCOUNT_NUMBER_OF_EMPLOYEE.equals(modelField.getField_ID())) {
                    SelectItem[] contactItems = contactServiceLocal.getContactSelectItems(Constants.NUMBER_OF_EMPLOYEES);
                    getContactSelectItems(query, start, maxLimit, entityCategories, categories, contactItems);
                } else if (CustomFormConstants.CRM_ACCOUNT_TYPE.equals(modelField.getField_ID())) {
                    SelectItem[] accountTypes = contactServiceLocal.getAccountTypes();
                    getContactSelectItems(query, start, maxLimit, entityCategories, categories, accountTypes);
                } else if (CustomFormConstants.CATEGORY.equals(modelField.getField_ID())) {
                    ArrayList<ContactCategoryListItem> contactCategoryListItems = contactCategoryServiceLocal.getContactCategories();
                    if (contactCategoryListItems != null) {
                        if (StringUtils.isNotBlank(query)) {
                            contactCategoryListItems = (ArrayList) contactCategoryListItems.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                        }
                        entityCategories.setTotal_count(contactCategoryListItems.size());
                        if (contactCategoryListItems.size() < (maxLimit + start)) {
                            entityCategories.setLeft(0);
                        } else {
                            entityCategories.setLeft(contactCategoryListItems.size() - (start + maxLimit));
                        }
                        ArrayList<ContactCategoryListItem> subList = ListUtils.getSublistSmart(contactCategoryListItems, start, maxLimit);
                        entityCategories.setCount(subList.size());
                        entityCategories.setOffset(start);
                        subList.forEach(contactCategoryListItem -> {
                            CategoryTO category = new CategoryTO();
                            category.setId(contactCategoryListItem.getObjectID());
                            category.setTitle(contactCategoryListItem.getName());
                            categories.add(category);
                        });
                        entityCategories.setList(categories);
                    }
                }
            }
        }
        return successResponse(entityCategories);
    }

    private void getContactSelectItems(@RequestParam(value = "query", required = false) String query, Integer start, Integer maxLimit, EntityCategoryTO entityCategories, ArrayList<CategoryTO> categories, SelectItem[] contactItems) {
        if (contactItems != null) {
            List<SelectItem> salesPeopleList = Arrays.asList(contactItems);

            if (StringUtils.isNotBlank(query)) {
                salesPeopleList = salesPeopleList.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
            }
            entityCategories.setTotal_count(salesPeopleList.size());
            if (salesPeopleList.size() < (maxLimit + start)) {
                entityCategories.setLeft(0);
            } else {
                entityCategories.setLeft(salesPeopleList.size() - (start + maxLimit));
            }
            ArrayList<SelectItem> stringArrayList = new ArrayList<>(salesPeopleList);
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

    @Operation(summary = "Get Contact Addresses")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Contact addresses"),
            @ApiResponse(responseCode = "400", description = "item_id is required")})
    @RequestMapping(value = "/contacts/{item_id}/addresses", method = RequestMethod.GET)
    public Object getContactAddresses(@PathVariable(value = "item_id") Integer item_id) throws RestException {
        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsCrmContact edsCrmContact;
        try {
            edsCrmContact = crmContactManager.get(item_id);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ArrayList<EntityContactAddressTO> entityAddresses = new ArrayList<>();
        if (edsCrmContact != null) {
            List<EdsAddress> addresses = edsCrmContact.getAddresses();
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

    @Operation(summary = "Update Contact Addresses")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/contacts/{item_id}/addresses", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_CONTACTS_LIST, PermissionConstants.CRM_EDIT_CONTACT})
    public Object updateContactAddresses(@PathVariable(value = "item_id") Integer item_id,
                                         @RequestBody EntityContactAddressListTO updateEntityContactAddressTO) throws RestException {

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ContactListItem contactListItem;
        try {
            contactListItem = contactServiceLocal.editContact(ContactListItem.CRM_CONTACT, item_id, null, null, false);
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


    @Operation(summary = "Update Contact Additional Information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/contacts/{item_id}/additional_information",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_CONTACTS_LIST, PermissionConstants.CRM_EDIT_CONTACT})
    public Object updateContactAdditionalInformation(
            @PathVariable(value = "item_id") Integer item_id,
            MultipartRequest multipartRequest,
            @RequestParam(name = "body") String jsonString) throws RestException {


        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        LinkedHashMap<Object, ArrayList<Object>> customFieldsObjectMap;

        try {
            customFieldsObjectMap = (LinkedHashMap<Object, ArrayList<Object>>) mapper.readValue(jsonString, Object.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong. ".concat(e.getMessage()), REQUIRED, HttpStatus.BAD_REQUEST);
        }

        //Get model fields by entity type
        List<ModelField> modelFields;
        try {
            modelFields = modelFieldManager.getFields(LayoutRPC.CONTACT_FORM);
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
        HistoryListItem note = null;

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
                contactListItem.setDepartment((String) modelFieldValueMap.get(fieldID));
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
                List<EdsReference> relationships = referenceManager.listReferences(EdsCrmContact.CONTACT_RELATION);
                if (relationships != null && relationships.size() > 0) {
                    SelectItem relationship = relationships.get(0).getAsSelectItem();
                    relationship.setName((String) modelFieldValueMap.get(fieldID));
                    relationship.setDescription((String) modelFieldValueMap.get(fieldID));
                    ArrayList<SelectItem> selectedRelationships = new ArrayList<>();
                    selectedRelationships.add(relationship);
                    contactListItem.setSelectedRelationships(selectedRelationships);
                }
            } else if (CustomFormConstants.REPORTS_TO.equals(fieldID)) {
                contactListItem.setReportsToId((Integer) modelFieldValueMap.get(fieldID));
                if (contactListItem.getReportsToId() != null) {
                    EdsCrmContact edsCrmContact = crmContactManager.get(contactListItem.getReportsToId());
                    if (edsCrmContact != null) {
                        contactListItem.setReportsTo(edsCrmContact.getName());
                    }
                }

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
            } else if (CustomFormConstants.NOTES.equals(fieldID) || CustomFormConstants.CRM_NOTE.equals(fieldID)) {
                if (StringUtils.isNotBlank((String) modelFieldValueMap.get(fieldID))) {
                    note = new HistoryListItem((String) modelFieldValueMap.get(fieldID));
                }
            }
        }

        contactListItem.setCrmAccount(account);

        Integer contactId;
        try {
            contactId = contactServiceLocal.saveContact(contactListItem, null, true);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (note != null) {
            try {
                note.setRelatedToId(EdsNoteHistory.CRM_CONTACT);
                note.setRelatedId(contactId);
                noteServiceLocal.saveNote(note);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return successResponse(new ResponseData());

    }


    @Operation(summary = "Get Contact Information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have contact information"),
            @ApiResponse(responseCode = "400", description = "item_id is required")})
    @RequestMapping(value = "/contacts/{entity_id}/information", method = RequestMethod.GET)
    public Object getContactInformation(@PathVariable(value = "entity_id") Integer entity_id) throws RestException {
        if (entity_id == null || entity_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "entity_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsCrmContact edsCrmContact = crmContactManager.get(entity_id);
        if (edsCrmContact == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Contact Information with id " + entity_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        EntityInformationTO contactInformaton = new EntityInformationTO();
        contactInformaton.setFirst_name(edsCrmContact.getFirstName());
        contactInformaton.setLast_name(edsCrmContact.getLastName());
        if (edsCrmContact.getPhoto() != null) {
            contactInformaton.setAvatar_url(commonServiceLocal.getImageUrl(edsCrmContact.getPhoto().getObjectID()));
        }
        if (StringUtils.isNotBlank(edsCrmContact.getPrimaryPhone())) {
            contactInformaton.setPhone_number(edsCrmContact.getPrimaryPhone());
        }
        if (StringUtils.isNotBlank(edsCrmContact.getPrimaryEmail())) {
            contactInformaton.setEmail(edsCrmContact.getPrimaryEmail());
        }
        if (edsCrmContact.getCrmAccount() != null) {
            contactInformaton.setCompany(new CategoryTO(edsCrmContact.getCrmAccount().getObjectID(), edsCrmContact.getCrmAccount().getName()));
        }
        if (edsCrmContact.getReportsToId() != null) {
            contactInformaton.setSupervisor(new CategoryTO(edsCrmContact.getReportsToId(), edsCrmContact.getReportsTo()));
        }
        return successResponse(new EntityInformationResultTO(contactInformaton));
    }


    @Operation(summary = "Update contact information", description = "It's multipart request.It will have part called avatar for Contact avatar image, if user will set any.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/contacts/{entity_id}/information", method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_CONTACTS_LIST, PermissionConstants.CRM_EDIT_CONTACT})
    public Object updateContact(MultipartRequest multipartRequest,
                                @PathVariable(value = "entity_id") Integer item_id,
                                @RequestParam(name = "body") String jsonString) throws RestException {

        ContactEditTO contactEditTO;

        try {
            contactEditTO = mapper.readValue(jsonString, ContactEditTO.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong.", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ContactListItem item = contactServiceLocal.getContact(item_id, true);
        item.setCheckForDuplicates(true);
        item.setObjectId(item_id);

        if (StringUtils.isNotBlank(contactEditTO.getFirst_name())) {
            item.setFirstName(contactEditTO.getFirst_name());
        }
        if (StringUtils.isNotBlank(contactEditTO.getLast_name())) {
            item.setLastName(contactEditTO.getLast_name());
        }

        if (contactEditTO.getCompany() != null && contactEditTO.getCompany() > 0) {
            EdsCrmAccount edsCrmAccount = (EdsCrmAccount) crmAccountManager.get(EdsCrmAccount.class, contactEditTO.getCompany());
            if (edsCrmAccount != null) {
                CrmAccountItem crmAccount = new CrmAccountItem();
                crmAccount.setObjectId(edsCrmAccount.getObjectID());
                crmAccount.setName(edsCrmAccount.getName());
                item.setCrmAccount(crmAccount);
            }
        }
        if (contactEditTO.getSupervisor() != null && contactEditTO.getSupervisor() > 0) {
            EdsCrmContact edsCrmContact = (EdsCrmContact) crmContactManager.get(EdsCrmContact.class, contactEditTO.getSupervisor());
            if (edsCrmContact != null) {
                item.setReportsToId(edsCrmContact.getObjectID());
                item.setReportsTo(edsCrmContact.getName());
            }
        }

        if (StringUtils.isNotBlank(contactEditTO.getEmail())) {
            if (!EMAIL_PATTERN.matcher(contactEditTO.getEmail()).matches()) {
                throw new RestException("Invalid email address", "Invalid email address", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            String primaryEmail = null;
            if (StringUtils.isNotBlank(item.getPrimaryEmail())) {
                primaryEmail = item.getPrimaryEmail();
            }
            HashMap<Integer, ArrayList<String>> emailParams;
            if (StringUtils.isBlank(primaryEmail)) {//if there is no contact primary email, set api provided email as WORK email
                emailParams = new HashMap<>();
                ArrayList<String> emails = new ArrayList<>();
                emails.add(contactEditTO.getEmail());
                emailParams.put(G_WORK, emails);
                item.setEmails(emailParams);
            } else {
                //this contact has primary email, find the primary email type (WORK,HOME,OTHER) and update the email to api provided email
                // employee old primary email will be updated
                emailParams = ContactListItem.getItemParamsAsMap(item, Constants.CONTACT_EMAILS);

                for (HashMap.Entry<Integer, ArrayList<String>> entry : emailParams.entrySet()) {
                    if (entry != null) {
                        switch (entry.getKey()) {
                            case Constants.G_HOME -> {
                                ArrayList<String> homeEmails = entry.getValue();
                                for (int i = 0; i < homeEmails.size(); i++) {
                                    if (homeEmails.get(i).equals(primaryEmail)) {
                                        homeEmails.set(i, contactEditTO.getEmail());
                                        break;
                                    }
                                }
                                emailParams.put(Constants.G_HOME, homeEmails);
                            }
                            case G_WORK -> {
                                ArrayList<String> workEmails = entry.getValue();
                                for (int i = 0; i < workEmails.size(); i++) {
                                    if (workEmails.get(i).equals(primaryEmail)) {
                                        workEmails.set(i, contactEditTO.getEmail());
                                        break;
                                    }
                                }
                                emailParams.put(G_WORK, workEmails);
                            }
                            case Constants.G_OTHER -> {
                                ArrayList<String> otherEmails = entry.getValue();
                                for (int i = 0; i < otherEmails.size(); i++) {
                                    if (otherEmails.get(i).equals(primaryEmail)) {
                                        otherEmails.set(i, contactEditTO.getEmail());
                                        break;
                                    }
                                }
                                emailParams.put(Constants.G_OTHER, otherEmails);
                            }
                        }
                    }
                }
            }

            item.setPrimaryEmail(contactEditTO.getEmail());
            item.setEmails(emailParams);
        }

        if (StringUtils.isNotBlank(contactEditTO.getPhone_number())) {

            HashMap<Integer, ArrayList<String>> phoneParams;

            String primaryPhone = null;
            if (StringUtils.isNotBlank(item.getPrimaryPhone())) {
                primaryPhone = item.getPrimaryPhone();
            }
            if (primaryPhone == null) {//if there is no contact primary phone, set api provided phone as MOBILE email
                phoneParams = new HashMap<>();
                ArrayList<String> phonesPhones = new ArrayList<>();
                phonesPhones.add(contactEditTO.getPhone_number());
                phoneParams.put(G_WORK, phonesPhones);
                item.setPhones(phoneParams);
            } else {
                phoneParams = ContactListItem.getItemParamsAsMap(item, Constants.CONTACT_PHONES);
                //If contact has primary phone, find the primary phone type (WORK,WORK,MOBILE) and update the phone to api provided phone
                // contact old primary email will be update
                for (HashMap.Entry<Integer, ArrayList<String>> entry : phoneParams.entrySet()) {
                    if (entry != null) {
                        switch (entry.getKey()) {
                            case Constants.G_HOME -> {
                                ArrayList<String> homePhones = entry.getValue();
                                for (int i = 0; i < homePhones.size(); i++) {
                                    if (homePhones.get(i).equals(primaryPhone)) {
                                        homePhones.set(i, contactEditTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_HOME, homePhones);
                            }
                            case Constants.G_WORK -> {
                                ArrayList<String> workPhones = entry.getValue();
                                for (int i = 0; i < workPhones.size(); i++) {
                                    if (workPhones.get(i).equals(primaryPhone)) {
                                        workPhones.set(i, contactEditTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_WORK, workPhones);
                            }
                            case Constants.G_MOBILE -> {
                                ArrayList<String> mobilePhones = entry.getValue();
                                for (int i = 0; i < mobilePhones.size(); i++) {
                                    if (mobilePhones.get(i).equals(primaryPhone)) {
                                        mobilePhones.set(i, contactEditTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_MOBILE, mobilePhones);
                            }
                            case Constants.G_HOME_FAX -> {
                                ArrayList<String> homeFax = entry.getValue();
                                for (int i = 0; i < homeFax.size(); i++) {
                                    if (homeFax.get(i).equals(primaryPhone)) {
                                        homeFax.set(i, contactEditTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_HOME_FAX, homeFax);
                            }
                            case Constants.G_WORK_FAX -> {
                                ArrayList<String> workFax = entry.getValue();
                                for (int i = 0; i < workFax.size(); i++) {
                                    if (workFax.get(i).equals(primaryPhone)) {
                                        workFax.set(i, contactEditTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_WORK_FAX, workFax);
                            }
                            case Constants.G_PAGER -> {
                                ArrayList<String> pagers = entry.getValue();
                                for (int i = 0; i < pagers.size(); i++) {
                                    if (pagers.get(i).equals(primaryPhone)) {
                                        pagers.set(i, contactEditTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_PAGER, pagers);
                            }
                            case Constants.G_OTHER -> {
                                ArrayList<String> others = entry.getValue();
                                for (int i = 0; i < others.size(); i++) {
                                    if (others.get(i).equals(primaryPhone)) {
                                        others.set(i, contactEditTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_OTHER, others);
                            }
                            case Constants.G_EXTENSION -> {
                                ArrayList<String> extentions = entry.getValue();
                                for (int i = 0; i < extentions.size(); i++) {
                                    if (extentions.get(i).equals(primaryPhone)) {
                                        extentions.set(i, contactEditTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_EXTENSION, extentions);
                            }
                            case Constants.G_FAX -> {
                                ArrayList<String> faxes = entry.getValue();
                                for (int i = 0; i < faxes.size(); i++) {
                                    if (faxes.get(i).equals(primaryPhone)) {
                                        faxes.set(i, contactEditTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_FAX, faxes);
                            }
                            case Constants.G_WHATS_APP -> {
                                ArrayList<String> whatsApps = entry.getValue();
                                for (int i = 0; i < whatsApps.size(); i++) {
                                    if (whatsApps.get(i).equals(primaryPhone)) {
                                        whatsApps.set(i, contactEditTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_WHATS_APP, whatsApps);
                            }
                            case Constants.G_TELEGRAM -> {
                                ArrayList<String> telegram = entry.getValue();
                                for (int i = 0; i < telegram.size(); i++) {
                                    if (telegram.get(i).equals(primaryPhone)) {
                                        telegram.set(i, contactEditTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_TELEGRAM, telegram);
                            }
                            case Constants.G_VIBER -> {
                                ArrayList<String> vibers = entry.getValue();
                                for (int i = 0; i < vibers.size(); i++) {
                                    if (vibers.get(i).equals(primaryPhone)) {
                                        vibers.set(i, contactEditTO.getPhone_number());
                                        break;
                                    }
                                }
                                phoneParams.put(Constants.G_VIBER, vibers);
                            }
                        }
                    }
                }
            }

            item.setPrimaryPhone(contactEditTO.getPhone_number());
            item.setPhones(phoneParams);
        }

        Integer contactId;
        try {
            contactId = contactServiceLocal.saveContact(item, null, true);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (contactId == -2) {
            throw new RestException("Contact email ".concat(contactEditTO.getEmail()).concat(" is already exist"), "Contact email ".concat(contactEditTO.getEmail()).concat(" is already exist"), CONFLICT, HttpStatus.CONFLICT);
        }
        if (contactId == -1) {
            throw new RestException("Contact name ".concat(contactEditTO.getFirst_name()).concat(" is already exist"), "Contact name ".concat(contactEditTO.getFirst_name()).concat(" is already exist"), CONFLICT, HttpStatus.CONFLICT);
        }

        if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
            for (MultipartFile file : multipartRequest.getFileMap().values()) {
                if (file.getName().equals("avatar")) {//Find contact avatar image
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
                            commonServiceLocal.saveCrmContactImageUrl(Integer.valueOf(result[0]), contactId);
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

    @Transactional
    @Operation(summary = "Create Customer Contact", description = "Request to create new contact")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Creates new contact with provided credentials")})
    @RequestMapping(value = "/contacts/zapier_create", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_CONTACTS_LIST, PermissionConstants.CRM_ADD_NEW_CONTACT})
    public Object createNewContact(@RequestBody String addContactTOStr) throws RestException {

        AddZapierContactTO addContactTO;

        try {
            String pattern = "\"addresses\":\"\"";
            addContactTOStr = addContactTOStr.replace(", " + pattern, "").replace("," + pattern, "").replace(pattern, "");
            log.info("JSON: {}", addContactTOStr);
            addContactTO = mapper.readValue(addContactTOStr, AddZapierContactTO.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong.", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        /*return createNewContact(null, jsonString);*/

        if (StringUtils.isBlank(addContactTO.getFirst_name())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "first_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(addContactTO.getLast_name())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "last_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ContactListItem contact = new ContactListItem();
        contact.setContactType(CrmConstants.TYPE_CRM_CONTACT);
//        contact.setCheckForDuplicates(true);
        contact.setFirstName(addContactTO.getFirst_name());
        contact.setLastName(addContactTO.getLast_name());

        if (StringUtils.isNotBlank(addContactTO.getPhone_number())) {
            contact.setPrimaryPhone(addContactTO.getPhone_number());
            HashMap<Integer, ArrayList<String>> phoneParam = new HashMap<>();
            ArrayList<String> phones = new ArrayList<>();
            phones.add(addContactTO.getPhone_number());
            phoneParam.put(G_WORK, phones);
            contact.setPhones(phoneParam);
        }

        if (StringUtils.isNotBlank(addContactTO.getEmail())) {
            if (!EMAIL_PATTERN.matcher(addContactTO.getEmail()).matches()) {
                throw new RestException("Invalid email address", "Invalid email address", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            contact.setPrimaryEmail(addContactTO.getEmail());
            HashMap<Integer, ArrayList<String>> emailParam = new HashMap<>();
            ArrayList<String> emails = new ArrayList<>();
            emails.add(addContactTO.getEmail());
            emailParam.put(G_WORK, emails);
            contact.setEmails(emailParam);
        }

        CrmAccountItem account = new CrmAccountItem();
        if (StringUtils.isNotBlank(addContactTO.getCompany_name())) {
            account.setName(addContactTO.getCompany_name());
        } else {
            account.setName(addContactTO.getFirst_name() + " " + addContactTO.getLast_name());
        }
        ArrayList<SelectItem> accountTypes = new ArrayList<>();
        EdsReference customerType = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER);
        if (customerType != null) {
            SelectItem customer = customerType.getAsSelectItem();
            customer.setSelected(true);
            accountTypes.add(customer);
        }
        account.setAccountTypes(accountTypes.toArray(new SelectItem[0]));

        if (addContactTO.getAddresses() != null) {
            for (ContactAddressAddTO addressTO : addContactTO.getAddresses()) {
                Address address = new Address();
                address.setName(addressTO.getName());
                address.setAddress(addressTO.getLine_1());
                address.setAddressb(addressTO.getLine_2());
                address.setCity(addressTO.getCity());
                if (addressTO.getCountry() != null) {
                    address.setCountry(addressTO.getCountry().getTitle());
                    address.setCountryId(addressTO.getCountry().getId());
                } else if (StringUtils.isNotBlank(addressTO.getCountry_code())) {
                    EdsCountry country = countryManager.getCountryByCode(addressTO.getCountry_code());
                    if (country != null) {
                        address.setCountryId(country.getObjectID());
                        address.setCountry(country.getName());
                    }
                }
                if (addressTO.getState() != null) {
                    address.setState(addressTO.getState().getTitle());
                    address.setStateId(addressTO.getState().getId());
                } else if (address.getCountryId() != null && StringUtils.isNotBlank(addressTO.getState_code())) {
                    EdsRegion state = regionManager.getRegion(address.getCountryId(), addressTO.getState_code());
                    if (state != null) {
                        address.setStateId(state.getObjectID());
                    }
                }
                address.setZipCode(addressTO.getPost_code());
                address.setPrimary(true);

                address.setEntityType(EdsAddress.ENTITY_TYPE_CONTACT);
                if (ContactParamEnum.HOME.getCode().equals(addressTO.getType())) {
                    address.setRelationType(EdsAddress.HOME);
                } else if (ContactParamEnum.WORK.getCode().equals(addressTO.getType())) {
                    address.setRelationType(EdsAddress.WORK);
                } else if (ContactParamEnum.OTHER.getCode().equals(addressTO.getType())) {
                    address.setRelationType(EdsAddress.OTHER);
                }
                address.setPrimary(addressTO.getIs_primary() != null ? addressTO.getIs_primary() : false);

                contact.getAddresses().add(address);
            }
        }

        contact.setCrmAccount(account);

        Integer contactId;
        try {
            contactId = contactServiceLocal.saveContact(contact, null, false);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (contactId == -2) {
            throw new RestException("Contact email ".concat(addContactTO.getEmail()).concat(" is already exist"), "Contact email ".concat(addContactTO.getEmail()).concat(" is already exist"), CONFLICT, HttpStatus.CONFLICT);
        }
        if (contactId == -1) {
            throw new RestException("Contact name ".concat(addContactTO.getFirst_name()).concat(" is already exist"), "Contact name ".concat(addContactTO.getFirst_name()).concat(" is already exist"), CONFLICT, HttpStatus.CONFLICT);
        }

        if (StringUtils.isNotBlank(addContactTO.getNote())) {
            try {
                HistoryListItem note = new HistoryListItem();
                note.setRelatedToId(EdsNoteHistory.CRM_CONTACT);
                note.setRelatedId(contactId);
                noteServiceLocal.saveNote(note);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return successResponse(new ContactTO(contactId));

    }

    @Operation(summary = "Create Contact", description = "Request to create new contact")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Creates new contact with provided credentials")})
    @RequestMapping(value = "/contacts/create", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object createNewContact(MultipartRequest multipartRequest, @Parameter(name = "body", description = """
            <pre>
            {
              "first_name": "Firstname",
              "last_name": "Lastname",
              "phone_number": "phonenumber",
              "email": "john.smith@kpi.com",
              "job_title": "developer",
              "title_id": "0",
              "company": 0,
              "phoneNumbers":[
              {
              "phoneCategory":"WORK",
              "number":"+998",
              "primary": true
              }
              ]
              "company_name": "companyName",
              "note": "Note goes here",
              "is_customer": true,
              "supervisor": 0,
              "contact_addresses": [
                {
                  "line_1": "Address Line 1",
                  "line_2": "Address Line 2",
                  "city": "City",
                  "post_code": "postcode/zipcode",
                  "is_primary": true,
                  "state_code": "AL",
                  "country_code": "US",
                  "type": "one of HOME/WORK/OTHER"
                }
              ],
              "custom_fields": [
                {
                  "id": 1,
                  "text": "string field value"
                },
                {
                  "id": 2,
                  "value": 1
                },
                {
                  "id": 3,
                  "choosed_ids": [ 1, 2, 3, 4 ]
                }
              ],
              "name_unique": false
            }
            
            </pre>""", schema = @Schema(type = "string")) @RequestParam(name = "body") String jsonString) throws RestException {

        AddContactTO newContact;
        ObjectMapper mapper = new ObjectMapper();
        try {
            newContact = mapper.readValue(jsonString, AddContactTO.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong.", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(newContact.getFirst_name())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "first_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
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
        for (Object customFieldObject : newContact.getCustom_fields()) {
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
                newContact.getCustom_fields().add(customFieldsMap);
            }
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        ContactListItem contact = new ContactListItem();
        contact.setContactType(CrmConstants.TYPE_CRM_CONTACT);
        contact.setCheckForDuplicates(true);

        contact.setFirstName(newContact.getFirst_name());
        contact.setTitleId(newContact.getTitle_id());
        contact.setLastName(newContact.getLast_name() != null ? newContact.getLast_name() : "");
        contact.setBirthDate(new DateNonConvertable(newContact.getDate_of_birth()));
        contact.setJobTitle(newContact.getJob_title() != null ? newContact.getJob_title() : "");

        if (StringUtils.isNotBlank(newContact.getPhone_number())) {
            contact.setPrimaryPhone(newContact.getPhone_number());
            HashMap<Integer, ArrayList<String>> phoneParam = new HashMap<>();
            ArrayList<String> phones = new ArrayList<>();
            phones.add(newContact.getPhone_number());
            phoneParam.put(Constants.G_WORK, phones);
            contact.setPhones(phoneParam);
        }

        if (newContact.getPhoneNumbers() != null) {
            HashMap<Integer, ArrayList<String>> phoneParam = new HashMap<>();
            for (PhoneDto phoneDto : newContact.getPhoneNumbers()) {
                phoneParam.put(getPhoneType(phoneDto.getPhoneCategory()), new ArrayList<>(Collections.singleton(phoneDto.getNumber())));
                if (phoneDto.isPrimary()) {
                    contact.setPrimaryPhone(phoneDto.getNumber());
                }
            }
            contact.setPhones(phoneParam);
        }

        if (StringUtils.isNotBlank(newContact.getEmail())) {
            if (!EMAIL_PATTERN.matcher(newContact.getEmail()).matches()) {
                throw new RestException("Invalid email address", "Invalid email address", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            contact.setPrimaryEmail(newContact.getEmail());
            HashMap<Integer, ArrayList<String>> emailParam = new HashMap<>();
            ArrayList<String> emails = new ArrayList<>();
            emails.add(newContact.getEmail());
            emailParam.put(Constants.G_WORK, emails);
            contact.setEmails(emailParam);
        }

        if (newContact.getEmails() != null) {
            HashMap<Integer, ArrayList<String>> emailsParam = new HashMap<>();
            ArrayList<String> emailsParam1 = new ArrayList<>();
            for (EmailDto emailDto : newContact.getEmails()) {
                emailsParam.put(G_WORK, new ArrayList<>(Collections.singleton(emailDto.getEmail())));
                emailsParam1.add(emailDto.getEmail());
                if (emailDto.isPrimary()) {
                    contact.setPrimaryEmail(emailDto.getEmail());
                }
            }

            contact.setWorkEmail(emailsParam1);
        }
        CrmAccountItem account = new CrmAccountItem();
        if (newContact.getCompany() != null && newContact.getCompany() > 0) {
            EdsCrmAccount edsCrmAccount = (EdsCrmAccount) crmAccountManager.get(EdsCrmAccount.class, newContact.getCompany());
            if (edsCrmAccount != null) {
                account.setObjectId(edsCrmAccount.getObjectID());
                account.setName(edsCrmAccount.getName());
            }
        } else if (StringUtils.isNotBlank(newContact.getCompany_name())) {
            account.setName(newContact.getCompany_name());
        }
        if (newContact.getSupervisor() != null && newContact.getSupervisor() > 0) {
            EdsCrmContact edsCrmContact = (EdsCrmContact) crmContactManager.get(EdsCrmContact.class, newContact.getSupervisor());
            if (edsCrmContact != null) {
                contact.setReportsToId(edsCrmContact.getObjectID());
                contact.setReportsTo(edsCrmContact.getName());
            }
        }

        if (newContact.getContact_addresses() != null && newContact.getContact_addresses().size() > 0) {
            for (ContactAddressAddTO addressTO : newContact.getContact_addresses()) {
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

                contact.getAddresses().add(address);
            }
        }

        EdsUser user = userFilterManager.getUser();
        Integer attachmentModelFieldId = null;
        //Find contact avatar
        MultipartFile contactAvatarMultipartFile = null;
        //Map key is model field id that related to attachment, and value is attachment
        LinkedHashMap<Integer, ArrayList<MultipartFile>> attachmentsMap = new LinkedHashMap<>();
        ArrayList<MultipartFile> contactAttachments = new ArrayList<>();
        if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
            for (MultipartFile file : multipartRequest.getFileMap().values()) {
                if (file.getName().equals("avatar")) {//Find contact avatar image
                    contactAvatarMultipartFile = file;
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
            modelFields = modelFieldManager.getFields(FORM_TYPES.get(EntityTypeEnum.CONTACTS.name().toLowerCase()));
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
        if (newContact.getCustom_fields() != null && !newContact.getCustom_fields().isEmpty()) {
            for (Object customFieldObject : newContact.getCustom_fields()) {
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
                contact.setCustomFields(customFieldItems);
            }
        }

        HistoryListItem note = null;

        for (String fieldID : modelFieldValueMap.keySet()) {
            if (CustomFormConstants.TITLE.equals(fieldID)) {
                contact.setTitle((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.MIDDLE_NAME.equals(fieldID)) {
                contact.setMiddleName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.FIRST_NAME.equals(fieldID)) {
                contact.setFirstName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.OTHER_NAME.equals(fieldID)) {
                contact.setOtherName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.LAST_NAME.equals(fieldID)) {
                contact.setLastName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.BIRTH_DAY.equals(fieldID)) {
                try {
                    contact.setBirthDate(new DateNonConvertable(longDateTimezoneFormat.parse((String) modelFieldValueMap.get(fieldID))));
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
                contact.setJobTitle((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_ORGANIZATION_TYPE.equals(fieldID)) {
                account.setOrganizationTypeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.DEPARTMENT.equals(fieldID)) {
                contact.setDepartment((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_ANNUAL_REVENUE.equals(fieldID)) {
                account.setAnnualRevenueID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.REF_IND_NUMBER.equals(fieldID)) {
                contact.setRefIndNumber((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.ASSETS.equals(fieldID)) {
                contact.setAssets((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_INDUSTRY.equals(fieldID)) {
                account.setIndustryID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_OWNERSHIP.equals(fieldID)) {
                account.setOwnershipId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_ACCOUNT_NUMBER_OF_EMPLOYEE.equals(fieldID)) {
                account.setNumberOfEmployeeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.LEAD_OWNER.equals(fieldID)) {
                contact.setOwnerId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.LEAD_NAME.equals(fieldID)) {
                contact.setFirstName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CATEGORY.equals(fieldID)) {
                ArrayList<SelectItem> selectedCategories = new ArrayList<>();
                ArrayList<Integer> categoryIDs = (ArrayList<Integer>) modelFieldValueMap.get(fieldID);
                for (Integer categoryId : categoryIDs) {
                    selectedCategories.add(new SelectItem(categoryId));
                }
                contact.setSelectedCategories(selectedCategories);
            } else if (CustomFormConstants.RELATIONSHIP.equals(fieldID)) {
                List<EdsReference> relationships = referenceManager.listReferences(EdsCrmContact.CONTACT_RELATION);
                if (relationships != null && relationships.size() > 0) {
                    SelectItem relationship = relationships.get(0).getAsSelectItem();
                    relationship.setName((String) modelFieldValueMap.get(fieldID));
                    relationship.setDescription((String) modelFieldValueMap.get(fieldID));
                    ArrayList<SelectItem> selectedRelationships = new ArrayList<>();
                    selectedRelationships.add(relationship);
                    contact.setSelectedRelationships(selectedRelationships);
                }
            } else if (CustomFormConstants.REPORTS_TO.equals(fieldID)) {
                contact.setReportsToId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.OWNER.equals(fieldID)) {
                contact.setOwnerId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_CAMPAIGN_NAME.equals(fieldID)) {
                contact.setCampaignId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.EMAIL_OPT_OUT.equals(fieldID)) {
                contact.setEmailOptOut((Boolean) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.ASSIGNEE.equals(fieldID)) {
                contact.setLeadAssigneeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.BACKUP_ASSIGNEE.equals(fieldID)) {
                contact.setLeadBackupAssigneeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.LEAD_SOURCE.equals(fieldID)) {
                contact.setLeadSourceID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.STATUS.equals(fieldID)) {
                contact.setLeadStatusID((Integer) modelFieldValueMap.get(fieldID));
                contact.setLeadStatus(new SelectItem((Integer) modelFieldValueMap.get(fieldID)));
            } else if (CustomFormConstants.RATING.equals(fieldID)) {
                contact.setLeadRatingID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.IM_ADDRESS.equals(fieldID)) {
                HashMap<Integer, ArrayList<String>> paramMap = new HashMap<>();
                ArrayList<String> gtalk = new ArrayList<>();
                gtalk.add((String) modelFieldValueMap.get(fieldID));
                paramMap.put(ContactParamEnum.GOOGLE_TALK.getId(), gtalk);
                contact.setImAddresses(paramMap);
            } else if (CustomFormConstants.WEB_ADDRESS.equals(fieldID)) {
                HashMap<Integer, ArrayList<String>> paramMap = new HashMap<>();
                ArrayList<String> workAddress = new ArrayList<>();
                workAddress.add((String) modelFieldValueMap.get(fieldID));
                paramMap.put(ContactParamEnum.WORK.getId(), workAddress);
                contact.setWebAddresses(paramMap);
            } else if (CustomFormConstants.NOTES.equals(fieldID) || CustomFormConstants.CRM_NOTE.equals(fieldID)) {
                if (StringUtils.isNotBlank((String) modelFieldValueMap.get(fieldID))) {
                    note = new HistoryListItem((String) modelFieldValueMap.get(fieldID));
                }
            } else if (CustomFormConstants.ATTACHMENTS.equals(fieldID)) {
                if (attachmentModelFieldId != null && attachmentsMap.get(attachmentModelFieldId) != null) {
                    contactAttachments.addAll(attachmentsMap.get(attachmentModelFieldId));
                }
            }
        }

        contact.setCrmAccount(account);

        Integer contactId;
        try {
            contact.setNameNotUnique(newContact.isName_unique());
            contactId = contactServiceLocal.saveContact(contact, null, false);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (contactId == -2) {
            throw new RestException("Contact email ".concat(newContact.getEmail()).concat(" is already exist"), "Contact email ".concat(newContact.getEmail()).concat(" is already exist"), CONFLICT, HttpStatus.CONFLICT);
        }
        if (contactId == -1) {
            throw new RestException("Contact name ".concat(newContact.getFirst_name()).concat(" is already exist"), "Contact name ".concat(newContact.getFirst_name()).concat(" is already exist"), CONFLICT, HttpStatus.CONFLICT);
        }
        if (contactId == -3) {
            throw new RestException("Contact phone number is already exist", "Contact phone number is already exist", CONFLICT, HttpStatus.CONFLICT);
        }

        if (note != null) {
            try {
                note.setRelatedToId(EdsNoteHistory.CRM_CONTACT);
                note.setRelatedId(contactId);
                noteServiceLocal.saveNote(note);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        if (contactAvatarMultipartFile != null) {
            CreateDocumentCommand documentCommand = new CreateDocumentCommand();
            documentCommand.setImgType(contactAvatarMultipartFile.getOriginalFilename().substring(contactAvatarMultipartFile.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase(Locale.ENGLISH));
            documentCommand.setCompanyID(user.getCompany().getObjectID());
            documentCommand.setFolderName("static");
            documentCommand.setNotdownloadable("YES");
            WfmMultipartFile multipartFile = new WfmMultipartFile("", contactAvatarMultipartFile);
            documentCommand.addFile(multipartFile);
            try {

//                CreateAttachmentHandler createAttachmentHandler = (CreateAttachmentHandler) ApplicationContextProvider.applicationContext.getBean("createAttachmentHandler");
//                createAttachmentHandler.execute(documentCommand);
//                String[] result = createAttachmentHandler.getResult();

                String[] attachmentHandler = wfmCommandServiceLocal.createAttachmentHandler(documentCommand);
                if (attachmentHandler != null && attachmentHandler.length > 0) {
                    commonServiceLocal.saveCrmContactImageUrl(Integer.valueOf(attachmentHandler[0]), contactId);
                }
            } catch (Throwable throwable) {
                log.error(throwable.getMessage());
            }
        }

        if (contactAttachments.size() > 0) {
            FolderResource folderResource = documentsServiceLocal.getFolderResource(Constants.F_CRM_CONTACT, contactId);
            for (MultipartFile multipartFile : contactAttachments) {
                try {
                    documentsServiceLocal.saveDocumentFile(multipartFile, folderResource.getObjectId(), folderResource.getFileType(), contactId, null);
                } catch (Exception e) {
                    log.error("", e);
                    throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }

        if (newContact.getCompany() == null && newContact.getCompany_name() != null && contact.getCrmAccount() != null) {
            return successResponse(new ContactTO(contactId, contact.getCrmAccount().getObjectId()));
        }
        return successResponse(new ContactTO(contactId));

    }

    private int getPhoneType(String type) {
        return switch (type) {
            case "WORK" -> G_WORK;
            case "HOME" -> Constants.G_HOME;
            case "FAX" -> Constants.G_FAX;
            case "MOBILE" -> Constants.G_MOBILE;
            case "WHATSAPP" -> Constants.G_WHATS_APP;
            case "TELEGRAM" -> Constants.G_TELEGRAM;
            case "VIBER" -> Constants.G_VIBER;
            default -> 0;
        };
    }

    private FacetFilterRpc initializeDefaultContactFacetFilter() {
        FacetFilterRpc mainMergedFilter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.ContactListPanel).getFacetFilter();
        //Custom fields which are facetable
        ArrayList<CompanyCustomFieldItem> contactCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.Contact);

        if (contactCustomFields != null && !contactCustomFields.isEmpty()) {
            contactCustomFields.forEach(companyCustomFieldItem -> {
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
        facetFilterToSave.setType(ListPanelType.ContactsQuickFilterForMobile);

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
        return commonServiceLocal.saveFacetFilter(facetFilterToSave, ListPanelType.ContactsQuickFilterForMobile);
    }

    private FacetFilterRpc getOneTimeFilter(FacetFilterRpc defaultFilter) {

        if (defaultFilter != null) {
            defaultFilter.setType(ListPanelType.ContactListPanelOTF);
        }
        if (defaultFilter != null) {
            defaultFilter.setUserID(userManager.getUser().getObjectID());
        }

        FacetFilterRpc otf = commonServiceLocal.getUserFacetFilter(defaultFilter);
        otf.setName("OTF");
        otf.setDefaultFilter(true);
        otf.setType(ListPanelType.ContactListPanelOTF);

        if (otf.getObjectID() != null) {
            EdsUserFilter edsUserFilter = userFilterManager.getByFacetFilterId(otf.getObjectID());
            if (edsUserFilter != null) {
                otf.setFavourFilter(Boolean.TRUE.equals(edsUserFilter.getFavour()));
            }
        }
        return otf;
    }
}
