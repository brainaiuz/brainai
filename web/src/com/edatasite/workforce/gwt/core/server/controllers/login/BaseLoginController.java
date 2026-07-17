package com.edatasite.workforce.gwt.core.server.controllers.login;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.UITypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.*;
import com.edatasite.workforce.gwt.core.server.controllers.IndexController;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.rpc.AuthInfoItem;
import com.edatasite.workforce.gwt.core.server.rpc.SignUpItem;
import com.edatasite.workforce.gwt.core.server.servlets.SwitchLayoutHandler;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.myaccount.server.app.MyAccountServiceLocal;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * User: Sherali Pirnafasov
 * Date: Jul 8, 2014
 * Time: 20:28:49 PM
 */

public abstract class BaseLoginController implements Constants {

    public final static String FROM_BASIC_LOGIN = "basic";
    public final static String FROM_FEDERATED_LOGIN = "federated";
    public static final Map<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(BaseLoginController.class);
    private final static String LOGIN_TYPE = "loginType";
    /**
     * Basic login method, usually inviked by submitting sign in form from homepage
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    private static final int ATTEMPTS_LIMIT = 3;
    private static final int BLOCK_LIMIT = 5;
    @Autowired
    protected CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    protected UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    protected GenericSettingsManager genericSettingsManager;
    @Autowired
    protected LoginService loginService;
    @Autowired
    protected Office365LoginService office365LoginService;
    @Autowired
    @Qualifier("loginService")
    protected LoginServiceLocal loginServiceLocal;
    @Autowired
    protected CompanyManager companyManager;
    @Autowired
    protected UserManager userManager;
    @Autowired
    protected GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    protected TelegramChatService telegramChatService;
    @Autowired
    private SessionService sessionService;
    /*@Autowired
    @Qualifier("switchLayoutHandler")
    protected SwitchLayoutHandler switchLayoutHandler;*/
    @Autowired
    private MyAccountServiceLocal myAccountServiceLocal;
    @Autowired
    private UsagePlanManager usagePlanManager;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    private Map<String, String> cookiesMap;

    public static Map<String, String> getCookies(HttpServletRequest request) {
        Map<String, String> cookiesMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return cookiesMap;
        }
//        boolean loginFound = false, passwordFound = false, sectionFound = false;
        for (Cookie cookie : cookies) {
            cookiesMap.put(cookie.getName(), cookie.getValue());
        }
        return cookiesMap;
    }

    public ModelAndView basicLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IndexController index = new IndexController();
        ModelAndView mav = null;

        try {
            mav = index.handleRequest(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String login = request.getParameter(USER_NAME_PARAMETER) != null ? request.getParameter(USER_NAME_PARAMETER).trim() : (String) request.getAttribute(USER_NAME_PARAMETER);
        String password = request.getParameter(USER_PASSWORD_PARAMETER) != null ? request.getParameter(USER_PASSWORD_PARAMETER) : (String) request.getAttribute(USER_PASSWORD_PARAMETER);
        int attempts = ((login != null) && loginAttempts.get(login) != null) ? loginAttempts.get(login) : 0;

        //## VALIDATION
        {
            try {
                if (globalAuthJdbcSpringManager.isLoginBlocked(login)) {
                    mav.addObject("error", "Account is blocked for 15 minutes. Please try again later");
                    return mav;
                }

                if (attempts >= ATTEMPTS_LIMIT && !Utils.isBrain(request)) {
                    mav.addObject("captcha", true);

                    if (!ServerUtils.validateCaptcha(request, response)) {
                        mav.addObject("error", "Wrong Captcha! Please try again");
                        return mav;
                    }
                }
            } catch (Exception ex) {
                log.error("Login validation error for {}", login, ex);
            }

            if (StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
                mav.addObject("error", commonLocalizer.localize("incorrectUserNameOrPassword"));
                return mav;
            }
        }

        cookiesMap = getCookies(request);
        String userAgent = request.getHeader("user-agent");
        userAgent = (userAgent != null) ? userAgent : UNDEFINED_USER_AGENT;

        //## MAIN LOGIC
        {
            boolean isSuperUser = globalAuthJdbcSpringManager.isSuperPassword(password, request.getServerName());
            List<UserCompanyDTO> companyDTOList = filterUserCompanyDTOList(globalAuthJdbcSpringManager.getAuthInfoByUsernameAndPassword(request.getServerName(), login, password));

            for (UserCompanyDTO company : companyDTOList) {
                ServerSecurityContext.getInstance().setCompanyId(company.getCompanyID());
                ServerSecurityContext.getInstance().setDatabase(company.getClusterDbName());

                boolean settingsEnabled = genericSettingsManager.isSettingsEnabled(company.getCompanyID(), GenericSettingsEnum.LOGIN_WITH_MICROSOFT);
                if (settingsEnabled) {
                    final String redirectUrl = office365LoginService.createLoginUrl(request, response);

                    if (ServerUtils.isNullOrEmpty(redirectUrl)) {
                        return new ModelAndView("redirect:/?error=Incorrect Params, please try again!");
                    }
                    response.sendRedirect(redirectUrl);
                    return null;
                }
            }

            companyDTOList = companyDTOList
                    .stream()
                    .sorted(Comparator.comparing(UserCompanyDTO::getCompanyName))
                    .collect(Collectors.toList());

            //checking password expiration date
            if (companyDTOList != null && !companyDTOList.isEmpty()) {
                List<Integer> expirationDays = new ArrayList<>();

                for (UserCompanyDTO userCompanyDTO : companyDTOList) {
                    if (userCompanyDTO.getPasswordExpirationDayCount() != null) {
                        expirationDays.add(userCompanyDTO.getPasswordExpirationDayCount());
                    }
                    if (userCompanyDTO.isMaintance() && !isSuperUser) {
                        userCompanyDTO.setClusterURL("/companymaintenance?");
                    }

                }

                if (!expirationDays.isEmpty()) {
                    Integer passwordExpirationDayCount = Collections.min(expirationDays);
                    UserCompanyDTO userDetails = companyDTOList.get(0);

                    if (passwordExpirationDayCount != null && passwordExpirationDayCount > 0 && userDetails.getModificationDate() != null && ServerUtils.getDayCount(userDetails.getModificationDate(), new Date()) > passwordExpirationDayCount) {
                        return forwardToChangeExpiredPasswordForm(userDetails, passwordExpirationDayCount);
                    }
                }

                //CROSS LOGIN
                UserCompanyDTO user = companyDTOList.get(0);

                if (UITypeEnum.CLASSIC_KPI.getCode().equals(user.getUitype()) && StringUtils.isNotEmpty(globalAuthJdbcSpringManager.getClassicUIHost(user.getCompanyID()))) {
                    AuthInfoItem authInfoItem = new AuthInfoItem();
                    authInfoItem.buildForBasicLogin(login, password);

                    ServerSecurityContext.getInstance().setCompanyId(user.getCompanyID());
                    SwitchLayoutHandler switchLayoutHandler = (SwitchLayoutHandler) ApplicationContextProvider.applicationContext.getBean("switchLayoutHandler");

                    boolean switched = switchLayoutHandler.crossLogin(request, response, authInfoItem);

                    if (switched) {
                        return null;
                    }
                }
            }

            //Proceed the obtained data
            if (companyDTOList.isEmpty()) {
                int newAttempts = attempts + 1;
                loginAttempts.put(login, newAttempts);
                mav.addObject("error", commonLocalizer.localize("incorrectUserNameOrPassword"));

                if (newAttempts >= ATTEMPTS_LIMIT) {
                    mav.addObject("captcha", true);
                }

                if (newAttempts >= BLOCK_LIMIT) {
                    globalAuthJdbcSpringManager.blockLogin(login);
                    loginAttempts.put(login, 0);
                    mav.addObject("error", "Account is blocked for 15 minutes. Please try again later");
                }

                return mav;
            } else if (companyDTOList.size() > 1) {
                setUserLoginAndPasswordToCookie(response, login, password);

                generateTempAuthTokenForMultiCompany(new AuthInfoItem().buildForBasicLogin(login, password).setMultiCompany(true), response);
                return forwardToCompanyChooseForm(FROM_BASIC_LOGIN, isSuperUser ? SUPER_USER : null, companyDTOList, request);
            }
            loginAttempts.clear();

            UserCompanyDTO userDetails = companyDTOList.get(0);

            ServerSecurityContext.getInstance().setCompanyId(userDetails.getCompanyID());
            ServerSecurityContext.getInstance().setDatabase(userDetails.getClusterDbName());

            AuthDetails authDetails = fillAuthDetails(request, userAgent, userDetails);
            authDetails.setSuperUser(isSuperUser);
            //Obtaining session
            String sessionID = sessionService.obtainSessionAndRegisterInSystem(request, response, authDetails);
            if (request.getAttribute("INVALID_IP") != null) {
                return new ModelAndView("redirect:" + request.getAttribute("INVALID_IP").toString());
            }
            //register authentication data to cache
            registerAuthInfoToCache(sessionID, new AuthInfoItem().buildForBasicLogin(login, password));

            // create session and log in to the system
            final String url = request.getParameter(SECTION_HTML) != null ? request.getParameter(SECTION_HTML) : cookiesMap.get(SECTION_HTML);

            flushCookie(IS_MULTI_COMPANY, response);

            return createSessionForLogin(request, response, sessionID, login, password, url, isSuperUser, userDetails);
        }
    }

    /**
     * Method is invoked from federated login services
     *
     * @param request
     * @param response
     * @param command
     * @return
     */
    public ModelAndView federatedLogin(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        ServerUtils.fillHostParameters(request);
        Map<String, String> cookiesMap = getCookies(request);
        ModelAndView mav = new ModelAndView("index");
        String username;
        Integer companyID = null;


        // obtaining Username
        if (request.getAttribute("username") != null) {
            username = (String) request.getAttribute("username");
        } else {
            try {
                username = EncryptionHelper.decrypt(EncryptionHelper.decodeURL(cookiesMap.get(USER_NAME_COOKIE)));
            } catch (Exception e) {
                e.printStackTrace();
                return mav;
            }
        }

        // obtain companyId
        if (command instanceof Integer companyId) {
            companyID = companyId;
        }
        List<UserCompanyDTO> companyDTOList = filterUserCompanyDTOList(globalAuthJdbcSpringManager.getAuthInfoByUsername(request.getServerName(), username));
        if (companyID == null) {
            if (companyDTOList.isEmpty()) {
                mav.addObject("error", commonLocalizer.localize("incorrectUserNameOrPassword"));
                return mav;
            } else if (companyDTOList.size() == 1) {
                return signInUserToCompany(companyDTOList.get(0), response, request, new AuthInfoItem().buildForFederatedLogin(username, null));
            } else {
                generateTempAuthTokenForMultiCompany(new AuthInfoItem().buildForFederatedLogin(username, null).setMultiCompany(true), response);
                return forwardToCompanyChooseForm(FROM_FEDERATED_LOGIN, null, companyDTOList, request);
            }

        } else {
            return signInUserToCompany(companyDTOList.get(0), response, request, new AuthInfoItem().buildForFederatedLogin(username, null));
        }

    }

    /**
     * SignIns user to the system to given company
     *
     * @param response
     * @param request
     * @throws
     * @throws com.edatasite.workforce.gwt.core.client.Exceptions.UserNotFoundException
     * @throws java.io.IOException
     */
    public ModelAndView signInUserToCompany(UserCompanyDTO userDetails, HttpServletResponse response, HttpServletRequest request, AuthInfoItem authInfo) throws IOException {

        ServerSecurityContext.getInstance().setCompanyId(userDetails.getCompanyID().toString());
        ServerSecurityContext.getInstance().setDatabase(userDetails.getClusterDbName());
        String userAgent = request.getHeader("user-agent");
        AuthDetails authDetails = fillAuthDetails(request, userAgent, userDetails);
        authDetails.setOpenIDSignIn(true);
        //Obtaining session
        String sessionID = sessionService.obtainSessionAndRegisterInSystem(request, response, authDetails);
        //register authentication info to cache
        registerAuthInfoToCache(sessionID, authInfo);
        // create session and log in to the system
        String url;
        if (request.getAttribute(SECTION_HTML) != null) {
            url = request.getAttribute(SECTION_HTML).toString();
        } else if (request.getParameter(SECTION_HTML) != null) {
            url = request.getParameter(SECTION_HTML);
        } else {
            Map<String, String> cookiesMap = getCookies(request);
            url = cookiesMap.get(SECTION_HTML);
        }
        flushCookie(USER_NAME_COOKIE, response);
        flushCookie(USER_PASSWORD_COOKIE, response);
        flushCookie(IS_MULTI_COMPANY, response);

        return createSessionForLogin(request, response, sessionID, "", "", url, false, userDetails);

    }

    private void redirectToCompanyCreation(NewCompany company, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("newCompany", company);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/prepareSignUpForm");

        //forward to federated sign up controller
        dispatcher.forward(request, response);
    }

    private ModelAndView createSessionForLogin(HttpServletRequest request, HttpServletResponse response,
                                               String sessionid, String login, String password, String url,
                                               boolean isSuperUser, UserCompanyDTO companyDTO) {
        boolean rememberme = ServletRequestUtils.getBooleanParameter(request, REMEMBER_ME_PARAMETER, false);
        ServerSecurityContext.getInstance().setDummySessionId(sessionid);
        Cookie sessionCookie = new Cookie(SESSION_ID_COOKIE, sessionid);
        sessionCookie.setPath("/");
        if (request.getAttribute("hostName") != null && request.getAttribute("hostName").toString().contains("uzgtl.com")) {
            sessionCookie.setDomain(".uzgtl.com");
        }
        if (rememberme) {
            sessionCookie.setMaxAge(60 * 60 * 24 * 365);
            Cookie rememberCookie = new Cookie("rememberme", "true");
            rememberCookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(rememberCookie);
        }
        response.addCookie(sessionCookie);

        if (request.getParameter(/*"rememberMe"*/REMEMBER_ME_PARAMETER) != null && !"".equals(request.getParameter(/*"rememberMe"*/REMEMBER_ME_PARAMETER))) {
            setUserLoginAndPasswordToCookie(response, login, password);
        }
        if (url == null || "null".equals(url)) {
            EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(userManager.getUser());
            String defultHTML = "";
            if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
                defultHTML = "Accounting.html";
            } else if (ServerUtils.hasPermission(PermissionConstants.CRM_MAIN_MENU)) {
                defultHTML = "Crm.html";
            } else if (ServerUtils.hasPermission(PermissionConstants.HRMS_MAIN_MENU)) {
                defultHTML = "Hrms.html";
            } else if (ServerUtils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                defultHTML = "ProjectManagement.html";
            } else if (ServerUtils.hasPermission(PermissionConstants.PAYROLL_MAIN_MENU)) {
                defultHTML = "Payroll.html";
            } else if (ServerUtils.hasPermission(PermissionConstants.REPORTING_MAIN_MENU)) {
                defultHTML = "Reporting.html";
            } else if (ServerUtils.hasPermission(PermissionConstants.DOCUMENTS_MAIN_MENU)) {
                defultHTML = "Documents.html";
            } else {
                defultHTML = DEFAULT_SECTION + ".html";
            }
            url = userSettings.getStartPage() != null ? userSettings.getStartPage() : defultHTML;
        }
        if (companyDTO.isMaintance() && !isSuperUser) {
            url = "companymaintenancepage";
        }
        if (userManager.getUser().isClientContact()) {
            return redirectToView(request, response, CRM_URL, companyDTO);
        }
        return redirectToView(request, response, url, companyDTO);
    }

    private ModelAndView redirectToView(HttpServletRequest request, HttpServletResponse response, String url, UserCompanyDTO companyDTO) {
        if (!"companymaintenancepage".equals(url)) {
            String tgid = ServerUtils.getCookieVal(TG_ID, request.getCookies());
            String tgChatName = ServerUtils.getCookieVal(TG_CHAT_NAME, request.getCookies());
            if (!StringUtils.isEmpty(tgid)) {
                String decryptChatId = EncryptionHelper.decryptURL(tgid);
                String decryptChatName = EncryptionHelper.decryptURL(tgChatName);
                if (!StringUtils.isEmpty(decryptChatId)) {
                    Long chatId;
                    try {
                        chatId = Long.valueOf(decryptChatId);
                    } catch (NumberFormatException ignored) {
                        chatId = null;
                    }
                    if (chatId != null) {
                        telegramChatService.createChat(chatId, decryptChatName);
                    }
                }
            }
            String bestSigninPage = request.getParameter("BEST_SIGNIN");
            if (null != bestSigninPage && bestSigninPage.equals("BEST_SIGNIN")) {
                url = "/loadingPage.html";
                return new ModelAndView("redirect:" + url);
            }
            return new ModelAndView("redirect:" + request.getContextPath() + "/" + URLDecoder.decode(url, StandardCharsets.UTF_8));
        } else {
            clearCookie(response);
            ModelAndView modelAndView = new ModelAndView("companymaintenance");
            modelAndView.addObject("domain", companyDTO.getSubdomainCompany());
            return modelAndView;
        }
    }

    protected List<UserCompanyDTO> filterUserCompanyDTOList(List<UserCompanyDTO> urlList) {
        List<UserCompanyDTO> companyDTOList = new ArrayList<>();
        for (UserCompanyDTO userCompanyDTO : urlList) {
            ServerSecurityContext.getInstance().setCompanyId(userCompanyDTO.getCompanyID());
            ServerSecurityContext.getInstance().setDatabase(userCompanyDTO.getClusterDbName());
            EdsCompany company = companyManager.get(userCompanyDTO.getCompanyID());

            if (!companyManager.schemaExists(userCompanyDTO.getCompanyID().toString())){
                continue;
            }
            EdsUser user = null;

            if (company != null) {
                userCompanyDTO.setActive(company.getActive());
                try {
                    user = userManager.get(userCompanyDTO.getUserID());
                } catch (Exception e) {
                    //schema dosnt exists
                }
            }
            if (company != null && user != null && !user.getDeleted() && EMPLOYEE_STATUS_ACTIVE.equals(userManager.getUserStatus(user.getObjectID()))) {
                if (Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) { // is survey respondent
                    continue;
                }
                if (user instanceof EdsClientContact && (user.getClientContact().getAccess() == null || (user.getClientContact().getAccess() != null && !user.getClientContact().getAccess()))) {
                    log.debug("Client contact access is false for user {}", user.getObjectID());
                    continue;

                }
                userCompanyDTO.setClusterURL(ServerUtils.getWebURL(userCompanyDTO));
                userCompanyDTO.setCompanyName(company.getName());
                String logo = null; /*loginServiceLocal.getCompanyLogoURL(company);*/
                logo = "/no-logo.gif";
                userCompanyDTO.setLogo(logo);
                userCompanyDTO.setFullName(user.getFullName());
                EdsUsagePlan usagePlan = usagePlanManager.getCurrentUsagePlan(company);
                if (usagePlan != null && usagePlan.getPaid()) {
                    userCompanyDTO.setStatusName(commonLocalizer.localize("active"));
                    userCompanyDTO.setStatus("active");
                } else {
                    userCompanyDTO.setStatus("free");
                    userCompanyDTO.setStatusName(commonLocalizer.localize("freeTrialButton"));
                }

                UsagePlanItem usagePlanItem = getUsagePlanItem(usagePlan, company.getObjectID());
                if (!(usagePlanItem.isCurrSub() && (usagePlanItem.isFree() || usagePlanItem.isPaid()))) {
                    userCompanyDTO.setStatus("expired");
                    userCompanyDTO.setStatusName(commonLocalizer.localize("expired"));
                }
                EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(userCompanyDTO.getCompanyID());
                if (companySystemSettings != null) {
                    userCompanyDTO.setPasswordExpirationDayCount(companySystemSettings.getPasswordExpirationDayCount());
                }
                if (company.isDeleted() == null || !company.isDeleted()) {
                    companyDTOList.add(userCompanyDTO);
                }
            }
        }
        return companyDTOList;
    }

    private UsagePlanItem getUsagePlanItem(EdsUsagePlan usagePlan, Integer companyId) {
        UsagePlanItem result = new UsagePlanItem();
        if (usagePlan != null) {
            UsagePlanItem item = myAccountServiceLocal.getParametr(usagePlan);
            result.setFree(item.isFree());
            result.setPaid(usagePlan.getPaid());
            result.setCurrSub(true);
        } else {
//            EdsUsagePlan lastUsagePlan = usagePlanManager.getLastUsagePlan(companyId);
            result.setCurrSub(false);
            result.setPaid(false);
            result.setFree(true);
        }
        return result;
    }

    /**
     * Register cookie for user login and password
     *
     * @param response - response
     * @param login    - user login
     * @param password - user password
     */
    private void setUserLoginAndPasswordToCookie(HttpServletResponse response, String login, String password) {
        Cookie userLogin = new Cookie(USER_NAME_COOKIE, EncryptionHelper.encodeURL(EncryptionHelper.encrypt(login)));
        userLogin.setMaxAge(Integer.MAX_VALUE);
        userLogin.setPath("/");
//        Cookie userPassword = new Cookie(USER_PASSWORD_COOKIE, EncryptionHelper.encodeURL(EncryptionHelper.encrypt(password)));
//        userPassword.setPath("/");
//        userPassword.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(userLogin);
//        response.addCookie(userPassword);
    }

    private AuthDetails fillAuthDetails(HttpServletRequest request, String userAgent, UserCompanyDTO userDetails) {
        AuthDetails authDetails = new AuthDetails();
        authDetails.setCompanyID(userDetails.getCompanyID());
        authDetails.setDatabase(userDetails.getClusterDbName());
        authDetails.setUserID(userDetails.getUserID());
        authDetails.setUserAgent(userAgent);
        authDetails.setIpAddress(ServerUtils.obtainClientIP(request));
        return authDetails;
    }

    public ModelAndView forwardToCompanyChooseForm(String loginType, String accountType, List<UserCompanyDTO> companyList, HttpServletRequest request) {
        ModelAndView chooseCompanyView = new ModelAndView("chooseCompany");
        String bestSigninPage = request.getParameter("BEST_SIGNIN");
        if (null != bestSigninPage && bestSigninPage.equals("BEST_SIGNIN")) {
            chooseCompanyView = new ModelAndView("iframeChooseCompany");
        }

        String redirectUri = null;
        String requestRedirectIri = request.getParameter(REDIRECT_URI);
        if (StringUtils.isNotBlank(requestRedirectIri)) {
            log.info("Document redirect uri: {}", requestRedirectIri);
            if (requestRedirectIri.toLowerCase().contains(".kpi.com")) {
                redirectUri = requestRedirectIri;
            }
        }
        EdsHostBasedSetting hostSetting = EdsContextParams.getHostSetting(request.getServerName());
        chooseCompanyView.addObject(LOGIN_TYPE, loginType);
        chooseCompanyView.addObject("logoUrl", hostSetting.getLogoImage());
        chooseCompanyView.addObject(ACCOUNT_TYPE, accountType);

        //Sort company list on redirection to chooseCompanyPage
        Map<String, Integer> statusRankMap = new HashMap<>();
        statusRankMap.put("active", 1);
        statusRankMap.put("free", 2);
        statusRankMap.put("expired", 3);
        companyList.sort((o1, o2) -> {
            if (!statusRankMap.containsKey(o1.getStatus()) || !statusRankMap.containsKey(o2.getStatus())) {
                return 0;
            }
            return statusRankMap.get(o1.getStatus()).compareTo(statusRankMap.get(o2.getStatus()));
        });

        chooseCompanyView.addObject("companyList", companyList);
        chooseCompanyView.addObject("fullName", companyList.get(0).getFullName());
        chooseCompanyView.addObject(IS_MULTI_COMPANY, "true");
        chooseCompanyView.addObject(REDIRECT_URI, redirectUri);

        return chooseCompanyView;
    }

    /**
     * Forward to change password.
     *
     * @param userCompanyDTO UserCompanyDTO
     * @param passwordPeriod Company passwords expiration day count.
     * @return ModelAndView
     */
    public ModelAndView forwardToChangeExpiredPasswordForm(UserCompanyDTO userCompanyDTO, Integer passwordPeriod) {
        ModelAndView changeExpiredPasswordView = new ModelAndView("changeExpiredPassword");
        changeExpiredPasswordView.addObject(COMPANY_ID, userCompanyDTO.getCompanyID());
        changeExpiredPasswordView.addObject("authid", EncryptionHelper.encrypt(String.valueOf(userCompanyDTO.getAuthId())));
        changeExpiredPasswordView.addObject("userName", userCompanyDTO.getUserName());
        changeExpiredPasswordView.addObject("fullName", userCompanyDTO.getFullName());
        changeExpiredPasswordView.addObject("userId", userCompanyDTO.getUserID());
        changeExpiredPasswordView.addObject("passwordPeriod", passwordPeriod);

        return changeExpiredPasswordView;
    }

    //Cookies can not be reached when the servlet is invoked by filter

    /**
     * @param signUpItem
     * @param request
     * @param response   @return ModelAndView
     * @Author: Aziz
     * This method takes user details provided by Open ID provider (Facebook, LiveID, Google, Yahoo etc.).
     * If the user already registered in the system, and has only one account, it signs in the user
     * If the user already registered in the system, and has more than one account, user is forwarded to company choose form
     * If the user is not registered, it sends the user to sign-up page  @param email
     */
    protected ModelAndView forwardToSignInOrSignUp(SignUpItem signUpItem, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<UserCompanyDTO> companyDTOList = new ArrayList<>();
        if (StringUtils.isNotBlank(signUpItem.getEmail())) {
            companyDTOList = filterUserCompanyDTOList(globalAuthJdbcSpringManager.getAuthInfoByUsername(request.getServerName(), signUpItem.getEmail()));
        }
        if (companyDTOList.isEmpty() && StringUtils.isNotBlank(signUpItem.getSocialNetworkId())) {
            companyDTOList = filterUserCompanyDTOList(globalAuthJdbcSpringManager.getAuthInfoByUsername(request.getServerName(), signUpItem.getSocialNetworkId()));
        }


        //## CROSS LOGIN
        if (!companyDTOList.isEmpty()) {
            UserCompanyDTO user = companyDTOList.get(0);

            if (UITypeEnum.CLASSIC_KPI.getCode().equals(user.getUitype()) && StringUtils.isNotEmpty(globalAuthJdbcSpringManager.getClassicUIHost(user.getCompanyID()))) {
                AuthInfoItem authInfoItem = new AuthInfoItem();
                authInfoItem.buildForFederatedLogin(signUpItem.getEmail(), signUpItem.getSocialNetworkId());

                ServerSecurityContext.getInstance().setCompanyId(user.getCompanyID());
                SwitchLayoutHandler switchLayoutHandler = (SwitchLayoutHandler) ApplicationContextProvider.applicationContext.getBean("switchLayoutHandler");

                boolean switched = switchLayoutHandler.crossLogin(request, response, authInfoItem);

                if (switched) {
                    return null;
                }
            }
        }

        ServerUtils.fillHostParameters(request);
        if (companyDTOList.size() == 1) {//user has only one company
            return signInUserToCompany(companyDTOList.get(0), response, request, new AuthInfoItem().buildForFederatedLogin(signUpItem.getEmail(), signUpItem.getSocialNetworkId()));
        } else if (companyDTOList.size() > 1) {//user has more than one company
            generateTempAuthTokenForMultiCompany(new AuthInfoItem().buildForFederatedLogin(signUpItem.getEmail(), signUpItem.getSocialNetworkId()).setMultiCompany(true), response);
            return forwardToCompanyChooseForm(FROM_FEDERATED_LOGIN, null, companyDTOList, request);
        } else {//user is not registered
            NewCompany company = new NewCompany();
            company.setCompanySignedUpFrom(SIGNED_UP_FROM_OPENID);
            company.setRegistrationType(signUpItem.getRegistrationType());
            company.setSocialUserName(signUpItem.getSocialNetworkId());
            company.setAdminEmail(signUpItem.getEmail());
            company.setAdminFName(signUpItem.getFirstName());
            company.setAdminLName(signUpItem.getLastName());
            company.setGoogleAccessToken(signUpItem.getAccessToken());

            try {
                if (StringUtils.isNotBlank(signUpItem.getFirstName())) {
                    company.setName(signUpItem.getFirstName() + " " + signUpItem.getLastName() + " Company");
                } else {
                    company.setName(signUpItem.getEmail() + " Company");
                }
            } catch (Exception e) {
                e.printStackTrace();
                company.setName("My Company");
            }
            redirectToCompanyCreation(company, request, response);
            return null;
        }
    }

    private void clearCookie(HttpServletResponse response) {
        ServerSecurityContext.getInstance().setSessionId(null);
        ServerUtils.removeCookie("JSESSIONID", response);
        ServerUtils.removeCookie(USER_PASSWORD_COOKIE, response);
        ServerUtils.removeCookie(SESSION_ID_COOKIE, response);
        ServerUtils.removeCookie(LAST_REQUEST_TIME, response);
        ServerUtils.removeCookie(HASH_LINK_COOKIE, response);
        ServerUtils.removeCookie(USER_AVAILABILITY, response);
        ServerUtils.removeCookie(USER_FULLNAME, response);
        ServerUtils.removeCookie(WEBAUTHTOKEN, response);
        ServerUtils.removeCookie(FROM_MARKETPLACE, response);
    }

    //Helper

    private void flushCookie(String cookieName, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private String parseURLResponse(URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        String res = null;
        while ((inputLine = in.readLine()) != null) {
            res = inputLine;
        }
        in.close();
        return res;

    }

    JsonObject parseJsonResponse(URL url) throws JsonSyntaxException, IOException {
        return (JsonObject) new JsonParser().parse(parseURLResponse(url));
    }

    protected void generateTempAuthTokenForMultiCompany(AuthInfoItem authInfoItem, HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        RedisClient.setKey(token, authInfoItem, authInfoItem.getClass(), 60 * 60 * 24/*Lifetime is one day*/);

        Cookie cookie = new Cookie(AUTH_TOKEN, token);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    protected void registerAuthInfoToCache(String sessionId, AuthInfoItem authInfo) {

        if (authInfo != null) {
            RedisClient.setKey(sessionId, authInfo, authInfo.getClass(), 60 * 60 * 24 * 7/*Lifetime is one week*/);
        }
    }
}
