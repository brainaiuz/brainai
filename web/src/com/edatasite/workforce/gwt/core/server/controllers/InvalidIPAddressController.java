package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: Eminem
 * Date: 24/09/12
 * Time: 17:36
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class InvalidIPAddressController {
    @RequestMapping(value = "/invalidIP.html")
    public ModelAndView handleRequest(HttpServletRequest request) {
        ServerUtils.fillHostParameters(request);
        return new ModelAndView("invalidIP");
    }
}
