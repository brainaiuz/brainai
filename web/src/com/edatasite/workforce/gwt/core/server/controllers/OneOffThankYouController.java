package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 25.11.2008
 * Time: 14:35:20
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class OneOffThankYouController {

    @RequestMapping(value = "/thankyou.html")
    public ModelAndView handleRequestInternal(HttpServletRequest request) throws ServletRequestBindingException {
        String employeeName = ServletRequestUtils.getStringParameter(request, "employee");
        String initiatorName = ServletRequestUtils.getStringParameter(request, "initiator");
        ServerUtils.fillHostParameters(request);
        ModelAndView model = new ModelAndView("thankyou");
        model.addObject("employee", employeeName);
        model.addObject("initiator", initiatorName);
        return model;
    }
}
