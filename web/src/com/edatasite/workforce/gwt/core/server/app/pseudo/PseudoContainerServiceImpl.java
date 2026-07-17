package com.edatasite.workforce.gwt.core.server.app.pseudo;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsContainer;
import com.edatasite.workforce.core.domain.EdsContainerItem;
import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.core.domain.EdsModule;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.customform.EdsCustomForm;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.ModuleDashboardService;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.PseudoMenuItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.fakeContainer.PseudoContainerService;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ContainerItemManager;
import com.edatasite.workforce.gwt.core.server.db.ContainerManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.PermissionManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.OnboardingItem;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingCategoryRPC;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.edatasite.workforce.core.domain.EdsRole.ADMIN;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ASSEMBLY_PRODUCTS;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.BRIGADA;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.BRIGADA_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.BUILD_ASSEMBLY_PRODUCTS;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.GROUP_GOAL;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.GROUP_PLACEMENT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.POSITION1;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PROJECT_GOAL;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ROTATION;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SHIFT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SHIFT_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ADD_FINGERPRINT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_LIVE_REQUEST;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.PAYROLL_MULTI_CASH_ADVANCE_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.PAYROLL_RECURRING_PD_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.SETTINGS_ASTERISK_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.SETTINGS_TWILIO_LIST;


@Transactional
@Service("pseudoService")
public class PseudoContainerServiceImpl implements PseudoContainersServiceLocal, PseudoContainerService {

    @Autowired
    private PermissionManager permissionManager;
    @Autowired
    private ReportingService reportingService;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    @Qualifier("hrmsLocalizer")
    private WfmMessageSource hrmsLocalizer;
    @Autowired
    @Qualifier("crmLocalizer")
    private WfmMessageSource crmLocalizer;
    @Autowired
    @Qualifier("payrollLocalizer")
    private WfmMessageSource payrollLocalizer;
    @Autowired
    @Qualifier("accountingStrings")
    private WfmMessageSource accountingStrings;
    @Autowired
    @Qualifier("clientLocalizer")
    private WfmMessageSource clientLocalizer;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    @Qualifier("availabilityLocalizer")
    private WfmMessageSource availabilityLocalizer;
    @Autowired
    @Qualifier("inventoryLocalizer")
    private WfmMessageSource inventoryLocalizer;
    @Autowired
    @Qualifier("workspaceLocalizer")
    private WfmMessageSource workspaceLocalizer;
    @Autowired
    @Qualifier("profileStrings")
    private WfmMessageSource profileStrings;
    @Autowired
    @Qualifier("wfmLocalizer")
    private WfmMessageSource wfmLocalizer;
    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private LeaveReasonManager leaveReasonManager;
    @Autowired
    private ModuleDashboardService moduleDashboardService;
    @Autowired
    private PropertManager propertManager;
    @Autowired
    private ContainerManager containerManager;
    @Autowired
    private ContainerItemManager containerItemManager;
    @Autowired
    private CustomFormManager customFormManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    private CustomFormItemManager customFormItemManager;


    public ArrayList<PseudoMenuItem> getSettingsMenuItems() {
        ArrayList<PseudoMenuItem> result = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.SETTINGS_COMPANY_SETTINGS);
        permissions.add(PermissionConstants.SETTINGS_PROFILE_SETTINGS);
        permissions.add(PermissionConstants.SETTINGS_USER_CREDENTIALS);
        permissions.add(PermissionConstants.SETTINGS_ACCOUNTING_SETTINGS);
        permissions.add(PermissionConstants.SETTINGS_CRM_SETTINGS);
        permissions.add(PermissionConstants.SETTINGS_HRMS_SETTINGS);
        permissions.add(PermissionConstants.SETTINGS_PROJECT_MANAGEMENT_SETTINGS);
        permissions.add(PermissionConstants.PAYROLL_SETTINGS);
        permissions.add(PermissionConstants.SETTINGS_DASHBOARD_LIST);
        permissions.add(PermissionConstants.SETTINGS_CUSTOMIZATION);
        permissions.add(PermissionConstants.CUSTOM_FIELD_SETTINGS);
        permissions.add(PermissionConstants.SETTINGS_RECURRENCE_SETTINGS);
        permissions.add(PermissionConstants.SETTINGS_WORKFLOW);
        permissions.add(PermissionConstants.SETTINGS_EMAIL_SETTINGS);
        permissions.add(PermissionConstants.REFERENCE_LIST);
        permissions.add(PermissionConstants.SETTINGS_CUSTOMIZATION_REFERENCE);
        permissions.add(PermissionConstants.SYSTEM_LOGS);
        permissions.add(PermissionConstants.SETTINGS_COMPANY_EMAL_SETTINGS);
        permissions.add(PermissionConstants.SETTINGS_EMAIL_TEMPALTE_LIST);
        permissions.add(PermissionConstants.SETTINGS_INTEGRATION);
        permissions.add(PermissionConstants.ACCOUNTING_PRODUCT_TABLE_SETTINGS);
        permissions.add(PermissionConstants.KANBAN_ITEM_SETTINGS);
        permissions.add(PermissionConstants.PERMISSION_MANAGEMENT);
        permissions.add(PermissionConstants.SETTINGS_MANAGE_ROLE);
        permissions.add(PermissionConstants.SETTINGS_ROLE_LIST);
        permissions.add(PermissionConstants.SETTINGS_EMPLOYEE_LIST);

        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());

        if (!permissionManager.getUser().getCompany().getAnyDataMissing()) {
//            PseudoMenuItem settings = new PseudoMenuItem();

//            result.add(settings);

            if (permissions.contains(PermissionConstants.SETTINGS_COMPANY_SETTINGS)) {

                result.add(new PseudoMenuItem(profileStrings.localize("companySettings"), "companySettingsHome|companySettings"));
            }
            if (permissions.contains(PermissionConstants.SETTINGS_PROFILE_SETTINGS)) {

                result.add(new PseudoMenuItem(commonLocalizer.localize("contactprofile"), "profileSettings|profile"));
            }
            if (permissions.contains(PermissionConstants.SETTINGS_USER_CREDENTIALS)) {

                result.add(new PseudoMenuItem(profileStrings.localize("userCredentials"), "userCredential|credentials"));
            }
            if (permissions.contains(PermissionConstants.SETTINGS_ACCOUNTING_SETTINGS)) {
                PseudoMenuItem invoiceSettings = new PseudoMenuItem(commonLocalizer.localize("accounts"), "accountingSettings");
                invoiceSettings.setChildren(getInvoiceSettingsMenuItems());
                result.add(invoiceSettings);
            }
            if (permissions.contains(PermissionConstants.SETTINGS_CRM_SETTINGS)) {
                PseudoMenuItem crmSettings = new PseudoMenuItem(commonLocalizer.localize("crm"), "customFieldsSettings");
                crmSettings.setChildren(getCrmSettingsMenuItems());
                result.add(crmSettings);
            }

            if (permissions.contains(PermissionConstants.SETTINGS_HRMS_SETTINGS) || roleManager.hasRole(userManager.getUser(), ADMIN)) {
                //Hrms
                PseudoMenuItem hrmsSettings = new PseudoMenuItem(commonLocalizer.localize("hrms"), "hrmsSettings");
                hrmsSettings.setChildren(getHrmsSettingsMenuItems());
                result.add(hrmsSettings);
            }
            if (permissions.contains(PermissionConstants.SETTINGS_PROJECT_MANAGEMENT_SETTINGS)) {
                PseudoMenuItem pmSettings = new PseudoMenuItem(commonLocalizer.localize("projects"), "pmSettings");
                ArrayList<PseudoMenuItem> children = new ArrayList<>();
                children.add(new PseudoMenuItem(commonLocalizer.localize("numberingSettings"), "numberingsettings"));
                children.add(new PseudoMenuItem(profileStrings.localize("timesheetSettings"), "timesheetSettings"));
                pmSettings.setChildren(children);
                result.add(pmSettings);
            }
            if (permissions.contains(PermissionConstants.PAYROLL_SETTINGS)) {
                PseudoMenuItem settings = new PseudoMenuItem(commonLocalizer.localize("payroll"), "payrollSettings");
                settings.setChildren(getPayrollSettingsMenuItems());
                result.add(settings);
            }
            if (permissions.contains(PermissionConstants.SETTINGS_DASHBOARD_LIST)) {

                result.add(new PseudoMenuItem(wfmLocalizer.localize("dashboards"), "moduleDashboardSettings|moduleDashboard"));
            }

            if (permissions.contains(PermissionConstants.SETTINGS_CUSTOMIZATION)) {
                PseudoMenuItem customFieldSettings = new PseudoMenuItem(profileStrings.localize("customization"), "customizationSettings");

                ArrayList<PseudoMenuItem> items = new ArrayList<>();
                //Custom Fields
                PseudoMenuItem customFieldsItem = new PseudoMenuItem(profileStrings.localize("fieldCustomization"), "#");
                ArrayList<PseudoMenuItem> children = new ArrayList<>();
                children.add(new PseudoMenuItem(profileStrings.localize("crmCustomFields"), "crmcustomfields"));
                children.add(new PseudoMenuItem(profileStrings.localize("pmCustomFields"), "pmcustomfields"));
                children.add(new PseudoMenuItem(profileStrings.localize("hrmsCustomFields"), "hrmscustomfields"));
                children.add(new PseudoMenuItem(profileStrings.localize("accountingCustomFields"), "accountingcustomfields"));
                children.add(new PseudoMenuItem(profileStrings.localize("payrollCustomFields"), "payrollcustomfields"));
                children.add(new PseudoMenuItem(profileStrings.localize("settingsCustomFields"), "settingscustomfields"));
                customFieldsItem.setChildren(children);
                items.add(customFieldsItem);

                //Templates
                if (permissions.contains(PermissionConstants.SETTINGS_EMAIL_SETTINGS)) {
                    PseudoMenuItem emailSettings = new PseudoMenuItem(commonLocalizer.localize("templates"), "emailSettings");
                    children = new ArrayList<>();
                    if (permissions.contains(PermissionConstants.SETTINGS_COMPANY_EMAL_SETTINGS)) {
                        children.add(new PseudoMenuItem(profileStrings.localize("emailNotifications"), "companyEmailSettings"));
                    }
                    if (permissions.contains(PermissionConstants.SETTINGS_EMAIL_TEMPALTE_LIST)) {
                        children.add(new PseudoMenuItem(wfmLocalizer.localize("emailTemplates"), "emailTemplateList"));
                    }
                    if (permissions.contains(PermissionConstants.SETTINGS_EMAIL_TEMPALTE_LIST)) {
                        children.add(new PseudoMenuItem(wfmLocalizer.localize("smsTemplates"), "smsTemplateList"));
                    }
                    children.add(new PseudoMenuItem(wfmLocalizer.localize("signature"), "signatureList"));
                    children.add(new PseudoMenuItem(wfmLocalizer.localize("pdfTemplates"), "pdfTemplateList"));
                    emailSettings.setChildren(children);
                    items.add(emailSettings);
                }

                if (permissions.contains(PermissionConstants.SETTINGS_CUSTOMIZATION_REFERENCE)) {
                    items.add(new PseudoMenuItem(wfmLocalizer.localize("referencces"), "referenceList"));
                }
                items.add(new PseudoMenuItem(profileStrings.localize("organizeModules"), "organizeModuleList"));

                if (permissions.contains(PermissionConstants.ACCOUNTING_PRODUCT_TABLE_SETTINGS)) {
                    //items.add(new PseudoMenuItem(wfmLocalizer.localize("itemTable"), "itemtablesettings"));
                    items.add(new PseudoMenuItem(wfmLocalizer.localize("itemTable"), "itemtablesettingsdraggable"));
                }
                if (permissions.contains(PermissionConstants.KANBAN_ITEM_SETTINGS)) {
                    //items.add(new PseudoMenuItem(wfmLocalizer.localize("itemTable"), "itemtablesettings"));
                    items.add(new PseudoMenuItem(wfmLocalizer.localize("kanbanItemSettings"), "kanbanitemsettingsdraggable"));
                }
                items.add(new PseudoMenuItem(profileStrings.localize("quickAddSettings"), "quickaddsettingsdraggable"));
                customFieldSettings.setChildren(items);
                result.add(customFieldSettings);
            }

            if (permissions.contains(PermissionConstants.SETTINGS_WORKFLOW)) {
                PseudoMenuItem workflowSettings = new PseudoMenuItem(profileStrings.localize("automationSettings"), "workflowSettings");
                ArrayList<PseudoMenuItem> items = new ArrayList<>();

                if (permissions.contains(PermissionConstants.SETTINGS_RECURRENCE_SETTINGS)) {
                    EdsProperty property = propertManager.findByCode(Constants.TIMESHEET);

                    PseudoMenuItem recurrenceSettings = new PseudoMenuItem(commonLocalizer.localize("recurrences"), "recurrences");
                    ArrayList<PseudoMenuItem> children = new ArrayList<>();

                    String singular = property != null && property.getPlural() != null ? property.getPlural() : wfmLocalizer.localize("timesheet");
                    children.add(new PseudoMenuItem(format(wfmLocalizer.localize("timesheetReminder"), singular), "timesheetReminder"));
                    children.add(new PseudoMenuItem(format(profileStrings.localize("defaultTimesheetReminder"), singular), "timesheetReminderD"));
                    recurrenceSettings.setChildren(children);
                    items.add(recurrenceSettings);
                }

                items.add(new PseudoMenuItem(wfmLocalizer.localize("rules"), Constants.WORKFLOW_RULES_LIST));
                items.add(new PseudoMenuItem(profileStrings.localize("approvalProcess"), "approvalProcess"));
                workflowSettings.setChildren(items);
                result.add(workflowSettings);
            }

            if (permissions.contains(PermissionConstants.PERMISSION_MANAGEMENT)) {
                PseudoMenuItem permissionSettings = new PseudoMenuItem(profileStrings.localize("usersAndPrivileges"), "permissionSettings");
                ArrayList<PseudoMenuItem> children = new ArrayList<>();
                if (permissions.contains(PermissionConstants.SETTINGS_EMPLOYEE_LIST)) {
                    children.add(new PseudoMenuItem(commonLocalizer.localize("users"), Constants.HRMS_EMPLOYEES_LIST));
                }
                if (permissions.contains(PermissionConstants.SETTINGS_ROLE_LIST)) {
                    children.add(new PseudoMenuItem(wfmLocalizer.localize("roles"), "role"));
                }
                if (permissions.contains(PermissionConstants.SETTINGS_MANAGE_ROLE)) {
                    children.add(new PseudoMenuItem(wfmLocalizer.localize("permissions"), "rolePermission"));
                }
                permissionSettings.setChildren(children);
                result.add(permissionSettings);
            }

            if (!permissionManager.getUser().hasEitherRoles(Constants.ESS_USER_CODE) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP)) {
                PseudoMenuItem consolidationSettings = new PseudoMenuItem(profileStrings.localize("setupCompanySubsidiaries"), "consolidationSettings");
                ArrayList<PseudoMenuItem> children = new ArrayList<>();
                children.add(new PseudoMenuItem(profileStrings.localize("companyConsalidationList"), "consalidationList"));
                consolidationSettings.setChildren(children);
                result.add(consolidationSettings);
            }

            if (permissions.contains(PermissionConstants.SETTINGS_INTEGRATION)) {
                PseudoMenuItem integrationSettings = new PseudoMenuItem(wfmLocalizer.localize("integrations"), "integrationsSettings");
                ArrayList<PseudoMenuItem> items = new ArrayList<>();

                //Collaboration (Google, Microsoft)
                PseudoMenuItem collaborationSettings = new PseudoMenuItem(profileStrings.localize("collaboration", "collaboration"), "collaborationSettings");
                ArrayList<PseudoMenuItem> children = new ArrayList<>();
                children.add(new PseudoMenuItem("Google", "googleIntegration"));
                children.add(new PseudoMenuItem("Microsoft", "microsoftIntegration"));
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.INTEGRATED_WITH_TARGET)) {
                    children.add(new PseudoMenuItem(wfmLocalizer.localize("targetIntegration"), "integrationSettings"));
                }
                collaborationSettings.setChildren(children);
                items.add(collaborationSettings);

                //Communication
                PseudoMenuItem communicationSettings = new PseudoMenuItem(profileStrings.localize("communication"), "communicationSettings");
                children = new ArrayList<>();
                if (ServerUtils.hasPermission(SETTINGS_ASTERISK_LIST) || ServerUtils.hasPermission(SETTINGS_TWILIO_LIST)) {
                    children.add(new PseudoMenuItem(profileStrings.localize("telephony"), "telephonySettings"));
                }
                children.add(new PseudoMenuItem("Messengers", "messengerSettings"));
                //children.add(new PseudoMenuItem(settingsStrings.localize("twilioAccounts"), Constants.TWILIO_SETTINGS_LIST));
                children.add(new PseudoMenuItem("Switchvox", "switchvoxSettings"));
                children.add(new PseudoMenuItem(profileStrings.localize("smsAccounts"), Constants.SMS_SETTINGS_LIST));
                communicationSettings.setChildren(children);
                items.add(communicationSettings);

                //E-Commerce
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MAGENTO_INTEGRATION_ENABLE)) {
                    PseudoMenuItem eCommerceSettings = new PseudoMenuItem(commonLocalizer.localize("eCommerce"), "eCommerceSettings");
                    children = new ArrayList<>();
                    children.add(new PseudoMenuItem("Magento", "magento"));
                    children.add(new PseudoMenuItem("Shopify", "shopify"));

                    eCommerceSettings.setChildren(children);
                    items.add(eCommerceSettings);
                }

                //Time Management
                //PseudoMenuItem timeManagementSettings = new PseudoMenuItem(settingsStrings.localize("timeManagement"), "timeManagementSettings");
                //timeManagementSettings.setChildren(Collections.singletonList());
                //items.add(timeManagementSettings);

                //Payment Gateways
                items.add(new PseudoMenuItem(profileStrings.localize("onlinePaymentDetails"), "paypalPaymentAndStripePayment"));
                items.add(new PseudoMenuItem(profileStrings.localize("recruitment"), "recruitment"));
                items.add(new PseudoMenuItem(profileStrings.localize("documents"), "document"));

                //Fingerprint Setup
                if (ServerUtils.hasPermission(HRMS_ADD_FINGERPRINT)) {
                    items.add(new PseudoMenuItem(commonLocalizer.localize("fingerprintSetup"), Constants.FINGERPRINT_SETUP));
                }

                integrationSettings.setChildren(items);
                result.add(integrationSettings);
            }

            if (permissions.contains(PermissionConstants.SYSTEM_LOGS)) {
                PseudoMenuItem systemLogSettings = new PseudoMenuItem(profileStrings.localize("systemLogs"), "systemLogSettings");
                ArrayList<PseudoMenuItem> children = new ArrayList<>();
                children.add(new PseudoMenuItem(profileStrings.localize("importLogs"), "importLogs"));
                children.add(new PseudoMenuItem(profileStrings.localize("systemNotifications"), "systemMessages"));
                children.add(new PseudoMenuItem(profileStrings.localize("workflowNotifications"), "workflowMessages"));
                children.add(new PseudoMenuItem(wfmLocalizer.localize("upcomingactivities"), Constants.WORKFLOW_ACTIVITIES_LIST));
                children.add(new PseudoMenuItem(wfmLocalizer.localize("permissionManagement"), Constants.PERMISSION_HISTORY_LIST));
                systemLogSettings.setChildren(children);
                result.add(systemLogSettings);
            }
        } else {
            PseudoMenuItem companySettings = new PseudoMenuItem("Company Settings", "newsignupcompanysettings");
            ArrayList<PseudoMenuItem> children = new ArrayList<>();
            children.add(new PseudoMenuItem("Company Settings", "newsignupcompanysettings"));
            companySettings.setChildren(children);
            result.add(companySettings);
        }
        return result;
    }

    private ArrayList<PseudoMenuItem> getPayrollSettingsMenuItems() {
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.PAYROLL_SETTINGS_EMPLOYER_SETTINGS);
        permissions.add(PermissionConstants.PAYROLL_GROUP_LIST);
        permissions.add(PermissionConstants.PAYROLL_PAYMENT_DEDUCATION_LIST);
        permissions.add(PermissionConstants.PAYROLL_SETTINGS_PENSION_PROVIDERS);
        permissions.add(PermissionConstants.PAYROLL_SETTINGS_PENSION_SCHEMES);
        permissions.add(PermissionConstants.PAYROLL_SETTINGS_CATEGORIES_LIST);
//        permissions.add(PermissionConstants.PAYROLL_SETTINGS_PAYMENT_CATEGORIES);
//        permissions.add(PermissionConstants.PAYROLL_SETTINGS_DEDUCATION_CATEGORIES);
//        permissions.add(PermissionConstants.PAYROLL_SETTINGS_EMPLOYER_CONTRIBUTION);
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        if (permissions.contains(PermissionConstants.PAYROLL_SETTINGS_EMPLOYER_SETTINGS)) {
            children.add(new PseudoMenuItem(payrollLocalizer.localize("employerSettings"), "employersettings"));
        }
        if (permissions.contains(PermissionConstants.PAYROLL_GROUP_LIST)) {
            children.add(new PseudoMenuItem(payrollLocalizer.localize("payrollBatches"), Constants.PAYROLL_BATCH));
        }
        if (permissions.contains(PermissionConstants.PAYROLL_PAYMENT_DEDUCATION_LIST)) {
            children.add(new PseudoMenuItem(payrollLocalizer.localize("bulkUpdate"), Constants.PAYMENT_LIST));
        }
        if (permissions.contains(PermissionConstants.PAYROLL_SETTINGS_PENSION_PROVIDERS)) {
            children.add(new PseudoMenuItem(payrollLocalizer.localize("pensionProviders"), Constants.PENSION_PROVIDER_LIST));
        }
        if (permissions.contains(PermissionConstants.PAYROLL_SETTINGS_PENSION_SCHEMES)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("pensionScheme"), Constants.PENSION_SCHEME));
        }
        if (permissions.contains(PermissionConstants.PAYROLL_SETTINGS_CATEGORIES_LIST)) {
            children.add(new PseudoMenuItem(payrollLocalizer.localize("payrollCategories"), Constants.PAYROLL_CATEGORY_LIST));
        }
        /*if (permissions.contains(PermissionConstants.PAYROLL_SETTINGS_PAYMENT_CATEGORIES)) {
            children.add(new PseudoMenuItem(wfmLocalizer.localize("payment"), "paymentCategoryList"));
        }
        if (permissions.contains(PermissionConstants.PAYROLL_SETTINGS_DEDUCATION_CATEGORIES)) {
            children.add(new PseudoMenuItem(wfmLocalizer.localize("deduction"), "deductionCategoryList"));
        }

        if (permissions.contains(PermissionConstants.PAYROLL_SETTINGS_EMPLOYER_CONTRIBUTION)) {
            children.add(new PseudoMenuItem(payrollLocalizer.localize("employerContribution"), "employerContributionList"));
        }*/

        if (ServerUtils.isArabicCompany(permissionManager.getUser().getCompany())) {
            children.add(new PseudoMenuItem(payrollLocalizer.localize("endOfServiceSettings"), "endOfServiceSettings"));
        }
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SICK_LEAVE_SETTINGS_CALCULATION)) {
            children.add(new PseudoMenuItem(payrollLocalizer.localize("annualLeaveSettings"), "sickleavesettingsadd"));
            children.add(new PseudoMenuItem(payrollLocalizer.localize("dailyRateCalculation"), "dailyRateCalculation"));
        }
        children.add(new PseudoMenuItem(commonLocalizer.localize("numberingSettings"), "payrollnumberingform"));
        children.add(new PseudoMenuItem(commonLocalizer.localize("payrollZones"), "payrollZoneList"));
        children.add(new PseudoMenuItem(commonLocalizer.localize("minimumWage"), "minimumWageList"));
        children.add(new PseudoMenuItem(commonLocalizer.localize("wageRate"), "wageRateList"));

        return children;
    }

    private ArrayList<PseudoMenuItem> getHrmsSettingsMenuItems() {
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.HRMS_COMPANY_NEWS_CATEGORIES);
        permissions.add(PermissionConstants.CETIFICATE_TEMPLATE_LIST);
        permissions.add(PermissionConstants.REFERENCE_LIST);
        permissions.add(PermissionConstants.HRMS_ANNUAL_ALLOWANCE);
        permissions.add(PermissionConstants.BENEFIT_TYPE);
        permissions.add(PermissionConstants.HRMS_DEPARTMENT);
        permissions.add(PermissionConstants.HRMS_POSITION);
        permissions.add(PermissionConstants.HRMS_LOCATION);
        permissions.add(PermissionConstants.HRMS_VIEW_EMPLOYEE_CHANGE_LOG);
        permissions.add(PermissionConstants.SETTINGS_HRMS_SETTINGS);
        permissions.add(PermissionConstants.TIMESLOT_LIST);
        permissions.add(PermissionConstants.HOLIDAY_LIST);
        permissions.add(PermissionConstants.SETTINGS_BENEFIT_ALLOWANCE);
        permissions.add(PermissionConstants.SETTINGS_APPRAISAL_SETTINGS);
        permissions.add(PermissionConstants.SETTINGS_VALIDITY_PERIODS);
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        if (permissions.contains(PermissionConstants.SETTINGS_HRMS_SETTINGS)) {
            if (permissions.contains(PermissionConstants.TIMESLOT_LIST)) {
                children.add(new PseudoMenuItem(wfmLocalizer.localize("timeslot"), "timeslot"));
            }
            if (permissions.contains(PermissionConstants.HOLIDAY_LIST)) {
                children.add(new PseudoMenuItem(wfmLocalizer.localize("publicHolidays"), "holiday"));
            }
            if (permissions.contains(PermissionConstants.SETTINGS_APPRAISAL_SETTINGS)) {
                children.add(new PseudoMenuItem(wfmLocalizer.localize("appraisalsSettings"), "appraisalssettings"));
            }
            if (permissions.contains(PermissionConstants.SETTINGS_VALIDITY_PERIODS)) {
                children.add(new PseudoMenuItem(wfmLocalizer.localize("validityPeriods"), Constants.VALIDITY_PERIOD_LIST));
            }
            if (permissions.contains(PermissionConstants.HRMS_COMPANY_NEWS_CATEGORIES)) {
                children.add(new PseudoMenuItem(workspaceLocalizer.localize("newsCategories"), Constants.NEWS_CATEGORY_LIST));
            }
            if (permissions.contains(PermissionConstants.CETIFICATE_TEMPLATE_LIST)) {
                children.add(new PseudoMenuItem(wfmLocalizer.localize("hrLetterTemplates"), Constants.CERTIFICATE_TYPES_LIST));
            }
            if (permissions.contains(PermissionConstants.REFERENCE_LIST)) {
                children.add(new PseudoMenuItem(commonLocalizer.localize(PdfLocalizationName.leaveReasons), "leaveReasonList"));
            }
            if (permissions.contains(PermissionConstants.HRMS_ANNUAL_ALLOWANCE)) {
                children.add(new PseudoMenuItem(availabilityLocalizer.localize("annualLeaveAllowance"), Constants.ANNUAL_LEAVE_ALLOWANCE_LIST_VIEW));
            }
            if (permissions.contains(PermissionConstants.BENEFIT_TYPE)) {
                children.add(new PseudoMenuItem(wfmLocalizer.localize("benefitTypes"), Constants.BENEFITS));
            }
            if (permissions.contains(PermissionConstants.HRMS_DEPARTMENT)) {
                EdsProperty property = propertManager.findByCode(Constants.DEPARTMENT_LIST);
                String organizeDepartment = property != null && property.getPlural() != null ? property.getPlural() : wfmLocalizer.localize("departments");
                children.add(new PseudoMenuItem(organizeDepartment, Constants.DEPARTMENT_LIST));
            }
            if (permissions.contains(PermissionConstants.HRMS_POSITION)) {
                EdsProperty property = propertManager.findByCode(Constants.POSITION1);
                String positionPseudo = "";
                if (property != null) {
                    if (property.getlPlural() != null) {
                        positionPseudo = property.getlPlural().getNameLocalization(ServerUtils.getUserLocale().getLanguage());
                    } else if (!ServerUtils.isNullOrEmpty(property.getPlural())) {
                        positionPseudo = property.getPlural();
                    }
                }
                children.add(new PseudoMenuItem(ServerUtils.isNullOrEmpty(positionPseudo) ? commonLocalizer.localize("positions") : positionPseudo, "hrmsPositions"));
            }
            if (permissions.contains(PermissionConstants.HRMS_LOCATION)) {
                EdsProperty property = propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME);
                String locationPseudo = "";
                if (property != null) {
                    if (property.getlPlural() != null) {
                        locationPseudo = property.getlPlural().getNameLocalization(ServerUtils.getUserLocale().getLanguage());
                    } else if (!ServerUtils.isNullOrEmpty(property.getPlural())) {
                        locationPseudo = property.getPlural();
                    }
                }
                children.add(new PseudoMenuItem(ServerUtils.isNullOrEmpty(locationPseudo) ? commonLocalizer.localize("location") : locationPseudo, "location"));
            }
            if (permissions.contains(PermissionConstants.HRMS_VIEW_EMPLOYEE_CHANGE_LOG)) {
                children.add(new PseudoMenuItem(wfmLocalizer.localize("logHistory"), Constants.EMPLOYEE_UPDATES_LIST));
            }
            if (permissions.contains(PermissionConstants.SETTINGS_BENEFIT_ALLOWANCE)) {
                children.add(new PseudoMenuItem(hrmsLocalizer.localize("benefitAllowance"), Constants.EMPLOYEE_BENEFIT_ALLOWANCE_LIST_VIEW));
            }
        } else if (roleManager.hasRole(userManager.getUser(), ADMIN)) {
            //hrms_department
            EdsProperty property = propertManager.findByCode(Constants.DEPARTMENT_LIST);
            String organizeDepartment = property != null && property.getPlural() != null ? property.getPlural() : wfmLocalizer.localize("departments");
            children.add(new PseudoMenuItem(organizeDepartment, Constants.DEPARTMENT_LIST));
            //hrms_location
            children.add(new PseudoMenuItem(wfmLocalizer.localize("locations"), "location"));
            //hrms_position
            children.add(new PseudoMenuItem(wfmLocalizer.localize("positions"), "hrmsPositions"));
        }

        return children;
    }

    private ArrayList<PseudoMenuItem> getCrmSettingsMenuItems() {
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.CRM_CONTACT_CATEGORY_LIST);
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        children.add(new PseudoMenuItem(profileStrings.localize("salesSettings"), "CRMSettings"));
        children.add(new PseudoMenuItem(wfmLocalizer.localize("emailFilters"), "EMAIL_FILTER_LIST"));
        if (permissions.contains(PermissionConstants.CRM_CONTACT_CATEGORY_LIST)) {
            children.add(new PseudoMenuItem(crmLocalizer.localize("contactCategories"), "contactCategoryList"));
        }
        return children;
    }

    private ArrayList<PseudoMenuItem> getInvoiceSettingsMenuItems() {
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.ACCOUNTING_INVOICE_SETTINGS);
        permissions.add(PermissionConstants.ACCOUNTING_FINANCIAL_SETTINGS);
        permissions.add(PermissionConstants.ACCOUNTING_ACCOUNT_NUMBERING);
        permissions.add(PermissionConstants.ACCOUNTING_NUMBERING_SETTINGS);
        permissions.add(PermissionConstants.ACCOUNTING_CONVERSION_BALANCE);
        permissions.add(PermissionConstants.ACCOUNTING_TAX_RATES_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_CURRENCY_RATES_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_PRICE_LEVELS_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_TERMS_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_DISCOUNTS_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_UNIT_MEASUREMENTS_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_PRODUCT_CATEGORIES_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_BRANDS_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_SHIPPING_METHODS_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_PAYMENT_METHOD_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_TRANSACTION_LOOKING_LIST);
        //permissions.add(PermissionConstants.ACCOUNTING_PRODUCT_TABLE_SETTINGS);
        permissions.add(PermissionConstants.ACCOUNTING_ACCOUNT_LIST);
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        if (permissions.contains(PermissionConstants.ACCOUNTING_INVOICE_SETTINGS)) {
            children.add(new PseudoMenuItem(profileStrings.localize("invoiceSettings"), "invoiceSettings"));
        }
        if (permissions.contains(PermissionConstants.ACCOUNTING_FINANCIAL_SETTINGS)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("financialSettings"), "financialSettings"));
            if (permissions.contains(PermissionConstants.ACCOUNTING_TRANSACTION_LOOKING_LIST)) {
                children.add(new PseudoMenuItem(commonLocalizer.localize("transactionLocking"), "transactionLocking"));
            }
        }
        if (permissionManager.getUser().getCompany().getAccountingSetup()) {

            if (permissions.contains(PermissionConstants.ACCOUNTING_ACCOUNT_NUMBERING)) {
                children.add(new PseudoMenuItem(wfmLocalizer.localize("accounting"), "accounttypesettings"));
            }
        }
        if (permissions.contains(PermissionConstants.ACCOUNTING_NUMBERING_SETTINGS)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("numberingSettings"), "productnumberingsettings"));
        }
        if (permissions.contains(PermissionConstants.ACCOUNTING_CONVERSION_BALANCE)) {
            children.add(new PseudoMenuItem(wfmLocalizer.localize("conversionBalance"), "conversionBalance"));
        }
        if (permissions.contains(PermissionConstants.ACCOUNTING_TAX_RATES_LIST)) {
            children.add(new PseudoMenuItem(accountingStrings.localize("taxRates"), "texes"));
        }
        if (permissions.contains(PermissionConstants.ACCOUNTING_CURRENCY_RATES_LIST)) {
            children.add(new PseudoMenuItem(accountingStrings.localize("currencyRates"), "exrates"));
        }
        if (permissions.contains(PermissionConstants.ACCOUNTING_PRICE_LEVELS_LIST)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("priceLevel"), Constants.PRICE_LEVEL_LIST));
        }
        if (permissions.contains(PermissionConstants.ACCOUNTING_TERMS_LIST)) {
            children.add(new PseudoMenuItem(accountingStrings.localize("terms"), "terms"));
        }
        if (permissions.contains(PermissionConstants.ACCOUNTING_DISCOUNTS_LIST)) {
            children.add(new PseudoMenuItem(accountingStrings.localize("discounts"), Constants.DISCOUNT_LIST));
        }
        if (permissions.contains(PermissionConstants.ACCOUNTING_UNIT_MEASUREMENTS_LIST)) {
            children.add(new PseudoMenuItem(inventoryLocalizer.localize("measurements"), "unitmeasurementsList"));
        }
        if (permissions.contains(PermissionConstants.ACCOUNTING_PRODUCT_CATEGORIES_LIST)) {
            children.add(new PseudoMenuItem(inventoryLocalizer.localize("productCategories"), "productCategoriesList"));
        }
        if (permissions.contains(PermissionConstants.ACCOUNTING_BRANDS_LIST)) {
            children.add(new PseudoMenuItem(inventoryLocalizer.localize("brands"), "brandsList"));
        }
        if (permissions.contains(PermissionConstants.ACCOUNTING_SHIPPING_METHODS_LIST)) {
            children.add(new PseudoMenuItem(accountingStrings.localize("shippingMethods"), "shippintMethod"));
        }
        if (permissions.contains(PermissionConstants.ACCOUNTING_PAYMENT_METHOD_LIST)) {
            children.add(new PseudoMenuItem(accountingStrings.localize("paymentMethods"), "paymentmethodlist"));
        }
        if (permissions.contains(PermissionConstants.ACCOUNTING_ACCOUNT_LIST)) {
            children.add(new PseudoMenuItem(accountingStrings.localize("chartOfAccounts"), "accountList"));
        }
        return children;
    }

    private String getLocalizedName(String language, CustomFormLocalization localization, String defaultName) {
        if (localization != null) {
            return switch (language) {
                case "en" -> localization.getEnglishName();
                case "ar" -> localization.getArabicName();
                case "ru" -> localization.getRussianName();
                case "uz" -> localization.getUzbekName();
                default -> defaultName;
            };
        }
        return defaultName;
    }

    public ArrayList<PseudoMenuItem> getAccountingMenuItems(String moduleName) {
        ArrayList<PseudoMenuItem> result = new ArrayList<>();

        EdsUser user = userManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);

        List<EdsProperty> properties = propertManager.findByModuleCode(moduleName);
        Map<String, EdsProperty> propertyMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(properties)) {
            propertyMap = properties.stream().collect(Collectors.toMap(EdsProperty::getObjectName, x -> x));
        }

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        boolean isMultiWareHouseEnabled = financialSettings.getEnableMultiWarehouse();
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.ACCOUNTING_GETTING_STARTED_MENU);
        permissions.add(PermissionConstants.ACCOUNTING_ACCOUNTING_MENU);
        permissions.add(PermissionConstants.ACCOUNTING_TRANSACTION_MENU);
        permissions.add(PermissionConstants.ACCOUNTING_REPORTS_MENU);
        permissions.add(PermissionConstants.ACCOUNTING_WAREHOUSE_MENU);
        permissions.add(PermissionConstants.ACCOUNTING_SETTINGSE_EXCHANGE_RATE);
        EdsUser edsUser = permissionManager.getUser();
        if (edsUser != null) {
            permissions = permissionManager.getPermissions(permissions, edsUser);
        }
        if (permissions.contains(PermissionConstants.ACCOUNTING_GETTING_STARTED_MENU)) {
            PseudoMenuItem item = new PseudoMenuItem(accountingStrings.localize("gettingStarted"), "guide");
            ArrayList<PseudoMenuItem> children = new ArrayList<>();
            children.add(new PseudoMenuItem("pmguide", accountingStrings.localize("gettingStarted")));
            item.setChildren(children);
        } else {
            PseudoMenuItem dashboardContainer = getDashboardContainer(Constants.MODULE_ACCOUNTING);
            if (dashboardContainer != null) {
                result.add(dashboardContainer);
            }

            List<EdsContainer> containers = containerManager.getContainerBySorder(moduleName);
            if (containers != null && containers.size() > 0) {
                for (EdsContainer container : containers) {
                    PseudoMenuItem dynamicSinksContainer = new PseudoMenuItem(container.isChanged() ? (container.getLocalization() != null ? getLocalizedName(userSettings.getInternationalization(), container.getLocalization().getRPC(), container.getDefaultName()) : container.getDefaultName()) : commonLocalizer.localize(container.getDefaultName()), container.getCode());
                    dynamicSinksContainer.setChildren(createAccountingContainerChildren(container, propertyMap, userSettings.getInternationalization()));

                    switch (container.getCode()) {
                        case "accounting" -> {
                            if (permissions.contains((PermissionConstants.ACCOUNTING_ACCOUNTING_MENU))) {
                                result.add(dynamicSinksContainer);
                            }
                        }
                        case "transaction" -> {
                            if (permissions.contains((PermissionConstants.ACCOUNTING_TRANSACTION_MENU))) {
                                result.add(dynamicSinksContainer);
                            }
                        }
                        case "report" -> {
                            if (permissions.contains((PermissionConstants.ACCOUNTING_REPORTS_MENU))) {
                                result.add(dynamicSinksContainer);
                            }
                        }
                        case "warehouse" -> {
                            if (permissions.contains((PermissionConstants.ACCOUNTING_WAREHOUSE_MENU)) && isMultiWareHouseEnabled) {
                                result.add(dynamicSinksContainer);
                            }
                        }
                        default -> result.add(dynamicSinksContainer);
                    }
                }
            }
        }
        return result;
    }

    private ArrayList<PseudoMenuItem> createAccountingContainerChildren(EdsContainer container, Map<String, EdsProperty> propertyMap, String userLanguage) {

        List<EdsCustomForm> formList = getCustomFormList(ModuleEnum.ACCOUNTING);
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.ACCOUNTING_SALES_QUOTE_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_SALES_ORDER_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_SALES_INVOICE_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_RECURRING_INVOICE_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_REQUEST_FOR_QUOTE_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_REQUEST_FOR_PURCHASE_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_PURCHASE_ORDER_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_RECURRING_BILL_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_FIXED_ASSET_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_CUSTOMER_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_SUPPLIER_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_PRODUCT_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_INVENTORY_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_RENTAL_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_RENTAL_ORDER_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_ASSEMBLY_ITEM_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_BUILD_ASSEMBLY_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_TRASH_BIN_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_STOCK_ADJUSTMENT_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_CONSIGNMENT_LIST_VIEW);


        permissions.add(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT);
        permissions.add(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT);
        permissions.add(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_RECEIVE);
        permissions.add(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_SPEND);
        permissions.add(PermissionConstants.ACCOUNTING_PREPAYMENT_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_SUPPLIER_CREDIT_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_CHECK_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_MANUAL_JOURNAL_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_RECEIVE_PAYMENT_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_PAY_BILL_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_PURCHASE_ORDER_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_SALES_ORDER_LIST);

        permissions.add(PermissionConstants.ACCOUNTING_PROFIT_AND_LOSS);
        permissions.add(PermissionConstants.ACCOUNTING_BALANCE_SHEET);
        permissions.add(PermissionConstants.ACCOUNTING_TRIAL_BALANCE);
        permissions.add(PermissionConstants.ACCOUNTING_CASH_FLOW);
        permissions.add(PermissionConstants.ACCOUNTING_AGING_SUMMARY_RECEIVABLE);
        permissions.add(PermissionConstants.ACCOUNTING_AGING_SUMMARY_PAYABLE);
        permissions.add(PermissionConstants.ACCOUNTING_JOURNAL_REPORT);
        permissions.add(PermissionConstants.ACCOUNTING_ACCOUNT_TRANSACTIONS);
        permissions.add(PermissionConstants.ACCOUNTING_STOCK_VALUATION);
        permissions.add(PermissionConstants.ACCOUNTING_VAT_RETURN);
        permissions.add(PermissionConstants.ACCOUNTING_VAT_RETURNS_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_BUDGET_SHEET);

        permissions.add(PermissionConstants.ACCOUNTING_WAREHOUSE_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_STOCK_TRANSFER_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_GDN_LIST);
        permissions.add(PermissionConstants.ACCOUNTING_GRN_LIST);

        for (EdsCustomForm customForm : formList) {
            permissions.add(customForm.getFormID() + "_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_ADD_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_EDIT_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_SUMMARY_" + permissionManager.getUser().getCompany().getObjectID());
        }

        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        boolean isMultiWareHouseEnabled = financialSettings.getEnableMultiWarehouse();
        EdsModule logisticsEnabled = moduleManager.getModuleByCode(PermissionConstants.LOGISTICS_MODULE);
        boolean isLogisticsEnabled = logisticsEnabled != null;
        String localize;

        boolean isVatReturnEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.VAT_RETURN_ENABLE);

        if (financialSettings != null) {

            if (ServerUtils.isUAECompany(moduleManager.getUser().getCompany())) {
                isVatReturnEnabled = financialSettings.isVatRegistered();
            } else {
                isVatReturnEnabled = financialSettings.isShowVatReturnReport();
            }
        }

        String taxName = null;
        if (financialSettings.isShowVatNumberInListings() && financialSettings.getCustomVatName() != null) {
            taxName = (financialSettings.getCustomVatName());
        }


        List<EdsContainerItem> containerItemList = containerItemManager.getItemsByContainer(container.getObjectID(), false);
        if (containerItemList != null && containerItemList.size() > 0) {
            for (EdsContainerItem containerItem : containerItemList) {
                PropertyItem propertyItem = containerItem.toItem();
                if (propertyItem != null && propertyItem.getObjectName() != null) {
                    switch (propertyItem.getObjectName()) {
                        case Constants.SALE_QUOTE -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_SALES_QUOTE_LIST) && !isLogisticsEnabled) {
//                                localize = propertyMap.get(Constants.SALE_QUOTE) != null ? propertyMap.get(Constants.SALE_QUOTE).getPlural() : accountingStrings.localize("salesQuotes");
                                if (propertyMap != null && propertyMap.get(Constants.SALE_QUOTE) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.SALE_QUOTE);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.SALE_QUOTE);
                                } else {
                                    localize = wfmLocalizer.localize("salesQuotes");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.SALE_QUOTE));
                            }
                        }
                        case Constants.SALE_ORDER_CODE -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_SALES_ORDER_LIST)) {
//                                localize = propertyMap.get(Constants.SALE_ORDER_CODE) != null ? propertyMap.get(Constants.SALE_ORDER_CODE).getPlural() : accountingStrings.localize("salesOrders");
                                if (propertyMap != null && propertyMap.get(Constants.SALE_ORDER_CODE) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.SALE_ORDER_CODE);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.SALE_ORDER_CODE);
                                } else {
                                    localize = wfmLocalizer.localize("salesOrders");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.SALE_ORDER_CODE));
                            }
                        }
                        case Constants.SALE_INVOICE -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_SALES_INVOICE_LIST)) {
//                                localize = propertyMap.get(Constants.SALE_INVOICE) != null ? propertyMap.get(Constants.SALE_INVOICE).getPlural() : wfmLocalizer.localize("saleInvoices");
                                if (propertyMap != null && propertyMap.get(Constants.SALE_INVOICE) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.SALE_INVOICE);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.SALE_INVOICE);
                                } else {
                                    localize = wfmLocalizer.localize("saleInvoices");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.SALE_INVOICE));
                            }
                        }
                        case Constants.RECURRING_INVOICE -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_RECURRING_INVOICE_LIST)) {
//                                localize = propertyMap.get(Constants.RECURRING_INVOICE) != null ? propertyMap.get(Constants.RECURRING_INVOICE).getPlural() : accountingStrings.localize("recurringInvoice");
                                if (propertyMap != null && propertyMap.get(Constants.RECURRING_INVOICE) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.RECURRING_INVOICE);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.RECURRING_INVOICE);
                                } else {
                                    localize = wfmLocalizer.localize("recurringInvoice");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.RECURRING_INVOICE));
                            }
                        }
                        case Constants.REQUEST_FOR_QUOTE -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_REQUEST_FOR_QUOTE_LIST)) {
                                localize = propertyMap.get(Constants.REQUEST_FOR_QUOTE) != null ? propertyMap.get(Constants.REQUEST_FOR_QUOTE).getPlural() : accountingStrings.localize("requestForQuote");
                                children.add(new PseudoMenuItem(localize, Constants.REQUEST_FOR_QUOTE));
                            }
                        }
                        case Constants.REQUEST_FOR_PURCHASE -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_REQUEST_FOR_PURCHASE_LIST)) {
//                                localize = propertyMap.get(Constants.REQUEST_FOR_PURCHASE) != null ? propertyMap.get(Constants.REQUEST_FOR_PURCHASE).getPlural() : accountingStrings.localize("requestForPurchase");
                                if (propertyMap != null && propertyMap.get(Constants.REQUEST_FOR_PURCHASE) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.REQUEST_FOR_PURCHASE);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.REQUEST_FOR_PURCHASE);
                                } else {
                                    localize = wfmLocalizer.localize("requestForPurchase");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.REQUEST_FOR_PURCHASE));
                            }
                        }
                        case Constants.PURCHASE_ORDER -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_PURCHASE_ORDER_LIST) && !isLogisticsEnabled) {
//                                localize = propertyMap.get(Constants.PURCHASE_ORDER) != null ? propertyMap.get(Constants.PURCHASE_ORDER).getPlural() : accountingStrings.localize("purchaseOrders");
                                if (propertyMap != null && propertyMap.get(Constants.PURCHASE_ORDER) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.PURCHASE_ORDER);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.PURCHASE_ORDER);
                                } else {
                                    localize = wfmLocalizer.localize("purchaseOrders");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.PURCHASE_ORDER));
                            }
                        }
                        case Constants.PURCHASE_INVOICE -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_LIST) && !isLogisticsEnabled) {
//                                localize = propertyMap.get(Constants.PURCHASE_INVOICE) != null ? propertyMap.get(Constants.PURCHASE_INVOICE).getPlural() : accountingStrings.localize("purchaseInvoices");
                                if (propertyMap != null && propertyMap.get(Constants.PURCHASE_INVOICE) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.PURCHASE_INVOICE);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.PURCHASE_INVOICE);
                                } else {
                                    localize = wfmLocalizer.localize("purchaseInvoices");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.PURCHASE_INVOICE));
                            }
                        }
                        case Constants.RECURRING_BILL -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_RECURRING_BILL_LIST) && !isLogisticsEnabled) {
//                                localize = propertyMap.get(Constants.RECURRING_BILL) != null ? propertyMap.get(Constants.RECURRING_BILL).getPlural() : accountingStrings.localize("recurringBills");
                                if (propertyMap != null && propertyMap.get(Constants.RECURRING_BILL) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.RECURRING_BILL);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.RECURRING_BILL);
                                } else {
                                    localize = wfmLocalizer.localize("recurringBills");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.RECURRING_BILL));
                            }
                        }
                        case Constants.FIXED_ASSETS -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_FIXED_ASSET_LIST)) {
//                                localize = propertyMap.get(Constants.FIXED_ASSETS) != null ? propertyMap.get(Constants.FIXED_ASSETS).getPlural() : wfmLocalizer.localize("fixedAssets");
                                if (propertyMap != null && propertyMap.get(Constants.FIXED_ASSETS) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.FIXED_ASSETS);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.FIXED_ASSETS);
                                } else {
                                    localize = wfmLocalizer.localize("fixedAssets");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.FIXED_ASSETS));
                            }
                        }
                        case Constants.EXPENSES_CLAIM -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_LIST) || permissions.contains(PermissionConstants.ACCOUNTING_COMPANY_EXPENSE_LIST)) {
//                                localize = propertyMap.get(Constants.EXPENSES_CLAIM) != null ? propertyMap.get(Constants.EXPENSES_CLAIM).getPlural() : wfmLocalizer.localize("expenseClaims");
                                if (propertyMap != null && propertyMap.get(Constants.EXPENSES_CLAIM) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.EXPENSES_CLAIM);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.EXPENSES_CLAIM);
                                } else {
                                    localize = wfmLocalizer.localize("expenseClaims");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.EXPENSES_CLAIM));
                            }
                        }
                        case Constants.CLIENT_LIST -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_CUSTOMER_LIST)) {
//                                localize = propertyMap.get(Constants.CLIENT_LIST) != null ? propertyMap.get(Constants.CLIENT_LIST).getPlural() : clientLocalizer.localize("customers");
                                if (propertyMap != null && propertyMap.get(Constants.CLIENT_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.CLIENT_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.CLIENT_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("customers");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.CLIENT_LIST));
                            }
                        }
                        case Constants.SUPPLIER_LIST -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_SUPPLIER_LIST) && !isLogisticsEnabled) {
//                                localize = propertyMap.get(Constants.SUPPLIER_LIST) != null ? propertyMap.get(Constants.SUPPLIER_LIST).getPlural() : wfmLocalizer.localize("supplierCenter");
                                if (propertyMap != null && propertyMap.get(Constants.SUPPLIER_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.SUPPLIER_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.SUPPLIER_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("supplierCenter");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.SUPPLIER_LIST));
                            }
                        }
                        case Constants.PRODUCTS_OR_SERVICES -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_PRODUCT_LIST) && !isLogisticsEnabled) {
//                                localize = propertyMap.get(Constants.PRODUCTS_OR_SERVICES) != null ? propertyMap.get(Constants.PRODUCTS_OR_SERVICES).getPlural() : wfmLocalizer.localize("productsOrServices");
                                if (propertyMap != null && propertyMap.get(Constants.PRODUCTS_OR_SERVICES) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.PRODUCTS_OR_SERVICES);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.PRODUCTS_OR_SERVICES);
                                } else {
                                    localize = wfmLocalizer.localize("productsOrServices");
                                }
                                children.add(new PseudoMenuItem(localize, "productList"));
                            }
                        }
                        case Constants.INVENTORY_ITEMS -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_INVENTORY_LIST) && !isLogisticsEnabled) {
//                                localize = propertyMap.get(Constants.INVENTORY_ITEMS) != null ? propertyMap.get(Constants.INVENTORY_ITEMS).getPlural() : wfmLocalizer.localize("inventoryItems");
                                if (propertyMap != null && propertyMap.get(Constants.INVENTORY_ITEMS) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.INVENTORY_ITEMS);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.INVENTORY_ITEMS);
                                } else {
                                    localize = wfmLocalizer.localize("inventoryItems");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.INVENTORY_ITEMS));
                            }
                        }
                        case Constants.RENTAL_PRODUCTS -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_RENTAL_LIST)) {
//                                localize = propertyMap.get(Constants.RENTAL_PRODUCTS) != null ? propertyMap.get(Constants.RENTAL_PRODUCTS).getPlural() : wfmLocalizer.localize("rentalProducts");
                                if (propertyMap != null && propertyMap.get(Constants.RENTAL_PRODUCTS) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.RENTAL_PRODUCTS);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.RENTAL_PRODUCTS);
                                } else {
                                    localize = wfmLocalizer.localize("rentalProducts");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.RENTAL_PRODUCTS));
                            }
                        }
                        case ASSEMBLY_PRODUCTS -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_ASSEMBLY_ITEM_LIST)) {
                                if (propertyMap != null && propertyMap.get(ASSEMBLY_PRODUCTS) != null) {
                                    EdsProperty edsProperty = propertyMap.get(ASSEMBLY_PRODUCTS);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, ASSEMBLY_PRODUCTS);
                                } else {
                                    localize = wfmLocalizer.localize("assemblyItems");
                                }
                                children.add(new PseudoMenuItem(localize, ASSEMBLY_PRODUCTS));
                            }
                        }
                        case BUILD_ASSEMBLY_PRODUCTS -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_BUILD_ASSEMBLY_LIST)) {
                                if (propertyMap != null && propertyMap.get(BUILD_ASSEMBLY_PRODUCTS) != null) {
                                    EdsProperty edsProperty = propertyMap.get(BUILD_ASSEMBLY_PRODUCTS);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, BUILD_ASSEMBLY_PRODUCTS);
                                } else {
                                    localize = wfmLocalizer.localize("buildAssemblyItem"); // todo localization
                                }
                                children.add(new PseudoMenuItem(localize, BUILD_ASSEMBLY_PRODUCTS));
                            }
                        }
                        case Constants.RENTAL_ORDERS -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_RENTAL_ORDER_LIST)) {
//                                localize = propertyMap.get(Constants.RENTAL_ORDERS) != null ? propertyMap.get(Constants.RENTAL_ORDERS).getPlural() : wfmLocalizer.localize("rentalOrders");
                                if (propertyMap != null && propertyMap.get(Constants.RENTAL_ORDERS) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.RENTAL_ORDERS);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.RENTAL_ORDERS);
                                } else {
                                    localize = wfmLocalizer.localize("rentalOrders");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.RENTAL_ORDERS));
                            }
                        }
                        case Constants.BANKACCOUNT -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_LIST)) {
//                                localize = propertyMap.get(Constants.BANKACCOUNT) != null ? propertyMap.get(Constants.BANKACCOUNT).getPlural() : accountingStrings.localize("bankAccounts");
                                if (propertyMap != null && propertyMap.get(Constants.BANKACCOUNT) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.BANKACCOUNT);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.BANKACCOUNT);
                                } else {
                                    localize = wfmLocalizer.localize("bankAccounts");
                                }
                                children.add(new PseudoMenuItem(localize, "bankaccount"));
                            }
                        }
                        case Constants.TRASH_BIN -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_TRASH_BIN_LIST)) {
                                children.add(new PseudoMenuItem(commonLocalizer.localize("trashBin"), "trashBin"));
                            }
                        }
                        case "STOCK_ADJUSTMENT" -> {
                            if (propertyItem.getContainer() != null && "accounting".equals(propertyItem.getContainer().getCode())) {
                                if (!isMultiWareHouseEnabled && permissions.contains(PermissionConstants.ACCOUNTING_STOCK_ADJUSTMENT_LIST) && !isLogisticsEnabled) {
                                    if (propertyMap != null && propertyMap.get("STOCK_ADJUSTMENT") != null) {
                                        EdsProperty edsProperty = propertyMap.get("STOCK_ADJUSTMENT");
                                        localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "STOCK_ADJUSTMENT");
                                    } else {
                                        localize = wfmLocalizer.localize("stockAdjustments");
                                    }
                                    children.add(new PseudoMenuItem(localize, "stockAdjustments"));
                                } else if (isMultiWareHouseEnabled && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
                                    if (propertyMap != null && propertyMap.get("STOCK_ADJUSTMENT") != null) {
                                        EdsProperty edsProperty = propertyMap.get("STOCK_ADJUSTMENT");
                                        localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "STOCK_ADJUSTMENT");
                                    } else {
                                        localize = wfmLocalizer.localize("stockAdjustments");
                                    }
                                    children.add(new PseudoMenuItem(localize, "stockAdjustments"));
                                }
                            } else {
                                if (isMultiWareHouseEnabled && permissions.contains(isLogisticsEnabled ? PermissionConstants.LOGISTICS_STOCK_ADJUSTMENT_LIST : PermissionConstants.ACCOUNTING_STOCK_ADJUSTMENT_LIST)) {
                                    if (propertyMap != null && propertyMap.get("STOCK_ADJUSTMENT") != null) {
                                        EdsProperty edsProperty = propertyMap.get("STOCK_ADJUSTMENT");
                                        localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "STOCK_ADJUSTMENT");
                                    } else {
                                        localize = wfmLocalizer.localize("stockAdjustments");
                                    }
                                    children.add(new PseudoMenuItem(localize, "stockAdjustments"));
                                }
                            }
                        }
                        case "STOCK_OUT" -> {
                            if (propertyItem.getContainer() != null && "accounting".equals(propertyItem.getContainer().getCode())) {
                                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION) && isMultiWareHouseEnabled && permissions.contains(isLogisticsEnabled ? PermissionConstants.LOGISTICS_STOCK_ADJUSTMENT_LIST : PermissionConstants.ACCOUNTING_STOCK_ADJUSTMENT_LIST)) {
                                    if (propertyMap != null && propertyMap.get("STOCK_OUT") != null) {
                                        EdsProperty edsProperty = propertyMap.get("STOCK_OUT");
                                        localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "STOCK_OUT");
                                    } else {
                                        localize = wfmLocalizer.localize("stockOut");
                                    }
                                    children.add(new PseudoMenuItem(localize, "stockOut"));
                                }
                            }
                        }
                        case "consignment" -> {
                            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP) || permissionManager.getUser().getCompany().getParentCompanyId() != null) {
                                if (permissions.contains(PermissionConstants.ACCOUNTING_CONSIGNMENT_LIST_VIEW)) {
//                                    localize = propertyMap.get("consignment") != null ? propertyMap.get("consignment").getPlural() : accountingStrings.localize("consignment");
                                    if (propertyMap != null && propertyMap.get("consignment") != null) {
                                        EdsProperty edsProperty = propertyMap.get("consignment");
                                        localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "consignment");
                                    } else {
                                        localize = wfmLocalizer.localize("consignment");
                                    }
                                    children.add((new PseudoMenuItem(localize, "consignment")));
                                }
                            }
                        }
                        case "CASH_RECEIPT" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT)) {
//                                localize = propertyMap.get("CASH_RECEIPT") != null ? propertyMap.get("CASH_RECEIPT").getPlural() : accountingStrings.localize("cashReceipt");
                                if (propertyMap != null && propertyMap.get("CASH_RECEIPT") != null) {
                                    EdsProperty edsProperty = propertyMap.get("CASH_RECEIPT");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "CASH_RECEIPT");
                                } else {
                                    localize = wfmLocalizer.localize("cashReceipt");
                                }
                                children.add(new PseudoMenuItem(localize, "cashReceipt"));
                            }
                        }
                        case "CASH_PAYMENT" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT)) {
//                                localize = propertyMap.get("CASH_PAYMENT") != null ? propertyMap.get("CASH_PAYMENT").getPlural() : accountingStrings.localize("cashPayment");
                                if (propertyMap != null && propertyMap.get("CASH_PAYMENT") != null) {
                                    EdsProperty edsProperty = propertyMap.get("CASH_PAYMENT");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "CASH_PAYMENT");
                                } else {
                                    localize = wfmLocalizer.localize("cashPayment");
                                }
                                children.add(new PseudoMenuItem(localize, "cashpayment"));
                            }
                        }
                        case "RECEIVE_MONEY" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_RECEIVE)) {
//                                localize = propertyMap.get("RECEIVE_MONEY") != null ? propertyMap.get("RECEIVE_MONEY").getPlural() : accountingStrings.localize("bankReceipts");
                                if (propertyMap != null && propertyMap.get("RECEIVE_MONEY") != null) {
                                    EdsProperty edsProperty = propertyMap.get("RECEIVE_MONEY");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "RECEIVE_MONEY");
                                } else {
                                    localize = wfmLocalizer.localize("bankReceipts");
                                }
                                children.add(new PseudoMenuItem(localize, "bankreceipt"));
                            }
                        }
                        case "SPEND_MONEY" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_SPEND)) {
//                                localize = propertyMap.get("SPEND_MONEY") != null ? propertyMap.get("SPEND_MONEY").getPlural() : accountingStrings.localize("bankPayments");
                                if (propertyMap != null && propertyMap.get("SPEND_MONEY") != null) {
                                    EdsProperty edsProperty = propertyMap.get("SPEND_MONEY");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "SPEND_MONEY");
                                } else {
                                    localize = wfmLocalizer.localize("bankPayments");
                                }
                                children.add(new PseudoMenuItem(localize, "bankpayment"));
                            }
                        }
                        case Constants.CUSTOMER_PREPAYMENT -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_PREPAYMENT_LIST)) {
//                                localize = propertyMap.get(Constants.CUSTOMER_PREPAYMENT) != null ? propertyMap.get(Constants.CUSTOMER_PREPAYMENT).getPlural() : commonLocalizer.localize("prepayments");
                                if (propertyMap != null && propertyMap.get(Constants.CUSTOMER_PREPAYMENT) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.CUSTOMER_PREPAYMENT);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.CUSTOMER_PREPAYMENT);
                                } else {
                                    localize = wfmLocalizer.localize("prepayments");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.CUSTOMER_PREPAYMENT));
                            }
                        }
                        case "supplierPrepayment" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_SUPPLIER_CREDIT_LIST)) {
//                                localize = propertyMap.get(Constants.SUPPLIER_LIST) != null ? propertyMap.get(Constants.SUPPLIER_LIST).getPlural() + " Prepayments" : accountingStrings.localize("supplierCredits");
                                if (propertyMap != null && propertyMap.get(Constants.SUPPLIER_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.SUPPLIER_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.SUPPLIER_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("supplierCredits");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.SUPPLIER_LIST));
                            }
                        }
                        case "checkList" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_CHECK_LIST)) {

//                                localize = propertyMap.get("checkList") != null ? propertyMap.get("checkList").getPlural() : accountingStrings.localize("writeChecks");
                                if (propertyMap != null && propertyMap.get("checkList") != null) {
                                    EdsProperty edsProperty = propertyMap.get("checkList");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "checkList");
                                } else {
                                    localize = wfmLocalizer.localize("writeChecks");
                                }
                                children.add(new PseudoMenuItem(localize, "checkList"));
                            }
                        }
                        case Constants.MANUAL_TRANSACTIONS -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_MANUAL_JOURNAL_LIST)) {
//                                localize = propertyMap.get("manualtransactions") != null ? propertyMap.get("manualtransactions").getPlural() : accountingStrings.localize("manualEntries");
                                if (propertyMap != null && propertyMap.get(Constants.MANUAL_TRANSACTIONS) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.MANUAL_TRANSACTIONS);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.MANUAL_TRANSACTIONS);
                                } else {
                                    localize = wfmLocalizer.localize("manualEntries");
                                }
                                children.add(new PseudoMenuItem(localize, "manualTransactions"));
                            }
                        }
                        case "BATCH_RECEIVE_PAYMENT" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_RECEIVE_PAYMENT_LIST)) {
//                                localize = propertyMap.get("BATCH_RECEIVE_PAYMENT") != null ? propertyMap.get("BATCH_RECEIVE_PAYMENT").getPlural() : accountingStrings.localize("receivePayments");
                                if (propertyMap != null && propertyMap.get("BATCH_RECEIVE_PAYMENT") != null) {
                                    EdsProperty edsProperty = propertyMap.get("BATCH_RECEIVE_PAYMENT");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "BATCH_RECEIVE_PAYMENT");
                                } else {
                                    localize = wfmLocalizer.localize("receivePayments");
                                }
                                children.add(new PseudoMenuItem(localize, "receivePaymentsList"));
                            }
                        }
                        case Constants.PAYBILLS_LIST -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_PAY_BILL_LIST)) {
//                                localize = propertyMap.get(Constants.PAYBILLS_LIST) != null ? propertyMap.get(Constants.PAYBILLS_LIST).getPlural() : accountingStrings.localize("payInvoices");
                                if (propertyMap != null && propertyMap.get(Constants.PAYBILLS_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.PAYBILLS_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.PAYBILLS_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("payInvoices");
                                }
                                children.add(new PseudoMenuItem(localize, "payBillsList"));
                            }
                        }
                        case "goodsreceivednotes" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_GRN_LIST)) {
//                                localize = propertyMap.get("goodsreceivednotes") != null ? propertyMap.get("goodsreceivednotes").getPlural() : accountingStrings.localize("goodsReceivedNotes");
                                if (propertyMap != null && propertyMap.get("goodsreceivednotes") != null) {
                                    EdsProperty edsProperty = propertyMap.get("goodsreceivednotes");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "goodsreceivednotes");
                                } else {
                                    localize = wfmLocalizer.localize("goodsReceivedNotes");
                                }
                                children.add(new PseudoMenuItem(localize, "goodsreceivednotes"));
                            }
                        }
                        case "goodsdeliverednotes" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_GDN_LIST)) {
//                                localize = propertyMap.get("goodsdeliverednotes") != null ? propertyMap.get("goodsdeliverednotes").getPlural() : accountingStrings.localize("goodsDeliveredNotes");
                                if (propertyMap != null && propertyMap.get("goodsdeliverednotes") != null) {
                                    EdsProperty edsProperty = propertyMap.get("goodsdeliverednotes");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "goodsdeliverednotes");
                                } else {
                                    localize = wfmLocalizer.localize("goodsDeliveredNotes");
                                }
                                children.add(new PseudoMenuItem(localize, "goodsdeliverednotes"));
                            }
                        }
                        case "newprofitLoss" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_PROFIT_AND_LOSS)) {
//                                localize = propertyMap.get("newprofitLoss") != null ? propertyMap.get("newprofitLoss").getPlural() : accountingStrings.localize("profitAndLoss");
                                if (propertyMap != null && propertyMap.get("newprofitLoss") != null) {
                                    EdsProperty edsProperty = propertyMap.get("newprofitLoss");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "newprofitLoss");
                                } else {
                                    localize = wfmLocalizer.localize("profitAndLoss");
                                }
                                children.add(new PseudoMenuItem(localize, "newprofitLoss"));
                            }
                        }
                        case "balanceSheet" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_BALANCE_SHEET)) {
//                                localize = propertyMap.get("balanceSheet") != null ? propertyMap.get("balanceSheet").getPlural() : accountingStrings.localize("balanceSheet");
                                if (propertyMap != null && propertyMap.get("balanceSheet") != null) {
                                    EdsProperty edsProperty = propertyMap.get("balanceSheet");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "balanceSheet");
                                } else {
                                    localize = wfmLocalizer.localize("balanceSheet");
                                }
                                children.add(new PseudoMenuItem(localize, "balanceSheet"));
                            }
                        }
                        case "trialBalance" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_TRIAL_BALANCE)) {
                                children.add(new PseudoMenuItem(accountingStrings.localize("trialBalance"), "trialBalance"));
                            }
                        }
                        case "cashFlowStatement" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_CASH_FLOW)) {
//                                localize = propertyMap.get("cashFlowStatement") != null ? propertyMap.get("cashFlowStatement").getPlural() : accountingStrings.localize("cashFlowStatement");
                                if (propertyMap != null && propertyMap.get("cashFlowStatement") != null) {
                                    EdsProperty edsProperty = propertyMap.get("cashFlowStatement");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "cashFlowStatement");
                                } else {
                                    localize = wfmLocalizer.localize("cashFlowStatement");
                                }
                                children.add(new PseudoMenuItem(localize, "cashFlowStatement"));
                            }
                        }
                        case "arAgingSummary" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_AGING_SUMMARY_RECEIVABLE)) {
//                                localize = propertyMap.get("arAgingSummary") != null ? propertyMap.get("arAgingSummary").getPlural() : accountingStrings.localize("arAgingSummary1");
                                if (propertyMap != null && propertyMap.get("arAgingSummary") != null) {
                                    EdsProperty edsProperty = propertyMap.get("arAgingSummary");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "arAgingSummary");
                                } else {
                                    localize = wfmLocalizer.localize("arAgingSummary1");
                                }
                                children.add(new PseudoMenuItem(localize, "arAgingSummary"));
                            }
                        }
                        case "apAgingSummary" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_AGING_SUMMARY_PAYABLE)) {
                                localize = propertyMap.get("apAgingSummary") != null ? propertyMap.get("apAgingSummary").getPlural() : accountingStrings.localize("apAgingSummary1");
                                if (propertyMap != null && propertyMap.get("apAgingSummary") != null) {
                                    EdsProperty edsProperty = propertyMap.get("apAgingSummary");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "apAgingSummary");
                                } else {
                                    localize = wfmLocalizer.localize("apAgingSummary1");
                                }
                                children.add(new PseudoMenuItem(localize, "apAgingSummary"));
                            }
                        }
                        case "journalReport" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_JOURNAL_REPORT)) {
//                                localize = propertyMap.get("journalReport") != null ? propertyMap.get("journalReport").getPlural() : accountingStrings.localize("journalReport");
                                if (propertyMap != null && propertyMap.get("journalReport") != null) {
                                    EdsProperty edsProperty = propertyMap.get("journalReport");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "journalReport");
                                } else {
                                    localize = wfmLocalizer.localize("journalReport");
                                }
                                children.add(new PseudoMenuItem(localize, "journalReport"));
                            }
                        }
                        case "transactionsByPeriod" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_ACCOUNT_TRANSACTIONS)) {
//                                localize = propertyMap.get("transactionsByPeriod") != null ? propertyMap.get("transactionsByPeriod").getPlural() : accountingStrings.localize("accountTransactions");
                                if (propertyMap != null && propertyMap.get("transactionsByPeriod") != null) {
                                    EdsProperty edsProperty = propertyMap.get("transactionsByPeriod");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "transactionsByPeriod");
                                } else {
                                    localize = wfmLocalizer.localize("accountTransactions");
                                }
                                children.add(new PseudoMenuItem(localize, "transactionsByPeriod"));
                            }
                        }
                        case "stockValuation" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_STOCK_VALUATION) && !isLogisticsEnabled) {
//                                localize = propertyMap.get("stockValuation") != null ? propertyMap.get("stockValuation").getPlural() : inventoryLocalizer.localize("stockValuation");
                                if (propertyMap != null && propertyMap.get("stockValuation") != null) {
                                    EdsProperty edsProperty = propertyMap.get("stockValuation");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "stockValuation");
                                } else {
                                    localize = wfmLocalizer.localize("stockValuation");
                                }
                                children.add(new PseudoMenuItem(localize, "stockValuation"));
                            }
                        }
                        case "vatReturnsSaudiOrUae", "oldGccVatReturn" -> {
                            if (isVatReturnEnabled && (ServerUtils.isUAECompany(moduleManager.getUser().getCompany()) || ServerUtils.isKSACompany(moduleManager.getUser().getCompany())) && permissions.contains(PermissionConstants.ACCOUNTING_VAT_RETURNS_LIST)) {
                                taxName = accountingStrings.localize("vatReturn");
                                children.add(new PseudoMenuItem(taxName, "vatReturns"));
                            }
                        }
                        case "vatReturn" -> {
                            if (isVatReturnEnabled && !((ServerUtils.isUAECompany(moduleManager.getUser().getCompany()) || ServerUtils.isKSACompany(moduleManager.getUser().getCompany())) && permissions.contains(PermissionConstants.ACCOUNTING_VAT_RETURNS_LIST)) && permissions.contains(PermissionConstants.ACCOUNTING_VAT_RETURN)) {
//                                taxName = taxName != null && !"".equals(taxName) ? taxName + " Return" : accountingStrings.localize("vatReturn");
                                if (propertyMap != null && propertyMap.get("vatReturn") != null) {
                                    EdsProperty edsProperty = propertyMap.get("vatReturn");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "vatReturn");
                                } else {
                                    localize = wfmLocalizer.localize("vatReturn");
                                }
                                children.add(new PseudoMenuItem(taxName, "vatReturn"));

                            }
                        }
                        case "vatReturns" -> {
                            if (isVatReturnEnabled && !((ServerUtils.isUAECompany(moduleManager.getUser().getCompany()) || ServerUtils.isKSACompany(moduleManager.getUser().getCompany())) && permissions.contains(PermissionConstants.ACCOUNTING_VAT_RETURNS_LIST)) && permissions.contains(PermissionConstants.ACCOUNTING_VAT_RETURNS_LIST)) {
//                                taxName = taxName != null && !"".equals(taxName) ? (taxName + " " + wfmLocalizer.localize("reports")) : accountingStrings.localize("vatReports");
                                if (propertyMap != null && propertyMap.get("vatReturns") != null) {
                                    EdsProperty edsProperty = propertyMap.get("vatReturns");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "vatReturns");
                                } else {
                                    localize = wfmLocalizer.localize("vatReports");
                                }
                                children.add(new PseudoMenuItem(taxName, "vatReturns"));

                            }
                        }
                        case "budgetsheetView" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_BUDGET_SHEET)) {
//                                localize = propertyMap.get("budgetsheetView") != null ? propertyMap.get("budgetsheetView").getPlural() : accountingStrings.localize("budgetManager");
                                if (propertyMap != null && propertyMap.get("budgetsheetView") != null) {
                                    EdsProperty edsProperty = propertyMap.get("budgetsheetView");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "budgetsheetView");
                                } else {
                                    localize = wfmLocalizer.localize("budgetManager");
                                }
                                children.add(new PseudoMenuItem(localize, "budgetsheetView"));
                            }
                        }
                        case "warehouseList" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_WAREHOUSE_LIST)) {
//                                localize = propertyMap.get("warehouseList") != null ? propertyMap.get("warehouseList").getPlural() : inventoryLocalizer.localize("warehouses");
                                if (propertyMap != null && propertyMap.get("warehouseList") != null) {
                                    EdsProperty edsProperty = propertyMap.get("warehouseList");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "warehouseList");
                                } else {
                                    localize = wfmLocalizer.localize("warehouses");
                                }
                                children.add(new PseudoMenuItem(localize, "warehouseList"));
                            }
                        }
                        case "STOCK_TRANSFER" -> {
                            if (permissions.contains(PermissionConstants.ACCOUNTING_STOCK_TRANSFER_LIST)) {
//                                localize = propertyMap.get("STOCK_TRANSFER") != null ? propertyMap.get("STOCK_TRANSFER").getPlural() : accountingStrings.localize("stockTransfer");
                                if (propertyMap != null && propertyMap.get("STOCK_TRANSFER") != null) {
                                    EdsProperty edsProperty = propertyMap.get("STOCK_TRANSFER");
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, "STOCK_TRANSFER");
                                } else {
                                    localize = wfmLocalizer.localize("stockTransfer");
                                }
                                children.add(new PseudoMenuItem(localize, "stocktransfer"));
                            }
                        }
                        default -> {
                            if (permissions.contains(propertyItem.getFormID() + "_" + permissionManager.getUser().getCompany().getObjectID())) {
                                if (Constants.PAGE.equals(propertyItem.getType())) {
                                    List<Integer> customFormItems = customFormItemManager.getCustomFormItemsByFormId(propertyItem.getfID());
                                    if (customFormItems != null && !customFormItems.isEmpty()) {
                                        propertyItem.setSelectedItemID(customFormItems.get(0));
                                    }
                                    if (propertyItem.getSelectedItemID() != null && permissions.contains(propertyItem.getFormID() + "_SUMMARY_" + permissionManager.getUser().getCompany().getObjectID())) {
                                        children.add(new PseudoMenuItem(getLocalizedName(userLanguage, propertyItem.getlPlural(), propertyItem.getPlural()), Constants.ITEM_LIST + "|summary/" + propertyItem.getSelectedItemID() + "/" + propertyItem.getfID() + "/" + propertyItem.getFormID() + "/" + propertyItem.getPlural() + "/PAGE", true));
                                    } else if (propertyItem.getSelectedItemID() != null && permissions.contains(propertyItem.getFormID() + "_EDIT_" + permissionManager.getUser().getCompany().getObjectID()) || permissions.contains(propertyItem.getFormID() + "_ADD_" + permissionManager.getUser().getCompany().getObjectID())) {

                                        String id = propertyItem != null && propertyItem.getSelectedItemID() != null ? propertyItem.getSelectedItemID().toString() : "";
                                        children.add(new PseudoMenuItem(getLocalizedName(userLanguage, propertyItem.getlPlural(), propertyItem.getPlural()), Constants.ITEM_LIST + "|add/add/" + id + "/" + propertyItem.getfID() + "/" + propertyItem.getFormID() + "/" + propertyItem.getPlural() + "//" + "/PAGE", true));
                                    }
                                } else {
                                    children.add(new PseudoMenuItem(getLocalizedName(userLanguage, propertyItem.getlPlural(), propertyItem.getPlural()), "custom_form_" + propertyItem.getfID()));
                                }
                            }
                        }
                    }
                }
            }
        }

        return children;
    }

    public ArrayList<PseudoMenuItem> getPMMenuItems(String moduleName) {
        ArrayList<PseudoMenuItem> result = new ArrayList<>();
        EdsUser user = userManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);

        List<EdsProperty> properties = propertManager.findByModuleCode(moduleName);
        Map<String, EdsProperty> propertyMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(properties)) {
            propertyMap = properties.stream().collect(Collectors.toMap(EdsProperty::getObjectName, x -> x));
        }

        PseudoMenuItem dashboardContainer = getDashboardContainer(Constants.MODULE_PM);
        if (dashboardContainer != null) {
            result.add(dashboardContainer);
        }

        List<EdsContainer> containers = containerManager.getContainerBySorder(moduleName);

        if (containers != null && containers.size() > 0) {
            for (EdsContainer container : containers) {
                PseudoMenuItem pmSinksContainer = new PseudoMenuItem(container.isChanged() ? (container.getLocalization() != null ? getLocalizedName(userSettings.getInternationalization(), container.getLocalization().getRPC(), container.getDefaultName()) : container.getDefaultName()) : commonLocalizer.localize(container.getDefaultName()), container.getCode());
                pmSinksContainer.setChildren(getPMContainerChildren(container,propertyMap,userSettings.getInternationalization()));

                switch (container.getCode()) {
                    case "project" -> {
                        result.add(pmSinksContainer);
                    }
                    default -> result.add(pmSinksContainer);
                }
            }
        }
        return result;
    }

    private ArrayList<PseudoMenuItem> getPMContainerChildren(EdsContainer container, Map<String, EdsProperty> propertyMap, String userLanguage) {
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        List<EdsCustomForm> formList = getCustomFormList(ModuleEnum.PM);

        permissions.add(PermissionConstants.PM_TASKS_LIST);
        permissions.add(PermissionConstants.PM_ISSUE_LIST);
        permissions.add(PermissionConstants.PM_TIMESHEET);
        permissions.add(PermissionConstants.PM_TIMESHEET_APPROVAL);
        permissions.add(PermissionConstants.PM_PROJECT_LIST);
        permissions.add(PermissionConstants.PM_CUSTOMER_LIST);
        permissions.add(PermissionConstants.PM_EMPLOYEE_LIST);
        permissions.add(PermissionConstants.PM_BOOKING_ITEMS);
        permissions.add(PermissionConstants.PM_RESOURCE_UTILIZATION_LIST);
        permissions.add(PermissionConstants.PM_CONTRACT_LIST);
        permissions.add(PermissionConstants.PM_PROJECT_EXPENSE_CLAIMS);
        permissions.add(PermissionConstants.MONTHLY_TIMESHEET);

        for (EdsCustomForm customForm : formList) {
            permissions.add(customForm.getFormID() + "_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_ADD_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_EDIT_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_SUMMARY_" + permissionManager.getUser().getCompany().getObjectID());
        }
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        String localize = "";

        List<EdsContainerItem> containerItemList = containerItemManager.getItemsByContainer(container.getObjectID(), false);
        if (containerItemList != null && !containerItemList.isEmpty()) {
            for (EdsContainerItem containerItem : containerItemList) {
                PropertyItem propertyItem = containerItem.toItem();
                if (propertyItem != null && propertyItem.getObjectName() != null) {
                    switch (propertyItem.getObjectName()) {
                        case Constants.TASK -> {
                            if (permissions.contains(PermissionConstants.PM_TASKS_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.TASK) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.TASK);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.TASK);
                                } else {
                                    localize = wfmLocalizer.localize("tasks");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.TASK_LIST));
                            }
                        }
                        case Constants.ISSUE -> {
                            if (permissions.contains(PermissionConstants.PM_ISSUE_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.ISSUE) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.ISSUE);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.ISSUE);
                                } else {
                                    localize = wfmLocalizer.localize("issues");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.ISSUE_LIST));
                            }
                        }
                        case Constants.TIMESHEET -> {
                            if (permissions.contains(PermissionConstants.PM_TIMESHEET)) {
                                if (propertyMap != null && propertyMap.get(Constants.TIMESHEET) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.TIMESHEET);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.TIMESHEET);
                                } else {
                                    localize = wfmLocalizer.localize("timesheet");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.TIMESHEET));
                            }
                        }
                        case Constants.MONTHLYTIMESHEET -> {
                            boolean isMonthlyTimesheetEnabled = moduleManager.getModuleByCodeByCompany(moduleManager.getUser().getCompany().getObjectID(), Constants.MONTHLY_TIMESHEET) != null;
                            if (isMonthlyTimesheetEnabled && permissions.contains(PermissionConstants.MONTHLY_TIMESHEET)) {
                                if (propertyMap != null && propertyMap.get(Constants.MONTHLYTIMESHEET) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.MONTHLYTIMESHEET);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.MONTHLYTIMESHEET);
                                } else {
                                    localize = wfmLocalizer.localize("monthlyTimeSheet");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.MONTHLYTIMESHEET));
                            }
                        }
                        case Constants.TIMESHEET_APPROVAL_LIST -> {
                            if (permissions.contains(PermissionConstants.PM_TIMESHEET_APPROVAL)) {
                                if (propertyMap != null && propertyMap.get(Constants.TIMESHEET_APPROVAL_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.TIMESHEET_APPROVAL_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.TIMESHEET_APPROVAL_LIST);
                                } else {
                                    localize = crmLocalizer.localize("timesheetApproval");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.TIMESHEET_APPROVAL_LIST));
                            }
                        }
                        case Constants.PROJECT -> {
                            if (permissions.contains(PermissionConstants.PM_PROJECT_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.PROJECT) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.PROJECT);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.PROJECT);
                                } else {
                                    localize = wfmLocalizer.localize("projects");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.PROJECT_LIST));
                            }
                        }
                        case Constants.CLIENT_LIST -> {
                            if (permissions.contains(PermissionConstants.PM_CUSTOMER_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.CLIENT_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.CLIENT_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.CLIENT_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("customers");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.CLIENT_LIST));
                            }
                        }
                        case Constants.EMLOYEE_LIST -> {
                            if (!permissionManager.getUser().getCompany().getObjectID().equals(22240) && permissions.contains(PermissionConstants.PM_EMPLOYEE_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.EMLOYEE_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.EMLOYEE_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.EMLOYEE_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("employees");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.EMLOYEE_LIST));
                            }
                        }
                        case Constants.BOOKINGITEMS_LIST -> {
                            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_BOOKING_ITEMS) && permissions.contains(PermissionConstants.PM_BOOKING_ITEMS)) {
                                if (propertyMap != null && propertyMap.get(Constants.BOOKINGITEMS_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.BOOKINGITEMS_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.BOOKINGITEMS_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("bookingItems");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.BOOKINGITEMS_LIST));
                            }
                        }
                        case Constants.RESOURCE_UTIL -> {
                            if (permissions.contains(PermissionConstants.PM_RESOURCE_UTILIZATION_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.RESOURCE_UTIL) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.RESOURCE_UTIL);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.RESOURCE_UTIL);
                                } else {
                                    localize = wfmLocalizer.localize("resourceUtilization");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.RESOURCE_UTIL));
                            }
                        }
                        case Constants.CONTRACT_LIST -> {
                            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.EMPLOYEE_ASSIGNMENT_ENABLE) && permissions.contains(PermissionConstants.PM_CONTRACT_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.CONTRACT_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.CONTRACT_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.CONTRACT_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("contracts");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.CONTRACT_LIST));
                            }
                        }
                        case Constants.EXPENSES_CLAIM -> {
                            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_EXPENSE_USE_AS_INTERNAL_INVOICE) && permissions.contains(PermissionConstants.PM_PROJECT_EXPENSE_CLAIMS)) {
                                if (propertyMap != null && propertyMap.get(Constants.EXPENSES_CLAIM) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.EXPENSES_CLAIM);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.EXPENSES_CLAIM);
                                } else {
                                    localize = wfmLocalizer.localize("expenseClaims");
                                }
                                children.add(new PseudoMenuItem(localize, "expenseList"));
                            }
                        }
                        default -> {
                            if (permissions.contains(propertyItem.getFormID() + "_" + permissionManager.getUser().getCompany().getObjectID())) {
                                if (Constants.PAGE.equals(propertyItem.getType())) {
                                    List<Integer> customFormItems = customFormItemManager.getCustomFormItemsByFormId(propertyItem.getfID());
                                    if (customFormItems != null && !customFormItems.isEmpty()) {
                                        propertyItem.setSelectedItemID(customFormItems.get(0));
                                    }
                                    if (propertyItem.getSelectedItemID() != null && permissions.contains(propertyItem.getFormID() + "_SUMMARY_" + permissionManager.getUser().getCompany().getObjectID())) {
                                        children.add(new PseudoMenuItem(getLocalizedName(userLanguage, propertyItem.getlPlural(), propertyItem.getPlural()), Constants.ITEM_LIST + "|summary/" + propertyItem.getSelectedItemID() + "/" + propertyItem.getfID() + "/" + propertyItem.getFormID() + "/" + propertyItem.getPlural() + "/PAGE", true));
                                    } else if (propertyItem.getSelectedItemID() != null && permissions.contains(propertyItem.getFormID() + "_EDIT_" + permissionManager.getUser().getCompany().getObjectID()) || permissions.contains(propertyItem.getFormID() + "_ADD_" + permissionManager.getUser().getCompany().getObjectID())) {

                                        String id = propertyItem != null && propertyItem.getSelectedItemID() != null ? propertyItem.getSelectedItemID().toString() : "";
                                        children.add(new PseudoMenuItem(getLocalizedName(userLanguage, propertyItem.getlPlural(), propertyItem.getPlural()), Constants.ITEM_LIST + "|add/add/" + id + "/" + propertyItem.getfID() + "/" + propertyItem.getFormID() + "/" + propertyItem.getPlural() + "//" + "/PAGE", true));
                                    }
                                } else {
                                    children.add(new PseudoMenuItem(getLocalizedName(userLanguage, propertyItem.getlPlural(), propertyItem.getPlural()), "custom_form_" + propertyItem.getfID()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return children;
    }

    public ArrayList<PseudoMenuItem> getCrmMenuItems(String moduleName) {
        ArrayList<PseudoMenuItem> result = new ArrayList<>();
        List<String> permissions = new ArrayList<>();

        EdsUser user = userManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);

        List<EdsProperty> properties = propertManager.findByModuleCode(moduleName);

        Map<String, EdsProperty> propertyMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(properties)) {
            propertyMap = properties.stream().collect(Collectors.toMap(EdsProperty::getObjectName, x -> x));
        }

        permissions.add(PermissionConstants.CRM_SALES_TAB);
        permissions.add(PermissionConstants.CUSTOMER_SERVICE_TAB);
        permissions.add(PermissionConstants.CRM_E_MAIL_MARKETING_TAB);
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        PseudoMenuItem dashboardContainer = getDashboardContainer(Constants.MODULE_CRM);
        if (dashboardContainer != null) {
            result.add(dashboardContainer);
        }
        List<EdsContainer> containers = containerManager.getContainerBySorder(moduleName);

        if (containers != null && containers.size() > 0) {
            for (EdsContainer container : containers) {
                PseudoMenuItem crmSinksContainer = new PseudoMenuItem(container.isChanged() ? (container.getLocalization() != null ? getLocalizedName(userSettings.getInternationalization(), container.getLocalization().getRPC(), container.getDefaultName()) : container.getDefaultName()) : commonLocalizer.localize(container.getDefaultName()), container.getCode());
                crmSinksContainer.setChildren(getCRMMainManuItems(container, propertyMap, userSettings.getInternationalization()));

                switch (container.getCode()) {
                    case Constants.CRM_WELCOME -> {
                        if (permissions.contains((PermissionConstants.CRM_SALES_TAB))) {
                            result.add(crmSinksContainer);
                        }
                    }
                    case Constants.CRM_ACCOUNT_LIST_2 -> {
                        if (permissions.contains((PermissionConstants.CUSTOMER_SERVICE_TAB))) {
                            result.add(crmSinksContainer);
                        }
                    }
                    case Constants.EMAIL_MARKETING -> {
                        if (permissions.contains((PermissionConstants.CRM_E_MAIL_MARKETING_TAB))) {
                            result.add(crmSinksContainer);
                        }
                    }
                    default -> result.add(crmSinksContainer);
                }
            }
        }
        return result;
    }

    private ArrayList<PseudoMenuItem> getCRMMainManuItems(EdsContainer container, Map<String, EdsProperty> propertyMap, String userLanguage) {
        EdsModule logisticsEnabled = moduleManager.getModuleByCode(PermissionConstants.LOGISTICS_MODULE);
        boolean isLogisticsEnabled = logisticsEnabled != null;
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        List<EdsCustomForm> formList = getCustomFormList(ModuleEnum.CRM);
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.CRM_LEADS_LIST);
        permissions.add(PermissionConstants.CRM_OPPORTUNITIES_LIST);
        permissions.add(PermissionConstants.CRM_SALES_QUOTE_LIST);
        permissions.add(PermissionConstants.CRM_ACCOUNTS_LIST);
        permissions.add(PermissionConstants.CRM_CONTACTS_LIST);
        permissions.add(PermissionConstants.CRM_ACTIVITIES_LIST);
        permissions.add(PermissionConstants.CRM_REQUEST_FOR_QUOTE_LIST);
        permissions.add(PermissionConstants.CRM_Calendar);

        permissions.add(PermissionConstants.CRM_CASES_LIST);
        permissions.add(PermissionConstants.CRM_SOLUTIONS_LIST);
        permissions.add(PermissionConstants.CRM_TASKS_LIST);

        permissions.add(PermissionConstants.CRM_MAILING_LIST);
        permissions.add(PermissionConstants.CRM_QUEUED_MESSAGES_LIST);
        permissions.add(PermissionConstants.CRM_SENT_MESSAGES_LIST);
        permissions.add(PermissionConstants.CRM_CAMPAIGNS_LIST);
        permissions.add(PermissionConstants.CRM_WEB_FORMS_LIST);


        for (EdsCustomForm customForm : formList) {
            permissions.add(customForm.getFormID() + "_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_ADD_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_EDIT_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_SUMMARY_" + permissionManager.getUser().getCompany().getObjectID());
        }
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        String localize;

        List<EdsContainerItem> containerItemList = containerItemManager.getItemsByContainer(container.getObjectID(), false);
        if (containerItemList != null && containerItemList.size() > 0) {
            for (EdsContainerItem containerItem : containerItemList) {
                PropertyItem propertyItem = containerItem.toItem();
                if (propertyItem != null && propertyItem.getObjectName() != null) {
                    switch (propertyItem.getObjectName()) {
                        case Constants.LEADS -> {
                            if (permissions.contains(PermissionConstants.CRM_LEADS_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.LEADS) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.LEADS);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.LEADS);
                                } else {
                                    localize = wfmLocalizer.localize("leads");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.LEAD_LIST));
                            }
                        }
                        case Constants.Opportunities -> {
                            if (permissions.contains(PermissionConstants.CRM_OPPORTUNITIES_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.Opportunities) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.Opportunities);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.Opportunities);
                                } else {
                                    localize = wfmLocalizer.localize("opportunities");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.OPPORTUNITY_LIST));
                            }
                        }
                        case Constants.SALE_QUOTE -> {
                            if (permissions.contains(PermissionConstants.CRM_SALES_QUOTE_LIST) && isLogisticsEnabled) {
                                if (propertyMap != null && propertyMap.get(Constants.SALE_QUOTE) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.SALE_QUOTE);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.SALE_QUOTE);
                                } else {
                                    localize = wfmLocalizer.localize("salesQuote");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.SALE_QUOTE));
                            }
                        }
                        case Constants.CRM_ACCOUNT_LIST -> {
                            if (permissions.contains(PermissionConstants.CRM_ACCOUNTS_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.CRM_ACCOUNT_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.CRM_ACCOUNT_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.CRM_ACCOUNT_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("companies");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.CRM_ACCOUNT_LIST));

                            }
                        }
                        case Constants.Contacts -> {
                            if (permissions.contains(PermissionConstants.CRM_CONTACTS_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.Contacts) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.Contacts);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.Contacts);
                                } else {
                                    localize = wfmLocalizer.localize("contacts");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.CRM_CONTACT_LIST));
                            }
                        }
                        case Constants.REQUEST_FOR_QUOTE -> {
                            if (permissions.contains(PermissionConstants.CRM_REQUEST_FOR_QUOTE_LIST)) {
                                children.add(new PseudoMenuItem(wfmLocalizer.localize("requestForQuote"), Constants.REQUEST_FOR_QUOTE));
                            }
                        }
                        case Constants.EVENT_LIST -> {
                            if (permissions.contains(PermissionConstants.CRM_ACTIVITIES_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.EVENT_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.EVENT_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.EVENT_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("activities");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.EVENT_LIST));
                            }
                        }
                        case Constants.CASE_LIST -> {
                            if (permissions.contains(PermissionConstants.CRM_CASES_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.CASE_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.CASE_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.CASE_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("cases");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.CASE_LIST));
                            }
                        }
                        case Constants.SOLUTION_LIST -> {
                            if (permissions.contains(PermissionConstants.CRM_SOLUTIONS_LIST)) {
                                children.add(new PseudoMenuItem(crmLocalizer.localize("solutions"), Constants.SOLUTION_LIST));
                            }
                        }
                        case Constants.TASK -> {
                            if (permissions.contains(PermissionConstants.CRM_TASKS_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.TASK) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.TASK);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.TASK);
                                } else {
                                    localize = wfmLocalizer.localize("tasks");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.TASK_LIST));
                            }
                        }
                        case Constants.MAIL_LIST -> {
                            if (permissions.contains(PermissionConstants.CRM_MAILING_LIST)) {
                                children.add(new PseudoMenuItem(wfmLocalizer.localize("mailingList"), Constants.MAIL_LIST));
                            }
                        }
                        case "scheduled_messages" -> {
                            if (permissions.contains(PermissionConstants.CRM_QUEUED_MESSAGES_LIST)) {
                                children.add(new PseudoMenuItem(crmLocalizer.localize("scheduledMessages"), "scheduled_messages"));
                            }
                        }
                        case "SentMessages" -> {
                            if (permissions.contains(PermissionConstants.CRM_SENT_MESSAGES_LIST)) {
                                children.add(new PseudoMenuItem(crmLocalizer.localize("sentMessages"), "SentMessages"));
                            }
                        }
                        case Constants.CAMPAIGN_LIST -> {
                            if (permissions.contains(PermissionConstants.CRM_CAMPAIGNS_LIST)) {
                                children.add(new PseudoMenuItem(wfmLocalizer.localize("campaigns"), Constants.CAMPAIGN_LIST));
                            }
                        }
                        case "webFormsList" -> {
                            if (permissions.contains(PermissionConstants.CRM_WEB_FORMS_LIST)) {
                                children.add(new PseudoMenuItem(crmLocalizer.localize("crmForms"), "webFormsList"));
                            }
                        }
                        default -> {
                            if (permissions.contains(propertyItem.getFormID() + "_" + permissionManager.getUser().getCompany().getObjectID())) {
                                if (Constants.PAGE.equals(propertyItem.getType())) {
                                    List<Integer> customFormItems = customFormItemManager.getCustomFormItemsByFormId(propertyItem.getfID());
                                    if (customFormItems != null && !customFormItems.isEmpty()) {
                                        propertyItem.setSelectedItemID(customFormItems.get(0));
                                    }
                                    if (propertyItem.getSelectedItemID() != null && permissions.contains(propertyItem.getFormID() + "_SUMMARY_" + permissionManager.getUser().getCompany().getObjectID())) {
                                        children.add(new PseudoMenuItem(getLocalizedName(userLanguage, propertyItem.getlPlural(), propertyItem.getPlural()), Constants.ITEM_LIST + "|summary/" + propertyItem.getSelectedItemID() + "/" + propertyItem.getfID() + "/" + propertyItem.getFormID() + "/" + propertyItem.getPlural() + "/PAGE", true));
                                    } else if (propertyItem.getSelectedItemID() != null && permissions.contains(propertyItem.getFormID() + "_EDIT_" + permissionManager.getUser().getCompany().getObjectID()) || permissions.contains(propertyItem.getFormID() + "_ADD_" + permissionManager.getUser().getCompany().getObjectID())) {

                                        String id = propertyItem != null && propertyItem.getSelectedItemID() != null ? propertyItem.getSelectedItemID().toString() : "";
                                        children.add(new PseudoMenuItem(getLocalizedName(userLanguage, propertyItem.getlPlural(), propertyItem.getPlural()), Constants.ITEM_LIST + "|add/add/" + id + "/" + propertyItem.getfID() + "/" + propertyItem.getFormID() + "/" + propertyItem.getPlural() + "//" + "/PAGE", true));
                                    }
                                } else {
                                    children.add(new PseudoMenuItem(getLocalizedName(userLanguage, propertyItem.getlPlural(), propertyItem.getPlural()), "custom_form_" + propertyItem.getfID()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return children;
    }

    private String getLocalizedSectionTitle(Map<String, EdsProperty> propertyMap, String locale, EdsProperty property, String section) {
        String localize;
        switch (locale) {
            case "ru" -> {
                localize = Optional.ofNullable(property.getlPlural())
                        .map(EdsCustomFormLocalization::getRussianName)
                        .orElseGet(() -> propertyMap.get(section).getPlural());
            }
            case "en" -> {
                localize = Optional.ofNullable(property.getlPlural())
                        .map(EdsCustomFormLocalization::getEnglishName)
                        .orElseGet(() -> propertyMap.get(section).getPlural());
            }
            case "uz" -> {
                localize = Optional.ofNullable(property.getlPlural())
                        .map(EdsCustomFormLocalization::getUzbekName)
                        .orElseGet(() -> propertyMap.get(section).getPlural());
            }
            case "ar" -> {
                localize = Optional.ofNullable(property.getlPlural())
                        .map(EdsCustomFormLocalization::getArabicName)
                        .orElseGet(() -> propertyMap.get(section).getPlural());
            }
            default -> localize = propertyMap.get(section) != null ? propertyMap.get(section).getPlural() : "";
        }
        return localize;
    }


    public ArrayList<PseudoMenuItem> getPayrollMenuItems(String moduleName) {
        ArrayList<PseudoMenuItem> result = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        EdsUser user = userManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);

        List<EdsProperty> properties = propertManager.findByModuleCode(moduleName);
        Map<String, EdsProperty> propertyMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(properties)) {
            propertyMap = properties.stream().collect(Collectors.toMap(EdsProperty::getObjectName, x -> x));
        }

        permissions.add(PermissionConstants.PAYROLL_MAIN_CONTENT);
        permissions.add(PermissionConstants.PAYROLL_REPORTS);
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        PseudoMenuItem dashboardContainer = getDashboardContainer(Constants.MODULE_PAYROLL);
        if (dashboardContainer != null) {
            result.add(dashboardContainer);
        }
        List<EdsContainer> containers = containerManager.getContainerBySorder(moduleName);
        if (containers != null && !containers.isEmpty()) {
            for (EdsContainer container : containers) {
                PseudoMenuItem dynamicSinksContainer = new PseudoMenuItem(container.isChanged() ? (container.getLocalization() != null ? getLocalizedName(userSettings.getInternationalization(), container.getLocalization().getRPC(), container.getDefaultName()) : container.getDefaultName()) : commonLocalizer.localize(container.getDefaultName()), container.getCode());
                switch (container.getCode()) {
                    case "payroll" -> {
                        if (permissions.contains((PermissionConstants.PAYROLL_MAIN_CONTENT))) {
                            dynamicSinksContainer.setChildren(createPayrollContainerChildren(container, propertyMap, userSettings.getInternationalization()));
                            result.add(dynamicSinksContainer);
                        }
                    }
                    case "myPayroll" -> {
                        dynamicSinksContainer.setChildren(getPayrollEmployeeContainerMenuItems(container, propertyMap, userSettings.getInternationalization()));
                        result.add(dynamicSinksContainer);
                    }
                    case "payrollReports" -> {
                        if (permissions.contains((PermissionConstants.PAYROLL_REPORTS))) {
                            dynamicSinksContainer.setChildren(getPayrollReportContainerMenuItems());
                            result.add(dynamicSinksContainer);
                        }
                    }
                    default -> result.add(dynamicSinksContainer);
                }

            }
        }

        return result;
    }

    private ArrayList<PseudoMenuItem> createPayrollContainerChildren(EdsContainer container, Map<String, EdsProperty> propertyMap, String userLanguage) {
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        List<EdsCustomForm> formList = getCustomFormList(ModuleEnum.PAYROLL);
        EdsCompany company = moduleManager.getUser().getCompany();

        permissions.add(PermissionConstants.PAYROLL_EMPLOYEES_LIST);
        permissions.add(PermissionConstants.PAYROLL_PAYSLIP_LIST);
        permissions.add(PermissionConstants.PAYROLL_GROUP_PAYRUN_LIST);
        permissions.add(PermissionConstants.PAYROLL_CASH_ADVANCE_LIST);
        permissions.add(PAYROLL_MULTI_CASH_ADVANCE_LIST);
        permissions.add(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_LIST);
        permissions.add(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST);
        permissions.add(PermissionConstants.PAYROLL_EMPLOYEES_LIST);
        permissions.add(PermissionConstants.END_OF_SERVICE_GRATUITY_LIST);
        permissions.add(PermissionConstants.MY_BENEFIT_REQUEST_LIST);
        permissions.add(PermissionConstants.PAYROLL_PENDING_CHANGES);
        permissions.add(PAYROLL_RECURRING_PD_LIST);

        for (EdsCustomForm customForm : formList) {
            permissions.add(customForm.getFormID() + "_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_ADD_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_EDIT_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_SUMMARY_" + permissionManager.getUser().getCompany().getObjectID());
        }
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        String localize = "";

        List<EdsContainerItem> containerItemList = containerItemManager.getItemsByContainer(container.getObjectID(), false);
        if (containerItemList != null && !containerItemList.isEmpty()) {
            for (EdsContainerItem containerItem : containerItemList) {
                PropertyItem propertyItem = containerItem.toItem();
                if (propertyItem != null && propertyItem.getObjectName() != null) {
                    switch (propertyItem.getObjectName()) {
                        case Constants.EMLOYEE_LIST -> {
                            if (permissions.contains(PermissionConstants.PAYROLL_EMPLOYEES_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.EMLOYEE_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.EMLOYEE_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.EMLOYEE_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("employeeList");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.EMLOYEE_LIST));
                            }
                        }
                        case Constants.SINGLE_PAYRUN_LIST -> {
                            if (permissions.contains(PermissionConstants.PAYROLL_PAYSLIP_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.SINGLE_PAYRUN_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.SINGLE_PAYRUN_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.SINGLE_PAYRUN_LIST);
                                } else {
                                    localize = accountingStrings.localize("payslips");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.SINGLE_PAYRUN_LIST));
                            }
                        }
                        case Constants.PAYSLIP_TABLE_LIST -> {
                            if (permissions.contains(PermissionConstants.PAYROLL_GROUP_PAYRUN_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.PAYSLIP_TABLE_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.PAYSLIP_TABLE_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.PAYSLIP_TABLE_LIST);
                                } else {
                                    localize = payrollLocalizer.localize("groupPayruns");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.PAYSLIP_TABLE_LIST));
                            }
                        }
                        case Constants.CASH_ADVANCE_LIST -> {
                            if (permissions.contains(PermissionConstants.PAYROLL_CASH_ADVANCE_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.CASH_ADVANCE_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.CASH_ADVANCE_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.CASH_ADVANCE_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("cashAdvance");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.CASH_ADVANCE_LIST));
                            }
                        }
                        case Constants.ADDITIONAL_PAYMENT_ITEM_LIST -> {
                            if (permissions.contains(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.ADDITIONAL_PAYMENT_ITEM_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.ADDITIONAL_PAYMENT_ITEM_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.ADDITIONAL_PAYMENT_ITEM_LIST);
                                } else {
                                    localize = payrollLocalizer.localize("singlePayments");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.ADDITIONAL_PAYMENT_ITEM_LIST));
                            }
                        }
                        case Constants.MULTI_CASH_ADVANCE_LIST -> {
                            if (permissions.contains(PermissionConstants.PAYROLL_MULTI_CASH_ADVANCE_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.MULTI_CASH_ADVANCE_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.MULTI_CASH_ADVANCE_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.MULTI_CASH_ADVANCE_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("multiCashAdvance");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.MULTI_CASH_ADVANCE_LIST));
                            }
                        }
                        case Constants.RECURRING_PAY_DEDUCTION_LIST -> {
                            if (permissions.contains(PermissionConstants.PAYROLL_RECURRING_PD_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.RECURRING_PAY_DEDUCTION_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.RECURRING_PAY_DEDUCTION_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.RECURRING_PAY_DEDUCTION_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("recurringPayDeduction");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.RECURRING_PAY_DEDUCTION_LIST));
                            }
                        }
                        case Constants.ADDITIONAL_PAYMENT_LIST -> {
                            if (permissions.contains(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.ADDITIONAL_PAYMENT_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.ADDITIONAL_PAYMENT_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.ADDITIONAL_PAYMENT_LIST);
                                } else {
                                    localize = payrollLocalizer.localize("additionals");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.ADDITIONAL_PAYMENT_LIST));
                            }
                        }
                        case Constants.BENEFIT_REQUESTS -> {
                            if (permissions.contains(PermissionConstants.MY_BENEFIT_REQUEST_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.BENEFIT_REQUESTS) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.BENEFIT_REQUESTS);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.BENEFIT_REQUESTS);
                                } else {
                                    localize = hrmsLocalizer.localize("benefitRequests");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.BENEFIT_REQUESTS));
                            }
                        }
                        case Constants.EMPLOYEE_TEMPLATE_LIST -> {
                            if (permissions.contains(PermissionConstants.PAYROLL_PENDING_CHANGES)) {
                                if (propertyMap != null && propertyMap.get(Constants.EMPLOYEE_TEMPLATE_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.EMPLOYEE_TEMPLATE_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.EMPLOYEE_TEMPLATE_LIST);
                                } else {
                                    localize = payrollLocalizer.localize("pendingChanges");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.EMPLOYEE_TEMPLATE_LIST));
                            }
                        }
                        case Constants.END_OF_SERVICE_GRATUITY -> {
                            if (ServerUtils.isArabicCompany(company) && permissions.contains(PermissionConstants.END_OF_SERVICE_GRATUITY_LIST)) {
                                if (propertyMap != null && propertyMap.get(Constants.END_OF_SERVICE_GRATUITY) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.END_OF_SERVICE_GRATUITY);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.END_OF_SERVICE_GRATUITY);
                                } else {
                                    localize = payrollLocalizer.localize("endOfServiceGratuity");
                                }
                                children.add(new PseudoMenuItem(localize, Constants.END_OF_SERVICE_GRATUITY));
                            }
                        }
                        default -> {
                            if (permissions.contains(propertyItem.getFormID() + "_" + permissionManager.getUser().getCompany().getObjectID())) {
                                if (Constants.PAGE.equals(propertyItem.getType())) {
                                    List<Integer> customFormItems = customFormItemManager.getCustomFormItemsByFormId(propertyItem.getfID());
                                    if (customFormItems != null && !customFormItems.isEmpty()) {
                                        propertyItem.setSelectedItemID(customFormItems.get(0));
                                    }
                                    if (propertyItem.getSelectedItemID() != null && permissions.contains(propertyItem.getFormID() + "_SUMMARY_" + permissionManager.getUser().getCompany().getObjectID())) {
                                        children.add(new PseudoMenuItem(propertyItem.getPlural(), Constants.ITEM_LIST + "|summary/" + propertyItem.getSelectedItemID() + "/" + propertyItem.getfID() + "/" + propertyItem.getFormID() + "/" + propertyItem.getPlural() + "/PAGE", true));
                                    } else if (propertyItem.getSelectedItemID() != null && permissions.contains(propertyItem.getFormID() + "_EDIT_" + permissionManager.getUser().getCompany().getObjectID()) || permissions.contains(propertyItem.getFormID() + "_ADD_" + permissionManager.getUser().getCompany().getObjectID())) {

                                        String id = propertyItem != null && propertyItem.getSelectedItemID() != null ? propertyItem.getSelectedItemID().toString() : "";
                                        children.add(new PseudoMenuItem(propertyItem.getPlural(), Constants.ITEM_LIST + "|add/add/" + id + "/" + propertyItem.getfID() + "/" + propertyItem.getFormID() + "/" + propertyItem.getPlural() + "//" + "/PAGE", true));
                                    }
                                } else {
                                    children.add(new PseudoMenuItem(propertyItem.getPlural(), "custom_form_" + propertyItem.getfID()));
                                }
                            }
                        }
                    }
                }
            }
        }

        return children;
    }

    private ArrayList<PseudoMenuItem> getPayrollEmployeeContainerMenuItems(EdsContainer container, Map<String, EdsProperty> propertyMap, String userLanguage) {
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        String localize = "";
        if (permissionManager.getUser().getRoleIds().contains(Constants.MEM)) {
            List<EdsContainerItem> containerItemList = containerItemManager.getItemsByContainer(container.getObjectID(), false);
            if (containerItemList != null && !containerItemList.isEmpty()) {
                for (EdsContainerItem containerItem : containerItemList) {
                    PropertyItem propertyItem = containerItem.toItem();
                    if (propertyItem != null && propertyItem.getObjectName() != null) {
                        switch (propertyItem.getObjectName()) {
                            case Constants.SINGLE_PAYRUN_LIST -> {
                                if (propertyMap != null && propertyMap.get(Constants.SINGLE_PAYRUN_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.SINGLE_PAYRUN_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.SINGLE_PAYRUN_LIST);
                                } else {
                                    localize = accountingStrings.localize("payslips");
                                }
                                children.add(new PseudoMenuItem(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_OLD_PAYSLIPS) ? wfmLocalizer.localize("new1") + " " : localize, Constants.SINGLE_PAYRUN_LIST));
                            }
                            case Constants.CASH_ADVANCE_LIST -> {
                                if (propertyMap != null && propertyMap.get(Constants.CASH_ADVANCE_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.CASH_ADVANCE_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.CASH_ADVANCE_LIST);
                                } else {
                                    localize = wfmLocalizer.localize("cashAdvance");
                                }
                                children.add(new PseudoMenuItem(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_OLD_PAYSLIPS) ? wfmLocalizer.localize("new1") + " " : localize, Constants.CASH_ADVANCE_LIST));
                            }
                            case Constants.ADDITIONAL_PAYMENT_LIST -> {
                                if (propertyMap != null && propertyMap.get(Constants.ADDITIONAL_PAYMENT_LIST) != null) {
                                    EdsProperty edsProperty = propertyMap.get(Constants.ADDITIONAL_PAYMENT_LIST);
                                    localize = getLocalizedSectionTitle(propertyMap, userLanguage, edsProperty, Constants.ADDITIONAL_PAYMENT_LIST);
                                } else {
                                    localize = payrollLocalizer.localize("additionalPayment");
                                }
                                children.add(new PseudoMenuItem(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_OLD_PAYSLIPS) ? wfmLocalizer.localize("new1") + " " : localize, Constants.ADDITIONAL_PAYMENT_LIST));
                            }
                        }
                    }
                }
            }
        }

        return children;
    }

    private ArrayList<PseudoMenuItem> getPayrollReportContainerMenuItems() {
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.PAYROLL_WPS_REPORT);
        permissions.add(PermissionConstants.PAYROLL_END_OF_SERVICE_REPORT);
        permissions.add(PermissionConstants.PAYROLL_PENSION_CONTRIBUTION_REPORT);
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        if (ServerUtils.isArabicCompany(moduleManager.getUser().getCompany())) {
            if (permissions.contains(PermissionConstants.PAYROLL_WPS_REPORT)) {
                children.add(new PseudoMenuItem(payrollLocalizer.localize("wps"), "wpsReport"));
            }
            if (permissions.contains(PermissionConstants.PAYROLL_END_OF_SERVICE_REPORT)) {
                children.add(new PseudoMenuItem(payrollLocalizer.localize("endOfService"), "eosReport"));
            }
            if (permissions.contains(PermissionConstants.PAYROLL_PENSION_CONTRIBUTION_REPORT)) {
                children.add(new PseudoMenuItem(payrollLocalizer.localize("pensionContribution"), "pensionContributionReport"));
            }
        }

        children.add(new PseudoMenuItem(wfmLocalizer.localize("cashAdvance"), "cashAdvanceReport"));
        children.add(new PseudoMenuItem(wfmLocalizer.localize("basicSalary"), "salaryReport"));
        return children;
    }

    public ArrayList<PseudoMenuItem> getHRMSMenuItems(String moduleName) {
        ArrayList<PseudoMenuItem> result = new ArrayList<>();
        List<String> permissions = new ArrayList<>();

        EdsUser user = userManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);

        List<EdsProperty> properties = propertManager.findByModuleCode(moduleName);
        Map<String, EdsProperty> propertyMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(properties)) {
            propertyMap = properties.stream().collect(Collectors.toMap(EdsProperty::getObjectName, x -> x));
        }

        permissions.add(PermissionConstants.HRMS_SECTION);
        permissions.add(PermissionConstants.HRMS_ATTENDANCE_TRACKING_TAB);
        permissions.add(PermissionConstants.HRMS_RECRUITMENT);
        permissions.add(PermissionConstants.HRMS_GOAL_MANAGEMENT);
        permissions.add(PermissionConstants.HRMS_PERFORMANCE_APPRAISALS);
        permissions.add(PermissionConstants.HRMS_ONBOARDING_MANAGEMENT);
        permissions.add(PermissionConstants.HRMS_DOCUMENTS_MANAGEMENT);
        permissions.add(PermissionConstants.HRMS_CURENT_EMPLOYEE_PROFILE_TAB);
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        PseudoMenuItem dashboardContainer = getDashboardContainer(Constants.MODULE_HRMS);
        if (dashboardContainer != null) {
            result.add(dashboardContainer);
        }

        List<EdsContainer> containers = containerManager.getContainerBySorder(moduleName);
        if (containers != null && !containers.isEmpty()) {
            for (EdsContainer container : containers) {
                PseudoMenuItem dynamicSinksContainer = new PseudoMenuItem(container.isChanged() ? (container.getLocalization() != null ? getLocalizedName(userSettings.getInternationalization(), container.getLocalization().getRPC(), container.getDefaultName()) : container.getDefaultName()) : commonLocalizer.localize(container.getDefaultName()), container.getCode());
                dynamicSinksContainer.setChildren(getHrmsMainMenuItems(container, propertyMap, userSettings.getInternationalization()));

                switch (container.getCode()) {
                    case Constants.HRMS_MAIN -> {
                        if (permissions.contains((PermissionConstants.HRMS_SECTION))) {
                            result.add(dynamicSinksContainer);
                        }
                    }
                    case "employee" -> {
                        if (permissions.contains((PermissionConstants.HRMS_CURENT_EMPLOYEE_PROFILE_TAB))) {
                            result.add(dynamicSinksContainer);
                        }
                    }
                    case "availability" -> {
                        if (permissions.contains((PermissionConstants.HRMS_ATTENDANCE_TRACKING_TAB))) {
                            result.add(dynamicSinksContainer);
                        }
                    }
                    case "recruitment" -> {
                        if (permissions.contains((PermissionConstants.HRMS_RECRUITMENT))) {
                            result.add(dynamicSinksContainer);
                        }
                    }
                    case Constants.GOAL -> {
                        if (permissions.contains((PermissionConstants.HRMS_GOAL_MANAGEMENT))) {
                            result.add(dynamicSinksContainer);
                        }
                    }
                    case "pa" -> {
                        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_HRMS_PERFORMANCE_APPRAISALS) && permissions.contains(PermissionConstants.HRMS_PERFORMANCE_APPRAISALS)) {
                            result.add(dynamicSinksContainer);
                        }
                    }
                    default -> result.add(dynamicSinksContainer);
                }
            }
        }

        if (permissions.contains(PermissionConstants.HRMS_ONBOARDING_MANAGEMENT)) {
            PseudoMenuItem onboard = new PseudoMenuItem(hrmsLocalizer.localize("onboarding"), "onboarding");
            onboard.setChildren(getOnboardingMenuItems());
            result.add(onboard);
        }
        return result;
    }

    private ArrayList<PseudoMenuItem> getHrmsMainMenuItems(EdsContainer container, Map<String, EdsProperty> propertyMap, String userlanguage) {
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        List<EdsCustomForm> formList = getCustomFormList(ModuleEnum.HRMS);
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.PM_TASKS_LIST);
        permissions.add(PermissionConstants.PM_PROJECT_LIST);
        permissions.add(PermissionConstants.HRMS_EMPLOYEES);
        permissions.add(PermissionConstants.EMPLOYEE_DOCUMENTS_LIST);
        permissions.add(PermissionConstants.COMPANY_DOCUMENTS_LIST);
        permissions.add(PermissionConstants.BENEFIT_REQUEST_LIST);
        permissions.add(PermissionConstants.MEETING_MINUTES_LIST);
        permissions.add(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_LIST);
        permissions.add(PermissionConstants.HRMS_INCIDENT_LIST);
        permissions.add(PermissionConstants.HRMS_ORGANIZATION_CHART_VIEW);
        permissions.add(PermissionConstants.HRMS_TEAM_ORGANIZATION_CHART_VIEW);
        permissions.add(PermissionConstants.HRMS_COMPANY_NEWS);
        permissions.add(PermissionConstants.HRMS_TELEGRAM_CHAT_LIST);
        permissions.add(PermissionConstants.HRMS_NOTIFICATIONS);
        permissions.add(PermissionConstants.HRMS_EMPLOYEE_PROFILE_SUMMARY);
        permissions.add(PermissionConstants.HRMS_LIVE_REQUEST);
        permissions.add(PermissionConstants.MY_BENEFIT_REQUEST_LIST);
        permissions.add(PermissionConstants.HRMS_EXPENCE_REPORT);
        permissions.add(PermissionConstants.PAYROLL_CASH_ADVANCE_LIST);
        permissions.add(PermissionConstants.HRMS_PAYSLIP_LIST);
        permissions.add(PermissionConstants.HRMS_DEPENDENT);
        permissions.add(PermissionConstants.HRMS_MY_ATTENDANCE);
        permissions.add(PermissionConstants.HRMS_ATTENDANCE_TRACKING);
//        permissions.add(PermissionConstants.HRMS_APPROVE_LIVE_STATUS);
        permissions.add(PermissionConstants.HRMS_ATTENDANCE_REPORT);
        permissions.add(PermissionConstants.HRMS_TERMINAL_REPORT);
        permissions.add(PermissionConstants.HRMS_ATTENDANCE_MARKS);
        permissions.add(PermissionConstants.HRMS_APPROVE_ATTENDANCE_MARKS);
        permissions.add(PermissionConstants.HRMS_ATTENDANCE_TERMINAL_LIST);
        permissions.add(PermissionConstants.HRMS_EMPLOYEE_LIVE_STATUS);
        permissions.add(PermissionConstants.HRMS_ANNUAL_LEAVE_BALANCE_REPORT);
        permissions.add(PermissionConstants.HRMS_VACANCY_LIST_VIEW);
        permissions.add(PermissionConstants.HRMS_VACANCY_SEE_ALL);
        permissions.add(PermissionConstants.HRMS_CANDIDATE_LIST_VIEW);
        permissions.add(PermissionConstants.HRMS_SHORT_LIST_VIEW);
        permissions.add(PermissionConstants.HRMS_PLACEMENT_LIST_VIEW);
        permissions.add(PermissionConstants.HRMS_ACTIVITIES_VIEW);
        permissions.add(PermissionConstants.HRMS_PERSONAL_GOALS);
        permissions.add(PermissionConstants.HRMS_DEPARTMENT_GOALS);
        permissions.add(PermissionConstants.HRMS_GROUP_PERSONAL_GOALS);
        permissions.add(PermissionConstants.HRMS_PROJECT_GOALS);
        permissions.add(PermissionConstants.HRMS_BUSINESS_GOALS);
        permissions.add(PermissionConstants.HRMS_COMPANY_GOALS);
        permissions.add(PermissionConstants.HRMS_SIMPLE_APPRAISALS);
        permissions.add(PermissionConstants.HRMS_TEMPLATES);
        permissions.add(PermissionConstants.HRMS_COMPETENCES);
        permissions.add(PermissionConstants.HRMS_PERFORMANCE_NOTE);
        permissions.add(PermissionConstants.HRMS_SINGLE_PAYRUN_LIST);
        permissions.add(PermissionConstants.HRMS_SHIFT);
        permissions.add(PermissionConstants.HRMS_BRIGADA);
        permissions.add(PermissionConstants.HRMS_ROTATION);
        permissions.add(PermissionConstants.HRMS_GROUP_PLACEMENT);
        permissions.add(PermissionConstants.HRMS_POSITION);
        permissions.add(PermissionConstants.HRMS_BACKUPS_EMPLOYEE_LIST);

        for (EdsCustomForm customForm : formList) {
            permissions.add(customForm.getFormID() + "_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_ADD_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_EDIT_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_SUMMARY_" + permissionManager.getUser().getCompany().getObjectID());
        }
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        String localize;

        List<EdsContainerItem> containerItemList = containerItemManager.getItemsByContainer(container.getObjectID(), false);
        if (containerItemList != null && containerItemList.size() > 0) {
            for (EdsContainerItem containerItem : containerItemList) {
                PropertyItem propertyItem = containerItem.toItem();
                if (propertyItem != null && propertyItem.getObjectName() != null) {
                    switch (propertyItem.getObjectName()) {
//                        case Constants.TASK:
//                            if (permissions.contains(PermissionConstants.PM_TASKS_LIST)) {
//                                localize = propertyMap != null && propertyMap.get(Constants.TASK) != null ? propertyMap.get(Constants.TASK).getPlural() : wfmLocalizer.localize("tasks");
//                                children.add(new PseudoMenuItem(localize, Constants.TASK_LIST));
//                            }
//                            break;
//                        case Constants.PROJECT_LIST:
//                            if (permissions.contains(PermissionConstants.PM_PROJECT_LIST)) {
//                                localize = propertyMap.get(Constants.PROJECT) != null ? propertyMap.get(Constants.PROJECT).getPlural() : wfmLocalizer.localize("projects");
//                                children.add(new PseudoMenuItem(localize, Constants.PROJECT_LIST));
//                            }
//                            break;
                        case Constants.EMLOYEE_LIST:
                            if (permissions.contains(PermissionConstants.HRMS_EMPLOYEES)) {
                                localize = propertyMap != null && propertyMap.get(Constants.EMLOYEE_LIST) != null ? propertyMap.get(Constants.EMLOYEE_LIST).getPlural() : wfmLocalizer.localize("employeesList");
                                children.add(new PseudoMenuItem(localize, Constants.HRMS_EMPLOYEES_LIST));
                            }
                            break;
                        case Constants.EMPLOYEE_DOCUMENTS:
                            if (permissions.contains(PermissionConstants.EMPLOYEE_DOCUMENTS_LIST)) {
                                localize = propertyMap != null && propertyMap.get(Constants.EMPLOYEE_DOCUMENTS) != null ? propertyMap.get(Constants.EMPLOYEE_DOCUMENTS).getPlural() : wfmLocalizer.localize("employeeDocuments");
                                children.add(new PseudoMenuItem(localize, Constants.EMPLOYEE_DOCUMENTS));
                            }
                            break;
                        case Constants.COMPANY_DOCUMENTS:
                            if (permissions.contains(PermissionConstants.COMPANY_DOCUMENTS_LIST)) {
                                localize = propertyMap != null && propertyMap.get(Constants.COMPANY_DOCUMENTS) != null ? propertyMap.get(Constants.COMPANY_DOCUMENTS).getPlural() : wfmLocalizer.localize("companyDocuments");
                                children.add(new PseudoMenuItem(localize, Constants.COMPANY_DOCUMENTS));
                            }
                            break;
                        case Constants.BENEFIT_REQUESTS:
                            if (permissions.contains(PermissionConstants.BENEFIT_REQUEST_LIST)) {
                                children.add(new PseudoMenuItem(availabilityLocalizer.localize("benefitRequests"), Constants.BENEFIT_REQUESTS));
                            } else if (permissions.contains(PermissionConstants.MY_BENEFIT_REQUEST_LIST)) {
                                children.add(new PseudoMenuItem(availabilityLocalizer.localize("benefitRequests"), Constants.BENEFIT_REQUESTS));
                            }
                            break;
                        case "meeting":
                            if (permissions.contains(PermissionConstants.MEETING_MINUTES_LIST)) {
                                localize = propertyMap != null && propertyMap.get(Constants.MEETING) != null ? propertyMap.get(Constants.MEETING).getPlural() : wfmLocalizer.localize("meetingMinutes");
                                children.add(new PseudoMenuItem(localize, Constants.MEETING));
                            }
                            break;
                        case Constants.CERTIFICATES_LIST:
                            if (permissions.contains(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_LIST)) {
                                children.add(new PseudoMenuItem(wfmLocalizer.localize("hrLetters"), Constants.CERTIFICATES_LIST));
                            }
                            break;
                        case "incidentList":
                            if (permissions.contains(PermissionConstants.HRMS_INCIDENT_LIST)) {
                                localize = propertyMap != null && propertyMap.get(Constants.INCIDENT_LIST) != null && propertyMap.get(Constants.INCIDENT_LIST).getPlural() != null ? propertyMap.get(Constants.INCIDENT_LIST).getPlural() : wfmLocalizer.localize("incidents");
                                children.add(new PseudoMenuItem(localize, Constants.INCIDENT_LIST));
                            }
                            break;
                        case "organizationChart":
                            if (permissions.contains(PermissionConstants.HRMS_ORGANIZATION_CHART_VIEW)) {
                                localize = propertyMap != null && propertyMap.get(Constants.ORG_CHART) != null ? propertyMap.get(Constants.ORG_CHART).getPlural() : hrmsLocalizer.localize("supervisorStructure");
                                children.add(new PseudoMenuItem(localize, Constants.ORG_CHART));
                            }
                            break;
                        case Constants.NEW_FLAME_ORG_CHART:
                            if (propertyMap != null) {
                                EdsProperty orgchartProperty = propertyMap.get(Constants.NEW_FLAME_ORG_CHART);
                                String orgchartPseudo = "";
                                if (orgchartProperty != null) {
                                    if (orgchartProperty.getLName() != null) {
                                        orgchartPseudo = orgchartProperty.getLName().getNameLocalization(ServerUtils.getUserLocale().getLanguage());
                                    } else if (orgchartProperty.getlPlural() != null) {
                                        orgchartPseudo = orgchartProperty.getlPlural().getNameLocalization(ServerUtils.getUserLocale().getLanguage());
                                    }
                                    children.add(new PseudoMenuItem(ServerUtils.isNullOrEmpty(orgchartPseudo) ? wfmLocalizer.localize("organizationChart") : orgchartPseudo, Constants.NEW_FLAME_ORG_CHART));
                                }
                            }
                            break;
                        case "departmentOrgChartView":
                            if (permissions.contains(PermissionConstants.HRMS_TEAM_ORGANIZATION_CHART_VIEW)) {
                                localize = propertyMap != null && propertyMap.get(Constants.DEPT_ORG_CHART) != null ? propertyMap.get(Constants.DEPT_ORG_CHART).getPlural() : wfmLocalizer.localize("organizationChart");
                                children.add(new PseudoMenuItem(localize, Constants.DEPT_ORG_CHART));
                            }
                            break;
                        case "notifications":
                            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SERVER_PUSH_NOTIFICATION) && permissions.contains(PermissionConstants.HRMS_NOTIFICATIONS)) {
                                localize = propertyMap != null && propertyMap.get(Constants.NOTIFICATIONS) != null ? propertyMap.get(Constants.NOTIFICATIONS).getPlural() : hrmsLocalizer.localize("notifications");
                                children.add(new PseudoMenuItem(localize, Constants.NOTIFICATIONS));
                            }
                            break;
                        case Constants.NEWS_LIST:
                            if (permissions.contains(PermissionConstants.HRMS_COMPANY_NEWS)) {
                                localize = propertyMap != null && propertyMap.get(Constants.NEWS_LIST) != null ? propertyMap.get(Constants.NEWS_LIST).getPlural() : wfmLocalizer.localize("companyNews");
                                children.add(new PseudoMenuItem(localize, Constants.NEWS_LIST));
                            }
                            break;
                        case Constants.EMPLOYEE_PROFILE_VIEW:
                            if (permissions.contains(PermissionConstants.HRMS_EMPLOYEE_PROFILE_SUMMARY)) {
                                localize = ((propertyMap != null) && (propertyMap.get(Constants.EMPLOYEE_PROFILE_VIEW) != null) && propertyMap.get(Constants.EMPLOYEE_PROFILE_VIEW).getActive()) ? propertyMap.get(Constants.EMPLOYEE_PROFILE_VIEW).getPlural() : hrmsLocalizer.localize("employeeProfile");
                                children.add(new PseudoMenuItem(localize, Constants.EMPLOYEE_PROFILE_VIEW));
                            }
                            break;
                        case "leave_request_list":
                            if (permissions.contains(HRMS_LIVE_REQUEST)) {
                                localize = propertyMap != null && propertyMap.get(Constants.LEAVE_REQUEST_LIST) != null ? propertyMap.get(Constants.LEAVE_REQUEST).getPlural() : wfmLocalizer.localize("leaveRequests");
                                children.add(new PseudoMenuItem(localize, "leaveRequestListView"));
                            }
                            break;
                        case "leave_planner_list":
                            localize = propertyMap != null && propertyMap.get(Constants.LEAVE_PLANNER) != null ? propertyMap.get(Constants.LEAVE_PLANNER).getPlural() : wfmLocalizer.localize("leavePlanner");
                            if (permissions.contains(PermissionConstants.HRMS_LEAVE_PLANNER) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.HRMS_LEAVE_PLANNER)) {

                                children.add(new PseudoMenuItem(localize, "leavePlannerListView"));

                            }

                            break;
                        case Constants.EXPENSES_CLAIM:
                            if (permissions.contains(PermissionConstants.HRMS_EXPENCE_REPORT)) {
                                localize = propertyMap.get(Constants.EXPENSES_CLAIM) != null ? propertyMap.get(Constants.EXPENSES_CLAIM).getPlural() : accountingStrings.localize("expenses");
                                children.add(new PseudoMenuItem(localize, Constants.HRMS_EXPENSE_REPORT_LIST));
                            }
                            break;
                        case Constants.CASH_ADVANCE_LIST:
                            if (permissions.contains(PermissionConstants.PAYROLL_CASH_ADVANCE_LIST)) {
                                localize = propertyMap != null && propertyMap.get(Constants.CASH_ADVANCE_LIST) != null ? propertyMap.get(Constants.CASH_ADVANCE_LIST).getPlural() : wfmLocalizer.localize("cashAdvance");
                                children.add(new PseudoMenuItem(localize, Constants.CASH_ADVANCE_LIST));
                            }
                            break;
                        case Constants.PAYSLIP_LIST:
                            if ((permissions.contains(PermissionConstants.HRMS_PAYSLIP_LIST))) {
                                localize = propertyMap != null && propertyMap.get(Constants.PAYSLIP_LIST) != null ? propertyMap.get(Constants.PAYSLIP_LIST).getPlural() : wfmLocalizer.localize("payslips");
                                children.add(new PseudoMenuItem(localize, "EmployeePayslipList"));
                            }
                            break;
                        case Constants.PERSONAL_GOAL + Constants.GOAL:
                            if (permissions.contains(PermissionConstants.HRMS_PERSONAL_GOALS)) {
                                localize = propertyMap != null && propertyMap.get(Constants.PERSONAL_GOAL + Constants.GOAL) != null ? propertyMap.get(Constants.PERSONAL_GOAL + Constants.GOAL).getPlural() : hrmsLocalizer.localize("personalGoals");
                                children.add(new PseudoMenuItem(localize, Constants.EMPLOYEE_GOAL));
                            }
                            break;
                        case "my_attendance":
                            if (permissions.contains(PermissionConstants.HRMS_MY_ATTENDANCE) && propertyItem.getContainer() != null && "availability".equals(propertyItem.getContainer().getCode())) {
                                children.add(new PseudoMenuItem(wfmLocalizer.localize("myAttendance"), "availability"));
                            } else if (permissions.contains(PermissionConstants.HRMS_LIVE_REQUEST)) {
                                children.add(new PseudoMenuItem(wfmLocalizer.localize("leaveRequests"), "hrmsleaveRequests"));
                            }
//                            if (permissions.contains(PermissionConstants.HRMS_MY_ATTENDANCE)) {
//                                children.add(new PseudoMenuItem(coreLocalizer.localize("myAvailability"), "availability"));
//                            } else if (permissions.contains(PermissionConstants.HRMS_LIVE_REQUEST)) {
//                                children.add(new PseudoMenuItem(coreLocalizer.localize("leaveRequests"), "hrmsleaveRequests"));
//                            }
                            break;
                        case Constants.TEAM_AVAILABILITY_VIEW:
                            if (permissions.contains(PermissionConstants.HRMS_ATTENDANCE_TRACKING)) {
                                localize = propertyMap != null && propertyMap.get(Constants.TEAM_AVAILABILITY_VIEW) != null ? propertyMap.get(Constants.TEAM_AVAILABILITY_VIEW).getPlural() : wfmLocalizer.localize("attendanceTracking");
                                children.add(new PseudoMenuItem(localize, Constants.TEAM_AVAILABILITY_VIEW));
                            }
                            break;
                        case "attendanceReport":
                            if (permissions.contains(PermissionConstants.HRMS_ATTENDANCE_REPORT)) {
                                localize = propertyMap != null && propertyMap.get(Constants.ATTENDANCE_REPORT) != null ? propertyMap.get(Constants.ATTENDANCE_REPORT).getPlural() : wfmLocalizer.localize("attendanceReport");
                                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ATTENDANCE_REPORT)) {
                                    children.add(new PseudoMenuItem(localize, "attendanceReport"));
                                }
                                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ATTENDANCE_REPORT_BETA)) {
                                    children.add(new PseudoMenuItem(localize, "attendanceReport2"));
                                }
                            }
                            break;
                        case "terminalAttendance":
                            if (permissions.contains(PermissionConstants.HRMS_ATTENDANCE_REPORT)) {
                                localize = propertyMap != null && propertyMap.get(Constants.TERMIANL_REPORT) != null ? propertyMap.get(Constants.TERMIANL_REPORT).getPlural() : wfmLocalizer.localize("terminalAttendance");
                                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ATTENDANCE_REPORT_BETA)) {
                                    children.add(new PseudoMenuItem(localize, "terminalAttendance"));
                                }
                            }
                            break;
                        case "attendanceMarksList":
                            if (permissions.contains(PermissionConstants.HRMS_ATTENDANCE_MARKS)) {
                                localize = propertyMap != null && propertyMap.get("attendanceMarksList") != null ? propertyMap.get("attendanceMarksList").getPlural() : wfmLocalizer.localize("attendanceMarks");
                                children.add(new PseudoMenuItem(localize, "attendanceMarksListView"));
                            }
                            break;
                        case "attendanceTerminalList":
                            if (permissions.contains(PermissionConstants.HRMS_ATTENDANCE_TERMINAL_LIST)) {
                                localize = propertyMap != null && propertyMap.get("attendanceTerminalList") != null ? propertyMap.get("attendanceTerminalList").getPlural() : wfmLocalizer.localize("attendanceTerminal");
                                children.add(new PseudoMenuItem(localize, "attendanceTerminal"));
                            }
                            break;
                        case SHIFT:
                            if (permissions.contains(PermissionConstants.HRMS_SHIFT)) {
                                localize = wfmLocalizer.localize("shifts");
                                children.add(new PseudoMenuItem(localize, SHIFT_LIST));
                            }
                            break;

                        case BRIGADA:
                            if (permissions.contains(PermissionConstants.HRMS_BRIGADA)) {
                                localize = propertyMap != null && propertyMap.get("birgadaList") != null ? propertyMap.get("birgadaList").getPlural() : wfmLocalizer.localize("brigadas");
                                children.add(new PseudoMenuItem(localize, BRIGADA_LIST));
                            }
                            break;
                        case ROTATION:
                            if (permissions.contains(PermissionConstants.HRMS_ROTATION)) {
                                localize = hrmsLocalizer.localize("rotations");
                                children.add(new PseudoMenuItem(localize, Constants.ROTATION_LIST));
                            }
                            break;
                        case GROUP_PLACEMENT:
                            if (permissions.contains(PermissionConstants.HRMS_GROUP_PLACEMENT)) {
                                localize = hrmsLocalizer.localize("placementsOnly");
                                children.add(new PseudoMenuItem(localize, Constants.GROUP_PLACEMENT_LIST));
                            }
                            break;
                        case POSITION1:
                            EdsProperty property = propertManager.findByCode(Constants.POSITION1);
                            String positionPseudo = "";
                            if (property != null) {
                                if (property.getlPlural() != null) {
                                    positionPseudo = property.getlPlural().getNameLocalization(ServerUtils.getUserLocale().getLanguage());
                                } else if (!ServerUtils.isNullOrEmpty(property.getPlural())) {
                                    positionPseudo = property.getPlural();
                                }
                            }
                            children.add(new PseudoMenuItem(ServerUtils.isNullOrEmpty(positionPseudo) ? commonLocalizer.localize("positions") : positionPseudo, "hrmsPositions"));
                            break;

                        case "annualLeaveBalance":
                            if (permissions.contains(PermissionConstants.HRMS_ANNUAL_LEAVE_BALANCE_REPORT)) {
                                EdsLeaveReason reason = leaveReasonManager.findByCode(CustomFormConstants.LR_TYPE_ANNUAL_LEAVE);
                                localize = propertyMap != null && propertyMap.get(Constants.ANNUALE_LEAVE_BALANCE) != null ? propertyMap.get(Constants.ANNUALE_LEAVE_BALANCE).getPlural() : availabilityLocalizer.localize("annualLeaveBalance");
                                if (reason != null && reason.hasProrata()) {
                                    children.add(new PseudoMenuItem(localize, "annualLeaveBalance"));
                                } else {
                                    children.add(new PseudoMenuItem(localize, "leaveBalanceReport"));
                                }
                            }
                            break;
                        case Constants.VACANCY:
                            if (permissions.contains(PermissionConstants.HRMS_VACANCY_LIST_VIEW) || permissions.contains(PermissionConstants.HRMS_VACANCY_SEE_ALL)) {
                                EdsProperty vacancyProperty = propertManager.findByCode(Constants.VACANCY);
                                String vacancyPseudo = "";
                                if (vacancyProperty != null) {
                                    if (vacancyProperty.getlPlural() != null) {
                                        vacancyPseudo = vacancyProperty.getlPlural().getNameLocalization(ServerUtils.getUserLocale().getLanguage());
                                    } else if (!ServerUtils.isNullOrEmpty(vacancyProperty.getPlural())) {
                                        vacancyPseudo = vacancyProperty.getPlural();
                                    }
                                }
                                children.add(new PseudoMenuItem(ServerUtils.isNullOrEmpty(vacancyPseudo) ? hrmsLocalizer.localize("vacancies") : vacancyPseudo, "vacancyAnnouncements"));
                            }
                            break;
                        case Constants.CANDIDATE:
                            if (permissions.contains(PermissionConstants.HRMS_CANDIDATE_LIST_VIEW)) {
                                localize = propertyMap != null && propertyMap.get(Constants.CANDIDATE) != null ? propertyMap.get(Constants.CANDIDATE).getPlural() : wfmLocalizer.localize("candidates");
                                children.add(new PseudoMenuItem(localize, "candidatesList"));
                            }
                            break;
                        case Constants.PLACEMENT:
                            if (permissions.contains(PermissionConstants.HRMS_PLACEMENT_LIST_VIEW)) {
                                localize = propertyMap != null && propertyMap.get(Constants.PLACEMENT) != null ? propertyMap.get(Constants.PLACEMENT).getPlural() : hrmsLocalizer.localize("placementsOnly");
                                children.add(new PseudoMenuItem(localize, "placementsListView"));
                            }
                            break;
                        case Constants.EVENT_LIST:
                            if (permissions.contains(PermissionConstants.HRMS_ACTIVITIES_VIEW)) {
                                localize = propertyMap != null && propertyMap.get(Constants.EVENT_LIST) != null ? propertyMap.get(Constants.EVENT_LIST).getPlural() : wfmLocalizer.localize("activities");
                                children.add(new PseudoMenuItem(localize, Constants.EVENT_LIST));
                            }
                            break;
                        case Constants.DEPARTMENT_GOAL:
                            if (permissions.contains(PermissionConstants.HRMS_DEPARTMENT_GOALS)) {
                                localize = propertyMap != null && propertyMap.get(Constants.DEPARTMENT_GOAL) != null ? propertyMap.get(Constants.DEPARTMENT_GOAL).getPlural() : wfmLocalizer.localize("groupGoals");
                                children.add(new PseudoMenuItem(localize, Constants.DEPARTMENT_GOAL));
                            }
                            break;
                        case Constants.GROUP_GOAL:
                            if (permissions.contains(PermissionConstants.HRMS_GROUP_PERSONAL_GOALS)) {
                                localize = propertyMap.get(Constants.GROUP_GOAL) != null ? propertyMap.get(GROUP_GOAL).getPlural() : hrmsLocalizer.localize("groupGoals");
                                children.add(new PseudoMenuItem(localize, Constants.GROUP_GOAL));
                            }
                            break;
                        case Constants.PROJECT_GOAL:
                            if (permissions.contains(PermissionConstants.HRMS_PROJECT_GOALS)) {
                                localize = propertyMap.get(Constants.PROJECT_GOAL) != null ? propertyMap.get(PROJECT_GOAL).getPlural() : hrmsLocalizer.localize("projectGoals");
                                children.add(new PseudoMenuItem(localize, Constants.PROJECT_GOAL));
                            }
                            break;
                        case Constants.BUSINESS_GOAL + Constants.GOAL:
                            if (permissions.contains(PermissionConstants.HRMS_BUSINESS_GOALS)) {
                                localize = propertyMap != null && propertyMap.get(Constants.BUSINESS_GOAL + Constants.GOAL) != null ? propertyMap.get(Constants.BUSINESS_GOAL + Constants.GOAL).getPlural() : wfmLocalizer.localize("businessGoals");
                                children.add(new PseudoMenuItem(localize, Constants.BUSINESS_GOAL + "goals"));
                            }
                            break;
                        case Constants.COMPANY_GOAL + Constants.GOAL:
                            if (permissions.contains(PermissionConstants.HRMS_COMPANY_GOALS)) {
                                localize = propertyMap != null && propertyMap.get(Constants.COMPANY_GOAL + Constants.GOAL) != null ? propertyMap.get(Constants.COMPANY_GOAL + Constants.GOAL).getPlural() : hrmsLocalizer.localize("companyGoals");
                                children.add(new PseudoMenuItem(localize, "companygoals"));
                            }
                            break;
                        case "simpleAppraisal":
                            if (permissions.contains(PermissionConstants.HRMS_SIMPLE_APPRAISALS)) {
                                localize = propertyMap != null && propertyMap.get("simpleAppraisal") != null ? propertyMap.get("simpleAppraisal").getPlural() : hrmsLocalizer.localize("simpleAppraisals");
                                children.add(new PseudoMenuItem(localize, Constants.PA_HOME_VIEW));
                            }
                            break;
                        case "appraisalsArchive":
                            if (permissions.contains(PermissionConstants.HRMS_SIMPLE_APPRAISALS)) {
                                localize = propertyMap != null && propertyMap.get(Constants.AP_ARCHIEVE) != null ? propertyMap.get(Constants.AP_ARCHIEVE).getPlural() : hrmsLocalizer.localize("appraisalsArchive");
                                children.add(new PseudoMenuItem(localize, Constants.PA_ARCHIVE));
                            }
                            break;
                        case "appraisalTemplate":
                            if (permissions.contains(PermissionConstants.HRMS_TEMPLATES)) {
                                localize = propertyMap != null && propertyMap.get(Constants.AP_TEMPLATE) != null ? propertyMap.get(Constants.AP_TEMPLATE).getPlural() : hrmsLocalizer.localize("templates");
                                children.add(new PseudoMenuItem(localize, "templates"));
                            }
                            break;
                        case "competencesView":
                            if (permissions.contains(PermissionConstants.HRMS_COMPETENCES)) {
                                localize = propertyMap != null && propertyMap.get("competencesView") != null ? propertyMap.get("competencesView").getPlural() : wfmLocalizer.localize("competences");
                                children.add(new PseudoMenuItem(localize, "competencesView"));
                            }
                            break;
                        case "competencesGroupView":
                            if (permissions.contains(PermissionConstants.HRMS_COMPETENCES)) {
                                localize = propertyMap != null && propertyMap.get("competencesGroupView") != null ? propertyMap.get("competencesGroupView").getPlural() : hrmsLocalizer.localize("groups");
                                children.add(new PseudoMenuItem(localize, "competencesGroupView"));
                            }
                            break;
                        case "performanceNote":
                            if (permissions.contains(PermissionConstants.HRMS_PERFORMANCE_NOTE)) {
                                localize = propertyMap != null && propertyMap.get("performanceNote") != null ? propertyMap.get("performanceNote").getPlural() : wfmLocalizer.localize("performanceNotes");
                                children.add(new PseudoMenuItem(localize, "noteList"));
                            }
                            break;
                        case "singlePayrunList":
                            if (permissions.contains(PermissionConstants.HRMS_SINGLE_PAYRUN_LIST)) {
                                localize = propertyMap != null && propertyMap.get(Constants.SINGLE_PAYRUN_LIST) != null ? propertyMap.get(Constants.SINGLE_PAYRUN_LIST).getPlural() : wfmLocalizer.localize("payslips");
                                children.add(new PseudoMenuItem(localize, Constants.SINGLE_PAYRUN_LIST));
                            }
                            break;
                        case Constants.BACKUPS_EMPLOYEE:
                            if (permissions.contains(PermissionConstants.HRMS_BACKUPS_EMPLOYEE_LIST)) {
                                localize = wfmLocalizer.localize("backupEmployee");
                                children.add(new PseudoMenuItem(localize, Constants.BACKUPS_EMPLOYEE));
                            }
                            break;
                        default:
                            if (permissions.contains(propertyItem.getFormID() + "_" + permissionManager.getUser().getCompany().getObjectID())) {
                                if (Constants.PAGE.equals(propertyItem.getType())) {
                                    List<Integer> customFormItems = customFormItemManager.getCustomFormItemsByFormId(propertyItem.getfID());
                                    if (customFormItems != null && !customFormItems.isEmpty()) {
                                        propertyItem.setSelectedItemID(customFormItems.get(0));
                                    }
                                    if (propertyItem.getSelectedItemID() != null && permissions.contains(propertyItem.getFormID() + "_SUMMARY_" + permissionManager.getUser().getCompany().getObjectID())) {
                                        children.add(new PseudoMenuItem(getLocalizedName(userlanguage, propertyItem.getlPlural(), propertyItem.getPlural()), Constants.ITEM_LIST + "|summary/" + propertyItem.getSelectedItemID() + "/" + propertyItem.getfID() + "/" + propertyItem.getFormID() + "/" + propertyItem.getPlural() + "/PAGE", true));
                                    } else if (propertyItem.getSelectedItemID() != null && permissions.contains(propertyItem.getFormID() + "_EDIT_" + permissionManager.getUser().getCompany().getObjectID()) || permissions.contains(propertyItem.getFormID() + "_ADD_" + permissionManager.getUser().getCompany().getObjectID())) {

                                        String id = propertyItem != null && propertyItem.getSelectedItemID() != null ? propertyItem.getSelectedItemID().toString() : "";
                                        children.add(new PseudoMenuItem(getLocalizedName(userlanguage, propertyItem.getlPlural(), propertyItem.getPlural()), Constants.ITEM_LIST + "|add/add/" + id + "/" + propertyItem.getfID() + "/" + propertyItem.getFormID() + "/" + propertyItem.getPlural() + "//" + "/PAGE", true));
                                    }
                                } else {
                                    children.add(new PseudoMenuItem(getLocalizedName(userlanguage, propertyItem.getlPlural(), propertyItem.getPlural()), "custom_form_" + propertyItem.getfID()));
                                }
                            }
                    }
                }
            }
        }

        return children;
    }

    private ArrayList<PseudoMenuItem> getOnboardingMenuItems() {
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.HRMS_ONBOARDING_STEP_LIST);
        permissions.add(PermissionConstants.HRMS_ONBOARDING_LIST);
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        if (permissions.contains(PermissionConstants.HRMS_ONBOARDING_STEP_LIST)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("onboardingStep"), Constants.ONBOARDING_STEP));
        }
        //onboardings period
        if (permissions.contains(PermissionConstants.HRMS_ONBOARDING_LIST)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("onboardingPeriod"), Constants.ONBOARDING_PERIOD));
        }
        ArrayList<OnboardingItem> onboardingSteps = hrmsService.getOnboardingStepsForListing();
        HashSet<String> onboardingPermissions = permissionManager.loadOnBoardingPermissions();
        for (OnboardingItem onboardingStep : onboardingSteps) {
            if (onboardingPermissions.contains(getOnboardingStepPermission(onboardingStep))) {
                children.add(new PseudoMenuItem(onboardingStep.getStepName(), onboardingStep.getStepName()));
            }
        }
        return children;
    }

    private String getOnboardingStepPermission(OnboardingItem onboardingItem) {
        return PermissionConstants.EMPLOYEE_STEP_ + onboardingItem.getFormID().replaceAll(Constants.ONBOARDING_STEP_FORM, "") + "_LIST";
    }

    public ArrayList<PseudoMenuItem> getDocsMenuItems() {
        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        children.add(new PseudoMenuItem(wfmLocalizer.localize("all"), Constants.DOCUMENTS_FOLDER_ALL));
        children.add(new PseudoMenuItem(wfmLocalizer.localize("sysTemFolder"), Constants.DOCUMENTS_FOLDER_SYSTEM));
        children.add(new PseudoMenuItem(wfmLocalizer.localize("myFolders"), Constants.DOCUMENTS_FOLDER_MYFOLDERS));
        children.add(new PseudoMenuItem(wfmLocalizer.localize("publicFolder"), Constants.DOCUMENTS_FOLDER_PUBLIC));
        children.add(new PseudoMenuItem(wfmLocalizer.localize("sharedByMe"), Constants.DOCUMENTS_FOLDER_SHARED));
        children.add(new PseudoMenuItem(wfmLocalizer.localize("sharedWithMe"), Constants.DOCUMENTS_FOLDER_OTHERS));
        children.add(new PseudoMenuItem(wfmLocalizer.localize("trashBin"), Constants.DOCUMENTS_FOLDER_TRASH));

        List<EdsCustomForm> formList = getCustomFormList(ModuleEnum.DOCUMENTS);
        for (EdsCustomForm customForm : formList) {
            permissions.add(customForm.getFormID() + "_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_ADD_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_EDIT_" + permissionManager.getUser().getCompany().getObjectID());
            permissions.add(customForm.getFormID() + "_SUMMARY_" + permissionManager.getUser().getCompany().getObjectID());
        }
        permissions = (ArrayList<String>) permissionManager.getPermissions(permissions, permissionManager.getUser());

        for (EdsCustomForm form : formList) {
            if (form != null && form.getProperty() != null) {
                PropertyItem propertyItem = form.getProperty().toItem(true);

                if (permissions.contains(propertyItem.getFormID() + "_" + permissionManager.getUser().getCompany().getObjectID())) {
                    if (Constants.PAGE.equals(propertyItem.getType())) {
                        List<Integer> customFormItems = customFormItemManager.getCustomFormItemsByFormId(propertyItem.getfID());
                        if (customFormItems != null && !customFormItems.isEmpty()) {
                            propertyItem.setSelectedItemID(customFormItems.get(0));
                        }
                        if (propertyItem.getSelectedItemID() != null && permissions.contains(propertyItem.getFormID() + "_SUMMARY_" + permissionManager.getUser().getCompany().getObjectID())) {
                            children.add(new PseudoMenuItem(propertyItem.getPlural(), Constants.ITEM_LIST + "|summary/" + propertyItem.getSelectedItemID() + "/" + propertyItem.getfID() + "/" + propertyItem.getFormID() + "/" + propertyItem.getPlural() + "/PAGE", true));
                        } else if (propertyItem.getSelectedItemID() != null && permissions.contains(propertyItem.getFormID() + "_EDIT_" + permissionManager.getUser().getCompany().getObjectID()) || permissions.contains(propertyItem.getFormID() + "_ADD_" + permissionManager.getUser().getCompany().getObjectID())) {

                            String id = propertyItem != null && propertyItem.getSelectedItemID() != null ? propertyItem.getSelectedItemID().toString() : "";
                            children.add(new PseudoMenuItem(propertyItem.getPlural(), Constants.ITEM_LIST + "|add/add/" + id + "/" + propertyItem.getfID() + "/" + propertyItem.getFormID() + "/" + propertyItem.getPlural() + "//" + "/PAGE", true));
                        }
                    } else {
                        children.add(new PseudoMenuItem(propertyItem.getPlural(), "custom_form_" + propertyItem.getfID()));
                    }
                }
            }
        }
        return children;
    }

    public ArrayList<PseudoMenuItem> getLogisticsMenuItems() {
        ArrayList<PseudoMenuItem> result = new ArrayList<>();
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.LOGISTICS_MAIN_MENU);
        permissions.add(PermissionConstants.LOGISTICS_SUPPLIER_LIST);
        permissions.add(PermissionConstants.LOGISTICS_PURCHASE_ORDER_LIST);
        permissions.add(PermissionConstants.LOGISTICS_PURCHASE_INVOICE_LIST);
        permissions.add(PermissionConstants.LOGISTICS_RECURRING_BILL_LIST);
        permissions.add(PermissionConstants.LOGISTICS_PRODUCT_LIST);
        permissions.add(PermissionConstants.LOGISTICS_INVENTORY_LIST);
        permissions.add(PermissionConstants.LOGISTICS_WAREHOUSE_LIST);
        permissions.add(PermissionConstants.LOGISTICS_STOCK_TRANSFER_LIST);
        permissions.add(PermissionConstants.LOGISTICS_STOCK_ADJUSTMENT_LIST);
        permissions.add(PermissionConstants.LOGISTICS_STOCK_VALUATION);
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());

        PseudoMenuItem logistics = new PseudoMenuItem(wfmLocalizer.localize("logistics"), Constants.LOGISTICS);

        if (permissions.contains(PermissionConstants.LOGISTICS_SUPPLIER_LIST)) {
            children.add(new PseudoMenuItem(wfmLocalizer.localize("supplierCenter"), Constants.SUPPLIER_LIST));
        }

        if (permissions.contains(PermissionConstants.LOGISTICS_PURCHASE_ORDER_LIST)) {
            children.add(new PseudoMenuItem(accountingStrings.localize("purchaseorder"), Constants.PURCHASE_ORDER));
        }

        if (permissions.contains(PermissionConstants.LOGISTICS_PURCHASE_INVOICE_LIST)) {
            children.add(new PseudoMenuItem(wfmLocalizer.localize("purchaseinvoice"), Constants.PURCHASE_INVOICE));
        }

        if (permissions.contains(PermissionConstants.LOGISTICS_RECURRING_BILL_LIST)) {
            children.add(new PseudoMenuItem(accountingStrings.localize("recurringBill"), Constants.RECURRING_BILL));
        }

        if (permissions.contains(PermissionConstants.LOGISTICS_PRODUCT_LIST)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("productsOrServices"), "productList"));
        }

        if (permissions.contains(PermissionConstants.LOGISTICS_INVENTORY_LIST)) {
            children.add(new PseudoMenuItem(accountingStrings.localize("inventoryItems"), Constants.INVENTORY_ITEMS));
        }

        if (permissions.contains(PermissionConstants.LOGISTICS_WAREHOUSE_LIST)) {
            children.add(new PseudoMenuItem(inventoryLocalizer.localize("warehouses"), "warehouseList"));
        }

        if (permissions.contains(PermissionConstants.LOGISTICS_STOCK_TRANSFER_LIST)) {
            children.add(new PseudoMenuItem(inventoryLocalizer.localize("warehouses"), "warehouseList"));
        }

        if (permissions.contains(PermissionConstants.LOGISTICS_STOCK_ADJUSTMENT_LIST)) {
            children.add(new PseudoMenuItem(wfmLocalizer.localize("stockAdjustments"), "stockAdjustments"));
        }

        if (permissions.contains(PermissionConstants.LOGISTICS_STOCK_VALUATION)) {
            children.add(new PseudoMenuItem(inventoryLocalizer.localize("stockValuation"), "stockValuation"));
        }
        logistics.setChildren(children);
        result.add(logistics);
        return result;
    }

    public ArrayList<PseudoMenuItem> getTrainingCentesMenuItems() {
        ArrayList<PseudoMenuItem> result = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.TC_OPERATION_MENU);
        permissions.add(PermissionConstants.TC_ASSESSMENT_MENU);
        permissions.add(PermissionConstants.TC_SCHEDULE);
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());

        if (permissions.contains(PermissionConstants.TC_OPERATION_MENU)) {
            PseudoMenuItem operation = new PseudoMenuItem(commonLocalizer.localize("operation"), "operation");
            operation.setChildren(getOperationContainerMenuItems());
            result.add(operation);
        }

        if (permissions.contains(PermissionConstants.TC_ASSESSMENT_MENU)) {
            PseudoMenuItem operation = new PseudoMenuItem(commonLocalizer.localize("assessment"), "assessment");
            operation.setChildren(getAssessmentContainerMenuItems());
            result.add(operation);
        }

        if (permissions.contains(PermissionConstants.TC_SCHEDULE)) {
            PseudoMenuItem operation = new PseudoMenuItem(commonLocalizer.localize("consolidatedInvoice"), "consolidatedInvoice");
            operation.setChildren(getConsolidatedInvoiceContainerMenuItems());
            result.add(operation);
        }


        return result;
    }


    private ArrayList<PseudoMenuItem> getOperationContainerMenuItems() {
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.TC_SCHEDULED_COURSE_LIST_VIEW);
        permissions.add(PermissionConstants.TC_COURSE_LIST_VIEW);
        permissions.add(PermissionConstants.TC_COURSE_SUBJECT_LIST_VIEW);
        permissions.add(PermissionConstants.TC_BOOKING_ITEMS_LIST_VIEW);
        permissions.add(PermissionConstants.TC_EMPLOYEE_LIST_VIEW);
        permissions.add(PermissionConstants.TC_STUDENT_LIST_VIEW);
        permissions.add(PermissionConstants.TC_ATTENDENCE_SHEET);
        permissions.add(PermissionConstants.TC_INSTRUCTOR_REASSIGN_LIST_VIEW);
        permissions.add(PermissionConstants.TC_COURSE_BOOKING_LIST_VIEW);
        permissions.add(PermissionConstants.TC_TRAINING_CONTRACT_LIST_VIEW);
        permissions.add(PermissionConstants.TC_CERTIFICATES_LIST_VIEW);
        permissions.add(PermissionConstants.TC_PASSPORT_LIST_VIEW);
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        if (permissions.contains(PermissionConstants.TC_SCHEDULED_COURSE_LIST_VIEW)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("courseSchedules"), "scheduledcourses"));
        }
        if (permissions.contains(PermissionConstants.TC_COURSE_LIST_VIEW)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("courses"), "course"));
        }
        if (permissions.contains(PermissionConstants.TC_COURSE_SUBJECT_LIST_VIEW)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("courseSubject"), "coursesubject"));
        }
        if (permissions.contains(PermissionConstants.TC_BOOKING_ITEMS_LIST_VIEW)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("bookingItems"), "bookingItemsList"));
        }
        if (permissions.contains(PermissionConstants.TC_EMPLOYEE_LIST_VIEW)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("instructors"), "employee"));
        }
        if (permissions.contains(PermissionConstants.TC_STUDENT_LIST_VIEW)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("students"), "students"));
        }
        if (permissions.contains(PermissionConstants.TC_ATTENDENCE_SHEET)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("attendenceSheet"), "attendenceSheet"));
        }
        if (permissions.contains(PermissionConstants.TC_INSTRUCTOR_REASSIGN_LIST_VIEW)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("instructorReassign"), "instructorReassignList"));
        }
        if (permissions.contains(PermissionConstants.TC_COURSE_BOOKING_LIST_VIEW)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("courseBooking"), "courseBooking"));
        }
        if (permissions.contains(PermissionConstants.TC_TRAINING_CONTRACT_LIST_VIEW)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("customerContracts"), "trainingContract"));
        }
        if (permissions.contains(PermissionConstants.TC_CERTIFICATES_LIST_VIEW)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("certificates"), "certificate"));
        }
        if (permissions.contains(PermissionConstants.TC_PASSPORT_LIST_VIEW)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("hsePassports"), "passport"));
        }
        return children;
    }

    private ArrayList<PseudoMenuItem> getAssessmentContainerMenuItems() {
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.TC_CONFIRMED_SCHEDULED_COURCE_LIST_VIEW);

        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        if (permissions.contains(PermissionConstants.TC_CONFIRMED_SCHEDULED_COURCE_LIST_VIEW)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("confirmedScheduledCourse"), "confirmedscheduledcourses"));
        }

        return children;
    }

    private ArrayList<PseudoMenuItem> getConsolidatedInvoiceContainerMenuItems() {
        ArrayList<PseudoMenuItem> children = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.TC_INVOICE_GENERATOR_VIEW);
        permissions.add(PermissionConstants.TC_SCHEDUL_VIEW);

        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        if (permissions.contains(PermissionConstants.TC_INVOICE_GENERATOR_VIEW)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("invoiceGenerator"), "invoicegenerator"));
        }
        if (permissions.contains(PermissionConstants.TC_SCHEDUL_VIEW)) {
            children.add(new PseudoMenuItem(commonLocalizer.localize("consolidatedInvoice"), "scheduleinvoice"));
        }

        return children;
    }

    public ArrayList<PseudoMenuItem> getReportingMenuItems() {
        ArrayList<PseudoMenuItem> result = new ArrayList<>();

        List<String> permissions = new ArrayList<>();
        permissions.add(PermissionConstants.REPORTING_MAIN_MENU);
        permissions = permissionManager.getPermissions(permissions, permissionManager.getUser());
        if (permissions.contains(PermissionConstants.REPORTING_MAIN_MENU)) {
            PseudoMenuItem reporting = new PseudoMenuItem(wfmLocalizer.localize("sections"), Constants.REPORTING_CATEGORY);
            List<ReportingCategoryRPC> categories = reportingService.getCategories();
            if (categories != null && categories.size() > 0) {
                ArrayList<PseudoMenuItem> reportChildren = new ArrayList<>();
                for (ReportingCategoryRPC category : categories) {
                    reportChildren.add(new PseudoMenuItem(category.getName(), "reportingHome" + category.getId()));
                }
                reporting.setChildren(reportChildren);
            }
            result.add(reporting);
        }
        return result;
    }

    private PseudoMenuItem getDashboardContainer(String moduleCode) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setModule(moduleCode);
        fp.setLimit(50);
        List<SelectItem> dashboards = moduleDashboardService.getModuleDashboards(fp);
        if (dashboards.isEmpty()) {
            return null;
        }
        String localizedName = wfmLocalizer.localize("dashboards");
        PseudoMenuItem result = new PseudoMenuItem(localizedName, "dashboard");
        ArrayList<PseudoMenuItem> list = Lists.newArrayList();
        for (SelectItem item : dashboards) {
            PseudoMenuItem menu = new PseudoMenuItem();
            if ("Dashboard".equals(item.getName()) || "HR Dashboard".equals(item.getName())) {
                menu.setName(localizedName);
            } else {
                menu.setName(item.getName());
            }
            menu.setUrl("dashboard_" + item.getId());
            list.add(menu);
        }
        result.setDashboard(true);
        result.setChildren(list);
        return result;
    }

    public String normalizeName(String name) {
        if (StringUtils.isEmpty(name)) {
            return "";
        }
        return name.replace(" ", "_")
                .replace("/", "_");
    }

    private List<EdsCustomForm> getCustomFormList(ModuleEnum context) {
        return customFormManager.findByContext(context.getCode());
    }

    private String format(String format, final String... args) {
        String retVal = format;
        for (final String current : args) {
            retVal = retVal.replaceFirst("[%][s]", current);
        }
        return retVal;
    }
}
