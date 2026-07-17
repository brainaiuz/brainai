package com.edatasite.workforce.gwt.crm.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.log.KpiEntityType;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.shared.mail.EdsMailer;
import com.edatasite.shared.mail.MailMessage;
import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.CrmAccountInvoiceTO;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsCaseSolution;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsCustomLayout;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeEvent;
import com.edatasite.workforce.core.domain.EdsEntity;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsFormProperty;
import com.edatasite.workforce.core.domain.EdsHistoryLog;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsItemCustomFields;
import com.edatasite.workforce.core.domain.EdsLayout;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsSmsSettings;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.EdsZoomMeeting;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBrand;
import com.edatasite.workforce.core.domain.accounting.EdsCustomerTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsDiscount;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsPickList;
import com.edatasite.workforce.core.domain.accounting.EdsPriceLevel;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsSubsidiariesCompany;
import com.edatasite.workforce.core.domain.accounting.EdsSupplierTransaction;
import com.edatasite.workforce.core.domain.analyzer.EdsSolrDbConsistency;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.approving.EdsApproverRoles;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.CrmHistory;
import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCaseHistory;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.EdsCrmSubItem;
import com.edatasite.workforce.core.domain.crm.EdsEmailDetails;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.core.domain.crm.EdsMailMessage;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.EdsOpportunityItem;
import com.edatasite.workforce.core.domain.crm.EdsSmsSendItem;
import com.edatasite.workforce.core.domain.crm.EdsSolution;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactHistory;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsCrmSubItemCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsOpportunityItemTableCF;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.core.domain.customform.EdsCustomItemTable;
import com.edatasite.workforce.core.domain.customform.EdsOpportunityCustomItemTable;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailAttachment;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailTracker;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.enums.SalesType;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.domain.settings.EdsEmailFilter;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.core.domain.settings.EdsGenericSettings;
import com.edatasite.workforce.core.domain.settings.EdsSMSTemplates;
import com.edatasite.workforce.core.domain.webforms.EdsWebField;
import com.edatasite.workforce.core.domain.webforms.EdsWebForm;
import com.edatasite.workforce.core.solr.component.CaseSolrComponent;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.solr.component.CrmAccountSolrComponent;
import com.edatasite.workforce.core.solr.component.EventSolrComponent;
import com.edatasite.workforce.core.solr.component.OpportunitySolrComponent;
import com.edatasite.workforce.core.solr.component.SaleQuoteSolrComponent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.core.solr.document.CaseSolrDoc;
import com.edatasite.workforce.core.solr.document.ContactSolrDoc;
import com.edatasite.workforce.core.solr.document.CrmAccountSolrDoc;
import com.edatasite.workforce.core.solr.document.EventSolrDoc;
import com.edatasite.workforce.core.solr.document.OpportunitySolrDoc;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.accounting.client.rpc.LogHistoryItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.PriceLevelServiceLocal;
import com.edatasite.workforce.gwt.client.client.rpc.ClientCurrency;
import com.edatasite.workforce.gwt.client.server.app.ClientSupplierAccessService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.TaxKeyEnum;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.reference.PhoneReference;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.BugReportItem;
import com.edatasite.workforce.gwt.core.client.rpc.CampaignItem;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyData;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryList;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyLayerItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCaseRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrClientRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCrmAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEventRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrOpportunityRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPurchaseInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TelegramConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.EmailTemplateServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ImportingServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.RejectedImportRecord;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaOperations;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.app.social.zoom.ZoomService;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.ApproverManager;
import com.edatasite.workforce.gwt.core.server.db.AttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import com.edatasite.workforce.gwt.core.server.db.CaseHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.CaseSolutionManager;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.ClockManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyPdfTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.ContactCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.ContactHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactItemParamsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CrmSubItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.CrmSubItemManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmailFilterManager;
import com.edatasite.workforce.gwt.core.server.db.EmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.EmailTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeEventManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EntityManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.FormPropertyManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceTermsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.LayoutManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.MailMessageManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityItemTableCFManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityItemTableManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.PaymentMethodManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import com.edatasite.workforce.gwt.core.server.db.SMSTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.SmsManager;
import com.edatasite.workforce.gwt.core.server.db.SmsSendItemManager;
import com.edatasite.workforce.gwt.core.server.db.SolutionManager;
import com.edatasite.workforce.gwt.core.server.db.StudentManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.db.VatManager;
import com.edatasite.workforce.gwt.core.server.db.ZoomMeetingManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BrandManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.CustomerSupplierPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.DiscountManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PickListManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PriceLevelManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SubsidiariesCompanyManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.UnitMeasurementManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.db.analyzer.SolrDbConsistencyManager;
import com.edatasite.workforce.gwt.core.server.db.currency.ExchangeCurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.CrmCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.ItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.EmailAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.EmailTrackerManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.EmailRepository;
import com.edatasite.workforce.gwt.core.server.db.impl.ListingObjectItem;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseBookingManager;
import com.edatasite.workforce.gwt.core.server.db.webforms.WebFieldManager;
import com.edatasite.workforce.gwt.core.server.db.webforms.WebFormManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.SolrTransactionManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.ActivityEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.ClientContactEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.ClientEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.CrmAccountEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.CrmCampaignEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.CrmCaseEventListeneImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.CrmOpportunityEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.CrmSolutionEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.CrmWebFormEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.NoteEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.SolrEvent;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.TelegramChatEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.CompanyRegistrationCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.CrmContactCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.EmailFetchingCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.KanbanCalculationEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.switchvox.Switchvox;
import com.edatasite.workforce.gwt.core.server.switchvox.SwitchvoxCredentials;
import com.edatasite.workforce.gwt.core.server.switchvox.SwitchvoxResult;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.EmailUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrRelationUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.ActivityItem;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CaseList;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmAccountList;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmHistoryList;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmSubItem;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.edatasite.workforce.gwt.crm.client.rpc.LeadList;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunitiesList;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityExpenseClaimListItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.client.rpc.SolutionCaseItem;
import com.edatasite.workforce.gwt.crm.client.rpc.SolutionItem;
import com.edatasite.workforce.gwt.crm.client.rpc.UpdateModeEnum;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarService;
import com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceLocal;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.ItemTableSettingsServiceLocal;
import com.edatasite.workforce.gwt.messagecenter.server.MessageCenterServiceLocal;
import com.edatasite.workforce.gwt.messagecenter.server.app.MailServices;
import com.edatasite.workforce.gwt.messagecenter.server.app.tracker.EmailTrackerService;
import com.edatasite.workforce.gwt.profile.client.rpc.EmailFilter;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.edatasite.workforce.gwt.project.client.rpc.CloneProjectItem;
import com.edatasite.workforce.gwt.project.server.actions.ProjectServiceLocal;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.edatasite.workforce.gwt.webforms.client.forms.WebField;
import com.edatasite.workforce.gwt.webforms.client.forms.WebForm;
import com.edatasite.workforce.mail.EdsSubjects;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseResultListData;
import com.edatasite.workforce.rest.v2.release10.enums.OrderFieldEnum;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.CrmActivityDTO;
import com.edatasite.workforce.utils.EdsContextParams;
import com.finnetlimited.reportservice.core.client.gwtrpc.ListItem;
import com.google.api.client.util.Lists;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import ezvcard.VCard;
import kpi.javax.mail.internet.KPIMimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.GroupParams;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_DEFAULT_TAX_TO_LEAD_CONVERT_TO_OPPORTUNITY;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.KANBAN_ORDER_GAP;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_ACCOUNT;
import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;


/**
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 13:33:15
 */
@Transactional
@Service("crmService")
public class CRMServiceImpl implements CRMService, CrmServiceLocal, Constants {

    private static final Logger log = LoggerFactory.getLogger(CRMServiceImpl.class);
    @Autowired
    protected InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    protected PickListManager pickListManager;
    @Autowired
    protected UnitMeasurementManager unitMeasurementManager;
    @Autowired
    @Qualifier("jpaTemplate")
    private WfmJpaOperations jpaTemplate;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private CustomFormManager customFormManager;
    @Autowired
    private CustomFormItemManager customFormItemManager;
    @Autowired
    private FormPropertyManager formPropertyManager;
    @Autowired
    private GoogleCalendarService googleCalendarService;
    @Autowired
    private CrmCustomFieldsManager crmCustomFieldsManager;
    @Autowired
    private OpportunityItemTableCFManager opportunityItemTableCFManager;
    @Autowired
    private OpportunityItemTableManager opportunityItemTableManager;
    @Autowired
    private ItemCFManager itemCFManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private EmailSettingsManager emailSettingsManager;
    @Autowired
    private SolutionManager solutionManager;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private CaseSolutionManager caseSolutionManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private WarehouseManager warehouseManager;
    @Autowired
    private EmployeeEventManager employeeEventManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    @Qualifier("emailTemplateService")
    private EmailTemplateServiceLocal emailTemplateServiceLocal;
    @Autowired
    private CaseHistoryManager caseHistoryManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private ClockManager clockManager;
    @Autowired
    private LayoutManager layoutManager;
    @Autowired
    private WebFieldManager webFieldManager;
    @Autowired
    private WebFormManager webFormManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    @Qualifier("priceLevelService")
    private PriceLevelServiceLocal priceLevelServiceLocal;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private QuoteService quoteService;
    @Autowired
    private ContactCategoryManager contactCategoryManager;
    @Autowired
    private ContactHistoryManager contactHistoryManager;
    @Autowired
    private EmailFilterManager emailFilterManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private ExchangeCurrencyManager exchangeCurrencyManager;
    @Autowired
    private PaymentMethodManager paymentMethodManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private SolrTransactionManager solrTransactionManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    @Qualifier("projectService")
    private ProjectServiceLocal projectServiceLocal;
    @Autowired
    @Qualifier("taskService")
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private SmsSendItemManager smsSendItemManager;
    @Autowired
    private SolrDbConsistencyManager solrDbConsistencyManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerSupplierPaymentManager customerSupplierPaymentManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private GoogleCalendarService getGoogleCalendarService;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private GoogleCalendarServiceLocal googleCalendarServiceLocal;
    @Autowired
    private FolderManager folderManager;
    @Autowired
    @Qualifier("allInOneService")
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private MessageCenterServiceLocal messageCenterService;
    @Autowired
    private MailServices mailServices;
    @Autowired
    private EmailTrackerService emailTrackerService;
    @Autowired
    private EmailAttachmentManager emailAttachmentManager;
    @Autowired
    private EmailTrackerManager emailTrackerManager;
    @Autowired
    private CompanyCustomFieldsManager companyCFSettingsManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private ApproverManager approverManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("countryLocalizer")
    private WfmMessageSource countryLocalizer;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private AccountingServiceLocal accountingService;
    @Autowired
    private InvoiceTermsManager invoiceTermsManager;
    @Autowired
    private SubsidiariesCompanyManager subsidiariesCompanyManager;
    @Autowired
    private ClientSupplierAccessService clientSupplierAccessService;
    @Autowired
    private StudentManager studentManager;
    @Autowired
    private CourseBookingManager courseBookingManager;
    @Autowired
    private MailMessageManager mailMessageManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private VacancyManager vacancyManager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    @Qualifier("contactService")
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private ContactService contactService;
    @Autowired
    private PriceLevelManager priceLevelManager;
    @Autowired
    private VatManager vatManager;
    @Autowired
    private InvoicingSettingsManager invoicingSettingsManager;
    @Autowired
    private SMSTemplateManager smsTemplateManager;
    @Autowired
    private SmsManager smsManager;
    @Autowired
    private ImportingServiceLocal importingServiceLocal;
    @Autowired
    private MassMailServiceLocal massMailServiceLocal;
    @Autowired
    private DiscountManager discountManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private RFQManager rfqManager;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private BrandManager brandManager;
    @Autowired
    private ProductCategoryManager productCategoryManager;
    @Autowired
    private ItemTableSettingService itemTableSettingService;
    @Autowired
    private ItemTableSettingsServiceLocal itemTableSettingsServiceLocal;
    @Autowired
    private CrmSubItemManager crmSubItemManager;
    @Autowired
    private CrmSubItemCFManager crmSubItemCFManager;
    @Autowired
    protected CompanyPdfTemplateManager companyPdfTemplateManager;
    @Autowired
    private CrmContactItemParamsManager crmContactItemParamsManager;
    @Autowired
    private TelegramChatService telegramChatService;
    @Autowired
    private ZoomService zoomService;
    @Autowired
    private ZoomMeetingManager zoomMeetingManager;
    @Autowired
    private ProfileServiceLocal profileService;
    @Autowired
    private CrmAccountSolrComponent crmAccountSolrComponent;
    @Autowired
    private OpportunitySolrComponent opportunitySolrComponent;
    @Autowired
    private CaseSolrComponent caseSolrComponent;
    @Autowired
    private EventSolrComponent eventSolrComponent;
    @Autowired
    private ContactSolrComponent contactSolrComponent;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private SaleQuoteSolrComponent saleQuoteSolrComponent;
    @Autowired
    private TaskSolrComponent taskSolrComponent;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ContactListItem> getNewLeads(ListingFilterParameter filterParameter) {
        ListLoadConfig congig = new ListLoadConfig();
        congig.setStart(filterParameter.getStart());
        congig.setLimit(filterParameter.getLimit());
        congig.setSortField(filterParameter.getSortField());
        congig.setSortDir(filterParameter.isAscending() ? 1 : 2);
        ListPanelToolRpc panelTools = filterParameter.getListPanelTool();
        if (panelTools == null) {
            panelTools = new ListPanelToolRpc();
            panelTools.setColumnCodeName(ContactListItem.defaultLeadColumnNames);
        }
        if (panelTools.isCustomFieldsShown()) {
            panelTools.setListViewCustomFields(commonService.getCompanyCustomFieldsForListView(ViewName.Lead));
            filterParameter.setCustomFieldsShown(panelTools.isCustomFieldsShown());
        }
        filterParameter.setColumnsOfListing(panelTools.getColumnCodeName());
        //Get Leads List
        LeadList leadList = getLeadList(filterParameter, congig);
        //Convert to ArrayList and create result
        ListResult<ContactListItem> list = new ListResult<ContactListItem>(new ArrayList<>(Arrays.asList(leadList.getLeadListItems())), leadList.getTotalCount());

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmContact.class.getSimpleName());
        kpiLog.setEntityType("LEAD_CONTACT");
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get Lead list");
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LeadList getLeadList(ListingFilterParameter fp, ListLoadConfig config) {
        if (fp != null && fp.isAllByFilter() != null && fp.isAllByFilter()) {
            int start = 0;
            int limit = config != null && config.getLimit() != 20 && config.getLimit() > 0 ? config.getLimit() : 200;
            int totalLength = 1;
            List<ContactListItem> leadListItems = new ArrayList<>();
            while (totalLength > start) {
                ListLoadConfig config1 = new ListLoadConfig();
                config1.setStart(start);
                config1.setLimit(limit);
                LeadList leadList = getLeadList(fp, config1, false);
                totalLength = leadList.getTotalCount();
                leadListItems.addAll(Arrays.asList(leadList.getLeadListItems()));
                start = start + limit;
            }

            return new LeadList(leadListItems.toArray(new ContactListItem[]{}), totalLength);
        }
        return getLeadList(fp, config, false);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult getKanbanLeadList(ListingFilterParameter fp, ListLoadConfig config) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CONTACT_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(getSolrQueryForLead(fp, config), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return getKanbanLeadFromSolrResult(resp);
    }

    private LeadList getLeadList(ListingFilterParameter fp, ListLoadConfig config, boolean asSelectItem) {
        if ((fp != null && fp.isIDsOnly()) || asSelectItem) {
            asSelectItem = true;
        }
        fp.setAsSelectItem(asSelectItem);
        Page<ContactSolrDoc> contactSolrDocPage = contactSolrComponent.getLeadList(fp, config);
        return getLeadFromSolrResult(contactSolrDocPage, fp);
    }

    private CrmAccountList getCrmAccountList(ListingFilterParameter fp, ListLoadConfig config, boolean asSelectItem) {
        if ((fp != null && fp.isIDsOnly()) || asSelectItem) {
            asSelectItem = true;
        }
        fp.setAsSelectItem(asSelectItem);
        Page<CrmAccountSolrDoc> crmAccountSolrDocPage = crmAccountSolrComponent.getCrmAccountList(fp, config);
        return getCrmAccountFromSolrResult(crmAccountSolrDocPage, fp);
    }

    /**
     * Generates Solr Query for LEAD_CORE
     *
     * @param fp
     * @param config
     * @return SolrQuery
     */
    private SolrQuery getSolrQueryForLead(ListingFilterParameter fp, ListLoadConfig config) {
        FacetFilterRpc leadFacetFilter = fp.getFacetFilter();
        if (leadFacetFilter != null) {
            leadFacetFilter.setUserID(fp.getUserID());
            if (!leadFacetFilter.isFilterChanges()) {
                leadFacetFilter = commonServiceLocal.getUserFacetFilter(leadFacetFilter);
            }
        }
        EdsUser edsUser;
        if (fp.getUserID() != null) {
            edsUser = userManager.get(fp.getUserID());
        } else {
            edsUser = crmContactManager.getUser();
        }
        EdsCompany edsCompany = edsUser.getCompany();
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getLeadListFacetFilterAssigneeQuery(edsCompany, edsUser, fp, leadFacetFilter, null));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(leadFacetFilter, edsCompany, null, null));
        if (fp.isDetectDuplicates()) {
            Set<String> duplicates = getContactDuplicateNames(solrQuery.toString(), fp.getObjectIDs());
            if (duplicates != null && duplicates.size() > 0) {
                StringBuilder duplicateQuery = new StringBuilder();
                boolean isFirst = true;
                boolean found = false;
                for (String duplicate : duplicates) {
                    duplicate = duplicate.trim();
                    if (!"".equals(duplicate)) {
                        if (isFirst) {
                            found = true;
                            duplicateQuery.append(SolrContactRepresenter.FIELD_CONTACT_NAME_L_TEXT_FIELD).append(":(");
                        }
                        duplicateQuery.append(!isFirst ? " OR " : "").append(duplicate).append(" OR ").append(duplicate).append("*");
                        isFirst = false;
                    }
                }
                if (found) {
                    duplicateQuery.append(")");
                }
                if (duplicateQuery.length() > 0) {
                    solrQuery.append(" AND (").append(duplicateQuery).append(")");
                }
            }
        }
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(config.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(config.getLimit()));

        if (!fp.isSearchButton() && !fp.isLookUp()) {
            if (!fp.isDetectDuplicates()) {
                if (config.getSortField() != null && !"".equals(config.getSortField())) {
                    boolean desc = Constants.DESC == config.getSortDir();
                    String solrSortField = SolrContactRepresenter.getSortingField(config.getSortField());
                    if (solrSortField != null) {
                        query.setSort(solrSortField, getSolrOrder(desc));
                    } else {
                        CustomFieldsUtils.setCustomFieldsSortableNameToSolr(config.getSortField(), desc, query, true);
                    }
                } else {
                    query.setSort(SolrContactRepresenter.FIELD_UPDATE_DATE, SolrQuery.ORDER.desc);
                }
            }
        }
        return query;
    }

    /**
     * Generates Solr Query for CRMACCOUNT_CORE
     *
     * @param fp
     * @param config
     * @return SolrQuery
     */
    private SolrQuery getSolrQueryForCrmAccount(ListingFilterParameter fp, ListLoadConfig config) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        FacetFilterRpc accountFacetFilter = fp.getFacetFilter();
        if (accountFacetFilter != null && !accountFacetFilter.isFilterChanges()) {
            accountFacetFilter = commonServiceLocal.getUserFacetFilter(accountFacetFilter);
        }

        EdsUser edsUser = fp.getUserID() != null ? userManager.get(fp.getUserID()) : crmContactManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();

        String[] customFullAccessRoles = null;
        EdsReference edsAccountType = null;

        if (fp.getAccountType() != null) {
            edsAccountType = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, fp.getAccountType());
            if (SUPPLIER.equals(fp.getAccountType())) {
                customFullAccessRoles = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.ACCOUNTING_SUPPLIER_FULL_LIST_ACCESS).toArray(new String[]{});
            }
        }

        fp.setClientIds(clientManager.getEmployeeClients(edsUser.getObjectID()));

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getCrmAccountListSolrQuery(fp, edsCompany, edsAccountType, edsUser, customFullAccessRoles));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(accountFacetFilter, edsCompany, SolrCrmAccountRepresenter.FIELD_CREATED_DATE, null));

        if (fp.isDetectDuplicates()) {
            Set<String> duplicates = getAccountDuplicateNames(solrQuery.toString(), fp.getObjectIDs());
            if (duplicates != null && duplicates.size() > 0) {
                StringBuilder duplicateQuery = new StringBuilder();
                boolean isFirst = true;
                boolean found = false;
                for (String duplicate : duplicates) {
                    duplicate = duplicate.trim();
                    if (!"".equals(duplicate)) {
                        if (isFirst) {
                            found = true;
                            duplicateQuery.append(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_NAME_TEXT_FIELD).append(":(");
                        }
                        duplicateQuery.append(!isFirst ? " OR " : "").append(duplicate).append(" OR ").append(duplicate).append("*");
                        isFirst = false;
                    }
                }
                if (found) {
                    duplicateQuery.append(")");
                }
                if (duplicateQuery.length() > 0) {
                    solrQuery.append(" AND (").append(duplicateQuery).append(")");
                }
            }
        }

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(config.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(config.getLimit()));

        //sorting
        if (!fp.isSearchButton() && !fp.isLookUp()) {
            if (config.getSortField() != null && !"".equals(config.getSortField())) {
                boolean desc = Constants.DESC == config.getSortDir();
                String solrSortField = SolrClientRepresenter.getSortingField(config.getSortField());
                if (solrSortField != null) {
                    query.setSort(solrSortField, getSolrOrder(desc));
                } else {
                    CustomFieldsUtils.setCustomFieldsSortableNameToSolr(config.getSortField(), desc, query, true);
                }
            } else {
                query.setSort(SolrCrmAccountRepresenter.FIELD_LAST_UPDATED_DATE, SolrQuery.ORDER.desc);
            }
        }
        return query;
    }

    private Set<String> getAccountDuplicateNames(String solrQuery, List<Integer> inIDs) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CRM_ACCOUNT_CORE);
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(0);
        query.setRows(10000);
        QueryResponse resp = null;
        try {
            resp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return crmAccountManager.getDuplicateNamesSet(SolrUtils.getIdsFromSolrDocument(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID, resp.getResults()), inIDs);
    }

    private LeadList getLeadFromSolrResult(Page<ContactSolrDoc> contactSolrDocPage, ListingFilterParameter fp) {
        SelectItem[] statusSelectItems = getAsSelectItem(referenceManager.listReferences(EdsCrmContact._LEAD_STATUS), ServerUtils.REFERENCE);
        List<ContactListItem> leadListItems = new ArrayList<>();
        int totalCount = 0;
        if (contactSolrDocPage != null && contactSolrDocPage.getContent() != null) {
            totalCount = (int) contactSolrDocPage.getTotalElements();
            // adding solr proposed results to map
            List<ContactSolrDoc> contactSolrDocs = contactSolrComponent.getDocumentsExistingInBase(contactSolrDocPage.getContent(), RelationItem.TYPE_LEAD);
            List<Integer> contactIds = contactSolrDocs.stream().map(ContactSolrDoc::getContactId).collect(Collectors.toList());
            Map<Integer, String> leadNotes = noteHistoryManager.getLastNotesAsMap(EdsNoteHistory.CRM_LEAD, contactIds);
            Map<Integer, EdsCountry> countries = ServerUtils.getListAsMapIntegerAndValue(countryManager.list());
            HashMap<Integer, EdsCrmAccount> crmAccountsAsMap = crmAccountManager.getCrmAccountsAsMap();
            for (ContactSolrDoc doc : contactSolrDocs) {
//                ContactListItem leadItem = contactServiceLocal.getRPCFromSolrDoc(doc, fp, null, countries, false, false, null, null, crmAccountsAsMap);
                ContactListItem leadItem = contactServiceLocal.getRPCFromContactSolrDoc(doc, fp, null, countries, false, false, null, null, crmAccountsAsMap);
                leadItem.setLeadStatuses(statusSelectItems);
                if (leadNotes.containsKey(leadItem.getObjectId())) {
                    leadItem.setNote(leadNotes.get(leadItem.getObjectId()));
                }
                leadListItems.add(leadItem);
            }
        }
        return new LeadList(leadListItems.toArray(new ContactListItem[]{}), totalCount);
    }

    private ListResult getKanbanLeadFromSolrResult(QueryResponse resp) {
        int totalCount = 0;
        ArrayList<ContactListItem> leadListItems = new ArrayList<>();
        Map<Integer, String> imgs = new HashMap<>();
        if (resp != null && resp.getResults() != null) {
            totalCount = (int) resp.getResults().getNumFound();
            List<SolrDocument> realSolrDocuments = getDocumentsExistingInBase(SOLR_CONTACT_CORE,
                    resp.getResults(),
                    SolrContactRepresenter.FIELD_CONTACT_ID,
                    RelationItem.TYPE_LEAD);

            Map<Integer, String> leadNotes = noteHistoryManager.getLastNotesAsMap(EdsNoteHistory.CRM_LEAD, SolrUtils.getIdsFromSolrDocument(SolrContactRepresenter.FIELD_CONTACT_ID, realSolrDocuments.toArray(new SolrDocument[]{})));
            //Map<Integer, EdsCountry> countries = ServerUtils.getListAsMapIntegerAndValue(countryManager.list());
            List<String> columnCodes = commonServiceLocal.getCFsColumnCodeByUiTypes(ViewName.Lead, ListUtils.getCFUITypesForKanbanItem());
            leadListItems.addAll(realSolrDocuments.stream()
                    .map(doc -> {
                        ContactListItem res = contactServiceLocal.getKanbanLeadFromSolrDoc(doc);
                        //Set Last Note
                        if (leadNotes.containsKey(res.getObjectId())) {
                            res.setNote(leadNotes.get(res.getObjectId()));
                        }

                        Integer assigneeId = res.getLeadAssigneeID();
                        if (imgs.get(assigneeId) != null) {
                            res.setContactImageUrl(imgs.get(assigneeId));
                        } else {
                            EdsUser assignee = userManager.get(assigneeId);
                            if (assigneeId != null && assignee.getPhoto() != null) {
                                String imgUrl = getImageUrl(assignee.getPhoto().getObjectID());
                                imgs.put(assigneeId, imgUrl);
                                res.setContactImageUrl(imgUrl);
                            }
                        }
                        res.setCustomFieldsMap(CustomFieldsUtils.getInSolrCustomFields(doc, columnCodes));
                        return res;
                    })
                    .collect(Collectors.toList()));
        }
        return new ListResult(leadListItems, totalCount);
    }

    private CrmAccountList getCrmAccountFromSolrResult(Page<CrmAccountSolrDoc> crmAccountSolrDocPage, ListingFilterParameter fp) {
        List<EdsReference> types = referenceManager.listReferences(EdsCrmAccount._CRM_ACCOUNT_TYPE);
        List<SelectItem> typesSelectItemMap = new ArrayList<>();

        ArrayList<CrmAccountItem> crmAccountItems = new ArrayList<>();
        int totalCount = 0;
        if (crmAccountSolrDocPage != null && crmAccountSolrDocPage.getContent() != null && crmAccountSolrDocPage.getContent().size() > 0) {
            totalCount = (int) crmAccountSolrDocPage.getTotalElements();
            List<CrmAccountSolrDoc> crmAccountSolrDocs = crmAccountSolrComponent.getDocumentsExistingInBase(crmAccountSolrDocPage.getContent());
            List<CompanyCustomFieldItem> cfResultForFiltering = commonServiceLocal.getCompanyCustomFieldsForFiltering(ViewName.CrmAccount);
            List<Integer> crmAccountIds = crmAccountSolrDocs.stream().map(CrmAccountSolrDoc::getCrmAccountId).collect(Collectors.toList());
            Map<Integer, String> notes = noteHistoryManager.getLastNotesAsMap(EdsNoteHistory.CRM_ACCOUNT, crmAccountIds);
            boolean briefly = fp != null && fp.isBriefly();
            boolean iDsOnly = fp != null && fp.isIDsOnly();
            boolean asSelectItem = fp != null && fp.isAsSelectItem();
            for (CrmAccountSolrDoc doc : crmAccountSolrDocs) {
                typesSelectItemMap.clear();
                for (EdsReference type : types) {
                    typesSelectItemMap.add(new SelectItem(type.getObjectID(), referenceWfmMessageSource.localize(type.getCode(), type.getName())));
                }
                CrmAccountItem crmAccountItem = (asSelectItem || iDsOnly || briefly)
                        ? getAccountSolrDocumentAsRPC(doc, fp, cfResultForFiltering, typesSelectItemMap)
                        : getAccount(crmAccountManager.get(doc.getCrmAccountId()), briefly);
                if (notes.containsKey(crmAccountItem.getObjectId())) {
                    crmAccountItem.setNote(notes.get(crmAccountItem.getObjectId()));
                }
                crmAccountItems.add(crmAccountItem);
            }
        }
        CrmAccountList list = new CrmAccountList(crmAccountItems, totalCount);
        if (list.getTotal() > 0 && !fp.isIDsOnly()) {
            list.setDefaultOne(editAccount(null, null));
        }
        return list;
    }

    public CrmAccountItem getAccountSolrDocumentAsRPC(CrmAccountSolrDoc doc, ListingFilterParameter fp, List<CompanyCustomFieldItem> cfResultForFiltering, List<SelectItem> types) {
        CrmAccountItem item = new CrmAccountItem();
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        if (doc == null) {
            return item;
        }
        boolean iDsOnly = fp.isIDsOnly();
        boolean asSelectItem = fp.isAsSelectItem();
        item.setObjectId(doc.getCrmAccountId());
        if (asSelectItem || iDsOnly) {
            if (!iDsOnly) {
                item.setName(doc.getCrmAccountName());
                item.setNumber(doc.getCrmAccountNumber());
                if (doc.getOwnerName() != null) {
                    item.setOwnerNames(ServerUtils.collectionToCommaDelimitedString(doc.getOwnerName()));
                }
            }
        } else {

            if (doc.getOwnerName() != null) {
                item.setOwnerNames(ServerUtils.collectionToCommaDelimitedString(doc.getOwnerName()));
            }
            if (doc.getCrmAccountParentId() != null) {
                CrmAccountItem parent = new CrmAccountItem();
                parent.setObjectId(doc.getCrmAccountParentId());
                parent.setName(doc.getCrmAccountParentName());
                item.setParent(parent);
            }
            item.setName(doc.getCrmAccountName());
            item.setNumber(doc.getCrmAccountNumber());
            if (types != null && !types.isEmpty()) {
                item.setAccountTypes(types.toArray(new SelectItem[]{}));
                if (doc.getTypeIds() != null) {
                    if (doc.getTypeIds() instanceof ArrayList) {
                        SelectItem.setSelected(item.getAccountTypes(), false, doc.getTypeIds().toArray(new Integer[]{}));
                    }
                }
            }
            item.setBlocked(doc.getBlocked());
            item.setIndustry(referenceWfmMessageSource.localize(doc.getIndustryCode(), doc.getIndustryName()));
            item.setIndustryID(doc.getIndustryId());
            item.setEmail(doc.getEmail());
            item.setPhone(doc.getPhone());
            item.setFax(doc.getFax());
            item.setWebsite(doc.getWebsite());
            item.setTermName(doc.getTermName());

            Address billAddress = new Address();
            billAddress.setPrimary(true);
            billAddress.setObjectID(doc.getAdress1Id());
            billAddress.setAddress(doc.getStreet());
            billAddress.setAddressb(doc.getStreetb());
            billAddress.setCity(doc.getCity());
            billAddress.setCountryId(doc.getCountryId());
            billAddress.setCountry(countryLocalizer.localize(doc.getCountryCode(), doc.getCountryName()));
            billAddress.setState(doc.getStateName());
            billAddress.setStateId(doc.getStateId());
            billAddress.setZipCode(doc.getPostCode());
            item.setBillAddresses(new Address[]{billAddress});

            Address mailAddress = new Address();
            mailAddress.setPrimary(true);
            mailAddress.setObjectID(doc.getAdress2Id());
            mailAddress.setAddress(doc.getStreet2());
            mailAddress.setAddressb(doc.getStreet2b());
            mailAddress.setCity(doc.getCity2());
            mailAddress.setCountryId(doc.getCountryId2());
            mailAddress.setCountry(countryLocalizer.localize(doc.getCountryCode2(), doc.getCountryName2()));
            mailAddress.setState(doc.getStateName2());
            mailAddress.setStateId(doc.getStateId2());
            mailAddress.setZipCode(doc.getPostCode2());
            item.setMailAddresses(new Address[]{mailAddress});

            item.setCurrency(doc.getCurrencyName());
            item.setCurrencyId(doc.getCurrencyId());
            item.setVatNumber(doc.getVatNumber());
            item.setTrn(doc.getTrnNumber());
            item.setRegistrationNumber(doc.getRegistrationNumber());
            item.setPaymentMethod(commonLocalizer.localize(doc.getPaymentMethodCode(), doc.getPaymentMethodName()));
            item.setPaymentMethodId(doc.getPaymentMethodId());
            item.setLastUpdatedDate(doc.getLastUpdateDate());
            item.setCreatedDate(doc.getCreationDate());
            item.setBankName(doc.getBankName());
            item.setTaxName(doc.getTaxName());
            item.setInTarget(doc.getInTarget());

            if (fp.isCustomFieldsShown() && fp.getListPanelTool() != null) {
                item.setCustomFieldsMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(doc, fp.getListPanelTool().getColumnCodeName()));
            }
            if (cfResultForFiltering != null && !cfResultForFiltering.isEmpty()) {
                item.setCustomFieldsForFiltering((ArrayList<CompanyCustomFieldItem>) cfResultForFiltering);
            }
            if (fp.isWithImage()) {
                EdsCrmAccount crmAccount = crmAccountManager.get(item.getObjectId());
                if (crmAccount != null && crmAccount.getLogo() != null) {
                    item.setLogoUrl(getImageUrl(crmAccount.getLogo().getObjectID()));
                    item.setLogoId(crmAccount.getLogo().getObjectID());
                }
            }
        }
        item.setSalesType(doc.getSalesTypeName());
        item.setSalesTypeId(doc.getSalesTypeId());
        return item;
    }


    public List<SolrDocument> getDocumentsExistingInBase(String core, SolrDocumentList results, String fieldObjectID, String type) {
        List<SolrDocument> documents = new ArrayList<>();
        Map<Integer, SolrDocument> mapDocuments = new HashMap<>();
        List<Integer> keyList = new ArrayList<>();
        if (results != null && results.size() > 0) {
            for (SolrDocument doc : results) {
                documents.add(doc);
                Object objectID = doc.getFieldValue(fieldObjectID);
                Integer key = objectID instanceof String ? Integer.parseInt((String) objectID) : (Integer) objectID;
                mapDocuments.put(key, doc);
                keyList.add(key);
            }
        }
        List<Integer> objectIDsFromDatabase = null;
        if (SOLR_CONTACT_CORE.equals(core)) {
            objectIDsFromDatabase = RelationItem.TYPE_LEAD.equals(type) ? crmContactManager.getLeadIDsByIDs(keyList) : crmContactManager.getContactIDsByIDs(keyList);
        } else if (SOLR_CRM_ACCOUNT_CORE.equals(core)) {
            objectIDsFromDatabase = crmAccountManager.getCrmAccountIDsByIDs(keyList);
        } else if (SOLR_EVENT_CORE.equals(core)) {
            objectIDsFromDatabase = eventManager.getEventIDsBySolrIDs(keyList);
        }
        if (objectIDsFromDatabase != null && objectIDsFromDatabase.size() > 0) {
            for (Integer objectID : objectIDsFromDatabase) {
                mapDocuments.remove(objectID);
            }
            if (mapDocuments.size() > 0) {
                documents.removeAll(mapDocuments.values());
            }
        }
        return documents;
    }

    @Transactional
    public Integer saveLead(ContactListItem item, ArrayList<Integer> subscribedMailLists) {
        item.setContactType(EdsCrmContact.LEAD_CONTACT);
        return contactService.saveContact(item, subscribedMailLists != null ? new ArrayList<>(subscribedMailLists) : null, false);
    }

    @Override
    public Integer changeLeadKanbanOrder(SelectItem columnLayoutData, Integer itemId, Integer widgetIndex, Integer prevItemId, Integer afterItemId) {

        if (itemId != null) {
            EdsCrmContact contact = crmContactManager.get(itemId);
            EdsReference edsLeadStatus = referenceManager.get(columnLayoutData.getId());
            boolean hasStatusChange = this.isValueChanged(contact.getLeadStatus(), edsLeadStatus);
            contact.setLeadStatus(edsLeadStatus);
            if (prevItemId != null && afterItemId == null) {
                EdsCrmContact potentialContact = crmContactManager.getSiblingContactByKanbanOrderAndContactType(prevItemId, EdsCrmContact.LEAD_CONTACT,
                        (contact.getLeadStatus() != null ? contact.getLeadStatus().getCode() : null));
                afterItemId = potentialContact != null ? potentialContact.getObjectID() : null;
            }

            contactServiceLocal.updateEdsCrmContactAndIndex(contact, false, crmContactManager.getUser());
            baseEventPostProcessor.registerCustomEvent(KanbanCalculationEventListenerImpl.TYPE, EdsMyUpdate.ADD, contact, prevItemId, afterItemId);
            if (hasStatusChange) {
                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, contact, userManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_LEAD);
            }
        }
        return 0;
    }

    private boolean isValueChanged(EdsReference oldValue, EdsReference newValue) {
        if (oldValue == null && newValue == null) {
            return false;
        }
        if (oldValue == null || newValue == null) {
            return true;
        } else
            return !oldValue.getCode().equals(newValue.getCode());
    }


    @Override
    @Transactional
    public Boolean changeLeadStatus(ArrayList<Integer> ids, Integer statusId) {
        if (statusId != null && ids != null && ids.size() > 0) {
            List<Integer> statusChangedLeads = crmContactManager.getStatusChangedLeads(statusId, ids);
            crmContactManager.changeLeadStatus(statusId, ids);

            EdsUser user = userManager.getUser();
            List<EdsCrmContact> leads = crmContactManager.getLeadsByIDs(ids);
            for (EdsCrmContact contact : leads) {
                if (statusChangedLeads.contains(contact.getObjectID())) {
                    contact.setPropertiesChanged(true);
                    crmContactManager.createHistory(contact);
                    contact.clear();
                    contact.addChange(CustomFormConstants.STATUS);
                    EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, contact, user);
                    workflowEvent.setEntityType(RelationItem.TYPE_LEAD);
                }
            }
            try {
                contactSolrComponent.indexes(leads);
                return Boolean.TRUE;
            } catch (Exception e) {
                e.printStackTrace();
                return Boolean.FALSE;
            }
        }
        return Boolean.FALSE;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactListItem editLead(Integer objectId, Integer webFormID) {
        ContactListItem item = contactService.editContact(CrmConstants.TYPE_LEAD_CONTACT, objectId, null, webFormID, true);
        if (objectId == null && item.getPrimaryAddress(true).getCountryId() == null && crmContactManager.getUser().getCompany() != null && crmContactManager.getUser().getCompany().getCountryZone() != null && crmContactManager.getUser().getCompany().getCountryZone().getCountry() != null) {
            Address primaryAddress = item.getPrimaryAddress(true);
            primaryAddress.setName(primaryAddress.getName() == null || primaryAddress.getName().isEmpty() ? "Billing Address" : primaryAddress.getName());
            primaryAddress.setCountryId(crmContactManager.getUser().getCompany().getCountryZone().getCountry().getObjectID());
            primaryAddress.setCountry(crmContactManager.getUser().getCompany().getCountryZone().getCountry().getName());
            item.setPrimaryAddress(primaryAddress);
        }
        EdsUser user = userManager.getUser();
        if (user != null && user.getCompany() != null && user.getCompany().getCompanySettings() != null) {
            item.setPdfLimit(user.getCompany().getCompanySettings().getPdfLimit());
            item.setExcelLimit(user.getCompany().getCompanySettings().getExcelLimit());
        }
        CurrencyItem baseCurrency = invoiceServiceLocal.getBaseCurrency();
        if (baseCurrency != null) {
            item.setBaseCurrencyID(baseCurrency.getId());
            item.setBaseCurrencyName(baseCurrency.getName());
        }
        LinkedHashMap<String, FormProperty> fields = new LinkedHashMap<>();
        EdsFormProperty edsFormProperty = formPropertyManager.getByFormID(LayoutRPC.LEAD_FORM);
        if (edsFormProperty != null) {
            Gson gson = new Gson();
            FormProperty[] formFields = gson.fromJson(edsFormProperty.getSettingsJSONData(), FormProperty[].class);
            for (FormProperty formProperty : formFields) {
                if (formProperty != null) {
                    if (formProperty.getDefaultValue() != null && formProperty.getDefaultValue().isEmpty()) {
                        formProperty.setDefaultValue(null);
                    }
                    if (formProperty.getRoleEdit() != null && !formProperty.getRoleEdit().isEmpty()) {
                        if (user.hasEitherRoles(formProperty.getRoleEdit().toArray(new Integer[]{}))) {
                            formProperty.setDisabled(false);
                        }
                    }
                    fields.put(formProperty.getCode(), formProperty);
                }
            }
        }
        item.setFormProperty(fields);
        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactListItem getLead(Integer objectId) {
        ContactListItem item = new ContactListItem();
        if (objectId != null) {
            item = contactService.getContact(objectId, false);
            if (item != null) {
                item.setLeadStatuses(getAsSelectItem(referenceManager.listReferences(EdsCrmContact._LEAD_STATUS), ServerUtils.REFERENCE));
                item.setLeadRatings(getAsSelectItem(referenceManager.listReferences(EdsCrmContact._LEAD_RATING), ServerUtils.REFERENCE));
//                if (objectId == 0) {
                item.setCountries(commonService.getCountries());
                item.setStates(commonService.getRegions());
                item.setLeadRatings(ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmContact._LEAD_RATING), ServerUtils.REFERENCE));
                item.getCrmAccount().setIndustries(getAsSelectItem(referenceManager.listReferences(_COMPANY_WORKAREA), ServerUtils.REFERENCE));
                item.setLeadAssignees(getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE));
                CurrencyItem baseCurrency = invoiceServiceLocal.getBaseCurrency();
                if (baseCurrency != null) {
                    item.setBaseCurrencyID(baseCurrency.getId());
                    item.setBaseCurrencyName(baseCurrency.getName());
                }
//                }
                LinkedHashMap<String, FormProperty> fields = new LinkedHashMap<>();
                EdsFormProperty edsFormProperty = formPropertyManager.getByFormID(LayoutRPC.LEAD_FORM);
                if (edsFormProperty != null) {
                    Gson gson = new Gson();
                    FormProperty[] formFields = gson.fromJson(edsFormProperty.getSettingsJSONData(), FormProperty[].class);
                    EdsUser user = userManager.getUser();
                    for (FormProperty formProperty : formFields) {
                        if (formProperty != null) {
                            if (formProperty.getDefaultValue() != null && formProperty.getDefaultValue().length() == 0) {
                                formProperty.setDefaultValue(null);
                            }
                            if (formProperty.getRoleEdit() != null && formProperty.getRoleEdit().size() > 0) {
                                if (user.hasEitherRoles(formProperty.getRoleEdit().toArray(new Integer[]{}))) {
                                    formProperty.setDisabled(false);
                                }
                            }
                            fields.put(formProperty.getCode(), formProperty);
                        }
                    }
                }
                item.setFormProperty(fields);
            }
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmContact.class.getSimpleName());
        kpiLog.setEntityType("LEAD_CONTACT");
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(item.getObjectId());
        ServerUtils.kpiLog(log, kpiLog, "View lead");
        return item;
    }

    @Transactional
    public void saveLeadAssignee(ArrayList<Integer> leadIDs, Integer assigneeId) {
        if (leadIDs != null && leadIDs.size() > 0 && assigneeId != null) {
            crmContactManager.changeLeadAssignee(assigneeId, leadIDs);
            List<EdsCrmContact> leads = crmContactManager.getLeadsByIDs(leadIDs);
            if (leads.size() > 0) {
                if (leads.size() == 1) {
                    EdsCrmContact lead = leads.get(0);
                    lead.clear();
                    lead.addChange(CustomFormConstants.ASSIGNEE);
                    EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, lead, userManager.getUser());
                    workflowEvent.setEntityType(RelationItem.TYPE_LEAD);
                }
                try {
                    contactSolrComponent.indexes(leads);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Transactional
    public void changeAccountsOwners(ArrayList<Integer> accountIDs, ArrayList<Integer> ownerIDs, UpdateModeEnum updateMode, ListingFilterParameter listingFilterParameter) {
        List<EdsCrmAccount> crmAccounts = crmAccountManager.getCrmAccountsByIDs(accountIDs);
        List<EdsUser> owners = userManager.getByIDs(ownerIDs);
        if (UpdateModeEnum.ADD.equals(updateMode)) {
            crmAccounts.forEach(crmAccount -> {
                owners.stream().filter(owner -> !crmAccount.getOwners().contains(owner)).forEach(owner -> crmAccount.getOwners().add(owner));
                crmAccountManager.update(crmAccount);
            });
        } else if (UpdateModeEnum.OVER_WRITE.equals(updateMode)) {
            crmAccounts.forEach(crmAccount -> {
                crmAccount.getOwners().clear();
                crmAccount.getOwners().addAll(owners);
                crmAccountManager.update(crmAccount);
            });
        }
        try {
            solrManager.addCrmAccountWithContactToIndex(crmAccounts.toArray(new EdsCrmAccount[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public SelectItem[] getOwnersListByPermission(String permissionCode) {
        List<EdsEmployee> ownersList = employeeManager.getOwnersByPermission(permissionCode);
        List<SelectItem> result = new ArrayList<>();
        ownersList.forEach(employee -> result.add(new SelectItem(employee.getObjectID(), employee.getFullName(), null, false)));
        return result.toArray(new SelectItem[0]);
    }

    @Override
    public ArrayList<SelectItem> getAccountOwnersList() {
        List<EdsEmployee> ownersList = employeeManager.getOwnersByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE);
        ArrayList<SelectItem> result = new ArrayList<>();
        ownersList.forEach(employee -> result.add(new SelectItem(employee.getObjectID(), employee.getFullName())));
        return result;
    }

    public ArrayList<SelectItem> getCrmAccountOwners(Integer crmAccountID) {
        ArrayList<SelectItem> result = new ArrayList<>();
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(crmAccountID);
        edsCrmAccount.getOwners().forEach(owner -> result.add(new SelectItem(owner.getObjectID(), owner.getName(), null, true)));
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Map<String, String[]> checkEmailExistenceInternally(Integer leadID, String[] email) {
        if (email == null || email.length == 0 || email[0] == null) {
            return null;
        }
        EdsUser user = userManager.getUser();
        boolean isSalesManagerOrAdmin = true;
        if (user != null) {
            isSalesManagerOrAdmin = user.hasRole(roleManager.get(EdsRole.SALESMAN)) || user.hasRole(roleManager.get(EdsRole.ADMIN));
        }
        Map<String, String[]> exist = crmContactManager.checkEmailExistence(leadID, email, user, isSalesManagerOrAdmin);
        if (exist != null && exist.size() > 0) {
            return exist;
        }
        exist = crmAccountManager.checkEmailExistence(email);
        if (exist.size() > 0) {
            return exist;
        }
        EdsCrmContact lead = leadID != null ? crmContactManager.get(leadID) : null;
        exist = clientContactManager.checkEmailExistence(lead != null ? lead.getEntityID() : null, email);
        return exist;
    }

    public ListResult<OpportunityListItem> getOpportunityList(ListingFilterParameter filterParameter) {

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsOpportunity.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get opportunity list");

        var opportunitySolrDocPage = opportunitySolrComponent.getList(filterParameter);

        return getOpportunityFromSolrResult(opportunitySolrDocPage, filterParameter);
    }

    public ListResult<OpportunityListItem> getOpportunityListByCategoryID(Integer categoryId) {
        List<EdsOpportunity> opportuniesByCategoryId = opportunityManager.getOpportuniesByCategoryId(categoryId);
        ArrayList<OpportunityListItem> list = new ArrayList<>();
        for (EdsOpportunity opportunity : opportuniesByCategoryId) {
            list.add(opportunity.getRPC(new OpportunityListItem()));
        }

        return new ListResult<>(list, opportuniesByCategoryId.size());
    }

    public ListResult<OpportunityListItem> getOpportunityListByProductID(Integer productId) {
        List<EdsOpportunity> opportuniesByProductId = opportunityManager.getOpportuniesByProductId(productId);
        ArrayList<OpportunityListItem> list = new ArrayList<>();
        for (EdsOpportunity opportunity : opportuniesByProductId) {
            list.add(opportunity.getRPC(new OpportunityListItem()));
        }

        return new ListResult<>(list, opportuniesByProductId.size());
    }

    public String getOpportunityFacetQuery(ListingFilterParameter filterParameter, FacetFilterRpc opportunityFacetFilter) {

        EdsUser edsUser = employeeManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(getOpportunityCoreSolrQuery(edsUser, filterParameter));
        solrQuery.append(SolrFacetUtils.generateSaleInvoiceDuePaidAmountFacet(
                opportunityFacetFilter,
                FacetContentType.OpportunityFacetFilter.getContentCode()[4]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(opportunityFacetFilter, edsCompany,
                SolrOpportunityRepresenter.FIELD_CLOSING_DATE,
                SolrOpportunityRepresenter.FIELD_CLOSING_DATE,
                FacetContentType.OpportunityFacetFilter.getContentCode()[4]));
        if (filterParameter.getRelationID() != null && filterParameter.getRelationType() != null) {
            List<Integer> opportunityIDs = relationManager.getRelationIDsByType(filterParameter.getRelationID(), null, filterParameter.getRelationType(), RelationItem.TYPE_OPPORTUNITY);
            if (opportunityIDs != null && opportunityIDs.size() > 0) {
                solrQuery.append(" AND ").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID).append(":(").append(ServerUtils.getAsCommoDelimited(opportunityIDs, "0", " ")).append(")");
            }
        }
        // ---- from kanban board ----
        if (filterParameter.getColumnMetadataId() != null) {
            if (Integer.valueOf(-1).equals(filterParameter.getColumnMetadataId())) {
                solrQuery.append(" AND -(").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID).append(":").append("[* TO *]").append(")");
            } else {
                solrQuery.append(" AND (").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID).append(":").append(filterParameter.getColumnMetadataId()).append(")");
            }
        }
        return solrQuery.toString();
    }

//    private ListResult<OpportunityListItem> getOpportunityListResponse(ListingFilterParameter filterParameter, String solrQuery) {
//        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_OPPORTUNITY_CORE);
//        QueryResponse resp = null;
//        try {
//            resp = server.query(getOpportunitySolrQuery(filterParameter, solrQuery), SolrRequest.METHOD.POST);
//
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//        }
//        return getOpportunityFromSolrResult(resp, filterParameter);
//    }

    @Override
    public String getOpportunityCoreSolrQuery(EdsUser edsUser, ListingFilterParameter filterParameter) {
        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrTaskRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        if (StringUtils.isNotBlank(filterParameter.getSearchKey())) {
            if (filterParameter.isFromMobile()) {
                solrQuery.append(" AND (").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_NAME_COMPOSITE)
                        .append(":(");
                solrQuery.append(QueryBuilderForSolr.normalaizeKeywordByCriteria(filterParameter.getSearchKey(), true, true));
                solrQuery.append(") ");

            } else {
                solrQuery.append(" AND (")
                        .append(filterParameter.isLookUp() ? SolrOpportunityRepresenter.FIELD_OPPORTUNITY_NAME_COMPOSITE : SolrSaleInvoiceRepresenter.FIELD_COMPOSITE)
                        .append(":( ")
                        .append(QueryBuilderForSolr.normalaizeKeyword(filterParameter.getSearchKey(), filterParameter.isLookUp()))
                        .append(" )");
            }

            SolrSearchUtils searchUtils = new SolrSearchUtils();
            if (filterParameter.isFromMobile()) {
                Map<String, Double> fields = new HashMap<>();
                fields.put(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_NAME_COMPOSITE, SolrSearchUtils.HIGH_PRIORITY);
                searchUtils.generateSearchQuery(solrQuery, fields, filterParameter.getSearchKey());
            } else if (!filterParameter.isLookUp()) {
                searchUtils.generateSearchQuery(solrQuery, QueryBuilderForSolr.getOpportunitySearchFields(), filterParameter.getSearchKey());
            }
            solrQuery.append(")");
        }

        if (filterParameter.getAccountID() != null) {
            solrQuery.append(" AND (").append(SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_ID).append(":(").append(filterParameter.getAccountID()).append(")").append(")");
        }
        if (filterParameter.getContactID() != null && filterParameter.getContactID() > 0) {
            solrQuery.append(" AND ").append(SolrOpportunityRepresenter.FIELD_CRM_CONTACT_ID).append(":").append(filterParameter.getContactID());
        }
        if (filterParameter.getCampaignID() != null) {
            solrQuery.append(" AND ").append(SolrOpportunityRepresenter.FIELD_CAMPAIGN_ID).append(":").append(filterParameter.getCampaignID());
        }
        if (filterParameter.getStatusID() != null) {
            solrQuery.append(" AND ").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID).append(":").append(filterParameter.getStatusID());
        }

        if (!ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_OPPORTUNITIES_LIST)) {

            boolean supervisorAccess = ServerUtils.hasPermission(PermissionConstants.CRM_SHOW_SUPERVISED_OPPORTUNITIES);
            boolean ownerAccess = ServerUtils.hasPermission(PermissionConstants.OPPORTUNITY_SEE_OWN);
            if (supervisorAccess) {
                List<Integer> childEmployeeIds = new ArrayList<>();
                getChildEmployeeIds(edsUser.getObjectID(), childEmployeeIds);
                childEmployeeIds.add(edsUser.getObjectID());
                if (childEmployeeIds.size() > 0) {
                    solrQuery.append(" AND ").append(SolrOpportunityRepresenter.FIELD_ASSIGNEE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(childEmployeeIds, "0", " ")).append(")");
                }

            } else {
                StringBuilder clientIDsStr = new StringBuilder();
                if (filterParameter.getAccountID() != null) {
                    EdsCrmAccount crmAccount = crmAccountManager.get(filterParameter.getAccountID());
                    ownerAccess = ownerAccess && crmAccount.getOwners().contains(edsUser);
                }
                if (ownerAccess && !edsUser.hasRole(EdsRole.ADMIN_CODE)) {
                    List<Integer> clientIDs = crmAccountManager.getAccountIDsByOwner(edsUser.getObjectID());
                    if (clientIDs != null && clientIDs.size() > 0) {
                        for (Integer clientID : clientIDs) {
                            clientIDsStr.append(" ").append(clientID);
                        }
                    }
                }

                if (filterParameter.hasOnlyClientAccess() && edsUser.isClientContact() && edsUser.getClientContact().getCrmContact() != null) {
                    EdsCrmContact contact = edsUser.getClientContact().getCrmContact();
                    solrQuery.append(" AND (").append(SolrOpportunityRepresenter.FIELD_CRM_CONTACT_ID).append(":").append(contact.getObjectID());
                    if (contact.getCrmAccount() != null) {
                        solrQuery.append(" OR ").append(SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_ID).append(":").append(contact.getCrmAccount().getObjectID());
                    }
                    solrQuery.append(" ) ");
                } else {
                    solrQuery.append(" AND (").append(SolrOpportunityRepresenter.FIELD_ASSIGNEE_ID).append(":").append(edsUser.getObjectID());
                    solrQuery.append(" OR ").append(SolrOpportunityRepresenter.FIELD_ESTIMATOR_ID).append(":").append(edsUser.getObjectID());
                    solrQuery.append(" OR ").append(SolrOpportunityRepresenter.FIELD_OWNER_ID).append(":").append(edsUser.getObjectID());
                    if (rolePermissionManager.hasPermissionCheckedForCreator(PermissionConstants.CRM_OPPORTUNITIES_LIST)) {
                        solrQuery.append(" OR ").append(SolrOpportunityRepresenter.FIELD_CREATOR_ID).append(":").append(edsUser.getObjectID());
                    }
                    solrQuery.append(" OR ").append(SolrOpportunityRepresenter.FIELD_BACKUP_ASSIGNEE_ID).append(":").append(edsUser.getObjectID());
                }

                if (!clientIDsStr.toString().trim().isEmpty()) {
                    solrQuery.append(" OR (");
                    solrQuery.append(SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_ID).append(":(").append(clientIDsStr.toString().trim()).append(") ");
                    solrQuery.append(")");
                }
                solrQuery.append(" ) ");
            }
        }
        return solrQuery.toString();
    }

    private void getChildEmployeeIds(Integer supervisorId, List<Integer> allEmployeeIds) {
        List<Integer> childEmployeeIds = employeeManager.getChildEmployees(supervisorId);
        if (childEmployeeIds != null && !childEmployeeIds.isEmpty()) {
            for (Integer childId : childEmployeeIds) {
                allEmployeeIds.add(childId);
                getChildEmployeeIds(childId, allEmployeeIds);
            }
        }
    }


    @Override
    public OpportunityListItem getDefaultOne() {
        OpportunityListItem result = new OpportunityListItem();
        result.setLeadSources(getAsSelectItem(referenceManager.listReferences(EdsCrmContact._LEAD_SOURCE), ServerUtils.REFERENCE));
        result.setStages(getOpportunityStages(false));
        return result;
    }

    public ListResult<EventItem> getEventList(ListingFilterParameter filterParameter) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsEvent.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get activity list");

        FacetFilterRpc eventFacetFilter = filterParameter.getFacetFilter();
        ListPanelToolRpc panelTools = filterParameter.getListPanelTool();
        if (panelTools != null && panelTools.isCustomFieldsShown()) {
            filterParameter.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(commonService.getCompanyCustomFieldsForListView(ViewName.Activity));
        }
        if (filterParameter.getWorkflowID() != null && filterParameter.isWorkflowEventList()) {
            List<EdsEvent> events = eventManager.getWorkflowEvents(filterParameter);
            ArrayList<EventItem> eventItems = new ArrayList<>();
            if (events != null && !events.isEmpty()) {
                for (EdsEvent event : events) {
                    if (event != null) {
                        eventItems.add(event.getRPC(null));
                    }
                }
                return new ListResult<>(eventItems, eventItems.size());
            }
        }
        if (eventFacetFilter != null && !eventFacetFilter.isFilterChanges()) {
            eventFacetFilter = commonServiceLocal.getUserFacetFilter(eventFacetFilter);
        }
        EdsUser edsUser = employeeManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        String solrQuery = getEventCoreSolrQuery(edsUser, eventFacetFilter, filterParameter) +
                SolrFacetUtils.generatedFacetFilterSolrQuery(eventFacetFilter, edsCompany, SolrEventRepresenter.FIELD_START_DATE, SolrEventRepresenter.FIELD_END_DATE);
        return getEventListResponse(filterParameter, edsUser, solrQuery);
    }

    @Override
    public String getEventCoreSolrQuery(EdsUser edsUser, FacetFilterRpc eventFacetFilter, ListingFilterParameter filterParameter) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        StringBuilder solrQuery = new StringBuilder();
        List<Integer> relatedIds = relationManager.getCustomFormForCurrentUser(edsUser.getObjectID(), RelationItem.TYPE_EVENT);
        boolean hasSeeOwnPermission = ServerUtils.hasPermission(PermissionConstants.CRM_SEE_OWN_ACTIVITY_EVENT);
        boolean hasSeeAllPermission = ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_ACTIVITIES_LIST);

        solrQuery.append(SolrEventRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        if (filterParameter.getSearchKey() != null && !"".equals(filterParameter.getSearchKey())) {
            solrQuery.append(" AND ").append(SolrEventRepresenter.FIELD_COMPOSITE).append(":( ").append(QueryBuilderForSolr.normalaizeKeyword(filterParameter.getSearchKey())).append(" )");
        }
        if (filterParameter.getRelationType() != null || (filterParameter.getRelationTypes() != null && !filterParameter.getRelationTypes().isEmpty())) {
            String relationID = filterParameter.getRelationID() == null ? "[* TO *] AND *:*" : filterParameter.getRelationID().toString();
            if (filterParameter.getRelationTypes() != null && !filterParameter.getRelationTypes().isEmpty()) {
                solrQuery.append(" AND (").append(SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID).append(filterParameter.getRelationTypes().get(0)).append(":(").append(relationID).append(")");
                for (int i = 1; i < filterParameter.getRelationTypes().size(); i++) {
                    solrQuery.append(" OR ").append(SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID).append(filterParameter.getRelationTypes().get(i)).append(":(").append(relationID).append(") ");
                }
                solrQuery.append(" )");
            } else {
                solrQuery.append(" AND (").append(SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID).append(filterParameter.getRelationType()).append(":(").append(relationID).append(")");
                solrQuery.append(" OR ").append(SolrEventRepresenter.FIELD_CONTACT_ID).append(":").append(relationID).append(")");
            }

        }
        if (filterParameter.getEventType() != null && !ServerUtils.hasPermission(PermissionConstants.CRM_ACTIVITIES_LOG_CALL_VIEW) && Appointment.FROM_HRMS != filterParameter.getCreatedFrom()) {
            solrQuery.append(" AND (").append(SolrEventRepresenter.FIELD_ACTIVITY_TYPE_ID).append(":").append(Appointment.EVENT).append(")");
        } else if (filterParameter.getEventType() != null) {
            if (filterParameter.getEventType() == Appointment.CALL_AND_SMS) {
                solrQuery.append(" AND ").append(SolrEventRepresenter.FIELD_ACTIVITY_TYPE_ID).append(":(").append(Appointment.CALL_LOG).append(" OR ").append(Appointment.SMS).append(")");
            } else {
                solrQuery.append(" AND ").append(SolrEventRepresenter.FIELD_ACTIVITY_TYPE_ID).append(":").append(filterParameter.getEventType());
            }
        }
        if (filterParameter.getCreatedFrom() != null && !ServerUtils.hasPermission(filterParameter.getCreatedFrom().equals(Appointment.FROM_CRM) ? PermissionConstants.CRM_SEE_ALL_ACTIVITIES_LIST :
                PermissionConstants.HRMS_ACTIVITIES_SEE_ALL)) {
            boolean ownerAccess = ServerUtils.hasPermission(PermissionConstants.ACTIVITY_SEE_OWN);
            if (ownerAccess && !edsUser.hasRole(ADMIN_CODE) && RelationItem.TYPE_CRM_ACCOUNT.equals(filterParameter.getRelationType()) &&
                    filterParameter.getRelationID() != null) {

                StringBuilder clientIDsStr = new StringBuilder();
                EdsCrmAccount crmAccount = crmAccountManager.get(filterParameter.getRelationID());
                ownerAccess = ownerAccess && crmAccount.getOwners().contains(edsUser);

                if (ownerAccess) {
                    List<Integer> clientIDs = crmAccountManager.getAccountIDsByOwner(edsUser.getObjectID());
                    if (clientIDs != null && !clientIDs.isEmpty()) {
                        for (Integer clientID : clientIDs) {
                            clientIDsStr.append(" ").append(clientID);
                        }
                    }
                    if (!clientIDsStr.toString().trim().isEmpty()) {
                        if (filterParameter.getRelationTypes() != null && !filterParameter.getRelationTypes().isEmpty()) {
                            solrQuery.append(" AND (").append(SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID).append(filterParameter.getRelationTypes().get(0)).append(":(").append(clientIDsStr).append(") ");
                            for (int i = 1; i < filterParameter.getRelationTypes().size(); i++) {
                                solrQuery.append(" OR ").append(SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID).append(filterParameter.getRelationTypes().get(i)).append(":(").append(clientIDsStr).append(") ");
                            }
                            solrQuery.append(" )");
                        } else {
                            solrQuery.append(" AND (").append(SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID).append(filterParameter.getRelationType()).append(":(").append(clientIDsStr).append("))");
                        }
                    } else {
                        solrQuery.append(" AND ").append(SolrEventRepresenter.FIELD_OWNER_ID).append(":").append(edsUser.getObjectID());
                    }
                } else {
                    solrQuery.append(" AND ").append(SolrEventRepresenter.FIELD_OWNER_ID).append(":").append(edsUser.getObjectID());
                }

            } else {
                solrQuery.append(" AND (").append(SolrEventRepresenter.FIELD_OWNER_ID).append(":").append(edsUser.getObjectID());
                if (!hasSeeAllPermission && hasSeeOwnPermission && relatedIds != null && !relatedIds.isEmpty()) {
                    for (Integer relatedId : relatedIds) {
                        solrQuery.append(" OR (").append(SolrEventRepresenter.FIELD_EVENT_ID).append(":").append(relatedId).append(") ");

                    }
                }
                solrQuery.append(")");

            }
        }

        if (filterParameter.getCreatedFrom() != null) {
            solrQuery.append(" AND (").append(SolrEventRepresenter.FIELD_CREATED_FROM_ID).append(":").append(filterParameter.getCreatedFrom());
            solrQuery.append(" OR ").append(SolrEventRepresenter.FIELD_CREATED_FROM_ID).append(":").append(Appointment.FROM_BOTH).append(")");
        }
        if (filterParameter.getDates() != null && !filterParameter.getDates().isEmpty()) {
            filterParameter.getDates().sort(Date::compareTo);

            Date startDate = filterParameter.getDates().get(0);
            Date endDate = filterParameter.getDates().get(filterParameter.getDates().size() - 1);

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(startDate);

            ServerUtils.setBeginningOfTheDay(calendar);
            String startDateStr = format.format(calendar.getTime());

            calendar = new GregorianCalendar();
            calendar.setTime(endDate);

            ServerUtils.setEndOfTheDay(calendar);
            String endDateStr = format.format(calendar.getTime());

            solrQuery.append(" AND ((").append(SolrEventRepresenter.FIELD_START_DATE).append(":[").append(startDateStr).append(" TO * ]) ");
            solrQuery.append(" AND (").append(SolrEventRepresenter.FIELD_START_DATE).append(":[ * TO ").append(endDateStr).append(" ]))");

        }

        if (eventFacetFilter != null) {
            if (eventFacetFilter.getCustomData().containsKey(Appointment.TODAY) && Boolean.valueOf(eventFacetFilter.getCustomData().get(Appointment.TODAY))) {

                Calendar calendar = new GregorianCalendar();
                calendar.setTime(new Date());

                ServerUtils.setBeginningOfTheDay(calendar);
                String startDate = format.format(calendar.getTime());

                ServerUtils.setEndOfTheDay(calendar);
                String endDate = format.format(calendar.getTime());

                solrQuery.append(" AND (((").append(SolrEventRepresenter.FIELD_START_DATE).append(":[").append(startDate).append(" TO * ]) ");
                solrQuery.append(" AND (").append(SolrEventRepresenter.FIELD_START_DATE).append(":[ * TO ").append(endDate).append(" ]))");
                solrQuery.append(" OR ((").append(SolrEventRepresenter.FIELD_END_DATE).append(":[").append(startDate).append(" TO * ])");
                solrQuery.append(" AND (").append(SolrEventRepresenter.FIELD_END_DATE).append(":[ * TO ").append(endDate).append(" ])))");
            }
        }
        return solrQuery.toString();
    }


    private ListResult<EventItem> getEventListResponse(ListingFilterParameter filterParameter, EdsUser edsUser, String solrQuery) {
        Page<EventSolrDoc> eventSolrDocPage = eventSolrComponent.getList(filterParameter, solrQuery);
        return getEventFromSolrResult(eventSolrDocPage, filterParameter);
    }

    public SolrQuery getOpportunitySolrQuery(ListingFilterParameter filterParameter, String solrQuery) {
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(filterParameter.getStart());
        query.setRows(filterParameter.getLimit() == 0 ? 20 : filterParameter.getLimit());

        if (!filterParameter.isSearchButton()) {
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                boolean desc = !filterParameter.isAscending();
                String sortField = SolrOpportunityRepresenter.getSortField(filterParameter.getSortField());
                if (sortField != null) {
                    query.setSort(sortField, desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc);
                } else {
                    CustomFieldsUtils.setCustomFieldsSortableNameToSolr(filterParameter.getSortField(), desc, query, true);
                }
            } else {
                query.setSort(SolrOpportunityRepresenter.FIELD_MODIFICATION_DATE, SolrQuery.ORDER.desc);
            }
        }
        return query;
    }

    @Override
    public SolrQuery getEventSolrQuery(ListingFilterParameter filterParameter, String solrQuery) {
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(filterParameter.getStart());
        query.setParam(CommonParams.ROWS, filterParameter.getLimit() > 0 ? String.valueOf(filterParameter.getLimit()) : "20");

        if (!filterParameter.isSearchButton()) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(filterParameter.getSortField())) {
                query.addSort(SolrEventRepresenter.getSortField(filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())
                        ? filterParameter.getSortField()
                        : null), filterParameter.isAscending()
                        ? SolrQuery.ORDER.asc
                        : SolrQuery.ORDER.desc);

            } else {
                query.setSort(SolrEventRepresenter.FIELD_LAST_UPDATE_DATE, SolrQuery.ORDER.desc);
            }

        } else {
            query.setSort(SolrEventRepresenter.FIELD_LAST_UPDATE_DATE, SolrQuery.ORDER.desc);
        }
        return query;
    }

    private ListResult<OpportunityListItem> getOpportunityFromSolrResult(Page<OpportunitySolrDoc> opportunitySolrDocPage, ListingFilterParameter filterParameter) {
        int totalNumber = 0;
        ArrayList<OpportunityListItem> opportunityItems = new ArrayList<>();
        int i = 0;
        CurrencyItem baseCurrency = invoiceServiceLocal.getBaseCurrency();
        Map<Integer, EdsReference> stages = EdsReference.getListAsMapIntegerAndValue(referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_STAGE));
        boolean requireContractUpload = opportunityManager.getUser().getCompany().getCompanySettings().getOpportunityRequireContractUpload();

        EdsFormProperty formProperty = formPropertyManager.getByFormID(LayoutRPC.OPPORTUNITY_FORM);
        boolean amountWidgetEnabled = false;
        boolean closeDateEnabled = false;
        boolean leadSourceEnabled = false;
        if (formProperty != null) {
            Gson gson = new Gson();
            FormProperty[] formFields = gson.fromJson(formProperty.getSettingsJSONData(), FormProperty[].class);
            if (formFields != null) {
                for (FormProperty field : formFields) {
                    boolean disabled = field.isDisabled();
                    if (field.getRoleEdit() != null && field.getRoleEdit().size() > 0) {
                        if (userManager.getUser().hasEitherRoles(field.getRoleEdit().toArray(new Integer[]{}))) {
                            disabled = false;
                        }
                    }
                    if (field != null && CustomFormConstants.CRM_OPPORTUNITY_AMOUNT.equals(field.getCode())) {
                        amountWidgetEnabled = disabled;
                    } else if (field != null && CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE.equals(field.getCode())) {
                        closeDateEnabled = disabled;
                    } else if (field != null && CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE.equals(field.getCode())) {
                        leadSourceEnabled = disabled;
                    }
                }
            }
        }

        boolean isProjectInLine = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);


        if (!opportunitySolrDocPage.isEmpty()) {
            totalNumber = (int) opportunitySolrDocPage.getTotalElements();
            for (OpportunitySolrDoc doc : opportunitySolrDocPage.getContent()) {
                if (doc != null) {
                    OpportunityListItem opportunity = getOpportunitySolrDocumentAsRPC(doc, filterParameter, stages, baseCurrency, isProjectInLine);
                    opportunity.setRequireContractUpload(requireContractUpload);
                    opportunity.setAmountWidgetDisable(amountWidgetEnabled);
                    opportunity.setCloseDateDisable(closeDateEnabled);
                    opportunity.setLeadSourceDisable(leadSourceEnabled);
                    opportunityItems.add(opportunity);
                }
            }
        }
        ListResult<OpportunityListItem> result = new ListResult<>(opportunityItems, totalNumber);
        result.setDefaultOne(editOpportunity(null));
        return result;
    }

    private OpportunityListItem getOpportunitySolrDocumentAsRPC(OpportunitySolrDoc doc, ListingFilterParameter fp, Map<Integer, EdsReference> stages, CurrencyItem baseCurrency, boolean isProjectInLine) {
        OpportunityListItem item = new OpportunityListItem();
        item.setConvertedToProject(doc.getOpportunityConvertProject());
        item.setConvertedLead(doc.getConvertedFromLead());
        item.setObjectId(doc.getOpportunityId());
        item.setOpportunityName(doc.getOpportunityName());
        item.setAccountId(doc.getCrmAccountId());
        item.setAccount(doc.getCrmAccountName());
        item.setAccountNumber(doc.getCrmAccountNumber());
        item.setContactId(doc.getCrmContactId());
        item.setContact(doc.getCrmContactName());
        item.setContactPrimaryEmail(doc.getCrmContactPrimaryEmail());
        item.setContactEmailOptOut(doc.getCrmContactEmailAllowed());
        item.setContactPrimaryPhone(doc.getCrmContactPrimaryPhone());
        item.setCampaign(doc.getCampaignName());
        item.setCampaignId(doc.getCampaignId());
        item.setAssigneeId(doc.getAssigneeId());
        item.setAssignee(doc.getAssigneeName());
        item.setBackupAssigneeID(doc.getBackupAssigneeId());
        item.setBackupAssignee(doc.getBackupAssigneeName());
        if (!fp.isFromExcelPDF()) {
            Integer rfqId = rfqManager.getByOpportunity(item.getObjectId());
            if (rfqId != null) {
                item.setRfqId(rfqId);
            }
        }
        item.setStageId(doc.getOpportunityStageId());
        item.setStageName(getStageName(doc));
        item.getStage().setCode(doc.getOpportunityStageCode());
        if (item.getStageId() != null && stages.containsKey(item.getStageId()) && stages.get(item.getStageId()) != null) {
            ((ReferenceItem) item.getStage()).setCssStyle(stages.get(item.getStageId()).getCssStyle());

            EdsReference reference = stages.get(item.getStageId());
            EdsUser user = userManager.getUser();

            boolean draggable = false;
            if (reference.getAllowedRoles().isEmpty() || !reference.getAllowedRoles().isEmpty() && user.hasEitherRoles(reference.getAllowedRoles().toArray(new EdsRole[]{}))) {
                item.setDraggable(true);
                draggable = true;
            }

            if (reference.getViewOnlyRoles() != null && !reference.getViewOnlyRoles().isEmpty()
                    && !draggable && user.hasEitherRoles(reference.getViewOnlyRoles().toArray(new EdsRole[]{}))) {
                item.setDraggable(false);
            }

            if (reference.getOppEditBtnRole() == null || (reference.getOppEditBtnRole() != null && reference.getOppEditBtnRole().isEmpty()) || user == null || (user != null && user.hasEitherRoles(reference.getOppEditBtnRole().toArray(new EdsRole[]{})))) {
                item.setAllowEdit(true);
            }
        }
        item.setClosingDate(doc.getClosingDate());
        item.setCreatedDate(doc.getCreationDate());
        item.setCreatorID(doc.getCreatorId());
        item.setCreatorName(doc.getCreatorName());
        item.setUpdatedDate(doc.getModificationDate());
        item.setAmount(doc.getAmount());
        item.setExpectedRevenue(doc.getExpectedRevenue());
        item.setCurrencyId(doc.getCurrencyId());
        item.setCurrency(doc.getCurrencyName());
        if (doc.getOpportunityStringNumber() != null && doc.getOpportunityIntNumber() != null) {
            item.setNumberData(new NumberData(doc.getOpportunityStringNumber(), doc.getOpportunityIntNumber()));
        }
        Integer projectId = doc.getRelatedProjectId();
        if (!fp.isFromExcelPDF()) {
            item.setProject(new SelectItem(projectId, getProjectName(isProjectInLine, doc)));
        }
        if (fp.getListPanelTool() != null) {
            item.setCustomFieldsMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(doc, fp.getListPanelTool().getColumnCodeName()));
        }

        //FOR OUTLOOK
        item.setType(doc.getTypeName());
        item.setTypeId(doc.getTypeId());
        item.setLeadSource(doc.getLeadSourceName());
        item.setLeadSourceId(doc.getLeadSourceId());
        item.setProbability(doc.getProbability());
        item.setNextStep(doc.getNextStep());

        item.setCountryName(doc.getCrmAccountCountryName());

        item.setRelationValueMap(SolrRelationUtils.getRelationBaseSolrDocValue(doc, EdsRelation.TYPE_OPPORTUNITY));
        item.setHasAttachments(doc.getHasAttachment() != null ? doc.getHasAttachment() : false);
        return item;
    }

    private String getStageName(OpportunitySolrDoc doc) {
        Locale userLocale = ServerSecurityContext.getInstance().getUserLocale();
        if (doc.getStageLocaleByCode(userLocale.getLanguage()) != null) {
            return doc.getStageLocaleByCode(userLocale.getLanguage());
        }
        return referenceWfmMessageSource.localize(doc.getOpportunityStageCode(), doc.getOpportunityStageName());
    }

    private String getProjectName(Boolean isProjectInLine, OpportunitySolrDoc doc) {
        if (isProjectInLine) {
            return ServerUtils.asListToString(doc.getMultiProjectNumberName());
        } else {
            String number = doc.getRelatedProjectNumber();
            String name = doc.getRelatedProjectName();
            if (number != null && !"".equals(number) && name != null && !"".equals(name)) {
                return number + SolrPurchaseInvoiceRepresenter.ARROW + name;
            } else {
                return "";
            }

        }
    }

    private ListResult<EventItem> getEventFromSolrResult(Page<EventSolrDoc> eventSolrDocPage, ListingFilterParameter filterParameter) {
        int totalNumber = (int) eventSolrDocPage.getTotalElements();
        ArrayList<EventItem> evenItems = new ArrayList<>();
        List<EventSolrDoc> realSolrDocuments = eventSolrComponent.getDocumentsExistingInBase2(eventSolrDocPage.getContent());
        for (EventSolrDoc doc : realSolrDocuments) {
            EventItem evenItem = EdsEvent.wrapSolrDocumentToRPC(doc, filterParameter);
            evenItem.setLinkURL(EncryptionHelper.encryptURL("event|summary/" + evenItem.getObjectID() + (evenItem.isCallLog() ? "/true" : "")));
            evenItems.add(evenItem);
        }
        return new ListResult<>(evenItems, totalNumber);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OpportunityListItem getOpportunity(Integer objectId) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsOpportunity.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(objectId);
        ServerUtils.kpiLog(log, kpiLog, "Get opportunity to view");
        EdsOpportunity opportunity = opportunityManager.get(objectId);
        OpportunityListItem item = getOpportunity(opportunity, true);
        if (opportunity != null && opportunity.getCrmContact() != null) {
            item.setContactItem(opportunity.getCrmContact().getRPC(new ListingFilterParameter(false)));
        } else if (item != null && item.getAccountId() != null) {
            EdsCrmAccount account = crmAccountManager.get(item.getAccountId());
            ContactListItem contactListItem = new ContactListItem();
            contactListItem.setContactType(TYPE_ACCOUNT);
            contactListItem.setOwnerId(account.getOwners().size() > 0 ? account.getOwners().get(0).getObjectID() : null);
            contactListItem.setOwner(account.getOwners().size() > 0 ? account.getOwners().get(0).getName() : null);
            contactListItem.setWorkEmail(account.getEmail());
            contactListItem.setWorkPhone(account.getPhone());
            contactListItem.setAccountIndustry(account.getIndustry() != null ? account.getIndustry().getLocalizedName() : "");
            contactListItem.setFirstName(account.getName());
            item.setContactItem(contactListItem);
        }
        EdsUser user = userManager.getUser();
        if (item != null) {
            item.setStageHistoryColConf(itemTableSettingService.getColumnConfigs(ItemTableEnum.OPPORTUNITY_STAGE_HISTORY));

            Integer rfqId = rfqManager.getByOpportunity(objectId);
            if (rfqId != null) {
                item.setRfqId(rfqId);
            }

            if (opportunity.getStage() != null) {

                item.setDraggable(opportunity.getStage().getAllowedRoles().isEmpty() || !opportunity.getStage().getAllowedRoles().isEmpty() && user.hasEitherRoles(opportunity.getStage().getAllowedRoles().toArray(new EdsRole[]{})));


                if (opportunity.getStage().getOppEditBtnRole() == null || (opportunity.getStage().getOppEditBtnRole() != null && opportunity.getStage().getOppEditBtnRole().isEmpty()) || user == null || (user != null && user.hasEitherRoles(opportunity.getStage().getOppEditBtnRole().toArray(new EdsRole[]{})))) {
                    item.setAllowEdit(true);
                }
            }
            item.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_OPPORTUNITY, item.getObjectId())));
            item.setAssignees(getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE));
            item.setLeadSources(getAsSelectItem(referenceManager.listReferences(EdsCrmContact._LEAD_SOURCE), ServerUtils.REFERENCE));
            item.setStages(getOpportunityStages(false));
            item.setTypes(getAsSelectItem(referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_TYPE), ServerUtils.REFERENCE));
            item.setTemplates(getOppotunityPdfTemplates(PdfReferenceCodeNameEnum.OPPORTUNITY.name()).getItems());
            CurrencyItem baseCurrency = invoiceServiceLocal.getBaseCurrency();
            if (baseCurrency != null) {
                item.setBaseCurrencyID(baseCurrency.getId());
                item.setBaseCurrencyName(baseCurrency.getName());
            }
            item.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_OPPORTUNITY));


            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setOpportunityID(opportunity.getObjectID());
            return item;
        }
        return null;
    }

    private CustomFormItemPdfTemplateList getOppotunityPdfTemplates(String type) {
        List<EdsCompanyPdfTemplate> templates = companyPdfTemplateManager.getCompanyPDFTemplatesByType(type, false);
        SelectItem[] items = new SelectItem[templates.size()];
        int i = 0;
        Integer defaultTemplateID = null;
        for (EdsCompanyPdfTemplate t : templates) {
            items[i] = new SelectItem(t.getObjectID(), t.getName());
            if (t.isDefaultTemplate()) {
                defaultTemplateID = t.getObjectID();
            }
            i++;
        }
        return new CustomFormItemPdfTemplateList(items, defaultTemplateID);
    }

    @Override
    public ArrayList<OpportunityListItem> getSubOpportunities(Integer objectId) {
        ArrayList<OpportunityListItem> opportunityListItems = new ArrayList<>();
        EdsOpportunity opportunity = opportunityManager.get(objectId);
        if (opportunity != null) {
            opportunity.getSubOpportunities().forEach(op -> opportunityListItems.add(getOpportunity(op, true)));
        }
        return opportunityListItems;
    }

    @Override
    public OpportunityItem[] getOpportunityItems(Integer objectId) {
        ArrayList<OpportunityItem> opportunityItems = new ArrayList<>();
        EdsOpportunity opportunity = opportunityManager.get(objectId);
        if (opportunity != null) {
            opportunity.getOpportunityItems().forEach(it -> {
                OpportunityItem item = new OpportunityItem();
                item.setItemID(it.getItem() != null ? it.getItem().getObjectID() : null);
                item.setItemName(it.getItem() != null ? it.getItem().getName() : it.getItemName());
                item.setItemNumber(it.getItem() != null ? it.getItem().getProductNumber() : "");
                item.setDescription(it.getDescription());
                item.setQty(it.getQty());
                if (it.getUnitMeasurement() != null) {
                    item.setUnitMeasurement(it.getUnitMeasurement().getAsSelectItem());
                }
                item.setPrice(it.getPrice());
                item.setDiscountPercent(it.getDiscount());
                item.setDiscountAmount(it.getDiscountAmount());

                if (it.getVat() != null) {
                    item.setTaxItem(it.getVat().createTaxItem());
                    item.setTaxAmount(it.getItemCalculatedTaxAmount());
                }

                item.setNet(it.getNet());
                item.setSubTotal(it.getSubTotal());

                item.setSupplierID(it.getSupplierID());
                item.setSupplierName(it.getSupplierName());
                if (opportunity.getCurrency() != null) {
                    item.setCurrency(opportunity.getCurrency().getName());
                }
                if (it.getCategory() != null) {
                    item.setProductCategory(new SelectItem(it.getCategory().getObjectID(), it.getCategory().getName()));
                }
                if (it.getBrand() != null) {
                    item.setProductBrand(new SelectItem(it.getBrand().getObjectID(), it.getBrand().getName()));
                }
                if (it.getProject() != null) {
                    item.setProject(new SelectItem(it.getProject().getObjectID(), it.getProject().getNumber() != null ? it.getProject().getNumber() + " -> " + it.getProject().getName() : it.getProject().getName()));
                }
                item.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(it.getCustomFields(), commonService.getCompanyCustomFields(ViewName.OpportunitySubItem)));
                opportunityItems.add(item);
            });
        }
        return opportunityItems.toArray(new OpportunityItem[]{});
    }

    @Override
    public OpportunityItem[] getLeadItems(Integer objectId) {
        ArrayList<OpportunityItem> crmContacts = new ArrayList<>();
        EdsCrmContact crmContact = crmContactManager.get(objectId);
        if (crmContact != null) {
            crmContact.getCrmContactItems().forEach(it -> {
                OpportunityItem item = new OpportunityItem();
                item.setItemID(it.getItem() != null ? it.getItem().getObjectID() : null);
                item.setItemName(it.getItem() != null ? it.getItem().getName() : it.getItemName());
                item.setItemNumber(it.getItem() != null ? it.getItem().getProductNumber() : "");
                item.setDescription(it.getDescription());
                item.setQty(it.getQty());
                if (it.getUnitMeasurement() != null) {
                    item.setUnitMeasurement(it.getUnitMeasurement().getAsSelectItem());
                }
                item.setPrice(it.getPrice());

                if (it.getVat() != null) {
                    item.setTaxItem(it.getVat().createTaxItem());
                    item.setTaxAmount(it.getItemCalculatedTaxAmount());
                }

                item.setNet(it.getNet());
                item.setSubTotal(it.getSubTotal());

                item.setSupplierID(it.getSupplierID());
                item.setSupplierName(it.getSupplierName());
                if (it.getCategory() != null) {
                    item.setProductCategory(new SelectItem(it.getCategory().getObjectID(), it.getCategory().getName()));
                }
                if (it.getBrand() != null) {
                    item.setProductBrand(new SelectItem(it.getBrand().getObjectID(), it.getBrand().getName()));
                }
                item.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(it.getCustomFields(), commonService.getCompanyCustomFields(ViewName.OpportunitySubItem)));
                crmContacts.add(item);
            });
        }
        return crmContacts.toArray(new OpportunityItem[]{});
    }

    private OpportunityListItem getOpportunity(EdsOpportunity opportunity, boolean brief) {
        if (opportunity == null) {
            return null;
        }
        OpportunityListItem item = new OpportunityListItem();
        item = opportunity.getRPC(item);
        if (item.getStage() != null) {
            String locale = ServerUtils.getUserLocale().getLanguage();
            if (opportunity.getStage().getLocale() != null && opportunity.getStage().getLocale().getLocaleByCode(locale) != null) {
                item.getStage().setName(opportunity.getStage().getLocale().getLocaleByCode(locale));
            } else {
                item.getStage().setName(referenceWfmMessageSource.localize(item.getStageCode(), item.getStage().getName()));
            }
        }
        if (opportunity.getRejectReason() != null) {
            SelectItem rejectionItem = new SelectItem();
            rejectionItem.setId(opportunity.getRejectReason().getObjectID());
            rejectionItem.setCode(opportunity.getRejectReason().getCode());
            rejectionItem.setName(opportunity.getRejectReason().isSystemReference() && !opportunity.getRejectReason().isChanged() ? referenceWfmMessageSource.localize(opportunity.getRejectReason().getCode()) : opportunity.getRejectReason().getName());

            item.setRejectionReason(rejectionItem);
        }
        if (opportunity.getNote() != null) {
            item.setNote(opportunity.getNote());
        }
        item.setRequireContractUpload(opportunityManager.getUser().getCompany().getCompanySettings().getOpportunityRequireContractUpload());
        if (opportunity.getOpportunityItems().size() > 0) {
            OpportunityItem[] items = new OpportunityItem[opportunity.getOpportunityItems().size()];
            ArrayList<OpportunityItem> listItems = new ArrayList<>();
            int index = 0;
            for (EdsOpportunityItem it : opportunity.getOpportunityItems()) {
                items[index] = new OpportunityItem();
                items[index].setItemID(it.getItem() != null ? it.getItem().getObjectID() : null);
                items[index].setItemName(it.getItem() != null ? it.getItem().getName() : it.getItemName());
                items[index].setItemNumber(it.getItem() != null ? it.getItem().getProductNumber() : "");
                items[index].setQty(it.getQty());
                items[index].setDescription(it.getDescription());
                items[index].setPrice(it.getPrice());
                if (it.getVat() != null) {
                    items[index].setTaxItem(it.getVat().createTaxItem());
                    items[index].setTaxAmount(it.getItemCalculatedTaxAmount());
                }
                items[index].setNet(it.getNet());
                items[index].setDiscountPercent(it.getDiscount());
                items[index].setDiscountAmount(it.getDiscountAmount());
                if (it.getItemDiscount() != null) {
                    items[index].setDiscountItemName(it.getItemDiscount().getName());
                    items[index].setDiscountItemID(it.getItemDiscount().getObjectID());
                }
                if (it.getItem() != null) {
                    items[index].setDiscountItems(opportunity.getProductDiscounts(it.getItem().getDiscounts()));
                }

                if (it.getUnitMeasurement() != null) {
                    items[index].setUnitMeasurement(it.getUnitMeasurement().getAsSelectItem());
                }
                if (it.getCategory() != null) {
                    items[index].setProductCategory(new SelectItem(it.getCategory().getObjectID(), it.getCategory().getName()));
                }
                if (it.getBrand() != null) {
                    items[index].setProductBrand(new SelectItem(it.getBrand().getObjectID(), it.getBrand().getName()));
                }
                items[index].setSupplierID(it.getSupplierID());
                items[index].setSupplierName(it.getSupplierName());
                items[index].setDiscountItemFixedType(it.getDiscountItemFixedType());

                ArrayList<CompanyCustomFieldItem> itemCustomFields = new ArrayList<>();

                for (CompanyCustomFieldItem customFieldItem : commonService.getCompanyCustomFields(ViewName.OpportunitySubItem)) {
                    itemCustomFields.add(customFieldItem.cloneObject());
                }
                items[index].setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(it.getCustomFields(), itemCustomFields));
                listItems.add(items[index]);
                index++;
            }
            item.setItems(listItems.toArray(new OpportunityItem[0]));

            if (opportunity != null) {
                Set<EdsOpportunityCustomItemTable> itemTables = opportunity.getItemTables();

                HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

                if (itemTables != null || itemTables.size() > 0) {

                    for (EdsOpportunityCustomItemTable itemTable : itemTables) {
                        CustomTableRpc rpc = itemTable.getRpc();

                        rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                                commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.OpportunityItemTable, rpc.getUuid())));

                        map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                    }
                    item.setCustomTableItems(map);
                }
                Map<String, ArrayList<CustomTableRpc>> tableItems = item.getCustomTableItems();


                for (List<CustomTableRpc> tableRpcs : tableItems.values()) {
                    tableRpcs.sort(Comparator.comparing(CustomTableRpc::getId));
                }
            }
        }

        item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(opportunity.getCustomFields(),
                commonService.getCompanyCustomFields(ViewName.Opportunity)));
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setOpportunityID(opportunity.getObjectID());
        return item;
    }

    @Transactional
    public Integer saveOpportunity(OpportunityListItem item) {
        try {
            SelectItem opportunityItem = saveOpportunity(item, null);
            return opportunityItem.getId();
        } catch (NumberExistingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Integer saveOpportunityWithAttachments(OpportunityListItem item, List<MultipartFile> attachments) {
        try {
            SelectItem opportunityItem = saveOpportunityWithAttachments(item, null, attachments);
            return opportunityItem.getId();
        } catch (NumberExistingException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    @Transactional
    public SelectItem saveOpportunity(OpportunityListItem item, Integer convertingTo) throws NumberExistingException {
        return saveOpportunityWithAttachments(item, convertingTo, null);
    }


    @Transactional
    public SelectItem saveOpportunityWithAttachments(OpportunityListItem item, Integer convertingTo, List<MultipartFile> attachments) throws NumberExistingException {
        if (item.getNumberData() != null &&
                item.getNumberData().getNumberString() != null &&
                !"".equals(item.getNumberData().getNumberString().trim()) &&
                opportunityManager.isOpportunityNumberExists(item.getNumberData().getNumberString(), item.getObjectId())) {
            item.setNumberData(generateOpportunityNumber());
//            throw new NumberExistingException("Opportunity with number " + item.getNumberData().getNumberString() + " already exists.");
        }

        EdsOpportunity opportunity = new EdsOpportunity();
        if (item.getObjectId() != null) {
            opportunity = opportunityManager.get(item.getObjectId());
        }
        opportunity.clear();
        if (item.getOwnerID() != null) {
            opportunity.setOwner(userManager.get(item.getOwnerID()));
        }
        if (item.getAssigneeId() != null) {
            opportunity.setAssignee(employeeManager.get(item.getAssigneeId()));
        }

        opportunity.setBackupAssignee(employeeManager.get(item.getBackupAssigneeID()));

        if (item.getNumberData() != null) {
            opportunity.setNumber(item.getNumberData().getNumberString());
            opportunity.setIntNumber(item.getNumberData().getIntNumber());
        }
        opportunity.setName(item.getOpportunityName());
        opportunity.setAmount(item.getAmount());
        if (item.getCurrencyId() != null) {
            opportunity.setCurrency(currencyManager.get(item.getCurrencyId()));
        } else {                            // set curency for opportunity is its Account (see T3582)
            if (item.getAccountId() != null) {
                EdsCrmAccount account = crmAccountManager.get(item.getAccountId());
                if (opportunity.getCurrency() == null) {
                    opportunity.setCurrency(currencyManager.get(account != null ? account.getCurrency() != null ? account.getCurrency().getObjectID() : null : null));
                    if (opportunity.getCurrency() != null) {
                        CurrencyLayerItem layerItem = currencyService.getExchangeRateDouble(opportunity.getCurrency().getName(), ServerUtils.getCompanyCurrencyName(), new Date(), 0);
                        if (layerItem != null) {
                            double exchangeRate = layerItem.getRate();
                            item.setExchangeRate(new BigDecimal(exchangeRate));
                        }
                    }
                }
            }
        }
        if (item.getExchangeRate() != null && item.getAmount() != null) {
            opportunity.setAmountBaseCurrency(((BigDecimal.valueOf(item.getAmount())).divide(item.getExchangeRate(), 8, RoundingMode.HALF_UP)).doubleValue());
        } else {
            opportunity.setAmountBaseCurrency(item.getAmount());
        }
        opportunity.setClosingDate(item.getClosingDate());
        opportunity.setProbability(item.getProbability());
        opportunity.setNextStep(item.getNextStep());

        opportunity.setTaxCalculationType(item.getTaxCalculationType());
        opportunity.setSubTotal(item.getSubTotal());
        opportunity.setDiscountTotal(item.getDiscountTotal());
        opportunity.setTaxTotal(item.getTaxTotal());
        opportunity.setTotal(item.getTotal());
        opportunity.setTotalInBase(item.getTotalInBase());
        opportunity.setQuantityTotal(item.getQuantityTotal());

        if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
            StringBuilder changesBuilder = new StringBuilder();
            for (CompanyCustomFieldItem cit : item.getCustomFields()) {
                changesBuilder.append(opportunity.getCustomFields() != null && CustomFieldsUtils.getObjectValue(opportunity.getCustomFields(), cit.getColumnCode()) != null ? getChanges(CustomFieldsUtils.getObjectValue(opportunity.getCustomFields(), cit.getColumnCode()), cit) : (cit.getColumnCode() + ","));
            }
            String changes = changesBuilder.toString();
            if (!"".equals(changes)) {
                opportunity.addCustomFieldChanges(changes);
            }
        }
        opportunity.setCustomFields(saveCustomFields(opportunity.getCustomFields(), item.getCustomFields()));
        if (item.getLeadSourceId() != null) {
            opportunity.setLeadSource(referenceManager.get(item.getLeadSourceId()));
        } else {
            opportunity.setLeadSource(null);
        }
        if (item.getTypeId() != null) {
            opportunity.setType(referenceManager.get(item.getTypeId()));
        } else {
            opportunity.setType(null);
        }


        if (item.getAccountId() != null) {
            EdsCrmAccount account = crmAccountManager.get(item.getAccountId());
            opportunity.setCrmAccount(account);
            if (account != null && account.getEntityID() != null) {
                opportunity.setEntityID(account.getEntityID());
            }
        } else {
            opportunity.setCrmAccount(null);
        }
        if (item.getContactId() != null) {
            EdsCrmContact contact = crmContactManager.get(item.getContactId());
            if (contact != null) {
                opportunity.setCrmContact(contact);
                if (contact.getEntityID() != null) {
                    opportunity.setEntityID(contact.getEntityID());
                }
            }
        } else {
            opportunity.setCrmContact(null);
        }
        if (item.getFromContactID() != null && opportunity.getCrmContact() == null) {
            EdsCrmContact contact = crmContactManager.get(item.getFromContactID());
            if (contact != null) {
                opportunity.setCrmContact(contact);
                if (contact.getEntityID() != null) {
                    opportunity.setEntityID(contact.getEntityID());
                }
            }
        }
        if (item.getStageId() != null) {
            EdsReference stage = referenceManager.get(item.getStageId());
            opportunity.setStage(stage);
            if (item.getProbability() == null && item.getObjectId() == null && stage != null) {
                Float probability = null;
                try {
                    probability = Float.valueOf(stage.getDescription());
                } catch (NumberFormatException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                item.setProbability(probability);
                opportunity.setProbability(probability);
            }
            //create the client and client contact from account and crm contact
            if ((EdsOpportunity.CLOSED_WON.equals(stage.getCode()) || "100".equals(stage.getDescription())) && opportunity.getCrmAccount() != null) {
                opportunity.getCrmAccount().addAccountType(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER));
                updateCrmAccountAndAddToSolr(opportunity.getCrmAccount(), false, userManager.getUser());
            }
        } else {
            opportunity.setStage(null);
        }
        boolean isNewOpportunity = item.getObjectId() == null;
        if (item.getCampaignId() != null) {
            opportunity.setCampaign(campaignManager.get(item.getCampaignId()));
        } else {
            opportunity.setCampaign(null);
            if (isNewOpportunity && opportunity.getCrmAccount() != null && opportunity.getCrmAccount().getCampaign() != null) {
                opportunity.setCampaign(opportunity.getCrmAccount().getCampaign());
            }
        }
        if (item.getProject() != null && item.getProject().getId() != null) {
            opportunity.setProject(projectManager.get(item.getProject().getId()));
        } else {
            opportunity.setProject(null);
        }
        opportunity.setExpectedRevenue(item.getExpectedRevenue());
        if (item.getExchangeRate() != null) {
            opportunity.setExchangeRate(item.getExchangeRate());
        }

        //set kanbanboard order if its null
        if (opportunity.getKanbanorder() == null) {
            Long minKanbanOrderInStatus = opportunityManager.getMinKanbanOrder(opportunity.getStage() != null ? opportunity.getStage().getObjectID() : null);
            if (minKanbanOrderInStatus == null) {
                minKanbanOrderInStatus = KANBAN_ORDER_GAP;
                opportunity.setKanbanorder(minKanbanOrderInStatus);
            } else {
                opportunity.setKanbanorder(minKanbanOrderInStatus - KANBAN_ORDER_GAP);
            }

        }

        boolean isNew = true;
        if (item.getObjectId() != null) {
            opportunityManager.update(opportunity);
            isNew = false;
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsOpportunity.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(opportunity.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Opportunity updated");
        } else {
            opportunityManager.create(opportunity);
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsOpportunity.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            if (opportunity.getObjectID() != null) {
                kpiLog.setEntityId(opportunity.getObjectID());
            }
            ServerUtils.kpiLog(log, kpiLog, "Opportunity created");
        }
        if (isOk(item.getApprovers())) {
            saveOpportunityApprovers(opportunity, item.getApprovers(), item.getStatusCode());
        }

        if (isOk(item.getApprovers())) {
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), opportunity, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_OPPORTUNITY);
        }


        for (HashMap.Entry<String, ArrayList<CustomTableRpc>> map : item.getCustomTableItems().entrySet()) {
            List<CustomTableRpc> values = map.getValue();
            if (opportunity != null && opportunity.getObjectID() != null) {
                for (CustomTableRpc customTableRpc : values) {
                    List<EdsOpportunityCustomItemTable> oldValuesOpportunity = opportunityItemTableManager.findByUuid(opportunity.getObjectID(), customTableRpc.getUuid());

                    if (oldValuesOpportunity != null && oldValuesOpportunity.size() > 0) {
                        for (EdsOpportunityCustomItemTable itemTable : oldValuesOpportunity) {
                            opportunityItemTableManager.delete(itemTable);
                        }
                    }
                }
            }

            for (CustomTableRpc rpc : values) {
                EdsOpportunityCustomItemTable customItemTable = new EdsOpportunityCustomItemTable();
                customItemTable.setUuid(map.getKey());
                customItemTable.setName(rpc.getItemName());
                customItemTable.setDescription(rpc.getDescription());
                customItemTable.setCustomFields(saveCustomTableFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()));
                customItemTable.setOpportunity(opportunity);
                if (saveCustomTableFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()) != null) {
                    opportunityItemTableManager.createOrUpdate(customItemTable);
                }

            }
        }
        EdsBusinessEvent s = baseEventPostProcessor.registerEvent(CrmOpportunityEventListenerImpl.TYPE, isNew ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, opportunity, userManager.getUser());
        saveCrmNotes(CrmConstants.CRM_OPPORTUNITY, opportunity.getObjectID(), item.getNotes());

        if (attachments != null && attachments.size() > 0) {
            FolderResource folderResource = documentsServiceLocal.getFolderResource(Constants.F_OPPORTUNITY, opportunity.getObjectID());
            for (MultipartFile multipartFile : attachments) {
                documentsServiceLocal.saveDocumentFile(multipartFile, folderResource.getObjectId(), folderResource.getFileType(), opportunity.getObjectID(), null);
            }
        }
        //save opportunity items
        saveOpportunityItems(opportunity, item.getItems());
        if (item.getAttachments() != null && item.getAttachments().length > 0) {
            saveOpportunityAttachments(item.getAttachments(), opportunity.getObjectID());
        }
        if (CT_PROJECT.equals(convertingTo)) {
            opportunity.setConvertedToProject(true);
            opportunityManager.update(opportunity);
        }

        if (item.isFromQuickAdd() && item.getContact() != null && item.getContactId() == null) {
            ContactListItem contactListItem = new ContactListItem();
            contactListItem.setFromOpportunityQuickAdd(true);

            OpportunityListItem opportunityItem = new OpportunityListItem();
            opportunityItem.setObjectId(opportunity.getObjectID());
            contactListItem.setOpportunity(opportunityItem);

            contactListItem.setContactType(ContactListItem.CRM_CONTACT);
            contactListItem.setFirstName(item.getContact());
            contactListItem.setPhones();
            contactListItem.addParam(Constants.CONTACT_PHONES, PhoneReference.WORK.getId(), item.getContactPrimaryPhone().trim());
            contactListItem.setPrimaryPhone(item.getContactPrimaryPhone().trim());

            contactListItem.getCrmAccount().setObjectId(null);
            if (item.getAccountId() != null) {
                EdsCrmAccount account = crmAccountManager.get(item.getAccountId());
                CrmAccountItem accountItem = new CrmAccountItem();
                accountItem.setObjectId(item.getContactId());
                accountItem.setName(account.getName());
                contactListItem.setCrmAccount(accountItem);
            } else {
                if (item.getAccount() != null && !"".equals(item.getAccount())) {
                    contactListItem.setCrmAccount(new CrmAccountItem());
                    contactListItem.getCrmAccount().setName(item.getAccount().trim());
                } else {
                    contactListItem.setCrmAccount(null);
                }
            }

            contactService.saveContact(contactListItem, null);
        }

        try {
            opportunitySolrComponent.index(opportunity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (CT_PROJECT.equals(convertingTo)) {
            return new SelectItem(opportunityConvertToProject(opportunity, true, null), opportunity.getNumber());
        }
        if (item.isRelationChanged()) {
            allInOneServiceLocal.saveRelations(RelationItem.TYPE_OPPORTUNITY, opportunity.getObjectID(), opportunity.getName(), item.getRelations());
        }
        String eventType = item.getObjectId() != null ? BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT : BaseEventsPostProcessorImpl.EVENT_TYPE_ADD;
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, eventType, opportunity, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_OPPORTUNITY);

        return new SelectItem(opportunity.getObjectID(), opportunity.getNumber());
    }

    private void saveOpportunityApprovers(EdsOpportunity edsApprovable, List<ApproverItemMini> approvers, String statusCode) {
        approvers.sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
        boolean isFirstApprover = true;
        for (ApproverItemMini approverItem : approvers) {
            EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
            if (approverItem.getObjectID() != null) {
                if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                    EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                    _edsApprover.setExactEmployee(user_);
                }
                approverManager.update(_edsApprover);
                if (edsApprovable.getCurrentApprover() != null && statusCode != null && isFirstApprover) {
                    edsApprovable.getCurrentApprover().setStatus(referenceManager.findReference(Constants.OPPORTUNITY_STATUS, statusCode));
                    edsApprovable.setEntityStatus(referenceManager.findReference(Constants.OPPORTUNITY_STATUS, Constants.OPPORTUNITY_SUBMITTED));
                    isFirstApprover = false;
                } else if (edsApprovable.getCurrentApprover() != null && statusCode != null) {
                    edsApprovable.getCurrentApprover().setStatus(referenceManager.findReference(Constants.OPPORTUNITY_STATUS, Constants.OPPORTUNITY_SUBMITTED));
                }
                if (statusCode != null && !Constants.OPPORTUNITY_APPROVED.equals(statusCode)) {
                    edsApprovable.setEntityStatus(referenceManager.findReference(Constants.OPPORTUNITY_STATUS, statusCode));
                }
                if (edsApprovable.isCurrentApproverRejected()) {
                    edsApprovable.setEntityStatus(edsApprovable.getCurrentApprover().getStatus());
                }
                continue;
            }
            EdsApprover edsApprover = _edsApprover.cloneShallow();
            edsApprover.setObjectID(null);
            edsApprover.setApproverHistory(new HashSet<>());
            edsApprover.setEntityID(edsApprovable.getObjectID());
            edsApprover.setIs_default(false);
            if (statusCode != null && isFirstApprover) {
                edsApprover.setStatus(referenceManager.findReference(Constants.OPPORTUNITY_STATUS, statusCode));
                if (Constants.OPPORTUNITY_DRAFT.equals(statusCode)) {
                    edsApprovable.setEntityStatus(referenceManager.findReference(Constants.OPPORTUNITY_STATUS, statusCode));
                } else {
                    edsApprovable.setEntityStatus(referenceManager.findReference(Constants.OPPORTUNITY_STATUS, Constants.OPPORTUNITY_SUBMITTED));
                }
                isFirstApprover = false;
            } else if (statusCode != null) {
                edsApprover.setStatus(referenceManager.findReference(Constants.OPPORTUNITY_STATUS, Constants.OPPORTUNITY_SUBMITTED));
            }
            if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                edsApprover.setExactEmployee(user_);
            }
            edsApprover.setApproverRoles(new HashSet<>());
            edsApprover.setApproverEmployees(new HashSet<>());
            edsApprover.setDynamicQueries(new HashSet<>());
            approverManager.createOrUpdate(edsApprover);

            for (EdsApproverRoles roleapp : _edsApprover.getApproverRoles()) {
                EdsApproverRoles roles = new EdsApproverRoles();
                roles.setApproverId(edsApprover.getObjectID());
                roles.setRoleId(roleapp.getRoleId());
                roles.setApproveForAll(roleapp.getApproveForAll());
                edsApprover.getApproverRoles().add(roles);
            }
            for (EdsApproverEmployees ucerapp : _edsApprover.getApproverEmployees()) {
                EdsApproverEmployees employees = new EdsApproverEmployees();
                employees.setApproveForAll(ucerapp.getApproveForAll());
                employees.setEmployeeId(ucerapp.getEmployeeId());
                employees.setApproverId(edsApprover.getObjectID());
                edsApprover.getApproverEmployees().add(employees);
            }
            if (edsApprovable.getCurrentApprover() == null) {
                edsApprovable.setCurrentApprover(edsApprover);
            }
            edsApprovable.getApprovers().add(edsApprover);
        }
    }

    public EdsOpportunityItemTableCF saveCustomTableFields(EdsOpportunityItemTableCF customfField, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (customfField == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && fieldItem.getFieldStringValue().length() > 0)
                            || fieldItem.getFieldDateNonConvertedValue() != null
                            || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || fieldItem.getProfielImageId() != null
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                customfField = new EdsOpportunityItemTableCF();
                opportunityItemTableCFManager.create(customfField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(customfField, customFieldItems);
            return customfField;
        }
        return null;
    }

    public ArrayList<HistoryListItem> saveCrmNotes(String entityType, Integer entityID, ArrayList<HistoryListItem> notes) {
        if (entityID != null && entityType != null && notes != null && notes.size() > 0) {
            for (HistoryListItem note : notes) {
                note.setObjectID(saveCrmNote(entityType, entityID, note));
            }
        }
        return notes;
    }

    public Integer saveCrmNote(String entityType, Integer entityID, HistoryListItem note) {
        if (note != null) {
            boolean existingNote = note.getObjectID() != null && note.getObjectID() > 0;
            if (entityType != null && entityID != null) {
                EdsUser user = userManager.getUser();
                EdsOpportunity opportunity = null;
                EdsNoteHistory edsNote = existingNote ? noteHistoryManager.get(note.getObjectID()) : new EdsNoteHistory();
                if (existingNote && edsNote != null) {
                    edsNote.setComment(note.getComment());
                    edsNote.setVisibility(note.isVisibility());
                } else {
                    edsNote = new EdsNoteHistory();
                    if (note.getEmployeeID() != null) {
                        EdsUser noteUser = userManager.get(note.getEmployeeID());
                        edsNote.setEmployee(noteUser != null ? noteUser : user);
                    } else {
                        edsNote.setEmployee(user);
                    }
                    edsNote.setComment(note.getComment());
                    edsNote.setEventDate(note.getEventDate() != null ? note.getEventDate() : new Date());
                    edsNote.setRelatedId(entityID);
                    edsNote.setRelatedTo(EdsNoteHistory.getRelatedToByEntityType(entityType));
                    edsNote.setVisibility(note.isVisibility());
                }
                if (CrmConstants.CRM_LEAD.equals(entityType)) {
                    crmContactManager.update(crmContactManager.get(entityID), true);
                } else if (CrmConstants.CRM_OPPORTUNITY.equals(entityType)) {
                    opportunity = opportunityManager.get(entityID);
                    opportunity.clear();
                    opportunity.setNote(note.getComment());
                    updateOpportunity(opportunity);
                }
                if (ServerUtils.hasNonSuperUserSession()) {
                    edsNote.setSuperUser(false);
                } else {
                    edsNote.setSuperUser(ServerUtils.isSuperUser());
                }
                noteHistoryManager.createOrUpdate(edsNote);
                note.setObjectID(edsNote.getObjectID());
                if (edsNote.getObjectID() != null && (CrmConstants.CRM_CONTACT.equals(entityType) || CrmConstants.CRM_LEAD.equals(entityType) || CrmConstants.CRM_OPPORTUNITY.equals(entityType))) {
                    edsNote.setEventDate(new Date());
                    if (!existingNote) {
                        baseEventPostProcessor.registerEvent(NoteEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsNote, user);
                    } else {
                        baseEventPostProcessor.registerEvent(NoteEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsNote, user);
                    }
                }
                switch (entityType){
                    case CrmConstants.CRM_OPPORTUNITY:
                        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, opportunity, userManager.getUser());
                        workflowEvent.setEntityType(RelationItem.TYPE_OPPORTUNITY);
                }
            }
        }
        return note != null ? note.getObjectID() : null;
    }

    private void saveOpportunityAttachments(FileItem[] attachments, Integer opportunityID) {
        attachmentUtilsManager.saveAttachments(F_OPPORTUNITY, opportunityID, opportunityID, attachments);
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OpportunityListItem editOpportunity(Integer objectId) {
        return editOpportunity(objectId, null, null, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OpportunityListItem editOpportunity(Integer objectId, String formType, Integer convertedFormId, Integer contactId) {
        OpportunityListItem item = new OpportunityListItem();
        item.setAssignees(getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE));
        item.setTaxCalculationType(allInOneServiceLocal.getTaxCalcTypeForInvoice());
        CurrencyItem baseCurrency = invoiceServiceLocal.getBaseCurrency();
        item.setStages(getOpportunityStages(false));
        item.setTypes(getAsSelectItem(referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_TYPE), ServerUtils.REFERENCE));
        item.setNumberData(generateOpportunityNumber());
        item.setRequireContractUpload(opportunityManager.getUser().getCompany().getCompanySettings().getOpportunityRequireContractUpload());
        item.setProductCategories((TreeSelectItem[]) accountingService.getCategoriesAsSelectItem());
        item.setProductBrands(accountingService.getBrandsAsSelectItem());

        ClientCurrency companyCurrency = getClientCurrency();

        if (companyCurrency != null) {
            item.setCurrencies(companyCurrency.getItems());
            if (companyCurrency.getUserCurrencyId() != null) {
                item.setCurrencyId(companyCurrency.getUserCurrencyId());
            }
        }

        item.setStageHistoryColConf(itemTableSettingService.getColumnConfigs(ItemTableEnum.OPPORTUNITY_STAGE_HISTORY));
        if (baseCurrency != null) {
            item.setBaseCurrencyID(baseCurrency.getId());
            item.setBaseCurrencyName(baseCurrency.getName());
        }
        EdsOpportunity opportunity;
        if (objectId != null) {
            opportunity = opportunityManager.get(objectId);
            if (opportunity != null) {
                EdsUser user = userManager.getUser();
                item = opportunity.getRPC(item);
                Integer rfqId = rfqManager.getByOpportunity(objectId);
                if (rfqId != null) {
                    item.setRfqId(rfqId);
                }

                if (opportunity.getStage() != null) {

                    boolean draggable = false;
                    if (opportunity.getStage().getAllowedRoles().isEmpty() || !opportunity.getStage().getAllowedRoles().isEmpty() && user.hasEitherRoles(opportunity.getStage().getAllowedRoles().toArray(new EdsRole[]{}))) {
                        item.setDraggable(true);
                        draggable = true;
                    }

                    if (!draggable && (opportunity.getStage().getViewOnlyRoles().isEmpty() || !opportunity.getStage().getViewOnlyRoles().isEmpty() && user.hasEitherRoles(opportunity.getStage().getViewOnlyRoles().toArray(new EdsRole[]{})))) {
                        item.setDraggable(false);
                    }

                    if (opportunity.getStage().getOppEditBtnRole() == null || (opportunity.getStage().getOppEditBtnRole() != null && opportunity.getStage().getOppEditBtnRole().isEmpty()) || user == null || (user != null && user.hasEitherRoles(opportunity.getStage().getOppEditBtnRole().toArray(new EdsRole[]{})))) {
                        item.setAllowEdit(true);
                    }
                }
                if (opportunity.getCrmContact() != null) {
                    item.setContactItem(opportunity.getCrmContact().getRPC(new ListingFilterParameter(false)));
                    item.setContact(opportunity.getCrmContact().getName());
                } else {
                    if (item.getAccountId() != null) {
                        EdsCrmAccount account = crmAccountManager.get(item.getAccountId());
                        ContactListItem contactListItem = new ContactListItem();
                        contactListItem.setContactType(TYPE_ACCOUNT);
                        if (account.getOwners() != null && !account.getOwners().isEmpty()) {
                            contactListItem.setOwnerId(account.getOwners() != null ? account.getOwners().get(0).getObjectID() : null);
                            contactListItem.setOwner(account.getOwners().get(0).getName());
                        }
                        contactListItem.setWorkEmail(account.getEmail());
                        contactListItem.setWorkPhone(account.getPhone());
                        contactListItem.setAccountIndustry(account.getIndustry() != null ? account.getIndustry().getLocalizedName() : "");
                        contactListItem.setFirstName(account.getName());
                        item.setContactItem(contactListItem);
                    }
                }
                if (opportunity.getLeadSource() != null) {
                    item.setLeadSource(opportunity.getLeadSource().getName());
                    item.setLeadSourceId(opportunity.getLeadSource().getObjectID());
                }
                item.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_OPPORTUNITY, objectId)));
                item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(opportunity.getCustomFields(),
                        commonService.getCompanyCustomFields(ViewName.Opportunity)));
                if (opportunity.getOpportunityItems().size() > 0) {
                    OpportunityItem[] items = new OpportunityItem[opportunity.getOpportunityItems().size()];
                    ArrayList<OpportunityItem> listItems = new ArrayList<>();
                    int index = 0;
                    for (EdsOpportunityItem it : opportunity.getOpportunityItems()) {
                        items[index] = new OpportunityItem();
                        items[index].setId(it.getObjectID());
                        if (it.getItem() != null) {
                            EdsItem edsItem = it.getItem();
                            items[index].setItemID(edsItem.getObjectID());
                            items[index].setItemName(edsItem.getName());
                            items[index].setItemNumber(edsItem.getProductNumber());
                            items[index].setQtyOnHand(edsItem.getQty());
                        } else {
                            items[index].setItemName(it.getItemName());
                            items[index].setItemNumber("");
                        }
                        items[index].setQty(it.getQty());
                        items[index].setDescription(it.getDescription());
                        items[index].setPrice(it.getPrice());
                        if (it.getVat() != null) {
                            items[index].setTaxItem(it.getVat().createTaxItem());
                            items[index].setTaxAmount(it.getItemCalculatedTaxAmount());
                        }

                        items[index].setNet(it.getNet());
                        items[index].setSubTotal(it.getSubTotal());
                        items[index].setDiscountPercent(it.getDiscount());
                        items[index].setDiscountAmount(it.getDiscountAmount());
                        if (it.getItemDiscount() != null) {
                            items[index].setDiscountItemName(it.getItemDiscount().getName());
                            items[index].setDiscountItemID(it.getItemDiscount().getObjectID());
                        }
                        if (it.getItem() != null) {
                            items[index].setDiscountItems(opportunity.getProductDiscounts(it.getItem().getDiscounts()));
                        }

                        if (it.getUnitMeasurement() != null) {
                            items[index].setUnitMeasurement(it.getUnitMeasurement().getAsSelectItem());
                        }

                        if (it.getCategory() != null) {
                            items[index].setProductCategory(new SelectItem(it.getCategory().getObjectID(), it.getCategory().getName()));
                        }

                        if (it.getBrand() != null) {
                            items[index].setProductBrand(new SelectItem(it.getBrand().getObjectID(), it.getBrand().getName()));
                        }

                        if (it.getProject() != null) {
                            items[index].setProject(new SelectItem(it.getProject().getObjectID(), it.getProject().getNumber() != null ? it.getProject().getNumber() + " -> " + it.getProject().getName() : it.getProject().getName()));
                        }
                        items[index].setSupplierID(it.getSupplierID());
                        items[index].setSupplierName(it.getSupplierName());
                        items[index].setDiscountItemFixedType(it.getDiscountItemFixedType());

                        ArrayList<CompanyCustomFieldItem> itemCustomFields = new ArrayList<>();

                        for (CompanyCustomFieldItem customFieldItem : commonService.getCompanyAllCustomFields(ViewName.OpportunitySubItem)) {
                            itemCustomFields.add(customFieldItem.cloneObject());
                        }

                        items[index].setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(it.getCustomFields(),
                                itemCustomFields));
                        listItems.add(items[index]);
                        index++;
                    }
                    item.setItems(listItems.toArray(new OpportunityItem[0]));
                }
                if (opportunity != null) {
                    Set<EdsOpportunityCustomItemTable> itemTables = opportunity.getItemTables();

                    HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

                    if (itemTables != null || itemTables.size() > 0) {

                        for (EdsOpportunityCustomItemTable itemTable : itemTables) {
                            CustomTableRpc rpc = itemTable.getRpc();

                            rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                                    commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.OpportunityItemTable, rpc.getUuid())));

                            map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                        }
                        item.setCustomTableItems(map);
                    }
                    HashMap<String, ArrayList<CustomTableRpc>> tableItems = item.getCustomTableItems();


                    for (List<CustomTableRpc> tableRpcs : tableItems.values()) {
                        tableRpcs.sort(Comparator.comparing(CustomTableRpc::getId));
                    }
                }

            }
        } else {
            EdsUser user = opportunityManager.getUser();
            if (user.isClientContact() && user.getClientContact().getCrmContact() != null) {
                EdsCrmContact contact = user.getClientContact().getCrmContact();
                EdsCrmAccount account = contact.getCrmAccount();
                item.setContactId(contact.getObjectID());
                item.setContact(contact.getName());
                item.getCrmAccountItem().setObjectId(account.getObjectID());
                item.getCrmAccountItem().setName(account.getName());
                item.getCrmAccountItem().setEmail(account.getEmail());
                item.getCrmAccountItem().setFax(account.getFax());
                item.getCrmAccountItem().setPhone(account.getPhone());
            }

            if (contactId != null) {
                EdsCrmContact crmContact = crmContactManager.get(contactId);
                if (crmContact != null) {
                    item.setContactItem(crmContact.getRPC(new ListingFilterParameter(false)));
                    item.setContact(crmContact.getName());
                    if (crmContact.getCrmAccount() != null) {
                        item.setAccountId(crmContact.getCrmAccount().getObjectID());
                        item.setAccount(crmContact.getCrmAccount().getName());
                    }
                }
            }

            if (convertedFormId != null && formType.contains("_FORM")) {
                item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null,
                        commonService.getCompanyCustomFields(ViewName.Opportunity)));

                item.setConvertedRelations(EdsRelation.asRPCs(relationManager.getAllRelations(formType, convertedFormId)));

                ColumnConfigs[] itemTableColumns = itemTableSettingService.getColumnConfigs(ItemTableEnum.OPPORTUNITY_SUB_ITEM, false);

                EdsFormProperty formProperty = formPropertyManager.getByFormID(LayoutRPC.OPPORTUNITY_FORM);

                Gson gson = new Gson();
                FormProperty[] fields = gson.fromJson(formProperty.getSettingsJSONData(), FormProperty[].class);

                EdsCustomFormItems edsItem = customFormItemManager.get(convertedFormId);
                FormItems formItems = edsItem.toRpc();
                Set<EdsCustomItemTable> itemTables = edsItem.getItemTables();

                HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

                if (itemTables != null || itemTables.size() > 0) {

                    for (EdsCustomItemTable itemTable : itemTables) {
                        CustomTableRpc rpc = itemTable.getRpc();

                        rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                                commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.CustomFormItemTable, rpc.getUuid())));

                        map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                    }
                    formItems.setTableItems(map);
                }
                Map<String, ArrayList<CustomTableRpc>> tableItems = formItems.getTableItems();


                for (List<CustomTableRpc> tableRpcs : tableItems.values()) {
                    tableRpcs.sort(Comparator.comparing(CustomTableRpc::getId));
                }

                ArrayList<OpportunityItem> listItems = new ArrayList<>();
                for (Map.Entry<String, ArrayList<CustomTableRpc>> mapTables : formItems.getTableItems().entrySet()) {
                    List<CustomTableRpc> values = mapTables.getValue();
                    for (CustomTableRpc rpc : values) {
                        OpportunityItem opportunityItem = new OpportunityItem();
                        ArrayList<CompanyCustomFieldItem> itemCustomFields = new ArrayList<>();
                        if (rpc != null && rpc.getItemCustomFields() != null) {
                            for (CompanyCustomFieldItem itemCF : rpc.getItemCustomFields()) {
                                if (itemCF != null) {
                                    convertItemTableFields(opportunityItem, itemCustomFields, itemCF);
                                }
                            }
                        }
                        opportunityItem.setItemCustomFields(itemCustomFields);
                        listItems.add(opportunityItem);
                    }
                }
                item.setItems(listItems.toArray(new OpportunityItem[0]));

                formItems.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsItem.getFormCustomFields(),
                        commonServiceLocal.getCompanyCategoryCustomFields(edsItem.getCustomForm() != null ? edsItem.getCustomForm().getObjectID() : null)));

                if (formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
                    for (int i = 0; i < formItems.getCustomFieldItems().size(); i++) {
                        if (UI_TYPE_AUTONUMBER.equals(formItems.getCustomFieldItems().get(i).getUiType()) && formItems.getCustomFieldItems().get(i).getFieldStringValue() != null) {
                            formItems.setAutoNumber(formItems.getCustomFieldItems().get(i).getFieldStringValue());
                            break;
                        }
                    }
                }
                item.setFromName(formItems.getAutoNumber() != null ? formItems.getAutoNumber() : formItems.getFormName() + ": " + formItems.getObjectID().toString());

                if (formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
                    for (CompanyCustomFieldItem companyCustomFieldItem : formItems.getCustomFieldItems()) {
                        convertFormCustomFields(item, fields, companyCustomFieldItem);
                    }
                }
            } else if (convertedFormId != null && RelationItem.TYPE_REQUEST_FOR_QUOTE.equals(formType)) {
                item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null,
                        commonService.getCompanyCustomFields(ViewName.Opportunity)));

                ColumnConfigs[] itemTableColumns = itemTableSettingService.getColumnConfigs(ItemTableEnum.OPPORTUNITY_SUB_ITEM, false);

                EdsFormProperty formProperty = formPropertyManager.getByFormID(LayoutRPC.OPPORTUNITY_FORM);

                Gson gson = new Gson();
                FormProperty[] fields = gson.fromJson(formProperty.getSettingsJSONData(), FormProperty[].class);


                RFQData rfqData = quoteService.getRFQData(convertedFormId, null);
                item.setFromName(rfqData.getNumberData() != null ? rfqData.getNumberData().getNumberString() : convertedFormId.toString());

                if (rfqData != null) {
                    item.setConvertedRelations(rfqData.getRelations());
                    if (rfqData.getCustomer() != null) {
                        item.setAccountId(rfqData.getCustomer().getId());
                        item.setAccount(rfqData.getCustomer().getName());
                    }
                    if (rfqData.getValidUntil() != null) {
                        item.setClosingDate(rfqData.getValidUntil().getNonConvertedDate());
                    }
                    if (rfqData.getNumberData() != null) {
                        item.setNumberData(rfqData.getNumberData());
                    }
                    if (rfqData.getCustomFieldList() != null && rfqData.getCustomFieldList().size() > 0) {
                        for (CompanyCustomFieldItem companyCustomFieldItem : rfqData.getCustomFieldList()) {
                            convertFormCustomFields(item, fields, companyCustomFieldItem);
                        }
                    }

                    if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
                        for (CompanyCustomFieldItem companyCustomFieldItem : item.getCustomFields()) {
                            if (companyCustomFieldItem != null) {
                                switch (companyCustomFieldItem.getAliasName()) {
                                    case "CUSTOMER" -> {
                                        if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.CUSTOMER.equals(companyCustomFieldItem.getLookUpTypeEnum()) && rfqData.getCustomer() != null) {
                                            companyCustomFieldItem.setSelectedId(rfqData.getCustomer().getId());
                                            companyCustomFieldItem.setFieldStringValue(rfqData.getCustomer().getName());
                                        }
                                    }
                                    case "DATE" -> {
                                        if (DATA_TYPE_DATE.equals(companyCustomFieldItem.getDataType()) && rfqData.getDate() != null) {
                                            companyCustomFieldItem.setFieldDateNonConvertedValue(rfqData.getDate());
                                        }
                                    }
                                    case "DUE_DATE" -> {
                                        if (DATA_TYPE_DATE.equals(companyCustomFieldItem.getDataType()) && rfqData.getValidUntil() != null) {
                                            companyCustomFieldItem.setFieldDateNonConvertedValue(rfqData.getValidUntil());
                                        }
                                    }
                                    case "NUMBER" -> {
                                        if (UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) && rfqData.getNumberData() != null) {
                                            companyCustomFieldItem.setFieldStringValue(rfqData.getNumberData().getNumberString());
                                        }
                                    }
                                    case "SQ_NUMBER" -> {
                                        if (UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) && rfqData.getSqNumber() != null) {
                                            companyCustomFieldItem.setFieldStringValue(rfqData.getSqNumber());
                                        }
                                    }
                                    case "PROJECT_MANAGER" -> {
                                        if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.PROJECT.equals(companyCustomFieldItem.getLookUpTypeEnum()) && rfqData.getProject() != null) {
                                            companyCustomFieldItem.setSelectedId(rfqData.getProject().getId());
                                            companyCustomFieldItem.setFieldStringValue(rfqData.getProject().getName());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ArrayList<OpportunityItem> listItems = new ArrayList<>();
                    if (rfqData.getItems() != null && rfqData.getItems().size() > 0) {
                        for (RFQItem rfqItem : rfqData.getItems()) {
                            OpportunityItem opportunityItem = new OpportunityItem();
                            ArrayList<CompanyCustomFieldItem> itemCustomFields = new ArrayList<>();
                            if (rfqItem != null) {
                                if (rfqItem.getDescription() != null) {
                                    opportunityItem.setDescription(rfqItem.getDescription());
                                }
                                if (rfqItem.getProduct() != null) {
                                    opportunityItem.setItemID(rfqItem.getProduct().getId());
                                    opportunityItem.setItemName(rfqItem.getProduct().getName());
                                }
                                if (rfqItem.getQty() != null) {
                                    opportunityItem.setQty(rfqItem.getQty());
                                }
                                if (rfqItem.getMeasurement() != null) {
                                    opportunityItem.setUnitMeasurement(rfqItem.getMeasurement());
                                }
                                if (rfqItem.getSupplier() != null) {
                                    opportunityItem.setSupplierID(rfqItem.getSupplier().getId());
                                    opportunityItem.setSupplierName(rfqItem.getSupplier().getName());
                                }
                                if (rfqItem.getItemCustomFields() != null) {
                                    for (CompanyCustomFieldItem rfqItemCf : rfqItem.getItemCustomFields()) {
                                        if (rfqItemCf != null) {
                                            convertItemTableFields(opportunityItem, itemCustomFields, rfqItemCf);
                                        }
                                    }
                                }
                            }
                            opportunityItem.setItemCustomFields(itemCustomFields);
                            listItems.add(opportunityItem);
                        }
                    }
                    item.setItems(listItems.toArray(new OpportunityItem[0]));
                }
            }
        }
        return item;
    }

    private void convertItemTableFields(OpportunityItem opportunityItem, ArrayList<CompanyCustomFieldItem> itemCustomFields, CompanyCustomFieldItem itemCF) {
        if ("DESCRIPTION".equals(itemCF.getAliasName()) && (UI_TYPE_TEXTBOX.equals(itemCF.getUiType()) || UI_TYPE_TEXTAREA.equals(itemCF.getUiType())) || (UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType()) && "PRODUCT".equals(itemCF.getAliasName()))) {
            if (UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType())) {
                opportunityItem.setDescription(itemCF.getItem() != null ? itemCF.getItem().getDescription() : "");
            } else {
                opportunityItem.setDescription(itemCF.getFieldStringValue() != null ? itemCF.getFieldStringValue() : "");
            }
        }
        if ("PRODUCT".equals(itemCF.getAliasName()) && (UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.PRODUCT.equals(itemCF.getLookUpTypeEnum()) || UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType()))) {
            if (UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType())) {
                opportunityItem.setItemID(itemCF.getItem() != null ? itemCF.getItem().getId() : null);
                opportunityItem.setItemName(itemCF.getItem() != null ? itemCF.getItem().getName() : "");
            } else {
                opportunityItem.setItemID(itemCF.getSelectedId() != null ? itemCF.getSelectedId() : null);
                opportunityItem.setItemName(itemCF.getFieldStringValue() != null ? itemCF.getFieldStringValue() : "");
            }
        }
        if ("QTY".equals(itemCF.getAliasName()) && DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
            opportunityItem.setQty(itemCF.getFieldStringValue() != null ? new BigDecimal(itemCF.getFieldStringValue()) : null);
        }
        if ("MEASUREMENT".equals(itemCF.getAliasName()) && UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.UNIT_MEASUREMENT.equals(itemCF.getLookUpTypeEnum())) {
            opportunityItem.setUnitMeasurement(new SelectItem(itemCF.getSelectedId() != null ? itemCF.getSelectedId() : null, itemCF.getFieldStringValue() != null ? itemCF.getFieldStringValue() : ""));
        }
        if ("UNITPRICE".equals(itemCF.getAliasName()) && DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
            opportunityItem.setPrice(itemCF.getFieldStringValue() != null ? new BigDecimal(itemCF.getFieldStringValue()) : null);
        }
        if ("DISCOUNT_AMT".equals(itemCF.getAliasName()) && DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
            opportunityItem.setDiscountAmount(itemCF.getFieldStringValue() != null ? new BigDecimal(itemCF.getFieldStringValue()) : null);
        }
        if ("CLIENT".equals(itemCF.getAliasName()) && UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.SUPPLIER.equals(itemCF.getLookUpTypeEnum())) {
            opportunityItem.setSupplierID(itemCF.getSelectedId() != null ? itemCF.getSelectedId() : null);
            opportunityItem.setSupplierName(itemCF.getFieldStringValue() != null ? itemCF.getFieldStringValue() : "");
        }


        for (CompanyCustomFieldItem customFieldItem : commonService.getCompanyCustomFields(ViewName.OpportunitySubItem)) {
            if (customFieldItem != null && itemCF.getUiType().equals(customFieldItem.getUiType()) && itemCF.getAliasName().equals(customFieldItem.getAliasName())) {
                if (UI_TYPE_LOOKUP.equals(customFieldItem.getUiType())) {
                    if (customFieldItem.getLookUpTypeEnum().equals(itemCF.getLookUpTypeEnum())) {
                        customFieldItem.setFieldStringValue(itemCF.getFieldStringValue());
                        customFieldItem.setSelectedId(itemCF.getSelectedId());
                        customFieldItem.setItem(itemCF.getItem());
                    }
                } else {
                    customFieldItem.setFieldStringValue(itemCF.getFieldStringValue());
                    customFieldItem.setSelectedId(itemCF.getSelectedId());
                    customFieldItem.setItem(itemCF.getItem());
                    customFieldItem.setFieldDateNonConvertedValue(itemCF.getFieldDateNonConvertedValue());
                }
                itemCustomFields.add(customFieldItem);
            }
        }
    }

    private void convertFormCustomFields(OpportunityListItem item, FormProperty[] fields, CompanyCustomFieldItem companyCustomFieldItem) {
        if (companyCustomFieldItem != null) {
            for (FormProperty formProperty1 : fields) {
                if (formProperty1 != null) {
                    if (companyCustomFieldItem.getAliasName().equals(formProperty1.getAliasName())) {
                        switch (formProperty1.getCode()) {
                            case "CRM_OPPORTUNITY_PROBABILITY" -> {
                                if (companyCustomFieldItem.getUiType().equals(formProperty1.getWidget())) {
                                    item.setProbability(companyCustomFieldItem.getFieldStringValue() == null ? 0 : Double.valueOf(companyCustomFieldItem.getFieldStringValue()).floatValue());
                                }
                            }
                            case "CRM_OPPORTUNITY_ASSIGNEE" -> {
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.EMPLOYEE.equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                    item.setAssignee(companyCustomFieldItem.getFieldStringValue());
                                    item.setAssigneeId(companyCustomFieldItem.getSelectedId());
                                }
                            }
                            case "CRM_OPPORTUNITY_BACKUP_ASSIGNEE" -> {
                                if (companyCustomFieldItem.getUiType().equals(formProperty1.getWidget())) {
                                    item.setBackupAssignee(companyCustomFieldItem.getFieldStringValue());
                                }
                            }
                            case "CRM_OPPORTUNITY_NUMBER" -> {
                                if (companyCustomFieldItem.getUiType().equals(formProperty1.getWidget()) && DATA_TYPE_NUMBER.equals(companyCustomFieldItem.getDataType()) && companyCustomFieldItem.getFieldStringValue() != null) {
                                    EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
                                    NumberData numberData = null;
                                    if (companyCustomFieldItem.getFieldStringValue() != null) {
                                        if (settings != null && settings.getOpportunityNumberingFormat() != null) {
                                            numberData = settings.parseNumberData(new BigDecimal(companyCustomFieldItem.getFieldStringValue()).intValue(), settings.getOpportunityNumberingFormat());
                                        } else {
                                            numberData = EdsNumberingSettings.getDefaultData(new BigDecimal(companyCustomFieldItem.getFieldStringValue()).intValue(), EdsNumberingSettings.DEF_OPPORTUNITY_PREFIX);
                                        }
                                    }
                                    item.setNumberData(numberData);
                                }
                            }
                            case "CRM_OPPORTUNITY_NAME" -> {
                                if (companyCustomFieldItem.getUiType().equals(formProperty1.getWidget())) {
                                    item.setOpportunityName(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                            }
                            case "CRM_OPPORTUNITY_ACCOUNT_NAME" -> {
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && (CustomFieldLookUpTypeEnum.CUSTOMER.equals(companyCustomFieldItem.getLookUpTypeEnum()) || CustomFieldLookUpTypeEnum.SUPPLIER.equals(companyCustomFieldItem.getLookUpTypeEnum()))) {
                                    item.setAccountId(companyCustomFieldItem.getSelectedId() != null ? companyCustomFieldItem.getSelectedId() : null);
                                    item.setAccount(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                            }
                            case "CRM_OPPORTUNITY_CONTACT_NAME" -> {
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.CONTACT.equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                    item.setContactId(companyCustomFieldItem.getSelectedId() != null ? companyCustomFieldItem.getSelectedId() : null);
                                    item.setContact(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                            }
                            case "CRM_OPPORTUNITY_NEXT_STEP" -> {
                                if (companyCustomFieldItem.getUiType().equals(formProperty1.getWidget())) {
                                    item.setNextStep(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                            }
                            case "CRM_OPPORTUNITY_AMOUNT" -> {
                                if (companyCustomFieldItem.getUiType().equals(formProperty1.getWidget()) && DATA_TYPE_NUMBER.equals(companyCustomFieldItem.getDataType()) && companyCustomFieldItem.getFieldStringValue() != null) {
                                    item.setAmount(Double.valueOf(companyCustomFieldItem.getFieldStringValue()));
                                }
                            }
                            case "CURRENCY" -> {
                                if (companyCustomFieldItem.getUiType().equals(UI_TYPE_CURRENCY) && companyCustomFieldItem.getFieldStringValue() != null) {
                                    item.setCurrencyId(companyCustomFieldItem.getSelectedId());
                                    item.setCurrency(companyCustomFieldItem.getFieldStringValue());
                                }
                            }
                            case "CRM_OPPORTUNITY_CLOSING_DATE" -> {
                                if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setClosingDate(companyCustomFieldItem.getFieldDateNonConvertedValue() != null ? companyCustomFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() : null);
                                }
                            }
                        }
                    }
                }
            }

            if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
                for (CompanyCustomFieldItem oppCustomFields : item.getCustomFields()) {
                    if (companyCustomFieldItem.getAliasName().equals(oppCustomFields.getAliasName()) && companyCustomFieldItem.getUiType().equals(oppCustomFields.getUiType()) && companyCustomFieldItem.getDataType().equals(oppCustomFields.getDataType())) {
                        if (UI_TYPE_LOOKUP.equals(oppCustomFields.getUiType())) {
                            if (oppCustomFields.getLookUpTypeEnum().equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                oppCustomFields.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                                oppCustomFields.setSelectedId(companyCustomFieldItem.getSelectedId());
                                oppCustomFields.setItem(companyCustomFieldItem.getItem());
                            }
                        } else {
                            oppCustomFields.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                            oppCustomFields.setSelectedId(companyCustomFieldItem.getSelectedId());
                            oppCustomFields.setItem(companyCustomFieldItem.getItem());
                            oppCustomFields.setFieldDateNonConvertedValue(companyCustomFieldItem.getFieldDateNonConvertedValue());
                        }
                    }
                }
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NumberData generateOpportunityNumber() {
        NumberData numberData;

        DecimalFormat decimalFormat = new DecimalFormat("000");
        boolean isSodiqCompany = "22240".equals(ServerSecurityContext.getInstance().getCompanyId());

        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = opportunityManager.getLastIntNumber();

        if (isSodiqCompany) { //super MUSIR code
            decimalFormat = new DecimalFormat("0000");
            Integer lastPMIntNumber = projectManager.getProjectLastIntNumber();

            if (lastPMIntNumber != null && intNumber != null && lastPMIntNumber > intNumber) {
                intNumber = lastPMIntNumber;
            }
        }

        if (settings != null && settings.getOpportunityNumberingFormat() != null) {
            numberData = isSodiqCompany ? settings.parseNumberData(intNumber, settings.getOpportunityNumberingFormat(), decimalFormat) : settings.parseNumberData(intNumber, settings.getOpportunityNumberingFormat());
        } else {
            numberData = isSodiqCompany ? EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_OPPORTUNITY_PREFIX, decimalFormat) : EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_OPPORTUNITY_PREFIX);
        }

        return numberData;
    }

    @Transactional
    public ArrayList<Integer> deleteOpportunity(ArrayList<Integer> objectIDs) {

        EdsUser edsUser = userManager.getUser();
        objectIDs = new ArrayList<>(objectIDs);
        List<Integer> result = opportunityManager.deleteOpportinities(objectIDs, edsUser);
        if (result != null && result.size() > 0) {
            try {    //for many shu erga keldi
                solrManager.removeOpportunitiesByIds(result.toArray(new Integer[]{}));
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
            }
            if (result.size() == 1) {
                KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                kpiLog.setEntityName(EdsOpportunity.class.getSimpleName());
                kpiLog.setActionType(KpiLog.ActionType.DELETE);
                kpiLog.setEntityId(objectIDs.get(0));
                ServerUtils.kpiLog(log, kpiLog, "Opportunity deleted");
                EdsOpportunity opportunity = opportunityManager.get(objectIDs.get(0));
                if (opportunity != null) {
                    baseEventPostProcessor.registerEvent(CrmOpportunityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, opportunity, edsUser);

                    EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, opportunity, userManager.getUser());
                    workflowEvent.setEntityType(RelationItem.TYPE_OPPORTUNITY);
                }
            } else {
                for (int i = 0; i < result.size(); i++) {
                    KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                    kpiLog.setEntityName(EdsOpportunity.class.getSimpleName());
                    kpiLog.setActionType(KpiLog.ActionType.DELETE);
                    kpiLog.setEntityId(objectIDs.get(i));
                    ServerUtils.kpiLog(log, kpiLog, "Opportunity deleted");
                    EdsOpportunity opportunity = opportunityManager.get(objectIDs.get(i));
                    if (opportunity != null) {
                        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, opportunity, userManager.getUser());
                        workflowEvent.setEntityType(RelationItem.TYPE_OPPORTUNITY);
                    }
                }
            }

            objectIDs.removeAll(result);
        }
        return objectIDs;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getOpportunityStages(boolean sortByName) {
        List<EdsReference> items = referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_STAGE);
        SelectItem[] stages = new SelectItem[items.size()];

        int i = 0;
        for (EdsReference item : items) {
            ReferenceItem referenceItem = item.getRPC();
            referenceItem.setName(item.isSystemReference() && !item.isChanged() ? referenceWfmMessageSource.localize(item.getCode()) : item.getName());
            referenceItem.setParam(item.getCode());
            referenceItem.setSelected(referenceItem.isRequiredComment());
            referenceItem.setDraggable(item.getAllowedRoles().isEmpty() || !item.getAllowedRoles().isEmpty() && userManager.getUser().hasEitherRoles(item.getAllowedRoles().toArray(new EdsRole[]{})));


            if (item.getOppEditBtnRole() == null || (item.getOppEditBtnRole() != null && item.getOppEditBtnRole().isEmpty()) || userManager.getUser() == null || (userManager.getUser() != null && userManager.getUser().hasEitherRoles(item.getOppEditBtnRole().toArray(new EdsRole[]{})))) {
                referenceItem.setAllowEdit(true);
            }

            stages[i++] = referenceItem;
        }
        return sortByName ? ServerUtils.sortSelectItem(stages) : stages;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CrmAccountList getCrmAccounts(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        if (panelTools == null) {
            ArrayList<String> columnCodeName = CrmAccountItem.defaultColumnNames;
            panelTools = new ListPanelToolRpc();
            panelTools.setColumnCodeName(columnCodeName);
            fp.setColumnsOfListing(columnCodeName);
        }
        if (panelTools.isCustomFieldsShown()) {
            fp.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(commonService.getCompanyCustomFieldsForListView(ViewName.CrmAccount));
        }

        //We must replace filter option values since values has additional "_" (see: getCrmAccountFacetResultFromSolr())
        if (fp.getFacetFilter() != null && fp.getFacetFilter().getFacetContentMap().containsKey(FacetContentType.CrmAccountFacetFilter.getContentCode()[8])) {

            String dotKey = FacetContentType.CrmAccountFacetFilter.getContentCode()[8];


            if (fp.getFacetFilter().getFacetContentMap().get(dotKey) != null) {

                Map<Integer, String> additionalInformation = new HashMap<>();
                additionalInformation.put("_false".hashCode(), "false");//false
                additionalInformation.put("false".hashCode(), "false");//false
                additionalInformation.put("_Qualified".hashCode(), "Qualified");//Qualified
                additionalInformation.put("Qualified".hashCode(), "Qualified");//Qualified
                additionalInformation.put("_true".hashCode(), "true");//true
                additionalInformation.put("true".hashCode(), "true");//true
                additionalInformation.put("_Unqualified".hashCode(), "Unqualified");//Unqualified
                additionalInformation.put("Unqualified".hashCode(), "Unqualified");//Unqualified

                //Facet Items
                if (fp.getFacetFilter().getFacetContentMap().get(dotKey).getFacetItems() != null) {
                    for (int i = 0; i < fp.getFacetFilter().getFacetContentMap().get(dotKey).getFacetItems().length; i++) {
                        String name = additionalInformation.get(fp.getFacetFilter().getFacetContentMap().get(dotKey).getFacetItems()[i].getId());
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(name)) {
                            try {
                                fp.getFacetFilter().getFacetContentMap().get(dotKey).getFacetItems()[i].setName(name);
                            } catch (NumberFormatException e) {
//                                e.printStackTrace();
                            }
                        }
                    }
                }
                //Saved Items
                HashMap<Integer, Integer> replacedSavedItems = new HashMap<>();

                for (Map.Entry<Integer, Integer> savedItem : fp.getFacetFilter().getFacetContentMap().get(dotKey).getSavedItems().entrySet()) {
                    String replaceId = additionalInformation.get(savedItem.getKey().toString());
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(replaceId)) {
                        try {
                            replacedSavedItems.put(savedItem.getKey(), Integer.valueOf(replaceId));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    } else {
                        replacedSavedItems.put(savedItem.getKey(), savedItem.getValue());
                    }
                }
                fp.getFacetFilter().getFacetContentMap().get(dotKey).setSavedItems(replacedSavedItems);
            }
        }

        CrmAccountList list = getCrmAccountList(fp, fp.asConfig(), false);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmAccount.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get CRM accounts list");
        return list;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<CampaignItem> getCampaigns(ListingFilterParameter fp) {
        List<EdsCampaign> campaignList = campaignManager.getCampaignList(fp);
        int totalCount = campaignManager.getCampaignListCount(fp);
        ArrayList<CampaignItem> results = new ArrayList<>();
        for (EdsCampaign campaign : campaignList) {
            results.add(getCampaign(campaign, false));
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCampaign.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get campaign list");
        return new ListResult<>(results, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CampaignItem editCampaign(Integer objectId) {
        CampaignItem item = new CampaignItem();
        SelectItem[] assignees = getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE);
        EdsUser user = userManager.getUser();
        item.setUser(new SelectItem(user.getObjectID(), user.getName()));
        item.setStatuss(getAsSelectItem(referenceManager.listReferences(EdsCampaign._CAMPAIGN_STATUS), ServerUtils.REFERENCE));
        item.setTypes(getAsSelectItem(referenceManager.listReferences(EdsCampaign._CAMPAIGN_TYPE), ServerUtils.REFERENCE));
        item.setAssignees(assignees);
        if (assignees != null && assignees.length == 1) {
            item.setAssigneeId(assignees[0].getId());
        }
        if (objectId != null) {
            EdsCampaign edsCampaign = campaignManager.get(objectId);
            if (edsCampaign != null) {
                item = edsCampaign.getRPC(item, true);
                item.setStatus(referenceWfmMessageSource.localize(item.getStatusCode(), item.getStatus()));
            }
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsCampaign.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.VIEW);
            kpiLog.setEntityId(objectId);
            ServerUtils.kpiLog(log, kpiLog, "View campaign");
        }
        return item;
    }

    @Transactional
    public Integer saveCampaign(CampaignItem item) {
        EdsCampaign campaign = new EdsCampaign();
        EdsUser user = userManager.getUser();
        if (item.getObjectId() != null) {
            campaign = campaignManager.get(item.getObjectId());
        } else {
            campaign.setCreator(user);
            campaign.setCreatedDate(new Date());
        }
        if (item.getAssigneeId() != null) {
            campaign.setAssignee(employeeManager.get(item.getAssigneeId()));
        }
        boolean isNameChanged = campaign != null && item.getName() != null && campaign.getName() != null && !campaign.getName().equals(item.getName());
        campaign.setName(item.getName());
        if (item.getTypeId() != null) {
            campaign.setType(referenceManager.get(item.getTypeId()));
        } else {
            campaign.setType(null);
        }
        if (item.getStatusId() != null) {
            campaign.setStatus(referenceManager.get(item.getStatusId()));
        } else {
            campaign.setStatus(null);
        }

        campaign.setStartDate(item.getStartDate());
        campaign.setEndDate(item.getEndDate());
        campaign.setExpectedRevenue(item.getExpectedRevenue());
        campaign.setBudgetCost(item.getBudgetCost());
        campaign.setActualCost(item.getActualCost());
        campaign.setExpectedResponse(item.getExpectedResponse());
        campaign.setNumberSent(item.getNumberSent());
        boolean isNew = campaignManager.createOrUpdate(campaign);
        if (user != null) {
            baseEventPostProcessor.registerEvent(CrmCampaignEventListenerImpl.TYPE, (isNew ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT), campaign, user);
        }
        saveCrmNotes(CrmConstants.CAMPAIGN, campaign.getObjectID(), item.getNotes());
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCampaign.class.getSimpleName());
        if (campaign.getObjectID() != null) {
            kpiLog.setEntityId(campaign.getObjectID());
        }
        if (isNameChanged && user != null) {
            baseEventPostProcessor.registerEvent(CrmCampaignEventListenerImpl.TYPE, CrmCampaignEventListenerImpl.EVENT_CAMPAIGN_NAME_CHANGED, campaign, user);
        }
        if (isNew) {
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(log, kpiLog, "Add new campaign");
        } else {
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            ServerUtils.kpiLog(log, kpiLog, "Update campaign");
        }
        return campaign.getObjectID();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CampaignItem getCampign(Integer objectId) {
        EdsCampaign campaign = campaignManager.get(objectId);
        return getCampaign(campaign, true);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String generateAccountNumber(String accountType) {
        return crmAccountManager.generateAccountNumber(accountType);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<CrmAccountItem> getAccountsForMerge(Integer[] accountIDs) {
        ArrayList<CrmAccountItem> accounts = new ArrayList<>();
        accounts.add(editAccount(null, null));
        if (accountIDs != null) {
            for (Integer accountID : accountIDs) {
                if (accountID != null) {
                    EdsCrmAccount edsAccount = crmAccountManager.get(accountID);
                    if (edsAccount != null && !edsAccount.isDeleted()) {
                        CrmAccountItem item = new CrmAccountItem();
                        ArrayList<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(edsAccount.getCustomFields(),
                                commonService.getCompanyCustomFields(ViewName.CrmAccount));
                        if (customFieldItems != null && customFieldItems.size() > 0) {
                            item.setCustomFields(customFieldItems);
                        }
                        accounts.add(edsAccount.getRPC(item, false));
                    }
                }
            }
        }
        return accounts;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CrmAccountItem editAccount(Integer objectId, String type) {
        CrmAccountItem item = new CrmAccountItem();
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        EdsCrmAccount account = null;
        if (objectId != null) {
            account = crmAccountManager.get(objectId);
        }
        SelectItem[] ownersList = getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE);
        if (account == null || account.getOwners().isEmpty()) {
            item.setOwnerItems(ownersList);
        } else {
            for (SelectItem owner : ownersList) {
                owner.setSelected(account.getOwnersMap().containsKey(owner.getId()));
            }
            item.setOwnerItems(ownersList);
        }
        item.setAccountTypes(getAsSelectItem(referenceManager.listReferences("_CRM_ACCOUNT_TYPE"), ServerUtils.REFERENCE));
        item.setIndustries(getAsSelectItem(referenceManager.listReferences("_COMPANY_WORKAREA"), ServerUtils.REFERENCE));
        item.setClientTypes(getAsSelectItem(referenceManager.listReferences("CLIENT_TYPES"), ServerUtils.REFERENCE));
        item.setVatCategories(getAsSelectItem(referenceManager.listReferences("_VAT_CATEGORY"), ServerUtils.REFERENCE));
        item.setTaxTreatments(getAsSelectItem(getTaxTreatments(), ServerUtils.REFERENCE));
        item.setPaymentMethods(allInOneServiceLocal.getPaymentMethodList());
        /**
         * this one is for GCC countries
         * but for now, we've temporary blocked this logic
         */
        if (GCC_COUNTRIES.contains(userManager.getUser().getCompany().getCountry().getCode())) {
            item.setGccCountries(getAsSelectItem(countryManager.getCountryByCodeIn(Constants.GCC_COUNTRIES), ServerUtils.EDS_COUNTRY));
            item.setGccStates(getAsSelectItem(regionManager.listByCountry(userManager.getUser().getCompany().getCountry().getObjectID()), ServerUtils.EDS_REGION));
        }

        item.setCountrys(getCountryList());
        item.setStates(getAsSelectItem(regionManager.list(), 12));
        if (EdsCrmAccount.SUPPLIER.equals(type)) {
            item.setTypeChecked("SUPPLIER", null);
            item.setItemCustomFields(commonService.getCompanyCustomFields(ViewName.SupplierItem));
            item.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.SUPPLIER_ITEM));
        } else if (EdsCrmAccount.CUSTOMER.equals(type)) {
            item.setTypeChecked("CUSTOMER", null);
            item.setItemCustomFields(commonService.getCompanyCustomFields(ViewName.ClientItem));
            item.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.CLIENT_ITEM));
            SelectItem[] bankAccounts = accountingService.getBankAccountItems();
            if (bankAccounts != null && bankAccounts.length > 0) {
                item.setBankAccounts(bankAccounts);
            }
            if (account == null) {
                EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(crmAccountManager.getUser().getCompany());
                if (invoicingSettings != null) {
                    item.setBankAccountId(invoicingSettings.getBankAccountId());
                }
            }
        }
        HashMap<String, Boolean> disabledAccountType = new HashMap<>();
        if (account != null && account.getAccountTypes() != null && account.getAccountTypes().size() > 0) {
            boolean hasAnyRelation;
            for (EdsReference r : account.getAccountTypes()) {
                hasAnyRelation = false;
                if (EdsCrmAccount.CUSTOMER.equals(r.getCode())) {
                    hasAnyRelation = allInOneServiceLocal.checkCrmAccountRelations(objectId, EdsCrmAccount.CUSTOMER);
                } else if (EdsCrmAccount.SUPPLIER.equals(r.getCode())) {
                    hasAnyRelation = allInOneServiceLocal.checkCrmAccountRelations(objectId, EdsCrmAccount.SUPPLIER);
                }
                if (hasAnyRelation) {
                    disabledAccountType.put(r.getCode(), true);
                }
            }
        }
        item.setAccountTypesDisabled(disabledAccountType);

        ClientCurrency companyCurrency = getClientCurrency();
        if (companyCurrency != null) {
            item.setCurrencies(companyCurrency.getItems());
            if (companyCurrency.getUserCurrencyId() != null) {
                item.setCurrencyId(companyCurrency.getUserCurrencyId());
            }
        }
        CurrencyItem baseCurrency = invoiceServiceLocal.getBaseCurrency();
        if (baseCurrency != null) {
            item.setBaseCurrencyID(baseCurrency.getId());
            item.setBaseCurrencyName(baseCurrency.getName());
        }

        if (account != null) {
            item = account.getRPC(item, false);
            if (EdsCrmAccount.CUSTOMER.equals(type) || EdsCrmAccount.SUPPLIER.equals(type)) {
                item.setItems(getCrmSubItems(type, account.getObjectID()));
                item.setOpeningBalanceEditable(!invoiceManager.isClientSupplierInvoiceExists(account.getObjectID(), EdsCrmAccount.CUSTOMER.equals(type)) && !customerSupplierPaymentManager.isPaymentsExists(account.getObjectID(), EdsCrmAccount.CUSTOMER.equals(type)));
            } else if (CRM_ACCOUNT_TYPE.equals(type) && account.isClient()) {
                item.setItems(getCrmSubItems(EdsCrmAccount.CUSTOMER, account.getObjectID()));
                item.setItemCustomFields(commonService.getCompanyCustomFields(ViewName.ClientItem));
                item.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.CLIENT_ITEM));
            }
            if (EdsCrmAccount.SUPPLIER.equals(type)) {
                if (account.getPayable() != null) {
                    item.setAccountsReceivablePayable(account.getPayable().createAccountItem());
                }
            } else if (EdsCrmAccount.CUSTOMER.equals(type)) {
                if (account.getReceivable() != null) {
                    item.setAccountsReceivablePayable(account.getReceivable().createAccountItem());
                }
            }
            item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(account.getCustomFields(),
                    commonService.getCompanyCustomFields(ViewName.CrmAccount)));
            List<EdsAddress> billAddrList = addressManager.getAddressesByEntityIdAndType(objectId, EdsAddress.BILLING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
            List<EdsAddress> mailAddrList = addressManager.getAddressesByEntityIdAndType(objectId, EdsAddress.MAILING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
            item.setBillAddresses(account.getAddressData(billAddrList, account.getBillingAddress()));
            item.setMailAddresses(account.getAddressData(mailAddrList, account.getMailingAddress()));
            if (account.getLogo() != null && account.getLogo().getObjectID() != null) {
                item.setLogoUrl(getImageUrl(account.getLogo().getObjectID()));
            }
            EdsCrmContact primaryContact = account.getPrimaryContact();
            if (primaryContact != null) {
                item.setPrimaryContact(primaryContact.getRPC(null));
            }
            //Set GCC Countries VAT fields
            if (account.getTaxTreatment() != null) {
                item.setTaxTreatment(account.getTaxTreatment().getAsSelectItem());
                item.getTaxTreatment().setCode(account.getTaxTreatment().getCode());
            }
            item.setTaxTreatmentId(account.getTaxtreatmentId());
            item.setPlaceOfSupplyCountryId(account.getPlaceofsupplyCountryId());
            item.setPlaceOfSupplyStateId(account.getPlaceofsupplyStateId());
            item.setTrn(account.getTrn());
            if (account.getSalesType() != null) {
                item.setSalesTypeId(account.getSalesType().getId());
                item.setSalesType(account.getSalesType().name());
            }
            item.setCrNumber(account.getCrNumber());
            if (account.getPasportNumber() != null) {
                item.setPassportNumber(account.getPasportNumber());
            }
            Map<Integer, ArrayList<String>> telegramChats = account.getParams(EdsCrmContactItemParams.TELEGRAM_CHATS);
            if (telegramChats.size() > 0) {
                ArrayList<SelectItem> chats = new ArrayList<>();
                for (Integer botId : telegramChats.keySet()) {
                    TelegramSettingsItem bot = telegramChatService.getTelegramSettingsItem(botId);
                    TelegramChatListItem chat = telegramChatService.getChat(Integer.valueOf(telegramChats.get(botId).get(0)));
                    chats.add(new SelectItem(botId, chat.getObjectId(), chat.getChatName(), bot.getToken()));
                }
                item.setTelegramChats(chats);
            }
        } else {
            Integer countryID = clientManager.getUser().getCompany().getCountryZone().getCountry().getObjectID();
            Address billingAddress = new Address();
            Address mailingAddress = new Address();
            billingAddress.setCountryId(countryID);
            mailingAddress.setCountryId(countryID);
            billingAddress.setPrimary(true);
            mailingAddress.setPrimary(true);
            billingAddress.setName(commonLocalizer.localize(PdfLocalizationName.billingAddress, "Billing Address"));
            mailingAddress.setName(commonLocalizer.localize(PdfLocalizationName.mailingAddress, "Mailing Address"));
            item.setBillAddresses(new Address[]{billingAddress});
            item.setMailAddresses(new Address[]{mailingAddress});
            if (companySettings != null && companySettings.getGenerateCrmAccountNumbering()) {
                NumberData numberData = generateAccountNumberData(type);
                item.setNumber(numberData.getNumberString());
                item.setPrefix(numberData.getFirstNumberString());
                item.setIntNumber(numberData.getIntNumber());
            }
            EdsFinancialSettings edsFinancialSettings = financialSettingsManager.getFinancialSettings();
            item.setBalanceDate(new DateNonConvertable(edsFinancialSettings.getConversionDate()));
            item.setSupplierBalanceDate(new DateNonConvertable(edsFinancialSettings.getConversionDate()));
        }
        if (EdsCrmAccount.CUSTOMER.equals(type) || EdsCrmAccount.SUPPLIER.equals(type)) {
            if (objectId != null) {
                List<EdsPriceLevel> list = priceLevelManager.getPriceLevels(item.getCurrencyId(), objectId, true);
                SelectItem[] listItem = new SelectItem[list.size()];
                int index = 0;
                for (EdsPriceLevel level : list) {
                    listItem[index] = new ListItem();
                    listItem[index].setId(level.getObjectID());
                    listItem[index].setName(level.getName());
                    index++;
                }
                item.setAppliedPriceLavel(listItem);

                List<SelectItem> discountItems = new ArrayList<>();
                for (EdsDiscount discount : account.getDiscounts()) {
                    discountItems.add(discount.getAsSelectItem());
                }
                item.setAppliedDiscounts(discountItems.toArray(new SelectItem[]{}));
            }
        }
        String formType = null;
        if (CUSTOMER.equals(type)) {
            formType = LayoutRPC.CLIENT_FORM;
        } else if (SUPPLIER.equals(type)) {
            formType = LayoutRPC.SUPPLIER_FORM;
        } else if (CustomFormConstants.CRM_ACCOUNT.equals(type)) {
            formType = LayoutRPC.ACCOUNT_FORM;
        }
        item.setSalesTypes(SalesType.toSelectItems());

        LinkedHashMap<String, FormProperty> fields = new LinkedHashMap<>();
        if (formType != null) {
            EdsFormProperty edsFormProperty = formPropertyManager.getByFormID(formType);
            if (edsFormProperty != null) {
                Gson gson = new Gson();
                FormProperty[] formFields = gson.fromJson(edsFormProperty.getSettingsJSONData(), FormProperty[].class);
                for (FormProperty formProperty : formFields) {
                    if (formProperty != null) {
                        if (formProperty.getDefaultValue() != null && formProperty.getDefaultValue().length() == 0) {
                            formProperty.setDefaultValue(null);
                        }
                        if (formProperty.getRoleEdit() != null && formProperty.getRoleEdit().size() > 0) {
                            if (userManager.getUser().hasEitherRoles(formProperty.getRoleEdit().toArray(new Integer[]{}))) {
                                formProperty.setDisabled(false);
                            }
                        }

                        fields.put(formProperty.getCode(), formProperty);
                    }
                }
            }
        }
        item.setFormProperty(fields);
        return item;
    }

    private ArrayList<CrmSubItem> getCrmSubItems(String entityType, Integer entityId) {
        List<EdsCrmSubItem> edsCrmSubItems = crmSubItemManager.getItemsByTypeAndId(entityType, entityId);
        ArrayList<CrmSubItem> items = new ArrayList<>();
        if (edsCrmSubItems != null && !edsCrmSubItems.isEmpty()) {
            for (EdsCrmSubItem edsCrmSubItem : edsCrmSubItems) {
                CrmSubItem crmSubItem = new CrmSubItem();
                if (edsCrmSubItem.getItem() != null) {
                    crmSubItem.setItemName(edsCrmSubItem.getItem().getName());
                    crmSubItem.setItemID(edsCrmSubItem.getItem().getObjectID());
                    crmSubItem.setItemNumber(edsCrmSubItem.getItem().getProductNumber());
                }
                crmSubItem.setDescription(edsCrmSubItem.getDescription());
                crmSubItem.setQty(edsCrmSubItem.getQty());
                if (edsCrmSubItem.getUnitMeasurement() != null) {
                    crmSubItem.setUnitMeasurement(edsCrmSubItem.getUnitMeasurement().getAsSelectItem());
                }
                crmSubItem.setQty(edsCrmSubItem.getQty());
                crmSubItem.setPrice(edsCrmSubItem.getPrice());
                ViewName name = null;
                if (SUPPLIER.equals(entityType)) {
                    name = ViewName.SupplierItem;
                } else if (CUSTOMER.equals(entityType)) {
                    name = ViewName.ClientItem;
                }
                crmSubItem.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(edsCrmSubItem.getCustomFields(), commonService.getCompanyCustomFields(name)));
                items.add(crmSubItem);
            }
        }

        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ClientCurrency getClientCurrency() {
        ClientCurrency userItem = new ClientCurrency();
        userItem.setItems(getCurrencies());
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        if (fs != null && fs.getCurrency().getObjectID() != null) {
            userItem.setUserCurrencyId(fs.getCurrency().getObjectID());
        }
        return userItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCurrencies() {
        return getCurrencies(true);
    }

    private SelectItem[] getCurrencies(boolean showUsed) {
        List<EdsCurrency> currencies;
        List<SelectItem> items = new ArrayList<>();

        EdsCurrency baseCurrency = financialSettingsManager.getFinancialSettings().getCurrency();
        if (showUsed) {
            currencies = exchangeCurrencyManager.getCurrencyList();
            items.add(new SelectItem(baseCurrency.getObjectID(), baseCurrency.getName()));
            for (EdsCurrency currency : currencies) {
                if (!baseCurrency.getObjectID().equals(currency.getObjectID())) {
                    items.add(new SelectItem(currency.getObjectID(), currency.getName()));
                }
            }
        } else {
            currencies = exchangeCurrencyManager.getAvailableCurrencies();
            for (EdsCurrency currency : currencies) {
                if (!currency.getObjectID().equals(baseCurrency.getObjectID())) {
                    items.add(new SelectItem(currency.getObjectID(), currency.getName()));
                }
            }
        }
        return items.toArray(new SelectItem[0]);
    }

    @Override
    @Transactional
    public Boolean convertAccounts(ArrayList<Integer> ids, Integer typeID) {
        if (typeID != null && ids != null && !ids.isEmpty()) {
            EdsReference customerType = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER);
            EdsReference supplierType = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER);
            boolean isCustomerOrSupplierType = (customerType != null && typeID.equals(customerType.getObjectID())) || (supplierType != null && typeID.equals(supplierType.getObjectID()));
            List<EdsCrmAccount> solrIDs = new ArrayList<>();
            List<EdsReference> edsAccountTypes = referenceManager.listReferences(EdsCrmAccount._CRM_ACCOUNT_TYPE);
            for (Integer id : ids) {
                EdsCrmAccount account = crmAccountManager.get(id);
                if (account != null) {
                    if (isCustomerOrSupplierType) {
                        CrmAccountItem item = new CrmAccountItem();
                        item.setAccountTypes(getAsSelectItem(edsAccountTypes, 10));
                        item = account.getRPC(item, false);
                        item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(account.getCustomFields(),
                                commonService.getCompanyCustomFields(ViewName.CrmAccount)));
                        item.setTypeChecked(null, typeID);
                        solrIDs.add(crmAccountManager.get(saveAccount(item, null, null, false, true, false, false)));
                    } else {
                        EdsReference edsType = referenceManager.get(typeID);
                        if (edsType != null && !account.getAccountTypes().contains(edsType)) {
                            solrIDs.add(account);
                            account.getAccountTypes().add(edsType);
                            crmAccountManager.update(account);
                        }
                    }
                }
            }
            if (!solrIDs.isEmpty()) {
                try {
                    crmAccountSolrComponent.indexes(solrIDs);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Boolean.FALSE;
                }
            }
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Transactional
    public void updateCreditLimit(Integer objectId, BigDecimal creditLimit) {
        EdsCrmAccount account = crmAccountManager.get(objectId);
        account.setCreditLimit(creditLimit);
        crmAccountManager.update(account);
    }

    @Transactional
    public Integer saveAccount(CrmAccountItem item, String type, Integer userID, boolean changeOnlyGivenValue, boolean doNotAddToSolr, boolean isMerging, boolean populateAddress) {
        return saveAccount(item, type, userID, changeOnlyGivenValue, doNotAddToSolr, isMerging, populateAddress, true);
    }

    public Integer saveAccount(CrmAccountItem item, String type, Integer userID, boolean changeOnlyGivenValue, boolean doNotAddToSolr, boolean isMerging, boolean populateAddress, boolean runWebhook) {

        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();

        if (item == null) {
            return -3;
        }

        if (item.getNumber() == null || "".equals(item.getNumber())) {
            NumberData numberData = generateAccountNumberData(type);
            item.setNumber(numberData.getNumberString());
            item.setPrefix(numberData.getFirstNumberString());
            item.setIntNumber(numberData.getIntNumber());
        }
        if (item.getIntNumber() == null) {
            Integer lastInt = crmAccountManager.getLastNumber(null);
            item.setIntNumber(lastInt != null ? lastInt : 1);
        }
        if (!isMerging) {
            Integer result = crmAccountManager.isAccountNameOrNumberAlreadyExists(item.getName(), item.getNumber(), item.getObjectId());
            if (result == -2 && (companySettings == null || (companySettings != null && companySettings.getGenerateCrmAccountNumbering()))) {
                NumberData numberData = generateAccountNumberData(type);
                item.setNumber(numberData.getNumberString());
                item.setPrefix(numberData.getFirstNumberString());
                item.setIntNumber(numberData.getIntNumber());
            } else if (result != 0) {
                return result;
            }
        }
        boolean isNameChanged = false;
        EdsCrmAccount account = new EdsCrmAccount();
        Integer oldCurrency = null;
        if (item.getObjectId() != null) {
            account = crmAccountManager.get(item.getObjectId());
            oldCurrency = account != null && account.getCurrency() != null ? account.getCurrency().getObjectID() : null;
            isNameChanged = account != null && item.getName() != null && account.getName() != null && !account.getName().equals(item.getName());
            if (account == null) {
                account = new EdsCrmAccount();
            } else if (changeOnlyGivenValue) {
                CrmAccountItem item2 = account.getRPC(null, false);
                //hozircha faqat AccountType bunaqa ish uchun berilgan qaysi biri otdelniy yoziladigan bulsa keyinchalik qo'shish kerak
                if (item.getAccountTypes() != null && item.getAccountTypes().length > 0) {
                    item2.setAccountTypes(item.getAccountTypes());
                }
                item = item2;
            }
        } else {
            item.setCreationTime(new Date());
            account.setCreator(userManager.getUser());
        }
        EdsUser user = userID != null ? userManager.get(userID) : userManager.get(employeeManager.getUser().getObjectID());
        if (user == null) {
            user = employeeManager.getUser();
        }
        /*if (item.getOwnerID() != null) {
            account.setOwner(employeeManager.get(item.getOwnerID()));
        } else {
            account.setOwner(user);
        }*/
        List<EdsUser> owners = new ArrayList<>();
        if (item.getSelectedOwners() != null && item.getSelectedOwners().size() > 0) {
            item.getSelectedOwners().forEach(owner -> owners.add(userManager.get(owner.getId())));
            account.setOwners(owners);
        } else {
            owners.add(user);
            account.setOwners(owners);
        }
        if (item.getParent() != null && !item.getParent().isNew()) {
            account.setParent(crmAccountManager.get(item.getParent().getObjectId()));
        } else {
            account.setParent(null);
        }
        account.setObjectKey(item.getObjectKey());
        account.setName(item.getName());
        account.setNote(item.getNote());
        if (item.getLogoId() != null) {
            EdsAttachment attachment = attachmentManager.get(item.getLogoId());
            if (attachment != null) {
                account.setLogo(attachment);
            }
        }

        if (item.getEntityID() != null) {
            account.setEntityID(item.getEntityID());
        }
        account.getAccountTypes().clear();
        if (item.getAccountTypes() != null) {
            item.getAccountTypes();
            for (SelectItem selectItem : item.getAccountTypes()) {
                if (selectItem != null && selectItem.getId() != null && selectItem.isSelected()) {
                    EdsReference edsType = referenceManager.get(selectItem.getId());
                    if (edsType != null) {
                        account.getAccountTypes().add(edsType);
                    }
                }
            }
        }
        if (type != null && !"".equals(type)) {
            EdsReference crmAccountType = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, type);
            if (crmAccountType != null) {
                account.getAccountTypes().add(crmAccountType);
            }
        }
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        String clientFormat = settings.getClientFormat();
        if (item.getNumber() != null && !"".equals(item.getNumber())) {
            account.setNumber(item.getNumber());
            account.setPrefix(item.getPrefix());
            account.setNumberInteger(item.getIntNumber());
        } else {
            NumberData numberData = generateAccountNumberData(type);
            if (numberData.getFirstNumberString() != null && numberData.getIntNumber() != null) {
                account.setNumber(numberData.getFirstNumberString() + new DecimalFormat("0000").format(numberData.getIntNumber()));
            } else {
                account.setNumber(numberData.getNumberString());
            }
            account.setPrefix(numberData.getFirstNumberString());
            account.setNumberInteger(numberData.getIntNumber());
        }

        EdsReference industryItem = null;
        if (item.getIndustry() != null) {
            industryItem = referenceManager.getByName(item.getIndustry().trim());
        }
        account.setIndustry(null);
        if (item.getIndustryID() != null) {
            account.setIndustry(referenceManager.get(item.getIndustryID()));
        } else if (industryItem != null) {
            account.setIndustry(industryItem);
        }
        account.setEmail(item.getEmail());
        account.setPhone(item.getPhone());
        account.setFax(item.getFax());
        account.setWebsite(item.getWebsite());
        account.setSignupCompanyId(item.getCompanyId());

        if (item.getWarehouse() != null) {
            account.setWarehouse(warehouseManager.get(item.getWarehouse().getId()));
        } else {
            account.setWarehouse(null);
        }
        if (item.getDepartment() != null) {
            account.setDepartment(departmentManager.get(item.getDepartment().getId()));
        } else {
            account.setDepartment(null);
        }

        if (account.getEntityID() == null) {
            createEntity(account);
        }
        if (account.getCreationTime() == null) {
            account.setCreationTime(new Date());
        }
        account.setVatNumber(item.getVatNumber());
        account.setRegistrationNumber(item.getRegistrationNumber());
        if (item.getCurrencyId() != null) {
            account.setCurrency(currencyManager.get(item.getCurrencyId()));
        } else {
            if (item.getDefaultAddress(true).getCountryId() != null) {
                account.setCurrency(ServerUtils.getCurrencyIDByCountry(countryManager.get(item.getDefaultAddress(true).getCountryId())));
            } else {
                if (item.getDefaultAddress(false).getCountryId() != null) {
                    account.setCurrency(ServerUtils.getCurrencyIDByCountry(countryManager.get(item.getDefaultAddress(false).getCountryId())));
                } else {
                    CurrencyItem baseCurrency = invoiceServiceLocal.getBaseCurrency();
                    if (baseCurrency != null) {
                        EdsCurrency currency = currencyManager.get(baseCurrency.getId());
                        account.setCurrency(currency);
                    }
                }
            }
        }
        account.setPaymentMethod(null);
        if (item.getPaymentMethodId() != null) {
            account.setPaymentMethod(paymentMethodManager.get(item.getPaymentMethodId()));
        }
        account.setCampaign(null);
        if (item.getCampaignId() != null) {
            account.setCampaign(campaignManager.get(item.getCampaignId()));
        }
        account.setVat(null);
        if (CrmConstants.CUSTOMER.equals(type) && item.getTaxTreatment() != null &&
                (GCC_VAT_REGISTERED.equals(item.getTaxTreatment().getCode())
                        || NON_GCC.equals(item.getTaxTreatment().getCode())
                        || GCC_NON_VAT_REGISTERED.equals(item.getTaxTreatment().getCode()))) {

            EdsVat zeroRate = vatManager.getVatByKey(TaxKeyEnum.ZERO_RATE);
            if (zeroRate != null) {
                account.setVat(zeroRate);
            }

        } else {
            if (item.getVat() != null) {
                account.setVat(vatManager.get(item.getVat().getId()));
            }
        }
        account.setBankAccount(null);
        if (item.getBankAccountId() != null) {
            account.setBankAccount(accountingService.getBankAccount(item.getBankAccountId()));
        }
        if (item.isFromMobile()) {
            account.setLastUpdateTime(new Date());
        }
        boolean newCreated = crmAccountManager.createOrUpdate(account);

        if (item.getPrimaryContact() != null && item.getPrimaryContact().getObjectId() != null) {
            makePrimaryContact(account.getObjectID(), item.getPrimaryContact().getObjectId());
        } else {
            List<EdsCrmContact> contactsToSolr = new ArrayList<>();
            for (EdsCrmContact contact : account.getCrmContacts()) {
                contact.setPrimaryContact(Boolean.FALSE);
                crmContactManager.update(contact);
                contactsToSolr.add(contact);
            }
            if (!contactsToSolr.isEmpty()) {
                try {
                    contactSolrComponent.indexes(contactsToSolr);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.error("Error occurred while updating primary contact", e);
                }
            }
        }

        saveCrmNotes(CrmConstants.CRM_ACCOUNT, account.getObjectID(), item.getNotes());
        if (StringUtils.isNotBlank(type)) {
            saveCrmSubItems(item.getItems(), account, type);
        }
        updateAddresses(item.getBillAddresses(), account, EdsAddress.BILLING_ADDRESS, isMerging);
        updateAddresses(item.getMailAddresses(), account, EdsAddress.MAILING_ADDRESS, isMerging);
        account.setShowPrimaryContactAddress(item.isShowContactAddress());
        account.setCrNumber(item.getCrNumber());
        if (item.getSalesType() != null) {
            account.setSalesType(SalesType.valueOf(item.getSalesType()));
        }
        if (item.getPassportNumber() != null) {
            account.setPasportNumber((item.getPassportNumber()));
        }
        if (item.getVatCategory() != null) {
            account.setVatCategory(referenceManager.get(item.getVatCategory().getId()));
        } else if (item.getTaxTreatment() == null
                || !NON_VAT_REGISTERED.equals(item.getTaxTreatment().getCode())) {
            account.setVatCategory(null);
            item.setVatCategory(null);
        }
        if (item.getVatCategory() != null) {
            String vatCode = item.getVatCategory().getCode();

            if ("VATEX-SA-29".equals(vatCode) || "VATEX-SA-29-7".equals(vatCode) || "VATEX-SA-30".equals(vatCode)) {
                account.setVatCode("E");
            } else if ("VATEX-SA-MLTRY".equals(vatCode) || "VATEX-SA-HEA".equals(vatCode) ||
                    "VATEX-SA-EDU".equals(vatCode) || "VATEX-SA-36".equals(vatCode) ||
                    "VATEX-SA-35".equals(vatCode) || "VATEX-SA-34-5".equals(vatCode) ||
                    "VATEX-SA-34-4".equals(vatCode) || "VATEX-SA-34-3".equals(vatCode) ||
                    "VATEX-SA-34-2".equals(vatCode) || "VATEX-SA-34-1".equals(vatCode) ||
                    "VATEX-SA-33".equals(vatCode) || "VATEX-SA-32".equals(vatCode)) {
                account.setVatCode("Z");
            } else if ("VATEX-SA-OOS".equals(vatCode)) {
                account.setVatCode("O");
            } else {
                account.setVatCode(null);
            }
        } else {
            account.setVatCode(null);
        }


        if (EdsCrmAccount.SUPPLIER.equals(type) || account.isSupplier() || account.isClient()) {
            if (item.getTaxTreatmentId() != null) {
                //account.setTaxTreatment(referenceManager.get(item.getTaxTreatmentId()));
                account.setTaxtreatmentId(item.getTaxTreatmentId());
            }
            if (item.getTrn() != null) {
                account.setTrn(item.getTrn());
            }
            if (item.getPlaceOfSupplyCountryId() != null) {
                //account.setPlaceOfSupplyCountry(countryManager.get(item.getPlaceOfSupplyCountryId()));
                account.setPlaceofsupplyCountryId(item.getPlaceOfSupplyCountryId());
            }
            if (item.getPlaceOfSupplyStateId() != null) {
                //account.setPlaceOfSupplyState(regionManager.get(item.getPlaceOfSupplyStateId()));
                account.setPlaceofsupplyStateId(item.getPlaceOfSupplyStateId());
            }
        }

        if (EdsCrmAccount.SUPPLIER.equals(type) || account.isSupplier()) {
            account.setSupplierBalanceDate(item.getSupplierBalanceDate() != null ? item.getSupplierBalanceDate().getNonConvertedDate() : null);
            account.setSupplierBalanceAmount(item.getSupplierBalanceAmount() != null ? BigDecimal.valueOf(item.getSupplierBalanceAmount()) : null);

            account.setBankName(item.getBankName());
            account.setAccountName(item.getAccountName());
            account.setAccountNo(item.getAccountNo());
            account.setSwiftCode(item.getSwiftCode());
            account.setSortCode(item.getSortCode());
            account.setIbanCode(item.getIbanCode());
            account.setBranch(item.getBranch());
            account.setBankAddress(item.getBankAddress());
            account.setReverseChargeApplicable(item.isReverseChargeApplicable());
            account.setCreditLimit(item.getCreditLimit());
            account.setQuoteCreditLimit(item.getQuoteCreditLimit());

            account.setSubsidiary((item.getSubsidiary() != null && item.getSubsidiary().getId() != null) ? subsidiariesCompanyManager.get(item.getSubsidiary().getId()) : null);
            if (item.isCreateGlAccount()) {
                account.setPayable(accountingManager.getGlAccount(EdsAccount.ACCOUNTS_PAYABLE, account));
                account.setCreateGlAccount(item.isCreateGlAccount());
            } else {
                if (item.getAccountsReceivablePayable() != null && item.getAccountsReceivablePayable().getId() != null) {
                    EdsAccount payableAccount = accountingManager.get(item.getAccountsReceivablePayable().getId());
                    if (payableAccount != null && (Integer.valueOf(EdsAccount.ACCOUNTS_PAYABLE).equals(payableAccount.getKey()) || Integer.valueOf(EdsAccount.ACCOUNTS_PAYABLE).equals(payableAccount.getGroupKey()))) {
                        account.setPayable(payableAccount);
                    }
                } else {
                    if (EdsCrmAccount.SUPPLIER.equals(type)) {
                        account.setPayable(null);
                    }
                }
                account.setCreateGlAccount(item.isCreateGlAccount());
            }
            if (newCreated) {
                UUID externalGUID = UUID.randomUUID();
                account.setExternalGUID(externalGUID.toString());
            }

            if (item.isFromSaasu()) {
                account.setLastUpdateTime(item.getSaasuLastUpdatedDate());
            } else {
                account.setLastUpdateTime(new Date());
            }
            account.setSaasuGUID(item.getSaasuGUID());
            account.setSasuuLastUpdatedTime(item.getSaasuLastUpdatedDate());
            account.setSaasuLastUpdatedUid(item.getSaasuLastUpdatedUid());

            accountingService.createOrUpdateSupplierTransaction(account.getObjectID(), user);
        }
        if (account.isClient()) {
            if (item.getQuickbookCustomerID() != null) {
                account.setQuickbookCustomerID(item.getQuickbookCustomerID());
                account.setQuickbookEditSequence(item.getQuickbookEditSequence());
            }

            if (item.getTermsItem() != null && item.getTermsItem().getId() != null) {
                account.setTerms(invoiceTermsManager.get(item.getTermsItem().getId()));
            }

            if (item.getClientType() != null) {
                account.setClientType(referenceManager.get(item.getClientType().getId()));
            }
            account.setBalanceDate(item.getBalanceDate() != null ? item.getBalanceDate().getNonConvertedDate() : null);
            account.setBalanceAmount(item.getBalanceAmount() != null ? BigDecimal.valueOf(item.getBalanceAmount()) : null);

            account.setCreditLimit(item.getCreditLimit());
            account.setQuoteCreditLimit(item.getQuoteCreditLimit());

            account.setSubsidiary((item.getSubsidiary() != null && item.getSubsidiary().getId() != null) ? subsidiariesCompanyManager.get(item.getSubsidiary().getId()) : null);

            if (item.isCreateGlAccount()) {
                account.setReceivable(accountingManager.getGlAccount(EdsAccount.ACCOUNTS_RECEIVABLE, account));
                account.setCreateGlAccount(item.isCreateGlAccount());
            } else {
                if (item.getAccountsReceivablePayable() != null && item.getAccountsReceivablePayable().getId() != null) {
                    EdsAccount receivableAccount = accountingManager.get(item.getAccountsReceivablePayable().getId());
                    if (receivableAccount != null && (Integer.valueOf(EdsAccount.ACCOUNTS_RECEIVABLE).equals(receivableAccount.getKey()) || Integer.valueOf(EdsAccount.ACCOUNTS_RECEIVABLE).equals(receivableAccount.getGroupKey()))) {
                        account.setReceivable(receivableAccount);
                    }
                } else {
                    if (EdsCrmAccount.CUSTOMER.equals(type)) {
                        account.setReceivable(null);
                    }
                }
                account.setCreateGlAccount(item.isCreateGlAccount());
            }
            if (newCreated) {
                UUID externalGUID = UUID.randomUUID();
                account.setExternalGUID(externalGUID.toString());
            }

            account.setSaasuGUID(item.getSaasuGUID());
            account.setSasuuLastUpdatedTime(item.getSaasuLastUpdatedDate());
            account.setSaasuLastUpdatedUid(item.getSaasuLastUpdatedUid());
            if (item.isFromSaasu()) {
                account.setLastUpdateTime(item.getSaasuLastUpdatedDate());
            } else {
                account.setLastUpdateTime(new Date());
            }

            account.setMagentoEntityId(item.getMagentoEntityId());
            account.setMagentoLastSyncDate(item.getMagentoLastSyncDate());
            if (newCreated) {
                baseEventPostProcessor.registerEvent(ClientEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, account, user);
            }

            accountingService.createOrUpdateCustomerTransaction(account.getObjectID(), user);

            createClientGroupToClientContact(account);
        } else {
            if (item.getTermsItem() != null && item.getTermsItem().getId() != null) {
                account.setTerms(invoiceTermsManager.get(item.getTermsItem().getId()));
            }
        }
        if (item.getContacts() != null && item.getContacts().size() > 0) {
            for (ContactListItem contactListItem : item.getContacts()) {
                if ((account.isClient() || account.isSupplier()) && contactListItem.getObjectId() == null) {
                    ArrayList<SelectItem> selectItems = new ArrayList<>();
                    for (EdsReference reference : account.getAccountTypes()) {
                        if (CrmAccountItem.CUSTOMER.equalsIgnoreCase(reference.getCode()) || CrmAccountItem.SUPPLIER.equalsIgnoreCase(reference.getCode())) {
                            EdsContactCategory category = contactCategoryManager.getDefaultCategoryByContactType(CrmAccountItem.CUSTOMER.equalsIgnoreCase(reference.getCode()) ? ContactListItem.CLIENT_CONTACT : ContactListItem.SUPPLIER_CONTACT);
                            if (category != null) {
                                SelectItem selectItem = new SelectItem(category.getObjectID(), category.getName());
                                selectItem.setSelected(true);
                                selectItems.add(selectItem);
                            }
                        }
                    }
                    if (!selectItems.isEmpty()) {
                        contactListItem.setSelectedCategories(selectItems);
                    }
                }
                contactListItem.getCrmAccount().setObjectId(account.getObjectID());
                Integer contactID = contactServiceLocal.saveContact(contactListItem, null, user, true, runWebhook);
                if (contactID != null) {
                    if (contactID != null && contactID == -1) {
                        contactListItem.setCheckForDuplicates(false);
                        contactID = contactServiceLocal.saveContact(contactListItem, null, null, true, true);
                    }
                    if (contactID != null && contactID > 0 && account.isClient()) {
                        if (contactListItem.isAccessEnabled()) {
                            enableAccess(contactID, null);
                        } else {
                            disableAccess(contactID);
                        }
                    }

                    if (contactID != null && contactID > 0) {
                        account.getCrmContacts().add(crmContactManager.get(contactID));
                    }
                }
            }
        }
        if (item.getCustomFields() != null && item.getCustomFields().size() > 0 && account.getCustomFields() != null) {
            StringBuilder changesBuilder = new StringBuilder();
            for (CompanyCustomFieldItem cit : item.getCustomFields()) {
                changesBuilder.append(account.getCustomFields() != null && CustomFieldsUtils.getObjectValue(account.getCustomFields(), cit.getColumnCode()) != null ? getChanges(CustomFieldsUtils.getObjectValue(account.getCustomFields(), cit.getColumnCode()), cit) : (cit.getColumnCode() + ","));
            }
            String changes = changesBuilder.toString();
            if (!"".equals(changes)) {
                account.addCustomFieldChanges(changes);
            }
        }
        account.setCustomFields(saveCustomFields(account.getCustomFields(), item.getCustomFields()));
        updateCrmAccountAndAddToSolr(account, newCreated, user);

        /*set currency to clint start*/
        if (oldCurrency != null || account.getCurrency() != null) {
            priceLevelServiceLocal.setPriceLevelToClient(item.getAppliedPriceLavel(), account, oldCurrency == null ? account.getCurrency().getObjectID() : oldCurrency);
        }
        /*set currency to clint end*/

        if (item.getAppliedDiscounts() != null) {
            account.getDiscounts().removeAll(account.getDiscounts());

            for (SelectItem discount : item.getAppliedDiscounts()) {
                EdsDiscount edsDiscount = discountManager.get(discount.getId());
                if (edsDiscount != null) {
                    account.getDiscounts().add(edsDiscount);
                }
            }
        }

        if (isNameChanged) {
            baseEventPostProcessor.registerEvent(CrmContactCustomEventListenerImpl.TYPE_ACCOUNT, CrmContactCustomEventListenerImpl.EVENT_CRM_ACCOUNT_NAME_CHANGED, account, user);
        }
        if (!item.isFromSignUp()) {
            baseEventPostProcessor.registerEvent(CrmAccountEventListenerImpl.TYPE, (newCreated ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT), account, user);
        }
        attachmentUtilsManager.saveAttachments(F_CRM_ACCOUNT, account.getObjectID(), account.getObjectID(), item.getAttachments());

        if (account.getObjectID() != null) {
            crmContactItemParamsManager.deleteAllAccountItemParams(account.getObjectID());
        }

        if (item.getTelegramChats() != null && !item.getTelegramChats().isEmpty()) {
            for (Map.Entry<Integer, ArrayList<String>> entry_ : item.getItemsParam(item).entrySet()) {
                Integer relation = entry_.getKey();
                List<String> values = entry_.getValue();
                if (values != null && values.size() > 0) {
                    for (String value : values) {
                        if (value != null && !"".equals(value.trim())) {
                            createItemParam(account, Constants.CONTACT_TELEGRAMS, relation, value);
                        }
                    }
                }
            }
        }


        baseEventPostProcessor.registerEvent(CrmAccountEventListenerImpl.TYPE, CrmAccountEventListenerImpl.EVENT_REINDEX_SQ_SI_PI_ER, account, user);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmAccount.class.getSimpleName());
        kpiLog.setEntityId(account.getObjectID());
        //shu erni qaytadan ko'rib chiqish kerak.
        if (newCreated) {
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            if (CrmConstants.SUPPLIER.equalsIgnoreCase(type)) {
                kpiLog.setEntityType(KpiEntityType.SUPPLIER);
            } else if (EdsCrmAccount.CUSTOMER.equalsIgnoreCase(type)) {
                kpiLog.setEntityType(KpiEntityType.CUSTOMER);
            } else if (type == null) {
                kpiLog.setEntityType(KpiEntityType.ACCOUNT);
            }
            ServerUtils.kpiLog(log, kpiLog, "Added CRM Account");

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, account, user);
            workflowEvent.setEntityType(RelationItem.TYPE_CRM_ACCOUNT);
        } else {
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            if (CrmConstants.SUPPLIER.equalsIgnoreCase(type)) {
                kpiLog.setEntityType(KpiEntityType.SUPPLIER);
            } else if (EdsCrmAccount.CUSTOMER.equalsIgnoreCase(type)) {
                kpiLog.setEntityType(KpiEntityType.CUSTOMER);
            } else if (type == null) {
                kpiLog.setEntityType(KpiEntityType.ACCOUNT);
            }
            ServerUtils.kpiLog(log, kpiLog, "Updated CRM Account");

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, account, user);
            workflowEvent.setEntityType(RelationItem.TYPE_CRM_ACCOUNT);
        }

        return account.getObjectID();
    }

    private EdsCrmContactItemParams createItemParam(final EdsCrmAccount account, int param, int relation, String value) {
        if (value != null && !"".equals(value)) {
            EdsCrmContactItemParams params = new EdsCrmContactItemParams();
            params.setAccount(account);
            params.setLastUpdateTime(new Date());
            params.setParam(param);
            params.setRelation(relation);
            params.setValue(value);
            crmContactItemParamsManager.create(params);
            account.getItemParams().add(params);
            return params;
        }
        return null;
    }

    @Override
    @Transactional
    public void saveClientCellValue(CrmAccountItem rowValue, String columnCodeName) {
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(rowValue.getObjectId());
        try {
            EdsCrmCustomFields edsCrmCustomFields = edsCrmAccount.getCustomFields();
            if (edsCrmCustomFields == null) {
                edsCrmCustomFields = new EdsCrmCustomFields();
                crmCustomFieldsManager.create(edsCrmCustomFields);
                edsCrmAccount.setCustomFields(edsCrmCustomFields);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(edsCrmCustomFields, rowValue.getCustomFieldsMap(), columnCodeName);

            crmAccountManager.update(edsCrmAccount);
//            solrManager.addCrmAccountToIndex(edsCrmAccount);
            crmAccountSolrComponent.index(edsCrmAccount);

        } catch (Exception e) {
            log.error("Customer List Edit Cell Column Code :" + columnCodeName, e);
        }
    }

    @Override
    public SelectItem getAccountCurrency(Integer accountId) {
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(accountId);
        if (edsCrmAccount != null && edsCrmAccount.getCurrency() != null) {
            return edsCrmAccount.getCurrency().getAsSelectItem();
        }
        return null;
    }

    private void saveCrmSubItems(List<CrmSubItem> items, EdsCrmAccount crmAccount, String type) {
        crmSubItemManager.deleteItems(crmAccount.getObjectID(), type);
        if (items != null && !items.isEmpty()) {
            for (CrmSubItem crmSubItem : items) {
                EdsCrmSubItem subItem = new EdsCrmSubItem();
                subItem.setEntityId(crmAccount.getObjectID());
                subItem.setEntityType(type);
                subItem.setItem(itemManager.getItem(crmSubItem.getItemID()));
                subItem.setDescription(crmSubItem.getDescription());
                subItem.setQty(crmSubItem.getQty());
                subItem.setPrice(crmSubItem.getPrice());
                if (crmSubItem.getUnitMeasurement() != null) {
                    subItem.setUnitMeasurement(unitMeasurementManager.get(crmSubItem.getUnitMeasurement().getId()));
                }
                subItem.setCustomFields(createCrmSubItemCustomFields(crmSubItem.getItemCustomFields()));
                crmSubItemManager.create(subItem);
            }
        }
    }

    public EdsCrmSubItemCustomFields createCrmSubItemCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsCrmSubItemCustomFields subItemCustomFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                subItemCustomFields = crmSubItemCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                subItemCustomFields = new EdsCrmSubItemCustomFields();
                crmSubItemCFManager.create(subItemCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(subItemCustomFields, customFieldItems);
            return subItemCustomFields;
        }
        return null;
    }

    @Override
    public boolean updateAddress(ListingFilterParameter filterParametrs, Address address, Integer type) {
        Address[] addresses = new Address[1];
        addresses[0] = address;
        EdsCrmAccount account = null;
        EdsCrmContact contact = null;
        EdsEmployee edsEmployee = null;
        if ("supplier".equals(filterParametrs.getViewType()) || "client".equals(filterParametrs.getViewType())
                || "crmAccount".equals(filterParametrs.getViewType()) || "account".equals(filterParametrs.getViewType())) {
            account = crmAccountManager.get(filterParametrs.getObjectId());
            return updateAddresses(addresses, account, type, false);
        } else if ("contact".equals(filterParametrs.getViewType()) || "lead".equals(filterParametrs.getViewType())
                || "candidate".equals(filterParametrs.getViewType())) {
            contact = crmContactManager.get(filterParametrs.getObjectId());
            return updateAddresses(addresses, contact, type, false);
        } else if ("employee".equals(filterParametrs.getViewType())) {
            edsEmployee = employeeManager.get(filterParametrs.getObjectId());
            if (edsEmployee != null && edsEmployee.getContact() != null) {
                return updateAddresses(addresses, edsEmployee.getContact(), type, false);
            }
        }
        return false;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ListResult<LogHistoryItem> getOpportunityLogHistoryList(ListingFilterParameter listingFilterParameter) {
        ArrayList<LogHistoryItem> subItems = new ArrayList<>();
        EdsOpportunity item = opportunityManager.get(listingFilterParameter.getEntityID());
        if (item != null && item.getLogHistories() != null) {
            // Convert EdsHistoryLog to LogHistoryItem
            List<LogHistoryItem> allItems = item.getLogHistories().stream()
                    .map(EdsHistoryLog::toRpc)
                    .collect(Collectors.toList());

            // Apply search filter if searchKey is present
            List<LogHistoryItem> filteredItems;
            if (listingFilterParameter.getSearchKey() != null && !listingFilterParameter.getSearchKey().isEmpty()) {
                String searchKeyLower = listingFilterParameter.getSearchKey().toLowerCase();
                filteredItems = allItems.stream()
                        .filter(historyItem -> {
                            String field = historyItem.getField() != null ? historyItem.getField().toLowerCase() : "";
                            String userName = historyItem.getUserName() != null ? historyItem.getUserName().toLowerCase() : "";
                            return field.contains(searchKeyLower) || userName.contains(searchKeyLower);
                        })
                        .collect(Collectors.toList());
            } else {
                filteredItems = allItems;
            }

            // Apply sorting
            if (!filteredItems.isEmpty()) {
                if ("MODIFIED_DATE".equals(listingFilterParameter.getSortField()) && listingFilterParameter.isAscending()) {
                    filteredItems.sort(Comparator.comparing(LogHistoryItem::getUpdatedDate, Comparator.nullsLast(Comparator.naturalOrder())));
                } else {
                    filteredItems.sort((o1, o2) -> {
                        Date date1 = o1.getUpdatedDate();
                        Date date2 = o2.getUpdatedDate();
                        if (date1 == null && date2 == null) return 0;
                        if (date1 == null) return 1;
                        if (date2 == null) return -1;
                        return date2.compareTo(date1);
                    });
                }
            }

            // Apply pagination
            int totalSize = filteredItems.size();
            int start = listingFilterParameter.getStart();
            int limit = listingFilterParameter.getLimit();

            if (start < totalSize) {
                int end = Math.min(start + limit, totalSize);
                subItems.addAll(filteredItems.subList(start, end));
            }

            return new ListResult<>(subItems, totalSize);
        }
        return new ListResult<>(subItems, 0);
    }

    @Transactional
    public boolean updateAddresses(Address[] addresses, EdsObject obj, Integer entityType, boolean isMerging) {
        return updateAddresses(addresses, obj, entityType, isMerging, false);
    }

    public boolean updateAddresses(Address[] addresses, EdsObject obj, Integer entityType, boolean isMerging, boolean isHrms) {

        EdsCrmAccount account = null;
        EdsCrmContact contact = null;
        EdsCompany company;
        List<EdsAddress> addressList = null;
        boolean result = isMerging;

        if (obj instanceof EdsCrmAccount) {
            account = (EdsCrmAccount) obj;
            addressList = addressManager.getAddressesByEntityIdAndType(account.getObjectID(), entityType, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
        } else if (obj instanceof EdsCrmContact) {
            contact = (EdsCrmContact) obj;
            addressList = addressManager.getContactAddresses(contact.getObjectID());
        } else if (obj instanceof EdsCompany) {
            company = (EdsCompany) obj;
            addressList = addressManager.getAddressesByEntityIdAndType(company.getObjectID(), entityType, EdsAddress.ENTITY_TYPE_COMPANY);
        }

        if (addressList != null) {
            for (EdsAddress ba : addressList) {
                boolean contains = false;
                if (addresses != null) {
                    for (Address data : addresses) {
                        if (data.getObjectID() != null && data.getObjectID().equals(ba.getObjectID())) {
                            contains = true;
                        }
                    }
                }
                if (!contains) {
                    ba.setDeleted(true);
                    result = true;
                    addressManager.update(ba);
                }
            }
        }

        if (account != null && entityType != null) {
            if (EdsAddress.BILLING_ADDRESS.equals(entityType)) {
                account.setBillingAddress(null);
            } else {
                account.setMailingAddress(null);
            }
        }
        if (addresses != null) {
            EdsAddress primaryAddress = null;
            for (Address data : addresses) {
                if (data.getObjectID() != null && data.getRelationType() != null && data.getName() == null) {
                    isMerging = true;
                }
                EdsAddress edsAddress;
                if (primaryAddress == null) {
                    edsAddress = primaryAddress = createOrUpdateAddress(data, obj, entityType == null ? data.getRelationType() : entityType, isMerging, isHrms);
                    result = result ? result : primaryAddress.isChanged();
                } else {
                    edsAddress = createOrUpdateAddress(data, obj, entityType == null ? data.getRelationType() : entityType, isMerging, isHrms);
                    result = result ? result : edsAddress.isChanged();
                }
                if (edsAddress != null && contact != null) {
                    contact.getAddresses().add(edsAddress);
                }

            }
            if (account != null && primaryAddress != null) {
                if (EdsAddress.BILLING_ADDRESS.equals(entityType)) {
                    if (account.getBillingAddress() == null) {
                        account.setBillingAddress(primaryAddress);
                    }
                } else if (EdsAddress.MAILING_ADDRESS.equals(entityType)) {
                    if (account.getMailingAddress() == null) {
                        account.setMailingAddress(primaryAddress);
                    }
                }
                if (Integer.valueOf(1).equals(account.getObjectID())) {
                    changeCompanyAddress(primaryAddress, entityType);
                }
            }

        }
        return result;
    }

    private void changeCompanyAddress(EdsAddress address, Integer entityType) {
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();

        if (EdsAddress.BILLING_ADDRESS.equals(entityType)) {
            EdsAddress billingAddress = EdsAddress.getFirstAddress(company.getBillingAddresses(), true, null);
            if (billingAddress == null) {
                billingAddress = new EdsAddress();
                billingAddress.setEntity(company);
                billingAddress.setRelationType(EdsAddress.BILLING_ADDRESS);
            }
            Address temp = address.getRPC();
            temp.setObjectID(null);
            billingAddress.setAddressData(temp);

        } else if (EdsAddress.MAILING_ADDRESS.equals(entityType)) {
            EdsAddress mailingAddress = EdsAddress.getFirstAddress(company.getMailingAddresses(), true, null);
            if (mailingAddress == null) {
                mailingAddress = new EdsAddress();
                mailingAddress.setEntity(company);
                mailingAddress.setRelationType(EdsAddress.MAILING_ADDRESS);
            }
            Address temp = address.getRPC();
            temp.setObjectID(null);
            mailingAddress.setAddressData(temp);
        }
        EdsCountry country = address.getCountry() != null ? address.getCountry() : new EdsCountry();
        genericSettingsManager.saveGenericSettings(company.getObjectID(), GenericSettingsEnum.VAT_RETURN_ENABLE, "GB".equals(country.getCode()) ? EdsGenericSettings.YES : EdsGenericSettings.NO);
    }

    private EdsAddress createOrUpdateAddress(Address data, EdsObject obj, Integer entityType, boolean isMerging, boolean isHrms) {

        EdsCrmAccount account = null;
        EdsCrmContact contact = null;
        EdsCompany company = null;
        if (isMerging) {
            EdsAddress address1 = addressManager.get(data.getObjectID());
            if (address1 != null) {
                data.setName(address1.getName());
                data.setCity(address1.getCity());
                data.setAddress(address1.getAddress());
                data.setAddressb(address1.getAddressb());
                data.setCountry(address1.getCountry() != null ? address1.getCountry().getName() : null);
                data.setCountryId(address1.getCountry() != null ? address1.getCountry().getObjectID() : null);
                data.setZipCode(address1.getZipCode());
                data.setState(address1.getStateName());
                data.setStateId(address1.getState() != null ? address1.getState().getObjectID() : null);
                data.setPrimary(address1.isPrimary());
                data.setLinkedAddress(address1.isLinkedAddress());
                data.setLinkedAddressID(address1.getLinkedAddressID());
                data.setLongitude(address1.getLongitude());
            }
        }
        if (obj instanceof EdsCrmAccount) {
            account = (EdsCrmAccount) obj;
        } else if (obj instanceof EdsCrmContact) {
            contact = (EdsCrmContact) obj;
        } else if (obj instanceof EdsCompany) {
            company = (EdsCompany) obj;
        }
        EdsCountry country = (data.getCountryId() != null ? countryManager.get(data.getCountryId()) : null);
        if (country == null && data.getCountry() != null) {
            country = countryManager.getCountryByName(data.getCountry());
        }
        EdsRegion state = (data.getStateId() != null ? regionManager.get(data.getStateId()) : null);
        EdsAddress address = (data.getObjectID() != null && addressManager.get(data.getObjectID()) != null) ? addressManager.get(data.getObjectID()) : new EdsAddress();
        address.setEntity(obj);
        if (address.getRelationType() == null)
            address.setRelationType(entityType);
//        if (!isMerging && !isHrms && !"".equals(data.getZipCode()) && data.getZipCode() != null) {
//            Double[] geoLocation = getLatitudeLongitude(data.getZipCode());
//
//            if (geoLocation[0] != null && geoLocation[1] != null) {
//                data.setLatitude(geoLocation[0]);
//                data.setLongitude(geoLocation[1]);
//            }
//        }
        address.setAddressData(data, country, state);
        if (!isMerging || data.getObjectID() != null) {
            addressManager.createOrUpdate(address);
            if (account != null && data.isPrimary()) {
                if (EdsAddress.BILLING_ADDRESS.equals(entityType)) {
                    account.setBillingAddress(address);
                } else if (EdsAddress.MAILING_ADDRESS.equals(entityType)) {
                    account.setMailingAddress(address);
                }
            }
        }
        return address;
    }

    @Transactional
    public Integer enableAccess(Integer contactID, Boolean fromSubscriptionForm) {
        return clientSupplierAccessService.enableAccess(contactID, fromSubscriptionForm, true);
    }

    @Override
    public Integer disableAccess(Integer contactID) {
        return clientSupplierAccessService.disableAccess(contactID);
    }

    @Transactional
    public boolean resendClientActivationLink(Integer clientContactID) {
        EdsClientContact clientContact = clientContactManager.getClientContactByCrmContact(clientContactID);
        if (clientContact != null) {
            boolean userNameExist = false;
            return clientSupplierAccessService.sendActivationLinkToClientContact(clientContact, userNameExist);
        }
        return false;
    }

    @Transactional
    public void activateOrDeActivateClientContact(Integer clientContactID, boolean activate) {
        try {
            EdsClientContact clientContact = clientContactManager.getClientContactByCrmContact(clientContactID);
            if (clientContact != null) {
                if (activate) {
                    clientContact.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE));
                } else {
                    clientContact.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_INACTIVE));
                }
                clientContactManager.update(clientContact);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Transactional
    @Override
    public Boolean smsSendTo(SmsSendItem smsSendItem) {
        EdsSmsSendItem item = new EdsSmsSendItem();
        item.setUserID(userManager.getUser() != null ? userManager.getUser().getObjectID() : null);
        item.setToNumber(smsSendItem.getToNumber());
        item.setSentDate(new Date());
        item.setMessageText(smsSendItem.getMessageText());
        item.setEntityID(smsSendItem.getEntityID());
        item.setProvider(smsManager.get(smsSendItem.getSettingID()));
        Boolean isSuccess = messageManager.generateAndSendSms(item);
        String relationType = smsSendItem.getRelations() != null ? smsSendItem.getRelations().get(0).getToType() : null;
        if (isSuccess) {
            smsSendItemManager.create(item);
            EdsEvent smsEvent = new EdsEvent();
            smsEvent.setActivityType(Appointment.SMS);
            smsEvent.setSubject("SMS: " + (smsSendItem.getMessageText().length() > 20 ? smsSendItem.getMessageText().substring(0, 20) : smsSendItem.getMessageText()));
            smsEvent.setDescription(smsSendItem.getMessageText());
            smsEvent.setStartDate(new Date());
            smsEvent.setEndDate(new Date());
            smsEvent.setCreationTime(new Date());
            smsEvent.setLastUpdateTime(new Date());
            smsEvent.setLastModifiedDate(new Date());
            smsEvent.setCreator(userManager.getUser());
            smsEvent.setLastModifiedBy(userManager.getUser());
            smsEvent.setAllDay(false);
            if (smsSendItem.isHrms()) {
                smsEvent.setCreatedFrom(Appointment.FROM_HRMS);
            }
            eventManager.create(smsEvent);
            allInOneServiceLocal.saveRelations(RelationItem.TYPE_EVENT, smsEvent.getObjectID(), smsEvent.getSubject(), smsSendItem.getRelations());

            EdsEmployeeEvent employeeEvent = new EdsEmployeeEvent();
            employeeEvent.setEvent(smsEvent);
            try {
                employeeEvent.setEmployee(userManager.getUser());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (relationType.equals(RelationItem.TYPE_CRM_ACCOUNT)) {
                sendActivityToCustomer(smsSendItem);
            }

            employeeEventManager.create(employeeEvent);
            eventManager.addToSolr(smsEvent);
        }

        return isSuccess;
    }

    private void sendActivityToCustomer(SmsSendItem smsSendItem) {
        Appointment appointment = new Appointment();
        appointment.setActivityType(1);
        appointment.setSubject("SMS: " + (smsSendItem.getMessageText().length() > 20 ? smsSendItem.getMessageText().substring(0, 20) : smsSendItem.getMessageText()));
        appointment.setDescription(smsSendItem.getMessageText());
        appointment.setStartDate(new Date());
        appointment.setEndDate(new Date());
        appointment.setCreatedBy(userManager.getUser().toString());
        appointment.setTaskCreator(userManager.getUser().getName());
        appointment.setCrmAccountRelation(smsSendItem.getRelations().get(0));
        googleCalendarService.saveCalendarEvent(userManager.getUser().getObjectID(), appointment, false);
    }

    @Override
    public ArrayList<SmsSendItem> getSmsNotes(ListingFilterParameter fp) {
        ArrayList<SmsSendItem> listItems = new ArrayList<>();
        List<EdsSmsSendItem> items = null;
        if (fp != null && (Integer.valueOf(0)).equals(fp.getEntityID())) {
            if (fp.getContactID() != null || fp.getLeadID() != null) {
                Integer objectID = fp.getContactID() != null ? fp.getContactID() : fp.getLeadID();
                EdsCrmContact crmContact = crmContactManager.get(objectID);
                if (crmContact != null && crmContact.getEntityID() != null) {
                    fp.setEntityID(crmContact.getEntityID());
                    items = smsSendItemManager.getSmsList(fp.getEntityID());
                }
            }
        }
        if (items != null) {
            for (EdsSmsSendItem item : items) {
                SmsSendItem sms = new SmsSendItem();
                sms.setObjectID(item.getObjectID());
                sms.setMessageText(item.getMessageText());
                sms.setToNumber(item.getToNumber());
                listItems.add(sms);
            }
        }
        return listItems;
    }

    @Override
    public void deleteSmsSendItem(Integer objectID) {
        EdsSmsSendItem smsSendItem = smsSendItemManager.get(objectID);
        if (smsSendItem != null) {
            smsSendItem.setDelete(true);
        }
    }

    @Override
    public String generateSMSTemplate(Integer i, ContactListItem lead, EmployeeListItem employee) {
        EdsSMSTemplates item = smsTemplateManager.get(i);
        return messageManager.smsTemplateGenerateText(item.getContent(), lead, employee);
    }

    @Override
    public String generateSMSTemplateForSalesInvoice(Integer i, Integer saleInvoiceId) {
        EdsSMSTemplates item = smsTemplateManager.get(i);
        return messageManager.smsTemplateGenerateTextForSalesInvoice(item.getContent(), saleInvoiceId);
    }

    @Override
    public LinkedHashMap<String, String> generateEmployeeEventTemplate(Integer templateId, EmployeeListItem employee) {
        EdsEmailTemplate item = emailTemplateManager.get(templateId);
        LinkedHashMap<String, String> subjectWithContent = new LinkedHashMap<>();
        String subject = messageManager.generateEmployeeEventTemplate(item.getSubject(), employee);
        String content = messageManager.generateEmployeeEventTemplate(item.getMessageHTML(), employee);
        subjectWithContent.put(subject, content);
        return subjectWithContent;
    }

    @Override
    public Appointment generateCandidateEventTemplate(Integer templateId, ContactListItem lead) {
        EdsEmailTemplate item = emailTemplateManager.get(templateId);
        String subject = messageManager.generateCandidateEventTemplate(item.getSubject(), lead, true).getTemplateValue();
        Appointment appointment = messageManager.generateCandidateEventTemplate(item.getMessageHTML(), lead, false, item.getSubject());
        appointment.setTemplateSubject(subject);
        return appointment;
    }

    @Override
    public String generateCrmAccountSMSTemplate(Integer templateId, CrmAccountItem crmAccount) {
        EdsSMSTemplates item = smsTemplateManager.get(templateId);
        return messageManager.crmAccountSmsTemplateGenerateText(item.getContent(), crmAccount);
    }

    @Override
    @Transactional
    public Boolean mergeAccounts(CrmAccountItem mainItem, boolean deleteOthers, ArrayList<Integer> otherObjectIDs) {
        Integer objectID = saveAccount(mainItem, null, null, false, false, true, true);
        if (otherObjectIDs != null) {
            otherObjectIDs.remove(objectID);
        }
        EdsCrmAccount savedAccount = crmAccountManager.get(objectID);
        List<EdsCrmAccount> otherAccounts = crmAccountManager.getCrmAccountsByIDs(otherObjectIDs);
        if (otherAccounts.size() > 0) {
            copyDetailsOfAccounts(savedAccount, otherAccounts);

            invoiceServiceLocal.mergeCrmAccounts(objectID, otherObjectIDs);
            // merge project client
            projectServiceLocal.mergeProjectAccounts(objectID, otherObjectIDs);

            // merge task client
            relationManager.mergeRelationByType(RelationItem.TYPE_TASK, savedAccount.getObjectID(), savedAccount.getName(), otherObjectIDs);
            taskServiceLocal.mergeTaskAccounts(objectID, otherObjectIDs);

            //merge events
            relationManager.mergeRelationByType(RelationItem.TYPE_EVENT, savedAccount.getObjectID(), savedAccount.getName(), otherObjectIDs);
            mergeEvent(objectID, otherObjectIDs);

            //merge Opportunity
            mergeOpportunity(objectID, otherObjectIDs);
            // merge student customer in Training Center
            studentManager.mergeStudentCustomers(otherObjectIDs, objectID);

            // merge course booking customer in Training Center
            courseBookingManager.mergeBookingCustomers(otherObjectIDs, objectID);

            deleteCrmAccount(otherObjectIDs, false);
        }
        return Boolean.TRUE;
    }

    private void mergeEvent(Integer objectID, ArrayList<Integer> otherObjectIDs) {
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(objectID);
        EdsCompany edsCompany = userManager.getUser().getCompany();
        if (otherObjectIDs != null) {
            List<EdsRelation> eventRelations = relationManager.getRelationsByRelationFromTypeToID(RelationItem.TYPE_EVENT, objectID);
            for (EdsRelation eventRelation : eventRelations) {
                EdsEvent event = eventManager.get(eventRelation.getFromID());
                try {
                    eventSolrComponent.index(event);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void mergeOpportunity(Integer objectID, ArrayList<Integer> otherObjectIDs) {
        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        EdsCrmAccount newAccount = crmAccountManager.get(objectID);

        for (Integer accountId : otherObjectIDs) {
            List<EdsOpportunity> opportunities = opportunityManager.getOpportunityByCrmAccountID(accountId);

            for (EdsOpportunity op : opportunities) {
                op.setCrmAccount(newAccount);
                opportunityManager.update(op);
                try {
                    opportunitySolrComponent.index(op);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void copyDetailsOfAccounts(EdsCrmAccount savedAccount, List<EdsCrmAccount> otherAccounts) {
        List<Integer> otherAccountIDs = EdsCrmAccount.getObjectIDs(otherAccounts);
        //move Contacts
        crmContactManager.updateContactWithAccountID(savedAccount.getObjectID(), otherAccountIDs);
        List<EdsCrmContact> othersContacts = crmContactManager.getContactsByCrmAccount(savedAccount.getObjectID());
        if (!othersContacts.isEmpty()) {
            try {
                contactSolrComponent.indexes(othersContacts);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        relationManager.mergeCrmAccountRelations(savedAccount.getObjectID(), savedAccount.getName(), otherAccountIDs);
        //move Notes
        noteHistoryManager.updateNotesWithAccountID(savedAccount.getObjectID(), otherAccountIDs);
    }

    @Transactional
    public void createClientGroupToClientContact(EdsCrmAccount client) {
        if (client.getObjectID() != null) {
            List<EdsClientContact> clientContacts = clientContactManager.getAccessEnabledContacts(client);
            if (clientContacts != null && clientContacts.size() > 0) {
                for (EdsClientContact clientContact : clientContacts) {
                    initClientGroups(clientContact);
                }
            }
        }
    }

    @Transactional
    public void initClientGroups(EdsUser client) { //only added clients group;
        clientSupplierAccessService.initClientGroups(client);
    }

    @Transactional
    public void updateCrmAccountAndAddToSolr(EdsCrmAccount account, boolean newCreated, EdsUser user) {
        crmAccountManager.update(account);
        try {
            crmAccountSolrComponent.index(account);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (newCreated) {
            EdsCompany company;

            if (user == null) {
                company = companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
            } else {
                company = user.getCompany();
            }

        }
    }

    private EdsCrmCustomFields saveCustomFields(EdsCrmCustomFields edsCrmCustomField, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (edsCrmCustomField == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsCrmCustomField = new EdsCrmCustomFields();
                crmCustomFieldsManager.create(edsCrmCustomField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsCrmCustomField, customFieldItems);
            return edsCrmCustomField;
        }
        return null;
    }

    private EdsItemCustomFields saveItemCustomFields(EdsItemCustomFields edsItemCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (edsItemCustomFields == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsItemCustomFields = new EdsItemCustomFields();
                itemCFManager.create(edsItemCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsItemCustomFields, customFieldItems);
            return edsItemCustomFields;
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CrmAccountItem getAccount(Integer objectId, String entityType) {
        EdsCrmAccount account = null;
        if (objectId != null) {
            account = crmAccountManager.get(objectId);
        }
        if (account != null) {
            account.setEntityType(entityType);
        }
        return getAccount(account, false);
    }

    /**
     * <h1>...THIS IS METHOD IN DATABASE MOVE TO SOLR ...</h1>
     * <br/>
     * <h2>... UPDATED DATE {21:34  30/04/2011} ...</h2>
     * <br/>
     * <h3>... CHANGED DEVELOPER DILSHOD.T ...</h3>
     * <br/>
     *
     * @param fp
     * @return CASE LIST
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CaseList getCases(ListingFilterParameter fp) {
        FacetFilterRpc caseFilter = fp.getFacetFilter();
        StringBuilder caseSolrQuery = getCasesFilter(fp);
        CaseList caseList = new CaseList();
        if (fp.isForExportOnly()) {
            caseList.setList(new ArrayList<>());
            int totalLength = fp.getLimit();
            fp.setStart(0);
            fp.setLimit(200);
            while (totalLength > fp.getStart()) {
                CaseList casePeiceList = getCrmCaseList(fp, caseSolrQuery.toString());
                caseList.getList().addAll(casePeiceList.getList());
                caseList.setTotal(casePeiceList.getTotal());
                fp.setStart(fp.getStart() + fp.getLimit());
            }
        } else {
            caseList = getCrmCaseList(fp, caseSolrQuery.toString());
        }
        // this condition working only for trash cases
        if (caseFilter != null && caseFilter.getCustomData().containsKey(CaseItem.IN_TRASH)) {
            if (Boolean.valueOf(caseFilter.getCustomData().get(CaseItem.IN_TRASH))) {
                caseList.setTrash(true);
            }
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCase.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get case list");
        return caseList;
    }


    private StringBuilder getCasesFilter(ListingFilterParameter fp) {
        FacetFilterRpc caseFilter = fp.getFacetFilter();
        if (caseFilter != null && !caseFilter.isFilterChanges()) {
            caseFilter = commonServiceLocal.getUserFacetFilter(caseFilter);
        }
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        StringBuilder caseSolrQuery = new StringBuilder();// generate solr query
        caseSolrQuery.append(commonServiceLocal.getCrmCaseSolrQuery(fp, company, caseFilter));
        caseSolrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(caseFilter, company, null, null));
        if (user.isClientContact() && /*fp.hasOnlyClientAccess()*/ServerUtils.hasPermission(PermissionConstants.CRM_CASES_LIST)) {
            EdsCrmContact contact = user.getClientContact().getCrmContact();
            if (contact != null) {
                caseSolrQuery.append(" AND (").append(SolrCaseRepresenter.RELEATED_TO_ID).append(":(").append(contact.getObjectID()).append(")");
                if (contact.getCrmAccount() != null) {
                    caseSolrQuery.append(" OR ").append(SolrCaseRepresenter.ACCOUNT_ID).append(":(").append(contact.getCrmAccount().getObjectID()).append(")");
                }
                caseSolrQuery.append(")");
            }
        }
        if (user.isEmployee() && !(user.hasEitherRoles(EdsRole.ADMIN, EdsRole.DR, EdsRole.SALESMAN, EdsRole.CUSTOMER_SERVICE_MANAGER) || ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_CASES_LIST))) { //!user.hasEitherRoles(EdsRole.ADMIN, EdsRole.DR, EdsRole.SALESMAN, EdsRole.CUSTOMER_SERVICE_MANAGER)) {
            caseSolrQuery.append(" AND (");
            caseSolrQuery.append(SolrCaseRepresenter.CASE_ASSIGNEE_ID).append(":").append(user.getObjectID());
            caseSolrQuery.append(" OR ").append(SolrCaseRepresenter.RESOLVER_ID).append(":").append(user.getObjectID());
            EdsEmployee employee = employeeManager.get(user.getObjectID());
            if (employee != null && employee.getTeam() != null) {
                caseSolrQuery.append(" OR (").append(SolrCaseRepresenter.CASE_DEPARTMENT_ID).append(":").append(employee.getTeam().getObjectID());
                caseSolrQuery.append(" AND (-").append(SolrCaseRepresenter.CASE_ASSIGNEE_ID).append(":[* TO *] AND *:*))");
            }
            caseSolrQuery.append(" )");
        }
        if (fp.getRelationID() != null && fp.getRelationType() != null) {
            StringBuilder operator = new StringBuilder();
            if (fp.getAccountID() != null) {
                operator.append(" AND (").append(SolrCaseRepresenter.ACCOUNT_ID).append(":").append(fp.getAccountID()).append(" OR ");
            } else if (fp.getCrmContactId() != null) {
                operator.append(" AND (").append(SolrCaseRepresenter.RELEATED_TO_ID).append(":").append(fp.getCrmContactId()).append(" OR ");
            } else if (fp.getOpportunityID() != null) {
                operator.append(" AND (").append(SolrCaseRepresenter.OPPORTUNITY_ID).append(":").append(fp.getOpportunityID()).append(" OR ");
            } else if (fp.getLeadID() != null) {
                operator.append(" AND (").append(SolrCaseRepresenter.LEAD_ID).append(":").append(fp.getLeadID()).append(" OR ");
            } else {
                operator.append(" AND (");
            }
            List<Integer> caseIDs = relationManager.getRelationIDsByType(fp.getRelationID(), fp.getEntityID(), fp.getRelationType(), RelationItem.TYPE_CASE);
            caseSolrQuery.append(operator).append(SolrCaseRepresenter.CASE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(caseIDs, "0", " ")).append("))");
        }
        return caseSolrQuery;
    }

    /**
     * <h1>...THIS IS METHOD READ ALL DATA IN SOLR REPOSITORY ...</h1>
     * <br/>
     * }
     * }
     * if (isFromApi) {
     * query.setParam(GroupParams.GROUP, true);
     * query.setParam(GroupParams.GROUP_LIMIT, fp.getLimit().toString());
     * query.setParam(GroupParams.GROUP_TOTAL_COUNT, true);
     * query.setParam(GroupParams.GROUP_FIELD, SolrCaseRepresenter.STATUS_NAME);
     * }
     * return query;
     * }
     *
     * @param fp
     * @param caseSolrQuery
     * @return
     */
    private CaseList getCrmCaseList(ListingFilterParameter fp, String caseSolrQuery) {
        Page<CaseSolrDoc> caseSolrDocPage = caseSolrComponent.getList(fp, caseSolrQuery, false);
        return getCrmCaseFromSolrResult(caseSolrDocPage, fp);
    }

    /**
     * <h1>... BUILD CASE SOLRQUERY ...</h1>
     * <br/>
     * <h2>... CREATE DATE {18:33  06/05/2011} ...</h2>
     * <br/>
     * <h3>... WRITER DEVELOPER DILSHOD.T ...</h3>
     * <br/>
     *
     * @param fp
     * @param solrQuery
     * @param isGroup
     * @return query
     */
    private SolrQuery getCrmCaseSolrQuery(ListingFilterParameter fp, String solrQuery, boolean isGroup) {
        SolrQuery query = new SolrQuery();
        StringBuilder caseSolrQuery = new StringBuilder(solrQuery);
        EdsUser user = caseManager.getUser();
        if (!ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_CASES_LIST) && !user.isClientContact()) {
            caseSolrQuery.append(" AND (");
            caseSolrQuery.append(SolrCaseRepresenter.CASE_ASSIGNEE_ID).append(":").append(user.getObjectID());
            caseSolrQuery.append(" OR ").append(SolrCaseRepresenter.RESOLVER_ID).append(":").append(user.getObjectID());
            EdsEmployee employee = employeeManager.get(user.getObjectID());
            if (employee != null && employee.getTeam() != null) {
                caseSolrQuery.append(" OR (").append(SolrCaseRepresenter.CASE_DEPARTMENT_ID).append(":").append(employee.getTeam().getObjectID());
                caseSolrQuery.append(" AND (-").append(SolrCaseRepresenter.CASE_ASSIGNEE_ID).append(":[* TO *] AND *:*))");
            }
            caseSolrQuery.append(" )");
        }
        query.setQuery(caseSolrQuery.toString());
        query.setStart(fp.getStart());
        query.setRows(fp.getLimit());

        if (!fp.isSearchButton()) {
            if (StringUtils.isNotEmpty(fp.getSortField())) {
                String solrField = SolrCaseRepresenter.LAST_UPDATE_DATE;
                if (CaseItem.CASE_ID.equals(fp.getSortField())) {
                    solrField = SolrCaseRepresenter.CASE_ID;
                } else if (CaseItem.SUBJECT.equals(fp.getSortField())) {
                    solrField = SolrCaseRepresenter.SORTABLE_CASE_SUBJECT;
                } else if (CaseItem.PRIORITY.equals(fp.getSortField())) {
                    solrField = SolrCaseRepresenter.PRIORITY_SORDER;
                } else if (CaseItem.REPORTED_BY.equals(fp.getSortField())) {
                    solrField = SolrCaseRepresenter.SORTABLE_REPORTED_BY;
                } else if (CaseItem.CREATED_DATE.equals(fp.getSortField())) {
                    solrField = SolrCaseRepresenter.CREATE_DATE;
                } else if (CaseItem.LAST_UPDATED_DATE.equals(fp.getSortField())) {
                    solrField = SolrCaseRepresenter.LAST_UPDATE_DATE;
                } else if (CaseItem.ASSIGNED_TO.equals(fp.getSortField())) {
                    solrField = SolrCaseRepresenter.SORTABLE_CASE_ASSIGNEE;
                } else if (CaseItem.STATUS.equals(fp.getSortField())) {
                    solrField = SolrCaseRepresenter.STATUS_SORDER;
                } else if (CaseItem.INTERNAL_STATUS.equals(fp.getSortField())) {
                    solrField = SolrCaseRepresenter.INTERNAL_STATUS_SORDER;
                } else if (CaseItem.INTERNAL_UPDATED_DATE.equals(fp.getSortField())) {
                    solrField = SolrCaseRepresenter.INTERNAL_UPDATED_DATE;
                } else if (CaseItem.KANBAN_ORDER.equals(fp.getSortField())) {
                    solrField = SolrCaseRepresenter.KANBAN_ORDER;
                } else {
                    solrField = SolrCaseRepresenter.LAST_UPDATE_DATE;
                }
                query.setSort(solrField, fp.isAscending() ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc);
                CustomFieldsUtils.setCustomFieldsSortableNameToSolr(fp.getSortField(), !fp.isAscending(), query, true);
            } else {
                query.setSort(SolrCaseRepresenter.LAST_UPDATE_DATE, SolrQuery.ORDER.desc);
            }
        }
        if (isGroup) {
            query.setParam(GroupParams.GROUP, true);
            query.setParam(GroupParams.GROUP_LIMIT, fp.getLimit().toString());
            query.setParam(GroupParams.GROUP_FIELD, SolrCaseRepresenter.STATUS_ID);
        }
        return query;
    }

    /**
     * <h1>... GET DATA IN SOLR AND SET TO CASEITEM OBJECT  ...</h1>
     * <br/>
     * <h2>... CREATE DATE {21:34  30/04/2011} ...</h2>
     * <br/>
     * <h3>... WRITER DEVELOPER DILSHOD.T ...</h3>
     * <br/>
     *
     * @param caseSolrDocPage
     * @param fp
     * @return
     */
    private CaseList getCrmCaseFromSolrResult(Page<CaseSolrDoc> caseSolrDocPage, ListingFilterParameter fp) {
        SelectItem[] statuses = null;
        Map<Integer, SelectItem> statusesMap = null;
        SelectItem[] priorities = null;
        SelectItem[] caseReasons = null;
        SelectItem[] types = null;
        SelectItem[] internalStatuses = null;
        if (!fp.isForExportOnly()) {// this is condation working only for listes not working for excel&pdf
            statuses = getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_STATUS), ServerUtils.REFERENCE);
            statusesMap = SelectItem.asMap(statuses);
            priorities = getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_PRIORITY), ServerUtils.REFERENCE);
            types = getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_TYPE), ServerUtils.REFERENCE);
            caseReasons = getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_REASON), ServerUtils.REFERENCE);
            internalStatuses = getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_INTERNAL_STATUS), ServerUtils.REFERENCE);
        }
        int total = (int) caseSolrDocPage.getTotalElements();
        ArrayList<CaseItem> caseListItems = new ArrayList<>();
        for (CaseSolrDoc doc : caseSolrDocPage.getContent()) {
            caseListItems.add(getRPCFromCaseSolrDoc(doc, statusesMap, fp, statuses, priorities, caseReasons, types, internalStatuses));
        }
        return new CaseList(caseListItems, total);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CaseItem getRPCFromCaseSolrDoc(CaseSolrDoc caseSolrDoc, Map<Integer, SelectItem> statusesMap, ListingFilterParameter fp, SelectItem[] statuses,
                                          SelectItem[] priorities, SelectItem[] caseReasons, SelectItem[] types, SelectItem[] internalStatuses) {
        CaseItem rpc = new CaseItem();
        rpc.setObjectId(caseSolrDoc.getCaseId());
        rpc.setCaseNumber(caseSolrDoc.getCaseNumber());
        rpc.setSubject(caseSolrDoc.getCaseSubject());
        rpc.setPriority(referenceWfmMessageSource.localize(caseSolrDoc.getPriorityCode(), caseSolrDoc.getPriorityName(), ServerUtils.getUserLocale()));
        rpc.setPriorityId(caseSolrDoc.getPriorityId());
        rpc.setType(referenceWfmMessageSource.localize(caseSolrDoc.getCaseTypeCode(), caseSolrDoc.getCaseTypeName()));
        rpc.setTypeId(caseSolrDoc.getCaseTypeId());
        rpc.setCreatedDate(caseSolrDoc.getCreateDate());
        rpc.setLastUpdatedDate(caseSolrDoc.getLastUpdatedDate());
        rpc.setCaseAssigneeName(caseSolrDoc.getCaseAssignee());
        rpc.setCaseAssigneeId(caseSolrDoc.getCaseAssigneeId());
        rpc.setResolverId(caseSolrDoc.getResolverId());
        rpc.setResolverName(caseSolrDoc.getResolverName());
        rpc.setDepartment(caseSolrDoc.getCaseDepartment());
        rpc.setDepartmentID(caseSolrDoc.getCaseDepartmentId());
        Integer statusID = caseSolrDoc.getStatusId();
        String statusCode = caseSolrDoc.getStatusCode();
        rpc.setStatusCode(statusCode);
        if (statusCode != null && !EdsCase.CLOSED.equals(statusCode) && !EdsCase.RESOLVED.equals(statusCode)) {
            rpc.setTimerIsStarted(clockManager.getActiveClockForCurrentUser(rpc.getObjectId(), CRM_CASE, employeeManager.getUser().getObjectID()) != null);
        }

        rpc.setCaseOrigin(referenceWfmMessageSource.localize(caseSolrDoc.getCaseOriginCode(), caseSolrDoc.getCaseOriginName(), ServerUtils.getUserLocale()));
        rpc.setCaseReason(caseSolrDoc.getCaseReasonName());
        rpc.setCaseReasonId(caseSolrDoc.getCaseReasonId());
        if (statusID != null) {
            rpc.setStatus(new ReferenceItem(statusID, referenceWfmMessageSource.localize(caseSolrDoc.getStatusCode(), caseSolrDoc.getStatusName(), ServerUtils.getUserLocale()), null));
            if (statusesMap != null && statusesMap.containsKey(statusID) && statusesMap.get(statusID) != null) {
                rpc.getStatus().setCssStyle(((ReferenceItem) statusesMap.get(statusID)).getCssStyle());
                rpc.getStatus().setAntonym(((ReferenceItem) statusesMap.get(statusID)).getAntonym());
                rpc.getStatus().setSelected(statusesMap.get(statusID).isSelected());
            }
        }
        rpc.setReportedBy(caseSolrDoc.getReportedBy());
        rpc.setReportedByName(rpc.getReportedBy());
        if (!fp.isForExportOnly()) {
            rpc.setHasAttachments(caseSolrDoc.getHasAttachment());
            rpc.setStatusItems(statuses);
            rpc.setPriorities(priorities);
            rpc.setTypes(types);
            rpc.setCaseReasons(caseReasons);
            rpc.setInternalStatusItems(internalStatuses);
        }
        if (fp.getListPanelTool() != null) {
            rpc.setCustomFieldsMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(caseSolrDoc, fp.getListPanelTool().getColumnCodeName()));
        }
        rpc.setRelationValueMap(SolrRelationUtils.getBaseSolrDocValue(caseSolrDoc, EdsRelation.TYPE_CASE));
        rpc.setInternalStatusName(caseSolrDoc.getInternalStatusName());
        rpc.setInternalStatusId(caseSolrDoc.getInternalStatusId());
        rpc.setInternalUpdatedDate(caseSolrDoc.getInternalUpdatedDate());
        rpc.setTrackerID(caseSolrDoc.getCaseTrackerId());

        EdsCase ceseItem = caseManager.get(rpc.getObjectId());
        if (ceseItem != null) {
            rpc.setReportedByCompanyName(ceseItem.getReportedByCompany());
            rpc.setReportedBy(ceseItem.getReportedBy());
            rpc.setReportedByName(ceseItem.getReportedBy());
        }

        boolean settingsEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_PRODUCT_DETAILS_TO_CRM);
        if (settingsEnabled) {
            if (ceseItem.getBrandId() != null) {
                EdsBrand brand = brandManager.get(ceseItem.getBrandId());
                if (brand != null) {
                    rpc.setBrand(new SelectItem(brand.getObjectID(), brand.getName()));
                }
            }
            if (ceseItem.getProductCategoryId() != null) {
                EdsProductCategory productCategory = productCategoryManager.get(ceseItem.getProductCategoryId());
                if (productCategory != null) {
                    rpc.setProductCategory(new SelectItem(productCategory.getObjectID(), productCategory.getName()));
                }
            }
            if (ceseItem.getProductId() != null) {
                EdsItem product = itemManager.get(ceseItem.getProductId());
                if (product != null) {
                    rpc.setProduct(new SelectItem(product.getObjectID(), product.getName()));
                }
            }
        }
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setRelationID(rpc.getObjectId());
        filterParameter.setRelationType(RelationItem.TYPE_CASE);
        TaskList taskList = taskServiceLocal.getTaskList(filterParameter);
        List<TaskListItem> taskLists = taskList.getList();
        rpc.setTasks(taskLists);
        return rpc;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Email getCaseEmail(String emailID, Integer trackerID) {
//        boolean convertEachCase = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CONVERT_EACH_EMAIL_TO_CASE);
        Email email = null;
        Optional<EdsEmail> caseEmail = Optional.empty();
        if (emailID != null) {
            caseEmail = emailRepository.findById(emailID);
        }
        if (caseEmail.isPresent() && caseEmail.isEmpty() && trackerID != null) {
            caseEmail = Optional.of(emailRepository.findLastByTrackerId(trackerID));
        }
        if (caseEmail != null && caseEmail.isPresent()) {
            caseEmail.get().setRead(false);

            if (caseEmail.get().getDescription() == null) {
                email = mailServices.getService(caseEmail.get().getEmailSettingId()).getWithContent(caseEmail.get());
            }
            if (email == null || email.isDeleted()) {
                email = caseEmail.get().getRPC();
            }
        }
        return email;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<Email> getCaseEmails(Integer trackerID) {
        ArrayList<Email> caseEmails = new ArrayList<>();
        Email email = null;
        List<EdsEmail> edsEmailList = emailRepository.findByTrackerIdAndCompanyId(trackerID, SecurityContext.getInstance().getCompanyId());
        for (EdsEmail edsEmail : edsEmailList) {
            edsEmail.setRead(false);

            if (edsEmail.getDescription() == null) {
                email = mailServices.getService(edsEmail.getEmailSettingId()).getWithContent(edsEmail);
            }

            if (email == null || email.isDeleted()) {
                email = edsEmail.getRPC();
            }

            caseEmails.add(email);
        }
        return caseEmails;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer saveCallLog(Appointment appointment) {
        SelectItem item = googleCalendarServiceLocal.saveCalendarEvent(caseManager.getUser().getObjectID(), appointment, false);
        return item != null ? item.getId() : null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CaseItem getCase(Integer objectId, boolean fromSummary) {
        EdsCase crmCase = caseManager.get(objectId);
        if (crmCase == null) {
            log.error("Getting case by id is null. Case id is " + objectId);
            return new CaseItem();
        }
        return getCase(crmCase, fromSummary);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CaseList getCaseChangeHistory(Integer caseId) {
        EdsCase crmCase = caseManager.get(caseId);
        ArrayList<CaseItem> caseItems = new ArrayList<>();
        if (crmCase != null) {
            for (EdsCase item : crmCase.getSubCases()) {
                CaseItem caseItem = new CaseItem();
                caseItem.setAuditInfoResource(item.getAuditInfo() != null ? item.getAuditInfo().getDTO() : null);
                caseItem.setCreatedDate(item.getHistoricalParent().getAuditInfo().getCreationDate());
                caseItem.setStatus(item.getStatus() != null ? item.getStatus().getRPC() : new ReferenceItem());
                caseItem.setStatusChangedNote(item.getNote() != null ? item.getNote() : "");
                if (caseItem.getStatus().getId() != null) {
                    caseItem.getStatus().setName(referenceWfmMessageSource.localizeRef(item.getStatus()));
                }
                caseItems.add(caseItem);
            }
        }
        return new CaseList(caseItems, caseItems.size());
    }


    @Override
    public FileResource[] getTrackerAttachments(Integer trackerID) {
        if (trackerID != null) {
            List<EdsEmailAttachment> attachments = emailAttachmentManager.getTrackerAttachments(trackerID);
            if (attachments != null && attachments.size() > 0) {
                return EdsEmailAttachment.asFileResourses(attachments).toArray(new FileResource[]{});
            }
        }
        return new FileResource[0];
    }

    @Override
    public void saveLeadCampaign(ArrayList<Integer> leadIDs, Integer campaignId, String type) {
        if (leadIDs != null && !leadIDs.isEmpty() && campaignId != null) {
            EdsUser user = userManager.getUser();
            switch (type) {
                case RelationItem.TYPE_LEAD -> {
                    crmContactManager.changeCampaign(campaignId, leadIDs, user.getObjectID());
                    List<EdsCrmContact> leads = crmContactManager.getLeadsByIDs(leadIDs);
                    if (!leads.isEmpty()) {
                        try {
                            contactSolrComponent.indexes(leads);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                case RelationItem.TYPE_CONTACT -> {
                    crmContactManager.changeCampaign(campaignId, leadIDs, user.getObjectID());
                    List<EdsCrmContact> leads = crmContactManager.getContactsByIDs(leadIDs);
                    if (!leads.isEmpty()) {
                        try {
                            contactSolrComponent.indexes(leads);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                default -> {
                    opportunityManager.changeOpportunity(campaignId, leadIDs, user.getObjectID());
                    String listOpportunities = ServerUtils.getAsCommoDelimited(leadIDs, "0");
                    List<EdsOpportunity> opportunities = opportunityManager.getOpportunityByIds(listOpportunities);
                    if (!opportunities.isEmpty()) {
                        try {
                            opportunitySolrComponent.indexes(opportunities);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem getCrmEntityAsSelectItem(String crmEntity, Integer objectId) {
        if (objectId != null) {
            if (CrmConstants.CRM_ACCOUNT.equals(crmEntity)) {
                EdsCrmAccount account = crmAccountManager.get(objectId);
                if (account != null) {
                    return account.getAsSelectItem();
                }
            }
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CaseItem editCase(Integer objectId, String formType, Integer convertFormId) {
        return editCase(objectId, formType, convertFormId, false);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CaseItem editCase(Integer objectId, String formType, Integer convertFormId, boolean fromUI) {
        CaseItem item = new CaseItem();
        if (objectId == null) {
            String[] ids = emailFilterManager.getDefaultSelections(EmailFilter.CREATE_CASE, EmailFilter.ID_ASSIGNEE, EmailFilter.ID_RESOLVER);
            Integer assigneeID = ids != null && ids.length > 0 && ids[0] != null && ids[0].matches(Constants.REGEX_INTEGER) ? Integer.valueOf(ids[0]) : null;
            Integer resolverID = ids != null && ids.length > 1 && ids[1] != null && ids[1].matches(Constants.REGEX_INTEGER) ? Integer.valueOf(ids[1]) : null;
            if (assigneeID != null) {
                EdsUser assignee = userManager.get(assigneeID);
                if (assignee != null) {
                    item.setCaseAssigneeName(assignee.getName());
                    item.setCaseAssigneeId(assigneeID);
                }
            }
            if (resolverID != null) {
                EdsUser resolver = userManager.get(resolverID);
                if (resolver != null) {
                    item.setResolverName(resolver.getName());
                    item.setResolverId(resolverID);
                }
            }
        }
        item.setTypes(getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_TYPE), ServerUtils.REFERENCE));
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setAllByFilter(true);
        List<EdsReference> caseOriginsList = referenceManager.listReferences(EdsCase._CASE_ORIGIN);
        ArrayList<EdsReference> listOfObject = new ArrayList<>(caseOriginsList);

        item.setCaseOrigins(getAsSelectItem(listOfObject, ServerUtils.REFERENCE));
        if (item.getCaseOrigins() != null) {
            item.getCaseOrigins();
            for (SelectItem caseOrigin : item.getCaseOrigins()) {
                if (caseOrigin.getDescription() != null && EdsCase.WEB.equals(caseOrigin.getDescription())) {
                    item.setCaseOriginId(caseOrigin.getId());
                    item.setCaseOriginCode(caseOrigin.getDescription());
                    item.setCaseOrigin(caseOrigin.getName());
                }
            }
        }
        item.setStatusItems(getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_STATUS), ServerUtils.REFERENCE));
        for (SelectItem reference : item.getStatusItems()) {
            if (objectId == null && EdsCase.NEW.equals(reference.getCode())) {
                item.setStatus( new SelectItem(reference.getId(), reference.getName()));
            }
        }
        item.setPriorities(getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_PRIORITY), ServerUtils.REFERENCE));
        for (SelectItem reference : item.getPriorities()) {
            if (objectId == null && EdsCase.CP_MEDIUM.equals(reference.getCode())) {
                item.setPriorityId(reference.getId());
            }
        }

        item.setCaseReasons(getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_REASON), ServerUtils.REFERENCE));
        EdsCase crmCase = new EdsCase();
        if (objectId != null) {
            crmCase = caseManager.get(objectId);
            if (crmCase != null) {
                item = crmCase.getRPC(item, false);
                item.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_CASE, crmCase.getObjectID())));
                item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(crmCase.getCustomFields(), commonService.getCompanyCustomFields(ViewName.CrmCase)));
                item.setAnonim(crmCase.IsUserFeedBackAnonim());

                if (fromUI) {
                    item.setLastEmail(getCaseEmail(crmCase.getEmailID(), crmCase.getTracker() != null ? crmCase.getTracker().getObjectID() : null));
                }

                //for the account tree
                if (item.getAccountId() != null) {
                    SelectItem[] parentAccountTreeList = allInOneServiceLocal.getParentAccountsTreeList(item.getAccountId());

                    if (parentAccountTreeList != null && parentAccountTreeList.length > 0) {
                        SelectItem[] treeitems = new SelectItem[parentAccountTreeList.length + 1];

                        int index = 0;
                        for (SelectItem titem : parentAccountTreeList) {
                            treeitems[index] = titem;
                            index++;
                        }

                        treeitems[index] = new SelectItem(item.getAccountId(), item.getAccountName());
                        item.setAccounts(treeitems);
                    } else {
                        item.setAccounts(new SelectItem[]{new SelectItem(item.getAccountId(), item.getAccountName())});
                    }
                }
            }
        }

        List<EdsReference> internalStatus = referenceManager.listReferences(EdsCase._CASE_INTERNAL_STATUS);
        List<SelectItem> internalStatusesList = Lists.newArrayList();
        internalStatus.forEach(edsReference -> internalStatusesList.add(new SelectItem(edsReference.getObjectID(),
                referenceWfmMessageSource.localize(edsReference.getCode(), edsReference.getName()),
                edsReference.getDescription())));
        item.setInternalStatusItems(internalStatusesList.toArray(SelectItem[]::new));

        EdsUser user = userManager.getUser();
        if (item.getCaseAssigneeId() == null && item.getDepartmentID() == null && user != null && !user.hasEitherRoles(EdsRole.ADMIN, EdsRole.DR, EdsRole.SALESMAN)) {
            item.setCaseAssigneeId(user.getObjectID());
            item.setCaseAssigneeName(user.getName());
        }
        if (item.getBrandId() != null) {
            EdsBrand brand = brandManager.get(item.getBrandId());
            if (brand != null) {
                item.setBrand(new SelectItem(brand.getObjectID(), brand.getName()));
            }
        }
        if (item.getProductCategoryId() != null) {
            EdsProductCategory productCategory = productCategoryManager.get(item.getProductCategoryId());
            if (productCategory != null) {
                item.setProductCategory(new SelectItem(productCategory.getObjectID(), productCategory.getName()));
            }
        }
        if (item.getProductId() != null) {
            EdsItem product = itemManager.get(item.getProductId());
            if (product != null) {
                item.setProduct(new SelectItem(product.getObjectID(), product.getName()));
            }
        }

        if (formType != null && convertFormId != null) {
            item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, commonService.getCompanyCustomFields(ViewName.CrmCase)));
            if (RelationItem.TYPE_PURCHASE_ORDER.equals(formType)) {
                Params params = new Params();
                params.setObjectID(convertFormId);
                params.setType(PAYABLE);
                params.setFormType(PURCHASE_ORDER);

                EdsFormProperty formProperties = formPropertyManager.getByFormID(LayoutRPC.CASE_FORM);

                Gson gson = new Gson();
                FormProperty[] fields = gson.fromJson(formProperties.getSettingsJSONData(), FormProperty[].class);

                NewInvoice newInvoice = quoteService.getAllQuoteData(params);
                item.setFromName(newInvoice.getInvoiceNumber() != null ? newInvoice.getInvoiceNumber() : "");
                item.setConvertedRelations(newInvoice.getRelations());
                if (newInvoice.getTypeItem() != null) {
                    item.setAccountId(newInvoice.getTypeItem().getId());
                    item.setAccountName(newInvoice.getTypeItem().getName());
                }
                if (newInvoice != null && newInvoice.getCustomFieldItems() != null) {
                    for (CompanyCustomFieldItem companyCustomFieldItem : newInvoice.getCustomFieldItems()) {
                        convertCaseFormCF(item, fields, companyCustomFieldItem);
                    }
                }
                if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
                    for (CompanyCustomFieldItem caseCustomFields : item.getCustomFields()) {
                        convertQuoteFieldsToCaseCF(caseCustomFields, newInvoice);
                    }
                }

            } else if (RelationItem.TYPE_SALEORDER.equals(formType)) {
                Params params = new Params();
                params.setObjectID(convertFormId);
                params.setType(RECEIVABLE);
                params.setFormType(SALE_ORDER);

                EdsFormProperty formProperties = formPropertyManager.getByFormID(LayoutRPC.CASE_FORM);

                Gson gson = new Gson();
                FormProperty[] fields = gson.fromJson(formProperties.getSettingsJSONData(), FormProperty[].class);

                NewInvoice newInvoice = quoteService.getAllQuoteData(params);
                item.setFromName(newInvoice.getInvoiceNumber() != null ? newInvoice.getInvoiceNumber() : "");
                item.setConvertedRelations(newInvoice.getRelations());
                if (newInvoice.getTypeItem() != null) {
                    item.setAccountId(newInvoice.getTypeItem().getId());
                    item.setAccountName(newInvoice.getTypeItem().getName());
                }
                if (newInvoice != null && newInvoice.getCustomFieldItems() != null) {
                    for (CompanyCustomFieldItem companyCustomFieldItem : newInvoice.getCustomFieldItems()) {
                        convertCaseFormCF(item, fields, companyCustomFieldItem);
                    }
                }

                if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
                    for (CompanyCustomFieldItem caseCustomFields : item.getCustomFields()) {
                        convertQuoteFieldsToCaseCF(caseCustomFields, newInvoice);
                    }
                }

            } else if (RelationItem.TYPE_SALEQUOTE.equals(formType)) {
                Params params = new Params();
                params.setObjectID(convertFormId);
                params.setType(RECEIVABLE);
                params.setFormType(SALE_QUOTE);

                EdsFormProperty formProperties = formPropertyManager.getByFormID(LayoutRPC.CASE_FORM);

                Gson gson = new Gson();
                FormProperty[] fields = gson.fromJson(formProperties.getSettingsJSONData(), FormProperty[].class);

                NewInvoice newInvoice = quoteService.getAllQuoteData(params);
                item.setFromName(newInvoice.getInvoiceNumber() != null ? newInvoice.getInvoiceNumber() : "");
                item.setConvertedRelations(newInvoice.getRelations());
                if (newInvoice.getTypeItem() != null) {
                    item.setAccountId(newInvoice.getTypeItem().getId());
                    item.setAccountName(newInvoice.getTypeItem().getName());
                }
                if (newInvoice != null && newInvoice.getCustomFieldItems() != null) {
                    for (CompanyCustomFieldItem companyCustomFieldItem : newInvoice.getCustomFieldItems()) {
                        convertCaseFormCF(item, fields, companyCustomFieldItem);
                    }
                }

                if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
                    for (CompanyCustomFieldItem caseCustomFields : item.getCustomFields()) {
                        convertQuoteFieldsToCaseCF(caseCustomFields, newInvoice);
                    }
                }
            } else if (CrmConstants.CRM_EVENT_CALLOG.equals(formType)) {

                EdsFormProperty formProperties = formPropertyManager.getByFormID(LayoutRPC.LOGACALL_FORM);

                Gson gson = new Gson();
                FormProperty[] fields = gson.fromJson(formProperties.getSettingsJSONData(), FormProperty[].class);

                Appointment appointment = googleCalendarService.getAppointment(convertFormId, false);
                if (appointment != null) {
                    item.setFromName(appointment.getSubject());
                    item.setConvertedRelations(appointment.getRelations());
                    item.setSubject(appointment.getSubject());
                    item.setDescription(appointment.getDescription());

                    if (appointment.getCustomFieldItems() != null) {
                        for (CompanyCustomFieldItem companyCustomFieldItem : appointment.getCustomFieldItems()) {
                            convertCaseFormCF(item, fields, companyCustomFieldItem);
                        }
                    }
                }
            }
        }
        return item;
    }

    private void convertQuoteFieldsToCaseCF(CompanyCustomFieldItem companyCustomFieldItem, NewInvoice newInvoice) {
        switch (companyCustomFieldItem.getAliasName()) {
            case "CUSTOMER" -> {
                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.CUSTOMER.equals(companyCustomFieldItem.getLookUpTypeEnum()) && newInvoice.getTypeItem() != null) {
                    companyCustomFieldItem.setSelectedId(newInvoice.getTypeItem().getId());
                    companyCustomFieldItem.setFieldStringValue(newInvoice.getTypeItem().getName());
                }
            }
            case "DATE" -> {
                if (DATA_TYPE_DATE.equals(companyCustomFieldItem.getDataType()) && newInvoice.getInvoiceDate() != null) {
                    companyCustomFieldItem.setFieldDateNonConvertedValue(newInvoice.getInvoiceDate());
                }
            }
            case "DUE_DATE" -> {
                if (DATA_TYPE_DATE.equals(companyCustomFieldItem.getDataType()) && newInvoice.getDueDate() != null) {
                    companyCustomFieldItem.setFieldDateNonConvertedValue(newInvoice.getDueDate());
                }
            }
            case "CANCEL_DATE" -> {
                if (DATA_TYPE_DATE.equals(companyCustomFieldItem.getDataType()) && newInvoice.getCancelDate() != null) {
                    companyCustomFieldItem.setFieldDateNonConvertedValue(newInvoice.getCancelDate());
                }
            }
            case "NUMBER" -> {
                if (UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) && newInvoice.getInvoiceNumber() != null) {
                    companyCustomFieldItem.setFieldStringValue(newInvoice.getInvoiceNumber());
                }
            }
            case "REFERENCE" -> {
                if (UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) && newInvoice.getReference() != null) {
                    companyCustomFieldItem.setFieldStringValue(newInvoice.getReference());
                }
            }
            case "SUPPLIER" -> {
                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.SUPPLIER.equals(companyCustomFieldItem.getLookUpTypeEnum()) && newInvoice.getTypeItem() != null) {
                    companyCustomFieldItem.setSelectedId(newInvoice.getTypeItem().getId());
                    companyCustomFieldItem.setFieldStringValue(newInvoice.getTypeItem().getName());
                }
            }
            case "CUSTOMER_TERMS", "SUPPLIER_TERMS" -> {
                if (Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.TERMS.equals(companyCustomFieldItem.getLookUpTypeEnum()) && newInvoice.getInvoiceTermsItem() != null) {
                    companyCustomFieldItem.setSelectedId(newInvoice.getInvoiceTermsItem().getId());
                    companyCustomFieldItem.setFieldStringValue(newInvoice.getInvoiceTermsItem().getName());
                }
            }
        }
    }

    private void convertCaseFormCF(CaseItem item, FormProperty[] fields, CompanyCustomFieldItem companyCustomFieldItem) {
        for (FormProperty formProperty1 : fields) {
            if (formProperty1 != null) {
                if (companyCustomFieldItem.getAliasName().equals(formProperty1.getAliasName())) {
                    switch (formProperty1.getCode()) {
                        case "SUBJECT" -> {
                            if (UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType())) {
                                item.setSubject(companyCustomFieldItem.getFieldStringValue());
                            }
                        }
                        case "REPORTED_BY" -> {
                            if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.LEAD.equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                item.setLead(companyCustomFieldItem.getFieldStringValue());
                                item.setLeadId(companyCustomFieldItem.getSelectedId());
                            } else if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && (CustomFieldLookUpTypeEnum.CUSTOMER.equals(companyCustomFieldItem.getLookUpTypeEnum()) || CustomFieldLookUpTypeEnum.SUPPLIER.equals(companyCustomFieldItem.getLookUpTypeEnum()))) {
                                item.setAccountName(companyCustomFieldItem.getFieldStringValue());
                                item.setAccountId(companyCustomFieldItem.getSelectedId());
                            } else if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.CONTACT.equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                item.setCrmContact(companyCustomFieldItem.getFieldStringValue());
                                item.setCrmContactID(companyCustomFieldItem.getSelectedId());
                            }
                        }
                        case "CASE_DESCRIPTION" -> {
                            if (UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType())) {
                                item.setDescription(companyCustomFieldItem.getFieldStringValue());
                            }
                        }
                        case "ASSIGNEE" -> {
                            if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.EMPLOYEE.equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                item.setCaseAssigneeId(companyCustomFieldItem.getSelectedId());
                                item.setCaseAssigneeName(companyCustomFieldItem.getFieldStringValue());
                            }
                        }
                        case "RESOLVER" -> {
                            if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.EMPLOYEE.equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                item.setResolverId(companyCustomFieldItem.getSelectedId());
                                item.setResolverName(companyCustomFieldItem.getFieldStringValue());
                            }
                        }
                    }
                }
            }
        }

        if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
            for (CompanyCustomFieldItem caseCustomFields : item.getCustomFields()) {
                if (companyCustomFieldItem.getAliasName().equals(caseCustomFields.getAliasName()) && companyCustomFieldItem.getUiType().equals(caseCustomFields.getUiType()) && companyCustomFieldItem.getDataType().equals(caseCustomFields.getDataType())) {
                    if (UI_TYPE_LOOKUP.equals(caseCustomFields.getUiType())) {
                        if (caseCustomFields.getLookUpTypeEnum().equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                            caseCustomFields.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                            caseCustomFields.setSelectedId(companyCustomFieldItem.getSelectedId());
                            caseCustomFields.setItem(companyCustomFieldItem.getItem());
                        }
                    } else {
                        caseCustomFields.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                        caseCustomFields.setSelectedId(companyCustomFieldItem.getSelectedId());
                        caseCustomFields.setItem(companyCustomFieldItem.getItem());
                        caseCustomFields.setFieldDateNonConvertedValue(companyCustomFieldItem.getFieldDateNonConvertedValue());
                    }
                }
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<SolutionItem> getSolutionList(ListingFilterParameter fp) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSolution.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get Solution list");
        if (fp.getAccountID() != null) {
            List<EdsCaseSolution> solutionList = caseSolutionManager.getCaseSolutions(fp);
            ArrayList<SolutionItem> results = new ArrayList<>();
            for (EdsCaseSolution cs : solutionList) {
                results.add(cs.getSolution().getRPC(null));
            }
            return new ListResult<>(results, solutionList.size());
        } else {
            ArrayList<SolutionItem> results = solutionManager.getList(fp).stream().map(l -> l.getRPC(null)).collect(Collectors.toCollection(ArrayList::new));
            Integer totalCount = solutionManager.getListCount(fp);
            return new ListResult<>(results, totalCount);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SolutionItem getSolution(Integer objectId) {
        SolutionItem item = new SolutionItem();
        EdsSolution solution = objectId != null ? solutionManager.get(objectId) : new EdsSolution();
        item.setStatuses(getAsSelectItem(referenceManager.listReferences(EdsSolution._SOLUTION_STATUS), 10));
        item.setAssignees(getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE));
//        SolutionCaseItem crmCase = getSolutionCase(objectId);
//        if (crmCase != null) {
//            item.setCrmCase(crmCase);
//        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSolution.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(objectId);
        ServerUtils.kpiLog(log, kpiLog, "View Solution");

        return solution.getRPC(item);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SolutionCaseItem getSolutionCase(Integer solutionId) {
        EdsCaseSolution edsCaseSolution = caseSolutionManager.getSolutionCase(solutionId);
        SolutionCaseItem solutionCase = null;
        if (edsCaseSolution != null) {
            solutionCase = new SolutionCaseItem();
            solutionCase.setObjectId(edsCaseSolution.getCrmCase().getObjectID());
            solutionCase.setCaseNumber(edsCaseSolution.getCrmCase().getCaseNumberString());
            solutionCase.setSubject(edsCaseSolution.getCrmCase().getSubject());
        }
        return solutionCase;
    }

    @Transactional
    public void saveSolution(SolutionItem item) {
        EdsSolution solution = item.getObjectId() != null ? solutionManager.get(item.getObjectId()) : new EdsSolution();
        solution.setTitle(item.getTitle());
        solution.setAssignee(item.getAssigneeId() != null ? employeeManager.get(item.getAssigneeId()) : null);
        solution.setStatus(item.getStatusId() != null ? referenceManager.get(item.getStatusId()) : null);
        solution.setQuestion(item.getQuestion());
        solution.setAnswer(item.getAnswer());

        boolean newCreated = solutionManager.createOrUpdate(solution);
        attachmentUtilsManager.saveAttachments(F_SOLUTION, solution.getObjectID(), solution.getObjectID(), item.getAttachments());
        baseEventPostProcessor.registerEvent(CrmSolutionEventListenerImpl.TYPE, (newCreated ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT), solution, userManager.getUser());

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSolution.class.getSimpleName());
        kpiLog.setEntityId(solution.getObjectID());
        kpiLog.setActionType(newCreated ? KpiLog.ActionType.ADD : KpiLog.ActionType.UPDATE);
        ServerUtils.kpiLog(log, kpiLog, newCreated ? "Add Solution" : "Update Solution");
    }

    @Transactional
    public void deleteSolution(Integer objectID) {
        EdsSolution item = solutionManager.get(objectID);
        item.setDeleted(true);
        baseEventPostProcessor.registerEvent(CrmSolutionEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, item, userManager.getUser());
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSolution.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(objectID);
        ServerUtils.kpiLog(log, kpiLog, "Delete Solution");
    }

    @Transactional(propagation = Propagation./**/SUPPORTS, readOnly = true)
    public ListResult<ActivityItem> getNewActivityList(ListingFilterParameter fp) {
        return getActivityList(fp);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ActivityItem> getTaskList(ListingFilterParameter fp) {
        if (fp == null || fp.getRelationID() == null || fp.getRelationType() == null) {
            return new ListResult<>(new ArrayList<>(), 0);
        }
        List<Integer> ids = relationManager.getRelationIDsByType(fp.getRelationID(), null, fp.getRelationType(), RelationItem.TYPE_TASK);
        List<EdsTask> tasks = taskManager.getTaskByIds(ServerUtils.getAsCommoDelimited(ids, "0", ","));
        if (tasks != null && tasks.size() > 0) {
            ArrayList<ActivityItem> items = new ArrayList<>();
            String categoryCustomFieldForAgroprime = null;
            if (Integer.valueOf(24899).equals(SecurityContext.getCompanyID())) {
                List<CompanyCustomFieldItem> customFields = commonService.getCompanyCustomFields(ViewName.Task);
                if (customFields != null && customFields.size() > 0) {
                    for (CompanyCustomFieldItem customField : customFields) {
                        if (customField.getAliasName() != null && customField.getAliasName().toLowerCase().contains("category")) {
                            categoryCustomFieldForAgroprime = customField.getColumnCode();
                            break;
                        }
                    }
                }
            }
            for (EdsTask task : tasks) {
                ActivityItem item = new ActivityItem();
                item.setTaskObjectId(task.getObjectID());
                item.setAssignee(EdsTask.getAssigneeAsCommaDelimited(task));
                item.setTimeSpent(EdsTask.getOverAllTimeSpent(task));
                item.setSubject(task.getName());
                if (Integer.valueOf(24899).equals(SecurityContext.getCompanyID()) && task.getTaskCustomFields() != null) {
                    item.setSubject(task.getTaskCustomFields().getStringValue(categoryCustomFieldForAgroprime == null ? "string_value1" : categoryCustomFieldForAgroprime));
                    if (item.getSubject() == null || "".equals(item.getSubject())) {
                        item.setSubject(task.getName());
                    }
                }
                item.setStartDate(task.getStartDate());
                items.add(item);
            }
            return new ListResult<>(items, items.size());
        }
        return new ListResult<>(new ArrayList<>(), 0);
    }

    public ListResult<ActivityItem> getCrmTaskListForActivityTab(ListingFilterParameter fp) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrTaskRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        Integer entityID = fp.getEntityID();
        if (fp.getAccountID() != null && (fp.getEntityID() == null || fp.getEntityID() == 0)) {
            EdsCrmAccount account = crmAccountManager.get(fp.getAccountID());
            if (account != null && account.getEntityID() != null) {
                entityID = account.getEntityID();
            }
        }
        solrQuery.append(" AND ").append(SolrTaskRepresenter.FIELD_TASK_ID).append(":(").append(ServerUtils.getAsCommoDelimited(relationManager.getRelationIDsByType(fp.getRelationID(), entityID, fp.getRelationType(), RelationItem.TYPE_TASK), "0", " ")).append(")");
        fp.setSortField("");
        if (fp.getLimit() == null || fp.getLimit() <= 0) {
            fp.setLimit(20);
        }
        QueryResponse queryResponse = taskSolrComponent.getList(fp, solrQuery.toString());

        Map<Integer, List<SolrDocument>> results = new HashMap<>();
        ArrayList<Integer> taskIds = new ArrayList<>();
        GroupCommand groupCommand = queryResponse.getGroupResponse().getValues().get(0);
        int totalCount = groupCommand.getNGroups();
        for (Group group : groupCommand.getValues()) {
            SolrDocumentList solrDocList = group.getResult();
            SolrDocument solrDoc = solrDocList.get(0);
            Integer taskid = Integer.parseInt(SolrUtils.asString(solrDoc, SolrTaskRepresenter.FIELD_TASK_ID));
            results.put(taskid, solrDocList);
            taskIds.add(taskid);
        }
        ArrayList<ActivityItem> taskList = new ArrayList<>();

        for (Integer taskId : taskIds) {
            Integer rank = 0;
            SolrDocument doc = null;
            List<SolrDocument> relatedEntries = results.get(taskId);
            for (SolrDocument doc1 : relatedEntries) {
                Integer currentRank = (Integer) doc1.getFieldValue(SolrTaskRepresenter.FIELD_RANK);
                if (rank < currentRank) {
                    doc = doc1;
                    rank = currentRank;
                }
            }
            ActivityItem item = new ActivityItem();
            item.setTaskObjectId(Integer.parseInt(SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_ID)));
            item.setActivityType(CrmConstants.TASK);
            item.setSubject(SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_NAME));
            item.setStatus(SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_STATUS));
            item.setStatusID(SolrUtils.asInteger(doc, SolrTaskRepresenter.FIELD_TASK_STATUS_ID));
            item.setCreationDate(SolrUtils.asDate(doc, SolrTaskRepresenter.FIELD_CREATION_DATE));
            item.setStartDate(SolrUtils.asDate(doc, SolrTaskRepresenter.FIELD_START_DATE));
            item.setDueDate(SolrUtils.asDate(doc, SolrTaskRepresenter.FIELD_DUE_DATE));
            item.setAssignee(ServerUtils.asListToString(SolrUtils.asListString(doc, SolrTaskRepresenter.FIELD_ASSIGNEE_NAMES)));
            item.setPercent(SolrUtils.asFloat(doc, SolrTaskRepresenter.FILED_TASK_PERCENT_COMPLETED));
            taskList.add(item);
        }
        return new ListResult<>(taskList, totalCount);
    }

    @Override
    public Integer convertLead(OpportunityListItem opportunityItem, Integer leadID) {
        if (leadID != null) {
            EdsCrmContact lead = crmContactManager.get(leadID);
            EdsCrmAccount account = lead != null ? lead.getCrmAccount() : null;

            Integer contactType = EdsCrmContact.CRM_CONTACT;

            EdsOpportunity opportunity = null;

            if (opportunityItem != null) {
                opportunityItem.setClosingDate(new Date());
                opportunityItem.setAccountId(account != null ? account.getObjectID() : null);
                opportunityItem.setContactId(leadID);
                opportunityItem.setNumberData(generateOpportunityNumber());
                if (lead.getLeadAssignee() != null) {
                    opportunityItem.setBackupAssigneeID(lead.getLeadAssignee().getObjectID());
                }
                if (lead.getOwner() != null) {
                    opportunityItem.setOwnerID(lead.getOwner().getObjectID());
                }
                if (lead.getCampaign() != null) {
                    opportunityItem.setCampaignId(lead.getCampaign().getObjectID());
                    opportunityItem.setCampaign(lead.getCampaign().getName());
                }
                if (lead.getLeadSource() != null) {
                    opportunityItem.setLeadSource(lead.getLeadSource().getName());
                    opportunityItem.setLeadSourceId(lead.getLeadSource().getObjectID());
                }
                CompanyCfAndPropertyItems result = commonService.getCompanyCustomFieldsAndFormProperties(ViewName.Opportunity, LayoutRPC.OPPORTUNITY_FORM);
                FormProperty formProperty = null;
                if (result != null && result.getFormPropertyMap() != null) {
                    formProperty = result.getFormPropertyMap().get(CustomFormConstants.CURRENCY);
                }
                CurrencyItem baseCurrency = currencyService.getCurrency(formProperty.getSelectedId());
                if (baseCurrency == null) {
                    baseCurrency = invoiceServiceLocal.getBaseCurrency();
                }
                if (baseCurrency != null) {
                    opportunityItem.setCurrencyId(baseCurrency.getId());
                }
                if (lead.getCrmContactItems() != null) {
                    ArrayList<OpportunityItem> opportunityItems = new ArrayList<>();
                    lead.getCrmContactItems().forEach(it -> {
                        OpportunityItem item = new OpportunityItem();
                        item.setItemID(it.getItem() != null ? it.getItem().getObjectID() : null);
                        item.setItemName(it.getItem() != null ? it.getItem().getName() : it.getItemName());
                        item.setItemNumber(it.getItem() != null ? it.getItem().getProductNumber() : "");
                        item.setDescription(it.getDescription());
                        item.setQty(it.getQty());
                        if (it.getUnitMeasurement() != null) {
                            item.setUnitMeasurement(it.getUnitMeasurement().getAsSelectItem());
                        }
                        item.setPrice(it.getPrice());

                        if (it.getVat() != null) {
                            item.setTaxItem(it.getVat().createTaxItem());
                            item.setTaxAmount(it.getItemCalculatedTaxAmount());
                        }

                        item.setNet(it.getNet());
                        item.setSubTotal(it.getSubTotal());

                        item.setSupplierID(it.getSupplierID());
                        item.setSupplierName(it.getSupplierName());
                        if (it.getCategory() != null) {
                            item.setProductCategory(new SelectItem(it.getCategory().getObjectID(), it.getCategory().getName()));
                        }
                        if (it.getBrand() != null) {
                            item.setProductBrand(new SelectItem(it.getBrand().getObjectID(), it.getBrand().getName()));
                        }
                        item.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(it.getCustomFields(), commonService.getCompanyCustomFields(ViewName.OpportunitySubItem)));
                        opportunityItems.add(item);
                    });
                    opportunityItem.setItems(opportunityItems.toArray(new OpportunityItem[]{}));
                }
                if (genericSettingsManager.isSettingsEnabled(ENABLE_DEFAULT_TAX_TO_LEAD_CONVERT_TO_OPPORTUNITY)) {
                    EdsFormProperty edsFormProperty = formPropertyManager.getByFormID(LayoutRPC.OPPORTUNITY_FORM);
                    if (edsFormProperty != null) {
                        Gson gson = new Gson();
                        FormProperty[] formFields = gson.fromJson(edsFormProperty.getSettingsJSONData(), FormProperty[].class);
                        FormProperty taxCalc = null;
                        for (FormProperty fp : formFields) {
                            if (fp.getCode().equals("TAX_CALC_TYPE")) {
                                taxCalc = fp;
                                break;
                            }
                        }
                        opportunityItem.setTaxCalculationType(taxCalc != null ? taxCalc.getSelectedId() : null);
                    }
                }

                Integer opportunityID = saveOpportunity(opportunityItem);

                //Attachments
                FileResource[] fileResources = getCrmAttachments(leadID, "lead");
                if (fileResources != null) {
                    for (FileResource fileResource : fileResources) {
                        if (fileResource != null) {
                            attachmentUtilsManager.copyFileWhenConvert(F_OPPORTUNITY, fileResource.getFolderId(), fileResource.getObjectId(), opportunityID, fileResource);
                        }
                    }
                }

                opportunity = opportunityID != null ? opportunityManager.get(opportunityID) : null;
                if (opportunity != null) {
                    opportunity.setConvertedFromLead(Boolean.TRUE);
                }

                //Type initialize by stage
                if (opportunityItem.getStageId() != null) {
                    EdsReference stage = referenceManager.findReferenceByDescription(EdsOpportunity._OPPORTUNITY_STAGE, "100");
                    stage = stage == null ? referenceManager.findReference(EdsOpportunity._OPPORTUNITY_STAGE, EdsOpportunity.CLOSED_WON) : stage;
                    if (stage != null && stage.getObjectID().equals(opportunityItem.getStageId())) {
                        contactType = EdsCrmContact.CLIENT_CONTACT;
                    }
                }
            }


            lead.setContactType(contactType);
            lead.getCategories().clear();
            lead.addCategories(contactCategoryManager.getDefaultCategoryByContactType(lead.getContactType()));
            //Custom Fields
            if (lead.getCustomFields() != null) {
                List<EdsCompanyCustomFieldsSettings> companyCustomFields = companyCFSettingsManager.getCompanyLeadContactAndOpportunityCustomFields(CFLEAD, CFCONTACT, CFOPPORTUNITY, CFCRMACCOUNT);
                List<EdsCompanyCustomFieldsSettings> companyContactCustomFields = new ArrayList<>();
                List<EdsCompanyCustomFieldsSettings> companyLeadCustomFields = new ArrayList<>();
                List<EdsCompanyCustomFieldsSettings> companyAccountCustomFields = new ArrayList<>();
                List<EdsCompanyCustomFieldsSettings> companyOpportunityCustomFields = new ArrayList<>();
                for (EdsCompanyCustomFieldsSettings companyCustomField : companyCustomFields) {
                    switch (companyCustomField.getEntityName()) {
                        case CFCONTACT -> companyContactCustomFields.add(companyCustomField);
                        case CFLEAD -> companyLeadCustomFields.add(companyCustomField);
                        case CFOPPORTUNITY -> companyOpportunityCustomFields.add(companyCustomField);
                        case CFCRMACCOUNT -> companyAccountCustomFields.add(companyCustomField);
                    }
                }
                EdsCrmCustomFields crmCustomFieldsForContact = new EdsCrmCustomFields();
                EdsCrmCustomFields crmCustomFieldsForOpportunity = new EdsCrmCustomFields();
                EdsCrmCustomFields crmCustomFieldsForAccount = new EdsCrmCustomFields();
                boolean added = false;
                for (EdsCompanyCustomFieldsSettings leadCustomFieldSetting : companyLeadCustomFields) {
                    added = populateCustomFieldDetails(lead, leadCustomFieldSetting, companyContactCustomFields, crmCustomFieldsForContact) || added;
                    if (opportunity != null) {
                        added = populateCustomFieldDetails(lead, leadCustomFieldSetting, companyOpportunityCustomFields, crmCustomFieldsForOpportunity) || added;
                    }
                    if (account != null) {
                        crmCustomFieldsForAccount = account.getCustomFields() != null ? account.getCustomFields() : crmCustomFieldsForAccount;
                        added = populateCustomFieldDetails(lead, leadCustomFieldSetting, companyAccountCustomFields, crmCustomFieldsForAccount) || added;
                    }
                }
                if (added) {
                    crmCustomFieldsManager.create(crmCustomFieldsForContact);
                    lead.setOldCustomFields(lead.getCustomFields());
                    lead.setCustomFields(crmCustomFieldsForContact);
                    if (opportunity != null) {
                        crmCustomFieldsManager.create(crmCustomFieldsForOpportunity);
                        opportunity.setCustomFields(crmCustomFieldsForOpportunity);
                    }
                    if (account != null) {
                        crmCustomFieldsManager.createOrUpdate(crmCustomFieldsForAccount);
                        account.setCustomFields(crmCustomFieldsForAccount);
                    }
                }
            }
            //addresses
            if (account != null) {
                List<EdsAddress> addressList = addressManager.getContactAddresses(lead.getObjectID());
                if (addressList != null && addressList.size() > 0) {
                    for (EdsAddress a : addressList) {
                        a.setPrimary(false);
                        a.setCrmAccount(account);
                        a.setRelationType(EdsAddress.BILLING_ADDRESS);
                        addressManager.update(a);
                    }
                }
                account.setLastUpdateTime(new Date());
            }
            //Details
            if (opportunity != null && opportunityItem.isCopyLeadDetails()) {
                copyDetails(RelationItem.TYPE_LEAD, leadID, RelationItem.TYPE_OPPORTUNITY, opportunity.getObjectID(), opportunityItem.getOpportunityName());
                baseEventPostProcessor.registerEvent(CrmContactCustomEventListenerImpl.TYPE, CrmContactCustomEventListenerImpl.EVENT_REINDEX_CONTACT_RELATIONS, lead, userManager.getUser());
            }
            //Solr reindex
            try {
                contactSolrComponent.index(lead);
                if (opportunity != null) {
                    opportunitySolrComponent.index(opportunity);
                }
                crmAccountSolrComponent.index(account);
            } catch (Exception e) {
                log.error("", e);
            }
            return opportunity != null ? opportunity.getObjectID() : leadID;
        }
        return null;
    }

    /**
     * this method will copy all relations relatedTo (toType,reporterID) to the new entity(fromType, fromID);
     *
     * @param toType
     * @param toID
     * @param fromType == opportunity
     * @param fromID
     * @param fromName
     */
    private void copyDetails(String toType, Integer toID, String fromType, Integer fromID, String fromName) {
        copyNotes(toType, toID, fromID, EdsNoteHistory.CRM_OPPORTUNITY);
        copyRelations(toType, toID, fromType, fromID, fromName);
        copyCases(toID, toType, fromType, fromID, fromName);
    }

    private void copyCases(Integer reporterID, String reporterType, String fromType, Integer fromID, String fromName) {
        List<EdsCase> cases = caseManager.getCasesByReporter(reporterType, reporterID);
        ArrayList<RelationItem> relations = new ArrayList<>();
        if (cases != null && !cases.isEmpty() && fromID != null && fromType != null) {
            for (EdsCase crmCase : cases) {
                if (RelationItem.TYPE_LEAD.equals(reporterType) && RelationItem.TYPE_OPPORTUNITY.equals(fromType)) {
                    crmCase.setLead(null);
                    crmCase.setCrmContact(crmContactManager.get(reporterID));
                }
                relations.add(new RelationItem(null, crmCase.getObjectID(), RelationItem.TYPE_CASE, crmCase.getSubject(), fromID, fromType, fromName));
            }
            allInOneServiceLocal.saveRelations(fromType, fromID, fromName, relations);
            try {
                caseSolrComponent.indexes(cases);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyRelations(String toType, Integer toID, String fromType, Integer fromID, String fromName) {
        HashMap<String, ArrayList<Integer>> typeIDs = new HashMap<>();
        ArrayList<RelationItem> newRelations = new ArrayList<>();
        if (toID != null && toType != null && fromID != null) {
            List<EdsRelation> relations = relationManager.getAllRelations(toType, toID);
            if (relations != null && relations.size() > 0) {
                for (EdsRelation relation : relations) {
                    RelationItem relationItem = relation.wrapToRPC();
                    relationItem.setObjectID(null);
                    if (!relationItem.isFrom(toType, toID)) {
                        relationItem.setToID(relationItem.getFromID());
                        relationItem.setToType(relationItem.getFromType());
                        relationItem.setToName(relationItem.getFromName());
                    }
                    relationItem.setFromType(fromType);
                    relationItem.setFromID(null);
                    relationItem.setFromName(null);
                    newRelations.add(relationItem);
                    if (!typeIDs.containsKey(relationItem.getToType())) {
                        typeIDs.put(relationItem.getToType(), new ArrayList<>());
                    }
                    typeIDs.get(relationItem.getToType()).add(relationItem.getToID());
                }
                allInOneServiceLocal.saveRelations(fromType, fromID, fromName, newRelations);
            }
        }
        allInOneServiceLocal.relationsChangeTypesByType(toID);

        try {
            relationManager.updateSolr(typeIDs);
        } catch (InterruptedException | SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    private void copyNotes(String toType, Integer toID, Integer fromID, int fromType) {
        ListingFilterParameter fP = new ListingFilterParameter();
        fP.setRelationID(toID);
        fP.setRelationType(toType);
        List<EdsNoteHistory> notes = noteHistoryManager.getNoteList(fP);
        ArrayList<HistoryListItem> copies = new ArrayList<>();
        if (notes != null && notes.size() > 0) {
            for (EdsNoteHistory note : notes) {
                HistoryListItem noteItem = note.getHistoryItem();
                noteItem.setRelatedId(fromID);
                noteItem.setRelatedToId(fromType);
                noteItem.setObjectID(null);
                copies.add(noteItem);
                note.setRelatedTo(EdsNoteHistory.CRM_CONTACT);
            }
        }
        saveCrmNotes(CrmConstants.CRM_OPPORTUNITY, fromID, copies);
    }

    private boolean populateCustomFieldDetails(EdsCrmContact lead,
                                               EdsCompanyCustomFieldsSettings leadCustomFieldSetting,
                                               List<EdsCompanyCustomFieldsSettings> customFieldsSettingsInQuestion,
                                               EdsCrmCustomFields crmCustomFieldsToBeAdded) {
        boolean added = false;
        for (EdsCompanyCustomFieldsSettings customFieldSetting : customFieldsSettingsInQuestion) {
            CompanyCustomFieldItem companyCustomFieldItem = new CompanyCustomFieldItem();
            if (leadCustomFieldSetting.getAliasName() != null && leadCustomFieldSetting.getAliasName().equals(customFieldSetting.getAliasName())) {
                if (leadCustomFieldSetting.getDataType().equals(customFieldSetting.getDataType())) {
                    companyCustomFieldItem.setColumnCode(customFieldSetting.getColumnCode());
                    if (DATA_TYPE_DATE.equals(leadCustomFieldSetting.getDataType())) {
                        companyCustomFieldItem.setFieldDateNonConvertedValue(new DateNonConvertable((Date) lead.getCustomFields().getValueByCode(leadCustomFieldSetting.getDataType(), leadCustomFieldSetting.getColumnCode())));
                        CustomFieldsUtils.setDateCustomFields(crmCustomFieldsToBeAdded, companyCustomFieldItem);
                        added = true;
                    } else if (DATA_TYPE_NUMBER.equals(leadCustomFieldSetting.getDataType())) {
                        companyCustomFieldItem.setFieldStringValue((Double) lead.getCustomFields().getValueByCode(leadCustomFieldSetting.getDataType(), leadCustomFieldSetting.getColumnCode()));
                        CustomFieldsUtils.setDoubleCustomFields(crmCustomFieldsToBeAdded, companyCustomFieldItem);
                        added = true;
                    } else if (DATA_TYPE_FILE_UPLOAD.equals(leadCustomFieldSetting.getDataType())) {
                        Double uploadid = (Double) lead.getCustomFields().getValueByCode(leadCustomFieldSetting.getDataType(), leadCustomFieldSetting.getColumnCode());
                        companyCustomFieldItem.setFileUploadFieldId(uploadid != null ? uploadid.intValue() : null);
                        CustomFieldsUtils.setFileUploadCustomFields(crmCustomFieldsToBeAdded, companyCustomFieldItem);
                        added = true;
                    } else {
                        companyCustomFieldItem.setFieldStringValue((String) lead.getCustomFields().getValueByCode(leadCustomFieldSetting.getDataType(), leadCustomFieldSetting.getColumnCode()));
                        CustomFieldsUtils.setStringCustomFields(crmCustomFieldsToBeAdded, companyCustomFieldItem);
                        added = true;
                    }
                }
            }
        }
        return added;
    }

    @Override
    public boolean saveOppotunityEditCellValue(OpportunityListItem rowValue, String columnCodeName) {
        EdsOpportunity opportunity = opportunityManager.get(rowValue.getObjectId());
        if (opportunity != null) {
            opportunity.setLastChanges("");
            if (OpportunityListItem.NUMBER.equals(columnCodeName) && rowValue.getNumberData() != null) {
                opportunity.setNumber(rowValue.getNumberData().getNumberString());
                opportunity.setIntNumber(rowValue.getNumberData().getIntNumber());
            } else if (OpportunityListItem.AMOUNT.equals(columnCodeName)) {
                opportunity.setAmount(rowValue.getAmount());
                if (opportunity.getExchangeRate() != null && opportunity.getAmount() != null) {
                    opportunity.setAmountBaseCurrency((BigDecimal.valueOf(opportunity.getAmount()).divide(opportunity.getExchangeRate(), 8, RoundingMode.HALF_UP)).doubleValue());
                } else {
                    opportunity.setAmountBaseCurrency(opportunity.getAmount());
                }
            } else if (OpportunityListItem.OPPORTUNITY_NAME.equals(columnCodeName)) {
                opportunity.setName(rowValue.getOpportunityName());
            } else if (OpportunityListItem.STAGE.equals(columnCodeName)) {
                if (rowValue.getNote() != null) {
                    EdsNoteHistory edsNote = new EdsNoteHistory();
                    edsNote.setEmployee(userManager.getUser());
                    edsNote.setComment(rowValue.getNote());
                    edsNote.setEventDate(new Date());
                    edsNote.setRelatedId(opportunity.getObjectID());
                    edsNote.setRelatedTo(EdsNoteHistory.getRelatedToByEntityType(CrmConstants.CRM_OPPORTUNITY));
                    edsNote.setSuperUser(ServerUtils.isSuperUser());
                    noteHistoryManager.createOrUpdate(edsNote);

                    opportunity.setNote(rowValue.getNote());
                } else {
                    opportunity.setNote("");
                }
                EdsReference stage = referenceManager.get(rowValue.getStageId());
                opportunity.setStage(stage);
                if (stage != null && "0".equals(stage.getDescription())) {
                    opportunity.setRejectReason(referenceManager.get(rowValue.getSelectedSubStageId()));
                }
                boolean isExpectedRevenueEnable = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_OPPORTUNITY_EXPECTED_REVENUE);
                if (isExpectedRevenueEnable) {
                    opportunity.setExpectedRevenue(opportunity.getAmount());
                }
                //create the client and client contact from account and crm contact
                if (stage != null && (EdsOpportunity.CLOSED_WON.equals(stage.getCode()) || "100".equals(stage.getDescription())) && opportunity.getCrmAccount() != null) {
                    opportunity.getCrmAccount().addAccountType(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER));
                    updateCrmAccountAndAddToSolr(opportunity.getCrmAccount(), false, null);
                }
            } else if (OpportunityListItem.CLOSING_DATE.equals(columnCodeName)) {
                opportunity.setClosingDate(rowValue.getClosingDate());
            } else if (OpportunityListItem.OPPORTUNITY_LEAD_SOURCE.equals(columnCodeName)) {
                opportunity.setLeadSource(referenceManager.get(rowValue.getLeadSourceId()));
            } else {
                EdsCrmCustomFields edsCustomFields = opportunity.getCustomFields();
                if (edsCustomFields == null) {
                    edsCustomFields = new EdsCrmCustomFields();
                    crmCustomFieldsManager.create(edsCustomFields);
                    opportunity.setCustomFields(edsCustomFields);
                }

                if (rowValue.getCustomFieldsMap() != null && rowValue.getCustomFieldsMap().size() > 0) {
                    StringBuilder changesBuilder = new StringBuilder();
                    for (String cit : rowValue.getCustomFieldsMap().keySet()) {
                        changesBuilder.append(opportunity.getCustomFields() != null && CustomFieldsUtils.getObjectValue(opportunity.getCustomFields(), cit) != null ? getChanges(CustomFieldsUtils.getObjectValue(opportunity.getCustomFields(), cit), (String) rowValue.getCustomFieldsMap().get(cit), cit) : (cit + ","));
                    }
                    String changes = changesBuilder.toString();
                    if (!"".equals(changes)) {
                        opportunity.addCustomFieldChanges(changes);
                    }


                    List<CompanyCustomFieldItem> companyCustomFieldsSettings = commonService.getCompanyCustomFieldsByColumnCode(ViewName.ProductServiceView, columnCodeName);
                    if (companyCustomFieldsSettings != null && !companyCustomFieldsSettings.isEmpty()) {
                        CompanyCustomFieldItem cfItem = companyCustomFieldsSettings.get(0);
                        if (DATA_TYPE_TEXT.equals(cfItem.getDataType())) {
                            String oldString = edsCustomFields.getStringValue(cfItem.getColumnCode()) != null ? edsCustomFields.getStringValue(cfItem.getColumnCode()) : "";
                            if (!oldString.equals(rowValue.getCustomFieldsMap().get(columnCodeName))) {
                                opportunity.addHistoryChange(cfItem.getFieldName(), oldString, rowValue.getCustomFieldsMap().get(columnCodeName));
                            }
                        } else if (DATA_TYPE_NUMBER.equals(cfItem.getDataType())) {
                            Double oldNumber = edsCustomFields.getDoubleValue(cfItem.getColumnCode());
                            String oldNumberString = oldNumber != null ? String.valueOf(oldNumber) : "";
                            String newNumber = rowValue.getCustomFieldsMap().get(columnCodeName) == null ? "" : String.valueOf(Double.parseDouble((String) rowValue.getCustomFieldsMap().get(columnCodeName)));
                            if (!oldNumberString.equals(newNumber)) {
                                opportunity.addHistoryChange(cfItem.getFieldName(), oldNumberString, newNumber);
                            }
                        } else if (DATA_TYPE_DATE.equals(cfItem.getDataType())) {
                            Date oldDate = edsCustomFields.getDateValue(cfItem.getColumnCode());
                            DateNonConvertable cnc = (DateNonConvertable) rowValue.getCustomFieldsMap().get(columnCodeName);
                            Date newDate = cnc != null ? cnc.getNonConvertedDate() : null;
                            if ((oldDate != null && newDate != null && (oldDate.after(newDate) || oldDate.before(newDate))) ||
                                    (oldDate != null && newDate == null) || (oldDate == null && newDate != null)) {
                                opportunity.addHistoryChange(cfItem.getFieldName(), oldDate, newDate);
                            }
                        }
                    }
                }

                CustomFieldsUtils.setDomenObjectFieldChange(edsCustomFields, rowValue.getCustomFieldsMap(), columnCodeName);
            }
        }

        try {
            opportunityManager.update(opportunity);
            opportunitySolrComponent.index(opportunity);

            if (rowValue.getObjectId() != null) {
                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, opportunity, userManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_OPPORTUNITY);
            } else {
                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, opportunity, userManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_OPPORTUNITY);
            }

            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean saveAccountsEditCellView(CrmAccountItem rowValue, String columnCodeName) {
        EdsCrmAccount crmAccount = crmAccountManager.get(rowValue.getObjectId());
        if (CrmAccountItem.EMAIL.equals(columnCodeName)) {
            crmAccount.setEmail(rowValue.getEmail());
        } else if (CrmAccountItem.FAX.equals(columnCodeName)) {
            crmAccount.setFax(rowValue.getFax());
        } else if (CrmAccountItem.WEBSITE.equals(columnCodeName)) {
            crmAccount.setWebsite(rowValue.getWebsite());
        } else if (CrmAccountItem.COUNTRY.equals(columnCodeName)) {
            EdsCountry edsCountry = countryManager.get(rowValue.getDefaultAddress(true).getCountryId());
            crmAccount.setBillingAddress(crmAccount.getBillingAddress(true));
            crmAccount.getBillingAddress().setCountry(edsCountry);
            crmAccount.getBillingAddress().setState(null);
        } else if (CrmAccountItem.COUNTRY2.equals(columnCodeName)) {
            EdsCountry edsCountry = countryManager.get(rowValue.getDefaultAddress(false).getCountryId());
            crmAccount.setMailingAddress(crmAccount.getMailingAddress(true));
            crmAccount.getMailingAddress().setCountry(edsCountry);
            crmAccount.getMailingAddress().setState(null);
        } else if (CrmAccountItem.STATE.equals(columnCodeName)) {
            if (null != rowValue.getDefaultAddress(true).getStateId()) {
                crmAccount.setBillingAddress(crmAccount.getBillingAddress(true));
                crmAccount.getBillingAddress(true).setState(regionManager.get(rowValue.getDefaultAddress(true).getStateId()));
            }
        } else if (CrmAccountItem.STATE2.equals(columnCodeName)) {
            if (null != rowValue.getDefaultAddress(false).getStateId()) {
                crmAccount.setMailingAddress(crmAccount.getMailingAddress(true));
                crmAccount.getMailingAddress(true).setState(regionManager.get(rowValue.getDefaultAddress(false).getStateId()));
            }
        } else if (CrmAccountItem.INDUSTRY.equals(columnCodeName)) {
            crmAccount.setIndustry(rowValue.getIndustryID() != null ? referenceManager.get(rowValue.getIndustryID()) : null);
        } else if (CrmAccountItem.CURRENCY.equals(columnCodeName)) {
            crmAccount.setCurrency(rowValue.getCurrencyId() != null ? currencyManager.get(rowValue.getCurrencyId()) : null);
        } else if (CrmAccountItem.PAYMENT_METHOD.equals(columnCodeName)) {
            crmAccount.setPaymentMethod(rowValue.getPaymentMethodId() != null ? paymentMethodManager.get(rowValue.getPaymentMethodId()) : null);
        } else if (CrmAccountItem.VAT_NUMBER.equals(columnCodeName)) {
            crmAccount.setVatNumber(rowValue.getVatNumber());
        } else if (CrmAccountItem.REGISTRATION_NUMBER.equals(columnCodeName)) {
            crmAccount.setRegistrationNumber(rowValue.getRegistrationNumber());
        } else if (CrmAccountItem.CITY.equals(columnCodeName)) {
            crmAccount.getBillingAddress(true).setCity(rowValue.getDefaultAddress(true).getCity());
        } else if (CrmAccountItem.BILLING_ADDRESS.equals(columnCodeName)) {
            crmAccount.getBillingAddress(true).setAddress(rowValue.getDefaultAddress(true).getAddress());
        } else if (CrmAccountItem.BILLING_ADDRESS2.equals(columnCodeName)) {
            crmAccount.getBillingAddress(true).setAddressb(rowValue.getDefaultAddress(true).getAddressb());
        } else if (CrmAccountItem.POST_CODE.equals(columnCodeName)) {
            crmAccount.getBillingAddress(true).setZipCode(rowValue.getDefaultAddress(true).getZipCode());
        } else if (CrmAccountItem.CITY2.equals(columnCodeName)) {
            crmAccount.getMailingAddress(true).setCity(rowValue.getDefaultAddress(false).getCity());
        } else if (CrmAccountItem.MAILING_ADDRESS.equals(columnCodeName)) {
            crmAccount.getMailingAddress(true).setAddress(rowValue.getDefaultAddress(false).getAddress());
        } else if (CrmAccountItem.MAILING_ADDRESS2.equals(columnCodeName)) {
            crmAccount.getMailingAddress(true).setAddressb(rowValue.getDefaultAddress(false).getAddressb());
        } else if (CrmAccountItem.POST_CODE2.equals(columnCodeName)) {
            crmAccount.getMailingAddress(true).setZipCode(rowValue.getDefaultAddress(false).getZipCode());
        } else {
            EdsCrmCustomFields edsCrmCustomFields = crmAccount.getCustomFields();
            if (edsCrmCustomFields == null) {
                edsCrmCustomFields = new EdsCrmCustomFields();
                crmCustomFieldsManager.create(edsCrmCustomFields);
                crmAccount.setCustomFields(edsCrmCustomFields);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(edsCrmCustomFields, rowValue.getCustomFieldsMap(), columnCodeName);
        }
        try {
            crmAccountManager.update(crmAccount);
//            solrManager.addCrmAccountToIndex(crmAccount);
            crmAccountSolrComponent.index(crmAccount);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ActivityItem> getActivityList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        if (fp.getAccountID() != null) {
            EdsCrmAccount crmAccount = crmAccountManager.get(fp.getAccountID());
            if (crmAccount != null && crmAccount.getEntityID() != null) {
                fp.setEntityID(crmAccount.getEntityID());
            }
        }
        int totalEvents, totalEmails, totalTasks, totalMassMails = 0, totalSmsAlerts, totalQuots = 0, totalOrders = 0, totalInvoices = 0;
        ListResult<EventItem> events;
        List<EdsEmail> emails;
        List<EdsMailMessage> massMails = null;
        List<EdsSmsSendItem> smsAlerts;
        InvoiceList quoteList = null;
        InvoiceList orderList = null;
        InvoiceList invoicesList = null;
        TaskList tasks;
        if (fp.getRelationID() == null || fp.getRelationType() == null) {
            if (fp.getContactID() != null) {
                fp.setRelationType(RelationItem.TYPE_CONTACT);
                fp.setRelationID(fp.getContactID());
            } else if (fp.getLeadID() != null) {
                fp.setRelationType(RelationItem.TYPE_LEAD);
                fp.setRelationID(fp.getLeadID());
            }
        }
        boolean isEntityIDNull = fp.getEntityID() == null;
        if (fp.getRelationID() != null && fp.getRelationType() != null) {
            if (RelationItem.TYPE_CONTACT.equals(fp.getRelationType()) || RelationItem.TYPE_CANDIDATE.equals(fp.getRelationType()) || RelationItem.TYPE_LEAD.equals(fp.getRelationType())) {
                EdsCrmContact lead = crmContactManager.get(fp.getRelationID());
                if (lead != null && lead.getEntityID() != null) {
                    if (EdsCrmContact.LEAD_CONTACT.equals(lead.getContactType())) {
                        fp.setRelationType(RelationItem.TYPE_LEAD);
                    } else if (EdsCrmContact.CANDIDATE.equals(lead.getContactType())) {
                        fp.setRelationType(RelationItem.TYPE_CANDIDATE);
                    } else {
                        fp.setRelationType(RelationItem.TYPE_CONTACT);
                    }
                    fp.setEntityID(lead.getEntityID());
                }
            } else if (RelationItem.TYPE_OPPORTUNITY.equals(fp.getRelationType())) {
                fp.setOpportunityID(fp.getRelationID());
            } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(fp.getRelationType())) {
                fp.setAccountID(fp.getRelationID());
            }
        }

        fp.setLimit(fp.getLimit() > 0 ? fp.getLimit() : 20);

        ListingFilterParameter smsFilterParameter = fp;

        //Mass Mails
        OrderFieldEnum orderFieldEnum = OrderFieldEnum.getOrderField(fp.getSortField());
        if (RelationItem.TYPE_CONTACT.equals(fp.getRelationType()) || RelationItem.TYPE_LEAD.equals(fp.getRelationType()) || RelationItem.TYPE_CANDIDATE.equals(fp.getRelationType())) {
            if (orderFieldEnum != null) {
                fp.setSortField(BaseApiControllerV2.getSortField(orderFieldEnum, ListPanelType.MessageListPanel));
            }
            massMails = getMassMailList(fp);
            totalMassMails = massMails.size();
        }

        //Emails
        if (orderFieldEnum != null) {
            fp.setSortField(BaseApiControllerV2.getSortField(orderFieldEnum, ListPanelType.SentMessageListPanel));
        }
        emails = emailRepository.getEmailList(fp);
        totalEmails = emailRepository.getEmailCount(fp);
        if (isEntityIDNull && fp.getEntityID() != null) {
            fp.setEntityID(null);
        }

        //Events
        if (orderFieldEnum != null) {
            fp.setSortField(BaseApiControllerV2.getSortField(orderFieldEnum, ListPanelType.EventsListPanel));
        }
        events = getEventList(fp);
        totalEvents = events.getTotal();

        //Tasks
        if (orderFieldEnum != null) {
            fp.setSortField(BaseApiControllerV2.getSortField(orderFieldEnum, ListPanelType.TaskListPanel));
        }
        tasks = taskServiceLocal.getTaskList(fp);
        totalTasks = tasks == null ? 0 : tasks.getTotal();

        //SMS
        smsAlerts = smsSendItemManager.getSMSBy(smsFilterParameter);
        totalSmsAlerts = smsAlerts.size();

        //Sales Quote
        if (RelationItem.TYPE_CONTACT.equals(fp.getRelationType()) || RelationItem.TYPE_CANDIDATE.equals(fp.getRelationType()) || RelationItem.TYPE_LEAD.equals(fp.getRelationType())) {
            fp.setCrmContactId(fp.getRelationID());
        }

        boolean unlink_contact_and_account_invoices = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.UNLINK_CONTACT_AND_ACCOUNT_INVOICES) && CrmConstants.CRM_OPPORTUNITY.equals(fp.getRelationType());

        HashMap<String, Integer> ids = allInOneServiceLocal.findIDsBy(fp);
        if (ids != null && ids.size() > 0 && !unlink_contact_and_account_invoices) {
            for (Map.Entry<String, Integer> entry : ids.entrySet()) {
                if (CrmConstants.CRM_CONTACT.equals(entry.getKey())) {
                    fp.setClientContactId(entry.getValue());
                } else if (CrmConstants.CRM_ACCOUNT.equals(entry.getKey())) {
                    fp.setAccountID(entry.getValue());
                }
            }
        }
        if (fp.getAccountID() != null || fp.getCrmContactId() != null || fp.getOpportunityID() != null) {
            //Sale Quotes
            if (orderFieldEnum != null) {
                fp.setSortField(BaseApiControllerV2.getSortField(orderFieldEnum, ListPanelType.SaleQuoteListPanel));
            }
            quoteList = quoteService.getSaleQuoteData(fp);
            if (quoteList != null) {
                totalQuots = quoteList.getTotal();
            }

            //Sale Orders
            if (orderFieldEnum != null) {
                fp.setSortField(BaseApiControllerV2.getSortField(orderFieldEnum, ListPanelType.SaleOrderListPanel));
            }
            orderList = quoteService.getSaleOrderData(fp);
            if (orderList != null) {
                totalOrders = orderList.getTotal();
            }

            //Sale Invoice
            if (orderFieldEnum != null) {
                fp.setSortField(BaseApiControllerV2.getSortField(orderFieldEnum, ListPanelType.SaleInvoiceListPanel));
            }
            invoicesList = invoiceServiceLocal.getSaleInvoiceData(fp);
            if (invoicesList != null) {
                totalInvoices = invoicesList.getTotal();
            }
        }

        int totalCount = totalEvents + totalEmails + totalTasks + totalMassMails + totalSmsAlerts + totalQuots + totalOrders + totalInvoices;
        ArrayList<ActivityItem> results = new ArrayList<>();

        if (totalEmails > 0) {
            for (EdsEmail email : emails) {
                results.add(getActivity(email, false, ""));
            }
        }
        if (totalEvents > 0) {
            for (EventItem crmEvent : events.getList()) {
                ActivityItem activityItem = crmEvent.asActivityItem();
                activityItem.setStatus(commonLocalizer.localize(crmEvent.asActivityItem().getStatus().toLowerCase(), ""));
                results.add(activityItem);
            }
        }
        if (totalTasks > 0) {
            for (TaskListItem task : tasks.getList()) {
                results.add(getActivity(task, false, ""));
            }
        }
        if (totalMassMails > 0) {
            for (EdsMailMessage mail : massMails) {
                results.add(getActivity(mail, false, ""));
            }
        }
        if (totalSmsAlerts > 0) {
            for (EdsSmsSendItem sms : smsAlerts) {
                results.add(getActivity(sms, false, ""));
            }
        }
        if (totalQuots > 0) {
            for (NewInvoice quote : quoteList.getList()) {
                quote.getType();
                results.add(getActivity(quote, false, CrmConstants.SALEQUOTE));
            }
        }
        if (totalOrders > 0) {
            for (NewInvoice order : orderList.getList()) {
                results.add(getActivity(order, false, CrmConstants.SALEORDER));
            }
        }
        if (totalInvoices > 0) {
            for (NewInvoice invoice : invoicesList.getList()) {
                results.add(getActivity(invoice, false, CrmConstants.SALEINVOICE));
            }
        }

        if (orderFieldEnum == null) {
            results.sort((o1, o2) -> {
                if (o1.getCreationDate() != null && !"".equals(o1.getCreationDate()) && o2.getCreationDate() != null && !"".equals(o2.getCreationDate())) {
                    if (o1.getCreationDate().before(o2.getCreationDate())) {
                        return 1;
                    } else if (o1.getCreationDate().after(o2.getCreationDate())) {
                        return -1;
                    } else {
                        return 0;
                    }
                } else {
                    return -1;
                }
            });
        }
        return new ListResult<>(results, totalCount);
    }

    private List<EdsMailMessage> getMassMailList(ListingFilterParameter fp) {
        List<Object[]> mailMessages = mailMessageManager.getListForLead(fp);
        List<EdsMailMessage> massMails = new ArrayList<>();
        for (Object[] mails : mailMessages) {
            EdsMailMessage mail = new EdsMailMessage();
            if (mails.length > 0 && mails[0] != null) {
                mail.setObjectID((Integer) mails[0]);
            }
            if (mails.length > 1 && mails[1] != null) {
                mail.setSubject((String) mails[1]);
            }
            if (mails.length > 2 && mails[2] != null) {
                mail.setCreationTime((Date) mails[2]);
            }
            massMails.add(mail);
        }
        return massMails;
    }

    /**
     * CRM attachments:
     *
     * @param objectID contact attachments
     * @param from     lead attatchments
     * @param from     case attachments
     * @return FileResource[]
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FileResource[] getCrmAttachments(Integer objectID, String from) {
        List<FileResource> attachments = null;
        if (objectID != null) {
            if (RelationItem.TYPE_CONTACT.equals(from) || RelationItem.TYPE_LEAD.equals(from) || RelationItem.TYPE_CANDIDATE.equals(from)) {
                EdsCrmContact contact = crmContactManager.get(objectID);//crm contact
                EdsUser user = crmContactManager.getUser();
                if (user == null) {
                    user = contact.getOwner();
                }
                //hozircha shunday qilib turish kerak, vaqt bulganda... convert lead ni o'zgartirish kerak... Sherali.P ni aytishicha update method yo'q folder typeni o'zgartirish(leadTypedan contactTypega...) 28 May 2011
                attachments = attachmentUtilsManager.getAttachments(CrmConstants.CANDIDATE.equals(from) ? F_CANDIDATE : F_LEAD, contact.getObjectID(), contact.getObjectID(), user);
                if (CrmConstants.CRM_CONTACT.equals(from)) {//o'zgartirilganda buni remove qilish kerak...
                    attachments.addAll(attachmentUtilsManager.getAttachments(F_CRM_CONTACT, contact.getObjectID(), contact.getObjectID(), user));
                }
            } else if ("crmCase".equals(from) || RelationItem.TYPE_CASE.equals(from)) {
                EdsCase crmCase = caseManager.get(objectID);//crm case
                if (crmCase != null && crmCase.getTracker() != null) {
                    attachments = attachmentUtilsManager.getAttachments(F_CASE, crmCase.getObjectID(), crmCase.getObjectID());
                    if (attachments == null) {
                        attachments = new ArrayList<>();
                    }
                    attachments.addAll(EdsEmailAttachment.asFileResourses(emailAttachmentManager.getTrackerAttachments(crmCase.getTracker().getObjectID())));
                }
            } else if (RelationItem.TYPE_OPPORTUNITY.equals(from)) {
                EdsOpportunity opportunity = opportunityManager.get(objectID);
                attachments = attachmentUtilsManager.getAttachments(F_OPPORTUNITY, opportunity.getObjectID(), opportunity.getObjectID());
            } else if ("crmEvent".equals(from) || RelationItem.TYPE_EVENT.equals(from)) {
                EdsEvent event = eventManager.get(objectID);
                EdsUser user = userManager.getUser();
                if (event != null && user != null) {
                    EdsFolder eventFolder = folderManager.getFolderByFolderType(EdsFolder.F_EVENT);
                    if (eventFolder != null) {
                        attachments = attachmentUtilsManager.getAttachments(F_EVENT, eventFolder.getObjectID(), event.getObjectID());
                    }
                }
            } else if ("crmSolution".equals(from)) {
                EdsSolution solution = solutionManager.get(objectID);
                attachments = attachmentUtilsManager.getAttachments(F_SOLUTION, solution.getObjectID(), solution.getObjectID());
                if (attachments == null) {
                    attachments = new ArrayList<>();
                }
            } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(from) || "crmAccount".equals(from)) {
                EdsCrmAccount crmaAccount = crmAccountManager.get(objectID);
                attachments = attachmentUtilsManager.getAttachments(F_CRM_ACCOUNT, crmaAccount.getObjectID(), crmaAccount.getObjectID());
                if (attachments == null) {
                    attachments = new ArrayList<>();
                }
            }
        }
        return attachments != null && attachments.size() > 0 ? attachments.toArray(new FileResource[]{}) : null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<SelectItem> getLookUpItems(ListingFilterParameter filterParametrs, Integer type) {
        ListResult<SelectItem> listResult = new ListResult<>();
        filterParametrs.setLookUp(true);

        Map<Integer, String> typeToCrmConstantMap = new HashMap<>();
        typeToCrmConstantMap.put(CrmConstants.CRM_ACCOUNT_ID, CrmConstants.CRM_ACCOUNT);
        typeToCrmConstantMap.put(CrmConstants.CLIENT_ID, CrmConstants.CLIENT);
        typeToCrmConstantMap.put(CrmConstants.SUPPLIER_ID, CrmConstants.SUPPLIER);
        typeToCrmConstantMap.put(CrmConstants.CRM_CASE_ID, CrmConstants.CRM_CASE);
        typeToCrmConstantMap.put(CrmConstants.CRM_CONTACT_ID, CrmConstants.CRM_CONTACT);
        typeToCrmConstantMap.put(CrmConstants.CANDIDATE_ID, CrmConstants.CANDIDATE);
        typeToCrmConstantMap.put(CrmConstants.CRM_OPPORTUNITY_ID, CrmConstants.CRM_OPPORTUNITY);
        typeToCrmConstantMap.put(CrmConstants.CRM_LEAD_ID, CrmConstants.CRM_LEAD);

        if (typeToCrmConstantMap.containsKey(type)) {
            String crmConstant = typeToCrmConstantMap.get(type);
            listResult = getCrmLookNames(filterParametrs, crmConstant);
        } else {
            switch (type) {
                case CrmConstants.CRM_CAMPAIGN_ID:
                    List<EdsCampaign> campaignList = campaignManager.getCampaignList(filterParametrs);
                    if (campaignList != null) {
                        List<SelectItem> campaignSelectItemList = campaignList.stream()
                                .map(EdsCampaign::getAsSelectItem)
                                .collect(Collectors.toList());
                        listResult = new ListResult<>(new ArrayList<>(campaignSelectItemList), campaignSelectItemList.size());
                    }
                    break;
                case CrmConstants.CRM_EVENT_ID:
                    if (filterParametrs.getStartDate() != null) {
                        EdsUser user = userManager.getUser();
                        Calendar fromTimeCalendar = Calendar.getInstance();
                        Calendar toTimeCalendar = Calendar.getInstance();
                        TimeZone tz = user != null ? user.getUserTimezone() : null;
                        Date startDate = tz != null ? ServerUtils.convertServerDateToUserDate(filterParametrs.getStartDate(), tz) : null;
                        fromTimeCalendar.setTime(startDate != null ? startDate : filterParametrs.getStartDate());
                        toTimeCalendar.setTime(startDate != null ? startDate : filterParametrs.getStartDate());
                        ServerUtils.setBeginningOfTheDay(fromTimeCalendar);
                        ServerUtils.setEndOfTheDay(toTimeCalendar);
                        Date serverStartDate = tz != null ? ServerUtils.convertUserDateToServerDate(fromTimeCalendar.getTime(), tz) : fromTimeCalendar.getTime();
                        Date serverEndDate = tz != null ? ServerUtils.convertUserDateToServerDate(toTimeCalendar.getTime(), tz) : toTimeCalendar.getTime();
                        filterParametrs.setStartDate(serverStartDate);
                        filterParametrs.setEndDate(serverEndDate);
                    }
                    List<EdsEvent> calendarEvents = eventManager.getList(filterParametrs);
                    if (calendarEvents != null) {
                        ArrayList<SelectItem> eventSelectItemList = new ArrayList<>();
                        calendarEvents.forEach(edsEvent -> {
                            eventSelectItemList.add(edsEvent.getAsSelectItem());
                        });
                        listResult = new ListResult<SelectItem>(eventSelectItemList, eventSelectItemList.size());
                    }
                    break;
                case CrmConstants.LEAD_SOURCE_ID:
                    EdsReference referenceByCode = referenceManager.getByCode(EdsCrmContact._LEAD_SOURCE);
                    filterParametrs.setParentID(referenceByCode.getObjectID());
                    List<EdsReference> references = referenceManager.listReferences(filterParametrs);
                    if (references != null && !references.isEmpty()) {
                        List<SelectItem> selectItems = references.stream().map(EdsReference::getAsSelectItem).collect(Collectors.toList());
                        listResult = new ListResult<>(new ArrayList<>(selectItems), selectItems.size());
                    }
                    break;

            }
        }
        listResult.setList(listResult.getList() != null ? listResult.getList() : new ArrayList<>());

        return listResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EventItem getEvent(Integer objectId) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsEvent.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(objectId);
        ServerUtils.kpiLog(log, kpiLog, "Get activity");
        EventItem item = new EventItem();
        EdsEvent event = eventManager.get(objectId);
        if (event != null) {
            item = event.getRPC(item);
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(Appointment.CALL_LOG == event.getActivityType() ? ViewName.LogACall : ViewName.Activity);
            item.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(event.getEventCustomFields(), customFieldsItems));
            item.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_EVENT, objectId)));
            item.setGuests(googleCalendarServiceLocal.wrapEdsGoogleCalendarEventGuestsToSelectItem(event, true));

            List<EdsUser> sharedEmployees = employeeEventManager.getEventSharedEmployees(event.getObjectID());
            ArrayList<PositionsSelectItem> eventSharedEmployees = new ArrayList<>();
            for (EdsUser employee : sharedEmployees) {
                PositionsSelectItem positionsSelectItem = new PositionsSelectItem();
                positionsSelectItem.setEmployeeId(employee.getObjectID());
                positionsSelectItem.setName(employee.getFullName());
                EdsEmployee edsEmployee = employeeManager.get(employee.getObjectID());//ClassCastException tashayapti isEmployee qilganda
                if (edsEmployee != null) {
                    if (edsEmployee.getPosition() != null) {
                        positionsSelectItem.setPositionName(edsEmployee.getPosition().getName());
                    }
                    if (edsEmployee.getEmployeeTeam() != null && edsEmployee.getEmployeeTeam().getTeam() != null) {
                        positionsSelectItem.setDepartmentName(edsEmployee.getEmployeeTeam().getTeam().getName());
                    }
                }
                eventSharedEmployees.add(positionsSelectItem);
            }
            item.setSharedEmployees(eventSharedEmployees);
            EdsFolder eventFolder = folderManager.getFolderByFolderType(EdsFolder.F_EVENT);
            if (eventFolder != null) {
                item.setAttachmentFolderID(eventFolder.getObjectID());
            }
            item.setInvitationResponse(event.getInvitationResponse());
        }
//        ArrayList<BookingReservationItem> reservationList = new ArrayList<>();
//        BookingReservationItem reservationItem;
//        for (RelationItem relationItem : item.getRelations()) {
//            if (relationItem.getToType() != null && relationItem.getToType().equals(RelationItem.TYPE_EVENT)) {
//                if (relationItem.getFromID() != null) {
//                    for (EdsBookingItemReservation edsBookingItemReservation : bookingItemReservationManager.getBookingItemReservationByid(relationItem.getFromID())) {
//                        reservationItem = new BookingReservationItem();
//                        reservationItem.setToDate(edsBookingItemReservation.getTo());
//                        reservationItem.setFromDate(edsBookingItemReservation.getFrom());
//                        reservationItem.setSelectedReservedById(edsBookingItemReservation.getReservedBy().getAsSelectItem());
//                        reservationItem.setBookingItemName(edsBookingItemReservation.getBookingItem().getName());
//                        reservationList.add(reservationItem);
//                    }
//                }
//            }
//        }
//        item.setBookingReservationItemList(reservationList);
        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCaseStatusLisItems() {
        return getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_STATUS), ServerUtils.REFERENCE);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCaseReasonItems() {
        return getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_REASON), ServerUtils.REFERENCE);
    }

    @Transactional
    public void saveCaseAndSolution(CaseItem caseItem, SolutionItem solutionItem) {
        EdsSolution solution = new EdsSolution();
        if (solutionItem.getObjectId() != null) {
            solution = solutionManager.get(solutionItem.getObjectId());
        }
        if (caseItem.getCaseAssigneeId() != null && solution.getAssignee() == null) {
            solution.setAssignee(employeeManager.get(caseItem.getCaseAssigneeId()));
        }
        solution.setTitle(solutionItem.getTitle());
        solution.setDetails(solutionItem.getDetails());
        if (solutionItem.getObjectId() != null) {
            solutionManager.update(solution);
        } else {
            solutionManager.create(solution);
        }
        EdsCase crmCase = new EdsCase();
        if (caseItem.getObjectId() != null) {
            crmCase = caseManager.get(caseItem.getObjectId());
        }
        if (caseItem.getCaseAssigneeId() != null) {
            crmCase.setAssignee(userManager.get(caseItem.getCaseAssigneeId()));
        }
        if (caseItem.getStatus().getId() != null) {
            crmCase.setStatus(referenceManager.get(caseItem.getStatus().getId()));
        } else {
            crmCase.setStatus(null);
        }
        if (caseItem.getCaseReasonId() != null) {
            crmCase.setCaseReason(referenceManager.get(caseItem.getCaseReasonId()));
        } else {
            crmCase.setCaseReason(null);
        }
        crmCase.setInternalComment(caseItem.getInternalComment());
        boolean isNew = caseManager.createOrUpdate(crmCase);
        if (isNew) {
            generateCaseNumber(crmCase);
        }
        try {
//            solrManager.indexAddCase(crmCase);
            caseSolrComponent.index(crmCase);
            if (isNew) {
                solrTransactionManager.registerEvent(SolrEvent.CRM_CASE_ADD, crmCase, userManager.getUser().getCompany());
            }
        } catch (Exception e) {
            baseEventPostProcessor.registerEvent(CrmCaseEventListeneImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, crmCase, userManager.getUser());
            e.printStackTrace();
        }

        EdsCaseSolution caseSolution = new EdsCaseSolution(crmCase, solution);
        caseSolutionManager.create(caseSolution);
    }


    @Transactional
    public void saveCrmNote(ListingFilterParameter fp, String comment) {
        EdsUser user = crmContactManager.getUser();
        saveCrmNote(fp, comment, user);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getContactsByAccount(Integer accountID, Integer contactID) {
        List<EdsCrmContact> contactList = crmContactManager.getContactsByCrmAccount(accountID);
        List<SelectItem> items = new ArrayList<>();
        for (EdsCrmContact con : contactList) {
            SelectItem item = new SelectItem();
            item.setName(getEmptyIfNull(con.getFirstName()) + " " + getEmptyIfNull(con.getLastName()));
            item.setId(con.getObjectID());
            if (contactID == null || !contactID.equals(con.getObjectID())) {
                items.add(item);
            }
        }
        return items.toArray(new SelectItem[]{});
    }

    @Transactional
    public ArrayList<Integer> deleteCrmAccount(ArrayList<Integer> objectIDs, boolean removeContactsAlso) {
        EdsUser user = userManager.getUser();
        objectIDs = new ArrayList<>(objectIDs);
        ArrayList<Integer> undeletibleAccountIDs = new ArrayList<>();
        // check the account's invoice , transaction and opening balance
        List<EdsCrmAccount> crmAccounts = crmAccountManager.getCrmAccountsByIDs(objectIDs);
        if (crmAccounts != null && !crmAccounts.isEmpty()) {
            for (EdsCrmAccount account : crmAccounts) {
                crmSubItemManager.deleteItems(account.getObjectID(), account.isClient() ? CUSTOMER : SUPPLIER);
                Map<Boolean, String> validation = checkCrmAccountRelations(account);
                if (validation.size() > 0 && validation.containsKey(Boolean.FALSE)) { //allow access to delete
                    if (validation.containsValue(EdsCrmAccount.CUSTOMER)) {
                        EdsCustomerTransaction customerTransaction = transactionManager.getCustomerOpeningBalanceTransaction(account.getObjectID());
                        if (customerTransaction != null) {
                            customerTransaction.setDeleted(true);
                            transactionManager.update(customerTransaction);
                        }
                        account.getAccountTypes().remove(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER));
                    } else { //SUPPLIER
                        EdsSupplierTransaction supplierTransaction = transactionManager.getSupplierOpeningBalanceTransaction(account.getObjectID());
                        if (supplierTransaction != null) {
                            supplierTransaction.setDeleted(true);
                            transactionManager.update(supplierTransaction);
                        }
                        account.getAccountTypes().remove(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.SUPPLIER));
                    }
                    account.setDeleted(true);
                    crmAccountManager.update(account);
                } else {
                    if (validation.size() != 0) {
                        undeletibleAccountIDs.add(account.getObjectID());
                    } else {
                        account.setDeleted(true);
                        crmAccountManager.update(account);
                    }
                }
                Set<EdsCrmContact> contacts = account.getCrmContacts();
                if (removeContactsAlso) {
                    for (EdsCrmContact crmContact : contacts) {
                        deletedClientContact(user, crmContact, account.isClient(), removeContactsAlso);
                    }
                }
            }
        }
        objectIDs.removeAll(undeletibleAccountIDs);

        if (objectIDs.size() > 0) {
            try {
                solrManager.removeCrmAccountByIds(objectIDs.toArray(new Integer[]{}));
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
            }
            if (objectIDs.size() == 1) {
                EdsCrmAccount account = crmAccountManager.get(objectIDs.get(0));
                KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                kpiLog.setEntityName(EdsCrmAccount.class.getSimpleName());
                kpiLog.setActionType(KpiLog.ActionType.DELETE);
                kpiLog.setEntityId(objectIDs.get(0));
                ServerUtils.kpiLog(log, kpiLog, "Crm Account deleted");
                if (account != null) {
                    baseEventPostProcessor.registerEvent(CrmAccountEventListenerImpl.TYPE, (BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE), account, user);

                    EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, account, user);
                    workflowEvent.setEntityType(RelationItem.TYPE_CRM_ACCOUNT);
                }
            } else {
                for (int id : objectIDs) {
                    EdsCrmAccount edsCrmAccount = crmAccountManager.get(id);
                    if (edsCrmAccount != null) {
                        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                        kpiLog.setEntityName(EdsCrmAccount.class.getSimpleName());
                        kpiLog.setActionType(KpiLog.ActionType.DELETE);
                        kpiLog.setEntityId(id);
                        ServerUtils.kpiLog(log, kpiLog, "Crm Account deleted");

                        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, edsCrmAccount, user);
                        workflowEvent.setEntityType(RelationItem.TYPE_CRM_ACCOUNT);
                    }
                }
            }
        }
        return objectIDs;
    }

    private void deletedClientContact(EdsUser user, EdsCrmContact contact, boolean isClient, boolean deleteCrmContact) {
        EdsClientContact clientContact = clientContactManager.getClientContactByCrmContact(contact.getObjectID());

        if (clientContact != null) {
            userManager.deleteUser(clientContact.getObjectID(), referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_INACTIVE));

            if (isClient) {
                baseEventPostProcessor.registerEvent(ClientContactEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, clientContact, user);
            }
        }
        if (deleteCrmContact) {
            Integer inactiveID = referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_INACTIVE).getObjectID();
            crmContactManager.deleteContact(contact.getObjectID(), inactiveID);
            createContactHistory("Contact deleted", contact);
            baseEventPostProcessor.registerEvent(CrmContactCustomEventListenerImpl.TYPE, CrmContactCustomEventListenerImpl.EVENT_DELETE_CRM_CONTACT_FROM_SOLR, contact, crmContactManager.getUser());
        } else {
            contact.setContactType(EdsCrmContact.CRM_CONTACT);
            contact.setEntityContactID(null);
            if (isClient) {
                contact.getCategories().remove(contactCategoryManager.getDefaultCategoryByContactType(EdsCrmContact.CLIENT_CONTACT));
            } else {
                contact.getCategories().remove(contactCategoryManager.getDefaultCategoryByContactType(EdsCrmContact.SUPPLIER_CONTACT));
            }
            crmContactManager.update(contact);
            createContactHistory("Updated the contact", contact);
            baseEventPostProcessor.registerEvent(CrmContactCustomEventListenerImpl.TYPE, CrmContactCustomEventListenerImpl.EVENT_ADD_CRM_CONTACT_TO_SOLR, contact, crmContactManager.getUser());
        }
    }


    /*
     * Check crm account relations before deleting if an account type is CUSTOMER or SUPPLIER
     * return Map<Boolean,String>
     * see also deleteClient and deleteClientsOrSuppliers methods in ClientService class
     */
    private Map<Boolean, String> checkCrmAccountRelations(EdsCrmAccount account) {
        Map<Boolean, String> checkRelationAndType = new HashMap<>();
        String type = "";
        if (account.getAccountTypes() != null && account.getAccountTypes().size() > 0) {
            boolean hasAnyRelation = false;
            for (EdsReference r : account.getAccountTypes()) {
                hasAnyRelation = false;
                if (EdsCrmAccount.CUSTOMER.equals(r.getCode())) {
                    hasAnyRelation = allInOneServiceLocal.checkCrmAccountRelations(account.getObjectID(), EdsCrmAccount.CUSTOMER);
                    type = r.getCode();
                } else if (EdsCrmAccount.SUPPLIER.equals(r.getCode())) {
                    hasAnyRelation = allInOneServiceLocal.checkCrmAccountRelations(account.getObjectID(), EdsCrmAccount.SUPPLIER);
                    type = r.getCode();
                }
                if (hasAnyRelation) {
                    break;
                }
            }
            checkRelationAndType.put(hasAnyRelation, type);
            return checkRelationAndType;
        }
        return checkRelationAndType;
    }

    @Transactional
    public void deleteCase(ArrayList<Integer> idsList) {
        caseManager.setCaseDeletedTrue(idsList);
        try {
            solrManager.removeCompanyCaseByIds(idsList.toArray(new Integer[]{}));
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public NumberData generateAccountNumberData(String accountType) {
        return crmAccountManager.generateAccountNumberData(accountType);
    }

    @Transactional
    public void deleteCase(Integer objectID) {
        EdsCase crmCase = caseManager.get(objectID);
        crmCase.setDeleted(true);
        caseManager.update(crmCase);
        try {
            solrManager.removeCompanyCaseByIds(objectID);
            baseEventPostProcessor.registerEvent(CrmCaseEventListeneImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, crmCase, userManager.getUser());
        } catch (IOException | SolrServerException e) {
            baseEventPostProcessor.registerEvent(CrmCaseEventListeneImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, crmCase, userManager.getUser());
            e.printStackTrace();
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCase.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(objectID);
        ServerUtils.kpiLog(log, kpiLog, "Delete case");
    }

    @Transactional
    public void deleteWebForm(Integer objectId) {
        if (objectId != null) {
            for (EdsWebField edsWebField : webFieldManager.getByWebFormID(objectId)) {
                edsWebField.setWebForm(null);
                webFieldManager.delete(edsWebField);
            }
            EdsWebForm edsWebForm = webFormManager.get(objectId);
            edsWebForm.setDeleted(true);
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsWebForm.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.DELETE);
            kpiLog.setEntityId(objectId);
            ServerUtils.kpiLog(log, kpiLog, "Delete WebForm");
        }
    }

    @Transactional
    public void deleteCampaign(Integer objectID) {
        EdsCampaign item = campaignManager.get(objectID);
        item.setDeleted(true);
        EdsUser user = userManager.getUser();
        if (user != null) {
            baseEventPostProcessor.registerEvent(CrmCampaignEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, item, user);
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCampaign.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(objectID);
        ServerUtils.kpiLog(log, kpiLog, "Campaign deleted");
    }

    @Transactional
    public ArrayList<Integer> deleteEvent(ArrayList<Integer> objectIDs) {
        EdsUser edsUser = userManager.getUser();
        objectIDs = new ArrayList<>(objectIDs);
        List<Integer> result = eventManager.deleteEvents(objectIDs, edsUser);
        if (objectIDs.size() > 0) {
            for (Integer objectId : objectIDs) {
                EdsEvent edsEvent = eventManager.get(objectId);
                EdsZoomMeeting meetingByEventId = zoomMeetingManager.getMeetingByEventId(objectId);
                if (meetingByEventId != null) {
                    zoomService.deleteMeeting(edsEvent);
                }
                baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, edsEvent, edsUser);
            }
        }

        if (result != null && result.size() > 0) {
            try {
                solrManager.deleteEvents(result.toArray(new Integer[]{}));
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
            }
            objectIDs.removeAll(result);
        }
        for (Integer ids : result) {
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsEvent.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.DELETE);
            kpiLog.setEntityId(ids);
            ServerUtils.kpiLog(log, kpiLog, "delete activity");

            EdsEvent event = eventManager.get(ids);
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, event, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_EVENT);
        }
        return objectIDs;
    }

    @Transactional
    public void deleteAttachment(Integer attachmentId) {
        commonServiceLocal.deleteAttachment(attachmentId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateReplyToReporterCaseItem(EntityToEmailTemplate emailTemplate, Integer autoResponseID) {
        return emailTemplateServiceLocal.generateReplyToReporterCaseItem(emailTemplate, autoResponseID);
    }

    /**
     * this method returns the histories array of the Crm.Entity
     *
     * @param objectID     - id of the CRM.Entity
     * @param relationType - type(Contact, Case) of the Crm.Entity
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CrmHistoryList[] getCrmHistories(Integer objectID, String relationType) {
        List<CrmHistoryList> histories = new ArrayList<>();
        List crmCaseHistory = relationType == null || RelationItem.TYPE_CASE.equals(relationType) ? caseHistoryManager.historyList(objectID) : (RelationItem.TYPE_CONTACT.equals(relationType) ? contactHistoryManager.getContactHistoryList(objectID) : null);
        HashMap<Integer, String> photos = new HashMap<>();
        if (crmCaseHistory != null) {
            for (CrmHistory history : (List<CrmHistory>) crmCaseHistory) {

                CrmHistoryList historyList = getHistoryAsRPC(history);
                if (history.getUpdater() != null && history.getUpdater().getPhoto() != null) {
                    if (photos.get(history.getUpdater().getPhoto().getObjectID()) != null) {
                        historyList.setUpdaterImageURL(photos.get(history.getUpdater().getPhoto().getObjectID()));
                    } else {
                        photos.put(history.getUpdater().getPhoto().getObjectID(), getImageUrl(history.getUpdater().getPhoto().getObjectID()));
                        historyList.setUpdaterImageURL(photos.get(history.getUpdater().getPhoto().getObjectID()));
                    }
                }


                histories.add(historyList);
            }
        }
        return histories.toArray(new CrmHistoryList[]{});
    }

    private CrmHistoryList getHistoryAsRPC(CrmHistory caseHistory) {
        CrmHistoryList historyList = new CrmHistoryList();
        historyList.setObjectID(caseHistory.getEntityID());
        historyList.setUpdaterID(caseHistory.getUpdater() != null ? caseHistory.getUpdater().getObjectID() : 0);
        if (caseHistory.isSuperUser()) {
            historyList.setUpdater(Constants.defaultSupportName);
        } else {
            historyList.setUpdater(caseHistory.getUpdater() != null ? caseHistory.getUpdater().getName() : "Anonymous");
        }
        historyList.setCreationTime(caseHistory.getCreationTime());
        String message = "";
        if (caseHistory.getMessage() != null) {
            message = caseHistory.getMessage().equals("Created the contact") ? commonLocalizer.localize(PdfLocalizationName.contactCreated, "Created the contact") : caseHistory.getMessage();
            message = caseHistory.getMessage().equals("Updated the contact") ? commonLocalizer.localize(PdfLocalizationName.contactUpdated, "Updated the contact") : message;
            message = caseHistory.getMessage().equals("Contact deleted") ? commonLocalizer.localize(PdfLocalizationName.contactDeleted, "Contact deleted") : message;
        }
        historyList.setMessage(message);
        return historyList;
    }

    @Transactional
    public void updateCaseStatus(Integer caseID, Integer statusID, String note) {
        if (caseID != null) {
            EdsCase crmCase = caseManager.get(caseID);
            crmCase.clear();
            if (statusID != null) {

                if (note != null) {
                    EdsNoteHistory edsNote = new EdsNoteHistory();
                    edsNote.setEmployee(userManager.getUser());
                    edsNote.setComment(note);
                    edsNote.setEventDate(new Date());
                    edsNote.setRelatedId(crmCase.getObjectID());
                    edsNote.setRelatedTo(EdsNoteHistory.getRelatedToByEntityType(CrmConstants.CRM_CASE));
                    edsNote.setSuperUser(ServerUtils.isSuperUser());
                    noteHistoryManager.createOrUpdate(edsNote);

                    crmCase.setNote(note);
                } else {
                    crmCase.setNote("");
                }
                EdsReference newStatus = referenceManager.get(statusID);
                if (crmCase.getStatus() == null || !crmCase.getStatus().getObjectID().equals(statusID)) {
                    updateCaseHistory("Changed the case status to " + referenceWfmMessageSource.localizeRef(newStatus), crmCase);
                }
                crmCase.setStatus(newStatus);
            } else {
                crmCase.setStatus(null);
            }
            caseManager.update(crmCase, true);
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, crmCase, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_CASE);
        }
    }

    @Override
    @Transactional
    public Boolean updateCases(Integer iDOfField, ArrayList<Integer> ids, String type) {
        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        if (iDOfField != null && type != null) {
            if (ids != null && ids.size() > 0) {
                ArrayList<Integer> solrIDs = new ArrayList<>();
                for (Integer id : ids) {
                    EdsCase crmCase = caseManager.get(id);
                    if (crmCase != null) {
                        CaseItem caseItem = crmCase.getRPC(null, false);
                        //get custom fields
                        caseItem.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(crmCase.getCustomFields(),
                                commonService.getCompanyCustomFields(ViewName.CrmCase)));
                        if (CaseItem.STATUS.equals(type)) {
                            caseItem.setStatus(new ReferenceItem(iDOfField));
                        }
                        if (CaseItem.PRIORITY.equals(type)) {
                            caseItem.setPriorityId(iDOfField);
                        }
                        if (CaseItem.CASE_TYPE.equals(type)) {
                            caseItem.setTypeId(iDOfField);
                        }
                        if (CaseItem.CASE_REASON.equals(type)) {
                            caseItem.setCaseReasonId(iDOfField);
                        }
                        if (CaseItem.ASSIGNED_TO.equals(type)) {
                            caseItem.setDepartmentID(null);
                            caseItem.setCaseAssigneeId(iDOfField);
                        }
                        if (CaseItem.ASSIGNED_TO_DEPARTMENT.equals(type)) {
                            caseItem.setCaseAssigneeId(null);
                            caseItem.setDepartmentID(iDOfField);
                        }
                        if (CaseItem.INTERNAL_STATUS.equals(type)) {
                            caseItem.setInternalStatusId(iDOfField);
                        }
                        solrIDs.add(saveCase(caseItem, false).getId());
                    }
                }
                if (!solrIDs.isEmpty()) {
                    List<EdsCase> cases = caseManager.getCasesByIDs(solrIDs);
                    if (!cases.isEmpty()) {
                        try {
                            caseSolrComponent.indexes(cases);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    @Transactional
    public Boolean updateOpportunities(Integer iDOfField, ArrayList<Integer> ids, String type) {
        if (iDOfField != null && type != null) {
            if (ids != null && ids.size() > 0) {
                ArrayList<Integer> solrIDs = new ArrayList<>();
                for (Integer id : ids) {
                    EdsOpportunity opportunity = opportunityManager.get(id);
                    if (opportunity != null) {
                        if (OpportunityListItem.ASSIGNEE_NAME.equals(type)) {
                            EdsEmployee assignee = employeeManager.get(iDOfField);
                            if (assignee != null) {
                                opportunity.setAssignee(assignee);
                            }
                        }
                        opportunityManager.createOrUpdate(opportunity);
                        solrIDs.add(opportunity.getObjectID());
                    }
                }

                if (solrIDs.size() > 0) {
                    List<EdsOpportunity> opportunities = opportunityManager.getOpportunityByIds(ServerUtils.getAsCommoDelimited(solrIDs, "0", ","));
                    if (opportunities.size() > 0) {
                        try {
                            solrManager.addOpportunityToIndex(opportunities.toArray(new EdsOpportunity[]{}));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Transactional
    public Boolean convertTrashToCase(Integer trashID) {
        if (trashID != null && !"".equals(trashID)) {
            EdsCase trash = caseManager.get(trashID);
            if (trash != null) {
                generateCaseNumber(trash);
                trash.setInTrash(false);
                try {
                    /*solrManager.indexAddCase(trash);*/
                    caseSolrComponent.index(trash);
                } catch (Exception e) {
                    baseEventPostProcessor.registerEvent(CrmCaseEventListeneImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, trash, userManager.getUser());
                    e.printStackTrace();
                }
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    @Transactional
    public void updateCompanyWebForms(Integer companyId) {
        List<EdsCompany> companies = new ArrayList<>();
        if (companyId != null) {
            EdsCompany company = companyManager.get(companyId);
            if (company != null) {
                companies.add(company);
            }
        } else {
            companies = companyManager.getCompanies();
        }
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (company.hasSchema(schemas)) {
                ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());
                updateWebForms();
                ServerSecurityContext.getInstance().removeCompanyId();
            }
        }
    }

    @Transactional
    public Integer saveWebForm(WebForm webForm) {
        EdsWebForm edsWebForm = new EdsWebForm();
        if (webForm.getObjectId() != null) {
            List<EdsWebField> webFields = webFieldManager.getByWebFormID(webForm.getObjectId());
            for (EdsWebField edsWebField : webFields) {
                edsWebField.setWebForm(null);
                webFieldManager.delete(edsWebField);
            }
            edsWebForm = webFormManager.get(webForm.getObjectId());
        }
        EdsUser user = userManager.getUser();
        edsWebForm.setTitle(webForm.getTitle());
        edsWebForm.setEdsCompany(user.getCompany());
        edsWebForm.setOwner(employeeManager.get(user.getObjectID()));
        edsWebForm.setConfirmationMessage(webForm.getConfirmationMessage());
        edsWebForm.setRedirectURL(webForm.getRedirectURL());
        edsWebForm.setDescription(webForm.getDescription());
        edsWebForm.setEmailAddress(webForm.getEmailAddress());
        edsWebForm.setButtonText(webForm.getButtonText());
        edsWebForm.setUseCaptcha(webForm.getUseCatpcha());
        edsWebForm.setCaptchaLabel(webForm.getCaptchaLabel());
        edsWebForm.setCaptchaDescription(webForm.getCaptchaDescription());
        edsWebForm.setCaptchaCantRead(webForm.getCaptchaCantRead());
        edsWebForm.setCaptchaTryAnotherLink(webForm.getCaptchaTryAnother());
        edsWebForm.setSendAutoResponse(webForm.isSendAutoResponse());
        edsWebForm.setEmailTemplateID(webForm.getEmailTemplateID());
        edsWebForm.setLastUpdatedTime(new Date());
        if (webForm.getWebFormType() != null) {
            EdsReference type = referenceManager.get(webForm.getWebFormType().getId());
            edsWebForm.setType(type);
        }
        //save web form source
        if (edsWebForm.getWebFormSource() != null) {
            EdsReference webFormSource = edsWebForm.getWebFormSource();
            webFormSource.setName("Web Form - " + webForm.getTitle());
            referenceManager.update(webFormSource);
        } else {
            String sourceCode = null;
            if (WebFormConstants.LEAD_FORM.equals(webForm.getWebFormType(true))) {
                sourceCode = EdsCrmContact._LEAD_SOURCE;
            } else if (WebFormConstants.CASE_FORM.equals(webForm.getWebFormType(true))) {
                sourceCode = EdsCase._CASE_ORIGIN;
            } else if (WebFormConstants.CANDIDATE_FORM.equals(webForm.getWebFormType(true))) {
                sourceCode = EdsCrmContact._CANDIDATE_SOURCE;
            }
            if (sourceCode != null) {
                EdsReference webFormSource = new EdsReference();
                webFormSource.setCode("WEB_FORM_" + webForm.getTitle().trim().replace(" ", "_"));
                webFormSource.setName("Web Form - " + webForm.getTitle());
                EdsReference parentReference = referenceManager.findReferenceByCode(sourceCode);
                if (parentReference != null) {
                    webFormSource.setParent(parentReference);
                }
                referenceManager.create(webFormSource);
                if (webFormSource.getObjectID() != null) {
                    edsWebForm.setWebFormSource(webFormSource);
                }
            }
        }
        boolean newCreated = webFormManager.createOrUpdate(edsWebForm);
        edsWebForm = webFormManager.get(edsWebForm.getObjectID());
        if (edsWebForm.getType() != null) {
            if (WebFormConstants.CASE_FORM.equals(edsWebForm.getType().getCode()) || WebFormConstants.LEAD_FORM.equals(edsWebForm.getType().getCode()) ||
                    WebFormConstants.CANDIDATE_FORM.equals(edsWebForm.getType().getCode())) {
                for (WebField webField : webForm.getWebFields()) {
                    EdsWebField edsWebField = new EdsWebField();
                    edsWebField.setSavingField(webField.getSavingField());
                    edsWebField.setGroupTitle(webField.getGroupTitle());
                    edsWebField.setOriginalLabel(webField.getOriginalLabel());
                    edsWebField.setLabel(webField.getLabel());
                    edsWebField.setType(webField.getType());
                    edsWebField.setShowInForm(webField.isShowInForm());
                    edsWebField.setMandatory(webField.isMandatory());
                    edsWebField.setUnchangable(webField.isUnchangable());
                    edsWebField.setDefaultValue((String) webField.getDefaultValue(true));
                    edsWebField.setWebForm(edsWebForm);
                    edsWebField.setDrawLine(webField.isDrawLine());
                    edsWebField.setSortOrder(webField.getSortOrder());
                    edsWebField.setCustomField(webField.isCustomField());
                    edsWebField.setOnlyIntegerAllowed(webField.isOnlyIntegerAllowed());
                    webFieldManager.create(edsWebField);
                }
            }
            if (webForm.getCustomForm() != null) {
                EdsLayout edsLayout = null;
                LayoutRPC layout = webForm.getCustomForm();
                if (layout.getObjectID() != null) {
                    edsLayout = layoutManager.get(SecurityContext.getCompanyID(), layout.getObjectID());
                }
                if (edsLayout == null) {
                    edsLayout = new EdsCustomLayout();
                }
                edsLayout.setLayout(layout.getLayout());
                edsLayout.setWebForm(true);
                edsLayout.setFormID(LayoutRPC.WEB_FORM);
                edsLayout.setAddForm(true);
                edsLayout.setTitle(edsWebForm.getTitle());
                edsLayout.setActive(true);
                edsLayout.setCustomCss(layout.getCustomCss());
                if (edsWebForm.getType() != null) {
                    if (WebForm.LEAD_FORM.equals(edsWebForm.getType().getCode())) {
                        edsLayout.setFormID(LayoutRPC.LEAD_FORM);
                    } else if (WebForm.CASE_FORM.equals(edsWebForm.getType().getCode())) {
                        edsLayout.setFormID(LayoutRPC.CASE_FORM);
                    } else if (WebForm.CANDIDATE_FORM.equals(edsWebForm.getType().getCode())) {
                        edsLayout.setFormID(LayoutRPC.CANDIDATE_FORM);
                    }
                }
                layoutManager.createOrUpdate(edsLayout);
                edsWebForm.setLayoutID(edsLayout.getObjectID());
                edsWebForm.setCustomLayout(true);
            } else {
                edsWebForm.setLayoutID(null);
                edsWebForm.setCustomLayout(false);
            }
        }
        baseEventPostProcessor.registerEvent(CrmWebFormEventListenerImpl.TYPE, (newCreated ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT), edsWebForm, user);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsWebForm.class.getSimpleName());
        if (edsWebForm.getObjectID() != null) {
            kpiLog.setEntityId(edsWebForm.getObjectID());
        }
        if (newCreated) {
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(log, kpiLog, "Add new WebForm");
        } else {
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            ServerUtils.kpiLog(log, kpiLog, "Update WebForm");
        }
        return edsWebForm.getObjectID();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WebForm getWebForm(Integer objectID) {
        EdsWebForm edsWebForm = webFormManager.get(objectID);
        if (edsWebForm != null) {
            WebForm item = edsWebForm.getRPC(false);
            item.setCustomForm(initWebFormLayout(edsWebForm));
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsWebForm.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.VIEW);
            kpiLog.setEntityId(objectID);
            ServerUtils.kpiLog(log, kpiLog, "View WebForm");
            return item;
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WebForm editWebForm(Integer objectID, String type) {
        EdsWebForm edsWebForm = objectID != null ? webFormManager.get(objectID) : null;
        type = type == null && edsWebForm != null && edsWebForm.getType() != null ? edsWebForm.getType().getCode() : type;
        ViewName viewName = WebFormConstants.LEAD_FORM.equals(type) ? ViewName.Lead : (WebFormConstants.CASE_FORM.equals(type) ? ViewName.CrmCase : ViewName.Candidate);
        WebForm webForm = new WebForm();
        webForm.setCustomFields(commonService.getCompanyCustomFields(viewName));
        webForm.setFormTypes(getAsSelectItem(referenceManager.listReferences(WebFormConstants.WEB_FORM), ServerUtils.REFERENCE));
        webForm.setEmailTemplates(getEmailTemplates(CRM_WEB_FORM_CATEGORY));
        if (edsWebForm != null) {
            webForm = edsWebForm.getRPC(false, webForm);
            webForm.setCustomForm(initWebFormLayout(edsWebForm));
        } else {
            webForm.setCaptchaCantRead("Can't Read text");
            webForm.setCaptchaLabel("Word Verification");
            webForm.setCaptchaDescription("Type the characters you see in the picture below.");
            webForm.setCaptchaTryAnother("Try Another.");
        }
        return webForm;
    }

    private LayoutRPC initWebFormLayout(EdsWebForm edsWebForm) {
        if (edsWebForm != null && edsWebForm.getLayoutID() != null) {
            EdsLayout layout = layoutManager.get(edsWebForm.isCustomLayout() ? SecurityContext.getCompanyID() : null, edsWebForm.getLayoutID());
            if (layout != null) {
                return layout.getRPC();
            }
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashMap<String, SelectItem[]> fillDropDowns(String form) {
        HashMap<String, SelectItem[]> map = new HashMap<>();

        List<CompanyCustomFieldItem> customFields = null;
        if (WebFormConstants.LEAD_FORM.equals(form)) {
            ListingFilterParameter fp = new ListingFilterParameter();
//            fp.setCompanyID(companyID);
            map.put(WebFormConstants.DROPDOWNITEMS_ASSIGNEES, getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE));
            map.put(WebFormConstants.DROPDOWNITEMS_COUNTIRES, getCountryList());
            map.put(WebFormConstants.DROPDOWNITEMS_STATES, getRegionList());
            map.put(WebFormConstants.DROPDOWNITEMS_SOURCES, getAsSelectItem(referenceManager.listReferences(EdsCrmContact._LEAD_SOURCE), ServerUtils.REFERENCE));
            map.put(WebFormConstants.DROPDOWNITEMS_CAMPAIGNS, getAsSelectItem(campaignManager.getCampaignList(fp), ServerUtils.CRM_CAMPAIGN));
            map.put(WebFormConstants.DROPDOWNITEMS_STATUSES, getAsSelectItem(referenceManager.listReferences(EdsCrmContact._LEAD_STATUS), ServerUtils.REFERENCE));
            map.put(WebFormConstants.DROPDOWNITEMS_INDUSTRIES, getAsSelectItem(referenceManager.listReferences(_COMPANY_WORKAREA), ServerUtils.REFERENCE));
            map.put(WebFormConstants.DROPDOWNITEMS_RATINGS, getAsSelectItem(referenceManager.listReferences(EdsCrmContact._LEAD_RATING), ServerUtils.REFERENCE));
            map.put(WebFormConstants.DROPDOWNITEMS_NUMBER_OF_EMPLOYEES, getAsSelectItem(referenceManager.listReferences(Constants.NUMBER_OF_EMPLOYEES), ServerUtils.REFERENCE));
            map.put(WebFormConstants.DROPDOWNITEMS_ANNUAL_REVENUES, getAsSelectItem(referenceManager.listReferences(Constants.ANNUAL_REVENUE), ServerUtils.REFERENCE));
            customFields = commonService.getCompanyCustomFields(ViewName.Lead);
        } else if (WebFormConstants.CASE_FORM.equals(form)) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setListEmployees(true);
            fp.setListDepartments(false);
            fp.setResignedEmployeesIncluded(false);
            map.put(WebFormConstants.DROPDOWNITEMS_ASSIGNEES, allInOneServiceLocal.getEmployeesAsSelectItem(null, fp));
            map.put(WebFormConstants.DROPDOWNITEMS_CASEORIGINS, getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_ORIGIN), ServerUtils.REFERENCE));
            map.put(WebFormConstants.DROPDOWNITEMS_STATUSES, getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_STATUS), ServerUtils.REFERENCE));
            map.put(WebFormConstants.DROPDOWNITEMS_TYPES, getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_TYPE), ServerUtils.REFERENCE));
            map.put(WebFormConstants.DROPDOWNITEMS_PRIORITIES, getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_PRIORITY), ServerUtils.REFERENCE));
            map.put(WebFormConstants.DROPDOWNITEMS_CASEREASONS, getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_REASON), ServerUtils.REFERENCE));
            map.put(WebFormConstants.DROPDOWNITEMS_RESOLVERS, map.get(WebFormConstants.DROPDOWNITEMS_ASSIGNEES));
            customFields = commonService.getCompanyCustomFields(ViewName.CrmCase);
        } else if (WebFormConstants.OPPORTUNITY_FORM.equals(form)) {
            map.put(WebFormConstants.DROPDOWNITEMS_ASSIGNEES, getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE));
            map.put(WebFormConstants.DROPDOWNITEMS_STAGES, getOpportunityStages(true));
            customFields = commonService.getCompanyCustomFields(ViewName.Opportunity);
        } else if (WebFormConstants.CANDIDATE_FORM.equals(form)) {
            map.put(WebFormConstants.DROPDOWNITEMS_OWNERS, getOwnersListByPermission(PermissionConstants.HRMS_SHOW_IN_CANDIDATE_OWNER));
            map.put(WebFormConstants.DROPDOWNITEMS_SOURCES, getAsSelectItem(referenceManager.listReferences(EdsCrmContact._CANDIDATE_SOURCE), ServerUtils.REFERENCE));
            map.put(WebFormConstants.DROPDOWNITEMS_STATUSES, getAsSelectItem(referenceManager.listReferences(EdsCrmContact._CANDIDATE_STATUS), ServerUtils.REFERENCE));
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setBriefly(false);
            List<EdsVacancy> list = vacancyManager.list(fp);
            if (list != null && list.size() > 0) {
                ArrayList<SelectItem> vacanciesList = new ArrayList<>();
                for (EdsVacancy vacancy : list) {
                    vacanciesList.add(vacancy.getAsSelectItem());
                }
                map.put(WebFormConstants.DROPDOWNITEMS_VACANCIES, vacanciesList.toArray(new SelectItem[]{}));
            }
            map.put(WebFormConstants.DROPDOWNITEMS_LOCATIONS, locationManager.getLocationsAsSelectItems(new ListingFilterParameter()));
            customFields = commonService.getCompanyCustomFields(ViewName.Candidate);
        }
        if (customFields != null && customFields.size() > 0) {
            SelectItem[] customFieldValues = new SelectItem[customFields.size()];
            int i = 0;
            for (CompanyCustomFieldItem customField : customFields) {
                customFieldValues[i++] = new SelectItem(customField.getObjectId(), customField.getPredefinedValues() != null ? customField.getPredefinedValuesWithSorting() : null);
            }
            map.put(WebFormConstants.DROPDOWNITEMS_CUSTOMFIELDS, customFieldValues);
        }
        return map;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<WebForm> getWebForms(ListingFilterParameter filterParametr) {
        filterParametr = initFilterParametrs(filterParametr);
        List<EdsWebForm> webForms = webFormManager.list(filterParametr);
        int totalCount = webFormManager.getListCount(filterParametr);
        ArrayList<WebForm> result = new ArrayList<>();
        for (EdsWebForm edsWebForm : webForms) {
            result.add(edsWebForm.getRPC(true));
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsWebForm.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get WebForm list");
        return new ListResult<>(result, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TypeItem[] getInvoicesOrQuotes(ListingFilterParameter fp) {
        if (fp.getContactID() != null) {
            fp.setClientContactId(fp.getContactID());
        }
        if (fp.getClientContactId() != null || fp.getClientId() != null || fp.getOpportunityID() != null) {
            InvoiceList invoiceList = null;
            fp.setWithEncryptedLink(true);
            if (fp.isInvoicesOnly()) {
                ListingFilterParameter lfp = new ListingFilterParameter();
                lfp.setWithEncryptedLink(true);
                lfp.setCrmContactId(fp.getContactID());
                lfp.setClientId(fp.getClientId());
                invoiceList = invoiceServiceLocal.getSaleInvoiceData(lfp);
            } else if (fp.isQuotesOnly()) {
                invoiceList = quoteService.getSaleQuoteData(fp);
            }
            return invoiceList != null ? invoiceList.getAsTypeItem(0) : null;
        }
        return null;
    }

    @Transactional
    public void indexCompanyCrmCase(SolrReindexRpc solrReindex) {
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
                profileService.clearFromDbDeletedCustomFieldsByFormId(LayoutRPC.CASE_FORM, null, false);
                solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.PRODUCTS_SERVICES);
                solrDbConsistencyManager.flushAndClear();
                if (nextCompanyID != null && schemas.contains(nextCompanyID.toString())) {
                    ServerSecurityContext.getInstance().setCompanyId(solrReindex.getCompanyId());
//                    SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));
                    try {
                        if (solrReindex.isAllReindex()) {
                            solrManager.removeCompanyCase(solrReindex.getCompanyId());
                        } else if (solrReindex.getLastUpdateTime() != null) {
                            List<Integer> deletedCaseIds = caseManager.getCompanyDeletedCaseForSolr(solrReindex);
                            solrManager.removeCompanyCaseByIds(deletedCaseIds.toArray(new Integer[]{}));
                        }
                    } catch (Exception e) {
                        log.error("Error Case Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
                    }

                    int startAt = 0;
                    int chunkSize = 1000; // DO NOT CHANGE THE CHUNK SIZE
                    List<EdsCase> caseList = caseManager.getCompanyCaseListForSolr(solrReindex, startAt, chunkSize);

                    while (Objects.nonNull(caseList) && !caseList.isEmpty()) {
                        try {
                            caseSolrComponent.indexConcurrently(caseList);
                        } catch (InterruptedException e) {
                            log.error("Error Case Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
                        }
                        caseManager.flushAndClear();
                        startAt++;
                        caseList = caseManager.getCompanyCaseListForSolr(solrReindex, (startAt * chunkSize), chunkSize);
                    }
                    caseManager.flushAndClear();
                }
            }
        }
    }

    @Transactional
    public Integer indexCompanyCrmCase(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        List<EdsCase> caseList = caseManager.getCompanyCaseForSolr(solrReindex, start, limit);
        if (!caseList.isEmpty()) {
            try {
                caseSolrComponent.indexes(caseList);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return caseList.get(caseList.size() - 1).getObjectID();
        } else {
            return -1;
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Integer> getCRMEntityIDs(String entityType, ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        fp.setIDsOnly(true);
        Set<Integer> ids = new HashSet<>();
        EdsUser user = userManager.getUser();
        boolean stop = false;
        ListLoadConfig config = new ListLoadConfig();
        int start = 0;
        final int limit = fp.getLimit();
        config.setLimit(limit);

        Page<ContactSolrDoc> contactSolrDocPage = null;
        Page<CrmAccountSolrDoc> crmAccountSolrDocPage = null;

        do {
            if (CrmConstants.CRM_CONTACT.equals(entityType) || CrmConstants.CRM_LEAD.equals(entityType)) {
                contactSolrDocPage = CrmConstants.CRM_LEAD.equals(entityType) ? contactSolrComponent.getLeadList(fp, config) : contactSolrComponent.getList(fp, config, user);
            } else if (CrmConstants.CRM_ACCOUNT.equals(entityType)) {
                crmAccountSolrDocPage = crmAccountSolrComponent.getCrmAccountList(fp, config);
            }

            List<Integer> contactOrAccountIds = null;
            if (contactSolrDocPage != null && contactSolrDocPage.getContent() != null && contactSolrDocPage.getContent().size() > 0) {
                contactOrAccountIds = contactSolrDocPage.getContent().stream().map(ContactSolrDoc::getContactId).collect(Collectors.toList());
            } else if (crmAccountSolrDocPage != null && crmAccountSolrDocPage.getContent() != null && crmAccountSolrDocPage.getContent().size() > 0) {
                contactOrAccountIds = crmAccountSolrDocPage.getContent().stream().map(CrmAccountSolrDoc::getCrmAccountId).collect(Collectors.toList());
            }
            if (contactOrAccountIds != null && contactOrAccountIds.size() > 0) {
                ids.addAll(contactOrAccountIds);
                stop = contactOrAccountIds.size() < limit || !fp.isAllByFilter();
                start += limit;
                config.setStart(start);
            } else {
                stop = true;
            }
        } while (!stop);
        return Arrays.asList(ids.toArray(new Integer[]{}));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmailTemplates(String templateCategory) {
        return emailTemplateServiceLocal.getEmailTemplates(templateCategory);
    }

    @Override
    public SmsSendItem getSMSItem(String moduleType) {
        SmsSendItem item = new SmsSendItem();
        ArrayList<SelectItem> smsTemplates = new ArrayList<>();
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setModule(moduleType);
        List<EdsSMSTemplates> templates = smsTemplateManager.getSMSTemplates(filterParameter);
        if (templates != null && templates.size() > 0) {
            for (EdsSMSTemplates template : templates) {
                if (template.isDefault()) {
                    item.setDefaultSmsTemplate(template.getAsSelectItem());
                }
                smsTemplates.add(template.getAsSelectItem());
            }
        }
        item.setTemplates(smsTemplates.toArray(new SelectItem[]{}));
        List<EdsSmsSettings> smsSettingses = smsManager.list(new ListingFilterParameter());
        ArrayList<SelectItem> sms = new ArrayList<>();
        for (EdsSmsSettings s : smsSettingses) {
            sms.add(new SelectItem(s.getObjectID(), s.getName()));
        }
        item.setProviders(sms.toArray(new SelectItem[]{}));
        item.setCustomForms(commonService.getCustomForms());
        return item;
    }

    @Override
    public void saveCaseEditCellValue(CaseItem rowValue, String columnCode) {
        EdsCase edsCase = caseManager.get(rowValue.getObjectId());
        //employee custom field
        edsCase.clear();
        EdsCrmCustomFields edsEmployeeCustomFields = edsCase.getCustomFields();
        if (edsEmployeeCustomFields == null) {
            edsEmployeeCustomFields = new EdsCrmCustomFields();
            crmCustomFieldsManager.create(edsEmployeeCustomFields);
            edsCase.setCustomFields(edsEmployeeCustomFields);
        }
        Object ob = CustomFieldsUtils.getObjectValue(edsEmployeeCustomFields, columnCode);
        if (ob != null) {
            if (ob instanceof String text) {
                if (!text.equals(rowValue.getCustomFieldsMap().get(columnCode))) {
                    edsCase.addChange(columnCode);
                }
            } else if (ob instanceof Number) {
                String text = String.valueOf(((Double) ob).intValue());
                if (!text.equals(rowValue.getCustomFieldsMap().get(columnCode))) {
                    edsCase.addChange(columnCode);
                }
            } else if (ob instanceof Date date) {
                if (!date.equals(rowValue.getCustomFieldsMap().get(columnCode))) {
                    edsCase.addChange(columnCode);
                }
            }
        } else {
            edsCase.addChange(columnCode);
        }
        CustomFieldsUtils.setDomenObjectFieldChange(edsEmployeeCustomFields, rowValue.getCustomFieldsMap(), columnCode);

        edsCase.getAuditInfo().setModificationDate(new Date());
        edsCase.getAuditInfo().setModifiedBy(userManager.getUser());
        caseManager.update(edsCase, true);
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsCase, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_CASE);
    }

    @Override
    public void deleteCampaigns(ArrayList<Integer> ids) {
        campaignManager.setCampaignsDeletedTrue(ServerUtils.getAsCommoDelimited(ids, "0", ","));
        for (Integer id : ids) {
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsCampaign.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.DELETE);
            kpiLog.setEntityId(id);
            ServerUtils.kpiLog(log, kpiLog, "Campaign deleted");
        }
    }

    public Boolean updateOpportunity(EdsOpportunity opportunity) {
        try {
            opportunityManager.update(opportunity);
            opportunitySolrComponent.index(opportunity);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean updateOpportunity(EdsOpportunity opportunity, boolean withoutUpdateAuditLog) {
        try {
            opportunityManager.update(opportunity, withoutUpdateAuditLog);
            solrManager.addOpportunityToIndex(opportunity);
            return true;
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Transactional
    public SelectItem saveCase(final CaseItem item, boolean isFromWebForm) {
        SelectItem selectItem = new SelectItem();
        EdsCase crmCase = new EdsCase();
        EdsWebForm webForm = null;
        if (isFromWebForm) {
            webForm = webFormManager.get(item.getWebFormID());
        }
        boolean caseUpdate = false;
        if (item.getObjectId() != null) {
            crmCase = caseManager.get(item.getObjectId());
        }
        if (crmCase == null) {
            crmCase = new EdsCase();
        }
        crmCase.clear();
        EdsUser user = caseManager.getUser();
        if (crmCase.isNew() && "".equals(item.getReportedBy()) && user != null && user.isClientContact() && user.getClientContact().getCrmContact() != null) {
            item.setCrmContactID(user.getClientContact().getCrmContact().getObjectID());
        }
        EdsEmailDetails caseDetails;
        if (crmCase.getCrmCaseDetails() == null) {
            caseDetails = new EdsEmailDetails();
            crmCase.setCrmCaseDetails(caseDetails);
        } else {
            caseDetails = crmCase.getCrmCaseDetails();
        }
        crmCase.setWebFormID(item.getWebFormID());
        if (webForm != null) {
            crmCase.setWebFormID(webForm.getObjectID());
        }
        crmCase.setEntityID(item.getEntityID());
        if (item.getTypeId() != null) {
            crmCase.setType(referenceManager.get(item.getTypeId()));
        } else {
            crmCase.setType(null);
        }
        crmCase.setFilterID(item.getFilterID());
        if (item.getCaseOriginId() != null) {
            crmCase.setCaseOrigion(referenceManager.get(item.getCaseOriginId()));
        } else {
            if (webForm != null && webForm.getWebFormSource() != null) {
                crmCase.setCaseOrigion(webForm.getWebFormSource());
            } else {
                crmCase.setCaseOrigion(null);
            }
        }
        if (item.getCcEmails() != null && !"".equals(item.getCcEmails())) {
            caseDetails.settoCC(item.getCcEmails());
        }
        //reportedBy Contact
        if (item.getCrmContactID() != null) {
            crmCase.setCrmContact(crmContactManager.get(item.getCrmContactID()));
        } else {
            crmCase.setCrmContact(null);
        }
        //reportedBy Lead
        if (item.getLeadId() != null) {
            crmCase.setLead(crmContactManager.get(item.getLeadId()));
        } else {
            crmCase.setLead(null);
        }
        //reportedBy Account
        if (item.getAccountId() != null) {
            crmCase.setCrmAccount(crmAccountManager.get(item.getAccountId()));
        } else {
            crmCase.setCrmAccount(null);
        }
        crmCase.setReplyTo(item.getReplyTo());
        if (crmCase.getCrmAccount() == null && crmCase.getCrmContact() == null && crmCase.getLead() == null) {
            if (!"".equals(item.getEmail()) && !"".equals(item.getCompany()) && !"".equals(item.getLastName())) {
                createNewLeadForCase(item, isFromWebForm, crmCase, webForm);
            }
        }
        if (item.getPotentialId() != null) {
            crmCase.setOpportunity(opportunityManager.get(item.getPotentialId()));
        } else {
            crmCase.setOpportunity(null);
        }
        if (item.getCaseAssigneeId() != null) {
            if (item.getObjectId() != null && !caseUpdate) {
                if (crmCase.getAssignee() == null || !crmCase.getAssignee().getObjectID().equals(item.getCaseAssigneeId())) {
                    updateCaseHistory("Reassigned the case to " + userManager.get(item.getCaseAssigneeId()).getName(), crmCase);
                    caseUpdate = true;
                }
            }
            EdsUser edsAssignedUser = userManager.get(item.getCaseAssigneeId());
            crmCase.setAssignee(edsAssignedUser);
        } else {
            crmCase.setAssignee(null);
        }
        if (crmCase.getAssignee() != null && crmCase.getAssignee().isEmployee()) {
            EdsEmployee employee = employeeManager.get(crmCase.getAssignee().getObjectID());
            if (employee != null) {
                crmCase.setDepartment(employee.getTeam());
            }
        } else {
            crmCase.setDepartment(item.getDepartmentID() != null ? departmentManager.get(item.getDepartmentID()) : null);
        }
        if (item.getResolverId() != null) {
            if (item.getObjectId() != null && !caseUpdate) {
                if (crmCase.getResolver() != null && !crmCase.getResolver().getObjectID().equals(item.getResolverId())) {
                    updateCaseHistory("Changed the case resolver to " + employeeManager.get(item.getResolverId()).getName(), crmCase);
                    caseUpdate = true;
                }
            }
            crmCase.setResolver(userManager.get(item.getResolverId()));
        }
        if (item.getStatus().getId() != null) {
            if (item.getObjectId() != null && !caseUpdate) {
                if (crmCase.getStatus() != null && !crmCase.getStatus().getObjectID().equals(item.getStatus().getId())) {
                    EdsReference newStatus = referenceManager.get(item.getStatus().getId());
                    updateCaseHistory("Changed the case status to " + referenceWfmMessageSource.localizeRef(newStatus), crmCase);
                    caseUpdate = true;
                }
            }
            crmCase.setStatus(referenceManager.get(item.getStatus().getId()));
        } else {
            crmCase.setStatus(null);
        }
        if (item.getPriorityId() != null) {
            if (item.getObjectId() != null && !caseUpdate) {
                if (crmCase.getPriority() != null && !crmCase.getPriority().getObjectID().equals(item.getPriorityId())) {
                    updateCaseHistory("Changed the case priority to " + referenceManager.get(item.getPriorityId()).getName(), crmCase);
                    caseUpdate = true;
                }
            }
            crmCase.setPriority(referenceManager.get(item.getPriorityId()));
        } else {
            crmCase.setPriority(null);
        }
        if (item.getCaseReasonId() != null) {
            EdsReference reference = referenceManager.get(item.getCaseReasonId());
            crmCase.setCaseReason(reference);
            if (reference != null && EdsCase.OTHER_REASON.equals(reference.getCode())) {
                crmCase.setOtherReason(item.getOtherReason());
            }
        } else {
            crmCase.setCaseReason(null);
        }

        crmCase.setSubject(item.getSubject());
        if (item.getDescription() != null) {
            caseDetails.setDescription(item.getDescription());
        }
        if (crmCase.isNew()) {
            if (caseDetails.isNew()) {
                jpaTemplate.persist(caseDetails);
            }
        }
        if (item.getTrackerID() != null) {
            crmCase.setTracker(emailTrackerManager.get(item.getTrackerID()));
        }
        if (crmCase.getTracker() == null) {
            crmCase.setTracker(emailTrackerService.createTracker(null));
        }
        if (crmCase.getEntityID() == null) {
            createEntity(crmCase);
        }
        if (item.isInTrash()) {
            crmCase.setInTrash(item.isInTrash());
        }
        if (item.getInternalStatusId() != null) {
            if (crmCase.getInternalStatus() != null && !crmCase.getInternalStatus().getObjectID().equals(item.getInternalStatusId())) {
                crmCase.setInternalUpdatedDate(new Date());
            } else if (crmCase.getInternalStatus() == null) {
                crmCase.setInternalUpdatedDate(new Date());
            }
            crmCase.setInternalStatus(referenceManager.get(item.getInternalStatusId()));
        } else {
            if (crmCase.getInternalStatus() != null) {
                crmCase.setInternalUpdatedDate(new Date());
            }
            crmCase.setInternalStatus(null);
        }
        if (item.getObjectId() != null && !caseUpdate) {
            updateCaseHistory(commonLocalizer.localize("updatedCase", "Updated the case"), crmCase);
        }

        if (crmCase.getInTrash()) {
            crmCase.setCaseNumberString(null);
        } else if (crmCase.getCaseNumberString() == null || "".equals(crmCase.getCaseNumberString())) {
            generateCaseNumber(crmCase);
        }
        if (item.isAddingFromWebForms()) {
            ArrayList<CompanyCustomFieldItem> customFields = item.getCustomFields();
            ArrayList<CompanyCustomFieldItem> newCustomFields = new ArrayList<>();
            if (customFields != null && customFields.size() > 0) {
                for (CompanyCustomFieldItem customFieldItem : customFields) {
                    EdsCompanyCustomFieldsSettings edsCompanyCustomFieldSetting = companyCFSettingsManager.get(customFieldItem.getObjectId());
                    if (edsCompanyCustomFieldSetting != null) {
                        CompanyCustomFieldItem newCustomFieldItem = edsCompanyCustomFieldSetting.getRPC(null);
                        newCustomFieldItem.setObjectId(null);
                        newCustomFieldItem.setFieldStringValue(customFieldItem.getFieldStringValue());
                        newCustomFieldItem.setFieldDateNonConvertedValue(customFieldItem.getFieldDateNonConvertedValue());
                        newCustomFields.add(newCustomFieldItem);
                    }
                }
                item.setCustomFields(newCustomFields);
            } else {
                item.setCustomFields(null);
            }
        }

        crmCase.setBrandId(item.getBrandId());
        crmCase.setProductCategoryId(item.getProductCategoryId());
        crmCase.setProductId(item.getProductId());

        if (item.getCustomFields() != null && item.getCustomFields().size() > 0 && crmCase.getCustomFields() != null) {
            StringBuilder changesBuilder = new StringBuilder();
            for (CompanyCustomFieldItem cit : item.getCustomFields()) {
                changesBuilder.append(crmCase.getCustomFields() != null && CustomFieldsUtils.getObjectValue(crmCase.getCustomFields(), cit.getColumnCode()) != null ? getChanges(CustomFieldsUtils.getObjectValue(crmCase.getCustomFields(), cit.getColumnCode()), cit) : (cit.getColumnCode() + ","));
            }
            String changes = changesBuilder.toString();
            if (!"".equals(changes)) {
                crmCase.addCustomFieldChanges(changes);
            }
        }
        crmCase.setCustomFields(saveCustomFields(crmCase.getCustomFields(), item.getCustomFields()));

        //set kanbanboard order if its null
        if (crmCase.getKanbanOrder() == null) {
            Long minKanbanOrderInStatus = caseManager.getMinKanbanOrder(crmCase.getStatus() != null ? crmCase.getStatus().getObjectID() : null);
            if (minKanbanOrderInStatus == null) {
                minKanbanOrderInStatus = KANBAN_ORDER_GAP;
                crmCase.setKanbanOrder(minKanbanOrderInStatus);
            } else {
                crmCase.setKanbanOrder(minKanbanOrderInStatus - KANBAN_ORDER_GAP);
            }
        }

        boolean isNew = caseManager.createOrUpdate(crmCase);

        if (isNew) {
            updateCaseHistory(commonLocalizer.localize("createdCase", "Created the Case"), crmCase);
        }
        if (item.getAttachments() != null) {
            attachmentUtilsManager.saveAttachments(F_CASE, crmCase.getObjectID(), crmCase.getObjectID(), item.getAttachments());
        }
        saveCrmNotes(CrmConstants.CRM_CASE, crmCase.getObjectID(), item.getNotes());
        if (item.isRelationChanged()) {
            allInOneServiceLocal.saveRelations(RelationItem.TYPE_CASE, crmCase.getObjectID(), crmCase.getSubject(), item.getRelations());
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        if (isNew) {
            kpiLog.setEntityName(EdsCase.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            kpiLog.setEntityId(crmCase.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Add case");
        } else {
            kpiLog.setEntityName(EdsCase.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(crmCase.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Update case");
        }
        try {
//            solrManager.indexAddCase(crmCase);
            caseSolrComponent.index(crmCase);
            if (isNew) {
                solrTransactionManager.registerEvent(SolrEvent.CRM_CASE_ADD, crmCase, userManager.getUser().getCompany());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jpaTemplate.flush();
        if (isNew) {
            baseEventPostProcessor.registerEvent(TelegramChatEventListenerImpl.TYPE, TelegramConstants.SEND_CASE_CREATE, crmCase, user);
        }
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, isNew ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : (crmCase.getDeleted() != null && crmCase.getDeleted() ? BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT), crmCase, user);
        workflowEvent.setEntityType(RelationItem.TYPE_CASE);
        selectItem.setId(crmCase.getObjectID());
        if (crmCase.getCaseNumberString() != null) {
            selectItem.setNumber(crmCase.getCaseNumberString());
        }
        return selectItem;
    }


    @Override
    public void saveFeedBack(BugReportItem feedback) {
        EdsUser user = userManager.getUser();
        if (user != null) {
            EdsEmployee employee = user.getEmployee();
            if (employee.getContact() != null) {
                EdsCrmContact contact = employee.getContact();

                if (feedback != null) {
                    EdsReference reference = referenceManager.findReference(EdsCase._CASE_STATUS, EdsCase.NEW);

                    EdsCase feedbackcase = new EdsCase();

                    // Main fields
                    feedbackcase.setSubject(commonLocalizer.localize(EdsSubjects.BUG_REPORT_TITLE));
                    if (feedback.getSubjectText() != null && !"".equals(feedback.getSubjectText())) {
                        feedbackcase.setSubject(feedback.getSubjectText());
                    }

                    feedbackcase.setUserFeedBackAnonim(feedback.getIsAnonim());
                    feedbackcase.setStatus(reference);

                    if (feedback.getTypeId() != null) {
                        feedbackcase.setType(referenceManager.get(feedback.getTypeId()));
                    }

                    // ReportedBy Contact
                    feedbackcase.setCrmContact(crmContactManager.get(feedback.getReportedBy()));

                    EdsEmailDetails caseDetails = new EdsEmailDetails();
                    caseDetails.setDescription(feedback.getReportText());
                    feedbackcase.setCrmCaseDetails(caseDetails);
                    feedbackcase.setDescription(feedback.getReportText());
                    feedbackcase.setCrmContact(contact);

                    feedbackcase.setTracker(emailTrackerService.createTracker(null));
                    if (feedbackcase.getTracker() != null) {
                        feedbackcase.setCaseNumberString(feedbackcase.getTracker().getCode());
                    }

                    // Set Kanban order
                    if (feedbackcase.getKanbanOrder() == null) {
                        Long minKanbanOrderInStatus = caseManager.getMinKanbanOrder(
                                feedbackcase.getStatus() != null ? feedbackcase.getStatus().getObjectID() : null
                        );
                        if (minKanbanOrderInStatus == null) {
                            minKanbanOrderInStatus = KANBAN_ORDER_GAP;
                            feedbackcase.setKanbanOrder(minKanbanOrderInStatus);
                        } else {
                            feedbackcase.setKanbanOrder(minKanbanOrderInStatus - KANBAN_ORDER_GAP);
                        }
                    }

                    caseManager.create(feedbackcase);

                    // Case History
                    EdsCaseHistory caseHistory = new EdsCaseHistory();
                    caseHistory.setCreationTime(new Date());
                    if (!feedback.getIsAnonim()) {
                        caseHistory.setUpdater(userManager.getUser());
                    }
                    caseHistory.setCrmCase(feedbackcase);
                    caseHistory.setMessage(commonLocalizer.localize("createdCase", "Created the Case"));
                    caseHistory.setSuperUser(ServerUtils.isSuperUser());
                    caseHistoryManager.create(caseHistory);

                    // Server Logs
                    KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                    kpiLog.setEntityName(EdsCase.class.getSimpleName());
                    kpiLog.setActionType(KpiLog.ActionType.ADD);
                    kpiLog.setEntityId(feedbackcase.getObjectID());
                    ServerUtils.kpiLog(log, kpiLog, "FeedBack Case Added");

                    // Solr Update
                    try {
                        caseSolrComponent.index(feedbackcase);
                        solrTransactionManager.registerEvent(
                                SolrEvent.CRM_CASE_ADD,
                                feedbackcase,
                                userManager.getUser().getCompany()
                        );
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    private String getChanges(Object ob, CompanyCustomFieldItem item) {
        if (ob != null) {
            if (DATA_TYPE_TEXT.equals(item.getDataType())) {
                String text = (String) ob;
                return !text.equals(item.getFieldStringValue()) ? (item.getColumnCode() + ",") : "";
            } else if (DATA_TYPE_NUMBER.equals(item.getDataType())) {
                String s = String.valueOf(((Double) ob).intValue());
                return !s.equals(item.getFieldStringValue()) ? (item.getColumnCode() + ",") : "";
            } else if (DATA_TYPE_DATE.equals(item.getDataType())) {
                return compareDate(ob, item) ? item.getColumnCode() + "," : "";
            }
        }
        return "";
    }

    private boolean compareDate(Object ob, CompanyCustomFieldItem item) {

        if (!(ob instanceof Date)) {
            return true;
        }

        Date newDate = (Date) ob;

        Date oldDate = item.getFieldDateNonConvertedValue() != null
                ? item.getFieldDateNonConvertedValue().getNonConvertedDate()
                : null;

        if (oldDate == null) {
            return true;
        }

        LocalDate newLocalDate = newDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate oldLocalDate = oldDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return !newLocalDate.equals(oldLocalDate);
    }


    private String getChanges(Object ob, String value, String columnCodeName) {

        if (ob != null) {
            if (columnCodeName.contains("string")) {
                String text = (String) ob;
                return !text.equals(value) ? (columnCodeName + ",") : "";
            } else if (columnCodeName.contains("double")) {
                String s = String.valueOf(((Double) ob).intValue());
                return !s.equals(value) ? (columnCodeName + ",") : "";
            } else if (columnCodeName.contains("date")) {
                Date date = (Date) ob;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String dateStr = format.format(date);
                return !dateStr.equals(value) ? (columnCodeName + ",") : "";
            }
        }
        return "";
    }

    public SelectItem[] getCaseStatus() {
        List<EdsReference> status = referenceManager.listReferences(EdsCase._CASE_STATUS);
        ArrayList<SelectItem> result = new ArrayList<>();
        ReferenceItem item;
        for (EdsReference reference : status) {
            item = new ReferenceItem(reference.getObjectID(), referenceWfmMessageSource.localize(reference.getCode(), reference.getName()), reference.getDescription(), reference.getCssStyle(), null);
            result.add(item);
            if (EdsCase.NEW.equalsIgnoreCase(reference.getCode())) {
                Collections.swap(result, result.indexOf(item), 0);
            }
        }

        SelectItem[] resultArr = new SelectItem[status.size()];
        return result.toArray(resultArr);
    }

    private void createNewLeadForCase(CaseItem item, boolean isFromWebForm, EdsCase crmCase, EdsWebForm webForm) {
        Map<String, String[]> existence = checkEmailExistenceInternally(null, new String[]{item.getEmail(), item.getCompany()});
        boolean foundAndSet = false;
        if (existence != null && existence.size() > 0) {
            for (Map.Entry<String, String[]> entry : existence.entrySet()) {
                String[] values = entry.getValue();
                if (values != null && values.length > 1) {
                    Integer crmEntityID = null;
                    try {
                        crmEntityID = values[0] != null && !"".equals(values[0]) ? Integer.parseInt(values[0]) : null;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (crmEntityID != null && values[1] != null && !"".equals(values[1])) {
                        String from = values[1];
                        if ("lead".equals(from)) {
                            EdsCrmContact lead = crmContactManager.get(crmEntityID);
                            if (lead != null) {
                                crmCase.setLead(lead);
                                foundAndSet = true;
                            }
                        } else if ("account".equals(from)) {
                            EdsCrmAccount crmAccount = crmAccountManager.get(crmEntityID);
                            crmCase.setCrmAccount(crmAccount);
                            foundAndSet = true;
                        } else if ("clientContact".equalsIgnoreCase(from)) {
                            EdsClientContact clientContact = clientContactManager.get(crmEntityID);
                            if (clientContact != null) {
                                EdsCrmContact crmContact = clientContact.getCrmContact();
                                if (crmContact != null) {
                                    crmCase.setCrmContact(crmContact);
                                    foundAndSet = true;
                                } else {
                                    foundAndSet = false;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!foundAndSet) {
            ContactListItem newLead = new ContactListItem();
            newLead.getHomeEmail().add(item.getEmail());
            newLead.setFirstName(item.getFirstName());
            newLead.setLastName(item.getLastName());
            newLead.getCrmAccount().setName(item.getCompany());
            newLead.getHomePhone().add(item.getPhone());
            newLead.getHomeFax().add(item.getFax());
            newLead.setContactType(ContactListItem.LEAD_CONTACT);
            if (isFromWebForm) {
                if (webForm.getOwner() != null) {
                    newLead.setOwnerId(webForm.getOwner().getObjectID());
                }
            }
            Integer leadID = saveLead(newLead, null);
            EdsCrmContact lead = crmContactManager.get(leadID);
            if (lead != null) {
                crmCase.setLead(lead);
            }
        }
    }

    public String getCaseDescription(Integer caseID, boolean stripHtmls) {
        String description = null;
        EdsCase edsCase = caseManager.get(caseID);
        if (edsCase != null) {
            description = EmailUtils.retrieveContent(edsCase.getDescription(), null, new StringBuilder(), null).toString();
        }
        if (stripHtmls && description != null && !"".equals(description)) {
            description = description.trim();
            org.jsoup.nodes.Document htmlDocument = Jsoup.parse(description);
            if (htmlDocument != null) {
                description = htmlDocument.text();
            }
        }
        return description;
    }

    @Transactional
    public void sendAutoResponseToCase(Integer crmCaseID, Integer emailSettingID, Integer filterID) {
        EdsEmailSetting emailSetting = emailSettingID != null ? emailSettingsManager.get(emailSettingID) : emailSettingsManager.getCompanyEmailSetting(SecurityContext.getCompanyID());
        EdsCase edsCase = caseManager.get(crmCaseID);
        filterID = filterID == null ? edsCase.getFilterID() : filterID;
        EdsEmailFilter filter = filterID != null ? emailFilterManager.get(filterID) : null;
        Integer emailTemplateID = filter != null && filter.isSendAutoresponse() ? filter.getEmailTemplateID() : null;
        EdsEmailTemplate emailTemplate = emailTemplateID != null ? emailTemplateManager.get(emailTemplateID) : null;
        if (emailTemplate == null) {
            List<EdsEmailTemplate> companyAutoResponseTemplates = emailTemplateManager.getCompanyAutoResponseTemplates();
            if (companyAutoResponseTemplates != null && companyAutoResponseTemplates.size() > 0) {
                emailTemplate = companyAutoResponseTemplates.get(0);
            }
        }
        if (emailTemplate != null) {
            if (edsCase != null && (edsCase.getInTrash() == null || !edsCase.getInTrash())) {
                EdsUser user;
                if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                    user = userManager.get(emailTemplate.getFromUser());
                } else {
                    user = userManager.getUser();
                    if (user == null) {
                        List<EdsEmployee> admins = userManager.getAdmins(SecurityContext.getCompanyID());
                        if (admins != null && admins.size() > 0) {
                            user = admins.get(0);
                        }
                    }
                }
                EntityToEmailTemplate emailTemplate_ = new EntityToEmailTemplate();
                emailTemplate_.setEntityId(crmCaseID);
                emailTemplate_.setEntityType(CASE_AUTO_RESPONSE_CATEGORY);
                emailTemplate_.setEmailTemplateId(emailTemplate.getObjectID());
                EmailTemplateItem templateItem = generateReplyToReporterCaseItem(emailTemplate_, emailSettingID);
                if (templateItem != null && templateItem.getMessageHTML() != null && edsCase.getTracker() != null) {
                    EdsEmail email = emailRepository.findLastByTrackerId(edsCase.getTracker().getObjectID());
                    String content;
                    String subject = templateItem.getSubject();
                    if (subject != null && !subject.contains("[" + edsCase.getCaseNumberString() + "]")) {
                        subject += "[" + edsCase.getCaseNumberString() + "]";
                    }
                    content = EdsCase.getEmailContent(SecurityContext.getCompanyID(), crmCaseID);
                    if (email != null && content == null) {
                        content = EmailUtils.retrieveContent(messageCenterService.getContentOnly(email.getId()), null, null, null).toString();
                    }
                    content = content == null ? edsCase.getDescription() : content;
                    String toemail = templateItem.getToEmail();
                    String defaultMessage = "----------------------------------------<br>" +
                            "<html style=\"background:none repeat scroll 0% 0% transparent;\">" +
                            "<head><style>body {margin:8px} .LW-yrriRe {font:normal small arial}\n" +
                            " img {-moz-force-broken-image-icon: 1;}</style></head>" +
                            "<body><div>" + "On " + getFormatDate(edsCase.getAuditInfo().getCreationDate()) + ", " +
                            (edsCase.getReportedBy() != null ? ("<b>" + edsCase.getReportedBy() + "</b>") : "") + " " +
                            ((edsCase.getEmail() != null && !"".equals(edsCase.getEmail())) ? ("&lt;" + edsCase.getEmail() + "&gt;") : "") + " wrote:" +
                            "<blockquote style=\"margin: 0pt 0pt 0pt 0.8ex; border-left: 1px solid rgb(204, 204, 204); padding-left: 1ex;\">" +
                            (content != null ? content : "") +
                            "</blockquote></div></body></html>";
                    String message = templateItem.getMessageHTML() + defaultMessage;
                    if (EdsContextParams.isLocal()) {
                        toemail = "";
                    }
                    if (user != null && StringUtils.isNotEmpty(toemail)) {
                        String fromEmail = emailSetting != null ? emailSetting.getEmail() : user.getEmail();
                        emailSetting = emailSetting != null ? emailSetting : StringUtils.isNotBlank(fromEmail) ? emailSettingsManager.getActiveEmailSetting(fromEmail) : emailSettingsManager.getCompanyEmailSetting(null);
                        emailSetting = emailSetting == null ? EdsMailer.getWhiteLabelingMailer().asEdsEmailSetting() : emailSetting;
                        MailMessage mailMessage = new MailMessage();
                        mailMessage.setSubject(subject);
                        mailMessage.setContent(message);
                        for (String toE : toemail.trim().split(",")) {
                            mailMessage.addContactsTo(toE);
                        }
                        mailMessage.setFromUserFullName(StringUtils.isNotEmpty(emailTemplate.getFromUserName()) ? emailTemplate.getFromUserName() : StringUtils.isNotEmpty(user.getFullName()) ? user.getFullName() : emailSetting.getFromName());
                        mailMessage.setFromEmail(StringUtils.isEmpty(fromEmail) ? emailSetting.getEmail() : fromEmail);
                        mailMessage.setTrackerID(edsCase.getTracker().getObjectID());
//                        emailFetchingServices.getService(emailSetting.getObjectID()).sendMessage(emailSetting, mailMessage);//todo temp stop auto responses
                    }
                }
            }
        }
    }

    private void createEntity(final Object object) {
        EdsEntity entity = new EdsEntity();
        if (object instanceof EdsCrmAccount) {
            entityManager.create(entity);
            ((EdsCrmAccount) object).setEntityID(entity.getObjectID());
        }
        if (object instanceof EdsCrmContact) {
            entityManager.create(entity);
            ((EdsCrmContact) object).setEntityID(entity.getObjectID());
        }
        if (object instanceof EdsCase) {
            entityManager.create(entity);
            ((EdsCase) object).setEntityID(entity.getObjectID());
        }
    }

    private void generateCaseNumber(final EdsCase crmCase) {
        if (crmCase.getTracker() != null) {
            crmCase.setCaseNumberString(crmCase.getTracker().getCode());
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS/*, readOnly = true*/)
    public CrmAccountItem getAccount(EdsCrmAccount account, boolean brief) {
        CrmAccountItem item = new CrmAccountItem();
        if (account != null) {
            item = account.getRPC(item, brief);
            if (StringUtils.isNotBlank(account.getEntityType())) {
                if (CRM_ACCOUNT_TYPE.equals(account.getEntityType()) && account.isClient()) {
                    item.setItemCustomFields(commonService.getCompanyCustomFields(ViewName.ClientItem));
                    item.setCustomItemColumns(itemTableSettingsServiceLocal.getColumnConfigs(ItemTableEnum.CLIENT_ITEM, false, true));
                    item.setItems(getCrmSubItems(CUSTOMER, account.getObjectID()));
                } else {
                    item.setItemCustomFields(commonService.getCompanyCustomFields(SUPPLIER.equals(account.getEntityType()) ? ViewName.SupplierItem : ViewName.ClientItem));
                    item.setCustomItemColumns(itemTableSettingsServiceLocal.getColumnConfigs(SUPPLIER.equals(account.getEntityType()) ? ItemTableEnum.SUPPLIER_ITEM : ItemTableEnum.CLIENT_ITEM, false, true));
                    item.setItems(getCrmSubItems(account.getEntityType(), account.getObjectID()));
                }
            }
            List<EdsAddress> billAddrList = addressManager.getAddressesByEntityIdAndType(account.getObjectID(), EdsAddress.BILLING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
            List<EdsAddress> mailAddrList = addressManager.getAddressesByEntityIdAndType(account.getObjectID(), EdsAddress.MAILING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
            item.setBillAddresses(account.getAddressData(billAddrList, account.getBillingAddress()));
            item.setMailAddresses(account.getAddressData(mailAddrList, account.getMailingAddress()));
            if (account.getLogo() != null && account.getLogo().getObjectID() != null) {
                item.setLogoUrl(getImageUrl(account.getLogo().getObjectID()));
            }
            if (account.getCurrency() == null) {
                EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
                if (fs != null && fs.getCurrency() != null) {
                    EdsCurrency userCurrency = fs.getCurrency();
                    item.setCurrency(userCurrency == null ? null : userCurrency.getName());
                }
            }
            if (!brief) {
                ListingFilterParameter fp = new ListingFilterParameter();
                fp.setAccountID(account.getObjectID());
                if (account.getEntityID() != null) {
                    fp.setEntityID(account.getEntityID());
                }
                item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(account.getCustomFields(),
                        commonService.getCompanyCustomFields(ViewName.CrmAccount)));
            }
//            if (!account.getBalanceCalculated()) {
            item.setClientBalance(crmAccountManager.getClientBalance(account.getObjectID()).doubleValue());
            item.setSupplierBalance(crmAccountManager.getSupplierBalance(account.getObjectID()).doubleValue());
//
//                account.setBalanceCalculated(true);
//                crmAccountManager.update(account);
//            }
            item.setIndustries(getAsSelectItem(referenceManager.listReferences("_COMPANY_WORKAREA"), 10));
            item.setPaymentMethods(allInOneServiceLocal.getPaymentMethodList());
            SelectItem[] ownersList = getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE);
            if (account == null || account.getOwners().isEmpty()) {
                item.setOwnerItems(ownersList);
            } else {
                for (SelectItem owner : ownersList) {
                    owner.setSelected(account.getOwnersMap().containsKey(owner.getId()));
                }
                item.setOwnerItems(ownersList);
            }

            item.setCurrencies(getCurrencies());
            if ((account.isClient() || account.isSupplier())) {
                if (account.getCurrency() != null) {
                    List<EdsPriceLevel> priceLevels = priceLevelManager.getPriceLevels(account.getCurrency().getObjectID(), account.getObjectID(), true);
                    if (priceLevels != null && priceLevels.size() > 0) {
                        SelectItem[] prices = new SelectItem[priceLevels.size()];
                        int indexer = 0;
                        for (EdsPriceLevel price : priceLevels) {
                            prices[indexer] = new SelectItem();
                            prices[indexer].setName(price.getName());
                            indexer++;
                        }
                        item.setAppliedPriceLavel(prices);
                    }
                }

                List<SelectItem> discountItems = new ArrayList<>();
                for (EdsDiscount discount : account.getDiscounts()) {
                    discountItems.add(discount.getAsSelectItem());
                }
                item.setAppliedDiscounts(discountItems.toArray(new SelectItem[]{}));

                EdsReference taxTreatment = referenceManager.get(account.getTaxtreatmentId());
                if (taxTreatment != null) {
                    item.setTaxTreatment(taxTreatment.getAsSelectItem());
                    item.getTaxTreatment().setCode(account.getTaxTreatment().getCode());
                }
                item.setTrn(account.getTrn());
                EdsCountry placeOfSupplyCountry = account.getPlaceofsupplyCountryId() != null ? countryManager.get(account.getPlaceofsupplyCountryId()) : null;
                if (placeOfSupplyCountry != null) {
                    item.setPlaceOfSupplyCountry(new SelectItem(placeOfSupplyCountry.getObjectID(), placeOfSupplyCountry.getName(), placeOfSupplyCountry.getCode()));
                }
                EdsRegion placeOfSupplyState = account.getPlaceofsupplyStateId() != null ? regionManager.get(account.getPlaceofsupplyStateId()) : null;
                if (placeOfSupplyState != null) {
                    item.setPlaceOfSupplyState(new SelectItem(placeOfSupplyState.getObjectID(), placeOfSupplyState.getName(), placeOfSupplyState.getCode()));
                }
            }
            EdsCrmContact primaryContact = account.getPrimaryContact();
            if (primaryContact != null) {
                item.setPrimaryContact(primaryContact.getRPC(null));
            }
            String formType = null;
            if (CUSTOMER.equals(account.getEntityType())) {
                formType = LayoutRPC.CLIENT_FORM;
            } else if (SUPPLIER.equals(account.getEntityType())) {
                formType = LayoutRPC.SUPPLIER_FORM;
            } else if (CustomFormConstants.CRM_ACCOUNT.equals(account.getEntityType())) {
                formType = LayoutRPC.ACCOUNT_FORM;
            }

            LinkedHashMap<String, FormProperty> fields = new LinkedHashMap<>();
            if (formType != null) {
                EdsFormProperty edsFormProperty = formPropertyManager.getByFormID(formType);
                if (edsFormProperty != null) {
                    Gson gson = new Gson();
                    FormProperty[] formFields = gson.fromJson(edsFormProperty.getSettingsJSONData(), FormProperty[].class);
                    for (FormProperty formProperty : formFields) {
                        if (formProperty != null) {
                            if (formProperty.getDefaultValue() != null && formProperty.getDefaultValue().length() == 0) {
                                formProperty.setDefaultValue(null);
                            }
                            if (formProperty.getRoleEdit() != null && formProperty.getRoleEdit().size() > 0) {
                                if (userManager.getUser().hasEitherRoles(formProperty.getRoleEdit().toArray(new Integer[]{}))) {
                                    formProperty.setDisabled(false);
                                }
                            }
                            fields.put(formProperty.getCode(), formProperty);
                        }
                    }
                }
            }
            item.setFormProperty(fields);
            Map<Integer, ArrayList<String>> telegramChats = account.getParams(EdsCrmContactItemParams.TELEGRAM_CHATS);
            if (telegramChats.size() > 0) {
                ArrayList<SelectItem> chats = new ArrayList<>();
                for (Integer botId : telegramChats.keySet()) {
                    TelegramSettingsItem bot = telegramChatService.getTelegramSettingsItem(botId);
                    TelegramChatListItem chat = telegramChatService.getChat(Integer.valueOf(telegramChats.get(botId).get(0)));
                    chats.add(new SelectItem(botId, bot.getBotName(), chat.getChatName()));
                }
                item.setTelegramChats(chats);
            }
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityName(EdsCrmAccount.class.getSimpleName());
        if (account != null) {
            kpiLog.setEntityId(account.getObjectID());
            if (account.isClient()) {
                kpiLog.setEntityType(KpiEntityType.CLIENT);
                ServerUtils.kpiLog(log, kpiLog, "View client");
            } else if (account.isSupplier()) {
                kpiLog.setEntityType(KpiEntityType.SUPPLIER);
                ServerUtils.kpiLog(log, kpiLog, "View Supplier");
            } else {
                kpiLog.setEntityType(KpiEntityType.ACCOUNT);
                ServerUtils.kpiLog(log, kpiLog, "View Crm Account");
            }
        }
        var activePaidInvoice = invoiceManager.getPrioritizedInvoices(List.of(account.getObjectID()));
        var expireDate = crmAccountInvoiceExpire(activePaidInvoice, account.getObjectID());
        item.setInvoiceExpireDate(new DateNonConvertable(expireDate));
        item.setInvoicePaidStatus(invoicePaidStatus(expireDate));
        return item;
    }

    public String invoicePaidStatus(Date expireDate) {
        if (expireDate == null) return "Not Paid";
        if (expireDate.after(new Date())) return "Paid";
        if (expireDate.before(new Date())) return "Expired";
        return "Pending";
    }

    public Date crmAccountInvoiceExpire(List<CrmAccountInvoiceTO> activePaidInvoice, Integer crmAccountId) {
        return getFirstActivePaidInvoice(activePaidInvoice, crmAccountId)
                .map(CrmAccountInvoiceTO::getInvoiceExpireDate)
                .orElse(null);
    }

    public Optional<CrmAccountInvoiceTO> getFirstActivePaidInvoice(List<CrmAccountInvoiceTO> activePaidInvoice, Integer crmAccountId) {
        return activePaidInvoice.stream()
                .filter(i -> Objects.equals(i.getCrmAccountId(), crmAccountId))
                .findFirst();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HistoryList getCrmNoteHistory(ListingFilterParameter fp) {
        if (fp != null) {
            if ((Integer.valueOf(0)).equals(fp.getEntityID())) {
                if (fp.getAccountID() != null) {
                    EdsCrmAccount crmAccount = crmAccountManager.get(fp.getAccountID());
                    if (crmAccount != null && crmAccount.getEntityID() != null) {
                        fp.setEntityID(crmAccount.getEntityID());
                    }
                } else {
                    fp.setEntityID(null);
                }
            }
        }
        Integer totalCount = noteHistoryManager.getListCount(fp);
        List<EdsNoteHistory> historyList = noteHistoryManager.getNoteList(fp);

        if (historyList == null) {
            historyList = new LinkedList<>();
        }

        HistoryListItem[] historyArray = new HistoryListItem[historyList.size()];

        int i = 0;
        for (EdsNoteHistory item : historyList) {
            historyArray[i++] = item.getHistoryItem();
        }

        return new HistoryList(historyArray, totalCount);
    }

    private ActivityItem getActivity(Object edsObject, boolean brief, String salesType) {
        ActivityItem item = new ActivityItem();
        EdsEvent event;
        EdsEmail email;
        EdsMailMessage mail;
        EdsSmsSendItem sms;
        NewInvoice quoteItem;
        NewInvoice orderItem;
        NewInvoice invoiceItem;

        if (edsObject instanceof EdsEvent) {
            event = (EdsEvent) edsObject;
            item.setEntityId(event.getObjectID());
            item.setCallLog(event.getActivityType() == Appointment.CALL_LOG);
            item.setInterview(event.getActivityType() == Appointment.INTERVIEW);
            item.setEventObjectId(event.getObjectID());
            item.setSubject(event.getSubject());
            item.setActivityType(CrmConstants.CRM_EVENT);
            item.setCreationDate(event.getCreationTime());
            if (event.getStartDate() != null) {
                Date date = event.getOwner().getUserDate(event.getStartDate());
                item.setSStartDate(DateFormat.getInstance().format(date));
                item.setStartDate(event.getStartDate());
            } else {
                item.setStartDate(null);
                item.setSStartDate("");
            }

            if (event.getEndDate() != null) {
                Date date = event.getEndDate();
                item.setSDueDate(DateFormat.getInstance().format(date));
                item.setDueDate(event.getEndDate());
            } else {
                item.setDueDate(null);
                item.setSDueDate("");
            }
            item.setStatus("");
            item.setPriority("");
            if (brief) {
                item.setDescription(event.getDescription() != null ? event.getDescription() : "");
                item.setAssignee(event.getAssignee() != null && !"".equals(event.getAssignee()) ? event.getAssignee().getName() : "");
            }
        }
        if (edsObject instanceof EdsEmail) {
            email = (EdsEmail) edsObject;
//            item.setEntityId(email.getId());
            item.setEmailObjectId(email.getId());
            item.setActivityType(CrmConstants.EMAIL);
            item.setStartDate(email.getCreatedDate());
            item.setDueDate(email.getCreatedDate());
            item.setCreationDate(email.getCreatedDate());
            item.setSubject(email.getSubject());
            item.setFrom(email.getFrom());
            item.setTo(email.getTo());
            item.setStatus(email.getFolderType());
            if (brief) {
                item.setContent(email.getDescription());
                item.setReplyTo(email.getReplyTo());
                item.setCc(email.getToCC());
                item.setBcc(email.getToBCC());
                if (email.getEmailTemplateId() != null) {
                    EdsEmailTemplate emailTemplate = emailTemplateManager.get(email.getEmailTemplateId());
                    item.setFromUserID((emailTemplate.getFromUser() != -1) ? emailTemplate.getFromUser() : (userManager.getUser() != null ? userManager.getUser().getObjectID() : null));
                }
            }
        }
        if (edsObject instanceof TaskListItem task) {
            item.setEntityId(task.getObjectID());
            item.setTaskObjectId(task.getObjectID());
            item.setActivityType(CrmConstants.TASK);
            item.setCreationDate(task.getCreationDate());
            item.setStartDate(task.getStartDate());
            item.setSubject(task.getName());
            item.setDueDate(task.getDueDate());
            item.setStatus(task.getStatusName());
            item.setStatusID(task.getTaskStatusId());
            item.setPriority(task.getPriorityName());
            item.setPriorityID(task.getPriorityId());
            item.setPriorityCode(task.getPriorityCode());
        }
        if (edsObject instanceof EdsMailMessage) {
            mail = (EdsMailMessage) edsObject;
            item.setEntityId(mail.getObjectID());
            item.setActivityType(CrmConstants.MASS_MAIL);
            item.setMassMailObjectId(mail.getObjectID());
            item.setStartDate(mail.getScheduled() != null ? mail.getScheduled() : mail.getCreationTime());
            item.setCreationDate(mail.getCreationTime());
            item.setSubject(mail.getSubject());
            item.setStatus(mail.getStatusCode() != null ? mail.getStatusCode().getCode() : "");
        }
        if (edsObject instanceof EdsSmsSendItem) {
            sms = (EdsSmsSendItem) edsObject;
            item.setEntityId(sms.getObjectID());
            item.setActivityType(CrmConstants.SMS);
            item.setMassMailObjectId(sms.getObjectID());
            item.setStartDate(sms.getSentDate());
            item.setCreationDate(sms.getSentDate());
            item.setSubject(sms.getToNumber());
        }
        if (salesType.equals(CrmConstants.SALEQUOTE)) {
            quoteItem = (NewInvoice) edsObject;
            item.setEntityId(quoteItem.getID());
            item.setActivityType(CrmConstants.SALEQUOTE);
            item.setSalesID(quoteItem.getID());
            item.setStartDate(quoteItem.getInvoiceDate() != null ? quoteItem.getInvoiceDate().getDate() : null);
            item.setDueDate(quoteItem.getDueDate() != null ? quoteItem.getDueDate().getDate() : null);
            item.setDueDate2(quoteItem.getDueDate());
            item.setStatus(quoteItem.getStatus());
            item.setStatusID(quoteItem.getStatusID());
            item.setSubject(quoteItem.getInvoiceNumber() != null ? quoteItem.getInvoiceNumber() : "");
            item.setCreationDate(quoteItem.getCreationDate());
        }
        if (salesType.equals(CrmConstants.SALEORDER)) {
            orderItem = (NewInvoice) edsObject;
            item.setEntityId(orderItem.getID());
            item.setActivityType(CrmConstants.SALEORDER);
            item.setSalesID(orderItem.getID());
            item.setStartDate(orderItem.getInvoiceDate() != null ? orderItem.getInvoiceDate().getDate() : null);
            item.setDueDate(orderItem.getDueDate() != null ? orderItem.getDueDate().getDate() : null);
            item.setDueDate2(orderItem.getDueDate());
            item.setStatus(orderItem.getStatus());
            item.setStatusID(orderItem.getStatusID());
            item.setSubject(orderItem.getInvoiceNumber() != null ? orderItem.getInvoiceNumber() : "");
            item.setCreationDate(orderItem.getCreationDate());
        }
        if (salesType.equals(CrmConstants.SALEINVOICE)) {
            invoiceItem = (NewInvoice) edsObject;
            item.setEntityId(invoiceItem.getID());
            item.setActivityType(CrmConstants.SALEINVOICE);
            item.setSalesID(invoiceItem.getID());
            item.setStartDate(invoiceItem.getInvoiceDate() != null ? invoiceItem.getInvoiceDate().getDate() : null);
            item.setDueDate(invoiceItem.getDueDate() != null ? invoiceItem.getDueDate().getDate() : null);
            item.setDueDate2(invoiceItem.getDueDate());
            item.setStatus(invoiceItem.getStatus());
            item.setStatusID(invoiceItem.getStatusID());
            item.setSubject(invoiceItem.getInvoiceNumber() != null ? invoiceItem.getInvoiceNumber() : "");
            item.setCreationDate(invoiceItem.getCreationDate());
        }
        return item;
    }

    private CampaignItem getCampaign(EdsCampaign campaign, boolean brief) {
        CampaignItem item = new CampaignItem();
        item = campaign.getRPC(item, brief);
        item.setStatus(referenceWfmMessageSource.localize(item.getStatusCode(), item.getStatus()));
        item.setType(referenceWfmMessageSource.localize(item.getTypeCode(), item.getType()));
        return item;
    }

    private CaseItem getCase(EdsCase crmCase, boolean fromSummary) {
        CaseItem item = crmCase.getRPC(null);
        item.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_CASE, crmCase.getObjectID())));

        Map<String, List<ReferenceItem>> referenceMap = referenceManager.listReferences(List.of(EdsCase._CASE_STATUS, EdsCase._CASE_PRIORITY, EdsCase._CASE_TYPE, EdsCase._CASE_REASON), false);

        item.setStatusItems(referenceMap.get(EdsCase._CASE_STATUS).toArray(new ReferenceItem[]{}));
        item.setPriorities(referenceMap.get(EdsCase._CASE_PRIORITY).toArray(new ReferenceItem[]{}));
        item.setTypes(referenceMap.get(EdsCase._CASE_TYPE).toArray(new ReferenceItem[]{}));
        item.setCaseReasons(referenceMap.get(EdsCase._CASE_REASON).toArray(new ReferenceItem[]{}));
        item.setHasAttachments(hasAttachment(crmCase.getObjectID(), "case"));
        item.setPriority(referenceWfmMessageSource.localize(item.getPriorityCode(), item.getPriority()));
        item.getStatus().setName(referenceWfmMessageSource.localizeRef(crmCase.getStatus()));
        item.setCaseReason(referenceWfmMessageSource.localize(item.getCaseReasonCode(), item.getCaseReason()));
        item.setType(referenceWfmMessageSource.localize(item.getTypeCode(), item.getType()));
        item.setCaseOrigin(referenceWfmMessageSource.localize(item.getCaseOriginCode(), item.getCaseOrigin()));
        item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(crmCase.getCustomFields(), commonService.getCompanyCustomFields(ViewName.CrmCase)));
        item.setAnonim(crmCase.IsUserFeedBackAnonim());
        if (fromSummary && crmCase.getTracker() != null) {
            item.setLastEmail(getCaseEmail(crmCase.getEmailID(), crmCase.getTracker().getObjectID()));
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SHOW_CASE_RELATED_ALL_EMAILS)) {
                item.setCaseEmails(getCaseEmails(crmCase.getTracker().getObjectID()));
            }
        }
        List<FileResource> attachment = attachmentUtilsManager.getAttachments(F_CASE, crmCase.getObjectID(), crmCase.getObjectID());
        List<FileItem> attachments = new ArrayList<>();
        for (FileResource fileResource : attachment) {
            FileItem fileItem = new FileItem();
            fileItem.setId(fileResource.getObjectId());
            fileItem.setFileName(fileResource.getFileName());
            attachments.add(fileItem);
        }
        item.setAttachments(attachments.toArray(new FileItem[]{}));
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCase.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(item.getObjectId());
        ServerUtils.kpiLog(log, kpiLog, "View case");

        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Boolean hasAttachment(Integer objectId, String from) {
        List<FileResource> attachments = null;
        if (objectId != null) {
            if ("contact".equals(from)) {
                attachments = attachmentUtilsManager.getAttachments(F_CRM_CONTACT, objectId, objectId);
            }
            if ("lead".equals(from)) {
                attachments = attachmentUtilsManager.getAttachments(F_LEAD, objectId, objectId);
            }
            if ("case".equals(from)) {
                EdsCase crmCase = caseManager.get(objectId);
                if (crmCase != null) {
                    List<EdsAttachment> attachments_ = attachmentManager.getCaseAttachments(crmCase.getObjectID());
                    if (attachments_ != null && attachments_.size() > 0) {
                        return true;
                    }
                }
            }
            return attachments != null && attachments.size() > 0;
        }
        return false;
    }

    private ListResult<SelectItem> getCrmLookNames(ListingFilterParameter filterParametrs, String entityType) {
        SolrClient server = null;
        QueryResponse resp = null;
        ArrayList<SelectItem> selectItems = null;
        Integer totalCount = 0;

        try {
            ListLoadConfig config = new ListLoadConfig();
            config.setStart(filterParametrs.getStart());
            config.setLimit(!Integer.valueOf(0).equals(filterParametrs.getLimit()) ? filterParametrs.getLimit() : 20);
            filterParametrs.setAsSelectItem(true);

            if (CrmConstants.CRM_CONTACT.equals(entityType) || CrmConstants.CRM_LEAD.equals(entityType) || CrmConstants.CANDIDATE.equals(entityType)) {
                Page<ContactSolrDoc> contactSolrDocPage = null;
                if (CrmConstants.CANDIDATE.equals(entityType)) {
                    contactSolrDocPage = contactSolrComponent.getCandidateList(filterParametrs, clientManager.getUser());
                } else if (CrmConstants.CRM_LEAD.equals(entityType)) {
                    contactSolrDocPage = contactSolrComponent.getLeadList(filterParametrs, config);
                } else {
                    contactSolrDocPage = contactSolrComponent.getList(filterParametrs, config, clientManager.getUser());
                }
                selectItems = contactSolrComponent.getCrmLookNamesForContact(contactSolrDocPage, filterParametrs);
                totalCount = Math.toIntExact(contactSolrDocPage.getTotalElements());
            } else if (CrmConstants.CRM_CASE.equals(entityType)) {
                Page<CaseSolrDoc> caseSolrDocPage = caseSolrComponent.getList(filterParametrs, commonServiceLocal.getCrmCaseSolrQuery(filterParametrs, null, filterParametrs.getFacetFilter()), false);
                selectItems = caseSolrComponent.getCrmLookNamesForCase(caseSolrDocPage, filterParametrs);
                totalCount = Math.toIntExact(caseSolrDocPage.getTotalElements());
            } else if (CrmConstants.CRM_OPPORTUNITY.equals(entityType)) {
                Page<OpportunitySolrDoc> opportunitySolrDocPage = opportunitySolrComponent.getList(filterParametrs);
                selectItems = opportunitySolrComponent.getCrmLookNamesForOpportunity(opportunitySolrDocPage);
                totalCount = Math.toIntExact(opportunitySolrDocPage.getTotalElements());
            } else if (CrmConstants.CRM_ACCOUNT.equals(entityType) || CrmConstants.CLIENT.equals(entityType) || CrmConstants.SUPPLIER.equals(entityType)) {
                server = WfmJpaTemplate.getSolrServerForCore(SOLR_CRM_ACCOUNT_CORE);

                ListingFilterParameter lFP = new ListingFilterParameter();
                lFP.setSearchKey(filterParametrs.getSearchKey());
                lFP.setFromMobile(filterParametrs.isFromMobile());
                lFP.setStart(config.getStart());
                lFP.setLimit(config.getLimit());
                lFP.setAsSelectItem(true);
                lFP.setWithBlockedAccount(filterParametrs.isWithBlockedAccount());
                String accountType = CrmConstants.CLIENT.equals(entityType) ? EdsCrmAccount.CUSTOMER : CrmConstants.SUPPLIER.equals(entityType) ? EdsCrmAccount.SUPPLIER : filterParametrs.getAccountType();
                lFP.setAccountType(accountType);
                lFP.setFacetFilter(filterParametrs.getFacetFilter());
                lFP.setLookUp(filterParametrs.isLookUp());
                lFP.setShowHeadOffice(filterParametrs.isShowHeadOffice());
                lFP.setAvoidType(filterParametrs.getAvoidType());

                if (filterParametrs.getAvoidType() != null && Constants.SUPPLIER.equals(filterParametrs.getAvoidType())) {
                    EdsReference edsSupplier = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.SUPPLIER);
                    lFP.setAvoidId(edsSupplier.getObjectID());
                }
                if (filterParametrs.isLookUp() && filterParametrs.getProjectId() != null) {
                    ArrayList<Integer> clients = projectManager.getProjectClientsByID(filterParametrs.getProjectId());
                    lFP.setObjectIDs(clients);
                }

                Page<CrmAccountSolrDoc> crmAccountSolrDocPage = crmAccountSolrComponent.getCrmAccountList(lFP, config);
                selectItems = crmAccountSolrComponent.getCrmLookNamesForAccount(crmAccountSolrDocPage, filterParametrs);
                totalCount = Math.toIntExact(crmAccountSolrDocPage.getTotalElements());
            }
//            resp = server.query(query, SolrRequest.METHOD.POST);
//            totalCount = Math.toIntExact(resp.getResults().getNumFound());
        } catch (SolrServerException | IOException e) {
            log.error("", e);
        }
        return new ListResult<>(selectItems, totalCount);
    }

    public SolrQuery getSolrQueryForCandidate(ListingFilterParameter fp, EdsUser user) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        FacetFilterRpc facetFilter = fp.getFacetFilter();
        if (facetFilter != null && !facetFilter.isFilterChanges()) {
            facetFilter = commonServiceLocal.getUserFacetFilter(facetFilter);
        }

        EdsCompany edsCompany = user.getCompany();
        StringBuilder solrQuery = new StringBuilder();
        fp.setUserID(user.getObjectID());
        solrQuery.append(QueryBuilderForSolr.getCandidateListSolrQuery(fp, facetFilter, edsCompany, user));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(facetFilter, edsCompany, SolrContactRepresenter.FIELD_UPDATE_DATE, null));

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(fp.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(fp.getLimit() == 0 ? 20 : fp.getLimit()));

        if (!fp.isSearchButton() && !fp.isLookUp()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                boolean desc = Constants.DESC == fp.getSortDir();
                String solrSortField = SolrContactRepresenter.getSortingField(fp.getSortField());
                if (solrSortField != null) {
                    query.setSort(solrSortField, desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc);
                } else {
                    CustomFieldsUtils.setCustomFieldsSortableNameToSolr(fp.getSortField(), desc, query, true);
                }
            } else {
                query.setSort(SolrContactRepresenter.FIELD_UPDATE_DATE, SolrQuery.ORDER.desc);
            }
        }
        return query;
    }

    /**
     * <h1>... This is method generated Conact List Solr Query ....</h1>
     * <br/>
     * <h2>... Written by developer {Hayot.R} ....</h2>
     * <h2>... Changed by developer {Dilshod.T} ....</h2>
     * <br/>
     * <h3>... Last Updated {18:27 11/06/2011} ...</h3>
     *
     * @param fp
     * @param config
     * @param user
     * @return
     */
    public SolrQuery getSolrQueryForContact(ListingFilterParameter fp, ListLoadConfig config, EdsUser user) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        if (config.getLimit() == 0) {
            config.setLimit(20);
        }
        FacetFilterRpc contactFacetFilter = fp.getFacetFilter();
        if (contactFacetFilter != null && !contactFacetFilter.isFilterChanges()) {
            contactFacetFilter = commonServiceLocal.getUserFacetFilter(contactFacetFilter);
        }

        EdsCompany edsCompany = user.getCompany();
        String caegoryIdsForUserForSolr = fp.isFiltirize() ? contactCategoryManager.getCategoryIDsForUserForSOLR(null, user, null, null) : null;

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(getContactListSolrQuery(fp, contactFacetFilter, user, caegoryIdsForUserForSolr));

        if (fp.isFiltirize()) {
            solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(contactFacetFilter, edsCompany, SolrContactRepresenter.FIELD_UPDATE_DATE, null));
        }
        if (fp.isDetectDuplicates()) {
            Set<String> duplicates = getContactDuplicateNames(solrQuery.toString(), fp.getObjectIDs());
            if (duplicates != null && duplicates.size() > 0) {
                StringBuilder duplicateQuery = new StringBuilder();
                boolean isFirst = true;
                boolean found = false;
                for (String duplicate : duplicates) {
                    duplicate = duplicate.trim();
                    if (!"".equals(duplicate)) {
                        if (isFirst) {
                            found = true;
                            duplicateQuery.append(SolrContactRepresenter.FIELD_CONTACT_NAME_TEXT_FIELD).append(":(");
                        }
                        duplicateQuery.append(!isFirst ? " OR " : "").append(duplicate).append(" OR ").append(duplicate).append("*");
                        isFirst = false;
                    }
                }
                if (found) {
                    duplicateQuery.append(")");
                }
                if (duplicateQuery.length() > 0) {
                    solrQuery.append(" AND (").append(duplicateQuery).append(")");
                }
            }
        }

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(config.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(config.getLimit()));

        if (!fp.isSearchButton() && !fp.isLookUp()) {
            if (fp.isDetectDuplicates()) {
                boolean desc = Constants.DESC == config.getSortDir();
                SolrQuery.ORDER sortOrder = getSolrOrder(desc);
                query.setSort(SolrContactRepresenter.SORTABLE_CONTACT_NAME, sortOrder);
            } else {
                if (config.getSortField() != null && !"".equals(config.getSortField())) {
                    boolean desc = Constants.DESC == config.getSortDir();
                    String solrSortField = SolrContactRepresenter.getSortingField(config.getSortField());
                    if (solrSortField != null) {
                        query.setSort(solrSortField, getSolrOrder(desc));
                    } else {
                        CustomFieldsUtils.setCustomFieldsSortableNameToSolr(config.getSortField(), desc, query, true);
                    }
                } else {
                    if (fp.isFavourite()) {
                        query.setSort(SolrContactRepresenter.FIELD_IS_FAVOURITED, SolrQuery.ORDER.desc);
                        query.addSort(SolrContactRepresenter.FIELD_UPDATE_DATE, SolrQuery.ORDER.desc);
                    } else {
                        query.setSort(SolrContactRepresenter.FIELD_UPDATE_DATE, SolrQuery.ORDER.desc);
                    }
                }
            }
        }
        if (!fp.isFiltirize() && fp.isLookUp()) {
            query.setFields(SolrContactRepresenter.FIELD_PRIMARY_EMAIL, SolrContactRepresenter.FIELD_FIRST_NAME, SolrContactRepresenter.FIELD_LAST_NAME, SolrContactRepresenter.FIELD_CONTACT_ID);//we need only these field for lookup in message center.
        }
        return query;
    }

    @Override
    public String getContactListSolrQuery(ListingFilterParameter fp,
                                          FacetFilterRpc contactFilter,
                                          EdsUser user,
                                          String categoryIdForUserForSolrQuery) {

        StringBuffer solrQuery = new StringBuffer("(");
        solrQuery.append(SolrContactRepresenter.FIELD_COMPANY_ID).append(":").append(user.getCompany().getObjectID());

        List<String> customAccessRoles = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.CRM_SEE_ALL_CONTACT_LIST);
        boolean hasCustomFullAccessToListing = customAccessRoles.size() > 0 && user.hasEitherRoles(customAccessRoles.toArray(new String[]{}));

        if (fp.isFiltirize() && categoryIdForUserForSolrQuery != null && !"".equals(categoryIdForUserForSolrQuery)) {
            solrQuery.append(" AND (");
            solrQuery.append(categoryIdForUserForSolrQuery);
            solrQuery.append(")");
        }

        // Set Search key
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            if (fp.isFromMobile()) {
                solrQuery.append(" AND ((").append(SolrContactRepresenter.FIELD_CONTACT_NAME);
                solrQuery.append(":(").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), true)).append(")");
                solrQuery.append(" OR ").append(SolrContactRepresenter.FIELD_PRIMARY_PHONE);
                solrQuery.append(":(*").append(fp.getSearchKey()).append("*))");
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                Map<String, Double> fields = new HashMap<>();
                fields.put(SolrContactRepresenter.FIELD_LOOKUP_COMPOSITE_MOBILE, SolrSearchUtils.HIGH_PRIORITY);
                searchUtils.generateApiSearchQuery(solrQuery, fields, fp.getSearchKey());
                solrQuery.append(")");
            } else {
                solrQuery.append(" AND (");
                if (fp.isLookUp()) {
                    if (Constants.BY_BOTH.equals(fp.getLookUpBy())) {
                        solrQuery.append(SolrContactRepresenter.FIELD_EMAIL_NAME_COMPOSITE);
                    } else if (Constants.BY_EMAIL.equals(fp.getLookUpBy())) {
                        solrQuery.append(SolrContactRepresenter.FIELD_EMAIL_COMPOSITE);
                    } else if (fp.isLetterSearch()) {
                        solrQuery.append("((");
                        solrQuery.append(SolrContactRepresenter.FIELD_CONTACT_FIRST_COMPOSITE);
                        solrQuery.append(":").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), true));
                        solrQuery.append(")^20000.0) OR ((");
                        solrQuery.append(SolrContactRepresenter.FIELD_CONTACT_LAST_COMPOSITE);
                        solrQuery.append(":").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), true));
                        solrQuery.append(")^0.02)");
                    } else {
                        solrQuery.append(SolrContactRepresenter.FIELD_CONTACT_NAME_COMPOSITE);
                    }
                } else if (fp.isWidgetSearch()) {
                    solrQuery.append(SolrContactRepresenter.FIELD_CONTACT_NAME_COMPOSITE);
                } else {
                    solrQuery.append(SolrContactRepresenter.FIELD_COMPOSITE);
                }
                if (!fp.isLetterSearch()) {
                    solrQuery.append(":(").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), fp.isLookUp()));
                    solrQuery.append(")");
                }

                if (!fp.isLookUp()) {
                    SolrSearchUtils searchUtils = new SolrSearchUtils();
                    searchUtils.generateSearchQuery(solrQuery, QueryBuilderForSolr.getCrmContactSearchFields(), fp.getSearchKey());
                }
                solrQuery.append(")");
            }
        }

        //If Client Access enabled
        if (fp.isAccessEnabled()) {
            solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_ACCESS_ENABLED).append(":").append(fp.isAccessEnabled());
        }
        // for mycontacts widget
        if (fp.getContactID() != null && !StringUtils.isNotBlank(fp.getSearchKey())) {
            solrQuery.append(" AND -").append(SolrContactRepresenter.FIELD_CONTACT_ID).append(":").append(fp.getContactID());
        }
        //End Of If Client Access enabled
        //If filter by CrmAccount Type
        if (StringUtils.isNotBlank(fp.getAccountType())) {
            solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_CRM_ACCOUNT_TYPE).append(":").append(fp.getAccountType());
        }
        //End of filter by CrmAccount Type

        if (!hasCustomFullAccessToListing) {
            boolean ownerAccess = ServerUtils.hasPermission(PermissionConstants.CONTACT_SEE_OWN);
            StringBuilder clientIDsStr = new StringBuilder();
            if (fp.getClientId() != null) {
                EdsCrmAccount crmAccount = crmAccountManager.get(fp.getClientId());
                ownerAccess = ownerAccess && crmAccount.getOwners().contains(user);
            }
            if (ownerAccess && !user.hasRole(EdsRole.ADMIN_CODE)) {
                List<Integer> clientIDs = crmAccountManager.getAccountIDsByOwner(user.getObjectID());
                if (clientIDs != null && clientIDs.size() > 0) {
                    for (Integer clientID : clientIDs) {
                        clientIDsStr.append(" ").append(clientID);
                    }
                }
            }

            if (!clientIDsStr.toString().trim().isEmpty()) {
                solrQuery.append(" AND (");
                solrQuery.append(SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID).append(":(").append(clientIDsStr.toString().trim()).append(") ");
                solrQuery.append(")");
            }
            if (!ownerAccess && !user.hasRole(EdsRole.ADMIN_CODE)) {
                solrQuery.append(" AND ( ");
                solrQuery.append(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID).append(":").append(user.getObjectID());
                solrQuery.append(")");
            }
        }

        if (fp.isFiltirize()) {
            if (fp.getAccountID() != null) {
                solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID).append(":").append(fp.getAccountID());
            }
            if (fp.getCampaignID() != null) {
                solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_CAMPAIGN_ID).append(":").append(fp.getCampaignID());
            }

            if (contactFilter != null && contactFilter.getFacetContentMap().containsKey(FacetContentType.ContactFacetFilter.getContentCode()[5])) {
                SelectItem[] items = contactFilter.getFacetContentMap().get(FacetContentType.ContactFacetFilter.getContentCode()[5]).getFacetItems();
                if (items.length != 0) {
                    solrQuery.append(" AND (");
                    boolean appendOperator = false;
                    for (SelectItem item : items) {
                        if (appendOperator) {
                            solrQuery.append(!fp.useAndOperator() ? " OR " : " AND ");
                        } else {
                            appendOperator = true;
                        }
                        if (fp.useAndOperator() && item.getDescription() != null && !"".equals(item.getDescription().trim())) {
                            solrQuery.append(SolrContactRepresenter.FIELD_CATEGORY_ID).append(":(").append(item.getId()).append(" ").append(item.getDescription()).append(")");
                        } else {
                            solrQuery.append(SolrContactRepresenter.FIELD_CATEGORY_ID).append(":").append(item.getId());
                        }
                    }
                    solrQuery.append(") ");
                }
            }
        }
        solrQuery.append(")");

        if (fp.isFiltirize() && fp.isFromOutlook()) {
            EdsUser edsUser = (EdsUser) ServerSecurityContext.getInstance().getUser();
            solrQuery.append("OR (");
            solrQuery.append(QueryBuilderForSolr.getLeadListFacetFilterAssigneeQuery(edsUser.getCompany(), edsUser, fp, null, null));
            solrQuery.append(")");
        }

        return solrQuery.toString();
    }

    private Set<String> getContactDuplicateNames(String solrQuery, List<Integer> inIDs) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CONTACT_CORE);
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(0);
        query.setRows(10000);
        QueryResponse resp = null;
        try {
            resp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return crmContactManager.getDuplicateNamesSet(SolrUtils.getIdsFromSolrDocument(SolrContactRepresenter.FIELD_CONTACT_ID, resp.getResults()), inIDs);
    }

    private SelectItem[] getCountryList() {
        return getAsSelectItem(countryManager.list(), ServerUtils.EDS_COUNTRY);
    }

    private String getEmptyIfNull(String value) {
        return value == null ? "" : value;
    }

    private String getFormatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String hour = String.valueOf(date.getHours());
        if (date.getHours() < 10) {
            hour = "0" + hour;
        }
        String minut = String.valueOf(date.getMinutes());
        if (date.getMinutes() < 10) {
            minut = "0" + minut;
        }

        return simpleDateFormat.format(date) + " [" + hour + ":" + minut + "]";
    }

    public String getImageUrl(Integer id) {
        return uploadManager.getFileURL(id);
    }

    private SelectItem[] getAsSelectItem(List listOfObject, final int type) {
        return ServerUtils.getAsSelectItem(listOfObject, type);
    }

    private SelectItem[] getRegionList() {
        return getAsSelectItem(regionManager.list(), ServerUtils.EDS_REGION);
    }

    private SolrQuery.ORDER getSolrOrder(boolean desc) {
        return desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc;
    }

    private ListingFilterParameter initFilterParametrs(ListingFilterParameter filterParametrs) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        return filterParametrs;
    }

    public Integer convertEmailToCase(EdsEmail email, List<EdsEmailFilter> filters) {
        email = prepareToFilter(email, filters);
        List<Integer> ids = new ArrayList<>();
        if (email != null) {
            List<EdsEmail> messages = new ArrayList<>();
            messages.add(email);
            Map<Boolean, List<Integer>> solrIDsToUpdate = convertEmailToCase(messages);
            if (solrIDsToUpdate.get(Boolean.TRUE).size() > 0) {
                ids.addAll(solrIDsToUpdate.get(Boolean.TRUE));
                onCasesHaveBeenFetchedAndCreated(solrIDsToUpdate.get(Boolean.TRUE), true, email.getEmailSettingId(), true);
            }
            if (solrIDsToUpdate.get(Boolean.FALSE).size() > 0) {
                ids.addAll(solrIDsToUpdate.get(Boolean.FALSE));
                onCasesHaveBeenFetchedAndCreated(solrIDsToUpdate.get(Boolean.FALSE), false, email.getEmailSettingId(), true);
            }
        }
        return ids.size() > 0 ? ids.get(0) : null;
    }

    @Transactional(readOnly = true)
    public EdsEmail prepareToFilter(EdsEmail email, List<EdsEmailFilter> filters) {
        KPIMimeMessage message = mailServices.getService(email.getEmailSettingId()).toMimeMessage(email);
        if (message != null) {
            email.filter(filters, message);
        }

        return email;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Map<Boolean, List<Integer>> convertEmailToCase(List<EdsEmail> messages) {
        Map<Boolean, List<Integer>> changedCaseIds = new HashMap<>();
        changedCaseIds.put(Boolean.TRUE, new ArrayList<>());
        changedCaseIds.put(Boolean.FALSE, new ArrayList<>());
        if (!messages.isEmpty()) {
            Map<Integer, Integer> casesWithTrackers = caseManager.getCasesWithTrackerIDs();
            EdsUser user = caseManager.getUser();
            boolean convertEachCase = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CONVERT_EACH_EMAIL_TO_CASE);
            for (EdsEmail email : messages) {
                if (!convertEachCase && email != null && email.getTrackerId() != null && casesWithTrackers.containsKey(email.getTrackerId())) {
                    Integer id = casesWithTrackers.get(email.getTrackerId());
                    if (id != null) {
                        if (!changedCaseIds.get(Boolean.FALSE).contains(id)) {
                            EdsCase crmCase = caseManager.get(id);
                            if (crmCase != null) {
                                crmCase.setEmailID(null);
                                if (email.hasCaseFilter()) {
                                    crmCase.clear();
                                    crmCase.setEmailID(email.getId());
                                    crmCase.setStatus(referenceManager.findReference(EdsCase._CASE_STATUS, EdsCase.WAITING_FOR_REPLY));
                                    caseManager.update(crmCase);
                                    EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, crmCase, user);
                                    workflowEvent.setEntityType(RelationItem.TYPE_CASE);
                                }
                                changedCaseIds.get(Boolean.FALSE).add(id);
                                log.info("Convert email[" + email.getId() + "] to existing case [" + id + "]");
                            }
                        }
                    }
                } else if (email.hasCaseFilter()) {
                    Integer id = createCaseByFetchedEmail(email);
                    if (id != null && id > 0) {
                        changedCaseIds.get(Boolean.TRUE).add(id);
                        log.info("Convert email[" + email.getId() + "] to case [" + id + "]");
                    }
                }
            }
        }
        return changedCaseIds;
    }

    @Override
    public void onCasesHaveBeenFetchedAndCreated(List<Integer> caseIDs, boolean isNewCreateCases, Integer emailSettingID, boolean fromRecurrence) {
        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        if (caseIDs != null && !caseIDs.isEmpty()) {
            if (fromRecurrence) {
                List<EdsCase> casesToAddToSolr = caseManager.getCasesByIDs(caseIDs);
                if (casesToAddToSolr != null && !casesToAddToSolr.isEmpty()) {
                    try {
                        caseSolrComponent.indexes(casesToAddToSolr);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isNewCreateCases) {
                        log.info("Sending Autoresponses");
                        for (EdsCase crmCase : casesToAddToSolr) {
                            EdsEmailFilter filter = crmCase.getFilterID() != null ? emailFilterManager.get(crmCase.getFilterID()) : null;
                            if (filter == null || filter.isSendAutoresponse()) {
                                sendAutoResponseToCase(crmCase.getObjectID(), emailSettingID, crmCase.getFilterID());
                            }
                        }
                    }
                }
            } else {
                String ids = ServerUtils.getAsCommoDelimited(caseIDs, "0", ",");
                EdsCase crmCase = new EdsCase();
                crmCase.setObjectID(caseIDs.get(0));
                EdsBusinessEvent solrEvent = baseEventPostProcessor.registerEvent(CrmCaseEventListeneImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, crmCase, userManager.getUser());
                solrEvent.setCustomStringField(ids);
                if (isNewCreateCases) {
                    EdsEmailSetting s = new EdsEmailSetting();
                    s.setObjectID(emailSettingID);
                    EdsBusinessEvent autoResponseEvent = baseEventPostProcessor.registerEvent(EmailFetchingCustomEventListenerImpl.TYPE_EMAIL_FETCHING, EmailFetchingCustomEventListenerImpl.EVENT_SEND_AUTORESPONSE, s, userManager.getUser());
                    autoResponseEvent.setCustomStringField(ids);
                }
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer createCaseByFetchedEmail(EdsEmail email) {
        EdsEmailTracker tracker = emailTrackerManager.get(email.getTrackerId());
        Integer companyID = SecurityContext.getCompanyID();
        EdsUser user = userManager.getUser();
        String from = email.getFrom();
        String fullName = "";
        if (from != null && from.contains("<") && from.contains(">")) {
            fullName = from.substring(0, from.indexOf("<"));
            int start = from.indexOf("<") + 1;
            int end = from.indexOf(">", start + 1);
            from = from.substring(start, end);
        } else if (from != null && from.contains("\"")) {
            fullName = from.substring(from.indexOf("\"") + 1, from.lastIndexOf("\""));
            String[] e = from.split("\\s");
            for (String e_ : e) {
                if (e_.contains("@")) {
                    from = e_;
                }
            }
        }
        String content = null;
        /*try {
            content = EmailUtils.retrieveContent(message.getContent(), null, new StringBuilder(), null).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        EdsCase crmCase = new EdsCase();
        crmCase.setSubject(email.getSubject());
        crmCase.setFilterID(email.getFilter(EmailFilter.CREATE_CASE).getObjectID());
        //Details
        /*EdsEmailDetails caseDetails = new EdsEmailDetails();
        if (email.getToCC() != null && !"".equals(email.getToCC())) {
            caseDetails.settoCC(email.getToCC());
        }
        caseDetails.setReplyTo(email.getReplyTo());
        caseDetails.setDescription(content);
        jpaTemplate.persist(caseDetails);
        crmCase.setCrmCaseDetails(caseDetails);*/
        //Case Fields
        crmCase.setEmailID(email.getId());
        crmCase.setTracker(tracker);
        crmCase.setCaseOrigion(referenceManager.findReference(EdsCase._CASE_ORIGIN, EdsCase.EMAIL));
        crmCase.setPriority(referenceManager.findReference(EdsCase._CASE_PRIORITY, EdsCase.CP_MEDIUM));
        crmCase.setStatus(referenceManager.findReference(EdsCase._CASE_STATUS, EdsCase.NEW));
        Integer assigneeId = email.getFilter(EmailFilter.CREATE_CASE).getAssigneeID();
        crmCase.setAssignee(assigneeId != null ? userManager.get(assigneeId) : null);
        Integer departmentId = email.getFilter(EmailFilter.CREATE_CASE).getDepartmentID();
        crmCase.setDepartment(departmentId != null ? departmentManager.get(departmentId) : null);
        Integer resolverId = email.getFilter(EmailFilter.CREATE_CASE).getResolverID();
        crmCase.setResolver(resolverId != null ? userManager.get(resolverId) : null);
        //Contact,Lead,Accounts
        if (user != null && user.isClientContact() && user.getClientContact().getCrmContact() != null) {
            crmCase.setCrmContact(user.getClientContact().getCrmContact());
        }
        if (from != null) {
            EdsCrmContact crmContact = crmContactManager.getContactByEmail(from, companyID);
            if (crmContact == null) {
                EdsCrmContact lead = crmContactManager.getLeadByEmail(from, companyID);
                if (lead == null) {
                    EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountByEmail(from, companyID);
                    if (crmAccount == null) {
                        ContactListItem leadRPC = new ContactListItem();
                        leadRPC.getHomeEmail().add(from);
                        if (!"".equals(fullName.trim())) {
                            if (fullName.contains(" ")) {
                                leadRPC.setFirstName(fullName.substring(0, fullName.indexOf(" ")));
                                leadRPC.setLastName(fullName.substring(fullName.indexOf(" ") + 1));
                            } else {
                                leadRPC.setLastName(fullName);
                            }
                        } else if (from.contains("@")) {
                            leadRPC.setLastName(from.substring(0, from.indexOf("@")));
                        }
                        if (tracker != null) {
                            tracker.setContactId(leadRPC.getObjectId());
                            leadRPC.addTrackerId(email.getTrackerId());
                        }
                        Integer leadID = saveLead(leadRPC, null);
                        crmCase.setLead(crmContactManager.get(leadID));
                    } else {
                        crmCase.setCrmAccount(crmAccount);
                    }
                } else {
                    if (tracker != null) {
                        tracker.setContactId(lead.getObjectID());
                        lead.addTracker(tracker);
                    }
                    crmCase.setLead(lead);
                }
            } else {
                if (tracker != null) {
                    tracker.setContactId(crmContact.getObjectID());
                    crmContact.addTracker(tracker);
                }
                crmCase.setCrmContact(crmContact);
            }
        }
        createEntity(crmCase);
        if (crmCase.getCaseNumberString() == null || "".equals(crmCase.getCaseNumberString())) {
            generateCaseNumber(crmCase);
        }
        caseManager.createOrUpdate(crmCase);
        updateCaseHistory(commonLocalizer.localize("createdCase", "Created the Case"), crmCase);
        baseEventPostProcessor.registerEvent(TelegramChatEventListenerImpl.TYPE, TelegramConstants.SEND_CASE_CREATE, crmCase, user);
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, crmCase, user);
        workflowEvent.setEntityType(RelationItem.TYPE_CASE);
        Integer newCaseID = crmCase.getObjectID();

//        Integer templateID = email.getFilter(EmailFilter.CREATE_CASE).getEmailTemplateID();
//        email.setEmailTemplate(templateID != null ? emailTemplateManager.get(templateID) : null);
        //Relations
        String emailSubject = email.getSubject();
        String caseSubject = crmCase.getSubject();
        ArrayList<RelationItem> emailRelationItems = new ArrayList<>();
        ArrayList<RelationItem> caseRelationItems = new ArrayList<>();
        caseRelationItems.add(new RelationItem(null, email.getTrackerId(), RelationItem.TYPE_EMAIL_TRACKER, emailSubject, null, null, null));
        //Project create
        Integer projectID = email.getFilter(EmailFilter.CREATE_CASE).getProjectTemplateID();
        Integer newProjectID = null;
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.COPY_PROJECT_TEMPLATE_ENABLED) && projectID != null) {
            newProjectID = copyAndCreateProject(projectID, emailSubject, email.getId());
            if (newProjectID != null) {
                caseRelationItems.add(new RelationItem(null, newProjectID, RelationItem.TYPE_PROJECT, emailSubject, null, null, null));
                emailRelationItems.add(new RelationItem(null, newProjectID, RelationItem.TYPE_PROJECT, emailSubject, null, null, null));
            }
        }

        // Emaildan case yaratilganda shu caseni email filteridagi relationlar case va emailga ko`chishi kk. (Munirni taski)
//        List<EdsEmailFilter> parentsOnly = emailFilterManager.getParentsOnly();
//        if (parentsOnly != null && parentsOnly.size() > 0) {
//            try {
//                for (EdsEmailFilter parentFilter : parentsOnly) {
//                    if (parentFilter.getSubFilters() != null && parentFilter.getSubFilters().size() > 0) {
//                        if (parentFilter.asSearchTerm() == null || "".equals(parentFilter.asSearchTerm()) || message.match(parentFilter.asSearchTerm())) {
//                            for (EdsEmailFilter childFilter : parentFilter.getSubFilters()) {
//                                if (childFilter.getSubFilters() != null && childFilter.getSubFilters().size() > 0) {
//                                    for (EdsEmailFilter rule : childFilter.getSubFilters()) {
//                                        if (rule != null && message.match(rule.asSearchTerm())) {
//                                            emailRelationItems.addAll(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_EMAIL_FILTER, rule.getObjectID())));
//                                            caseRelationItems.addAll(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_EMAIL_FILTER, rule.getObjectID())));
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
//            } catch (MessagingException e) {
//                e.printStackTrace();
//            }
//        }
        allInOneServiceLocal.saveRelations(RelationItem.TYPE_CASE, newCaseID, caseSubject, caseRelationItems);
        allInOneServiceLocal.saveRelations(RelationItem.TYPE_EMAIL_TRACKER, email.getTrackerId(), emailSubject, emailRelationItems);

        if (content != null) {
            EdsCase.addTempContentReady(companyID, newCaseID, content);
        }
        return newCaseID;
    }

    private Integer copyAndCreateProject(Integer projectID, String projectName, String caseEmailID) {
        EdsProject project = projectManager.get(projectID);
        CloneProjectItem cloneItem = new CloneProjectItem();
        cloneItem.setProjectId(projectID);
        NumberData numberData = projectServiceLocal.generateProjectNumber(new Date(), null, null);
        cloneItem.setNumberData(numberData);
        if (projectName != null && !"".equals(projectName)) {
            if (projectName.length() > 255) {
                projectName = projectName.substring(0, 254);
            }
            cloneItem.setProjectName(projectName);
        } else {
            cloneItem.setProjectName(project.getName());
        }
        cloneItem.setProjectDescription(project.getDescription());
        cloneItem.setDueDate(project.getDueDate());
        cloneItem.setStartDate(new Date());
        cloneItem.setStatusId(project.getStatus() != null ? project.getStatus().getObjectID() : null);
        cloneItem.setClientId(project.getClient() != null ? project.getClient().getObjectID() : null);
        cloneItem.setParentId(project.getParent() != null ? project.getParent().getObjectID() : null);
        cloneItem.setManager(project.getManager() != null ? project.getManager().getObjectID() : null);
        ProjectMember[] projectMembers = projectServiceLocal.getProjectEmployees(projectID);
        cloneItem.setMembers(projectMembers);
        cloneItem.setLocationId(project.getProjectLocation() != null ? project.getProjectLocation().getObjectID() : null);
        cloneItem.setBackupManagerIDs(project.getBackupManagerIDs());
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Project);
        cloneItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(project.getProjectCustomFields(), customFieldsItems));
        Integer newProjectID = null;
        //project source
        cloneItem.setProjectSource(PROJECT_SOURCE_COPY_TO_PROJECT_FROM_CASE + caseEmailID + "_" + projectID);
        try {
            newProjectID = projectServiceLocal.saveCloneProject(cloneItem);
        } catch (NumberExistingException e) {
            e.printStackTrace();
        }
        return newProjectID;
    }

    private void saveCrmNote(ListingFilterParameter fp, String comment, EdsUser user) {
        Integer objectID = fp.getCaseID() != null ? fp.getCaseID() : (fp.getLeadID() != null ? fp.getLeadID() : (fp.getAccountID() != null ? fp.getAccountID() : (fp.getContactID() != null ? fp.getContactID() : (fp.getOpportunityID() != null ? fp.getOpportunityID() : (fp.getCampaignID() != null ? fp.getCampaignID() : fp.getCaseID())))));
        EdsUser employee = user != null ? user : (fp.getEmployeeId() != null ? userManager.get(fp.getEmployeeId()) : null);
        boolean isLead = fp.getLeadID() != null;
        boolean isCrmAccount = fp.getAccountID() != null;
        boolean isCrmContact = fp.getContactID() != null;
        boolean isOpportunity = fp.getOpportunityID() != null;
        boolean isCase = fp.getCaseID() != null;
        boolean isCampaign = fp.getCampaignID() != null;
        if (objectID != null) {
            EdsNoteHistory edsCrmNote = new EdsNoteHistory();
            edsCrmNote.setComment(comment);
            edsCrmNote.setEmployee(employee);
            edsCrmNote.setEventDate(new Date());
            edsCrmNote.setRelatedId(objectID);
            if (isCrmContact) {
                EdsCrmContact contact = crmContactManager.get(objectID);
                if (contact != null) {
                    if (contact.is(EdsCrmContact.LEAD_CONTACT)) {
                        isLead = true;
                    } else {
                        edsCrmNote.setRelatedTo(EdsNoteHistory.CRM_CONTACT);
                        edsCrmNote.setEntityID(contact.getEntityID());
                    }
                }
            }
            if (isLead) {
                final EdsCrmContact lead = crmContactManager.get(objectID);
                if (lead != null) {
                    edsCrmNote.setEntityID(lead.getEntityID());
                    edsCrmNote.setRelatedTo(EdsNoteHistory.CRM_CONTACT);
                    contactServiceLocal.updateEdsCrmContactAndIndex(lead, false, user);
                }
            }
            if (isCrmAccount) {
                edsCrmNote.setRelatedTo(EdsNoteHistory.CRM_ACCOUNT);
            }
            if (isOpportunity) {
                edsCrmNote.setRelatedTo(EdsNoteHistory.CRM_OPPORTUNITY);
                updateOpportunity(opportunityManager.get(objectID));
            }
            if (isCampaign) {
                edsCrmNote.setRelatedTo(EdsNoteHistory.CRM_CAMPAIGN);
            }
            if (isCase) {
                EdsCase crmCase = caseManager.get(fp.getCaseID());
                edsCrmNote.setRelatedTo(EdsNoteHistory.CRM_CASE);
                if (crmCase != null) {
                    caseManager.update(crmCase);
                    try {
                        /*solrManager.indexAddCase(crmCase);*/
                        caseSolrComponent.index(crmCase);
                    } catch (Exception e) {
                        baseEventPostProcessor.registerEvent(CrmCaseEventListeneImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, crmCase, userManager.getUser());
                        e.printStackTrace();
                    }
                }
            }
            noteHistoryManager.create(edsCrmNote);
            if (edsCrmNote.getRelatedTo() == EdsNoteHistory.CRM_CONTACT) {
                EdsCrmContact contact = crmContactManager.get(edsCrmNote.getRelatedId());
                if (contact != null && !(contact.is(EdsCrmContact.LEAD_CONTACT) || contact.is(EdsCrmContact.CANDIDATE))) {
                    String subject = edsCrmNote.getComment();
                    if (subject != null && subject.length() > 10) {
                        subject = subject.substring(0, 10) + "...";
                    }
                    createContactHistory("Added a note - " + "\"" + subject + "\"", contact);
                }
            }
        }
    }

    @Transactional
    public boolean createContactHistory(String message, EdsCrmContact contact) {
        EdsContactHistory contactHistory = new EdsContactHistory();
        contactHistory.setCreationTime(new Date());
        if (userManager.getUser() != null && userManager.getUser().getObjectID() != null) {
            contactHistory.setUpdaterId(userManager.getUser().getObjectID());
        }
        if (contact != null) {
            contactHistory.setContactId(contact.getObjectID());
        }
        contactHistory.setMessage(message);
        contactHistory.setSuperUser(ServerUtils.isSuperUser());
        contactHistoryManager.create(contactHistory);
        return contactHistory.getObjectID() != null;
    }

    public boolean createContactHistory(String message, Integer contactId, Integer userId) {
        EdsContactHistory contactHistory = new EdsContactHistory();
        contactHistory.setCreationTime(new Date());
        contactHistory.setUpdaterId(userId);
        contactHistory.setContactId(contactId);
        contactHistory.setMessage(message);
        contactHistory.setSuperUser(ServerUtils.isSuperUser());
        contactHistoryManager.create(contactHistory);
        return contactHistory.getObjectID() != null;
    }


    private void updateCaseHistory(String message, EdsCase crmCase) {
        EdsCaseHistory caseHistory = new EdsCaseHistory();
        caseHistory.setCreationTime(new Date());
        caseHistory.setUpdater(userManager.getUser());
        caseHistory.setCrmCase(crmCase);
        caseHistory.setMessage(message);
        caseHistory.setSuperUser(ServerUtils.isSuperUser());
        caseHistoryManager.create(caseHistory);
    }

    @Transactional
    public void updateWebForms() {
        List<EdsWebForm> webForms = webFormManager.getCompanyWebFormsIncludeDeleteds();
        for (EdsWebForm webForm : webForms) {
            webFormManager.updateUrl(webForm);
        }
        webFormManager.flushAndClear();
    }

    private void saveOpportunityItems(EdsOpportunity opportunity, OpportunityItem[] items) {
        //Before clear all old items of the Opportunity
        opportunityManager.deleteItems(opportunity.getObjectID());

        if (items != null) {
            for (OpportunityItem item : items) {
                EdsOpportunityItem opportunityItem = new EdsOpportunityItem();
                opportunityItem.setOpportunity(opportunity);
                if (item.getItemID() != null) {
                    opportunityItem.setItem(itemManager.get(item.getItemID()));
                }

                opportunityItem.setItemName(item.getItemName());
                opportunityItem.setDescription(item.getDescription());
                opportunityItem.setQty(item.getQty());
                opportunityItem.setPrice(item.getPrice());
                opportunityItem.setDiscount(item.getDiscountPercent());
                opportunityItem.setDiscountAmount(item.getDiscountAmount());
                if (item.getDiscountItemFixedType() != null) {
                    opportunityItem.setDiscountItemFixedType(item.getDiscountItemFixedType());
                    opportunityItem.setItemDiscount(null);
                }
                if (item.getDiscountItemID() != null) {
                    opportunityItem.setItemDiscount(discountManager.get(item.getDiscountItemID()));
                    opportunityItem.setDiscountItemFixedType(null);
                }
                if (item.getUnitMeasurement() != null && item.getUnitMeasurement().getId() != null) {
                    opportunityItem.setUnitMeasurement(unitMeasurementManager.get(item.getUnitMeasurement().getId()));
                }
                opportunityItem.setSupplierID(item.getSupplierID());
                if (item.getSupplierName() != null) {
                    opportunityItem.setSupplierName(item.getSupplierName());
                }

                if (item.getProductCategory() != null) {
                    opportunityItem.setCategory(productCategoryManager.get(item.getProductCategory().getId()));
                }
                if (item.getProductBrand() != null) {
                    opportunityItem.setBrand(brandManager.get(item.getProductBrand().getId()));
                }
                if (item.getTaxItem() != null && item.getTaxItem().getId() != null) {
                    opportunityItem.setVat(vatManager.get(item.getTaxItem().getId()));
                }
                if (item.getProject() != null && item.getProject().getId() != null) {
                    opportunityItem.setProject(projectManager.get(item.getProject().getId()));
                }
                opportunityItem.setTaxAmount(item.getTaxAmount());
                opportunityItem.setNet(item.getNet());
                opportunityItem.setSubTotal(item.getSubTotal());
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.OPPORTUNITY_ITEM_TABLE_CONVERT_CF) && opportunityItem.getItem() != null && productService.getProductBaseData(opportunityItem.getItem().getObjectID()) != null) {
                    ArrayList<CompanyCustomFieldItem> productCustomFields = productService.getProductBaseData(opportunityItem.getItem().getObjectID(), true).getProductCustomFieldItems();

                    if (productCustomFields != null && productCustomFields.size() > 0) {
                        setValueStaticFieldFromCFByAliasName(opportunityItem, productCustomFields);


                        ArrayList<CompanyCustomFieldItem> opportunityItemCustomFields = item.getItemCustomFields();

                        ArrayList<CompanyCustomFieldItem> opportunityAllItemCustomFields = commonService.getCompanyAllCustomFields(ViewName.OpportunitySubItem);
                        if (opportunityAllItemCustomFields != null && !opportunityAllItemCustomFields.isEmpty() && opportunityItemCustomFields != null) {
                            for (CompanyCustomFieldItem companyCustomFieldItem : opportunityAllItemCustomFields) {
                                if (!opportunityItemCustomFields.contains(companyCustomFieldItem)) {
                                    opportunityItemCustomFields.add(companyCustomFieldItem);
                                }
                            }
                        }

                        if (opportunityItemCustomFields != null && !opportunityItemCustomFields.isEmpty()) {
                            for (CompanyCustomFieldItem opportunityCF : opportunityItemCustomFields) {
                                for (CompanyCustomFieldItem productCF : productCustomFields) {
                                    if (opportunityCF.getDataType().equals(productCF.getDataType())
                                            && opportunityCF.getUiType().equals(productCF.getUiType())
                                            && opportunityCF.getAliasName().equals(productCF.getAliasName())
                                            && (opportunityCF.getFieldStringValue() == null || (opportunityCF.getFieldStringValue() != null && opportunityCF.getFieldStringValue().length() == 0))) {
                                        opportunityCF.setPredefinedValues(productCF.getPredefinedValues());
                                        opportunityCF.setPredefinedValuesWithSorting(productCF.getPredefinedValuesWithSorting());
                                        opportunityCF.setQuery(productCF.getQuery());
                                        opportunityCF.setQueryItems(productCF.getQueryItems());
                                        opportunityCF.setFieldStringValue(productCF.getFieldStringValue());
                                        opportunityCF.setFieldDateNonConvertedValue(productCF.getFieldDateNonConvertedValue());
                                        opportunityCF.setAttachments(productCF.getAttachments());
                                        opportunityCF.setLookUpTypeEnum(productCF.getLookUpTypeEnum());
                                        opportunityCF.setSelectedId(productCF.getSelectedId());
                                        opportunityCF.setDefaultValue(productCF.getDefaultValue());
                                        opportunityCF.setPrefix(productCF.getPrefix());
                                        opportunityCF.setItem(productCF.getItem());
                                        opportunityCF.setSelectItems(productCF.getSelectItems());
                                    }
                                }
                            }
                        }
                    }
                }

                opportunityItem.setCustomFields(saveItemCustomFields(opportunityItem.getCustomFields(), item.getItemCustomFields()));
                opportunity.getOpportunityItems().add(opportunityItem);
            }
        }
    }

    private void setValueStaticFieldFromCFByAliasName(EdsOpportunityItem opportunityItem, ArrayList<CompanyCustomFieldItem> productCustomFieldItems) {
        for (CompanyCustomFieldItem productCFItem : productCustomFieldItems) {
            if (productCFItem != null && productCFItem.getAliasName() != null) {
                switch (productCFItem.getAliasName()) {
                    case ItemTableConstants.DESCRIPTION -> {
                        if ((opportunityItem.getDescription() == null || opportunityItem.getDescription() != null && opportunityItem.getDescription().length() == 0) &&
                                productCFItem.getFieldStringValue() != null && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType()))) {
                            opportunityItem.setDescription(productCFItem.getFieldStringValue());
                        }
                    }
                    case ItemTableConstants.QTY -> {
                        if (opportunityItem.getQty() == null &&
                                productCFItem.getFieldStringValue() != null && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            opportunityItem.setQty(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                    }
                    case ItemTableConstants.MEASUREMENT -> {
                        if (opportunityItem.getUnitMeasurement() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.UNIT_MEASUREMENT.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setUnitMeasurement(unitMeasurementManager.get(productCFItem.getSelectedId()));
                        }
                    }
                    case ItemTableConstants.UNITPRICE -> {
                        if (opportunityItem.getPrice() == null &&
                                productCFItem.getFieldStringValue() != null && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            opportunityItem.setPrice(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                    }
                    case ItemTableConstants.DISCOUNT_AMT -> {
                        if (opportunityItem.getDiscountAmount() == null &&
                                productCFItem.getFieldStringValue() != null && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            opportunityItem.setDiscountAmount(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                    }
                    case ItemTableConstants.SUPPLIER -> {
                        if (opportunityItem.getSupplierID() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.SUPPLIER.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setSupplierID(productCFItem.getSelectedId());
                            opportunityItem.setSupplierName(productCFItem.getFieldStringValue());
                        }
                    }
                    case ItemTableConstants.CATEGORY -> {
                        if (opportunityItem.getCategory() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.PRODUCT_CATEGORY.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setCategory(productCategoryManager.get(productCFItem.getSelectedId()));
                        }
                    }
                    case ItemTableConstants.PROJECT -> {
                        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE) && opportunityItem.getProject() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.PROJECT.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setProject(projectManager.get(productCFItem.getSelectedId()));
                        }
                    }
                }
            }
        }
    }

    @Transactional
    @Override
    public Integer opportunityConvertToProject(Integer opportunityId, Integer accountID, FileItem contract) {
        EdsOpportunity opportunity = opportunityManager.get(opportunityId);
        return opportunityConvertToProject(opportunity, false, contract);
    }

    @Override
    @Transactional
    public Double getCrmAccountBalance(Integer accountId) {
        Double customerBalance = accountId != null && crmAccountManager.getClientBalance(accountId) != null ?
                crmAccountManager.getClientBalance(accountId).doubleValue() : 0d;
        return customerBalance;
    }

    private Integer opportunityConvertToProject(EdsOpportunity opportunity, boolean solrIndexed, FileItem contract) {
        EdsEmployee assignee = opportunity.getAssignee();
        EdsEmployee backupManager = employeeManager.get(userManager.getUser().getObjectID());
        ArrayList<Integer> backupManagerIDs = new ArrayList<>();
        opportunity.setConvertedToProject(true);
        if (assignee == null) {
            assignee = employeeManager.get(opportunityManager.getUser().getObjectID());
        } else if (!Objects.equals(assignee.getObjectID(), userManager.getUser().getObjectID())) {
            backupManagerIDs.add(userManager.getUser().getObjectID());
        }
        ProjectSingleItem project = new ProjectSingleItem();
        if (contract != null) {
            project.setAttachments(new FileItem[]{contract});
        }
        if (backupManagerIDs.size() > 0) {
            project.setBackupManagerIDs(backupManagerIDs);
        }
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.OPPORTUNITY_CONVERTED_TO_PROJECT_AUTO_NUMBERING)) {
            project.setNumberData(projectServiceLocal.generateProjectNumber(opportunity.getCreationDate(), opportunity.getCrmAccount().getObjectID(), null));
        } else {
            project.setNumberData(new NumberData(opportunity.getNumber(), opportunity.getIntNumber()));
        }
        project.setName(opportunity.getName());
        project.setManagerId(assignee.getObjectID());
        project.setStartDate(opportunity.getCreationDate());
        project.setEndDate(opportunity.getClosingDate());

        EdsReference notStarted = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.NOT_STARTED);
        project.setStatusId(notStarted.getObjectID());

        ProjectMember manager = new ProjectMember();
        ProjectMember firstBackupManager = new ProjectMember();
        ProjectMember[] members = new ProjectMember[assignee.getObjectID().equals(backupManager.getObjectID()) ? 1 : 2];
        manager.setId(assignee.getObjectID());
        manager.setWageRate(assignee.getWageRate());
        manager.setClientChargeRate(assignee.getClientChargeRate());
        firstBackupManager.setId(backupManager.getObjectID());
        firstBackupManager.setWageRate(backupManager.getWageRate());
        firstBackupManager.setClientChargeRate(backupManager.getClientChargeRate());
        members[0] = manager;
        if (!assignee.getObjectID().equals(backupManager.getObjectID())) {
            members[1] = firstBackupManager;
        }
        project.setProjectMembers(members);

        ArrayList<CompanyCustomFieldItem> projectCustomFields = commonService.getCompanyCustomFields(ViewName.Project);
        List<CompanyCustomFieldItem> opportunityCustomFields = CustomFieldsUtils.setRPCCustomFieldItems(opportunity.getCustomFields(),
                commonService.getCompanyCustomFields(ViewName.Opportunity));
        for (CompanyCustomFieldItem projectCustomFieldItem : projectCustomFields) {
            for (CompanyCustomFieldItem fieldItem : opportunityCustomFields) {
                if ((StringUtils.equals(projectCustomFieldItem.getAliasName(), fieldItem.getAliasName())) && (StringUtils.equals(projectCustomFieldItem.getDataType(), fieldItem.getDataType()) || StringUtils.equals(DATA_TYPE_TEXT, projectCustomFieldItem.getDataType()))) {
                    if (DATA_TYPE_DATE.equals(projectCustomFieldItem.getDataType())) {
                        projectCustomFieldItem.setFieldDateNonConvertedValue(fieldItem.getFieldDateNonConvertedValue());
                    } else if (DATA_TYPE_NUMBER.equals(projectCustomFieldItem.getDataType())) {
                        projectCustomFieldItem.setFieldStringValue(fieldItem.getFieldStringValue());
                    } else {
                        projectCustomFieldItem.setFieldStringValue(fieldItem.getFieldStringValue());
                    }
                    projectCustomFieldItem.setFacetable(fieldItem.isFacetable());
                    projectCustomFieldItem.setShowInListing(fieldItem.isShowInListing());
                    projectCustomFieldItem.setClickable(fieldItem.isClickable());
                    projectCustomFieldItem.setShowInFilterGrouping(fieldItem.isShowInFilterGrouping());
                }
                projectCustomFieldItem.setObjectId(null);
            }
            projectCustomFieldItem.setObjectId(null);
        }

        project.setCustomFieldItems(projectCustomFields);

        EdsCrmAccount crmAccount = opportunity.getCrmAccount();
        if (crmAccount != null) {
            crmAccount.addAccountType(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER));
            crmAccountManager.update(crmAccount, true);
            project.setClientId(opportunity.getCrmAccount().getObjectID());
        }

        //project source
        if (opportunity.getObjectID() != null) {
            project.setProjectSource(PROJECT_SOURCE_CONVERT_FROM_OPPORTUNITY + opportunity.getObjectID());
        }

        Integer projectID = null;
        try {
            projectID = projectServiceLocal.saveProject(project);
            ArrayList<RelationItem> relations = EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_OPPORTUNITY, opportunity.getObjectID()));
            relations.add(new RelationItem(null, projectID, RelationItem.TYPE_PROJECT, project.getName(), opportunity.getObjectID(), RelationItem.TYPE_OPPORTUNITY, opportunity.getName()));
            if (opportunity.getCrmContact() != null) {
                relations.add(new RelationItem(null, opportunity.getCrmContact().getObjectID(), RelationItem.TYPE_CONTACT, opportunity.getCrmContact().getName(), projectID, RelationItem.TYPE_PROJECT, project.getName()));
            }
            allInOneServiceLocal.saveRelations(RelationItem.TYPE_OPPORTUNITY, opportunity.getObjectID(), opportunity.getName(), relations);
            opportunity.setConvertedToProject(true);
        } catch (NumberExistingException e) {
            e.printStackTrace();
        }
        List<FileResource> fileResources = attachmentUtilsManager.getAttachments(F_OPPORTUNITY, opportunity.getObjectID(), opportunity.getObjectID());
        if (fileResources != null && fileResources.size() > 0) {
            for (FileResource file : fileResources) {
                attachmentUtilsManager.copyFileWhenConvert(F_PROJECT, projectID, file.getObjectId(), projectID, file);
            }
        }
        opportunityManager.update(opportunity);
        //update expense report
        expenseReportManager.updateExpenseReport(opportunity.getObjectID(), projectID);
        if (!solrIndexed) {
            try {
                opportunitySolrComponent.index(opportunity);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return projectID;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createLeadFromSignUpper(String data) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<NewCompany> httpRequest = new HttpEntity<>(NewCompany.fromString(data), httpHeaders);
            restTemplate.postForObject("https://"+ SpringPropertiesUtil.getProperty("bg_hostName")+"/services/api/v2/internal/crm/lead_from_signup", httpRequest, Object.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createActualLeadFromSignUpper(String data) {
        try {
            Integer leadCompanyID =65159;

            if (leadCompanyID != null) {
                Integer leadUserId = 1;

                if (leadCompanyID == 100042) {//1erp.sa companyid 100030, userid = 4
                    leadUserId = 4;//shahir@sahara.com
                }
                SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyClusterType(leadCompanyID));
                System.out.println(">>>>>>>>REGISTER EVENT lead DATABASE = " + SecurityContext.getInstance().getDatabase());
                System.out.println(">>>>>>>>REGISTER EVENT lead companyID = " + leadCompanyID);

                SecurityContext.getInstance().setCompanyId(leadCompanyID);
                EdsCompany comp = companyManager.get(leadCompanyID);
                System.out.println(">>>>>>>>LEAD COMPANY NAME" + comp);
                EdsUser user = employeeManager.get(leadUserId);
                EdsBusinessEvent registrationEvent = baseEventPostProcessor.registerEvent(CompanyRegistrationCustomEventListenerImpl.TYPE, CompanyRegistrationCustomEventListenerImpl.EVENT_REGISTRATION, comp, user);
                System.out.println(">>>>>>>>REGISTER EVENT lead DATABASE = " + SecurityContext.getInstance().getDatabase());
                System.out.println(">>>>>>>>REGISTER EVENT lead companyID = " + leadCompanyID);
                System.out.println(">>>>>>>>REGISTER EVENT lead userID = " + leadUserId);
                registrationEvent.setCompanyId(leadCompanyID);
                registrationEvent.setCustomStringField(data);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createCustomerForTextilefinds(String companyName) {

        EdsCrmAccount customer = new EdsCrmAccount();
        customer.setName(companyName);

        customer.addAccountType(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER));

        crmAccountManager.create(customer);
        crmAccountManager.flush();

        try {
            crmAccountSolrComponent.index(customer);
        } catch (Exception e) {
            log.error("Solr index xatosi: {}", e.getMessage());
        }

        log.info("Customer yaratildi: name={}, id={}", companyName, customer.getObjectID());
    }


    public List<Integer> getEventIdsByIDs(String ids) {
        return eventManager.getEventIdsByIDs(ids);
    }

    public List<Integer> getEventIdsWithLimit(int startat, int limit) {
        return eventManager.getEventIdsWithLimit(startat, limit);
    }

    public EdsEvent getEventByID(Integer eventID) {
        return eventManager.getEventByID(eventID);
    }

    public List<Integer> getOpportunityIdsByIDs(String ids) {
        return opportunityManager.getOpportunityIdsByIDs(ids);
    }

    public List<Integer> getOpportunityIdsWithLimit(int startat, int limit) {
        return opportunityManager.getOpportunityIdsWithLimit(startat, limit);
    }

    public void setJpaTemplate(WfmJpaOperations jpaTemplate) {
        this.jpaTemplate = jpaTemplate;
    }

    private Double[] getLatitudeLongitude(String postCode) {
        Double latitude = null;
        Double longitude = null;
        String link = "http://maps.googleapis.com/maps/api/geocode/xml?address=" + postCode + "&sensor=false";
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            URL url = new URL(link);
            XMLStreamReader parser = factory.createXMLStreamReader(url.openStream());
            while (parser.hasNext()) {
                String tagName = "";
                int CONST = parser.next();
                if (CONST == XMLStreamReader.END_ELEMENT) {
                    continue;
                }
                if (parser.hasName()) {
                    tagName = parser.getName().toString();
                }
                if (CONST == XMLStreamReader.START_ELEMENT && "GeocodeResponse".equals(tagName)) {
                    CONST = parser.next();
                    tagName = readNameFromParser(parser);
                    if (CONST == XMLStreamReader.END_ELEMENT && "GeocodeResponse".equals(tagName)) {
                        break;
                    }
                    if (CONST == XMLStreamReader.START_ELEMENT && "geometry".equals(tagName)) {
                        CONST = parser.next();
                        tagName = readNameFromParser(parser);
                        if (CONST == XMLStreamReader.END_ELEMENT && "geometry".equals(tagName)) {
                            break;
                        }
                        if (CONST == XMLStreamReader.START_ELEMENT && "location".equals(tagName)) {
                            CONST = parser.next();
                            tagName = readNameFromParser(parser);
                            if (CONST == XMLStreamReader.END_ELEMENT && "location".equals(tagName)) {
                                break;
                            }
                            if (CONST == XMLStreamReader.START_ELEMENT && "lat".equals(tagName)) {
                                latitude = Double.parseDouble(parser.getElementText());
                            }
                            if (CONST == XMLStreamReader.START_ELEMENT && "lng".equals(tagName)) {
                                longitude = Double.parseDouble(parser.getElementText());
                            }
                        }
                    }
                }
            }

        } catch (IOException | XMLStreamException ignored) {
        }
        return new Double[]{latitude, longitude};
    }

    private String readNameFromParser(XMLStreamReader parser) {
        if (parser != null && parser.hasName()) {
            return parser.getName().toString();
        }
        return "";
    }

    @Override
    public Integer saveSubsidiaryCrmAccount(CrmAccountItem crmAccountItem, Integer companyID, String transactionType) {
        String crmAccountType = PAYABLE.equals(transactionType) ? CrmAccountItem.CUSTOMER : CrmAccountItem.SUPPLIER;
        EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountBySubsidiary(companyID, null);
        if (crmAccount == null) {
//            System.out.println("COMPANY_ID: " + companyID);
            EdsSubsidiariesCompany subsidiaryCompany = subsidiariesCompanyManager.getSubsidiaryByCompanyID(companyID);

            crmAccountItem.setSubsidiary(new SelectItem(subsidiaryCompany.getObjectID(), subsidiaryCompany.getCompanyName(), subsidiaryCompany.getCurrencyID().toString()));
            //crmAccountItem.setOwnerID(crmAccountManager.getUser().getObjectID());
            crmAccountItem.setOwnerItems(Collections.singletonList(crmAccountManager.getUser().getAsSelectItem()).toArray(new SelectItem[]{}));

            Integer result = crmAccountManager.isAccountNameOrNumberAlreadyExists(crmAccountItem.getName(), crmAccountItem.getNumber(), crmAccountItem.getObjectId());
            int cycleCount = 0;
            while (result != 0) {
                if (result == -1) {
                    crmAccountItem.setName(crmAccountItem.getName() + "_Intercompany");
                }
                result = crmAccountManager.isAccountNameOrNumberAlreadyExists(crmAccountItem.getName(), crmAccountItem.getNumber(), crmAccountItem.getObjectId());
                if (cycleCount >= 10) {
                    result = 0;
                }
                cycleCount++;
            }

            Integer crmAccountID = saveAccount(crmAccountItem, crmAccountType, crmAccountManager.getUser().getObjectID(), false, true, false, false);
            if (crmAccountID == null || crmAccountID <= 0) {
                return null;
            }
            return crmAccountID;
        } else {

            if (PAYABLE.equals(transactionType)) {
                crmAccount.addAccountType(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER));
            } else {
                crmAccount.addAccountType(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.SUPPLIER));
            }
        }

        return crmAccount.getObjectID();
    }

    @Override
    public TypeItem getInterCompanyCrmAccountAsTypeItem(Integer crmAccountID) {
        EdsCrmAccount crmAccount = crmAccountID != null ? crmAccountManager.get(crmAccountID) : null;
        TypeItem crmAccountData = null;
        if (crmAccount != null) {
            crmAccountData = new TypeItem();
            crmAccountData.setId(crmAccount.getObjectID());
            if (crmAccount.getBillingAddress() != null) {
                crmAccountData.setBillAddressID(crmAccount.getBillingAddress().getObjectID());
            }

            if (crmAccountData.getBillAddressID() == null && crmAccount.getBillingAddresses() != null
                    && crmAccount.getBillingAddresses().size() > 0 && crmAccount.getBillingAddresses().get(0) != null) {
                crmAccountData.setBillAddressID(crmAccount.getBillingAddresses().get(0).getObjectID());
            }
        }
        return crmAccountData;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public ContactListItem getIncomingCallerID() {
//        if (true) return null;
        EdsUser user = crmContactManager.getUser();

        if (user == null || user.getCompany() == null || user.getCompany().getCompanySettings() == null) {
            return null;
        }
        SwitchvoxCredentials cred = new SwitchvoxCredentials();
        cred.setDomain(user.getCompany().getCompanySettings().getSwitchvoxServerId());
        cred.setUsername(user.getCompany().getCompanySettings().getSwitchvoxUserName());
        cred.setPassword(user.getCompany().getCompanySettings().getSwitchvoxPassword());

        SwitchvoxResult.CurrentCalls.CallItem callerPhone = Switchvox.getInstance().getCurrentCallerPhone(cred, getPhoneExtension(user));
        if (callerPhone == null) {
            return null;
        }
        System.out.println(">>> Caller Phone: " + callerPhone.getFrom_caller_id_number());
        //get details from crm, if no details found, just return phone number
        EdsCrmContact contact = crmContactManager.getByPhone(callerPhone.getFrom_caller_id_number());
        ContactListItem item = new ContactListItem();
        if (contact != null) {
            item = contact.getRPC(new ListingFilterParameter(false), item);
            if (item.getWorkPhone().size() > 0) {
                item.getWorkPhone().set(0, callerPhone.getFrom_caller_id_number());
            } else {
                item.getWorkPhone().add(callerPhone.getFrom_caller_id_number());
            }
        } else {
            item.setContactName(callerPhone.getFrom_caller_id_name());
            item.getWorkPhone().add(callerPhone.getFrom_caller_id_number());
        }
        return item;
    }

    public void switchvoxCall(String externalPhone) {
        if (externalPhone != null) {
            externalPhone = externalPhone.replaceAll("\\+", "");
        }
        System.out.println(">>>Switchvox call to " + externalPhone);
        EdsUser user = crmContactManager.getUser();

        SwitchvoxCredentials cred = new SwitchvoxCredentials();
        cred.setDomain(user.getCompany().getCompanySettings().getSwitchvoxServerId());
        cred.setUsername(user.getCompany().getCompanySettings().getSwitchvoxUserName());
        cred.setPassword(user.getCompany().getCompanySettings().getSwitchvoxPassword());

        //make a call
        Switchvox.getInstance().createCall(cred, getPhoneExtension(user), externalPhone);

    }

    private String getPhoneExtension(EdsUser user) {
        String ext = null;
        try {
            ext = user.getEmployee().getContact().getRPC(new ListingFilterParameter(false)).getExtension().get(0).replaceFirst("\\D*(\\d*).*", "$1");
        } catch (Exception ignored) {
        }
        return ext;
    }

    @Override
    public ListResult<OpportunityExpenseClaimListItem> getOpportunityExpenseClaimList(Integer opportunityId, ListingFilterParameter filterParameter) {
        ListingObjectItem expenses = opportunityManager.getOpportunityExpenseClaimList(opportunityId, filterParameter);
        ArrayList<OpportunityExpenseClaimListItem> result = new ArrayList<>();
        for (EdsExpenseReport expense : (List<EdsExpenseReport>) expenses.getItems()) {
            OpportunityExpenseClaimListItem items = new OpportunityExpenseClaimListItem();

            items.setId(expense.getObjectID());
            items.setTitle(expense.getTitle());
            items.setDescription(expense.getDescription());
            if (expense.getReporter() != null) {
                items.setReporterId(expense.getReporter().getObjectID());
                items.setReporterName(expense.getReporter().getName());
            }
            if (expense.getCurrentApprover() != null && expense.getCurrentApprover().getExactEmployee() != null) {
                items.setApproverSelectItem(new SelectItem(expense.getCurrentApprover().getExactEmployee().getObjectID(), expense.getCurrentApprover().getExactEmployee().getName()));
            }
            if (expense.getNumber() != null) {
                items.setNumber(expense.getNumber());
            }
            if (expense.getProject() != null) {
                items.setProjectId(expense.getProject().getObjectID());
                items.setProjectName(expense.getProject().getName());
            }
            if (expense.getStatus() != null) {
                items.setStatusId(expense.getStatus().getObjectID());
                items.setStatusCode(expense.getStatus().getCode());
                items.setStatusName(referenceWfmMessageSource.localize(expense.getStatus().getCode(), expense.getStatus().getName()));
            }
            if (expense.getBaseTotal() != null) {
                items.setTotal(expense.getBaseTotal().doubleValue());
            }
            items.setStartDate(expense.getStartDate());
            result.add(items);
        }
        return new ListResult<>(result, expenses.getTotalCount());

    }

    @Override
    public HashMap<Integer, SelectItem[]> getStatesByCountryName() {
        List<EdsRegion> regions = regionManager.list();
        HashMap<Integer, ArrayList<SelectItem>> result = new HashMap<>();
        HashMap<Integer, SelectItem[]> result2 = new HashMap<>();
        for (EdsRegion region : regions) {
            Integer countryID = region.getCountry().getObjectID();
            if (!result.containsKey(countryID)) {
                ArrayList<SelectItem> ar = new ArrayList<>();
                result.put(countryID, ar);
            }
            result.get(countryID).add(region.getAsSelectItem());
        }
        for (Integer i : result.keySet()) {
            result2.put(i, result.get(i).toArray(new SelectItem[]{}));
        }
        return result2;
    }

    @Override
    public HashMap<String, String[]> getCountriesKey() {
        HashMap<String, String[]> map = new HashMap<>();
        for (EdsCountry item : countryManager.list()) {
            String key = countryLocalizer.localize(item.getCode(), item.getName());
            map.put(key, new String[]{String.valueOf(item.getObjectID()), key /*+ countryManager.getCountryTimeZoneAndPhoneCode(item)*/});
        }
        return map;
    }

    @Override
    @Transactional
    public OpportunityListItem addAccountOrContactToOpportunity(Integer opportunityID, boolean customer) {
        OpportunityListItem item = new OpportunityListItem();
        if (opportunityID != null) {
            EdsOpportunity opportunity = opportunityManager.get(opportunityID);
            boolean accountAdded = false;
            boolean contactAdded = false;
            EdsUser user = userManager.getUser();
            EdsCrmAccount crmAccount = new EdsCrmAccount();
            EdsCrmContact crmContact = new EdsCrmContact();
            if (opportunity.getCrmAccount() == null) {
                if (opportunity.getCrmContact() != null && opportunity.getCrmContact().getCrmAccount() != null) {
                    crmAccount = opportunity.getCrmContact().getCrmAccount();
                } else {
                    crmAccount.setName(opportunity.getName());
                    crmAccount.addAccountType(customer ? referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER) : referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.SUPPLIER));
                    crmAccount.setOwners(Collections.singletonList(user));
                    crmAccount.setCurrency(opportunity.getCurrency() != null ? opportunity.getCurrency() : invoiceCircularResolver.returnBaseCurrency(user.getCompany()));
                    if (opportunity.getCrmContact() != null) {
                        crmAccount.getCrmContacts().add(opportunity.getCrmContact());
                    }
                    crmAccountManager.create(crmAccount);
                    createNewAddress(crmAccount);
                    accountAdded = true;
                }
                opportunity.setCrmAccount(crmAccount);
                if (crmAccount.getEntityID() != null) {
                    opportunity.setEntityID(crmAccount.getEntityID());
                }
                item.setCrmAccountItem(crmAccount.getRPCForContact(null));
            }
            if (opportunity.getCrmContact() == null) {
                crmContact.setFirstName(opportunity.getName());
                crmContact.setCrmAccount(opportunity.getCrmAccount());
                crmContact.setOwner(user);
                crmContactManager.create(crmContact);

                if (opportunity.getCrmAccount() != null) {
                    EdsCrmAccount account = opportunity.getCrmAccount();
                    account.getCrmContacts().add(crmContact);
                    crmAccountManager.update(account);
                }

                opportunity.setCrmContact(crmContact);
                if (crmContact.getEntityID() != null) {
                    opportunity.setEntityID(crmContact.getEntityID());
                }
                item.setContactId(crmContact.getObjectID());
                createNewAddress(crmContact);
                contactAdded = true;
            } else if (opportunity.getCrmContact() != null) {
                EdsCrmContact contact = opportunity.getCrmContact();
                if (contact.getCrmAccount() == null && crmAccount.getObjectID() != null) {
                    contact.setCrmAccount(crmAccount);
                    crmContactManager.createOrUpdate(contact);
                    crmContact = contact;
                    contactAdded = true;
                }
            }
            opportunityManager.update(opportunity);
            try {
                opportunitySolrComponent.index(opportunity);
                if (accountAdded && crmAccount.getObjectID() != null) {
                    crmAccountSolrComponent.index(crmAccount);
                }
                if (contactAdded && crmContact.getObjectID() != null) {
                    contactSolrComponent.index(crmContact);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return item;
    }

    @Override
    public ContactListItem addAccountToContact(ContactListItem item, boolean customer) {
        if (item != null) {
            EdsUser user = userManager.getUser();
            EdsCrmAccount account = new EdsCrmAccount();
            EdsCrmContact contact = crmContactManager.get(item.getObjectId());
            account.setName(contact.getName());
            //account.setOwner(user);
            account.setOwners(Collections.singletonList(user));
            account.addAccountType(customer ? referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER) : referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.SUPPLIER));
            account.setCurrency(invoiceCircularResolver.returnBaseCurrency(user.getCompany()));
            crmAccountManager.create(account);
            if (contact.getPrimaryAddressFromAll() != null) {
                EdsAddress billingAddress = new EdsAddress();
                EdsAddress mailingAddress = new EdsAddress();
                billingAddress.setAddressData(contact.getPrimaryAddressFromAll());
                mailingAddress.setAddressData(contact.getPrimaryAddressFromAll());
                if (contact.getPrimaryAddressFromAll().getCountry() != null) {
                    billingAddress.setCountry(countryManager.getCountryByName(contact.getPrimaryAddressFromAll().getCountry()));
                    mailingAddress.setCountry(countryManager.getCountryByName(contact.getPrimaryAddressFromAll().getCountry()));
                }
                billingAddress.setPrimary(true);
                mailingAddress.setPrimary(true);
                billingAddress.setLinkedAddress(false);
                mailingAddress.setLinkedAddress(false);
                billingAddress.setLinkedAddressID(null);
                mailingAddress.setLinkedAddressID(null);
                mailingAddress.setName("Mailing Address");
                billingAddress.setCrmAccount(account);
                mailingAddress.setCrmAccount(account);
                addressManager.create(billingAddress);
                addressManager.create(mailingAddress);
                account.setBillingAddress(billingAddress);
                account.setMailingAddress(mailingAddress);
                crmAccountManager.update(account);
            } else {
                createNewAddress(account);
            }
            contact.setCrmAccount(account);
            crmContactManager.update(contact);
            item.setCrmAccount(account.getRPCForContact(null));
            try {
                crmAccountSolrComponent.index(account);
                contactSolrComponent.index(contact);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return item;
    }

    @Override
    public String getCrmAccountNameByID(Integer crmAccountID) {
        EdsCrmAccount account = crmAccountManager.get(crmAccountID);
        return account.getName();
    }

    @Override
    public FileResource[] getTrackerAttachments(HashSet<Integer> trackerIDSet) {
        HashSet<EdsEmailAttachment> attachments = new HashSet<>();
        for (Integer trackerId : trackerIDSet) {
            if (trackerId != null) {
                attachments.addAll(emailAttachmentManager.getTrackerAttachments(trackerId));

            }
        }
        if (attachments.size() > 0) {
            return EdsEmailAttachment.asFileResourses(attachments).toArray(new FileResource[]{});
        } else {
            return new FileResource[0];
        }
    }

    private void createNewAddress(EdsObject accountOrContact) {
        EdsAddress billingAddress = new EdsAddress();
        EdsAddress mailingAddress = new EdsAddress();
        EdsCountry country = userManager.getUser().getCompany().getCountryZone().getCountry();
        billingAddress.setName(commonLocalizer.localize(PdfLocalizationName.billingAddress, "Billing Address"));
        billingAddress.setCountry(country);
        billingAddress.setPrimary(true);
        billingAddress.setLinkedAddress(false);
        mailingAddress.setName(commonLocalizer.localize(PdfLocalizationName.mailingAddress, "Mailing Address"));
        mailingAddress.setCountry(country);
        mailingAddress.setPrimary(true);
        mailingAddress.setLinkedAddress(false);
        if (accountOrContact instanceof EdsCrmAccount) {
            billingAddress.setCrmAccount((EdsCrmAccount) accountOrContact);
            mailingAddress.setCrmAccount((EdsCrmAccount) accountOrContact);
            addressManager.create(billingAddress);
            addressManager.create(mailingAddress);
        } else {
            billingAddress.setContact((EdsCrmContact) accountOrContact);
            addressManager.create(billingAddress);
        }
        if (accountOrContact instanceof EdsCrmAccount) {
            ((EdsCrmAccount) accountOrContact).setBillingAddress(billingAddress);
            ((EdsCrmAccount) accountOrContact).setMailingAddress(mailingAddress);
            crmAccountManager.update(((EdsCrmAccount) accountOrContact));
        } else {
            ArrayList<EdsAddress> addresses = new ArrayList<>();
            addresses.add(billingAddress);
            ((EdsCrmContact) accountOrContact).setAddresses(addresses);
            crmContactManager.update((EdsCrmContact) accountOrContact);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<ContactListItem> getContactsForMerge(Integer[] contactIds) {
        ArrayList<ContactListItem> result = new ArrayList<>();
        List<EdsCrmContact> contactList = crmContactManager.getContactListByIds(ServerUtils.getAsCommoDelimited(Arrays.asList(contactIds), "0", ","));
        if (contactList != null && contactList.size() > 0) {
            result.add(contactService.editContact(CrmConstants.TYPE_CRM_CONTACT, null, null, null, false));
            for (EdsCrmContact edsContact : contactList) {
                ContactListItem item = new ContactListItem();
                item.setSelectedMailingLists(massMailServiceLocal.getSubscribedListsByCrmEntityId(edsContact.getObjectID()));
                ArrayList<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(edsContact.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Contact));
                if (customFieldItems != null && customFieldItems.size() > 0) {
                    item.setCustomFields(customFieldItems);
                }
                result.add(edsContact.getRPC(new ListingFilterParameter(false), item));
            }
            return result;
        }
        return null;
    }

    @Override
    @Transactional
    public Boolean validateContactInvoices(ContactListItem mainItem, ArrayList<Integer> otherObjectIds) {
        if (mainItem != null && mainItem.getObjectId() != null && otherObjectIds != null && otherObjectIds.size() > 0) {
            EdsCrmContact edsNewContact = crmContactManager.get(mainItem.getObjectId());
            if (edsNewContact != null) {
                boolean isNewContCrmAccount = edsNewContact.getCrmAccount() != null;
                boolean isCrmAccount;

                if (isNewContCrmAccount) {
                    for (Integer othContId : otherObjectIds) {
                        EdsCrmContact edsOthCont = crmContactManager.get(othContId);
                        isCrmAccount = edsOthCont != null && edsOthCont.getCrmAccount() != null;
                        if (isCrmAccount) {
                            if (!edsNewContact.getCrmAccount().getObjectID().equals(edsOthCont.getCrmAccount().getObjectID())) {
                                boolean isNewContSaleInvoices = invoiceManager.findSaleInvoicesByCrmAccountID(edsNewContact.getCrmAccount().getObjectID());
                                boolean isNewContSaleQuote = quoteManager.findSaleQuotesByCrmAccountID(edsNewContact.getCrmAccount().getObjectID());
                                for (Integer otherContId : otherObjectIds) {
                                    EdsCrmContact edsOtherCont = crmContactManager.get(otherContId);
                                    boolean isOtherSalesInvoice = invoiceManager.findSaleInvoicesByCrmAccountID(edsOtherCont.getCrmAccount().getObjectID());
                                    boolean isOtherSalesQuote = quoteManager.findSaleQuotesByCrmAccountID(edsOtherCont.getCrmAccount().getObjectID());
                                    if (isNewContSaleInvoices || isNewContSaleQuote || isOtherSalesInvoice || isOtherSalesQuote) {
                                        return Boolean.FALSE;
                                    }
                                }
                            }
                        }
                    }
                    return Boolean.TRUE;
                } else if (mainItem.getCrmAccount() != null && mainItem.getCrmAccount().getObjectId() != null) {
                    boolean isNewContSaleInvoices = invoiceManager.findSaleInvoicesByCrmAccountID(mainItem.getCrmAccount().getObjectId());
                    boolean isNewContSalesQuote = quoteManager.findSaleQuotesByCrmAccountID(mainItem.getCrmAccount().getObjectId());
                    boolean isCrmAccoun;
                    if (isNewContSaleInvoices || isNewContSalesQuote) {
                        for (Integer othContId : otherObjectIds) {
                            EdsCrmContact edsOthCont = crmContactManager.get(othContId);
                            isCrmAccoun = edsOthCont != null && edsOthCont.getCrmAccount() != null;
                            if (isCrmAccoun) {
                                if (!mainItem.getCrmAccount().getObjectId().equals(edsOthCont.getCrmAccount().getObjectID())) {
                                    return Boolean.FALSE;
                                }
                            }
                        }
                    }
                    return Boolean.TRUE;
                } else {
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    @Transactional
    public Boolean mergeContacts(ContactListItem mainItem, boolean deleteOthers, ArrayList<Integer> otherObjectIds) {
        ArrayList<Integer> mailingListIds = new ArrayList<>();
        if (mainItem != null && mainItem.getSelectedMailingLists() != null) {
            mainItem.getSelectedMailingLists();
            for (SelectItem item : mainItem.getSelectedMailingLists()) {
                mailingListIds.add(item.getId());
            }
        }
        if (mainItem != null && mainItem.getCrmAccount() != null && mainItem.getCrmAccount().getObjectId() == null) {
            mainItem.setCrmAccount(null);
        }
        mainItem.getAddresses().remove(0);
        Integer objectId = contactService.saveContact(mainItem, mailingListIds, true);
        if (otherObjectIds != null) {
            otherObjectIds.remove(objectId);
        }
        if (otherObjectIds.size() > 0) {
            EdsCrmContact savedContact = crmContactManager.get(objectId);
            List<EdsCrmContact> otherContacts = crmContactManager.getContactsByIDs(otherObjectIds);
            copyDetailsOfContacts(savedContact, otherContacts);
            contactService.deleteContacts(otherObjectIds, null, false);
        }
        return Boolean.TRUE;
    }

    private void copyDetailsOfContacts(EdsCrmContact savedContact, List<EdsCrmContact> otherContacts) {
        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        List<Integer> otherContactIDs = EdsCrmContact.getObjectIDs(otherContacts);
        //move Tasks
        relationManager.mergeCrmContactRelations(savedContact.getObjectID(), savedContact.getName(), otherContactIDs);
        //move Events
        List<Integer> eventIds = relationManager.getRelationIDsByType(savedContact.getObjectID(), null, RelationItem.TYPE_CONTACT, RelationItem.TYPE_EVENT);
        if (eventIds != null && eventIds.size() > 0) {
            eventManager.addToSolr(eventIds.toArray(new Integer[]{}));
        }
        //move Invoices
        if (savedContact.getCrmAccount() != null) {
            for (EdsCrmContact otherContact : otherContacts) {
                if (otherContact.getCrmAccount() != null) {
                    List<EdsSaleInvoice> edsSalesInvoices = invoiceManager.getSaleInvoicesByCrmAccountID(otherContact.getCrmAccount().getObjectID());
                    for (EdsSaleInvoice edsSaleInvoice : edsSalesInvoices) {
                        if (edsSaleInvoice != null && edsSaleInvoice.isDeleted() != null && !edsSaleInvoice.isDeleted()) {
                            relationManager.mergeCrmContactInvoices(otherContact.getObjectID(), RelationItem.TYPE_SALEINVOICE, savedContact.getObjectID());
                        }
                    }

                    List<EdsSaleQuote> edsSaleQuotes = quoteManager.getSaleQuotesByCrmAccountID(otherContact.getCrmAccount().getObjectID());
                    for (EdsSaleQuote edsSaleQuote : edsSaleQuotes) {
                        if (edsSaleQuote != null && edsSaleQuote.isDeleted() != null && !edsSaleQuote.isDeleted()) {
                            relationManager.mergeCrmContactInvoices(otherContact.getObjectID(), RelationItem.TYPE_SALEQUOTE, savedContact.getObjectID());
                        }
                    }

                    List<EdsSaleQuote> salesQuotes = quoteManager.getSaleQuotesByCrmContactID(otherContact.getObjectID());
                    for (EdsSaleQuote sq : salesQuotes) {
                        sq.setClientContact(savedContact);
                        quoteManager.update(sq);
                        EdsPickList pickList = pickListManager.getPickListBySaleQuoteID(sq.getObjectID());
                        try {
                            saleQuoteSolrComponent.indexes(Collections.singletonList(sq), (pickList != null) ? Collections.singletonList(pickList) : null);
                        } catch (IOException | SolrServerException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        //move Notes
        noteHistoryManager.updateNotesWithContactID(savedContact.getObjectID(), otherContactIDs);
    }

    @Override
    public void saveEventEditCellValue(EventItem rowValue, String columnCodeName) {
        try {
            EdsEvent event = eventManager.get(rowValue.getObjectID());
            event.clear();
            if (EventItem.START_DATE.equals(columnCodeName)) {
                event.setStartDate(rowValue.getStartDate());
            } else if (EventItem.END_DATE.equals(columnCodeName)) {
                event.setEndDate(rowValue.getEndDate());
            } else if (EventItem.DESCRIPTION.equals(columnCodeName)) {
                event.setDescription(rowValue.getDescription());
            } else {
                EdsCrmCustomFields edsCrmCustomFields = event.getEventCustomFields();
                if (edsCrmCustomFields == null) {
                    edsCrmCustomFields = new EdsCrmCustomFields();
                    crmCustomFieldsManager.create(edsCrmCustomFields);
                    event.setEventCustomFields(edsCrmCustomFields);
                }
                CustomFieldsUtils.setDomenObjectFieldChange(edsCrmCustomFields, rowValue.getCustomFieldsMap(), columnCodeName);
            }
            eventManager.update(event);
            eventSolrComponent.index(event);
        } catch (Exception e) {
            System.out.println("Event Edit Cell Column Code :" + columnCodeName);
        }
    }

    @Override
    public void saveLeadMultiAssignee(boolean allTableItems, ArrayList<Integer> itemIDs, ListingFilterParameter filterParameter, ArrayList<SelectItem> selectItems) {
        if (allTableItems) {
            ListLoadConfig congig = new ListLoadConfig();
            congig.setStart(0);
            congig.setLimit(getLeadList(filterParameter, congig, false).getTotalCount());
            congig.setSortField(filterParameter.getSortField());
            congig.setSortDir(filterParameter.isAscending() ? 1 : 2);
            ContactListItem[] leadList = getLeadList(filterParameter, congig, false).getLeadListItems();
            ArrayList<Integer> allItemsIds = new ArrayList<>();
            for (ContactListItem contactListItem : leadList) {
                allItemsIds.add(contactListItem.getObjectId());
            }
            itemIDs = allItemsIds;
        }
        ArrayList<Integer> oldChangedIds = new ArrayList<>();
        for (SelectItem item : selectItems) {
            Integer assigneeId = item.getId();
            Integer assigneeCount = Integer.valueOf(item.getDescription());
            ArrayList<Integer> leadIDs = new ArrayList<>();
            int t = 1;
            for (Integer itemid : itemIDs) {
                if (t <= assigneeCount && !oldChangedIds.contains(itemid)) {
                    leadIDs.add(itemid);
                    oldChangedIds.add(itemid);
                    t++;
                } else if (t > assigneeCount) {
                    break;
                }
            }
            saveLeadAssignee(leadIDs, assigneeId);

            try {
                messageManager.sendMultiAssignLeadNotification(assigneeId, assigneeCount);
            } catch (EdsDbException e) {
                e.printStackTrace();
            }
        }

    }


    @Transactional
    @Override
    public ContactListItem makePrimaryContact(Integer accountID, Integer contactID) {

        ContactListItem item = new ContactListItem();
        List<EdsCrmContact> contactsToSolr = new ArrayList<>();

        if (accountID == null || contactID == null) {
            return item;
        }

        EdsCrmAccount edsCrmAccount = crmAccountManager.get(accountID);
        EdsCrmContact edsCrmContact = crmContactManager.get(contactID);

        if (edsCrmAccount == null || edsCrmContact == null) {
            return item;
        }

        edsCrmContact.setPrimaryContact(Boolean.TRUE);
        contactsToSolr.add(edsCrmContact);
        crmContactManager.update(edsCrmContact);

        for (EdsAddress address : edsCrmContact.getAddresses()) {
            item.getAddresses().add(address.getRPC());
        }

        for (EdsCrmContact contact : edsCrmAccount.getCrmContacts()) {
            if (!contact.getObjectID().equals(edsCrmContact.getObjectID())) {
                contact.setPrimaryContact(Boolean.FALSE);
                crmContactManager.update(contact);
                contactsToSolr.add(contact);
            }
        }


        try {
            contactSolrComponent.indexes(contactsToSolr);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("Error occurred while updating primary contact", e);
        }

        item.setPrimaryContact(Boolean.TRUE);
        return item;

    }

    @Override
    public ContactListItem getPrimaryContactAddresses(Integer contactID) {
        if (contactID != null) {
            ContactListItem item = new ContactListItem();
            EdsCrmContact edsCrmContact = crmContactManager.get(contactID);
            if (edsCrmContact != null && edsCrmContact.getAddresses() != null && !edsCrmContact.getAddresses().isEmpty()) {
                for (EdsAddress address : edsCrmContact.getAddresses()) {
                    if (address != null && !address.isLinkedAddress()) {
                        item.getAddresses().add(address.getRPC());
                    }
                }
                return item;
            }
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ArrayList<RejectedImportRecord[]> importAccounts(ImportFile importFile, List dataBank, String from) {
        return importingServiceLocal.importAccounts(importFile, dataBank, from);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void importVCardAccounts(ImportFile importFile, List<VCard> items, String type, String from) {
        importingServiceLocal.importVCardAccounts(importFile, items, type, from);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ArrayList<RejectedImportRecord[]> importContacts(ImportFile importFile, List listOfRows, int contactType, Integer mailListId) throws Exception {
        return importingServiceLocal.importContacts(importFile, listOfRows, contactType, mailListId);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Set<Integer> importVCardContacts(ImportFile importFile, List<VCard> listOfRows, Set<Integer> savedLeadsHashCodes, int contactType) throws Exception {
        return importingServiceLocal.importVCardContacts(importFile, listOfRows, savedLeadsHashCodes, contactType);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public int createEntityMailList(EdsMailList mailList, List<Integer> iDs) {
        return importingServiceLocal.createEntityMailList(mailList, iDs);
    }

    @Override
    public SelectItem[] getBankAccounts() {
        return accountingService.getBankAccountItems();
    }

    @Override
    public OpportunityListItem getOpportunityQuickData() {
        OpportunityListItem result = new OpportunityListItem();
        result.setNumberData(generateOpportunityNumber());
        SelectItem[] crmSelasManList = getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE);

        if (crmSelasManList == null || crmSelasManList.length == 0) {
            EdsUser user = userManager.getUser();
            if (user != null) {
                crmSelasManList = new SelectItem[]{user.getAsSelectItem()};
            }
        }
        result.setAssignees(crmSelasManList);
        result.setStages(getOpportunityStages(false));
        return result;
    }

    @Override
    public ContactListItem getContactQuickData(Integer crmAccountId, int contactType) {
        ContactListItem result = new ContactListItem();
        EdsUser edsUser = userManager.getUser();
        if (edsUser == null) {
            return result;
        }
        result.setLeadAssignees(getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE));
        result.setOwnerId(edsUser.getObjectID());
        EdsCrmAccount crmAccount = crmAccountManager.get(crmAccountId);
        if (crmAccount != null) {
            CrmAccountItem accountItem = crmAccount.getRPC(null, false);
            result.setCrmAccount(accountItem);
        }
        if (ContactListItem.LEAD_CONTACT == contactType) {

            List<EdsReference> statuses = referenceManager.listReferences(EdsCrmContact._LEAD_STATUS);

            List<SelectItem> statusItems = statuses.stream()
                    .map(status -> new SelectItem(status.getObjectID(), referenceWfmMessageSource.localize(status.getCode(), status.getName())))
                    .toList();

            EdsReference defaultStatus = statuses.stream()
                    .filter(status -> EdsCrmContact.ATTEMPTED_TO_CONTACT.equals(status.getCode()))
                    .findFirst()
                    .orElse(null);

            result.setLeadStatuses(statusItems.toArray(new SelectItem[]{}));

            if (defaultStatus != null) {
                result.setLeadStatus(defaultStatus.getRPC());
                result.getLeadStatus(true).setName(referenceWfmMessageSource.localizeRef(defaultStatus));
            }
        }
        return result;
    }

    @Override
    public ContactListItem getCandidateQuickData() {
        ContactListItem result = new ContactListItem();
        result.setNumberData(allInOneServiceLocal.generateCandidateNumber(null));
        result.setCandidateStatuses(ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmContact._CANDIDATE_STATUS), ServerUtils.REFERENCE));
        result.setLeadAssignees(getOwnersListByPermission(PermissionConstants.HRMS_SHOW_IN_CANDIDATE_OWNER));
        EdsUser edsUser = userManager.getUser();
        if (edsUser != null) {
            result.setOwnerId(edsUser.getObjectID());
        }
        return result;
    }

    @Override
    public CaseItem getCaseQuickData() {
        CaseItem item = new CaseItem();
        item.setTypes(getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_TYPE), ServerUtils.REFERENCE));
        item.setPriorities(getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_PRIORITY), ServerUtils.REFERENCE));
        item.setStatusItems(getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_STATUS), ServerUtils.REFERENCE));
        EdsUser user = userManager.getUser();
        if (!user.hasEitherRoles(EdsRole.ADMIN, EdsRole.DR, EdsRole.SALESMAN)) {
            item.setCaseAssigneeId(user.getObjectID());
            item.setCaseAssigneeName(user.getName());
        }
        return item;
    }

    @Override
    public CaseItem getCaseTypes() {
        CaseItem item = new CaseItem();
        item.setTypes(getAsSelectItem(referenceManager.listReferences(EdsCase._CASE_TYPE), ServerUtils.REFERENCE));
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ContactListItem> getNewKanbanLeads(ListingFilterParameter filterParameter, SelectItem columnMetadata) {
        filterParameter.setColumnMetadataId(columnMetadata.getId());
        filterParameter.setSortField(null);
        filterParameter.setSortDir(1);

        ListLoadConfig congig = new ListLoadConfig();
        congig.setStart(filterParameter.getStart());
        congig.setLimit(filterParameter.getLimit());
        congig.setSortField(filterParameter.getSortField());
        congig.setSortDir(filterParameter.getSortDir());
        ListResult<ContactListItem> result = getKanbanLeadList(filterParameter, congig);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmContact.class.getSimpleName());
        kpiLog.setEntityType("LEAD_KANBAN_LIST");
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get Lead Kanban list");
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OpportunitiesList<OpportunityListItem> getNewKanbanOpportunities(ListingFilterParameter filterParameter, SelectItem columnMetadata) {

        filterParameter.setColumnMetadataId(columnMetadata.getId());
        filterParameter.setSortField(null);
        filterParameter.setSortDir(1);


        FacetFilterRpc opportunityFacetFilter = filterParameter.getFacetFilter();
        if (opportunityFacetFilter != null && !opportunityFacetFilter.isFilterChanges()) {
            opportunityFacetFilter = commonServiceLocal.getUserFacetFilter(opportunityFacetFilter);
        }

        String mainSolrQuery = opportunitySolrComponent.getOpportunityFacetQuery(filterParameter, opportunityFacetFilter);

        OpportunitiesList<OpportunityListItem> result = getKanbanOpportunityList(filterParameter, mainSolrQuery);

        StringBuilder statusAmountSum = new StringBuilder(mainSolrQuery);
        if (columnMetadata.getId() == -1) {
            statusAmountSum.append(" AND (-").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID).append(":[* TO *] AND *:*)");
        } else {
            statusAmountSum.append(" AND ").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID).append(":").append(columnMetadata.getId());
        }
        Double statusAmountSumResp = getOpportunityStatusSum(statusAmountSum.toString());
        result.setTotalAmount(statusAmountSumResp);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmContact.class.getSimpleName());
        kpiLog.setEntityType("OPPORTUNITY_KANBAN_LIST");
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get Opportunity Kanban list");
        return result;
    }

    public Double getOpportunityStatusSum(String solrQuery) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_OPPORTUNITY_CORE);
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);

        query.setFacetMinCount(1);
        query.setFacet(true);

        query.addGetFieldStatistics(SolrOpportunityRepresenter.FIELD_AMOUNT_BASE_CURRENCY);

        QueryResponse statusAmountSumResp = null;
        try {
            statusAmountSumResp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        if (statusAmountSumResp != null && statusAmountSumResp.getFieldStatsInfo() != null && statusAmountSumResp.getFieldStatsInfo().containsKey(SolrOpportunityRepresenter.FIELD_AMOUNT_BASE_CURRENCY)) {
            return (Double) (statusAmountSumResp.getFieldStatsInfo().get(SolrOpportunityRepresenter.FIELD_AMOUNT_BASE_CURRENCY).getSum());
        } else {
            return null;
        }

    }

    private OpportunitiesList<OpportunityListItem> getKanbanOpportunityList(ListingFilterParameter filterParameter, String solrQuery) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_OPPORTUNITY_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(getOpportunitySolrQuery(filterParameter, solrQuery), SolrRequest.METHOD.POST);

        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return getKanbanOpportunityFromSolrResult(resp);
    }

    private OpportunitiesList getKanbanOpportunityFromSolrResult(QueryResponse resp) {

        int totalNumber = 0;
        ArrayList<OpportunityListItem> opportunityItems = new ArrayList<>();
        Map<Integer, String> imgs = new HashMap<>();

        if (resp != null && resp.getResults() != null) {

            totalNumber = (int) resp.getResults().getNumFound();

            if (resp.getResults() != null) {
                Map<Integer, String> leadNotes = noteHistoryManager.getLastNotesAsMap(EdsNoteHistory.CRM_OPPORTUNITY, SolrUtils.getIdsFromSolrDocument(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID, resp.getResults().toArray(new SolrDocument[]{})));
                List<String> columnCodes = commonServiceLocal.getCFsColumnCodeByUiTypes(ViewName.Opportunity, ListUtils.getCFUITypesForKanbanItem());

                for (SolrDocument doc : resp.getResults()) {
                    if (doc != null) {
                        OpportunityListItem opportunityItem = getKanbanOpportunityFromSolr(doc);
                        EdsCrmAccount crmAccount = crmAccountManager.get(opportunityItem.getAccountId());
                        if (crmAccount != null) {
                            opportunityItem.setCrmAccountItem(crmAccount.getRPC(null, true));
                        }
                        Integer assigneeId = opportunityItem.getAssigneeId();
                        if (imgs.get(assigneeId) != null) {
                            opportunityItem.setOpportunityImageUrl(imgs.get(assigneeId));
                        } else {
                            EdsUser edsUser = userManager.get(assigneeId);
                            if (assigneeId != null && edsUser.getPhoto() != null) {
                                String imgUrl = getImageUrl(edsUser.getPhoto().getObjectID());
                                imgs.put(assigneeId, imgUrl);
                                opportunityItem.setOpportunityImageUrl(imgUrl);
                            }
                        }
                        opportunityItem.setCustomFieldsMap(CustomFieldsUtils.getSolrDocDynamicFields(doc, columnCodes));

                        if (leadNotes.containsKey(opportunityItem.getObjectId())) {
                            opportunityItem.setNote(leadNotes.get(opportunityItem.getObjectId()));
                        }
                        opportunityItems.add(opportunityItem);
                    }
                }

            }
        }
        return new OpportunitiesList<>(opportunityItems, totalNumber);
    }

    private OpportunityListItem getKanbanOpportunityFromSolr(SolrDocument doc) {
        OpportunityListItem item = new OpportunityListItem();
        item.setConvertedLead(SolrUtils.asBoolean(doc, SolrOpportunityRepresenter.FIELD_CONVERTED_FROM_LEAD));
        item.setObjectId(SolrUtils.asInteger(doc, SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID));
        item.setOpportunityName(SolrUtils.asString(doc, SolrOpportunityRepresenter.FIELD_OPPORTUNITY_NAME));
        item.setAccountId(SolrUtils.asInteger(doc, SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_ID));
        item.setAccount(SolrUtils.asString(doc, SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_NAME));
        item.setAccountNumber(SolrUtils.asString(doc, SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_NUMBER));
        item.setContactId(SolrUtils.asInteger(doc, SolrOpportunityRepresenter.FIELD_CRM_CONTACT_ID));
        item.setContact(SolrUtils.asString(doc, SolrOpportunityRepresenter.FIELD_CRM_CONTACT_NAME));
        item.setContactPrimaryEmail(SolrUtils.asString(doc, SolrOpportunityRepresenter.FIELD_CRM_CONTACT_PRIMARY_EMAIL));
        item.setContactEmailOptOut(SolrUtils.asBoolean(doc, SolrOpportunityRepresenter.FIELD_CRM_CONTACT_EMAIL_ALLOWED));
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(SolrUtils.asInteger(doc, SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_ID));
        item.setContactPrimaryPhone(SolrUtils.asString(doc, SolrOpportunityRepresenter.FIELD_CRM_CONTACT_PRIMARY_PHONE) != null ? SolrUtils.asString(doc, SolrOpportunityRepresenter.FIELD_CRM_CONTACT_PRIMARY_PHONE) : edsCrmAccount != null ? edsCrmAccount.getPhone() : null);
        item.setAssignee(SolrUtils.asString(doc, SolrOpportunityRepresenter.FIELD_ASSIGNEE_NAME));
        item.setAssigneeId(SolrUtils.asInteger(doc, SolrOpportunityRepresenter.FIELD_ASSIGNEE_ID));
        item.setBackupAssignee(SolrUtils.asString(doc, SolrOpportunityRepresenter.FIELD_BACKUP_ASSIGNEE_NAME));

        item.setClosingDate(SolrUtils.asDate(doc, SolrOpportunityRepresenter.FIELD_CLOSING_DATE));
        item.setAmount(SolrUtils.asDouble(doc, SolrOpportunityRepresenter.FIELD_AMOUNT));
        item.setAmountInBaseCurrency(SolrUtils.asDouble(doc, SolrOpportunityRepresenter.FIELD_AMOUNT_BASE_CURRENCY, 0d));
        item.setCurrency(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_CURRENCY_NAME));

        if (SolrUtils.asString(doc, SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STRING_NUMBER) != null && SolrUtils.asInteger(doc, SolrOpportunityRepresenter.FIELD_OPPORTUNITY_INT_NUMBER) != null) {
            item.setNumberData(new NumberData(SolrUtils.asString(doc, SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STRING_NUMBER), SolrUtils.asInteger(doc, SolrOpportunityRepresenter.FIELD_OPPORTUNITY_INT_NUMBER)));
        }
        item.setStageId(SolrUtils.asInteger(doc, SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID));
        if (item.getStageId() != null) {
            EdsReference reference = referenceManager.get(item.getStageId());
            EdsUser user = userManager.getUser();

            boolean draggable = false;
            if (reference.getAllowedRoles().isEmpty() || !reference.getAllowedRoles().isEmpty() && user.hasEitherRoles(reference.getAllowedRoles().toArray(new EdsRole[]{}))) {
                item.setDraggable(true);
                draggable = true;
            }

            if (reference.getViewOnlyRoles() != null && !reference.getViewOnlyRoles().isEmpty()) {
                if (!draggable && user.hasEitherRoles(reference.getViewOnlyRoles().toArray(new EdsRole[]{}))) {
                    item.setDraggable(false);
                }
            }

            if (reference.getOppEditBtnRole() == null || (reference.getOppEditBtnRole() != null && reference.getOppEditBtnRole().isEmpty()) || user == null || (user != null && user.hasEitherRoles(reference.getOppEditBtnRole().toArray(new EdsRole[]{})))) {
                item.setAllowEdit(true);
            }
        }

        return item;
    }

    public SelectItem takeReference(String statusCode) {
        EdsReference reference = referenceManager.getByCode(statusCode);
        SelectItem selectItem = new SelectItem();
        if (reference != null) {
            selectItem = new SelectItem(reference.getObjectID(), reference.getName());
        }
        return selectItem;
    }

    @Override
    public void updateOpportunityNoteAndRejectReason(Integer opportunityId, String note, Integer rejectReasonId) {
        if (opportunityId == null) return;

        EdsOpportunity opportunity = opportunityManager.get(opportunityId);
        if (opportunity == null) return;

        if (note != null && !note.isEmpty()) {
            EdsNoteHistory edsNote = new EdsNoteHistory();
            edsNote.setEmployee(userManager.getUser());
            edsNote.setComment(note);
            edsNote.setEventDate(new Date());
            edsNote.setRelatedId(opportunity.getObjectID());
            edsNote.setRelatedTo(EdsNoteHistory.getRelatedToByEntityType(CrmConstants.CRM_OPPORTUNITY));
            edsNote.setSuperUser(ServerUtils.isSuperUser());
            noteHistoryManager.createOrUpdate(edsNote);

            opportunity.setNote(note);
        } else {
            opportunity.setNote("");
        }
        if (rejectReasonId != null) {
            opportunity.setRejectReason(referenceManager.get(rejectReasonId));
        }

        updateOpportunity(opportunity);
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(
                WorkflowActionDetectedEventListenerImpl.TYPE,
                BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT,
                opportunity,
                userManager.getUser()
        );
        workflowEvent.setEntityType(RelationItem.TYPE_OPPORTUNITY);
    }


    public Integer changeOpportunityKanbanOrder(String statusCode, Integer itemId, Integer widgetIndex) {
        EdsReference reference = referenceManager.getByCode(statusCode);
        SelectItem selectItem = new SelectItem();
        if (reference != null) {
            selectItem = new SelectItem(reference.getObjectID(), reference.getName());
        }
        return changeOpportunityKanbanOrder(selectItem, itemId, widgetIndex, null, null);
    }


    @Override
    public Integer changeOpportunityKanbanOrder(SelectItem columnLayoutData, Integer itemId, Integer widgetIndex,
                                                Integer prevItemId, Integer afterItemId) {
        if (itemId != null) {
            EdsOpportunity opportunity = opportunityManager.get(itemId);
            opportunity.setLastChanges("");
            EdsReference newStage = referenceManager.get(columnLayoutData.getId());
            boolean stageChanged = isValueChanged(opportunity.getStage(), newStage);
            opportunity.setStage(newStage);
            if ("0".equals(columnLayoutData.getDescription())) {
                opportunity.setRejectReason(referenceManager.get(columnLayoutData.getEntityId()));
            }
            if (columnLayoutData.getCategory() != null) {
                EdsNoteHistory edsNote = new EdsNoteHistory();
                edsNote.setEmployee(userManager.getUser());
                edsNote.setComment(columnLayoutData.getCategory());
                edsNote.setEventDate(new Date());
                edsNote.setRelatedId(opportunity.getObjectID());
                edsNote.setRelatedTo(EdsNoteHistory.getRelatedToByEntityType(CrmConstants.CRM_OPPORTUNITY));
                edsNote.setSuperUser(ServerUtils.isSuperUser());
                noteHistoryManager.createOrUpdate(edsNote);

                opportunity.setNote(columnLayoutData.getCategory());
            } else {
                opportunity.setNote("");
            }
            if (prevItemId != null && afterItemId == null) {
                EdsOpportunity potentialContact = opportunityManager.getSiblingOpportunityByPrevItem(prevItemId, opportunity.getStage().getObjectID());
                afterItemId = potentialContact != null ? potentialContact.getObjectID() : null;
            }
            if (prevItemId == null && afterItemId != null) {
                Long minKanbanOrderInStatus = opportunityManager.getMinKanbanOrder(opportunity.getStage() != null ? opportunity.getStage().getObjectID() : null);
                if (minKanbanOrderInStatus == null) {
                    opportunity.setKanbanorder(KANBAN_ORDER_GAP);
                } else if (opportunity.getKanbanorder() == null || (minKanbanOrderInStatus <= opportunity.getKanbanorder())) {
                    opportunity.setKanbanorder(minKanbanOrderInStatus - 1);
                }
            }
            updateOpportunity(opportunity);
            baseEventPostProcessor.registerCustomEvent(KanbanCalculationEventListenerImpl.TYPE_OPPORTUNITY, EdsMyUpdate.ADD,
                    opportunity, prevItemId, afterItemId);

            if (stageChanged) {
                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, opportunity, userManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_OPPORTUNITY);
            }
        }
        return 0;
    }

    @Override
    public ListResult<CaseItem> getNewKanbanCases(ListingFilterParameter filterParameter, SelectItem columnMetadata) {
        filterParameter.setColumnMetadataId(columnMetadata.getId());
        filterParameter.setSortField(null);
        filterParameter.setSortDir(1);

        StringBuilder mainSolrQuery = getCasesFilter(filterParameter);
        ListResult<CaseItem> result = getKanbanCaseList(filterParameter, mainSolrQuery.toString());

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCase.class.getSimpleName());
        kpiLog.setEntityType("CASE_KANBAN_LIST");
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get Case Kanban list");
        return result;
    }

    private ListResult<CaseItem> getKanbanCaseList(ListingFilterParameter filterParameter, String solrQuery) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CASE_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(getCrmCaseSolrQuery(filterParameter, solrQuery, false), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return getKanbanCaseFromSolrResult(resp);
    }

    private ListResult<CaseItem> getKanbanCaseFromSolrResult(QueryResponse resp) {
        int totalCount = 0;
        ArrayList<CaseItem> list = new ArrayList<>();

        Map<Integer, String> imgs = new HashMap<>();
        final List<String> columnCodes = commonServiceLocal.getCFsColumnCodeByUiTypes(ViewName.CrmCase, ListUtils.getCFUITypesForKanbanItem());

        if (resp != null && resp.getResults() != null && !resp.getResults().isEmpty()) {
            totalCount = (int) resp.getResults().getNumFound();
            Map<Integer, String> caseNotes = noteHistoryManager.getLastNotesAsMap(EdsNoteHistory.CRM_CASE, SolrUtils.getIdsFromSolrDocument(SolrCaseRepresenter.CASE_ID, resp.getResults().toArray(new SolrDocument[]{})));
            list.addAll(resp.getResults().stream()
                    .map(doc -> {
                        CaseItem item = new CaseItem();
                        item.setObjectId(SolrUtils.asInteger(doc, SolrCaseRepresenter.CASE_ID));
                        item.setTrackerID(SolrUtils.asInteger(doc, SolrCaseRepresenter.CASE_TRACKER_ID));
                        item.setEmailID(SolrUtils.asString(doc, SolrCaseRepresenter.CASE_EMAIL_ID));
                        item.setKanbanOrder(SolrUtils.asLong(doc, SolrCaseRepresenter.KANBAN_ORDER));
                        item.setCaseNumber(SolrUtils.asString(doc, SolrCaseRepresenter.CASE_NUMBER));
                        item.setSubject(SolrUtils.asString(doc, SolrCaseRepresenter.CASE_SUBJECT));
                        item.setPhone(SolrUtils.asString(doc, SolrCaseRepresenter.CASE_PHONE));
                        item.setPriority(referenceWfmMessageSource.localize(SolrUtils.asString(doc, SolrCaseRepresenter.PRIORITY_CODE), SolrUtils.asString(doc, SolrCaseRepresenter.PRIORITY_NAME)));
                        item.setPriorityColor(SolrUtils.asString(doc, SolrCaseRepresenter.PRIORITY_COLOR));
                        item.setReportedBy(SolrUtils.asString(doc, SolrCaseRepresenter.REPORTED_BY));
                        item.setCaseAssigneeName(SolrUtils.asString(doc, SolrCaseRepresenter.CASE_ASSIGNEE));
                        Integer assigneeID = SolrUtils.asInteger(doc, SolrCaseRepresenter.CASE_ASSIGNEE_ID);
                        item.setCustomFieldsMap(CustomFieldsUtils.getInSolrCustomFields(doc, columnCodes));

                        if (imgs.get(assigneeID) != null) {
                            item.setCaseAssigneeImageUrl(imgs.get(assigneeID));
                        } else {
                            EdsUser assignee = userManager.get(assigneeID);
                            if (assigneeID != null && assignee.getPhoto() != null) {
                                String imgUrl = getImageUrl(assignee.getPhoto().getObjectID());
                                imgs.put(assigneeID, imgUrl);
                                item.setCaseAssigneeImageUrl(getImageUrl(assignee.getPhoto().getObjectID()));
                            }
                        }

                        if (caseNotes.containsKey(item.getObjectId())) {
                            item.setLastNote(caseNotes.get(item.getObjectId()));
                        }
                        return item;
                    })
                    .toList());
        }
        return new CaseList(list, totalCount);
    }

    @Override
    public Integer changeCaseKanbanOrder(SelectItem columnLayoutData, Integer itemId, Integer widgetIndex, Integer prevItemId, Integer afterItemId) {
        if (itemId != null) {
            EdsCase crmCase = caseManager.get(itemId);
            EdsReference newStatus = referenceManager.get(columnLayoutData.getId());
            boolean isCaseStatusChanged = isValueChanged(crmCase.getStatus(), newStatus);
            if (columnLayoutData.getCategory() != null) {
                EdsNoteHistory edsNote = new EdsNoteHistory();
                edsNote.setEmployee(userManager.getUser());
                edsNote.setComment(columnLayoutData.getCategory());
                edsNote.setEventDate(new Date());
                edsNote.setRelatedId(crmCase.getObjectID());
                edsNote.setRelatedTo(EdsNoteHistory.getRelatedToByEntityType(CrmConstants.CRM_CASE));
                edsNote.setSuperUser(ServerUtils.isSuperUser());
                noteHistoryManager.createOrUpdate(edsNote);

                crmCase.setNote(columnLayoutData.getCategory());
            } else {
                crmCase.setNote("");
            }
            updateCaseHistory("Changed the case status to " + referenceWfmMessageSource.localizeRef(newStatus), crmCase);
            crmCase.setStatus(newStatus);

            if (prevItemId != null && afterItemId == null) {
                EdsCase potentialCase = caseManager.getSiblingCaseByPrevItem(prevItemId, columnLayoutData.getId());
                afterItemId = potentialCase != null ? potentialCase.getObjectID() : null;
            }

            try {
                caseManager.update(crmCase);
                caseSolrComponent.index(crmCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
            baseEventPostProcessor.registerCustomEvent(KanbanCalculationEventListenerImpl.TYPE_CASE, EdsMyUpdate.ADD, crmCase, prevItemId, afterItemId);

            if (isCaseStatusChanged) {
                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, crmCase, userManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_CASE);
            }
        }
        return 0;
    }

    @Override
    public ArrayList<Integer> convertLead(HashMap<Integer, OpportunityListItem> items, boolean withOpportunity) {
        ArrayList<Integer> leadIDs = new ArrayList<>(items.keySet());
        if (items.size() > 0) {
            Integer opportunityID = null;
            List<EdsCrmAccount> crmAccounts = new ArrayList<>();
            List<EdsOpportunity> opportunities = new ArrayList<>();
            List<EdsCrmContact> leads = new ArrayList<>();
            List<EdsCompanyCustomFieldsSettings> companyCustomFields = companyCFSettingsManager.getCompanyLeadContactAndOpportunityCustomFields(CFLEAD, CFCONTACT, CFOPPORTUNITY, CFCRMACCOUNT);
            List<EdsCompanyCustomFieldsSettings> companyContactCustomFields = new ArrayList<>();
            List<EdsCompanyCustomFieldsSettings> companyLeadCustomFields = new ArrayList<>();
            List<EdsCompanyCustomFieldsSettings> companyAccountCustomFields = new ArrayList<>();
            List<EdsCompanyCustomFieldsSettings> companyOpportunityCustomFields = new ArrayList<>();
            for (EdsCompanyCustomFieldsSettings companyCustomField : companyCustomFields) {
                switch (companyCustomField.getEntityName()) {
                    case CFCONTACT -> companyContactCustomFields.add(companyCustomField);
                    case CFLEAD -> companyLeadCustomFields.add(companyCustomField);
                    case CFOPPORTUNITY -> companyOpportunityCustomFields.add(companyCustomField);
                    case CFCRMACCOUNT -> companyAccountCustomFields.add(companyCustomField);
                }
            }
            ArrayList<Integer> leadIDsForRelations = new ArrayList<>();
            boolean copyDetails = false;
            for (Integer leadID : leadIDs) {
                EdsCrmContact lead = crmContactManager.get(leadID);
                EdsCrmAccount account = null;
                FileResource[] fileResources = getCrmAttachments(leadID, "lead");
                if (lead != null) {
                    account = lead.getCrmAccount();
                }
                leadIDsForRelations.add(leadID);
                copyDetails = items.get(leadID) != null && items.get(leadID).isCopyLeadDetails();
                if (withOpportunity) {
                    OpportunityListItem opportunityItem = items.get(leadID);
                    opportunityItem.setClosingDate(new Date());
                    opportunityItem.setAccountId(account != null ? account.getObjectID() : null);
                    opportunityItem.setContactId(lead.getObjectID());
                    opportunityItem.setNumberData(generateOpportunityNumber());
                    if (lead.getLeadAssignee() != null) {
                        opportunityItem.setBackupAssigneeID(lead.getLeadAssignee().getObjectID());
                    }
                    if (lead.getOwner() != null) {
                        opportunityItem.setOwnerID(lead.getOwner().getObjectID());
                    }
                    if (lead.getCampaign() != null) {
                        opportunityItem.setCampaignId(lead.getCampaign().getObjectID());
                        opportunityItem.setCampaign(lead.getCampaign().getName());
                    }
                    if (lead.getLeadSource() != null) {
                        opportunityItem.setLeadSource(lead.getLeadSource().getName());
                        opportunityItem.setLeadSourceId(lead.getLeadSource().getObjectID());
                    }
                    CurrencyItem baseCurrency = invoiceServiceLocal.getBaseCurrency();
                    if (baseCurrency != null) {
                        opportunityItem.setCurrencyId(baseCurrency.getId());
                    }
                    opportunityID = saveOpportunity(opportunityItem);
                    if (fileResources != null) {
                        for (FileResource fileResource : fileResources) {
                            if (fileResource != null) {
                                attachmentUtilsManager.copyFileWhenConvert(F_OPPORTUNITY, fileResource.getFolderId(), fileResource.getObjectId(), opportunityID, fileResource);
                            }
                        }
                    }
                }
                EdsOpportunity opportunity = opportunityID != null ? opportunityManager.get(opportunityID) : null;
                if (opportunity != null) {
                    opportunities.add(opportunity);
                    opportunity.setConvertedFromLead(Boolean.TRUE);
                }
                Integer contactType = EdsCrmContact.CRM_CONTACT;
                if (withOpportunity && items.get(leadID).getStageId() != null) {
                    EdsReference stage = referenceManager.findReferenceByDescription(EdsOpportunity._OPPORTUNITY_STAGE, "100");
                    stage = stage == null ? referenceManager.findReference(EdsOpportunity._OPPORTUNITY_STAGE, EdsOpportunity.CLOSED_WON) : stage;
                    if (stage != null && stage.getObjectID().equals(items.get(leadID).getStageId())) {
                        contactType = EdsCrmContact.CLIENT_CONTACT;
                    }
                }
                lead.setContactType(contactType);
                lead.getCategories().clear();
                lead.addCategories(contactCategoryManager.getDefaultCategoryByContactType(lead.getContactType()));
                leads.add(lead);
                if (lead.getCustomFields() != null) {
                    //convert custom fields start
                    EdsCrmCustomFields crmCustomFieldsForContact = new EdsCrmCustomFields();
                    EdsCrmCustomFields crmCustomFieldsForOpportunity = new EdsCrmCustomFields();
                    EdsCrmCustomFields crmCustomFieldsForAccount = new EdsCrmCustomFields();
                    boolean added = false;
                    for (EdsCompanyCustomFieldsSettings leadCustomFieldSetting : companyLeadCustomFields) {
                        //populate custom fields for contact
                        added = populateCustomFieldDetails(lead, leadCustomFieldSetting, companyContactCustomFields, crmCustomFieldsForContact) || added;
                        if (withOpportunity && opportunityID != null) {
                            //populate custom fields for opportunity
                            added = populateCustomFieldDetails(lead, leadCustomFieldSetting, companyOpportunityCustomFields, crmCustomFieldsForOpportunity) || added;
                        }
                        if (account != null) {
                            crmCustomFieldsForAccount = account.getCustomFields() != null ? account.getCustomFields() : crmCustomFieldsForAccount;
                            added = populateCustomFieldDetails(lead, leadCustomFieldSetting, companyAccountCustomFields, crmCustomFieldsForAccount) || added;
                        }
                    }
                    if (added) {
                        crmCustomFieldsManager.create(crmCustomFieldsForContact);
                        lead.setOldCustomFields(lead.getCustomFields());
                        lead.setCustomFields(crmCustomFieldsForContact);
                        //set custom field values to opportunity
                        if (opportunity != null) {
                            crmCustomFieldsManager.create(crmCustomFieldsForOpportunity);
                            opportunity.setCustomFields(crmCustomFieldsForOpportunity);
                        }
                        if (account != null) {
                            crmCustomFieldsManager.createOrUpdate(crmCustomFieldsForAccount);
                            account.setCustomFields(crmCustomFieldsForAccount);
                        }
                    }
                    //convert custom fields end
                }
                if (account != null) {
                    List<EdsAddress> addressList = addressManager.getContactAddresses(lead.getObjectID());
                    if (addressList != null && addressList.size() > 0) {
                        for (EdsAddress a : addressList) {
                            a.setPrimary(false);
                            a.setCrmAccount(account);
                            a.setRelationType(EdsAddress.BILLING_ADDRESS);
                            addressManager.update(a);
                        }
                    }
                    account.setLastUpdateTime(new Date());
                    crmAccounts.add(account);
                }
            }
            if (copyDetails && !leadIDsForRelations.isEmpty() && !opportunities.isEmpty()) {
                copyDetails(RelationItem.TYPE_LEAD, leadIDsForRelations.get(0), RelationItem.TYPE_OPPORTUNITY, opportunityID, opportunities.get(0).getName());

                EdsCrmContact edsCrmContact = crmContactManager.get(leadIDsForRelations.get(0));
                if (edsCrmContact != null) {
                    baseEventPostProcessor.registerEvent(CrmContactCustomEventListenerImpl.TYPE, CrmContactCustomEventListenerImpl.EVENT_REINDEX_CONTACT_RELATIONS, edsCrmContact, userManager.getUser());
                }
            }
            List<Integer> caseIDs = caseManager.getCaseIDsByLeadIDs(EdsCase.getObjectIDs(leads));
            if (caseIDs != null && !caseIDs.isEmpty()) {
            }
            try {
                contactSolrComponent.indexes(leads);
                opportunitySolrComponent.indexes(opportunities);
                crmAccountSolrComponent.indexes(crmAccounts);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (leadIDs.size() > 0) {
                leadIDs.removeAll(EdsObject.getObjectIDs(leads));
            }
        }
        return leadIDs;
    }

    @Override
    public Integer getOrCreateCrmAccount(String name) {
        EdsCrmAccount account = crmAccountManager.getCrmAccountByName(name, null);
        if (account != null) {
            return account.getObjectID();
        }
        CrmAccountItem item = editAccount(null, CrmAccountItem.CRM_ACCOUNT);
        item.setName(name);
        return saveAccount(item, CrmAccountItem.CRM_ACCOUNT, crmAccountManager.getUser().getObjectID(), false, false, false, false);
    }

    public ListResult<Appointment> getLastActivities(Integer objectId, String type) {
        ArrayList<Appointment> activities = new ArrayList<>();
        List<Object[]> tasks = relationManager.getRelationsByIdAndType(objectId, type, RelationItem.TYPE_TASK);

        if (tasks != null) {
            tasks.forEach(task -> {
                Appointment t = new Appointment();
                t.setAction(RelationItem.TYPE_TASK);
                t.setObjectID(Integer.valueOf(task[0].toString()));
                t.setSubject(task[1] != null ? task[1].toString() : "");
                t.setStartDate(task[2] != null ? (Date) task[2] : null);
                activities.add(t);
            });
        }

        //Events
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setRelationID(objectId);
        filterParametrs.setRelationType(type);
        filterParametrs.setCreatedFrom(Appointment.FROM_CRM);
        ListResult<EventItem> events = getEventList(filterParametrs);
        if (events != null && events.getList() != null) {
            events.getList().forEach(event -> {
                if ((event.getStartDate() == null || event.getStartDate().after(new Date())) || (event.getEndDate() == null || event.getEndDate().after(new Date()))) {
                    Appointment e = new Appointment();
                    e.setAction(RelationItem.TYPE_EVENT);
                    e.setObjectID(event.getObjectID());
                    e.setSubject(event.getSubject());
                    e.setStartDate(event.getStartDate());
                    activities.add(e);
                }
            });
        }
        return new ListResult<>(activities, activities.size());
    }

    public void updateLeadData(CompanyData companyData) {
        EdsCrmAccount acc = crmAccountManager.getSignupLeadByCompanyId(companyData.getCompanyId());
        if (acc != null) {
            if (StringUtils.isNotBlank(companyData.getName())) {
                acc.setName(companyData.getName());
            }

            System.out.println("updateLead : " + companyData.getSelectedApps());

            if (companyData.getIndustryId() != null && companyData.getIndustryId() > 0) {
                try {
                    EdsReference industry = referenceManager.get(companyData.getIndustryId());
                    if (industry != null) {
                        acc.setIndustry(industry);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }


            //Set Billing && Mailing Addresses
            if (companyData.getAddress() != null) {
                EdsAddress billingAddress = acc.getBillingAddresses() == null || acc.getBillingAddresses().isEmpty() ? new EdsAddress() : acc.getBillingAddresses().get(0);
                billingAddress.setEntity(acc);
                billingAddress.setRelationType(EdsAddress.BILLING_ADDRESS);
                billingAddress.setAddress(companyData.getAddress().getAddress());
                billingAddress.setAddressb(companyData.getAddress().getAddressb());
                billingAddress.setZipCode(companyData.getAddress().getZipCode());
                billingAddress.setCity(companyData.getAddress().getCity());
                billingAddress.setPrimary(true);

                if (companyData.getAddress().getCountryId() != null) {
                    EdsCountry country = countryManager.get(companyData.getAddress().getCountryId());
                    billingAddress.setCountry(country);
                } else {
                    billingAddress.setCountry(null);
                }

                if (companyData.getAddress().getStateId() != null) {
                    EdsRegion state = regionManager.get(companyData.getAddress().getStateId());
                    if (state != null && state.getCountry().equals(billingAddress.getCountry())) {
                        billingAddress.setState(state);
                    } else {
                        billingAddress.setState(null);
                    }
                } else {
                    billingAddress.setState(null);
                }


                if (acc.getBillingAddresses() == null) {
                    addressManager.create(billingAddress);
                    List<EdsAddress> billingAddresses = new ArrayList<>();
                    billingAddresses.add(billingAddress);
                    acc.setBillingAddresses(billingAddresses);
                } else {
                    addressManager.update(billingAddress);
                }
            }
            try {
                crmAccountSolrComponent.index(acc);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("CrmAccount: " + (acc.getCrmContacts() != null && !acc.getCrmContacts().isEmpty()));
            log.info("companyData.getAccountingTool(): \"" + companyData.getAccountingTool() + "\"");
            log.info("companyData.getSelectedApps(): \"" + companyData.getSelectedApps() + "\"");
            if (acc.getCrmContacts() != null && !acc.getCrmContacts().isEmpty()) {
                try {

                    EdsCrmContact contact = acc.getCrmContacts().toArray(new EdsCrmContact[]{})[0];
                    //SAVE CUSTOM FIELDS
                    if (contact != null) {

                        if (StringUtils.isNotBlank(companyData.getRating())) {
                            EdsReference rating = referenceManager.findReference(EdsCrmContact._LEAD_RATING, companyData.getRating());
                            if (rating != null) {
                                contact.setLeadRating(rating);
                            }
                        }

                        ArrayList<CompanyCustomFieldItem> customFields = new ArrayList<>();

                        ArrayList<CompanyCustomFieldItem> leadCustomFields = CustomFieldsUtils.setRPCCustomFieldItems(contact.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Lead));

                        if (leadCustomFields != null && leadCustomFields.size() > 0) {
                            for (CompanyCustomFieldItem customFieldItem : leadCustomFields) {
                                System.out.println("CustomFieldName: \"" + customFieldItem.getFieldName() + "\"");
                                if ("Selected Apps".equalsIgnoreCase(customFieldItem.getFieldName()) && StringUtils.isNotBlank(companyData.getSelectedApps())) {
                                    //Selected Apps
                                    System.out.println("Selected Apps found: " + companyData.getSelectedApps());
                                    customFieldItem.setFieldStringValue(companyData.getSelectedApps());
                                    //Set updated date as per Munir's request
                                    if (contact.getAuditInfo() != null) {
                                        contact.getAuditInfo().setModificationDate(new Date());
                                    }
                                }
                                if ("What does your organisation do?".equalsIgnoreCase(customFieldItem.getFieldName()) && StringUtils.isNotBlank(companyData.getWhatDoesYourOrgDo())) {
                                    System.out.println("WhatDoesYourOrgDo found: " + companyData.getWhatDoesYourOrgDo());
                                    customFieldItem.setFieldStringValue(companyData.getWhatDoesYourOrgDo());
                                }
                                if ("I DO MY ACCOUNTING USING".equalsIgnoreCase(customFieldItem.getFieldName().trim()) && StringUtils.isNotBlank(companyData.getAccountingTool())) {
                                    customFieldItem.setFieldStringValue(companyData.getAccountingTool());                                  //Prev accounting tool they used
                                    System.out.println("I DO MY ACCOUNTING USING : " + companyData.getAccountingTool());
                                }
                                customFields.add(customFieldItem);
                            }
                        }
                        contact.setCustomFields(saveCustomFields(contact.getCustomFields(), customFields));
                    }
                    //Run Workflow
                    if (companyData.isPreventWorkflow()) {
                        try {
                            log.info("----------------------- CREATED WORKFLOW POST EVENT FOR LEAD---------------------------------------------");
                            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, contact, userManager.getUser());
                            workflowEvent.setEntityType(RelationItem.TYPE_LEAD);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    contactSolrComponent.indexes((List<EdsCrmContact>) acc.getCrmContacts());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public List<EdsReference> getTaxTreatments() {
        List<EdsReference> filteredItems = new ArrayList<>();
        boolean isReverseCharge = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ACCOUNTING_IS_REVERSE_CHARGE);
        String countryCode = userManager.getUser().getCompany().getCountry().getCode();
        if (Constants.UK.equals(countryCode)) {
            return getUKTaxTreatments();
        }
        List<EdsReference> treatments = referenceManager.listReferences(Constants._TAX_TREATMENT);
        for (EdsReference t : treatments) {

            if (Constants.GCC_VAT_REGISTERED.equals(t.getCode()) || Constants.GCC_NON_VAT_REGISTERED.equals(t.getCode()) || Constants.NON_GCC.equals(t.getCode())) {

                if (isReverseCharge) {
                    filteredItems.add(t);
                }
            } else if (Constants.VAT_REGISTERED_DESIGNATED_ZONE.equals(t.getCode()) || Constants.NON_VAT_REGISTERED_DESIGNATED_ZONE.equals(t.getCode())) {

                if (Constants.AE.equals(countryCode)) {
                    filteredItems.add(t);
                }
            } else if (!(Constants.NON_VAT.equalsIgnoreCase(t.getCode()) || Constants.OUT_OF_SCOPE.equalsIgnoreCase(t.getCode()))) {
                filteredItems.add(t);
            }
        }

        return filteredItems;
    }

    @Override
    public List<EdsReference> getUKTaxTreatments() {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        List<EdsReference> taxTreatments = referenceManager.listReferences(Constants._UK_TAX_TREATMENTS);

        return taxTreatments.stream().filter(treatment -> !OVERSEAS.equals(treatment.getCode()) || financialSettings.isEnableContractOutsite()).collect(Collectors.toList());
    }

    @Override
    public SelectItem[] getCrmSubItemsLookUpItems(ListingFilterParameter filterParameter, CustomFieldLookUpTypeEnum typeEnum) {

        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_OPPORTUNITY_CORE);
        SolrQuery query = new SolrQuery();
        query.setQuery(getOpportunityFacetQuery(filterParameter, filterParameter.getFacetFilter()));
        String fieldId = SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID;
        String fieldNumber = SolrOpportunityRepresenter.FIELD_OPPORTUNITY_NUMBER;

        if (CustomFieldLookUpTypeEnum.OPPORTUNITY_NAME.equals(typeEnum)) {
            fieldNumber = SolrOpportunityRepresenter.FIELD_OPPORTUNITY_NAME;
        } else if (CustomFieldLookUpTypeEnum.CASE.equals(typeEnum)) {
            server = WfmJpaTemplate.getSolrServerForCore(SOLR_CASE_CORE);
            query.setQuery(getCasesFilter(filterParameter).toString());
            fieldId = SolrCaseRepresenter.CASE_ID;
            fieldNumber = SolrCaseRepresenter.CASE_SUBJECT;
        } else if (CustomFieldLookUpTypeEnum.CONTACT.equals(typeEnum)) {
            server = WfmJpaTemplate.getSolrServerForCore(SOLR_CONTACT_CORE);
            query.setQuery(getContactListSolrQuery(filterParameter, filterParameter.getFacetFilter(), employeeManager.getUser(), null));
            fieldId = SolrContactRepresenter.FIELD_CONTACT_ID;
            fieldNumber = SolrContactRepresenter.FIELD_CONTACT_NAME;
        } else if (CustomFieldLookUpTypeEnum.CANDIDATE.equals(typeEnum)) {
            EdsUser edsUser;
            if (filterParameter.getUserID() != null) {
                edsUser = userManager.get(filterParameter.getUserID());
            } else {
                edsUser = crmContactManager.getUser();
            }
            EdsCompany edsCompany = edsUser.getCompany();
            server = WfmJpaTemplate.getSolrServerForCore(SOLR_CONTACT_CORE);
            query.setQuery(QueryBuilderForSolr.getCandidateListSolrQuery(filterParameter, filterParameter.getFacetFilter(), edsCompany, edsUser));
            fieldId = SolrContactRepresenter.FIELD_CONTACT_ID;
            fieldNumber = SolrContactRepresenter.FIELD_CONTACT_NAME;
        }
        query.setStart(0);
        query.setParam(CommonParams.ROWS, "10");

        QueryResponse resp = null;
        try {
            resp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        SolrDocumentList solrDocumentList = resp.getResults();
        List<SelectItem> items = com.google.common.collect.Lists.newArrayList();
        for (SolrDocument relevantDoc : solrDocumentList) {
            SelectItem item = new SelectItem();
            item.setId(SolrUtils.asInteger(relevantDoc, fieldId));
            item.setName(SolrUtils.asString(relevantDoc, fieldNumber));
            items.add(item);
        }

        return items.toArray(new SelectItem[]{});
    }

    @Override
    public ResponseResultListData<CrmActivityDTO> getNextEventList() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        String solrQuery = SolrEventRepresenter.FIELD_COMPANY_ID +
                ":" + SecurityContext.getCompanyID() +
                " AND (" + SolrEventRepresenter.FIELD_ACTIVITY_TYPE_ID +
                ":" + Appointment.EVENT + ")" +
                " AND (" + SolrEventRepresenter.FIELD_START_DATE +
                ":[" + format.format(new Date()) + " TO * ]) ";

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setParam(CommonParams.ROWS, "4");
        query.setSort(SolrEventRepresenter.FIELD_LAST_UPDATE_DATE, SolrQuery.ORDER.desc);

        QueryResponse resp = null;
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_EVENT_CORE);
        try {
            resp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        int count = 0;
        ArrayList<CrmActivityDTO> list = Lists.newArrayList();
        if (resp.getResults() != null && !resp.getResults().isEmpty()) {
            count = (int) resp.getResults().getNumFound();
            list.addAll(resp.getResults().stream().map(item -> new CrmActivityDTO(
                    Integer.parseInt(SolrUtils.asString(item, SolrEventRepresenter.FIELD_EVENT_ID)),
                    SolrUtils.asString(item, SolrEventRepresenter.FIELD_SUBJECT),
                    SolrUtils.asDate(item, SolrEventRepresenter.FIELD_START_DATE)
            )).toList());
        }
        return new ResponseResultListData<>(list, count);
    }

    @Override
    public void addEmployeeToEvent(Integer eventId, Integer employeeId) {
        this.googleCalendarServiceLocal.addEmployeeToEvent(eventId, employeeId);
    }

    @Override
    public void updateOpportunityStatus(OpportunityListItem data) {
        EdsOpportunity opportunity = opportunityManager.get(data.getObjectId());
        EdsUser user = userManager.getUser();

        //updating status
        EdsReference referenceStatus = referenceManager.findReference(Constants.OPPORTUNITY_STATUS, data.getStatusCode());
        if (!OPPORTUNITY_APPROVED.equals(data.getStatusCode())) {
            opportunity.setOverallStatus(referenceStatus);
        } else if (OPPORTUNITY_APPROVED.equals(data.getStatusCode()) && opportunity.getOverallStatus() != null && OPPORTUNITY_DRAFT.equals(opportunity.getOverallStatus().getCode())) {
            opportunity.setOverallStatus(referenceManager.findReference(Constants.OPPORTUNITY_STATUS, Constants.OPPORTUNITY_SUBMITTED));
        }
        opportunity.updateStatus(referenceStatus);
        opportunityManager.update(opportunity);

        if (!OPPORTUNITY_APPROVED.equals(data.getStatusCode()) && !OPPORTUNITY_DRAFT.equals(data.getStatusCode())) {
            baseEventPostProcessor.registerEvent(CrmOpportunityEventListenerImpl.TYPE, MyUpdateItem.EDIT, opportunity, userManager.getUser());
        }

        if (data.getStatusCode().equals(OPPORTUNITY_SUBMITTED)) {
            baseEventPostProcessor.registerEvent(CrmOpportunityEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, opportunity, userManager.getUser());
        }
    }

    @Override
    public SelectItem getEmployeeByCode(String employeeCode) {
        EdsEmployee employeeByCode = employeeManager.getEmployeeByCode(employeeCode);
        return employeeByCode != null ? employeeByCode.getAsSelectItem() : null;
    }

    @Override
    public SelectItem getEmployeeByPassportNumber(String passportNumber) {
        EdsEmployee employeeByCode = employeeManager.getEmployeeByPassportNumber(passportNumber);
        return employeeByCode != null ? employeeByCode.getAsSelectItem() : null;
    }

    @Override
    public SelectItem getEmployeeByFirstAndLastName(String firstName, String lastName) {
        EdsEmployee employeeByCode = employeeManager.getEmployeeByFirstAndLastName(firstName,lastName);
        return employeeByCode != null ? employeeByCode.getAsSelectItem() : null;
    }
}
