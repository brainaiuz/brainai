package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Jonibek
 * Date: 26-May-2009
 * Time: 21:47:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class LogoutController implements Constants {

    @RequestMapping(value = "/logout.html", method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerSecurityContext.getInstance().expireServerSession();


        removeCookie(USER_NAME_COOKIE, response);
        removeCookie(USER_PASSWORD_COOKIE, response);
        removeCookie(SESSION_ID_COOKIE, response);
        removeCookie(SERVICE_ID_COOKIE, response);
        removeCookie(LAST_REQUEST_TIME, response);
        removeCookie(HASH_LINK_COOKIE, response);
        removeCookie(SECTION_HTML, response);
        removeCookie(USER_AVAILABILITY, response);
        removeCookie(USER_FULLNAME, response);
        removeCookie(WEBAUTHTOKEN, response);
        removeCookie(FROM_MARKETPLACE, response);

//        response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());
//        if (request.getServerName().contains("localhost")) {
//        } else {
//            response.sendRedirect("http://" + request.getServerName());
//        }

        return new ModelAndView("redirect:index.html");
    }

    private void removeCookie(String name, HttpServletResponse response) {
        ServerUtils.removeCookie(name, response);
    }
}
