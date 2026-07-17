package com.edatasite.workforce.rest.v2.release10.crm;

import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsReferenceColor;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsFacetFilter;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsUserFilter;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.ModelFieldManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.EmailRepository;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.FacetFilterManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.UserFilterManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmAccountList;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;
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
import com.edatasite.workforce.rest.base.enums.NoteEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ListingFilterHelper;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.ModelFieldLocalizer;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.PhoneTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CustomFieldTo;
import com.edatasite.workforce.rest.v2.release10.core.to.base.EntityCategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.StageTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldCategoryChooseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldNumberTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldTextTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ChangeOpportunityStatusRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CompanyOpportunityTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EntityInformationResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.FilteredStatusItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ItemInStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.OpportunityAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.OpportunityDetailsItemResponseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.OpportunityDetailsItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.OpportunityEditTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.OpportunityInformationTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.OpportunityResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.OpportunitySubItemsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.OpportunityTO;
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
import com.edatasite.workforce.rest.v2.release10.enums.TaskPresenceEnum;
import com.edatasite.workforce.rest.v2.release10.enums.TaskPriorityEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.CRM_OPPORTUNITY_NUMBER;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.CRM_OPPORTUNITY_TYPE;
import static com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants.BRAND;
import static com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants.CATEGORY;
import static com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants.DESCRIPTION;
import static com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants.DISCOUNT_AMT;
import static com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants.MEASUREMENT;
import static com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants.NET_AMT;
import static com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants.QTY;
import static com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants.TAX_LIST;
import static com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants.TOTAL_AMT;
import static com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants.UNITPRICE;

/**
 * Created by Farrukh Abdurakhmonov on 12/26/2017.
 */
@Tag(name = "Opportunity", description = "Opportunity API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiOpportunityControllerV2 extends BaseApiControllerV2 implements Constants {
    private static final Logger log = LoggerFactory.getLogger(ApiOpportunityControllerV2.class);

    private boolean opportunityType = false;

    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private FacetFilterManager facetFilterManager;
    @Autowired
    private HttpServletRequest servletRequest;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private NoteServiceLocal noteServiceLocal;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private ModelFieldManager modelFieldManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private UserFilterManager userFilterManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private ModelFieldLocalizer modelFieldLocalizer;
    @Autowired
    private ItemTableSettingService itemTableSettingService;


    @Operation(summary = "Search Opportunity", description = "Searches the opportunities based on provided data")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have found opportunities."),
            @ApiResponse(responseCode = "400", description = "query, limit and offset fields are required"),
            @ApiResponse(responseCode = "400", description = "limit and offset cannot be zero at the same time")})
    @RequestMapping(value = "/opportunities/search", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.CRM_OPPORTUNITIES_LIST})
    public Object searchOpportunity(@RequestParam(value = "query") String query,
                                    @RequestParam(value = "limit", required = false) Integer limit,
                                    @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        OpportunityResultTO opportunityResult = new OpportunityResultTO();

        if (StringUtils.isBlank(query)) {
            return successResponse(opportunityResult);
        }

        Integer start = (offset != null && offset > 0) ? offset : 0;
        Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;

        log.info("Api search opportunity. Search encode text: " + query);
        query = query.replace("%20", " ").trim();
        log.info("Api search opportunity. Search text: " + query);

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(start);
        filterParameter.setLimit(maxLimit);
        filterParameter.setSearchKey(query);
        filterParameter.setDetectDuplicates(false);
        filterParameter.setWithImage(true);
        filterParameter.setSearchButton(true);
        // filterParameter.setFromMobile(true);
//        filterParameter.setLookUp(true);

        ListResult<OpportunityListItem> result;

        try {
            result = crmServiceLocal.getOpportunityList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        opportunityResult.setTotal_count(result.getTotal());
        if (result.getTotal() < (maxLimit + start)) {
            opportunityResult.setLeft(0);
        } else {
            opportunityResult.setLeft(result.getTotal() - (maxLimit + start));
        }
        opportunityResult.setCount(result.getList() != null ? result.getList().size() : 0);
        opportunityResult.setOffset(start);

        String baseCurrency = currencyServiceLocal.getCompanyBaseCurrency().getName();

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        ArrayList<OpportunityTO> opportunityList = new ArrayList<>();
        if (result.getList() != null) {
            for (OpportunityListItem item : result.getList()) {
                OpportunityTO opportunity = new OpportunityTO();
                opportunity.setName(item.getOpportunityName());
                if (item.getNumberData() != null) {
                    opportunity.setNumber(item.getNumberData().getNumberString());
                }
                if (item.getCrmAccountItem() != null) {
                    opportunity.setCompany(item.getCrmAccountItem().getName());
                }
                opportunity.setItem_id(item.getObjectId());
                if (item.getStage() != null) {
                    opportunity.setStatus_id(item.getStage().getId());
                } else {
                    opportunity.setStatus_id(0);
                }
                if (item.getAmount() != null && item.getAmount() > 0) {
                    CurrencyValueTO currencyValue = new CurrencyValueTO();
                    currencyValue.setValue(BigDecimal.valueOf(item.getAmount()));
                    if (item.getCurrency() != null) {
                        currencyValue.setCurrency(item.getCurrency());
                    } else {
                        currencyValue.setCurrency(baseCurrency);
                    }
                    opportunity.setItem_price(currencyValue);
                }
                opportunity.setDate_added(longDateTimezoneFormat.format(item.getCreatedDate()));
                List<Integer> taskIDs = relationManager.getRelationIDsByType(item.getObjectId(), RelationItem.TYPE_OPPORTUNITY, RelationItem.TYPE_TASK);
                if (taskIDs != null && !taskIDs.isEmpty()) {
                    boolean hasLeadOverdueTasks = taskManager.getOverdueTasksByIDs(taskIDs).size() > 0;
                    boolean hasLeadTasks = taskManager.getTasksByIDs(taskIDs).size() > 0;
                    if (hasLeadOverdueTasks) {
                        opportunity.setTasks_presence(TaskPresenceEnum.OVERDUE.getType());
                    } else {
                        opportunity.setTasks_presence(hasLeadTasks ? TaskPresenceEnum.AVAILABLE.getType() : TaskPresenceEnum.NO_TASKS.getType());
                    }
                } else {
                    opportunity.setTasks_presence(TaskPresenceEnum.NO_TASKS.getType());
                }
                opportunityList.add(opportunity);
            }
            opportunityResult.setList(opportunityList);
        }
        return successResponse(opportunityResult);
    }


    private FacetFilterRpc getOneTimeFilter(FacetFilterRpc defaultFilter) {
        if (defaultFilter == null) {
            return null;
        }
        defaultFilter.setType(ListPanelType.OpportunityListPanelOTF);
        defaultFilter.setUserID(userManager.getUser().getObjectID());

        FacetFilterRpc otf = commonServiceLocal.getUserFacetFilter(defaultFilter);
        otf.setName("OTF");
        otf.setDefaultFilter(true);
        otf.setType(ListPanelType.OpportunityListPanelOTF);

        if (otf.getObjectID() != null) {
            EdsUserFilter edsUserFilter = userFilterManager.getByFacetFilterId(otf.getObjectID());
            if (edsUserFilter != null) {
                otf.setFavourFilter(Boolean.TRUE.equals(edsUserFilter.getFavour()));
            }
        }
        return otf;
    }

    private FacetFilterRpc initializeDefaultFacetFilter() {
        /*HashMap<String, FacetSolrField> showSolrFieldMap = new HashMap<>();
            FacetSolrField leadstatusfield = new FacetSolrField(SolrContactRepresenter.FIELD_LEAD_STATUS_ID, SolrContactRepresenter.FIELD_LEAD_STATUS_ID_CODE_NAME);
            showSolrFieldMap.put(FacetContentType.LeadFacetFilter.getContentCode()[2], leadstatusfield);*/

            /*ArrayList<String> showFacetCodeName = new ArrayList<>();
            showFacetCodeName.add(FacetContentType.LeadFacetFilter.getContentCode()[2]);*/

        //Initialize main Filter which we will use to combine/merge requested filters and peopleids
        FacetFilterRpc mainMergedFilter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.OpportunitiesListPanel).getFacetFilter();//new FacetFilterRpc(ListPanelType.LeadListPanel, showSolrFieldMap, showFacetCodeName);


        //Custom fields which are facetable
        ArrayList<CompanyCustomFieldItem> opportunityCustomFields = commonServiceLocal.getCompanyCustomFieldsForListView(ViewName.Opportunity);

        if (opportunityCustomFields != null && opportunityCustomFields.size() > 0) {
            opportunityCustomFields.forEach(companyCustomFieldItem -> {
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


    @Operation(summary = "List Items in Status", description = "Retrieves information on the filtered items in the status based on is_active filtering of quick_filter status")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of items in status"),
            @ApiResponse(responseCode = "400", description = "Status and offset ids are required"),
            @ApiResponse(responseCode = "400", description = "limit and offset cannot be zero at the same time")})
    @RequestMapping(value = "/opportunities/items_in_status", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @CheckPermission(permissions = {PermissionConstants.CRM_OPPORTUNITIES_LIST})
    public Object opportunitiesInStatus(@RequestBody ItemInStatusTO opportunitiesInStatus) throws RestException {
        if (opportunitiesInStatus.getStatus_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Status id required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (opportunitiesInStatus.getOffset() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Offset required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (opportunitiesInStatus.getOffset() < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Offset can not be less then zero", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (opportunitiesInStatus.getCount() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Count required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (opportunitiesInStatus.getCount() < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Count can not be less then zero", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (opportunitiesInStatus.getOffset() == 0 && opportunitiesInStatus.getCount() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Offset and count can not be zero at the same time", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        OrderFieldEnum orderFieldEnum = null;
        OrderByEnum orderByEnum = null;
        if (opportunitiesInStatus.getOrder() != null) {
            if (StringUtils.isNotBlank(opportunitiesInStatus.getOrder().getType())) {
                orderFieldEnum = OrderFieldEnum.getOrderField(opportunitiesInStatus.getOrder().getType());
                if (orderFieldEnum == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Type field should be one of ID, NAME, DATE, COMPANY", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            if (StringUtils.isNotBlank(opportunitiesInStatus.getOrder().getDirection())) {
                orderByEnum = OrderByEnum.getDirection(opportunitiesInStatus.getOrder().getDirection());
                if (orderByEnum == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Direction field should be one of ASC or DESC", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
        }
        //As per request from Islom Gulomov and Munir Yamal by default we will set sorting by kanban_order
        if (orderByEnum == null) {
            orderFieldEnum = OrderFieldEnum.KANBAN_ORDER;
            orderByEnum = OrderByEnum.ASC;
        }

        EdsReference requestedStatus = null;
        if (opportunitiesInStatus.getStatus_id() > 0) {
            requestedStatus = referenceManager.get(opportunitiesInStatus.getStatus_id());
            if (requestedStatus == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Status with " + opportunitiesInStatus.getStatus_id() + " id not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        }
        ListingFilterParameter filterParameter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.OpportunitiesListPanel);
        HashMap<String, FacetContentRpc> facetContentMap = filterParameter.getFacetFilter().getFacetContentMap();

        ArrayList<SelectItem> statusFacetItems = new ArrayList<>();
        if (requestedStatus != null) {
            statusFacetItems.add(new SelectItem(requestedStatus.getObjectID(), requestedStatus.getName()));
        } else {
            statusFacetItems.add(new SelectItem(-1, "N/A"));
        }

        facetContentMap.get(FacetContentType.OpportunityFacetFilter.getContentCode()[0]).setFacetItems(statusFacetItems.toArray(new SelectItem[0]));

        ArrayList<SelectItem> assignedToFacetItems = new ArrayList<>();
        EdsUser currentUser = userManager.getUser();
        EdsFacetFilter edsFacetFilter = facetFilterManager.getDefaultUserFacetFilter(ListPanelType.OpportunitiesQuickFilterForMobile, currentUser);

        if (edsFacetFilter != null) {
            HashSet<String> colNames = new HashSet<>();
            colNames.add("people");
            colNames.add("categories");
            FacetFilterRpc facetFilterRpc = edsFacetFilter.getFacetFilter(colNames);

            FacetContentRpc p = facetFilterRpc.getFacetContentMap().get("people");
            FacetContentRpc c = facetFilterRpc.getFacetContentMap().get("categories");
            if (p != null) {
                for (SelectItem pI : p.getFacetItems()) {
                    assignedToFacetItems.add(new SelectItem(pI.getId() > 0 ? pI.getId() : -1, ""));
                }
                //Apply Quick Filter by assigned to
                if (!assignedToFacetItems.isEmpty()) {
                    facetContentMap.get(FacetContentType.OpportunityFacetFilter.getContentCode()[1]).setFacetItems(assignedToFacetItems.toArray(new SelectItem[0]));
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
                                        && !FacetContentType.OpportunityFacetFilter.getContentCode()[0].equalsIgnoreCase(entry.getKey())
                                        && !FacetContentType.OpportunityFacetFilter.getContentCode()[1].equalsIgnoreCase(entry.getKey())) {
                                    FacetContentRpc existingValues = facetContentMap.get(entry.getKey());
                                    if (existingValues != null) {
                                        ArrayList<SelectItem> existingItems = new ArrayList<>(Arrays.asList(existingValues.getFacetItems()));
                                        existingItems.addAll(Arrays.asList(entry.getValue().getFacetItems()));

                                        ArrayList<SelectItem> removedDuplicates = new ArrayList<>(new HashSet<>(existingItems));

                                        existingValues.setFacetItems(removedDuplicates.toArray(new SelectItem[0]));
                                        facetContentMap.put(entry.getKey(), existingValues);
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

        //If OneTimeFilter is active
        FacetFilterRpc oneTimeFilter = getOneTimeFilter(initializeDefaultFacetFilter());
        if (oneTimeFilter != null && oneTimeFilter.isFavourFilter()) {
            //Important to pass facetcodenames
            //FacetFilterRpc filterRpc = oneTimeFilter.getFacetFilter(new HashSet<>(filterParameter.getFacetFilter().getShowFacetCodeName()));
            if (!oneTimeFilter.getFacetContentMap().isEmpty()) {
                for (Map.Entry<String, FacetContentRpc> entry : oneTimeFilter.getFacetContentMap().entrySet()) {

                    //ignore status and assignedto fields
                    if (entry.getValue().getFacetItems().length > 0
                            && !FacetContentType.OpportunityFacetFilter.getContentCode()[0].equalsIgnoreCase(entry.getKey())
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

        filterParameter.getFacetFilter().setFacetContentMap(facetContentMap);

        ArrayList<String> columnCodeNames = OpportunityListItem.defaultColumnNames;
        ListPanelToolRpc panelTools = new ListPanelToolRpc();
        panelTools.setColumnCodeName(columnCodeNames);
        panelTools.setShowPopup(true);
        filterParameter.setListPanelTool(panelTools);
        filterParameter.setColumnsOfListing(columnCodeNames);

        filterParameter.setStart(opportunitiesInStatus.getOffset());
        filterParameter.setLimit(opportunitiesInStatus.getCount());
        filterParameter.setSearchButton(false);
        filterParameter.setDetectDuplicates(false);
        filterParameter.setWithImage(true);
        if (orderFieldEnum != null) {
            filterParameter.setSortField(getSortField(orderFieldEnum, ListPanelType.OpportunitiesListPanel));
        }
        filterParameter.setAscending(orderByEnum == null || OrderByEnum.ASC.getDirection().equals(orderByEnum.getDirection()));
        filterParameter.setSortDir(orderByEnum != null ? orderByEnum.getId() : OrderByEnum.ASC.getId());

        ArrayList<CompanyCustomFieldItem> customFieldItems = commonServiceLocal.getCompanyCustomFieldsForListView(ViewName.Opportunity);
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
        ListResult<OpportunityListItem> result;
        try {
            result = crmServiceLocal.getOpportunityList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            if (((UndeclaredThrowableException) e).getUndeclaredThrowable() instanceof CheckPermissionException) {
                throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        OpportunityResultTO opportunityResult = new OpportunityResultTO();
        opportunityResult.setStatus_id(opportunitiesInStatus.getStatus_id() > 0 ? opportunitiesInStatus.getStatus_id() : 0);
        opportunityResult.setTotal_count(result.getTotal());
        if (result.getTotal() < (opportunitiesInStatus.getCount() + opportunitiesInStatus.getOffset())) {
            opportunityResult.setLeft(0);
        } else {
            opportunityResult.setLeft(result.getTotal() - (opportunitiesInStatus.getOffset() + opportunitiesInStatus.getCount()));
        }
        opportunityResult.setCount(result.getList() != null ? result.getList().size() : 0);
        opportunityResult.setOffset(opportunitiesInStatus.getOffset());

        String companyBaseCurrency = currencyServiceLocal.getCompanyBaseCurrency().getName();
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        ArrayList<OpportunityTO> opportunities = new ArrayList<>();

        if (result.getList() != null) {
            for (OpportunityListItem item : result.getList()) {
                OpportunityTO opportunity = new OpportunityTO();
                opportunity.setName(item.getOpportunityName());
                if (item.getCrmAccountItem() != null) {
                    opportunity.setCompany(item.getCrmAccountItem().getName());
                }
                opportunity.setItem_id(item.getObjectId());
                if (item.getStage() != null) {
                    opportunity.setStatus_id(item.getStage().getId());
                } else {
                    opportunity.setStatus_id(0);
                }
                String opportunityCurrency = StringUtils.isNotBlank(item.getCurrency()) ? item.getCurrency() : companyBaseCurrency;
                if (item.getAmount() != null && item.getAmount() > 0) {
                    CurrencyValueTO itemPrice = new CurrencyValueTO();
                    itemPrice.setValue(BigDecimal.valueOf(item.getAmount()));
                    itemPrice.setCurrency(opportunityCurrency);
                    opportunity.setItem_price(itemPrice);

                    CurrencyValueTO companyPrice = new CurrencyValueTO();
                    if (companyBaseCurrency.equals(opportunityCurrency)) {
                        companyPrice.setCurrency(companyBaseCurrency);
                        companyPrice.setValue(BigDecimal.valueOf(item.getAmount()));
                    } else {
                        companyPrice.setCurrency(companyBaseCurrency);
                        EdsOpportunity edsOpportunity = opportunityManager.get(item.getObjectId());
                        if (edsOpportunity != null) {
                            BigDecimal exchangeRate = edsOpportunity.getExchangeRate();
                            if (exchangeRate != null && exchangeRate.doubleValue() > 0) {
                                companyPrice.setValue(BigDecimal.valueOf(item.getAmount()).divide(exchangeRate, 2, RoundingMode.HALF_UP));
                            }
                        }
                    }
                    opportunity.setCompany_price(companyPrice);
                }
                if (item.getCreatedDate() != null) {
                    opportunity.setDate_added(longDateTimezoneFormat.format(item.getCreatedDate()));
                }
                if (item.getClosingDate() != null) {
                    opportunity.addProperty("close_date", longDateTimezoneFormat.format(item.getClosingDate()));
                }

                List<Integer> taskIDs = relationManager.getRelationIDsByType(item.getObjectId(), RelationItem.TYPE_OPPORTUNITY, RelationItem.TYPE_TASK);
                if (taskIDs != null && !taskIDs.isEmpty()) {
                    boolean hasLeadOverdueTasks = taskManager.getOverdueTasksByIDs(taskIDs).size() > 0;
                    boolean hasTasks = taskManager.getTasksByIDs(taskIDs).size() > 0;
                    if (hasLeadOverdueTasks) {
                        opportunity.setTasks_presence(TaskPresenceEnum.OVERDUE.getType());
                    } else {
                        opportunity.setTasks_presence(hasTasks ? TaskPresenceEnum.AVAILABLE.getType() : TaskPresenceEnum.NO_TASKS.getType());
                    }
                } else {
                    opportunity.setTasks_presence(TaskPresenceEnum.NO_TASKS.getType());
                }
                opportunities.add(opportunity);
            }
        }
        opportunityResult.setList(opportunities);
        return successResponse(opportunityResult);
    }

    @Operation(summary = "Change Item Status", description = "Changes the status of the item")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error codes"),
            @ApiResponse(responseCode = "400", description = "item_id is required"),
            @ApiResponse(responseCode = "400", description = "status_id is required")})
    @RequestMapping(value = "/opportunities/change_status", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @CheckPermission(permissions = {PermissionConstants.CRM_OPPORTUNITIES_LIST, PermissionConstants.CRM_OPPORTUNITY_CHANGE_STAGE})
    public Object changeItemStatus(@RequestBody ChangeOpportunityStatusRequestTO changeItemStatus) throws RestException {

        if (changeItemStatus.getItem_id() == null || changeItemStatus.getItem_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        OpportunityListItem item = crmServiceLocal.getOpportunity(changeItemStatus.getItem_id());
        if (item == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Opportunity with " + changeItemStatus.getItem_id() + " id not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if (changeItemStatus.getStatus_id() == null || changeItemStatus.getStatus_id() <= 0) {
            item.setStage(null);
        } else {
            EdsReference status = referenceManager.get(changeItemStatus.getStatus_id());
            if (status == null || status.getParent() == null || StringUtils.isBlank(status.getParent().getCode()) || !EdsOpportunity._OPPORTUNITY_STAGE.equalsIgnoreCase(status.getParent().getCode())) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Status with " + changeItemStatus.getStatus_id() + " id not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            item.setStage(status.getAsSelectItem());
        }

        item.setSelectedSubStageId(changeItemStatus.getReason_id());
        item.setNote(changeItemStatus.getNote());

        try {
            crmServiceLocal.saveOppotunityEditCellValue(item, OpportunityListItem.STAGE);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), ERROR_LEAD_MODIFY, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return successResponse(new ResponseData());
    }


    @Operation(summary = "Opportunity Detail Info")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have opportunity details"),
            @ApiResponse(responseCode = "400", description = "id is required")})
    @RequestMapping(value = "/opportunities/{id}/details", method = RequestMethod.GET)
    public Object getOpportunityDetails(@PathVariable(value = "id") Integer id) throws RestException {
        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsOpportunity edsOpportunity = opportunityManager.get(id);
        if (edsOpportunity == null || edsOpportunity.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Opportunity with id " + id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        OpportunityDetailsItemTO opportunityDetailsItem = new OpportunityDetailsItemTO();
        opportunityDetailsItem.addProperty("created_date", edsOpportunity.getCreationDate());

        //base_info
        OpportunityTO opportunityBaseInfo = new OpportunityTO();
        opportunityBaseInfo.setName(edsOpportunity.getName());
        if (edsOpportunity.getCrmAccount() != null) {
            opportunityBaseInfo.setCompany(edsOpportunity.getCrmAccount().getName());
        }
        opportunityBaseInfo.setItem_id(edsOpportunity.getObjectID());
        if (edsOpportunity.getStage() != null) {
            opportunityBaseInfo.setStatus_id(edsOpportunity.getStage().getObjectID());
        }
        if (edsOpportunity.getCreationDate() != null) {
            opportunityBaseInfo.setDate_added(longDateTimezoneFormat.format(edsOpportunity.getCreationDate()));
        }

        //item_price
        if (edsOpportunity.getAmount() != null && edsOpportunity.getAmount() != 0d) {
            CurrencyItem baseCurrency = currencyServiceLocal.getBaseCurrency();
            Integer calculationScale = ServerUtils.getCalculationScale();
            CurrencyValueTO itemPrice = new CurrencyValueTO();
            itemPrice.setValue(BigDecimal.valueOf(edsOpportunity.getAmount()).setScale(calculationScale, RoundingMode.HALF_UP));
            if (edsOpportunity.getCurrency() != null) {
                itemPrice.setCurrency(edsOpportunity.getCurrency().getName());
            } else {
                itemPrice.setCurrency(baseCurrency.getName());
            }
            opportunityBaseInfo.setItem_price(itemPrice);

            CurrencyValueTO companyPrice = new CurrencyValueTO();
            companyPrice.setValue(BigDecimal.valueOf(edsOpportunity.getAmount()).divide(edsOpportunity.getExchangeRate() != null ? edsOpportunity.getExchangeRate() : BigDecimal.ONE, calculationScale, RoundingMode.HALF_UP));
            companyPrice.setCurrency(baseCurrency.getName());

            opportunityBaseInfo.setCompany_price(companyPrice);
        }

        //Default value of tasks presence
        opportunityBaseInfo.setTasks_presence(TaskPresenceEnum.NO_TASKS.getType());

        //task
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(0);
        filterParameter.setLimit(MAX_LIMIT);
        filterParameter.setSortField(TaskListItem.ID);
        filterParameter.setRelationType(RelationItem.TYPE_OPPORTUNITY);
        filterParameter.setRelationID(edsOpportunity.getObjectID());
        try {
            TaskList taskList = taskServiceLocal.getTaskList(filterParameter);
            if (taskList != null && taskList.getList() != null && taskList.getList().size() > 0) {

                List<TaskListItem> overdueTasks = taskList.getList().stream().filter(taskItem -> (EdsTask.IN_PROGRESS.equals(taskItem.getStatusCode())
                        || EdsTask.NOT_STARTED.equals(taskItem.getStatusCode())
                        || EdsTask.WAITING_FOR_SOMEONE_ELSE.equals(taskItem.getStatusCode()))
                        && taskItem.getDueDate() != null && taskItem.getDueDate().before(new Date())).toList();

                if (overdueTasks.size() > 0) {
                    opportunityBaseInfo.setTasks_presence(TaskPresenceEnum.OVERDUE.getType());
                } else {
                    opportunityBaseInfo.setTasks_presence(TaskPresenceEnum.AVAILABLE.getType());
                }

                //Task. One of latest task
                TaskListItem taskListItem = taskList.getList().get(0);

                TaskDetailInfoTO task = new TaskDetailInfoTO();

                TaskBaseInfoTO taskBaseInfo = new TaskBaseInfoTO();
                taskBaseInfo.setName(taskListItem.getName());
                taskBaseInfo.setStatus_id(taskListItem.getTaskStatusId());
                taskBaseInfo.setItem_id(taskListItem.getObjectID());
                if (taskListItem.getDueDate() != null) {
                    taskBaseInfo.setDue_date(longDateTimezoneFormat.format(taskListItem.getDueDate()));
                }
                taskBaseInfo.setPriority(TaskPriorityEnum.get(taskListItem.getPriorityCode()));
                task.setBase_info(taskBaseInfo);

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

                opportunityDetailsItem.setTask(task);
            }
        } catch (Exception e) {
            log.error("Error occurred while getting opportunity's task, {}", e.getMessage());
        }

        opportunityDetailsItem.setBase_info(opportunityBaseInfo);

        //Linked person
        if (edsOpportunity.getCrmContact() != null) {

            ContactTO linkedPerson = new ContactTO();
            linkedPerson.setName(edsOpportunity.getCrmContact().getName());
            linkedPerson.setItem_id(edsOpportunity.getCrmContact().getObjectID());
            if (edsOpportunity.getCrmContact().getPhoto() != null) {
                linkedPerson.setAvatar_image(commonServiceLocal.getImageUrl(edsOpportunity.getCrmContact().getPhoto().getObjectID()));
            }

            ContactsTO contactsTO = new ContactsTO();

            //Phones
            contactsTO.setPhones(contactServiceLocal.convertToPhoneTO(edsOpportunity.getCrmContact()));

            //Emails
            contactsTO.setEmails(contactServiceLocal.convertContactEmails(edsOpportunity.getCrmContact()));

            linkedPerson.setContacts(contactsTO);

            //Company
            linkedPerson.setCompany(contactServiceLocal.convertCompany(edsOpportunity.getCrmContact().getCrmAccount()));

            opportunityDetailsItem.setLinked_person(linkedPerson);
        }

        //Note. One of latest note
        filterParameter = new ListingFilterParameter();
        filterParameter.setRelationID(edsOpportunity.getObjectID());
        filterParameter.setRelationType(RelationItem.TYPE_OPPORTUNITY);
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
                    note.setNote_content(noteHistory.getComment().replaceAll("\n", " "));
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
                opportunityDetailsItem.setNote(note);
            }
        } catch (Exception e) {
            log.error("Error occurred while getting contact's notes ", e);
        }

        //Status
        if (edsOpportunity.getStage() != null) {
            FilteredStatusItemTO status = new FilteredStatusItemTO();
            EdsReference edsReference = edsOpportunity.getStage();
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
            status.setEdit_permission(edsOpportunity.getStage().getOppEditBtnRole().isEmpty() || userManager.getUser().hasEitherRoles(edsOpportunity.getStage().getOppEditBtnRole().toArray(new EdsRole[]{})));
            status.setStatus_permission(edsOpportunity.getStage().getAllowedRoles().isEmpty() || userManager.getUser().hasEitherRoles(edsOpportunity.getStage().getAllowedRoles().toArray(new EdsRole[]{})));
            status.setView_permission(edsOpportunity.getStage().getViewOnlyRoles().isEmpty() || userManager.getUser().hasEitherRoles(edsOpportunity.getStage().getViewOnlyRoles().toArray(new EdsRole[]{})));
            status.setCommentRequired(edsOpportunity.getStage().isRequiredComment());
            opportunityDetailsItem.setStatus(status);
        }

        //Contacts
        if (edsOpportunity.getCrmContact() != null) {
            ContactsTO primaryContact = new ContactsTO();
            //Emails
            primaryContact.setEmails(contactServiceLocal.convertContactEmails(edsOpportunity.getCrmContact()));

            ArrayList<PhoneTO> phones = new ArrayList<>();
            if (StringUtils.isNotBlank(edsOpportunity.getCrmContact().getPrimaryPhone())) {
                String phoneNumber = edsOpportunity.getCrmContact().getPrimaryPhone();
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
            primaryContact.setPhones(phones);

            opportunityDetailsItem.setContacts(primaryContact);
        }

        //Email count as per Nasimxon's request for Artel
        Integer messageTotalCount = emailRepository.getEmailCount(filterParameter);
        opportunityDetailsItem.setEmail_count(messageTotalCount);

        OpportunityDetailsItemResponseTO result = new OpportunityDetailsItemResponseTO();
        result.setItem(opportunityDetailsItem);
        return successResponse(result);
    }

    @Operation(summary = "Opportunity Items")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have opportunity items"),
            @ApiResponse(responseCode = "400", description = "item_id, sort_type and direction are required")})
    @RequestMapping(value = "/opportunities/{item_id}/sub_items", method = RequestMethod.GET)
    public Object getOpportunitySubItems(
            @PathVariable(value = "item_id") Integer item_id,
            @RequestParam(value = "sort_type") String sort_type,
            @RequestParam(value = "direction") String direction) throws RestException {

        if (item_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id should be more than zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (StringUtils.isBlank(sort_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "sort_type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(direction)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "direction is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (!OrderFieldEnum.NAME.getField().equalsIgnoreCase(sort_type) && !OrderFieldEnum.ID.getField().equalsIgnoreCase(sort_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Type field should be one of name, id", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!OrderByEnum.ASC.getDirection().equals(direction) && !OrderByEnum.DESC.getDirection().equals(direction)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Direction field should be one of ASC or DESC", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        CurrencyItem baseCurrency = currencyServiceLocal.getBaseCurrency();
        ArrayList<OpportunitySubItemsTO> opportunitySubItems = new ArrayList<>();
        OpportunityItem[] opportunityListItem = crmServiceLocal.getOpportunityItems(item_id);
        ColumnConfigs[] columns = itemTableSettingService.getColumnConfigs(ItemTableEnum.OPPORTUNITY_SUB_ITEM);
        List<String> columnCodes = new ArrayList<>();
        for (ColumnConfigs cf : columns) {
            columnCodes.add(cf.getCode());
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (opportunityListItem != null) {
            for (OpportunityItem opportunityItem : opportunityListItem) {
                OpportunitySubItemsTO opportunitySubItem = new OpportunitySubItemsTO();
                opportunitySubItem.setId(opportunityItem.getItemID());
                opportunitySubItem.setName(opportunityItem.getItemName());
                if (opportunityItem.getQty() != null && columnCodes.contains(QTY)) {
                    opportunitySubItem.setCount(opportunityItem.getQty());
                }
                if (StringUtils.isNotBlank(opportunityItem.getDescription())) {
                    opportunitySubItem.setDescription(opportunityItem.getDescription());
                }
                if (StringUtils.isNotBlank(opportunityItem.getSupplierName())) {
                    opportunitySubItem.setSupplier(opportunityItem.getSupplierName());
                }
                if (opportunityItem.getPrice() != null && columnCodes.contains(UNITPRICE)) {
                    CurrencyValueTO currencyValue = new CurrencyValueTO();
                    if (StringUtils.isNotBlank(opportunityItem.getCurrency())) {
                        currencyValue.setCurrency(opportunityItem.getCurrency());
                    } else {
                        if (baseCurrency != null) {
                            currencyValue.setCurrency(baseCurrency.getName());
                        }
                    }
                    if (opportunityItem.getPrice() != null) {
                        currencyValue.setValue(opportunityItem.getPrice());
                    }
                    opportunitySubItem.setPrice(currencyValue);
                }
                if (opportunityItem.getDiscountAmount() != null && columnCodes.contains(DISCOUNT_AMT)) {
                    opportunitySubItem.addProperty("discount", opportunityItem.getDiscountAmount());
                }
                if (opportunityItem.getUnitMeasurement() != null && columnCodes.contains(MEASUREMENT)) {
                    opportunitySubItem.addProperty("measurement", opportunityItem.getUnitMeasurement().getName());
                }
                if (columnCodes.contains(TAX_LIST)) {
                    opportunitySubItem.addProperty("tax", opportunityItem.getTaxItem() != null ? opportunityItem.getTaxItem().getEffectiveTaxPercent() :
                            opportunityItem.getTaxAmount() != null ? opportunityItem.getTaxAmount() : BigDecimal.ZERO);
                }
                if (columnCodes.contains(ItemTableConstants.CLIENT)) {
                    opportunitySubItem.addProperty("client", opportunityItem.getSupplierName());
                }
                if (columnCodes.contains(NET_AMT)) {
                    opportunitySubItem.addProperty("net-amount", opportunityItem.getNet());
                }
                if (columnCodes.contains(TOTAL_AMT)) {
                    opportunitySubItem.addProperty("total-amount", opportunityItem.getSubTotal());
                }
                if (columnCodes.contains(CATEGORY)) {
                    opportunitySubItem.addProperty("category", opportunityItem.getProductCategory() != null ? opportunityItem.getProductCategory().getName() : "");
                }
                if (columnCodes.contains(BRAND)) {
                    opportunitySubItem.addProperty("brand", opportunityItem.getProductBrand() != null ? opportunityItem.getProductBrand().getName() : "");
                }
                if (columnCodes.contains(PROJECT)) {
                    opportunitySubItem.addProperty("project", opportunityItem.getProject() != null ? opportunityItem.getProject().getName() : "");
                }
                if (opportunityItem.getCustomFieldValuesAsMap() != null && !opportunityItem.getCustomFieldValuesAsMap().isEmpty()) {
                    List<Object> customFields = new ArrayList<>();
                    for (String key : opportunityItem.getCustomFieldValuesAsMap().keySet()) {
                        if (columnCodes.contains(key)) {
                            CompanyCustomFieldItem cfItem = opportunityItem.getCustomFieldValuesAsMap().get(key);
                            String value;
                            if (Constants.DATA_TYPE_DATE.equals(cfItem.getDataType())) {
                                value = cfItem.getFieldDateNonConvertedValue() != null ? DateUtils.format(cfItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            } else if (Constants.UI_TYPE_PERCENTAGE.equals(cfItem.getUiType())) {
                                value = cfItem.getFieldStringValue() != null ? cfItem.getFieldStringValue() + " % " : "";
                            } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(cfItem.getUiType())) {
                                StringBuilder finalValue = new StringBuilder();
                                if (cfItem.getSelectItems() != null && cfItem.getSelectItems().size() > 0) {
                                    for (SelectItem selectItem : cfItem.getSelectItems()) {
                                        finalValue.append(selectItem.getName()).append("; ");
                                    }
                                }
                                value = finalValue.toString();
                            } else {
                                value = cfItem.getFieldStringValue();
                            }
                            customFields.add(new CustomFieldTo(cfItem.getFieldName(), value));
                        }
                    }
                    opportunitySubItem.setCustom_fields(customFields);
                }
                opportunitySubItems.add(opportunitySubItem);
                totalAmount = totalAmount.add(opportunityItem.getSubTotal());
            }
        }
        if (OrderFieldEnum.NAME.getField().equalsIgnoreCase(sort_type)) {
            opportunitySubItems.sort((o1, o2) -> {
                if (OrderByEnum.ASC.getDirection().equals(direction)) {
                    return o1.getName().compareTo(o2.getName());
                } else {
                    return o2.getName().compareTo(o1.getName());
                }
            });
        } else {
            opportunitySubItems.sort((o1, o2) -> {
                if (OrderByEnum.ASC.name().equals(direction)) {
                    return (o1.getId() != null && o2.getId() != null) ? o1.getId().compareTo(o2.getId()) : -1;
                } else {
                    return (o1.getId() != null && o2.getId() != null) ? o1.getId().compareTo(o2.getId()) : -1;
                }
            });
        }
        ResponseListData<OpportunitySubItemsTO> result = new ResponseListData<>(opportunitySubItems);
        result.addProperty("total", totalAmount);
        return successResponse(result);
    }

    @Operation(summary = "Opportunity Items")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have opportunity items"),
            @ApiResponse(responseCode = "400", description = "item_id, sort_type and direction are required")})
    @RequestMapping(value = "/opportunities/{item_id}/sub_items_with_name", method = RequestMethod.GET)
    public Object getOpportunitySubItemsWithFieldName(
            @PathVariable(value = "item_id") Integer item_id,
            @RequestParam(value = "sort_type") String sort_type,
            @RequestParam(value = "direction") String direction) throws RestException {

        if (item_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id should be more than zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (StringUtils.isBlank(sort_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "sort_type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(direction)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "direction is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (!OrderFieldEnum.NAME.getField().equalsIgnoreCase(sort_type) && !OrderFieldEnum.ID.getField().equalsIgnoreCase(sort_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Type field should be one of name, id", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!OrderByEnum.ASC.getDirection().equals(direction) && !OrderByEnum.DESC.getDirection().equals(direction)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Direction field should be one of ASC or DESC", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        CurrencyItem baseCurrency = currencyServiceLocal.getBaseCurrency();
        ArrayList<OpportunitySubItemsTO> opportunitySubItems = new ArrayList<>();
        OpportunityItem[] opportunityListItem = crmServiceLocal.getOpportunityItems(item_id);
        ColumnConfigs[] columns = itemTableSettingService.getColumnConfigs(ItemTableEnum.OPPORTUNITY_SUB_ITEM);
        Map<String, ColumnConfigs> columnCodes = new LinkedHashMap<>();
        for (ColumnConfigs cf : columns) {
            columnCodes.put(cf.getCode(), cf);
        }
        if (opportunityListItem != null) {
            for (OpportunityItem opportunityItem : opportunityListItem) {
                OpportunitySubItemsTO opportunitySubItem = new OpportunitySubItemsTO();
                List<Object> customFields = new ArrayList<>();
                opportunitySubItem.setId(opportunityItem.getItemID());
                opportunitySubItem.setName(opportunityItem.getItemName());
                if (opportunityItem.getQty() != null && columnCodes.containsKey(QTY)) {
                    customFields.add(new CustomFieldTo(columnCodes.get(QTY).getTitle(), opportunityItem.getQty().toString()));
                }
                if (StringUtils.isNotBlank(opportunityItem.getDescription()) && columnCodes.containsKey(DESCRIPTION)) {
                    customFields.add(new CustomFieldTo(columnCodes.get(DESCRIPTION).getTitle(), opportunityItem.getDescription()));
                }
                if (opportunityItem.getPrice() != null && columnCodes.containsKey(UNITPRICE)) {
                    String currencyValue = "";
                    if (StringUtils.isNotBlank(opportunityItem.getCurrency())) {
                        currencyValue += opportunityItem.getCurrency();
                    } else {
                        if (baseCurrency != null) {
                            currencyValue += baseCurrency.getName();
                        }
                    }
                    if (opportunityItem.getPrice() != null) {
                        currencyValue += " " + opportunityItem.getPrice();
                    }
                    customFields.add(new CustomFieldTo(columnCodes.get(UNITPRICE).getTitle(), currencyValue));
                }
                if (opportunityItem.getDiscountAmount() != null && columnCodes.containsKey(DISCOUNT_AMT)) {
                    customFields.add(new CustomFieldTo(columnCodes.get(DISCOUNT_AMT).getTitle(), opportunityItem.getDiscountAmount().toString()));
                }
                if (opportunityItem.getUnitMeasurement() != null && columnCodes.containsKey(MEASUREMENT)) {
                    customFields.add(new CustomFieldTo(columnCodes.get(MEASUREMENT).getTitle(), opportunityItem.getUnitMeasurement().getName()));
                }
                if (columnCodes.containsKey(TAX_LIST)) {
                    customFields.add(new CustomFieldTo(columnCodes.get(TAX_LIST).getTitle(), opportunityItem.getTaxItem() != null ? opportunityItem.getTaxItem().getEffectiveTaxPercent().toString() :
                            opportunityItem.getTaxAmount() != null ? opportunityItem.getTaxAmount().toString() : ""));
                }
                if (columnCodes.containsKey(ItemTableConstants.CLIENT)) {
                    customFields.add(new CustomFieldTo(columnCodes.get(ItemTableConstants.CLIENT).getTitle(), opportunityItem.getSupplierName()));
                }
                if (columnCodes.containsKey(NET_AMT)) {
                    customFields.add(new CustomFieldTo(columnCodes.get(NET_AMT).getTitle(), opportunityItem.getNet().toString()));
                }
                if (columnCodes.containsKey(TOTAL_AMT)) {
                    customFields.add(new CustomFieldTo(columnCodes.get(TOTAL_AMT).getTitle(), opportunityItem.getSubTotal().toString()));
                }
                if (columnCodes.containsKey(CATEGORY)) {
                    customFields.add(new CustomFieldTo(columnCodes.get(CATEGORY).getTitle(), opportunityItem.getProductCategory() != null ? opportunityItem.getProductCategory().getName() : ""));
                }
                if (columnCodes.containsKey(BRAND)) {
                    customFields.add(new CustomFieldTo(columnCodes.get(BRAND).getTitle(), opportunityItem.getProductBrand() != null ? opportunityItem.getProductBrand().getName() : ""));
                }
                if (columnCodes.containsKey(PROJECT)) {
                    customFields.add(new CustomFieldTo(columnCodes.get(PROJECT).getTitle(), opportunityItem.getProject() != null ? opportunityItem.getProject().getName() : ""));
                }
                if (opportunityItem.getCustomFieldValuesAsMap() != null && !opportunityItem.getCustomFieldValuesAsMap().isEmpty()) {
                    for (String key : opportunityItem.getCustomFieldValuesAsMap().keySet()) {
                        if (columnCodes.containsKey(key)) {
                            CompanyCustomFieldItem cfItem = opportunityItem.getCustomFieldValuesAsMap().get(key);
                            String value;
                            if (Constants.DATA_TYPE_DATE.equals(cfItem.getDataType())) {
                                value = cfItem.getFieldDateNonConvertedValue() != null ? DateUtils.format(cfItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            } else if (Constants.UI_TYPE_PERCENTAGE.equals(cfItem.getUiType())) {
                                value = cfItem.getFieldStringValue() != null ? cfItem.getFieldStringValue() + " % " : "";
                            } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(cfItem.getUiType())) {
                                StringBuilder finalValue = new StringBuilder();
                                if (cfItem.getSelectItems() != null && cfItem.getSelectItems().size() > 0) {
                                    for (SelectItem selectItem : cfItem.getSelectItems()) {
                                        finalValue.append(selectItem.getName()).append("; ");
                                    }
                                }
                                value = finalValue.toString();
                            } else {
                                value = cfItem.getFieldStringValue();
                            }
                            customFields.add(new CustomFieldTo(cfItem.getFieldName(), value));
                        }
                    }
                    opportunitySubItem.setCustom_fields(customFields);
                }
                opportunitySubItems.add(opportunitySubItem);
            }
        }
        if (OrderFieldEnum.NAME.getField().equalsIgnoreCase(sort_type)) {
            opportunitySubItems.sort((o1, o2) -> {
                if (OrderByEnum.ASC.getDirection().equals(direction)) {
                    return o1.getName().compareTo(o2.getName());
                } else {
                    return o2.getName().compareTo(o1.getName());
                }
            });
        } else {
            opportunitySubItems.sort((o1, o2) -> {
                if (OrderByEnum.ASC.name().equals(direction)) {
                    return (o1.getId() != null && o2.getId() != null) ? o1.getId().compareTo(o2.getId()) : -1;
                } else {
                    return (o1.getId() != null && o2.getId() != null) ? o1.getId().compareTo(o2.getId()) : -1;
                }
            });
        }
        return successResponse(new ResponseListData<>(opportunitySubItems));
    }

    @Operation(summary = "Company Opportunities")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have opportunities related to a particular company"),
            @ApiResponse(responseCode = "400", description = "item_id, sort_type and direction are required")})
    @RequestMapping(value = "/companies/{item_id}/opportunities", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.CRM_OPPORTUNITIES_LIST})
    public Object getCompanyOpportunities(
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
            throw new RestException(GENERAL_ERROR_MESSAGE, "No Account found with provided id", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (direction != null && !OrderByEnum.ASC.getDirection().equals(direction) && !OrderByEnum.DESC.getDirection().equals(direction)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "direction should be ASC or DESC", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (sort_type != null && !OrderFieldEnum.NAME.getField().equalsIgnoreCase(sort_type) && !OrderFieldEnum.ID.getField().equalsIgnoreCase(sort_type) && !OrderFieldEnum.DATE.getField().equalsIgnoreCase(sort_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "sort_type should be date, name or id", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }


        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setAccountID(item_id);
        filterParameter.setStart(0);
        filterParameter.setLimit(limit != null ? limit : MAX_LIMIT);

        ArrayList<CompanyOpportunityTO> companyOpportunities = new ArrayList<>();
        ListResult<OpportunityListItem> result;
        try {
            result = crmServiceLocal.getOpportunityList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            if (((UndeclaredThrowableException) e).getUndeclaredThrowable() instanceof CheckPermissionException) {
                throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        String companyBaseCurrency = currencyServiceLocal.getCompanyBaseCurrency().getName();

        if (result != null && result.getList() != null) {
            result.getList().forEach(opportunityListItem -> {
                CompanyOpportunityTO opportunity = new CompanyOpportunityTO();
                opportunity.setName(opportunityListItem.getOpportunityName());
                EdsOpportunity edsOpportunity = opportunityManager.get(opportunityListItem.getObjectId());
                if (edsOpportunity != null && edsOpportunity.getCrmAccount() != null) {
                    opportunity.setCompany(edsOpportunity.getCrmAccount().getName());
                }
                opportunity.setItem_id(opportunityListItem.getObjectId());
                if (opportunityListItem.getStageId() != null) {
                    FilteredStatusItemTO status = new FilteredStatusItemTO();
                    EdsReference edsReference = referenceManager.get(opportunityListItem.getStageId());
                    if (edsReference != null) {
                        status.setStatus_id(edsReference.getObjectID());
                        status.setStatus_name(edsReference.getName());
                        status.setOrder_id(edsReference.getSorder());
                        if (edsReference.getReferenceColor() != null) {
                            EdsReferenceColor edsReferenceColor = edsReference.getReferenceColor();
                            if (edsReferenceColor != null) {
                                status.setStatus_color(new ColorTO(edsReferenceColor.getObjectID(), edsReferenceColor.getHex(), edsReferenceColor.getName()));
                            } else {
                                status.setStatus_color(getDefaultColor());
                            }
                        } else {
                            status.setStatus_color(getDefaultColor());
                        }
                        opportunity.setStatus(status);
                    }
                    opportunity.setStatus_id(opportunityListItem.getStageId());
                }
                if (opportunityListItem.getCreatedDate() != null) {
                    opportunity.setDate_added(longDateTimezoneFormat.format(opportunityListItem.getCreatedDate()));
                }
                String opportunityCurrency = StringUtils.isNotBlank(opportunityListItem.getCurrency()) ? opportunityListItem.getCurrency() : companyBaseCurrency;
                if (opportunityListItem.getAmount() != null && opportunityListItem.getAmount() != 0d) {
                    CurrencyValueTO itemPrice = new CurrencyValueTO();
                    itemPrice.setValue(BigDecimal.valueOf(opportunityListItem.getAmount()));
                    itemPrice.setCurrency(opportunityCurrency);
                    opportunity.setItem_price(itemPrice);

                    CurrencyValueTO companyPrice = new CurrencyValueTO();
                    if (companyBaseCurrency.equals(opportunityCurrency)) {
                        companyPrice.setCurrency(companyBaseCurrency);
                        companyPrice.setValue(BigDecimal.valueOf(opportunityListItem.getAmount()));
                    } else {
                        companyPrice.setCurrency(companyBaseCurrency);
                        if (edsOpportunity != null) {
                            BigDecimal exchangeRate = edsOpportunity.getExchangeRate();
                            companyPrice.setValue(BigDecimal.valueOf(opportunityListItem.getAmount()).divide(exchangeRate, 2, RoundingMode.HALF_UP));
                        }
                    }
                    opportunity.setCompany_price(companyPrice);
                }
                opportunity.setDate_added(longDateTimezoneFormat.format(opportunityListItem.getCreatedDate()));
                List<Integer> taskIDs = relationManager.getRelationIDsByType(opportunityListItem.getObjectId(), RelationItem.TYPE_OPPORTUNITY, RelationItem.TYPE_TASK);
                if (taskIDs != null && !taskIDs.isEmpty()) {
                    boolean hasLeadOverdueTasks = taskManager.getOverdueTasksByIDs(taskIDs).size() > 0;
                    boolean hasTasks = taskManager.getTasksByIDs(taskIDs).size() > 0;
                    if (hasLeadOverdueTasks) {
                        opportunity.setTasks_presence(TaskPresenceEnum.OVERDUE.getType());
                    } else {
                        opportunity.setTasks_presence(hasTasks ? TaskPresenceEnum.AVAILABLE.getType() : TaskPresenceEnum.NO_TASKS.getType());
                    }
                } else {
                    opportunity.setTasks_presence(TaskPresenceEnum.NO_TASKS.getType());
                }
                companyOpportunities.add(opportunity);

            });
        }
        if (OrderFieldEnum.NAME.getField().equalsIgnoreCase(sort_type)) {
            companyOpportunities.sort((o1, o2) -> {
                if (OrderByEnum.ASC.getDirection().equals(direction)) {
                    return o1.getName().compareTo(o2.getName());
                } else {
                    return o2.getName().compareTo(o1.getName());
                }
            });
        } else if (OrderFieldEnum.DATE.getField().equalsIgnoreCase(sort_type)) {
            companyOpportunities.sort((o1, o2) -> {
                if (OrderByEnum.ASC.name().equals(direction)) {
                    return (o1.getDate_added() != null && o2.getDate_added() != null) ? o1.getDate_added().compareTo(o2.getDate_added()) : -1;
                } else {
                    return (o1.getDate_added() != null && o2.getDate_added() != null) ? o1.getDate_added().compareTo(o2.getDate_added()) : -1;
                }
            });
        } else {
            companyOpportunities.sort((o1, o2) -> {
                if (OrderByEnum.ASC.getDirection().equals(direction)) {
                    return o1.getItem_id().compareTo(o2.getItem_id());
                } else {
                    return o2.getItem_id().compareTo(o1.getItem_id());
                }
            });
        }
        return successResponse(new ResponseListData<>(companyOpportunities));
    }

    @Operation(summary = "Delete Opportunity", description = "Delete particular entity like Lead, Opportunity, Company, Contact etc. Particular entity is described in path, like other requests. Server should check if current user has permissions to delete this particular item, and if no give user message: You don't have permissions to delete this entry. Please contact your administrator")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/opportunities/{item_id}/delete", method = RequestMethod.DELETE)
    @CheckPermission(permissions = {PermissionConstants.CRM_OPPORTUNITIES_LIST, PermissionConstants.CRM_REMOVE_OPPORTUNITIES})
    public Object deleteOpportunity(@PathVariable(value = "item_id") Integer item_id) throws RestException {

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsOpportunity edsOpportunity = opportunityManager.get(item_id);
        if (edsOpportunity == null || edsOpportunity.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Opportunity with id " + item_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (ServerUtils.hasPermission(PermissionConstants.CRM_REMOVE_OPPORTUNITIES)) {
            try {
                ArrayList<Integer> objectIDs = new ArrayList<>();
                objectIDs.add(item_id);
                crmServiceLocal.deleteOpportunity(objectIDs);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
        }
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Get Opportunity Stage List", description = "Retrieves list of Opportunity Stages ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Opportunity Stages")})
    @RequestMapping(value = "/opportunities/stage", method = RequestMethod.GET)
    public Object getOpportunityStageList() throws RestException {
        ArrayList<CategoryTO> opportunityStages = new ArrayList<>();
        List<EdsReference> edsReferences;
        try {
            edsReferences = referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_STAGE);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (edsReferences != null) {
            edsReferences.forEach(edsReference -> opportunityStages.add(new StageTO(edsReference.getObjectID(), edsReference.getName(), edsReference.getDescription())));
        }
        return successResponse(new ResponseListData<>(opportunityStages));
    }

    @Operation(summary = "Get Opportunity Reasons List", description = "Retrieves list of Opportunity Reasons ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Opportunity Stages")})
    @RequestMapping(value = "/opportunities/reasons", method = RequestMethod.GET)
    public Object getOpportunityReasonList() throws RestException {

        List<EdsReference> reasons = referenceManager.listReferences("_OPPORTUNITY_SUB_STAGE"/*EdsOpportunity._OPPORTUNITY_STAGE*/);
        List<CategoryTO> opportunityStages = reasons.stream().map(r -> new CategoryTO(r.getObjectID(), r.getName())).collect(Collectors.toList());
        return successResponse(new ResponseListData<>(opportunityStages));
    }

    @Operation(summary = "Get Entity Category List", description = "Get Categories for particular entities like leads, activities, opportunities")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the list of particular entities like leads, activities, opportunities")})
    @RequestMapping(value = "/opportunities/{field_type}/categories", method = RequestMethod.GET)
    public Object getEntityFieldCategories(
            @PathVariable(value = "field_type") String field_type,
            @RequestParam(value = "custom_field_id", required = false) Integer custom_field_id,
            @RequestParam(value = "dependency_id", required = false) Integer dependency_id,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "dependency_selected_item", required = false) String dependencySelectedItem,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        if (StringUtils.isBlank(field_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "field_type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (!EntityFieldTypeEnum.COMPANY_NAME.name().equals(field_type) && !EntityFieldTypeEnum.ASSIGNEE.name().equals(field_type) && !EntityFieldTypeEnum.BACKUP_ASSIGNEE.name().equals(field_type) &&
                !EntityFieldTypeEnum.CONTACT_NAME.name().equals(field_type) && !EntityFieldTypeEnum.CUSTOM.name().equals(field_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "field_type should be one of | COMPANY_NAME | ASSIGNEE | BACKUP_ASSIGNEE | CONTACT_NAME | CUSTOM", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Integer start = (offset != null && offset > 0) ? offset : 0;
        Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;
        EdsUser user = userManager.getUser();
        EntityCategoryTO entityCategories = new EntityCategoryTO();
        ArrayList<CategoryTO> categories = new ArrayList<>();

        if (EntityFieldTypeEnum.ASSIGNEE.name().equals(field_type) || EntityFieldTypeEnum.BACKUP_ASSIGNEE.name().equals(field_type)) {
            SelectItem[] salesPeople = crmServiceLocal.getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE);
            if (salesPeople != null) {
                List<SelectItem> salesPeopleList = Arrays.asList(salesPeople);

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
        } else if (EntityFieldTypeEnum.COMPANY_NAME.name().equals(field_type)) {
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
                crmAccountList.getList().forEach(crmAccountItem -> categories.add(new CategoryTO(crmAccountItem.getObjectId(), crmAccountItem.getName())));
                entityCategories.setList(categories);
            }
        } else if (EntityFieldTypeEnum.CONTACT_NAME.name().equals(field_type)) {
            if (dependency_id == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "dependency_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.isFiltirize();
            if (dependency_id > 0) {
                filterParameter.setAccountID(dependency_id);
            }
            filterParameter.setStart(start);
            filterParameter.setSearchKey(query);
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
                entityCategories.setTotal_count(result.getTotal());
                if (result.getTotal() < (maxLimit + start)) {
                    entityCategories.setLeft(0);
                } else {
                    entityCategories.setLeft(result.getTotal() - (start + maxLimit));
                }
                entityCategories.setCount(result.getList() != null ? result.getList().size() : 0);
                entityCategories.setOffset(start);
                if (result.getList() != null && result.getList().size() > 0) {
                    result.getList().forEach(contactListItem -> categories.add(new CategoryTO(contactListItem.getObjectId(), contactListItem.getName(), contactListItem.getCrmAccount() != null ? contactListItem.getCrmAccount().getObjectId() : null, contactListItem.getCrmAccount() != null ? contactListItem.getCrmAccount().getName() : null)));
                }
                entityCategories.setList(categories);
            }

        } else if (EntityFieldTypeEnum.CUSTOM.name().equals(field_type)) {

            if (custom_field_id < GAP_BTW_STATIC_AND_CUSTOM_FIELDS) {
                CompanyCustomFieldItem customFieldItem;
                try {
                    customFieldItem = profileServiceLocal.getCustomFieldData(custom_field_id, null);
                } catch (Exception e) {
                    log.error("", e);
                    throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                if (customFieldItem == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Options list by custom field id " + custom_field_id + " are not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                }
                if (Constants.UI_TYPE_LOOKUP.equalsIgnoreCase(customFieldItem.getUiType())) {
                    //if type is lookup
                    ListingFilterParameter filterParameter = new ListingFilterParameter();
                    filterParameter.setSearchKey(query);
                    filterParameter.setStart(start);
                    filterParameter.setLimit(limit);
                    if (CustomFieldLookUpTypeEnum.REFERENCE.equals(customFieldItem.getLookUpTypeEnum())) {
                        filterParameter.setParentID(customFieldItem.getReferenceItem() != null ? customFieldItem.getReferenceItem().getId() : null);
                    }
                    List<CategoryTO> lookupOptions = getCustomFieldLookupValues(filterParameter, customFieldItem).stream().map(selectItem -> {
                        CategoryTO category = new CategoryTO();
                        category.setId(selectItem.getId());
                        category.setCode(selectItem.getCode());
                        category.setTitle(selectItem.getName());
                        return category;
                    }).collect(Collectors.toList());
                    entityCategories.setList(lookupOptions);
                } else if (Constants.UI_TYPE_DROPDOWN.equalsIgnoreCase(customFieldItem.getUiType())) {
                    List<CategoryTO> categoryList = new ArrayList<>();
                    Map<String, Integer> actualValues = new LinkedHashMap<>();
                    int id = 0;
                    for (String cf : getCustomFieldValue(custom_field_id)) {
                        actualValues.put(cf, ++id);
                    }
                    List<String> predefinedValuesList = new ArrayList<>();
                    if (customFieldItem.getRelationFieldValues() != null && !customFieldItem.getRelationFieldValues().isEmpty()) {
                        predefinedValuesList = getChildsByParent(dependencySelectedItem, customFieldItem.getRelationFieldValues());
                    }
                    if (predefinedValuesList.size() == 0) {
                        predefinedValuesList = getCustomFieldValue(custom_field_id);
                    }
                    for (String values : predefinedValuesList) {
                        if (StringUtils.isNotBlank(values)) {
                            CategoryTO category = new CategoryTO();
                            category.setId(actualValues.get(values));
                            category.setTitle(values);
                            categoryList.add(category);
                        }
                    }
                    if (StringUtils.isNotBlank(query)) {
                        categoryList = categoryList.stream().filter(item -> item.getTitle().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                    }
                    entityCategories.setTotal_count(categoryList.size());
                    if (categoryList.size() < (maxLimit + start)) {
                        entityCategories.setLeft(0);
                    } else {
                        entityCategories.setLeft(categoryList.size() - (start + maxLimit));
                    }
                    ArrayList<CategoryTO> stringArrayList = new ArrayList<>(categoryList);
                    ArrayList<CategoryTO> sublist = ListUtils.getSublistSmart(stringArrayList, start, maxLimit);
                    entityCategories.setCount(sublist.size());
                    entityCategories.setOffset(start);
                    entityCategories.setList(sublist);
                } else {
                    List<CategoryTO> categoryList = new ArrayList<>();
                    List<String> predefinedValuesList = getCustomFieldValue(custom_field_id);
                    int id = 0;
                    for (String values : predefinedValuesList) {
                        if (StringUtils.isNotBlank(values)) {
                            CategoryTO category = new CategoryTO();
                            category.setId(++id);
                            category.setTitle(values);
                            categoryList.add(category);
                        }
                    }
                    if (StringUtils.isNotBlank(query)) {
                        categoryList = categoryList.stream().filter(item -> item.getTitle().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                    }
                    entityCategories.setTotal_count(categoryList.size());
                    if (categoryList.size() < (maxLimit + start)) {
                        entityCategories.setLeft(0);
                    } else {
                        entityCategories.setLeft(categoryList.size() - (start + maxLimit));
                    }
                    ArrayList<CategoryTO> stringArrayList = new ArrayList<>(categoryList);
                    ArrayList<CategoryTO> sublist = ListUtils.getSublistSmart(stringArrayList, start, maxLimit);
                    entityCategories.setCount(sublist.size());
                    entityCategories.setOffset(start);
                    entityCategories.setList(sublist);
                }
            } else {
                int real_model_field_id = custom_field_id - GAP_BTW_STATIC_AND_CUSTOM_FIELDS;
                EdsModelField modelField = modelFieldManager.get(real_model_field_id, true);

                if (modelField == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Custom field with id " + custom_field_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                }

                if (CustomFormConstants.CRM_OPPORTUNITY_TYPE.equals(modelField.getField_ID())) {
                    opportunityType = true;
                    getOpportunityItems(query, start, maxLimit, entityCategories, categories);
                } else if (CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE.equals(modelField.getField_ID())) {
                    opportunityType = false;
                    getOpportunityItems(query, start, maxLimit, entityCategories, categories);
                } else if (CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE.equals(modelField.getField_ID())) {
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

                } else if (CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE.equals(modelField.getField_ID())) {
                    ListingFilterParameter fp = new ListingFilterParameter();
                    fp.setListEmployees(true);
                    fp.setPermissionCode(PermissionConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE_LIST_VALUE);
                    SelectItem[] backups = allInOneServiceLocal.getEmployeesAsSelectItem(new ListLoadConfig(), fp);
                    if (backups != null) {
                        List<SelectItem> backupList = Arrays.asList(backups);
                        if (StringUtils.isNotBlank(query)) {
                            backupList = backupList.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                        }
                        entityCategories.setTotal_count(backupList.size());
                        if (backupList.size() < (maxLimit + start)) {
                            entityCategories.setLeft(0);
                        } else {
                            entityCategories.setLeft(backupList.size() - (start + maxLimit));
                        }
                        List<SelectItem> subList = ListUtils.getSublist(backupList, start, maxLimit);
                        entityCategories.setCount(subList.size());
                        entityCategories.setOffset(start);
                        subList.forEach(backup -> {
                            CategoryTO category = new CategoryTO();
                            category.setId(backup.getId());
                            category.setTitle(backup.getName());
                            categories.add(category);
                        });
                        entityCategories.setList(categories);
                    }
                }
            }
        }
        return successResponse(entityCategories);

    }

    private List<String> getChildsByParent(String dependencySelectedItem, String relationFieldValues) {
        List<String> predefinedValuesList = new ArrayList<>();
        if (dependencySelectedItem != null && !dependencySelectedItem.isEmpty()) {
            String[] strings = relationFieldValues.split("-:-");
            for (String string : strings) {
                String[] values = string.split("=");
                if (values.length > 1 && values[1].equalsIgnoreCase(dependencySelectedItem)) {
                    predefinedValuesList.add(values[0]);
                }
            }
        }
        return predefinedValuesList;
    }

    private void getOpportunityItems(@RequestParam(value = "query", required = false) String query, Integer
            start, Integer maxLimit, EntityCategoryTO entityCategories, ArrayList<CategoryTO> categories) throws
            RestException {
        ArrayList<EdsReference> opportunityItems;
        try {
            if (opportunityType) {
                opportunityItems = (ArrayList) referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_TYPE);
            } else {
                opportunityItems = (ArrayList) referenceManager.listReferences(EdsCrmContact._LEAD_SOURCE);
            }
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (opportunityItems != null) {
            if (StringUtils.isNotBlank(query)) {
                opportunityItems = (ArrayList) opportunityItems.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
            }
            entityCategories.setTotal_count(opportunityItems.size());
            if (opportunityItems.size() < (maxLimit + start)) {
                entityCategories.setLeft(0);
            } else {
                entityCategories.setLeft(opportunityItems.size() - (start + maxLimit));
            }
            ArrayList<EdsReference> subList = ListUtils.getSublistSmart(opportunityItems, start, maxLimit);
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

    @Operation(summary = "Create Opportunity", description = "Request to create new Opportunity. It's multipart request.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/opportunities/create", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_OPPORTUNITIES_LIST, PermissionConstants.CRM_ADD_NEW_OPPORTUNITIES})
    public Object createOpportunity(MultipartRequest multipartRequest, @RequestParam(name = "body") String
            jsonString) throws RestException {

        OpportunityAddTO opportunityAddTO;
        ObjectMapper mapper = new ObjectMapper();
        try {
            opportunityAddTO = mapper.readValue(jsonString, OpportunityAddTO.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong.", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(opportunityAddTO.getName())) {
            throw new RestException("Opportunity name is required", "name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (opportunityAddTO.getCompany() == null || opportunityAddTO.getCompany() <= 0) {
            throw new RestException("Company is required", "company is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (opportunityAddTO.getStage() == null || opportunityAddTO.getStage() <= 0) {
            throw new RestException("Opportunity stage is required", "stage is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(opportunityAddTO.getClose_date())) {
            throw new RestException("Close date is required", "close_date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsReference stage = referenceManager.get(opportunityAddTO.getStage());
        if (!stage.getAllowedRoles().isEmpty() && !userManager.getUser().hasEitherRoles(stage.getAllowedRoles().toArray(new EdsRole[]{}))) {
            throw new RestException("You don't have enough permission for this status", "don't have permission to create", ACCESS_DENIED, HttpStatus.BAD_REQUEST);
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
        if (opportunityAddTO.getCustom_fields() != null) {
            for (Object customFieldObject : opportunityAddTO.getCustom_fields()) {
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
                opportunityAddTO.getCustom_fields().add(customFieldsMap);
            }
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        OpportunityListItem item = new OpportunityListItem();
        item.setOpportunityName(opportunityAddTO.getName());
        item.setNumberData(crmServiceLocal.generateOpportunityNumber());
        if (opportunityAddTO.getAmount() != null) {
            item.setAmount(opportunityAddTO.getAmount().doubleValue());
        }
        try {
            item.setClosingDate(longDateTimezoneFormat.parse(opportunityAddTO.getClose_date()));
        } catch (ParseException e) {
            throw new RestException("Invalid date format", "Invalid date format for close_date. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        item.setAccountId(opportunityAddTO.getCompany());
        item.setContactId(opportunityAddTO.getContact());
        item.setStageId(opportunityAddTO.getStage());
        item.setAssigneeId(opportunityAddTO.getAssignee());
        item.setBackupAssigneeID(opportunityAddTO.getBackup_assignee());
        item.setCurrencyId(opportunityAddTO.getCurrency());
        if (opportunityAddTO.getAssignee() == null) {
            item.setAssigneeId(userManager.getUser().getObjectID());
        }

        Integer attachmentModelFieldId = null;

        //Get model fields by entity type
        List<ModelField> modelFields;
        try {
            modelFields = modelFieldManager.getFields(FORM_TYPES.get(EntityTypeEnum.OPPORTUNITIES.name().toLowerCase()));
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
        if (opportunityAddTO.getCustom_fields() != null && !opportunityAddTO.getCustom_fields().isEmpty()) {
            for (Object customFieldObject : opportunityAddTO.getCustom_fields()) {
                LinkedHashMap<Object, Object> customFieldsMap = (LinkedHashMap<Object, Object>) customFieldObject;
                if (customFieldsMap.get("id") != null && customFieldsMap.get("id") instanceof Integer customFieldId) {
                    if (customFieldId < GAP_BTW_STATIC_AND_CUSTOM_FIELDS) {//it means real custom field
                        customFieldObjects.add(customFieldsMap);
                    } else {
                        String fieldID = modelFieldsMap.get(customFieldId - GAP_BTW_STATIC_AND_CUSTOM_FIELDS);//it means model field
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
                                attachmentModelFieldId = customFieldId - GAP_BTW_STATIC_AND_CUSTOM_FIELDS;
                            }
                        }
                    }
                }
            }
        }

        //Map key is model field id that related to attachment, and value is attachment
        LinkedHashMap<Integer, ArrayList<MultipartFile>> attachmentsMap = new LinkedHashMap<>();
        ArrayList<MultipartFile> opportunityAttachments = new ArrayList<>();
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
                } else {
                    if (attachmentsMap.get(attachmentModelFieldId) == null) {
                        ArrayList<MultipartFile> files = new ArrayList<>();
                        files.add(file);
                        attachmentsMap.put(attachmentModelFieldId, files);
                    } else {
                        attachmentsMap.get(attachmentModelFieldId).add(file);
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
            if (CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE.equals(fieldID)) {
                item.setAssigneeId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE.equals(fieldID)) {
                item.setBackupAssigneeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_NUMBER.equals(fieldID)) {
                NumberData numberData = item.getNumberData();
                numberData.setNumberString((String) modelFieldValueMap.get(fieldID));
                item.setNumberData(numberData);
            } else if (CustomFormConstants.CRM_OPPORTUNITY_NAME.equals(fieldID)) {
                item.setOpportunityName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME.equals(fieldID)) {
                item.setAccountId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME.equals(fieldID)) {
                item.setContactId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE.equals(fieldID)) {
                item.setLeadSourceId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE.equals(fieldID)) {
                item.setCampaignId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_TYPE.equals(fieldID)) {
                item.setTypeId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP.equals(fieldID)) {
                item.setNextStep((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_AMOUNT.equals(fieldID)) {
                item.setAmount((Double) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CURRENCY.equals(fieldID)) {
                item.setCurrencyId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE.equals(fieldID)) {
                try {
                    item.setClosingDate(longDateTimezoneFormat.parse((String) modelFieldValueMap.get(fieldID)));
                } catch (ParseException e) {
                    log.error("", e);
                }
            } else if (CustomFormConstants.CRM_OPPORTUNITY_STAGE.equals(fieldID)) {
                item.setStageId((Integer) modelFieldValueMap.get(fieldID));
            } /*else if (CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY.equals(fieldID)) {
                item.setProbability((Float) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE.equals(fieldID)) {
                item.setExpectedRevenue((Double) modelFieldValueMap.get(fieldID));
            }*/ else if (CustomFormConstants.NOTES.equals(fieldID) || CustomFormConstants.CRM_NOTE.equals(fieldID)) {
                if (StringUtils.isNotBlank((String) modelFieldValueMap.get(fieldID))) {
                    note = new HistoryListItem((String) modelFieldValueMap.get(fieldID));
                }
            } else if (CustomFormConstants.CRM_OPPORTUNITY_ATTACHMENTS.equals(fieldID)) {
                if (attachmentModelFieldId != null && attachmentsMap.get(attachmentModelFieldId) != null) {
                    opportunityAttachments.addAll(attachmentsMap.get(attachmentModelFieldId));
                }
            }
        }

        CurrencyItem baseCurrency = currencyServiceLocal.getCompanyBaseCurrency();
        if (baseCurrency.getId().equals(item.getCurrencyId()) || item.getCurrencyId() == null) {
            item.setExchangeRate(BigDecimal.ONE);
            item.setCurrencyId(baseCurrency.getId());
        } else {
            CurrencyListItem exchangeRateItem = currencyServiceLocal.getCurrencyRateByDate(item.getCurrencyId(), item.getClosingDate() != null ? new DateNonConvertable(item.getClosingDate()) : new DateNonConvertable(new Date()));
            item.setExchangeRate(BigDecimal.valueOf(exchangeRateItem.getExchangeRate()));
        }
        if (opportunityAddTO.getOpportunity_sub_items() != null) {
            List<OpportunityItem> opportunityItems = new ArrayList<>();
            BigDecimal netTotal = BigDecimal.ZERO;
            BigDecimal qtyTotal = BigDecimal.ZERO;
            BigDecimal discountTotal = BigDecimal.ZERO;
            for (OpportunitySubItemsTO opportunitySubItemsTO : opportunityAddTO.getOpportunity_sub_items()) {
                OpportunityItem opportunityItem = new OpportunityItem();
                NewProduct product = new NewProduct();
                if (opportunitySubItemsTO.getId() != null) {
                    product = productService.getProductBaseData(opportunitySubItemsTO.getId(), true);
                }
                opportunityItem.setProductCategory(new SelectItem(opportunitySubItemsTO.getCategory_id()));
                opportunityItem.setItemID(opportunitySubItemsTO.getId());
                opportunityItem.setQty(opportunitySubItemsTO.getCount());
                opportunityItem.setProductBrand(new SelectItem(product.getBrandID()));
                opportunityItem.setDescription(product.getDescription());
                if (opportunitySubItemsTO.getPrice() == null) {
                    opportunityItem.setPrice(product.getSellingPrice());
                } else {
                    opportunityItem.setPrice(opportunitySubItemsTO.getPrice().getValue());
                }
                if (opportunitySubItemsTO.getDiscount() != null) {
                    BigDecimal total = opportunityItem.getPrice().multiply(opportunitySubItemsTO.getCount());
                    BigDecimal discount = total.multiply(opportunitySubItemsTO.getDiscount()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                    BigDecimal totalWithDiscount = total.subtract(discount);
                    opportunityItem.setNet(totalWithDiscount);
                    opportunityItem.setSubTotal(totalWithDiscount);
                    discountTotal = discountTotal.add(discount);
                } else {
                    opportunityItem.setNet(opportunityItem.getPrice().multiply(opportunitySubItemsTO.getCount()));
                    opportunityItem.setSubTotal(opportunityItem.getPrice().multiply(opportunitySubItemsTO.getCount()));
                }
                netTotal = netTotal.add(opportunityItem.getNet());

                List<CompanyCustomFieldItem> productCustomFieldItems = product.getProductCustomFieldItems();
                if (productCustomFieldItems != null && !productCustomFieldItems.isEmpty()) {
                    setValueStaticFieldFromCFByAliasName(opportunityItem, productCustomFieldItems);
                }
                if ((opportunitySubItemsTO.getCustom_fields() == null || opportunitySubItemsTO.getCustom_fields().isEmpty()) &&
                        (product.getProductCustomFieldItems() != null && !product.getProductCustomFieldItems().isEmpty())) {
                    ArrayList<CompanyCustomFieldItem> companyCustomFieldItems = commonService.getCompanyAllCustomFields(ViewName.OpportunitySubItem);
                    if (productCustomFieldItems != null && !productCustomFieldItems.isEmpty()) {
                        ArrayList<CompanyCustomFieldItem> resultItems = new ArrayList<>();
                        for (CompanyCustomFieldItem companyCustomFieldItem : companyCustomFieldItems) {
                            CompanyCustomFieldItem result = productCustomFieldItems.stream()
                                    .filter(x -> x.getDataType().equals(companyCustomFieldItem.getDataType()) && x.getUiType().equals(companyCustomFieldItem.getUiType()) && x.getAliasName().equals(companyCustomFieldItem.getAliasName()))
                                    .findAny().orElse(null);
                            if (result != null) {
                                result.setColumnCode(companyCustomFieldItem.getColumnCode());
                                resultItems.add(result);
                            }
                        }
                        opportunityItem.setItemCustomFields(resultItems);
                    }
                } else {
                    opportunityItem.setItemCustomFields(convertCustomFields(opportunitySubItemsTO.getCustom_fields(), Collections.emptyMap()));
                }
                if (opportunitySubItemsTO.getBrand() != null) {
                    opportunityItem.setProductBrand(new SelectItem(opportunitySubItemsTO.getBrand().getBrand_id(), opportunitySubItemsTO.getBrand().getBrand_name()));
                }
                opportunityItem.setDiscountPercent(opportunitySubItemsTO.getDiscount());
                qtyTotal = qtyTotal.add(opportunityItem.getQty());
                opportunityItems.add(opportunityItem);
            }
            if (netTotal.compareTo(BigDecimal.ZERO) > 0) {
                item.setAmount(netTotal.doubleValue());
            }
            item.setDiscountTotal(discountTotal);
            item.setQuantityTotal(qtyTotal);
            item.setTotal(netTotal);
            item.setSubTotal(netTotal);
            item.setItems(opportunityItems.toArray(new OpportunityItem[]{}));
        }

        //Calculate opportunity expected revenue and probability
        BigDecimal amount = item.getAmount() != null ? BigDecimal.valueOf(item.getAmount()) : BigDecimal.ZERO;
        Double probability = Double.valueOf(stage.getDescription());
        Double expectedRevenue = amount.doubleValue() * probability / 100;
        item.setProbability(probability.floatValue());
        item.setExpectedRevenue(expectedRevenue);
        if (note != null) {
            item.setNotes(new ArrayList<>(Collections.singletonList(note)));
        }

        Integer opportunityId;
        try {
            opportunityId = crmServiceLocal.saveOpportunityWithAttachments(item, opportunityAttachments);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (opportunityId == null) {
            throw new RestException("Opportunity number ".concat(item.getNumberData().getNumberString()).concat(" is already exist"), "Opportunity number ".concat(item.getNumberData().getNumberString()).concat(" is already exist"), CONFLICT, HttpStatus.CONFLICT);
        }

        return successResponse(new ResponseData());

    }

    @Operation(summary = "Update Opportunity Information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/opportunities/{item_id}/information", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_OPPORTUNITIES_LIST, PermissionConstants.CRM_EDIT_OPPORTUNITIES})
    public Object updateOpportunity(@PathVariable(value = "item_id") Integer
                                            item_id, @RequestBody OpportunityEditTO opportunityEditTO) throws RestException {

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        OpportunityListItem item = crmServiceLocal.editOpportunity(item_id);
        EdsReference stage = referenceManager.get(opportunityEditTO.getStage());
        EdsReference oldStage = referenceManager.get(item.getStageId());
        if ((!stage.getAllowedRoles().isEmpty() && !userManager.getUser().hasEitherRoles(stage.getAllowedRoles().toArray(new EdsRole[]{}))) ||
                (!oldStage.getAllowedRoles().isEmpty() && !userManager.getUser().hasEitherRoles(oldStage.getAllowedRoles().toArray(new EdsRole[]{})))) {
            throw new RestException("You don't have enough permission for this status", "don't have permission to update", ACCESS_DENIED, HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        if (opportunityEditTO.getName() != null) {
            item.setOpportunityName(opportunityEditTO.getName());
        }
        if (opportunityEditTO.getAmount() != null) {
            item.setAmount(opportunityEditTO.getAmount().doubleValue());
        }
        if (opportunityEditTO.getClose_date() != null) {
            try {
                item.setClosingDate(longDateTimezoneFormat.parse(opportunityEditTO.getClose_date()));
            } catch (ParseException e) {
                throw new RestException("Invalid date format", "Invalid date format for close_date. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        if (opportunityEditTO.getCompany() != null) {
            item.setAccountId(opportunityEditTO.getCompany());
        }
        if (opportunityEditTO.getContact() != null) {
            item.setContactId(opportunityEditTO.getContact());
        }
        if (opportunityEditTO.getStage() != null) {
            item.setStageId(opportunityEditTO.getStage());
        }
        if (opportunityEditTO.getAssignee() != null) {
            item.setAssigneeId(opportunityEditTO.getAssignee());
        }
        if (opportunityEditTO.getBackup_assignee() != null) {
            item.setBackupAssigneeID(opportunityEditTO.getBackup_assignee());
        }
        if (opportunityEditTO.getCurrency() != null) {
            item.setCurrencyId(opportunityEditTO.getCurrency());
        }

        if (opportunityEditTO.getNotes() != null && opportunityEditTO.getNotes().size() > 0) {
            try {
                for (NoteDto noteDto : opportunityEditTO.getNotes()) {
                    HistoryListItem note = new HistoryListItem();
                    note.setRelatedToId(EdsNoteHistory.CRM_OPPORTUNITY);
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

        try {
            crmServiceLocal.saveOpportunity(item);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Update Opportunity Additional Information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/opportunities/{item_id}/additional_information",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object updateOpportunityAdditionalInformation(
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
            modelFields = modelFieldManager.getFields(LayoutRPC.OPPORTUNITY_FORM);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //Map key is model field id and man value is model field field_id. e.g 1,CRM_OPPORTUNITY_STAGE
        LinkedHashMap<Integer, String> modelFieldsMap = new LinkedHashMap<>();
        if (modelFields != null && modelFields.size() > 0) {
            modelFields.forEach(modelField -> modelFieldsMap.put(modelField.getObjectID(), modelField.getField_ID()));
        }

        OpportunityListItem opportunityListItem = crmServiceLocal.editOpportunity(item_id);

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

        //Compare draft files to old files by unique keys: filename & file size. If there is a difference between them by name or size, delete the differ old files
        // but keep other non changed files
        ArrayList<FileResource> oldAttachments = new ArrayList<>();
        HashSet<Integer> deleteIDs = new HashSet<>();

        if (opportunityListItem.getCustomFields() != null && opportunityListItem.getCustomFields().size() > 0) {
            for (CompanyCustomFieldItem companyCustomFieldItem : opportunityListItem.getCustomFields()) {
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
                if (opportunityListItem.getCustomFields() != null && opportunityListItem.getCustomFields().size() > 0) {
                    for (CompanyCustomFieldItem companyCustomFieldItem : opportunityListItem.getCustomFields()) {
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
            opportunityListItem.setCustomFields(customFieldItems);
        }

        HistoryListItem note = null;

        for (String fieldID : modelFieldValueMap.keySet()) {
            if (CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE.equals(fieldID)) {
                opportunityListItem.setAssigneeId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE.equals(fieldID)) {
                opportunityListItem.setBackupAssigneeID((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_NUMBER.equals(fieldID)) {
                NumberData numberData = opportunityListItem.getNumberData();
                numberData.setNumberString((String) modelFieldValueMap.get(fieldID));
                opportunityListItem.setNumberData(numberData);
            } else if (CustomFormConstants.CRM_OPPORTUNITY_NAME.equals(fieldID)) {
                opportunityListItem.setOpportunityName((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME.equals(fieldID)) {
                opportunityListItem.setAccountId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME.equals(fieldID)) {
                opportunityListItem.setContactId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE.equals(fieldID)) {
                opportunityListItem.setLeadSourceId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE.equals(fieldID)) {
                opportunityListItem.setCampaignId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_TYPE.equals(fieldID)) {
                opportunityListItem.setTypeId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP.equals(fieldID)) {
                opportunityListItem.setNextStep((String) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_AMOUNT.equals(fieldID)) {
                opportunityListItem.setAmount((Double) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CURRENCY.equals(fieldID)) {
                opportunityListItem.setCurrencyId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE.equals(fieldID)) {
                try {
                    opportunityListItem.setClosingDate(longDateTimezoneFormat.parse((String) modelFieldValueMap.get(fieldID)));
                } catch (ParseException e) {
                    log.error("", e);
                }
            } else if (CustomFormConstants.CRM_OPPORTUNITY_STAGE.equals(fieldID)) {
                opportunityListItem.setStageId((Integer) modelFieldValueMap.get(fieldID));
            } else if (CustomFormConstants.NOTES.equals(fieldID) || CustomFormConstants.CRM_NOTE.equals(fieldID)) {
                if (StringUtils.isNotBlank((String) modelFieldValueMap.get(fieldID))) {
                    note = new HistoryListItem((String) modelFieldValueMap.get(fieldID));
                }
            }
        }

        if (opportunityListItem.getStageId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Opportunity stage is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        CurrencyItem baseCurrency = currencyServiceLocal.getCompanyBaseCurrency();
        if (baseCurrency.getId().equals(opportunityListItem.getCurrencyId()) || opportunityListItem.getCurrencyId() == null) {
            opportunityListItem.setExchangeRate(BigDecimal.ONE);
        } else {
            CurrencyListItem exchangeRateItem = currencyServiceLocal.getCurrencyRateByDate(opportunityListItem.getCurrencyId(), opportunityListItem.getClosingDate() != null ? new DateNonConvertable(opportunityListItem.getClosingDate()) : new DateNonConvertable(new Date()));
            opportunityListItem.setExchangeRate(BigDecimal.valueOf(exchangeRateItem.getExchangeRate()));
        }

        EdsReference stage = referenceManager.get(opportunityListItem.getStageId());
        if (stage == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Opportunity stage is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        //Calculate opportunity expected revenue and probability
        BigDecimal amount = opportunityListItem.getAmount() != null ? BigDecimal.valueOf(opportunityListItem.getAmount()) : BigDecimal.ZERO;
        Double probability = Double.valueOf(stage.getDescription());
        Double expectedRevenue = amount.doubleValue() * probability / 100;
        opportunityListItem.setProbability(probability.floatValue());
        opportunityListItem.setExpectedRevenue(expectedRevenue);

        Integer opportunityId;
        try {
            opportunityId = crmServiceLocal.saveOpportunity(opportunityListItem);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (opportunityId == null) {
            throw new RestException("Opportunity number ".concat(opportunityListItem.getNumberData().getNumberString()).concat(" is already exist"), "Opportunity number ".concat(opportunityListItem.getNumberData().getNumberString()).concat(" is already exist"), CONFLICT, HttpStatus.CONFLICT);
        }
        if (note != null) {
            try {
                note.setRelatedToId(EdsNoteHistory.CRM_OPPORTUNITY);
                note.setRelatedId(opportunityId);
                noteServiceLocal.saveNote(note);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return successResponse(new ResponseData());

    }


    @Operation(summary = "Get Opportunity Information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have opportunity information"),
            @ApiResponse(responseCode = "400", description = "item_id is required")})
    @RequestMapping(value = "/opportunities/{item_id}/information", method = RequestMethod.GET)
    public Object getOpportunityInformation(@PathVariable(value = "item_id") Integer item_id) throws RestException {
        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        OpportunityListItem opportunityListItem = crmServiceLocal.getOpportunity(item_id);
        if (opportunityListItem == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Opportunity with id " + item_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        OpportunityInformationTO opportunity = new OpportunityInformationTO();
        if (opportunityListItem.getAssignee() != null) {
            opportunity.setAssignee(new CategoryTO(opportunityListItem.getAssigneeId(), opportunityListItem.getAssignee()));
        }
        if (opportunityListItem.getBackupAssignee() != null) {
            opportunity.setBackup_assignee(new CategoryTO(opportunityListItem.getBackupAssigneeID(), opportunityListItem.getBackupAssignee()));
        }
        opportunity.setName(opportunityListItem.getOpportunityName());
        if (opportunityListItem.getAmount() != null && opportunityListItem.getAmount() != 0d) {
            Integer calculationScale = ServerUtils.getCalculationScale();
            opportunity.setAmount(BigDecimal.valueOf(opportunityListItem.getAmount()).setScale(calculationScale, RoundingMode.HALF_UP));
        }
        CurrencyItem baseCurrency = currencyServiceLocal.getBaseCurrency();
        if (opportunityListItem.getCurrency() != null) {
            opportunity.setCurrency(opportunityListItem.getCurrencyId());
        } else {
            opportunity.setCurrency(baseCurrency.getId());
        }
        if (opportunityListItem.getClosingDate() != null) {
            opportunity.setClose_date(longDateTimezoneFormat.format(opportunityListItem.getClosingDate()));
        }
        if (opportunityListItem.getCrmAccountItem() != null) {
            opportunity.setCompany(new CategoryTO(opportunityListItem.getCrmAccountItem().getObjectId(), opportunityListItem.getCrmAccountItem().getName()));
        }
        if (opportunityListItem.getContact() != null && opportunityListItem.getContactId() != null) {
            opportunity.setContact(new CategoryTO(opportunityListItem.getContactId(), opportunityListItem.getContact()));
        }
        if (opportunityListItem.getStage() != null) {
            opportunity.setStage(new CategoryTO(opportunityListItem.getStage().getId(), opportunityListItem.getStage().getName()));
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setRelationType(RelationItem.TYPE_OPPORTUNITY);
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
            opportunity.setNotes(newNotes);
        }

        ArrayList<CustomFieldsTO> customFields = new ArrayList<>();
        CustomFieldsTO customField;

        ArrayList<String> opportunityFieldTypes = new ArrayList<>();
        opportunityFieldTypes.add(CRM_OPPORTUNITY_NUMBER);
        opportunityFieldTypes.add(CRM_OPPORTUNITY_TYPE);
        opportunityFieldTypes.add(CRM_OPPORTUNITY_NEXT_STEP);
        opportunityFieldTypes.add(CRM_OPPORTUNITY_PROBABILITY);
        opportunityFieldTypes.add(CRM_OPPORTUNITY_EXPECTED_REVENUE);
        opportunityFieldTypes.add(CRM_OPPORTUNITY_CAMPAIGN_SOURCE);
        opportunityFieldTypes.add(CRM_OPPORTUNITY_LEAD_SOURCE);

        LinkedHashMap<String, Integer> modelFieldMap = new LinkedHashMap<>();
        List<EdsModelField> modelFields = modelFieldManager.getSpecificFields(LayoutRPC.OPPORTUNITY_FORM, opportunityFieldTypes);

        if (modelFields == null || modelFields.size() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Model Fields are not found with provided types", NOT_FOUND, HttpStatus.NOT_FOUND);
        }


        for (EdsModelField modelField : modelFields) {

            modelFieldMap.put(modelField.getField_ID(), modelField.getObjectID());

            if (opportunityListItem.getNumberData() != null && CRM_OPPORTUNITY_NUMBER.equals(modelField.getField_ID())) {
                customField = new CustomFieldsTO();
                customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                CustomFieldTextTO text = new CustomFieldTextTO();
                text.setId(modelFieldMap.get(CRM_OPPORTUNITY_NUMBER) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                text.setTitle(modelFieldLocalizer.localizeOpportunity(CRM_OPPORTUNITY_NUMBER));
                text.setText(opportunityListItem.getNumberData().getNumberString());
                customField.setObject(text);

                customFields.add(customField);
            }
            if (opportunityListItem.getTypeId() != null && CRM_OPPORTUNITY_TYPE.equals(modelField.getField_ID())) {
                customField = new CustomFieldsTO();
                customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                CategoryTO category = new CategoryTO(opportunityListItem.getTypeId(), opportunityListItem.getType());
                customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_OPPORTUNITY_TYPE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeOpportunity(CRM_OPPORTUNITY_TYPE), category));
                customFields.add(customField);
            }
            if (StringUtils.isNotBlank(opportunityListItem.getNextStep()) && CRM_OPPORTUNITY_NEXT_STEP.equals(modelField.getField_ID())) {
                customField = new CustomFieldsTO();
                customField.setType(CustomFieldCategoryEnum.TEXT_INPUT.name());
                CustomFieldTextTO text = new CustomFieldTextTO();
                text.setId(modelFieldMap.get(CRM_OPPORTUNITY_NEXT_STEP) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                text.setTitle(modelFieldLocalizer.localizeOpportunity(CRM_OPPORTUNITY_NEXT_STEP));
                text.setText(opportunityListItem.getNextStep());
                customField.setObject(text);

                customFields.add(customField);
            }
            if (opportunityListItem.getProbability() != null && CRM_OPPORTUNITY_PROBABILITY.equals(modelField.getField_ID())) {
                customField = new CustomFieldsTO();
                customField.setType(CustomFieldCategoryEnum.NUMBER_INPUT.name());
                CustomFieldNumberTO number = new CustomFieldNumberTO();
                number.setId(modelFieldMap.get(CRM_OPPORTUNITY_PROBABILITY) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                number.setTitle(modelFieldLocalizer.localizeOpportunity(CRM_OPPORTUNITY_PROBABILITY));
                number.setValue(String.valueOf(opportunityListItem.getProbability()));
                customField.setObject(number);

                customFields.add(customField);
            }
            if (opportunityListItem.getExpectedRevenue() != null && opportunityListItem.getExpectedRevenue() != 0d && CRM_OPPORTUNITY_EXPECTED_REVENUE.equals(modelField.getField_ID())) {
                customField = new CustomFieldsTO();
                customField.setType(CustomFieldCategoryEnum.NUMBER_INPUT.name());
                CustomFieldNumberTO number = new CustomFieldNumberTO();
                number.setId(modelFieldMap.get(CRM_OPPORTUNITY_EXPECTED_REVENUE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                number.setTitle(modelFieldLocalizer.localizeOpportunity(CRM_OPPORTUNITY_EXPECTED_REVENUE));
                number.setValue(String.valueOf(opportunityListItem.getExpectedRevenue()));
                customField.setObject(number);

                customFields.add(customField);
            }
            if (opportunityListItem.getCampaignId() != null && CRM_OPPORTUNITY_CAMPAIGN_SOURCE.equals(modelField.getField_ID())) {
                customField = new CustomFieldsTO();
                customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                CategoryTO category = new CategoryTO(opportunityListItem.getCampaignId(), opportunityListItem.getCampaign());
                customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_OPPORTUNITY_CAMPAIGN_SOURCE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeOpportunity(CRM_OPPORTUNITY_CAMPAIGN_SOURCE), category));
                customFields.add(customField);
            }
            if (opportunityListItem.getLeadSourceId() != null && CRM_OPPORTUNITY_LEAD_SOURCE.equals(modelField.getField_ID())) {
                customField = new CustomFieldsTO();
                customField.setType(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
                CategoryTO category = new CategoryTO(opportunityListItem.getLeadSourceId(), opportunityListItem.getLeadSource());
                customField.setObject(new CustomFieldCategoryChooseTO(modelFieldMap.get(CRM_OPPORTUNITY_LEAD_SOURCE) + GAP_BTW_STATIC_AND_CUSTOM_FIELDS, modelFieldLocalizer.localizeOpportunity(CRM_OPPORTUNITY_LEAD_SOURCE), category));
                customFields.add(customField);
            }
        }
        customFields.addAll(getCustomFields(opportunityListItem.getCustomFields()));
        opportunity.setCustomFields(customFields);

       /* if (opportunityListItem.getNotes() != null && opportunityListItem.getNotes().size() > 0) {
            ArrayList<NoteDto> notes = new ArrayList<>();
            for (HistoryListItem historyListItem: opportunityListItem.getNotes()) {
                NoteDto noteDto = new NoteDto();
                noteDto.setId(historyListItem.getObjectID());
                noteDto.setText(historyListItem.getComment());
                if (historyListItem.isVisibility() == null) {
                    noteDto.setVisibility("INTERNAL");
                } else if (historyListItem.isVisibility()) {
                    noteDto.setVisibility("PRIVATE");
                } else {
                    noteDto.setVisibility("PUBLIC");
                }
                notes.add(noteDto);
            }
            opportunity.setNotes(notes);
        }*/
        return successResponse(new EntityInformationResultTO(opportunity));
    }


    private void setValueStaticFieldFromCFByAliasName(OpportunityItem
                                                              opportunityItem, List<CompanyCustomFieldItem> productCustomFieldItems) {
        for (CompanyCustomFieldItem productCFItem : productCustomFieldItems) {
            if (productCFItem != null && productCFItem.getAliasName() != null) {
                switch (productCFItem.getAliasName()) {
                    case ItemTableConstants.DESCRIPTION -> {
                        if ((StringUtils.isBlank(opportunityItem.getDescription())) &&
                                StringUtils.isNotBlank(productCFItem.getFieldStringValue()) && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType()))) {
                            opportunityItem.setDescription(productCFItem.getFieldStringValue());
                        }
                    }
                    case ItemTableConstants.QTY -> {
                        if (opportunityItem.getQty() == null &&
                                !Utils.isNullOrEmpty(productCFItem.getFieldStringValue()) && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            opportunityItem.setQty(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                    }
                    case ItemTableConstants.MEASUREMENT -> {
                        if (opportunityItem.getUnitMeasurement() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.UNIT_MEASUREMENT.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setUnitMeasurement(new SelectItem(productCFItem.getSelectedId(), productCFItem.getFieldStringValue()));
                        }
                    }
                    case UNITPRICE -> {
                        if (opportunityItem.getPrice() == null &&
                                !Utils.isNullOrEmpty(productCFItem.getFieldStringValue()) && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            opportunityItem.setPrice(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                    }
                    case ItemTableConstants.DISCOUNT_AMT -> {
                        if (opportunityItem.getDiscountAmount() == null &&
                                !Utils.isNullOrEmpty(productCFItem.getFieldStringValue()) && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            opportunityItem.setDiscountAmount(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                    }
                    case ItemTableConstants.SUPPLIER -> {
                        if (opportunityItem.getSupplierID() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.SUPPLIER.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setSupplierID(productCFItem.getSelectedId());
                            opportunityItem.setSupplierName(productCFItem.getFieldStringValue());
                        }
                    }
                    case ItemTableConstants.CATEGORY -> {
                        if (opportunityItem.getProductCategory() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.PRODUCT_CATEGORY.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setProductCategory(new SelectItem(productCFItem.getSelectedId(), productCFItem.getFieldStringValue()));
                        }
                    }
                    case ItemTableConstants.PROJECT -> {
                        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE) && opportunityItem.getProject() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.PROJECT.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setProject(new SelectItem(productCFItem.getSelectedId(), productCFItem.getFieldStringValue()));
                        }
                    }
                }
            }
        }
    }
}
