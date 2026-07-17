package com.edatasite.workforce.rest.v2.release10.crm;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsUserFilter;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.FacetFilterManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.UserFilterManager;
import com.edatasite.workforce.gwt.crm.client.rpc.CaseList;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.note.server.NoteServiceLocal;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ListingFilterHelper;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.NoteTO;
import com.edatasite.workforce.rest.base.to.crm.CaseTO;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApiResult;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.RequestListSearchData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseResultListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SelectItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ChangeOpportunityStatusRequestTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.IdDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Anvar Akramov on 26/03/2020.
 */
@Tag(name = "Case", description = "Case API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiCaseControllerV2 extends BaseApiControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(ApiCaseControllerV2.class);

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
    private ReferenceManager referenceManager;
    @Autowired
    private NoteServiceLocal noteServiceLocal;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private UserFilterManager userFilterManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;


    private FacetFilterRpc getOneTimeFilter(FacetFilterRpc defaultFilter) {
        if (defaultFilter == null) {
            return null;
        }
        defaultFilter.setType(ListPanelType.CaseListPanelOTF);
        defaultFilter.setUserID(userManager.getUser().getObjectID());

        FacetFilterRpc otf = commonServiceLocal.getUserFacetFilter(defaultFilter);
        otf.setName("OTF");
        otf.setDefaultFilter(true);
        otf.setType(ListPanelType.CaseListPanelOTF);

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
        FacetFilterRpc mainMergedFilter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.CaseListPanel).getFacetFilter();//new FacetFilterRpc(ListPanelType.LeadListPanel, showSolrFieldMap, showFacetCodeName);


        //Custom fields which are facetable
        ArrayList<CompanyCustomFieldItem> casesCustomFields = commonServiceLocal.getCompanyCustomFieldsForListView(ViewName.CrmCase);

        if (casesCustomFields != null && casesCustomFields.size() > 0) {
            casesCustomFields.forEach(companyCustomFieldItem -> {
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

    @Operation(summary = "Get Cases List", description = "Retrieves Cases List")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Sales Orders details "),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/cases/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@RequestBody RequestListSearchData requestListSearchData) throws RestException {

        if (requestListSearchData.getStart() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListSearchData.getLimit() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListSearchData.getStart().equals(requestListSearchData.getLimit()) && requestListSearchData.getLimit() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point and limit can not be zero at the same time", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(requestListSearchData.getStart());
        filterParameter.setLimit(requestListSearchData.getLimit());
        filterParameter.setSearchKey(requestListSearchData.getSearch_text());

        //Get List of Cases from Solr
        CaseList caseList = crmServiceLocal.getCases(filterParameter);

        ResponseResultListData<CaseTO> resultListData = new ResponseResultListData<>();

        if (caseList == null || caseList.getList() == null) {
            return successResponse(resultListData);
        }
        //Convert to API Transfer object
        List<CaseTO> resultList = caseList.getList().stream().map(this::convert).collect(Collectors.toList());


        resultListData.setList(resultList);
        resultListData.setTotal(caseList.getTotal());
        return successResponse(resultListData);
    }

    @Operation(summary = "Change Case Status", description = "Changes the status of the case")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error codes"),
            @ApiResponse(responseCode = "400", description = "item_id is required"),
            @ApiResponse(responseCode = "400", description = "status_id is required")})
    @RequestMapping(value = "/cases/change_status", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @CheckPermission(permissions = {PermissionConstants.CRM_CASES_LIST})
    public Object changeCaseStatus(@RequestBody ChangeOpportunityStatusRequestTO changeItemStatus) throws RestException {
        if (changeItemStatus.getItem_id() == null || changeItemStatus.getItem_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        CaseItem item = crmServiceLocal.getCase(changeItemStatus.getItem_id(), false);
        if (item == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Case with id " + changeItemStatus.getItem_id() + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        try {
            crmServiceLocal.updateCaseStatus(changeItemStatus.getItem_id(), changeItemStatus.getStatus_id(), null);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return successResponse(new ResponseData());
    }


    @Operation(summary = "Case Detail Info")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have case details"),
            @ApiResponse(responseCode = "400", description = "id is required")})
    @RequestMapping(value = "/cases/{id}", method = RequestMethod.GET)
    public Object getCaseDetails(@PathVariable(value = "id") Integer id) throws RestException {
        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        CaseItem caseItem = crmServiceLocal.getCase(id, true);

        if (caseItem == null || caseItem.getObjectId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Case with id " + id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        //convert and return
        return successResponse(convert(caseItem));
    }

    private CaseTO convert(CaseItem caseItem) {

        CaseTO result = new CaseTO();
//        result.setCcEmails(getCcEmails());
//        result.setReplyTo(getReplyTo());
        result.setCase_number(caseItem.getCaseNumber());
        result.setId(caseItem.getObjectId());
        result.setSubject(caseItem.getSubject());
        result.setInternal_updated_date(caseItem.getInternalUpdatedDate());
        result.setCreated_date(caseItem.getCreatedDate());
        result.setClosed_date(caseItem.getClosedDate());
        /*result.setReportedBy(getReportedBy());
        result.setTrackerID(getTracker() != null ? getTracker().getObjectID() : null);
        result.setEmailID(getEmailID());
        result.setInternalComment(getInternalComment());
        result.setDescription(!isBrief ? replaceAllUrl(EmailUtils.retrieveContent(getDescription(), null, new StringBuilder(), null).toString()) : null);
        result.setEmail(getEmail());
        result.setPhone(getPhone());
        result.setClosedDate(getClosedDate());
        result.setCreatedDate(getAuditInfo().getCreationDate());
        result.setLastUpdatedDate(getAuditInfo().getModificationDate());
        result.setFilterID(getFilterID());
        result.setAuditInfoResource(getAuditInfo().getDTO());
        result.setBrandId(getBrandId());
        result.setProductCategoryId(getProductCategoryId());
        result.setProductId(getProductId());
        result.setProblemId(getProblemId());
        if (getEntityID() != null) {
            item.setEntityID(getEntityID());
        }*/
        if (caseItem.getCaseOriginId() != null) {
            result.setOrigin(new SelectItemTO(caseItem.getCaseOriginId(), caseItem.getCaseOrigin(), caseItem.getCaseOriginCode()));
        }
        if (caseItem.getPriorityId() != null) {
            result.setPriority(new SelectItemTO(caseItem.getPriorityId(), caseItem.getPriority(), caseItem.getPriorityCode()));
        }
        if (caseItem.getTypeId() != null) {
            result.setPriority(new SelectItemTO(caseItem.getTypeId(), caseItem.getType(), caseItem.getTypeCode()));
        }
        if (caseItem.getCrmContactID() != null) {
            /*item.setCrmContact(getCrmContact().getName());
            item.setCrmContactID(getCrmContact().getObjectID());
            List<EdsCrmContactItemParams> fax = getCrmContact().getItemParams(EdsCrmContactItemParams.G_HOME_FAX);
            if (fax == null || fax.size() == 0) {
                fax = getCrmContact().getItemParams(EdsCrmContactItemParams.G_WORK_FAX);
            }
            if (fax != null && fax.size() > 0) {
                item.setFax(fax.get(0) != null ? fax.get(0).getValue() : "");
            }
            if ("".equals(item.getCompany()) && getCrmContact() != null && getCrmContact().getCrmAccount() != null) {
                item.setCompany(getCrmContact().getCrmAccount().getName());
            }*/
            result.setContact(new IdNameTO(caseItem.getCrmContactID(), caseItem.getCrmContact()));
        }
        if (caseItem.getAccountId() != null) {
            /*item.setAccountNumber(getCrmAccount().getNumber());
            item.setAccountName(getCrmAccount().getName());
            item.setAccountId(getCrmAccount().getObjectID());
            item.setCompany(getCrmAccount().getName());
            item.setFax(getCrmAccount().getFax());*/
            result.setCompany(new IdNameTO(caseItem.getAccountId(), caseItem.getAccountName()));
        }
        if (caseItem.getLeadId() != null) {
//            item.setLead(getLead().getName());
//            item.setLeadId(getLead().getObjectID());
//            item.setCompany(getLead().getCrmAccount() != null ? getLead().getCrmAccount().getName() : null);
//            item.setFax(EdsCrmContactItemParams.getFirstItemParamValue(getLead().getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.HOME_FAX, EdsCrmContactItemParams.WORK_FAX));
            result.setLead(new IdNameTO(caseItem.getLeadId(), caseItem.getLead()));
        }
        if (caseItem.getPotentialId() != null) {
            result.setOpportunity(new IdNameTO(caseItem.getPotentialId(), caseItem.getPotentialName()));
        }
        if (caseItem.getResolverId() != null) {
            result.setResolver(new IdNameTO(caseItem.getResolverId(), caseItem.getResolverName()));
        }
        if (caseItem.getCaseAssigneeId() != null) {
            result.setAssignee(new IdNameTO(caseItem.getCaseAssigneeId(), caseItem.getCaseAssigneeName()));
        }
        if (caseItem.getDepartmentID() != null) {
            result.setDepartment(new IdNameTO(caseItem.getDepartmentID(), caseItem.getDepartment()));
        }
        if (caseItem.getStatus() != null) {
            result.setStatus(new SelectItemTO(caseItem.getStatus().getObjectID(), caseItem.getStatus().getName(), caseItem.getStatusCode()));
        }
        if (caseItem.getCaseReasonId() != null) {
            result.setReason(new SelectItemTO(caseItem.getCaseReasonId(), caseItem.getCaseReason(), caseItem.getCaseReasonCode()));
        }
        if (caseItem.getInternalStatusId() != null) {
            result.setInternal_status(new IdNameTO(caseItem.getInternalStatusId(), caseItem.getInternalStatusName()));
        }
        ListingFilterParameter listingFilterParameter = new ListingFilterParameter();
        listingFilterParameter.setRelationID(caseItem.getObjectId());
        listingFilterParameter.setRelationType(RelationItem.TYPE_CASE);
        ListResultTO<NoteTO> notes = noteServiceLocal.getNoteListForAPI(listingFilterParameter);
        if (notes != null && notes.getTotalNumber() > 0) {
            result.setCase_notes(notes.getItems());
        }


        return result;
    }

    @Operation(summary = "Delete Case", description = "Delete , and if no give user message: You don't have permissions to delete this entry. Please contact your administrator")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/cases/{id}", method = RequestMethod.DELETE)
    @CheckPermission(permissions = {PermissionConstants.CRM_CASES_LIST, PermissionConstants.CRM_REMOVE_CASE})
    public Object deleteCase(@PathVariable(value = "id") Integer caseId) throws RestException {

        if (caseId == null || caseId <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        CaseItem caseItem = crmServiceLocal.getCase(caseId, false);
        if (caseItem == null || caseItem.getObjectId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Case with id " + caseId + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (ServerUtils.hasPermission(PermissionConstants.CRM_REMOVE_CASE)) {
            try {
                ArrayList<Integer> objectIDs = new ArrayList<>();
                objectIDs.add(caseId);
                crmServiceLocal.deleteCase(objectIDs);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
        }
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Get Case Status List", description = "Retrieves list of Case Statuses ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Case statuses")})
    @RequestMapping(value = "/cases/statuses", method = RequestMethod.GET)
    public Object getStatusesList() throws RestException {

        List<EdsReference> edsReferences = referenceManager.listReferences(EdsCase._CASE_STATUS);
        List<CategoryTO> caseInternalStatuses = edsReferences.stream().map(edsReference -> new CategoryTO(edsReference.getObjectID(), referenceWfmMessageSource.localize(edsReference.getCode(), edsReference.getName()))).collect(Collectors.toList());

        return successResponse(new ResponseListData<>(caseInternalStatuses));
    }

    @Operation(summary = "Get Case Priority List", description = "Retrieves list of Case Priorities ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Case Priorities")})
    @RequestMapping(value = "/cases/priorities", method = RequestMethod.GET)
    public Object getPriorityList() throws RestException {

        List<EdsReference> edsReferences = referenceManager.listReferences(EdsCase._CASE_PRIORITY);
        List<CategoryTO> casePriorities = edsReferences.stream().map(edsReference -> new CategoryTO(edsReference.getObjectID(), referenceWfmMessageSource.localize(edsReference.getCode(), edsReference.getName()))).collect(Collectors.toList());

        return successResponse(new ResponseListData<>(casePriorities));
    }

    @Operation(summary = "Get Case Reason List", description = "Retrieves list of Case Reasons ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Case Reasons")})
    @RequestMapping(value = "/cases/reasons", method = RequestMethod.GET)
    public Object getReasonList() throws RestException {

        List<EdsReference> edsReferences = referenceManager.listReferences(EdsCase._CASE_REASON);
        List<CategoryTO> caseReasons = edsReferences.stream().map(edsReference -> new CategoryTO(edsReference.getObjectID(), referenceWfmMessageSource.localize(edsReference.getCode(), edsReference.getName()))).collect(Collectors.toList());

        return successResponse(new ResponseListData<>(caseReasons));
    }

    @Operation(summary = "Get Case Origins List", description = "Retrieves list of Case origins ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Case Origins")})
    @RequestMapping(value = "/cases/origins", method = RequestMethod.GET)
    public Object getOriginsList() throws RestException {

        List<EdsReference> edsReferences = referenceManager.listReferences(EdsCase._CASE_ORIGIN);
        List<CategoryTO> caseOrigins = edsReferences.stream().map(edsReference -> new CategoryTO(edsReference.getObjectID(), referenceWfmMessageSource.localize(edsReference.getCode(), edsReference.getName()))).collect(Collectors.toList());

        return successResponse(new ResponseListData<>(caseOrigins));
    }

    @Operation(summary = "Get Case Types List", description = "Retrieves list of Case types ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Case Types")})
    @RequestMapping(value = "/cases/types", method = RequestMethod.GET)
    public Object getCaseTypesList() throws RestException {

        List<EdsReference> edsReferences = referenceManager.listReferences(EdsCase._CASE_TYPE);
        List<CategoryTO> caseOrigins = edsReferences.stream().map(edsReference -> new CategoryTO(edsReference.getObjectID(), referenceWfmMessageSource.localize(edsReference.getCode(), edsReference.getName()))).collect(Collectors.toList());

        return successResponse(new ResponseListData<>(caseOrigins));
    }

    @Operation(summary = "Create Case", description = "Request to create new Case. It's multipart request.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/cases/create", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_CASES_LIST/*, PermissionConstants.CRM_CASE_ADD*/})
    public Object createCase(MultipartRequest multipartRequest, @Parameter(name = "body", description = "{" +
            "\"id\":0," +
            "\"case_number\":\"casenumber\"," +
            "\"subject\":\"subject\"," +
            "\"description\":\"description here\"," +
            "\"type\":{\"id\":0,\"name\":\"One of Problem/Demo Request/Feature Request/Question/Request etc\"}," +
            "\"contact\":{\"id\":0,\"name\":\"contact name\"},\"lead\":{\"id\":0,\"name\":\"lead name\"}," +
            "\"company\":{\"id\":0,\"name\":\"company name\"}," +
            "\"other\":{\"first_name\":\"John\"," +
            "\"last_name\":\"Smith\",\"email\":\"john.smith@email.com\"," +
            "\"phone\":\"phonenumber\"," +
            "\"fax\":\"faxnumber\"," +
            "\"company_name\":\"John Co\"}," +
            "\"origin\":{\"id\":0," +
            "\"name\":\"name\",\"code\":\"code\"},\"reason\":{\"id\":0,\"name\":\"name\",\"code\":\"code\"},\"other_reason\":\"string\",\"priority\":{\"id\":0,\"name\":\"name\",\"code\":\"code\"},\"status\":{\"id\":0,\"name\":\"name\",\"code\":\"code\"},\"opportunity\":{\"id\":0,\"name\":\"opportunity name\"},\"assignee\":{\"id\":0,\"name\":\"assignee name\"},\"department\":{\"id\":0,\"name\":\"department name\"},\"resolver\":{\"id\":0,\"name\":\"resolver name\"},\"internal_status\":{\"id\":0,\"name\":\"status\"}," +
            "\"created_date\":1585228189327,\"closed_date\":1585228189327,\"internal_updated_date\":1585228189327,\"notes\":\"notes here ...\"}\n", ref = "string") @RequestParam(name = "body") String jsonString) throws RestException {

        CaseTO newCaseTO;
        ObjectMapper mapper = new ObjectMapper();
        try {
            newCaseTO = mapper.readValue(jsonString, CaseTO.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong.", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        CaseItem item = convert(newCaseTO);

        Integer caseId;
        try {
            caseId = crmServiceLocal.saveCase(item, false).getId();
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (caseId == null || caseId <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Case couldn't be created ", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return successResponse(new IdDTO(caseId));

    }

    private CaseItem convert(CaseTO newCaseTO) {
        CaseItem item = new CaseItem();
        item.setObjectId(newCaseTO.getId());
        item.setSubject(newCaseTO.getSubject());
        item.setDescription(newCaseTO.getDescription());
        if (newCaseTO.getCompany() != null) {
            item.setAccountId(newCaseTO.getCompany().getId());
            item.setAccountName(newCaseTO.getCompany().getName());
        }
        if (newCaseTO.getContact() != null) {
            item.setCrmContactID(newCaseTO.getContact().getId());
            item.setCrmContact(newCaseTO.getContact().getName());
        }
        if (newCaseTO.getLead() != null) {
            item.setLeadId(newCaseTO.getLead().getId());
            item.setLead(newCaseTO.getLead().getName());
        }
        if (newCaseTO.getOther() != null) {
            item.setFirstName(newCaseTO.getOther().getFirst_name());
            item.setLastName(newCaseTO.getOther().getLast_name());
            item.setCompany(newCaseTO.getOther().getCompany_name());
            item.setEmail(newCaseTO.getOther().getEmail());
            item.setPhone(newCaseTO.getOther().getPhone());
            item.setFax(newCaseTO.getOther().getFax());
        }
        if (newCaseTO.getType() != null) {
            item.setTypeId(newCaseTO.getType().getId());
        }
        if (newCaseTO.getOrigin() != null) {
            item.setCaseOriginId(newCaseTO.getOrigin().getId());
        }
        if (newCaseTO.getReason() != null) {
            item.setCaseReasonId(newCaseTO.getReason().getId());
        } else {
            item.setOtherReason(newCaseTO.getOther_reason());
        }
        if (newCaseTO.getPriority() != null) {
            item.setPriorityId(newCaseTO.getPriority().getId());
        }
        if (newCaseTO.getStatus() != null) {
            item.setStatus(new SelectItem(newCaseTO.getStatus().getId(), newCaseTO.getStatus().getName(), newCaseTO.getStatus().getCode()));
        }
        if (newCaseTO.getAssignee() != null) {
            item.setCaseAssigneeId(newCaseTO.getAssignee().getId());
            item.setCaseAssigneeName(newCaseTO.getAssignee().getName());
        }
        if (newCaseTO.getDepartment() != null) {
            item.setDepartmentID(newCaseTO.getDepartment().getId());
            item.setDepartment(newCaseTO.getDepartment().getName());
        }
        if (newCaseTO.getResolver() != null) {
            item.setResolverId(newCaseTO.getResolver().getId());
        }
        if (newCaseTO.getInternal_status() != null) {
            item.setInternalStatusId(newCaseTO.getInternal_status().getId());
        }
        if (newCaseTO.getNotes() != null) {
            HistoryListItem newNote = new HistoryListItem();
            newNote.setComment(newCaseTO.getNotes());
            ArrayList<HistoryListItem> notes = new ArrayList<>();
            notes.add(newNote);
            item.setNotes(notes);
        }
        return item;
    }

    @Operation(summary = "Update Case")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/cases/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_CASES_LIST, PermissionConstants.CRM_EDIT_CASE})
    public ApiResult updateCase(@RequestBody CaseTO caseEditTO) throws RestException {

        if (caseEditTO.getId() == null || caseEditTO.getId() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        CaseItem item = convert(caseEditTO);
        Integer caseId;
        try {
            caseId = crmServiceLocal.saveCase(item, false).getId();
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (caseId == null || caseId <= 0) {
//            throw new RestException("Case number ".concat(item.getNumberData().getNumberString()).concat(" is already exist"), "Opportunity number ".concat(item.getNumberData().getNumberString()).concat(" is already exist"), CONFLICT, HttpStatus.CONFLICT);
            throw new RestException(GENERAL_ERROR_MESSAGE, "Case couldn't be created ", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return successResponse(convert(crmServiceLocal.getCase(caseId, false)));
    }

}
