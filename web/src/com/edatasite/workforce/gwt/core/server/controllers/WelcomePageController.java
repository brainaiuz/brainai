package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 5/13/11
 * Time: 6:30 PM
 */
@Controller
public class WelcomePageController {

    @RequestMapping(value = "/welcomePage.html")
    public ModelAndView handleRequest(HttpServletRequest request) {
        ServerUtils.fillHostParameters(request);

		String hostName = request.getAttribute("hostName").toString();
		if (hostName!=null  && hostName.contains("tjilo.com")){

			return new ModelAndView("welcomeTjilo");

		} else if (hostName!=null  && hostName.contains("new.kpi.com")){

            return new ModelAndView("welcomeNewKpi");

        } else {

            ModelAndView welcomePage = new ModelAndView("welcomePage");
            request.getParameter("adminEmail");
            welcomePage.addObject("adminEmail", request.getParameter("adminEmail"));
            return welcomePage;

		}
    }
}
