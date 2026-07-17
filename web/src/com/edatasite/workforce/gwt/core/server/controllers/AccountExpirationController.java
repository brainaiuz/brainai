package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Mar 18, 2009
 * Time: 7:35:51 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class AccountExpirationController implements Constants {

    @Autowired
    @Qualifier("loginService")
    private LoginServiceLocal loginServiceLocal;

    @RequestMapping(value = "/accountExpiration.html")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Cookie[] cookies = request.getCookies();
        boolean active = true;
        String cookieSessionId = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(SESSION_ID_COOKIE)) {
                    cookieSessionId = cookie.getValue();
                    break;
                }
            }
        }
        ServerUtils.fillHostParameters(request);
        ModelAndView model = new ModelAndView("accountExpiration");
        ServerSecurityContext.getInstance().setDummySessionId(cookieSessionId);
        if (!"".equals(cookieSessionId)) {
            String[] admin = loginServiceLocal.getAdmin();
//            //ServerSecurityContext.getInstance().setSessionId(null);
            model.addObject("adminEmail", admin[0]);
            model.addObject("fullName", admin[1]);
            model.addObject("supportEmail", EdsContextParams.getSupportEmail());
        } else {

            if (request.getServerName().contains("localhost")) {
                response.sendRedirect("http://" + request.getServerName() + ":" + request.getServerPort());
            } else {
                response.sendRedirect("http://" + request.getServerName());
            }

            return new ModelAndView("redirect:index.html");
        }
        return model;
    }
}
