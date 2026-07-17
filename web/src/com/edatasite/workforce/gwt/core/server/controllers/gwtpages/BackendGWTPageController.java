package com.edatasite.workforce.gwt.core.server.controllers.gwtpages;

import com.edatasite.workforce.core.domain.EdsModuleLocalize;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.UiSettings;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ModuleLocalizeManager;
import com.edatasite.workforce.gwt.core.server.db.PermissionManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.rpc.UserSignUPSessionID;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
 * User: admin
 * Date: Jun 26, 2009
 * Time: 6:02:15 PM
 */
@Controller
public class BackendGWTPageController implements Constants {

    @Autowired
    private LoginService loginService;
    @Autowired
    private LoginServiceLocal loginServiceLocal;
    @Autowired
    EmployeeManager employeeManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private ModuleLocalizeManager moduleLocalizeManager;
    @Autowired
    @Qualifier("wfmLocalizer")
    private WfmMessageSource wfmLocalizer;
    @Autowired
    private CommonServiceLocal commonService;
    @Autowired
    private PermissionManager permissionManager;

    @RequestMapping(value = "/Backend.html", method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerUtils.setUserSessionid(request);
        ServerUtils.fillHostParameters(request);
        setModulesData(request);
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
        UserSignUPSessionID user = loginServiceLocal.getSignedUser();
        if (user == null) {
            proceedCookie(request, response);
            return new ModelAndView("redirect:index.html");
        }
        String uri = request.getRequestURI();
        if (true || ((user.isEnableSalesBackend() || user.isEnableSupportBackend() || user.isEnableAdminBackend() ||
                user.isEnablePartnerAdminBackend() || user.isEnableDeveloperBackend()) ||
                "lochin.shodiev@workforcetrack.com".equals(user.getUserName()) || "aknbdev".equals(user.getUserName())) && !user.isSuperUser()) {
            //The loading page and GWT specific data
            //public data
            request.setAttribute(USER_FULLNAME, user.getFullName());
            request.setAttribute(USER_INITIALNAME, user.getInitialName());
            request.setAttribute(FULL_NAME, user.getFlexfullName());
            request.setAttribute(USER_NAME, user.getUserName());
            request.setAttribute(COMPANY_ID, user.getCompanyId());//Left non-encrypted for backend
            request.setAttribute(ACCESS_GRANTED, user.getCompanyActive() ? TRUE : FALSE);
            request.setAttribute(COMPANY_NAME, user.getCompanyName());
            request.setAttribute(LOCALE, user.getLocaleString());
            request.setAttribute(SHORT_DATE_FORMAT, user.getShortDateFormat());
            request.setAttribute(LONG_DATE_FORMAT, user.getLongDateFormat());
            request.setAttribute(MESSAGE_CENTER_ENABLED, user.isMessageCenterEnabled());
            //GWT module specific data
            request.setAttribute(PM_IS_SETUP, user.isPmIsSetup() ? "true" : "false");//PM
            request.setAttribute(ACCOUNTING_IS_SETUP, user.isAccountingIsSetup() ? "true" : "false");//accoun
            request.setAttribute(ENABLE_SALES_BACKEND_FOR_USER, user.isEnableSalesBackend() ? "true" : "false");//sales backend shown
            request.setAttribute(ENABLE_SUPPORT_BACKEND_FOR_USER, user.isEnableSupportBackend() ? "true" : "false");//support backend shown
            request.setAttribute(ENABLE_ADMIN_BACKEND_FOR_USER, user.isEnableAdminBackend() ? "true" : "false");//system backend shown
            request.setAttribute(ENABLE_PARTNER_ADMIN_BACKEND_FOR_USER, user.isEnablePartnerAdminBackend() ? "true" : "false");//system backend shown
            request.setAttribute(ENABLE_DEVELOPER_BACKEND_FOR_USER, user.isEnableDeveloperBackend() ? "true" : "false");//reporting backend shown
            request.setAttribute(PROMOTIONAL_CODE, user.getPromotionalCode());
            request.setAttribute(HOST_NAME_VALUE, EdsContextParams.getHostname());
            request.setAttribute(PRODUCT_NAME, EdsContextParams.getProductName());
            request.setAttribute(VAT_RATE_VALUE, EdsContextParams.getVAT());
            request.setAttribute(HELP_HOST, EdsContextParams.getHelpHost());
            request.setAttribute(UPLOAD_DIR, EdsContextParams.getUploadDir() != null ? EdsContextParams.getUploadDir() : "");
            request.setAttribute(UPLOAD_TYPE, EdsContextParams.getUploadType() != null ? EdsContextParams.getUploadType() : "");
            request.setAttribute(CommandConstants.UPLOAD_TYPE_PARAM_NAME, EdsContextParams.getUploadTypeParam());
            request.setAttribute(FREE_TRIAL_DAYS_LEFT, user.getFreeTrialDaysLeft());
            request.setAttribute(IS_PAID_COMPANY, user.isPaidCompany());
            request.setAttribute(DEFAULT_CURRENCY_CODE, user.getDefaultCurrencyCODE());
            request.setAttribute(THEME_FOR_SYSTEM, user.getThemeForSystem());
            request.setAttribute(LANGUAGE_FOR_USER, user.getLanguageForUser());
            request.setAttribute(SIDE_NAV_STYLE, user.getSideNavStyle());
            request.setAttribute(TIMESHEET_WEEK_START, user.getTimesheetWeekStart());
            request.setAttribute(PROFILE_CONTENT, user.getProfileContent());
            request.setAttribute(MODULE_PERMSISIONS, user.getModulePermissions());
            request.setAttribute(OVERALL_DATE_PICKER_WEEK_START, user.getOverallDatePickerWeekStart());
            request.setAttribute(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_START_TIME, user.getDefaultCurrentEmployeeTimeSlotStartTIME());
            request.setAttribute(DEFAULT_CURRENT_EMPLOYEE_TIMESLOT_END_TIME, user.getDefaultCurrentEmployeeTimeSlotEndTIME());
            request.setAttribute(TRAINING_CENTER_ENABLED, user.isEnableTraningCenterView() ? "true" : "false");
            request.setAttribute(LOGISTICS, user.isEnableLogistics() ? "true" : "false");
            request.setAttribute(Constants.SETTINGS_ACCOUNTING_SETTINGS, user.isAccountingSettingsEnabled() ? "true" : "false");
            request.setAttribute(ENABLE_SWITCHABLE_LAYOUT, user.isEnableSwitchableLayout() ? "true" : "false");
            request.setAttribute(IS_TEST_COMPANY, user.isTestCompany() ? "true" : "false");
            request.setAttribute("cssVersion", System.getProperty("cssVersion"));
            request.setAttribute(ACCOUNTING_MODULE, user.isEnableAccountingModule() ? "true" : "false");

            if (user.getTawkToSiteId() != null) {
                request.setAttribute(TAWK_TO_SITE_ID, user.getTawkToSiteId());
            }
            String page = uri.substring(uri.lastIndexOf("/") + 1, uri.lastIndexOf("."));
            request.setAttribute(ACTIVE_MENU, page);

            request.setAttribute("theme", UiSettings.THEMES[0].getName()); //new-ui do not have color schema

            return new ModelAndView("gwt-pages/backend");
        }
        return new ModelAndView("redirect:ProjectManagement.html");

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

    private void proceedCookie(HttpServletRequest request, HttpServletResponse response) {
        String[] URIArray = request.getRequestURI().split("/");
        Cookie sectionHtml = new Cookie(SECTION_HTML, URIArray[URIArray.length - 1]);
        response.addCookie(sectionHtml);
    }
}
