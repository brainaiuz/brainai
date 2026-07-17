package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: Lochin
 * Date: 20-Nov-2010
 * Time: 18:55:16
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class PricingFrontEndController implements Constants {

    @RequestMapping(value = "/PricingFrontEnd.html", method = RequestMethod.GET)
    public ModelAndView handleRequestInternal(HttpServletRequest request) throws Exception {
        ModelAndView model = new ModelAndView("pricingFrontEnd");
        ServerUtils.fillHostParameters(request);
        // check IP for country code
        boolean isUKClient = false;

        String countryCode = ServerUtils.getClientCountryCodeByIP(request);
        if (countryCode != null && countryCode.equals(UK)) {
            isUKClient = true;
        }

        model.addObject("isukclient", isUKClient);
        return model;
    }

    @RequestMapping(value = "/WebForms.html", method = RequestMethod.GET)
    public ModelAndView webWorms(HttpServletRequest request) throws Exception {
        ServerUtils.fillHostParameters(request);
        return new ModelAndView("webForms");
    }

    @RequestMapping(value = "/BugReport.html", method = RequestMethod.GET)
    protected ModelAndView bugReport(HttpServletRequest request) throws Exception {
        ServerUtils.fillHostParameters(request);

        return new ModelAndView("bugReport");
    }

    @RequestMapping(value = "/printPage")
    public ModelAndView generateNewPage(HttpServletRequest req) {
        ModelAndView model = new ModelAndView("printable");
        model.addObject("html", req.getParameter("html"));
        return model;

    }
}
