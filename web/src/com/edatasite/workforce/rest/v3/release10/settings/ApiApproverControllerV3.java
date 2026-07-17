package com.edatasite.workforce.rest.v3.release10.settings;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.LEAVE_REQUEST;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.ACCESS_TOKEN;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.REQUIRED;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.X_AUTH;

@Tag(name = "Approver", description = "Approver API")
@RestController
@RequestMapping(value = "/approver", headers = {ACCESS_TOKEN, X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiApproverControllerV3 {
    @Autowired
    private AllInOneService allInOneService;

    @Operation(summary = "Get Approvers", description = "Get Approvers by Entity type")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Approvers"))
    @RequestMapping(path = "/permissions/{type}", method = RequestMethod.GET, headers = {X_AUTH, ACCESS_TOKEN}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<?> getApproversByType(@PathVariable String type) throws RestException {
        if (type == null || type.isEmpty()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Entity Type can not be null or empty", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        boolean isLeaveRequestForm = LEAVE_REQUEST.equalsIgnoreCase(type);
        ArrayList<ApproverItem> approvers = allInOneService.getApprovers(type, null, isLeaveRequestForm, null, false).getList();
        if (approvers == null || approvers.size() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Approvers not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        return approvers.get(0).getEmployees();
    }
}
