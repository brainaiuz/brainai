package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsAdjustmentItem;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsExpense;
import com.edatasite.workforce.core.domain.EdsExpensePayment;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsFormProperty;
import com.edatasite.workforce.core.domain.EdsImportFile;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsItemMultiPrice;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsRentalOrder;
import com.edatasite.workforce.core.domain.EdsRentalOrderItem;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsAccountType;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankCheck;
import com.edatasite.workforce.core.domain.accounting.EdsBankCheckItem;
import com.edatasite.workforce.core.domain.accounting.EdsBankCheckPaymentHistory;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransfer;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransferItem;
import com.edatasite.workforce.core.domain.accounting.EdsBaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsBasePurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsBaseSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsBatchPayment;
import com.edatasite.workforce.core.domain.accounting.EdsBillOfEntry;
import com.edatasite.workforce.core.domain.accounting.EdsBillOfEntryItem;
import com.edatasite.workforce.core.domain.accounting.EdsBillOfEntryTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsCustomerPrepaymentNote;
import com.edatasite.workforce.core.domain.accounting.EdsCustomerSupplierPayment;
import com.edatasite.workforce.core.domain.accounting.EdsDeferredTransactionItem;
import com.edatasite.workforce.core.domain.accounting.EdsDiscount;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsFixedAsset;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePaymentTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceQuoteNote;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceTaxTotal;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceTerms;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsItemTableSettings;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournal;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournalItem;
import com.edatasite.workforce.core.domain.accounting.EdsOverPayment;
import com.edatasite.workforce.core.domain.accounting.EdsPaymentInstruction;
import com.edatasite.workforce.core.domain.accounting.EdsPaymentRefund;
import com.edatasite.workforce.core.domain.accounting.EdsPaymentRefundTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsPickList;
import com.edatasite.workforce.core.domain.accounting.EdsPriceLevel;
import com.edatasite.workforce.core.domain.accounting.EdsProductSerial;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteHistory;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteItem;
import com.edatasite.workforce.core.domain.accounting.EdsRecurringBill;
import com.edatasite.workforce.core.domain.accounting.EdsRecurringInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsSharedNumber;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.core.domain.accounting.EdsShippingDataItem;
import com.edatasite.workforce.core.domain.accounting.EdsShippingMethod;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsTransactionItem;
import com.edatasite.workforce.core.domain.accounting.EdsVatEFiling;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.core.domain.analyzer.EdsSolrDbConsistency;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.approving.EdsApproverRoles;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCustomCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.EdsOpportunityItem;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceItemCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.core.domain.customform.EdsCustomItemTable;
import com.edatasite.workforce.core.domain.documents.EdsFileBody;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.enums.DeferredTransactionType;
import com.edatasite.workforce.core.domain.enums.EntityTypeEnum;
import com.edatasite.workforce.core.domain.fifo.EdsFifoFailure;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.core.domain.settings.EdsOverdueInvoiceReminderSettings;
import com.edatasite.workforce.core.domain.settings.EdsRestHook;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseBooking;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseScheduleStudent;
import com.edatasite.workforce.core.kafka.producer.KafkaEventProducer;
import com.edatasite.workforce.core.solr.component.ExpenseReportClaimsSolrComponent;
import com.edatasite.workforce.core.solr.component.FolderSolrComponent;
import com.edatasite.workforce.core.solr.component.ProductsServicesSolrComponent;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.core.solr.component.PurchaseInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.PurchaseOrderSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.ShippingDataSolrComponent;
import com.edatasite.workforce.core.solr.document.PurchaseInvoiceSolrDoc;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.FindMatchFilterData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxList;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxListData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionPDFObject;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountAppliesItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountService;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.ReceiveTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.ProductServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.costofgoods.COGSService;
import com.edatasite.workforce.gwt.accounting.server.app.itemBatches.ItemBatchServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.itemserials.ItemSerialServiceLocal;
import com.edatasite.workforce.gwt.client.client.rpc.ContactItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportStatusEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataItemStatus;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataStatus;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataType;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateService;
import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProjectListRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPurchaseInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.PdfTemplateItemList;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.EmailTemplateServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.PathFinder;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaOperations;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.commons.MastercardPaymentHandler;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CustomCrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.EmailTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ExpensePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ImportFileManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceItemManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceTermsManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.OverPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.PaymentMethodManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.core.server.db.RentalOrderManager;
import com.edatasite.workforce.gwt.core.server.db.ShippingDataManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BankCheckItemManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BankCheckManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BankCheckPaymentHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BatchPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ConsignmentManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.CustomerSupplierPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.DeferredTransactionManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.DiscountManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.InvoiceItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemTableSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalItemManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PaymentRefundManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PriceLevelManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQItemManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SharedNumberManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SpendReceiveMoneyItemManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SpendReceiveMoneyManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.VatEFilingManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.db.analyzer.SolrDbConsistencyManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.InvoiceCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.fifo.FifoFailureManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager;
import com.edatasite.workforce.gwt.core.server.db.settings.OverdueInvoiceReminderSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseBookingManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseScheduleStudentManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.BankTransferAppliedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.BatchPaymentEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.InvoicePaymentEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.ManualEntryAppliedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.PurchaseInvoiceEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.RecurringInvoiceEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.SaleInvoiceEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.DeferredTransactionCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.ImportCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.ProjectBudgetCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.SaleInvoiceCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.TransactionCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFODataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.InterCompanyDataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EntityType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.FailTarget;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.FifoFailureService;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.usps.USPSDeliveryConfirmation;
import com.edatasite.workforce.gwt.core.server.usps.USPSExpressMailLabel;
import com.edatasite.workforce.gwt.core.server.usps.USPSWebService;
import com.edatasite.workforce.gwt.core.server.utils.CacheConstants;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.EventHandler;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.core.server.zatca.service.errors.ZatcaException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpensePaymentData;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.invoice.client.rpc.AgingSummaryInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.AgingSummaryItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.AllocateCreditData;
import com.edatasite.workforce.gwt.invoice.client.rpc.AllocateCreditItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentAddEditData;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.BillOfEntry;
import com.edatasite.workforce.gwt.invoice.client.rpc.BillOfEntryItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.ListHeap;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.MultiQuoteConvertItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentAndPrePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectAllocateData;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RecurringInvoiceListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.SendToFormFillingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethodsList;
import com.edatasite.workforce.gwt.invoice.client.rpc.TotalTaxItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TransactionAllocateItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.ItemSerialEntityType;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.ProcessType;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.QIGroupingField;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockTransactionType;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.usps.ShippingLabelData;
import com.edatasite.workforce.gwt.invoice.client.rpc.usps.USPSPackage;
import com.edatasite.workforce.gwt.invoice.client.rpc.usps.USPSRates;
import com.edatasite.workforce.gwt.invoice.server.app.multiquoteconverter.ConvertedQuotesDto;
import com.edatasite.workforce.gwt.invoice.server.app.multiquoteconverter.MultiQuoteConverterUtils;
import com.edatasite.workforce.gwt.payroll.client.ui.PayrollContants;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentAsInvoiceItem;
import com.edatasite.workforce.gwt.trainingcenter.server.TCServiceLocal;
import com.edatasite.workforce.mail.EdsTemplate;
import com.edatasite.workforce.rest.base.to.ShippingMethodTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.CurrencyTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.ItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.SupplierTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.ZapierInvoiceItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.CustomerTO;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.InvoiceFieldsUpdaterDto;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.common.base.Stopwatch;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.CommonParams;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Transactional
@Service("invoiceService")
public class InvoiceServiceImpl extends BaseInvoiceService implements InvoiceService, InvoiceServiceLocal, Constants {

    private static final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    @Autowired
    protected PaymentMethodManager paymentMethodManager;
    @Autowired
    protected BatchPaymentManager batchPaymentManager;
    @Autowired
    protected NumberingSettingsManager numberingSettingsManager;
    @Autowired
    protected SharedNumberManager sharedNumberManager;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private InvoicePaymentManager invoicePaymentManager;
    @Autowired
    private PaymentRefundManager paymentRefundManager;
    @Autowired
    private OverPaymentManager overPaymentManager;
    @Autowired
    @Qualifier("savedReceivableCreditNoteViewPDFHandler")
    private IPostPDFHandler savedReceivableCreditNoteViewPDFHandler;
    @Autowired
    @Qualifier("savedPayableCreditNoteViewPDFHandler")
    private IPostPDFHandler savedPayableCreditNoteViewPDFHandler;
    @Autowired
    @Qualifier("savedSaleInvoceViewPDFHandler")
    private IPostPDFHandler savedSaleInvoiceViewPDFHandler;
    @Autowired
    @Qualifier("savedProjectBaseInvoiceViewPDFHandler")
    private IPostPDFHandler savedProjectBaseInvoiceViewPDFHandler;
    @Autowired
    @Qualifier("salesReceiptViewPDFHandler")
    private IPostPDFHandler salesReceiptViewPDFHandler;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    @Qualifier("allInOneService")
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private MyUpdateTypeManager myUpdateTypeManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    @Qualifier("emailTemplateService")
    private EmailTemplateServiceLocal emailTemplateServiceLocal;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private InvoiceCFManager invoiceCFManager;
    @Autowired
    private InvoiceItemCFManager invoiceItemCFManager;
    @Autowired
    private VatEFilingManager vatEFilingManager;
    @Autowired
    private PriceLevelManager priceLevelManager;
    @Autowired
    private ItemStockManager itemStockManager;
    @Autowired
    private SolrDbConsistencyManager solrDbConsistencyManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private InvoiceTermsManager invoiceTermsManager;
    @Autowired
    private EmailTemplateService emailTemplateService;
    @Autowired
    private CourseBookingManager courseBookingManager;
    @Autowired
    private CourseScheduleStudentManager courseScheduleStudentManager;
    @Autowired
    @Qualifier("tcService")
    private TCServiceLocal tcServiceLocal;
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private BankCheckManager bankCheckManager;
    @Autowired
    private BankCheckItemManager bankCheckItemManager;
    @Autowired
    private BankCheckPaymentHistoryManager bankCheckPaymentHistoryManager;
    @Autowired
    @Qualifier("productService")
    private ProductServiceLocal productServiceLocal;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private DiscountManager discountManager;
    @Autowired
    private InvoiceItemManager invoiceItemManager;
    @Autowired
    private ManualJournalManager manualJournalManager;
    @Autowired
    private ManualJournalItemManager manualJournalItemManager;
    @Autowired
    private QuoteHistoryManager quoteHistoryManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private WfmJpaOperations jpaTemplate;
    @Autowired
    private CustomerSupplierPaymentManager customerSupplierPaymentManager;
    @Autowired
    private CompanyCustomFieldsManager companyCFSettingsManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private SpendReceiveMoneyManager spendReceiveMoneyManager;
    @Autowired
    private SpendReceiveMoneyItemManager spendReceiveMoneyItemManager;
    @Autowired
    private ExpensePaymentManager expensePaymentManager;
    @Autowired
    private RFQItemManager rfqItemManager;
    @Autowired
    private RFQManager rfqManager;
    @Autowired
    private ConsignmentManager consignmentManager;
    @Autowired
    private OverdueInvoiceReminderSettingsManager overdueInvoiceReminderSettingsManager;
    @Autowired
    private ItemTableSettingService itemTableSettingService;
    @Autowired
    private ItemTableSettingsServiceLocal itemTableSettingsServiceLocal;
    @Autowired
    private ImportFileManager importFileManager;
    @Autowired
    private ShippingDataManager shippingDataManager;
    @Autowired
    private StockValidationService stockValidationService;
    @Autowired
    private PrepaymentServiceLocal prepaymentServiceLocal;
    @Autowired
    private ItemSerialServiceLocal itemSerialService;
    @Autowired
    private ItemBatchServiceLocal itemBatchService;
    @Autowired
    private ItemTableSettingsManager itemTableSettingsManager;
    @Autowired
    @Qualifier("batchReceivePaymentViewPDFHandler")
    private IPostPDFHandler batchReceivePaymentViewPDFHandler;
    @Autowired
    private DeferredTransactionManager deferredTransactionManager;
    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private ProfileServiceLocal profileService;
    @Autowired
    private CustomCrmAccountManager customCrmAccountManager;
    @Autowired
    private ShippingDataSolrComponent shippingDataSolrComponent;
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
    private RentalOrderManager rentalOrderManager;
    @Autowired
    private FifoFailureManager fifoFailureManager;
    @Autowired
    private KafkaEventProducer kafkaEventProducer;
    @Autowired
    private FifoFailureService fifoFailureService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashMap<String, Integer> findIDsBy(ListingFilterParameter fp) {
        return allInOneServiceLocal.findIDsBy(fp);
    }

    public SaveResult saveSaleInvoice(NewInvoice data, String quoteNumber) {
        return saveSaleInvoiceData(data, quoteNumber, true);
    }

    @Override
    public SaveResult saveSaleInvoice(NewInvoice data) {
        return saveSaleInvoiceData(data, null, true);
    }

    @Override
    public SaveResult saveSaleInvoice(NewInvoice data, boolean runWebhook) {
        return saveSaleInvoiceData(data, null, runWebhook);
    }

    @Override
    public synchronized Integer createInvoiceFromRecurringInvoice(Integer recurringInvoiceID) {
        EdsRecurringInvoice rInvoice = jpaTemplate.find(EdsRecurringInvoice.class, recurringInvoiceID);
        if (rInvoice == null || rInvoice.isDeleted()) return -1;
        EdsSaleInvoice saleInvoice = new EdsSaleInvoice();
        saleInvoice.setData(rInvoice);
        saleInvoice.setRecurringInvoiceID(recurringInvoiceID);

        Date userCurrentDate = rInvoice.getSender().getUserDate();

        Calendar rInvoiceDateCal = new GregorianCalendar();
        rInvoiceDateCal.setTime(rInvoice.getInvoiceDate());
        ServerUtils.setBeginningOfTheDay(rInvoiceDateCal);

        Calendar rInvoiceDueDateCal = new GregorianCalendar();
        rInvoiceDueDateCal.setTime(rInvoice.getDueDate());
        ServerUtils.setEndOfTheDay(rInvoiceDueDateCal);

        int dayDifference = DateUtil.countDays(rInvoiceDateCal.getTime(), userCurrentDate) - 1;
        if (dayDifference < 0) {
            dayDifference = 0;
        }

        Calendar newInvoiceDateCal = new GregorianCalendar();
        Calendar newInvoiceDueDateCal = new GregorianCalendar();
        newInvoiceDateCal.setTime(DateUtil.addDays(rInvoiceDateCal.getTime(), dayDifference));
        newInvoiceDueDateCal.setTime(DateUtil.addDays(rInvoiceDueDateCal.getTime(), dayDifference));
        ServerUtils.setBeginningOfTheDay(newInvoiceDateCal);
        ServerUtils.setEndOfTheDay(newInvoiceDueDateCal);
        saleInvoice.setInvoiceDate(newInvoiceDateCal.getTime());
        saleInvoice.setDueDate(newInvoiceDueDateCal.getTime());
        saleInvoice.setUpdatedDate(new Date());
        saleInvoice.setUpdater(rInvoice.getSender());

        invoiceManager.create(saleInvoice);
        InvoiceNumberData numberData = generateAndGetSaleInvoiceNumber(rInvoice.getSender().getCompany());//use generateAndGetSaleInvoiceNumber(company) instead
        if (numberData.isWithClient()) {
            numberData.setClientCode(saleInvoice.getClient().getNumber());
        }
        if (numberData.isWithProject() && saleInvoice.getRelatedProject() != null) {
            numberData.setProjectCode(saleInvoice.getRelatedProject().getNumber());
        }
        if (numberData.isWithDate()) {
            numberData.setDate(new SimpleDateFormat("yyyyMMdd").format(saleInvoice.getInvoiceDate()));
        }
        saleInvoice.setNumber(numberData.getInvoiceNumber());
        saleInvoice.setFourDigitNumber(Integer.parseInt(numberData.getFourDigitNumber()));

        //set payment instruction to the new creation invoice from recurring invoice generation
        if (saleInvoice.getPaymentInstructionID() != null) {
            EdsPaymentInstruction paymentInstruction = paymentInstructionManager.get(saleInvoice.getPaymentInstructionID());

            if (paymentInstruction.getText() != null && !paymentInstruction.getText().isEmpty()) {
                try {
                    EdsTemplate template = new EdsTemplate(paymentInstruction.getText());

                    Map<String, Object> map = new HashMap<>();
                    map.put("dueday", DateUtil.countDays(newInvoiceDateCal.getTime(), newInvoiceDueDateCal.getTime()));
                    map.put("duedate", Utils.formatDate(saleInvoice.getDueDate(), rInvoice.getCompany()));
                    if (rInvoice.getClient().getPaymentMethod() != null) {
                        map.put("paymentmethod", commonLocalizer.localize(rInvoice.getClient().getPaymentMethod().getCode(), rInvoice.getClient().getPaymentMethod().getName()));
                    }
                    map.put("startdate", Utils.formatDate(saleInvoice.getInvoiceDate(), rInvoice.getCompany()));
                    map.put("number", saleInvoice.getNumber());
                    if (saleInvoice.getFromDate() != null) {
                        map.put("fromdate", Utils.formatDate(saleInvoice.getFromDate(), rInvoice.getCompany()));
                    }
                    if (saleInvoice.getToDate() != null) {
                        map.put("todate", Utils.formatDate(saleInvoice.getToDate(), rInvoice.getCompany()));
                    }
                    saleInvoice.setPaymentInstruction(template.processContent(map));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            saleInvoiceSolrComponent.index(saleInvoice);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }

        baseEventPostProcessor.registerEvent(SaleInvoiceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, saleInvoice, saleInvoice.getCreator());

        return saleInvoice.getObjectID();
    }

    @Override
    public synchronized Integer createInvoiceFromRecurringBill(Integer recurringBillID) {

        EdsRecurringBill edsRecurringBill = jpaTemplate.find(EdsRecurringBill.class, recurringBillID);
        Date userCurrentDate = edsRecurringBill.getSender().getUserDate();
        EdsCompany edsCompany = edsRecurringBill.getSender().getCompany();

        ServerSecurityContext.getInstance().setStaticUserID(edsRecurringBill.getSender().getObjectID());

        EdsPurchaseInvoice edsPurchaseInvoice = new EdsPurchaseInvoice();
        edsPurchaseInvoice.setData(edsRecurringBill);
        edsPurchaseInvoice.setRecurringBillID(recurringBillID);

        EdsInvoicingSettings edsInvoicingSettings = invoicingSettingsManager.getInvoiceSettings(edsCompany);
        if (edsInvoicingSettings != null && edsInvoicingSettings.getIsPurchaseInvoiceNumberingShow()) {
            InvoiceNumberData invoiceNumberData = invoiceCircularResolver.generatePurchaseInvoiceNumber(false);
            edsPurchaseInvoice.setNumber(invoiceNumberData.getInvoiceNumber());
            edsPurchaseInvoice.setFourDigitNumber(Integer.parseInt(invoiceNumberData.getFourDigitNumber()));
        }

        Calendar rInvoiceDateCal = new GregorianCalendar();
        rInvoiceDateCal.setTime(edsRecurringBill.getInvoiceDate());
        ServerUtils.setBeginningOfTheDay(rInvoiceDateCal);

        Calendar rInvoiceDueDateCal = new GregorianCalendar();
        rInvoiceDueDateCal.setTime(edsRecurringBill.getDueDate());
        ServerUtils.setEndOfTheDay(rInvoiceDueDateCal);

        int dayDifference = DateUtil.countDays(rInvoiceDateCal.getTime(), userCurrentDate) - 1;
        if (dayDifference < 0) {
            dayDifference = 0;
        }

        Calendar newInvoiceDateCal = new GregorianCalendar();
        Calendar newInvoiceDueDateCal = new GregorianCalendar();
        newInvoiceDateCal.setTime(DateUtil.addDays(rInvoiceDateCal.getTime(), dayDifference));
        newInvoiceDueDateCal.setTime(DateUtil.addDays(rInvoiceDueDateCal.getTime(), dayDifference));
        ServerUtils.setBeginningOfTheDay(newInvoiceDateCal);
        ServerUtils.setEndOfTheDay(newInvoiceDueDateCal);
        edsPurchaseInvoice.setInvoiceDate(newInvoiceDateCal.getTime());
        edsPurchaseInvoice.setDueDate(newInvoiceDueDateCal.getTime());
        edsPurchaseInvoice.setUpdater(edsRecurringBill.getSender());
        edsPurchaseInvoice.setUpdatedDate(new Date());

        invoiceManager.create(edsPurchaseInvoice);

        try {
            purchaseInvoiceSolrComponent.index(edsPurchaseInvoice);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }

        baseEventPostProcessor.registerEvent(PurchaseInvoiceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsPurchaseInvoice, edsPurchaseInvoice.getCreator());

        return edsPurchaseInvoice.getObjectID();
    }

    public synchronized void createSalesInvoiceTransactionAndSendMessage(Integer newInvoiceID, Integer recurringInvoiceID) {
        EdsInvoice invoice = invoiceManager.get(newInvoiceID);
        EdsRecurringInvoice recurringInvoice = (EdsRecurringInvoice) invoiceManager.get(recurringInvoiceID);
        String statusCode = invoice.getStatus().getCode();

        ServerSecurityContext.getInstance().setStaticUserID(recurringInvoice.getSender().getObjectID());

        if (Constants.APPROVE.equals(statusCode) || Constants.OPEN.equals(statusCode)) {
            accountingServiceLocal.createTransactionsForInvoice(invoice, recurringInvoice.getSender());
        }
        if (Constants.OPEN.equals(statusCode)) {
            MessageItem item = getRecurringInvoiceMessageItem(recurringInvoiceID);
            if (item.getEmailTemplateID() == null) {
                System.out.println("There is no template details for sending mail. RecurringInvoiceID:" + recurringInvoiceID + "; NewInvoiceID:" + newInvoiceID);
                return;
            }
            item.setInvoiceID(newInvoiceID);
            item.setType(SALES_INVOICE_CATEGORY);
            item.setClient(true);

            EntityToEmailTemplate toTemplate = new EntityToEmailTemplate();
            toTemplate.setEntityId(newInvoiceID);
            toTemplate.setEntityType(Constants.SALES_INVOICE_CATEGORY);
            toTemplate.setMailReceiverId(item.getContactId());
            toTemplate.setEmailTemplateId(item.getEmailTemplateID());

            if (item.getContactId() != null) {
                EdsCrmContact crmContact = crmContactManager.get(item.getContactId());
                if (crmContact.isDeleted()) {
                    EdsCrmAccount clintOrSupplier = crmAccountManager.get(crmContact.getCrmAccount().getObjectID());
                    if (clintOrSupplier != null) {
                        Set<EdsCrmContact> contacts = clintOrSupplier.getCrmContacts();
                        if (contacts != null) {
                            for (EdsCrmContact c : contacts) {
                                if (c.getPrimaryEmail() != null && !"".equals(c.getPrimaryEmail().trim()) && !c.isDeleted() && c.isPrimaryContact()) {
                                    crmContact = c;
                                    item.setContactId(c.getObjectID());
                                    toTemplate.setMailReceiverId(c.getObjectID());
                                    break;
                                }
                            }
                        }
                    }
                    invoice.setClientContact(crmContact);
                    invoiceManager.update(invoice);
                }
            }
            EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateData(toTemplate, item.getSenderID());
            item.setMailContent(templateItem.getMessageHTML());
            item.setSubject(templateItem.getSubject());
            sendToClient(item);
        }
    }

    @Override
    public synchronized void createPurchaseInvoiceTransaction(Integer newInvoiceID, Integer recurringBillID) {
        EdsInvoice invoice = invoiceManager.get(newInvoiceID);
        String statusCode = invoice.getStatus().getCode();
        if (Constants.APPROVE.equals(statusCode) || Constants.OPEN.equals(statusCode)) {
            EdsRecurringBill edsRecurringBill = invoiceManager.getRecurringBill(recurringBillID);
            ServerSecurityContext.getInstance().setStaticUserID(edsRecurringBill.getSender().getObjectID());
            accountingServiceLocal.createTransactionsForInvoice(invoice, edsRecurringBill.getSender());
        }
    }

    public static String generateSelectedTemplate(String template, Date invDate, Date dueDate, String paymentMethod, Date startDate, String number) {
        String dueDay = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        if (invDate != null) {
            dueDate = DateUtil.resetTime(dueDate);
            int i = 0;
            while (invDate.before(dueDate)) {
                i++;
                invDate = DateUtil.addDays(invDate, 1);
            }
            dueDay = String.valueOf(i != 0 ? i : 1);
        }
        if (template != null) {
            if (invDate != null)
                template = template.replace("${dueday}", dueDay);
            if (dueDate != null)
                template = template.replace("${duedate}", dateFormat.format(dueDate));
            if (paymentMethod != null)
                template = template.replace("${paymentmethod}", paymentMethod);
            if (startDate != null) {
                template = template.replace("${startdate}", dateFormat.format(startDate));
            }
            if (number != null) {
                template = template.replace("${number}", number);
            }
            return template;
        }
        return "";
    }

    private void initializeSaveResult(NewInvoice data, SaveResult result, EdsFinancialSettings financialSettings) {
        result.setExceededCreditLimit(false);
        result.setInvoiceExist(false);
        result.setRestrictCreatingOrUpdatingInvoices(financialSettings.getRestrictCreatingOrUpdatingInvoices());

        boolean genrateNewNumber = false;
        try {
            genrateNewNumber = financialSettings.isGenerateNewNumber();
        } catch (Exception e) {
            genrateNewNumber = false;
        }
        if (genrateNewNumber) {
            data.setForceValidNumberGenerate(genrateNewNumber);
        }
        data.setCalcScale(financialSettings.getCalculationScale());
    }

    private InvoiceNumberData generateInvoiceNumberData(NewInvoice data, EdsCompany company) {
        InvoiceNumberData numberData = getSaleInvoiceNumber(company);

        if (numberData.isWithDate() && data.getInvoiceDate() != null) {
            numberData.setDate(new SimpleDateFormat("yyyyMMdd").format(data.getInvoiceDate().getNonConvertedDate()));
        }
        if (numberData.isWithClient() && data.getTypeItem() != null) {
            numberData.setClientCode(data.getTypeItem().getCode());
        }
        if (numberData.isWithProject() && data.getRelatedProject() != null && data.getRelatedProject().getId() != null) {
            EdsProject project = projectManager.get(data.getRelatedProject().getId());
            numberData.setProjectCode((project != null && project.getNumber() != null) ? project.getNumber() : "");
        }
        DecimalFormat format = new DecimalFormat("0000");
        while (isSaleInvoiceExists(numberData.getInvoiceNumber())) {
            System.out.println("Sales Invoice with number " + numberData.getInvoiceNumber() + " already exists");
            numberData.setFourDigitNumber(format.format(Integer.parseInt(numberData.getFourDigitNumber()) + 1));
        }

        return numberData;
    }

    private void checkCreditLimit(EdsCrmAccount account, BigDecimal creditLimit) {

        if (account.getCurrency() != null && !currencyService.getBaseCurrency().getId().equals(account.getCurrency().getObjectID())) {
            Double exchangeRate = currencyService.getCurrencyRateByDate(account.getCurrency().getObjectID(), new DateNonConvertable(new Date())).getExchangeRate();
            creditLimit = creditLimit.divide(BigDecimal.valueOf(exchangeRate), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
        }

    }

    private SaveResult checkCreditLimitExceeded(EdsCrmAccount account, BigDecimal creditLimit, NewInvoice data, SaveResult saveResult) {
        BigDecimal clientBalance = crmAccountManager.getClientBalance(account.getObjectID());
        checkCreditLimit(account, creditLimit);
        if (!data.isForceSave() && creditLimit.subtract(data.getTotal().add(clientBalance)).compareTo(BigDecimal.ZERO) < 0) {
            saveResult.setCreditLimit(creditLimit);
            saveResult.setRemainingBalance(clientBalance);
            saveResult.setExceededCreditLimit(true);
            return saveResult;
        }
        return null;
    }

    private EdsBaseSaleInvoice initializeInvoice(NewInvoice data, boolean isRecurringInvoice) {
        EdsBaseSaleInvoice invoice;
        if (isRecurringInvoice) {
            invoice = new EdsRecurringInvoice();
            ((EdsRecurringInvoice) invoice).setInvoiceType(data.getInvoiceType());
        } else {
            invoice = new EdsSaleInvoice();
            //Set Order Number when it comes from zapier
            if (data.getZapierordernumber() != null) {
                ((EdsSaleInvoice) invoice).setZapierordernumber(data.getZapierordernumber());
            }
            invoice.setRecurrence_number(data.getRecurrenceNumber());
            invoice.setRecurrence_pattern(data.getRecurrencePatternId() != null ? referenceManager.get(data.getRecurrencePatternId()) : null);

            if (data.getConvertedItemID() != null) {
                invoice.setConvertedQuotes(Collections.singleton(quoteManager.get(data.getConvertedItemID())));
            }
            if (data.getTargetGrnId() != null) {
                final EdsShippingData shippingData = this.shippingDataManager.get(data.getTargetGrnId());

                if (shippingData != null && !shippingData.isDeleted() && shippingData.getQuote() != null) {
                    invoice.setConvertedQuotes(Collections.singleton(shippingData.getQuote()));
                }
            }

            if (data.getPeriodStart() != null) {
                ((EdsSaleInvoice) invoice).setFromDate(data.getPeriodStart().getNonConvertedDate());
            }
            if (data.getOpportunityID() != null) {
                ((EdsSaleInvoice) invoice).setOpportunityID(data.getOpportunityID());
            }
            if (data.getPeriodEnd() != null) {
                ((EdsSaleInvoice) invoice).setToDate(data.getPeriodEnd().getNonConvertedDate());
            }
            invoice.setPriceLevelID(data.getPriceLevel() != null ? data.getPriceLevel().getId() : null);
            invoice.setClientDiscountID(data.getClientDiscount() != null ? data.getClientDiscount().getId() : null);
            ((EdsSaleInvoice) invoice).setPreviousBalance(data.getPreviosBalance());
            ((EdsSaleInvoice) invoice).setPaymentReceived(data.getPaymentsReceived());
            ((EdsSaleInvoice) invoice).setProjectBasedInvoice(data.isProjectBasedInvoice());
            ((EdsSaleInvoice) invoice).setQuotePercent(data.getConvertedPercent());
            ((EdsSaleInvoice) invoice).setQuoteAmount(data.getConvertedAmount());
            ((EdsSaleInvoice) invoice).setInvoiceType(data.getInvoiceType());
            invoice.setUpdatedDate(new Date());

            if (data.getInvoiceTermsItem() != null && data.getInvoiceTermsItem().getId() != null) {
                ((EdsSaleInvoice) invoice).setInvoiceTerms(invoiceTermsManager.get(data.getInvoiceTermsItem().getId()));
            }
            ((EdsSaleInvoice) invoice).setInTarget(false);
        }
        invoice.setQuoteNumber(data.getQuoteNumber());
        invoice.setIntroduction(data.getIntroduction());

        if (data.getShippingMethodID() != null) {
            invoice.setShippingMethod(shippingMethodManager.get(data.getShippingMethodID()));
            invoice.setShippingAmount(data.getShippingPrice());
        }
        if (data.getMarkupAccount() != null) {
            invoice.setMarkupAccount(accountingManager.get(data.getMarkupAccount().getId()));
        }
        invoice.setBillExpTotal(data.getBillableExpenseAmount());
        invoice.setBillExpTaxTotal(data.getBillableExpenseTaxAmount());
        invoice.setMarkupAmount(data.getMarkupAmount());
        invoice.setPercent(data.isPercent());
        if (data.getClientContactID() != null) {//Used In Batch Invoice
            invoice.setContact(crmContactManager.get(data.getClientContactID()));
        }

        invoice.setClient(clientManager.get(data.getClientID()));
        if (data.getClientID() != null) {
            invoice.setClientContact(clientContactManager.getPrimaryClientContact(data.getClientID()));
        }
        invoice.setBankAccount((data.getBankAccount() != null && data.getBankAccount().getId() != null) ? bankAccountManager.get(data.getBankAccount().getId()) : null);
        invoice.setTaxCalculationType(data.getTaxCalculationType());
        invoice.setCalcScale(data.getCalcScale());

        if (data.getPaymentInstructionID() != null) {
            invoice.setPaymentInstructionID(data.getPaymentInstructionID());
        } else {
            SelectItem[] item = getPaymentInstructions(SALE_INVOICE);
            if (item != null && item.length > 0 && item[0] != null) {
                invoice.setPaymentInstructionID(item[0].getId());
                String paymentInstruction = generateSelectedTemplate(item[0].getDescription(), data.getInvoiceDate().getDate(), data.getDueDate().getDate(), data.getPaymentMethod(), data.getInvoiceDate().getDate(), data.getInvoiceNumber());
                data.setPaymentInstruction(paymentInstruction);
            }
        }
        invoice.setTotalDiscount(data.getTotalDiscount());

        return invoice;
    }


    private void backfillFaiReportedDate(EdsInvoice invoice) {
        if (invoice.getReportedDate() != null || !genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FAI_INTEGRATION_ENABLED)) {
            return;
        }
        invoice.setReportedDate(new Date());
    }

    private SaveResult saveSaleInvoiceData(NewInvoice data, String quoteNumber, boolean runWebhook) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        SaveResult saveResult = new SaveResult();
        initializeSaveResult(data, saveResult, financialSettings);

        EdsUser user = data.getUserID() != null ? userManager.get(data.getUserID()) : invoiceManager.getUser();
        EdsCompany company = user.getCompany();

        boolean isRecurringInvoice = data.getRecurrenceJobItem() != null;

        if (!isRecurringInvoice) {
            EdsCrmAccount account = crmAccountManager.get(data.getClientID());
            BigDecimal creditLimit = account != null ? account.getCreditLimit() : null;

            if (data.isConvertingFromQuote() || data.isForceValidNumberGenerate()) {

                if (isSaleInvoiceExists(data.getInvoiceNumber())) {
                    System.out.println("Sales Invoice with number " + data.getInvoiceNumber() + " already exists");
                    InvoiceNumberData numberData = generateInvoiceNumberData(data, company);
                    data.setInvoiceNumber(numberData.getInvoiceNumber());
                    data.setFourDigitNumber(numberData.getFourDigitNumber());
                }
            } else {

                if (isSaleInvoiceExists(data.getInvoiceNumber())) {
                    System.out.println("Sales Invoice with number " + data.getInvoiceNumber() + " already exists");
                    saveResult.setInvoiceExist(true);
                    return saveResult;
                } else if (creditLimit != null && creditLimit.compareTo(BigDecimal.ZERO) > 0) {
                    SaveResult result = checkCreditLimitExceeded(account, creditLimit, data, saveResult);
                    if (result != null) {
                        return result;
                    }
                }
            }

            if (data.isConvertingFromQuote()) {
                if (creditLimit != null && creditLimit.compareTo(BigDecimal.ZERO) > 0) {
                    SaveResult result = checkCreditLimitExceeded(account, creditLimit, data, saveResult);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        EdsBaseSaleInvoice invoice = initializeInvoice(data, isRecurringInvoice);
        EdsProject oldProject = invoice.getRelatedProject(), newProject = null;
        if (data.getRelatedProjectID() != null) {
            newProject = projectManager.get(data.getRelatedProjectID());
            invoice.setRelatedProject(newProject);
        }
        super.initInvoiceData(invoice, data);

        invoice.setFixedAssetRelated(data.isFixedAssetRelated());

        //Invoice Custom Fields
        invoice.setCustomFields(createInvoiceCustomFields(data.getCustomFieldItems()));

        if (data.getInvoiceCustomType() != null && !data.getInvoiceCustomType().isEmpty()) {
            invoice.setCustomType(getInvoiceCustomType(data.getInvoiceCustomType()));
        }

        if (!isRecurringInvoice) {
            //fourDigitNumber maybe null in case the data comes from quote service during converting to invoice.
            String fourDigitNumber = data.getFourDigitNumber() != null ? data.getFourDigitNumber() : getSaleInvoiceNumber(company).getFourDigitNumber();
            invoice.setFourDigitNumber(Integer.valueOf(fourDigitNumber));
            updateProjectsInvoiceDate(data);
        }

        updateConvertedShippingData(invoice, data);
        //Set taxes
        initTaxTotals(invoice, data.getTotalTaxItems());

        //SAVE INVOICE ITEMS
        saveResult.setId(initInvoiceItemsForSave(data, invoice));
        saveResult.setNumber(invoice.getNumber());

        //Set converted quote details
        if (!CollectionUtils.isEmpty(data.getConvertedQuoteIDs()) || !CollectionUtils.isEmpty(invoice.getConvertedQuotes())) {
            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(SaleInvoiceCustomEventListenerImpl.TYPE, SaleInvoiceCustomEventListenerImpl.EVENT_UPDATE_SOQ_INVOICING_DATA, (EdsSaleInvoice) invoice, user);
            ConvertedQuotesDto dto = new ConvertedQuotesDto("saveSI", data.getProgressInvoicingType(), !CollectionUtils.isEmpty(data.getConvertedQuoteIDs()) ? data.getConvertedQuoteIDs() : null);
            event.setCustomStringField(new Gson().toJson(dto));
        }
        if (isRecurringInvoice) {
            ((EdsRecurringInvoice) invoice).setSender(user);
            saveInvoiceRecurringItem(data, saveResult.getId());
        }
        if (!isRecurringInvoice) {
            if (!isOk(data.getApprovers())) {
                EdsReference status;
                if (Constants.APPROVE.equals(data.getStatusCode())) {
                    status = hasInventoryItem(invoice) && !isConvertedToGdnGrn(invoice) ? referenceManager.findReference(Constants.INVOICE_STATUS, Constants.PENDING) :
                            referenceManager.findReference(Constants.INVOICE_STATUS, Constants.APPROVE);
                } else {
                    status = referenceManager.findReference(Constants.INVOICE_STATUS, data.getStatusCode());
                }
                invoice.setEntityStatus(status);
            }
            if (isOk(data.getApprovers())) {
                saveInvoiceApprovers(invoice, data.getApprovers(), data.getStatusCode(), APPROVE);
                invoiceManager.update(invoice);

                EdsBusinessEvent workflowApprovingEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), invoice, user);
                workflowApprovingEvent.setEntityType(RelationItem.TYPE_SALEINVOICE);
            }
            addSaleInvoiceToSolr((EdsSaleInvoice) invoice);
            updateRelatedProjectFromSolr(oldProject, newProject, company);
        }

        if (invoice instanceof EdsSaleInvoice) {
            backfillFaiReportedDate(invoice);

            if (data.isFixedAssetRelated()) {
                EdsFixedAsset fixedAsset = fixedAssetManager.get(data.getFixedAssetItem().getObjectID());
                fixedAsset.setSalesInvoice((EdsSaleInvoice) invoice);
                fixedAssetManager.update(fixedAsset);

                data.getFixedAssetItem().setDisposeType(DISPOSE_ACCOUNTS_RECEIVABLE);
                data.getFixedAssetItem().setDisposeAmount(invoice.getTotal());
                data.getFixedAssetItem().setDisposeTaxAmount(invoice.getTotalTaxes());
                data.getFixedAssetItem().setDisposedDate(data.getInvoiceDate());
                fixedAssetService.disposeFixedAssetItem(data.getFixedAssetItem());
            }

            if (quoteNumber != null) {
                EdsBusinessEvent event = baseEventPostProcessor.registerEvent(SaleInvoiceEventListenerImpl.TYPE, SaleInvoiceEventListenerImpl.EVENT_SALES_INVOICE_CONVERTED_FROM_SALES_QUOTE, (EdsSaleInvoice) invoice, user);
                event.setCustomStringField(quoteNumber);
            } else {
                baseEventPostProcessor.registerEvent(SaleInvoiceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, (EdsSaleInvoice) invoice, user);

                if (invoice.getStatus() != null && SUBMITTED_TO_MANAGER.equals(invoice.getStatus().getCode())) {
                    baseEventPostProcessor.registerEvent(SaleInvoiceEventListenerImpl.TYPE, SaleInvoiceEventListenerImpl.EVENT_SALES_INVOICE_SUBMITTED_TO_MANAGER, (EdsSaleInvoice) invoice, user);
                } else if (invoice.getStatus() != null && APPROVE.equals(invoice.getStatus().getCode())) {
                    baseEventPostProcessor.registerEvent(SaleInvoiceEventListenerImpl.TYPE, SaleInvoiceEventListenerImpl.EVENT_SALES_INVOICE_MANAGER_APPROVE, (EdsSaleInvoice) invoice, user);
                }
            }
            final ReceivePaymentData receivePaymentData = data.getPaymentData();

            if (receivePaymentData != null && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PAID_AND_PRINT_INVOICE)) {
                PaymentData[] payments = receivePaymentData.getPayments();

                if (payments != null) {
                    for (PaymentData payment : payments) {
                        payment.setInvoiceID(invoice.getObjectID());
                    }
                }
                this.saveReceivePaymentData(receivePaymentData, true);
            }
            final Integer templateId = data.getHtmlTemplateId();

            if (templateId != null) {
                final InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(invoice.getObjectID(), templateId, null);

                saveResult.setPdfTemplate(savedSaleInvoiceViewPDFHandler.velocityReplaceContentAttributes(requestObject));
            }

            EdsCrmAccount edsCrmAccount = clientManager.get(data.getClientID());
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_CUSTOM_CRM_ACCOUNT) && edsCrmAccount != null) {
                EdsCustomCrmAccount edsCustomCrmAccount = new EdsCustomCrmAccount();
                edsCustomCrmAccount.setEntityId(invoice.getObjectID());
                edsCustomCrmAccount.setEntityType(EntityTypeEnum.SALE_INVOICE.name());
                edsCustomCrmAccount.setClientName(edsCrmAccount.getName());
                edsCustomCrmAccount.setClientNumber(edsCrmAccount.getNumber());
                edsCustomCrmAccount.setClientId(edsCrmAccount.getObjectID());
                edsCustomCrmAccount.setVatNumber(edsCrmAccount.getVatNumber());
                edsCustomCrmAccount.setTrnNumber(edsCrmAccount.getTrn());

                if (edsCrmAccount.getBillingAddress() != null) {
                    edsCustomCrmAccount.setBillingAddressName(edsCrmAccount.getBillingAddress().getName());
                    edsCustomCrmAccount.setBillingAddress(edsCrmAccount.getBillingAddress().getAddress());
                    edsCustomCrmAccount.setBillingAddressb(edsCrmAccount.getBillingAddress().getAddressb());
                    edsCustomCrmAccount.setBillingCity(edsCrmAccount.getBillingAddress().getCity());
                    edsCustomCrmAccount.setBillingCountryName(edsCrmAccount.getBillingAddress().getCountryName());
                    edsCustomCrmAccount.setBillingStateName(edsCrmAccount.getBillingAddress().getStateName());
                    edsCustomCrmAccount.setBillingZipCode(edsCrmAccount.getBillingAddress().getZipCode());
                }

                if (edsCrmAccount.getMailingAddress() != null) {
                    edsCustomCrmAccount.setMailingAddressName(edsCrmAccount.getMailingAddress().getName());
                    edsCustomCrmAccount.setMailingAddress(edsCrmAccount.getMailingAddress().getAddress());
                    edsCustomCrmAccount.setMailingAddressb(edsCrmAccount.getMailingAddress().getAddressb());
                    edsCustomCrmAccount.setMailingCity(edsCrmAccount.getMailingAddress().getCity());
                    edsCustomCrmAccount.setMailingCountryName(edsCrmAccount.getMailingAddress().getCountryName());
                    edsCustomCrmAccount.setMailingStateName(edsCrmAccount.getMailingAddress().getStateName());
                    edsCustomCrmAccount.setMailingZipCode(edsCrmAccount.getMailingAddress().getZipCode());
                }

                customCrmAccountManager.create(edsCustomCrmAccount);
            }

            List<EdsShippingData> convertedShippingData = shippingDataManager.getGrnGdnsByInvoiceId(invoice.getObjectID());

            try {
                if (convertedShippingData != null && !convertedShippingData.isEmpty()) {
                    shippingDataSolrComponent.indexes(convertedShippingData);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        if ((data.getConvertedItemID() != null || data.getTargetGrnId() != null) && !StringUtils.isEmpty(data.getQuoteNumber())) {
            EdsSaleQuote saleQuote = null;
            if (data.getConvertedItemID() != null) {
                saleQuote = quoteManager.getSaleQuote(data.getConvertedItemID());
            }
            if (data.getTargetGrnId() != null) {
                EdsShippingData shippingData = this.shippingDataManager.get(data.getTargetGrnId());
                if (shippingData != null && !shippingData.isDeleted() && shippingData.getQuote() != null) {
                    saleQuote = quoteManager.getSaleQuote(shippingData.getQuote().getObjectID());
                    List<EdsInvoice> invoices = saleQuote.getInvoices();
                    if (invoices != null && !invoices.isEmpty()) {
                        List<EdsShippingData> gdns = this.shippingDataManager.getByQuoteId(saleQuote.getObjectID());
                        // If there is more than one gnd, then the status is partial invoiced
                        if (gdns != null && !gdns.isEmpty() && gdns.size() > 1) {
                            EdsReference partialInvoiced = referenceManager.findReference(Constants.INVOICE_STATUS, Constants.PARTIAL_INVOICED);
                            if (partialInvoiced != null) {
                                saleQuote.setStatus(partialInvoiced);
                                quoteManager.update(saleQuote);
                                addSaleQuoteToSolr(saleQuote);
                            }
                        }
                    }
                }
            }

            if (saleQuote != null && saleQuote.getOpportunityID() != null) {
                List<EdsRelation> relations = relationManager.getAllRelations(saleQuote.isSalesOrder() ? RelationItem.TYPE_SALEORDER : RelationItem.TYPE_SALEQUOTE, saleQuote.getObjectID());
                if (!CollectionUtils.isEmpty(relations)) {
                    ArrayList<RelationItem> rpcs = new ArrayList<>();
                    for (EdsRelation relation : relations) {
                        RelationItem item = relation.wrapToRPC();
                        item.setObjectID(null);
                        item.setFromType(RelationItem.TYPE_SALEINVOICE);
                        item.setFromID(invoice.getObjectID());
                        item.setFromName(invoice.getNumber());
                        rpcs.add(item);
                    }
                    data.getRelations().addAll(rpcs);
                }

                if (invoice instanceof EdsSaleInvoice) {
                    ((EdsSaleInvoice) invoice).setOpportunityID(saleQuote.getOpportunityID());
                    invoiceManager.update(invoice);
                    addSaleInvoiceToSolr((EdsSaleInvoice) invoice);
                }
            }
        }

        if (!CollectionUtils.isEmpty(data.getRelations())) {
            allInOneServiceLocal.saveRelations(RelationItem.TYPE_SALEINVOICE, invoice.getObjectID(), invoice.getNumber(), data.getRelations());
        }
        if (!data.isFromWorkflow()) {
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, invoice, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_SALEINVOICE);
            workflowEvent.setCustomStringField(WorkflowActionDetectedEventListenerImpl.FROM_WORKFLOW);
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        kpiLog.setEntityId(saveResult.getId());
        if (isRecurringInvoice) {
            EdsRecurringInvoice recurringInvoice = (EdsRecurringInvoice) invoice;
            baseEventPostProcessor.registerEvent(RecurringInvoiceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, recurringInvoice, user);
            kpiLog.setEntityName(EdsRecurringInvoice.class.getSimpleName());
            ServerUtils.kpiLog(log, kpiLog, "Added new Recurring Invoice");
        } else {
            kpiLog.setEntityName(EdsSaleInvoice.class.getSimpleName());
            ServerUtils.kpiLog(log, kpiLog, "Added new Sale Invoice");
        }
        if (runWebhook && invoice instanceof EdsSaleInvoice) {
            try {
                List<EdsRestHook> webhooks = restHookManager.getByEventName("order.create");
                if (!webhooks.isEmpty()) {
                    for (EdsRestHook webhook : webhooks) {
                        try {
                            if (!"https://hooks.zapier.com/fake-subscription-url".equalsIgnoreCase(webhook.getTargetUrl())) {
                                log.info("Triggering webhook {}: {}", webhook.getEventName(), webhook.getTargetUrl());

                                HttpHeaders httpHeaders = new HttpHeaders();
                                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                                HttpEntity<ZapierInvoiceItemTO> httpRequest = new HttpEntity<>(convertToZapierOrder((EdsSaleInvoice) invoice), httpHeaders);

                                String resp = restTemplate.postForObject(webhook.getTargetUrl(), httpRequest, String.class);
                                log.info("ZAPIER WEBHOOK: {}", resp);
                            }
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    }
                }
            } catch (Exception e1) {
                log.error("", e1);
            }
        }
        return saveResult;
    }

    private ZapierInvoiceItemTO convertToZapierOrder(EdsSaleInvoice saleInvoice) {
        ZapierInvoiceItemTO result = new ZapierInvoiceItemTO();
        if (saleInvoice != null) {
//            ZapierInvoiceItemTO salesInvoice = new ZapierInvoiceItemTO();
            result.setId(saleInvoice.getObjectID());
            result.setInvoice_number(saleInvoice.getNumber());
            if (saleInvoice.getInvoiceDate() != null) {
                SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ssZ");
                result.setInvoice_date(longDateTimezoneFormat.format(saleInvoice.getInvoiceDate()));
            }
            if (saleInvoice.getStatus() != null) {
                result.setInvoice_status(new InvoiceStatusTO(saleInvoice.getStatus().getName(), saleInvoice.getStatus().getCode()));
            }
            result.setInvoice_total(saleInvoice.getTotal());

            if (saleInvoice.getCurrency() != null) {
                CurrencyTO currency = new CurrencyTO();
                currency.setCurrency_id(saleInvoice.getCurrency().getObjectID());
                currency.setCurrency_name(saleInvoice.getCurrency().getName());
                result.setInvoice_currency(currency);
            }

            //Get Other details
            NewInvoice invoice = getInvoiceSummaryData(saleInvoice.getObjectID());
            if (invoice != null) {
                result.setReference(saleInvoice.getReference());
                result.setDue_date(saleInvoice.getDueDate());
                result.setSubtotal(saleInvoice.getSubtotal());

                result.setBase_total(saleInvoice.getTotal());
                result.setBase_subtotal(saleInvoice.getSubtotal());
                result.setDiscount_total(saleInvoice.getTotalDiscount());
                result.setBase_discount_total(saleInvoice.getDiscountAmount());
                result.setTax_total(saleInvoice.getTotalTaxes());
                result.setBase_tax_total(saleInvoice.getTotalTaxes());
                result.setDue_amount(saleInvoice.getDueAmount());
                result.setExchange_rate(saleInvoice.getExchangeRate());
                result.setShipping_price(saleInvoice.getShippingAmount());

                result.setCustomer(new CustomerTO(saleInvoice.getClient().getObjectID(), invoice.getClientName(), saleInvoice.getClient().getEmail()));
                EdsAddress billAddress = addressManager.get(saleInvoice.getBillAddressID());
                if (billAddress != null) {
                    result.setBill_to_address(new com.edatasite.workforce.rest.base.to.AddressTO(billAddress.getRPC()));
                }
                EdsAddress mailAddress = addressManager.get(saleInvoice.getMailAddressID());
                if (mailAddress != null && !mailAddress.getAddressDataAsHTML().isEmpty()) {
                    result.setShip_to_address(new com.edatasite.workforce.rest.base.to.AddressTO(mailAddress.getRPC()));
                } else if (userManager.getUser() != null && userManager.getUser().getCompany() != null &&
                        userManager.getUser().getCompany().getMailingAddress() != null) {
                    mailAddress = userManager.getUser().getCompany().getMailingAddress();
                    result.setShip_to_address(new com.edatasite.workforce.rest.base.to.AddressTO(mailAddress.getRPC()));
                }


                result.setIntroduction(invoice.getIntroduction());
//                com.edatasite.workforce.rest.base.to.SelectItemTO invoice_type;
                if (saleInvoice.getShippingMethod() != null) {
                    ShippingMethodTO shippingMethodTO = new ShippingMethodTO();
                    shippingMethodTO.setName(saleInvoice.getShippingMethod().getName());
                    result.setShipping_method(shippingMethodTO);
                }
                HistoryListItem[] historyListItem = invoiceCircularResolver.getInvoiceNotes(invoice.getID());
                if (historyListItem != null && historyListItem.length > 0) {
                    StringBuilder notes = new StringBuilder();
                    for (HistoryListItem note : historyListItem) {
                        notes.append(note.getComment(true)).append("<br> ");
                    }
                    result.setNotes(notes.toString());
                }
                /*com.edatasite.workforce.rest.base.to.SelectItemTO terms;
                com.edatasite.workforce.rest.base.to.SelectItemTO tax_type;
                com.edatasite.workforce.rest.base.to.SelectItemTO bank_account;
                com.edatasite.workforce.rest.base.to.SelectItemTO account;
                com.edatasite.workforce.rest.base.to.SelectItemTO email_template;*/
                ArrayList<InvoiceItemTO> items = new ArrayList<>();
                if (invoice.getItems() != null) {
                    for (EdsInvoiceItem invoiceItem : saleInvoice.getInvoiceItems()) {
                        EdsItem product = itemManager.get(invoiceItem.getItem().getObjectID());
                        if (product != null) {
                            InvoiceItemTO invoiceItemTO = new InvoiceItemTO();

                            ItemTO itemTO = new ItemTO(product.getObjectID(), product.getName(), product.getProductNumber(), ServerUtils.getProductTypeName(product.getProductType()));
                            itemTO.setSku(product.getInternalSKUNumber());
                            itemTO.setQuantity(product.getQty());
                            itemTO.setDescription(product.getDescription());
                            if (product.getSuppliers() != null && !product.getSuppliers().isEmpty()) {
                                for (EdsCrmAccount supplier : product.getSuppliers()) {
                                    itemTO.setSupplier(new SupplierTO(supplier.getObjectID(), supplier.getName()));
                                    break;
                                }
                            }
                            if (product.getParent() != null) {
                                itemTO.setParent_item_name(product.getParent().getName());
                            }
                            invoiceItemTO.setItem(itemTO);

                            invoiceItemTO.setZapiervariantid(product.getZapiervariantid());

                            invoiceItemTO.setItem_description(invoiceItem.getItem().getDescription());
                            invoiceItemTO.setItem_quantity(invoiceItem.getQty());
                            invoiceItemTO.setItem_price(invoiceItem.getUnitPrice());
                            invoiceItemTO.setTotal_discount(invoiceItem.getDiscountAmount());

                            items.add(invoiceItemTO);
                        }
                    }
                }
                result.setItems(items);
            }
        }
        return result;
    }

    private ArrayList<EdsQuote> createMultiQuoteData(NewInvoice invoice) {
        ArrayList<EdsQuote> result = new ArrayList<>();

        for (Integer quoteID : invoice.getConvertedQuoteIDs()) {
            EdsQuote quote = quoteManager.get(quoteID);

            //TODO This is too old logic, I did some research there are 3 companies that use this logic.
            // And I think create SO to SQ is a extra logic which is no needed

            /*if (quote.getStatus().getCode().equals(SALE_ORDER)
                    || quote.getStatus().getCode().equals(PICKED)
                    || quote.getStatus().getCode().equals(PACKED)) {
                isSaleOrder = true;
            }

            pickList = pickListManager.getPickListBySaleQuoteID(quoteID);
            if (pickList == null) {
                pickList = new EdsPickList();
            }
            if (!isSaleOrder) {
                BigDecimal plTotal = ZERO;
                BigDecimal plTaxAmount = ZERO;
                BigDecimal plDiscount = ZERO;
                BigDecimal exchangeRate = quote.getExchangeRate() != null ? quote.getExchangeRate() : new BigDecimal(1);
                List<EdsPickListItem> items = new LinkedList<>();
                for (EdsQuoteItem newItem : quote.getQuoteItems()) {

                    if (newItem.getProductItem() != null) {
                        EdsItem inventoryItem = newItem.getProductItem();

                        if (inventoryItem.getType() != null && inventoryItem.getType().equals(INVENTORY_ITEM)) {
                            accessCreatPickList = true;
                            EdsPickListItem listItem = new EdsPickListItem();
                            listItem.setProductItem(inventoryItem);
                            listItem.setQuantity(newItem.getQty());
                            listItem.setPickList(pickList);
                            items.add(listItem);

                            plTotal = plTotal.add(newItem.getAmmount().divide(exchangeRate, 4, RoundingMode.HALF_UP));

                            if (newItem.getVat() != null) {
                                plTaxAmount = plTaxAmount.add(newItem.getItemCalculatedTaxAmount(false).divide(exchangeRate, 4, RoundingMode.HALF_UP));
                            }

                            BigDecimal discount;
                            if (newItem.getItemDiscount() != null) {
                                discount = newItem.getNet().multiply(newItem.getItemDiscount().getPercentage()).divide(HUNDRED, 4, RoundingMode.HALF_UP);
                            } else {
                                discount = newItem.getDiscountAmount();
                            }
                            plDiscount = plDiscount.add(discount.divide(exchangeRate, 4, RoundingMode.HALF_UP));
                        }
                    }
                }
                if (accessCreatPickList) {
                    pickList.setSaleQuote((EdsSaleQuote) quote);
                    pickList.setPickListItems(items);
                    pickList.setTotal(plTotal);
                    pickList.setTaxAmount(plTaxAmount);
                    pickList.setDiscount(plDiscount);
                    pickListManager.create(pickList);
                    createQuoteHistory((EdsSaleQuote) quote);
                    baseEventPostProcessor.registerEvent(SalesOrderEventListenerImpl.TYPE, SalesOrderEventListenerImpl.EVENT_PICKLIST_SALE_ORDER, (EdsSaleQuote) quote, userManager.getUser());
                }
            }*/
            result.add(quote);
        }
        return result;
    }

    private void createQuoteHistory(EdsSaleQuote quote) {
        EdsUser user = quoteManager.getUser();
        String eventDescription = null;
        String status = quote.getStatus().getCode();
        String userFullName;
        quote.setUpdatedDate(new Date());
        if (user instanceof EdsClientContact) {
            if ((user.getFirstName() != null && !"".equals(user.getFirstName())) || (user.getLastName() != null && !"".equals(user.getLastName()))) {
                userFullName = user.getFirstName() + " " + user.getLastName();
            } else {
                userFullName = user.getFullName();
            }
        } else {
            userFullName = user.getFullName();
        }
        boolean sent = quote.getSent() != null && quote.getSent();
        if (DRAFT.equals(status)) {
            eventDescription = userFullName + " saved sale quote as draft.";
        } else if (APPROVE.equals(status) || CLIENT_APPROVE.equals(status) || (OPEN.equals(status) && !sent)) {
            eventDescription = userFullName + " approved sale quote.";
        } else if (OPEN.equals(status) && sent) {
            eventDescription = userFullName + " approved sale quote and sent message to client.";
        } else if (REJECT.equals(status)) {
            eventDescription = userFullName + " rejected sale quote.";
        } else if (CONVERTED.equals(status)) {
            eventDescription = userFullName + " converted sale quote to invoice.";
        }

        EdsQuoteHistory record = new EdsQuoteHistory();
        record.setQuote(quote);
        record.setCommentator(user);
        record.setComment(null);
        record.setEvent(quote.getStatus());

        record.setEventDescription(eventDescription);
        record.setEventDate(new Date());
        quoteHistoryManager.create(record);
    }

    public void updateSalesQuoteProgressInvoicingData(Integer saleInvoiceId, ConvertedQuotesDto convertedQuotes) {
        final EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        final Integer qtyScale = fs.getAccountingQtyCalculationScale();
        final Integer priceScale = fs.getAccountingCalculationScale();
        String methodKey = convertedQuotes.getMethodKey(), progInvoiceType = convertedQuotes.getProgInvoiceType();
        boolean saveCN = false;
        boolean deleteCN = false;
        if (methodKey.equals("saveCN")) {
            methodKey = "deleteSI";
            saveCN = true;
        } else if (methodKey.equals("deleteCN")) {
            methodKey = "deleteSI";
            deleteCN = true;
        }
        EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(saleInvoiceId);
        if (saleInvoice.isDeleted() || saleInvoice.getStatus() != null && REVERSED.equals(saleInvoice.getStatus().getCode())) {
            methodKey = "deleteSI";
        }
        boolean deleteOrVoid = "deleteSI".equals(methodKey);

        List<EdsShippingData> shippingDataList = new ArrayList<>(saleInvoice.getConvertedShippingData());

        List<EdsSaleQuote> quotes = quoteManager.getSaleQuotesByIds(ServerUtils.getAsCommoDelimited(convertedQuotes.getQuoteIds(), "0", ","));
        for (EdsSaleQuote saleQuote : quotes) {
            boolean fullyConverted = true;
            boolean partiallyConverted = false;

            if (saleQuote.isDeleted()) {
                return;
            }
            progInvoiceType = Optional.ofNullable(progInvoiceType).orElse(saleQuote.getProgressInvoicingType());

            if (saleQuote.isProgressInvoicing()) {
                if ("saveSI".equals(methodKey)) {
                    saleInvoice.getConvertedQuotes().add(saleQuote);
                }

                if ("byItem".equals(progInvoiceType) || "byAmount".equals(progInvoiceType)) {
                    for (EdsQuoteItem quoteItem : saleQuote.getQuoteItems()) {

                        if ("byItem".equals(progInvoiceType)) {
                            boolean trackBatchEnabled = quoteItem.getItem() != null ? quoteItem.getItem().getTrackBatchesEnabled() : false;
                            quoteItem.setConvertedQty(invoiceManager.getConvertedQtyByQuoteItem(quoteItem.getObjectID()).setScale(trackBatchEnabled ? qtyScale : 10, RoundingMode.HALF_UP));

                            if (quoteItem.getConvertedQty().compareTo(BigDecimal.ZERO) > 0) {
                                partiallyConverted = true;
                            }
                            if (quoteItem.getQty().setScale(qtyScale, RoundingMode.HALF_UP).compareTo(quoteItem.getConvertedQty()) > 0) {
                                fullyConverted = false;
                            }
                        } else if ("byAmount".equals(progInvoiceType)) {
                            quoteItem.setConvertedAmount(invoiceManager.getConvertedAmountByQuoteItem(quoteItem.getObjectID()).setScale(qtyScale, RoundingMode.HALF_UP));

                            if (quoteItem.getConvertedAmount().compareTo(BigDecimal.ZERO) > 0) {
                                partiallyConverted = true;
                            }

                            BigDecimal net = quoteItem.getQty().multiply(quoteItem.getUnitPrice()).setScale(priceScale, RoundingMode.HALF_UP);


                            // Compute the difference between the net value and the converted amount.
                            BigDecimal difference = net.subtract(quoteItem.getConvertedAmount());

                            // If the difference is less than 0.02, then consider it fully converted.
                            // In Invoice Transaction Total and Sub Total are also adjusted.
                            if (difference.compareTo(BigDecimal.ZERO) > 0 && difference.compareTo(new BigDecimal("0.05")) < 0) {
                                fullyConverted = true;
                                quoteItem.setConvertedAmount(net);
                            } else if (net.compareTo(quoteItem.getConvertedAmount()) > 0) {
                                fullyConverted = false;
                            }
                        }
                    }
                }
                if ("byPercent".equals(progInvoiceType) || "byCustom".equals(progInvoiceType) || "byMultiProgress".equals(progInvoiceType)) {
                    saleQuote.setConvertedPercent(invoiceManager.getConvertedInvoicesPercent(saleQuote.getObjectID(), saleInvoice.getObjectID(), deleteOrVoid));
                    saleQuote.setConvertedAmount(invoiceManager.getConvertedInvoicesAmount(saleQuote.getObjectID(), saleInvoice.getObjectID(), deleteOrVoid));
                    BigDecimal totalConverted = saleQuote.getConvertedAmount() != null && saleQuote.getNetAmountTotal().compareTo(BigDecimal.ZERO) != 0 ?
                            saleQuote.getConvertedAmount()
                                    .divide(saleQuote.getNetAmountTotal(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP)
                                    .multiply(HUNDRED).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP) : BigDecimal.ZERO;

                    BigDecimal totalConvertedPercent = saleQuote.getConvertedPercent() != null ? saleQuote.getConvertedPercent().setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP) : BigDecimal.ZERO;

                    fullyConverted = totalConverted.compareTo(HUNDRED) >= 0 || totalConvertedPercent.compareTo(HUNDRED) >= 0;
                    partiallyConverted = (totalConverted.compareTo(BigDecimal.ZERO) > 0 || totalConvertedPercent.compareTo(ZERO) > 0) && !fullyConverted;
                }

                if (saleQuote.getStatus() != null && !INVOICE_STATUS_CLOSED.equals(saleQuote.getStatus().getCode())) {
                    if (fullyConverted) {
                        saleQuote.setStatus(referenceManager.findReference(INVOICE_STATUS, INVOICED));
                    } else if (partiallyConverted) {
                        saleQuote.setStatus(referenceManager.findReference(INVOICE_STATUS, PARTIAL_INVOICED));
                    } else if (saleQuote.isSalesOrder()) {
                        saleQuote.setStatus(referenceManager.findReference(INVOICE_STATUS, SALE_ORDER));
                    } else {
                        saleQuote.setStatus(referenceManager.findReference(INVOICE_STATUS, CLIENT_APPROVE));
                    }
                }

                /*
                 * If you delete all of the invoices from SQ/SO then you can select convert option from scrach
                 */
                if ((SALE_ORDER.equals(saleQuote.getStatus().getCode()) || CLIENT_APPROVE.equals(saleQuote.getStatus().getCode())) && (!saveCN && !deleteCN)) {
                    saleQuote.setProgressInvoicingType(null);
                } else {
                    saleQuote.setProgressInvoicingType(progInvoiceType);
                }

            } else {
                if ("saveSI".equals(methodKey)) {
                    saleInvoice.getConvertedQuotes().add(saleQuote);

                    if (saleQuote.getQuoteItems() != null && saleQuote.getQuoteItems().size() > 0) {
                        for (EdsQuoteItem quoteItem : saleQuote.getQuoteItems()) {
                            quoteItem.setConvertedQty(invoiceManager.getConvertedQtyByQuoteItem(quoteItem.getObjectID()));
                        }
                    }

                    if ((!saleQuote.isSalesOrder() && !genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST)) || (saleQuote.isSalesOrder() && !PARTIAL_SHIPPED.equals(saleQuote.getStatus().getCode())) || (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST) && !saleQuote.isSalesOrder() && !PARTIAL_SHIPPED.equals(saleQuote.getStatus().getCode()))) {

                        if (saleQuote.isSalesOrder() || (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST) && !saleQuote.isSalesOrder())) {
                            List<EdsShippingData> list = shippingDataManager.getByQuoteId(saleQuote.getObjectID());
                            list.removeAll(saleInvoice.getConvertedShippingData());

                            if (!CollectionUtils.isEmpty(list)) {
                                List<Integer> shippingDataIds = new ArrayList<>();
                                list.forEach(data -> shippingDataIds.add(data.getObjectID()));
                                List<Integer> draftInvoices = shippingDataManager.getAllInvoicesByShippingDataIds(shippingDataIds, DRAFT);

                                List<EdsShippingData> notConvertedGDNs = list.stream().filter(shd -> shd.getStatus() == null || shd.getStatus() != ShippingDataStatus.CONVERTED).toList();

                                if (CollectionUtils.isEmpty(notConvertedGDNs)) {
                                    if (saleQuote != null && saleQuote.getStatus() != null && !INVOICE_STATUS_CLOSED.equals(saleQuote.getStatus().getCode())) {
                                        saleQuote.setStatus(referenceManager.findReference(INVOICE_STATUS,
                                                !DRAFT.equals(saleInvoice.getStatus().getCode()) && CollectionUtils.isEmpty(draftInvoices) ? INVOICED : CONVERTED));
                                        if (!quoteManager.isFullyShipped(saleQuote.getObjectID())) {
                                            saleQuote.setStatus(referenceManager.findReference(INVOICE_STATUS, PARTIAL_SHIPPED));
                                        }
                                    }
                                }
                            } else {
                                if (saleQuote != null && saleQuote.getStatus() != null && !INVOICE_STATUS_CLOSED.equals(saleQuote.getStatus().getCode())) {
                                    saleQuote.setStatus(referenceManager.findReference(INVOICE_STATUS, !DRAFT.equals(saleInvoice.getStatus().getCode()) ? INVOICED : CONVERTED));
                                }
                            }
                        } else {
                            saleQuote.setStatus(referenceManager.findReference(INVOICE_STATUS, !DRAFT.equals(saleInvoice.getStatus().getCode()) ? INVOICED : CONVERTED));
                        }
                    }
                } else {
                    if (saleQuote.isSalesOrder() || (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST) && !saleQuote.isSalesOrder())) {

                        if (saleQuote.getStatus() != null && !INVOICE_STATUS_CLOSED.equals(saleQuote.getStatus().getCode())) {
                            if (saleInvoice.getConvertedShippingData().isEmpty()) {
                                if (saleQuote.isSalesOrder() && !deleteCN) {
                                    saleQuote.setStatus(referenceManager.findReference(INVOICE_STATUS, SALE_ORDER));
                                } else if (saleQuote.isSalesOrder() && deleteCN && !saleQuote.isProgressInvoicing()) {
                                    saleQuote.setStatus(referenceManager.findReference(INVOICE_STATUS, INVOICED));
                                } else {
                                    saleQuote.setStatus(referenceManager.findReference(INVOICE_STATUS, CLIENT_APPROVE));
                                }

                            } else {
                                saleQuote.setStatus(referenceManager.findReference(INVOICE_STATUS, PARTIAL_SHIPPED));

                                if (quoteManager.isFullyShipped(saleQuote.getObjectID())) {
                                    saleQuote.setStatus(referenceManager.findReference(INVOICE_STATUS, SHIPPED));
                                    List<EdsInvoice> salesOrderInvoices = saleQuote.getInvoices();
                                    if (salesOrderInvoices != null && !salesOrderInvoices.isEmpty()) {
                                        saleQuote.setStatus(referenceManager.findReference(INVOICE_STATUS, PARTIAL_INVOICED));
                                    }
                                }
                            }
                        }
                        if (saleInvoice.getConvertedShippingData() != null && !saleInvoice.getConvertedShippingData().isEmpty()) {
                            saleInvoice.getConvertedShippingData().clear();
                        }

                        if (!shippingDataList.isEmpty()) {
                            for (EdsShippingData shippingData : shippingDataList) {
                                shippingData.setStatus(ShippingDataStatus.SUCCESSFUL);
                            }
                            try {
                                shippingDataManager.updateAll(new ArrayList<>(shippingDataList), shippingDataList.size());
                                shippingDataSolrComponent.indexes(shippingDataList);
                                EventHandler.fireEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, "Related Invoice is voided");
                            } catch (Exception e) {
                                log.error("Error occurred while updating SI", e);
                            }
                        }
                    } else {
                        if (saleQuote.getStatus() != null && !INVOICE_STATUS_CLOSED.equals(saleQuote.getStatus().getCode())) {
                            saleQuote.setStatus(referenceManager.findReference(INVOICE_STATUS, CLIENT_APPROVE));
                        }
                    }

                    if (!saveCN) {
                        for (EdsInvoiceItem invoiceItem : saleInvoice.getInvoiceItems()) {
                            if (invoiceItem.getQuoteItemId() != null) {
                                EdsQuoteItem quoteItem = quoteManager.getQuoteItemByID(invoiceItem.getQuoteItemId());
                                if (quoteItem != null) {
                                    quoteItem.setConvertedQty(quoteItem.getConvertedQty().subtract(invoiceItem.getQty()));
                                }
                            }
                        }
                    }
                }
            }
            quoteManager.update(saleQuote);
            addSaleQuoteToSolr(saleQuote);

            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
                quoteManager.calculateCustomerQuoteBalance(saleQuote.getClient().getObjectID());
            }
        }
        if (deleteOrVoid && (!saveCN && !deleteCN)) {
            invoiceManager.removeRelationFromQuote(saleInvoiceId, convertedQuotes.getQuoteIds());
        }
    }

    private void addSaleInvoiceToSolr(EdsSaleInvoice invoice) {
        try {
            saleInvoiceSolrComponent.index(invoice);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isSaleInvoiceExists(String invNumber) {
        List<EdsBaseSaleInvoice> existingInvoices = invoiceManager.getSaleInvoiceByNumberGlobal(invNumber);
        return existingInvoices != null && existingInvoices.size() > 0;
    }

    private boolean isPurchaseInvoiceExists(String piNumber) {
        List<EdsPurchaseInvoice> existingInvoices = invoiceManager.getPurchaseInvoiceByNumberGlobal(piNumber);
        return existingInvoices != null && existingInvoices.size() > 0;
    }

    private boolean isSalesOrder(EdsSaleQuote quote) {
        String pickedStatusId = referenceManager.findReference(INVOICE_STATUS, PICKED).getCode();
        String packedStatusId = referenceManager.findReference(INVOICE_STATUS, PACKED).getCode();
        String shippedStatusId = referenceManager.findReference(INVOICE_STATUS, SHIPPED).getCode();
        String saleOrderStatusId = referenceManager.findReference(INVOICE_STATUS, SALE_ORDER).getCode();
        return (quote.getStatus() != null && (pickedStatusId.equals(quote.getStatus().getCode())
                || packedStatusId.equals(quote.getStatus().getCode())
                || shippedStatusId.equals(quote.getStatus().getCode())
                || saleOrderStatusId.equals(quote.getStatus().getCode())))
                || quote.isSalesOrder();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getInvoiceByNumber(String invNumber, String type) {
        ArrayList<NewInvoice> list = new ArrayList<>();
        int listCount = 0;
        if (INVOICE.equals(type) || CREDIT_NOTE.equals(type)) {
            List<EdsBaseSaleInvoice> existingInvoices = invoiceManager.getSaleInvoiceByNumber(invNumber, null);
            listCount = existingInvoices.size();
            for (EdsBaseSaleInvoice invoice : existingInvoices) {
                if (INVOICE.equals(type)) {
                    list.add(getInvoice(invoice.getObjectID()));
                } else {
                    list.add(getCreditNote(invoice.getObjectID()));
                }
            }
        } else if (PURCHASE_INVOICE.equals(type)) {
            List<EdsPurchaseInvoice> existingInvoices = invoiceManager.getPurchaseInvoiceByNumber(invNumber, null, null);
            listCount = existingInvoices.size();
            for (EdsPurchaseInvoice invoice : existingInvoices) {
                list.add(getInvoice(invoice.getObjectID()));
            }
        } else if (SALE_QUOTE.equals(type) || SALE_ORDER.equals(type)) {
            List<EdsSaleQuote> existingQuotes = quoteManager.getSalesQuoteByNumber(invNumber, null, SALE_ORDER.equals(type));
            listCount = existingQuotes.size();
            for (EdsSaleQuote quote : existingQuotes) {
                list.add(invoiceCircularResolver.getQuote(quote.getObjectID(), null));
            }
        } else if (PURCHASE_ORDER.equals(type)) {
            List<EdsPurchaseOrder> existingOrders = quoteManager.getPurchaseOrderByNumber(invNumber, null);
            listCount = existingOrders.size();
            for (EdsPurchaseOrder purchaseOrder : existingOrders) {
                list.add(invoiceCircularResolver.getQuote(purchaseOrder.getObjectID(), null));
            }
        }
        return new InvoiceList(list, listCount);
    }

    @Override
    public List<CompanyCustomFieldItem> getInvoiceCustomFields(Integer entityId, ViewName viewName) {
        EdsInvoice invoice = invoiceManager.get(entityId);
        EdsCustomFields edsCustomFields = invoice.getCustomFields();
        return CustomFieldsUtils.setRPCCustomFieldItems(edsCustomFields, commonServiceLocal.getCompanyCustomFields(viewName));
    }

    @Override
    public void createInvoiceCustomFields(Integer entityId, List<CompanyCustomFieldItem> customFieldTO) {
        EdsInvoice invoice = invoiceManager.get(entityId);
        invoice.setCustomFields(createInvoiceCustomFields(customFieldTO));
        invoice.setUpdatedDate(new Date());
        invoiceManager.update(invoice);
        try {
            if (invoice instanceof EdsSaleInvoice) {
                saleInvoiceSolrComponent.index((EdsSaleInvoice) invoice);
            } else if (invoice instanceof EdsPurchaseInvoice) {
                purchaseInvoiceSolrComponent.index((EdsPurchaseInvoice) invoice);
            }
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void batchImportInvoicesFromFile(Integer fileID) {
        EdsImportFile edsImportFile = new EdsImportFile();
        edsImportFile.setFileID(fileID);
        edsImportFile.setType(ImportTypeEnum.BATCH_SALES_INVOICE);
        edsImportFile.setStatus(ImportStatusEnum.IN_PROCESS);
        edsImportFile.setOwner(userManager.getUser());
        importFileManager.createOrUpdate(edsImportFile);
        baseEventPostProcessor.registerEvent(ImportCustomEventListenerImpl.TYPE, ImportCustomEventListenerImpl.EVENT_IMPORT_BATCH_INVOICE, edsImportFile, employeeManager.getUser());
    }

    private void updateRelatedProjectFromSolr(EdsProject oldProject, EdsProject newProject, EdsCompany company) {
        if (company == null && invoiceManager.getUser() != null) {
            company = invoiceManager.getUser().getCompany();
        }
        if (company != null) {
            if (oldProject != null) {
                try {
                    projectSolrComponent.index(oldProject);
                } catch (SolrServerException | IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (newProject != null) {
                try {
                    projectSolrComponent.index(newProject);
                } catch (SolrServerException | IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Integer createInvoiceNoteAndHistory(Integer invoiceOrQuoteID, String viewType, HistoryListItem
            historyItem, Boolean isInvoice) {
        if (invoiceOrQuoteID != null) {
            EdsInvoiceQuoteNote note = new EdsInvoiceQuoteNote();
            if (isInvoice) {
                EdsInvoice invoice = invoiceManager.get(invoiceOrQuoteID);
                note.setComment(historyItem.getComment());
                note.setCommentator(invoiceManager.getUser());
                note.setInvoice(invoice);
                note.setDate(new Date());
            } else {
                EdsQuote quote = quoteManager.get(invoiceOrQuoteID);
                note.setComment(historyItem.getComment());
                note.setCommentator(invoiceManager.getUser());
                note.setQuote(quote);
                note.setDate(new Date());
            }
            note.setSuperUser(ServerUtils.isSuperUser());
            invoiceQuoteNoteManager.create(note);
            return note.getObjectID();
        }
        return null;
    }

    public Boolean removeInvoiceNoteAndHistory(Integer invoiceQuoteNoteID) {
        if (invoiceQuoteNoteID != null) {
            EdsInvoiceQuoteNote quoteNote = invoiceQuoteNoteManager.get(invoiceQuoteNoteID);
            if (quoteNote != null) {
                invoiceQuoteNoteManager.delete(quoteNote);
            }
            return true;
        }
        return false;
    }

    @Override
    public CrmAccountItem getCustomerEmailToSend(Integer customerID) {
        CrmAccountItem customer = null;
        if (customerID != null) {
            EdsCrmAccount clintOrSupplier = crmAccountManager.get(customerID);
            customer = new CrmAccountItem();
            customer.setObjectId(clintOrSupplier.getObjectID());
            customer.setName(clintOrSupplier.getName());
            customer.setEmail(clintOrSupplier.getEmail());
        }
        return customer;
    }

    private void createInvoiceNoteAndHistory(NewInvoice data, EdsInvoice invoice) {
        HistoryListItem[] noteItems = data.getHistoryList();
        List<EdsInvoiceQuoteNote> invoiceNotes = invoiceQuoteNoteManager.getInvoiceNotes(invoice.getObjectID());
        if (noteItems != null && noteItems.length > 0) {
            HashMap<Integer, Integer> existingNotesMap = new HashMap<>();
            for (HistoryListItem noteItem : noteItems) {
                if (noteItem.getObjectID() == null && noteItem.getComment() != null && !"".equals(noteItem.getComment())) {
                    EdsInvoiceQuoteNote note = new EdsInvoiceQuoteNote();
                    note.setComment(noteItem.getComment());
                    note.setCommentator(invoiceManager.getUser());
                    note.setSuperUser(ServerUtils.isSuperUser());
                    note.setInvoice(invoice);
                    note.setDate(new Date());
                    invoiceQuoteNoteManager.create(note);
                }
                if (noteItem.getObjectID() != null) {
                    existingNotesMap.put(noteItem.getObjectID(), noteItem.getObjectID());
                }
            }

            for (EdsInvoiceQuoteNote quoteNote : invoiceNotes) {
                if (!existingNotesMap.containsKey(quoteNote.getObjectID())) {
                    invoiceQuoteNoteManager.delete(quoteNote);
                }
            }
        } else {
            for (EdsInvoiceQuoteNote noteForDelete : invoiceNotes) {
                invoiceQuoteNoteManager.delete(noteForDelete);
            }
        }
    }

    private void saveInvoiceRecurringItem(NewInvoice data, Integer invoiceID) {
        EdsCompany company = recurrenceManager.getUser().getCompany();
        EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.RECURRING_INVOICE_REMINDER, invoiceID, company.getObjectID());
        data.getRecurrenceJobItem().setObjectId(recurrence != null ? recurrence.getObjectID() : null);
        data.getRecurrenceJobItem().setBusObjectId(invoiceID);
        data.getRecurrenceJobItem().setJobType(SchedulerConstant.RECURRING_INVOICE_REMINDER);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsRecurringInvoice.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        if (data.getID() != null) {
            kpiLog.setEntityId(data.getID());
        }
        ServerUtils.kpiLog(log, kpiLog, "Update Recurring Invoice");
        recurrenceService.saveRecurrenceJob(data.getRecurrenceJobItem());
    }

    private void saveBillRecurringItem(NewInvoice data, Integer invoiceID) {
        EdsCompany company = recurrenceManager.getUser().getCompany();
        EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.RECURRING_BILL_REMINDER, invoiceID, company.getObjectID());
        data.getRecurrenceJobItem().setObjectId(recurrence != null ? recurrence.getObjectID() : null);
        data.getRecurrenceJobItem().setBusObjectId(invoiceID);
        data.getRecurrenceJobItem().setJobType(SchedulerConstant.RECURRING_BILL_REMINDER);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsRecurringInvoice.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        if (data.getID() != null) {
            kpiLog.setEntityId(data.getID());
        }
        ServerUtils.kpiLog(log, kpiLog, "Update Recurring Bill");
        recurrenceService.saveRecurrenceJob(data.getRecurrenceJobItem());
    }

    private void updateProjectsInvoiceDate(NewInvoice data) {
        if (data.getProjectIDs() != null) {
            for (
                    int i = 0;
                    i < data.getProjectIDs().length;
                    i++) {
                if (data.getProjectIDs()[i] != null) {
                    projectManager.get(data.getProjectIDs()[i]).setLastInvoicedDate(data.getInvoiceDate().getNonConvertedDate());
                }
            }
        }
    }

    @Override
    public SaveResult savePurchaseInvoice(NewInvoice data) {
        EdsUser user = data.getUserID() != null ? userManager.get(data.getUserID()) : invoiceManager.getUser();
        boolean isRecurringBill = data.getRecurrenceJobItem() != null;
        SaveResult saveResult = new SaveResult();

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        boolean genrateNewNumber = false;
        try {
            genrateNewNumber = financialSettings.isGenerateNewNumber();
        } catch (Exception e) {
            genrateNewNumber = false;
        }
        data.setForceValidNumberGenerate(genrateNewNumber);
        data.setCalcScale(financialSettings.getCalculationScale());
        EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(user.getCompany());
        if (!ServerUtils.isNullOrEmpty(data.getInvoiceNumber())) {
            if (invSettings != null && !invSettings.getIsPurchaseInvoiceNumberingShow() && isPurchaseInvoiceExists(data.getInvoiceNumber())) {
                data.setInvoiceNumber(data.getInvoiceNumber());
            } else {
                List<EdsPurchaseInvoice> existingInvoices = invoiceManager.getPurchaseInvoiceByNumber(data.getInvoiceNumber(), data.getClientID(), null);
                if (existingInvoices != null && existingInvoices.size() > 0) {
                    saveResult.setInvoiceExist(true);
                    return saveResult;
                }
            }
        } else if (invSettings != null && invSettings.getIsPurchaseInvoiceNumberingShow()) {
            InvoiceNumberData numberData = getPurchaseInvoiceNumber(false);
            data.setNumberData(numberData);
            data.setInvoiceNumber(numberData.getInvoiceNumber());
        }

        EdsBasePurchaseInvoice invoice = (isRecurringBill ? new EdsRecurringBill() : new EdsPurchaseInvoice());

        if (data.getInvoiceTermsItem() != null && data.getInvoiceTermsItem().getId() != null) {
            invoice.setInvoiceTerms(invoiceTermsManager.get(data.getInvoiceTermsItem().getId()));
        }
        if (!isRecurringBill && data.getNumberData() != null && data.getNumberData().getFourDigitNumber() != null) {
            ((EdsPurchaseInvoice) invoice).setFourDigitNumber(Integer.valueOf(data.getNumberData().getFourDigitNumber()));
        }
        applyPurchaseInvoiceData(invoice, data);
        super.initInvoiceData(invoice, data);
        initTaxTotals(invoice, data.getTotalTaxItems());
        invoice.setTotalDiscount(data.getTotalDiscount());

        if (data.getConvertedItemID() != null) {
            invoice.setConvertedQuotes(Collections.singleton(quoteManager.get(data.getConvertedItemID())));
        }
        if (data.getTargetGrnId() != null) {
            final EdsShippingData shippingData = this.shippingDataManager.get(data.getTargetGrnId());

            if (shippingData != null && !shippingData.isDeleted() && shippingData.getQuote() != null) {
                invoice.setConvertedQuotes(Collections.singleton(shippingData.getQuote()));
            }
        }
        invoice.setFixedAssetRelated(data.isFixedAssetRelated());
        updateConvertedShippingData(invoice, data);

        Integer invoiceID = initInvoiceItemsForSave(data, invoice);
        saveResult.setId(invoiceID);
        saveResult.setNumber(invoice.getNumber());

        invoice.setCustomFields(createInvoiceCustomFields(data.getCustomFieldItems()));

        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, data.getID() != null ? BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT : BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, invoice, user);
        workflowEvent.setEntityType(RelationItem.TYPE_PURCHASE_INVOICE);

        if (isRecurringBill) {
            ((EdsRecurringBill) invoice).setSender(user);
            saveBillRecurringItem(data, saveResult.getId());
        } else {
            EdsPurchaseInvoice edsPurchaseInvoice = (EdsPurchaseInvoice) invoice;

            if (data.getOpportunityID() != null) {
                edsPurchaseInvoice.setOpportunityID(data.getOpportunityID());
            }
            if (APPROVE.equals(data.getStatusCode())) {
                updateItemUnitPriceOnPurchaseInvoiceApprove(edsPurchaseInvoice, data);
            }
            baseEventPostProcessor.registerEvent(PurchaseInvoiceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsPurchaseInvoice, user);

            if (edsPurchaseInvoice.getStatus() != null && SUBMITTED_TO_MANAGER.equals(edsPurchaseInvoice.getStatus().getCode())) {
                baseEventPostProcessor.registerEvent(PurchaseInvoiceEventListenerImpl.TYPE, PurchaseInvoiceEventListenerImpl.EVENT_PURCHASE_INVOICE_SUBMITTED_TO_MANAGER, edsPurchaseInvoice, user);
            }
            if (data.isFixedAssetRelated() && data.getConvertedItemID() == null && data.getTargetGrnId() == null) {
                try {
                    data.getFixedAssetItem().setPurchaseInvoiceID(invoice.getObjectID());
                    data.getFixedAssetItem().setFinancedByAccount(accountingManager.getMultiCurrencyAccount(EdsAccount.ACCOUNTS_PAYABLE, invoice.getCurrency()).createAccountItem());
                    saveResult.setFixedAssetID(fixedAssetService.saveFixedAssetData(data.getFixedAssetItem()));
                } catch (NumberExistingException e) {
                    e.printStackTrace();
                }
            }
            if (!isOk(data.getApprovers())) {
                EdsReference status;
                if (Constants.APPROVE.equals(data.getStatusCode())) {
                    status = hasInventoryItem(invoice) && !isConvertedToGdnGrn(invoice) ? referenceManager.findReference(Constants.INVOICE_STATUS, Constants.PENDING) :
                            referenceManager.findReference(Constants.INVOICE_STATUS, Constants.APPROVE);
                } else {
                    status = referenceManager.findReference(Constants.INVOICE_STATUS, data.getStatusCode());
                }
                invoice.setEntityStatus(status);
            }
            if (isOk(data.getApprovers())) {
                saveInvoiceApprovers(edsPurchaseInvoice, data.getApprovers(), data.getStatusCode(), Constants.APPROVE);
                invoiceManager.update(edsPurchaseInvoice);

                EdsBusinessEvent workflowApprovingEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), invoice, userManager.getUser());
                workflowApprovingEvent.setEntityType(edsPurchaseInvoice.isCreditNote() ? RelationItem.TYPE_DEBIT_NOTE : RelationItem.TYPE_PURCHASE_INVOICE);
            }

            if (data.isRelationChanged()) {
                this.allInOneServiceLocal.saveRelations(RelationItem.TYPE_PURCHASE_INVOICE, edsPurchaseInvoice.getObjectID(), edsPurchaseInvoice.getNumber(), data.getRelations());
            }

            List<EdsShippingData> convertedShippingData = shippingDataManager.getGrnGdnsByInvoiceId(edsPurchaseInvoice.getObjectID());

            try {
                purchaseInvoiceSolrComponent.index(edsPurchaseInvoice);
                if (convertedShippingData != null && !convertedShippingData.isEmpty()) {
                    shippingDataSolrComponent.indexes(convertedShippingData);
                }
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
            updateConvertedPurchaseInvoiceData(edsPurchaseInvoice, "savePI");
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPurchaseInvoice.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        kpiLog.setEntityId(invoice.getObjectID());
        ServerUtils.kpiLog(log, kpiLog, (isRecurringBill ? "Add Recurring Bill" : "Add Purchase Invoice"));

        return saveResult;
    }

    protected boolean hasInventoryItem(EdsInvoice invoice) {
        WarehouseManager warehouseManager = StaticContextAccessor.getBean(WarehouseManager.class);
        HashBasedTable<Integer, Integer, BigDecimal> itemsMap = HashBasedTable.create();
        for (EdsInvoiceItem invoiceItem : invoice.getInvoiceItems()) {
            Integer warehouseId = invoiceItem.getWarehouse() != null ? invoiceItem.getWarehouse().getObjectID() : warehouseManager.getDefaultWarehouse().getObjectID();
            COGSService.mapRequestedItems(invoiceItem.getItem(), invoiceItem.getQty(), warehouseId, itemsMap);
            if (!itemsMap.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    protected boolean isConvertedToGdnGrn(EdsInvoice invoice) {
        return !invoice.getConvertedShippingData().isEmpty();
    }

    @Override
    public void saveInvoiceApprovers(EdsApprovable edsApprovable,
                                     ArrayList<ApproverItemMini> approvers,
                                     String statusCode,
                                     String approveStatusCode) {
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
                    edsApprovable.getCurrentApprover().setStatus(referenceManager.findReference(Constants.INVOICE_STATUS, statusCode));
                    edsApprovable.setEntityStatus(referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
                    isFirstApprover = false;
                } else if (edsApprovable.getCurrentApprover() != null && statusCode != null) {
                    edsApprovable.getCurrentApprover().setStatus(referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
                }
                if (statusCode != null && !approveStatusCode.equals(statusCode)) {
                    edsApprovable.setEntityStatus(referenceManager.findReference(Constants.INVOICE_STATUS, statusCode));
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
                if (edsApprovable instanceof EdsSaleQuote && Constants.SALE_ORDER.equals(statusCode)) {
                    edsApprovable.setEntityStatus(referenceManager.findReference(Constants.INVOICE_STATUS, statusCode));
                    edsApprover.setStatus(referenceManager.findReference(Constants.INVOICE_STATUS, statusCode));
                } else {
                    edsApprover.setStatus(referenceManager.findReference(Constants.INVOICE_STATUS, statusCode));
                    if (Constants.DRAFT.equals(statusCode)) {
                        edsApprovable.setEntityStatus(referenceManager.findReference(Constants.INVOICE_STATUS, statusCode));
                    } else {
                        edsApprovable.setEntityStatus(referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
                    }
                }
                isFirstApprover = false;
            } else if (statusCode != null) {
                edsApprover.setStatus(referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
                edsApprovable.setEntityStatus(referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
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

    private void initTaxTotals(EdsInvoice invoice, TotalTaxItem[] totalTaxItems) {
        if (invoice.getObjectID() != null) {
            invoiceManager.deleteInvoiceOldTaxTotals(invoice);
        }
        if (totalTaxItems != null) {
            List<EdsInvoiceTaxTotal> totalTaxes = new LinkedList<>();
            for (TotalTaxItem item : totalTaxItems) {
                EdsInvoiceTaxTotal totalTax = new EdsInvoiceTaxTotal();
                totalTax.setInvoice(invoice);
                totalTax.setVat(vatManager.get(item.getTaxItem().getId()));
                totalTax.setAmount(item.getTaxAmount());
                totalTaxes.add(totalTax);
            }
            invoice.setInvoiceTaxTotals(totalTaxes);
        }
    }

    private Integer initInvoiceItemsForSave(NewInvoice data, EdsInvoice invoice) {
        EdsUser user = data.getUserID() != null ? userManager.get(data.getUserID()) : userManager.getUser();
        Integer calculationScale = 2;
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        try {
            calculationScale = financialSettings.getAccountingCalculationScale();
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean isMultipleWarehouseEnabled = financialSettings.getEnableMultiWarehouse();
        EdsWarehouse defaultWarehouse = warehouseManager.getDefaultWarehouse();

        boolean isRecurringInvoice = data.isRecurringInvoice();

        invoiceManager.create(invoice);

        List<EdsInvoiceItem> items = new ArrayList<>();
        for (NewInvoiceItem newItem : data.getItems()) {
            EdsInvoiceItem localItem = new EdsInvoiceItem();

            if (!isMultipleWarehouseEnabled) {
                localItem.setWarehouse(defaultWarehouse);
            }
            super.initInvoiceItemData(localItem, newItem);
            localItem.setExpenceItemId(newItem.getExpanceItemId());
            localItem.setQuoteItemId(newItem.getQuoteItemId());
            localItem.setFromTimesheet(newItem.isFromTimesheet());
            Optional.ofNullable(newItem.getFromDate()).ifPresent(fd -> localItem.setFromDate(fd.getNonConvertedDate()));
            Optional.ofNullable(newItem.getToDate()).ifPresent(td -> localItem.setToDate(td.getNonConvertedDate()));
            localItem.setFaiCategoryId(newItem.getFaiCategoryId());

            ViewName customFieldType = null;
            if (invoice instanceof EdsPurchaseInvoice) {
                customFieldType = ViewName.PurchaseInvoiceItem;
            } else if (invoice instanceof EdsSaleInvoice) {
                customFieldType = ViewName.SaleInvoiceItem;
            }
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.INVOICE_ITEM_TABLE_POPULATE_WITH_CF) && newItem.getItemID() != null && productService.getProductBaseData(newItem.getItemID()) != null) {
                populateItemTableCfValue(newItem, localItem, customFieldType);
            }


            if (data.isProgressInvoicing()) {
                List<CompanyCustomFieldItem> saleInvoiceItemCustomFieldsItems = commonService.getCompanyAllCustomFields(ViewName.SaleInvoiceItem);

                List<CompanyCustomFieldItem> siItemCustomFields = new ArrayList<>();

                if (saleInvoiceItemCustomFieldsItems != null && saleInvoiceItemCustomFieldsItems.size() > 0
                        && newItem.getCustomFieldItems() != null && newItem.getCustomFieldItems().size() > 0) {
                    for (CompanyCustomFieldItem si : saleInvoiceItemCustomFieldsItems) {
                        si.setObjectId(null);
                        for (CompanyCustomFieldItem sq : newItem.getCustomFieldItems()) {
                            if (si.getDataType().equals(sq.getDataType())
                                    && si.getUiType().equals(sq.getUiType())
                                    && si.getAliasName().equals(sq.getAliasName())) {
                                si.setPredefinedValues(sq.getPredefinedValues());
                                si.setPredefinedValuesWithSorting(sq.getPredefinedValuesWithSorting());
                                si.setQuery(sq.getQuery());
                                si.setQueryItems(sq.getQueryItems());
                                si.setFieldStringValue(sq.getFieldStringValue());
                                si.setFieldDateNonConvertedValue(sq.getFieldDateNonConvertedValue());
                                si.setAttachments(sq.getAttachments());
                                si.setLookUpTypeEnum(sq.getLookUpTypeEnum());
                                si.setSelectedId(sq.getSelectedId());
                                si.setDefaultValue(sq.getDefaultValue());
                                si.setPrefix(sq.getPrefix());
                                si.setItem(sq.getItem());
                                si.setSelectItems(sq.getSelectItems());
                            }
                        }
                        siItemCustomFields.add(si);
                    }
                }

                localItem.setCustomFields(createInvoiceItemCustomFields(siItemCustomFields));
            } else {
                localItem.setCustomFields(createInvoiceItemCustomFields(newItem.getCustomFieldItems()));
            }


            if (invoice instanceof EdsPurchaseInvoice && newItem.getClient() != null) {
                localItem.setClient(crmAccountManager.get(newItem.getClient().getId()));
            }

            invoiceItemManager.create(localItem);

            if (newItem.getProjectBasedEntryIds() != null) {
                for (int i = 0; i < newItem.getProjectBasedEntryIds().length; i++) {
                    if (newItem.getProjectBasedEntryIds()[i] != null) {
                        timeSheetManager.get(newItem.getProjectBasedEntryIds()[i]).setUsedInInvoice(true);
                        timeSheetManager.get(newItem.getProjectBasedEntryIds()[i]).setInvoiceItemID(localItem.getObjectID());
                    }
                }
            }

            localItem.setSerials(newItem.getSerials());
            localItem.setAssignedSerials(newItem.getAssignedSerials());
            EdsItem item = newItem.getItemID() != null ? itemManager.get(newItem.getItemID()) : null;

            if (item != null && data.getRentalOrderId() != null) {
                updateRentItemStatus(item);
            }

            if (item != null && item.getTrackBatchesEnabled()) {

                if (invoice instanceof EdsPurchaseInvoice) {
                    if (data.getTargetGrnId() != null || data.getConvertedItemID() != null) { //GRN convert to PI or PO convert to PI
                        itemBatchService.createBatchForConvertedPurchaseInvoice(invoice.getObjectID(), newItem, localItem.getObjectID());
                    } else {
                        itemBatchService.createBatchForPurchaseInvoice(invoice.getObjectID(), newItem, localItem.getObjectID());
                    }
                } else if (invoice instanceof EdsSaleInvoice) {
                    if (data.getTargetGrnId() != null || data.getConvertedItemID() != null) { //GDN convert to SI or SO conver to SI
                        itemBatchService.createBatchForConvertedSaleInvoice(invoice.getObjectID(), newItem, localItem.getObjectID());
                    } else {
                        itemBatchService.createBatchForSaleInvoice(invoice.getObjectID(), newItem, localItem.getObjectID());
                    }
                }
            }
            items.add(localItem);
            localItem.setInvoice(invoice);
        }
        invoice.setInvoiceItems(items);

        //Attachments
        if (invoice instanceof EdsPurchaseInvoice || invoice instanceof EdsSaleInvoice) {
            if (data.getAttachments() != null && data.getAttachments().length > 0) {
                attachmentUtilsManager.saveAttachments(invoice instanceof EdsPurchaseInvoice ? F_PUR_INV : F_SALE_INV, invoice.getObjectID(), invoice.getObjectID(), data.getAttachments());
            }
            if (invoice.getConvertedQuotes() != null && invoice.getConvertedQuotes().size() != 0) {
                for (EdsQuote quote : invoice.getConvertedQuotes()) {
                    List<FileResource> attachments = attachmentUtilsManager.getAttachments(invoice instanceof EdsPurchaseInvoice ? F_PUR_ORDER : F_SALE_QUOTE, quote.getObjectID(), quote.getObjectID());
                    for (FileResource file : attachments) {
                        attachmentUtilsManager.copyFileWhenConvert(invoice instanceof EdsPurchaseInvoice ? F_PUR_INV : F_SALE_INV, file.getFolderId(), file.getObjectId(), invoice.getObjectID(), file);
                    }
                }
            }
        }

        applyExpensesToInvoice(data, invoice);

        if (!isRecurringInvoice) {
            if (data.isBookkeep() && !isOk(data.getApprovers()) && (APPROVE.equals(data.getStatusCode()) || OPEN.equals(data.getStatusCode()))) {
                this.createInvoiceTransactionsAndCalculateProjectBugdet(invoice, user);
            }
            registerInterCompanySalesTransaction(data, invoice, user);
        }
        createInvoiceNoteAndHistory(data, invoice);

        if (invoice instanceof EdsSaleInvoice) {
            updateSaleInvoiceItemsProductSerials(invoice, null);
        }

        if (!data.isRecurringInvoice() && BigDecimal.ZERO.compareTo(invoice.getTotalInInvoiceCurrency().setScale(calculationScale, RoundingMode.HALF_UP)) == 0 && (APPROVE.equals(invoice.getStatus().getCode()) || OPEN.equals(invoice.getStatus().getCode()))) {
            invoice.setStatus(referenceManager.findReference(INVOICE_STATUS, PAID));
        }
        invoiceManager.update(invoice);
        return invoice.getObjectID();
    }

    private void updateRentItemStatus(EdsItem item) {
        item.setRentStatus(referenceManager.getByCode(RENT_ITEMS.OCCUPIED));
        itemManager.update(item);
        try {
            productsServicesSolrComponent.indexes(List.of(item));
        } catch (Exception ignored) {
        }
    }

    private void populateItemTableCfValue(NewInvoiceItem newItem, EdsInvoiceItem localItem, ViewName
            customFieldType) {
        ArrayList<CompanyCustomFieldItem> productCustomFields = productService.getProductBaseData(newItem.getItemID()).getProductCustomFieldItems();

        if (productCustomFields != null && productCustomFields.size() > 0) {
            setValueStaticFieldFromCfByAliasName(localItem, productCustomFields);

            ArrayList<CompanyCustomFieldItem> invoiceItemCustomFields = null;
            if (newItem.getCustomFieldItems() != null && newItem.getCustomFieldItems().size() > 0) {
                invoiceItemCustomFields = (ArrayList<CompanyCustomFieldItem>) newItem.getCustomFieldItems();
            } else {
                invoiceItemCustomFields = new ArrayList<>();
            }
            ArrayList<CompanyCustomFieldItem> invoiceAllItemCustomFields = commonService.getCompanyAllCustomFields(customFieldType);
            if (invoiceAllItemCustomFields != null && !invoiceAllItemCustomFields.isEmpty()) {
                for (CompanyCustomFieldItem companyCustomFieldItem : invoiceAllItemCustomFields) {
                    if (invoiceItemCustomFields.contains(companyCustomFieldItem)) {
                        continue;
                    }
                    companyCustomFieldItem.setObjectId(null);
                    invoiceItemCustomFields.add(companyCustomFieldItem);
                }
            }

            if (invoiceItemCustomFields != null && invoiceItemCustomFields.size() > 0) {
                for (CompanyCustomFieldItem invoiceCF : invoiceItemCustomFields) {
                    for (CompanyCustomFieldItem productCF : productCustomFields) {
                        if (invoiceCF.getDataType().equals(productCF.getDataType())
                                && invoiceCF.getUiType().equals(productCF.getUiType())
                                && invoiceCF.getAliasName().equals(productCF.getAliasName())
                                && (invoiceCF.getFieldStringValue() == null || (invoiceCF.getFieldStringValue() != null && invoiceCF.getFieldStringValue().length() == 0))) {
                            invoiceCF.setPredefinedValues(productCF.getPredefinedValues());
                            invoiceCF.setPredefinedValuesWithSorting(productCF.getPredefinedValuesWithSorting());
                            invoiceCF.setQuery(productCF.getQuery());
                            invoiceCF.setQueryItems(productCF.getQueryItems());
                            invoiceCF.setFieldStringValue(productCF.getFieldStringValue());
                            invoiceCF.setFieldDateNonConvertedValue(productCF.getFieldDateNonConvertedValue());
                            invoiceCF.setAttachments(productCF.getAttachments());
                            invoiceCF.setLookUpTypeEnum(productCF.getLookUpTypeEnum());
                            invoiceCF.setSelectedId(productCF.getSelectedId());
                            invoiceCF.setDefaultValue(productCF.getDefaultValue());
                            invoiceCF.setPrefix(productCF.getPrefix());
                            invoiceCF.setItem(productCF.getItem());
                            invoiceCF.setSelectItems(productCF.getSelectItems());
                        }
                    }
                }
            }
            newItem.setCustomFieldItems(invoiceItemCustomFields);
        }
    }

    @Override
    public void createInvoiceTransactionsAndCalculateProjectBugdet(EdsInvoice edsInvoice, EdsUser user) {
        Integer transactionId = edsInvoice.isCreditNote() ? accountingServiceLocal.createTransactionsForCreditNote(edsInvoice, null) :
                accountingServiceLocal.createTransactionsForInvoice(edsInvoice, user);//accounting entry

        //this piece of code need to calculate the project budget
        if (edsInvoice.getRelatedProject() != null || genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            if (edsInvoice instanceof EdsSaleInvoice) {
                EdsBusinessEvent event = baseEventPostProcessor.registerEvent(ProjectBudgetCustomEventListenerImpl.TYPE, ProjectBudgetCustomEventListenerImpl.SALE_INVOICE_APPROVE, edsInvoice, null);
                event.setCustomStringField(edsInvoice.getRelatedProject() != null ? edsInvoice.getRelatedProject().getObjectID().toString() : "");
            } else if (edsInvoice instanceof EdsPurchaseInvoice) {
                EdsBusinessEvent event = baseEventPostProcessor.registerEvent(ProjectBudgetCustomEventListenerImpl.TYPE, ProjectBudgetCustomEventListenerImpl.PURCHASE_INVOICE_APPROVE, edsInvoice, null);
                event.setCustomStringField(edsInvoice.getRelatedProject() != null ? edsInvoice.getRelatedProject().getObjectID().toString() : "");
            }
        }
        if (transactionId != null) {
            boolean hasDeferredTransactionItem = false;
            for (EdsInvoiceItem edsInvoiceItem : edsInvoice.getInvoiceItems()) {
                if (edsInvoiceItem.getItem() != null && edsInvoiceItem.getItem().getInventoryTrackingEnabled()) {
                    if (edsInvoice instanceof EdsPurchaseInvoice) {
                        itemSerialService.createForPurchaseInvoice(edsInvoiceItem, transactionId);
                    } else if (edsInvoice instanceof EdsSaleInvoice) {
                        itemSerialService.assignForSalesInvoice(edsInvoiceItem, transactionId);
                    }
                }

                if (edsInvoiceItem.isDeferredTransasctionItem()) {
                    hasDeferredTransactionItem = true;
                }
            }
            if (hasDeferredTransactionItem) {
                baseEventPostProcessor.registerEvent(DeferredTransactionCustomEventListenerImpl.TYPE, DeferredTransactionCustomEventListenerImpl.EVENT_INVOICE_DEFERRED_TRANSACTION, edsInvoice, user);
            }
        }
    }


    private void setValueStaticFieldFromCfByAliasName(EdsInvoiceItem
                                                              invoiceItem, ArrayList<CompanyCustomFieldItem> productCustomFieldItems) {
        for (CompanyCustomFieldItem productCFItem : productCustomFieldItems) {
            if (productCFItem != null && productCFItem.getAliasName() != null) {
                switch (productCFItem.getAliasName()) {
                    case ItemTableConstants.DESCRIPTION -> {
                        if ((invoiceItem.getDescription() == null || invoiceItem.getDescription() != null && invoiceItem.getDescription().length() == 0) &&
                                productCFItem.getFieldStringValue() != null && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType()))) {
                            invoiceItem.setDescription(productCFItem.getFieldStringValue());
                        }
                    }
                    case ItemTableConstants.QTY -> {
                        if (invoiceItem.getQty() == null &&
                                productCFItem.getFieldStringValue() != null && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            invoiceItem.setQty(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                    }
                    case ItemTableConstants.MEASUREMENT -> {
                        if (invoiceItem.getUnitMeasurement() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.UNIT_MEASUREMENT.equals(productCFItem.getLookUpTypeEnum())) {
                            invoiceItem.setUnitMeasurement(unitMeasurementManager.get(productCFItem.getSelectedId()));
                        }
                    }
                    case ItemTableConstants.UNITPRICE -> {
                        if (invoiceItem.getUnitPrice() == null &&
                                productCFItem.getFieldStringValue() != null && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            invoiceItem.setUnitPrice(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                    }
                    case ItemTableConstants.DISCOUNT_AMT -> {
                        if (invoiceItem.getDiscountAmount() == null &&
                                productCFItem.getFieldStringValue() != null && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            invoiceItem.setDiscountAmount(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                    }
                    case ItemTableConstants.PROJECT -> {
                        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE) && invoiceItem.getProject() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.PROJECT.equals(productCFItem.getLookUpTypeEnum())) {
                            invoiceItem.setProject(projectManager.get(productCFItem.getSelectedId()));
                        }
                    }
                }
            }
        }
    }

    private void updateSaleInvoiceItemsProductSerials(EdsInvoice invoice, List<Integer> invoiceItemsDeleted) {
        List<Integer> oldSerialItems = null;
        if (invoiceItemsDeleted != null) {
            oldSerialItems = productSerialManager.getProductSerialsBySalesInvoiceItems(invoiceItemsDeleted);
        }

        List<EdsInvoiceItem> invoiceItems = invoice.getInvoiceItems();
        HashMap<Integer, Integer> existingSerials = new HashMap<>();
        for (EdsInvoiceItem item : invoiceItems) {
            if (item.getAssignedSerials() != null && item.getAssignedSerials().length > 0) {
                boolean isAlmadarSerials = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ALMADAR_PRODUCT_SERIAL_ENABLED);
                if (isAlmadarSerials) {
                    for (ProductSerialItem assignedSerial : item.getAssignedSerials()) {
                        EdsProductSerial edsProductSerial = productSerialManager.get(assignedSerial.getObjectID());
                        List<EdsProductSerial> productSerialItems = productSerialManager.getProductSerialsByCount(item.getItem().getObjectID(), edsProductSerial, assignedSerial.getQty().intValue());
                        if (productSerialItems != null) {
                            for (EdsProductSerial productSerial : productSerialItems) {
                                productSerial.setInvoiceItemID(item.getObjectID());
                                productSerialManager.update(productSerial);
                                existingSerials.put(productSerial.getObjectID(), productSerial.getObjectID());
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < item.getAssignedSerials().length; j++) {
                        EdsProductSerial serial;
                        if (item.getAssignedSerials()[j].getObjectID() != null) {
                            serial = productSerialManager.get(item.getAssignedSerials()[j].getObjectID());
                            serial.setInvoiceItemID(item.getObjectID());
                            productSerialManager.update(serial);
                            existingSerials.put(serial.getObjectID(), serial.getObjectID());
                        }
                    }
                }
            }
        }

        if (oldSerialItems != null) {
            for (Integer id : oldSerialItems) {
                if (!existingSerials.containsKey(id)) {
                    EdsProductSerial ps = productSerialManager.get(id);
                    ps.setInvoiceItemID(null);
                    productSerialManager.update(ps);
                }
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewInvoice getInvoiceSummaryData(Integer id) {
        NewInvoice invoiceObject = getInvoice(id);
        String type = RECEIVABLE.equals(invoiceObject.getType()) ? SALE_INVOICE : PURCHASE_INVOICE;
        String layout = PathFinder.getLayoutHTML(type);
        invoiceObject.setLayoutHTML(layout);
        Set<GenericSettingsEnum> genericSettings = genericSettingsManager.getEnabledGenericSettings();
        invoiceObject.setRoundingModeDisabled(genericSettings.contains(GenericSettingsEnum.ROUNDING_MODE_DISABLED));
        invoiceObject.setDoubleTaxEnabled(genericSettings.contains(GenericSettingsEnum.DOUBLE_TAX_ENABLED));
        invoiceObject.setCustomExcelEnabled(genericSettings.contains(GenericSettingsEnum.GENERATE_CUSTOM_EXCEL_ENABLED));
        invoiceObject.setRevisionHistoryEnabled(genericSettings.contains(GenericSettingsEnum.REVISION_HISTORY_ENABLED));
        invoiceObject.setCustomItemColumns(itemTableSettingsServiceLocal.getColumnConfigs(RECEIVABLE.equals(invoiceObject.getType()) ? ItemTableEnum.SALE_INVOICE_ITEM : ItemTableEnum.PURCHASE_INVOICE_ITEM, false, true));

        final List<FileResource> attachments = this.attachmentUtilsManager.getAttachments(Constants.F_SALE_INV, invoiceObject.getID(), invoiceObject.getID());
        attachments.stream()
                .filter(file -> file.getFileName().contains("Approved_By_") && file.getFileName().endsWith(".pdf"))
                .findFirst()
                .ifPresent(file -> invoiceObject.setAmazonLink(file.getAmazonLink()));

        List<EdsShippingData> convertedShippingDataList = shippingDataManager.getGrnGdnsByInvoiceId(id);
        if (convertedShippingDataList != null && !convertedShippingDataList.isEmpty()) {
            ArrayList<ShippingData> shippingDataList = new ArrayList<>();
            for (EdsShippingData edsShippingData : convertedShippingDataList) {
                ShippingData shippingData = edsShippingData.toTO();
                Integer invoiceId = shippingDataManager.getGrnGdnRelatedInvoiceNumber(shippingData.getId());
                if (invoiceId != null) {
                    EdsInvoice invoice = invoiceManager.get(invoiceId);
                    NewInvoice to = EdsInvoice.getInvoiceData(invoice);
                    shippingData.setInvoice(to);
                }
                shippingDataList.add(shippingData);
            }
            invoiceObject.setConvertedShippingDataList(shippingDataList);
        }


        if (RECEIVABLE.equals(invoiceObject.getType())) {
            invoiceObject.getTypeItem().setSupplierCustomerBalance(crmAccountManager.getClientBalance(invoiceObject.getClientID()).doubleValue());
        } else {
            EdsCrmAccount clientBase = crmAccountManager.get(invoiceObject.getClientID());
            invoiceObject.getTypeItem().setSupplierCustomerBalance(crmAccountManager.getSupplierBalance(clientBase.getObjectID()).doubleValue());
        }
        if (invoiceObject.isRevisionHistoryEnabled()) {
            invoiceObject.setRevisionHistoryItems(invoiceManager.getRevisionHistory(id, RECEIVABLE.equals(invoiceObject.getType()) ? SALE_INVOICE : PURCHASE_INVOICE));
        }
        if (invoiceObject.getClientItem() != null && invoiceObject.getClientItem().getMailAddressID() != null) {
            EdsAddress mailAddress = addressManager.get(invoiceObject.getClientItem().getMailAddressID());
            if (mailAddress != null) {
                invoiceObject.getClientItem().setDropShipToMailAddressHTML(mailAddress.getAddressDataAsHTML());
            }
        }

        if (invoiceObject.isProjectBasedInvoice()) {
            invoiceObject.setPdfTemplateList(getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PROJECT_BASED_INVOICE.name()));
        } else if (PAYABLE.equals(invoiceObject.getType())) {
            invoiceObject.setPdfTemplateList(this.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PURCHASE_INVOICE.name()));
        } else {
            invoiceObject.setPdfTemplateList(this.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.SALES_INVOICE.name()));
            invoiceObject.setHtmlTemplateList(this.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.SALES_INVOICE.name(), true));
            if (invoiceObject.getQuoteNumber() != null) {
                List<EdsSaleQuote> quoteList = quoteManager.getQuoteByNumber(invoiceObject.getQuoteNumber());
                if (quoteList != null && !quoteList.isEmpty()) {
                    invoiceObject.setQuoteId(quoteList.get(0) != null ? quoteList.get(0).getObjectID() : null);
                    invoiceObject.setSalesOrder(quoteList.get(0) != null ? quoteList.get(0).isSalesOrder() : null);
                }
            }
        }
        invoiceObject.setPaymentMethods(paymentMethodManager.list()
                .stream()
                .map(paymentMethod -> new SelectItem(paymentMethod.getObjectID(), paymentMethod.getName()))
                .collect(Collectors.toCollection(ArrayList::new)));

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(id);
        if (RECEIVABLE.equals(invoiceObject.getType())) {
            if (invoiceObject.isRecurringInvoice()) {
                kpiLog.setEntityName(EdsRecurringInvoice.class.getSimpleName());
                ServerUtils.kpiLog(log, kpiLog, "View Recurring Invoice");
            } else if (invoiceObject.isProjectBasedInvoice()) {
                kpiLog.setEntityName(EdsSaleInvoice.class.getSimpleName());
                ServerUtils.kpiLog(log, kpiLog, "View Project based Invoice");
            } else {
                kpiLog.setEntityName(EdsSaleInvoice.class.getSimpleName());
                ServerUtils.kpiLog(log, kpiLog, "View Sale Invoice");
            }
        } else {
            kpiLog.setEntityName(EdsPurchaseInvoice.class.getSimpleName());
            ServerUtils.kpiLog(log, kpiLog, "View Purchase Invoice");
        }
        return getDataForSummaryView(invoiceObject);
    }

    @Override
    public void changeInvoiceStatus(Integer objectId, String statusCode) {
        EdsSaleInvoice edsSaleInvoice = invoiceManager.getSaleInvoice(objectId);
        if (edsSaleInvoice != null) {
            EdsReference edsStatus = super.getInvoiceStatus(statusCode);
            if (!APPROVE.equals(edsStatus.getCode())) {
                edsSaleInvoice.setEntityStatus(edsStatus);
            }
            edsSaleInvoice.updateStatus(edsStatus);
            invoiceManager.update(edsSaleInvoice);
            addSaleInvoiceToSolr(edsSaleInvoice);

            EdsUser edsUser = userManager.getUser();
            if (APPROVE.equals(edsStatus.getCode())) {
                baseEventPostProcessor.registerEvent(SaleInvoiceEventListenerImpl.TYPE, SaleInvoiceEventListenerImpl.EVENT_SALES_INVOICE_MANAGER_APPROVE, edsSaleInvoice, edsUser);
            } else if (MANAGER_REJECT.equals(edsStatus.getCode())) {
                baseEventPostProcessor.registerEvent(SaleInvoiceEventListenerImpl.TYPE, SaleInvoiceEventListenerImpl.EVENT_SALES_INVOICE_MANAGER_REJECT, edsSaleInvoice, edsUser);
            }

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), edsSaleInvoice, edsUser);
            workflowEvent.setEntityType(edsSaleInvoice.isCreditNote() ? RelationItem.TYPE_CREDIT_NOTE : RelationItem.TYPE_SALEINVOICE);

            EdsBusinessEvent workflow = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsSaleInvoice, edsUser);
            workflow.setEntityType(edsSaleInvoice.isCreditNote() ? RelationItem.TYPE_CREDIT_NOTE : RelationItem.TYPE_SALEINVOICE);
            workflow.setCustomStringField(WorkflowActionDetectedEventListenerImpl.FROM_WORKFLOW);

        } else {
            EdsPurchaseInvoice purchaseInvoice = invoiceManager.getPurchaseInvoice(objectId);
            if (purchaseInvoice != null) {
                changePurchaseInvoiceStatus(purchaseInvoice.getObjectID(), statusCode);
            }
        }

    }

    @Override
    public void changePurchaseInvoiceStatus(Integer objectId, String statusCode) {
        EdsPurchaseInvoice edsPurchaseInvoice = invoiceManager.getPurchaseInvoice(objectId);
        if (edsPurchaseInvoice != null) {
            EdsReference edsStatus = super.getInvoiceStatus(statusCode);
            if (!APPROVE.equals(edsStatus.getCode())) {
                edsPurchaseInvoice.setEntityStatus(edsStatus);
            }
            edsPurchaseInvoice.updateStatus(edsStatus);
            invoiceManager.update(edsPurchaseInvoice);
            try {
                purchaseInvoiceSolrComponent.index(edsPurchaseInvoice);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
            EdsUser edsUser = userManager.getUser();
            if (APPROVE.equals(edsStatus.getCode())) {
                baseEventPostProcessor.registerEvent(PurchaseInvoiceEventListenerImpl.TYPE, PurchaseInvoiceEventListenerImpl.EVENT_PURCHASE_INVOICE_MANAGER_APPROVE, edsPurchaseInvoice, edsUser);
            } else if (MANAGER_REJECT.equals(edsStatus.getCode())) {
                baseEventPostProcessor.registerEvent(PurchaseInvoiceEventListenerImpl.TYPE, PurchaseInvoiceEventListenerImpl.EVENT_PURCHASE_INVOICE_MANAGER_REJECT, edsPurchaseInvoice, edsUser);
            }

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), edsPurchaseInvoice, edsUser);
            workflowEvent.setEntityType(edsPurchaseInvoice.isCreditNote() ? RelationItem.TYPE_DEBIT_NOTE : RelationItem.TYPE_PURCHASE_INVOICE);
        }
    }

    private NewInvoice getDataForSummaryView(NewInvoice data) {
        EdsAddress billAddress = addressManager.get(data.getBillAddressID());
        if (billAddress != null) {
            data.setBillAddressAsHTML(billAddress.getAddressDataAsHTML());
            data.setBillAddress(billAddress.getRPC());
        }
        EdsAddress mailAddress = addressManager.get(data.getMailAddressID());
        if (mailAddress != null && !mailAddress.getAddressDataAsHTML().isEmpty()) {
            data.setMailAddressAsHTML(mailAddress.getAddressDataAsHTML());
            data.setCompanyMailAddressAsHTML(mailAddress.getAddressDataAsHTML());
            data.setMailAddress(mailAddress.getRPC());
        } else if (userManager.getUser() != null && userManager.getUser().getCompany() != null &&
                userManager.getUser().getCompany().getMailingAddress() != null) {
            mailAddress = userManager.getUser().getCompany().getMailingAddress();
            data.setMailAddress(mailAddress.getRPC());
            data.setMailAddressAsHTML(mailAddress.getAddressDataAsHTML());
            data.setCompanyMailAddressAsHTML(data.getMailAddressAsHTML());
        }
        return data;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewInvoice getInvoice(Integer id) {
        EdsInvoice invoice = invoiceManager.get(id);

        ArrayList<CompanyCustomFieldItem> itemCustomFields = null;

        if (invoice instanceof EdsSaleInvoice) {
            itemCustomFields = commonService.getCompanyAllCustomFields(ViewName.SaleInvoiceItem);
        } else if (invoice instanceof EdsPurchaseInvoice) {
            itemCustomFields = commonService.getCompanyAllCustomFields(ViewName.PurchaseInvoiceItem);
        }
        invoice.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));

        return createInvoiceData(invoice);
    }

    @Override
    public CrmAccountItem getSaleInvoiceCustomer(Integer id) {
        CrmAccountItem accountItem = null;
        EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(id);
        if (saleInvoice != null) {
            EdsCrmAccount clientOrSupplier = saleInvoice.getClientOrSupplier();
            if (clientOrSupplier != null) {
                accountItem = clientOrSupplier.getRPC(new CrmAccountItem(), false);
            }
        }

        return accountItem;
    }

    private NewInvoice createInvoiceData(EdsInvoice invoice) {
        NewInvoice newInvoice = getInvoiceBaseData(invoice);
        if (invoice instanceof EdsBaseSaleInvoice) {
            EdsBaseSaleInvoice inv = getBaseSaleInvoiceData(invoice, newInvoice);
            if (inv instanceof EdsSaleInvoice) {
                setSaleInvoiceData(invoice, newInvoice, inv);
            }

        } else if (invoice instanceof EdsBasePurchaseInvoice) {
            getBasePurchaseInvoiceData(invoice, newInvoice);
        }

        if (invoice != null) {
            EdsInvoiceTransaction invoiceTransaction = transactionManager.getTransactionByInvoice(invoice);
            if (invoiceTransaction != null) {
                newInvoice.setJournalId(invoiceTransaction.getJournalId());
            }
        }
        return newInvoice;
    }

    private void setSaleInvoiceData(EdsInvoice invoice, NewInvoice newInvoice, EdsBaseSaleInvoice inv) {
        EdsSaleInvoice saleInv = setSaleInvoiceBaseData((EdsSaleInvoice) invoice, newInvoice);
        setItemQtyPermanentlyIfProgressInvoicing(saleInv, newInvoice);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewInvoice getInvoiceForCreditNote(Integer id) {
        EdsInvoice invoice = invoiceManager.get(id);

        ArrayList<CompanyCustomFieldItem> itemCustomFields = null;

        if (invoice instanceof EdsSaleInvoice) {
            itemCustomFields = commonService.getCompanyAllCustomFields(ViewName.SaleInvoiceItem);
        } else if (invoice instanceof EdsPurchaseInvoice) {
            itemCustomFields = commonService.getCompanyAllCustomFields(ViewName.PurchaseInvoiceItem);
        }
        invoice.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));

        return createCreditNoteData(invoice);
    }

    private NewInvoice createCreditNoteData(EdsInvoice invoice) {
        NewInvoice newInvoice = getInvoiceBaseData(invoice);
        if (invoice instanceof EdsBaseSaleInvoice) {
            EdsBaseSaleInvoice inv = getBaseSaleInvoiceData(invoice, newInvoice);
            if (inv instanceof EdsSaleInvoice) {
                setSaleInvoiceDataForCreditNote(invoice, newInvoice, inv);
            }

        } else if (invoice instanceof EdsBasePurchaseInvoice) {
            getBasePurchaseInvoiceData(invoice, newInvoice);
        }
        return newInvoice;
    }

    private NewInvoice getInvoiceBaseData(EdsInvoice invoice) {
        NewInvoice newInvoice = EdsInvoice.getInvoiceData(invoice);
        newInvoice.setBaseCurrencyName(getBaseCurrency().getName());
        newInvoice.setExchageRate(invoice.getExchangeRate());
        newInvoice.setTaxCalculationType(invoice.getTaxCalculationType());

        if (invoice.getCustomType() != null) {
            newInvoice.setInvoiceCustomType(invoice.getCustomType().getCode());
        }
        newInvoice.setDiscountType(invoice.getDiscountType());
        newInvoice.setDiscountAmount(invoice.getDiscountAmount());
        if (invoice.getPriceLevelID() != null) {
            EdsPriceLevel priceLevel = priceLevelManager.get(invoice.getPriceLevelID());
            newInvoice.setPriceLevel(new SelectItem(priceLevel.getObjectID(), priceLevel.getName()));
        }
        if (invoice.getClientDiscountID() != null) {
            EdsDiscount discount = discountManager.get(invoice.getClientDiscountID());
            newInvoice.setClientDiscount(new SelectItem(discount.getObjectID(), discount.getName()));
        }
        if (invoice.getPlaceOfSupplyId() != null) {
            if (PLACEOFSUPPLY_CATEGORY.REGION.equals(invoice.getPlaceOfSupplyCategory())) {
                EdsRegion region = regionManager.get(invoice.getPlaceOfSupplyId());
                SelectItem placeOfSupply = region.getAsSelectItem();
                placeOfSupply.setCode(region.getCode());
                placeOfSupply.setCategory(PLACEOFSUPPLY_CATEGORY.REGION);
                newInvoice.setPlaceOfSupply(placeOfSupply);
            } else if (PLACEOFSUPPLY_CATEGORY.COUNTRY.equals(invoice.getPlaceOfSupplyCategory())) {
                EdsCountry country = countryManager.get(invoice.getPlaceOfSupplyId());
                SelectItem placeOfSupply = country.getAsSelectItem();
                placeOfSupply.setCode(country.getCode());
                placeOfSupply.setCategory(PLACEOFSUPPLY_CATEGORY.COUNTRY);
                newInvoice.setPlaceOfSupply(placeOfSupply);
            }
        }
        newInvoice.setHistoryList(invoiceCircularResolver.getInvoiceNotes(invoice.getObjectID()));
        if (invoice.getPdfTemplate() != null) {
            newInvoice.setPdfTemplateID(invoice.getPdfTemplate().getObjectID());
        }
        return newInvoice;
    }

    private void getBasePurchaseInvoiceData(EdsInvoice invoice, NewInvoice newInvoice) {
        EdsBasePurchaseInvoice pInv = (EdsBasePurchaseInvoice) invoice;
        if (pInv.getClientID() != null) {
            EdsCrmAccount client = crmAccountManager.get(pInv.getClientID());
            if (client != null) {
                TypeItem typeItem = new TypeItem(client.getObjectID(), client.getName(), null);
                typeItem.setMailAddressID(pInv.getClientMailAddressID());
                newInvoice.setClientItem(typeItem);
            }
        }
        if (pInv.getSupplier() != null && pInv.getSupplier().getObjectID() != null) {
            EdsCrmAccount supplier = crmAccountManager.get(pInv.getSupplier().getObjectID());
            newInvoice.getTypeItem().setSupplierCustomerBalance(crmAccountManager.getSupplierBalance(supplier.getObjectID()).doubleValue());
        }

        if (pInv.getInvoiceTerms() != null) {
            newInvoice.setInvoiceTermsItem(pInv.getInvoiceTerms().getAsRPC());
        }
        newInvoice.setCancelDate(pInv.getCancelDate() != null ? new DateNonConvertable(pInv.getCancelDate()) : null);
        newInvoice.setReversechargeApplicable(pInv.isReverseChargeApplicable());

        if (invoice instanceof EdsRecurringBill) {
            newInvoice.setRecurringInvoice(true);
            newInvoice.setRecurrenceJobItem(recurrenceService.getRecurringBillRecurrenceItem((EdsRecurringBill) invoice));
        }
        FileItem[] atts = getAttachments(invoice.getObjectID(), F_PUR_INV);
        newInvoice.setAttachments((atts != null && atts.length > 0) ? atts : new FileItem[0]);

        //init invoice custom fields
        EdsInvoiceCustomFields customFields = invoice.getCustomFields();
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.PurchaseInvoice);
        newInvoice.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(customFields, customFieldsItems));
        newInvoice.setSystemCustomFields(commonService.getCompanyCustomFields(ViewName.PurchaseInvoiceSystem));
        //Set BillOfEntryId
        if (invoice instanceof EdsPurchaseInvoice) {
            newInvoice.setBillOfEntryId(((EdsPurchaseInvoice) invoice).getBillOfEntryId());
            newInvoice.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_PURCHASE_INVOICE) || approverManager.isExistApproverByEntityType(RelationItem.TYPE_DEBIT_NOTE));
            if (invoice.getCurrentApprover() != null && invoice.getCurrentApprover().getExactEmployee() != null) {
                newInvoice.setCurrentApproverSelectItem(invoice.getCurrentApprover().getExactEmployee().getAsSelectItem());
            }
            newInvoice.setApproverSaved(
                    approverManager.isExistApproverByEntityTypeAndEntityId(RelationItem.TYPE_PURCHASE_INVOICE, invoice.getObjectID()) ||
                            approverManager.isExistApproverByEntityTypeAndEntityId(RelationItem.TYPE_DEBIT_NOTE, invoice.getObjectID())
            );
        }
        for (NewInvoiceItem invoiceItem : newInvoice.getItems()) {
            if (invoiceItem.getTrackBatchesEnabled()) {
                invoiceItem.setBatchItems(itemBatchService.getBatchItems(invoiceItem.getID(), invoiceItem.getItemID(), newInvoice.getID(), ItemSerialEntityType.PURCHASE_INVOICE.name()));
            }
        }
    }

    private EdsBaseSaleInvoice getBaseSaleInvoiceData(EdsInvoice invoice, NewInvoice newInvoice) {
        EdsBaseSaleInvoice inv = (EdsBaseSaleInvoice) invoice;
        if (inv.getRecurrence_number() != null) {
            newInvoice.setRecurrenceNumber(inv.getRecurrence_number());
        }
        if (inv.getRecurrence_pattern() != null) {
            newInvoice.setRecurrencePatternId(inv.getRecurrence_pattern().getObjectID());
            newInvoice.setRecurrencePattern(inv.getRecurrence_pattern().getDescription());
        } else {
            newInvoice.setRecurrencePattern("");
        }
        newInvoice.setNumberData(getSaleInvoiceNumber(inv.getCompany(), inv.getCustomType() != null ? inv.getCustomType().getDescription() : null));
        newInvoice.setQuoteNumber(inv.getQuoteNumber());
        newInvoice.setReference(inv.getReference());
        newInvoice.setIntroduction(inv.getIntroduction());
        newInvoice.setPaymentInstructionID(inv.getPaymentInstructionID());
        newInvoice.setBankAccount(inv.getBankAccount() != null ? inv.getBankAccount().getAsSelectItem() : null);
        if (inv instanceof EdsRecurringInvoice) {
            newInvoice.setRecurringInvoice(true);
            newInvoice.setRecurrenceJobItem(recurrenceService.getRecurringInvoiceRecurrenceItem((EdsRecurringInvoice) inv));
            newInvoice.setInvoiceType(((EdsRecurringInvoice) inv).getInvoiceType());
        }

        //init invoice custom fields
        EdsInvoiceCustomFields customFields = invoice.getCustomFields();
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.SaleInvoice);
        newInvoice.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(customFields, customFieldsItems));
        newInvoice.setSystemCustomFields(commonService.getCompanyCustomFields(ViewName.SaleInvoiceSystem));

        if (inv.getShippingMethod() != null) {
            newInvoice.setShippingMethodID(inv.getShippingMethod().getObjectID());
            newInvoice.setShippingMethodName(inv.getShippingMethod().getName());

            ShippingMethod shm = inv.getShippingMethod().getRPC();
            shm.setCurrencyId(inv.getCurrency().getObjectID());
            shm.setExchangeRate(inv.getExchangeRate());

            if (inv.getShippingAmount() != null && inv.getShippingAmount().compareTo(BigDecimal.ZERO) > 0) {
                shm.setPrice(inv.getShippingAmount());
            }
            newInvoice.setShippingPrice(shm.getPrice());
            newInvoice.setShippingMethod(shm);
        }

        FileItem[] atts = newInvoice.isRecurringInvoice() ? new FileItem[0] : getAttachments(invoice.getObjectID(), F_SALE_INV);
        newInvoice.setAttachments((atts != null && atts.length > 0) ? atts : new FileItem[0]);

        for (NewInvoiceItem invoiceItem : newInvoice.getItems()) {
            invoiceItem.setAssignedSerials(productSerialManager.getInvoiceItemSerialsAsSelectItem(invoiceItem.getID()));
            if (invoiceItem.getTrackBatchesEnabled()) {
                invoiceItem.setBatchItems(itemBatchService.getBatchItems(invoiceItem.getID(), invoiceItem.getItemID(), newInvoice.getID(), ItemSerialEntityType.SALES_INVOICE.name()));
            }
        }
        return inv;
    }

    private void setSaleInvoiceDataForCreditNote(EdsInvoice invoice, NewInvoice newInvoice, EdsBaseSaleInvoice inv) {
        EdsSaleInvoice saleInv = setSaleInvoiceBaseData((EdsSaleInvoice) invoice, newInvoice);
//        setItemQtyPermanentlyIfProgressInvoicing(saleInv, newInvoice);
    }

    private EdsSaleInvoice setSaleInvoiceBaseData(EdsSaleInvoice invoice, NewInvoice newInvoice) {
        EdsSaleInvoice saleInv = invoice;
        newInvoice.setNumberData(parseNumberData(saleInv));
        newInvoice.setProjectBasedInvoice(saleInv.isProjectBasedInvoice());
        newInvoice.setPeriodStart(saleInv.getFromDate() != null ? new DateNonConvertable(saleInv.getFromDate()) : null);
        newInvoice.setPeriodEnd(saleInv.getToDate() != null ? new DateNonConvertable(saleInv.getToDate()) : null);
        newInvoice.setPreviosBalance(saleInv.getPreviousBalance());
        newInvoice.setPaymentsReceived(saleInv.getPaymentReceived());
//                newInvoice.setProgressInvoicing(saleInv.getQuotePercent() != null || saleInv.getQuoteAmount() != null);
        newInvoice.setConvertedPercent(saleInv.getQuotePercent());
        newInvoice.setConvertedAmount(saleInv.getQuoteAmount());
        newInvoice.setInvoiceType(saleInv.getInvoiceType());
        newInvoice.setClientID(saleInv.getClient() != null ? saleInv.getClient().getObjectID() : null);
        newInvoice.setInTarget(saleInv.isInTarget());
        newInvoice.setTargetId(saleInv.getTargetId());
        if (saleInv.getClient() != null && saleInv.getClient().getObjectID() != null) {
            newInvoice.getTypeItem().setSupplierCustomerBalance(crmAccountManager.getClientBalance(saleInv.getClient().getObjectID()).doubleValue());
        }
        if (saleInv.getCurrentApprover() != null && saleInv.getCurrentApprover().getExactEmployee() != null) {
            newInvoice.setCurrentApproverSelectItem(saleInv.getCurrentApprover().getExactEmployee().getAsSelectItem());
        }
        if (saleInv.getInvoiceTerms() != null) {
            newInvoice.setInvoiceTermsItem(saleInv.getInvoiceTerms().getAsRPC());
        }
        newInvoice.setZatcaStatus(saleInv.getZatcaStatus());
        newInvoice.setApproverSaved(approverManager.isExistApproverByEntityTypeAndEntityId(RelationItem.TYPE_SALEINVOICE, saleInv.getObjectID()));
        return saleInv;
    }

    private void setItemQtyPermanentlyIfProgressInvoicing(EdsInvoice saleInv, NewInvoice newInvoice) {
        if (saleInv.getConvertedQuotes() != null && !saleInv.getConvertedQuotes().isEmpty()) {
            for (EdsQuote q : saleInv.getConvertedQuotes()) {
                if (q instanceof EdsSaleQuote saleQuote && saleQuote.isProgressInvoicing()) {
                    newInvoice.setProgressInvoicing(true);
                    if (saleQuote.getProgressInvoicingType() != null) {
                        newInvoice.setProgressInvoicingType(saleQuote.getProgressInvoicingType());
                    }
                    break;
                }
            }
        }
        if (newInvoice.isProgressInvoicing()) {
            NewInvoiceItem[] invoiceItems = newInvoice.getItems();
            for (NewInvoiceItem nii : invoiceItems) {
                nii.setQtyWithHighScale(nii.getQuantity());
            }
        }
    }

    private InvoiceNumberData parseNumberData(EdsBaseSaleInvoice inv) {
        InvoiceNumberData numberData = getSaleInvoiceNumber(inv.getCompany(), inv.getCustomType() != null ? inv.getCustomType().getDescription() : null);
        numberData.setFourDigitNumber(inv.getFourDigitNumber() != null ? new DecimalFormat("0000").format(inv.getFourDigitNumber()) : "");
        numberData.setWithClient(inv.getClient().getNumber() != null && inv.getNumber().contains(inv.getClient().getNumber()));
        numberData.setClientCode(numberData.isWithClient() ? inv.getClient().getNumber() : "");
        if (inv.getRelatedProject() != null && inv.getRelatedProject().getNumber() != null) {
            numberData.setWithProject(inv.getNumber().contains(inv.getRelatedProject().getNumber()));
            if (numberData.isWithProject()) {
                numberData.setProjectCode(inv.getRelatedProject().getNumber());
            }
        }
        return numberData;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FileItem[] getAttachments(Integer id, int folderType) {
        List<FileResource> purchaseInvAttachments = attachmentUtilsManager.getAttachments(folderType, id, id);
        FileItem[] fileItems = new FileItem[purchaseInvAttachments.size()];
        for (
                int i = 0;
                i < purchaseInvAttachments.size();
                i++) {
            FileResource fileResource = purchaseInvAttachments.get(i);
            FileItem fileItem = new FileItem();
            fileItem.setAttachmentId(fileResource.getBodyId());
            fileItem.setFileName(fileResource.getEncodedName());
            fileItem.setDescription(fileResource.getDescription());
            fileItem.setSize(fileResource.getContentLength());
            fileItem.setUploadType(fileResource.getUploadType());
            fileItem.setDate(fileResource.getCreationDate());
            switch (fileResource.getUploadType()) {
                case GOOGLE -> fileItem.setGoogleDocumentLink(fileResource.getGoogleDownloadLink());
                case OFFICE_365, OFFICE_365_SHARE_POINT -> {
                    fileItem.setDocumentID(fileResource.getDocumentID());
                    fileItem.setDocumentOpenID(fileResource.getDocumentOpenID());
                    fileItem.setOfficeDocumentLink(fileResource.getOfficeDownloadLink());
                }
                default -> fileItem.setAmazonLink(fileResource.getAmazonLink());
            }
            fileItems[i] = fileItem;
        }

        return fileItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListHeap getUserInfo() {
        ListHeap listHeap = new ListHeap();
        HashMap userInfo = new HashMap();

        EdsUser user = invoiceManager.getUser();
        String billingInfo = invoiceManager.getLastBillingInformation();

        userInfo.put("email", user.getEmail());
        userInfo.put("company", user.getCompany().getName());
        userInfo.put("notes", (billingInfo == null ? "" : billingInfo));
        userInfo.put("dueDate", getDueDate(user));
        EdsCurrency currency = financialSettingsManager.getFinancialSettings().getCurrency();
        userInfo.put("currency", currency == null ? null : currency.getObjectID());
        userInfo.put("currencySymbol", getDefaultCurrencySymbol());

        listHeap.setUserInfo(userInfo);

        return listHeap;
    }

    @Transactional
    public Integer savePayment(PaymentData data) {

        if (data.isValidateReference() && accountingManager.isDuplicateReference(data.getReferenceNumber(), null)) {
            return PaymentData.REFERENCE_EXIST;
        }

        EdsUser user = invoiceManager.getUser();

        Integer calculationScale = user != null ? financialSettingsManager.getFinancialSettings().getAccountingCalculationScale() : 2;
        EdsInvoice invoice = data.getInvoiceID() != null ? invoiceManager.get(data.getInvoiceID()) : null;
        EdsExpenseReport edsExpenseReport = expenseReportManager.getExpenseReport(data.getExpenseId());

        if (invoice != null) {
            if (currencyService.getBaseCurrency().getId().equals(invoice.getCurrency().getObjectID())) {
                BigDecimal fullPayment = invoice.getFullPaymentsInBase().add(data.getPaymentAmount());
                if (!data.isFullPaid() && data.getUnderPaymentID() == null && invoice.getTotal().setScale(calculationScale, RoundingMode.HALF_UP).compareTo(fullPayment.setScale(calculationScale, RoundingMode.HALF_UP)) < 0) {
                    return PaymentData.OVER_PAID;
                }
            } else {
                BigDecimal fullPayments = invoice.getFullPayments().add(data.getPaymentAmountInInvoiceCurrency() != null ? data.getPaymentAmountInInvoiceCurrency() : data.getPaymentAmount());
                if (!data.isFullPaid() && data.getUnderPaymentID() == null && invoice.getTotalInInvoiceCurrency().setScale(calculationScale, RoundingMode.HALF_UP).compareTo(fullPayments.setScale(calculationScale, RoundingMode.HALF_UP)) < 0) {
                    return PaymentData.OVER_PAID;
                }
            }
        }

        createRevisionHistory(invoice);

        if (user == null) {
            EdsCrmContact crmContact = invoice != null ? invoice.getClientContact() : null;
            if (crmContact != null) {
                user = clientContactManager.getClientContactByCrmContact(crmContact.getObjectID());
            }
        }

        EdsInvoicePayment result = new EdsInvoicePayment();
        result.setBatchPaymentID(data.getBatchPaymentID());
        result.setPaymentRefundID(data.getPaymentRefundID());
        result.setAmount(data.getPaymentAmount());
        result.setAmountInInvoiceCurrency(data.getPaymentAmountInInvoiceCurrency());
        result.setUnderPaymentID(data.getUnderPaymentID());
        result.setUnderPaymentTaxRate(data.getUnderPaymentTaxRate());
        result.setUnderPaymentTaxAmount(data.getUnderPaymentTaxAmount());
        result.setPaymentDate(data.getDate().getNonConvertedDate());
        result.setForeignAccExRate(data.getForeignAccExRate());
        if (invoice != null && invoice.getCalcScale() != null) {
            result.setCalcScale(invoice.getCalcScale());
        }

        if (RECEIVABLE_PREPAYMENT_SHARE.equals(data.getType()) || RECEIVABLE_CRM_ACCOUNT_CREDIT.equals(data.getType())) {
            if (data.getPaymentAccount() != null && data.getPaymentAccount().getId() != null) {
                result.setAccount(accountingManager.get(data.getPaymentAccount().getId()));
            } else {
                if (data.getReceivablePayable() != null) {
                    result.setAccount(accountingManager.get(data.getReceivablePayable().getId()));
                } else {
                    result.setAccount(accountingManager.getAccountByKey(EdsAccount.ACCOUNTS_RECEIVABLE));
                }
            }

            EdsCrmAccount crmAccount = crmAccountManager.get(data.getCrmAccount().getId());
            result.setCrmAccount(crmAccount);
            result.setBaseAmount(data.getBaseAmount());

            if (RECEIVABLE_PREPAYMENT_SHARE.equals(data.getType())) {
                if (data.getObjectID() != null) {
                    EdsInvoicePayment prePaymentItem = invoicePaymentManager.get(data.getObjectID());
                    if (prePaymentItem != null) {
                        result.setAppliedPayment(prePaymentItem);
                        invoicePaymentManager.update(prePaymentItem);
                        BigDecimal appliedAmount = invoicePaymentManager.getAppliedPrePaymentAmounts(prePaymentItem.getCrmAccount().getObjectID(), prePaymentItem.getObjectID(), AccountingConstants.RECEIVABLE_PREPAYMENT_SHARE, RECEIVABLE_PREPAYMENT_REFUND);

                        if (!invoice.getCurrency().getObjectID().equals(prePaymentItem.getCurrencyID())) {
                            appliedAmount = appliedAmount.add(data.getBaseAmount().multiply(prePaymentItem.getExchangeRate())).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                        } else {
                            appliedAmount = appliedAmount.add(data.getPaymentAmount()).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                        }
                        BigDecimal totalAmount = prePaymentItem.getAmountInInvoiceCurrency() != null ? prePaymentItem.getAmountInInvoiceCurrency() : prePaymentItem.getAmount();
                        BigDecimal subtractAmount = totalAmount.subtract(appliedAmount).setScale(calculationScale, RoundingMode.HALF_UP);

                        if (subtractAmount.compareTo(BigDecimal.ZERO) == 0) {
                            prePaymentItem.setPaymentStatus(AccountingConstants.PRE_PAYMENT_APPLIED_STATUS);
                        } else if (subtractAmount.compareTo(BigDecimal.ZERO) > 0) {
                            prePaymentItem.setPaymentStatus(AccountingConstants.PRE_PAYMENT_PARTIAL_APPLIED_STATUS);
                        } else {
                            prePaymentItem.setPaymentStatus(AccountingConstants.PRE_PAYMENT_OPEN_STATUS);
                        }
                        if (prePaymentItem.getBatchPaymentID() != null) {
                            EdsBatchPayment batchPayment = batchPaymentManager.get(prePaymentItem.getBatchPaymentID());
                            batchPayment.setChangedType("APPLY_CREDIT");
                            batchPayment.setCreditAmount(data.getPaymentAmount());
//                            batchPaymentManager.createOrUpdate(batchPayment);
                            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, batchPayment, userManager.getUser());
                            event.setEntityType(RelationItem.TYPE_BATCH_PAYMENT_RECEIVABLE);
                        }
                        invoicePaymentManager.update(prePaymentItem);
                    }
                }
            } else if (RECEIVABLE_CRM_ACCOUNT_CREDIT.equals(data.getType())) {
                crmAccount.setAppliedCredit(crmAccount.getAppliedCredit().add(data.getBaseAmount()));
                crmAccountManager.update(crmAccount);
            }
        } else if (PAYABLE_BANK_CHECK_SHARE.equals(data.getType()) || PAYABLE_SUPPLIER_CREDIT_SHARE.equals(data.getType()) || PAYABLE_CRM_ACCOUNT_CREDIT.equals(data.getType())) {

            if (data.getPaymentAccount() != null && data.getPaymentAccount().getId() != null) {
                result.setAccount(accountingManager.get(data.getPaymentAccount().getId()));
            } else {
                if (data.getReceivablePayable() != null) {
                    result.setAccount(accountingManager.get(data.getReceivablePayable().getId()));
                } else {
                    result.setAccount(accountingManager.getAccountByKey(EdsAccount.ACCOUNTS_PAYABLE));
                }
            }
            EdsCrmAccount crmAccount = crmAccountManager.get(data.getCrmAccount().getId());
            result.setCrmAccount(crmAccount);
            result.setBaseAmount(data.getBaseAmount());

            if (PAYABLE_SUPPLIER_CREDIT_SHARE.equals(data.getType())) {
                if (data.getObjectID() != null) {
                    EdsInvoicePayment supplierCreditItem = invoicePaymentManager.get(data.getObjectID());
                    if (supplierCreditItem != null) {
                        result.setAppliedPayment(supplierCreditItem);
                        invoicePaymentManager.update(supplierCreditItem);
                        BigDecimal appliedAmount = invoicePaymentManager.getAppliedPrePaymentAmounts(supplierCreditItem.getCrmAccount().getObjectID(), supplierCreditItem.getObjectID(), AccountingConstants.PAYABLE_SUPPLIER_CREDIT_SHARE, PAYABLE_PREPAYMENT_REFUND);
                        Integer currencyID = invoice != null && invoice.getCurrency() != null ? invoice.getCurrency().getObjectID() : null;
                        if (edsExpenseReport != null) {
                            currencyID = edsExpenseReport.getCurrency().getObjectID();
                        }
                        if (!currencyID.equals(supplierCreditItem.getCurrencyID())) {
                            appliedAmount = appliedAmount.add(data.getBaseAmount().multiply(supplierCreditItem.getExchangeRate())).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                        } else {
                            appliedAmount = appliedAmount.add(data.getPaymentAmount()).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                        }

                        appliedAmount = appliedAmount.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);

                        BigDecimal totalAmount = supplierCreditItem.getAmountInInvoiceCurrency() != null ? supplierCreditItem.getAmountInInvoiceCurrency() : supplierCreditItem.getAmount();
                        BigDecimal subtractAmount = totalAmount.subtract(appliedAmount);
                        if (subtractAmount.compareTo(BigDecimal.ZERO) == 0) {
                            supplierCreditItem.setPaymentStatus(AccountingConstants.PRE_PAYMENT_APPLIED_STATUS);
                        } else if (subtractAmount.compareTo(BigDecimal.ZERO) > 0) {
                            supplierCreditItem.setPaymentStatus(AccountingConstants.PRE_PAYMENT_PARTIAL_APPLIED_STATUS);
                        } else {
                            supplierCreditItem.setPaymentStatus(AccountingConstants.PRE_PAYMENT_OPEN_STATUS);
                        }
                        invoicePaymentManager.update(supplierCreditItem);
                    }
                }
            } else if (PAYABLE_CRM_ACCOUNT_CREDIT.equals(data.getType())) {
                crmAccount.setAppliedCredit(crmAccount.getAppliedCredit().add(data.getBaseAmount()));
                crmAccountManager.update(crmAccount);
            }
        } else if (RECEIVABLE_MANUAL_CREDIT.equals(data.getType()) || RECEIVABLE_BANKTRANSFER_CREDIT.equals(data.getType())) {

            if (data.getPaymentAccount() != null && data.getPaymentAccount().getId() != null) {
                result.setAccount(accountingManager.get(data.getPaymentAccount().getId()));
            } else {
                result.setAccount(accountingManager.getAccountByKey(EdsAccount.ACCOUNTS_RECEIVABLE));
            }
            EdsCrmAccount crmAccount = crmAccountManager.get(data.getCrmAccount().getId());
            result.setCrmAccount(crmAccount);
            result.setBaseAmount(data.getBaseAmount());
            result.setManualJournalID(data.getManualJournalID());
            result.setBankTransferID(data.getBankTransferID());
        } else if (PAYABLE_MANUAL_CREDIT.equals(data.getType()) || PAYABLE_BANKTRANSFER_CREDIT.equals(data.getType())) {

            if (data.getPaymentAmount() != null && data.getPaymentAccount().getId() != null) {
                result.setAccount(accountingManager.get(data.getPaymentAccount().getId()));
            } else {
                result.setAccount(accountingManager.getAccountByKey(EdsAccount.ACCOUNTS_PAYABLE));
            }
            EdsCrmAccount crmAccount = crmAccountManager.get(data.getCrmAccount().getId());
            result.setCrmAccount(crmAccount);
            result.setBaseAmount(data.getBaseAmount());
            result.setManualJournalID(data.getManualJournalID());
            result.setBankTransferID(data.getBankTransferID());
        } else {
            if (VATRETURN_PAYMENT_RECEIVABLE.equals(data.getType()) || VATRETURN_PAYMENT_PAYABLE.equals(data.getType())) {
                EdsVatEFiling vatEFile = vatEFilingManager.get(data.getRelatedObjectID());
                result.setVatEFile(vatEFile);
                BigDecimal paidAmount = vatEFilingManager.getVatReturnPaymentTotal(vatEFile.getObjectID());
                paidAmount = (paidAmount != null ? paidAmount : ZERO).add(data.getPaymentAmount()).setScale(calculationScale, RoundingMode.HALF_UP);
                if (vatEFile.getVatToReclaimFromCustoms().compareTo(paidAmount) <= 0) {
                    vatEFile.setStatus(AccountingConstants.SUBMISSION_PAID);
                }
            } else if ((RECEIVABLE.equals(data.getType()) || PAYABLE.equals(data.getType()))) {
                result.setAmountInInvoiceCurrency(data.getPaymentAmountInInvoiceCurrency());
            }
            result.setAccount(accountingManager.get(data.getPaymentAccount().getId()));

            if (RECEIVABLE_PREPAYMENT_REFUND.equals(data.getType()) && data.getPrepaymentID() != null) {
                EdsInvoicePayment prePaymentItem = invoicePaymentManager.get(data.getPrepaymentID());
                if (prePaymentItem != null) {
                    result.setAppliedPayment(prePaymentItem);
                    result.setBaseAmount(data.getBaseAmount());
                    result.setClosedAmount(data.getClosedAmount());
                    invoicePaymentManager.update(prePaymentItem);
                    BigDecimal appliedAmount = invoicePaymentManager.getAppliedPrePaymentAmounts(prePaymentItem.getCrmAccount().getObjectID(), prePaymentItem.getObjectID(), AccountingConstants.RECEIVABLE_PREPAYMENT_SHARE, AccountingConstants.RECEIVABLE_PREPAYMENT_REFUND);

                    BigDecimal paymentAmount = data.getPaymentAmountInInvoiceCurrency() != null ? data.getPaymentAmountInInvoiceCurrency() : data.getPaymentAmount();
                    appliedAmount = appliedAmount.add(paymentAmount).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);

                    BigDecimal totalAmount = prePaymentItem.getAmountInInvoiceCurrency() != null ? prePaymentItem.getAmountInInvoiceCurrency() : prePaymentItem.getAmount();
                    BigDecimal subtractAmount = totalAmount.subtract(appliedAmount).setScale(calculationScale, RoundingMode.HALF_UP);

                    if (subtractAmount.compareTo(BigDecimal.ZERO) == 0 || data.isClosePrepayment()) {
                        prePaymentItem.setPaymentStatus(AccountingConstants.PRE_PAYMENT_APPLIED_STATUS);
                    } else if (subtractAmount.compareTo(BigDecimal.ZERO) > 0) {
                        prePaymentItem.setPaymentStatus(AccountingConstants.PRE_PAYMENT_PARTIAL_APPLIED_STATUS);
                    } else {
                        prePaymentItem.setPaymentStatus(AccountingConstants.PRE_PAYMENT_OPEN_STATUS);
                    }
                    invoicePaymentManager.update(prePaymentItem);
                    if (prePaymentItem.getBatchPaymentID() != null) {
                        EdsBatchPayment batchPayment = batchPaymentManager.get(prePaymentItem.getBatchPaymentID());
                        batchPayment.setChangedType("ADD_REFUND");
                        batchPayment.setCreditAmount(data.getPaymentAmount());
                        EdsBusinessEvent event = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, batchPayment, userManager.getUser());
                        event.setEntityType(RelationItem.TYPE_BATCH_PAYMENT_RECEIVABLE);
                    }
                }
            } else if (PAYABLE_PREPAYMENT_REFUND.equals(data.getType()) && data.getPrepaymentID() != null) {
                EdsInvoicePayment prePaymentItem = invoicePaymentManager.get(data.getPrepaymentID());
                if (prePaymentItem != null) {
                    result.setAppliedPayment(prePaymentItem);
                    result.setBaseAmount(data.getBaseAmount());
                    result.setClosedAmount(data.getClosedAmount());
                    invoicePaymentManager.update(prePaymentItem);
                    BigDecimal appliedAmount = invoicePaymentManager.getAppliedPrePaymentAmounts(prePaymentItem.getCrmAccount().getObjectID(), prePaymentItem.getObjectID(), AccountingConstants.PAYABLE_SUPPLIER_CREDIT_SHARE, AccountingConstants.PAYABLE_PREPAYMENT_REFUND);

                    BigDecimal paymentAmount = data.getPaymentAmountInInvoiceCurrency() != null ? data.getPaymentAmountInInvoiceCurrency() : data.getPaymentAmount();
                    appliedAmount = appliedAmount.add(paymentAmount).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);

                    BigDecimal totalAmount = prePaymentItem.getAmountInInvoiceCurrency() != null ? prePaymentItem.getAmountInInvoiceCurrency() : prePaymentItem.getAmount();
                    BigDecimal subtractAmount = totalAmount.subtract(appliedAmount).setScale(calculationScale, RoundingMode.HALF_UP);

                    if (subtractAmount.compareTo(BigDecimal.ZERO) == 0 || data.isClosePrepayment()) {
                        prePaymentItem.setPaymentStatus(AccountingConstants.PRE_PAYMENT_APPLIED_STATUS);
                    } else if (subtractAmount.compareTo(BigDecimal.ZERO) > 0) {
                        prePaymentItem.setPaymentStatus(AccountingConstants.PRE_PAYMENT_PARTIAL_APPLIED_STATUS);
                    } else {
                        prePaymentItem.setPaymentStatus(AccountingConstants.PRE_PAYMENT_OPEN_STATUS);
                    }
                    invoicePaymentManager.update(prePaymentItem);
                }
            }

            if (invoice != null) {
                result.setCrmAccount(invoice.getClientOrSupplier());
            } else if (data.getCrmAccount() != null) {
                result.setCrmAccount(crmAccountManager.get(data.getCrmAccount().getId()));
            }
        }
        result.setReference(data.getReferenceNumber());
        result.setUser(user);
        result.setExchangeRate(data.getExchangeRate());
        result.setCurrencyID(data.getCurrency() != null && data.getCurrency().getId() != null ? data.getCurrency().getId() : null);
        result.setType(data.getType());
        result.setGatewayReturnedURL(data.getGatewayReturnedURL());
        result.setExpenseID(data.getExpenseId());
        invoicePaymentManager.create(result);

        if (invoice != null) {
            invoice.setUpdatedDate(new Date());
            result.setInvoice(invoice);
            // include current saved payment, because it is not included in invoice.getPayments()
            List<EdsInvoicePayment> payments = invoicePaymentManager.getPayments(invoice);
            BigDecimal fullPayments = invoice.getFullPayments();
            if (invoice != null && invoice.getPayments() != null && payments != null && invoice.getPayments().size() < payments.size()) {
                invoice.setPayments(payments);
                fullPayments = invoice.getFullPayments();
            } else {
                fullPayments = fullPayments.add(data.getPaymentAmountInInvoiceCurrency() != null ? data.getPaymentAmountInInvoiceCurrency() : data.getPaymentAmount());
            }
            if (invoice.getTotalInInvoiceCurrency().setScale(calculationScale, RoundingMode.HALF_UP).compareTo(fullPayments.setScale(calculationScale, RoundingMode.HALF_UP)) <= 0) {
                invoice.setPaidDate(getCompanyDate(user));
                invoice.setStatus(referenceManager.findReference(INVOICE_STATUS, PAID));
            }
        }

        accountingServiceLocal.createTransactionForPayment(result);

        if (edsExpenseReport != null) {
            BigDecimal paidTotal = expenseServiceLocal.getPaidTotal(edsExpenseReport);
            Double dueAmount = edsExpenseReport.getTotal().doubleValue() - paidTotal.doubleValue();
            if (BigDecimal.valueOf(dueAmount).setScale(5, RoundingMode.HALF_UP).doubleValue() <= 0.01) {
                EdsReference paid = referenceManager.findReference(EXPENSE_STATUS, EXPENSE_PAID);
                edsExpenseReport.setEntityStatus(paid);
            } else {
                EdsReference paid = referenceManager.findReference(EXPENSE_STATUS, PARTIALLY_PAID);
                edsExpenseReport.setEntityStatus(paid);
            }
            expenseReportManager.update(edsExpenseReport);
            try {
                solrManager.addExpenseReportToIndex(edsExpenseReport);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }

        if (invoice != null) {
            invoiceManager.update(invoice);
            addInvoiceToSolr(invoice);
            if (invoice instanceof EdsSaleInvoice) {
                baseEventPostProcessor.registerEvent(InvoicePaymentEventListenerImpl.TYPE, InvoicePaymentEventListenerImpl.EVENT_SALES_INVOICE_PAYMENT_RECEIVE, result, user);
                if (result.getBankTransferID() != null) {
                    baseEventPostProcessor.registerEvent(BankTransferAppliedEventListenerImpl.TYPE, BankTransferAppliedEventListenerImpl.EVENT_BANK_TRANSFER_APPLIED_RECEIVABLE, result, user);
                }
                if (result.getManualJournalID() != null) {
                    baseEventPostProcessor.registerEvent(ManualEntryAppliedEventListenerImpl.TYPE, ManualEntryAppliedEventListenerImpl.EVENT_MANUAL_JOURNAL_APPLIED_RECEIVABLE_PAYABLE, result, user);
                }
            }
            if (invoice instanceof EdsPurchaseInvoice) {
                baseEventPostProcessor.registerEvent(InvoicePaymentEventListenerImpl.TYPE, InvoicePaymentEventListenerImpl.EVENT_PURCHASE_INVOICE_PAYMENT_PAY, result, user);
                if (result.getBankTransferID() != null) {
                    baseEventPostProcessor.registerEvent(BankTransferAppliedEventListenerImpl.TYPE, BankTransferAppliedEventListenerImpl.EVENT_BANK_TRANSFER_APPLIED_PAYABLE, result, user);
                }
                if (result.getManualJournalID() != null) {
                    baseEventPostProcessor.registerEvent(ManualEntryAppliedEventListenerImpl.TYPE, ManualEntryAppliedEventListenerImpl.EVENT_MANUAL_JOURNAL_APPLIED_RECEIVABLE_PAYABLE, result, user);
                }
            }

            ArrayList<EdsShippingData> shippingDataList = new ArrayList<>(invoice.getConvertedShippingData());
            if (!shippingDataList.isEmpty()) {
                try {
                    shippingDataSolrComponent.indexes(shippingDataList);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                EventHandler.fireEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, "Related Invoice is updated");
            }
        }
        return result.getObjectID();
    }

    public void reversePayment(Integer paymentID, DateNonConvertable voidDate) {
        EdsInvoicePayment payment = invoicePaymentManager.get(paymentID);

        reverseAppliedPrePaymentShareBalance(payment);

        payment.setStatus(referenceManager.findReference(EdsInvoicePayment.INVOICEPAYMENT_STATUS, EdsInvoicePayment.REVERSED));
        invoicePaymentManager.update(payment);

        accountingServiceLocal.createReversalTransactionForPayment(payment, voidDate);

        if (payment.getInvoice() != null) {
            checkAndUpdateInvoiceCreditNoteStatuses(payment.getInvoice(), false);

            if (PAYABLE.equals(payment.getInvoice().getType())) {
                EdsPurchaseInvoice purchaseInvoice = invoiceManager.getPurchaseInvoice(payment.getInvoice().getObjectID());
                if (purchaseInvoice != null) {
                    EdsBusinessEvent event = baseEventPostProcessor.registerEvent(InvoicePaymentEventListenerImpl.TYPE, InvoicePaymentEventListenerImpl.EVENT_PURCHASE_INVOICE_PAYMENT_VOID, payment, userManager.getUser());
                    event.setEntityID(purchaseInvoice.getObjectID());
                }
            }
            if (RECEIVABLE.equals(payment.getInvoice().getType())) {
                EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(payment.getInvoice().getObjectID());
                if (saleInvoice != null) {
                    baseEventPostProcessor.registerEvent(InvoicePaymentEventListenerImpl.TYPE, InvoicePaymentEventListenerImpl.EVENT_SALES_INVOICE_PAYMENT_VOID, payment, userManager.getUser());
                }
            }

        }
        if (payment.getCreditNote() != null) {
            checkAndUpdateInvoiceCreditNoteStatuses(payment.getCreditNote(), true);
        }
    }

    public TestRPC deletePayment(Integer paymentID) {
        TestRPC result = new TestRPC();
        EdsInvoicePayment payment = invoicePaymentManager.get(paymentID);
        if (RECEIVABLE.equals(payment.getType()) && payment.getBatchPaymentID() != null && payment.getInvoice() != null && payment.getCrmAccount() != null) {
            boolean isReceivable = RECEIVABLE.equals(payment.getType());

            BigDecimal appliedAmount = invoicePaymentManager.getAppliedPrePaymentAmounts(payment.getCrmAccount().getObjectID(), payment.getObjectID(), isReceivable ? AccountingConstants.RECEIVABLE_PREPAYMENT_SHARE : AccountingConstants.PAYABLE_SUPPLIER_CREDIT_SHARE, isReceivable ? RECEIVABLE_PREPAYMENT_REFUND : PAYABLE_PREPAYMENT_REFUND);
            if (appliedAmount.compareTo(ZERO) > 0) {
                result.setError(true);
                List<String> numbers = invoicePaymentManager.getAppliedPrepaymentsNumberFirst(payment.getCrmAccount().getObjectID(), payment.getObjectID(), isReceivable ? AccountingConstants.RECEIVABLE_PREPAYMENT_SHARE : AccountingConstants.PAYABLE_SUPPLIER_CREDIT_SHARE);
                result.setMessage(commonLocalizer.localize("pleaseDeletePrepaymentsFirst") + ServerUtils.getAsCommoDelimited(numbers, "0", ", "));
                return result;
            }
        }

        //payment came from batch payment then update its batch payment object
        if (payment.getBatchPaymentID() != null) {
            BigDecimal total = invoicePaymentManager.getBatchPaymentItems(payment.getBatchPaymentID(), payment.getObjectID(), true);

            EdsBatchPayment batchPayment = batchPaymentManager.get(payment.getBatchPaymentID());

            if (total.compareTo(BigDecimal.ZERO) > 0) {
                batchPayment.setTotalAmount(total);
            } else {
                batchPayment.setDeleted(true);
                deleteBatchPaymentNumberData(batchPayment);
            }
        }


        reverseAppliedPrePaymentShareBalance(payment);

        accountingServiceLocal.deleteInvoicePaymentTransaction(payment);
        payment.setDeleted(true);

        invoicePaymentManager.update(payment);

        if (payment.getInvoice() != null && !payment.getInvoice().isDeleted()) {
            checkAndUpdateInvoiceCreditNoteStatuses(payment.getInvoice(), false);
            if (PAYABLE.equals(payment.getInvoice().getType())) {
                EdsPurchaseInvoice purchaseInvoice = invoiceManager.getPurchaseInvoice(payment.getInvoice().getObjectID());
                if (purchaseInvoice != null) {
                    baseEventPostProcessor.registerEvent(InvoicePaymentEventListenerImpl.TYPE, InvoicePaymentEventListenerImpl.EVENT_PURCHASE_INVOICE_PAYMENT_DELETE, payment, userManager.getUser());
                }
            }
            if (RECEIVABLE.equals(payment.getInvoice().getType())) {
                EdsSaleInvoice saleInvoice = (EdsSaleInvoice) invoiceManager.get(payment.getInvoice().getObjectID());
                if (saleInvoice != null) {
                    baseEventPostProcessor.registerEvent(InvoicePaymentEventListenerImpl.TYPE, InvoicePaymentEventListenerImpl.EVENT_SALES_INVOICE_PAYMENT_DELETE, payment, userManager.getUser());
                }
            }
        }
        if (payment.getCreditNote() != null) {
            checkAndUpdateInvoiceCreditNoteStatuses(payment.getCreditNote(), true);
        }
        if (payment.getExpenseID() != null) {
            EdsExpenseReport edsExpenseReport = expenseReportManager.getExpenseReport(payment.getExpenseID());
            if (edsExpenseReport != null) {
                BigDecimal paidTotal = expenseServiceLocal.getPaidTotal(edsExpenseReport);
                Double dueAmount = edsExpenseReport.getTotal().doubleValue() - paidTotal.doubleValue();
                if (BigDecimal.valueOf(dueAmount).setScale(5, RoundingMode.HALF_UP).doubleValue() <= 0.01) {
                    EdsReference paid = referenceManager.findReference(EXPENSE_STATUS, EXPENSE_PAID);
                    edsExpenseReport.setEntityStatus(paid);
                } else {
                    EdsReference paid = referenceManager.findReference(EXPENSE_STATUS, PARTIALLY_PAID);
                    edsExpenseReport.setEntityStatus(paid);
                }
                expenseReportManager.update(edsExpenseReport);
                try {
                    solrManager.addExpenseReportToIndex(edsExpenseReport);
                } catch (SolrServerException | IOException e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            }
        }
        return result;
    }

    private void reverseAppliedPrePaymentShareBalance(EdsInvoicePayment payment) {
        if (payment != null && payment.getBaseAmount() != null) {
            if (RECEIVABLE_PREPAYMENT_SHARE.equals(payment.getType()) || RECEIVABLE_PREPAYMENT_REFUND.equals(payment.getType())) {
                if (payment.getAppliedPayment() != null) {
                    EdsInvoicePayment prePayment = payment.getAppliedPayment();
                    BigDecimal appliedAmount = invoicePaymentManager.getAppliedPrePaymentAmounts(prePayment.getCrmAccount().getObjectID(), prePayment.getObjectID(), AccountingConstants.RECEIVABLE_PREPAYMENT_SHARE, RECEIVABLE_PREPAYMENT_REFUND);
                    BigDecimal paymentBalance = prePayment.getAmountInInvoiceCurrency() != null ? prePayment.getAmountInInvoiceCurrency() : prePayment.getAmount();
                    BigDecimal paymentFirstBalance = paymentBalance;

                    BigDecimal paymentAmount = payment.getAmountInInvoiceCurrency() != null ? payment.getAmountInInvoiceCurrency() : payment.getAmount();
                    BigDecimal minusAmount = appliedAmount.subtract(paymentAmount);
                    if (!prePayment.getCurrencyID().equals(payment.getCurrencyID())) {
                        minusAmount = appliedAmount.subtract(payment.getBaseAmount().multiply(prePayment.getExchangeRate())).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    }
                    paymentBalance = paymentBalance.subtract(minusAmount);

                    if (paymentFirstBalance.compareTo(paymentBalance) > 0) {
                        prePayment.setPaymentStatus(AccountingConstants.PRE_PAYMENT_PARTIAL_APPLIED_STATUS);
                    } else if (paymentFirstBalance.compareTo(paymentBalance) == 0) {
                        prePayment.setPaymentStatus(AccountingConstants.PRE_PAYMENT_OPEN_STATUS);
                    } else {
                        prePayment.setPaymentStatus(AccountingConstants.PRE_PAYMENT_APPLIED_STATUS);
                    }
                    invoicePaymentManager.update(prePayment);
                }
            } else if (PAYABLE_SUPPLIER_CREDIT_SHARE.equals(payment.getType()) || PAYABLE_PREPAYMENT_REFUND.equals(payment.getType())) {
                if (payment.getAppliedPayment() != null) {
                    EdsInvoicePayment supplierCredit = payment.getAppliedPayment();
                    BigDecimal appliedAmount = invoicePaymentManager.getAppliedPrePaymentAmounts(supplierCredit.getCrmAccount().getObjectID(), supplierCredit.getObjectID(), AccountingConstants.PAYABLE_SUPPLIER_CREDIT_SHARE, PAYABLE_PREPAYMENT_REFUND);
                    BigDecimal paymentBalance = supplierCredit.getAmountInInvoiceCurrency() != null ? supplierCredit.getAmountInInvoiceCurrency() : supplierCredit.getAmount();
                    BigDecimal paymentFirstBalance = paymentBalance;
                    BigDecimal paymentAmount = payment.getAmountInInvoiceCurrency() != null ? payment.getAmountInInvoiceCurrency() : payment.getAmount();
                    BigDecimal minusAmount = appliedAmount.subtract(paymentAmount);
                    if (!supplierCredit.getCurrencyID().equals(payment.getCurrencyID())) {
                        minusAmount = appliedAmount.subtract(payment.getBaseAmount().multiply(supplierCredit.getExchangeRate())).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    }
                    paymentBalance = paymentBalance.subtract(minusAmount);

                    if (paymentFirstBalance.compareTo(paymentBalance) > 0) {
                        supplierCredit.setPaymentStatus(AccountingConstants.PRE_PAYMENT_PARTIAL_APPLIED_STATUS);
                    } else if (paymentFirstBalance.compareTo(paymentBalance) == 0) {
                        supplierCredit.setPaymentStatus(AccountingConstants.PRE_PAYMENT_OPEN_STATUS);
                    } else {
                        supplierCredit.setPaymentStatus(AccountingConstants.PRE_PAYMENT_APPLIED_STATUS);
                    }
                    invoicePaymentManager.update(supplierCredit);
                }
            } else if (RECEIVABLE_CRM_ACCOUNT_CREDIT.equals(payment.getType()) || PAYABLE_CRM_ACCOUNT_CREDIT.equals(payment.getType())) {
                EdsCrmAccount crmAccount = payment.getCrmAccount();
                crmAccount.setAppliedCredit(crmAccount.getAppliedCredit().subtract(payment.getBaseAmount()));
                crmAccountManager.update(crmAccount);
            } else if (PAYABLE_BANK_CHECK_SHARE.equals(payment.getType())) {
                List<EdsBankCheckPaymentHistory> historyList = bankCheckPaymentHistoryManager.getBankCheckPaymentHistoryList(payment.getObjectID());
                for (EdsBankCheckPaymentHistory bch : historyList) {
                    EdsBankCheckItem bankCheckItem = bch.getBankCheckItem();
                    bankCheckItem.setUsedAsPayment(bankCheckItem.getUsedAsPayment().subtract(bch.getAmount()));
                    bankCheckPaymentHistoryManager.delete(bch);
                }
            }
        }
    }

    private Date getCompanyDate(EdsUser user) {
        Calendar companyTime = new GregorianCalendar(TimeZone
                .getTimeZone(user.getCompany().getCountryZone().getZone().getZoneID()));
        return companyTime.getTime();
    }

    public PaymentAndPrePaymentData getCustomerCreditData(Integer invoiceID, Integer crmAccountID, boolean isExpense) {

        EdsInvoice invoiceOrNote = invoiceManager.get(invoiceID);
        EdsExpenseReport edsExpenseReport = expenseReportManager.getExpenseReport(invoiceID);

        PaymentAndPrePaymentData transferData = new PaymentAndPrePaymentData();
        if (!isExpense) {
            transferData.setPayments(getPayments(invoiceOrNote));
        }
        List<PaymentData> dataList = new LinkedList<>();

        if (isExpense && edsExpenseReport != null || !isExpense && !invoiceOrNote.isCreditNote() && (invoiceOrNote instanceof EdsSaleInvoice || invoiceOrNote instanceof EdsPurchaseInvoice)) {
            boolean isClient = isExpense ? false : invoiceOrNote instanceof EdsSaleInvoice;

            EdsCrmAccount crmAccount = crmAccountManager.get(crmAccountID);
            //get pre-payment list
            List<EdsInvoicePayment> paymentList = invoicePaymentManager.getAccountPrePaymentsWithoutReversed(crmAccountID, isClient ? AccountingConstants.RECEIVABLE_PREPAYMENT : AccountingConstants.PAYABLE_SUPPLIER_CREDIT, null);
            if (paymentList != null && paymentList.size() > 0) {
                for (EdsInvoicePayment item : paymentList) {
                    if (item.getReceivablePayable() != null && (item.getReceivablePayable().getKey() == null && item.getReceivablePayable().getGroupKey() == null)) {
                        continue;
                    }

                    PaymentData pData = new PaymentData();
                    pData.setObjectID(item.getObjectID());
                    if (item.getPaymentDate() != null) {
                        pData.setDate(new DateNonConvertable(item.getPaymentDate()));
                    }
                    pData.setCrmAccount(new SelectItem(crmAccount.getObjectID(), crmAccount.getName()));
                    BigDecimal balance = item.getAmountInInvoiceCurrency() != null ? item.getAmountInInvoiceCurrency() : item.getAmount();
                    BigDecimal appliedAmount = invoicePaymentManager.getAppliedPrePaymentAmounts(item.getCrmAccount().getObjectID(), item.getObjectID(), isClient ? AccountingConstants.RECEIVABLE_PREPAYMENT_SHARE : AccountingConstants.PAYABLE_SUPPLIER_CREDIT_SHARE, isClient ? RECEIVABLE_PREPAYMENT_REFUND : PAYABLE_PREPAYMENT_REFUND);
                    balance = balance.subtract(appliedAmount);

                    if (item.getExchangeRate() != null && item.getExchangeRate().compareTo(BigDecimal.ZERO) != 0) {
                        Integer currencyId = isExpense ? edsExpenseReport.getCurrency().getObjectID() : invoiceOrNote.getCurrency().getObjectID();
                        if (currencyId.equals(item.getCurrencyID())) {
                            balance = balance.divide(isExpense ? edsExpenseReport.getExchangeRate() : invoiceOrNote.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                        } else {
                            balance = balance.divide(item.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                        }
                    }

                    pData.setPaymentAmount(balance);
                    pData.setPrepayment(true);
                    pData.setReference(item.getReference() != null ? item.getReference() : "");
                    pData.setNumber(item.getNumber() != null ? item.getNumber() : "");
                    if (balance.compareTo(BigDecimal.ZERO) > 0) {
                        if (item.getCurrencyID() == null && item.getExchangeRate().compareTo(BigDecimal.ONE) == 0) {
                            item.setCurrencyID(currencyService.getBaseCurrency().getId());
                        }
                        EdsAccount paymentAccount = accountingManager.getAccountByKey(isClient ? EdsAccount.ACCOUNTS_RECEIVABLE : EdsAccount.ACCOUNTS_PAYABLE, item.getCurrencyID());
                        if (item.getReceivablePayable() != null) {
                            pData.setPaymentAccount(item.getReceivablePayable().createAccountItem());
                        } else if (paymentAccount != null) {
                            pData.setPaymentAccount(paymentAccount.getAsSelectItem());
                        }
                        dataList.add(pData);
                    }

                    if (item.getSaleQuote() != null) {
                        pData.setSaleQuoteItem(new SelectItem(item.getSaleQuote().getObjectID(), item.getSaleQuote().getNumber()));
                    } else if (item.getPurchaseOrder() != null) {
                        pData.setPurchaseOrderItem(new SelectItem(item.getPurchaseOrder().getObjectID(), item.getPurchaseOrder().getNumber()));
                    }
                    if (item.getSaleInvoice() != null) {
                        pData.setSaleInvoiceItem(new SelectItem(item.getSaleInvoice().getObjectID(), item.getSaleInvoice().getNumber()));
                    }
                }
            }
            //get client/supplier opening balance witch was added as pre-payment
            if (crmAccount.getBalanceAmount() != null && crmAccount.getBalanceAmount().compareTo(BigDecimal.ZERO) < 0
                    || crmAccount.getSupplierBalanceAmount() != null && crmAccount.getSupplierBalanceAmount().compareTo(BigDecimal.ZERO) < 0) {

                BigDecimal appliedCredit = invoicePaymentManager.getAppliedCreditAmount(crmAccountID, isClient ? RECEIVABLE_CRM_ACCOUNT_CREDIT : PAYABLE_CRM_ACCOUNT_CREDIT);
                BigDecimal unAllocatedAmount = isClient ? crmAccount.getBalanceAmount().abs().subtract(appliedCredit)
                        : crmAccount.getSupplierBalanceAmount().abs().subtract(appliedCredit);
                if (unAllocatedAmount.compareTo(BigDecimal.ZERO) > 0) {
                    PaymentData crmAccountCredit = new PaymentData();
                    crmAccountCredit.setPaymentAmount(unAllocatedAmount);
                    if ((isClient && crmAccount.getBalanceDate() != null) || crmAccount.getSupplierBalanceDate() != null) {
                        crmAccountCredit.setDate(new DateNonConvertable(isClient ? crmAccount.getBalanceDate() : crmAccount.getSupplierBalanceDate()));
                    }
                    crmAccountCredit.setCrmAccount(crmAccount.getAsSelectItem());
                    if (isClient && crmAccount.getReceivable() != null) {
                        crmAccountCredit.setReceivablePayable(crmAccount.getReceivable().createAccountItem());
                    } else if (crmAccount.getPayable() != null) {
                        crmAccountCredit.setReceivablePayable(crmAccount.getPayable().createAccountItem());
                    }
                    crmAccountCredit.setCrmAccountCredit(true);
                    dataList.add(crmAccountCredit);
                }
            }
            //get manual transaction list that was added as pre-payment to the client/supplier
            List<TransactionAllocateItem> manualTransactions = manualJournalManager.getManualTransactionsByCrmAccount(crmAccountID, isClient, true, null, null, null);
            if (manualTransactions != null && !manualTransactions.isEmpty()) {
                for (TransactionAllocateItem item : manualTransactions) {
                    EdsManualJournal manualJournal = manualJournalManager.get(item.getObjectID());

                    BigDecimal balance = item.getAmount();
                    BigDecimal paidAmount = invoicePaymentManager.getManualPaymentAmount(item.getObjectID(), crmAccountID, isClient ? RECEIVABLE_MANUAL_CREDIT : PAYABLE_MANUAL_CREDIT);
                    balance = balance.subtract(paidAmount);

                    if (manualJournal != null) {
                        if (manualJournal.getExchangeRate() != null && manualJournal.getExchangeRate().compareTo(BigDecimal.ZERO) != 0) {

                            //There is a case(Base currency SUM). INV currency USD and also Pre Payment currency USD
                            //You had received 100 USD pre payment with 1 SUM = 0.000333 USD exchange rate, and Nowadays you want to issue a new invoice with amount 100 USD,
                            //at this time exchange rate is 1 SUM = 0.00025 USD, so if you calculate pre payment with its exchange rate 100/0.000333 * 0.00025 = 75 USD.
                            //As you saw the value, you've got 75 USD but you had got 100 USD pre payment. That is the problem! Therefore I've added the "IF" clause
                            Integer currencyId = isExpense ? edsExpenseReport.getCurrency().getObjectID() : invoiceOrNote.getCurrency().getObjectID();
                            if (currencyId.equals(manualJournal.getCurrency().getObjectID())) {
                                balance = balance.divide(isExpense ? edsExpenseReport.getExchangeRate() : invoiceOrNote.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                            } else {
                                balance = balance.divide(manualJournal.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                            }
                        }
                    }

                    if (balance.compareTo(ZERO) > 0) {
                        PaymentData pData = new PaymentData();
                        pData.setManualJournal(true);
                        pData.setManualJournalID(item.getObjectID());
                        pData.setPaymentAccount(new SelectItem(item.getAccountID()));
                        pData.setPaymentAmount(balance);
                        pData.setCrmAccount(crmAccount.getAsSelectItem());
                        if (manualJournal != null) {
                            pData.setDate(new DateNonConvertable(manualJournal.getDate()));
                            pData.setReferenceNumber(manualJournal.getNarration());
                            pData.setInvoiceNumber(manualJournal.getNumber());
                        }
                        dataList.add(pData);
                    }
                }
            }
            //get bank transfer list that was added as pre-payment to the client/supplier
            List<TransactionAllocateItem> bankTransferList = spendReceiveMoneyManager.getTransactionsByCrmAccount(crmAccountID, isClient);
            if (bankTransferList != null && !bankTransferList.isEmpty()) {
                for (TransactionAllocateItem item : bankTransferList) {
                    EdsBankTransfer bankTransfer = spendReceiveMoneyManager.get(item.getObjectID());

                    BigDecimal balance = item.getAmount();
                    BigDecimal paidAmount = invoicePaymentManager.getBankTransferPaymentAmount(item.getObjectID(), crmAccountID, isClient ? RECEIVABLE_BANKTRANSFER_CREDIT : PAYABLE_BANKTRANSFER_CREDIT);
                    balance = balance.subtract(paidAmount);

                    if (bankTransfer != null) {
                        if (bankTransfer.getExchangeRate() != null && bankTransfer.getExchangeRate().compareTo(BigDecimal.ZERO) != 0) {
                            Integer currencyId = isExpense ? edsExpenseReport.getCurrency().getObjectID() : invoiceOrNote.getCurrency().getObjectID();

                            if (bankTransfer.getCurrency() != null && currencyId.equals(bankTransfer.getCurrency().getObjectID())) {
                                balance = balance.divide(isExpense ? edsExpenseReport.getExchangeRate() : invoiceOrNote.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                            } else {
                                balance = balance.divide(bankTransfer.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                            }
                        }
                    }

                    if (balance.compareTo(ZERO) > 0) {
                        PaymentData pData = new PaymentData();
                        pData.setBankTransafer(true);
                        pData.setBankTransferID(item.getObjectID());
                        pData.setPaymentAccount(new SelectItem(item.getAccountID()));
                        pData.setPaymentAmount(balance);
                        pData.setCrmAccount(crmAccount.getAsSelectItem());
                        pData.setDate(new DateNonConvertable(bankTransfer.getDate()));
                        pData.setReferenceNumber(item.getReference());
                        pData.setNumber(item.getNumber());

                        dataList.add(pData);
                    }
                }
            }
            //get cheque list for supplier as pre-peyment
            if (!isClient) {
                List<EdsBankCheckItem> bankCheckItems = bankCheckManager.getBankCheckItemsBySupplier(crmAccountID, null);
                for (EdsBankCheckItem bci : bankCheckItems) {
                    if (bci.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                        PaymentData paymentData = new PaymentData();
                        paymentData.setObjectID(bci.getBankCheck().getObjectID());
                        paymentData.setCrmAccount(bci.getCrmAccount().getAsSelectItem());
                        paymentData.setBankCheckItem(new SelectItem(bci.getObjectID(), bci.getDescription()));
                        paymentData.setNumber(bci.getBankCheck() != null ? bci.getBankCheck().getNumber() : null);
                        paymentData.setReferenceNumber(bci.getBankCheck() != null ? bci.getBankCheck().getMemo() : null);
                        paymentData.setDate(new DateNonConvertable(bci.getBankCheck().getDate()));
                        paymentData.setPaymentAccount(bci.getAccount().getAsSelectItem());
                        paymentData.setPaymentAmount(bci.getBalance());


                        EdsBankCheck bankCheck = bci.getBankCheck();
                        if (bankCheck.getExchangeRate() != null) {
                            BigDecimal balance = bci.getBalance();
                            Integer currencyId = isExpense ? edsExpenseReport.getCurrency().getObjectID() : invoiceOrNote.getCurrency().getObjectID();

                            if (bankCheck.getCurrency() != null && currencyId.equals(bankCheck.getCurrency().getObjectID())) {
                                balance = balance.divide(isExpense ? edsExpenseReport.getExchangeRate() : invoiceOrNote.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                            } else {
                                balance = balance.divide(bankCheck.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                            }
                            paymentData.setPaymentAmount(balance);
                        }
                        dataList.add(paymentData);
                    }
                }
            }
            transferData.setCredits(dataList.toArray(new PaymentData[]{}));
        }
        return transferData;
    }

    @Override
    public void saveItemTableItems(ColumnConfigs item, Integer accountId, Integer discountId, String oldTitleName, ItemTableEnum itemTableEnum) {
        if (item != null) {
            ColumnConfigs[] itemTableColumns = itemTableSettingService.getColumnConfigs(itemTableEnum, true);
            EdsItemTableSettings its = itemTableSettingsManager.getSettingsBySection(itemTableEnum);
            LinkedList<ColumnConfigs> columns = new LinkedList<>();
            if (itemTableColumns != null && itemTableColumns.length > 0 && item.getCode() != null) {
                for (ColumnConfigs columnConfigs : itemTableColumns) {
                    if (columnConfigs != null && item.getCode().equals(columnConfigs.getCode())) {
                        columnConfigs.setTitle(item.getTitle());
                        columnConfigs.setRequired(item.isSelected());
                        columnConfigs.setDisabled(item.isDisabled());
                        columnConfigs.setMinValue(item.getMinValue());
                        columnConfigs.setHasDefault(accountId != null);
                        if (!oldTitleName.equals(item.getTitle())) {
                            columnConfigs.setChanged(true);
                        }
                        columnConfigs.setAllowedRoles(item.getAllowedRoles());
                        columnConfigs.setAllowedRolesView(item.getAllowedRolesView());
                        columnConfigs.setAllowedRolesDisabled(item.getAllowedRolesDisabled());
                    }
                    columns.add(columnConfigs);
                }
                Gson gson = new Gson();
                ColumnConfigs[] columnConfigs = columns.toArray(new ColumnConfigs[]{});
                its.setSettingsJSONData(gson.toJson(columnConfigs));
                itemTableSettingsManager.createOrUpdate(its);

                String key = CacheConstants.ITEM_TABLE_SECTION + "_" + itemTableEnum.getTitle() + "_" + SecurityContext.getCompanyID();
                RedisClient.removeKey(key);
                RedisClient.setKey(key, columnConfigs, ColumnConfigs[].class);
            }

            EdsCompany company = accountingManager.getUser().getCompany();
            EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(company);
            if (discountId == null) discountId = 0;
            if (ItemTableEnum.SALE_QUOTE_ITEM.equals(itemTableEnum)) {
                invoicingSettings.setDefDiscountSQ(discountId);
            } else if (ItemTableEnum.SALE_ORDER_ITEM.equals(itemTableEnum)) {
                invoicingSettings.setDefDiscountSO(discountId);
            } else if (ItemTableEnum.SALE_INVOICE_ITEM.equals(itemTableEnum)) {
                invoicingSettings.setDefDiscountSI(discountId);
            } else if (ItemTableEnum.PURCHASE_ORDER_ITEM.equals(itemTableEnum)) {
                invoicingSettings.setDefDiscountPO(discountId);
            } else if (ItemTableEnum.PURCHASE_INVOICE_ITEM.equals(itemTableEnum)) {
                invoicingSettings.setDefDiscountPI(discountId);
            }

            if (accountId != null) {
                EdsAccount selectedAccount = accountingManager.get(accountId);
                if (selectedAccount != null) {
                    if (ItemTableEnum.SALE_QUOTE_ITEM.equals(itemTableEnum)) {
                        invoicingSettings.setDefAccountSQ(selectedAccount);
                    } else if (ItemTableEnum.SALE_ORDER_ITEM.equals(itemTableEnum)) {
                        invoicingSettings.setDefAccountSO(selectedAccount);
                    } else if (ItemTableEnum.SALE_INVOICE_ITEM.equals(itemTableEnum)) {
                        invoicingSettings.setDefAccountSI(selectedAccount);
                    } else if (ItemTableEnum.PURCHASE_ORDER_ITEM.equals(itemTableEnum)) {
                        invoicingSettings.setDefAccountPO(selectedAccount);
                    } else if (ItemTableEnum.PURCHASE_INVOICE_ITEM.equals(itemTableEnum)) {
                        invoicingSettings.setDefAccountPI(selectedAccount);
                    }
                }
            } else {
                if (ItemTableEnum.SALE_QUOTE_ITEM.equals(itemTableEnum)) {
                    invoicingSettings.setDefAccountSQ(null);
                } else if (ItemTableEnum.SALE_ORDER_ITEM.equals(itemTableEnum)) {
                    invoicingSettings.setDefAccountSO(null);
                } else if (ItemTableEnum.SALE_INVOICE_ITEM.equals(itemTableEnum)) {
                    invoicingSettings.setDefAccountSI(null);
                } else if (ItemTableEnum.PURCHASE_ORDER_ITEM.equals(itemTableEnum)) {
                    invoicingSettings.setDefAccountPO(null);
                } else if (ItemTableEnum.PURCHASE_INVOICE_ITEM.equals(itemTableEnum)) {
                    invoicingSettings.setDefAccountPI(null);
                }
            }
        }
    }

    @Override
    public SelectItem getItemTableDefaultAccount(CustomFieldSection section) {
        EdsCompany company = accountingManager.getUser().getCompany();
        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(company);
        SelectItem item = new SelectItem();
        if (invoicingSettings != null && section != null) {
            if (CustomFieldSection.SaleQuoteItem.equals(section) && invoicingSettings.getDefAccountSQ() != null) {
                item.setId(invoicingSettings.getDefAccountSQ().getObjectID());
                item.setName(invoicingSettings.getDefAccountSQ().getAccountCode() + "->" + invoicingSettings.getDefAccountSQ().getName());
            } else if (CustomFieldSection.SaleOrderItem.equals(section) && invoicingSettings.getDefAccountSO() != null) {
                item.setId(invoicingSettings.getDefAccountSO().getObjectID());
                item.setName(invoicingSettings.getDefAccountSO().getAccountCode() + "->" + invoicingSettings.getDefAccountSO().getName());
            } else if (CustomFieldSection.SaleInvoiceItem.equals(section) && invoicingSettings.getDefAccountSI() != null) {
                item.setId(invoicingSettings.getDefAccountSI().getObjectID());
                item.setName(invoicingSettings.getDefAccountSI().getAccountCode() + "->" + invoicingSettings.getDefAccountSI().getName());
            } else if (CustomFieldSection.PurchaseOrderItem.equals(section) && invoicingSettings.getDefAccountPO() != null) {
                item.setId(invoicingSettings.getDefAccountPO().getObjectID());
                item.setName(invoicingSettings.getDefAccountPO().getAccountCode() + "->" + invoicingSettings.getDefAccountPO().getName());
            } else if (CustomFieldSection.PurchaseInvoiceItem.equals(section) && invoicingSettings.getDefAccountPI() != null) {
                item.setId(invoicingSettings.getDefAccountPI().getObjectID());
                item.setName(invoicingSettings.getDefAccountPI().getAccountCode() + "->" + invoicingSettings.getDefAccountPI().getName());
            }
        }
        return item;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PaymentItem[] getPayments(EdsInvoice invoiceOrNote) {
        List<EdsInvoicePayment> paymentsOrRefunds;
        if (invoiceOrNote.isCreditNote()) {
            paymentsOrRefunds = invoicePaymentManager.getRefunds(invoiceOrNote);
        } else {
            paymentsOrRefunds = invoicePaymentManager.getPayments(invoiceOrNote);
        }
        PaymentItem[] items = new PaymentItem[paymentsOrRefunds.size()];
        int i = 0;
        for (EdsInvoicePayment p : paymentsOrRefunds) {
            items[i] = p.getPaymentAsRPC();
            items[i].setStatusText("Partially paid");
            i++;
        }
        if (invoiceOrNote.getStatus().equals(referenceManager.findReference(INVOICE_STATUS, PAID))) {
            if (items.length > 0) {
                items[0].setStatusText("Paid");
            }//just check it, why it is set only to the zero element.
        }
        return items;
    }

    @Override
    public Integer saveGatewayPaymentData(PaymentData paymentData, String gatewayType) {
        EdsInvoice invoice = invoiceManager.get(paymentData.getInvoiceID());
        EdsUser user = invoice.getCreator();
        EdsCompany company = user.getCompany();
        ServerSecurityContext.getInstance().setStaticUserID(user.getObjectID());

        log.info("PAYMENT GATEWAY:" + gatewayType);

        String invoiceStatus = invoice.getStatus().getCode();
        if (!(APPROVE.equals(invoiceStatus) || OPEN.equals(invoiceStatus) || OVER_DUE.equals(invoiceStatus))) {
            log.info("GATEWAY PAYMENT CANCELLED: INVOICE STATUS IS NOT VALID. STATUS: " + invoiceStatus);
            return null;
        }

        if (invoice.getDueAmount().setScale(2, RoundingMode.HALF_UP).compareTo(paymentData.getPaymentAmount().setScale(2, RoundingMode.HALF_UP)) < 0) {
            log.info("GATEWAY PAYMENT CANCELLED: PAYMENT AMOUNT IS MORE THAN DUE AMOUNT");
            return null;
        }

        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(company);

        if (STRIPE_PAYMENT.equals(gatewayType)) {
            if (invoicingSettings.getStripePaymentAccount() == null) {
                log.info(gatewayType + " ACCOUNT IS NOT SELECTED");
                return null;
            }
            paymentData.setDate(new DateNonConvertable(user.getUserDate()));
            paymentData.setPaymentAccount(invoicingSettings.getStripePaymentAccount().getAsSelectItem());
        } else if (MASTERCARD_PAYMENT.equals(gatewayType)) {
            if (invoicingSettings.getMasterCardPaymentAccount() == null) {
                log.info(gatewayType + " ACCOUNT IS NOT SELECTED");
                return null;
            }
            paymentData.setDate(new DateNonConvertable(user.getUserDate()));
            paymentData.setPaymentAccount(invoicingSettings.getMasterCardPaymentAccount().getAsSelectItem());
        } else if (PAYPAL_PAYMENT.equals(gatewayType)) {
            if (invoicingSettings.getPayPalPaymentAccount() == null) {
                log.info(gatewayType + " ACCOUNT IS NOT SELECTED");
                return null;
            }
            paymentData.setPaymentAccount(invoicingSettings.getPayPalPaymentAccount().getAsSelectItem());
        } else if (GOOGLE_CHECKOUT_PAYMENT.equals(gatewayType)) {
            if (invoicingSettings.getGoogleCheckoutPaymentAccount() == null) {
                log.info(gatewayType + " ACCOUNT IS NOT SELECTED");
                return null;
            }
            paymentData.setPaymentAccount(invoicingSettings.getGoogleCheckoutPaymentAccount().getAsSelectItem());
        } else if (ELAVON_PAYMENT.equals(gatewayType)) {
            if (invoicingSettings.getElavonPaymentAccount() == null) {
                log.info(gatewayType + " ACCOUNT IS NOT SELECTED");
                return null;
            }
            paymentData.setPaymentAccount(invoicingSettings.getElavonPaymentAccount().getAsSelectItem());
        }
        ReceivePaymentData receivePaymentData = new ReceivePaymentData();
        receivePaymentData.setBatchPayment(true);

        receivePaymentData.setCrmAccount(invoice.getClientOrSupplier().getAsSelectItem());
        receivePaymentData.setAccount(paymentData.getPaymentAccount());
        receivePaymentData.setExRate(paymentData.getExchangeRate());
        receivePaymentData.setCurrency(invoice.getCurrency().createCurrencyItem());
        receivePaymentData.setReference(paymentData.getReferenceNumber());
        receivePaymentData.setDate(paymentData.getDate());
        receivePaymentData.setTotalAmount(paymentData.getPaymentAmount());
        receivePaymentData.setPayments(new PaymentData[]{paymentData});
        receivePaymentData.setType(paymentData.getType());
        receivePaymentData.setPaymentTarget(AccountingConstants.PAYMENT_TARGET_INVOICE);

        BatchPaymentResult bpResult = saveReceivePaymentData(receivePaymentData, true);

//        Integer invoicePaymentID = savePayment(paymentData);
        log.info(gatewayType + " PROCESSED SUCCESSFULLY");

        return bpResult.getResult();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceNumberData getSaleInvoiceNumber() {
        return getSaleInvoiceNumber(null, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceNumberData getSaleInvoiceNumber(DateNonConvertable invoiceDate) {
        return invoiceCircularResolver.getInvoiceNumberData(null, null, invoiceDate);
    }

    private InvoiceNumberData getSaleInvoiceNumber(EdsCompany company) {
        return getSaleInvoiceNumber(company, null);
    }

    public InvoiceNumberData getSaleInvoiceNumber(String customPrefix) {
        return getSaleInvoiceNumber(null, customPrefix);
    }

    private InvoiceNumberData getSaleInvoiceNumber(EdsCompany company, String customPrefix) {
        return invoiceCircularResolver.getInvoiceNumberData(company, customPrefix);
    }

    public InvoiceNumberData getPurchaseInvoiceNumber(boolean isPICreditNote) {
        return invoiceCircularResolver.getPurchaseInvoiceNumberData(isPICreditNote);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PaymentItem getPaymentOrRefund(Integer paymentId, boolean isRefund) {
        boolean isMultiCurrencyEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTICURRENCY_ENABLED);

        EdsInvoicePayment invPayment = invoicePaymentManager.get(paymentId);
        PaymentItem item = getPaymentOrRefundDetails(invPayment, isRefund, isMultiCurrencyEnabled);

        if (invPayment.getType().equals(RECEIVABLE_PREPAYMENT) && invPayment.getSaleQuote() != null) {
            item.setProject(invPayment.getSaleQuote().getRelatedProject() != null ? invPayment.getSaleQuote().getRelatedProject().getAsSelectItem() : null);
        }

        if (invPayment.getType().equals(PAYABLE_SUPPLIER_CREDIT) && invPayment.getPurchaseOrder() != null) {
            item.setProject(invPayment.getPurchaseOrder().getRelatedProject() != null ? invPayment.getPurchaseOrder().getRelatedProject().getAsSelectItem() : null);
        }

        if (invPayment.getPrepaymentCustomFields() != null) {
            item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(invPayment.getPrepaymentCustomFields(), commonService.getCompanyCustomFields(RECEIVABLE_PREPAYMENT.equals(item.getType()) ? ViewName.Prepayment : ViewName.Supplier)));
        }


        EdsInvoicePaymentTransaction transaction = transactionManager.getTransactionByPayment(invPayment);
        if (transaction != null) {
            item.setJournalID(transaction.getJournalId());
        }
        EdsCustomerPrepaymentNote note = invoicePaymentManager.getPrepaymentNote(paymentId);
        if (note != null && note.getComment() != null) {
            item.setNote(note.getComment());
        }

        if (RECEIVABLE_PREPAYMENT.equals(invPayment.getType())) {
            item.setTemplates(getCompanyPdfTemplates(PREPAYMENT).getItems());
            if (invPayment.getSaleQuote() != null) {
                item.setSaleQuoteItem(new SelectItem(invPayment.getSaleQuote().getObjectID(), invPayment.getSaleQuote().getNumber()));
            }
            if (invPayment.getSaleInvoice() != null) {
                item.setSaleInvoiceItem(new SelectItem(invPayment.getSaleInvoice().getObjectID(), invPayment.getSaleInvoice().getNumber()));
            }
            if (invPayment.getRentalOrder() != null) {
                SelectItem rentalOrderItem = new SelectItem(invPayment.getRentalOrder().getObjectID(), invPayment.getRentalOrder().getNumber());
                rentalOrderItem.setDescription(RENTAL_ORDERS);
                item.setRentalOrderItem(rentalOrderItem);
            }
        } else if (PAYABLE_SUPPLIER_CREDIT.equals(invPayment.getType())) {
            item.setTemplates(getCompanyPdfTemplates(SUPPLIER_CREDIT).getItems());
            if (invPayment.getPurchaseOrder() != null) {
                item.setPurchaseOrderItem(new SelectItem(invPayment.getPurchaseOrder().getObjectID(), invPayment.getPurchaseOrder().getNumber()));
            }
        }

        if (RECEIVABLE_PREPAYMENT.equals(invPayment.getType()) || PAYABLE_SUPPLIER_CREDIT.equals(invPayment.getType())) {
            ArrayList<PaymentItem> appliedPayments = new ArrayList<>();
            List<EdsInvoicePayment> payments = invoicePaymentManager.getAppliedPrepayments(item.getCrmAccount().getId(), item.getObjectId(), RECEIVABLE_PREPAYMENT.equals(invPayment.getType()) ? RECEIVABLE_PREPAYMENT_SHARE : PAYABLE_SUPPLIER_CREDIT_SHARE);
            BigDecimal appliedAmount = invoicePaymentManager.getAppliedPrePaymentAmount(item.getCrmAccount().getId(), item.getObjectId(), RECEIVABLE_PREPAYMENT.equals(invPayment.getType()) ? RECEIVABLE_PREPAYMENT_SHARE : PAYABLE_SUPPLIER_CREDIT_SHARE);
            for (EdsInvoicePayment payment : payments) {
                PaymentItem paymentItem = getPaymentOrRefundDetails(payment, false, isMultiCurrencyEnabled);

                if (isMultiCurrencyEnabled && !invPayment.getCurrencyID().equals(paymentItem.getCurrency().getId())) {
                    paymentItem.setAmount(payment.getBaseAmount().multiply(invPayment.getExchangeRate()));
                }

                appliedPayments.add(paymentItem);
            }
            item.setAppliedPayments(appliedPayments);
            item.setAppliedPaymentAmount(appliedAmount);

            ArrayList<PaymentItem> refundPayment = new ArrayList<>();
            List<EdsInvoicePayment> refundPayments = invoicePaymentManager.getAppliedPrepayments(item.getCrmAccount().getId(), item.getObjectId(), RECEIVABLE_PREPAYMENT.equals(invPayment.getType()) ? RECEIVABLE_PREPAYMENT_REFUND : PAYABLE_PREPAYMENT_REFUND);
            BigDecimal refundAmount = invoicePaymentManager.getRefundPrePaymentAmount(item.getCrmAccount().getId(), item.getObjectId(), RECEIVABLE_PREPAYMENT.equals(invPayment.getType()) ? RECEIVABLE_PREPAYMENT_REFUND : PAYABLE_PREPAYMENT_REFUND);
            for (EdsInvoicePayment payment : refundPayments) {
                PaymentItem paymentItem = getPaymentOrRefundDetails(payment, false, isMultiCurrencyEnabled);

                if (payment.getPaymentRefundID() != null) {
                    EdsPaymentRefund paymentRefund = paymentRefundManager.get(payment.getPaymentRefundID());
                    paymentItem.setInvoice(new SelectItem(paymentRefund.getObjectID(), paymentRefund.getNumber()));
                    paymentItem.setInvoiceDate(paymentRefund.getPaymentDate());
                    paymentItem.setInvoiceTotal(paymentRefund.getTotalInBase());
                    paymentItem.setInvoiceType(paymentRefund.getType());
                    paymentItem.setCloseAmount(payment.getClosedAmount());
                }
                if (isMultiCurrencyEnabled && !invPayment.getCurrencyID().equals(paymentItem.getCurrency().getId())) {
                    paymentItem.setAmount(payment.getBaseAmount().multiply(invPayment.getExchangeRate()));
                }

                refundPayment.add(paymentItem);
            }
            item.setRefundPayments(refundPayment);
            item.setRefundPaymentAmount(refundAmount);
        }
        if (invPayment.getAppliedPayment() != null) {
            if ((RECEIVABLE_PREPAYMENT_SHARE.equals(invPayment.getType()) || PAYABLE_SUPPLIER_CREDIT_SHARE.equals(invPayment.getType())) && invPayment.getAppliedPayment().getAccount() != null) {
                item.setPaidToID(invPayment.getAppliedPayment().getAccount().getObjectID());
                item.setPaidTo(invPayment.getAppliedPayment().getAccount().getName());
            }
            item.setNumber(invPayment.getAppliedPayment().getNumber());
            if (invPayment.getAppliedPayment().getSaleQuote() != null && invPayment.getAppliedPayment().getSaleQuote().getObjectID() != null && invPayment.getAppliedPayment().getSaleQuote().getNumber() != null) {
                item.setSaleQuoteItem(new SelectItem(invPayment.getAppliedPayment().getSaleQuote().getObjectID(), invPayment.getAppliedPayment().getSaleQuote().getNumber()));
            }
            if (invPayment.getAppliedPayment().getPurchaseOrder() != null && invPayment.getAppliedPayment().getPurchaseOrder().getObjectID() != null && invPayment.getAppliedPayment().getPurchaseOrder().getNumber() != null) {
                item.setPurchaseOrderItem(new SelectItem(invPayment.getAppliedPayment().getPurchaseOrder().getObjectID(), invPayment.getAppliedPayment().getPurchaseOrder().getNumber()));
            }
            if (invPayment.getAppliedPayment().getSaleInvoice() != null && invPayment.getAppliedPayment().getSaleInvoice().getObjectID() != null && invPayment.getAppliedPayment().getSaleInvoice().getNumber() != null) {
                item.setSaleInvoiceItem(new SelectItem(invPayment.getAppliedPayment().getSaleInvoice().getObjectID(), invPayment.getAppliedPayment().getSaleInvoice().getNumber()));
            }
        } else {
            item.setNumber(invPayment.getNumber());
        }
        if (invPayment.getCrmAccount() != null) {
            if (RECEIVABLE_PREPAYMENT.equals(invPayment.getType())) {
                item.setSupplierCustomerBalance(crmAccountManager.getClientBalance(invPayment.getCrmAccount().getObjectID()));
            } else {
                item.setSupplierCustomerBalance(crmAccountManager.getSupplierBalance(invPayment.getCrmAccount().getObjectID()));
            }
        }
        return item;
    }

    private PaymentItem getPaymentOrRefundDetails(EdsInvoicePayment invoicePayment, boolean isRefund,
                                                  boolean isMultiCurrencyEnabled) {
        EdsInvoice invoiceOrNote = isRefund ? invoicePayment.getCreditNote() : invoicePayment.getInvoice();
        PaymentItem item = invoicePayment.getPaymentAsRPC();

        if (invoicePayment.getCurrencyID() != null) {
            item.setCurrency(currencyManager.getCurrency(invoicePayment.getCurrencyID()).getAsSelectItem());
        } else {
            item.setCurrency(getBaseCurrency());
        }
        item.setExchangeRate(invoicePayment.getExchangeRate() != null ? invoicePayment.getExchangeRate() : BigDecimal.ONE);

        if (invoiceOrNote != null) {
            if (invoiceOrNote.getInvoiceDate() != null) {
                item.setInvoiceDate(new Date(invoiceOrNote.getInvoiceDate().getTime()));
            }
            if (invoiceOrNote.getDueDate() != null) {
                item.setInvoiceDueDate(new Date(invoiceOrNote.getDueDate().getTime()));
            }
            item.setInvoiceTotal(isMultiCurrencyEnabled ? invoiceOrNote.getTotalInInvoiceCurrency() : invoiceOrNote.getTotal());
            item.setInvoiceType(invoiceOrNote.getType());
        }
        EdsExpenseReport edsExpenseReport = expenseReportManager.getExpenseReport(invoicePayment.getExpenseID());
        if (edsExpenseReport != null) {
            item.setExpense(new SelectItem(edsExpenseReport.getObjectID(), edsExpenseReport.getNumber()));
            item.setInvoiceTotal(isMultiCurrencyEnabled ? edsExpenseReport.getTotal() : edsExpenseReport.getBaseTotal());
            if (edsExpenseReport.getStartDate() != null) {
                item.setInvoiceDate(new Date(edsExpenseReport.getStartDate().getTime()));
            }
        }
        item.setReceivablePayable(invoicePayment.getReceivablePayable() != null ? invoicePayment.getReceivablePayable().getAsSelectItem() : null);
        item.setBankFee(invoicePayment.getBankFee() != null ? invoicePayment.getBankFee().getAsSelectItem() : null);
        item.setBankFeeType(invoicePayment.getBankFeeType());
        item.setBankFeeValue(invoicePayment.getBankFeeValue());
//        item.setAmount(isMultiCurrencyEnabled ? invoicePayment.getAmount() : invoicePayment.getAmount().divide(invoicePayment.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        item.setReversed(invoicePaymentManager.isReversed(invoicePayment.getObjectID()));
        item.setType(invoicePayment.getType());

        return item;
    }

    public Integer sendToClient(MessageItem messageItem) {//We have only an access send to Client not to Supplier.
        EdsUser user = messageItem.getSenderID() != null ? userManager.get(messageItem.getSenderID()) : userManager.getUser();
        EdsInvoice invoice = invoiceManager.get(messageItem.getInvoiceID());
        NewInvoice data = EdsInvoice.getInvoiceData(invoice);
        data.setClientMessage(messageItem.getMailContent());
        EdsCrmAccount client = clientManager.get(data.getClientID());

        if (invoice.getStatus() != null && DRAFT.equals(invoice.getStatus().getCode()) || INVOICE_STATUS_PENDING.equals(invoice.getStatus().getCode())) {
            accountingServiceLocal.createTransactionsForInvoice(invoice, user);
        }
        Integer trackerID = null;
        if (messageItem.isReceipt()) {
            if (PROJECT_BASE_INVOICE_CATEGORY.equals(messageItem.getType())) {
                trackerID = super.initDataForSending(messageItem, invoice, data, savedProjectBaseInvoiceViewPDFHandler.getPDFStream(new RequestObject(messageItem.getInvoiceID())), savedProjectBaseInvoiceViewPDFHandler.getFileName(), client, "Receipt Invoice");
            } else {
                trackerID = super.initDataForSending(messageItem, invoice, data, salesReceiptViewPDFHandler.getPDFStream(new RequestObject(messageItem.getInvoiceID())), salesReceiptViewPDFHandler.getFileName(), client, "Receipt Invoice");
            }
        } else {
            InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(messageItem.getInvoiceID(), messageItem.getPdfTemplateID(), messageItem.getSenderID(), messageItem.getContactId());
            if (CREDIT_NOTE_CATEGORY.equals(messageItem.getType())) {
                if (RECEIVABLE.equals(invoice.getType())) {
                    trackerID = super.initDataForSending(messageItem, invoice, data, savedReceivableCreditNoteViewPDFHandler.getPDFStream(requestObject), savedReceivableCreditNoteViewPDFHandler.getFileName(), client, "Invoice");
                } else {
                    trackerID = super.initDataForSending(messageItem, invoice, data, savedPayableCreditNoteViewPDFHandler.getPDFStream(requestObject), savedPayableCreditNoteViewPDFHandler.getFileName(), client, "Invoice");
                }
            } else {
                if (PROJECT_BASE_INVOICE_CATEGORY.equals(messageItem.getType())) {
                    trackerID = super.initDataForSending(messageItem, invoice, data, savedProjectBaseInvoiceViewPDFHandler.getPDFStream(requestObject), savedProjectBaseInvoiceViewPDFHandler.getFileName(), client, "Invoice");
                } else {
                    trackerID = super.initDataForSending(messageItem, invoice, data, savedSaleInvoiceViewPDFHandler.getPDFStream(requestObject), savedSaleInvoiceViewPDFHandler.getFileName(), client, "Invoice");
                }
            }
        }
        if (messageItem.getRelations() != null && messageItem.getRelations().size() > 0) {
            allInOneServiceLocal.saveRelations(RelationItem.TYPE_EMAIL_TRACKER, trackerID, messageItem.getSubject(), messageItem.getRelations());
        }
        addInvoiceToSolr(invoice);

        if (SALES_INVOICE_CATEGORY.equals(messageItem.getType()) && invoice.getConvertedQuotes() != null && invoice.getConvertedQuotes().size() > 0) {
            for (EdsQuote quotes : invoice.getConvertedQuotes()) {
                EdsSaleQuote quote = (EdsSaleQuote) quotes;
                if (quote != null && !quote.isProgressInvoicing()) {
                    quote.setStatus(super.getInvoiceStatus(INVOICED));
                    quoteManager.update(quote);
                    addSaleQuoteToSolr(quote);
                }
            }
        }

        //Register event in MyUpdate
        if (invoice instanceof EdsSaleInvoice && trackerID != null) {
            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(SaleInvoiceEventListenerImpl.TYPE, SaleInvoiceEventListenerImpl.EVENT_SALES_INVOICE_SEND_TO_CLIENT, (EdsSaleInvoice) invoice, user);
            if (messageItem.getContactId() != null) {
                event.setAdditionalSourceID(messageItem.getContactId());
            }
        }
        return trackerID;
    }

    @Override
    public void sendMastercardReceiptToClient(Integer invoiceID) {
        EdsSaleInvoice invoice = invoiceManager.getSaleInvoice(invoiceID);
        EdsEmailTemplate emailTemplate = emailTemplateManager.getDefaultEmailTemplateByCategory(RECEIPT_CATEGORY);

        EdsCrmContact contact = invoice.getClientContact();
        if (contact == null) {
            EdsCrmContact primaryContact = invoice.getClientOrSupplier().getPrimaryContact();
            if (primaryContact != null && !primaryContact.isDeleted()) {
                contact = primaryContact;
            }
        }

        if (emailTemplate != null && contact != null) {
            MessageItem messageItem = new MessageItem();
            messageItem.setAccess(contact.isAccessEnabled());
            messageItem.setClient(true);
            messageItem.setInvoiceID(invoiceID);
            messageItem.setSendCopyToMe(true);

            EntityToEmailTemplate entityToEmailTemplateItem = new EntityToEmailTemplate();
            entityToEmailTemplateItem.setEntityId(invoiceID);
            entityToEmailTemplateItem.setEntityType(RECEIPT_CATEGORY);
            entityToEmailTemplateItem.setMailReceiverId(contact.getObjectID());
            entityToEmailTemplateItem.setEmailTemplateId(emailTemplate.getObjectID());

            EmailTemplateItem generatedTemplateItem = emailTemplateService.generateEmailTemplateData(entityToEmailTemplateItem, invoiceManager.getUser().getObjectID());

            messageItem.setSubject(generatedTemplateItem.getSubject());
            messageItem.setMailContent(generatedTemplateItem.getMessageHTML());
            messageItem.setContactId(contact.getObjectID());
            messageItem.setReceipt(true);
            messageItem.setEmailTemplateID(emailTemplate.getObjectID());
            messageItem.setPdfTemplateID(null);
            messageItem.setType(RECEIPT_CATEGORY);

            sendToClient(messageItem);

            log.info("MASTERCARD_AUTO_RECEIPT_SEND_TO_CLIENT. CONTACT:" + contact.getPrimaryEmail());
        } else {
            log.info("CAN NOT SEND MASTERCARD_AUTO_RECEIPT.");
            if (emailTemplate == null) {
                log.info("MASTERCARD_AUTO_RECEIPT EMAIL TEMPLATE IS NULL.");
            }
            if (emailTemplate == null) {
                log.info("MASTERCARD_AUTO_RECEIPT CONTACT IS NULL.");
            }
        }
    }

    public void saveSendToClientDetails(MessageItem messageItem) {
        EdsRecurringInvoice recurringInvoice = (EdsRecurringInvoice) invoiceManager.get(messageItem.getInvoiceID());
        if (messageItem.getContactId() != null) {
            recurringInvoice.setContact(crmContactManager.get(messageItem.getContactId()));
            recurringInvoice.setClientContact(crmContactManager.get(messageItem.getContactId()));
        }
        recurringInvoice.setEmailSubject(messageItem.getSubject());
        if (messageItem.getEmailTemplateID() != null) {
            recurringInvoice.setEmailTemplate(emailTemplateManager.get(messageItem.getEmailTemplateID()));
        }
        if (messageItem.getPdfTemplateID() != null) {
            recurringInvoice.setPdfTemplate(companyPdfTemplateManager.get(messageItem.getPdfTemplateID()));
        }
        recurringInvoice.setSendCopyToMe(messageItem.isSendCopyToMe());
        recurringInvoice.setStatus(getInvoiceStatus(OPEN));
        recurringInvoice.setToEmails(messageItem.getToEmails());
        recurringInvoice.setCc(messageItem.getCc());
        recurringInvoice.setBcc(messageItem.getBcc());
        recurringInvoice.setFromEmail(messageItem.getFromEmail());
        recurringInvoice.setReplyTo(messageItem.getReplyTo());
        if (messageItem.getFileResources() != null && messageItem.getFileResources().size() > 0) {
            List<EdsUpload> uploads = new ArrayList<>();
            for (FileResource fileResource : messageItem.getFileResources()) {
                EdsUpload upload = (EdsUpload) uploadManager.get(fileResource.getObjectId());
                if (upload != null) {
                    uploads.add(upload);
                }
            }
            recurringInvoice.setFileIDs(uploads);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getSaleInvoiceData(ListingFilterParameter filterParametrs) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSaleInvoice.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get invoice list");
        return invoiceCircularResolver.getSaleInvoiceData(filterParametrs);
    }

    public InvoiceList getSaleInvoiceDataByCategoryId(ListingFilterParameter filterParameter) {
        List<EdsSaleInvoice> invoicesByCategoryId = invoiceManager.getInvoicesByCategoryId(filterParameter.getCategoryID());
        List<NewInvoice> collect = invoicesByCategoryId.stream().map(EdsSaleInvoice::getInvoiceData).collect(Collectors.toList());
        return new InvoiceList((ArrayList<NewInvoice>) collect, collect.size());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getInvoicesForConversionBalance(boolean isSaleInvoice) {
        return invoiceCircularResolver.getInvoicesForConversionBalance(isSaleInvoice);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getSaleInvoiceDataForRecurrenceJob(Integer employeeId) {
        EdsEmployee employee = employeeManager.get(employeeId);
        if (employee != null && employee.getCompany().getActive() && !employee.getDeleted()) {
            ListingFilterParameter filterParametrs = new ListingFilterParameter();
            filterParametrs.setInvoiceStatusId(referenceManager.findReference(INVOICE_STATUS, OVER_DUE).getObjectID());
            return invoiceCircularResolver.getSaleInvoiceDataForRecurrenceJob(filterParametrs, employeeId);
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList[] getSaleInvoiceDataForRecurrenceJobForEveryClient(Integer employeeId) {
        EdsEmployee employee = employeeManager.get(employeeId);
        if (employee != null && employee.getCompany().getActive() && !employee.getDeleted()) {
            ListingFilterParameter filterParametrs = new ListingFilterParameter();
            filterParametrs.setInvoiceStatusId(referenceManager.findReference(INVOICE_STATUS, OVER_DUE).getObjectID());
            return invoiceCircularResolver.getSaleInvoiceDataForRecurrenceJobForEveryClient(filterParametrs, employeeId);
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<RecurringInvoiceListItem> getRecurringInvoiceData(ListingFilterParameter filterParametrs) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsRecurringInvoice.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get recurring invoice list");
        return invoiceCircularResolver.getRecurringInvoiceData(filterParametrs);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<RecurringInvoiceListItem> getRecurringBillData(ListingFilterParameter filterParametrs) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsRecurringInvoice.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get recurring bill list");
        return invoiceCircularResolver.getRecurringBillData(filterParametrs);
    }

    public void sendMailToAccountants(InvoiceList data, Integer companyId, Integer recurrenceId) {
        if (recurrenceId != null && companyId != null) {
            List<EdsOverdueInvoiceReminderSettings> settingses = overdueInvoiceReminderSettingsManager.getReminderSettingsByRecurrenceId(companyId, recurrenceId);
            if (settingses != null && settingses.size() > 0) {
                List<EdsUser> users = new ArrayList<>();
                for (EdsOverdueInvoiceReminderSettings edsSettings : settingses) {
                    if (edsSettings != null && edsSettings.getRole() != null) {
                        users.addAll(employeeManager.getUserByRole(edsSettings.getRole().getObjectID()));
                    }
                }
                ArrayList<Integer> sentUsersId = new ArrayList<>();
                for (EdsUser edsUser : users) {
                    if (edsUser != null && !sentUsersId.contains(edsUser.getObjectID())) {
                        sentUsersId.add(edsUser.getObjectID());
                        try {
                            EdsReference activeStatus = referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE);
                            if (!edsUser.getDeleted() && activeStatus.equals(edsUser.getAccountStatus())) {
                                messageManager.sendOverdueInvoiceReminder(data, edsUser, invoiceCircularResolver.getBaseCurrency(edsUser.getCompany().getObjectID()).getName());
                            }
                        } catch (EdsDbException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void sendMailFromAccountantsToClients(InvoiceList[] data, Integer employeeId) {
        try {
            EdsUser user = employeeManager.get(employeeId);
            EdsEmailTemplate companyDefaultEmailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(OVERDUE_INVOICE_REMINDER_FOR_CLIENT_CATEGORY);
            if (companyDefaultEmailTemplate != null) {
                for (InvoiceList invoiceListForClient : data) {
                    if (invoiceListForClient != null && invoiceListForClient.getList() != null && invoiceListForClient.getList().size() > 0) {
                        for (NewInvoice newInvoice : invoiceListForClient.getList()) {
                            if (!newInvoice.isCreditNote()) {
                                EmailTemplateItem emailTemplateItem = emailTemplateServiceLocal.getOverdueReminderForClientTemplateItem(companyDefaultEmailTemplate, newInvoice, user);
                                String to = emailTemplateItem.getToEmail();
                                String subject = "Invoice overdue reminder " + emailTemplateItem.getSubject();
                                String text = emailTemplateItem.getMessageHTML();
                                if (!"".equals(to)) {
                                    messageManager.registerInternalMessageBasic(to, subject, text, user.getCompany().getObjectID());
                                }
                            }
                        }
                    }
                }
            } else {
                sendEmailToClientForOverdueReminder(data, user);
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    private void sendEmailToClientForOverdueReminder(InvoiceList[] data, EdsUser user) throws EdsDbException {
        if (data != null) {
            for (InvoiceList ilForClient : data) {
                Map<String, List<NewInvoice>> datas = new LinkedHashMap<>();
                if (ilForClient != null && ilForClient.getList() != null && ilForClient.getList().size() > 0) {
                    for (NewInvoice newInvoice : ilForClient.getList()) {
                        if (!newInvoice.isCreditNote() && !"".equals(newInvoice.getClientContactEmail())) {
                            if (datas.containsKey(newInvoice.getClientContactEmail().toLowerCase())) {
                                datas.get(newInvoice.getClientContactEmail().toLowerCase()).add(newInvoice);
                            } else {
                                List<NewInvoice> invoiceList = new ArrayList<>();
                                invoiceList.add(newInvoice);
                                datas.put(newInvoice.getClientContactEmail().toLowerCase(), invoiceList);
                            }
                        }
                    }
                }
                messageManager.sendOverdueInvoiceReminderForEveryClient(datas, user);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getPurchaseInvoiceData(ListingFilterParameter filterParametrs) {
        return invoiceCircularResolver.getPurchaseInvoiceData(filterParametrs);
    }

    public ListResult<NewInvoice> getPurchaseInvoiceDataFromSolr(ListingFilterParameter filterParameter) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPurchaseInvoice.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get Purchase Invoice list");

        FacetFilterRpc purchaseFacetFilter = filterParameter.getFacetFilter();
        if (purchaseFacetFilter != null && !purchaseFacetFilter.isFilterChanges()) {
            purchaseFacetFilter = commonServiceLocal.getUserFacetFilter(purchaseFacetFilter);
        }
        if (filterParameter.getStartDateNC() != null) {
            filterParameter.setStartDate(ServerUtils.parseFilterParameterDate(filterParameter.getStartDateNC()));
        }
        if (filterParameter.getEndDateNC() != null) {
            filterParameter.setEndDate(ServerUtils.parseFilterParameterDate(filterParameter.getEndDateNC()));
        }
        if (purchaseFacetFilter != null) {
            if (purchaseFacetFilter.getSearchKey() != null && !"".equals(purchaseFacetFilter.getSearchKey())) {
                filterParameter.setSearchKey(purchaseFacetFilter.getSearchKey());
            }
            filterParameter.setFacetFilter(purchaseFacetFilter);
        }
        EdsUser edsUser = employeeManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(invoiceCircularResolver.getPurchaseInvoiceCoreSolrQuery(filterParameter, edsUser, null));
        solrQuery.append(SolrFacetUtils.generateForPricesFacet(purchaseFacetFilter,
                FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[4],
                FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[5],
                FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(purchaseFacetFilter, edsCompany,
                SolrPurchaseInvoiceRepresenter.FIELD_INVOICE_DATE,
                SolrPurchaseInvoiceRepresenter.FIELD_DUE_DATE,
                FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[4],
                FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[5],
                FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6]
        ));
        return getPurchaseInvoiceResponse(filterParameter, edsUser, solrQuery.toString());
    }

    private ListResult<NewInvoice> getPurchaseInvoiceResponse(ListingFilterParameter filterParameter, EdsUser edsUser, String solrQuery) {
//        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_PURCHASE_INVOICE_CORE);
//        QueryResponse resp = null;
//        try {
//            resp = server.query(getPurchaseInvoiceSolrQuery(filterParameter, solrQuery));
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//        }
//        return getPurchaseInvoiceFromSolrResult(resp, filterParameter);
        Page<PurchaseInvoiceSolrDoc> purchaseInvoiceSolrDocs = purchaseInvoiceSolrComponent.getList(filterParameter, solrQuery);
        return getPurchaseInvoiceFromSolrResult(purchaseInvoiceSolrDocs, filterParameter);
    }

    private ListResult<NewInvoice> getPurchaseInvoiceFromSolrResult(Page<PurchaseInvoiceSolrDoc> purchaseInvoiceSolrDocs, com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter filterParameter) {
        ListPanelToolRpc panelSettings = filterParameter.getListPanelTool();
        int totalNumber = (int) purchaseInvoiceSolrDocs.getTotalElements();
        ArrayList<NewInvoice> purchaseItems = new ArrayList<>();
        EdsUser user = roleManager.getUser();

        boolean isApproveProcessEnable = approverManager.isExistApproverByEntityType(RelationItem.TYPE_PURCHASE_INVOICE);
        boolean isProjectInLine = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);
        if (purchaseInvoiceSolrDocs.getContent() != null) {
            for (PurchaseInvoiceSolrDoc doc : purchaseInvoiceSolrDocs) {
                if (doc != null) {
                    NewInvoice purchaseItem = getPurchaseInvoiceRpc(doc, isProjectInLine);
                    purchaseItem.setApprover(isApproveProcessEnable);

                    if (purchaseItem != null) {
                        //init invoice custom fields

                        if (panelSettings != null) {
                            purchaseItem.setCustomFieldMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(doc, panelSettings.getColumnCodeName()));
                        }

                        purchaseItem.setAmount(purchaseItem.getTotalInInvoiceCurrency() != null && purchaseItem.getTaxCalculationType() != null && purchaseItem.getTaxCalculationType().equals(TAX_CALCULATION_INCLUSIVE) ? purchaseItem.getTotalInInvoiceCurrency().subtract(purchaseItem.getTotalTaxes() != null ? purchaseItem.getTotalTaxes() : BigDecimal.ZERO) : purchaseItem.getTotalInInvoiceCurrency());
                        purchaseItems.add(purchaseItem);

                    }

                }
            }
        }
        return new ListResult<>(purchaseItems, totalNumber);
    }

    public NewInvoice getPurchaseInvoiceRpc(PurchaseInvoiceSolrDoc doc, boolean isProjectInLine) {
        NewInvoice item = new NewInvoice();

        item.setID(doc.getPurchaseInvoiceId());
        item.setInvoiceNumber(doc.getPurchaseInvoiceNumber());
        item.setInvoiceDate(new DateNonConvertable(doc.getInvoiceDate()));
        item.setDueDate(new DateNonConvertable(doc.getDueDate()));
        item.setRelatedProjectID(doc.getRelatedProjectId());
        item.setRelatedProjectName(getProjectName(isProjectInLine, doc));
        item.setClientName(doc.getClientName());
        item.setCurrencyName(doc.getCurrencyName());
        item.setPaidAmount(BigDecimal.valueOf(doc.getPaidAmount()));
        item.setStatus(referenceWfmMessageSource.localize(doc.getStatusCode(), doc.getStatusName()));
        item.setStatusCode(doc.getStatusCode());
        item.setCreditNote(doc.getCreditNote());
        item.setAnyPaymentExists(doc.getHasPayment());

        String invoiceType = doc.getInvoiceType();
        if (invoiceType != null && !invoiceType.isEmpty()) {
            item.setDebitNote(PAYABLE.equals(invoiceType));
        }
        item.setTotalInInvoiceCurrency(BigDecimal.valueOf(doc.getTotalInInvoiceCurrency()));
        item.setTotal(BigDecimal.valueOf(doc.getTotalInvoiceBase()));
        item.setPoNumber(doc.getPoNumber());
        item.setOpportunityNumber(doc.getOpportunityNumber());
        item.setReference(doc.getReference());
//        item.setStatusCode(SolrUtils.asString(doc, SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_RELATED_PROJECT_STATUS_CODE));
        item.setClientVatNumber(doc.getPurchaseInvoiceSupplierVatNumber());
        item.setClientTrnNumber(doc.getPurchaseInvoiceSupplierTrn());
        item.setTotalTaxes(BigDecimal.valueOf(doc.getPurchaseInvoiceTotalTaxes()));
        item.setExchageRate(BigDecimal.valueOf(doc.getPurchaseInvoiceExchangeRate()));
        item.setTaxCalculationType(doc.getPurchaseInvoiceTaxCalculationType());
        item.setRelatedProjectID(doc.getRelatedProjectId());
        item.setCreatorName(doc.getCreaterFullName());
        if (doc.getRelatedProjectId() != null && !isProjectInLine) {
            item.setRelatedProjectName(doc.getRelatedProjectNumber() + " -> " + doc.getRelatedProjectName());
            item.setProjectStatusCode(doc.getPurchaseInvoiceRelatedProjectStatusCode());
        }
        Integer creatorId = doc.getCreatorId();
        String creatorName = doc.getCreatorName();
        if (creatorId != null && creatorName != null) {
            item.setCreator(new SelectItem(creatorId, creatorName));
        }
        Integer approverId = doc.getCurrentApproverId();
        if (approverId != null) {
            item.setCurrentApproverSelectItem(new SelectItem(approverId, doc.getCurrentApproverName()));
        }
        item.setZatcaStatus(doc.getZatcaStatus());
        item.setConverted(doc.isConverted());
        return item;
    }

    private String getProjectName(Boolean isProjectInLine, PurchaseInvoiceSolrDoc relevantDoc) {
        if (isProjectInLine) {
            return ServerUtils.asListToString(relevantDoc.getMultiProjectNumberName());
        } else {
            String number = relevantDoc.getRelatedProjectNumber();
            String name = relevantDoc.getRelatedProjectName();
            if (number != null && !"".equals(number) && name != null && !"".equals(name)) {
                return number + SolrPurchaseInvoiceRepresenter.ARROW + name;
            } else {
                return "";
            }

        }
    }

    private SolrQuery getPurchaseInvoiceSolrQuery(ListingFilterParameter filterParameter, String solrQuery) {
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(filterParameter.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(filterParameter.getLimit()));

        if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
            boolean desc = !filterParameter.isAscending();
            if (AccountingConstants.INVOICE_NUMBER_COLUMN.equals(filterParameter.getSortField())) {
                query.setSort(SolrPurchaseInvoiceRepresenter.SORTABLE_PURCHASEINVOICE_NUMBER, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (AccountingConstants.INVOICE_DATE_COLUMN.equals(filterParameter.getSortField())) {
                query.setSort(SolrPurchaseInvoiceRepresenter.FIELD_INVOICE_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (AccountingConstants.DUE_DATE_COLUMN.equals(filterParameter.getSortField())) {
                query.setSort(SolrPurchaseInvoiceRepresenter.FIELD_DUE_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (InvoiceList.RELATED_PROJECT.equals(filterParameter.getSortField())) {
                query.setSort(SolrPurchaseInvoiceRepresenter.SORTABLE_RELATED_PROJECT_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (InvoiceList.SUPPLIER.equals(filterParameter.getSortField())) {
                query.setSort(SolrPurchaseInvoiceRepresenter.SORTABLE_CLIENT_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (AccountingConstants.CURRENCY_COLUMN.equals(filterParameter.getSortField())) {
                query.setSort(SolrPurchaseInvoiceRepresenter.SORTABLE_CURRENCY_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (AccountingConstants.DUE_AMOUNT_COLUMN.equals(filterParameter.getSortField())) {
                query.setSort(SolrPurchaseInvoiceRepresenter.FIELD_DUE_AMOUNT, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (AccountingConstants.PAID_AMOUNT_COLUMN.equals(filterParameter.getSortField())) {
                query.setSort(SolrPurchaseInvoiceRepresenter.FIELD_PAID_AMOUNT, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (AccountingConstants.TAX_AMOUNT_COLUMN.equals(filterParameter.getSortField())) {
                query.setSort(SolrPurchaseInvoiceRepresenter.FIELD_TAX_AMOUNT, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (AccountingConstants.STATUS_COLUMN.equals(filterParameter.getSortField())) {
                query.setSort(SolrPurchaseInvoiceRepresenter.FIELD_STATUS_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (AccountingConstants.ORIGINAL_AMOUNT_COLUMN.equals(filterParameter.getSortField())) {
                query.setSort(SolrPurchaseInvoiceRepresenter.FIELD_TOTAL_IN_INVOICE_CURRENCY, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (InvoiceList.BASE_TOTAL.equals(filterParameter.getSortField())) {
                query.setSort(SolrPurchaseInvoiceRepresenter.FIELD_TOTAL_INVOICE_BASE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (InvoiceList.QUOTE_NUMBER.equals(filterParameter.getSortField())) {
                query.setSort(SolrPurchaseInvoiceRepresenter.FIELD_TOTAL_IN_INVOICE_CURRENCY, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (InvoiceList.PO_NUMBER.equals(filterParameter.getSortField())) {
                query.setSort(SolrPurchaseInvoiceRepresenter.FIELD_PO_NUMBER, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (InvoiceList.REFERENCE.equals(filterParameter.getSortField())) {
                query.setSort(SolrPurchaseInvoiceRepresenter.FIELD_REFERENCE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else {
                CustomFieldsUtils.setCustomFieldsSortableNameToSolr(filterParameter.getSortField(), !filterParameter.isAscending(), query, true);
            }
        } else {
            query.setSort(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID, SolrQuery.ORDER.desc);
        }
        return query;
    }

    @Override
    public boolean reSendToFifo(Integer entityID) {
        EdsInvoice invoice = invoiceManager.get(entityID);

        if (invoice == null) {
            return false;
        }
        EdsFifoFailure failure = fifoFailureManager.getAnyByEntityId(entityID, invoice.getType().equals("RECEIVABLE") ? (invoice.isCreditNote() ? EntityType.CUSTOMER_CREDIT_NOTE : EntityType.SALES_INVOICE) : (invoice.isCreditNote() ? EntityType.SUPPLIER_CREDIT_NOTE : EntityType.PURCHASE_INVOICE), ServerSecurityContext.getInstance().getCompanyId());

        if (failure != null) {
            failure.setDeleted(false);
            failure.setOnQue(true);
            failure.setRetries(1);
            failure.setLastAttemptAt(new Date());
            fifoFailureManager.update(failure);

            invoice.setEntityStatus(referenceManager.findReference(Constants.INVOICE_STATUS, Constants.PENDING));

            try {
                sendEvent(failure.toRPC(), UUID.randomUUID().toString());

                invoiceManager.update(invoice);
                if (invoice.getType().equals("RECEIVABLE")) {
                    saleInvoiceSolrComponent.index((EdsSaleInvoice) invoice);
                } else if (invoice.getType().equals("PAYABLE")) {
                    purchaseInvoiceSolrComponent.index((EdsPurchaseInvoice) invoice);
                }
                return true;
            } catch (IOException | InterruptedException | SolrServerException e) {
                log.error("Error while indexing the invoice with number -> {}", invoice.getNumber(), e);
            }
        }
        return false;
    }

    private void sendEvent(FIFODataMQ fifoDataMQ, String key) {
        fifoDataMQ.setTarget(FailTarget.SENDING);

        try {
            kafkaEventProducer.sendMessage(fifoDataMQ, key).get();
        } catch (Exception e) {
            log.error("====================== FAILED TO SEND THE EVENT ======================");
            handleFailure(fifoDataMQ, key, e.getMessage());
        }
    }

    private void handleFailure(FIFODataMQ fifoDataMQ, String key, String failMessage) {
        fifoFailureService.trackFailur(fifoDataMQ, key, failMessage);
    }

    public SaveResult updateSaleInvoice(NewInvoice data) {
        return updateSaleInvoice(data, true);
    }

    public SaveResult updateSaleInvoice(NewInvoice data, boolean runWebhook) {
        EdsBaseSaleInvoice invoice = (EdsBaseSaleInvoice) invoiceManager.get(data.getID());
        invoice.clear();

        SaveResult saveResult = new SaveResult();
        if (!data.isRecurringInvoice()) {
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsSaleInvoice.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(data.getID());
            ServerUtils.kpiLog(log, kpiLog, "Update Sale Invoice");
            List<EdsBaseSaleInvoice> existingInvoices = invoiceManager.getSaleInvoiceByNumber(data.getInvoiceNumber(), invoice.getCreationDate());
            if (existingInvoices != null && existingInvoices.size() > 0) {
                for (EdsBaseSaleInvoice i : existingInvoices) {
                    if (!i.getObjectID().equals(invoice.getObjectID())) {
                        saveResult.setInvoiceExist(true);
                        return saveResult;
                    }
                }
            }
        }

        createRevisionHistory(invoice);

        if (invoice.getClient() != null && data.getClientID() != null && !invoice.getClient().getObjectID().equals(data.getClientID())) {
            invoice.setClientContact(clientContactManager.getPrimaryClientContact(data.getClientID()));
        }

        invoice.setClient(clientManager.get(data.getClientID()));
        super.initInvoiceData(invoice, data);

        if (data.getFourDigitNumber() != null) {
            invoice.setFourDigitNumber(Integer.parseInt(data.getFourDigitNumber()));
        }
        Integer calculationScale = financialSettingsManager.getFinancialSettings().getCalculationScale();
        invoice.setCalcScale(calculationScale);
        EdsProject oldProject = null, newProject = null;

        if (invoice instanceof EdsSaleInvoice saleInv) {
            backfillFaiReportedDate(saleInv);

            saleInv.setFromDate(data.getPeriodStart() != null ? data.getPeriodStart().getNonConvertedDate() : null);
            saleInv.setToDate(data.getPeriodEnd() != null ? data.getPeriodEnd().getNonConvertedDate() : null);
            saleInv.setPriceLevelID(data.getPriceLevel() != null ? data.getPriceLevel().getId() : null);
            saleInv.setClientDiscountID(data.getClientDiscount() != null ? data.getClientDiscount().getId() : null);
            saleInv.setPreviousBalance(data.getPreviosBalance());
            saleInv.setPaymentReceived(data.getPaymentsReceived());
            saleInv.setShippingAmount(data.getShippingPrice());
            saleInv.setZatcaStatus(data.getZatcaStatus());

            oldProject = saleInv.getRelatedProject();
            newProject = data.getRelatedProjectID() != null ? projectManager.get(data.getRelatedProjectID()) : null;
            saleInv.setRelatedProject(newProject);
            saleInv.setInvoiceType(data.getInvoiceType());
            saleInv.setUpdatedDate(new Date());

            if (data.getInvoiceTermsItem() != null && data.getInvoiceTermsItem().getId() != null) {
                saleInv.setInvoiceTerms(invoiceTermsManager.get(data.getInvoiceTermsItem().getId()));
            } else {
                saleInv.setInvoiceTerms(null);
            }

            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(SaleInvoiceCustomEventListenerImpl.TYPE, SaleInvoiceCustomEventListenerImpl.EVENT_UPDATE_SOQ_INVOICING_DATA, saleInv, null);
            event.setCustomStringField(new Gson().toJson(new ConvertedQuotesDto("saveSI")));
            saleInv.setInTarget(false);

            if (isOk(data.getApprovers()) && (invoice.getFullPayments() == null || invoice.getFullPayments().compareTo(BigDecimal.ZERO) == 0 ||
                    (invoice.getTotalInInvoiceCurrency() != null && invoice.getFullPayments() != null && invoice.getTotalInInvoiceCurrency().compareTo(invoice.getFullPayments()) >= 0))) {
                saveInvoiceApprovers(invoice, data.getApprovers(), data.getStatusCode(), APPROVE);

                EdsBusinessEvent workflowApprovingEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), invoice, userManager.getUser());
                workflowApprovingEvent.setEntityType(RelationItem.TYPE_SALEINVOICE);
            }
        } else if (invoice instanceof EdsRecurringInvoice) {
            ((EdsRecurringInvoice) invoice).setInvoiceType(data.getInvoiceType());
        }
        invoice.setQuoteNumber(data.getQuoteNumber());

        invoice.setBankAccount((data.getBankAccount() != null && data.getBankAccount().getId() != null) ? bankAccountManager.get(data.getBankAccount().getId()) : null);
        invoice.setTaxCalculationType(data.getTaxCalculationType());
        invoice.setShippingMethod(data.getShippingMethodID() != null ? shippingMethodManager.get(data.getShippingMethodID()) : null);
        invoice.setRecurrence_number(data.getRecurrenceNumber());
        invoice.setRecurrence_pattern(data.getRecurrencePatternId() != null ? referenceManager.get(data.getRecurrencePatternId()) : null);
        invoice.setTotalDiscount(data.getTotalDiscount());
        invoice.setPaymentInstructionID(data.getPaymentInstructionID());

        if (data.getMarkupAccount() != null) {
            invoice.setMarkupAccount(accountingManager.get(data.getMarkupAccount().getId()));
        }
        invoice.setBillExpTotal(data.getBillableExpenseAmount());
        invoice.setBillExpTaxTotal(data.getBillableExpenseTaxAmount());
        invoice.setMarkupAmount(data.getMarkupAmount());
        invoice.setPercent(data.isPercent());


        if (data.getRecurrenceJobItem() != null) {
            saveInvoiceRecurringItem(data, invoice.getObjectID());
        }

        updateProjectsInvoiceDate(data);
        //Invoice Custom Fields
        invoice.setCustomFields(createInvoiceCustomFields(data.getCustomFieldItems()));
        //Save Tax Items
        initTaxTotals(invoice, data.getTotalTaxItems());
        Integer invoiceID = initInvoiceItemsForUpdate(data, invoice, runWebhook);

        if (invoice instanceof EdsSaleInvoice) {
            addSaleInvoiceToSolr((EdsSaleInvoice) invoice);
            updateRelatedProjectFromSolr(oldProject, newProject, employeeManager.getUser().getCompany());
            baseEventPostProcessor.registerEvent(SaleInvoiceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, (EdsSaleInvoice) invoice, userManager.getUser());
            if (data.getStatusCode().equals(APPROVE)) {
                baseEventPostProcessor.registerEvent(SaleInvoiceEventListenerImpl.TYPE, SaleInvoiceEventListenerImpl.EVENT_SALES_INVOICE_MANAGER_APPROVE, (EdsSaleInvoice) invoice, userManager.getUser());
            }
        }
        if (invoice instanceof EdsRecurringInvoice recurringInvoice) {
            baseEventPostProcessor.registerEvent(RecurringInvoiceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, recurringInvoice, userManager.getUser());
        }
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, invoice, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_SALEINVOICE);
        saveResult.setId(invoiceID);
        return saveResult;
    }

    public String getShortLink(Integer id) {
        EdsInvoice invoice = invoiceManager.getInvoice(id);
        if (invoice == null) {
            return invoiceManager.getSaleInvoice(id).getShortLink();
        }
        return invoice.getShortLink();
    }

    public void updateSaleInvoiceCustomFields(NewInvoice data) {
        EdsSaleInvoice invoice = (EdsSaleInvoice) invoiceManager.get(data.getID());
        invoice.setCustomFields(createInvoiceCustomFields(data.getCustomFieldItems()));

        try {
            saleInvoiceSolrComponent.index(invoice);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public SaveResult updatePurchaseInvoice(NewInvoice data) {
        SaveResult saveResult = new SaveResult();

        boolean isRecurringBill = data.getRecurrenceJobItem() != null;

        EdsBasePurchaseInvoice invoice;
        if (isRecurringBill) {
            invoice = invoiceManager.getRecurringBill(data.getID());
        } else {
            invoice = invoiceManager.getPurchaseInvoice(data.getID());
        }

        if (!isRecurringBill && !ServerUtils.isNullOrEmpty(data.getInvoiceNumber())) {
            List<EdsPurchaseInvoice> existingInvoices = invoiceManager.getPurchaseInvoiceByNumber(data.getInvoiceNumber(), data.getClientID(), invoice.getCreationDate());
            if (existingInvoices != null && existingInvoices.size() > 0) {
                for (EdsPurchaseInvoice pi : existingInvoices) {
                    if (!pi.getObjectID().equals(invoice.getObjectID())) {
                        saveResult.setInvoiceExist(true);
                        return saveResult;
                    }
                }
            }
        }
        createRevisionHistory(invoice);

        EdsUser user = data.getUserID() != null ? userManager.get(data.getUserID()) : invoiceManager.getUser();

        if (data.getInvoiceTermsItem() != null && data.getInvoiceTermsItem().getId() != null) {
            invoice.setInvoiceTerms(invoiceTermsManager.get(data.getInvoiceTermsItem().getId()));
        } else {
            invoice.setInvoiceTerms(null);
        }

        Integer calculationScale = financialSettingsManager.getFinancialSettings().getCalculationScale();
        data.setCalcScale(calculationScale);
        applyPurchaseInvoiceData(invoice, data);
        super.initInvoiceData(invoice, data);

        initTaxTotals(invoice, data.getTotalTaxItems());
        invoice.setZatcaStatus(data.getZatcaStatus());
        invoice.setCustomFields(createInvoiceCustomFields(data.getCustomFieldItems()));

        if (invoice instanceof EdsPurchaseInvoice edsPurchaseInvoice) {
            if (isOk(data.getApprovers()) && (invoice.getFullPayments() == null || invoice.getFullPayments().compareTo(BigDecimal.ZERO) == 0 || (invoice.getTotalInInvoiceCurrency() != null && invoice.getFullPayments() != null && invoice.getTotalInInvoiceCurrency().compareTo(invoice.getFullPayments()) >= 0))) {
                saveInvoiceApprovers(invoice, data.getApprovers(), data.getStatusCode(), Constants.APPROVE);

                EdsBusinessEvent workflowApprovingEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), invoice, userManager.getUser());
                workflowApprovingEvent.setEntityType(RelationItem.TYPE_PURCHASE_INVOICE);
            }

//            if (getCompanyDate(user).after(invoice.getDueDate()) && !invoice.getStatus().getCode().equals(PAID) &&
//                    !invoice.getStatus().getCode().equals(DRAFT)) {
//                invoice.setEntityStatus(referenceManager.findReference(INVOICE_STATUS, OVER_DUE));
//            }

            if (APPROVE.equals(data.getStatusCode()) && APPROVE.equals(data.getOldStatus())) {
                updateItemUnitPriceOnPurchaseInvoiceApprove((EdsPurchaseInvoice) invoice, data);
                baseEventPostProcessor.registerEvent(PurchaseInvoiceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsPurchaseInvoice, user);
            } else if (APPROVE.equals(data.getStatusCode()) || OPEN.equals(data.getStatusCode())) {
                baseEventPostProcessor.registerEvent(PurchaseInvoiceEventListenerImpl.TYPE, PurchaseInvoiceEventListenerImpl.EVENT_PURCHASE_INVOICE_APPROVE, edsPurchaseInvoice, user);
                updateItemUnitPriceOnPurchaseInvoiceApprove((EdsPurchaseInvoice) invoice, data);
            } else {
                baseEventPostProcessor.registerEvent(PurchaseInvoiceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsPurchaseInvoice, user);
            }

            EdsBusinessEvent workflowApprovingEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                    BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT,
                    edsPurchaseInvoice,
                    user);
            workflowApprovingEvent.setEntityType(RelationItem.TYPE_PURCHASE_INVOICE);
            try {
                purchaseInvoiceSolrComponent.index(edsPurchaseInvoice);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        saveResult.setId(initInvoiceItemsForUpdate(data, invoice, true));

        if (invoice instanceof EdsPurchaseInvoice) {
            updateConvertedPurchaseInvoiceData((EdsPurchaseInvoice) invoice, "savePI");
        }

        if (isRecurringBill) {
            ((EdsRecurringBill) invoice).setSender(user);
            saveBillRecurringItem(data, saveResult.getId());
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPurchaseInvoice.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        kpiLog.setEntityId(invoice.getObjectID());
        ServerUtils.kpiLog(log, kpiLog, (isRecurringBill ? "Update Recurring Bill" : "Update Purchase Invoice"));

        return saveResult;
    }

    private void updateItemUnitPriceOnPurchaseInvoiceApprove(EdsPurchaseInvoice invoice, NewInvoice newInvoice) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings != null && financialSettings.getUpdateCostPriceOnPurhcase() && newInvoice != null && (invoice.getConvertedQuotes() == null || invoice.getConvertedQuotes().isEmpty())) {
            NewInvoiceItem[] invoiceitems = newInvoice.getItems();
            if (invoiceitems != null) {
                for (NewInvoiceItem invoiceitem : invoiceitems) {
                    if (invoiceitem.getItemID() != null) {
                        EdsItem item = itemManager.get(invoiceitem.getItemID());
                        if (item != null) {
                            if (item.getMultiPrices() != null && !item.getMultiPrices().isEmpty()) {
                                for (EdsItemMultiPrice mp : item.getMultiPrices()) {
                                    EdsCurrency currency = currencyManager.get(newInvoice.getCurrencyID());
                                    if (mp.getCurrency() != null && mp.getCurrency().equals(currency)) {
                                        if (EdsItemMultiPrice.PAYABLE.equals(mp.getType())) {
                                            mp.setSellingPrice(invoiceitem.getUnitPrice());
                                        }
                                    }
                                }
                            } else {
                                item.setUnitPrice(invoiceitem.getUnitPrice().divide(newInvoice.getExchageRate(), ServerUtils.getSystemPriceScale(), RoundingMode.HALF_UP));
                            }
                            try {
                                productsServicesSolrComponent.index(item);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public void updatePurchaseInvoiceCustomFields(NewInvoice data) {
        EdsPurchaseInvoice invoice = invoiceManager.getPurchaseInvoice(data.getID());
        invoice.setCustomFields(createInvoiceCustomFields(data.getCustomFieldItems()));

        try {
            purchaseInvoiceSolrComponent.index(invoice);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //methodKey - qaysi method dan kelayotganini bilish un
    private void updateConvertedPurchaseInvoiceData(EdsPurchaseInvoice invoice, String methodKey) {
        if (invoice.getConvertedQuotes() != null && !invoice.getConvertedQuotes().isEmpty()) {
            for (EdsQuote quote : invoice.getConvertedQuotes()) {
                EdsPurchaseOrder purchaseOrder = quoteManager.getPurchaseOrderByID(quote.getObjectID());

                if (purchaseOrder != null) {
                    EdsFixedAsset fixedAsset = fixedAssetManager.getFixedAssetByPurchaseOrder(purchaseOrder.getObjectID());
                    if (fixedAsset != null) {
                        fixedAsset.setPurchaseInvoice(invoice);
                        fixedAsset.setFinancedBy(accountingManager.getAccountByKey(EdsAccount.ACCOUNTS_PAYABLE));
                        fixedAssetManager.update(fixedAsset);
                        fixedAssetService.createOrUpdateFixedAssetTransaction(fixedAsset);
                    }
                }
            }
            updatePIConvertedQtyAndStatus(invoice, methodKey);
        }

        if (invoice.isFixedAssetRelated()) {
            EdsFixedAsset fixedAsset = fixedAssetManager.getFixedAssetByPurchaseInvoice(invoice.getObjectID());
            if (fixedAsset != null) {
                fixedAsset.setFinancedBy(accountingManager.getMultiCurrencyAccount(EdsAccount.ACCOUNTS_PAYABLE, invoice.getCurrency()));
                fixedAssetManager.update(fixedAsset);
                fixedAssetService.createOrUpdateFixedAssetTransaction(fixedAsset);
            }
        }
    }

    private void updateConvertedShippingData(EdsInvoice invoice, NewInvoice data) {

        if (invoice == null || data == null) {
            return;
        }

        if (data.getTargetGrnId() != null) {
            final EdsShippingData shippingData = this.shippingDataManager.get(data.getTargetGrnId());

            if (shippingData == null) {
                return;
            }
            shippingData.setStatus(ShippingDataStatus.CONVERTED);
            this.shippingDataManager.update(shippingData);
            invoice.getConvertedShippingData().add(shippingData);
            return;
        }
        final List<Integer> quoteIds = data.getConvertedItemID() != null ? Collections.singletonList(data.getConvertedItemID()) :
                !CollectionUtils.isEmpty(data.getConvertedQuoteIDs()) ? data.getConvertedQuoteIDs() : Collections.emptyList();

        final List<EdsShippingData> list = this.shippingDataManager.getShippingDataQuoteIds(quoteIds);

        for (EdsShippingData shippingData : list) {
            if (shippingData.getQuote() == null) {
                continue;
            }
            shippingData.setStatus(ShippingDataStatus.CONVERTED);
            invoice.getConvertedShippingData().add(shippingData);
        }
    }

    private void checkForConvertedShippingDataItems(BigDecimal convertedQty, List<EdsShippingDataItem> items) {
        if (convertedQty == null || convertedQty.compareTo(BigDecimal.ZERO) <= 0 || items == null) {
            return;
        }
        for (EdsShippingDataItem edsShippingDataItem : items) {
            final BigDecimal quantityToConvert = Optional.ofNullable(edsShippingDataItem.getFullQuantityToConvert()).orElse(BigDecimal.ZERO);

            if (convertedQty.compareTo(BigDecimal.ZERO) == 0) {
                edsShippingDataItem.setConverted(BigDecimal.ZERO);
                edsShippingDataItem.setStatus(ShippingDataItemStatus.NEW);
                continue;
            } else if (convertedQty.compareTo(quantityToConvert) >= 0) {
                edsShippingDataItem.setStatus(ShippingDataItemStatus.CONVERTED);
                edsShippingDataItem.setConverted(quantityToConvert);
                convertedQty = convertedQty.subtract(quantityToConvert);

            } else if (convertedQty.compareTo(quantityToConvert) < 0) {
                edsShippingDataItem.setConverted(convertedQty);
                edsShippingDataItem.setStatus(ShippingDataItemStatus.PARTLY_CONVERTED);
                convertedQty = BigDecimal.ZERO;
            }
        }
    }

    private void updatePIConvertedQtyAndStatus(EdsPurchaseInvoice invoice, String methodKey) {
        final EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        final Integer qtyScale = fs.getAccountingQtyCalculationScale();
        final Integer priceScale = fs.getAccountingCalculationScale();

        List<EdsShippingData> shippingDataList = new ArrayList<>(invoice.getConvertedShippingData());

        for (EdsQuote quote : invoice.getConvertedQuotes()) {
            EdsPurchaseOrder purchaseOrder = quoteManager.getPurchaseOrderByID(quote.getObjectID());
            boolean fullyConverted = true;
            List<EdsQuoteItem> quoteItems = purchaseOrder.getQuoteItems();
            boolean isConvertedQtyExists = false;
            BigDecimal totalConvertedQtys = BigDecimal.ZERO, totalConvertedAmounts = BigDecimal.ZERO;
            for (EdsQuoteItem quoteItem : quoteItems) {

                if (ReceiveTypeEnum.RECEIVE_BY_VALUE.equals(quoteItem.getReceiveType())) {
                    BigDecimal convertedAmount = invoiceManager.getConvertedAmountByQuoteItem(quoteItem.getObjectID()).setScale(qtyScale, RoundingMode.HALF_UP);
                    quoteItem.setConvertedAmount(convertedAmount);
                    totalConvertedAmounts = totalConvertedAmounts.add(convertedAmount);
                    if (BigDecimal.ZERO.compareTo(quoteItem.getReceivedAmount()) > 0) {
                        isConvertedQtyExists = true;
                    }
                    BigDecimal net = quoteItem.getQty().multiply(quoteItem.getUnitPrice()).setScale(priceScale, RoundingMode.HALF_UP);
                    if (net.compareTo(quoteItem.getConvertedAmount()) > 0) {
                        fullyConverted = false;
                    }
                } else {
                    BigDecimal convertedQty = invoiceManager.getConvertedQtyByQuoteItem(quoteItem.getObjectID()).setScale(qtyScale, RoundingMode.HALF_UP);
                    quoteItem.setConvertedQty(convertedQty);
                    totalConvertedQtys = totalConvertedQtys.add(convertedQty);
                    if (BigDecimal.ZERO.compareTo(quoteItem.getReceivedQty()) > 0) {
                        isConvertedQtyExists = true;
                    }
                    if (quoteItem.getQty().setScale(qtyScale, RoundingMode.HALF_UP).compareTo(quoteItem.getConvertedQty()) > 0) {
                        fullyConverted = false;
                    }
                }
            }
            purchaseOrder.setQuoteItems(quoteItems);
            String invoiceStatus = invoice.getStatus().getCode();
            if (fullyConverted) {

                List<EdsPurchaseInvoice> purchaseInvoices = invoiceManager.getPurchaseInvoicesByConvertedItem(purchaseOrder.getObjectID());
                boolean isAllInvoicesApproved = true;
                for (EdsPurchaseInvoice pi : purchaseInvoices) {
                    if (DRAFT.equals(pi.getStatus().getCode())) {
                        isAllInvoicesApproved = false;
                        break;
                    }
                }
                if (!INVOICE_STATUS_CLOSED.equals(purchaseOrder.getStatus().getCode())) {
                    if (isAllInvoicesApproved) {
                        purchaseOrder.setStatus(referenceManager.findReference(INVOICE_STATUS, INVOICED));
                    } else {
                        purchaseOrder.setStatus(referenceManager.findReference(INVOICE_STATUS, CONVERTED));
                    }
                }
                quoteManager.update(purchaseOrder);
                addPurchaseOrderToSolr(purchaseOrder);
            } else {
                if ("savePI".equals(methodKey)) {
                    if (invoiceStatus.equals(APPROVE) && CONVERTED.equals(purchaseOrder.getStatus().getCode())
                            || totalConvertedQtys.compareTo(BigDecimal.ZERO) == 0 && totalConvertedAmounts.compareTo(BigDecimal.ZERO) == 0) {
                        purchaseOrder.setStatus(referenceManager.findReference(INVOICE_STATUS, INVOICED));
                    }
                } else if ("deletePI".equals(methodKey) || "voidPI".equals(methodKey)) {
                    if (!INVOICE_STATUS_CLOSED.equals(purchaseOrder.getStatus().getCode())) {
                        if (INVOICED.equals(purchaseOrder.getStatus().getCode()) || CONVERTED.equals(purchaseOrder.getStatus().getCode())) {
                            purchaseOrder.setStatus(referenceManager.findReference(INVOICE_STATUS, (isConvertedQtyExists ? PARTIAL_RECEIVED : RECEIVED)));
                        }
                    }

                    if (invoice.getConvertedShippingData() != null && !invoice.getConvertedShippingData().isEmpty()) {
                        invoice.getConvertedShippingData().clear();
                    }
                }
                quoteManager.update(purchaseOrder);
                addPurchaseOrderToSolr(purchaseOrder);
                if (!shippingDataList.isEmpty()) {
                    for (EdsShippingData shippingData : shippingDataList) {
                        shippingData.setStatus(ShippingDataStatus.SUCCESSFUL);
                    }
                    try {
                        shippingDataManager.updateAll(new ArrayList<>(shippingDataList), shippingDataList.size());
                        shippingDataSolrComponent.indexes(shippingDataList);
                        EventHandler.fireEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, "Related Invoice is voided");
                    } catch (Exception e) {
                        log.error("Error occurred while updating PI", e);
                    }
                }
            }
        }
    }

    public void updateGatewaySaleInvoice(NewInvoice data) {
        EdsSaleInvoice invoice = (EdsSaleInvoice) invoiceManager.get(data.getID());
        invoice.setClientApproved(data.isClientApproved());
        invoiceManager.update(invoice);
        addInvoiceToSolr(invoice);
    }

    private void createRevisionHistory(EdsInvoice invoice) {
        if (invoice == null) {
            log.error("Invoice object is null");
            return;
        }
        EdsInvoice clonedInvoice = invoice.cloneShallow();
        clonedInvoice.setDeleted(true);
        clonedInvoice.setHistoricalParent(invoice);

        List<EdsInvoiceItem> invoiceItems = new LinkedList<>();
        for (EdsInvoiceItem ii : invoice.getInvoiceItems()) {
            EdsInvoiceItem nii = ii.cloneShallow();
            nii.setInvoice(clonedInvoice);
            nii.setDeleted(true);
            invoiceItems.add(nii);
        }
        clonedInvoice.setInvoiceItems(invoiceItems);

        List<EdsInvoiceTaxTotal> invoiceTaxTotals = new LinkedList<>();
        for (EdsInvoiceTaxTotal itt : invoice.getInvoiceTaxTotals()) {
            EdsInvoiceTaxTotal ittCloned = itt.cloneShallow();
            ittCloned.setInvoice(clonedInvoice);
            invoiceTaxTotals.add(ittCloned);
        }
        clonedInvoice.setInvoiceTaxTotals(invoiceTaxTotals);

        List<EdsInvoicePayment> invoicePayments = new LinkedList<>();
        if (invoice.getPayments() != null) {
            for (EdsInvoicePayment ip : invoice.getPayments()) {
                EdsInvoicePayment ipCloned = ip.cloneShallow();
                ipCloned.setHistoricalParent(ip);
                ipCloned.setDeleted(true);
                ipCloned.setInvoice(clonedInvoice);
                invoicePayments.add(ipCloned);
            }
        }
        clonedInvoice.setPayments(invoicePayments);

        List<EdsInvoicePayment> creditNoteRefunds = new LinkedList<>();
        if (invoice.getRefunds() != null) {
            for (EdsInvoicePayment cnr : invoice.getRefunds()) {
                EdsInvoicePayment cnrCloned = cnr.cloneShallow();
                cnrCloned.setHistoricalParent(cnr);
                cnrCloned.setDeleted(true);
                cnrCloned.setCreditNote(clonedInvoice);
                creditNoteRefunds.add(cnrCloned);
            }
        }
        clonedInvoice.setRefunds(creditNoteRefunds);

        if (clonedInvoice instanceof EdsRecurringInvoice) {
            ((EdsRecurringInvoice) clonedInvoice).setFileIDs(null);
        }

        clonedInvoice.setExpense(null);
        clonedInvoice.setConvertedQuotes(null);
        clonedInvoice.setItemsAsExpense(null);
        clonedInvoice.setMjItemsAsExpense(null);
        clonedInvoice.setBtItemsAsExpense(null);
        clonedInvoice.setApprovers(null);
//        clonedInvoice.setApproverHistory(null);
        clonedInvoice.setBchItemsAsExpense(null);
        clonedInvoice.setConvertedShippingData(null);
        invoiceManager.create(clonedInvoice);
    }

    private void applyPurchaseInvoiceData(EdsBasePurchaseInvoice invoice, NewInvoice data) {
        invoice.setSupplier(crmAccountManager.get(data.getClientID()));
        if (data.getClientItem() != null) {
            invoice.setClientID(data.getClientItem().getId());
            invoice.setClientMailAddressID(data.getClientItem().getMailAddressID());
        } else {
            invoice.setClientID(null);
            invoice.setClientMailAddressID(null);
        }
        invoice.setCancelDate(data.getCancelDate() != null ? data.getCancelDate().getNonConvertedDate() : null);
        invoice.setRelatedProject(data.getRelatedProjectID() != null ? projectManager.get(data.getRelatedProjectID()) : null);
        invoice.setTaxCalculationType(data.getTaxCalculationType());
        invoice.setPriceLevelID(data.getPriceLevel() != null ? data.getPriceLevel().getId() : null);
        invoice.setReverseChargeApplicable(data.isReversechargeApplicable());
        invoice.setCalcScale(data.getCalcScale());

        invoice.setUpdatedDate(new Date());
    }

    private Integer initInvoiceItemsForUpdate(NewInvoice data, EdsInvoice invoice, boolean runWebhook) {
        EdsUser user = data.getUserID() != null ? userManager.get(data.getUserID()) : userManager.getUser();
        List<Integer> invoiceItemsDeleted = null;
        try {
            invoiceItemsDeleted = invoiceManager.deleteInvoiceItems(data.getID());

            itemSerialService.deleteForInvoice(invoice, invoiceItemsDeleted);
            itemBatchService.deleteBatchSerialsForInvoice(invoice);

        } catch (Exception e) {
            log.info("Can't delete invoice items. Invoice ID: " + data.getID());
        }

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        boolean isMultipleWarehouseEnabled = financialSettings.getEnableMultiWarehouse();
        EdsWarehouse defaultWarehouse = warehouseManager.getDefaultWarehouse();

        if (data.isProjectBasedInvoice()) {
            invoiceItemManager.removeTimesheetRelationsForInvoiceItems(invoiceItemsDeleted);
        }

        List<EdsInvoiceItem> items = new LinkedList<>();
        for (NewInvoiceItem newItem : data.getItems()) {
            if (newItem == null) {
                continue;
            }
            EdsInvoiceItem item = new EdsInvoiceItem();

            if (!isMultipleWarehouseEnabled) {
                item.setWarehouse(defaultWarehouse);
            }

            item.setInvoice(invoice);
            super.initInvoiceItemData(item, newItem);
            item.setExpenceItemId(newItem.getExpanceItemId());
            item.setQuoteItemId(newItem.getQuoteItemId());
            item.setFromTimesheet(newItem.isFromTimesheet());
            Optional.ofNullable(newItem.getFromDate()).ifPresent(fd -> item.setFromDate(fd.getNonConvertedDate()));
            Optional.ofNullable(newItem.getToDate()).ifPresent(td -> item.setToDate(td.getNonConvertedDate()));
            item.setFaiCategoryId(newItem.getFaiCategoryId());

            ViewName customFieldType = null;
            if (invoice instanceof EdsPurchaseInvoice) {
                customFieldType = ViewName.PurchaseInvoiceItem;
            } else if (invoice instanceof EdsSaleInvoice) {
                customFieldType = ViewName.SaleInvoiceItem;
            }
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.INVOICE_ITEM_TABLE_POPULATE_WITH_CF) && newItem.getItemID() != null && productService.getProductBaseData(newItem.getItemID()) != null) {
                populateItemTableCfValue(newItem, item, customFieldType);
            }

            item.setCustomFields(createInvoiceItemCustomFields(newItem.getCustomFieldItems()));

            if (invoice instanceof EdsPurchaseInvoice && newItem.getClient() != null) {
                item.setClient(crmAccountManager.get(newItem.getClient().getId()));

                if (newItem.getSaleInvoiceId() != null) {
                    item.setSaleInvoice((EdsSaleInvoice) invoiceManager.get(newItem.getSaleInvoiceId()));
                }
            }

            invoiceItemManager.create(item);
            if (data.isProjectBasedInvoice() && newItem.getProjectBasedEntryIds() != null) {
                for (int i = 0; i < newItem.getProjectBasedEntryIds().length; i++) {
                    if (newItem.getProjectBasedEntryIds()[i] != null) {
                        timeSheetManager.get(newItem.getProjectBasedEntryIds()[i]).setUsedInInvoice(true);
                        timeSheetManager.get(newItem.getProjectBasedEntryIds()[i]).setInvoiceItemID(item.getObjectID());
                    }
                }
            }

            if (newItem.getInventoryTrackingEnabled()) {
                item.setSerials(newItem.getSerials());
            }

            item.setAssignedSerials(newItem.getAssignedSerials());

            if (item.getItem() != null && item.getItem().getTrackBatchesEnabled()) {
                if (invoice instanceof EdsPurchaseInvoice) {
                    if ((invoice.getConvertedQuotes() != null && !invoice.getConvertedQuotes().isEmpty())
                            || (invoice.getConvertedShippingData() != null && !invoice.getConvertedShippingData().isEmpty())) { //GRN convert to PI or PO convert to PI
                        itemBatchService.createBatchForConvertedPurchaseInvoice(invoice.getObjectID(), newItem, item.getObjectID());
                    } else {
                        itemBatchService.createBatchForPurchaseInvoice(invoice.getObjectID(), newItem, item.getObjectID());
                    }
                } else if (invoice instanceof EdsSaleInvoice) {
                    if ((invoice.getConvertedQuotes() != null && !invoice.getConvertedQuotes().isEmpty())
                            || (invoice.getConvertedShippingData() != null && !invoice.getConvertedShippingData().isEmpty())) { //GDN convert to SI or SO conver to SI
                        itemBatchService.createBatchForConvertedSaleInvoice(invoice.getObjectID(), newItem, item.getObjectID());
                    } else {
                        itemBatchService.createBatchForSaleInvoice(invoice.getObjectID(), newItem, item.getObjectID());
                    }
                }
            }
            items.add(item);
        }
        invoice.setInvoiceItems(items);
        invoiceItemManager.removeRelatedInvoicesFromBillableExpense(invoice.getObjectID());
        applyExpensesToInvoice(data, invoice);

        if (data.getAttachments() != null && data.getAttachments().length > 0) {
            if (invoice instanceof EdsPurchaseInvoice) {
                attachmentUtilsManager.saveAttachments(F_PUR_INV, invoice.getObjectID(), invoice.getObjectID(), data.getAttachments());
            } else if (invoice instanceof EdsSaleInvoice) {
                attachmentUtilsManager.saveAttachments(F_SALE_INV, invoice.getObjectID(), invoice.getObjectID(), data.getAttachments());
            }
        }
        if (!data.isRecurringInvoice() && data.isBookkeep() && !isOk(data.getApprovers()) && (APPROVE.equals(data.getStatusCode()) || OPEN.equals(data.getStatusCode()))) {
            this.createInvoiceTransactionsAndCalculateProjectBugdet(invoice, invoiceManager.getUser());
        }
        registerInterCompanySalesTransaction(data, invoice, user);
        createInvoiceNoteAndHistory(data, invoice);

        if (invoice instanceof EdsSaleInvoice) {
            updateSaleInvoiceItemsProductSerials(invoice, invoiceItemsDeleted);
        }

        Integer calculationScale = financialSettingsManager.getFinancialSettings().getAccountingCalculationScale();

        if (!data.isRecurringInvoice() && BigDecimal.ZERO.compareTo(invoice.getTotalInInvoiceCurrency().setScale(calculationScale, RoundingMode.HALF_UP)) == 0 && (APPROVE.equals(invoice.getStatus().getCode()) || OPEN.equals(invoice.getStatus().getCode()))) {
            invoice.setStatus(referenceManager.findReference(INVOICE_STATUS, PAID));
            invoiceManager.update(invoice);
        }

        if (runWebhook && invoice instanceof EdsSaleInvoice) {
            try {
                List<EdsRestHook> webhooks = restHookManager.getByEventName("order.update");
                if (!webhooks.isEmpty()) {
                    for (EdsRestHook webhook : webhooks) {
                        try {
                            if (!"https://hooks.zapier.com/fake-subscription-url".equalsIgnoreCase(webhook.getTargetUrl())) {
                                log.info("Triggering webhook {}: {}", webhook.getEventName(), webhook.getTargetUrl());

                                HttpHeaders httpHeaders = new HttpHeaders();
                                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                                HttpEntity<ZapierInvoiceItemTO> httpRequest = new HttpEntity<>(convertToZapierOrder((EdsSaleInvoice) invoice), httpHeaders);

                                String resp = restTemplate.postForObject(webhook.getTargetUrl(), httpRequest, String.class);
                                log.info("ZAPIER WEBHOOK: {}", resp);
                            }
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    }
                }
            } catch (Exception e1) {
                log.error("", e1);
            }
        }
        return invoice.getObjectID();
    }

    private void applyExpensesToInvoice(NewInvoice data, EdsInvoice invoice) {

        if (data.getExpenses() != null && data.getExpenses().size() > 0) {
            for (BillableExpenseItem exp : data.getExpenses()) {

                switch (exp.getType()) {
                    case BillableExpenseItem.EXPENSE -> {
                        EdsExpense expense = expenseManager.get(exp.getObjectID());
                        EdsExpenseReport er = expense.getReport();
                        expense.setMarkupAmount(convertAmountInnerCurrencies(exp.getMarkupAmount(), data.getCurrencyID(), data.getExchageRate(), er.getCurrency().getObjectID(), er.getExchangeRate()));
                        expense.setMarkupTaxAmount(convertAmountInnerCurrencies(exp.getMarkupTaxAmount(), data.getCurrencyID(), data.getExchageRate(), er.getCurrency().getObjectID(), er.getExchangeRate()));
                        if (exp.getMarkupAccount() != null) {
                            expense.setMarkupAccount(accountingManager.get(exp.getMarkupAccount().getId()));
                        } else {
                            expense.setMarkupAccount(null);
                        }
                        if (exp.getMarkupTax() != null) {
                            expense.setMarkupTax(vatManager.get(exp.getMarkupTax().getId()));
                        } else {
                            expense.setMarkupTax(null);
                        }
                        expense.setInvoice(invoice);
                        expenseManager.update(expense);
                        continue;
                    }
                    case BillableExpenseItem.PURCHASE_AS_EXPENSE -> {
                        EdsInvoiceItem invoiceItem = invoiceItemManager.get(exp.getObjectID());
                        EdsInvoice inv = invoiceItem.getInvoice();
                        invoiceItem.setSaleInvoice((EdsSaleInvoice) invoice);
                        if (exp.getMarkupAccount() != null) {
                            invoiceItem.setMarkupAccount(accountingManager.get(exp.getMarkupAccount().getId()));
                        } else {
                            invoiceItem.setMarkupAccount(null);
                        }
                        if (exp.getMarkupTax() != null) {
                            invoiceItem.setMarkupTax(vatManager.get(exp.getMarkupTax().getId()));
                        } else {
                            invoiceItem.setMarkupTax(null);
                        }
                        invoiceItem.setMarkupAmount(convertAmountInnerCurrencies(exp.getMarkupAmount(), data.getCurrencyID(), data.getExchageRate(), inv.getCurrency().getObjectID(), inv.getExchangeRate()));
                        invoiceItem.setMarkupTaxAmount(convertAmountInnerCurrencies(exp.getMarkupTaxAmount(), data.getCurrencyID(), data.getExchageRate(), inv.getCurrency().getObjectID(), inv.getExchangeRate()));
                        invoiceItemManager.update(invoiceItem);
                    }
                    case BillableExpenseItem.BANK_TRANSFER_AS_EXPENSE -> {
                        EdsBankTransferItem bankTransferItem = spendReceiveMoneyItemManager.get(exp.getObjectID());
                        EdsBankTransfer bt = bankTransferItem.getMoneyTransfer();
                        bankTransferItem.setMarkupAmount(convertAmountInnerCurrencies(exp.getMarkupAmount(), data.getCurrencyID(), data.getExchageRate(), bt.getCurrency().getObjectID(), bt.getExchangeRate()));
                        bankTransferItem.setMarkupTaxAmount(convertAmountInnerCurrencies(exp.getMarkupTaxAmount(), data.getCurrencyID(), data.getExchageRate(), bt.getCurrency().getObjectID(), bt.getExchangeRate()));
                        if (exp.getMarkupAccount() != null) {
                            bankTransferItem.setMarkupAccount(accountingManager.get(exp.getMarkupAccount().getId()));
                        } else {
                            bankTransferItem.setMarkupAccount(null);
                        }
                        if (exp.getMarkupTax() != null) {
                            bankTransferItem.setMarkupTax(vatManager.get(exp.getMarkupTax().getId()));
                        } else {
                            bankTransferItem.setMarkupTax(null);
                        }
                        bankTransferItem.setInvoice(invoice);
                        spendReceiveMoneyItemManager.update(bankTransferItem);
                    }
                    case BillableExpenseItem.MANUAL_TRANSACTION_AS_EXPENSE -> {
                        EdsManualJournalItem manualJournalItem = manualJournalItemManager.get(exp.getObjectID());
                        EdsManualJournal mj = manualJournalItem.getManualTransfer();
                        manualJournalItem.setMarkupAmount(convertAmountInnerCurrencies(exp.getMarkupAmount(), data.getCurrencyID(), data.getExchageRate(), mj.getCurrency().getObjectID(), mj.getExchangeRate()));
                        manualJournalItem.setMarkupTaxAmount(convertAmountInnerCurrencies(exp.getMarkupTaxAmount(), data.getCurrencyID(), data.getExchageRate(), mj.getCurrency().getObjectID(), mj.getExchangeRate()));
                        if (exp.getMarkupAccount() != null) {
                            manualJournalItem.setMarkupAccount(accountingManager.get(exp.getMarkupAccount().getId()));
                        } else {
                            manualJournalItem.setMarkupAccount(null);
                        }
                        if (exp.getMarkupTax() != null) {
                            manualJournalItem.setMarkupTax(vatManager.get(exp.getMarkupTax().getId()));
                        } else {
                            manualJournalItem.setMarkupTax(null);
                        }
                        manualJournalItem.setInvoice(invoice);
                        manualJournalItemManager.update(manualJournalItem);
                    }
                    case BillableExpenseItem.CHECK_AS_EXPENSE -> {
                        EdsBankCheckItem bankCheckItem = bankCheckItemManager.get(exp.getObjectID());
                        CurrencyItem baseCurrency = getBaseCurrency();
                        bankCheckItem.setMarkupAmount(convertAmountInnerCurrencies(exp.getMarkupAmount(), data.getCurrencyID(), data.getExchageRate(), baseCurrency.getId(), BigDecimal.ONE));
                        bankCheckItem.setMarkupTaxAmount(convertAmountInnerCurrencies(exp.getMarkupTaxAmount(), data.getCurrencyID(), data.getExchageRate(), baseCurrency.getId(), BigDecimal.ONE));
                        if (exp.getMarkupAccount() != null) {
                            bankCheckItem.setMarkupAccount(accountingManager.get(exp.getMarkupAccount().getId()));
                        } else {
                            bankCheckItem.setMarkupAccount(null);
                        }
                        if (exp.getMarkupTax() != null) {
                            bankCheckItem.setMarkupTax(vatManager.get(exp.getMarkupTax().getId()));
                        } else {
                            bankCheckItem.setMarkupTax(null);
                        }
                        bankCheckItem.setInvoice(invoice);
                        bankCheckItemManager.update(bankCheckItem);
                    }
                    default -> {
                    }
                }

            }
        }
    }

    private BigDecimal convertAmountInnerCurrencies(BigDecimal amount, Integer fromCurrencyId, BigDecimal
            fromExchangeRate, Integer toCurrencyId, BigDecimal toExchangeRate) {
        if (fromCurrencyId.equals(toCurrencyId)) {
            return amount;
        }

        BigDecimal convertedAmount = amount.divide(fromExchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        convertedAmount = convertedAmount.multiply(toExchangeRate).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);

        return convertedAmount;
    }

    private Map<Integer, BigDecimal> getInvetoryEnabledItemsTotal(List<EdsInvoiceItem> invItems) {
        Map<Integer, BigDecimal> totals = new HashMap<>();
        for (EdsInvoiceItem invItem : invItems) {
            EdsItem item = invItem.getItem();
            if (item != null && (item.getType().equals(INVENTORY_ITEM) || item.getType().equals(ASSEMBLY_ITEM))) {
                if (!totals.containsKey(item.getObjectID())) {
                    totals.put(item.getObjectID(), invItem.getQty());
                } else {
                    totals.put(item.getObjectID(), totals.get(item.getObjectID()).add(invItem.getQty()));
                }
            }
        }
        return totals;
    }

    private Map<String, BigDecimal> getInvItemsTotal(List<EdsInvoiceItem> invItems) {
        Map<String, BigDecimal> totals = new HashMap<>();
        String key;
        for (EdsInvoiceItem invItem : invItems) {
            EdsItem item = invItem.getItem();
            if (item != null && invItem.getWarehouse() != null && (item.getType().equals(INVENTORY_ITEM) || item.getType().equals(ASSEMBLY_ITEM))) {
                key = item.getObjectID().toString() + "@" + invItem.getWarehouse().getObjectID().toString();
                if (!totals.containsKey(key)) {
                    totals.put(key, invItem.getQty());
                } else {
                    totals.put(key, totals.get(key).add(invItem.getQty()));
                }
            }
        }

        return totals;
    }

    private Map<Integer, BigDecimal> getInvetoryEnabledStockTransferItemsTotal
            (List<EdsStockAdjustment> edsStockAdjustmentList) {
        Map<Integer, BigDecimal> totals = new HashMap<>();
        for (EdsStockAdjustment edsStockAdjustment : edsStockAdjustmentList) {
            if (edsStockAdjustment.getAdjustmentItemList().size() > 0) {
                EdsAdjustmentItem edsAdjustmentItem = edsStockAdjustment.getAdjustmentItemList().get(0);
                BigDecimal qty = edsAdjustmentItem.getUsedQty().add(edsAdjustmentItem.getNewQty());
                EdsItem item = edsAdjustmentItem.getItem();
                if (item != null && (item.getType().equals(INVENTORY_ITEM) || item.getType().equals(ASSEMBLY_ITEM))) {
                    if (!totals.containsKey(item.getObjectID())) {
                        totals.put(item.getObjectID(), qty);
                    } else {
                        totals.put(item.getObjectID(), totals.get(item.getObjectID()).add(qty));
                    }
                }
            }
        }
        return totals;
    }

    private Map<String, BigDecimal> getStockTransferItemsTotal(List<EdsStockAdjustment> edsStockAdjustmentList) {
        Map<String, BigDecimal> totals = new HashMap<>();
        String key;
        for (EdsStockAdjustment edsStockAdjustment : edsStockAdjustmentList) {
            if (edsStockAdjustment.getAdjustmentItemList().size() > 0) {
                EdsAdjustmentItem edsAdjustmentItem = edsStockAdjustment.getAdjustmentItemList().get(0);
                BigDecimal qty = edsAdjustmentItem.getUsedQty().add(edsAdjustmentItem.getNewQty());
                EdsItem item = edsAdjustmentItem.getItem();
                if (item != null && edsStockAdjustment.getFromWarehouse() != null && (item.getType().equals(INVENTORY_ITEM) || item.getType().equals(ASSEMBLY_ITEM))) {
                    key = item.getObjectID().toString() + "@" + edsStockAdjustment.getFromWarehouse().getObjectID().toString();
                    if (!totals.containsKey(key)) {
                        totals.put(key, qty);
                    } else {
                        totals.put(key, totals.get(key).add(qty));
                    }
                }
            }
        }

        return totals;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getGoogleCheckoutMerchantId() {
        return invoiceCircularResolver.getInvoiceGoogleCheckoutMerchantId(invoiceManager.getUser().getCompany().getObjectID());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getInvoicePaymentLink(Integer id) {
        return invoiceCircularResolver.getInvoicePaymentLink(id, null, invoiceManager.getUser().getCompany().getObjectID());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getRecurrencePattern() {
        List<EdsReference> referenceList = referenceManager.listReferences(EdsSaleInvoice._RECURRING_PERIOD);
        SelectItem[] items = new SelectItem[referenceList.size()];
        int i = 0;
        for (EdsReference reference : referenceList) {
            SelectItem item = new SelectItem();
            item.setId(reference.getObjectID());
            item.setName(wfmMessageSource.localize(reference.getCode(), reference.getName()));
            item.setDescription(reference.getDescription());
            items[i] = item;
            i++;
        }
        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] validateStockAvailability(QuantityItem[] qItems, Integer entityID, StockOutFlow outFlow, DateNonConvertable tillDate) {
        return stockValidationService.validateStockAvailability(qItems, entityID, outFlow, tillDate);
    }

    @Override
    public SelectItem validateStockInconsistencyInDeleteProcess(StockTransactionType transactionType, Integer
            entityID) {
        return stockValidationService.validateStockInconsistency(transactionType, entityID, null, null, ProcessType.DELETE);
    }

    @Override
    public SelectItem validateStockInconsistencyInAdjustProcess(StockTransactionType transactionType, Integer
            entityID, QuantityItem[] qItems) {
        return stockValidationService.validateStockInconsistency(transactionType, entityID, qItems, null, ProcessType.ADJUST);
    }

    @Override
    public SelectItem validateStockInconsistencyInUnbuildAssembly(Integer[] assemblyTransactionIds) {
        return stockValidationService.validateStockInconsistency(StockTransactionType.ASSEMBLY, null, null, assemblyTransactionIds, ProcessType.DELETE);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String[] validateBatchSerials(HashMap<Integer, ArrayList<ProductSerialItem>> serialItems) {
        List<String> errors = new ArrayList<>();

        HashMap<Integer, Map<String, ProductSerialItem>> sorted = new HashMap<>();
        for (Map.Entry<Integer, ArrayList<ProductSerialItem>> entry : serialItems.entrySet()) {
            if (sorted.containsKey(entry.getKey())) {
                Map<String, ProductSerialItem> itemSerial = sorted.get(entry.getKey());
                for (ProductSerialItem productSerialItem : entry.getValue()) {
                    if (itemSerial.containsKey(productSerialItem.getSerial())) {
                        itemSerial.get(productSerialItem.getSerial()).setQty(itemSerial.get(productSerialItem.getSerial()).getQty().add(productSerialItem.getQty()));
                    } else {
                        itemSerial.put(productSerialItem.getSerial(), productSerialItem);
                    }
                }
            } else {
                Map<String, ProductSerialItem> itemSerial = new HashMap<>();
                for (ProductSerialItem productSerialItem : entry.getValue()) {
                    String key = productSerialItem.getSerial();
                    if (itemSerial.containsKey(key)) {
                        itemSerial.get(productSerialItem.getSerial()).setQty(itemSerial.get(key).getQty().add(productSerialItem.getQty()));
                    } else {
                        itemSerial.put(key, productSerialItem);
                    }
                }
                sorted.put(entry.getKey(), itemSerial);
            }
        }

        for (Map.Entry<Integer, Map<String, ProductSerialItem>> entry : sorted.entrySet()) {
            Map<String, ProductSerialItem> productSerialMap = entry.getValue();
            for (Map.Entry<String, ProductSerialItem> productSerialItem : productSerialMap.entrySet()) {
                ProductSerialItem serialItem = productSerialItem.getValue();
                Integer serialsQTY = productSerialManager.getProductSerialsQty(entry.getKey(), serialItem.getSerial().split(" ")[0], serialItem.getExpirationDate());
                if (serialsQTY < serialItem.getQty().intValue()) {
                    errors.add(serialItem.getSerial());
                }
            }
        }
        return errors.toArray(new String[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public BigDecimal countItemsInStock(QuantityItem qItem) {
        return stockValidationService.countItemsInStock(qItem);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String[] validateItemsInConsignmentToSell(QuantityItem[] items, Integer invoiceId) {
        List<String> itemsOutOfStock = new LinkedList<>();
        if (items != null) {
            for (QuantityItem qItem : items) {
                if (qItem.getId() != null) {
                    EdsItem item = itemManager.get(qItem.getId());

                    if (item == null || !NON_INVENTORY_ITEM.equals(item.getType())) {
                        continue;
                    }
                    BigDecimal conQtyToSale = consignmentManager.getConsignmentQtyToSell(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()), qItem.getId(), invoiceId);
                    if (qItem.getQuantity().compareTo(conQtyToSale) > 0) {
                        itemsOutOfStock.add(item.getName());
                    }
                }
            }
        }
        return itemsOutOfStock.toArray(new String[]{});
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String[] validateItemsInConsignment(QuantityItem[] items, Integer invoiceId) {
        List<String> itemsOutOfStock = new LinkedList<>();
        if (items != null) {
            for (QuantityItem qItem : items) {
                if (qItem.getId() != null) {
                    EdsItem item = itemManager.get(qItem.getId());

                    if (item == null || SERVICE.equals(item.getType())) {
                        continue;
                    }
                    BigDecimal conQtyToPurchase = consignmentManager.getConsignmentQtyToPurchase(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()), qItem.getId(), invoiceId);
                    if (qItem.getQuantity().compareTo(conQtyToPurchase) > 0) {
                        itemsOutOfStock.add(item.getName());
                    }
                }
            }
        }
        return itemsOutOfStock.toArray(new String[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getNumberForDueDate() {
        return invoicingSettingsManager.getInvoiceSettings(invoiceManager.getUser().getCompany()).getPaymentDue();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPaymentInstructions(String type) {
        Integer typeAsInt = SALE_INVOICE.equals(type) ? EdsPaymentInstruction.SALES_INVOICE_PAYMENT_INSTRUCTION :
                PURCHASE_ORDER.equals(type) ? EdsPaymentInstruction.PURCHASE_ORDER_TERMS_CONDITIONS :
                PURCHASE_INVOICE.equals(type) ? EdsPaymentInstruction.PURCHASE_INVOICE_PAYMENT_INSTRUCTION :
                SALE_ORDER_CODE.equals(type) ? EdsPaymentInstruction.SALES_ORDER_PAYMENT_INSTRUCTION : EdsPaymentInstruction.SALES_QUOTE_TERMS_CONDITIONS;
        List<EdsPaymentInstruction> instructions = paymentInstructionManager.getInstructions(typeAsInt);
        SelectItem[] items = new SelectItem[instructions.size()];
        int i = 0;
        for (EdsPaymentInstruction pi : instructions) {
            String name;
            if (pi.getText() != null && !"".equals(pi.getText().trim())) {
                name = pi.getText().trim().length() > 30 ? pi.getText().trim().substring(0, 30) + "..." : pi.getText();
            } else {
                name = "(no data)";
            }
            items[i++] = new SelectItem(pi.getObjectID(), name, pi.getText() != null ? pi.getText() : "");
        }
        return items;
    }

    private NewInvoiceItem[] setCopyItems(NewInvoiceItem[] items) {
        for (final NewInvoiceItem lineItem : items) {
            lineItem.setID(null);
        }
        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPaymentIntroduction(String type) {
        Integer typeAsInt = SALE_INVOICE_INTR.equals(type) ? EdsPaymentInstruction.SALES_INVOICE_INTRODUCTION :
                SALE_QUOTE_INTR.equals(type) ? EdsPaymentInstruction.SALES_QUOTE_INTRODUCTION :
                SALE_ORDER_INTR.equals(type) ? EdsPaymentInstruction.SALES_ORDER_INTRODUCTION :
                REQUEST_FOR_QUOTE_INTR.equals(type) ? EdsPaymentInstruction.REQUEST_FOR_QUOTE_INTRODUCTION :
                null;
        List<EdsPaymentInstruction> instructions = paymentInstructionManager.getInstructions(typeAsInt);
        SelectItem[] items = new SelectItem[instructions.size()];
        int i = 0;
        for (EdsPaymentInstruction pi : instructions) {
            String name;
            if (pi.getText() != null && !"".equals(pi.getText().trim())) {
                name = pi.getText().trim().length() > 30 ? pi.getText().trim().substring(0, 30) + "..." : pi.getText();
            } else {
                name = "(no data)";
            }
            items[i++] = new SelectItem(pi.getObjectID(), name, pi.getText() != null ? pi.getText() : "");
        }
        return items;
    }

    @Transactional
    public Integer deleteInvoice(Integer objectID, String type) {

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityId(objectID);
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        if (SALE_INVOICE.equals(type)) {
            EdsSaleInvoice inv = invoiceManager.getSaleInvoice(objectID);
            Boolean hasCreditNote = invoicePaymentManager.hasCreditDebitNote(objectID);

            if (hasCreditNote) return -2;

            List<EdsShippingData> convertedShippingData = shippingDataManager.getGrnGdnsByInvoiceId(inv.getObjectID());
            EdsInvoiceTransaction invoiceTransaction = transactionManager.getTransactionByInvoice(inv);
            if (invoiceTransaction != null && invoiceTransaction.getFiledVatId() != null && "REPORTED".equals(inv.getZatcaStatus())) {
                return -3;
            }

            kpiLog.setEntityName(EdsSaleInvoice.class.getSimpleName());
            ServerUtils.kpiLog(log, kpiLog, "Delete Sale Invoice");
            invoiceQuoteNoteManager.deleteInvoiceQuoteNotes(objectID, true);
            productSerialManager.removeSalesInvoiceFromProductSerials(objectID);

            if (inv.getExpense() != null && inv.getExpense().size() > 0) {
                for (EdsExpense exp : inv.getExpense()) {
                    exp.setInvoice(null);
                    expenseManager.update(exp);
                }
            }
            if (inv.getItemsAsExpense() != null && !inv.getItemsAsExpense().isEmpty()) {
                for (EdsInvoiceItem exp : inv.getItemsAsExpense()) {
                    exp.setSaleInvoice(null);
                    invoiceItemManager.update(exp);
                }
            }
            if (inv.getMjItemsAsExpense() != null && !inv.getMjItemsAsExpense().isEmpty()) {
                for (EdsManualJournalItem exp : inv.getMjItemsAsExpense()) {
                    exp.setInvoice(null);
                }
            }
            if (inv.getBtItemsAsExpense() != null && !inv.getBtItemsAsExpense().isEmpty()) {
                for (EdsBankTransferItem exp : inv.getBtItemsAsExpense()) {
                    exp.setInvoice(null);
                }
            }
            List<Integer> invoiceItemsIDList = new LinkedList<>();
            List<EdsInvoiceItem> invoiceItems = inv.getInvoiceItems();
            for (EdsInvoiceItem ii : invoiceItems) {
                invoiceItemsIDList.add(ii.getObjectID());
            }
            if (inv.isProjectBasedInvoice()) {
                invoiceItemManager.removeTimesheetRelationsForInvoiceItems(invoiceItemsIDList);
            }
            itemSerialService.deleteForInvoice(inv);

            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(SaleInvoiceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, inv, userManager.getUser());
            event.setCustomStringField(inv.getNumber());

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, inv, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_SALEINVOICE);

            if (!DRAFT.equals(inv.getStatus().getCode())) {
                deleteInvoiceTransaction(inv.getObjectID(), type);
            }

            if (inv.isCreditNote()) {
                if (inv.getRefunds() != null && !inv.getRefunds().isEmpty()) {
                    for (EdsInvoicePayment payment : inv.getRefunds()) {
                        deletePayment(payment.getObjectID());
                    }
                }
            }

            invoiceManager.removeInvoiceItems(inv.getObjectID());
            courseScheduleStudentManager.removeInvoiceFromTrainingData(inv.getObjectID());

            inv.setDeleted(true);
            String progressInvoiceType = null;
            if (inv.getConvertedQuotes() != null && inv.getConvertedQuotes().size() == 1) {
                for (EdsQuote q : inv.getConvertedQuotes()) {
                    if (q instanceof EdsSaleQuote) {
                        if (((EdsSaleQuote) q).isProgressInvoicing()) {
                            progressInvoiceType = ((EdsSaleQuote) q).getProgressInvoicingType();
                        }
                    }
                }
            }

            invoiceManager.update(inv);

            Integer convertedQuoteId = null;
            String methodKey = "deleteSI";
            if (inv.getQuoteNumberCN() != null) {
                List<EdsSaleQuote> saleQuoteList = quoteManager.getQuoteByNumber(inv.getQuoteNumberCN());
                if (saleQuoteList != null && !saleQuoteList.isEmpty()) {
                    convertedQuoteId = saleQuoteList.get(0).getObjectID();
                    methodKey = "deleteCN";
                }
            }
            EdsBusinessEvent eventUpdateSOQ = baseEventPostProcessor.registerEvent(SaleInvoiceCustomEventListenerImpl.TYPE, SaleInvoiceCustomEventListenerImpl.EVENT_UPDATE_SOQ_INVOICING_DATA, inv, null);
            eventUpdateSOQ.setCustomStringField(new Gson().toJson(new ConvertedQuotesDto(methodKey, progressInvoiceType, convertedQuoteId != null ? Collections.singletonList(convertedQuoteId) : null)));
            fixedAssetService.deleteSalesInvoiceRelatedFixedAsset(inv.getObjectID());
//            deleteSaleInvoiceInSolr(inv.getObjectID());

            if (convertedShippingData != null && !convertedShippingData.isEmpty()) {
                for (EdsShippingData data : convertedShippingData) {
                    try {
                        data.setStatus(ShippingDataStatus.SUCCESSFUL);
                        shippingDataSolrComponent.index(data);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                shippingDataManager.updateAll(convertedShippingData, convertedShippingData.size());
            }

            trashBinManager.saveTrashBin(objectID, SALE_INVOICE, inv.getNumber());
            baseEventPostProcessor.registerEvent(SaleInvoiceCustomEventListenerImpl.TYPE, SaleInvoiceCustomEventListenerImpl.EVENT_SALEINVOICE_DELETE_FROM_SOLR, inv, null);
        } else if (RECURRING_INVOICE.equals(type)) {
            invoiceQuoteNoteManager.deleteInvoiceQuoteNotes(objectID, true);
            EdsBaseSaleInvoice inv = (EdsBaseSaleInvoice) invoiceManager.get(objectID);
            recurrenceService.deleteRecurrence(inv.getObjectID(), SchedulerConstant.RECURRING_INVOICE_REMINDER);

            //clear invoice transaction if there was
            EdsInvoiceTransaction invoiceTransaction = transactionManager.getTransactionByInvoice(inv);
            if (invoiceTransaction != null && invoiceTransaction.getObjectID() != null) {
                invoiceTransaction.setDeleted(true);
                transactionManager.update(invoiceTransaction);
            }
            inv.setDeleted(true);
            invoiceManager.update(inv);
            EdsRecurringInvoice invoice = (EdsRecurringInvoice) inv;
            baseEventPostProcessor.registerEvent(RecurringInvoiceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, invoice, userManager.getUser());

            kpiLog.setEntityName(EdsRecurringInvoice.class.getSimpleName());
            ServerUtils.kpiLog(log, kpiLog, "Delete Recurring Invoice");

            trashBinManager.saveTrashBin(objectID, RECURRING_INVOICE, "");
        } else if (PURCHASE_INVOICE.equals(type)) {
            EdsPurchaseInvoice inv = invoiceManager.getPurchaseInvoice(objectID);
            Boolean hasDebitNote = invoicePaymentManager.hasCreditDebitNote(objectID);

            if (hasDebitNote) return -2;

            List<EdsShippingData> convertedShippingData = shippingDataManager.getGrnGdnsByInvoiceId(inv.getObjectID());
            EdsInvoiceTransaction invoiceTransaction = transactionManager.getTransactionByInvoice(inv);
            if (invoiceTransaction != null && invoiceTransaction.getFiledVatId() != null) {
                return -3;
            }
            if (inv.getVatReturnId() != null) {
                return -3;
            }

            for (EdsInvoiceItem item : inv.getInvoiceItems()) {
                if (item.getSaleInvoice() != null && item.getSaleInvoice().getObjectID() != null) {
                    return -1;
                }
                if (item.getItem() != null && item.getItem().getTrackBatchesEnabled()) {
                    itemBatchManager.deleteBatchesByEntity(inv.getObjectID(), item.getItem().getObjectID(), ItemSerialEntityType.PURCHASE_INVOICE.name());
                }
            }

            kpiLog.setEntityName(EdsPurchaseInvoice.class.getSimpleName());
            ServerUtils.kpiLog(log, kpiLog, "Delete Purchase Invoice");
            invoiceQuoteNoteManager.deleteInvoiceQuoteNotes(objectID, true);

            if (!DRAFT.equals(inv.getStatus().getCode())) {
                deleteInvoiceTransaction(inv.getObjectID(), type);
            }
            if (inv.isCreditNote()) {
                if (inv.getRefunds() != null && !inv.getRefunds().isEmpty()) {
                    for (EdsInvoicePayment payment : inv.getRefunds()) {
                        deletePayment(payment.getObjectID());
                    }
                }
            }
            itemSerialService.deleteForInvoice(inv);
            invoiceManager.removeInvoiceItems(inv.getObjectID());
            inv.setDeleted(true);
            invoiceManager.update(inv);
            baseEventPostProcessor.registerEvent(PurchaseInvoiceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, inv, userManager.getUser());

            updateConvertedPurchaseInvoiceData(inv, "deletePI");
            fixedAssetService.updatePurchaseInvoiceRelatedFixedAsset(inv.getObjectID(), true);
            invoiceManager.removeRelationFromQuote(inv.getObjectID());

            if (convertedShippingData != null && !convertedShippingData.isEmpty()) {
                for (EdsShippingData shippingData : convertedShippingData) {
                    try {
                        shippingData.setStatus(ShippingDataStatus.SUCCESSFUL);
                        shippingDataSolrComponent.index(shippingData);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                shippingDataManager.updateAll(convertedShippingData, convertedShippingData.size());
            }
            trashBinManager.saveTrashBin(objectID, PURCHASE_INVOICE, inv.getNumber());
            deletePurchaseInvoiceInSolr(inv.getObjectID());
        } else if (RECURRING_BILL.equals(type)) {
            invoiceQuoteNoteManager.deleteInvoiceQuoteNotes(objectID, true);
            EdsBasePurchaseInvoice inv = (EdsBasePurchaseInvoice) invoiceManager.get(objectID);
            recurrenceService.deleteRecurrence(inv.getObjectID(), SchedulerConstant.RECURRING_BILL_REMINDER);

            EdsInvoiceTransaction invoiceTransaction = transactionManager.getTransactionByInvoice(inv);
            if (invoiceTransaction != null && invoiceTransaction.getObjectID() != null) {
                invoiceTransaction.setDeleted(true);
                transactionManager.update(invoiceTransaction);
            }
            inv.setDeleted(true);
            invoiceManager.update(inv);
            kpiLog.setEntityName(EdsRecurringInvoice.class.getSimpleName());
            ServerUtils.kpiLog(log, kpiLog, "Delete Recurring Bill");

            trashBinManager.saveTrashBin(objectID, RECURRING_BILL, "");
        }
        try {
            EdsInvoice invoice = invoiceManager.get(objectID);
            EdsFifoFailure failure = fifoFailureManager.getByEntityId(objectID, SALE_INVOICE.equals(type) ? (invoice.isCreditNote() ? EntityType.CUSTOMER_CREDIT_NOTE : EntityType.SALES_INVOICE) : (invoice.isCreditNote() ? EntityType.SUPPLIER_CREDIT_NOTE : EntityType.PURCHASE_INVOICE), ServerSecurityContext.getInstance().getCompanyId());
            if (failure != null) {
                failure.setDeleted(true);
                failure.setOnQue(false);
                fifoFailureManager.update(failure);
            }
        } catch (Exception e) {
            log.error("An error occurred while updating EdsFifoFailure.");
        }

        return objectID;
    }

    @Override
    public void deleteInvoiceTransaction(Integer invoiceId, String type) {
        EdsInvoice inv = invoiceManager.get(invoiceId);
        EdsInvoiceTransaction invoiceTransaction = transactionManager.getTransactionByInvoice(inv);

        if (invoiceTransaction != null) {
            List<EdsShippingData> convertedShippingData = shippingDataManager.getGrnGdnsByInvoiceId(inv.getObjectID());
            EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
            financialSettingsManager.update(fs);
            transactionManager.setChangedAccountsForRecalculate(invoiceTransaction.getObjectID());
            invoiceTransaction.setDeleted(true);
            transactionManager.update(invoiceTransaction);
            reindexStockProductOnStockRemoval(invoiceTransaction);
            if (inv.getStatus().getCode().equals(REVERSED)) {
                EdsTransaction reversedTransaction = transactionManager.getTransactionByReversedId(invoiceTransaction.getObjectID());
                if (reversedTransaction instanceof EdsInvoiceTransaction) {
                    reversedTransaction.setDeleted(true);
                    transactionManager.update(reversedTransaction);
                }
            }

            if (inv.isCreditNote()) {
                baseEventPostProcessor.registerEvent(TransactionCustomEventListenerImpl.TYPE, SALE_INVOICE.equals(type) ? TransactionCustomEventListenerImpl.EVENT_REMOVE_CUSTOMER_CREDIT_NOTE_TRANSACTION : TransactionCustomEventListenerImpl.EVENT_REMOVE_SUPPLIER_CREDIT_NOTE_TRANSACTION, inv, userManager.getUser());
            } else if (!inv.isFixedAssetRelated() && CollectionUtils.isEmpty(convertedShippingData)) {
                baseEventPostProcessor.registerEvent(TransactionCustomEventListenerImpl.TYPE, SALE_INVOICE.equals(type) ? TransactionCustomEventListenerImpl.EVENT_REMOVE_SALES_INVOICE_TRANSACTION : TransactionCustomEventListenerImpl.EVENT_REMOVE_PURCHASE_INVOICE_TRANSACTION, inv, userManager.getUser());
            }
            baseEventPostProcessor.registerEvent(DeferredTransactionCustomEventListenerImpl.TYPE, DeferredTransactionCustomEventListenerImpl.EVENT_REMOVE_INVOICE_DEFERRED_TRANSACTION, inv, null);
        }
        if (inv.getPayments() != null && inv.getPayments().size() > 0) {
            for (EdsInvoicePayment payment : inv.getPayments()) {
                deletePayment(payment.getObjectID());
            }
        }
    }

    private void reindexStockProductOnStockRemoval(EdsTransaction transaction) {
        List<EdsItem> items = itemManager.getStockItemProductByTransaction(transaction.getObjectID());
        itemStockManager.deleteItemStocksByTransaction(transaction.getObjectID());
        for (EdsItem item : items) {
            try {
                productsServicesSolrComponent.index(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    @Override
    public Integer voidInvoice(Integer invoiceID, DateNonConvertable voidDate) {
        EdsInvoice invoice = invoiceManager.get(invoiceID);
        if (invoice instanceof EdsPurchaseInvoice || (invoice instanceof EdsSaleInvoice && invoice.isCreditNote())) {
            for (EdsInvoiceItem item : invoice.getInvoiceItems()) {
                if (item.getSaleInvoice() != null && item.getSaleInvoice().getObjectID() != null) {
                    return -3;
                }
            }
        }

        if (invoice.getExpense() != null && invoice.getExpense().size() > 0) {
            for (EdsExpense exp : invoice.getExpense()) {
                exp.setInvoice(null);
                expenseManager.update(exp);
            }
        }
        if (invoice.getItemsAsExpense() != null && !invoice.getItemsAsExpense().isEmpty()) {
            for (EdsInvoiceItem exp : invoice.getItemsAsExpense()) {
                exp.setSaleInvoice(null);
                invoiceItemManager.update(exp);
            }
        }

        List<EdsInvoicePayment> payments = invoicePaymentManager.getPayments(invoice);
        if (!payments.isEmpty()) {
            return 0;
        }
        recurrenceService.deleteInvoiceRecurrenceIfExists(invoiceID);

        Integer transactionID = accountingServiceLocal.voidInvoiceTransactions(invoiceID, voidDate);
        baseEventPostProcessor.registerEvent(DeferredTransactionCustomEventListenerImpl.TYPE, DeferredTransactionCustomEventListenerImpl.EVENT_REMOVE_INVOICE_DEFERRED_TRANSACTION, invoice, null);

        if (invoice.isCreditNote()) {
            EdsInvoicePayment payment = invoiceManager.getInvoicePaymentByCreditNote(invoice);
            if (payment != null) {
                deletePayment(payment.getObjectID());
            }
        }

        itemSerialService.voidForInvoice(invoice, transactionID);

        invoice.setStatus(referenceManager.findReference(INVOICE_STATUS, REVERSED));
        List<EdsShippingData> shippingDataList = new ArrayList<>(invoice.getConvertedShippingData());

        if (invoice instanceof EdsSaleInvoice) {
            addInvoiceToSolr(invoice);
            EdsSaleInvoice saleInv = invoiceManager.getSaleInvoice(invoiceID);

            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(SaleInvoiceCustomEventListenerImpl.TYPE, SaleInvoiceCustomEventListenerImpl.EVENT_UPDATE_SOQ_INVOICING_DATA, saleInv, null);
            event.setCustomStringField(new Gson().toJson(new ConvertedQuotesDto("deleteSI")));

            fixedAssetService.voidSalesInvoiceRelatedFixedAsset(invoice.getObjectID(), voidDate);

            if (invoice.isCreditNote()) {
                baseEventPostProcessor.registerEvent(TransactionCustomEventListenerImpl.TYPE, TransactionCustomEventListenerImpl.EVENT_REMOVE_CUSTOMER_CREDIT_NOTE_TRANSACTION, invoice, userManager.getUser());
            } else {
                baseEventPostProcessor.registerEvent(TransactionCustomEventListenerImpl.TYPE, TransactionCustomEventListenerImpl.EVENT_REMOVE_SALES_INVOICE_TRANSACTION, invoice, userManager.getUser());
            }
        } else if (invoice instanceof EdsPurchaseInvoice) {
            try {
                solrManager.indexAddPurchaseInvoice((EdsPurchaseInvoice) invoice);
            } catch (IOException | SolrServerException e) {
                e.printStackTrace();
            }

            updateConvertedPurchaseInvoiceData((EdsPurchaseInvoice) invoice, "voidPI");
            fixedAssetService.updatePurchaseInvoiceRelatedFixedAsset(invoice.getObjectID(), false);

            if (invoice.isCreditNote()) {
                baseEventPostProcessor.registerEvent(TransactionCustomEventListenerImpl.TYPE, TransactionCustomEventListenerImpl.EVENT_REMOVE_SUPPLIER_CREDIT_NOTE_TRANSACTION, invoice, userManager.getUser());
            } else {
                baseEventPostProcessor.registerEvent(TransactionCustomEventListenerImpl.TYPE, TransactionCustomEventListenerImpl.EVENT_REMOVE_PURCHASE_INVOICE_TRANSACTION, invoice, userManager.getUser());
            }
        }

        return transactionID;
    }

    public TaxList getCompanyTaxList(ListingFilterParameter filterParametrs, ListLoadConfig config) {
        List<EdsVat> taxList = accountingServiceLocal.companyVatList(filterParametrs, null);
        return createCompanyTaxList(taxList);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TaxListData getAccountingTaxList(ListingFilterParameter filterParametrs) {
        TaxListData result = new TaxListData();
        Integer taxRateScale = financialSettingsManager.getFinancialSettings().getTaxRateScale();

        result.setVatReturnEnabled(isVatReturnEnabled());
        List<EdsVat> taxList = accountingServiceLocal.companyVatList(filterParametrs, null);
        int totalCount = accountingServiceLocal.companyVatListCount(filterParametrs, null);
        TaxListItem[] items = new TaxListItem[taxList.size()];
        int i = 0;
        for (EdsVat t : taxList) {
            items[i] = new TaxListItem();
            items[i].setObjectID(t.getObjectID());
            items[i].setName(t.getTaxNameAndRateAsString());
            items[i].setPercent(t.getTaxRateAsBigDecimal());
            items[i].setPermissionType(t.getPermissionType());
            items[i].setType(t.getTaxType());
            items[i].setSelectedByDefault(t.isSelectedByTaxDefault());

            i++;
        }
        result.setTaxList(new ListResult<TaxListItem>(new ArrayList<>(Arrays.asList(items)), totalCount));
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public MessageItem getRecurringInvoiceMessageItem(Integer invoiceID) {
        MessageItem item = new MessageItem();
        EdsRecurringInvoice recurringInvoice = (EdsRecurringInvoice) invoiceManager.get(invoiceID);
        item.setSubject(recurringInvoice.getEmailSubject());
        item.setSendCopyToMe(recurringInvoice.getSendCopyToMe() != null ? recurringInvoice.getSendCopyToMe() : false);
        item.setContactId(recurringInvoice.getContact() != null ? recurringInvoice.getContact().getObjectID() : null);
        item.setSenderID(recurringInvoice.getSender().getObjectID());
        item.setEmailTemplateID(recurringInvoice.getEmailTemplate() != null ? recurringInvoice.getEmailTemplate().getObjectID() : null);
        item.setPdfTemplateID(recurringInvoice.getPdfTemplate() != null ? recurringInvoice.getPdfTemplate().getObjectID() : null);
        item.setToEmails(recurringInvoice.getToEmails());
        item.setCc(recurringInvoice.getCc());
        item.setBcc(recurringInvoice.getBcc());
        item.setFromEmail(recurringInvoice.getFromEmail());
        item.setReplyTo(recurringInvoice.getReplyTo());
        if (recurringInvoice.getFileIDs() != null && recurringInvoice.getFileIDs().size() > 0) {
            ArrayList<FileResource> uploads = new ArrayList<>();
            for (EdsUpload upload : recurringInvoice.getFileIDs()) {
                FileResource fileResource = new FileResource();
                fileResource.setObjectId(upload.getObjectID());
                fileResource.setName(upload.getName());
                uploads.add(fileResource);
            }
            item.setFileResources(uploads);
        }

        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceNumberData getCreditNoteNumber() {
        return getCreditNoteNumber(null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceNumberData getCreditNoteNumber(DateNonConvertable invoiceDate) {
        InvoiceNumberData numberData = new InvoiceNumberData();
        EdsCompany company = roleManager.getUser().getCompany();
        EdsInvoicingSettings setting = invoicingSettingsManager.getInvoiceSettings(company);//for ex.: "INV_data_clientcode_0001";
        Integer fourDigitNumber = invoiceManager.getCreditNoteFourDigitNumber(invoiceDate);
        String numberFormat = setting.getInvoiceCreditNoteNumberingFormat();
        if (numberFormat == null) {
            numberFormat = "CN_0001";
        }
        //it will be run for only enable invoice custom type
        parseNumber(numberFormat, numberData, fourDigitNumber, "clientcode");
        return numberData;
    }

    private void parseNumber(String number, InvoiceNumberData data, Integer fourDigitNumber, String crmAccountCode) {
        String[] partNumbers = number.split("_");
        int parametersCount = 0;
        switch (partNumbers.length) {
            case 5 -> {
                data.setPrefix(partNumbers[0]);
                data.setWithDate(true);
                data.setWithClient(true);
                data.setWithProject(true);
            }
            case 4 -> {
                if (number.contains("date")) {
                    data.setWithDate(true);
                    parametersCount++;
                }
                if (number.contains(crmAccountCode)) {
                    data.setWithClient(true);
                    parametersCount++;
                }
                if (number.contains("projectcode")) {
                    data.setWithProject(true);
                    parametersCount++;
                }
                if (parametersCount != 3) {
                    data.setPrefix(partNumbers[0]);
                }
            }
            case 3 -> {
                if (number.contains("date")) {
                    data.setWithDate(true);
                    parametersCount++;
                }
                if (number.contains(crmAccountCode)) {
                    data.setWithClient(true);
                    parametersCount++;
                }
                if (number.contains("projectcode")) {
                    data.setWithProject(true);
                    parametersCount++;
                }
                if (parametersCount != 2) {
                    data.setPrefix(partNumbers[0]);
                }
            }
            case 2 -> {
                if (number.contains("date")) {
                    data.setWithDate(true);
                    parametersCount++;
                }
                if (number.contains(crmAccountCode)) {
                    data.setWithClient(true);
                    parametersCount++;
                }
                if (number.contains("projectcode")) {
                    data.setWithProject(true);
                    parametersCount++;
                }
                if (parametersCount != 1) {
                    data.setPrefix(partNumbers[0]);
                }
            }
        }
        String lastFourNumber = number.substring(number.length() - 4);

        Integer intLastFourNumber = 1;
        try {
            intLastFourNumber = Integer.parseInt(lastFourNumber);
        } catch (NumberFormatException ignored) {
        }

        DecimalFormat format = new DecimalFormat("0000");
        data.setFourDigitNumber((fourDigitNumber != null && fourDigitNumber.compareTo(intLastFourNumber) >= 0) ? format.format(fourDigitNumber + 1) : lastFourNumber);
    }

    @Override
    public SaveResult saveCreditNote(NewInvoice data) {
        EdsInvoice creditNote;
        SaveResult saveResult = new SaveResult();

        if (RECEIVABLE.equals(data.getType()) && isSaleInvoiceExists(data.getInvoiceNumber())) {
            saveResult.setInvoiceExist(true);
            return saveResult;
        } else if (PAYABLE.equals(data.getType()) && !StringUtil.isEmpty(data.getInvoiceNumber())) {
            List<EdsPurchaseInvoice> existingCreditNotes = invoiceManager.getPurchaseInvoiceByNumber(data.getInvoiceNumber(), data.getClientID(), null);

            if (existingCreditNotes != null && !existingCreditNotes.isEmpty()) {
                saveResult.setInvoiceExist(true);
                return saveResult;
            }
        }
        if (RECEIVABLE.equals(data.getType())) {
            creditNote = new EdsSaleInvoice();
            ((EdsSaleInvoice) creditNote).setClient(clientManager.get(data.getClientID()));
            final String fourDigitNumber = data.getFourDigitNumber() != null ? data.getFourDigitNumber() : getCreditNoteNumber().getFourDigitNumber();

            ((EdsSaleInvoice) creditNote).setFourDigitNumber(Integer.valueOf(fourDigitNumber));
            creditNote.setCreditNote(true);
            creditNote.setUpdatedDate(new Date());
            if (data.getShippingMethodID() != null) {
                ((EdsSaleInvoice) creditNote).setShippingMethod(shippingMethodManager.get(data.getShippingMethodID()));
            }
            backfillFaiReportedDate(creditNote);
        } else {
            creditNote = new EdsPurchaseInvoice();
            ((EdsPurchaseInvoice) creditNote).setSupplier(crmAccountManager.get(data.getClientID()));
            creditNote.setCreditNote(true);
            creditNote.setUpdatedDate(new Date());
            String fourDigitNumber = data.getFourDigitNumber() != null ? data.getFourDigitNumber() : getPurchaseInvoiceNumber(true).getFourDigitNumber();

            ((EdsPurchaseInvoice) creditNote).setFourDigitNumber(Integer.valueOf(fourDigitNumber));
            creditNote.setReverseChargeApplicable(data.isReversechargeApplicable());
        }
        super.initInvoiceData(creditNote, data);
        creditNote.setTotalDiscount(data.getTotalDiscount());
        creditNote.setTaxCalculationType(data.getTaxCalculationType());
        creditNote.setRelatedProject(data.getRelatedProjectID() != null ? projectManager.get(data.getRelatedProjectID()) : null);
        creditNote.setPriceLevelID(data.getPriceLevel() != null ? data.getPriceLevel().getId() : null);
        creditNote.setNoteReason(data.getNoteReason());
        creditNote.setNotePaymentCode(data.getPaymentTypeCode());

        if (data.getCreditedInvoiceID() != null) {
            creditNote.setCreditNoteInvoice(invoiceManager.get(data.getCreditedInvoiceID()));
        }
        this.initCreditNoteTaxTotals(creditNote, data.getTotalTaxItems());
        creditNote.setCustomFields(this.createInvoiceCustomFields(data.getCustomFieldItems()));
        Integer objectID = this.initCreditNoteItemsForSave(data, creditNote, false);

        if (data.getAttachments() != null && data.getAttachments().length > 0) {
            attachmentUtilsManager.saveAttachments(F_SALE_INV, creditNote.getObjectID(), creditNote.getObjectID(), data.getAttachments());
        }
        this.createInvoiceNoteAndHistory(data, creditNote);

        if (RECEIVABLE.equals(data.getType())) {
            baseEventPostProcessor.registerEvent(SaleInvoiceEventListenerImpl.TYPE, SaleInvoiceEventListenerImpl.EVENT_SALES_INVOICE_ADD_CREDIT_NOTE, (EdsSaleInvoice) creditNote, userManager.getUser());
        } else {
            baseEventPostProcessor.registerEvent(PurchaseInvoiceEventListenerImpl.TYPE, PurchaseInvoiceEventListenerImpl.EVENT_PURCHASE_INVOICE_ADD_CREDIT_NOTE, (EdsPurchaseInvoice) creditNote, userManager.getUser());
        }

        if (PAYABLE.equals(data.getType())) {
            try {
                purchaseInvoiceSolrComponent.index((EdsPurchaseInvoice) creditNote);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (RECEIVABLE.equals(data.getType())) {
            if (creditNote.getCreditNoteInvoice() != null) {
                try {
                    saleInvoiceSolrComponent.index((EdsSaleInvoice) creditNote.getCreditNoteInvoice());
                } catch (IOException | SolrServerException | InterruptedException e) {
                    e.printStackTrace();
                }
                EdsBusinessEvent event = baseEventPostProcessor.registerEvent(SaleInvoiceCustomEventListenerImpl.TYPE, SaleInvoiceCustomEventListenerImpl.EVENT_UPDATE_SOQ_INVOICING_DATA, (EdsSaleInvoice) creditNote.getCreditNoteInvoice(), userManager.getUser());
                ConvertedQuotesDto dto = new ConvertedQuotesDto("saveCN", data.getProgressInvoicingType(), !CollectionUtils.isEmpty(data.getConvertedQuoteIDs()) ? data.getConvertedQuoteIDs() : null);
                event.setCustomStringField(new Gson().toJson(dto));
            }
        }

        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, creditNote, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_CREDIT_NOTE);

        saveResult.setId(objectID);
        saveResult.setNumber(creditNote.getNumber());
        return saveResult;
    }

    private void initCreditNoteTaxTotals(EdsInvoice creditNote, TotalTaxItem[] totalTaxItems) {
        if (creditNote.getObjectID() != null) {
            invoiceManager.deleteInvoiceOldTaxTotals(creditNote);
        }
        if (totalTaxItems != null) {
            List<EdsInvoiceTaxTotal> totalTaxes = new LinkedList<>();
            for (TotalTaxItem item : totalTaxItems) {
                EdsInvoiceTaxTotal totalTax = new EdsInvoiceTaxTotal();
                totalTax.setInvoice(creditNote);
                totalTax.setVat(vatManager.get(item.getTaxItem().getId()));
                totalTax.setAmount(item.getTaxAmount());
                totalTaxes.add(totalTax);
            }
            creditNote.setInvoiceTaxTotals(totalTaxes);
        }
    }

    private Integer initCreditNoteItemsForSave(NewInvoice data, EdsInvoice creditNote, boolean edit) {
        List<EdsInvoiceItem> items = new LinkedList<>();
        if (edit) {
            try {
                invoiceManager.deleteInvoiceItems(data.getID());
                itemBatchService.deleteBatchSerialsForInvoice(creditNote);
            } catch (Exception e) {
                log.info("Can't delete credit note items. Credit Note ID: {}", data.getID());
            }
        }
        if (data.getCreditedInvoiceID() == null && creditNote.getCreditNoteInvoice() != null) {
            data.setCreditedInvoiceID(creditNote.getCreditNoteInvoice().getObjectID());
        }

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        boolean isMultipleWarehouseEnabled = financialSettings.getEnableMultiWarehouse();
        EdsWarehouse defaultWarehouse = warehouseManager.getDefaultWarehouse();

        for (NewInvoiceItem newItem : data.getItems()) {
            EdsInvoiceItem localItem = new EdsInvoiceItem();
            if (!isMultipleWarehouseEnabled) {
                localItem.setWarehouse(defaultWarehouse);
            }
            super.initInvoiceItemData(localItem, newItem);
            localItem.setCustomFields(createInvoiceItemCustomFields(newItem.getCustomFieldItems()));
            localItem.setSerials(newItem.getSerials());
            localItem.setBatchItems(newItem.getBatchItems());
            localItem.setFaiCategoryId(newItem.getFaiCategoryId());
            //invoiceItemManager.create(localItem);
            items.add(localItem);
            localItem.setInvoice(creditNote);
        }

        creditNote.setInvoiceItems(items);
        invoiceManager.create(creditNote);

        if (!isOk(data.getApprovers())) {
            creditNote.setEntityStatus(referenceManager.findReference(Constants.INVOICE_STATUS, data.getStatusCode()));
        }
        if (isOk(data.getApprovers())) {
            saveInvoiceApprovers(creditNote, data.getApprovers(), data.getStatusCode(), APPROVE);
            invoiceManager.update(creditNote);
            if (RECEIVABLE.equals(data.getType())) {
                EdsBusinessEvent workflowApprovingEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), creditNote, userManager.getUser());
                workflowApprovingEvent.setEntityType(RelationItem.TYPE_CREDIT_NOTE);
            } else if (PAYABLE.equals(data.getType())) {
                EdsBusinessEvent workflowApprovingEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), creditNote, userManager.getUser());
                workflowApprovingEvent.setEntityType(RelationItem.TYPE_DEBIT_NOTE);
            }
        }
        String statusCode = creditNote.getStatus() != null ? creditNote.getStatus().getCode() : data.getStatusCode();
        if (APPROVE.equals(statusCode) || OPEN.equals(statusCode)) {
            Integer transactionId = accountingServiceLocal.createTransactionsForCreditNote(creditNote, data.getCreditedInvoiceID());

            for (EdsInvoiceItem invoiceItem : creditNote.getInvoiceItems()) {
                if (invoiceItem.getItem() != null && invoiceItem.getItem().getInventoryTrackingEnabled()) {
                    if (RECEIVABLE.equals(data.getType())) {
                        itemSerialService.assignForCreditNote(invoiceItem, transactionId);
                    } else if (PAYABLE.equals(data.getType())) {
                        itemSerialService.assignForDebitNote(invoiceItem, transactionId);
                    }
                }
                if (invoiceItem.getItem() != null && invoiceItem.getItem().getTrackBatchesEnabled()) {
                    if (RECEIVABLE.equals(data.getType())) {
                        itemBatchService.createBatchCreditNote(creditNote.getObjectID(), invoiceItem);
                    } else if (PAYABLE.equals(data.getType())) {
                        itemBatchService.createBatchDebitNote(creditNote.getObjectID(), invoiceItem);
                    }
                }
            }

            if (creditNote.getCreditNoteInvoice() != null) {//data.getCreditedInvoiceAmount()
                if (creditNote.getTotalInInvoiceCurrency().compareTo(creditNote.getCreditNoteInvoice().getTotalInInvoiceCurrency()) < 0) {
                    data.setCreditedInvoiceAmount(creditNote.getTotalInInvoiceCurrency());
                } else {
                    data.setCreditedInvoiceAmount(creditNote.getCreditNoteInvoice().getTotalInInvoiceCurrency());
                }

                if (creditNote.getRefunds() != null && !creditNote.getRefunds().isEmpty()) {
                    for (EdsInvoicePayment payment : creditNote.getRefunds()) {
                        deletePayment(payment.getObjectID());
                    }
                }
                saveCreditedInvoicePayment(data, creditNote);
            } else {
                this.addInvoiceToSolr(creditNote);
            }
        } else {
            addInvoiceToSolr(creditNote);
        }

        return creditNote.getObjectID();
    }

    private void saveCreditedInvoicePayment(NewInvoice data, EdsInvoice creditNote) {
        EdsUser user = invoiceManager.getUser();

        EdsInvoicePayment refund = new EdsInvoicePayment();
        refund.setAmount(data.getCreditedInvoiceAmount());
        refund.setPaymentDate(creditNote.getInvoiceDate());
        if (RECEIVABLE.equals(creditNote.getType())) {
            refund.setAccount(accountingManager.getAccountByKey(EdsAccount.ACCOUNTS_RECEIVABLE));
        } else {
            refund.setAccount(accountingManager.getAccountByKey(EdsAccount.ACCOUNTS_PAYABLE));
        }
        EdsInvoice invoice = invoiceManager.get(data.getCreditedInvoiceID() == null ? data.getID() : data.getCreditedInvoiceID());
        refund.setCreditNote(creditNote);
        refund.setInvoice(invoice);

        refund.setReference(invoice.getNumber());
        refund.setExchangeRate(creditNote.getExchangeRate());
        refund.setCurrencyID(creditNote.getCurrency().getObjectID());
        refund.setUser(user);
        invoice.getPayments().add(refund);
        creditNote.getRefunds().add(refund);

        checkAndUpdateInvoiceCreditNoteStatuses(creditNote, true);
        checkAndUpdateInvoiceCreditNoteStatuses(invoice, false);

        accountingServiceLocal.createTransactionForCreditedInvoice(refund);

        if (RECEIVABLE.equals(creditNote.getType())) {
            baseEventPostProcessor.registerEvent(InvoicePaymentEventListenerImpl.TYPE, InvoicePaymentEventListenerImpl.EVENT_SALES_INVOICE_PAYMENT_RECEIVE, refund, user);
        } else {
            baseEventPostProcessor.registerEvent(InvoicePaymentEventListenerImpl.TYPE, InvoicePaymentEventListenerImpl.EVENT_PURCHASE_INVOICE_PAYMENT_PAY, refund, user);
        }
    }

    public SaveResult updateCreditNote(NewInvoice data) {
        EdsInvoice creditNote = invoiceManager.get(data.getID());

        SaveResult saveResult = new SaveResult();

        if (RECEIVABLE.equals(data.getType())) {
            List<EdsBaseSaleInvoice> existingCredNotes = invoiceManager.getSaleInvoiceByNumber(data.getInvoiceNumber(), creditNote.getCreationDate());
            if (existingCredNotes != null && existingCredNotes.size() > 0) {
                for (EdsBaseSaleInvoice cn : existingCredNotes) {
                    if (!cn.getObjectID().equals(creditNote.getObjectID())) {
                        saveResult.setInvoiceExist(true);
                        return saveResult;
                    }
                }
            }
        } else if (PAYABLE.equals(data.getType()) && data.getInvoiceNumber() != null && !"".equals(data.getInvoiceNumber().trim())) {
            List<EdsPurchaseInvoice> existingCredNotes = invoiceManager.getPurchaseInvoiceByNumber(data.getInvoiceNumber(), data.getClientID(), null);
            if (existingCredNotes != null && existingCredNotes.size() > 0) {
                for (EdsPurchaseInvoice cn : existingCredNotes) {
                    if (!cn.getObjectID().equals(creditNote.getObjectID())) {
                        saveResult.setInvoiceExist(true);
                        return saveResult;
                    }
                }
            }
        }

        List<EdsInvoicePayment> refunds = invoicePaymentManager.getPayments(creditNote);
        if (refunds != null && refunds.size() > 0) {
            saveResult.setPaymentExist(true);
            return saveResult;
        }

        createRevisionHistory(creditNote);
        if (creditNote instanceof EdsSaleInvoice) {
            ((EdsSaleInvoice) creditNote).setClient(clientManager.get(data.getClientID()));
            if (data.getFourDigitNumber() != null) {
                ((EdsSaleInvoice) creditNote).setFourDigitNumber(Integer.parseInt(data.getFourDigitNumber()));
                creditNote.setUpdatedDate(new Date());

            }
            if (data.getShippingMethodID() != null) {
                ((EdsSaleInvoice) creditNote).setShippingMethod(shippingMethodManager.get(data.getShippingMethodID()));
            }
            ((EdsSaleInvoice) creditNote).setInTarget(false);
            backfillFaiReportedDate(creditNote);
        } else {
            ((EdsPurchaseInvoice) creditNote).setSupplier(crmAccountManager.get(data.getClientID()));
            creditNote.setUpdatedDate(new Date());
            creditNote.setReverseChargeApplicable(data.isReversechargeApplicable());
        }
        super.initInvoiceData(creditNote, data);
        creditNote.setTaxCalculationType(data.getTaxCalculationType());
        creditNote.setRelatedProject(data.getRelatedProjectID() != null ? projectManager.get(data.getRelatedProjectID()) : null);

        creditNote.setNoteReason(data.getNoteReason());
        creditNote.setNotePaymentCode(data.getPaymentTypeCode());
        initCreditNoteTaxTotals(creditNote, data.getTotalTaxItems());

        creditNote.setCustomFields(createInvoiceCustomFields(data.getCustomFieldItems()));

        if (creditNote instanceof EdsBaseSaleInvoice inv) {
            inv.setBankAccount((data.getBankAccount() != null && data.getBankAccount().getId() != null) ? bankAccountManager.get(data.getBankAccount().getId()) : null);
        }

        if (data.getAttachments() != null && data.getAttachments().length > 0) {
            //attachmentUtilsManager.saveAttachments(RECEIVABLE.equals(data.getType()) ? F_RECEIVABLE_CREDIT_NOTE : F_PAYABLE_CREDIT_NOTE, creditNote.getObjectID(), creditNote.getObjectID(), data.getAttachments());
            attachmentUtilsManager.saveAttachments(F_SALE_INV, creditNote.getObjectID(), creditNote.getObjectID(), data.getAttachments());
        }

        createInvoiceNoteAndHistory(data, creditNote);

        Integer objectID = initCreditNoteItemsForSave(data, creditNote, true);

        if (!RECEIVABLE.equals(data.getType())) {
            try {
                purchaseInvoiceSolrComponent.index((EdsPurchaseInvoice) creditNote);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        saveResult.setId(objectID);
        return saveResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewInvoice getCreditNoteSummaryData(Integer objectID) {
        NewInvoice creditNote = getCreditNote(objectID);
        creditNote.setLayoutHTML(PathFinder.getLayoutHTML(CREDIT_NOTE));
        creditNote.setRoundingModeDisabled(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ROUNDING_MODE_DISABLED));
        creditNote.setCustomItemColumns(itemTableSettingsServiceLocal.getColumnConfigs(RECEIVABLE.equals(creditNote.getType()) ? ItemTableEnum.CREDIT_NOTE_ITEM : ItemTableEnum.DEBIT_NOTE_ITEM, false, true));

        if (RECEIVABLE.equals(creditNote.getType())) {
            creditNote.setPdfTemplateList(getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.RECEIVABLE_CREDIT_NOTE.name()));
        } else {
            creditNote.setPdfTemplateList(getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PAYABLE_CREDIT_NOTE.name()));
        }

        creditNote.setRevisionHistoryEnabled(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.REVISION_HISTORY_ENABLED));
        if (creditNote.isRevisionHistoryEnabled()) {
            creditNote.setRevisionHistoryItems(invoiceManager.getRevisionHistory(objectID, RECEIVABLE.equals(creditNote.getType()) ? SALE_INVOICE : PURCHASE_INVOICE));
        }
        if (RECEIVABLE.equals(creditNote.getType())) {
            creditNote.getTypeItem().setSupplierCustomerBalance(crmAccountManager.getClientBalance(creditNote.getClientID()).doubleValue());
        } else {
            EdsCrmAccount clientBase = crmAccountManager.get(creditNote.getClientID());
//            if (!clientBase.getBalanceCalculated()) {
            creditNote.getTypeItem().setSupplierCustomerBalance(crmAccountManager.getSupplierBalance(clientBase.getObjectID()).doubleValue());
//            } else {
//                creditNote.getTypeItem().setSupplierCustomerBalance(clientBase.getSupplierBalance().doubleValue());
//            }
            creditNote.getTypeItem().setReverseChargeApplicable(clientBase.isReverseChargeApplicable());
        }


        return getDataForSummaryView(creditNote);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewInvoice getCreditNote(Integer objectID) {
        EdsInvoice creditNote = invoiceManager.get(objectID);
        ArrayList<CompanyCustomFieldItem> itemCustomFields = null;

        if (creditNote instanceof EdsSaleInvoice) {
            itemCustomFields = commonService.getCompanyAllCustomFields(ViewName.SaleInvoiceItem);
        } else if (creditNote instanceof EdsPurchaseInvoice) {
            itemCustomFields = commonService.getCompanyAllCustomFields(ViewName.PurchaseInvoiceItem);
        }
        creditNote.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));

        NewInvoice creditNoteTransfer = EdsInvoice.getInvoiceData(creditNote);
        creditNoteTransfer.setExchageRate(creditNote.getExchangeRate());
        creditNoteTransfer.setTaxCalculationType(creditNote.getTaxCalculationType());
        creditNoteTransfer.setBaseCurrencyName(getBaseCurrency().getName());
        if (creditNote.getPriceLevelID() != null) {
            EdsPriceLevel priceLevel = priceLevelManager.get(creditNote.getPriceLevelID());
            creditNoteTransfer.setPriceLevel(new SelectItem(priceLevel.getObjectID(), priceLevel.getName()));
        }
        if (creditNote.getPdfTemplate() != null) {
            creditNoteTransfer.setPdfTemplateID(creditNote.getPdfTemplate().getObjectID());
        }

        if (creditNote instanceof EdsPurchaseInvoice) {
            creditNoteTransfer.setReversechargeApplicable(creditNote.isReverseChargeApplicable());
        }
        if (creditNote instanceof EdsSaleInvoice) {
            creditNoteTransfer.setNumberData(parseNumberData((EdsSaleInvoice) creditNote));
            EdsShippingMethod shippingMethod = ((EdsSaleInvoice) creditNote).getShippingMethod();
            if (shippingMethod != null && !shippingMethod.getDeleted()) {
                creditNoteTransfer.setShippingMethodID(shippingMethod.getObjectID());
                creditNoteTransfer.setShippingMethodName(shippingMethod.getName());

                ShippingMethod shm = shippingMethod.getRPC();
                shm.setCurrencyId(creditNote.getCurrency().getObjectID());
                shm.setExchangeRate(creditNote.getExchangeRate());

                if (((EdsSaleInvoice) creditNote).getShippingAmount() != null && ((EdsSaleInvoice) creditNote).getShippingAmount().compareTo(BigDecimal.ZERO) > 0) {
                    shm.setPrice(((EdsSaleInvoice) creditNote).getShippingAmount());
                }
                creditNoteTransfer.setShippingPrice(shm.getPrice());
                creditNoteTransfer.setShippingMethod(shm);
            }
            creditNoteTransfer.setInTarget(((EdsSaleInvoice) creditNote).isInTarget());
        }
        if (creditNote instanceof EdsBaseSaleInvoice inv) {
            creditNoteTransfer.setBankAccount(inv.getBankAccount() != null ? inv.getBankAccount().getAsSelectItem() : null);
        }
        creditNoteTransfer.setHistoryList(invoiceCircularResolver.getInvoiceNotes(creditNote.getObjectID()));

        if (creditNote != null) {
            EdsInvoiceTransaction invoiceTransaction = transactionManager.getTransactionByInvoice(creditNote);
            if (invoiceTransaction != null) {
                creditNoteTransfer.setJournalId(invoiceTransaction.getJournalId());
            }
        }
        creditNoteTransfer.setNoteReason(creditNote.getNoteReason());
        creditNoteTransfer.setPaymentTypeCode(creditNote.getNotePaymentCode());
        EdsInvoiceCustomFields customFields = creditNote.getCustomFields();
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(RECEIVABLE.equals(creditNote.getType()) ? ViewName.SaleInvoice : ViewName.PurchaseInvoice);
        creditNoteTransfer.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(customFields, customFieldsItems));
        if (creditNote.getCurrentApprover() != null && creditNote.getCurrentApprover().getExactEmployee() != null) {
            creditNoteTransfer.setCurrentApproverSelectItem(creditNote.getCurrentApprover().getExactEmployee().getAsSelectItem());
        }
        return creditNoteTransfer;
    }

    public void approveCreditNote(Integer creditNoteID) {
        EdsInvoice creditNote = invoiceManager.get(creditNoteID);
        accountingServiceLocal.createTransactionsForCreditNote(creditNote, null);
        creditNote.setStatus(super.getInvoiceStatus(APPROVE));

        addInvoiceToSolr(creditNote);
    }

    public void saveCreditNoteRefund(PaymentData data) {
        EdsUser user = invoiceManager.getUser();

        EdsInvoicePayment refund = new EdsInvoicePayment();
        EdsInvoicePayment oldRefund = new EdsInvoicePayment();
        EdsInvoice creditNote;
        if (data.getObjectID() != null) {
            oldRefund = invoicePaymentManager.get(data.getObjectID());
            creditNote = oldRefund.getCreditNote();
            deletePayment(data.getObjectID());
        } else {
            creditNote = invoiceManager.get(data.getInvoiceID());
        }
        if (data.getCrmAccount() != null) {
            refund.setCrmAccount(crmAccountManager.get(data.getCrmAccount().getId()));
        }
        refund.setAmount(data.getPaymentAmount());
        refund.setPaymentDate(data.getDate().getNonConvertedDate());
        refund.setAccount(accountingManager.get(data.getPaymentAccount().getId()));
        refund.setReference(data.getReferenceNumber());
        refund.setCreditNote(creditNote);
        refund.setUser(user);
        refund.setExchangeRate(data.getExchangeRate());
        refund.setCurrencyID(data.getCurrency() != null ? data.getCurrency().getId() : null);
        refund.setType(oldRefund.getCreditNote() != null ? creditNote.getType() : data.getType());
        List<EdsInvoicePayment> refunds = creditNote.getRefunds();
        refunds.add(refund);

        checkAndUpdateInvoiceCreditNoteStatuses(creditNote, true);

        addAttachmentsToPaymentOrRefund(data, refund.getObjectID());

        accountingServiceLocal.createTransactionForRefund(refund);
        if (creditNote.getType().equals(RECEIVABLE)) {
            baseEventPostProcessor.registerEvent(InvoicePaymentEventListenerImpl.TYPE, InvoicePaymentEventListenerImpl.EVENT_SI_CREDIT_NOTE_REFUND, refund, user);
        } else {
            baseEventPostProcessor.registerEvent(InvoicePaymentEventListenerImpl.TYPE, InvoicePaymentEventListenerImpl.EVENT_PI_CREDIT_NOTE_REFUND, refund, user);
        }

    }

    private void checkAndUpdateInvoiceCreditNoteStatuses(EdsInvoice invoiceOrNote, boolean isCreditNote) {
        EdsUser user = invoiceManager.getUser();

        Integer calculationScale = financialSettingsManager.getFinancialSettings().getAccountingCalculationScale();
        List<EdsInvoicePayment> refundsOrPayments = (isCreditNote ? invoiceOrNote.getRefunds() : invoiceOrNote.getPayments());

//        List<EdsInvoicePayment> refundsOrPayments;
//        if (isCreditNote) {
//            refundsOrPayments = invoicePaymentManager.getRefunds(invoiceOrNote);
//        } else {
//            refundsOrPayments = invoicePaymentManager.getPayments(invoiceOrNote);
//        }
//        BigDecimal fullPayments = ZERO;
//        for (EdsInvoicePayment item : refundsOrPayments) {
//            if (!item.isReversed()) {
//                fullPayments = fullPayments.add(item.getAmountInInvoiceCurrency() != null ? item.getAmountInInvoiceCurrency() : item.getAmount());
//            }
//        }

        BigDecimal totalInInvoiceCurrency = invoiceOrNote.getTotalInInvoiceCurrency() != null ? invoiceOrNote.getTotalInInvoiceCurrency()
                : invoiceOrNote.getTotal().multiply(invoiceOrNote.getExchangeRate());

        if (totalInInvoiceCurrency.setScale(calculationScale, RoundingMode.HALF_UP).compareTo(invoiceOrNote.getFullPayments().setScale(calculationScale, RoundingMode.HALF_UP)) <= 0) {
            invoiceOrNote.setPaidDate(getCompanyDate(user));
            invoiceOrNote.setStatus(referenceManager.findReference(INVOICE_STATUS, PAID));
        } else if (totalInInvoiceCurrency.compareTo(invoiceOrNote.getFullPayments()) >= 0 && PAID.equals(invoiceOrNote.getStatus().getCode())) {
            invoiceOrNote.setStatus(referenceManager.findReference(INVOICE_STATUS, APPROVE));
        }
        if (RECEIVABLE.equals(invoiceOrNote.getType())) {
            EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(invoiceOrNote.getObjectID());
            if (saleInvoice != null) {
                try {
                    if (isCreditNote) {
                        saleInvoice.setRefunds(refundsOrPayments);
                    } else {
                        saleInvoice.setPayments(refundsOrPayments);
                    }
                    saleInvoiceSolrComponent.index(saleInvoice);
                } catch (IOException | SolrServerException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else if (PAYABLE.equals(invoiceOrNote.getType())) {
            EdsPurchaseInvoice purchaseInvoice = invoiceManager.getPurchaseInvoice(invoiceOrNote.getObjectID());
            if (purchaseInvoice != null) {
                try {
                    if (isCreditNote) {
                        purchaseInvoice.setRefunds(refundsOrPayments);
                    } else {
                        purchaseInvoice.setPayments(refundsOrPayments);
                    }
                    purchaseInvoiceSolrComponent.index(purchaseInvoice);
                } catch (IOException | SolrServerException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Set<EdsShippingData> shippingDataList = invoiceOrNote.getConvertedShippingData();
        if (!shippingDataList.isEmpty()) {
            try {
                shippingDataSolrComponent.indexes(new ArrayList<>(shippingDataList));
                EventHandler.fireEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, "Related Invoice is approved by manager");
                EventHandler.fireEvent(WfmUiEventType.ON_SALES_INVOICE_APPROVAL, "Invoice status is updated");
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AllocateCreditData getAllocateCreditData(Integer creditNoteID) {
        EdsInvoice creditNote = invoiceManager.get(creditNoteID);
        List<EdsInvoice> invoicesForAllocating;
        if (creditNote instanceof EdsSaleInvoice) {
            invoicesForAllocating = invoiceManager.getInvoicesForAllocatingCredits(creditNote, true);
        } else {
            invoicesForAllocating = invoiceManager.getInvoicesForAllocatingCredits(creditNote, false);
        }

        AllocateCreditData data = new AllocateCreditData();
        data.setCreditNoteID(creditNoteID);
        List<AllocateCreditItem> items = new LinkedList<>();
        for (EdsInvoice invoice : invoicesForAllocating) {
            if (OVER_DUE.equals(invoice.getStatus().getCode())) {
                EdsInvoiceTransaction invTrans = accountingServiceLocal.getInvoiceTransaction(invoice);
                if (invTrans == null || invTrans.getTransactionItems() == null || invTrans.getTransactionItems().size() == 0) {
                    continue;
                }
            }
            BigDecimal fullPayments = invoice.getFullPayments(), dueAmount;
            BigDecimal totalInInvoiceCurrency = invoice.getTotalInInvoiceCurrency() != null ? invoice.getTotalInInvoiceCurrency() : invoice.getTotal().multiply(invoice.getExchangeRate());
            dueAmount = totalInInvoiceCurrency.subtract(fullPayments);
            if (dueAmount.compareTo(ZERO) > 0) {
                AllocateCreditItem item = new AllocateCreditItem();
                item.setInvoiceID(invoice.getObjectID());
                item.setInvoiceNumber(invoice.getNumber());
                item.setInvoiceDate(invoice.getInvoiceDate());
                item.setPaidAmount(fullPayments);
                item.setDueAmount(dueAmount);
                items.add(item);
            }
        }
        data.setInvoices(items.toArray(new AllocateCreditItem[]{}));
        return data;
    }

    public void allocateCreditsToInvoices(AllocateCreditData data) {
        EdsUser user = invoiceManager.getUser();
        EdsInvoice creditNote = invoiceManager.get(data.getCreditNoteID());
        EdsAccount account = accountingManager.getAccountByKey(RECEIVABLE.equals(creditNote.getType()) ? EdsAccount.ACCOUNTS_RECEIVABLE : EdsAccount.ACCOUNTS_PAYABLE);

        AllocateCreditItem[] invoices = data.getInvoices();
        for (AllocateCreditItem i : invoices) {
            EdsInvoice invoice = invoiceManager.get(i.getInvoiceID());

            EdsInvoicePayment refund = new EdsInvoicePayment();
            refund.setAmount(i.getCreditedAmount());
            refund.setPaymentDate(i.getInvoiceDate());
            refund.setAccount(account);
            refund.setReference(invoice.getNumber());
            refund.setExchangeRate(invoice.getExchangeRate());
            refund.setUser(user);
            refund.setCreditNote(creditNote);
            refund.setInvoice(invoice);
            invoicePaymentManager.create(refund);

            checkAndUpdateInvoiceCreditNoteStatuses(creditNote, true);
            checkAndUpdateInvoiceCreditNoteStatuses(invoice, false);
            accountingServiceLocal.createTransactionForCreditedInvoice(refund);
            if (invoice instanceof EdsSaleInvoice) {
                invoiceManager.update(invoice);
                baseEventPostProcessor.registerEvent(InvoicePaymentEventListenerImpl.TYPE, InvoicePaymentEventListenerImpl.EVENT_SALES_INVOICE_PAYMENT_RECEIVE, refund, user);
            }
            if (invoice instanceof EdsPurchaseInvoice) {
                baseEventPostProcessor.registerEvent(InvoicePaymentEventListenerImpl.TYPE, InvoicePaymentEventListenerImpl.EVENT_PURCHASE_INVOICE_PAYMENT_PAY, refund, user);
            }
        }
    }

    @Transactional
    public void saleInvoiceToSolrIndex(SolrReindexRpc solrReindex) {
        ServerSecurityContext.getInstance().setCompanyId(solrReindex.getCompanyId());
//        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));
        profileService.clearFromDbDeletedCustomFieldsByFormId(LayoutRPC.SALEINVOICE_FORM, null, false);

        solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.INVOICE);
        solrDbConsistencyManager.flushAndClear();
        try {
            if (solrReindex.isAllReindex()) {
                solrManager.removeCompanySaleInvoice(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deleteTaskIds = invoiceManager.getCompanyDeletedInvoicesForSolr(solrReindex);
                this.solrManager.removeCompanyTasksbyIds(deleteTaskIds.toArray(new Integer[]{}));
            }
        } catch (Exception e) {
            log.error("Error Sale Invoice Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int start = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsSaleInvoice> invoiceList = invoiceManager.getSaleInvoiceListForSolr(solrReindex, start, limit);
        while (!invoiceList.isEmpty()) {
            try {
                saleInvoiceSolrComponent.indexConcurrently(invoiceList);
            } catch (IOException | SolrServerException | InterruptedException e) {
                log.error("Error Sale Invoice Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            invoiceManager.flushAndClear();
            start++;
            invoiceList = invoiceManager.getSaleInvoiceListForSolr(solrReindex, (start * limit), limit);
        }
        solrDbConsistencyManager.flushAndClear();
    }

    @Transactional
    public Integer indexCompanySaleInvoice(SolrReindexRpc solrReindex, Integer start, int limit) {
        List<EdsSaleInvoice> invoiceList = invoiceManager.getSaleInvoiceListForSolr(solrReindex, start, limit);
        if (!invoiceList.isEmpty()) {
            try {
                saleInvoiceSolrComponent.indexes(invoiceList);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
            return invoiceList.get(invoiceList.size() - 1).getObjectID();
        } else {
            return -1;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateCompanySalesInvoicesStatuses(Integer companyId) {
        // Set the current tenant context
        ServerSecurityContext.getInstance().setCompanyId(companyId);

        // Load required references and company info
        EdsCompany company = companyManager.get(companyId);
        EdsReference statusPaid = referenceManager.findReference(INVOICE_STATUS, PAID);
        EdsReference statusDue = referenceManager.findReference(INVOICE_STATUS, OVER_DUE);

        // Define batch parameters and Redis cursor key
        final int BATCH_SIZE = 200;
        final Date now = new Date();
        final String redisKey = companyId + "_si_status_updater";

        // Retrieve the last processed invoice ID (cursor) from Redis
        int lastId = getOrInitLastId(redisKey);

        // Determine the company’s timezone for accurate date‐based filtering
        TimeZone tz = Optional.ofNullable(company.getCountryZone())
                .map(zone -> TimeZone.getTimeZone(zone.getZone().getZoneID()))
                .orElse(TimeZone.getDefault());

        log.info("Starting sales‐invoice status batch for company={} (cursor={})", companyId, lastId);

        try {
            // Fetch the next batch of invoices to process
            Date companyDate = getCompanyCurrentDate(tz);
            List<EdsSaleInvoice> invoiceBatch = invoiceManager.getCompanySaleInvoiceByTimeZoneAndCorsor(companyDate, lastId, BATCH_SIZE);

            // If no invoices remain, reset cursor to 0 and exit
            if (invoiceBatch.isEmpty()) {
                log.info("No more sales invoices for company={} at cursor={}. Resetting cursor to 0.",
                        companyId, lastId);
                RedisClient.setKey(redisKey, 0, Integer.class, TTL_10_DAYS);
                return;
            }

            // Process each invoice and collect those whose status changed
            List<EdsSaleInvoice> changed = new ArrayList<>(invoiceBatch.size());
            for (EdsSaleInvoice inv : invoiceBatch) {
                if (updateIfNeededSI(inv, statusDue, statusPaid, now)) {
                    changed.add(inv);
                }
            }

            // Bulk‐index any invoices with updated statuses
            if (!changed.isEmpty()) {
                saleInvoiceSolrComponent.indexes(changed);
                log.debug("Indexed {}  updated sales invoices for company={}", changed.size(), companyId);
            } else {
                log.debug("No sales‐invoice status changes for company={} in this batch", companyId);
            }

            // Advance or reset the cursor based on batch size
            int lastProcessedId = invoiceBatch.get(invoiceBatch.size() - 1).getObjectID();
            if (invoiceBatch.size() < BATCH_SIZE) {
                log.info("Processed final batch for company={}, resetting cursor to 0 (was {}).",
                        companyId, lastId);
                RedisClient.setKey(redisKey, 0, Integer.class, TTL_10_DAYS);
            } else {
                log.info("Processed {} invoices for company={}, advancing cursor from {} to {}.",
                        invoiceBatch.size(), companyId, lastId, lastProcessedId);
                RedisClient.setKey(redisKey, lastProcessedId, Integer.class, TTL_10_DAYS);
            }

        } catch (Exception ex) {
            // Log and swallow exceptions to avoid rolling back the outer context
            log.error("Failed to update sales‐invoice statuses for company={} at cursor={}", companyId, lastId, ex);
        }
    }

    private int getOrInitLastId(String key) {
        Integer last = RedisClient.getKey(key, Integer.class);
        if (last == null) {
            RedisClient.setKey(key, 0, Integer.class, TTL_10_DAYS);
            return 0;
        }
        return last;
    }

    private boolean updateIfNeededSI(EdsSaleInvoice inv, EdsReference statusOverDue, EdsReference statusPaid, Date now) {
        String code = inv.getStatus().getCode();
        BigDecimal dueAmt = inv.getDueAmount();

        if (!OVER_DUE.equals(code) && dueAmt.compareTo(BigDecimal.ZERO) > 0) {
            inv.setStatus(statusOverDue);
            inv.setUpdatedDate(now);
            return true;
        }
        if (!OPEN.equals(code) && dueAmt.compareTo(BigDecimal.ZERO) <= 0) {
            inv.setStatus(statusPaid);
            inv.setUpdatedDate(now);
            return true;
        }
        return false;
    }

    private boolean updateIfNeededPI(EdsPurchaseInvoice piv, EdsReference statusOverDue, EdsReference statusPaid, Date now) {
        String code = piv.getStatus().getCode();
        BigDecimal dueAmt = piv.getDueAmount();

        if (!OVER_DUE.equals(code) && dueAmt.compareTo(BigDecimal.ZERO) > 0) {
            piv.setStatus(statusOverDue);
            piv.setUpdatedDate(now);
            return true;
        }
        if (!OPEN.equals(code) && dueAmt.compareTo(BigDecimal.ZERO) <= 0) {
            piv.setStatus(statusPaid);
            piv.setUpdatedDate(now);
            return true;
        }
        return false;
    }

    public void resetCompanySalesInvoicesStatuses(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        EdsReference statusApprove = referenceManager.findReference(INVOICE_STATUS, APPROVE);
        TimeZone timeZone = null;
        if (company.getCountryZone() != null && company.getCountryZone().getZone() != null) {
            timeZone = TimeZone.getTimeZone(company.getCountryZone().getZone().getZoneID());
        }
        try {
            Date companyTime = getCompanyCurrentDate(timeZone);
            List<EdsSaleInvoice> saleInvoiceList = invoiceManager.getCompanyOverdueSaleInvoiceByTimeZone(companyTime, -1);
            log.info("Reset Company Sales Invoices Status, cId: {}", company.getObjectID().toString());
            for (EdsSaleInvoice saleInvoice : saleInvoiceList) {
                saleInvoice.setStatus(statusApprove);
                saleInvoice.setUpdatedDate(new Date());
                saleInvoiceSolrComponent.index(saleInvoice);
            }
        } catch (Exception ex) {
            log.error("Reset Invoice status failed for company " + companyID + "!!!", ex);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateCompanyPurchaseInvoicesStatuses(Integer companyId) {
        // Set the current tenant context
        ServerSecurityContext.getInstance().setCompanyId(companyId);

        // Load company info and invoice‐status references
        EdsCompany company = companyManager.get(companyId);
        EdsReference statusOverDue = referenceManager.findReference(INVOICE_STATUS, OVER_DUE);
        EdsReference statusPaid = referenceManager.findReference(INVOICE_STATUS, PAID);

        // Define batch parameters and Redis cursor key
        final int BATCH_SIZE = 200;
        final Date now = new Date();
        final String redisKey = companyId + "_pi_status_updater";

        // Retrieve the last processed invoice ID (cursor) from Redis
        int lastId = getOrInitLastId(redisKey);

        // Determine the company’s timezone for date‐based filtering
        TimeZone tz = Optional.ofNullable(company.getCountryZone())
                .map(zone -> TimeZone.getTimeZone(zone.getZone().getZoneID()))
                .orElse(TimeZone.getDefault());

        log.info("Starting purchase‐invoice status batch for company={} (cursor={})", companyId, lastId);

        try {
            // Fetch the next batch of purchase invoices to process
            Date companyDate = getCompanyCurrentDate(tz);
            List<EdsPurchaseInvoice> purchaseBatch =
                    invoiceManager.getCompanyPurchaseInvoiceByTimeZone(companyDate, lastId, BATCH_SIZE);

            // If no invoices remain, reset cursor to 0 and exit
            if (purchaseBatch.isEmpty()) {
                log.info("No purchase invoices for company={} at cursor={}. Resetting cursor to 0.", companyId, lastId);
                RedisClient.setKey(redisKey, 0, Integer.class, TTL_10_DAYS);
                return;
            }

            // Process each invoice and collect those whose status changed
            List<EdsPurchaseInvoice> changed = new ArrayList<>(purchaseBatch.size());
            for (EdsPurchaseInvoice piv : purchaseBatch) {
                if (updateIfNeededPI(piv, statusOverDue, statusPaid, now)) {
                    changed.add(piv);
                }
            }

            // Bulk‐index any invoices with updated statuses
            if (!changed.isEmpty()) {
                purchaseInvoiceSolrComponent.indexes(changed);
                log.debug("Indexed {} updated purchase invoices for company={}", changed.size(), companyId);
            } else {
                log.debug("No purchase‐invoice status changes for company={} in this batch", companyId);
            }

            // Advance or reset the cursor based on batch size
            int lastProcessedId = purchaseBatch.get(purchaseBatch.size() - 1).getObjectID();
            if (purchaseBatch.size() < BATCH_SIZE) {
                log.info("Processed final purchase batch for company={}, resetting cursor to 0 (was {}).", companyId, lastId);
                RedisClient.setKey(redisKey, 0, Integer.class, TTL_10_DAYS);
            } else {
                log.info("Processed {} purchase invoices for company={}, advancing cursor from {} to {}.", purchaseBatch.size(), companyId, lastId, lastProcessedId);
                RedisClient.setKey(redisKey, lastProcessedId, Integer.class, TTL_10_DAYS);
            }

        } catch (Exception ex) {
            // Log and swallow exceptions to avoid rolling back the outer context
            log.error("Failed to update purchase‐invoice statuses for company={} at cursor={}", companyId, lastId, ex);
        }
    }

    public void resetCompanyPurchaseInvoicesStatuses(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        EdsReference statusApprove = referenceManager.findReference(INVOICE_STATUS, APPROVE);
        TimeZone timeZone = null;
        if (company.getCountryZone() != null && company.getCountryZone().getZone() != null) {
            timeZone = TimeZone.getTimeZone(company.getCountryZone().getZone().getZoneID());
        }
        try {
            Date companyTime = getCompanyCurrentDate(timeZone);
            List<EdsPurchaseInvoice> purchaseInvoiceList = invoiceManager.getCompanyOverduePurchaseInvoiceByTimeZone(companyTime, -1);
            log.info("Reset Company Purchase Invoices Status, cId: {}", company.getObjectID().toString());
            for (EdsPurchaseInvoice purchaseInvoice : purchaseInvoiceList) {
                purchaseInvoice.setStatus(statusApprove);
                purchaseInvoice.setUpdatedDate(new Date());
                purchaseInvoiceSolrComponent.index(purchaseInvoice);
            }
        } catch (Exception ex) {
            log.error("Reset Invoice status failed for company " + companyID + "!!!", ex);
        }
    }

    private Date getCompanyCurrentDate(TimeZone companyTimeZone) throws ParseException {
        Date currentServerTime = new Date();
        SimpleDateFormat companyTimeZoneDateFormat = new SimpleDateFormat();
        if (companyTimeZone != null) {
            companyTimeZoneDateFormat.setTimeZone(companyTimeZone);
        }
        String s = companyTimeZoneDateFormat.format(currentServerTime);
        SimpleDateFormat dateFormatForParse = new SimpleDateFormat();
        return dateFormatForParse.parse(s);
    }

    @Transactional
    public void addInvoiceToSolr(EdsInvoice invoice) {
        if (RECEIVABLE.equals(invoice.getType()) && (invoice instanceof EdsSaleInvoice)) {
            try {
                saleInvoiceSolrComponent.index((EdsSaleInvoice) invoice);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
            final Set<EdsItem> items = Sets.newHashSetWithExpectedSize(invoice.getInvoiceItems().size());

            for (EdsInvoiceItem invoiceItem : invoice.getInvoiceItems()) {
                if (invoiceItem.getItem() == null) {
                    continue;
                }
                items.add(invoiceItem.getItem());
            }
            if (!items.isEmpty()) {
                try {
                    List<EdsItem> items1 = new ArrayList<>(items);
                    productsServicesSolrComponent.indexes(items1);
                } catch (Exception ignored) {
                }
            }

        } else if (PAYABLE.equals(invoice.getType()) && (invoice instanceof EdsPurchaseInvoice)) {
            try {
                purchaseInvoiceSolrComponent.index((EdsPurchaseInvoice) invoice);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteSaleInvoiceInSolr(Integer saleInvoiceId) {
        try {
            solrManager.removeSaleInvoice(saleInvoiceId, invoiceManager.get(saleInvoiceId).getCompany().getObjectID());
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }
    }

    public void deletePurchaseInvoiceInSolr(Integer purchaseInvoiceId) {
        try {
            solrManager.removePurchaseInvoice(purchaseInvoiceId, invoiceManager.get(purchaseInvoiceId).getCompany().getObjectID());
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewInvoice getAllInvoiceData(Params fp) {
        NewInvoice invObject = null;
        boolean isAlmadarSerials = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ALMADAR_PRODUCT_SERIAL_ENABLED);
        EdsCompany company = invoiceManager.getUser().getCompany();
        Set<GenericSettingsEnum> genericSettings = genericSettingsManager.getEnabledGenericSettings();
        boolean lockClosedProjectItems = genericSettings.contains(GenericSettingsEnum.LOCK_COMPLETED_PROJECT_ITEMS);
        EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);

        if (fp.getObjectID() != null) {
            invObject = getInvoice(fp.getObjectID());

            //getting customer/supplier balance
            if (invObject.getClientID() != null) {
                if (RECEIVABLE.equals(fp.getType())) {
                    invObject.getTypeItem().setSupplierCustomerBalance(crmAccountManager.getClientBalance(invObject.getClientID()).doubleValue());

                    ListingFilterParameter beFp = new ListingFilterParameter();
                    beFp.setInvoiceClientId(invObject.getClientID());

                    ArrayList<BillableExpenseItem> unAssignedBillExp = getBillableExpensesByClient(beFp);

                    if (!unAssignedBillExp.isEmpty()) {

                        if (CollectionUtils.isEmpty(invObject.getExpenses())) {
                            invObject.setExpenses(unAssignedBillExp);
                        } else {
                            invObject.getExpenses().addAll(unAssignedBillExp);
                        }
                    }
                } else {
                    EdsCrmAccount clientBase = crmAccountManager.get(invObject.getClientID());
//                    if (!clientBase.getBalanceCalculated()) {
                    invObject.getTypeItem().setSupplierCustomerBalance(crmAccountManager.getSupplierBalance(clientBase.getObjectID()).doubleValue());
//                    } else {
//                        invObject.getTypeItem().setSupplierCustomerBalance(clientBase.getSupplierBalance().doubleValue());
//                    }
                }
            }

        } else {
            if (fp.getExternalFormID() != null && COPY_FROM_EXISTING_DATA.equals(fp.getExternalFormID())) {
                invObject = getInvoice(fp.getExternalObjectID());

                if (invObject.getCustomFieldItems() != null && invObject.getCustomFieldItems().size() > 0) {
                    for (CompanyCustomFieldItem ccfItem : invObject.getCustomFieldItems()) {
                        ccfItem.setObjectId(null);
                    }
                }
                //set null to the custom field id
                clearObjectIdFromItems(invObject);

                if (lockClosedProjectItems && invObject.getRelatedProject() != null) {
                    EdsProject project = projectManager.get(invObject.getRelatedProjectID());
                    if (EdsProject.CLOSED.equals(project.getStatus().getCode())) {
                        invObject.setRelatedProject(null);
                    }
                }
                invObject.setHasBillableExpense(false);
                invObject.setExpenses(null);
                invObject.setBillableExpenseAmount(null);
                invObject.setBillableExpenseTaxAmount(null);
//                invObject.setReference(null);
                invObject.setQuoteNumber(null);
                invObject.setPaidAmount(null);

                //load expenses
                {
                    ListingFilterParameter beFp = new ListingFilterParameter();
                    beFp.setInvoiceClientId(invObject.getClientID());
                    ArrayList<BillableExpenseItem> unAssignedBillExp = getBillableExpensesByClient(beFp);

                    if (!unAssignedBillExp.isEmpty()) {

                        if (CollectionUtils.isEmpty(invObject.getExpenses())) {
                            invObject.setExpenses(unAssignedBillExp);
                        } else {
                            invObject.getExpenses().addAll(unAssignedBillExp);
                        }
                    }
                }
            } else if (fp.getExternalFormID() != null && CONVERT_MULTI_QUOTE_TO_INVOICE.equals(fp.getExternalFormID())) {
                Stopwatch watch = Stopwatch.createStarted();
                invObject = getMultiQuoteData(fp.getMultiQuoteConvertItem(), fp.getType());

                if (invObject.getRelatedProject() != null) {
                    EdsProject project = projectManager.get(invObject.getRelatedProjectID());
                    if (lockClosedProjectItems && EdsProject.CLOSED.equals(project.getStatus().getCode())) {
                        invObject.setRelatedProject(null);
                    }
                }
                watch.elapsed(TimeUnit.MILLISECONDS);
                System.out.println("Order generation elapsed: " + watch);
            } else if (fp.getExternalFormID() != null && COPY_PO_TO_PI.equals(fp.getExternalFormID())) {
                invObject = invoiceCircularResolver.getQuote(fp.getExternalObjectID(), fp.getExternalFormID());
                invObject.setPoNumber(invObject.getInvoiceNumber());

                //set null to the custom field id
                clearObjectIdFromItems(invObject);

                if (lockClosedProjectItems && invObject.getRelatedProject() != null) {
                    EdsProject project = projectManager.get(invObject.getRelatedProjectID());
                    if (EdsProject.CLOSED.equals(project.getStatus().getCode())) {
                        invObject.setRelatedProject(null);
                    }
                }
            } else if (fp.getExternalFormID() != null && COPY_FROM_SI_TO_PI.equals(fp.getExternalFormID())) {
                invObject = getInvoice(fp.getExternalObjectID());
                invObject.setDiscountType(null);
                invObject.setDiscountAmount(null);
                if (lockClosedProjectItems && invObject.getRelatedProject() != null) {
                    final EdsProject project = this.projectManager.get(invObject.getRelatedProjectID());
                    if (EdsProject.CLOSED.equals(project.getStatus().getCode())) {
                        invObject.setRelatedProject(null);
                    }
                }
                InvoiceNumberData invoiceNumberData = invoiceCircularResolver.generatePurchaseInvoiceNumber(false);
                invObject.setNumberData(invoiceNumberData);
                invObject.setInvoiceNumber(invoiceNumberData.getInvoiceNumber());
                invObject.setInvoiceDate(new DateNonConvertable(userManager.getUser().getUserDate()));
                invObject.setDueDate(new DateNonConvertable(userManager.getUser().getUserDate()));
                invObject.setTypeItem(null);
                invObject.setItems(setCopyItems(invObject.getItems()));
            } else if (fp.getExternalFormID() != null && COPY_FROM_PI_TO_SI.equals(fp.getExternalFormID())) {
                invObject = getInvoice(fp.getExternalObjectID());
                InvoiceNumberData invoiceNumberData = getSaleInvoiceNumber();
                invObject.setNumberData(invoiceNumberData);
                invObject.setInvoiceNumber(invoiceNumberData.getInvoiceNumber());
                invObject.setItems(invObject.getItems());
                invObject.setTypeItem(null);
                invObject.setDueDateType(DUE_TYPE);
                EdsPurchaseInvoice purchaseInvoice = invoiceManager.getPurchaseInvoice(fp.getExternalObjectID());
                ArrayList<CompanyCustomFieldItem> purchaseCustomFields = commonService.getCompanyAllCustomFields(ViewName.PurchaseInvoiceItem);
                if (isOk(invObject.getCustomFieldItems())) { // purchase custom fields to sales custom fields
                    invObject.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(purchaseInvoice.getCustomFields(), purchaseCustomFields));
                    ArrayList<CompanyCustomFieldItem> saleInvoiceCustomFields = commonService.getCompanyCustomFields(ViewName.SaleInvoice);
                    copyCustomFieldData(saleInvoiceCustomFields, purchaseCustomFields);
                }
                if (isOk(purchaseCustomFields)) {// purchase custom fields to sales system fields
                    copyPICustomFieldsToSISystemFields(invObject, purchaseCustomFields);
                }

                /// custom fields to system fields
            } else if (fp.getExternalFormID() != null && (PROGRESS_INVOICING.equals(fp.getExternalFormID()) || CONVERT_TO_INVOICE.equals(fp.getExternalFormID()))) {
                //Info: Goes here when conver from SO and PO
                invObject = invoiceCircularResolver.getQuote(fp.getExternalObjectID(), fp.getExternalFormID());
                EdsQuote quote = quoteManager.get(fp.getExternalObjectID());

                if (invObject.getClientID() != null) {
                    EdsCrmAccount crmAccount = crmAccountManager.get(invObject.getClientID());

                    if (crmAccount != null && crmAccount.getBankAccount() != null) {
                        invObject.setBankAccount(new SelectItem(crmAccount.getBankAccount().getObjectID()));
                    }

                    ListingFilterParameter beFp = new ListingFilterParameter();
                    beFp.setInvoiceClientId(invObject.getClientID());

                    ArrayList<BillableExpenseItem> unAssignedBillExp = getBillableExpensesByClient(beFp);

                    if (!unAssignedBillExp.isEmpty()) {
                        if (CollectionUtils.isEmpty(invObject.getExpenses())) {
                            invObject.setExpenses(unAssignedBillExp);
                        } else {
                            invObject.getExpenses().addAll(unAssignedBillExp);
                        }
                    }
                }

                if (RECEIVABLE.equals(fp.getType())) {
                    invObject.setQuoteNumber(quote.getNumber());
                    invObject.setInvoiceDate(new DateNonConvertable(new Date()));
                    invObject.setDueDate(null);
                    invObject.setInvoiceTermsItem(((EdsSaleQuote) quote).getInvoiceTerms() != null ? ((EdsSaleQuote) quote).getInvoiceTerms().getAsRPC() : null);

                    if (invSettings != null) {
                        invObject.setInvoiceType(invSettings.getInvoiceType());
                    }
                    if (PROGRESS_INVOICING.equals(fp.getExternalFormID())) {
                        invObject.setProgressInvoicing(true);
                        if (fp.getProgressiveInvoiceType() != null) {
                            invObject.setProgressInvoicingType(fp.getProgressiveInvoiceType());
                        }
                        invObject.setDiscountType(null);
                    }
                    //For overall discount: patrially converting to invoice
                    BigDecimal totalOverallDiscount = BigDecimal.ZERO;

                    for (NewInvoiceItem item : invObject.getItems()) {
                        item.setQuoteItemId(item.getID());

                        if (lockClosedProjectItems && item.getProject() != null && item.getProject().getId() != null) {
                            EdsProject project = projectManager.get(item.getProject().getId());

                            if (EdsProject.CLOSED.equals(project.getStatus().getCode())) {
                                item.setProject(null);
                            }
                        }

                        //set product default warehouse to line item if it hasn't own selected warehouse
                        EdsItem edsItem = itemManager.get(item.getItemID());
                        if (item.getWarehouse() == null && edsItem != null && edsItem.getDefaultWarehouse() != null) {
                            item.setWarehouse(edsItem.getDefaultWarehouse().getAsSelectItem());
                        }

                        totalOverallDiscount = totalOverallDiscount.add(item.getDiscountAmount());

                        if (CONVERT_TO_INVOICE.equals(fp.getExternalFormID())) {
                            final List<EdsShippingData> shippingDataList = this.shippingDataManager.getByQuoteId(quote.getObjectID());
                            boolean havePickList = shippingDataList != null && shippingDataList.size() > 0;
                            if (item.getNonConvertedQty2(havePickList).compareTo(BigDecimal.ZERO) != 0) {
                                item.setDiscountAmount(item.getNonConvertedQty2(havePickList).multiply(item.getDiscountAmount() != null ? item.getDiscountAmount() : BigDecimal.ZERO).divide(item.getQuantity(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                            }
                            item.setQuantity(item.getNonConvertedQty2(havePickList));

                            if (item.getInventoryTrackingEnabled()) {
                                item.setSerials(itemSerialService.getSerials(item.getID(), ItemSerialEntityType.GOODS_DELIVERED));
                            }

                            if (item.getTrackBatchesEnabled()) {
                                if (RECEIVABLE.equals(fp.getType())) {
                                    item.setBatchItems(itemBatchService.getBatchItemsOfGrnOrGdn(item.getQuoteItemId(),
                                            item.getItemID(),
                                            fp.getExternalObjectID(),
                                            ItemSerialEntityType.GOODS_DELIVERED.name()));
                                } else if (PAYABLE.equals(fp.getType())) {
                                    item.setBatchItems(itemBatchService.getBatchItemsOfGrnOrGdn(item.getQuoteItemId(),
                                            item.getItemID(),
                                            fp.getExternalObjectID(),
                                            ItemSerialEntityType.GOODS_RECEIVED.name()));
                                }
                            }
                        }
                    }
                    if (CONVERT_TO_INVOICE.equals(fp.getExternalFormID())) {
                        List<NewInvoiceItem> items = new LinkedList<>();
                        for (NewInvoiceItem item : invObject.getItems()) {
                            if (item.getQuantity() != null && item.getQuantity().compareTo(BigDecimal.ZERO) != 0) {
                                items.add(item);
                            }
                        }
                        invObject.setItems(items.toArray(new NewInvoiceItem[]{}));
                    }

                    if (Constants.ONE_OFF_FIXED_AMOUNT.equals(invObject.getDiscountType())) {
                        invObject.setDiscountAmount(totalOverallDiscount);
                    }
                    invObject.getTypeItem().setSupplierCustomerBalance(crmAccountManager.getClientBalance(invObject.getClientID()).doubleValue());


                    EdsSaleQuote saleQuote = quoteManager.getSaleQuote(quote.getObjectID());
                    List<CompanyCustomFieldItem> saleInvoiceCustomFieldsItems = commonService.getCompanyCustomFields(ViewName.SaleInvoice);
                    ArrayList<CompanyCustomFieldItem> saleQuoteCustomFieldsItems = commonService.getCompanyCustomFields(saleQuote != null && saleQuote.isSalesOrder() ? ViewName.SaleOrder : ViewName.SaleQuote);
                    List<CompanyCustomFieldItem> sqCustomValues = CustomFieldsUtils.setRPCCustomFieldItems(quote.getCustomFields(), saleQuoteCustomFieldsItems);

                    ArrayList<CompanyCustomFieldItem> siCustomFields = new ArrayList<>();

                    for (CompanyCustomFieldItem si : saleInvoiceCustomFieldsItems) {
                        si.setObjectId(null);
                        for (CompanyCustomFieldItem sq : sqCustomValues) {
                            if (si.getDataType().equals(sq.getDataType())
                                    && si.getUiType().equals(sq.getUiType())
                                    && si.getAliasName().equals(sq.getAliasName())) {
                                si.setPredefinedValues(sq.getPredefinedValues());
                                si.setPredefinedValuesWithSorting(sq.getPredefinedValuesWithSorting());
                                si.setQuery(sq.getQuery());
                                si.setQueryItems(sq.getQueryItems());
                                si.setFieldStringValue(sq.getFieldStringValue());
                                si.setFieldDateNonConvertedValue(sq.getFieldDateNonConvertedValue());
                                si.setAttachments(sq.getAttachments());
                                si.setLookUpTypeEnum(sq.getLookUpTypeEnum());
                                si.setSelectedId(sq.getSelectedId());
                                si.setDefaultValue(sq.getDefaultValue());
                                si.setPrefix(sq.getPrefix());
                                si.setItem(sq.getItem());
                                si.setSelectItems(sq.getSelectItems());
                            }
                        }
                        siCustomFields.add(si);
                    }

                    invObject.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(null, siCustomFields));

                } else if (PAYABLE.equals(fp.getType())) {
                    EdsPurchaseOrder purchaseOrder = quoteManager.getPurchaseOrderByID(fp.getExternalObjectID());
                    invObject.setPoNumber(purchaseOrder.getNumber());

                    if (CONVERT_TO_INVOICE.equals(fp.getExternalFormID())) {
                        invObject.setFixedAssetRelated(purchaseOrder.isFixedAssetRelated());

                        if (purchaseOrder.getOrderTerms() != null) {
                            invObject.setInvoiceTermsItem(purchaseOrder.getOrderTerms().getAsRPC());
                            invObject.setDueDateType(TERMS_TYPE);
                        }

                        if (invSettings != null) {

                            if (invSettings.getConvertPurchaseInvoiceDateType().equals(AccountingConstants.RECEIVE_DATE_TYPE)) {
                                invObject.setReceiveDate(purchaseOrder.getReceiveDate() != null ? new DateNonConvertable(purchaseOrder.getReceiveDate()) : null);
                            }
                            invObject.setInvoiceType(invSettings.getInvoiceType());
                        }
                        //For overall discount: patrially converting to invoice
                        BigDecimal totalOverallDiscount = BigDecimal.ZERO;
                        for (NewInvoiceItem item : invObject.getItems()) {
                            item.setQuoteItemId(item.getID());

                            if (ReceiveTypeEnum.RECEIVE_BY_VALUE.equals(item.getReceiveType())) {

                                BigDecimal invoicedAmount = BigDecimal.ZERO;
                                if (purchaseOrder.getInvoices() != null) {
                                    for (EdsInvoice invoice : purchaseOrder.getInvoices()) {
                                        if (invoice.getInvoiceItems() != null) {
                                            for (EdsInvoiceItem invoiceItem : invoice.getInvoiceItems()) {
                                                if (item.getItemID().equals(invoiceItem.getItem().getObjectID()) && invoiceItem.getAmmount() != null) {
                                                    invoicedAmount = invoicedAmount.add(invoiceItem.getAmmount());
                                                }
                                            }
                                        }
                                    }
                                }

                                BigDecimal receiveAmount = item.getReceivedAmount().subtract(invoicedAmount);
                                if (receiveAmount.compareTo(BigDecimal.ZERO) == 0)
                                    receiveAmount = BigDecimal.ONE;

                                BigDecimal totalAmount = item.getTotalAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : item.getTotalAmount();

                                item.setQuantity(item.getQuantity().multiply(receiveAmount).divide(totalAmount, 5, RoundingMode.HALF_UP));
                                if (item.getDiscountItemStaticType() == ONE_OFF_FIXED_AMOUNT) {
                                    item.setDiscountAmount(item.getDiscountAmount().multiply(receiveAmount).divide(totalAmount, 5, RoundingMode.HALF_UP));
                                }
                                if (item.getTaxAmount() != null) {
                                    item.setTaxAmount(item.getTaxAmount().multiply(receiveAmount).divide(totalAmount, 5, RoundingMode.HALF_UP));
                                }
                            } else {
                                item.setDiscountAmount(item.getNonConvertedQty().multiply(item.getDiscountAmount() != null ? item.getDiscountAmount() : BigDecimal.ZERO).divide(item.getQuantity(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                                item.setQuantity(item.getNonConvertedQty());
                                totalOverallDiscount = totalOverallDiscount.add(item.getDiscountAmount());
                            }

                            if (lockClosedProjectItems && item.getProject() != null && item.getProject().getId() != null) {
                                EdsProject project = projectManager.get(item.getProject().getId());

                                if (EdsProject.CLOSED.equals(project.getStatus().getCode())) {
                                    item.setProject(null);
                                }
                            }

                            if (item.getInventoryTrackingEnabled()) {
                                item.setSerials(itemSerialService.getSerials(item.getID(), ItemSerialEntityType.GOODS_RECEIVED));
                            }
                            if (item.getTrackBatchesEnabled()) {
                                if (RECEIVABLE.equals(fp.getType())) {
                                    item.setBatchItems(itemBatchService.getBatchItemsOfGrnOrGdn(item.getQuoteItemId(),
                                            item.getItemID(),
                                            fp.getExternalObjectID(),
                                            ItemSerialEntityType.GOODS_DELIVERED.name()));
                                } else if (PAYABLE.equals(fp.getType())) {
                                    item.setBatchItems(itemBatchService.getBatchItemsOfGrnOrGdn(item.getQuoteItemId(),
                                            item.getItemID(),
                                            fp.getExternalObjectID(),
                                            ItemSerialEntityType.GOODS_RECEIVED.name()));
                                }
                            }
                        }
                        if (Constants.ONE_OFF_FIXED_AMOUNT.equals(invObject.getDiscountType())) {
                            invObject.setDiscountAmount(totalOverallDiscount);
                        }
                    }
                    EdsCrmAccount clientBase = crmAccountManager.get(invObject.getClientID());
//                    if (!clientBase.getBalanceCalculated()) {
                    invObject.getTypeItem().setSupplierCustomerBalance(crmAccountManager.getSupplierBalance(clientBase.getObjectID()).doubleValue());
//                    } else {
//                        invObject.getTypeItem().setSupplierCustomerBalance(clientBase.getSupplierBalance().doubleValue());
//                    }
                    invObject.getTypeItem().setReverseChargeApplicable(clientBase.isReverseChargeApplicable());
                }

                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CONVERT_BATCH_SERIALS_EACH_OF_THEM_NEW_LINE_ITEM) && CONVERT_TO_INVOICE.equals(fp.getExternalFormID())) {
                    seperateSerails(invObject);
                }

                if (!PROGRESS_INVOICING.equals(fp.getExternalFormID())) {
                    //set null to the custom field id
                    clearObjectIdFromItems(invObject);
                }


                if (lockClosedProjectItems && invObject.getRelatedProject() != null) {
                    EdsProject project = projectManager.get(invObject.getRelatedProjectID());
                    if (EdsProject.CLOSED.equals(project.getStatus().getCode())) {
                        invObject.setRelatedProject(null);
                    }
                }
            } else if (CONVERT_TO_INVOICE_FROM_GRN.equals(fp.getExternalFormID())) {
                //Info: Convert from GRN & GDN
                final EdsShippingData shippingData = this.shippingDataManager.get(fp.getExternalObjectID());

                if (shippingData != null && shippingData.getQuote() != null) {
                    final EdsQuote quote = shippingData.getQuote();
                    EdsQuote mainQuote = quoteManager.get(quote.getObjectID());
                    invObject = invoiceCircularResolver.getQuote(quote.getObjectID(), fp.getExternalFormID());
                    invObject.setReference(shippingData.getNumber());
                    if (RECEIVABLE.equals(fp.getType())) {
                        invObject.setQuoteNumber(quote.getNumber());
                        invObject.setPoNumber(mainQuote.getPoNumber());
                    } else {
                        invObject.setQuoteNumber(quote.getNumber());
                        invObject.setPoNumber(quote.getNumber());
                    }

                    if (invSettings != null) {
                        if (invSettings.getConvertPurchaseInvoiceDateType().equals(AccountingConstants.RECEIVE_DATE_TYPE)) {
                            if (shippingData.getShippingDate() != null)
                                invObject.setReceiveDate(new DateNonConvertable(shippingData.getShippingDate()));
                        }
                        invObject.setInvoiceType(invSettings.getInvoiceType());
                    }
                    final List<NewInvoiceItem> items = Lists.newArrayListWithCapacity(shippingData.getItems().size());
                    for (EdsShippingDataItem shippingDataItem : shippingData.getItems()) {
                        final EdsQuoteItem quoteItem = shippingDataItem.getQuoteItem();

                        if (quoteItem == null) {
                            continue;
                        }
                        final NewInvoiceItem item = quote.getItem(quoteItem);
                        item.setQuoteItemId(item.getID());
                        item.setID(null);

                        if (ReceiveTypeEnum.RECEIVE_BY_VALUE.equals(shippingDataItem.getReceiveType())) {

                            item.setQuantity(item.getQuantity().multiply(shippingDataItem.getReceivedAmount()).divide(item.getTotalAmount(), 5, RoundingMode.HALF_UP));
                            item.setUnitPrice(item.getUnitPrice());
                            if (item.getDiscountItemStaticType() == ONE_OFF_FIXED_AMOUNT) {
                                item.setDiscountAmount(item.getDiscountAmount().multiply(shippingDataItem.getReceivedAmount()).divide(item.getTotalAmount(), 5, RoundingMode.HALF_UP));
                            }
                            if (item.getTaxAmount() != null) {
                                item.setTaxAmount(item.getTaxAmount().multiply(shippingDataItem.getReceivedAmount()).divide(item.getTotalAmount(), 5, RoundingMode.HALF_UP));
                            }

                        } else {
                            item.setDiscountAmount(shippingDataItem.getReceivedQty().multiply(item.getDiscountAmount() != null ? item.getDiscountAmount() : BigDecimal.ZERO).divide(item.getQuantity(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                            item.setQuantity(shippingDataItem.getReceivedQty());
                        }
                        if (isAlmadarSerials && shippingDataItem.getQuoteItem() != null) {//ALMADAR MEDICAL company
                            ArrayList<CompanyCustomFieldItem> cfList = new ArrayList<>(1);
                            cfList.add(commonService.getCompanyCustomFieldByEntityNameAndFieldName(ViewName.ProductServiceView, "ARTICLE"));
                            item.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(shippingDataItem.getQuoteItem().getItem().getCustomFields(), cfList));
                        }
                        if (item.getInventoryTrackingEnabled()) {
                            if (ShippingDataType.IN.equals(shippingData.getShippingType())) {
                                item.setSerials(itemSerialService.getSerials(shippingDataItem.getObjectID(), ItemSerialEntityType.GOODS_RECEIVED));
                            } else {
                                item.setSerials(itemSerialService.getSerials(shippingDataItem.getObjectID(), ItemSerialEntityType.GOODS_DELIVERED));
                            }
                        }
                        if (item.getTrackBatchesEnabled()) {
                            if (ShippingDataType.IN.equals(shippingData.getShippingType())) {
                                item.setBatchItems(itemBatchService.getBatchItems(quoteItem.getObjectID(),
                                        quoteItem.getItem().getObjectID(),
                                        shippingData.getObjectID(),
                                        ItemSerialEntityType.GOODS_RECEIVED.name()));
                            } else {
                                item.setBatchItems(itemBatchService.getBatchItems(quoteItem.getObjectID(),
                                        quoteItem.getItem().getObjectID(),
                                        shippingData.getObjectID(),
                                        ItemSerialEntityType.GOODS_DELIVERED.name()));
                            }
                        }
                        if (quoteItem.getQuote() != null) {
                            EdsSaleQuote saleQuote = quoteManager.getSaleQuote(quoteItem.getQuote().getObjectID());
                            if (saleQuote != null) {
                                List<CompanyCustomFieldItem> saleInvoiceCustomFieldsItems = commonService.getCompanyAllCustomFields(ViewName.SaleInvoiceItem);
                                ArrayList<CompanyCustomFieldItem> saleQuoteCustomFieldsItems = commonService.getCompanyAllCustomFields(saleQuote != null && saleQuote.isSalesOrder() ? ViewName.SaleOrderItem : ViewName.SaleQuoteItem);
                                List<CompanyCustomFieldItem> sqCustomValues = CustomFieldsUtils.setRPCCustomFieldItems(quoteItem.getCustomFields(), saleQuoteCustomFieldsItems);

                                ArrayList<CompanyCustomFieldItem> siCustomFields = new ArrayList<>();

                                for (CompanyCustomFieldItem si : saleInvoiceCustomFieldsItems) {
                                    si.setObjectId(null);
                                    for (CompanyCustomFieldItem sq : sqCustomValues) {
                                        if (si.getDataType().equals(sq.getDataType())
                                                && si.getUiType().equals(sq.getUiType())
                                                && si.getAliasName().equals(sq.getAliasName())) {
                                            si.setPredefinedValues(sq.getPredefinedValues());
                                            si.setPredefinedValuesWithSorting(sq.getPredefinedValuesWithSorting());
                                            si.setQuery(sq.getQuery());
                                            si.setQueryItems(sq.getQueryItems());
                                            si.setFieldStringValue(sq.getFieldStringValue());
                                            si.setFieldDateNonConvertedValue(sq.getFieldDateNonConvertedValue());
                                            si.setAttachments(sq.getAttachments());
                                            si.setLookUpTypeEnum(sq.getLookUpTypeEnum());
                                            si.setSelectedId(sq.getSelectedId());
                                            si.setDefaultValue(sq.getDefaultValue());
                                            si.setPrefix(sq.getPrefix());
                                            si.setItem(sq.getItem());
                                            si.setSelectItems(sq.getSelectItems());
                                        }
                                    }
                                    siCustomFields.add(si);
                                }

                                item.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(null, siCustomFields));
                            }
                        }
                        items.add(item);
                    }
                    invObject.setItems(items.toArray(new NewInvoiceItem[]{}));

                    if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CONVERT_BATCH_SERIALS_EACH_OF_THEM_NEW_LINE_ITEM)) {
                        seperateSerails(invObject);
                    }

                    if (isAlmadarSerials) {//ALMADAR MEDICAL company
                        ArrayList<CompanyCustomFieldItem> cfList = new ArrayList<>(2);
                        cfList.add(commonService.getCompanyCustomFieldByEntityNameAndFieldName(ShippingDataType.IN.equals(shippingData.getShippingType()) ? ViewName.PurchaseOrder : ViewName.SaleOrder, "Sales Person"));
                        cfList.add(commonService.getCompanyCustomFieldByEntityNameAndFieldName(ShippingDataType.IN.equals(shippingData.getShippingType()) ? ViewName.PurchaseOrder : ViewName.SaleOrder, "PO Date"));
                        invObject.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(shippingData.getQuote().getCustomFields(), cfList));
                    } else if (quote.getCustomFields() != null) {
                        List<EdsCompanyCustomFieldsSettings> quoteCustomFields = companyCFSettingsManager.getCompanyCustomFieldsByEntityName(ShippingDataType.IN.equals(shippingData.getShippingType()) ? ViewName.PurchaseOrder.name() : ViewName.SaleOrder.name());
                        ArrayList<CompanyCustomFieldItem> invoiceCustomFields = commonService.getCompanyCustomFields(RECEIVABLE.equals(fp.getType()) ? ViewName.SaleInvoice : ViewName.PurchaseInvoice);
                        CustomFieldsUtils.setRPCCustomFieldItems(null, invoiceCustomFields);

                        if (quoteCustomFields != null && invoiceCustomFields != null) {
                            for (EdsCompanyCustomFieldsSettings quoteCustomField : quoteCustomFields) {
                                if (quoteCustomField.getAliasName() == null) {
                                    continue;
                                }
                                for (CompanyCustomFieldItem invoiceCustomField : invoiceCustomFields) {
                                    if (quoteCustomField.getAliasName().equals(invoiceCustomField.getAliasName())
                                            && quoteCustomField.getDataType().equals(invoiceCustomField.getDataType())) {

                                        if (DATA_TYPE_DATE.equals(quoteCustomField.getDataType())) {
                                            Date date = (Date) quote.getCustomFields().getValueByCode(quoteCustomField.getDataType(), quoteCustomField.getColumnCode());
//                                            invoiceCustomField.setFieldDateValue(date);
                                            invoiceCustomField.setFieldDateNonConvertedValue(new DateNonConvertable(date));
                                        } else if (DATA_TYPE_NUMBER.equals(quoteCustomField.getDataType())) {
                                            invoiceCustomField.setFieldStringValue((Double) quote.getCustomFields().getValueByCode(quoteCustomField.getDataType(), quoteCustomField.getColumnCode()));
                                        } else {
                                            invoiceCustomField.setFieldStringValue((String) quote.getCustomFields().getValueByCode(quoteCustomField.getDataType(), quoteCustomField.getColumnCode()));
                                        }
                                    }
                                }
                            }
                            invObject.setCustomFieldItems(invoiceCustomFields);
                        }

                    }
                }
            } else if (CONVERT_TO_INVOICE_FROM_RENTAL_ORDER.equals(fp.getExternalFormID())) {
                invObject = new NewInvoice();
                if (invSettings != null) {
                    invObject.setTaxCalculationType(invSettings.getTaxCalculationType());
                    invObject.setInvoiceType(invSettings.getInvoiceType());
                }
                ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.SaleInvoice);
                ArrayList<CompanyCustomFieldItem> saleInvoiceCustomFields = (ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(null, customFieldsItems);
                invObject.setCustomFieldItems(saleInvoiceCustomFields);

                EdsRentalOrder edsRentalOrder = rentalOrderManager.get(fp.getExternalObjectID());
                if (edsRentalOrder != null) {
                    EdsCrmAccount client = edsRentalOrder.getCustomer();
                    if (client != null) {
                        invObject.setClientID(client.getObjectID());
                        invObject.setClientName(client.getName());
                        if (client.getTerms() != null) {
                            invObject.setInvoiceTermsItem(client.getTerms().getAsRPC());
                        }
                        if (RECEIVABLE.equals(fp.getType())) {
                            invObject.setTypeItem(getClientOrSupplier(client.getObjectID(), fp.getType()));
                        }
                        invObject.setReference(edsRentalOrder.getNumber() + " -> " + edsRentalOrder.getName());
                        NewInvoiceItem[] invoiceItems = new NewInvoiceItem[(edsRentalOrder.getItems() != null && edsRentalOrder.getItems().size() > 0) ? edsRentalOrder.getItems().size() : 1];
                        if (edsRentalOrder.getItems() != null && edsRentalOrder.getItems().size() > 0) {
                            int i = 0;
                            for (EdsRentalOrderItem item : edsRentalOrder.getItems()) {
                                invoiceItems[i] = new NewInvoiceItem();
                                EdsItem product = item.getProductItem();
                                invoiceItems[i].setItemID(product != null ? product.getObjectID() : 0);
                                if (product != null) {
                                    invoiceItems[i].setFullItemName(product.getProductNumber() + " -> " + product.getName());
                                } else {
                                    invoiceItems[i].setFullItemName(edsRentalOrder.getNumber());
                                }
                                if (product != null) {
                                    invoiceItems[i].setItemDiscountList(EdsDiscount.getItemDiscounts(product.getDiscounts()));
                                    if (product.getAccount() != null) {
                                        EdsAccount account = product.getAccount();
                                        invoiceItems[i].setAccountItem(account.createAccountItem());
                                    } else {
                                        invoiceItems[i].setAccountItem(getDefaultAccountItem(fp.getFormType(), fp.getType()));
                                    }
                                    if (item.getVat() != null) {
                                        invoiceItems[i].setTaxItem(item.getVat().createTaxItem());
                                    }
                                } else {
                                    invoiceItems[i].setAccountItem(getDefaultAccountItem(fp.getFormType(), fp.getType()));
                                }
                                invoiceItems[i].setQuantity(item.getQty() != null ? item.getQty() : ONE);
                                invoiceItems[i].setUnitPrice(item.getPrice());
                                invoiceItems[i].setDiscountPercent(item.getRentalItem().getDiscountAmount());
                                invoiceItems[i].setDiscountPercent(item.getRentalItem().getDiscountAmount());
                                invoiceItems[i].setNet(item.getPrice());
                                if (item.getVat() != null) {
                                    invoiceItems[i].setTaxItem(item.getVat().createTaxItem());
                                    invoiceItems[i].setTaxAmount(item.getTaxAmount());
                                }
                                invoiceItems[i].setNet(item.getNet());
                                invoiceItems[i].setTotalAmount(item.getSubTotal());
                                invoiceItems[i].setDescription(item.getDescription());
                                i++;
                            }
                            invObject.setItems(invoiceItems);
                        }
                    }
                }
            } else {
                invObject = new NewInvoice();
                if (invSettings != null) {
                    invObject.setTaxCalculationType(invSettings.getTaxCalculationType());
                    invObject.setInvoiceType(invSettings.getInvoiceType());
                }
                if (fp.getOpportunityID() != null) {
                    EdsOpportunity opportunity = jpaTemplate.find(EdsOpportunity.class, fp.getOpportunityID());
                    if (opportunity != null) {
                        ArrayList<CompanyCustomFieldItem> opportunityCustomFields = (ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(opportunity.getCustomFields(),
                                commonService.getCompanyCustomFields(ViewName.Opportunity));

                        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(RECEIVABLE.equals(fp.getType()) ? ViewName.SaleInvoice : ViewName.PurchaseInvoice);
                        ArrayList<CompanyCustomFieldItem> saleInvoiceCustomFields = (ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(null, customFieldsItems);

                        for (CompanyCustomFieldItem inputcf : saleInvoiceCustomFields) {
                            for (CompanyCustomFieldItem resultcf : opportunityCustomFields) {
                                if (inputcf.getAliasName().equals(resultcf.getAliasName()) && inputcf.getUiType().equals(resultcf.getUiType())) {
                                    if (UI_TYPE_DATEPICKER.equals(inputcf.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(inputcf.getUiType())) {
                                        inputcf.setFieldDateNonConvertedValue(resultcf.getFieldDateNonConvertedValue());
                                    } else {
                                        inputcf.setFieldStringValue(resultcf.getFieldStringValue());
                                    }
                                    if (TYPE_ENTITY_LOOKUP.equals(inputcf.getUiType())
                                            || UI_TYPE_LOOKUP.equals(inputcf.getUiType())
                                            || UI_TYPE_CURRENCY.equals(inputcf.getUiType())) {
                                        inputcf.setSelectedId(resultcf.getSelectedId());
                                        inputcf.setEntityType(resultcf.getEntityType());
                                    } else if (TYPE_ENTITY_MULTI_LOOKUP.equals(inputcf.getUiType())
                                            || UI_TYPE_MULTI_LOOKUP.equals(inputcf.getUiType())) {
                                        inputcf.setSelectItems(resultcf.getSelectItems());
                                    } else if (UI_TYPE_ITEM_WITH_DESCRIPTION.equals(inputcf.getUiType())) {
                                        inputcf.setItem(resultcf.getItem());
                                    }
                                }
                            }

                            if (inputcf.getAliasName().equals("PROBABILITY") && (UI_TYPE_TEXTAREA.equals(inputcf.getUiType()) || UI_TYPE_TEXTBOX.equals(inputcf.getUiType())) && opportunity.getProbability() != null) {
                                inputcf.setFieldStringValue(opportunity.getProbability().toString());
                            } else if (inputcf.getAliasName().equals("ASSIGNEE") && UI_TYPE_LOOKUP.equals(inputcf.getUiType()) && CustomFieldLookUpTypeEnum.EMPLOYEE.equals(inputcf.getLookUpTypeEnum()) && opportunity.getAssignee() != null) {
                                inputcf.setFieldStringValue(opportunity.getAssignee().getFullName());
                                inputcf.setSelectedId(opportunity.getAssignee().getObjectID());
                            } else if (inputcf.getAliasName().equals("BACKUP_ASSIGNEE") && UI_TYPE_LOOKUP.equals(inputcf.getUiType()) && CustomFieldLookUpTypeEnum.EMPLOYEE.equals(inputcf.getLookUpTypeEnum()) && opportunity.getBackupAssignee() != null) {
                                inputcf.setFieldStringValue(opportunity.getBackupAssignee().getFullName());
                                inputcf.setSelectedId(opportunity.getBackupAssignee().getObjectID());
                            } else if (inputcf.getAliasName().equals("NUMBER") && (UI_TYPE_TEXTAREA.equals(inputcf.getUiType()) || UI_TYPE_TEXTBOX.equals(inputcf.getUiType())) && opportunity.getNumber() != null) {
                                inputcf.setFieldStringValue(opportunity.getNumber());
                            } else if (inputcf.getAliasName().equals("NAME") && (UI_TYPE_TEXTAREA.equals(inputcf.getUiType()) || UI_TYPE_TEXTBOX.equals(inputcf.getUiType())) && opportunity.getName() != null) {
                                inputcf.setFieldStringValue(opportunity.getName());
                            } else if (inputcf.getAliasName().equals("CUSTOMER") && UI_TYPE_LOOKUP.equals(inputcf.getUiType()) && CustomFieldLookUpTypeEnum.CUSTOMER.equals(inputcf.getLookUpTypeEnum()) && opportunity.getCrmAccount() != null) {
                                inputcf.setFieldStringValue(opportunity.getCrmAccount().getName());
                                inputcf.setSelectedId(opportunity.getCrmAccount().getObjectID());
                            } else if (inputcf.getAliasName().equals("CONTACT") && UI_TYPE_LOOKUP.equals(inputcf.getUiType()) && CustomFieldLookUpTypeEnum.CONTACT.equals(inputcf.getLookUpTypeEnum()) && opportunity.getCrmAccount() != null) {
                                inputcf.setFieldStringValue(opportunity.getCrmContact().getFullName());
                                inputcf.setSelectedId(opportunity.getCrmContact().getObjectID());
                            }
                        }
                        invObject.setCustomFieldItems(saleInvoiceCustomFields);

                        EdsCrmAccount client = opportunity.getCrmAccount();
                        if (client != null) {
                            invObject.setClientID(client.getObjectID());
                            invObject.setClientName(client.getName());
                            if (client.getTerms() != null) {
                                invObject.setInvoiceTermsItem(client.getTerms().getAsRPC());
                            }
                            if (RECEIVABLE.equals(fp.getType())) {
                                invObject.setTypeItem(getClientOrSupplier(client.getObjectID(), fp.getType()));
                            }
                            invObject.setCurrencyID(opportunity.getCurrency() != null ? opportunity.getCurrency().getObjectID() : client.getCurrency() != null ? client.getCurrency().getObjectID() : null);
                            invObject.setReference(opportunity.getNumber() + " -> " + opportunity.getName());
                            invObject.setExchageRate(opportunity.getExchangeRate());
                            invObject.setTaxCalculationType(opportunity.getTaxCalculationType());
                            if (opportunity.getProject() != null) {
                                invObject.setRelatedProject(new SelectItem(opportunity.getProject().getObjectID(), opportunity.getProject().getNumber() != null ? opportunity.getProject().getNumber() + " -> " + opportunity.getProject().getName() : opportunity.getProject().getName()));
                            }
                            NewInvoiceItem[] invoiceItems = new NewInvoiceItem[(opportunity.getOpportunityItems() != null && opportunity.getOpportunityItems().size() > 0) ? opportunity.getOpportunityItems().size() : 1];
                            if (opportunity.getOpportunityItems() != null && opportunity.getOpportunityItems().size() > 0) {
                                int i = 0;
                                for (EdsOpportunityItem item : opportunity.getOpportunityItems()) {
                                    invoiceItems[i] = new NewInvoiceItem();
                                    EdsItem product = item.getItem();
                                    invoiceItems[i].setItemID(product != null ? product.getObjectID() : 0);
                                    if (product != null) {
                                        invoiceItems[i].setFullItemName(product.getProductNumber() + " -> " + product.getName());
                                    } else {
                                        invoiceItems[i].setFullItemName(opportunity.getName());
                                    }
                                    if (product != null) {
                                        invoiceItems[i].setItemDiscountList(EdsDiscount.getItemDiscounts(product.getDiscounts()));
                                        if (product.getAccount() != null) {
                                            EdsAccount account = product.getAccount();
                                            invoiceItems[i].setAccountItem(account.createAccountItem());
                                        } else {
                                            invoiceItems[i].setAccountItem(getDefaultAccountItem(fp.getFormType(), fp.getType()));
                                        }
                                        if (item.getVat() != null) {
                                            invoiceItems[i].setTaxItem(item.getVat().createTaxItem());
                                        }
                                    } else {
                                        invoiceItems[i].setAccountItem(getDefaultAccountItem(fp.getFormType(), fp.getType()));
                                    }
                                    invoiceItems[i].setQuantity(item.getQty() != null ? item.getQty() : ONE);
                                    BigDecimal amount = opportunity.getAmount() != null ? BigDecimal.valueOf(opportunity.getAmount()) : ZERO;
                                    if (RECEIVABLE.equals(fp.getType())) {
                                        invoiceItems[i].setUnitPrice(item.getPrice() != null ? item.getPrice() : amount);
                                    } else {
                                        invoiceItems[i].setUnitPrice(product != null && product.getUnitPrice() != null ? product.getUnitPrice().multiply(opportunity.getExchangeRate()) : BigDecimal.ZERO);
                                    }
                                    invoiceItems[i].setDiscountPercent(item.getDiscount());
                                    invoiceItems[i].setDiscountAmount(item.getDiscountAmount());
                                    if (item.getItemDiscount() != null) {
                                        invoiceItems[i].setItemDiscountID(item.getItemDiscount().getObjectID());
                                        invoiceItems[i].setItemDiscount(item.getItemDiscount().getName());
                                    }
                                    invoiceItems[i].setDiscountItemStaticType(item.getDiscountItemFixedType());
                                    if (item.getUnitMeasurement() != null) {
                                        invoiceItems[i].setMeasurement(item.getUnitMeasurement().getAsSelectItem());
                                    }
                                    invoiceItems[i].setNet(item.getPrice() != null ? item.getPrice() : amount);
                                    invoiceItems[i].setComission(product != null ? product.getComission() : BigDecimal.ZERO);

                                    if (item.getVat() != null) {
                                        invoiceItems[i].setTaxItem(item.getVat().createTaxItem());
                                        invoiceItems[i].setTaxAmount(item.getItemCalculatedTaxAmount());
                                    }
                                    if (item.getProject() != null) {
                                        invoiceItems[i].setProject(new SelectItem(item.getProject().getObjectID(), item.getProject().getNumber() != null ? item.getProject().getNumber() + " -> " + item.getProject().getName() : item.getProject().getName()));
                                    }
                                    invoiceItems[i].setNet(item.getNet());
                                    invoiceItems[i].setTotalAmount(item.getSubTotal());

                                    invoiceItems[i].setDescription(item.getDescription());
                                    invoiceItems[i].setSupplierID(item.getSupplierID());
                                    invoiceItems[i].setSupplierName(item.getSupplierName());

                                    ArrayList<CompanyCustomFieldItem> opportunityItemTableCustomFields = (ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(item.getCustomFields(), commonService.getCompanyCustomFields(ViewName.OpportunitySubItem));

                                    ArrayList<CompanyCustomFieldItem> itemTableCustomFieldsItems = commonService.getCompanyCustomFields(RECEIVABLE.equals(fp.getType()) ? ViewName.SaleInvoiceItem : ViewName.PurchaseInvoiceItem);
                                    ArrayList<CompanyCustomFieldItem> quoteCustomFields = (ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(null, itemTableCustomFieldsItems);


                                    invoiceItems[i].setCustomFieldItems(ServerUtils.mergeCustomFields(opportunityItemTableCustomFields, quoteCustomFields));
                                    i++;
                                }
                            } else {
                                invoiceItems[0] = new NewInvoiceItem();
                                invoiceItems[0].setFullItemName(opportunity.getName());
                                invoiceItems[0].setQuantity(BigDecimal.ONE);
                                BigDecimal amount = opportunity.getAmount() != null ? BigDecimal.valueOf(opportunity.getAmount()) : ZERO;
                                invoiceItems[0].setUnitPrice(amount);
                                invoiceItems[0].setNet(amount);
                                invoiceItems[0].setAccountItem(getDefaultAccountItem(fp.getFormType(), fp.getType()));
                            }
                            invObject.setItems(invoiceItems);
                        }
                    }
                }

                if (fp.getExternalObjectID() != null && (COPY_FROM_CLIENT_SUPPLIER.equals(fp.getExternalFormID())
                        || COPY_FROM_CRM_ACCOUNT.equals(fp.getExternalFormID()) || COPY_FROM_FIXED_ASSET.equals(fp.getExternalFormID())) && fp.getOpportunityID() == null) {
                    invObject.setTypeItem(getClientOrSupplier(fp.getExternalObjectID(), fp.getType()));
                    if (fp.getRelatedProjectID() != null) {
                        invObject.setRelatedProject(getRelatedProject(fp.getRelatedProjectID()));
                    }
                    if (RECEIVABLE.equals(fp.getType())) {
                        invObject.getTypeItem().setSupplierCustomerBalance(crmAccountManager.getClientBalance(fp.getExternalObjectID()).doubleValue());
                    } else {
                        EdsCrmAccount clientBase = crmAccountManager.get(fp.getExternalObjectID());
//                        if (!clientBase.getBalanceCalculated()) {
                        invObject.getTypeItem().setSupplierCustomerBalance(crmAccountManager.getSupplierBalance(clientBase.getObjectID()).doubleValue());
//                        } else {
//                            invObject.getTypeItem().setSupplierCustomerBalance(clientBase.getSupplierBalance().doubleValue());
//                        }
                    }

                    if (lockClosedProjectItems && invObject.getRelatedProject() != null) {
                        EdsProject project = projectManager.get(invObject.getRelatedProjectID());
                        if (EdsProject.CLOSED.equals(project.getStatus().getCode())) {
                            invObject.setRelatedProject(null);
                        }
                    }
                }

                if (fp.getExternalFormID() != null && COPY_FROM_CLIENT_SUPPLIER.equals(fp.getExternalFormID()) && fp.getRelatedProjectID() != null) {
                    invObject.setRelatedProject(getRelatedProject(fp.getRelatedProjectID()));
                }

                if (lockClosedProjectItems && invObject.getRelatedProject() != null) {
                    EdsProject project = projectManager.get(invObject.getRelatedProjectID());
                    if (EdsProject.CLOSED.equals(project.getStatus().getCode())) {
                        invObject.setRelatedProject(null);
                    }
                }
            }
            if (RECEIVABLE.equals(fp.getType())) {
                if (fp.getInvoiceCustomType() != null && !fp.getInvoiceCustomType().isEmpty()) {
                    EdsReference invCustomType = getInvoiceCustomType(fp.getInvoiceCustomType());
                    invObject.setNumberData(getSaleInvoiceNumber(null, invCustomType != null ? invCustomType.getDescription() : null));
                } else {
                    invObject.setNumberData(getSaleInvoiceNumber());
                }
            }
            if (fp.isFromGettingStarted()) {
                invObject.setInvoiceDate(new DateNonConvertable(getInvoiceDate(-1)));
            }
        }
        if (isAlmadarSerials) {
            EdsCrmAccount clientSupp = crmAccountManager.get(invObject.getClientID());
            if (clientSupp != null && clientSupp.getTerms() != null) {
                invObject.setDueDays(clientSupp.getTerms().getDays()); //allo
            }
        } else {
            invObject.setDueDays(getNumberForDueDate());
        }
        /*---Dropdown items data start---*/
        if (RECEIVABLE.equals(fp.getType())) {
            invObject.setLayoutHTML(PathFinder.getLayoutHTML(SALE_INVOICE));
            invObject.setRecurrencePatterns(getRecurrencePattern());
            invObject.setProductSerialsEnabled(genericSettings.contains(GenericSettingsEnum.PRODUCT_SERIAL_ENABLED));
            if (fp.isProjectBasedInvoice()) {
                invObject.setPdfTemplateList(getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PROJECT_BASED_INVOICE.name()));
            } else {
                invObject.setPdfTemplateList(getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.SALES_INVOICE.name()));
                invObject.setClientPdfTemplateList(getClientPdfTemplatesByType(PdfReferenceCodeNameEnum.SALES_INVOICE.name()));
            }

            if (fp.getObjectID() != null) {
                List<EdsShippingData> convertedShippingDataList = shippingDataManager.getGrnGdnsByInvoiceId(fp.getObjectID());
                if (convertedShippingDataList != null && !convertedShippingDataList.isEmpty()) {
                    invObject.setFromGdn(true);
                }
            }

//            invObject.setPdfTemplateList(this.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.SALES_INVOICE.getCode()));
            invObject.setHtmlTemplateList(this.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.SALES_INVOICE.name(), true));

            ArrayList<SelectItem> list = paymentMethodManager.list()
                    .stream()
                    .map(paymentMethod -> new SelectItem(paymentMethod.getObjectID(), paymentMethod.getName()))
                    .collect(Collectors.toCollection(ArrayList::new));
            invObject.setPaymentMethods(list);
            invObject.setBaseCurrencyName(this.getBaseCurrency().getName());
            if (fp.getObjectID() != null && invObject.getItems() != null) {
                for (NewInvoiceItem item : invObject.getItems()) {
                    if (item.getTrackBatchesEnabled()) {
                        item.setBatchItems(itemBatchService.getBatchItems(item.getID(), item.getItemID(), invObject.getID(), ItemSerialEntityType.SALES_INVOICE.name()));
                    }
                    if (item.getItemID() != null) {
                        item.setItemAverageCost(productServiceLocal.getAverageCost(item.getItemID()));
                    }
                }
            }
            invObject.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_SALEINVOICE));
        }

        invObject.setDefaultAccountItem(getDefaultAccountItem(fp.getFormType(), fp.getType()));

        invObject.setDefaultDiscountItem(getDefaultDiscountItem(fp.getFormType()));

        invObject.setDefaultTaxItem(getDefaultTaxItem());

        if (PAYABLE.equals(fp.getType())) {
            if (fp.getObjectID() != null && invObject.getItems() != null) {
                for (NewInvoiceItem item : invObject.getItems()) {
                    if (item.getTrackBatchesEnabled()) {
                        item.setBatchItems(itemBatchService.getBatchItems(item.getID(), item.getItemID(), invObject.getID(), ItemSerialEntityType.PURCHASE_INVOICE.name()));
                    }
                }
            }
            invObject.setLayoutHTML(PathFinder.getLayoutHTML(PURCHASE_INVOICE));
            if (invObject.getMailAddressID() != null) {
                EdsAddress address = addressManager.get(invObject.getMailAddressID());
                if (address != null) {
                    invObject.setCompanyMailAddressAsHTML(address.getAddressDataAsHTML());
                }
            }
            invObject.setPurchaseClientEnabled(genericSettings.contains(GenericSettingsEnum.PURCHASE_CLIENT_ENABLED));
            invObject.setCancelDateEnabled(genericSettings.contains(GenericSettingsEnum.CANCEL_DATE_ENABLED));
            invObject.setPdfTemplateList(getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PURCHASE_INVOICE.name()));
            if (invSettings != null && invSettings.getIsPurchaseInvoiceNumberingShow()) {
                invObject.setNumberData(getPurchaseInvoiceNumber(false));
                invObject.setPurchaseInvoiceNumberingShow(true);
            } else {
                invObject.setPurchaseInvoiceNumberingShow(false);
            }
            invObject.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_PURCHASE_INVOICE));
        }

        if (fp.getObjectID() == null && invObject.getCustomFieldItems() == null && !CONVERT_TO_INVOICE_FROM_RENTAL_ORDER.equals(fp.getExternalFormID()) && !CONVERT_TO_INVOICE_FROM_GRN.equals(fp.getExternalFormID()) && !(CONVERT_TO_INVOICE.equals(fp.getExternalFormID()) && RECEIVABLE.equals(fp.getType()))) {
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(RECEIVABLE.equals(fp.getType()) ? ViewName.SaleInvoice : ViewName.PurchaseInvoice);
            invObject.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(null, customFieldsItems));
        }
        ArrayList<CompanyCustomFieldItem> systemFields = commonService.getCompanyCustomFields(RECEIVABLE.equals(fp.getType()) ? ViewName.SaleInvoiceSystem : ViewName.PurchaseInvoiceSystem);
        systemFields.forEach(f -> f.setFieldName(commonLocalizer.localize(f.getFieldName().toLowerCase(), f.getFieldName())));
        invObject.setSystemCustomFields(systemFields);
        if (fp.getExternalObjectID() != null) {
            EdsCrmAccount clientBase = crmAccountManager.get(fp.getExternalObjectID());
            if (clientBase != null && clientBase.getTerms() != null) {
                invObject.setInvoiceTermsItem(clientBase.getTerms().getAsRPC());
            }
        }

        ArrayList<CompanyCustomFieldItem> itemCustomFields = commonService.getCompanyAllCustomFields(RECEIVABLE.equals(fp.getType()) ? ViewName.SaleInvoiceItem : ViewName.PurchaseInvoiceItem);
        invObject.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));
        invObject.setLumpSumEnabled(genericSettings.contains(GenericSettingsEnum.LUMPSUM_ENABLED));
        invObject.setRoundingModeDisabled(genericSettings.contains(GenericSettingsEnum.ROUNDING_MODE_DISABLED));
        invObject.setDoubleTaxEnabled(genericSettings.contains(GenericSettingsEnum.DOUBLE_TAX_ENABLED));
        invObject.setCustomExcelEnabled(genericSettings.contains(GenericSettingsEnum.GENERATE_CUSTOM_EXCEL_ENABLED));
        invObject.setMultiQuoteConvertEnabled(genericSettings.contains(GenericSettingsEnum.ENABLE_MULTI_QUOTE_CONVERT));
        invObject.setCustomItemColumns(itemTableSettingService.getColumnConfigs(RECEIVABLE.equals(fp.getType()) ? ItemTableEnum.SALE_INVOICE_ITEM : ItemTableEnum.PURCHASE_INVOICE_ITEM));

        if (invSettings != null) {
            invObject.setSalesQuoteTermCopyToSalesInvoice(invSettings.isSalesQuoteTermCopyToSalesInvoice());
            invObject.setSalesQuoteTermCopyToSalesOrder(invSettings.isSalesQuoteTermCopyToSalesOrder());
            invObject.setSalesOrderTermCopyToSalesInvoice(invSettings.isSalesOrderTermCopyToSalesInvoice());
            invObject.setDueDateType(invSettings.getDueDateType());
            invObject.setConvertPurchaseInvoiceDateType(invSettings.getConvertPurchaseInvoiceDateType());
            if (invSettings.getBankAccountId() != null) {
                final EdsBankAccount bankAcc = bankAccountManager.get(invSettings.getBankAccountId());

                if (bankAcc != null) {
                    final BankAccount bankAccount = new BankAccount();

                    bankAccount.setObjectId(bankAcc.getObjectID());
                    bankAccount.setAccountId(bankAcc.getAccount().getObjectID());
                    bankAccount.setCode(bankAcc.getAccount().getCodeString());
                    bankAccount.setName(bankAcc.getAccount().getName());
                    invObject.setBankAccountItem(bankAccount);
                }
            }
        }
        // If it is converted from SO/SQ delete and add rows needs to be dsiabled
        if (invObject.getConvertedItemID() != null) {
            invObject.setIsDeleteAndAddDsiabled(true);
        }
        if (fp.getConvertFormType() != null && fp.getConvertFormId() != null) {
            if (RelationItem.TYPE_PURCHASE_INVOICE.equals(fp.getConvertFormType()) && RECEIVABLE.equals(fp.getType())) {
                final EdsFormProperty formProperty = this.formPropertyManager.getByFormID(LayoutRPC.PURCHASEINVOICE_FORM);
//
                final Gson gson = new Gson();
                final FormProperty[] fields = gson.fromJson(formProperty.getSettingsJSONData(), FormProperty[].class);

                NewInvoice purchaseInvData = getInvoiceSummaryData(fp.getConvertFormId());
                if (purchaseInvData != null) {
                    invObject.setFromName(purchaseInvData.getInvoiceNumber() != null ? purchaseInvData.getInvoiceNumber() : fp.getConvertFormId().toString());
                    invObject.setConvertedRelations(purchaseInvData.getRelations());

                    if (purchaseInvData.getInvoiceDate() != null) {
                        invObject.setInvoiceDate(purchaseInvData.getInvoiceDate());
                    }
                    if (purchaseInvData.getInvoiceTermsItem() != null) {
                        invObject.setInvoiceTermsItem(purchaseInvData.getInvoiceTermsItem());
                    } else if (purchaseInvData.getDueDate() != null) {
                        invObject.setDueDate(purchaseInvData.getDueDate());
                    }
                    if (purchaseInvData.getReference() != null) {
                        invObject.setReference(purchaseInvData.getReference());
                    }
                    if (purchaseInvData.getCurrencyID() != null) {
                        invObject.setCurrencyID(purchaseInvData.getCurrencyID());
                    }
                    if (purchaseInvData.getTaxCalculationType() != null) {
                        invObject.setTaxCalculationType(purchaseInvData.getTaxCalculationType());
                    }
                    if (purchaseInvData.getBankAccount() != null) {
                        invObject.setBankAccount(purchaseInvData.getBankAccount());
                    }
                    if (purchaseInvData.getAccountsReceivablePayable() != null) {
                        invObject.setAccountsReceivablePayable(purchaseInvData.getAccountsReceivablePayable());
                    }
                    if (purchaseInvData.getIntroduction() != null) {
                        invObject.setIntroduction(purchaseInvData.getIntroduction());
                    }

                    if (purchaseInvData.getCustomFieldItems() != null && purchaseInvData.getCustomFieldItems().size() > 0) {
                        for (final CompanyCustomFieldItem companyCustomFieldItem : purchaseInvData.getCustomFieldItems()) {
                            this.convertFormCustomFields(invObject, fields, companyCustomFieldItem, fp);
                        }
                    }
                    if (purchaseInvData.getCustomFieldItems() != null && purchaseInvData.getCustomFieldItems().size() > 0) {
                        for (final CompanyCustomFieldItem companyCustomFieldItem : purchaseInvData.getCustomFieldItems()) {
                            if (companyCustomFieldItem != null) {
                                switch (companyCustomFieldItem.getAliasName()) {
                                    case "SUPPLIER" -> {
                                        if (Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.SUPPLIER.equals(companyCustomFieldItem.getLookUpTypeEnum()) && purchaseInvData.getTypeItem() != null) {
                                            companyCustomFieldItem.setSelectedId(purchaseInvData.getTypeItem().getId());
                                            companyCustomFieldItem.setFieldStringValue(purchaseInvData.getTypeItem().getName());
                                        }
                                    }
                                    case "DATE" -> {
                                        if (Constants.DATA_TYPE_DATE.equals(companyCustomFieldItem.getDataType()) && purchaseInvData.getInvoiceDate() != null) {
                                            companyCustomFieldItem.setFieldDateNonConvertedValue(purchaseInvData.getInvoiceDate());
                                        }
                                    }
                                    case "DUE_DATE" -> {
                                        if (Constants.DATA_TYPE_DATE.equals(companyCustomFieldItem.getDataType()) && purchaseInvData.getDueDate() != null) {
                                            companyCustomFieldItem.setFieldDateNonConvertedValue(purchaseInvData.getDueDate());
                                        }
                                    }
                                    case "NUMBER" -> {
                                        if (Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) && purchaseInvData.getInvoiceNumber() != null) {
                                            companyCustomFieldItem.setFieldStringValue(purchaseInvData.getInvoiceNumber());
                                        }
                                    }
                                    case "PROJECT" -> {
                                        if (Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.PROJECT.equals(companyCustomFieldItem.getLookUpTypeEnum()) && purchaseInvData.getRelatedProject() != null) {
                                            companyCustomFieldItem.setSelectedId(purchaseInvData.getRelatedProject().getId());
                                            companyCustomFieldItem.setFieldStringValue(purchaseInvData.getRelatedProject().getName());
                                        }
                                    }
                                    case "CURRENCY" -> {
                                        if ((Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.CURRENCY.equals(companyCustomFieldItem.getLookUpTypeEnum()) || Constants.UI_TYPE_CURRENCY.equals(companyCustomFieldItem.getUiType())) && purchaseInvData.getCurrencyID() != null) {
                                            companyCustomFieldItem.setSelectedId(purchaseInvData.getCurrencyID());
                                            companyCustomFieldItem.setFieldStringValue(purchaseInvData.getCurrencyName());
                                        }
                                    }
                                    case "REFERENCE" -> {
                                        if (Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) && purchaseInvData.getReference() != null) {
                                            companyCustomFieldItem.setFieldStringValue(purchaseInvData.getReference());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    final ArrayList<NewInvoiceItem> listItems = new ArrayList<>();
                    if (purchaseInvData.getItems() != null) {
                        purchaseInvData.getItems();
                        for (NewInvoiceItem purInvItem : purchaseInvData.getItems()) {
                            final NewInvoiceItem newInvoiceItem = purInvItem;
                            newInvoiceItem.setID(null);
                            final ArrayList<CompanyCustomFieldItem> itemCFs = new ArrayList<>();
                            if (newInvoiceItem != null) {
                                List<CompanyCustomFieldItem> piCFs = purInvItem.getCustomFieldItems();
                                newInvoiceItem.setCustomFieldItems(null);
                                if (piCFs != null) {
                                    for (CompanyCustomFieldItem purInvItemCf : piCFs) {
                                        if (purInvItemCf != null) {
                                            convertItemTableFields(newInvoiceItem, itemCFs, purInvItemCf, fp);
                                        }
                                    }
                                }
                                newInvoiceItem.setCustomFieldItems(itemCFs);
                            }
                            listItems.add(newInvoiceItem);
                        }
                    }
                    invObject.setItems(listItems.toArray(new NewInvoiceItem[0]));
                }
            } else if (fp.getConvertFormType().contains("_FORM")) {

                invObject.setConvertedRelations(EdsRelation.asRPCs(relationManager.getAllRelations(fp.getConvertFormType(), fp.getConvertFormId())));

                EdsFormProperty formProperty = formPropertyManager.getByFormID(RECEIVABLE.equals(fp.getType()) ? LayoutRPC.SALEINVOICE_FORM : LayoutRPC.PURCHASEINVOICE_FORM);

                Gson gson = new Gson();
                FormProperty[] fields = gson.fromJson(formProperty.getSettingsJSONData(), FormProperty[].class);

                EdsCustomFormItems edsItem = customFormItemManager.get(fp.getConvertFormId());
                FormItems formItems = edsItem.toRpc();

                Set<EdsCustomItemTable> itemTables = edsItem.getItemTables();

                HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

                if (itemTables != null || itemTables.size() > 0) {

                    for (EdsCustomItemTable itemTable : itemTables) {
                        CustomTableRpc rpc = itemTable.getRpc();

                        rpc.setItemCustomFields((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                                commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.CustomFormItemTable, rpc.getUuid())));

                        map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                    }
                    formItems.setTableItems(map);
                }
                Map<String, ArrayList<CustomTableRpc>> tableItems = formItems.getTableItems();


                for (List<CustomTableRpc> tableRpcs : tableItems.values()) {
                    tableRpcs.sort(Comparator.comparing(CustomTableRpc::getId));
                }

                ArrayList<NewInvoiceItem> listItems = new ArrayList<>();
                for (Map.Entry<String, ArrayList<CustomTableRpc>> mapTables : formItems.getTableItems().entrySet()) {
                    List<CustomTableRpc> values = mapTables.getValue();
                    for (CustomTableRpc rpc : values) {
                        NewInvoiceItem newInvoiceItem = new NewInvoiceItem();
                        ArrayList<CompanyCustomFieldItem> itemCFs = new ArrayList<>();
                        if (rpc != null && rpc.getItemCustomFields() != null) {
                            for (CompanyCustomFieldItem itemCF : rpc.getItemCustomFields()) {
                                if (itemCF != null) {
                                    convertItemTableFields(newInvoiceItem, itemCFs, itemCF, fp);
                                }
                            }
                        }
                        newInvoiceItem.setCustomFieldItems(itemCFs);
                        listItems.add(newInvoiceItem);
                    }
                }
                invObject.setItems(listItems.toArray(new NewInvoiceItem[0]));

                formItems.setCustomFieldItems((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(edsItem.getFormCustomFields(),
                        commonServiceLocal.getCompanyCategoryCustomFields(edsItem.getCustomForm() != null ? edsItem.getCustomForm().getObjectID() : null)));

                if (formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
                    for (int i = 0; i < formItems.getCustomFieldItems().size(); i++) {
                        if (UI_TYPE_AUTONUMBER.equals(formItems.getCustomFieldItems().get(i).getUiType()) && formItems.getCustomFieldItems().get(i).getFieldStringValue() != null) {
                            formItems.setAutoNumber(formItems.getCustomFieldItems().get(i).getFieldStringValue());
                            break;
                        }
                    }
                }
                invObject.setFromName(formItems.getAutoNumber() != null ? formItems.getAutoNumber() : formItems.getFormName() + ": " + fp.getConvertFormId());

                if (formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
                    for (CompanyCustomFieldItem companyCustomFieldItem : formItems.getCustomFieldItems()) {
                        convertFormCustomFields(invObject, fields, companyCustomFieldItem, fp);
                    }
                }
            }
        }
        if (invObject != null && invObject.getItems() != null) {
            EdsUser currentUser = userManager.getUser();
            EdsCompany currentCompany = currentUser.getCompany();
            boolean isArabicCompany = ServerUtils.isArabicCompany(currentCompany);
            if (isArabicCompany) {
                for (NewInvoiceItem item : invObject.getItems()) {
                    Set<SelectItem> categories = new HashSet<>();
                    Set<SelectItem> purchaseCategories = new HashSet<>();
                    if (item.getTaxItem() == null) continue;
                    EdsVat edsVat = vatManager.get(item.getTaxItem().getId());
                    if (edsVat == null) continue;
                    if (edsVat.getFaiCategorieIds() != null) {
                        for (Integer categoryId : edsVat.getFaiCategorieIds()) {
                            EdsReference cat = referenceManager.get(categoryId);
                            categories.add(new SelectItem(cat.getObjectID(), cat.getName()));
                        }
                    }
                    if (edsVat.getFaiPurchaseCategoryIds() != null) {
                        for (Integer categoryId : edsVat.getFaiPurchaseCategoryIds()) {
                            EdsReference cat = referenceManager.get(categoryId);
                            purchaseCategories.add(new SelectItem(cat.getObjectID(), cat.getName()));
                        }
                    }
                    TaxItem taxItem = item.getTaxItem();
                    taxItem.setFaiCategories(categories.toArray(SelectItem[]::new));
                    taxItem.setFaiPurchaseCategories(purchaseCategories.toArray(SelectItem[]::new));
                    item.setTaxItem(taxItem);
                }
            }
        }
        return invObject;
    }

    /**
     * FROM
     * Item 1 {serial 1-1,serial 1-2,serail 1-3}
     * TO
     * Item 1 {serial 1-1}
     * Item 1 {serail 1-2}
     * Item 1 {serail 1-3}
     */
    private void seperateSerails(NewInvoice invoice) {
        if (invoice.getItems() != null && invoice.getItems().length > 0) {
            List<NewInvoiceItem> separatedItems = new LinkedList<>();
            for (NewInvoiceItem item : invoice.getItems()) {
                if (item.getTrackBatchesEnabled() && item.getBatchItems() != null && !item.getBatchItems().isEmpty()) {
                    for (ProductTrackBatchItem batchItem : item.getBatchItems()) {
                        // copyData new istance
                        NewInvoiceItem newItem = NewInvoiceItem.copyData(item);
                        ArrayList<ProductTrackBatchItem> singleBatchItemList = new ArrayList<>();
                        singleBatchItemList.add(batchItem);
                        newItem.setQuantity(batchItem.getQty());
                        newItem.setBatchItems(singleBatchItemList);

                        separatedItems.add(newItem);
                    }
                } else {
                    separatedItems.add(item);
                }
            }
            invoice.setItems(separatedItems.toArray(new NewInvoiceItem[0]));
        }
    }

    private void copyPICustomFieldsToSISystemFields(NewInvoice invObject, ArrayList<CompanyCustomFieldItem> purchaseCustomFields) {
        if (isOk(purchaseCustomFields)) {
            purchaseCustomFields.forEach(item -> {
                if ("reference".equalsIgnoreCase(item.getAliasName())) {
                    invObject.setReference(item.getFieldStringValue());
                } else if ("taxcalc".equalsIgnoreCase(item.getAliasName()) && UI_TYPE_LOOKUP.equalsIgnoreCase(item.getUiType()) && UI_TYPE_DROPDOWN.equalsIgnoreCase(item.getUiType())) {
                    invObject.setTaxCalculationType(item.getSelectedId());
                } else if ("number".equalsIgnoreCase(item.getAliasName())) {
                    invObject.setInvoiceNumber(item.getFieldStringValue());
                } else if ("invoiceType".equalsIgnoreCase(item.getAliasName()) && UI_TYPE_LOOKUP.equalsIgnoreCase(item.getUiType()) && UI_TYPE_DROPDOWN.equalsIgnoreCase(item.getUiType())) {
                    invObject.setInvoiceType(item.getSelectedId());
                } else if ("dueDate".equalsIgnoreCase(item.getAliasName()) && DATA_TYPE_DATE.equalsIgnoreCase(item.getDataType())) {
                    invObject.setDueDate(item.getFieldDateNonConvertedValue());
                }
            });
        }
    }


    private ArrayList<CompanyCustomFieldItem> copyCustomFieldData(ArrayList<CompanyCustomFieldItem> toCustomFieldsList, ArrayList<CompanyCustomFieldItem> fromCustomFieldsList) {
        ArrayList<CompanyCustomFieldItem> resultItems = new ArrayList<>();
        if (!(isOk(toCustomFieldsList) && isOk(fromCustomFieldsList))) {
            return resultItems;
        }
        for (CompanyCustomFieldItem toCustomField : toCustomFieldsList) {
            toCustomField.setObjectId(null);
            for (CompanyCustomFieldItem fromCustomField : fromCustomFieldsList) {
                if (toCustomField.getDataType().equals(fromCustomField.getDataType())
                        && toCustomField.getUiType().equals(fromCustomField.getUiType())
                        && toCustomField.getAliasName().equals(fromCustomField.getAliasName())) {
                    toCustomField.setPredefinedValues(fromCustomField.getPredefinedValues());
                    toCustomField.setPredefinedValuesWithSorting(fromCustomField.getPredefinedValuesWithSorting());
                    toCustomField.setQuery(fromCustomField.getQuery());
                    toCustomField.setQueryItems(fromCustomField.getQueryItems());
                    toCustomField.setFieldStringValue(fromCustomField.getFieldStringValue());
                    toCustomField.setFieldDateNonConvertedValue(fromCustomField.getFieldDateNonConvertedValue());
                    toCustomField.setAttachments(fromCustomField.getAttachments());
                    toCustomField.setLookUpTypeEnum(fromCustomField.getLookUpTypeEnum());
                    toCustomField.setSelectedId(fromCustomField.getSelectedId());
                    toCustomField.setDefaultValue(fromCustomField.getDefaultValue());
                    toCustomField.setPrefix(fromCustomField.getPrefix());
                    toCustomField.setItem(fromCustomField.getItem());
                    toCustomField.setSelectItems(fromCustomField.getSelectItems());
                }
            }
            resultItems.add(toCustomField);
        }

        return resultItems;
    }

    private void convertItemTableFields(NewInvoiceItem
                                                newInvoiceItem, ArrayList<CompanyCustomFieldItem> itemCFs, CompanyCustomFieldItem itemCF, Params fp) {
        if ("DESCRIPTION".equals(itemCF.getAliasName()) && (UI_TYPE_TEXTBOX.equals(itemCF.getUiType()) || UI_TYPE_TEXTAREA.equals(itemCF.getUiType())) || (UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType()) && "PRODUCT".equals(itemCF.getAliasName()))) {
            if (UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType())) {
                newInvoiceItem.setDescription(itemCF.getItem() != null ? itemCF.getItem().getDescription() : "");
            } else {
                newInvoiceItem.setDescription(itemCF.getFieldStringValue() != null ? itemCF.getFieldStringValue() : "");
            }
        }
        if ("PRODUCT".equals(itemCF.getAliasName()) && (UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.PRODUCT.equals(itemCF.getLookUpTypeEnum()) || UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType()))) {
            if (UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType())) {
                newInvoiceItem.setItemID(itemCF.getItem() != null ? itemCF.getItem().getId() : null);
                newInvoiceItem.setItemName(itemCF.getItem() != null ? itemCF.getItem().getName() : "");
                newInvoiceItem.setFullItemName(itemCF.getItem() != null ? itemCF.getItem().getName() : "");
            } else {
                newInvoiceItem.setItemID(itemCF.getSelectedId());
                newInvoiceItem.setItemName(itemCF.getFieldStringValue() != null ? itemCF.getFieldStringValue() : "");
                newInvoiceItem.setFullItemName(itemCF.getFieldStringValue() != null ? itemCF.getFieldStringValue() : "");
            }
            if (newInvoiceItem.getItemID() != null) {
                EdsItem item = itemManager.get(newInvoiceItem.getItemID());
                if (PAYABLE.equals(fp.getType())) {
                    if (item.getCogsAccount() != null) {
                        EdsAccount account = item.getCogsAccount();
                        newInvoiceItem.setAccountID(account.getObjectID());
                        newInvoiceItem.setAccountItem(account.createAccountItem());
                    }
                } else {
                    if (item.getAccount() != null) {
                        EdsAccount account = item.getAccount();
                        newInvoiceItem.setAccountID(account.getObjectID());
                        newInvoiceItem.setAccountItem(account.createAccountItem());
                    }
                }
            }
        }
        if ("QTY".equals(itemCF.getAliasName()) && DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
            newInvoiceItem.setQuantity(itemCF.getFieldStringValue() != null ? new BigDecimal(itemCF.getFieldStringValue()) : null);
        }
        if ("MEASUREMENT".equals(itemCF.getAliasName()) && UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.UNIT_MEASUREMENT.equals(itemCF.getLookUpTypeEnum())) {
            newInvoiceItem.setMeasurement(new SelectItem(itemCF.getSelectedId(), itemCF.getFieldStringValue() != null ? itemCF.getFieldStringValue() : ""));
        }
        if (RECEIVABLE.equals(fp.getType())) {
            if ("UNITPRICE".equals(itemCF.getAliasName()) && DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
                newInvoiceItem.setUnitPrice(itemCF.getFieldStringValue() != null ? new BigDecimal(itemCF.getFieldStringValue()) : null);
            }
        } else {
            if ("COSTPRICE".equals(itemCF.getAliasName()) && DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
                newInvoiceItem.setUnitPrice(itemCF.getFieldStringValue() != null ? new BigDecimal(itemCF.getFieldStringValue()) : null);
            }
        }
        if ("DISCOUNT_AMT".equals(itemCF.getAliasName()) && DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
            newInvoiceItem.setDiscountAmount(itemCF.getFieldStringValue() != null ? new BigDecimal(itemCF.getFieldStringValue()) : null);
        }
        if ("PROJECT".equals(itemCF.getAliasName()) && UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.PROJECT.equals(itemCF.getLookUpTypeEnum())) {
            newInvoiceItem.setProject(new SelectItem(itemCF.getSelectedId(), itemCF.getFieldStringValue() != null ? itemCF.getFieldStringValue() : ""));
        }

        for (CompanyCustomFieldItem customFieldItem : commonService.getCompanyCustomFields(RECEIVABLE.equals(fp.getType()) ? ViewName.SaleInvoiceItem : ViewName.PurchaseInvoiceItem)) {
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
                    customFieldItem.setFieldDateNonConvertedValue(itemCF.getFieldDateNonConvertedValue());
                    customFieldItem.setItem(itemCF.getItem());
                }
                itemCFs.add(customFieldItem);
            }
        }
    }

    private void convertFormCustomFields(NewInvoice item, FormProperty[] fields, CompanyCustomFieldItem
            companyCustomFieldItem, Params fp) {
        if (companyCustomFieldItem != null) {
            for (FormProperty formProperty1 : fields) {
                if (formProperty1 != null) {
                    if (companyCustomFieldItem.getAliasName().equals(formProperty1.getAliasName())) {
                        switch (formProperty1.getCode()) {
                            case "inputcrmaccount" -> {
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && RECEIVABLE.equals(fp.getType()) ? CustomFieldLookUpTypeEnum.CUSTOMER.equals(companyCustomFieldItem.getLookUpTypeEnum()) : CustomFieldLookUpTypeEnum.SUPPLIER.equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                    TypeItem typeItem = new TypeItem();
                                    typeItem.setId(companyCustomFieldItem.getSelectedId());
                                    typeItem.setName(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                    item.setTypeItem(typeItem);

                                    if (companyCustomFieldItem.getSelectedId() != null) {
                                        EdsCrmAccount clientBase = crmAccountManager.get(companyCustomFieldItem.getSelectedId());
                                        if (clientBase != null && clientBase.getTerms() != null) {
                                            item.setInvoiceTermsItem(clientBase.getTerms().getAsRPC());
                                        }
                                    }
                                }
                            }
                            case "inputdate" -> {
                                if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setInvoiceDate(companyCustomFieldItem.getFieldDateNonConvertedValue());
                                }
                            }
                            case "inputnumber" -> {
                                if (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_AUTONUMBER.equals(companyCustomFieldItem.getUiType())) {
                                    item.setInvoiceNumber(companyCustomFieldItem.getFieldStringValue());
                                }
                            }
                            case "inputcurrency" -> {
                                if ((companyCustomFieldItem.getUiType().equals(UI_TYPE_CURRENCY) || (companyCustomFieldItem.getUiType().equals(UI_TYPE_LOOKUP) && CustomFieldLookUpTypeEnum.CURRENCY.equals(companyCustomFieldItem.getLookUpTypeEnum()))) && companyCustomFieldItem.getSelectedId() != null) {
                                    item.setCurrencyID(companyCustomFieldItem.getSelectedId());
                                    item.setCurrencyName(companyCustomFieldItem.getFieldStringValue());
                                    if (item.getCurrencyID() != null) {
                                        CurrencyListItem currencyListItem = currencyService.getCurrencyRateByDate(item.getCurrencyID(), new DateNonConvertable(new Date()));
                                        BigDecimal exchangeRate = BigDecimal.valueOf(currencyListItem.getExchangeRate()).setScale(financialSettingsManager.getFinancialSettings().getExchangeRateScale(), RoundingMode.HALF_UP);
                                        item.setExchageRate(exchangeRate);
                                    }
                                }
                            }
                            case "inputduedate" -> {
                                if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setDueDate(companyCustomFieldItem.getFieldDateNonConvertedValue());
                                }
                            }
                            case "inputreference" -> {
                                if (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                                    item.setReference(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                            }
                            case "PROJECT" -> {
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.PROJECT.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getFieldStringValue() != null && companyCustomFieldItem.getSelectedId() != null) {
                                    item.setRelatedProject(new SelectItem(companyCustomFieldItem.getSelectedId(), companyCustomFieldItem.getFieldStringValue()));
                                }
                            }
                            case "PO_NUMBER" -> {
                                if ((UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) && companyCustomFieldItem.getFieldStringValue() != null && companyCustomFieldItem.getFieldStringValue() != null) {
                                    item.setPoNumber(companyCustomFieldItem.getFieldStringValue());
                                }
                            }
                            case "SUPPLIER_INVOICE_TERM" -> {
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.TERMS.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getSelectedId() != null) {
                                    EdsInvoiceTerms invoiceTerms = invoiceTermsManager.get(companyCustomFieldItem.getSelectedId());
                                    if (invoiceTerms != null) {
                                        item.setInvoiceTermsItem(invoiceTerms.getAsRPC());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
                for (CompanyCustomFieldItem cf : item.getCustomFieldItems()) {
                    if (companyCustomFieldItem.getAliasName().equals(cf.getAliasName()) && companyCustomFieldItem.getUiType().equals(cf.getUiType()) && companyCustomFieldItem.getDataType().equals(cf.getDataType())) {
                        if (UI_TYPE_LOOKUP.equals(cf.getUiType())) {
                            if (cf.getLookUpTypeEnum().equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                cf.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                                cf.setSelectedId(companyCustomFieldItem.getSelectedId());
                                cf.setItem(companyCustomFieldItem.getItem());
                            }
                        } else if (TYPE_ENTITY_LOOKUP.equals(cf.getUiType())) {
                            Integer selectedId = companyCustomFieldItem.getFieldStringValue() != null && !StringUtils.isEmpty(companyCustomFieldItem.getFieldStringValue().trim())
                                    ? Integer.parseInt(companyCustomFieldItem.getFieldStringValue()) : null;
                            String name = "";
                            if (selectedId != null) {
                                for (SelectItem selectItem : companyCustomFieldItem.getQueryItems()) {
                                    if (selectItem != null && selectItem.getId() != null && selectedId.equals(selectItem.getId())) {
                                        name = selectItem.getName();
                                        break;
                                    }
                                }
                            }
                            cf.setFieldStringValue(name);
                            cf.setSelectedId(selectedId);
                            cf.setFieldDateNonConvertedValue(companyCustomFieldItem.getFieldDateNonConvertedValue());
                            cf.setItem(companyCustomFieldItem.getItem());
                        } else {
                            cf.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                            cf.setSelectedId(companyCustomFieldItem.getSelectedId());
                            cf.setFieldDateNonConvertedValue(companyCustomFieldItem.getFieldDateNonConvertedValue());
                            cf.setItem(companyCustomFieldItem.getItem());
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void saveInvoiceEditCellValue(NewInvoice rowValue, String columnCodeName) {
        EdsSaleInvoice edsSaleInvoice = invoiceManager.getSaleInvoice(rowValue.getID());

        EdsInvoiceCustomFields edsCustomFields = edsSaleInvoice.getCustomFields();
        if (edsCustomFields == null) {
            edsCustomFields = new EdsInvoiceCustomFields();
            invoiceCFManager.create(edsCustomFields);
            edsSaleInvoice.setCustomFields(edsCustomFields);
        }
        CustomFieldsUtils.setDomenObjectFieldChange(edsCustomFields, rowValue.getCustomFieldMap(), columnCodeName);

        try {
            saleInvoiceSolrComponent.index(edsSaleInvoice);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<CompanyCustomFieldItem> getSalesInvoiceCustomFieldsByNumber(String number) {
        List<EdsBaseSaleInvoice> edsBaseSaleInvoiceList = invoiceManager.getSaleInvoiceByNumber(number, null);
        if (edsBaseSaleInvoiceList == null || edsBaseSaleInvoiceList.isEmpty()) {
            return null;
        }
        for (EdsBaseSaleInvoice edsBaseSaleInvoice : edsBaseSaleInvoiceList) {
            if (edsBaseSaleInvoice instanceof EdsSaleInvoice) {
                EdsInvoiceCustomFields customFields = edsBaseSaleInvoice.getCustomFields();
                if (customFields == null) {
                    return null;
                }
                ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.SaleInvoice);
                return (ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(customFields, customFieldsItems);
            }
        }
        return null;
    }

    private NewInvoice getMultiQuoteData(MultiQuoteConvertItem item, String type) {
        Date startTime = new Date();
        NewInvoice result = new NewInvoice();
        List<NewInvoiceItem> items = new ArrayList<>();
        result.setID(null);
        EdsQuote quote = quoteManager.get(item.getSelectedProjectQuoteId() != null ? item.getSelectedProjectQuoteId() : item.getQuoteIds().get(0));
        EdsCrmAccount client = quote.getClientOrSupplier();
        result.setClientID(client.getObjectID());
        result.setClientName(client.getName());

        result.setTypeItem(getClientOrSupplier(client.getObjectID(), type));
        result.setBillAddressID(quote.getBillAddressID());
        result.setMailAddressID(quote.getMailAddressID());

        result.setClientContactID(quote.getClientContact() != null ? quote.getClientContact().getObjectID() : null);
        result.setClientContactEmail(quote.getClientContact() != null ? quote.getClientContact().getPrimaryEmail() : "");
        result.setCurrencyID(quote.getCurrency().getObjectID());
        result.setCurrencyName(quote.getCurrency().getName());
        result.setCurrencySymbol(quote.getCurrency().getSymbol());
        result.setPoNumber(quote.getPoNumber());
        result.setInvoiceNumber(quote.getNumber());
        result.setInvoiceDate(new DateNonConvertable(quote.getInvoiceDate()));
        result.setDueDate(new DateNonConvertable(quote.getDueDate()));
        result.setReference(quote.getReference());
        result.setTaxCalculationType(quote.getTaxCalculationType());
        EdsProject project = quote.getRelatedProject();

        if (project != null) {
            result.setRelatedProject(new SelectItem(project.getObjectID(),
                    (project.getNumber() != null && !"".equals(project.getNumber().trim()) ? project.getNumber() + " -> " : "") + project.getName(), project.getNumber()));
        }

        if (quote instanceof EdsSaleQuote) {
            result.setCustomFieldItems(getWrappedSOQCustomFieldsToSI((EdsSaleQuote) quote));
        }
        System.out.println("Before items take time: " + (new Date().getTime() - startTime.getTime()));
        startTime = new Date();
        try {
            items = MultiQuoteConverterUtils.getItems(item.getQuoteIds(), item);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("take time: " + (new Date().getTime() - startTime.getTime()));
        result.setItems(items.toArray(new NewInvoiceItem[]{}));
        result.setConvertedQuoteIDs(item.getQuoteIds());
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewInvoice getAllCreditNoteData(Params fp) {
        NewInvoice creditNoteObject;
        EdsCompany company = invoiceManager.getUser().getCompany();
        if (fp.getObjectID() != null) {
            creditNoteObject = getCreditNote(fp.getObjectID());
            if (creditNoteObject.getClientID() != null) {
                if (RECEIVABLE.equals(fp.getType())) {
                    creditNoteObject.getTypeItem().setSupplierCustomerBalance(crmAccountManager.getClientBalance(creditNoteObject.getClientID()).doubleValue());
                } else {
                    EdsCrmAccount clientBase = crmAccountManager.get(creditNoteObject.getClientID());
//                    if (!clientBase.getBalanceCalculated()) {
                    creditNoteObject.getTypeItem().setSupplierCustomerBalance(crmAccountManager.getSupplierBalance(clientBase.getObjectID()).doubleValue());
//                    } else {
//                        creditNoteObject.getTypeItem().setSupplierCustomerBalance(clientBase.getSupplierBalance().doubleValue());
//                    }
                    creditNoteObject.getTypeItem().setReverseChargeApplicable(clientBase.isReverseChargeApplicable());
                }
            }

        } else {
            if (fp.getExternalFormID() != null && COPY_INVOICE_TO_CREDITNOTE.equals(fp.getExternalFormID())) {
                creditNoteObject = getInvoiceForCreditNote(fp.getExternalObjectID());
            } else if (fp.getExternalFormID() != null && COPY_FROM_EXISTING_DATA.equals(fp.getExternalFormID())) {
                creditNoteObject = getCreditNote(fp.getExternalObjectID());
            } else {
                creditNoteObject = new NewInvoice();
                EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(company);
                if (invoicingSettings != null) {
                    creditNoteObject.setTaxCalculationType(invoicingSettings.getTaxCalculationType());
                }
                ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(RECEIVABLE.equals(fp.getType()) ? ViewName.SaleInvoice : ViewName.PurchaseInvoice);
                creditNoteObject.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(null, customFieldsItems));
            }
            if (RECEIVABLE.equals(fp.getType())) {
                creditNoteObject.setNumberData(getCreditNoteNumber());
                creditNoteObject.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_CREDIT_NOTE));
            } else {
                creditNoteObject.setNumberData(invoiceCircularResolver.getPurchaseInvoiceNumberData(true, true));
                EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);
                creditNoteObject.setPurchaseInvoiceNumberingShow(invSettings != null && invSettings.getIsPurchaseInvoiceNumberingShow());
                creditNoteObject.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_DEBIT_NOTE));
            }
        }
        creditNoteObject.setDueDays(getNumberForDueDate());
        creditNoteObject.setLayoutHTML(PathFinder.getLayoutHTML(CREDIT_NOTE));
        creditNoteObject.setRoundingModeDisabled(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ROUNDING_MODE_DISABLED));
        creditNoteObject.setDoubleTaxEnabled(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DOUBLE_TAX_ENABLED));
        creditNoteObject.setCustomItemColumns(itemTableSettingService.getColumnConfigs(RECEIVABLE.equals(fp.getType()) ? ItemTableEnum.CREDIT_NOTE_ITEM : ItemTableEnum.DEBIT_NOTE_ITEM));
        creditNoteObject.setDefaultTaxItem(getDefaultTaxItem());
//        creditNoteObject.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_CREDIT_NOTE));

        if (fp.getObjectID() != null && creditNoteObject.getItems() != null) {
            if (RECEIVABLE.equals(fp.getType())) {
                for (NewInvoiceItem item : creditNoteObject.getItems()) {
                    if (item.getTrackBatchesEnabled()) {
                        item.setBatchItems(itemBatchService.getBatchItems(item.getID(), item.getItemID(), creditNoteObject.getID(), ItemSerialEntityType.CREDIT_NOTE.name()));
                    }
                }
            } else if (PAYABLE.equals(fp.getType())) {
                for (NewInvoiceItem item : creditNoteObject.getItems()) {
                    if (item.getTrackBatchesEnabled()) {
                        item.setBatchItems(itemBatchService.getBatchItems(item.getID(), item.getItemID(), creditNoteObject.getID(), ItemSerialEntityType.DEBIT_NOTE.name()));
                    }
                }
            }
        }
        ArrayList<CompanyCustomFieldItem> itemCustomFields = commonService.getCompanyAllCustomFields(RECEIVABLE.equals(fp.getType()) ? ViewName.SaleInvoiceItem : ViewName.PurchaseInvoiceItem);
        creditNoteObject.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));

        if (RECEIVABLE.equals(fp.getType())) {
            creditNoteObject.setPdfTemplateList(getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.RECEIVABLE_CREDIT_NOTE.name()));
        } else {
            creditNoteObject.setPdfTemplateList(getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PAYABLE_CREDIT_NOTE.name()));
        }
        return creditNoteObject;
    }

    public void saveBatchInvoiceData(NewInvoice[] invoices) {
        EdsUser user = invoiceManager.getUser();
        for (NewInvoice invData : invoices) {
            try {
                if (invData.getNumberData() == null) {
                    invData.setNumberData(getSaleInvoiceNumber());
                    invData.setInvoiceNumber(invData.getNumberData().getInvoiceNumber());
                }
                invData.setForceSave(true);
                Integer invoiceID = saveSaleInvoice(invData).getId();
                EdsSaleInvoice invoice = (EdsSaleInvoice) invoiceManager.get(invoiceID);
                log.info("Batch Invoice Saved INVOICE_ID:" + invoiceID + " STATUS:" + invData.getStatus());
                if (OPEN.equals(invData.getStatusCode())) {
                    baseEventPostProcessor.registerEvent(SaleInvoiceCustomEventListenerImpl.TYPE,
                            SaleInvoiceCustomEventListenerImpl.EVENT_SALEINVOICE_SEND_MESSAGE_TO_CLIENT, invoice, user);
                    log.info("Batch Invoice Message Registered. INVOICE_ID:" + invoiceID);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.info("Error occured while saving invoice: CLIENT_NAME:" + invData.getClientID());
            }
        }
    }

    public void deleteAttachment(Integer bodyID) {
        EdsFileBody body = (EdsFileBody) uploadManager.get(bodyID);
        EdsFileHeader fileHeader = body.getHeader();
        fileHeader.setDeleted(true);
        fileHeader.getAuditInfo().setModificationDate(new Date());
        fileHeader.getAuditInfo().setModifiedBy(userManager.getUser());
        try {
            /*solrManager.addFileToIndex(body.getHeader());*/
            folderSolrComponent.index(body.getHeader());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PdfTemplateItemList getCompanyPdfTemplates(String type) {
        if (SALE_INVOICE.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.SALES_INVOICE.name());
        } else if (PROJECT_BASED_INVOICE.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PROJECT_BASED_INVOICE.name());
        } else if (PURCHASE_INVOICE.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PURCHASE_INVOICE.name());
        } else if (RECEIVABLE_CREDIT_NOTE.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.RECEIVABLE_CREDIT_NOTE.name());
        } else if (PAYABLE_CREDIT_NOTE.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PAYABLE_CREDIT_NOTE.name());
        } else if (SALE_QUOTE.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.SALES_QUOTE.name());
        } else if (SALE_ORDER.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.SALES_ORDER.name());
        } else if (PURCHASE_ORDER.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PURCHASE_ORDER.name());
        } else if (PACKING_SLIP.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PACKING_SLIP.name());
        } else if (SO_PACKING_SLIP.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.SO_PACKING_SLIP.name());
        } else if (RECEIVE_MONEY_STR.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.BANK_RECEIPT.name());
        } else if (SPEND_MONEY_STR.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.BANK_PAYMENT.name());
        } else if (CASH_RECEIPT_STR.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.CASH_RECEIPT.name());
        } else if (CASH_PAYMENT_STR.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.CASH_PAYMENT.name());
        } else if (MANUAL_ENTRY.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.MANUAL_ENTRY.name());
        } else if (BANK_CHECK.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.BANK_CHECK.name());
        } else if (BATCH_RECEIVE_PAYMENT.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.BATCH_RECEIVE_PAYMENT.name());
        } else if (BATCH_PAY_BILL.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.BATCH_PAY_BILL.name());
        } else if (GROUP_PAYRUN.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.GROUP_PAYRUN.name());
        } else if (PREPAYMENT.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PREPAYMENT.name());
        } else if (SUPPLIER_CREDIT.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.SUPPLIER_CREDIT.name());
        } else if (RFQ.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.RFQ.name());
        } else if (RFP.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.RFP.name());
        } else if (AGED_RECEIVABLE.equals(type) || AGED_PAYABLE.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.AGING_SUMMARY.name());
        } else if (AccountingConstants.LEAVE_REQUEST.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.LEAVE_REQUEST.name());
        } else if (GOODS_DELIVERED_NOTES.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.GOODS_DELIVERED_NOTES.name());
        } else if (GOODS_RECEIVED_NOTES.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.GOODS_RECEIVED_NOTES.name());
        } else if (STOCK_TRANSFER.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.STOCK_TRANSFER.name());
        } else if (STOCK_ADJUSTMENT.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.STOCK_ADJUSTMENT.name());
        } else if (PDFConstants.VACANCY.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.VACANCY.name());
        } else if (CUSTOMER_SUPPLIER_BALANCE.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.CUSTOMER_SUPPLIER_BALANCE.name());
        } else if (PICK_LIST_VIEW.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PICK_LIST_VIEW.name());
        } else if (DASHBOARD_CHARTS.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.DASHBOARD_CHARTS.name());
        } else if (EMPLOYEE_PROFILE.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.EMPLOYEE_PROFILE.name());
        } else if (RENTAL_PRODUCT.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.RENTAL_PRODUCT.name());
        } else if (RENTAL_ORDER.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.RENTAL_ORDER.name());
        } else if (PayrollContants.PLACEMENT.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PLACEMENT.name());
        } else if (POSITION1.equals(type)) {
            return getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.POSITION.name());
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ReceivePaymentData getReceivePaymentData(ListingFilterParameter fp, FindMatchFilterData filterData) {
        ArrayList<PaymentData> dataList = new ArrayList<>();

        Integer crmAccountID = fp.getCrmAccountId();
        Integer currencyID = fp.getCurrencyID();
        filterData.setProjectID(fp.getProjectId());

        ReceivePaymentData paymentData = new ReceivePaymentData();
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(crmAccountID);

        if (currencyID == null && edsCrmAccount != null) {
            paymentData.setCurrency(edsCrmAccount.getCurrency() != null ? edsCrmAccount.getCurrency().createCurrencyItem() : paymentData.getEnabledCurrencies()[0]);
            currencyID = paymentData.getCurrency().getId();
        }
        fp.setCurrencyID(currencyID);

        if (filterData.getReceivablePayableID() != null) {
            EdsAccount edsAccount = accountingManager.get(filterData.getReceivablePayableID());
            filterData.setParentReceivablePayable(edsAccount.getKey() != null);
        }
        fp.getOptions().forEach(code -> {
            if (AccountingConstants.PAYMENT_TARGET_INVOICE.equals(code) && crmAccountID != null) {
                dataList.addAll(loadInvoicePayments(fp, filterData));
            } else if (AccountingConstants.PAYMENT_TARGET_MANUAL_JOURNAL.equals(code) && crmAccountID != null) {
                dataList.addAll(loadManualEntryPaymentData(fp, filterData));
            } else if (AccountingConstants.PAYMENT_TARGET_EXPENSE.equals(code)) {
                dataList.addAll(loadExpensePaymentData(fp, filterData));
            }
        });

        if (filterData.getSortField() != null) {
            boolean isAscending = ASC_STR.equals(filterData.getSortDirection());
            dataList.sort((o1, o2) -> {
                if (AccountingConstants.DESCRIPTION_COLUMN.equals(filterData.getSortField())) {
                    if (o1.getInvoiceNumber() == null || o2.getInvoiceNumber() == null)
                        return 0;
                    return isAscending ? (o2.getInvoiceNumber().compareTo(o1.getInvoiceNumber())) : (o1.getInvoiceNumber().compareTo(o2.getInvoiceNumber()));
                }

                if (AccountingConstants.DATE_COLUMN.equals(filterData.getSortField())) {
                    if (o1.getInvoiceDate() == null || o2.getInvoiceDate() == null)
                        return 0;
                    return isAscending ? (o2.getInvoiceDate().getNonConvertedDate().compareTo(o1.getInvoiceDate().getNonConvertedDate())) : (o1.getInvoiceDate().getNonConvertedDate().compareTo(o2.getInvoiceDate().getNonConvertedDate()));
                }
                if (AccountingConstants.AMOUNT_COLUMN.equals(filterData.getSortField())) {
                    if (o1.getTotal() == null || o2.getTotal() == null)
                        return 0;
                    return isAscending ? (o2.getTotal().compareTo(o1.getTotal())) : (o1.getTotal().compareTo(o2.getTotal()));
                }

                return 0;

            });
        }

        paymentData.setPayments(dataList.toArray(new PaymentData[0]));
        return paymentData;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ReceivePaymentData getPaymentRefund(ListingFilterParameter filterParameter) {
        ArrayList<PaymentData> dataList = new ArrayList<>();

        ReceivePaymentData paymentData = new ReceivePaymentData();

        paymentData.setEnabledCurrencies(currencyService.getCurrencies());
        if (filterParameter.getObjectId() == null) {
            paymentData.setNumberData(generatePaymentRefundNumber(filterParameter.isReceivable() ? Constants.RECEIVABLE : PAYABLE));

            if (filterParameter.getAccountID() != null) {
                EdsAccount account = accountingManager.get(filterParameter.getAccountID());
                if (account != null) {
                    paymentData.setAccount(account.getAsSelectItem());
                }
            }
            if (filterParameter.getEntityID() != null) {
                EdsInvoicePayment prepayment = invoicePaymentManager.get(filterParameter.getEntityID());
                if (prepayment != null) {
                    if (prepayment.getCurrencyID() != null) {
                        paymentData.setCurrency(currencyService.getCurrency(prepayment.getCurrencyID()));
                    }
                    if (prepayment.getAccount() != null && !prepayment.getCurrencyID().equals(prepayment.getAccount().getCurrency().getObjectID())) {
                        paymentData.setBankAccountCurrency(prepayment.getAccount().getCurrency().createCurrencyItem());
                    }
                    if (prepayment.getCrmAccount() != null) {
                        paymentData.setCrmAccount(prepayment.getCrmAccount().getAsSelectItem());
                    }
                    if (prepayment.getAccount() != null) {
                        paymentData.setAccount(prepayment.getAccount().getAsSelectItem());
                    }
                    if (prepayment.getPaymentDate() != null) {
                        paymentData.setDate(new DateNonConvertable(prepayment.getPaymentDate()));
                    }
                    if (prepayment.getReference() != null) {
                        paymentData.setReference(prepayment.getReference());
                    }
                }
            }
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalRefundAmount = BigDecimal.ZERO;
        EdsPaymentRefund edsPaymentRefund = paymentRefundManager.get(filterParameter.getObjectId());
        if (edsPaymentRefund != null) {
            paymentData.setNumber(edsPaymentRefund.getNumber());
            paymentData.setReference(edsPaymentRefund.getReference());
            paymentData.setExRate(edsPaymentRefund.getExchangeRate() != null ? edsPaymentRefund.getExchangeRate() : BigDecimal.ONE);
            if (edsPaymentRefund.getPaymentDate() != null) {
                paymentData.setDate(new DateNonConvertable(edsPaymentRefund.getPaymentDate()));
            }
            if (edsPaymentRefund.getCrmAccount() != null) {
                paymentData.setCrmAccount(edsPaymentRefund.getCrmAccount().getAsSelectItem());
            }
            if (edsPaymentRefund.getAccount() != null) {
                paymentData.setAccount(edsPaymentRefund.getAccount().getAsSelectItem());
            }
            if (edsPaymentRefund.getCurrencyID() != null) {
                paymentData.setCurrency(currencyService.getCurrency(edsPaymentRefund.getCurrencyID()));
            }
            if (edsPaymentRefund.getPaymentTarget() != null) {
                paymentData.setPaymentTarget(edsPaymentRefund.getPaymentTarget());
            }


            List<EdsInvoicePayment> refundPayments = invoicePaymentManager.getRefundItems(edsPaymentRefund.getObjectID());

            BigDecimal totalCloseAmount = BigDecimal.ZERO;
            for (EdsInvoicePayment payment : refundPayments) {
                PaymentData pData = new PaymentData();
                EdsInvoicePayment invoicePayment = payment.getAppliedPayment();

                if (invoicePayment != null) {
                    pData.setInvoiceID(invoicePayment.getObjectID());
                    if (invoicePayment.getCurrencyID() != null) {
                        pData.setCurrency(currencyService.getCurrency(invoicePayment.getCurrencyID()));
                    }
                    pData.setExchangeRate(invoicePayment.getExchangeRate());
                    pData.setInvoiceNumber(invoicePayment.getNumber());
                    pData.setInvoiceDate(new DateNonConvertable(invoicePayment.getPaymentDate()));
                    totalAmount = totalAmount.add(invoicePayment.getAmountInInvoiceCurrency() != null ? invoicePayment.getAmountInInvoiceCurrency() : invoicePayment.getAmount());
                    pData.setTotal(invoicePayment.getAmountInInvoiceCurrency() != null ? invoicePayment.getAmountInInvoiceCurrency() : invoicePayment.getAmount());
                    pData.setType(filterParameter.isReceivable() ? AccountingConstants.RECEIVABLE_PREPAYMENT_REFUND : AccountingConstants.PAYABLE_PREPAYMENT_REFUND);

                    if (payment.getClosedAmount() != null) {
                        pData.setClosedAmount(payment.getClosedAmount());
                        totalCloseAmount = totalCloseAmount.add(payment.getClosedAmount());
                    }

                    BigDecimal amount = payment.getAmountInInvoiceCurrency() != null ? payment.getAmountInInvoiceCurrency() : payment.getAmount();
                    if (amount != null) {
                        pData.setPaymentAmount(amount);
                        totalRefundAmount = totalRefundAmount.add(amount);
                    }
                }
                dataList.add(pData);
            }
            if (refundPayments != null && refundPayments.size() > 0) {
                if (refundPayments.size() == 1) {
                    EdsInvoicePayment invoicePayment = invoicePaymentManager.get(refundPayments.get(0).getObjectID());
                    if (invoicePayment != null) {
                        EdsInvoicePaymentTransaction transaction = transactionManager.getTransactionByPayment(invoicePayment);
                        if (transaction != null) {
                            paymentData.setJournalID(transaction.getJournalId());
                        }
                    }
                } else if (refundPayments.size() > 1) {
                    paymentData.setHasMultiTransaction(true);
                }
            }
            if (totalCloseAmount != null && totalCloseAmount.compareTo(BigDecimal.ZERO) > 0) {
                EdsPaymentRefundTransaction paymentRefundTransaction = transactionManager.getTransactionByRefund(edsPaymentRefund);
                if (paymentRefundTransaction != null && paymentRefundTransaction.getTransactionItems() != null) {
                    for (EdsTransactionItem transactionItem : paymentRefundTransaction.getTransactionItems()) {
                        if (RECEIVABLE.equals(edsPaymentRefund.getType()) && transactionItem.getCredit() != null) {
                            paymentData.setCloseAccount(transactionItem.getAccount().createAccountItem());
                            break;
                        } else if (PAYABLE.equals(edsPaymentRefund.getType()) && transactionItem.getDebit() != null) {
                            paymentData.setCloseAccount(transactionItem.getAccount().createAccountItem());
                            break;
                        }
                    }

                }
                paymentData.setCloseAmount(totalCloseAmount);
            }

        }

        paymentData.setPayments(dataList.toArray(new PaymentData[]{}));
        paymentData.setTotalAmount(totalAmount);
        paymentData.setTotalRefundAmount(totalRefundAmount);
        return paymentData;
    }

    @Override
    public void deleteRefundPayment(Integer objectID) {
        EdsPaymentRefund edsPaymentRefund = paymentRefundManager.get(objectID);

        List<EdsInvoicePayment> edsInvoicePaymentList = invoicePaymentManager.getRefundItems(edsPaymentRefund.getObjectID());
        for (EdsInvoicePayment edsInvoicePayment : edsInvoicePaymentList) {

            EdsInvoicePayment invoicePayment = invoicePaymentManager.get(edsInvoicePayment.getObjectID());
            deletePayment(invoicePayment.getObjectID());
        }
        transactionManager.deleteTransactionByRefund(edsPaymentRefund);
        edsPaymentRefund.setDeleted(true);
        paymentRefundManager.update(edsPaymentRefund);
    }

    @Override
    public void deleteSelectedInvoices(final ArrayList<Integer> idArray, String type) {
        for (final Integer objectID : idArray) {
            this.deleteInvoice(objectID, type);
        }
    }

    @Override
    public InvoiceNumberData generateNewNumberData(String type, DateNonConvertable invoiceDate) {
        if (Constants.SALE_INVOICE.equals(type)) {
            return getSaleInvoiceNumber(invoiceDate);
        }
        if (Constants.PURCHASE_INVOICE.equals(type)) {
            return invoiceCircularResolver.getPurchaseInvoiceNumberData(false, true, invoiceDate);
        } else if (Constants.CREDIT_NOTE.equals(type)) {
            return getCreditNoteNumber(invoiceDate);
        } else if ("DEBIT_NOTE".equals(type)) {
            return invoiceCircularResolver.getPurchaseInvoiceNumberData(true, true, invoiceDate);
        }
        return this.invoiceCircularResolver.getQuoteOrderNumberData(type, invoiceDate);
    }

    @Override
    public void sendInvoiceToZatca(Integer invoiceId, String xmlType) throws Exception {
        if (ServerUtils.isNullOrEmpty(xmlType)) {
            throw new Exception("Xml type can not be null or empty");
        }
        try {
            String test = zatcaService.clearanceInvoice(invoiceId, xmlType);
            System.out.println(test + " ============================== Zatca Response ================================================== ");
            try {
                solrManager.indexAddSaleInvoice(invoiceManager.getSaleInvoice(invoiceId));
                System.out.println(" ============================== Invoice add to solr after sending to Zatca ================================================== ");
            } catch (IOException | SolrServerException e) {
                e.printStackTrace();
            }
        } catch (ZatcaException e) {
            e.printStackTrace();
            throw new Exception("something is wrong ");
        }
    }

    @Override
    public void saveItemTableDefaultDiscount(Integer discountId, CustomFieldSection section) {
        EdsCompany company = accountingManager.getUser().getCompany();
        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(company);

        if (CustomFieldSection.SaleQuoteItem.equals(section)) {
            invoicingSettings.setDefDiscountSQ(discountId);
        } else if (CustomFieldSection.SaleOrderItem.equals(section)) {
            invoicingSettings.setDefDiscountSO(discountId);
        } else if (CustomFieldSection.SaleInvoiceItem.equals(section)) {
            invoicingSettings.setDefDiscountSI(discountId);
        } else if (CustomFieldSection.PurchaseOrderItem.equals(section)) {
            invoicingSettings.setDefDiscountPO(discountId);
        } else if (CustomFieldSection.PurchaseInvoiceItem.equals(section)) {
            invoicingSettings.setDefDiscountPI(discountId);
        }
    }


    @Override
    public SelectItem getItemTableDefaultDiscount(CustomFieldSection section) {
        EdsCompany company = accountingManager.getUser().getCompany();
        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(company);
        SelectItem item = new SelectItem();
        if (invoicingSettings != null && section != null) {
            if (CustomFieldSection.SaleQuoteItem.equals(section) && invoicingSettings.getDefDiscountSQ() != null) {
                item.setId(invoicingSettings.getDefDiscountSQ());
                item.setName(getDiscountName(invoicingSettings.getDefDiscountSQ()));
            } else if (CustomFieldSection.SaleOrderItem.equals(section) && invoicingSettings.getDefDiscountSO() != null) {
                item.setId(invoicingSettings.getDefDiscountSO());
                item.setName(getDiscountName(invoicingSettings.getDefDiscountSO()));
            } else if (CustomFieldSection.SaleInvoiceItem.equals(section) && invoicingSettings.getDefDiscountSI() != null) {
                item.setId(invoicingSettings.getDefDiscountSI());
                item.setName(getDiscountName(invoicingSettings.getDefDiscountSI()));
            } else if (CustomFieldSection.PurchaseOrderItem.equals(section) && invoicingSettings.getDefDiscountPO() != null) {
                item.setId(invoicingSettings.getDefDiscountPO());
                item.setName(getDiscountName(invoicingSettings.getDefDiscountPO()));
            } else if (CustomFieldSection.PurchaseInvoiceItem.equals(section) && invoicingSettings.getDefDiscountPI() != null) {
                item.setId(invoicingSettings.getDefDiscountPI());
                item.setName(getDiscountName(invoicingSettings.getDefDiscountPI()));
            }
        }
        return item;
    }

    private String getDiscountName(Integer id) {
        String name = null;
        if (id == null || id == 0) {
            name = "Percentage";
        } else if (id == 1) {
            name = "Fixed Amount";
        } else {
            name = discountManager.get(id).getName();
        }
        return name;
    }

    @Transactional
    public BankTransferNumberData generatePaymentRefundNumber(String type) {
        BankTransferNumberData numberData;
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = null;
        if (type != null) {
            intNumber = paymentRefundManager.getLastIntNumberByType(type);
        }
        intNumber = (intNumber == null) ? 0 : intNumber;
        String numberingFormat = null;
        if (type.contains("RECEIVABLE")) {
            if (settings != null && settings.getCrfNumberingFormat() != null) {
                numberingFormat = settings.getCrfNumberingFormat();
                type = "CRF";
            } else {
                type = "CRF";
                numberingFormat = "CRF_0001";
            }
        } else if (type.contains("PAYABLE")) {
            if (settings != null && settings.getSrfNumberingFormat() != null) {
                numberingFormat = settings.getSrfNumberingFormat();
                type = "SRF";
            } else {
                numberingFormat = "SRF_0001";
                type = "SRF";
            }
        }
        numberData = paymentRefundNumberDataSettings(numberingFormat, intNumber, type);
        return numberData;
    }

    private BankTransferNumberData paymentRefundNumberDataSettings(String numberingFormat, Integer
            fourDigitNumber, String defaultPrefix) {
        BankTransferNumberData bankTransferNumberData = new BankTransferNumberData();

        if (numberingFormat != null) {
            parsePaymentRefundNumber(numberingFormat, bankTransferNumberData, fourDigitNumber);
        } else {
            NumberData numberData = EdsNumberingSettings.getDefaultData(fourDigitNumber, defaultPrefix);
            String[] numberParts = numberData.getNumberFormat().split("_");
            bankTransferNumberData.setPrefix(numberParts[0]);
            bankTransferNumberData.setFourDigitNumber(String.valueOf(numberParts[1]));
            bankTransferNumberData.setWithDate(numberParts[1].split("-").length == 2);
        }
        return bankTransferNumberData;
    }

    private void parsePaymentRefundNumber(String numberFormat, BankTransferNumberData numberData, Integer
            fourDigitNumber) {
        String[] mainPartNumbers = numberFormat.split("_");  // e.g CP_0001-05/2015 or CR_0001-05/2015
        String[] datePartNumbers = mainPartNumbers[1].split("-");  // e.g 0001-05/2015 or 0001-05/2015

        numberData.setPrefix(mainPartNumbers[0]);
        numberData.setWithDate(datePartNumbers.length == 2);

        String lastFourNumber = datePartNumbers[0];

        DecimalFormat format = new DecimalFormat("0000");
        numberData.setFourDigitNumber(fourDigitNumber != null ? format.format(fourDigitNumber + 1) : lastFourNumber);
        if (numberData.isWithDate()) {
            numberData.setDate(ServerUtils.getBankTransferDateNumber(new Date()));
        }
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ReceivePaymentData getPaymentRefundItemData(ListingFilterParameter fp) {
        ArrayList<PaymentData> dataList = new ArrayList<>();

        Integer crmAccountID = fp.getCrmAccountId();
        Integer currencyID = fp.getCurrencyID();

        ReceivePaymentData paymentData = new ReceivePaymentData();
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(crmAccountID);

        if (currencyID == null && edsCrmAccount != null) {
            paymentData.setCurrency(edsCrmAccount.getCurrency() != null ? edsCrmAccount.getCurrency().createCurrencyItem() : paymentData.getEnabledCurrencies()[0]);
            currencyID = paymentData.getCurrency().getId();
        }
        fp.setCurrencyID(currencyID);


        if (fp.getOptions() != null) {
            fp.getOptions().forEach(code -> {
                if (AccountingConstants.PREPAYMENT.equals(code) && crmAccountID != null) {
                    dataList.addAll(loadOpenPrepayments(fp));
                } else if (Constants.CREDIT_NOTE.equals(code) && crmAccountID != null) {
                    dataList.addAll(loadOpenCreditNotes(fp));
                }
            });
        }

        paymentData.setPayments(dataList.toArray(new PaymentData[0]));
        return paymentData;
    }


    private List<PaymentData> loadOpenCreditNotes(ListingFilterParameter fp) {
        ArrayList<PaymentData> result = new ArrayList<>();
        Integer crmAccountID = fp.getCrmAccountId();
        Integer currencyID = fp.getCurrencyID();
        boolean isCustomer = fp.isReceivable();
        if (crmAccountID != null && currencyID != null) {
            List<EdsInvoice> creditNotes = invoiceManager.getCreditOrDebitNotes(fp);
            if (creditNotes != null && creditNotes.size() > 0) {
                for (EdsInvoice creditNote : creditNotes) {

                    BigDecimal paidAmount = creditNote.getFullPayments();
                    BigDecimal totalInCurrency = creditNote.getTotalInInvoiceCurrency();
                    if (totalInCurrency != null && (paidAmount == null || (paidAmount != null && totalInCurrency.subtract(paidAmount).compareTo(BigDecimal.ZERO) > 0))) {
                        PaymentData pData = new PaymentData();
                        pData.setInvoiceID(creditNote.getObjectID());
                        if (creditNote.getCurrency() != null) {
                            pData.setCurrency(creditNote.getCurrency().createCurrencyItem());
                        }
                        pData.setExchangeRate(creditNote.getExchangeRate());
                        pData.setInvoiceNumber(creditNote.getNumber());
                        pData.setInvoiceDate(new DateNonConvertable(creditNote.getInvoiceDate()));
                        pData.setType(isCustomer ? RECEIVABLE_CREDIT_REFUND : PAYABLE_DEBIT_REFUND);
                        pData.setTotal(totalInCurrency);
                        if (paidAmount != null) {
                            pData.setRemainingAmount(totalInCurrency.subtract(paidAmount));
                        } else {
                            pData.setRemainingAmount(totalInCurrency);
                        }
                        result.add(pData);
                    }
                }
            }
        }

        return result;
    }

    private List<PaymentData> loadExpensePaymentData(ListingFilterParameter fp, FindMatchFilterData filter) {
        List<PaymentData> result = new ArrayList<>();
        Integer crmAccountID = fp.getCrmAccountId();
        Integer currencyID = fp.getCurrencyID();
        CurrencyItem baseCurrency = getBaseCurrency();
        boolean isMultiCurrencyEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTICURRENCY_ENABLED);
        List<EdsExpenseReport> expenseReports = expenseReportManager.getNotFullyPaidExpenses(crmAccountID, currencyID, isMultiCurrencyEnabled);
        EdsAccount unpaidExpenseClaims = accountingManager.getAccountByKey(EdsAccount.UNPAID_EXPENSE_CLAIMS);
        EdsAccount accountsPayable = accountingManager.getAccountByKey(EdsAccount.ACCOUNTS_PAYABLE);

        for (EdsExpenseReport expenseReport : expenseReports) {
            BigDecimal paidTotal = BigDecimal.ZERO;
            BigDecimal paidTotalBase = BigDecimal.ZERO;
            List<EdsExpensePayment> payments = expensePaymentManager.getPayments(expenseReport);
            for (EdsExpensePayment payment : payments) {
                paidTotal = paidTotal.add(payment.getAmount());
                paidTotalBase = paidTotalBase.add(payment.getAmountInEntityCurrency() != null ? payment.getAmountInEntityCurrency() : BigDecimal.ZERO);
            }
            BigDecimal dueAmount = expenseReport.getTotal().subtract(paidTotalBase);
            BigDecimal dueAmountBase = expenseReport.getBaseTotal().subtract(paidTotal);
            PaymentData pData = new PaymentData();
            pData.setInvoiceID(expenseReport.getObjectID());
            pData.setExpensePayment(true);
            pData.setCurrency(expenseReport.getCurrency().getAsSelectItem());
            pData.setExchangeRate(expenseReport.getExchangeRate());
            pData.setInvoiceNumber(expenseReport.getNumber());
            DateNonConvertable dateNonConvertable = new DateNonConvertable(expenseReport.getStartDate());
            pData.setInvoiceDate(dateNonConvertable);
            pData.setTotal(dueAmount);
            pData.setPaymentDiffCurrency(!currencyID.equals(expenseReport.getCurrency().getObjectID()));
            pData.setTotalInInvoiceCurrency(dueAmount);
            if (expenseReport.getSupplier() != null) {
                pData.setCrmAccount(expenseReport.getSupplier().getAsSelectItem());
            }

            if (isMultiCurrencyEnabled && pData.isPaymentDiffCurrency()) {
                if (baseCurrency.getId().equals(currencyID)) {
                    pData.setTotal(dueAmountBase);
                } else if (fp.getExchangeRate() != null) {
                    pData.setTotal(dueAmount.multiply(fp.getExchangeRate()).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                }
            }
            pData.setAccountItem(expenseReport.getSupplier() == null ? unpaidExpenseClaims.getAsSelectItem() : accountsPayable.getAsSelectItem());

            result.add(pData);
        }
        return result;
    }


    private List<PaymentData> loadManualEntryPaymentData(ListingFilterParameter fp, FindMatchFilterData filterData) {
        List<PaymentData> result = new ArrayList<>();
        PaymentData pData;
        Integer crmAccountID = fp.getCrmAccountId();
        Integer currencyID = fp.getCurrencyID();
        boolean isCustomer = fp.isReceivable();
        boolean isMultiCurrencyEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTICURRENCY_ENABLED);
        CurrencyItem baseCurrency = getBaseCurrency();
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(crmAccountID);
        ArrayList<Integer> IdList = new ArrayList<>();
        HashMap<Integer, Boolean> hasOpeningBalanceMap = new HashMap<>();
        if (fp.isShowSubAccountTransaction()) {
            buildChildIds(edsCrmAccount, IdList, hasOpeningBalanceMap, isCustomer);
        } else {
            IdList.add(crmAccountID);
        }
        filterData.setMultiEnabled(isMultiCurrencyEnabled);
        filterData.setPaymentDiffCurrency(!baseCurrency.getId().equals(currencyID));
        filterData.setBaseCurrencyID(baseCurrency.getId());
        List<TransactionAllocateItem> manualTransactions = manualJournalManager.getManualTransactionsByCrmAccount(IdList, isCustomer, false, null, filterData, currencyID);

        for (TransactionAllocateItem item : manualTransactions) {
            BigDecimal paidAmount = customerSupplierPaymentManager.getManualPaymentsAmount(item.getObjectID(), crmAccountID, isCustomer, item.getAccountID());
            BigDecimal balance = item.getAmount().subtract(paidAmount);

            if (balance.compareTo(ZERO) > 0) {
                EdsManualJournal manualJournal = manualJournalManager.get(item.getObjectID());

                pData = new PaymentData();
                pData.setInvoiceID(item.getObjectID());
                pData.setManualJournal(true);
                pData.setCurrency(manualJournal.getCurrency().getAsSelectItem());
                pData.setExchangeRate(manualJournal.getExchangeRate());
                pData.setInvoiceNumber((item.getNumber() != null ? item.getNumber() + (item.getNarration() != null ? ":" : "") : "") + (item.getNarration() != null ? item.getNarration() : ""));
                DateNonConvertable dateNonConvertable = new DateNonConvertable(item.getDate());
                pData.setInvoiceDate(dateNonConvertable);
                pData.setTotal(balance);
                pData.setPaymentDiffCurrency(!currencyID.equals(manualJournal.getCurrency().getObjectID()));
                pData.setTotalInInvoiceCurrency(balance);
                pData.setCrmAccount(new SelectItem(item.getCrmAccountID(), item.getCrmAccountName()));

                if (isMultiCurrencyEnabled && !currencyID.equals(pData.getCurrency().getId())) {
                    if (baseCurrency.getId().equals(currencyID)) {
                        BigDecimal amountInPaymentCurrency = pData.getTotalInInvoiceCurrency().divide(pData.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                        pData.setTotal(amountInPaymentCurrency);
                    } else if (fp.getExchangeRate() != null) {
                        BigDecimal amountInPaymentCurrency = pData.getTotalInInvoiceCurrency().multiply(fp.getExchangeRate()).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                        pData.setTotal(amountInPaymentCurrency);
                    }
                }

                if (item.getAccountID() != null) {
                    pData.setAccountItem(new SelectItem(item.getAccountID()));
                }

                result.add(pData);
            }
        }
        return result;
    }

    private List<PaymentData> loadInvoicePayments(ListingFilterParameter fp, FindMatchFilterData filterData) {
        ArrayList<PaymentData> result = new ArrayList<>();
        Integer crmAccountID = fp.getCrmAccountId();
        Integer currencyID = fp.getCurrencyID();
        boolean isCustomer = fp.isReceivable();
        boolean isMultiCurrencyEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTICURRENCY_ENABLED);
        CurrencyItem baseCurrency = getBaseCurrency();
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(crmAccountID);
        ArrayList<Integer> IdList = new ArrayList<>();
        HashMap<Integer, Boolean> hasOpeningBalanceMap = new HashMap<>();
        if (fp.isShowSubAccountTransaction()) {
            buildChildIds(edsCrmAccount, IdList, hasOpeningBalanceMap, isCustomer);
        } else {
            IdList.add(crmAccountID);
        }


        PaymentData pData;
        List<String> statuses = new LinkedList<>();
        statuses.add(APPROVE);
        statuses.add(OPEN);
        statuses.add(OVER_DUE);

        //Opening Balance
        EdsCrmAccount clientSupplier = (isCustomer ? clientManager.get(crmAccountID) : crmAccountManager.get(crmAccountID));
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        boolean isOpeningBalanceIncluded;

        if (isCustomer) {
            kpiLog.setEntityName(EdsCustomerSupplierPayment.class.getSimpleName() + " Receive Payment");
            kpiLog.setActionType(KpiLog.ActionType.VIEW);
            kpiLog.setEntityId(clientSupplier.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Get Receive paymanet data");
            isOpeningBalanceIncluded = clientSupplier.getBalanceDate() != null && clientSupplier.getBalanceAmount() != null;
            hasOpeningBalanceMap.put(crmAccountID, isOpeningBalanceIncluded);
        } else {
            kpiLog.setEntityName(EdsCustomerSupplierPayment.class.getSimpleName() + " Payment Bill");
            kpiLog.setActionType(KpiLog.ActionType.VIEW);
            kpiLog.setEntityId(clientSupplier.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Get Payment bill data");
            isOpeningBalanceIncluded = clientSupplier.getSupplierBalanceDate() != null && clientSupplier.getSupplierBalanceAmount() != null;
            hasOpeningBalanceMap.put(crmAccountID, isOpeningBalanceIncluded);
        }

        for (Integer caId : IdList) {
            if (hasOpeningBalanceMap.get(caId) != null && hasOpeningBalanceMap.get(caId)) {
                buildOpeningBalanceDataForPayment(caId, isCustomer, isMultiCurrencyEnabled, currencyID, fp, result);
            }
        }

        //Invoices
        List<EdsInvoice> invoices = invoiceManager.getInvoicesByClientSupplierAndStatuses(IdList, currencyID, isCustomer, statuses, filterData, isMultiCurrencyEnabled);
        invoices.removeIf(invoice -> Constants.APPROVE.equals(invoice.getStatus().getCode()) && invoice.getDueAmount().compareTo(BigDecimal.ZERO) == 0);
        for (EdsInvoice inv : invoices) {
            pData = new PaymentData();
            pData.setInvoiceID(inv.getObjectID());
            pData.setCurrency(inv.getCurrency().getAsSelectItem());
            pData.setExchangeRate(inv.getExchangeRate());
            pData.setInvoiceNumber(inv.getNumber());
            pData.setInvoiceDate(new DateNonConvertable(inv.getInvoiceDate()));
            pData.setTotal(inv.getDueAmount());
            pData.setPaymentDiffCurrency(!currencyID.equals(inv.getCurrency().getObjectID()));
            pData.setTotalInInvoiceCurrency(inv.getDueAmount());
            pData.setCrmAccount(inv.getClientOrSupplier().getAsSelectItem());

            if (isMultiCurrencyEnabled && pData.isPaymentDiffCurrency()) {
                if (baseCurrency.getId().equals(currencyID)) {
                    pData.setTotal(pData.getTotalInInvoiceCurrency().divide(pData.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                } else if (fp.getExchangeRate() != null) {
                    pData.setTotal(pData.getTotalInInvoiceCurrency().multiply(fp.getExchangeRate()).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                }
            }

            //amount boyicha filter uchun
            if (filterData.getStartAmount() != null && filterData.getEndAmount() != null) {
                if (pData.getTotal().compareTo(filterData.getStartAmount()) >= 0 && pData.getTotal().compareTo(filterData.getEndAmount()) <= 0) {
                    result.add(pData);
                }
            } else if (filterData.getStartAmount() != null) {
                if (pData.getTotal().compareTo(filterData.getStartAmount()) >= 0) {
                    result.add(pData);
                }
            } else if (filterData.getEndAmount() != null) {
                if (pData.getTotal().compareTo(filterData.getEndAmount()) <= 0) {
                    result.add(pData);
                }
            } else
                result.add(pData);
        }
        return result;
    }

    private List<PaymentData> loadOpenPrepayments(ListingFilterParameter fp) {
        ArrayList<PaymentData> result = new ArrayList<>();
        Integer crmAccountID = fp.getCrmAccountId();
        Integer currencyID = fp.getCurrencyID();
        boolean isCustomer = fp.isReceivable();
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(crmAccountID);

        StringBuilder statuses = new StringBuilder("'").append(AccountingConstants.PRE_PAYMENT_OPEN_STATUS).append("','").append(AccountingConstants.PRE_PAYMENT_PARTIAL_APPLIED_STATUS).append("'");

        List<EdsInvoicePayment> prePayments = invoicePaymentManager.getOpenPrePayments(currencyID, edsCrmAccount.getObjectID(), isCustomer ? AccountingConstants.RECEIVABLE_PREPAYMENT : AccountingConstants.PAYABLE_SUPPLIER_CREDIT, statuses.toString());
        for (EdsInvoicePayment prepayment : prePayments) {
            PaymentData pData = new PaymentData();
            BigDecimal appliedAmount = invoicePaymentManager.getAppliedPrePaymentAmounts(edsCrmAccount.getObjectID(), prepayment.getObjectID(), RECEIVABLE_PREPAYMENT.equals(prepayment.getType()) ? RECEIVABLE_PREPAYMENT_SHARE : PAYABLE_SUPPLIER_CREDIT_SHARE, RECEIVABLE_PREPAYMENT.equals(prepayment.getType()) ? RECEIVABLE_PREPAYMENT_REFUND : PAYABLE_PREPAYMENT_REFUND);
            pData.setInvoiceID(prepayment.getObjectID());
            if (prepayment.getCurrencyID() != null) {
                pData.setCurrency(currencyService.getCurrency(prepayment.getCurrencyID()));
            }
            pData.setExchangeRate(prepayment.getExchangeRate());
            pData.setInvoiceNumber(prepayment.getNumber());
            pData.setInvoiceDate(new DateNonConvertable(prepayment.getPaymentDate()));
            pData.setType(isCustomer ? AccountingConstants.RECEIVABLE_PREPAYMENT_REFUND : AccountingConstants.PAYABLE_PREPAYMENT_REFUND);
            BigDecimal amount = prepayment.getAmountInInvoiceCurrency() != null ? prepayment.getAmountInInvoiceCurrency() : prepayment.getAmount();
            if (amount != null) {
                pData.setTotal(amount);
                if (appliedAmount != null) {
                    pData.setRemainingAmount(amount.subtract(appliedAmount));
                } else {
                    pData.setRemainingAmount(amount);
                }
            }
            result.add(pData);
        }
        return result;
    }

    private void buildOpeningBalanceDataForPayment(Integer crmAccountID, boolean isCustomer,
                                                   boolean isMultiCurrencyEnabled, Integer currencyID, ListingFilterParameter fp, List<PaymentData> dataList) {
        CurrencyItem baseCurrency = getBaseCurrency();
        BigDecimal paidAmount = ZERO;
        List<EdsCustomerSupplierPayment> payments = customerSupplierPaymentManager.getPayments(crmAccountID, isCustomer);
        for (EdsCustomerSupplierPayment p : payments) {
            if (!p.isDeleted() && p.getManualJournalId() == null) {
                paidAmount = paidAmount.add(p.getAmountInEntityCurrency() != null ? p.getAmountInEntityCurrency() : p.getAmount());
            }
        }
        EdsCrmAccount clientSupplier = (isCustomer ? clientManager.get(crmAccountID) : crmAccountManager.get(crmAccountID));
        BigDecimal balanceAmount = isCustomer ? clientSupplier.getBalanceAmount() : clientSupplier.getSupplierBalanceAmount();
        Date balanceDate = isCustomer ? clientSupplier.getBalanceDate() : clientSupplier.getSupplierBalanceDate();
        BigDecimal balance = balanceAmount.subtract(paidAmount);
        if (balance.compareTo(ZERO) > 0) {
            PaymentData pData = new PaymentData();
            pData.setOpeningBalance(true);
            pData.setInvoiceID(-1);
            pData.setCurrency(baseCurrency);
            pData.setExchangeRate(BigDecimal.ONE);
            pData.setInvoiceNumber("Opening Balance");
            pData.setInvoiceDate(new DateNonConvertable(balanceDate));
            pData.setTotal(balance);
            pData.setPaymentDiffCurrency(!currencyID.equals(pData.getCurrency().getId()));
            pData.setTotalInInvoiceCurrency(balance);
            pData.setCrmAccount(clientSupplier.getAsSelectItem());

            if (isMultiCurrencyEnabled && !currencyID.equals(pData.getCurrency().getId())) {
                if (baseCurrency.getId().equals(currencyID)) {
                    pData.setTotal(pData.getTotalInInvoiceCurrency().divide(pData.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                } else if (fp.getExchangeRate() != null) {
                    pData.setTotal(pData.getTotalInInvoiceCurrency().multiply(fp.getExchangeRate()).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                }
            }

            dataList.add(pData);
        }
    }


    private CurrencyItem[] getCrmAccountEnabledCurrencies(Integer crmAccountID, boolean isCustomer) {
        EdsCrmAccount crmAccount = crmAccountManager.get(crmAccountID);
        List<Integer> crmAccountUsedCurrencies = invoiceManager.getInvoiceEnabledCurrencies(crmAccountID, isCustomer);
        ArrayList<CurrencyItem> enabledCurrencyList = new ArrayList<>();
        HashSet<Integer> addedCurrencySet = new HashSet<>();
        if (crmAccount.getCurrency() != null) {
            enabledCurrencyList.add(crmAccount.getCurrency().createCurrencyItem());
            addedCurrencySet.add(crmAccount.getCurrency().getObjectID());
        }

        if (crmAccountUsedCurrencies != null && crmAccountUsedCurrencies.size() > 0) {
            for (Integer currencyID : crmAccountUsedCurrencies) {
                if (!addedCurrencySet.contains(currencyID)) {
                    enabledCurrencyList.add(currencyManager.get(currencyID).createCurrencyItem());
                    addedCurrencySet.add(currencyID);
                }
            }
        }

        CurrencyItem baseCurrency = getBaseCurrency();
        if (baseCurrency != null && !addedCurrencySet.contains(baseCurrency.getId())) {
            enabledCurrencyList.add(baseCurrency);
            addedCurrencySet.add(baseCurrency.getId());
        }
        return enabledCurrencyList.toArray(new CurrencyItem[]{});
    }

    @Override
    @Transactional
    public BatchPaymentResult saveReceivePaymentData(ReceivePaymentData paymentData, boolean isClient) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        kpiLog.setEntityId(paymentData.getCrmAccount() != null ? paymentData.getCrmAccount().getId() : null);
        if (isClient) {
            kpiLog.setEntityName(EdsCustomerSupplierPayment.class.getSimpleName() + " Receive Payment");
            ServerUtils.kpiLog(log, kpiLog, "Save Receive payment data");
        } else {
            kpiLog.setEntityName(EdsCustomerSupplierPayment.class.getSimpleName() + " Payment Bill");
            ServerUtils.kpiLog(log, kpiLog, "Save Payment bill data");
        }

        BatchPaymentResult result = new BatchPaymentResult();

        PaymentData[] payments = paymentData.getPayments();
        if (payments != null && payments.length > 0) {
            ArrayList<String> refNumbers = new ArrayList<>();
            for (PaymentData p : payments) {
                if (paymentData.isValidateReferences() && !ServerUtils.isNullOrEmpty(p.getReferenceNumber()) && invoicePaymentManager.isDuplicateReference(p.getReferenceNumber(), p.getObjectID())) {
                    refNumbers.add(p.getReferenceNumber());
                }
            }
            if (!refNumbers.isEmpty()) {
                result.setResult(0);
                result.setDuplicatedReferences(refNumbers.toArray(new String[0]));
                return result;
            }
        }


        Integer batchPaymentID = null;
        EdsBatchPayment edsBatchPayment = null;
        if (paymentData.isBatchPayment()) {
            //check for enable Receive Payments and Pay Invoices numbering settings
            boolean isEnableRPAndPINumberingSettings = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_RP_AND_PI_NUMBERING_SETTINGS);

            String receivablePrefix = isEnableRPAndPINumberingSettings ? EdsNumberingSettings.DEF_RECEIVE_PAYMENT_PREFIX : EdsNumberingSettings.DEF_BANK_RECEIPT_PREFIX;
            String notReceivablePrefix = isEnableRPAndPINumberingSettings ? EdsNumberingSettings.DEF_PAY_BILL_PREFIX : EdsNumberingSettings.DEF_BANK_PAYMENT_PREFIX;

            String code = Constants.RECEIVABLE.equals(paymentData.getType()) ? receivablePrefix : notReceivablePrefix;
            boolean isTransactionUniqueNumberEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.TRANSACTION_UNIQUE_NUMBERING);
            if (isTransactionUniqueNumberEnabled) {
                EdsBankAccount bankAccount = bankAccountManager.getBankAccountByAccountID(paymentData.getAccount().getId());

                if (Constants.RECEIVABLE.equals(paymentData.getType())) {
                    code = bankAccount != null ? receivablePrefix : EdsNumberingSettings.DEF_CASH_RECEIPT_PREFIX;
                } else {
                    code = bankAccount != null ? notReceivablePrefix : EdsNumberingSettings.DEF_CASH_PAYMENT_PREFIX;
                }
            }
            if (paymentData.getNumber() != null) {

                if (sharedNumberManager.isNumberExists(paymentData.getNumber(), paymentData.getObjectID(), code, BATCH_PAYMENT)) {
                    result.setResult(-1);
                    return result;
                }
            } else {
                BankTransferNumberData numberData = generateBatchPaymentNumberData(RECEIVABLE.equals(paymentData.getType()), paymentData.getAccount().getId());

                if (numberData.isWithDate()) {
                    numberData.setDate(ServerUtils.getBankTransferDateNumber(paymentData.getDate().getNonConvertedDate()));
                }

                paymentData.setNumber(numberData.getTransferNumber());

                Integer intNumber;
                try {
                    intNumber = Integer.parseInt(numberData.getFourDigitNumber());
                } catch (NumberFormatException e) {
                    intNumber = null;
                }
                paymentData.setIntNumber(intNumber);
            }
            if (paymentData.getObjectID() == null) {
                edsBatchPayment = new EdsBatchPayment();
                edsBatchPayment.setCreator(userManager.getUser());
            } else {
                edsBatchPayment = batchPaymentManager.get(paymentData.getObjectID());
                //Invoice payments delete and reverse applied prepayments
                List<EdsInvoicePayment> edsInvoicePaymentList = invoicePaymentManager.getBatchPaymentItems(edsBatchPayment.getObjectID());
                for (EdsInvoicePayment edsInvoicePayment : edsInvoicePaymentList) {
                    if (RECEIVABLE_PREPAYMENT.equals(edsInvoicePayment.getType()) || PAYABLE_SUPPLIER_CREDIT.equals(edsInvoicePayment.getType())) {
                        continue;
                    }
                    EdsInvoicePayment invoicePayment = invoicePaymentManager.get(edsInvoicePayment.getObjectID());
                    invoicePayment.setBatchPaymentID(null);
                    deletePayment(invoicePayment.getObjectID());

                    EdsInvoicePayment invoiceUnderPayment = invoicePaymentManager.getInvoiceUnderPayment(invoicePayment.getObjectID());
                    if (invoiceUnderPayment != null) {
                        invoiceUnderPayment.setBatchPaymentID(null);
                        deletePayment(invoiceUnderPayment.getObjectID());
                    }
                }
                //Manual Transaction or Customer/Supplier payments,transactions delete and reverse applied prepayments
                List<EdsCustomerSupplierPayment> edsCustomerSupplierPaymentList = customerSupplierPaymentManager.getBatchPaymentItems(edsBatchPayment.getObjectID());
                for (EdsCustomerSupplierPayment customerSupplierPayment : edsCustomerSupplierPaymentList) {
                    transactionManager.deleteCustomerSupplierPaymentTransaction(customerSupplierPayment.getObjectID());
                    customerSupplierPayment.setDeleted(true);

                    if (customerSupplierPayment.getManualJournalId() == null) {
                        EdsCrmAccount customerOrSupplier = crmAccountManager.get(customerSupplierPayment.getCustomerSupplierID());
                        customerOrSupplier.setPaid(false);
                    }
                    customerSupplierPaymentManager.update(customerSupplierPayment);

                    EdsCustomerSupplierPayment customerSupplierUnderPayment = customerSupplierPaymentManager.getUnderPayment(customerSupplierPayment.getObjectID());
                    if (customerSupplierUnderPayment != null) {
                        transactionManager.deleteCustomerSupplierPaymentTransaction(customerSupplierUnderPayment.getObjectID());
                        customerSupplierUnderPayment.setDeleted(true);

                        if (customerSupplierUnderPayment.getManualJournalId() == null) {
                            EdsCrmAccount customerOrSupplier = crmAccountManager.get(customerSupplierUnderPayment.getCustomerSupplierID());
                            customerOrSupplier.setPaid(false);
                        }
                        customerSupplierPaymentManager.update(customerSupplierUnderPayment);
                    }
                }
            }

            edsBatchPayment.setNumber(paymentData.getNumber());
            edsBatchPayment.setIntNumber(paymentData.getIntNumber());
            if (paymentData.getCrmAccount() != null) {
                edsBatchPayment.setCrmAccount(crmAccountManager.get(paymentData.getCrmAccount().getId()));
            }
            edsBatchPayment.setCurrency(currencyManager.get(paymentData.getCurrency().getId()));
            edsBatchPayment.setExchangeRate(paymentData.getExRate());
            edsBatchPayment.setAccount(accountingManager.get(paymentData.getAccount().getId()));
            edsBatchPayment.setReference(paymentData.getReference());
            edsBatchPayment.setDate(paymentData.getDate().getNonConvertedDate());
            edsBatchPayment.setPaymentMethod(paymentData.getPaymentMethod() != null ? paymentMethodManager.get(paymentData.getPaymentMethod().getId()) : null);
            edsBatchPayment.setTotalAmount(paymentData.getTotalAmount());
            edsBatchPayment.setType(paymentData.getType());
            edsBatchPayment.setPaymentTarget(paymentData.getPaymentTarget());
            edsBatchPayment.setDescription(paymentData.getDescription());
            edsBatchPayment.setIncludeSuAccountTransaction(paymentData.isIncludeSubAccountTransaction());

            if (paymentData.getProject() != null) {
                edsBatchPayment.setProject(projectManager.get(paymentData.getProject().getId()));
            }
            if (paymentData.getDepartment() != null) {
                edsBatchPayment.setDepartment(departmentManager.get(paymentData.getDepartment().getId()));
            }
            if (paymentData.getPdfTemplateID() != null) {
                edsBatchPayment.setPdfTemplate(companyPdfTemplateManager.get(paymentData.getPdfTemplateID()));
            }
            edsBatchPayment.setCustomFields(createInvoiceCustomFields(paymentData.getCustomFieldItems()));

            batchPaymentManager.createOrUpdate(edsBatchPayment);

            String entityCode = Constants.RECEIVABLE.equals(paymentData.getType()) ? AccountingConstants.RECEIVABLE_PREPAYMENT : AccountingConstants.PAYABLE_SUPPLIER_CREDIT;
            sharedNumberManager.saveNumberData(edsBatchPayment.getNumber(), edsBatchPayment.getIntNumber(), code, edsBatchPayment.getObjectID(), entityCode);

            batchPaymentID = edsBatchPayment.getObjectID();
            if (paymentData.getAttachments() != null && paymentData.getAttachments().length > 0 && batchPaymentID != null) {
                attachmentUtilsManager.saveAttachments(F_BATCH_PAYMENT, batchPaymentID, batchPaymentID, paymentData.getAttachments());
            }
            FileResource[] attachments = paymentData.getPaymentAttachments();
            if (attachments != null && attachments.length > 0) {
                FileItem[] fItems = new FileItem[attachments.length];
                for (int i = 0; i < attachments.length; i++) {
                    fItems[i] = new FileItem();
                    fItems[i].setId(attachments[i].getObjectId());
                    fItems[i].setFileName(attachments[i].getEncodedName());
                }

                attachmentUtilsManager.saveAttachments(F_BATCH_PAYMENT, batchPaymentID, batchPaymentID, fItems);
            }
        }

        Integer paymentID;
        if (payments != null && payments.length > 0) {
            EdsRelation relation = new EdsRelation();
            if (paymentData.isBatchPayment()) {
                relation.setFromType(isClient ? RelationItem.TYPE_CUSTOMER_PREPAYMENTS : RelationItem.TYPE_SUPPLIER_PREPAYMENTS);
                relation.setFromName(payments[0].getNumber());
            }

            Integer baseCurrencyId = getBaseCurrency().getId();
            for (PaymentData payment : payments) {
                Integer paymentCurrencyId = payment.getCurrency().getId();

                BigDecimal invoiceAmountInPaymentCurrency = payment.getTotal();
                BigDecimal paymentAmountInPaymentCurrency = payment.getPaymentAmount();
                BigDecimal invoiceAmount;
                BigDecimal paymentAmount;

                if (baseCurrencyId.equals(paymentCurrencyId)) {
                    invoiceAmount = invoiceAmountInPaymentCurrency != null ? invoiceAmountInPaymentCurrency : null;
                    paymentAmount = paymentAmountInPaymentCurrency;
                } else {
                    invoiceAmount = invoiceAmountInPaymentCurrency != null ? invoiceAmountInPaymentCurrency.divide(payment.getExchangeRate(), 5, RoundingMode.HALF_UP) : null;
                    paymentAmount = paymentAmountInPaymentCurrency.divide(payment.getExchangeRate(), 5, RoundingMode.HALF_UP);

                }
                BigDecimal underPaymentInBase = BigDecimal.ZERO, underPaymentInTc = BigDecimal.ZERO;
                if (payment.getUnderPaymentAmount() != null && payment.getUnderPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
                    underPaymentInTc = payment.getUnderPaymentAmountInInvoiceCurrency() != null ? payment.getUnderPaymentAmountInInvoiceCurrency() : payment.getUnderPaymentAmount();
                    underPaymentInBase = (underPaymentInTc).divide(payment.getExchangeRate(), 5, RoundingMode.HALF_UP);
                }
                payment.setPaymentAmount(paymentAmount);
                payment.setPaymentAmountInInvoiceCurrency(paymentAmountInPaymentCurrency);
                payment.setTotal(invoiceAmount);

                payment.setValidateReference(false);
                payment.setBatchPaymentID(batchPaymentID);
                payment.setUnderPaymentID(null);
                payment.setForeignAccExRate(paymentData.getForeignAccExRate());

                SelectItem paymentCrmAccountItem = payment.getCrmAccount();
                Integer clientSupplierID = (paymentCrmAccountItem != null && paymentCrmAccountItem.getId() != null) ? paymentCrmAccountItem.getId() : null;

                if (payment.getPaymentAmount().compareTo(ZERO) > 0) {
                    if (payment.isExpensePayment()) {
                        paymentID = createExpensePayment(clientSupplierID, payment);
                        if (payment.getUnderPaymentAccount() != null) {
                            payment.setUnderPaymentID(paymentID);
                            payment.setPaymentAccount(payment.getUnderPaymentAccount());
                            payment.setPaymentAmount(underPaymentInBase);
                            payment.setPaymentAmountInInvoiceCurrency(underPaymentInTc);
                            createExpensePayment(clientSupplierID, payment);
                        }
                    } else if (payment.isOpeningBalance()) {
                        paymentID = createClientSupplierPayment(clientSupplierID, payment, isClient);
                        if (payment.getUnderPaymentAccount() != null) {
                            payment.setUnderPaymentID(paymentID);
                            payment.setPaymentAccount(payment.getUnderPaymentAccount());
                            payment.setPaymentAmount(underPaymentInBase);
                            payment.setPaymentAmountInInvoiceCurrency(underPaymentInTc);
                            createClientSupplierPayment(clientSupplierID, payment, isClient);
                        }
                    } else if (payment.isManualJournal()) {
                        paymentID = createManualJournalPayment(clientSupplierID, payment, isClient);
                        if (payment.getUnderPaymentAccount() != null) {
                            payment.setUnderPaymentID(paymentID);
                            payment.setPaymentAccount(payment.getUnderPaymentAccount());
                            payment.setPaymentAmount(underPaymentInBase);
                            payment.setPaymentAmountInInvoiceCurrency(underPaymentInTc);
                            createManualJournalPayment(clientSupplierID, payment, isClient);
                        }
                    } else if (RECEIVABLE_PREPAYMENT.equals(payment.getType()) || PAYABLE_SUPPLIER_CREDIT.equals(payment.getType())) {
                        paymentID = prepaymentServiceLocal.savePrePayment(payment, false);
                    } else {

                        if (payment.getType() == null) {
                            payment.setType(isClient ? RECEIVABLE : PAYABLE);
                        }
                        paymentID = savePayment(payment);

                        if (payment.getUnderPaymentAccount() != null && payment.getUnderPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
                            payment.setUnderPaymentID(paymentID);
                            payment.setPaymentAccount(payment.getUnderPaymentAccount());
                            payment.setPaymentAmount(underPaymentInBase);
                            payment.setPaymentAmountInInvoiceCurrency(underPaymentInTc);
                            savePayment(payment);
                        }
                    }
                    relation.setFromID(paymentID);
                }
                if (payment.getSaleQuoteItem() != null) {
                    EdsSaleQuote quote = quoteManager.getSaleQuote(payment.getSaleQuoteItem().getId());
                    relation.setToID(payment.getSaleQuoteItem().getId());
                    relation.setToType(quote.isSalesOrder() ? RelationItem.TYPE_SALEORDER : RelationItem.TYPE_SALEQUOTE);
                    relation.setToName(payment.getSaleQuoteItem().getName());
                    relationManager.create(relation);
                } else if (paymentData.getRentalOrderId() != null || payment.getRentalOrderItem() != null) {
                    relation.setToID(paymentData.getRentalOrderId() == null ? payment.getRentalOrderItem().getId() : paymentData.getRentalOrderId());
                    relation.setToName(wfmMessageSource.localize("rentalOrder"));
                    relation.setToType(RelationItem.TYPE_RENTAL_ORDER);
                    relationManager.create(relation);
                }
            }

            if (paymentData.getOverPayment() != null) {
                paymentData.getOverPayment().setBatchPaymentID(batchPaymentID);
                saveOverPayment(paymentData.getOverPayment());
            }
        }
        EdsBusinessEvent paymentWorkflow = null;
        if (paymentData.getObjectID() == null) {
            baseEventPostProcessor.registerEvent(BatchPaymentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsBatchPayment, userManager.getUser());
            paymentWorkflow = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsBatchPayment, userManager.getUser());
        } else {
            baseEventPostProcessor.registerEvent(BatchPaymentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsBatchPayment, userManager.getUser());
            paymentWorkflow = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsBatchPayment, userManager.getUser());
        }
        paymentWorkflow.setEntityType(isClient ? RelationItem.TYPE_BATCH_PAYMENT_RECEIVABLE : RelationItem.TYPE_BATCH_PAYMENT_PAYABLE);
        if (payments != null && payments.length == 1 && payments[0].getInvoiceID() != null) {  // payment made from invoiceSummaryView
            EdsInvoice invoice = invoiceManager.get(payments[0].getInvoiceID());
            if ((invoice != null) && (invoice.getStatus() != null)) {
                result.setInvoiceStatusCode(invoice.getStatus().getCode());
                String lastChanges = invoice.getLastChanges();
                invoice.setLastChanges(lastChanges + ",PAID_AMOUNT,DUE_AMOUNT");
            }
            EdsBusinessEvent workflow = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, invoice, userManager.getUser());
            workflow.setEntityType(isClient ? RelationItem.TYPE_SALEINVOICE : RelationItem.TYPE_PURCHASE_INVOICE);
        }
        result.setPaymentId(batchPaymentID);
        result.setResult(1);
        return result;
    }

    @Override
    public BatchPaymentResult savePaymentRefundData(ReceivePaymentData paymentData) {

        BatchPaymentResult result = new BatchPaymentResult();

        Integer refundPaymentID = null;
        EdsPaymentRefund edsPaymentRefund = null;

        if (paymentData.getObjectID() == null) {
            edsPaymentRefund = new EdsPaymentRefund();
            edsPaymentRefund.setCreator(userManager.getUser());
        } else {
            edsPaymentRefund = paymentRefundManager.get(paymentData.getObjectID());


            List<EdsInvoicePayment> edsInvoicePaymentList = invoicePaymentManager.getRefundItems(edsPaymentRefund.getObjectID());
            for (EdsInvoicePayment edsInvoicePayment : edsInvoicePaymentList) {

                EdsInvoicePayment invoicePayment = invoicePaymentManager.get(edsInvoicePayment.getObjectID());
                invoicePayment.setPaymentRefundID(null);
                deletePayment(invoicePayment.getObjectID());

            }
        }

        if (paymentData.getNumber() == null || "".equals(paymentData.getNumber())) {
            paymentData.setNumber(generatePaymentRefundNumber(paymentData.getType()).getTransferNumber());
        }

        edsPaymentRefund.setNumber(paymentData.getNumber());
        edsPaymentRefund.setNumberInt(paymentData.getIntNumber());
        if (paymentData.getCrmAccount() != null) {
            edsPaymentRefund.setCrmAccount(crmAccountManager.get(paymentData.getCrmAccount().getId()));
        }
        if (paymentData.getCurrency() != null) {
            edsPaymentRefund.setCurrencyID(paymentData.getCurrency().getId());
        }
        edsPaymentRefund.setExchangeRate(paymentData.getExRate());
        if (paymentData.getAccount() != null) {
            EdsAccount account = accountingManager.get(paymentData.getAccount().getId());
            edsPaymentRefund.setAccount(account);
        }
        edsPaymentRefund.setReference(paymentData.getReference());
        edsPaymentRefund.setPaymentDate(paymentData.getDate().getNonConvertedDate());
        edsPaymentRefund.setTotal(paymentData.getTotalAmount());
        edsPaymentRefund.setTotalInBase(paymentData.getTotalAmount().divide(edsPaymentRefund.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        edsPaymentRefund.setType(paymentData.getType());
        edsPaymentRefund.setPaymentTarget(paymentData.getPaymentTarget());
        edsPaymentRefund.setDescription(paymentData.getDescription());

        paymentRefundManager.createOrUpdate(edsPaymentRefund);

        refundPaymentID = edsPaymentRefund.getObjectID();
//        if (paymentData.getAttachments() != null && paymentData.getAttachments().length > 0 && refundPaymentID != null) {
//            attachmentUtilsManager.saveAttachments(F_BATCH_PAYMENT, refundPaymentID, refundPaymentID, paymentData.getAttachments());
//        }
//        FileResource[] attachments = paymentData.getPaymentAttachments();
//        if (attachments != null && attachments.length > 0) {
//            FileItem[] fItems = new FileItem[attachments.length];
//            for (int i = 0; i < attachments.length; i++) {
//                fItems[i] = new FileItem();
//                fItems[i].setId(attachments[i].getObjectId());
//                fItems[i].setFileName(attachments[i].getEncodedName());
//            }
//
//            attachmentUtilsManager.saveAttachments(F_BATCH_PAYMENT, refundPaymentID, refundPaymentID, fItems);
//        }

        if (paymentData.getPayments() != null && paymentData.getPayments().length > 0) {
            for (PaymentData payment : paymentData.getPayments()) {
                payment.setValidateReference(false);
                payment.setPaymentRefundID(refundPaymentID);
                payment.setPrepaymentID(payment.getInvoiceID());

                payment.setDate(paymentData.getDate());
                if (payment.getRefundAmount() != null && payment.getRefundAmount().compareTo(ZERO) > 0 || payment.getClosedAmount() != null && payment.getClosedAmount().compareTo(ZERO) > 0) {
                    payment.setPaymentAmountInInvoiceCurrency(payment.getRefundAmount());
                    payment.setPaymentAmount(payment.getRefundAmount().divide(paymentData.getExRate(), 2, RoundingMode.HALF_UP));
                    payment.setBasePaymentAmount(payment.getRefundAmount().divide(paymentData.getExRate(), 2, RoundingMode.HALF_UP));
                    payment.setBaseAmount(payment.getRefundAmount().divide(paymentData.getExRate(), 2, RoundingMode.HALF_UP));
                    payment.setPaymentAccount(edsPaymentRefund.getAccount() != null ? edsPaymentRefund.getAccount().createAccountItem() : null);
                    payment.setPrepaymentID(payment.getInvoiceID());
                    payment.setInvoiceID(null);
                    payment.setCrmAccount(paymentData.getCrmAccount());
                    payment.setReferenceNumber(edsPaymentRefund.getNumber());

                    savePayment(payment);
                }
            }
            if (paymentData.getCloseAccount() != null && paymentData.getCloseAmount() != null) {
                accountingServiceLocal.createTransactionForPaymentRefundCloseAmount(edsPaymentRefund, paymentData.getCloseAmount(), paymentData.getCloseAccount(), paymentData.getPayments());
            }
        }

        result.setPaymentId(refundPaymentID);
        result.setResult(1);
        return result;
    }

    private void addAttachmentsToPaymentOrRefund(PaymentData data, Integer paymentOrRefundId) {
        if (data.getAttachedFiles() != null && data.getAttachedFiles().length > 0 && paymentOrRefundId != null) {
            attachmentUtilsManager.saveAttachments(F_BATCH_PAYMENT, paymentOrRefundId, paymentOrRefundId, data.getAttachedFiles());
        }
        FileResource[] attachments = data.getAttachments();
        if (attachments != null && attachments.length > 0) {
            FileItem[] fItems = new FileItem[attachments.length];
            for (int i = 0; i < attachments.length; i++) {
                fItems[i] = new FileItem();
                fItems[i].setId(attachments[i].getObjectId());
                fItems[i].setFileName(attachments[i].getEncodedName());
            }

            attachmentUtilsManager.saveAttachments(F_BATCH_PAYMENT, paymentOrRefundId, paymentOrRefundId, fItems);
        }
    }

    private Integer createExpensePayment(Integer clientSupplierID, PaymentData pData) {
        ExpensePaymentData expensePaymentData = new ExpensePaymentData();
        expensePaymentData.setObjectID(pData.getObjectID());
        expensePaymentData.setPaymentAmount(pData.getPaymentAmount());
        expensePaymentData.setDate(new DateNonConvertable(pData.getDate().getNonConvertedDate()));
        expensePaymentData.setPaymentAccount(pData.getPaymentAccount());
        expensePaymentData.setReferenceNumber(pData.getReferenceNumber());
        expensePaymentData.setValidateReference(false);
        expensePaymentData.setReportId(pData.getInvoiceID());
        expensePaymentData.setExchangeRate(pData.getExchangeRate());
        expensePaymentData.setBatchPaymentID(pData.getBatchPaymentID());

        if (pData.isPaymentDiffCurrency()) {
            expensePaymentData.setPaymentAmountInExpenseCurrency(pData.getPaymentAmountInInvoiceCurrency());
            if (pData.getCurrency() != null) {
                expensePaymentData.setCurrency(currencyService.getCurrency(pData.getCurrency().getId()));
            }
        }
        if (pData.getObjectID() != null) {
            expenseService.deleteExpensePayment(pData.getObjectID());
        }
        expensePaymentData.setObjectID(null);
        return expenseService.savePayment(expensePaymentData);
    }

    private Integer createClientSupplierPayment(Integer clientSupplierID, PaymentData pData, boolean isClient) {
        BigDecimal paidAmount = customerSupplierPaymentManager.getPaidAmount(clientSupplierID, isClient);

        EdsUser user = userManager.getUser();

        EdsCustomerSupplierPayment payment = new EdsCustomerSupplierPayment();
        payment.setCustomerSupplierID(clientSupplierID);
        payment.setBatchPaymentID(pData.getBatchPaymentID());
        payment.setUnderPaymentID(pData.getUnderPaymentID());
        payment.setType(isClient ? EdsCustomerSupplierPayment.CUSTOMER_PAYMENT : EdsCustomerSupplierPayment.SUPPLIER_PAYMENT);
        payment.setAmount(pData.getPaymentAmount());
        payment.setPaymentDate(pData.getDate().getNonConvertedDate());
        payment.setAccount(accountingManager.get(pData.getPaymentAccount().getId()));
        payment.setExchangeRate(pData.getExchangeRate());
        payment.setCurrencyID(pData.getCurrency() != null ? pData.getCurrency().getId() : null);
        payment.setReference(pData.getReferenceNumber());
        payment.setUser(user);

        if (pData.isPaymentDiffCurrency()) {
            payment.setAmountInEntityCurrency(pData.getPaymentAmountInInvoiceCurrency());
        }
        customerSupplierPaymentManager.create(payment);

        //If opening balance fully paid then mark opening balance as paid
        paidAmount = paidAmount.add((payment.getAmountInEntityCurrency() != null ? payment.getAmountInEntityCurrency() : payment.getAmount()).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        EdsCrmAccount crmAccount = crmAccountManager.get(clientSupplierID);
        if (crmAccount.getBalanceAmount() != null && paidAmount.compareTo(crmAccount.getBalanceAmount()) >= 0) {
            crmAccount.setPaid(true);
            crmAccountManager.update(crmAccount);
        }

        accountingServiceLocal.createClientSupplierPaymentTransaction(payment.getObjectID());

        return payment.getObjectID();
    }

    private Integer createManualJournalPayment(Integer clientSupplierID, PaymentData pData, boolean isClient) {
        EdsUser user = userManager.getUser();

        EdsCustomerSupplierPayment payment = new EdsCustomerSupplierPayment();
        payment.setUnderPaymentID(pData.getUnderPaymentID());
        payment.setCustomerSupplierID(clientSupplierID);
        payment.setBatchPaymentID(pData.getBatchPaymentID());
        payment.setManualJournalId(pData.getInvoiceID());
        payment.setType(isClient ? EdsCustomerSupplierPayment.CUSTOMER_PAYMENT : EdsCustomerSupplierPayment.SUPPLIER_PAYMENT);
        payment.setAmount(pData.getPaymentAmount());
        payment.setAmountInEntityCurrency(pData.getPaymentAmountInInvoiceCurrency());
        payment.setPaymentDate(pData.getDate().getNonConvertedDate());
        payment.setAccount(accountingManager.get(pData.getPaymentAccount().getId()));
        payment.setExchangeRate(pData.getExchangeRate());
        payment.setCurrencyID(pData.getCurrency() != null ? pData.getCurrency().getId() : null);
        payment.setReference(pData.getReferenceNumber());
        payment.setUser(user);

        if (pData.isPaymentDiffCurrency()) {
            payment.setAmountInEntityCurrency(pData.getPaymentAmountInInvoiceCurrency());
        }

        if (pData.getAccountItem() != null && pData.getAccountItem().getId() != null) {
            payment.setAccountArAp(accountingManager.get(pData.getAccountItem().getId()));
        }
        customerSupplierPaymentManager.create(payment);
        accountingServiceLocal.createClientSupplierPaymentTransaction(payment.getObjectID());

        return payment.getObjectID();
    }

    @Deprecated
    @Override
    public SelectItem[] getRelatedProjectsWithFilter(ListingFilterParameter filterParametrs) {
        if (filterParametrs.getInvoiceType() != null && EXPENSE_REPORT.equals(filterParametrs.getInvoiceType())) {
            return getExpenseRelatedProjects(filterParametrs).getList().toArray(new SelectItem[]{});
        } else if (Constants.PROJECT_GOAL.equals(filterParametrs.getRelationType())) {
            filterParametrs.setAllByProjectGoal(true);
            ListResult<GoalItem> projectGoalList = hrmsService.getProjectGoalList(filterParametrs);
            ArrayList<SelectItem> result = new ArrayList<>();
            if (projectGoalList.getList() != null && !projectGoalList.getList().isEmpty()) {
                for (GoalItem item : projectGoalList.getList()) {
                    String goalTitle = item.getGoalNumber().getNumberString() + " - " + item.getTitle();
                    result.add(new SelectItem(item.getObjectId(), goalTitle));
                }
            }
            return result.toArray(new SelectItem[0]);
        } else {
            filterParametrs.setViewAsId(DR);
            return invoiceCircularResolver.getRelatedProjects(filterParametrs);
        }
    }

    @Override
    public ListResult<SelectItem> getExpenseRelatedProjects(ListingFilterParameter fp) {
        EdsUser edsUser = fp.getEmployeeId() != null ? userManager.get(fp.getEmployeeId()) : userManager.getUser();

        EdsCompany edsCompany = edsUser.getCompany();
        Set<Integer> roles = edsUser.getRoleIds();

        SolrQuery solrQuery = getProjectSolrQuery(fp, edsUser, edsCompany, roles);
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_PROJECT_CORE);
        QueryResponse resp;
        try {
            resp = server.query(solrQuery);
        } catch (SolrServerException | IOException e) {
            log.error("", e);
            return new ListResult<>(new ArrayList<>(), 0);
        }

        if (resp != null && resp.getResults() != null) {
            ArrayList<SelectItem> result = new ArrayList<>();
            if (fp.isFromMobile()) {
                for (SolrDocument sDoc : resp.getResults()) {
                    Integer projectID = (Integer) sDoc.getFieldValue(SolrProjectListRepresenter.FIELD_PROJECT_ID);
                    String number = (String) sDoc.getFieldValue(SolrProjectListRepresenter.FIELD_PROJECT_NUMBER);
                    String name = (String) sDoc.getFieldValue(SolrProjectListRepresenter.FIELD_PROJECT_NAME);
                    result.add(new SelectItem(projectID, name, number, "", ""));
                }
            } else {
                for (SolrDocument sDoc : resp.getResults()) {
                    Integer projectID = (Integer) sDoc.getFieldValue(SolrProjectListRepresenter.FIELD_PROJECT_ID);
                    String number = (String) sDoc.getFieldValue(SolrProjectListRepresenter.FIELD_PROJECT_NUMBER);
                    String name = (String) sDoc.getFieldValue(SolrProjectListRepresenter.FIELD_PROJECT_NAME);
                    result.add(new SelectItem(projectID, (number != null && !"".equals(number.trim()) ? number + SolrProjectListRepresenter.ARROW : "") + name, number));
                }
            }
            return new ListResult<>(result, Long.valueOf(resp.getResults().getNumFound()).intValue());
        }

        return new ListResult<>(new ArrayList<SelectItem>(), 0);
    }

    private SolrQuery getProjectSolrQuery(ListingFilterParameter fp, EdsUser user, EdsCompany
            company, Set<Integer> userRoles) {
        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrProjectListRepresenter.FIELD_COMPANY_ID).append(":").append(company.getObjectID());
        if (!ServerUtils.hasPermission(PermissionConstants.PM_SEE_ALL_PROJECTS) || fp.getEmployeeId() != null) {
            if (!userRoles.contains(EdsRole.ADMIN) && !userRoles.contains(EdsRole.DR) && userRoles.contains(EdsRole.ADMIN_LOCATION)) {
                EdsLocation location = user.getLocation();
                if (location != null) {
                    solrQuery.append(" AND ").append(SolrProjectListRepresenter.FIELD_USER_LOCATION_ID).append(":").append(location.getObjectID());
                }
            } else {
                if (user.isClientContact()) {
                    solrQuery.append(" AND ").append(SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_ID).append(":").append(user.getClientContact().getClientID());
                } else if (!(userRoles.contains(EdsRole.DR) || userRoles.contains(EdsRole.ADMIN) || userRoles.contains(EdsRole.ACCOUNTANT))) {
                    solrQuery.append(" AND ").append(SolrProjectListRepresenter.FIELD_USER_ID).append(":").append(user.getObjectID());
                }
            }
        }
        // Get projects by parentId
        if (fp.getProjectId() != null) {
            solrQuery.append(" AND ").append(SolrProjectListRepresenter.FIELD_PARENT_ID).append(":").append(fp.getProjectId());
        } else if (!EXPENSE_REPORT.equals(fp.getInvoiceType())) {
            solrQuery.append(" AND (-").append(SolrProjectListRepresenter.FIELD_PARENT_ID).append(":[* TO *] AND *:*)");
        }
        // get projects by clientId
        if (fp.getClientId() != null) {
            solrQuery.append(" AND (").append(SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_ID).append(":").append(fp.getClientId())
                    .append(" OR ").append(SolrProjectListRepresenter.FIELD_PROJECT_MULTI_CLIENT_ID).append(":").append(fp.getClientId()).append(") ");
        }
        // Set Search key
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            if (fp.isFromMobile()) {
                solrQuery.append(" AND (").append(SolrProjectListRepresenter.FIELD_PROJECT_NAME_NUMBER_COMPOSITE).append(":(").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), true)).append(")");
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateApiSearchQuery(solrQuery, QueryBuilderForSolr.getApiSearchFields(), fp.getSearchKey());
                solrQuery.append(")");
            } else {
                solrQuery.append(" AND (");
                solrQuery.append(SolrProjectListRepresenter.FIELD_LOOKUP_COMPOSITE).append(":").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), fp.isLookUp()));
                solrQuery.append(") ");
            }
        }
        solrQuery.append(" AND -(").append(SolrProjectListRepresenter.FIELD_PROJECT_STATUS_CODE).append(":").append(EdsProject.COMPLETED);
        solrQuery.append(") ");

        solrQuery.append(" AND -(").append(SolrProjectListRepresenter.FIELD_PROJECT_STATUS_CODE).append(":").append(EdsProject.CLOSED);
        solrQuery.append(") ");

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(fp.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(fp.getLimit()));
        if (!fp.isSearchButton()) {
            query.setSort(SolrProjectListRepresenter.FIELD_PROJECT_NAME, SolrQuery.ORDER.asc);
        }

        return query;
    }

    @Override
    public void mergeCrmAccounts(Integer newAccountID, List<Integer> oldAccountIDs) {
        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        EdsCrmAccount newAccount = crmAccountManager.get(newAccountID);

        if (oldAccountIDs != null && !oldAccountIDs.isEmpty()) {

            for (Integer accId : oldAccountIDs) {
                List<EdsSaleInvoice> saleInvoices = invoiceManager.getSaleInvoicesByCrmAccountID(accId);
                List<EdsSaleQuote> salesQuotes = quoteManager.getSaleQuotesByCrmAccountID(accId);
                List<EdsRecurringInvoice> recurringInvoices = invoiceManager.getRecurringInvoicesByCrmAccountID(accId);
                List<EdsPurchaseInvoice> purchaseInvoices = invoiceManager.getPurchaseInvoicesByCrmAccountID(accId);
                List<EdsPurchaseOrder> purchaseOrders = quoteManager.getPurchaseOrdersByCrmAccountID(accId);
                List<EdsExpenseReport> expenseReports = expenseReportManager.getExpensesByCrmAccountID(accId);

                for (EdsSaleInvoice si : saleInvoices) {
                    si.setClient(newAccount);
                    invoiceManager.update(si);
                    try {
                        saleInvoiceSolrComponent.index(si);
                    } catch (IOException | SolrServerException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateAddressCrmAccounts(si, newAccountID);
                }
                for (EdsSaleQuote sq : salesQuotes) {
                    sq.setClient(newAccount);
                    quoteManager.update(sq);
                    EdsPickList pickList = pickListManager.getPickListBySaleQuoteID(sq.getObjectID());
                    try {
                        saleQuoteSolrComponent.indexes(Collections.singletonList(sq), (pickList != null) ? Collections.singletonList(pickList) : null);
                    } catch (IOException | SolrServerException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateAddressCrmAccounts(sq, newAccountID);
                }
                for (EdsRecurringInvoice ri : recurringInvoices) {
                    ri.setClient(newAccount);
                    invoiceManager.update(ri);
                    updateAddressCrmAccounts(ri, newAccountID);
                }
                for (EdsPurchaseInvoice pi : purchaseInvoices) {
                    pi.setSupplier(newAccount);
                    invoiceManager.update(pi);
                    try {
                        purchaseInvoiceSolrComponent.index(pi);
                    } catch (IOException | SolrServerException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateAddressCrmAccounts(pi, newAccountID);
                }
                for (EdsPurchaseOrder po : purchaseOrders) {
                    po.setSupplier(newAccount);
                    quoteManager.update(po);
                    try {
                        purchaseOrderSolrComponent.index(po);
                    } catch (IOException | SolrServerException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateAddressCrmAccounts(po, newAccountID);
                }
                for (EdsExpenseReport er : expenseReports) {
                    er.setSupplier(newAccount);
                    try {
//                        solrManager.addExpenseReportToIndex(er);
                        expenseReportClaimsSolrComponent.index(er);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            //Batch update old crm accounts to new one
            manualJournalManager.mergeOldCrmAccountToNewOne(oldAccountIDs, newAccountID);
            spendReceiveMoneyManager.mergeOldCrmAccountToNewOne(oldAccountIDs, newAccountID);
            invoicePaymentManager.mergeOldCrmAccountToNewOne(oldAccountIDs, newAccountID);
            customerSupplierPaymentManager.mergeOldCrmAccountToNewOne(oldAccountIDs, newAccountID);
            bankCheckManager.mergeOldCrmAccountToNewOne(oldAccountIDs, newAccountID);
            transactionManager.mergeOldCrmAccountToNewOne(oldAccountIDs, newAccountID);
            rfqItemManager.mergeOldCrmAccountToNewOne(oldAccountIDs, newAccountID);
            rfqManager.mergeOldCrmAccountToNewOne(oldAccountIDs, newAccountID);
            expenseReportManager.mergeExpenseItemWithOldCrmAccountToNewOne(oldAccountIDs, newAccountID);
            expensePaymentManager.mergeOldCrmAccountToNewOne(oldAccountIDs, newAccountID);
        }
    }

    public void changeRelatedProject(Integer invoiceQuoteID, String type, Integer relatedProjectID) {
        EdsProject relatedProject = relatedProjectID != null ? projectManager.get(relatedProjectID) : null;
        if (Constants.SALE_QUOTE.equals(type) || Constants.SALE_ORDER.equals(type)) {
            EdsQuote edsQuote = quoteManager.get(invoiceQuoteID);
            if (edsQuote instanceof EdsSaleQuote saleQuote) {
                saleQuote.setRelatedProject(relatedProject);
                saleQuote.setUpdatedDate(new Date());
                addSaleQuoteToSolr(saleQuote);
            }
        } else if (Constants.SALE_INVOICE.equals(type) || RECEIVABLE_CREDIT_NOTE.equals(type)) {
            EdsSaleInvoice saleInvoice = (EdsSaleInvoice) invoiceManager.get(invoiceQuoteID);
            EdsProject oldProject = saleInvoice.getRelatedProject();
            saleInvoice.setRelatedProject(relatedProject);
            invoiceManager.update(saleInvoice);
            try {
                saleInvoiceSolrComponent.index(saleInvoice);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
            if (Constants.SALE_INVOICE.equals(type)) {
                updateRelatedProjectFromSolr(oldProject, relatedProject, invoiceManager.getUser().getCompany());
            }
        } else if (Constants.PURCHASE_ORDER.equals(type)) {
            EdsPurchaseOrder purchaseOrder = (EdsPurchaseOrder) quoteManager.get(invoiceQuoteID);
            purchaseOrder.setRelatedProject(relatedProject);
            purchaseOrder.setUpdatedDate(new Date());
            try {
                purchaseOrderSolrComponent.index(purchaseOrder);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (Constants.PURCHASE_INVOICE.equals(type) || PAYABLE_CREDIT_NOTE.equals(type)) {
            EdsPurchaseInvoice purchaseInvoice = (EdsPurchaseInvoice) invoiceManager.get(invoiceQuoteID);
            purchaseInvoice.setRelatedProject(relatedProject);
            purchaseInvoice.setUpdatedDate(new Date());
            try {
                purchaseInvoiceSolrComponent.index(purchaseInvoice);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void changeBankAccount(Integer invoiceQuoteID, String type, Integer bankAccountId) {
        EdsBankAccount bankAccount = bankAccountId != null ? bankAccountManager.get(bankAccountId) : null;
        if (Constants.SALE_INVOICE.equals(type) || RECEIVABLE_CREDIT_NOTE.equals(type)) {
            EdsSaleInvoice saleInvoice = (EdsSaleInvoice) invoiceManager.get(invoiceQuoteID);
            saleInvoice.setBankAccount(bankAccount);
            invoiceManager.update(saleInvoice);
            try {
                saleInvoiceSolrComponent.index(saleInvoice);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (Constants.SALE_QUOTE.equals(type) || Constants.SALE_ORDER.equals(type)) {
            EdsQuote edsQuote = quoteManager.get(invoiceQuoteID);
            if (edsQuote instanceof EdsSaleQuote saleQuote) {
                saleQuote.setBankAccount(bankAccount);
                saleQuote.setUpdatedDate(new Date());
                addSaleQuoteToSolr(saleQuote);
            }
        } else if (Constants.PURCHASE_ORDER.equals(type)) {
            EdsPurchaseOrder purchaseOrder = (EdsPurchaseOrder) quoteManager.get(invoiceQuoteID);
            purchaseOrder.setBankAccount(bankAccount);
            purchaseOrder.setUpdatedDate(new Date());
            try {
                purchaseOrderSolrComponent.index(purchaseOrder);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private SelectItem getRelatedProject(Integer projectID) {
        EdsProject project = projectManager.get(projectID);
        return new SelectItem(project.getObjectID(),
                (project.getNumber() != null && !"".equals(project.getNumber().trim()) ? project.getNumber() + " -> " : "") + project.getName(), project.getNumber());
    }

    private void updateAddressCrmAccounts(EdsBaseInvoice invoice, Integer newAccountID) {
        if (invoice.getBillAddressID() != null) {
            EdsAddress address = addressManager.get(invoice.getBillAddressID());
            if (address != null) {
                address.setEntityType(EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
                address.setEntityID(newAccountID);
                addressManager.update(address);
            }
        }
        if (invoice.getMailAddressID() != null) {
            EdsAddress address = addressManager.get(invoice.getMailAddressID());
            if (address != null) {
                address.setEntityType(EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
                address.setEntityID(newAccountID);
                addressManager.update(address);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactItem[] getContactsEmailAsSelectItem(Integer objectID, String messageType, Integer contactId,
                                                      boolean isComposeForm) {
        if (objectID != null) {
            if (PURCHASE_ORDER_MANAGER_CATEGORY.equals(messageType) || SALES_QUOTE_MANAGER_CATEGORY.equals(messageType)) {
                EdsUser user = userManager.get(objectID);
                ContactItem contactItem = new ContactItem(user.getObjectID(), user.getEmail(), user.getName(), false, false);
                return new ContactItem[]{contactItem};
            } else {
                if (isComposeForm) {
                    EdsCrmAccount clintOrSupplier = crmAccountManager.get(objectID);
                    List<ContactItem> items = new LinkedList<>();
                    if (clintOrSupplier != null) {
                        Set<EdsCrmContact> contacts = clintOrSupplier.getCrmContacts();

                        if (contacts.size() > 0) {
                            for (EdsCrmContact edsContact : contacts) {
                                if (edsContact.getPrimaryEmail() != null && !"".equals(edsContact.getPrimaryEmail().trim())) {
                                    ContactItem item = new ContactItem();
                                    item.setId(edsContact.getObjectID());
                                    item.setName(edsContact.getPrimaryEmail());
                                    item.setContactName(edsContact.getName());
                                    item.setPrimaryContact(edsContact.getPrimaryContact() != null ? edsContact.getPrimaryContact() : false);
                                    item.setHasAccess(edsContact.isAccessEnabled());
                                    items.add(item);
                                }
                            }
                            items.sort(Comparator.comparing(SelectItem::getId));
                        }
                    }
                    return items.toArray(new ContactItem[]{});
                } else {
                    EdsCrmAccount clintOrSupplier = crmAccountManager.get(objectID);
                    Set<EdsCrmContact> contacts = null;
                    if (clintOrSupplier != null) {
                        contacts = clintOrSupplier.getCrmContacts();
                    }
                    List<ContactItem> items = new LinkedList<>();
                    if (contacts != null && contacts.size() > 0) {
                        for (EdsCrmContact c : contacts) {
                            if (c.getPrimaryEmail() != null && !"".equals(c.getPrimaryEmail().trim()) && !c.isDeleted()) {
                                items.add(new ContactItem(c.getObjectID(),
                                        c.getPrimaryEmail() + (c.isAccessEnabled() ? "(Access Enabled)" : ""),
                                        c.getName(),
                                        c.getPrimaryContact() != null ? c.getPrimaryContact() : false,
                                        c.isAccessEnabled()));
                            }
                        }
                        items.sort(Comparator.comparing(SelectItem::getId));
                    }
                    return items.toArray(new ContactItem[]{});
                }
            }
        }
        return new ContactItem[]{};
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SendToFormFillingData getSendToFormFillingData(SendToFormFillingData data) {
        data.setContacts(getContactsEmailAsSelectItem(data.getClientOrManagerID(), data.getMessageType(), null, false));

        String formType = null;
        if (SALES_INVOICE_CATEGORY.equals(data.getMessageType()) || RECURRING_INVOICE_CATEGORY.equals(data.getMessageType())) {
            formType = SALE_INVOICE;
        } else if (SALES_QUOTE_CATEGORY.equals(data.getMessageType()) || SALES_QUOTE_MANAGER_CATEGORY.equals(data.getMessageType())) {
            formType = SALE_QUOTE;
        } else if (PURCHASE_ORDER_CATEGORY.equals(data.getMessageType()) || PURCHASE_ORDER_MANAGER_CATEGORY.equals(data.getMessageType())) {
            formType = PURCHASE_ORDER;
        } else if (CREDIT_NOTE_CATEGORY.equals(data.getMessageType())) {
            formType = RECEIVABLE_CREDIT_NOTE;
        } else if (SALES_ORDER_CATEGORY.equals(data.getMessageType())) {
            formType = SALE_ORDER;
        } else if (PROJECT_BASE_INVOICE_CATEGORY.equals(data.getMessageType())) {
            formType = PROJECT_BASED_INVOICE;
        }
        if (formType != null) {
            data.setTemplateData(getCompanyPdfTemplates(formType));
        }
        return data;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SendToFormFillingData getSendToFormData(SendToFormFillingData data, Integer contactId,
                                                   boolean isComposeForm, Integer formDataId) {
        ArrayList<RelationItem> relationItems = new ArrayList<>();
        data.setContacts(getContactsEmailAsSelectItem(data.getClientOrManagerID(), data.getMessageType(), contactId, isComposeForm));

        if (data.getClientOrManagerID() != null) {
            if (PURCHASE_ORDER_MANAGER_CATEGORY.equals(data.getMessageType()) || SALES_QUOTE_MANAGER_CATEGORY.equals(data.getMessageType()) || SALES_ORDER_CATEGORY.equals(data.getMessageType())) {
                EdsUser user = userManager.get(data.getClientOrManagerID());
                if (user != null) {
                    ContactItem item = new ContactItem();
                    item.setId(user.getObjectID());
                    item.setName(user.getEmail());
                    item.setContactName(user.getName());
                    data.setPrimaryContact(item);
                    relationItems.add(RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, user.getObjectID(), user.getName()));
                }
            } else {
                EdsCrmAccount clintOrSupplier = crmAccountManager.get(data.getClientOrManagerID());
                if (clintOrSupplier != null) {
                    relationItems.add(RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, clintOrSupplier.getObjectID(), clintOrSupplier.getName()));
                }
                if ((clintOrSupplier != null && clintOrSupplier.getPrimaryContact() != null) || REQUEST_FOR_QUOTE_CATEGORY.equals(data.getMessageType())) {
                    EdsCrmContact primaryContact;
                    if (REQUEST_FOR_QUOTE_CATEGORY.equals(data.getMessageType())) {
                        primaryContact = crmContactManager.get(contactId);
                    } else {
                        primaryContact = clintOrSupplier.getPrimaryContact();
                    }
                    if (primaryContact != null && !StringUtils.isEmpty(primaryContact.getPrimaryEmail())) {
                        ContactItem item = new ContactItem();
                        item.setId(primaryContact.getObjectID());
                        item.setName(primaryContact.getPrimaryEmail());
                        item.setContactName(primaryContact.getName());
                        item.setPrimaryContact(primaryContact.getPrimaryContact() != null ? primaryContact.getPrimaryContact() : false);
                        item.setHasAccess(primaryContact.isAccessEnabled());
                        data.setPrimaryContact(item);
                        relationItems.add(RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, primaryContact.getObjectID(), primaryContact.getName()));
                    }
                }
                if (formDataId != null) {
                    if (SALES_INVOICE_CATEGORY.equals(data.getMessageType())) {
                        EdsInvoice invoice = invoiceManager.get(formDataId);
                        if (invoice.getRelatedProject() != null) {
                            relationItems.add(RelationItem.newEventRelation(RelationItem.TYPE_PROJECT, invoice.getRelatedProject().getObjectID(), invoice.getRelatedProject().getName()));
                        }
                    }
                }
            }
        }
        data.setRelationItems(relationItems);

        String formType = null;
        if (SALES_INVOICE_CATEGORY.equals(data.getMessageType()) || RECURRING_INVOICE_CATEGORY.equals(data.getMessageType())) {
            formType = SALE_INVOICE;
        } else if (SALES_QUOTE_CATEGORY.equals(data.getMessageType()) || SALES_QUOTE_MANAGER_CATEGORY.equals(data.getMessageType())) {
            formType = SALE_QUOTE;
        } else if (PURCHASE_ORDER_CATEGORY.equals(data.getMessageType()) || PURCHASE_ORDER_MANAGER_CATEGORY.equals(data.getMessageType())) {
            formType = PURCHASE_ORDER;
        } else if (CREDIT_NOTE_CATEGORY.equals(data.getMessageType())) {
            formType = RECEIVABLE_CREDIT_NOTE;
        } else if (SALES_ORDER_CATEGORY.equals(data.getMessageType())) {
            formType = SALE_ORDER;
        } else if (PROJECT_BASE_INVOICE_CATEGORY.equals(data.getMessageType())) {
            formType = PROJECT_BASED_INVOICE;
        } else if (REQUEST_FOR_QUOTE_CATEGORY.equals(data.getMessageType())) {
            formType = RFQ;
        } else if (CUSTOMER_BALANCE_CATEGORY.equals(data.getMessageType()) || SUPPLIER_BALANCE_CATEGORY.equals(data.getMessageType())) {
            formType = CUSTOMER_SUPPLIER_BALANCE;
        } else if (RECEIVE_PAYMENT_CATEGORY.equals(data.getMessageType())) {
            formType = BATCH_RECEIVE_PAYMENT;
        }
        if (formType != null) {
            data.setTemplateData(getCompanyPdfTemplates(formType));
        }
        return data;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EdsReference getInvoiceCustomType(String customType) {
        return referenceManager.findReference(INVOICE_CUSTOM_TYPE, customType);
    }

    @Transactional
    public EdsInvoiceCustomFields createInvoiceCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsInvoiceCustomFields invoiceCustomFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                invoiceCustomFields = invoiceCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null
                            || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                invoiceCustomFields = new EdsInvoiceCustomFields();
                invoiceCFManager.create(invoiceCustomFields);
            }
            CustomFieldsUtils.setAccountingDomainObjectCustomFields(invoiceCustomFields, customFieldItems);
            return invoiceCustomFields;
        }
        return null;
    }

    public EdsInvoiceItemCustomFields createInvoiceItemCustomFields
            (List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsInvoiceItemCustomFields invoiceItemCustomFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                invoiceItemCustomFields = invoiceItemCFManager.get(customFieldItems.get(0).getObjectId());
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
                invoiceItemCustomFields = new EdsInvoiceItemCustomFields();
                invoiceItemCFManager.create(invoiceItemCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(invoiceItemCustomFields, customFieldItems);
            return invoiceItemCustomFields;
        }
        return null;
    }

    private void saveOverPayment(PaymentData overPaymentData) {
        EdsCrmAccount crmAccount = crmAccountManager.get(overPaymentData.getCrmAccount().getId());
        EdsOverPayment overPayment;
        if (overPaymentData.getObjectID() != null) {
            overPayment = overPaymentManager.get(overPaymentData.getObjectID());
        } else {
            overPayment = new EdsOverPayment();
            overPayment.setBatchPayment(batchPaymentManager.get(overPaymentData.getBatchPaymentID()));
        }

        overPayment.setAccount(accountingManager.get(overPaymentData.getPaymentAccount().getId()));
        overPayment.setOverPaymentAccount(accountingManager.get(overPaymentData.getOverPaymentAccount().getId()));
        if (overPaymentData.getCurrency() != null && overPaymentData.getCurrency().getId() != null) {
            overPayment.setCurrencyID(overPaymentData.getCurrency().getId());
            overPayment.setExchangeRate(overPaymentData.getExchangeRate());
        } else {
            overPayment.setExchangeRate(ONE);
        }
        if (overPaymentData.isPaymentDiffCurrency()) {
            overPayment.setAmountInEntityCurrency(overPaymentData.getOverPaymentAmount());
            overPayment.setAmount(overPaymentData.getOverPaymentAmount().divide(overPaymentData.getExchangeRate(), financialSettingsManager.getFinancialSettings().getExchangeRateScale(), RoundingMode.HALF_UP));
        } else {
            overPayment.setAmount(overPaymentData.getOverPaymentAmount());
        }
        overPayment.setCrmAccount(crmAccount);

        overPayment.setPaymentDate(overPaymentData.getDate().getNonConvertedDate());
        overPayment.setReference(overPaymentData.getReferenceNumber());
        overPayment.setUser(invoiceManager.getUser());
        overPayment.setType(overPaymentData.getType());
        overPaymentManager.createOrUpdate(overPayment);

        accountingServiceLocal.createTransactionForOverPayment(overPayment);

    }

    @Deprecated
    @Override
    public ProjectAllocateData getCrmAccountProjectBalance(ProjectAllocateData data) {
        return data;
    }

    @Deprecated
    @Override
    public void saveCrmAccountProjectBalance(ProjectAllocateData data) {

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ShippingMethod> getShippingMethodData(ListingFilterParameter filterParametrs) {
        List<EdsShippingMethod> list = shippingMethodManager.getShippingMethodsByCompanyID(filterParametrs);
        Integer totalCount = shippingMethodManager.listCount(filterParametrs);
        ArrayList<ShippingMethod> results = new ArrayList<>();
        for (EdsShippingMethod item : list) {
            ShippingMethod result = new ShippingMethod();
            result.setId(item.getObjectID());
            result.setName(item.getName());
            result.setDescription(item.getDescription());
            result.setPrice(item.getPrice());
            if (item.getVat() != null) {
                result.setTaxItem(item.getVat().createTaxItem());
            }
            results.add(result);
        }
        return new ListResult<>(results, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ShippingMethodsList getShippingMethodList() {
        List<EdsShippingMethod> list = shippingMethodManager.getShippingMethodsByCompanyID(new ListingFilterParameter());
        if (list != null) {
            ArrayList<ShippingMethod> shippingMethodList = new ArrayList<>(list.size());
            list.forEach(shippingMethod -> shippingMethodList.add(shippingMethod.toTO()));
            return new ShippingMethodsList(shippingMethodList.toArray(new ShippingMethod[]{}), shippingMethodList.size());
        }
        return new ShippingMethodsList();
    }

    public ShippingMethod saveShippingMethod(ShippingMethod shippingMethod) {
        if (shippingMethod != null) {
            EdsShippingMethod item = new EdsShippingMethod();
            EdsVat vat = new EdsVat();
            if (shippingMethod.getId() != null) {
                item = shippingMethodManager.get(shippingMethod.getId());
            }
            item.setName(shippingMethod.getName());
            item.setDescription(shippingMethod.getDescription());
            item.setPrice(shippingMethod.getPrice());
            item.setExchangeRate(shippingMethod.getExchangeRate());

            if (shippingMethod.getAccount() != null) {
                EdsAccount account = accountingManager.get(shippingMethod.getAccount().getId());
                item.setAccount(account);
            } else {
                item.setAccount(null);
            }

            if (shippingMethod.getTaxItem() != null) {
                vat.setObjectID(shippingMethod.getTaxItem().getId());
                vat.setName(shippingMethod.getTaxItem().getName());
                vat.setTaxType(shippingMethod.getTaxItem().getTaxType());
                vat.setUpdatedDate(new Date());
                item.setVat(vat);
            } else {
                item.setVat(null);
            }

            List<EdsCrmAccount> customerList = new ArrayList<>();
            if (shippingMethod.getAppliedClients() != null) {
                for (SelectItem items : shippingMethod.getAppliedClients()) {
                    EdsCrmAccount crmAccount = crmAccountManager.get(items.getId());
                    customerList.add(crmAccount);
                }
            }

            item.setCustomers(customerList);

            if (shippingMethod.getCurrencyId() != null) {
                item.setCurrency(currencyManager.get(shippingMethod.getCurrencyId()));
            } else {
                item.setCurrency(null);
            }

            if (shippingMethod.getId() != null) {
                shippingMethodManager.update(item);
            } else {
                shippingMethodManager.create(item);
            }
            shippingMethod.setId(item.getObjectID());
            return shippingMethod;
        }

        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ShippingMethod getShippingMethod(Integer shippingMethodID, Integer clientId) {
        List<SelectItem> items;
        ShippingMethod result = new ShippingMethod();

        EdsAccount systemShippingAccount = accountingManager.getAccountByKey(EdsAccount.DISTRIBUTION_CARRIAGE);
        if (systemShippingAccount != null) {
            result.setAccount(systemShippingAccount.getAsSelectItem());
        }

        if (shippingMethodID != null) {
            EdsShippingMethod shippingMethod = shippingMethodManager.get(shippingMethodID);
            if (shippingMethod != null && !shippingMethod.getDeleted()) {
                result.setName(shippingMethod.getName());
                result.setId(shippingMethod.getObjectID());
                result.setDescription(shippingMethod.getDescription());
                result.setPrice(shippingMethod.getPrice());
                result.setCurrencyId(shippingMethod.getCurrency() != null ? shippingMethod.getCurrency().getObjectID() : null);
                result.setExchangeRate(shippingMethod.getExchangeRate());

                if (shippingMethod.getAccount() != null) {
                    result.setAccount(shippingMethod.getAccount().getAsSelectItem());
                }
                if (shippingMethod.getVat() != null) {
                    result.setTaxItem(shippingMethod.getVat().createTaxItem());
                }
                if (shippingMethod.getCustomers() != null && shippingMethod.getCustomers().size() > 0) {
                    items = new ArrayList<>();
                    for (EdsCrmAccount account : shippingMethod.getCustomers()) {
                        items.add(account.getAsSelectItem());
                    }
                    result.setAppliedClients(items.toArray(new SelectItem[]{}));
                }
            }
        } else if (clientId != null) {
            EdsCrmAccount client = crmAccountManager.get(clientId);
            result.setCurrencyId(client.getCurrency() != null ? client.getCurrency().getObjectID() : null);
        }
        result.setExchangeRate(result.getExchangeRate() != null ? result.getExchangeRate() : BigDecimal.ONE);
        result.setCurrencyId(result.getCurrencyId() != null ? result.getCurrencyId() : getBaseCurrency().getId());

        return result;
    }

    @Override
    public ArrayList<NewInvoice> getSaleQuoteByClient(ListingFilterParameter fp) {

        List<Integer> statusIds = new ArrayList<>();
        ArrayList<NewInvoice> invoiceList = new ArrayList<>();

        statusIds.add(super.getInvoiceStatus(CLIENT_APPROVE).getObjectID());

        if (fp.isInvoicesOnly()) {
            statusIds.add(super.getInvoiceStatus(SALE_ORDER).getObjectID());
            statusIds.add(super.getInvoiceStatus(PICKED).getObjectID());
            statusIds.add(super.getInvoiceStatus(PACKED).getObjectID());
//            statusIds.add(super.getInvoiceStatus(SHIPPED).getObjectID()); TODO DO NOT INCLUDE SHIPPED and PARTIAL_SHIPPED
        }
        List<EdsSaleQuote> quotes = quoteManager.getQuotesByClient(fp, statusIds, genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_MULTI_QUOTE_CONVERT));
        for (EdsSaleQuote q : quotes) {
            NewInvoice item = EdsQuote.getQuoteData(q);//new NewInvoice();
            item.setID(q.getObjectID());
            item.setQuoteNumber(q.getNumber());
            item.setPoNumber(q.getPoNumber());
            item.setInvoiceDate(new DateNonConvertable(q.getInvoiceDate()));

            if (q.getStatus().getCode().equals(SALE_ORDER)
                    || q.getStatus().getCode().equals(PICKED)
                    || q.getStatus().getCode().equals(PACKED)) {
                item.setStatus(commonLocalizer.localize("ORDER", ORDER));
            } else {
                item.setStatus(commonLocalizer.localize("QUOTE", QUOTE));
            }
            item.setProgressInvoicing(q.isProgressInvoicing());
            item.setProgressInvoicingType(q.getProgressInvoicingType());
            item.setConvertedPercent(q.getConvertedPercent());
            item.setConvertedAmount(q.getConvertedAmount());
            item.setTotal(q.getTotal());
            item.setItems(q.wrapToNewItem());
            invoiceList.add(item);
        }

        return invoiceList;
    }

    @Override
    public ArrayList<BillableExpenseItem> getBillableExpensesByClient(ListingFilterParameter fp) {
        ArrayList<BillableExpenseItem> beList = new ArrayList<>();

        List<EdsExpense> expenses = expenseManager.getBillableExpenses(fp);
        for (EdsExpense exp : expenses) {
            beList.add(exp.createBillableExpenseItem());
        }

        List<EdsInvoiceItem> purchaseInvoiceItemsAsExpense = invoiceItemManager.getBillableExpense(fp);
        for (EdsInvoiceItem exp : purchaseInvoiceItemsAsExpense) {
            beList.add(exp.createBillableExpenseItem());
        }

        List<EdsBankTransferItem> bankTransferItemsAsExpense = spendReceiveMoneyItemManager.getBillableExpense(fp);
        for (EdsBankTransferItem exp : bankTransferItemsAsExpense) {
            beList.add(exp.createBillableExpenseItem());
        }

        List<EdsManualJournalItem> manualTransactionItemsAsExpense = manualJournalItemManager.getBillableExpense(fp);
        for (EdsManualJournalItem exp : manualTransactionItemsAsExpense) {
            beList.add(exp.createBillableExpenseItem());
        }

        List<EdsBankCheckItem> checkItemsAsExpense = bankCheckItemManager.getBillableExpense(fp);
        for (EdsBankCheckItem exp : checkItemsAsExpense) {
            beList.add(exp.createBillableExpenseItem());
        }
        return beList;
    }

    @Override
    public NewInvoice[] getPurchaseOrderBySupplier(Integer supplierID) {
        Map<String, Integer> statusId = new HashMap<>();

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setInvoiceClientId(supplierID);

        statusId.put(RECEIVED, super.getInvoiceStatus(RECEIVED).getObjectID());
        statusId.put(PARTIAL_RECEIVED, super.getInvoiceStatus(PARTIAL_RECEIVED).getObjectID());

        List<EdsPurchaseOrder> orders = quoteManager.getPurchaseOrderBySupplier(fp, statusId);
        NewInvoice item;
        List<NewInvoice> items = new ArrayList<>();
        boolean p;
        for (EdsPurchaseOrder order : orders) {
            /*p = true;
            if (order.getInvoices().size() > 0) {
                for (EdsInvoice invoice : order.getInvoices()) {
                    if (!invoice.isDeleted()) {
                        p = false;
                        break;
                    }
                }
            }*/
            if (CollectionUtils.isEmpty(order.getInvoices())) {
                item = new NewInvoice();
                item.setID(order.getObjectID());
                item.setPoNumber(order.getNumber());
                item.setInvoiceDate(new DateNonConvertable(order.getInvoiceDate()));
                item.setStatus(PURCHASE_O);
                item.setTotal(order.getTotal());
                items.add(item);
            }
        }
        return items.toArray(new NewInvoice[0]);
    }

    @Override
    public NewInvoice[] getInvoicesByConvertedQuote(Integer quoteID) {
        List<EdsSaleInvoice> invoices = invoiceManager.getSalesInvoicesByConvertedItem(quoteID);
        NewInvoice[] items = new NewInvoice[invoices.size()];
        int i = 0;
        for (EdsSaleInvoice inv : invoices) {
            items[i] = new NewInvoice();
            items[i].setID(inv.getObjectID());
            items[i].setInvoiceNumber(inv.getNumber());
            items[i].setInvoiceDate(new DateNonConvertable(inv.getInvoiceDate()));
            items[i].setDueDate(new DateNonConvertable(inv.getDueDate()));
            items[i].setInvoiceNumber(inv.getNumber());
            items[i].setConvertedPercent(inv.getQuotePercent());
            items[i].setTotal(inv.getTotal());
            items[i].setSubtotal(inv.getSubtotal());
            items[i].setTotalInInvoiceCurrency(inv.getTotalInInvoiceCurrency());
            items[i].setItems(inv.getItemsAsNewInvoiceItem());
            items[i].setClientID(inv.getClient() != null ? inv.getClient().getObjectID() : null);
            items[i].setClientContactID(inv.getClientContact() != null ? inv.getClientContact().getObjectID() : null);
            items[i].setBillAddressID(inv.getBillAddressID());
            items[i].setMailAddressID(inv.getMailAddressID());
            i++;
        }
        return items;
    }

    @Override
    public ProductSerialItem[] getProductSerials(ListingFilterParameter filterParametrs) {
        List<EdsProductSerial> serialsList = productSerialManager.getProductSerials(filterParametrs);
        ProductSerialItem[] serialItems = new ProductSerialItem[serialsList.size()];
        int i = 0;
        EdsUser user = userManager.getUser();
        String format = ServerUtils.getShortDateFormat(user);
        String datePattern = ServerUtils.getShortDateFormat(user);
        for (EdsProductSerial ps : serialsList) {
            ProductSerialItem serialItem = new ProductSerialItem();
            serialItem.setObjectID(ps.getObjectID());
            if (ps.getExpirationDate() != null) {
                serialItem.setSerial(ps.getSerial() + (user != null ? " (" + ServerUtils.dateFormat(ServerUtils.convertServerDateToUserDate(ps.getExpirationDate(), user.getUserTimezone()), datePattern) + ")" : ""));
            } else {
                serialItem.setSerial(ps.getSerial());
            }
            serialItem.setExpirationDate(ps.getExpirationDate());
            serialItem.setLotNumber(ps.getLotNumber());
            serialItem.setRefNumber(ps.getRefNumber());
            serialItems[i++] = serialItem;
        }
        return serialItems;
    }

    @Override
    public ArrayList<ProductTrackBatchItem> getProductTrachBatches(ListingFilterParameter filterParametrs) {
        if (filterParametrs.getWarehouseID() == null) {
            EdsWarehouse warehouse = warehouseManager.getDefaultWarehouse();
            filterParametrs.setWarehouseID(warehouse.getObjectID());
        }
        List<Object> batches = itemBatchManager.getBatchesOnHand(filterParametrs);
        ArrayList<ProductTrackBatchItem> resultList = Lists.newArrayList();
        if (batches != null && !batches.isEmpty()) {
            int i = 0;
            for (Object item : batches) {
                i++;
                Object[] it = (Object[]) item;
                String serial = (String) it[0];
                Date expiryDate = (Date) it[1];
                BigDecimal onHand = (BigDecimal) it[2];
//                BigDecimal cost = it[3] != null ? (BigDecimal) it[3] : null;

                ProductTrackBatchItem batchItem = new ProductTrackBatchItem();
                batchItem.setObjectID(i);
                batchItem.setSerial(serial);
                batchItem.setExpirationDate(expiryDate);
                batchItem.setBalanceInbatch(onHand);
//                batchItem.setCost(cost);
                resultList.add(batchItem);
            }
        }

        return resultList;
    }

    @Override
    public void updateSaleInvoicesAfterExportSaasu(Integer objectId, Date lastUpdateDate, String
            saasuLastUpdatedUid, Integer saasuGUID) {
        EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(objectId);
        if (saleInvoice != null) {
            if (saasuGUID != null) {
                saleInvoice.setSaasuGUID(saasuGUID.toString());
            }
            saleInvoice.setSasuuLastUpdatedTime(lastUpdateDate);
            saleInvoice.setSaasuLastUpdatedUid(saasuLastUpdatedUid);
            invoiceManager.update(saleInvoice);
        }
    }

    @Override
    public InvoiceTermsItem saveInvoiceTerms(InvoiceTermsItem invoiceTermsData) {
        EdsInvoiceTerms edsInvoiceTerms;
        if (invoiceTermsData.getId() != null) {
            edsInvoiceTerms = invoiceTermsManager.get(invoiceTermsData.getId());
        } else {
            edsInvoiceTerms = new EdsInvoiceTerms();
        }
        edsInvoiceTerms.setName(invoiceTermsData.getName());
        edsInvoiceTerms.setDays(invoiceTermsData.getDays());
        invoiceTermsManager.createOrUpdate(edsInvoiceTerms);

        invoiceTermsData.setId(edsInvoiceTerms.getObjectID());

        return invoiceTermsData;
    }

    @Override
    public Integer applySupplierCreditData(ReceivePaymentData appliedPrePaymentData) {

        List<EdsBankCheckPaymentHistory> checkPaymentHistoryList = new LinkedList<>();
        PaymentData[] bankCheckItemsPaymentData = appliedPrePaymentData.getPayments();
        int calculationScale = financialSettingsManager.getFinancialSettings().getAccountingCalculationScale();

        Integer paymentID = null;
        for (PaymentData bcipData : bankCheckItemsPaymentData) {
            EdsBankCheckPaymentHistory checkPaymentHistory = null;
            if (bcipData.getBankCheckItem() != null && bcipData.getBankCheckItem().getId() != null) {
                EdsBankCheckItem bankCheckItem = bankCheckManager.getBankCheckItem(bcipData.getBankCheckItem().getId());
                BigDecimal amount = bcipData.getBaseAmount();
                EdsBankCheck bankCheck = bankCheckItem.getBankCheck();

                if (bankCheck.getExchangeRate() != null && !bcipData.getCurrency().getId().equals(bankCheck.getCurrency().getObjectID())) {
                    amount = amount.divide(bcipData.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP)
                            .multiply(bankCheck.getExchangeRate()).setScale(calculationScale, RoundingMode.HALF_UP);
                }
                bankCheckItem.setUsedAsPayment(bankCheckItem.getUsedAsPayment().add(amount));
                checkPaymentHistory = new EdsBankCheckPaymentHistory();
                checkPaymentHistory.setBankCheckItem(bankCheckItem);
                checkPaymentHistory.setAmount(amount);
                checkPaymentHistoryList.add(checkPaymentHistory);
            }

            paymentID = savePayment(bcipData);

            if (checkPaymentHistory != null) {
                EdsInvoicePayment invoicePayment = invoicePaymentManager.get(paymentID);
                checkPaymentHistory.setInvoicePayment(invoicePayment);
                bankCheckPaymentHistoryManager.create(checkPaymentHistory);
            }
        }

        return paymentID;
    }

    @Override
    public ArrayList<MyUpdateItem> getAllHistory(Integer invoiceID, String viewType) {

        ArrayList<EdsMyUpdate> myUpdates = new ArrayList<>();
        Integer relationID = invoiceID;
        String relationType = null;

        switch (viewType) {
            case Constants.SALE_INVOICE -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.SALES_INVOICE);
                relationType = RelationItem.TYPE_SALEINVOICE;
            }
            case Constants.RECURRING_INVOICE ->
                    myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.RECURRING_INVOICE);
            case Constants.SALE_QUOTE -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.SALES_QUOTE);
                relationType = RelationItem.TYPE_SALEQUOTE;
            }
            case Constants.PURCHASE_ORDER -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.PURCHASE_ORDER);
                relationType = RelationItem.TYPE_PURCHASE_ORDER;
            }
            case Constants.PURCHASE_INVOICE -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.PURCHASE_INVOICE);
                relationType = RelationItem.TYPE_PURCHASE_INVOICE;
            }
            case Constants.SALE_ORDER -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.SALES_ORDER);
                relationType = RelationItem.TYPE_SALEQUOTE;
            }
            case Constants.RECEIVABLE_CREDIT_NOTE -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.SALES_INVOICE);
                myUpdates = getRefunds(invoiceID, myUpdates, MyUpdateTypeManager.SALES_INVOICE);
                relationType = RelationItem.TYPE_SALEINVOICE;
            }
            case Constants.PAYABLE_CREDIT_NOTE -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.PURCHASE_INVOICE);
                myUpdates = getRefunds(invoiceID, myUpdates, MyUpdateTypeManager.PURCHASE_INVOICE);
                relationType = RelationItem.TYPE_PURCHASE_INVOICE;
            }
            case Constants.FIXED_ASSETS ->
                    myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.FIXED_ASSET);
            case Constants.MANUAL_JOURNAL -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.ACCOUNTING_MANUAL_JOURNAL);
                List<EdsInvoicePayment> manualJournalAppliedPayments = invoicePaymentManager.getInvoicePaymentByManualJournal(invoiceID);
                if (manualJournalAppliedPayments != null && manualJournalAppliedPayments.size() > 0) {
                    for (EdsInvoicePayment appliedPayment : manualJournalAppliedPayments) {
                        myUpdates.addAll(myUpdateManager.getUpdatesForAffectedID(appliedPayment.getObjectID(), MyUpdateTypeManager.MANUAL_JOURNAL_APPLIED));
                    }
                }
                relationType = RelationItem.TYPE_MANUAL_JOURNAL;
            }
            case Constants.REQUEST_FOR_QUOTE -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.ACCOUNTING_REQUEST_FOR_QUOTE);
                relationType = RelationItem.TYPE_REQUEST_FOR_QUOTE;
            }
            case CrmConstants.REQUEST_FOR_PURCHASE -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.ACCOUNTING_REQUEST_FOR_PURCHASE);
                relationType = RelationItem.REQUEST_FOR_PURCHASE;
            }
            case CrmConstants.STOCK_TRANSFER -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.ACCOUNTING_STOCK_TRANSFER);
                relationType = RelationItem.TYPE_STOCK_TRANSFER;
            }
            case CrmConstants.STOCK_ADJUSTMENT -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.ACCOUNTING_STOCK_ADJUSTMENT);
                relationType = RelationItem.TYPE_STOCK_ADJUSTMENT;
            }
            case Constants.EXPENSE_REPORT -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.EXPENSE_REPORT);
                relationType = RelationItem.TYPE_EXPENSE_CLAIM;
            }
            case Constants.BANK_TRANSFER -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.BANK_TRANSFER);
                List<EdsInvoicePayment> bankTransferAppliedPayments = invoicePaymentManager.getInvoicePaymentByBankTransfer(invoiceID);
                if (bankTransferAppliedPayments != null && bankTransferAppliedPayments.size() > 0) {
                    for (EdsInvoicePayment appliedPayment : bankTransferAppliedPayments) {
                        myUpdates.addAll(myUpdateManager.getUpdatesForAffectedID(appliedPayment.getObjectID(), MyUpdateTypeManager.BANK_TRANSFER_APPLIED));
                    }
                }
                relationType = RelationItem.TYPE_BANK_TRANSFER;
            }
            case MyUpdateTypeManager.BATCH_PAYMENT -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.BATCH_PAYMENT);
                relationType = RelationItem.TYPE_BATCH_PAYMENT;
            }
            case MyUpdateTypeManager.INVOICE_PAYMENT -> {
                myUpdates = myUpdateManager.getUpdatesForAffectedID(invoiceID, MyUpdateTypeManager.INVOICE_PAYMENT);
                relationType = RelationItem.TYPE_PRE_PAYMENT;
            }
        }

        ArrayList<MyUpdateItem> result = new ArrayList<>();
        if (myUpdates.size() > 0) {
            EdsUser user = myUpdateTypeManager.getUser();
            for (EdsMyUpdate myUpdate : myUpdates) {

                MyUpdateItem item = new MyUpdateItem();
                item.setType(myUpdate.getEventType());
                item.setEventDate(myUpdate.getDate());
                item.setLink(myUpdateTypeManager.getUpdatesLink(myUpdate));
                myUpdateTypeManager.getMyUpdateMessageAllHistory(myUpdate, item, !user.getObjectID().equals(myUpdate.getReceiver()), true);

                result.add(item);
            }
        }

        if (relationType != null) {
            List<MyUpdateItem> attachmentUpdates = myUpdateManager.getAttachmentUpdates(relationID, relationType);
            if (attachmentUpdates != null && attachmentUpdates.size() > 0) {
                result.addAll(attachmentUpdates);
            }
        }

        result.sort(Comparator.comparing(MyUpdateItem::getEventDate));

        return result;
    }

    @Override
    public List<HistoryNote> getHistoryByHistoryNote(Integer invoiceID, String viewType) {
        List<HistoryNote> result = new ArrayList<>();
        result.addAll(getAllHistory(invoiceID, viewType));
        return result;
    }

    private ArrayList<EdsMyUpdate> getRefunds(Integer invoiceID, ArrayList<EdsMyUpdate> myupdats, String updateType) {
        EdsInvoice invoice = invoiceManager.get(invoiceID);
        List<EdsInvoicePayment> payments = invoicePaymentManager.getRefundsAll(invoice);
        for (EdsInvoicePayment payment : payments) {
            List<EdsMyUpdate> myupdate = myUpdateManager.getCreditNoteRefundUpdatesForAffectedID(payment.getObjectID(), updateType);
            myupdats.addAll(myupdate);
        }
        return myupdats;
    }

    @Override
    public ListResult<InvoiceTermsItem> getInvoiceTermsList(ListingFilterParameter filterParametrs) {
        List<EdsInvoiceTerms> invoiceTerms = invoiceTermsManager.getInvoiceTerms(filterParametrs);
        ArrayList<InvoiceTermsItem> itemsList = new ArrayList<>();
        for (EdsInvoiceTerms iTerm : invoiceTerms) {
            itemsList.add(iTerm.getAsRPC());
        }
        return new ListResult<>(itemsList, invoiceTermsManager.getInvoiceTermsCount(filterParametrs));
    }

    @Override
    public InvoiceTermsItem getInvoiceTerm(Integer termID) {
        return invoiceTermsManager.get(termID).getAsRPC();
    }

    @Override
    public Boolean deleteInvoiceTerm(Integer termID) {
        EdsInvoiceTerms edsTerm = invoiceTermsManager.get(termID);
        edsTerm.setDeleted(true);
        invoiceTermsManager.update(edsTerm);
        return true;
    }

    public ListResult<AgingSummaryItem> getOverdueInvoiceByCrmAccount(ListingFilterParameter filter) {
        ArrayList<AgingSummaryItem> result = new ArrayList<>();
        Integer calculationScale = financialSettingsManager.getFinancialSettings().getAccountingCalculationScale();
        EdsUser user = userManager.getUser();
        LinkedHashMap<Integer, ArrayList<AgingSummaryInvoiceItem>> map = null;
        if (filter.getStartDateNC() != null) {
            filter.setDate(ServerUtils.parseFilterParameterDate(filter.getStartDateNC()));
        }
        if (filter.getClientId() == null && (user.hasRole(roleManager.getByCode(SUPPLIER)) && user instanceof EdsClientContact)) {
            filter.setClientId(user.getClientContact().getClientID());
        }
        if (filter.isShowBudget()) {
            map = accountingManager.getClientSupplierBalanceForAgingDetails(filter);
        } else {
            map = accountingManager.getClientSupplierBalanceForAging(filter);
        }

        Map<Integer, ArrayList<AgingSummaryInvoiceItem>> finalMap = map;
        ArrayList<AgingSummaryItem> list = map.keySet().stream().filter(key -> key != null && key != 0 && finalMap.get(key) != null).map(key -> {
            AgingSummaryInvoiceItem ii = finalMap.get(key).get(0);

            AgingSummaryItem item = new AgingSummaryItem();
            item.setCustomerOrSupplier(ii.getCustomerOrSupplierName());
            item.setCustomerOrSupplierObjectId(ii.getClientOrSupplierId());
            item.setAccountType(ii.getAccountType());
            item.setInvoiceList(finalMap.get(key));
            return item;
        }).collect(Collectors.toCollection(ArrayList::new));

        if (map.get(0) != null) {
            AgingSummaryItem item = new AgingSummaryItem();
            item.setCustomerOrSupplier("N/A");
            item.setCustomerOrSupplierObjectId(0);
            item.setAccountType("N/A");
            item.setInvoiceList(map.get(0));
            list.add(item);
        }
        filtrateZeroValues(filter, calculationScale, list);
        int totalCount = list.size();
        if (filter.isFromListing()) {
            int endRange = filter.getStart() + filter.getLimit();
            int limit = Math.min(endRange, list.size());
            result.addAll(list.subList(filter.getStart(), limit));
        } else {
            result = list;
        }
        return new ListResult<>(result, totalCount);
    }

    private void filtrateZeroValues(ListingFilterParameter filter, Integer calculationScale, ArrayList<AgingSummaryItem> list) {
        int columnCount;
        if (filter.getIntervalLimit() % filter.getInterval() == 0) {
            columnCount = filter.getIntervalLimit() / filter.getInterval() + 4;
        } else {
            columnCount = filter.getIntervalLimit() / filter.getInterval() + 5;
        }
        ArrayList<AgingSummaryItem> removable = new ArrayList<>();
        for (AgingSummaryItem item : list) {
            BigDecimal total = ZERO;
            for (int i = 1, j = -1; i < columnCount - 1; i++, j++) {
                BigDecimal balance = ZERO;
                Integer start = j * filter.getInterval();
                Integer in = (j + 1) * filter.getInterval();

                if (item.getInvoiceList() != null && !item.getInvoiceList().isEmpty()) {
                    for (AgingSummaryInvoiceItem inv : item.getInvoiceList()) {
                        if (start > filter.getIntervalLimit()) {
                            start = filter.getIntervalLimit();
                        }
                        if (in > filter.getIntervalLimit()) {
                            in = filter.getIntervalLimit();
                        }
                        if ((inv.getAging() > j * filter.getInterval() && inv.getAging() <= in) || (inv.getAging() > filter.getIntervalLimit() && i == columnCount - 2) || (i == 1 && inv.getAging() == 0)) {
                            balance = balance.add(inv.getAmount());
                        }
                    }
                }
                total = total.add(balance);
            }
            if (total.setScale(calculationScale, RoundingMode.HALF_UP).compareTo(ZERO) == 0) {
                removable.add(item);
            }
        }
        list.removeAll(removable);
    }

//    public AgingReportItem getOverdueInvoicesForReport(String type, Date startDate, Date endDate, Integer interval, boolean isDetails, Integer clientOrSupplierID) { // TODO remove???
//        List<AgingSummaryItem> result = new ArrayList<>();
//        ListingFilterParameter filter = new ListingFilterParameter();
//        filter.setAccountType(type);
//        filter.setClientId(clientOrSupplierID);
//        filter.setExcludePrePayments(false);
//        filter.setDate(endDate);
//        filter.setType(interval);
//        Map<Integer, List<AgingSummaryInvoiceItem>> map = !isDetails ? accountingManager.getClientSupplierBalanceForAging(filter) : accountingManager.getClientSupplierBalanceForAgingDetails(filter);
//
//        for (Integer key : map.keySet()) {
//            if (key != 0 && map.get(key) != null) {
//                AgingSummaryInvoiceItem ii = map.get(key).get(0);
//
//                AgingSummaryItem item = new AgingSummaryItem();
//                item.setCustomerOrSupplier(ii.getCustomerOrSupplierName());
//                item.setCustomerOrSupplierObjectId(ii.getClientOrSupplierId());
//                item.setInvoiceList(map.get(key));
//                result.add(item);
//            }
//        }
//        ListingFilterParameter fp = new ListingFilterParameter();
//        fp.setStart(0);
//        fp.setLimit(0);
//        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
//        String formattedStartDate = dformat.format(startDate);
//        String formattedEndDate = dformat.format(endDate);
//        ArrayList<Integer> idList = new ArrayList<>();
//        idList.add(clientOrSupplierID);
//        BigDecimal beginningBalance = transactionManager.getCrmAccountEarlyBalance(idList, formattedStartDate, CrmAccountItem.CUSTOMER, null, true);
//        BigDecimal endingBalance = BigDecimal.ZERO;
//        List<CrmAccountBalanceItem> items = transactionManager.getCrmAccountBalance(idList, formattedStartDate, formattedEndDate, CrmAccountItem.CUSTOMER, null, true, fp);
//        for (CrmAccountBalanceItem bi : items) {
//            endingBalance = endingBalance.add(bi.getAmount());
//        }
//        endingBalance = beginningBalance.add(transactionManager.getCrmAccountPrevPageBalance(idList, formattedStartDate, formattedEndDate, CrmAccountItem.CUSTOMER, null, true, fp));
//
//        if (map.get(0) != null) {
//            AgingSummaryItem item = new AgingSummaryItem();
//            item.setCustomerOrSupplier("N/A");
//            item.setCustomerOrSupplierObjectId(0);
//            item.setInvoiceList(map.get(0));
//            result.add(item);
//        }
//
//        AgingReportItem reportItem = new AgingReportItem();
//        reportItem.setBeginningBalance(beginningBalance);
//        reportItem.setEndingBalance(endingBalance);
//        reportItem.setReports(result);
//        return reportItem;
//    }

    @Override
    public USPSPackage[] getUSPSRates(ShippingLabelData labelData) {

        if (AccountingConstants.POSTCARD.equals(labelData.getServiceType())) {
            labelData.setPounds(0);
            labelData.setOunces(1);
        }

        List<USPSPackage> packageList = new LinkedList<>();
        if (AccountingConstants.POSTCARD.equals(labelData.getServiceType())) {
            packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.FIRST_CLASS, labelData, "REGULAR"));
            packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.FIRST_CLASS, labelData, "LARGE"));
        } else {

            if (AccountingConstants.LETTER.equals(labelData.getServiceType()) || AccountingConstants.LARGE_ENVELOPE.equals(labelData.getServiceType()) || AccountingConstants.PACKAGE.equals(labelData.getServiceType())) {
                packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.EXPRESS, labelData, "REGULAR"));
                packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.EXPRESS_HFP, labelData, "REGULAR"));
                packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.PRIORITY, labelData, "REGULAR"));
                packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.PRIORITY_HFP_COMMERCIAL, labelData, "REGULAR"));
                if (AccountingConstants.LETTER.equals(labelData.getServiceType()) || AccountingConstants.LARGE_ENVELOPE.equals(labelData.getServiceType())) {
                    packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.FIRST_CLASS, labelData, "REGULAR"));
                } else if (AccountingConstants.PACKAGE.equals(labelData.getServiceType())) {
                    packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.FIRST_CLASS, labelData, "REGULAR", "PARCEL"));
                    packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.PARCEL, labelData, "REGULAR"));
                    packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.MEDIA, labelData, "REGULAR"));
                }
            }

            if (AccountingConstants.LARGE_PACKAGE.equals(labelData.getServiceType())) {
                packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.EXPRESS, labelData, "LARGE"));
                packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.EXPRESS_HFP, labelData, "LARGE"));
                packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.PRIORITY, labelData, "LARGE"));
                packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.PRIORITY_HFP_COMMERCIAL, labelData, "LARGE"));

                packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.FIRST_CLASS, labelData, "LARGE", "PARCEL"));
                packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.PARCEL, labelData, "LARGE"));
                packageList.add(USPSPackage.createUSPSPackage(SHIPPING_SERVICE.MEDIA, labelData, "LARGE"));
            }
        }

        String uspsUserID = genericSettingsManager.getValueByKey(GenericSettingsEnum.USPS_USER_ID);
        String uspsUserPassword = genericSettingsManager.getValueByKey(GenericSettingsEnum.USPS_USER_ID);
        USPSWebService uspsWebService = new USPSWebService("RateV4", genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.USPS_IS_TEST_SERVER), uspsUserID, uspsUserPassword);
        String responseXML = uspsWebService.submitRequestAndGetResponse(new USPSRates(packageList.toArray(new USPSPackage[]{})).toXML());

        return USPSWebService.parseRateAPIResponse(responseXML);
    }

    @Override
    public ShippingLabelData getShippingLabelData(Integer invoiceID) {
        ShippingLabelData shippingLabelData = new ShippingLabelData();

        EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(invoiceID);

        EdsCompany company = invoiceManager.getUser().getCompany();
        EdsAddress edsToAddress = addressManager.get(saleInvoice.getMailAddressID());

        shippingLabelData.setFromZip(company.getMailingPostCode());
        shippingLabelData.setToZip(edsToAddress.getZipCode());

        return shippingLabelData;
    }

    @Override
    public ShippingMethod[] getShippinhMethodsForLookUp(ListingFilterParameter filterParameter) {
        List<EdsShippingMethod> list = shippingMethodManager.getShippingMethodsByCustomer(filterParameter);
        ArrayList<ShippingMethod> results = new ArrayList<>();
        for (EdsShippingMethod item : list) {
            ShippingMethod result = new ShippingMethod();
            result.setId(item.getObjectID());
            result.setName(item.getName());
            result.setDescription(item.getDescription());
            result.setPrice(item.getPrice());
            result.setCurrencyId(item.getCurrency() != null ? item.getCurrency().getObjectID() : null);
            result.setExchangeRate(item.getExchangeRate());

            if (item.getVat() != null) {
                result.setTaxItem(item.getVat().createTaxItem());
            }
            results.add(result);
        }
        return results.toArray(new ShippingMethod[0]);
    }

    @Override
    public Boolean hasShippingMethod() {
        return shippingMethodManager.hasShippingMethod();
    }

    @Override
    public void updatePurchaseInvoiceByQB(NewInvoice newPurchaseInvoice, int synchItemId) {
        EdsPurchaseInvoice purchaseInvoice = invoiceManager.getPurchaseInvoiceByExternalGUID(newPurchaseInvoice.getInvoiceNumber());
        if (purchaseInvoice != null) {
            purchaseInvoice.setQuickbookInvoiceID(newPurchaseInvoice.getQuickbookInvoiceID());
            purchaseInvoice.setQuickbookEditSequence(newPurchaseInvoice.getQuickbookEditSequence());
            if (purchaseInvoice.getInvoiceItems() != null && purchaseInvoice.getInvoiceItems().size() > 0) {
                int i = 0;
                for (EdsInvoiceItem invoiceItem : purchaseInvoice.getInvoiceItems()) {
                    invoiceItem.setQuickbookItemID(newPurchaseInvoice.getItems()[i].getQbItemId());
                    i++;
                }
            }
            invoiceManager.update(purchaseInvoice);
        }
    }

    @Override
    public void updateSaleInvoiceByQB(NewInvoice newInvoice, int synchItemId) {
        EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoiceByExternalGUID(newInvoice.getInvoiceNumber());
        if (saleInvoice != null) {
            saleInvoice.setQuickbookInvoiceID(newInvoice.getQuickbookInvoiceID());
            saleInvoice.setQuickbookEditSequence(newInvoice.getQuickbookEditSequence());
            if (saleInvoice.getInvoiceItems() != null && saleInvoice.getInvoiceItems().size() > 0) {
                int i = 0;
                for (EdsInvoiceItem invoiceItem : saleInvoice.getInvoiceItems()) {
                    invoiceItem.setQuickbookItemID(newInvoice.getItems()[i].getQbItemId());
                    i++;
                }
            }
            invoiceManager.update(saleInvoice);
        }
    }

    public USPSDeliveryConfirmation getUSPSDeliveryConfirmation(Integer invoiceID, String serviceName, String
            fromZipCode, String toZipCode, String weightInOunces) {

        EdsUser edsUser = invoiceManager.getUser();
        EdsCompany company = edsUser.getCompany();

        EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(invoiceID);
        EdsAddress mailAddress = addressManager.get(saleInvoice.getMailAddressID());

        USPSDeliveryConfirmation deliveryConfirmation = new USPSDeliveryConfirmation();

        EdsCrmContact primaryContact = saleInvoice.getClient().getPrimaryContact();
        if (primaryContact == null) {
            deliveryConfirmation.setValidationStatus(USPSDeliveryConfirmation.PRIMARY_CONTACT_IS_NOT_EXIST);
            return deliveryConfirmation;
        }

        deliveryConfirmation.setOption("1");
        deliveryConfirmation.setFromName(edsUser.getName());
        deliveryConfirmation.setFromFirm(edsUser.getCompany().getName());
        deliveryConfirmation.setFromAddress1(company.getAddress2() != null ? company.getAddress2() : "");
        deliveryConfirmation.setFromAddress2(company.getMailAddress2() != null ? company.getMailAddress2() : "");
        deliveryConfirmation.setFromCity(company.getMailingCity() != null ? company.getMailingCity() : "");
        deliveryConfirmation.setFromState(company.getMailingCountryRegion() != null && company.getMailingCountryRegion().getCode() != null ? company.getMailingCountryRegion().getCode() : "");
        deliveryConfirmation.setFromZip5(fromZipCode);
        deliveryConfirmation.setFromZip4("");
        deliveryConfirmation.setToName(primaryContact.getName());
        deliveryConfirmation.setToFirm(primaryContact.getCrmAccount().getName());
        deliveryConfirmation.setToAddress1(mailAddress.getAddress() != null ? mailAddress.getAddress() : "");
        deliveryConfirmation.setToAddress2(mailAddress.getAddressb() != null ? mailAddress.getAddressb() : "");
        deliveryConfirmation.setToCity(mailAddress.getCity() != null ? mailAddress.getCity() : "");
        deliveryConfirmation.setToState(mailAddress.getState() != null && mailAddress.getState().getCode() != null ? mailAddress.getState().getCode() : "");
        deliveryConfirmation.setToZip5(toZipCode);
        deliveryConfirmation.setToZip4("");
        if ("0".equals(weightInOunces)) {
            weightInOunces = "1";
        }
        deliveryConfirmation.setWeightInOunces(weightInOunces);
        if (serviceName.startsWith("Express Mail")) {
            deliveryConfirmation.setServiceType("Express Mail");
        } else if (serviceName.startsWith("Priority Mail")) {
            deliveryConfirmation.setServiceType("Priority");
        } else if (serviceName.startsWith("First-Class")) {
            deliveryConfirmation.setServiceType("First Class");
        } else if (serviceName.startsWith("Parcel Post")) {
            deliveryConfirmation.setServiceType("Parcel Post");
        } else if (serviceName.startsWith("Media Mail")) {
            deliveryConfirmation.setServiceType("Media Mail");
        } else if (serviceName.startsWith("Library Mail")) {
            deliveryConfirmation.setServiceType("Library Mail");
        }

        deliveryConfirmation.setPoZipCode("");
        deliveryConfirmation.setImageType("PDF");
        deliveryConfirmation.setLabelDate("");
        deliveryConfirmation.setCustomerReferenceNo(saleInvoice.getNumber());
        deliveryConfirmation.setSenderName(edsUser.getName());
        deliveryConfirmation.setSenderEMail(edsUser.getEmail());
        deliveryConfirmation.setRecipientName(primaryContact.getName());
        deliveryConfirmation.setRecipientEMail(primaryContact.getPrimaryEmail() != null ? primaryContact.getPrimaryEmail() : "");

        return deliveryConfirmation;
    }

    public USPSExpressMailLabel getUSPSExpressMailLabel(Integer invoiceID, String fromZipCode, String
            toZipCode, String weightInOunces) {

        EdsUser edsUser = invoiceManager.getUser();
        EdsCompany company = edsUser.getCompany();

        EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(invoiceID);
        EdsAddress mailAddress = addressManager.get(saleInvoice.getMailAddressID());

        USPSExpressMailLabel expressMailLabel = new USPSExpressMailLabel();

        EdsCrmContact primaryContact = saleInvoice.getClient().getPrimaryContact();
        if (primaryContact == null) {
            expressMailLabel.setValidationStatus(USPSDeliveryConfirmation.PRIMARY_CONTACT_IS_NOT_EXIST);
            return expressMailLabel;
        }

        expressMailLabel.setFromFirstName(edsUser.getFirstName());
        expressMailLabel.setFromLastName(edsUser.getLastName());
        expressMailLabel.setFromFirm(edsUser.getCompany().getName());
        expressMailLabel.setFromAddress1(company.getAddress2() != null ? company.getAddress2() : "");
        expressMailLabel.setFromAddress2(company.getMailAddress2() != null ? company.getMailAddress2() : "");
        expressMailLabel.setFromCity(company.getMailingCity() != null ? company.getMailingCity() : "");
        expressMailLabel.setFromState(company.getMailingCountryRegion() != null && company.getMailingCountryRegion().getCode() != null ? company.getMailingCountryRegion().getCode() : "");
        expressMailLabel.setFromZip5(fromZipCode);
        expressMailLabel.setFromPhone(edsUser.getEmployee().getPrimaryPhone() != null ? edsUser.getEmployee().getPrimaryPhone() : "");
        expressMailLabel.setToFirstName(primaryContact.getFirstName());
        expressMailLabel.setToLastName(primaryContact.getLastName());
        expressMailLabel.setToFirm(primaryContact.getCrmAccount().getName());
        expressMailLabel.setToAddress1(mailAddress.getAddress() != null ? mailAddress.getAddress() : "");
        expressMailLabel.setToAddress2(mailAddress.getAddressb() != null ? mailAddress.getAddressb() : "");
        expressMailLabel.setToCity(mailAddress.getCity() != null ? mailAddress.getCity() : "");
        expressMailLabel.setToState(mailAddress.getState() != null && mailAddress.getState().getCode() != null ? mailAddress.getState().getCode() : "");
        expressMailLabel.setToZip5(toZipCode);
        expressMailLabel.setToPhone(primaryContact.getPrimaryPhone() != null ? primaryContact.getPrimaryPhone() : "");

        if ("0".equals(weightInOunces)) {
            weightInOunces = "1";
        }

        expressMailLabel.setCustomerReferenceNo(saleInvoice.getNumber());
        expressMailLabel.setSenderName(edsUser.getName());
        expressMailLabel.setSenderEMail(edsUser.getEmail());
        expressMailLabel.setRecipientName(primaryContact.getName());
        expressMailLabel.setRecipientEMail(primaryContact.getPrimaryEmail() != null ? primaryContact.getPrimaryEmail() : "");

        return expressMailLabel;
    }

    @Override
    public Integer createInvoiceFromCourseBooking(Integer keyID) {
        Integer invoiceID = createInvoiceFromCourseBooking(keyID, APPROVE);
        tcServiceLocal.updateCourseBookingStatus(keyID, Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()), BOOKING_PAID);
        return invoiceID;
    }

    public Integer createInvoiceFromCourseBooking(Integer courseBookingID, String status) {
        EdsCourseBooking courseBooking = courseBookingManager.get(courseBookingID);

        if (courseBooking.getStatus() != null && BOOKING_PAID.equals(courseBooking.getStatus().getCode())) {
            return MastercardPaymentHandler.COURSE_BOOKING_ALREADY_PAID;
        }
        EdsUser edsUser = courseBooking.getCreator();
        List<StudentAsInvoiceItem> items = new ArrayList<>();
        for (EdsCourseScheduleStudent student : courseBooking.getStudents()) {
            items.add(student.createStudentAsInvoiceItem(student));
        }
        return createDailyInvoiceForCustomerByCustomerStaff(edsUser.getUserDate(), courseBooking.getCustomer().getObjectID(), items, courseBooking.getInvoiceID(), edsUser.getObjectID(), true);
    }

    @Override
    public String updateInvoiceFields(InvoiceFieldsUpdaterDto dto) {
        EdsSaleInvoice edsInvoice = (EdsSaleInvoice) invoiceManager.get(dto.getId());

        Optional.ofNullable(dto.getReference()).ifPresent(edsInvoice::setReference);
        Optional.ofNullable(dto.getNumber()).ifPresent(edsInvoice::setNumber);
        Optional.ofNullable(dto.getDueDate()).ifPresent(edsInvoice::setDueDate);
        Optional.ofNullable(dto.getTaxCalcType()).ifPresent(edsInvoice::setTaxCalculationType);

        try {
            invoiceManager.update(edsInvoice);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            saleInvoiceSolrComponent.index(edsInvoice);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }

        return "Successfully updated!";
    }

    @Override
    public Integer createDailyInvoiceForCustomerByCustomerStaff(Date date, Integer
            customerID, List<StudentAsInvoiceItem> studentAsInvoiceItemList, Integer invoiceID) {
        return createDailyInvoiceForCustomerByCustomerStaff(date, customerID, studentAsInvoiceItemList, invoiceID, null, false);
    }

    @Override
    public Integer createDailyInvoiceForCustomerByCustomerStaff(Date date, Integer
                                                                        customerID, List<StudentAsInvoiceItem> studentAsInvoiceItemList, Integer invoiceID, Integer userID,
                                                                boolean isCash) {
        EdsUser edsUser = userID != null ? userManager.get(userID) : null;

        if (edsUser == null) {
            edsUser = userManager.getUser();
        }

        EdsSaleInvoice saleInvoice = new EdsSaleInvoice();

        if (invoiceID != null) {
            saleInvoice = (EdsSaleInvoice) invoiceManager.get(invoiceID);

            try {
                invoiceManager.deleteInvoiceItems(saleInvoice.getObjectID());
            } catch (Exception e) {
                log.info("Can't delete invoice items. Invoice ID: " + saleInvoice.getObjectID());
            }
        } else {
            Calendar invoiceDate = Calendar.getInstance();
            invoiceDate.setTime((Date) date.clone());
            ServerUtils.setBeginningOfTheDay(invoiceDate);
            saleInvoice.setInvoiceDate(invoiceDate.getTime());

            //generate invoice due date
            Calendar dueDate = new GregorianCalendar();
            dueDate.setTime((Date) date.clone());
            dueDate.add(Calendar.MONTH, 1);
            dueDate.set(Calendar.DATE, dueDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            ServerUtils.setEndOfTheDay(dueDate);
            saleInvoice.setDueDate(dueDate.getTime());

            generateAndSetSaleInvoiceNumber(saleInvoice);

            String customFieldCode = genericSettingsManager.getValueByKey(GenericSettingsEnum.DYNAMIC_CUSTOM_FIELD);
            if (customFieldCode != null && !customFieldCode.isEmpty()) {
                EdsCompanyCustomFieldsSettings customFieldsSettings = companyCFSettingsManager.getByColumnCode(ViewName.SaleInvoice.name(), customFieldCode);

                if (customFieldsSettings != null) {
                    CompanyCustomFieldItem customFieldItem = customFieldsSettings.getRPC(null);
                    customFieldItem.setObjectId(null);
                    customFieldItem.setFieldStringValue(isCash ? CASH_STR : CREDIT_STR);
                    saleInvoice.setCustomFields(createInvoiceCustomFields(Collections.singletonList(customFieldItem)));
                }
            }
        }


        EdsCrmAccount customer = crmAccountManager.get(customerID);
        saleInvoice.setClient(customer);

        EdsCrmAccount parentCustomer = customer.getParent();
        if (parentCustomer != null && parentCustomer.getObjectID().equals(33) && "31287".equals(ServerSecurityContext.getInstance().getCompanyId())) { //this is for only "Knowledge Grid LLC" company
            saleInvoice.setContact(parentCustomer.getPrimaryContact() != null ? parentCustomer.getPrimaryContact() : customer.getPrimaryContact());
        } else {
            saleInvoice.setContact(customer.getPrimaryContact());
        }

        List<EdsAddress> billAddressList = customer.getBillingAddresses();
        List<EdsAddress> mailAddressList = customer.getMailingAddresses();
        if (billAddressList != null && billAddressList.size() > 0) {
            saleInvoice.setBillAddressID(billAddressList.get(0).getObjectID());
        }
        if (mailAddressList != null && mailAddressList.size() > 0) {
            saleInvoice.setMailAddressID(mailAddressList.get(0).getObjectID());
        }

        saleInvoice.setCurrency(invoiceCircularResolver.returnBaseCurrency(edsUser.getCompany()));
        saleInvoice.setExchangeRate(BigDecimal.ONE);

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<EdsInvoiceItem> itemList = new ArrayList<>();
        for (StudentAsInvoiceItem studentAsInvoiceItem : studentAsInvoiceItemList) {
            EdsCourseScheduleStudent temp = courseScheduleStudentManager.get(studentAsInvoiceItem.getObjectID());
            if (temp.getAttendedStatus() != null && Constants.STUDENT_NO_SHOW.equals(temp.getAttendedStatus().getCode())) {
                if (temp.getCourseBooking() != null && temp.getCourseBooking().getCustomer() != null) {
                    studentAsInvoiceItem.includedInInvoice = false;
                    continue;
                }
            }
            EdsInvoiceItem invoiceItem = new EdsInvoiceItem();
            invoiceItem.setItemName(studentAsInvoiceItem.getFullName());
            invoiceItem.setDescription(studentAsInvoiceItem.getItemDescription());
            invoiceItem.setUnitPrice(studentAsInvoiceItem.getPrice().add(studentAsInvoiceItem.getStopFee()));
            invoiceItem.setQty(BigDecimal.ONE);
            invoiceItem.setAccount(accountingManager.getAccountTypeWithMinCode(EdsAccountType.SALES));
            invoiceItem.setNet(invoiceItem.getUnitPrice());
            invoiceItem.setAmmount(invoiceItem.getNet());
            invoiceItem.setInvoice(saleInvoice);
            itemList.add(invoiceItem);

            totalAmount = totalAmount.add(invoiceItem.getNet());
        }

        saleInvoice.setInvoiceItems(itemList);
        saleInvoice.setSubtotal(totalAmount);
        saleInvoice.setTotal(totalAmount);
        saleInvoice.setTotalInInvoiceCurrency(totalAmount);
        saleInvoice.setTotalDiscount(BigDecimal.ZERO);
        saleInvoice.setTotalTaxes(BigDecimal.ZERO);
        saleInvoice.setBillExpTotal(BigDecimal.ZERO);
        saleInvoice.setTaxCalculationType(2);
        saleInvoice.setStatus(referenceManager.findReference(INVOICE_STATUS, APPROVE));
        saleInvoice.setType(RECEIVABLE);
        saleInvoice.setCreator(edsUser);

        if (invoiceID == null) {
            saleInvoice.setCreationDate(new Date());
        }

        saleInvoice.setUpdatedDate(new Date());

        if (invoiceID == null) {
            invoiceManager.create(saleInvoice);
        } else {
            invoiceManager.update(saleInvoice);
        }

        //create transaction for approved invoice
        accountingServiceLocal.createTransactionsForInvoice(saleInvoice, edsUser);
        addSaleInvoiceToSolr(saleInvoice);
        return saleInvoice.getObjectID();
    }


    public InvoiceNumberData generateAndGetSaleInvoiceNumber(EdsCompany company) {
        InvoiceNumberData numberData = getSaleInvoiceNumber();
        Integer fourDigitNumber = Integer.parseInt(numberData.getFourDigitNumber());
        DecimalFormat format = new DecimalFormat("0000");
        while (isSaleInvoiceExists(numberData.getInvoiceNumber())) {
            System.out.println("Sales Invoice with number " + numberData.getInvoiceNumber() + " already exists");
            fourDigitNumber = fourDigitNumber + 1;
            numberData.setFourDigitNumber(format.format(fourDigitNumber));
        }
        return numberData;
    }

    private void registerInterCompanySalesTransaction(NewInvoice data, EdsInvoice invoice, EdsUser user) {

        if (!invoice.isRegisteredInterCompanyTransaction()
                && (APPROVE.equals(data.getStatusCode()) || OPEN.equals(data.getStatusCode()))
                && invoice.getClientOrSupplier() != null && invoice.getClientOrSupplier().getSubsidiary() != null) {
            invoice.setRegisteredInterCompanyTransaction(Boolean.TRUE);

            //Creating Intercompany Transaction
            InterCompanyDataMQ dataMQ = new InterCompanyDataMQ();
            data.setCompanyID(user.getCompany().getObjectID());
            dataMQ.setCrmAccountItem(getCompanyAsCrmAccount(user));
            dataMQ.setTransaction(data);
            dataMQ.setProducts(productServiceLocal.getInterCompanyTransactionProducts(data.getItems()));
            dataMQ.setDiscounts(getInterCompanyTransactionDiscounts(data.getItems()));
            dataMQ.setTaxes(getInterCompanyTransactionTaxes(data.getItems()));
            dataMQ.setAccounts(accountingServiceLocal.getInterCompanyTransactionAccounts(dataMQ.getProducts()));

            if (invoice.getClientOrSupplier().getSubsidiary() != null) {
                rabbitMQService.sendInterCompanySales(dataMQ, invoice.getClientOrSupplier().getSubsidiary().getCompanyId());
            }
        }
    }

    private List<TaxData> getInterCompanyTransactionTaxes(NewInvoiceItem[] invoiceItems) {
        List<TaxData> taxList = new LinkedList<>();
        HashMap<Integer, Integer> taxIDsMap = new HashMap<>();
        if (invoiceItems != null) {
            for (NewInvoiceItem nii : invoiceItems) {
                if (nii.getTaxItem() != null && nii.getTaxItem().getId() != null && nii.getItemID() != null && !taxIDsMap.containsKey(nii.getItemID())) {
                    TaxData taxData = getTax(nii.getTaxItem().getId());
                    taxList.add(taxData);
                    taxIDsMap.put(nii.getTaxItem().getId(), nii.getTaxItem().getId());
                }
            }
        }
        return taxList;
    }


    private List<DiscountItem> getInterCompanyTransactionDiscounts(NewInvoiceItem[] invoiceItems) {
        List<DiscountItem> discountList = new LinkedList<>();
        HashMap<Integer, Integer> discountIDsMap = new HashMap<>();
        if (invoiceItems != null) {
            for (NewInvoiceItem nii : invoiceItems) {
                if (nii.getItemDiscountID() != null && nii.getItemDiscountID() > 0 && !discountIDsMap.containsKey(nii.getItemDiscountID())) {
                    DiscountItem discount = discountService.getDiscountData(nii.getItemDiscountID());
                    discount.setAppliedClients(new SelectItem[]{});
                    discount.setAppliedProductIDs(new Integer[]{});
                    discount.setProductList(new DiscountAppliesItem[]{});
                    discountList.add(discount);
                    discountIDsMap.put(nii.getItemDiscountID(), nii.getItemDiscountID());
                }
            }
        }
        return discountList;
    }

    private CrmAccountItem getCompanyAsCrmAccount(EdsUser edsUser) {
        EdsCompany edsCompany = edsUser.getCompany();
        EdsFinancialSettings edsFinancialSettings = financialSettingsManager.getFinancialSettings();
        CrmAccountItem crmAccountItem = new CrmAccountItem();
        //crmAccountItem.setOwnerID(edsUser.getObjectID());
        //crmAccountItem.setOwnerName(edsUser.getName());
        crmAccountItem.setSelectedOwners(com.google.common.collect.Lists.newArrayList(new SelectItem(edsUser.getObjectID(), edsUser.getName())));

        crmAccountItem.setName(edsCompany.getName());
        crmAccountItem.setCurrencyId(edsFinancialSettings.getCurrency() != null ? edsFinancialSettings.getCurrency().getObjectID() : null);
        crmAccountItem.setSubsidiary(new SelectItem(edsCompany.getObjectID(), edsCompany.getName(), edsFinancialSettings.getCurrency().getObjectID().toString()));

        Address[] billingAddress = new Address[1];
        Address[] mailAddress = new Address[1];

        billingAddress[0] = new Address();
        billingAddress[0].setName(commonLocalizer.localize(PdfLocalizationName.billingAddress, "Billing Address"));
        billingAddress[0].setAddress(edsCompany.getAddress1());
        billingAddress[0].setAddressb(edsCompany.getBillAddress2());
        billingAddress[0].setCity(edsCompany.getCity());
        billingAddress[0].setPrimary(true);
        if (edsCompany.getCountryZone() != null) {
            billingAddress[0].setCountryId(edsCompany.getCountryZone().getCountry().getObjectID());
        }
        if (edsCompany.getCountryRegion() != null) {
            billingAddress[0].setStateId(edsCompany.getCountryRegion().getObjectID());
        }
        billingAddress[0].setZipCode(edsCompany.getPostCode());

        mailAddress[0] = new Address();
        mailAddress[0].setName("Mailing Address");
        mailAddress[0].setAddress(edsCompany.getAddress2());
        mailAddress[0].setAddressb(edsCompany.getMailAddress2());
        mailAddress[0].setCity(edsCompany.getMailingCity());
        mailAddress[0].setPrimary(true);
        if (edsCompany.getMailingCountry() != null) {
            mailAddress[0].setCountryId(edsCompany.getMailingCountry().getObjectID());
        }
        if (edsCompany.getMailingCountryRegion() != null) {
            mailAddress[0].setStateId(edsCompany.getMailingCountryRegion().getObjectID());
        }
        mailAddress[0].setZipCode(edsCompany.getMailingPostCode());

        crmAccountItem.setBillAddresses(billingAddress);
        crmAccountItem.setMailAddresses(mailAddress);

        return crmAccountItem;
    }

    @Override
    public HashMap<Integer, Integer> convertInterCompanyTaxes(List<TaxData> taxes) {
        HashMap<Integer, Integer> conversionIDSMap = new HashMap<>();
        if (taxes != null && taxes.size() > 0) {
            for (TaxData taxData : taxes) {
                EdsVat edsTax = vatManager.getTaxByNameAndPercent(taxData.getTaxName(), taxData.getTaxRate());
                if (edsTax != null) {
                    conversionIDSMap.put(taxData.getObjectId(), edsTax.getObjectID());
                } else {
                    Integer externalTaxID = taxData.getObjectId();
                    taxData.setObjectId(null);
                    Integer taxID = accountingServiceLocal.saveTaxRate(taxData).getId();
                    if (taxID != null && taxID > 0) {
                        edsTax = vatManager.get(taxID);
                        conversionIDSMap.put(externalTaxID, edsTax.getObjectID());
                    } else {
                        return null;
                    }
                }
            }
        }
        return conversionIDSMap;
    }

    @Override
    public HashMap<Integer, Integer> convertInterCompanyDiscounts(List<DiscountItem> discounts) {
        HashMap<Integer, Integer> conversionIDSMap = new HashMap<>();
        if (discounts != null && discounts.size() > 0) {
            for (DiscountItem discountItem : discounts) {
                EdsDiscount edsDiscount = discountManager.getDiscountByDiscountItem(discountItem);
                if (edsDiscount != null) {
                    conversionIDSMap.put(discountItem.getId(), edsDiscount.getObjectID());
                } else {
                    Integer externalDiscountID = discountItem.getId();
                    discountItem.setId(null);
                    Integer discountID = discountService.save(discountItem);
                    if (discountID != null && discountID > 0) {
                        edsDiscount = discountManager.get(discountID);
                        conversionIDSMap.put(externalDiscountID, edsDiscount.getObjectID());
                    } else {
                        return null;
                    }
                }
            }
        }
        return conversionIDSMap;
    }

    @Override
    public void saveInterCompanySales(InterCompanyDataMQ
                                              data, HashMap<Integer, Integer> productConversionIDs, HashMap<Integer, Integer> discountConversionIDs, HashMap<Integer, Integer> taxConversionIDs) {
        data.getTransaction().setNumberData(new InvoiceNumberData());
        data.getTransaction().setInvoiceNumber("");
        data.getTransaction().setStatusCode(INVOICE_STATUS_PENDING);

        EdsCurrency invoiceCurrency = currencyManager.get(data.getTransaction().getCurrencyID());
        BigDecimal newExRate = getExchangeRate(invoiceCurrency.getName());
        data.getTransaction().setExchageRate(newExRate);

        Integer calculationScale = financialSettingsManager.getFinancialSettings().getAccountingQtyCalculationScale();
        Integer stockAccountID = accountingManager.getStockAccountID();

        EdsAccount salesAccount = accountingManager.getAccountTypeWithMinCode(EdsAccountType.SALES);
        Integer salesAccountID = salesAccount != null ? salesAccount.getObjectID() : null;

        NewInvoiceItem[] invoiceItems = data.getTransaction().getItems();
        if (invoiceItems != null) {
            for (NewInvoiceItem invItem : invoiceItems) {
                /*CONVERTING RELATED IDS START*/
                if (invItem.getItemDiscountID() != null && invItem.getItemDiscountID() > 0) {
                    invItem.setItemDiscountID(discountConversionIDs.get(invItem.getItemDiscountID()));
                }
                if (invItem.getTaxItem() != null && invItem.getTaxItem().getId() != null && taxConversionIDs.get(invItem.getTaxItem().getId()) != null) {
                    EdsVat edsTax = vatManager.get(taxConversionIDs.get(invItem.getTaxItem().getId()));
                    invItem.setTaxItem(edsTax != null ? edsTax.createTaxItem() : null);
                }

                if (invItem.getItemID() != null && invItem.getItemID() > 0) {
                    invItem.setItemID(productConversionIDs.get(invItem.getItemID()));

                    if (PAYABLE.equals(data.getTransaction().getType())) {
                        invItem.setAccountID(salesAccountID);
                    } else {
                        invItem.setAccountID(stockAccountID);
                    }

                    EdsItem product = itemManager.get(invItem.getItemID());
                    if (product != null) {
                        if (PAYABLE.equals(data.getTransaction().getType())) {
                            if (product.getAccount() != null) {
                                invItem.setAccountID(product.getAccount().getObjectID());
                            }
                        } else {
                            if (product.getAssetAccount() != null) {
                                invItem.setAccountID(product.getAssetAccount().getObjectID());
                            }
                        }

                        if (product.getVat() != null) {
                            invItem.setTaxItem(product.getVat().createTaxItem());
                        }
                    }
                }
                /*CONVERTING RELATED IDS END*/
            }
        }
        BigDecimal taxTotal = BigDecimal.ZERO;
        TotalTaxItem[] totalTaxItems = data.getTransaction().getTotalTaxItems();

        if (totalTaxItems != null)
            for (TotalTaxItem totalTaxItem : totalTaxItems) {
                if (taxConversionIDs.get(totalTaxItem.getTaxItem().getId()) != null) {
                    EdsVat edsTax = vatManager.get(taxConversionIDs.get(totalTaxItem.getTaxItem().getId()));
                    totalTaxItem.setTaxItem(edsTax != null ? edsTax.createTaxItem() : null);
                    taxTotal = taxTotal.add(totalTaxItem.getTaxAmount());
                }
            }

        data.getTransaction().setTotalTaxItems(totalTaxItems);
        data.getTransaction().setShippingMethodID(null);
        data.getTransaction().setShippingPrice(BigDecimal.ZERO);
        data.getTransaction().setBillableExpenseAmount(BigDecimal.ZERO);
        data.getTransaction().setTotalInInvoiceCurrency(data.getTransaction().getSubtotal().subtract(data.getTransaction().getTotalDiscount()).add(TAX_CALCULATION_EXCLUSIVE.equals(data.getTransaction().getTaxCalculationType()) ? taxTotal : BigDecimal.ZERO));
        data.getTransaction().setTotal(newExRate.compareTo(ZERO) != 0 ? data.getTransaction().getTotalInInvoiceCurrency().divide(newExRate, calculationScale, RoundingMode.HALF_UP) : data.getTransaction().getTotalInInvoiceCurrency());
        data.getTransaction().setRegisteredInterCompanyTransaction(Boolean.TRUE);

        if (PAYABLE.equals(data.getTransaction().getType())) {
            data.getTransaction().setType(RECEIVABLE);
            data.getTransaction().setNumberData(getSaleInvoiceNumber());
            data.getTransaction().setInvoiceNumber(data.getTransaction().getNumberData().getInvoiceNumber());
            saveSaleInvoice(data.getTransaction()).getId();
        } else {
            data.getTransaction().setType(PAYABLE);

            EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(null);

            if (invSettings != null && invSettings.getIsPurchaseInvoiceNumberingShow()) {
                data.getTransaction().setNumberData(getPurchaseInvoiceNumber(false));
                data.getTransaction().setInvoiceNumber(data.getTransaction().getNumberData().getInvoiceNumber());
            }
            savePurchaseInvoice(data.getTransaction()).getId();
        }
    }

    private Integer generateAndSetSaleInvoiceNumber(EdsSaleInvoice saleInvoice) {
        InvoiceNumberData numberData = getSaleInvoiceNumber();
        Integer fourDigitNumber = Integer.parseInt(numberData.getFourDigitNumber());
        DecimalFormat format = new DecimalFormat("0000");
        while (isSaleInvoiceExists(numberData.getInvoiceNumber())) {
            System.out.println("Sales Invoice with number " + numberData.getInvoiceNumber() + " already exists");
            fourDigitNumber = fourDigitNumber + 1;
            numberData.setFourDigitNumber(format.format(fourDigitNumber));
        }
        saleInvoice.setNumber(numberData.getInvoiceNumber());
        saleInvoice.setFourDigitNumber(fourDigitNumber);
        return fourDigitNumber;
    }

    @Override
    public ArrayList<CompanyCustomFieldItem> saveBaseInvoiceCustomFields(String viewType, Integer
            objectID, ArrayList<CompanyCustomFieldItem> customFields) {
        ViewName customFieldTypeToGet = null;
        EdsInvoiceCustomFields edsCustomFields = null;
        if (SALE_QUOTE.equals(viewType) || SALE_ORDER.equals(viewType)) {
            EdsSaleQuote salesQuote = quoteManager.getSaleQuote(objectID);
            salesQuote.setCustomFields(createInvoiceCustomFields(customFields));
            quoteManager.update(salesQuote);
            customFieldTypeToGet = SALE_ORDER.equals(viewType) ? ViewName.SaleOrder : ViewName.SaleQuote;
            edsCustomFields = salesQuote.getCustomFields();

            EdsBusinessEvent event = null;
            if (SALE_ORDER.equals(viewType)) {
                event = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, salesQuote, userManager.getUser());
                event.setEntityType(RelationItem.TYPE_SALEORDER);
            }
        } else if (SALE_INVOICE.equals(viewType)) {
            EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(objectID);
            saleInvoice.setCustomFields(createInvoiceCustomFields(customFields));
            invoiceManager.update(saleInvoice);
            customFieldTypeToGet = ViewName.SaleInvoice;
            edsCustomFields = saleInvoice.getCustomFields();
        } else if (PURCHASE_ORDER.equals(viewType)) {
            EdsPurchaseOrder purchaseOrder = quoteManager.getPurchaseOrderByID(objectID);
            purchaseOrder.setCustomFields(createInvoiceCustomFields(customFields));
            quoteManager.update(purchaseOrder);
            customFieldTypeToGet = ViewName.PurchaseOrder;
            edsCustomFields = purchaseOrder.getCustomFields();
        } else if (PURCHASE_INVOICE.equals(viewType)) {
            EdsPurchaseInvoice purchaseInvoice = invoiceManager.getPurchaseInvoice(objectID);
            purchaseInvoice.setCustomFields(createInvoiceCustomFields(customFields));
            invoiceManager.update(purchaseInvoice);
            customFieldTypeToGet = ViewName.PurchaseInvoice;
            edsCustomFields = purchaseInvoice.getCustomFields();
        } else if (EXPENSE_REPORT.equals(viewType)) {
            EdsExpenseReport expenseReport = expenseReportManager.getExpenseReport(objectID);
            expenseReport.setCustomFields(createInvoiceCustomFields(customFields));
            expenseReportManager.update(expenseReport);
            customFieldTypeToGet = ViewName.ExpenceReportView;
            edsCustomFields = expenseReport.getCustomFields();
        } else if (RECEIVABLE_CREDIT_NOTE.equals(viewType)) {
            EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(objectID);
            saleInvoice.setCustomFields(createInvoiceCustomFields(customFields));
            invoiceManager.update(saleInvoice);
            customFieldTypeToGet = ViewName.SaleInvoice;
            edsCustomFields = saleInvoice.getCustomFields();
        } else if (PAYABLE_CREDIT_NOTE.equals(viewType)) {
            EdsPurchaseInvoice purchaseInvoice = invoiceManager.getPurchaseInvoice(objectID);
            purchaseInvoice.setCustomFields(createInvoiceCustomFields(customFields));
            invoiceManager.update(purchaseInvoice);
            customFieldTypeToGet = ViewName.PurchaseInvoice;
            edsCustomFields = purchaseInvoice.getCustomFields();
        } else if (RECEIVABLE.equals(viewType) || PAYABLE.equals(viewType)) {//receive payment/pay bill
            EdsBatchPayment batchPayment = batchPaymentManager.get(objectID);
            batchPayment.setCustomFields(createInvoiceCustomFields(customFields));
            batchPaymentManager.update(batchPayment);
            customFieldTypeToGet = RECEIVABLE.equals(viewType) ? ViewName.BatchInvoicePaymentView : ViewName.BatchPayBillView;
            edsCustomFields = batchPayment.getCustomFields();
        }

        if (edsCustomFields != null) {
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(customFieldTypeToGet);
            return CustomFieldsUtils.setRPCCustomFieldItems(edsCustomFields, customFieldsItems);
        }
        return null;
    }

    @Override
    public void removeUnbookkeepedInvoice(Integer invoiceID) {
        String type = "";
        EdsInvoice invoice = invoiceManager.get(invoiceID);
        if (invoice != null) {
            if (invoice instanceof EdsSaleInvoice) {
                type = SALE_INVOICE;
            } else if (invoice instanceof EdsRecurringInvoice) {
                type = RECURRING_INVOICE;
            } else if (invoice instanceof EdsPurchaseInvoice) {
                type = PURCHASE_INVOICE;
            }
            deleteInvoice(invoice.getObjectID(), type);
        }
    }

    @Override
    public void runPostDatedTransactions(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsCompany company = companyManager.get(companyID);
        TimeZone timeZone = null;
        if (company.getCountryZone() != null && company.getCountryZone().getZone() != null) {
            timeZone = TimeZone.getTimeZone(company.getCountryZone().getZone().getZoneID());
        }
        try {
            Date companyTime = getCompanyCurrentDate(timeZone);
            List<EdsInvoicePayment> postedDatePrePayments = invoicePaymentManager.getPostDatedPrePayments(companyTime);

            for (EdsInvoicePayment prePayment : postedDatePrePayments) {
                prePayment.setStatus(null);
                accountingServiceLocal.createTransactionForPayment(prePayment);
            }
        } catch (Exception ex) {
            log.error("running Pre-Payment Post Dated transaction failed for company " + companyID + "!!!", ex);
        }

        try {
            Date companyTime = getCompanyCurrentDate(timeZone);
            List<EdsBankCheck> postDatedCheckList = bankCheckManager.getPostDatedPreCheckList(companyTime);

            for (EdsBankCheck postDatedCheck : postDatedCheckList) {
                postDatedCheck.setPostDatedTransaction(false);
                accountingServiceLocal.createOrUpdateBankCheckTransaction(postDatedCheck);
            }
        } catch (Exception ex) {
            log.error("running Check Post Dated transaction failed for company " + companyID + "!!!", ex);
        }
    }

    @Override
    public ListResult<BatchPaymentListItem> getBatchPayments(ListingFilterParameter filterParameter) {
        ListPanelToolRpc panelTools = filterParameter.getListPanelTool();
        if (panelTools.isCustomFieldsShown()) {
            filterParameter.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            filterParameter.setCustomFieldsShown(true);
            panelTools.setListViewCustomFields(commonService.getCompanyCustomFieldsForListView(RECEIVABLE.equals(filterParameter.getDataType()) ? ViewName.BatchInvoicePaymentView : ViewName.BatchPayBillView));
            filterParameter.setColumnsOfListing(panelTools.getColumnCodeName());
        }
        ListResult<EdsBatchPayment> edsBatchPaymentList = batchPaymentManager.getBatchPayments(filterParameter);
        int totalCount = edsBatchPaymentList.getTotal();
        ArrayList<BatchPaymentListItem> resultList = new ArrayList<>();
        for (EdsBatchPayment edsBatchPayment : edsBatchPaymentList.getList()) {
            BatchPaymentListItem item = new BatchPaymentListItem();
            item.setObjectID(edsBatchPayment.getObjectID());
            item.setNumber(edsBatchPayment.getNumber());
            item.setCrmAccount(edsBatchPayment.getCrmAccount() != null ? edsBatchPayment.getCrmAccount().getAsSelectItem() : null);
            item.setAccount(edsBatchPayment.getAccount().getAsSelectItem());
            item.setDate(new DateNonConvertable(edsBatchPayment.getDate()));
            item.setReference(edsBatchPayment.getReference());
            item.setTotalAmount(edsBatchPayment.getTotalAmount());
            item.setCurrency(edsBatchPayment.getCurrency() != null ? edsBatchPayment.getCurrency().createCurrencyItem() : null);
            item.setProject(edsBatchPayment.getProject() != null ? edsBatchPayment.getProject().getName() : null);
            item.setCreator(edsBatchPayment.getCreator() != null ? edsBatchPayment.getCreator().getName() : null);
            item.setReversed(edsBatchPayment.getReversed());
            if (edsBatchPayment.getPaymentMethod() != null) {
                item.setPaymentMethod(edsBatchPayment.getPaymentMethod().getAsSelectItem());
            }
            if (edsBatchPayment.getCustomFields() != null && filterParameter.isCustomFieldsShown()) {
                item.setCustomFieldsMap(CustomFieldsUtils.getRPCCustomFields(edsBatchPayment.getCustomFields(), filterParameter.getColumnsOfListing()));
            }
            if (edsBatchPayment.getDepartment() != null) {
                item.setDepartment(edsBatchPayment.getDepartment().getName());
            }
            resultList.add(item);
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsBatchPayment.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get Batch Payment List");
        return new ListResult<>(resultList, totalCount);

    }

    @Override
    public Integer sendEmail(MessageItem messageItem) {
        EdsBatchPayment batchPayment = batchPaymentManager.get(messageItem.getInvoiceID());
        batchPayment.setEmailTemplateID(messageItem.getEmailTemplateID());
        batchPaymentManager.update(batchPayment);

        ByteArrayOutputStream pdfStream = batchReceivePaymentViewPDFHandler.getPDFStream(new TransactionPDFObject(messageItem.getInvoiceID(), messageItem.getPdfTemplateID(), Constants.RECEIVABLE, null));

        Email email = new Email(messageItem.getToEmails(), messageItem.getSubject(), messageItem.getMailContent());
        email.setFromEmail(messageItem.getFromEmail());
        email.setFromName(messageItem.getReplyTo());
        email.setCc(messageItem.getCc());
        email.setBcc(messageItem.getBcc());
        email.setIsInvisibleTrackerInSubject(true);
        email.setAttachments(messageItem.getFileResources());

        ByteArrayInputStream bais = new ByteArrayInputStream(pdfStream.toByteArray());

        EdsUpload upload = null;
        upload = new EdsUpload();
        upload.setContentType("application/pdf");
        upload.setOriginalName(batchPayment.getCreator().getCompany().getName() + "_" + batchPayment.getNumber() + ".pdf");
        upload.setType(referenceManager.findReference(_UPLOAD_TYPE, EdsContextParams.getUploadType()));
        upload.setInputStream(bais);
        uploadManager.create(upload);

        if (upload != null && upload.getObjectID() != null) {
            FileResource f = new FileResource();
            f.setBodyId(upload.getObjectID());
            f.setName(upload.getOriginalName());
            f.setContentLength(upload.getSize());
            f.setContentType(upload.getContentType());
            f.setUploadType(upload.getType() != null ? upload.getType().getName() : "");
            email.getAttachments().add(f);
        }

        Integer trackerID = messageCenterServiceLocal.sendMessage(email);

        try {
            pdfStream.flush();
            pdfStream.close();
        } catch (IOException ex) {
            log.error("Unable to close PDF Stream.", ex);
        }
        return trackerID;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmailTemplates(String templateCategory) {
        List<EdsEmailTemplate> emailTemplates = emailTemplateManager.getEmailTemplatesByCategory(templateCategory);
        EdsEmailTemplate defaultEmailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(templateCategory);

        if (defaultEmailTemplate == null) {
            defaultEmailTemplate = emailTemplateManager.getDefaultEmailTemplateByCategory(templateCategory);
        }

        ArrayList<SelectItem> itemsList = new ArrayList<>();

        if (defaultEmailTemplate != null) {
            itemsList.add(new SelectItem(defaultEmailTemplate.getObjectID(), defaultEmailTemplate.getName()));
        }

        for (EdsEmailTemplate emailTemplate : emailTemplates) {
            if (defaultEmailTemplate != null && !defaultEmailTemplate.equals(emailTemplate)) {
                itemsList.add(new SelectItem(emailTemplate.getObjectID(), emailTemplate.getName()));
            }
        }

        return itemsList.toArray(new SelectItem[]{});
    }

    @Override
    public ReceivePaymentData getBatchPaymentData(Integer objectID) {
        return getBatchPaymentData(objectID, false);
    }

    private ReceivePaymentData getBatchPaymentData(Integer objectID, boolean edit) {
        EdsBatchPayment edsBatchPayment = batchPaymentManager.get(objectID);
        ReceivePaymentData receivePaymentData = edsBatchPayment.getAsRPC();
        receivePaymentData.setLayoutHtml(PathFinder.getLayoutHTML(BATCH_PAYMENT_FORM));

        EdsOverPayment edsOverPayment = overPaymentManager.getOverPaymentByBatchPayment(objectID);
        if (edsOverPayment != null) {
            PaymentData overPaymentData = new PaymentData();
            overPaymentData.setObjectID(edsOverPayment.getObjectID());
            overPaymentData.setOverPaymentAccount(edsOverPayment.getOverPaymentAccount().getAsSelectItem());
            if (edsOverPayment.getAmountInEntityCurrency() != null) {
                overPaymentData.setOverPaymentAmount(edsOverPayment.getAmountInEntityCurrency());
            } else {
                overPaymentData.setOverPaymentAmount(edsOverPayment.getAmount());
            }
            receivePaymentData.setOverPayment(overPaymentData);
        }

        boolean isReceivable = RECEIVABLE.equals(receivePaymentData.getType());

        //Invoice
        List<PaymentData> invoicePayments = loadInvoicePayments(objectID, edit);
        List<PaymentData> payments = new ArrayList<>(invoicePayments);

        if (invoicePayments.size() == 1 && edsOverPayment == null) {
            EdsInvoicePayment invoicePayment = invoicePaymentManager.get(invoicePayments.get(0).getObjectID());
            if (invoicePayment != null) {
                EdsInvoicePaymentTransaction transaction = transactionManager.getTransactionByPayment(invoicePayment);
                if (transaction != null) {
                    receivePaymentData.setJournalID(transaction.getJournalId());
                }
                receivePaymentData.setPaymentStatus(invoicePayment.getPaymentStatus());
            }
        } else if (invoicePayments.size() > 1 || (invoicePayments.size() == 1 && edsOverPayment != null)) {
            receivePaymentData.setHasMultiTransaction(true);
        }
        payments.addAll(loadManualEntryPayments(objectID, edit));
        payments.addAll(loadExpensePayments(objectID, edit));

        receivePaymentData.setPayments(payments.toArray(new PaymentData[]{}));
        receivePaymentData.setBaseCurrency(invoiceCircularResolver.getBaseCurrency());
        receivePaymentData.setEnabledCurrencies(new CurrencyItem[]{edsBatchPayment.getCurrency().createCurrencyItem()});
        receivePaymentData.setEnableDepartment(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PAYMENT_DEPARTMENT_ENABLED));
        receivePaymentData.setPdfTemplateList(getCompanyPdfTemplatesByType(isReceivable ? PdfReferenceCodeNameEnum.BATCH_RECEIVE_PAYMENT.name() : PdfReferenceCodeNameEnum.BATCH_PAY_BILL.name()));
        EdsInvoiceCustomFields customFields = edsBatchPayment.getCustomFields();
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonServiceLocal.getCompanyCustomFields(isReceivable ? ViewName.BatchInvoicePaymentView : ViewName.BatchPayBillView);
        receivePaymentData.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(customFields, customFieldsItems));
        receivePaymentData.setSystemCustomFields(commonServiceLocal.getCompanyCustomFields(ViewName.BatchPayBillViewSystem));

        return receivePaymentData;
    }

    private List<PaymentData> loadExpensePayments(Integer objectId, boolean edit) {
        List<EdsExpensePayment> expensePayments = expensePaymentManager.findAllByBatchPaymentId(objectId);

        EdsBatchPayment edsBatchPayment = batchPaymentManager.get(objectId);
        List<PaymentData> payments = new ArrayList<>();
        for (EdsExpensePayment expensePayment : expensePayments) {

            EdsCrmAccount supplier = expensePayment.getSupplier();

            PaymentData expensePayment_ = new PaymentData();
            expensePayment_.setObjectID(expensePayment.getObjectID());
            expensePayment_.setInvoiceID(expensePayment.getExpenseReport().getObjectID());
            expensePayment_.setDate(new DateNonConvertable(expensePayment.getPaymentDate()));
            expensePayment_.setPaymentAmount(expensePayment.getAmount());
            expensePayment_.setPaymentAmountInInvoiceCurrency(expensePayment.getAmount());
            expensePayment_.setBaseAmount(expensePayment.getExpenseReport().getTotal());
            expensePayment_.setExchangeRate(edsBatchPayment.getExchangeRate());
            expensePayment_.setInvoiceNumber(expensePayment.getExpenseReport().getNumber());
            expensePayment_.setReferenceNumber(expensePayment.getReference());
            expensePayment_.setTotalInInvoiceCurrency(expensePayment.getExpenseReport().getTotal());
            expensePayment_.setTotal(expensePayment.getExpenseReport().getTotal());
            expensePayment_.setExpensePayment(true);
            if (supplier != null) {
                expensePayment_.setCrmAccount(supplier.getAsSelectItem());
            }

            //TODO under payment

            if (edit) {
//                expensePayment_.setTotalInInvoiceCurrency(expensePayment_.getTotalInInvoiceCurrency().add(expensePayment_.getPaymentAmountInInvoiceCurrency()));
//                expensePayment_.setTotal(expensePayment_.getTotal().add(expensePayment_.getPaymentAmount()));

                if (expensePayment.getExpenseReport() != null) {
                    if (expensePayment_.getUnderPaymentAmount() != null) {
                        expensePayment_.setTotal(expensePayment_.getTotal().add(expensePayment_.getUnderPaymentAmount()));
                    }
                    if (expensePayment_.getUnderPaymentAmountInInvoiceCurrency() != null) {
                        expensePayment_.setTotalInInvoiceCurrency(expensePayment_.getTotalInInvoiceCurrency().add(expensePayment_.getUnderPaymentAmountInInvoiceCurrency()));
                    }
                }
            }
            payments.add(expensePayment_);
        }
        return payments;
    }

    private List<PaymentData> loadManualEntryPayments(Integer objectId, boolean edit) {
        List<PaymentData> payments = new ArrayList<>();
        EdsBatchPayment edsBatchPayment = batchPaymentManager.get(objectId);
        ReceivePaymentData receivePaymentData = edsBatchPayment.getAsRPC();
        receivePaymentData.setLayoutHtml(PathFinder.getLayoutHTML(BATCH_PAYMENT_FORM));
        boolean isReceivable = RECEIVABLE.equals(receivePaymentData.getType());
        List<EdsCustomerSupplierPayment> edsCustomerSupplierPaymentList = customerSupplierPaymentManager.getBatchPaymentItems(objectId);
        CurrencyItem baseCurrency = getBaseCurrency();
        Integer currencyID = edsBatchPayment.getCurrency() != null ? edsBatchPayment.getCurrency().getObjectID() : baseCurrency.getId();
        boolean isMultiCurrencyEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTICURRENCY_ENABLED);
        for (EdsCustomerSupplierPayment customerSupplierPayment : edsCustomerSupplierPaymentList) {
            EdsCrmAccount edsCrmAccount = crmAccountManager.get(customerSupplierPayment.getCustomerSupplierID());

            PaymentData manualPayment = new PaymentData();
            manualPayment.setObjectID(customerSupplierPayment.getObjectID());
            manualPayment.setDate(new DateNonConvertable(customerSupplierPayment.getPaymentDate()));
            manualPayment.setPaymentAmount(customerSupplierPayment.getAmount());
            manualPayment.setPaymentAmountInInvoiceCurrency(customerSupplierPayment.getAmount());
            manualPayment.setExchangeRate(edsBatchPayment.getExchangeRate());
            manualPayment.setCrmAccount(edsCrmAccount.getAsSelectItem());

            EdsCustomerSupplierPayment underPayment = customerSupplierPaymentManager.getUnderPayment(customerSupplierPayment.getObjectID());
            if (underPayment != null) {
                manualPayment.setUnderPaymentID(underPayment.getObjectID());
                manualPayment.setUnderPaymentAccount(underPayment.getAccount().getAsSelectItem());
                manualPayment.setUnderPaymentAmount(underPayment.getAmount());
                manualPayment.setUnderPaymentAmountInInvoiceCurrency(underPayment.getAmountInEntityCurrency());
            }

            //Manual Transaction
            if (customerSupplierPayment.getManualJournalId() != null) {
                TransactionAllocateItem item = manualJournalManager.getPaidManualTransaction(customerSupplierPayment.getManualJournalId(), customerSupplierPayment.getCustomerSupplierID(), customerSupplierPayment.getAccountArAp().getObjectID(), isReceivable);

                if (item != null) {
                    BigDecimal paidAmount = customerSupplierPaymentManager.getManualPaymentsAmount(item.getObjectID(), customerSupplierPayment.getCustomerSupplierID(), isReceivable, item.getAccountID());
                    manualPayment.setInvoiceNumber((item.getNumber() != null ? item.getNumber() + (item.getNarration() != null ? ":" : "") : "") + (item.getNarration() != null ? item.getNarration() : ""));
                    manualPayment.setInvoiceID(item.getObjectID());
                    manualPayment.setManualJournal(true);
                    manualPayment.setInvoiceDate(new DateNonConvertable(item.getDate()));
                    manualPayment.setPaymentDiffCurrency(!currencyID.equals(item.getCurrencyID()));
                    manualPayment.setTotalInInvoiceCurrency(edit ? item.getAmount().subtract(paidAmount) : item.getAmount());
                    manualPayment.setTotal(edit ? item.getAmount().subtract(paidAmount) : item.getAmount());

                    if (isMultiCurrencyEnabled && !currencyID.equals(item.getCurrencyID())) {
                        if (baseCurrency.getId().equals(currencyID)) {
                            manualPayment.setPaymentAmountInInvoiceCurrency(manualPayment.getPaymentAmount().multiply(item.getExchangeRate()).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                            manualPayment.setTotal(manualPayment.getTotalInInvoiceCurrency().divide(item.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                        } else if (edsBatchPayment.getExchangeRate() != null) {
                            manualPayment.setPaymentAmountInInvoiceCurrency(manualPayment.getPaymentAmount().divide(edsBatchPayment.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                            manualPayment.setTotal(manualPayment.getTotalInInvoiceCurrency().multiply(edsBatchPayment.getExchangeRate()).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                        }
                    }
                    if (item.getAccountID() != null) {
                        manualPayment.setAccountItem(new SelectItem(item.getAccountID()));
                    }
                }

            }
            //Opening Balance
            if (customerSupplierPayment.getCustomerSupplierID() != null && customerSupplierPayment.getManualJournalId() == null) {
                EdsCrmAccount clientSupplier = (isReceivable ? clientManager.get(customerSupplierPayment.getCustomerSupplierID()) : crmAccountManager.get(customerSupplierPayment.getCustomerSupplierID()));
                Date balanceDate = isReceivable ? clientSupplier.getBalanceDate() : clientSupplier.getSupplierBalanceDate();
                BigDecimal balanceAmount = isReceivable ? clientSupplier.getBalanceAmount() : clientSupplier.getSupplierBalanceAmount();
                BigDecimal paidAmount = BigDecimal.ZERO;
                if (!customerSupplierPayment.isDeleted() && customerSupplierPayment.getManualJournalId() == null) {
                    paidAmount = paidAmount.add(customerSupplierPayment.getAmountInEntityCurrency() != null ? customerSupplierPayment.getAmountInEntityCurrency() : customerSupplierPayment.getAmount());
                }
                manualPayment.setInvoiceNumber("Opening Balance");
                manualPayment.setInvoiceID(-1);
                manualPayment.setOpeningBalance(true);
                manualPayment.setInvoiceDate(new DateNonConvertable(balanceDate));
                manualPayment.setPaymentDiffCurrency(!currencyID.equals(baseCurrency.getId()));
                manualPayment.setTotalInInvoiceCurrency(edit ? balanceAmount.subtract(paidAmount) : balanceAmount);
                manualPayment.setTotal(edit ? balanceAmount.subtract(paidAmount) : balanceAmount);

                if (isMultiCurrencyEnabled && !currencyID.equals(baseCurrency.getId())) {
                    manualPayment.setPaymentAmountInInvoiceCurrency(manualPayment.getPaymentAmount().multiply(edsBatchPayment.getExchangeRate()).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                    manualPayment.setTotal(manualPayment.getTotalInInvoiceCurrency().multiply(edsBatchPayment.getExchangeRate()).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                }
            }
            if (edit) {
                manualPayment.setTotalInInvoiceCurrency(manualPayment.getTotalInInvoiceCurrency().add(manualPayment.getPaymentAmountInInvoiceCurrency()));
                manualPayment.setTotal(manualPayment.getTotal().add(manualPayment.getPaymentAmount()));

                if (customerSupplierPayment.getManualJournalId() != null) {
                    if (manualPayment.getUnderPaymentAmount() != null) {
                        manualPayment.setTotal(manualPayment.getTotal().add(manualPayment.getUnderPaymentAmount()));
                    }
                    if (manualPayment.getUnderPaymentAmountInInvoiceCurrency() != null) {
                        manualPayment.setTotalInInvoiceCurrency(manualPayment.getTotalInInvoiceCurrency().add(manualPayment.getUnderPaymentAmountInInvoiceCurrency()));
                    }
                }
            }
            payments.add(manualPayment);
        }
        return payments;
    }

    private List<PaymentData> loadInvoicePayments(Integer objectId, boolean edit) {
        EdsBatchPayment edsBatchPayment = batchPaymentManager.get(objectId);
        ReceivePaymentData receivePaymentData = edsBatchPayment.getAsRPC();
        receivePaymentData.setLayoutHtml(PathFinder.getLayoutHTML(BATCH_PAYMENT_FORM));
        boolean isReceivable = RECEIVABLE.equals(receivePaymentData.getType());
        List<EdsInvoicePayment> edsInvoicePaymentList = invoicePaymentManager.getBatchPaymentItems(edsBatchPayment.getObjectID());
        CurrencyItem baseCurrency = getBaseCurrency();
        Integer currencyID = edsBatchPayment.getCurrency() != null ? edsBatchPayment.getCurrency().getObjectID() : baseCurrency.getId();
        boolean isMultiCurrencyEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTICURRENCY_ENABLED);
        List<PaymentData> payments = new ArrayList<>();
        for (EdsInvoicePayment edsInvoicePayment : edsInvoicePaymentList) {
            EdsCurrency invoiceCurrency = edsInvoicePayment.getInvoice() != null ? edsInvoicePayment.getInvoice().getCurrency() : edsBatchPayment.getCurrency();

            PaymentData invoicePayment = new PaymentData();
            invoicePayment.setObjectID(edsInvoicePayment.getObjectID());
            invoicePayment.setType(edsInvoicePayment.getType());
            if (edsInvoicePayment.getInvoice() != null) {
                EdsInvoicePayment invoiceUnderPayment = invoicePaymentManager.getInvoiceUnderPayment(edsInvoicePayment.getObjectID());
                if (invoiceUnderPayment != null) {
                    invoicePayment.setUnderPaymentID(invoiceUnderPayment.getObjectID());
                    invoicePayment.setUnderPaymentAccount(invoiceUnderPayment.getAccount().getAsSelectItem());
                    invoicePayment.setUnderPaymentAmount(invoiceUnderPayment.getAmount());
                    invoicePayment.setUnderPaymentAmountInInvoiceCurrency(invoiceUnderPayment.getAmountInInvoiceCurrency());
                    invoicePayment.setUnderPaymentTaxRate(invoiceUnderPayment.getUnderPaymentTaxRate());
                    invoicePayment.setUnderPaymentTaxAmount(invoiceUnderPayment.getUnderPaymentTaxAmount());
                }
            }
            if (RECEIVABLE_PREPAYMENT.equals(invoicePayment.getType()) || PAYABLE_SUPPLIER_CREDIT.equals(invoicePayment.getType())) {
                invoicePayment.setInvoiceID(edsInvoicePayment.getObjectID());
                invoicePayment.setInvoiceNumber(edsInvoicePayment.getNumber());
                invoicePayment.setCrmAccount(edsInvoicePayment.getCrmAccount() != null ? edsInvoicePayment.getCrmAccount().getAsSelectItem() : null);
                invoicePayment.setAppliedAmount(invoicePaymentManager.getAppliedPrePaymentAmounts(edsInvoicePayment.getCrmAccount().getObjectID(), edsInvoicePayment.getObjectID(), isReceivable ? RECEIVABLE_PREPAYMENT_SHARE : PAYABLE_SUPPLIER_CREDIT_SHARE, isReceivable ? RECEIVABLE_PREPAYMENT_REFUND : PAYABLE_PREPAYMENT_REFUND));
                invoicePayment.setIntNumber(edsInvoicePayment.getNumberInt());
                invoicePayment.setNumber(edsInvoicePayment.getNumber());
                invoicePayment.setPaymentStatus(edsInvoicePayment.getPaymentStatus());
            } else {
                invoicePayment.setInvoiceID(edsInvoicePayment.getInvoice().getObjectID());
                invoicePayment.setInvoiceNumber(edsInvoicePayment.getInvoice().getNumber());
                invoicePayment.setInvoiceDate(new DateNonConvertable(edsInvoicePayment.getInvoice().getInvoiceDate()));
                invoicePayment.setInvoiceDueDate(new DateNonConvertable(edsInvoicePayment.getInvoice().getDueDate()));
                invoicePayment.setEntityExchangeRate(edsInvoicePayment.getInvoice().getExchangeRate());
                invoicePayment.setTotal(edit ? edsInvoicePayment.getInvoice().getDueAmount() : edsInvoicePayment.getInvoice().getTotalInInvoiceCurrency());
                invoicePayment.setTotalInInvoiceCurrency(edit ? edsInvoicePayment.getInvoice().getDueAmount() : edsInvoicePayment.getInvoice().getTotalInInvoiceCurrency());
                invoicePayment.setCrmAccount(edsInvoicePayment.getInvoice().getClientOrSupplier().getAsSelectItem());
            }


            invoicePayment.setDate(new DateNonConvertable(edsInvoicePayment.getPaymentDate()));
            invoicePayment.setPaymentDiffCurrency(!currencyID.equals(invoiceCurrency.getObjectID()));
            invoicePayment.setExchangeRate(edsBatchPayment.getExchangeRate());
            invoicePayment.setPaymentAmount(edsInvoicePayment.getAmount());
            invoicePayment.setPaymentAmountInInvoiceCurrency(edsInvoicePayment.getAmountInInvoiceCurrency() != null ? edsInvoicePayment.getAmountInInvoiceCurrency() :
                    edsInvoicePayment.getAmount().multiply(edsBatchPayment.getExchangeRate()));


            if (isMultiCurrencyEnabled && !currencyID.equals(invoiceCurrency.getObjectID())) {
                if (baseCurrency.getId().equals(currencyID)) {
                    invoicePayment.setPaymentAmountInInvoiceCurrency(invoicePayment.getPaymentAmount().multiply(edsInvoicePayment.getInvoice().getExchangeRate()).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                    invoicePayment.setTotal(invoicePayment.getTotalInInvoiceCurrency().divide(edsInvoicePayment.getInvoice().getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                } else if (edsBatchPayment.getExchangeRate() != null) {
                    invoicePayment.setPaymentAmountInInvoiceCurrency(edsInvoicePayment.getAmountInInvoiceCurrency() != null ? edsInvoicePayment.getAmountInInvoiceCurrency() :
                            invoicePayment.getPaymentAmount().multiply(edsBatchPayment.getExchangeRate()));
                    invoicePayment.setTotal(invoicePayment.getTotalInInvoiceCurrency().multiply(edsBatchPayment.getExchangeRate()).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                }
            }

            if (edit && !(RECEIVABLE_PREPAYMENT.equals(invoicePayment.getType()) || PAYABLE_SUPPLIER_CREDIT.equals(invoicePayment.getType()))) {
                invoicePayment.setTotalInInvoiceCurrency(invoicePayment.getTotalInInvoiceCurrency().add(invoicePayment.getPaymentAmountInInvoiceCurrency()));
                invoicePayment.setTotal(invoicePayment.getTotal().add(invoicePayment.getPaymentAmount()));
                if (invoicePayment.getUnderPaymentAmount() != null) {
                    invoicePayment.setTotal(invoicePayment.getTotal().add(invoicePayment.getUnderPaymentAmount()));
                }
                if (invoicePayment.getUnderPaymentAmountInInvoiceCurrency() != null) {
                    invoicePayment.setTotalInInvoiceCurrency(invoicePayment.getTotalInInvoiceCurrency().add(invoicePayment.getUnderPaymentAmountInInvoiceCurrency()));
                }
            }
            invoicePayment.setReferenceNumber(edsInvoicePayment.getReference());
            payments.add(invoicePayment);
        }
        return payments;
    }

    @Override
    public ReceivePaymentData getBatchPaymentPdfData(Integer objectID) {
        EdsBatchPayment edsBatchPayment = batchPaymentManager.get(objectID);
        ReceivePaymentData receivePaymentData = edsBatchPayment.getAsRPC();
        boolean isReceivable = RECEIVABLE.equals(receivePaymentData.getType());
        receivePaymentData.setBaseCurrency(getBaseCurrency());
        if (receivePaymentData.getCrmAccountBillAddressId() != null) {
            EdsAddress edsAddress = addressManager.get(receivePaymentData.getCrmAccountBillAddressId());
            Address address = new Address();
            address.setName(edsAddress.getName());
            address.setAddress(edsAddress.getAddress());
            address.setAddressb(edsAddress.getAddressb());
            address.setCity(edsAddress.getCity());
            address.setState(edsAddress.getState() != null ? edsAddress.getState().getName() : null);
            address.setCountry(edsAddress.getCountry() != null ? edsAddress.getCountry().getName() : null);
            address.setZipCode(edsAddress.getZipCode());
            receivePaymentData.setCrmAccountAddress(address);
        }
        List<EdsInvoicePayment> edsInvoicePaymentList = invoicePaymentManager.getBatchPaymentItems(objectID);
        List<EdsCustomerSupplierPayment> edsCustomerSupplierPaymentList = customerSupplierPaymentManager.getBatchPaymentItems(objectID);
        List<PaymentData> manualEntryPayments = loadManualEntryPayments(objectID, false);
        List<PaymentData> expensePayments = loadExpensePayments(objectID, false);
        boolean islineItemProject = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);
        int i = 0;
        PaymentData[] payments = new PaymentData[edsInvoicePaymentList.size() + edsCustomerSupplierPaymentList.size() + manualEntryPayments.size() + expensePayments.size()];
        for (EdsInvoicePayment edsInvoicePayment : edsInvoicePaymentList) {
            payments[i] = new PaymentData();
            payments[i].setObjectID(edsInvoicePayment.getObjectID());
            if (edsInvoicePayment.getInvoice() != null) {
                payments[i].setInvoiceNumber(edsInvoicePayment.getInvoice().getNumber());
                payments[i].setReferenceNumber(edsInvoicePayment.getReference());
                payments[i].setPoNumber(edsInvoicePayment.getInvoice().getPoNumber());
                payments[i].setTotalDueAmount(edsInvoicePayment.getInvoice().getDueAmount());
                payments[i].setTotalDiscount(edsInvoicePayment.getInvoice().getTotalDiscount());
                if (islineItemProject && edsInvoicePayment.getInvoice().getInvoiceItems() != null && edsInvoicePayment.getInvoice().getInvoiceItems().size() > 0) {
                    ArrayList<String> projects = new ArrayList<>();
                    ArrayList<String> parentProjects = new ArrayList<>();
                    HashMap<String, BigDecimal> itemNameAndQty = new HashMap<>();
                    for (EdsInvoiceItem item : edsInvoicePayment.getInvoice().getInvoiceItems()) {
                        if (item != null && item.getProject() != null) {
                            projects.add(item.getProject().getName() != null ? item.getProject().getName() : "");
                            if (item.getProject().getParent() != null) {
                                parentProjects.add(item.getProject().getParent().getName() != null ? item.getProject().getParent().getName() : "");
                            }
                        }
                        if (item != null && item.getItemName() != null && item.getQty() != null) {
                            itemNameAndQty.put(item.getItemName(), item.getQty());
                        }
                    }
                    payments[i].setLineItemProject(projects);
                    payments[i].setLineItemParentProject(parentProjects);
                    payments[i].setLineItemNameAndQty(itemNameAndQty);
                }
                if (edsInvoicePayment.getInvoice().getInvoiceItems() != null && edsInvoicePayment.getInvoice().getInvoiceItems().size() > 0) {
                    for (EdsInvoiceItem item : edsInvoicePayment.getInvoice().getInvoiceItems()) {
                        payments[i].setItemId(item.getItem() != null ? item.getItem().getObjectID() : null);
                        payments[i].setItemName(item.getItem() != null ? item.getItem().getName() : "");
                        payments[i].setItemQty(item.getQty());
                        payments[i].setItemUnitPrice(item.getUnitPrice());
                        payments[i].setItemNetAmount(item.getNet());
                        payments[i].setItemTotalAmount(item.getAmmount());
                        payments[i].setItemTaxAmount(item.getTaxAmount());
                        payments[i].setTaxItem(item.getVat() != null ? item.getVat().createTaxItem() : null);
                        payments[i].setTaxCalculationType(item.getTaxCalculationType());
                    }
                }
                payments[i].setInvoiceDate(new DateNonConvertable(edsInvoicePayment.getInvoice().getInvoiceDate()));
                payments[i].setInvoiceDueDate(new DateNonConvertable(edsInvoicePayment.getInvoice().getDueDate()));
                payments[i].setTotal(edsInvoicePayment.getInvoice().getTotalInInvoiceCurrency());
                payments[i].setBaseTotal(edsInvoicePayment.getInvoice().getTotal());
                payments[i].setInvoiceProjectNumber(edsInvoicePayment.getInvoice().getRelatedProject() != null ? edsInvoicePayment.getInvoice().getRelatedProject().getNumber() : null);
                payments[i].setInvoiceProjectName(edsInvoicePayment.getInvoice().getRelatedProject() != null ? edsInvoicePayment.getInvoice().getRelatedProject().getName() : null);
            }
            if (RECEIVABLE_PREPAYMENT.equals(edsInvoicePayment.getType())) {
                payments[i].setInvoiceNumber(edsInvoicePayment.getNumber());
                payments[i].setReferenceNumber(edsInvoicePayment.getReference());
            }
            if (PAYABLE_SUPPLIER_CREDIT.equals(edsInvoicePayment.getType())) {
                payments[i].setInvoiceNumber(edsInvoicePayment.getNumber());
                payments[i].setTotal(edsInvoicePayment.getAmount());
            }
            payments[i].setDate(new DateNonConvertable(edsInvoicePayment.getPaymentDate()));
            payments[i].setPaymentAmount(edsInvoicePayment.getAmount());
            if (receivePaymentData.getCurrency().getName().equals(receivePaymentData.getBaseCurrency().getName())) {
                payments[i].setBasePaymentAmount(edsInvoicePayment.getAmount());
            } else {
                payments[i].setBasePaymentAmount(edsInvoicePayment.getAmount().divide(edsInvoicePayment.getExchangeRate(), 5, RoundingMode.HALF_UP));
            }
            EdsInvoicePaymentTransaction invoicePaymentTransaction = transactionManager.getTransactionByPayment(edsInvoicePayment);
            if (invoicePaymentTransaction != null) {
                Set<EdsTransactionItem> transactionItemsSet = invoicePaymentTransaction.getTransactionItems();
                for (EdsTransactionItem item : transactionItemsSet) {
                    if (!EdsAccountType.BANK.equals(item.getAccount().getAccountType().getCode())) {
                        payments[i].setAccountItem(new SelectItem(item.getAccount().getObjectID(), item.getAccount().getName(), item.getAccount().getAccountCode() != null ? item.getAccount().getAccountCode() : ""));
                    }
                }
            }
            i++;
        }

        for (EdsCustomerSupplierPayment ecsp : edsCustomerSupplierPaymentList) {
            payments[i] = new PaymentData();
            payments[i].setObjectID(ecsp.getObjectID());
            payments[i].setInvoiceNumber("");
            payments[i].setTotalDueAmount(BigDecimal.ZERO);
            payments[i].setLineItemProject(new ArrayList<>());
            payments[i].setLineItemParentProject(new ArrayList<>());
            payments[i].setInvoiceDate(new DateNonConvertable(ecsp.getPaymentDate()));
            payments[i].setInvoiceDueDate(new DateNonConvertable(ecsp.getPaymentDate()));
            payments[i].setDate(new DateNonConvertable(ecsp.getPaymentDate()));
            payments[i].setTotal(BigDecimal.ZERO);
            payments[i].setBaseTotal(ecsp.getAmount());
            payments[i].setPaymentAmount(ecsp.getAmount());
            payments[i].setInvoiceProjectNumber(null);
            payments[i].setInvoiceProjectName(null);
            if (receivePaymentData.getCurrency().getName().equals(receivePaymentData.getBaseCurrency().getName())) {
                payments[i].setBasePaymentAmount(ecsp.getAmount());
            } else {
                payments[i].setBasePaymentAmount(ecsp.getAmount().divide(ecsp.getExchangeRate(), 5, RoundingMode.HALF_UP));
            }

            if (ecsp.getManualJournalId() != null) {
                EdsManualJournal edsManualJournal = manualJournalManager.get(ecsp.getManualJournalId());
                if (edsManualJournal != null) {
                    BigDecimal balance = BigDecimal.ZERO;
                    if (isReceivable) {
                        for (EdsManualJournalItem item : edsManualJournal.getItems()) {
                            balance = balance.add(item.getDebit() == null ? ZERO : item.getDebit());
                        }
                    } else {
                        for (EdsManualJournalItem item : edsManualJournal.getItems()) {
                            balance = balance.add(item.getCredit() == null ? ZERO : item.getCredit());
                        }
                    }
                    payments[i].setInvoiceNumber(edsManualJournal.getNarration() != null ? "Manual Transaction" + ":" + edsManualJournal.getNarration() : "Manual Transaction");
                    payments[i].setInvoiceDate(new DateNonConvertable(edsManualJournal.getDate()));
                    payments[i].setTotal(balance);
                }
            }//Opening Balance

            if (ecsp.getCustomerSupplierID() != null && ecsp.getManualJournalId() == null) {
                EdsCrmAccount edsCrmAccount = crmAccountManager.get(ecsp.getCustomerSupplierID());
                payments[i].setCrmAccount(edsCrmAccount.getAsSelectItem());
                EdsCrmAccount clientSupplier = (isReceivable ? clientManager.get(ecsp.getCustomerSupplierID()) : crmAccountManager.get(ecsp.getCustomerSupplierID()));
                Date balanceDate = isReceivable ? clientSupplier.getBalanceDate() : clientSupplier.getSupplierBalanceDate();
                BigDecimal balanceAmount = isReceivable ? clientSupplier.getBalanceAmount() : clientSupplier.getSupplierBalanceAmount();
                payments[i].setInvoiceNumber("Opening Balance");
                payments[i].setInvoiceDate(new DateNonConvertable(balanceDate));
                payments[i].setTotal(balanceAmount);
            }

            i++;
        }
        for (PaymentData manual : manualEntryPayments) {
            payments[i] = manual;
            i++;
        }
        for (PaymentData expense : expensePayments) {
            payments[i] = expense;
            i++;
        }
        receivePaymentData.setEnabledLineItemProject(islineItemProject);
        receivePaymentData.setPayments(payments);
        if (receivePaymentData.getAccount() != null) {
            EdsBankAccount edsBankAccount = bankAccountManager.getBankAccountByAccountID(receivePaymentData.getAccount().getId());
            if (edsBankAccount != null) {
                receivePaymentData.setAccountNumber(edsBankAccount.getAccountNumber());
                receivePaymentData.setBankAccount(edsBankAccount.getAsSelectItem());
            }
        }
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonServiceLocal.getCompanyCustomFields(isReceivable ? ViewName.BatchInvoicePaymentView : ViewName.BatchPayBillView);
        receivePaymentData.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsBatchPayment.getCustomFields(), customFieldsItems));

        return receivePaymentData;
    }

    @Override
    public BatchPaymentAddEditData getBatchPaymentAddEditData(Integer objectID, boolean isReceivable) {
        BatchPaymentAddEditData addEditData = new BatchPaymentAddEditData();
        addEditData.setBaseCurrency(getBaseCurrency());
        addEditData.setPaymentMethods(allInOneServiceLocal.getPaymentMethodList());
        addEditData.setEnableDepartment(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PAYMENT_DEPARTMENT_ENABLED));

        ReceivePaymentData receivePaymentData;
        if (objectID == null) {
            receivePaymentData = new ReceivePaymentData();
            if (isReceivable) {
                receivePaymentData.setPdfTemplateList(getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.BATCH_RECEIVE_PAYMENT.name()));
            } else {
                receivePaymentData.setPdfTemplateList(getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.BATCH_PAY_BILL.name()));
            }
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonServiceLocal.getCompanyCustomFields(isReceivable ? ViewName.BatchInvoicePaymentView : ViewName.BatchPayBillView);
            receivePaymentData.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(null, customFieldsItems));
            receivePaymentData.setLayoutHtml(PathFinder.getLayoutHTML(BATCH_PAYMENT_FORM));
            addEditData.setNumberData(generateBatchPaymentNumberData(isReceivable, null));
        } else {
            receivePaymentData = getBatchPaymentData(objectID, true);
            Integer accountId = receivePaymentData.getAccount() != null ? receivePaymentData.getAccount().getId() : null;
            addEditData.setNumberData(generateBatchPaymentNumberData(isReceivable, accountId));
        }
        receivePaymentData.setSystemCustomFields(commonServiceLocal.getCompanyCustomFields(ViewName.BatchPayBillViewSystem));
        addEditData.setData(receivePaymentData);
        return addEditData;
    }

    public BankTransferNumberData generateBatchPaymentNumberData(boolean isReceivable, Integer accountID) {
        //check for enable Receive Payments and Pay Invoices numbering settings
        boolean isEnableRPAndPINumbering = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_RP_AND_PI_NUMBERING_SETTINGS);

        String receivablePrefix = isEnableRPAndPINumbering ? EdsNumberingSettings.DEF_RECEIVE_PAYMENT_PREFIX : EdsNumberingSettings.DEF_BANK_RECEIPT_PREFIX;
        String notReceivablePrefix = isEnableRPAndPINumbering ? EdsNumberingSettings.DEF_PAY_BILL_PREFIX : EdsNumberingSettings.DEF_BANK_PAYMENT_PREFIX;

        boolean isTransactionUniqueNumberEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.TRANSACTION_UNIQUE_NUMBERING);
        EdsBankAccount bankAccount = null;

        String prefix = (isReceivable ? receivablePrefix : notReceivablePrefix);

        if (isTransactionUniqueNumberEnabled) {
            if (accountID != null) {
                bankAccount = bankAccountManager.getBankAccountByAccountID(accountID);
            }

            if (isReceivable) {
                prefix = bankAccount != null ? receivablePrefix : EdsNumberingSettings.DEF_CASH_RECEIPT_PREFIX;
            } else {
                prefix = bankAccount != null ? notReceivablePrefix : EdsNumberingSettings.DEF_CASH_PAYMENT_PREFIX;
            }
        }

        BankTransferNumberData transferNumberData = new BankTransferNumberData();
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer fourDigitNumber = sharedNumberManager.getLastIntNumber(prefix, BATCH_PAYMENT);
        String format = null;

        if (settings != null) {
            format = (isReceivable ? (isEnableRPAndPINumbering ? settings.getRpNumberingFormat() : settings.getRmNumberingFormat()) :
                    (isEnableRPAndPINumbering ? settings.getPbNumberingFormat() : settings.getSmNumberingFormat()));

            if (isTransactionUniqueNumberEnabled) {
                if (isReceivable) {
                    format = bankAccount != null ? (isEnableRPAndPINumbering ? settings.getRpNumberingFormat() : settings.getRmNumberingFormat()) : settings.getCrNumberingFormat();
                } else {
                    format = bankAccount != null ? (isEnableRPAndPINumbering ? settings.getPbNumberingFormat() : settings.getSmNumberingFormat()) : settings.getCpNumberingFormat();
                }
            }
        }

        if (format != null) {
            parseNumberData(format, transferNumberData, fourDigitNumber);
        } else {
            NumberData numberData = EdsNumberingSettings.getDefaultData(fourDigitNumber, prefix);
            String[] numberParts = numberData.getNumberFormat().split("_");
            transferNumberData.setPrefix(numberParts[0]);
            transferNumberData.setFourDigitNumber(String.valueOf(numberParts[1]));
            transferNumberData.setWithDate(numberParts[1].split("-").length == 2);
        }
        return transferNumberData;
    }

    private void parseNumberData(String numberFormat, BankTransferNumberData numberData, Integer fourDigitNumber) {
        String[] mainPartNumbers = numberFormat.split("_");  // e.g RP_0001-05/2015 or RP_0001-05/2015
        String[] datePartNumbers = mainPartNumbers[1].split("-");  // e.g 0001-05/2015 or 0001-05/2015

        numberData.setPrefix(mainPartNumbers[0]);
        numberData.setWithDate(datePartNumbers.length == 2);

        String lastFourNumber = datePartNumbers[0];

        DecimalFormat format = new DecimalFormat("0000");
        numberData.setFourDigitNumber(fourDigitNumber != null ? format.format(fourDigitNumber + 1) : lastFourNumber);
    }

    @Override
    public void deleteBatchPayment(ArrayList<Integer> ids) {
        for (Integer id : ids) {
            deleteBatchPayment(id);
        }
    }

    @Override
    public TestRPC deleteBatchPayment(Integer objectID) {
        TestRPC result = new TestRPC();
        EdsBatchPayment edsBatchPayment = batchPaymentManager.get(objectID);
        //Invoice payments delete and reverse applied prepayments
        List<EdsInvoicePayment> edsInvoicePaymentList = invoicePaymentManager.getBatchPaymentItems(objectID);
        for (EdsInvoicePayment edsInvoicePayment : edsInvoicePaymentList) {
            result = deletePayment(edsInvoicePayment.getObjectID());
            if (result.isError()) {
                return result;
            }
            EdsInvoicePayment invoiceUnderPayment = invoicePaymentManager.getInvoiceUnderPayment(edsInvoicePayment.getObjectID());
            if (invoiceUnderPayment != null) {
                deletePayment(invoiceUnderPayment.getObjectID());
            }
            List<EdsInvoicePayment> appliedPaymentItems = invoicePaymentManager.getAppliedPaymentItems(edsInvoicePayment.getObjectID());
            for (EdsInvoicePayment item : appliedPaymentItems) {
                deletePayment(item.getObjectID());
            }
        }
        //Manual Transaction or Customer/Supplier payments,transactions delete and reverse applied prepayments
        List<EdsCustomerSupplierPayment> edsCustomerSupplierPaymentList = customerSupplierPaymentManager.getBatchPaymentItems(objectID);
        for (EdsCustomerSupplierPayment customerSupplierPayment : edsCustomerSupplierPaymentList) {
            deleteCustomerSupplierPayment(customerSupplierPayment);
            EdsCustomerSupplierPayment customerSupplierUnderPayment = customerSupplierPaymentManager.getUnderPayment(customerSupplierPayment.getObjectID());
            if (customerSupplierUnderPayment != null) {
                deleteCustomerSupplierPayment(customerSupplierUnderPayment);
            }
        }
        deleteBatchExpensePaymentsIfExists(objectID);
        //Over payment transaction delete
        accountingServiceLocal.deleteTransactionForOverPayment(objectID);

        edsBatchPayment.setDeleted(true);
        batchPaymentManager.update(edsBatchPayment);

        deleteBatchPaymentNumberData(edsBatchPayment);
        baseEventPostProcessor.registerEvent(BatchPaymentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, edsBatchPayment, userManager.getUser());
        return result;
    }

    private void deleteBatchExpensePaymentsIfExists(Integer objectID) {
        List<EdsExpensePayment> expensePayments = expensePaymentManager.findAllByBatchPaymentId(objectID);
        for (EdsExpensePayment expensePayment : expensePayments) {
            expenseService.deleteExpensePayment(expensePayment.getObjectID());
        }
    }

    private void deleteCustomerSupplierPayment(EdsCustomerSupplierPayment customerSupplierPayment) {
        transactionManager.deleteCustomerSupplierPaymentTransaction(customerSupplierPayment.getObjectID());
        customerSupplierPayment.setDeleted(true);

        if (customerSupplierPayment.getManualJournalId() == null) {
            EdsCrmAccount customerOrSupplier = crmAccountManager.get(customerSupplierPayment.getCustomerSupplierID());
            customerOrSupplier.setPaid(false);
        }
        customerSupplierPaymentManager.update(customerSupplierPayment);
    }

    @Override
    public Integer voidBatchPayment(Integer objectID, DateNonConvertable voidDate) {
        EdsBatchPayment edsBatchPayment = batchPaymentManager.get(objectID);
        edsBatchPayment.setReversed(true);

        List<EdsInvoicePayment> edsInvoicePaymentList = invoicePaymentManager.getBatchPaymentItems(objectID);
        for (EdsInvoicePayment edsInvoicePayment : edsInvoicePaymentList) {
            reversePayment(edsInvoicePayment.getObjectID(), voidDate);
            EdsInvoicePayment invoiceUnderPayment = invoicePaymentManager.getInvoiceUnderPayment(edsInvoicePayment.getObjectID());
            if (invoiceUnderPayment != null) {
                reversePayment(invoiceUnderPayment.getObjectID(), voidDate);
            }
        }

        List<EdsCustomerSupplierPayment> edsCustomerSupplierPaymentList = customerSupplierPaymentManager.getBatchPaymentItems(objectID);
        for (EdsCustomerSupplierPayment customerSupplierPayment : edsCustomerSupplierPaymentList) {
            reverseCustomerSupplierPayment(customerSupplierPayment);
            EdsCustomerSupplierPayment customerSupplierUnderPayment = customerSupplierPaymentManager.getUnderPayment(customerSupplierPayment.getObjectID());
            if (customerSupplierUnderPayment != null) {
                reverseCustomerSupplierPayment(customerSupplierUnderPayment);
            }
        }

        //reverse over payment transaction where batch payment is void
        EdsOverPayment edsOverPayment = overPaymentManager.getOverPaymentByBatchPayment(objectID);
        if (edsOverPayment != null) {
            accountingServiceLocal.createReversalTransactionForOverPayment(edsOverPayment, voidDate);
        }
        deleteBatchPaymentNumberData(edsBatchPayment);
        baseEventPostProcessor.registerEvent(BatchPaymentEventListenerImpl.TYPE, BatchPaymentEventListenerImpl.VOID, edsBatchPayment, userManager.getUser());
        return objectID;
    }

    private void deleteBatchPaymentNumberData(EdsBatchPayment edsBatchPayment) {
        String entityCode = Constants.RECEIVABLE.equals(edsBatchPayment.getType()) ? AccountingConstants.RECEIVABLE_PREPAYMENT : AccountingConstants.PAYABLE_SUPPLIER_CREDIT;
        EdsSharedNumber sharedNumber = sharedNumberManager.getByEntityID(edsBatchPayment.getObjectID(), entityCode);
        if (sharedNumber != null) {
            sharedNumber.setDeleted(true);
            sharedNumberManager.update(sharedNumber);
        }
    }

    private void reverseCustomerSupplierPayment(EdsCustomerSupplierPayment customerSupplierPayment) {
        transactionManager.deleteCustomerSupplierPaymentTransaction(customerSupplierPayment.getObjectID());
        customerSupplierPayment.setDeleted(true);

        if (customerSupplierPayment.getManualJournalId() == null) {
            EdsCrmAccount customerOrSupplier = crmAccountManager.get(customerSupplierPayment.getCustomerSupplierID());
            customerOrSupplier.setPaid(false);
        }
        customerSupplierPaymentManager.update(customerSupplierPayment);
    }


    private void buildChildIds(EdsCrmAccount parent, ArrayList<Integer> IdList, HashMap<Integer, Boolean> map,
                               boolean isCustomer) {
        if (parent.getChildList() != null && !parent.getChildList().isEmpty()) {
            IdList.add(parent.getObjectID());

            if (isCustomer) {
                map.put(parent.getObjectID(), parent.getBalanceDate() != null && parent.getBalanceAmount() != null && !parent.isPaid());
            } else {
                map.put(parent.getObjectID(), parent.getSupplierBalanceDate() != null && parent.getSupplierBalanceAmount() != null && !parent.isPaid());
            }

            for (EdsCrmAccount child : parent.getChildList()) {
                buildChildIds(child, IdList, map, isCustomer);
            }
        } else {
            IdList.add(parent.getObjectID());

            if (isCustomer) {
                map.put(parent.getObjectID(), parent.getBalanceDate() != null && parent.getBalanceAmount() != null && !parent.isPaid());
            } else {
                map.put(parent.getObjectID(), parent.getSupplierBalanceDate() != null && parent.getSupplierBalanceAmount() != null && !parent.isPaid());
            }
        }
    }

    @Override
    public void sendOverDueInvoiceReminders(Integer employeeId, Integer companyId, Boolean toClient, Integer
            recurrenceId) {
        if (recurrenceId != null && companyId != null) {
            List<EdsOverdueInvoiceReminderSettings> settingses = overdueInvoiceReminderSettingsManager.getReminderSettingsByRecurrenceId(companyId, recurrenceId);
            if (settingses != null && settingses.size() > 0) {
                InvoiceList data = getSaleInvoiceDataForRecurrenceJob(employeeId);
                if (data != null) {
                    sendMailToAccountants(data, companyId, recurrenceId);
                }
            }
        }
        if (toClient != null && toClient) {
            InvoiceList[] data = getSaleInvoiceDataForRecurrenceJobForEveryClient(employeeId);
            if (data != null) {
                sendMailFromAccountantsToClients(data, employeeId);
            }
        }
    }

    @Override
    public ArrayList<ExpenseListItem> getInvoiceBillableExpenses(Integer invoiceId) {
        ArrayList<ExpenseListItem> resutl = Lists.newArrayList();
        if (invoiceId == null) {
            return resutl;
        }
        EdsInvoice edsInvoice = invoiceManager.get(invoiceId);
        if (edsInvoice instanceof EdsSaleInvoice) {
            if (edsInvoice.getExpense().size() > 0) {
                for (EdsExpense edsExpense : edsInvoice.getExpense()) {
                    ExpenseListItem item = new ExpenseListItem();
                    if (edsExpense.getReport() != null) {
                        item.setExpenseReportNumber(edsExpense.getReport().getNumber());
                        item.setReportId(edsExpense.getReport().getObjectID());
                        item.setBillExpTotal(edsInvoice.getBillExpTotal());
                        item.setExpenseDate(edsExpense.getReport().getStartDate());
                        item.setDate(edsInvoice.getInvoiceDate());
                        item.setMarkupAmount(edsExpense.getMarkupAmount());
                        item.setCategoryName(edsExpense.getAccount().getName());
                        resutl.add(item);
                    }
                }
            }
        }
        return resutl;
    }

    @Override
    public ArrayList<ExpenseListItem> getInvoiceBillableExpensesList(Integer invoiceId) {
        ArrayList<ExpenseListItem> resutl = Lists.newArrayList();
        if (invoiceId == null) {
            return resutl;
        }
        EdsInvoice edsInvoice = invoiceManager.get(invoiceId);
        NewInvoice newInvoice = EdsInvoice.getInvoiceData(edsInvoice);
//        if (edsInvoice != null && edsInvoice instanceof EdsSaleInvoice) {
        if (newInvoice.getExpenses().size() > 0) {
            for (BillableExpenseItem edsExpense : newInvoice.getExpenses()) {
                ExpenseListItem item = new ExpenseListItem();
//                    if (edsExpense.getReport() != null) {
                item.setExpenseReportNumber(edsExpense.getNumber());
                item.setReportId(edsExpense.getInvoiceId());
                item.setExpenseAmountInCurency(edsExpense.getAmountInCurrency());
                item.setMarkupAmount(edsExpense.getMarkupAmount());
                item.setExpenseDate(edsExpense.getDate());
                item.setDate(edsInvoice.getInvoiceDate());
                item.setCategoryName(edsExpense.getAccount().getName());
                item.setType(edsExpense.getType());
                if (edsExpense.getBankTransferType() != null) {
                    item.setBankTransferType(edsExpense.getBankTransferType());
                }
                resutl.add(item);
//                    }
            }
        }
//        }
        return resutl;
    }

    @Override
    public String generateInvoicePdfTemplateHtml(Integer invoiceId, Integer pdfTemplateId) {
        if (invoiceId == null || pdfTemplateId == null) {
            return null;
        }
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PAID_AND_PRINT_INVOICE)) {
            EdsSaleInvoice edsSaleInvoice = invoiceManager.getSaleInvoice(invoiceId);
            EdsCompany edsCompany = userManager.getUser() != null ? userManager.getUser().getCompany() : null;
            if (edsSaleInvoice != null && !edsSaleInvoice.isDeleted()
                    && edsSaleInvoice.getStatus() != null && edsCompany != null
                    && (APPROVE.equals(edsSaleInvoice.getStatus().getCode())
                    || OPEN.equals(edsSaleInvoice.getStatus().getCode())
                    || OVER_DUE.equals(edsSaleInvoice.getStatus().getCode()))) {

                EdsInvoicingSettings edsInvoicingSettings = invoicingSettingsManager.getInvoiceSettings(edsCompany);
                EdsAccount edsDefaultAccount = null;
                if (edsInvoicingSettings != null && edsInvoicingSettings.getDefaultPaymentAccountId() != null) {
                    edsDefaultAccount = accountingManager.get(edsInvoicingSettings.getDefaultPaymentAccountId());
                }
                if (edsDefaultAccount == null) {
                    return null;
                }
                ReceivePaymentData receivePaymentData = new ReceivePaymentData();
                receivePaymentData.setBatchPayment(true);
                receivePaymentData.setAccount(edsDefaultAccount.getAsSelectItem());
                if (edsSaleInvoice.getClientOrSupplier() != null) {
                    receivePaymentData.setCrmAccount(edsSaleInvoice.getClientOrSupplier().getAsSelectItem());
                }
                if (edsSaleInvoice.getCurrency() != null) {
                    receivePaymentData.setCurrency(edsSaleInvoice.getCurrency().createCurrencyItem());
                }
                CurrencyListItem currencyItem = currencyService.getCurrencyRateByDate(edsSaleInvoice.getCurrency().getObjectID(),
                        new DateNonConvertable(new Date()));
                if (currencyItem != null && currencyItem.getExchangeRate() != null) {
                    receivePaymentData.setExRate(BigDecimal.valueOf(currencyItem.getExchangeRate()));
                }
                receivePaymentData.setReference(edsSaleInvoice.getNumber());
                receivePaymentData.setDate(new DateNonConvertable(new Date()));
                receivePaymentData.setTotalAmount(edsSaleInvoice.getDueAmount());
                receivePaymentData.setPaymentTarget(AccountingConstants.PAYMENT_TARGET_INVOICE);
                receivePaymentData.setType(RECEIVABLE);

                PaymentData paymentData = new PaymentData();
                paymentData.setReferenceNumber(edsSaleInvoice.getNumber());
                paymentData.setInvoiceID(edsSaleInvoice.getObjectID());
                paymentData.setPaymentAmount(edsSaleInvoice.getDueAmount());
                if (currencyItem != null && currencyItem.getExchangeRate() != null) {
                    paymentData.setExchangeRate(BigDecimal.valueOf(currencyItem.getExchangeRate()));
                }
                paymentData.setOpeningBalance(false);
                paymentData.setManualJournal(false);
                paymentData.setDate(new DateNonConvertable(new Date()));
                paymentData.setPaymentAccount(edsDefaultAccount.getAsSelectItem());
                receivePaymentData.setPayments(new PaymentData[]{paymentData});

                saveReceivePaymentData(receivePaymentData, true);
            }
        }
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(invoiceId, pdfTemplateId, null);
        return savedSaleInvoiceViewPDFHandler.velocityReplaceContentAttributes(requestObject);
    }

    private void clearObjectIdFromItems(NewInvoice invObject) {
        for (NewInvoiceItem invoiceItem : invObject.getItems()) {
            invoiceItem.setID(null);

            if (invoiceItem.getCustomFieldItems() != null && !invoiceItem.getCustomFieldItems().isEmpty()) {
                for (CompanyCustomFieldItem ccfItem : invoiceItem.getCustomFieldItems()) {
                    ccfItem.setObjectId(null);
                }
            }
        }
    }

    @Override
    public boolean savePurchaseInvoiceCellValue(NewInvoice rowValue, String columnCodeName) {
        EdsPurchaseInvoice edsPurchaseInvoice = invoiceManager.getPurchaseInvoice(rowValue.getID());
        try {
            EdsInvoiceCustomFields purchaseOrderCF = edsPurchaseInvoice.getCustomFields();
            if (purchaseOrderCF == null) {
                purchaseOrderCF = new EdsInvoiceCustomFields();
                invoiceCFManager.create(purchaseOrderCF);
                edsPurchaseInvoice.setCustomFields(purchaseOrderCF);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(purchaseOrderCF, rowValue.getCustomFieldMap(), columnCodeName);
            try {
                purchaseInvoiceSolrComponent.index(edsPurchaseInvoice);
            } catch (IOException | SolrServerException e) {
                e.printStackTrace();
            }
            return true;
        } catch (Exception e) {
            log.error("Purchase Invoice List Edit Cell Column Code :" + columnCodeName, e);
            return false;
        }
    }

    @Override
    public boolean saveBatchPaymentCellValue(BatchPaymentListItem rowValue, String columnCodeName) {
        EdsBatchPayment batchPayment = batchPaymentManager.get(rowValue.getObjectID());
        try {
            EdsInvoiceCustomFields batchPaymentCF = batchPayment.getCustomFields();
            if (batchPaymentCF == null) {
                batchPaymentCF = new EdsInvoiceCustomFields();
                invoiceCFManager.create(batchPaymentCF);
                batchPayment.setCustomFields(batchPaymentCF);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(batchPaymentCF, rowValue.getCustomFieldsMap(), columnCodeName);
            return true;
        } catch (Exception e) {
            log.error("Batch payment List Edit Cell Column Code :" + columnCodeName, e);
            return false;
        }
    }

    @Override
    public List<HistoryNote> loadInvoiceHistoryNote(Integer id, String viewType, boolean isInvoice) {
        List<MyUpdateItem> historyItems = getAllHistory(id, viewType);
        HistoryListItem[] notes;
        if (isInvoice) {
            notes = invoiceCircularResolver.getInvoiceNotes(id);
        } else {
            notes = invoiceCircularResolver.getQuoteNotes(id);
        }
        List<HistoryNote> result = new ArrayList<>(historyItems);
        result.addAll(Arrays.asList(notes));
        return result;
    }

    @Override
    public List<HistoryNote> loadFixedAssetHistoryNote(Integer id, String viewType) {
        List<MyUpdateItem> historyItems = getAllHistory(id, viewType);
        return new ArrayList<>(historyItems);
    }

    public Integer saveStripeAccount(String authorization_code) {
        try {
            final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_secret", EdsContextParams.getStripeSecretKey());
            params.add("grant_type", "authorization_code");
            params.add("code", authorization_code);
            final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Accept", "application/json");
            final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            String oauthRespStr = restTemplate.postForObject("https://connect.stripe.com/oauth/token", entity, String.class);
            log.info(oauthRespStr);

            JSONObject parentJSONObject = (JSONObject) new JSONParser().parse(oauthRespStr);
            String stripe_user_id = (String) parentJSONObject.get("stripe_user_id");
            if (StringUtils.isNotBlank(stripe_user_id)) {
                EdsCompany company = invoicingSettingsManager.getUser().getCompany();
                EdsInvoicingSettings invoiceSettings = invoicingSettingsManager.getInvoiceSettings(company);
                if (invoiceSettings == null) {
                    invoiceSettings = new EdsInvoicingSettings();
                }
                invoiceSettings.setStripeAuthResp(oauthRespStr);
                invoiceSettings.setStripeUserId(stripe_user_id);
                if (invoiceSettings.getObjectID() != null) {
                    invoicingSettingsManager.update(invoiceSettings);
                } else {
                    invoicingSettingsManager.create(invoiceSettings);
                }
                return invoiceSettings.getObjectID();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    @Override
    public Integer removeStripeAccount() {
        try {
            EdsCompany company = invoicingSettingsManager.getUser().getCompany();
            EdsInvoicingSettings invoiceSettings = invoicingSettingsManager.getInvoiceSettings(company);
            if (invoiceSettings != null) {
                invoiceSettings.setStripeUserId(null);
                invoicingSettingsManager.update(invoiceSettings);
            }
            return invoiceSettings.getObjectID();
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    @Override
    public AccountItem getChartOfAccountFromProductAndService(String type, String accountType, Integer productId) {
        EdsCompany company = accountingManager.getUser().getCompany();
        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(company);
        if (invoicingSettings.getDefAccountSI() == null) {
            EdsItem item = itemManager.get(productId);
            if (item != null && item.getAccount() != null) {
                EdsAccount account = accountingManager.get(item.getAccount().getObjectID());
                if (account == null) return getDefaultAccountItem(type, accountType);
                return account.createAccountItem();
            }
        }
        return getDefaultAccountItem(type, accountType);
    }

    @Override
    public BillOfEntry getBillOfEntry(Integer purchaseInvoiceId, Integer billOfEntryId) {
        if (purchaseInvoiceId != null && purchaseInvoiceId > 0) {

            BillOfEntry result = new BillOfEntry();
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setExcludeExemptAndOutOfScope(true);

            //Set zero taxes
            TaxItem[] allTaxes = accountingServiceLocal.getCompanyTaxesWithFilter(fp);
            if (allTaxes != null) {
                /*List<TaxItem> zTs = (List<TaxItem>) (Arrays.asList(allTaxes).stream().filter(taxItem -> {
//                    return taxItem.getTaxPercent().doubleValue() <= 0;
                    return taxItem!=null;
                })).map(taxItem -> {
                    return taxItem;//new SelectItem(taxItem.getId(), taxItem.getName(), taxItem.getDescription());
                }).collect(Collectors.toList());
                result.setZeroTaxes(zTs);*/
                result.setZeroTaxes(com.google.common.collect.Lists.newArrayList(allTaxes));
            }
            //End of Set zero taxes

            if (billOfEntryId != null && billOfEntryId > 0) {
                EdsBillOfEntry billOfEntry = billOfEntryManager.get(billOfEntryId);
                if (billOfEntry != null) {

                    result.setBoeNumber(billOfEntry.getBoeNumber());
                    result.setBoeDate(billOfEntry.getBoeDate());
                    result.setDescription(billOfEntry.getDescription());
                    result.setObjectID(billOfEntry.getObjectID());
                    result.setReference(billOfEntry.getReference());
                    result.setPortId(billOfEntry.getPortId());
                    if (billOfEntry.getPort() != null) {
                        result.setPortCode(billOfEntry.getPort().getCode());
                        result.setPortName(billOfEntry.getPort().getName());
                    }
                    if (billOfEntry.getPaidThrough() != null) {
                        result.setPaidThrough(billOfEntry.getPaidThrough().getAsSelectItem());
                    }
                    result.setTotalCustomDuty(billOfEntry.getTotalCustomDuty());
                    result.setTotalAmount(billOfEntry.getTotalAmount());
                    result.setTotalTaxAmount(billOfEntry.getTotalTaxAmount());
                    if (billOfEntry.getItems() != null && !billOfEntry.getItems().isEmpty()) {
                        for (EdsBillOfEntryItem entryItem : billOfEntry.getItems()) {

                            BillOfEntryItem billOfEntryItem = new BillOfEntryItem();
                            billOfEntryItem.setId(entryItem.getObjectID());
                            if (entryItem.getItem() != null) {
                                billOfEntryItem.setItemID(entryItem.getItem().getObjectID());
                                billOfEntryItem.setItemName(entryItem.getItem().getName());
                                billOfEntryItem.setFullItemName(entryItem.getItem().getProductNumber() + " -> " + entryItem.getItem().getName());
                            } else {
                                billOfEntryItem.setItemName(entryItem.getItemName());
                                billOfEntryItem.setFullItemName(entryItem.getItemName());
                            }
                            billOfEntryItem.setAssessableValue(entryItem.getAssessableValue());
                            billOfEntryItem.setTaxableAmount(entryItem.getTaxableAmount());
                            billOfEntryItem.setCustomDutyAdditionalCharges(entryItem.getCustomDutyAdditionalCharges());
                            if (entryItem.getTax() != null) {
                                billOfEntryItem.setTax(entryItem.getTax().createTaxItem());
                            }

                            result.getItems().add(billOfEntryItem);
                        }
                    }
                }
            }

            if (result.getItems().isEmpty()) {

                EdsPurchaseInvoice purchaseInvoice = invoiceManager.getPurchaseInvoice(purchaseInvoiceId);

                if (purchaseInvoice != null && purchaseInvoice.getInvoiceItems() != null) {

                    for (EdsInvoiceItem invoiceItem : purchaseInvoice.getInvoiceItems()) {
                        BillOfEntryItem item = new BillOfEntryItem();

                        if (invoiceItem.getItem() != null && invoiceItem.getItem().getObjectID() != null) {
                            item.setItemName(invoiceItem.getItem().getName());
                            item.setFullItemName(invoiceItem.getItem().getProductNumber() + " -> " + invoiceItem.getItem().getName());

                            if (invoiceItem.getItem().getCategory() != null) {
                                item.setItemCategory(invoiceItem.getItem().getCategory().getName());
                            }
                            item.setItemID(invoiceItem.getItem().getObjectID());
                            item.setItemType(invoiceItem.getItem().getType());
                        } else {
                            item.setItemName(invoiceItem.getItemName());
                            item.setFullItemName(item.getItemName());
                        }
                        item.setAssessableValue(invoiceItem.getNet());
                        item.setTaxableAmount(item.getAssessableValue());

                        result.getItems().add(item);
                    }
                }
            }

            return result;
        }
        return null;
    }

    @Override
    @Transactional
    public BillOfEntry saveBillOfEntry(Integer purchaseInvoiceId, BillOfEntry billOfEntryRpc) {


        EdsPurchaseInvoice purchaseInvoice = (EdsPurchaseInvoice) invoiceManager.get(purchaseInvoiceId);
        if (purchaseInvoice == null || billOfEntryRpc == null) {
            return null;
        }

        EdsBillOfEntry billOfEntry = billOfEntryRpc != null && billOfEntryRpc.getObjectID() != null && billOfEntryRpc.getObjectID() > 0 ? billOfEntryManager.get(billOfEntryRpc.getObjectID()) : new EdsBillOfEntry();

        billOfEntry.setBoeNumber(billOfEntryRpc.getBoeNumber());
        billOfEntry.setBoeDate(billOfEntryRpc.getBoeDate());
        billOfEntry.setDescription(billOfEntryRpc.getDescription());
        billOfEntry.setObjectID(billOfEntryRpc.getObjectID());
        billOfEntry.setReference(billOfEntryRpc.getReference());
        if (billOfEntryRpc.getPortId() != null) {
            billOfEntry.setPortId(billOfEntryRpc.getPortId());
        }
        if (billOfEntryRpc.getPaidThrough() != null) {
            billOfEntry.setPaidThrough(accountingManager.get(billOfEntryRpc.getPaidThrough().getId()));
        }
        billOfEntry.setTotalCustomDuty(billOfEntryRpc.getTotalCustomDuty());
        billOfEntry.setTotalAmount(billOfEntryRpc.getTotalAmount());
        billOfEntry.setTotalTaxAmount(billOfEntryRpc.getTotalTaxAmount());
        if (billOfEntry.getObjectID() != null) {
            billOfEntryManager.update(billOfEntry);
        } else {
            billOfEntryManager.create(billOfEntry);
        }
        //SAVE PURCHASE INVOICE
        purchaseInvoice.setBillOfEntryId(billOfEntry.getObjectID());
        purchaseInvoice.setBillOfEntry(billOfEntry);

        //Save BillOfEntry items
        List<Integer> itemsDeleted = billOfEntryManager.deleteBillOfEntryItems(billOfEntry.getObjectID());

        for (BillOfEntryItem billOfEntryItem : billOfEntryRpc.getItems()) {
            EdsBillOfEntryItem item = new EdsBillOfEntryItem();
            item.setAssessableValue(billOfEntryItem.getAssessableValue());
            item.setCategoryName(billOfEntryItem.getItemCategory());
            item.setCustomDutyAdditionalCharges(billOfEntryItem.getCustomDutyAdditionalCharges());
            item.setItem(itemManager.get(billOfEntryItem.getItemID()));
            item.setItemName(billOfEntryItem.getItemName());
            if (billOfEntryItem.getTax() != null) {
                item.setTax(vatManager.get(billOfEntryItem.getTax().getId()));
            }
            item.setTaxableAmount(billOfEntryItem.getTaxableAmount());
            item.setBillofentry_id(billOfEntry.getObjectID());
            billOfEntryItemManager.create(item);
        }

        invoiceManager.update(purchaseInvoice);

        //Create transaction items
        createUpdateBillOfEntryTransactions(billOfEntry);

        billOfEntryRpc.setObjectID(billOfEntry.getObjectID());
        return billOfEntryRpc;

    }

    private void createUpdateBillOfEntryTransactions(EdsBillOfEntry billOfEntry/*, EdsCurrency baseCurrency*/) {
        EdsUser user = transactionManager.getUser();

        EdsBillOfEntryTransaction transaction = transactionManager.getBillOfEntryTransaction(billOfEntry.getObjectID());
        if (transaction != null) {
            transactionManager.deleteTransactionItems(transaction.getObjectID());
        } else {
            transaction = new EdsBillOfEntryTransaction();
            transaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID() + 1);
            transaction.setBillOfEntryId(billOfEntry.getObjectID());
        }
        transaction.setName("Bill Of Entry");
        transaction.setJournalDate(billOfEntry.getBoeDate());
        transaction.setPostedDate(user.getUserDate());
        transaction.setPostedBy(user);
        transaction.setReference(billOfEntry.getReference());

        EdsTransactionItem cargoExpenseItem = new EdsTransactionItem();
        cargoExpenseItem.setAccount(accountingManager.getAccountByKey(EdsAccount.CARGO_EXPENSE));
        cargoExpenseItem.setDebit(billOfEntry.getTotalCustomDuty());
        transaction.addTransactionItem(cargoExpenseItem);

        EdsTransactionItem paidFromCreditItem = new EdsTransactionItem();
        paidFromCreditItem.setAccount(billOfEntry.getPaidThrough());
        paidFromCreditItem.setCredit(billOfEntry.getTotalCustomDuty());
        transaction.addTransactionItem(paidFromCreditItem);

        //vatManager.get
        EdsTransactionItem inputVatItem = new EdsTransactionItem();
        inputVatItem.setAccount(accountingManager.getVatAccount(PAYABLE));
        inputVatItem.setDebit(billOfEntry.getTotalTaxAmount());
        transaction.addTransactionItem(inputVatItem);

        EdsTransactionItem outputVatItem = new EdsTransactionItem();
        outputVatItem.setAccount(accountingManager.getVatAccount(RECEIVABLE));
        outputVatItem.setCredit(billOfEntry.getTotalTaxAmount());
        transaction.addTransactionItem(outputVatItem);

        transactionManager.createOrUpdate(transaction);
    }

    @Override
    @Transactional
    public Boolean deleteBillOfEntry(Integer billOfEntryId) {

        EdsBillOfEntry billOfEntry = billOfEntryManager.get(billOfEntryId);

        if (billOfEntry != null) {
            billOfEntryManager.delete(billOfEntry);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;

    }

    @Override
    public SelectItem[] getPILookUp(ListingFilterParameter filterParameter) {
        List<SelectItem> piList = invoiceManager.getPurchaseInvoicesForLookUp(filterParameter);
        return piList.toArray(new SelectItem[]{});
    }

    @Transactional(readOnly = true)
    public List<NewInvoiceItem> getSOQItems(List<Integer> quoteIds) {
        List<NewInvoiceItem> items = new ArrayList<>();
        List<EdsSaleQuote> quotes = quoteManager.getSaleQuotesByIds(ServerUtils.getAsCommoDelimited(quoteIds, "0", ","));
        ArrayList<CompanyCustomFieldItem> saleQuoteCustomFieldsItems = commonService.getCompanyAllCustomFields(quotes.get(0) != null && quotes.get(0).isSalesOrder() ? ViewName.SaleOrderItem : ViewName.SaleQuoteItem);

        for (EdsSaleQuote quote : quotes) {
            EdsReference quoteStatus = quote.getStatus();

            for (EdsQuoteItem quoteItem : quote.getQuoteItems()) {
                if ((PARTIAL_SHIPPED.equals(quoteStatus.getCode()) || SHIPPED.equals(quoteStatus.getCode())) && quoteItem.getGdnNonConvertedQty().compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                NewInvoiceItem invoiceItem = new NewInvoiceItem();
                invoiceItem.setID(quoteItem.getObjectID());
                if (quoteItem.getItem() != null) {
                    quoteItem.getItem().setInvoiceItemData(invoiceItem);
                    invoiceItem.setItemDiscountList(EdsDiscount.getItemDiscounts(quoteItem.getItem().getDiscounts()));
                } else if (quoteItem.getItemName() != null) {
                    invoiceItem.setItemName(quoteItem.getItemName());
                    invoiceItem.setFullItemName(quoteItem.getItemName());
                }
                if (quoteItem.getUnitMeasurement() != null) {
                    invoiceItem.setMeasurement(new SelectItem(quoteItem.getUnitMeasurement().getObjectID(), quoteItem.getUnitMeasurement().getName(), quoteItem.getUnitMeasurement().getDescription()));
                }
                invoiceItem.setDescription(quoteItem.getDescription());

                if ((PARTIAL_SHIPPED.equals(quoteStatus.getCode()) || SHIPPED.equals(quoteStatus.getCode()))) {
                    invoiceItem.setQuantity(quoteItem.getGdnNonConvertedQty());
                } else {
                    invoiceItem.setQuantity(quoteItem.getQty());
                }
                invoiceItem.setLumpsum(quoteItem.isLumpsum());
                invoiceItem.setUnitPrice(quoteItem.getUnitPrice());
                invoiceItem.setPriceLevelAmount(quoteItem.getPriceLevelAmount());
                invoiceItem.setComission(quoteItem.getComission());
                invoiceItem.setDiscountPercent(quoteItem.getDiscount());
                invoiceItem.setDiscountAmount(quoteItem.getDiscountAmount());
                if (quoteItem.getItemDiscount() != null) {
                    invoiceItem.setItemDiscountID(quoteItem.getItemDiscount().getObjectID());
                    invoiceItem.setItemDiscount(quoteItem.getItemDiscount().getName());
                }

                invoiceItem.setReceivedAllocation(quoteItem.getReceivedAllocation());
                invoiceItem.setNet(quoteItem.getNet());
                if (quoteItem.getAccount() != null) {
                    EdsAccount account = quoteItem.getAccount();
                    invoiceItem.setAccountID(account.getObjectID());
                    invoiceItem.setAccountName(account.getName());
                    invoiceItem.setAccountItem(account.createAccountItem());
                }
                if (quoteItem.getVat() != null) {
                    invoiceItem.setTaxItem(quoteItem.getVat().createTaxItem());
                    invoiceItem.setTaxAmount(quoteItem.getItemCalculatedTaxAmount(false, quoteItem.getQuote().getTaxCalculationType()));
                }
                if (quoteItem.getDoubleVat() != null) {
                    invoiceItem.setDoubleTaxItem(quoteItem.getDoubleVat().createTaxItem());
                    invoiceItem.setDoubleTaxAmount(quoteItem.getItemCalculatedTaxAmount(true, quoteItem.getQuote().getTaxCalculationType()));
                }
                invoiceItem.setTotalAmount(quoteItem.getAmmount());
                invoiceItem.setReceiveType(quoteItem.getReceiveType());
                invoiceItem.setReceivedAmount(quoteItem.getReceivedAmount());
                invoiceItem.setReceivedQty(quoteItem.getReceivedQty());

                if (quoteItem.getWarehouse() != null) {
                    invoiceItem.setWarehouse(quoteItem.getWarehouse().getAsSelectItem());
                }
                if (quoteItem.getDepartment() != null) {
                    invoiceItem.setDepartmentItem(quoteItem.getDepartment().getAsSelectItem());
                }

                invoiceItem.setQuoteItemId(quoteItem.getObjectID());
                invoiceItem.setConvertedQty(quoteItem.getConvertedQty());
                invoiceItem.setConvertedAmount(quoteItem.getConvertedAmount());

                List<CompanyCustomFieldItem> saleInvoiceCustomFieldsItems = commonService.getCompanyAllCustomFields(ViewName.SaleInvoiceItem);
                ArrayList<CompanyCustomFieldItem> siCustomFields = new ArrayList<>();
                List<CompanyCustomFieldItem> sqCustomValues = CustomFieldsUtils.setRPCCustomFieldItems(quoteItem.getCustomFields(), saleQuoteCustomFieldsItems);
                for (CompanyCustomFieldItem si : saleInvoiceCustomFieldsItems) {
                    si.setObjectId(null);
                    for (CompanyCustomFieldItem sq : sqCustomValues) {
                        if (si.getDataType().equals(sq.getDataType())
                                && si.getUiType().equals(sq.getUiType())
                                && si.getAliasName().equals(sq.getAliasName())) {
                            si.setPredefinedValues(sq.getPredefinedValues());
                            si.setPredefinedValuesWithSorting(sq.getPredefinedValuesWithSorting());
                            si.setQuery(sq.getQuery());
                            si.setQueryItems(sq.getQueryItems());
                            si.setFieldStringValue(sq.getFieldStringValue());
                            si.setFieldDateNonConvertedValue(sq.getFieldDateNonConvertedValue());
                            si.setAttachments(sq.getAttachments());
                            si.setLookUpTypeEnum(sq.getLookUpTypeEnum());
                            si.setSelectedId(sq.getSelectedId());
                            si.setDefaultValue(sq.getDefaultValue());
                            si.setPrefix(sq.getPrefix());
                            si.setItem(sq.getItem());
                            si.setSelectItems(sq.getSelectItems());
                        }
                    }
                    siCustomFields.add(si);
                }
                invoiceItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(null, siCustomFields));
                items.add(invoiceItem);
            }
        }


        return items;
    }

    @Transactional(readOnly = true)
    public List<NewInvoiceItem> getSOQItems(List<Integer> quoteIds, List<QIGroupingField> groupingFields) {
        Map<Integer, EdsItem> productsMap = Maps.newHashMap();
        Map<Integer, EdsAccount> accountsMap = Maps.newHashMap();
        Map<Integer, EdsVat> vatsMap = Maps.newHashMap();
        Map<Integer, EdsDepartment> departmentsMap = Maps.newHashMap();
        List<NewInvoiceItem> results = new ArrayList<>();
        List<Object[]> list = quoteManager.getGroupedItems(quoteIds, groupingFields);
        list.forEach(objs -> {
            Integer productId = (Integer) objs[0];
            String productName = (String) objs[1];
            BigDecimal unitPrice = (BigDecimal) objs[2];
            BigDecimal quantity = (BigDecimal) objs[3];
            Integer accountId = (Integer) objs[4];
            Integer vatId = (Integer) objs[5];
            Integer departmentId = (Integer) objs[6];

            if (quantity.compareTo(BigDecimal.ZERO) != 0) {
                NewInvoiceItem invoiceItem = new NewInvoiceItem();
                if (productId != null) {
                    EdsItem item;
                    if (productsMap.get(productId) == null) {
                        item = itemManager.get(productId);
                        productsMap.put(productId, item);
                    } else {
                        item = productsMap.get(productId);
                    }
                    item.setInvoiceItemData(invoiceItem);
                } else {
                    invoiceItem.setItemName(productName);
                    invoiceItem.setFullItemName(productName);
                }
                invoiceItem.setUnitPrice(unitPrice);
                invoiceItem.setQuantity(quantity);

                if (accountId != null) {
                    EdsAccount account;
                    if (accountsMap.get(accountId) == null) {
                        account = accountingManager.get(accountId);
                        accountsMap.put(accountId, account);
                    } else {
                        account = accountsMap.get(accountId);
                    }
                    invoiceItem.setAccountID(account.getObjectID());
                    invoiceItem.setAccountName(account.getName());
                    invoiceItem.setAccountItem(account.createAccountItem());
                }
                if (vatId != null) {
                    EdsVat vat;
                    if (vatsMap.get(vatId) == null) {
                        vat = vatManager.get(vatId);
                        vatsMap.put(vatId, vat);
                    } else {
                        vat = vatsMap.get(vatId);
                    }
                    invoiceItem.setTaxItem(vat.createTaxItem());
                }
                if (departmentId != null) {
                    EdsDepartment department;
                    if (departmentsMap.get(departmentId) == null) {
                        department = departmentManager.get(departmentId);
                    } else {
                        department = departmentsMap.get(departmentId);
                    }
                    invoiceItem.setDepartmentItem(department.getAsSelectItem());
                }
                results.add(invoiceItem);
            }
        });
        return results;
    }

    ArrayList<CompanyCustomFieldItem> getWrappedSOQCustomFieldsToSI(EdsSaleQuote quote) {
        List<CompanyCustomFieldItem> saleInvoiceCustomFieldsItems = commonService.getCompanyCustomFields(ViewName.SaleInvoice);
        ArrayList<CompanyCustomFieldItem> saleQuoteCustomFieldsItems = commonService.getCompanyCustomFields(quote.isSalesOrder() ? ViewName.SaleOrder : ViewName.SaleQuote);
        List<CompanyCustomFieldItem> sqCustomValues = CustomFieldsUtils.setRPCCustomFieldItems(quote.getCustomFields(), saleQuoteCustomFieldsItems);

        ArrayList<CompanyCustomFieldItem> siCustomFields = new ArrayList<>();

        for (CompanyCustomFieldItem si : saleInvoiceCustomFieldsItems) {
            si.setObjectId(null);
            for (CompanyCustomFieldItem sq : sqCustomValues) {
                if (si.getDataType().equals(sq.getDataType())
                        && si.getUiType().equals(sq.getUiType())
                        && si.getAliasName().equals(sq.getAliasName())) {
                    si.setPredefinedValues(sq.getPredefinedValues());
                    si.setPredefinedValuesWithSorting(sq.getPredefinedValuesWithSorting());
                    si.setQuery(sq.getQuery());
                    si.setQueryItems(sq.getQueryItems());
                    si.setFieldStringValue(sq.getFieldStringValue());
                    si.setFieldDateNonConvertedValue(sq.getFieldDateNonConvertedValue());
                    si.setAttachments(sq.getAttachments());
                    si.setLookUpTypeEnum(sq.getLookUpTypeEnum());
                    si.setSelectedId(sq.getSelectedId());
                    si.setDefaultValue(sq.getDefaultValue());
                    si.setPrefix(sq.getPrefix());
                    si.setItem(sq.getItem());
                    si.setSelectItems(sq.getSelectItems());
                }
            }
            siCustomFields.add(si);
        }
        return CustomFieldsUtils.setRPCCustomFieldItems(null, siCustomFields);
    }

    @Transactional
    public void saveDeferredTransactionItemsByInvoice(Integer invoiceId) {
        EdsInvoice invoice = invoiceManager.get(invoiceId);
        deferredTransactionManager.deleteByTypeAndEntity(invoice instanceof EdsSaleInvoice ? DeferredTransactionType.SALE_INVOICE : DeferredTransactionType.PURCHASE_INVOICE, invoiceId);
        List<EdsDeferredTransactionItem> transactionItems = invoice.getInvoiceItems()
                .stream()
                .filter(EdsInvoiceItem::isDeferredTransasctionItem)
                .map(EdsInvoiceItem::createDeferredTransactionItem)
                .toList();
        transactionItems.forEach(tItem -> deferredTransactionManager.create(tItem));

        LocalDate fromDate = transactionItems.stream()
                .map(EdsDeferredTransactionItem::getFromDate)
                .map(fd -> Instant.ofEpochMilli(fd.getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
                .min(LocalDate::compareTo).get();

        LocalDate toDate = transactionItems.stream()
                .map(EdsDeferredTransactionItem::getToDate)
                .map(td -> Instant.ofEpochMilli(td.getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
                .max(LocalDate::compareTo).get();
        LocalDate transactionToDate = LocalDate.now().withDayOfMonth(1).minusDays(1); //end of month
        transactionToDate = transactionToDate.isBefore(toDate) ? transactionToDate : toDate.withDayOfMonth(toDate.lengthOfMonth());

        transactionManager.deleteDeferredTransaction(invoice instanceof EdsSaleInvoice ? DeferredTransactionType.SALE_INVOICE : DeferredTransactionType.PURCHASE_INVOICE, invoiceId);
        while (Period.between(fromDate, transactionToDate).getMonths() >= 0) {
            LocalDate startDate = fromDate.withDayOfMonth(1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

            for (EdsDeferredTransactionItem item : transactionItems) {
                LocalDate fromLocalDate = Instant.ofEpochMilli(item.getFromDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate toLocalDate = Instant.ofEpochMilli(item.getToDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

                if (!startDate.isBefore(fromLocalDate) && startDate.isBefore(toLocalDate)) {
                    fromLocalDate = startDate;
                } else if (!(startDate.isBefore(fromLocalDate) && fromLocalDate.isBefore(endDate))) {
                    continue;
                }

                if (!endDate.isAfter(toLocalDate) && endDate.isAfter(fromLocalDate)) {
                    toLocalDate = endDate;
                } else if (!(endDate.isAfter(toLocalDate) && toLocalDate.isAfter(startDate))) {
                    continue;
                }

                item.setDayCount(Period.between(fromLocalDate, toLocalDate).getDays() + 1);
                item.setJournalDate(Date.from(endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                accountingServiceLocal.createTransactionForDeferredObject(item);
            }
            fromDate = fromDate.plusMonths(1);
        }
    }

    @Override
    public void deleteDeferredTransactionItemsByInvoice(Integer invoiceId) {
        EdsInvoice invoice = invoiceManager.get(invoiceId);
        deferredTransactionManager.deleteByTypeAndEntity(invoice instanceof EdsSaleInvoice ? DeferredTransactionType.SALE_INVOICE : DeferredTransactionType.PURCHASE_INVOICE, invoiceId);
        transactionManager.deleteDeferredTransaction(invoice instanceof EdsSaleInvoice ? DeferredTransactionType.SALE_INVOICE : DeferredTransactionType.PURCHASE_INVOICE, invoiceId);
    }

    @Override
    public NewInvoice getInvoiceCustomFieldItems(Integer customerId, ViewName viewName) {
        NewInvoice invoiceItem = new NewInvoice();

        EdsCrmAccount customer = crmAccountManager.get(customerId);
        ArrayList<CompanyCustomFieldItem> customerCustomFields = null;
        if (customer != null) {
            customerCustomFields = (ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(customer.getCustomFields(),
                    this.commonService.getCompanyCustomFields(ViewName.CrmAccount));

            final ArrayList<CompanyCustomFieldItem> customFieldsItems = this.commonService.getCompanyCustomFields(viewName);
            final ArrayList<CompanyCustomFieldItem> saleInvoiceCustomFields = (ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(null, customFieldsItems);

            for (final CompanyCustomFieldItem inputcf : saleInvoiceCustomFields) {
                for (final CompanyCustomFieldItem resultcf : customerCustomFields) {
                    if (inputcf.getAliasName().equals(resultcf.getAliasName()) && inputcf.getUiType().equals(resultcf.getUiType())) {
                        if (Constants.UI_TYPE_DATEPICKER.equals(inputcf.getUiType()) || Constants.UI_TYPE_DATEPICKER_TIME.equals(inputcf.getUiType())) {
                            inputcf.setFieldDateNonConvertedValue(resultcf.getFieldDateNonConvertedValue());
                        } else {
                            inputcf.setFieldStringValue(resultcf.getFieldStringValue());
                        }
                    }
                }

            }
        }
        invoiceItem.setCustomFieldItems(customerCustomFields);
        if (invoiceItem != null && invoiceItem.getCustomFieldItems() != null) {
            invoiceItem.getCustomFieldItems().forEach(customFieldItem -> customFieldItem.setObjectId(null));
        }
        return invoiceItem;
    }

    @Override
    public CurrencyListItem checkExchangeRate(Integer accountId, DateNonConvertable date) {
        EdsAccount account = accountingManager.get(accountId);
        if (account.getCurrency() == null) {
            return null;
        }
        return currencyService.getCurrencyRateByDate(account.getCurrency().getObjectID(), date);
    }
}
