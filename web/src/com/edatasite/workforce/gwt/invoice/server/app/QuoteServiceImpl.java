package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.shared.log.KpiEntityType;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsExpense;
import com.edatasite.workforce.core.domain.EdsFormProperty;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsComissionAllocateItem;
import com.edatasite.workforce.core.domain.accounting.EdsDiscount;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsFixedAsset;
import com.edatasite.workforce.core.domain.accounting.EdsGoodsDeliveredTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsGoodsReceivedTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceQuoteNote;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceTerms;
import com.edatasite.workforce.core.domain.accounting.EdsItemTableSettings;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournal;
import com.edatasite.workforce.core.domain.accounting.EdsPaymentInstruction;
import com.edatasite.workforce.core.domain.accounting.EdsPickList;
import com.edatasite.workforce.core.domain.accounting.EdsPriceLevel;
import com.edatasite.workforce.core.domain.accounting.EdsProductSerial;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteHistory;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteItem;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteTaxTotal;
import com.edatasite.workforce.core.domain.accounting.EdsRFP;
import com.edatasite.workforce.core.domain.accounting.EdsRFPItem;
import com.edatasite.workforce.core.domain.accounting.EdsRFQ;
import com.edatasite.workforce.core.domain.accounting.EdsRFQItem;
import com.edatasite.workforce.core.domain.accounting.EdsRFQSupplierBid;
import com.edatasite.workforce.core.domain.accounting.EdsRfqRfpNote;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.core.domain.accounting.EdsShippingDataItem;
import com.edatasite.workforce.core.domain.accounting.EdsStockAdjustmentNote;
import com.edatasite.workforce.core.domain.accounting.EdsStockTransferNote;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.core.domain.analyzer.EdsSolrDbConsistency;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.approving.EdsApproverRoles;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.EdsOpportunityItem;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsRFPCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsRFPItemCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsRFQCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsRFQItemCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.core.domain.customform.EdsCustomItemTable;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.fifo.EdsFifoFailure;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.solr.component.PurchaseInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.PurchaseOrderSolrComponent;
import com.edatasite.workforce.core.solr.component.RequestForQuoteSolrComponent;
import com.edatasite.workforce.core.solr.component.ShippingDataSolrComponent;
import com.edatasite.workforce.core.solr.document.RequestForQuoteSolrDoc;
import com.edatasite.workforce.core.solr.document.ShippingDataSolrDoc;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.ReceiveTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.itemBatches.ItemBatchServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.itemserials.ItemSerialServiceLocal;
import com.edatasite.workforce.gwt.client.server.app.ClientSupplierAccessService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
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
import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProductServiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.MessageCommand;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.EmailTemplateServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.PathFinder;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaOperations;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.FormPropertyManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.PaymentMethodManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteItemManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.RfqRfpNoteManager;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import com.edatasite.workforce.gwt.core.server.db.ShippingDataItemManager;
import com.edatasite.workforce.gwt.core.server.db.ShippingDataManager;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentManager;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentNoteManager;
import com.edatasite.workforce.gwt.core.server.db.StockTransferManager;
import com.edatasite.workforce.gwt.core.server.db.StockTransferNoteManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ComissionAllocateManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.DiscountManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemTableSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PriceLevelManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFPItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFPItemManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFPManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQItemManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQSupplierBidManager;
import com.edatasite.workforce.gwt.core.server.db.analyzer.SolrDbConsistencyManager;
import com.edatasite.workforce.gwt.core.server.db.chart.ChartConfigManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.InvoiceCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.RFPCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.RFQCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.EmailRepository;
import com.edatasite.workforce.gwt.core.server.db.fifo.FifoFailureManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.documents.FolderRbacManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.PurchaseOrderEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.RequestForPurchaseEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.RequestForQuoteEventListenerimpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.SalesOrderEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.SalesQuoteEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.StockTransferEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.ProjectBudgetCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.TransactionCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EntityType;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CacheConstants;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarService;
import com.edatasite.workforce.gwt.invoice.client.rpc.AllocateComissionItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.ListHeap;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.PickList;
import com.edatasite.workforce.gwt.invoice.client.rpc.PickListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFPData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFPItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQSupplierBid;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.SendToFormFillingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingDataItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.edatasite.workforce.gwt.invoice.client.rpc.TotalTaxItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.ItemSerialEntityType;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.client.rpc.saleorderbaseinvoice.SaleOrderBaseInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.edatasite.workforce.gwt.project.server.actions.ProjectServiceLocal;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.mail.EdsTemplates;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;
import static com.google.gwt.user.server.rpc.security.ServerSecurityContext.getInstance;
import static java.util.Arrays.asList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 07.04.2009
 * Time: 16:20:13
 */

@Transactional
@Service("quoteService")
public class QuoteServiceImpl extends BaseInvoiceService implements QuoteService, QuoteServiceLocal {

    private static final Logger log = LoggerFactory.getLogger(QuoteServiceImpl.class);
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;
    @Autowired
    CurrencyService currencyService;
    @Autowired
    DocumentsServiceLocal documentsService;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private InvoiceCFManager invoiceCFManager;
    @Autowired
    private PaymentMethodManager paymentMethodManager;
    @Autowired
    private QuoteHistoryManager quoteHistoryManager;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    @Qualifier("invoiceService")
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    @Qualifier("savedSaleQuoteViewPDFHandler")
    private IPostPDFHandler savedSaleQuoteViewPDFHandler;
    @Autowired
    @Qualifier("savedSaleOrderViewPDFHandler")
    private IPostPDFHandler savedSaleOrderViewPDFHandler;
    @Autowired
    @Qualifier("savedPurchaseOrderViewPDFHandler")
    private IPostPDFHandler savedPurchaseOrderViewPDFHandler;
    @Autowired
    private WfmJpaOperations jpaTemplate;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private SolrDbConsistencyManager solrDbConsistencyManager;
    @Autowired
    @Qualifier("allInOneService")
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private ItemStockManager itemStockManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private PriceLevelManager priceLevelManager;
    @Autowired
    @Qualifier("projectService")
    private ProjectServiceLocal projectServiceLocal;
    @Autowired
    @Qualifier("taskService")
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private RFQManager rfqManager;
    @Autowired
    private RFQItemManager rfqItemManager;
    @Autowired
    private RFPManager rfpManager;
    @Autowired
    private RFPItemManager rfpItemManager;
    @Autowired
    private RFQSupplierBidManager rfqSupplierBidManager;
    @Autowired
    private ClientSupplierAccessService clientSupplierAccessService;
    @Autowired
    private ComissionAllocateManager comissionAllocateManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private FolderRbacManager folderRbacManager;
    @Autowired
    private DiscountManager discountManager;
    @Autowired
    private RfqRfpNoteManager rfqRfpNoteManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private ItemTableSettingService itemTableSettingService;
    @Autowired
    private ItemTableSettingsServiceLocal itemTableSettingsServiceLocal;
    @Autowired
    private FormPropertyManager formPropertyManager;
    @Autowired
    private GoogleCalendarService googleCalendarService;
    @Autowired
    private CustomFormItemManager customFormItemManager;
    @Autowired
    private EmailTemplateServiceLocal emailTemplateServiceLocal;
    @Autowired
    private RFQCFManager rfqcfManager;
    @Autowired
    private RFPCFManager rfpcfManager;
    @Autowired
    private RFQItemCFManager rfqItemCFManager;
    @Autowired
    private RFPItemCFManager rfpItemCFManager;
    @Autowired
    private ShippingDataManager shippingDataManager;
    @Autowired
    private ShippingDataItemManager shippingDataItemManager;
    @Autowired
    private ChartConfigManager chartConfigManager;
    @Autowired
    private QuoteItemManager quoteItemManager;
    @Autowired
    private ItemSerialServiceLocal itemSerialService;
    @Autowired
    private ItemBatchServiceLocal itemBatchService;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private StockTransferManager stockTransferManager;
    @Autowired
    private StockTransferNoteManager stockTransferNoteManager;
    @Autowired
    private StockAdjustmentManager stockAdjustmentManager;
    @Autowired
    private StockAdjustmentNoteManager stockAdjustmentNoteManager;
    @Autowired
    private ProfileServiceLocal profileService;
    @Autowired
    private ShippingDataSolrComponent shippingDataSolrComponent;
    @Autowired
    private PurchaseOrderSolrComponent purchaseOrderSolrComponent;
    @Autowired
    private RequestForQuoteSolrComponent rfqSolrComponent;
    @Autowired
    private PurchaseInvoiceSolrComponent purchaseInvoiceSolrComponent;
    @Autowired
    private ItemTableSettingsManager itemTableSettingsManager;
    @Autowired
    private FifoFailureManager fifoFailureManager;

    private static Map<String, Double> getDynSearchFields() {
        final Map<String, Double> fields = new HashMap<>();
        fields.put(SolrTaskRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }

    public static Map<String, Double> getRFQSearchFields() {
        final Map<String, Double> fields = new HashMap<>();
        fields.put(SolrSaleInvoiceRepresenter.FIELD_CLIENT_NAME, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NUMBER, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_RFQ_NUMBER, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_NUMBER, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrTaskRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }

    public Boolean checkForAccess(final Integer quoteId) {
        final EdsQuote quote = this.quoteManager.get(quoteId);
        if (quote == null) {
            return null;
        } else {
            return !quote.isDeleted();
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewInvoice getQuoteSummaryData(final Integer id) {
        final NewInvoice quoteObject = this.invoiceCircularResolver.getQuote(id, null);
        final EdsQuote edsQuote = this.quoteManager.get(id);

        final List<FileResource> attachments = this.attachmentUtilsManager.getAttachments(Constants.F_SALE_QUOTE, edsQuote.getObjectID(), edsQuote.getObjectID());
        attachments.stream()
                .filter(file -> file.getFileName().contains("Approved_By_") && file.getFileName().endsWith(".pdf"))
                .findFirst()
                .ifPresent(file -> quoteObject.setAmazonLink(file.getAmazonLink()));

        final Set<GenericSettingsEnum> genericSettings = this.genericSettingsManager.getEnabledGenericSettings();

        /**
         * Sale quote/Purchase order invoiced items
         */
        if (!CollectionUtils.isEmpty(edsQuote.getInvoices())/* && !(edsQuote instanceof EdsSaleQuote && genericSettings.contains(GenericSettingsEnum.ENABLE_MULTI_QUOTE_CONVERT))*/) {
            final List<NewInvoice> items = new ArrayList<>();
            BigDecimal invoicedItemsTotalInBase = BigDecimal.ZERO;
            BigDecimal invoicedItemsTotalInTr = BigDecimal.ZERO;
            for (final EdsInvoice invoice : edsQuote.getInvoices()) {
                final NewInvoice inv = new NewInvoice();
                inv.setID(invoice.getObjectID());
                inv.setInvoiceDate(new DateNonConvertable(invoice.getInvoiceDate()));
                inv.setDueDate(new DateNonConvertable(invoice.getDueDate()));
                inv.setInvoiceNumber(invoice.getNumber());
                inv.setTotal(invoice.getTotal());
                inv.setStatus(invoice.getStatus().getName());
                // Addition for dueAmount of Sales Order
                invoicedItemsTotalInBase = invoicedItemsTotalInBase.add(invoice.getTotal());
                if (invoice.getTotalInInvoiceCurrency() != null) {
                    invoicedItemsTotalInTr = invoicedItemsTotalInTr.add(invoice.getTotalInInvoiceCurrency());
                    if (invoice.getPayments() != null && !invoice.getPayments().isEmpty()){
                        for (EdsInvoicePayment payment : invoice.getPayments()) {
                            if (payment.getCreditNote() != null && payment.getCreditNote().getTotalInInvoiceCurrency() != null) {
                                invoicedItemsTotalInTr = invoicedItemsTotalInTr.subtract(payment.getCreditNote().getTotalInInvoiceCurrency());
                            }
                        }
                    }
                }
                if (invoice instanceof EdsSaleInvoice) {
                    inv.setPercentage(((EdsSaleInvoice) invoice).getQuotePercent());
                }
                items.add(inv);
            }
            if (quoteObject.getTotalInInvoiceCurrency() != null && quoteObject.getTotalInInvoiceCurrency().compareTo(BigDecimal.ZERO) > 0) {
                quoteObject.setOrderDueAmount(quoteObject.getTotalInInvoiceCurrency().subtract(invoicedItemsTotalInTr).setScale(2, RoundingMode.HALF_UP));
            } else {
                quoteObject.setOrderDueAmount(quoteObject.getTotal().subtract(invoicedItemsTotalInBase).setScale(2, RoundingMode.HALF_UP));
            }
            quoteObject.setInvoicedItems(items.toArray(new NewInvoice[]{}));
        } else {
            quoteObject.setOrderDueAmount(edsQuote.getTotalInInvoiceCurrency());
        }

        quoteObject.setLayoutHTML(Constants.RECEIVABLE.equals(quoteObject.getType()) ? PathFinder.getLayoutHTML(Constants.SALE_QUOTE) : PathFinder.getLayoutHTML(Constants.PURCHASE_ORDER));
        quoteObject.setCustomItemColumns(this.itemTableSettingsServiceLocal.getColumnConfigs(Constants.RECEIVABLE.equals(quoteObject.getType()) && quoteObject.isSalesOrder() ? ItemTableEnum.SALE_ORDER_ITEM : Constants.RECEIVABLE.equals(quoteObject.getType()) ? ItemTableEnum.SALE_QUOTE_ITEM : ItemTableEnum.PURCHASE_ORDER_ITEM, false, true));

        final EdsCompany company = this.invoicingSettingsManager.getUser().getCompany();
        final EdsInvoicingSettings invSettings = this.invoicingSettingsManager.getInvoiceSettings(company);

        if (invSettings != null) {
            quoteObject.setConvertInvoiceBtnShow(invSettings.isConvertInvoiceBtnShow());
        }
        if (quoteObject.isSalesOrder()) {
            boolean hasGDNInSalesOrder = shippingDataManager.hasGDNInSalesOrder(quoteObject.getID());
            quoteObject.setHasGDN(hasGDNInSalesOrder);
        }
        if (Constants.PAYABLE.equals(quoteObject.getType())) {
            quoteObject.setAllocatedExpenses(this.expenseReportManager.getExpensesAllocatedToPO(id));
            quoteObject.setCancelRemainingQtyEnabled(genericSettings.contains(GenericSettingsEnum.CANCEL_REMAINING_QTY_ENABLED));
            final NewInvoiceItem[] quoteItems = quoteObject.getItems();
            for (final NewInvoiceItem quoteItem : quoteItems) {
                if (quoteItem.getNonConvertedQty().compareTo(BigDecimal.ZERO) > 0) {
                    quoteObject.setNonConvertedItemsExists(true);
                }

                if (quoteItem.getNonConvertedAmount().compareTo(BigDecimal.ZERO) > 0) {
                    quoteObject.setNonConvertedItemsExists(true);
                }
            }
        } else {
            final List<EdsShippingData> shippingDataList = this.shippingDataManager.getByQuoteId(quoteObject.getID());
            final EdsPickList pickList = this.pickListManager.getPickListBySaleQuoteID(quoteObject.getID());
            if (pickList != null) {
                quoteObject.setPickListID(pickList.getObjectID());
            }

            Integer countInvoicedGdn = 0;
            for (final EdsShippingData shippingData : shippingDataList) {
                if (shippingData != null && shippingData.getStatus() != null) {
                    countInvoicedGdn++;
                }
            }
            quoteObject.setAllGdnInvoiced(shippingDataList.size() - countInvoicedGdn == 0);
        }
        if (Constants.RECEIVABLE.equals(quoteObject.getType())) {
            quoteObject.getTypeItem().setSupplierCustomerBalance(this.crmAccountManager.getClientBalance(quoteObject.getClientID()).doubleValue());
        } else {
            final EdsCrmAccount clientBase = this.crmAccountManager.get(quoteObject.getClientID());
//            if (!clientBase.getBalanceCalculated()) {
            quoteObject.getTypeItem().setSupplierCustomerBalance(this.crmAccountManager.getSupplierBalance(clientBase.getObjectID()).doubleValue());

            quoteObject.getTypeItem().setReverseChargeApplicable(clientBase.isReverseChargeApplicable());
        }
        if (quoteObject.getBillAddressID() != null) {
            final EdsAddress address = this.addressManager.get(quoteObject.getBillAddressID());
            if (address != null) {
                quoteObject.setBillAddressAsHTML(address.getAddressDataAsHTML());
            }
        }
        final EdsAddress address = this.addressManager.get(quoteObject.getMailAddressID());
        if (address != null) {
            quoteObject.setMailAddressAsHTML(address.getAddressDataAsHTML());
            quoteObject.setCompanyMailAddressAsHTML(address.getAddressDataAsHTML());
        } else if (this.userManager.getUser() != null && this.userManager.getUser().getCompany() != null &&
                this.userManager.getUser().getCompany().getMailingAddress() != null) {
            quoteObject.setMailAddressAsHTML(this.userManager.getUser().getCompany().getMailingAddress().getAddressDataAsHTML());
            quoteObject.setCompanyMailAddressAsHTML(quoteObject.getMailAddressAsHTML());
        }

        if (quoteObject.getClientItem() != null && quoteObject.getClientItem().getMailAddressID() != null) {
            final EdsAddress mailAddress = this.addressManager.get(quoteObject.getClientItem().getMailAddressID());
            if (mailAddress != null) {
                quoteObject.getClientItem().setDropShipToMailAddressHTML(mailAddress.getAddressDataAsHTML());
            }
        }
        quoteObject.setRevisionHistoryEnabled(genericSettings.contains(GenericSettingsEnum.REVISION_HISTORY_ENABLED));
        if (quoteObject.isRevisionHistoryEnabled()) {
            quoteObject.setRevisionHistoryItems(this.invoiceManager.getRevisionHistory(id, Constants.RECEIVABLE.equals(quoteObject.getType()) ? Constants.SALE_QUOTE : Constants.PURCHASE_ORDER));
        }
        quoteObject.setProductSerialsEnabled(genericSettings.contains(GenericSettingsEnum.PRODUCT_SERIAL_ENABLED));
        quoteObject.setRoundingModeDisabled(genericSettings.contains(GenericSettingsEnum.ROUNDING_MODE_DISABLED));
        quoteObject.setDoubleTaxEnabled(genericSettings.contains(GenericSettingsEnum.DOUBLE_TAX_ENABLED));
        quoteObject.setCustomExcelEnabled(genericSettings.contains(GenericSettingsEnum.GENERATE_CUSTOM_EXCEL_ENABLED));
        quoteObject.setMultiQuoteConvertEnabled(genericSettings.contains(GenericSettingsEnum.ENABLE_MULTI_QUOTE_CONVERT));
        quoteObject.setDoubleDiscountEnabled(genericSettings.contains(GenericSettingsEnum.DOUBLE_DISCOUNT_ENABLE));

        if (edsQuote.getCurrentApprover() != null && edsQuote.getCurrentApprover().getExactEmployee() != null) {
            final EdsUser edsUser = edsQuote.getCurrentApprover().getExactEmployee();
            final Set<String> roles = edsUser.getRoleCODEs();
            for (final EdsApproverRoles edsApproverRoles : edsQuote.getCurrentApprover().getApproverRoles()) {
                final EdsRole edsRole = edsApproverRoles.getRole();
                if (roles.contains(edsRole.getCode()) && edsApproverRoles.getApproveForAll()) {
                    quoteObject.setApproveForAll(true);
                    break;
                }
            }
            if (!quoteObject.isApproveForAll()) {
                for (final EdsApproverEmployees edsApproverEmployees : edsQuote.getCurrentApprover().getApproverEmployees()) {
                    if (edsUser.getObjectID().equals(edsApproverEmployees.getEmployee()) && edsApproverEmployees.getApproveForAll()) {
                        quoteObject.setApproveForAll(true);
                        break;
                    }
                }
            }
        }
        final KpiLog kpiLog = getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(id);
        if (Constants.RECEIVABLE.equals(quoteObject.getType())) {
            kpiLog.setEntityName(EdsSaleQuote.class.getSimpleName());
            if (Constants.SALE_ORDER.equals(quoteObject.getStatusCode()) || quoteObject.isSalesOrder()) {
                kpiLog.setEntityType(KpiEntityType.SALE_ORDER);
                quoteObject.setPdfTemplateList(this.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.SALES_ORDER.name()));
                quoteObject.setSystemCustomFields(commonService.getCompanyCustomFields(ViewName.SaleOrderSystem));
            } else {
                kpiLog.setEntityType(KpiEntityType.SALE_QUOTE);
                quoteObject.setPdfTemplateList(this.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.SALES_QUOTE.name()));
                quoteObject.setSystemCustomFields(commonService.getCompanyCustomFields(ViewName.SaleQuoteSystem));
            }
            quoteObject.setProgressInvoicePDFTemplateList(this.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PROGRESS_INVOICING_VIEW.name()));
            ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "View Sale Quote");
        } else {
            kpiLog.setEntityName(EdsPurchaseOrder.class.getSimpleName());
            quoteObject.setPdfTemplateList(this.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PURCHASE_ORDER.name()));
            ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "View Purchase Order");
        }
        quoteObject.setGrnCount(this.getGdnGrnCount(id, false));
        return quoteObject;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewInvoice getQuote(final Integer id, final Integer externalFormID) {
        return this.invoiceCircularResolver.getQuote(id, externalFormID);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PickList getPickList(final Integer id) {
        EdsPickList item = this.pickListManager.get(id);

        if (item == null) {
            return null;
        }
        PickList result = item.getData(this.warehouseManager.getDefaultWarehouse());

        result.setGdnCount(this.getGdnGrnCount(id, true));

        final boolean isTalalCompany = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ALMADAR_PRODUCT_SERIAL_ENABLED);
        if (isTalalCompany) {//ALMADAR MEDICAL company
            for (final PickListItem pli : result.getItems()) {
                final ArrayList<CompanyCustomFieldItem> list = new ArrayList<>(1);
                list.add(this.commonService.getCompanyCustomFieldByEntityNameAndFieldName(ViewName.ProductServiceView, "ARTICLE"));
                pli.setArticleNumberCF(CustomFieldsUtils.setRPCCustomFieldItems(this.itemManager.get(pli.getItemID()).getCustomFields(), list).get(0));
            }
        }

        final EdsCrmAccount clientBase = this.crmAccountManager.get(result.getClientID());
        result.setSupplierCustomerBalance(this.crmAccountManager.getClientBalance(clientBase.getObjectID()));
        result.setBaseCurrency(getBaseCurrency());
        result.setLayoutHtml(PathFinder.getLayoutHTML(LayoutRPC.PICK_LIST_FORM));
        result.setGdnNumberData(invoiceCircularResolver.parseGdnNumberData());
        result.setProductSerialsEnabled(this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PRODUCT_SERIAL_ENABLED));
        result.setTemplates(this.invoiceServiceLocal.getCompanyPdfTemplates(AccountingConstants.PICK_LIST_VIEW).getItems());
        final EdsCompanyPdfTemplate template = this.companyPdfTemplateManager.getDefaultCompanyPdfTemplateByType(AccountingConstants.PICK_LIST_VIEW);
        if (template != null) {
            result.setSelectedTemplateId(template.getObjectID());
        }
        String key = CacheConstants.ITEM_TABLE_SECTION + "_" + ItemTableEnum.PICKLIST.getTitle() + "_" + SecurityContext.getCompanyID();
        ColumnConfigs[] settingsJSONData = RedisClient.getKey(key, ColumnConfigs[].class);

        if (settingsJSONData == null || settingsJSONData != null && settingsJSONData.length == 0) {
            Gson gson = new Gson();
            EdsItemTableSettings its = itemTableSettingsManager.getSettingsBySection(ItemTableEnum.PICKLIST);
            if (its != null)
                settingsJSONData = gson.fromJson(its.getSettingsJSONData(), ColumnConfigs[].class);
        }
        result.setItemTableColumns(settingsJSONData);
        return result;
    }

    @Override
    public BigDecimal getProductQTYInWarehouse(final Integer productId, final Integer warehouseId) {
        final ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setCaseID(productId);
        filterParameter.setWarehouseID(warehouseId);
        filterParameter.setShortList(false);

        final LinkedHashMap<Integer, BigDecimal> stockItemsQtyMap = this.itemManager.getStockValuationQTY(filterParameter, false);
        return stockItemsQtyMap.get(productId);
    }

    @Override
    public SaveResult saveSaleQuote(final NewInvoice data) {

        final Integer companyID = Integer.valueOf(getInstance().getCompanyId());
        final Set<GenericSettingsEnum> genericSettings = this.genericSettingsManager.getEnabledGenericSettings();
        final boolean enableIgnoreQuoteNumberValidation = genericSettings.contains(GenericSettingsEnum.IGNORE_QUOTE_NUMBER_VALIDATION);
        final boolean enableCreditLimitForQuote = genericSettings.contains(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED);
        final boolean enableSalesOrderNumbering = genericSettings.contains(GenericSettingsEnum.ENABLE_SALES_ORDER_NUMBERING);
        final boolean enableSalesQuotePicklist = genericSettings.contains(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST);
        final EdsFinancialSettings financialSettings = this.financialSettingsManager.getFinancialSettings();
        final EdsWarehouse defaultWarehouse = this.warehouseManager.getDefaultWarehouse();
        final String fourDigitNumber;
        final SaveResult saveResult = new SaveResult();

        final List<EdsSaleQuote> existingQuotes = this.quoteManager.getSalesQuoteByNumberGlobal(data.getInvoiceNumber(), data.isSalesOrder());

        final EdsCrmAccount crmAccount = this.crmAccountManager.get(data.getClientID());
        BigDecimal quoteCreditLimit = crmAccount.getQuoteCreditLimit();

        if (enableCreditLimitForQuote && quoteCreditLimit != null) {
            if (crmAccount.getCurrency() != null && !this.currencyService.getBaseCurrency().getId().equals(crmAccount.getCurrency().getObjectID())) {
                final Double exchangeRate = this.currencyService.getCurrencyRateByDate(crmAccount.getCurrency().getObjectID(), new DateNonConvertable(new Date())).getExchangeRate();
                quoteCreditLimit = quoteCreditLimit.divide(BigDecimal.valueOf(exchangeRate), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
            }

            if (quoteCreditLimit.subtract(data.getTotal().add(crmAccount.getOtherBalance())).compareTo(BigDecimal.ZERO) < 0) {
                saveResult.setCreditLimit(quoteCreditLimit);
                saveResult.setRemainingBalance(crmAccount.getOtherBalance().add(data.getTotal()));
                saveResult.setExceededCreditLimit(true);
                return saveResult;
            }
        }

        if (!enableIgnoreQuoteNumberValidation && existingQuotes != null && existingQuotes.size() > 0) {
            saveResult.setInvoiceExist(true);
            return saveResult;
        } else if (enableIgnoreQuoteNumberValidation && existingQuotes.size() > 0) {
            data.setNumberData(this.getQuoteNumber());
        }

        final EdsSaleQuote quote = new EdsSaleQuote();

        if (enableIgnoreQuoteNumberValidation && data.getNumberData() != null) {
            fourDigitNumber = data.getNumberData().getFourDigitNumber();
            data.setInvoiceNumber(data.getNumberData().getInvoiceNumber());
        } else {
            fourDigitNumber = data.getFourDigitNumber() != null ? data.getFourDigitNumber() : (enableSalesOrderNumbering && data.isSalesOrder()) ? this.getSalesOrderNumber().getFourDigitNumber() : this.getQuoteNumber().getFourDigitNumber();
        }
        quote.setFourDigitNumber(Integer.valueOf(fourDigitNumber));
        quote.setClient(this.clientManager.get(data.getClientID()));
//        if (data.getSupplierID() != null) {
//            quote.setSupplier(clientManager.get(data.getSupplierID()));
//        }
        quote.setRelatedProject(data.getRelatedProjectID() != null ? this.projectManager.get(data.getRelatedProjectID()) : null);
        quote.setTaxCalculationType(data.getTaxCalculationType());

        initInvoiceData(quote, data);

        if (data.getShippingMethodID() != null) {
            quote.setShippingMethod(this.shippingMethodManager.get(data.getShippingMethodID()));
            quote.setShippingAmount(data.getShippingPrice());
        }

        quote.setIntroduction(data.getIntroduction());
        quote.setPriceLevelID(data.getPriceLevel() != null ? data.getPriceLevel().getId() : null);
//        quote.setManager((data.getManager() != null && data.getManager().getId() != null) ? employeeManager.get(data.getManager().getId()) : null);
        quote.setProgressInvoicing(data.isProgressInvoicing());
        quote.setOpportunityID(data.getOpportunityID());
        quote.setTotalDiscount(data.getTotalDiscount());
        quote.setNetAmountTotal(data.getNetAmountTotal());
        quote.setTermsConditionsID(data.getPaymentInstructionID());
        quote.setBankAccount((data.getBankAccount() != null && data.getBankAccount().getId() != null) ? this.bankAccountManager.get(data.getBankAccount().getId()) : null);
        if (enableCreditLimitForQuote && !Constants.DRAFT.equals(data.getStatusCode()) && data.isSalesOrder()) {
            quote.getClient().setOtherBalance(quote.getClient().getOtherBalance().add(data.getTotal()));
        }

        this.initTaxTotals(quote, data.getTotalTaxItems());

        final Integer saleOrderID = this.initQuoteItemsForSave(data, quote, financialSettings, defaultWarehouse);

        if (data.getAllocateComissionItems() != null && !data.getAllocateComissionItems().isEmpty()) {
            this.createComissionAllocateItems(data.getAllocateComissionItems(), quote, false);
        }

        if (data.isSalesOrder()) {
            this.saveOrConvertSalesOrder(saleOrderID, true);

            if (Constants.SUBMITTED_TO_MANAGER.equals(data.getStatusCode())) {
                this.baseEventPostProcessor.registerEvent(SalesOrderEventListenerImpl.TYPE, SalesOrderEventListenerImpl.EVENT_SALE_ORDER_SUBMITTED_TO_MANAGER, quote, this.userManager.getUser());
            }
        } else {
            this.baseEventPostProcessor.registerEvent(SalesQuoteEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, quote, this.userManager.getUser());

            if (enableSalesQuotePicklist && !data.isProgressInvoicing()) {
                this.saveOrUpdateSalesQuoteEnablePicklist(quote.getObjectID());
            }
        }
        if (!isOk(data.getApprovers())) {
            quote.setEntityStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, data.getStatusCode()));
        }
        if (isOk(data.getApprovers())) {
            this.invoiceServiceLocal.saveInvoiceApprovers(quote, data.getApprovers(), data.getStatusCode(), Constants.APPROVE);
        }
        if (data.isRelationChanged()) {
            this.allInOneServiceLocal.saveRelations(quote.isSalesOrder() ? RelationItem.TYPE_SALEORDER : RelationItem.TYPE_SALEQUOTE, quote.getObjectID(), quote.getNumber(), data.getRelations());
        }

        quote.setCustomFields(this.invoiceServiceLocal.createInvoiceCustomFields(data.getCustomFieldItems()));
        quote.setUpdatedDate(new Date());
        if (data.getClientContactID() != null) {
            quote.setClientContact(this.crmContactManager.get(data.getClientContactID()));
        }

        if (data.getInvoiceTermsItem() != null && data.getInvoiceTermsItem().getId() != null) {
            quote.setInvoiceTerms(this.invoiceTermsManager.get(data.getInvoiceTermsItem().getId()));
        }
        final EdsPickList pickList = this.pickListManager.getPickListBySaleQuoteID(quote.getObjectID());
        try {
            saleQuoteSolrComponent.indexes(Collections.singletonList(quote), (pickList != null) ? Collections.singletonList(pickList) : null);
        } catch (final IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }

        final KpiLog kpiLog = getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSaleQuote.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        kpiLog.setEntityId(quote.getObjectID());
        if (data.isSalesOrder()) {
            kpiLog.setEntityType(KpiEntityType.SALE_ORDER);
            ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Add Sale Order");
        } else {
            kpiLog.setEntityType(KpiEntityType.SALE_QUOTE);
            ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Add Sale Quote");
        }

        //this piece of the code need to calculate project budget
        if (!data.isSalesOrder() && Constants.APPROVE.equals(data.getStatusCode())) {
            if (data.getRelatedProjectID() != null || this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                final EdsBusinessEvent event = this.baseEventPostProcessor.registerEvent(ProjectBudgetCustomEventListenerImpl.TYPE, ProjectBudgetCustomEventListenerImpl.SALE_QUOTE_APPROVE, quote, null);
                event.setCustomStringField(data.getRelatedProjectID() != null ? data.getRelatedProjectID().toString() : null);
            }
        }
        if (data.isSalesOrder()) {
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, quote, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_SALEORDER);

            final EdsBusinessEvent workflowApprovingEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), quote, this.userManager.getUser());
            workflowApprovingEvent.setEntityType(RelationItem.TYPE_SALEORDER);
        } else {
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, quote, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_SALEQUOTE);

            final EdsBusinessEvent workflowApprovingEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), quote, this.userManager.getUser());
            workflowApprovingEvent.setEntityType(RelationItem.TYPE_SALEQUOTE);
        }
        saveResult.setId(saleOrderID);
        saveResult.setNumber(quote.getNumber());

        //FROM API. SEND EMAIL
        if (data.isFromApi() && data.isSalesOrder() && data.getEmailTemplateID() != null) {
            this.sendToClient(quote, data.getEmailTemplateID());
        }

        return saveResult;
    }

    public SaveResult saveSaleQuoteForBatchImport(final NewInvoice data, Set<GenericSettingsEnum> genericSettings, EdsFinancialSettings financialSettings, EdsWarehouse defaultWarehouse) {

        final boolean enableIgnoreQuoteNumberValidation = genericSettings.contains(GenericSettingsEnum.IGNORE_QUOTE_NUMBER_VALIDATION);
        final boolean enableCreditLimitForQuote = genericSettings.contains(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED);
        final boolean enableSalesOrderNumbering = genericSettings.contains(GenericSettingsEnum.ENABLE_SALES_ORDER_NUMBERING);
        final boolean enableSalesQuotePicklist = genericSettings.contains(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST);
        final String fourDigitNumber;
        final SaveResult saveResult = new SaveResult();

        final List<EdsSaleQuote> existingQuotes = this.quoteManager.getSalesQuoteByNumberGlobal(data.getInvoiceNumber(), data.isSalesOrder());

        final EdsCrmAccount crmAccount = this.crmAccountManager.get(data.getClientID());
        BigDecimal quoteCreditLimit = crmAccount.getQuoteCreditLimit();

        if (enableCreditLimitForQuote && quoteCreditLimit != null) {
            if (crmAccount.getCurrency() != null && !this.currencyService.getBaseCurrency().getId().equals(crmAccount.getCurrency().getObjectID())) {
                final Double exchangeRate = this.currencyService.getCurrencyRateByDate(crmAccount.getCurrency().getObjectID(), new DateNonConvertable(new Date())).getExchangeRate();
                quoteCreditLimit = quoteCreditLimit.divide(BigDecimal.valueOf(exchangeRate), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
            }

            if (quoteCreditLimit.subtract(data.getTotal().add(crmAccount.getOtherBalance())).compareTo(BigDecimal.ZERO) < 0) {
                saveResult.setCreditLimit(quoteCreditLimit);
                saveResult.setRemainingBalance(crmAccount.getOtherBalance().add(data.getTotal()));
                saveResult.setExceededCreditLimit(true);
                return saveResult;
            }
        }

        if (!enableIgnoreQuoteNumberValidation && existingQuotes != null && !existingQuotes.isEmpty()) {
            saveResult.setInvoiceExist(true);
            return saveResult;
        } else if (enableIgnoreQuoteNumberValidation && !existingQuotes.isEmpty()) {
            data.setNumberData(this.getQuoteNumber());
        }

        final EdsSaleQuote quote = new EdsSaleQuote();

        if (enableIgnoreQuoteNumberValidation && data.getNumberData() != null) {
            fourDigitNumber = data.getNumberData().getFourDigitNumber();
            data.setInvoiceNumber(data.getNumberData().getInvoiceNumber());
        } else {
            fourDigitNumber = data.getFourDigitNumber() != null ? data.getFourDigitNumber() : (enableSalesOrderNumbering && data.isSalesOrder()) ? this.getSalesOrderNumber().getFourDigitNumber() : this.getQuoteNumber().getFourDigitNumber();
        }
        quote.setFourDigitNumber(Integer.valueOf(fourDigitNumber));
        quote.setClient(crmAccount);
        quote.setRelatedProject(data.getRelatedProjectID() != null ? this.projectManager.get(data.getRelatedProjectID()) : null);
        quote.setTaxCalculationType(data.getTaxCalculationType());

        initInvoiceData(quote, data);

        if (data.getShippingMethodID() != null) {
            quote.setShippingMethod(this.shippingMethodManager.get(data.getShippingMethodID()));
            quote.setShippingAmount(data.getShippingPrice());
        }

        quote.setIntroduction(data.getIntroduction());
        quote.setPriceLevelID(data.getPriceLevel() != null ? data.getPriceLevel().getId() : null);
//        quote.setManager((data.getManager() != null && data.getManager().getId() != null) ? employeeManager.get(data.getManager().getId()) : null);
        quote.setProgressInvoicing(data.isProgressInvoicing());
        quote.setOpportunityID(data.getOpportunityID());
        quote.setTotalDiscount(data.getTotalDiscount());
        quote.setNetAmountTotal(data.getNetAmountTotal());
        quote.setTermsConditionsID(data.getPaymentInstructionID());
        quote.setBankAccount((data.getBankAccount() != null && data.getBankAccount().getId() != null) ? this.bankAccountManager.get(data.getBankAccount().getId()) : null);
        if (enableCreditLimitForQuote && !Constants.DRAFT.equals(data.getStatusCode()) && data.isSalesOrder()) {
            quote.getClient().setOtherBalance(quote.getClient().getOtherBalance().add(data.getTotal()));
        }

        this.initTaxTotals(quote, data.getTotalTaxItems());

        final Integer saleOrderID = this.initQuoteItemsForSave(data, quote, financialSettings, defaultWarehouse);

        if (data.getAllocateComissionItems() != null && !data.getAllocateComissionItems().isEmpty()) {
            this.createComissionAllocateItems(data.getAllocateComissionItems(), quote, false);
        }

        if (data.isSalesOrder()) {
            this.saveOrConvertSalesOrder(saleOrderID, true);

            if (Constants.SUBMITTED_TO_MANAGER.equals(data.getStatusCode())) {
                this.baseEventPostProcessor.registerEvent(SalesOrderEventListenerImpl.TYPE, SalesOrderEventListenerImpl.EVENT_SALE_ORDER_SUBMITTED_TO_MANAGER, quote, this.userManager.getUser());
            }
        } else {
            this.baseEventPostProcessor.registerEvent(SalesQuoteEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, quote, this.userManager.getUser());

            if (enableSalesQuotePicklist && !data.isProgressInvoicing()) {
                this.saveOrUpdateSalesQuoteEnablePicklist(quote.getObjectID());
            }
        }
        if (!isOk(data.getApprovers())) {
            quote.setEntityStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, data.getStatusCode()));
        }
        if (isOk(data.getApprovers())) {
            this.invoiceServiceLocal.saveInvoiceApprovers(quote, data.getApprovers(), data.getStatusCode(), Constants.APPROVE);
        }
        if (data.isRelationChanged()) {
            this.allInOneServiceLocal.saveRelations(quote.isSalesOrder() ? RelationItem.TYPE_SALEORDER : RelationItem.TYPE_SALEQUOTE, quote.getObjectID(), quote.getNumber(), data.getRelations());
        }

        quote.setCustomFields(this.invoiceServiceLocal.createInvoiceCustomFields(data.getCustomFieldItems()));
        quote.setUpdatedDate(new Date());
        if (data.getClientContactID() != null) {
            quote.setClientContact(this.crmContactManager.get(data.getClientContactID()));
        }

        if (data.getInvoiceTermsItem() != null && data.getInvoiceTermsItem().getId() != null) {
            quote.setInvoiceTerms(this.invoiceTermsManager.get(data.getInvoiceTermsItem().getId()));
        }
        final EdsPickList pickList = this.pickListManager.getPickListBySaleQuoteID(quote.getObjectID());
        try {
            saleQuoteSolrComponent.indexes(Collections.singletonList(quote), (pickList != null) ? Collections.singletonList(pickList) : null);
        } catch (final IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }

        final KpiLog kpiLog = getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSaleQuote.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        kpiLog.setEntityId(quote.getObjectID());
        if (data.isSalesOrder()) {
            kpiLog.setEntityType(KpiEntityType.SALE_ORDER);
            ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Add Sale Order");
        } else {
            kpiLog.setEntityType(KpiEntityType.SALE_QUOTE);
            ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Add Sale Quote");
        }

        //this piece of the code need to calculate project budget
        if (!data.isSalesOrder() && Constants.APPROVE.equals(data.getStatusCode())) {
            if (data.getRelatedProjectID() != null || genericSettings.contains(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                final EdsBusinessEvent event = this.baseEventPostProcessor.registerEvent(ProjectBudgetCustomEventListenerImpl.TYPE, ProjectBudgetCustomEventListenerImpl.SALE_QUOTE_APPROVE, quote, null);
                event.setCustomStringField(data.getRelatedProjectID() != null ? data.getRelatedProjectID().toString() : null);
            }
        }
        if (data.isSalesOrder()) {
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, quote, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_SALEORDER);

            final EdsBusinessEvent workflowApprovingEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), quote, this.userManager.getUser());
            workflowApprovingEvent.setEntityType(RelationItem.TYPE_SALEORDER);
        } else {
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, quote, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_SALEQUOTE);

            final EdsBusinessEvent workflowApprovingEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), quote, this.userManager.getUser());
            workflowApprovingEvent.setEntityType(RelationItem.TYPE_SALEQUOTE);
        }
        saveResult.setId(saleOrderID);
        saveResult.setNumber(quote.getNumber());

        //FROM API. SEND EMAIL
        if (data.isFromApi() && data.isSalesOrder() && data.getEmailTemplateID() != null) {
            this.sendToClient(quote, data.getEmailTemplateID());
        }

        return saveResult;
    }

    private void sendToClient(final EdsSaleQuote quote, final Integer emailTemplateID) {
        String toText = null;
        Integer mailReceiverId = null;
        Integer contactId = null;
        SelectItem replyTo;
        String content = null;
        String subject = null;
        SelectItem fromEmail = null;
        final EdsCrmContact primaryContact = quote.getClientOrSupplier().getPrimaryContact();

        final SendToFormFillingData sendToFormFillingData = new SendToFormFillingData(quote.getClientOrSupplier().getObjectID(), Constants.SALES_ORDER_CATEGORY);
        final SendToFormFillingData formFillingData = this.invoiceService.getSendToFormData(sendToFormFillingData, primaryContact != null ? primaryContact.getObjectID() : null, true, null);

        if (formFillingData.getPrimaryContact() != null) {
            toText = formFillingData.getPrimaryContact().getName();
        } else if (formFillingData.getContacts() != null && formFillingData.getContacts().length > 0) {
            toText = formFillingData.getContacts()[0].getName();
        }

        if (primaryContact == null) {
            if (formFillingData.getPrimaryContact() != null) {
                mailReceiverId = formFillingData.getPrimaryContact().getId();
                contactId = formFillingData.getPrimaryContact().getId();
            } else if (formFillingData.getContacts() != null && formFillingData.getContacts().length > 0) {
                mailReceiverId = formFillingData.getContacts()[0].getId();
                contactId = formFillingData.getContacts()[0].getId();
            }
        } else {
            mailReceiverId = primaryContact.getObjectID();
            contactId = primaryContact.getObjectID();
        }
        final SelectItem[] result = this.messageCenterServiceLocal.getUserEmailAccounts(true);
        for (final SelectItem it : result) {
            if (it.isSelected()) {
                fromEmail = it;
                fromEmail.setSelected(true);
                break;
            }
        }

        final EntityToEmailTemplate item = new EntityToEmailTemplate();
        item.setEntityId(quote.getObjectID());
        item.setEntityType(Constants.SALES_ORDER_CATEGORY);
        item.setMailReceiverId(mailReceiverId);
        item.setEmailTemplateId(emailTemplateID);

        replyTo = new SelectItem();
        final String replyToText = this.emailTemplateServiceLocal.getReplyToById(emailTemplateID);
        if (replyToText != null && !"".equals(replyToText)) {
            replyTo.setId(emailTemplateID);
            replyTo.setName(replyToText);
        } else {
            replyTo = new SelectItem(this.userManager.getUser().getObjectID(), this.userManager.getUser().getEmail());
        }
        final EmailTemplateItem emailTemplateItem = this.emailTemplateServiceLocal.generateEmailTemplateData(item, null);
        if (emailTemplateItem != null && emailTemplateItem.getMessageHTML() != null) {
            content = emailTemplateItem.getMessageHTML();
            subject = emailTemplateItem.getSubject();
        }

        final MessageItem messageItem = new MessageItem();
        messageItem.setSubject(subject);
        messageItem.setClient(true);
        messageItem.setInvoiceID(quote.getObjectID());
        messageItem.setSendCopyToMe(true);
        messageItem.setMailContent(content);
        messageItem.setContactId(contactId);
        messageItem.setReceipt(true);
        messageItem.setEmailTemplateID(emailTemplateID);
        messageItem.setType(Constants.SALES_ORDER_CATEGORY);
        messageItem.setToEmails(toText);
        messageItem.setFromEmail(fromEmail != null ? fromEmail.getName() : null);
        messageItem.setReplyTo(replyTo.getName());
        messageItem.setFileResources(new ArrayList<>());

        this.sendToClientOrSupplier(messageItem);
    }

    private void createComissionAllocateItems(final List<AllocateComissionItem> allocateComissionItems, final EdsSaleQuote saleQuote, final boolean update) {
        EdsComissionAllocateItem comissionAllocateItem;
        if (update) {
            this.comissionAllocateManager.deleteAllocateItemsByQuote(saleQuote.getObjectID());
        }
        for (final AllocateComissionItem allocateComissionItem : allocateComissionItems) {
            comissionAllocateItem = new EdsComissionAllocateItem();
            comissionAllocateItem.setQuote(saleQuote);
            comissionAllocateItem.setComissionPercent(allocateComissionItem.getAllocatePercent());
            comissionAllocateItem.setAllocateAmount(allocateComissionItem.getAllocateTotal());
            if (allocateComissionItem.getSalesMan() != null) {
                final EdsUser salesMan = this.userManager.get(allocateComissionItem.getSalesMan().getId());
                comissionAllocateItem.setSalesMan(salesMan);
            }
            this.comissionAllocateManager.create(comissionAllocateItem);
        }
    }

    @Override
    @Transactional
    public Boolean isGdnNumberExist(String gdnNumber) {
        Boolean isGdnExist = this.shippingDataManager.getShippingDataByNumber(gdnNumber);
        return isGdnExist;
    }


    @Override
    @Transactional
    public Boolean updatePickList(final PickList data) {
        if (data == null || data.getId() == null) {
            return false;
        }
        EdsPickList pickList = this.pickListManager.get(data.getId());

        if (pickList == null || pickList.getSaleQuote() == null) {
            return false;
        }
        EdsSaleQuote saleQuote = this.quoteManager.getSaleQuote(pickList.getSaleQuote().getObjectID());

        pickList.setCarrierAccountID(data.getCarrierAccountID());
        pickList.setExpectedDate(data.getExpectedDate() != null ? data.getExpectedDate().getNonConvertedDate() : null);
        pickList.setPickDate(data.getPickDate() != null ? data.getPickDate().getNonConvertedDate() : null);
        pickList.setPackDate(data.getPackDate() != null ? data.getPackDate().getNonConvertedDate() : null);
        pickList.setShipDate(data.getShipDate() != null ? data.getShipDate().getNonConvertedDate() : null);
        pickList.setGrossWeight(data.getGrossWeight());
        EdsReference status = this.referenceManager.findReference(Constants.INVOICE_STATUS, data.getStatus());

        if (status != null) {
            pickList.setStatus(status);
            saleQuote.setStatus(status);
            // If status is shipped, set the status to partial invoiced
            if (saleQuote.getInvoices() != null && !saleQuote.getInvoices().isEmpty() && Constants.SHIPPED.equals(status.getCode())) {
                EdsReference partialInvoiced = this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.PARTIAL_INVOICED);
                saleQuote.setStatus(partialInvoiced);
            }
        }
        final NewInvoice newData = new NewInvoice();
        newData.setShippingFourDigitNumber(data.getGdnFourDigitNumber());
        newData.setShippingNumber(data.getGdnNumber());
        newData.setShippingLabel(data.getShippingLabel());
        newData.setReceiveDate(data.getShipDate());
        newData.setProductSerialItems(data.getProductSerialItems());
        final List<NewInvoiceItem> newDataItems = new ArrayList<>();
        final boolean isAlmadarSerials = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ALMADAR_PRODUCT_SERIAL_ENABLED);
        BigDecimal totalReadyToship = BigDecimal.ZERO;
        for (final PickListItem newItem : data.getItems()) {
            EdsQuoteItem qitem = this.quoteManager.getQuoteItemByID(newItem.getObjectID());

            if (qitem == null) {
                continue;
            }
            if (Optional.ofNullable(newItem.getShipped()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) > 0) {
                qitem.setShippedQty(newItem.getShipped().add(Optional.ofNullable(qitem.getShippedQty()).orElse(BigDecimal.ZERO)));
                if (isAlmadarSerials && Optional.ofNullable(newItem.getReadyToShip()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) > 0) {
                    qitem.setReadyToShip(newItem.getReadyToShip().subtract(newItem.getShipped()));
                    totalReadyToship = totalReadyToship.add(qitem.getReadyToShip());
                }
            }
            qitem.setShip(Optional.ofNullable(newItem.getShipped()).orElse(BigDecimal.ZERO));
            qitem.setQty(newItem.getQty());
            qitem.setReference(newItem.getReference());
            qitem.setNumberOfPacks(newItem.getNumberOfPacks());
            if (Optional.ofNullable(newItem.getNumberOfPacks()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) > 0) {
                qitem.setQtyPerPack(qitem.getQty().divide(newItem.getNumberOfPacks(), 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
            } else {
                qitem.setQtyPerPack(null);
            }
            if (newItem.getWarehouse() != null && newItem.getWarehouse().getId() != null) {
                qitem.setWarehouse(this.warehouseManager.get(newItem.getWarehouse().getId()));
            } else {
                qitem.setWarehouse(this.warehouseManager.getDefaultWarehouse());
            }
            final NewInvoiceItem newDataItem = new NewInvoiceItem();
            newDataItem.setID(newItem.getObjectID());
            newDataItem.setSerials(newItem.getSerials());
            newDataItem.setBatchItems(newItem.getAssignedBatchItems());
            newDataItems.add(newDataItem);
        }
        if (isAlmadarSerials) {
            this.updateSaleQuoteReadyToShip(saleQuote, totalReadyToship);
        }
        newData.setItems(newDataItems.toArray(new NewInvoiceItem[]{}));
        this.pickListManager.update(pickList);
        this.quoteManager.update(saleQuote);
        EdsShippingData shippingData = null;

        if (Objects.equals(Constants.SHIPPED, status.getCode()) || Objects.equals(Constants.PARTIAL_SHIPPED, status.getCode())) {
            shippingData = this.createShippingData(saleQuote, newData);

            if (saleQuote.isSalesOrder()) {
                final Integer transactionId = this.accountingServiceLocal.createTransactionForGoodsDelivered(saleQuote, shippingData);

                for (final EdsShippingDataItem shippingItem : shippingData.getItems()) {
                    final EdsQuoteItem quoteItem = this.quoteItemManager.get(shippingItem.getQuoteItemId());
                    if (quoteItem.getItem() != null && quoteItem.getItem().getInventoryTrackingEnabled()) {
                        this.itemSerialService.assignForGoodsDelivered(shippingItem, transactionId);
                    }
                    if (quoteItem.getItem() != null && quoteItem.getItem().getTrackBatchesEnabled()) {
                        this.itemBatchService.assignForGoodsDelivered(shippingData.getObjectID(), shippingItem);
                    }
                }
            }
        }
        this.baseEventPostProcessor.registerEvent(SalesOrderEventListenerImpl.TYPE, SalesOrderEventListenerImpl.EVENT_PICKLIST_SALE_ORDER, saleQuote, this.userManager.getUser(), shippingData);
        final Integer companyID = Integer.valueOf(getInstance().getCompanyId());

        try {
            saleQuoteSolrComponent.indexes(Collections.singletonList(saleQuote), Collections.singletonList(pickList));
        } catch (final IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean updatePickListItem(final PickList data) {
        if (data == null || data.getId() == null) {
            return false;
        }
        EdsPickList pickList = this.pickListManager.get(data.getId());

        if (pickList == null || pickList.getSaleQuote() == null) {
            return false;
        }
        EdsSaleQuote saleQuote = this.quoteManager.getSaleQuote(pickList.getSaleQuote().getObjectID());

        EdsReference status = this.referenceManager.findReference(Constants.INVOICE_STATUS, data.getStatus());
        if (status != null) {
            saleQuote.setStatus(status);
        }
        if (data.getPackDate() != null) {
            pickList.setPackDate(data.getPackDate() != null ? data.getPackDate().getNonConvertedDate() : null);
        }

        if (data.getPickDate() != null) {
            pickList.setPickDate(data.getPickDate() != null ? data.getPickDate().getNonConvertedDate() : null);
        }

        pickList.setExpectedDate(data.getExpectedDate() != null ? data.getExpectedDate().getNonConvertedDate() : null);

        for (final PickListItem newItem : data.getItems()) {
            EdsQuoteItem qitem = this.quoteManager.getQuoteItemByID(newItem.getObjectID());

            if (qitem == null) {
                continue;
            }
            qitem.setBookReservation(newItem.getBookReserve());
            if (newItem.getWarehouse() != null && newItem.getWarehouse().getId() != null) {
                qitem.setWarehouse(this.warehouseManager.get(newItem.getWarehouse().getId()));
            } else {
                qitem.setWarehouse(this.warehouseManager.getDefaultWarehouse());
            }
            qitem.setNumberOfPacks(newItem.getNumberOfPacks());
            qitem.setQtyPerPack(newItem.getQtyPerPack());
            qitem.setReference(newItem.getReference());

        }
        this.pickListManager.update(pickList);
        this.quoteManager.update(saleQuote);

        final Integer companyID = Integer.valueOf(getInstance().getCompanyId());

        try {
            saleQuoteSolrComponent.indexes(Collections.singletonList(saleQuote), Collections.singletonList(pickList));
        } catch (final IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean updateReadyToShipPickList(final PickList data) {
        if (data == null || data.getId() == null) {
            return false;
        }
        EdsPickList pickList = this.pickListManager.get(data.getId());

        if (pickList == null || pickList.getSaleQuote() == null) {
            return false;
        }
        EdsSaleQuote saleQuote = this.quoteManager.getSaleQuote(pickList.getSaleQuote().getObjectID());

        final NewInvoice newData = new NewInvoice();
        final List<NewInvoiceItem> newDataItems = new ArrayList<>();
        BigDecimal totalReadyToship = BigDecimal.ZERO;
        for (final PickListItem newItem : data.getItems()) {
            EdsQuoteItem qitem = this.quoteManager.getQuoteItemByID(newItem.getObjectID());

            if (qitem == null) {
                continue;
            }
            qitem.setReadyToShip(Optional.ofNullable(newItem.getReadyToShip()).orElse(BigDecimal.ZERO));
            totalReadyToship = totalReadyToship.add(qitem.getReadyToShip());
            final NewInvoiceItem newDataItem = new NewInvoiceItem();
            newDataItem.setID(newItem.getObjectID());
            newDataItems.add(newDataItem);
        }
        newData.setItems(newDataItems.toArray(new NewInvoiceItem[]{}));
        this.pickListManager.update(pickList);

        this.updateSaleQuoteReadyToShip(saleQuote, totalReadyToship);
        this.quoteManager.update(saleQuote);

        final Integer companyID = Integer.valueOf(getInstance().getCompanyId());
        try {
            saleQuoteSolrComponent.indexes(Collections.singletonList(saleQuote), (pickList != null) ? Collections.singletonList(pickList) : null);
        } catch (final IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
        if (totalReadyToship.compareTo(BigDecimal.ZERO) > 0) {
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                    BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT,
                    pickList,
                    this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_SHIPPING_DATA);
        }

        return true;
    }

    private void updateSaleQuoteReadyToShip(final EdsSaleQuote saleQuote, final BigDecimal totalReadyToship) {
        final List<CompanyCustomFieldItem> itemCustomFields = this.commonServiceLocal.getCompanyCustomFields(ViewName.SaleQuote);

        String columnCodeName = "";
        for (final CompanyCustomFieldItem customFieldItem : itemCustomFields) {
            if (customFieldItem.getFieldName().equals("Ready to Ship")) {
                columnCodeName = customFieldItem.getColumnCode();
                if (totalReadyToship.compareTo(BigDecimal.ZERO) > 0) {
                    customFieldItem.setFieldStringValue(String.join("-:-", customFieldItem.getPredefinedValues()));
                } else {
                    customFieldItem.setFieldStringValue("");
                }
            }
        }
        saleQuote.setCustomFields(this.saveCustomFields(saleQuote.getCustomFields(), itemCustomFields));
        saleQuote.addChange(columnCodeName);
    }

    @Transactional
    public EdsInvoiceCustomFields saveCustomFields(EdsInvoiceCustomFields edsInvoiceCustomFields, final List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (edsInvoiceCustomFields == null) {
                boolean isEmpty = true;
                for (final CompanyCustomFieldItem fieldItem : customFieldItems) {
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
                edsInvoiceCustomFields = new EdsInvoiceCustomFields();
                this.invoiceServiceLocal.createInvoiceCustomFields(customFieldItems);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsInvoiceCustomFields, customFieldItems);
            return edsInvoiceCustomFields;
        }
        return null;
    }

    private void updateGDNProductSerials(final EdsQuoteItem qitem, final Integer gdnID, final List<ProductSerialItem> productSerialItems) {
        if (productSerialItems != null && productSerialItems.size() > 0) {
            final boolean isAlmadarSerials = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ALMADAR_PRODUCT_SERIAL_ENABLED);
            for (final ProductSerialItem item : productSerialItems) {
                if (item.getObjectID() != null) {
                    if (isAlmadarSerials) {
                        final EdsProductSerial edsProductSerial = this.productSerialManager.get(item.getObjectID());
                        final List<EdsProductSerial> productSerials = this.productSerialManager.getProductSerialsByCount(qitem.getItem().getObjectID(), edsProductSerial, item.getQty().intValue());
                        for (final EdsProductSerial serial : productSerials) {
                            serial.setInvoiceItemID(qitem.getObjectID());
                            serial.setGdnid(gdnID);
                            this.productSerialManager.update(serial);
                        }
                    } else {
                        final EdsProductSerial serial = this.productSerialManager.get(item.getObjectID());
                        serial.setInvoiceItemID(qitem.getObjectID());
                        serial.setGdnid(gdnID);
                        this.productSerialManager.update(serial);
                    }
                }
            }
        }
    }

    @Override
    public SaveResult savePurchaseOrder(final NewInvoice data) {
        final SaveResult saveResult = new SaveResult();
        final String fourDigitNumber;
        Set<GenericSettingsEnum> genericSettings = this.genericSettingsManager.getByKeys(GenericSettingsEnum.IGNORE_QUOTE_NUMBER_VALIDATION,
                GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);
        boolean enableIgnoreQuoteNumberValidation = genericSettings.contains(GenericSettingsEnum.IGNORE_QUOTE_NUMBER_VALIDATION);
        final EdsFinancialSettings financialSettings = this.financialSettingsManager.getFinancialSettings();
        final EdsWarehouse defaultWarehouse = this.warehouseManager.getDefaultWarehouse();
        List<EdsPurchaseOrder> existingOrders = this.quoteManager.getPurchaseOrderByNumber(data.getInvoiceNumber(), null);

        if (!enableIgnoreQuoteNumberValidation && existingOrders != null && !existingOrders.isEmpty()) {
            saveResult.setInvoiceExist(true);
            return saveResult;
        } else if (enableIgnoreQuoteNumberValidation && !existingOrders.isEmpty()) {
            data.setNumberData(this.getOrderNumber());
        }
        final EdsPurchaseOrder order = new EdsPurchaseOrder();

        if (enableIgnoreQuoteNumberValidation && data.getNumberData() != null) {
            fourDigitNumber = data.getNumberData().getFourDigitNumber();
            data.setInvoiceNumber(data.getNumberData().getInvoiceNumber());
        } else {
            fourDigitNumber = data.getFourDigitNumber() != null ? data.getFourDigitNumber() : this.getOrderNumber().getFourDigitNumber();
        }
        if (data.getInvoiceTermsItem() != null && data.getInvoiceTermsItem().getId() != null) {
            order.setInvoiceTerms(this.invoiceTermsManager.get(data.getInvoiceTermsItem().getId()));
        }
        order.setFourDigitNumber(Integer.valueOf(fourDigitNumber));
        order.setSupplier(this.crmAccountManager.get(data.getClientID()));
        if (data.getOpportunityID() != null) {
            order.setOpportunityID(data.getOpportunityID());
        }
        //Purchase Order generic Data
        initInvoiceData(order, data);

        order.setTaxCalculationType(data.getTaxCalculationType());

        if (data.getShippingMethodID() != null) {
            order.setShippingMethod(this.shippingMethodManager.get(data.getShippingMethodID()));
            order.setShippingAmount(data.getShippingPrice());
        }

        order.setTotalDiscount(data.getTotalDiscount());
        order.setRelatedProject(data.getRelatedProjectID() != null ? this.projectManager.get(data.getRelatedProjectID()) : null);
        order.setFixedAssetRelated(data.isFixedAssetRelated());
        order.setPriceLevelID(data.getPriceLevel() != null ? data.getPriceLevel().getId() : null);
        applyPurchaseOrderData(order, data);
        order.setCustomFields(this.invoiceServiceLocal.createInvoiceCustomFields(data.getCustomFieldItems()));
        initTaxTotals(order, data.getTotalTaxItems());
        //Purchase Order Items
        final Integer purchaseOrderId = initQuoteItemsForSave(data, order, financialSettings, defaultWarehouse);

        final String purchaseOderNumber = order.getNumber();

        order.setObjectID(purchaseOrderId);

        if (!isOk(data.getApprovers())) {
            order.setEntityStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, data.getStatusCode()));
        }
        if (isOk(data.getApprovers())) {
            this.invoiceServiceLocal.saveInvoiceApprovers(order, data.getApprovers(), data.getStatusCode(), Constants.APPROVE);
        }
        if (data.getQuoteNumber() != null && !data.getQuoteNumber().equals("quoteNumber") && !data.getQuoteNumber().equals("")) {
            ArrayList<RelationItem> relationItems = new ArrayList<>();
            relationItems.add(new RelationItem(
                    null, data.getID(), RelationItem.TYPE_PURCHASE_ORDER, data.getName(), data.getQuoteId(), RelationItem.TYPE_SALEORDER, data.getQuoteNumber()));
            this.allInOneServiceLocal.saveRelations(RelationItem.TYPE_PURCHASE_ORDER, order.getObjectID(), order.getNumber(), relationItems);

        }
        if (data.isRelationChanged()) {
            this.allInOneServiceLocal.saveRelations(RelationItem.TYPE_PURCHASE_ORDER, order.getObjectID(), order.getNumber(), data.getRelations());
        }

        if (data.isFromSaasu()) {
            order.setUpdatedDate(data.getSaasuLastUpdateDate());
        } else {
            order.setUpdatedDate(new Date());
        }

        if (data.isFixedAssetRelated()) {
            try {
                data.getFixedAssetItem().setPurchaseOrderID(purchaseOrderId);
                data.getFixedAssetItem().setFinancedByAccount(this.accountingManager.getAccountByKey(EdsAccount.PENDING_GOODS_RECEIVED_NOTES).createAccountItem());
                data.getFixedAssetItem().setStatus(AccountingConstants.FIXED_ASSET_DRAFT);
                saveResult.setFixedAssetID(this.fixedAssetService.saveFixedAssetData(data.getFixedAssetItem()));
            } catch (final NumberExistingException e) {
                e.printStackTrace();
            }
        }
        this.baseEventPostProcessor.registerEvent(PurchaseOrderEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, order, this.userManager.getUser());
        addPurchaseOrderToSolr(order);
        convertRFPstoPO(data.getRelatedRFPIDs());

        final KpiLog kpiLog = getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPurchaseOrder.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        kpiLog.setEntityId(order.getObjectID());
        ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Add Purchase Order");

        //this piece of the code need to calculate project budget
        if (Constants.APPROVE.equals(data.getStatusCode()) &&
                (data.getRelatedProjectID() != null ||
                        genericSettings.contains(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE))) {
            final EdsBusinessEvent event = this.baseEventPostProcessor.registerEvent(ProjectBudgetCustomEventListenerImpl.TYPE,
                    ProjectBudgetCustomEventListenerImpl.PURCHASE_ORDER_APPROVE,
                    order,
                    null);
            event.setCustomStringField(data.getRelatedProjectID() != null
                    ? data.getRelatedProjectID().toString()
                    : null);
        }

        final EdsBusinessEvent workflowApprovingEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), order, this.userManager.getUser());
        workflowApprovingEvent.setEntityType(RelationItem.TYPE_PURCHASE_ORDER);

        final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                BaseEventsPostProcessorImpl.EVENT_TYPE_ADD,
                order,
                this.userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_PURCHASE_ORDER);

        saveResult.setId(purchaseOrderId);
        saveResult.setNumber(purchaseOderNumber);
        return saveResult;
    }

    private void applyPurchaseOrderData(final EdsPurchaseOrder order, final NewInvoice data) {
        order.setClientID(null);
        order.setClientMailAddressID(null);

        if (data.getClientItem() != null) {
            order.setClientID(data.getClientItem().getId());
            order.setClientMailAddressID(data.getClientItem().getMailAddressID());
        }
        order.setRequisitionedBy((data.getRequisitionedBy() != null && data.getRequisitionedBy().getId() != null) ? this.crmContactManager.get(data.getRequisitionedBy().getId()) : null);
        order.setPaymentMethod(data.getPaymentMethodID() != null ? this.paymentMethodManager.get(data.getPaymentMethodID()) : null);
        order.setPaymentTerms(data.getPaymentTerms());
        order.setShippingTerms(data.getShippingTerms());
        order.setQuoteNumber(data.getQuoteNumber());
        order.setApprover(data.getPurchaseOrderManager() != null && data.getPurchaseOrderManager().getId() != null ? this.userManager.get(data.getPurchaseOrderManager().getId()) : null);
        order.setShipDate(data.getShipDate() != null ? data.getShipDate().getNonConvertedDate() : null);
        order.setCancelDate(data.getCancelDate() != null ? data.getCancelDate().getNonConvertedDate() : null);
        order.setReverseChargeApplicable(data.isReversechargeApplicable());
        order.setQuoteId(data.getQuoteId());
        order.setUpdatedDate(new Date());
    }

    private void initTaxTotals(final EdsQuote quote, final TotalTaxItem[] totalTaxItems) {
        if (quote.getObjectID() != null) {
            this.quoteManager.deleteQuoteOldTaxTotals(quote);
        }
        if (totalTaxItems == null) {
            return;
        }
        List<EdsQuoteTaxTotal> totalTaxes = Lists.newLinkedList();

        for (final TotalTaxItem item : totalTaxItems) {
            final EdsQuoteTaxTotal totalTax = new EdsQuoteTaxTotal();
            totalTax.setQuote(quote);
            totalTax.setVat(this.vatManager.get(item.getTaxItem().getId()));
            totalTax.setAmount(item.getTaxAmount());
            totalTaxes.add(totalTax);
        }
        quote.setQuoteTaxTotals(totalTaxes);
    }

    private Integer initQuoteItemsForSave(final NewInvoice data, final EdsQuote quote, EdsFinancialSettings financialSettings, EdsWarehouse defaultWarehouse) {
        boolean isMultipleWarehouseEnabled = financialSettings.getEnableMultiWarehouse();
        List<EdsQuoteItem> items = Lists.newArrayListWithCapacity(data.getItems().length);

        if (!StringUtils.isEmpty(data.getSaasuGUID())) {
            quote.setSaasuGUID(data.getSaasuGUID());
        }
        if (data.getSaasuLastUpdateDate() != null) {
            quote.setSasuuLastUpdatedTime(data.getSaasuLastUpdateDate());
        }
        quote.setSaasuLastUpdatedUid(data.getSaasuLastUpdatedUid());
        quote.setCreationDate(new Date());
        this.quoteManager.create(quote);
        if (data.getAttachments() != null && data.getAttachments().length > 0) {
            if (quote instanceof EdsPurchaseOrder) {
                this.attachmentUtilsManager.saveAttachments(Constants.F_PUR_ORDER, quote.getObjectID(), quote.getObjectID(), data.getAttachments());
            } else if (quote instanceof EdsSaleQuote) {
                this.attachmentUtilsManager.saveAttachments(Constants.F_SALE_QUOTE, quote.getObjectID(), quote.getObjectID(), data.getAttachments());
            }
        }

        int sorder = 0;
        for (final NewInvoiceItem newItem : data.getItems()) {
            final EdsQuoteItem localItem = new EdsQuoteItem();
            localItem.setSorder(sorder++);
            if (!isMultipleWarehouseEnabled) {
                localItem.setWarehouse(defaultWarehouse);
            }
            localItem.setAssignedSerials(newItem.getAssignedSerials());
            localItem.setCustomFields(this.invoiceServiceLocal.createInvoiceItemCustomFields(newItem.getCustomFieldItems()));
            initInvoiceItemData(localItem, newItem);
            this.quoteItemManager.create(localItem);
            if (newItem.getAttachments() != null && !newItem.getAttachments().isEmpty()) {
                this.attachmentUtilsManager.saveAttachments(Constants.F_SALE_QUOTE_ITEM, localItem.getObjectID(), localItem.getObjectID(), newItem.getAttachments().toArray(new FileItem[]{}));
            }
            /*if (newItem.getInventoryTrackingEnabled()) {
                localItem.setSerials(newItem.getSerials());
                if (quote instanceof EdsPurchaseOrder) {
                    itemSerialService.createSerialNumbers(localItem, ItemSerialEntityType.PURCHASE_ORDER);
                } else if (quote instanceof EdsSaleQuote) {
                    itemSerialService.assignSerialNumbers(localItem, ItemSerialEntityType.SALES_ORDER);
                }
            }*/
            items.add(localItem);
            localItem.setQuote(quote);
        }
        quote.setQuoteItems(items);
        this.quoteManager.createOrUpdate(quote);
        createOrUpdateNoteAndHistory(data, quote);
        return quote.getObjectID();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getSaleQuoteData(final ListingFilterParameter filterParametrs, final ListLoadConfig config) {
        return this.invoiceCircularResolver.getSaleQuoteData(filterParametrs, config);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getSaleQuoteData(final ListingFilterParameter filterParametrs) {
        final KpiLog kpiLog = getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSaleQuote.class.getSimpleName());
        kpiLog.setEntityType(KpiEntityType.SALE_QUOTE);
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Get Sale Quote list");

        return this.invoiceCircularResolver.getSaleQuoteData(filterParametrs);
    }

    @Override
    public InvoiceList getSaleQuoteByCategoryId(Integer categoryId) {
        List<EdsSaleQuote> list = quoteManager.getSaleQuotesByCategoryId(categoryId);
        List<NewInvoice> quoteList = list.stream().map(EdsQuote::getQuoteData).collect(Collectors.toList());
        return new InvoiceList((ArrayList<NewInvoice>) quoteList, quoteList.size());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getSaleOrderData(final ListingFilterParameter filterParametrs, final ListLoadConfig config) {
        return this.invoiceCircularResolver.getSaleOrderData(filterParametrs, config);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getSaleOrderData(final ListingFilterParameter filterParametrs) {
        final KpiLog kpiLog = getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSaleQuote.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityType(KpiEntityType.SALE_ORDER);
        ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "get Sale Order list");

        return this.invoiceCircularResolver.getSaleOrderData(filterParametrs);
    }

    @Override
    public InvoiceList getSaleOrderDataByCategoryId(Integer categoryId) {
        List<EdsQuote> saleOrderByProductCategoryID = (ArrayList) quoteManager.getSaleOrderByProductCategoryID(categoryId);
        List<NewInvoice> collect = saleOrderByProductCategoryID.stream().map(EdsQuote::getQuoteData).collect(Collectors.toList());
        return new InvoiceList((ArrayList<NewInvoice>) collect, collect.size());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<PickList> getPickListData(final ListingFilterParameter filterParametrs) {
        final List<EdsPickList> list = this.pickListManager.list(filterParametrs);
        final Integer totalCount = this.pickListManager.listCount(filterParametrs);
        final PickList[] pickLists = new PickList[list.size()];
        int i = 0;
        for (final EdsPickList item : list) {
            pickLists[i] = item.getData(this.warehouseManager.getDefaultWarehouse());
            i++;
        }
        return new ListResult<PickList>(new ArrayList<>(asList(pickLists)), totalCount);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getPurchaseOrderData(final ListingFilterParameter filterParametrs, final ListLoadConfig config) {
        return this.invoiceCircularResolver.getPurchaseOrderData(filterParametrs, config);
    }

    @Override
    //@CheckPermission(permissions = {PermissionConstants.ACCOUNTING_PURCHASE_ORDER_LIST})
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getPurchaseOrderData(final ListingFilterParameter filterParametrs) {

        final KpiLog kpiLog = getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPurchaseOrder.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Get Purchase Order list");

        return this.invoiceCircularResolver.getPurchaseOrderData(filterParametrs);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListHeap getUserInfo() {
        final ListHeap listHeap = new ListHeap();
        final HashMap userInfo = new HashMap();

        final EdsUser user = this.quoteManager.getUser();
        final String billingInfo = this.quoteManager.getLastBillingInformation();
        final EdsFinancialSettings fs = this.financialSettingsManager.getFinancialSettings();

        userInfo.put("contactName", user.getName());
        userInfo.put("email", user.getEmail());
        userInfo.put("company", user.getCompany().getName());
        userInfo.put("notes", (billingInfo == null ? "" : billingInfo));
        userInfo.put("dueDate", this.getDueDate(user));
        userInfo.put("currency", fs.getCurrency() == null ? null : fs.getCurrency().getObjectID());
        userInfo.put("currencySymbol", this.getDefaultCurrencySymbol());
        listHeap.setUserInfo(userInfo);

        return listHeap;
    }

    @Override
    public Integer sendToClientOrSupplier(final MessageItem messageItem) {
        final EdsQuote quote = this.quoteManager.get(messageItem.getInvoiceID());
        final NewInvoice data = EdsQuote.getQuoteData(quote);
        data.setClientMessage(messageItem.getMailContent());

        Integer crmContactID = null;
        if (Constants.SALES_ORDER_CATEGORY.equals(messageItem.getType()) || Constants.SALES_QUOTE_CATEGORY.equals(messageItem.getType()) || Constants.PURCHASE_ORDER_CATEGORY.equals(messageItem.getType())) {
            crmContactID = messageItem.getContactId();
        }
        final Integer trackerID;
        if (quote instanceof EdsSaleQuote) {
            if (Constants.SALES_ORDER_CATEGORY.equals(messageItem.getType())) {
                trackerID = initDataForSending(messageItem, quote, data, this.savedSaleOrderViewPDFHandler.getPDFStream(new InvoiceQuoteRequestObject(messageItem.getInvoiceID(), messageItem.getPdfTemplateID(), null, crmContactID)), this.savedSaleOrderViewPDFHandler.getFileName(), this.clientManager.get(data.getClientID()), "Sales Order");
            } else {
                trackerID = initDataForSending(messageItem, quote, data, this.savedSaleQuoteViewPDFHandler.getPDFStream(new InvoiceQuoteRequestObject(messageItem.getInvoiceID(), messageItem.getPdfTemplateID(), null, crmContactID)), this.savedSaleQuoteViewPDFHandler.getFileName(), this.clientManager.get(data.getClientID()), "Quote");
            }
        } else {
            trackerID = initDataForSending(messageItem, quote, data, this.savedPurchaseOrderViewPDFHandler.getPDFStream(new InvoiceQuoteRequestObject(messageItem.getInvoiceID(), messageItem.getPdfTemplateID(), null, crmContactID)), this.savedPurchaseOrderViewPDFHandler.getFileName(), this.crmAccountManager.get(data.getClientID()), "Order");
        }

        if (trackerID != null && quote instanceof EdsSaleQuote) {
            this.addSaleQuoteToSolr((EdsSaleQuote) quote);
            //Register event in MyUpdate
            if (Constants.SALES_QUOTE_MANAGER_CATEGORY.equals(messageItem.getType())) {
                this.baseEventPostProcessor.registerEvent(SalesQuoteEventListenerImpl.TYPE, SalesQuoteEventListenerImpl.EVENT_SALES_QUOTE_SUBMITTED_TO_MANAGER, (EdsSaleQuote) quote, this.userManager.getUser());
            } else {
                this.baseEventPostProcessor.registerEvent(SalesQuoteEventListenerImpl.TYPE, SalesQuoteEventListenerImpl.EVENT_SALES_QUOTE_SEND_TO_CLIENT, (EdsSaleQuote) quote, this.userManager.getUser());
            }
        }
        if (trackerID != null && quote instanceof EdsPurchaseOrder) {
            if (Constants.PURCHASE_ORDER_MANAGER_CATEGORY.equals(messageItem.getType())) {
                this.baseEventPostProcessor.registerEvent(PurchaseOrderEventListenerImpl.TYPE, PurchaseOrderEventListenerImpl.EVENT_PURCHASE_ORDER_SUBMITTED_TO_MANAGER, (EdsPurchaseOrder) quote, this.userManager.getUser());
            } else {
                this.baseEventPostProcessor.registerEvent(PurchaseOrderEventListenerImpl.TYPE, PurchaseOrderEventListenerImpl.EVENT_PURCHASE_ORDER_SEND_TO_CLIENT, (EdsPurchaseOrder) quote, this.userManager.getUser());
            }
            this.addPurchaseOrderToSolr((EdsPurchaseOrder) quote);
        }
        return trackerID;
    }

    @Override
    public Integer convertToInvoice(final Integer quoteID) {
        final boolean lockClosedProjectItems = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.LOCK_COMPLETED_PROJECT_ITEMS);
        final Integer companyID = Integer.valueOf(getInstance().getCompanyId());
        final EdsQuote quote = this.quoteManager.get(quoteID);

        List<CompanyCustomFieldItem> itemCustomFields = null;
        ArrayList<CompanyCustomFieldItem> quoteItemCustomFields = null;
        ArrayList<CompanyCustomFieldItem> quoteCustomFields = null;
        ArrayList<CompanyCustomFieldItem> invoiceCustomFields = null;

        if (quote instanceof EdsSaleQuote) {
            itemCustomFields = this.commonService.getCompanyCustomFields(ViewName.SaleInvoiceItem);
            quoteItemCustomFields = this.commonService.getCompanyCustomFields(((EdsSaleQuote) quote).isSalesOrder() ? ViewName.SaleOrderItem : ViewName.SaleQuoteItem);
            quoteCustomFields = this.commonService.getCompanyCustomFields(((EdsSaleQuote) quote).isSalesOrder() ? ViewName.SaleOrder : ViewName.SaleQuote);
            invoiceCustomFields = this.commonService.getCompanyCustomFields(ViewName.SaleInvoice);
        } else if (quote instanceof EdsPurchaseOrder) {
            itemCustomFields = this.commonService.getCompanyCustomFields(ViewName.PurchaseInvoiceItem);
            quoteItemCustomFields = this.commonService.getCompanyCustomFields(ViewName.PurchaseOrderItem);
            quoteCustomFields = this.commonService.getCompanyCustomFields(ViewName.PurchaseOrder);
            invoiceCustomFields = this.commonService.getCompanyCustomFields(ViewName.PurchaseInvoice);
        }
        quote.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, quoteItemCustomFields));

        if (this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.REVISION_HISTORY_ENABLED)) {
            this.createRevisionHistory(quote);
        }

        final NewInvoice invoice = EdsQuote.getQuoteData(quote);
        invoice.setID(null);
        invoice.setStatusCode(Constants.DRAFT);
        invoice.setConvertedItemID(quote.getObjectID());

        // Transfer invoice-level custom fields from quote to invoice
        if (quote.getCustomFields() != null && quoteCustomFields != null && invoiceCustomFields != null) {
            HashMap<String, String> quoteEntityRefs = null;
            if (quote.getCustomFields().getJsonEntities() != null) {
                quoteEntityRefs = new Gson().fromJson(quote.getCustomFields().getJsonEntities(), new TypeToken<HashMap<String, String>>() {
                }.getType());
            }
            for (CompanyCustomFieldItem quoteCF : quoteCustomFields) {
                if (quoteCF.getAliasName() == null) {
                    continue;
                }
                // File based fields hold a Double file id bound to the source record - not transferable
                if (Constants.DATA_TYPE_FILE_UPLOAD.equals(quoteCF.getDataType()) || Constants.DATA_TYPE_PROFILE_IMAGE.equals(quoteCF.getDataType())) {
                    continue;
                }
                for (CompanyCustomFieldItem invoiceCF : invoiceCustomFields) {
                    if (quoteCF.getAliasName().equals(invoiceCF.getAliasName())
                            && quoteCF.getDataType().equals(invoiceCF.getDataType())) {

                        Object value = quote.getCustomFields().getValueByCode(quoteCF.getDataType(), quoteCF.getColumnCode());
                        if (value != null) {
                            if (Constants.DATA_TYPE_DATE.equals(quoteCF.getDataType())) {
                                invoiceCF.setFieldDateNonConvertedValue(new DateNonConvertable((Date) value));
                            } else if (Constants.DATA_TYPE_NUMBER.equals(quoteCF.getDataType())) {
                                invoiceCF.setFieldStringValue(value.toString());
                            } else {
                                invoiceCF.setFieldStringValue((String) value);
                            }
                        }

                        if (quoteEntityRefs != null && quoteEntityRefs.get(quoteCF.getColumnCode()) != null
                                && quoteCF.getUiType() != null && quoteCF.getUiType().equals(invoiceCF.getUiType())) {
                            try {
                                String[] keyValue = quoteEntityRefs.get(quoteCF.getColumnCode()).split("=");
                                if (Constants.TYPE_ENTITY_LOOKUP.equals(quoteCF.getUiType()) && keyValue.length > 1) {
                                    invoiceCF.setEntityType(new SelectItem(Integer.parseInt(keyValue[0])));
                                    invoiceCF.setSelectedId(Integer.parseInt(keyValue[1]));
                                } else if (Constants.UI_TYPE_LOOKUP.equals(quoteCF.getUiType()) || Constants.UI_TYPE_CURRENCY.equals(quoteCF.getUiType())) {
                                    invoiceCF.setSelectedId(Integer.parseInt(keyValue[0]));
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            // getCompanyCustomFields() sets objectId to the field-definition (settings) id.
            // createInvoiceCustomFields() interprets a non-null objectId as an existing
            // EdsInvoiceCustomFields row id, so it would reuse/overwrite an unrelated record
            // instead of creating a new one for this invoice - which then breaks saving the
            // converted invoice (its one-to-one custom fields row is already owned elsewhere).
            // Reset it so a fresh custom fields row is created, matching the item-level handling.
            for (CompanyCustomFieldItem invoiceCF : invoiceCustomFields) {
                invoiceCF.setObjectId(null);
            }
            invoice.setCustomFieldItems(invoiceCustomFields);
        }

        if (lockClosedProjectItems && Constants.PS_CLOSED.equals(invoice.getProjectStatusCode())) {
            invoice.setRelatedProjectID(null);
            invoice.setRelatedProject(null);
        }

        final Integer invoiceID;
        /*EdsPickList pickList = null;
        boolean accessCreatPickList = false;
        boolean isSaleOrder = false;*/


        if (quote instanceof EdsSaleQuote) {

            /*if (quote.getStatus().getCode().equals(SALE_ORDER) || quote.getStatus().getCode().equals(PICKED)
                    || quote.getStatus().getCode().equals(PACKED) || quote.getStatus().getCode().equals(SHIPPED)) {
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
                BigDecimal exchangeRate = invoice.getExchageRate() != null ? invoice.getExchageRate() : new BigDecimal(1);

                List<EdsPickListItem> items = new LinkedList<>();
                for (NewInvoiceItem newItem : invoice.getItems()) {
                    initCustomFieldForConvert(newItem, itemCustomFields);

                    if (lockClosedProjectItems && newItem.getProject() != null && newItem.getProject().getId() != null) {
                        EdsProject project = projectManager.get(newItem.getProject().getId());
                        if (PS_CLOSED.equals(project.getStatus().getCode())) {
                            newItem.setProject(null);
                        }
                    }
                    if (newItem.getItemID() != null && newItem.getItemID() != 0) {
                        EdsItem inventoryItem = itemManager.get(newItem.getItemID());

                        if (inventoryItem.getType() != null && inventoryItem.getType().equals(INVENTORY_ITEM)) {
                            accessCreatPickList = true;
                            EdsPickListItem listItem = new EdsPickListItem();
                            listItem.setItem(inventoryItem);
                            listItem.setQuantity(newItem.getQuantity());
                            listItem.setPickList(pickList);
                            items.add(listItem);

                            plTotal = plTotal.add(newItem.getTotalAmount().divide(exchangeRate, 4, BigDecimal.ROUND_HALF_UP));

                            if (newItem.getTaxAmount() != null) {
                                plTaxAmount = plTaxAmount.add(newItem.getTaxAmount().divide(exchangeRate, 4, BigDecimal.ROUND_HALF_UP));
                            }

                            BigDecimal discount;
                            if (newItem.getDiscountPercent() != null) {
                                discount = newItem.getNet().multiply(newItem.getDiscountPercent()).divide(HUNDRED, 4, BigDecimal.ROUND_HALF_UP);
                            } else {
                                discount = newItem.getDiscountAmount();
                            }
                            plDiscount = plDiscount.add(discount.divide(exchangeRate, 4, BigDecimal.ROUND_HALF_UP));
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
            final Integer quoteConvertToInvoiceCustomType = this.invoicingSettingsManager.getInvoiceSettings(null).getQuoteConvertToInvoiceCustomType();
            EdsReference ictype = null;

            if (quoteConvertToInvoiceCustomType != null) {
                ictype = this.referenceManager.get(quoteConvertToInvoiceCustomType);
            }
            final InvoiceNumberData numberData = ictype != null ? this.invoiceServiceLocal.getSaleInvoiceNumber(ictype.getDescription()) : this.invoiceServiceLocal.getSaleInvoiceNumber();

            if (numberData.isWithDate()) {
                numberData.setDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            }
            if (numberData.isWithClient()) {
                numberData.setClientCode(((EdsSaleQuote) quote).getClient().getNumber());
            }
            if (numberData.isWithProject() && quote.getRelatedProject() != null) {
                numberData.setProjectCode(quote.getRelatedProject().getNumber());
            }

            invoice.setInvoiceCustomType(ictype != null ? ictype.getCode() : null);
            invoice.setQuoteNumber(quote.getNumber());
            invoice.setPoNumber(quote.getPoNumber());
            invoice.setInvoiceNumber(numberData.getInvoiceNumber());
            if (quote.getShippingMethod() != null) {
                invoice.setShippingMethodID(quote.getShippingMethod().getObjectID());
                final ShippingMethod shm = quote.getShippingMethod().getRPC();
                shm.setCurrencyId(quote.getCurrency().getObjectID());
                shm.setExchangeRate(quote.getExchangeRate());

                if (quote.getShippingAmount() != null && quote.getShippingAmount().compareTo(BigDecimal.ZERO) > 0) {
                    shm.setPrice(quote.getShippingAmount());
                }
                invoice.setShippingPrice(shm.getPrice());
                invoice.setShippingMethod(shm);
            }
            if (quote.getPriceLevelID() != null) {
                final EdsPriceLevel priceLevel = this.priceLevelManager.get(quote.getPriceLevelID());
                invoice.setPriceLevel(new SelectItem(priceLevel.getObjectID(), priceLevel.getName()));
            }

            invoice.setHistoryList(this.getNotesForConvert(quoteID));
            invoice.setAttachments(this.invoiceCircularResolver.getAttachments(quoteID, Constants.F_SALE_QUOTE));

            invoice.setForceSave(true);
            invoiceID = this.invoiceService.saveSaleInvoice(invoice, quote.getNumber()).getId();
        } else {
            invoice.setPoNumber(quote.getNumber());
            invoice.setInvoiceNumber("");
            final EdsInvoicingSettings invSettings = this.invoicingSettingsManager.getInvoiceSettings(this.userManager.getUser().getCompany());

            if (invSettings != null && invSettings.getIsPurchaseInvoiceNumberingShow()) {
                final InvoiceNumberData data = this.invoiceCircularResolver.getPurchaseInvoiceNumberData(false);
                invoice.setNumberData(data);
                final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                data.setDate(format.format(invoice.getInvoiceDate() != null ? invoice.getInvoiceDate().getDate() : new Date()));
                if (invoice.getClientID() != null) {
                    data.setClientCode(this.crmAccountManager.get(invoice.getClientID()).getNumber());
                }
                invoice.setInvoiceNumber(data.getInvoiceNumber());
            }

            if (quote instanceof EdsPurchaseOrder po) {
                if (po.getClientID() != null) {
                    final EdsCrmAccount client = this.crmAccountManager.get(po.getClientID());
                    if (client != null) {
                        final TypeItem clientItem = new TypeItem(client.getObjectID(), client.getName(), null);
//                        clientItem.setBillAddressID(po.getClientBillAddressID());
                        clientItem.setMailAddressID(po.getClientMailAddressID());
                        invoice.setClientItem(clientItem);
                    }
                }

                for (final NewInvoiceItem newItem : invoice.getItems()) {
                    this.initCustomFieldForConvert(newItem, itemCustomFields);
                }
            }

            invoice.setHistoryList(this.getNotesForConvert(quoteID));
            invoice.setAttachments(this.invoiceCircularResolver.getAttachments(quoteID, Constants.F_PUR_ORDER));
            invoiceID = this.invoiceService.savePurchaseInvoice(invoice).getId();
        }
        quote.setConvertedToInvoice(true);
        if (!quote.getStatus().getCode().equals(Constants.OPEN)) {
            quote.setStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.CONVERTED));
        }
        if (quote instanceof EdsSaleQuote) {
            this.createQuoteHistory((EdsSaleQuote) quote);
            try {
                saleQuoteSolrComponent.indexes(Collections.singletonList((EdsSaleQuote) quote), null);
            } catch (final IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (quote instanceof EdsPurchaseOrder) {
            this.addPurchaseOrderToSolr((EdsPurchaseOrder) quote);
        }
        return invoiceID;
    }

    private void initCustomFieldForConvert(final NewInvoiceItem newItem, final List<CompanyCustomFieldItem> itemCustomFields) {
        if (itemCustomFields != null && !itemCustomFields.isEmpty() && newItem.getCustomFieldItems() != null) {
            newItem.setID(null);

            final ArrayList<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();
            for (final CompanyCustomFieldItem itemCustomField : itemCustomFields) {

                if (newItem.getCustomFieldByAlias(itemCustomField.getAliasName()) != null) {
                    final CompanyCustomFieldItem fitem = itemCustomField.cloneObject();
                    fitem.setFieldStringValue(newItem.getCustomFieldByAlias(itemCustomField.getAliasName()).getFieldStringValue());
                    customFieldItems.add(fitem);
                }
            }
            newItem.setCustomFieldItems(customFieldItems);
        }
    }

    private HistoryListItem[] getNotesForConvert(final Integer quoteID) {
        final HistoryListItem[] noteItems = this.invoiceCircularResolver.getQuoteNotes(quoteID);
        if (noteItems != null) {
            for (final HistoryListItem noteItem : noteItems) {
                if (noteItem != null) {
                    noteItem.setObjectID(null);
                }
            }
        }
        return noteItems;
    }

    @Override
    @Transactional
    public SaveResult updateSaleQuote(final NewInvoice data) {
        final Integer companyID = Integer.valueOf(getInstance().getCompanyId());
        final boolean enableSalesQuotePicklist = this.genericSettingsManager.isSettingsEnabled((GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST));
        final EdsSaleQuote quote = (EdsSaleQuote) this.quoteManager.get(data.getID());

        final SaveResult saveResult = new SaveResult();
        final List<EdsSaleQuote> existingQuotes = this.quoteManager.getSalesQuoteByNumberGlobal(data.getInvoiceNumber(), quote.isSalesOrder());
        if (existingQuotes != null && existingQuotes.size() > 0) {
            for (final EdsSaleQuote q : existingQuotes) {
                if (!q.getObjectID().equals(quote.getObjectID()) && !quote.getNumber().equals(quote.getFromNumber())) {
                    saveResult.setInvoiceExist(true);
                    return saveResult;
                }
            }
        }

        // Sales Order status logic: do not change
        List<EdsShippingData> notConvertedGDNs = shippingDataManager.getNotConvertedGDNs(data.getID());
        BigDecimal remainingQty = quoteManager.getRemainingQtyByQuoteId(quote.getObjectID());
        List<EdsInvoice> invoices = quote.getInvoices();

        if (data.isSalesOrder() && Constants.SALE_ORDER.equals(data.getStatusCode())) {
            if (notConvertedGDNs != null && !notConvertedGDNs.isEmpty()) {
                data.setStatusCode(Constants.PARTIAL_SHIPPED);
                if (remainingQty.compareTo(BigDecimal.ZERO) == 0) {
                    data.setStatusCode(Constants.SHIPPED);
                    if (invoices != null && !invoices.isEmpty()) {
                        data.setStatusCode(Constants.PARTIAL_INVOICED);
                    }
                }
            } else {
                boolean hasGDNInSalesOrder = shippingDataManager.hasGDNInSalesOrder(data.getID());
                if (hasGDNInSalesOrder) {
                    if (remainingQty.compareTo(BigDecimal.ZERO) > 0) {
                        data.setStatusCode(Constants.PARTIAL_SHIPPED);
                    }
                } else if (invoices != null && !invoices.isEmpty() && invoices.stream().anyMatch(inv ->
                        inv.getDueAmount() != null
                                && inv.getDueAmount().compareTo(BigDecimal.ZERO) != 0)) {
                    data.setStatusCode(Constants.PARTIAL_INVOICED);
                    if (quote.getStatus().getCode().equals(Constants.INVOICED)) {
                        data.setStatusCode(Constants.INVOICED);
                    }
                    if (quote.getStatus().getCode().equals(Constants.CONVERTED)) {
                        data.setStatusCode(Constants.CONVERTED);
                    }
                }
            }
        }

        if (this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.REVISION_HISTORY_ENABLED)) {
            this.createRevisionHistory(quote);
        }
        quote.setClient(this.clientManager.get(data.getClientID()));
//        if (data.getSupplierID() != null) {
//            quote.setSupplier(clientManager.get(data.getSupplierID()));
//        }
        initInvoiceData(quote, data);

        if (isOk(data.getApprovers())) {
            data.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirstApprover = true;
            for (final ApproverItemMini approverItem : data.getApprovers()) {
                final EdsApprover _edsApprover = this.approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        final EdsUser user_ = this.userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    this.approverManager.update(_edsApprover);
                    if (Constants.SUBMITTED_TO_MANAGER.equals(data.getStatusCode()) && isFirstApprover) {
                        quote.setPrevApprover(null);
                        quote.setCurrentApprover(_edsApprover);
                        quote.getCurrentApprover().setStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, data.getStatusCode()));
                        quote.setEntityStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
                        isFirstApprover = false;
                    } else if (quote.getCurrentApprover() != null && data.getStatusCode() != null && isFirstApprover) {
                        quote.getCurrentApprover().setStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, data.getStatusCode()));
                        quote.setEntityStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
                        isFirstApprover = false;
                    } else if (quote.getCurrentApprover() != null && data.getStatusCode() != null) {
                        quote.getCurrentApprover().setStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
                    }
                    if (data.getStatusCode() != null && !Constants.APPROVE.equals(data.getStatusCode())) {
                        quote.setEntityStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, data.getStatusCode()));
                    }
                    if (quote.isCurrentApproverRejected()) {
                        quote.setEntityStatus(quote.getCurrentApprover().getStatus());
                    }
                    continue;
                }
                final EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(quote.getObjectID());
                edsApprover.setIs_default(false);
                if (data.getStatusCode() != null && isFirstApprover) {
                    edsApprover.setStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, data.getStatusCode()));
                    if (Constants.DRAFT.equals(data.getStatusCode())) {
                        quote.setEntityStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, data.getStatusCode()));
                    } else {
                        quote.setEntityStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
                    }
                    isFirstApprover = false;
                } else if (data.getStatusCode() != null) {
                    edsApprover.setStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
                }
                if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                    final EdsUser user_ = this.userManager.get(approverItem.getExactEmployee().getId());
                    edsApprover.setExactEmployee(user_);
                }
                edsApprover.setApproverRoles(new HashSet<>());
                edsApprover.setApproverEmployees(new HashSet<>());
                edsApprover.setDynamicQueries(new HashSet<>());
                this.approverManager.createOrUpdate(edsApprover);
                for (final EdsApproverRoles roleapp : _edsApprover.getApproverRoles()) {
                    edsApprover.getApproverRoles().add(roleapp);
                }
                for (final EdsApproverEmployees ucerapp : _edsApprover.getApproverEmployees()) {
                    edsApprover.getApproverEmployees().add(ucerapp);
                }
                if (quote.getCurrentApprover() == null) {
                    quote.setCurrentApprover(edsApprover);
                }
                quote.getApprovers().add(edsApprover);
            }
        }
        quote.setRelatedProject(data.getRelatedProjectID() != null
                ? this.projectManager.get(data.getRelatedProjectID())
                : null);
        quote.setIntroduction(data.getIntroduction());
        quote.setPriceLevelID(data.getPriceLevel() != null ? data.getPriceLevel().getId() : null);
        quote.setTaxCalculationType(data.getTaxCalculationType());

        if (data.getFourDigitNumber() != null) {
            quote.setFourDigitNumber(Integer.valueOf(data.getFourDigitNumber()));
        }
        quote.setTotalDiscount(data.getTotalDiscount());
        quote.setNetAmountTotal(data.getNetAmountTotal());
        quote.setTermsConditionsID(data.getPaymentInstructionID());
        quote.setProgressInvoicing(data.isProgressInvoicing());
        quote.setBankAccount((data.getBankAccount() != null && data.getBankAccount().getId() != null) ? this.bankAccountManager.get(data.getBankAccount().getId()) : null);
        this.initTaxTotals(quote, data.getTotalTaxItems());
        saveResult.setId(this.initQuoteItemsForUpdate(data, quote, ""));
        if (data.getInvoiceTermsItem() != null && data.getInvoiceTermsItem().getId() != null) {
            quote.setInvoiceTerms(this.invoiceTermsManager.get(data.getInvoiceTermsItem().getId()));
        } else {
            quote.setInvoiceTerms(null);
        }
        if (quote.isSalesOrder()) {

            if (!data.isProgressInvoicing() && Constants.SALE_ORDER.equals(data.getStatusCode())) {
                final EdsPickList pickList = this.pickListManager.getPickListBySaleQuoteID(quote.getObjectID());

                if (pickList == null) {
                    this.saveOrConvertSalesOrder(quote.getObjectID(), false);
                }
            }

            this.baseEventPostProcessor.registerEvent(SalesOrderEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, quote, this.userManager.getUser());
        } else {
            this.baseEventPostProcessor.registerEvent(SalesQuoteEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, quote, this.userManager.getUser());
            if (enableSalesQuotePicklist && !data.isProgressInvoicing()) {
                this.saveOrUpdateSalesQuoteEnablePicklist(quote.getObjectID());
            }
        }
        quote.setCustomFields(this.invoiceServiceLocal.createInvoiceCustomFields(data.getCustomFieldItems()));
        quote.setUpdatedDate(new Date());

        String entityType = RelationItem.TYPE_SALEQUOTE;
        if (quote.isSalesOrder()) {
            entityType = RelationItem.TYPE_SALEORDER;
        }

        final EdsBusinessEvent workflowApprovingEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), quote, this.userManager.getUser());
        workflowApprovingEvent.setEntityType(entityType);

        if (data.getAllocateComissionItems() != null && !data.getAllocateComissionItems().isEmpty()) {
            this.createComissionAllocateItems(data.getAllocateComissionItems(), quote, true);
        }

        try {
            saleQuoteSolrComponent.indexes(Collections.singletonList(quote), null);
        } catch (final IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }

        if (data.isChangedNumber()) {
            final List<EdsPurchaseOrder> relatedPurchaseOrderList = this.quoteManager.getPurchaseOrderByQuoteId(quote.getObjectID());

            if (relatedPurchaseOrderList != null && !relatedPurchaseOrderList.isEmpty()) {
                for (final EdsPurchaseOrder aRelatedPurchaseOrderList : relatedPurchaseOrderList) {
                    aRelatedPurchaseOrderList.setQuoteNumber(data.getInvoiceNumber());
                }
                try {
                    purchaseOrderSolrComponent.indexes(relatedPurchaseOrderList);
                } catch (final IOException | SolrServerException | InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        final KpiLog kpiLog = getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSaleQuote.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        kpiLog.setEntityId(quote.getObjectID());
        if (quote.isSalesOrder()) {
            kpiLog.setEntityType(KpiEntityType.SALE_ORDER);
            ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Update Sale order");
        } else {
            kpiLog.setEntityType(KpiEntityType.SALE_QUOTE);
            ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Update Sale Quote");
        }


        //FROM API. SEND EMAIL
        if (data.isFromApi() && quote.isSalesOrder() && data.getEmailTemplateID() != null) {
            this.sendToClient(quote, data.getEmailTemplateID());
        }

        final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, quote, this.userManager.getUser());
        workflowEvent.setEntityType(entityType);

        return saveResult;
    }

    @Override
    public void updateSaleQuoteCustomFields(final NewInvoice data) {
        final Integer companyID = Integer.valueOf(getInstance().getCompanyId());
        final EdsSaleQuote quote = (EdsSaleQuote) this.quoteManager.get(data.getID());
        quote.setCustomFields(this.invoiceServiceLocal.createInvoiceCustomFields(data.getCustomFieldItems()));
        quote.setUpdatedDate(new Date());

        try {
            saleQuoteSolrComponent.indexes(Collections.singletonList(quote), null);
        } catch (final IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SaveResult updatePurchaseOrder(final NewInvoice data, final boolean checkForUnallocatedExpenses) {
        SaveResult saveResult = new SaveResult();
        EdsPurchaseOrder order = (EdsPurchaseOrder) this.quoteManager.get(data.getID());
        String status = order.getStatus().getCode();
        List<EdsPurchaseOrder> existingPOs = this.quoteManager.getPurchaseOrderByNumber(data.getInvoiceNumber(), order.getCreationDate());
        boolean hasGRN = shippingDataManager.hasGRNInPurchaseOrder(order.getObjectID());
        boolean stayInSameStatus = !Constants.RECEIVED.equals(data.getStatusCode()) && ((order.getInvoices() != null && !order.getInvoices().isEmpty()) || hasGRN);
        if (stayInSameStatus) {
            data.setStatusCode(status);
        }

        for (final EdsPurchaseOrder po : existingPOs) {
            if (!po.getObjectID().equals(order.getObjectID())) {
                saveResult.setInvoiceExist(true);
                return saveResult;
            }
        }
        if (checkForUnallocatedExpenses &&
                (data.getAllocatedExpenses() == null || data.getAllocatedExpenses().size() == 0) &&
                !this.expenseReportManager.getPurchaseOrderRelatedExpenseItems(order.getObjectID()).isEmpty()) {
            saveResult.setUnallocatedExpensesExist(true);
            return saveResult;
        }
        if (this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.REVISION_HISTORY_ENABLED)) {
            createRevisionHistory(order);
        }

        //this piece of the code need to calculate project budget
        if (!Constants.APPROVE.equals(order.getStatus().getCode()) && Constants.APPROVE.equals(data.getStatusCode())) {
            if (data.getRelatedProjectID() != null || this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                final EdsBusinessEvent event = this.baseEventPostProcessor.registerEvent(ProjectBudgetCustomEventListenerImpl.TYPE, ProjectBudgetCustomEventListenerImpl.PURCHASE_ORDER_APPROVE, order, null);
                event.setCustomStringField(data.getRelatedProjectID() != null ? data.getRelatedProjectID().toString() : null);
                this.baseEventPostProcessor.registerEvent(PurchaseOrderEventListenerImpl.TYPE, PurchaseOrderEventListenerImpl.EVENT_PURCHASE_ORDER_CLIENT_APPROVE, order, this.userManager.getUser());
            }
        }
        if (data.getInvoiceTermsItem() != null && data.getInvoiceTermsItem().getId() != null) {
            order.setInvoiceTerms(this.invoiceTermsManager.get(data.getInvoiceTermsItem().getId()));
        } else {
            order.setInvoiceTerms(null);
        }
        order.setSupplier(this.crmAccountManager.get(data.getClientID()));
        initInvoiceData(order, data);
        order.setTaxCalculationType(data.getTaxCalculationType());

        if (data.getShippingMethodID() != null) {
            order.setShippingMethod(this.shippingMethodManager.get(data.getShippingMethodID()));
            order.setShippingAmount(data.getShippingPrice());
        }
        order.setTotalDiscount(data.getTotalDiscount());
        order.setRelatedProject(data.getRelatedProjectID() != null ? this.projectManager.get(data.getRelatedProjectID()) : null);
        order.setReceiveDate(data.getReceiveDate() != null ? data.getReceiveDate().getNonConvertedDate() : order.getReceiveDate());
        order.setPriceLevelID(data.getPriceLevel() != null ? data.getPriceLevel().getId() : null);

        if (data.getFourDigitNumber() != null) {
            order.setFourDigitNumber(Integer.valueOf(data.getFourDigitNumber()));
        }
        applyPurchaseOrderData(order, data);

        if (!isOk(data.getApprovers()) && stayInSameStatus == false) {
            order.setEntityStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, data.getStatusCode()));
        }
        if (isOk(data.getApprovers()) && stayInSameStatus == false) {
            this.invoiceServiceLocal.saveInvoiceApprovers(order, data.getApprovers(), data.getStatusCode(), Constants.APPROVE);

            final EdsBusinessEvent workflowApprovingEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), order, this.userManager.getUser());
            workflowApprovingEvent.setEntityType(RelationItem.TYPE_PURCHASE_ORDER);
        }
        order.setCustomFields(this.invoiceServiceLocal.createInvoiceCustomFields(data.getCustomFieldItems()));
        if (data.isFromSaasu()) {
            order.setUpdatedDate(data.getSaasuLastUpdateDate());
        } else {
            order.setUpdatedDate(new Date());
        }
        initTaxTotals(order, data.getTotalTaxItems());
        if (data.getAllocatedExpenses() != null) {
            for (final Integer expID : data.getAllocatedExpenses().keySet()) {
                final EdsExpense expense = this.expenseManager.get(expID);
                expense.setPurchaseOrder(order);
                this.expenseManager.update(expense);
            }
        }

        if (Constants.RECEIVED.equals(data.getStatusCode())) {
            this.baseEventPostProcessor.registerEvent(PurchaseOrderEventListenerImpl.TYPE, PurchaseOrderEventListenerImpl.EVENT_PURCHASE_ORDER_RECEIVED, order, this.userManager.getUser());
        } else if (Constants.PARTIAL_RECEIVED.equals(data.getStatusCode())) {
            this.baseEventPostProcessor.registerEvent(PurchaseOrderEventListenerImpl.TYPE, PurchaseOrderEventListenerImpl.EVENT_PURCHASE_ORDER_PARTIAL_RECEIVED, order, this.userManager.getUser());
        } else {
            this.baseEventPostProcessor.registerEvent(PurchaseOrderEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, order, this.userManager.getUser());
        }

        if (order.isFixedAssetRelated() && Constants.RECEIVED.equals(order.getStatus().getCode())) {
            final EdsFixedAsset fixedAsset = this.fixedAssetManager.getFixedAssetByPurchaseOrder(order.getObjectID());
            if (fixedAsset != null) {
                fixedAsset.setStatus(AccountingConstants.FIXED_ASSET_APPROVED);
                this.fixedAssetManager.update(fixedAsset);
                this.fixedAssetService.createOrUpdateFixedAssetTransaction(fixedAsset);
                saveResult.setFixedAssetID(fixedAsset.getObjectID());
            }
        }
        //this.addPurchaseOrderToSolr(order);

        final KpiLog kpiLog = getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPurchaseOrder.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        kpiLog.setEntityId(order.getObjectID());
        ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Update Purchase Order");

        saveResult.setId(initQuoteItemsForUpdate(data, order, status));
        addPurchaseOrderToSolr(order);

        final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT,
                order,
                this.userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_PURCHASE_ORDER);

        return saveResult;
    }

    @Override
    public void updatePurchaseOrderCustomFields(final NewInvoice data) {
        final EdsPurchaseOrder order = (EdsPurchaseOrder) this.quoteManager.get(data.getID());
        order.setCustomFields(this.invoiceServiceLocal.createInvoiceCustomFields(data.getCustomFieldItems()));

        try {
            purchaseOrderSolrComponent.index(order);
        } catch (final IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createRevisionHistory(final EdsQuote quote) {
        final EdsQuote clonedQuote = quote.cloneShallow();
        clonedQuote.setDeleted(true);
        clonedQuote.setHistoricalParent(quote);

        final List<EdsQuoteItem> quoteItems = new LinkedList<>();
        for (final EdsQuoteItem qi : quote.getQuoteItems()) {
            final EdsQuoteItem nqi = qi.cloneShallow();
            nqi.setQuote(clonedQuote);
            nqi.setDeleted(true);
            quoteItems.add(nqi);
        }
        clonedQuote.setQuoteItems(quoteItems);

        final List<EdsQuoteTaxTotal> quoteTaxTotals = new LinkedList<>();
        clonedQuote.setQuoteTaxTotals(quoteTaxTotals);

        final List<EdsInvoice> invoices = new ArrayList<>();
//        for (EdsInvoice invoice : quote.getInvoices()) {
//            EdsInvoice clonedInvoice = invoice.<EdsInvoice>cloneShallow();
//            clonedInvoice.setHistoricalParent(invoice);
//            clonedInvoice.setDeleted(true);
//            invoices.add(clonedInvoice);
//        }
        clonedQuote.setInvoices(invoices);

        if (quote instanceof EdsSaleQuote) {

            final List<EdsComissionAllocateItem> comissionAllocateItems = new ArrayList<>();
            for (final EdsComissionAllocateItem item : ((EdsSaleQuote) quote).getComissionAllocateItems()) {
                comissionAllocateItems.add(item.cloneShallow());
            }

            ((EdsSaleQuote) clonedQuote).setComissionAllocateItems(comissionAllocateItems);

            clonedQuote.setApprovers(new ArrayList<>());
            ((EdsSaleQuote) clonedQuote).setOrderApprovers(new ArrayList<>());
        }
        if (quote instanceof EdsPurchaseOrder) {
            clonedQuote.setApprovers(new ArrayList<>());
        }
//        clonedQuote.setApproverHistory(new HashSet<>());

        this.quoteManager.create(clonedQuote);
    }

    private Integer initQuoteItemsForUpdate(final NewInvoice data, final EdsQuote quote, String oldStatus) {
        List<Integer> quoteItemsDeleted = new ArrayList<>();
        if (!data.isReceiveQtyAction()) {
            try {
                final ArrayList<Integer> qiIds = new ArrayList<>();
                for (int i = 0; i < data.getItems().length; i++)
                    qiIds.add(data.getItems()[i].getID() != null ? data.getItems()[i].getID() : 0);

                if (!qiIds.isEmpty()) {
                    quoteItemsDeleted = this.quoteManager.deleteQuoteItems(data.getID(), qiIds);
                }
            } catch (final Exception e) {
                QuoteServiceImpl.log.info("Can't delete quote items. Quote ID: " + data.getID());
            }
        }

        final EdsFinancialSettings financialSettings = this.financialSettingsManager.getFinancialSettings();
        final boolean isMultipleWarehouseEnabled = financialSettings.getEnableMultiWarehouse();
        final EdsWarehouse defaultWarehouse = this.warehouseManager.getDefaultWarehouse();
        final EdsPickList pickList = this.pickListManager.getPickListBySaleQuoteID(quote.getObjectID());

        int sorder = 0;
        final List<EdsQuoteItem> items = new ArrayList<>();
        for (final NewInvoiceItem newItem : data.getItems()) {
            final EdsQuoteItem item;

            if (data.isReceiveQtyAction() || newItem.getID() != null && newItem.getID() > 0) {
                item = this.quoteManager.getQuoteItemByID(newItem.getID());
                newItem.setConvertedQty(item.getConvertedQty());
                newItem.setConvertedAmount(item.getConvertedAmount());
            } else {
                item = new EdsQuoteItem();
            }

            if (!isMultipleWarehouseEnabled) {
                item.setWarehouse(defaultWarehouse);
            }

            item.setQuote(quote);
            item.setAllocatedExpense(newItem.getAllocatedExpense());
            item.setReceivedAllocation(newItem.getReceivedAllocation());
            item.setAssignedSerials(newItem.getAssignedSerials());
            item.setCustomFields(this.invoiceServiceLocal.createInvoiceItemCustomFields(newItem.getCustomFieldItems()));
            initInvoiceItemData(item, newItem);

            if (quote instanceof EdsSaleQuote && pickList != null && item.getItem() != null) {
                item.setPickable(true);
            }
            if (newItem.getAttachments() != null && !newItem.getAttachments().isEmpty()) {
                final EdsUser user = this.userManager.getUser();
                final List<FileResource> attachments = this.attachmentUtilsManager.getAttachments(Constants.F_SALE_QUOTE_ITEM, newItem.getID(), newItem.getID(), user);
                if (attachments != null && !attachments.isEmpty()) {
                    boolean imageExist = false;
                    for (final FileItem fi : newItem.getAttachments()) {
                        imageExist = false;
                        for (final FileResource attachment : attachments) {
                            if (attachment.getObjectId().equals(fi.getId())) {
                                imageExist = true;
                                break;
                            }
                        }
                        if (!imageExist) {
                            this.saveSQLineItemImage(newItem.getID() != null ? newItem.getID() : item.getObjectID(), fi, user);
                        }
                    }
                } else {
                    for (final FileItem fi : newItem.getAttachments()) {
                        this.saveSQLineItemImage(newItem.getID(), fi, user);
                    }
                }
            }

            item.setSorder(sorder++);
            items.add(item);
        }
        if (quote instanceof EdsPurchaseOrder) {
            ((EdsPurchaseOrder) quote).setExpenseAllocationType(data.getExpenseAllocationType());
        }
        quote.setQuoteItems(items);

        if (data.getSaasuGUID() != null && !"".equals(data.getSaasuGUID())) {
            quote.setSaasuGUID(data.getSaasuGUID());
        }
        if (data.getSaasuLastUpdateDate() != null) {
            quote.setSasuuLastUpdatedTime(data.getSaasuLastUpdateDate());
        }

        quote.setSaasuLastUpdatedUid(data.getSaasuLastUpdatedUid());

        if (data.getAttachments() != null && data.getAttachments().length > 0) {
            if (quote instanceof EdsPurchaseOrder) {
                this.attachmentUtilsManager.saveAttachments(Constants.F_PUR_ORDER, quote.getObjectID(), quote.getObjectID(), data.getAttachments());
            } else if (quote instanceof EdsSaleQuote) {
                this.attachmentUtilsManager.saveAttachments(Constants.F_SALE_QUOTE, quote.getObjectID(), quote.getObjectID(), data.getAttachments());
            }
        }

        EdsShippingData shippingData = new EdsShippingData();
        if (data.getReceiveDate() != null && data.getReceiveDate().getNonConvertedDate() != null &&
                quote instanceof EdsPurchaseOrder && !quote.isFixedAssetRelated()) {
            shippingData = this.createShippingData(quote, data);

            final Integer transactionId = this.accountingServiceLocal.createTransactionsForGoodsReceived((EdsPurchaseOrder) quote, shippingData, null);

            for (final EdsShippingDataItem shippingItem : shippingData.getItems()) {
                final EdsQuoteItem quoteItem = this.quoteItemManager.get(shippingItem.getQuoteItemId());
                if (quoteItem.getItem() != null && quoteItem.getItem().getInventoryTrackingEnabled()) {
                    this.itemSerialService.createForGoodsReceived(shippingItem, transactionId);
                }
                if (quoteItem.getItem() != null && quoteItem.getItem().getTrackBatchesEnabled()) {
                    this.itemBatchService.createForGoodsReceived(shippingData.getObjectID(), shippingItem);
                }
            }
        }
        if ((RECEIVED.equals(data.getStatusCode()) || PARTIAL_RECEIVED.equals(data.getStatusCode()) || INVOICED.equals(data.getStatusCode())) &&
                data.getReceiveDate() == null && quote instanceof EdsPurchaseOrder && !quote.isFixedAssetRelated()) {
            List<EdsShippingData> grns = shippingDataManager.getByQuoteId(quote.getObjectID());
            if (grns != null && !grns.isEmpty()) {
                for (EdsShippingData grn : grns) {
                    grn.getItems().forEach(i -> i.getQuoteItem().setReceive(i.getReceiveType().equals(ReceiveTypeEnum.RECEIVE_BY_QTY) ?
                            i.getReceivedQty() : i.getReceivedAmount()));
                    accountingServiceLocal.createTransactionsForGoodsReceived((EdsPurchaseOrder) quote, grn, null);
                }
            }
        }

        if (quote instanceof EdsSaleQuote) {
            quote.setShippingMethod(data.getShippingMethodID() != null ? this.shippingMethodManager.get(data.getShippingMethodID()) : null);
            quote.setShippingAmount(data.getShippingPrice());

            quote.setUpdatedDate(new Date());
        }
        this.createOrUpdateNoteAndHistory(data, quote);

        if (quote instanceof EdsPurchaseOrder) {
            this.updatePurchaseOrderItemProductSerials((EdsPurchaseOrder) quote, quoteItemsDeleted, shippingData.getObjectID());
        }

        return quote.getObjectID();
    }

    private void saveSQLineItemImage(final Integer itemID, final FileItem fileItem, final EdsUser user) {
        final EdsFileHeader file = this.fileHeaderManager.get(fileItem.getId());
        if (file != null) {
            file.setEntityId(itemID);
            file.setOwner(user);
            file.setFolder(this.attachmentUtilsManager.getFolder(Constants.F_SALE_QUOTE_ITEM, itemID, user.getCompany()));
            file.setFileType(Constants.F_SALE_QUOTE_ITEM);
            this.fileHeaderManager.update(file);
            this.folderRbacManager.removeFileEntries(file.getObjectID());
            this.folderRbacManager.indexFile(file);
        }
    }

    private EdsShippingData createShippingData(final EdsQuote quote, final NewInvoice data) {
        if (quote == null) {
            return null;
        }
        final Integer companyID = Integer.valueOf(getInstance().getCompanyId());
        boolean isPurchaseOrder = quote instanceof EdsPurchaseOrder;
        EdsShippingData shippingData = new EdsShippingData();

        shippingData.setQuote(quote);
        shippingData.setCurrency(quote.getCurrency());
        shippingData.setCrmAccount(quote.getClientOrSupplier());
        shippingData.setStatus(ShippingDataStatus.PENDING);
        if (isPurchaseOrder) {
            shippingData.setShippingType(ShippingDataType.IN);
        } else {
            shippingData.setShippingType(ShippingDataType.OUT);
        }
        if (data.getShippingFourDigitNumber() == null) {
            data.setShippingFourDigitNumber(this.invoiceCircularResolver.parseGrnNumberData().getFourDigitNumber());
        }
        shippingData.setIntNumber(Integer.valueOf(data.getShippingFourDigitNumber()));
        shippingData.setNumber(data.getShippingNumber());

        if (isPurchaseOrder) {
            shippingData.setShippingLabel(data.getReference());
        } else {
            shippingData.setShippingLabel(data.getShippingLabel());
        }


        if (data.getReceiveDate() != null) {
            shippingData.setShippingDate(data.getReceiveDate().getNonConvertedDate());
        }
        shippingData.setCreator(this.userManager.getUser());
        shippingData.setCreationTime(new Date());
        this.shippingDataManager.create(shippingData);
        List<EdsShippingDataItem> shippingDataItems = Lists.newArrayListWithCapacity(quote.getQuoteItems().size());

        for (final NewInvoiceItem item : data.getItems()) {
            final EdsQuoteItem quoteItem = this.quoteItemManager.get(item.getID());
            EdsShippingDataItem edsShippingDataItem = new EdsShippingDataItem();
            BigDecimal valueToReceive = BigDecimal.ZERO;

            edsShippingDataItem.setQuoteItemId(quoteItem.getObjectID());
            edsShippingDataItem.setQuoteItem(quoteItem);

            if (isPurchaseOrder) {
                valueToReceive = quoteItem.getReceive();
                if (ReceiveTypeEnum.RECEIVE_BY_QTY.equals(quoteItem.getReceiveType())) {
                    edsShippingDataItem.setReceivedQty(quoteItem.getReceive());
                } else {
                    edsShippingDataItem.setReceivedAmount(quoteItem.getReceive());
                }
            } else {
                edsShippingDataItem.setReceivedQty(quoteItem.getShip());
                valueToReceive = quoteItem.getShip();
            }
            if (Optional.ofNullable(valueToReceive).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            edsShippingDataItem.setReceiveType(quoteItem.getReceiveType());
            if (quoteItem.getWarehouse() != null) {
                edsShippingDataItem.setWarehouseId(quoteItem.getWarehouse().getObjectID());
                edsShippingDataItem.setWarehouse(quoteItem.getWarehouse());
            }
            edsShippingDataItem.setReceivedAllocation(quoteItem.getAllocatedExpense());
            edsShippingDataItem.setShippingDataId(shippingData.getObjectID());
            if (quoteItem.getItem() != null && data.getProductSerialItems() != null && data.getProductSerialItems().size() > 0) {
                this.updateGDNProductSerials(quoteItem, shippingData.getObjectID(), data.getProductSerialItems().get(quoteItem.getItem().getObjectID()));
            }
            this.shippingDataItemManager.create(edsShippingDataItem);

            if (quoteItem.getItem() != null) {
                if (quoteItem.getItem().getInventoryTrackingEnabled()) {
                    edsShippingDataItem.setSerials(item.getSerials());
                }
                if (quoteItem.getItem().getTrackBatchesEnabled()) {
                    edsShippingDataItem.setBatchItems(item.getBatchItems());
                }
            }
            shippingDataItems.add(edsShippingDataItem);
        }
        shippingData.setItems(shippingDataItems);
        this.shippingDataManager.create(shippingData);
        if (!isPurchaseOrder) {
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                    BaseEventsPostProcessorImpl.EVENT_TYPE_ADD,
                    shippingData,
                    this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_GDN);
        }

        try {
            shippingDataSolrComponent.index(shippingData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return shippingData;
    }

    private void updatePurchaseOrderItemProductSerials(final EdsPurchaseOrder order, final List<Integer> quoteItemsDeleted, final Integer shippingDataId) {
        final List<Integer> oldSerialItems = this.productSerialManager.getProductSerialsByPurchaseOrderItems(quoteItemsDeleted);
        final List<EdsQuoteItem> quoteItems = order.getQuoteItems();
        final Map<Integer, Integer> existingSerials = new HashMap<>();

        for (final EdsQuoteItem item : quoteItems) {
            if (item.getAssignedSerials() != null && item.getAssignedSerials().length > 0) {
                for (int j = 0; j < item.getAssignedSerials().length; j++) {
                    final EdsProductSerial serial;
                    if (item.getAssignedSerials()[j].getObjectID() != null) {
                        serial = this.productSerialManager.get(item.getAssignedSerials()[j].getObjectID());
                        serial.setOrderItemID(item.getObjectID());
                        serial.setItemID(item.getItem() != null ? item.getItem().getObjectID() : null);
                        serial.setGrnid(shippingDataId);
                        this.productSerialManager.update(serial);
                        existingSerials.put(serial.getObjectID(), serial.getObjectID());
                    } else {
                        serial = new EdsProductSerial();
                        serial.setOrderItemID(item.getObjectID());
                        serial.setItemID(item.getItem() != null ? item.getItem().getObjectID() : null);
                        serial.setSerial(item.getAssignedSerials()[j].getSerial());
                        serial.setLotNumber(item.getAssignedSerials()[j].getLotNumber());
                        serial.setRefNumber(item.getAssignedSerials()[j].getRefNumber());
                        serial.setExpirationDate(item.getAssignedSerials()[j].getExpirationDate());
                        serial.setGrnid(shippingDataId);
                        this.productSerialManager.create(serial);
                    }
                }
            }
        }
        for (final Integer id : oldSerialItems) {
            if (!existingSerials.containsKey(id)) {
                this.productSerialManager.delete(this.productSerialManager.get(id));
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceNumberData getQuoteNumber() {
        return this.invoiceCircularResolver.getQuoteOrderNumberData(Constants.SALE_QUOTE);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceNumberData getSalesOrderNumber() {
        return this.invoiceCircularResolver.getQuoteOrderNumberData(Constants.SALE_ORDER);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceNumberData getOrderNumber() {
        return this.invoiceCircularResolver.getQuoteOrderNumberData(Constants.PURCHASE_ORDER);
    }

    @Override
    public void approveQuote(final Integer id) {
        final EdsQuote quote = this.quoteManager.get(id);

        if (this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.REVISION_HISTORY_ENABLED)) {
            this.createRevisionHistory(quote);
        }

        final EdsReference reference = getInvoiceStatus(Constants.APPROVE);
        if (quote instanceof EdsSaleQuote) {
            if (((EdsSaleQuote) quote).isSalesOrder()) {
                quote.setStatus(reference);
            }
            quote.updateStatus(reference);

            this.messageManager.sendSalesQuotePingPongNotificationIfEnabled((EdsSaleQuote) quote, null);

            this.addSaleQuoteToSolr((EdsSaleQuote) quote);

            this.baseEventPostProcessor.registerEvent(SalesQuoteEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, (EdsSaleQuote) quote, this.userManager.getUser());

            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), quote, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_SALEQUOTE);
        }
        if (quote instanceof EdsPurchaseOrder) {
            quote.setStatus(reference);
            this.baseEventPostProcessor.registerEvent(PurchaseOrderEventListenerImpl.TYPE, PurchaseOrderEventListenerImpl.EVENT_PURCHASE_ORDER_CLIENT_APPROVE, (EdsPurchaseOrder) quote, this.userManager.getUser());
            this.addPurchaseOrderToSolr((EdsPurchaseOrder) quote);
//            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PO_DOUBLE_APPROVER_ENABLED)) {
//                messageManager.sendPurchaseOrderApprovedOrDeclinedMessage((EdsPurchaseOrder) quote);
//            }
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), quote, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_PURCHASE_ORDER);
        }
    }

    @Override
    public void closedOrder(final Integer id) {
        final EdsQuote quote = this.quoteManager.get(id);

        final EdsReference reference = getInvoiceStatus(Constants.INVOICE_STATUS_CLOSED);
        if (quote instanceof EdsSaleQuote) {
            quote.setStatus(reference);
            quote.updateStatus(reference);

            final List<EdsQuoteItem> quoteItems = quote.getQuoteItems();

            for (final EdsQuoteItem quoteItem : quoteItems) {
                quoteItem.setBookReservation(null);
            }

            this.addSaleQuoteToSolr((EdsSaleQuote) quote);
            if (((EdsSaleQuote) quote).isSalesOrder()) {
                this.baseEventPostProcessor.registerEvent(SalesOrderEventListenerImpl.TYPE, SalesOrderEventListenerImpl.EVENT_STATUS_CLOSED_SALE_ORDER, (EdsSaleQuote) quote, this.userManager.getUser());
            } else {
                this.baseEventPostProcessor.registerEvent(SalesQuoteEventListenerImpl.TYPE, SalesQuoteEventListenerImpl.EVENT_STATUS_CLOSED_SALE_QUOTE, (EdsSaleQuote) quote, this.userManager.getUser());
            }
        }
        if (quote instanceof EdsPurchaseOrder) {
            quote.setStatus(reference);
            this.baseEventPostProcessor.registerEvent(PurchaseOrderEventListenerImpl.TYPE, PurchaseOrderEventListenerImpl.EVENT_STATUS_CLOSED_PURCHASE_ORDER, (EdsPurchaseOrder) quote, this.userManager.getUser());
            this.addPurchaseOrderToSolr((EdsPurchaseOrder) quote);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getSalesQuoteDue() {
        return this.invoicingSettingsManager.getInvoiceSettings(this.quoteManager.getUser().getCompany()).getSalesQuoteDue();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getPurchaseOrderDue() {
        return this.invoicingSettingsManager.getInvoiceSettings(this.quoteManager.getUser().getCompany()).getPurchaseOrderDue();
    }

    @Override
    @Transactional
    public void changeQuoteStatus(final Integer id, final String status, SelectItem rejectionReason, final boolean hasApproveForAll) {
        final EdsQuote quote = this.quoteManager.get(id);
        final EdsReference statusReference = getInvoiceStatus(status);

        final EdsUser eventCauser = this.userManager.getUser();
        String reason = "";
        if (quote instanceof EdsSaleQuote saleQuote) {
            if (!EdsSaleQuote.APPROVE.equals(statusReference.getCode())) {
                quote.setEntityStatus(statusReference);
            } else if (EdsSaleQuote.APPROVE.equals(statusReference.getCode()) && hasApproveForAll) {
                quote.setEntityStatus(statusReference);
            }
            if (rejectionReason != null) {
                if (rejectionReason.getId() != null && StringUtils.isNotBlank(rejectionReason.getName())) {
                    EdsReference reference = referenceManager.get(rejectionReason.getId());
                    saleQuote.setRejectReason(reference);
                    saleQuote.setRejectText(rejectionReason.getName());
                    reason = reference.isSystemReference() && !reference.isChanged() ? wfmMessageSource.localize(reference.getCode()) : reference.getName();
                    reason += " (" + rejectionReason.getName() + ")";
                } else if (rejectionReason.getId() != null) {
                    EdsReference reference = referenceManager.get(rejectionReason.getId());
                    saleQuote.setRejectReason(reference);
                    reason = reference.isSystemReference() && !reference.isChanged() ? wfmMessageSource.localize(reference.getCode()) : reference.getName();
                } else if (StringUtils.isNotBlank(rejectionReason.getName())) {
                    saleQuote.setRejectText(rejectionReason.getName());
                    reason = rejectionReason.getName();
                }
            }
            this.createQuoteHistory(reason, saleQuote);

            this.messageManager.sendSalesQuotePingPongNotificationIfEnabled(saleQuote, reason);

            final String eventType;

            String entityType = RelationItem.TYPE_SALEQUOTE;
            if (saleQuote.isSalesOrder()) {
                entityType = RelationItem.TYPE_SALEORDER;
            } else {
                if (this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.REVISION_HISTORY_ENABLED)) {
                    this.createRevisionHistory(quote);
                }
            }
            if (Constants.MANAGER_REJECT.equals(status)) {
                quote.updateStatus(statusReference);

                if (saleQuote.isSalesOrder()) {
                    final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), quote, eventCauser);
                    workflowEvent.setEntityType(entityType);

                    this.baseEventPostProcessor.registerEvent(SalesOrderEventListenerImpl.TYPE, SalesOrderEventListenerImpl.EVENT_SALE_ORDER_MANAGER_REJECT, saleQuote, eventCauser);
                } else {
                    eventType = SalesQuoteEventListenerImpl.EVENT_SALES_QUOTE_MANAGER_REJECT;
                    this.baseEventPostProcessor.registerEvent(SalesQuoteEventListenerImpl.TYPE, eventType, saleQuote, eventCauser);

                    EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), quote, eventCauser);
                    workflowEvent.setEntityType(entityType);
                }
            } else if (Constants.REJECT.equals(status)) {
                eventType = SalesQuoteEventListenerImpl.EVENT_SALES_QUOTE_REJECT;
                this.baseEventPostProcessor.registerEvent(SalesQuoteEventListenerImpl.TYPE, eventType, saleQuote, eventCauser);
            } else if (Constants.APPROVE.equals(status)) {
                saleQuote.updateStatus(statusReference);

                switch (entityType) {
                    case RelationItem.TYPE_SALEQUOTE -> {
                        eventType = SalesQuoteEventListenerImpl.EVENT_SALES_QUOTE_MANAGER_APPROVE;
                        this.baseEventPostProcessor.registerEvent(SalesQuoteEventListenerImpl.TYPE, eventType, saleQuote, eventCauser);
                    }
                    case RelationItem.TYPE_SALEORDER -> {
                        eventType = SalesOrderEventListenerImpl.EVENT_SALE_ORDER_MANAGER_APPROVE;
                        this.baseEventPostProcessor.registerEvent(SalesOrderEventListenerImpl.TYPE, eventType, saleQuote, eventCauser);
                    }
                }


                EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), quote, eventCauser);
                workflowEvent.setEntityType(entityType);
            } else if (Constants.CLIENT_APPROVE.equals(status)) {
                eventType = SalesQuoteEventListenerImpl.EVENT_SALES_QUOTE_CLIENT_APPROVE;
                this.baseEventPostProcessor.registerEvent(SalesQuoteEventListenerImpl.TYPE, eventType, saleQuote, eventCauser);

                if (this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST) && !saleQuote.isSalesOrder() && !saleQuote.isProgressInvoicing()) {

                    boolean accessCreatPickList = false;
                    EdsPickList pickList = this.pickListManager.getPickListBySaleQuoteID(saleQuote.getObjectID());
                    saleQuote.setSalesOrder(false);

                    if (pickList == null) {
                        pickList = new EdsPickList();

                        if (saleQuote.getQuoteItems() != null && saleQuote.getQuoteItems().size() > 0) {
                            accessCreatPickList = false;
                            for (final EdsQuoteItem qitem : saleQuote.getQuoteItems()) {

                                if (qitem.getItem() != null && qitem.getItem().getObjectID() != null) {
                                    qitem.setPickable(true);
                                    accessCreatPickList = true;
                                }
                            }
                        }

                        if (accessCreatPickList) {
                            pickList.setSaleQuote(saleQuote);
                            this.pickListManager.create(pickList);
                            this.quoteManager.update(saleQuote);
                            this.createQuoteHistory(saleQuote);
                        }
                    }
                }
            }


            this.addSaleQuoteToSolr(saleQuote);

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, saleQuote, userManager.getUser());
            workflowEvent.setEntityType(entityType);
        } else if (quote instanceof EdsPurchaseOrder) {
            reason = rejectionReason != null ? rejectionReason.getName() : null;
            if (!Constants.APPROVE.equals(statusReference.getCode())) {
                quote.setEntityStatus(statusReference);
            }

            final EdsPurchaseOrder purchaseOrder = (EdsPurchaseOrder) quote;
            final String eventType;

            final String entityType = RelationItem.TYPE_PURCHASE_ORDER;

            if (Constants.APPROVE.equals(status)) {
                quote.updateStatus(statusReference);

                eventType = PurchaseOrderEventListenerImpl.EVENT_PURCHASE_ORDER_CLIENT_APPROVE;
                this.baseEventPostProcessor.registerEvent(PurchaseOrderEventListenerImpl.TYPE, eventType, purchaseOrder, eventCauser);
//                messageManager.sendPurchaseOrderApprovedOrDeclinedMessage((EdsPurchaseOrder) quote);
                final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), purchaseOrder, eventCauser);
                workflowEvent.setEntityType(entityType);
            } else if (Constants.REJECT.equals(status)) {
                quote.updateStatus(statusReference);

                final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), purchaseOrder, eventCauser);
                workflowEvent.setEntityType(entityType);
            }

            if (hasApproveForAll) {
                statusUpdater(quote, statusReference);
            }

            this.addPurchaseOrderToSolr((EdsPurchaseOrder) quote);

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, purchaseOrder, userManager.getUser());
            workflowEvent.setEntityType(entityType);
        }

        if (quote.getStatus() != null && StringUtils.isNotBlank(reason) && (Constants.REJECT.equals(quote.getStatus().getCode()) || Constants.MANAGER_REJECT.equals(quote.getStatus().getCode()))) {
            this.createQuoteNote(this.commonLocalizer.localize(PdfLocalizationName.rejectionReason, "Rejection Reason ") + ": " + reason, quote);
        }
    }

    // It is for "approve for all and only one approver case"
    private void statusUpdater(EdsQuote quote, EdsReference status) {
        if (quote.getApprovers().size() == 1) {
            quote.setEntityStatus(status);
        }
    }

    private void createOrUpdateNoteAndHistory(final NewInvoice data, final EdsQuote quote) {
        final HistoryListItem[] noteItems = data.getHistoryList();
        final List<EdsInvoiceQuoteNote> quoteNotes = this.invoiceQuoteNoteManager.getQuoteNotes(quote.getObjectID());

        if (noteItems != null && noteItems.length > 0) {
            Map<Integer, Integer> existingNotesMap = Maps.newHashMap();

            for (final HistoryListItem noteItem : noteItems) {
                if (noteItem.getObjectID() == null && !StringUtils.isEmpty(noteItem.getComment())) {
                    createQuoteNote(noteItem.getComment(), quote);
                }
                if (noteItem.getObjectID() != null) {
                    existingNotesMap.put(noteItem.getObjectID(), noteItem.getObjectID());
                }
            }
            for (final EdsInvoiceQuoteNote quoteNote : quoteNotes) {
                if (!existingNotesMap.containsKey(quoteNote.getObjectID())) {
                    this.invoiceQuoteNoteManager.delete(quoteNote);
                }
            }
        } else {
            for (final EdsInvoiceQuoteNote noteForDelete : quoteNotes) {
                this.invoiceQuoteNoteManager.delete(noteForDelete);
            }
        }
        if (quote instanceof EdsSaleQuote) {
            this.createQuoteHistory((EdsSaleQuote) quote);
        }
    }

    private void createQuoteNote(final String comment, final EdsQuote quote) {
        final EdsInvoiceQuoteNote note = new EdsInvoiceQuoteNote();
        note.setComment(comment);
        note.setCommentator(this.quoteManager.getUser());
        note.setQuote(quote);
        note.setDate(new Date());
        this.invoiceQuoteNoteManager.create(note);
    }

    private void saveConvertedRelations(EdsSaleQuote order, EdsSaleQuote quote) {

    }

    private void createQuoteHistory(final EdsSaleQuote quote) {
        this.createQuoteHistory(null, quote);
    }

    private void createQuoteHistory(final String comment, final EdsSaleQuote quote) {
        final EdsUser user = this.quoteManager.getUser();
        String eventDescription = null;
        final String status = quote.getStatus().getCode();
        final String userFullName;
        quote.setUpdatedDate(new Date());
        if (user instanceof EdsClientContact) {
            if ((user.getFirstName() != null && !"".equals(user.getFirstName())) || (user.getLastName() != null && !"".equals(user.getLastName()))) {
                userFullName = user.getFirstName() + " " + user.getLastName();
            } else {
                userFullName = user.getFullName();
            }
        } else {
            userFullName = user != null ? user.getFullName() : "";
        }
        final boolean sent = quote.getSent() != null && quote.getSent();
        if (Constants.DRAFT.equals(status)) {
            eventDescription = userFullName + " saved sale quote as draft.";
        } else if (Constants.APPROVE.equals(status) || Constants.CLIENT_APPROVE.equals(status) || (Constants.OPEN.equals(status) && !sent)) {
            eventDescription = userFullName + " approved sale quote.";
        } else if (Constants.OPEN.equals(status) && sent) {
            eventDescription = userFullName + " approved sale quote and sent message to client.";
        } else if (Constants.REJECT.equals(status)) {
            eventDescription = userFullName + " rejected sale quote.";
        } else if (Constants.CONVERTED.equals(status)) {
            eventDescription = userFullName + " converted sale quote to invoice.";
        }

        final EdsQuoteHistory record = new EdsQuoteHistory();
        record.setQuote(quote);
        record.setCommentator(user);
        record.setComment(comment);
        record.setEvent(quote.getStatus());

        record.setEventDescription(eventDescription);
        record.setEventDate(new Date());
        this.quoteHistoryManager.create(record);
    }

    @Override
    public TestRPC deleteQuote(final Integer objectID, final String type) {
        TestRPC result = new TestRPC();
        Integer companyID = Integer.valueOf(getInstance().getCompanyId());

        if (Constants.SALE_QUOTE.equals(type)) {
            EdsSaleQuote quote = this.quoteManager.getSaleQuote(objectID);
            final String status = quote.getStatus().getCode();

            if (Constants.INVOICED.equals(status) || Constants.PARTIAL_INVOICED.equals(status) || Constants.CONVERTED.equals(status)) {
                result.setMessageCommand(MessageCommand.hasConvertedItems);
                return result;
            }
            Long count = this.shippingDataManager.countShippingDataByQuoteIdAndType(quote.getObjectID(), ShippingDataType.OUT);

            if (count > 0L) {
                result.setMessageCommand(MessageCommand.hasShippingData);
                return result;
            }

            this.invoiceQuoteNoteManager.deleteInvoiceQuoteNotes(objectID, false);
            deleteSalesQuote(objectID, false);

            //this piece of the code need to calculate project budget
            if (quote.getRelatedProject() != null ||
                    this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {

                final EdsBusinessEvent event = this.baseEventPostProcessor.registerEvent(ProjectBudgetCustomEventListenerImpl.TYPE,
                        ProjectBudgetCustomEventListenerImpl.SALE_QUOTE_AVOID,
                        quote,
                        null);
                event.setCustomStringField(quote.getRelatedProject() != null ? quote.getRelatedProject().getObjectID().toString() : null);
            }

        } else if (Constants.PURCHASE_ORDER.equals(type)) {
            // TODO: 2/28/18 check for converted shipping data
            if (this.quoteManager.hasConvertedItems(objectID)) {
                result.setMessageCommand(MessageCommand.hasConvertedItems);
                return result;
            }
            EdsPurchaseOrder order = (EdsPurchaseOrder) this.quoteManager.get(objectID);
            List<Integer> transactionIds = this.transactionManager.getTransactionIdsByPurchaseOrderId(order.getObjectID());

            if (!transactionIds.isEmpty()) {
                boolean hasOutTransactionOfItemWithChosenIn = this.itemStockManager.hasOutTransactionsOfItemWithChosenIn(transactionIds);

                if (hasOutTransactionOfItemWithChosenIn) {
                    result.setMessageCommand(MessageCommand.hasOutTransactions);
                    return result;
                }
            }
            Long count = this.shippingDataManager.countShippingDataByQuoteIdAndType(order.getObjectID(), ShippingDataType.IN);

            if (count > 0L) {
                result.setMessageCommand(MessageCommand.hasShippingData);
                return result;
            }
            invoiceQuoteNoteManager.deleteInvoiceQuoteNotes(objectID, false);
            deletePurchaseOrder(objectID);
            solrManager.removePurchaseOrder(objectID, companyID);

            final EdsPurchaseOrder purchaseOrder = this.quoteManager.getPurchaseOrderByID(objectID);
            if (purchaseOrder.getRelatedProject() != null || this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                final EdsBusinessEvent event = this.baseEventPostProcessor.registerEvent(ProjectBudgetCustomEventListenerImpl.TYPE, ProjectBudgetCustomEventListenerImpl.PURCHASE_ORDER_AVOID, purchaseOrder, null);
                event.setCustomStringField(purchaseOrder.getRelatedProject() != null ? purchaseOrder.getRelatedProject().getObjectID().toString() : null);
            }
            this.updatePurchaseOrderRelatedFixedAsset(objectID);
        }
        return result;
    }

    @Override
    public void deleteSelectedQuotes(final ArrayList<Integer> idArray) {
        for (final Integer objectID : idArray) {
            this.deleteSalesQuote(objectID, true);
        }
    }

    @Override
    public void deleteSelectedPurchaseOrders(final ArrayList<Integer> idArray) {
        final Integer companyID = Integer.valueOf(getInstance().getCompanyId());
        for (final Integer objectID : idArray) {
            if (this.quoteManager.hasConvertedItems(objectID)) {
                continue;
            }
            EdsPurchaseOrder order = (EdsPurchaseOrder) this.quoteManager.get(objectID);
            List<Integer> transactionIds = this.transactionManager.getTransactionIdsByPurchaseOrderId(order.getObjectID());

            if (!transactionIds.isEmpty()) {
                boolean hasOutTransactionOfItemWithChosenIn = this.itemStockManager.hasOutTransactionsOfItemWithChosenIn(transactionIds);

                if (hasOutTransactionOfItemWithChosenIn) {
                    continue;
                }
            }
            Long count = this.shippingDataManager.countShippingDataByQuoteIdAndType(order.getObjectID(), ShippingDataType.IN);

            if (count > 0L) {
                continue;
            }
            invoiceQuoteNoteManager.deleteInvoiceQuoteNotes(objectID, false);
            deletePurchaseOrder(objectID);
            solrManager.removePurchaseOrder(objectID, companyID);

            final EdsPurchaseOrder purchaseOrder = this.quoteManager.getPurchaseOrderByID(objectID);
            if (purchaseOrder.getRelatedProject() != null || this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                final EdsBusinessEvent event = this.baseEventPostProcessor.registerEvent(ProjectBudgetCustomEventListenerImpl.TYPE, ProjectBudgetCustomEventListenerImpl.PURCHASE_ORDER_AVOID, purchaseOrder, null);
                event.setCustomStringField(purchaseOrder.getRelatedProject() != null ? purchaseOrder.getRelatedProject().getObjectID().toString() : null);
            }
            this.updatePurchaseOrderRelatedFixedAsset(objectID);
        }
    }


    private void updatePurchaseOrderRelatedFixedAsset(final Integer objectID) {
        final EdsFixedAsset item = this.fixedAssetManager.getFixedAssetByPurchaseOrder(objectID);
        if (item != null) {
            item.setPurchaseOrder(null);
            this.fixedAssetManager.createOrUpdate(item);
        }
    }

    private void deleteSalesQuote(final Integer objectID, final boolean sendCancelledMessage) {
        final EdsSaleQuote quote = (EdsSaleQuote) this.quoteManager.get(objectID);
        if (!(Constants.INVOICED.equals(quote.getStatus().getCode()) || Constants.PARTIAL_INVOICED.equals(quote.getStatus().getCode()) || Constants.CONVERTED.equals(quote.getStatus().getCode()) || Constants.SHIPPED.equals(quote.getStatus().getCode()) || Constants.PARTIAL_SHIPPED.equals(quote.getStatus().getCode()))) {
            final List<EdsQuoteHistory> records = this.quoteHistoryManager.getQuoteRecords(objectID);
            final Integer companyID = Integer.valueOf(getInstance().getCompanyId());
            final NewInvoice data = EdsQuote.getQuoteData(quote);
            for (final EdsQuoteHistory r : records) {
                this.quoteHistoryManager.delete(r);
            }
            final boolean isSalesOrder = data.getStatusCode().equals(Constants.SALE_ORDER);
            final List<EdsInvoiceQuoteNote> notes = this.invoiceQuoteNoteManager.getQuoteNotes(objectID);
            for (final EdsInvoiceQuoteNote n : notes) {
                this.invoiceQuoteNoteManager.delete(n);
            }
            EdsBusinessEvent event = null;
            if (quote != null && data != null) {
                if (isSalesOrder) {
                    event = this.baseEventPostProcessor.registerEvent(SalesOrderEventListenerImpl.TYPE,
                            BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE,
                            quote, this.userManager.getUser());
                } else {
                    event = this.baseEventPostProcessor.registerEvent(SalesQuoteEventListenerImpl.TYPE,
                            BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE,
                            quote, this.userManager.getUser());
                }
                event.setCustomStringField(quote.getNumber());
            }
            List<EdsGoodsDeliveredTransaction> transactions = this.transactionManager.getTransactionBySaleOrderId(quote.getObjectID());

            for (final EdsGoodsDeliveredTransaction transaction : transactions) {
                this.transactionManager.setChangedAccountsForRecalculate(transaction.getObjectID());
                transaction.setDeleted(true);
                this.transactionManager.update(transaction);
                this.itemStockManager.deleteItemStocksByTransaction(transaction.getObjectID());
                this.baseEventPostProcessor.registerEvent(TransactionCustomEventListenerImpl.TYPE, TransactionCustomEventListenerImpl.EVENT_REMOVE_GOODS_DELIVIRY_NOTE_TRANSACTION, transaction.getShippingData(), this.userManager.getUser());
            }
            final EdsCrmContact clientContact = quote.getClientContact();
            this.pickListManager.deletePickListAndItemsByQuote(quote.getObjectID());
            this.quoteManager.removeRelationFromInvoice(quote.getObjectID());
            this.quoteManager.removeQuoteItems(objectID);
            quote.setDeleted(true);
            this.quoteManager.update(quote);

            if (sendCancelledMessage && Constants.OPEN.equals(quote.getStatus().getCode()) && clientContact != null) {
                this.messageManager.sendSalesQuoteCancelledMessage(quote.getNumber(), clientContact);
            }
            if (quote != null && quote.getFromNumber() != null && quote.isSalesOrder()) {
                final List<EdsSaleQuote> convertedQuote = this.quoteManager.getSalesQuoteByNumberGlobal(quote.getFromNumber(), false);
                if (convertedQuote != null && !convertedQuote.isEmpty()) {
                    convertedQuote.get(0).setStatus(getInvoiceStatus(Constants.CLIENT_APPROVE));
                    convertedQuote.get(0).setQuoteNumberCN(null);
                    try {
                        saleQuoteSolrComponent.indexes(Collections.singletonList(convertedQuote.get(0)), null);
                    } catch (final IOException e) {
                        e.printStackTrace();
                    } catch (final SolrServerException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            this.solrManager.removeSaleQuote(objectID, companyID);

            final List<EdsPurchaseOrder> relatedPurchaseOrderList = this.quoteManager.getPurchaseOrderByQuoteId(objectID);
            if (relatedPurchaseOrderList != null && !relatedPurchaseOrderList.isEmpty()) {
                for (final EdsPurchaseOrder aRelatedPurchaseOrderList : relatedPurchaseOrderList) {
                    aRelatedPurchaseOrderList.setQuoteId(null);
                    aRelatedPurchaseOrderList.setQuoteNumber("");
                }
                try {
                    purchaseOrderSolrComponent.indexes(relatedPurchaseOrderList);
                } catch (final IOException | SolrServerException | InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            final KpiLog kpiLog = getInstance().getKpiLog();
            kpiLog.setEntityName(EdsSaleQuote.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.DELETE);
            kpiLog.setEntityId(objectID);

            if (quote.getStatus() != null &&
                    (Constants.SALE_ORDER.equals(quote.getStatus().getCode())
                            || Constants.PICKED.equals(quote.getStatus().getCode())
                            || Constants.PACKED.equals(quote.getStatus().getCode())
                            || Constants.SHIPPED.equals(quote.getStatus().getCode())
                            || Constants.PARTIAL_SHIPPED.equals(quote.getStatus().getCode()))) {
                kpiLog.setEntityType(KpiEntityType.SALE_ORDER);
                ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Delete Sale order");
            } else {
                kpiLog.setEntityType(KpiEntityType.SALE_QUOTE);
                ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Delete Sale Quote");
            }

            this.trashBinManager.saveTrashBin(objectID, Constants.SALE_QUOTE, quote.getNumber());

            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, quote, this.userManager.getUser());
            workflowEvent.setEntityType(isSalesOrder ? RelationItem.TYPE_SALEORDER : RelationItem.TYPE_SALEQUOTE);
        }
    }

    private void deletePurchaseOrder(final Integer objectID) {
        final EdsPurchaseOrder order = (EdsPurchaseOrder) this.quoteManager.get(objectID);
        if (order.getStatus().getCode().equals(Constants.PARTIAL_RECEIVED) ||
                order.getStatus().getCode().equals(Constants.RECEIVED) ||
                order.getStatus().getCode().equals(Constants.CONVERTED) ||
                order.getStatus().getCode().equals(Constants.INVOICED) ||
                order.getStatus().getCode().equals(Constants.INVOICE_STATUS_CLOSED)) {

            List<EdsGoodsReceivedTransaction> transactions = this.transactionManager.getTransactionsyPurchaseOrderId(order.getObjectID());

            for (final EdsGoodsReceivedTransaction transaction : transactions) {
                final EdsFinancialSettings fs = this.financialSettingsManager.getFinancialSettings();
                this.financialSettingsManager.update(fs);
                this.transactionManager.setChangedAccountsForRecalculate(transaction.getObjectID());
                transaction.setDeleted(true);
                this.transactionManager.update(transaction);
                this.itemStockManager.deleteItemStocksByTransaction(transaction.getObjectID());
                this.baseEventPostProcessor.registerEvent(TransactionCustomEventListenerImpl.TYPE, TransactionCustomEventListenerImpl.EVENT_REMOVE_GOODS_RECEIVED_NOTE_TRANSACTION, transaction.getShippingData(), this.userManager.getUser());
            }
        }
        this.productSerialManager.removePurchaseOrderProductSerials(objectID);

        this.expenseReportManager.removeRelatedPO(objectID);
        this.quoteManager.removeQuoteItems(objectID);
        order.setDeleted(true);
        this.baseEventPostProcessor.registerEvent(PurchaseOrderEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, order, this.userManager.getUser());
        this.quoteManager.update(order);

        final KpiLog kpiLog = getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPurchaseOrder.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(objectID);
        ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Delete Purchase Order");

        final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, order, this.userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_PURCHASE_ORDER);

        this.trashBinManager.saveTrashBin(objectID, Constants.PURCHASE_ORDER, order.getNumber());
    }

    @Override
    @Transactional
    public void indexCompanySaleQuoteToSolr(final SolrReindexRpc solrReindex) {
        getInstance().setCompanyId(solrReindex.getCompanyId());
//        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));
        removeSalesQuoteAndOrderDeletedCF();
        this.solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.QUOTE);
        try {
            if (solrReindex.isAllReindex()) {
                this.solrManager.removeCompanySaleQuote(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deleteTaskIds = quoteManager.getCompanyDeletedQuotesForSolr(solrReindex);
                this.solrManager.removeCompanySaleQuotesbyIds(deleteTaskIds.toArray(new Integer[]{}));
            }
        } catch (final IOException | SolrServerException e) {
            log.error("Error Sale Quote Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int start = 0;
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        List<EdsSaleQuote> quoteList = quoteManager.getSaleQuoteListForSolr(solrReindex, start, limit);
        while (!quoteList.isEmpty()) {
            var quoteIds = quoteList.stream().map(EdsSaleQuote::getObjectID).map(String::valueOf).collect(Collectors.joining(", "));
            var pickList = pickListManager.getPickListBySaleQuoteIDs(quoteIds);
            try {
                saleQuoteSolrComponent.indexConcurrently(quoteList, pickList);
                quoteManager.flushAndClear();
                start++;
                quoteList = quoteManager.getSaleQuoteListForSolr(solrReindex, (start * limit), limit);
            } catch (IOException | SolrServerException | InterruptedException e) {
                log.error("Error Sale Quote Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
        }
        this.quoteManager.flushAndClear();
    }

    private void removeSalesQuoteAndOrderDeletedCF() {
        profileService.clearFromDbDeletedCustomFieldsByFormId(LayoutRPC.SALEQUOTE_FORM, null, false);
        profileService.clearFromDbDeletedCustomFieldsByFormId(LayoutRPC.SALEORDER_FORM, null, false);
    }

    @Override
    @Transactional
    public void purchaseOrderToSolrIndex(final SolrReindexRpc solrReindex) {
        getInstance().setCompanyId(solrReindex.getCompanyId());
//        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));
        profileService.clearFromDbDeletedCustomFieldsByFormId(LayoutRPC.PURCHASEORDER_FORM, null, false);
        solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.PURCHASE_ORDER);
        solrDbConsistencyManager.flushAndClear();
        try {
            if (solrReindex.isAllReindex()) {
                this.solrManager.removeCompanyPurchaseOrder(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deleteTaskIds = quoteManager.getCompanyDeletedPurchaseOrdersForSolr(solrReindex);
                this.solrManager.removeCompanyPurchaseOrdersbyIds(deleteTaskIds.toArray(new Integer[]{}));
            }
        } catch (Exception e) {
            log.error("Error Purchase Order Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int start = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsPurchaseOrder> ordersList = quoteManager.getPurchaseOrderListForSolr(solrReindex, start, limit);
        while (!ordersList.isEmpty()) {
            try {
                purchaseOrderSolrComponent.indexConcurrently(ordersList);
            } catch (IOException | SolrServerException | InterruptedException e) {
                log.error("Error Purchase Order Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            quoteManager.flushAndClear();
            start++;
            ordersList = quoteManager.getPurchaseOrderListForSolr(solrReindex, (start * limit), limit);
        }
        quoteManager.flushAndClear();
    }

    @Override
    @Transactional
    public void purchaseInvoiceToSolrIndex(final SolrReindexRpc solrReindex) {
        getInstance().setCompanyId(solrReindex.getCompanyId());
//        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));
        profileService.clearFromDbDeletedCustomFieldsByFormId(LayoutRPC.PURCHASEINVOICE_FORM, null, false);

        this.solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.PURCHASE_INVOICE);
        solrDbConsistencyManager.flushAndClear();
        try {
            if (solrReindex.isAllReindex()) {
                this.solrManager.removeCompanyPurchaseInvoice(solrReindex.getCompanyId());
            } else if (solrReindex.getLastUpdateTime() != null) {
                List<Integer> deleteTaskIds = invoiceManager.getCompanyDeletedPurchaseInvoicesForSolr(solrReindex);
                this.solrManager.removeCompanyPurchaseInvoicebyIds(deleteTaskIds.toArray(new Integer[]{}));
            }
        } catch (final IOException | SolrServerException e) {
            log.error("Error Purchase Invoice Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int start = 0;
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit

        List<EdsPurchaseInvoice> purchaseInvoiceList = invoiceManager.getPurchaseInvoiceListForSolr(solrReindex, start, limit);
        while (!purchaseInvoiceList.isEmpty()) {
            try {
                purchaseInvoiceSolrComponent.indexConcurrently(purchaseInvoiceList);
            } catch (IOException | SolrServerException | InterruptedException e) {
                log.error("Error Purchase Invoice Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
            }
            invoiceManager.flushAndClear();
            start++;
            purchaseInvoiceList = invoiceManager.getPurchaseInvoiceListForSolr(solrReindex, (start * limit), limit);
        }
        invoiceManager.flushAndClear();
    }

    @Override
    public String getQuoteConvertToInvoiceCustomType() {
        final Integer quoteConvertToInvoiceCustomType = this.invoicingSettingsManager.getInvoiceSettings(null).getQuoteConvertToInvoiceCustomType();
        EdsReference type = this.referenceManager.findReference(Constants.INVOICE_CUSTOM_TYPE, Constants.PRODUCT_INVOICE);

        if (quoteConvertToInvoiceCustomType != null) {
            type = this.referenceManager.get(quoteConvertToInvoiceCustomType);
        }

        return type != null ? type.getCode() : null;
    }

    @Override
    @Transactional
    public SelectItem convertToSaleOrder(final Integer quoteID) {
        //return saveOrConvertSalesOrder(quoteID, true, true);
        final SelectItem result = new SelectItem();
        final EdsUser user = this.userManager.getUser();
        final EdsCompany company = user.getCompany();
        final EdsInvoicingSettings invSettings = this.invoicingSettingsManager.getInvoiceSettings(company);

        final boolean salesOrderNumberingEnabled = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SALES_ORDER_NUMBERING);
        final EdsSaleQuote saleQuote = (EdsSaleQuote) this.quoteManager.get(quoteID);

        final EdsSaleQuote saleOrder = saleQuote.cloneShallow();

        saleOrder.setComissionAllocateItems(null);
        saleOrder.setQuoteTaxTotals(null);
        saleOrder.setApprovers(null);
        saleOrder.setOrderApprovers(null);
        saleOrder.setQuoteItems(null);
        saleOrder.setInvoices(null);


        saleOrder.setFromNumber(saleQuote.getNumber());
        saleOrder.setQuotationDate(saleQuote.getInvoiceDate());
        saleOrder.setReference(saleQuote.getReference());
        if (salesOrderNumberingEnabled) {
            saleOrder.setNumber(this.applyQuoteNumberData(saleQuote, this.getSalesOrderNumber()).getInvoiceNumber());
            saleOrder.setFourDigitNumber(Integer.valueOf(this.getSalesOrderNumber().getFourDigitNumber()));
        } else {
//            saleOrder.setNumber(applyQuoteNumberData(saleQuote, getQuoteNumber()).getInvoiceNumber());
            saleOrder.setNumber(saleQuote.getNumber());
            saleOrder.setFourDigitNumber(saleQuote.getFourDigitNumber());
        }

        saleQuote.setStatus(getInvoiceStatus(Constants.CONVERTED));
        saleQuote.setQuoteNumberCN(saleOrder.getNumber());

        final List<CompanyCustomFieldItem> saleOrderCustomFieldsItems = this.commonService.getCompanyCustomFields(ViewName.SaleOrder);
        final ArrayList<CompanyCustomFieldItem> saleQuoteCustomFieldsItems = this.commonService.getCompanyCustomFields(ViewName.SaleQuote);
        final List<CompanyCustomFieldItem> sqCustomValues = CustomFieldsUtils.setRPCCustomFieldItems(saleQuote.getCustomFields(), saleQuoteCustomFieldsItems);

        final List<CompanyCustomFieldItem> soCustomFields = new ArrayList<>();

        for (final CompanyCustomFieldItem so : saleOrderCustomFieldsItems) {
            so.setObjectId(null);
            for (final CompanyCustomFieldItem sq : sqCustomValues) {
                if (so.getDataType().equals(sq.getDataType())
                        && (so.getUiType().equals(sq.getUiType()) || (Constants.UI_TYPE_TEXTBOX.equals(so.getUiType()) && Constants.UI_TYPE_TEXTAREA.equals(sq.getUiType())) || (Constants.UI_TYPE_TEXTBOX.equals(sq.getUiType()) && Constants.UI_TYPE_TEXTAREA.equals(so.getUiType())))
                        && so.getAliasName().equals(sq.getAliasName())) {

                    if (Constants.UI_TYPE_LOOKUP.equals(so.getUiType())) {
                        if (so.getLookUpTypeEnum().equals(sq.getLookUpTypeEnum())) {
                            this.setCustomFieldValues(so, sq);
                        }
                    } else {
                        this.setCustomFieldValues(so, sq);
                    }
                }
            }
            soCustomFields.add(so);
        }

        saleOrder.setCustomFields(this.invoiceServiceLocal.createInvoiceCustomFields(soCustomFields));

        SelectItem[] termsConditions = this.invoiceService.getPaymentInstructions(Constants.SALE_ORDER_CODE);
        if (invSettings != null && invSettings.isSalesQuoteTermCopyToSalesOrder() && saleQuote.getTermsConditionsID() != null) {
            String paymentInstructionText = saleQuote.getPaymentInstruction();
            paymentInstructionText = paymentInstructionText.trim().length() > 30 ? paymentInstructionText.trim().substring(0, 30) + "..." : paymentInstructionText;

            final SelectItem paymentInstructionItem = new SelectItem(saleQuote.getTermsConditionsID(), paymentInstructionText, saleQuote.getPaymentInstruction());
            SelectItem[] paymentInstructionItems = null;

            if (termsConditions.length == 0) {
                termsConditions = new SelectItem[1];
                termsConditions[0] = paymentInstructionItem;
            } else {
                int i = 1;
                paymentInstructionItems = new SelectItem[termsConditions.length + 1];
                paymentInstructionItems[0] = paymentInstructionItem;
                for (final SelectItem item : termsConditions) {
                    paymentInstructionItems[i] = item;
                    i++;
                }
            }
            termsConditions = paymentInstructionItems;
        }
        if (invSettings == null || !invSettings.getCopySQIntroduction()) {
            final SelectItem[] paymentInstructions = this.invoiceService.getPaymentIntroduction(Constants.SALE_ORDER_INTR);
            saleOrder.setIntroduction(paymentInstructions != null && paymentInstructions.length > 0 ? paymentInstructions[0].getDescription() : "");
        }
        if (termsConditions != null) {
            saleOrder.setPaymentInstruction(termsConditions.length > 0 ? termsConditions[0].getDescription() : "");
            saleOrder.setTermsConditionsID(termsConditions.length > 0 ? termsConditions[0].getId() : null);
        }
        if (ServerUtils.isNullOrEmpty(saleOrder.getPaymentInstruction())) {
            saleOrder.setPaymentInstruction(saleQuote.getPaymentInstruction());
        }
        /*if (ServerUtils.isNullOrEmpty(saleOrder.getIntroduction())) {
            saleOrder.setIntroduction(saleQuote.getIntroduction());
        }*/
        saleOrder.setPdfTemplate(this.companyPdfTemplateManager.getCompanyPdfTemplateByIDOrCode(company.getObjectID(), PdfReferenceCodeNameEnum.SO_PACKING_SLIP.name(), null));

        boolean accessCreatPickList = false;
        this.quoteManager.createOrUpdate(saleOrder);

        final List<EdsQuoteItem> quoteItems = saleQuote.getQuoteItems();
        final List<EdsQuoteItem> orderItems = new ArrayList<>();

        final List<CompanyCustomFieldItem> saleOrderItemCustomFields = this.commonService.getCompanyCustomFields(ViewName.SaleOrderItem);
        final ArrayList<CompanyCustomFieldItem> saleQuoteItemCustomFields = this.commonService.getCompanyCustomFields(ViewName.SaleQuoteItem);

        final NewInvoice data = this.getQuote(quoteID, null);
        final NewInvoiceItem[] newItem = data.getItems();
        int count = 0;
        for (final EdsQuoteItem item : quoteItems) {
            final EdsQuoteItem quoteItem = item.cloneShallow();
            final List<CompanyCustomFieldItem> sqItemCustomValues = CustomFieldsUtils.setRPCCustomFieldItems(item.getCustomFields(), saleQuoteItemCustomFields);
            quoteItem.setQuote(saleOrder);

            final List<CompanyCustomFieldItem> soItemCustomFields = new ArrayList<>();

            for (final CompanyCustomFieldItem so : saleOrderItemCustomFields) {
                so.setObjectId(null);
                for (final CompanyCustomFieldItem sq : sqItemCustomValues) {
                    if (so.getDataType().equals(sq.getDataType())
                            && (so.getUiType().equals(sq.getUiType()) || (Constants.UI_TYPE_TEXTBOX.equals(so.getUiType()) && Constants.UI_TYPE_TEXTAREA.equals(sq.getUiType())) || (Constants.UI_TYPE_TEXTBOX.equals(sq.getUiType()) && Constants.UI_TYPE_TEXTAREA.equals(so.getUiType())))
                            && so.getAliasName().equals(sq.getAliasName())) {

                        if (Constants.UI_TYPE_LOOKUP.equals(so.getUiType())) {
                            if (so.getLookUpTypeEnum().equals(sq.getLookUpTypeEnum())) {
                                this.setCustomFieldValues(so, sq);
                            }
                        } else {
                            this.setCustomFieldValues(so, sq);
                        }
                    }
                }
                soItemCustomFields.add(so);
            }
            if (!soItemCustomFields.isEmpty()) {
                quoteItem.setCustomFields(this.invoiceServiceLocal.createInvoiceItemCustomFields(soItemCustomFields));
            }
            this.quoteItemManager.createOrUpdate(quoteItem);

            if (newItem[count].getAttachments() != null && !newItem[count].getAttachments().isEmpty()) {
                this.attachmentUtilsManager.saveAttachments(Constants.F_SALE_QUOTE_ITEM, quoteItem.getObjectID(), quoteItem.getObjectID(), newItem[count].getAttachments().toArray(new FileItem[]{}));
            }
            for (final FileItem file : newItem[count].getAttachments()) {
                this.saveSQLineItemImage(quoteItem.getObjectID(), file, user);
            }
            count++;
            orderItems.add(quoteItem);
        }
        saleOrder.setQuoteItems(orderItems);
        final List<FileResource> attachments = this.attachmentUtilsManager.getAttachments(Constants.F_SALE_QUOTE, saleQuote.getObjectID(), saleQuote.getObjectID(), user);
        for (final FileResource file : attachments) {
            this.attachmentUtilsManager.copyFileWhenConvert(Constants.F_SALE_QUOTE, file.getFolderId(), file.getObjectId(), saleOrder.getObjectID(), file);
        }

        if (saleQuote.getApprovers() != null && saleQuote.getApprovers().size() > 0) {
            final List<EdsApprover> approvers = new ArrayList<>();
            for (final EdsApprover approver : saleQuote.getApprovers()) {
                final EdsApprover newApprover = approver.cloneShallow();
                newApprover.setApproverRoles(null);
                newApprover.setApproverEmployees(null);
                newApprover.setDynamicQueries(null);
                newApprover.setApproverHistory(null);
                newApprover.setEntityID(saleOrder.getObjectID());
                this.approverManager.createOrUpdate(newApprover);
                approvers.add(newApprover);
            }
            saleOrder.setApprovers(approvers);
        }
        if (saleQuote.getOrderApprovers() != null && saleQuote.getOrderApprovers().size() > 0) {
            final List<EdsApprover> approvers = new ArrayList<>();
            for (final EdsApprover approver : saleQuote.getOrderApprovers()) {
                final EdsApprover newApprover = approver.cloneShallow();
                newApprover.setApproverRoles(null);
                newApprover.setApproverEmployees(null);
                newApprover.setDynamicQueries(null);
                newApprover.setApproverHistory(null);
                newApprover.setEntityID(saleOrder.getObjectID());
                this.approverManager.createOrUpdate(newApprover);
                approvers.add(newApprover);
            }
            saleOrder.setOrderApprovers(approvers);
        }
        saleOrder.setSalesOrder(true);

        if (saleQuote.getQuoteTaxTotals() != null && saleQuote.getQuoteTaxTotals().size() > 0) {
            final List<EdsQuoteTaxTotal> quoteTaxTotals = new LinkedList<>();
            for (final EdsQuoteTaxTotal qtt : saleQuote.getQuoteTaxTotals()) {
                final EdsQuoteTaxTotal qttCloned = qtt.cloneShallow();
                qttCloned.setQuote(saleOrder);
                quoteTaxTotals.add(qttCloned);
            }
            saleOrder.setQuoteTaxTotals(quoteTaxTotals);
        }
        if (saleQuote.getComissionAllocateItems() != null && saleQuote.getComissionAllocateItems().size() > 0) {
            final List<EdsComissionAllocateItem> allocateItems = new LinkedList<>();
            for (final EdsComissionAllocateItem qtt : saleQuote.getComissionAllocateItems()) {
                final EdsComissionAllocateItem qttCloned = qtt.cloneShallow();
                qttCloned.setQuote(saleOrder);
                allocateItems.add(qttCloned);
            }
            saleOrder.setComissionAllocateItems(allocateItems);
        }


        saleOrder.setInvoices(saleQuote.getInvoices() != null && saleQuote.getInvoices().size() > 0 ? saleQuote.getInvoices() : null);

        this.baseEventPostProcessor.registerEvent(SalesQuoteEventListenerImpl.TYPE, SalesQuoteEventListenerImpl.EVENT_SALES_QUOTE_CONVERT_TO_SALE_ORDER, saleQuote, this.userManager.getUser());
        this.baseEventPostProcessor.registerEvent(SalesOrderEventListenerImpl.TYPE, SalesOrderEventListenerImpl.EVENT_SALE_ORDER_CONVERT_FROM_SQ, saleOrder, this.userManager.getUser());


        EdsPickList pickList = this.pickListManager.getPickListBySaleQuoteID(saleOrder.getObjectID());

        if (!saleOrder.isProgressInvoicing()) {

            if (pickList == null) {
                pickList = new EdsPickList();
            }

            if (saleOrder.getQuoteItems() != null && saleOrder.getQuoteItems().size() > 0) {
                accessCreatPickList = false;
                for (final EdsQuoteItem qitem : saleOrder.getQuoteItems()) {

                    if (qitem.getItem() != null && qitem.getItem().getObjectID() != null) {
                        qitem.setPickable(true);
                        accessCreatPickList = true;
                    }
                }
            }
        }

        ArrayList<RelationItem> relationItems = new ArrayList<>();
        relationItems.add(new RelationItem(null, saleOrder.getObjectID(), RelationItem.TYPE_SALEORDER, saleOrder.getNumber(), saleQuote.getObjectID(), RelationItem.TYPE_SALEQUOTE, saleQuote.getNumber()));
        if (relationItems != null) {
            this.allInOneServiceLocal.saveRelations(RelationItem.TYPE_SALEORDER, saleOrder.getObjectID(), saleOrder.getNumber(), relationItems);
        }


        if (accessCreatPickList) {
            pickList.setSaleQuote(saleOrder);
            this.pickListManager.create(pickList);
            this.baseEventPostProcessor.registerEvent(SalesOrderEventListenerImpl.TYPE, SalesOrderEventListenerImpl.EVENT_PICKLIST_SALE_ORDER, saleQuote, user);
            saleOrder.setStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SALE_ORDER));
            this.quoteManager.update(saleOrder);
            this.createQuoteHistory(saleQuote);
            this.addSaleQuoteToSolr(saleQuote);
            try {
                saleQuoteSolrComponent.indexes(Collections.singletonList(saleOrder), Collections.singletonList(pickList));
            } catch (final IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }

            result.setId(saleOrder.getObjectID());
            result.setOrderId(pickList.getObjectID());
            result.setNumber(saleOrder.getNumber());
            return result;
        } else {
            saleOrder.setStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SALE_ORDER));
            this.quoteManager.update(saleOrder);
            this.createQuoteHistory(saleQuote);
            this.addSaleQuoteToSolr(saleQuote);
            this.addSaleQuoteToSolr(saleOrder);

            result.setId(saleOrder.getObjectID());
            result.setNumber(saleOrder.getNumber());
            return result;
        }
    }

    private void setCustomFieldValues(final CompanyCustomFieldItem so, final CompanyCustomFieldItem sq) {
        so.setPredefinedValues(sq.getPredefinedValues());
        so.setPredefinedValuesWithSorting(sq.getPredefinedValuesWithSorting());
        so.setQuery(sq.getQuery());
        so.setQueryItems(sq.getQueryItems());
        so.setFieldStringValue(sq.getFieldStringValue());
        so.setFieldDateNonConvertedValue(sq.getFieldDateNonConvertedValue());
        so.setAttachments(sq.getAttachments());
        so.setLookUpTypeEnum(sq.getLookUpTypeEnum());
        so.setSelectedId(sq.getSelectedId());
        so.setDefaultValue(sq.getDefaultValue());
        so.setPrefix(sq.getPrefix());
        so.setItem(sq.getItem());
        so.setSelectItems(sq.getSelectItems());
    }

    @Override
    public SaveResult checkForCreditLimit(final Integer quoteID) {
        final SaveResult saveResult = new SaveResult();
        saveResult.setExceededCreditLimit(false);

        BigDecimal invoicedGDNTotalsSum = BigDecimal.ZERO;
        final List<EdsShippingData> shippingDataList = this.shippingDataManager.getByQuoteId(quoteID);
        for (final EdsShippingData edsShippingData : shippingDataList) {
            final Integer invoiceId = this.shippingDataManager.getGrnGdnRelatedInvoiceNumber(edsShippingData.getObjectID());
            if (invoiceId != null) {
                final EdsInvoice invoice = this.invoiceManager.get(invoiceId);
                if (!Constants.DRAFT.equals(invoice.getStatus().getCode()) && invoice.getTotal().compareTo(BigDecimal.ZERO) > 0) {
                    invoicedGDNTotalsSum = invoicedGDNTotalsSum.add(invoice.getTotal());
                }
            }
        }

        final EdsQuote edsQuote = this.quoteManager.get(quoteID);

        final EdsCrmAccount crmAccount = edsQuote.getClientOrSupplier();
        BigDecimal creditLimit = crmAccount.getCreditLimit();

        if (creditLimit != null && creditLimit.compareTo(BigDecimal.ZERO) > 0) {

            if (crmAccount.getCurrency() != null && !this.currencyService.getBaseCurrency().getId().equals(crmAccount.getCurrency().getObjectID())) {
                final Double exchangeRate = this.currencyService.getCurrencyRateByDate(crmAccount.getCurrency().getObjectID(), new DateNonConvertable(new Date())).getExchangeRate();
                creditLimit = creditLimit.divide(BigDecimal.valueOf(exchangeRate), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
            }

            final BigDecimal clientBalance = this.crmAccountManager.getClientBalance(crmAccount.getObjectID());
            final BigDecimal neededAmount = edsQuote.getTotal().add(clientBalance).subtract(invoicedGDNTotalsSum);

            if (creditLimit.subtract(neededAmount).compareTo(BigDecimal.ZERO) < 0) {
                saveResult.setCreditLimit(creditLimit);
                saveResult.setRemainingBalance(neededAmount);
                saveResult.setExceededCreditLimit(true);
                saveResult.setMessage(crmAccount.getName());
                return saveResult;
            }
        }
        return saveResult;
    }

    private Integer saveOrConvertSalesOrder(final Integer quoteID, final boolean isSalesOrderAdd) {
        final boolean salesOrderNumberingEnabled = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SALES_ORDER_NUMBERING);
        final Integer companyID = Integer.valueOf(getInstance().getCompanyId());
        final EdsSaleQuote saleQuote = (EdsSaleQuote) this.quoteManager.get(quoteID);

        if (!isSalesOrderAdd) {
            final SelectItem paymentInstructionItem;
            SelectItem[] paymentInstructionItems = null;
            SelectItem[] termsConditions = this.invoiceService.getPaymentInstructions(Constants.SALE_ORDER_CODE);
            saleQuote.setFromNumber(saleQuote.getNumber());
            saleQuote.setQuotationDate(saleQuote.getInvoiceDate());

            if (salesOrderNumberingEnabled) {
                saleQuote.setReference(saleQuote.getNumber());
                saleQuote.setNumber(this.applyQuoteNumberData(saleQuote, this.getSalesOrderNumber()).getInvoiceNumber());
                saleQuote.setFourDigitNumber(Integer.valueOf(this.getSalesOrderNumber().getFourDigitNumber()));
            }
            final EdsCompany company = this.quoteManager.getUser().getCompany();
            final EdsInvoicingSettings invSettings = this.invoicingSettingsManager.getInvoiceSettings(company);

            if (invSettings != null && invSettings.isSalesQuoteTermCopyToSalesOrder() && saleQuote.getTermsConditionsID() != null) {
                String paymentInstructionText = saleQuote.getPaymentInstruction();
                paymentInstructionText = paymentInstructionText.trim().length() > 30 ? paymentInstructionText.trim().substring(0, 30) + "..." : paymentInstructionText;
                paymentInstructionItem = new SelectItem(saleQuote.getTermsConditionsID(), paymentInstructionText, saleQuote.getPaymentInstruction());

                if (termsConditions.length == 0) {
                    termsConditions = new SelectItem[1];
                    termsConditions[0] = paymentInstructionItem;
                } else {
                    int i = 1;
                    paymentInstructionItems = new SelectItem[termsConditions.length + 1];
                    paymentInstructionItems[0] = paymentInstructionItem;
                    for (final SelectItem item : termsConditions) {
                        paymentInstructionItems[i] = item;
                        i++;
                    }
                }
                termsConditions = paymentInstructionItems;
            }
            if (invSettings == null || !invSettings.getCopySQIntroduction()) {
                final SelectItem[] paymentInstructions = this.invoiceService.getPaymentIntroduction(Constants.SALE_ORDER_INTR);
                saleQuote.setIntroduction(paymentInstructions != null && paymentInstructions.length > 0 ? paymentInstructions[0].getDescription() : "");
            }
            if (termsConditions != null) {
                saleQuote.setPaymentInstruction(termsConditions.length > 0 && !ServerUtils.isNullOrEmpty(termsConditions[0].getDescription()) ? termsConditions[0].getDescription() : saleQuote.getPaymentInstruction());
                saleQuote.setTermsConditionsID(termsConditions.length > 0 ? termsConditions[0].getId() : null);
            }
            saleQuote.setPdfTemplate(this.companyPdfTemplateManager.getCompanyPdfTemplateByIDOrCode(company.getObjectID(), PdfReferenceCodeNameEnum.SO_PACKING_SLIP.name(), null));
        }
        saleQuote.setSalesOrder(true);

        boolean accessCreatPickList = false;

        if (isSalesOrderAdd) {
            if (saleQuote.isSalesOrder()) {
                this.baseEventPostProcessor.registerEvent(SalesOrderEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, saleQuote, this.userManager.getUser());
            }
        } else {
            this.baseEventPostProcessor.registerEvent(SalesQuoteEventListenerImpl.TYPE, SalesQuoteEventListenerImpl.EVENT_SALES_QUOTE_CONVERT_TO_SALE_ORDER, saleQuote, this.userManager.getUser());
        }

        EdsPickList pickList = this.pickListManager.getPickListBySaleQuoteID(quoteID);

        if (!saleQuote.isProgressInvoicing()) {

            if (pickList == null) {
                pickList = new EdsPickList();
            }

            if (saleQuote.getQuoteItems() != null && saleQuote.getQuoteItems().size() > 0) {
                accessCreatPickList = false;
                for (final EdsQuoteItem qitem : saleQuote.getQuoteItems()) {

                    if (qitem.getItem() != null && qitem.getItem().getObjectID() != null) {
                        qitem.setPickable(true);
                        accessCreatPickList = true;
                    }
                }
            }
        }

        if (accessCreatPickList) {
            pickList.setSaleQuote(saleQuote);
            this.pickListManager.create(pickList);
            this.baseEventPostProcessor.registerEvent(SalesOrderEventListenerImpl.TYPE, SalesOrderEventListenerImpl.EVENT_PICKLIST_SALE_ORDER, saleQuote, this.userManager.getUser());
            this.quoteManager.update(saleQuote);
            this.createQuoteHistory(saleQuote);
            try {
                saleQuoteSolrComponent.indexes(Collections.singletonList(saleQuote), Collections.singletonList(pickList));
            } catch (final IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
            return pickList.getObjectID();
        } else {
            this.quoteManager.update(saleQuote);
            this.createQuoteHistory(saleQuote);
            this.addSaleQuoteToSolr(saleQuote);
            return null;
        }
    }

    private Integer saveOrUpdateSalesQuoteEnablePicklist(final Integer quoteID) {
        final EdsSaleQuote saleQuote = (EdsSaleQuote) this.quoteManager.get(quoteID);
        boolean accessCreatPickList = false;
        EdsPickList pickList = this.pickListManager.getPickListBySaleQuoteID(quoteID);
        saleQuote.setSalesOrder(false);
        if (!saleQuote.isProgressInvoicing()) {

            if (pickList == null) {
                pickList = new EdsPickList();
            }

            if (saleQuote.getQuoteItems() != null && saleQuote.getQuoteItems().size() > 0) {
                accessCreatPickList = false;
                for (final EdsQuoteItem qitem : saleQuote.getQuoteItems()) {

                    if (qitem.getItem() != null && qitem.getItem().getObjectID() != null) {
                        qitem.setPickable(true);
                        accessCreatPickList = true;
                    }
                }
            }
        }

        if (accessCreatPickList) {
            pickList.setSaleQuote(saleQuote);
            this.pickListManager.createOrUpdate(pickList);
            this.quoteManager.update(saleQuote);
            this.createQuoteHistory(saleQuote);
            try {
                saleQuoteSolrComponent.indexes(Collections.singletonList(saleQuote), Collections.singletonList(pickList));
            } catch (final IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
            return pickList.getObjectID();
        }

        return null;
    }

    private InvoiceNumberData applyQuoteNumberData(final EdsSaleQuote quote, final InvoiceNumberData numberData) {
        if (numberData != null) {
            if (numberData.isWithDate()) {
                numberData.setDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            }
            if (numberData.isWithClient() && quote.getClient() != null) {
                numberData.setClientCode(quote.getClient().getNumber());
            }
            if (numberData.isWithProject()) {
                numberData.setProjectCode(quote.getRelatedProject().getNumber());
            }
        }
        return numberData;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewInvoice getAllQuoteData(final Params fp) {
        final NewInvoice quoteObject;
        final EdsUser user = this.userManager.getUser();
        Set<GenericSettingsEnum> genericSettings = genericSettingsManager.getByKeys(GenericSettingsEnum.LOCK_COMPLETED_PROJECT_ITEMS,
                GenericSettingsEnum.ENABLE_SALES_ORDER_NUMBERING,
                GenericSettingsEnum.CUSTOM_INVOICE_ITEM_PRODUCT_DESCRIPTION_ENABLED,
                GenericSettingsEnum.PURCHASE_CLIENT_ENABLED,
                GenericSettingsEnum.PO_DOUBLE_APPROVER_ENABLED,
                GenericSettingsEnum.CANCEL_DATE_ENABLED,
                GenericSettingsEnum.LUMPSUM_ENABLED,
                GenericSettingsEnum.ROUNDING_MODE_DISABLED,
                GenericSettingsEnum.DOUBLE_TAX_ENABLED,
                GenericSettingsEnum.QUOTE_COMISSION_ENABLED,
                GenericSettingsEnum.DOUBLE_DISCOUNT_ENABLE);


        boolean lockClosedProjectItems = genericSettings.contains(GenericSettingsEnum.LOCK_COMPLETED_PROJECT_ITEMS);
        boolean enableSalesOrderNumbering = genericSettings.contains(GenericSettingsEnum.ENABLE_SALES_ORDER_NUMBERING);
        EdsInvoicingSettings invoicingSettings = this.invoicingSettingsManager.getInvoiceSettings(this.quoteManager.getUser().getCompany());

        if (fp.getObjectID() != null) {
            quoteObject = this.getQuote(fp.getObjectID(), fp.getExternalFormID());
            if (quoteObject.getClientID() != null) {
                if (Constants.RECEIVABLE.equals(fp.getType())) {
                    quoteObject.getTypeItem().setSupplierCustomerBalance(this.crmAccountManager.getClientBalance(quoteObject.getClientID()).doubleValue());
                    quoteObject.setRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(Constants.SALE_ORDER.equals(fp.getInvoiceCustomType()) ? RelationItem.TYPE_SALEORDER : RelationItem.TYPE_SALEQUOTE, fp.getObjectID())));

                } else {
                    final EdsCrmAccount clientBase = this.crmAccountManager.get(quoteObject.getClientID());
                    quoteObject.getTypeItem().setSupplierCustomerBalance(this.crmAccountManager.getSupplierBalance(clientBase.getObjectID()).doubleValue());

                    quoteObject.getTypeItem().setReverseChargeApplicable(clientBase.isReverseChargeApplicable());
                }
            }
        } else {
            if (fp.getExternalFormID() != null && AccountingConstants.COPY_FROM_EXISTING_DATA.equals(fp.getExternalFormID())) {
                quoteObject = this.getQuote(fp.getExternalObjectID(), fp.getExternalFormID());

                if (lockClosedProjectItems && quoteObject.getRelatedProject() != null) {
                    final EdsProject project = this.projectManager.get(quoteObject.getRelatedProjectID());
                    if (EdsProject.CLOSED.equals(project.getStatus().getCode())) {
                        quoteObject.setRelatedProject(null);
                    }
                }
                if (quoteObject.getCustomFieldItems() != null && quoteObject.getCustomFieldItems().size() > 0) {
                    final List<CompanyCustomFieldItem> customFieldValue = quoteObject.getCustomFieldItems();
                    for (final CompanyCustomFieldItem customFieldItem : customFieldValue) {
                        customFieldItem.setObjectId(null);
                    }
                }
            } else if (fp.getExternalFormID() != null && AccountingConstants.COPY_FROM_SO_TO_SQ.equals(fp.getExternalFormID())) {
                quoteObject = this.getQuote(fp.getExternalObjectID(), fp.getExternalFormID());
                quoteObject.setDiscountType(null);
                quoteObject.setDiscountAmount(null);
            } else if (fp.getExternalFormID() != null && AccountingConstants.COPY_FROM_SQ_SO_TO_PO.equals(fp.getExternalFormID())) {
                quoteObject = this.getQuote(fp.getExternalObjectID(), fp.getExternalFormID());
                quoteObject.setDiscountType(null);
                quoteObject.setDiscountAmount(null);

                if (lockClosedProjectItems && quoteObject.getRelatedProject() != null) {
                    final EdsProject project = this.projectManager.get(quoteObject.getRelatedProjectID());
                    if (EdsProject.CLOSED.equals(project.getStatus().getCode())) {
                        quoteObject.setRelatedProject(null);
                    }
                }
                quoteObject.setItems(this.setCostPriceForCopyItems(quoteObject.getItems()));
            } else if (fp.getExternalFormID() != null && AccountingConstants.COPY_FROM_SI_TO_PO.equals(fp.getExternalFormID())) {
                quoteObject = this.invoiceService.getInvoice(fp.getExternalObjectID());
                quoteObject.setDiscountType(null);
                quoteObject.setDiscountAmount(null);

                if (lockClosedProjectItems && quoteObject.getRelatedProject() != null) {
                    final EdsProject project = this.projectManager.get(quoteObject.getRelatedProjectID());
                    if (EdsProject.CLOSED.equals(project.getStatus().getCode())) {
                        quoteObject.setRelatedProject(null);
                    }
                }

                quoteObject.setItems(this.setCostPriceForCopyItems(quoteObject.getItems()));
            } else if (fp.getExternalFormID() != null && AccountingConstants.CONVERT_RFP_TO_PO.equals(fp.getExternalFormID())) {
                quoteObject = new NewInvoice();
                if (fp.getExternalObjectIDList().size() == 1) {
                    final EdsRFP edsRFP = this.rfpManager.get(fp.getExternalObjectIDList().get(0));
                    if (edsRFP.getProject() != null) {
                        quoteObject.setRelatedProject(edsRFP.getProject().getAsSelectItem());
                    }
                }
                final List<EdsRFPItem> list = this.getRPFItemsForConverting(fp.getExternalObjectIDList());
                final NewInvoiceItem[] newInvoiceItems = new NewInvoiceItem[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    final NewInvoiceItem item = new NewInvoiceItem();
                    EdsRFPItem rfpItem = list.get(i);
                    if (rfpItem.getProduct() != null) {
                        final String fullitemName = rfpItem.getProduct().getProductNumber() + " -> " + rfpItem.getProduct().getName();
                        item.setFullItemName(fullitemName);
                        item.setItemName(rfpItem.getProduct().getName());
                        item.setItemID(rfpItem.getProduct().getObjectID());
                        item.setProductType(rfpItem.getProduct().getProductType());
                        item.setUnitPrice(rfpItem.getProduct().getUnitPrice());
                        final Set<EdsCrmAccount> suppliers = rfpItem.getProduct().getSuppliers();
                        if (suppliers != null && !suppliers.isEmpty()) {
                            final EdsCrmAccount account = suppliers.iterator().next();
                            item.setSupplierID(account.getObjectID());
                            item.setSupplierName(account.getName());
                            quoteObject.setTypeItem(new TypeItem(account.getObjectID(), account.getName(), ""));
                        }
                        final EdsAccount assetAccount = rfpItem.getProduct().getAssetAccount();
                        EdsAccount cogsAccount = list.get(i).getProduct().getCogsAccount();
                        if (INVENTORY_ITEM.equals(list.get(i).getProduct().getType())) {
                            if (assetAccount != null) {
                                item.setAccountItem(new AccountItem(assetAccount.getObjectID(), assetAccount.getCodeString() + "->" + assetAccount.getName()));
                            }
                        } else {
                            if (cogsAccount != null) {
                                item.setAccountItem(new AccountItem(cogsAccount.getObjectID(), cogsAccount.getCodeString() + "->" + cogsAccount.getName()));
                            }
                        }
                    } else if (rfpItem.getItemName() != null) {
                        item.setFullItemName(rfpItem.getItemName());
                        item.setItemName(rfpItem.getItemName());
                    }

                    item.setQuantity(rfpItem.getQty());
                    item.setDescription(rfpItem.getDescription());
                    if (rfpItem.getMeasurement() != null) {
                        item.setMeasurement(rfpItem.getMeasurement().getAsSelectItem());
                    }
                    if (rfpItem.getDepartment() != null) {
                        item.setDepartmentItem(rfpItem.getDepartment().getAsSelectItem());
                    }

                    newInvoiceItems[i] = item;
                }
                quoteObject.setItems(newInvoiceItems);
            } else if (fp.getExternalFormID() != null && AccountingConstants.COPY_FROM_PRODUCT_LIST.equals(fp.getExternalFormID())) {
                quoteObject = new NewInvoice();

                final List<Integer> productIDList = fp.getExternalObjectIDList();
                final List<NewInvoiceItem> invoiceItemList = new LinkedList<>();
                for (final Integer productID : productIDList) {
                    final EdsItem product = this.itemManager.get(productID);
                    if (AccountingConstants.INVENTORY_ITEM.equals(product.getType()) || (!AccountingConstants.INVENTORY_ITEM.equals(product.getType()) && product.isPurchasedFromSupplier()) || fp.isSaleQuote()) {
                        final NewInvoiceItem invoiceItem = new NewInvoiceItem();
                        product.setInvoiceItemData(invoiceItem);
                        if (AccountingConstants.ASSEMBLY_ITEM.equals(product.getType()) &&
                                genericSettings.contains(GenericSettingsEnum.CUSTOM_INVOICE_ITEM_PRODUCT_DESCRIPTION_ENABLED)) {
                            invoiceItem.setCustomDescription(product.getCustomDescriptionData());
                        } else {
                            invoiceItem.setDescription(product.getDescription());
                        }
                        invoiceItem.setDiscountPercent(BigDecimal.ZERO);
                        invoiceItem.setItemDiscountList(EdsDiscount.getItemDiscounts(product.getDiscounts()));
                        invoiceItem.setQuantity(BigDecimal.ONE);
                        if (Constants.PAYABLE.equals(fp.getType())) {
                            if (product.getAssetAccount() != null) {
                                invoiceItem.setAccountID(product.getAssetAccount().getObjectID());
                                invoiceItem.setAccountName(product.getAssetAccount().getName());
                                invoiceItem.setAccountItem(product.getAssetAccount().createAccountItem());
                            }
                            if (product.getCogsAccount() != null && product.isPurchasedFromSupplier()) {
                                invoiceItem.setAccountID(product.getCogsAccount().getObjectID());
                                invoiceItem.setAccountName(product.getCogsAccount().getName());
                                invoiceItem.setAccountItem(product.getCogsAccount().createAccountItem());
                            }
                        } else if (product.getAccount() != null) {
                            invoiceItem.setAccountID(product.getAccount().getObjectID());
                            invoiceItem.setAccountName(product.getAccount().getName());
                            invoiceItem.setAccountItem(product.getAccount().createAccountItem());
                        }
                        if (Constants.RECEIVABLE.equals(fp.getType())) {
                            invoiceItem.setUnitPrice(product.getSellingPrice() != null ? product.getSellingPrice() : BigDecimal.ZERO);
                        } else {
                            invoiceItem.setUnitPrice(product.getUnitPrice() != null ? product.getUnitPrice() : BigDecimal.ZERO);
                        }
                        invoiceItemList.add(invoiceItem);
                    }
                }
                quoteObject.setItems(invoiceItemList.toArray(new NewInvoiceItem[]{}));
            } else if (AccountingConstants.COPY_PO_TO_SQ.equals(fp.getExternalFormID())) {
                final NewInvoice poObject = this.getQuote(fp.getExternalObjectID(), fp.getExternalFormID());

                if (lockClosedProjectItems && poObject.getRelatedProject() != null) {
                    final EdsProject project = this.projectManager.get(poObject.getRelatedProjectID());
                    if (EdsProject.CLOSED.equals(project.getStatus().getCode())) {
                        poObject.setRelatedProject(null);
                    }
                }

                final Double comission = (Double) this.crmAccountManager.getCustomFieldValue(poObject.getClientID(), EdsCrmAccount.CUSTOM_FIELD_AS_MARGIN);

                final List<NewInvoiceItem> quoteItemList = new LinkedList<>();
                for (final NewInvoiceItem item : poObject.getItems()) {
                    final NewInvoiceItem quoteItem = new NewInvoiceItem();
                    quoteItem.setItemID(item.getItemID());
                    quoteItem.setItemName(item.getItemName());
                    quoteItem.setFullItemName(item.getFullItemName());
                    quoteItem.setDescription(item.getDescription());
                    quoteItem.setItemCategory(item.getItemCategory());
                    quoteItem.setDiscountPercent(BigDecimal.ZERO);
                    quoteItem.setProductType(item.getProductType());
                    quoteItem.setTaxItem(item.getTaxItem());
                    quoteItem.setTaxAmount(item.getTaxAmount());
                    quoteItem.setDoubleTaxItem(item.getDoubleTaxItem());
                    quoteItem.setDoubleTaxAmount(item.getDoubleTaxAmount());
                    quoteItem.setAccountItem(item.getSalesAccount());

                    if (comission != null && comission != 0d) {
                        quoteItem.setUnitPrice(item.getUnitPrice().multiply(BigDecimal.ONE.add(new BigDecimal(comission).divide(new BigDecimal(100), ServerUtils.getSystemPriceScale(), RoundingMode.HALF_UP))));
                    } else if (item.getItemID() != null) {
                        final EdsItem pi = this.itemManager.get(item.getItemID());
                        quoteItem.setUnitPrice(pi.getSellingPrice());
                    } else {
                        quoteItem.setUnitPrice(item.getUnitPrice());
                    }
                    quoteItem.setQuantity(item.getQuantity());
                    quoteItemList.add(quoteItem);
                }

                quoteObject = new NewInvoice();
                quoteObject.setPoNumber(poObject.getInvoiceNumber());
                quoteObject.setInvoiceTermsItem(poObject.getInvoiceTermsItem());
                quoteObject.setItems(quoteItemList.toArray(new NewInvoiceItem[0]));
                quoteObject.setTypeItem(poObject.getTypeItem());
            } else {
                quoteObject = new NewInvoice();
                quoteObject.setTaxCalculationType(allInOneServiceLocal.getTaxCalcTypeForInvoice());

                if (fp.getExternalObjectID() != null && fp.getExternalFormID() != null &&
                        (AccountingConstants.COPY_FROM_CLIENT_SUPPLIER.equals(fp.getExternalFormID()) || AccountingConstants.COPY_FROM_CRM_ACCOUNT.equals(fp.getExternalFormID()))) {
                    quoteObject.setTypeItem(this.getClientOrSupplier(fp.getExternalObjectID(), fp.getType()));
                    if (fp.getRelatedProjectID() != null) {
                        quoteObject.setRelatedProject(this.getRelatedProject(fp.getRelatedProjectID()));
                    }
                } else if (fp.getExternalFormID() != null && AccountingConstants.COPY_FROM_CLIENT_SUPPLIER.equals(fp.getExternalFormID()) && fp.getRelatedProjectID() != null) {
                    quoteObject.setRelatedProject(this.getRelatedProject(fp.getRelatedProjectID()));
                } else if (user.isClientContact()) {
                    final Integer crmAccountId = user.getClientContact().getCrmContact() != null &&
                            user.getClientContact().getCrmContact().getCrmAccount() != null ?
                            user.getClientContact().getCrmContact().getCrmAccount().getObjectID() :
                            null;
                    quoteObject.setTypeItem(this.getClientOrSupplier(crmAccountId, fp.getType()));
                    if (quoteObject.getTypeItem() != null) {
                        quoteObject.setAccess(true);
                    }
                }
            }
            if (fp.getOpportunityID() != null) {
                final EdsOpportunity opportunity = this.jpaTemplate.find(EdsOpportunity.class, fp.getOpportunityID());
                if (opportunity != null) {
                    final ArrayList<CompanyCustomFieldItem> opportunityCustomFields = CustomFieldsUtils.setRPCCustomFieldItems(opportunity.getCustomFields(),
                            this.commonService.getCompanyCustomFields(ViewName.Opportunity));

                    final ArrayList<CompanyCustomFieldItem> customFieldsItems = this.commonService.getCompanyCustomFields(Constants.RECEIVABLE.equals(fp.getType()) ? fp.getViewName() : ViewName.PurchaseOrder);
                    final ArrayList<CompanyCustomFieldItem> saleQuoteCustomFields = CustomFieldsUtils.setRPCCustomFieldItems(null, customFieldsItems);

                    for (final CompanyCustomFieldItem inputcf : saleQuoteCustomFields) {
                        for (final CompanyCustomFieldItem resultcf : opportunityCustomFields) {
                            if (inputcf.getAliasName().equals(resultcf.getAliasName()) && inputcf.getUiType().equals(resultcf.getUiType())) {
                                if (Constants.UI_TYPE_DATEPICKER.equals(inputcf.getUiType()) || Constants.UI_TYPE_DATEPICKER_TIME.equals(inputcf.getUiType())) {
                                    inputcf.setFieldDateNonConvertedValue(resultcf.getFieldDateNonConvertedValue());
//                        resultcf.setFieldDateValue(inputcf.getFieldDateValue());
                                } else {
                                    inputcf.setFieldStringValue(resultcf.getFieldStringValue());
                                }
                                // Selection based fields validate and persist by their selected id(s),
                                // not by the display text - carry the selection state over as well.
                                if (Constants.TYPE_ENTITY_LOOKUP.equals(inputcf.getUiType())
                                        || Constants.UI_TYPE_LOOKUP.equals(inputcf.getUiType())
                                        || Constants.UI_TYPE_CURRENCY.equals(inputcf.getUiType())) {
                                    inputcf.setSelectedId(resultcf.getSelectedId());
                                    inputcf.setEntityType(resultcf.getEntityType());
                                } else if (Constants.TYPE_ENTITY_MULTI_LOOKUP.equals(inputcf.getUiType())
                                        || Constants.UI_TYPE_MULTI_LOOKUP.equals(inputcf.getUiType())) {
                                    inputcf.setSelectItems(resultcf.getSelectItems());
                                } else if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(inputcf.getUiType())) {
                                    inputcf.setItem(resultcf.getItem());
                                }
                            }
                        }

                        if (inputcf.getAliasName().equals("PROBABILITY") && (Constants.UI_TYPE_TEXTAREA.equals(inputcf.getUiType()) || Constants.UI_TYPE_TEXTBOX.equals(inputcf.getUiType())) && opportunity.getProbability() != null) {
                            inputcf.setFieldStringValue(opportunity.getProbability().toString());
                        } else if (inputcf.getAliasName().equals("ASSIGNEE") && Constants.UI_TYPE_LOOKUP.equals(inputcf.getUiType()) && CustomFieldLookUpTypeEnum.EMPLOYEE.equals(inputcf.getLookUpTypeEnum()) && opportunity.getAssignee() != null) {
                            inputcf.setFieldStringValue(opportunity.getAssignee().getFullName());
                            inputcf.setSelectedId(opportunity.getAssignee().getObjectID());
                        } else if (inputcf.getAliasName().equals("BACKUP_ASSIGNEE") && Constants.UI_TYPE_LOOKUP.equals(inputcf.getUiType()) && CustomFieldLookUpTypeEnum.EMPLOYEE.equals(inputcf.getLookUpTypeEnum()) && opportunity.getBackupAssignee() != null) {
                            inputcf.setFieldStringValue(opportunity.getBackupAssignee().getFullName());
                            inputcf.setSelectedId(opportunity.getBackupAssignee().getObjectID());
                        } else if (inputcf.getAliasName().equals("NUMBER") && (Constants.UI_TYPE_TEXTAREA.equals(inputcf.getUiType()) || Constants.UI_TYPE_TEXTBOX.equals(inputcf.getUiType())) && opportunity.getNumber() != null) {
                            inputcf.setFieldStringValue(opportunity.getNumber());
                        } else if (inputcf.getAliasName().equals("NAME") && (Constants.UI_TYPE_TEXTAREA.equals(inputcf.getUiType()) || Constants.UI_TYPE_TEXTBOX.equals(inputcf.getUiType())) && opportunity.getName() != null) {
                            inputcf.setFieldStringValue(opportunity.getName());
                        } else if (inputcf.getAliasName().equals("CUSTOMER") && Constants.UI_TYPE_LOOKUP.equals(inputcf.getUiType()) && CustomFieldLookUpTypeEnum.CUSTOMER.equals(inputcf.getLookUpTypeEnum()) && opportunity.getCrmAccount() != null) {
                            inputcf.setFieldStringValue(opportunity.getCrmAccount().getName());
                            inputcf.setSelectedId(opportunity.getCrmAccount().getObjectID());
                        } else if (inputcf.getAliasName().equals("CONTACT") && Constants.UI_TYPE_LOOKUP.equals(inputcf.getUiType()) && CustomFieldLookUpTypeEnum.CONTACT.equals(inputcf.getLookUpTypeEnum()) && opportunity.getCrmAccount() != null) {
                            inputcf.setFieldStringValue(opportunity.getCrmContact().getFullName());
                            inputcf.setSelectedId(opportunity.getCrmContact().getObjectID());
                        } else if (inputcf.getAliasName().equals("LEAD_SOURCE") && Constants.UI_TYPE_LOOKUP.equals(inputcf.getUiType()) && opportunity.getLeadSource() != null) {
                            inputcf.setFieldStringValue(opportunity.getLeadSource().getName());
                            inputcf.setSelectedId(opportunity.getLeadSource().getObjectID());
                        }
                    }
                    quoteObject.setCustomFieldItems(saleQuoteCustomFields);

                    final EdsCrmAccount client = opportunity.getCrmAccount();
                    if (client != null) {
                        quoteObject.setClientID(client.getObjectID());
                        quoteObject.setClientName(client.getName());
                        if (client.getTerms() != null) {
                            quoteObject.setInvoiceTermsItem(client.getTerms().getAsRPC());
                        }
                        if (!AccountingConstants.COPY_FROM_OPPORTUNITY_TO_PO.equals(fp.getExternalFormID())) {
                            quoteObject.setTypeItem(this.getClientOrSupplier(client.getObjectID(), fp.getType()));
                        }
                        Integer currencyId = opportunity.getCurrency() != null ? opportunity.getCurrency().getObjectID() : client.getCurrency() != null ? client.getCurrency().getObjectID() : null;
                        quoteObject.setCurrencyID(currencyId);
                        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_REFERENCE_IN_SUM)) {
                            quoteObject.setReference(String.valueOf(currencyService.getExchangeRateInSumm(currencyId != null ? currencyId : 0)));
                        } else {
                            quoteObject.setReference(opportunity.getNumber() + " -> " + opportunity.getName());
                        }
                        quoteObject.setExchageRate(opportunity.getExchangeRate());
                        if (opportunity.getTaxCalculationType() != null) {
                            quoteObject.setTaxCalculationType(opportunity.getTaxCalculationType());
                        }
                        if (opportunity.getProject() != null) {
                            quoteObject.setRelatedProject(new SelectItem(opportunity.getProject().getObjectID(), opportunity.getProject().getNumber() != null ? opportunity.getProject().getNumber() + " -> " + opportunity.getProject().getName() : opportunity.getProject().getName()));
                        }
                        final NewInvoiceItem[] invoiceItems = new NewInvoiceItem[(opportunity.getOpportunityItems() != null && opportunity.getOpportunityItems().size() > 0) ? opportunity.getOpportunityItems().size() : 1];
                        if (opportunity.getOpportunityItems() != null && opportunity.getOpportunityItems().size() > 0) {
                            int i = 0;
                            for (final EdsOpportunityItem item : opportunity.getOpportunityItems()) {
                                invoiceItems[i] = new NewInvoiceItem();
                                final EdsItem product = item.getItem();
                                invoiceItems[i].setItemID(product != null ? product.getObjectID() : 0);
                                if (product != null) {
                                    invoiceItems[i].setFullItemName(product.getProductNumber() + " -> " + product.getName());
                                } else {
                                    invoiceItems[i].setFullItemName(opportunity.getName());
                                }
                                if (product != null) {
                                    invoiceItems[i].setItemDiscountList(EdsDiscount.getItemDiscounts(product.getDiscounts()));
                                    if (product.getAccount() != null) {
                                        final EdsAccount account = product.getAccount();
                                        invoiceItems[i].setAccountItem(account.createAccountItem());
                                    } else {
                                        invoiceItems[i].setAccountItem(this.getDefaultAccountItem(fp.getFormType(), fp.getType()));
                                    }
                                    if (item.getVat() != null) {
                                        invoiceItems[i].setTaxItem(item.getVat().createTaxItem());
                                    }
                                } else {
                                    invoiceItems[i].setAccountItem(this.getDefaultAccountItem(fp.getFormType(), fp.getType()));
                                }
                                invoiceItems[i].setQuantity(item.getQty() != null ? item.getQty() : AccountingConstants.ONE);
                                final BigDecimal amount = opportunity.getAmount() != null ? BigDecimal.valueOf(opportunity.getAmount()) : AccountingConstants.ZERO;
                                if (Constants.RECEIVABLE.equals(fp.getType())) {
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
                                invoiceItems[i].setNet(item.getNet());
                                invoiceItems[i].setTotalAmount(item.getSubTotal());

                                invoiceItems[i].setDescription(item.getDescription());
                                invoiceItems[i].setSupplierID(item.getSupplierID());
                                invoiceItems[i].setSupplierName(item.getSupplierName());

                                if (item.getProject() != null) {
                                    invoiceItems[i].setProject(new SelectItem(item.getProject().getObjectID(), item.getProject().getNumber() != null ? item.getProject().getNumber() + " -> " + item.getProject().getName() : item.getProject().getName()));
                                }

                                final ArrayList<CompanyCustomFieldItem> opportunityItemTableCustomFields = CustomFieldsUtils.setRPCCustomFieldItems(item.getCustomFields(), this.commonService.getCompanyCustomFields(ViewName.OpportunitySubItem));

                                final ArrayList<CompanyCustomFieldItem> itemTableCustomFieldsItems = this.commonService.getCompanyCustomFields(Constants.RECEIVABLE.equals(fp.getType()) ? (Constants.SALE_ORDER.equals(fp.getInvoiceCustomType()) ? ViewName.SaleOrderItem : ViewName.SaleQuoteItem) : ViewName.PurchaseOrderItem);
                                final ArrayList<CompanyCustomFieldItem> quoteCustomFields = CustomFieldsUtils.setRPCCustomFieldItems(null, itemTableCustomFieldsItems);

                                invoiceItems[i].setCustomFieldItems(ServerUtils.mergeCustomFields(opportunityItemTableCustomFields, quoteCustomFields));
                                i++;
                            }
                        } else {
                            invoiceItems[0] = new NewInvoiceItem();
                            invoiceItems[0].setFullItemName(opportunity.getName());
                            invoiceItems[0].setQuantity(BigDecimal.ONE);
                            final BigDecimal amount = opportunity.getAmount() != null ? BigDecimal.valueOf(opportunity.getAmount()) : AccountingConstants.ZERO;
                            invoiceItems[0].setUnitPrice(amount);
                            invoiceItems[0].setNet(amount);
                            invoiceItems[0].setAccountItem(this.getDefaultAccountItem(fp.getFormType(), fp.getType()));
                        }
                        quoteObject.setItems(invoiceItems);
                    }
                }
            }

            if (fp.getExternalFormID() != null && !AccountingConstants.COPY_FROM_EXISTING_DATA.equals(fp.getExternalFormID())) {
                if (invoicingSettings != null) {
                    quoteObject.setProgressInvoicing(invoicingSettings.isSalesQuoteProgressInvoicing());
                }
            }

            if (Constants.RECEIVABLE.equals(fp.getType())) {
                if (enableSalesOrderNumbering && Constants.SALE_ORDER.equals(fp.getInvoiceCustomType())) {
                    quoteObject.setNumberData(this.getSalesOrderNumber());
                } else {
                    quoteObject.setNumberData(this.getQuoteNumber());
                }
            } else if (Constants.PAYABLE.equals(fp.getType())) {
                quoteObject.setNumberData(this.getOrderNumber());
            }
        }
        if (Constants.RECEIVABLE.equals(fp.getType())) {
            quoteObject.setDueDays(this.getSalesQuoteDue());
        } else if (Constants.PAYABLE.equals(fp.getType())) {
            quoteObject.setDueDays(this.getPurchaseOrderDue());
        }
        quoteObject.setDefaultAccountItem(this.getDefaultAccountItem(fp.getFormType(), fp.getType()));
        quoteObject.setDefaultDiscountItem(new DiscountItem(invoicingSettings.getDefDiscountSO(), getDiscountName(invoicingSettings.getDefDiscountSO())));
        quoteObject.setDefaultTaxItem(getDefaultTaxItem());
        if (Constants.RECEIVABLE.equals(fp.getType())) {
            quoteObject.setLayoutHTML(PathFinder.getLayoutHTML(Constants.SALE_QUOTE));
            if (Constants.SALE_ORDER.equals(fp.getInvoiceCustomType())) {
                quoteObject.setPdfTemplateList(this.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.SALES_ORDER.name()));

                quoteObject.setApprover(this.approverManager.isExistApproverByEntityType(RelationItem.TYPE_SALEORDER));
            } else {
                quoteObject.setPdfTemplateList(this.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.SALES_QUOTE.name()));
                quoteObject.setApprover(this.approverManager.isExistApproverByEntityType(RelationItem.TYPE_SALEQUOTE));
            }
        } else if (Constants.PAYABLE.equals(fp.getType())) {
            quoteObject.setLayoutHTML(PathFinder.getLayoutHTML(Constants.PURCHASE_ORDER));
            quoteObject.setPurchaseClientEnabled(genericSettings.contains(GenericSettingsEnum.PURCHASE_CLIENT_ENABLED));
            quoteObject.setDoubleApprovalEnabled(genericSettings.contains(GenericSettingsEnum.PO_DOUBLE_APPROVER_ENABLED));
            quoteObject.setCancelDateEnabled(genericSettings.contains(GenericSettingsEnum.CANCEL_DATE_ENABLED));
            quoteObject.setPdfTemplateList(this.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.PURCHASE_ORDER.name()));
            quoteObject.setApprover(this.approverManager.isExistApproverByEntityType(RelationItem.TYPE_PURCHASE_ORDER));
        }
        if (!(AccountingConstants.COPY_FROM_EXISTING_DATA.equals(fp.getExternalFormID()) || AccountingConstants.COPY_FROM_SO_TO_SQ.equals(fp.getExternalFormID()))) {
            if (fp.getObjectID() == null && fp.getOpportunityID() == null) {
                final ArrayList<CompanyCustomFieldItem> customFieldsItems = this.commonService.getCompanyCustomFields(Constants.RECEIVABLE.equals(fp.getType()) && Constants.SALE_ORDER.equals(fp.getInvoiceCustomType()) ? ViewName.SaleOrder : Constants.RECEIVABLE.equals(fp.getType()) ? ViewName.SaleQuote : ViewName.PurchaseOrder);
                quoteObject.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(null, customFieldsItems));
            }
        }
        if (Constants.RECEIVABLE.equals(fp.getType()) && Constants.SALE_ORDER.equals(fp.getInvoiceCustomType())) {
            quoteObject.setSystemCustomFields(commonService.getCompanyCustomFields(ViewName.SaleOrderSystem));
        } else if (Constants.RECEIVABLE.equals(fp.getType())) {
            quoteObject.setSystemCustomFields(commonService.getCompanyCustomFields(ViewName.SaleQuoteSystem));
        }
        final ArrayList<CompanyCustomFieldItem> itemCustomFields = this.commonService.getCompanyAllCustomFields(
                Constants.RECEIVABLE.equals(fp.getType()) && Constants.SALE_ORDER.equals(fp.getInvoiceCustomType()) ? ViewName.SaleOrderItem :
                        Constants.RECEIVABLE.equals(fp.getType()) ? ViewName.SaleQuoteItem : ViewName.PurchaseOrderItem);
        quoteObject.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));

        if (fp.getExternalObjectID() != null) {
            final EdsCrmAccount clientBase = this.crmAccountManager.get(fp.getExternalObjectID());
            if (clientBase != null && clientBase.getTerms() != null) {
                quoteObject.setInvoiceTermsItem(clientBase.getTerms().getAsRPC());
            }
        }

        quoteObject.setBaseCurrency(this.getBaseCurrency());
        quoteObject.setLumpSumEnabled(genericSettings.contains(GenericSettingsEnum.LUMPSUM_ENABLED));
        quoteObject.setRoundingModeDisabled(genericSettings.contains(GenericSettingsEnum.ROUNDING_MODE_DISABLED));
        quoteObject.setDoubleTaxEnabled(genericSettings.contains(GenericSettingsEnum.DOUBLE_TAX_ENABLED));
        quoteObject.setQuoteComissionEnabled(genericSettings.contains(GenericSettingsEnum.QUOTE_COMISSION_ENABLED));
        quoteObject.setDoubleDiscountEnabled(genericSettings.contains(GenericSettingsEnum.DOUBLE_DISCOUNT_ENABLE));
        quoteObject.setCustomItemColumns(this.itemTableSettingService.getColumnConfigs(Constants.RECEIVABLE.equals(fp.getType()) && Constants.SALE_ORDER.equals(fp.getInvoiceCustomType()) ? ItemTableEnum.SALE_ORDER_ITEM : Constants.RECEIVABLE.equals(fp.getType()) ? ItemTableEnum.SALE_QUOTE_ITEM : ItemTableEnum.PURCHASE_ORDER_ITEM));

        if (invoicingSettings != null) {
            quoteObject.setSalesQuoteTermCopyToSalesInvoice(invoicingSettings.isSalesQuoteTermCopyToSalesInvoice());
            quoteObject.setSalesQuoteTermCopyToSalesOrder(invoicingSettings.isSalesQuoteTermCopyToSalesOrder());
            quoteObject.setSalesOrderTermCopyToSalesInvoice(invoicingSettings.isSalesOrderTermCopyToSalesInvoice());
            quoteObject.setCopySOIntroduction(invoicingSettings.getCopySOIntroduction());
            quoteObject.setDueDateType(invoicingSettings.getDueDateType());
        }

        if (fp.getConvertFormType() != null && fp.getConvertFormId() != null) {
            if (RelationItem.TYPE_REQUEST_FOR_QUOTE.equals(fp.getConvertFormType())) {
                final EdsFormProperty formProperty = this.formPropertyManager.getByFormID(Constants.RECEIVABLE.equals(fp.getType()) ? Constants.SALE_ORDER.equals(fp.getFormType()) ? "SALEORDER_FORM" : LayoutRPC.SALEQUOTE_FORM : LayoutRPC.PURCHASEORDER_FORM);
//
                final Gson gson = new Gson();
                final FormProperty[] fields = gson.fromJson(formProperty.getSettingsJSONData(), FormProperty[].class);
//
//
                final RFQData rfqData = this.getRFQData(fp.getConvertFormId(), null);
                quoteObject.setFromName(rfqData.getNumberData() != null ? rfqData.getNumberData().getNumberString() : fp.getConvertFormId().toString());
                if (rfqData != null) {
                    quoteObject.setConvertedRelations(rfqData.getRelations());
                    if (rfqData.getCustomer() != null && Constants.RECEIVABLE.equals(fp.getType())) {
                        final TypeItem typeItem = new TypeItem();
                        typeItem.setId(rfqData.getCustomer() != null ? rfqData.getCustomer().getId() : null);
                        typeItem.setName(rfqData.getCustomer() != null ? rfqData.getCustomer().getName() : "");
                        quoteObject.setTypeItem(typeItem);
                    }
                    if (rfqData.getDate() != null) {
                        quoteObject.setInvoiceDate(rfqData.getDate());
                    }
                    if (rfqData.getInvoiceTermsItem() != null) {
                        quoteObject.setInvoiceTermsItem(rfqData.getInvoiceTermsItem());
                    } else if (rfqData.getValidUntil() != null) {
                        quoteObject.setDueDate(rfqData.getValidUntil());
                    }
//                    if (rfqData.getNumberData() != null) {
//                        quoteObject.setNumberData(rfqData.getNumberData());
//                    }
                    if (rfqData.getCustomFieldList() != null && rfqData.getCustomFieldList().size() > 0) {
                        for (final CompanyCustomFieldItem companyCustomFieldItem : rfqData.getCustomFieldList()) {
                            this.convertFormCustomFields(quoteObject, fields, companyCustomFieldItem, fp);
                        }
                    }
                    if (quoteObject.getCustomFieldItems() != null && quoteObject.getCustomFieldItems().size() > 0) {
                        for (final CompanyCustomFieldItem companyCustomFieldItem : quoteObject.getCustomFieldItems()) {
                            if (companyCustomFieldItem != null) {
                                switch (companyCustomFieldItem.getAliasName()) {
                                    case "CUSTOMER" -> {
                                        if (Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.CUSTOMER.equals(companyCustomFieldItem.getLookUpTypeEnum()) && rfqData.getCustomer() != null) {
                                            companyCustomFieldItem.setSelectedId(rfqData.getCustomer().getId());
                                            companyCustomFieldItem.setFieldStringValue(rfqData.getCustomer().getName());
                                        }
                                    }
                                    case "DATE" -> {
                                        if (Constants.DATA_TYPE_DATE.equals(companyCustomFieldItem.getDataType()) && rfqData.getDate() != null) {
                                            companyCustomFieldItem.setFieldDateNonConvertedValue(rfqData.getDate());
                                        }
                                    }
                                    case "DUE_DATE" -> {
                                        if (Constants.DATA_TYPE_DATE.equals(companyCustomFieldItem.getDataType()) && rfqData.getValidUntil() != null) {
                                            companyCustomFieldItem.setFieldDateNonConvertedValue(rfqData.getValidUntil());
                                        }
                                    }
                                    case "NUMBER" -> {
                                        if (Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) && rfqData.getNumberData() != null) {
                                            companyCustomFieldItem.setFieldStringValue(rfqData.getNumberData().getNumberString());
                                        }
                                    }
                                    case "SQ_NUMBER" -> {
                                        if (Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) && rfqData.getSqNumber() != null) {
                                            companyCustomFieldItem.setFieldStringValue(rfqData.getSqNumber());
                                        }
                                    }
                                    case "PROJECT_MANAGER" -> {
                                        if (Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.PROJECT.equals(companyCustomFieldItem.getLookUpTypeEnum()) && rfqData.getProject() != null) {
                                            companyCustomFieldItem.setSelectedId(rfqData.getProject().getId());
                                            companyCustomFieldItem.setFieldStringValue(rfqData.getProject().getName());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    final ArrayList<NewInvoiceItem> listItems = new ArrayList<>();
                    if (rfqData.getItems() != null && rfqData.getItems().size() > 0) {
                        for (final RFQItem rfqItem : rfqData.getItems()) {
                            final NewInvoiceItem newInvoiceItem = new NewInvoiceItem();
                            final ArrayList<CompanyCustomFieldItem> itemCFs = new ArrayList<>();
                            if (rfqItem != null) {
                                if (rfqItem.getDescription() != null) {
                                    newInvoiceItem.setDescription(rfqItem.getDescription());
                                }
                                if (rfqItem.getProduct() != null) {
                                    newInvoiceItem.setItemID(rfqItem.getProduct().getId());
                                    newInvoiceItem.setItemName(rfqItem.getProduct().getName());
                                    newInvoiceItem.setFullItemName(rfqItem.getProduct().getName());
                                    newInvoiceItem.setItemType(rfqItem.getProduct().getProductType());
                                }
                                if (rfqItem.getQty() != null) {
                                    newInvoiceItem.setQuantity(rfqItem.getQty());
                                }
                                if (rfqItem.getMeasurement() != null) {
                                    newInvoiceItem.setMeasurement(rfqItem.getMeasurement());
                                }
                                if (rfqItem.getUnitCost() != null && Constants.RECEIVABLE.equals(fp.getType())) {
                                    newInvoiceItem.setUnitPrice(rfqItem.getUnitCost());
                                }
                                if (rfqItem.getSupplier() != null) {
                                    newInvoiceItem.setSupplierID(rfqItem.getSupplier().getId());
                                    newInvoiceItem.setSupplierName(rfqItem.getSupplier().getName());
                                }
                                if (rfqItem.getItemCustomFields() != null) {
                                    for (final CompanyCustomFieldItem rfqItemCf : rfqItem.getItemCustomFields()) {
                                        if (rfqItemCf != null) {
                                            this.convertItemTableFields(fp.getType(), newInvoiceItem, itemCFs, rfqItemCf, fp);
                                        }
                                    }
                                }
                            }
                            newInvoiceItem.setCustomFieldItems(itemCFs);
                            listItems.add(newInvoiceItem);
                        }
                    }
                    quoteObject.setItems(listItems.toArray(new NewInvoiceItem[0]));
                }
            } else if (LookUpConstants.CRM_EVENT_CALLOG.equals(fp.getConvertFormType())) {
                final EdsFormProperty formProperty = this.formPropertyManager.getByFormID(LayoutRPC.LOGACALL_FORM);

                final Gson gson = new Gson();
                final FormProperty[] fields = gson.fromJson(formProperty.getSettingsJSONData(), FormProperty[].class);

                final Appointment appointment = this.googleCalendarService.getAppointment(fp.getConvertFormId(), false);
                if (appointment != null) {
                    quoteObject.setFromName(appointment.getSubject());
                    quoteObject.setConvertedRelations(appointment.getRelations());

                    if (appointment.getCustomFieldItems() != null && appointment.getCustomFieldItems().size() > 0) {
                        for (final CompanyCustomFieldItem companyCustomFieldItem : appointment.getCustomFieldItems()) {
                            this.convertFormCustomFields(quoteObject, fields, companyCustomFieldItem, fp);
                        }
                    }
                }
            } else if (fp.getConvertFormType().contains("_FORM")) {

                quoteObject.setConvertedRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(fp.getConvertFormType(), fp.getConvertFormId())));

                final EdsFormProperty formProperty = this.formPropertyManager.getByFormID(Constants.RECEIVABLE.equals(fp.getType()) ? Constants.SALE_ORDER.equals(fp.getFormType()) ? "SALEORDER_FORM" : LayoutRPC.SALEQUOTE_FORM : LayoutRPC.PURCHASEORDER_FORM);

                final Gson gson = new Gson();
                final FormProperty[] fields = gson.fromJson(formProperty.getSettingsJSONData(), FormProperty[].class);

                final EdsCustomFormItems edsItem = this.customFormItemManager.get(fp.getConvertFormId());
                final FormItems formItems = edsItem.toRpc();

                final Set<EdsCustomItemTable> itemTables = edsItem.getItemTables();

                final HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

                if (itemTables != null || itemTables.size() > 0) {

                    for (final EdsCustomItemTable itemTable : itemTables) {
                        final CustomTableRpc rpc = itemTable.getRpc();

                        rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                                this.commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.CustomFormItemTable, rpc.getUuid())));

                        map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                    }
                    formItems.setTableItems(map);
                }
                final Map<String, ArrayList<CustomTableRpc>> tableItems = formItems.getTableItems();


                for (final List<CustomTableRpc> tableRpcs : tableItems.values()) {
                    tableRpcs.sort(Comparator.comparing(CustomTableRpc::getId));
                }

                final ArrayList<NewInvoiceItem> listItems = new ArrayList<>();
                for (final Map.Entry<String, ArrayList<CustomTableRpc>> mapTables : formItems.getTableItems().entrySet()) {
                    final List<CustomTableRpc> values = mapTables.getValue();
                    for (final CustomTableRpc rpc : values) {
                        final NewInvoiceItem newInvoiceItem = new NewInvoiceItem();
                        final ArrayList<CompanyCustomFieldItem> itemCFs = new ArrayList<>();
                        if (rpc != null && rpc.getItemCustomFields() != null) {
                            for (final CompanyCustomFieldItem itemCF : rpc.getItemCustomFields()) {
                                if (itemCF != null) {
                                    this.convertItemTableFields(fp.getType(), newInvoiceItem, itemCFs, itemCF, fp);
                                }
                            }
                        }
                        newInvoiceItem.setCustomFieldItems(itemCFs);
                        listItems.add(newInvoiceItem);
                    }
                }
                quoteObject.setItems(listItems.toArray(new NewInvoiceItem[0]));

                formItems.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsItem.getFormCustomFields(),
                        this.commonServiceLocal.getCompanyCategoryCustomFields(edsItem.getCustomForm() != null ? edsItem.getCustomForm().getObjectID() : null)));

                if (formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
                    for (int i = 0; i < formItems.getCustomFieldItems().size(); i++) {
                        if (Constants.UI_TYPE_AUTONUMBER.equals(formItems.getCustomFieldItems().get(i).getUiType()) && formItems.getCustomFieldItems().get(i).getFieldStringValue() != null) {
                            formItems.setAutoNumber(formItems.getCustomFieldItems().get(i).getFieldStringValue());
                            break;
                        }
                    }
                }
                quoteObject.setFromName(formItems.getAutoNumber() != null ? formItems.getAutoNumber() : formItems.getFormName() + ": " + fp.getConvertFormId());

                if (formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
                    for (final CompanyCustomFieldItem companyCustomFieldItem : formItems.getCustomFieldItems()) {
                        this.convertFormCustomFields(quoteObject, fields, companyCustomFieldItem, fp);
                    }
                }
            }
        }
        boolean hasGrn = shippingDataManager.hasGRNInPurchaseOrder(quoteObject.getID());
        // If PO has GRN or Invoice the line items should be disabled.
        if (Constants.PURCHASE_ORDER.equals(fp.getFormType()) && fp.isEditForm() && (hasGrn || (quoteObject.getInvoicedItems() != null && quoteObject.getInvoicedItems().length > 0))) {
            quoteObject.setIsDeleteAndAddDsiabled(true);
            quoteObject.setConvertedToInvoice(true);
        }
        if (quoteObject.isSalesOrder()) {
            boolean hasGDN = shippingDataManager.hasGDNInSalesOrder(quoteObject.getID());
            quoteObject.setHasGDN(hasGDN);
        }
        quoteObject.setInvoicedItems(quoteObject.getInvoicedItems() != null ? quoteObject.getInvoicedItems() : new NewInvoice[]{});
        if (ViewName.PurchaseOrderSystem.equals(fp.getViewName())) {
            quoteObject.setSystemCustomFields(commonService.getCompanyCustomFields(ViewName.PurchaseOrderSystem));
        }
        return quoteObject;
    }

    private void convertFormCustomFields(final NewInvoice item, final FormProperty[] fields, final CompanyCustomFieldItem companyCustomFieldItem, final Params fp) {
        if (companyCustomFieldItem != null) {
            for (final FormProperty formProperty1 : fields) {
                if (formProperty1 != null) {
                    if (companyCustomFieldItem.getAliasName().equals(formProperty1.getAliasName())) {
                        switch (formProperty1.getCode()) {
                            case "inputcrmaccount" -> {
                                if (Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && Constants.RECEIVABLE.equals(fp.getType()) ? CustomFieldLookUpTypeEnum.CUSTOMER.equals(companyCustomFieldItem.getLookUpTypeEnum()) : CustomFieldLookUpTypeEnum.SUPPLIER.equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                    final TypeItem typeItem = new TypeItem();
                                    typeItem.setId(companyCustomFieldItem.getSelectedId());
                                    typeItem.setName(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                    item.setTypeItem(typeItem);

                                    if (companyCustomFieldItem.getSelectedId() != null) {
                                        final EdsCrmAccount clientBase = this.crmAccountManager.get(companyCustomFieldItem.getSelectedId());
                                        if (clientBase != null && clientBase.getTerms() != null) {
                                            item.setInvoiceTermsItem(clientBase.getTerms().getAsRPC());
                                        }
                                    }
                                }
                            }
                            case "inputdate" -> {
                                if (Constants.UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setInvoiceDate(companyCustomFieldItem.getFieldDateNonConvertedValue());
                                }
                            }
                            case "inputcurrency" -> {
                                if (companyCustomFieldItem.getUiType().equals(Constants.UI_TYPE_CURRENCY) && companyCustomFieldItem.getFieldStringValue() != null) {
                                    item.setCurrencyID(companyCustomFieldItem.getSelectedId());
                                    item.setCurrencyName(companyCustomFieldItem.getFieldStringValue());
                                }
                            }
                            case "inputduedate" -> {
                                if (Constants.UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setDueDate(companyCustomFieldItem.getFieldDateNonConvertedValue());
                                }
                            }
                            case "CUSTOMER_INVOICE_TERM" -> {
                                if (Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.TERMS.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getSelectedId() != null) {
                                    final EdsInvoiceTerms invoiceTerms = this.invoiceTermsManager.get(companyCustomFieldItem.getSelectedId());
                                    if (invoiceTerms != null) {
                                        item.setInvoiceTermsItem(invoiceTerms.getAsRPC());
                                    }
                                }
                            }
                            case "inputreference" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                                    item.setReference(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                            }
                            case "inputcanceldate" -> {
                                if (Constants.UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setCancelDate(companyCustomFieldItem.getFieldDateNonConvertedValue());
                                }
                            }
                        }
                    }
                }
            }

            if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
                for (final CompanyCustomFieldItem cf : item.getCustomFieldItems()) {
                    if (companyCustomFieldItem.getAliasName().equals(cf.getAliasName()) && companyCustomFieldItem.getUiType().equals(cf.getUiType()) && companyCustomFieldItem.getDataType().equals(cf.getDataType())) {
                        if (Constants.UI_TYPE_LOOKUP.equals(cf.getUiType())) {
                            if (cf.getLookUpTypeEnum().equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                cf.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                                cf.setSelectedId(companyCustomFieldItem.getSelectedId());
                                cf.setItem(companyCustomFieldItem.getItem());
                            }
                        } else {
                            cf.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                            cf.setSelectedId(companyCustomFieldItem.getSelectedId());
                            cf.setItem(companyCustomFieldItem.getItem());
                            cf.setFieldDateNonConvertedValue(companyCustomFieldItem.getFieldDateNonConvertedValue());
                        }
                    }
                }
            }
        }
    }

    private void convertItemTableFields(final String type, final NewInvoiceItem newInvoiceItem, final ArrayList<CompanyCustomFieldItem> itemCFs, final CompanyCustomFieldItem itemCF, final Params fp) {
        if ("DESCRIPTION".equals(itemCF.getAliasName()) && (Constants.UI_TYPE_TEXTBOX.equals(itemCF.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(itemCF.getUiType())) || (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType()) && "PRODUCT".equals(itemCF.getAliasName()))) {
            if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType())) {
                newInvoiceItem.setDescription(itemCF.getItem() != null ? itemCF.getItem().getDescription() : "");
            } else {
                newInvoiceItem.setDescription(itemCF.getFieldStringValue() != null ? itemCF.getFieldStringValue() : "");
            }
        }
        if ("PRODUCT".equals(itemCF.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.PRODUCT.equals(itemCF.getLookUpTypeEnum()) || Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType()))) {
            if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType())) {
                newInvoiceItem.setItemID(itemCF.getItem() != null ? itemCF.getItem().getId() : null);
                newInvoiceItem.setItemName(itemCF.getItem() != null ? itemCF.getItem().getName() : "");
                newInvoiceItem.setFullItemName(itemCF.getItem() != null ? itemCF.getItem().getName() : "");
            } else {
                newInvoiceItem.setItemID(itemCF.getSelectedId());
                newInvoiceItem.setItemName(itemCF.getFieldStringValue() != null ? itemCF.getFieldStringValue() : "");
                newInvoiceItem.setFullItemName(itemCF.getFieldStringValue() != null ? itemCF.getFieldStringValue() : "");
            }
            if (newInvoiceItem.getItemID() != null) {
                final EdsItem item = this.itemManager.get(newInvoiceItem.getItemID());
                if (Constants.PAYABLE.equals(fp.getType())) {
                    if (item.getAssetAccount() != null) {
                        final EdsAccount account = item.getAssetAccount();
                        newInvoiceItem.setAccountID(account.getObjectID());
                        newInvoiceItem.setAccountItem(account.createAccountItem());
                    }
                } else {
                    if (item.getAccount() != null) {
                        final EdsAccount account = item.getAccount();
                        newInvoiceItem.setAccountID(account.getObjectID());
                        newInvoiceItem.setAccountItem(account.createAccountItem());
                    }
                }
            }
        }
        if ("QTY".equals(itemCF.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
            newInvoiceItem.setQuantity(itemCF.getFieldStringValue() != null ? new BigDecimal(itemCF.getFieldStringValue()) : null);
        }
        if ("MEASUREMENT".equals(itemCF.getAliasName()) && Constants.UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.UNIT_MEASUREMENT.equals(itemCF.getLookUpTypeEnum())) {
            newInvoiceItem.setMeasurement(new SelectItem(itemCF.getSelectedId(), itemCF.getFieldStringValue() != null ? itemCF.getFieldStringValue() : ""));
        }
        if (Constants.RECEIVABLE.equals(fp.getType())) {
            if ("UNITPRICE".equals(itemCF.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
                newInvoiceItem.setUnitPrice(itemCF.getFieldStringValue() != null ? new BigDecimal(itemCF.getFieldStringValue()) : null);
            }
        } else {
            if ("COSTPRICE".equals(itemCF.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
                newInvoiceItem.setUnitPrice(itemCF.getFieldStringValue() != null ? new BigDecimal(itemCF.getFieldStringValue()) : null);
            }
        }
        if ("DISCOUNT_AMT".equals(itemCF.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
            newInvoiceItem.setDiscountAmount(itemCF.getFieldStringValue() != null ? new BigDecimal(itemCF.getFieldStringValue()) : null);
        }
        if ("PROJECT".equals(itemCF.getAliasName()) && Constants.UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.PROJECT.equals(itemCF.getLookUpTypeEnum())) {
            newInvoiceItem.setProject(new SelectItem(itemCF.getSelectedId(), itemCF.getFieldStringValue() != null ? itemCF.getFieldStringValue() : ""));
        }

        for (final CompanyCustomFieldItem customFieldItem : this.commonService.getCompanyCustomFields(Constants.RECEIVABLE.equals(fp.getType()) ? Constants.SALE_ORDER.equals(fp.getInvoiceCustomType()) ? ViewName.SaleOrderItem : ViewName.SaleQuoteItem : ViewName.PurchaseOrderItem)) {
            if (customFieldItem != null && itemCF.getUiType().equals(customFieldItem.getUiType()) && itemCF.getAliasName().equals(customFieldItem.getAliasName())) {
                if (Constants.UI_TYPE_LOOKUP.equals(customFieldItem.getUiType())) {
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
                itemCFs.add(customFieldItem);
            }
        }
    }

    private NewInvoiceItem[] setCostPriceForCopyItems(final NewInvoiceItem[] items) {
        EdsItem item;
        for (final NewInvoiceItem lineItem : items) {
            item = lineItem.getItemID() != null ? this.itemManager.getItem(lineItem.getItemID()) : null;
            lineItem.setAccountItem(null);
            lineItem.setID(null);
            if (item != null) {
                if (item.getUnitPrice() != null) {
                    lineItem.setUnitPrice(item.getUnitPrice());
                } else {
                    lineItem.setUnitPrice(item.getSellingPrice());
                }
                if (item.getAssetAccount() != null) {
                    final EdsAccount expenseAccount = item.getAssetAccount();
                    lineItem.setAccountItem(new AccountItem(expenseAccount.getObjectID(), expenseAccount.getAccountCode(), expenseAccount.getName()));
                    lineItem.setAccountID(expenseAccount.getObjectID());
                    lineItem.setAccountName(expenseAccount.getName());
                }
            } else {
                lineItem.setUnitPrice(new BigDecimal("0.00"));
            }
        }
        return items;
    }

    private SelectItem getRelatedProject(final Integer projectID) {
        final EdsProject project = this.projectManager.get(projectID);
        return new SelectItem(project.getObjectID(),
                (project.getNumber() != null && !"".equals(project.getNumber().trim()) ? project.getNumber() + " - " : "") + project.getName());
    }

    @Override
    public SelectItem[] getPurchaseOrders(final ListingFilterParameter filterParameter) {
        filterParameter.setStatusValues(Stream.of(Constants.APPROVE, Constants.OPEN, Constants.PARTIAL_RECEIVED, Constants.RECEIVED, Constants.INVOICED).collect(Collectors.joining("','", "'", "'")));
        final List<EdsPurchaseOrder> orders = this.quoteManager.getPurchaseOrderList(filterParameter, null);
        final SelectItem[] items = new SelectItem[orders.size()];
        int i = 0;
        for (final EdsPurchaseOrder po : orders) {
            items[i++] = new SelectItem(po.getObjectID(), po.getNumber());
        }
        return items;
    }

    @Override
    public SelectItem[] getGrnItems(final ListingFilterParameter filterParameter) {
        filterParameter.setStatusValues("'" + Constants.APPROVE + "', '" + Constants.OPEN + "', '" + Constants.PARTIAL_RECEIVED + "'");
        final List<EdsShippingData> edsShippingDataList = this.shippingDataManager.getList(filterParameter);
        final SelectItem[] items = new SelectItem[edsShippingDataList.size()];
        int i = 0;
        for (final EdsShippingData shd : edsShippingDataList) {
            items[i++] = new SelectItem(shd.getObjectID(), shd.getNumber());
        }
        return items;
    }

    @Override
    public List<CompanyCustomFieldItem> getQuoteCustomFields(final Integer entityId, final ViewName viewName) {
        final EdsQuote quote = this.quoteManager.get(entityId);
        final EdsCustomFields edsCustomFields = quote.getCustomFields();
        return CustomFieldsUtils.setRPCCustomFieldItems(edsCustomFields, this.commonServiceLocal.getCompanyCustomFields(viewName));
    }

    @Override
    public void createQuoteCustomFields(final Integer entityId, final List<CompanyCustomFieldItem> customFieldTO) {
        final EdsQuote quote = this.quoteManager.get(entityId);
        quote.setCustomFields(this.invoiceServiceLocal.createInvoiceCustomFields(customFieldTO));
        quote.setUpdatedDate(new Date());
        this.quoteManager.update(quote);

        final EdsPickList pickList = this.pickListManager.getPickListBySaleQuoteID(quote.getObjectID());
        try {
            if (quote instanceof EdsSaleQuote) {
                saleQuoteSolrComponent.indexes(Collections.singletonList((EdsSaleQuote) quote), (pickList != null ? Collections.singletonList(pickList) : null));
            } else if (quote instanceof EdsPurchaseOrder) {
                purchaseOrderSolrComponent.index((EdsPurchaseOrder) quote);
            }
        } catch (final IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePurchaseOrdersAfterExportSaasu(final Integer objectId, final Date lastUpdateDate, final String
            saasuLastUpdatedUid, final Integer saasuGUID) {
        final EdsPurchaseOrder order = this.quoteManager.getPurchaseOrderByID(objectId);
        if (order != null) {
            if (saasuGUID != null) {
                order.setSaasuGUID(saasuGUID.toString());
            }
            order.setSasuuLastUpdatedTime(lastUpdateDate);
            order.setSaasuLastUpdatedUid(saasuLastUpdatedUid);
            this.quoteManager.update(order);
        }
    }

    @Override
    public Integer convertToProject(final Integer salesQuoteID) {
        final EdsSaleQuote saleQuote = this.quoteManager.getSaleQuote(salesQuoteID);
        EdsEmployee assignee = saleQuote.getCreator() != null ? this.employeeManager.get(saleQuote.getCreator().getObjectID()) : null;
        saleQuote.setConvertedToProject(true);
        if (assignee == null) {
            assignee = this.employeeManager.get(this.employeeManager.getUser().getObjectID());
        }

        final ProjectSingleItem project = new ProjectSingleItem();
        project.setNumberData(this.projectServiceLocal.generateProjectNumber(new Date(), saleQuote.getClient().getObjectID(), null));
        project.setName(saleQuote.getReference() != null ? saleQuote.getReference() + " " +  saleQuote.getNumber()  : saleQuote.getNumber());
        project.setManagerId(assignee.getObjectID());
        if (saleQuote.getInvoiceDate() != null && saleQuote.getDueDate() != null) {

            final EdsUser user = this.userManager.get(saleQuote.getCreator().getObjectID());
            project.setStartDate(new Date(saleQuote.getInvoiceDate().getTime() - user.getUserTimezone().getRawOffset()));
            project.setEndDate(new Date(saleQuote.getDueDate().getTime() - user.getUserTimezone().getRawOffset()));
        }
        final EdsReference notStarted = this.referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.NOT_STARTED);
        project.setStatusId(notStarted.getObjectID());

        final ProjectMember member = new ProjectMember();
        member.setId(assignee.getObjectID());
        member.setWageRate(assignee.getWageRate());
        member.setClientChargeRate(assignee.getClientChargeRate());
        project.setProjectMembers(new ProjectMember[]{member});

        final ArrayList<CompanyCustomFieldItem> projectCustomFields = this.commonService.getCompanyCustomFields(ViewName.Project);
        final List<CompanyCustomFieldItem> opportunityCustomFields = CustomFieldsUtils.setRPCCustomFieldItems(saleQuote.getCustomFields(), this.commonService.getCompanyCustomFields(ViewName.Opportunity));
        for (final CompanyCustomFieldItem projectCustomFieldItem : projectCustomFields) {
            for (final CompanyCustomFieldItem fieldItem : opportunityCustomFields) {
                if ((StringUtils.equals(projectCustomFieldItem.getAliasName(), fieldItem.getAliasName())) && (StringUtils.equals(projectCustomFieldItem.getDataType(), fieldItem.getDataType()) || StringUtils.equals(Constants.DATA_TYPE_TEXT, projectCustomFieldItem.getDataType()))) {
                    if (Constants.DATA_TYPE_DATE.equals(projectCustomFieldItem.getDataType())) {
                        projectCustomFieldItem.setFieldDateNonConvertedValue(fieldItem.getFieldDateNonConvertedValue());
                    } else if (Constants.DATA_TYPE_NUMBER.equals(projectCustomFieldItem.getDataType())) {
                        projectCustomFieldItem.setFieldStringValue(fieldItem.getFieldStringValue());
                    } else {
                        projectCustomFieldItem.setFieldStringValue(fieldItem.getFieldStringValue());
                    }
                    projectCustomFieldItem.setFacetable(fieldItem.isFacetable());
                    projectCustomFieldItem.setShowInListing(fieldItem.isShowInListing());
                    projectCustomFieldItem.setClickable(fieldItem.isClickable());
                    projectCustomFieldItem.setShowInFilterGrouping(fieldItem.isShowInFilterGrouping());
                    projectCustomFieldItem.setObjectId(null);
                }
            }
            projectCustomFieldItem.setObjectId(null);
        }

        project.setCustomFieldItems(projectCustomFields);

        final EdsCrmAccount customer = saleQuote.getClientOrSupplier();
        customer.addAccountType(this.referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER));
        this.crmAccountManager.update(customer, true);
        project.setClientId(customer.getObjectID());

        //project source
        project.setProjectSource(Constants.PROJECT_SOURCE_CONVERT_FROM_SALES_QUOTE + salesQuoteID);

        Integer projectID = null;
        try {
            projectID = this.projectServiceLocal.saveProject(project);

            saleQuote.setRelatedProject(this.projectManager.get(projectID));

            if (saleQuote.getQuoteItems() != null && saleQuote.getQuoteItems().size() > 0) {
                project.setParentId(projectID);
                this.convertingQuoteItemsToTasks(saleQuote.getQuoteItems(), project);
            }
            ArrayList<RelationItem> relationItems = EdsRelation.asRPCs(this.relationManager.getAllRelations(RelationItem.TYPE_SALEORDER, salesQuoteID));
            relationItems.add(new RelationItem(null,projectID,"project",project.getName(),salesQuoteID,"saleorder", saleQuote.getNumber()));
            allInOneServiceLocal.saveRelations("saleorder",salesQuoteID,saleQuote.getNumber(),relationItems);
            saleQuote.setConvertedToProject(true);
        } catch (final NumberExistingException e) {
            e.printStackTrace();
        }
        this.quoteManager.merge(saleQuote);
        this.addSaleQuoteToSolr(saleQuote);

        return projectID;
    }

    private void convertingQuoteItemsToTasks(final List<EdsQuoteItem> items, final ProjectSingleItem project) {
        for (final EdsQuoteItem item : items) {
            final TaskSingleItem taskItem = new TaskSingleItem();
            taskItem.setProjectID(project.getParentId());

            taskItem.setNumberData(this.taskServiceLocal.generateTaskNumber(project.getParentId(), project.getStartDate(), null));
            taskItem.setName(item.getItem() != null ? item.getItem().getName() : item.getItemName());

            taskItem.setDescription(item.getDescription());
            taskItem.setStartDate(project.getStartDate());
            taskItem.setEndDate(project.getEndDate());
            taskItem.setDueDate(project.getEndDate());
            taskItem.setAllDay(true);
            taskItem.setBillable(true);

            final SelectItem[] priorities = this.taskServiceLocal.getPriorities();
            for (final SelectItem priority : priorities) {
                if (priority.getName().trim().equals("Medium")) {
                    taskItem.setPriorityID(priority.getId());
                }
            }

            final ProjectMember[] members = this.projectServiceLocal.getProjectEmployees(project.getParentId());
            final IdTime[] projectEmployees = new IdTime[members.length];

            final int i = 0;
            for (final ProjectMember member : members) {
                projectEmployees[i] = new IdTime(member.getProjectEmployeeId(), 0);
            }
            taskItem.setProjectEmployees(projectEmployees);

            try {
                this.taskServiceLocal.saveTask(taskItem);
            } catch (final NumberExistingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public RFQData getRFQData(final Integer objectID, final Params formParameters) {
        final RFQData rfqData;
        EdsRFQ edsRFQ = null;
        if (objectID != null) {
            final EdsUser edsUser = this.rfqManager.getUser();
            edsRFQ = this.rfqManager.get(objectID);
            edsRFQ.setItemCustomFields(this.commonService.getCompanyCustomFields(ViewName.RFQItem));
            if (ServerUtils.hasPermission(PermissionConstants.REQUEST_FOR_QUOTE_CELL_EDITABLE)) {
                rfqData = edsRFQ.createRFQData(false);
                rfqData.setEditable(!this.rfqManager.isSupplierBidApplied(edsRFQ.getObjectID()));
                rfqData.setSupplier(true);
                final ArrayList<RFQItem> itemsList = new ArrayList<>();
                final List<EdsRFQItem> rfqItemsList;
                if (edsUser.hasRole(this.roleManager.getByCode(Constants.SUPPLIER))) {
                    final EdsCrmAccount supplier = edsUser.getClientContact().getCrmContact().getCrmAccount();
                    rfqItemsList = this.rfqManager.getRFQItemsBySupplier(supplier.getObjectID(), objectID);
                } else {
                    rfqItemsList = this.rfqManager.getRFQItemsForAccountant(objectID);
                }
                for (final EdsRFQItem edsRFQItem : rfqItemsList) {
                    final RFQItem item = edsRFQItem.createItemData(edsRFQ.getItemCustomFields());
                    final List<FileResource> expAttachments = this.attachmentUtilsManager.getAttachments(Constants.F_RFQ, item.getObjectID(), item.getObjectID());
                    item.setAttachments(expAttachments.toArray(new FileResource[]{}));
                    itemsList.add(item);
                }
                rfqData.setItems(itemsList);
            } else {
                rfqData = edsRFQ.createRFQData(true);
            }
            if (edsRFQ.getInvoiceTerms() != null) {
                rfqData.setInvoiceTermsItem(edsRFQ.getInvoiceTerms().getAsRPC());
            }
            rfqData.setTemplates(this.invoiceServiceLocal.getCompanyPdfTemplates(AccountingConstants.RFQ).getItems());
            final EdsCompanyPdfTemplate template = this.companyPdfTemplateManager.getDefaultCompanyPdfTemplateByType(AccountingConstants.RFQ);
            if (template != null) {
                rfqData.setSelectedTemplateId(template.getObjectID());
            }
            rfqData.setNotConvertedSupplierBidExists(this.rfqManager.isNotConvertedBidsExists(objectID));
            rfqData.setHistoryList(this.getRFQNotes(objectID).toArray(new HistoryListItem[]{}));

            final ArrayList<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(edsRFQ.getCustomFields(), this.commonService.getCompanyCustomFields(ViewName.RequestForQuote));
            customFieldItems.sort((o1, o2) -> ((Comparable) o1.getEntityId()).compareTo(o2.getEntityId()));
            rfqData.setCustomFieldList(customFieldItems);

            if (edsRFQ.getCurrentApprover() != null && edsRFQ.getCurrentApprover().getExactEmployee() != null) {
                if (edsRFQ.getCurrentApprover().getExactEmployee().isEmployee()) {
                    final EdsEmployee edsEmployee = edsRFQ.getCurrentApprover().getExactEmployee().getEmployee();
                    if (edsEmployee.getProfile() != null && edsEmployee.getProfile().getEmployeeCode() != null) {
                        rfqData.setApprover(new SelectItem(edsEmployee.getObjectID(), edsEmployee.getProfile().getEmployeeCode() + " - " + edsEmployee.getFullName()));
                    } else {
                        rfqData.setApprover(edsRFQ.getCurrentApprover().getExactEmployee().getAsSelectItem());
                    }
                } else {
                    rfqData.setApprover(edsRFQ.getCurrentApprover().getExactEmployee().getAsSelectItem());
                }
            }
            rfqData.setApproverSaved(this.approverManager.isExistApproverByEntityTypeAndEntityId(RelationItem.TYPE_REQUEST_FOR_QUOTE, edsRFQ.getObjectID()));
            rfqData.setRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(RelationItem.TYPE_REQUEST_FOR_QUOTE, edsRFQ.getObjectID())));
            if (rfqData.getRelations() != null) {
                final ArrayList<Email> emails = new ArrayList<>();
                for (final RelationItem relationItem : rfqData.getRelations()) {
                    final EdsEmail email = this.emailRepository.findLastByTrackerId(relationItem.getFromID());
                    if (email != null) {
                        emails.add(email.getRPC());
                    }
                }
                rfqData.setLinkedEmails(new ListResult<Email>(emails, emails.size()));
            }

            final List<EdsPaymentInstruction> instructions = this.paymentInstructionManager.getInstructions(EdsPaymentInstruction.REQUEST_FOR_QUOTE_INSTRUCTION);
            if (instructions != null && instructions.size() > 0) {
                final SelectItem[] items = new SelectItem[instructions.size()];
                int i = 0;
                for (final EdsPaymentInstruction pi : instructions) {
                    final String name;
                    if (pi.getText() != null && !"".equals(pi.getText().trim())) {
                        name = pi.getText();
                    } else {
                        name = "(no data)";
                    }
                    items[i++] = new SelectItem(pi.getObjectID(), name, pi.getText() != null ? pi.getText() : "");
                }

                rfqData.setInstructions(items);
            }
        } else {
            rfqData = new RFQData();
            rfqData.setNumberData(this.rfqManager.generateNumberData());
            final ArrayList<RFQItem> rfqItems = new ArrayList<>();

            if (formParameters.getClientId() != null) {
                final EdsCrmAccount crmAccountItem = this.crmAccountManager.get(formParameters.getClientId());
                rfqData.setCustomer(new SelectItem(crmAccountItem.getObjectID(), crmAccountItem.getName()));
            }
            if ("opportunity".equals(formParameters.getCrmFormName()) && formParameters.getOpportunityID() != null) {
                final EdsOpportunity opportunity = this.jpaTemplate.find(EdsOpportunity.class, formParameters.getOpportunityID());
                if (opportunity != null && opportunity.getCrmAccount() != null) {
                    rfqData.setCustomer(new SelectItem(opportunity.getCrmAccount().getObjectID(), opportunity.getCrmAccount().getName()));
                    if (opportunity.getCrmAccount().getTerms() != null) {
                        rfqData.setInvoiceTermsItem(opportunity.getCrmAccount().getTerms().getAsRPC());
                    }
                }
                if (opportunity != null && opportunity.getOpportunityItems() != null && opportunity.getOpportunityItems().size() > 0) {
                    for (final EdsOpportunityItem item : opportunity.getOpportunityItems()) {
                        if (item.getItem() == null) {
                            final RFQItem rfqItem = new RFQItem();
                            rfqItem.setProduct(new ProductSelectItem(item.getObjectID(), item.getItemName()));
                            rfqItem.setQty(item.getQty());
                            rfqItem.setMeasurement(item.getUnitMeasurement() != null ? item.getUnitMeasurement().getAsSelectItem() : null);
                            rfqItem.setUnitCost(item.getPrice());
                            rfqItem.setDescription(item.getDescription());
                            rfqItem.setCommission(item.getDiscount());
                            if (item.getSupplierID() != null) {
                                final EdsCrmAccount supplier = this.crmAccountManager.get(item.getSupplierID());
                                rfqItem.setSupplier(supplier.getAsSelectItem());
                            }
                            rfqItems.add(rfqItem);
                        } else {
                            final EdsItem product = item.getItem();
                            if (product.getSuppliers() != null && !product.getSuppliers().isEmpty()) {
                                for (final EdsCrmAccount supplier : product.getSuppliers()) {
                                    final RFQItem rfqItem = new RFQItem();
                                    rfqItem.setProduct(product.getAsProductSelectItem());
                                    rfqItem.setSupplier(supplier.getAsSelectItem());
                                    rfqItem.setQty(item.getQty());
                                    rfqItem.setMeasurement(item.getUnitMeasurement() != null ? item.getUnitMeasurement().getAsSelectItem() : null);
                                    rfqItem.setUnitCost(item.getItem().getUnitPrice());
                                    rfqItem.setDescription(item.getDescription());
                                    rfqItem.setCommission(item.getDiscount());
                                    rfqItems.add(rfqItem);
                                }
                            } else {
                                final RFQItem rfqItem = new RFQItem();
                                rfqItem.setProduct(product.getAsProductSelectItem());
                                rfqItem.setQty(item.getQty());
                                rfqItem.setMeasurement(item.getUnitMeasurement() != null ? item.getUnitMeasurement().getAsSelectItem() : null);
                                rfqItem.setUnitCost(item.getItem().getUnitPrice());
                                rfqItem.setDescription(item.getDescription());
                                rfqItem.setCommission(item.getDiscount());
                                if (item.getSupplierID() != null && item.getSupplierName() != null) {
                                    rfqItem.setSupplier(new SelectItem(item.getSupplierID(), item.getSupplierName()));
                                }
                                rfqItems.add(rfqItem);
                            }
                        }
                    }
                }
            } else if (formParameters.getExternalFormID() != null && AccountingConstants.CONVERT_RFP_TO_RFQ.equals(formParameters.getExternalFormID())) {
                final List<EdsRFPItem> list = this.getRPFItemsForConverting(formParameters.getExternalObjectIDList());
                for (final EdsRFPItem item : list) {
                    final RFQItem rfqItem = new RFQItem();
                    rfqItem.setProduct(item.getProduct().getAsProductSelectItem());
                    rfqItem.setQty(item.getQty());
                    if (item.getMeasurement() != null) {
                        rfqItem.setMeasurement(item.getMeasurement().getAsSelectItem());
                    }
                    //                    rfqItem.setUnitCost(item.getProduct().getUnitPrice());
                    rfqItem.setDescription(item.getDescription());

                    rfqItems.add(rfqItem);
                }
            }
            if (!rfqItems.isEmpty())
                rfqData.setItems(rfqItems);
        }
        if (rfqData.getMailAddressId() != null) {
            final EdsAddress address = this.addressManager.get(rfqData.getMailAddressId());
            if (address != null)
                rfqData.setAddressData(address.getRPC());
        } else {
            rfqData.setAddressData(this.createRFQAddressData());
        }
        final ArrayList<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(edsRFQ != null ? edsRFQ.getCustomFields() : null, this.commonService.getCompanyCustomFields(ViewName.RequestForQuote));
        customFieldItems.sort((o1, o2) -> ((Comparable) o1.getEntityId()).compareTo(o2.getEntityId()));
        rfqData.setCustomFieldList(customFieldItems);
        rfqData.setItemCustomFields(this.commonService.getCompanyCustomFields(ViewName.RFQItem));
        rfqData.setCustomItemColumns(formParameters != null && formParameters.isView() ? this.itemTableSettingsServiceLocal.getColumnConfigs(ItemTableEnum.RFQ_ITEM, false, true) : this.itemTableSettingService.getColumnConfigs(ItemTableEnum.RFQ_ITEM));
        rfqData.setCurrentUserId(this.userManager.getUser().getObjectID());
        rfqData.setApprover(this.approverManager.isExistApproverByEntityType(RelationItem.TYPE_REQUEST_FOR_QUOTE));
        EdsInvoicingSettings invoicingSettings = this.invoicingSettingsManager.getInvoiceSettings(this.rfqManager.getUser().getCompany());
        if (invoicingSettings != null) {
            rfqData.setDueDateType(invoicingSettings.getDueDateType());
        }
        return rfqData;
    }

    @Transactional
    public List<HistoryNote> getRFPHistoryNotes(final Integer objectId) {
        if (objectId == null) {
            return null;
        }
        final List<HistoryListItem> notes = this.getRFPNotes(objectId);
        final List<HistoryNote> result = new ArrayList<>(notes);

        final List<MyUpdateItem> updates = this.invoiceServiceLocal.getAllHistory(objectId, LookUpConstants.REQUEST_FOR_PURCHASE);
        result.addAll(updates);


        return result;
    }

    @Transactional
    public List<HistoryNote> getRFQHistoryNotes(final Integer objectId) {
        if (objectId == null) {
            return null;
        }
        final List<HistoryListItem> notes = this.getRFQNotes(objectId);
        final List<HistoryNote> result = new ArrayList<>(notes);
        final List<MyUpdateItem> updates = this.invoiceServiceLocal.getAllHistory(objectId, Constants.REQUEST_FOR_QUOTE);
        result.addAll(updates);
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<HistoryListItem> getRFQNotes(final Integer objectID) {
        return this.rfqRfpNoteManager.getRfqNotesAsHistoryListItem(objectID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<HistoryListItem> getRFPNotes(final Integer objectID) {
        return this.rfqRfpNoteManager.getRfpNotesAsHistoryListItem(objectID);
    }

    private Address createRFQAddressData() {
        final Integer companyId = Integer.parseInt(getInstance().getCompanyId());
        final EdsAddress address = EdsAddress.getFirstAddress(this.addressManager.getAddressesByEntityIdAndType(companyId, EdsAddress.MAILING_ADDRESS, EdsAddress.ENTITY_TYPE_COMPANY), true, null);
        if (address == null)
            return new Address();
        return address.getRPC();
    }

    @Override
    public Integer saveRFQData(final RFQData rfqData) {
        final EdsUser user = this.userManager.getUser();
        final EdsRFQ edsRFQ;
        if (rfqData.getObjectID() != null) {
            edsRFQ = this.rfqManager.get(rfqData.getObjectID());
            edsRFQ.getItems().clear();
//            rfqManager.deleteRFQItems(rfqData.getObjectID());
        } else {
            edsRFQ = new EdsRFQ();
            edsRFQ.setCreator(this.rfqManager.getUser());
        }

        if (this.rfqManager.isRFQNumberExist(rfqData.getNumberData().getNumberString(), rfqData.getObjectID())) {
            return -1;
        }
        if (!isOk(rfqData.getApprovers())) {
            edsRFQ.setEntityStatus(this.referenceManager.findReference(Constants.RFQ_STATUS, rfqData.getStatusCode()));
        }
        boolean isNew = false;
        if (edsRFQ.getObjectID() == null) {
            isNew = true;
            this.rfqManager.create(edsRFQ);
        }
        if (isOk(rfqData.getApprovers())) {
            rfqData.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirstApprover = true;
            for (final ApproverItemMini approverItem : rfqData.getApprovers()) {
                final EdsApprover _edsApprover = this.approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        final EdsUser user_ = this.userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    this.approverManager.update(_edsApprover);
                    if (edsRFQ.getCurrentApprover() != null && rfqData.getStatusCode() != null && isFirstApprover) {
                        edsRFQ.getCurrentApprover().setStatus(this.referenceManager.findReference(Constants.RFQ_STATUS, rfqData.getStatusCode()));
                        edsRFQ.setEntityStatus(this.referenceManager.findReference(Constants.RFQ_STATUS, Constants.RFQ_SUBMITTED));
                        isFirstApprover = false;
                    } else if (edsRFQ.getCurrentApprover() != null && rfqData.getStatusCode() != null) {
                        edsRFQ.getCurrentApprover().setStatus(this.referenceManager.findReference(Constants.RFQ_STATUS, Constants.RFQ_SUBMITTED));
                    }
                    if (rfqData.getStatusCode() != null && !Constants.APPROVE.equals(rfqData.getStatusCode())) {
                        edsRFQ.setEntityStatus(this.referenceManager.findReference(Constants.RFQ_STATUS, rfqData.getStatusCode()));
                    }
                    if (edsRFQ.isCurrentApproverRejected()) {
                        edsRFQ.setEntityStatus(edsRFQ.getCurrentApprover().getStatus());
                    }
                    continue;
                }
                final EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(edsRFQ.getObjectID());
                edsApprover.setIs_default(false);

                if (rfqData.getStatusCode() != null && isFirstApprover) {
                    edsApprover.setStatus(this.referenceManager.findReference(Constants.RFQ_STATUS, rfqData.getStatusCode()));
                    if (Constants.DRAFT.equals(rfqData.getStatusCode())) {
                        edsRFQ.setEntityStatus(this.referenceManager.findReference(Constants.RFQ_STATUS, rfqData.getStatusCode()));
                    } else {
                        edsRFQ.setEntityStatus(this.referenceManager.findReference(Constants.RFQ_STATUS, Constants.RFQ_SUBMITTED));
                    }
                    isFirstApprover = false;
                } else if (rfqData.getStatusCode() != null) {
                    edsApprover.setStatus(this.referenceManager.findReference(Constants.RFQ_STATUS, Constants.RFQ_SUBMITTED));
                }
                if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                    final EdsUser user_ = this.userManager.get(approverItem.getExactEmployee().getId());
                    edsApprover.setExactEmployee(user_);
                }
                edsApprover.setApproverRoles(new HashSet<>());
                edsApprover.setApproverEmployees(new HashSet<>());
                edsApprover.setDynamicQueries(new HashSet<>());
                this.approverManager.createOrUpdate(edsApprover);

                for (final EdsApproverRoles roleapp : _edsApprover.getApproverRoles()) {
                    edsApprover.getApproverRoles().add(roleapp);
                }

                for (final EdsApproverEmployees ucerapp : _edsApprover.getApproverEmployees()) {
                    edsApprover.getApproverEmployees().add(ucerapp);
                }

                if (edsRFQ.getCurrentApprover() == null) {
                    edsRFQ.setCurrentApprover(edsApprover);
                }
                edsRFQ.getApprovers().add(edsApprover);
            }
        }
        edsRFQ.setRequestFrom(rfqData.getRequestFrom());
        edsRFQ.setProject(rfqData.getProject() != null ? this.projectManager.get(rfqData.getProject().getId()) : null);
        edsRFQ.setDate(rfqData.getDate().getNonConvertedDate());
        edsRFQ.setValidUntil(rfqData.getValidUntil().getNonConvertedDate());
        edsRFQ.setNumber(rfqData.getNumberData().getNumberString());
        edsRFQ.setIntNumber(rfqData.getNumberData().getIntNumber());
        edsRFQ.setSqNumber(rfqData.getSqNumber());
        edsRFQ.setIntroduction(rfqData.getIntroduction());
        edsRFQ.setSendNotificationToSuppliers(rfqData.isSendNotificationToSuppliers());
        edsRFQ.setMailingAddress(this.addressManager.get(rfqData.getMailAddressId()));
        edsRFQ.setCustomFields(this.saveCustomFields(edsRFQ.getCustomFields(), rfqData.getCustomFieldList()));
        if (rfqData.getInvoiceTermsItem() != null && rfqData.getInvoiceTermsItem().getId() != null) {
            edsRFQ.setInvoiceTerms(this.invoiceTermsManager.get(rfqData.getInvoiceTermsItem().getId()));
        } else {
            edsRFQ.setInvoiceTerms(null);
        }

        if (rfqData.getCustomer() != null) {
            edsRFQ.setClient(this.crmAccountManager.get(rfqData.getCustomer().getId()));
        }
        if (rfqData.getOpportunityID() != null) {
            edsRFQ.setOpportunityID(rfqData.getOpportunityID());
        }

        final List<EdsRFQItem> rfqItems = new LinkedList<>();
        for (final RFQItem item : rfqData.getItems()) {
            final EdsRFQItem edsRFQItem = new EdsRFQItem();
            if (item.getProduct() != null) {
                final EdsItem product = this.itemManager.get(item.getProduct().getId());
                if (product != null) {
                    edsRFQItem.setProduct(this.itemManager.get(item.getProduct().getId()));
                } else if (item.getProduct().getName() != null) {
                    edsRFQItem.setName(item.getProduct().getName());
                }
            } else if (item.getName() != null) {
                edsRFQItem.setName(item.getName());
            }
            edsRFQItem.setDescription(item.getDescription());
            edsRFQItem.setQty(item.getQty());
            edsRFQItem.setCommission(item.getCommission());
            edsRFQItem.setRemarks(item.getReMarks());
            edsRFQItem.setUnitCost(item.getUnitCost());
            if (item.getMeasurement() != null && item.getMeasurement().getId() != null) {
                edsRFQItem.setMeasurement(this.unitMeasurementManager.get(item.getMeasurement().getId()));
            }
            if (item.getSupplier() != null && item.getSupplier().getId() != null) {
                edsRFQItem.setSupplier(this.crmAccountManager.get(item.getSupplier().getId()));
            }
            this.rfqItemManager.createOrUpdate(edsRFQItem);
            final FileResource[] attachments = item.getAttachments();

            if (attachments != null && attachments.length > 0) {
                final FileItem[] fItems = new FileItem[attachments.length];
                for (int i = 0; i < attachments.length; i++) {
                    fItems[i] = new FileItem();
                    fItems[i].setId(attachments[i].getObjectId());
                    fItems[i].setFileName(attachments[i].getEncodedName());
                }

                this.attachmentUtilsManager.saveAttachments(Constants.F_RFQ, edsRFQItem.getObjectID(), edsRFQItem.getObjectID(), fItems);
            }
            if (item.getItemCustomFields() != null && !item.getItemCustomFields().isEmpty()) {
                edsRFQItem.setCustomFields(this.createRFQItemCustomFields(item.getItemCustomFields()));
            }
            rfqItems.add(edsRFQItem);
        }
        edsRFQ.setItems(rfqItems);

        this.rfqManager.createOrUpdate(edsRFQ);
        if (rfqData.getAttachments() != null && rfqData.getAttachments().length > 0) {
            this.attachmentUtilsManager.saveAttachments(Constants.F_RFQ_1, edsRFQ.getObjectID(), edsRFQ.getObjectID(), rfqData.getAttachments());
        }

        if (rfqData.getRelations() != null && rfqData.getRelations().size() > 0) {
            this.allInOneServiceLocal.saveRelations(RelationItem.TYPE_REQUEST_FOR_QUOTE, edsRFQ.getObjectID(), edsRFQ.getNumber(), rfqData.getRelations());
        }

        addRFQToSolr(edsRFQ);

        //Register event in MyUpdate
        final KpiLog kpiLog = getInstance().getKpiLog();
        if (rfqData.getObjectID() != null) {
            this.baseEventPostProcessor.registerEvent(RequestForQuoteEventListenerimpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsRFQ, user);

            kpiLog.setEntityName(EdsRFQ.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(edsRFQ.getObjectID());
            ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Update request for quote");
        } else {
            this.baseEventPostProcessor.registerEvent(RequestForQuoteEventListenerimpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsRFQ, user);
            kpiLog.setEntityName(EdsRFQ.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            kpiLog.setEntityId(edsRFQ.getObjectID());
            ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Add request for quote");
        }
        this.createRfqNote(rfqData, edsRFQ);
        this.convertRFPstoPO(rfqData.getRfpIds());

        if (edsRFQ.getOverallStatus() != null && Constants.OPEN.equals(edsRFQ.getOverallStatus().getCode())
                && rfqData.isSendNotificationToSuppliers()) {
            this.sendRFQEmployeeRequest(edsRFQ);
        }
        if (isNew) {
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                    BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsRFQ, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_REQUEST_FOR_QUOTE);
        } else {
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                    BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsRFQ, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_REQUEST_FOR_QUOTE);
        }
        /* Run workflow approval process */
        final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(),
                edsRFQ, this.userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_REQUEST_FOR_QUOTE);

        return edsRFQ.getObjectID();
    }

    @Override
    public void updateRFQStatus(final Integer objectId, final String statusCode) {
        final EdsRFQ edsRFQ = this.rfqManager.get(objectId);
        if (edsRFQ == null) {
            throw new IllegalArgumentException("RFQ with such objectId doesn't exist. ObjectId = " + objectId);
        }
        final EdsReference edsReference = this.referenceManager.findReference(Constants.RFQ_STATUS, statusCode);
        if (edsReference == null) {
            throw new IllegalArgumentException("There is no such status for RFQ. Status name = " + statusCode);
        }
        if (!Constants.RFQ_APPROVED.equals(edsReference.getCode())) {
            edsRFQ.setOverallStatus(edsReference);
        } else if (Constants.RFQ_APPROVED.equals(edsReference.getCode()) && edsRFQ.getOverallStatus() != null
                && Constants.RFQ_DRAFT.equals(edsRFQ.getOverallStatus().getCode())) {
            edsRFQ.setOverallStatus(this.referenceManager.findReference(Constants.RFQ_STATUS, Constants.RFQ_SUBMITTED));
        }
        edsRFQ.updateStatus(edsReference);

        this.addRFQToSolr(edsRFQ);
        this.rfqManager.update(edsRFQ);
        this.allInOneServiceLocal.approvedOrRejected(RelationItem.TYPE_REQUEST_FOR_QUOTE, edsRFQ.getObjectID(), null);
    }

    @Override
    public void updateStockTransferStatus(final Integer objectId, final String statusCode, final String rejectionReason) {
        final EdsStockTransfer edsStockTransfer = this.stockTransferManager.get(objectId);
        if (edsStockTransfer != null) {
            final EdsUser user = this.employeeManager.getUser();
            final EdsReference edsReference = this.referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, statusCode);

            if (!Constants.STOCK_TRANSFER_APPROVED.equals(edsReference.getCode())) {
                edsStockTransfer.setOverallStatus(edsReference);
            } else if (Constants.STOCK_TRANSFER_APPROVED.equals(edsReference.getCode()) && edsStockTransfer.getOverallStatus() != null
                    && Constants.STOCK_TRANSFER_DRAFT.equals(edsStockTransfer.getOverallStatus().getCode())) {
                edsStockTransfer.setOverallStatus(this.referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, Constants.STOCK_TRANSFER_SUBMITTED));
            }
            edsStockTransfer.updateStatus(edsReference);
            if (Constants.STOCK_TRANSFER_DECLINED.equals(statusCode)) {
                edsStockTransfer.setOverallStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.REJECT));
            }
            if (Constants.STOCK_TRANSFER_TRANSFERRED.equals(edsReference.getCode())) {
                this.itemBatchService.updateStockTransferBatchItemsStatus(edsStockTransfer.getObjectID());
                this.accountingServiceLocal.createTransactionForStockTransfer(edsStockTransfer);
            }

            if (org.apache.commons.lang3.StringUtils.isNotBlank(rejectionReason)) {
                final HistoryListItem historyListItem = new HistoryListItem();
                historyListItem.setEmployee(this.expenseReportManager.getUser().getName());
                historyListItem.setEventDate(new Date());
                historyListItem.setComment(rejectionReason);
                this.saveStockTransferNotes(historyListItem, edsStockTransfer.getObjectID());
            }

            this.stockTransferManager.update(edsStockTransfer);
//            this.allInOneServiceLocal.approvedOrRejected(RelationItem.TYPE_STOCK_TRANSFER, edsStockTransfer.getObjectID(), null);
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), edsStockTransfer, user);
            workflowEvent.setEntityType(RelationItem.TYPE_STOCK_TRANSFER);

            if (Constants.STOCK_TRANSFER_SUBMITTED.equals(statusCode)) {
                this.baseEventPostProcessor.registerEvent(StockTransferEventListenerImpl.TYPE, StockTransferEventListenerImpl.STOCK_TRANSFER_SUBMITTED, edsStockTransfer, user);
            } else if (Constants.STOCK_TRANSFER_APPROVED.equals(statusCode)) {
                this.baseEventPostProcessor.registerEvent(StockTransferEventListenerImpl.TYPE, StockTransferEventListenerImpl.STOCK_TRANSFER_APPROVED, edsStockTransfer, user);
            } else if (Constants.STOCK_TRANSFER_DECLINED.equals(statusCode)) {
                this.baseEventPostProcessor.registerEvent(StockTransferEventListenerImpl.TYPE, StockTransferEventListenerImpl.STOCK_TRANSFER_DECLINED, edsStockTransfer, user);
            } else if (Constants.STOCK_TRANSFER_TRANSFERRED.equals(statusCode)) {
                this.baseEventPostProcessor.registerEvent(StockTransferEventListenerImpl.TYPE, StockTransferEventListenerImpl.STOCK_TRANSFER_TRANSFERRED, edsStockTransfer, user);
            }
        }
    }

    public EdsRFQItemCustomFields createRFQItemCustomFields(final List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            final EdsRFQItemCustomFields rfqItemCustomFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                rfqItemCustomFields = this.rfqItemCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (final CompanyCustomFieldItem fieldItem : customFieldItems) {
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
                rfqItemCustomFields = new EdsRFQItemCustomFields();
                this.rfqItemCFManager.create(rfqItemCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(rfqItemCustomFields, customFieldItems);
            return rfqItemCustomFields;
        }
        return null;
    }

    public EdsRFPItemCustomFields createRFPItemCustomFields(final List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            final EdsRFPItemCustomFields rfpItemCustomFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                rfpItemCustomFields = this.rfpItemCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (final CompanyCustomFieldItem fieldItem : customFieldItems) {
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
                rfpItemCustomFields = new EdsRFPItemCustomFields();
                this.rfpItemCFManager.create(rfpItemCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(rfpItemCustomFields, customFieldItems);
            return rfpItemCustomFields;
        }
        return null;
    }

    @Transactional
    public EdsRFQCustomFields saveCustomFields(EdsRFQCustomFields
                                                       edsRFQCustomFields, final List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (edsRFQCustomFields == null) {
                boolean isEmpty = true;
                for (final CompanyCustomFieldItem fieldItem : customFieldItems) {
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
                edsRFQCustomFields = new EdsRFQCustomFields();
                this.rfqcfManager.create(edsRFQCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsRFQCustomFields, customFieldItems);
            return edsRFQCustomFields;
        }
        return null;
    }

    @Transactional
    public EdsRFPCustomFields saveRFPCustomFields(EdsRFPCustomFields
                                                          edsRFPCustomFields, final List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (edsRFPCustomFields == null) {
                boolean isEmpty = true;
                for (final CompanyCustomFieldItem fieldItem : customFieldItems) {
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
                edsRFPCustomFields = new EdsRFPCustomFields();
                this.rfpcfManager.create(edsRFPCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsRFPCustomFields, customFieldItems);
            return edsRFPCustomFields;
        }
        return null;
    }

    @Override
    public Integer saveRFQNotes(final HistoryListItem historyListItem, final Integer rfqId) {
        if (historyListItem != null && rfqId != null) {
            final EdsRFQ edsRFQ = this.rfqManager.get(rfqId);
            if (edsRFQ == null) {
                return null;
            }
            if (historyListItem.getObjectID() != null) { // note exists, thus updated
                final EdsRfqRfpNote note = this.rfqRfpNoteManager.get(historyListItem.getObjectID());
                note.setComment(historyListItem.getComment());
                note.setCommentator(this.invoiceManager.getUser());
                note.setSuperUser(ServerUtils.isSuperUser());
                this.rfqRfpNoteManager.update(note);
                return note.getObjectID();
            } else {                                    // note has to be created
                final EdsRfqRfpNote note = new EdsRfqRfpNote();
                note.setComment(historyListItem.getComment());
                note.setCommentator(this.invoiceManager.getUser());
                note.setRfq(edsRFQ);
                note.setDate(new Date());
                note.setSuperUser(ServerUtils.isSuperUser());
                this.rfqRfpNoteManager.create(note);
                return note.getObjectID();
            }
        }
        return null;
    }

    @Override
    public Integer saveRFPNotes(final HistoryListItem historyListItem, final Integer rfqId) {
        if (historyListItem != null && rfqId != null) {
            final EdsRFP edsRFP = this.rfpManager.get(rfqId);
            if (edsRFP == null) {
                return null;
            }
            if (historyListItem.getObjectID() != null) { // note exists, thus updated
                final EdsRfqRfpNote note = this.rfqRfpNoteManager.get(historyListItem.getObjectID());
                note.setComment(historyListItem.getComment());
                note.setCommentator(this.invoiceManager.getUser());
                note.setSuperUser(ServerUtils.isSuperUser());
                this.rfqRfpNoteManager.update(note);
                return note.getObjectID();
            } else {                                    // note has to be created
                final EdsRfqRfpNote note = new EdsRfqRfpNote();
                note.setComment(historyListItem.getComment());
                note.setCommentator(this.invoiceManager.getUser());
                note.setRfp(edsRFP);
                note.setDate(new Date());
                note.setSuperUser(ServerUtils.isSuperUser());
                this.rfqRfpNoteManager.create(note);
                return note.getObjectID();
            }
        }
        return null;
    }

    @Override
    public Boolean deleteRFQNotes(final HistoryListItem historyListItem) {
        if (historyListItem != null && historyListItem.getObjectID() != null) {
            final EdsRfqRfpNote note = this.rfqRfpNoteManager.get(historyListItem.getObjectID());
            if (note != null) {
                this.rfqRfpNoteManager.delete(note);
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean deleteRFPNotes(final HistoryListItem historyListItem) {
        if (historyListItem != null && historyListItem.getObjectID() != null) {
            final EdsRfqRfpNote note = this.rfqRfpNoteManager.get(historyListItem.getObjectID());
            if (note != null) {
                this.rfqRfpNoteManager.delete(note);
                return true;
            }
        }
        return false;
    }

    private void createRfqNote(final RFQData rfqData, final EdsRFQ edsRFQ) {
        final HistoryListItem[] noteItems = rfqData.getHistoryList();
        final List<EdsRfqRfpNote> rfqNotes = this.rfqRfpNoteManager.getRfqNotes(edsRFQ.getObjectID());
        if (noteItems != null && noteItems.length > 0) {
            final HashMap<Integer, Integer> existingNotesMap = new HashMap<>();
            for (final HistoryListItem noteItem : noteItems) {
                if (noteItem.getObjectID() == null && noteItem.getComment() != null && !"".equals(noteItem.getComment())) {
                    final EdsRfqRfpNote note = new EdsRfqRfpNote();
                    note.setComment(noteItem.getComment());
                    note.setCommentator(this.invoiceManager.getUser());
                    note.setRfq(edsRFQ);
                    note.setDate(new Date());
                    note.setSuperUser(ServerUtils.isSuperUser());
                    this.rfqRfpNoteManager.create(note);
                }
                if (noteItem.getObjectID() != null) {
                    existingNotesMap.put(noteItem.getObjectID(), noteItem.getObjectID());
                }
            }

            for (final EdsRfqRfpNote quoteNote : rfqNotes) {
                if (!existingNotesMap.containsKey(quoteNote.getObjectID())) {
                    this.rfqRfpNoteManager.delete(quoteNote);
                }
            }
        } else {
            for (final EdsRfqRfpNote noteForDelete : rfqNotes) {
                this.rfqRfpNoteManager.delete(noteForDelete);
            }
        }
    }

    private void createRfpNote(final RFPData rfpData, final EdsRFP edsRFP) {
        final HistoryListItem[] noteItems = rfpData.getHistoryList();
        final List<EdsRfqRfpNote> rfpNotes = this.rfqRfpNoteManager.getRfpNotes(edsRFP.getObjectID());

        if (noteItems != null && noteItems.length > 0) {
            final HashMap<Integer, Integer> existingNotesMap = new HashMap<>();
            for (final HistoryListItem noteItem : noteItems) {
                if (noteItem.getObjectID() == null && noteItem.getComment() != null && !"".equals(noteItem.getComment())) {
                    final EdsRfqRfpNote note = new EdsRfqRfpNote();
                    note.setComment(noteItem.getComment());
                    note.setCommentator(this.invoiceManager.getUser());
                    note.setDate(new Date());
                    note.setRfp(edsRFP);
                    note.setSuperUser(ServerUtils.isSuperUser());
                    this.rfqRfpNoteManager.create(note);
                }
                if (noteItem.getObjectID() != null) {
                    existingNotesMap.put(noteItem.getObjectID(), noteItem.getObjectID());
                }
            }

            for (final EdsRfqRfpNote purchaseNote : rfpNotes) {
                if (!existingNotesMap.containsKey(purchaseNote.getObjectID())) {
                    this.rfqRfpNoteManager.delete(purchaseNote);
                }
            }
        } else {
            for (final EdsRfqRfpNote noteForDelete : rfpNotes) {
                this.rfqRfpNoteManager.delete(noteForDelete);
            }
        }
        if (edsRFP != null && edsRFP.getRejectionReason() != null && edsRFP.getRejectionReason().trim().length() > 0) {

            final EdsRfqRfpNote note = new EdsRfqRfpNote();
            note.setComment(edsRFP.getRejectionReason());
            note.setCommentator(this.invoiceManager.getUser());
            note.setDate(new Date());
            note.setRfp(edsRFP);
            note.setSuperUser(ServerUtils.isSuperUser());
            this.rfqRfpNoteManager.create(note);

        }
    }

    private void sendRFQEmployeeRequest(final EdsRFQ edsRFQ) {
        try {
            final EdsUser user = this.messageManager.getUser();
            final Integer companyID = user.getCompany().getObjectID();

            final LinkedHashMap<Integer, EdsCrmAccount> suppliersMap = new LinkedHashMap<>();
            for (final EdsRFQItem rfqItem : edsRFQ.getItems()) {
                if (rfqItem.getSupplier() != null) {
                    if (!suppliersMap.containsKey(rfqItem.getSupplier().getObjectID())) {
                        suppliersMap.put(rfqItem.getSupplier().getObjectID(), rfqItem.getSupplier());
                    }
                }
            }

            final String subject = "REQUEST FOR QUOTE";
            final Collection<EdsCrmAccount> suppliersList = suppliersMap.values();
            for (final EdsCrmAccount supplier : suppliersList) {
                final EdsCrmContact primaryContact = supplier.getPrimaryContact();
                if (primaryContact != null) {
                    final String toEmail = primaryContact.getPrimaryEmail();
                    if (toEmail != null && !"".equals(toEmail)) {

                        final Map<String, Object> values = new TreeMap<>();
                        values.put("CONTACT_NAME", primaryContact.getName());
                        values.put("COMPANY_NAME", user.getCompany().getName());
                        values.put("INTRODUCTION", edsRFQ.getIntroduction());

                        if (!primaryContact.isAccessEnabled()) {
                            final Integer clientContactID = this.clientSupplierAccessService.enableAccess(primaryContact.getObjectID(), false, false);
                            if (clientContactID > 0) {
                                final String encryptedClientContactID = EncryptionHelper.encryptURL(clientContactID.toString());
                                final String encryptedCompanyID = EncryptionHelper.encryptURL(companyID.toString());
                                final String activationLink = EdsContextParams.getHost(companyID) + "/account?uid=" + encryptedClientContactID + "&cid=" + encryptedCompanyID;
                                values.put("ACTIVATION_LINK", activationLink);
                            }
                        }

                        final String text = EdsTemplates.processTemplate(values, EdsTemplates.RFQ_REQUEST_MESSAGE);
                        this.messageManager.sendMessageFromUser(null, toEmail, null, null, subject, text, false, null, null, false, null, null, user);
                    }
                }
            }
        } catch (final EdsTemplateException | EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ListResult<RFQData> getRFQList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        FacetFilterRpc rfqFacetFilter = fp.getFacetFilter();
        if (rfqFacetFilter != null && !rfqFacetFilter.isFilterChanges()) {
            rfqFacetFilter = this.commonServiceLocal.getUserFacetFilter(rfqFacetFilter);
        }

        if (rfqFacetFilter != null) {
            if (rfqFacetFilter.getSearchKey() != null && !"".equals(rfqFacetFilter.getSearchKey())) {
                fp.setSearchKey(rfqFacetFilter.getSearchKey());
            }
            fp.setFacetFilter(rfqFacetFilter);
        }
        if (fp.getStartDateNC() != null) {
            fp.setStartDate(ServerUtils.parseFilterParameterDate(fp.getStartDateNC()));
        }
        if (fp.getEndDateNC() != null) {
            fp.setEndDate(ServerUtils.parseFilterParameterDate(fp.getEndDateNC()));
        }
        final ListPanelToolRpc panelTools = fp.getListPanelTool();

        final EdsUser edsUser = this.rfqManager.getUser();
        if (edsUser.hasRole(this.roleManager.getByCode(Constants.SUPPLIER))) {
            final EdsCrmAccount supplier = edsUser.getClientContact().getCrmContact().getCrmAccount();
            fp.setCrmAccountId(supplier.getObjectID());
        }

        final StringBuilder solrQuery = new StringBuilder(this.getRFQSolrQuery(fp, edsUser, null));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(
                rfqFacetFilter, edsUser.getCompany(),
                SolrSaleInvoiceRepresenter.FIELD_DUE_DATE, SolrSaleInvoiceRepresenter.FIELD_DUE_DATE));

//        final SolrClient server = WfmJpaTemplate.getSolrServerForCore(Constants.SOLR_REQUEST_FOR_QUOTE_CORE);
//        QueryResponse resp = null;
//        try {
//            resp = server.query(this.getRFQSolrQuery(fp, solrQuery.toString()), SolrRequest.METHOD.POST);
//        } catch (final SolrServerException | IOException e) {
//            e.printStackTrace();
//        }
        Page<RequestForQuoteSolrDoc> requestForQuoteSolrDocs = rfqSolrComponent.getList(fp, solrQuery.toString());

        final int totalCount = (int) requestForQuoteSolrDocs.getTotalElements();
        final ArrayList<RFQData> resultList = new ArrayList<>();

        for (final RequestForQuoteSolrDoc relevantDoc : requestForQuoteSolrDocs.getContent()) {
            final RFQData rfqData = new RFQData();
            if (relevantDoc.getRfqId() != null) {
                final Integer rfqId = relevantDoc.getRfqId();
                rfqData.setObjectID(rfqId);
                rfqData.setEditable(!this.rfqManager.isSupplierBidApplied(rfqId));
                final EdsRFQ rfq = this.rfqManager.get(rfqId);
                if (rfq != null) {
                    final RFQData data = rfq.createRFQData(false);
                    if (data.getOpportunityID() != null) {
                        final EdsOpportunity opportunity = this.opportunityManager.get(rfq.getOpportunityID());
                        if (opportunity != null) {
                            rfqData.setOppportunityNumber(opportunity.getNumber());
                            if (opportunity.getCrmAccount() != null) {
                                rfqData.setOpportunityName(opportunity.getCrmAccount().getName());
                            }
                        }
                    }
                    rfqData.setNumber(relevantDoc.getRfqNumber());
                    rfqData.setDate(new DateNonConvertable(relevantDoc.getRfqDate()));
                    rfqData.setValidUntil(new DateNonConvertable(relevantDoc.getDueDate()));
                    if (relevantDoc.getClientId() != null && relevantDoc.getClientName() != null) {
                        rfqData.setCustomer(new SelectItem(relevantDoc.getClientId(), relevantDoc.getClientName()));
                        if (data.getClientAddress() != null) {
                            rfqData.setClientAddress(data.getClientAddress());
                        }
                    }
                    if (relevantDoc.getRelatedProjectId() != null && relevantDoc.getRelatedProjectName() != null) {
                        rfqData.setProject(new SelectItem(relevantDoc.getRelatedProjectId(), relevantDoc.getRelatedProjectName()));
                    }
                    if (relevantDoc.getCurrentApproverId() != null && relevantDoc.getCurrentApproverName() != null) {
                        rfqData.setApprover(new SelectItem(relevantDoc.getCurrentApproverId(), relevantDoc.getCurrentApproverName()));
                    }
                    if (relevantDoc.getStatusId() != null && relevantDoc.getStatusCode() != null) {
                        final ReferenceItem overallStatus = new ReferenceItem();
                        overallStatus.setId(relevantDoc.getStatusId());
                        overallStatus.setCode(relevantDoc.getStatusCode());
                        rfqData.setOverallStatus(overallStatus);
                    }

                    if (panelTools != null) {
                        rfqData.setCustomFields(CustomFieldsUtils.getBaseSolrDocDynamicFields(relevantDoc, panelTools.getColumnCodeName()));
                    }

                    resultList.add(rfqData);
                }
            }
        }

        return new ListResult<>(resultList, totalCount);
    }

    @Override
    public void deleteRFQ(final Integer rfqID) {
        final EdsRFQ edsRFQ = this.rfqManager.get(rfqID);
        edsRFQ.setDeleted(true);
        this.rfqManager.update(edsRFQ);
        this.rfqRfpNoteManager.deleteRfqRfpNotes(rfqID, true);

        try {
            this.solrManager.removeRFQSolr(edsRFQ.getObjectID(), SecurityContext.getCompanyID());
        } catch (final IOException | SolrServerException e) {
            e.printStackTrace();
        }

        this.baseEventPostProcessor.registerEvent(RequestForQuoteEventListenerimpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, edsRFQ, this.userManager.getUser());
        final KpiLog kpiLog = getInstance().getKpiLog();
        kpiLog.setEntityName(EdsManualJournal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(edsRFQ.getObjectID());
        ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Delete request for quote");
    }

    @Override
    public void saveRfqCellValue(final RFQData rowValue, final String columnCodeName) {
        final EdsRFQ edsRFQ = this.rfqManager.get(rowValue.getObjectID());
        try {
            EdsRFQCustomFields edsRFQCustomFields = edsRFQ.getCustomFields();
            if (edsRFQCustomFields == null) {
                edsRFQCustomFields = new EdsRFQCustomFields();
                this.rfqcfManager.create(edsRFQCustomFields);
                edsRFQ.setCustomFields(edsRFQCustomFields);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(edsRFQCustomFields, rowValue.getCustomFields(), columnCodeName);

            //Register event in MyUpdate
            final KpiLog kpiLog = getInstance().getKpiLog();
            this.baseEventPostProcessor.registerEvent(RequestForQuoteEventListenerimpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsRFQ, this.userManager.getUser());

            kpiLog.setEntityName(EdsRFQ.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(edsRFQ.getObjectID());
            ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Update request for quote");

        } catch (final Exception e) {
            QuoteServiceImpl.log.error("Request For Quote List Edit Cell Column Code :" + columnCodeName, e);
        }
    }

    @Override
    public SelectItem getSupplier(final Integer supplierId) {
        final EdsCrmAccount supplier = this.crmAccountManager.get(supplierId);
        final SelectItem selectItem = new SelectItem();
        selectItem.setId(supplierId);
        selectItem.setName(supplier.getName());
        return selectItem;
    }

    @Override
    public void saveRFQSupplierBids(final RFQSupplierBid[] bids) {
        EdsRFQ edsRFQ = null;
        for (final RFQSupplierBid bid : bids) {
            final EdsCrmAccount supplier = this.crmAccountManager.get(bid.getSupplier().getId());
            EdsRFQSupplierBid supplierBid = this.rfqSupplierBidManager.getSupplierBidByItem(bid.getRfqItemID(), supplier.getObjectID());
            if (supplierBid == null) {
                supplierBid = new EdsRFQSupplierBid();
                supplierBid.setRfqItem(this.rfqItemManager.get(bid.getRfqItemID()));
                supplierBid.setSupplier(supplier);
            }
            if (edsRFQ == null) {
                edsRFQ = supplierBid.getRfqItem().getRfq();
            }
            supplierBid.setAmount(bid.getAmount());
            this.rfqSupplierBidManager.createOrUpdate(supplierBid);
        }
        this.sendSupplierBiddedMessage(edsRFQ);
    }

    private void sendSupplierBiddedMessage(final EdsRFQ edsRFQ) {
        if (edsRFQ == null) {
            return;
        }
        try {
            final String subject = "Supplier Bid for purchase request " + edsRFQ.getNumber();
            final EdsCrmAccount clientContact = edsRFQ.getItems().get(0).getSupplier();
            final Map<String, Object> values = new TreeMap<>();
            values.put("COMPANY_NAME", edsRFQ.getCreator().getCompany().getName());
            values.put("SUPPLIER_NAME", clientContact.getName());
            values.put("REQUEST_NUMBER", edsRFQ.getNumber());
            values.put("HOST", EdsContextParams.getHost(edsRFQ.getCreator().getCompany().getObjectID()));
            values.put("link", EncryptionHelper.encryptURL("requestforquote|summary/" + edsRFQ.getObjectID()) + "&" + Constants.U_ID + "=" + EncryptionHelper.encryptURL(edsRFQ.getCreator().getObjectID().toString()) + Constants.C_ID + "=" + EncryptionHelper.encryptURL(edsRFQ.getCreator().getCompany().getObjectID().toString()));

            final String text = EdsTemplates.processTemplate(values, EdsTemplates.RFQ_SUPPLIER_BID_MESSAGE);
            this.messageManager.sendMessageFromUser(null, edsRFQ.getCreator().getEmail(), null, null, subject, text, false, null, null, false, null, null, this.rfpItemManager.getUser());
        } catch (final EdsTemplateException | EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer convertRFQToPurchaseOrder(final RFQData rfqData) {
        final EdsRFQ edsRFQ = this.rfqManager.get(rfqData.getObjectID());
        edsRFQ.setOverallStatus(this.referenceManager.findReference(Constants.RFQ_STATUS, rfqData.getStatusCode()));

        final LinkedHashMap<Integer, List<EdsRFQItem>> itemsGroupedBySupplier = new LinkedHashMap<>();
        final List<RFQItem> rfqItems = rfqData.getItems();
        for (final RFQItem ri : rfqItems) {
            final EdsRFQItem edsRFQItem = this.rfqItemManager.get(ri.getObjectID());
            if (!edsRFQItem.isConverted()) {
                edsRFQItem.setUnitCost(ri.getUnitCost());
                this.rfqItemManager.update(edsRFQItem);
                if (itemsGroupedBySupplier.containsKey(ri.getSupplier().getId())) {
                    itemsGroupedBySupplier.get(ri.getSupplier().getId()).add(edsRFQItem);
                } else {
                    final List<EdsRFQItem> items = new LinkedList<>();
                    items.add(edsRFQItem);
                    itemsGroupedBySupplier.put(ri.getSupplier().getId(), items);
                }
            }
        }

        final Integer calculationScale = this.financialSettingsManager.getFinancialSettings().getAccountingCalculationScale();

        final Set<Integer> supplierIDs = itemsGroupedBySupplier.keySet();

        Integer lastGeneratedNumber = null;
        for (final Integer supplierID : supplierIDs) {
            final EdsCrmAccount supplier = this.crmAccountManager.get(supplierID);

            final EdsPurchaseOrder purchaseOrder = new EdsPurchaseOrder();
            lastGeneratedNumber = this.generateAndSetPONumber(purchaseOrder, (lastGeneratedNumber != null ? (lastGeneratedNumber + 1) : null));
            purchaseOrder.setSupplier(supplier);
            purchaseOrder.setInvoiceDate(edsRFQ.getDate());
            purchaseOrder.setDueDate(edsRFQ.getValidUntil());
            purchaseOrder.setCurrency(this.invoiceCircularResolver.returnBaseCurrency(null));
            purchaseOrder.setExchangeRate(BigDecimal.ONE);

            BigDecimal totalAmount = BigDecimal.ZERO;
            final List<EdsRFQItem> supplierRFQItemsList = itemsGroupedBySupplier.get(supplierID);
            int sorder = 0;
            final List<EdsQuoteItem> orderItems = new LinkedList<>();
            for (final EdsRFQItem edsRFQItem : supplierRFQItemsList) {
                final EdsQuoteItem poItem = new EdsQuoteItem();
                poItem.setQuote(purchaseOrder);
                poItem.setItem(edsRFQItem.getProduct());
                poItem.setDescription(edsRFQItem.getDescription());
                poItem.setQty(edsRFQItem.getQty());
                poItem.setUnitPrice(edsRFQItem.getUnitCost());
                poItem.setAccount(this.accountingManager.getAccountByCode("5000"));
                poItem.setNet(edsRFQItem.getQty().multiply(edsRFQItem.getUnitCost()).setScale(calculationScale, RoundingMode.HALF_UP));
                poItem.setAmmount(poItem.getNet());
                poItem.setSorder(sorder++);
                orderItems.add(poItem);
                totalAmount = totalAmount.add(poItem.getNet());
            }
            purchaseOrder.setQuoteItems(orderItems);

            purchaseOrder.setSubtotal(totalAmount);
            purchaseOrder.setDiscount(BigDecimal.ZERO);
            purchaseOrder.setTotalTaxes(BigDecimal.ZERO);
            purchaseOrder.setTotalInInvoiceCurrency(totalAmount);
            purchaseOrder.setTotal(totalAmount);

            purchaseOrder.setType(Constants.PAYABLE);
            purchaseOrder.setStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.DRAFT));
            this.quoteManager.create(purchaseOrder);

            for (final EdsRFQItem ri : supplierRFQItemsList) {
                ri.setPurchaseOrder(purchaseOrder);
                this.rfqItemManager.update(ri);
            }

            this.addPurchaseOrderToSolr(purchaseOrder);
        }

        return null;
    }

    private Integer generateAndSetPONumber(final EdsPurchaseOrder purchaseOrder, final Integer startNumber) {
        final InvoiceNumberData numberData = this.getOrderNumber();
        Integer fourDigitNumber = Integer.parseInt(numberData.getFourDigitNumber());
        if (startNumber != null) {
            fourDigitNumber = startNumber;
            numberData.setFourDigitNumber(startNumber.toString());
        }
        final DecimalFormat format = new DecimalFormat("0000");
        while (this.isPurchaseOrderNumberExists(numberData.getInvoiceNumber())) {
            System.out.println("Purchase Order with number " + numberData.getInvoiceNumber() + " already exists");
            fourDigitNumber = fourDigitNumber + 1;
            numberData.setFourDigitNumber(format.format(fourDigitNumber));
        }
        purchaseOrder.setNumber(numberData.getInvoiceNumber());
        purchaseOrder.setFourDigitNumber(fourDigitNumber);
        return fourDigitNumber;
    }

    private boolean isPurchaseOrderNumberExists(final String number) {
        final List<EdsPurchaseOrder> existingOrders = this.quoteManager.getPurchaseOrderByNumberGlobal(number);
        return existingOrders != null && existingOrders.size() > 0;
    }

    @Override
    public RFQItem getProductPreferredSupplier(final Integer productID) {
        if (productID != null) {
            final EdsItem product = this.itemManager.get(productID);
            if (product != null) {
                final RFQItem rfqItem = new RFQItem();
                final ArrayList<SelectItem> list = new ArrayList<>();
                if (!product.getSuppliers().isEmpty()) {
                    for (final EdsCrmAccount supplier : product.getSuppliers()) {
                        list.add(supplier.getAsSelectItem());
                    }
                }
                rfqItem.setSuppliers(list.toArray(new SelectItem[]{}));

                if (product.getUnitMeasurement() != null) {
                    rfqItem.setMeasurement(product.getUnitMeasurement().getAsSelectItem());
                }

                return rfqItem;
            }
        }
        return null;
    }

    @Override
    public void updateSaleQuoteByQB(final NewInvoice newQuote, final int synchItemId) {
        final EdsSaleQuote saleQuote = this.quoteManager.getSalesQuoteByCode(newQuote.getInvoiceNumber());
        if (saleQuote != null) {
            saleQuote.setQuickbookInvoiceID(newQuote.getQuickbookInvoiceID());
            saleQuote.setQuickbookEditSequence(newQuote.getQuickbookEditSequence());
            if (saleQuote.getQuoteItems() != null && saleQuote.getQuoteItems().size() > 0) {
                int i = 0;
                for (final EdsQuoteItem quoteItem : saleQuote.getQuoteItems()) {
                    quoteItem.setQuickbookItemID(newQuote.getItems()[i].getQbItemId());
                    i++;
                }
            }

            this.quoteManager.update(saleQuote);
        }
    }

    @Override
    public ListResult<RFPData> getRFPList(final ListingFilterParameter fp) {

        final List<EdsRFP> result = this.rfpManager.getEdsRFPList(fp, false);
        final Integer total = this.rfpManager.getEdsRFPList(fp, true).size();

        final EdsNumberingSettings settings = this.numberingSettingsManager.getNumberingSetting();
        final String rfpNumberingFormat = settings != null ? settings.getRfpNumberingFormat() : null;

        final EdsUser user = this.userManager.getUser();
        final ListPanelToolRpc panelTools = fp.getListPanelTool();
        final ArrayList<RFPData> list = new ArrayList<>();
        for (final EdsRFP rfp : result) {
            final RFPData rfpData = rfp.createRFPdata();
            rfpData.setIsEmployee(user.getObjectID().equals(rfp.getCreator().getObjectID()));
            rfpData.setNumberData(new NumberData(rfp.getNumber(), rfp.getIntNumber()));
            rfpData.getNumberData().setNumberFormat(rfpNumberingFormat);
            rfpData.setCustomer(rfp.getClient() != null ? rfp.getClient().getAsSelectItem() : null);
            if (rfp.getCurrentApprover() != null) {
                final EdsUser approver = rfp.getCurrentApprover().getExactEmployee();
                if (approver != null) {
                    rfpData.setCurrentApprover(new SelectItem(approver.getObjectID(), approver.getName()));
                    rfpData.setIsCurrentApprover(user.getObjectID().equals(approver.getObjectID()));
                }
                if (rfp.getCustomFields() != null) {
                    rfpData.setCustomFields(CustomFieldsUtils.getRPCCustomFields(rfp.getCustomFields(), panelTools.getColumnCodeName() != null ? panelTools.getColumnCodeName() : null));
                }
            }
            list.add(rfpData);
        }
        return new ListResult<>(list, total);
    }

    @Override
    public void deleteRFP(final Integer rfpID) {
        final EdsRFP edsRFP = this.rfpManager.get(rfpID);
        edsRFP.setDeleted(true);
        //delete rfp items
        this.rfpManager.deleteRFPItems(edsRFP.getObjectID());
        this.rfpManager.update(edsRFP);
        this.rfqRfpNoteManager.deleteRfqRfpNotes(rfpID, false);

        this.baseEventPostProcessor.registerEvent(RequestForPurchaseEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, edsRFP, this.userManager.getUser());
        final KpiLog kpiLog = getInstance().getKpiLog();
        kpiLog.setEntityName(EdsManualJournal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(edsRFP.getObjectID());
        ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Delete request for purchase");
    }

    @Override
    public RFPData getRFPData(final RFPData filter) {
        final RFPData item;
        final EdsUser user = this.userManager.getUser();
        final ArrayList<RFPItem> items = new ArrayList<>();
        if (filter.getObjectID() != null) {
            if (filter.isCopyFromQuote()) {
                final EdsSaleQuote quote = (EdsSaleQuote) this.quoteManager.get(filter.getObjectID());
                item = new RFPData();
                item.setCopy(true);
                item.setNumberData(this.generateRfpNumber());
                item.setCreator(quote.getCreator() != null ? quote.getCreator().getAsSelectItem() : user.getAsSelectItem());
                item.setIsEmployee(true);
                item.setIsCurrentApprover(false);
                item.setDueDate(quote.getDueDate());
                if (quote.getRelatedProject() != null) {
                    item.setRelatedProject(quote.getRelatedProject().getAsSelectItem());
                }
                if (quote.getClientOrSupplier() != null) {
                    item.setCustomer(quote.getClientOrSupplier().getAsSelectItem());
                }

                for (final EdsQuoteItem quoteItem : quote.getQuoteItems()) {
                    if (quoteItem.getItem() != null) {
                        final RFPItem rfpItem = new RFPItem();
                        rfpItem.setProductItem(quoteItem.getItem().getAsProductSelectItem());
                        rfpItem.setDescription(quoteItem.getDescription());
                        if (quoteItem.getUnitMeasurement() != null) {
                            rfpItem.setMeasurement(quoteItem.getUnitMeasurement().getAsSelectItem());
                        }
                        rfpItem.setQty(quoteItem.getQty());
                        rfpItem.setWareHouse(quoteItem.getWarehouse() != null ? quoteItem.getWarehouse().getAsSelectItem() : null);
                        rfpItem.setQtyOnhand(quoteItem.getItem().getItemsInStock());
                        rfpItem.setSelected(true);
                        items.add(rfpItem);
                    }
                }
                item.setItems(items);
                item.setCustomFieldList(CustomFieldsUtils.setRPCCustomFieldItems(null, this.commonService.getCompanyCustomFields(ViewName.RequestForPurchase)));
            } else if (filter.isFromBillOfMaterials() && filter.getProjectID() != null) {
                final EdsProject project = this.projectManager.get(filter.getProjectID());
                item = new RFPData();
                item.setRelatedProject(project != null ? project.getAsSelectItem() : null);
                item.setNumberData(this.generateRfpNumber());
                item.setCreator(user.getAsSelectItem());
                item.setIsEmployee(true);
                item.setIsCurrentApprover(false);
                item.setDueDate(project != null ? project.getDueDate() : new Date());
                if (project != null && project.getClient() != null) {
                    item.setCustomer(project.getClient().getAsSelectItem());
                }
                final String key = CacheConstants.REQUESTED_BILL_OF_MATERIALS + "_" + user.getCompany().getObjectID() + "_" + filter.getProjectID() + "_" + user.getObjectID();
//                Map<Integer, BigDecimal> map = ApplicationCache.getInstance().getMap(key + CacheConstants.REQUESTED_BILL_OF_MATERIALS);

                final Map<Integer, BigDecimal> map = RedisClient.getKey(key, new TypeToken<Map<Integer, BigDecimal>>() {
                }.getType());

                if (project != null) {
                    project.getBillOfMaterials().forEach(x -> {

                        if (map.containsKey(x.getObjectID())) {
                            final RFPItem rfpItem = new RFPItem();
                            rfpItem.setProductItem(x.getItem().getAsProductSelectItem());
                            rfpItem.setDescription(x.getDescription());
                            if (x.getUnitMeasurement() != null) {
                                rfpItem.setMeasurement(x.getUnitMeasurement().getAsSelectItem());
                            }
                            rfpItem.setQty(map.get(x.getObjectID()));
                            rfpItem.setQtyOnhand(x.getItem().getItemsInStock());
                            rfpItem.setSelected(true);
                            rfpItem.setEntityID(x.getObjectID());
                            items.add(rfpItem);
                        }
                    });
                }
                item.setItems(items);
                item.setCustomFieldList(CustomFieldsUtils.setRPCCustomFieldItems(null, this.commonService.getCompanyCustomFields(ViewName.RequestForPurchase)));
            } else {
                final EdsRFP edsRFP = this.rfpManager.get(filter.getObjectID());
                edsRFP.setItemCustomFields(this.commonService.getCompanyCustomFields(ViewName.RFPItem));
                item = edsRFP.createRFPdata();

                if (filter.isCopy()) {
                    item.setNumberData(this.generateRfpNumber());
                    item.setObjectID(null);
                    item.setCreator(user.getAsSelectItem());
                } else {
                    item.setNumberData(this.generateRfpNumber());
                    item.getNumberData().setNumberString(edsRFP.getNumber());
                    item.getNumberData().setIntNumber(edsRFP.getIntNumber());
                }
                item.setIsEmployee(user.getObjectID().equals(edsRFP.getCreator().getObjectID()));
                if (edsRFP.getCurrentApprover() != null) {
                    final EdsUser currentApprover = edsRFP.getCurrentApprover().getExactEmployee();
                    if (currentApprover != null) {
                        item.setCurrentApprover(new SelectItem(currentApprover.getObjectID(), currentApprover.getName()));
                        item.setIsCurrentApprover(user.getObjectID().equals(currentApprover.getObjectID()));
                    }

                    final List<EdsApprover> rfpApprovers = edsRFP.getApprovers();
                    if (rfpApprovers != null && rfpApprovers.size() > 0) {
                        final ArrayList<SelectItem> managers = new ArrayList<>();
                        for (final EdsApprover approver : rfpApprovers) {
                            final EdsUser manager = approver.getExactEmployee();
                            if (manager != null) {
                                managers.add(new SelectItem(manager.getObjectID(), manager.getName()));
                            }
                        }
                        item.setManagers(managers);
                    }
                }
                item.setApproverSaved(this.approverManager.isExistApproverByEntityTypeAndEntityId(RelationItem.REQUEST_FOR_PURCHASE, edsRFP.getObjectID()));

                final List<EdsRFPItem> edsRFPItems = edsRFP.getItems();
                for (final EdsRFPItem edsRFPItem : edsRFPItems) {
                    items.add(edsRFPItem.createRFPItem(filter.isCopy(), edsRFP.getItemCustomFields()));
                }
                item.setItems(items);
                if (edsRFP.getOverallStatus() != null && Constants.REJECT.equals(edsRFP.getOverallStatus().getCode())) {
                    item.setRejectionReason(edsRFP.getRejectionReason());
                }
                item.setCustomFieldList(CustomFieldsUtils.setRPCCustomFieldItems(edsRFP != null ? edsRFP.getCustomFields() : null, this.commonService.getCompanyCustomFields(ViewName.RequestForPurchase)));

                if (edsRFP != null) {
                    item.setRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(RelationItem.REQUEST_FOR_PURCHASE, edsRFP.getObjectID())));
                }
            }
            item.setHistoryList(this.getRFPNotes(filter.getObjectID()).toArray(new HistoryListItem[]{}));
            item.setTemplates(this.invoiceServiceLocal.getCompanyPdfTemplates(AccountingConstants.RFP).getItems());
            final EdsCompanyPdfTemplate template = this.companyPdfTemplateManager.getDefaultCompanyPdfTemplateByType(AccountingConstants.RFP);
            if (template != null) {
                item.setSelectedTemplateId(template.getObjectID());
            }
        } else {
            item = new RFPData();
            item.setCreator(user.getAsSelectItem());
            item.setNumberData(this.generateRfpNumber());
            item.setIsEmployee(true);
            item.setIsCurrentApprover(false);
            item.setCustomFieldList(CustomFieldsUtils.setRPCCustomFieldItems(null, this.commonService.getCompanyCustomFields(ViewName.RequestForPurchase)));
        }
        if (filter.getProjectID() != null && filter.isFromProject()) {
            final EdsProject project = this.projectManager.get(filter.getProjectID());
            if (project != null) {
                item.setRelatedProject(project != null ? project.getAsSelectItem() : null);
            }
        }

        item.setItemCustomFields(this.commonService.getCompanyCustomFields(ViewName.RFPItem));
        item.setCustomItemColumns(filter != null && filter.isView() ? this.itemTableSettingsServiceLocal.getColumnConfigs(ItemTableEnum.RFP_ITEM, false, true) : this.itemTableSettingService.getColumnConfigs(ItemTableEnum.RFP_ITEM));

        return item;
    }

    @Override
    public NumberData generateRfpNumber() {
        final EdsNumberingSettings settings = this.numberingSettingsManager.getNumberingSetting();
        final Integer intNumber = this.rfpManager.getRfpLastIntNumber();

        if (settings != null && settings.getRfpNumberingFormat() != null) {
            return settings.parseNumberData(intNumber, settings.getRfpNumberingFormat());
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_RFP_PREFIX);
        }
    }

    @Override
    public ProductItem[] getRFPItemsForStockAdjustment(final ArrayList<Integer> ids) {
        List<ProductItem> result = new ArrayList<>();

        if (ids == null) {
            return result.toArray(new ProductItem[]{});
        }
        for (final Integer id : ids) {
            EdsRFP rfp = this.rfpManager.get(id);

            if (rfp == null) {
                continue;
            }
            for (final EdsRFPItem item : rfp.getItems()) {
                if (!item.getSelected() || item.getProduct() == null) {
                    continue;
                }
                ProductItem productItem = new ProductItem();

                productItem.setObjectId(item.getProduct().getObjectID());
                productItem.setName(item.getProduct().getName());
                productItem.setDescription(item.getDescription());
                productItem.setUsedQty(item.getQty());
                productItem.setNewQty(BigDecimal.ZERO);
                productItem.setCurrentQty(item.getProduct().getItemsInStock());
                productItem.setItemsInStock(item.getProduct().getItemsInStock());
                productItem.setTotalQty(productItem.getCurrentQty().subtract(item.getQty()));
                if (rfp.getProject() != null) {
                    productItem.setProjectID(rfp.getProject().getObjectID());
                    productItem.setProjectName(rfp.getProject().getName());
                }
                EdsWarehouse warehouse = item.getWarehouse();

                if (warehouse != null) {
                    productItem.setWarehouseId(warehouse.getObjectID());
                    productItem.setWarehouseName(warehouse.getName());
                }
                result.add(productItem);
            }
        }
        return result.toArray(new ProductItem[]{});
    }

    @Override
    @Transactional
    public String saveRFPData(final RFPData rfpData) throws NumberExistingException {
        NumberData numberData = rfpData.getNumberData();
        if (rfpData.getObjectID() == null && (numberData == null || numberData.getNumberString() == null || "".equals(numberData.getNumberString().trim()))) {
            throw new NumberExistingException("Incorrect request for purchase number format.");
        }
        final EdsUser user = this.userManager.getUser();
        final EdsRFP edsRFP;
        if (rfpData.getObjectID() == null) {
            edsRFP = new EdsRFP();
            edsRFP.setCreator(this.userManager.getUser());
            edsRFP.setCreatedDate(new Date());

        } else {
            edsRFP = this.rfpManager.get(rfpData.getObjectID());
            this.rfpManager.deleteRFPItems(rfpData.getObjectID());
        }
        edsRFP.setUpdatedDate(new Date());
        edsRFP.setDueDate(rfpData.getDueDate());
        if (Constants.REJECT.equals(rfpData.getStatus())) {
            edsRFP.setRejectionReason(rfpData.getRejectionReason());
        }

        NumberData newNumberData = numberData;
        if (numberData.getNumberString() == null || numberData.getNumberString().isEmpty() || this.rfpManager.isRFPNumberExist(rfpData.getNumberData().getNumberString(), rfpData.getObjectID())) {
            newNumberData = this.generateRfpNumber();
        }

        if (newNumberData != null) {
            edsRFP.setNumber(newNumberData.getNumberString());
            edsRFP.setIntNumber(newNumberData.getIntNumber());
        }

        if (rfpData.getRelatedProject() != null) {
            edsRFP.setProject(this.projectManager.get(rfpData.getRelatedProject().getId()));
        }
        if (rfpData.getCustomer() != null) {
            edsRFP.setClient(this.crmAccountManager.get(rfpData.getCustomer().getId()));
        }


        boolean limitExceeded = false;
        if (rfpData.isFromBillOfMaterials()) {

            final List<Integer> ids = rfpData.getItems().stream()
                    .map(RFPItem::getEntityID)
                    .collect(Collectors.toList());

            final Map<Integer, BigDecimal> requestedQtys = this.rfpManager.getRemainingQtys(ids);
            if (!requestedQtys.isEmpty()) {
                for (final RFPItem item : rfpData.getItems()) {
                    final BigDecimal remaining = requestedQtys.get(item.getEntityID());
                    if (remaining != null && remaining.compareTo(item.getQty()) < 0) {
                        limitExceeded = true;
                        break;
                    }
                }
            }
        }
        if (limitExceeded) {
            return Constants.FALSE;
        }

        final List<EdsRFPItem> rfpItems = new LinkedList<>();
        edsRFP.setCustomFields(this.saveRFPCustomFields(edsRFP.getCustomFields(), rfpData.getCustomFieldList()));

        for (final RFPItem item : rfpData.getItems()) {
            if (item.getProductItem() == null) {
                continue;
            }
            EdsRFPItem edsRFPItem = new EdsRFPItem();


            // temporary solution
            if (item.getProductItem() != null && item.getProductItem().getId() != null &&
                    !"Type here to search...".equals(item.getProductItem().getName())) {
                final EdsItem product = this.itemManager.getItem(item.getProductItem().getId());
                if (product != null && (product.getProductNumber() + " -> " + product.getName()).equals(item.getProductItem().getName())) {
                    edsRFPItem.setProduct(this.itemManager.get(item.getProductItem().getId()));
                    edsRFPItem.setItemName(null);
                    edsRFPItem.setHasProductList(true);
                } else {
                    edsRFPItem.setItemName(item.getProductItem().getName());
                    edsRFPItem.setProduct(null);
                    edsRFPItem.setHasProductList(false);
                }
            } else {
                if (item.getProductItem() != null && item.getProductItem().getName() != null
                        && !"Type here to search...".equals(item.getProductItem().getName())) {
                    edsRFPItem.setItemName(item.getProductItem().getName());
                    edsRFPItem.setProduct(null);
                    edsRFPItem.setHasProductList(false);
                }
            }

            edsRFPItem.setDescription(item.getDescription());
            if (item.getMeasurement() != null && item.getMeasurement().getId() != null) {
                edsRFPItem.setMeasurement(this.unitMeasurementManager.get(item.getMeasurement().getId()));
            }
            if (item.getDepartmentItem() != null && item.getDepartmentItem().getId() != null) {
                edsRFPItem.setDepartment(this.departmentManager.get(item.getDepartmentItem().getId()));
            }
            edsRFPItem.setQty(item.getQty());
            edsRFPItem.setEntityID(item.getEntityID());
            if (item.getWareHouse() != null) {
                edsRFPItem.setWarehouse(this.warehouseManager.get(item.getWareHouse().getId()));
            }
            if (item.getItemCustomFields() != null && !item.getItemCustomFields().isEmpty()) {
                edsRFPItem.setCustomFields(this.createRFPItemCustomFields(item.getItemCustomFields()));
            }
            rfpItems.add(edsRFPItem);
        }
        edsRFP.setItems(rfpItems);
        if (!Constants.DRAFT.equals(rfpData.getStatus()) && edsRFP.getItems().isEmpty()) {
            throw new RuntimeException("Please, fill items");
        }
        if (edsRFP.getObjectID() == null) {
            this.rfpManager.create(edsRFP);
        } else {
            this.rfpManager.update(edsRFP);
        }
        if (rfpData.getAttachments() != null && rfpData.getAttachments().length > 0) {
            this.attachmentUtilsManager.saveAttachments(Constants.F_RFP, edsRFP.getObjectID(), edsRFP.getObjectID(), rfpData.getAttachments());
        }

        if (rfpData.getRelations() != null && rfpData.getRelations().size() > 0) {
            this.allInOneServiceLocal.saveRelations(RelationItem.REQUEST_FOR_PURCHASE, edsRFP.getObjectID(), edsRFP.getNumber(), rfpData.getRelations());
        }
        //Register event in MyUpdate
        final KpiLog kpiLog = getInstance().getKpiLog();
        if (rfpData.getObjectID() != null) {
            this.baseEventPostProcessor.registerEvent(RequestForPurchaseEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsRFP, user);

            kpiLog.setEntityName(EdsRFP.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(edsRFP.getObjectID());
            ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Update request for purchase");
        } else {
            this.baseEventPostProcessor.registerEvent(RequestForPurchaseEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsRFP, user);
            kpiLog.setEntityName(EdsRFP.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            kpiLog.setEntityId(edsRFP.getObjectID());
            ServerUtils.kpiLog(QuoteServiceImpl.log, kpiLog, "Add request for purchase");
        }
        this.createRfpNote(rfpData, edsRFP);

        if (!isOk(rfpData.getApprovers())) {
            edsRFP.setEntityStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, rfpData.getStatus()));
        }
        if (isOk(rfpData.getApprovers())) {
            this.saveApproversForEdit(rfpData, edsRFP);
        }

        this.rfpManager.update(edsRFP);

        if (!rfpData.getStatus().equals(Constants.DRAFT)) {
            final EdsBusinessEvent workflowEvent2 = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsRFP, user);
            workflowEvent2.setEntityType(RelationItem.REQUEST_FOR_PURCHASE);
            this.changeRFPstatus(edsRFP.getObjectID(), rfpData.getStatus(), rfpData.getRejectionReason(), false);
        }


        return edsRFP.getStatus().getCode();
    }

    private void saveApproversForEdit(final RFPData rfpData, final EdsRFP edsRFP) {

        rfpData.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
        boolean isFirstApprover = true;
        for (final ApproverItemMini approverItem : rfpData.getApprovers()) {
            final EdsApprover _edsApprover = this.approverManager.get(approverItem.getClonedFrom());
            if (approverItem.getObjectID() != null) {
                if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                    final EdsUser user_ = this.userManager.get(approverItem.getExactEmployee().getId());
                    _edsApprover.setExactEmployee(user_);
                }
                this.approverManager.update(_edsApprover);
                if (edsRFP.getCurrentApprover() != null && rfpData.getStatus() != null && isFirstApprover) {
                    edsRFP.getCurrentApprover().setStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, rfpData.getStatus()));
                    edsRFP.setEntityStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
                    isFirstApprover = false;
                } else if (edsRFP.getCurrentApprover() != null && rfpData.getStatus() != null) {
                    edsRFP.getCurrentApprover().setStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
                }
                if (rfpData.getStatus() != null && !Constants.APPROVE.equals(rfpData.getStatus())) {
                    edsRFP.setEntityStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, rfpData.getStatus()));
                }
                if (edsRFP.isCurrentApproverRejected()) {
                    edsRFP.setEntityStatus(edsRFP.getCurrentApprover().getStatus());
                }
                continue;
            }
            final EdsApprover edsApprover = _edsApprover.cloneShallow();
            edsApprover.setObjectID(null);
            edsApprover.setApproverHistory(new HashSet<>());
            edsApprover.setEntityID(edsRFP.getObjectID());
            edsApprover.setIs_default(false);
            if (rfpData.getStatus() != null && isFirstApprover) {
                edsApprover.setStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, rfpData.getStatus()));
                if (Constants.DRAFT.equals(rfpData.getStatus())) {
                    edsRFP.setEntityStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, rfpData.getStatus()));
                } else {
                    edsRFP.setEntityStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
                }
                isFirstApprover = false;
            } else if (rfpData.getStatus() != null) {
                edsApprover.setStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
            }
            if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                final EdsUser user_ = this.userManager.get(approverItem.getExactEmployee().getId());
                edsApprover.setExactEmployee(user_);
            }
            edsApprover.setApproverRoles(new HashSet<>());
            edsApprover.setApproverEmployees(new HashSet<>());
            edsApprover.setDynamicQueries(new HashSet<>());
            this.approverManager.createOrUpdate(edsApprover);

            for (final EdsApproverRoles roleapp : _edsApprover.getApproverRoles()) {
                final EdsApproverRoles roles = new EdsApproverRoles();
                roles.setApproverId(edsApprover.getObjectID());
                roles.setRoleId(roleapp.getRoleId());
                roles.setApproveForAll(roleapp.getApproveForAll());
                edsApprover.getApproverRoles().add(roles);
            }
            for (final EdsApproverEmployees ucerapp : _edsApprover.getApproverEmployees()) {
                final EdsApproverEmployees employees = new EdsApproverEmployees();
                employees.setApproveForAll(ucerapp.getApproveForAll());
                employees.setEmployeeId(ucerapp.getEmployeeId());
                employees.setApproverId(edsApprover.getObjectID());
                edsApprover.getApproverEmployees().add(employees);
            }
            if (edsRFP.getCurrentApprover() == null) {
                edsRFP.setCurrentApprover(edsApprover);
            }
            edsRFP.getApprovers().add(edsApprover);
        }
    }

    @Override
    public void changeRFPstatus(final Integer id, final String statusCode, final String rejectionReason, final Boolean fromUi) {
        final EdsRFP edsRFP = this.rfpManager.get(id);
        if (edsRFP != null) {
            final EdsUser user = this.employeeManager.getUser();

            edsRFP.updateStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, statusCode));
            if (Constants.SUBMITTED_TO_MANAGER.equals(statusCode)
                    && edsRFP.getOverallStatus() != null
                    && Constants.DRAFT.equals(edsRFP.getOverallStatus().getCode())) {

                edsRFP.setOverallStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SUBMITTED_TO_MANAGER));
            }
            if (Constants.REJECT.equals(statusCode)) {
                edsRFP.setRejectionReason(rejectionReason);
                edsRFP.setOverallStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.REJECT));
                final EdsRfqRfpNote noteReject = new EdsRfqRfpNote();
                noteReject.setRfp(edsRFP);
                noteReject.setComment(rejectionReason);
                noteReject.setCommentator(user);
                noteReject.setDate(new Date());
                noteReject.setSuperUser(ServerUtils.isSuperUser());
                this.rfqRfpNoteManager.create(noteReject);
                this.baseEventPostProcessor.registerEvent(RequestForPurchaseEventListenerImpl.TYPE, RequestForPurchaseEventListenerImpl.EVENT_RFP_MANAGER_REJECT, edsRFP, this.userManager.getUser());
            }
            if (!Constants.DRAFT.equals(edsRFP.getOverallStatus().getCode()) && fromUi) {
                if (Constants.SUBMITTED_TO_MANAGER.equals(statusCode)) {
                    this.baseEventPostProcessor.registerEvent(RequestForPurchaseEventListenerImpl.TYPE, RequestForPurchaseEventListenerImpl.EVENT_RFP_SUBMITTED_TO_MANAGER, edsRFP, this.userManager.getUser());
                }
                if (Constants.APPROVE.equals(statusCode)) {
                    this.baseEventPostProcessor.registerEvent(RequestForPurchaseEventListenerImpl.TYPE, RequestForPurchaseEventListenerImpl.EVENT_RFP_MANAGER_APPROVE, edsRFP, this.userManager.getUser());
                }
            }


            edsRFP.setUpdatedDate(new Date());
            this.rfpManager.update(edsRFP);

            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), edsRFP, user);
            workflowEvent.setEntityType(RelationItem.REQUEST_FOR_PURCHASE);
        }
    }

    @Override
    public void sendRFPEmailRequest(final MessageItem messageItem) {
        this.messageManager.sendRFPEmailRequest(messageItem);
    }

    private void convertRFPstoPO(final List<Integer> ids) {
        if (ids == null) {
            return;
        }
        for (final Integer objectID : ids) {
            changeRFPstatus(objectID, Constants.CONVERTED, null, false);
        }
    }

    private List<EdsRFPItem> getRPFItemsForConverting(final List<Integer> rfpIDs) {
        final StringBuilder ids = new StringBuilder();
        for (int i = 0; i < rfpIDs.size(); i++) {
            ids.append(rfpIDs.get(i));
            if (i != rfpIDs.size() - 1) {
                ids.append(", ");
            }
        }
        return this.rfpItemManager.getRFPItemsByRFPIDs(ids.toString());
    }

    @Override
    public void closePurchaseOrderRemainingQty(final Integer purchaseOrderID) {
        final EdsPurchaseOrder purchaseOrder = this.quoteManager.getPurchaseOrderByID(purchaseOrderID);
        purchaseOrder.setStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.INVOICE_STATUS_CLOSED));
        this.quoteManager.update(purchaseOrder);
        this.addPurchaseOrderToSolr(purchaseOrder);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String[] validateItemsInStock(final QuantityItem[] qItems, final Integer quoteId, final DateNonConvertable
            startDate, final DateNonConvertable endDate) {
        final List<String> itemsOutOfStock = new LinkedList<>();
        String productName;

        for (final QuantityItem qItem : qItems) {
            if (qItem.getId() != null) {
                final EdsItem item = this.itemManager.get(qItem.getId());
                if (item != null && (AccountingConstants.INVENTORY_ITEM.equals(item.getType()) || AccountingConstants.ASSEMBLY_ITEM.equals(item.getType()))) {
                    BigDecimal itemQtyInStock = item.getQty();
                    BigDecimal reservedQty = BigDecimal.ZERO;
                    String reservedQuoteNumber = "";

                    final Object object = this.quoteManager.getQuotedItemCountByPeriod(startDate.getNonConvertedDate(), endDate.getNonConvertedDate(), qItem.getId(), quoteId);

                    if (object != null) {
                        final Object[] objects = (Object[]) object;
                        reservedQty = (BigDecimal) objects[0];
                        reservedQuoteNumber = (String) objects[1];
                    }
                    if (reservedQty != null) {
                        itemQtyInStock = itemQtyInStock.subtract(reservedQty);
                    }

                    productName = item.getName() + (reservedQuoteNumber != null && !reservedQuoteNumber.isEmpty() ? " - " + reservedQuoteNumber : "");

                    if (qItem.getQuantity().compareTo(itemQtyInStock) > 0) {
                        itemsOutOfStock.add(productName);
                    }
                }
            }
        }
        return itemsOutOfStock.toArray(new String[]{});
    }

    @Override
    public TestRPC updateRFQItem(final RFQItem item) {
        if (item.getObjectID() != null) {
            final EdsRFQItem rfqItem = this.rfqItemManager.get(item.getObjectID());
            if (item.getReMarks() != null) {
                rfqItem.setRemarks(item.getReMarks());
            }
            if (item.getUnitCost() != null) {
                rfqItem.setUnitCost(item.getUnitCost());
            }
            if (item.getCommission() != null) {
                rfqItem.setCommission(item.getCommission());
            }
            final TestRPC testRPC = new TestRPC();
            testRPC.setMessage("Successfully updated");
            return testRPC;
        } else {
            final TestRPC testRPC = new TestRPC();
            testRPC.setMessage("Object Id is not specified! Update is not committed");
            return testRPC;
        }
    }

    @Override
    public Boolean setSelectedRfpItems(final ArrayList<Integer> rfpItems, final Integer rfpId) {
        final List<EdsRFPItem> list = this.rfpItemManager.getRFPItemByRFPID(rfpId);
        for (final EdsRFPItem li : list) {
            li.setSelected(false);
            this.rfpItemManager.createOrUpdate(li);
        }
        if (rfpItems != null) {
            for (final Integer id : rfpItems) {
                final EdsRFPItem rfpItem = this.rfpItemManager.get(id);
                rfpItem.setSelected(true);
                this.rfpItemManager.createOrUpdate(rfpItem);
            }
        }
        return true;
    }

    @Override
    public ArrayList<CompanyCustomFieldItem> saveRFQCustomFields(final Integer
                                                                         objectID, final ArrayList<CompanyCustomFieldItem> customFields) {
        final EdsRFQ edsRfq = this.rfqManager.get(objectID);
        if (edsRfq != null && customFields != null) {
            edsRfq.setCustomFields(this.saveCustomFields(edsRfq.getCustomFields(), customFields));
            this.rfqManager.update(edsRfq);

            final ArrayList<CompanyCustomFieldItem> customFieldsItems = this.commonService.getCompanyCustomFields(ViewName.RequestForQuote);
            return CustomFieldsUtils.setRPCCustomFieldItems(edsRfq.getCustomFields(), customFieldsItems);
        }
        return customFields;
    }

    @Override
    public ListResult<ShippingData> getShippingDataList(final ListingFilterParameter fp) {
        if (fp == null) {
            return new ListResult<ShippingData>(Lists.newArrayListWithCapacity(0), 0);
        }
        Integer quoteId = fp.getEntityID();

        if (fp.isGdn()) {
            EdsPickList pickList = this.pickListManager.get(quoteId);

            if (pickList == null || pickList.getSaleQuote() == null) {
                return new ListResult<ShippingData>(Lists.newArrayListWithCapacity(0), 0);
            }
            quoteId = pickList.getSaleQuote().getObjectID();
        }
        fp.setEntityID(quoteId);
        Integer count = shippingDataManager.getListingCount(fp);
        List<ShippingData> result = Lists.newArrayListWithExpectedSize(fp.getLimit());

        if (count > 0) {
            List<EdsShippingData> list = shippingDataManager.getList(fp);

            for (final EdsShippingData edsShippingData : list) {
                final ShippingData shippingData = edsShippingData.toTO();
                final Integer invoiceId = this.shippingDataManager.getGrnGdnRelatedInvoiceNumber(shippingData.getId());
                if (invoiceId != null) {
                    final EdsInvoice invoice = this.invoiceManager.get(invoiceId);
                    final NewInvoice to = EdsInvoice.getInvoiceData(invoice);
                    shippingData.setInvoice(to);
                }
                result.add(shippingData);
            }
        }

        return new ListResult<ShippingData>(Lists.newArrayList(result), count);
    }

    @Override
    public ListResult<ShippingData> getShippingDataForListing(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        FacetFilterRpc shippingDataFacetFilter = fp.getFacetFilter();
        if (shippingDataFacetFilter != null && !shippingDataFacetFilter.isFilterChanges()) {
            shippingDataFacetFilter = this.commonServiceLocal.getUserFacetFilter(shippingDataFacetFilter);
        }

        if (shippingDataFacetFilter != null) {
            if (shippingDataFacetFilter.getSearchKey() != null && !"".equals(shippingDataFacetFilter.getSearchKey())) {
                fp.setSearchKey(shippingDataFacetFilter.getSearchKey());
            }
            fp.setFacetFilter(shippingDataFacetFilter);
        }

        if (fp.getStartDateNC() != null) {
            fp.setStartDate(ServerUtils.parseFilterParameterDate(fp.getStartDateNC()));
        }
        if (fp.getEndDateNC() != null) {
            fp.setEndDate(ServerUtils.parseFilterParameterDate(fp.getEndDateNC()));
        }

        final EdsUser edsUser = this.employeeManager.getUser();
        final StringBuilder solrQuery = new StringBuilder(this.getShippingDataSolrQuery(fp, edsUser));

        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(shippingDataFacetFilter, edsUser.getCompany(),
                SolrSaleInvoiceRepresenter.FIELD_CREATION_DATE, null, null));
        Page<ShippingDataSolrDoc> shippingDataSolrDocs = shippingDataSolrComponent.getList(fp, solrQuery.toString());

        // adding solr collapsed reusults to map
        final ListPanelToolRpc panelSettings = fp.getListPanelTool();

        int totalCount = (int) shippingDataSolrDocs.getTotalElements();

        // adding solr collapsed reusults to map
        final ArrayList<ShippingData> resultList = new ArrayList<>();

        for (ShippingDataSolrDoc relevantDoc : shippingDataSolrDocs.getContent()) {
            final ShippingData shippingData = new ShippingData();
            EdsShippingData edsShippingData = shippingDataManager.get(relevantDoc.getShippingDataId());
            if (edsShippingData != null) {
                if (edsShippingData.getCreator() != null) {
                    shippingData.setCreator(edsShippingData.getCreator().getAsSelectItem());
                }

                shippingData.setInvoiceNumber(relevantDoc.getInvoiceNumber());
                shippingData.setInvoiceStatus(relevantDoc.getStatusName());
                shippingData.setInvoiceId(relevantDoc.getSaleInvoiceId());
                shippingData.setId(relevantDoc.getShippingDataId());
                shippingData.setNumber(relevantDoc.getShippingDataNumber());
                shippingData.setStatus(edsShippingData.getStatus());
                shippingData.setShippingDate(new DateNonConvertable(relevantDoc.getShippingDate()));
                shippingData.setClientName(relevantDoc.getClientName());

                if (edsShippingData.getCrmAccount() != null && !fp.isFromExcelPDF()) {
                    shippingData.setCustomer(edsShippingData.getCrmAccount().getRPC(null, true));
                }
                if (edsShippingData.getCurrency() != null) {
                    shippingData.setCurrencyName(edsShippingData.getCurrency().getFullName());
                }
                shippingData.setCreatorName(relevantDoc.getCreatorName());
                shippingData.setOrderNumber(relevantDoc.getQuoteNumber());

                if (edsShippingData.getQuote() != null && edsShippingData.getQuote().getObjectID() != null) {
                    shippingData.setQuoteId(edsShippingData.getQuote().getObjectID());
                }
            }
            resultList.add(shippingData);
        }

        return new ListResult<>(resultList, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getShippingDataSolrQuery(final ListingFilterParameter filterParametrs, final EdsUser user) {
        final StringBuffer sql = new StringBuffer();
        sql.append(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID).append(":").append(user.getCompany().getObjectID());

        sql.append(" AND (");
        if (filterParametrs.isGdn()) {
            sql.append(SolrSaleInvoiceRepresenter.FIELD_IS_GDN).append(":TRUE)");
        } else {
            sql.append(SolrSaleInvoiceRepresenter.FIELD_IS_GDN).append(":FALSE)");
        }

        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null) {
            final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sql.append(" AND (((").append(SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATE).append(":[").append(format.format(filterParametrs.getStartDate())).append(" TO * ]) ");
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATE).append(":[ * TO ").append(format.format(filterParametrs.getEndDate())).append(" ])))");
        }

        // search key in composite
        if (filterParametrs.getCrmContactId() != null) {
            sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_ID).append(":").append(filterParametrs.getCrmContactId());
        }
        if (filterParametrs.getClientId() != null) {
            sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(filterParametrs.getClientId());
        }
        if (filterParametrs.getSupplierId() != null) {
            sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(filterParametrs.getSupplierId());
        }

        if (filterParametrs.getAccountID() != null) {
            sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(filterParametrs.getAccountID());
        }
        if (!user.hasRole(EdsRole.ADMIN_CODE)) {
            if (!filterParametrs.isGdn()) {
                EdsLocation location = user.getLocation();
                if (location != null && ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_GOODS_RECEIVED_NOTE_SEE_BY_LOCATION)) {
                    sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_CREATOR_LOCATION_ID).append(":").append(location.getObjectID());
                } else if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_GOODS_RECEIVED_NOTE_SEE_OWN)) {
                    sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID).append(":").append(user.getObjectID());
                }
            }
        }

        if (filterParametrs.getWarehouseID() != null) {
            sql.append(" AND ").append(SolrProductServiceRepresenter.FIELD_WAREHOUSE_ID + ":").append(filterParametrs.getWarehouseID()).append(" ");
        }

        if (filterParametrs.getSearchKey() != null && !"".equals(filterParametrs.getSearchKey())) {
            sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(filterParametrs.getSearchKey()));
//            if (!filterParametrs.isLookUp()) {
//                final SolrSearchUtils searchUtils = new SolrSearchUtils();
//                searchUtils.generateSearchQuery(sql, QuoteServiceImpl.getDynSearchFields(), filterParametrs.getSearchKey());
//            }
            sql.append(")");
        }
        return sql.toString();
    }


    @Override
    public Integer getGdnGrnCount(final Integer picklistId, final boolean isGdn) {
        final ListingFilterParameter fp = new ListingFilterParameter();
        fp.setIsGdn(isGdn);
        Integer pickListID = picklistId;
        if (fp.isGdn()) {
            EdsPickList pickList = this.pickListManager.get(picklistId);

            if (pickList == null || pickList.getSaleQuote() == null) {
                return 0;
            }
            pickListID = pickList.getSaleQuote().getObjectID();
        }
        fp.setEntityID(pickListID);
        final Integer count = shippingDataManager.getListingCount(fp);

        return count;
    }

    @Override
    public ShippingData getShippingDate(final Integer id) {
        if (id == null) {
            return null;
        }
        EdsShippingData edsShippingData = this.shippingDataManager.get(id);

        if (edsShippingData == null || edsShippingData.isDeleted()) {
            return null;
        }
        return edsShippingData.toTO();
    }

    @Override
    public ShippingData getShippingData(final Integer id, final boolean isGdn) {
        return this.getShippingData(id, isGdn, false);
    }

    @Override
    @Transactional(readOnly = true)
    public ShippingData getShippingData(final Integer id, final boolean isGdn, final boolean forExcel) {
        if (id == null) {
            return null;
        }
        EdsShippingData edsShippingData = this.shippingDataManager.get(id);

        if (edsShippingData == null || edsShippingData.isDeleted()) {
            return null;
        }
        final ShippingData item = edsShippingData.toTO();
        if (isGdn) {
            final List<EdsGoodsDeliveredTransaction> transaction = this.transactionManager.getGoodsDeliverdTransactionByShippingData(edsShippingData);
            if (transaction != null && transaction.size() > 0) {
                item.setJournalId(transaction.get(0).getJournalId());
            }
        } else {
            final List<EdsGoodsReceivedTransaction> transaction = this.transactionManager.getGoodsReceivedTransactionByShippingData(edsShippingData);
            if (transaction != null && transaction.size() > 0) {
                item.setJournalId(transaction.get(0).getJournalId());
            }
            if (item.getQuoteId() != null) {
                item.setRelatedExpenses(this.expenseReportManager.getExpensesAllocatedToPO(item.getQuoteId()));
                item.setTotalAllocatedAmount(this.shippingDataManager.getTotalAllocatedAmount(item.getQuoteId()));
            }
        }
        if (edsShippingData.getQuote() != null && edsShippingData.getQuote().getObjectID() != null) {
            final EdsSaleQuote saleQuote = this.quoteManager.getSaleQuote(edsShippingData.getQuote().getObjectID());
            if (saleQuote != null) {
                item.setSalesOrder(saleQuote.isSalesOrder());
            }
        }

        final EdsCrmAccount crmAccount = edsShippingData.getCrmAccount();
        final CrmAccountItem accountItem = new CrmAccountItem(crmAccount.getName());
//        tepada o'zi set qilgan
//        item.setCustomer(accountItem);
        CrmAccountItem customer = item.getCustomer();
        if (customer != null && customer.getClientBalance().doubleValue() == 0) {
            BigDecimal clientBalance = crmAccountManager.getClientBalance(crmAccount.getObjectID());
            item.getCustomer().setClientBalance(clientBalance.doubleValue());
        }
        accountItem.setPhone(crmAccount.getPhone());
        accountItem.setMailAddresses(new Address[]{crmAccount.getMailingAddress(true).getRPC()});
        List<EdsShippingDataItem> list = this.shippingDataItemManager.findByShippingDataId(edsShippingData.getObjectID());

        String grnOrGdn = "";
        if (item.getShippingType() != null && item.getShippingType().equals(ShippingDataType.OUT)) {
            grnOrGdn = AccountingConstants.GOODS_DELIVERED_NOTES;
        } else {
            grnOrGdn = AccountingConstants.GOODS_RECEIVED_NOTES;
        }
        item.setLayoutHtml(PathFinder.getLayoutHTML(AccountingConstants.GOODS_RECEIVED_NOTES));
        item.setTemplates(this.invoiceServiceLocal.getCompanyPdfTemplates(grnOrGdn).getItems());
        final EdsCompanyPdfTemplate template = this.companyPdfTemplateManager.getDefaultCompanyPdfTemplateByType(grnOrGdn);
        if (template != null && template.getObjectID() != null) {
            item.setSelectedTemplateId(template.getObjectID());
        }

        List<Integer> invoiceIds = list.stream().map(EdsShippingDataItem::getQuoteItemId).toList();
        List<Integer> itemIds = list.stream().map(EdsShippingDataItem::getObjectID).toList();
        Map<Integer, EdsInvoice> invoiceMap = invoiceManager.getInvoiceListByIds(invoiceIds);
        Map<Integer, String> gdnShippingLabels = shippingDataItemManager.getGDNShippingLabelsBySdiIds(itemIds);

        final boolean isAlmadarSerials = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ALMADAR_PRODUCT_SERIAL_ENABLED);

        for (final EdsShippingDataItem sdi : list) {
            log.info("CREATING SHIPPING DATA ITEM DTO FOR ITEM: {}", sdi.getObjectID());
            final EdsInvoice invoice = invoiceMap.get(sdi.getQuoteItemId());
            if (invoice != null && forExcel) {
                item.setInvoice(EdsInvoice.getInvoiceData(invoice));
            }

            final ShippingDataItem dataItem = sdi.toTO();
            final String report = gdnShippingLabels.get(sdi.getObjectID());
            if (report != null && !report.isEmpty()) {
                item.setShippingLabel(item.getShippingLabel() + report);
            }
            dataItem.setNumberOfPacks(sdi.getQuoteItem().getNumberOfPacks());
            if (forExcel) {
                if (sdi.getQuoteItem() != null && sdi.getQuoteItem().getItem() != null) {
                    final EdsItem edsItem = sdi.getQuoteItem().getItem();
                    dataItem.setItem(new ProductSelectItem(edsItem.getObjectID(), edsItem.getName() + " " + edsItem.getDescription(), edsItem.getProductType(), edsItem.isPurchasedFromSupplier()));
                }
                dataItem.getItem().setCategory(sdi.getShippingData() != null ? sdi.getShippingData().getShippingLabel() : "");
            }
            if (isAlmadarSerials) {//ALMADAR MEDICAL company
                final ArrayList<CompanyCustomFieldItem> cfList = new ArrayList<>(1);
                cfList.add(this.commonService.getCompanyCustomFieldByEntityNameAndFieldName(ViewName.ProductServiceView, "ARTICLE"));
                dataItem.setArticleNumberCF(CustomFieldsUtils.setRPCCustomFieldItems(sdi.getQuoteItem().getItem().getCustomFields(), cfList).get(0));
            }

            if (grnOrGdn == AccountingConstants.GOODS_DELIVERED_NOTES) {
                dataItem.setBatchItems(this.itemBatchService.getBatchItems(
                        sdi.getQuoteItemId(),
                        sdi.getQuoteItem().getItem().getObjectID(),
                        item.getId(),
                        ItemSerialEntityType.GOODS_DELIVERED.name()));
            } else {
                dataItem.setBatchItems(this.itemBatchService.getBatchItems(
                        sdi.getQuoteItemId(),
                        dataItem.getItem().getId(),
                        item.getId(),
                        ItemSerialEntityType.GOODS_RECEIVED.name()));
            }

            item.getItems().add(dataItem);
            log.info("SHIPPING DATA ITEM DTO CREATED FOR ITEM: {}", sdi.getObjectID());
        }
        return item;
    }

    @Override
    public Integer allocateExpensesToGrn(final ShippingData shippingData) {
        if (shippingData == null || CollectionUtils.isEmpty(shippingData.getItems())) {
            return null;
        }

        BigDecimal unallocatedAmount = shippingDataManager.getUnallocatedAmount(shippingData.getId(), shippingData.getQuoteId());
        BigDecimal newAllocationSum = shippingData.getItems().stream()
                .map(ShippingDataItem::getReceivedAllocation)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (unallocatedAmount.compareTo(BigDecimal.ZERO) != 0 && newAllocationSum.compareTo(unallocatedAmount) > 0) {
            return Errors.MORE_THAN_UNALLOCATED_AMOUNT;
        }

        HashMap<Integer, BigDecimal> oldAllocatedAmount = new HashMap<>();
        shippingData.getItems().forEach(item -> {
            final EdsShippingDataItem edsShippingDataItem = this.shippingDataItemManager.get(item.getId());
            oldAllocatedAmount.put(edsShippingDataItem.getObjectID(), edsShippingDataItem.getReceivedAllocation());
            edsShippingDataItem.setReceivedAllocation(item.getReceivedAllocation());
        });

        final EdsShippingData edsShippingData = this.shippingDataManager.get(shippingData.getId());
        final EdsPurchaseOrder purchaseOrder = this.quoteManager.getPurchaseOrderByID(shippingData.getQuoteId());
        this.accountingServiceLocal.createTransactionsForGoodsReceived(purchaseOrder, edsShippingData, oldAllocatedAmount);
        return Errors.COMPLETED;
    }

    @Override
    public TestRPC deleteGoodsReceivedNotes(final Integer id) {
        TestRPC result = new TestRPC();
        EdsShippingData shippingData = this.shippingDataManager.get(id);

        if (shippingData == null || shippingData.isDeleted() || shippingData.getQuote() == null) {
            throw new RuntimeException("Shipping data not found!");
        }
        EdsPurchaseOrder purchaseOrder = quoteManager.getPurchaseOrderByID(shippingData.getQuote().getObjectID());

        if (purchaseOrder == null) {
            throw new RuntimeException("Shipping data not found!");
        }
        if (this.quoteManager.hasConvertedShippingData(shippingData.getObjectID())) {
            result.setMessageCommand(MessageCommand.hasConvertedItems);
            return result;
        }
        List<Integer> transactionIds = this.transactionManager.getTransactionIdsByShippingData(shippingData);

        this.itemStockManager.deleteItemStocksByTransactionIds(transactionIds);
        final List<EdsGoodsReceivedTransaction> transactionList = this.transactionManager.getTransactionsByShippingData(shippingData);

        for (EdsGoodsReceivedTransaction edsGoodsReceivedTransaction : transactionList) {
            edsGoodsReceivedTransaction.setDeleted(true);
            transactionManager.update(edsGoodsReceivedTransaction);
            baseEventPostProcessor.registerEvent(TransactionCustomEventListenerImpl.TYPE, TransactionCustomEventListenerImpl.EVENT_REMOVE_GOODS_RECEIVED_NOTE_TRANSACTION, shippingData, userManager.getUser());
        }
        for (EdsShippingDataItem edsShippingDataItem : shippingData.getItems()) {
            final EdsQuoteItem quoteItem = edsShippingDataItem.getQuoteItem();
            if (quoteItem == null) {
                continue;
            }
            final BigDecimal quoteTotalReceivedAllocation = Optional.ofNullable(quoteItem.getReceivedAllocation()).orElse(BigDecimal.ZERO);
            final BigDecimal shippingReceivedAllocation = Optional.ofNullable(edsShippingDataItem.getReceivedAllocation()).orElse(BigDecimal.ZERO);
            final BigDecimal subtractResultAllocation = quoteTotalReceivedAllocation.subtract(shippingReceivedAllocation);
            quoteItem.setReceivedAllocation(subtractResultAllocation.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : subtractResultAllocation);

            if (ReceiveTypeEnum.RECEIVE_BY_QTY.equals(quoteItem.getReceiveType())) {
                final BigDecimal quoteTotalReceivedQty = Optional.ofNullable(quoteItem.getReceivedQty()).orElse(BigDecimal.ZERO);
                final BigDecimal shippingDataReceivedQty = Optional.ofNullable(edsShippingDataItem.getReceivedQty()).orElse(BigDecimal.ZERO);
                final BigDecimal subtractResult = quoteTotalReceivedQty.subtract(shippingDataReceivedQty);

                quoteItem.setReceivedQty(subtractResult.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : subtractResult);
            } else {
                final BigDecimal quoteTotalReceivedQty = Optional.ofNullable(quoteItem.getReceivedAmount()).orElse(BigDecimal.ZERO);
                final BigDecimal shippingDataReceivedQty = Optional.ofNullable(edsShippingDataItem.getReceivedAmount()).orElse(BigDecimal.ZERO);
                final BigDecimal subtractResult = quoteTotalReceivedQty.subtract(shippingDataReceivedQty);

                quoteItem.setReceivedAmount(subtractResult.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : subtractResult);
            }

            if (quoteItem.getItem() != null) {
                if (quoteItem.getItem().getInventoryTrackingEnabled()) {
                    itemSerialService.deleteSerialRelation(edsShippingDataItem.getObjectID(), ItemSerialEntityType.GOODS_RECEIVED.name());
                } else if (quoteItem.getItem().getTrackBatchesEnabled()) {
                    itemBatchManager.deleteBatchesByEntity(id, quoteItem.getItem().getObjectID(), ItemSerialEntityType.GOODS_RECEIVED.name());
                }
            }
            edsShippingDataItem.setDeleted(true);
            this.shippingDataItemManager.update(edsShippingDataItem);
        }
        Date deletionDate = new Date();
        EdsUser user = userManager.getUser();
        log.info("GRN: {} has been deleted at {} by {} user", id, deletionDate, user.getObjectID());
        shippingData.setLastChanges("deleted by user: " + user.getObjectID());
        shippingData.setDeleted(true);
        this.shippingDataManager.update(shippingData);

        EdsFifoFailure failure = fifoFailureManager.getByEntityId(shippingData.getObjectID(), EntityType.GRN, ServerSecurityContext.getInstance().getCompanyId());

        if (failure != null) {
            failure.setDeleted(true);
            failure.setOnQue(false);
            fifoFailureManager.update(failure);
        }

        try {
            solrManager.removeShippingData(id, SecurityContext.getCompanyID());
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }

        boolean hasReceivedItems = false;
        final List<EdsQuoteItem> quoteItems = purchaseOrder.getQuoteItems();

        for (EdsQuoteItem quoteItem : quoteItems) {
            if (ReceiveTypeEnum.RECEIVE_BY_VALUE.equals(quoteItem.getReceiveType())) {
                if (Optional.ofNullable(quoteItem.getReceivedAmount()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) > 0) {
                    hasReceivedItems = true;
                }
            } else {
                if (Optional.ofNullable(quoteItem.getReceivedQty()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) > 0) {
                    hasReceivedItems = true;
                }
            }
        }
        EdsReference poStatus = this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.APPROVE);
        if (hasReceivedItems) {
            poStatus = this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.PARTIAL_RECEIVED);
        }
        if (poStatus != null) {
            purchaseOrder.setStatus(poStatus);
        }
        purchaseOrder.setQuoteItems(quoteItems);
        this.quoteManager.update(purchaseOrder);
        this.addPurchaseOrderToSolr(purchaseOrder);
        productSerialManager.removeGrnSerialNumbers(id);
        return result;
    }

    @Override
    public TestRPC deleteGoodsDeliveredNotes(Integer id) {
        final TestRPC result = new TestRPC();
        final EdsShippingData shippingData = shippingDataManager.get(id);

        if (shippingData == null || shippingData.isDeleted() || shippingData.getQuote() == null) {
            throw new RuntimeException("Shipping data not found!");
        }
        final EdsSaleQuote saleQuote = this.quoteManager.getSaleQuote(shippingData.getQuote().getObjectID());

        if (saleQuote == null) {
            throw new RuntimeException("Shipping data not found!");
        }
        if (quoteManager.hasConvertedShippingData(shippingData.getObjectID())) {
            result.setMessageCommand(MessageCommand.hasConvertedItems);
            return result;
        }
        final List<EdsGoodsDeliveredTransaction> transactionList = this.transactionManager.getGoodsDeliverdTransactionsByShippingData(shippingData);
        final List<Integer> transactionIds = transactionList.stream()
                .map(EdsGoodsDeliveredTransaction::getObjectID)
                .collect(Collectors.toList());

        this.itemStockManager.deleteItemStocksByTransactionIds(transactionIds);

        for (EdsGoodsDeliveredTransaction edsGoodsDeliveredTransaction : transactionList) {
            edsGoodsDeliveredTransaction.setDeleted(true);
            this.transactionManager.update(edsGoodsDeliveredTransaction);
            baseEventPostProcessor.registerEvent(TransactionCustomEventListenerImpl.TYPE, TransactionCustomEventListenerImpl.EVENT_REMOVE_GOODS_DELIVIRY_NOTE_TRANSACTION, shippingData, userManager.getUser());
        }
        for (EdsShippingDataItem edsShippingDataItem : shippingData.getItems()) {
            final EdsQuoteItem quoteItem = edsShippingDataItem.getQuoteItem();
            if (quoteItem == null) {
                continue;
            }
            final BigDecimal quoteTotalShippedQty = Optional.ofNullable(quoteItem.getShippedQty()).orElse(BigDecimal.ZERO);
            final BigDecimal shippingDataReceivedQty = Optional.ofNullable(edsShippingDataItem.getReceivedQty()).orElse(BigDecimal.ZERO);
            final BigDecimal subtractResult = quoteTotalShippedQty.subtract(shippingDataReceivedQty);

            quoteItem.setShippedQty(subtractResult.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : subtractResult);

            if (quoteItem.getItem() != null && quoteItem.getItem().getInventoryTrackingEnabled()) {
                itemSerialService.deleteSerialRelation(edsShippingDataItem.getObjectID(), ItemSerialEntityType.GOODS_DELIVERED.name());
            }
            if (quoteItem.getItem() != null && quoteItem.getItem().getTrackBatchesEnabled()) {
                itemBatchManager.deleteBatchesByEntity(id, quoteItem.getItem().getObjectID(), ItemSerialEntityType.GOODS_DELIVERED.name());
            }

            edsShippingDataItem.setDeleted(true);
            this.shippingDataItemManager.update(edsShippingDataItem);
        }

        Date deletionDate = new Date();
        EdsUser user = userManager.getUser();
        log.info("GDN: {} has been deleted at {} by {} user", id, deletionDate, user.getObjectID());
        shippingData.setLastChanges("deleted by user: " + user.getObjectID());
        shippingData.setDeleted(true);
        this.shippingDataManager.update(shippingData);

        EdsFifoFailure failure = fifoFailureManager.getByEntityId(shippingData.getObjectID(), EntityType.GDN, ServerSecurityContext.getInstance().getCompanyId());

        if (failure != null) {
            failure.setDeleted(true);
            failure.setOnQue(false);
            fifoFailureManager.update(failure);
        }

        try {
            solrManager.removeShippingData(id, SecurityContext.getCompanyID());
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }

        deleteGDNProductSerails(shippingData.getItems());
        boolean hasShippedItems = false;
        final List<EdsQuoteItem> quoteItems = saleQuote.getQuoteItems();

        final EdsPickList pickList = pickListManager.getPickListBySaleQuoteID(saleQuote.getObjectID());

        for (EdsQuoteItem quoteItem : quoteItems) {
            if (Optional.ofNullable(quoteItem.getShippedQty()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) > 0) {
                hasShippedItems = true;
                break;
            }
        }
        EdsReference soStatus = this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SALE_ORDER);
        EdsReference sqStatus = this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.CLIENT_APPROVE);
        if (hasShippedItems) {
            soStatus = this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.PARTIAL_SHIPPED);
            sqStatus = this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.PARTIAL_SHIPPED);
        }
        if (!saleQuote.isSalesOrder()) {
            if (sqStatus != null) {
                saleQuote.setStatus(sqStatus);
                if (pickList != null) {
                    pickList.setStatus(sqStatus);
                }
            }
        } else {
            if (soStatus != null) {
                saleQuote.setStatus(soStatus);
                if (pickList != null) {
                    pickList.setStatus(soStatus);
                }
            }
        }

        saleQuote.setQuoteItems(quoteItems);
        this.quoteManager.update(saleQuote);
        this.addSaleQuoteToSolr(saleQuote);
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE,
                shippingData,
                userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_GDN);
        return result;
    }

    private void deleteGDNProductSerails(List<EdsShippingDataItem> quoteItem) {
        List<Integer> collect = quoteItem.stream().map(EdsShippingDataItem::getShippingDataId).collect(Collectors.toList());
        List<Integer> itemsDeleted = productSerialManager.getProductSerialsByGDN(collect);
        for (Integer id : itemsDeleted) {
            EdsProductSerial ps = productSerialManager.get(id);
            ps.setInvoiceItemID(null);
            ps.setGdnid(null);
            productSerialManager.update(ps);
        }
    }

    @Override
    public boolean saveSaleQuoteEditCellValue(NewInvoice rowValue, String columnCodeName) {
        EdsSaleQuote quote = quoteManager.getSaleQuote(rowValue.getID());
        try {
            EdsInvoiceCustomFields quoteCF = quote.getCustomFields();
            if (quoteCF == null) {
                quoteCF = new EdsInvoiceCustomFields();
                invoiceCFManager.create(quoteCF);
                quote.setCustomFields(quoteCF);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(quoteCF, rowValue.getCustomFieldMap(), columnCodeName);
            addSaleQuoteToSolr(quote);

            return true;
        } catch (Exception e) {
            log.error("Quote List Edit Cell Column Code :" + columnCodeName, e);
            return false;
        }
    }

    @Override
    public boolean savePurchaseOrderCellValue(NewInvoice rowValue, String columnCodeName) {
        EdsPurchaseOrder order = quoteManager.getPurchaseOrderByID(rowValue.getID());
        try {
            EdsInvoiceCustomFields purchaseOrderCF = order.getCustomFields();
            if (purchaseOrderCF == null) {
                purchaseOrderCF = new EdsInvoiceCustomFields();
                invoiceCFManager.create(purchaseOrderCF);
                order.setCustomFields(purchaseOrderCF);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(purchaseOrderCF, rowValue.getCustomFieldMap(), columnCodeName);
            addPurchaseOrderToSolr(order);
            return true;
        } catch (Exception e) {
            log.error("Purchase Order List Edit Cell Column Code :" + columnCodeName, e);
            return false;
        }
    }

    @Override
    public SelectItem[] getRfqItemSuppliersAsSelectItem(Integer rfqId) {
        EdsRFQ edsRFQ = rfqManager.get(rfqId);
        if (edsRFQ != null) {
            if (edsRFQ.getItems() != null && edsRFQ.getItems().size() > 0) {
                List<SelectItem> items = new ArrayList<>();
                for (EdsRFQItem rfqItem : edsRFQ.getItems()) {
                    if (rfqItem.getSupplier() != null) {
                        items.add(rfqItem.getSupplier().getAsSelectItem());
                    }
                }
                items.sort(Comparator.comparing(SelectItem::getName));
                return items.toArray(new SelectItem[]{});
            }
        }
        return null;
    }

    @Transactional
    public List<HistoryNote> getStockTransferHistoryNotes(Integer objectId) {
        if (objectId == null) {
            return null;
        }
        List<HistoryListItem> notes = getStockTransferNotes(objectId);
        List<HistoryNote> result = new ArrayList<>(notes);

        List<MyUpdateItem> updates = invoiceServiceLocal.getAllHistory(objectId, LookUpConstants.STOCK_TRANSFER);
        result.addAll(updates);

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<HistoryListItem> getStockTransferNotes(Integer objectID) {
        return stockTransferNoteManager.getStockTransferNotesAsHistoryListItem(objectID);
    }

    @Override
    public Integer saveStockTransferNotes(HistoryListItem historyListItem, Integer stockTransferId) {
        if (historyListItem != null && stockTransferId != null) {
            EdsStockTransfer edsStockTransfer = stockTransferManager.get(stockTransferId);
            if (edsStockTransfer == null) {
                return null;
            }
            if (historyListItem.getObjectID() != null) {
                EdsStockTransferNote note = stockTransferNoteManager.get(historyListItem.getObjectID());
                note.setComment(historyListItem.getComment());
                note.setCommentator(invoiceManager.getUser());
                note.setSuperUser(ServerUtils.isSuperUser());
                stockTransferNoteManager.update(note);
                return note.getObjectID();
            } else {
                EdsStockTransferNote note = new EdsStockTransferNote();
                note.setComment(historyListItem.getComment());
                note.setCommentator(invoiceManager.getUser());

                note.setStockTransfer(edsStockTransfer);
                note.setDate(new Date());
                note.setSuperUser(ServerUtils.isSuperUser());
                stockTransferNoteManager.create(note);
                return note.getObjectID();
            }
        }
        return null;
    }


    @Transactional
    public List<HistoryNote> getStockAdjustmentHistoryNotes(Integer objectId) {
        if (objectId == null) {
            return null;
        }
        List<HistoryListItem> notes = getStockAdjustmentNotes(objectId);
        List<HistoryNote> result = new ArrayList<>(notes);

        List<MyUpdateItem> updates = invoiceServiceLocal.getAllHistory(objectId, LookUpConstants.STOCK_ADJUSTMENT);
        result.addAll(updates);

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<HistoryListItem> getStockAdjustmentNotes(Integer objectID) {
        return stockAdjustmentNoteManager.getStockAdjustmentNotesAsHistoryListItem(objectID);
    }

    @Override
    public Integer saveStockAdjustmentNotes(HistoryListItem historyListItem, Integer stockAdjustmentId) {
        if (historyListItem != null && stockAdjustmentId != null) {
            EdsStockAdjustment edsStockAdjustment = stockAdjustmentManager.get(stockAdjustmentId);
            if (edsStockAdjustment == null) {
                return null;
            }
            if (historyListItem.getObjectID() != null) {
                EdsStockAdjustmentNote note = stockAdjustmentNoteManager.get(historyListItem.getObjectID());
                note.setComment(historyListItem.getComment());
                note.setCommentator(invoiceManager.getUser());
                note.setSuperUser(ServerUtils.isSuperUser());
                stockAdjustmentNoteManager.update(note);
                return note.getObjectID();
            } else {
                EdsStockAdjustmentNote note = new EdsStockAdjustmentNote();
                note.setComment(historyListItem.getComment());
                note.setCommentator(invoiceManager.getUser());

                note.setStockAdjustment(edsStockAdjustment);
                note.setDate(new Date());
                note.setSuperUser(ServerUtils.isSuperUser());
                stockAdjustmentNoteManager.create(note);
                return note.getObjectID();
            }
        }
        return null;
    }

    @Override
    public BigDecimal getBookingProductQTYInWarehouse(Integer pickListId, Integer productId, Integer warehouseId) {
        SelectItem bookingItem = quoteManager.getBookingQty(productId, warehouseId, pickListId);

        return BigDecimal.valueOf(bookingItem.getTotalAmount());
    }

    @Override
    public ArrayList<SaleOrderBaseInvoiceItem> getConvertingItems(ListingFilterParameter fp) {
        if (fp.isGdn()) {
            List<EdsShippingData> gdns = shippingDataManager.getShippingDataList(fp);
            return gdns.stream().filter(gdn -> gdn.getStatus() == null || !gdn.getStatus().equals(ShippingDataStatus.CONVERTED)).map(gdn -> {
                SaleOrderBaseInvoiceItem item = new SaleOrderBaseInvoiceItem();
                item.setObjectId(gdn.getObjectID());
                item.setNumber(gdn.getNumber());
                item.setShippingLabel(gdn.getShippingLabel());
                item.setQuoteId(gdn.getQuote().getObjectID());
                item.setReference(gdn.getQuote().getNumber());
                item.setType(SaleOrderBaseInvoiceItem.GDN);
                item.setShipDate(new DateNonConvertable(gdn.getShippingDate()));
                item.setOrderDate(new DateNonConvertable(gdn.getQuote().getInvoiceDate()));
                return item;
            }).collect(Collectors.toCollection(ArrayList::new));
        } else {
            ArrayList<Integer> statusIds = new ArrayList<>();
            if (fp.getType() == 0 || fp.getType() == 2) {
                statusIds.add(this.getInvoiceStatus(Constants.CLIENT_APPROVE).getObjectID());
            }

            if (fp.getType() == 0 || fp.getType() == 1) {
                statusIds.add(this.getInvoiceStatus(Constants.SALE_ORDER).getObjectID());
                statusIds.add(this.getInvoiceStatus(Constants.PICKED).getObjectID());
                statusIds.add(this.getInvoiceStatus(Constants.PACKED).getObjectID());

                if (fp.getType() == 1 && fp.getStatusID() == 1) {
                    statusIds.clear();
                    statusIds.add(this.getInvoiceStatus(Constants.SHIPPED).getObjectID());
                    statusIds.add(this.getInvoiceStatus(Constants.PARTIAL_SHIPPED).getObjectID());
                }
            }
            List<EdsSaleQuote> quotes = quoteManager.getSaleQuotes(fp, statusIds);
            return quotes.stream().map(quote -> {
                SaleOrderBaseInvoiceItem item = new SaleOrderBaseInvoiceItem();
                item.setObjectId(quote.getObjectID());
                item.setNumber(quote.getNumber());
                item.setReference(quote.getReference());
                item.setOrderDate(new DateNonConvertable(quote.getInvoiceDate()));
                item.setDueDate(new DateNonConvertable(quote.getDueDate()));
                item.setType(quote.isSalesOrder() ? SaleOrderBaseInvoiceItem.SALE_ORDER : SaleOrderBaseInvoiceItem.SALE_QUOTE);
                return item;
            }).collect(Collectors.toCollection(ArrayList::new));
        }
    }

    @Override
    public void deleteSelectedRFQs(ArrayList<Integer> ids) {
        for (Integer id : ids) {
            deleteRFQ(id);
        }
    }

    @Override
    public SelectItem[] getGroupedItems(String objectType, ArrayList<Integer> Ids, HashMap<String, Boolean> fieldsForName, HashMap<String, Boolean> fieldsForDesc) {

        if (!SaleOrderBaseInvoiceItem.GDN.equals(objectType)) {
            return quoteManager.getGroupedItems(Ids, fieldsForName, fieldsForDesc).toArray(new SelectItem[]{});
        }
        return new SelectItem[0];
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getRFQSolrQuery(ListingFilterParameter filterParametrs, EdsUser user, String selectedDate) {
        StringBuffer sql = new StringBuffer();
        sql.append(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID).append(":").append(user.getCompany().getObjectID());

        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null && "DUE_DATE".equals(selectedDate)) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sql.append(" AND (((").append(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE).append(":[").append(format.format(filterParametrs.getStartDate())).append(" TO * ]) ");
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE).append(":[ * TO ").append(format.format(filterParametrs.getEndDate())).append(" ])))");
        }
        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null && "RFQ_DATE".equals(selectedDate)) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sql.append(" AND (((").append(SolrSaleInvoiceRepresenter.FIELD_RFQ_DATE).append(":[").append(format.format(filterParametrs.getStartDate())).append(" TO * ]) ");
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_RFQ_DATE).append(":[ * TO ").append(format.format(filterParametrs.getEndDate())).append(" ])))");
        }

        List<String> customAccessRoles = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS);
        boolean hasCustomFullAccessToListing = customAccessRoles.size() > 0 && user.hasEitherRoles(customAccessRoles.toArray(new String[]{}));


        if (!hasCustomFullAccessToListing) {
            boolean ownerAccess = ServerUtils.hasPermission(PermissionConstants.RFQ_SEE_OWN);
            StringBuilder clientIDsStr = new StringBuilder();
            if (filterParametrs.getClientId() != null) {
                EdsCrmAccount crmAccount = crmAccountManager.get(filterParametrs.getClientId());
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

            if (!clientIDsStr.toString().isEmpty()) {
                sql.append(" AND (");
                sql.append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":(").append(clientIDsStr).append(") ");
                sql.append(" OR ").append(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID).append(":").append(user.getObjectID());
                sql.append(")");
            } else {
                sql.append(" AND ");
                sql.append("( ");
                sql.append(SolrSaleInvoiceRepresenter.FIELD_CURRENT_APPROVER_ID).append(":").append(user.getObjectID());
                sql.append(" OR ").append(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID).append(":").append(user.getObjectID());
                sql.append(" )");
            }

        }

        if (filterParametrs.getClientId() != null) {

            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(filterParametrs.getClientId());

            if (filterParametrs.getRelationID() != null && filterParametrs.getRelationType() != null) {
                List<Integer> rfqIds = new ArrayList<>();
                rfqIds = relationManager.getRelationIDsByType(filterParametrs.getRelationID(), filterParametrs.getEntityID(), filterParametrs.getRelationType(), RelationItem.TYPE_REQUEST_FOR_QUOTE);
                sql.append(" OR (").append(SolrSaleInvoiceRepresenter.FIELD_RFQ_ID).append(":(").append(ServerUtils.getAsCommoDelimited(rfqIds, "0", " ")).append("))");
            }
            sql.append(" ) ");
        } else if (filterParametrs.getRelationID() != null && filterParametrs.getRelationType() != null) {
            List<Integer> rfqIds = new ArrayList<>();
            rfqIds = relationManager.getRelationIDsByType(filterParametrs.getRelationID(), filterParametrs.getEntityID(), filterParametrs.getRelationType(), RelationItem.TYPE_REQUEST_FOR_QUOTE);
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_RFQ_ID).append(":(").append(ServerUtils.getAsCommoDelimited(rfqIds, "0", " ")).append("))");
        }

        // search key in composite
        if (filterParametrs.getSearchKey() != null && !"".equals(filterParametrs.getSearchKey())) {
            sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(filterParametrs.getSearchKey()));
            if (!filterParametrs.isLookUp()) {
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(sql, getRFQSearchFields(), filterParametrs.getSearchKey());
            }
            sql.append(")");
        }
        if (filterParametrs.getObjectsIds() != null) {
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_RFQ_ID).append(":").append(filterParametrs.getObjectsIds().replace(",", " OR " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":")).append(")");
        }
        return sql.toString();
    }

    private String getDiscountName(Integer id) {
        String name = null;
        // If nothing selected, by default it gets Percentage
        if (id == null || id == 0) {
            name = "Percentage";
        } else if (id == 1) {
            name = "Fixed Amount";
        } else {
            name = discountManager.get(id).getName();
        }
        return name;
    }

//    private SolrQuery getRFQSolrQuery(ListingFilterParameter filterParametrs, String solrQuery) {
//        SolrQuery query = new SolrQuery();
//        query.setQuery(solrQuery);
//        query.setStart(filterParametrs.getStart());
//        if (filterParametrs.getLimit() > 0) {
//            query.setParam(CommonParams.ROWS, String.valueOf(filterParametrs.getLimit()));
//        } else {
//            query.setParam(CommonParams.ROWS, "500");
//        }
//
//        if (!filterParametrs.isSearchButton()) {
//            if (filterParametrs.getSortField() != null && !"".equals(filterParametrs.getSortField())) {
//                SolrQuery.ORDER order = filterParametrs.isAscending() ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc;
//                switch (filterParametrs.getSortField()) {
//                    case RFQData.REQUEST_NUMBER:
//                        query.setSort(SolrSaleInvoiceRepresenter.FIELD_RFQ_NUMBER, order);
//                        break;
//                    case RFQData.DATE:
//                        query.setSort(SolrSaleInvoiceRepresenter.FIELD_RFQ_DATE, order);
//                        break;
//                    case RFQData.STATUS:
//                        query.setSort(SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME, order);
//                        break;
//                    case RFQData.OPPORTUNITY_NAME:
//                        query.setSort(SolrSaleInvoiceRepresenter.FIELD_CLIENT_NAME, order);
//                        break;
//                    case RFQData.PROJECT:
//                        query.setSort(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NAME, order);
//                        break;
//                    case RFQData.APPROVER:
//                        query.setSort(SolrSaleInvoiceRepresenter.FIELD_CURRENT_APPROVER_NAME, order);
//                        break;
//                }
//            } else {
//                query.setSort(SolrSaleInvoiceRepresenter.FIELD_RFQ_ID, SolrQuery.ORDER.desc);
//            }
//        }
//        return query;
//    }

    @Override
    public NewInvoice getQuoteCustomFieldItems(Integer customerId, boolean isSalesOrder) {
        NewInvoice invoiceItem = new NewInvoice();

        EdsCrmAccount customer = crmAccountManager.get(customerId);
        ArrayList<CompanyCustomFieldItem> customerCustomFields = null;
        if (customer != null) {
            customerCustomFields = (ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(customer.getCustomFields(),
                    this.commonService.getCompanyCustomFields(ViewName.CrmAccount));

            final ArrayList<CompanyCustomFieldItem> customFieldsItems = isSalesOrder ? this.commonService.getCompanyCustomFields(ViewName.SaleOrder) : this.commonService.getCompanyCustomFields(ViewName.SaleQuote);
            final ArrayList<CompanyCustomFieldItem> saleOrderCustomFields = (ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(null, customFieldsItems);

            for (final CompanyCustomFieldItem inputcf : saleOrderCustomFields) {
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
        return invoiceItem;
    }

    @Override
    public String grnOrGdnCorrection(Integer objectId) {
        StringBuilder str = new StringBuilder();
        EdsShippingData shippingData = shippingDataManager.get(objectId);
        if (ShippingDataType.OUT.equals(shippingData.getShippingType())) {
            ArrayList<QuantityItem> itemsToValidate = new ArrayList<>();
            shippingData.getItems().forEach(shitem -> {
                if (!shitem.isDeleted()) {
                    QuantityItem quantityItem = new QuantityItem();
                    quantityItem.setId(shitem.getItem().getObjectID());
                    quantityItem.setWarehouseID(shitem.getWarehouseId());
                    quantityItem.setQuantity(shitem.getApplyingQuantity());
                    itemsToValidate.add(quantityItem);
                }
            });
            SelectItem[] eroritems = stockValidationService.validateStockAvailability(itemsToValidate.toArray(new QuantityItem[0]), shippingData.getObjectID(), StockOutFlow.FROM_GOODS_DELIVERY_NOTES, null);
            if (eroritems != null && eroritems.length > 0) {
                StringBuilder itemNames = new StringBuilder();
                for (int i = 0; i < eroritems.length; i++) {
                    if (i != 0) {
                        itemNames.append(", ");
                    }
                    itemNames.append("\"").append(eroritems[i].getName()).append("\"");
                }
                str.append(" GDN update failed, There are items not enough of " + itemNames + " in your warehouse");
            } else {
                List<Integer> transactionIds = this.transactionManager.getTransactionIdsByShippings(Collections.singletonList(shippingData.getObjectID()));
                if (!CollectionUtils.isEmpty(transactionIds)) {
                    this.itemStockManager.deleteItemStocksByTransactionIds(transactionIds);
                    transactionIds.forEach(tId -> transactionManager.deleteTransaction(tId));
                }
                EdsSaleQuote saleQuote = quoteManager.getSaleQuote(shippingData.getQuote().getObjectID());
                accountingServiceLocal.createTransactionForGoodsDelivered(saleQuote, shippingData);
            }
        } else {
            EdsPurchaseOrder purchaseORder = quoteManager.getPurchaseOrderByID(shippingData.getQuote().getObjectID());
            this.accountingServiceLocal.createTransactionsForGoodsReceived(purchaseORder, shippingData, null);
        }
        return str.toString();
    }
}
