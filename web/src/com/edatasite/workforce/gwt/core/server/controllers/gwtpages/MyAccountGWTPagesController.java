package com.edatasite.workforce.gwt.core.server.controllers.gwtpages;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsModuleLocalize;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.UiSettings;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StatusServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.ModuleLocalizeManager;
import com.edatasite.workforce.gwt.core.server.db.PermissionManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.rpc.UserSignUPSessionID;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
 * User: Anvarbek
 * Date: 09.05.2009
 * Time: 20:22:05
 */
@Controller
public class MyAccountGWTPagesController implements Constants {

    @Autowired
    private StatusServiceLocal statusServiceLocal;
    @Autowired
    private LoginServiceLocal loginServiceLocal;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private ModuleLocalizeManager moduleLocalizeManager;
    @Autowired
    @Qualifier("wfmLocalizer")
    private WfmMessageSource wfmLocalizer;
    @Autowired
    private CommonServiceLocal commonService;
    @Autowired
    private PermissionManager permissionManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;

    private static Logger log = LoggerFactory.getLogger(MyAccountGWTPagesController.class);

    @RequestMapping(value = "/Myaccount.html", method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerUtils.setUserSessionid(request);
        ServerUtils.fillHostParameters(request);
        List<FileResource> logo = attachmentUtilsManager.getAttachments(Constants.F_WHITE_LABEL_LOGO, EdsContextParams.getHostSetting(request.getServerName()).getObjectID(), EdsContextParams.getHostSetting(request.getServerName()).getObjectID());
        if (logo != null && logo.size() > 0) {
            FileResource fileResource = logo.get(0);
            request.setAttribute("logoLink", fileResource.getAmazonLink());
        }
        List<FileResource> favicon = attachmentUtilsManager.getAttachments(Constants.F_WHITE_LABEL_FAVICON, EdsContextParams.getHostSetting(request.getServerName()).getObjectID(), EdsContextParams.getHostSetting(request.getServerName()).getObjectID());
        if (favicon != null && favicon.size() > 0) {
            FileResource fileResource = favicon.get(0);
            request.setAttribute("favIcon", fileResource.getAmazonLink());
        }
        setModulesData(request);
        if (ServerSecurityContext.getInstance().getUser() == null) {
            log.info("Session is null, therefore redirected to index.html");
            /*if (request.getServerName().contains("localhost")) {
                response.sendRedirect("http://" + request.getServerName() + ":" + request.getServerPort());
            } else {
                response.sendRedirect("http://" + request.getServerName());
            }*/
            return new ModelAndView("redirect:index.html");
        }
        String uri = request.getRequestURI();
        String page = uri.substring(uri.lastIndexOf("/") + 1, uri.lastIndexOf("."));
        request.setAttribute(ACTIVE_MENU, page);
        UserSignUPSessionID user = loginServiceLocal.getSignedUser();

        String userLocale = user.getLanguageForUser();
        if (userLocale == null) {
            userLocale = EdsContextParams.getDefaultLocale(request.getServerName()).getLanguage();
        }

        Locale preferredLocale = new Locale(userLocale, "", "");
        HttpSession session = request.getSession(true);

        if (session != null) {
            session.setAttribute(Constants.PREFERRED_LOCALE_KEY, preferredLocale);
            Config.set(session, Config.FMT_LOCALE, preferredLocale);
        }
        //The loading page specific data in case of Logging in
//        request.setAttribute(USER_ID, user.getUserId());
        request.setAttribute(USER_FULLNAME, user.getFullName());
        request.setAttribute(USER_INITIALNAME, user.getInitialName());
        //request.setAttribute(EMPLOYEE_NUMBER, user.getEmployeeNumber());
        request.setAttribute(USER_AVAILABILITY, statusServiceLocal.getUserStatus(user.getUserId()));
        request.setAttribute(COMPANY_NAME, user.getCompanyName());
        request.setAttribute(COMPANY_ID, EncryptionHelper.encrypt(user.getCompanyId().toString()));
        request.setAttribute(ACCESS_GRANTED, user.getCompanyActive() ? TRUE : FALSE);
        request.setAttribute(LOCALE, user.getLocaleString());
        request.setAttribute(FIRST_NAME, user.getFirstname());

        request.setAttribute(PM_IS_SETUP, user.isPmIsSetup() ? "true" : "false");//PM
        request.setAttribute(ACCOUNTING_IS_SETUP, user.isAccountingIsSetup() ? "true" : "false");//accoun
        request.setAttribute(INVOICE_FIRST_VIEW, user.getInvFirst() == null ? LANDING_PAGE : user.getInvFirst());
        request.setAttribute(PM_FIRST_VIEW, user.getPmFirst() == null ? LANDING_PAGE : user.getPmFirst());
        request.setAttribute(PA_FIRST_VIEW, user.getPaFirst() == null ? LANDING_PAGE : user.getPaFirst());
        request.setAttribute(SHORT_DATE_FORMAT, user.getShortDateFormat());
        request.setAttribute(LONG_DATE_FORMAT, user.getLongDateFormat());
        request.setAttribute(GOOGLE_APP_DOMAIN, user.getGoogleAppDomain());
        request.setAttribute(GOOGLE_MARKETPLACE_USERS_IMPORT_POPUP_SHOW, user.isGoogleMarketplaceUsersImportShow() ? "true" : "false");
        request.setAttribute(FACEBOOK_API_KEY, EdsContextParams.getFacebookAPIKey());
        request.setAttribute(LINKEDIN_API_KEY, EdsContextParams.getLinkedinAPIKey());
        request.setAttribute(LINKEDIN_SECRET_KEY, EdsContextParams.getLinkedinSecret());
        request.setAttribute(IS_LIVE_ENVIRONMENT, EdsContextParams.isLiveEnvironment() ? "true" : "false");
        request.setAttribute(PAYPAL_ACCOUNT, EdsContextParams.getPaypalAccount());
        request.setAttribute(STRIPE_PUBLIC_KEY, EdsContextParams.getStripePublicKey()!=null ? EdsContextParams.getStripePublicKey() : "");
        request.setAttribute(PRODUCT_NAME, EdsContextParams.getProductName());
        request.setAttribute(UPLOAD_DIR, EdsContextParams.getUploadDir() != null ? EdsContextParams.getUploadDir() : "");
        request.setAttribute(UPLOAD_TYPE, EdsContextParams.getUploadType() != null ? EdsContextParams.getUploadType() : "");
        request.setAttribute(CommandConstants.UPLOAD_TYPE_PARAM_NAME, EdsContextParams.getUploadTypeParam());
        request.setAttribute(HOST_NAME_VALUE, EdsContextParams.getHostname());
        request.setAttribute(VAT_RATE_VALUE, EdsContextParams.getVAT());
        request.setAttribute(HELP_HOST, EdsContextParams.getHelpHost());
        request.setAttribute(SUPPORT_EMAIL, EdsContextParams.getSupportEmail());
        request.setAttribute(PHONE, EdsContextParams.getPhone());
        request.setAttribute(COMPANY_COUNTRY_CODE, user.getCompanyCountryCode());
        request.setAttribute(ISAUTOMATIC, user.isAutomatic());
        request.setAttribute(ISAUTOMATICAPPROVAL, user.isAutomaticApproval());
        request.setAttribute(ISAUTOMATICWAITINGFORAPPROVAL, user.isAutomaticWaitingForApproval());
        request.setAttribute(VALIDATE_TASK_START, user.isValidateTaskStart());
        request.setAttribute(VALIDATE_TASK_END, user.isValidateTaskEnd());
        request.setAttribute(VALIDATE_MAXIMUM_HOURS, user.isValidateMaximumHours());
        request.setAttribute(VALIDATE_DAY_OFF, user.isValidateDayOff());
        request.setAttribute("cssVersion", System.getProperty("cssVersion"));
        if (user.isValidateMaximumHours()) {
            request.setAttribute(VALIDATE_TIMESLOT, user.isValidateTimeslot());
            if (!user.isValidateTimeslot()) {
                request.setAttribute(MAXIMUM_HOURS, user.getMaximumHours());
            }
        }
        request.setAttribute(VALIDATE_PAST_TIMSHEET, user.isValidatePastTimesheet());
        if (user.isValidatePastTimesheet()) {
            request.setAttribute(PAST_TIMSHEET_DAYS, user.getPastTimesheetDays());
        }
        request.setAttribute(VALIDATE_FUTURE_TIMESHEET, user.isValidateFutureTimesheet());
        if (user.isValidateFutureTimesheet()) {
            request.setAttribute(FUTURE_TIMESHEET_DAYS, user.getFutureTimesheetDays());
        }
        request.setAttribute(VALIDATE_HOLIDAY, user.isValidateHoliday());
        request.setAttribute(VALIDATE_lEAVE_REQUEST, user.isValidateLeaveRequest());
        request.setAttribute(TIMESHEET_WEEK_START, user.getTimesheetWeekStart());
        request.setAttribute(OVERALL_DATE_PICKER_WEEK_START, user.getOverallDatePickerWeekStart());
        request.setAttribute(SHOW_COMPLETED_TASKS, user.isShowCompletedTasks());
        request.setAttribute(SHOW_HOUR_TYPE_DROPDOWN, user.isShowHourTypeDropdown());
        request.setAttribute(ENABLE_MULTIPLE_TIMER_INTSTANCES, user.isEnableMultipleTimerInstances());
        request.setAttribute(SAVE_TIMER_INTO_TIMESHEET_AUTOMATICALLY, user.isSaveTimerIntoTimesheetAutomatically());
        request.setAttribute(MESSAGE_CENTER_ENABLED, user.isMessageCenterEnabled());
        request.setAttribute(THEME_FOR_SYSTEM, user.getThemeForSystem());
        request.setAttribute(LANGUAGE_FOR_USER, user.getLanguageForUser());
        request.setAttribute(LATEST_SERVER_UPLOAD_VERSION, user.getLatestUploadVersion());
        request.setAttribute(FREE_TRIAL_DAYS_LEFT, user.getFreeTrialDaysLeft());
        request.setAttribute(IS_PAID_COMPANY, user.isPaidCompany());
        request.setAttribute(DEFAULT_CURRENCY_CODE, user.getDefaultCurrencyCODE());
        request.setAttribute(ENABLE_SALES_BACKEND_FOR_USER, user.isEnableSalesBackend() ? "true" : "false");//sales backend shown
        request.setAttribute(ENABLE_SUPPORT_BACKEND_FOR_USER, user.isEnableSupportBackend() ? "true" : "false");//support backend shown
        request.setAttribute(ENABLE_ADMIN_BACKEND_FOR_USER, user.isEnableAdminBackend() ? "true" : "false");//system backend shown
        request.setAttribute(ENABLE_PARTNER_ADMIN_BACKEND_FOR_USER, user.isEnablePartnerAdminBackend() ? "true" : "false");
//        request.setAttribute(ENABLE_PDF_BACKEND_FOR_USER, user.isEnablePDFBackend() ? "true" : "false");//pdf backend shown
        request.setAttribute(ENABLE_DEVELOPER_BACKEND_FOR_USER, user.isEnableDeveloperBackend() ? "true" : "false");//reporting backend shown
        //current employee timeSlot start/end time
        request.setAttribute(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_START_TIME, user.getDefaultCurrentEmployeeTimeSlotStartTIME());
        request.setAttribute(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_END_TIME, user.getDefaultCurrentEmployeeTimeSlotEndTIME());
        request.setAttribute(Constants.SETTINGS_ACCOUNTING_SETTINGS, user.isAccountingSettingsEnabled() ? "true" : "false");
        request.setAttribute(PRODUCTION_ENABLED, user.isAccountingProductionEnabled() ? "true" : "false");
        request.setAttribute(ENABLE_SWITCHABLE_LAYOUT, user.isEnableSwitchableLayout() ? "true" : "false");
        request.setAttribute(IS_TEST_COMPANY, user.isTestCompany() ? "true" : "false");
        request.setAttribute(MODULE_PERMSISIONS, user.getModulePermissions());
        if (user.getTawkToSiteId() != null) {
            request.setAttribute(TAWK_TO_SITE_ID, user.getTawkToSiteId());
        }

        request.setAttribute("theme", UiSettings.THEMES[0].getName()); //new-ui do not have color schema
        /*for (SelectItem o : UiSettings.THEMES) {
            if (o.getDescription().equals(user.getThemeForSystem())) {
                request.setAttribute("theme", o.getName());
                break;
            }
        }*/
        String enableMonthlyPlan = genericSettingsManager.getValueByKey(GenericSettingsEnum.ENABLE_MONTLY_PLAN);
        request.setAttribute(ENABLE_MONTLY_PLAN, StringUtils.isNotBlank(enableMonthlyPlan) ? enableMonthlyPlan : "NO");

        return new ModelAndView("/gwt-pages/myaccount");
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


}
