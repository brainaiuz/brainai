package com.edatasite.workforce.gwt.core.server.filters;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.ApiAccessToken;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ResponseDataTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApiResult;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 01.07.2009
 * Time: 17:18:48
 * To change this template use File | Settings | File Templates.
 */
public class RemotingFilter implements Filter, ApiConstants {
    private static final Logger log = LoggerFactory.getLogger(RemotingFilter.class);
    private final Gson gson = new Gson();

    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    //    private final Pattern allowedURIs = Pattern.compile("^(/services/api/v2/api-docs|/services/api/v3/v2/api-docs|/services/api/v3/api-docs|/services/api/swagger-ui.html|/services/api/v1/swagger-ui.html|/services/api/v2/swagger-ui.html|/services/api/v3/swagger-ui.html|/services/api/webjars/.*|/services/api/v1/webjars/.*|/services/api/v2/webjars/.*|/services/api/v3/webjars/.*|/services/api/swagger-resources.*|/services/api/v1/swagger-resources.*|/services/api/v2/swagger-resources.*|/services/api/v3/swagger-resources.*|/services/api/v1/v2.*|/services/api/v2/v2.*|/services/api/v2/v2/v3.*|/services/api/v2/.*/redirect|/services/api/v3/.*/redirect|/services/api/v2/stripe_webhook|/services/api/v2/twilio/.*|/services/api/v2/internal/.*|/services/api/v2/oauth2/.*|/services/api/v2/telegram/.*|/services/api/v3/helper/.*)$");
    private final Pattern allowedURIs = Pattern.compile("^(/services/api/v1/api-docs|" +
            "/services/api/v1/api-docs/.*|/services/api/v1/swagger-ui.html|/services/api/v1/swagger-ui/.*|" +
            "/services/api/v2/api-docs|/services/api/v2/api-docs/.*|/services/api/v2/swagger-ui.html|" +
            "/services/api/v2/swagger-ui/.*|/services/api/v3/api-docs|/services/api/v3/api-docs/.*|" +
            "/services/api/v3/swagger-ui.html|/services/api/v3/swagger-ui/.*|/services/api/v2/.*/redirect|" +
            "/services/api/v3/.*/redirect|/services/api/v2/stripe_webhook|/services/api/v2/twilio/.*|" +
            "/services/api/v2/internal/.*|/services/api/v2/oauth2/.*|/services/api/v2/telegram/.*|" +
            "/services/api/v3/helper/.*|/services/api/v3/sipuni/.*|/services/api/v3/mycalls.*|" +
            "/services/api/v2/whatsapp/.*|/services/api/v3/mobile/versions/:name/:version|" +
            "/services/api/v3/gym/scan/.*|/services/api/v3/gym/auth/.*|/services/api/v3/auth/company-register|" +
            "/services/api/v3/auth/activate|/services/api/v3/currency|/services/api/v3/sales-invoice/fai-event|/services/api/v3/facebook/.*|" +
            "/services/api/v3/payme/merchant|/services/api/v4/.*)$");

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if ("options".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        if (request.getHeader("X-Source") != null) {
            ServerSecurityContext.getInstance().setSource(request.getHeader("X-Source"));
        } else if (request.getHeader("X-Gwt-Module-Base") != null) {
            ServerSecurityContext.getInstance().setSource("WEB");
        }
        boolean isApiV1 = request.getRequestURL().toString().contains("/v1/");
        //Allowed URIs must not go through auth process
        if (!this.allowedURIs.matcher(request.getRequestURI()).matches()) {
            String queryString = request.getQueryString();
            String sessionID = request.getHeader(ApiConstants.SESSION_ID) != null ? request.getHeader(ApiConstants.SESSION_ID) : request.getHeader(ApiConstants.X_AUTH);
            String version = request.getHeader(ApiConstants.VERSION);
            String accessToken = request.getHeader(ApiConstants.ACCESS_TOKEN);

            if (StringUtil.isEmpty(accessToken)) {
                response.setStatus(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION);
                PrintWriter writer = response.getWriter();
                if (isApiV1) {
                    writer.write(gson.toJson(new ResponseDataTO("", "info", "Access token required", 0), ResponseDataTO.class));
                } else {
                    writer.write(gson.toJson(new ApiResult("Access token required", "Access token required", REQUIRED), ApiResult.class));
                }
                writer.close();
                log.error("Access token required");
                return;
            }


            ApiAccessToken apiAccessToken = getGlobalAuthJdbcSpringManager().getApiAccessToken(accessToken);
            if (apiAccessToken == null) {
                response.setStatus(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION);
                PrintWriter writer = response.getWriter();
                if (isApiV1) {
                    writer.write(gson.toJson(new ResponseDataTO("", "info", "Invalid access token", 0), ResponseDataTO.class));
                } else {
                    writer.write(gson.toJson(new ApiResult("Invalid access token", "Invalid access token", INVALID), ApiResult.class));
                }
                writer.close();
                log.error("Invalid access token {}", accessToken);
                return;
            }

            log.info(apiAccessToken.getDescription() + " : " + apiAccessToken.getToken());

            if (!StringUtil.isEmpty(queryString) && StringUtil.isEmpty(sessionID)) {
                if (queryString.contains(ApiConstants.VERSION)) {
                    version = request.getParameter(ApiConstants.VERSION);
                }

                if (queryString.contains(ApiConstants.SESSION_ID)) {
                    sessionID = request.getParameter(ApiConstants.SESSION_ID);
                }
            }

            ServerSecurityContext.getInstance().setVersion(version);

            if (!isAllowedUrl(request.getRequestURL().toString())) {
                if (ServerUtils.isNullOrEmpty(sessionID)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    PrintWriter writer = response.getWriter();
                    if (isApiV1) {
                        writer.write(gson.toJson(new ResponseDataTO("", "info", "Session required", 0), ResponseDataTO.class));
                    } else {
                        writer.write(gson.toJson(new ApiResult("Session required", "Session required", NOT_FOUND), ApiResult.class));
                    }
                    writer.close();
                    log.info("Session required");
                    return;
                }
                if (!sessionID.matches(Constants.SESSION_REGEX)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    PrintWriter writer = response.getWriter();
                    if (isApiV1) {
                        writer.write(gson.toJson(new ResponseDataTO("", "info", "Invalid session", 0), ResponseDataTO.class));
                    } else {
                        writer.write(gson.toJson(new ApiResult("Invalid session", "Invalid session", INVALID), ApiResult.class));
                    }
                    writer.close();
                    log.info("Invalid session");
                    return;
                }

                try {
                    ServerSecurityContext.getInstance().setSessionId(sessionID);
                    if (ServerSecurityContext.getInstance().getUser() == null) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        PrintWriter writer = response.getWriter();
                        if (isApiV1) {
                            writer.write(gson.toJson(new ResponseDataTO("", "info", "Session expired", 0), ResponseDataTO.class));
                        } else {
                            writer.write(gson.toJson(new ApiResult("Session expired", "Session expired", EXPIRED), ApiResult.class));
                        }
                        writer.close();
                        log.info("Session expired");
                        return;
                    }
                    if (ServerSecurityContext.getInstance().getCompanyId() != null) {
                        log.info("doFilter for company: {}", ServerSecurityContext.getInstance().getCompanyId());
                    }
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    PrintWriter writer = response.getWriter();
                    if (isApiV1) {
                        writer.write(gson.toJson(new ResponseDataTO("", "info", "Session expired", 0), ResponseDataTO.class));
                    } else {
                        writer.write(gson.toJson(new ApiResult("Session expired", "Session expired", EXPIRED), ApiResult.class));
                    }
                    writer.close();
                    log.info("Session expired");
                    log.error(e.getMessage(), e);
                    return;
                }

                if (!isApiV1) {
                    //If access token is related to CRM, check crm main permissions for APIV2
                    if (PermissionConstants.CRM_MODULE.equals(apiAccessToken.getModuleCode())
                            && (!ServerUtils.hasPermission(PermissionConstants.CRM_MAIN_MENU) || !ServerUtils.hasPermission(PermissionConstants.CRM_SALES_TAB))) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        PrintWriter writer = response.getWriter();
                        writer.write(gson.toJson(new ApiResult(PERMISSION_MESSAGE, PERMISSION_MESSAGE, ACCESS_DENIED), ApiResult.class));
                        writer.close();
                        log.error("{}:{}", accessToken, PERMISSION_MESSAGE);
                        return;
                    }

                    if (PermissionConstants.HRMS_MODULE.equals(apiAccessToken.getModuleCode())
                            && (!ServerUtils.hasPermission(PermissionConstants.HRMS_MAIN_MENU) || !ServerUtils.hasPermission(PermissionConstants.HRMS_SECTION))) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        PrintWriter writer = response.getWriter();
                        writer.write(gson.toJson(new ApiResult(PERMISSION_MESSAGE, PERMISSION_MESSAGE, ACCESS_DENIED), ApiResult.class));
                        writer.close();
                        log.error("{}:{}", accessToken, PERMISSION_MESSAGE);
                        return;
                    }
                }
            }
        }

        boolean isGymOrPraaktis = Utils.isGym((HttpServletRequest) servletRequest) || Utils.isPraaktis((HttpServletRequest) servletRequest);
        try {
            validateUser(isGymOrPraaktis);
        } catch (RestException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter writer = response.getWriter();
            writer.write(gson.toJson(new ApiResult(e.getMessage(), e.getDeveloper_msg(), e.getError_code()), ApiResult.class));
            writer.close();
            return;
        }

        filterChain.doFilter(request, servletResponse);

    }

    protected void validateUser(boolean isGymOrPraaktis) throws RestException {
        ServerSecurityContext securityContext = ServerSecurityContext.getInstance();
        if (securityContext == null) {
            return;
        }
        if (StringUtils.isEmpty(securityContext.getSessionId())) {
            return;
        }

        EdsUser user;
        try {
            user = Optional.ofNullable(securityContext.getUser())
                    .map(u -> (EdsUser) u)
                    .orElseThrow(() -> new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED));
        } catch (RestException e) {
            log.error("Error retrieving user from security context", e);
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }

        validateUserAccess(user, isGymOrPraaktis);
    }

    private void validateUserAccess(EdsUser user, boolean isGymOrPraaktis) throws RestException {
        if (user instanceof EdsClientContact) {
            Optional.ofNullable(user.getClientContact())
                    .map(EdsClientContact::getAccess)
                    .filter(Boolean::booleanValue)
                    .orElseThrow(() -> new RestException("Access denied", "Access denied", ACCESS_DENIED, HttpStatus.FORBIDDEN));
        }

        if (user.getDeleted() || Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) {
            throw new RestException("Your account was disabled. Please contact your company admin.", "User is deleted/resigned.", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        String status = user.getAccountStatus().getCode();
        if (!Constants.EMPLOYEE_STATUS_ACTIVE.equals(status) && !isGymOrPraaktis) {
            throw new RestException("Please verify your registration from a confirmation email sent to you to proceed.", "User is not active.", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
    }

    private boolean isAllowedUrl(String requestURL) {
        return requestURL.contains("/v1/auth/login")
                || requestURL.contains("/v1/auth/signUp")
                || requestURL.contains("/v1/auth/signUpTypes")
                || requestURL.contains("/v1/auth/countries")
                || requestURL.contains("/v1/auth/forgotPassword")
                || requestURL.contains("/v1/deviceStatusHistory")
                || requestURL.contains("/v2/country_codes")
                || requestURL.contains("/v2/email_login")
                || requestURL.contains("/v2/pinfl_login")
                || requestURL.contains("/v2/forgot_password")
                || requestURL.contains("/v2/registration")
                || requestURL.contains("/v2/social_check")
                || requestURL.contains("/v3/auth/login");
    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void destroy() {

    }

    private GlobalAuthJdbcSpringManager getGlobalAuthJdbcSpringManager() {
        return globalAuthJdbcSpringManager = globalAuthJdbcSpringManager == null
                ? ApplicationContextProvider.applicationContext.getBean(GlobalAuthJdbcSpringManager.class)
                : globalAuthJdbcSpringManager;
    }
}
