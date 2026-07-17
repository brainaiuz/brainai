package com.edatasite.workforce.gwt.profile.client.factory;

import com.edatasite.workforce.gwt.accounting.client.history.ClientImportHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.SupplierImportHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.AddAccountHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.AddProductHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.BuildAssemblyItemHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.ChartOfAccountsSummaryHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.CustomFormLocalizationHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.DiscountHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.ImportBrandHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.ImportChartOfAccountsHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.ImportProductCategoriesHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.PaymentMethodListHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.PriceLevelHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.StockAdjustmentHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.StockTransferHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.TaxHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.guide.GuideHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.AddBrandHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.AddProductCategoryHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.BrandsListHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.ProductCategoriesListHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.ProductCategorySummaryHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.UnitMeasurementsListHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.report.ClickedReportHistoryProcessor;
import com.edatasite.workforce.gwt.assessment.client.ui.history.AppraisalsSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.availability.client.history.AttendanceTerminalHistoryProcessor;
import com.edatasite.workforce.gwt.availability.client.history.HolidayHistoryProcessor;
import com.edatasite.workforce.gwt.availability.client.history.ShiftSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.availability.client.history.TimeslotHistoryProcessor;
import com.edatasite.workforce.gwt.client.client.history.ClientEditHistoryProcessor;
import com.edatasite.workforce.gwt.client.client.history.ClientHistoryProcessor;
import com.edatasite.workforce.gwt.client.client.history.SupplierHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.history.WebhookListHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.WorkflowWebHookEditHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.WorkflowWebHookHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.crm.client.history.OpportunityHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.history.EmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.history.SingleEmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.googlecalendar.client.history.GoogleCalendarHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.AddPositionHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.CustomizeCertificateHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.EmployeeLeaveTypesHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.EmployeeProfileEditHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.EmployeeProfileHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.HrmsEmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.HrmsPositionHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.ImportDepartmentHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.ImportPositionHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.MultiDepartmentHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.MultiPositionHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.PositionSummaryViewHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.VacancyHistoryProcessor;
import com.edatasite.workforce.gwt.hrms.client.history.WorkflowEmployeeStepHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.purchaseinvoice.PayableCreditNoteHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.purchaseinvoice.PurchaseInvoiceHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.purchaseorder.PurchaseOrderHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.saleinvoice.ReceivableCreditNoteHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.saleinvoice.SaleInvoiceHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.salequote.SaleOrderHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.salequote.SaleQuoteHistoryProcessor;
import com.edatasite.workforce.gwt.issue.client.history.IssueHistoryProcessor;
import com.edatasite.workforce.gwt.location.client.history.LocationHistoryProcessor;
import com.edatasite.workforce.gwt.messagecenter.client.history.EmailHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.PayrollSettingsSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.history.EmployerSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.EndOfServiceHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.PayrollBatchHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.PayrollCategoryV2HistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.PayrollGlobalSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.PensionProviderHistoryProcessor;
import com.edatasite.workforce.gwt.payroll.client.history.PensionSchemeHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.AttendanceTerminalIntegrationSinksContainer;
import com.edatasite.workforce.gwt.profile.client.CRMSettingsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.CollaborationSettingsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.CommunicationSettingsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.CompanySettingsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.CustomizationSettingsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.DocumentIntegrationSinksContainer;
import com.edatasite.workforce.gwt.profile.client.ECommerceSettingsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.EmailSettingsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.HrmsSettingsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.IntegrationSinksContainer;
import com.edatasite.workforce.gwt.profile.client.InvoiceSettingsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.ModuleDashboardSinksContainer;
import com.edatasite.workforce.gwt.profile.client.PMSettingsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.PaymentGatewaysSettingsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.PermissionSinksContainer;
import com.edatasite.workforce.gwt.profile.client.ProfileSettingsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.RecruitmentIntegrationSinksContainer;
import com.edatasite.workforce.gwt.profile.client.RecurrenceSettingsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.SignupCompanySettingsSinkContainer;
import com.edatasite.workforce.gwt.profile.client.SystemLogsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.UserCredentialsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.container.ConsalidationSinkContainer;
import com.edatasite.workforce.gwt.profile.client.history.AsteriskEmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.AsteriskEmployeeListHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.AsteriskSettingHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.BenefitHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.CRMSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.CompanySettingsHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.ConsolidationHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.CounrtySettingsHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.CreateEMLTemplatesHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.CustomFieldManagementHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.CustomizationSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.EmailFilterHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.EmailSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.EmailTemplateEditHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.EmailTemplateHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.EmployeeBenefitHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.HrmsSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.ImportEmployeeLeaveAllowanceHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.InvoiceSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.LeaveReasonHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.MinimumWageHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.ModuleDashboardHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.ModuleSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.MyCallsSettingHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.PMSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.PayrollZoneHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.PermissionHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.ProfileSettingsEditViewHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.ProfileSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.RecurrenceSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.ReferenceHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.ReferenceLocaleHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.SMSSettingHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.SMSTemplateHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.SettingsPdfTemplateHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.SignatureEditHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.SignatureHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.SignupCompanyHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.SipuniSettingHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.SwitchvoxHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.SystemLogsHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.TelegramBotSettingHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.TelephonySettingsListHistoryProcesser;
import com.edatasite.workforce.gwt.profile.client.history.TwilioSettingHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.UserCredentialsHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.WageRateHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.WorkflowAlertHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.WorkflowEmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.WorkflowEventHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.WorkflowHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.WorkflowInvoiceHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.WorkflowSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.WorkflowTelegramAlertHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.WorkflowSettingsSinksContainer;
import com.edatasite.workforce.gwt.task.client.history.TaskEditHistoryProcessor;
import com.edatasite.workforce.gwt.task.client.history.TaskHistoryProcessor;
import com.edatasite.workforce.gwt.team.client.history.TeamHistoryProcessor;

//import com.edatasite.workforce.gwt.hrms.client.history.AddJobDescriptionHistoryProcessor;

/**
 * User: Admin
 * Date: 13.10.2008
 * Time: 12:22:20
 */
public class SettingsSinksContainerFactory extends SinksContainerFactory {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SettingsSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
        setDefaultContainer("companySettings");
    }

    @Override
    public void initDefaultContainers() {
        if (!Utils.isAnyDataMissing()) {
//            SinksContainer settings = new SettingsSinksContainer();
//            settings.setPreparedView("settingsHome");
//            setSinksContainer(settings);

            if (Utils.hasPermission(PermissionConstants.SETTINGS_COMPANY_SETTINGS)) {
                SinksContainer companySettings = new CompanySettingsSinksContainer("companySettingsHome", wfmStrings.company(), null);
                companySettings.setPreparedView("companySettings");
                setSinksContainer(companySettings);
            }
            if (Utils.hasPermission(PermissionConstants.SETTINGS_PROFILE_SETTINGS)) {
                SinksContainer profile = new ProfileSettingsSinksContainer("profileSettings", wfmStrings.contactprofile(), null);
                profile.setPreparedView("profile");
                setSinksContainer(profile);
            }
            if (Utils.hasPermission(PermissionConstants.SETTINGS_USER_CREDENTIALS)) {
                SinksContainer userCredential = new UserCredentialsSinksContainer("userCredential", settingsStrings.userCredentials(), null);
                userCredential.setPreparedView("credentials");
                setSinksContainer(userCredential);
            }
            if (Utils.hasPermission(PermissionConstants.SETTINGS_ACCOUNTING_SETTINGS)) {
                SinksContainer invoiceSettings = new InvoiceSettingsSinksContainer("accountingSettings", wfmStrings.accounting());
                invoiceSettings.setPreparedView("invoiceSettings");
                setSinksContainer(invoiceSettings);
            }
            if (Utils.hasPermission(PermissionConstants.SETTINGS_CRM_SETTINGS)) {
                SinksContainer crmSettings = new CRMSettingsSinksContainer("customFieldsSettings", wfmStrings.sales(), null);
                crmSettings.setPreparedView("opportunitySettings");
                setSinksContainer(crmSettings);
            }

            if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS) || Utils.hasRole(Constants.ADMIN)) {
                //Settings_hrms_settings
                SinksContainer hrmsSettings = new HrmsSettingsSinksContainer("hrmsSettings", wfmStrings.hrms(), null);
                hrmsSettings.setPreparedView("timeslot");
                setSinksContainer(hrmsSettings);
            }

            if (Utils.hasPermission(PermissionConstants.SETTINGS_PROJECT_MANAGEMENT_SETTINGS)) {
                SinksContainer pmSettings = new PMSettingsSinksContainer("pmSettings", wfmStrings.projects(), null);
                pmSettings.setPreparedView("numberingsettings");
                setSinksContainer(pmSettings);
            }
            if (Utils.hasPermission(PermissionConstants.PAYROLL_SETTINGS)) {
                SinksContainer settings = new PayrollSettingsSinksContainer("payrollSettings", wfmStrings.payroll());
                settings.setPreparedView("employersettings");
                setSinksContainer(settings);
            }
            if (Utils.hasPermission(PermissionConstants.SETTINGS_DASHBOARD_LIST)) {
                SinksContainer dashboardSettings = new ModuleDashboardSinksContainer("moduleDashboardSettings", wfmStrings.dashboards());
                dashboardSettings.setPreparedView("moduleDashboard");
                setSinksContainer(dashboardSettings);
            }

            if (Utils.hasPermission(PermissionConstants.SETTINGS_CUSTOMIZATION)) {
                SinksContainer customizationSettings = new CustomizationSettingsSinksContainer("customizationSettings", settingsStrings.customization());
                customizationSettings.setPreparedView("customFieldSettings");
                setSinksContainer(customizationSettings);
            }
            if (Utils.hasPermission(PermissionConstants.SETTINGS_RECURRENCE_SETTINGS)) {
                SinksContainer recurrenceSettings = new RecurrenceSettingsSinksContainer("recurrenceSettings", wfmStrings.recurrence(), null);
                recurrenceSettings.setPreparedView("timesheetReminder");
                setSinksContainer(recurrenceSettings);
            }
            if (Utils.hasPermission(PermissionConstants.SETTINGS_WORKFLOW)) {
                SinksContainer workflowSettings = new WorkflowSettingsSinksContainer("workflowSettings", settingsStrings.automationSettings(), null);
                workflowSettings.setPreparedView("opportunitySettings");
                setSinksContainer(workflowSettings);
            }
            if (Utils.hasPermission(PermissionConstants.SETTINGS_EMAIL_SETTINGS)) {
                SinksContainer emailSettings = new EmailSettingsSinksContainer("emailSettings", wfmStrings.templates(), null);
                emailSettings.setPreparedView("companyEmailSettings");
                setSinksContainer(emailSettings);
            }
            if (Utils.hasPermission(PermissionConstants.PERMISSION_MANAGEMENT)) {
                SinksContainer permissionSettings = new PermissionSinksContainer("permissionSettings", settingsStrings.usersAndPrivileges(), null);
                permissionSettings.setPreparedView("role");
                setSinksContainer(permissionSettings);
            }

            if (!Utils.hasEitherRole(ESS_USER_CODE) && Utils.hasGenericAccess(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP)) {
                SinksContainer consolidationSettings = new ConsalidationSinkContainer("consolidationSettings", settingsStrings.setupCompanySubsidiaries(), null);
                consolidationSettings.setPreparedView("consalidationList");
                setSinksContainer(consolidationSettings);
            }

            if (Utils.hasPermission(PermissionConstants.SETTINGS_INTEGRATION)) {
                SinksContainer integrationsSettings = new IntegrationSinksContainer("integrationsSettings", wfmStrings.integrations(), null);
                integrationsSettings.setPreparedView("integration");
                setSinksContainer(integrationsSettings);


                SinksContainer collaborationSettings = new CollaborationSettingsSinksContainer("collaborationSettings", settingsStrings.collaboration(), null);
                collaborationSettings.setPreparedView("collaboration");
                setSinksContainer(collaborationSettings);

                SinksContainer paymentGatewaysSettings = new PaymentGatewaysSettingsSinksContainer("paymentGatewaysSettings", settingsStrings.onlinePaymentDetails(), null);
                paymentGatewaysSettings.setPreparedView("paymentGateways");
                setSinksContainer(paymentGatewaysSettings);

                SinksContainer recruitmentIntegration = new RecruitmentIntegrationSinksContainer("recruitment", settingsStrings.recruitment(), null);
                recruitmentIntegration.setPreparedView("recruitment");
                setSinksContainer(recruitmentIntegration);

                SinksContainer documentIntegration = new DocumentIntegrationSinksContainer("document", wfmStrings.documents(), null);
                documentIntegration.setPreparedView("document");
                setSinksContainer(documentIntegration);

                SinksContainer attendanceTerminalIntegration = new AttendanceTerminalIntegrationSinksContainer("attendanceTerminal", wfmStrings.attendanceTerminal(), null);
                attendanceTerminalIntegration.setPreparedView("attendanceTerminal");
                setSinksContainer(attendanceTerminalIntegration);

                SinksContainer communicationSettings = new CommunicationSettingsSinksContainer("communicationSettings", "Communication", null);
                communicationSettings.setPreparedView("twilioAccounts");
                setSinksContainer(communicationSettings);

                if (Utils.hasGenericAccess(GenericSettingsEnum.MAGENTO_INTEGRATION_ENABLE)) {
                    SinksContainer eCommerceSettings = new ECommerceSettingsSinksContainer("eCommerceSettings", "E-Commerce", null);
                    eCommerceSettings.setPreparedView("magento");
                    setSinksContainer(eCommerceSettings);
                }
            }

            if (Utils.hasPermission(PermissionConstants.SYSTEM_LOGS)) {
                SinksContainer systemLogSettings = new SystemLogsSinksContainer("systemLogSettings", settingsStrings.systemLogs(), null);
                systemLogSettings.setPreparedView("importLogs");
                setSinksContainer(systemLogSettings);
            }
        } else {
            SinksContainer companySettings = new SignupCompanySettingsSinkContainer();
            companySettings.setPreparedView("newsignupcompanysettings");
            setSinksContainer(companySettings);
        }
    }

    @Override
    public void registerProcessors() {
        registerHistoryProcessor(TASK, new TaskHistoryProcessor());
        registerHistoryProcessor("taskedit", new TaskEditHistoryProcessor());
        registerHistoryProcessor("emailSettingsHome", new EmailSettingsHistoryProcessor());
        registerHistoryProcessor("autoResponseSettingsHome", new EmailSettingsHistoryProcessor());
        registerHistoryProcessor("profileSettingsHome", new ProfileSettingsHistoryProcessor());
        registerHistoryProcessor("profileSettingsEdit", new ProfileSettingsEditViewHistoryProcessor());
        registerHistoryProcessor("workflowSettingsHome", new WorkflowSettingsHistoryProcessor());
        registerHistoryProcessor("workflow", new WorkflowHistoryProcessor());
        registerHistoryProcessor("workflowalert", new WorkflowAlertHistoryProcessor());
        registerHistoryProcessor("workflowtelegramalert", new WorkflowTelegramAlertHistoryProcessor());
        registerHistoryProcessor("workflowevent", new WorkflowEventHistoryProcessor());
        registerHistoryProcessor("companySettingsHome", new CompanySettingsHistoryProcessor());
        registerHistoryProcessor("moduleSettingsHome", new ModuleSettingsHistoryProcessor());
        registerHistoryProcessor("invoiceSettingsHome", new InvoiceSettingsHistoryProcessor());
        registerHistoryProcessor("accountingGuide", new GuideHistoryProcessor());
        registerHistoryProcessor("userCredentialsHome", new UserCredentialsHistoryProcessor());
        registerHistoryProcessor("customFieldsHome", new CRMSettingsHistoryProcessor());
        registerHistoryProcessor("customFieldManagement", new CustomFieldManagementHistoryProcessor());
        registerHistoryProcessor("reference", new ReferenceHistoryProcessor());
        registerHistoryProcessor("referencelocale", new ReferenceLocaleHistoryProcessor());
        registerHistoryProcessor("leaveReason", new LeaveReasonHistoryProcessor());
        registerHistoryProcessor("country", new CounrtySettingsHistoryProcessor());
        registerHistoryProcessor("template", new EmailTemplateHistoryProcessor());
        registerHistoryProcessor("templateedit", new EmailTemplateEditHistoryProcessor());
        registerHistoryProcessor("smstemplate", new SMSTemplateHistoryProcessor());
        registerHistoryProcessor("smsSetting", new SMSSettingHistoryProcessor());
        registerHistoryProcessor("twilioSetting", new TwilioSettingHistoryProcessor());
        registerHistoryProcessor("asteriskSetting", new AsteriskSettingHistoryProcessor());
        registerHistoryProcessor("sipuniSetting", new SipuniSettingHistoryProcessor());
        registerHistoryProcessor("myCallsSetting", new MyCallsSettingHistoryProcessor());
        registerHistoryProcessor("telegramsetting", new TelegramBotSettingHistoryProcessor());
        registerHistoryProcessor("asteriskEmployeeSettings", new AsteriskEmployeeHistoryProcessor());
        registerHistoryProcessor("asteriskEmployeeList", new AsteriskEmployeeListHistoryProcessor());
        registerHistoryProcessor("signature", new SignatureHistoryProcessor());
        registerHistoryProcessor("signatureedit", new SignatureEditHistoryProcessor());
        registerHistoryProcessor("hrmsSettingsHome", new HrmsSettingsHistoryProcessor());
        registerHistoryProcessor("timeslot", new TimeslotHistoryProcessor());
        registerHistoryProcessor("shiftsettings", new ShiftSettingsHistoryProcessor());
        registerHistoryProcessor("holiday", new HolidayHistoryProcessor());
        registerHistoryProcessor("recurrenceSettingsHome", new RecurrenceSettingsHistoryProcessor());
        registerHistoryProcessor("projectManagementSettings", new PMSettingsHistoryProcessor());
        registerHistoryProcessor("createEMLTemplateFiles", new CreateEMLTemplatesHistoryProcessor());
        registerHistoryProcessor("saleinvoice", new SaleInvoiceHistoryProcessor());
        registerHistoryProcessor("salequote", new SaleQuoteHistoryProcessor());
        registerHistoryProcessor("saleorder", new SaleOrderHistoryProcessor());
        registerHistoryProcessor("opportunity", new OpportunityHistoryProcessor());
        registerHistoryProcessor("receivablecreditnote", new ReceivableCreditNoteHistoryProcessor());
        registerHistoryProcessor("purchaseinvoice", new PurchaseInvoiceHistoryProcessor());
        registerHistoryProcessor("purchaseorder", new PurchaseOrderHistoryProcessor());
        registerHistoryProcessor("payablecreditnote", new PayableCreditNoteHistoryProcessor());
        registerHistoryProcessor("client", new ClientHistoryProcessor());
        registerHistoryProcessor("clientedit", new ClientEditHistoryProcessor());
        registerHistoryProcessor("supplier", new SupplierHistoryProcessor());
        registerHistoryProcessor("issue", new IssueHistoryProcessor());
        registerHistoryProcessor("tax", new TaxHistoryProcessor());
        registerHistoryProcessor("priceLevel", new PriceLevelHistoryProcessor());
        registerHistoryProcessor("discount", new DiscountHistoryProcessor());
        registerHistoryProcessor("unitmeasurementlist", new UnitMeasurementsListHistoryProcessor());
        registerHistoryProcessor("productcategorylist", new ProductCategoriesListHistoryProcessor());
        registerHistoryProcessor("brandlist", new BrandsListHistoryProcessor());
        registerHistoryProcessor("emailfilter", new EmailFilterHistoryProcessor());
        registerHistoryProcessor("email", new EmailHistoryProcessor());
        registerHistoryProcessor("employeeLeaveTypes", new EmployeeLeaveTypesHistoryProcessor());
        registerHistoryProcessor("telephonySettingsList", new TelephonySettingsListHistoryProcesser());
        registerHistoryProcessor("permission", new PermissionHistoryProcessor());
        registerHistoryProcessor("brand", new AddBrandHistoryProcessor());
        registerHistoryProcessor("productcategory", new AddProductCategoryHistoryProcessor());
        registerHistoryProcessor("productcategoryview", new ProductCategorySummaryHistoryProcessor());
        registerHistoryProcessor("product", new AddProductHistoryProcessor());
        registerHistoryProcessor("stockadjustment", new StockAdjustmentHistoryProcessor());
        registerHistoryProcessor("stocktransfer", new StockTransferHistoryProcessor());
        registerHistoryProcessor("buildAssembly", new BuildAssemblyItemHistoryProcessor());
        registerHistoryProcessor("location", new LocationHistoryProcessor());
        registerHistoryProcessor("consolidation", new ConsolidationHistoryProcessor());
        registerHistoryProcessor("switchvox", new SwitchvoxHistoryProcessor());
        registerHistoryProcessor("customizeCertificate", new CustomizeCertificateHistoryProcessor());
        registerHistoryProcessor("benefit", new BenefitHistoryProcessor());
        registerHistoryProcessor("employeeBenefit", new EmployeeBenefitHistoryProcessor());
        registerHistoryProcessor(EMPLOYEE_STEP, new WorkflowEmployeeStepHistoryProcessor());
        registerHistoryProcessor("workflowEmployee", new WorkflowEmployeeHistoryProcessor());
        registerHistoryProcessor("workflowInvoice", new WorkflowInvoiceHistoryProcessor());
        registerHistoryProcessor("paymentmethodlist", new PaymentMethodListHistoryProcessor());
        registerHistoryProcessor("newsignupcompanysettings", new SignupCompanyHistoryProcessor());
        registerHistoryProcessor("employeeProfile", new EmployeeProfileHistoryProcessor());
        registerHistoryProcessor("editemployeeProfile", new EmployeeProfileEditHistoryProcessor());
        registerHistoryProcessor("employee", new EmployeeHistoryProcessor());
        registerHistoryProcessor("singleemployee", new SingleEmployeeHistoryProcessor());
        registerHistoryProcessor("hrmsemployee", new HrmsEmployeeHistoryProcessor());
        registerHistoryProcessor(DEPARTMENT, new TeamHistoryProcessor());
        registerHistoryProcessor("positionsummary", new PositionSummaryViewHistoryProcessor());
        registerHistoryProcessor("positions", new HrmsPositionHistoryProcessor());
        registerHistoryProcessor("position", new AddPositionHistoryProcessor());
        registerHistoryProcessor("multiposition", new MultiPositionHistoryProcessor());
        registerHistoryProcessor("multidepartment", new MultiDepartmentHistoryProcessor());
        registerHistoryProcessor("importposition", new ImportPositionHistoryProcessor());
        registerHistoryProcessor("importdepartment", new ImportDepartmentHistoryProcessor());
        registerHistoryProcessor("account", new AddAccountHistoryProcessor());
        registerHistoryProcessor("chartOfAccount", new ChartOfAccountsSummaryHistoryProcessor());
        registerHistoryProcessor("importchartofaccounts", new ImportChartOfAccountsHistoryProcessor());
        if (Utils.hasPermission(PermissionConstants.SYSTEM_LOGS)) {
            registerHistoryProcessor("systemLogs", new SystemLogsHistoryProcessor());
        }
//        registerHistoryProcessor("jobdescription", new AddJobDescriptionHistoryProcessor());
        registerHistoryProcessor("moduleDashboard", new ModuleDashboardHistoryProcessor());
//        Payroll
        registerHistoryProcessor("employersettings", new EmployerSettingsHistoryProcessor());
        registerHistoryProcessor("payrollGlobalSettings", new PayrollGlobalSettingsHistoryProcessor());
        registerHistoryProcessor("payrollBatch", new PayrollBatchHistoryProcessor());
        registerHistoryProcessor("payrollcategory", new PayrollCategoryV2HistoryProcessor());
        registerHistoryProcessor("pensionscheme", new PensionSchemeHistoryProcessor());
        registerHistoryProcessor("pensionprovider", new PensionProviderHistoryProcessor());
//        registerHistoryProcessor("payrollpaymentcategory", new PayrollPaymentCategoryHistoryProcessor());
//        registerHistoryProcessor("payrolldeductioncategory", new PayrollDeductionCategoryHistoryProcessor());
        registerHistoryProcessor("endOfService", new EndOfServiceHistoryProcessor());
        registerHistoryProcessor("appraisalssettings", new AppraisalsSettingsHistoryProcessor());
        registerHistoryProcessor("pdftemplate", new SettingsPdfTemplateHistoryProcessor());
        registerHistoryProcessor("customizationSettings", new CustomizationSettingsHistoryProcessor());
        registerHistoryProcessor("calendar", new GoogleCalendarHistoryProcessor());
//        registerHistoryProcessor("employercontribution", new EmployerContributionHistoryProcessor());
//        registerHistoryProcessor("importpaymentdeduction", new ImportPaymentDeductionHistoryProcessor());
        registerHistoryProcessor("clickedreport", new ClickedReportHistoryProcessor());
        registerHistoryProcessor("importclient", new ClientImportHistoryProcessor());
        registerHistoryProcessor("importsupplier", new SupplierImportHistoryProcessor());
        registerHistoryProcessor("importproductcategories", new ImportProductCategoriesHistoryProcessor());
        registerHistoryProcessor("importbrand", new ImportBrandHistoryProcessor());
        registerHistoryProcessor("importemployeeallowance", new ImportEmployeeLeaveAllowanceHistoryProcessor());
        registerHistoryProcessor("customFormLocalization", new CustomFormLocalizationHistoryProcessor());
        registerHistoryProcessor("webhook", new WorkflowWebHookHistoryProcessor());
        registerHistoryProcessor("webhookEdit", new WorkflowWebHookEditHistoryProcessor());
        registerHistoryProcessor("vacancy", new VacancyHistoryProcessor());
        registerHistoryProcessor("webhooklist", new WebhookListHistoryProcessor());
        registerHistoryProcessor("payrollZone", new PayrollZoneHistoryProcessor());
        registerHistoryProcessor("minimumWage", new MinimumWageHistoryProcessor());
        registerHistoryProcessor("wageRate", new WageRateHistoryProcessor());
        registerHistoryProcessor("attendanceTerminal", new AttendanceTerminalHistoryProcessor());
    }

    @Override
    public void registerMenuItems() {
        disableAddNew();
    }
}
