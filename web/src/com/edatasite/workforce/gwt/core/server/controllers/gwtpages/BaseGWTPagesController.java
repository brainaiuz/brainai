package com.edatasite.workforce.gwt.core.server.controllers.gwtpages;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.core.domain.EdsModule;
import com.edatasite.workforce.core.domain.EdsModuleLocalize;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.core.domain.settings.EdsGenericSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.AuthUtils;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import com.edatasite.workforce.gwt.core.server.db.ModuleLocalizeManager;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.PermissionManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CompanyPayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.rpc.LoggingInUser;
import com.edatasite.workforce.gwt.core.server.rpc.UserSignUPSessionID;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.DOCUMENTS_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.PAYROLL_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.PM_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.REPORTING_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.TC_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.WORKSPACE_MAIN_MENU;

/**
 * Created by IntelliJ IDEA.
 * User: Jonibek
 * Date: May 6, 2009
 * Time: 4:46:26 PM
 */
//@Controller
public abstract class BaseGWTPagesController implements Constants {

    @Autowired
    private AccountingService accountingService;
    @Autowired
    @Qualifier("loginService")
    protected LoginServiceLocal loginServiceLocal;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private LeaveReasonManager leaveReasonManager;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private ModuleLocalizeManager moduleLocalizeManager;
    @Autowired
    @Qualifier("wfmLocalizer")
    private WfmMessageSource wfmLocalizer;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private PermissionManager permissionManager;
    @Autowired
    private CompanyPayrollSettingsManager companyPayrollSettingsManager;


    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerUtils.fillHostParameters(request);
        String link = request.getParameter(BITLY);
        if (StringUtils.isNotBlank(link)) {
            return new ModelAndView("redirect:" + request.getRequestURI() + "?link=" + EncryptionHelper.decryptURL(link));
        }
        AuthDetails authDetails = AuthUtils.parseRequest(request, response);
        UserSignUPSessionID signedUser;

        if (authDetails != null) {
            ServerSecurityContext.getInstance().setCompanyId(authDetails.getCompanyID());
            ServerSecurityContext.getInstance().setDatabase(authDetails.getDatabase());
            LoggingInUser loggingUser = loginServiceLocal.getLoggingUser(authDetails.getUserID());

            boolean SHOULD_BE_ACTIVATED = (!loggingUser.isActivated()) && (loggingUser.isCompanyActivated()) && (!loggingUser.isDeleted());

            if (SHOULD_BE_ACTIVATED) {
                SessionService sessionService = ApplicationContextProvider.applicationContext.getBean(SessionService.class);
                String sessionId = sessionService.obtainSession(authDetails);
                ServerSecurityContext.getInstance().setDummySessionId(sessionId);
                signedUser = loginServiceLocal.getSignedUser();
                setAllCookies(request, response, signedUser);
                ServerUtils.removeCookie(Constants.SERVICE_ID_COOKIE, response);
                return new ModelAndView("redirect:password/changePassword.html");
            } else {
                ServerUtils.setUserSessionid(request);//if sessionID exists in cookies
                signedUser = loginServiceLocal.getSignedUser();
            }
            if (signedUser == null || (!signedUser.getUserId().equals(authDetails.getUserID()) && !signedUser.getCompanyId().equals(authDetails.getCompanyID()))) {
                setAllCookies(request, response, signedUser);
                ServerUtils.removeCookie(Constants.SERVICE_ID_COOKIE, response);
                ServerSecurityContext.getInstance().removeCompanyId();
                return new ModelAndView("redirect:/mainLogin?action=customLogin");
            }
        } else {
            ServerUtils.setUserSessionid(request);//if sessionID exists in cookies
            signedUser = loginServiceLocal.getSignedUser();
        }
//        String protocol = request.getHeader("X-Forwarded-Proto") != null ? request.getHeader("X-Forwarded-Proto") : request.getScheme();
        if (signedUser == null) {
            //else if authDetails is null, to put link in cookies
            setAllCookies(request, response, signedUser);
//            response.sendRedirect(protocol + "://" + request.getServerName() + ":" + request.getServerPort());

            ServerUtils.removeCookie(Constants.SERVICE_ID_COOKIE, response);
            return new ModelAndView("redirect:index.html");
        } else {
            AuthDetails shadowAuthDetails = AuthUtils.parseShadowRequest(request, signedUser.getUserId());
            if (shadowAuthDetails != null && !signedUser.getCompanyId().equals(shadowAuthDetails.getCompanyID())) {
                UserCompanyDTO userCompanyDTO = new UserCompanyDTO(signedUser.getUserId(), shadowAuthDetails.getCompanyID());
                setAllCookies(request, response, signedUser);

                return new ModelAndView("redirect:" + ServerUtils.getWebURL(userCompanyDTO));
            }
        }
        //Update UserSessionTrack to track which section user used and what parameters were in the link content.
        if (!signedUser.isHasAccess()) {
//            response.sendRedirect(protocol + "://" + request.getServerName() + ":" + request.getServerPort());
            ServerUtils.removeCookie(Constants.SERVICE_ID_COOKIE, response);
            return new ModelAndView("redirect:index.html");
        }
        // create session track
        Integer userSessionTrackId = updateUserSessionTrack(signedUser.getSessionID(), request);

        if (!signedUser.getCompanyActive()) {
            if (signedUser.getAdmin()) {
                return new ModelAndView("redirect:Myaccount.html");
            } else {
                return new ModelAndView("redirect:accountExpiration.html");
            }
        } else {  // otherwise proceed cookie
            setSectionHTMLCookie(request, response);
        }
        if (signedUser.isAnyDataMissing()) {
            return new ModelAndView("gettingStarted");
        }

        List<FileResource> attachments = attachmentUtilsManager.getAttachments(Constants.F_WHITE_LABEL_LOGO, EdsContextParams.getHostSetting(request.getServerName()).getObjectID(), EdsContextParams.getHostSetting(request.getServerName()).getObjectID());
        if (attachments != null && attachments.size() > 0) {
            FileResource fileResource = attachments.get(0);
            request.setAttribute("logoLink", fileResource.getAmazonLink());
        }
        List<FileResource> favicon = attachmentUtilsManager.getAttachments(Constants.F_WHITE_LABEL_FAVICON, EdsContextParams.getHostSetting(request.getServerName()).getObjectID(), EdsContextParams.getHostSetting(request.getServerName()).getObjectID());
        if (favicon != null && favicon.size() > 0) {
            FileResource fileResource = favicon.get(0);
            request.setAttribute("favIcon", fileResource.getAmazonLink());
        }
        setModulesData(request);

        String uri = request.getRequestURI();
        String page = uri.substring(uri.lastIndexOf("/") + 1, uri.lastIndexOf("."));

        String reportcategoryid = ServerUtils.getCookieVal("reportcategoryid", request.getCookies());

        String userLocale = signedUser.getLanguageForUser();

        if (userLocale == null) {
            userLocale = EdsContextParams.getDefaultLocale(request.getServerName()).getLanguage();
        }

        Locale preferredLocale;
        int underscore = userLocale.indexOf('_');
        int dash = userLocale.indexOf('-');

        if (underscore != -1) {
            String language = userLocale.substring(0, underscore);
            String country = userLocale.substring(underscore + 1);
            preferredLocale = generateLocal(language, country);
        } else if (dash != -1) {
            preferredLocale = generateLocal(userLocale.substring(0, dash), userLocale.substring(dash + 1));
        } else {
            preferredLocale = new Locale(userLocale);
        }
        HttpSession session = request.getSession(true);

        if (session != null) {
            session.setAttribute(Constants.PREFERRED_LOCALE_KEY, preferredLocale);
            Config.set(session, Config.FMT_LOCALE, preferredLocale);
        }

        if (!signedUser.isAccountingIsSetup() && ACCOUNTING_PAGE.equalsIgnoreCase(page)) {
            return getAccountingSetupView(signedUser);
        } else if (!signedUser.isSalesIsSetup() && CRM_PAGE.equalsIgnoreCase(page)) {
            return getSalesSetupView(signedUser);
        } else if (reportcategoryid != null && !reportcategoryid.isEmpty()) {
            ServerUtils.removeCookie("reportcategoryid", "/", response);
            return getImportReportDataView(reportcategoryid);
        }

        //The loading page and GWT specific data
        //public data
        setRequestAttributes(request, signedUser, userSessionTrackId);

        return doHandleRequest(request, response);
    }

    private ModelAndView getAccountingSetupView(UserSignUPSessionID signedUser) {
        ModelAndView initView = new ModelAndView("accountinginit").addObject("errormsg", "Modules must not be empty.");
        initView.addObject("isGccCountry", GCC_COUNTRIES.contains(signedUser.getCompanyCountryCode()));
        initView.addObject("isUk", UK.equals(signedUser.getCompanyCountryCode()));
        initView.addObject("isSA", SA.equals(signedUser.getCompanyCountryCode()));
        initView.addObject("VatCountry", VAT_COUNTRIES.contains(signedUser.getCompanyCountryCode()));
        initView.addObject("countryName", signedUser.getCountryName());
        initView.addObject("countryCode", signedUser.getCompanyCountryCode());
        initView.addObject("industries", ServerUtils.getAsSelectItem(referenceManager.listReferences(_COMPANY_INDUSTRY), ServerUtils.REFERENCE));
        initView.addObject("fullname", signedUser.getFullName());

        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        EdsCompany company = user.getCompany();

        if (company.getCountryZone() != null && company.getCountryZone().getCountry() != null) {
            initView.addObject("countryid", company.getCountryZone().getCountry().getObjectID());
            initView.addObject("states", commonService.getRegions(company.getCountryZone().getCountry().getObjectID()));
        }

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings != null && financialSettings.getConversionDate() != null) {
            initView.addObject("conversion_date", new SimpleDateFormat("yyyy-MM-dd").format(financialSettings.getConversionDate()));
            initView.addObject("conversion_date_year", new SimpleDateFormat("yyyy").format(financialSettings.getConversionDate()));
            initView.addObject("conversion_date_month", financialSettings.getConversionDate().getMonth());

            if (financialSettings.getCurrency() != null) {
                initView.addObject("companyCurrency", financialSettings.getCurrency().getName());
            }
        } else {
            initView.addObject("conversion_date_month", 0);
            initView.addObject("conversion_date_year", new SimpleDateFormat("yyyy").format(new Date()));
        }

        if (company != null && company.getBillingAddresses() != null && company.getBillingAddresses().size() > 0) {
            initView.addObject("address1", company.getBillingAddresses().get(0).getAddress());
            initView.addObject("address2", company.getBillingAddresses().get(0).getAddressb());
            initView.addObject("city", company.getBillingAddresses().get(0).getCity());
            initView.addObject("zip", company.getBillingAddresses().get(0).getZipCode());
        }
        if (company != null && company.getWorkArea() != null) {
            initView.addObject("industryid", company.getWorkArea().getObjectID());
        }
        if (company != null && StringUtils.isNotBlank(company.getAccountingTool())) {
            initView.addObject("accounting_tool", company.getAccountingTool());
        }
        initView.addObject("productname", EdsContextParams.getProductName());
        List<SelectItem> currencies = new ArrayList<>();
        for (EdsCurrency currency : currencyManager.getAllCurrency()) {
            currencies.add(new SelectItem(currency.getObjectID(), currency.getFullName(), currency.getName()));
        }
        initView.addObject("currencies", currencies.toArray(new SelectItem[]{}));
        final EdsCompanyPayrollSettings industrySettings = companyPayrollSettingsManager.getCompanySettingValue("INDUSTRY_ID");
        if (industrySettings != null && industrySettings.getValue() != null) {
            initView.addObject("industryid", Integer.valueOf(industrySettings.getValue()));
        }

        return initView;
    }

    private ModelAndView getImportReportDataView(String reportcategoryid) {
        ModelAndView initView = new ModelAndView("reportinginit");
        initView.addObject("reportcategoryid", reportcategoryid);
        return initView;
    }

    private void setModulesData(HttpServletRequest request) {
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        request.setAttribute("image", commonService.getImageUrl(user.getPhoto() != null ? user.getPhoto().getObjectID() : null));
        List<EdsModuleLocalize> moduleLocalizes = moduleLocalizeManager.listModuleLocalize();
        HashMap<String, String> moduleLocalizeMap = new HashMap<>();
        if (moduleLocalizes != null && !moduleLocalizes.isEmpty()) {
            for (EdsModuleLocalize moduleLocalize : moduleLocalizes) {
                moduleLocalizeMap.put(moduleLocalize.getModuleCode(), moduleLocalize.getName());
            }
        }
        List<String> permissions = new ArrayList<>(Arrays.asList(
                WORKSPACE_MAIN_MENU,
                ACCOUNTING_MAIN_MENU,
                CRM_MAIN_MENU,
                HRMS_MAIN_MENU,
                PM_MAIN_MENU,
                PAYROLL_MAIN_MENU,
                REPORTING_MAIN_MENU,
                DOCUMENTS_MAIN_MENU,
                TC_MAIN_MENU
        ));
        List<String> userHasPermissions = permissionManager.getPermissions(permissions, user);
        request.setAttribute(ModuleEnum.ACCOUNTING.getCode(), moduleLocalizeMap.get(ModuleEnum.ACCOUNTING.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.ACCOUNTING.getCode()) : wfmLocalizer.localize("accounts"));
        request.setAttribute(ModuleEnum.CRM.getCode(), moduleLocalizeMap.get(ModuleEnum.CRM.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.CRM.getCode()) : wfmLocalizer.localize("crm"));
        request.setAttribute(ModuleEnum.HRMS.getCode(), moduleLocalizeMap.get(ModuleEnum.HRMS.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.HRMS.getCode()) : wfmLocalizer.localize("hrms"));
        request.setAttribute(ModuleEnum.PM.getCode(), moduleLocalizeMap.get(ModuleEnum.PM.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.PM.getCode()) : wfmLocalizer.localize("projects"));
        request.setAttribute(ModuleEnum.PAYROLL.getCode(), moduleLocalizeMap.get(ModuleEnum.PAYROLL.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.PAYROLL.getCode()) : wfmLocalizer.localize("payroll"));
        request.setAttribute(ModuleEnum.REPORTING.getCode(), moduleLocalizeMap.get(ModuleEnum.REPORTING.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.REPORTING.getCode()) : wfmLocalizer.localize("reports"));
        request.setAttribute(ModuleEnum.DOCUMENTS.getCode(), moduleLocalizeMap.get(ModuleEnum.DOCUMENTS.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.DOCUMENTS.getCode()) : wfmLocalizer.localize("docs"));
        request.setAttribute(ModuleEnum.TC.getCode(), moduleLocalizeMap.get(ModuleEnum.TC.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.TC.getCode()) : wfmLocalizer.localize("trainingCenter"));
        request.setAttribute(WORKSPACE_MAIN_MENU, userHasPermissions.contains(WORKSPACE_MAIN_MENU));
        request.setAttribute(ACCOUNTING_MAIN_MENU, userHasPermissions.contains(ACCOUNTING_MAIN_MENU));
        request.setAttribute(CRM_MAIN_MENU, userHasPermissions.contains(CRM_MAIN_MENU));
        request.setAttribute(HRMS_MAIN_MENU, userHasPermissions.contains(HRMS_MAIN_MENU));
        request.setAttribute(PM_MAIN_MENU, userHasPermissions.contains(PM_MAIN_MENU));
        request.setAttribute(PAYROLL_MAIN_MENU, userHasPermissions.contains(PAYROLL_MAIN_MENU));
        request.setAttribute(REPORTING_MAIN_MENU, userHasPermissions.contains(REPORTING_MAIN_MENU));
        request.setAttribute(DOCUMENTS_MAIN_MENU, userHasPermissions.contains(DOCUMENTS_MAIN_MENU));
        request.setAttribute(TC_MAIN_MENU, userHasPermissions.contains(TC_MAIN_MENU));
    }

    private ModelAndView getSalesSetupView(UserSignUPSessionID signedUser) {
        ModelAndView initView = new ModelAndView("salesinit").addObject("errormsg", "Modules must not be empty.");
        initView.addObject("industries", ServerUtils.getAsSelectItem(referenceManager.listReferences(_COMPANY_INDUSTRY), ServerUtils.REFERENCE));
        initView.addObject("fullname", signedUser.getFullName());

        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();

        EdsCompany company = companyManager.getUser().getCompany();
        if (company != null && company.getBillingAddresses() != null && company.getBillingAddresses().size() > 0) {
            initView.addObject("address1", company.getBillingAddresses().get(0).getAddress());
            initView.addObject("address2", company.getBillingAddresses().get(0).getAddressb());
            initView.addObject("city", company.getBillingAddresses().get(0).getCity());
            initView.addObject("zip", company.getBillingAddresses().get(0).getZipCode());
            if (company.getBillingAddresses().get(0).getCountry() != null) {
                initView.addObject("countryid", company.getBillingAddresses().get(0).getCountry().getObjectID());
                initView.addObject("states", commonService.getRegions(company.getBillingAddresses().get(0).getCountry().getObjectID()));
            } else if (user.getCompany().getCountryZone() != null && user.getCompany().getCountryZone().getCountry() != null) {
                initView.addObject("countryid", user.getCompany().getCountryZone().getCountry().getObjectID());
                initView.addObject("states", commonService.getRegions(user.getCompany().getCountryZone().getCountry().getObjectID()));
            }
        } else if (user.getCompany().getCountryZone() != null && user.getCompany().getCountryZone().getCountry() != null) {
            initView.addObject("countryid", user.getCompany().getCountryZone().getCountry().getObjectID());
            initView.addObject("states", commonService.getRegions(user.getCompany().getCountryZone().getCountry().getObjectID()));
        }
        final EdsCompanyPayrollSettings industrySettings = companyPayrollSettingsManager.getCompanySettingValue("INDUSTRY_ID");
        if (industrySettings != null && industrySettings.getValue() != null) {
            initView.addObject("industryid", Integer.valueOf(industrySettings.getValue()));
        }
//        if (company != null && company.getWorkArea() != null) {
//            initView.addObject("industryid", company.getWorkArea().getObjectID());
//        }

        initView.addObject("roles", allInOneServiceLocal.getRoles().stream().filter(r -> ADMIN.equals(r.getId()) || SALESMAN.equals(r.getId()) || SALESPERSON.equals(r.getId())).collect(Collectors.toList()));
        initView.addObject("countries", commonService.getCountries());
        initView.addObject("productname", EdsContextParams.getProductName());

        return initView;
    }

    private void setRequestAttributes(HttpServletRequest request, UserSignUPSessionID signedUser, Integer userSessionTrackId) {
        request.setAttribute(USER_FULLNAME, signedUser.getFullName());
        request.setAttribute(USER_INITIALNAME, signedUser.getInitialName());
        request.setAttribute(FULL_NAME, signedUser.getFlexfullName());
        request.setAttribute(FIRST_NAME, signedUser.getFirstname());
        request.setAttribute(USER_NAME, signedUser.getUserName());
        request.setAttribute(EMAIL, signedUser.getEmail());
        request.setAttribute(COMPANY_ID, EncryptionHelper.encrypt(signedUser.getCompanyId().toString()));
        request.setAttribute(WITHOUT_ENCRYPTED_COMPANY_ID, signedUser.getCompanyId().toString());
        request.setAttribute(ACCESS_GRANTED, signedUser.getCompanyActive() ? TRUE : FALSE);
        request.setAttribute(COMPANY_NAME, signedUser.getCompanyName());
        request.setAttribute(SESSION_TRACK_ID, userSessionTrackId);
        request.setAttribute(USER_CITY, signedUser.getCityName());
        request.setAttribute(USER_COUNTRY, signedUser.getCountryName());
        request.setAttribute(IS_EMPLOYEE, signedUser.isEmployee());
        request.setAttribute(COMPANY_COUNTRY_CODE, signedUser.getCompanyCountryCode());
        request.setAttribute("cssVersion", System.getProperty("cssVersion"));
        if (signedUser.getAlternativeCalendarId() != null) {
            request.setAttribute(ALTERNATIVE_CALENDAR_ID, signedUser.getAlternativeCalendarId());
        }
        //Hash link
        String hashLink = ServerUtils.getCookieVal(HASH_LINK_COOKIE, request.getCookies());
        if (request.getParameter(LINK) != null) {
            String link = EncryptionHelper.decryptURL(request.getParameter(LINK));
            request.setAttribute(INITIAL_URL, link);
            System.out.print("------------------------------------INITIAL_URL is " + link);
        } else if (hashLink != null) {
            request.setAttribute(INITIAL_URL, EncryptionHelper.decryptURL(hashLink));
        }
        //GWT module specific data
        request.setAttribute(IS_SETUP_SUPPROJECT, signedUser.isSetupSubProject());
        request.setAttribute(IS_SETUP_SUPPROJECT_TWO_LEVEL, signedUser.isSetupSubProjectTwoLevel());
        request.setAttribute(PM_IS_SETUP, signedUser.isPmIsSetup() ? "true" : "false");//PM
        request.setAttribute(ACCOUNTING_IS_SETUP, signedUser.isAccountingIsSetup() ? "true" : "false");//accounting
        request.setAttribute(MULTI_COMPANY_SUBSIDIARY, signedUser.isMultiCompanySubsidiary() ? "true" : "false");//accounting
        request.setAttribute(MULTIWAREHOUSE_ENABLED, signedUser.isMultiWarehouseEnabled() ? "true" : "false");//accounting
        request.setAttribute(PRODUCTION_ENABLED, signedUser.isAccountingProductionEnabled() ? "true" : "false");//accounting
        request.setAttribute(PO_IGNORE_MANAGER_APPROVAL, signedUser.isPoIgnoreManagerApproval() ? "true" : "false");//accounting

        if (signedUser.getAccountingCalculationScale() != null) {
            request.setAttribute(ACCOUNTING_CALCULATION_SCALE, signedUser.getAccountingCalculationScale().toString());//accounting
        }
        if (signedUser.getAccountingCustomQtyScale() != null) {
            request.setAttribute(ACCOUNTING_CUSTOM_QUANTITY_SCALE, signedUser.getAccountingCustomQtyScale().toString());//accounting
        }
        if (signedUser.getAccountingCustomPriceScale() != null) {
            request.setAttribute(ACCOUNTING_CUSTOM_PRICE_SCALE, signedUser.getAccountingCustomPriceScale().toString());//accounting
        }
        if (signedUser.getAccountingCustomExRateScale() != null) {
            request.setAttribute(ACCOUNTING_CUSTOM_EXRATE_SCALE, signedUser.getAccountingCustomExRateScale().toString());//accounting
        }
        if (signedUser.getTransactionLockDate() != null) {
            request.setAttribute(TRANSACTION_LOCKING_DATE, signedUser.getTransactionLockDate());
        }
        request.setAttribute(TRANSACTION_LOCKING_SALES, signedUser.isSalesLocked() ? "true" : "false");
        request.setAttribute(TRANSACTION_LOCKING_PURCHASES, signedUser.isPurchasesLocked() ? "true" : "false");
        request.setAttribute(TRANSACTION_LOCKING_BANKING, signedUser.isBankingLocked() ? "true" : "false");
        request.setAttribute(TRANSACTION_LOCKING_EMPLOYEES, signedUser.isEmployeesLocked() ? "true" : "false");
        request.setAttribute(TRANSACTION_LOCKING_ATTENDANCE, signedUser.isAttendanceLocked() ? "true" : "false");
        request.setAttribute(TRANSACTION_LOCKING_RECRUITMENT, signedUser.isRecruitmentLocked() ? "true" : "false");
        request.setAttribute(TRANSACTION_LOCKING_PAYSLIPS, signedUser.isPayslipsLocked() ? "true" : "false");
        request.setAttribute(TRANSACTION_LOCKING_CASHADVANCES, signedUser.isCashAdvancesLocked() ? "true" : "false");
        request.setAttribute(TRANSACTION_LOCKING_ADDITIONALPAYMENTS, signedUser.isAdditionalPaymentsLocked() ? "true" : "false");

        if (signedUser.getAccountingTaxRateScalse() != null) {
            request.setAttribute(ACCOUNTING_TAX_RATE_SCALE, signedUser.getAccountingTaxRateScalse());
        }
        if (signedUser.getAccountingDiscountScale() != null) {
            request.setAttribute(ACCOUNTING_DISCOUNT_SCALE, signedUser.getAccountingDiscountScale());
        }
        if (signedUser.getAccountingProgressinvoiceingAmountScale() != null) {
            request.setAttribute(ACCOUNTING_PROGRESS_INVOICING_AMOUNT_SCALE, signedUser.getAccountingProgressinvoiceingAmountScale());
        }

        request.setAttribute(MONTHLY_TIMESHEET, signedUser.isEnableMonthlyTimesheet() ? "true" : "false");
        request.setAttribute(IS_SUPPLIER, signedUser.isSupplier() ? "true" : "false");//pm,accounting,workspace,documents
        request.setAttribute(IS_CLIENT_CONTACT, signedUser.isClientContact() ? "true" : "false");
        request.setAttribute(INVOICE_FIRST_VIEW, signedUser.getInvFirst() == null ? LANDING_PAGE : signedUser.getInvFirst());
        request.setAttribute(PM_FIRST_VIEW, signedUser.getPmFirst() == null ? LANDING_PAGE : signedUser.getPmFirst());
        request.setAttribute(PA_FIRST_VIEW, signedUser.getPaFirst() == null ? LANDING_PAGE : signedUser.getPaFirst());
        request.setAttribute(SHORT_DATE_FORMAT, signedUser.getShortDateFormat());
        request.setAttribute(LONG_DATE_FORMAT, signedUser.getLongDateFormat());
        request.setAttribute(GOOGLE_APP_DOMAIN, signedUser.getGoogleAppDomain());
        request.setAttribute(GOOGLE_MARKETPLACE_USERS_IMPORT_POPUP_SHOW, signedUser.isGoogleMarketplaceUsersImportShow() ? "true" : "false");
        request.setAttribute(FACEBOOK_API_KEY, EdsContextParams.getFacebookAPIKey());
        request.setAttribute(LINKEDIN_API_KEY, EdsContextParams.getLinkedinAPIKey());
        request.setAttribute(LINKEDIN_SECRET_KEY, EdsContextParams.getLinkedinSecret());
        request.setAttribute(IS_LIVE_ENVIRONMENT, EdsContextParams.isLiveEnvironment() ? "true" : "false");
        request.setAttribute(PAYPAL_ACCOUNT, EdsContextParams.getPaypalAccount());
        request.setAttribute(PRODUCT_NAME, EdsContextParams.getProductName());
        request.setAttribute(UPLOAD_DIR, EdsContextParams.getUploadDir() != null ? EdsContextParams.getUploadDir() : "");
        request.setAttribute(UPLOAD_TYPE, EdsContextParams.getUploadType() != null ? EdsContextParams.getUploadType() : "");
        request.setAttribute(CommandConstants.UPLOAD_TYPE_PARAM_NAME, EdsContextParams.getUploadTypeParam());
        request.setAttribute(PRODUCT_NAME, EdsContextParams.getProductName());
        request.setAttribute(HOST_NAME_VALUE, EdsContextParams.getHostname());
        request.setAttribute(VAT_RATE_VALUE, EdsContextParams.getVAT());
        request.setAttribute(HELP_HOST, EdsContextParams.getHelpHost());
        request.setAttribute(SUPPORT_EMAIL, EdsContextParams.getSupportEmail());
        request.setAttribute(PHONE, EdsContextParams.getPhone());
        request.setAttribute(ISAUTOMATIC, signedUser.isAutomatic());
        request.setAttribute(ISAUTOMATICAPPROVAL, signedUser.isAutomaticApproval());
        request.setAttribute(ISAUTOMATICWAITINGFORAPPROVAL, signedUser.isAutomaticWaitingForApproval());
        request.setAttribute(TIMESHEET_COMMENT_REQUIRED, signedUser.isTimesheetCommentRequired());
        request.setAttribute(VALIDATE_TASK_START, signedUser.isValidateTaskStart());
        request.setAttribute(VALIDATE_TASK_END, signedUser.isValidateTaskEnd());
        request.setAttribute(VALIDATE_HOLIDAY, signedUser.isValidateHoliday());
        request.setAttribute(VALIDATE_lEAVE_REQUEST, signedUser.isValidateLeaveRequest());
        request.setAttribute(VALIDATE_MAXIMUM_HOURS, signedUser.isValidateMaximumHours());
        request.setAttribute(VALIDATE_DAY_OFF, signedUser.isValidateDayOff());
        request.setAttribute(ANY_DATA_MISSING, signedUser.isAnyDataMissing());

        if (signedUser.isValidateMaximumHours()) {
            request.setAttribute(VALIDATE_TIMESLOT, signedUser.isValidateTimeslot());
            if (!signedUser.isValidateTimeslot()) {
                request.setAttribute(MAXIMUM_HOURS, signedUser.getMaximumHours());
            }
        }
        request.setAttribute(VALIDATE_PAST_TIMSHEET, signedUser.isValidatePastTimesheet());
        if (signedUser.isValidatePastTimesheet()) {
            request.setAttribute(PAST_TIMSHEET_DAYS, signedUser.getPastTimesheetDays());
        }
        request.setAttribute(VALIDATE_FUTURE_TIMESHEET, signedUser.isValidateFutureTimesheet());
        if (signedUser.isValidateFutureTimesheet()) {
            request.setAttribute(FUTURE_TIMESHEET_DAYS, signedUser.getFutureTimesheetDays());
        }
        request.setAttribute(TIMESHEET_WEEK_START, signedUser.getTimesheetWeekStart());
        request.setAttribute(OVERALL_DATE_PICKER_WEEK_START, signedUser.getOverallDatePickerWeekStart());
        request.setAttribute(SHOW_COMPLETED_TASKS, signedUser.isShowCompletedTasks());
        request.setAttribute(SHOW_HOUR_TYPE_DROPDOWN, signedUser.isShowHourTypeDropdown());
        request.setAttribute(ENABLE_MULTIPLE_TIMER_INTSTANCES, signedUser.isEnableMultipleTimerInstances());
        request.setAttribute(SAVE_TIMER_INTO_TIMESHEET_AUTOMATICALLY, signedUser.isSaveTimerIntoTimesheetAutomatically());
        request.setAttribute(MESSAGE_CENTER_ENABLED, signedUser.isMessageCenterEnabled());
        request.setAttribute(THEME_FOR_SYSTEM, signedUser.getThemeForSystem());
        request.setAttribute(LANGUAGE_FOR_USER, signedUser.getLanguageForUser());
        request.setAttribute(SIDE_NAV_STYLE, signedUser.getSideNavStyle());
        request.setAttribute(PROFILE_CONTENT, signedUser.getProfileContent());
        request.setAttribute(MODULE_PERMSISIONS, signedUser.getModulePermissions());
        request.setAttribute(LATEST_SERVER_UPLOAD_VERSION, System.getProperty("cssVersion")/*signedUser.getLatestUploadVersion()*/);
        request.setAttribute(SESSION_LENGTH, signedUser.getSessionLength());
        request.setAttribute(IS_ACTIVE_MEETING_MINUTES, signedUser.isMeetingMinutesActive() ? "true" : "false");
        request.setAttribute(FREE_TRIAL_DAYS_LEFT, signedUser.getFreeTrialDaysLeft());
        request.setAttribute(IS_PAID_COMPANY, signedUser.isPaidCompany());
        request.setAttribute(DEFAULT_CURRENCY_CODE, signedUser.getDefaultCurrencyCODE());
        request.setAttribute(IS_CLIENT, signedUser.isClient() ? "true" : "false");//more menu for other users(ADMIN, DR, HR, TL, PM, MEM, e.t.c.)
        request.setAttribute(ENABLE_SALES_BACKEND_FOR_USER, signedUser.isEnableSalesBackend() ? "true" : "false");//sales backend shown
        request.setAttribute(ENABLE_SUPPORT_BACKEND_FOR_USER, signedUser.isEnableSupportBackend() ? "true" : "false");//support backend shown
        request.setAttribute(ENABLE_ADMIN_BACKEND_FOR_USER, signedUser.isEnableAdminBackend() ? "true" : "false");//system backend shown
        request.setAttribute(ENABLE_PARTNER_ADMIN_BACKEND_FOR_USER, signedUser.isEnablePartnerAdminBackend() ? "true" : "false");//system backend shown
        request.setAttribute(ENABLE_DEVELOPER_BACKEND_FOR_USER, signedUser.isEnableDeveloperBackend() ? "true" : "false");//reporting backend shown

        request.setAttribute(SHOW_GOOGLE_TALK_CHAT, signedUser.getCompanySystemSettingsItem().isShowGoogleTalkChat() != null && signedUser.getCompanySystemSettingsItem().isShowGoogleTalkChat() ? "true" : "false");
        EdsModule resourcePlanning = moduleManager.getModuleByCode(PermissionConstants.RESOURCE_PLANNING);
        request.setAttribute(RESOURCE_UTILIZATION_ENABLED, resourcePlanning != null ? "true" : "false");//PM -> Resource utilization view enabled

        request.setAttribute(SHOW_SCORE_CALCULATION, signedUser.getCompanySystemSettingsItem().isShowScoreCalculation() ? "true" : "false");//Goal Management -> Add Goal show/hide score calculations
        request.setAttribute(CUSTOM_RATE_ENABLE, signedUser.getCompanySystemSettingsItem().isCustomRateEnable() ? "true" : "false");//initiate employee appraisal custom rates enable

        //Backendan module sectiondan  TRAINING_CENTER enabled qilish uchun qilindi
        EdsModule trainingCenterEnabled = moduleManager.getModuleByCode(PermissionConstants.TRAINING_CENTER);
        request.setAttribute(TRAINING_CENTER_ENABLED, trainingCenterEnabled != null ? "true" : "false");
        /////////////////
        EdsModule logisticsEnabled = moduleManager.getModuleByCode(PermissionConstants.LOGISTICS_MODULE);
        request.setAttribute(LOGISTICS, logisticsEnabled != null ? "true" : "false");
        EdsModule accountingEnabled = moduleManager.getModuleByCode(PermissionConstants.ACCOUNTING_MODULE);
        request.setAttribute(ACCOUNTING_MODULE, signedUser.isEnableAccountingModule() ? "true" : "false");

        /////////////////////
        //current employee timeSlot start/end time
        request.setAttribute(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_START_TIME, signedUser.getDefaultCurrentEmployeeTimeSlotStartTIME());
        request.setAttribute(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_END_TIME, signedUser.getDefaultCurrentEmployeeTimeSlotEndTIME());
        request.setAttribute(CUSTOM_TAX_NAME, signedUser.getTaxName());
        request.setAttribute(SUPER_USER, signedUser.isSuperUser());
        request.setAttribute(TIMESHEET_DF, signedUser.getTimesheetDateFormat());
        request.setAttribute(TIMESHEET_VALIDATE_EST, signedUser.isValidateTimesheetEstimate());

        request.setAttribute(ACCOUNTING_VAT_RETURN_REPORT, accountingService.getVatReturnReportVisibility());

        request.setAttribute(DOUBLE_MESSAGE_ENABLE, signedUser.isDoubleMessageEnable());
        request.setAttribute(MULTIPLE_SALES_PRICE_ENABLED, signedUser.isMultipleSalesPriceEnable());
        request.setAttribute(Constants.SETTINGS_ACCOUNTING_SETTINGS, signedUser.isAccountingSettingsEnabled() ? "true" : "false");
        request.setAttribute(ENABLE_SWITCHABLE_LAYOUT, signedUser.isEnableSwitchableLayout() ? "true" : "false");
        request.setAttribute(IS_TEST_COMPANY, signedUser.isTestCompany() ? "true" : "false");
        EdsLeaveReason reason = leaveReasonManager.findByCode(CustomFormConstants.LR_TYPE_ANNUAL_LEAVE);
        if (reason != null) {
            request.setAttribute(PRORATA_BASED_ANNUAL_LEAVE, reason.hasProrata() ? "true" : "false");
        }


        if (signedUser.getTawkToSiteId() != null) {
            request.setAttribute(TAWK_TO_SITE_ID, signedUser.getTawkToSiteId());
        }

        request.setAttribute(GenericSettingsEnum.EMPLOYEE_FORM_PERSONAL_ID.name(), genericSettingsManager.getValueByKey(GenericSettingsEnum.EMPLOYEE_FORM_PERSONAL_ID));
        request.setAttribute(FACEBOOK_APP_ID, EdsContextParams.getFacebookAppID());
        final String customImport = this.genericSettingsManager.getValueByKey(GenericSettingsEnum.PURCHASE_ORDER_CUSTOM_ITEM_RECIEVE_IMPORT);

        request.setAttribute(GenericSettingsEnum.PURCHASE_ORDER_CUSTOM_ITEM_RECIEVE_IMPORT.name(), "YES".equals(customImport));

        request.setAttribute(STRIPE_PUBLIC_KEY, EdsContextParams.getStripePublicKey() != null ? EdsContextParams.getStripePublicKey() : "");
        request.setAttribute(VAT_REGISTERED, signedUser.isVatRegistered());
        request.setAttribute(VAT_ACCOUNTING_BASIS, signedUser.getVatAccountingBasis());

        if (genericSettingsManager.getByKey(GenericSettingsEnum.MULTI_QUOTE_CONVERTER_GROUP_BY_ITEM_CONFIG) != null) {
            request.setAttribute(MULTI_QUOTE_CONVERTER_GROUP_BY_ITEM_CONFIG, genericSettingsManager.getByKey(GenericSettingsEnum.MULTI_QUOTE_CONVERTER_GROUP_BY_ITEM_CONFIG).getValue());
        }
        EdsGenericSettings enableAsterisk = genericSettingsManager.getByKey(GenericSettingsEnum.ENABLE_ASTERISK);
        if (enableAsterisk != null && StringUtils.isNotBlank(enableAsterisk.getValue())) {
            request.setAttribute(GenericSettingsEnum.ENABLE_ASTERISK.name(), enableAsterisk.getValue());
        } else {
            request.setAttribute(GenericSettingsEnum.ENABLE_ASTERISK.name(), "NO");
        }

        request.setAttribute(DASHBOARD_WIDGETS_MAX_LIMIT, "24");
        EdsGenericSettings dashboardWidgetMaxLimit = genericSettingsManager.getByKey(GenericSettingsEnum.DASHBOARD_WIDGETS_MAX_LIMIT);
        if (dashboardWidgetMaxLimit != null) {
            request.setAttribute(DASHBOARD_WIDGETS_MAX_LIMIT, dashboardWidgetMaxLimit.getValue());
        }
        request.setAttribute(EMAIL_ACCOUNT_SET_UP, signedUser.isEmailAccountSetup());
    }

    private void setSectionHTMLCookie(HttpServletRequest request, HttpServletResponse response) {
        String[] URIArray = request.getRequestURI().split("/");    //writing section into cookie such as Assessment.html
        Cookie sectionHtml = new Cookie(SECTION_HTML, URIArray[URIArray.length - 1]);
        sectionHtml.setPath("/");
        response.addCookie(sectionHtml);

    }

    private void setAllCookies(HttpServletRequest request, HttpServletResponse response, UserSignUPSessionID user) {

        //Section
        setSectionHTMLCookie(request, response);

        // Hash link
//        ServerUtils.removeCookie(HASH_LINK_COOKIE, response);
        ServerUtils.removeCookie(SERVICE_ID_COOKIE, response);
        if (request.getParameter(LINK) != null) {
            Cookie cookie = new Cookie(HASH_LINK_COOKIE, EncryptionHelper.encodeURL(request.getParameter(LINK))); //encoding a url again because it came within the request parameter value and decoded automaticly
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        //if user's context not null, set cookie to username and password
        if (user != null) {

            // Company Id
            Cookie companyIDcookie = new Cookie(HASH_COMPANYID_COOKIE, EncryptionHelper.encodeURL(user.getCompanyId().toString())); //encoding a url again because it came within the request parameter value and decoded automaticly
            companyIDcookie.setPath("/");
            response.addCookie(companyIDcookie);

            // User Id
            Cookie userIdCookie = new Cookie(USER_ID, EncryptionHelper.encodeURL(user.getUserId().toString())); //encoding a url again because it came within the request parameter value and decoded automaticly
            userIdCookie.setPath("/");
            response.addCookie(userIdCookie);

            // username
            Cookie userLogin = new Cookie(USER_NAME_COOKIE, EncryptionHelper.encodeURL(EncryptionHelper.encrypt(user.getUserName())));
            userLogin.setPath("/");
            response.addCookie(userLogin);

            // sessionId
            Cookie sessionCookie = new Cookie(SESSION_ID_COOKIE, user.getSessionID());
            //We are making SESSION_ID cookie visible for all multisubdomains
            /*if(!"localhost".equalsIgnoreCase(request.getServerName())) {
                sessionCookie.setDomain("." + request.getServerName());
            }*/
            response.addCookie(sessionCookie);
        }
    }

    private Integer updateUserSessionTrack(String sessionID, HttpServletRequest request) {
        String[] URIArray = request.getRequestURI().split("/");
        String section = "";
        if (URIArray.length > 1) {
            section = URIArray[URIArray.length - 1];
        }
        String params = "";
        if (request.getParameter(LINK) != null) {
            params = request.getParameter(LINK);
        }
        if (sessionID != null) {
            return loginServiceLocal.updateUserSessionTrack(sessionID, section, params);
        }
        return null;
    }

    public abstract ModelAndView doHandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception;

    private Locale generateLocal(String language, String country) {
        return new Locale(language, country);
    }
}
