package com.edatasite.workforce.rest.v2.release10.settings;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsReferenceColor;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsFacetFilter;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsUserFilter;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.SaveFilterSelectItems;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCrmAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrLeaveRequestConst;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrOpportunityRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.FacetFilterManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.UserFilterManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ListingFilterHelper;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.filters.BaseFilterTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.filters.DatePeriodTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.filters.FilterCategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.filters.FilterTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.filters.FiltersListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.filters.SubCategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ApplyFilterRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.FilteredStatusItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.FilteredStatusesListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.FilteredStatusesRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.PeopleTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.QuickFiltersTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.SavedFilterTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.ColorTO;
import com.edatasite.workforce.rest.v2.release10.enums.PeopleTypeEnum;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Dilshod Madrahimov on 11/28/2017.
 */

@Tag(name = "Filters", description = "Filters API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiFiltersControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiFiltersControllerV2.class);
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private RbacService rbacService;
    @Autowired
    private FacetFilterManager facetFilterManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private UserFilterManager userFilterManager;
    @Autowired
    private HttpServletRequest servletRequest;

    @Operation(summary = "Quick Filters", description = """
            Retrieves data on quick filters.

            Quick filters are of two types:\s

            PEOPLE - by people, all employees are displayed on which there are leads in all statuses (which are listed as "Assigned to").

            CATEGORIES - by category, these are filters created by the user.""")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of filtered people or category")})
    @RequestMapping(value = "/{entity_type}/quick_filter", method = RequestMethod.GET)
    public Object quickFilter(@PathVariable("entity_type") String entityType) throws RestException {

        String assignedtofacetkey;
        FacetSolrField assignedfield;
        FacetFilterRpc defaultFilter;
//        HashMap<Integer, String> activeFilters;
        ListPanelType type;
        ListPanelType quickFilterType;
        ListPanelType otfType;

        if ("leads".equalsIgnoreCase(entityType)) {
            assignedtofacetkey = FacetContentType.LeadFacetFilter.getContentCode()[5];
            assignedfield = new FacetSolrField(SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID, SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID_NAME);
            //Initialize Facet Filter
            defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, initializeDefaultFacetFilter(ListPanelType.LeadListPanel, ViewName.Lead));
//            activeFilters = getActiveFilters(ListPanelType.FilteredStatusesForMobile);
            type = ListPanelType.LeadListPanel;
            quickFilterType = ListPanelType.FilteredStatusesForMobile;
            otfType = ListPanelType.LeadListPanelOTF;
        } else if ("contacts".equalsIgnoreCase(entityType)) {
            assignedtofacetkey = FacetContentType.ContactFacetFilter.getContentCode()[4];
            assignedfield = new FacetSolrField(SolrContactRepresenter.FIELD_OWNER_ID, SolrContactRepresenter.FIELD_OWNER_ID_NAME);
            //Initialize Facet Filter
            defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_CONTACT, initializeDefaultFacetFilter(ListPanelType.ContactListPanel, ViewName.Contact));
//            activeFilters = getActiveFilters(ListPanelType.FilteredStatusesForMobile);
            type = ListPanelType.ContactListPanel;
            quickFilterType = ListPanelType.ContactsQuickFilterForMobile;
            otfType = ListPanelType.ContactListPanelOTF;
        } else if ("tasks".equalsIgnoreCase(entityType)) {

            assignedtofacetkey = FacetContentType.TaskFacetFilter.getContentCode()[5];
            assignedfield = new FacetSolrField(SolrTaskRepresenter.FIELD_USER_ID, SolrTaskRepresenter.FIELD_USER_ID_NAME);
            //Initialize Facet Filter
            defaultFilter = rbacService.getTaskFacetFilterData(initializeDefaultFacetFilter(ListPanelType.TaskListPanel, ViewName.Task), true);
//            activeFilters = getActiveFilters(ListPanelType.TasksQuickFilterForMobile);
            type = ListPanelType.TaskListPanel;
            quickFilterType = ListPanelType.TasksQuickFilterForMobile;
            otfType = ListPanelType.TaskListPanelOTF;
        } else if ("opportunities".equalsIgnoreCase(entityType)) {

            assignedtofacetkey = FacetContentType.OpportunityFacetFilter.getContentCode()[1];
            assignedfield = new FacetSolrField(SolrOpportunityRepresenter.FIELD_ASSIGNEE_ID, SolrOpportunityRepresenter.FIELD_ASSIGNEE_ID_NAME);
            //Initialize Facet Filter
            defaultFilter = rbacService.getOpportunityFacetFilterData(initializeDefaultFacetFilter(ListPanelType.OpportunitiesListPanel, ViewName.Opportunity));
//            activeFilters = getActiveFilters(ListPanelType.TasksQuickFilterForMobile);
            type = ListPanelType.OpportunitiesListPanel;
            quickFilterType = ListPanelType.OpportunitiesQuickFilterForMobile;
            otfType = ListPanelType.OpportunityListPanelOTF;
        } else if ("companies".equalsIgnoreCase(entityType)) {

            assignedtofacetkey = FacetContentType.CrmAccountFacetFilter.getContentCode()[2];
            assignedfield = new FacetSolrField(SolrCrmAccountRepresenter.FIELD_OWNER_ID, SolrCrmAccountRepresenter.FIELD_OWNER_ID_NAME);
            //Initialize Facet Filter
            defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_ACCOUNT, initializeDefaultFacetFilter(ListPanelType.CrmAccountListPanel, ViewName.CrmAccount));
//            activeFilters = getActiveFilters(ListPanelType.TasksQuickFilterForMobile);
            type = ListPanelType.CrmAccountListPanel;
            quickFilterType = ListPanelType.CrmAccountQuickFilterForMobile;
            otfType = ListPanelType.CrmAccountListPanelOTF;
        } else if ("leaverequests".equalsIgnoreCase(entityType)) {

            assignedtofacetkey = FacetContentType.LeaveFacetFilter.getContentCode()[1];
            assignedfield = new FacetSolrField(SolrLeaveRequestConst.FIELD_EMPLOYEE_ID, SolrLeaveRequestConst.FIELD_EMPLOYEE_NAME);
            //Initialize Facet Filter
            defaultFilter = rbacService.getLeaveFacetFilterData(initializeDefaultFacetFilter(ListPanelType.LeaveRequestApprove, ViewName.LeaveRequest));
//            activeFilters = getActiveFilters(ListPanelType.TasksQuickFilterForMobile);
            type = ListPanelType.LeaveRequestApprove;
            quickFilterType = ListPanelType.LeaveRequestQuickFilterForMobile;
            otfType = ListPanelType.LeaveRequestApproveOTF;
        } else {
            throw new RestException("entity_type incorrect, available options are leads, tasks, contacts, opportunities, companies", "invalid entity_type", INVALID, HttpStatus.BAD_REQUEST);
        }

        HashMap<String, FacetSolrField> showSolrFieldMap = new HashMap<>();
        showSolrFieldMap.put(assignedtofacetkey, assignedfield);

        ArrayList<String> showFacetCodeName = new ArrayList<>();
        showFacetCodeName.add(assignedtofacetkey);

        FacetFilterRpc byAssignedToFacetFilter = new FacetFilterRpc(type, showSolrFieldMap, showFacetCodeName);
        //Important if we not set below to true then it will use default filter
        byAssignedToFacetFilter.setFilterChanges(true);
        //Retrieve Data from solr
        if (ListPanelType.LeadListPanel.equals(type)) {
            byAssignedToFacetFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, byAssignedToFacetFilter);
        } else if (ListPanelType.ContactListPanel.equals(type)) {
            byAssignedToFacetFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_CONTACT, byAssignedToFacetFilter);
        } else if (ListPanelType.TaskListPanel.equals(type)) {
            FacetFilterRpc assigneeFacetFilter = ListingFilterHelper.createFilterParameter(servletRequest, byAssignedToFacetFilter.getType()).getFacetFilter();
            byAssignedToFacetFilter = rbacService.getTaskFacetFilterData(assigneeFacetFilter, true);
        } else if (ListPanelType.OpportunitiesListPanel.equals(type)) {
            FacetFilterRpc assigneeFacetFilter = ListingFilterHelper.createFilterParameter(servletRequest, byAssignedToFacetFilter.getType()).getFacetFilter();
            byAssignedToFacetFilter = rbacService.getOpportunityFacetFilterData(assigneeFacetFilter);
        } else if (ListPanelType.CrmAccountListPanel.equals(type)) {
            FacetFilterRpc assigneeFacetFilter = ListingFilterHelper.createFilterParameter(servletRequest, byAssignedToFacetFilter.getType()).getFacetFilter();
            byAssignedToFacetFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_ACCOUNT, assigneeFacetFilter);
        } else if (ListPanelType.LeaveRequestApprove.equals(type)) {
            FacetFilterRpc assigneeFacetFilter = ListingFilterHelper.createFilterParameter(servletRequest, byAssignedToFacetFilter.getType()).getFacetFilter();
            byAssignedToFacetFilter = rbacService.getLeaveFacetFilterData(assigneeFacetFilter);
        }

        EdsUser currentUser = userManager.getUser();

        EdsFacetFilter edsFacetFilter = facetFilterManager.getDefaultUserFacetFilter(quickFilterType, currentUser);
        HashMap<Integer, String> activePeople = new HashMap<>();
        HashMap<Integer, String> activeFilters = new HashMap<>();
        if (edsFacetFilter != null) {
            HashSet<String> colNames = new HashSet<>();
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
        if (byAssignedToFacetFilter.getFacetContentMap() != null) {
            FacetContentRpc contentRpc = byAssignedToFacetFilter.getFacetContentMap().get(assignedtofacetkey);
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

        SaveFilterSelectItems filterSelectList = commonServiceLocal.getSavedFacetFilterList(type, null);
        if (filterSelectList.getItems() != null) {
            filterSelectList.getItems();
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
        //Retrieve FACET FILTER data
        FacetFilterRpc otf = getOneTimeFilter(otfType, defaultFilter);
        quickFilters.setOtf_is_active(isOTFActive(otf, activeFilters));

        if (peoples.isEmpty()) {
            throw new RestException("Currently, you do not have any " + entityType + " in this criteria", "Currently, you do not have any " + entityType + " in this criteria", NO_ITEMS_FOUND, HttpStatus.NOT_FOUND);
        }

        return successResponse(quickFilters);
    }

    @Operation(summary = "Get Filters", description = "Getting the filters. You can specify a specific filter id and get an array of the 1th element you want. If you do not specify the id as the query, then we get all the available filters.")
    @RequestMapping(value = "/{entity_type}/filters", method = RequestMethod.GET)
    public Object getFilters(@PathVariable("entity_type") String entityType,
                             @RequestParam(value = "id", required = false) Integer id) throws RestException {

        FacetFilterRpc defaultFilter;
        HashMap<Integer, String> activeFilters;
        ListPanelType type;
        ListPanelType otfType;
        ViewName viewName;

        if ("leads".equalsIgnoreCase(entityType)) {
            //Initialize Facet Filter
            defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, initializeDefaultFacetFilter(ListPanelType.LeadListPanel, ViewName.Lead));
            activeFilters = getActiveFilters(ListPanelType.FilteredStatusesForMobile);
            type = ListPanelType.LeadListPanel;
            otfType = ListPanelType.LeadListPanelOTF;
            viewName = ViewName.Lead;
        } else if ("tasks".equalsIgnoreCase(entityType)) {
            //Initialize Facet Filter
            defaultFilter = rbacService.getTaskFacetFilterData(initializeDefaultFacetFilter(ListPanelType.TaskListPanel, ViewName.Task), true);
            activeFilters = getActiveFilters(ListPanelType.TasksQuickFilterForMobile);
            type = ListPanelType.TaskListPanel;
            otfType = ListPanelType.TaskListPanelOTF;
            viewName = ViewName.Task;
        } else if ("opportunities".equalsIgnoreCase(entityType)) {
            //Initialize Facet Filter
            defaultFilter = rbacService.getOpportunityFacetFilterData(initializeDefaultFacetFilter(ListPanelType.OpportunitiesListPanel, ViewName.Opportunity));
            activeFilters = getActiveFilters(ListPanelType.OpportunitiesQuickFilterForMobile);
            type = ListPanelType.OpportunitiesListPanel;
            otfType = ListPanelType.OpportunityListPanelOTF;
            viewName = ViewName.Opportunity;
        } else if ("contacts".equalsIgnoreCase(entityType)) {
            //Initialize Facet Filter
            defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_CONTACT, initializeDefaultFacetFilter(ListPanelType.ContactListPanel, ViewName.Contact));
            activeFilters = getActiveFilters(ListPanelType.ContactsQuickFilterForMobile);
            type = ListPanelType.ContactListPanel;
            otfType = ListPanelType.ContactListPanelOTF;
            viewName = ViewName.Contact;
        } else if ("companies".equalsIgnoreCase(entityType)) {
            //Initialize Facet Filter
            defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_ACCOUNT, initializeDefaultFacetFilter(ListPanelType.CrmAccountListPanel, ViewName.CrmAccount));
            activeFilters = getActiveFilters(ListPanelType.CrmAccountQuickFilterForMobile);
            type = ListPanelType.CrmAccountListPanel;
            otfType = ListPanelType.CrmAccountListPanelOTF;
            viewName = ViewName.CrmAccount;
        } else if ("leaverequests".equalsIgnoreCase(entityType)) {
            //Initialize Facet Filter
            defaultFilter = rbacService.getLeaveFacetFilterData(initializeDefaultFacetFilter(ListPanelType.LeaveRequestApprove, ViewName.LeaveRequest));
            activeFilters = getActiveFilters(ListPanelType.LeaveRequestApprove);
            type = ListPanelType.LeaveRequestApprove;
            otfType = ListPanelType.LeaveRequestApproveOTF;
            viewName = ViewName.LeaveRequest;
        } else {
            throw new RestException("entity_type incorrect, available options are leads, tasks", "invalid entity_type", INVALID, HttpStatus.BAD_REQUEST);
        }
        ArrayList<FilterTO> result = new ArrayList<>();

        //FacetFilterRpc allAvailableFacetItems = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, mainFilter);

        //Get Filters
        if (id != null) {
            if (id > 0) {
                EdsFacetFilter edsFacetFilter = facetFilterManager.getFacetFilter(id);

                if (edsFacetFilter != null) {
                    FilterTO filterTO = retrieveAndConvertFacetFilter(viewName, type, edsFacetFilter, defaultFilter, activeFilters);
                    if (filterTO != null) {
                        result.add(filterTO);
                    }
                } else {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Filter with id = " + id + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                }
            } else if (id == 0) {
                //One Time Filter has only diff type all other parts are same as for LeadListPanel
                //mainFilter.setType(ListPanelType.LeadListPanelOTF);
                //commonServiceLocal.getUserFacetFilter(mainFilter);
                FacetFilterRpc oneTimeFilter = getOneTimeFilter(otfType, defaultFilter);

                FilterTO filterTO = retrieveAndConvertFacetFilter(viewName, type, oneTimeFilter, defaultFilter, activeFilters);
                if (filterTO != null) {
                    result.add(filterTO);
                }
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "id can not be negative.", INVALID, HttpStatus.BAD_REQUEST);
            }
        } else {
            //If id of filter not came in request then retrieve all saved filters and OTF
            List<EdsFacetFilter> edsFacetFilters = facetFilterManager.getUserFacetFilter(type);
            if (edsFacetFilters != null) {
                edsFacetFilters.forEach(edsFacetFilter -> {
                    FilterTO filterTO = retrieveAndConvertFacetFilter(viewName, type, edsFacetFilter, defaultFilter, activeFilters);
                    if (filterTO != null) {
                        result.add(filterTO);
                    }
                });
            }
            //Add OTF
            // #70 2) When we request all filters (without parameter), please don't sent OTF
            /*FacetFilterRpc oneTimeFilter = getOneTimeFilter(defaultFilter);

            FilterTO filterTO = retrieveAndConvertFacetFilter(oneTimeFilter, defaultFilter, activeFilters);

            if (filterTO != null) {
                result.add(filterTO);
            }*/
        }

        return successResponse(new FiltersListResultTO(result));
    }

    @Operation(summary = "Filtered Statuses", description = """
            Retrieves data on the filtered statuses.\s
             If parameters are not specified, all available statuses will be retrieved.
            people_filter_id list and categories_filter_id list should be provided from quick filters.""")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of filtered statuses or all statuses")})
    @RequestMapping(value = "/{entity_type}/filtered_statuses", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object filteredStatuses(@PathVariable("entity_type") String entityType,
                                   @RequestBody FilteredStatusesRequestTO filteredStatuses) throws RestException {

        final ArrayList<FilteredStatusItemTO> statuses;
        final ArrayList<SelectItem> people = new ArrayList<>();
        final ArrayList<SelectItem> categories = new ArrayList<>();
        try {
            FacetFilterRpc mainMergedFilter;
            FacetFilterRpc defaultFilter;
            //HashMap<Integer, String> activeFilters;
            ListPanelType type;
            ListPanelType otfType;
            ListPanelType quickFilterType;
            String assignedtofacetkey;
            String statusfacetkey;


            if ("leads".equalsIgnoreCase(entityType)) {

                mainMergedFilter = initializeDefaultFacetFilter(ListPanelType.LeadListPanel, ViewName.Lead);
                assignedtofacetkey = FacetContentType.LeadFacetFilter.getContentCode()[5];
                statusfacetkey = FacetContentType.LeadFacetFilter.getContentCode()[2];
                //Initialize Facet Filter
                defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, initializeDefaultFacetFilter(ListPanelType.LeadListPanel, ViewName.Lead));
                //activeFilters = getActiveFilters(ListPanelType.FilteredStatusesForMobile);
                type = ListPanelType.LeadListPanel;
                quickFilterType = ListPanelType.FilteredStatusesForMobile;
                otfType = ListPanelType.LeadListPanelOTF;
            } else if ("tasks".equalsIgnoreCase(entityType)) {

                mainMergedFilter = initializeDefaultFacetFilter(ListPanelType.TaskListPanel, ViewName.Task);
                assignedtofacetkey = FacetContentType.TaskFacetFilter.getContentCode()[5];
                statusfacetkey = FacetContentType.TaskFacetFilter.getContentCode()[3];
                //Initialize Facet Filter
                defaultFilter = rbacService.getTaskFacetFilterData(initializeDefaultFacetFilter(ListPanelType.TaskListPanel, ViewName.Task), true);
                //activeFilters = getActiveFilters(ListPanelType.TasksQuickFilterForMobile);
                type = ListPanelType.TaskListPanel;
                quickFilterType = ListPanelType.TasksQuickFilterForMobile;
                otfType = ListPanelType.TaskListPanelOTF;
            } else if ("opportunities".equalsIgnoreCase(entityType)) {

                mainMergedFilter = initializeDefaultFacetFilter(ListPanelType.OpportunitiesListPanel, ViewName.Opportunity);
                assignedtofacetkey = FacetContentType.OpportunityFacetFilter.getContentCode()[1];
                statusfacetkey = FacetContentType.OpportunityFacetFilter.getContentCode()[0];
                //Initialize Facet Filter
                defaultFilter = rbacService.getOpportunityFacetFilterData(initializeDefaultFacetFilter(ListPanelType.OpportunitiesListPanel, ViewName.Opportunity));
                //activeFilters = getActiveFilters(ListPanelType.OpportunitiesQuickFilterForMobile);
                type = ListPanelType.OpportunitiesListPanel;
                quickFilterType = ListPanelType.OpportunitiesQuickFilterForMobile;
                otfType = ListPanelType.OpportunityListPanelOTF;
            } else {
                throw new RestException("entity_type incorrect, available options are leads, tasks", "invalid entity_type", INVALID, HttpStatus.BAD_REQUEST);
            }

            //Flag to identify if there is filter based on STATUS of Lead
            boolean hasStatusRestriction = false;

            //if OneTime Filter is active
            if (filteredStatuses.getCategories_filter_id().isEmpty() && filteredStatuses.getPeople_filter_id().isEmpty()) {
                //Get OneTimeFilter
                FacetFilterRpc oneTimeFilter = getOneTimeFilter(otfType, defaultFilter);

                if (oneTimeFilter != null && oneTimeFilter.isFavourFilter()) {

//                    log.info("OneTimeFilter is active:" + WfmJsonUtils.facetFilrerConvertToJsonData(oneTimeFilter));

                    if (!oneTimeFilter.getFacetContentMap().isEmpty()) {
                        for (Map.Entry<String, FacetContentRpc> entry : oneTimeFilter.getFacetContentMap().entrySet()) {
                            //Ignore assigned to filter because we expecting it to come from request
                            if (entry.getValue().getFacetItems().length > 0
                                    && ((ListPanelType.LeadListPanel.equals(type) || ListPanelType.TaskListPanel.equals(type) || ListPanelType.OpportunitiesListPanel.equals(type))
                                    /*&& !assignedtofacetkey.equalsIgnoreCase(entry.getKey())*/)) {

                                if ((ListPanelType.LeadListPanel.equals(type) || ListPanelType.TaskListPanel.equals(type) || ListPanelType.OpportunitiesListPanel.equals(type))
                                        && statusfacetkey.equalsIgnoreCase(entry.getKey())) {
                                    hasStatusRestriction = true;
                                }
                                FacetContentRpc existingVals = mainMergedFilter.getFacetContentMap().get(entry.getKey());
                                if (existingVals != null) {
                                    ArrayList<SelectItem> existingItems = new ArrayList<>(Arrays.asList(existingVals.getFacetItems()));
                                    existingItems.addAll(Arrays.asList(entry.getValue().getFacetItems()));
                                    existingVals.setFacetItems(existingItems.toArray(new SelectItem[0]));
                                    existingVals.setSavedItems(entry.getValue().getSavedItems());
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
                        if (StringUtils.isNotBlank(oneTimeFilter.getSelectedDateSolrCodeName())) {
                            mainMergedFilter.setSelectedDateSolrCodeName(oneTimeFilter.getSelectedDateSolrCodeName());
                        } /*else {
                            if(ListPanelType.LeadListPanel.equals(type)) {
                                mainMergedFilter.setSelectedDateSolrCodeName(SolrContactRepresenter.FIELD_UPDATE_DATE);
                            } else {
                                //@TODO need to check
                                mainMergedFilter.setSelectedDateSolrCodeName(SolrTaskRepresenter.FIELD_DUE_DATE);
                            }
                        }*/
                    }
                    //End Of Set Periods
                }
            } else {
                //if OTF is not active and quick filter is not empty

                //Merge Categories (saved facet filters)
                if (filteredStatuses.getCategories_filter_id() != null && filteredStatuses.getCategories_filter_id().size() > 0) {
                    for (Integer facetFilterId : filteredStatuses.getCategories_filter_id()) {
                        //fill list to save it later
                        categories.add(new SelectItem(facetFilterId, ""));
                        EdsFacetFilter filter = facetFilterManager.getFacetFilter(facetFilterId);
                        if (filter != null) {
                            FacetFilterRpc filterRpc = filter.getFacetFilter(new HashSet<>(mainMergedFilter.getShowFacetCodeName()));
                            if (filterRpc != null && !filterRpc.getFacetContentMap().isEmpty()) {
                                for (Map.Entry<String, FacetContentRpc> entry : filterRpc.getFacetContentMap().entrySet()) {
                                    //Ignore assigned to filter because we expecting it to come from request
                                    if (entry.getValue().getFacetItems().length > 0
                                            && ((ListPanelType.LeadListPanel.equals(type) || ListPanelType.TaskListPanel.equals(type)
                                            || ListPanelType.OpportunitiesListPanel.equals(type))
                                            /*&& !assignedtofacetkey.equalsIgnoreCase(entry.getKey())*/)) {
//
                                        if ((ListPanelType.LeadListPanel.equals(type) || ListPanelType.TaskListPanel.equals(type) || ListPanelType.OpportunitiesListPanel.equals(type))
                                                && statusfacetkey.equalsIgnoreCase(entry.getKey())) {
                                            hasStatusRestriction = true;
                                        }
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
                            //Set Periods
                            if (filterRpc.getStartDate() != null && filterRpc.getEndDate() != null) {
                                mainMergedFilter.setStartDate(filterRpc.getStartDate());
                                mainMergedFilter.setEndDate(filterRpc.getEndDate());
                                if (StringUtils.isNotBlank(filterRpc.getSelectedDateSolrCodeName())) {
                                    mainMergedFilter.setSelectedDateSolrCodeName(filterRpc.getSelectedDateSolrCodeName());
                                } else {
                                    if (ListPanelType.LeadListPanel.equals(type)) {
                                        mainMergedFilter.setSelectedDateSolrCodeName(SolrContactRepresenter.FIELD_UPDATE_DATE);
                                    } else {
                                        //@TODO need to check
                                        mainMergedFilter.setSelectedDateSolrCodeName(SolrTaskRepresenter.FIELD_DUE_DATE);
                                    }
                                }
                            }
                            //End Of Set Periods
                        }
                    }
                }

                //Add PeopleIDs
                if (filteredStatuses.getPeople_filter_id() != null && filteredStatuses.getPeople_filter_id().size() > 0) {

                    FacetContentRpc existingAssignedTo = mainMergedFilter.getFacetContentMap().get(assignedtofacetkey);

                    if (existingAssignedTo == null) {
                        existingAssignedTo = new FacetContentRpc();
                    }
                    ArrayList<SelectItem> peopleItems = new ArrayList<>(Arrays.asList(existingAssignedTo.getFacetItems()));//reverted back
                    for (Integer assigneeId : filteredStatuses.getPeople_filter_id()) {
                        SelectItem assignee = new SelectItem(assigneeId > 0 ? assigneeId : -1, "");
                        peopleItems.add(assignee);
                        //fill list to save it later
                        people.add(assignee);
                    }
                    existingAssignedTo.setFacetItems(peopleItems.toArray(new SelectItem[0]));
                    mainMergedFilter.getFacetContentMap().put(assignedtofacetkey, existingAssignedTo);
                }
            }

            SelectItem[] requestedStatuses = mainMergedFilter.getFacetContentMap().get(statusfacetkey).getFacetItems();

//            log.info("\n\n\nmainMergedFilter: " + WfmJsonUtils.facetFilrerConvertToJsonData(mainMergedFilter));
            //Retrieve DATA from SOLR
            FacetFilterRpc filterWithData = null;
            if (ListPanelType.LeadListPanel.equals(type)) {
                filterWithData = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, mainMergedFilter);
            } else if (ListPanelType.TaskListPanel.equals(type)) {
                filterWithData = rbacService.getTaskFacetFilterData(mainMergedFilter, true);
            } else if (ListPanelType.OpportunitiesListPanel.equals(type)) {
                filterWithData = rbacService.getOpportunityFacetFilterData(mainMergedFilter);
            }

//            log.info("\n\n\nfilterWithData: " + WfmJsonUtils.facetFilrerConvertToJsonData(filterWithData));

            //Generate API Response
            HashMap<Integer, FilteredStatusItemTO> availableStatusesFromSolr = new HashMap<>();

            if (filterWithData != null && filterWithData.getFacetContentMap() != null) {
                FacetContentRpc contentRpc = filterWithData.getFacetContentMap().get(statusfacetkey);
                if (contentRpc != null && contentRpc.getFacetItems() != null) {
                    for (SelectItem selectItem : contentRpc.getFacetItems()) {
                        if (selectItem.getId() != null) {

                            FilteredStatusItemTO item = new FilteredStatusItemTO();
                            //Set Leads Count from solr
                            item.setCount_of_items(selectItem.getTotalCount());

                            if (selectItem.getId() > 0) {

                                EdsReference statusFromDB = referenceManager.get(selectItem.getId());
                                //Filter by roles
                                if (statusFromDB != null) {
                                    if (!statusFromDB.isDeleted() && (userManager.getUser() == null || userManager.getUser().hasEitherRoles(statusFromDB.getAllowedRoles().toArray(new EdsRole[]{})) || userManager.getUser().hasEitherRoles(statusFromDB.getViewOnlyRoles().toArray(new EdsRole[]{}))) || userManager.getUser().hasEitherRoles(statusFromDB.getAllowedRoles().toArray(new EdsRole[]{})) || statusFromDB.getAllowedRoles().isEmpty() || statusFromDB.getOppEditBtnRole().isEmpty() || statusFromDB.getViewOnlyRoles().isEmpty()) {

                                        item.setStatus_id(statusFromDB.getObjectID());
                                        item.setStatus_name(statusFromDB.getName());
                                        item.setOrder_id(statusFromDB.getSorder());
                                        if (ListPanelType.OpportunitiesListPanel.equals(type)) {
                                            item.setPercentage(statusFromDB.getDescription());
                                        }

                                        if (statusFromDB.getReferenceColor() != null) {
                                            EdsReferenceColor edsReferenceColor = statusFromDB.getReferenceColor();//referenceColorManager.get(selectItem.getColorId());
                                            if (edsReferenceColor != null) {
                                                item.setStatus_color(new ColorTO(edsReferenceColor.getObjectID(), edsReferenceColor.getHex(), edsReferenceColor.getName()));
                                            } else {
                                                item.setStatus_color(getDefaultColor());
                                            }
                                        } else {
                                            //if Status from database doesnt have color set
                                            item.setStatus_color(getDefaultColor());
                                        }
                                        item.setEdit_permission(statusFromDB.getOppEditBtnRole().isEmpty() || userManager.getUser().hasEitherRoles(statusFromDB.getOppEditBtnRole().toArray(new EdsRole[]{})));
                                        item.setStatus_permission(statusFromDB.getAllowedRoles().isEmpty() || userManager.getUser().hasEitherRoles(statusFromDB.getAllowedRoles().toArray(new EdsRole[]{})));
                                        item.setView_permission(statusFromDB.getViewOnlyRoles().isEmpty() || userManager.getUser().hasEitherRoles(statusFromDB.getViewOnlyRoles().toArray(new EdsRole[]{})) || item.isStatus_permission());
                                        item.setCommentRequired(statusFromDB.isRequiredComment());
                                    } else {
                                        continue;
                                    }
                                } else {
                                    //Status was not found in database
                                    //Get data from solr
                                    item.setStatus_name(selectItem.getName());
                                    item.setStatus_id(0);
                                    item.setOrder_id(0);
                                    item.setStatus_color(getDefaultColor());
                                }

                            } else {
                                //If N/A status Stepan asked to set below values
                                item.setStatus_name(selectItem.getName());
                                item.setStatus_id(0);
                                item.setOrder_id(0);
                                item.setStatus_color(getDefaultColor());
                            }
                            availableStatusesFromSolr.put(item.getStatus_id(), item);

                        }
                    }
                }
            }
            //Add All statuses from SOLR to result
            statuses = new ArrayList<>(availableStatusesFromSolr.values());

            //as per #171 : If passed categories_filter_id doesn't have limitations directly set on statuses to show, you should return all statuses.
            if (!hasStatusRestriction) {
                log.info("hasStatusRestriction = " + hasStatusRestriction);
                List<EdsReference> allStatuses = null;
                if (ListPanelType.LeadListPanel.equals(type)) {
                    allStatuses = referenceManager.listReferences(EdsCrmContact._LEAD_STATUS);
                } else if (ListPanelType.TaskListPanel.equals(type)) {
                    allStatuses = referenceManager.listReferences(EdsTask.TASK_STATUS);
                } else if (ListPanelType.OpportunitiesListPanel.equals(type)) {
                    allStatuses = referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_STAGE);
                }
                if (allStatuses != null) {
                    allStatuses.forEach(status -> {
                        //Filter by roles
                        if (status != null && !status.isDeleted() && (userManager.getUser() == null || userManager.getUser().hasEitherRoles(status.getAllowedRoles().toArray(new EdsRole[]{})) || userManager.getUser().hasEitherRoles(status.getViewOnlyRoles().toArray(new EdsRole[]{}))) || userManager.getUser().hasEitherRoles(status.getAllowedRoles().toArray(new EdsRole[]{})) || status.getAllowedRoles().isEmpty() || status.getOppEditBtnRole().isEmpty() || status.getViewOnlyRoles().isEmpty()) {
                            //We must add only nonexisting items into list
                            if (status != null && availableStatusesFromSolr.get(status.getObjectID()) == null) {
                                FilteredStatusItemTO item = new FilteredStatusItemTO();
                                item.setStatus_id(status.getObjectID());
                                item.setCount_of_items(0L);
                                item.setOrder_id(status.getSorder());
                                item.setStatus_name(status.getName());
                                if (ListPanelType.OpportunitiesListPanel.equals(type)) {
                                    item.setPercentage(status.getDescription());
                                }
                                if (status.getReferenceColor() != null) {
                                    item.setStatus_color(new ColorTO(status.getReferenceColor().getObjectID(), status.getReferenceColor().getHex(), status.getReferenceColor().getName()));
                                }
                                item.setEdit_permission(status.getOppEditBtnRole().isEmpty() || userManager.getUser().hasEitherRoles(status.getOppEditBtnRole().toArray(new EdsRole[]{})));
                                item.setStatus_permission(status.getAllowedRoles().isEmpty() || userManager.getUser().hasEitherRoles(status.getAllowedRoles().toArray(new EdsRole[]{})));
                                item.setView_permission(status.getViewOnlyRoles().isEmpty() || userManager.getUser().hasEitherRoles(status.getViewOnlyRoles().toArray(new EdsRole[]{})) || item.isStatus_permission());
                                item.setCommentRequired(status.isRequiredComment());
                                statuses.add(item);
                            }
                        }
                    });
                }
            } else {
                //#49 When there is a direct filter for certain statuses, these statuses should always come, even if no leads
                if (requestedStatuses != null && requestedStatuses.length > 0) {

                    Map<Integer, Integer> requestedStatusesMap = Arrays.stream(requestedStatuses).collect(Collectors.toMap(SelectItem::getId, SelectItem::getId,
                            (status1, status2) -> {
                                System.out.println("duplicate key found! - " + status1);
                                return status1;
                            }));

                    /*Arrays.stream(requestedStatuses)*/
                    requestedStatusesMap.keySet().forEach(requestedStatusId -> {
                        //We must add only nonexisting items into list
                        if (requestedStatusId == -1) {
                            requestedStatusId = 0;
                        }
                        if (availableStatusesFromSolr.get(requestedStatusId) == null) {
                            log.info("availableStatusesFromSolr = " + requestedStatusId /*+ " " + requestedStatus.getName()*/);
                            if (requestedStatusId > 0) {
                                EdsReference leadstatus = referenceManager.get(requestedStatusId/*requestedStatus.getId()*/);
                                //Filter by roles
                                if (leadstatus != null && !leadstatus.isDeleted() && (userManager.getUser() == null || userManager.getUser().hasEitherRoles(leadstatus.getAllowedRoles().toArray(new EdsRole[]{})) || leadstatus.getAllowedRoles().isEmpty())) {
                                    if (leadstatus != null) {
                                        FilteredStatusItemTO item = new FilteredStatusItemTO();
                                        item.setStatus_id(leadstatus.getObjectID());
                                        item.setCount_of_items(0L);
                                        item.setOrder_id(leadstatus.getSorder());
                                        item.setStatus_name(leadstatus.getName());
                                        if (leadstatus.getReferenceColor() != null) {
                                            item.setStatus_color(new ColorTO(leadstatus.getReferenceColor().getObjectID(), leadstatus.getReferenceColor().getHex(), leadstatus.getReferenceColor().getName()));
                                        }
                                        if (ListPanelType.OpportunitiesListPanel.equals(type)) {
                                            item.setPercentage(leadstatus.getDescription());
                                        }
                                        statuses.add(item);
                                    }
                                }
                            } else {
                                FilteredStatusItemTO item = new FilteredStatusItemTO();
                                item.setStatus_name("N/A");
                                item.setStatus_id(0);
                                item.setOrder_id(0);
                                item.setStatus_color(getDefaultColor());
                                statuses.add(item);
                            }
                        }
                    });
                }
            }

            //Saving request as json into database
            saveFilter(quickFilterType, people, categories);

        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        return successResponse(new FilteredStatusesListTO(statuses));
    }

    @Operation(summary = "Edit (Save)  Filter", description = "Edit the filter. The method is actually for saving the Filter, after the user has configured it with the help of Lead Global Filters Setup. Sent is_active values become is_active for the specified filter on the server, thus saving / editing it.")
    @RequestMapping(value = "/{entity}/filters/{id}", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object editSaveFilter(@PathVariable(value = "entity") String entity, @PathVariable(value = "id") Integer id, @RequestBody BaseFilterTO filterTO) throws RestException {

        if (id != null) {

            ListPanelType type;
            ViewName viewName;

            if ("leads".equalsIgnoreCase(entity)) {
                type = ListPanelType.LeadListPanel;
                viewName = ViewName.Lead;
            } else if ("tasks".equalsIgnoreCase(entity)) {
                type = ListPanelType.TaskListPanel;
                viewName = ViewName.Task;
            } else if ("opportunities".equalsIgnoreCase(entity)) {
                type = ListPanelType.OpportunitiesListPanel;
                viewName = ViewName.Opportunity;
            } else if ("contacts".equalsIgnoreCase(entity)) {
                type = ListPanelType.ContactListPanel;
                viewName = ViewName.Contact;
            } else if ("companies".equalsIgnoreCase(entity)) {
                type = ListPanelType.CrmAccountListPanel;
                viewName = ViewName.CrmAccount;
            } else if ("leaverequests".equalsIgnoreCase(entity)) {
                type = ListPanelType.LeaveRequestApprove;
                viewName = ViewName.LeaveRequest;
            } else {
                throw new RestException("entity_type incorrect, available options are leads, tasks", "invalid entity", INVALID, HttpStatus.BAD_REQUEST);
            }

            //Initialize Facet Filter
            FacetFilterRpc filterRpc = initializeDefaultFacetFilter(type, viewName);
            //End Of main facet filter initialization

            if (id > 0) {
                EdsFacetFilter edsFacetFilter = facetFilterManager.getFacetFilter(id);
                if (edsFacetFilter != null) {
                    filterRpc = edsFacetFilter.getFacetFilter(new HashSet<>(filterRpc.getShowFacetCodeName()));
                    filterRpc.setType(ListPanelType.valueOf(edsFacetFilter.getType()));
                } else {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Filter with id = " + id + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                }
            } else if (id == 0) {
                filterRpc = getOneTimeFilter(type, filterRpc);
            }


            filterRpc.setFilterChanges(true);
            filterRpc.setShowSolrFieldMap(filterRpc.getShowSolrFieldMap());

            filterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, filterRpc);
            if ("leads".equalsIgnoreCase(entity)) {
                filterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, filterRpc);
            } else if ("tasks".equalsIgnoreCase(entity)) {
                filterRpc = rbacService.getTaskFacetFilterData(filterRpc, true);
            } else if ("opportunities".equalsIgnoreCase(entity)) {
                filterRpc = rbacService.getOpportunityFacetFilterData(filterRpc);
            } else if ("contacts".equalsIgnoreCase(entity)) {
                filterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_CONTACT, filterRpc);
            } else if ("companies".equalsIgnoreCase(entity)) {
                filterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_ACCOUNT, filterRpc);
            } else if ("leaverequests".equalsIgnoreCase(entity)) {
                filterRpc = rbacService.getLeaveFacetFilterData(filterRpc);
            }

            SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

            if (filterTO != null) {
                filterRpc.setName(filterTO.getName());
                filterRpc.setDefaultFilter(Boolean.TRUE.equals(filterTO.getIs_default()));
                filterRpc.setPublicFilter(Boolean.TRUE.equals(filterTO.getIs_public()));
                filterRpc.setName(filterTO.getName());

                //Date Period
                setDatePeriod(filterTO, filterRpc);
                //End Of Date Period

                HashMap<String, String> categoriesMap = new HashMap<>();

                for (FilterCategoryTO filterCategoryTO : filterTO.getFilter_categories()) {

                    if (StringUtils.isNotBlank(filterCategoryTO.getCategory_code())) {

                        categoriesMap.put(filterCategoryTO.getCategory_code(), filterCategoryTO.getCategory_code());

                        if (filterRpc.getFacetContentMap().get(filterCategoryTO.getCategory_code()) != null) {
                            //Clear existing
                            filterRpc.getFacetContentMap().get(filterCategoryTO.getCategory_code()).getSavedItems().clear();
                            ArrayList<SelectItem> facetItems = new ArrayList<>();

                            if (filterCategoryTO.getSub_categories() != null) {
                                for (SubCategoryTO subCategoryTO : filterCategoryTO.getSub_categories()) {

                                    if (subCategoryTO.getId() == null || subCategoryTO.getId() == 0) {
                                        subCategoryTO.setId(-1);
                                    }

                                    if (Boolean.TRUE.equals(subCategoryTO.getIs_active())) {
                                        //Set Saved Items
                                        filterRpc.getFacetContentMap().get(filterCategoryTO.getCategory_code()).getSavedItems().put(subCategoryTO.getId(), subCategoryTO.getId());

                                        //Facet item
                                        SelectItem item = new SelectItem(subCategoryTO.getId(), subCategoryTO.getName());
                                        item.setTotalCount(subCategoryTO.getTotal_count());
                                        facetItems.add(item);
                                    }
                                }
                            }

                            filterRpc.getFacetContentMap().get(filterCategoryTO.getCategory_code()).setFacetItems(facetItems.toArray(new SelectItem[0]));

                        } else {
                            //If Not exist in filter
                            FacetContentRpc newCategory = new FacetContentRpc();
                            ArrayList<SelectItem> facetItems = new ArrayList<>();

                            if (filterCategoryTO.getSub_categories() != null) {
                                for (SubCategoryTO subCategoryTO : filterCategoryTO.getSub_categories()) {
                                    if (Boolean.TRUE.equals(subCategoryTO.getIs_active())) {
                                        if (subCategoryTO.getId() == null || subCategoryTO.getId() <= 0) {
                                            subCategoryTO.setId(-1);
                                        }
                                        newCategory.getSavedItems().put(subCategoryTO.getId(), subCategoryTO.getId());

                                        SelectItem item = new SelectItem(subCategoryTO.getId(), subCategoryTO.getName());
                                        item.setTotalCount(subCategoryTO.getTotal_count());
                                        facetItems.add(item);
                                    }
                                }
                            }
                            newCategory.setFacetItems(facetItems.toArray(new SelectItem[0]));
                            filterRpc.getFacetContentMap().put(filterCategoryTO.getCategory_code(), newCategory);
                        }
                    }

                }
                //Remove missing categories
                for (String categoryName : filterRpc.getFacetContentMap().keySet()) {
                    if (categoriesMap.get(categoryName) == null) {
                        filterRpc.getFacetContentMap().get(categoryName).setFacetItems(new SelectItem[0]);
                        filterRpc.getFacetContentMap().get(categoryName).getSavedItems().clear();
                    }
                }

                //Save Filter
                commonServiceLocal.saveFacetFilter(filterRpc, filterRpc.getType()/*ListPanelType.LeadListPanel*/);

                return successResponse(new ResponseData());
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Filter data empty", REQUIRED, HttpStatus.BAD_REQUEST);
            }


        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Setup Filter", description = "Configuring filters for leads. The idea is that when sending to the server the status of the screen on the client, get back a new filter configuration, approximately as it is implemented on the web.<br/>Possible values for Filter_by: DATE_CREATED, DATE_UPDATED, NOT_SELECTED")
    @RequestMapping(value = "/{entity}/filters/{id}/setup", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object setupFilter(@PathVariable(value = "entity") String entity, @PathVariable(value = "id") Integer id, @RequestBody FilterTO filterTO) throws RestException {

        if (id != null) {

            if (filterTO != null) {

                FacetFilterRpc defaultFilter;
                ListPanelType type;
                ViewName viewName;
                ListPanelType quickFilterType;
                ListPanelType otfType;

                if ("leads".equalsIgnoreCase(entity)) {
                    type = ListPanelType.LeadListPanel;
                    viewName = ViewName.Lead;
                    quickFilterType = ListPanelType.FilteredStatusesForMobile;
                    otfType = ListPanelType.LeadListPanelOTF;
                    //Initialize Facet Filter
                    defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, initializeDefaultFacetFilter(ListPanelType.LeadListPanel, ViewName.Lead));
                } else if ("tasks".equalsIgnoreCase(entity)) {
                    type = ListPanelType.TaskListPanel;
                    viewName = ViewName.Task;
                    quickFilterType = ListPanelType.TasksQuickFilterForMobile;
                    otfType = ListPanelType.TaskListPanelOTF;
                    //Initialize Facet Filter
                    defaultFilter = rbacService.getTaskFacetFilterData(initializeDefaultFacetFilter(ListPanelType.TaskListPanel, ViewName.Task), true);
                } else if ("opportunities".equalsIgnoreCase(entity)) {
                    type = ListPanelType.OpportunitiesListPanel;
                    viewName = ViewName.Opportunity;
                    quickFilterType = ListPanelType.OpportunitiesQuickFilterForMobile;
                    otfType = ListPanelType.OpportunityListPanelOTF;
                    //Initialize Facet Filter
                    defaultFilter = rbacService.getOpportunityFacetFilterData(initializeDefaultFacetFilter(ListPanelType.OpportunitiesListPanel, ViewName.Opportunity));
                } else if ("contacts".equalsIgnoreCase(entity)) {
                    type = ListPanelType.ContactListPanel;
                    viewName = ViewName.Contact;
                    quickFilterType = ListPanelType.ContactsQuickFilterForMobile;
                    otfType = ListPanelType.ContactListPanelOTF;
                    //Initialize Facet Filter
                    defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_CONTACT, initializeDefaultFacetFilter(ListPanelType.ContactListPanel, ViewName.Contact));
                } else if ("companies".equalsIgnoreCase(entity)) {
                    type = ListPanelType.CrmAccountListPanel;
                    viewName = ViewName.CrmAccount;
                    quickFilterType = ListPanelType.CrmAccountQuickFilterForMobile;
                    otfType = ListPanelType.CrmAccountListPanelOTF;
                    //Initialize Facet Filter
                    defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_ACCOUNT, initializeDefaultFacetFilter(ListPanelType.CrmAccountListPanel, ViewName.CrmAccount));
                } else if ("leaverequests".equalsIgnoreCase(entity)) {
                    type = ListPanelType.LeaveRequestApprove;
                    viewName = ViewName.LeaveRequest;
                    quickFilterType = ListPanelType.LeaveRequestQuickFilterForMobile;
                    otfType = ListPanelType.LeaveRequestApproveOTF;
                    //Initialize Facet Filter
                    defaultFilter = rbacService.getLeaveFacetFilterData(initializeDefaultFacetFilter(ListPanelType.LeaveRequestApprove, ViewName.LeaveRequest));
                } else {
                    throw new RestException("entity_type incorrect, available options are leads, tasks, opportunities, contacts, companies", "invalid entity", INVALID, HttpStatus.BAD_REQUEST);
                }

                FacetFilterRpc filterRpc = initializeDefaultFacetFilter(type, viewName);//edsFacetFilter.getFacetFilter(new HashSet<>(mainFilter.getShowFacetCodeName()));
                filterRpc.setFilterChanges(true);
//                filterRpc.setShowSolrFieldMap(mainFilter.getShowSolrFieldMap());

                if (id > 0) {
                    EdsFacetFilter edsFacetFilter = facetFilterManager.getFacetFilter(id);
                    filterRpc.setName(edsFacetFilter.getName());

                    EdsUserFilter edsUserFilter = userFilterManager.getByFacetFilterId(edsFacetFilter.getObjectID());
                    if (edsUserFilter != null) {
                        filterRpc.setDefaultFilter(Boolean.TRUE.equals(edsUserFilter.getIsDefault()));
                        filterRpc.setPublicFilter(Boolean.TRUE.equals(edsFacetFilter.isSystemFilter()));
                    }
                } else {
                    filterRpc.setName("OTF");
                    filterRpc.setDefaultFilter(false);
                    filterRpc.setPublicFilter(false);
                }

                //Date Period
                setDatePeriod(filterTO, filterRpc);
                //End Of Date Period

                HashMap<String, String> categoriesMap = new HashMap<>();

                for (FilterCategoryTO filterCategoryTO : filterTO.getFilter_categories()) {

                    if (StringUtils.isNotBlank(filterCategoryTO.getCategory_code())) {

                        categoriesMap.put(filterCategoryTO.getCategory_code(), filterCategoryTO.getCategory_code());

                        if (filterRpc.getFacetContentMap().get(filterCategoryTO.getCategory_code()) != null) {
                            //Clear existing saveditems
                            filterRpc.getFacetContentMap().get(filterCategoryTO.getCategory_code()).getSavedItems().clear();
                            ArrayList<SelectItem> facetItems = new ArrayList<>();

                            if (filterCategoryTO.getSub_categories() != null) {
                                for (SubCategoryTO subCategoryTO : filterCategoryTO.getSub_categories()) {

                                    if (subCategoryTO.getId() == null || subCategoryTO.getId() == 0) {
                                        subCategoryTO.setId(-1);
                                    }

                                    if (Boolean.TRUE.equals(subCategoryTO.getIs_active())) {
                                        //Set Saved Items
                                        filterRpc.getFacetContentMap().get(filterCategoryTO.getCategory_code()).getSavedItems().put(subCategoryTO.getId(), subCategoryTO.getId());

                                        //Facet item
                                        SelectItem item = new SelectItem(subCategoryTO.getId(), subCategoryTO.getName());
                                        item.setTotalCount(subCategoryTO.getTotal_count());
                                        if (subCategoryTO.getId() != null && "95".equals(subCategoryTO.getId().toString()) && "jobtitle".equalsIgnoreCase(filterCategoryTO.getCategory_code())) {
                                            item.setName("");
                                        }
                                        facetItems.add(item);
                                    }
                                }
                            }

                            filterRpc.getFacetContentMap().get(filterCategoryTO.getCategory_code()).setFacetItems(facetItems.toArray(new SelectItem[0]));

                        } else {
                            //If Not exist in filter
                            FacetContentRpc newCategory = new FacetContentRpc();
                            ArrayList<SelectItem> facetItems = new ArrayList<>();

                            if (filterCategoryTO.getSub_categories() != null) {
                                for (SubCategoryTO subCategoryTO : filterCategoryTO.getSub_categories()) {
                                    if (Boolean.TRUE.equals(subCategoryTO.getIs_active())) {
                                        if (subCategoryTO.getId() == null || subCategoryTO.getId() <= 0) {
                                            subCategoryTO.setId(-1);
                                        }
                                        newCategory.getSavedItems().put(subCategoryTO.getId(), subCategoryTO.getId());

                                        SelectItem item = new SelectItem(subCategoryTO.getId(), subCategoryTO.getName());
                                        if (subCategoryTO.getId() != null && "95".equals(subCategoryTO.getId().toString()) && "jobtitle".equalsIgnoreCase(filterCategoryTO.getCategory_code())) {
                                            item.setName("");
                                        }
                                        item.setTotalCount(subCategoryTO.getTotal_count());
                                        facetItems.add(item);
                                    }
                                }
                            }
                            newCategory.setFacetItems(facetItems.toArray(new SelectItem[0]));
                            filterRpc.getFacetContentMap().put(filterCategoryTO.getCategory_code(), newCategory);
                        }
                    }

                }
                //Remove missing categories
                for (String categoryName : filterRpc.getFacetContentMap().keySet()) {
                    if (categoriesMap.get(categoryName) == null) {
                        filterRpc.getFacetContentMap().get(categoryName).setFacetItems(new SelectItem[0]);
                        filterRpc.getFacetContentMap().get(categoryName).getSavedItems().clear();
                    }
                }

                //Fetch Facet Data
//                filterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, filterRpc); we will not retrieve it because we do that in convertfacetFilter(); used below
                filterRpc.setObjectID(id);

                HashMap<Integer, String> activeFilters = getActiveFilters(quickFilterType);

                //End Of main facet filter initialization
//                log.info("Filter 1: " + WfmJsonUtils.facetFilrerConvertToJsonData(filterRpc));
                FilterTO result = retrieveAndConvertFacetFilter(viewName, (id > 0 ? type : otfType), filterRpc, defaultFilter, activeFilters);
                //"category" is something we need to clean before
                if (ViewName.Contact.equals(viewName)) {

                    if (result.getFilter_categories() != null) {
                        ArrayList<FilterCategoryTO> newcategories = new ArrayList<>();

                        result.getFilter_categories().forEach(cat -> {
                            if (FacetContentType.ContactFacetFilter.getContentCode()[5].equals(cat.getCategory_code())) {
                                if (cat.getSub_categories() != null) {
                                    List<SubCategoryTO> filteredSubCats = cat.getSub_categories().stream().filter(s -> Boolean.TRUE.equals(s.getIs_active())).collect(Collectors.toList());

                                    if (filteredSubCats.size() > 0) {
                                        cat.setSub_categories((ArrayList<SubCategoryTO>) filteredSubCats);
                                    }
                                }
                            }
                            newcategories.add(cat);
                        });
                        result.setFilter_categories(newcategories);
                    }
                }
//                log.info("Filter 3: " + WfmJsonUtils.facetFilrerConvertToJsonData(filterRpc));
                //we must save state of OneTimeFilter
                if (id == 0) {
                    FacetFilterRpc oneTimeFilter = getOneTimeFilter(otfType, defaultFilter);
//                    log.info("OTF Before Update: " + WfmJsonUtils.facetFilrerConvertToJsonData(oneTimeFilter));
                    oneTimeFilter.setFacetContentMap(filterRpc.getFacetContentMap());
                    for (Map.Entry<String, FacetContentRpc> entry : oneTimeFilter.getFacetContentMap().entrySet()) {
                        if (entry.getValue() == null || entry.getValue().getSavedItems().isEmpty()) {
                            oneTimeFilter.getFacetContentMap().get(entry.getKey()).setFacetItems(null);
                        } else {
                            ArrayList<SelectItem> items = new ArrayList<>();
                            for (SelectItem item : entry.getValue().getFacetItems()) {
                                if (entry.getValue().getSavedItems().get(item.getId()) != null) {
                                    items.add(item);
                                }
                            }
                            oneTimeFilter.getFacetContentMap().get(entry.getKey()).setFacetItems(items.toArray(new SelectItem[]{}));
                        }
                    }

                    oneTimeFilter.setStartDate(filterRpc.getStartDate());
                    oneTimeFilter.setEndDate(filterRpc.getEndDate());
                    oneTimeFilter.setSelectedDateSolrCodeName(filterRpc.getSelectedDateSolrCodeName());
                    log.info("OTF Id: " + oneTimeFilter.getObjectID());
//                    log.info("OTF After Update: " + WfmJsonUtils.facetFilrerConvertToJsonData(oneTimeFilter));
                    //Save OneTimeFilter
                    Integer updatedId = commonServiceLocal.saveFacetFilter(oneTimeFilter, otfType);
                    log.info("UpdatedId: " + updatedId);
                }

                return successResponse(result);


            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Filter empty", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } else {

            throw new RestException(GENERAL_ERROR_MESSAGE, "id required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Reset One Time Filter", description = "Resets the filter to id. We are not going to reset any filters except OTF, so I suggest at the moment to all queries with id! = 0 to return the error from the server. This url is purely for preserving integrity, convenience and scaling, if you have something to change.")
    @RequestMapping(value = "/{entity}/filters/{id}/reset", method = RequestMethod.POST, /*consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},*/ produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object resetFilter(@PathVariable(value = "entity") String entity, @PathVariable(value = "id") Integer id) throws RestException {

        if (id != null) {

            FacetFilterRpc defaultFilter;
            ListPanelType type;
            ViewName viewName;
            ListPanelType quickFilterType;
            ListPanelType otfType;

            if ("leads".equalsIgnoreCase(entity)) {
                type = ListPanelType.LeadListPanel;
                viewName = ViewName.Lead;
                quickFilterType = ListPanelType.FilteredStatusesForMobile;
                otfType = ListPanelType.LeadListPanelOTF;
                //Initialize Facet Filter
                defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, initializeDefaultFacetFilter(ListPanelType.LeadListPanel, ViewName.Lead));
            } else if ("tasks".equalsIgnoreCase(entity)) {
                type = ListPanelType.TaskListPanel;
                viewName = ViewName.Task;
                quickFilterType = ListPanelType.TasksQuickFilterForMobile;
                otfType = ListPanelType.TaskListPanelOTF;
                //Initialize Facet Filter
                defaultFilter = rbacService.getTaskFacetFilterData(initializeDefaultFacetFilter(ListPanelType.TaskListPanel, ViewName.Task), true);
            } else if ("opportunities".equalsIgnoreCase(entity)) {
                type = ListPanelType.OpportunitiesListPanel;
                viewName = ViewName.Opportunity;
                quickFilterType = ListPanelType.OpportunitiesQuickFilterForMobile;
                otfType = ListPanelType.OpportunityListPanelOTF;
                //Initialize Facet Filter
                defaultFilter = rbacService.getOpportunityFacetFilterData(initializeDefaultFacetFilter(ListPanelType.OpportunitiesListPanel, ViewName.Opportunity));
            } else if ("contacts".equalsIgnoreCase(entity)) {
                type = ListPanelType.ContactListPanel;
                viewName = ViewName.Contact;
                quickFilterType = ListPanelType.ContactsQuickFilterForMobile;
                otfType = ListPanelType.ContactListPanelOTF;
                //Initialize Facet Filter
                defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_CONTACT, initializeDefaultFacetFilter(ListPanelType.ContactListPanel, ViewName.Contact));
            } else if ("companies".equalsIgnoreCase(entity)) {
                type = ListPanelType.CrmAccountListPanel;
                viewName = ViewName.CrmAccount;
                quickFilterType = ListPanelType.CrmAccountQuickFilterForMobile;
                otfType = ListPanelType.CrmAccountListPanelOTF;
                //Initialize Facet Filter
                defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_ACCOUNT, initializeDefaultFacetFilter(ListPanelType.CrmAccountListPanel, ViewName.CrmAccount));
            } else if ("leaverequests".equalsIgnoreCase(entity)) {
                type = ListPanelType.LeaveRequestApprove;
                viewName = ViewName.LeaveRequest;
                quickFilterType = ListPanelType.LeaveRequestQuickFilterForMobile;
                otfType = ListPanelType.LeaveRequestApproveOTF;
                //Initialize Facet Filter
                defaultFilter = rbacService.getLeaveFacetFilterData(initializeDefaultFacetFilter(ListPanelType.LeaveRequestApprove, ViewName.LeaveRequest));
            } else {
                throw new RestException("entity_type incorrect, available options are leads, tasks, opportunities, contacts, companies", "invalid entity", INVALID, HttpStatus.BAD_REQUEST);
            }

            if (id == 0) {
//                FacetFilterRpc defaultFilter = initializeDefaultLeadFacetFilter();
                //Retrieve FACET FILTER data
//                defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, defaultFilter);

                FacetFilterRpc otf = getOneTimeFilter(otfType, defaultFilter);

                defaultFilter.setObjectID(otf.getObjectID());
                defaultFilter.setType(otf.getType());
                otf.getFacetContentMap().forEach((key, value) -> {
                    otf.getFacetContentMap().get(key).getSavedItems().clear();
                    otf.getFacetContentMap().get(key).setFacetItems(new SelectItem[0]);
                });
                otf.setFavourFilter(false);
                //Save Filter
                commonServiceLocal.saveFacetFilter(otf, otfType);

                HashMap<Integer, String> activeFilters = getActiveFilters(quickFilterType);

                FilterTO filterTO = retrieveAndConvertFacetFilter(viewName, type, otf, defaultFilter, activeFilters);

                return successResponse(filterTO);
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Cant reset filter with given id", REQUIRED, HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Apply Filter", description = "Edit the filter. The method is actually for saving the Filter, after the user has configured it with the help of Lead Global Filters Setup. Sent is_active values become is_active for the specified filter on the server, thus saving / editing it.")
    @RequestMapping(value = "/{entity}/filters/{id}/active", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object applyFilter(@PathVariable(value = "entity") String entity, @PathVariable(value = "id") Integer id, @RequestBody ApplyFilterRequestTO applyFilterRequestTO) throws RestException {

        //FacetFilterRpc defaultFilter;
        ListPanelType type;
        ViewName viewName;
        ListPanelType quickFilterType;
        ListPanelType otfType;

        if ("leads".equalsIgnoreCase(entity)) {
            type = ListPanelType.LeadListPanel;
            viewName = ViewName.Lead;
            quickFilterType = ListPanelType.FilteredStatusesForMobile;
            otfType = ListPanelType.LeadListPanelOTF;
            //Initialize Facet Filter
            //defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, initializeDefaultFacetFilter(ListPanelType.LeadListPanel, ViewName.Lead));
        } else if ("tasks".equalsIgnoreCase(entity)) {
            type = ListPanelType.TaskListPanel;
            viewName = ViewName.Task;
            quickFilterType = ListPanelType.TasksQuickFilterForMobile;
            otfType = ListPanelType.TaskListPanelOTF;
            //Initialize Facet Filter
            //defaultFilter = rbacService.getTaskFacetFilterData(initializeDefaultFacetFilter(ListPanelType.TaskListPanel, ViewName.Task), true);
        } else if ("opportunities".equalsIgnoreCase(entity)) {
            type = ListPanelType.OpportunitiesListPanel;
            viewName = ViewName.Opportunity;
            quickFilterType = ListPanelType.OpportunitiesQuickFilterForMobile;
            otfType = ListPanelType.OpportunityListPanelOTF;
            //Initialize Facet Filter
            //defaultFilter = rbacService.getOpportunityFacetFilterData(initializeDefaultFacetFilter(ListPanelType.OpportunitiesListPanel, ViewName.Opportunity));
        } else if ("contacts".equalsIgnoreCase(entity)) {
            type = ListPanelType.ContactListPanel;
            viewName = ViewName.Contact;
            quickFilterType = ListPanelType.ContactsQuickFilterForMobile;
            otfType = ListPanelType.ContactListPanelOTF;
            //Initialize Facet Filter
            //defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_CONTACT, initializeDefaultFacetFilter(ListPanelType.ContactListPanel, ViewName.Contact));
        } else if ("companies".equalsIgnoreCase(entity)) {
            type = ListPanelType.CrmAccountListPanel;
            viewName = ViewName.CrmAccount;
            quickFilterType = ListPanelType.CrmAccountQuickFilterForMobile;
            otfType = ListPanelType.CrmAccountListPanelOTF;
            //Initialize Facet Filter
            //defaultFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_ACCOUNT, initializeDefaultFacetFilter(ListPanelType.CrmAccountListPanel, ViewName.CrmAccount));
        } else if ("leaverequests".equalsIgnoreCase(entity)) {
            //Initialize Facet Filter
            type = ListPanelType.LeaveRequestApprove;
            quickFilterType = ListPanelType.LeaveRequestQuickFilterForMobile;
            otfType = ListPanelType.LeaveRequestApproveOTF;
            viewName = ViewName.LeaveRequest;
        } else {
            throw new RestException("entity_type incorrect, available options are leads, tasks, opportunities, contacts, companies", "invalid entity", INVALID, HttpStatus.BAD_REQUEST);
        }

        if (id > 0) {
            EdsFacetFilter edsFacetFilter = facetFilterManager.getFacetFilter(id);

            if (edsFacetFilter != null) {

//                HashMap<Integer, String> activeFilters = getActiveFilters();

                HashMap<Integer, String> activeFilters = new HashMap<>();

                if (applyFilterRequestTO.isIs_active()) {
                    activeFilters.put(edsFacetFilter.getObjectID(), "");
                } else {
                    if (activeFilters.get(edsFacetFilter.getObjectID()) != null) {
                        activeFilters.remove(edsFacetFilter.getObjectID());
                    }
                }

                saveQuickFilter(quickFilterType, activeFilters);
                //Save OTF
                FacetFilterRpc oneTimeFilter = getOneTimeFilter(otfType, initializeDefaultFacetFilter(type, viewName));
                if (oneTimeFilter != null) {
                    oneTimeFilter.setFavourFilter(false);
                    commonServiceLocal.saveFacetFilter(oneTimeFilter, oneTimeFilter.getType());
                }

                return successResponse(new ResponseData());
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Filter with id = " + id + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } else if (id == 0) {

            saveQuickFilter(quickFilterType, new HashMap<>());
            //Save OTF
            FacetFilterRpc oneTimeFilter = getOneTimeFilter(otfType, initializeDefaultFacetFilter(type, viewName));
            if (oneTimeFilter != null) {
                boolean hasActiveOptions = false;
                if (oneTimeFilter.getStartDate() != null && oneTimeFilter.getEndDate() != null) {
                    hasActiveOptions = Boolean.TRUE;
                }
                if (!hasActiveOptions) {
                    for (Map.Entry<String, FacetContentRpc> entry : oneTimeFilter.getFacetContentMap().entrySet()) {
                        if (entry.getValue().getSavedItems().size() > 0) {
                            hasActiveOptions = Boolean.TRUE;
                        }
                    }
                }
                oneTimeFilter.setFavourFilter(hasActiveOptions/*applyFilterRequestTO.isIs_active()*/);
                commonServiceLocal.saveFacetFilter(oneTimeFilter, oneTimeFilter.getType());
            }
            return successResponse(new ResponseData());
            /*EdsFacetFilter edsFacetFilter = facetFilterManager.getFacetFilter(id);

            if (edsFacetFilter != null) {
                saveQuickFilter(edsFacetFilter.getObjectID(), applyFilterRequestTO.isIs_active());
                return successResponse(new ResponseData());
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Filter with id = " + id + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }*/
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Create new Filter", description = "Creates and saves a new filter, which the user preconfigured.")
    @RequestMapping(value = "/{entity}/filters", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object createNewFilter(@PathVariable(value = "entity") String entity, @RequestBody BaseFilterTO filterTO) throws RestException {

        FacetFilterRpc filterRpc;
        ListPanelType type;
        ViewName viewName;
        ListPanelType quickFilterType;
        ListPanelType otfType;

        if ("leads".equalsIgnoreCase(entity)) {
            type = ListPanelType.LeadListPanel;
//            viewName = ViewName.Lead;
//            quickFilterType = ListPanelType.FilteredStatusesForMobile;
//            otfType = ListPanelType.LeadListPanelOTF;
            //Initialize Facet Filter
            filterRpc = initializeDefaultFacetFilter(ListPanelType.LeadListPanel, ViewName.Lead);
            filterRpc.setFilterChanges(true);
            filterRpc.setShowSolrFieldMap(filterRpc.getShowSolrFieldMap());
            filterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, filterRpc);
        } else if ("tasks".equalsIgnoreCase(entity)) {
            type = ListPanelType.TaskListPanel;
//            viewName = ViewName.Task;
//            quickFilterType = ListPanelType.TasksQuickFilterForMobile;
//            otfType = ListPanelType.TaskListPanelOTF;
            //Initialize Facet Filter
            filterRpc = initializeDefaultFacetFilter(ListPanelType.TaskListPanel, ViewName.Task);
            filterRpc.setFilterChanges(true);
            filterRpc.setShowSolrFieldMap(filterRpc.getShowSolrFieldMap());
            filterRpc = rbacService.getTaskFacetFilterData(filterRpc, true);
        } else if ("opportunities".equalsIgnoreCase(entity)) {
            type = ListPanelType.OpportunitiesListPanel;
//            viewName = ViewName.Opportunity;
//            quickFilterType = ListPanelType.OpportunitiesQuickFilterForMobile;
//            otfType = ListPanelType.OpportunityListPanelOTF;
            //Initialize Facet Filter
            filterRpc = initializeDefaultFacetFilter(ListPanelType.OpportunitiesListPanel, ViewName.Opportunity);
            filterRpc.setFilterChanges(true);
            filterRpc.setShowSolrFieldMap(filterRpc.getShowSolrFieldMap());
            filterRpc = rbacService.getOpportunityFacetFilterData(filterRpc);
        } else if ("contacts".equalsIgnoreCase(entity)) {
            type = ListPanelType.ContactListPanel;
//            viewName = ViewName.Contact;
//            quickFilterType = ListPanelType.ContactsQuickFilterForMobile;
//            otfType = ListPanelType.ContactListPanelOTF;
            //Initialize Facet Filter
            filterRpc = initializeDefaultFacetFilter(ListPanelType.ContactListPanel, ViewName.Contact);
            filterRpc.setFilterChanges(true);
            filterRpc.setShowSolrFieldMap(filterRpc.getShowSolrFieldMap());
            filterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_CONTACT, filterRpc);
        } else if ("companies".equalsIgnoreCase(entity)) {
            type = ListPanelType.CrmAccountListPanel;
//            viewName = ViewName.CrmAccount;
//            quickFilterType = ListPanelType.CrmAccountQuickFilterForMobile;
//            otfType = ListPanelType.CrmAccountListPanelOTF;
            //Initialize Facet Filter
            filterRpc = initializeDefaultFacetFilter(ListPanelType.CrmAccountListPanel, ViewName.CrmAccount);
            filterRpc.setFilterChanges(true);
            filterRpc.setShowSolrFieldMap(filterRpc.getShowSolrFieldMap());
            filterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_ACCOUNT, filterRpc);
        } else if ("leaverequests".equalsIgnoreCase(entity)) {
            //Initialize Facet Filter
            filterRpc = initializeDefaultFacetFilter(ListPanelType.LeaveRequestApprove, ViewName.LeaveRequest);
            filterRpc.setFilterChanges(true);
            filterRpc.setShowSolrFieldMap(filterRpc.getShowSolrFieldMap());
            filterRpc = rbacService.getLeaveFacetFilterData(filterRpc);
            type = ListPanelType.LeaveRequestApprove;
        } else {
            throw new RestException("entity_type incorrect, available options are leads, tasks, opportunities, contacts, companies", "invalid entity", INVALID, HttpStatus.BAD_REQUEST);
        }

        //Initialize Facet Filter
        //FacetFilterRpc filterRpc = initializeDefaultLeadFacetFilter();
        //End Of main facet filter initialization

        /*filterRpc.setFilterChanges(true);
        filterRpc.setShowSolrFieldMap(filterRpc.getShowSolrFieldMap());
        filterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, filterRpc);*/

        if (filterTO != null) {

            filterRpc.setName(filterTO.getName());
            filterRpc.setDefaultFilter(Boolean.TRUE.equals(filterTO.getIs_default()));
            filterRpc.setPublicFilter(Boolean.TRUE.equals(filterTO.getIs_public()));
            filterRpc.setName(filterTO.getName());

            //Date Period
            setDatePeriod(filterTO, filterRpc);
            //End Of Date Period

            HashMap<String, String> categoriesMap = new HashMap<>();

            for (FilterCategoryTO filterCategoryTO : filterTO.getFilter_categories()) {

                if (StringUtils.isNotBlank(filterCategoryTO.getCategory_code())) {

                    categoriesMap.put(filterCategoryTO.getCategory_code(), filterCategoryTO.getCategory_code());

                    if (filterRpc.getFacetContentMap().get(filterCategoryTO.getCategory_code()) != null) {
                        //Clear existing
                        filterRpc.getFacetContentMap().get(filterCategoryTO.getCategory_code()).getSavedItems().clear();
                        ArrayList<SelectItem> facetItems = new ArrayList<>();

                        if (filterCategoryTO.getSub_categories() != null) {
                            for (SubCategoryTO subCategoryTO : filterCategoryTO.getSub_categories()) {

                                if (subCategoryTO.getId() == null || subCategoryTO.getId() == 0) {
                                    subCategoryTO.setId(-1);
                                }

                                if (Boolean.TRUE.equals(subCategoryTO.getIs_active())) {
                                    //Set Saved Items
                                    filterRpc.getFacetContentMap().get(filterCategoryTO.getCategory_code()).getSavedItems().put(subCategoryTO.getId(), subCategoryTO.getId());

                                    //Facet item
                                    SelectItem item = new SelectItem(subCategoryTO.getId(), subCategoryTO.getName());
                                    item.setTotalCount(subCategoryTO.getTotal_count());
                                    facetItems.add(item);
                                }
                            }
                        }

                        filterRpc.getFacetContentMap().get(filterCategoryTO.getCategory_code()).setFacetItems(facetItems.toArray(new SelectItem[0]));

                    } else {
                        //If Not exist in filter
                        FacetContentRpc newCategory = new FacetContentRpc();
                        ArrayList<SelectItem> facetItems = new ArrayList<>();

                        if (filterCategoryTO.getSub_categories() != null) {
                            for (SubCategoryTO subCategoryTO : filterCategoryTO.getSub_categories()) {
                                if (Boolean.TRUE.equals(subCategoryTO.getIs_active())) {
                                    if (subCategoryTO.getId() == null || subCategoryTO.getId() <= 0) {
                                        subCategoryTO.setId(-1);
                                    }
                                    newCategory.getSavedItems().put(subCategoryTO.getId(), subCategoryTO.getId());

                                    SelectItem item = new SelectItem(subCategoryTO.getId(), subCategoryTO.getName());
                                    item.setTotalCount(subCategoryTO.getTotal_count());
                                    facetItems.add(item);
                                }
                            }
                        }
                        newCategory.setFacetItems(facetItems.toArray(new SelectItem[0]));
                        filterRpc.getFacetContentMap().put(filterCategoryTO.getCategory_code(), newCategory);
                    }
                }

            }
            //Remove missing categories
            for (String categoryName : filterRpc.getFacetContentMap().keySet()) {
                if (categoriesMap.get(categoryName) == null) {
                    filterRpc.getFacetContentMap().get(categoryName).setFacetItems(new SelectItem[0]);
                    filterRpc.getFacetContentMap().get(categoryName).getSavedItems().clear();
                }
            }

            //Save Filter
            commonServiceLocal.saveFacetFilter(filterRpc, type);

            return successResponse(new ResponseData());
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Filter data empty", REQUIRED, HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Delete Filter", description = "Removes a filter by id.")
    @RequestMapping(value = "/{entity}/filters/{id}", method = RequestMethod.DELETE)
    public Object deleteFilter(@PathVariable(value = "id") Integer id) throws RestException {

        if (id > 0) {
            EdsFacetFilter edsFacetFilter = facetFilterManager.getFacetFilter(id);

            if (edsFacetFilter != null) {
                //Delete Filter
                commonServiceLocal.deleteFacetFilter(edsFacetFilter.getObjectID());
                return successResponse(new ResponseData());
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Filter with id = " + id + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }

    private Integer saveQuickFilter(ListPanelType quickFilterType, HashMap<Integer, String> activeFilters) {
        FacetFilterRpc facetFilterToSave = new FacetFilterRpc();
        facetFilterToSave.setType(quickFilterType);
//      EdsFacetFilter edsFacetFilter = facetFilterManager.getDefaultUserFacetFilter(ListPanelType.FilteredStatusesForMobile, userManager.getUser());
        facetFilterToSave = commonServiceLocal.getUserFacetFilter(facetFilterToSave);
        facetFilterToSave.setDefaultFilter(true);

        FacetContentRpc categoriesContentRpc = facetFilterToSave.getFacetContentMap().get("categories");
        if (categoriesContentRpc == null) {
            categoriesContentRpc = new FacetContentRpc();
        }


//        categoriesContentRpc.setFacetItems(existingFiltersMap.values().toArray(new SelectItem[0]));
        ArrayList<SelectItem> newFilters = new ArrayList<>();
        activeFilters.keySet().forEach(filterid -> newFilters.add(new SelectItem(filterid, "")));
        categoriesContentRpc.setFacetItems(newFilters.toArray(new SelectItem[0]));

        facetFilterToSave.getFacetContentMap().put("categories", categoriesContentRpc);

        facetFilterToSave.setUserID(userManager.getUser().getObjectID());
        facetFilterToSave.setDefaultFilter(true);
        return commonServiceLocal.saveFacetFilter(facetFilterToSave, quickFilterType);
    }

    private void setDatePeriod(BaseFilterTO filterTO, FacetFilterRpc filterRpc) {
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        if (filterTO.getDate_period() != null) {
            if (StringUtils.isNotBlank(filterTO.getDate_period().getDate_from())) {
                try {
                    filterRpc.setStartDate(longDateTimezoneFormat.parse(filterTO.getDate_period().getDate_from()));
                } catch (ParseException e) {
                    log.error("", e);
                    filterRpc.setStartDate(null);
                }
            }
            if (StringUtils.isNotBlank(filterTO.getDate_period().getDate_to())) {
                try {
                    filterRpc.setEndDate(longDateTimezoneFormat.parse(filterTO.getDate_period().getDate_to()));
                } catch (ParseException e) {
                    log.error("", e);
                    filterRpc.setEndDate(null);
                }
            }

            if ("DATE_CREATED".equalsIgnoreCase(filterTO.getDate_period().getFilter_by())) {
                filterRpc.setSelectedDateSolrCodeName(SolrContactRepresenter.FIELD_CREATION_DATE);
            } else if ("DATE_UPDATED".equalsIgnoreCase(filterTO.getDate_period().getFilter_by())) {
                filterRpc.setSelectedDateSolrCodeName(SolrContactRepresenter.FIELD_UPDATE_DATE);
            } else {
                filterRpc.setSelectedDateSolrCodeName(null);
//                filterRpc.setSelectedDateSolrCodeName(SolrContactRepresenter.FIELD_UPDATE_DATE);
            }
        } else {
            filterRpc.setStartDate(null);
            filterRpc.setEndDate(null);
            filterRpc.setSelectedDateSolrCodeName(null);
        }
    }

    private FacetFilterRpc initializeDefaultFacetFilter(ListPanelType type, ViewName viewName) {
        //Initialize Facet Filter
        final FacetFilterRpc mainFilter = ListingFilterHelper.createFilterParameter(servletRequest, type).getFacetFilter();
        //new FacetFilterRpc(ListPanelType.LeadListPanel, showSolrFieldMap, showFacetCodeName);

        //Custom fields which are facetable
        ArrayList<CompanyCustomFieldItem> companyCustomFields = commonServiceLocal.getCompanyCustomFieldsForListView(viewName/*ViewName.Lead*/);

        if (companyCustomFields != null && companyCustomFields.size() > 0) {
            companyCustomFields.forEach(companyCustomFieldItem -> {
                if (!CompanyCustomFieldItem.DATE.equals(companyCustomFieldItem.getDataType()) && companyCustomFieldItem.isFacetable() && StringUtils.isNotBlank(companyCustomFieldItem.getColumnCode())) {
                    //we must add this condition otherwise it will add again and again into map (static block inside other class)
                    if (!mainFilter.getShowFacetCodeName().contains(companyCustomFieldItem.getColumnCode())) {
                        mainFilter.getShowFacetCodeName().add(companyCustomFieldItem.getColumnCode());
                    }
                    FacetSolrField solrField = new FacetSolrField(companyCustomFieldItem.getColumnCode().toUpperCase(), companyCustomFieldItem.getColumnCode().toLowerCase());
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

    private HashMap<Integer, String> getActiveFilters(ListPanelType type) {
        EdsUser currentUser = userManager.getUser();
        EdsFacetFilter edsFacetFilter = facetFilterManager.getDefaultUserFacetFilter(type, currentUser);
        HashMap<Integer, String> activeFilters = new HashMap<>();
        if (edsFacetFilter != null) {
            HashSet<String> colNames = new HashSet<>();
            colNames.add("categories");
            FacetFilterRpc facetFilterRpc = edsFacetFilter.getFacetFilter(colNames);

            FacetContentRpc c = facetFilterRpc.getFacetContentMap().get("categories");
            if (c != null) {
                for (SelectItem cI : c.getFacetItems()) {
                    activeFilters.put(cI.getId(), "");
                }
            }
        }
        return activeFilters;
    }

    private FacetFilterRpc getOneTimeFilter(ListPanelType type, FacetFilterRpc defaultFilter) {

        if (defaultFilter != null) {
            defaultFilter.setType(type);
            defaultFilter.setUserID(userManager.getUser().getObjectID());

            FacetFilterRpc otf = commonServiceLocal.getUserFacetFilter(defaultFilter);
            otf.setName("OTF");
            otf.setDefaultFilter(true);
            otf.setType(type);

            if (otf.getObjectID() != null) {
                EdsUserFilter edsUserFilter = userFilterManager.getByFacetFilterId(otf.getObjectID());
                if (edsUserFilter != null) {
                    otf.setFavourFilter(Boolean.TRUE.equals(edsUserFilter.getFavour()));
                }
            }

            return otf;
        } else {
            return null;
        }

    }

    private FilterTO retrieveAndConvertFacetFilter(ViewName viewName, ListPanelType type, EdsFacetFilter edsFacetFilter, FacetFilterRpc defaultFilter, HashMap<Integer, String> activeFilters) {
        if (edsFacetFilter != null) {
            FilterTO filterTO = new FilterTO();
            filterTO.setFilter_id(edsFacetFilter.getObjectID());
            filterTO.setName(edsFacetFilter.getName());
            filterTO.setIs_active(activeFilters.get(edsFacetFilter.getObjectID()) != null);

            FacetFilterRpc filterRpc = edsFacetFilter.getFacetFilter(new HashSet<>(defaultFilter.getShowFacetCodeName()));

            filterRpc.setFilterChanges(true);
            filterRpc.setShowSolrFieldMap(defaultFilter.getShowSolrFieldMap());
            //Retrieve FACET FILTER data
            if (ListPanelType.LeadListPanel.equals(type)) {
                filterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, filterRpc);
            } else if (ListPanelType.TaskListPanel.equals(type)) {
                filterRpc = rbacService.getTaskFacetFilterData(filterRpc, true);
            } else if (ListPanelType.OpportunitiesListPanel.equals(type)) {
                filterRpc = rbacService.getOpportunityFacetFilterData(initializeDefaultFacetFilter(ListPanelType.OpportunitiesListPanel, ViewName.Opportunity));
            } else if (ListPanelType.ContactListPanel.equals(type)) {
                filterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_CONTACT, initializeDefaultFacetFilter(ListPanelType.ContactListPanel, ViewName.Contact));
            } else if (ListPanelType.CrmAccountListPanel.equals(type)) {
                filterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_ACCOUNT, initializeDefaultFacetFilter(ListPanelType.CrmAccountListPanel, ViewName.CrmAccount));
            } /*else if (ListPanelType.LeaveRequestApprove.equals(type)) {
                filterRpc = rbacService.getLeaveFacetFilterData(initializeDefaultFacetFilter(ListPanelType.LeaveRequestApprove, ViewName.LeaveRequest));
            }*/

            EdsUserFilter edsUserFilter = userFilterManager.getByFacetFilterId(edsFacetFilter.getObjectID());
            if (edsUserFilter != null) {
                filterTO.setIs_default(edsUserFilter.getIsDefault());
                filterTO.setIs_public(edsFacetFilter.isSystemFilter());
            }

            filterTO = fillFilterCategories(viewName, filterRpc, filterTO, defaultFilter);

            return filterTO;
        } else {
            return null;
        }
    }

    private FilterTO retrieveAndConvertFacetFilter(ViewName viewName, ListPanelType type, FacetFilterRpc facetFilterRpc, FacetFilterRpc defaultFilter, HashMap<Integer, String> activeFilters) {
        if (facetFilterRpc != null) {
            FilterTO filterTO = new FilterTO();
            //#70 1) Please don't send field "name" for OTF, it doesn't have name
            if (!ListPanelType.LeadListPanelOTF.equals(facetFilterRpc.getType())
                    && !ListPanelType.TaskListPanelOTF.equals(facetFilterRpc.getType())
                    && !ListPanelType.OpportunityListPanelOTF.equals(facetFilterRpc.getType())
                    && !ListPanelType.ContactListPanelOTF.equals(facetFilterRpc.getType())
                    && !ListPanelType.CrmAccountListPanelOTF.equals(facetFilterRpc.getType())) {
                filterTO.setName(facetFilterRpc.getName());
            }
//            filterTO.setIs_active(activeFilters.get(facetFilterRpc.getObjectID())!=null);

//            FacetFilterRpc filterRpc = edsFacetFilter.getFacetFilter(new HashSet<>(mainFilter.getShowFacetCodeName()));

            facetFilterRpc.setFilterChanges(true);
            facetFilterRpc.setShowSolrFieldMap(facetFilterRpc.getShowSolrFieldMap());
//            log.info("Filter 11: " + WfmJsonUtils.facetFilrerConvertToJsonData(facetFilterRpc));
            //Reretrieve FACET FILTER data
            if (ListPanelType.LeadListPanel.equals(type) || ListPanelType.LeadListPanelOTF.equals(type)) {
                facetFilterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, facetFilterRpc);
            } else if (ListPanelType.TaskListPanel.equals(type) || ListPanelType.TaskListPanelOTF.equals(type)) {
                facetFilterRpc = rbacService.getTaskFacetFilterData(facetFilterRpc, true);
            } else if (ListPanelType.OpportunitiesListPanel.equals(type) || ListPanelType.OpportunityListPanelOTF.equals(type)) {
                facetFilterRpc = rbacService.getOpportunityFacetFilterData(facetFilterRpc);
            } else if (ListPanelType.ContactListPanel.equals(type) || ListPanelType.ContactListPanelOTF.equals(type)) {
                facetFilterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_CONTACT, facetFilterRpc);
            } else if (ListPanelType.CrmAccountListPanel.equals(type) || ListPanelType.CrmAccountListPanelOTF.equals(type)) {
                facetFilterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_ACCOUNT, facetFilterRpc);
            } else if (ListPanelType.LeaveRequestApprove.equals(type) || ListPanelType.LeaveRequestApproveOTF.equals(type)) {
                facetFilterRpc = rbacService.getCRMFacetFilterData(CrmConstants.CRM_ACCOUNT, facetFilterRpc);
            }
//            log.info("Filter 22: " + WfmJsonUtils.facetFilrerConvertToJsonData(facetFilterRpc));
            filterTO.setIs_default(Boolean.TRUE.equals(facetFilterRpc.isDefaultFilter()));
            filterTO.setIs_public(Boolean.TRUE.equals(facetFilterRpc.isPublicFilter()));

            if (ListPanelType.LeadListPanelOTF.equals(facetFilterRpc.getType())
                    || ListPanelType.TaskListPanelOTF.equals(facetFilterRpc.getType())
                    || ListPanelType.OpportunityListPanelOTF.equals(facetFilterRpc.getType())
                    || ListPanelType.ContactListPanelOTF.equals(facetFilterRpc.getType())
                    || ListPanelType.CrmAccountListPanelOTF.equals(facetFilterRpc.getType())
                    || ListPanelType.LeaveRequestApproveOTF.equals(facetFilterRpc.getType())
            ) {
                filterTO.setFilter_id(0);
                filterTO.setIs_active(isOTFActive(facetFilterRpc, activeFilters));
            } else {
                filterTO.setFilter_id(facetFilterRpc.getObjectID());
                filterTO.setIs_active(activeFilters.get(facetFilterRpc.getObjectID()) != null);
            }

//            log.info("Filter 2: " + WfmJsonUtils.facetFilrerConvertToJsonData(facetFilterRpc));

            filterTO = fillFilterCategories(viewName, facetFilterRpc, filterTO, defaultFilter);

            return filterTO;
        } else {
            return null;
        }
    }

    private FilterTO fillFilterCategories(ViewName viewName, FacetFilterRpc filterRpc, FilterTO filterTO, FacetFilterRpc defaultFilter) {
        if (filterRpc != null && !filterRpc.getFacetContentMap().isEmpty()) {
            for (Map.Entry<String, FacetContentRpc> entry : filterRpc.getFacetContentMap().entrySet()) {

                String categoryCode = getLocalizedMessages(viewName, entry.getKey(), entry.getKey());
                if (categoryCode.startsWith("string_value") || categoryCode.startsWith("double_value") || categoryCode.startsWith("date_value")) {
                    continue;
                }
                FilterCategoryTO filterCategoryTO = new FilterCategoryTO(entry.getKey(), categoryCode);

                //We must return all available options for status/stage fields
                if ((
                        ListPanelType.LeadListPanel.equals(filterRpc.getType()) || ListPanelType.LeadListPanelOTF.equals(filterRpc.getType())
                                || ListPanelType.OpportunitiesListPanel.equals(filterRpc.getType()) || ListPanelType.OpportunityListPanelOTF.equals(filterRpc.getType())
                                || ListPanelType.TaskListPanel.equals(filterRpc.getType()) || ListPanelType.TaskListPanelOTF.equals(filterRpc.getType())
                                || ListPanelType.LeaveRequestApprove.equals(filterRpc.getType()) || ListPanelType.LeaveRequestApproveOTF.equals(filterRpc.getType())

                )
                        && (
                        FacetContentType.LeadFacetFilter.getContentCode()[2].equalsIgnoreCase(entry.getKey())
                                || FacetContentType.OpportunityFacetFilter.getContentCode()[0].equalsIgnoreCase(entry.getKey())
                                || FacetContentType.TaskFacetFilter.getContentCode()[3].equalsIgnoreCase(entry.getKey())
                                || FacetContentType.LeaveFacetFilter.getContentCode()[2].equalsIgnoreCase(entry.getKey())
                )) {

                    FacetContentRpc defaultFilterSubcategory = (defaultFilter != null) ? defaultFilter.getFacetContentMap().get(entry.getKey()) : null;

                    if (defaultFilterSubcategory != null && defaultFilterSubcategory.getFacetItems().length > 0) {

                        //We must set is_active for CATEGORY only if all items are selected
                        filterCategoryTO.setIs_active(defaultFilterSubcategory.getFacetItems().length == entry.getValue().getSavedItems().size() && entry.getValue().getSavedItems().size() > 0);

                        ArrayList<SubCategoryTO> subCategories = new ArrayList<>();

                        Map<Integer, SelectItem> filteredStatusesMap = Arrays.stream(entry.getValue().getFacetItems()).collect(
                                Collectors.toMap(SelectItem::getId, item -> item, (item1, item2) -> {
                                    log.info("duplicate key found! - " + item1);
                                    return item1;
                                }));
                        for (SelectItem facetItem : defaultFilterSubcategory.getFacetItems()) {
                            subCategories.add(
                                    new SubCategoryTO(facetItem.getId() == -1 ? 0 : facetItem.getId(),
                                            facetItem.getName() != null ? facetItem.getName() : "N/A",
                                            entry.getValue().getSavedItems().get(facetItem.getId()) != null,
                                            filteredStatusesMap.get(facetItem.getId()) != null && filteredStatusesMap.get(facetItem.getId()).getTotalCount() != null ? filteredStatusesMap.get(facetItem.getId()).getTotalCount() : 0)
                            );
                        }

                        filterCategoryTO.setSub_categories(subCategories);
                    }
                } else {
                    if (entry.getValue() != null && entry.getValue().getFacetItems() != null && entry.getValue().getFacetItems().length > 0) {
                        //We must set is_active only if all items are selected
                        filterCategoryTO.setIs_active(entry.getValue().getFacetItems().length == entry.getValue().getSavedItems().size() && entry.getValue().getFacetItems().length > 0);

                        ArrayList<SubCategoryTO> subCategories = new ArrayList<>();

                        Arrays.asList(entry.getValue().getFacetItems()).forEach(facetItem -> subCategories.add(new SubCategoryTO(facetItem.getId() == -1 ? 0 : facetItem.getId(),
                                StringUtils.isNotBlank(facetItem.getName()) ? facetItem.getName() : "N/A",
                                entry.getValue().getSavedItems().get(facetItem.getId()) != null,
                                facetItem.getTotalCount())));

                        filterCategoryTO.setSub_categories(subCategories);
                    }
                }

                filterTO.getFilter_categories().add(filterCategoryTO);

            }

            //Date Period
            if (true/*filterRpc.getStartDate()!=null && filterRpc.getEndDate()!=null && StringUtils.isNotBlank(filterRpc.getSelectedDateSolrCodeName())*/) {

                SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

                DatePeriodTO datePeriodTO = new DatePeriodTO(filterRpc.getStartDate() != null ? longDateTimezoneFormat.format(filterRpc.getStartDate()) : null,
                        filterRpc.getEndDate() != null ? longDateTimezoneFormat.format(filterRpc.getEndDate()) : null,
                        filterRpc.getSelectedDateSolrCodeName());
                if (SolrContactRepresenter.FIELD_CREATION_DATE.equalsIgnoreCase(filterRpc.getSelectedDateSolrCodeName())) {
                    datePeriodTO.setFilter_by("DATE_CREATED");
                } else if (SolrContactRepresenter.FIELD_UPDATE_DATE.equalsIgnoreCase(filterRpc.getSelectedDateSolrCodeName())) {
                    datePeriodTO.setFilter_by("DATE_UPDATED");
                } else {
                    datePeriodTO.setFilter_by("NOT_SELECTED");
                }
                filterTO.setDate_period(datePeriodTO);
            }
        }
        return filterTO;
    }


    private boolean isOTFActive(FacetFilterRpc otf, HashMap<Integer, String> activeFilters) {
        //if OTF is active then other filters are not active
        if (activeFilters == null || activeFilters.size() == 0) {
            //set if OTF is active or not (consider itf_active after we apply it with some values)
            /*for( Map.Entry<String, FacetContentRpc> entry : otf.getFacetContentMap().entrySet()) {
                if(entry.getValue()!=null && entry.getValue().getSavedItems()!=null && entry.getValue().getSavedItems().size()>0) {
                    return true;
                }
            }*/
            return otf.isFavourFilter();
        }
        return false;
    }

    private Integer saveFilter(ListPanelType quickFilterType, ArrayList<SelectItem> people, ArrayList<SelectItem> categories) {
        FacetFilterRpc facetFilterToSave = new FacetFilterRpc();
        facetFilterToSave.setType(quickFilterType);


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
        return commonServiceLocal.saveFacetFilter(facetFilterToSave, quickFilterType);
    }
}
