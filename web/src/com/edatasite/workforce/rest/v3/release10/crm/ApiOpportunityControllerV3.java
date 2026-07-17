package com.edatasite.workforce.rest.v3.release10.crm;

import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.OpportunityTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.crm.dto.OpportunityDto;
import com.edatasite.workforce.rest.v3.release10.crm.dto.contact.OpportunityByStageTO;
import com.edatasite.workforce.rest.v3.release10.crm.service.ApiOpportunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Normurod Buriev.
 * Date: 3/23/2021 6:13 PM
 */
@Tag(name = "Opportunity Api Resource", description = "Here is a opportunity api resouce that contains CRUD operations and so on...")
@RestController
@RequestMapping(value = "/opportunity", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiOpportunityControllerV3 implements ApiConstants {
    private static final Logger log = LoggerFactory.getLogger(ApiOpportunityControllerV3.class);

    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private ApiOpportunityService opportunityService;

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<ListResultTO<OpportunityDto>> list(@RequestBody ListParamsDTO params) {
        ListingFilterParameter filterParameter = ListingFilterHelperV3.createListingFilter(params, ListPanelType.OpportunitiesListPanel);
        return ResponseEntity.ok(opportunityService.getList(filterParameter));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResultTO<OpportunityDto> get(@PathVariable("id") Integer id) throws RestException {
        EdsOpportunity opportunity = opportunityManager.get(id);
        if (opportunity == null) {
            throw new RestException(ERROR, "Opportunity is not found by given Id.", SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        return ResultTO.success(opportunityService.get(id));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<OpportunityDto> create(@RequestBody @Valid OpportunityDto dto) throws RestException {
        if (dto.getId() != null && dto.getId() > 0) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Id is specified, in a creation process you should not specify Id!", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        Integer objectId = opportunityService.save(dto);
        return ResultTO.success(opportunityService.get(objectId));
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<OpportunityDto> update(@RequestBody @Valid OpportunityDto dto) throws RestException {
        validateForExistince(dto);
        Integer objectId = opportunityService.save(dto);
        return ResultTO.success(opportunityService.get(objectId));
    }

    @RequestMapping(method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<OpportunityDto> applyChanges(@RequestBody OpportunityDto dto) throws RestException {
        log.info("=====OPPORTUNITY APPLY CHANGE PROCESS IS STARTED=====");
        log.info("REQUEST BODY: {}", dto);
        validateForExistince(dto);
        Integer objectId = opportunityService.applyChanges(dto);
        return ResultTO.success(opportunityService.get(objectId));
    }

    @RequestMapping(value = "/apply-changes", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<OpportunityDto> applyChangesByPOST(@RequestBody OpportunityDto dto) throws RestException {
        return applyChanges(dto);
    }

    private void validateForExistince(OpportunityDto dto) throws RestException {
        if (dto.getId() == null && dto.getNumber() == null) {
            throw new RestException(ERROR, "You must proviced Id or Number of Opportunity to apply changes to it.", INVALID, HttpStatus.BAD_REQUEST);
        }
        EdsOpportunity opportunity = null;
        if (dto.getId() != null) {
            opportunity = opportunityManager.get(dto.getId());
        }
        if (opportunity == null && StringUtils.isNotBlank(dto.getNumber())) {
            opportunity = opportunityManager.getByNumber(dto.getNumber());
        }
        if (opportunity == null) {
            throw new RestException(ERROR, "Opportunity is not found by given Id/Number.", INVALID, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/opportunity/list/by-stage", method = RequestMethod.POST,headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<List<OpportunityByStageTO>> opportunityByStage(@RequestBody ListParamsDTO listParams) {
        log.info("REST request to get opportunity list grouped by stage");
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(listParams, ListPanelType.OpportunitiesListPanel);
        fp.setHasOnlyClientAccess(false);

        List<OpportunityByStageTO> response = opportunityService.getOpportunityByStage(fp);
        return ResultTO.success(response);
    }
    @Operation(summary = "Get Kanban items by status", description = "Get Kanban items by status (opportunity, lead, task)")
    @PostMapping(path = "kanban/items/by-status",
            headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
            produces = "application/json")
    public ResponseEntity<?> getKanbanItemsByStatus(@RequestBody ListParamsDTO listParams,
                                                    @RequestParam Integer statusId,
                                                    @RequestParam String type) {

        if (statusId == null || listParams == null || type == null) {
            return ResponseEntity.badRequest()
                    .body("All parameters are required: statusId, listParams, type (OPPORTUNITY | LEAD | TASK)");
        }

        // Build filter params
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(
                listParams, ListPanelType.OpportunitiesKanbanPanel);
        fp.setHasOnlyClientAccess(false);

        List<OpportunityTO> result;
        switch (type.toUpperCase()) {
            case "OPPORTUNITY":
                result = opportunityService.getOppotunityKanbanItemsByStatus(fp, statusId);
                break;
            case "LEAD":
                result = opportunityService.getLeadKanbanItemsByStatus(fp, statusId);
                break;
            case "TASK":
                result = opportunityService.getTaskKanbanItemsByStatus(fp, statusId);
                break;
            default:
                return ResponseEntity.badRequest()
                        .body("Invalid type. Allowed values: OPPORTUNITY, LEAD, TASK");
        }

        return ResponseEntity.ok(result);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> deleteOpportunity(@PathVariable("id") Integer id) {
        log.info("REST request to delete opportunity by id: {}", id);
        opportunityService.delete(id);
        return ResultTO.success();
    }

    @GetMapping(path = "/number", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<String> generateOpportunityNumber() {
        log.info("REST request to generate opportunity number");
        NumberData numberData = opportunityService.generateNumber();
        return ResultTO.success(numberData.getNumberString());
    }
}
