package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.status.ColorTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.service.ApiReferenceService;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.to.StatusTo;
import com.edatasite.workforce.rest.v3.release10.enums.ContextTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.REQUIRED;

/**
 * User : Akhror on 01/08/2021
 */
@Tag(name = "Reference", description = "Reference Public API")
@RestController
@RequestMapping(value = "/reference", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiReferenceControllerV3 {

    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ApiReferenceService apiReferenceService;

    @Operation(summary = "Get existing reference by parent code")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Reference"))
    @RequestMapping(path = "/{parentCode}", method = RequestMethod.GET)
    public List<ItemDto> getListByParentCode(@PathVariable String parentCode) throws RestException {
        if (StringUtils.isEmpty(parentCode)) {
            throw new RestException("Parent code is required", "Parent code is required", REQUIRED, HttpStatus.NOT_FOUND);
        }
        return apiReferenceService.getListByParentCode(parentCode);
    }

    @Operation(summary = "Get status list by context")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Status List"))
    @RequestMapping(path = "/by-context/{context}", method = RequestMethod.GET)
    public List<StatusTo> getStatusListByContext(@PathVariable String context) throws RestException {
        if (context == null || context.isEmpty()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Context type can not be null or empty", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        Set<EdsReference> statusList = referenceManager.getReferenceSetByParentCode(ContextTypeEnum.valueOf(context).getParentStatusCode());
        return statusList.stream().map(this::getStatusTo).collect(Collectors.toList());
    }

    private StatusTo getStatusTo(EdsReference reference) {
        StatusTo status = new StatusTo();
        status.setId(reference.getObjectID());
        status.setName(reference.getLocalizedName());
        status.setCode(reference.getCode());
        if (reference.getReferenceColor() != null) {
            ColorTO colorTO = new ColorTO();
            colorTO.setId(reference.getReferenceColor().getObjectID());
            colorTO.setName(reference.getReferenceColor().getName());
            colorTO.setHex(reference.getReferenceColor().getHex());
            status.setColorTO(colorTO);
        }
        return status;
    }
}
