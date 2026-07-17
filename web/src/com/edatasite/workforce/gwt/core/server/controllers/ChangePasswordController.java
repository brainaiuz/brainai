package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.backend.client.rpc.AccountManagementListItem;
import com.edatasite.workforce.gwt.core.client.rpc.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.rpc.AuthInfoItem;
import com.edatasite.workforce.utils.redis.RedisClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 24.12.2008
 * Time: 17:16:52
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class ChangePasswordController implements Constants {

    private final LoginService loginService;
    private final GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    private final SessionService sessionService;

    private final String[] notallowed = {"*", "?", "/", "\\", "<", ">", "|"};

    public ChangePasswordController(@Qualifier("loginService") LoginService loginService,
                                    GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager,
                                    SessionService sessionService) {
        this.loginService = loginService;
        this.globalAuthJdbcSpringManager = globalAuthJdbcSpringManager;
        this.sessionService = sessionService;
    }

    @RequestMapping(value = "/changePassword.html")
    public ModelAndView changePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean checkUser = false;
        boolean adPassEnabled = false;
        if (request.getParameter("checkUser") != null) {
            checkUser = ServletRequestUtils.getBooleanParameter(request, "checkUser");
        }
        if (request.getParameter("adPassEnabled") != null) {
            adPassEnabled = ServletRequestUtils.getBooleanParameter(request, "adPassEnabled");
        }
        AccountItem account;
        String jspPageForchangePassword = "changePassword";
        if (request.getHeader("host").contains("vaival")) {
            jspPageForchangePassword = "vaivalChangePassword";
        } else if (request.getHeader("host").contains("victechweb")) {
            jspPageForchangePassword = "victechWebChangePassword";
        } else if (request.getHeader("host").contains("icomtech")) {
            jspPageForchangePassword = "icomtechChangePassword";
        } else if (request.getHeader("host").contains("kmrsi")) {
            jspPageForchangePassword = "kmrsiChangePassword";
        } else if (Utils.isNewKpi(request)) {
            jspPageForchangePassword = "newKpiChangePassword";
        }
        ModelAndView model = new ModelAndView(jspPageForchangePassword);
        String sessionId = ServerUtils.setUserSessionid(request);
        ServerUtils.fillHostParameters(request);
        if ((sessionId != null && !sessionId.isEmpty())) {
            account = loginService.getAccount();
            model.addObject("fullName", account.getUserFullName());
            model.addObject("compName", account.getCompanyName());
            model.addObject("roles", account.getRole());
            model.addObject("email", account.getLogin());
            if (!checkUser) {
                checkUser = true;
                model.addObject("checkUser", checkUser);
                return model;
            }
            model.addObject("checkUser", checkUser);
            int minLength = account.getAdvancedPassEnabled() ? 15 : 8;
            String errorMsg = "";
            String pwd;
            String cpwd;
            pwd = request.getParameter("password");
            cpwd = request.getParameter("cPassword");
            String strength = request.getParameter("strength");
            if (("".equals(pwd)) || ("".equals(cpwd))) {
                errorMsg = "Please enter password";
            } else if (pwd.equals(cpwd)) {
                if (!"WEAK".equals(strength)) {
                    boolean valid = true;
                    for (String aNotallowed : notallowed) {
                        if (pwd.contains(aNotallowed)) {
                            errorMsg = "Please do not use special characters such /\"*?!<>|";
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                        account.setPassword(pwd);
                        loginService.updateAccount(account);
                        //## Reset auth info for Redis
                        var authInfoItem = RedisClient.getKey(sessionId, AuthInfoItem.class);
                        if (authInfoItem != null) {
                            authInfoItem.setPassword(pwd);
                            RedisClient.setKey(sessionId, authInfoItem, AuthInfoItem.class);
                        }
                        return getData(request);
                    }
                } else {
                    errorMsg = "Password is weak or too short (Min. " + minLength + " characters and must contain at least one uppercase, " +
                            "lowercase letters, numbers, and special symbols such as $ % & ( @ # § = ')' , : ; - _ + ^ )";
                }
            } else if (!pwd.equals(cpwd)) {
                errorMsg = "Passwords do not match";
            }
            model.addObject("message", errorMsg);
            model.addObject("adPassEnabled", adPassEnabled);
            return model;
        } else {
            if (request.getServerName().contains("localhost")) {
                response.sendRedirect("http://" + request.getServerName() + ":" + request.getServerPort());
            } else {
                response.sendRedirect("http://" + request.getServerName());
            }
            return new ModelAndView("redirect:index.html");
        }
    }

    @RequestMapping(value = "/changeExpiredPassword.html")
    public ModelAndView changeExpiredPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView model = new ModelAndView("changeExpiredPassword");
        ServerUtils.fillHostParameters(request);

        String authId = ServletRequestUtils.getStringParameter(request, "authid");
        Integer passwordPeriod = ServletRequestUtils.getIntParameter(request, "passwordPeriod");
        String userName = ServletRequestUtils.getStringParameter(request, "userName");
        String fullName = ServletRequestUtils.getStringParameter(request, "fullName");
        String userId = ServletRequestUtils.getStringParameter(request, "userId");
        String companyId = ServletRequestUtils.getStringParameter(request, "companyId");

        model.addObject("userName", userName);
        model.addObject("fullName", fullName);
        model.addObject("authid", authId);
        model.addObject("passwordPeriod", passwordPeriod);

        String errorMsg = "";
        String pwd;
        String cpwd;
        pwd = request.getParameter("password");
        cpwd = request.getParameter("cPassword");
        String strength = request.getParameter("strength");
        if ("".equals(pwd) || "".equals(cpwd)) {
            errorMsg = "Please enter password";
        } else if (pwd.equals(cpwd)) {
            if (pwd.length() >= 8 && !"WEAK".equals(strength)) {
                boolean valid = true;
                for (String aNotallowed : notallowed) {
                    if (pwd.contains(aNotallowed)) {
                        errorMsg = "Please do not use special characters such /\"*?:<>|";
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    String authValue = EncryptionHelper.decrypt(authId);
                    Integer authIntValue = Integer.valueOf(authValue);
                    AccountManagementListItem item = new AccountManagementListItem();
                    item.setAuthID(authIntValue);
                    item.setPassword(cpwd);
                    item.setLogin(userName);
                    item.setName(fullName);
                    item.setUserId(Integer.valueOf(userId));
                    item.setCompanyID(Integer.valueOf(companyId));
                    item.setActorCompanyId(Integer.valueOf(companyId));
                    item.setActorUserId(Integer.valueOf(userId));
                    item.setActorUsername(fullName);
                    globalAuthJdbcSpringManager.chageAccountPasswordByAuthId(item, pwd);
                    //## Expire mobile sessions for the user whose password just changed
                    sessionService.expireMobileUserSessionsAcrossCompanies(item.getLogin());
                    return new ModelAndView("successPasswordChanged");
                }
            } else {
                errorMsg = "Password is weak or too short (Min. 8 characters)";
            }

        } else if (!pwd.equals(cpwd)) {
            errorMsg = "Passwords do not match";
        }
        model.addObject("message", errorMsg);
        return model;
    }

    private ModelAndView getData(HttpServletRequest request) throws IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (SECTION_HTML.equals(cookie.getName())) {
                    String url = cookie.getValue();
                    if (url != null) {
                        return new ModelAndView("redirect:/" + url);
                    }
                }
            }
        }
        return new ModelAndView("redirect:/" + DEFAULT_SECTION + ".html");
    }

}
