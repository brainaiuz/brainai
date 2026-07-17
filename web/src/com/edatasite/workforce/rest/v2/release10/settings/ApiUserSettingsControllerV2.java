package com.edatasite.workforce.rest.v2.release10.settings;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.ApiAccessToken;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.status.AddPushToken;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dilsh0d on 10/9/2017.
 */
@Tag(name = "User Settings", description = "User Settings API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiUserSettingsControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiUserSettingsControllerV2.class);

    @Autowired
    private HttpServletRequest servletRequest;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    @Transactional
    @Operation(summary = "Add Push Token", description = "Request to add Push token for current user.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code"),
            @ApiResponse(responseCode = "400", description = "name, email, phone number and type are required")})
    @RequestMapping(value = "/push_token", method = RequestMethod.POST, headers = {X_AUTH, ACCESS_TOKEN}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object addPushToken(@RequestHeader(ACCESS_TOKEN) String accessToken, @RequestBody AddPushToken pushToken) throws RestException {

        if (StringUtils.isBlank(pushToken.getPush_token())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "push_token required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ApiAccessToken apiAccessToken = globalAuthJdbcSpringManager.getApiAccessToken(accessToken);
        if (apiAccessToken == null || StringUtils.isBlank(apiAccessToken.getModuleCode())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invalid access token", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        EdsUser user = null;
        Object object = validateUser();
        if (object == null || StringUtils.isBlank(ServerSecurityContext.getInstance().getCompanyId())) {
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }
        if (object instanceof EdsUser) {
            user = (EdsUser) object;
        }
        if (StringUtils.isBlank(pushToken.getPush_token())) {
            throw new RestException("Push token is empty", "Push token is empty", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(pushToken.getDevice_os())) {
            throw new RestException("Device OS is empty", "Device OS is empty", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        String serverName = servletRequest.getServerName();
        String username = globalAuthJdbcSpringManager.getUsername(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()), user.getObjectID());
        Boolean result = globalAuthJdbcSpringManager.createPushNotificationToken(serverName, username, pushToken.getPush_token(), pushToken.getDevice_os(), apiAccessToken.getModuleCode());
        if (result) {
            return successResponse(new ResponseData());
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Couldn't add push_token.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional
    @Operation(summary = "Delete Push Token", description = "Request to delete Push token for current user.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code"),
            @ApiResponse(responseCode = "400", description = "name, email, phone number and type are required")})
    @RequestMapping(value = "/push_token", method = RequestMethod.DELETE, headers = {X_AUTH, ACCESS_TOKEN}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object deletePushToken(@RequestBody AddPushToken pushToken) throws RestException {
        if (StringUtils.isBlank(pushToken.getPush_token())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "push_token required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsUser user = null;
        Object object = validateUser();
        if (object == null || StringUtils.isBlank(ServerSecurityContext.getInstance().getCompanyId())) {
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }
        if (object instanceof EdsUser) {
            user = (EdsUser) object;
        }
        if (StringUtils.isBlank(pushToken.getPush_token())) {
            throw new RestException("Push token is empty", "Push token is empty", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(pushToken.getDevice_os())) {
            throw new RestException("Device OS is empty", "Device OS is empty", REQUIRED, HttpStatus.BAD_REQUEST);
        }
//        String serverName = servletRequest.getServerName();
//        String username = globalAuthJdbcSpringManager.getUsername(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()), user.getObjectID());
        Boolean result = globalAuthJdbcSpringManager.deletePushNotificationToken(pushToken.getPush_token(), pushToken.getDevice_os());
        if (result) {
            return successResponse(new ResponseData());
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Couldn't add push_token.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
