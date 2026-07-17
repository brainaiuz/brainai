package com.edatasite.workforce.rest.v2.release10.core;

import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.v2.release10.crm.ApiActivityControllerV2;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.finnetlimited.reportservice.core.server.CoreServiceLocal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Reports", description = "Report Data API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiReportDataControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiActivityControllerV2.class);

    @Autowired
    protected CoreServiceLocal reportingCoreService;


    @Operation(summary = "Repor Deta ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have report data "),
            @ApiResponse(responseCode = "400", description = "id is required")})
    @RequestMapping(value = "/report/{id}/details", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getReportDetailsInfo(@RequestBody MListingFilterParameter filterParameter) throws RestException {
        if (filterParameter == null || filterParameter.getObjectId() == null || filterParameter.getObjectId() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        try {
            return successResponse(reportingCoreService.getReportDateForApi(filterParameter));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
