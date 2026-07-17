package com.edatasite.workforce.gwt.core.server.controllers.login;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 31.01.2018
 * Time: 14:08:12
 */
@Controller
public class CookieDevCustomController extends BaseLoginController implements Constants {

    public CookieDevCustomController() {
    }

    @RequestMapping(value = "/mobile/{app_name}/redirect", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView handleRequest(@PathVariable("app_name") String app_name,
                                      @RequestParam("code") String code,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView dynamicRedirectForCookieDev = new ModelAndView("dynamic_redirect");
        dynamicRedirectForCookieDev.addObject("redirectlink", app_name + "://parameters?code=" + code);
        return dynamicRedirectForCookieDev;
    }

}
