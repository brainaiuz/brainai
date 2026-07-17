package com.edatasite.workforce.gwt.core.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Faxriddin
 * Date: 5/15/13
 */
@Controller
public class WebsiteUpdateController {

    @RequestMapping(value = "/websiteUpdate.html")
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("imageOrFileTransferResponse");
    }

}
