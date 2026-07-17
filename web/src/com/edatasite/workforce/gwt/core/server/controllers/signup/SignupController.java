package com.edatasite.workforce.gwt.core.server.controllers.signup;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.AuthUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CountryCallingCodeLayer;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.utils.EdsContextParams;
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class SignupController extends GeneralFreeSignUpController {
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
    @RequestMapping(value = "/signup.html", method = RequestMethod.GET)
    public ModelAndView showForm(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String viewName = request.getPathInfo().substring(request.getPathInfo().indexOf("/") + 1, request.getPathInfo().indexOf("."));

        //    Get User country code from ip address

        SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        String as = request.getPathInfo().substring(request.getPathInfo().indexOf("/") + 1, request.getPathInfo().indexOf("."));
        Integer usersSize = ServletRequestUtils.getIntParameter(request, "users");//free users count, default 4 users;
        ServerUtils.fillHostParameters(request);
        ModelAndView model = null;
        if (Utils.isPraaktis(request)) {
            model = new ModelAndView("praaktisgosignup");
        } else if (Utils.isCspace(request)) {
            model = new ModelAndView("cspaceSignup");
        }else if (Utils.isTextileFinds(request)) {
            model = new ModelAndView("textilefindsSignup");
        } else if (Utils.isBrain(request)) {
            model = new ModelAndView("brainUzSignUp");
        } else {
            model = new ModelAndView(as);
        }
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
        if (org.apache.commons.lang.StringUtils.isNotBlank(EdsContextParams.getOauth2ConsumerKey())) {
            request.setAttribute(Constants.GOOGLE_CLIENT_ID, EdsContextParams.getOauth2ConsumerKey());
        } else {
            request.setAttribute(Constants.GOOGLE_CLIENT_ID, "508805038834-snuqpahkvtkvjlfudk611plkbn9c25hm.apps.googleusercontent.com");
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
        String countryCode;
        String countryCallCode;
        if (Utils.isBrain(request)) {
            // Brain signup is an Uzbekistan product: always default to Uzbekistan (ignore GeoIP).
            countryCode = UZ;
            CountryCallingCodeLayer.CountryCallCode uz = callCodes.stream()
                    .filter(item -> UZ.equals(item.getCountryCode()))
                    .findAny().orElse(null);
            countryCallCode = uz != null ? "+" + uz.getCallCode() : null;
        } else {
            // Other clients: resolve the visitor's country via GeoIP, falling back to US when unknown.
            countryCode = ServerUtils.getClientCountryCodeByIP(request);
            if (countryCode == null || "".equals(countryCode) || "--".equals(countryCode)) {
                countryCode = US_CODE;
                countryCallCode = US_CALL_CODE;
            } else {
                countryCallCode = null;
            }
            if (countryCallCode == null) {
                String finalCountryCode = countryCode;
                CountryCallingCodeLayer.CountryCallCode detected = callCodes.stream()
                        .filter(item -> finalCountryCode.equals(item.getCountryCode()))
                        .findAny().orElse(null);
                if (detected != null) {
                    countryCallCode = "+" + detected.getCallCode();
                }
            }
        }
        List<IdName> currencies = currencyManager.getAllCurrency(null).stream()
                .map(c -> {
                    IdName idName = new IdName(c.getObjectID(), c.getName());
                    idName.addProperty("fullName", c.getFullName());
                    idName.addProperty("symbol", c.getSymbol());
                    return idName;
                })
                .toList();
        model.addObject("currencies", currencies);
        model.addObject("countryCallCodes", callCodes);
        model.addObject("currentCountry", countryCode);
        model.addObject("currentCallCode", countryCallCode);
        model.addObject("countrys", countrys);
        model.addObject("regions", regions);
        if (Utils.isBrain(request)) {
            model.addObject("industries", getIndustries());
        }
        model.addObject("users", usersSize != null ? usersSize : 4);
        model.addObject("freeTrialDays", freeTrialDays);
        model.addObject("locales", signupService.getSupportedLocales());
        model.addObject("activeProfile", System.getProperty("spring.profiles.active"));
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


    public List<SelectItem> getIndustries() {
        return Arrays.asList(
                new SelectItem(1, "Retail", "RETAIL", "Chakana savdo", "Розничная торговля"),
                new SelectItem(2, "Manufacturing", "MANUFACTURING", "Ishlab chiqarish", "Производство"),
                new SelectItem(3, "Healthcare / Medicine", "HEALTHCARE_MEDICINE", "Sog'liqni saqlash / Tibbiyot", "Здравоохранение / Медецина"),
                new SelectItem(4, "Wholesale", "WHOLESALE", "Ulgurji savdo", "Оптовая торговля"),
                new SelectItem(5, "Construction", "CONSTRUCTION", "Qurilish va ta'mirlash", "Стоительство и ремонтная"),
                new SelectItem(6, "Other", "OTHER", "Boshqa", "Другие")
        );

    }

    /**
     * Method is invoked when form submitted
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/signup.html", method = RequestMethod.POST)
    public ModelAndView signUp(HttpServletRequest request, HttpServletResponse response, NewCompany newCompany) throws Exception {
        boolean fromWordpress = request.getParameter("fromWordpress") != null;
        boolean botTrapped = request.getParameter("rt") != null && request.getParameter("rt").contains("on");
        String formId = request.getParameter("formId");
        if (fromWordpress && botTrapped) {
            return null;
        }
        List<CountryCallingCodeLayer.CountryCallCode> callCodes = CountryCallingCodeLayer.getCountryCallCodes();
        String viewName = request.getPathInfo().substring(request.getPathInfo().indexOf("/") + 1, request.getPathInfo().indexOf("."));
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0);
        String countryCode = request.getParameter("countryCode");
        String phone = request.getParameter("phone");
        String callCode;
        if (countryCode ==null) {
            callCode = request.getParameter("callCode");
            countryCode = CountryCallingCodeLayer.getCountryCodeByCallCode(callCode);
        } else {
            callCode = CountryCallingCodeLayer.getCallCodeByCountryCode(countryCode);
        }
        newCompany.setCallCode(callCode);
        String companyName = newCompany.getName();
        newCompany.setName((companyName != null && !companyName.isBlank()) ? companyName : DEFAULT_COMPANY_NAME);
        newCompany.setPromoCode(request.getParameter("promoCode") != null ? request.getParameter("promoCode") : null);

        if (org.apache.commons.lang3.StringUtils.isNotBlank(request.getParameter("locale"))) {
            String locale = request.getParameter("locale");
            int indexOfUnderscore = locale.indexOf('_');
            int indexOfDash = locale.indexOf('-');

            if (indexOfUnderscore != -1) {
                newCompany.setLocale(locale.substring(0, indexOfUnderscore));
            } else if (indexOfDash != -1) {
                newCompany.setLocale(locale.substring(0, indexOfDash));
            }
            if (countryCode != null && countryCode.equals(UZ)) {
                newCompany.setLocale("uz");
            } else if ((countryCode != null && countryCode.equals(CIS_COUNTRIES))) {
                newCompany.setLocale("ru");
            } else {
                newCompany.setLocale("en");
            }
        }
        EdsCountry country = null;
        if (countryCode != null && !countryCode.isEmpty()) {
            country = countryManager.getCountryByCode(countryCode);
        }
        if (country != null) {
            newCompany.setCountryID(country.getObjectID());
            if (newCompany.getPhone() != null && newCompany.getPhone().contains(country.getTelCode())) {
                newCompany.setPhone(newCompany.getPhone().substring(country.getTelCode().length()));
            }
        }
        newCompany.setCountryCode(countryCode);
        newCompany.setRedirectToSettings(true);

        if (!request.getAttribute("hostName").toString().contains("localhost")) {
            if (!org.apache.commons.lang3.StringUtils.isBlank(request.getParameter("kpi_code"))) {
                String kpi_code = request.getParameter("kpi_code");
                String adminFName = request.getParameter("adminFName");
                String adminEmail = request.getParameter("adminEmail");
                String code = adminFName + "/" + adminEmail + "/" + phone;
                if (!kpi_code.equals(encodeString(code))) {
                    ModelAndView mav = new ModelAndView();
                    if (Utils.isPraaktis(request)) {
                        mav = new ModelAndView("praaktisgosignup");
                    } else if (Utils.isCspace(request)) {
                        mav = new ModelAndView("cspaceSignup");
                    }else if (Utils.isTextileFinds(request)) {
                        mav = new ModelAndView("textilefindsSignup");
                    } else if (Utils.isBrain(request)) {
                        mav = new ModelAndView("brainUzSignUp");
                    }
                    mav.addObject("errorMessage", "Possible bot activity detected. Please verify your information.");
                    mav.addObject("countryCallCodes", callCodes);
                    mav.addObject("currentCountry", countryCode);
                    mav.addObject("currentCallCode", phone);
                    return mav;
                }
            } else {
                ModelAndView mav = new ModelAndView();
                if (Utils.isPraaktis(request)) {
                    mav = new ModelAndView("praaktisgosignup");
                } else if (Utils.isCspace(request)) {
                    mav = new ModelAndView("cspaceSignup");
                }else if (Utils.isTextileFinds(request)) {
                    mav = new ModelAndView("textilefindsSignup");
                } else if (Utils.isBrain(request)) {
                    mav = new ModelAndView("brainUzSignUp");
                }
                mav.addObject("errorMessage", "Possible bot activity detected. Please verify your information.");
                mav.addObject("countryCallCodes", callCodes);
                mav.addObject("currentCountry", countryCode);
                mav.addObject("currentCallCode", phone);
                return mav;
            }
        }

        //Get view name from url
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
        String x = request.getParameter("x");
        boolean validCaptcha = false;

        String userCount = request.getParameter("users");
        ServerUtils.fillHostParameters(request);
        ModelAndView mav = new ModelAndView();
        mav.addObject("countryCallCodes", callCodes);
        mav.addObject("currentCountry", countryCode);
        mav.addObject("currentCallCode", phone);
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

            StringBuilder sb = null;
            for (ObjectError objectError : errors.getAllErrors()) {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                String description = ((FieldError) objectError).getDefaultMessage();
                if (description == null) {
                    continue;
                }
                sb.append(description).append("</br>");
            }
            if (sb != null) {
                mav.addObject("errorMessage", sb.toString());
                mav.addObject("errors", errors);
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
        if (Utils.isBrain(request)) {
            modelMap.put("industries", getIndustries());
        }
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

        if (Utils.isPraaktis(request)) {
            mav.setViewName("praaktisgosignup");
        } else if (Utils.isCspace(request)) {
            mav.setViewName("cspaceSignup");
        }else if (Utils.isBrain(request)) {
            mav.setViewName("brainUzSignUp");
        } else {
            mav.setViewName(viewName);
        }
        request.setAttribute("activeProfile", System.getProperty("spring.profiles.active"));
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
            response.sendRedirect("https://www.kpi.com/auth?" + sb);
            return null;
        }
        return mav;
    }

    private String encodeString(String input) {
        String hash = "";
        try {
            MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = sha256Digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the hash bytes to a hexadecimal representation
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }

            hash = hexString.toString();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }
}
