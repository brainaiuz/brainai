package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 14-Mar-2011
 * Time: 20:31:37
 */
@Controller
public class ErrorPageController {

    @RequestMapping(value = "/errorPage", method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest) throws Exception {
        ServerUtils.fillHostParameters(httpServletRequest);
        if (Utils.isFromGenesisGift(httpServletRequest)) {
            return new ModelAndView("genesisGift404");
        }
        return new ModelAndView("error");
    }
}
