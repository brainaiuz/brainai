package com.edatasite.workforce.rest.v3.release10.hrms;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.CertificateDto;
import com.edatasite.workforce.rest.v3.release10.hrms.service.ApiCertificateService;
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

/**
 * User : Jamshid on 29/10/2021
 */

@Tag(name = "Certificate", description = "Certificate Public API")
@RestController
@RequestMapping(value = "/certificate", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiCertificateControllerV3 implements ApiConstants {
    @Autowired
    private ApiCertificateService apiCertificateService;

    @Operation(summary = "Get Certificate List")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Certificate"))
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ListResultTO<CertificateDto>> getCertificate(@RequestBody ListParamsDTO params) {
        ListingFilterParameter filterParameter = ListingFilterHelperV3.createListingFilter(params, ListPanelType.CertificateListPanel);

        return ResultTO.success(apiCertificateService.getCertificateList(filterParameter));
    }

    @Operation(summary = "Get existing certificate by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "certificate"))
    @RequestMapping(value = "/{certificateId}", method = RequestMethod.GET)
    public ResultTO<CertificateDto> getCertificateById(@PathVariable final Integer certificateId) throws RestException {
        return ResultTO.success(apiCertificateService.getById(certificateId));
    }

    @Operation(summary = "Create new certificate")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Certificate"))
    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<CertificateDto> createCertificate(@RequestBody CertificateDto certificateDto) throws RestException {
        if (certificateDto.getId() != null) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Certificate ID is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        apiCertificateService.save(certificateDto, true);
        return ResultTO.success(certificateDto);
    }

    @Operation(summary = "Edit Certificate")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Certificate"))
    @RequestMapping(method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<CertificateDto> updateCertificate(@RequestBody CertificateDto certificateDto) throws RestException {
        if (certificateDto.getId() == null || certificateDto.getId() < 1) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Certificate ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        apiCertificateService.save(certificateDto, false);
        return ResultTO.success(certificateDto);
    }

    @Operation(summary = "Patch Update existing certificate")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Certificate"))
    @RequestMapping(method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<CertificateDto> patchUpdateCertificate(@RequestBody CertificateDto certificateDto) throws RestException {
        if (certificateDto.getId() == null) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Certificate ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        apiCertificateService.savePatch(certificateDto);
        return ResultTO.success(certificateDto);
    }
}
