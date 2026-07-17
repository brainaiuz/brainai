package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.utils.EdsContextParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: muratov
 * Date: Feb 17, 2010
 * Time: 6:01:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class PricingController implements Constants {

    @RequestMapping(value = "/Pricing.html", method = RequestMethod.GET)
    public ModelAndView pricing(HttpServletRequest request) throws Exception {
        ModelAndView model = new ModelAndView("pricing");
        // check IP for country code
        boolean isUKClient = false;
        ServerUtils.fillHostParameters(request);

        String countryCode = ServerUtils.getClientCountryCodeByIP(request);
        if (countryCode != null && countryCode.equals(UK)) {
            isUKClient = true;
        }
        model.addObject("freeTrialDays", EdsContextParams.getFreeTrialDays(request.getServerName()));
        model.addObject("currencyCODE", EdsContextParams.getCurrencyCODE(request.getServerName()));
        model.addObject("isukclient", isUKClient);
        model.addObject("productname", request.getAttribute("productName"));
        model.addObject("defaultLocale", request.getAttribute("defaultLocale"));
        return model;
    }
}
