package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: jamshid.asatillayev
 * Date: Jan 28, 2011
 * Time: 6:01:28 AM
 */
@Controller
public class NotFoundViewController {
    @RequestMapping(value = "/notFound", method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        ServerUtils.fillHostParameters(httpServletRequest);
        if (Utils.isWikiVipworkspace(httpServletRequest)) {
            return new ModelAndView("wikiWipWorcspace404");
        }
        if (Utils.isFromGenesisGift(httpServletRequest)) {
            return new ModelAndView("genesisGift404");
        }
        return new ModelAndView("pagenotfound");
    }

    @RequestMapping(value = "/badRequestErrorPage", method = RequestMethod.GET)
    public ModelAndView badRequestError(HttpServletRequest request) throws Exception {
        ServerUtils.fillHostParameters(request);
        return new ModelAndView("badRequestError");
    }
}
