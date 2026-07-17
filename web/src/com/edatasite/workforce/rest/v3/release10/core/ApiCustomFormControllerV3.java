package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.workforce.gwt.core.client.form.CustomizeFormItem;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterCutomField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.SaveFilterSelectItems;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEventRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CustomFormManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SelectItemTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.service.ApiCustomFormService;
import com.edatasite.workforce.rest.v3.release10.core.service.CustomPredefinedValueService;
import com.edatasite.workforce.rest.v3.release10.core.to.CFItemExistanceDto;
import com.edatasite.workforce.rest.v3.release10.core.to.CheckExistanceRequestDto;
import com.edatasite.workforce.rest.v3.release10.core.to.CustomFieldTo;
import com.edatasite.workforce.rest.v3.release10.core.to.CustomFormDto;
import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Custom Forms", description = "Custom Forms Api")
@RestController
@RequestMapping(value = "/custom-form", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiCustomFormControllerV3 extends BaseApiControllerV3 {
    private final ApiCustomFormService apiCustomFormService;
    private final AllInOneServiceLocal allInOneService;
    private final RbacService rbacService;
    private final CustomPredefinedValueService customPredefinedValueService;
    private final CommonService commonService;
    private final CustomFormManager customFormManager;


    public ApiCustomFormControllerV3(ApiCustomFormService apiCustomFormService,
                                     AllInOneServiceLocal allInOneService,
                                     RbacService rbacService,
                                     CustomPredefinedValueService customPredefinedValueService,
                                     CommonService commonService,
                                     CustomFormManager customFormManager) {
        this.apiCustomFormService = apiCustomFormService;
        this.allInOneService = allInOneService;
        this.rbacService = rbacService;
        this.customPredefinedValueService = customPredefinedValueService;
        this.commonService = commonService;
        this.customFormManager = customFormManager;
    }

    @Operation(summary = "Add Score for quiz custom form items")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Add Quiz form score"))
    @PostMapping(path = "/quiz_score", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Boolean> saveQuizFormFieldsScore(@RequestBody CustomFieldTo fieldTo) throws RestException {
        apiCustomFormService.saveScores(fieldTo);
        return ResultTO.success(true);
    }

    @Operation(summary = "Get Quiz Custom Form Scores", description = "Get Quiz Custom Form Scores")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have get Quiz Custom Form fields score values"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/quiz-form/score", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<CustomFieldTo>> getQuizFormFieldsScore(@RequestBody DynamicDto dto) throws RestException {
        return ResultTO.success(apiCustomFormService.getScores(dto));
    }

    @Operation(summary = "Get Custom Form Items list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Custom Form Items"))
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ListResultTO<CustomFormDto>> getList(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.CustomFormItemsPanel);

        return ResultTO.success(apiCustomFormService.getCustomFormItemsList(fp));
    }

    @Operation(summary = "Get existing Custom Form Item by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Custom Form Item"))
    @PostMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<CustomFormDto> getById(@RequestBody IdCode dto) throws RestException {

        return ResultTO.success(apiCustomFormService.getCustomFormItemById(dto));
    }

    @Operation(summary = "Create new Custom Form Item")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Custom Form Item"))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<IdCode> create(@RequestBody CustomFormDto dto) throws RestException {
        if (!dto.isNew()) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Custom Form Item ID is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        return ResultTO.success(apiCustomFormService.save(dto, true));
    }

    @Operation(summary = "Update existing Custom Form Item")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Custom Form Item"))
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<IdCode> update(@RequestBody CustomFormDto dto) throws RestException {
        if (dto.isNew()) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Custom Form Item ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        return ResultTO.success(apiCustomFormService.save(dto, false));
    }

    @Operation(summary = "Patch Update existing Custom Form Item")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Custom Form Item"))
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<IdCode> patchUpdate(@RequestBody CustomFormDto dto) throws RestException {
        if (dto.isNew()) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Custom Form Item ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        return ResultTO.success(apiCustomFormService.patchUpdate(dto));
    }

    @Operation(summary = "Check existance Custom Form Item")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Custom Form Item"))
    @PostMapping(value = "/check_existance", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<CFItemExistanceDto> checkExistance(@RequestBody CheckExistanceRequestDto dto) {
        return ResultTO.success(apiCustomFormService.checkExistance(dto));
    }

    @Operation(summary = "Get Form Properties")
    @GetMapping(value = "/form-properties", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<List<CustomizeFormItem>>> getFields(@RequestParam("formId") String formId) {
        List<List<CustomizeFormItem>> formProperties = apiCustomFormService.getFormProperties(formId);
        return ResultTO.success(formProperties);
    }

    @Operation(summary = "Get dropdown by field")
    @GetMapping(value = "/dropdown", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<SelectItem[]> getDropdown(@RequestParam("formId") String formId,
                                              @RequestParam("fieldId") String fieldId) {
        SelectItem[] response = customPredefinedValueService.predefinedValue(fieldId, formId);
        return ResultTO.success(response);
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getTaskFacetFilterData(@RequestParam(value = "search", required = false) String search,
                                         @RequestParam("type") String type,
                                         @RequestParam("fields") String fields) {
        FacetFilterRpc taskFacet = new FacetFilterRpc();
        taskFacet.setType(ListPanelType.valueOf(type));
        ArrayList<String> visibleFields = Arrays.stream(fields.split(",")).map(String::trim).collect(Collectors.toCollection(ArrayList::new));
        taskFacet.setFilterChanges(true);
        taskFacet.setShowFacetCodeName(visibleFields);
        taskFacet.setShowSolrFieldMap(getTaskSolrFields());
        taskFacet.setSearchKey(search);
        taskFacet.setFacetContentMap(getTaskFacetFilterRpc());
        FacetFilterRpc taskFacetFilterData = rbacService.getTaskFacetFilterData(taskFacet, false);
        HashMap<String, FacetContentRpc> response = new HashMap<>();
        if (taskFacetFilterData.getFacetContentMap() != null) {
            for (String s : taskFacetFilterData.getFacetContentMap().keySet()) {
                if (visibleFields.contains(s)) {
                    FacetContentRpc facetContentRpc = taskFacetFilterData.getFacetContentMap().get(s);
                    if (search != null && !search.isEmpty()) {
                        SelectItem[] selectItems = Arrays.stream(facetContentRpc.getFacetItems())
                                .filter(f -> f.getName() != null)
                                .filter(f -> f.getName().toLowerCase().contains(search.toLowerCase()))
                                .toArray(SelectItem[]::new);
                        facetContentRpc.setFacetItems(selectItems);
                    }
                    response.put(s, facetContentRpc);
                }
            }
        }
        return response;
    }

    static HashMap<String, FacetSolrField> getTaskSolrFields() {
        final String[] contentCodes = FacetContentType.TaskFacetFilter.getContentCode();

        return new HashMap<>() {{
            this.put(contentCodes[0], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_PROJECT_ID, SolrTaskRepresenter.FIELD_TASK_PROJECT_ID_NAME));
            this.put(contentCodes[1], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_ID, SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_ID_NAME));
            this.put(contentCodes[2], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_WORKSTREAM_ID, SolrTaskRepresenter.FIELD_TASK_WORKSTREAM_ID_NAME));
            this.put(contentCodes[18], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS_ID, SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[3], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_STATUS_ID, SolrTaskRepresenter.FIELD_TASK_STATUS_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[19], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_PROJECT_MANAGER_ID, SolrTaskRepresenter.FIELD_TASK_PROJECT_MANAGER_ID_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[4], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_PRIORITY_ID, SolrTaskRepresenter.FIELD_TASK_PRIORITY_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[5], new FacetSolrField(SolrTaskRepresenter.FIELD_USER_ID, SolrTaskRepresenter.FIELD_USER_ID_NAME));
            this.put(contentCodes[6], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CONTACT, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CONTACT));
            this.put(contentCodes[7], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CRM_ACCOUNT, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CRM_ACCOUNT));
            this.put(contentCodes[8], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_LEAD, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_LEAD));
            this.put(contentCodes[9], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CASE, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CASE));
            this.put(contentCodes[10], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_OPPORTUNITY, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_OPPORTUNITY));
            this.put(contentCodes[11], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_PROJECT, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_PROJECT));
            this.put(contentCodes[12], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_EVENT, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_EVENT));
            this.put(contentCodes[13], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_ISSUE, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_ISSUE));
            this.put(contentCodes[14], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_EMPLOYEE, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_EMPLOYEE));
            this.put(contentCodes[15], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_DEPARTMENT, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_DEPARTMENT));
            this.put(contentCodes[16], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CLIENT, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CLIENT));
            this.put(contentCodes[17], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_SUPPLIER, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_SUPPLIER));
            this.put(contentCodes[4], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_TYPE_ID, SolrTaskRepresenter.FIELD_TASK_TYPE_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[20], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_TYPE_ID, SolrTaskRepresenter.FIELD_TASK_TYPE_ID_CODE_NAME, LocalizationType.REFERENCE));
        }};
    }

    static HashMap<String, FacetContentRpc> getTaskFacetFilterRpc() {
        final String[] contentCodes = FacetContentType.TaskFacetFilter.getContentCode();

        return new HashMap<>() {{
            this.put(contentCodes[0], new FacetContentRpc());
            this.put(contentCodes[1], new FacetContentRpc());
            this.put(contentCodes[2], new FacetContentRpc());
            this.put(contentCodes[18], new FacetContentRpc());
            this.put(contentCodes[3], new FacetContentRpc());
            this.put(contentCodes[19], new FacetContentRpc());
            this.put(contentCodes[4], new FacetContentRpc());
            this.put(contentCodes[5], new FacetContentRpc());
            this.put(contentCodes[6], new FacetContentRpc());
            this.put(contentCodes[7], new FacetContentRpc());
            this.put(contentCodes[8], new FacetContentRpc());
            this.put(contentCodes[9], new FacetContentRpc());
            this.put(contentCodes[10], new FacetContentRpc());
            this.put(contentCodes[11], new FacetContentRpc());
            this.put(contentCodes[12], new FacetContentRpc());
            this.put(contentCodes[13], new FacetContentRpc());
            this.put(contentCodes[14], new FacetContentRpc());
            this.put(contentCodes[15], new FacetContentRpc());
            this.put(contentCodes[16], new FacetContentRpc());
            this.put(contentCodes[17], new FacetContentRpc());
            this.put(contentCodes[4], new FacetContentRpc());
            this.put(contentCodes[20], new FacetContentRpc());
        }};
    }

    @Operation(summary = "Get LookUp Values")
    @GetMapping(value = "/lookup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ArrayList<SelectItemTO>> getFields(@RequestParam(value = "searchkey", required = false) String searchkey,
                                                       @RequestParam(value = "entity", required = false) String entity,
                                                       @RequestParam(value = "query", required = false) String query) {
        SelectItem[] lookUpItems = allInOneService.getLookUpItems(entity, searchkey, query);
        List<SelectItemTO> items = (lookUpItems == null)
                ? Collections.emptyList()
                : Arrays.stream(lookUpItems).map(SelectItemTO::new).toList();
        return ResultTO.success(new ArrayList<>(items));
    }

    @Operation(summary = "Get facet filter")
    @GetMapping(path = "/saved-facet-filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getUserFacetFilter(@RequestParam("type") ListPanelType type,
                                     @RequestParam(value = "typeId", required = false) Integer typeId) {
        SaveFilterSelectItems savedFacetFilterList = commonService.getSavedFacetFilterList(type, typeId);
        if (savedFacetFilterList.getItems() == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(savedFacetFilterList.getItems())
                .map(ff -> new IdNameTO(ff.getId(), ff.getName()))
                .toList();
    }

    @GetMapping(path = "/facet-filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public FacetFilterRpc getFacetFilter(@RequestParam("fromHrms") boolean fromHrms,
                                         @RequestParam(value = "relationId", required = false) Integer relationId,
                                         @RequestParam(value = "relationType", required = false) ListPanelType relationType,
                                         @RequestParam(value = "filterId") Integer filterId) {
        FacetFilterRpc data = new FacetFilterRpc();
        data.setObjectID(filterId);
        if (relationId != null) {
            data.setCustomDataPut(FacetFilterCutomField.RELATION_ID, String.valueOf(relationId));
        }
        if (relationType != null) {
            data.setType(relationType);
        }
        data.setShowFacetCodeName(new ArrayList<>(List.of(FacetContentType.EventFacetFilter.getContentCode())));
        HashMap<String, FacetSolrField> showSolrFieldMap = new HashMap<>();
        showSolrFieldMap.put(FacetContentType.EventFacetFilter.getContentCode()[0], new FacetSolrField(SolrEventRepresenter.FIELD_SHARED_USER_ID, SolrEventRepresenter.FIELD_SHARED_USER_ID_NAME));
        showSolrFieldMap.put(FacetContentType.EventFacetFilter.getContentCode()[1], new FacetSolrField(SolrEventRepresenter.FIELD_ACTIVITY_TYPE_ID, SolrEventRepresenter.FIELD_ACTIVITY_TYPE_ID_NAME));
        showSolrFieldMap.put(FacetContentType.EventFacetFilter.getContentCode()[2], new FacetSolrField(SolrEventRepresenter.FIELD_CALL_TYPE, SolrEventRepresenter.FIELD_CALL_TYPE));
        showSolrFieldMap.put(FacetContentType.EventFacetFilter.getContentCode()[15], new FacetSolrField(SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CANDIDATE, SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CANDIDATE));
        showSolrFieldMap.put(FacetContentType.EventFacetFilter.getContentCode()[16], new FacetSolrField(SolrEventRepresenter.FIELD_OWNER_ID, SolrEventRepresenter.FIELD_OWNER_ID_NAME));
        if (!fromHrms) {
            showSolrFieldMap.put(FacetContentType.EventFacetFilter.getContentCode()[3], new FacetSolrField(SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CONTACT, SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CONTACT));
            showSolrFieldMap.put(FacetContentType.EventFacetFilter.getContentCode()[4], new FacetSolrField(SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CRM_ACCOUNT, SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CRM_ACCOUNT));
            showSolrFieldMap.put(FacetContentType.EventFacetFilter.getContentCode()[6], new FacetSolrField(SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CASE, SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CASE));
            showSolrFieldMap.put(FacetContentType.EventFacetFilter.getContentCode()[7], new FacetSolrField(SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_OPPORTUNITY, SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_OPPORTUNITY));
        }
        data.setShowSolrFieldMap(showSolrFieldMap);
        data.setUserID(customFormManager.getUser().getObjectID());
        return rbacService.getEventFacetFilterData(data, null, fromHrms ? Appointment.FROM_HRMS : Appointment.FROM_CRM);
    }
}
