package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.PriceLevelListDto;
import com.edatasite.workforce.rest.v3.release10.accounting.service.ApiPriceLevelService;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Price Level", description = "Collection of public APIs for Price Level")
@RestController
@RequestMapping(value = "/price-level", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiPriceLevelControllerV3 implements ApiConstants {
    @Autowired
    private ApiPriceLevelService apiPriceLevelService;

    @Operation(summary = "Get price levels list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Price Level"))
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ListResultTO<PriceLevelListDto>> getPriceLevels(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.PriceLevelListPanel);

        return ResultTO.success(apiPriceLevelService.getPriceLevelList(fp));
    }
}
