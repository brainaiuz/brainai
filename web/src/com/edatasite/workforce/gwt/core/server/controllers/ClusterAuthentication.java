package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.AuthUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.controllers.login.MainLoginController;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Accepts the encrypted token from AuthServer and creates the session based on it
 * <p/>
 * <p/>
 * User: Anvarbek
 * Date: 1/25/11
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class ClusterAuthentication implements Constants {

    private static final Logger log = LoggerFactory.getLogger(ClusterAuthentication.class);

    @Autowired
    private SessionService sessionService;
    @Autowired
    private TelegramChatService telegramChatService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;

    @RequestMapping(value = "/authentication")
    public ModelAndView authenticate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //parse request and prepare info to obtain session
        AuthDetails authDetails = AuthUtils.parseRequest(request, response);
        //Obtaining service reference
        //Obtaining session
        log.info("authentication Database=" + authDetails.getDatabase());
        ServerSecurityContext.getInstance().setSessionId(authDetails.getSessionID());
        String sessionID = sessionService.obtainSessionAndRegisterInSystem(request, response, authDetails);

        //## Temporary Auth info re-initialized with real session ID
        {
            String tempAuthToken = ServerUtils.getCookieVal(Constants.AUTH_TOKEN, request.getCookies());

            if (StringUtils.isNotBlank(tempAuthToken) && RedisClient.getKey(tempAuthToken) != null) {
                RedisClient.setKey(sessionID, RedisClient.getKey(tempAuthToken));
                RedisClient.removeKey(tempAuthToken);

                ServerUtils.removeCookie(Constants.AUTH_TOKEN, response);
            }
        }

        //Getting URL
        String url = request.getParameter(SECTION_HTML) != null ? request.getParameter(SECTION_HTML) :
                MainLoginController.getCookies(request).get(SECTION_HTML);
        //Filling with cookies and redirect
        if (url == null || "null".equals(url)) {
            EdsUserEmailSettings userSettings = sessionService.getUserSettings();
            String defaultSection = DEFAULT_SECTION + ".html";
            url = userSettings.getStartPage() != null ? userSettings.getStartPage() : defaultSection;
        }
        String tgid = ServerUtils.getCookieVal(TG_ID, request.getCookies());
        String tgChatName = ServerUtils.getCookieVal(TG_CHAT_NAME, request.getCookies());
        if (!StringUtils.isEmpty(tgid) && !StringUtils.isEmpty(tgChatName)) {
            String decryptChatId = EncryptionHelper.decryptURL(tgid);
            String decryptChatName = EncryptionHelper.decryptURL(tgChatName);
            if (!StringUtils.isEmpty(decryptChatId)) {
                Long chatId = null;
                try {
                    chatId = Long.valueOf(decryptChatId);
                } catch (NumberFormatException ignored) {
                }
                if (chatId != null) {
                    telegramChatService.createChat(chatId, decryptChatName);
                }
            }
        }

        String redirectUri = request.getParameter(REDIRECT_URI);
        if (StringUtils.isNotBlank(redirectUri)) {
            url = redirectUri;
        } else {
            EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(userManager.getUser());
            String defultHTML = "";
            if (ServerUtils.hasPermission(PermissionConstants.HRMS_MAIN_MENU)) {
                defultHTML = "Hrms.html";
            } else if (ServerUtils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                defultHTML = "ProjectManagement.html";
            } else if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
                defultHTML = "Accounting.html";
            } else if (ServerUtils.hasPermission(PermissionConstants.CRM_MAIN_MENU)) {
                defultHTML = "Crm.html";
            } else if (ServerUtils.hasPermission(PermissionConstants.PAYROLL_MAIN_MENU)) {
                defultHTML = "Payroll.html";
            } else if (ServerUtils.hasPermission(PermissionConstants.REPORTING_MAIN_MENU)) {
                defultHTML = "Reporting.html";
            } else {
                defultHTML = DEFAULT_SECTION + ".html";
            }
            url = userSettings != null && userSettings.getStartPage() != null ? userSettings.getStartPage() : defultHTML;
        }

        return AuthUtils.fillCookieValuesAndRedirectToTheSystem(request, response, sessionID, url);
    }


}
