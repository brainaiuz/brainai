package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 */
@Controller
public class LinkAuthorizeController implements Constants {

    @Qualifier("loginService")
    @Autowired
    LoginService loginService;
    @Autowired
    @Qualifier("loginService")
    LoginServiceLocal loginServiceLocal;

    @Autowired
    GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    @Autowired
    SessionService sessionService;

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public ModelAndView linkRedirect(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(value = "link") String link,
                                     @RequestParam(value = "session") String session) {
//        ServerUtils.fillHostParameters(request);

        System.out.println("Enter link= " + link);
        System.out.println("Enter session= " + session);

        SecurityContext.getInstance().setSessionId(session);
        Cookie sessionCookie = new Cookie(SESSION_ID_COOKIE, session);
        //We are making SESSION_ID cookie visible for all multisubdomains
        /*if(!"localhost".equalsIgnoreCase(request.getServerName())) {
            sessionCookie.setDomain("." + request.getServerName());
        }*/
        response.addCookie(sessionCookie);
        if (request.getServerName().contains("localhost")) {
            try {
                response.sendRedirect("http://" + request.getServerName() + ":" + request.getServerPort() + "/" + link);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                response.sendRedirect("http://" + request.getServerName() + "/" + link);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            return new ModelAndView("redirect:index.html");
        }
}
