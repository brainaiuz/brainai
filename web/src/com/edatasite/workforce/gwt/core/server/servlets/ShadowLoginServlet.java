package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.gwt.core.client.Exceptions.IncorrectPasswordException;
import com.edatasite.workforce.gwt.core.client.Exceptions.UserNotFoundException;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.rpc.ShadowAccount;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.web.servlet.FrameworkServlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShadowLoginServlet extends FrameworkServlet implements Constants {

    protected void doService(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        String companyId = EncryptionHelper.decrypt(request.getParameter("id"));
        if (companyId != null) {
            Integer id = Integer.parseInt(companyId);
            ServerSecurityContext.getInstance().setCompanyId(id);
            LoginServiceLocal loginService = (LoginServiceLocal) getWebApplicationContext().getBean("loginService");
            ShadowAccount employee = loginService.getShadowAccount(id);
            if (employee != null) {
                try {
                    try {
                        String sessionId = loginService.loginShadow(employee.getLogin(), id);
                        Cookie sessionCookie = new Cookie(SESSION_ID_COOKIE, sessionId);
                        //We are making SESSION_ID cookie visible for all multisubdomains
                        /*if(!"localhost".equalsIgnoreCase(request.getServerName())) {
                            sessionCookie.setDomain("." + request.getServerName());
                        }*/
                        response.addCookie(sessionCookie);
                        response.sendRedirect(request.getContextPath() + "/Workspace.html");
                        ServerSecurityContext.getInstance().setCompanyId(id);
                    } catch (UserNotFoundException exc) {
                        ServerSecurityContext.getInstance().setCompanyId(id);
                        response.sendRedirect(request.getContextPath() + "/NoUserFound.html");
                    } catch (IncorrectPasswordException exc) {
                        ServerSecurityContext.getInstance().setCompanyId(id);
                        response.sendRedirect(request.getContextPath() + "/IncorrectPassword.html");
                    } catch (IOException t) {
                        ServerSecurityContext.getInstance().setCompanyId(id);
                        response.sendRedirect(request.getContextPath() + "/Error.html");
                    }
                } catch (Exception ex) {
                    ServerSecurityContext.getInstance().setCompanyId(id);
                    response.sendRedirect(request.getContextPath() + "/BadUrlError.html");
                }

            } else {
                response.sendRedirect(request.getContextPath() + "/NoUserFound.html");
                ServerSecurityContext.getInstance().setCompanyId(id);
            }
        }
    }
}