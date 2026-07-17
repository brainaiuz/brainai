package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 11.12.2009
 * Time: 11:47:12
 * To change this template use File | Settings | File Templates.
 */
@Transactional
@Controller
public class GoogleAppsController implements Constants {


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @RequestMapping(value = "/enterGoogleDomain.html")
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerUtils.fillHostParameters(request);

        return new ModelAndView("enterGoogleDomain");
    }
}