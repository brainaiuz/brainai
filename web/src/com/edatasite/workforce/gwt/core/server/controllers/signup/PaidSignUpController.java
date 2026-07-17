package com.edatasite.workforce.gwt.core.server.controllers.signup;

import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.controllers.EmailAddressValidator;
import com.edatasite.workforce.gwt.core.server.controllers.PhoneNumberValidator;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.myaccount.client.rpc.MyAccountService;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.pricing.client.PayPalCalculationHelper;
import com.edatasite.workforce.gwt.pricing.client.SubscriptionPaymentItem;
import com.edatasite.workforce.gwt.pricing.client.UserRateItem;
import com.edatasite.workforce.gwt.signup.client.rpc.CreatedCompany;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.gwt.signup.client.rpc.SignUpService;
import com.edatasite.workforce.utils.EdsContextParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Locale;

/**
 * User: Sherali
 * Date: 11.12.2008
 * Time: 11:47:12
 */
@Controller
public class PaidSignUpController implements Constants {

    @Autowired
    private CountryManager countryManager;
    @Autowired
    private SignUpService signupService;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private MyAccountService myAccountService;

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(@Qualifier("messageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping(value = {"/signup2.html"})
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerUtils.fillHostParameters(request);
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
        SubscriptionPaymentItem subscriptionPaymentItem = new SubscriptionPaymentItem();
        PayPalCalculationHelper payPalCalculationHelper = new PayPalCalculationHelper();
        subscriptionPaymentItem.userCount = ServletRequestUtils.getIntParameter(request, "users");
        subscriptionPaymentItem.storage = ServletRequestUtils.getIntParameter(request, "storage");
        subscriptionPaymentItem.usagePeriodID = ServletRequestUtils.getIntParameter(request, "usagePeriodID");
        subscriptionPaymentItem.serviceType = ServletRequestUtils.getBooleanParameter(request, "type");
        subscriptionPaymentItem.isUK = ServletRequestUtils.getBooleanParameter(request, "tax");
        subscriptionPaymentItem.category = ServletRequestUtils.getStringParameter(request, "category");
        subscriptionPaymentItem.isGBP = ServletRequestUtils.getBooleanParameter(request, "isGBP");
        subscriptionPaymentItem.modules = ServletRequestUtils.getStringParameter(request, "modules");
        if (request.getParameter("moduleLimit") != null && !"".equals(request.getParameter("moduleLimit"))) {
            subscriptionPaymentItem.moduleLimit = ServletRequestUtils.getIntParameter(request, "moduleLimit");
        }
        /***    signup.html?users=.....
         ***    Link ga birinchi marta keganida {supportPackage} null boladigan bolsa, {supportPackage} ga -1 berivoriladi,
         ***    Chunki signup button bosilganda ham shu yerga qayta murojat qilinadi (Agar supportPackage ning qiymati -1 boladigan bolsa, demak signup button bosilgan boladi )
         ***
         **/
        subscriptionPaymentItem.supportPackage = ServletRequestUtils.getIntParameter(request, "supportPackage") != null
                                    ? ServletRequestUtils.getIntParameter(request, "supportPackage")
                                    : -1;

        /***    signup button bosilganda shu yerga qayta murojat qilinadi
         ***    Agar {supportPackage} ni qiymati -1 buladigan bolsa, {supportPackage} ga null berivoriladi
         ***    Chunki signup button bosilgandan bu yerga qayta murojat qilinadi
         ***
         **/
        if (ServletRequestUtils.getIntParameter(request, "supportPackage") != null && ServletRequestUtils.getIntParameter(request, "supportPackage").equals(-1)) {
            subscriptionPaymentItem.supportPackage = null;
        }


        NewCompany newCompany = new NewCompany();
        newCompany.setAdminFName(ServletRequestUtils.getStringParameter(request, "adminFName"));
        newCompany.setAdminLName(ServletRequestUtils.getStringParameter(request, "adminLName"));
        newCompany.setAdminEmail(ServletRequestUtils.getStringParameter(request, "adminEmail"));
        newCompany.setName(ServletRequestUtils.getStringParameter(request, "name"));
        Object defaultLocale = request.getAttribute("defaultLocale");
        newCompany.setLocale(request.getParameter("locale") != null
                             ? request.getParameter("locale")
                             : defaultLocale.toString());
        newCompany.setPaymentMethod(ServletRequestUtils.getStringParameter(request, "paymentMethod"));
        String phone = "";

        String clientIPAdderss = request.getHeader("X-FORWARDED-FOR"); //RETURNS IP ADDRESS OF CLIENT SYSTEM
        if (clientIPAdderss == null) {
            clientIPAdderss = request.getRemoteAddr();
        }

        newCompany.setClientSingUpIPAddress(clientIPAdderss);

        Locale locale;
        if (newCompany.getLocale() != null) {
            locale = new Locale(newCompany.getLocale());
        } else {
            locale = EdsContextParams.getDefaultLocale(request.getServerName());
        }

        Integer countryID = null;
        int errors = 0;
        ModelAndView model = null;
        if (request.getRequestURL().toString().contains("signup2.html")) {
            model = new ModelAndView("signup2");
        } else {
            model = new ModelAndView("signup");
        }
        if (EdsContextParams.isCaptchaEnabled(request.getServerName())) {
            model.addObject("captcha", true);
            model.addObject("captchaTheme", "white");
            if (!ServerUtils.validateCaptcha(request, response) && newCompany.getAdminEmail() != null) {
                request.setAttribute("captchaError", messageSource.getMessage("signup.WrongCaptcha", null, "Wrong Captcha! Please try again", locale));
                errors++;
            }
        }

        phone = ServletRequestUtils.getStringParameter(request, "phone");
        ServerUtils.fillHostParameters(request);
        model.addObject("phone", phone);
        if (phone != null) {
            PhoneNumberValidator phonevalid = new PhoneNumberValidator(phone);
            String errmsg = null;
            if (!phonevalid.hasContent()) {
                errmsg = messageSource.getMessage("signup.EnterYourPhoneNumber", null, "Enter Your phone number", locale);
            } else if (!phonevalid.checkPhone()) {
                errmsg = messageSource.getMessage("signup.PleaseEnterYourPhoneNumber", null, "Please Enter Your phone number", locale);
            } else {
                newCompany.setPhone(phone);
            }
            if (errmsg != null) {
                model.addObject("phoneError", errmsg);
                errors++;
            }
        }

        model.addObject("type", subscriptionPaymentItem.serviceType);
        if (request.getParameter("service") != null) {
            subscriptionPaymentItem.service = ServletRequestUtils.getStringParameter(request, "service");
            model.addObject("service", subscriptionPaymentItem.service);
            newCompany.setSignedUpPage(subscriptionPaymentItem.service);
            if (PA.equals(subscriptionPaymentItem.service)) {
                subscriptionPaymentItem.serviceName = messageSource.getMessage("frontendmain.performanceAppraisals", null, "Performance Appraisals", locale);

            } else if (PA2.equals(subscriptionPaymentItem.service)) {
                subscriptionPaymentItem.serviceName = messageSource.getMessage("frontendmain.performanceAppraisals", null, "Performance Appraisals", locale);
            } else if (PRM.equals(subscriptionPaymentItem.service)) {
                subscriptionPaymentItem.serviceName = messageSource.getMessage("frontendmain.projectManagement", null, "Project Management", locale);
            } else if (PRM2.equals(subscriptionPaymentItem.service)) {
                subscriptionPaymentItem.serviceName = messageSource.getMessage("freeTrial.timeTracking", null, "Time Tracking", locale);
            } else if (INV.equals(subscriptionPaymentItem.service)) {
                subscriptionPaymentItem.serviceName = messageSource.getMessage("pricing.invoicing", null, "Invoicing", locale);
            } else if (AVA.equals(subscriptionPaymentItem.service)) {
                subscriptionPaymentItem.serviceName = messageSource.getMessage("signup.attendaveTracking", null, "Attendave tracking", locale);
            } else if (EXP.equals(subscriptionPaymentItem.service)) {
                subscriptionPaymentItem.serviceName = messageSource.getMessage("signup.expenseClaims", null, "Expense Claims", locale);
            } else if (ACC.equals(subscriptionPaymentItem.service)) {
                subscriptionPaymentItem.serviceName = messageSource.getMessage("signup.accountingFinance", null, "Accounting & Finance", locale);
            } else {
                subscriptionPaymentItem.service = PRM;
                subscriptionPaymentItem.serviceName = messageSource.getMessage("frontendmain.projectManagement", null, "Project Management", locale);
            }
        } else {
            newCompany.setSignedUpPage(PRM);
            subscriptionPaymentItem.serviceName = messageSource.getMessage("signup.allServices", null, "All Services", locale);
        }
        if (!"".equals(request.getParameter("countryID"))) {
            countryID = ServletRequestUtils.getIntParameter(request, "countryID");
        } else {
            errors++;
            model.addObject("countryError", messageSource.getMessage("countrNameRequared", null, "Please select country", locale));
        }
        String hostNameP = String.valueOf(request.getAttribute("hostName"));
        String pricingPackageNAME = PayPalCalculationHelper.getPricingPackageNAME(subscriptionPaymentItem.category);
        boolean pricingType = UsagePlanItem.CUSTOM.equals(subscriptionPaymentItem.category);
        String modules = PayPalCalculationHelper.getModuleNAME(subscriptionPaymentItem.modules);
        String supportPackageNAME = null;
        if (hostNameP.contains("aws.kpi.com") || hostNameP.contains("mcloud.kpi.com") || hostNameP.contains("app.kpi.com") || hostNameP.contains("kpi.com")) {
            supportPackageNAME = PayPalCalculationHelper.getSupportPackageNAME2(subscriptionPaymentItem.supportPackage);
        } else {
            supportPackageNAME = PayPalCalculationHelper.getSupportPackageNAME(subscriptionPaymentItem.supportPackage);
        }
        double vat_rate = Double.valueOf(String.valueOf(request.getAttribute("vatN")));

        boolean booleanIsSmebuOrTjiloHosts = hostNameP.contains("smebu.com") || hostNameP.contains("tjilo.com") || hostNameP.contains("localhost");
        boolean isErp = hostNameP.contains("1erp.sa");

        if (booleanIsSmebuOrTjiloHosts) {
            UserRateItem userRateItemPerHOST = myAccountService.getUserDiscount(subscriptionPaymentItem.userCount, hostNameP);
            final double[][] discount_per_monthly2 = {{userRateItemPerHOST.getDiscountOneMonth(), userRateItemPerHOST.getDiscountThreeMonth(),
                                                       userRateItemPerHOST.getDiscountSixMonth(), userRateItemPerHOST.getDiscountTwentyMonth()}}; // Example: (1 month - not discount), 3 month - 15%, 6 month - 25%, 12 month - 30%;

            if (subscriptionPaymentItem.usagePeriodID == 0) {
                subscriptionPaymentItem.usageMonths = 1;
                subscriptionPaymentItem.usagePeriod = "1";
                subscriptionPaymentItem.planType = EdsUsagePlan.ONE_MONTH_0;
                payPalCalculationHelper.calculateCosts(subscriptionPaymentItem, userRateItemPerHOST.getUserRate(), hostNameP, vat_rate, discount_per_monthly2);
            } else if (subscriptionPaymentItem.usagePeriodID == 1) {
                subscriptionPaymentItem.usageMonths = 3;
                subscriptionPaymentItem.usagePeriod = "3";
                subscriptionPaymentItem.planType = EdsUsagePlan.THREE_MONTH_15;
                payPalCalculationHelper.calculateCosts(subscriptionPaymentItem, userRateItemPerHOST.getUserRate(), hostNameP, vat_rate, discount_per_monthly2);
            } else if (subscriptionPaymentItem.usagePeriodID == 2) {
                subscriptionPaymentItem.usageMonths = 6;
                subscriptionPaymentItem.usagePeriod = "6";
                subscriptionPaymentItem.planType = EdsUsagePlan.SIX_MONTH_20;
                payPalCalculationHelper.calculateCosts(subscriptionPaymentItem, userRateItemPerHOST.getUserRate(), hostNameP, vat_rate, discount_per_monthly2);
            } else if (subscriptionPaymentItem.usagePeriodID == 3) {
                subscriptionPaymentItem.usageMonths = 12;
                subscriptionPaymentItem.usagePeriod = "12";
                subscriptionPaymentItem.planType = EdsUsagePlan.TWELVE_MONTH_TWENTY_30;
                payPalCalculationHelper.calculateCosts(subscriptionPaymentItem, userRateItemPerHOST.getUserRate(), hostNameP, vat_rate, discount_per_monthly2);
            }
        } else {
            UserRateItem userRateItemPerHOST = new UserRateItem();
            if (isErp) {
                userRateItemPerHOST.setUserRate(payPalCalculationHelper.getTotalPrice(subscriptionPaymentItem.usagePeriodID, subscriptionPaymentItem.userCount, subscriptionPaymentItem.moduleLimit));
                userRateItemPerHOST.setDiscountOneMonth(0d);
                userRateItemPerHOST.setDiscountThreeMonth(0d);
                userRateItemPerHOST.setDiscountSixMonth(0d);
                userRateItemPerHOST.setDiscountTwentyMonth(0d);
                userRateItemPerHOST.setSupportPackagePrice(0d);
            } else {
                userRateItemPerHOST = myAccountService.getUserT(hostNameP, pricingPackageNAME, supportPackageNAME, modules, pricingType);
            }

            final double[][] discount_per_monthly2 = {{userRateItemPerHOST.getDiscountOneMonth(), userRateItemPerHOST.getDiscountThreeMonth(),
                                                       userRateItemPerHOST.getDiscountSixMonth(), userRateItemPerHOST.getDiscountTwentyMonth()}}; // Example: (1 month - not discount), 3 month - 15%, 6 month - 25%, 12 month - 30%;
            if (subscriptionPaymentItem.usagePeriodID == 0) {
                subscriptionPaymentItem.usageMonths = 1;
                subscriptionPaymentItem.usagePeriod = "1";
                subscriptionPaymentItem.planType = EdsUsagePlan.ONE_MONTH_0;
                payPalCalculationHelper.calculateCostsNEW(subscriptionPaymentItem, userRateItemPerHOST.getUserRate(), userRateItemPerHOST.getSupportPackagePrice(), hostNameP, vat_rate, discount_per_monthly2);
            } else if (subscriptionPaymentItem.usagePeriodID == 1) {
                subscriptionPaymentItem.usageMonths = 3;
                subscriptionPaymentItem.usagePeriod = "3";
                subscriptionPaymentItem.planType = EdsUsagePlan.THREE_MONTH_15;
                payPalCalculationHelper.calculateCostsNEW(subscriptionPaymentItem, userRateItemPerHOST.getUserRate(), userRateItemPerHOST.getSupportPackagePrice(), hostNameP, vat_rate, discount_per_monthly2);
            } else if (subscriptionPaymentItem.usagePeriodID == 2) {
                subscriptionPaymentItem.usageMonths = 6;
                subscriptionPaymentItem.usagePeriod = "6";
                subscriptionPaymentItem.planType = EdsUsagePlan.SIX_MONTH_20;
                payPalCalculationHelper.calculateCostsNEW(subscriptionPaymentItem, userRateItemPerHOST.getUserRate(), userRateItemPerHOST.getSupportPackagePrice(), hostNameP, vat_rate, discount_per_monthly2);
            } else if (subscriptionPaymentItem.usagePeriodID == 3) {
                subscriptionPaymentItem.usageMonths = 12;
                subscriptionPaymentItem.usagePeriod = "12";
                subscriptionPaymentItem.planType = EdsUsagePlan.TWELVE_MONTH_TWENTY_30;
                payPalCalculationHelper.calculateCostsNEW(subscriptionPaymentItem, userRateItemPerHOST.getUserRate(), userRateItemPerHOST.getSupportPackagePrice(), hostNameP, vat_rate, discount_per_monthly2);
            }
        }
        newCompany.setCountryID(countryID);

        //Set Affialiate and Compaing
        String aff = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("aff")) {
                aff = cookie.getValue();
                cookie.setMaxAge(-1);
                break;
            }
        }

        if (aff != null) {
            newCompany.setValue1(aff);
        }

        String kcpn = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("kcpn")) {
                kcpn = cookie.getValue();
                cookie.setMaxAge(-1);
                break;
            }
        }

        if (kcpn != null) {
            newCompany.setValue2(kcpn);
        }

        String referer = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("referer")) {
                referer = cookie.getValue();
                cookie.setMaxAge(-1);
                break;
            }
        }

        if (referer != null) {
            newCompany.setValue3(referer);
        }

        newCompany.setValue1(aff);
        newCompany.setValue2(kcpn);
        newCompany.setValue3(referer);

        if (newCompany.getAdminEmail() == null) {
            errors++;
        }

        model.addObject("newCompany", newCompany);
        model.addObject("adminFName", newCompany.getAdminFName());
        if ("".equals(newCompany.getAdminFName())) {
            model.addObject("fNameError", messageSource.getMessage("firstNameRequared", null, "First name is required", locale));
            errors++;
        }
        model.addObject("adminLName", newCompany.getAdminLName());
        if ("".equals(newCompany.getAdminLName())) {
            model.addObject("lNameError", messageSource.getMessage("lastNameRequared", null, "Last name is required", locale));
            errors++;
        }
        model.addObject("adminEmail", newCompany.getAdminEmail());
        String email = "";
        email = newCompany.getAdminEmail();

        if (email != null) {
            String errmsg = null;
            if (!EmailAddressValidator.checkEmail(email)) {
                errmsg = messageSource.getMessage("emailAddressSsInvalid", null, "Email address is invalid", locale);
            }

            if (errmsg != null) {
                model.addObject("emailError", errmsg);
                errors++;
            }
        }
        model.addObject("name", newCompany.getName());
        if ("".equals(newCompany.getName())) {
            model.addObject("nameError", messageSource.getMessage("companyNameRequared", null, "Company name is required", locale));
            errors++;
        }

        boolean isCheked = false;
        if (ServletRequestUtils.getBooleanParameter(request, "check") != null) {
            isCheked = ServletRequestUtils.getBooleanParameter(request, "check");
            if (!isCheked) {
                model.addObject("chekedError", messageSource.getMessage("agreeWithConditionRequared", null, "You should agree with \"Terms and Conditions\"", locale));
                errors++;
            }
        }
        String defaultCurrencyCODE = request.getAttribute("defaultCurrencyCODE") != null
                                     ? String.valueOf(request.getAttribute("defaultCurrencyCODE"))
                                     : "USD";
        final String UKorUSD = isErp
                               ? "(SR) "
                               : ServerUtils.getHTMLCODESForCurrency(subscriptionPaymentItem.isGBP
                                                                     ? "GBP"
                                                                     : defaultCurrencyCODE/*EdsContextParams.getCurrencyCODE()*/);

        model.addObject("usagePeriod", subscriptionPaymentItem.usagePeriod);
        model.addObject("usagePeriodID", subscriptionPaymentItem.usagePeriodID);
        model.addObject("users", subscriptionPaymentItem.userCount);
        model.addObject("storage", subscriptionPaymentItem.storage);
        if (pricingType) {
            model.addObject("usersCost", UKorUSD + /*numberFormat.format*/getNumberFormatWithBigDecimal(/*payPalItem.usersCost*/subscriptionPaymentItem.perUserCost));
        }
        model.addObject("storageCost", UKorUSD + /*numberFormat.format*/getNumberFormatWithBigDecimal(subscriptionPaymentItem.storageCost));
        model.addObject("discount", UKorUSD + /*numberFormat.format*/getNumberFormatWithBigDecimal(subscriptionPaymentItem.discounts));
        model.addObject("taxCost", UKorUSD + /*numberFormat.format*/getNumberFormatWithBigDecimal(subscriptionPaymentItem.taxC));
        model.addObject("tax", subscriptionPaymentItem.isUK);
        model.addObject("isGBP", subscriptionPaymentItem.isGBP);
        model.addObject("supportPackage", subscriptionPaymentItem.supportPackage);
        model.addObject("total", UKorUSD + /*numberFormat.format*/getNumberFormatWithBigDecimal(subscriptionPaymentItem.tot));
        model.addObject("category", subscriptionPaymentItem.category);
        model.addObject("modules", subscriptionPaymentItem.modules);
        model.addObject("moduleLimit", subscriptionPaymentItem.moduleLimit);
        model.addObject("countrys", countryManager.list());
        model.addObject("locales", signupService.getSupportedLocales());
        model.addObject("locale", newCompany.getLocale());
        model.addObject("aff", aff);
        model.addObject("kcpn", kcpn);
        model.addObject("referer", referer);
        model.addObject("smebuOrTjiloHosts", booleanIsSmebuOrTjiloHosts ? "true" : "false");

        String[] paymentMethods = new String[2];
        paymentMethods[0] = PayType.PayPal.name();
        paymentMethods[1] = PayType.WorldPay.name();
        model.addObject("paymentMethods", paymentMethods);
        model.addObject("paymentMethod", paymentMethods[0]);

        UsagePlanItem fUsagePlanItem = new UsagePlanItem();
        fUsagePlanItem.setDiscount(subscriptionPaymentItem.discounts);
        fUsagePlanItem.setPlanType(subscriptionPaymentItem.planType);
        fUsagePlanItem.setStorageCount(subscriptionPaymentItem.storage);
        fUsagePlanItem.setTotalAmount(subscriptionPaymentItem.tot);
        fUsagePlanItem.setUserCount(subscriptionPaymentItem.userCount);
        fUsagePlanItem.setCurrencyGBP(subscriptionPaymentItem.isGBP);
        fUsagePlanItem.setSupportPackage(subscriptionPaymentItem.supportPackage);
        fUsagePlanItem.setSupportPackageNAME(subscriptionPaymentItem.supportPackageNAME);
        fUsagePlanItem.setSupportPackagePrice(subscriptionPaymentItem.supportPackagePrice);
        fUsagePlanItem.setCategoryREAL(subscriptionPaymentItem.categoryREAL);
        fUsagePlanItem.setCompanyUk(subscriptionPaymentItem.isUK);
        fUsagePlanItem.setUserRate(subscriptionPaymentItem.perUserCost);
        if (modules != null) {
            fUsagePlanItem.setModules(modules.length() > 0 ? modules.replace("'", "") : "");
        }
        if (subscriptionPaymentItem.serviceType) {
            fUsagePlanItem.setService(ALL_SERVICES);
        } else {
            fUsagePlanItem.setService(subscriptionPaymentItem.service);
        }
        final String cmd = "cmd";                       //_xclick-subscriptions;
        final String business = "business";             //saleinvoice@workforcetrack.com
        final String currency_code = "currency_code";   //USD
        final String amount = "amount";                 //25$
        final String item_name = "item_name";           //
        final String item_number = "item_number";       //
        final String worldPayItemNumber = "MC_item_number";       //
        final String txn_type = "txn_type";             //
        final String custom = "custom";                 //
        final String worldPayCustom = "MC_custom";                 //
        final String tax = "tax";                       //
        final String a3 = "a3";                          //5.00
        final String p3 = "p3";                          //1  (1,3,6,12 - Months)
        final String t3 = "t3";                          //M (Month)
        final String srt = "srt";                       //1,2,3 (Limit the number of billing cycles.)
        final String src = "src";                       //0,1 (Recurring payments  {0  subscription payments do not recur, 1  subscription payments recur}.)
        final String cancel_return = "cancel_return";
        final String returnT = "return";
        final String currencyValue = subscriptionPaymentItem.isGBP
                                     ? "GBP"
                                     : defaultCurrencyCODE/*EdsContextParams.getCurrencyCODE()*//*"USD"*/;
        if (errors > 0) {
            return model;
        } else {
            newCompany.setActive(false);
            if (newCompany.getLocale() == null || newCompany.getLocale().equals("")) {
                newCompany.setLocale(EdsContextParams.getDefaultLocale(request.getServerName()).getLanguage());
            }

            if (newCompany.getTheme() == null || newCompany.getLocale().equals("")) {
                newCompany.setTheme(EdsContextParams.getDefaultTheme());
            }

            newCompany.setHost(request.getServerName());
            CreatedCompany comID = signupService.createCompany(newCompany);
            fUsagePlanItem.setCompanyID(comID.getCompanyId());
            UsagePlanItem usagePlanID = commonServiceLocal.usagePlanSaveAndGetId(fUsagePlanItem);
            if (comID != null && comID.getCompanyId() != null) {
                SecurityContext.getInstance().setDatabase(Constants.DATABASE_PAID);
                try {
                    crmServiceLocal.createLeadFromSignUpper(NewCompany.toString(newCompany));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
            SecurityContext.getInstance().setCompanyId(comID.getCompanyId());
            /*DecimalFormat pureFormat = new DecimalFormat(",##0.00");*/
            //SR static for now
            String totalPrice = isErp
                                ? getNumberFormatWithBigDecimal(subscriptionPaymentItem.tot / 3.75)
                                : getNumberFormatWithBigDecimal(subscriptionPaymentItem.tot);
            String payType = request.getParameter("paymentMethod");
            if (payType != null && payType.equals(PayType.WorldPay.name())) {
                String contextPath = getWorldPayLink() + "?";
                response.sendRedirect(response.encodeRedirectURL(contextPath + "desc=" + EdsContextParams.getProductName() + " - ERP.&"
                                                                 + "cartId" + "=" + String.valueOf(usagePlanID.getObjectID()) + "&"
                                                                 + "currency" + "=" + currencyValue + "&"
                                                                 + "testMode" + "=" + getWorldPayTestModeValue() + "&"
                                                                 + "instId" + "=" + getWorldPayAccount() + "&"
                                                                 + "hideCurrency=hideCurrency&option=0&"

                                                                 + "futurePayType=regular&"
                                                                 + "noOfPayments=0&"
                                                                 + "amountLimit=0&"
                                                                 + "lengthMult=1&"
                                                                 + "lengthUnit=3&"
                                                                 + "intervalUnit=3&"  //1 -day, 2-week, 3-month, 4-year
                                                                 + "intervalMult="+ subscriptionPaymentItem.usageMonths + "&"
                                                                 + "startDelayUnit=1&"
                                                                 + "startDelayMult=1&"
                                                                 + "normalAmount=" + totalPrice + "&"
                                                                 + "MC_usageplaceId=" + String.valueOf(usagePlanID.getObjectID()) + "&"
                                                                 + worldPayItemNumber + "=1&"
                                                                 + worldPayCustom + "=" + comID.getCompanyId() + SUBSCRIPTION_ADD + usagePlanID.getObjectID() + "&"
                                                                 + "MC_host=" + response.encodeRedirectURL(EdsContextParams.getFullHost() + "success.html")
                                                                ));
            } else {
                String contextPath = "https://" + getPayPalLink() + "?";
                response.sendRedirect(response.encodeRedirectURL(contextPath + cmd + "=_xclick-subscriptions&"
                                                                 + business + "=" + getPayPalAccount() + "&"         //      sales@workforcetrack.com
                                                                 + currency_code + "=" + currencyValue + "&"
                                                                 + amount + "=" + /*pureFormat.format*/totalPrice + "&"  ////test "tot"
                                                                 + tax + "=" + /*pureFormat.format*/getNumberFormatWithBigDecimal(subscriptionPaymentItem.taxC) + "&"  ////test "taxC"
                                                                 + item_name + "=" + EdsContextParams.getProductName() + " - " + "ERP" + ".&"
                                                                 + item_number + "=1&"
                                                                 + a3 + "=" + /*pureFormat.format*/totalPrice + "&"       ////test "tot"
                                                                 + p3 + "=" + subscriptionPaymentItem.usageMonths + "&"
                                                                 + t3 + "=M&"
                                                                 + src + "=1&"
                                                                 + custom + "=" + comID.getCompanyId() + SUBSCRIPTION_ADD + usagePlanID.getObjectID() + "&"
                                                                 + returnT + "=" + response.encodeRedirectURL(EdsContextParams.getFullHost() + "success.html") + "&"
                                                                 + cancel_return + "=" + response.encodeRedirectURL(EdsContextParams.getFullHost() + "cancel.html")
                                                                ));
            }
            return null;
        }
    }

    private String getNumberFormatWithBigDecimal(double value) {
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    private static String getPayPalLink() {
        if (EdsContextParams.isLiveEnvironment()) {
            return paypal_LINK_Live;
        } else {
            return paypal_LINK_Test;
        }
    }

    private static String getWorldPayLink() {
        return WORLDPAY_LINK_Live;
    }

    private static String getPayPalAccount() {
        String paypalAccount = EdsContextParams.getPaypalAccount();
        if (paypalAccount != null) {
            return paypalAccount;
        }
        return paypal_ACCOUNT_Live;
    }

    private String getWorldPayTestModeValue() {
        return "0";//live mode

    }

    public static String getWorldPayAccount() {
        return WORLDPAY_ACCOUNT_Live;
    }


    private enum PayType {
        PayPal, WorldPay
    }

    /**
     *
     * @param userCount
     * @param essUserCount
     * @param noAccessUserCount
     * @param subsriptionType
     * @param subscriptionPaymentItem
     * @return
     *
     * 		                Subscrition Per Year * 	 Subscrition Per Quarter ** 	 Subscrition Per Month ***
    ( > $1000)                     ( > $2400)
    Full Access User (FAU)	             $150.00 	                $40.00 	                        $14.00 	user
    Limited Access User (LAU)	         $50.00 	                $15.00 	                        $5.50 	limiteduser
    No Access User (NAU)	             $12.50 	                $3.50 	                        $1.25 	noaccessuser

     */

    private UsagePlanItem calcualateUsagePlanForPakistan(Integer userCount, Integer essUserCount, Integer noAccessUserCount, Integer subsriptionType, SubscriptionPaymentItem subscriptionPaymentItem) {
        if (userCount != null && essUserCount != null && noAccessUserCount != null && subsriptionType != null) {
            UsagePlanItem result = new UsagePlanItem();
            result.setUserCount(userCount);
            result.setEssUserCount(essUserCount);
            result.setNonAccessUserCount(noAccessUserCount);
            double userRate = 0;
            double noAccessUserRate = 0;
            double essUserRate = 0;
            if (subsriptionType == 0) {
                userRate = 14.00;
                noAccessUserRate = 5.50;
                essUserRate = 1.25;
            } else if (subsriptionType == 1) {
                userRate = 40.00;
                noAccessUserRate = 15.00;
                essUserRate = 3.50;
            } else if (subsriptionType == 3) {
                userRate = 150.00;
                noAccessUserRate = 50.00;
                essUserRate = 12.50;
            }

            subscriptionPaymentItem.tot = (float) ((userRate * userCount) + (noAccessUserRate * noAccessUserCount) + (essUserRate * essUserCount));
            result.setTotalAmount(subscriptionPaymentItem.tot);
            result.setEssUserCount(essUserCount);
            return result;
        }
        return null;
    }

}
