package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.BeansException;
import org.springframework.web.servlet.FrameworkServlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 21-Feb-2011
 * Time: 23:06:44
 */
public class DirectAccessWebSaytServlet extends FrameworkServlet implements Constants {
    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String pageNameEncrypt = request.getParameter("pageName");
        String chatJIDEncrypt = request.getParameter("chatJID");
//        String userJIDEncrypt = request.getParameter("userJID");
        String companyIdEncrypt = request.getParameter(C_ID);
        String userIdEncrypt = request.getParameter(U_ID);

        String chatJID = chatJIDEncrypt != null ? EncryptionHelper.decrypt(chatJIDEncrypt) : null;
        String pageName = EncryptionHelper.decrypt(pageNameEncrypt);
        Integer companyId = Integer.valueOf(EncryptionHelper.decrypt(companyIdEncrypt));
        Integer userId = Integer.valueOf(EncryptionHelper.decrypt(userIdEncrypt));

        GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager = ApplicationContextProvider.applicationContext.getBean(GlobalAuthJdbcSpringManager.class);
        String databse = globalAuthJdbcSpringManager.getCompanyDatabaseName(companyId);
        AuthDetails authDetails = new AuthDetails();
        try {
            LoginService loginService = (LoginService) getWebApplicationContext().getBean("loginService");
            SessionService sessionService = (SessionService) getWebApplicationContext().getBean("sessionService");
            ServerSecurityContext.getInstance().setCompanyId(companyId);
            ServerSecurityContext.getInstance().setDatabase(databse);

            String userAgent = request.getHeader("user-agent");

            authDetails.setCompanyID(companyId);
            authDetails.setDatabase(databse);
            authDetails.setIpAddress(ServerUtils.obtainClientIP(request));
            authDetails.setUserAgent(userAgent != null ? userAgent : UNDEFINED_USER_AGENT);
            authDetails.setUserID(userId);
            if(ServerSecurityContext.getInstance().getSessionId() ==null){
                String sessionId = sessionService.obtainSession(authDetails);
                ServerSecurityContext.getInstance().setSessionId(sessionId);
                Cookie sessionCookie = new Cookie(SESSION_ID_COOKIE, sessionId);
                //We are making SESSION_ID cookie visible for all multisubdomains
                /*if(!"localhost".equalsIgnoreCase(request.getServerName())) {
                    sessionCookie.setDomain("." + request.getServerName());
                }*/
                response.addCookie(sessionCookie);
            }

            if (chatJID != null) {
                response.sendRedirect(request.getContextPath() + "/" + pageName + ".html?chatJID=" + chatJID);
            } else {
                response.sendRedirect(request.getContextPath() + "/" + pageName + ".html");
            }

        } catch (BeansException e) {
            e.printStackTrace();
        }
    }
}
