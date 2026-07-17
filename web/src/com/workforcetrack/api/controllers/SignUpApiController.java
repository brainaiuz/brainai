package com.workforcetrack.api.controllers;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.signup.client.rpc.SignUpService;
import com.workforcetrack.api.aspects.CheckRequest;
import com.workforcetrack.api.base.APIConstants;
import com.workforcetrack.api.base.APISelectItemList;
import com.workforcetrack.api.exceptions.ApiExceptions;
import com.workforcetrack.api.exceptions.BaseApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 18.05.12
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/signup")
public class SignUpApiController {

    @Autowired
    private SignUpService signUpService;

    @RequestMapping(value = "/countries", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest(checkSession = false)
    @ResponseBody
    public Object getCountries() throws BaseApiException {
        try {
            SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
            SelectItem[] countries = signUpService.getCountries();
            return new APISelectItemList(countries);
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }


}
