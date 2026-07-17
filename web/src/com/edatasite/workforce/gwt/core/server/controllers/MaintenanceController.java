package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Aziz
 * Date: 24/07/13
 * Time: 17:36
 */
@Controller
public class MaintenanceController {
    @RequestMapping(value = "/companymaintenance")
    public ModelAndView handleRequest(HttpServletRequest request) {
        ServerUtils.fillHostParameters(request);
        return new ModelAndView("companymaintenance");
    }
}
