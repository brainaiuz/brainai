package com.edatasite.workforce.rest.v3.release10.crm;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.CaseDto;
import com.edatasite.workforce.rest.v3.release10.crm.service.ApiCaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;

/**
 * User: Akhror
 * Date: 09.03.2021 15:45
 */
@Tag(name = "Case", description = "Case Public API")
@RestController
@RequestMapping(value = "/case", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiCaseControllerV3 {

    @Autowired
    private ApiCaseService apiCaseService;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private CRMService crmService;

    @Operation(summary = "Get Cases list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Cases"))
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ListResultTO<CaseDto>> getCases(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.CaseListPanel);

        return ResultTO.success(apiCaseService.getCasesList(fp));
    }

    @Operation(summary = "Get existing case by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "news"))
    @RequestMapping(path = "/{caseId}", method = RequestMethod.GET)
    public ResultTO<CaseDto> getCaseById(@PathVariable final Integer caseId) throws RestException {

        return ResultTO.success(apiCaseService.getCaseById(caseId));
    }

    @Operation(summary = "Create new case")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Case"))
    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<CaseDto> createCase(@RequestBody CaseDto caseDto) throws RestException {
        if (caseDto.getId() != null) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Case ID is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        apiCaseService.save(caseDto, true);
        return ResultTO.success(caseDto);
    }

    @Operation(summary = "Update existing case")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Case"))
    @RequestMapping(method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<CaseDto> updateProduct(@RequestBody CaseDto caseDto) throws RestException {
        if (caseDto.getId() == null || caseDto.getId() < 1) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Case ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        apiCaseService.save(caseDto, false);
        return ResultTO.success(caseDto);
    }

    @Operation(summary = "Patch Update existing case")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Case"))
    @RequestMapping(method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<CaseDto> patchUpdateCase(@RequestBody CaseDto caseDto) throws RestException {
        if (caseDto.getId() == null) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Case ID or Number is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        apiCaseService.savePatch(caseDto);
        return ResultTO.success(caseDto);
    }

    @Operation(summary = "Delete existing case by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Case"))
    @RequestMapping(path = "/{caseId}", method = RequestMethod.DELETE)
    public Object deleteNews(@PathVariable final Integer caseId) throws RestException {
        Optional.ofNullable(caseManager.get(caseId)).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Case with this id is not found", NOT_FOUND, HttpStatus.BAD_REQUEST));

        crmService.deleteCase(caseId);
        return ResultTO.success();
    }
}
