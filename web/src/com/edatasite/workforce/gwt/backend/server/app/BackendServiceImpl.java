package com.edatasite.workforce.gwt.backend.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.shared.mail.EdsMailer;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsBackendManagement;
import com.edatasite.workforce.core.domain.EdsBlackList;
import com.edatasite.workforce.core.domain.EdsBugAttachment;
import com.edatasite.workforce.core.domain.EdsBugComment;
import com.edatasite.workforce.core.domain.EdsBugReport;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyStatistic;
import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCustomLayout;
import com.edatasite.workforce.core.domain.EdsDefaultLayout;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsFingerPrintDeviceStatusHistory;
import com.edatasite.workforce.core.domain.EdsHelpDocument;
import com.edatasite.workforce.core.domain.EdsImportFile;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsLayout;
import com.edatasite.workforce.core.domain.EdsLocalization;
import com.edatasite.workforce.core.domain.EdsLocalizationPermissions;
import com.edatasite.workforce.core.domain.EdsMoreMenuSettings;
import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsRecurrenceHistory;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsServerHistory;
import com.edatasite.workforce.core.domain.EdsServerUploadHistory;
import com.edatasite.workforce.core.domain.EdsSubscriptionHistory;
import com.edatasite.workforce.core.domain.EdsSubscriptionPayment;
import com.edatasite.workforce.core.domain.EdsSubscriptionType;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUploadSettings;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserSessionTracker;
import com.edatasite.workforce.core.domain.EdsWFTPlagin;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsAccountTemplate;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsRFQ;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.core.domain.accounting.EdsTaxTemplate;
import com.edatasite.workforce.core.domain.analyzer.EdsSolrDbConsistency;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTable;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.pdf.EdsPdfReference;
import com.edatasite.workforce.core.domain.pdf.EdsPdfTemplate;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.core.domain.reporting.EdsChartConfig;
import com.edatasite.workforce.core.domain.reporting.EdsKpiWidget;
import com.edatasite.workforce.core.domain.reporting.EdsReportTemplate;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.domain.settings.EdsGenericSettings;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseBooking;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.webforms.EdsWebForm;
import com.edatasite.workforce.core.solr.component.AdditionalPaymentSolrComponent;
import com.edatasite.workforce.core.solr.component.CaseSolrComponent;
import com.edatasite.workforce.core.solr.component.CashAdvanceSolrComponent;
import com.edatasite.workforce.core.solr.component.CertificateSolrComponent;
import com.edatasite.workforce.core.solr.component.ChartOfAccountSolrComponent;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.solr.component.CourseBookingSolrComponent;
import com.edatasite.workforce.core.solr.component.CourseScheduleSolrComponent;
import com.edatasite.workforce.core.solr.component.CrmAccountSolrComponent;
import com.edatasite.workforce.core.solr.component.CustomFormItemSolrComponent;
import com.edatasite.workforce.core.solr.component.DepartmentSolrComponent;
import com.edatasite.workforce.core.solr.component.EmployeeSolrComponent;
import com.edatasite.workforce.core.solr.component.EmployeeStepSolrComponent;
import com.edatasite.workforce.core.solr.component.EventSolrComponent;
import com.edatasite.workforce.core.solr.component.ExpenseReportClaimsSolrComponent;
import com.edatasite.workforce.core.solr.component.FolderSolrComponent;
import com.edatasite.workforce.core.solr.component.GroupPayrunSolrComponent;
import com.edatasite.workforce.core.solr.component.LeaveRequestSolrComponent;
import com.edatasite.workforce.core.solr.component.NewsSolrComponent;
import com.edatasite.workforce.core.solr.component.OpportunitySolrComponent;
import com.edatasite.workforce.core.solr.component.PositionSolrComponent;
import com.edatasite.workforce.core.solr.component.ProductsServicesSolrComponent;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.core.solr.component.PurchaseInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.PurchaseOrderSolrComponent;
import com.edatasite.workforce.core.solr.component.RequestForQuoteSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleQuoteSolrComponent;
import com.edatasite.workforce.core.solr.component.ShippingDataSolrComponent;
import com.edatasite.workforce.core.solr.component.SinglePayrunSolrComponent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.core.solr.component.VacancySolrComponent;
import com.edatasite.workforce.core.tools.BackendRunSchemaUpdate;
import com.edatasite.workforce.core.tools.CompanyidDomain;
import com.edatasite.workforce.core.tools.EdsSchemaUpdater;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountTypesByCategory;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AddAccountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxList;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.backend.client.constants.BackendConstants;
import com.edatasite.workforce.gwt.backend.client.exceptions.CustomException;
import com.edatasite.workforce.gwt.backend.client.rpc.AccessLogList;
import com.edatasite.workforce.gwt.backend.client.rpc.AccessLogListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.AccountManagementListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.AccountManagerItemList;
import com.edatasite.workforce.gwt.backend.client.rpc.ActivationLinkList;
import com.edatasite.workforce.gwt.backend.client.rpc.ActivationLinkListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendManagementListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BugComment;
import com.edatasite.workforce.gwt.backend.client.rpc.BugList;
import com.edatasite.workforce.gwt.backend.client.rpc.BugListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.BugsPerEmployeesListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyItem;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyList;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.ContactPrivelegiesItem;
import com.edatasite.workforce.gwt.backend.client.rpc.CountryList;
import com.edatasite.workforce.gwt.backend.client.rpc.CountryListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.EditSubscription;
import com.edatasite.workforce.gwt.backend.client.rpc.FingerPrintDeviceStatusHistoryListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.GenericSettingsRPC;
import com.edatasite.workforce.gwt.backend.client.rpc.IndustryList;
import com.edatasite.workforce.gwt.backend.client.rpc.IndustryListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.MoreMenuUpdateItem;
import com.edatasite.workforce.gwt.backend.client.rpc.PDFSettingsTransObject;
import com.edatasite.workforce.gwt.backend.client.rpc.PDFTemplatesListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.PaypalReceiptsListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.RecurrenceLogItem;
import com.edatasite.workforce.gwt.backend.client.rpc.RecurrenceLogList;
import com.edatasite.workforce.gwt.backend.client.rpc.ReportsListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.SchemaList;
import com.edatasite.workforce.gwt.backend.client.rpc.SchemaListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.SignupsRate;
import com.edatasite.workforce.gwt.backend.client.rpc.SimpleUsagePlanItem;
import com.edatasite.workforce.gwt.backend.client.rpc.SolrDbInconsistencyItem;
import com.edatasite.workforce.gwt.backend.client.rpc.SolrDbInconsistencyList;
import com.edatasite.workforce.gwt.backend.client.rpc.SolrInconsistencyList;
import com.edatasite.workforce.gwt.backend.client.rpc.SolrMonitorRpc;
import com.edatasite.workforce.gwt.backend.client.rpc.Statistics;
import com.edatasite.workforce.gwt.backend.client.rpc.SubscriptionList;
import com.edatasite.workforce.gwt.backend.client.rpc.SubscriptionListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.SubscriptionManagementItem;
import com.edatasite.workforce.gwt.backend.client.rpc.TestCompanyItem;
import com.edatasite.workforce.gwt.backend.client.rpc.UserSessionHistoryItem;
import com.edatasite.workforce.gwt.backend.client.rpc.WFTPlaginList;
import com.edatasite.workforce.gwt.backend.client.rpc.WFTPlaginListItem;
import com.edatasite.workforce.gwt.backend.server.actions.CreateSubscriptionCommand;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.ApiAccessToken;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.DynamicLogin;
import com.edatasite.workforce.gwt.core.client.rpc.EncryptionUtils;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReportingListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormValidation;
import com.edatasite.workforce.gwt.core.client.rpc.form.HelpDocumentItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.LocalizationItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LocalizationPermissionItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrAdditionalPaymentPresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCaseRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCashAdvanceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCertificateRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrChartOfAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCourseBookingRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCourseScheduleRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCrmAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCustomFormConst;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrDepartmentRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeStepRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEventRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrExpenseReportRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrGroupPayrunRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrLeaveRequestConst;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrNewsRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrOpportunityRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPositionRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProductServiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProjectListRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPurchaseInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSinglePayrunRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrVacancyRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.FileTransferService;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.TransactionHelper;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaOperations;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.commons.CreateTemplateSchema;
import com.edatasite.workforce.gwt.core.server.controllers.EmailAddressValidator;
import com.edatasite.workforce.gwt.core.server.db.AccountTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.AttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.BackendManagementManager;
import com.edatasite.workforce.gwt.core.server.db.BlackListManager;
import com.edatasite.workforce.gwt.core.server.db.BugCommentManager;
import com.edatasite.workforce.gwt.core.server.db.BugReportManager;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyPdfTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyStatisticManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.FingerPrintDeviceStatusHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.HelpDocumentManager;
import com.edatasite.workforce.gwt.core.server.db.HostBasedSettingManager;
import com.edatasite.workforce.gwt.core.server.db.ImportFileManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.LayoutManager;
import com.edatasite.workforce.gwt.core.server.db.LocalizationManager;
import com.edatasite.workforce.gwt.core.server.db.LocalizationPermissionManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.MoreMenuSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.NewsManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.PdfReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.PdfTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.ServerHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.ServerUploadHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.ShippingDataManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.SinxDocumentsSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.StepEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.SubscriptionHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.SubscriptionManager;
import com.edatasite.workforce.gwt.core.server.db.SubscriptionPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TaxTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.UserSessionManager;
import com.edatasite.workforce.gwt.core.server.db.UserSessionTrackerManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.db.VatManager;
import com.edatasite.workforce.gwt.core.server.db.WFTPlaginManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PickListManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQManager;
import com.edatasite.workforce.gwt.core.server.db.analyzer.SolrDbConsistencyManager;
import com.edatasite.workforce.gwt.core.server.db.certificate.CertificateOfEmploymentManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.impl.ListingObjectItem;
import com.edatasite.workforce.gwt.core.server.db.payroll.AdditionalPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CashAdvanceManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableItemManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TrusteeManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.documents.FolderRbacManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseBookingManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.ScheduledCourseManager;
import com.edatasite.workforce.gwt.core.server.db.webforms.WebFormManager;
import com.edatasite.workforce.gwt.core.server.db.wfp.ReportTemplateManager;
import com.edatasite.workforce.gwt.core.server.enums.TemplateSchema;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.ImportCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.rpc.RpcMap;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextFontTypeEnum;
import com.edatasite.workforce.gwt.core.server.utils.AbstractComparator;
import com.edatasite.workforce.gwt.core.server.utils.ComparatorFactory;
import com.edatasite.workforce.gwt.core.server.utils.LocalizationUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.solr.SolrFolderRepresenter;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarService;
import com.edatasite.workforce.gwt.hrms.server.app.RecruitmentServiceLocal;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.QuoteServiceLocal;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.myaccount.server.app.MyAccountServiceLocal;
import com.edatasite.workforce.gwt.profile.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.gwt.project.server.actions.ProjectServiceLocal;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingTestDTO;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.signup.server.app.SignUpServiceLocal;
import com.edatasite.workforce.utils.EdsContextParams;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.ReportGenerateTableRpc;
import com.finnetlimited.reportservice.core.server.CoreServiceLocal;
import com.finnetlimited.reportservice.core.server.db.schema.ReportingManager;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.GroupParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Transactional
@Service("backendService")
public class BackendServiceImpl implements BackendService, BackendConstants, BackendServiceLocal, Constants {

    private static final Logger log = LoggerFactory.getLogger(BackendServiceImpl.class);
    private static final Map<String, ComparatorFactory<Object[]>> comparatorFactories = new HashMap<>();
    private static final Map<String, ComparatorFactory<EdsBugReport>> comparatorFactoriesBugList = new HashMap<>();
    private static final Map<String, ComparatorFactory<EdsUsagePlan>> comparatorFactoriesPaypalReceiptsList = new HashMap<>();
    private static Boolean UpdateInProgress = false;

    @Autowired
    private RelationManager relationManager;

    static {

        comparatorFactories.put(CompanyListItem.COMPANY_NAME/*"Company Name"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getCompanyName().toUpperCase(),
                        ((EdsCompanyStatistic) o2[0]).getCompanyName().toUpperCase(), sortOrder);
            }
        });
        comparatorFactories.put(CompanyListItem.REGISTRATION_DATE/*"Registration Date"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getRegistrationDate(),
                        ((EdsCompanyStatistic) o2[0]).getRegistrationDate(), sortOrder);
            }
        });
        comparatorFactories.put(CompanyListItem.ACCESS_COUNT/*"Access Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getAccessCount(),
                        ((EdsCompanyStatistic) o2[0]).getAccessCount(), sortOrder);
            }
        });
        comparatorFactories.put(CompanyListItem.FIRST_ACCESS_DATE/*"First Access Date"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getFirstAccessDate(),
                        ((EdsCompanyStatistic) o2[0]).getFirstAccessDate(), sortOrder);
            }
        });
        comparatorFactories.put(CompanyListItem.LAST_ACCESS_DATE/*"Last Access Date"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getLastAccessDate(),
                        ((EdsCompanyStatistic) o2[0]).getLastAccessDate(), sortOrder);
            }
        });
        comparatorFactories.put(CompanyListItem.PERIOD_ACCESS/*"Period Access"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getPeriodAccess(),
                        ((EdsCompanyStatistic) o2[0]).getPeriodAccess(), sortOrder);
            }
        });

        comparatorFactories.put(CompanyListItem.USER_COUNT/*"User Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getUserCount(),
                        ((EdsCompanyStatistic) o2[0]).getUserCount(), sortOrder);
            }
        });


        comparatorFactories.put(CompanyListItem.ACTIVE_USERS_COUNT/*"Active Users Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getActiveUsersCount(),
                        ((EdsCompanyStatistic) o2[0]).getActiveUsersCount(), sortOrder);
            }
        });
        comparatorFactories.put(CompanyListItem.PROJECT_COUNT/*"Project Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getProjectCount(),
                        ((EdsCompanyStatistic) o2[0]).getProjectCount(), sortOrder);
            }
        });
        comparatorFactories.put(CompanyListItem.TASK_COUNT/*"Task Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getTaskCount(),
                        ((EdsCompanyStatistic) o2[0]).getTaskCount(), sortOrder);
            }
        });
        comparatorFactories.put(CompanyListItem.TIMESHEET_COUNT/*"TimeSheet Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getTimesheetCount(),
                        ((EdsCompanyStatistic) o2[0]).getTimesheetCount(), sortOrder);
            }
        });

        comparatorFactories.put(CompanyListItem.CLIENT_COUNT/*"Client Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getClientCount(),
                        ((EdsCompanyStatistic) o2[0]).getClientCount(), sortOrder);
            }
        });
        comparatorFactories.put(CompanyListItem.SUPPLIER_COUNT/*"Supplier Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getSupplierCount(),
                        ((EdsCompanyStatistic) o2[0]).getSupplierCount(), sortOrder);
            }
        });
        comparatorFactories.put(CompanyListItem.LEAD_COUNT/*"Lead Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getLeadCount(),
                        ((EdsCompanyStatistic) o2[0]).getLeadCount(), sortOrder);
            }
        });

        comparatorFactories.put(CompanyListItem.CONTACT_COUNT/*"Contact Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getContactCount(),
                        ((EdsCompanyStatistic) o2[0]).getContactCount(), sortOrder);
            }
        });

        comparatorFactories.put(CompanyListItem.CRM_TASK_COUNT/*"Crm Task Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getCrmtaskCount(),
                        ((EdsCompanyStatistic) o2[0]).getCrmtaskCount(), sortOrder);
            }
        });

        comparatorFactories.put(CompanyListItem.EVENT_COUNT/*"Event Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getEventCount(),
                        ((EdsCompanyStatistic) o2[0]).getEventCount(), sortOrder);
            }
        });

        comparatorFactories.put(CompanyListItem.CASE_COUNT/*"Case Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getCaseCount(),
                        ((EdsCompanyStatistic) o2[0]).getCaseCount(), sortOrder);
            }
        });

        comparatorFactories.put(CompanyListItem.INVOICE_COUNT/*"Invoice Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getInvoiceCount(),
                        ((EdsCompanyStatistic) o2[0]).getInvoiceCount(), sortOrder);
            }
        });

        comparatorFactories.put(CompanyListItem.EXPENSE_COUNT/*"Expense Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getExpenseCount(),
                        ((EdsCompanyStatistic) o2[0]).getExpenseCount(), sortOrder);
            }
        });

        comparatorFactories.put(CompanyListItem.PRODUCT_COUNT/*"Product Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getProductCount(),
                        ((EdsCompanyStatistic) o2[0]).getProductCount(), sortOrder);
            }
        });

        comparatorFactories.put(CompanyListItem.FOLDER_COUNT/*"Folder Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getFolderCount(),
                        ((EdsCompanyStatistic) o2[0]).getFolderCount(), sortOrder);
            }
        });

        comparatorFactories.put(CompanyListItem.FILE_COUNT/*"File Count"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getFileCount(),
                        ((EdsCompanyStatistic) o2[0]).getFileCount(), sortOrder);
            }
        });

        comparatorFactories.put(CompanyListItem.SIGNED_UP_FROM/*"Signed Up From"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[0]).getCompanySignedUpFrom(),
                        ((EdsCompanyStatistic) o2[0]).getCompanySignedUpFrom(), sortOrder);

            }
        });
        comparatorFactories.put(CompanyListItem.HOST_NAME, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsCompanyStatistic) o1[1]).getHost(),
                        ((EdsCompanyStatistic) o2[1]).getHost(), sortOrder);
            }
        });

        comparatorFactories.put(CompanyListItem.SUBSCRIPTION_TYPE/*"Subscription Type"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsUsagePlan) o1[1]).getPeriodType().getName(),
                        ((EdsUsagePlan) o2[1]).getPeriodType().getName(), sortOrder);
            }
        });
        comparatorFactories.put(CompanyListItem.PAYMENT_STATUS/*"Payment Status"*/, sortOrder -> new AbstractComparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return internalCompare(((EdsUsagePlan) o1[1]).getStatus().getName(),
                        ((EdsUsagePlan) o2[1]).getStatus().getName(), sortOrder);
            }
        });

    }

    static {
        comparatorFactoriesBugList.put(BugListItem.CREATION_TIME, sortOrder -> new AbstractComparator<EdsBugReport>() {
            public int compare(EdsBugReport o1, EdsBugReport o2) {
                return internalCompare(o1.getCreationTime(), o2.getCreationTime(), sortOrder);
            }
        });
        comparatorFactoriesBugList.put(BugListItem.UPDATE_TIME, sortOrder -> new AbstractComparator<EdsBugReport>() {
            public int compare(EdsBugReport o1, EdsBugReport o2) {
                return internalCompare(o1.getUpdateTime(), o2.getUpdateTime(), sortOrder);
            }
        });

        comparatorFactoriesPaypalReceiptsList.put(PaypalReceiptsListItem.COMPANY_NAME, sortOrder -> new AbstractComparator<EdsUsagePlan>() {
            public int compare(EdsUsagePlan o1, EdsUsagePlan o2) {
                return internalCompare(o1.getCompany().getName(), o2.getCompany().getName(), sortOrder);
            }
        });

        comparatorFactoriesPaypalReceiptsList.put(PaypalReceiptsListItem.SUBSCSTARTDATE, sortOrder -> new AbstractComparator<EdsUsagePlan>() {
            public int compare(EdsUsagePlan o1, EdsUsagePlan o2) {
                return internalCompare(o1.getStartDate(), o2.getStartDate(), sortOrder);
            }
        });

        comparatorFactoriesPaypalReceiptsList.put(PaypalReceiptsListItem.SUBSCENDDATE, sortOrder -> new AbstractComparator<EdsUsagePlan>() {
            public int compare(EdsUsagePlan o1, EdsUsagePlan o2) {
                return internalCompare(o1.getEndDate(), o2.getEndDate(), sortOrder);
            }
        });

        comparatorFactoriesPaypalReceiptsList.put(PaypalReceiptsListItem.NUMBEROFEMPLOYEES, sortOrder -> new AbstractComparator<EdsUsagePlan>() {
            public int compare(EdsUsagePlan o1, EdsUsagePlan o2) {
                return internalCompare(o1.getUsers(), o2.getUsers(), sortOrder);
            }
        });

        comparatorFactoriesPaypalReceiptsList.put(PaypalReceiptsListItem.PAIDAMOUNT, sortOrder -> new AbstractComparator<EdsUsagePlan>() {
            public int compare(EdsUsagePlan o1, EdsUsagePlan o2) {
                return internalCompare(o1.getTotalAmount(), o2.getTotalAmount(), sortOrder);
            }
        });

        comparatorFactoriesPaypalReceiptsList.put(PaypalReceiptsListItem.STATUS, sortOrder -> new AbstractComparator<EdsUsagePlan>() {
            public int compare(EdsUsagePlan o1, EdsUsagePlan o2) {
                return internalCompare(o1.getStatus().getName(), o2.getStatus().getName(), sortOrder);
            }
        });
    }

    private final Date tdate = new Date();
    @Autowired
    protected UploadManager uploadManager;
    @Autowired
    @Qualifier("googleCalendarService")
    GoogleCalendarService googleCalendarService;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private QuoteServiceLocal quoteServiceLocal;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private PayslipTableItemManager payslipTableItemManager;
    @Autowired
    private PayslipTableManager payslipTableManager;
    @Autowired
    private CashAdvanceManager cashAdvanceManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private NewsManager newsManager;
    @Autowired
    private UsagePlanManager usagePlanManager;
    @Autowired
    private SubscriptionPaymentManager subscriptionPaymentManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private CompanyStatisticManager companyStatisticManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private SubscriptionManager subscriptionManager;
    @Autowired
    private BugReportManager bugReportManager;
    @Autowired
    private BugCommentManager bugCommentManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private VatManager vatManager;
    @Autowired
    private TaxTemplateManager taxTemplateManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private AccountTemplateManager accountTemplateManager;
    @Autowired
    private UserSessionManager userSessionManager;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserSessionTrackerManager userSessionTrackerManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private ReportingManager reportingManager;
    @Autowired
    private ReportTemplateManager reportTemplateManager;
    @Autowired
    private SinxDocumentsSettingsManager sinxDocumentsSettingsManager;
    @Autowired
    private BlackListManager blackListManager;
    @Autowired
    private WFTPlaginManager wftPlaginManager;
    @Autowired
    @Qualifier("myAccountService")
    private MyAccountServiceLocal myAccountServiceLocal;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    @Qualifier("signUpService")
    private SignUpServiceLocal signUpServiceLocal;
    @Autowired
    private PdfReferenceManager pdfReferenceManager;
    @Autowired
    private CompanyPdfTemplateManager companyPdfTemplateManager;
    @Autowired
    private PdfTemplateManager pdfTemplateManager;
    @Autowired
    private CompanyAttachmentManager companyAttachmentManager;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private RecurrenceHistoryManager recurrenceHistoryManager;
    @Autowired
    private ServerHistoryManager serverHistoryManager;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private MoreMenuSettingsManager moreMenuSettingsManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private SubscriptionHistoryManager subscriptionHistoryManager;
    @Autowired
    private TaskRbacManager taskRbacManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private InvoicingSettingsManager invoicingSettingsManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private FolderRbacManager folderRbacManager;
    @Autowired
    private SolrDbConsistencyManager solrDbConsistencyManager;
    @Autowired
    private WfmJpaOperations jpaTemplate;
    @Autowired
    private FileHeaderManager fileHeaderManager;
    @Autowired
    private PickListManager pickListManager;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    @Qualifier("projectService")
    private ProjectServiceLocal projectServiceLocal;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    @Qualifier("reportingCoreService")
    private CoreService coreService;
    @Autowired
    @Qualifier("reportingCoreService")
    private CoreServiceLocal reportingServiceLocal;
    @Autowired
    @Qualifier("reportingService")
    private ReportingService reportingService;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private ShippingDataManager shippingDataManager;
    @Autowired
    @Qualifier("documentsService")
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    private TrusteeManager trusteeManager;
    @Autowired
    private GroupManager groupManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private BackendManagementManager backendManagementManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private CourseBookingManager courseBookingManager;
    @Autowired
    private ScheduledCourseManager scheduledCourseManger;
    @Autowired
    private LayoutManager layoutManager;
    @Autowired
    private HelpDocumentManager documentManager;
    @Autowired
    private LocalizationManager localizationManager;
    @Autowired
    private LocalizationPermissionManager localizationPermissionManager;
    @Autowired
    private VacancyManager vacancyManager;
    @Autowired
    private StepEmployeeManager stepEmployeeManager;
    @Autowired
    private RecruitmentServiceLocal recruitmentServiceLocal;
    @Autowired
    private ServerUploadHistoryManager serverUploadHistoryManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private AdditionalPaymentManager additionalPaymentManager;
    @Autowired
    private AvailabilityServiceLocal availabilityService;
    @Autowired
    private BaseEventsPostProcessor baseEventsPostProcessor;
    @Autowired
    private ImportFileManager importFileManager;
    @Autowired
    private FingerPrintDeviceStatusHistoryManager fingerPrintDeviceStatusHistoryManager;
    @Autowired
    private WebFormManager webFormManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private CustomFormItemManager customFormItemManager;
    @Autowired
    private CreateTemplateSchema templateSchema;
    @Autowired
    private PropertManager propertManager;
    @Autowired
    private RFQManager rfqManager;
    @Autowired
    private CertificateOfEmploymentManager certificatemanager;
    @Autowired
    private HostBasedSettingManager hostBasedSettingManager;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private ProfileServiceLocal profileService;
    @Autowired
    private LeaveRequestSolrComponent leaveRequestSolrComponent;
    @Autowired
    private EmployeeSolrComponent employeeSolrComponent;
    @Autowired
    private SinglePayrunSolrComponent singlePayrunSolrComponent;
    @Autowired
    private CourseBookingSolrComponent courseBookingSolrComponent;
    @Autowired
    private CashAdvanceSolrComponent cashAdvanceSolrComponent;
    @Autowired
    private GroupPayrunSolrComponent groupPayrunSolrComponent;
    @Autowired
    private AdditionalPaymentSolrComponent additionalPaymentSolrComponent;
    @Autowired
    private ChartOfAccountSolrComponent chartOfAccountSolrComponent;
    @Autowired
    private ShippingDataSolrComponent shippingDataSolrComponent;
    @Autowired
    private SaleQuoteSolrComponent saleQuoteSolrComponent;
    @Autowired
    private SaleInvoiceSolrComponent saleInvoiceSolrComponent;
    @Autowired
    private PurchaseInvoiceSolrComponent purchaseInvoiceSolrComponent;
    @Autowired
    private ProductsServicesSolrComponent productsServicesSolrComponent;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;
    @Autowired
    private FolderSolrComponent folderSolrComponent;
    @Autowired
    private PurchaseOrderSolrComponent purchaseOrderSolrComponent;
    @Autowired
    private ExpenseReportClaimsSolrComponent expenseReportClaimsSolrComponent;
    @Autowired
    private CaseSolrComponent caseSolrComponent;
    @Autowired
    private TaskSolrComponent taskSolrComponent;
    @Autowired
    private ContactSolrComponent contactSolrComponent;
    @Autowired
    private CrmAccountSolrComponent crmAccountSolrComponent;
    @Autowired
    private NewsSolrComponent newsSolrComponent;
    @Autowired
    private OpportunitySolrComponent opportunitySolrComponent;
    @Autowired
    private EventSolrComponent eventSolrComponent;
    @Autowired
    private CustomFormItemSolrComponent customFormItemSolrComponent;
    @Autowired
    private VacancySolrComponent vacancySolrComponent;
    @Autowired
    private EmployeeStepSolrComponent employeeStepSolrComponent;
    @Autowired
    private CertificateSolrComponent certificateSolrComponent;
    @Autowired
    private RequestForQuoteSolrComponent rfqSolrComponent;
    @Autowired
    private PositionSolrComponent positionSolrComponent;
    @Autowired
    private DepartmentSolrComponent departmentSolrComponent;
    @Autowired
    private CourseScheduleSolrComponent courseScheduleSolrComponent;
    @Autowired
    private FileTransferService fileTransferService;
    @Autowired
    private TransactionHelper transactionHelper;
    @Autowired
    private ExecutorService executor;
    @PersistenceContext
    private EntityManager entityManager;
    private Long totalCount;
    private Timestamp sdate;
    private Timestamp edate;
    private WorkspaceImportExporter workspaceImportExporter;
    private Format formatter = new SimpleDateFormat("MM/dd/yy");

    private static synchronized Boolean getUpdateInProgress() {
        return UpdateInProgress;
    }

    private static synchronized void setUpdateInProgress(Boolean updateInProgress) {
        UpdateInProgress = updateInProgress;
    }

    private Long getEmployeesCountByDateLimit() {
        totalCount = jdbcSpringManager.getEmployeesCountByDateLimit(sdate, edate);
        return totalCount;
    }

    private Long getSystemUsedCountByDateLimit() {
        totalCount = companyStatisticManager.getSystemUsedUsersCountByDateLimit(sdate, edate);
        return totalCount;
    }

    private Long getSystemBouncedCountByDateLimit() {
        totalCount = companyStatisticManager.getBouncedUsersCountByDateLimit(sdate, edate);
        return totalCount;
    }

    /**
     * Saves the latest upload details in order to inform signed up
     * users  about expiring  the last view that has been opened in
     * their own browsers.Because there maybe conflicts between two
     * versions  of  uploaded files. Creating  a RPC class from the
     * scratch was extremely  redundant  there, thus we simply used
     * existing but not proper for current usage class that name
     * was CompanyItem.
     */
    public void saveLastUploadDetails(CompanyItem uploadDetails) {
        EdsServerUploadHistory history = new EdsServerUploadHistory();
        history.setVersion(uploadDetails.getName());
        history.setMessage(uploadDetails.getCompanyName());
        history.setDescription(uploadDetails.getDescription());
        history.setDate(new Date());
        serverUploadHistoryManager.create(history);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CompanyList getCompanies(Boolean isCount, ListingFilterParameter fp) {

        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        EdsUser user = userManager.getUser();
        EdsBackendManagement backendManagement = backendManagementManager.getBackendManagement(user.getCompany().getObjectID(), user.getObjectID());
        if (backendManagement != null) {
            fp.setParams(backendManagement.getHostNames());
            fp.setAccountCode(backendManagement.getPromotionalCode());
        }

        List<Object[]> companyStatistics = companyStatisticManager.getCompanyStatistics(fp, isCount, fp.getViewAsId(), fp.getBackendUsersId());

        HashMap<Integer, Integer> existingCompanies = new HashMap<>();

        for (Iterator<Object[]> i = companyStatistics.iterator(); i.hasNext(); ) {
            Object[] iObj = (Object[]) i.next();
            EdsCompanyStatistic cStatic = (EdsCompanyStatistic) iObj[0];

            if (existingCompanies.get(cStatic.getCompanyID()) != null) {
                i.remove();
            } else {
                existingCompanies.put(cStatic.getCompanyID(), cStatic.getCompanyID());
            }
        }

        int totalCount = companyStatistics.size();
        ComparatorFactory factory = null;
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            factory = comparatorFactories.get(fp.getSortField());
        }
        int sortDir = fp.getSortDir();
        if (factory == null && isCount) {
            factory = comparatorFactories.get(CompanyListItem.LAST_ACCESS_DATE);
            sortDir = Constants.DESC;
        } else if (factory == null) {
            factory = comparatorFactories.get(CompanyListItem.REGISTRATION_DATE);
            sortDir = Constants.DESC;
        }
        companyStatistics.sort(factory.createComparator(sortDir));

        if (fp.getLimit() > 0) {
            companyStatistics = ListUtils.getSublist(companyStatistics, fp.getStart(), fp.getLimit());
        }
        ArrayList<CompanyListItem> result = new ArrayList<>();

        int i = -1;
        Date statisticLasUpdatedTime = null;// = new Date();

        for (Object[] objects : companyStatistics) {

            EdsCompanyStatistic cStatic = (EdsCompanyStatistic) objects[0];
            EdsUsagePlan usagePlan = (EdsUsagePlan) objects[1];

            if (statisticLasUpdatedTime == null && cStatic.getStatisticUpdatedTime() != null) {
                statisticLasUpdatedTime = new Date(cStatic.getStatisticUpdatedTime().getTime());
            }
            String shadowLoginlink = "shadowLogin?id=";
            shadowLoginlink = shadowLoginlink + EncryptionHelper.encryptURL(cStatic.getCompanyID().toString());
            try {
                CompanyListItem item = new CompanyListItem();
                item = cStatic.getRPC_CompanyListItem();
                item.setUsagePlanPaymentStatus(usagePlan.getStatus() != null ? usagePlan.getStatus().getName() : "");
                item.setUsagePlanPaymentType(usagePlan.getPeriodType() != null ? usagePlan.getPeriodType().getName() : "");
                item.setPeriodStartDate(formatter.format(usagePlan.getStartDate()));
                item.setPeriodEndDate(formatter.format(usagePlan.getEndDate()));
                item.setUsagPlanEndDate(usagePlan.getEndDate());
                result.add(item);
            } catch (RuntimeException e) {

                e.printStackTrace();
            }

        }
        CompanyList cp = new CompanyList(result, totalCount);
        cp.setLastpUpdateTime(Objects.requireNonNullElseGet(statisticLasUpdatedTime, Date::new));
        return cp;

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Statistics getOverallStatistics() {
        Statistics stat = new Statistics();
        Object[] oStatistic = (Object[]) companyStatisticManager.getOverallStatistics();
        stat.setCompaniesCount(oStatistic[0] != null ? oStatistic[0].toString() : "0");
        stat.setUsersCount(oStatistic[1] != null ? oStatistic[1].toString() : "0");
        stat.setSystemAccessCount(oStatistic[2] != null ? oStatistic[2].toString() : "0");
        stat.setDepartmentCount(oStatistic[3] != null ? oStatistic[3].toString() : "0");
        stat.setProjectCount(oStatistic[4] != null ? oStatistic[4].toString() : "0");
        stat.setTaskCount(oStatistic[5] != null ? oStatistic[5].toString() : "0");
        stat.setTimesheetCount(oStatistic[6] != null ? oStatistic[6].toString() : "0");
        stat.setClientsCount(oStatistic[7] != null ? oStatistic[7].toString() : "0");
        stat.setLeadCount(oStatistic[8] != null ? oStatistic[8].toString() : "0");
        return stat;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SignupsRate getSignupsRate(String days) {

        setStardAndEndDate(days);
        Object[] result = (Object[]) companyStatisticManager.getSignuppersRate(sdate, edate);
        SignupsRate signupsRate = new SignupsRate();
        Long TS = Long.valueOf(result[0].toString());
        Long NU = getEmployeesCountByDateLimit();
        Long ACT = Long.valueOf(result[1].toString());
        Long ACTINPERCENTAGE = null;
        Long INACT = Long.valueOf(result[2].toString());
        Long INACTINPERCENTAGE = null;
        Long USED = getSystemUsedCountByDateLimit();
        Long USEDINPERCENTAGE = null;
        Long BOUNCED = getSystemBouncedCountByDateLimit();
        Long BOUNCEDINPERCENTAGE = null;
        if (TS > 0) {
            if (ACT != null) {
                ACTINPERCENTAGE = (ACT * 100) / TS;
            }
            if (INACT != null) {
                INACTINPERCENTAGE = (INACT * 100) / TS;
            }
            if (USED != null) {
                USEDINPERCENTAGE = (USED * 100) / TS;
            }
            if (BOUNCED != null) {
                BOUNCEDINPERCENTAGE = (BOUNCED * 100) / TS;
            }

        }
        signupsRate.setSignups(TS.toString());
        signupsRate.setNewUsers(NU != null ? NU.toString() : "0");
        signupsRate.setActivated(ACT != null ? ACT.toString() : "0");
        signupsRate.setActivatedInPercentage(ACTINPERCENTAGE != null ? ACTINPERCENTAGE.toString() : "0");
        signupsRate.setInactive(INACT != null ? INACT.toString() : "0");
        signupsRate.setInactiveInPercentage(INACTINPERCENTAGE != null ? INACTINPERCENTAGE.toString() : "0");
        signupsRate.setUsed(USED != null ? USED.toString() : "0");
        signupsRate.setUsedInPercentage(USEDINPERCENTAGE != null ? USEDINPERCENTAGE.toString() : "0");
        signupsRate.setBounce(BOUNCED != null ? BOUNCED.toString() : "0");
        signupsRate.setBounceInPercentage(BOUNCEDINPERCENTAGE != null ? BOUNCEDINPERCENTAGE.toString() : "0");

        return signupsRate;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CountryList getCountryList(String days) {

        setStardAndEndDate(days);
        List<Object[]> countries = null;
        try {
            countries = companyStatisticManager.getCountriesByDate(sdate, edate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CountryListItem[] result = null;
        if (countries.size() > 0) {
            result = new CountryListItem[countries.size()];
            Long totalUsers = companyStatisticManager.getLastUpdationCount(sdate, edate);
            int i = 0;
            try {
                for (Object[] country : countries) {
                    result[i] = new CountryListItem();
                    result[i].setSystemUsedCount(country[0] != null ? country[0].toString() : "0");
                    result[i].setCountry(country[1] != null ? country[1].toString() : "");
                    Long value = Long.valueOf(country[0].toString());
                    value = (value * 100) / totalUsers;
                    result[i].setSystemUsedCountInPercentage(value.toString());
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new CountryList(result);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public IndustryList getIndustryList(String days) {

        setStardAndEndDate(days);
        List<Object[]> industries = null;
        try {
            industries = companyStatisticManager.getIndustriesByDate(sdate, edate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        IndustryListItem[] result = null;
        if (industries.size() > 0) {
            result = new IndustryListItem[industries.size()];
            Long totalUsers = companyStatisticManager.getLastUpdationCount(sdate, edate);
            int i = 0;
            try {
                for (Object[] industry : industries) {
                    result[i] = new IndustryListItem();
                    result[i].setSystemUsedCount(industry[0] != null ? industry[0].toString() : "0");
                    result[i].setIndustry(industry[1] != null ? industry[1].toString() : "");
                    Long value = Long.valueOf(industry[0].toString());
                    value = (value * 100) / totalUsers;
                    result[i].setSystemUsedCountInPercentage(value.toString());
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new IndustryList(result);
    }

    private void setStardAndEndDate(String days) {
        switch (days) {
            case TODAY -> {
                sdate = new Timestamp(tdate.getYear(), tdate.getMonth(), (tdate.getDate()), 0, 0, 0, 0);
                edate = new Timestamp(tdate.getYear(), tdate.getMonth(), (tdate.getDate() + 1), 0, 0, 0, 0);
            }
            case YESTERDAY -> {
                sdate = new Timestamp(tdate.getYear(), tdate.getMonth(), (tdate.getDate() - 1), 0, 0, 0, 0);
                edate = new Timestamp(tdate.getYear(), tdate.getMonth(), (tdate.getDate()), 0, 0, 0, 0);
            }
            case LAST_TEN_DAYS -> {
                sdate = new Timestamp(tdate.getYear(), tdate.getMonth(), (tdate.getDate() - 10), 0, 0, 0, 0);
                edate = new Timestamp(tdate.getYear(), tdate.getMonth(), (tdate.getDate() + 1), 0, 0, 0, 0);
            }
            case LAST_MONTH -> {
                sdate = new Timestamp(tdate.getYear(), tdate.getMonth() - 1, (1), 0, 0, 0, 0);
                edate = new Timestamp(tdate.getYear(), tdate.getMonth(), (1), 0, 0, 0, 0);
            }
            case THIS_MONTH -> {
                sdate = new Timestamp(tdate.getYear(), tdate.getMonth(), (1), 0, 0, 0, 0);
                edate = new Timestamp(tdate.getYear(), tdate.getMonth(), (tdate.getDate() + 1), 0, 0, 0, 0);
            }
            case ALL_PERIOD -> {
                sdate = new Timestamp(tdate.getYear(), 0, 1, 0, 0, 0, 0);
                edate = new Timestamp(tdate.getYear(), tdate.getMonth(), (tdate.getDate() + 1), 0, 0, 0, 0);
            }
            default -> {
                sdate = new Timestamp(tdate.getYear(), tdate.getMonth(), (tdate.getDate()), 0, 0, 0, 0);
                edate = new Timestamp(tdate.getYear(), tdate.getMonth(), (tdate.getDate() + 1), 0, 0, 0, 0);
            }
        }
    }

    /**
     * the purpose is to get all companies statistics.
     *
     * @return companySize() : Companies updated.
     */
    @Transactional
    public Integer updateCompaniesStatistic() {
        log.info(">>>> Begin company update statistic");
        if (getUpdateInProgress()) {
            log.info(">>>> Other user may be updating. Please try after some time");
            return -1;
        }
        int companySize = updateCompaniesStatistics();
        log.info(">>>> DONE ALL COMPANY UPDATE STATISTICS");
        return companySize;
    }

    private int updateCompaniesStatistics() {
        List<EdsCompany> companyList;
        List<String> existSchemas = companyManager.getExistingSchemas();
        try {
            companyList = jdbcSpringManager.getCompanyList();
        } catch (Exception e) {
            return 0;
        }
        int companySize = companyList.size();
        for (EdsCompany company : companyList) {
            if (existSchemas == null || !existSchemas.contains(company.getObjectID().toString())) {
                continue;
            }
            log.info("Begin update statistic for COMPANY = " + company.getObjectID());
            setUpdateInProgress(true);
            ServerSecurityContext.getInstance().setCompanyId(company.getObjectID().toString());

            List<Object[]> companyStatisticList = companyStatisticManager.getCompanyStatistic();
            if (companyStatisticList.isEmpty()) {
                companySize--;
            }

            for (Object[] statistic : companyStatisticList) {
                EdsCompanyStatistic companyStatistic = companyStatisticManager.getStatisticByCompanyID(company.getObjectID());
                if (companyStatistic == null) {
                    companyStatistic = new EdsCompanyStatistic();
                }
                wrapStatisticData(statistic, companyStatistic);
                companyStatisticManager.createOrUpdate(companyStatistic);
            }
            setUpdateInProgress(false);
        }
        return companySize;
    }

    public void wrapStatisticData(Object[] statistic, EdsCompanyStatistic companystat) {
        companystat.setCompanyID((Integer) statistic[0]);
        companystat.setCompanyName((String) statistic[1]);
        companystat.setCountry(statistic[2] != null ? statistic[2].toString() : "");
        companystat.setIndustry(statistic[3] != null ? statistic[3].toString() : "");
        companystat.setContactPerson(statistic[4] != null ? statistic[4].toString() : "");
        companystat.setPhone(statistic[5] != null ? statistic[5].toString() : "");
        companystat.setSignedUpPage(statistic[6] != null ? statistic[6].toString() : "");
        String companySignedUpFrom = statistic[7] != null ? (String) statistic[7] : "N/A";
        if (SIGNED_UP_FROM_GOOGLE_MARKETPLACE.equals(companySignedUpFrom)) {
            companySignedUpFrom += statistic[8] != null ? (" (" + statistic[8] + ")") : "";
        } else if (companySignedUpFrom.equals("N/A")) {
            companySignedUpFrom = statistic[9] != null ? statistic[9].toString() : "N/A";
        }
        companystat.setCompanySignedUpFrom(companySignedUpFrom);
        companystat.setHost(statistic[9] != null ? statistic[9].toString() : "N/A");
        companystat.setClientSignUpCompIP(statistic[10] != null ? (String) statistic[10] : "N/A");
        companystat.setActivated(statistic[11] != null && (Boolean) statistic[11]);
        companystat.setAccessCount(statistic[12] != null ? Integer.parseInt(statistic[12].toString()) : 0);
        companystat.setFirstAccessDate(statistic[13] != null ? (Date) statistic[13] : null);
        companystat.setLastAccessDate(statistic[14] != null ? (Date) statistic[14] : null);
        companystat.setRegistrationDate(statistic[15] != null ? (Date) statistic[15] : null);
        companystat.setPeriodAccess(companystat.getFirstAccessDate(), companystat.getLastAccessDate());
        companystat.setUserCount(statistic[16] != null ? Integer.parseInt(statistic[16].toString()) : 0);
        companystat.setActiveUsersCount(statistic[17] != null ? Integer.parseInt(statistic[17].toString()) : 0);
        companystat.setProjectCount(statistic[18] != null ? Integer.valueOf(statistic[18].toString()) : 0);
        companystat.setTaskCount(statistic[19] != null ? Integer.valueOf(statistic[19].toString()) : 0);
        companystat.setTimesheetCount(statistic[20] != null ? Integer.valueOf(statistic[20].toString()) : 0);
        companystat.setClientCount(statistic[21] != null ? Integer.valueOf(statistic[21].toString()) : 0);
        companystat.setSupplierCount(statistic[22] != null ? Integer.valueOf(statistic[22].toString()) : 0);
        companystat.setLeadCount(statistic[23] != null ? Integer.valueOf(statistic[23].toString()) : 0);
        companystat.setContactCount(statistic[24] != null ? Integer.valueOf(statistic[24].toString()) : 0);
        companystat.setCrmtaskCount(0);
        companystat.setEventCount(statistic[26] != null ? Integer.valueOf(statistic[26].toString()) : 0);
        companystat.setCaseCount(statistic[27] != null ? Integer.valueOf(statistic[27].toString()) : 0);
        companystat.setInvoiceCount(statistic[28] != null ? Integer.valueOf(statistic[28].toString()) : 0);
        companystat.setExpenseCount(statistic[29] != null ? Integer.valueOf(statistic[29].toString()) : 0);
        companystat.setProductCount(statistic[30] != null ? Integer.valueOf(statistic[30].toString()) : 0);
        companystat.setFolderCount(statistic[31] != null ? Integer.valueOf(statistic[31].toString()) : 0);
        companystat.setFileCount(statistic[32] != null ? Integer.valueOf(statistic[32].toString()) : 0);
        companystat.setAdminEmail(statistic[33] != null ? statistic[33].toString() : "");
        companystat.setAdminName(statistic[34] != null ? statistic[34].toString() : "");
        companystat.setAffiliate(statistic[35] != null ? statistic[35].toString() : "");
        companystat.setCompaing(statistic[36] != null ? statistic[36].toString() : "");
        companystat.setSource(statistic[37] != null ? statistic[37].toString() : "");
        companystat.setMedium(statistic[38] != null ? statistic[38].toString() : "");
        companystat.setRedirected(statistic[39] != null ? statistic[39].toString() : "");
        companystat.setReferrer(statistic[40] != null ? statistic[40].toString() : "");
        companystat.setGclid(statistic[41] != null ? statistic[41].toString() : "");
        companystat.setNoAccessUsersCount(statistic[42] != null ? Integer.valueOf(statistic[42].toString()) : 0);
        if (statistic[43] != null) {
            companystat.setEssUsersCount(Integer.valueOf(statistic[43].toString()));
            companystat.setActiveUsersCount(companystat.getActiveUsersCount() - companystat.getEssUsersCount());
        }
        companystat.setCurrentUsagePlanId(statistic[44] != null ? Integer.valueOf(statistic[44].toString()) : 0);
        companystat.setPlannedActiveUsers(statistic[45] != null ? Integer.valueOf(statistic[45].toString()) : 0);
        companystat.setPlannedEssUsers(statistic[46] != null ? Integer.valueOf(statistic[46].toString()) : 0);
        companystat.setPlannedNoAccessUsers(statistic[47] != null ? Integer.valueOf(statistic[47].toString()) : 0);
        companystat.setUsagPlanStartDate(statistic[48] != null ? (Date) statistic[48] : null);
        companystat.setUsagPlanEndDate(statistic[49] != null ? (Date) statistic[49] : null);
        companystat.setUsagePlanUserRate(statistic[50] != null ? Float.valueOf(statistic[50].toString()) : 0);
        companystat.setUsagePlanPaymentType(statistic[51] != null ? statistic[51].toString() : "");
        companystat.setUsagePlanPaymentStatus(statistic[52] != null ? statistic[52].toString() : "");
        if (statistic[53] != null) {
            companystat.setClientContactCount(Integer.valueOf(statistic[53].toString()));
            companystat.setActiveUsersCount(companystat.getActiveUsersCount() - companystat.getClientContactCount());
        }
        companystat.setOrgType((String) statistic[54]);
        companystat.setAdminPhone((String) statistic[55]);
        companystat.setStatisticUpdatedTime(new Date());
    }

    public void markasTestCompany(Integer companyID) {
        try {
            EdsCompany company = companyManager.getCompany(companyID);
            company.setTestCompany(true);
            companyManager.update(company);
            companyStatisticManager.deleteByCompanyID(companyID);
        } catch (RuntimeException e) {

            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getCompanyEmail(Integer companyID) {
        EdsCompanyStatistic companyStatistic = companyStatisticManager.getStatisticByCompanyID(companyID);
        return companyStatistic == null ? null : companyStatistic.getEmail();
    }

    public void sendMessage(String to, String subject, String text) {
        try {
            messageManager.sendMessageFromUser(null, to, null, null, subject, text, false, null, null, false, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCompany(Integer companyID) {
        List<Object[]> companyStatistic = companyStatisticManager.getCompanyStatistics(companyID);
        Object[] statistic = companyStatistic.get(0);
        EdsCompanyStatistic companystat = companyStatisticManager.getStatisticByCompanyID(companyID);
        if (companystat == null) {
            companystat = new EdsCompanyStatistic();
        }
        try {
            companystat.setCompanyName((String) statistic[0]);
            companystat.setAccessCount(statistic[1] != null ? Integer.parseInt(statistic[1].toString()) : 0);
            companystat.setLastAccessDate(statistic[2] != null ? (Date) statistic[2] : null);
            companystat.setRegistrationDate(statistic[3] != null ? (Date) statistic[3] : null);
            companystat.setActivated(statistic[4] != null && (Boolean) statistic[4]);
            companystat.setUserCount(statistic[5] != null ? Integer.parseInt(statistic[5].toString()) : 0);
            companystat.setTaskCount(statistic[6] != null ? Integer.valueOf(statistic[6].toString()) : 0);
            companystat.setClientCount(statistic[10] != null ? Integer.valueOf(statistic[10].toString()) : 0);
            companystat.setAppraisalsCount(statistic[12] != null ? Integer.valueOf(statistic[12].toString()) : 0);
            companystat.setCountry(statistic[13] != null ? statistic[13].toString() : "");
            companystat.setIndustry(statistic[14] != null ? statistic[14].toString() : "");
            companystat.setEmail(statistic[15] != null ? statistic[15].toString() : "");
            companystat.setContactPerson(statistic[16] != null ? statistic[16].toString() : "");
            companystat.setPhone(statistic[17] != null ? statistic[17].toString() : "");
            companystat.setSignedUpPage(statistic[18] != null ? statistic[18].toString() : "");
            companystat.setActiveUsersCount(statistic[20] != null ? Integer.valueOf(statistic[20].toString()) : 0);
            companystat.setInvoiceCount(statistic[25] != null ? Integer.valueOf(statistic[25].toString()) : 0);
            companystat.setAdminEmail(statistic[26] != null ? statistic[26].toString() : "");
            companystat.setOrgType((String) statistic[27]);
            companyStatisticManager.update(companystat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resendActivationLink(Integer companyID) {

        EdsCompany company = companyManager.getCompany(companyID);
        String remoteAddr = "remote address";
        List<EdsEmployee> adminList = employeeManager.getAdministrators();
        try {
            messageManager.resendCompanyRegistrationNotification(adminList, company, remoteAddr);
        } catch (EdsDbException ignored) {
        }


    }

    @Transactional
    public void resendEmployeesActivationLink(Integer companyID) {
        String backendCompanyID = ServerSecurityContext.getInstance().getCompanyId();
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        try {
            List<EdsEmployee> employeesList = employeeManager.getInactiveEmployees();
            List<EdsEmployee> adminList = employeeManager.getAdministrators();
            EdsEmployee admin = adminList.get(0);

            messageManager.resendEmployeesRegistrationNotification(employeesList, admin);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ServerSecurityContext.getInstance().setCompanyId(backendCompanyID);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ActivationLinkList getActivationLinkList(Integer companyID) {
        String backendCompanyID = ServerSecurityContext.getInstance().getCompanyId();
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        ActivationLinkListItem[] result = null;
        try {
            List<EdsEmployee> employeesList = employeeManager.getInactiveEmployees();
            if (employeesList.size() > 0) {

                result = new ActivationLinkListItem[employeesList.size()];
                int i = 0;
                String uid;
                String cid;
                String link;
                String host;
                host = EdsContextParams.getFullHost();

                for (EdsEmployee employee : employeesList) {
                    result[i] = new ActivationLinkListItem();
                    uid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(employee.getObjectID().toString()));
                    cid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(employee.getCompany().getObjectID().toString()));

                    link = host + "account?uid=" + uid + "&cid=" + cid;
                    result[i].setFisrtName(employee.getFirstName() != null ? employee.getFirstName() : "");
                    result[i].setLastName(employee.getLastName() != null ? employee.getLastName() : "");
                    result[i].setEmail(employee.getEmail() != null ? employee.getEmail() : "");
                    result[i].setActivationLink(link);
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ServerSecurityContext.getInstance().setCompanyId(backendCompanyID);
        }
        return new ActivationLinkList(result);
    }

    public void disableAccount(Integer companyID) {
        EdsCompany company = companyManager.getCompany(companyID);
        SecurityContext.setCompanyID(companyID);
        List<EdsEmployee> employeelist = employeeManager.getEmployees(company);
        for (EdsEmployee user : employeelist) {
            user.setEmail("test@workforcetrack.com");
            employeeManager.update(user);
        }
    }

    public void convertMarketplace(Integer companyID, String googleAppsDomain) {

        EdsCompany company = companyManager.getCompany(companyID);
        EmailAddressValidator.getHost(company.getEmail());

        SecurityContext.setCompanyID(companyID);
        if (globalAuthJdbcSpringManager.findByGoogleAppDomain(googleAppsDomain) == null) {
            EdsCompanySystemSettings systemSettings = companySystemSettingsManager.findByCompanyID(companyID);
            systemSettings.setGoogleAppDomain(googleAppsDomain);
            systemSettings.setCompanySignedUpFrom(SIGNED_UP_FROM_GOOGLE_MARKETPLACE);
            companySystemSettingsManager.create(systemSettings);
            globalAuthJdbcSpringManager.updateCompanyDomainInfo(companyID, googleAppsDomain);
        } else {
            throw new RuntimeException("Company with this domain already exists");
        }
    }

    public String getCompanyDomain(Integer companyID) {
        EdsCompanySystemSettings systemSettings = companySystemSettingsManager.findByCompanyID(companyID);

        if (systemSettings.getGoogleAppDomain() != null) {
            return systemSettings.getGoogleAppDomain();
        }
        return EmailAddressValidator.getHost(systemSettings.getAdminEmail());
    }

    public String[] removetestmails(String s) {

        String[] s1 = s.split(",");
        int len = s1.length;
        int i = 0;
        int size;
        int j = -1;
        String[] result = new String[len];
        for (; i < len; i++) {
            List<EdsEmployee> employee = employeeManager.getEmployeesByEmail(s1[i]);
            size = employee.size();
            if (size > 0) {
                for (EdsEmployee user : employee) {
                    user.setEmail("test@workforcetrack.com");
                    employeeManager.update(user);
                }

            } else {
                ++j;
                result[j] = s1[i];
            }
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SubscriptionList getSubscriptiontype(ListLoadConfig config) {

        List<EdsSubscriptionType> subscriptiontype;
        subscriptiontype = subscriptionManager.getSubscriptionType();
        int totalCount = subscriptiontype.size();
        SubscriptionListItem[] result = new SubscriptionListItem[subscriptiontype.size()];
        int i = 0;
        for (EdsSubscriptionType type : subscriptiontype) {
            try {
                result[i] = new SubscriptionListItem();
                result[i].setObjectID(type.getObjectID());
                result[i].setName(type.getName());
                result[i].setEmployeeLimit(type.getEmployeeLimit());
                result[i].setProjectLimit(type.getProjectLimit());
                result[i].setTaskLimit(type.getTaskLimit());
                result[i].setDepartmentLimit(type.getDepartmentLimit());
                result[i].setAttachmentsFileSizePerUserLimit(type.getAttachmentsFileSizePerUserLimit());
                result[i].setAttachmentsSizePerCompany(type.getAttachmentsSizePerCompany());
                result[i].setAppraisalsLimit(type.getAppraisalsLimit());
                result[i].setAppraisals360Limit(type.getAppraisals360Limit());
                result[i].setInvoiceLimit(type.getInvoiceLimit());

                i++;
            } catch (RuntimeException e) {

                e.printStackTrace();
            }
        }
        return new SubscriptionList(result, totalCount);
    }

    @Transactional
    public void createSubscription(CreateSubscriptionCommand c) {
        EdsSubscriptionType type = new EdsSubscriptionType();

        type.setName(c.getName());
        type.setEmployeeLimit(c.getEmployeeLimit());
        type.setProjectLimit(c.getProjectLimit());
        type.setTaskLimit(c.getTaskLimit());
        type.setDepartmentLimit(c.getDepartmentLimit());
        type.setAttachmentsFileSizePerUserLimit(c.getAttachmentsFileSizePerUserLimit());
        type.setAttachmentsSizePerCompany(c.getAttachmentsSizePerCompany());
        type.setAppraisalsLimit(c.getAppraisalsLimit());
        type.setAppraisals360Limit(c.getAppraisals360Limit());
        type.setInvoiceLimit(c.getInvoiceLimit());


        subscriptionManager.create(type);

    }

    public void editSubscription(EditSubscription Subscription) {
        EdsSubscriptionType Subscription1 = subscriptionManager.get(Subscription.getObjectID());
        Subscription1.setName(Subscription.getName());
        Subscription1.setObjectID(Subscription.getObjectID());
        Subscription1.setTaskLimit(Subscription.getTaskLimit());
        Subscription1.setEmployeeLimit(Subscription.getEmployeeLimit());
        Subscription1.setProjectLimit(Subscription.getProjectLimit());
        Subscription1.setDepartmentLimit(Subscription.getDepartmentLimit());
        Subscription1.setAttachmentsFileSizePerUserLimit(Subscription.getAttachmentsFileSizePerUserLimit());
        Subscription1.setAttachmentsSizePerCompany(Subscription.getAttachmentsSizePerCompany());
        Subscription1.setAppraisalsLimit(Subscription.getAppraisalsLimit());
        Subscription1.setAppraisals360Limit(Subscription.getAppraisals360Limit());
        Subscription1.setInvoiceLimit(Subscription.getInvoiceLimit());

        subscriptionManager.update(Subscription1);
    }

    public void deleteSubscription(Integer objectID) {

        try {
            int a = objectID;
            subscriptionManager.deleteSubscriptionType(objectID);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String[] SubscriptiontypeById(Integer objectID) {

        String[] s = new String[11];

        EdsSubscriptionType Subscription = subscriptionManager.getId(objectID);
        s[0] = Subscription.getTaskLimit().toString();
        s[1] = Subscription.getProjectLimit().toString();
        s[2] = Subscription.getEmployeeLimit().toString();
        s[3] = Subscription.getName();
        s[4] = Subscription.getDepartmentLimit().toString();
        s[5] = Subscription.getAttachmentsFileSizePerUserLimit().toString();
        s[6] = Subscription.getAttachmentsSizePerCompany().toString();
        s[7] = Subscription.getAppraisalsLimit().toString();
        s[8] = Subscription.getAppraisals360Limit().toString();
        s[9] = Subscription.getInvoiceLimit().toString();

        s[10] = Subscription.getObjectID().toString();

        return s;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<PaypalReceiptsListItem> getPaypalReceiptsList(ListingFilterParameter fp) {
        List<EdsUsagePlan> usagePlanList = usagePlanManager.list(fp);
        Integer totalCount = usagePlanManager.listCount(fp);
        ArrayList<PaypalReceiptsListItem> result = new ArrayList<>();
        EdsUser edsUser = invoiceManager.getUser();
        for (EdsUsagePlan usagePlan : usagePlanList) {
            PaypalReceiptsListItem item = new PaypalReceiptsListItem();
            item.setObjectId(usagePlan.getObjectID());
            item.setCompanyID(usagePlan.getCompany().getObjectID());
            item.setCompanyName(usagePlan.getCompany().getName()/*!=null ? usagePlan.getCompany().getName(): ""*/);
            if (usagePlan.getStartDate() != null) {
                item.setSubscStartDate(ServerUtils.convertServerDateToUserDate(usagePlan.getStartDate(), edsUser.getUserTimezone()).toString()/*!=null ? usagePlan.getStartDate().toString() : ""*/);
            }
            if (usagePlan.getEndDate() != null) {
                item.setSubscEndDate(ServerUtils.convertServerDateToUserDate(usagePlan.getEndDate(), edsUser.getUserTimezone()).toString()/*!=null ? usagePlan.getEndDate().toString() : ""*/);
            }
            item.setNumberOfEmployees(usagePlan.getUsers() != null ? usagePlan.getUsers() : 0);
            item.setPaidAmount(usagePlan.getTotalAmount());
            //Get payment type from transaction
            EdsSubscriptionPayment payment = subscriptionPaymentManager.getByUsageplanUID(usagePlan.getUnique_guid());
            if (payment != null && payment.getPaymentType() != null) {
                item.setPaymenttype(payment.getPaymentType().getCode());
            }
            item.setStatus(usagePlan.getStatus().getName() != null ? usagePlan.getStatus().getName() : usagePlan.getStatus().getCode());
            result.add(item);
        }
        return new ListResult<>(result, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public BugList getBugLists(ListingFilterParameter fp, ListLoadConfig config) {
        List<EdsBugReport> bugList = bugReportManager.getBugLists(fp);
        int totalCount = bugList.size();

        ComparatorFactory factory = null;
        if (config.getSortField() != null && !"".equals(config.getSortField())) {
            factory = comparatorFactoriesBugList.get(config.getSortField());
        }
        int sortDir = config.getSortDir();
        if (factory == null) {
            factory = comparatorFactoriesBugList.get(BugListItem.UPDATE_TIME);
            sortDir = Constants.DESC;
        }
        bugList.sort(factory.createComparator(sortDir));

        if (config.getLimit() > 0) {
            bugList = ListUtils.getSublist(bugList, config.getStart(), config.getLimit());
        }

        BugListItem[] result = new BugListItem[bugList.size()];
        int i = 0;

        for (EdsBugReport bug : bugList) {
            try {
                result[i] = new BugListItem();
                result[i].setBugId(bug.getObjectID() != null ? Integer.toString(bug.getObjectID()) : "");
                result[i].setBug(bug.getDescription() != null ? bug.getDescription() : "");
                result[i].setSubject(bug.getSubject() != null ? bug.getSubject() : "");
                result[i].setCreationTime(formatter.format(bug.getCreationTime()));
                result[i].setUpdateTime(bug.getUpdateTime() != null ? formatter.format(bug.getUpdateTime()) : "");
                result[i].setStatus(bug.getStatus() != null ? getUpperLowerCaseString(bug.getStatus()) : "");
                result[i].setPriority(bug.getPriority() != null ? getUpperLowerCaseString(bug.getPriority()) : "");
                result[i].setLabel(bug.getLabel() != null ? getUpperLowerCaseString(bug.getLabel()) : "");
                result[i].setCreatedFrom(bug.getCreatedFrom() != null ? bug.getCreatedFrom() : "");
                result[i].setCompany(bug.getCompany() != null ? (bug.getCompanyName() + " (CompanyID=" + bug.getCompany() + ") ") : "");
                result[i].setUser(bug.getCreatorName() != null ? bug.getCreatorName() : "");
                result[i].setAssignee(bug.getAssignName() != null ? bug.getAssignName() : "");
                result[i].setComment(bug.getComment() != null ? bug.getComment() : "");
                result[i].setEmail(bug.getEmail() != null ? bug.getEmail() : "");
                result[i].setBrowser(bug.getUserAgent() != null ? getUserAgentAccessLog(bug.getUserAgent()) : "");

                if (bug.getBugAttachments() != null && bug.getBugAttachments().size() > 0) {
                    FileItem[] attachments = getAttachments(bug.getBugAttachments(), bug.getCompany());
                    result[i].setAttachments(attachments);
                } else {
                    result[i].setAttachments(new FileItem[0]);
                }
                if (bug.getBugHistory() != null && bug.getBugHistory().size() > 0) {
                    result[i].setBugHistory(getBugHistories(bug.getBugHistory()));
                } else {
                    result[i].setBugHistory(new BugListItem[0]);
                }
                if (bug.getComments() != null && bug.getComments().size() > 0) {
                    result[i].setBugCommentsHistr(getBugComments(bug.getComments()));
                } else {
                    result[i].setBugCommentsHistr(new BugComment[0]);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }

        return new BugList(result, totalCount);
    }

    private String getUpperLowerCaseString(String s) {
        if (s.contains(" ")) {
            String[] split = s.split(" ");
            String lover1 = split[0].toLowerCase();
            String lover2 = split[1].toLowerCase();
            String result1 = lover1.substring(0, 1).toUpperCase() + lover1.substring(1);
            String result2 = lover2.substring(0, 1).toUpperCase() + lover2.substring(1);
            return (result1 + " " + result2);
        } else if (s.contains("_")) {
            String[] split = s.split("_");
            String lover1 = split[0].toLowerCase();
            String lover2 = split[1].toLowerCase();
            String result1 = lover1.substring(0, 1).toUpperCase() + lover1.substring(1);
            String result2 = lover2.substring(0, 1).toUpperCase() + lover2.substring(1);
            return (result1 + " " + result2);
        } else {
            String lover = s.toLowerCase();
            return lover.substring(0, 1).toUpperCase() + lover.substring(1);
        }
    }

    private BugListItem[] getBugHistories(List<EdsBugReport> bugHistories) {
        BugListItem[] bugListItems = new BugListItem[bugHistories.size()];
        int i = 0;
        for (EdsBugReport bugReport : bugHistories) {
            bugListItems[i] = new BugListItem();
            bugListItems[i].setPriority(bugReport.getPriority() != null ? getUpperLowerCaseString(bugReport.getPriority()) : "");
            bugListItems[i].setStatus(bugReport.getStatus() != null ? getUpperLowerCaseString(bugReport.getStatus()) : "");
            bugListItems[i].setComment(bugReport.getComment() != null ? bugReport.getComment() : "");
            bugListItems[i].setUpdaterName(bugReport.getUpdaterName());
            bugListItems[i].setUpdateTime(formatter.format(bugReport.getUpdateTime()));
            i++;
        }
        return bugListItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FileItem[] getAttachments(List<EdsBugAttachment> bugIds, Integer userCompanyID) {
        ServerSecurityContext.getInstance().setCompanyId(userCompanyID);
        if (!companyManager.schemaExists(String.valueOf(userCompanyID))) {
            return new FileItem[0];
        }
        ArrayList<FileItem> fileItems = new ArrayList<>();
        for (EdsBugAttachment bugId : bugIds) {
            EdsAttachment bugAttachment = attachmentManager.get(bugId.getAttachmentID());
            if (bugAttachment != null) {
                FileItem fileItem = new FileItem();
                fileItem.setAttachmentId(bugAttachment.getObjectID());
                fileItem.setAmazonLink(commonServiceLocal.getFileUrl(bugId.getAttachmentID()));
                fileItem.setCompanyID(userCompanyID);
                fileItem.setFileName(bugAttachment.getOriginalName());
                fileItem.setDescription(bugAttachment.getDescription());
                fileItem.setSize(bugAttachment.getSize());
                fileItem.setUploadType(bugAttachment.getType().getCode());
                if (bugAttachment.getType().getCode().equals(GOOGLE)) {
                    fileItem.setGoogleDocumentLink(sinxDocumentsSettingsManager.getSinxDocsSettings(bugAttachment).getDocumentLink());
                } else if (bugAttachment.getType().getCode().equals(OFFICE_365) || bugAttachment.getType().getCode().equals(OFFICE_365_SHARE_POINT)) {
                    fileItem.setGoogleDocumentLink(sinxDocumentsSettingsManager.getSinxDocsSettings(bugAttachment).getDocumentLink());
                }
                fileItems.add(fileItem);
            }
        }
        return fileItems.toArray(new FileItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<BugsPerEmployeesListItem> getBugsPerEmployees(ListingFilterParameter fp) {

        String newStatusRef = BUG_STATUS_NEW;

        List<Object[]> bugListEmployess = bugReportManager.getBugsPerEmployees(newStatusRef, BUG_STATUS_RESOLVED,
                BUG_STATUS_UNDER_INVESTIGATION, BUG_STATUS_IN_PROGRESS, BUG_STATUS_IGNORED, BUG_STATUS_DONE, fp);
        int totalCount = bugListEmployess.size();

        ArrayList<BugsPerEmployeesListItem> results = new ArrayList<>();

        for (Object[] bugEmployee : bugListEmployess) {
            BugsPerEmployeesListItem result = new BugsPerEmployeesListItem();
            result.setObjectID(bugEmployee[0] != null ? (Integer) bugEmployee[0] : Integer.valueOf(0));
            result.setNewStatusName(newStatusRef);
            if (bugEmployee[1] != null) {
                result.setEmployee(bugEmployee[1].toString());
            } else {
                result.setEmployee("Un Assigned");
            }
            result.setStatusNew(Integer.valueOf(bugEmployee[2].toString()));
            result.setResolved(Integer.valueOf(bugEmployee[3].toString()));
            result.setUnderInvest(Integer.valueOf(bugEmployee[4].toString()));
            result.setInProgress(Integer.valueOf(bugEmployee[5].toString()));
            result.setIgnored(Integer.valueOf(bugEmployee[6].toString()));
            result.setDone(Integer.valueOf(bugEmployee[7].toString()));
            result.setTotal(Integer.valueOf(bugEmployee[8].toString()));
            results.add(result);
        }
        return new ListResult<>(results, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<BugsPerEmployeesListItem> getBugsPerSections(ListingFilterParameter fp) {

        List<Object[]> bugListSection = bugReportManager.getBugsPerSection(BUG_STATUS_NEW, BUG_STATUS_RESOLVED,
                BUG_STATUS_UNDER_INVESTIGATION, BUG_STATUS_IN_PROGRESS, BUG_STATUS_IGNORED, BUG_STATUS_DONE, fp);
        int totalCount = bugListSection.size();
        if (fp.getLimit() > 0) {
            bugListSection = ListUtils.getSublist(bugListSection, fp.getStart(), fp.getLimit());
        }
        ArrayList<BugsPerEmployeesListItem> result = new ArrayList<>();
        for (Object[] bugSection : bugListSection) {
            BugsPerEmployeesListItem item = new BugsPerEmployeesListItem();
            item.setSection(bugSection[0] != null ? bugSection[0].toString() : "(another section)");
            item.setStatusNew(Integer.valueOf(bugSection[1].toString()));
            item.setResolved(Integer.valueOf(bugSection[2].toString()));
            item.setUnderInvest(Integer.valueOf(bugSection[3].toString()));
            item.setInProgress(Integer.valueOf(bugSection[4].toString()));
            item.setIgnored(Integer.valueOf(bugSection[5].toString()));
            item.setDone(Integer.valueOf(bugSection[6].toString()));
            item.setTotal(Integer.valueOf(bugSection[7].toString()));
            result.add(item);
        }
        return new ListResult<>(result, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<AccessLogListItem> getAccessLog(ListingFilterParameter fp) {
        log.info("Begin backend getAccessLog");
        AccessLogList logList = jdbcSpringManager.getAccessLogSection(fp);

        int i = 0;
        ArrayList<AccessLogListItem> result = new ArrayList<>();
        for (AccessLogListItem item : logList.getAccessLogListItems()) {
            if (item.getAccessedSection() == null) {
                item.setAccessedSection("");
            }

            UserSessionHistoryItem[] userSessionHistoryItems = getAccessUserLogHistorys(item.getCompanyid(), item.getObjectID());
            if (userSessionHistoryItems.length > 0) {
                item.setUserSessionHistory(userSessionHistoryItems);
            } else {
                item.setUserSessionHistory(new UserSessionHistoryItem[0]);
            }
            result.add(item);
        }
        log.info("DONE backend getAccessLog");
        //noinspection ToArrayCallWithZeroLengthArrayArgument
        return new ListResult<>(result, logList.getTotalCount());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<BackendManagementListItem> getBackendManagementList(ListingFilterParameter fp) {

        List<EdsBackendManagement> getBackendManagementList = backendManagementManager.getBackendManagements(fp);
        Integer totalCount = backendManagementManager.getBackendManagementsCount(fp);
        ArrayList<BackendManagementListItem> listItems = new ArrayList<>();
        for (EdsBackendManagement backendManagement : getBackendManagementList) {
            BackendManagementListItem listItem = new BackendManagementListItem();
            listItem.setObjectID(backendManagement.getObjectID());
            listItem.setCompanyID(backendManagement.getCompanyID());
            listItem.setCompanyName(backendManagement.getCompanyName());
            listItem.setCreatorID(backendManagement.getCreatorID());
            listItem.setCreatorName(backendManagement.getCreatorName());
            listItem.setCreateTime(backendManagement.getCreationTime());
            listItem.setUpdaterID(backendManagement.getUpdaterID());
            listItem.setUpdaterName(backendManagement.getUpdaterName());
            listItem.setUpdateTime(backendManagement.getUpdateTime());
            listItem.setUserID(backendManagement.getUserID());
            listItem.setUserName(backendManagement.getUserName());
            listItem.setHostName(backendManagement.getHostNames());
            listItem.setPromotionCode(backendManagement.getPromotionalCode());
            listItem.setCreatorOrUpdaterLocalComputerName(backendManagement.getLocalComputerName());
            listItem.setCreatorOrUpdaterLocalHostIP(backendManagement.getLocalComputerIPAddress());
            listItem.setEnableSalesBackend(backendManagement.getEnableSalesBackend());
            listItem.setEnableSupportBackend(backendManagement.getEnableSupportBackend());
            listItem.setEnableAdminBackend(backendManagement.getEnableAdminBackend());
            listItem.setEnablePartnerAdminBackend(backendManagement.getEnablePartnerAdminBackend());
//            listItem.setEnablePDFBackend(backendManagement.getEnablePDFBackend());
            listItem.setEnableDeveloperBackend(backendManagement.getEnableDeveloperBackend());
            listItems.add(listItem);
        }

        return new ListResult<>(listItems, totalCount);
    }

    @Override
    public LinkedHashMap<String, String> getDeviceUniqueKeyListMap(ListingFilterParameter fp) {
        return globalAuthJdbcSpringManager.getCompanyUniqueKeyListMap(fp.getCompanyID());
    }

    @Override
    public ListResult<FingerPrintDeviceStatusHistoryListItem> getFingerPrintDeviceHistoryList(ListingFilterParameter fp) {
        LinkedHashMap<String, String> deviceUniqueKeyListMap = getDeviceUniqueKeyListMap(fp);
        ArrayList<String> companyUniqueKeyList = new ArrayList<>(deviceUniqueKeyListMap.keySet());
        fp.setAccountTypes(companyUniqueKeyList);
        List<EdsFingerPrintDeviceStatusHistory> list = fingerPrintDeviceStatusHistoryManager.getList(fp);
        Integer listTotal = fingerPrintDeviceStatusHistoryManager.getListTotal(fp);

        ArrayList<FingerPrintDeviceStatusHistoryListItem> result = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (EdsFingerPrintDeviceStatusHistory edsDeviceStatusHistory : list) {
                FingerPrintDeviceStatusHistoryListItem item = new FingerPrintDeviceStatusHistoryListItem();
                item.setObjectID(edsDeviceStatusHistory.getObjectID());
                item.setDeviceUniqueKey(edsDeviceStatusHistory.getDeviceUniqueKey());
                item.setDeviceName(edsDeviceStatusHistory.getDeviceName());
                item.setDeviceStatus(edsDeviceStatusHistory.getDeviceStatus().getStatus());
                item.setStatusUpdateTime(new DateNonConvertable(edsDeviceStatusHistory.getStatusUpdateTime()));
                item.setDescription(edsDeviceStatusHistory.getDescription());
                result.add(item);
            }
        }
        return new ListResult<>(result, listTotal);
    }

    public void saveBackendManagement(BackendManagementListItem managementListItem) {
        EdsBackendManagement backendManagement = new EdsBackendManagement();
        if (managementListItem.getObjectID() != null) {
            backendManagement = backendManagementManager.get(managementListItem.getObjectID());
        }
        backendManagement.setCompanyID(managementListItem.getCompanyID());
        backendManagement.setCompanyName(managementListItem.getCompanyName());
        backendManagement.setCreationTime(managementListItem.getCreateTime());
        backendManagement.setCreatorID(managementListItem.getCreatorID());
        backendManagement.setCreatorName(managementListItem.getCreatorName());
        backendManagement.setUpdaterID(managementListItem.getUpdaterID());
        backendManagement.setUpdaterName(managementListItem.getUpdaterName());
        backendManagement.setUpdateTime(managementListItem.getUpdateTime());
        backendManagement.setUserID(managementListItem.getUserID());
        backendManagement.setUserName(managementListItem.getUserName());
        backendManagement.setPromotionalCode(managementListItem.getPromotionCode());
        backendManagement.setEnableSalesBackend(managementListItem.isEnableSalesBackend());
        backendManagement.setEnableSupportBackend(managementListItem.isEnableSupportBackend());
        backendManagement.setEnableAdminBackend(managementListItem.isEnableAdminBackend());
        backendManagement.setEnablePartnerAdminBackend(managementListItem.isEnablePartnerAdminBackend());
//        backendManagement.setEnablePDFBackend(managementListItem.isEnablePDFBackend());
        backendManagement.setEnableDeveloperBackend(managementListItem.isEnableDeveloperBackend());
        backendManagement.setLocalComputerIPAddress(managementListItem.getCreatorOrUpdaterLocalHostIP());
        backendManagement.setLocalComputerName(managementListItem.getCreatorOrUpdaterLocalComputerName());
        if (managementListItem.getHostName() != null) {
            backendManagement.setHostNames(managementListItem.getHostName());
        } else {
            EdsCompanySystemSettings systemSettings = companySystemSettingsManager.findByCompanyID(managementListItem.getCompanyID());
            if (systemSettings != null) {
                backendManagement.setHostNames(systemSettings.getHost());
            }
        }

        if (backendManagement.getObjectID() == null) {
            backendManagementManager.create(backendManagement);
        } else {
            backendManagementManager.update(backendManagement);
        }
    }

    public void deleteBackendManagement(Integer objectID) {
        EdsBackendManagement backendManagement = backendManagementManager.get(objectID);
        backendManagement.setDeleted(true);
        backendManagementManager.update(backendManagement);
    }

    private String getUserAgentAccessLog(String userAgent) {
        String ua = userAgent.toLowerCase();
        if (ua.contains("opera")) {
            return ua.substring(ua.indexOf("opera"), ua.indexOf(" ", ua.indexOf(" ", ua.indexOf("opera"))));
        } else if (ua.contains("safari") && !ua.contains("chrome")) {
            String safari = ua.contains("version") ?
                    ua.substring(ua.indexOf("version"), ua.indexOf(" ", ua.indexOf(" ", ua.indexOf("version")))) : ua.substring(ua.indexOf("safari"));
            return "Safari " + safari;
        } else if (ua.contains("msie 5.0")) {
            return "IE 5.0";
        }
        if (ua.contains("msie 5.5")) {
            return "IE 5.5";
        }
        if (ua.contains("msie 6.0")) {
            return "IE 6.0";
        } else if (ua.contains("msie 7.0")) {
            return "IE 7.0";
        } else if (ua.contains("msie 8.0")) {
            return "IE 8.0";
        } else if (ua.contains("msie 9.0")) {
            return "IE 9.0";
        } else if (ua.contains("firefox")) {
            return ua.substring(ua.indexOf("firefox"));
        } else if (ua.contains("chrome")) {
            return ua.substring(ua.indexOf("chrome"), ua.indexOf(" ", ua.indexOf(" ", ua.indexOf("chrome"))));
        } else if (ua.contains("blackberry")) {
            return ua.substring(ua.indexOf("blackberry"), ua.indexOf(" ", ua.indexOf(" ", ua.indexOf("blackberry"))));
        } else if ("Outlook".equals(userAgent)) {
            return "Outlook";
        } else if ("Android".equals(userAgent)) {
            return "Android";
        } else if ("IWFTAPPFromMotorsTeam".equals(userAgent)) {
            return "IWFTAPPFromMotorsTeam";
        } else if ("Excel".equals(userAgent)) {
            return "Excel";
        } else if ("IPhone".equals(userAgent)) {
            return "IPhone";
        }
        return "Another Browser";
    }

    @Transactional
    public UserSessionHistoryItem[] getAccessUserLogHistorys(Integer companyId, Integer sessionID) {
        ServerSecurityContext.getInstance().setCompanyId(companyId);
        List<EdsUserSessionTracker> userLogHistory = userSessionTrackerManager.list(sessionID);
        UserSessionHistoryItem[] userLogHistr = new UserSessionHistoryItem[userLogHistory.size()];
        for (int i = 0; i < userLogHistory.size(); i++) {
            EdsUserSessionTracker usersHistr = userLogHistory.get(i);
            UserSessionHistoryItem item = new UserSessionHistoryItem();

            item.setAccessedSectionName(usersHistr.getSectionName() != null ? getUserSectionName(usersHistr.getSectionName()) : "");
            item.setLastAccessDate(usersHistr.getAccessTime() != null ? String.valueOf(usersHistr.getAccessTime()) : "");
            try {
                if (usersHistr.getAccessTime() != null && usersHistr.getModuleLoadedTime() != null) {
                    long diff = usersHistr.getModuleLoadedTime().getTime() - usersHistr.getAccessTime().getTime();
                    item.setModuleLoadedTime(ServerUtils.millisecondToString(diff));
                } else {
                    item.setModuleLoadedTime("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            userLogHistr[i] = item;
        }
        return userLogHistr;
    }

    private String getUserSectionName(String name) {
        switch (name) {
            case "ProjectManagement.html" -> {
                return "Project Management";
            }
            case "Crm.html" -> {
                return "CRM";
            }
            case "Accounting.html" -> {
                return "Accounting & Finance";
            }
            case "GoogleDocuments.html" -> {
                return "Documents";
            }
            case "Dashboard.html" -> {
                return "Dashboard";
            }
            case "Settings.html" -> {
                return "Settings";
            }
            case "GoogleContacts.html" -> {
                return "Google Talk";
            }
            case "Myaccount.html" -> {
                return "My Account";
            }
        }
        if (name.equals("Payroll.html")) {
            return "Payroll";
        }
        if (name.equals("Hrms.html")) {
            return "Hrms";
        }
        return "Another Section";
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getBugPriority() {
        SelectItem[] selectItems = new SelectItem[4];
        selectItems[0] = new SelectItem(1, "Critical", BUG_PRIORITY_CRITICAL);
        selectItems[1] = new SelectItem(2, "High", BUG_PRIORITY_HIGH);
        selectItems[2] = new SelectItem(3, "Medium", BUG_PRIORITY_MEDIUM);
        selectItems[3] = new SelectItem(4, "Low", BUG_PRIORITY_LOW);
        return selectItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getBugStatus() {
        SelectItem[] selectItems = new SelectItem[6];
        selectItems[0] = new SelectItem(5, "New", BUG_STATUS_NEW);
        selectItems[1] = new SelectItem(6, "Resolved", BUG_STATUS_RESOLVED);
        selectItems[2] = new SelectItem(7, "Under Investigation", BUG_STATUS_UNDER_INVESTIGATION);
        selectItems[3] = new SelectItem(8, "In Progress", BUG_STATUS_IN_PROGRESS);
        selectItems[4] = new SelectItem(9, "Ignored", BUG_STATUS_IGNORED);
        selectItems[5] = new SelectItem(10, "Done", BUG_STATUS_DONE);
        return selectItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getBugLabel() {

        SelectItem[] selectItems = new SelectItem[4];
        selectItems[0] = new SelectItem(11, "New Issue", BUG_LABEL_NEW_ISSUE);
        selectItems[1] = new SelectItem(12, "Repeated Issue", BUG_LABEL_REPEATED_ISSUE);
        selectItems[2] = new SelectItem(13, "Customization", BUG_LABEL_CUSTOMIZATION);
        selectItems[3] = new SelectItem(14, "Improvement", BUG_LABEL_IMPROVEMENT);
        return selectItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmployees() {
        List<EdsEmployee> employeesForOldWFT = projectEmployeeManager.getEmployeesByProject(46);
        List<EdsEmployee> employeesForNewWFT2011 = projectEmployeeManager.getEmployeesByProject(12728);
        List<EdsEmployee> empList = new ArrayList<>();
        empList.addAll(employeesForOldWFT);
        empList.addAll(employeesForNewWFT2011);
        SelectItem[] r = new SelectItem[empList.size()];
        int i = 0;
        for (EdsEmployee employee : empList) {
            r[i] = new SelectItem();
            r[i].setId(employee.getObjectID());
            r[i].setName(employee.getName());
            r[i].setDescription(employee.getUserName());
            i++;
        }
        Arrays.sort(r, Comparator.comparing(SelectItem::getName));

        return r;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCompanyEmployees(Integer companyID) {
        EdsCompany company = companyManager.get(companyID);
        List<EdsUser> users = userManager.getCompanyUsers(company);
//        List<EdsEmployee> users = userManager.getAdmins(companyID);
        SelectItem[] s = new SelectItem[users.size()];
        int i = 0;
        for (EdsUser user : users) {
            s[i] = new SelectItem(user.getObjectID(), user.getName(), user.getUserName());
            i++;
        }
        Arrays.sort(s, Comparator.comparing(SelectItem::getName));
        return s;
    }

    public SelectItem[] getCompanyHosts() {
        List<String> hostStrings = companySystemSettingsManager.getCompanyHosts();
        SelectItem[] s = new SelectItem[hostStrings.size()];
        int i = 0;
        for (String host : hostStrings) {
            s[i] = new SelectItem(i, host);
            i++;
        }
        return s;
    }

    public void updateBugReport(String bugId, String bugPriority, String bugStatus, String bugLabel, Integer assignId, String assignName, String comment) {
        boolean bugPriorityChanged = false;
        boolean bugStatusChanged = false;
        boolean bugLabelChanged = false;
        boolean bugAssigneeChanged = false;

        EdsBugReport bugReport = bugReportManager.get(Integer.parseInt(bugId));

        EdsBugReport chBugReport = new EdsBugReport();
        chBugReport.setParent(bugReport);
        if (bugReport.getLabel() != null) {
            chBugReport.setLabel(bugReport.getLabel());
        }
        if (bugReport.getPriority() != null) {
            chBugReport.setPriority(bugReport.getPriority());
        }
        if (bugReport.getStatus() != null) {
            chBugReport.setStatus(bugReport.getStatus());
        }
        if (bugReport.getComment() != null) {
            chBugReport.setComment(comment);
        }
        chBugReport.setUpdateTime(new Date());
        EdsUser updater = userManager.getUser();
        chBugReport.setUpdater(updater.getObjectID());
        chBugReport.setUpdaterName(updater.getUserName());
        chBugReport.setCreator(updater.getObjectID());
        chBugReport.setCreatorName(updater.getUserName());
        chBugReport.setCompany(bugReport.getCompany());
        chBugReport.setCompanyName(bugReport.getCompanyName());

        bugReportManager.create(chBugReport);

        String currentBugPriority = bugReport.getPriority();
        String currentBugStatus = bugReport.getStatus();
        String currentBugLabel = bugReport.getLabel();
        Integer currentBugAssignId = bugReport.getAssign();

        if (bugPriority != null && !bugPriority.equals(currentBugPriority)) {
            bugReport.setPriority(bugPriority);
            bugPriorityChanged = true;
        }
        if (bugStatus != null && !bugStatus.equals(currentBugStatus)) {
            bugReport.setStatus(bugStatus);
            bugStatusChanged = true;
        }
        if (bugLabel != null && !bugLabel.equals(currentBugLabel)) {
            bugReport.setLabel(bugLabel);
            bugLabelChanged = true;
        }
        if (assignId != null && !assignId.equals(currentBugAssignId)) {
            bugReport.setAssign(assignId);
            bugReport.setAssignName(assignName);
            bugAssigneeChanged = true;
        }
        if (comment != null) {
            bugReport.setComment(comment);
        }
        bugReport.setUpdateTime(new Date());

        bugReportManager.update(bugReport);

        if (bugPriorityChanged || bugStatusChanged || bugLabelChanged || bugAssigneeChanged) {
            try {
                if (bugReport.getAssign() != null) {
                    messageManager.sendBugReportChangeNotification(bugReport, bugStatusChanged);
                }
            } catch (EdsDbException e) {
                e.printStackTrace();
            }
        }
    }

    private BugComment[] getBugComments(List<EdsBugComment> bugCommentList) {
        BugComment[] bugComments = new BugComment[bugCommentList.size()];
        int i = 0;
        for (EdsBugComment bugComment : bugCommentList) {
            bugComments[i] = new BugComment();
            bugComments[i].setUser(bugComment.getCreatorName());
            bugComments[i].setCreationDate(bugComment.getCreationDate());
            bugComments[i].setText(bugComment.getText());
            i++;
        }
        return bugComments;
    }

    public void setBugComment(String bugID, String comment) {
        EdsBugComment bugComment = new EdsBugComment();
        bugComment.setCreationDate(new Date());
        EdsUser creator = userManager.getUser();
        bugComment.setCreatorName(creator.getName());
        bugComment.setText(comment);
        if (bugID != null) {
            bugComment.setBug(bugReportManager.get(Integer.parseInt(bugID)));
            bugCommentManager.create(bugComment);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AccountTypesByCategory getAccountTypes() {
        return accountingService.getAccountTypes();
    }

    public String updateCompanyAsTest(TestCompanyItem[] items) {
        return "";
    }

    public void createAccount(AddAccountItem accountItem) {
        EdsAccountTemplate accountTemplate = new EdsAccountTemplate();
        accountTemplate.setCountry(countryManager.get(accountItem.getCountryId()));
        accountTemplate.setAccountType(accountingManager.getAccountType(accountItem.getAccountTypeId()));
        accountTemplate.setCode(Integer.parseInt(accountItem.getCode()));
        accountTemplate.setCodeString(accountItem.getCode());
        accountTemplate.setName(accountItem.getName());
        accountTemplate.setDescription(accountItem.getDescription());
        if (accountItem.getTaxItem() != null) {
            accountTemplate.setTax(vatManager.get(accountItem.getTaxItem().getId()));
        }
        accountTemplate.setShowInExpense(accountItem.isShowInExpense());
        accountTemplate.setEnablePayments(accountItem.isEnablePayments());
        accountTemplateManager.create(accountTemplate);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCountries() {
        List<EdsCountry> countries = countryManager.list();
        SelectItem[] items = new SelectItem[countries.size()];
        int k = 0;
        for (EdsCountry country : countries) {
            items[k] = new SelectItem(country.getObjectID(), country.getName());
            k++;
        }
        return items;
    }

    public void saveTaxRate(TaxData data) {
        EdsTaxTemplate taxTemplate = new EdsTaxTemplate();
        taxTemplate.setCountry(countryManager.get(data.getCountryId()));
        taxTemplate.setName(data.getTaxName());
        taxTemplate.setAmount(data.getTaxRate().setScale(financialSettingsManager.getFinancialSettings().getTaxRateScale(), RoundingMode.HALF_UP));
        taxTemplateManager.create(taxTemplate);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TaxList getCompanysVatList() {
        return invoiceServiceLocal.getCompanyTaxList();
    }

    public boolean isEmployeeFromWFT() {
        EdsUser user = userManager.getUser();
        List<EdsEmployee> employeesForOldWFT = projectEmployeeManager.getEmployeesByProject(46);
        List<EdsEmployee> employeesForNewWFT2011 = projectEmployeeManager.getEmployeesByProject(12728);
        List<EdsEmployee> employees = new ArrayList<>();
        employees.addAll(employeesForOldWFT);
        employees.addAll(employeesForNewWFT2011);
        for (EdsEmployee employee : employees) {
            if (user.getObjectID().equals(employee.getObjectID())) {
                return true;
            }
        }
        return false;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<AccountManagementListItem> getValidUsers(ListingFilterParameter fp) {
        EdsUser loggedUser = userManager.getUser();
        if (fp.getCompanyID() == null) {
            fp.setCompanyID(loggedUser.getCompany().getObjectID());
        }
        AccountManagerItemList list = jdbcSpringManager.findOverallInActiveUsers(fp);
        list.setLoggedUserEmail(loggedUser.getEmail());

        if (list.getListItems() != null) {
            list.getListItems();
            for (AccountManagementListItem accountItem : list.getListItems()) {
                AccountManagementListItem item = globalAuthJdbcSpringManager.getValidUser(accountItem);
                if (item != null) {
                    accountItem.setUserCompanyId(item.getUserCompanyId());
                    accountItem.setUserId(item.getUserId());
                    accountItem.setAuthID(item.getAuthID());
                    accountItem.setLogin(item.getLogin());
                    accountItem.setPassword(item.getPassword());
                }
            }
        }
        return new ListResult<AccountManagementListItem>(new ArrayList<>(Arrays.asList(list.getListItems())), list.getTotalCount());
    }

    @Transactional
    public void changeAccountStatus(Integer companyID, Integer userID, Boolean active) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsUser user = userManager.get(userID);
        if (active) {
            user.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_INACTIVE));
        } else {
            user.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE));
        }
        userManager.update(user);
        if (!(user instanceof EdsClientContact)) {
            try {
                employeeSolrComponent.index((EdsEmployee) user);
            } catch (SolrServerException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void killUserSessions(Integer companyID, Integer userID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsUser user = userManager.get(userID);
        userSessionManager.expireUserSession(user);
    }

    @Transactional
    public void activateAndSendMessage(MessageItem item, Integer userCompanyId) {
        //Activate user
        throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean isUserExist(String email) {
        throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getBlackList(ListingFilterParameter fp, ListLoadConfig config) {
        EdsUser user = userManager.getUser();
        EdsBackendManagement backendManagement = backendManagementManager.getBackendManagement(user.getCompany().getObjectID(), user.getObjectID());
        if (backendManagement != null) {
            fp.setParams(backendManagement.getHostNames());
        }
        List<EdsBlackList> blackLists = blackListManager.blackLists(fp);
        SelectItem[] items = new SelectItem[blackLists.size()];
        int i = 0;
        for (EdsBlackList blackList : blackLists) {
            items[i] = new SelectItem(blackList.getObjectID(), blackList.getEmail(), blackList.getHostName());
            i++;
        }
        return items;
    }

    public void deleteBlackListById(Integer blackListId) {
        blackListManager.delete(blackListManager.get(blackListId));
    }

    public void saveBlackEmails(String[] emails) {
        EdsUser user = userManager.getUser();
        String hostName = EdsContextParams.getHostname();
        EdsBackendManagement backendManagement = backendManagementManager.getBackendManagement(user.getCompany().getObjectID(), user.getObjectID());
        if (backendManagement != null) {
            hostName = backendManagement.getHostNames();
        }
        EdsBlackList blackList;
        for (String email : emails) {
            blackList = new EdsBlackList();
            blackList.setEmail(email);
            blackList.setHostName(hostName);
            blackListManager.create(blackList);
        }
    }

    @Transactional
    public void changeAccountPassword(AccountManagementListItem item, String password) {
        if (item != null && password != null) {
            var actor = userManager.getUser();
            item.setActorUserId(actor.getObjectID());
            item.setActorUsername(actor.getFullName());
            item.setActorCompanyId(actor.getCompany().getObjectID());
            globalAuthJdbcSpringManager.chageAccountPasswordByAuthId(item, password);
            sessionService.expireMobileUserSessionsAcrossCompanies(item.getLogin());
        }
    }

    @Transactional
    public String changeAccountUserName(Integer userID, Integer companyID, Integer userCompanyID, String userName, String newUserName) {
        String errorMessage = null;

        if (userID != null && userCompanyID != null && userName != null && !"".equals(userName) && !"".equals(newUserName)) {
            Integer existingAuthId = userManager.findExistingUserName(userName);
            if (existingAuthId == null) {
                globalAuthJdbcSpringManager.changeAccountUserName(userCompanyID, userName);
                userManager.changeAccountUserName(userID, companyID, userName);
            } else {
                Integer oldAuthID = userManager.findExistingUserName(newUserName);
                if (oldAuthID != null) {
                    globalAuthJdbcSpringManager.changeExistingACcountCompany(existingAuthId, userID, companyID, oldAuthID);
                    userManager.changeAccountUserName(userID, companyID, newUserName);
                } else {
                    globalAuthJdbcSpringManager.changeAccountUserName(userCompanyID, newUserName);
                    userManager.changeAccountUserName(userID, companyID, newUserName);
                }
            }
            EdsBusinessEvent workflowEvent = baseEventsPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, employeeManager.get(userID), this.employeeManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_EMPLOYEE);
        } else {
            errorMessage = "Invalid " + (userID == null ? "UserID: " + userID + " " : "") + (companyID == null ? " CompanyID: " + companyID + " " : "") +
                    (userCompanyID == null ? " UserCompanyID: " + userCompanyID + " " : "") +
                    (userName == null || "".equals(userName) ? " UserName: " + userName : "") + " data";
        }
        return errorMessage;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<SubscriptionManagementItem> getSubscriptions(ListingFilterParameter fp) {
        formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        EdsUser user = userManager.getUser();
        EdsBackendManagement backendManagement = backendManagementManager.getBackendManagement(user.getCompany().getObjectID(), user.getObjectID());
        if (backendManagement != null) {
            fp.setParams(backendManagement.getHostNames());
            fp.setAccountCode(backendManagement.getPromotionalCode());
        }
        fp.setShowActive(true);
        List<String> existingCompanyList = companyManager.getExistingSchemas();
        ListingObjectItem<EdsCompany> companies = companyManager.getNonTestCompanies(fp, existingCompanyList);
        ArrayList<SubscriptionManagementItem> managementItems = new ArrayList<>();
        for (EdsCompany company : companies.getItems()) {
            SubscriptionManagementItem managementItem = getCompanySubscription(company);
            managementItems.add(managementItem);
        }

        return new ListResult<>(managementItems, companies.getTotalCount());
    }

    public SubscriptionManagementItem getCompanySubscription(EdsCompany company) {
        ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());
        SubscriptionManagementItem managementItem = new SubscriptionManagementItem();
        managementItem.setCompanyId(company.getObjectID());
        managementItem.setCompanyName(company.getName());
        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(company.getObjectID());
        if (companySystemSettings != null) {
            managementItem.setAdminEmail(companySystemSettings.getAdminEmail() != null ? companySystemSettings.getAdminEmail() : "");
        }
        EdsUsagePlan usagePlan = usagePlanManager.getCurrentUsagePlan(company);
        if (usagePlan == null) {
            usagePlan = usagePlanManager.getLastUsagePlan(company.getObjectID());
        }
        if (usagePlan != null) {
            managementItem.setCurrentUsagePlan(usagePlan.getObjectID());
        }
        try {
            boolean projectToNew = genericSettingsManager.isSettingsEnabled(company.getObjectID(), GenericSettingsEnum.CHANGED_PROJECT_PERCENT);
            managementItem.setProjectPercentNewLogic(projectToNew);
        } catch (Exception e) {
            managementItem.setProjectPercentNewLogic(false);
        }
        managementItem.setAdminUsername("");
        managementItem.setRegistrationDate(company.getRegistrationDate() != null ? formatter.format(company.getRegistrationDate()) : "");
        managementItem.setActive(company.getActive());
        managementItem.setCompanyId(company.getObjectID());
        return managementItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SubscriptionManagementItem getCompanySubscriptionManagementItem(Integer companyID) {
        SubscriptionManagementItem item = null;
        EdsCompany company = companyManager.get(companyID);
        if (company != null) {
            item = new SubscriptionManagementItem();
            item.setCompanyId(company.getObjectID());
            item.setCompanyName(company.getName());
            EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(company.getObjectID());
            if (companySystemSettings != null) {
                item.setAdminEmail(companySystemSettings.getAdminEmail() != null ? companySystemSettings.getAdminEmail() : "");
            }

            item.setAdminUsername("");
            item.setRegistrationDate(company.getRegistrationDate() != null ? formatter.format(company.getRegistrationDate()) : "");
            item.setActive(company.getActive());
            item.setCompanyId(company.getObjectID());
        }
        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UsagePlanItem getCurrentSubscriptionPlan(Integer companyId) {
        return myAccountServiceLocal.getCompanyLastUsagePlan(companyId);
    }

    public void extentSubscriptionPlanAndActivateCompany(UsagePlanItem usagePlanItem) {
        myAccountServiceLocal.updateCompanyLastUsagePlan(usagePlanItem);
        EdsCompany comp = companyManager.get(usagePlanItem.getCompanyID());
        if (comp != null) {
            ServerSecurityContext.getInstance().setCompanyId(comp.getObjectID());
            EdsCompany company = companyManager.get(usagePlanItem.getCompanyID());
            company.setActive(true);
            company.getCreator().setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE));
            try {
                employeeSolrComponent.index((EdsEmployee) company.getCreator());
            } catch (SolrServerException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ATTENTION!!! DO NOT FORGET CLEAR HIBERNATE CACHE AT THE END OF THIS MEHTOD
     * TO PREVENT DIRTY ENTITY RETRIVAL FROM CACHE OF PREVIOUS COMPANY
     * indexes all company contacts
     *
     * @param solrReindex
     */
    @Override
    @Transactional
    public void indexCompanyContacts(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();

        for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
            solrDbConsistencyManager.removeInconsistences(nextCompanyID, EdsSolrDbConsistency.CONTACT);
            solrDbConsistencyManager.flushAndClear();
            if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                solrReindex.setCompanyId(nextCompanyID);
                transactionHelper.runInANewTransaction(() -> indexCompanyContactToSolr(solrReindex));
            }
        }
    }

    @Transactional
    public void indexCompanyContactToSolr(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
//        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        profileService.clearFromDbDeletedCustomFieldsByFormId(LayoutRPC.CONTACT_FORM, null, false);
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanyCrmContact(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deleteContactIds = crmContactManager.getCompanyDeletedContactsForSolr(solrReindex);
                solrManager.removeCompanyCrmContactBuIds(deleteContactIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Contact Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsCrmContact> contacts = crmContactManager.getCompanyContactsForSolr(solrReindex, startat, limit);
        while (!contacts.isEmpty()) {
            try {
                contactSolrComponent.indexConcurrently(contacts);
            } catch (InterruptedException e) {
                log.error("Error Contact Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            crmContactManager.flushAndClear();
            startat++;
            contacts = crmContactManager.getCompanyContactsForSolr(solrReindex, (startat * limit), limit);
        }
        crmContactManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    /**
     * ATTENTION!!! DO NOT FORGET CLEAR HIBERNATE CACHE AT THE END OF THIS MEHTOD
     * TO PREVENT DIRTY ENTITY RETRIVAL FROM CACHE OF PREVIOUS COMPANY
     * indexes all company contacts
     *
     * @param solrReindex
     */
    @Override
    @Transactional
    public void indexCompanyCrmAccounts(SolrReindexRpc solrReindex) {
        List<Integer> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(solrReindex.getCompanyId());
        } else {
            companys.addAll(companyManager.getCompaniesId());
        }
        List<String> schemas = companyManager.getExistingSchemas();

        for (Integer companyId : companys) {
            solrDbConsistencyManager.removeInconsistences(companyId, EdsSolrDbConsistency.CRM_ACCOUNT);
            solrDbConsistencyManager.flushAndClear();
            if (companyId != null && !schemas.isEmpty() && schemas.contains(companyId.toString())) {
                solrReindex.setCompanyId(companyId);
                transactionHelper.runInANewTransaction(() -> indexCompanyAccountToSolr(solrReindex));
            }
        }
    }

    @Transactional
    public void indexCompanyAccountToSolr(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
//        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        profileService.clearFromDbDeletedCustomFieldsByFormId(LayoutRPC.ACCOUNT_FORM, null, false);
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanyCrmAccount(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedCrmAccountIds = crmAccountManager.getCompanyDeletedCrmAccountsForSolr(solrReindex);
                solrManager.removeCrmAccountByIds(deletedCrmAccountIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Crm Account Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsCrmAccount> crmAccounts = crmAccountManager.getCompanyCrmAccountsForSolr(solrReindex, startat, limit);
        while (!crmAccounts.isEmpty()) {
            try {
                crmAccountSolrComponent.indexConcurrently(crmAccounts);
            } catch (InterruptedException e) {
                log.error("Error Crm Account Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            crmAccountManager.flushAndClear();
            startat++;
            crmAccounts = crmAccountManager.getCompanyCrmAccountsForSolr(solrReindex, (startat * limit), limit);
        }
        crmAccountManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    @Transactional
    public void reindexProject(Integer projectId) throws CustomException {
        projectServiceLocal.indexProjectTasks(projectId);
    }

    @Override
    @Transactional
    public void indexCompanyLeads(SolrReindexRpc solrReindex) {
        if (!(Integer.valueOf(0)).equals(solrReindex.getCompanyId())) {
            indexLeads(solrReindex);
        } else {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (company.hasSchema(schemas)) {
                    System.out.println("Indexing leads of company named " + company.getName());
                    solrReindex.setCompanyId(company.getObjectID());
                    transactionHelper.runInANewTransaction(() -> indexLeads(solrReindex));
                }
            }
        }
    }

    @Override
    @Transactional
    public void indexCompanyCandidates(SolrReindexRpc solrReindex) {
        if (!(Integer.valueOf(0)).equals(solrReindex.getCompanyId())) {
            indexCandidates(solrReindex);
        } else {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (company.hasSchema(schemas)) {
                    System.out.println("Indexing candidates of company named " + company.getName());
                    solrReindex.setCompanyId(company.getObjectID());
                    transactionHelper.runInANewTransaction(() -> indexCandidates(solrReindex));
                }
            }
        }
    }

    /**
     * ATTENTION!!! DO NOT FORGET CLEAR HIBERNATE CACHE AT THE END OF THIS MEHTOD
     * TO PREVENT DIRTY ENTITY RETRIVAL FROM CACHE OF PREVIOUS COMPANY
     * indexes leads of given company
     *
     * @param solrReindex
     */
    @Transactional
    public void indexLeads(SolrReindexRpc solrReindex) {
        ServerSecurityContext.getInstance().setCompanyId(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        profileService.clearFromDbDeletedCustomFieldsByFormId(LayoutRPC.LEAD_FORM, null, false);
        solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.LEAD);
        companyManager.flushAndClear();
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeAllLead(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedLeadIds = crmContactManager.getCompanyDeletedLeadsForSolr(solrReindex);
                solrManager.removeCompanyLeadByIds(deletedLeadIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Lead Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsCrmContact> leads = crmContactManager.getCompanyLeadsForSolr(solrReindex, startat, limit);
        System.out.println(leads.size());
        while (!leads.isEmpty()) {
            try {
                contactSolrComponent.indexConcurrently(leads);
            } catch (InterruptedException e) {
                log.error("Error Lead Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            crmContactManager.flushAndClear();
            startat++;
            leads = crmContactManager.getCompanyLeadsForSolr(solrReindex, (startat * limit), limit);
        }
        crmContactManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    //    @Transactional
    public void indexCandidates(SolrReindexRpc solrReindex) {
        ServerSecurityContext.getInstance().setCompanyId(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        profileService.clearFromDbDeletedCustomFieldsByFormId(LayoutRPC.CANDIDATE_FORM, null, false);
        solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.CANDIDATE);
        solrDbConsistencyManager.flushAndClear();
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeAllCandidate(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedLeadIds = crmContactManager.getCompanyDeletedCandidatesForSolr(solrReindex);
                solrManager.removeCompanyLeadByIds(deletedLeadIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Candidate Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsCrmContact> candidates = crmContactManager.getCompanyCandidatesForSolr(solrReindex, startat, limit);
        System.out.println(candidates.size());
        while (!candidates.isEmpty()) {
            try {
                contactSolrComponent.indexConcurrently(candidates);
            } catch (InterruptedException e) {
                log.error("Error Candidate Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            crmContactManager.flushAndClear();
            startat++;
            candidates = crmContactManager.getCompanyCandidatesForSolr(solrReindex, (startat * limit), limit);
        }
        crmContactManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexCompanyNews(SolrReindexRpc solrReindex) {
        if (!(Integer.valueOf(0)).equals(solrReindex.getCompanyId())) {
            indexNews(solrReindex);
        } else {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (company.hasSchema(schemas)) {
                    log.info("Indexing news of company named " + company.getName());
                    solrReindex.setCompanyId(company.getObjectID());
                    transactionHelper.runInANewTransaction(() -> indexNews(solrReindex));
                }
            }
        }
    }

    //    @Transactional
    public void indexNews(SolrReindexRpc solrReindex) {
        ServerSecurityContext.getInstance().setCompanyId(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.NEWS);
        solrDbConsistencyManager.flushAndClear();
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanyNews(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedNewsList = newsManager.getCompanyDeletedNewsFolrSolr(solrReindex);
                solrManager.removeCompanyNewsByIds(deletedNewsList.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error News Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int start = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsNews> newsList = newsManager.getCompanyNewsForSolr(solrReindex, start, limit);
        while (!newsList.isEmpty()) {
            try {
                newsSolrComponent.indexConcurrently(newsList);
            } catch (Exception e) {
                log.error("Error News Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            newsManager.flushAndClear();
            start++;
            newsList = newsManager.getCompanyNewsForSolr(solrReindex, (start * limit), limit);
        }
        newsManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    public void analyzeNewsInconsistencies(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.NEWS);
            companyManager.flushAndClear();
            analyzeNewsSolrDbconsistence(companyID);
            analyzeNewsDbSolrInconsistencies(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemas)) {
                    solrDbConsistencyManager.removeInconsistences(company.getObjectID(), EdsSolrDbConsistency.NEWS);
                    companyManager.flushAndClear();
                    analyzeNewsSolrDbconsistence(company.getObjectID());
                    analyzeNewsDbSolrInconsistencies(company.getObjectID());
                }
            }
        }
    }

    /**
     * ATTENTION!!! DO NOT FORGET CLEAR HIBERNATE CACHE AT THE END OF THIS MEHTOD
     * TO PREVENT DIRTY ENTITY RETRIVAL FROM CACHE OF PREVIOUS COMPANY
     *
     * @param companyID
     */
    private void analyzeNewsSolrDbconsistence(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_NEWS_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrNewsRepresenter.FIELD_NEWS_ID);
        sQuery.addField(SolrNewsRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrNewsRepresenter.FIELD_NEWS_ID).toString());
                    Integer newsid = Integer.valueOf(sd.getFieldValue(SolrNewsRepresenter.FIELD_NEWS_ID).toString());
                    nonExisting.put(newsid, sd);
                }

                List<Integer> newsIdList = newsManager.getUndeletedNewsIdList(ids.toString());
                for (Integer id : newsIdList) {
                    nonExisting.remove(id);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);

            }
            Iterator<Map.Entry<Integer, SolrDocument>> it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrNewsRepresenter.FIELD_NEWS_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.NEWS);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrTaskRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("News with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    /**
     * verifies contacts existing in db with solr
     *
     * @param companyID
     */
    private void analyzeNewsDbSolrInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_NEWS_CORE);
        List<Integer> newsIdList = newsManager.getNewsIdListWithLimit(companyID, startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (newsIdList.size() != 0) {
                nonExisting.addAll(newsIdList);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrNewsRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrNewsRepresenter.FIELD_NEWS_ID + ":(" + ServerUtils.getAsCommoDelimited(newsIdList, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer newsid = Integer.valueOf(sd.getFieldValue(SolrNewsRepresenter.FIELD_NEWS_ID).toString());
                    nonExisting.remove(newsid);
                }
                newsIdList = newsManager.getNewsIdListWithLimit(companyID, newsIdList.get(newsIdList.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.NEWS);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("News with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    public void fixNewsIncosistencies(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixNewsInconsistenciesInSolr(companyID);
            fixNewsInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixNewsInconsistenciesInSolr(company.getObjectID());
                    fixNewsInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    @Override
    @Transactional
    public void fixProjectIncosistencies(Integer companyID) {
        if (companyID == null || companyID == 0) {
            fixCompaniesProjects();
        } else {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
            fixProjectInconsistenciesInSolr(companyID);
            fixProjectInconsistenciesInDB(companyID);
        }

    }

    public void fixCompaniesProjects() {
        List<EdsCompany> companies = companyManager.getCompanies();
        for (EdsCompany company : companies) {
            ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());
            fixProjectInconsistenciesInSolr(company.getObjectID());
            fixProjectInconsistenciesInDB(company.getObjectID());
            companyManager.flushAndClear();
        }
    }

    @Transactional
    public void fixProjectInconsistenciesInDB(Integer companyID) {
        System.out.println("Fixing DB - >Project SOLR inconsistences started for companyID = " + companyID);
        Integer start = 1;
        // first iteratively will fix project inconsistencies in DB
        try {
            while (start != -1) {
                start = fixProjectInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix DB - >Project SOLR inconsistence for companyID = " + companyID);
        }
    }

    private Integer fixProjectInconsistenciesInDb(Integer companyID, Integer start) {
        int limit = 100;
        /// retrives 10 inconsistencies
        List<EdsSolrDbConsistency> contactDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.PROJECT, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, start, limit);
        if (contactDbInconsistencies.size() == 0) {
            return -1; // if there are no inconsistencies
        }
        /// add batch tasks using batch add 100
        boolean firsttime = true;
        StringBuilder sb = new StringBuilder();
        for (EdsSolrDbConsistency sdb : contactDbInconsistencies) {
            if (!firsttime) {
                sb.append(",");
            }
            sb.append(sdb.getEntityID());
            firsttime = false;
        }
        List<EdsProject> projects = projectManager.getProjects(sb.toString());
        try {
            projectSolrComponent.indexes(projects);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (EdsSolrDbConsistency sdb : contactDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed projects of Company ID=" + companyID + " DB inconsistency fileIDs (" + sb + ")");

        EdsSolrDbConsistency lastOne = contactDbInconsistencies.get(contactDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    @Transactional
    public void fixProjectInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR Project - > DB inconsistences started for companyID = " + companyID);
        Integer start = 1;
        // first iteratively will fix project inconsistencies in Solr
        try {
            while (start != -1) {
                start = fixProjectInconsistenciesInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Project SOLR - > DB inconsistence for companyID = " + companyID);
        }

    }

    @Transactional
    public Integer fixProjectInconsistenciesInSolr(Integer companyID, Integer start) {
        int limt = 100;
        //retrives 100 inconsistences
        List<EdsSolrDbConsistency> contactSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.PROJECT, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, start, limt);
        if (contactSolrInconsistencies.size() == 0) {
            return -1;// if there are no inconsistences
        }
        StringBuilder sb = new StringBuilder();

        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrProjectListRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrProjectListRepresenter.FIELD_PROJECT_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_PROJECT_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixeds company=" + companyID + " Project solr inconsistency fileids(" + sb + ")");
        EdsSolrDbConsistency sdb = contactSolrInconsistencies.get(contactSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    @Override
    public void analyzeProjectInconsistencies(Integer companyID) {
        if (companyID == null || companyID == 0) {
            analyzeCompaniesProject();
        } else {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
            analyzeProjectInSolr(companyID);
            analyzeProjectInDataBase(companyID);
        }
    }

    /**
     * All companies projects
     */
    private void analyzeCompaniesProject() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (company.hasSchema(schemas)) {
                ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());
                analyzeProjectInSolr(company.getObjectID());
                analyzeProjectInDataBase(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    /**
     * Analize in Data Base
     *
     * @param companyID
     */
    private void analyzeProjectInDataBase(Integer companyID) {
        EdsCompany company = companyManager.get(companyID);
        Integer start = 1;
        Integer limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_PROJECT_CORE);
        List<Integer> projects = projectManager.getCompanyProjectIdList(companyID, start, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (projects.size() != 0) {
                nonExisting.addAll(projects);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrProjectListRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND "
                        + SolrProjectListRepresenter.FIELD_PROJECT_ID + ":(" + ServerUtils.getAsCommoDelimited(projects, "0", " ") + ")");
                sQuery.add(SolrProjectListRepresenter.FIELD_PROJECT_ID);
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument solrDoc : response.getResults()) {
                    Integer projectId = (Integer) solrDoc.getFieldValue(SolrProjectListRepresenter.FIELD_PROJECT_ID);
                    nonExisting.remove(projectId);
                }

                start = projects.get(projects.size() - 1);
                projects = projectManager.getCompanyProjectIdList(companyID, start, limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer projectIdKey : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(projectIdKey);
                sdb.setEntityType(EdsSolrDbConsistency.PROJECT);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Project with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    /**
     * Analize in Solr
     *
     * @param companyID
     */
    private void analyzeProjectInSolr(Integer companyID) {

        solrDbConsistencyManager.updateOldInconsistency(companyID, EdsSolrDbConsistency.PROJECT);

        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_PROJECT_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 100;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrProjectListRepresenter.FIELD_PROJECT_ID);
        sQuery.addField(SolrProjectListRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                ids = new StringBuffer();
                boolean isNonFirstLoop = false;
                for (SolrDocument solrDoc : resp.getResults()) {
                    if (isNonFirstLoop) {
                        ids.append(",");
                    } else {
                        isNonFirstLoop = true;
                    }
                    Integer projectId = (Integer) solrDoc.getFieldValue(SolrProjectListRepresenter.FIELD_PROJECT_ID);
                    ids.append(projectId);
                    nonExisting.put(projectId, solrDoc);
                }
                List<Integer> projectList = projectManager.getProjectIdList(ids.toString());
                for (Integer projectId : projectList) {
                    nonExisting.remove(projectId);
                }
                start += limit;
                sQuery.setStart(start);
                sQuery.setRows(limit);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer keyDocProjectID : nonExisting.keySet()) {
                flushed = false;
                SolrDocument solrDoc = nonExisting.get(keyDocProjectID);
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(keyDocProjectID);
                sdb.setEntityType(EdsSolrDbConsistency.PROJECT);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(solrDoc.getFieldValue(SolrProjectListRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("Project with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ATTENTION!!! DO NOT FORGET CLEAR HIBERNATE CACHE AT THE END OF THIS MEHTOD
     * TO PREVENT DIRTY ENTITY RETRIVAL FROM CACHE OF PREVIOUS COMPANY
     *
     * @param companyID
     */
    @Transactional
    public void fixNewsInconsistenciesInSolr(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        System.out.println("Fixing News SOLR - > DB inconsistences started for companyID = " + companyID);
        Integer start = 1;
        // first iteratively will fix task inconsistencies in Solr
        try {
            while (start != -1) {
                start = fixNewsInconsistenciesInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix SOLR News - > DB inconsistence for companyID = " + companyID);
        }
        companyManager.flushAndClear();
    }

    @Transactional
    public void fixNewsInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing News DB - > SOLR inconsistences started for companyID = " + companyID);

        Integer start = 1;
        // first iteratively will fix task inconsistencies in DB
        try {
            while (start != -1) {
                start = fixNewsInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix News DB - > SOLR inconsistence for companyID = " + companyID);
        }
        companyManager.flushAndClear();
    }

    private Integer fixNewsInconsistenciesInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limt = 100;
        //retrives 100 inconsistences
        List<EdsSolrDbConsistency> newsSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.NEWS, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limt);
        if (newsSolrInconsistencies.size() == 0) {
            return -1;// if there are no inconsistences
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : newsSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrNewsRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrNewsRepresenter.FIELD_NEWS_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_NEWS_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : newsSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        System.out.println("Fixeds company=" + companyID + " News solr inconsistency newsIds(" + sb + ")");
        EdsSolrDbConsistency sdb = newsSolrInconsistencies.get(newsSolrInconsistencies.size() - 1);
        return sdb.getObjectID();// returns last fixed inconsistency objectID for iterator
    }

    private Integer fixNewsInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        /// retrives 10 inconsistencies
        List<EdsSolrDbConsistency> newsDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.NEWS, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (newsDbInconsistencies.isEmpty()) {
            return -1; // if there are no inconsistencies
        }
        List<Integer> newsIds = newsDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).collect(Collectors.toList());
        List<EdsNews> newsList = newsManager.get(newsIds);
        try {
            newsSolrComponent.indexConcurrently(newsList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : newsDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed news of Company ID=" + companyID + " DB inconsistency newsIDs (" + newsIds + ")");

        EdsSolrDbConsistency lastOne = newsDbInconsistencies.get(newsDbInconsistencies.size() - 1);
        return lastOne.getObjectID(); // returns last fixed inconsistency objectID
    }

    @Transactional
    public void indexCompanyFolders(SolrReindexRpc solrReindex) {
        if (solrReindex.getCompanyId() != null && solrReindex.getCompanyId() != 0) {
            commonServiceLocal.indexCompanyFolders(solrReindex);
        } else {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (company != null && company.hasSchema(schemas)) {
                    solrReindex.setCompanyId(company.getObjectID());
                    commonServiceLocal.indexCompanyFolders(solrReindex);
                }
            }
        }
    }

    @Override
    @Transactional
    public void indexCompanySystemFolders(SolrReindexRpc solrReindex) {
        if (solrReindex.getCompanyId() != null && solrReindex.getCompanyId() != 0) {
            documentsServiceLocal.indexCompanySystemFolders(solrReindex);
        } else {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsCompany company : companys) {
                if (company != null && company.hasSchema(schemas)) {
                    Callable<Void> task = () -> {
                        solrReindex.setCompanyId(company.getObjectID());
                        documentsServiceLocal.indexCompanySystemFolders(solrReindex);
                        return null;
                    };
                    tasks.add(task);
                }
            }
            try {
                List<Future<Void>> results = executor.invokeAll(tasks);
                for (Future<Void> f : results) {
                    try {
                        f.get();
                    } catch (ExecutionException e) {
                        log.error("❌ Task execution failed", e.getCause());
                    }
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void indexCompanyFiles(SolrReindexRpc solrReindex) {
        if (solrReindex.getCompanyId() != null && solrReindex.getCompanyId() != 0) {
            commonServiceLocal.indexFiles(solrReindex);
        } else {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (company != null && company.hasSchema(schemas)) {
                    solrReindex.setCompanyId(company.getObjectID());
                    commonServiceLocal.indexFiles(solrReindex);
                }
            }
        }
    }

    @Transactional
    public void indexSaleInvoice(SolrReindexRpc solrReindex) {
        if (solrReindex.getCompanyId() != null && solrReindex.getCompanyId() != 0) {
            invoiceServiceLocal.saleInvoiceToSolrIndex(solrReindex);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemas)) {
                    solrReindex.setCompanyId(company.getObjectID());
                    transactionHelper.runInANewTransaction(() -> invoiceServiceLocal.saleInvoiceToSolrIndex(solrReindex));
                }
            }
        }
    }

    @Transactional
    public void indexSaleQuote(SolrReindexRpc solrReindex) {
        if (solrReindex.getCompanyId() == 0) {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (schemas.contains(company.getObjectID().toString())) {
                    solrReindex.setCompanyId(company.getObjectID());
                    transactionHelper.runInANewTransaction(() -> quoteServiceLocal.indexCompanySaleQuoteToSolr(solrReindex));
                }
            }
        } else {
            quoteServiceLocal.indexCompanySaleQuoteToSolr(solrReindex);
        }
    }

    @Transactional
    public void indexPurchaseOrder(SolrReindexRpc solrReindex) {
        if (solrReindex.getCompanyId() == 0) {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (schemas.contains(company.getObjectID().toString())) {
                    solrReindex.setCompanyId(company.getObjectID());
                    transactionHelper.runInANewTransaction(() -> quoteServiceLocal.purchaseOrderToSolrIndex(solrReindex));
                }
            }
        } else {
            quoteServiceLocal.purchaseOrderToSolrIndex(solrReindex);
        }
    }

    @Override
    @Transactional
    public void indexOpportunities(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {

            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrDbConsistencyManager.removeInconsistences(nextCompanyID, EdsSolrDbConsistency.OPPORTUNITY);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    solrReindex.setCompanyId(nextCompanyID);
                    transactionHelper.runInANewTransaction(() -> indexCompanyOpportunity(solrReindex));
                }
            }
        }
    }

    @Transactional
    public void indexCompanyOpportunity(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        profileService.clearFromDbDeletedCustomFieldsByFormId(LayoutRPC.OPPORTUNITY_FORM, null, false);
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanyOpportunity(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedOprIds = opportunityManager.getCompanyOpportunityListForSolr(solrReindex);
                solrManager.removeOpportunitiesByIds(deletedOprIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Opportunity Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsOpportunity> opportunities = opportunityManager.getCompanyOpportunityListForSolr(solrReindex, startat, limit);
        while (!opportunities.isEmpty()) {
            try {
                opportunitySolrComponent.indexConcurrently(opportunities);
            } catch (InterruptedException e) {
                log.error("Error Opportunity Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            opportunityManager.flushAndClear();
            startat++;
            opportunities = opportunityManager.getCompanyOpportunityListForSolr(solrReindex, (startat * limit), limit);
        }
        opportunityManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexEvents(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrDbConsistencyManager.removeInconsistences(nextCompanyID, EdsSolrDbConsistency.EVENT);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    solrReindex.setCompanyId(nextCompanyID);
                    transactionHelper.runInANewTransaction(() -> indexCompanyEvent(solrReindex));
                }
            }
        }
    }

    public void indexCompanyEvent(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanyEvents(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedEventIds = eventManager.getCompanyDeletedEventListForSolr(solrReindex);
                solrManager.removeCompanyEventByIds(deletedEventIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Event Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int start = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsEvent> events = eventManager.getCompanyEventListForSolr(solrReindex, start, limit);
        List<Integer> eventIds = events.stream().map(EdsEvent::getObjectID).collect(Collectors.toList());
        Map<Integer, List<EdsRelation>> relationMap = eventIds.stream().collect(Collectors.toMap(eventId -> eventId, event -> relationManager.getAllRelations(EdsRelation.TYPE_EVENT, event)));
        while (!CollectionUtils.isEmpty(events)) {
            try {
                eventSolrComponent.indexConcurrently(events, relationMap);
            } catch (InterruptedException e) {
                log.error("Error Event Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            eventManager.flushAndClear();
            start++;
            events = eventManager.getCompanyEventListForSolr(solrReindex, (start * limit), limit);
            eventIds = events.stream().map(EdsEvent::getObjectID).collect(Collectors.toList());
            relationMap = eventIds.stream().collect(Collectors.toMap(eventId -> eventId, event -> relationManager.getAllRelations(EdsRelation.TYPE_EVENT, event)));
        }
        eventManager.flushAndClear();
//        relationManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexProductsServices(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrReindex.setCompanyId(nextCompanyID);
                solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.PRODUCTS_SERVICES);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanyProductService(solrReindex));
                }
            }
        }
    }

    public void indexCompanyProductService(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
//        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanyProductsServices(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedProductIds = itemManager.getCompanyDeletedItemListForSolr(solrReindex);
                solrManager.removeProductsServicesByIds(deletedProductIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Product Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE; // DO NOT CHANGE THE CHUNK SIZE
        List<EdsItem> productsList = itemManager.getCompanyItemListForSolr(solrReindex, startat, limit);
        while (!productsList.isEmpty()) {
            try {
                productsServicesSolrComponent.indexConcurrently(productsList);
            } catch (InterruptedException e) {
                log.error("Error Product Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            itemManager.flushAndClear();
            startat++;
            productsList = itemManager.getCompanyItemListForSolr(solrReindex, (startat * limit), limit);
        }
        itemManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexCourseSchedule(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrReindex.setCompanyId(nextCompanyID);
                solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.COURSE_SCHEDULE);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanyCourseSchedule(solrReindex));
                }
            }
        }
    }

    public void indexCompanyCourseSchedule(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
//        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanyCourseSchedule(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedCourseSchedules = scheduledCourseManger.getCompanyDeletedCourseScheduleListForSolr(solrReindex);
                solrManager.removeCourseSchedulesByIds(deletedCourseSchedules.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Course Schedule Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        List<EdsCourseSchedule> scheduledCourseList = scheduledCourseManger.getCourseScheduleItemListForSolr(solrReindex, startat, limit);
        while (!scheduledCourseList.isEmpty()) {
            try {
                courseScheduleSolrComponent.indexConcurrently(scheduledCourseList);
            } catch (InterruptedException e) {
                log.error("Error Course Schedule Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            scheduledCourseManger.flushAndClear();
            startat++;
            scheduledCourseList = scheduledCourseManger.getCourseScheduleItemListForSolr(solrReindex, (startat * limit), limit);
        }
        scheduledCourseManger.flushAndClear();
//        companyManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexEmployee(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrReindex.setCompanyId(nextCompanyID);
                solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.EMPLOYEE);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanyEmployee(solrReindex));
                }
            }
        }
    }

    public void indexCompanyEmployee(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanyEmployee(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedEmployees = employeeManager.getCompanyDeletedEmployeeListForSolr(solrReindex);
                solrManager.removeEmployeesByIds(deletedEmployees.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Employee Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsEmployee> employeeList = employeeManager.getEmployeeItemListForSolr(solrReindex, startat, limit);
        while (!employeeList.isEmpty()) {
            try {
                employeeSolrComponent.indexConcurrently(employeeList);
            } catch (SolrServerException | IOException | InterruptedException e) {
                log.error("Error Employee Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            employeeManager.flushAndClear();
            startat++;
            employeeList = employeeManager.getEmployeeItemListForSolr(solrReindex, (startat * limit), limit);
        }
        employeeManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexSinglePayrun(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrReindex.setCompanyId(nextCompanyID);
                solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.SINGLE_PAYRUN);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanySinglePayrun(solrReindex));
                }
            }
        }
    }

    public void indexCompanySinglePayrun(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanySinglePayrun(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedSinglePayruns = payslipTableItemManager.getCompanyDeletedPayslipTableItemListForSolr(solrReindex);
                solrManager.removeSinglePayrunByIds(deletedSinglePayruns.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Single Payrun Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsPayslipTableItem> singlePayrunList = payslipTableItemManager.getPayslipTableItemListForSolr(solrReindex, startat, limit);
        while (!singlePayrunList.isEmpty()) {
            try {
                singlePayrunSolrComponent.indexConcurrently(singlePayrunList);
            } catch (InterruptedException e) {
                log.error("Error Single Payrun Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            payslipTableItemManager.flushAndClear();
//            companyManager.flushAndClear();
            startat++;
            singlePayrunList = payslipTableItemManager.getPayslipTableItemListForSolr(solrReindex, (startat * limit), limit);
        }
        payslipTableItemManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexGroupPayrun(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrReindex.setCompanyId(nextCompanyID);
                solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.GROUP_PAYRUN);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanyGroupPayrun(solrReindex));
                }
            }
        }
    }

    public void indexCompanyGroupPayrun(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanyGroupPayrun(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedGroupPayruns = payslipTableManager.getCompanyDeletedPayslipTableListForSolr(solrReindex);
                solrManager.removeGroupPayrunByIds(deletedGroupPayruns.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Group Payrun Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE; // do not change chunk size

        List<EdsPayslipTable> groupPayrunList = payslipTableManager.getPayslipTableListForSolr(solrReindex, startat, limit);
        while (!groupPayrunList.isEmpty()) {
            try {
                groupPayrunSolrComponent.indexConcurrently(groupPayrunList);
            } catch (InterruptedException e) {
                log.error("Error Group Payrun Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            payslipTableManager.flushAndClear();
            startat++;
            groupPayrunList = payslipTableManager.getPayslipTableListForSolr(solrReindex, (startat * limit), limit);
        }
        payslipTableManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexCashAdvance(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrReindex.setCompanyId(nextCompanyID);
                solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.CASH_ADVANCE);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanyCashAdvance(solrReindex));
                }
            }
        }
    }

    public void indexCompanyCashAdvance(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanyCashAdvance(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedCashAdvances = cashAdvanceManager.getCompanyDeletedCashAdvanceListForSolr(solrReindex);
                solrManager.removeGroupPayrunByIds(deletedCashAdvances.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Cach Advance Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsCashAdvance> cashAdvanceList = cashAdvanceManager.getCashAdvanceListForSolr(solrReindex, startat, limit);
        while (!cashAdvanceList.isEmpty()) {
            try {
                cashAdvanceSolrComponent.indexConcurrently(cashAdvanceList);
            } catch (InterruptedException | SolrServerException | IOException e) {
                log.error("Error Cach Advance Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            cashAdvanceManager.flushAndClear();
//            companyManager.flushAndClear();
            startat++;
            cashAdvanceList = cashAdvanceManager.getCashAdvanceListForSolr(solrReindex, (startat * limit), limit);
        }
        cashAdvanceManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexAdditionalPayment(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrReindex.setCompanyId(nextCompanyID);
                solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.ADDITIONAL_PAYMENT);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanyAdditionalPayment(solrReindex));
                }
            }
        }
    }

    public void indexCompanyAdditionalPayment(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanyAdditionalPayment(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedAdditionalPayments = additionalPaymentManager.getCompanyDeletedAdditionalPaymentListForSolr(solrReindex);
                solrManager.removeGroupPayrunByIds(deletedAdditionalPayments.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Additional Payment. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsAdditionalPayment> additionalPaymentList = additionalPaymentManager.getAdditionalPaymentListForSolr(solrReindex, startat, limit);
        while (Objects.nonNull(additionalPaymentList) && !additionalPaymentList.isEmpty()) {
            try {
                additionalPaymentSolrComponent.indexConcurrently(additionalPaymentList);
            } catch (InterruptedException e) {
                log.error("Error Additional Payment. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            additionalPaymentManager.flushAndClear();
            startat++;
            additionalPaymentList = additionalPaymentManager.getAdditionalPaymentListForSolr(solrReindex, (startat * limit), limit);
        }
        additionalPaymentManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexChartOfAccount(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();

        if (companys != null && !companys.isEmpty()) {
            for (Integer companyId : EdsObject.getObjectIDs(companys)) {
                solrReindex.setCompanyId(companyId);
                solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.CHART_OF_ACCOUNT);
                solrDbConsistencyManager.flushAndClear();

                if (companyId != null && schemas.contains(companyId.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanyChartOfAccount(solrReindex));
                }
            }
        }
    }

    public void indexCompanyChartOfAccount(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanyChartOfAccount(solrReindex.getCompanyId());

            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedAccountIds = accountingManager.getDeletedAccountListForSolr(solrReindex);
                solrManager.removeChartOfAccountByIds(deletedAccountIds.toArray(new Integer[]{}));

            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Chart Of Account Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int start = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsAccount> edsAccountList = accountingManager.getAccountListForSolr(solrReindex, start, limit);

        while (edsAccountList != null && !edsAccountList.isEmpty()) {
            try {
                chartOfAccountSolrComponent.indexConcurrently(edsAccountList);
            } catch (InterruptedException e) {
                log.error("Error Chart Of Account Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            accountingManager.flushAndClear();
            start++;
            edsAccountList = accountingManager.getAccountListForSolr(solrReindex, (start * limit), limit);
        }
        accountingManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexLeaveRequest(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrReindex.setCompanyId(nextCompanyID);
                solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.LEAVE_REQUEST);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanyLeaveRequest(solrReindex));
                }
            }
        }
    }

    public void indexCompanyLeaveRequest(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeLeaveRequests(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> leaveRequesIds = sickRequestManager.getLeaveRequestListForSolr(solrReindex);
                solrManager.removeEmployeesByIds(leaveRequesIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Leave Request Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        availabilityService.indexLeaveRequests(solrReindex);
    }

    @Override
    @Transactional
    public void indexCustomFormItems(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrReindex.setCompanyId(nextCompanyID);
                solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.CUSTOM_FORM);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanyCustomFormItems(solrReindex));
                }
            }
        }
    }

    public void indexCompanyCustomFormItems(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        profileService.clearFromDbDeletedCustomFieldsByFormId(solrReindex.getFormID(), null, false);
        if (StringUtils.isNotBlank(solrReindex.getFormID())) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setForm(solrReindex.getFormID());
            commonServiceLocal.indexCustomFormItems(fp);
        } else {
            try {
                solrManager.removeCustomFormItems(solrReindex.getCompanyId());
            } catch (IOException | SolrServerException e) {
                log.error("Error Custom Form Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            commonServiceLocal.indexCustomFormItems(null);
        }
    }

    @Transactional
    public void indexPurchaseInvoice(SolrReindexRpc solrReindex) {
        if (solrReindex.getCompanyId() == 0) {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (schemas.contains(company.getObjectID().toString())) {
                    solrReindex.setCompanyId(company.getObjectID());
                    transactionHelper.runInANewTransaction(() -> quoteServiceLocal.purchaseInvoiceToSolrIndex(solrReindex));
                }
            }
        } else {
            quoteServiceLocal.purchaseInvoiceToSolrIndex(solrReindex);
        }
    }

    @Override
    @Transactional
    public void indexExpenseReportClaims(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrReindex.setCompanyId(nextCompanyID);
                solrDbConsistencyManager.removeInconsistences(nextCompanyID, EdsSolrDbConsistency.EXPENSE_REPORT_CLAIMS);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanyExpenseReportClaims(solrReindex));
                }
            }
        }
    }

    public void indexCompanyExpenseReportClaims(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanyExpenseReportClaims(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedExRprtIds = expenseReportManager.getCompanyDeletedExpenseReportListForSolr(solrReindex);
                solrManager.removeExpenseReportByIds(deletedExRprtIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Product Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsExpenseReport> reportsList = expenseReportManager.getCompanyExpenseReportListForSolr(solrReindex, startat, limit);
        while (!reportsList.isEmpty()) {
            try {
                expenseReportClaimsSolrComponent.indexConcurrently(reportsList);
            } catch (InterruptedException e) {
                log.error("Error Product Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            expenseReportManager.flushAndClear();
            startat++;
            reportsList = expenseReportManager.getCompanyExpenseReportListForSolr(solrReindex, (startat * limit), limit);
        }
        expenseReportManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexShippingData(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrReindex.setCompanyId(nextCompanyID);
                solrDbConsistencyManager.removeInconsistences(nextCompanyID, EdsSolrDbConsistency.SHIPPING_DATA);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanyShippingData(solrReindex));
                }
            }
        }
    }

    public void indexCompanyShippingData(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeShippingData(null, solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deleteTaskIds = shippingDataManager.getCompanyDeletedShippingDatasForSolr(solrReindex);
                this.solrManager.removeCompanyShippingDatasbyIds(deleteTaskIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Shipping Data Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = 1000;
        List<EdsShippingData> shippingDataList = shippingDataManager.getShippingDataListForSolr(solrReindex, startat, limit);
        while (!shippingDataList.isEmpty()) {
            try {
                shippingDataSolrComponent.indexConcurrently(shippingDataList);
            } catch (InterruptedException e) {
                log.error("Error Shipping Data Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            shippingDataManager.flushAndClear();
            startat++;
            shippingDataList = shippingDataManager.getShippingDataListForSolr(solrReindex, (startat * limit), limit);
        }
        shippingDataManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexCertificates(SolrReindexRpc solrRpc) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrRpc.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrRpc.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrRpc.setCompanyId(nextCompanyID);
                solrDbConsistencyManager.removeInconsistences(nextCompanyID, EdsSolrDbConsistency.CERTIFICATE);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanyCertificate(solrRpc));
                }
            }
        }
    }

    public void indexCompanyCertificate(SolrReindexRpc solrRpc) {
        SecurityContext.setCompanyID(solrRpc.getCompanyId());
//        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrRpc.getCompanyId()));
        try {
            if (solrRpc.isAllReindex()) {
                solrManager.removeCertificate(null, solrRpc.getCompanyId());
            } else if (solrRpc.getLastUpdateTime() != null) {
                List<Integer> deleteTaskIds = certificatemanager.getCompanyDeletedCertificatesForSolr(solrRpc);
                this.solrManager.removeCompanyCertificatesbyIds(deleteTaskIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Certificate Index. Company ID : {} , Message : {} ", solrRpc.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE; // do not chane this limit
        var certificateList = certificatemanager.getCertificatesForSolr(solrRpc, startat, limit);
        while (!certificateList.isEmpty()) {
            try {
                certificateSolrComponent.indexConcurrently(certificateList);
            } catch (Exception e) {
                log.error("Error Certificate Index. Company ID : {} , Message : {} ", solrRpc.getCompanyId(), e.getMessage());
            }
            certificatemanager.flushAndClear();
            startat++;
            certificateList = certificatemanager.getCertificatesForSolr(solrRpc, (startat * limit), limit);
        }
        certificatemanager.flushAndClear();
    }

    @Transactional
    public void indexPositions(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrReindex.setCompanyId(nextCompanyID);
                solrDbConsistencyManager.removeInconsistences(nextCompanyID, EdsSolrDbConsistency.POSITION);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanyPosition(solrReindex));
                }
            }
        }
    }

    public void indexCompanyPosition(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removePosition(null, solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deleteTaskIds = positionManager.getCompanyDeletedPositionsForSolr(solrReindex);
                this.solrManager.removeCompanyPositionsbyIds(deleteTaskIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Position Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsPosition> positionList = positionManager.getPositionsForSolr(solrReindex, startat, limit);
        while (!positionList.isEmpty()) {
            try {
                positionSolrComponent.indexConcurrently(positionList);
            } catch (Exception e) {
                log.error("Error Position Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            positionManager.flushAndClear();
            startat++;
            positionList = positionManager.getPositionsForSolr(solrReindex, (startat * limit), limit);
        }
        positionManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexDepartments(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrReindex.setCompanyId(nextCompanyID);
                solrDbConsistencyManager.removeInconsistences(nextCompanyID, EdsSolrDbConsistency.DEPARTMENT);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanyDepartment(solrReindex));
                }
            }
        }
    }

    public void indexCompanyDepartment(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeDepartment(null, solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deleteTaskIds = departmentManager.getCompanyDeletedDepartmentsForSolr(solrReindex);
                this.solrManager.removeCompanyDepartmentsbyIds(deleteTaskIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Department Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        List<EdsDepartment> departmentList = departmentManager.getDepartmentsForSolr(solrReindex, startat, limit);
        while (!departmentList.isEmpty()) {
            try {
                departmentSolrComponent.indexConcurrently(departmentList);
            } catch (IOException | SolrServerException | InterruptedException e) {
                log.error("Error Department Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            departmentManager.flushAndClear();
            startat++;
            departmentList = departmentManager.getDepartmentsForSolr(solrReindex, (startat * limit), limit);
        }
        departmentManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    @Transactional
    @Override
    public String indexAllCoresOfSelectedCompany(SolrReindexRpc solrReindexRpc) {
        Integer companyId = solrReindexRpc.getCompanyId();
        log.info("@eindex all {} cores has been started", companyId);

        String successfull = "Successfully indexed All Cores";
        StringBuilder errorMessage = new StringBuilder();
        try {
            reindexCompanyTasks(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company task indexing error \n")));
        }
        try {
            indexCompanyLeads(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company Lead indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexCompanyCandidates(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company Candidates indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexCompanyCrmAccounts(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company CRM Accounts indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexCompanyContacts(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company Contacts indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexCompanyNews(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company News indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexSaleInvoice(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company Sales Ivoice indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexCompanyProjects(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company Project indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexCompanyCrmCase(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company CRM Case indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexSaleQuote(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company Sales Quote indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexPurchaseOrder(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company Purchase Order indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexOpportunities(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company Opportunities indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexEvents(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company Events indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexProductsServices(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company ProductServices indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexPurchaseInvoice(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company Purchase Invoice indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexShippingData(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company Shipping Data indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexExpenseReportClaims(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company Expense Report Claims indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexCompanyFiles(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company file indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexCourseBookings(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company Course Booking indexing error \n")));
//            e.printStackTrace();
        }
        try {
            indexCourseSchedule(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Company Course Schedule indexing error")));
//            e.printStackTrace();
        }
        try {
            indexEmployee(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Employee indexing error")));
//            e.printStackTrace();
        }
        try {
            indexSinglePayrun(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Single Payrun indexing error")));
//            e.printStackTrace();
        }
        try {
            indexGroupPayrun(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Group Payrun indexing error")));
//            e.printStackTrace();
        }
        try {
            indexCashAdvance(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Cash Advance indexing error")));
//            e.printStackTrace();
        }
        try {
            indexAdditionalPayment(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Additional Payment indexing error")));
            //            e.printStackTrace();
        }

        try {
            indexVacancy(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Vacnacy indexing error")));
            //            e.printStackTrace();
        }

        try {
            indexEmployeeStep(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Employee Step indexing error")));
            //            e.printStackTrace();
        }

        try {
            indexChartOfAccount(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Employee Step indexing error")));
            //            e.printStackTrace();
        }
        try {
            indexLeaveRequest(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Leave Request indexing error")));
            //            e.printStackTrace();
        }

        try {
            indexCertificates(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Certificate indexing error")));
            //            e.printStackTrace();
        }

        try {
            indexDepartments(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Department indexing error")));
            //            e.printStackTrace();
        }

        try {
            indexPositions(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Certificate indexing error")));
            //            e.printStackTrace();
        }

        try {
            indexCustomFormItems(solrReindexRpc);
        } catch (Exception e) {
            log.error(String.valueOf(errorMessage.append("Custom Form indexing error")));
            //            e.printStackTrace();
        }
        log.info("@Reindex all {} cores has been completed", companyId);

        if (!"".contentEquals(errorMessage)) {
            log.info("@Reindex all {} cores status: FATAL", companyId);
            return errorMessage.toString();
        } else {
            log.info("@Reindex all {} cores status: OK", companyId);
            return successfull;
        }
    }

    @Override
    public ListResult<LayoutRPC> getCustomForms(ListingFilterParameter filterParameter) {
        List<EdsLayout> layoutListResult = layoutManager.list(filterParameter);
        ArrayList<LayoutRPC> result = layoutListResult.stream().map(EdsLayout::getRPC).collect(Collectors.toCollection(ArrayList::new));
        int totalCount = layoutManager.listCount(filterParameter);
        return new ListResult<>(result, totalCount);
    }

    @Override
    @Transactional
    public Integer saveCustomForm(Integer companyID, LayoutRPC formRpc) {
        SecurityContext.setCompanyID(companyID);
        EdsLayout edsLayout = formRpc.getObjectID() != null ? layoutManager.get(companyID, formRpc.getObjectID()) : null;
        if (edsLayout == null) {
            edsLayout = companyID != null ? new EdsCustomLayout() : new EdsDefaultLayout();
        }
        edsLayout.setActive(formRpc.isActive());
        edsLayout.setWebForm(formRpc.isWebForm());
        edsLayout.setLayout(formRpc.getLayout());
        edsLayout.setCustomCss(formRpc.getCustomCss());
        edsLayout.setTitle(formRpc.getTitle());
        edsLayout.setFormID(formRpc.getFormID());
        edsLayout.setAddForm(formRpc.isAddForm());
        edsLayout.setEditForm(formRpc.isEditForm());
        edsLayout.setViewForm(formRpc.isViewForm());
        edsLayout.setImportForm(formRpc.isImportForm());
        edsLayout.setValidations(CustomFormValidation.parse(formRpc.getValidations()));
        layoutManager.createOrUpdate(edsLayout);
        return edsLayout.getObjectID();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LayoutRPC getCustomForm(Integer companyID, Integer customFormID) {
        if (customFormID == null) {
            return new LayoutRPC();
        }
        SecurityContext.setCompanyID(companyID);
        EdsLayout layout = layoutManager.get(companyID, customFormID);
        if (layout != null) {
            LayoutRPC rpc = layout.getRPC();
            if (companyID != null && layout instanceof EdsCustomLayout customLayout) {
                EdsWebForm webForm = webFormManager.getWebFormByCustomLayout(customLayout.getObjectID());
                if (webForm != null) {
                    rpc.setWebFormUrl(EdsContextParams.getHost() + "/" + "WebForms.html?link=" + webForm.getiFrameUrl());
                }
            }
            return rpc;
        }
        return new LayoutRPC();
    }

    @Override
    @Transactional
    public void applyToMultiDBReportTemplate(Integer[] iDs, ArrayList<SelectItem> selectedItems) {
        LinkedHashMap<String, EdsReportTemplate> map = new LinkedHashMap<>();
        for (Integer objectID : iDs) {
            EdsReportTemplate edsReportTemplate = reportTemplateManager.get(objectID);
            edsReportTemplate.setCategoryCode(null);
            edsReportTemplate.setCustomReportTemplates(null);
            map.put(edsReportTemplate.getCode(), edsReportTemplate);
        }
        for (SelectItem selectItem : selectedItems) {
            if (selectItem.getId() == 0) {
                ServerSecurityContext.getInstance().setDatabase(DATABASE_AWS_FREE);
                reportingServiceLocal.exportReportTemplates(map);
            }
            if (selectItem.getId() == 1) {
                ServerSecurityContext.getInstance().setDatabase(DATABASE_AWS_PAID);
                reportingServiceLocal.exportReportTemplates(map);
            }
            if (selectItem.getId() == 2) {
                ServerSecurityContext.getInstance().setDatabase(DATABASE_FREE);
                reportingServiceLocal.exportReportTemplates(map);
            }
            if (selectItem.getId() == 3) {
                ServerSecurityContext.getInstance().setDatabase(DATABASE_PAID);
                reportingServiceLocal.exportReportTemplates(map);
            }
        }
    }

    @Override
    @Transactional
    public void indexCourseBookings(SolrReindexRpc solrReindex) {
        List<EdsCompany> companys = new ArrayList<>();
        if (!solrReindex.getCompanyId().equals(0)) {
            companys.add(companyManager.get(solrReindex.getCompanyId()));
        } else {
            companys = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        if (companys != null && !companys.isEmpty()) {
            for (Integer nextCompanyID : EdsObject.getObjectIDs(companys)) {
                solrReindex.setCompanyId(nextCompanyID);
                solrDbConsistencyManager.removeInconsistences(nextCompanyID, EdsSolrDbConsistency.COURSE_BOOKING);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    transactionHelper.runInANewTransaction(() -> indexCompanyCourseBookings(solrReindex));
                }
            }
        }
    }

    public void indexCompanyCourseBookings(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanyCourseBooking(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deletedCBIds = courseBookingManager.getRejectedCourseBookingForSolr();
                solrManager.removeCourseBookingByIds(deletedCBIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Course Booking Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }

        int start = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsCourseBooking> courseBookingList = courseBookingManager.getCourseBookings(start, limit);
        while (!courseBookingList.isEmpty()) {
            try {
                courseBookingSolrComponent.indexConcurrently(courseBookingList);
            } catch (InterruptedException e) {
                log.error("Error Course Booking Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            courseBookingManager.flushAndClear();
//            companyManager.flushAndClear();
            start++;
            courseBookingList = courseBookingManager.getCourseBookings((start * limit), limit);
        }
        courseBookingManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    @Override
    public void registrationChatUsers(Integer id) {

    }

    @Override
    public String getCompanyStampURL(Integer companyID, String logoType) {
        EdsCompany company = companyManager.getCompany(companyID);
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        return companyAttachmentManager.getCompanyStampUrl(company, logoType);
    }

    @Override
    public String saveStampLogoSize(Integer width, Integer height, Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsInvoicingSettings settings = invoicingSettingsManager.getInvoiceSettings(companyManager.get(companyID));
        if (settings != null) {
            settings.setInvoiceStampWidth(width);
            settings.setInvoiceStampHeight(height);
            return "Invoice PDF Stamp Size saved";
        } else {
            return "There is no settings to save Stamp Size";
        }
    }

    @Override
    public void enabledCompanyPdfStamper(Boolean enabled, Integer companyID) {
        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(companyID);
        if (companySystemSettings != null) {
            companySystemSettings.setEnablePdfStamper(enabled);
        }
    }

    @Override
    public Boolean isPdfStamperEnabled(Integer companyID) {
        Boolean result = true;
        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(companyID);
        if (companySystemSettings != null) {
            result = (companySystemSettings.getEnablePdfStamper() != null ? companySystemSettings.getEnablePdfStamper() : true);
        }
        return result;
    }

    @Override
    @Transactional
    public void indexCompanyProjects(SolrReindexRpc solrRendex) {
        if (solrRendex.getCompanyId() != null && solrRendex.getCompanyId() != 0) {
            projectServiceLocal.indexCompanyProjects(solrRendex);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemas)) {
                    solrRendex.setCompanyId(company.getObjectID());
                    transactionHelper.runInANewTransaction(() -> projectServiceLocal.indexCompanyProjects(solrRendex));
                }
            }
        }
    }

    public void clenupCompanyUsersMembership(Integer companyID) {
        EdsCompany company = companyManager.get(companyID);
        List<EdsUser> employees = userManager.getCompanyUsers(company);
        for (EdsUser user : employees) {
            clenupUserMembership(user.getObjectID());
        }

    }

    public void clenupUserMembership(Integer userID) {

    }

    @Transactional
    public void reindexCompanyTasks(SolrReindexRpc solrReindexRpc) {
        if (solrReindexRpc.getCompanyId() == 0) {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (company.hasSchema(schemas)) {
                    solrReindexRpc.setCompanyId(company.getObjectID());
                    transactionHelper.runInANewTransaction(() -> projectServiceLocal.indexCompanyTasks(solrReindexRpc));
                }
            }
        } else {
            projectServiceLocal.indexCompanyTasks(solrReindexRpc);
        }
    }

    @Transactional
    public void stealContacts(String fileName) {
        rabbitMQService.stealContacts(fileName);
    }

    public void clenupUserMembershipGroupsIncosistency(Integer companyid) {
        if (companyid == null) {
            List<EdsCompany> activeCompanies = companyManager.getCompanies();
            for (EdsCompany company : activeCompanies) {
                cleanUpCompanyGroups(company);
            }
        } else {
            EdsCompany company = companyManager.get(companyid);
            cleanUpCompanyGroups(company);
        }

    }

    private void cleanUpCompanyGroups(EdsCompany company) {

    }

    @Transactional
    public void cleanDublicateTrustees() {
        List<Object[]> dublicateTrustees = taskRbacManager.getDublicatTrustees();
        for (Object[] dub : dublicateTrustees) {
            Integer count = Integer.valueOf(dub[0].toString());
            if (count > 1) {
                Integer trusteeID = Integer.valueOf(dub[1].toString());
                Integer trusteeType = Integer.valueOf(dub[2].toString());
                List<EdsTrustee> trustees = taskRbacManager.getDublicateTrustee(trusteeID, trusteeType);
                updateDublicateTrusteeGroups(trustees);
//                List<EdsGroup> groups = groupManager.getUserGroups()
            }
        }
    }

    private void updateDublicateTrusteeGroups(List<EdsTrustee> trustees) {

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<WFTPlaginListItem> getPlagins(ListingFilterParameter fp) {
        WFTPlaginList plaginList = new WFTPlaginList();
        List<EdsWFTPlagin> plagins = wftPlaginManager.getLastVersionPlagin(fp);
        plaginList.setTotalCount(plagins.size());
        plagins = ListUtils.getSublist(plagins, fp.getStart(), fp.getLimit());
        WFTPlaginListItem[] plaginListItems = new WFTPlaginListItem[plagins.size()];
        int i = 0;
        for (EdsWFTPlagin plagin : plagins) {
            plaginListItems[i] = new WFTPlaginListItem();
            plaginListItems[i].setObjectID(plagin.getObjectID());
            if (EdsWFTPlagin.EXCEL.equals(plagin.getPlagin())) {
                plaginListItems[i].setPlaginName("Excel");
            } else if (EdsWFTPlagin.OUTLOOK.equals(plagin.getPlagin())) {
                plaginListItems[i].setPlaginName("Outlook");
            }
            plaginListItems[i].setPlaginVersion(plagin.getVersion());
            plaginListItems[i].setUpdateDate(plagin.getDate());
            i++;
        }
        plaginList.setPlaginListItems(plaginListItems);
        return new ListResult<WFTPlaginListItem>(new ArrayList<>(Arrays.asList(plaginList.getPlaginListItems())), plaginList.getTotalCount());
    }

    public void updatePlaginItem(WFTPlaginListItem plaginListItem) {
        EdsWFTPlagin plagin = wftPlaginManager.get(plaginListItem.getObjectID() != null ? plaginListItem.getObjectID() : -1);
        if (plagin == null) {
            plagin = new EdsWFTPlagin();
        }
        plagin.setVersion(plaginListItem.getPlaginVersion());
        plagin.setDate(new Date());
        if (plagin.getObjectID() != null) {
            wftPlaginManager.update(plagin);
        } else {
            if ("Excel".equals(plaginListItem.getPlaginName())) {
                plagin.setPlagin(EdsWFTPlagin.EXCEL);
            } else if ("Outlook".equals(plaginListItem.getPlaginName())) {
                plagin.setPlagin(EdsWFTPlagin.OUTLOOK);
            }
            wftPlaginManager.create(plagin);
        }
    }

    /**
     * Related get Contact Privelegies items
     *
     * @param companyID
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactPrivelegiesItem getContactPrivelegiesItem(Integer companyID) {
        EdsCompany company = companyManager.get(companyID);
        ContactPrivelegiesItem privelegiesItem = new ContactPrivelegiesItem();
        privelegiesItem.setCompanyID(company.getObjectID());
        if (company.getCompanySettings() != null) {
            privelegiesItem.setYesOrNo(company.getCompanySettings().isShowPrivateContact());//showPrivateContact item;
        }
        privelegiesItem.setCompanyName(company.getName());//company name;
        String shadowLoginlink = "shadowLogin?id=";
        shadowLoginlink = shadowLoginlink + EncryptionHelper.encryptURL(company.getObjectID().toString());
        privelegiesItem.setCompanyLoginLink(shadowLoginlink);//shadow login.
        return privelegiesItem;
    }

    /**
     * Related save Company Settings show private contacts for ADMIN
     *
     * @param isPrivate
     * @param companyID
     */
    public void saveContactPrivelegies(boolean isPrivate, Integer companyID) {
        EdsCompany company = companyManager.get(companyID);
        if (company != null) {
            company.getCompanySettings().setShowPrivateContact(isPrivate);//update privateContact item true or false;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public MoreMenuUpdateItem getMoreMenuItems(Integer companyID) {
        List<EdsMoreMenuSettings> moreMenuSettings = moreMenuSettingsManager.getMoreMenuSettingItems(companyID);
        EdsCompany company = companyManager.get(companyID);
        EdsCompanySystemSettings systemSettings = companySystemSettingsManager.findByCompanyID(companyID);

        MoreMenuUpdateItem moreMenuUpdateItem = new MoreMenuUpdateItem();
        moreMenuUpdateItem.setCompanyName(company.getName());
        int i = 0;
        SelectItem[] moreMenuItems = new SelectItem[moreMenuSettings.size()];
        for (EdsMoreMenuSettings moreMenu : moreMenuSettings) {
            moreMenuItems[i] = new SelectItem(moreMenu.getObjectID(), moreMenu.getActionName());
            moreMenuItems[i].setDescription(moreMenu.getLinkName() != null ? moreMenu.getLinkName() : "");
            moreMenuItems[i].setNewItem(moreMenu.getEnabled() != null ? moreMenu.getEnabled() : false);
            i++;
        }
        moreMenuUpdateItem.setMoreMenuItems(moreMenuItems);
        if (systemSettings != null) {
            moreMenuUpdateItem.setEnableWFTMoreMenuForMEM(systemSettings.getEnableWFTMoreMenuForMEM() != null ? systemSettings.getEnableWFTMoreMenuForMEM() : true);
            moreMenuUpdateItem.setEnableWFTMoreMenuForADMIN(systemSettings.getEnableWFTMoreMenuForADMIN() != null ? systemSettings.getEnableWFTMoreMenuForADMIN() : true);
        }
        return moreMenuUpdateItem;
    }

    public void saveMoreMenuItems(SelectItem[] selectItems, Integer companyID) {
        if (selectItems != null) {
            for (SelectItem s : selectItems) {
                EdsMoreMenuSettings moreMenu = moreMenuSettingsManager.getMoreMenuSettings(s.getName(), companyID);
                if (moreMenu == null) {
                    moreMenu = new EdsMoreMenuSettings();
                    moreMenu.setActionName(s.getName());
                    moreMenu.setLinkName(s.getDescription());
                    moreMenu.setEnabled(s.isNewItem());
                    moreMenuSettingsManager.create(moreMenu);
                } else {
                    moreMenu.setLinkName(s.getDescription());
                    moreMenu.setEnabled(s.isNewItem());
                    moreMenuSettingsManager.update(moreMenu);
                }
            }
        }
    }

    @Transactional
    public void saveEnableWFTMoreMenu(boolean forMembers, boolean forAdmin, Integer companyID) {
        EdsCompanySystemSettings systemSettings = companySystemSettingsManager.findByCompanyID(companyID);
        if (systemSettings != null) {
            systemSettings.setEnableWFTMoreMenuForMEM(forMembers);
            systemSettings.setEnableWFTMoreMenuForADMIN(forAdmin);
        }
    }

    /**
     * Related company is shown WFT footer in pdf exports
     *
     * @param companyId - companyID
     * @return - item
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactPrivelegiesItem getCompanyShownWFTFooterPDFs(Integer companyId) {
        EdsCompany company = companyManager.get(companyId);
        ContactPrivelegiesItem wftFooter = new ContactPrivelegiesItem();
        wftFooter.setCompanyID(company.getObjectID());
        wftFooter.setCompanyName(company.getName());
        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(companyId);
        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(companyManager.get(companyId));
        wftFooter.setYesOrNo(companySystemSettings != null && companySystemSettings.getShownWFTFooter() != null ? companySystemSettings.getShownWFTFooter() : true);
        wftFooter.setShowOrHide(invoicingSettings != null && invoicingSettings.isShowPDFPoweredBy() != null ? invoicingSettings.isShowPDFPoweredBy() : true);
        wftFooter.setShowEmployeePDFFooter(invoicingSettings != null && invoicingSettings.isShownEmployeeFooter() != null ? invoicingSettings.isShownEmployeeFooter() : true);
        return wftFooter;
    }

    /**
     * Related save Company all pdf's shown WFT footer
     *
     * @param companyItem - company item
     */
    public void saveCompanyIsShownWFTFooter(ContactPrivelegiesItem companyItem) {
        boolean isShownInvoiceWftFooter = companyItem.isShowOrHide();
        boolean isShownOtherWftFooter = companyItem.isYesOrNo();
        boolean isShownEmployeeFooter = companyItem.getShowEmployeePDFFooter();
        Integer companyId = companyItem.getCompanyID();
        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(companyId);
        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(companyManager.get(companyId));

        if (companySystemSettings != null) {
            companySystemSettings.setShownWFTFooter(isShownOtherWftFooter);//update is shown wft footer
        }
        if (invoicingSettings != null) {
            invoicingSettings.setShowPDFPoweredBy(isShownInvoiceWftFooter);
            invoicingSettings.setShownEmployeeFooter(isShownEmployeeFooter);
        }
    }

    /**
     * Related Company Usage Plan lists
     *
     * @param companyID
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SimpleUsagePlanItem[] getUsagePlanListByCompany(Integer companyID) {
        List<EdsUsagePlan> usagePlanList = usagePlanManager.getCompanyAllUsagePlans(companyID);
        SimpleUsagePlanItem[] usagePlanItems = new SimpleUsagePlanItem[usagePlanList.size()];
        int i = 0;
        for (EdsUsagePlan usagePlan : usagePlanList) {
            SimpleUsagePlanItem usagePlanItem = getSimpleUsagePlanItem(usagePlan);
            usagePlanItems[i] = usagePlanItem;
            i++;
        }
        return usagePlanItems;
    }

    /**
     * Related set usage plan items
     *
     * @param usagePlan
     * @return
     */
    private SimpleUsagePlanItem getSimpleUsagePlanItem(EdsUsagePlan usagePlan) {
        SimpleUsagePlanItem usagePlanItem = new SimpleUsagePlanItem();
        usagePlanItem.setObjectID(usagePlan.getObjectID());
        usagePlanItem.setCompanyID(usagePlan.getCompany().getObjectID());
        usagePlanItem.setCompName(usagePlan.getCompany().getName());
        if (usagePlan.getStartDate() != null) {
            usagePlanItem.setStartDate(usagePlan.getStartDate());
        }
        usagePlanItem.setEndDate(usagePlan.getEndDate());
        if (usagePlan.getPayment_StartDate() != null) {
            usagePlanItem.setPaymentStartDate(usagePlan.getPayment_StartDate());
        }
        usagePlanItem.setPaymentEndDate(usagePlan.getPayment_EndDate());
        usagePlanItem.setService(usagePlan.getServiceType() != null ? usagePlan.getServiceType().getName() : "");
        if (usagePlan.getServiceType() != null) {
            usagePlanItem.setServiceTypeId(usagePlan.getServiceType().getObjectID());
        }
        usagePlanItem.setPlanType(usagePlan.getPeriodType() != null ? usagePlan.getPeriodType().getName() : "");
        if (usagePlan.getPeriodType() != null) {
            usagePlanItem.setPeriodTypeId(usagePlan.getPeriodType().getObjectID());
        }
        usagePlanItem.setStatus(usagePlan.getStatus() != null ? usagePlan.getStatus().getName() : "");
        if (usagePlan.getStatus() != null) {
            usagePlanItem.setPaymentStatusId(usagePlan.getStatus().getObjectID());
        }
        usagePlanItem.setUserCount(usagePlan.getUsers());
        usagePlanItem.setNonAccessUserCount(usagePlan.getNoAccessUsers());
        usagePlanItem.setEssUserCount(usagePlan.getEssUsers());
        usagePlanItem.setUsersFree(usagePlan.getUsersFree());
        usagePlanItem.setStorageCount(usagePlan.getStorage());
        usagePlanItem.setStorageFree(usagePlan.getStorageFree());
        usagePlanItem.setTotalAmount(usagePlan.getTotalAmount());
        usagePlanItem.setDiscount(usagePlan.getDiscount());
        usagePlanItem.setTax(usagePlan.getTaxt());
        usagePlanItem.setTotalpayable(usagePlan.getTotalpayable());
        usagePlanItem.setPaid(usagePlan.getPaid());

        UsagePlanItem item = myAccountServiceLocal.getParametr(usagePlan);

        usagePlanItem.setFree(item.isFree());
        usagePlanItem.setUsageMonth(item.getUsageMonth());
        usagePlanItem.setCostDown(item.getCostDown());
        usagePlanItem.setPeriodType(item.getPeriodType());

        usagePlanItem.setPaypalStatus(usagePlan.isPaypalStatus() != null ? usagePlan.isPaypalStatus() : false);
        usagePlanItem.setCurrencyGBP(usagePlan.isCurrencyGBP() != null ? usagePlan.isCurrencyGBP() : false);
        usagePlanItem.setCompanyUk(usagePlan.isUKCompany() != null ? usagePlan.isUKCompany() : false);
        usagePlanItem.setDeleted(usagePlan.isDeleted());
        usagePlanItem.setMobile(usagePlan.isMobile());
        usagePlanItem.setTaskCount(usagePlan.getTaskCount());
        usagePlanItem.setProjectCount(usagePlan.getProjectCount());
        usagePlanItem.setUpgrade(usagePlan.getUpgrade());
        usagePlanItem.setUpgradePayable(usagePlan.getUpgradePayable());
        usagePlanItem.setMessageSended(usagePlan.getMessageSended());
        usagePlanItem.setUserRate(usagePlan.getUserRate());
        usagePlanItem.setCategoryREAL(usagePlan.getCategoryCODE());
        if (usagePlan.getSupportPackageNAME() != null && !"".equals(usagePlan.getSupportPackageNAME())) {
            usagePlanItem.setSupportPackageNAME(usagePlan.getSupportPackageNAME());
            Double supportPackagePricePerHostPerPackage = commonServiceLocal.getSupportPackagePricePerHostPerPackage(EdsContextParams.getHostname(), usagePlan.getSupportPackageNAME());
            usagePlanItem.setSupportPackagePrice(supportPackagePricePerHostPerPackage.floatValue());
        }

        return usagePlanItem;
    }

    /**
     * Related set selected usage plan items
     *
     * @param usagePlanID
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SimpleUsagePlanItem getUsagePlanItem(Integer usagePlanID) {
        EdsUsagePlan usagePlan = usagePlanManager.get(usagePlanID);
        return getSimpleUsagePlanItem(usagePlan);
    }

    /**
     * Related SelectItem  -- Usage Plan Items;
     *
     * @param parentCode -- or == _PERIOD_TYPE, or == _SERVICE_TYPE, or == _PAYMENT_STATUS;
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getItemsByParent(String parentCode) {
        List<EdsReference> references = referenceManager.listReferences(parentCode);
        SelectItem[] result = new SelectItem[references.size()];
        int i = 0;
        for (EdsReference reference : references) {
            result[i] = new SelectItem(reference.getObjectID(), reference.getName(), reference.getCode());
            i++;
        }
        return result;
    }

    /**
     * Related Company current/last usage plan end date;
     *
     * @param companyId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Date getLastUsagePlanEndDate(Integer companyId) {
        EdsUsagePlan usagePlan = usagePlanManager.getLastUsagePlan(companyId);
        if (usagePlan != null) {
            return usagePlan.getEndDate();
        } else {
            return null;
        }
    }

    public void saveUsagePlan(SimpleUsagePlanItem simpleUsagePlanItem) {
        EdsUsagePlan us;
        if (simpleUsagePlanItem.getObjectID() != null) {
            us = usagePlanManager.get(simpleUsagePlanItem.getObjectID());
        } else {
            us = new EdsUsagePlan();
            us.setUnique_guid(UUID.randomUUID().toString());
        }
        EdsCompany edsCompany1 = companyManager.get(simpleUsagePlanItem.getCompanyID());
        EdsReference pendingStatus = referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.PENDING);
        List<EdsUsagePlan> usagePlans = usagePlanManager.getPendingUsagePlans(pendingStatus, edsCompany1);
        for (EdsUsagePlan pendingUp : usagePlans) {
            if (!pendingUp.getObjectID().equals(us.getObjectID())) {
                pendingUp.setDeleted(true);
            }
        }

        us.setCompany(edsCompany1);
        us.setDiscount(simpleUsagePlanItem.getDiscount());
        us.setPaid(simpleUsagePlanItem.isPaid());
        if (simpleUsagePlanItem.getPlanType() != null) {
            EdsReference periodType1 = referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, simpleUsagePlanItem.getPlanType());
            us.setPeriodType(periodType1);
        }

        us.setStartDate(simpleUsagePlanItem.getStartDate());
        us.setEndDate(simpleUsagePlanItem.getEndDate());

        us.setStorage(simpleUsagePlanItem.getStorageCount());
        us.setStorageFree(simpleUsagePlanItem.getStorageFree() != null ? simpleUsagePlanItem.getStorageFree() : Integer.valueOf(1));
        us.setTotalAmount(simpleUsagePlanItem.getTotalAmount());
        us.setCurrencyGBP(simpleUsagePlanItem.isCurrencyGBP());
        us.setUKCompany(simpleUsagePlanItem.isCompanyUk());
        us.setMobile(simpleUsagePlanItem.isMobile());
        us.setPaypalStatus(simpleUsagePlanItem.isPaypalStatus());
        if (simpleUsagePlanItem.getProjectCount() != null) {
            us.setProjectCount(simpleUsagePlanItem.getProjectCount());
        }
        if (simpleUsagePlanItem.getTaskCount() != null) {
            us.setTaskCount(simpleUsagePlanItem.getTaskCount());
        }

        if (simpleUsagePlanItem.getService() != null) {
            EdsReference serviceType1 = referenceManager.findReference(EdsUsagePlan._SERVICE_TYPE, simpleUsagePlanItem.getService());
            us.setServiceType(serviceType1);
        }
        if ((EdsUsagePlan.ACTIVE).equals(simpleUsagePlanItem.getStatus())) {
            EdsReference statusActive = referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.ACTIVE);
            us.setStatus(statusActive);
        } else if ((EdsUsagePlan.PENDING).equals(simpleUsagePlanItem.getStatus())) {
            EdsReference statusPending = referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.PENDING);
            us.setStatus(statusPending);
        } else if ((EdsUsagePlan.EXPIRED).equals(simpleUsagePlanItem.getStatus())) {
            EdsReference statusPending = referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.EXPIRED);
            us.setStatus(statusPending);
        }
        us.setUsers(simpleUsagePlanItem.getUserCount());
        us.setNoAccessUsers(simpleUsagePlanItem.getNonAccessUserCount());
        us.setEssUsers(simpleUsagePlanItem.getEssUserCount());

        if (simpleUsagePlanItem.isMessageSended() != null) {
            us.setMessageSended(simpleUsagePlanItem.isMessageSended());
        }
        if (simpleUsagePlanItem.isDeleted() != null) {
            us.setDeleted(simpleUsagePlanItem.isDeleted());
        }
        if (simpleUsagePlanItem.getUserRate() != null) {
            us.setUserRate(simpleUsagePlanItem.getUserRate());
        }
        if (simpleUsagePlanItem.getCategoryREAL() != null && !"".equals(simpleUsagePlanItem.getCategoryREAL())) {
            us.setCategoryCODE(simpleUsagePlanItem.getCategoryREAL());
        }
        if (simpleUsagePlanItem.getSupportPackageNAME() != null && !"".equals(simpleUsagePlanItem.getSupportPackageNAME())) {
            us.setSupportPackageNAME(simpleUsagePlanItem.getSupportPackageNAME());
        }

        if (us.getObjectID() != null) {
            usagePlanManager.update(us);
        } else {
            usagePlanManager.create(us);
        }

        if (us.getObjectID() != null) {
            us.getCompany().setActive(true);
        }
    }

    public void analyzeTaskSolrDbconsistence(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_TASK_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrTaskRepresenter.FIELD_COMPANY_ID + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrTaskRepresenter.FIELD_TASK_ID);
        sQuery.addField(SolrTaskRepresenter.FIELD_COMPANY_ID);
        sQuery.setParam(GroupParams.GROUP, true);
        sQuery.setParam(GroupParams.GROUP_TOTAL_COUNT, true);
        sQuery.setParam(GroupParams.GROUP_FIELD, SolrTaskRepresenter.FIELD_TASK_ID);

        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            GroupCommand groupCommand = resp.getGroupResponse().getValues().get(0);
            while (!groupCommand.getValues().isEmpty()) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (Group group : groupCommand.getValues()) {
                    SolrDocument sd = group.getResult().get(0);
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrTaskRepresenter.FIELD_TASK_ID).toString());
                    Integer taskid = Integer.valueOf(sd.getFieldValue(SolrTaskRepresenter.FIELD_TASK_ID).toString());
                    nonExisting.put(taskid, sd);

                }
                List<Integer> tasks = taskManager.getTaskIDsByIDs(companyID, ids.toString());
                for (Integer id : tasks) {
                    nonExisting.remove(id);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                groupCommand = resp.getGroupResponse().getValues().get(0);
                companyManager.flushAndClear();
            }
            Iterator<Map.Entry<Integer, SolrDocument>> it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrTaskRepresenter.FIELD_TASK_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.TASK);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrTaskRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("Task with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    public void analyzeTaskInconsistency(Integer companyID) {
        if (companyID == 0) {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (company.hasSchema(schemas)) {
                    solrDbConsistencyManager.removeInconsistences(company.getObjectID(), EdsSolrDbConsistency.TASK);
                    companyManager.flushAndClear();
                    analyzeTaskSolrDbconsistence(company.getObjectID());
                    analyzeTaskDbSolrConsistency(company.getObjectID());
                }
            }
        } else {
            solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.TASK);
            companyManager.flushAndClear();
            analyzeTaskSolrDbconsistence(companyID);
            analyzeTaskDbSolrConsistency(companyID);
        }
    }

    public void analyzeTaskSolrDbconsistence() {
        List<EdsCompany> companys = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companys) {
            if (schemas.contains(company.getObjectID().toString())) {
                solrDbConsistencyManager.removeInconsistences(company.getObjectID(), EdsSolrDbConsistency.TASK);
                companyManager.flushAndClear();
                analyzeTaskSolrDbconsistence(company.getObjectID());
            }
        }
    }

    public void analyzeInvoiceInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeInvoiceSolrInconsistenciesInAllCompanies();
            analayzeInvoiceDbInconsistenciesInAllCompanies();
        } else {
            analyzeInvoiceSolrDbInconsistencies(companyID);
            analyzeInvoiceDbSolrInconsistency(companyID);
        }
    }

    public void analyzeQuoteInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeQuoteSolrInconsistenciesInAllCompanies();
            analayzeQuoteDbInconsistenciesInAllCompanies();
        } else {
            analyzeQuoteSolrDbInconsistencies(companyID);
            analyzeQuoteDbSolrInconsistency(companyID);
        }
    }

    public void analyzePurchaseOrderInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzePurchaseOrderSolrInconsistenciesInAllCompanies();
            analayzePurchaseOrderDbInconsistenciesInAllCompanies();
        } else {
            analyzePurchaseOrderSolrDbInconsistencies(companyID);
            analyzePurchaseOrderDbSolrInconsistency(companyID);
        }
    }

    public void analyzeOpportunityInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeOpportunitySolrInconsistenciesInAllCompanies();
            analayzeOpportunityDbInconsistenciesInAllCompanies();
        } else {
            analyzeOpportunitySolrDbInconsistencies(companyID);
            analyzeOpportunityDbSolrInconsistency(companyID);
        }
    }

    public void analyzeEventInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeEventSolrInconsistenciesInAllCompanies();
            analayzeEventDbInconsistenciesInAllCompanies();
        } else {
            analyzeEventSolrDbInconsistencies(companyID);
            analyzeEventDbSolrInconsistency(companyID);
        }
    }

    public void analyzeProductsServicesInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeProductsServicesSolrInconsistenciesInAllCompanies();
            analayzeProductsServicesDbInconsistenciesInAllCompanies();
        } else {
            analyzeProductsServicesSolrDbInconsistencies(companyID);
            analyzeProductsServicesDbSolrInconsistency(companyID);
        }
    }

    public void analyzeCourseSchedulesInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeCourseSchedulesSolrInconsistenciesInAllCompanies();
            analayzeCourseScheduleDbInconsistenciesInAllCompanies();
        } else {
            analyzeCourseSchedulesSolrDbInconsistencies(companyID);
            analyzeCourseScheduleDbSolrInconsistency(companyID);
        }
    }

    public void analyzeCourseBookingInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeCourseBookingSolrInconsistenciesInAllCompanies();
            analayzeCourseBookingDbInconsistenciesInAllCompanies();
        } else {
            analyzeCourseBookingSolrDbInconsistencies(companyID);
            analyzeCourseBookingDbSolrInconsistency(companyID);
        }
    }

    public void analyzeEmployeeInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeEmployeesSolrInconsistenciesInAllCompanies();
            analayzeEmployeeDbInconsistenciesInAllCompanies();
        } else {
            analyzeEmployeesSolrDbInconsistencies(companyID);
            analyzeEmployeeDbSolrInconsistency(companyID);
        }
    }

    public void analyzeSinglePayrunInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeSinglePayrunsSolrInconsistenciesInAllCompanies();
            analayzeSinglePayrunDbInconsistenciesInAllCompanies();
        } else {
            analyzeSinglePayrunsSolrDbInconsistencies(companyID);
            analyzeSinglePayrunDbSolrInconsistency(companyID);
        }
    }

    public void analyzeGroupPayrunInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeGroupPayrunsSolrInconsistenciesInAllCompanies();
            analayzeGroupPayrunDbInconsistenciesInAllCompanies();
        } else {
            analyzeGroupPayrunsSolrDbInconsistencies(companyID);
            analyzeGroupPayrunDbSolrInconsistency(companyID);
        }
    }

    public void analyzeCashAdvanceInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeCashAdvancesSolrInconsistenciesInAllCompanies();
            analayzeCashAdvanceDbInconsistenciesInAllCompanies();
        } else {
            analyzeCashAdvancesSolrDbInconsistencies(companyID);
            analyzeCashAdvanceDbSolrInconsistency(companyID);
        }
    }

    public void analyzeAdditionalPaymentInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeAdditionalPaymentsSolrInconsistenciesInAllCompanies();
            analayzeAdditionalPaymentDbInconsistenciesInAllCompanies();
        } else {
            analyzeAdditionalPaymentsSolrDbInconsistencies(companyID);
            analyzeAdditionalPaymentDbSolrInconsistency(companyID);
        }
    }

    public void analyzePurchaseInvoiceInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzePurchaseInvoiceSolrInconsistenciesInAllCompanies();
            analayzePurchaseInvoiceDbInconsistenciesInAllCompanies();
        } else {
            analyzePurchaseInvoiceSolrDbInconsistencies(companyID);
            analyzePurchaseInvoiceDbSolrInconsistency(companyID);
        }
    }

    public void analyzeExpenseReportClaimsInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeExpenseReportClaimsSolrInconsistenciesInAllCompanies();
            analayzeExpenseReportClaimsDbInconsistenciesInAllCompanies();
        } else {
            analyzeExpenseReportClaimsSolrDbInconsistencies(companyID);
            analyzeExpenseReportClaimsDbSolrInconsistency(companyID);
        }
    }

    @Override
    public void analyzeChartOfAccountInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeChartOfAccountSolrInconsistenciesInAllCompanies();
            analayzeChartOfAccountDbInconsistenciesInAllCompanies();
        } else {
            analyzeChartOfAccountSolrDbInconsistencies(companyID);
            analyzeChartOfAccountDbSolrInconsistency(companyID);
        }
    }

    @Override
    public void analyzeLeavRequestInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeLeaveRequestSolrInconsistenciesInAllCompanies();
            analayzeLeaveRequestDbInconsistenciesInAllCompanies();
        } else {
            analyzeLeaveRequestSolrDbInconsistencies(companyID);
            analyzeLeaveRequestDbSolrInconsistency(companyID);
        }
    }

    @Override
    public void analyzeCustomFormInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeCustomFormSolrInconsistenciesInAllCompanies();
            analayzeCustomFormDbInconsistenciesInAllCompanies();
        } else {
            analyzeCustomFormSolrDbInconsistencies(companyID);
            analyzeCustomFormDbSolrInconsistency(companyID);
        }
    }

    @Override
    public void analyzeShippingDataInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeShippingDataSolrInconsistenciesInAllCompanies();
            analayzeShippingDataDbInconsistenciesInAllCompanies();
        } else {
            analyzeShippingDataSolrDbInconsistencies(companyID);
            analyzeShippingDataDbSolrInconsistency(companyID);
        }
    }

    @Override
    public void analyzeCertificatesInconsistency(Integer companyId) {
        if (companyId == 0) {
            analayzeCertificateSolrInconsistenciesInAllCompanies();
            analayzeCertificateDbInconsistenciesInAllCompanies();
        } else {
            analyzeCertificateSolrDbInconsistencies(companyId);
            analyzeCertificateDbSolrInconsistency(companyId);
        }
    }

    @Override
    public void analyzePositionsInconsistency(Integer companyId) {
        if (companyId == 0) {
            analayzePositionSolrInconsistenciesInAllCompanies();
            analayzePositionDbInconsistenciesInAllCompanies();
        } else {
            analyzePositionSolrDbInconsistencies(companyId);
            analyzePositionDbSolrInconsistency(companyId);
        }
    }

    @Override
    public void analyzeDepartmentsInconsistency(Integer companyId) {
        if (companyId == 0) {
            analayzeDepartmentSolrInconsistenciesInAllCompanies();
            analayzeDepartmentDbInconsistenciesInAllCompanies();
        } else {
            analyzeDepartmentSolrDbInconsistencies(companyId);
            analyzeDepartmentDbSolrInconsistency(companyId);
        }
    }

    private void analayzeInvoiceSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeInvoiceSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeQuoteSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeQuoteSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzePurchaseOrderSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzePurchaseOrderSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeOpportunitySolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeOpportunitySolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeEventSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeEventSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeProductsServicesSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeProductsServicesSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeCourseSchedulesSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeCourseSchedulesSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeCourseBookingSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeCourseBookingSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeEmployeesSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeEmployeesSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeSinglePayrunsSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeSinglePayrunsSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeGroupPayrunsSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeGroupPayrunsSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeCashAdvancesSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeCashAdvancesSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeAdditionalPaymentsSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeAdditionalPaymentsSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzePurchaseInvoiceSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzePurchaseInvoiceSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeExpenseReportClaimsSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeExpenseReportClaimsSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeChartOfAccountSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeChartOfAccountSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeLeaveRequestSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeLeaveRequestSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeCustomFormSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeCustomFormSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeShippingDataSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeShippingDataSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeCertificateSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeCertificateSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzePositionSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzePositionSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeDepartmentSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeDepartmentSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeInvoiceDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeInvoiceDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeQuoteDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeQuoteDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzePurchaseOrderDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzePurchaseOrderDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeOpportunityDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeOpportunityDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeEventDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeEventDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeProductsServicesDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeProductsServicesDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeCourseScheduleDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeCourseScheduleDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeCourseBookingDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeCourseBookingDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeEmployeeDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeEmployeeDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeSinglePayrunDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeSinglePayrunDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeGroupPayrunDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeGroupPayrunDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeCashAdvanceDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeCashAdvanceDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeAdditionalPaymentDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeAdditionalPaymentDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzePurchaseInvoiceDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzePurchaseInvoiceDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeExpenseReportClaimsDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeExpenseReportClaimsDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    public void analyzeTaskDbSolrConsistency() {
        List<EdsCompany> companys = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companys) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeTaskDbSolrConsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeChartOfAccountDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeChartOfAccountDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeLeaveRequestDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeLeaveRequestDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeCustomFormDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeCustomFormDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeShippingDataDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeShippingDataDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeCertificateDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeCertificateDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzePositionDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzePositionDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeDepartmentDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeDepartmentDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    /**
     * Analyzes task inconsistency in solr for company <br/>
     * if task exist in DB but not exist in solr it will be registered as inconsistency
     * every 100 inconsisteny will be flushed manually to DB
     *
     * @param companyID
     */
    public void analyzeTaskDbSolrConsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_TASK_CORE);
        List<Integer> taskIds = taskManager.getTaskIdsWithLimit(companyID, startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (taskIds.size() != 0) {
                nonExisting.addAll(taskIds);

                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrTaskRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrTaskRepresenter.FIELD_TASK_ID + ":(" + ServerUtils.getAsCommoDelimited(taskIds, "0", " ") + ")");
                sQuery.setParam(GroupParams.GROUP, true);
                sQuery.setParam(GroupParams.GROUP_TOTAL_COUNT, true);
                sQuery.setParam(GroupParams.GROUP_FIELD, SolrTaskRepresenter.FIELD_TASK_ID);

                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                GroupCommand groupCommand = response.getGroupResponse().getValues().get(0);
                for (Group group : groupCommand.getValues()) {
                    SolrDocument sd = group.getResult().get(0);
                    Integer taskid = Integer.valueOf(sd.getFieldValue(SolrTaskRepresenter.FIELD_TASK_ID).toString());
                    nonExisting.remove(taskid);
                }

                taskIds = taskManager.getTaskIdsWithLimit(companyID, taskIds.get(taskIds.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.TASK);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Task with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
//        ServerSecurityContext.getInstance().removeCompanyId();
    }

    /**
     * Manually persists inconsistences
     *
     * @param entityList
     */
    private void batchPersist(List<EdsSolrDbConsistency> entityList) {
        for (EdsSolrDbConsistency sdb : entityList) {
            solrDbConsistencyManager.create(sdb);
        }
        companyManager.flushAndClear();
    }

    /**
     * Fixes task inconsistences in solr if existing tasks in DB are not present in solr <br/>
     * limit is 10 to not overload server and hibernate session
     *
     * @param companyID
     * @param start
     * @return
     */
    public Integer fixTaskInconsistenciesInDb(Integer companyID, Integer start) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        /// retrives 10 inconsistencies
        List<EdsSolrDbConsistency> taskDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.TASK, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, start, limit);
        if (taskDbInconsistencies.isEmpty()) {
            return -1; // if there are no inconsistencies
        }
        String sb = taskDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).map(String::valueOf).collect(Collectors.joining(","));
        List<EdsTask> tasks = taskManager.getUndeletedTasksIn(sb);
        try {
            taskSolrComponent.indexConcurrently(tasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : taskDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed tasks of Company ID=" + companyID + " DB inconsistency tasksIDs (" + sb + ")");

        EdsSolrDbConsistency lastOne = taskDbInconsistencies.get(taskDbInconsistencies.size() - 1);
        return lastOne.getObjectID(); // returns last fixed inconsistency objectID
    }

    /**
     * Fixes task inconsistent references in solr <br/>
     * this method will fix every 100 inconsisteny
     *
     * @param companyID
     * @param startAt
     * @return
     */
    @Transactional
    public Integer fixTaskInconsistenciesInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limt = 100;
        //retrives 100 inconsistences
        List<EdsSolrDbConsistency> taskSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.TASK, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limt);
        if (taskSolrInconsistencies.size() == 0) {
            return -1;// if there are no inconsistences
        }
        StringBuilder sb = new StringBuilder();
        for (EdsSolrDbConsistency sdb : taskSolrInconsistencies) {
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrTaskRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrTaskRepresenter.FIELD_TASK_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_TASK_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : taskSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixeds company=" + companyID + " tasks solr inconsistency tasksids(" + sb + ")");
        EdsSolrDbConsistency sdb = taskSolrInconsistencies.get(taskSolrInconsistencies.size() - 1);
        return sdb.getObjectID();// returns last fixed inconsistency objectID for iterator
    }

    public SolrDbInconsistencyList getInconsistencyStatistic(Integer companyID) {
        List<EdsSolrDbConsistency> inconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID);
        ArrayList<SolrDbInconsistencyItem> items = new ArrayList<>();
        for (EdsSolrDbConsistency sdb : inconsistencies) {
            SolrDbInconsistencyItem item = new SolrDbInconsistencyItem();
            item.setEntityType(sdb.getEntityType());
            item.setEntityName(sdb.getEntityName());
            item.setEntityID(sdb.getEntityID());
            item.setStatisticDate(sdb.getAnalizedate());
            item.setStatus(sdb.getStatus());
            item.setFixed(sdb.isFixed());
            items.add(item);
        }
        SolrDbInconsistencyList list = new SolrDbInconsistencyList();
        list.setTotalCount(inconsistencies.size());
        list.setCompanyID(companyID);
        list.setItems(items);
        return list;
    }

    public SolrInconsistencyList getInconsistencyStatistic(Integer companyID, String entryType) {
        ArrayList<SolrDbInconsistencyItem> items = new ArrayList<>();
        List<EdsCompany> companies = new ArrayList<>();
        if (companyID != null && companyID != 0) {
            companies.add(companyManager.get(companyID));
        } else {
            companies = companyManager.getCompanies();
        }
        if ("ORDER".equalsIgnoreCase(entryType)) {
            entryType = "QUOTE";
        }
        for (EdsCompany company : companies) {
            SolrDbInconsistencyItem item = new SolrDbInconsistencyItem();

            deleteFixedInconsistencies(company.getObjectID());

            List<Long> inconsistenciesSolr = solrDbConsistencyManager.getCompanyInconsistiensCount(company.getObjectID(),
                    entryType.toUpperCase(), EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
            List<Long> inconsistenciesDb = solrDbConsistencyManager.getCompanyInconsistiensCount(company.getObjectID(),
                    entryType.toUpperCase(), EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
            if (inconsistenciesSolr != null || inconsistenciesDb != null) {
                String less = inconsistenciesDb != null ? inconsistenciesDb.get(0).toString() : "0";
                String more = inconsistenciesSolr != null ? inconsistenciesSolr.get(0).toString() : "0";
                item.setLessInSolr("- " + less);
                item.setMoreInSolr("+ " + more);
                item.setEntityType(entryType.toUpperCase());
                item.setCompanyID(company.getObjectID());
                item.setCompanyName(company.getName());
                if (!less.equals("0") || !more.equals("0")) {
                    items.add(item);
                }

            }

        }
        SolrInconsistencyList list = new SolrInconsistencyList();
        list.setTotal(items.size());
        list.setList(items);
        return list;
    }

    public void deleteFixedInconsistencies(Integer companyID) {
        solrDbConsistencyManager.removeFixedInconsistences(companyID);
    }

    public void deleteFixedInconsistencesForAllCompanies() {
        solrDbConsistencyManager.removeFixedInconsistences();
    }

    public void analyzeFileInconsistencies(Integer companyID) {
        if (companyID != 0) {
            analyzeFileSolrDbInconsistencies(companyID);
            analyzeFileDbSolrInconsistencies(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            for (EdsCompany company : companies) {
                analyzeFileSolrDbInconsistencies(company.getObjectID());
                analyzeFileDbSolrInconsistencies(company.getObjectID());
            }
        }
    }

    /**
     * Analizes File inconsistencies, verifies data existing in Solr with DB
     *
     * @param companyID
     */
    @Transactional
    public void analyzeFileSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.updateOldInconsistency(companyID, EdsSolrDbConsistency.FILE);

        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_FOLDER_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 100;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrFolderRepresenter.FIELD_FOLDER_ID);
        sQuery.addField(SolrFolderRepresenter.FIELD_FOLDER_NAME);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (!resp.getResults().isEmpty()) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrFolderRepresenter.FIELD_FOLDER_ID).toString());
                    Integer fileid = Integer.valueOf(sd.getFieldValue(SolrFolderRepresenter.FIELD_FOLDER_ID).toString());
                    nonExisting.put(fileid, sd);

                }
                List<EdsFileHeader> files = fileHeaderManager.getFileIdsIn(ids.toString());
                for (EdsFileHeader file : files) {
                    nonExisting.remove(file.getObjectID());
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);

            }
            Iterator<Map.Entry<Integer, SolrDocument>> it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrFolderRepresenter.FIELD_FOLDER_ID).toString()));
                sdb.setEntityName(sd.getFieldValue(SolrFolderRepresenter.FIELD_FOLDER_NAME).toString());
                sdb.setEntityType(EdsSolrDbConsistency.FILE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(companyID);
                sdb.setAnalizedate(startDate);
                System.out.println("File with name " + sdb.getEntityName() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    /**
     * Analyzes File inconistencies, verifies data existing in DB with Solr
     *
     * @param companyID
     */
    @Transactional
    public void analyzeFileDbSolrInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 100;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_FOLDER_CORE);
        SolrReindexRpc solrReindexRpc = new SolrReindexRpc();
        solrReindexRpc.setCompanyId(companyID);
        List<EdsFileHeader> files = fileHeaderManager.getCompanyFileForSolr(solrReindexRpc, startat, limit);
        Map<Integer, EdsFileHeader> filesMap;
        ArrayList<Integer> nonExisting;
        StringBuffer sb;
        try {
            filesMap = new HashMap<>();
            nonExisting = new ArrayList<>();
            while (!files.isEmpty()) {
                sb = new StringBuffer();
                boolean firstTime = true;
                for (EdsFileHeader file : files) {
                    if (!firstTime) {
                        sb.append(" ");
                    }
                    sb.append(file.getObjectID());
                    firstTime = false;
                    filesMap.put(file.getObjectID(), file);
                    nonExisting.add(file.getObjectID());
                }

                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrFolderRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrFolderRepresenter.FIELD_FOLDER_ID + ":(" + sb + ")");
                sQuery.setRows(limit);
                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer fileid = Integer.valueOf(sd.getFieldValue(SolrFolderRepresenter.FIELD_FOLDER_ID).toString());
                    nonExisting.remove(fileid);
                    filesMap.remove(fileid);
                }

                files = fileHeaderManager.getCompanyFileForSolr(solrReindexRpc, files.get(files.size() - 1).getObjectID(), limit);
            }

            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                EdsFileHeader file = filesMap.get(tId);
                sdb.setEntityID(file.getObjectID());
                sdb.setEntityName(file.getName());
                sdb.setEntityType(EdsSolrDbConsistency.FILE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("File with name -- " + sdb.getEntityName() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    public void fixFileIncosistencies(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixFileInconsistenciesInSolr(companyID);
            fixFileInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schema = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schema)) {
                    fixFileInconsistenciesInSolr(company.getObjectID());
                    fixFileInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    @Transactional
    public void fixFileInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing File SOLR - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        // first iteratively will fix task inconsistencies in Solr
        try {
            while (start != -1) {
                start = fixFileIncosistenciesInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix SOLR File - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixFileInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing File DB - > SOLR inconsistences started for companyID = " + companyID);

        Integer start = 0;
        // first iteratively will fix task inconsistencies in DB
        try {
            while (start != -1) {
                start = fixFileInconsistenciesInDB(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix File DB - > SOLR inconsistence for companyID = " + companyID);
        }
    }

    /**
     * ATTENTION!!! DO NOT FORGET CLEAR HIBERNATE CACHE AT THE END OF THIS MEHTOD
     * TO PREVENT DIRTY ENTITY RETRIVAL FROM CACHE OF PREVIOUS COMPANY
     * fixes inconsistencies in solr for file core
     *
     * @param companyID
     * @param startAt
     * @return
     */
    @Transactional
    public Integer fixFileIncosistenciesInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limt = 100;
        //retrives 100 inconsistences
        List<EdsSolrDbConsistency> fileSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.FILE, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limt);
        if (fileSolrInconsistencies.size() == 0) {
            return -1;// if there are no inconsistences
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : fileSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrFolderRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrFolderRepresenter.FIELD_FOLDER_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_FOLDER_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        folderRbacManager.removeFileEntriesIdsIn(ids.toString(), companyID);
        for (EdsSolrDbConsistency sdb : fileSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixeds company=" + companyID + " file solr inconsistency fileids(" + sb + ")");
        EdsSolrDbConsistency sdb = fileSolrInconsistencies.get(fileSolrInconsistencies.size() - 1);
        return sdb.getObjectID();// returns last fixed inconsistency objectID for iterator
    }

    /**
     * ATTENTION!!! DO NOT FORGET CLEAR HIBERNATE CACHE AT THE END OF THIS MEHTOD
     * TO PREVENT DIRTY ENTITY RETRIVAL FROM CACHE OF PREVIOUS COMPANY
     * Fixes file inconistencies in DB
     *
     * @param companyID
     * @param startAt
     * @return
     */
    @Transactional
    public Integer fixFileInconsistenciesInDB(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        /// retrives 10 inconsistencies
        List<EdsSolrDbConsistency> fileDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.FILE, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (fileDbInconsistencies.isEmpty()) {
            return -1; // if there are no inconsistencies
        }
        var fileIds = fileDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).collect(Collectors.toList());
        List<EdsFileHeader> files = fileHeaderManager.get(fileIds);
        for (EdsFileHeader file : files) {
            folderRbacManager.addFileRbacEntries(file);
        }

        try {
            folderSolrComponent.indexConcurrently(files);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : fileDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed files of Company ID=" + companyID + " DB inconsistency fileIDs (" + fileIds + ")");

        EdsSolrDbConsistency lastOne = fileDbInconsistencies.get(fileDbInconsistencies.size() - 1);
        return lastOne.getObjectID(); // returns last fixed inconsistency objectID
    }

    public void analyzeContactInconsistencies(Integer companyID) {
        if (companyID != 0) {
            solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.CONTACT);
            companyManager.flushAndClear();
            analyzeContactDbSolrInconsistencies(companyID, EdsCrmContact.CRM_CONTACT);
            analyzeContactSolrDbInconsistencies(companyID, EdsCrmContact.CRM_CONTACT);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.CONTACT);
                companyManager.flushAndClear();
                if (company.hasSchema(schemaList)) {
                    analyzeContactDbSolrInconsistencies(company.getObjectID(), EdsCrmContact.CRM_CONTACT);
                    analyzeContactSolrDbInconsistencies(company.getObjectID(), EdsCrmContact.CRM_CONTACT);
                }
            }
        }
    }

    /**
     * verifies contact solr data with db
     *
     * @param companyID
     * @param contactType
     */
    @Transactional
    public void analyzeContactSolrDbInconsistencies(Integer companyID, int contactType) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        boolean isLead = contactType == EdsCrmContact.LEAD_CONTACT;
        boolean isCandidate = contactType == EdsCrmContact.CANDIDATE;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CONTACT_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        StringBuilder query = new StringBuilder(SolrContactRepresenter.FIELD_COMPANY_ID).append(":").append(companyID).append(" AND ");
        if (isLead || isCandidate) {
            query.append("(").append(SolrContactRepresenter.FIELD_CONTACT_TYPE).append(":").append(contactType).append(")");
        } else {
            query.append(" NOT ").append("(").append(SolrContactRepresenter.FIELD_CONTACT_TYPE).append(":").append(EdsCrmContact.LEAD_CONTACT).append(" OR ").append(SolrContactRepresenter.FIELD_CONTACT_TYPE).append(":").append(EdsCrmContact.CANDIDATE).append(")");
        }
        sQuery.setQuery(query.toString());
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrContactRepresenter.FIELD_CONTACT_ID);
        sQuery.addField(SolrContactRepresenter.FIELD_CONTACT_TYPE);
        sQuery.addField(SolrContactRepresenter.FIELD_COMPANY_ID);

        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrContactRepresenter.FIELD_CONTACT_ID).toString());
                    Integer contactid = SolrUtils.asInteger(sd, SolrContactRepresenter.FIELD_CONTACT_ID);
                    nonExisting.put(contactid, sd);
                }
                List<Integer> contacts = isLead ? crmContactManager.getLeadIDsByIDs(ServerUtils.getStringAsList(ids.toString(), ",")) : (isCandidate ? crmContactManager.getCandidateIDsByIDs(ServerUtils.getStringAsList(ids.toString(), ",")) : crmContactManager.getContactIDsByIDs(ServerUtils.getStringAsList(ids.toString(), ",")));
                for (Integer contactId : contacts) {
                    nonExisting.remove(contactId);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator<Map.Entry<Integer, SolrDocument>> it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(SolrUtils.asInteger(sd, SolrContactRepresenter.FIELD_CONTACT_ID));
                sdb.setEntityType(isLead ? EdsSolrDbConsistency.LEAD : (isCandidate ? EdsSolrDbConsistency.CANDIDATE : EdsSolrDbConsistency.CONTACT));
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrContactRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println((isLead ? "Lead" : (isCandidate ? "Candidate" : "Contact")) + " with id " + companyID + " < - > " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    /**
     * verifies contacts existing in db with solr
     *
     * @param companyID
     * @param contactType
     */
    @Transactional
    public void analyzeContactDbSolrInconsistencies(Integer companyID, int contactType) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        boolean isLead = contactType == EdsCrmContact.LEAD_CONTACT;
        boolean isCandidate = contactType == EdsCrmContact.CANDIDATE;
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CONTACT_CORE);
        List<Integer> contactIds = isLead ? crmContactManager.getCompanyLeadIds(companyID, startat, limit) : (isCandidate ? crmContactManager.getCompanyCandidateIds(companyID, startat, limit) : crmContactManager.getCompanyContactIds(companyID, startat, limit));
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (contactIds.size() != 0) {
                SolrQuery sQuery = new SolrQuery();
                StringBuilder query = new StringBuilder(SolrContactRepresenter.FIELD_COMPANY_ID).append(":").append(companyID).append(" AND ");
                if (isLead || isCandidate) {
                    query.append("(").append(SolrContactRepresenter.FIELD_CONTACT_TYPE).append(":").append(contactType).append(")");
                } else {
                    query.append(" NOT ").append("(").append(SolrContactRepresenter.FIELD_CONTACT_TYPE).append(":").append(EdsCrmContact.LEAD_CONTACT).append(" OR ").append(SolrContactRepresenter.FIELD_CONTACT_TYPE).append(":").append(EdsCrmContact.CANDIDATE).append(")");
                }
                query.append(" AND ").append(SolrContactRepresenter.FIELD_CONTACT_ID).append(":(").append(ServerUtils.getAsCommoDelimited(contactIds, "0", " ")).append(")");
                sQuery.setQuery(query.toString());
                sQuery.setRows(limit);
                nonExisting.addAll(contactIds);
                QueryResponse response = server.query(sQuery, SolrRequest.METHOD.POST);
                for (SolrDocument sd : response.getResults()) {
                    Integer contactid = SolrUtils.asInteger(sd, SolrContactRepresenter.FIELD_CONTACT_ID);
                    nonExisting.remove(contactid);
                }
                contactIds = isLead ? crmContactManager.getCompanyLeadIds(companyID, contactIds.get(contactIds.size() - 1), limit) : (isCandidate ? crmContactManager.getCompanyCandidateIds(companyID, contactIds.get(contactIds.size() - 1), limit) : crmContactManager.getCompanyContactIds(companyID, contactIds.get(contactIds.size() - 1), limit));
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(isLead ? EdsSolrDbConsistency.LEAD : (isCandidate ? EdsSolrDbConsistency.CANDIDATE : EdsSolrDbConsistency.CONTACT));
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println((isLead ? "Lead" : (isCandidate ? "Candidate" : "Contact")) + " with id -- " + companyID + " < - > " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    public void fixContactInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixContactInconsistenciesInSolr(companyID, EdsCrmContact.CRM_CONTACT);
            fixContactInconsistenciesInDb(companyID, EdsCrmContact.CRM_CONTACT);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schema = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schema)) {
                    fixContactInconsistenciesInSolr(company.getObjectID(), EdsCrmContact.CRM_CONTACT);
                    fixContactInconsistenciesInDb(company.getObjectID(), EdsCrmContact.CRM_CONTACT);
                }
            }
        }
    }

    @Override
    public void analyzeCrmAccountInconsistencies(Integer companyID) {
        if (companyID != 0) {
            solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.CRM_ACCOUNT);
            companyManager.flushAndClear();
            analyzeCrmAccountDbSolrInconsistencies(companyID);
            analyzeCrmAccountSolrDbInconsistencies(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.CRM_ACCOUNT);
                companyManager.flushAndClear();
                if (company.hasSchema(schemaList)) {
                    analyzeCrmAccountDbSolrInconsistencies(company.getObjectID());
                    analyzeCrmAccountSolrDbInconsistencies(company.getObjectID());
                }
            }
        }
    }

    /**
     * verifies crmaccount solr data with db
     *
     * @param companyID
     */
    @Transactional
    public void analyzeCrmAccountSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CRM_ACCOUNT_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrCrmAccountRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID);
        sQuery.addField(SolrCrmAccountRepresenter.FIELD_COMPANY_ID);

        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(SolrUtils.asInteger(sd, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID));
                    nonExisting.put(SolrUtils.asInteger(sd, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID), sd);
                }
                List<Integer> crmAccountIDs = crmAccountManager.getCrmAccountIDsByIDs(ServerUtils.getStringAsList(ids.toString(), ","));
                for (Integer crmAccountID : crmAccountIDs) {
                    nonExisting.remove(crmAccountID);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator<Map.Entry<Integer, SolrDocument>> it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(SolrUtils.asInteger(sd, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID));
                sdb.setEntityType(EdsSolrDbConsistency.CRM_ACCOUNT);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrCrmAccountRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("CrmAccount with id " + companyID + " < - > " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    /**
     * verifies crmaccounts existing in db with solr
     *
     * @param companyID
     */
    @Transactional
    public void analyzeCrmAccountDbSolrInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CRM_ACCOUNT_CORE);
        List<Integer> crmAccountIDs = crmAccountManager.getCompanyCrmAccountIds(companyID, startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (crmAccountIDs.size() != 0) {
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrCrmAccountRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID + ":(" + ServerUtils.getAsCommoDelimited(crmAccountIDs, "0", " ") + ")");
                sQuery.setRows(limit);
                nonExisting.addAll(crmAccountIDs);
                QueryResponse response = server.query(sQuery);
                if (response.getResults().size() > 0) {
                    for (SolrDocument sd : response.getResults()) {
                        nonExisting.remove(SolrUtils.asInteger(sd, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID));
                    }
                }
                crmAccountIDs = crmAccountManager.getCompanyCrmAccountIds(companyID, crmAccountIDs.get(crmAccountIDs.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.CRM_ACCOUNT);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("CrmAccount with id -- " + companyID + " < - > " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    @Override
    public void fixCrmAccountInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixCrmAccountInconsistenciesInSolr(companyID);
            fixCrmAccountInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schema = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schema)) {
                    fixCrmAccountInconsistenciesInSolr(company.getObjectID());
                    fixCrmAccountInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    @Transactional
    public void fixCrmAccountInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR CrmAccount - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        // first iteratively will fix task inconsistencies in Solr
        try {
            while (start != -1) {
                start = fixCrmAccountInconsistenciesInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix CrmAccount SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixCrmAccountInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing DB - > CrmAccount SOLR inconsistences started for companyID = " + companyID);

        Integer start = 0;
        // first iteratively will fix task inconsistencies in DB
        try {
            while (start != -1) {
                start = fixCrmAccountInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix DB - > CrmAccount SOLR inconsistence for companyID = " + companyID);
        }
    }

    /**
     * ATTENTION!!! DO NOT FORGET CLEAR HIBERNATE CACHE AT THE END OF THIS MEHTOD
     * TO PREVENT DIRTY ENTITY RETRIVAL FROM CACHE OF PREVIOUS COMPANY
     *
     * @param companyID
     * @param startAt
     * @return
     */
    @Transactional
    public Integer fixCrmAccountInconsistenciesInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limt = 100;
        //retrives 100 inconsistences
        List<EdsSolrDbConsistency> contactSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.CRM_ACCOUNT, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limt);
        if (contactSolrInconsistencies.size() == 0) {
            return -1;// if there are no inconsistences
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrCrmAccountRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_CRM_ACCOUNT_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixeds company=" + companyID + " crmaccount solr inconsistency fileids(" + sb + ")");
        EdsSolrDbConsistency sdb = contactSolrInconsistencies.get(contactSolrInconsistencies.size() - 1);
        return sdb.getObjectID();// returns last fixed inconsistency objectID for iterator
    }

    /**
     * ATTENTION!!! DO NOT FORGET CLEAR HIBERNATE CACHE AT THE END OF THIS MEHTOD
     * TO PREVENT DIRTY ENTITY RETRIVAL FROM CACHE OF PREVIOUS COMPANY
     *
     * @param companyID
     * @param startAt
     * @return
     */
    @Transactional
    public Integer fixCrmAccountInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        /// retrives 10 inconsistencies
        List<EdsSolrDbConsistency> dbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.CRM_ACCOUNT, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (dbInconsistencies.isEmpty()) {
            return -1; // if there are no inconsistencies
        }
        /// add batch tasks using batch add 100
        boolean firsttime = true;

        StringBuilder sb = new StringBuilder();
        for (EdsSolrDbConsistency sdb : dbInconsistencies) {
            if (!firsttime) {
                sb.append(",");
            }
            sb.append(sdb.getEntityID());
            firsttime = false;
        }
        List<EdsCrmAccount> crmAccounts = crmAccountManager.getCrmAccountsByIDs(ServerUtils.getStringAsList(sb.toString(), ","));
        try {
            crmAccountSolrComponent.indexConcurrently(crmAccounts);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : dbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed CrmAccount of Company ID=" + companyID + " DB inconsistency fileIDs (" + sb + ")");

        EdsSolrDbConsistency lastOne = dbInconsistencies.get(dbInconsistencies.size() - 1);
        return lastOne.getObjectID(); // returns last fixed inconsistency objectID
    }

    public void fixInvoiceInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixInvoceInconsistenciesInSolr(companyID);
            fixInvoceInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixInvoceInconsistenciesInSolr(company.getObjectID());
                    fixInvoceInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    public void fixQuoteInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixQuoteInconsistenciesInSolr(companyID);
            fixQuoteInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixQuoteInconsistenciesInSolr(company.getObjectID());
                    fixQuoteInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    public void fixPurchaseOrderInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixPurchaseOrderInconsistenciesInSolr(companyID);
            fixPurchaseOrderInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixPurchaseOrderInconsistenciesInSolr(company.getObjectID());
                    fixPurchaseOrderInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    public void fixOpportunityInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixOpportunityInconsistenciesInSolr(companyID);
            fixOpportunityInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixOpportunityInconsistenciesInSolr(company.getObjectID());
                    fixOpportunityInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    public void fixEventInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixEventInconsistenciesInSolr(companyID);
            fixEventInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixEventInconsistenciesInSolr(company.getObjectID());
                    fixEventInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    public void fixProductsServicesInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixProductsServicesInconsistenciesInSolr(companyID);
            fixProductsServicesInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixProductsServicesInconsistenciesInSolr(company.getObjectID());
                    fixProductsServicesInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    public void fixCourseScheduleInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixCourseScheduleInconsistenciesInSolr(companyID);
            fixCourseScheduleInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixCourseScheduleInconsistenciesInSolr(company.getObjectID());
                    fixCourseScheduleInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    public void fixCourseBookingInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixCourseBookingInconsistenciesInSolr(companyID);
            fixCourseBookingInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixCourseBookingInconsistenciesInSolr(company.getObjectID());
                    fixCourseBookingInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    public void fixEmployeeInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixEmployeeInconsistenciesInSolr(companyID);
            fixEmployeeInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixEmployeeInconsistenciesInSolr(company.getObjectID());
                    fixEmployeeInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    public void fixSinglePayrunInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixSinglePayrunInconsistenciesInSolr(companyID);
            fixSinglePayrunInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixSinglePayrunInconsistenciesInSolr(company.getObjectID());
                    fixSinglePayrunInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    public void fixGroupPayrunInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixGroupPayrunInconsistenciesInSolr(companyID);
            fixGroupPayrunInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixGroupPayrunInconsistenciesInSolr(company.getObjectID());
                    fixGroupPayrunInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    public void fixCashAdvanceInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixCashAdvanceInconsistenciesInSolr(companyID);
            fixCashAdvanceInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixCashAdvanceInconsistenciesInSolr(company.getObjectID());
                    fixCashAdvanceInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    public void fixAdditionalPaymentInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixAdditionalPaymentInconsistenciesInSolr(companyID);
            fixAdditionalPaymentInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixAdditionalPaymentInconsistenciesInSolr(company.getObjectID());
                    fixAdditionalPaymentInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    public void fixPurchaseInvoiceInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixPurchaseInvoiceInconsistenciesInSolr(companyID);
            fixPurchaseInvoiceInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixPurchaseInvoiceInconsistenciesInSolr(company.getObjectID());
                    fixPurchaseInvoiceInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    public void fixExpenseReportClaimsInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixExpenseReportClaimsInconsistenciesInSolr(companyID);
            fixExpenseReportClaimsInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixExpenseReportClaimsInconsistenciesInSolr(company.getObjectID());
                    fixExpenseReportClaimsInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    @Override
    public void fixChartOfAccountInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixChartOfAccountInconsistenciesInSolr(companyID);
            fixChartOfAccountInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixChartOfAccountInconsistenciesInSolr(company.getObjectID());
                    fixChartOfAccountInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    @Override
    public void fixLeaveRequestInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixLeaveRequestInconsistenciesInSolr(companyID);
            fixLeaveRequestInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixLeaveRequestInconsistenciesInSolr(company.getObjectID());
                    fixLeaveRequestInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    @Override
    public void fixCustomFormInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixCustomFormInconsistenciesInSolr(companyID);
            fixCustomFormInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixCustomFormInconsistenciesInSolr(company.getObjectID());
                    fixCustomFormInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    @Override
    public void fixShippingDataInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixShippingDataInconsistenciesInSolr(companyID);
            fixShippingDataInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixShippingDataInconsistenciesInSolr(company.getObjectID());
                    fixShippingDataInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    @Override
    public void fixCertificatesInconsistency(Integer companyId) {
        if (!(Integer.valueOf(0)).equals(companyId)) {
            fixCertificateInconsistenciesInSolr(companyId);
            fixCertificateInconsistenciesInDb(companyId);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixCertificateInconsistenciesInSolr(company.getObjectID());
                    fixCertificateInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    @Override
    public void fixPositionsInconsistency(Integer companyId) {
        if (!(Integer.valueOf(0)).equals(companyId)) {
            fixPositionInconsistenciesInSolr(companyId);
            fixPositionInconsistenciesInDb(companyId);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixPositionInconsistenciesInSolr(company.getObjectID());
                    fixPositionInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    @Override
    public void fixDepartmentsInconsistency(Integer companyId) {
        if (!(Integer.valueOf(0)).equals(companyId)) {
            fixDepartmentInconsistenciesInSolr(companyId);
            fixCertificateInconsistenciesInDb(companyId);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixDepartmentInconsistenciesInSolr(company.getObjectID());
                    fixCertificateInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    @Transactional
    public void fixInvoceInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR Invoice - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        // first iteratively will fix task inconsistencies in Solr
        try {
            while (start != -1) {
                start = fixInvoiceInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Invoice SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixQuoteInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR Quote - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        // first iteratively will fix task inconsistencies in Solr
        try {
            while (start != -1) {
                start = fixQuoteInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Quote SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixPurchaseOrderInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR Purchase order - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        // first iteratively will fix task inconsistencies in Solr
        try {
            while (start != -1) {
                start = fixPurchaseOrderInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Purchase Order SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixOpportunityInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR Opportinuty - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixOpportunityInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Opportinuty SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixEventInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR Event - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixEventInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Event SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixProductsServicesInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR Products - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixProductsServicesInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Products SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixCourseScheduleInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR CourseSchedule - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixCourseScheduleInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix courseSchedule SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixCourseBookingInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR CourseBooking - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixCourseBookingInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix courseBooking SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixEmployeeInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR Employee - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixEmployeeInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix employee SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixSinglePayrunInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR SinglePayrun - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixSinglePayrunInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix single payrun SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixGroupPayrunInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR GroupPayrun - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixGroupPayrunInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix group payrun SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixCashAdvanceInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR CashAdvance - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixCashAdvanceInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix cash advance SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixAdditionalPaymentInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR AdditionalPayment - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixAdditionalPaymentInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix additional payment SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixPurchaseInvoiceInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR Purchase Invoice - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixPurchaseInvoiceInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Purchase Invoice SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixExpenseReportClaimsInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR Expense Reports - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixExpenseReportInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Expense Reports SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixInvoceInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR Invoice - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        // first iteratively will fix task inconsistencies in Solr
        try {
            while (start != -1) {
                start = fixInvoiceInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Invoice SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixQuoteInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR Quote - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        // first iteratively will fix task inconsistencies in Solr
        try {
            while (start != -1) {
                start = fixQuoteInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Quote SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixPurchaseOrderInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR Purchase order - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        // first iteratively will fix task inconsistencies in Solr
        try {
            while (start != -1) {
                start = fixPurchaseOrderInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Purchase order SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixOpportunityInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR Opportinuty - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixOpportunityInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Opportinuty SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixEventInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR Event - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixEventInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Event SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixProductsServicesInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR Products - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixProductsServicesInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Products SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixCourseScheduleInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR CourseSchedule - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixCourseSchedulesInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Products SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixCourseBookingInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR CourseBooking - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixCourseBookingInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix CourseBooking SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixEmployeeInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR Employee - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixEmployeesInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Employee SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixSinglePayrunInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR SinglePayrun - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixSinglePayrunsInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix SinglePayrun SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixGroupPayrunInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR GroupPayrun - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixGroupPayrunsInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix GroupPayrun SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixCashAdvanceInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR CashAdvance - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixCashAdvancesInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix CashAdvance SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixAdditionalPaymentInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR AdditionalPayment - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixAdditionalPaymentsInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix CashAdvance SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixPurchaseInvoiceInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR Purchase - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixPurchaseInvoiceInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Purchase SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixExpenseReportClaimsInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR Expense Report - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixExpenseReportInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Expense Report SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixShippingDataInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR Shipping Data - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixGdnGrnInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Shipping Data SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixCertificateInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR Certificate - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixCertificateInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Certificate SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixPositionInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR Position - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixPositionInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Position SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixContactInconsistenciesInSolr(Integer companyID, int contactType) {
        boolean isLead = contactType == EdsCrmContact.LEAD_CONTACT;
        boolean isCandidate = contactType == EdsCrmContact.CANDIDATE;
        System.out.println("Fixing SOLR " + (isLead ? "Lead" : (isCandidate ? "Candidate" : "Contact")) + " - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        // first iteratively will fix task inconsistencies in Solr
        try {
            while (start != -1) {
                start = fixContactInconsistenciesInSolr(companyID, start, contactType);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed fix " + (isLead ? "Lead" : (isCandidate ? "Candidate" : "Contact")) + " SOLR - > DB inconsistence for companyID = " + companyID);
            ex.printStackTrace();
        }
    }

    @Transactional
    public void fixContactInconsistenciesInDb(Integer companyID, int contactType) {
        boolean isLead = contactType == EdsCrmContact.LEAD_CONTACT;
        boolean isCandidate = contactType == EdsCrmContact.CANDIDATE;
        System.out.println("Fixing DB - >" + (isLead ? "Lead" : (isCandidate ? "Candidate" : "Contact")) + " SOLR inconsistences started for companyID = " + companyID);

        Integer start = 0;
        // first iteratively will fix task inconsistencies in DB
        try {
            while (start != -1) {
                start = fixContactInconsistenciesInDb(companyID, start, contactType);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed fix DB - >" + (isLead ? "Lead" : (isCandidate ? "Candidate" : "Contact")) + " SOLR inconsistence for companyID = " + companyID);
            ex.printStackTrace();
        }
    }

    @Transactional
    public void fixChartOfAccountInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR EdsAccount - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixChartOfAccountInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed fix EdsAccount SOLR - > DB inconsistence for companyID = " + companyID);
            ex.printStackTrace();
        }
    }

    public void fixLeaveRequestInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR EdsSickrequest - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixLeaveRequestInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed fix EdsAccount SOLR - > DB inconsistence for companyID = " + companyID);
            ex.printStackTrace();
        }
    }

    public void fixCustomFormInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR EdsCustomFormItems - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixCustomFormInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed fix EdsCustomFormItems SOLR - > DB inconsistence for companyID = " + companyID);
            ex.printStackTrace();
        }
    }

    public void fixShippingDataInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR EdsShippingData - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixShippingDataInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed fix EdsShippingData SOLR - > DB inconsistence for companyID = " + companyID);
            ex.printStackTrace();
        }
    }

    public void fixCertificateInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR EdsCertificateOfEmployment - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixCertificateInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed fix EdsCertificateOfEmployment SOLR - > DB inconsistence for companyID = " + companyID);
            ex.printStackTrace();
        }
    }

    public void fixPositionInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR EdsPosition - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixPositionInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed fix EdsPosition SOLR - > DB inconsistence for companyID = " + companyID);
            ex.printStackTrace();
        }
    }

    public void fixDepartmentInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR EdsDepartment - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixDepartmentInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed fix EdsDepartment SOLR - > DB inconsistence for companyID = " + companyID);
            ex.printStackTrace();
        }
    }

    @Transactional
    public void fixChartOfAccountInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing DB - > EdsAccount SOLR inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixChartOfAccountInconsistencyInDb(companyID, start);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed fix DB - > EdsAccount SOLR inconsistence for companyID = " + companyID);
            ex.printStackTrace();
        }
    }

    public void fixLeaveRequestInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing DB - > EdsSickrequest SOLR inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixLeaveRequestInconsistencyInDb(companyID, start);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed fix DB - > EdsSickrequest SOLR inconsistence for companyID = " + companyID);
            ex.printStackTrace();
        }
    }

    public void fixCustomFormInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing DB - > EdsCustomFormItems SOLR inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixCustomFormInconsistencyInDb(companyID, start);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed fix DB - > EdsCustomFormItems SOLR inconsistence for companyID = " + companyID);
            ex.printStackTrace();
        }
    }

    /**
     * ATTENTION!!! DO NOT FORGET CLEAR HIBERNATE CACHE AT THE END OF THIS MEHTOD
     * TO PREVENT DIRTY ENTITY RETRIVAL FROM CACHE OF PREVIOUS COMPANY
     *
     * @param companyID
     * @param startAt
     * @param contactType
     * @return
     */
    @Transactional
    public Integer fixContactInconsistenciesInSolr(Integer companyID, Integer startAt, Integer contactType) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        boolean isLead = contactType == EdsCrmContact.LEAD_CONTACT;
        boolean isCandidate = contactType == EdsCrmContact.CANDIDATE;
        int limt = 100;
        //retrives 100 inconsistences
        List<EdsSolrDbConsistency> contactSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, (isLead ? EdsSolrDbConsistency.LEAD : (isCandidate ? EdsSolrDbConsistency.CANDIDATE : EdsSolrDbConsistency.CONTACT)), EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limt);
        if (contactSolrInconsistencies.size() == 0) {
            return -1;// if there are no inconsistences
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrContactRepresenter.FIELD_CONTACT_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_CONTACT_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixeds company=" + companyID + " contact solr inconsistency fileids(" + sb + ")");
        EdsSolrDbConsistency sdb = contactSolrInconsistencies.get(contactSolrInconsistencies.size() - 1);
        return sdb.getObjectID();// returns last fixed inconsistency objectID for iterator
    }

    /**
     * ATTENTION!!! DO NOT FORGET CLEAR HIBERNATE CACHE AT THE END OF THIS MEHTOD
     * TO PREVENT DIRTY ENTITY RETRIVAL FROM CACHE OF PREVIOUS COMPANY
     *
     * @param companyID
     * @param startAt
     * @param contactType
     * @return
     */
    @Transactional
    public Integer fixContactInconsistenciesInDb(Integer companyID, Integer startAt, Integer contactType) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        boolean isLead = contactType == EdsCrmContact.LEAD_CONTACT;
        boolean isCandidate = contactType == EdsCrmContact.CANDIDATE;
        int limit = HIBERNATE_CHUNK_SIZE;
        /// retrives 10 inconsistencies
        List<EdsSolrDbConsistency> contactDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, (isLead ? EdsSolrDbConsistency.LEAD : (isCandidate ? EdsSolrDbConsistency.CANDIDATE : EdsSolrDbConsistency.CONTACT)), EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (contactDbInconsistencies.isEmpty()) {
            return -1; // if there are no inconsistencies
        }
        /// add batch tasks using batch add 100
        boolean firsttime = true;

        StringBuilder sb = new StringBuilder();
        for (EdsSolrDbConsistency sdb : contactDbInconsistencies) {
            if (!firsttime) {
                sb.append(",");
            }
            sb.append(sdb.getEntityID());
            firsttime = false;
        }
        List<EdsCrmContact> contacts = isLead ? crmContactManager.getLeadsByIDs(ServerUtils.getStringAsList(sb.toString(), ",")) : (isCandidate ? crmContactManager.getCandidatesByIDs(ServerUtils.getStringAsList(sb.toString(), ",")) : crmContactManager.getContactsByIDs(ServerUtils.getStringAsList(sb.toString(), ",")));
        try {
            contactSolrComponent.indexConcurrently(contacts);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : contactDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed " + (isLead ? "Lead" : (isCandidate ? "Candidate" : "Contact")) + " of Company ID=" + companyID + " DB inconsistency fileIDs (" + sb + ")");

        EdsSolrDbConsistency lastOne = contactDbInconsistencies.get(contactDbInconsistencies.size() - 1);
        return lastOne.getObjectID(); // returns last fixed inconsistency objectID
    }

    public void fixLeadInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixContactInconsistenciesInSolr(companyID, EdsCrmContact.LEAD_CONTACT);
            fixContactInconsistenciesInDb(companyID, EdsCrmContact.LEAD_CONTACT);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schema = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schema)) {
                    fixContactInconsistenciesInSolr(company.getObjectID(), EdsCrmContact.LEAD_CONTACT);
                    fixContactInconsistenciesInDb(company.getObjectID(), EdsCrmContact.LEAD_CONTACT);
                }
            }
        }
    }

    public void fixCandidateInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixContactInconsistenciesInSolr(companyID, EdsCrmContact.CANDIDATE);
            fixContactInconsistenciesInDb(companyID, EdsCrmContact.CANDIDATE);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schema = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schema)) {
                    fixContactInconsistenciesInSolr(company.getObjectID(), EdsCrmContact.CANDIDATE);
                    fixContactInconsistenciesInDb(company.getObjectID(), EdsCrmContact.CANDIDATE);
                }
            }
        }
    }

    public void analyzeLeadInconsistencies(Integer companyID) {
        if (companyID != null && companyID != 0) {
            solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.LEAD);
            analyzeContactSolrDbInconsistencies(companyID, EdsCrmContact.LEAD_CONTACT);
            analyzeContactDbSolrInconsistencies(companyID, EdsCrmContact.LEAD_CONTACT);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    solrDbConsistencyManager.removeInconsistences(company.getObjectID(), EdsSolrDbConsistency.LEAD);
                    analyzeContactSolrDbInconsistencies(company.getObjectID(), EdsCrmContact.LEAD_CONTACT);
                    analyzeContactDbSolrInconsistencies(company.getObjectID(), EdsCrmContact.LEAD_CONTACT);
                }
            }
        }
    }

    public void analyzeCandidateInconsistencies(Integer companyID) {
        if (companyID != null && companyID != 0) {
            solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.CANDIDATE);
            analyzeContactSolrDbInconsistencies(companyID, EdsCrmContact.CANDIDATE);
            analyzeContactDbSolrInconsistencies(companyID, EdsCrmContact.CANDIDATE);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    solrDbConsistencyManager.removeInconsistences(company.getObjectID(), EdsSolrDbConsistency.CANDIDATE);
                    analyzeContactSolrDbInconsistencies(company.getObjectID(), EdsCrmContact.CANDIDATE);
                    analyzeContactDbSolrInconsistencies(company.getObjectID(), EdsCrmContact.CANDIDATE);
                }
            }
        }
    }

    @Transactional
    public void analyzeInvoiceSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.INVOICE);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_SALEINVOICE_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID);
        sQuery.addField(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).toString());
                    Integer invoiceid = Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).toString());
                    nonExisting.put(invoiceid, sd);
                }

                List<Integer> invoiceIdList = invoiceManager.getInvoiceIdsByIDs(ids.toString());
                for (Integer invoiceId : invoiceIdList) {
                    nonExisting.remove(invoiceId);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.INVOICE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("Invoice with  id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    @Transactional
    public void analyzeQuoteSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.QUOTE);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_SALEQUOTE_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID);
        sQuery.addField(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).toString());
                    Integer quoteid = Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).toString());
                    nonExisting.put(quoteid, sd);
                }

                List<Integer> quoteIdList = quoteManager.getQuoteIdsByIDs(ids.toString());
                for (Integer quoteId : quoteIdList) {
                    nonExisting.remove(quoteId);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.QUOTE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("Quote with  id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzePurchaseOrderSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.PURCHASE_ORDER);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_PURCHASE_ORDER_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID);
        sQuery.addField(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).toString());
                    Integer purchaseOrderID = Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).toString());
                    nonExisting.put(purchaseOrderID, sd);
                }

                List<Integer> purchaseOrderIdList = quoteManager.getPurchaseOrderIdsByIDs(ids.toString());
                for (Integer purchaseOrderId : purchaseOrderIdList) {
                    nonExisting.remove(purchaseOrderId);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.PURCHASE_ORDER);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("Purchase Order with  id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeOpportunitySolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.OPPORTUNITY);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_OPPORTUNITY_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID);
        sQuery.addField(SolrOpportunityRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID).toString());
                    Integer opportunityID = Integer.valueOf(sd.getFieldValue(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID).toString());
                    nonExisting.put(opportunityID, sd);
                }

                List<Integer> opportunityIdList = crmServiceLocal.getOpportunityIdsByIDs(ids.toString());
                for (Integer opportunityId : opportunityIdList) {
                    nonExisting.remove(opportunityId);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.OPPORTUNITY);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("Opportunity with  id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeEventSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.EVENT);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_EVENT_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrEventRepresenter.FIELD_EVENT_ID);
        sQuery.addField(SolrEventRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrEventRepresenter.FIELD_EVENT_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrEventRepresenter.FIELD_EVENT_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> evetIdList = crmServiceLocal.getEventIdsByIDs(ids.toString());
                for (Integer eventId : evetIdList) {
                    nonExisting.remove(eventId);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrEventRepresenter.FIELD_EVENT_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.EVENT);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrEventRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("Event with  id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeProductsServicesSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.PRODUCTS_SERVICES);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_PRODUCTS_SERVICES_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrProductServiceRepresenter.FIELD_PRODUCT_ID);
        sQuery.addField(SolrEventRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrProductServiceRepresenter.FIELD_PRODUCT_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrProductServiceRepresenter.FIELD_PRODUCT_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> productsIdList = itemManager.getProductsIDsByIDs(ids.toString());
                for (Integer produtID : productsIdList) {
                    nonExisting.remove(produtID);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrProductServiceRepresenter.FIELD_PRODUCT_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.PRODUCTS_SERVICES);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrEventRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("Product with  id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeCourseSchedulesSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.COURSE_SCHEDULE);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_COURSE_SCHEDULE_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrCourseScheduleRepresenter.FIELD_COURSE_SCHEDULE_ID);
        sQuery.addField(SolrEventRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrCourseScheduleRepresenter.FIELD_COURSE_SCHEDULE_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrCourseScheduleRepresenter.FIELD_COURSE_SCHEDULE_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> courseSchedulesIdList = scheduledCourseManger.getCourseScheduleIDsByIDs(ids.toString());
                for (Integer courseScheduleID : courseSchedulesIdList) {
                    nonExisting.remove(courseScheduleID);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrCourseScheduleRepresenter.FIELD_COURSE_SCHEDULE_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.COURSE_SCHEDULE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrEventRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("Course schedule with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeCourseBookingSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.COURSE_BOOKING);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_COURSE_BOOKING_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrCourseBookingRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrCourseBookingRepresenter.FIELD_COURSE_BOOKING_ID);
        sQuery.addField(SolrCourseBookingRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrCourseBookingRepresenter.FIELD_COURSE_BOOKING_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrCourseBookingRepresenter.FIELD_COURSE_BOOKING_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> courseSchedulesIdList = courseBookingManager.getCourseBookingIDsByIDs(ids.toString());
                for (Integer courseScheduleID : courseSchedulesIdList) {
                    nonExisting.remove(courseScheduleID);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrCourseBookingRepresenter.FIELD_COURSE_BOOKING_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.COURSE_BOOKING);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrCourseBookingRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("Course schedule with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeEmployeesSolrDbInconsistencies(Integer companyID) {
        if (companyID == null) {
            throw new IllegalArgumentException("Company ID cannot be null");
        }
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.EMPLOYEE);
        companyManager.flushAndClear();

        Date startDate = new Date();
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        final int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID);
        sQuery.addField(SolrEmployeeRepresenter.FIELD_COMPANY_ID);

        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuilder ids = new StringBuilder();

        try (SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_EMPLOYEE_CORE)) {
            QueryResponse resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                ids.setLength(0); // Reset the StringBuilder
                for (SolrDocument sd : resp.getResults()) {
                    if (ids.length() > 0) {
                        ids.append(",");
                    }
                    ids.append(sd.getFieldValue(SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> employeeIDList = employeeManager.getEmployeeIDsByIDs(ids.toString());
                for (Integer employeeID : employeeIDList) {
                    nonExisting.remove(employeeID);
                }

                start += limit;
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }

            Iterator<Map.Entry<Integer, SolrDocument>> it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                Map.Entry<Integer, SolrDocument> entry = it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.EMPLOYEE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrEmployeeRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("Employee with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items.clear();
                    flushLimit = 0;
                }
            }
            if (!items.isEmpty()) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            // Consider logging the exception and possibly rethrowing it or handling it accordingly
        }
        companyManager.flushAndClear();
    }

    private void analyzeSinglePayrunsSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.SINGLE_PAYRUN);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_SINGLE_PAYRUN_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrSinglePayrunRepresenter.FIELD_SINGLE_PAYRUN_ID);
        sQuery.addField(SolrSinglePayrunRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrSinglePayrunRepresenter.FIELD_SINGLE_PAYRUN_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrSinglePayrunRepresenter.FIELD_SINGLE_PAYRUN_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> singlePayrunIDList = payslipTableItemManager.getPayslipTableItemIdsByIds(ids.toString());
                for (Integer singlePayrunID : singlePayrunIDList) {
                    nonExisting.remove(singlePayrunID);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrSinglePayrunRepresenter.FIELD_SINGLE_PAYRUN_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.SINGLE_PAYRUN);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrEventRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("SinglePayrun with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeGroupPayrunsSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.GROUP_PAYRUN);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_GROUP_PAYRUN_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrGroupPayrunRepresenter.FIELD_GROUP_PAYRUN_ID);
        sQuery.addField(SolrGroupPayrunRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrGroupPayrunRepresenter.FIELD_GROUP_PAYRUN_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrGroupPayrunRepresenter.FIELD_GROUP_PAYRUN_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> groupPayrunIDList = payslipTableManager.getPayslipTableIdsByIds(ids.toString());
                for (Integer groupPayrunID : groupPayrunIDList) {
                    nonExisting.remove(groupPayrunID);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrGroupPayrunRepresenter.FIELD_GROUP_PAYRUN_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.GROUP_PAYRUN);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrEventRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("GroupPayrun with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeCashAdvancesSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.CASH_ADVANCE);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CASH_ADVANCE_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrCashAdvanceRepresenter.FIELD_CASH_ADVANCE_ID);
        sQuery.addField(SolrCashAdvanceRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrCashAdvanceRepresenter.FIELD_CASH_ADVANCE_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrCashAdvanceRepresenter.FIELD_CASH_ADVANCE_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> cashAdvanceIDList = cashAdvanceManager.getCashAdvanceIdsByIds(ids.toString());
                for (Integer cashAdvanceID : cashAdvanceIDList) {
                    nonExisting.remove(cashAdvanceID);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrCashAdvanceRepresenter.FIELD_CASH_ADVANCE_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.CASH_ADVANCE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrEventRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("CashAdvance with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeAdditionalPaymentsSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.ADDITIONAL_PAYMENT);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_ADDITIONAL_PAYMENT_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrAdditionalPaymentPresenter.FIELD_ADDITIONAL_PAYMENT_ID);
        sQuery.addField(SolrAdditionalPaymentPresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrAdditionalPaymentPresenter.FIELD_ADDITIONAL_PAYMENT_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrAdditionalPaymentPresenter.FIELD_ADDITIONAL_PAYMENT_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> additionalPaymentIDList = additionalPaymentManager.getAdditionalPaymentIdsByIds(ids.toString());
                for (Integer additionalPaymentID : additionalPaymentIDList) {
                    nonExisting.remove(additionalPaymentID);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrAdditionalPaymentPresenter.FIELD_ADDITIONAL_PAYMENT_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.ADDITIONAL_PAYMENT);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrEventRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("AdditionalPayment with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzePurchaseInvoiceSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.PURCHASE_INVOICE);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_PURCHASE_INVOICE_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID);
        sQuery.addField(SolrPurchaseInvoiceRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID).toString());
                    Integer purchaseID = Integer.valueOf(sd.getFieldValue(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID).toString());
                    nonExisting.put(purchaseID, sd);
                }

                List<Integer> purchaseIdList = invoiceManager.getPurchaseInvoiceIdsByIDs(ids.toString());
                for (Integer purchaseID : purchaseIdList) {
                    nonExisting.remove(purchaseID);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.PURCHASE_INVOICE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrPurchaseInvoiceRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("Purchase Invoice with  id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeExpenseReportClaimsSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.EXPENSE_REPORT_CLAIMS);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_EXPENSE_REPORT_CLAIMS_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrExpenseReportRepresenter.FIELD_REPORT_ID);
        sQuery.addField(SolrExpenseReportRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrExpenseReportRepresenter.FIELD_REPORT_ID).toString());
                    Integer reportID = Integer.valueOf(sd.getFieldValue(SolrExpenseReportRepresenter.FIELD_REPORT_ID).toString());
                    nonExisting.put(reportID, sd);
                }

                List<Integer> reportIdList = expenseReportManager.getExpenseReportClaimsIdsByIDs(ids.toString());

                for (Integer reportID : reportIdList) {
                    nonExisting.remove(reportID);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrExpenseReportRepresenter.FIELD_REPORT_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.EXPENSE_REPORT_CLAIMS);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrExpenseReportRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("Expense Report with  id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeInvoiceDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_SALEINVOICE_CORE);
        List<Integer> invoices = invoiceManager.getCompanyInoviceIdsWithLimit(companyID, startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (invoices.size() != 0) {
                nonExisting.addAll(invoices);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":(" + ServerUtils.getAsCommoDelimited(invoices, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer invoiceid = Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).toString());
                    nonExisting.remove(invoiceid);
                }
                invoices = invoiceManager.getCompanyInoviceIdsWithLimit(companyID, invoices.get(invoices.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.INVOICE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Invoice with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeQuoteDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_SALEQUOTE_CORE);

        List<Integer> quotes = quoteManager.getCompanyQuoteIdsWithLimit(companyID, startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (quotes.size() != 0) {
                nonExisting.addAll(quotes);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":(" + ServerUtils.getAsCommoDelimited(quotes, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer quoteid = Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).toString());
                    nonExisting.remove(quoteid);
                }
                quotes = quoteManager.getCompanyQuoteIdsWithLimit(companyID, quotes.get(quotes.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.QUOTE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Quote with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzePurchaseOrderDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_PURCHASE_ORDER_CORE);

        List<Integer> purchaseOrders = quoteManager.getPurchaseOrderIdsWithLimit(startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (purchaseOrders.size() != 0) {
                nonExisting.addAll(purchaseOrders);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":(" + ServerUtils.getAsCommoDelimited(purchaseOrders, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer quoteid = Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).toString());
                    nonExisting.remove(quoteid);
                }
                purchaseOrders = quoteManager.getPurchaseOrderIdsWithLimit(purchaseOrders.get(purchaseOrders.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.QUOTE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Purchase Order with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeOpportunityDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_OPPORTUNITY_CORE);

        List<Integer> opportunity = crmServiceLocal.getOpportunityIdsWithLimit(startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (opportunity.size() != 0) {
                nonExisting.addAll(opportunity);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID + ":(" + ServerUtils.getAsCommoDelimited(opportunity, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer opportunityid = Integer.valueOf(sd.getFieldValue(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID).toString());
                    nonExisting.remove(opportunityid);
                }
                opportunity = crmServiceLocal.getOpportunityIdsWithLimit(opportunity.get(opportunity.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.OPPORTUNITY);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Opportunity with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeEventDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_EVENT_CORE);

        List<Integer> event = crmServiceLocal.getEventIdsWithLimit(startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (event.size() != 0) {
                nonExisting.addAll(event);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrEventRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrEventRepresenter.FIELD_EVENT_ID + ":(" + ServerUtils.getAsCommoDelimited(event, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer eventid = Integer.valueOf(sd.getFieldValue(SolrEventRepresenter.FIELD_EVENT_ID).toString());
                    nonExisting.remove(eventid);
                }
                event = crmServiceLocal.getEventIdsWithLimit(event.get(event.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.EVENT);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Event with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeProductsServicesDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_PRODUCTS_SERVICES_CORE);

        List<Integer> products = itemManager.getProductsServicesIdsWithLimit(startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (products.size() != 0) {
                nonExisting.addAll(products);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrEventRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrProductServiceRepresenter.FIELD_PRODUCT_ID + ":(" + ServerUtils.getAsCommoDelimited(products, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer productid = Integer.valueOf(sd.getFieldValue(SolrProductServiceRepresenter.FIELD_PRODUCT_ID).toString());
                    nonExisting.remove(productid);
                }
                products = itemManager.getProductsServicesIdsWithLimit(products.get(products.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.PRODUCTS_SERVICES);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Product with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeCourseScheduleDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_COURSE_SCHEDULE_CORE);

        List<Integer> courseSchedules = scheduledCourseManger.getCourseScheduleIdsWithLimit(startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (courseSchedules.size() != 0) {
                nonExisting.addAll(courseSchedules);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrEventRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND "
                        + SolrCourseScheduleRepresenter.FIELD_COURSE_SCHEDULE_ID + ":(" + ServerUtils.getAsCommoDelimited(courseSchedules, "0", " ") + ")");

                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer courseScheduleId = Integer.valueOf(sd.getFieldValue(SolrCourseScheduleRepresenter.FIELD_COURSE_SCHEDULE_ID).toString());
                    nonExisting.remove(courseScheduleId);
                }
                courseSchedules = scheduledCourseManger.getCourseScheduleIdsWithLimit(courseSchedules.get(courseSchedules.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.COURSE_SCHEDULE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Course schedule with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeCourseBookingDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_COURSE_BOOKING_CORE);

        List<Integer> courseSchedules = courseBookingManager.getCourseBookingIdsWithLimit(startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (courseSchedules.size() != 0) {
                nonExisting.addAll(courseSchedules);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrCourseBookingRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND "
                        + SolrCourseBookingRepresenter.FIELD_COURSE_BOOKING_ID + ":(" + ServerUtils.getAsCommoDelimited(courseSchedules, "0", " ") + ")");

                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer courseScheduleId = Integer.valueOf(sd.getFieldValue(SolrCourseBookingRepresenter.FIELD_COURSE_BOOKING_ID).toString());
                    nonExisting.remove(courseScheduleId);
                }
                courseSchedules = courseBookingManager.getCourseBookingIdsWithLimit(courseSchedules.get(courseSchedules.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.COURSE_SCHEDULE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Course booking with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeEmployeeDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_EMPLOYEE_CORE);

        List<Integer> employees = employeeManager.getEmployeeIDsWithLimit(startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (employees.size() != 0) {
                nonExisting.addAll(employees);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrEmployeeRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND "
                        + SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID + ":(" + ServerUtils.getAsCommoDelimited(employees, "0", " ") + ")");

                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer employeeId = Integer.valueOf(sd.getFieldValue(SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID).toString());
                    nonExisting.remove(employeeId);
                }
                employees = employeeManager.getEmployeeIDsWithLimit(employees.get(employees.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.EMPLOYEE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Employee with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeSinglePayrunDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_SINGLE_PAYRUN_CORE);

        List<Integer> singlePayruns = payslipTableItemManager.getPayslipTableItemIdsWithLimit(startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (singlePayruns.size() != 0) {
                nonExisting.addAll(singlePayruns);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrSinglePayrunRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND "
                        + SolrSinglePayrunRepresenter.FIELD_SINGLE_PAYRUN_ID + ":(" + ServerUtils.getAsCommoDelimited(singlePayruns, "0", " ") + ")");

                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer singlePayrunId = Integer.valueOf(sd.getFieldValue(SolrSinglePayrunRepresenter.FIELD_SINGLE_PAYRUN_ID).toString());
                    nonExisting.remove(singlePayrunId);
                }
                singlePayruns = payslipTableItemManager.getPayslipTableItemIdsWithLimit(singlePayruns.get(singlePayruns.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.SINGLE_PAYRUN);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("SinglePayrun with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeGroupPayrunDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_GROUP_PAYRUN_CORE);

        List<Integer> groupPayruns = payslipTableManager.getPayslipTableIdsWithLimit(startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (groupPayruns.size() != 0) {
                nonExisting.addAll(groupPayruns);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrGroupPayrunRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND "
                        + SolrGroupPayrunRepresenter.FIELD_GROUP_PAYRUN_ID + ":(" + ServerUtils.getAsCommoDelimited(groupPayruns, "0", " ") + ")");

                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer groupPayrunId = Integer.valueOf(sd.getFieldValue(SolrGroupPayrunRepresenter.FIELD_GROUP_PAYRUN_ID).toString());
                    nonExisting.remove(groupPayrunId);
                }
                groupPayruns = payslipTableManager.getPayslipTableIdsWithLimit(groupPayruns.get(groupPayruns.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.GROUP_PAYRUN);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("GroupPayrun with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeCashAdvanceDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CASH_ADVANCE_CORE);

        List<Integer> cashAdvances = cashAdvanceManager.getCashAdvanceIdsWithLimit(startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (cashAdvances.size() != 0) {
                nonExisting.addAll(cashAdvances);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrCashAdvanceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND "
                        + SolrCashAdvanceRepresenter.FIELD_CASH_ADVANCE_ID + ":(" + ServerUtils.getAsCommoDelimited(cashAdvances, "0", " ") + ")");

                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer cashAdvanceId = Integer.valueOf(sd.getFieldValue(SolrCashAdvanceRepresenter.FIELD_CASH_ADVANCE_ID).toString());
                    nonExisting.remove(cashAdvanceId);
                }
                cashAdvances = cashAdvanceManager.getCashAdvanceIdsWithLimit(cashAdvances.get(cashAdvances.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.CASH_ADVANCE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("CashAdvance with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeAdditionalPaymentDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_ADDITIONAL_PAYMENT_CORE);

        List<Integer> additionalPayments = additionalPaymentManager.getAdditionalPaymentIdsWithLimit(startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (additionalPayments.size() != 0) {
                nonExisting.addAll(additionalPayments);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrAdditionalPaymentPresenter.FIELD_COMPANY_ID + ":" + companyID + " AND "
                        + SolrAdditionalPaymentPresenter.FIELD_ADDITIONAL_PAYMENT_ID + ":(" + ServerUtils.getAsCommoDelimited(additionalPayments, "0", " ") + ")");

                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer additionalPaymentId = Integer.valueOf(sd.getFieldValue(SolrAdditionalPaymentPresenter.FIELD_ADDITIONAL_PAYMENT_ID).toString());
                    nonExisting.remove(additionalPaymentId);
                }
                additionalPayments = additionalPaymentManager.getAdditionalPaymentIdsWithLimit(additionalPayments.get(additionalPayments.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.ADDITIONAL_PAYMENT);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("AdditionalPayment with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzePurchaseInvoiceDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_PURCHASE_INVOICE_CORE);

        List<Integer> purchase = invoiceManager.getPurchaseInvoiceIdsWithLimit(startat, limit);

        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (purchase.size() != 0) {
                nonExisting.addAll(purchase);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrPurchaseInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID + ":(" + ServerUtils.getAsCommoDelimited(purchase, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer purchaseid = Integer.valueOf(sd.getFieldValue(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID).toString());
                    nonExisting.remove(purchaseid);
                }
                purchase = invoiceManager.getPurchaseInvoiceIdsWithLimit(purchase.get(purchase.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.PURCHASE_INVOICE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Purchase Invoice with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeExpenseReportClaimsDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_EXPENSE_REPORT_CLAIMS_CORE);

        List<Integer> reports = expenseReportManager.getExpenseReportClaimsIdsWithLimit(startat, limit);

        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (reports.size() != 0) {
                nonExisting.addAll(reports);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrExpenseReportRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrExpenseReportRepresenter.FIELD_REPORT_ID + ":(" + ServerUtils.getAsCommoDelimited(reports, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer reportId = Integer.valueOf(sd.getFieldValue(SolrExpenseReportRepresenter.FIELD_REPORT_ID).toString());
                    nonExisting.remove(reportId);
                }
                reports = expenseReportManager.getExpenseReportClaimsIdsWithLimit(reports.get(reports.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.EXPENSE_REPORT_CLAIMS);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Expense Reports with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeShippingDataDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_SHIPPING_DATA_CORE);

        List<Integer> shippingDataIds = shippingDataManager.getShippingDataIdsWithLimit(startat, limit);

        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (shippingDataIds.size() != 0) {
                nonExisting.addAll(shippingDataIds);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_ID + ":(" + ServerUtils.getAsCommoDelimited(shippingDataIds, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer reportId = Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_ID).toString());
                    nonExisting.remove(reportId);
                }
                startat++;
                shippingDataIds = shippingDataManager.getShippingDataIdsWithLimit(startat * limit, limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.SHIPPING_DATA);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Shipping Data with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeCertificateDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CERTIFICATE_CORE);

        List<Integer> certificateIds = certificatemanager.getCertificateIdsWithLimit(startat, limit);

        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (certificateIds.size() != 0) {
                nonExisting.addAll(certificateIds);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrCertificateRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrCertificateRepresenter.FIELD_CERTIFICATE_ID + ":(" + ServerUtils.getAsCommoDelimited(certificateIds, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer reportId = Integer.valueOf(sd.getFieldValue(SolrCertificateRepresenter.FIELD_CERTIFICATE_ID).toString());
                    nonExisting.remove(reportId);
                }
                startat++;
                certificateIds = certificatemanager.getCertificateIdsWithLimit(startat * limit, limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.CERTIFICATE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Certificate with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzePositionDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_POSITION_CORE);

        List<Integer> positionIds = positionManager.getPositionIdsWithLimit(startat, limit);

        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (positionIds.size() != 0) {
                nonExisting.addAll(positionIds);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrCertificateRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrPositionRepresenter.FIELD_POSITION_ID + ":(" + ServerUtils.getAsCommoDelimited(positionIds, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer reportId = Integer.valueOf(sd.getFieldValue(SolrPositionRepresenter.FIELD_POSITION_ID).toString());
                    nonExisting.remove(reportId);
                }
                startat++;
                positionIds = positionManager.getPositionIdsWithLimit(startat * limit, limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.CERTIFICATE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Position with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeDepartmentDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_DEPARTMENT_CORE);

        List<Integer> departmentIds = departmentManager.getDepartmentIdsWithLimit(startat, limit);

        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (departmentIds.size() != 0) {
                nonExisting.addAll(departmentIds);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrCertificateRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrDepartmentRepresenter.FIELD_DEPARTMENT_ID + ":(" + ServerUtils.getAsCommoDelimited(departmentIds, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer reportId = Integer.valueOf(sd.getFieldValue(SolrDepartmentRepresenter.FIELD_DEPARTMENT_ID).toString());
                    nonExisting.remove(reportId);
                }
                startat++;
                departmentIds = departmentManager.getDepartmentIdsWithLimit(startat * limit, limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.DEPARTMENT);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Department with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeChartOfAccountDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CHART_OF_ACCOUNT_CORE);

        List<Integer> edsAccountIds = accountingManager.getAccountIdsWithLimit(startat, limit);
        ArrayList<Integer> nonExisting = Lists.newArrayList();
        try {
            while (edsAccountIds.size() != 0) {
                nonExisting.addAll(edsAccountIds);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrChartOfAccountRepresenter.FIELD_COMPANY_ID + ":" + companyID +
                        " AND " + SolrChartOfAccountRepresenter.FIELD_ACCOUNT_ID
                        + ":(" + ServerUtils.getAsCommoDelimited(edsAccountIds, "0", " ") + ")");

                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer edsAccountId = Integer.valueOf(sd.getFieldValue(SolrChartOfAccountRepresenter.FIELD_ACCOUNT_ID).toString());
                    nonExisting.remove(edsAccountId);
                }
                edsAccountIds = accountingManager.getAccountIdsWithLimit(edsAccountIds.get(edsAccountIds.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.CHART_OF_ACCOUNT);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("EdsAccount with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeLeaveRequestDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_LEAVE_REQUEST_CORE);

        List<Integer> ids = sickRequestManager.getIdsWithLimit(startat, limit);
        ArrayList<Integer> nonExisting = Lists.newArrayList();
        try {
            while (ids.size() != 0) {
                nonExisting.addAll(ids);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrLeaveRequestConst.FIELD_COMPANY_ID + ":" + companyID +
                        " AND " + SolrLeaveRequestConst.FIELD_OBJECT_ID
                        + ":(" + ServerUtils.getAsCommoDelimited(ids, "0", " ") + ")");

                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer requestId = Integer.valueOf(sd.getFieldValue(SolrLeaveRequestConst.FIELD_OBJECT_ID).toString());
                    nonExisting.remove(requestId);
                }
                startat++;
                ids = sickRequestManager.getIdsWithLimit(startat * limit, limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.LEAVE_REQUEST);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("EdsSickrequest with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeCustomFormDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CUSTOM_FORM_ITEM_CORE);

        List<Integer> ids = customFormItemManager.getIdsWithLimit(startat, limit);
        ArrayList<Integer> nonExisting = Lists.newArrayList();
        try {
            while (ids.size() != 0) {
                nonExisting.addAll(ids);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrCustomFormConst.FIELD_COMPANY_ID + ":" + companyID +
                        " AND " + SolrCustomFormConst.FIELD_OBJECT_ID
                        + ":(" + ServerUtils.getAsCommoDelimited(ids, "0", " ") + ")");

                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer requestId = Integer.valueOf(sd.getFieldValue(SolrCustomFormConst.FIELD_OBJECT_ID).toString());
                    nonExisting.remove(requestId);
                }
                startat++;
                ids = customFormItemManager.getIdsWithLimit(startat * limit, limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.CUSTOM_FORM);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("EdsCustomFormItems with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeChartOfAccountSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.CHART_OF_ACCOUNT);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CHART_OF_ACCOUNT_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrChartOfAccountRepresenter.FIELD_ACCOUNT_ID);
        sQuery.addField(SolrChartOfAccountRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrChartOfAccountRepresenter.FIELD_ACCOUNT_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrChartOfAccountRepresenter.FIELD_ACCOUNT_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> edsAccountIds = accountingManager.getAccountIdsByIds(ids.toString());
                for (Integer edsAccountId : edsAccountIds) {
                    nonExisting.remove(edsAccountId);
                }
                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = Lists.newArrayList();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrChartOfAccountRepresenter.FIELD_ACCOUNT_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.CHART_OF_ACCOUNT);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrEventRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("EdsAccount with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeLeaveRequestSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.LEAVE_REQUEST);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_LEAVE_REQUEST_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrLeaveRequestConst.FIELD_OBJECT_ID);
        sQuery.addField(SolrLeaveRequestConst.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrLeaveRequestConst.FIELD_OBJECT_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrLeaveRequestConst.FIELD_OBJECT_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> sickrequestIds = sickRequestManager.getRequestIdsByIds(ids.toString());
                for (Integer id : sickrequestIds) {
                    nonExisting.remove(id);
                }
                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = Lists.newArrayList();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrLeaveRequestConst.FIELD_OBJECT_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.LEAVE_REQUEST);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrLeaveRequestConst.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("EdsSickrequest with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeCustomFormSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.CUSTOM_FORM);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CUSTOM_FORM_ITEM_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrCustomFormConst.FIELD_OBJECT_ID);
        sQuery.addField(SolrCustomFormConst.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrCustomFormConst.FIELD_OBJECT_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrCustomFormConst.FIELD_OBJECT_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> cIds = customFormItemManager.getCustomFormIdsByIds(ids.toString());
                for (Integer id : cIds) {
                    nonExisting.remove(id);
                }
                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = Lists.newArrayList();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrCustomFormConst.FIELD_OBJECT_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.CUSTOM_FORM);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrCustomFormConst.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("EdsCustomFormItems with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeShippingDataSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.SHIPPING_DATA);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_SHIPPING_DATA_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_ID);
        sQuery.addField(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> cIds = shippingDataManager.getShippingDataIdsByIds(ids.toString());
                for (Integer id : cIds) {
                    nonExisting.remove(id);
                }
                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = Lists.newArrayList();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.SHIPPING_DATA);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("EdsShippingData with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeCertificateSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.CERTIFICATE);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CERTIFICATE_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrCertificateRepresenter.FIELD_CERTIFICATE_ID);
        sQuery.addField(SolrCertificateRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrCertificateRepresenter.FIELD_CERTIFICATE_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrCertificateRepresenter.FIELD_CERTIFICATE_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> cIds = certificatemanager.getCertificateIdsByIds(ids.toString());
                for (Integer id : cIds) {
                    nonExisting.remove(id);
                }
                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = Lists.newArrayList();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrCertificateRepresenter.FIELD_CERTIFICATE_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.CERTIFICATE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("EdsCertificateOfEmployment with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzePositionSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.POSITION);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_POSITION_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrPositionRepresenter.FIELD_POSITION_ID);
        sQuery.addField(SolrPositionRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrPositionRepresenter.FIELD_POSITION_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrPositionRepresenter.FIELD_POSITION_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> cIds = positionManager.getPositionIdsByIds(ids.toString());
                for (Integer id : cIds) {
                    nonExisting.remove(id);
                }
                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = Lists.newArrayList();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrPositionRepresenter.FIELD_POSITION_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.POSITION);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("EdsPosition with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeDepartmentSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.DEPARTMENT);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_DEPARTMENT_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrDepartmentRepresenter.FIELD_DEPARTMENT_ID);
        sQuery.addField(SolrDepartmentRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrDepartmentRepresenter.FIELD_DEPARTMENT_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrDepartmentRepresenter.FIELD_DEPARTMENT_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> cIds = departmentManager.getDepartmentIdsByIds(ids.toString());
                for (Integer id : cIds) {
                    nonExisting.remove(id);
                }
                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = Lists.newArrayList();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrDepartmentRepresenter.FIELD_DEPARTMENT_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.DEPARTMENT);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("EdsDepartment with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private Integer fixInvoiceInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limt = 100;
        //retrives 100 inconsistences
        List<EdsSolrDbConsistency> contactSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.INVOICE, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limt);
        if (contactSolrInconsistencies.size() == 0) {
            return -1;// if there are no inconsistences
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_SALEINVOICE_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " invoice solr inconsistency fileids(" + sb + ")");
        EdsSolrDbConsistency sdb = contactSolrInconsistencies.get(contactSolrInconsistencies.size() - 1);
//        ServerSecurityContext.getInstance().removeCompanyId();
        return sdb.getObjectID();// returns last fixed inconsistency objectID for iterator
    }

    private Integer fixQuoteInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        //retrives 100 inconsistences
        List<EdsSolrDbConsistency> contactSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.QUOTE, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (contactSolrInconsistencies.size() == 0) {
            return -1;// if there are no inconsistences
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_SALEQUOTE_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " quote solr inconsistency fileids(" + sb + ")");
        EdsSolrDbConsistency sdb = contactSolrInconsistencies.get(contactSolrInconsistencies.size() - 1);
        return sdb.getObjectID();// returns last fixed inconsistency objectID for iterator
    }

    private Integer fixPurchaseOrderInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        //retrives 100 inconsistences
        List<EdsSolrDbConsistency> contactSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.PURCHASE_ORDER, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (contactSolrInconsistencies.size() == 0) {
            return -1;// if there are no inconsistences
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_PURCHASE_ORDER_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " purchase order solr inconsistency fileids(" + sb + ")");
        EdsSolrDbConsistency sdb = contactSolrInconsistencies.get(contactSolrInconsistencies.size() - 1);
        return sdb.getObjectID();// returns last fixed inconsistency objectID for iterator
    }

    private Integer fixOpportunityInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        //retrives 100 inconsistences
        List<EdsSolrDbConsistency> contactSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.OPPORTUNITY, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (contactSolrInconsistencies.size() == 0) {
            return -1;// if there are no inconsistences
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_OPPORTUNITY_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " opportunity solr inconsistency fileids(" + sb + ")");
        EdsSolrDbConsistency sdb = contactSolrInconsistencies.get(contactSolrInconsistencies.size() - 1);
        return sdb.getObjectID();// returns last fixed inconsistency objectID for iterator
    }

    private Integer fixEventInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> contactSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.EVENT, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (contactSolrInconsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrEventRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrEventRepresenter.FIELD_EVENT_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_EVENT_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " event solr inconsistency fileids(" + sb + ")");
        EdsSolrDbConsistency sdb = contactSolrInconsistencies.get(contactSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixProductsServicesInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> productsSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.PRODUCTS_SERVICES, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (productsSolrInconsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : productsSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrEventRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrProductServiceRepresenter.FIELD_PRODUCT_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_PRODUCTS_SERVICES_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : productsSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " products solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = productsSolrInconsistencies.get(productsSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixCourseScheduleInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> courseScheduleSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.COURSE_SCHEDULE, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (courseScheduleSolrInconsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : courseScheduleSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrEventRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrCourseScheduleRepresenter.FIELD_COURSE_SCHEDULE_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_COURSE_SCHEDULE_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : courseScheduleSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " course schedule solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = courseScheduleSolrInconsistencies.get(courseScheduleSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixCourseBookingInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> courseBookingSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.COURSE_BOOKING, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (courseBookingSolrInconsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : courseBookingSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrEventRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrCourseBookingRepresenter.FIELD_COURSE_BOOKING_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_COURSE_BOOKING_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : courseBookingSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " course booking solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = courseBookingSolrInconsistencies.get(courseBookingSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixEmployeeInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> employeeSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.EMPLOYEE, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (employeeSolrInconsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : employeeSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrEmployeeRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_EMPLOYEE_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : employeeSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " employee solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = employeeSolrInconsistencies.get(employeeSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixSinglePayrunInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> singlePayrunSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.SINGLE_PAYRUN, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (singlePayrunSolrInconsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        for (EdsSolrDbConsistency sdb : singlePayrunSolrInconsistencies) {
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrSinglePayrunRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrSinglePayrunRepresenter.FIELD_SINGLE_PAYRUN_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_SINGLE_PAYRUN_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : singlePayrunSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " single payrun solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = singlePayrunSolrInconsistencies.get(singlePayrunSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixGroupPayrunInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> groupPayrunSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.GROUP_PAYRUN, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (groupPayrunSolrInconsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : groupPayrunSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrGroupPayrunRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrGroupPayrunRepresenter.FIELD_GROUP_PAYRUN_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_GROUP_PAYRUN_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : groupPayrunSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " group payrun solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = groupPayrunSolrInconsistencies.get(groupPayrunSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixCashAdvanceInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> cashAdvanceSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.CASH_ADVANCE, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (cashAdvanceSolrInconsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : cashAdvanceSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrCashAdvanceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrCashAdvanceRepresenter.FIELD_CASH_ADVANCE_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_CASH_ADVANCE_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : cashAdvanceSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " cash advance solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = cashAdvanceSolrInconsistencies.get(cashAdvanceSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixAdditionalPaymentInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> additionalPaymentSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.ADDITIONAL_PAYMENT, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (additionalPaymentSolrInconsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : additionalPaymentSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrAdditionalPaymentPresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrAdditionalPaymentPresenter.FIELD_ADDITIONAL_PAYMENT_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_ADDITIONAL_PAYMENT_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : additionalPaymentSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " additional payment solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = additionalPaymentSolrInconsistencies.get(additionalPaymentSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixPurchaseInvoiceInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> purchaseSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.PURCHASE_INVOICE, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (purchaseSolrInconsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        for (EdsSolrDbConsistency sdb : purchaseSolrInconsistencies) {
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrPurchaseInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_PURCHASE_INVOICE_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : purchaseSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " products solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = purchaseSolrInconsistencies.get(purchaseSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixExpenseReportInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> expenseReportSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.EXPENSE_REPORT_CLAIMS, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (expenseReportSolrInconsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        for (EdsSolrDbConsistency sdb : expenseReportSolrInconsistencies) {
            sb.append(sdb.getEntityID()).append(" ");
        }
        String query = SolrExpenseReportRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrExpenseReportRepresenter.FIELD_REPORT_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_EXPENSE_REPORT_CLAIMS_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : expenseReportSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " expense reports solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = expenseReportSolrInconsistencies.get(expenseReportSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixShippingDataInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> shippingDataSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.SHIPPING_DATA, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (shippingDataSolrInconsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        for (EdsSolrDbConsistency sdb : shippingDataSolrInconsistencies) {
            sb.append(sdb.getEntityID()).append(" ");
        }
        String query = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_SHIPPING_DATA_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : shippingDataSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " shipping data solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = shippingDataSolrInconsistencies.get(shippingDataSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixCertificateInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> certificateSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.CERTIFICATE, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (certificateSolrInconsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        for (EdsSolrDbConsistency sdb : certificateSolrInconsistencies) {
            sb.append(sdb.getEntityID()).append(" ");
        }
        String query = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrCertificateRepresenter.FIELD_CERTIFICATE_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_CERTIFICATE_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : certificateSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " shipping data solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = certificateSolrInconsistencies.get(certificateSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixPositionInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> positionSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.POSITION, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (positionSolrInconsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        for (EdsSolrDbConsistency sdb : positionSolrInconsistencies) {
            sb.append(sdb.getEntityID()).append(" ");
        }
        String query = SolrPositionRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrPositionRepresenter.FIELD_POSITION_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_POSITION_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : positionSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " position solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = positionSolrInconsistencies.get(positionSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixDepartmentInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> departmentSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.DEPARTMENT, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (departmentSolrInconsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        for (EdsSolrDbConsistency sdb : departmentSolrInconsistencies) {
            sb.append(sdb.getEntityID()).append(" ");
        }
        String query = SolrDepartmentRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrDepartmentRepresenter.FIELD_DEPARTMENT_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_DEPARTMENT_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : departmentSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " department solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = departmentSolrInconsistencies.get(departmentSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixInvoiceInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        /// retrives 10 inconsistencies
        List<EdsSolrDbConsistency> contactDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.INVOICE, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (contactDbInconsistencies.isEmpty()) {
            return -1; // if there are no inconsistencies
        }
        /// add batch tasks using batch add 100

        var invoicesIds = contactDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).map(String::valueOf).collect(Collectors.joining(","));
        var invoiceList = invoiceManager.getSaleInvoiceByIds(invoicesIds);
        try {
            saleInvoiceSolrComponent.indexConcurrently(invoiceList);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : contactDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed invoice of Company ID=" + companyID + " DB inconsistency fileIDs (" + invoicesIds + ")");

        EdsSolrDbConsistency lastOne = contactDbInconsistencies.get(contactDbInconsistencies.size() - 1);
        return lastOne.getObjectID(); // returns last fixed inconsistency objectID
    }

    private Integer fixQuoteInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        /// retrives 10 inconsistencies
        List<EdsSolrDbConsistency> contactDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.QUOTE, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (contactDbInconsistencies.isEmpty()) {
            return -1; // if there are no inconsistencies
        }
        /// add batch tasks using batch add 100
        var quoteIds = contactDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).map(String::valueOf).collect(Collectors.joining(","));
        var pickList = pickListManager.getPickListBySaleQuoteIDs(quoteIds);
        var quoteList = quoteManager.getSaleQuotesByIds(quoteIds);
        try {
            saleQuoteSolrComponent.indexConcurrently(quoteList, pickList != null ? pickList : null);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : contactDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed Quote of Company ID=" + companyID + " DB inconsistency fileIDs (" + quoteIds + ")");

        EdsSolrDbConsistency lastOne = contactDbInconsistencies.get(contactDbInconsistencies.size() - 1);
        return lastOne.getObjectID(); // returns last fixed inconsistency objectID
    }

    private Integer fixPurchaseOrderInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        /// retrives 10 inconsistencies
        List<EdsSolrDbConsistency> contactDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.PURCHASE_ORDER, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (contactDbInconsistencies.isEmpty()) {
            return -1; // if there are no inconsistencies
        }
        /// add batch tasks using batch add 100

        var poIds = contactDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).map(String::valueOf).collect(Collectors.joining(","));
        var orderList = quoteManager.getPurchaseOrdersByIds(poIds);
        try {
            purchaseOrderSolrComponent.indexConcurrently(orderList);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : contactDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed Quote of Company ID=" + companyID + " DB inconsistency fileIDs (" + poIds + ")");

        EdsSolrDbConsistency lastOne = contactDbInconsistencies.get(contactDbInconsistencies.size() - 1);
        return lastOne.getObjectID(); // returns last fixed inconsistency objectID
    }

    private Integer fixOpportunityInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE;
        /// retrives 10 inconsistencies
        List<EdsSolrDbConsistency> contactDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.OPPORTUNITY, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (contactDbInconsistencies.size() == 0) {
            return -1; // if there are no inconsistencies
        }
        /// add batch tasks using batch add 100

        List<Integer> opportinutyIDs = new ArrayList<>();
        for (EdsSolrDbConsistency sdb : contactDbInconsistencies) {
            opportinutyIDs.add(sdb.getEntityID());
        }

        List<EdsOpportunity> opportinuties = opportunityManager.getOpportunityByIds(ServerUtils.getAsCommoDelimited(opportinutyIDs, "0", ","));
        try {
            opportunitySolrComponent.indexConcurrently(opportinuties);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : contactDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed Opportinuty of Company ID=" + companyID + " DB inconsistency fileIDs (" + opportinutyIDs + ")");

        EdsSolrDbConsistency lastOne = contactDbInconsistencies.get(contactDbInconsistencies.size() - 1);
        return lastOne.getObjectID(); // returns last fixed inconsistency objectID
    }

    private Integer fixEventInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        List<EdsSolrDbConsistency> contactDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.EVENT, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (contactDbInconsistencies.isEmpty()) {
            return -1;
        }

        var eventIDs = contactDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).collect(Collectors.toList());
        var eventList = eventManager.get(eventIDs);
        var relationMap = eventIDs.stream().collect(Collectors.toMap(eventId -> eventId, event -> relationManager.getAllRelations(EdsRelation.TYPE_EVENT, event)));

        try {
            eventSolrComponent.indexConcurrently(eventList, relationMap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : contactDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed Event of Company ID=" + companyID + " DB inconsistency fileIDs (" + eventIDs + ")");

        EdsSolrDbConsistency lastOne = contactDbInconsistencies.get(contactDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixProductsServicesInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsSolrDbConsistency> productsDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.PRODUCTS_SERVICES, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (productsDbInconsistencies.isEmpty()) {
            return -1;
        }

        var productIDs = productsDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).collect(Collectors.toList());

        List<EdsItem> itemList = itemManager.get(productIDs);
        try {
            productsServicesSolrComponent.indexConcurrently(itemList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : productsDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed Products of Company ID=" + companyID + " DB inconsistency IDs (" + productIDs + ")");

        EdsSolrDbConsistency lastOne = productsDbInconsistencies.get(productsDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixCourseSchedulesInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> courseScheduleDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.COURSE_SCHEDULE, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (courseScheduleDbInconsistencies.size() == 0) {
            return -1;
        }
        List<Integer> courseScheduleIDs = new ArrayList<>();
        for (EdsSolrDbConsistency sdb : courseScheduleDbInconsistencies) {
            courseScheduleIDs.add(sdb.getEntityID());
        }

        for (Integer invId : courseScheduleIDs) {
            try {
                EdsCourseSchedule courseSchedule = scheduledCourseManger.get(invId);
                solrManager.addCourseScheduleToIndex(courseSchedule);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
            }
        }
        for (EdsSolrDbConsistency sdb : courseScheduleDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed Course Schedules of Company ID=" + companyID + " DB inconsistency IDs (" + courseScheduleIDs + ")");

        EdsSolrDbConsistency lastOne = courseScheduleDbInconsistencies.get(courseScheduleDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixCourseBookingInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> courseBookingDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.COURSE_BOOKING, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (courseBookingDbInconsistencies.size() == 0) {
            return -1;
        }
        List<Integer> courseScheduleIDs = new ArrayList<>();
        for (EdsSolrDbConsistency sdb : courseBookingDbInconsistencies) {
            courseScheduleIDs.add(sdb.getEntityID());
        }

        for (Integer invId : courseScheduleIDs) {
            try {
                EdsCourseSchedule courseSchedule = scheduledCourseManger.get(invId);
                solrManager.addCourseScheduleToIndex(courseSchedule);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
            }
        }
        for (EdsSolrDbConsistency sdb : courseBookingDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed Course Booking of Company ID=" + companyID + " DB inconsistency IDs (" + courseScheduleIDs + ")");

        EdsSolrDbConsistency lastOne = courseBookingDbInconsistencies.get(courseBookingDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixEmployeesInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        List<EdsSolrDbConsistency> employeeDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.EMPLOYEE, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (employeeDbInconsistencies.isEmpty()) {
            return -1;
        }
        var employeeIds = employeeDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).map(String::valueOf).collect(Collectors.joining(","));
        List<EdsEmployee> employeesList = employeeManager.getEmployeesByIds(employeeIds);

        try {
            employeeSolrComponent.indexConcurrently(employeesList);
        } catch (SolrServerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : employeeDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed Employee of Company ID=" + companyID + " DB inconsistency IDs (" + employeeIds + ")");

        EdsSolrDbConsistency lastOne = employeeDbInconsistencies.get(employeeDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixSinglePayrunsInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        List<EdsSolrDbConsistency> singlePayrunDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.SINGLE_PAYRUN, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (singlePayrunDbInconsistencies.isEmpty()) {
            return -1;
        }
        var singlePayrunIDs = singlePayrunDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).collect(Collectors.toList());
        var singlePayrunList = payslipTableItemManager.get(singlePayrunIDs);
        try {
            singlePayrunSolrComponent.indexConcurrently(singlePayrunList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : singlePayrunDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed SinglePayrun of Company ID=" + companyID + " DB inconsistency IDs (" + singlePayrunIDs + ")");

        EdsSolrDbConsistency lastOne = singlePayrunDbInconsistencies.get(singlePayrunDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixGroupPayrunsInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        List<EdsSolrDbConsistency> groupPayrunDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.GROUP_PAYRUN, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (groupPayrunDbInconsistencies.isEmpty()) {
            return -1;
        }
        var groupPayrunIDs = groupPayrunDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).collect(Collectors.toList());
        var groupPayrunList = payslipTableManager.get(groupPayrunIDs);
        try {
            groupPayrunSolrComponent.indexConcurrently(groupPayrunList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : groupPayrunDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed GroupPayrun of Company ID=" + companyID + " DB inconsistency IDs (" + groupPayrunIDs + ")");

        EdsSolrDbConsistency lastOne = groupPayrunDbInconsistencies.get(groupPayrunDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixCashAdvancesInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit size
        List<EdsSolrDbConsistency> cashAdvanceDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.CASH_ADVANCE, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (cashAdvanceDbInconsistencies.isEmpty()) {
            return -1;
        }
        var cashAdvanceIDs = cashAdvanceDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).collect(Collectors.toList());

        List<EdsCashAdvance> cashAdvanceList = cashAdvanceManager.getCashAdvanceByIds(cashAdvanceIDs);
        try {
            cashAdvanceSolrComponent.indexConcurrently(cashAdvanceList);
        } catch (SolrServerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : cashAdvanceDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed CashAdvance of Company ID=" + companyID + " DB inconsistency IDs (" + cashAdvanceIDs + ")");

        EdsSolrDbConsistency lastOne = cashAdvanceDbInconsistencies.get(cashAdvanceDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixAdditionalPaymentsInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> additionalPaymentDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.ADDITIONAL_PAYMENT, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (additionalPaymentDbInconsistencies.isEmpty()) {
            return -1;
        }
        List<Integer> additionalPaymentIDs = new ArrayList<>();
        for (EdsSolrDbConsistency sdb : additionalPaymentDbInconsistencies) {
            additionalPaymentIDs.add(sdb.getEntityID());
        }

        for (Integer additionalPaymentId : additionalPaymentIDs) {
            try {
                EdsAdditionalPayment additionalPayment = additionalPaymentManager.get(additionalPaymentId);
                additionalPaymentSolrComponent.index(additionalPayment);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (EdsSolrDbConsistency sdb : additionalPaymentDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed AdditionalPayment of Company ID=" + companyID + " DB inconsistency IDs (" + additionalPaymentIDs + ")");

        EdsSolrDbConsistency lastOne = additionalPaymentDbInconsistencies.get(additionalPaymentDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixPurchaseInvoiceInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsSolrDbConsistency> purchaseDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.PURCHASE_INVOICE, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (purchaseDbInconsistencies.isEmpty()) {
            return -1;
        }

        List<EdsPurchaseInvoice> purchaseInvoiceByIds = invoiceManager.getPurchaseInvoiceByIds(purchaseDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).map(String::valueOf).collect(Collectors.joining(",")));
        try {
            purchaseInvoiceSolrComponent.indexConcurrently(purchaseInvoiceByIds);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : purchaseDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed Purchase Invoice of Company ID=" + companyID + " DB inconsistency IDs (" + purchaseInvoiceByIds + ")");

        EdsSolrDbConsistency lastOne = purchaseDbInconsistencies.get(purchaseDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixExpenseReportInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        List<EdsSolrDbConsistency> expenseReportDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.EXPENSE_REPORT_CLAIMS, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (expenseReportDbInconsistencies.isEmpty()) {
            return -1;
        }
        var repIds = expenseReportDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).collect(Collectors.toList());
        var itemList = expenseReportManager.get(repIds);
        try {
            expenseReportClaimsSolrComponent.indexConcurrently(itemList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : expenseReportDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed Expense Reports of Company ID=" + companyID + " DB inconsistency IDs (" + repIds + ")");

        EdsSolrDbConsistency lastOne = expenseReportDbInconsistencies.get(expenseReportDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixGdnGrnInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        List<EdsSolrDbConsistency> shippingDataDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.SHIPPING_DATA, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (shippingDataDbInconsistencies.isEmpty()) {
            return -1;
        }
        var reportsIDs = shippingDataDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).collect(Collectors.toList());
        var itemList = shippingDataManager.get(reportsIDs);

        try {
            shippingDataSolrComponent.indexConcurrently(itemList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : shippingDataDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed Shipping Data of Company ID=" + companyID + " DB inconsistency IDs (" + reportsIDs + ")");

        EdsSolrDbConsistency lastOne = shippingDataDbInconsistencies.get(shippingDataDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixCertificateInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsSolrDbConsistency> certificateDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.CERTIFICATE, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (certificateDbInconsistencies.isEmpty()) {
            return -1;
        }
        String certificateIds = certificateDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).map(String::valueOf).collect(Collectors.joining(","));
        var certificatesList = certificatemanager.getCertificatesByIds(certificateIds);

        try {
            certificateSolrComponent.indexConcurrently(certificatesList);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        for (EdsSolrDbConsistency sdb : certificateDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed Certificates of Company ID=" + companyID + " DB inconsistency IDs (" + certificateIds + ")");

        EdsSolrDbConsistency lastOne = certificateDbInconsistencies.get(certificateDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixPositionInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> positionDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.POSITION, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (positionDbInconsistencies.size() == 0) {
            return -1;
        }
        List<Integer> positionIDs = new ArrayList<>();
        for (EdsSolrDbConsistency sdb : positionDbInconsistencies) {
            positionIDs.add(sdb.getEntityID());
        }

        for (Integer posId : positionIDs) {
            try {
                EdsPosition item = positionManager.get(posId);
                solrManager.indexAddPosition(Collections.singletonList(item), companyID);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
            }
        }
        for (EdsSolrDbConsistency sdb : positionDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed Positions of Company ID=" + companyID + " DB inconsistency IDs (" + positionIDs + ")");

        EdsSolrDbConsistency lastOne = positionDbInconsistencies.get(positionDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixChartOfAccountInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> edsAccountConsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID,
                EdsSolrDbConsistency.CHART_OF_ACCOUNT,
                EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB,
                startAt,
                limit);
        if (edsAccountConsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : edsAccountConsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        String query = SolrChartOfAccountRepresenter.FIELD_COMPANY_ID + ":" + companyID +
                " AND " + SolrChartOfAccountRepresenter.FIELD_ACCOUNT_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_CHART_OF_ACCOUNT_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : edsAccountConsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " account solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = edsAccountConsistencies.get(edsAccountConsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixLeaveRequestInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> sickrequestConsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID,
                EdsSolrDbConsistency.LEAVE_REQUEST,
                EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB,
                startAt,
                limit);
        if (sickrequestConsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : sickrequestConsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        String query = SolrLeaveRequestConst.FIELD_COMPANY_ID + ":" + companyID +
                " AND " + SolrLeaveRequestConst.FIELD_OBJECT_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_LEAVE_REQUEST_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : sickrequestConsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " leave request solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = sickrequestConsistencies.get(sickrequestConsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixCustomFormInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> customFormConsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID,
                EdsSolrDbConsistency.CUSTOM_FORM,
                EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB,
                startAt,
                limit);
        if (customFormConsistencies.size() == 0) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : customFormConsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        String query = SolrCustomFormConst.FIELD_COMPANY_ID + ":" + companyID +
                " AND " + SolrCustomFormConst.FIELD_OBJECT_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_CUSTOM_FORM_ITEM_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : customFormConsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " custom form solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = customFormConsistencies.get(customFormConsistencies.size() - 1);
        return sdb.getObjectID();
    }

    private Integer fixChartOfAccountInconsistencyInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        List<EdsSolrDbConsistency> edsAccountConsistency = solrDbConsistencyManager.getCompanyInconsistiens(companyID,
                EdsSolrDbConsistency.CHART_OF_ACCOUNT,
                EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR,
                startAt,
                limit);
        if (edsAccountConsistency.isEmpty()) {
            return -1;
        }
        var accountIds = edsAccountConsistency.stream().map(EdsSolrDbConsistency::getEntityID).map(String::valueOf).collect(Collectors.joining(","));
        List<EdsAccount> accountsList = accountingManager.getAccountsByIds(accountIds);
        try {
            chartOfAccountSolrComponent.indexConcurrently(accountsList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : edsAccountConsistency) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed EdsAccount of Company ID=" + companyID + " DB inconsistency IDs (" + accountIds + ")");

        EdsSolrDbConsistency lastOne = edsAccountConsistency.get(edsAccountConsistency.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixLeaveRequestInconsistencyInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        List<EdsSolrDbConsistency> leaveRequestConsistency = solrDbConsistencyManager.getCompanyInconsistiens(companyID,
                EdsSolrDbConsistency.LEAVE_REQUEST,
                EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR,
                startAt,
                limit);
        if (leaveRequestConsistency.isEmpty()) {
            return -1;
        }
        var requestIds = leaveRequestConsistency.stream().map(EdsSolrDbConsistency::getEntityID).collect(Collectors.toList());
        var leaveRequestList = sickRequestManager.get(requestIds);
        try {
            leaveRequestSolrComponent.indexConcurrently(leaveRequestList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : leaveRequestConsistency) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed EdsSickRequest of Company ID=" + companyID + " DB inconsistency IDs (" + requestIds + ")");

        EdsSolrDbConsistency lastOne = leaveRequestConsistency.get(leaveRequestConsistency.size() - 1);
        return lastOne.getObjectID();
    }

    private Integer fixCustomFormInconsistencyInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        List<EdsSolrDbConsistency> customFormConsistency = solrDbConsistencyManager.getCompanyInconsistiens(companyID,
                EdsSolrDbConsistency.CUSTOM_FORM,
                EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR,
                startAt,
                limit);
        if (customFormConsistency.isEmpty()) {
            return -1;
        }
        var cfIds = customFormConsistency.stream().map(EdsSolrDbConsistency::getEntityID).map(String::valueOf).collect(Collectors.joining(","));
        var customFormItems = customFormItemManager.getCustomFormByIds(cfIds);
        try {
            customFormItemSolrComponent.indexConcurrently(customFormItems);
        } catch (SolrServerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : customFormConsistency) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed EdsCustomFormItems of Company ID=" + companyID + " DB inconsistency IDs (" + cfIds + ")");

        EdsSolrDbConsistency lastOne = customFormConsistency.get(customFormConsistency.size() - 1);
        return lastOne.getObjectID();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SchemaList getSchemas(ListingFilterParameter filterParameter) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setFacetFilter(filterParameter.getFacetFilter());
        fp.setSearchKey(filterParameter.getSearchKey());
        fp.setSortField(filterParameter.getSortField());
        fp.setAscending(filterParameter.isAscending());
        fp.setBriefly(true);

        ListLoadConfig config = new ListLoadConfig();
        config.setStart(filterParameter.getStart());
        config.setLimit(filterParameter.getLimit());
        config.setSortField(filterParameter.getSortField());
        config.setSortDir(filterParameter.isAscending() ? 1 : 2);

        List<Object[]> companies = companyManager.getSchemaList(fp);
        if (companies != null && companies.size() > 0) {
            SchemaList schemaList = new SchemaList();
            schemaList.setTotal(companies.size());


            if (config.getLimit() > 0) {
                companies = ListUtils.getSublist(companies, config.getStart(), config.getLimit());
            }

            SchemaListItem[] companyItems = new SchemaListItem[companies.size()];
            int i = 0;
            for (Object[] c : companies) {
                String name = (String) c[1];
                companyItems[i] = new SchemaListItem();
                companyItems[i].setObjectID(Integer.valueOf(c[0].toString()));
                companyItems[i].setName(name);
                companyItems[i].setDescription("");
                companyItems[i].setFree(name != null ? Boolean.FALSE : Boolean.TRUE);
                companyItems[i].setMaintenance(globalAuthJdbcSpringManager.getMaintenanceStatus(Integer.valueOf(c[0].toString())));
                i++;
            }

            schemaList.setList(new ArrayList(Arrays.asList(companyItems)));
            ListPanelToolRpc panelSettings = filterParameter.getListPanelTool();
            if (panelSettings == null) {//Default View Column Code Name
                ArrayList<String> columnCodeName = new ArrayList<>();
                columnCodeName.add(SchemaListItem.NAME);
                columnCodeName.add(SchemaListItem.DESCRIPTION);
                columnCodeName.add(SchemaListItem.FREE);
                columnCodeName.add(SchemaListItem.OBJECT_ID);
                panelSettings = new ListPanelToolRpc();
                panelSettings.setColumnCodeName(columnCodeName);
            }
            return schemaList;
        } else {
            return new SchemaList(new ArrayList<>(), 0, new SelectItem[0]);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SchemaListItem> getAllSchemaList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        List<Object[]> companies = companyManager.getSchemaList(fp);

        List<SchemaListItem> companyItems = new ArrayList<>();
        for (Object[] c : companies) {
            String name = (String) c[1];
            SchemaListItem companyItem = new SchemaListItem();
            companyItem.setObjectID(Integer.valueOf(c[0].toString()));
            companyItem.setName(name);
            companyItem.setDescription("");
            companyItem.setFree(name != null ? Boolean.FALSE : Boolean.TRUE);
            companyItems.add(companyItem);
        }

        return companyItems;
    }

    public Boolean removeCompany(Integer companyID) {
        if (companyID != null && companyID > 0) {
            try {
                jdbcSpringManager.deleteCompanyAndSchema(companyID);
                globalAuthJdbcSpringManager.deleteCompanyFromGlobalAuth(companyID);
                EdsCompany company = companyManager.get(companyID);
                if (company != null) {
                    company.setActive(false);
                    companyManager.update(company);
                }
                return Boolean.TRUE;
            } catch (Exception e) {
                e.printStackTrace();
                return Boolean.FALSE;
            }
        }
        return Boolean.FALSE;
    }

    @Transactional
    public boolean removeSchema(Integer companyID) {
        if (companyID != null && companyID > 0) {
            try {
                String schema = "\"" + companyID + "\"";
                jdbcSpringManager.getSimJdbcOperations().execute("DROP SCHEMA " + schema + " CASCADE ");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Transactional(propagation = Propagation.NEVER)
    public String createSchemaByID(Integer schema, HashSet<String> activeModules) {
        try {
            return signUpServiceLocal.preProcessCompanyData(schema, activeModules);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(propagation = Propagation.NEVER)
    public String getTemplateSchemaForID(Integer schema, TemplateSchema templateSchema) {
        try {
            return signUpServiceLocal.preProcessSampleCompanyData(schema, templateSchema);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public Integer createSchemas(Integer count) {
        int created = 0;
        if (count != null && count > 0) {
            for (int i = 0; i < count; i++) {
                try {
                    signUpServiceLocal.preProcessCompanyData(null, null);
                    created++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return created;
    }

    @Transactional
    public String applyPatch(String schemaName, String excludeSchemas, String query) {
        StringBuilder result = new StringBuilder();
        if (StringUtils.isNotEmpty(schemaName)) {
            try {
                ArrayList<String> exSchemas = new ArrayList<>();
                if (StringUtils.isNotEmpty(excludeSchemas)) {
                    String[] ss = excludeSchemas.split(";");
                    exSchemas.addAll(Arrays.asList(ss));
                }
                switch (schemaName.trim()) {
                    case "0" -> {
                        List<String> companies = companyManager.getExistingSchemasWithTemplate();
                        int processedCount = 0;
                        for (String company : companies) {
                            if (company != null && !company.equals("0") && !exSchemas.contains(company)) {
                                try {
                                    String query1 = query.replaceAll(EdsScope.PRIVATE_SCHEMA, company);
                                    Query queryObject = entityManager.createNativeQuery(query1);
                                    queryObject.executeUpdate();
                                    processedCount++;
                                    // Batch flush every 50 companies to avoid memory issues
                                    if (processedCount % 50 == 0) {
                                        entityManager.flush();
                                        entityManager.clear();
                                    }
                                } catch (Exception e) {
                                    log.error("Failed to apply patch for company: " + company, e);
                                    result.append("Error in company ").append(company).append(": ").append(e.getMessage()).append("; ");
                                }
                            }
                        }
                        result.insert(0, "Processed " + processedCount + " companies. ");
                    }
                    case "freeSchemas" -> {
                        List<SchemaListItem> schemaListItems = getAllSchemaList(null);
                        int processedCount = 0;
                        for (SchemaListItem schemaListItem : schemaListItems) {
                            if (schemaListItem.getFree() && schemaListItem.getObjectID() != 0 && !exSchemas.contains(String.valueOf(schemaListItem.getObjectID()))) {
                                try {
                                    String query1 = query.replaceAll(EdsScope.PRIVATE_SCHEMA, String.valueOf(schemaListItem.getObjectID()));
                                    Query queryObject = entityManager.createNativeQuery(query1);
                                    queryObject.executeUpdate();
                                    processedCount++;
                                    if (processedCount % 50 == 0) {
                                        entityManager.flush();
                                        entityManager.clear();
                                    }
                                } catch (Exception e) {
                                    log.error("Failed to apply patch for schema: " + schemaListItem.getObjectID(), e);
                                    result.append("Error in schema ").append(schemaListItem.getObjectID()).append(": ").append(e.getMessage()).append("; ");
                                }
                            }
                        }
                        result.insert(0, "Processed " + processedCount + " free schemas. ");
                    }
                    default -> {
                        if (!schemaName.equals("0") && !exSchemas.contains(schemaName)) {
                            try {
                                query = query.replaceAll(EdsScope.PRIVATE_SCHEMA, schemaName);
                                Query queryObject = entityManager.createNativeQuery(query);
                                queryObject.executeUpdate();
                                result.append("Successfully applied patch to schema: ").append(schemaName);
                            } catch (Exception e) {
                                log.error("Failed to apply patch for schema: " + schemaName, e);
                                result.append("Error in schema ").append(schemaName).append(": ").append(e.getMessage());
                            }
                        }
                    }
                }

            } catch (Exception e) {
                log.error("Error in applyPatch", e);
                result.append("Global error: ").append(e.getMessage());
            }
        }
        return result.toString();
    }

    public SelectItem[] getSchemasAsSelectItem(ListingFilterParameter filterParameter) {
        filterParameter = filterParameter == null ? new ListingFilterParameter() : filterParameter;
        EdsUser user = userManager.getUser();
        EdsBackendManagement backendManagement = backendManagementManager.getBackendManagement(user.getCompany().getObjectID(), user.getObjectID());
        if (backendManagement != null) {
            filterParameter.setParams(backendManagement.getHostNames());
            filterParameter.setAccountCode(backendManagement.getPromotionalCode());
        }
        List<Object[]> companies = companyManager.getCompanySchemas(filterParameter);

        ArrayList<SelectItem> result = new ArrayList<>();
        if (!filterParameter.isFromPartnerBackend()) {
            result.add(new SelectItem(0, "All Schemas"));
            result.add(new SelectItem(-1, "All Free Schemas"));
            result.add(new SelectItem(-2, "All Paid Schemas"));
        }
        for (Object[] company : companies) {
            Integer schemaID = Integer.parseInt((String) company[0]);
            if (filterParameter.getDiscludedSchemaID() == null || !schemaID.equals(filterParameter.getDiscludedSchemaID())) {
                result.add(new SelectItem(schemaID, company[0] + " ( " + (company[1] != null ? ((String) company[1]).trim() : "") + " )"));
            }
        }
        return result.toArray(new SelectItem[]{});
    }

    public Boolean exportSchema(String schemaName) {
        try {
            List<String> schemas = new ArrayList<>();
            if (schemaName != null && !schemaName.trim().isEmpty()) {
                if (schemaName.equals("0")) {
                    List<Object[]> companies = companyManager.getCompanySchemas(new ListingFilterParameter());
                    for (Object[] company : companies) {
                        schemas.add((String) company[0]);
                    }
                } else {
                    schemas.add(schemaName);
                }
                EdsSchemaUpdater.process(schemas, ServerSecurityContext.getInstance().getDatabase());
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }

    }

    //Start Pdf Settings

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getSchemaFacetFilterData(FacetFilterRpc schemaFacet) {
        int num = 0;
        if (schemaFacet != null && !schemaFacet.isFilterChanges()) {
            schemaFacet = commonServiceLocal.getUserFacetFilter(schemaFacet);
        }
        StringBuilder sqlQuery = new StringBuilder("SELECT count(id) as total, isFree as isFree FROM company ");
        for (String key : schemaFacet.getShowSolrFieldMap().keySet()) {
            FacetContentRpc facetContent = schemaFacet.getFacetContentMap().get(key);
            if (facetContent != null && facetContent.getFacetItems().length != 0) {
                SelectItem[] items = facetContent.getFacetItems();
                boolean appendOperator = false;
                for (SelectItem item : items) {
                    if (appendOperator) {
                        sqlQuery.append(" OR ");
                    } else {
                        sqlQuery.append(" WHERE ");
                        appendOperator = true;
                    }
                    sqlQuery.append(" isFree=").append(item.getName());
                }
            }

        }
        sqlQuery.append(" GROUP by isfree");
        List<Object[]> schemasCount = companyManager.findNative(sqlQuery.toString());
        if (schemasCount != null) {
            SelectItem[] schemas = new SelectItem[schemasCount.size()];
            for (Object[] data : schemasCount) {
                schemas[num] = new SelectItem();
                schemas[num].setId(((BigInteger) data[0]).intValue());
                schemas[num].setName(String.valueOf(data[1]));
                schemas[num].setDescription(schemas[num].getName() + " ( <b>" + ((BigInteger) data[0]).intValue() + "</b> )");
                num++;
            }
            schemaFacet.getFacetContentMap().get(FacetContentType.SchemaFacetFilter.getContentCode()[0]).setFacetItems(ServerUtils.sortSelectItemByDesc(schemas));
        } else {
            schemaFacet.getFacetContentMap().get(FacetContentType.SchemaFacetFilter.getContentCode()[0]).setFacetItems(new SelectItem[0]);
        }

        return schemaFacet;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getInsertPublicData(Integer objectID) {
        return jdbcSpringManager.generateSqlQuery(objectID);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<PDFTemplatesListItem> getCompanyPDFTemplates(ListingFilterParameter fp) {
        Integer companyID = fp.getCompanyID();

        if (companyID == null || companyID == 0 || companyID == -1) {
            return new ListResult<>(new ArrayList<>(), 0);
        }

        ServerSecurityContext.getInstance().setCompanyId(companyID);
        List<EdsCompanyPdfTemplate> templates = companyPdfTemplateManager.getCompanyPDFTemplates(fp);
        Integer templatesCount = companyPdfTemplateManager.getCompanyPDFTemplatesCount(fp);
        if (templates == null || templates.isEmpty()) {
            return new ListResult<>(new ArrayList<>(), 0);
        }
        ArrayList<PDFTemplatesListItem> itemList = new ArrayList<>(templates.size());
        EdsCompany company = companyManager.getCompany(companyID);

        for (EdsCompanyPdfTemplate t : templates) {
            PDFTemplatesListItem items = new PDFTemplatesListItem();
            items.setObjectID(t.getObjectID());
            items.setCompanyID(company.getObjectID());
            items.setDefaultTemplate(t.isDefaultTemplate());
            items.setCompanyName(company.getName());
            items.setTemplateName(t.getName());
            if (t.getTemplate() != null && t.getTemplate().getType() != null) {
                items.setType(t.getTemplate().getType().getName());
            }
            items.setFont(t.getFontFamily());
            items.setShortNumberFormat(t.getNumberFormat());
            items.setExtendedNumberFormat(t.getExtendedNumberFormat());
            items.setGenerateType(t.getGenerateType());
            itemList.add(items);
        }
        return new ListResult<>(itemList, templatesCount);
    }

    public ListResult<GenericSettingsRPC> getCompanyGenericSettings(ListingFilterParameter filterParameter) {
        ArrayList<GenericSettingsRPC> result = new ArrayList<>();
        List<String> keys = filterParameter.getCompanyID() != null ? genericSettingsManager.getEnabledGenericSettings(filterParameter.getCompanyID()) : new ArrayList<>();
        if (filterParameter.getSearchKey() != null) {
            String temp = filterParameter.getSearchKey().toLowerCase();
            temp = temp.replaceAll("\\s", "_");
            for (GenericSettingsEnum gs : GenericSettingsEnum.values()) {
                if (!(GenericSettingsEnum.PO_IGNORE_MANAGER_APPROVAL_ROLES.equals(gs) ||
                        GenericSettingsEnum.USPS_USER_ID.equals(gs) ||
                        GenericSettingsEnum.USPS_USER_PASSWORD.equals(gs) ||
                        GenericSettingsEnum.COMPANY_BALANCE.equals(gs) ||
                        GenericSettingsEnum.UNRECOGNIZED_REVENUE_CODE.equals(gs) ||
                        GenericSettingsEnum.DEFAULT_ACCOUNT_FOR_PROJECT_BASE_EXPENSE.equals(gs) ||
                        GenericSettingsEnum.DEFAULT_SKILL_GROUPS.equals(gs) ||
                        GenericSettingsEnum.DYNAMIC_CUSTOM_FIELD.equals(gs) ||
                        GenericSettingsEnum.PRODUCT_CUSTOM_FIELDS.equals(gs) ||
                        GenericSettingsEnum.TASK_DISCOUNT_FIELD.equals(gs) ||
                        GenericSettingsEnum.EMPLOYEE_FORM_PERSONAL_ID.equals(gs))) {
                    if (gs.name().toLowerCase().contains(temp)) {
                        GenericSettingsRPC rpc = new GenericSettingsRPC();
                        rpc.setKey(gs);
                        rpc.setEnabled(keys.contains(gs.name()));
                        result.add(rpc);
                    }
                }
            }

        } else {
            for (GenericSettingsEnum gs : GenericSettingsEnum.values()) {
                if (!(GenericSettingsEnum.PO_IGNORE_MANAGER_APPROVAL_ROLES.equals(gs) ||
                        GenericSettingsEnum.USPS_USER_ID.equals(gs) ||
                        GenericSettingsEnum.USPS_USER_PASSWORD.equals(gs) ||
                        GenericSettingsEnum.COMPANY_BALANCE.equals(gs) ||
                        GenericSettingsEnum.UNRECOGNIZED_REVENUE_CODE.equals(gs) ||
                        GenericSettingsEnum.DEFAULT_ACCOUNT_FOR_PROJECT_BASE_EXPENSE.equals(gs) ||
                        GenericSettingsEnum.DEFAULT_SKILL_GROUPS.equals(gs) ||
                        GenericSettingsEnum.DYNAMIC_CUSTOM_FIELD.equals(gs) ||
                        GenericSettingsEnum.PRODUCT_CUSTOM_FIELDS.equals(gs) ||
                        GenericSettingsEnum.TASK_DISCOUNT_FIELD.equals(gs) ||
                        GenericSettingsEnum.EMPLOYEE_FORM_PERSONAL_ID.equals(gs))) {
                    GenericSettingsRPC rpc = new GenericSettingsRPC();
                    rpc.setKey(gs);
                    rpc.setEnabled(keys.contains(gs.name()));
                    result.add(rpc);
                }
            }
        }
        Collections.sort(result, Comparator.comparing(GenericSettingsRPC::getKey));
        return new ListResult<>(result, GenericSettingsEnum.values().length);
    }

    @Override
    public void enableDisableGenericSettings(Integer companyID, GenericSettingsEnum key, boolean enable) {
        genericSettingsManager.saveGenericSettings(companyID, key, enable ? EdsGenericSettings.YES : EdsGenericSettings.NO);
    }

    private SelectItem[] getPDFFonts() {
        ITextFontTypeEnum[] fonts = ITextFontTypeEnum.values();
        SelectItem[] fontsAsSelectItem = new SelectItem[fonts.length];
        for (int i = 0; i < fonts.length; i++) {
            fontsAsSelectItem[i] = new SelectItem(fonts[i].getId(), fonts[i].getName(), fonts[i].getFileName());
        }
        return fontsAsSelectItem;
    }

    private SelectItem[] getPdfTemplateReferences() {
        List<EdsPdfReference> references = pdfReferenceManager.getReferences();
        Comparator<EdsPdfReference> referenceComparator = Comparator.comparing(EdsPdfReference::getName);
        references.sort(referenceComparator);
        SelectItem[] types = new SelectItem[references.size()];
        int i = 0;
        for (EdsPdfReference r : references) {
            types[i] = new SelectItem(r.getObjectID(), r.getName());
            i++;
        }
        return types;
    }

    private SelectItem[] getCustomFormItems(String module) {
        List<EdsProperty> properties = propertManager.findByModuleCodeFromBackend(module);
        SelectItem[] types = new SelectItem[properties.size()];
        int i = 0;
        for (EdsProperty item : properties) {
            String name = StringUtils.isNotEmpty(item.getPlural()) ? item.getPlural() : item.getDefaultName();
            types[i] = new SelectItem(item.getObjectID(), name, item.getFormID());
            i++;
        }
        return types;
    }

    @Override
    public Integer saveAiPhantomPdfTemplate(PDFSettingsTransObject transObject) {
        EdsPdfReference pdfType = pdfReferenceManager.getById(transObject.getPdfReferenceID());
        if (transObject.getAttachedFiles() == null || transObject.getAttachedFiles().length == 0) {
            return null;
        }

        FileResource f = documentsServiceLocal.getFileResource(transObject.getAttachedFiles()[0].getId());
        if (f != null && !ServerUtils.isNullOrEmpty(f.getAmazonLink())) {
            transObject = AiPhantomPdfClient.getHtmlFromAi(transObject, pdfType.getCode(), f.getAmazonLink());
        }

        if (transObject == null) {
            return null;
        }
        return this.saveCompanyPdfTemplate(transObject);
    }

    @Override
    public Integer saveCompanyPdfTemplate(PDFSettingsTransObject transObject) {
        ServerSecurityContext.getInstance().setCompanyId(transObject.getCompanyID());
        EdsCompanyPdfTemplate companyPdfTemplate;
        EdsPdfTemplate pdfTemplate;
        if (transObject.getObjectID() != null) {
            companyPdfTemplate = companyPdfTemplateManager.get(transObject.getObjectID());
            pdfTemplate = companyPdfTemplate.getTemplate();
        } else {
            companyPdfTemplate = new EdsCompanyPdfTemplate();
            pdfTemplate = new EdsPdfTemplate();
        }
        String decodedHtml = StringEscapeUtils.unescapeHtml4(transObject.getContent());
        pdfTemplate.setContent(decodedHtml);
        pdfTemplate.setType(pdfReferenceManager.getById(transObject.getPdfReferenceID()));
        pdfTemplate.setHeader(transObject.getHeader());
        pdfTemplate.setFooter(transObject.getFooter());
        companyPdfTemplate.setDefaultTemplate(transObject.isDefaultTemplate());
        companyPdfTemplate.setBrowserVersion(transObject.isBrowserVersion());
        companyPdfTemplate.setTemplate(pdfTemplate);
        companyPdfTemplate.setName(transObject.getTemplateName());
        companyPdfTemplate.setFontFamily(transObject.getFontFileName());
        companyPdfTemplate.setCustomFormItemFormId(transObject.getCustomFormItemFormId());
        companyPdfTemplate.setSection(transObject.getSection());
        companyPdfTemplate.setNumberFormat(transObject.getNumFormat());
        companyPdfTemplate.setNumberFormatDecimalSeparator(transObject.getNumFormatDecSeparator());
        companyPdfTemplate.setNumberFormatGroupSeparator(transObject.getNumFormatGroupSeparator());
        companyPdfTemplate.setExtendedNumberFormat(transObject.getExNumFormat());
        companyPdfTemplate.setExtendedNumberFormatDecimalSeparator(transObject.getExNumFormatDecSeparator());
        companyPdfTemplate.setExtendedNumberFormatGroupSeparator(transObject.getExNumFormatGroupSeparator());
        companyPdfTemplate.setGenerateType(transObject.getGenerateType());
        companyPdfTemplate.setPageFormat(transObject.getPageFormat());
        companyPdfTemplate.setOrientation(transObject.getOrientation());
        companyPdfTemplate.setMarginTop(transObject.getMarginTop());
        companyPdfTemplate.setMarginRight(transObject.getMarginRight());
        companyPdfTemplate.setMarginBottom(transObject.getMarginBottom());
        companyPdfTemplate.setMarginLeft(transObject.getMarginLeft());
        companyPdfTemplate.setHeaderHeight(transObject.getHeaderHeight());
        companyPdfTemplate.setFooterHeight(transObject.getFooterHeight());
        if (companyPdfTemplate.getObjectID() != null) {
            companyPdfTemplateManager.update(companyPdfTemplate);
        } else {
            companyPdfTemplateManager.create(companyPdfTemplate);
        }
        companyPdfTemplateManager.flush();
        return companyPdfTemplate.getObjectID();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PDFSettingsTransObject getCompanyPDFSettings(Integer companyID, Integer companyPDFTemplateID) {
        if (companyID != null) {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
        }
        PDFSettingsTransObject transObject = new PDFSettingsTransObject();
        transObject.setReferences(getPdfTemplateReferences());
        transObject.setFonts(getPDFFonts());

        if (companyID == null || companyPDFTemplateID == null) {
            return transObject;
        }
        EdsCompanyPdfTemplate companyPdfTemplate = companyPdfTemplateManager.get(companyPDFTemplateID);
        if (companyPdfTemplate == null) {
            return transObject;
        }
        transObject.setObjectID(companyPdfTemplate.getObjectID());
        transObject.setCompanyID(companyID);
        transObject.setDefaultTemplate(companyPdfTemplate.isDefaultTemplate());
        transObject.setBrowserVersion(companyPdfTemplate.getBrowserVersion());
        transObject.setTemplateName(companyPdfTemplate.getName());
        transObject.setFontFileName(companyPdfTemplate.getFontFamily());
        transObject.setCustomFormItemFormId(companyPdfTemplate.getCustomFormItemFormId());
        transObject.setSection(companyPdfTemplate.getSection());
        if (companyPdfTemplate.getSection() != null) {
            transObject.setCustomFormItems(getCustomFormItems(companyPdfTemplate.getSection()));
        }
        transObject.setNumFormat(companyPdfTemplate.getNumberFormat());
        transObject.setNumFormatDecSeparator(companyPdfTemplate.getNumberFormatDecimalSeparator());
        transObject.setNumFormatGroupSeparator(companyPdfTemplate.getNumberFormatGroupSeparator());
        transObject.setExNumFormat(companyPdfTemplate.getExtendedNumberFormat());
        transObject.setExNumFormatDecSeparator(companyPdfTemplate.getExtendedNumberFormatDecimalSeparator());
        transObject.setExNumFormatGroupSeparator(companyPdfTemplate.getExtendedNumberFormatGroupSeparator());
        transObject.setPageFormat(companyPdfTemplate.getPageFormat());
        transObject.setOrientation(companyPdfTemplate.getOrientation());
        transObject.setMarginTop(companyPdfTemplate.getMarginTop());
        transObject.setMarginRight(companyPdfTemplate.getMarginRight());
        transObject.setMarginBottom(companyPdfTemplate.getMarginBottom());
        transObject.setMarginLeft(companyPdfTemplate.getMarginLeft());
        transObject.setHeaderHeight(companyPdfTemplate.getHeaderHeight());
        transObject.setFooterHeight(companyPdfTemplate.getFooterHeight());
        if (companyPdfTemplate.getTemplate() != null) {
            if (companyPdfTemplate.getTemplate().getType() != null) {
                transObject.setPdfReferenceID(companyPdfTemplate.getTemplate().getType().getObjectID());
            }
            transObject.setContent(companyPdfTemplate.getTemplate().getContent());
            transObject.setHeader(companyPdfTemplate.getTemplate().getHeader());
            transObject.setFooter(companyPdfTemplate.getTemplate().getFooter());
        }
        return transObject;
    }

    @Transactional
    public boolean deletePDFTemplate(Integer companyID, Integer companyPDFTemplateID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompanyPdfTemplate companyPdfTemplate = companyPdfTemplateManager.get(companyPDFTemplateID);
        if (companyPdfTemplate != null) {
            companyPdfTemplate.setDeleted(true);
            companyPdfTemplateManager.update(companyPdfTemplate);
            EdsPdfTemplate pdfTemplate = companyPdfTemplate.getTemplate();
            if (pdfTemplate != null) {
                pdfTemplate.setDeleted(true);
                pdfTemplateManager.update(pdfTemplate);
            }
            companyPdfTemplateManager.flush();
            return true;
        }
        return false;
//        ServerSecurityContext.getInstance().removeCompanyId();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PDFSettingsTransObject getCustomFormItemList(Integer companyId, String module) {
        String defaultCompanyid = ServerSecurityContext.getInstance().getCompanyId();
        if (companyId != null) {
            ServerSecurityContext.getInstance().setCompanyId(companyId);
        }
        PDFSettingsTransObject transObject = new PDFSettingsTransObject();
        transObject.setCustomFormItems(getCustomFormItems(module));
        ServerSecurityContext.getInstance().setCompanyId(defaultCompanyid);
        return transObject;
    }

    public String saveInvoiceLogoSize(Integer width, Integer height, Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsInvoicingSettings settings = invoicingSettingsManager.getInvoiceSettings(companyManager.get(companyID));
        if (settings != null) {
            settings.setInvoiceLogoWidth(width);
            settings.setInvoiceLogoHeight(height);
            return "Invoice PDF Logo Size saved";
        } else {
            return "There is no settings to save Logo Size";
        }
    }

    public String savePdfLogoSize(Integer width, Integer height, Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompanySettings settings = companyManager.get(companyID).getCompanySettings();
        if (settings != null) {
            settings.setPdfLogoWidth(width);
            settings.setPdfLogoHeight(height);
            return "PDF Logo Size saved";
        } else {
            return "There is no settings to save Logo Size";
        }
    }

    public String getCompanyLogoURL(Integer companyID, String logoType) {
        EdsCompany company = companyManager.getCompany(companyID);
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        return companyAttachmentManager.getCompanyLogoUrl(company, logoType);
    }

    @Override
    public String backupSchema(Integer companyID) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder pgdump = new StringBuilder();
        //pg_dump -Fc -n "0" -U postgres multischematest > wfmAPPReal-2011-02-13-0.backup
        pgdump.append("pg_dump -Fc -n ");
        pgdump.append("\"" + companyID + "\"");
        pgdump.append(" -U postgres ");
        pgdump.append(" multischemafree > /mnt/lochin/wfmAPPReal-");
        pgdump.append(formatter.format(new Date()));
        pgdump.append("-");
        pgdump.append(companyID.toString());
        pgdump.append(".backup");

        try {
            Process p = Runtime.getRuntime().exec(pgdump.toString());//Windows command, use "ls -oa" for UNIX
            Scanner sc1 = new Scanner(p.getErrorStream());
            while (sc1.hasNext()) {
                System.out.println(sc1.nextLine());
            }
            Scanner sc = new Scanner(p.getInputStream());
            while (sc.hasNext()) {
                System.out.println(sc.nextLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pgdump.toString();
    }

    @Override
    @Transactional
    public void setCompanyUnderMaintenance(Integer companyID) {
        globalAuthJdbcSpringManager.changeMaintenanceStatus(companyID);
    }

    @Override
    @Transactional
    public Integer createSchemasSecond(Integer count, boolean backupZeroSchema) throws IOException {
        String backupName = "multischema-0.backup";
        String pg_dump = "pg_dump -Fc -n \"0\" -U postgres multischematest --file " + backupName;
        String pg_restore = "pg_restore --host localhost --port 5432 -U postgres multischematest  --verbose " + backupName;

        if (backupZeroSchema) {
            Process p;
            try {
                p = Runtime.getRuntime().exec(pg_dump);
            } catch (IOException e) {
                throw e;
            }

            Scanner sc1 = new Scanner(p.getErrorStream());
            while (sc1.hasNext()) {
                System.out.println(sc1.nextLine());
            }

            Scanner sc = new Scanner(p.getInputStream());
            while (sc.hasNext()) {
                System.out.println(sc.nextLine());
            }
        }

        int created = 0;
        if (count != null && count > 0) {
            for (int i = 0; i < count; i++) {
                if (!companyManager.schemaExists("0")) {
                    Process p;//Windows command, use "ls -oa" for UNIX
                    try {
                        p = Runtime.getRuntime().exec(pg_restore);
                    } catch (IOException e) {
                        throw e;
                    }

                    Scanner sc1 = new Scanner(p.getErrorStream());
                    while (sc1.hasNext()) {
                        System.out.println(sc1.nextLine());
                    }

                    Scanner sc = new Scanner(p.getInputStream());
                    while (sc.hasNext()) {
                        System.out.println(sc.nextLine());
                    }

                }
                TransactionDefinition def = new DefaultTransactionDefinition();
                TransactionStatus status = transactionManager.getTransaction(def);
                try {
                    Integer expectedID = (Integer) companyManager.findSingle("select max(id) from EdsCompany") + 1;
                    EdsCompany company = new EdsCompany();
                    company.setObjectID(expectedID);
                    company.setFree(true);
                    companyManager.create(company);
                    companyManager.update("ALTER SCHEMA \"0\" RENAME TO \"" + company.getObjectID() + "\"");
                    transactionManager.commit(status);

                } catch (DataAccessException e) {
                    transactionManager.rollback(status);
                    throw e;
                }
            }
        }
        return created;
    }

    @Transactional
    @Override
    public boolean updateReport(ReportingListItem reportListItem) {
        return coreService.updateReportTemplate(reportListItem);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RecurrenceLogList getRecurrenceHistory(ListingFilterParameter filterParametrs) {
        List<EdsRecurrenceHistory> recurrenceHistoryList = recurrenceHistoryManager.list(filterParametrs);
        RecurrenceLogList recurrenceLogList = new RecurrenceLogList();
        recurrenceLogList.setTotal(recurrenceHistoryList.size());
        if (filterParametrs.getLimit() > 0) {
            recurrenceHistoryList = ListUtils.getSublist(recurrenceHistoryList, filterParametrs.getStart(), filterParametrs.getLimit());
        }

        ArrayList<RecurrenceLogItem> recurrenceLogItems = new ArrayList<>();
        for (EdsRecurrenceHistory history : recurrenceHistoryList) {
            RecurrenceLogItem recurrenceLogItem = new RecurrenceLogItem();
            recurrenceLogItem.setObjectID(history.getObjectID());
            recurrenceLogItem.setJobName(history.getJobName());
            recurrenceLogItem.setJobType(history.getJobType());
            recurrenceLogItem.setCronExpression(history.getCronExpression());
            recurrenceLogItem.setNormalFireTime(history.getNormalFireTime());
            recurrenceLogItem.setLateFireTime(history.getLateFireTime());
            recurrenceLogItem.setRecurrenceID(history.getRecurrenceID());
            recurrenceLogItem.setFired(history.getFired());
            recurrenceLogItem.setCompanyID(history.getCompanyID());
            recurrenceLogItems.add(recurrenceLogItem);
        }

        recurrenceLogList.setList(recurrenceLogItems);
        ListPanelToolRpc panelSettings = filterParametrs.getListPanelTool();
        if (panelSettings == null) {
            ArrayList<String> columnCodeName = new ArrayList<>();
            columnCodeName.add(RecurrenceLogItem.JOBTYPE);
            columnCodeName.add(RecurrenceLogItem.CRONEXPRESSION);
            columnCodeName.add(RecurrenceLogItem.NORMALFIRETIME);
            columnCodeName.add(RecurrenceLogItem.LATEFIRETIME);
            columnCodeName.add(RecurrenceLogItem.ISFIRED);
            columnCodeName.add(RecurrenceLogItem.RECURRENCEID);
            columnCodeName.add(RecurrenceLogItem.COMPANYID);
            panelSettings = new ListPanelToolRpc();
            panelSettings.setColumnCodeName(columnCodeName);
        }
        return recurrenceLogList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RecurrenceLogList getServerHistory(ListingFilterParameter filterParameter) {
        List<EdsServerHistory> serverHistoryList = serverHistoryManager.list(filterParameter);
        RecurrenceLogList recurrenceLogList = new RecurrenceLogList();
        recurrenceLogList.setTotal(serverHistoryList.size());
        if (filterParameter.getLimit() > 0) {
            serverHistoryList = ListUtils.getSublist(serverHistoryList, filterParameter.getStart(), filterParameter.getLimit());
        }

        ArrayList<RecurrenceLogItem> recurrenceLogItems = new ArrayList<>();
        for (EdsServerHistory history : serverHistoryList) {
            if (history.getDownTimeFrom() != null) {
                RecurrenceLogItem recurrenceLogItem = new RecurrenceLogItem();
                recurrenceLogItem.setDownTimeFrom(history.getDownTimeFrom());
                recurrenceLogItem.setDownTimeTo(history.getDownTimeTo());
                Long recCount = recurrenceHistoryManager.getLateRecurrencesInThisSeries(history);
                recurrenceLogItem.setLateRecurrenceCount(recCount != null ? recCount.intValue() : 0);
                recurrenceLogItem.setCatchUp(recurrenceLogItem.getLateRecurrenceCount() == 0 || history.getCatchUp());
                recurrenceLogItems.add(recurrenceLogItem);
            }
        }
        recurrenceLogList.setList(recurrenceLogItems);
        ListPanelToolRpc panelSettings = filterParameter.getListPanelTool();
        if (panelSettings == null) {
            ArrayList<String> columnCodeName = new ArrayList<>();
            columnCodeName.add(RecurrenceLogItem.DOWNTIMEFROM);
            columnCodeName.add(RecurrenceLogItem.DOWNTIMETO);
            columnCodeName.add(RecurrenceLogItem.CATCHUP);
            columnCodeName.add(RecurrenceLogItem.LATERECCOUNT);
            panelSettings = new ListPanelToolRpc();
            panelSettings.setColumnCodeName(columnCodeName);
        }
        return recurrenceLogList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RecurrenceLogList getRecurrenceJobItems(ListingFilterParameter filterParametrs) {
        RecurrenceLogList recurrenceLogList = new RecurrenceLogList();
        ArrayList<RecurrenceLogItem> recurrenceLogItems = recurrenceService.getRecurrenceJobItems();
        recurrenceLogList.setTotal(recurrenceLogItems.size());
        recurrenceLogList.setList(recurrenceLogItems);

        ListPanelToolRpc panelSettings = filterParametrs.getListPanelTool();
        if (panelSettings == null) {
            ArrayList<String> columnCodeName = new ArrayList<>();
            columnCodeName.add(RecurrenceLogItem.JOBTYPE);
            columnCodeName.add(RecurrenceLogItem.CRONEXPRESSION);
            columnCodeName.add(RecurrenceLogItem.NORMALFIRETIME);
            columnCodeName.add(RecurrenceLogItem.LATEFIRETIME);
            columnCodeName.add(RecurrenceLogItem.ISFIRED);
            columnCodeName.add(RecurrenceLogItem.RECURRENCEID);
            columnCodeName.add(RecurrenceLogItem.COMPANYID);
            panelSettings = new ListPanelToolRpc();
            panelSettings.setColumnCodeName(columnCodeName);
        }
        return recurrenceLogList;
    }

    /**
     * <h1>... THIS IS METHOD CHECKED CASES INCONSISTENCE ...</h1>
     * <br/>
     * <h2>... METHOD WRITE BY DEVELOPER - {DILSHOD.T} ...<h2>
     * <br/>
     * <h3>... METHOD CREATED DATE {15:28 28/04/2011}  ...</h3>
     * <br/>
     *
     * @param companyID
     */
    @Override
    public void fixCrmCaseInconsistencies(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            SecurityContext.setCompanyID(companyID);
            fixCaseInconsistenciesInSolr(companyID);
            fixCaseInconsistenciesInDb(companyID);
            companyManager.flushAndClear();
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (Integer company : EdsObject.getObjectIDs(companies)) {
                if (company != null && schemaList.contains(company.toString())) {
                    SecurityContext.setCompanyID(company);
                    fixCaseInconsistenciesInSolr(company);
                    fixCaseInconsistenciesInDb(company);
                    companyManager.flushAndClear();
                }
            }
        }
    }

    /**
     * <h1>... THIS IS FIX CASE INCONSISTENCIES IN DATA BASE ...</h1>
     * <br/>
     * <h2>... METHOD WRITE BY DEVELOPER - {DILSHOD.T} ...<h2>
     * <br/>
     * <h3>... METHOD CREATED DATE {16:05 28/04/2011}  ...</h3>
     * <br/>
     *
     * @param companyID
     */
    private void fixCaseInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing DB - >Crm Case SOLR inconsistences started for companyID = " + companyID);
        Integer start = 1;
        // first iteratively will fix project inconsistencies in DB
        try {
            while (start != -1) {
                start = fixCaseInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix DB - >Crm Case SOLR inconsistence for companyID = " + companyID);
        }
    }

    /**
     * <h1>... THIS IS FIX CASE INCONSISTENCIES IN DATA BASE ...</h1>
     * <br/>
     * <h2>... METHOD WRITE BY DEVELOPER - {DILSHOD.T} ...<h2>
     * <br/>
     * <h3>... METHOD CREATED DATE {16:58 28/04/2011}  ...</h3>
     * <br/>
     *
     * @param companyID
     * @param start
     * @return
     */
    private Integer fixCaseInconsistenciesInDb(Integer companyID, Integer start) {
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        /// retrives 10 inconsistencies
        List<EdsSolrDbConsistency> contactDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.CASE, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, start, limit);
        if (contactDbInconsistencies.isEmpty()) {
            return -1; // if there are no inconsistencies
        }
        /// add batch tasks using batch add 100
        List<Integer> ids = new ArrayList<>();
        for (EdsSolrDbConsistency sdb : contactDbInconsistencies) {
            ids.add(sdb.getEntityID());
        }
        if (!ids.isEmpty()) {
            List<EdsCase> cases = caseManager.getCasesByIDs(ids);
            try {
                caseSolrComponent.indexConcurrently(cases);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (EdsSolrDbConsistency sdb : contactDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed cases of Company ID=" + companyID + " DB inconsistency caseIDs (" + ServerUtils.getAsCommoDelimited(ids, "yedi") + ")");
        EdsSolrDbConsistency lastOne = contactDbInconsistencies.get(contactDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    /**
     * <h1>... THIS IS FIX CASE INCONSISTENCIES IN SOLR ...</h1>
     * <br/>
     * <h2>... METHOD WRITE BY DEVELOPER - {DILSHOD.T} ...<h2>
     * <br/>
     * <h3>... METHOD CREATED DATE {16:03 28/04/2011}  ...</h3>
     *
     * @param companyID -- companyID
     */
    private void fixCaseInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR Case - > DB inconsistences started for companyID = " + companyID);
        Integer start = 1;
        // first iteratively will fix project inconsistencies in Solr
        try {
            while (start != -1) {
                start = fixCaseInconsistenciesInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Case SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    /**
     * <h1>... THIS IS FIX CASE INCONSISTENCIES IN SOLR ...</h1>
     * <br/>
     * <h2>... METHOD WRITE BY DEVELOPER - {DILSHOD.T} ...<h2>
     * <br/>
     * <h3>... METHOD CREATED DATE {16:40 28/04/2011}  ...</h3>
     *
     * @param companyID
     * @param start
     * @return
     */
    private Integer fixCaseInconsistenciesInSolr(Integer companyID, Integer start) {
        int limt = 100;
        //retrives 100 inconsistences
        List<EdsSolrDbConsistency> contactSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.CASE, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, start, limt);
        if (contactSolrInconsistencies.size() == 0) {
            return -1;// if there are no inconsistences
        }
        StringBuilder sb = new StringBuilder();

        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrCaseRepresenter.COMPANY_ID + ":" + companyID + " AND " + SolrCaseRepresenter.CASE_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_CASE_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : contactSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixeds company=" + companyID + " Crm Case solr inconsistency fileids(" + sb + ")");
        EdsSolrDbConsistency sdb = contactSolrInconsistencies.get(contactSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    /**
     * <h1>... THIS METHOD REINDEXING ALL CASES IN SOLR REPOSITORY ...</h1>
     * <br/>
     * <h2>... METHOD WRITE BY DEVELOPER - {DILSHOD.T} ...<h2>
     * <br/>
     * <h3>... METHOD CREATED DATE {14:16 28/04/2011}  ...</h3>
     *
     * @param solrReindex
     */
    @Transactional
    public void indexCompanyCrmCase(SolrReindexRpc solrReindex) {
        if (solrReindex.getCompanyId() == null || solrReindex.getCompanyId() == 0) {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (schemas.contains(company.getObjectID().toString())) {
                    solrReindex.setCompanyId(company.getObjectID());
                    transactionHelper.runInANewTransaction(() -> crmServiceLocal.indexCompanyCrmCase(solrReindex));
                }
            }
        } else {
            crmServiceLocal.indexCompanyCrmCase(solrReindex);
        }
    }

    /**
     * <h1>... THIS IS METHOD ANALAZY CASES IN SOLR REPOSITORY AND IN DATA BASE IN CONSISTENCIES ...</h1>
     * <br/>
     * <h2>... METHOD WRITE BY DEVELOPER - {DILSHOD.T} ...<h2>
     * <br/>
     * <h3>... METHOD CREATED DATE {14:21 28/04/2011}  ...</h3>
     *
     * @param companyID
     */
    @Override
    public void analazyCrmCaseInconsistencies(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.CASE);
            companyManager.flushAndClear();
            analyzeCaseSolrDbInconsistencies(companyID);
            analyzeCaseDbSolrInconsistencies(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemas)) {
                    solrDbConsistencyManager.removeInconsistences(company.getObjectID(), EdsSolrDbConsistency.CASE);
                    companyManager.flushAndClear();
                    analyzeCaseSolrDbInconsistencies(company.getObjectID());
                    analyzeCaseDbSolrInconsistencies(company.getObjectID());
                } else {
                    solrDbConsistencyManager.removeInconsistences(company.getObjectID(), EdsSolrDbConsistency.CASE);
                    companyManager.flushAndClear();
                }
            }
        }
    }

    /**
     * <h1>... THIS IS METHOD ANALYZE DABA BASE TO SOLR INCONSISTENCIES ...</h1>
     * <br/>
     * <h2>... METHOD WRITE BY DEVELOPER - {DILSHOD.T} ...<h2>
     * <br/>
     * <h3>... METHOD CREATED DATE {14:26 28/04/2011}  ...</h3>
     *
     * @param companyID
     */
    @Transactional
    public void analyzeCaseDbSolrInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CASE_CORE);
        List<Integer> caseIdList = caseManager.getCompanyCaseIdList(companyID, startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();

        try {
            while (caseIdList.size() != 0) {
                nonExisting.addAll(caseIdList);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrCaseRepresenter.COMPANY_ID + ":" + companyID + " AND " + SolrCaseRepresenter.CASE_ID + ":(" + ServerUtils.getAsCommoDelimited(caseIdList, "0", " ") + ")");
                sQuery.addField(SolrCaseRepresenter.CASE_ID);
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer networkId = Integer.valueOf(sd.getFieldValue(SolrCaseRepresenter.CASE_ID).toString());
                    nonExisting.remove(networkId);
                }

                caseIdList = caseManager.getCompanyCaseIdList(companyID, caseIdList.get(caseIdList.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.CASE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(companyID);
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                log.info("Crm Case with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    /**
     * <h1>... THIS IS METHOD ANALYZE SOLR TO DATA BASE INCONSISTENCIES ...</h1>
     * <br/>
     * <h2>... METHOD WRITE BY DEVELOPER - {DILSHOD.T} ...<h2>
     * <br/>
     * <h3>... METHOD CREATED DATE {14:28 28/04/2011}  ...</h3>
     *
     * @param companyID
     */
    @Transactional
    public void analyzeCaseSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CASE_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrCaseRepresenter.COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrCaseRepresenter.CASE_ID);

        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrCaseRepresenter.CASE_ID).toString());
                    Integer caseId = Integer.valueOf(sd.getFieldValue(SolrCaseRepresenter.CASE_ID).toString());
                    nonExisting.put(caseId, sd);

                }
                List<Integer> casesIds = caseManager.getCasesIdsByIds(ids.toString());
                for (Integer id : casesIds) {
                    nonExisting.remove(id);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery);
                companyManager.flushAndClear();

            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrCaseRepresenter.CASE_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.CASE);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(companyID);
                sdb.setAnalizedate(startDate);
                System.out.println("Crm Case with id" + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    @Override
    public void copyUsagePlansToLoginDispatcher() {
        EdsReference freeTrial = referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.FREE_TRIAL);
        EdsReference expired = referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.EXPIRED);
        //copy all edsusageplan
        List<EdsUsagePlan> edsUsagePlanList = usagePlanManager.getAllPaidUsagePlans(freeTrial, expired);
        globalAuthJdbcSpringManager.insertEdsUsagePlanList(edsUsagePlanList);

        //copy all edssubscriptionhistory
        List<EdsSubscriptionHistory> edsSubscriptionHistoryList = subscriptionHistoryManager.getAllSubscriptionHistoryList(freeTrial, expired);
        globalAuthJdbcSpringManager.insertEdsSubscriptionHistoryList(edsSubscriptionHistoryList);

    }

    @Override
    public void registerCompanyToLoginDispatcher(Integer companyId) {
        List<Integer> companyList = new ArrayList<>();
        if (companyId == null) {
            List<Object[]> companies = companyManager.getSchemaList(new ListingFilterParameter());
            for (Object[] company : companies) {
                companyList.add(Integer.valueOf(company[0].toString()));
            }
        } else {
            companyList.add(companyId);
        }
        List<CompanyidDomain> cDomains = new ArrayList<>();
        for (Integer companyid : companyList) {
            CompanyidDomain cd = new CompanyidDomain();
            cd.companyid = companyid;
            if (companySystemSettingsManager.findByCompanyID(companyid) != null) {
                cd.domain = companySystemSettingsManager.findByCompanyID(companyid).getGoogleAppDomain();
            } else {
                cd.domain = "";
            }
            cDomains.add(cd);
        }
        globalAuthJdbcSpringManager.registerCompanyToLoginDispatcherDummy(cDomains);

    }

    @Override
    public void clearHostSettings() {
        EdsContextParams.clearHostSetting();
        EdsMailer.clearHostSetting();
    }

    @Transactional
    @Override
    public ListResult<ReportsListItem> getReportTemplateList(boolean isCustom, ListingFilterParameter filterParameter) {
        ArrayList<EdsReportTemplate> reportTemplateList = reportTemplateManager.getReportTemplateList(isCustom, filterParameter);
        Integer totalCount = reportTemplateManager.getReportTemplateIds(isCustom).size();
        ArrayList<ReportsListItem> items = new ArrayList<>();
        if (reportTemplateList != null && reportTemplateList.size() > 0) {
            for (EdsReportTemplate template : reportTemplateList) {
                ReportsListItem item = new ReportsListItem();
                item.setReportID(template.getObjectID());
                item.setViewName(template.getCategoryCode());
                item.setName(template.getName());
                items.add(item);
            }
        }
        if (totalCount == null) {
            totalCount = 0;
        }
        return new ListResult<>(items, totalCount);
    }

    @Transactional
    @Override
    public ListResult<ReportsListItem> getReportsListCollectin(ListingFilterParameter filterParametrs) {
        Integer mySchema = filterParametrs.getCompanyID();
        StringBuffer stringBuffer = new StringBuffer();

        if (filterParametrs.isCleanTheList() && mySchema != null) {
            try {
                coreService.executeNative("ALTER TABLE \"" + mySchema + "\".reporting DROP CONSTRAINT reporting_code_unique cascade");
            } catch (Exception exp) {
                exp.printStackTrace();
            }
            try {
                coreService.executeNative("ALTER TABLE \"" + mySchema + "\".reporting DROP CONSTRAINT reporting_code_key cascade");
            } catch (Exception exp) {
                exp.printStackTrace();
            }
            coreService.executeNative("delete from \"" + mySchema + "\".companyfavouritereporttemplates; delete from \"" + mySchema + "\".reporting;");
        }

        Integer totalCount = coreService.getReportsNativeCount(filterParametrs);
        if ((totalCount == null || totalCount == 0) && mySchema != null) {
            makeReportingTestSchema(filterParametrs, mySchema, stringBuffer);
        }

        ArrayList<ReportsListItem> items = new ArrayList<>();
        totalCount = getReportingList(filterParametrs, mySchema, items);
        if (totalCount == null) {
            totalCount = 0;
        }
        return new ListResult<>(items, totalCount);
    }

    private ReportsListItem getReportsListItem(Integer mySchema, String[] myReportsList) {
        ReportsListItem item = new ReportsListItem();
        item.setId(Integer.valueOf(myReportsList[0]));
        item.setName(myReportsList[1]);
        if (!(myReportsList.length < 3 || myReportsList[2] == null || "".equals(myReportsList[2]))) {
            item.setCompanyID(Integer.valueOf(myReportsList[2]));
        } else {
            item.setCompanyID(mySchema);
        }
        if (!(myReportsList.length < 4 || myReportsList[3] == null || "".equals(myReportsList[3]))) {
            item.setException(myReportsList[3]);
        } else {
            item.setException("");
        }
        if (!(myReportsList.length < 5 || myReportsList[4] == null || "".equals(myReportsList[4]))) {
            item.setSuccess("t".equalsIgnoreCase(myReportsList[4]));
        } else {
            item.setSuccess(null);
        }
        if (!(myReportsList.length < 6 || myReportsList[5] == null || "".equals(myReportsList[5]))) {
            item.setViewName(myReportsList[5]);
        } else {
            item.setViewName(null);
        }
        if (!(myReportsList.length < 7 || myReportsList[6] == null || "".equals(myReportsList[6]))) {
            item.setDefault("t".equalsIgnoreCase(myReportsList[6]));
        } else {
            item.setDefault(false);
        }
        return item;
    }

    @Transactional
    public Integer getReportingList(ListingFilterParameter filterParametrs, Integer mySchema, ArrayList<ReportsListItem> items) {
        Integer totalCount;
        ArrayList<String[]> reports = coreService.getReportsNative(filterParametrs);
        totalCount = coreService.getReportsNativeCount(filterParametrs);
        if (reports != null) {
            for (String[] myReportsList : reports) {
                items.add(getReportsListItem(mySchema, myReportsList));
            }
        }
        return totalCount;
    }

    @Transactional
    public void makeReportingTestSchema(ListingFilterParameter filterParametrs, Integer mySchema, StringBuffer stringBuffer) {
        if (null != filterParametrs.getParams() || "copy".equals(filterParametrs.getParams())) {
            coreService.makeTestingReportSchema(mySchema);
            stringBuffer.append(" insert into \"" + mySchema + "\".folders (companyid,createdate,domainname,name,showhide,type,userid) " +
                    " select " + mySchema + ", now(),'#','test','true','Public',1; ");
            String reportingColumns = EdsReport.wrapper();

            for (String company : companyManager.getExistingSchemas()) {
                Integer companyID = Integer.valueOf(company);
                if (companyID.equals(mySchema)) {
                    continue;
                }
                stringBuffer.append("insert into \"" + mySchema + "\".reporting (" + reportingColumns + ",companyid,folderid) select " + reportingColumns + "," + companyID + ",1 from \"" + companyID + "\".reporting where deleted is not true AND viewcode in (select code from reportTemplate where isLibrary is not true); ");
            }
            coreService.executeNative(stringBuffer.toString());
        }
    }

    @Transactional
    @Override
    public void runReport(HashSet<ReportsListItem> listItems, Integer companyid, Date testedDate) {
        List<Integer> companyList;
        Boolean isPaid = switch (companyid) {
            case -2 -> true;
            case -1 -> false;
            case 0 -> null;
            default -> null;
        };

        if (isPaid != null) {
            companyList = companyManager.getCompaniesIdsList(isPaid);
            if (companyList.size() > 0) {
                for (Integer comID : companyList) {
                    for (ReportsListItem reportItem : listItems) {
                        try {
                            runSingleReport(comID, reportItem);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } else if (isPaid == null && companyid == 0) {
            companyList = companyManager.getCompaniesIdsList(null);
            if (companyList.size() > 0) {
                for (Integer comID : companyList) {
                    for (ReportsListItem reportItem : listItems) {
                        try {
                            runSingleReport(comID, reportItem);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } else {
            for (ReportsListItem reportItem : listItems) {
                try {
                    runSingleReport(companyid, reportItem);
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    @Transactional
    public void runSingleReport(Integer companyid, ReportsListItem reportItem) {
        ReportingTestDTO testDTO = new ReportingTestDTO();
        EdsUser modifiedBy = userManager.getUser();
        testDTO.setModifiedBy(modifiedBy.getFullName());
        testDTO.setUserName(userManager.getAdmin(companyid).getUserName());
        testDTO.setReportID(reportItem.getReportID());
        testDTO.setCompanyID(companyid);
        testDTO.setReportName(reportItem.getName());
        testDTO.setModuleName(reportItem.getViewName());

        HashMap<String, String> map = new HashMap<>();
        try {
            ReportRpc report = reportingService.getReportStructure(reportItem.getReportID());
            report.setBrowserTimeZone("GMT+05:00");
            try {
                ResultSet result;
                ReportGenerateTableRpc reportGenerateTableRpc = new ReportGenerateTableRpc();
                try {
                    if (ReportType.TABULAR.name().equals(report.getTableType())) {
                        log.info("====================== Report " + report.getName() + " started for company " + companyid + "======================");
                        long startedAt = System.currentTimeMillis();
                        testDTO.setTestedDate(new Date(startedAt));
                        result = reportingServiceLocal.getTabularReportResult(report, userManager.getSchemaAllUsers(String.valueOf(companyid), 1).get(0).getObjectID());
                        long finishedIn = System.currentTimeMillis() - startedAt;
                        log.info("====================== Report " + report.getName() + " started!");
                        testDTO.setTimeSpent(TimeUnit.MILLISECONDS.toSeconds(finishedIn));
                    } else {
                        log.info("====================== Report " + report.getName() + " started for company " + companyid + "======================");
                        long startedAt = System.currentTimeMillis();
                        testDTO.setTestedDate(new Date(startedAt));
                        result = reportingServiceLocal.getSummaryReportResult(report, userManager.getSchemaAllUsers(String.valueOf(companyid), 1).get(0).getObjectID());
                        long finishedIn = System.currentTimeMillis() - startedAt;
                        log.info("====================== Report " + report.getName() + " started!");
                        testDTO.setTimeSpent(TimeUnit.MILLISECONDS.toSeconds(finishedIn));
                    }
                    result.next();
                } catch (Exception exp) {
                    reportGenerateTableRpc.setTextExceptionLog(getLog(exp));
                }
                if (reportGenerateTableRpc.getTextExceptionLog() != null) {
                    testDTO.setSuccess(false);
                    testDTO.setLastException("<br/>" + reportGenerateTableRpc.getTextExceptionLog());
                } else {
                    testDTO.setSuccess(true);
                    testDTO.setLastException("<br/>" + "success");
                    System.out.print(report.getName() + " Success");
                }
            } catch (Exception exp) {
                testDTO.setSuccess(false);
                testDTO.setLastException(exp + "<br/>" + getLog(exp));
            }
        } catch (Exception exp) {
            testDTO.setSuccess(false);
            testDTO.setLastException(exp + "<br/>" + getLog(exp));
        }
        coreService.executeNative(reportingManager.setParametersNative(testDTO, companyid));
    }

    private StringBuffer getLog(Exception ex) {
        StringBuffer sb = new StringBuffer();
        for (StackTraceElement error : ex.getStackTrace()) {
            sb.append(error).append("<br/>");
        }
        return sb;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<SolrMonitorRpc> getSolrMonitorStatistic(ListingFilterParameter filterParametrs) {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore("");
        String[] solrCores = new String[]{Constants.SOLR_TASK_CORE, Constants.SOLR_PROJECT_CORE, Constants.SOLR_PURCHASE_ORDER_CORE,
                Constants.SOLR_FOLDER_CORE, Constants.SOLR_CONTACT_CORE, Constants.SOLR_CASE_CORE, Constants.SOLR_CRM_ACCOUNT_CORE,
                Constants.SOLR_SALEINVOICE_CORE, Constants.SOLR_SALEQUOTE_CORE, Constants.SOLR_NEWS_CORE,
                Constants.SOLR_OPPORTUNITY_CORE, Constants.SOLR_EVENT_CORE, Constants.SOLR_PRODUCTS_SERVICES_CORE,
                Constants.SOLR_PURCHASE_INVOICE_CORE, Constants.SOLR_EXPENSE_REPORT_CLAIMS_CORE, Constants.SOLR_VACANCY_CORE};
        ArrayList<SolrMonitorRpc> solrMonitorList = new ArrayList<>();
        try {
            for (String coreName : solrCores) {
                SolrMonitorRpc solrMonitor = new SolrMonitorRpc();
                solrMonitorList.add(solrMonitor);
                NamedList<Object> coreStatus = CoreAdminRequest.getStatus(coreName, solrServer).getCoreStatus(coreName);
                solrMonitor.setCoreId(solrMonitorList.size());
                solrMonitor.setCoreName(coreStatus.get("name").toString());
                solrMonitor.setStartTime((Date) coreStatus.get("startTime"));
                SimpleOrderedMap simpleOrderedMap = ((SimpleOrderedMap) coreStatus.get("index"));
                if (simpleOrderedMap != null) {
                    solrMonitor.setLastModified((Date) simpleOrderedMap.get("lastModified"));
                    solrMonitor.setNumDocs(Integer.parseInt(simpleOrderedMap.get("numDocs").toString()));
                    solrMonitor.setFileSize(simpleOrderedMap.get("size").toString());
                }
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return new ListResult<>(solrMonitorList, solrMonitorList.size());
    }

    @Override
    public void deleteCompanyInSoreCore(String coreName, Integer companyId) {
        try {
            if (Constants.SOLR_TASK_CORE.equals(coreName)) {
                solrManager.removeCompanyTasks(companyId);
            } else if (Constants.SOLR_PROJECT_CORE.equals(coreName)) {
                solrManager.removeCompanyProjects(companyId);
            } else if (Constants.SOLR_PURCHASE_ORDER_CORE.equals(coreName)) {
                solrManager.removeCompanyPurchaseOrder(companyId);
            } else if (Constants.SOLR_FOLDER_CORE.equals(coreName)) {
                solrManager.removeCompanyFolders(companyId);
            } else if (Constants.SOLR_CONTACT_CORE.equals(coreName)) {
                solrManager.removeCompanyCrmContact(companyId);
            } else if (Constants.SOLR_CASE_CORE.equals(coreName)) {
                solrManager.removeCompanyCase(companyId);
            } else if (Constants.SOLR_CRM_ACCOUNT_CORE.equals(coreName)) {
                solrManager.removeCompanyCrmAccount(companyId);
            } else if (Constants.SOLR_SALEINVOICE_CORE.equals(coreName)) {
                solrManager.removeCompanySaleInvoice(companyId);
            } else if (Constants.SOLR_SALEQUOTE_CORE.equals(coreName)) {
                solrManager.removeCompanySaleQuote(companyId);
            } else if (Constants.SOLR_NEWS_CORE.equals(coreName)) {
                solrManager.removeCompanyNews(companyId);
            } else if (Constants.SOLR_OPPORTUNITY_CORE.equals(coreName)) {
                solrManager.removeCompanyOpportunity(companyId);
            } else if (Constants.SOLR_EVENT_CORE.equals(coreName)) {
                solrManager.removeCompanyEvents(companyId);
            } else if (Constants.SOLR_PRODUCTS_SERVICES_CORE.equals(coreName)) {
                solrManager.removeCompanyProductsServices(companyId);
            } else if (Constants.SOLR_PURCHASE_INVOICE_CORE.equals(coreName)) {
                solrManager.removeCompanyPurchaseInvoice(companyId);
            } else if (Constants.SOLR_EXPENSE_REPORT_CLAIMS_CORE.equals(coreName)) {
                solrManager.removeCompanyExpenseReportClaims(companyId);
            } else if (Constants.SOLR_SHIPPING_DATA_CORE.equals(coreName)) {
                solrManager.removeShippingData(null, companyId);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<SelectItem> getSolrCoreByCompanyList(String coreName, ListingFilterParameter filterParameter) {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(coreName);
        StringBuilder solrQuery = new StringBuilder();
        if (filterParameter.getSearchKey() != null && !"".equals(filterParameter.getSearchKey())) {
            solrQuery.append(SolrTaskRepresenter.FIELD_COMPANY_ID + ":" + SolrSearchUtils.normalaizeKeyword(filterParameter.getSearchKey()));
        } else {
            solrQuery.append("*:*");
        }
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQuery(filterParameter.getFacetFilter(), null, null, null));

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(filterParameter.getStart());
        query.setRows(filterParameter.getLimit());
        query.setParam(GroupParams.GROUP, true);
        query.setParam(GroupParams.GROUP_TOTAL_COUNT, true);
        query.setParam(GroupParams.GROUP_FIELD, SolrTaskRepresenter.FIELD_COMPANY_ID);
        query.setFields(SolrTaskRepresenter.FIELD_COMPANY_ID);
        int total = 0;
        ArrayList<SelectItem> listItems = new ArrayList<>();
        try {
            List<String> schemaList = companyManager.getExistingSchemas();
            QueryResponse response = solrServer.query(query, SolrRequest.METHOD.POST);
            GroupCommand groupCommand = response.getGroupResponse().getValues().get(0);
            total = groupCommand.getNGroups();
            for (Group group : groupCommand.getValues()) {
                SelectItem item = new SelectItem();
                SolrDocumentList solrDocList = group.getResult();
                SolrDocument solrDoc = solrDocList.get(0);
                item.setId(Integer.parseInt(SolrUtils.asString(solrDoc, SolrTaskRepresenter.FIELD_COMPANY_ID)));
                item.setDescription(String.valueOf(solrDocList.getNumFound()));
                item.setSelected(!schemaList.contains(String.valueOf(item.getId()))); // not deleted in data base
                listItems.add(item);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        String companyIds = ServerUtils.getSelectItemIdAsCommaDelimeted(listItems.toArray(new SelectItem[]{}));
        List<EdsCompany> edsCompanyList = companyManager.getCompaniesByIDs(companyIds);
        Map<Integer, EdsCompany> companyMap = new HashMap<>();
        for (EdsCompany edsCompany : edsCompanyList) {
            companyMap.put(edsCompany.getObjectID(), edsCompany);
        }
        for (SelectItem item : listItems) {
            if (companyMap.containsKey(item.getId())) {
                item.setName(companyMap.get(item.getId()).getName());
            }
        }

        if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
            if ("numberDocs".equals(filterParameter.getSortField())) {
                final int comparator = filterParameter.isAscending() ? 1 : -1;
                listItems.sort((o1, o2) -> {
                    if (o1.getDescription() == null) {
                        return -1 * comparator;
                    }
                    if (o2.getDescription() == null) {
                        return comparator;
                    }
                    if (Integer.parseInt(o1.getDescription()) >= Integer.parseInt(o2.getDescription())) {
                        return comparator;
                    }
                    return -1 * comparator;
                });
            }
        } else {
            listItems.sort(Comparator.comparing(SelectItem::getId));
        }
        return new ListResult<>(listItems, total);
    }

    @Override
    public void optimizeSolrCore(String coreName) {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(coreName);
        try {
            solrServer.optimize(true, false);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public boolean createClientGroupsToClientContactForCompany(Integer companyID) {
        if (companyID == 0 || companyID == -1) {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (schemas.contains(company.getObjectID().toString())) {
                    ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());
                    List<EdsClientContact> companyClientContacts = clientContactManager.getAccessEnabledContactList();
                    if (companyClientContacts != null && !companyClientContacts.isEmpty()) {
                        for (EdsClientContact clientContact : companyClientContacts) {
                            initClientGroup(clientContact, company.getObjectID());
                        }
                    }
                }
            }
            return true;
        } else {
            EdsCompany company = companyManager.get(companyID);
            if (company != null) {
                ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());
                List<EdsClientContact> companyClientContacts = clientContactManager.getAccessEnabledContactList();
                if (companyClientContacts != null && !companyClientContacts.isEmpty()) {
                    for (EdsClientContact clientContact : companyClientContacts) {
                        initClientGroup(clientContact, company.getObjectID());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ListResult<BugListItem> getBugLists(ListingFilterParameter fp) {
        List<EdsBugReport> bugList = bugReportManager.getBugLists(fp);
        int totalCount = bugList.size();

        ComparatorFactory factory = null;
        Boolean isCount = true;
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            factory = comparatorFactoriesBugList.get(fp.getSortField());
        }
        int sortDir = fp.getSortDir();
        if (factory == null) {
            factory = comparatorFactoriesBugList.get(BugListItem.UPDATE_TIME);
            sortDir = Constants.DESC;
        }
        bugList.sort(factory.createComparator(sortDir));

        if (fp.getLimit() > 0) {
            bugList = ListUtils.getSublist(bugList, fp.getStart(), fp.getLimit());
        }

        ArrayList<BugListItem> result = new ArrayList<>();

        for (EdsBugReport bug : bugList) {
            BugListItem bugListItem = new BugListItem();
            bugListItem.setBugId(bug.getObjectID() != null ? Integer.toString(bug.getObjectID()) : "");
            bugListItem.setBug(bug.getDescription() != null ? bug.getDescription() : "");
            bugListItem.setSubject(bug.getSubject() != null ? bug.getSubject() : "");
            bugListItem.setCreationTime(formatter.format(bug.getCreationTime()));
            bugListItem.setUpdateTime(bug.getUpdateTime() != null ? formatter.format(bug.getUpdateTime()) : "");
            bugListItem.setStatus(bug.getStatus() != null ? getUpperLowerCaseString(bug.getStatus()) : "");
            bugListItem.setPriority(bug.getPriority() != null ? getUpperLowerCaseString(bug.getPriority()) : "");
            bugListItem.setLabel(bug.getLabel() != null ? getUpperLowerCaseString(bug.getLabel()) : "");
            bugListItem.setCreatedFrom(bug.getCreatedFrom() != null ? bug.getCreatedFrom() : "");
            bugListItem.setCompany(bug.getCompany() != null ? (bug.getCompanyName() + " (CompanyID=" + bug.getCompany() + ") ") : "");
            bugListItem.setUser(bug.getCreatorName() != null ? bug.getCreatorName() : "");
            bugListItem.setAssignee(bug.getAssignName() != null ? bug.getAssignName() : "");
            bugListItem.setComment(bug.getComment() != null ? bug.getComment() : "");
            bugListItem.setEmail(bug.getEmail() != null ? bug.getEmail() : "");
            bugListItem.setBrowser(bug.getUserAgent() != null ? getUserAgentAccessLog(bug.getUserAgent()) : "");

            if (bug.getBugAttachments() != null && bug.getBugAttachments().size() > 0) {
                FileItem[] attachments = getAttachments(bug.getBugAttachments(), bug.getCompany());
                bugListItem.setAttachments(attachments);
            } else {
                bugListItem.setAttachments(new FileItem[0]);
            }
            if (bug.getBugHistory() != null && bug.getBugHistory().size() > 0) {
                bugListItem.setBugHistory(getBugHistories(bug.getBugHistory()));
            } else {
                bugListItem.setBugHistory(new BugListItem[0]);
            }
            if (bug.getComments() != null && bug.getComments().size() > 0) {
                bugListItem.setBugCommentsHistr(getBugComments(bug.getComments()));
            } else {
                bugListItem.setBugCommentsHistr(new BugComment[0]);
            }

            result.add(bugListItem);

        }

        return new ListResult<>(result, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<SelectItem> getBlackLists(ListingFilterParameter fp) {
        EdsUser user = userManager.getUser();
        EdsBackendManagement backendManagement = backendManagementManager.getBackendManagement(user.getCompany().getObjectID(), user.getObjectID());
        if (backendManagement != null) {
            fp.setParams(backendManagement.getHostNames());
        }
        List<EdsBlackList> blackLists = blackListManager.blackLists(fp);
        int totalCount = blackListManager.blackListsCount(fp);
        ArrayList<SelectItem> items = new ArrayList<>();
        for (EdsBlackList blackList : blackLists) {
            SelectItem selectItem = new SelectItem();
            selectItem.setId(blackList.getObjectID());
            selectItem.setName(blackList.getEmail());
            selectItem.setDescription(blackList.getHostName());
            items.add(selectItem);
        }
        return new ListResult<>(items, totalCount);
    }

    @Override
    public SelectItem[] getPaypalStatus(ListingFilterParameter fp) {
        List<EdsReference> statusList = referenceManager.listReferences("_PAYMENT_STATUS");
        SelectItem[] items = new SelectItem[statusList.size()];
        int i = 0;
        for (EdsReference status : statusList) {
            items[i] = status.getAsSelectItem();
            i++;
        }

        return items;

    }

    @Override
    public CompanyListItem getCompany(Integer companyID) {

        ListingFilterParameter fp = new ListingFilterParameter();
        EdsUser user = userManager.getUser();
        EdsBackendManagement backendManagement = backendManagementManager.getBackendManagement(user.getCompany().getObjectID(), user.getObjectID());
        if (backendManagement != null) {
            //Allows only from specified HOSTNAMES which set from Admin Backend
            fp.setParams(backendManagement.getHostNames());
        }
        fp.setCompanyID(companyID);

        CompanyListItem item;

        List<Object[]> companyStatistics = companyStatisticManager.getCompanyStatistics(fp, Boolean.FALSE, fp.getViewAsId(), fp.getBackendUsersId());
        HashMap<Integer, Integer> existingCompanies = new HashMap<>();
        for (Iterator i = companyStatistics.iterator(); i.hasNext(); ) {
            Object[] iObj = (Object[]) i.next();
            EdsCompanyStatistic cStatic = (EdsCompanyStatistic) iObj[0];

            if (existingCompanies.get(cStatic.getCompanyID()) != null) {
                i.remove();
            } else {
                existingCompanies.put(cStatic.getCompanyID(), cStatic.getCompanyID());
            }
        }
        if (companyStatistics.isEmpty()) {
            log.error("", "Company statistics are null from company: " + companyID);
            return null;
        }
        Object[] companyStatisticsT = companyStatistics.get(0);

        EdsCompanyStatistic cStatic = (EdsCompanyStatistic) companyStatisticsT[0];
        EdsUsagePlan usagePlan = (EdsUsagePlan) companyStatisticsT[1];
        EdsCompany company = companyManager.get(companyID);
        item = cStatic.getRPC_CompanyListItem();
        if (company != null) {
            item.setPromoCode(company.getPromoCode());
        }
        item.setUsagePlanPaymentStatus(usagePlan.getStatus() != null ? usagePlan.getStatus().getName() : "");
        item.setUsagePlanPaymentType(usagePlan.getPeriodType() != null ? usagePlan.getPeriodType().getName() : "");
        item.setPeriodStartDate(formatter.format(usagePlan.getStartDate()));
        item.setPeriodEndDate(formatter.format(usagePlan.getEndDate()));
        item.setUsagPlanEndDate(usagePlan.getEndDate());
        return item;
    }

    @Transactional
    public Boolean[] getChatActivities(Integer companyId) {
        EdsCompany edsCompany = companyManager.get(companyId);
        Boolean[] isActiveChats = new Boolean[2];
        if (edsCompany.getLiveDiscussionEnabled() != null) {
            isActiveChats[0] = edsCompany.getLiveDiscussionEnabled();
        } else {
            isActiveChats[0] = false;
        }
        if (edsCompany.getExpertPanelEnabled() != null) {
            isActiveChats[1] = edsCompany.getExpertPanelEnabled();
        } else {
            isActiveChats[1] = false;
        }
        return isActiveChats;
    }

    @Transactional
    public void saveChatActivities(Integer companyId, boolean isActiveLiveChat, boolean isActiveExpertChat) {
        EdsCompany edsCompany = companyManager.get(companyId);
        edsCompany.setLiveDiscussionEnabled(isActiveLiveChat);
        edsCompany.setExpertPanelEnabled(isActiveExpertChat);
        companyManager.update(edsCompany);
    }

    @Transactional
    public void initClientGroup(EdsUser client, Integer companyID) {
        if (ServerSecurityContext.getInstance().getCompanyId() == null ||
                !companyID.equals(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()))) {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
        }

        EdsTrustee userTrustee = trusteeManager.getTrustee(client);
        client.getMembershipGroups().clear();
        if (userTrustee == null) {
            userTrustee = trusteeManager.getTrustee(client);
        }
        if (client.getRoles() != null && client.getRoles().size() > 0) {
            for (EdsRole role : client.getRoles()) {
                if (EdsRole.CLIENT.equals(role.getObjectID())) {
                    EdsGroup clients = groupManager.getCompanyBuiltInGroup(EdsGroup.CLIENTS);
                    client.getMembershipGroups().add(clients);
                    clients.getMembers().add(userTrustee);
                }
            }
        } else if (client.isClientContact()) {
            EdsGroup clients = groupManager.getCompanyBuiltInGroup(EdsGroup.CLIENTS);
            client.getMembershipGroups().add(clients);
            clients.getMembers().add(userTrustee);
        }
    }

    public Boolean saveAccessToken(ApiAccessToken apiAccessToken) {
        return globalAuthJdbcSpringManager.saveAccessToken(apiAccessToken);
    }

    private String getConvertToUPPERCASE_CODE(String messageSubject) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return messageSubject.toUpperCase().replace('-', '_').replace('/', '_').replace(' ', '_') + "_" + dateFormat.format(currentDate).toUpperCase();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ApiAccessToken> getAccessTokenList(ListingFilterParameter fp) {
        return globalAuthJdbcSpringManager.getApiAccessTokenList(fp);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ApiAccessToken getAccessTokenByID(Integer id) {
        return globalAuthJdbcSpringManager.getApiAccessTokenByID(id);
    }

    @Transactional
    @Override
    public String exportSavedReports(final ListingFilterParameter filterParameter) {
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            final Integer companyID = filterParameter.getCompanyID();
            Integer[] objectIDs = filterParameter.getCategories();
            Integer[] companies = filterParameter.getCompaines();

            //Get from schema target Saved Reports
            ServerSecurityContext.getInstance().setCompanyId(companyID);
            EntityManager em = jpaTemplate.createHibernateEntityManager();
            Transaction tx = null;
            try (Session session = em.unwrap(Session.class)) {
                tx = session.getTransaction();
                tx.begin();

                final ArrayList<EdsReport> reports = new ArrayList<>();
                final HashMap<Integer, EdsUpload> uploadHashMap = new HashMap<>();
                final HashMap<Integer, EdsUploadSettings> uploadSettingsHashMap = new HashMap<>();
//                final HashMap<Integer, EdsChart> chartHashMap = new HashMap<>();
                final HashMap<Integer, EdsChartConfig> chartHashMap = new HashMap<>();
                final HashMap<Integer, EdsKpiWidget> kpiWidgetMap = new HashMap<>();
                for (Integer objectID : objectIDs) {
                    EdsReport edsReport = reportingManager.get(objectID);
                    if (edsReport != null) {
                        reports.add(edsReport);
                        if (null != edsReport.getExcelTemplateId()) {
                            EdsUpload edsUpload = (EdsUpload) uploadManager.get(edsReport.getExcelTemplateId());
                            uploadHashMap.put(edsReport.getExcelTemplateId(), edsUpload);
                            uploadSettingsHashMap.put(edsReport.getExcelTemplateId(), uploadManager.getUploadSettings(edsUpload));
                        }
                    /*if (null != edsReport.getChart()) {
                        edsReport.setTemp(edsReport.getChart().getObjectID());
                        chartHashMap.put(edsReport.getTemp(), edsReport.getChart());
                    }*/
                        if (null != edsReport.getChartConfig()) {
                            edsReport.setTemp(edsReport.getChartConfig().getObjectID());
                            chartHashMap.put(edsReport.getTemp(), edsReport.getChartConfig());
                        }
                        if (edsReport.getKpiWidget() != null) {
                            edsReport.setTempWidgetId(edsReport.getKpiWidget().getObjectID());
                            kpiWidgetMap.put(edsReport.getTempWidgetId(), edsReport.getKpiWidget());
                        }
                    }
                }
                tx.commit();

                final String dbName = ServerSecurityContext.getInstance().getDatabase();
                //Export from map to existing schema
                for (final Integer schema : companies) {
                    executor.execute(() -> {
                        try {
                            if (!schema.equals(companyID)) {
                                ServerSecurityContext.getInstance().setDatabase(dbName);
                                reportingServiceLocal.exportSavedReport(schema, reports, chartHashMap, uploadHashMap, uploadSettingsHashMap, kpiWidgetMap, Boolean.TRUE.equals(filterParameter.isSelected()));
                                log.info("<<<<<<<<<<<<<<<<<<<<<<CompanyID=" + schema + " Export Report Migration Successful complated !>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                ServerSecurityContext.getInstance().setDatabase("");
                            }
                        } catch (Exception e) {
                            stringBuilder.append("|   " + schema + "   ");
                            log.error("************************** CompanyID=" + schema + " Export Report Migration failed! **********************");
                        }
                    });
                }
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                log.error(" Get Saved Reporting Exception", e);
            } finally {
                em.close();
            }
        } catch (Exception e) {
            stringBuilder.append("Error!");
        }
        return stringBuilder.toString();
    }

    @Override
    public ListResult<SelectItem> getWorkspaceList(ListingFilterParameter filterParametrs) {
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        if (!(filterParametrs.getSearchKey() == null || "".equals(filterParametrs.getSearchKey()))) {
            EdsCompany edsCompany = companyManager.getCompany(Integer.valueOf(filterParametrs.getSearchKey()));
            if (edsCompany != null) {
                SelectItem selectItem = new SelectItem();
                selectItem.setId(edsCompany.getObjectID());
                selectItem.setName(edsCompany.getName());
                selectItem.setSelected(companySystemSettingsManager.findByCompanyID(edsCompany.getObjectID()).getShowDraggableWorkspace());
                selectItems.add(selectItem);
            }
            return new ListResult<>(selectItems, selectItems.size());
        } else {
            List<String> list = companyManager.getExistingSchemas();
            int start = (list.size() > filterParametrs.getStart()) ? filterParametrs.getStart() : list.size();
            int limit = (list.size() > filterParametrs.getStart() + filterParametrs.getLimit()) ? filterParametrs.getStart() + filterParametrs.getLimit() : list.size();
            for (int i = start; i < limit; i++) {
                EdsCompany edsCompany = companyManager.getCompany(Integer.valueOf(list.get(i)));
                SelectItem selectItem = new SelectItem();
                if (edsCompany == null) {
                    selectItem.setId(Integer.valueOf(list.get(i)));
                    selectItem.setName(null);
                    try {
                        selectItem.setSelected(Boolean.valueOf(coreService.getValue("select isShowDraggableWorkspace from companySystemSettings where companyid=" + list.get(i))));
                    } catch (Exception exp) {
                        selectItem.setSelected(false);
                    }
                }/* else {

                    EdsWebsite edsWebsite = websiteManager.getCompanyDefaultWebsite(edsCompany.getObjectID());
                    if (Boolean.TRUE.equals(edsWebsite != null && edsWebsite.isCustom())) {
                        continue;
                    }
                    selectItem.setId(edsCompany.getObjectID());
                    selectItem.setName(edsCompany.getName());
                    selectItem.setSelected(companySystemSettingsManager.findByCompanyID(edsCompany.getObjectID()).getShowDraggableWorkspace());
                }*/
                selectItems.add(selectItem);
            }

            return new ListResult<>(selectItems, list.size());
        }
    }

    @Override
    public String getLink(Integer exceltemplateId) {
        return uploadManager.getFileURL(exceltemplateId);
    }

    @Transactional
    @Override
    public void activeDraggableWorkspace(ListingFilterParameter filterParametrs) {
        if (filterParametrs.getCompaines() == null || null == filterParametrs.getParentID()) {
            return;
        }
        String sourceDatabase = PAID;
        getWorkspaceImportExporter().importPermission(sourceDatabase);
        for (Integer companyID : filterParametrs.getCompaines()) {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
            try {
                getWorkspaceImportExporter().openWorkspace(companyID, filterParametrs);
                getWorkspaceImportExporter().importRolePermission(String.valueOf(companyID), String.valueOf(filterParametrs.getParentID()), sourceDatabase);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public ListResult<HelpDocumentItem> getHelpDocumentList(ListingFilterParameter filterParameter) {
        ListResult<HelpDocumentItem> result = new ListResult<>(new ArrayList<>(), 0);
        List<EdsHelpDocument> helpDocumentList = documentManager.getHelpDocumentList(filterParameter);
        Integer helpCount = documentManager.getHelpDocumentTotalCount(filterParameter);
        if (helpDocumentList != null && !helpDocumentList.isEmpty()) {
            ArrayList<HelpDocumentItem> documentItems = new ArrayList<>();
            HelpDocumentItem item;
            for (EdsHelpDocument edsHelpDocument : helpDocumentList) {
                item = edsHelpDocument.getRPC();
                documentItems.add(item);
            }
            result = new ListResult<>(documentItems, helpCount);
        }
        return result;
    }

    @Override
    public Integer saveHelpDocument(HelpDocumentItem item) {
        EdsHelpDocument document = item.getObjectID() != null ? documentManager.get(item.getObjectID()) : null;
        if (document == null) {
            document = new EdsHelpDocument();
        }
        document.setTitle(item.getTitle());
        document.setDescription(item.getDescription());
        document.setLink(item.getLink());
        document.setHostName(item.getHostName());
        document.setSection(item.getSection());
        document.setForm(item.getForm());
        document.setBlock(item.getBlock());
        documentManager.createOrUpdate(document);
        return item.getObjectID();
    }

    @Override
    public HelpDocumentItem getHelpDocuments(Integer objectID) {
        EdsHelpDocument document = documentManager.get(objectID);
        if (document != null) {
            return document.getRPC();
        }
        return new HelpDocumentItem();
    }

    @Override
    public Boolean deleteHelpDocument(Integer objectID) {
        if (objectID != null) {
            EdsHelpDocument document = documentManager.get(objectID);
            if (document != null) {
                documentManager.deleteHelpDocument(objectID);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean isExistHelpDocument(Integer objectID, String form, String block) {
        return documentManager.getExistHelpDocument(objectID, form, block);

    }

    @Override
    public ListResult<LocalizationItem> getLocalizations(ListingFilterParameter filter, String code, String untranslatedField) {
        ArrayList<LocalizationItem> items = new ArrayList<>();
        List<EdsLocalization> result = localizationManager.list(filter, code, untranslatedField);
        LocalizationItem property;
        int total = result.size();
        result = ListUtils.getSublist(result, filter.getStart(), filter.getLimit());
        for (EdsLocalization item : result) {
            items.add(loadLocalization(item));
        }
        return new ListResult<>(items, total);
    }

    @Override
    public LocalizationItem getLocalization(Integer id) {
        EdsLocalization result = localizationManager.getLocalization(id);
        LocalizationItem item = loadLocalization(result);
        item.setLocalizationPermission(getCompanyLocalizationPermissions());
        return item;

    }

    private LocalizationItem loadLocalization(EdsLocalization item) {
        LocalizationItem property = new LocalizationItem();
        property.setObjectID(item == null || item.getObjectID() == null ? 0 : item.getObjectID());
        property.setCode(item == null || item.getCode() == null ? " " : item.getCode());
        property.setDefaultText(item == null || item.getDefaultText() == null ? "" : item.getDefaultText());
        property.setEn(item == null || item.getEn() == null ? "" : item.getEn());
        property.setRu(item == null || item.getRu() == null ? "" : item.getRu());
        property.setArabic(item == null || item.getAr() == null ? "" : item.getAr());
        property.setTurkish(item == null || item.getTr() == null ? "" : item.getTr());
        property.setGer(item == null || item.getGer() == null ? "" : item.getGer());
        property.setSpa(item == null || item.getSpa() == null ? "" : item.getSpa());
        property.setFr(item == null || item.getFr() == null ? "" : item.getFr());
        property.setPor(item == null || item.getPt() == null ? "" : item.getPt());
        property.setNeder(item == null || item.getNl() == null ? "" : item.getNl());
        property.setIta(item == null || item.getIt() == null ? "" : item.getIt());
        property.setThai(item == null || item.getTh() == null ? "" : item.getTh());
        property.setPropertyCode(item == null || item.getPropertyCode() == null ? "" : item.getPropertyCode());
        property.setPropertyPath(item == null || item.getPropertyPath() == null ? "" : item.getPropertyPath());
        property.setDescription(item == null || item.getDescription() == null ? "" : item.getDescription());

        if (item != null) {
            property.setDefaultLastChanger(item.getDefaultLastChanger());
            property.setEnLastChanger(item.getEnLastChanger());
            property.setItaLastChanger(item.getItaLastChanger());
            property.setRuLastChanger(item.getRuLastChanger());
            property.setArLastChanger(item.getArLastChanger());
            property.setPorLastChanger(item.getPorLastChanger());
            property.setFrLastChanger(item.getFrLastChanger());
            property.setSpaLastChanger(item.getSpaLastChanger());
            property.setTurLastChanger(item.getTurLastChanger());
            property.setThaiLastChanger(item.getThaiLastChanger());
            property.setNederLastChanger(item.getNederLastChanger());

            property.setDefaultLastUpdate(item.getDefaultLastUpdate());
            property.setEnLastUpdate(item.getEnLastUpdate());
            property.setItaLastUpdate(item.getItaLastUpdate());
            property.setRuLastUpdate(item.getRuLastUpdate());
            property.setArLastUpdate(item.getArLastUpdate());
            property.setPorLastUpdate(item.getPorLastUpdate());
            property.setFrLastUpdate(item.getFrLastUpdate());
            property.setSpaLastUpdate(item.getSpaLastUpdate());
            property.setTurLastUpdate(item.getTurLastUpdate());
            property.setThaiLastUpdate(item.getThaiLastUpdate());
            property.setNederLastUpdate(item.getNederLastUpdate());
        }
        return property;
    }

    public Boolean saveLocalization(LocalizationItem item) {
        EdsLocalization result;
        if (item.getObjectID().equals(0) || item.getObjectID() == null) {
            result = localizationManager.getLocalizationByCode(item.getPropertyCode(), item.getCode());
            if (result != null) {
                return null;
            }
        }
        if (item.getObjectID() != 0) {  // 0 degani bu localization engi qushilvoti degani
            result = localizationManager.getLocalization(item.getObjectID());
        } else {
            result = new EdsLocalization();
            result.setPropertyCode(item.getPropertyCode());
        }
        result.setCode(item.getCode());
        result.setDefaultText(item.getDefaultText());
        result.setEn(item.getEn());
        result.setRu(item.getRu());
        result.setAr(item.getArabic());
        result.setTr(item.getTurkish());
        result.setGer(item.getGer());
        result.setSpa(item.getSpa());
        result.setFr(item.getFr());
        result.setPt(item.getPor());
        result.setNl(item.getNeder());
        result.setIt(item.getIta());
        result.setTh(item.getThai());
        result.setLastUpdate(new Date());
        result.setDescription(item.getDescription());

        result.setDefaultLastChanger(item.getDefaultLastChanger());
        result.setEnLastChanger(item.getEnLastChanger());
        result.setRuLastChanger(item.getRuLastChanger());
        result.setArLastChanger(item.getArLastChanger());
        result.setSpaLastChanger(item.getSpaLastChanger());
        result.setTurLastChanger(item.getTurLastChanger());
        result.setFrLastChanger(item.getFrLastChanger());
        result.setPorLastChanger(item.getPorLastChanger());
        result.setNederLastChanger(item.getNederLastChanger());
        result.setItaLastChanger(item.getItaLastChanger());
        result.setThaiLastChanger(item.getThaiLastChanger());

        result.setDefaultLastUpdate(item.getDefaultLastUpdate());
        result.setEnLastUpdate(item.getEnLastUpdate());
        result.setRuLastUpdate(item.getRuLastUpdate());
        result.setArLastUpdate(item.getArLastUpdate());
        result.setSpaLastUpdate(item.getSpaLastUpdate());
        result.setTurLastUpdate(item.getTurLastUpdate());
        result.setFrLastUpdate(item.getFrLastUpdate());
        result.setPorLastUpdate(item.getPorLastUpdate());
        result.setNederLastUpdate(item.getNederLastUpdate());
        result.setItaLastUpdate(item.getItaLastUpdate());
        result.setThaiLastUpdate(item.getThaiLastUpdate());

        localizationManager.createOrUpdate(result);
        return true;
    }

    @Override
    public ListResult<LocalizationPermissionItem> getLocalizationPermission() {
        ArrayList<LocalizationPermissionItem> items = new ArrayList<>();
        List<EdsLocalizationPermissions> result = localizationPermissionManager.list();
        for (EdsLocalizationPermissions permission : result) {
            items.add(loadLocalizationPermission(permission));
        }
        return new ListResult<>(items, items.size());
    }

    public Boolean saveLocalizationPermission(String str, Boolean permission) {   // str da companyId_languageCode kurinishda keladi
        String[] splt = str.split("_");
        int companyID = Integer.parseInt(splt[0]);
        int code = Integer.parseInt(splt[1]);
        EdsLocalizationPermissions item = localizationPermissionManager.getCompanyLocalization(companyID);
        if (item == null) {
            item = new EdsLocalizationPermissions();
        }
        switch (code) {
            case Constants.CODE -> item.setCode(permission);
            case Constants.DEFAULT_TEXT -> item.setDefaultText(permission);
            case Constants.EN -> item.setEn(permission);
            case Constants.RU -> item.setRu(permission);
            case Constants.ARABIC -> item.setArabic(permission);
            case Constants.TURKISH -> item.setTurkish(permission);
            case Constants.GER -> item.setGer(permission);
            case Constants.SPA -> item.setSpa(permission);
            case Constants.FR -> item.setFr(permission);
            case Constants.POR -> item.setPor(permission);
            case Constants.NEDER -> item.setNeder(permission);
            case Constants.ITA -> item.setIta(permission);
            case Constants.THAI -> item.setThai(permission);
        }
        item.setCompany(companyManager.get(companyID));
        localizationPermissionManager.createOrUpdate(item);
        return true;
    }

    public LocalizationPermissionItem getCompanyLocalizationPermissions() {
        EdsLocalizationPermissions permissions = localizationPermissionManager.getCompanyLocalization(userManager.getUser().getCompany().getObjectID());
        return loadLocalizationPermission(permissions);
    }

    private LocalizationPermissionItem loadLocalizationPermission(EdsLocalizationPermissions item) {
        LocalizationPermissionItem permission = new LocalizationPermissionItem();
        permission.setCompanName(item != null && item.getCompany() != null ? item.getCompany().getName() : "");
        permission.setCompanyID(item != null && item.getCompany() != null ? item.getCompany().getObjectID() : null);
        permission.setCode(item != null && item.getCode() != null && item.getCode());
        permission.setDefaultText(item != null && item.getDefaultText() != null && item.getDefaultText());
        permission.setEn(item != null && item.getEn() != null && item.getEn());
        permission.setRu(item != null && item.getRu() != null && item.getRu());
        permission.setArabic(item != null && item.getArabic() != null && item.getArabic());
        permission.setTurkish(item != null && item.getTurkish() != null && item.getTurkish());
        permission.setGer(item != null && item.getGer() != null && item.getGer());
        permission.setSpa(item != null && item.getSpa() != null && item.getSpa());
        permission.setFr(item != null && item.getFr() != null && item.getFr());
        permission.setPor(item != null && item.getPor() != null && item.getPor());
        permission.setNeder(item != null && item.getNeder() != null && item.getNeder());
        permission.setIta(item != null && item.getIta() != null && item.getIta());
        permission.setThai(item != null && item.getThai() != null && item.getThai());
        return permission;
    }

    public SelectItem[] getPropertyItems() {
        List<String> items = localizationManager.propertyList();
        SelectItem[] selectItems = new SelectItem[items.size()];
        Integer i = 0;
        for (String item : items) {
            selectItems[i] = new SelectItem();
            selectItems[i].setId(i);
            selectItems[i].setName(item);
            selectItems[i].setDescription(item);
            i++;
        }
        return selectItems;

    }

    public String backupCompanyDocuments(Integer companyID) {
        return uploadManager.backupCompanyDocuments(companyID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCompanyActiveUsers(Integer companyID) {
        List<EdsUser> users = userManager.getCompanyActiveUsers(companyID);
        SelectItem[] s = new SelectItem[users.size()];
        int i = 0;
        for (EdsUser user : users) {
            s[i] = new SelectItem(user.getObjectID(), user.getName() + " (" + user.getUserName() + ")");
            i++;
        }
        return s;
    }

    private WorkspaceImportExporter getWorkspaceImportExporter() {
        return workspaceImportExporter = workspaceImportExporter == null ? new WorkspaceImportExporter() {
            {
                setReportingService(coreService);
                //setWebsiteService(websiteService);
            }
        } : workspaceImportExporter;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<CompanyListItem> getCompanyStatisticList(ListingFilterParameter fp) {
        EdsUser user = userManager.getUser();
        EdsBackendManagement backendManagement = backendManagementManager.getBackendManagement(user.getCompany().getObjectID(), user.getObjectID());
        if (backendManagement != null) {
            fp.setParams(backendManagement.getHostNames());
            if (!(backendManagement.getPromotionalCode() == null || backendManagement.getPromotionalCode().isEmpty() || "null".equals(backendManagement.getPromotionalCode()))) {
                fp.setAccountCode(backendManagement.getPromotionalCode());
            }
        }

        LinkedHashMap<Integer, CompanyListItem> companyStatisticMap = new LinkedHashMap<>();

        ListResult<CompanyListItem> dublicateCompaniesStatictisList = companyStatisticManager.getCompanyStatisticList(fp);

        for (CompanyListItem item : dublicateCompaniesStatictisList.getList()) {
            if (companyStatisticMap.get(item.getCompanyID()) != null && !"Active".equals(item.getUsagePlanPaymentStatus())) {
                companyStatisticMap.remove(item.getObjectID());
            } else {
                if (item.getCurrentUsagePlan() == null) {
                    EdsUsagePlan usagePlan = usagePlanManager.getLastUsagePlan(item.getCompanyID());
                    if (usagePlan != null) {
                        item.setCurrentUsagePlan(usagePlan.getObjectID());
                    }
                } else {
                    item.setCurrentUsagePlan(item.getCurrentUsagePlan());
                }
                companyStatisticMap.put(item.getCompanyID(), item);
            }
        }
        ArrayList<CompanyListItem> companyStatisticList = new ArrayList<>(companyStatisticMap.values());
        return new ListResult<>(companyStatisticList, dublicateCompaniesStatictisList.getTotal());
    }

    @Override
    public BackendManagementListItem getBackendManagement(Integer objectID) {
        EdsBackendManagement backendManagement = backendManagementManager.get(objectID);
        if (backendManagement != null) {
            return backendManagement.getRPC();
        }
        return new BackendManagementListItem();
    }

    @Transactional
    @Override
    public void setPermissionForSavedReports(ListingFilterParameter filterParametrs) {
        if (filterParametrs == null || filterParametrs.getCategories() == null) {
            return;
        }
        Integer[] companies = filterParametrs.getCompaines();
        for (Integer companyId : companies) {
            ServerSecurityContext.getInstance().setCompanyId(companyId);
            for (String code : filterParametrs.getStatusCodes()) {
                EdsReport report = reportingManager.getByCode(code);
                if (report != null) {
                    reportingServiceLocal.saveReportPermission(report);
                }
            }
            referenceManager.flushAndClear();
            System.out.println(">>>>>>>>>>>>>>>>> Apply permission task successful completed (companyId =" + companyId + ").");
        }
    }

    @Transactional
    @Override
    public String synchronizationReporting(Integer companyId) {
        ServerSecurityContext.getInstance().setCompanyId(companyId);
        List<EdsReportTemplate> reportTemplateList = reportTemplateManager.getReportTemplateList(null);
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setLimit(1000);
        filterParameter.setSelected(true);
        List<Object[]> edsReportList = reportingManager.getReportList(filterParameter);

        LinkedList<RpcMap> templateList = new LinkedList<>();
        LinkedList<RpcMap> reportList = new LinkedList<>();
        for (EdsReportTemplate template : reportTemplateList) {
            if (Boolean.TRUE.equals(template.getSynchronization()))
                templateList.add(template.getMap());
        }
        for (Object[] objects : edsReportList) {
            reportList.add(((EdsReport) objects[0]).getMap());
        }

        final HashMap map = new HashMap();
        map.put("templateList", templateList);
        map.put("reportList", reportList);
        final String json = new Gson().toJson(map);
//        TOMCAT_URLS

        for (final String tomcatUrl : WfmJpaTemplate.getTomcatUrls()) {
            System.out.println(tomcatUrl);
            Thread thread = new Thread(() -> {

                StringEntity requestEntity = new StringEntity(json, ContentType.APPLICATION_JSON);

                final HttpPost post = new HttpPost(tomcatUrl);
                post.setEntity(requestEntity);
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    HttpResponse response = httpClient.execute(post);
                    String responseContent = EntityUtils.toString(response.getEntity());
                    System.out.print(tomcatUrl + " " + responseContent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread.yield();
            });
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.setDaemon(true);
            thread.start();
        }
        return null;
    }

    @Override
    public void changeProjectPercents(Integer companyId, boolean toReset) {
        ServerSecurityContext.getInstance().setCompanyId(companyId);
        try {
            projectTaskLogic(companyId, toReset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeCompnayID(Integer companyId) {
        ServerSecurityContext.getInstance().removeCompanyId();
    }

    @Override
    @Transactional
    public void indexVacancy(SolrReindexRpc solrReindexRpc) {
        if (solrReindexRpc.getCompanyId() == 0) {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (company.hasSchema(schemas)) {
                    solrReindexRpc.setCompanyId(company.getObjectID());
                    recruitmentServiceLocal.indexCompanyVacancy(solrReindexRpc);
                }
            }
        } else {
            recruitmentServiceLocal.indexCompanyVacancy(solrReindexRpc);
        }
    }

    @Override
    public void fixVacancyInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixVacancyInconsistenciesInSolr(companyID);
            fixVacancyInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixVacancyInconsistenciesInSolr(company.getObjectID());
                    fixVacancyInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    @Transactional
    public void fixVacancyInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing Vacancy DB - > SOLR inconsistences started for companyID = " + companyID);

        Integer start = 1;
        // first iteratively will fix task inconsistencies in DB
        try {
            while (start != -1) {
                start = fixVacancyInconsistenciesInDbData(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix Vacancy DB - > SOLR inconsistence for companyID = " + companyID);
        }
        companyManager.flushAndClear();
    }

    private Integer fixVacancyInconsistenciesInDbData(Integer companyID, Integer start) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        /// retrives 10 inconsistencies
        List<EdsSolrDbConsistency> newsDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.VACANCY, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, start, limit);
        if (newsDbInconsistencies.size() == 0) {
            return -1; // if there are no inconsistencies
        }
        /// add batch tasks using batch add 100
        boolean firsttime = true;

        StringBuilder sb = new StringBuilder();
        for (EdsSolrDbConsistency sdb : newsDbInconsistencies) {
            if (!firsttime) {
                sb.append(",");
            }
            sb.append(sdb.getEntityID());
            firsttime = false;
        }
        List<EdsVacancy> edsVacancies = vacancyManager.getUndeletedVacancyIn(sb.toString());
        try {
            vacancySolrComponent.indexConcurrently(edsVacancies);
        } catch (SolrServerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : newsDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed vacancy of Company ID=" + companyID + " DB inconsistency vacancyIDs (" + sb + ")");

        EdsSolrDbConsistency lastOne = newsDbInconsistencies.get(newsDbInconsistencies.size() - 1);
        return lastOne.getObjectID(); // returns last fixed inconsistency objectID
    }

    @Transactional
    public void fixVacancyInconsistenciesInSolr(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        System.out.println("Fixing Vacancy SOLR - > DB inconsistences started for companyID = " + companyID);
        Integer start = 1;
        // first iteratively will fix task inconsistencies in Solr
        try {
            while (start != -1) {
                start = fixVacancyInconsistenciesInSolrData(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix SOLR Vacancy - > DB inconsistence for companyID = " + companyID);
        }
        companyManager.flushAndClear();
    }

    private Integer fixVacancyInconsistenciesInSolrData(Integer companyID, Integer start) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limt = 100;
        //retrives 100 inconsistences
        List<EdsSolrDbConsistency> vacancySolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.VACANCY, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, start, limt);
        if (vacancySolrInconsistencies.size() == 0) {
            return -1;// if there are no inconsistences
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : vacancySolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrVacancyRepresenter.COMPANY_ID + ":" + companyID + " AND " + SolrVacancyRepresenter.FIELD_VACANCY_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_VACANCY_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : vacancySolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        System.out.println("Fixeds company=" + companyID + " Vacancy solr inconsistency vacancyIds(" + sb + ")");
        EdsSolrDbConsistency sdb = vacancySolrInconsistencies.get(vacancySolrInconsistencies.size() - 1);
        return sdb.getObjectID();// returns last fixed inconsistency objectID for iterator
    }

    @Override
    public void analyzeVacancyInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.VACANCY);
            companyManager.flushAndClear();
            analyzeVacancySolrDbconsistence(companyID);
            analyzeVacancyDbSolrInconsistencies(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemas)) {
                    solrDbConsistencyManager.removeInconsistences(company.getObjectID(), EdsSolrDbConsistency.VACANCY);
                    companyManager.flushAndClear();
                    analyzeVacancySolrDbconsistence(company.getObjectID());
                    analyzeVacancyDbSolrInconsistencies(company.getObjectID());
                }
            }
        }
    }

    private void analyzeVacancyDbSolrInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_VACANCY_CORE);
        List<Integer> vacancyIdList = vacancyManager.getVacancyIdListWithLimit(companyID, startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (vacancyIdList.size() != 0) {
                nonExisting.addAll(vacancyIdList);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrVacancyRepresenter.COMPANY_ID + ":" + companyID + " AND " + SolrVacancyRepresenter.FIELD_VACANCY_ID + ":(" + ServerUtils.getAsCommoDelimited(vacancyIdList, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer vacancyId = Integer.valueOf(sd.getFieldValue(SolrVacancyRepresenter.FIELD_VACANCY_ID).toString());
                    nonExisting.remove(vacancyId);
                }
                vacancyIdList = vacancyManager.getVacancyIdListWithLimit(companyID, vacancyIdList.get(vacancyIdList.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.VACANCY);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("Vacancy with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeVacancySolrDbconsistence(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_VACANCY_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrVacancyRepresenter.COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrVacancyRepresenter.FIELD_VACANCY_ID);
        sQuery.addField(SolrVacancyRepresenter.COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrVacancyRepresenter.FIELD_VACANCY_ID).toString());
                    Integer vacancyid = Integer.valueOf(sd.getFieldValue(SolrVacancyRepresenter.FIELD_VACANCY_ID).toString());
                    nonExisting.put(vacancyid, sd);
                }

                List<Integer> vacancyIdList = vacancyManager.getUndeletedVacancyIdList(ids.toString());
                for (Integer id : vacancyIdList) {
                    nonExisting.remove(id);
                }

                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);

            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrVacancyRepresenter.FIELD_VACANCY_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.VACANCY);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrVacancyRepresenter.COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("Vacancy with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexEmployeeStep(SolrReindexRpc solrReindexRpc) {
        if (solrReindexRpc.getCompanyId() == 0) {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (company.hasSchema(schemas)) {
                    solrReindexRpc.setCompanyId(company.getObjectID());
                    recruitmentServiceLocal.indexCompanyEmployeeStep(solrReindexRpc);
                }
            }
        } else {
            recruitmentServiceLocal.indexCompanyEmployeeStep(solrReindexRpc);
        }
    }

    @Override
    public void fixEmployeeStepInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixEmployeeStepInconsistenciesInSolr(companyID);
            fixEmployeeStepInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixEmployeeStepInconsistenciesInSolr(company.getObjectID());
                    fixEmployeeStepInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    @Transactional
    public void fixEmployeeStepInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing EmployeeStep DB - > SOLR inconsistences started for companyID = " + companyID);

        Integer start = 1;
        // first iteratively will fix task inconsistencies in DB
        try {
            while (start != -1) {
                start = fixEmployeeStepInconsistenciesInDbData(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix EmployeeStep DB - > SOLR inconsistence for companyID = " + companyID);
        }
        companyManager.flushAndClear();
    }

    private Integer fixEmployeeStepInconsistenciesInDbData(Integer companyID, Integer start) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        /// retrives 10 inconsistencies
        List<EdsSolrDbConsistency> newsDbInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.EMPLOYEE_STEP, EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, start, limit);
        if (newsDbInconsistencies.isEmpty()) {
            return -1; // if there are no inconsistencies
        }
        var stepEmpList = newsDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).map(String::valueOf).collect(Collectors.joining(","));
        var stepEmployees = stepEmployeeManager.getUndeletedStepIn(stepEmpList);
        try {
            employeeStepSolrComponent.indexConcurrently(stepEmployees);
        } catch (SolrServerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : newsDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed employee stage of Company ID=" + companyID + " DB inconsistency vacancyIDs (" + stepEmpList + ")");

        EdsSolrDbConsistency lastOne = newsDbInconsistencies.get(newsDbInconsistencies.size() - 1);
        return lastOne.getObjectID(); // returns last fixed inconsistency objectID
    }

    @Transactional
    public void fixEmployeeStepInconsistenciesInSolr(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        System.out.println("Fixing EmployeeStep SOLR - > DB inconsistences started for companyID = " + companyID);
        Integer start = 1;
        // first iteratively will fix task inconsistencies in Solr
        try {
            while (start != -1) {
                start = fixEmployeeStepInconsistenciesInSolrData(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix SOLR EmployeeStep - > DB inconsistence for companyID = " + companyID);
        }
        companyManager.flushAndClear();
    }

    private Integer fixEmployeeStepInconsistenciesInSolrData(Integer companyID, Integer start) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limt = 100;
        //retrives 100 inconsistences
        List<EdsSolrDbConsistency> stepEmployeeSolrInconsistencies = solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.EMPLOYEE_STEP, EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, start, limt);
        if (stepEmployeeSolrInconsistencies.size() == 0) {
            return -1;// if there are no inconsistences
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        boolean firsttime = true;
        for (EdsSolrDbConsistency sdb : stepEmployeeSolrInconsistencies) {
            if (!firsttime) {
                ids.append(",");
            }
            ids.append(sdb.getEntityID());
            firsttime = false;
            sb.append(sdb.getEntityID()).append(" ");
        }
        // generting solr query
        String query = SolrEmployeeStepRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrEmployeeStepRepresenter.FIELD_STEP_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_EMPLOYEE_STEP_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : stepEmployeeSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        System.out.println("Fixeds company=" + companyID + " EmployeeStep solr inconsistency vacancyIds(" + sb + ")");
        EdsSolrDbConsistency sdb = stepEmployeeSolrInconsistencies.get(stepEmployeeSolrInconsistencies.size() - 1);
        return sdb.getObjectID();// returns last fixed inconsistency objectID for iterator
    }

    @Override
    public void analyzeEmployeeStepInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.EMPLOYEE_STEP);
            companyManager.flushAndClear();
            analyzeEmployeeStepSolrDbconsistence(companyID);
            analyzeEmployeeStepDbSolrInconsistencies(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemas)) {
                    solrDbConsistencyManager.removeInconsistences(company.getObjectID(), EdsSolrDbConsistency.EMPLOYEE_STEP);
                    companyManager.flushAndClear();
                    analyzeEmployeeStepSolrDbconsistence(company.getObjectID());
                    analyzeEmployeeStepDbSolrInconsistencies(company.getObjectID());
                }
            }
        }
    }

    @Override
    public ArrayList<CompanyDomain> getFingerprintSetup(Integer companyID) {
        return globalAuthJdbcSpringManager.getFingerprintSetup(companyID);
    }

    @Override
    public ArrayList<String> saveFingerPrintSetup(Integer companyID, ArrayList<CompanyDomain> setupTOs) {
        globalAuthJdbcSpringManager.deleteCompanyDomainByCompanyId(companyID);
        String errorMessege;
        ArrayList<String> errorList = new ArrayList<>();
        for (CompanyDomain companyDomain : setupTOs) {
            errorMessege = globalAuthJdbcSpringManager.insertCompanyDeviceIdByCompanyId(companyID, companyDomain);
            if (errorMessege != null && !"".equals(errorMessege)) {
                errorList.add(errorMessege);
            }
        }
        return errorList.size() > 0 ? errorList : null;
    }

    private void analyzeEmployeeStepDbSolrInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_EMPLOYEE_STEP_CORE);
        List<Integer> stepEmployeeIdList = stepEmployeeManager.getEmployeeStepIdListWithLimit(companyID, startat, limit);
        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (stepEmployeeIdList.size() != 0) {
                nonExisting.addAll(stepEmployeeIdList);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrEmployeeStepRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrEmployeeStepRepresenter.FIELD_STEP_ID + ":(" + ServerUtils.getAsCommoDelimited(stepEmployeeIdList, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer vacancyId = Integer.valueOf(sd.getFieldValue(SolrEmployeeStepRepresenter.FIELD_STEP_ID).toString());
                    nonExisting.remove(vacancyId);
                }
                stepEmployeeIdList = stepEmployeeManager.getEmployeeStepIdListWithLimit(companyID, stepEmployeeIdList.get(stepEmployeeIdList.size() - 1), limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.EMPLOYEE_STEP);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("EmployeeStep with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void analyzeEmployeeStepSolrDbconsistence(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_EMPLOYEE_STEP_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrEmployeeStepRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrEmployeeStepRepresenter.FIELD_STEP_ID);
        sQuery.addField(SolrEmployeeStepRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrEmployeeStepRepresenter.FIELD_STEP_ID).toString());
                    Integer stepid = Integer.valueOf(sd.getFieldValue(SolrEmployeeStepRepresenter.FIELD_STEP_ID).toString());
                    nonExisting.put(stepid, sd);
                }
                List<Integer> employeeStepIdList = stepEmployeeManager.getUndeletedEmployeeStepIdList(ids.toString());
                for (Integer id : employeeStepIdList) {
                    nonExisting.remove(id);
                }
                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
            }
            Iterator it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrEmployeeStepRepresenter.FIELD_STEP_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.EMPLOYEE_STEP);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrEmployeeStepRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("EmployeeStep with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexRFQ(SolrReindexRpc solrReindex) {
        if (solrReindex.getCompanyId() == 0) {
            List<EdsCompany> companys = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (schemas.contains(company.getObjectID().toString())) {
                    solrReindex.setCompanyId(company.getObjectID());
                    transactionHelper.runInANewTransaction(() ->  indexCompanyRFQ(solrReindex));
                }
            }
        } else {
            indexCompanyRFQ(solrReindex);
        }
    }

    @Transactional
    public void indexCompanyRFQ(SolrReindexRpc solrReindex) {
        SecurityContext.setCompanyID(solrReindex.getCompanyId());
        solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.RFQ);
        //        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));;
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeShippingData(null, solrReindex.getCompanyId());
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error RFQ Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());

        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        List<EdsRFQ> rfqList = rfqManager.getRFQListForSolr(solrReindex, startat, limit);
        while (!rfqList.isEmpty()) {
            try {
                rfqSolrComponent.indexConcurrently(rfqList);
            } catch (IOException | SolrServerException | InterruptedException e) {
                log.error("Error RFQ Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            rfqManager.flushAndClear();
            startat++;
            rfqList = rfqManager.getRFQListForSolr(solrReindex, (startat * limit), limit);
        }
        rfqManager.flushAndClear();
//        companyManager.flushAndClear();
    }

    @Override
    public void fixRFQInconsistency(Integer companyID) {
        if (!(Integer.valueOf(0)).equals(companyID)) {
            fixRFQInconsistenciesInSolr(companyID);
            fixRFQInconsistenciesInDb(companyID);
        } else {
            List<EdsCompany> companies = companyManager.getCompanies();
            List<String> schemaList = companyManager.getExistingSchemas();
            for (EdsCompany company : companies) {
                if (company.hasSchema(schemaList)) {
                    fixRFQInconsistenciesInSolr(company.getObjectID());
                    fixRFQInconsistenciesInDb(company.getObjectID());
                }
            }
        }
    }

    public void fixRFQInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR EdsRFQ - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixRFQInconsistencyInSolr(companyID, start);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed fix EdsRFQ SOLR - > DB inconsistence for companyID = " + companyID);
            ex.printStackTrace();
        }
    }

    @Transactional
    public Integer fixRFQInconsistencyInSolr(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = 100;
        List<EdsSolrDbConsistency> rfqSolrInconsistencies =
                solrDbConsistencyManager.getCompanyInconsistiens(companyID,
                        EdsSolrDbConsistency.RFQ,
                        EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB, startAt, limit);
        if (rfqSolrInconsistencies.isEmpty()) {
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        for (EdsSolrDbConsistency sdb : rfqSolrInconsistencies) {
            sb.append(sdb.getEntityID()).append(" ");
        }
        String query = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_ID + ":(" + sb + ")";
        try {
            solrManager.removeEntity(query, SOLR_REQUEST_FOR_QUOTE_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : rfqSolrInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed company=" + companyID + " RFQ solr inconsistency ids(" + sb + ")");
        EdsSolrDbConsistency sdb = rfqSolrInconsistencies.get(rfqSolrInconsistencies.size() - 1);
        return sdb.getObjectID();
    }

    @Transactional
    public void fixRFQInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing SOLR RFQ - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        try {
            while (start != -1) {
                start = fixRFQInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix RFQ SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public Integer fixRFQInconsistenciesInDb(Integer companyID, Integer startAt) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        List<EdsSolrDbConsistency> rfqDbInconsistencies =
                solrDbConsistencyManager.getCompanyInconsistiens(companyID, EdsSolrDbConsistency.RFQ,
                        EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR, startAt, limit);
        if (rfqDbInconsistencies.isEmpty()) {
            return -1;
        }
        var reportsIDs = rfqDbInconsistencies.stream().map(EdsSolrDbConsistency::getEntityID).collect(Collectors.toList());
        var itemList = rfqManager.get(reportsIDs);
        try {
            rfqSolrComponent.indexConcurrently(itemList);
        } catch (SolrServerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        for (EdsSolrDbConsistency sdb : rfqDbInconsistencies) {
            sdb.setFixed(true);
            solrDbConsistencyManager.update(sdb);
        }
        companyManager.flushAndClear();
        System.out.println("Fixed RFQ of Company ID=" + companyID + " DB inconsistency IDs (" + reportsIDs + ")");

        EdsSolrDbConsistency lastOne = rfqDbInconsistencies.get(rfqDbInconsistencies.size() - 1);
        return lastOne.getObjectID();
    }

    @Override
    public void analyzeRFQInconsistency(Integer companyID) {
        if (companyID == 0) {
            analayzeRFQSolrInconsistenciesInAllCompanies();
            analayzeRFQDbInconsistenciesInAllCompanies();
        } else {
            analyzeRFQSolrDbInconsistencies(companyID);
            analyzeRFQDbSolrInconsistency(companyID);
        }
    }

    private void analayzeRFQSolrInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeRFQSolrDbInconsistencies(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    private void analayzeRFQDbInconsistenciesInAllCompanies() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (schemas.contains(company.getObjectID().toString())) {
                analyzeShippingDataDbSolrInconsistency(company.getObjectID());
                companyManager.flushAndClear();
            }
        }
    }

    @Transactional
    public void analyzeRFQSolrDbInconsistencies(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        solrDbConsistencyManager.removeInconsistences(companyID, EdsSolrDbConsistency.RFQ);
        companyManager.flushAndClear();
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_REQUEST_FOR_QUOTE_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery(SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID);
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.addField(SolrSaleInvoiceRepresenter.FIELD_RFQ_ID);
        sQuery.addField(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID);
        QueryResponse resp;
        Map<Integer, SolrDocument> nonExisting = new HashMap<>();
        StringBuffer ids;
        try {
            resp = server.query(sQuery, SolrRequest.METHOD.POST);
            while (resp.getResults().size() > 0) {
                boolean firstTime = true;
                ids = new StringBuffer();
                for (SolrDocument sd : resp.getResults()) {
                    if (!firstTime) {
                        ids.append(",");
                    }
                    firstTime = false;
                    ids.append(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_RFQ_ID).toString());
                    Integer eventID = Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_RFQ_ID).toString());
                    nonExisting.put(eventID, sd);
                }

                List<Integer> rfqIds = rfqManager.getRFQIdsByIds(ids.toString());
                for (Integer id : rfqIds) {
                    nonExisting.remove(id);
                }
                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery, SolrRequest.METHOD.POST);
                companyManager.flushAndClear();
            }
            Iterator<Map.Entry<Integer, SolrDocument>> it = nonExisting.entrySet().iterator();
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = Lists.newArrayList();
            while (it.hasNext()) {
                flushed = false;
                Map.Entry<Integer, SolrDocument> entry = (Map.Entry<Integer, SolrDocument>) it.next();
                SolrDocument sd = entry.getValue();
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_RFQ_ID).toString()));
                sdb.setEntityType(EdsSolrDbConsistency.RFQ);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_SOLR_BUT_MISSED_IN_DB);
                sdb.setCompanyid(Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID).toString()));
                sdb.setAnalizedate(startDate);
                System.out.println("EdsRFQ with id " + sdb.getEntityID() + " does not exist in DB");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    @Transactional
    public void analyzeRFQDbSolrInconsistency(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        int startat = 0;
        int limit = 1000;
        Date startDate = new Date();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_REQUEST_FOR_QUOTE_CORE);

        List<Integer> rfqIds = rfqManager.getRFQIdsWithLimit(startat, limit);

        ArrayList<Integer> nonExisting = new ArrayList<>();
        try {
            while (rfqIds.size() != 0) {
                nonExisting.addAll(rfqIds);
                SolrQuery sQuery = new SolrQuery();
                sQuery.setQuery(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":"
                        + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_RFQ_ID
                        + ":(" + ServerUtils.getAsCommoDelimited(rfqIds, "0", " ") + ")");
                sQuery.setRows(limit);

                QueryResponse response = server.query(sQuery);
                for (SolrDocument sd : response.getResults()) {
                    Integer reportId = Integer.valueOf(sd.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_RFQ_ID).toString());
                    nonExisting.remove(reportId);
                }
                startat++;
                rfqIds = rfqManager.getRFQIdsWithLimit(startat * limit, limit);
            }
            int flushLimit = 0;
            boolean flushed = false;
            List<EdsSolrDbConsistency> items = new ArrayList<>();
            for (Integer tId : nonExisting) {
                flushed = false;
                EdsSolrDbConsistency sdb = new EdsSolrDbConsistency();
                sdb.setEntityID(tId);
                sdb.setEntityType(EdsSolrDbConsistency.RFQ);
                sdb.setStatus(EdsSolrDbConsistency.STATUS_EXIST_IN_DB_BUT_MISSED_IN_SOLR);
                sdb.setCompanyid(company.getObjectID());
                sdb.setCompanyName(company.getName());
                sdb.setAnalizedate(startDate);
                System.out.println("RFQ with id -- " + sdb.getEntityID() + "-- does not exist in Solr");
                items.add(sdb);
                flushLimit++;
                if (flushLimit == WfmJpaTemplate.flushLimit) {
                    batchPersist(items);
                    items = new ArrayList<>();
                    flushLimit = 0;
                    flushed = true;
                }
            }
            if (!flushed) {
                batchPersist(items);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        companyManager.flushAndClear();
    }

    private void projectTaskLogic(Integer companyId, boolean toReset) {
        List<EdsProject> projects = projectManager.getProjectByCompanyID(companyId);
        if (genericSettingsManager.isSettingsEnabled(companyId, GenericSettingsEnum.CHANGED_PROJECT_PERCENT) && toReset) {
            changeProjectToReset(companyId, projects);
            genericSettingsManager.saveGenericSettings(companyId, GenericSettingsEnum.CHANGED_PROJECT_PERCENT, EdsGenericSettings.NO);
        } else {
            changeProjectToNewLogic(companyId, projects);
        }
    }

    private void changeProjectToReset(Integer companyId, List<EdsProject> projects) {
        try {
            for (EdsProject project : projects) {
                project.setPercent(project.getProjectTasksAveragePercentCompleted());
                projectManager.update(project);
                projectSolrComponent.index(project);
                for (EdsTask task : project.getUndeletedTasks()) {
                    task.setPercent(task.getTaskAveragePercentCompleted());
                    taskManager.update(task);
                    taskSolrComponent.index(task);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void changeProjectToNewLogic(Integer companyId, List<EdsProject> projects) {
        genericSettingsManager.saveGenericSettings(companyId, GenericSettingsEnum.CHANGED_PROJECT_PERCENT, EdsGenericSettings.YES);
        try {
            for (EdsProject project : projects) {
                project.setPercent(project.getProjectTasksAveragePercentCompletedNewLogic());
                projectManager.update(project);
                projectSolrComponent.index(project);
                for (EdsTask task : project.getUndeletedTasks()) {
                    task.setPercent(task.getTaskAveragePercentCompletedNewLogic());
                    taskManager.update(task);
                    taskSolrComponent.index(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public LinkedHashMap<String, String> getExpiringCompanyRatio(ListingFilterParameter fp) {
        return getCompanylist(fp);
    }

    private LinkedHashMap<String, String> getCompanylist(ListingFilterParameter fp) {
        if (fp == null) {
            return new LinkedHashMap<>();
        }
        LinkedHashMap<String, String> resultMap = new LinkedHashMap<>();
        List<Object[]> results = usagePlanManager.getExpiringCompaniesByMonthYear(fp.getSelectedMonth(), fp.getSelectedYear());
        //List<Object[]> result1 = usagePlanManager.getnotLoggedCompaniesSinceMonthYear(fp.getSelectedMonth(), fp.getSelectedYear());

        for (Object[] result : results) {
            resultMap.put(result[0].toString(), ServerUtils.shortDateFormat((Date) result[1], userManager.getUser().getCompany()));
        }

        return resultMap;
    }

    @Override
    public LinkedHashMap<String, String> getNotLoggingCompanyRatio() {
        LinkedHashMap<String, String> resultMap = new LinkedHashMap<>();
        List<Object[]> results = companyStatisticManager.getnotLoggedCompaniesSinceMonthYear();
        for (Object[] result : results) {

            resultMap.put(result[0].toString(), ServerUtils.shortDateFormat((Date) result[1], userManager.getUser().getCompany()));
        }
        return resultMap;
    }

    @Transactional
    @Override
    public void localizationUpdateDataBase() {
        final LocalizationUtils utils = new LocalizationUtils();
        utils.init();
        utils.setLocalizationService((LocalizationService) ApplicationContextProvider.applicationContext.getBean("localizationService"));

        for (final Map.Entry<String, LocalizationUtils.LocalizationInfo> properties : utils.propertiesMap.entrySet()) {
            utils.updateDataBase(properties.getValue());
        }
    }

    @Transactional
    @Override
    public void localizationUpdateResource() {
        final LocalizationUtils utils = new LocalizationUtils();
        utils.init();
        utils.setLocalizationService((LocalizationService) ApplicationContextProvider.applicationContext.getBean("localizationService"));

        for (final Map.Entry<String, LocalizationUtils.LocalizationInfo> properties : utils.propertiesMap.entrySet()) {
            utils.updateResoure(properties.getValue());
        }
    }

    @Override
    @Transactional
    public String startTansferCompanyFile(Integer companyId, SelectItem importType) {
        if (companyId == null || importType == null) {
            return "Incorrect incoming params";
        }
        final EdsCompany companyDomain = companyManager.get(companyId);

        if (companyDomain == null || companyDomain.isDeleted()) {
            return "Company not found!";
        }
        SecurityContext.setCompanyID(companyDomain.getObjectID());
        final String uploadType = EdsContextParams.getUploadType();

        if (!Objects.equals(Constants.LOCAL, uploadType)) {
            return "Incorrect upload type and import type";
        }
        final String uploadPath = fileTransferService.getLocalUploadDirectory();

        log.info("++++++++++++++++++++++++++Starting transfer files++++++++++++++++++++++++++");
        Integer start = 0;

        do {
            start = fileTransferService.transferFilesLimitedToLocalStorageFromAmazon(start, 100, uploadPath);
        } while (start != null && start > 0);

        SecurityContext.setCompanyID(null);
        log.info("++++++++++++++++++++++++++End transfer files++++++++++++++++++++++++++++++++");
        return "Successful";
    }

    @Override
    public void importLocalizationPropertyToDB(ImportFile file) {
        EdsImportFile edsImportFile = new EdsImportFile(file);
        importFileManager.createOrUpdate(edsImportFile);
        baseEventsPostProcessor.registerEvent(ImportCustomEventListenerImpl.TYPE, ImportCustomEventListenerImpl.EVENT_IMPORT_LOCALIZATION_PROPERTY, edsImportFile, employeeManager.getUser());
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void createAttendaceRawDataRecords(ListingFilterParameter fp) {
        Calendar from = Calendar.getInstance();
        Date startDate = ServerUtils.parseFilterParameterDate(fp.getStartDateNC());
        Date endDate = ServerUtils.parseFilterParameterDate(fp.getEndDateNC());

        from.setTime(startDate);
        from.set(Calendar.AM_PM, 0);
        from.set(Calendar.HOUR_OF_DAY, 0);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);
        from.set(Calendar.MILLISECOND, 0);

        Calendar to = Calendar.getInstance();
        to.setTime(endDate);
        to.set(Calendar.AM_PM, 0);
        to.set(Calendar.HOUR_OF_DAY, 0);
        to.set(Calendar.MINUTE, 0);
        to.set(Calendar.SECOND, 0);
        to.set(Calendar.MILLISECOND, 0);

        // update for All Free or All Paid Companies
        if (fp.getCompanyID() == 0 || fp.getCompanyID() == -1) {
            if (fp.getCompanyID() == 0) {
                ServerSecurityContext.getInstance().setDatabase(Constants.DATABASE_PAID);
            } else {
                ServerSecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
            }
            List<EdsCompany> companyList = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companyList) {
                if (company.hasSchema(schemas)) {
                    fp.setCompanyID(company.getObjectID());
                    try {
                        runAttendance(fp, from, to);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    log.info(">>RAW DATA CREATION SKIPPED for companyID: " + company.getObjectID());
                }

            }
        } else {
            if (fp.getEmployeeId() == null) {
                runAttendance(fp, from, to);
            } else {
                runAttendance(fp, from, to);
            }
        }
    }

    private void runAttendance(ListingFilterParameter fp, Calendar from, Calendar to) {
        log.error(">>RAW DATA CREATION STARTED for companyID: " + fp.getCompanyID());
        ServerSecurityContext.getInstance().setCompanyId(fp.getCompanyID());
        if (fp.getEmployeeId() != null) {
            availabilityService.createEmployeeAttendanceData(fp.getCompanyID(), from, to, fp.getEmployeeId());
        } else {
            List<Integer> employeesIds = employeeManager.getEmployeeIds();
            Calendar fromDate;
            for (Integer employeeId : employeesIds) {
                fromDate = (Calendar) from.clone();
                availabilityService.createEmployeeAttendanceData(fp.getCompanyID(), fromDate, to, employeeId);
            }
        }
    }

    @Override
    @Transactional
    public Integer createTemplateSchema(Integer count) {
        if (count > 50) {
            return null;
        }
        ServerSecurityContext.getInstance().setDatabase("FREE");
        return templateSchema.createSchemaTemplate(count, TemplateSchema.TEMPLATE);
    }

    @Override
    public ListResult<DynamicLogin> getDynamicLoginList(ListingFilterParameter filterParameter) {
        List<DynamicLogin> list = hostBasedSettingManager.getList(filterParameter);
        ArrayList<DynamicLogin> result = new ArrayList<>(list);
        return new ListResult<>(result, result.size());
    }

    @Override
    public ListResult<DynamicLogin> getWhiteLabelList(ListingFilterParameter filterParameter) {
        List<DynamicLogin> list = hostBasedSettingManager.getWhiteLabelList(filterParameter);
        ArrayList<DynamicLogin> result = new ArrayList<>(list);
        return new ListResult<>(result, hostBasedSettingManager.getWhiteLabelCount(filterParameter));
    }

    @Override
    public DynamicLogin getDynamicLoginItem(String hostname) {
        return hostBasedSettingManager.getDynamicLoginItem(hostname);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public String saveDynamicLogin(DynamicLogin item) {
        hostBasedSettingManager.saveDynamicLogin(item);
        EdsContextParams.clearHostSetting(item.getHostname());
        return null;
    }

    @Override
    public void saveWhiteLabelItems(DynamicLogin item) {
        if (item.getAttachments() != null) {
            saveAttachments(Constants.F_WHITE_LABEL_LOGO, item.getAttachments(), item.getId());
        }
        if (item.getFavIcon() != null) {
            saveAttachments(Constants.F_WHITE_LABEL_FAVICON, item.getFavIcon(), item.getId());
        }
        String rawToken = item.getOpenAiToken();
        if (rawToken != null && !rawToken.isEmpty()) {
            String encrypted = EncryptionUtils.encrypt(rawToken);
            item.setOpenAiToken(encrypted);
        }
        hostBasedSettingManager.saveWhiteLabel(item);
    }

    @Override
    public DynamicLogin getWhiteLabelItem(String hostname) {
        DynamicLogin whiteLabelItem = hostBasedSettingManager.getWhiteLabelItem(hostname);
        String token = whiteLabelItem.getOpenAiToken();
        if (token != null && !token.isBlank()) {
            whiteLabelItem.setOpenAiToken(EncryptionUtils.decrypt(token));
        }
        return whiteLabelItem;
    }

    @Override
    public SelectItem[] getHosts() {
        return hostBasedSettingManager.getHosts();
    }

    private void saveAttachments(int type, final FileItem[] attachments, final Integer objectId) {
        this.attachmentUtilsManager.saveAttachments(type, objectId, objectId, attachments);
    }

    @Override
    public void runSchemaUpdate(String[] args) {
        BackendRunSchemaUpdate.runSchemaUpdate(args);
    }
}
