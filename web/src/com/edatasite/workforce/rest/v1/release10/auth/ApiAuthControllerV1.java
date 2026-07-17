package com.edatasite.workforce.rest.v1.release10.auth;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.rpc.UserSignUPSessionID;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.signup.client.rpc.CreatedCompany;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.gwt.signup.server.app.SignUpServiceLocal;
import com.edatasite.workforce.rest.base.enums.SignUpTypeEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.AuthTO;
import com.edatasite.workforce.rest.base.to.CompanyTO;
import com.edatasite.workforce.rest.base.to.LoginTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.base.to.UserTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Umidbek.
 */
@Tag(name = "Auth", description = "Auth API")
@RestController
@RequestMapping(value = "/auth",
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiAuthControllerV1 extends BaseApiControllerV1 implements ApiConstants {

    private static final Logger log = LoggerFactory.getLogger(ApiAuthControllerV1.class);
    @Autowired
    protected UserManager userManager;
    @Autowired
    protected SessionService sessionService;
    @Autowired
    protected SignUpServiceLocal signUpServiceLocal;
    @Autowired
    protected CompanyManager companyManager;
    @Autowired
    protected GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private LoginServiceLocal loginServiceLocal;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private HttpServletRequest servletRequest;
    @Autowired
    private HttpServletResponse servletResponse;

    @RequestMapping(value = "/login", method = RequestMethod.POST, headers = ACCESS_TOKEN, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object login(@RequestBody LoginTO data) {
        if (data == null) {
            return this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        if (StringUtil.isEmpty(data.getUsername()) || StringUtil.isEmpty(data.getPassword())) {
            return this.errorResponse("Incorrect username or password", HttpServletResponse.SC_NOT_FOUND);
        }

        List<UserCompanyDTO> companies = this.getCompanies(data.getUsername(), data.getPassword());

        if (companies == null || companies.isEmpty()) {
            return this.errorResponse("Incorrect username or password", HttpServletResponse.SC_NOT_FOUND);
        }

        AuthTO authTO;
        for (UserCompanyDTO companyDTO : companies) {
            try {
                authTO = this.obtainSession(new AuthTO(), companyDTO);
                if (validateFreeTrial()) {
                    return successResponse(authTO);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.freeTrailExpiredException();
    }


    @RequestMapping(value = "/companies", method = RequestMethod.GET, headers = {SESSION_ID, ACCESS_TOKEN})
    public Object getCompanies() {
        EdsUser user = null;
        if (this.validateUser() instanceof EdsUser) {
            user = (EdsUser) this.validateUser();
        }
        if (user == null) {
            return errorResponse(REQUEST_USER_NOT_AUTHORIZED, HttpServletResponse.SC_UNAUTHORIZED);
        }

        ArrayList<CompanyTO> result = new ArrayList<>();

        List<UserCompanyDTO> companies = this.getCompanies(user.getUserName(), null);
        for (UserCompanyDTO company : companies) {
            try {
                this.obtainSession(new AuthTO(), company);
                try {
                    if (validateFreeTrial()) {
                        CompanyTO companyTO = new CompanyTO();
                        companyTO.setId(company.getCompanyID());
                        companyTO.setName(company.getCompanyName());
                        companyTO.setLogo(company.getLogo());
                        companyTO.setShortDateFormat(company.getShortDateFormat());
                        companyTO.setLongDateFormat(company.getLongDateFormat());
                        result.add(companyTO);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return successResponse(result);
    }

    @RequestMapping(value = "/signUpTypes", method = RequestMethod.GET, headers = ACCESS_TOKEN)
    public Object getSignUpTypes() {
        ArrayList<SignUpTypeEnum> result = new ArrayList<>();
        result.add(SignUpTypeEnum.WEB_API);
        result.add(SignUpTypeEnum.ANDROID);
        result.add(SignUpTypeEnum.IPHONE);
        return successResponse(result);
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST, headers = ACCESS_TOKEN, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object signUp(@RequestBody CompanyTO companyData) {
        if (companyData == null) {
            return errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        if (companyData.getName() == null || companyData.getEmail() == null || companyData.getPhone() == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        NewCompany company = new NewCompany();
        company.setName(companyData.getName());
        company.setAdminEmail(companyData.getEmail());
        company.setAdminFName(companyData.getFirstName());
        company.setAdminLName(companyData.getLastName());
        company.setAdminPassword(companyData.getPassword());
        if (companyData.getCountry() != null) {
            company.setCountryName(companyData.getCountry().getName());
            company.setCountryCode(companyData.getCountry().getCode());
            company.setCallCode(companyData.getCountry().getPhoneCode());
        }
        company.setPhone(companyData.getPhone());
        String host = (companyData.getHost() != null && !"".equals(companyData.getHost())) ? companyData.getHost() : servletRequest.getServerName();
        company.setHost(host);
        company.setLocale("en");
        company.setTheme("workforce");

        company.setFromFederatedLogin(true);
        if (companyData.getSignUpType() != null) {
            if (SignUpTypeEnum.ANDROID.getCode().equals(companyData.getSignUpType().getCode())) {
                company.setCompanySignedUpFrom(Constants.SIGNED_UP_FROM_ANDROID);
            } else if (SignUpTypeEnum.IPHONE.getCode().equals(companyData.getSignUpType().getCode())) {
                company.setCompanySignedUpFrom(Constants.SIGNED_UP_FROM_IPHONE);
            } else if (SignUpTypeEnum.WEB_API.getCode().equals(companyData.getSignUpType().getCode())) {
                company.setFromFederatedLogin(false);
                company.setCompanySignedUpFrom(SignUpTypeEnum.WEB_API.getCode());
            }
        }
        company.setActive(true);
        company.setAdminActive(true);
        //Create a new company
        ServerSecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        CreatedCompany newCompany = signUpServiceLocal.createCompany(company);
        //Create a new lead
        if (newCompany.getCompanyId() != null) {
            company.setCompanyId(newCompany.getCompanyId());
            SecurityContext.getInstance().setDatabase(Constants.DATABASE_PAID);
            crmServiceLocal.createLeadFromSignUpper(NewCompany.toString(company));
        }
        SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        //Create usage plan
        if (!Constants.SIGNED_UP_FROM_IPHONE.equals(company.getCompanySignedUpFrom()) && !Constants.SIGNED_UP_FROM_ANDROID.equals(company.getCompanySignedUpFrom())) {
            signUpServiceLocal.createFreeTrialUsagePlan(newCompany.getCompanyId(), false, 4, host, "");
        }

        UserCompanyDTO userCompanyDTO = new UserCompanyDTO();
        userCompanyDTO.setUserID(newCompany.getAdminId());
        userCompanyDTO.setClusterDbName(Constants.DATABASE_FREE);
        userCompanyDTO.setCompanyID(newCompany.getCompanyId());
        companyData.setAuthInfo(this.obtainSession(new AuthTO(), userCompanyDTO));

        return successResponse(companyData);
    }

    @RequestMapping(value = "/countries", method = RequestMethod.GET, headers = {ACCESS_TOKEN})
    public Object getCountries() {
        ArrayList<SelectItemTO> countries = new ArrayList<>();
        for (SelectItem s : signUpServiceLocal.getCountries()) {
            countries.add(new SelectItemTO(s.getId(), s.getName(), s.getCode(), s.getCategory()));
        }

        return successResponse(countries);
    }

    @RequestMapping(value = "/switchCompany", method = RequestMethod.GET, headers = {SESSION_ID, ACCESS_TOKEN})
    public Object switchCompany(@RequestParam(value = "companyId") Integer companyId) {
        EdsUser user = null;
        if (this.validateUser() instanceof EdsUser) {
            user = (EdsUser) this.validateUser();
        }
        if (companyId == null) {
            companyId = WrapUtils.getInteger(ServerSecurityContext.getInstance().getCompanyId());
        }

        if (companyId == null || user == null) {
            return errorResponse(REQUEST_USER_NOT_AUTHORIZED, HttpServletResponse.SC_UNAUTHORIZED);
        }

        List<UserCompanyDTO> companies = this.getCompanies(user.getUserName(), null);
        UserCompanyDTO company = null;

        for (UserCompanyDTO item : companies) {
            if (companyId.equals(item.getCompanyID())) {
                company = item;
                break;
            }
        }

        if (company == null) {
            return this.errorResponse(ERROR_INVALID_QUERY_PARAM);
        }

        this.removeSession();

        AuthTO authTO = this.obtainSession(new AuthTO(), company);
        if (validateFreeTrial()) {
            return successResponse(authTO);
        }
        return this.freeTrailExpiredException();
    }

    @RequestMapping(value = "/removeSession", method = RequestMethod.DELETE, headers = {SESSION_ID, ACCESS_TOKEN})
    public Object removeSession() {
        try {
            if (this.validateSession() instanceof String) {
                sessionService.expireCurrentSession(this.validateSession().toString());
                return successResponse();
            } else {
                return errorResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse();
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET, headers = {SESSION_ID, ACCESS_TOKEN})
    public Object logout() {
        try {
            this.removeSession();
            ServerUtils.removeCookie(Constants.USER_NAME_COOKIE, servletResponse);
            ServerUtils.removeCookie(Constants.USER_PASSWORD_COOKIE, servletResponse);
            ServerUtils.removeCookie(Constants.SESSION_ID_COOKIE, servletResponse);
            ServerUtils.removeCookie(Constants.LAST_REQUEST_TIME, servletResponse);
            ServerUtils.removeCookie(Constants.HASH_LINK_COOKIE, servletResponse);
            ServerUtils.removeCookie(Constants.SECTION_HTML, servletResponse);
            ServerUtils.removeCookie(Constants.USER_AVAILABILITY, servletResponse);
            ServerUtils.removeCookie(Constants.USER_FULLNAME, servletResponse);
            ServerUtils.removeCookie(Constants.WEBAUTHTOKEN, servletResponse);
            ServerUtils.removeCookie(Constants.FROM_MARKETPLACE, servletResponse);
            return successResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse();
        }
    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST, headers = ACCESS_TOKEN, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object forgotPassword(@RequestBody UserTO user) {
        if (user == null || StringUtil.isEmpty(user.getEmail())) {
            return this.errorResponse("Email address is required");
        }
        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            return this.errorResponse("Email address not found");
        }

        List<UserCompanyDTO> users = globalAuthJdbcSpringManager.getUserCompanyByEmail(servletRequest.getServerName(), user.getEmail());
        if (users == null || users.size() == 0) {
            return errorResponse(REQUEST_USER_NOT_AUTHORIZED, HttpServletResponse.SC_UNAUTHORIZED);
        }

        for (UserCompanyDTO userCompany : users) {
            ServerSecurityContext.getInstance().setDatabase(userCompany.getClusterDbName());
            ServerSecurityContext.getInstance().setCompanyId(userCompany.getCompanyID());
            Map<Boolean, CompanyDomain> isKpi = new HashMap<>();
            isKpi.put(true, null);
            try {
                boolean isSent = loginServiceLocal.sendForgotPasswordNotification(userCompany.getUserID(), isKpi);
                if (isSent) {
                    return true;
                }
            } catch (EdsDbException e) {
                e.printStackTrace();
                return errorResponse(Boolean.FALSE);
            }
        }
        return successResponse(Boolean.TRUE);
    }

    private Object validateSession() {
        String session = ServerSecurityContext.getInstance().getSessionId();

        if (StringUtil.isEmpty(session)) {
            return errorResponse(REQUEST_USER_NOT_AUTHORIZED, HttpServletResponse.SC_UNAUTHORIZED);
        }

        return session;
    }


    private Object validateUser() {
        this.validateSession();
        EdsUser user;

        try {
            user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        } catch (Exception e) {
            user = null;
            e.printStackTrace();
        }

        if (user == null) {
            return errorResponse(REQUEST_USER_SESSION_EXPIRED, RESPONSE_SESSION_EXPIRED_CODE);
        }

        if (user.getDeleted() || Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) {
            return errorResponse(REQUEST_USER_SESSION_EXPIRED, RESPONSE_SESSION_EXPIRED_CODE);
        }
        if (user instanceof EdsClientContact) {
            if (user.getClientContact().getAccess() == null || !user.getClientContact().getAccess()) {
                return errorResponse(REQUEST_USER_SESSION_EXPIRED, RESPONSE_SESSION_EXPIRED_CODE);
            }
        }
        String status = userManager.getUserStatus(user.getObjectID());
        if (!Constants.EMPLOYEE_STATUS_ACTIVE.equals(status)) {
            return errorResponse(REQUEST_USER_SESSION_EXPIRED, RESPONSE_SESSION_EXPIRED_CODE);
        }

        return user;
    }

    private List<UserCompanyDTO> getCompanies(String username, String password) {
        String sessionId = SecurityContext.getInstance().getSessionId();
        String serverName = servletRequest.getServerName();
        log.info("Domain : " + serverName);
        List<UserCompanyDTO> companies;


        if (StringUtil.isEmpty(password)) {
            companies = globalAuthJdbcSpringManager.getAuthInfoByUsername(serverName, username);
        } else {
            companies = globalAuthJdbcSpringManager.getAuthInfoByUsernameAndPassword(serverName, username, password);
        }

        List<UserCompanyDTO> result = new ArrayList<>();

        for (UserCompanyDTO userCompanyDTO : companies) {
            ServerSecurityContext.getInstance().setCompanyId(userCompanyDTO.getCompanyID());
            ServerSecurityContext.getInstance().setDatabase(userCompanyDTO.getClusterDbName());

            try {
                userCompanyDTO = validateCompany(userCompanyDTO);
            } catch (Exception e) {
                e.printStackTrace();
                userCompanyDTO = null;
            }

            if (userCompanyDTO != null) {
                String logo = loginServiceLocal.getCompanyLogoURL(null);
                if (logo == null) {
                    logo = Constants.COMPANY_NO_LOGO;
                }
                userCompanyDTO.setLogo(logo);

                result.add(userCompanyDTO);
            }
        }

        ServerSecurityContext.getInstance().setSessionId(sessionId);

        return result;
    }

    private UserCompanyDTO validateCompany(UserCompanyDTO userCompanyDTO) {
        ServerSecurityContext.getInstance().setCompanyId(userCompanyDTO.getCompanyID());
        ServerSecurityContext.getInstance().setDatabase(userCompanyDTO.getClusterDbName());

        EdsCompany company = companyManager.get(userCompanyDTO.getCompanyID());

        if (company == null) {
            return null;
        }

        EdsUser user;

        try {
            user = userManager.get(userCompanyDTO.getUserID());
        } catch (Exception ignored) {
            return null;
        }

        if (user.getDeleted() || Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) {
            return null;
        }

        if (user instanceof EdsClientContact) {
            if (user.getClientContact().getAccess() == null || !user.getClientContact().getAccess()) {
                return null;
            }
        }

        String status = userManager.getUserStatus(user.getObjectID());

        if (!Constants.EMPLOYEE_STATUS_ACTIVE.equals(status)) {
            return null;
        }

        userCompanyDTO.setCompanyName(company.getName());
        if (company.getCompanySettings() != null) {
            userCompanyDTO.setShortDateFormat(company.getCompanySettings().getShortDateFormat());
            userCompanyDTO.setLongDateFormat(company.getCompanySettings().getLongDateFormat());
        }

        return userCompanyDTO;
    }

    private AuthTO obtainSession(AuthTO data, UserCompanyDTO company) {
        try {
            AuthDetails authDetails = new AuthDetails();
            authDetails.setUserID(company.getUserID());
            authDetails.setDatabase(company.getClusterDbName());
            authDetails.setCompanyID(company.getCompanyID());
            ServerSecurityContext.getInstance().setCompanyId(company.getCompanyID());
            ServerSecurityContext.getInstance().setDatabase(company.getClusterDbName());

            data.setSession(sessionService.obtainSessionAndRegisterInSystem(null, null, authDetails));

            ServerSecurityContext.getInstance().setSessionId(data.getSession());
            EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
            UserTO userTO = new UserTO(user);
            if (user.getRoles() != null) {
                ArrayList<SelectItemTO> roles = new ArrayList<>();
                for (EdsRole role : user.getRoles()) {
                    roles.add(new SelectItemTO(role.getObjectID(), role.getName(), role.getCode(), role.getDescription()));
                }
                userTO.setRoles(roles);
            }
            if (user.getPhoto() != null) {
                userTO.setImageUrl(commonServiceLocal.getImageUrl(user.getPhoto().getObjectID()));
            }
            data.setUser(userTO);
            data.setCompanyId(company.getCompanyID());
            //registerCookies();
        } catch (Exception e) {
            e.printStackTrace();
            //throw this.runtimeException();
        }

        data.setPassword(null);
        data.setUsername(null);

        return data;
    }

    private boolean validateFreeTrial() {
        UserSignUPSessionID signedUser = loginServiceLocal.getSignedUser();
        if (signedUser == null || !signedUser.getCompanyActive()) {
            this.removeSession();
            return false;
        }
        return true;
    }
}
