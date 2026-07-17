package com.edatasite.workforce.gwt.core.server.controllers.login;

import com.edatasite.workforce.gwt.core.client.enums.RegistrationTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.Office365LoginService;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.rpc.SignUpItem;
import com.edatasite.workforce.gwt.core.server.rpc.office365.MeUserResponseTO;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25.02.2014
 * Time: 14:08:12
 */
@Controller
public class Office365LoginController extends BaseLoginController implements Constants {

    private static Logger log = LoggerFactory.getLogger(Office365LoginController.class);

    private final Office365LoginService office365LoginService;

    @Autowired
    public Office365LoginController(Office365LoginService office365LoginService) {
        this.office365LoginService = office365LoginService;
    }

    @RequestMapping(
            value = "/office365authorization",
            method = RequestMethod.GET
    )
    public ModelAndView sendRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String redirectUrl = office365LoginService.createLoginUrl(request, response);

        if (ServerUtils.isNullOrEmpty(redirectUrl)) {
            return new ModelAndView("redirect:/?error=Incorrect Params, please try again!");
        }
        response.sendRedirect(redirectUrl);
        return null;
    }

    @RequestMapping(
            value = "/office365Login",
            method = RequestMethod.GET
    )
    public ModelAndView handleResponse(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final MeUserResponseTO user = office365LoginService.getUser(request, response);

        if (user == null || ServerUtils.isNullOrEmpty(user.getUserPrincipalName())) {
            return new ModelAndView("redirect:/?error=Incorrect Params, please try again!");
        }
        return this.forwardToSignInOrSignUp(new SignUpItem(user.getId(), user.getUserPrincipalName(), user.getGivenName(), user.getSurname(), null, RegistrationTypeEnum.MICROSOFT),
                                            request,
                                            response);
    }
}
