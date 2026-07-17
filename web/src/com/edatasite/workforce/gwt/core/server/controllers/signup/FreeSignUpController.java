package com.edatasite.workforce.gwt.core.server.controllers.signup;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.AuthUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.enums.TemplateSchema;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CountryCallingCodeLayer;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 13.12.2008
 * Time: 12:14:48
 * To change this template use File | Settings | File Templates.
 */
@Controller
//@RequestMapping(value = "/showForm")
public class FreeSignUpController extends GeneralFreeSignUpController {

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        errors = binder.getBindingResult();
    }
    //Handler methods START

    /*@RequestMapping(value = {"/showForm", "/freeSignup.html", "/iframeSignup", "/webFormSignup.html", "/customTrialDay.html", "/websiteSignup.html"
            , "/bestSignup.html", "/websiteFreeTrial.html", "/marketingSignup.html", "/signupFromWebSite.html"
            , "/slidebarSignup.html", "/productTourSignup.html", "/freeTrial.html", "/freeSignup.html", "/existingUser.html", "/customSignUp.html"}, method = RequestMethod.GET)
    public ModelAndView showForm(HttpServletRequest request) throws Exception {
        String as = request.getPathInfo().substring(request.getPathInfo().indexOf("/") + 1, request.getPathInfo().indexOf("."));
        ModelAndView model = new ModelAndView(as);
        return model;
    }*/

    /**
     * default request handler
     * invoke when request has no param
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = {
            "/showForm",
            "/iframeSignup",
            "/freeTrial.html",
            "/freeSignup.html",
            "/homeSignUp.html",
            "/bestSignup.html",
            "/freeSignup.html",
            "/existingUser.html",
            "/webFormSignup.html",
            "/websiteSignup.html",
            "/customTrialDay.html",
            "/slidebarSignup.html",
            "/marketingSignup.html",
            "/websiteFreeTrial.html",
            "/sign-up-for-free.html",
            "/signupFromWebSite.html",
            "/productTourSignup.html",
            "/existingUserNewKpi.html",
            "/websiteMcloudSignup.html",
            "/websiteNewSignupRight.html",
            "/websiteMcloudSignupRight.html",
    },
            method = RequestMethod.GET)
    public ModelAndView showForm(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String viewName = request.getPathInfo().substring(request.getPathInfo().indexOf("/") + 1, request.getPathInfo().indexOf("."));

        //    Get User country code from ip address

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
            newCompany.setPromoCode(request.getParameter("promoCode") != null ? request.getParameter("promoCode") : null);
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
            if (!"value1".equals(request.getParameter("value1")))
                newCompany.setValue1(request.getParameter("value1"));
            if (!"value2".equals(request.getParameter("value1")))
                newCompany.setValue2(request.getParameter("value2"));
            if (!"value3".equals(request.getParameter("value1")))
                newCompany.setValue3(request.getParameter("value3"));
            newCompany.setParentIframeUrl(request.getParameter("parentIframeUrl") != null && !"".equals(request.getParameter("parentIframeUrl")) ? request.getParameter("parentIframeUrl") : null);
        }
        //model.addObject();
        model.addObject(BIND_OBJECT_NAME, newCompany);
        Integer freeTrialDays = (Integer) request.getAttribute("freeTrialDays");
        String currencyCODE;
        if (request.getAttribute("currencyCODE") != null) {
            currencyCODE = request.getAttribute("currencyCODE").toString();
            model.addObject("currencyCODE", currencyCODE);
        }

        List<EdsCountry> countrys = countryManager.list();
        List<EdsRegion> regions = regionManager.listBySaudiArabia();
        List<CountryCallingCodeLayer.CountryCallCode> callCodes = CountryCallingCodeLayer.getCountryCallCodes();
        model.addObject("countryCallCodes", callCodes);
        model.addObject("countrys", countrys);
        model.addObject("regions", regions);
        model.addObject("users", usersSize != null ? usersSize : 4);
        model.addObject("freeTrialDays", freeTrialDays);
        model.addObject("locales", signupService.getSupportedLocales());
        String manuallyDisableCaptcha = request.getParameter("disableCaptchaForFreeSignup");
        boolean captchaRequired = true;
        if ("1".equals(manuallyDisableCaptcha)) {
            captchaRequired = false;
            model.addObject("disableCaptchaForFreeSignup", "1");
        }
        Boolean isCaptchaEnabled = (Boolean) request.getAttribute("isCaptchaEnabled") && captchaRequired;
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
    @RequestMapping(value = {
            "/showForm",
            "/freeSignup.html",
            "/iframeSignup",
            "/webFormSignup.html",
            "/customTrialDay.html",
            "/websiteSignup.html",
            "/sign-up-for-free.html",
            "/websiteNewSignupRight.html",
            "/websiteMcloudSignup.html",
            "/websiteMcloudSignupRight.html",
            "/bestSignup.html", "/websiteFreeTrial.html", "/marketingSignup.html", "/signupFromWebSite.html"
            , "/slidebarSignup.html", "/productTourSignup.html", "/freeTrial.html", "/freeSignup.html", "/existingUser.html", "/existingUserNewKpi.html", "/homeSignUp.html"},
            method = RequestMethod.POST)
    public ModelAndView signUp(HttpServletRequest request, HttpServletResponse response, NewCompany newCompany) throws Exception {
        boolean fromWordpress = request.getParameter("fromWordpress") != null;
        boolean botTrapped = request.getParameter("rt") != null && request.getParameter("rt").contains("on");
        String formId = request.getParameter("formId");
        if (fromWordpress && botTrapped) {
            return null;
        }
        String viewName = request.getPathInfo().substring(request.getPathInfo().indexOf("/") + 1, request.getPathInfo().indexOf("."));
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0);

        //str = str.replaceAll("\\D+","");
        String callCode = request.getParameter("callCode");
        String countryCode = CountryCallingCodeLayer.getCountryCodeByCallCode(callCode);
        newCompany.setCallCode(callCode);
        newCompany.setName(DEFAULT_COMPANY_NAME);
        newCompany.setPromoCode(request.getParameter("promoCode") != null ? request.getParameter("promoCode") : null);
        EdsCountry country = null;
        if (countryCode != null && !"".equals(countryCode)) {
            country = countryManager.getCountryByCode(countryCode);
        }
        if (country != null) {
            newCompany.setCountryID(country.getObjectID());
        }
        newCompany.setCountryCode(countryCode);

        System.out.println("CountryCode: " + countryCode);
        newCompany.setRedirectToSettings(true);

        errors = new BeanPropertyBindingResult(newCompany, "newCompany");

        newCompany.setUtm_campaign(request.getParameter("utm_campaign"));
        newCompany.setUtm_source(request.getParameter("utm_source"));
        newCompany.setUtm_medium(request.getParameter("utm_medium"));
        newCompany.setUtm_keyword(request.getParameter("utm_keyword"));
        newCompany.setUtm_btn(request.getParameter("utm_btn"));
        newCompany.setUtm_content(request.getParameter("utm_content"));
        newCompany.setUtm_term(request.getParameter("utm_term"));
        newCompany.setGclid(request.getParameter("gclid"));
        if (request.getParameter("referrer") != null) {
            newCompany.setReferrer(StringUtils.newStringUtf8(Base64.decodeBase64(request.getParameter("referrer"))));
        }
        if (request.getParameter("redirected") != null) {
            newCompany.setRedirected(StringUtils.newStringUtf8(Base64.decodeBase64(request.getParameter("redirected"))));
        }

        System.out.println("campaign = " + newCompany.getUtm_campaign());
        System.out.println("keyword = " + newCompany.getUtm_keyword());
        System.out.println("redirected = " + newCompany.getRedirected());
        System.out.println("");
        System.out.println("email: " + newCompany.getAdminEmail());
        System.out.println("phone: " + newCompany.getPhone() + " " + request.getParameter("callCode"));

        String userCount = request.getParameter("users");
        ServerUtils.fillHostParameters(request);
        ModelAndView mav = new ModelAndView();
        boolean captchaRequired = true;
        String manuallyDisableCaptcha = request.getParameter("disableCaptchaForFreeSignup");
        if ("1".equals(manuallyDisableCaptcha)) {
            captchaRequired = false;
        }

        // Validation
        if (validate(newCompany, request)) {
            Boolean isCaptchaEnabled = (Boolean) request.getAttribute("isCaptchaEnabled") && captchaRequired;
            if (isCaptchaEnabled) {
                mav.addObject("captcha", true);
                mav.addObject("captchaTheme", "clean");
            }
            if (org.apache.commons.lang3.StringUtils.isNotBlank(request.getParameter("callCode"))) {
                mav.addObject("currentCountry", request.getParameter("callCode"));
            }
            mav = reinitialize(userCount, viewName, mav, request, response);
            return mav;
        } else {
            Boolean isCaptchaEnabled = (Boolean) request.getAttribute("isCaptchaEnabled") && captchaRequired;
            if (isCaptchaEnabled) {
                mav.addObject("captcha", true);
                mav.addObject("captchaTheme", "clean");
                if (!ServerUtils.validateCaptcha(request, response)) {
                    errors.addError(new ObjectError("captchaError", ""));
                    request.setAttribute("captchaError", "Wrong Captcha! Please try again");
                    return reinitialize(userCount, viewName, mav, request, response);
                }
            }
            mav = continueSignUp(request, response, newCompany);
            if (mav.getModel().get("authDetails") == null) {//simple hack
                return mav;
            }
            //for login after signing up with a NEW user-name
            String sessionID = sessionService.obtainSessionAndRegisterInSystem(request, response, (AuthDetails) mav.getModel().get("authDetails"));
            return AuthUtils.fillCookieValuesAndRedirectToTheSystem(request, response, sessionID, "/welcomePage.html");
//            return null; // the whole redirect process occurs in the method above with na
        }
    }

    private ModelAndView reinitialize(String userCount, String viewName, ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean fromWordpress = request.getParameter("fromWordpress") != null;
        String formId = request.getParameter("formId");
        Map modelMap = errors.getModel();
        List<EdsCountry> countrys = countryManager.list();
        List<EdsRegion> regions = regionManager.listBySaudiArabia();
        modelMap.put("countrys", countrys);
        modelMap.put("regions", regions);
        modelMap.put("users", !StringUtil.isEmpty(userCount) ? userCount : 4);
        modelMap.put("locales", signupService.getSupportedLocales());
        List<CountryCallingCodeLayer.CountryCallCode> callCodes = CountryCallingCodeLayer.getCountryCallCodes();
        modelMap.put("countryCallCodes", callCodes);
        if (viewName.toLowerCase().contains("iframesignup")) {
            viewName = "iframeSignup";
        }
        if (viewName.toLowerCase().contains("webformsignup")) {
            viewName = "webFormSignup";
        }
        if (viewName.toLowerCase().contains("slidebarsignup")) {
            viewName = "slidebarSignup";
        }
        if (viewName.toLowerCase().contains("producttoursignup")) {
            viewName = "productTourSignup";
        }
        if (viewName.toLowerCase().contains("signupfromwebsite")) {
            viewName = "signupFromWebSite";
        }
        if (viewName.toLowerCase().contains("marketingsignup")) {
            viewName = "marketingSignup";
        }
        if (viewName.toLowerCase().contains("websitesignup")) {
            viewName = "websiteSignup";
        }
        if (viewName.toLowerCase().contains("websitefreetrial")) {
            viewName = "websiteFreeTrial";
        }
        if (viewName.toLowerCase().contains("customtrialday")) {
            viewName = "customTrialDay";
        }
        if (viewName.toLowerCase().contains("freesignup")) {
            viewName = "freeSignup";
        }
        if (viewName.toLowerCase().contains("bestsignup")) {
            viewName = "bestSignup";
        }
        mav.addAllObjects(modelMap);
        mav.setViewName(viewName);
        if (fromWordpress) {
            boolean first = true;
            StringBuilder sb = new StringBuilder();
            if (formId != null) {
                sb.append("formId=").append(formId);
                first = false;
            }
            for (ObjectError objectError : errors.getAllErrors()) {
                String field = ((FieldError) objectError).getField();
                String description = ((FieldError) objectError).getDefaultMessage();
                String code = ((FieldError) objectError).getCode();
                if (!first) {
                    sb.append("&");
                }
                sb.append(field).append("=").append(code);
                first = false;
            }
            response.sendRedirect("https://www.kpi.com/auth?" + sb.toString());
            return null;
        }
        return mav;
    }

    /**
     * Ussed from existingUser.jsp while clicking "continue" link (to continue create new account with existing username)
     *
     * @param request
     * @param response
     * @param newCompany
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/registerCompany", method = RequestMethod.POST)
    public ModelAndView registerCompanyAndRedirectToSystem(HttpServletRequest request, HttpServletResponse response, NewCompany newCompany) throws Exception {
        ModelAndView mav = registerCompany(request, response, newCompany, TemplateSchema.getSchema(newCompany.getAdminFName(), request.getHeader("host")));
        String sessionID = sessionService.obtainSessionAndRegisterInSystem(request, response, (AuthDetails) mav.getModel().get("authDetails"));
        return AuthUtils.fillCookieValuesAndRedirectToTheSystem(request, response, sessionID, "/welcomePage.html");
    }

    /**
     * Method handles ajax request
     */

    @RequestMapping(value = "/handleAjaxRequest")
    public void handleAjaxRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter writer = response.getWriter();
        writer.print(shortEmailValidate(request));
    }

    //Request Handler Methods END

}
