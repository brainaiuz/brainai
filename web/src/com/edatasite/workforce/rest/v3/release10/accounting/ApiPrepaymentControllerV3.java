package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.PrepaymentDto;
import com.edatasite.workforce.rest.v3.release10.accounting.service.ApiPrepaymentService;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Prepayments", description = "Collection of public APIs for Prepayments")
@RestController
@RequestMapping(value = "/prepayment", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.ALL_VALUE})
public class ApiPrepaymentControllerV3 implements ApiConstants {
    private static final Logger log = LoggerFactory.getLogger(ApiPrepaymentControllerV3.class);

    private final ApiPrepaymentService apiPrepaymentService;

    public ApiPrepaymentControllerV3(ApiPrepaymentService apiPrepaymentService) {
        this.apiPrepaymentService = apiPrepaymentService;
    }

    // get by id
    @Operation(summary = "Get existing prepayment by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Prepayments"))
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResultTO<PrepaymentDto> get(@PathVariable final Integer id) throws RestException {
        log.info("REST request to get prepayment by id: {}", id);
        return ResultTO.success(apiPrepaymentService.getById(id));
    }

    // create
    @Operation(summary = "Create new prepayment")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Prepayments"))
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<Integer> create(@RequestBody PrepaymentDto req) throws RestException {
        log.info("REST request to create rental order");
        return ResultTO.success(apiPrepaymentService.save(req));
    }
}
