package com.edatasite.workforce.rest.v4.settings.controller;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.profile.client.rpc.request.CreateReferenceReq;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.rest.v4.settings.service.ReferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reference", description = "Reference Public API")
@RestController
@RequestMapping(
        value = "/settings/reference",
        headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
)
public class ReferenceController {

    private final ReferenceService service;

    public ReferenceController(ReferenceService service) {
        this.service = service;
    }

    @Operation(summary = "Create Reference")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Reference"))
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<ReferenceItem> createReference(@Validated @RequestBody CreateReferenceReq request) {
        ReferenceItem reference = service.createReference(request);
        return ResultTO.success(reference);
    }

    @Operation(summary = "Get Reference By ID")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Get Reference By ID"))
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResultTO<ReferenceItem> getReferenceById(@PathVariable Integer id) {
        ReferenceItem reference = service.getById(id);
        return ResultTO.success(reference);
    }

    @Operation(summary = "Get Reference By CODE")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Get Reference By CODE"))
    @RequestMapping(method = RequestMethod.GET)
    public ResultTO<ReferenceItem> getOrCreateOrgBoardReference(@RequestParam(value = "code") String code) {
        ReferenceItem reference = service.getOrCreateOrgBoardReference(code);
        return ResultTO.success(reference);
    }

    @Operation(summary = "Update Reference")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Update Reference by code"))
    @RequestMapping(path = "/code", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<ReferenceItem> updateReferenceByCode(@Validated @RequestBody CreateReferenceReq request) {
        if (request == null || request.getCode() == null) return null;
        ReferenceItem referenceItem = service.updateReferenceByCode(request);
        return ResultTO.success(referenceItem);
    }
}
