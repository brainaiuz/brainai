package com.edatasite.workforce.gwt.core.server.controllers.signup;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.AuthUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CountryCallingCodeLayer;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Sep 12, 2009
 * Time: 4:46:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class FederatedSignUpController extends GeneralFreeSignUpController implements Constants {

    private static final Logger logger = LoggerFactory.getLogger(FederatedSignUpController.class);
    /**
     * Set up a custom property editor for converting Longs
     *
     * @param binder the default databinder
     */
    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        errors = binder.getBindingResult();
    }

    /**
     * shows sign up form
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/prepareSignUpForm")
    public ModelAndView prepareSignUpForm(HttpServletRequest request) {
        System.out.println("PREPARE SIGN UP FORM");
        SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        ServerUtils.fillHostParameters(request);
//        ModelAndView model = new ModelAndView("customSignUp");
        ModelAndView model = new ModelAndView("socialSignUp");
        List<EdsCountry> countrys = countryManager.list();
        NewCompany newCompany = null;
        try {
            newCompany = (NewCompany) request.getAttribute(BIND_OBJECT_NAME);
            if (newCompany != null) {
                model.addObject("registrationType", newCompany.getRegistrationType());
                model.addObject("socialUserName", newCompany.getSocialUserName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<CountryCallingCodeLayer.CountryCallCode> callCodes = CountryCallingCodeLayer.getCountryCallCodes();
        String countryCode = ServerUtils.getClientCountryCodeByIP(request);
        String countryCallCode = null;
        if ("".equals(countryCode) || "--".equals(countryCode)) {
            countryCode = US_CODE;
            countryCallCode = US_CALL_CODE;
        }
        if (countryCallCode == null) {
            String finalCountryCode = countryCode;
            CountryCallingCodeLayer.CountryCallCode country = callCodes.stream()
                    .filter(item -> finalCountryCode.equals(item.getCountryCode()))
                    .findAny().orElse(null);
            if (country != null) {
                countryCallCode = "+" + country.getCallCode();
            }
        }
        model.addObject("currentCountry", countryCode);
        model.addObject("currentCallCode", countryCallCode);

        model.addObject(BIND_OBJECT_NAME, newCompany);

        model.addObject("countryCallCodes", callCodes);
        model.addObject("countrys", countrys);
        model.addObject("locales", signupService.getSupportedLocales());
        return model;
    }

    /**
     * default request handler
     * invoke when request has no param
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/customSignUp.html", method = RequestMethod.GET)
    public ModelAndView showForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getHeader("referer") != null) {
            response.addCookie(new Cookie(REFERER, request.getHeader("referer")));
        }

        if (request.getParameter("aff") != null) {
            response.addCookie(new Cookie(AFFILIATE, request.getParameter("aff")));
        }
        if (request.getParameter("kcpn") != null) {
            response.addCookie(new Cookie(COMPAING, request.getParameter("kcpn")));
        }
        SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        String as = request.getPathInfo().substring(request.getPathInfo().indexOf("/") + 1, request.getPathInfo().indexOf("."));
        Integer usersSize = ServletRequestUtils.getIntParameter(request, "users");//free users count, default 4 users;
        ServerUtils.fillHostParameters(request);
        ModelAndView model = new ModelAndView(as);
        NewCompany newCompany = new NewCompany();
        newCompany.setActive(true);
        if (request.getParameter("iframe") != null) {
            newCompany.setAdminFName(request.getParameter("adminFName"));
            newCompany.setAdminLName(request.getParameter("adminLName"));
            newCompany.setAdminEmail(request.getParameter("adminEmail"));
            newCompany.setName(request.getParameter("name"));
            newCompany.setAdminActive(Boolean.valueOf(request.getParameter("adminActive")));
            newCompany.setSignedUpPage(request.getParameter("signedUpPage"));
            newCompany.setCountryID((request.getParameter("countryID") != null && !"".equals(request.getParameter("countryID"))) ? Integer.valueOf(request.getParameter("countryID")) : null);
            newCompany.setLocale(request.getParameter("locale"));
            newCompany.setStateID((request.getParameter("stateID") != null && !"".equals(request.getParameter("stateID"))) ? (Integer.valueOf(request.getParameter("stateID"))) : null);
            newCompany.setWorkArea((request.getParameter("workArea") != null && !"".equals(request.getParameter("workArea"))) ? Integer.valueOf(request.getParameter("workArea")) : null);
            newCompany.setPhone(request.getParameter("phone"));
            newCompany.setSetUp(Boolean.valueOf(request.getParameter("setUp")));
            newCompany.setCurrencyID(request.getParameter("currencyID") != null && !"".equals(request.getParameter("currencyID")) ? Integer.valueOf(request.getParameter("currencyID")) : null);
            newCompany.setClientSingUpIPAddress(request.getParameter("clientSingUpIPAddress"));
            newCompany.setAgreeWithCondition(Boolean.valueOf(request.getParameter("agreeWithCondition")));
            newCompany.setCountryName(request.getParameter("countryName"));
            newCompany.setGoogleAppsDomain(request.getParameter("googleAppsDomain"));
            newCompany.setCompanySignedUpFrom(request.getParameter("companySignedUpFrom"));
            newCompany.setFromFederatedLogin(Boolean.valueOf(request.getParameter("fromFederatedLogin")));
            newCompany.setValue1(request.getParameter("value1"));
            newCompany.setValue2(request.getParameter("value2"));
            newCompany.setValue3(request.getParameter("value3"));
            newCompany.setParentIframeUrl(request.getParameter("parentIframeUrl") != null && !"".equals(request.getParameter("parentIframeUrl")) ? request.getParameter("parentIframeUrl") : null);
        }
        model.addObject(BIND_OBJECT_NAME, newCompany);
        Integer freeTrialDays = (Integer) request.getAttribute("freeTrialDays");
        String currencyCODE;
        if (request.getAttribute("currencyCODE") != null) {
            currencyCODE = request.getAttribute("currencyCODE").toString();
            model.addObject("currencyCODE", currencyCODE);
        }
        List<EdsCountry> countrys = countryManager.list();
        model.addObject("countrys", countrys);

        List<CountryCallingCodeLayer.CountryCallCode> callCodes = CountryCallingCodeLayer.getCountryCallCodes();
        model.addObject("countryCallCodes", callCodes);

        model.addObject("users", usersSize != null ? usersSize : 4);
        model.addObject("freeTrialDays", freeTrialDays);
        model.addObject("locales", signupService.getSupportedLocales());
        Boolean isCaptchaEnabled = (Boolean) request.getAttribute("isCaptchaEnabled");
        if (isCaptchaEnabled) {
            model.addObject("captcha", true);
            model.addObject("captchaTheme", "clean");
        }
        return model;
    }


    /**
     * Method is invoked when form submitted
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/customSignUp.html", method = RequestMethod.POST)
    public ModelAndView signUp(HttpServletRequest request,
                               HttpServletResponse response,
                               final RedirectAttributes redirectAttributes,
                               NewCompany newCompany) throws Exception {
        System.out.println("FEDERATED SIGN UP");
        SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        ServerUtils.fillHostParameters(request);
        //Fill country code for validation if its not present in request
        if (newCompany != null && !ServerUtils.isNullOrEmpty(newCompany.getCallCode())) {
            final EdsCountry country = countryManager.getCountryByCallCode(newCompany.getCallCode());
            if (country != null) {
                newCompany.setCountryID(country.getObjectID());
                newCompany.setCountryCode(country.getCode());
            }
        }
        // validation
        if (validate(newCompany, request)) {
            Map modelMap = errors.getModel();
            List<EdsCountry> countrys = countryManager.list();
            modelMap.put("countrys", countrys);
            modelMap.put("locales", signupService.getSupportedLocales());
            List<CountryCallingCodeLayer.CountryCallCode> callCodes = CountryCallingCodeLayer.getCountryCallCodes();
            modelMap.put("countryCallCodes", callCodes);

            if(StringUtils.isNotBlank(request.getParameter("registrationType"))) {
                modelMap.put("registrationType", request.getParameter("registrationType"));
            }
            if(StringUtils.isNotBlank(request.getParameter("socialUserName"))) {
                modelMap.put("socialUserName", request.getParameter("socialUserName"));
            }
            return new ModelAndView("socialSignUp", modelMap);
        } else {

            //change some logic to the federated sign up
            newCompany.setFromFederatedLogin(true);
            //continue signup process
            ModelAndView mav = continueSignUp(request, response, newCompany);
            if (mav.getModel().get("authDetails") == null) {
                return mav;
            }
            redirectAttributes.addFlashAttribute("signedFromSocial", SIGNED_UP_FROM_OPENID.equals(newCompany.getCompanySignedUpFrom()));
            //for login after signing up with a NEW user-name
            String sessionID = sessionService.obtainSessionAndRegisterInSystem(request, response, (AuthDetails) mav.getModel().get("authDetails"));
            return AuthUtils.fillCookieValuesAndRedirectToTheSystem(request, response, sessionID, "/gettingStarted.html");
        }
    }

    /**
     * Method is invoked when form submitted
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/marketplaceSignUp")
    public ModelAndView marketplaceSignUp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        NewCompany newCompany = (NewCompany) request.getAttribute(BIND_OBJECT_NAME);
        //change some logic to the federated sign up
        newCompany.setFromFederatedLogin(true);
        newCompany.setCompanySignedUpFrom(SIGNED_UP_FROM_GOOGLE_MARKETPLACE);
        logger.info("MarketplaceSignUp:>>>" + newCompany.toString());
        ServerUtils.fillHostParameters(request);
        //continue signup process
        ModelAndView mav = continueSignUp(request, response, newCompany);
        if ("existingUser".equals(mav.getViewName())) {
            sendMarketplaceCompanyExistsNotification(newCompany);
            return mav;
        } else {
            String sessionID = sessionService.obtainSessionAndRegisterInSystem(request, response, (AuthDetails) mav.getModel().get("authDetails"));
            return AuthUtils.fillCookieValuesAndRedirectToTheSystem(request, response, sessionID, "/welcomePage.html");
        }


    }

    private void sendMarketplaceCompanyExistsNotification(NewCompany newCompany) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Dear Aziza, <br>");
        stringBuffer.append(newCompany.getAdminEmail() + " asked to bind his existing company with Google Marketlace account.");
        signupService.getParamsFromMarketPlace(stringBuffer, "Bind existing company with Google Marketplace");
    }

}
