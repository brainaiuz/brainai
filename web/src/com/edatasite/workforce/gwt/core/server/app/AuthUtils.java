package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.controllers.login.BaseLoginController;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 2/12/11
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthUtils implements Constants {

    private static final Logger log = LoggerFactory.getLogger(AuthUtils.class);

    public static final int COOKIE_AGE = 60 * 60 * 24 * 365;

    public static AuthDetails parseRequest(HttpServletRequest request, HttpServletResponse response) throws InvalidAuthException {
        String cIDEnc = request.getParameter(C_ID);
        String uIDEnc = request.getParameter(U_ID);
        String dIDEnc = request.getParameter(D_ID);
        if (StringUtils.isEmpty(cIDEnc) || StringUtils.isEmpty(uIDEnc)) {
            return null;
        }
        String companyID;
        String userId;
        String database;
        String sessionID;

        companyID = EncryptionHelper.decrypt(request.getParameter(C_ID));
        userId = EncryptionHelper.decrypt(request.getParameter(U_ID));

        if (StringUtils.isEmpty(dIDEnc)) {
            GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager = ApplicationContextProvider.applicationContext.getBean(GlobalAuthJdbcSpringManager.class);
            database = globalAuthJdbcSpringManager.getUserDatabaseName(Integer.valueOf(userId), Integer.valueOf(companyID));
        } else {
            database = EncryptionHelper.decodeURL(request.getParameter(D_ID));
        }
        if (StringUtils.isEmpty(database)) {
            return null;
        }

        sessionID = request.getParameter(SESSION_ID);

        if (StringUtils.isEmpty(companyID) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(database)) {
            throw new RuntimeException("Probable hack attempt");
        }

        AuthDetails authDetails;

        if (StringUtils.isNotEmpty(companyID) && StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(database)) {
            authDetails = new AuthDetails(Integer.parseInt(companyID), Integer.parseInt(userId), database);
            authDetails.setSuperUser(SUPER_USER.equals(request.getParameter(ACCOUNT_TYPE)));
            if (sessionID == null) {
                SessionService sessionService = ApplicationContextProvider.applicationContext.getBean(SessionService.class);
                SecurityContext.getInstance().setDatabase(database);
                SecurityContext.getInstance().setCompanyId(companyID);
                try {
                    sessionID = sessionService.obtainSessionAndRegisterInSystem(request, response, authDetails);
                    Cookie cookie = new Cookie(SESSION_ID_COOKIE, sessionID);
                    if (request.getAttribute("hostName") != null && request.getAttribute("hostName").toString().contains("uzgtl.com")) {
                        cookie.setDomain(".uzgtl.com");
                    }
                    response.addCookie(cookie);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (StringUtils.isNotEmpty(sessionID)) {
            authDetails = new AuthDetails(sessionID);
        } else {
            throw new InvalidAuthException("Can not authenticate");
        }
        String userAgent = request.getHeader("user-agent");
        authDetails.setUserAgent(userAgent != null ? userAgent : UNDEFINED_USER_AGENT);
        authDetails.setIpAddress(ServerUtils.obtainClientIP(request));
        authDetails.setSessionID(sessionID);
        return authDetails;
    }

    public static AuthDetails parseShadowRequest(HttpServletRequest request, Integer userId) throws InvalidAuthException {
        String cIDEnc = request.getParameter(C_ID);
        if (StringUtils.isEmpty(cIDEnc)) {
            return null;
        }

        String companyID = EncryptionHelper.decrypt(request.getParameter(C_ID));
        if (StringUtils.isEmpty(companyID)) {
            return null;
        }
        GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager = ApplicationContextProvider.applicationContext.getBean(GlobalAuthJdbcSpringManager.class);
        String database = globalAuthJdbcSpringManager.getUserDatabaseName(userId, Integer.valueOf(companyID));

        return new AuthDetails(Integer.parseInt(companyID), null, database);
    }

    public static ModelAndView fillCookieValuesAndRedirectToTheSystem(HttpServletRequest request, HttpServletResponse response,
                                                                      String sessionID, String url) throws IOException {
        boolean rememberMe = ServletRequestUtils.getBooleanParameter(request, REMEMBER_ME_PARAMETER, false);

        Cookie sessionCookie = new Cookie(SESSION_ID_COOKIE, sessionID);
        //We are making SESSION_ID cookie visible for all multisubdomains
        /*if(!"localhost".equalsIgnoreCase(request.getServerName())) {
            sessionCookie.setDomain("." + request.getServerName());
        }*/
        sessionCookie.setPath("/");
        if (request.getAttribute("hostName") != null && request.getAttribute("hostName").toString().contains("uzgtl.com")) {
            sessionCookie.setDomain(".uzgtl.com");
        }
        response.addCookie(sessionCookie);
        ServerUtils.removeCookie(TG_ID, response);
        ServerUtils.removeCookie(TG_CHAT_NAME, response);

        if (rememberMe) {
            response.addCookie(createCookie(REMEMBER_ME_PARAMETER, Boolean.TRUE.toString()));
            response.addCookie(createCookie(USER_NAME_COOKIE, ServletRequestUtils.getStringParameter(request, USER_NAME_COOKIE, "NO_USER_NAME")));
//            response.addCookie(createCookie(USER_PASSWORD_COOKIE, ServletRequestUtils.getStringParameter(request, USER_PASSWORD_COOKIE, "NO_PASSWORD")));
        }
        boolean isMultiCompany = ServletRequestUtils.getBooleanParameter(request, IS_MULTI_COMPANY, false);
        if (isMultiCompany) {
            response.addCookie(createCookie(IS_MULTI_COMPANY, ServletRequestUtils.getStringParameter(request, IS_MULTI_COMPANY, "false")));
        }

        if (StringUtils.isEmpty(url) || "null".equals(url)) {
            url = DEFAULT_SECTION + ".html";
        }
        url = !url.startsWith("/") ? "/".concat(url) : url;
        ModelAndView modelAndView = new ModelAndView("redirect:" + (StringUtils.isNotBlank(request.getContextPath()) ? request.getContextPath() : "") + URLDecoder.decode(url, StandardCharsets.UTF_8));
        String adminEmail = request.getParameter("adminEmail");
        if (adminEmail != null) {
            modelAndView.addObject("adminEmail", adminEmail);
        }
        return modelAndView;
    }

    private static Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(COOKIE_AGE);
        cookie.setPath("/");
        return cookie;
    }

    public static boolean marketplaceShowSection(HttpServletRequest request, String section) {
        Map<String, String> cookiesMap = BaseLoginController.getCookies(request);

        String fromMarketplace = cookiesMap.get(FROM_MARKETPLACE);
        if (fromMarketplace == null || "".equals(fromMarketplace) || !Boolean.valueOf(fromMarketplace)) {
            return true;
        }
        String marketplaceSection = cookiesMap.get(Constants.SECTION_HTML);
        if (!"".equals(marketplaceSection)) {
            if (!marketplaceSection.contains(section + ".html")) {
                return false;
            }
        }
//        System.out.println(marketplaceSection);
        return true;
    }
}
