package com.edatasite.workforce.rest.v2.release10.auth;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.enums.DeviceTypeEnum;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.backend.server.app.BackendServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.RegistrationTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.PermissionSettings;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionService;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.Office365LoginService;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.app.social.facebook.FacebookAPIService;
import com.edatasite.workforce.gwt.core.server.app.social.facebook.model.User;
import com.edatasite.workforce.gwt.core.server.app.social.linkedin.LinkedinAPIService;
import com.edatasite.workforce.gwt.core.server.app.social.linkedin.model.LinkedInProfile;
import com.edatasite.workforce.gwt.core.server.controllers.GoogleAuthorizationController;
import com.edatasite.workforce.gwt.core.server.controllers.login.marketplace.UserInfo;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.rpc.AuthInfoItem;
import com.edatasite.workforce.gwt.core.server.rpc.UserSignUPSessionID;
import com.edatasite.workforce.gwt.core.server.rpc.office365.MeUserResponseTO;
import com.edatasite.workforce.gwt.core.server.rpc.office365.TokenResponseTO;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CountryCallingCodeLayer;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.pricing.client.PayPalCalculationHelper;
import com.edatasite.workforce.gwt.signup.client.rpc.CreatedCompany;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.gwt.signup.server.app.SignUpServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.CompanyTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.CountryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.EmailTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.LoginTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.PhoneTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.RoleTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.SelectCompanyTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.SignUpGenericTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.SignUpTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.SocialCheckMiniTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.SocialCheckTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.SocialDataTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.TokenTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DATABASE_FREE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.EMPLOYEE_STATUS_ACTIVE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.HOST_LIVE;

/**
 * Created by Dilsh0d on 9/28/2017.
 */
@Tag(name = "Auth", description = "Auth API")
@RestController
//@RequestMapping()
public class ApiAuthControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiAuthControllerV2.class);

    @Autowired
    private SignUpServiceLocal signUpServiceLocal;
    @Autowired
    private LoginServiceLocal loginServiceLocal;
    @Autowired
    private HttpServletRequest servletRequest;
    @Autowired
    private HttpServletResponse servletResponse;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private FacebookAPIService facebookAPIService;
    @Autowired
    private LinkedinAPIService linkedinAPIService;
    @Autowired
    private Office365LoginService office365LoginService;
    @Autowired
    private GoogleAuthorizationController googleAuthorizationController;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private BackendServiceLocal backendServiceLocal;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private UsagePlanManager usagePlanManager;


    @Operation(summary = "Login with email ", description = "Login with email")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code"),
            @ApiResponse(responseCode = "401", description = "Session Expired"),
            @ApiResponse(responseCode = "401", description = "Incorrect email or password"),
            @ApiResponse(responseCode = "426", description = "Free trial ended"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/email_login", method = RequestMethod.POST, headers = {ApiConstants.ACCESS_TOKEN},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object emailLogin(@RequestBody LoginTO loginTO) throws RestException {
        if (StringUtil.isEmpty(loginTO.getEmail()) || StringUtil.isEmpty(loginTO.getPassword())) {
            throw new RestException("Incorrect login or password", "Incorrect login or password", INCORRECT_USERNAME_OR_PASSWORD, HttpStatus.UNAUTHORIZED);
        }

        List<UserCompanyDTO> companies = getCompanyDTOList(loginTO.getEmail(), loginTO.getPassword());
        if (companies == null || companies.isEmpty()) {
            throw new RestException("Incorrect login or password", "Incorrect login or password", INCORRECT_USERNAME_OR_PASSWORD, HttpStatus.UNAUTHORIZED);
        }

        for (UserCompanyDTO companyDTO : companies) {
            String session = obtainSession(companyDTO);
//            if (validateFreeTrial()) {
                /*IMap<String, String> sessionMap = ApplicationCache.getInstance().getMap(CacheConstants.SESSION);
                EdsCompanySystemSettings settings = companySystemSettingsManager.findByCompanyID(companyDTO.getCompanyID());
                if (settings == null || StringUtils.isBlank(settings.getSessionLength())) {
                    sessionMap.put(CacheConstants.SESSION, session);
                } else {
                    sessionMap.put(CacheConstants.SESSION, session, Long.valueOf(settings.getSessionLength()), TimeUnit.MINUTES);
                }*/
            //Get user from new company
            EdsUser newUser = userManager.get(companyDTO.getUserID());
            if (loginTO.getDeviceToken() != null ){
                loginServiceLocal.setUserDeviceTypeAndToken(newUser.getObjectID(),DeviceTypeEnum.IPhone.toString(),loginTO.getDeviceToken());
            }
            //Get User roles
            List<RoleTO> roles = getUserRoles(newUser);
            return successResponse(new TokenTO(session, roles, newUser.getUUID()));
//            }
        }

        throw new RestException("Free trial ended", "Free trial ended", EXPIRED, HttpStatus.UPGRADE_REQUIRED);
    }


    @Operation(summary = "Login with pinfl for GTL ", description = "Login with pinfl for GTL")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code"),
            @ApiResponse(responseCode = "401", description = "Session Expired"),
            @ApiResponse(responseCode = "401", description = "Incorrect email or password"),
            @ApiResponse(responseCode = "426", description = "Free trial ended"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/pinfl_login", method = RequestMethod.POST, headers = {ApiConstants.ACCESS_TOKEN},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object pinflLogin(@RequestBody DynamicDto loginTO) throws RestException {
        String pinfl = (String) loginTO.getProperties().get("pinfl");
        String inn = (String) loginTO.getProperties().get("inn");
        if (StringUtil.isEmpty(pinfl) && StringUtil.isEmpty(inn)) {
            throw new RestException("Incorrect login or password", "Incorrect login or password", INCORRECT_USERNAME_OR_PASSWORD, HttpStatus.UNAUTHORIZED);
        }

        SecurityContext.getInstance().setCompanyId(90826);
        SecurityContext.getInstance().setDatabase(DATABASE_FREE);
        EdsEmployee employee;
        if (!StringUtil.isEmpty(pinfl)) {
            employee = employeeManager.getEmployeeByNumber(pinfl);
        } else {
            employee = employeeManager.getEmployeeByInn(inn);
        }
        if (employee == null) {
            throw new RestException("Incorrect login or password", "Incorrect login or password", INCORRECT_USERNAME_OR_PASSWORD, HttpStatus.UNAUTHORIZED);
        }
        List<UserCompanyDTO> companies = getCompanyDTOList(employee.getUserName(), null);
        if (companies.isEmpty()) {
            throw new RestException("Incorrect login or password", "Incorrect login or password", INCORRECT_USERNAME_OR_PASSWORD, HttpStatus.UNAUTHORIZED);
        }

        for (UserCompanyDTO companyDTO : companies) {
            String session = obtainSession(companyDTO);
            EdsUser newUser = userManager.get(companyDTO.getUserID());
            List<RoleTO> roles = getUserRoles(newUser);
            return successResponse(new TokenTO(session, roles, newUser.getUUID()));
        }

        throw new RestException("Free trial ended", "Free trial ended", EXPIRED, HttpStatus.UPGRADE_REQUIRED);
    }

    @Operation(summary = "Log Out", description = "Remove user authentification data")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    public Object loguot() throws RestException {
        try {
            removeSession();
            return successResponse(new ResponseData());
        } catch (Exception e) {
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Country Codes", description = "Retrieves list of country details")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have country_id, country_code, country_name"),
            @ApiResponse(responseCode = "401", description = "Session Expired"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/country_codes", method = RequestMethod.GET, headers = {ApiConstants.ACCESS_TOKEN},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.ALL_VALUE})
    public Object getCountryCodes() throws RestException {
        try {
            ArrayList<CountryTO> countries = new ArrayList<>();
            List<EdsCountry> countryList = countryManager.list();
            for (EdsCountry country : countryList) {
                countries.add(new CountryTO(country.getObjectID(), country.getTelCode(), country.getName()));
            }
            ResponseListData<CountryTO> result = new ResponseListData<>();
            result.setList(countries);
            return successResponse(result);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Get list of Companies", description = "Retrieves list of companies that current user has")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have company_id, company_name, company_logo"),
            @ApiResponse(responseCode = "401", description = "Session Expired"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/list_companies", method = RequestMethod.GET, headers = {X_AUTH, ACCESS_TOKEN},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.ALL_VALUE})
    public Object getCompanies() throws RestException {
        EdsUser user = null;
        //Validates user
        Object object = validateUser();

        if (object == null) {
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }
        if (object instanceof EdsUser) {
            user = (EdsUser) object;
        }
        if (user == null) {
            return object;
        }
        String username = globalAuthJdbcSpringManager.getUsername(user.getCompany().getObjectID(), user.getObjectID());
        List<UserCompanyDTO> companies = StringUtils.isNotBlank(username) ? getCompanyDTOList(username, null) : new ArrayList<>();

        ResponseListData<CompanyTO> result = new ResponseListData<>();
        ArrayList<CompanyTO> companyList = new ArrayList<>();
        for (UserCompanyDTO company : companies) {
            try {
                //we need to create session only if we will validate trial this.obtainSession(company);
                try {
//                    if (validateFreeTrial()) {
                    CompanyTO companyTO = new CompanyTO();
                    companyTO.setCompany_id(company.getCompanyID());
                    companyTO.setCompany_name(company.getCompanyName());
                    companyTO.setCompany_logo(company.getLogo());
                    companyTO.setStatus(company.getStatus());
                    companyList.add(companyTO);

//                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }

        /*if(companyList.isEmpty()) {
            throw new RestException("Your subscription period has expired, please renew your account.", "Your subscription period has expired, please renew your account.", EXPIRED, HttpStatus.UNAUTHORIZED);
        }*/
        result.setList(companyList);

        return successResponse(result);
    }

    @Operation(summary = "Select Company", description = "Retrieves access token of the selected company")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have token of the selected company"),
            @ApiResponse(responseCode = "404", description = "Company does not exist with provided company_id"),
            @ApiResponse(responseCode = "401", description = "Session Expired"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/select_company", method = RequestMethod.POST, headers = {X_AUTH, ACCESS_TOKEN},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object selectCompany(@RequestBody SelectCompanyTO selectCompanyTO) throws RestException {
        EdsUser user = null;
        Object object = this.validateUser();
        if (object instanceof EdsUser) {
            user = (EdsUser) object;
        }
        Integer company_id = (selectCompanyTO != null && selectCompanyTO.getCompany_id() != null) ? selectCompanyTO.getCompany_id() : null;
        if (company_id == null) {
            company_id = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        }

        if (company_id == null || user == null) {
            throw new RestException(ERROR_MESSAGE, "Not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        String username = globalAuthJdbcSpringManager.getUsername(user.getCompany().getObjectID(), user.getObjectID());

        List<UserCompanyDTO> companies = StringUtils.isNotBlank(username) ? getCompanyDTOList(username, null) : new ArrayList<>();
        UserCompanyDTO company = null;

        for (UserCompanyDTO item : companies) {
            if (company_id.equals(item.getCompanyID())) {
                company = item;
                break;
            }
        }

        if (company == null) {
            throw new RestException(ERROR_MESSAGE, "Not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        this.removeSession();

        String sessionId = this.obtainSession(company);

        //Get user from new company
        EdsUser newUser = userManager.get(company.getUserID());
        //Get User roles
        List<RoleTO> roles = getUserRoles(newUser);

        {
            RedisClient.setKey(sessionId, new AuthInfoItem().buildForBasicLogin(newUser.getUserName(), newUser.getPassword(), company.getServiceID()), AuthInfoItem.class);
        }
        return successResponse(new TokenTO(sessionId, ServerUtils.getUserLocale().getLanguage(), roles));
    }

    private List<RoleTO> getUserRoles(EdsUser user) {
        if (user != null && !user.getRoles().isEmpty()) {
            return user.getRoles().stream().map(role -> new RoleTO(role.getCode(), role.getName(), role.getSystem())).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Operation(summary = "Forgot Password", description = "Sends email to the provided email address to reset the password")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code"),
            @ApiResponse(responseCode = "404", description = "Invalid email address"),
            @ApiResponse(responseCode = "401", description = "Session Expired"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/forgot_password", method = RequestMethod.POST, headers = {ACCESS_TOKEN},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object forgotPassword(@RequestBody EmailTO emailTO) throws RestException {
        if (emailTO == null || StringUtil.isEmpty(emailTO.getEmail())) {
            throw new RestException("Email address required", "Email address required", REQUIRED, HttpStatus.BAD_REQUEST);

        }
        if (!EMAIL_PATTERN.matcher(emailTO.getEmail()).matches()) {
            throw new RestException("Invalid email address", "Invalid email address", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<UserCompanyDTO> users = globalAuthJdbcSpringManager.getUserCompanyByEmail(servletRequest.getServerName(), emailTO.getEmail());
        if (users == null || users.size() == 0) {
            throw new RestException("There is no user with such email. Please double check and try again", "There is no user with such email. Please double check and try again", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        boolean isSend = false;
        for (UserCompanyDTO userCompany : users) {
            ServerSecurityContext.getInstance().setDatabase(userCompany.getClusterDbName());
            ServerSecurityContext.getInstance().setCompanyId(userCompany.getCompanyID());
            HashMap<Boolean, CompanyDomain> isKpi = new HashMap<>();
            isKpi.put(true, null);
            try {
                isSend = loginServiceLocal.sendForgotPasswordNotification(userCompany.getUserID(), isKpi);
            } catch (EdsDbException e) {
                e.printStackTrace();
            }
        }
        if (isSend) {
            return successResponse(new ResponseData());
        }

        throw new RestException(ERROR_MESSAGE, "Error occurred while sending email notification", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Social Check", description = "Contains user information from the social network \n\n social_type should be FACEBOOK, GOOGLE, LINKED_IN or MICROSOFT")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will contain user information from the social network"),
            @ApiResponse(responseCode = "400", description = "Social type and social token required"),
            @ApiResponse(responseCode = "401", description = "Session Expired"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/social_check", method = RequestMethod.POST, headers = ACCESS_TOKEN,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object socialCheck(@RequestBody SocialCheckMiniTO socialCheckMini) throws RestException {
        if (StringUtil.isEmpty(socialCheckMini.getSocial_type())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Social type required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtil.isEmpty(socialCheckMini.getSocial_token())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Social token required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SocialCheckTO socialCheckResult = new SocialCheckTO();
        socialCheckResult.setSocial_type(socialCheckMini.getSocial_type());
        socialCheckResult.setSocial_token(socialCheckMini.getSocial_token());

        SocialDataTO socialData = new SocialDataTO();
        String username = "";

        if (RegistrationTypeEnum.FACEBOOK.getType().equalsIgnoreCase(socialCheckMini.getSocial_type())) {
            User facebookUser = facebookAPIService.getUserProfile(socialCheckMini.getSocial_token(), "id,email,first_name,last_name,location{location},picture.type(large)");

            if (facebookUser != null) {

                username = facebookUser.getId();

                socialData.setEmail(facebookUser.getEmail());
                socialData.setFirst_name(facebookUser.getFirst_name());
                socialData.setLast_name(facebookUser.getLast_name());
                if (facebookUser.getPicture() != null && facebookUser.getPicture().getData() != null) {
                    socialData.setAvatar_image(facebookUser.getPicture().getData().getUrl());
                }
                if (facebookUser.getLocation() != null && facebookUser.getLocation().getLocation() != null
                        && StringUtils.isNotBlank(facebookUser.getLocation().getLocation().getCountry())) {
                    EdsCountry country = countryManager.getCountryByName(facebookUser.getLocation().getLocation().getCountry());
                    if (country != null) {
                        PhoneTO phoneTO = new PhoneTO(country.getTelCode(), "");
                        socialData.setPhone(phoneTO);
                    }
                }
            } else {
                //if user not exist
                throw new RestException("Connection with Facebook failed. Please check back later.", "Facebook call error.", REQUIRED, HttpStatus.BAD_REQUEST);
            }
        } else if (RegistrationTypeEnum.GOOGLE.getType().equalsIgnoreCase(socialCheckMini.getSocial_type())) {
            UserInfo googleUserInfo = null;

            try {
                googleUserInfo = googleAuthorizationController.extractUserInfoFromMobileAppToken(socialCheckMini.getSocial_token());
            } catch (Exception e) {
                log.error("Google Get User Data Error: {}", e.getMessage());
            }

            if (googleUserInfo != null) {
                //Get Google User Id to verify later
                username = googleUserInfo.getClaimedId();

                socialData.setEmail(googleUserInfo.getEmail());
                socialData.setFirst_name(googleUserInfo.getFirstName());
                socialData.setLast_name(googleUserInfo.getLastName());
                /*PhoneTO phoneTO = new PhoneTO(googleUserInfo.getCountry(), "");
                socialData.setPhone(phoneTO);*/
            } else {
                //if user not exist
                throw new RestException("Connection with Google failed. Please check back later.", "Invalid token.", REQUIRED, HttpStatus.BAD_REQUEST);
            }
        } else if (RegistrationTypeEnum.LINKEDIN.getType().equalsIgnoreCase(socialCheckMini.getSocial_type())) {

//            LinkedInProfile linkedInProfile = linkedinAPIService.getLinkedinUserData(socialCheckMini.getSocial_token(), ":(id,first-name,last-name,email-address,headline,industry,site-standard-profile-request,public-profile-url,picture-url::(original),summary)");
            LinkedInProfile linkedInProfile = linkedinAPIService.getLinkedinUserData(socialCheckMini.getSocial_token(), ":(id,first-name,last-name,email-address,headline,industry,site-standard-profile-request,public-profile-url,picture-urls::(original),summary)");

            if (linkedInProfile != null) {
                username = linkedInProfile.getId();

                socialData.setEmail(linkedInProfile.getEmailAddress());
                socialData.setFirst_name(linkedInProfile.getFirstName());
                socialData.setLast_name(linkedInProfile.getLastName());
                if (linkedInProfile.getPictureUrls() != null && linkedInProfile.getPictureUrls().getValues() != null
                        && linkedInProfile.getPictureUrls().getValues().length > 0) {
                    socialData.setAvatar_image(linkedInProfile.getPictureUrls().getValues()[0]);
                } else if (StringUtils.isNotBlank(linkedInProfile.getPictureUrl())) {
                    socialData.setAvatar_image(linkedInProfile.getPictureUrl());
                }
            } else {
                //if user not exist
                throw new RestException("Connection with LinkedIn failed. Please check back later.", "LinkedIn call error.", REQUIRED, HttpStatus.BAD_REQUEST);
            }
        } else if (RegistrationTypeEnum.MICROSOFT.getType().equalsIgnoreCase(socialCheckMini.getSocial_type())) {

            MeUserResponseTO meUserResponseTO = office365LoginService.getUserByToken(new TokenResponseTO(socialCheckMini.getSocial_token()));

            if (meUserResponseTO != null) {
                username = meUserResponseTO.getId();

                if (StringUtils.isNotBlank(meUserResponseTO.getMail())) {
                    socialData.setEmail(meUserResponseTO.getMail());
                } else {
                    socialData.setEmail(meUserResponseTO.getUserPrincipalName());
                }
                //@TODO must be firstname
                socialData.setFirst_name(meUserResponseTO.getGivenName());
                socialData.setLast_name(meUserResponseTO.getSurname());
                PhoneTO phoneTO = new PhoneTO(meUserResponseTO.getMobilePhone());
                socialData.setPhone(phoneTO);
            } else {
                //if user not exist
                throw new RestException("Connection with Microsoft failed. Please check back later.", "Microsoft call error.", REQUIRED, HttpStatus.BAD_REQUEST);
            }
        }
        log.info("SOCIAL CHECK: TOKEN=" + socialCheckMini.getSocial_token());
        log.info("SOCIAL CHECK: TYPE=" + socialCheckMini.getSocial_type());
        log.info("SOCIAL CHECK: username=" + username);
        log.info("SOCIAL CHECK: EdsContextParams.getHostname()=" + EdsContextParams.getHostname());
        //IF username was determined from social network then try to loggin into KPI and generate KPI Session Token
        if (StringUtils.isNotBlank(username/*socialData.getEmail()*/)) {
            //Obtain Token (sessionID)
            String sessionID = loginWithEmail(username/*socialCheckResult.getSocial_data().getEmail()*/, "restapi", EdsContextParams.getHostname());
            if (StringUtils.isBlank(sessionID) && StringUtils.isNotBlank(socialData.getEmail())) {
                log.info("SOCIAL CHECK: COULDNT OBTAIN SESSION WITH =" + username);
                log.info("SOCIAL CHECK: TRYING OBTAIN SESSION WITH =" + socialData.getEmail());
                sessionID = loginWithEmail(socialData.getEmail(), "restapi", EdsContextParams.getHostname());
            }
            log.info("SOCIAL CHECK: FINAL SESSION =" + sessionID);

            if (StringUtils.isNotBlank(sessionID)) {
                socialCheckResult.setUser_token(sessionID);
                socialCheckResult.setUser_exist(true);
            } else {
                socialCheckResult.setUser_exist(false);
                socialCheckResult.setSocial_data(socialData);
            }
        } else {
            socialCheckResult.setUser_exist(false);
            socialCheckResult.setSocial_data(socialData);
        }
        //Special Check for IOS
        if (!socialCheckResult.getUser_exist() && "IOS".equalsIgnoreCase(socialCheckMini.getDevice_os())) {
            //if user not exist
            throw new RestException("There is no such user. Please register at https://kpi.com", "Means user_exist=false and called from IOS.", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        return successResponse(socialCheckResult);
    }

    public String loginWithEmail(String email, String userAgent, String hostUrl) {
        try {
            List<UserCompanyDTO> globalauthInfo = globalAuthJdbcSpringManager.getAuthInfoByUsername(StringUtils.isNotBlank(hostUrl) ? hostUrl : HOST_LIVE, email);
            ArrayList<UserCompanyDTO> companyDTOList = new ArrayList<>();

            if (companyDTOList != null) {
                for (UserCompanyDTO userCompanyDTO : globalauthInfo) {
                    ServerSecurityContext.getInstance().setCompanyId(userCompanyDTO.getCompanyID());
                    ServerSecurityContext.getInstance().setDatabase(userCompanyDTO.getClusterDbName());
                    UserCompanyDTO checkedItem = loginServiceLocal.filterUserCompanyDTOList(userCompanyDTO);
                    if (checkedItem != null) {
                        companyDTOList.add(checkedItem);
                    }
                }

            }

            log.info("USER " + email + " COMPANIES SIZE = " + companyDTOList.size());

            if (companyDTOList.size() > 0) {//user has only one company
                AuthDetails authDetails = fillAuthDetails(userAgent, companyDTOList.get(0));
                log.info("Companyid= " + authDetails.getCompanyID());
                log.info("Userid= " + authDetails.getUserID());
                log.info("Database= " + authDetails.getDatabase());
                ServerSecurityContext.getInstance().setCompanyId(authDetails.getCompanyID());
                ServerSecurityContext.getInstance().setDatabase(authDetails.getDatabase());
                //Obtaining session
                String sessionID = sessionService.obtainSession(authDetails);
                return sessionID;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    private AuthDetails fillAuthDetails(String userAgent, UserCompanyDTO userDetails) {
        AuthDetails authDetails = new AuthDetails();
        authDetails.setCompanyID(userDetails.getCompanyID());
        authDetails.setDatabase(userDetails.getClusterDbName());
        authDetails.setUserID(userDetails.getUserID());
        authDetails.setUserAgent(userAgent);
        return authDetails;
    }


    @Operation(summary = "Register", description = "New user will be registered to the system \n\n type should be FACEBOOK, GOOGLE, LINKED_IN or MICROSOFT")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code"),
            @ApiResponse(responseCode = "400", description = "name, email, phone number and type are required"),
            @ApiResponse(responseCode = "404", description = "Invalid email address"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/registration", method = RequestMethod.POST, headers = {ACCESS_TOKEN},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object registration(@RequestBody SignUpTO signUpTO) throws RestException {

        log.info("Server Name: " + servletRequest.getServerName());
        String username = null;
        if (StringUtils.isBlank(signUpTO.getName())) {
            throw new RestException("Name required", "Name required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(signUpTO.getEmail())) {
            throw new RestException("Email address required", "Email address required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (signUpTO.getPhone() == null) {
            throw new RestException("Phone number required", "Phone number required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(signUpTO.getPhone().getCountry_code()) || StringUtils.isBlank(signUpTO.getPhone().getPhone_number())) {
            throw new RestException("Phone number required", "Phone number required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(signUpTO.getRegistration_type())) {
            throw new RestException("Registration type required", "Registration type required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (RegistrationTypeEnum.EMAIL.getType().equalsIgnoreCase(signUpTO.getRegistration_type())) {
            //if email sign up
            if (!EMAIL_PATTERN.matcher(signUpTO.getEmail()).matches()) {
                throw new RestException("Invalid email address", "Invalid email address", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            username = signUpTO.getEmail();
        } else {
            //if social sign up
            if (StringUtils.isBlank(signUpTO.getSocial_token())) {
                throw new RestException("Social token required", "Social token required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (RegistrationTypeEnum.FACEBOOK.getType().equalsIgnoreCase(signUpTO.getRegistration_type())) {
                User facebookUser = facebookAPIService.getUserProfile(signUpTO.getSocial_token(), "id");
                if (facebookUser != null) {
                    username = facebookUser.getId();
                } else {
                    throw new RestException("Connection with Facebook failed. Please check back later.", "Facebook call error.", REQUIRED, HttpStatus.BAD_REQUEST);
                }
            } else if (RegistrationTypeEnum.GOOGLE.getType().equalsIgnoreCase(signUpTO.getRegistration_type())) {
                UserInfo googleUserInfo = null;
                try {
                    googleUserInfo = googleAuthorizationController.extractUserInfoFromMobileAppToken(signUpTO.getSocial_token());
                } catch (Exception e) {
                    log.error("Google Get User Data Error: {}", e.getMessage());
                }
                if (googleUserInfo != null) {
                    username = googleUserInfo.getClaimedId();
                } else {
                    throw new RestException("Connection with Facebook failed. Please check back later.", "Facebook call error.", REQUIRED, HttpStatus.BAD_REQUEST);
                }
            } else if (RegistrationTypeEnum.LINKEDIN.getType().equalsIgnoreCase(signUpTO.getRegistration_type())) {
                LinkedInProfile linkedInProfile = null;
                try {
                    linkedInProfile = linkedinAPIService.getLinkedinUserData(signUpTO.getSocial_token(), ":(id)");
                } catch (Exception e) {
                    log.error("Linkedin Get User Data Error: {}", e.getMessage());
                }
                if (linkedInProfile != null) {
                    username = linkedInProfile.getId();
                } else {
                    throw new RestException("Connection with Linkedin failed. Please check back later.", "Linkedin call error.", REQUIRED, HttpStatus.BAD_REQUEST);
                }
            } else if (RegistrationTypeEnum.MICROSOFT.getType().equalsIgnoreCase(signUpTO.getRegistration_type())) {
                MeUserResponseTO meUserResponseTO = null;
                try {
                    meUserResponseTO = office365LoginService.getUserByToken(new TokenResponseTO(signUpTO.getSocial_token()));
                } catch (Exception e) {
                    log.error("Microsoft Get User Data Error: {}", e.getMessage());
                }
                if (meUserResponseTO != null) {
                    username = meUserResponseTO.getId();
                } else {
                    throw new RestException("Connection with Microsoft failed. Please check back later.", "Microsoft call error.", REQUIRED, HttpStatus.BAD_REQUEST);
                }
            }
        }

        if (StringUtils.isBlank(username)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Can not obtain username from social network: " + signUpTO.getRegistration_type(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        //check user for existence
        log.info("Host Name: " + EdsContextParams.getHostname());

        String password = userManager.findActiveAndNonFederateLoginUsers(EdsContextParams.getHostname(), username);

        log.info("-------------------------ApiAuthControllerV2.registration type:" + signUpTO.getRegistration_type() + " username:" + username + " password:" + password);

        if (StringUtils.isNotBlank(password)) {
            throw new RestException("Username already exists", "Username already exists: " + username, EMAIL_OR_PHONE_EXIST, HttpStatus.CONFLICT);
        }

        String callCode = signUpTO.getPhone().getCountry_code();
        String countryCode = CountryCallingCodeLayer.getCountryCodeByCallCode(callCode);

        NewCompany company = new NewCompany();
        company.setName(signUpTO.getCompany_name());
        company.setAdminFName(signUpTO.getName());
        company.setAdminEmail(signUpTO.getEmail());
        company.setPhone(signUpTO.getPhone().getPhone_number());
        company.setCallCode(signUpTO.getPhone().getCountry_code());
        company.setRegistrationType(RegistrationTypeEnum.getRegistrationType(signUpTO.getRegistration_type()));
        company.setAdminSocialImageUrl(signUpTO.getAvatar_image());
        company.setSocialUserName(username);
        company.setCallCode(callCode);

        EdsCountry country = null;
        if (!StringUtil.isEmpty(countryCode)) {
            country = countryManager.getCountryByCode(countryCode);
        }
        if (country != null) {
            company.setCountryID(country.getObjectID());
        }
        company.setCountryCode(countryCode);
        company.setRedirectToSettings(true);


        ServerUtils.fillHostParameters(servletRequest);

        company.setClientSingUpIPAddress(servletRequest.getRemoteAddr());
        company.setSignedUpPage(Constants.MY_WORKSPACE);
        company.setActive(true);


        ServerSecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        company.setLocale("en");
        company.setTheme("workforce");
        company.setHost(servletRequest.getServerName());
        company.setCompanySignedUpFrom(signUpTO.getRegistration_type());

        AuthDetails authDetails;
        try {

            Integer objectID = signUpServiceLocal.getCompany();
            company.setCompanyId(objectID);

            try {
                backendServiceLocal.createSchemaByID(objectID, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            CreatedCompany newCompany = signUpServiceLocal.createCompany(company);

            if (company.getName() != null && !"".equals(company.getName())) {
                company.setName(StringEscapeUtils.unescapeHtml(company.getName()));
            }
            if (company.getAdminFName() != null && !"".equals(company.getAdminFName())) {
                company.setAdminFName(StringEscapeUtils.unescapeHtml(company.getAdminFName()));
            }
            if (company.getAdminLName() != null && !"".equals(company.getAdminLName())) {
                company.setAdminLName(StringEscapeUtils.unescapeHtml(company.getAdminLName()));
            }
            //Lead Create
            if (newCompany != null && newCompany.getCompanyId() != null) {
                //company.setCompanyId(newCompany.getCompanyId());//todo need ask
                SecurityContext.getInstance().setDatabase(Constants.DATABASE_PAID);
                crmServiceLocal.createLeadFromSignUpper(NewCompany.toString(company));
            }

            SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);

            //Usage Plan
            Boolean isCurrencyGBP = false;

            Integer usersSize = null;
            try {
                usersSize = ServletRequestUtils.getIntParameter(servletRequest, "users");//free users count, default 4 users;
            } catch (Exception ignored) {
            }
            String pricingPackageNAME = null;
            try {
                String pricingCategory = ServletRequestUtils.getStringParameter(servletRequest, "category");//selected pricing package;
                pricingPackageNAME = (pricingCategory != null && !"".equals(pricingCategory)) ? PayPalCalculationHelper.getPricingPackageNAME(pricingCategory) : "";
            } catch (ServletRequestBindingException e) {
                e.printStackTrace();
            }


            if (Constants.UK.equals(countryCode)) {
                isCurrencyGBP = true;
            }

            ServerSecurityContext.getInstance().setCompanyId(newCompany.getCompanyId());
            String hostName = String.valueOf(servletRequest.getServerName());
            signUpServiceLocal.createFreeTrialUsagePlan(newCompany.getCompanyId(), isCurrencyGBP, (usersSize != null ? usersSize : 4), hostName, pricingPackageNAME);

            authDetails = new AuthDetails(newCompany.getCompanyId(), newCompany.getAdminId(), ServerSecurityContext.getInstance().getDatabase());
            authDetails.setUserAgent(servletRequest.getHeader("user-agent"));
            authDetails.setIpAddress(ServerUtils.obtainClientIP(servletRequest));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //for login after signing up with a NEW user-name
        String sessionID;
        try {
            sessionID = sessionService.obtainSessionAndRegisterInSystem(servletRequest, servletResponse, authDetails);
        } catch (IOException e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (sessionID == null) {
            throw new RestException(ERROR_MESSAGE, "Registration failed", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return successResponse(new TokenTO(sessionID));
    }

    @Operation(summary = "Generic Registration", description = "New user will be registered to the system")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code"),
            @ApiResponse(responseCode = "400", description = "name, email, phone number"),
            @ApiResponse(responseCode = "404", description = "Invalid email address"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/registration_generic", method = RequestMethod.POST, headers = {ACCESS_TOKEN},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object registrationGeneric(@RequestBody SignUpGenericTO signUpGenericTO) throws RestException {

        log.info("Server Name: " + servletRequest.getServerName());
        String username = null;
        if (StringUtils.isBlank(signUpGenericTO.getName())) {
            throw new RestException("Name required", "Name required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(signUpGenericTO.getEmail())) {
            throw new RestException("Email address required", "Email address required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (signUpGenericTO.getPhone() == null) {
            throw new RestException("Phone number required", "Phone number required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(signUpGenericTO.getPhone().getCountry_code()) || StringUtils.isBlank(signUpGenericTO.getPhone().getPhone_number())) {
            throw new RestException("Phone number required", "Phone number required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        //if email sign up
        if (!EMAIL_PATTERN.matcher(signUpGenericTO.getEmail()).matches()) {
            throw new RestException("Invalid email address", "Invalid email address", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        username = signUpGenericTO.getEmail();

        //check user for existence
        log.info("Host Name: " + EdsContextParams.getHostname());

        String password = userManager.findActiveAndNonFederateLoginUsers(EdsContextParams.getHostname(), username);

        log.info("-------------------------ApiAuthControllerV2.registration_generic type:" + RegistrationTypeEnum.EMAIL.getType() + " username:" + username + " password:" + password);

        if (StringUtils.isNotBlank(password)) {
            throw new RestException("Username already exists", "Username already exists: " + username, EMAIL_OR_PHONE_EXIST, HttpStatus.CONFLICT);
        }

        String callCode = signUpGenericTO.getPhone().getCountry_code();
        String countryCode = CountryCallingCodeLayer.getCountryCodeByCallCode(callCode);

        NewCompany company = new NewCompany();
        company.setName(signUpGenericTO.getCompany_name());
        company.setAdminFName(signUpGenericTO.getName());
        company.setAdminEmail(signUpGenericTO.getEmail());
        company.setPhone(signUpGenericTO.getPhone().getPhone_number());
        company.setCallCode(signUpGenericTO.getPhone().getCountry_code());
        company.setRegistrationType(RegistrationTypeEnum.EMAIL);
//        company.setAdminSocialImageUrl(signUpGenericTO.getAvatar_image());
        company.setSocialUserName(username);
        company.setCallCode(callCode);

        EdsCountry country = null;
        if (!StringUtil.isEmpty(countryCode)) {
            country = countryManager.getCountryByCode(countryCode);
        }
        if (country != null) {
            company.setCountryID(country.getObjectID());
        }
        company.setCountryCode(countryCode);
        company.setRedirectToSettings(true);


        ServerUtils.fillHostParameters(servletRequest);

        company.setClientSingUpIPAddress(servletRequest.getRemoteAddr());
        company.setSignedUpPage(Constants.MY_WORKSPACE);
        company.setActive(true);

        ServerSecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        //Set Company Language
        company.setLocale("en");
        if (StringUtils.isNotBlank(signUpGenericTO.getCompany_language())) {
            company.setLocale(signUpGenericTO.getCompany_language());
        }
        company.setTheme("workforce");
        company.setHost(servletRequest.getServerName());
        company.setCompanySignedUpFrom(RegistrationTypeEnum.EMAIL.getType());
        if (StringUtils.isNotBlank(company.getName())) {
            company.setName(StringEscapeUtils.unescapeHtml(company.getName()));
        }
        if (StringUtils.isNotBlank(company.getAdminFName())) {
            company.setAdminFName(StringEscapeUtils.unescapeHtml(company.getAdminFName()));
        }
        if (StringUtils.isNotBlank(company.getAdminLName())) {
            company.setAdminLName(StringEscapeUtils.unescapeHtml(company.getAdminLName()));
        }

        //Utm field
        if (signUpGenericTO.getCustom_fields() != null) {

            company.setUtm_campaign(signUpGenericTO.getCustom_fields().get("utm_campaign"));
            company.setUtm_source(signUpGenericTO.getCustom_fields().get("utm_source"));
            company.setUtm_medium(signUpGenericTO.getCustom_fields().get("utm_medium"));
            company.setUtm_keyword(signUpGenericTO.getCustom_fields().get("utm_keyword"));
            company.setUtm_btn(signUpGenericTO.getCustom_fields().get("utm_btn"));
            company.setUtm_content(signUpGenericTO.getCustom_fields().get("utm_content"));
            company.setUtm_term(signUpGenericTO.getCustom_fields().get("utm_term"));
            company.setGclid(signUpGenericTO.getCustom_fields().get("gclid"));
            if (signUpGenericTO.getCustom_fields().get("referrer") != null && signUpGenericTO.getCustom_fields().get("referrer").contains("http")) {
                company.setReferrer(signUpGenericTO.getCustom_fields().get("referrer"));
            }

            if (signUpGenericTO.getCustom_fields().get("redirected") != null && signUpGenericTO.getCustom_fields().get("redirected").contains("http")) {
                company.setRedirected(signUpGenericTO.getCustom_fields().get("redirected"));
            }
        }
        //End Of Utm field

        AuthDetails authDetails;
        try {
            CreatedCompany newCompany = signUpServiceLocal.createCompany(company);

            //Lead Create
            if (newCompany != null && newCompany.getCompanyId() != null) {
                company.setCompanyId(newCompany.getCompanyId());
                SecurityContext.getInstance().setDatabase(Constants.DATABASE_PAID);
                crmServiceLocal.createLeadFromSignUpper(NewCompany.toString(company));
            }

            SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);

            //Usage Plan
            Boolean isCurrencyGBP = false;

            Integer usersSize = null;
            try {
                usersSize = ServletRequestUtils.getIntParameter(servletRequest, "users");//free users count, default 4 users;
            } catch (Exception ignored) {
            }
            String pricingPackageNAME = null;
            try {
                String pricingCategory = ServletRequestUtils.getStringParameter(servletRequest, "category");//selected pricing package;
                pricingPackageNAME = (pricingCategory != null && !"".equals(pricingCategory)) ? PayPalCalculationHelper.getPricingPackageNAME(pricingCategory) : "";
            } catch (ServletRequestBindingException e) {
                e.printStackTrace();
            }


            if (Constants.UK.equals(countryCode)) {
                isCurrencyGBP = true;
            }

            ServerSecurityContext.getInstance().setCompanyId(newCompany.getCompanyId());
            String hostName = String.valueOf(servletRequest.getServerName());
            signUpServiceLocal.createFreeTrialUsagePlan(newCompany.getCompanyId(), isCurrencyGBP, (usersSize != null ? usersSize : 4), hostName, pricingPackageNAME);

            authDetails = new AuthDetails(newCompany.getCompanyId(), newCompany.getAdminId(), ServerSecurityContext.getInstance().getDatabase());
            authDetails.setUserAgent(servletRequest.getHeader("user-agent"));
            authDetails.setIpAddress(ServerUtils.obtainClientIP(servletRequest));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //for login after signing up with a NEW user-name
        String sessionID;
        try {
            sessionID = sessionService.obtainSessionAndRegisterInSystem(servletRequest, servletResponse, authDetails);
        } catch (IOException e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (sessionID == null) {
            throw new RestException(ERROR_MESSAGE, "Registration failed", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return successResponse(new TokenTO(sessionID));

    }

    @Operation(summary = "Get user permissions")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Lead"))
    @RequestMapping(path = "/permissions/{context}", method = RequestMethod.GET, headers = {X_AUTH, ACCESS_TOKEN}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getUserPermissions(@RequestParam(value = "context", required = false, defaultValue = PermissionConstants.WORKSPACE_CONTEXT) String context) {
        PermissionSettings permissionSettings = rolePermissionService.getPermissionSettings(context);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("userID", permissionSettings.getUserID());
        result.put("roles", permissionSettings.getRoles());
        result.put("permissions", permissionSettings.getPermissions());

        return result;
    }

    private boolean validateFreeTrial() {
        try {
            UserSignUPSessionID signedUser = loginServiceLocal.getSignedUser();
            if (signedUser == null || !signedUser.getCompanyActive()) {
                removeSession();
                return false;
            }
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
        return true;
    }

    private void removeSession() {
        if (!isExpireSession()) {
            String sessionId = ServerSecurityContext.getInstance().getSessionId();
            sessionService.expireCurrentSession(sessionId);
            {
                RedisClient.removeKey(sessionId);
            }
        }
    }

    private List<UserCompanyDTO> getCompanyDTOList(String username, String password) {
        String sessionId = SecurityContext.getInstance().getSessionId();
        String serverName = servletRequest.getServerName();
        List<UserCompanyDTO> companies;

        if (StringUtil.isEmpty(password)) {
            companies = globalAuthJdbcSpringManager.getAuthInfoByUsername(serverName, username);
        } else {
            companies = globalAuthJdbcSpringManager.getAuthInfoByUsernameAndPassword(serverName, username, password);
        }

        ArrayList<UserCompanyDTO> result = new ArrayList<>();

        for (UserCompanyDTO userCompanyDTO : companies) {
            ServerSecurityContext.getInstance().setCompanyId(userCompanyDTO.getCompanyID());
            ServerSecurityContext.getInstance().setDatabase(userCompanyDTO.getClusterDbName());

            userCompanyDTO = validateCompany(userCompanyDTO);
            if (userCompanyDTO != null) {
                String logo = loginServiceLocal.getCompanyLogoURL(null);
                if (logo == null) {
                    logo = Constants.COMPANY_NO_LOGO;
                }
                userCompanyDTO.setLogo(logo);

                setCompanyStatusCode(userCompanyDTO);
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

        if (user == null || user.getDeleted() || Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) {
            return null;
        }

        if (user instanceof EdsClientContact) {
            if (user.getClientContact().getAccess() == null || !user.getClientContact().getAccess()) {
                return null;
            }
        }

        String status = userManager.getUserStatus(user.getObjectID());

        if (!EMPLOYEE_STATUS_ACTIVE.equals(status)) {
            return null;
        }
        userCompanyDTO.setCompanyName(company.getName());

        return userCompanyDTO;
    }

    private String obtainSession(UserCompanyDTO companyDTO) {
        AuthDetails authDetails = new AuthDetails();
        authDetails.setUserID(companyDTO.getUserID());
        authDetails.setDatabase(companyDTO.getClusterDbName());
        authDetails.setCompanyID(companyDTO.getCompanyID());
        ServerSecurityContext.getInstance().setCompanyId(companyDTO.getCompanyID());
        ServerSecurityContext.getInstance().setDatabase(companyDTO.getClusterDbName());
        try {
            return sessionService.obtainSessionAndRegisterInSystem(null, null, authDetails);
        } catch (IOException e) {
            log.error("", e);
            return null;
        }
    }

    private void setCompanyStatusCode(UserCompanyDTO companyDTO) {
        EdsCompany company = companyManager.get(companyDTO.getCompanyID());
        if (company != null) {
            EdsUsagePlan usagePlan = usagePlanManager.getCurrentUsagePlan(company);
            UsagePlanItem usagePlanItem = loginServiceLocal.getUsagePlanItem(usagePlan, company.getObjectID());

            if (usagePlan != null && usagePlan.getPaid()) {
                companyDTO.setStatus(ACTIVE_COMPANY);
            } else if ((usagePlanItem.isCurrSub() && (usagePlanItem.isFree() || usagePlanItem.isPaid()))) {
                companyDTO.setStatus(FREE_COMPANY);
            }
            if (!(usagePlanItem.isCurrSub() && (usagePlanItem.isFree() || usagePlanItem.isPaid()))) {
                companyDTO.setStatus(EXPIRED_COMPANY);
            }
        }
    }
}
