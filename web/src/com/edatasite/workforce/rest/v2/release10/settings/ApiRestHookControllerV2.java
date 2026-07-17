package com.edatasite.workforce.rest.v2.release10.settings;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsRestHook;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.ApiAccessToken;
import com.edatasite.workforce.gwt.core.server.db.settings.RestHookManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.RestHookTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anvar Akramov on 29/6/2019.
 */
@Tag(name = "Rest Hook", description = "Rest Hook API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiRestHookControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiRestHookControllerV2.class);

    @Autowired
    private HttpServletRequest servletRequest;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private RestHookManager restHookManager;

    @Transactional
    @Operation(summary = "List RestHooks", description = "Request list of RestHooks.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code")})
    @RequestMapping(value = "/subscription", method = RequestMethod.GET, headers = {X_AUTH, ACCESS_TOKEN})
    public Object listRestHook(@RequestHeader(ACCESS_TOKEN) String accessToken) throws RestException {

        ApiAccessToken apiAccessToken = globalAuthJdbcSpringManager.getApiAccessToken(accessToken);
        if (apiAccessToken == null || StringUtils.isBlank(apiAccessToken.getModuleCode())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invalid access token", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Object object = validateUser();
        if (object == null || StringUtils.isBlank(ServerSecurityContext.getInstance().getCompanyId())) {
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }
        EdsUser user = null;
        if (object instanceof EdsUser) {
            user = (EdsUser) object;
        }

        try {

            List<EdsRestHook> restHooks = restHookManager.getAllRestHooks();
            ArrayList<RestHookTO> result = new ArrayList<>();
            for (EdsRestHook i : restHooks) {
                RestHookTO t = new RestHookTO();
                t.setId(i.getObjectID());
                t.setEvent_name(i.getEventName());
                t.setTargetUrl(i.getTargetUrl());
                if (i.getUser() != null) {
                    t.setCreatedby(i.getUser().getObjectID());
                    t.setCreator(i.getUser().getFullName());
                }
                result.add(t);
            }
            return result;
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, "Couldn't add rest hook.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional
    @Operation(summary = "Add RestHook", description = "Request to add RestHook.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code"),
            @ApiResponse(responseCode = "400", description = "event_name and target_url are required")})
    @RequestMapping(value = "/subscription", method = RequestMethod.POST, headers = {X_AUTH, ACCESS_TOKEN}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object addRestHook(@RequestHeader(ACCESS_TOKEN) String accessToken, @RequestBody RestHookTO newRestHookTO, HttpServletRequest request) throws RestException {

        if (newRestHookTO == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request is empty", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(newRestHookTO.getEvent_name())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "event_name required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(newRestHookTO.getTargetUrl())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "target_url required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ApiAccessToken apiAccessToken = globalAuthJdbcSpringManager.getApiAccessToken(accessToken);
        if (apiAccessToken == null || StringUtils.isBlank(apiAccessToken.getModuleCode())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invalid access token", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }


        Object object = validateUser();
        if (object == null || StringUtils.isBlank(ServerSecurityContext.getInstance().getCompanyId())) {
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }
        EdsUser user = null;
        if (object instanceof EdsUser) {
            user = (EdsUser) object;
        }

        try {
            EdsRestHook newHook = new EdsRestHook();
            newHook.setDeleted(false);
            newHook.setEventName(newRestHookTO.getEvent_name());
            newHook.setTargetUrl(newRestHookTO.getTargetUrl());
            newHook.setUser(userManager.get(user.getObjectID()));
            restHookManager.create(newHook);
            newRestHookTO.setId(newHook.getObjectID());
            return successResponse(newRestHookTO);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, "Couldn't add rest hook.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Operation(summary = "Update RestHook", description = "Request to update RestHook.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code"),
            @ApiResponse(responseCode = "400", description = "event_name and target_url are required")})
    @RequestMapping(value = "/subscription/{id}", method = RequestMethod.PUT, headers = {X_AUTH, ACCESS_TOKEN}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object updateRestHook(@RequestHeader(ACCESS_TOKEN) String accessToken,
                                 @PathVariable(value = "id") Integer id,
                                 @RequestBody RestHookTO updateRestHookTO) throws RestException {

        if (id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Id field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (updateRestHookTO == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request is empty", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(updateRestHookTO.getEvent_name())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "event_name required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(updateRestHookTO.getTargetUrl())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "target_url required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ApiAccessToken apiAccessToken = globalAuthJdbcSpringManager.getApiAccessToken(accessToken);
        if (apiAccessToken == null || StringUtils.isBlank(apiAccessToken.getModuleCode())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invalid access token", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }


        Object object = validateUser();
        if (object == null || StringUtils.isBlank(ServerSecurityContext.getInstance().getCompanyId())) {
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }
        EdsUser user = null;
        if (object instanceof EdsUser) {
            user = (EdsUser) object;
        }

        try {
            EdsRestHook newHook = restHookManager.get(id);
            newHook.setEventName(updateRestHookTO.getEvent_name());
            newHook.setTargetUrl(updateRestHookTO.getTargetUrl());
            newHook.setUser(user);
            restHookManager.update(newHook);
            return successResponse(new ResponseData());
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, "Couldn't update rest hook.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Operation(summary = "Delete RestHook", description = "Request to delete RestHook.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code"),
            @ApiResponse(responseCode = "400", description = "id is required")})
    @RequestMapping(value = "/subscription", method = RequestMethod.DELETE, headers = {X_AUTH, ACCESS_TOKEN}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object deleteRestHook(@RequestParam("id") Integer id, @RequestBody Object t, HttpServletRequest request) throws RestException {

        if (id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Id field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsUser user = null;
        Object object = validateUser();
        if (object == null || StringUtils.isBlank(ServerSecurityContext.getInstance().getCompanyId())) {
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }
        if (object instanceof EdsUser) {
            user = (EdsUser) object;
        }
        EdsRestHook restHookToDelete = restHookManager.get(id);
        if (restHookToDelete == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "RestHook not found", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        try {
            restHookToDelete.setDeleted(true);
            restHookManager.update(restHookToDelete);
            return successResponse(new ResponseData());
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, "Couldn't delete RestHook.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
