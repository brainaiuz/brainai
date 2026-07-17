package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.utils.MultiApprovalUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Multi Approval Api Resource", description = "Retrieves approval schemes based on given type")
@RestController
@RequestMapping(value = "/multiapproval", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN})
public class MultiApprovalApiResource implements ApiConstants {
    @Autowired
    private MultiApprovalUtils utils;

    @RequestMapping(value = "/schemes/{type}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<ApproverItem>> getSchemes(@PathVariable("type") String type) throws RestException {

        if (StringUtils.isBlank(type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "You must provide a type of scheme", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(utils.getApprovalSchemes2(type), HttpStatus.OK);
    }
}
