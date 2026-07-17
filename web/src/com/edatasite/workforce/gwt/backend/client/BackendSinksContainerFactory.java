package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.container.PartnerAdminBackendSinksContainer;
import com.edatasite.workforce.gwt.backend.client.container.SalesBackendSinksContainer;
import com.edatasite.workforce.gwt.backend.client.container.SupportBackendSinksContainer;
import com.edatasite.workforce.gwt.backend.client.history.AIPhantomPDFSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.ActivationLinkHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.BackendHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.BackendManagementHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.BackendViewHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.BlackListHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.BugListHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.BugListPerEmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.CompanyFileTransferViewHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.ContactPrivelegiesHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.CustomFormAddHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.CustomisedPDFSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.CustomisedPhantomPDFSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.DynamicLoginHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.ExportSchemaHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.FingerPrintDeviceStatusHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.FingerprintSetupHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.HelpDocumentAddHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.LocalizationHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.MoreMenuUpdateViewHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.RemovedHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.ReportTemplateCategoryHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.ReportingDBUrlHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.ReportingExcelTemplatesHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.ReportingTemplatesHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.SchemaHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.ServerUploadHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.SetTestCompanyHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.SolrCoreCompanyListHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.SubscriptionHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.SubscriptionManagementViewHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.TaxHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.UsagePlanUpdateViewHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.WFTFooterPdfHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.history.WhiteLabelHistoryProcessor;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.history.PublicWebhookHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.profile.client.history.CustomFieldManagementHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.ModuleSettingsHistoryProcessor;

public class BackendSinksContainerFactory extends SinksContainerFactory {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    BackendSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
        setDefaultContainer("backend");
    }

    @Override
    public void initDefaultContainers() {
        if (Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com") || Utils.enableSalesBackend()) {
            registerSalesBackendSinksContainer();
        }
        if (Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com") || Utils.enableSupportBackend()) {
            registerSupportBackendSinksContainer();
        }
        if (Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com") || Utils.enableDeveloperBackend()) {
            registerDeveloperBackendSinksContainer();
        }
        if (Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com") || Utils.enableAdminBackend()) {
            registerAdminBackendSinksContainer();
        }
        if (Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com") || Utils.enablePartnerAdminBackend()) {
            registerPartnerAdminBackendSinksContainer();
        }
    }

    @Override
    public void registerMenuItems() {
        if (Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com") || Utils.enableAdminBackend()) {
            addNewMenuItem(wfmStrings.isPaid(), "subscriptiontype|add/add");
            addNewMenuItem(backendStrings.removeTestEmails(), "remove|add/add");
            addNewMenuItem(wfmStrings.addAccount(), "account|add/add");
            addNewMenuItem(wfmStrings.addTaxRate(), "tax|add/add");
//            addNewMenuItem(backendStrings.applyDatabasePatch(), "schemas");
            addNewMenuItem(backendStrings.exportSchemas(), "exportschema|add/add");
            addNewMenuItem(backendStrings.serverUploadVersion(), "serverupload|add/add");
        } else {
            disableAddNew();
        }
    }

    public void registerProcessors() {
        registerHistoryProcessor("backend", new BackendHistoryProcessor());
        registerHistoryProcessor("activationLink", new ActivationLinkHistoryProcessor());
        registerHistoryProcessor("buglist", new BugListHistoryProcessor());
        registerHistoryProcessor("settestcompany", new SetTestCompanyHistoryProcessor());
        registerHistoryProcessor("bugListSummary", new BugListPerEmployeeHistoryProcessor());
        registerHistoryProcessor("blackList", new BlackListHistoryProcessor());
        registerHistoryProcessor("subscription", new SubscriptionHistoryProcessor());
        registerHistoryProcessor("contactPriv", new ContactPrivelegiesHistoryProcessor());
        registerHistoryProcessor("usagePlanUpd", new UsagePlanUpdateViewHistoryProcessor());
        registerHistoryProcessor("moduleSettingsHome", new ModuleSettingsHistoryProcessor());
        registerHistoryProcessor("wftFooterPdf", new WFTFooterPdfHistoryProcessor());
        registerHistoryProcessor("moreMenuUpdate", new MoreMenuUpdateViewHistoryProcessor());
        registerHistoryProcessor("schema", new SchemaHistoryProcessor());
        if (Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com") || Utils.enableAdminBackend()) {
            registerHistoryProcessor("exportschema", new ExportSchemaHistoryProcessor());
            registerHistoryProcessor("subscriptiontype", new SubscriptiontypeHistoryProcessor());
            registerHistoryProcessor("delete", new RemovedHistoryProcessor());
            registerHistoryProcessor("tax", new TaxHistoryProcessor());
            registerHistoryProcessor("serverupload", new ServerUploadHistoryProcessor());
        }
        registerHistoryProcessor("pdftemplate", new CustomisedPDFSettingsHistoryProcessor());
        registerHistoryProcessor("newpdftemplate", new CustomisedPhantomPDFSettingsHistoryProcessor());
        registerHistoryProcessor("pdftemplatewAi", new AIPhantomPDFSettingsHistoryProcessor());
        registerHistoryProcessor("customFieldManagement", new CustomFieldManagementHistoryProcessor());
        registerHistoryProcessor("reportingtemplate", new ReportingTemplatesHistoryProcessor());
        registerHistoryProcessor("reportxmltemplate", new ReportingExcelTemplatesHistoryProcessor());
        registerHistoryProcessor("reporttemplatecategory", new ReportTemplateCategoryHistoryProcessor());
        registerHistoryProcessor("backendView", new BackendViewHistoryProcessor());
        registerHistoryProcessor("customform", new CustomFormAddHistoryProcessor());
        registerHistoryProcessor("helpDocument", new HelpDocumentAddHistoryProcessor());
        registerHistoryProcessor("solrCoreComanyList", new SolrCoreCompanyListHistoryProcessor());
        registerHistoryProcessor("subscriptionManagementView", new SubscriptionManagementViewHistoryProcessor());
        registerHistoryProcessor("companyFileTransferView", new CompanyFileTransferViewHistoryProcessor());
        registerHistoryProcessor("backendManagement", new BackendManagementHistoryProcessor());
        registerHistoryProcessor("reportingdburl", new ReportingDBUrlHistoryProcessor());
        registerHistoryProcessor("localization", new LocalizationHistoryProcessor());
        registerHistoryProcessor("fingerprintSetup", new FingerprintSetupHistoryProcessor());
        registerHistoryProcessor("fingerPrintDeviceStatusHistory", new FingerPrintDeviceStatusHistoryProcessor());
        registerHistoryProcessor("dynamicLogin", new DynamicLoginHistoryProcessor());
        registerHistoryProcessor("whiteLabel", new WhiteLabelHistoryProcessor());
        registerHistoryProcessor("publicwebhook", new PublicWebhookHistoryProcessor());
    }

    private void registerSalesBackendSinksContainer() {
        //register sales backend sinks container
        SinksContainer salesBackend = new SalesBackendSinksContainer("salesBackend", backendStrings.salesBackend());
        salesBackend.setPreparedView("salesBackendView");
        setSinksContainer(salesBackend);
    }

    private void registerSupportBackendSinksContainer() {
        //register support backend sinks container
        SinksContainer supportBackend = new SupportBackendSinksContainer("supportBackend", backendStrings.supportBackend());
        supportBackend.setPreparedView("supportBackendView");
        setSinksContainer(supportBackend);
    }

    private void registerDeveloperBackendSinksContainer() {
        SinksContainer reportingManage = new DeveloperManagementSinksContainer("reportingtemplate", backendStrings.developerBackend());
        reportingManage.setPreparedView("reportinglist");
        setSinksContainer(reportingManage);
    }

    private void registerAdminBackendSinksContainer() {
        SinksContainer manageSchema = new AdminManagementSinksContainer("schemas", backendStrings.adminBackend());
        manageSchema.setPreparedView("schemalistview");
        setSinksContainer(manageSchema);
    }

    private void registerPartnerAdminBackendSinksContainer() {
        setSinksContainer(new PartnerAdminBackendSinksContainer("partnerAdminBackend", backendStrings.partnerAdminBackend()));
    }
}
