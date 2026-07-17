package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Hasan Xo'janazarov
 * Date: 04.04.13
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class ZopimCantroller {

    @RequestMapping(value = "/LiveChat.html", method = RequestMethod.GET)
    public ModelAndView zopim(HttpServletRequest request) throws Exception {
        ModelAndView model = new ModelAndView("zopim");
        ServerUtils.fillHostParameters(request);


        return model;
    }
}
