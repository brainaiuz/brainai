package com.edatasite.workforce.gwt.core.server.controllers.login;

import com.edatasite.workforce.gwt.core.client.Exceptions.UserNotFoundException;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.server.rpc.AuthInfoItem;
import com.edatasite.workforce.gwt.core.server.rpc.SignUpItem;
import com.edatasite.workforce.utils.redis.RedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * User: Abdulaziz
 * Date: Nov 24, 2010
 * Time: 2:28:49 PM
 */
@Controller
public class MainLoginController extends BaseLoginController {

    /**
     * Method is invoked from federated login services
     *
     * @param request
     * @param response
     * @param command
     * @return
     */
    @RequestMapping(value = "/federatedLogin")
    public ModelAndView federatedLogin(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        return super.federatedLogin(request, response, command);

    }

    /**
     * SignIns user to the system to given company
     *
     * @param response
     * @param request
     * @throws
     * @throws UserNotFoundException
     * @throws IOException
     */
    @RequestMapping(value = "/signInUserToCompany")
    public ModelAndView signInUserToCompany(UserCompanyDTO userDetails, HttpServletResponse response, HttpServletRequest request) throws IOException {
        return super.signInUserToCompany(userDetails, response, request, null);

    }

    /**
     * Basic login method, usually inviked by submitting sign in form from homepage
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/basicLogin", "/mainLogin", "/userLogin"})
    public ModelAndView basicLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return super.basicLogin(request, response);
    }

    @RequestMapping(value = "/crossLogin")
    public ModelAndView crossLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = request.getParameter("token");

        if (StringUtils.isNotBlank(token)) {
            AuthInfoItem authInfoItem = RedisClient.getKey(token, AuthInfoItem.class);

            if (authInfoItem == null) {
                return new ModelAndView("redirect:index.html");
            }
            if (FROM_BASIC_LOGIN.equals(authInfoItem.getAuthType())) {
                request.setAttribute(USER_NAME_PARAMETER, authInfoItem.getUsername());
                request.setAttribute(USER_PASSWORD_PARAMETER, authInfoItem.getPassword());
                return super.basicLogin(request, response);
            } else if (FROM_FEDERATED_LOGIN.equals(authInfoItem.getAuthType())) {
                return forwardToSignInOrSignUp(new SignUpItem(authInfoItem.getSocialNetworkId(), authInfoItem.getEmail(), null, null, null, null), request, response);
            }
        }
        return new ModelAndView("redirect:index.html");
    }


    public ModelAndView forwardToCompanyChooseForm(String loginType, String accountType, List<UserCompanyDTO> companyList, HttpServletRequest request) {
        return super.forwardToCompanyChooseForm(loginType, accountType, companyList, request);
    }

    /**
     * Forward to change password.
     *
     * @param userCompanyDTO UserCompanyDTO
     * @param passwordPeriod Company passwords expiration day count.
     * @return ModelAndView
     */
    public ModelAndView forwardToChangeExpiredPasswordForm(UserCompanyDTO userCompanyDTO, Integer passwordPeriod) {
        return super.forwardToChangeExpiredPasswordForm(userCompanyDTO, passwordPeriod);
    }

    /**
     * @param signUpItem
     * @param request
     * @param response    @return ModelAndView
     * @Author: Aziz
     * This method takes user details provided by Open ID provider (Facebook, LiveID, Google, Yahoo etc.).
     * If the user already registered in the system, and has only one account, it signs in the user
     * If the user already registered in the system, and has more than one account, user is forwarded to company choose form
     * If the user is not registered, it sends the user to sign-up page  @param email
     */
    protected ModelAndView forwardToSignInOrSignUp(SignUpItem signUpItem, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.forwardToSignInOrSignUp(signUpItem, request, response);
    }
}
