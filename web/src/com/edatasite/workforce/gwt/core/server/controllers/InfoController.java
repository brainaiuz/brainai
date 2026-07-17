package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: muratov
 * Date: Feb 17, 2010
 * Time: 6:01:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class InfoController implements Constants {

    @RequestMapping(value = "/info.html", method = RequestMethod.GET)
    public ModelAndView info(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView model = new ModelAndView("info");
        // check IP for country code
        ServerUtils.fillHostParameters(request);
        ServerUtils.removeCookie("gtalk_account_login", response);
        ServerUtils.removeCookie("gtalk_account_password", response);
        return model;
    }
}
