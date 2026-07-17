package com.edatasite.workforce.rest.v2.release10.pm;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsFacetFilter;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsUserFilter;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.FacetFilterManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.UserFilterManager;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.aspects.CheckPermissionException;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ListingFilterHelper;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ItemInStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.LeadInStatusResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskListItemTO;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.UndeclaredThrowableException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Dilshod Madrahimov on 11/28/2017.
 */

@Tag(name = "Task Filters", description = "Task Filters API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiTaskFilterControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiTaskFilterControllerV2.class);
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private FacetFilterManager facetFilterManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private UserFilterManager userFilterManager;
    @Autowired
    private HttpServletRequest servletRequest;

    @Operation(summary = "Get info about Tasks in status", description = "Retrieves filtered users based on is_active filters from quick_filter")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of filtered users"),
            @ApiResponse(responseCode = "400", description = "status_id, offset and count are required"),
            @ApiResponse(responseCode = "422", description = "count and offset should be more than zero")})
    @RequestMapping(value = "/tasks/items_in_status", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @CheckPermission(permissions = {PermissionConstants.PM_MAIN_MENU, PermissionConstants.PM_TASKS_LIST})
    public Object tasksInStatus(@RequestBody ItemInStatusTO itemInStatus) throws RestException {
        if (itemInStatus.getStatus_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Status id required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (itemInStatus.getOffset() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Offset required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (itemInStatus.getOffset() < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Offset can not be less then zero", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (itemInStatus.getCount() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Count required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (itemInStatus.getCount() < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Count can not be less then zero", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (itemInStatus.getOffset() == 0 && itemInStatus.getCount() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Offset and count can not be zero at the same time", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        OrderFieldEnum orderFieldEnum = null;
        OrderByEnum orderByEnum = null;
        if (itemInStatus.getOrder() != null) {
            if (StringUtils.isNotBlank(itemInStatus.getOrder().getType())) {
                orderFieldEnum = OrderFieldEnum.getOrderField(itemInStatus.getOrder().getType());
                if (orderFieldEnum == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Type field should be one of ID, NAME, DATE, COMPANY", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            if (StringUtils.isNotBlank(itemInStatus.getOrder().getDirection())) {
                orderByEnum = OrderByEnum.getDirection(itemInStatus.getOrder().getDirection());
                if (orderByEnum == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Direction field should be one of ASC or DESC", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
        }

        EdsReference requestedStatus = null;
        if (itemInStatus.getStatus_id() > 0) {
            requestedStatus = referenceManager.get(itemInStatus.getStatus_id());
            if (requestedStatus == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Status with " + itemInStatus.getStatus_id() + " id not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        }

        ListingFilterParameter filterParameter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.TaskListPanel);

        HashMap<String, FacetContentRpc> facetContentMap = filterParameter.getFacetFilter().getFacetContentMap();

        ArrayList<SelectItem> statusFacetItems = new ArrayList<SelectItem>();
        if (requestedStatus != null) {
            statusFacetItems.add(new SelectItem(requestedStatus.getObjectID(), requestedStatus.getName()));
        } else {
            statusFacetItems.add(new SelectItem(-1, "N/A"));
        }

        facetContentMap.get(FacetContentType.TaskFacetFilter.getContentCode()[3]).setFacetItems(statusFacetItems.toArray(new SelectItem[0]));

        //quickfilter
        ArrayList<SelectItem> assignedToFacetItems = new ArrayList<>();
        EdsUser currentUser = userManager.getUser();
        EdsFacetFilter edsQuickFilter = facetFilterManager.getDefaultUserFacetFilter(ListPanelType.TasksQuickFilterForMobile, currentUser);
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
                    facetContentMap.get(FacetContentType.TaskFacetFilter.getContentCode()[5]).setFacetItems(assignedToFacetItems.toArray(new SelectItem[0]));
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
                                        && !FacetContentType.TaskFacetFilter.getContentCode()[3].equalsIgnoreCase(entry.getKey())
                                        && !FacetContentType.TaskFacetFilter.getContentCode()[5].equalsIgnoreCase(entry.getKey())) {
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
                    }
                }
            }
        }
        //end of quickfilter
        //If OneTimeFilter is active
        FacetFilterRpc oneTimeFilter = getOneTimeFilter(initializeDefaultTaskFacetFilter());
        if (oneTimeFilter != null && oneTimeFilter.isFavourFilter()) {
            //Important to pass facetcodenames
            //FacetFilterRpc filterRpc = oneTimeFilter.getFacetFilter(new HashSet<>(filterParameter.getFacetFilter().getShowFacetCodeName()));
            if (!oneTimeFilter.getFacetContentMap().isEmpty()) {
                for (Map.Entry<String, FacetContentRpc> entry : oneTimeFilter.getFacetContentMap().entrySet()) {
                    if (entry.getValue().getFacetItems().length > 0
                            && !FacetContentType.TaskFacetFilter.getContentCode()[3].equalsIgnoreCase(entry.getKey())
                            && !FacetContentType.TaskFacetFilter.getContentCode()[5].equalsIgnoreCase(entry.getKey())) {
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
        }
        //End of If OneTimeFilter is active


        /*Map<Integer, Integer> savedItems = new HashMap<>();
        savedItems.put(leadInStatus.getStatus_id(), leadInStatus.getStatus_id());
        facetContentMap.get(FacetContentType.LeadFacetFilter.getContentCode()[2]).setSavedItems(savedItems);*/

        filterParameter.getFacetFilter().setFacetContentMap(facetContentMap);

        ArrayList<String> columnCodeNames = filterParameter.getFacetFilter().getShowFacetCodeName();//@TODO need to check this
        ListPanelToolRpc panelTools = new ListPanelToolRpc();
//        panelTools.setColumnCodeName(columnCodeNames);
        panelTools.setColumnCodeName(new ArrayList<>(Arrays.asList(TaskListItem.NUMBER, TaskListItem.NAME,// default show column code
                TaskListItem.PROJECT_NAME, TaskListItem.CLIENT,
                TaskListItem.PRIORITY_NAME, TaskListItem.STATUS_NAME,
                TaskListItem.START_DATE, TaskListItem.DUE_DATE,
                TaskListItem.COMPLETE, TaskListItem.DESCRIPTION, TaskListItem.LAST_MODIFIED)));
        panelTools.setShowPopup(true);
        filterParameter.setListPanelTool(panelTools);
        filterParameter.setColumnsOfListing(columnCodeNames);

        filterParameter.setStart(itemInStatus.getOffset());
        filterParameter.setLimit(itemInStatus.getCount());
        filterParameter.setSearchButton(false);
        filterParameter.setDetectDuplicates(false);
        filterParameter.setWithImage(true);
        if (orderFieldEnum != null) {
            filterParameter.setSortField(getSortField(orderFieldEnum, ListPanelType.TaskListPanel));
        }
        filterParameter.setAscending(orderByEnum == null || OrderByEnum.ASC.getDirection().equals(orderByEnum.getDirection()));
        filterParameter.setSortDir(orderByEnum != null ? orderByEnum.getId() : OrderByEnum.ASC.getId());

        //Custom fields
        //Custom fields which are facetable
        ArrayList<CompanyCustomFieldItem> taskCustomFields = commonServiceLocal.getCompanyCustomFieldsForListView(ViewName.Task);

        if (taskCustomFields != null && taskCustomFields.size() > 0) {
            taskCustomFields.forEach(companyCustomFieldItem -> {
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


        TaskList result;
        try {
            result = taskServiceLocal.getTaskList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            if (((UndeclaredThrowableException) e).getUndeclaredThrowable() instanceof CheckPermissionException) {
                throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        LeadInStatusResultTO leadInStatusResult = new LeadInStatusResultTO();
        leadInStatusResult.setStatus_id(itemInStatus.getStatus_id() > 0 ? itemInStatus.getStatus_id() : 0);
        leadInStatusResult.setTotal_count(result.getTotal());
        if (result.getTotal() < (itemInStatus.getCount() + itemInStatus.getOffset())) {
            leadInStatusResult.setLeft(0);
        } else {
            leadInStatusResult.setLeft(result.getTotal() - (itemInStatus.getOffset() + itemInStatus.getCount()));
        }
        leadInStatusResult.setCount(result.getList() != null ? result.getList().size() : 0);
        leadInStatusResult.setOffset(itemInStatus.getOffset());

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        ArrayList<TaskListItemTO> tasks = new ArrayList<>();
        for (TaskListItem item : result.getList()) {
            TaskListItemTO task = new TaskListItemTO();
            task.setName(item.getName());
            if (StringUtils.isNotBlank(item.getDescription())) {
                task.setDescription(item.getDescription());
            }
            task.setItem_id(item.getObjectID());
            //We set status_id which must be same as we took from request
            if (item.getTaskStatusId() != null) {
                task.setStatus_id(item.getTaskStatusId());
            } else {
                task.setStatus_id(0);
            }
            if (item.getDueDate() != null) {
                task.setDue_date(longDateTimezoneFormat.format(item.getDueDate()));
            }
            if (EdsTask.LOW.equals(item.getPriorityCode())) {
                task.setPriority("LOW");
            } else if (EdsTask.MEDIUM.equals(item.getPriorityCode())) {
                task.setPriority("MEDIUM");
            } else if (EdsTask.HIGH.equals(item.getPriorityCode())) {
                task.setPriority("HIGH");
            }

            tasks.add(task);
        }
        leadInStatusResult.setList(tasks);

        return successResponse(leadInStatusResult);
    }

    private FacetFilterRpc initializeDefaultTaskFacetFilter() {
        //Initialize Facet Filter
        final FacetFilterRpc mainFilter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.TaskListPanel).getFacetFilter();
        //new FacetFilterRpc(ListPanelType.LeadListPanel, showSolrFieldMap, showFacetCodeName);

        //Custom fields which are facetable
        ArrayList<CompanyCustomFieldItem> leadCustomFields = commonServiceLocal.getCompanyCustomFieldsForListView(ViewName.Task);

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

        defaultFilter.setType(ListPanelType.TaskListPanelOTF);
        defaultFilter.setUserID(userManager.getUser().getObjectID());

        FacetFilterRpc otf = commonServiceLocal.getUserFacetFilter(defaultFilter);
        otf.setName("OTF");
        otf.setDefaultFilter(true);
        otf.setType(ListPanelType.TaskListPanelOTF);

        if (otf.getObjectID() != null) {
            EdsUserFilter edsUserFilter = userFilterManager.getByFacetFilterId(otf.getObjectID());
            if (edsUserFilter != null) {
                otf.setFavourFilter(Boolean.TRUE.equals(edsUserFilter.getFavour()));
            }
        }
        return otf;
    }

}
