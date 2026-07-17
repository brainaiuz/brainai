package com.edatasite.workforce.gwt.core.server.controllers.signup;

import com.edatasite.workforce.core.config.datasource.TenantContextHolder;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.FinancialSettingsItem;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.server.app.BackendServiceLocal;
import com.edatasite.workforce.gwt.backend.server.app.BannedDomainServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.RegistrationTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.*;
import com.edatasite.workforce.gwt.core.server.controllers.EmailAddressValidator;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.enums.TemplateSchema;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.validators.SignUpValidator;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.pricing.client.PayPalCalculationHelper;
import com.edatasite.workforce.gwt.signup.client.rpc.CreatedCompany;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.gwt.signup.client.rpc.SignUpService;
import com.edatasite.workforce.gwt.signup.server.app.SignUpServiceLocal;
import com.edatasite.workforce.rest.v3.release10.auth.dto.AccountingInitTO;
import com.edatasite.workforce.rest.v3.release10.auth.dto.CompanyInitTO;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * User: admin
 * Date: Oct 22, 2009
 * Time: 5:52:56 PM
 */
public abstract class GeneralFreeSignUpController implements Constants {

    private static final Logger log = LoggerFactory.getLogger(GeneralFreeSignUpController.class);

    protected final static String BIND_OBJECT_NAME = "newCompany";
    protected final static String FREE_SIGNUP_VIEW_NAME = "freeTrial";
    @Autowired
    protected CountryManager countryManager;
    @Autowired
    protected RegionManager regionManager;
    @Autowired
    protected SignUpService signupService;
    @Autowired
    protected ModuleManager moduleManager;
    @Autowired
    @Qualifier("crmService")
    protected CrmServiceLocal crmServiceLocal;
    @Autowired
    @Qualifier("signUpService")
    protected SignUpServiceLocal signupServiceLocal;
    @Autowired
    protected UserManager userManager;
    @Autowired
    protected LoginService loginService;
    @Autowired
    @Qualifier("loginService")
    private LoginServiceLocal loginServiceLocal;
    @Autowired
    protected SessionService sessionService;

    @Autowired
    private BackendService backendService;
    @Autowired
    private BackendServiceLocal backendServiceLocal;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    protected WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private BannedDomainServiceLocal bannedDomainServiceLocal;
    @Autowired
    protected CurrencyManager currencyManager;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private CurrencyService currecyService;
    @Autowired
    protected ReferenceManager referenceManager;
    @Autowired
    private PayrollService payrollService;
    //Handler methods START

    /**
     * Method is invoked when form submitted
     *
     * @param request
     * @param response
     * @param newCompany
     * @param templateSchema
     * @return
     * @throws Exception
     */
    public ModelAndView registerCompany(HttpServletRequest request, HttpServletResponse response, NewCompany newCompany, TemplateSchema templateSchema) throws Exception {
        String domain = request.getAttribute("hostName").toString();
        String fullHost = request.getAttribute("fullHost").toString();
        log.info("######## Domain -> " + domain + "##########");
        log.info("######## FullHost -> " + fullHost + "##########");
        if (!domain.contains("kpi.com")
                && !domain.contains("localhost")
                && !domain.contains("praaktisgo")
                && !domain.contains("cspace")
                && !domain.contains("brain")
                && !domain.contains("kpi.uz")
                && !domain.contains("brainbm")
                && !domain.contains("textilefinds")
        ) {
            log.info("#####DOMAIN DOESN'T CONTAINS KPI.COM######");
            ModelAndView mav = new ModelAndView();
            mav.addObject("errorMessage", "You can only sign up from the approved domains.");
            return mav;
        }
        log.info("Free SigpUp:>>>" + newCompany.toString());
        ServerSecurityContext.getInstance().setDatabase(DATABASE_FREE);
        ServerUtils.fillHostParameters(request);
        Object defaultLocale = request.getAttribute("defaultLocale");
        Object defaultTheme = request.getAttribute("defaultTheme");
        newCompany.setRedirectToSettings(false);
        if (newCompany.getLocale() == null || newCompany.getLocale().equals("")) {
            newCompany.setLocale(defaultLocale.toString());
        }
        if (defaultTheme != null && !defaultTheme.equals("")) {
            newCompany.setTheme(defaultTheme.toString());
        } else {
            newCompany.setTheme(newCompany.getTheme());
        }
        newCompany.setHost(request.getServerName());
        //Set Affialiate and Campaign
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("aff")) {
                    newCompany.setValue1(cookie.getValue());
                    cookie.setMaxAge(-1);
                } else if (cookie.getName().equals("kcpn")) {
                    newCompany.setValue2(cookie.getValue());
                    cookie.setMaxAge(-1);
                } else if (cookie.getName().equals("referer")) {
                    newCompany.setRedirected(cookie.getValue());
                    cookie.setMaxAge(-1);
                }
            }
        }

        newCompany.setUtm_campaign(request.getParameter("utm_campaign"));
        newCompany.setUtm_source(request.getParameter("utm_source"));
        newCompany.setUtm_medium(request.getParameter("utm_medium"));
        newCompany.setUtm_keyword(request.getParameter("utm_keyword"));
        newCompany.setUtm_btn(request.getParameter("utm_btn"));
        newCompany.setUtm_content(request.getParameter("utm_content"));
        newCompany.setUtm_term(request.getParameter("utm_term"));
        newCompany.setGclid(request.getParameter("gclid"));
        if (request.getParameter("referrer") != null && request.getParameter("referrer").contains("http")) {
            newCompany.setReferrer(request.getParameter("referrer"));
        }

        if (request.getParameter("redirected") != null && request.getParameter("redirected").contains("http")) {
            newCompany.setRedirected(request.getParameter("redirected"));
        }

        Integer objectID = signupServiceLocal.getCompany();
        newCompany.setCompanyId(objectID);
        newCompany.setSendNotification(true);//for monolith application

        try {
            backendServiceLocal.getTemplateSchemaForID(objectID, templateSchema);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CreatedCompany comID = signupServiceLocal.createSampleCompany(newCompany, templateSchema);

        if (newCompany.getName() != null && !"".equals(newCompany.getName())) {
            newCompany.setName(StringEscapeUtils.unescapeHtml(newCompany.getName()));
        }
        if (newCompany.getAdminFName() != null && !"".equals(newCompany.getAdminFName())) {
            newCompany.setAdminFName(StringEscapeUtils.unescapeHtml(newCompany.getAdminFName()));
        }
        if (newCompany.getAdminLName() != null && !"".equals(newCompany.getAdminLName())) {
            newCompany.setAdminLName(StringEscapeUtils.unescapeHtml(newCompany.getAdminLName()));
        }
        if (comID != null && comID.getCompanyId() != null) {
            SecurityContext.getInstance().setDatabase(EdsContextParams.getLeadSignUpCompany() != null ? globalAuthJdbcSpringManager.getCompanyClusterType(EdsContextParams.getLeadSignUpCompany()) : TenantContextHolder.PAID_DB);
            crmServiceLocal.createLeadFromSignUpper(NewCompany.toString(newCompany));
        }
        SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        //usage Plan

        Boolean isCurrencyGBP = false;

        Integer usersSize = null;
        try {
            usersSize = ServletRequestUtils.getIntParameter(request, "users");//free users count, default 4 users;
        } catch (Exception ex) {
        }
        String pricingCategory = ServletRequestUtils.getStringParameter(request, "category");//selected pricing package;
        String pricingPackageNAME = StringUtils.isNotBlank(pricingCategory) ? PayPalCalculationHelper.getPricingPackageNAME(pricingCategory) : "";


        String countryCode = ServerUtils.getClientCountryCodeByIP(request);
        if (countryCode != null && countryCode.equals(UK)) {
            isCurrencyGBP = true;
        }
        ServerSecurityContext.getInstance().setCompanyId(comID.getCompanyId().toString());
        String hostName = String.valueOf(request.getAttribute("hostName"));
        createSignUpUsagePlan(comID.getCompanyId(), isCurrencyGBP, (usersSize != null ? usersSize : 4), hostName, pricingPackageNAME);
        //Set google services access TOKEN
        if (newCompany.getGoogleAccessToken() != null) {
            loginServiceLocal.registrGoogleServices(newCompany.getAdminEmail(), newCompany.getGoogleAccessToken());
        }

        AuthDetails authDetails = new AuthDetails(comID.getCompanyId(), comID.getAdminId(), ServerSecurityContext.getInstance().getDatabase());
        authDetails.setUserAgent(request.getHeader("user-agent"));
        authDetails.setIpAddress(ServerUtils.obtainClientIP(request));

        if (newCompany.getIndustry() != null) {
            EdsReference reference = referenceManager.getByCode(newCompany.getIndustry());
            if (reference != null) {
                payrollService.saveIndustrySettings(reference.getObjectID().toString());
            }
        }

        if (TemplateSchema.GYM.equals(templateSchema)) {
            sessionService.obtainSession(authDetails);
            CompanyInitTO companyInitTO = new CompanyInitTO();
            companyInitTO.setCompanyName(newCompany.getName());
            companyInitTO.setLanguage(newCompany.getLocale());
            companyInitTO.setTimezone("1");
            companyInitTO.setOrgType("gym");
            companyInitTO.setModules(List.of("projects", "humans", "sales", "accounts"));
            companyService.gymCompanyInit(companyInitTO);

            AccountingInitTO accountingInitTO = new AccountingInitTO();
            accountingInitTO.setConversionDateMonthStr(11);
            accountingInitTO.setConversionDateYearStr("2023");
            if (newCompany.getCurrencyID() != null) {
                EdsCurrency currency = currencyManager.getCurrency(newCompany.getCurrencyID());
                accountingInitTO.setCurrencyCode(currency.getName());
            }
            accountingInitTO.setIndustryId(-1);
            accountingInitTO.setAccountingTool("-1");
            accountingInitTO.setStateId(1040);
            accountingInitTO.setModules(List.of(
                    "SALES_QUOTES",
                    "SALES_ORDERS",
                    "RECURRING_INVOICES",
                    "TIMESHEET_INVOICES",
                    "FIXED_ASSETS",
                    "PURCHASE_ORDERS",
                    "RECURRING_BILLS",
                    "INVENTORY_MANAGEMENT",
                    "REQUEST_FOR_QUOTES",
                    "REQUEST_FOR_PURCHASES"));
            companyService.gymAccountingInit(accountingInitTO);

            if (newCompany.getCurrencyID() != null) {
                FinancialSettingsItem settings = accountingService.getCompanyFinancialSettings();
                boolean existsCurrency = Optional.ofNullable(newCompany.getCurrencyID())
                        .map(currecyService::getCurrency)
                        .isPresent();
                if (existsCurrency && !settings.isHasTransaction()) {
                    settings.setCurrencyId(newCompany.getCurrencyID());
                }
                accountingService.saveFinancialSettings(settings);
            }
        }

        ModelAndView mav = new ModelAndView();
        mav.getModel().put("authDetails", authDetails);
        return mav;
    }

    private void importCompanyDefaultWebsite(String schemaName, boolean activira) {
        ServerSecurityContext.getInstance().setDatabase(DATABASE_FREE);
        ListingFilterParameter listingFilterParameter = new ListingFilterParameter();
        listingFilterParameter.setParentID(0);
        listingFilterParameter.setCompaines(new Integer[]{Integer.valueOf(schemaName)});
        if (activira)
            listingFilterParameter.setURL(ACTIVRA_PUBLIC_WORKSPACE);
        else
            listingFilterParameter.setURL(KPI_PUBLIC_WORKSPACE);
        backendService.activeDraggableWorkspace(listingFilterParameter);
    }

    //Request Handler Methods END


    /**
     * Continues sign up process
     * Checks if the user already exists, and suggests to create a new company or login
     *
     * @param request
     * @param response
     * @param newCompany
     * @return
     * @throws Exception
     */
    public ModelAndView continueSignUp(HttpServletRequest request, HttpServletResponse response, NewCompany newCompany) throws Exception {
        ServerSecurityContext.getInstance().setDatabase(DATABASE_FREE);
        todoContinueSignUp(request, newCompany);

        //validate the request that whether trying to attack or trying to sign up from fake domain
        if (!signupServiceLocal.validateForSecurity(newCompany)) {
            return new ModelAndView("unableToProcess");
        }

        //check user for existance
        String password;
        if (newCompany.getRegistrationType() == null || RegistrationTypeEnum.EMAIL.equals(newCompany.getRegistrationType())) {
            password = userManager.findActiveAndNonFederateLoginUsers(request.getServerName(), newCompany.getAdminEmail());
        } else {
            password = userManager.findActiveAndNonFederateLoginUsers(request.getServerName(), newCompany.getSocialUserName());
        }
        Object defaultLocale = request.getAttribute("defaultLocale");
        if (request.getAttribute("locale") != null) {
            defaultLocale = request.getAttribute("locale");
        }
        if (newCompany.getLocale() == null || newCompany.getLocale().equals("")) {
            newCompany.setLocale(defaultLocale.toString());
        }
        if (StringUtils.isNotEmpty(password)) {
            String viewName = "existingUser";
            if (Utils.isNewKpi(request)) {
                viewName = "existingUserNewKpi";
            }
            ModelAndView model = new ModelAndView(viewName);
            model.addObject(BIND_OBJECT_NAME, newCompany);
            return model;
        }
        //create company and entering the system
        TemplateSchema templateSchema;
        if (Utils.isBrain(request)) {
            String industry = newCompany.getIndustry();
            Integer employeeCount = newCompany.getEmployeeCount();
            String schemaPostfix = "";
            if (!TemplateSchema.OTHER.toString().equals(industry)) {
                schemaPostfix = employeeCount > 50 ? "_CLASSIC" : "_SIMPLIFIED";
            }
            templateSchema = TemplateSchema.of(industry + schemaPostfix);
        } else {
            templateSchema = TemplateSchema.getSchema(newCompany.getAdminFName(), request.getHeader("host"));
        }
        return registerCompany(request, response, newCompany, templateSchema);
    }

    private void todoContinueSignUp(HttpServletRequest request, NewCompany newCompany) throws Exception {
        //obtain company data
        //IP address of client
        String clientIPAdderss = request.getHeader("X-FORWARDED-FOR"); //RETURNS IP ADDRESS OF CLIENT SYSTEM
        if (clientIPAdderss == null) {
            clientIPAdderss = request.getRemoteAddr();
        }
        newCompany.setClientSingUpIPAddress(clientIPAdderss);
        //StickyService type
        if (request.getParameter("service") != null) {
            newCompany.setSignedUpPage(ServletRequestUtils.getStringParameter(request, "service"));
        } else {
            newCompany.setSignedUpPage(MY_WORKSPACE);
        }
        newCompany.setActive(true);
    }

    protected BindingResult errors;

    public BindingResult getErrors() {
        return errors;
    }

    public void setErrors(BindingResult errors) {
        this.errors = errors;
    }


    /**
     * Bind request parameters onto the given command bean
     *
     * @param request request from which parameters will be bound
     * @param command command object, that must be a JavaBean
     * @throws Exception in case of invalid state or arguments
     */
    protected void bind(HttpServletRequest request, Object command) throws Exception {

    }

    /**
     * Method will validate form fields and reject to errors
     *
     * @param command binded object
     */
    protected boolean validate(Object command, HttpServletRequest request) {

        if (errors == null) {
            ServletRequestDataBinder binder = new ServletRequestDataBinder(command, getCommandName(command));
            binder.bind(request);
            errors = binder.getBindingResult();
        }
        SignUpValidator signUpValidator = new SignUpValidator();
        ValidationUtils.invokeValidator(signUpValidator, command, errors);

        // Full email validation
        Object value = errors.getFieldValue("adminEmail");

        if (value == null || !org.springframework.util.StringUtils.hasText(value.toString())) {
            errors.rejectValue("adminEmail", "EMAIL_REQUIRED", EmailAddressValidator.EMAIL_REQUIRED);
        } else {
            String emailValidateMessage = fullEmailValidate(request);
            if (emailValidateMessage.equals(EmailAddressValidator.EMAIL_VALID)) {
                boolean isDomainBanned = bannedDomainServiceLocal.areEmailAndDomainBanned(((String) value));
                if (isDomainBanned) {
                    errors.rejectValue("adminEmail", "EMAIL_BANNED", "We are unable to process your request now. Please try again after sometime or contact  " + EdsContextParams.getSupportEmail() != null ? EdsContextParams.getSupportEmail() : "support@kpi.com");
                }
            } else {
                errors.rejectValue("adminEmail", "EMAIL_INVALID", emailValidateMessage);
            }
        }

        Object adminFName = errors.getFieldValue("adminFName");
        if (value != null && !StringUtils.isBlank(adminFName.toString())) {
            String name = adminFName.toString().trim();
            if (!Pattern.compile(VALID_NAME_REGEX).matcher(name).matches()) {
                errors.rejectValue("adminFName", "NAME_INVALID", "Name is invalid.");
            }
        }
        return errors.hasErrors();
    }

    protected boolean validateSecurity(NewCompany newCompany) {
        String ipAdress = newCompany.getClientSingUpIPAddress();
        if (StringUtils.isNotBlank(ipAdress)) {

        }
        return false;
    }

    protected String shortEmailValidate(HttpServletRequest request) {
        String result = " ";
        String email = request.getParameter("adminEmail");
        try {
            if (!EmailAddressValidator.checkEmail(email)) {
                result = EmailAddressValidator.EMAIL_INVALID;
            } else if (!EmailAddressValidator.checkHost(email)) {
                result = EmailAddressValidator.EMAIL_SUCH_NOT_EXSIST;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    protected String fullEmailValidate(HttpServletRequest request) {
        String result = EmailAddressValidator.EMAIL_VALID;
        String email = request.getParameter("adminEmail");
        try {
            if (!EmailAddressValidator.checkEmail(email)) {
                result = EmailAddressValidator.EMAIL_INVALID;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    protected String getCommandName(Object command) {
        return BIND_OBJECT_NAME;
    }

    protected void createSignUpUsagePlan(Integer companyId, boolean isCurrencyGBP, int usersCount, String hostName, String pricingPackageNAME) {
        signupServiceLocal.createFreeTrialUsagePlan(companyId, isCurrencyGBP, usersCount, hostName, pricingPackageNAME);
    }
}
