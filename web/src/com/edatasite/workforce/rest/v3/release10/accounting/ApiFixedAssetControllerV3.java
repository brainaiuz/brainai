package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.FixedAssetDTO;
import com.edatasite.workforce.rest.v3.release10.accounting.service.ApiFixedAssetService;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.INVALID;

@Tag(name = "FixedAsset", description = "Fixed Asset Public API")
@RestController
@RequestMapping(value = "/fixed-asset", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiFixedAssetControllerV3 {

    Logger log = Logger.getLogger(ApiFixedAssetControllerV3.class);

    @Autowired
    private ApiFixedAssetService apiFixedAssetService;

    @Operation(summary = "Get Fixed Asset List", description = "Get Fixed Asset List", tags = {"FixedAsset"})
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Success"))
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ListResultTO<FixedAssetDTO>> getFixedAssetsList(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.FixedAssetListPanel);
        ListResultTO<FixedAssetDTO> fixedAssets = apiFixedAssetService.getFixedAssetsList(fp);

        return ResultTO.success(fixedAssets);
    }
    @Operation(summary = "Get existing Fixed Asset by id", description = "Get Fixed Asset By Id", tags = {"FixedAsset"})
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Success"))
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResultTO<FixedAssetDTO> getExistingFixedAssetById(@PathVariable("id") Integer fixedAssetId) throws RestException {
        return ResultTO.success(apiFixedAssetService.getFixedAssetById(fixedAssetId));
    }

    @Operation(summary = "Create Fixed Asset", description = "Create new Fixed Asset", tags = {"FixedAsset"})
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Success"))
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<FixedAssetDTO> createFixedAsset(@RequestBody FixedAssetDTO fixedAssetDTO) throws RestException {
        if (fixedAssetDTO.getId() != null) {
            return ResultTO.failure("Object ID should be null", HttpStatus.BAD_REQUEST.value());
        }
        return apiFixedAssetService.save(fixedAssetDTO, true);
    }

    @Operation(summary = "Update Fixed Asset", description = "Update existing Fixed Asset", tags = {"FixedAsset"})
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Success"))
    @RequestMapping(method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<FixedAssetDTO> updateFixedAsset(@RequestBody FixedAssetDTO fixedAssetDto) throws RestException {
        if(fixedAssetDto == null || fixedAssetDto.getId() < 1) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Fixed Asset DTO is not specified", INVALID, HttpStatus.BAD_REQUEST);
        }

        return apiFixedAssetService.save(fixedAssetDto, false);
    }

    @Operation(summary = "Delete Fixed Asset", description = "Delete Fixed Asset", tags = {"FixedAsset"})
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Success"))
    @RequestMapping(path = "/{fixedAssetId}", method = RequestMethod.DELETE)
    public Object deleteFixedAsset(@PathVariable Integer fixedAssetId) {
        apiFixedAssetService.deleteFixedAsset(fixedAssetId);

        return ResultTO.success();
    }

}
