package com.edatasite.workforce.gwt.core.server.controllers.hmrc;

import com.edatasite.workforce.gwt.core.server.app.hmrc.dto.HmrcUserCredentialsDTO;
import com.edatasite.workforce.gwt.core.server.app.hmrc.service.HmrcAuthService;
import com.edatasite.workforce.gwt.core.server.app.hmrc.service.HmrcUserCredentialsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/hmrc/auth")
public class HMRCAuthorizationController {

    private static Logger log = LoggerFactory.getLogger(HMRCAuthorizationController.class);

    @Autowired
    private HmrcAuthService hmrcAuthService;

    @Autowired
    private HmrcUserCredentialsService hmrcUserCredentialsService;

    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public ModelAndView sendRequest(HttpServletResponse response) throws Exception {

        final String redirectUrl = hmrcAuthService.generateAuthorizationRequestURL();

        if (StringUtils.isBlank(redirectUrl)) {
            return new ModelAndView("redirect:/?error=Incorrect Params, please try again!");
        }
        response.sendRedirect(redirectUrl);
        return null;
    }

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public ModelAndView callback(HttpServletRequest request) {
        String error = request.getParameter("error");
        String errorDescription = request.getParameter("error_description");
        String code = request.getParameter("code");

        ModelAndView modelAndView = new ModelAndView("hmrcResponse");
        if (StringUtils.isNotBlank(error)) {
            modelAndView.addObject("response", errorDescription);
        }
        if (StringUtils.isNotBlank(code)) {
            modelAndView.addObject("response", "Success");
            HmrcUserCredentialsDTO credentialsDTO = hmrcAuthService.exchangeToken(code, false);
            hmrcUserCredentialsService.saveCredentials(credentialsDTO);
            hmrcUserCredentialsService.updateFinancialSettingsAuthorized();
        }
        return modelAndView;
    }
}
