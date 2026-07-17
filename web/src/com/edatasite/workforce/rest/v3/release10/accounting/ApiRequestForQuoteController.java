package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.RequestForQuoteDto;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Request For Quotes", description = "Collection of public APIs for Request For Quotes")
@RestController
@RequestMapping(value = "/rfq", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiRequestForQuoteController {

    @Operation(summary = "Get RFQ list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "RFQ"))
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ListResultTO<RequestForQuoteDto>> getRequestForQuotes(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.RequestForQuoteListPanel);

        return ResultTO.success(new ListResultTO<>());
    }
}
