package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyAttachment;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsBaseInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsBaseSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsComissionAllocateItem;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceQuoteNote;
import com.edatasite.workforce.core.domain.accounting.EdsPickList;
import com.edatasite.workforce.core.domain.accounting.EdsPriceLevel;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteItem;
import com.edatasite.workforce.core.domain.accounting.EdsRecurringBill;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsShippingMethod;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseBooking;
import com.edatasite.workforce.core.solr.component.PurchaseInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.PurchaseOrderSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleQuoteSolrComponent;
import com.edatasite.workforce.core.solr.document.PurchaseOrderSolrDoc;
import com.edatasite.workforce.core.solr.document.SaleInvoiceSolrDoc;
import com.edatasite.workforce.core.solr.document.SaleQuoteSolrDoc;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataType;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProductServiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPurchaseInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.app.social.revolut.RevolutService;
import com.edatasite.workforce.gwt.core.server.app.social.revolut.dto.RevolutResponseDto;
import com.edatasite.workforce.gwt.core.server.commons.MastercardPaymentHandler;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.ApproverManager;
import com.edatasite.workforce.gwt.core.server.db.BankAccountManager;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceQuoteNoteManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import com.edatasite.workforce.gwt.core.server.db.ShippingDataManager;
import com.edatasite.workforce.gwt.core.server.db.VatManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.InvoiceItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PickListManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PriceLevelManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductSerialManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseBookingManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.invoice.client.rpc.AllocateComissionItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.RecurringInvoiceListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingDataItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.enums.TaxTypeEnum;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.BaseInvoiceDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.InvoiceDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.InvoicePaymentDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.LineItemDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.OrderDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.IdDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CommonParams;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 06.10.2008
 * Time: 16:21:46
 * To change this template use File | Settings | File Templates.
 */

@Transactional
@Service("invoiceCircularResolver")
public class InvoiceCircularResolverImpl implements InvoiceCircularResolver, InvoiceAPIService, Constants, AccountingConstants {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected CurrencyManager currencyManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private InvoicingSettingsManager invoicingSettingsManager;
    @Autowired
    private CompanyAttachmentManager companyAttachmentManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private PickListManager pickListManager;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private InvoiceQuoteNoteManager invoiceQuoteNoteManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private PriceLevelManager priceLevelManager;
    @Autowired
    private ProductSerialManager productSerialManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private CourseBookingManager courseBookingManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private ShippingDataManager shippingDataManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private InvoiceItemCFManager invoiceItemCFManager;
    @Autowired
    private ApproverManager approverManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private RevolutService revolutService;
    @Autowired
    private QuoteServiceLocal quoteServiceLocal;
    @Autowired
    private SaleQuoteSolrComponent saleQuoteSolrComponent;
    @Autowired
    private SaleInvoiceSolrComponent saleInvoiceSolrComponent;
    @Autowired
    private PurchaseInvoiceSolrComponent purchaseInvoiceSolrComponent;
    @Autowired
    private PurchaseOrderSolrComponent purchaseOrderSolrComponent;
    @Autowired
    private BankAccountManager bankAccountManager;
    @Autowired
    private VatManager vatManager;

    private static Map<String, Double> getPurchaseInvoiceSearchFields() {
        Map<String, Double> fields = new HashMap<>();
        fields.put(SolrPurchaseInvoiceRepresenter.FIELD_CLIENT_NAME, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrPurchaseInvoiceRepresenter.FIELD_RELATED_PROJECT_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_NUMBER, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.REFERENCE, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrTaskRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }

    private static Map<String, Double> getPurchaseOrderSearchFields() {
        Map<String, Double> fields = new HashMap<>();
        fields.put(SolrPurchaseInvoiceRepresenter.FIELD_CLIENT_NAME, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_NUMBER, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrTaskRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }

    private static Map<String, Double> getSalesOrderSearchFields() {
        Map<String, Double> fields = new HashMap<>();
        fields.put(SolrPurchaseInvoiceRepresenter.FIELD_CLIENT_NAME, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_NUMBER, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.REFERENCE, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrTaskRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }

    private static Map<String, Double> getSalesQuoteSearchFields() {
        Map<String, Double> fields = new HashMap<>();
        fields.put(SolrPurchaseInvoiceRepresenter.FIELD_CLIENT_NAME, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_NUMBER, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.REFERENCE, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrTaskRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }

    private static Map<String, Double> getSaleInvoiceSearchFields() {
        Map<String, Double> fields = new HashMap<>();
        fields.put(SolrSaleInvoiceRepresenter.FIELD_CLIENT_NAME, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_PRODUCT_NAME, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NUMBER, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_QUOTE_NUMBER, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_PO_NUMBER, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_NUMBER, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrSaleInvoiceRepresenter.REFERENCE, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrTaskRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getSaleInvoiceData(ListingFilterParameter filterParametrs) {
        return getSaleInvoiceDataInSolr(filterParametrs, roleManager.getUser(), false);
    }

    @Override
    @Transactional(readOnly = true)
    public ListResultTO<InvoiceDto> getSaleInvoiceList(ListingFilterParameter filterParametrs) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_SALEINVOICE_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(getSaleSolrQuery(filterParametrs, getSaleInvoiceSolrQuery(filterParametrs, roleManager.getUser(), false)), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return getSaleInvoiceFromSolrResult(resp, filterParametrs);
    }

    @Override
    public ListResultTO<InvoiceDto> getPurchaseInvoiceList(ListingFilterParameter filterParametrs) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_PURCHASE_INVOICE_CORE);
        QueryResponse resp = null;
        try {
            FacetFilterRpc purchaseFacetFilter = filterParametrs.getFacetFilter();
            if (purchaseFacetFilter != null && !purchaseFacetFilter.isFilterChanges()) {
                purchaseFacetFilter = commonServiceLocal.getUserFacetFilter(purchaseFacetFilter);
            }
            if (filterParametrs.getStartDateNC() != null) {
                filterParametrs.setStartDate(ServerUtils.parseFilterParameterDate(filterParametrs.getStartDateNC()));
            }
            if (filterParametrs.getEndDateNC() != null) {
                filterParametrs.setEndDate(ServerUtils.parseFilterParameterDate(filterParametrs.getEndDateNC()));
            }
            if (purchaseFacetFilter != null) {
                if (purchaseFacetFilter.getSearchKey() != null && !"".equals(purchaseFacetFilter.getSearchKey())) {
                    filterParametrs.setSearchKey(purchaseFacetFilter.getSearchKey());
                }
                filterParametrs.setFacetFilter(purchaseFacetFilter);
            }

            String solrQuery = getPurchaseInvoiceCoreSolrQuery(filterParametrs, roleManager.getUser(), null) +
                    SolrFacetUtils.generateForPricesFacet(purchaseFacetFilter,
                            FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[4],
                            FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[5],
                            FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6]) +
                    SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(purchaseFacetFilter, null,
                            SolrPurchaseInvoiceRepresenter.FIELD_INVOICE_DATE,
                            SolrPurchaseInvoiceRepresenter.FIELD_DUE_DATE,
                            FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[4],
                            FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[5],
                            FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6]
                    );
            resp = server.query(getPurchaseInvoiceSolrQuery(filterParametrs, solrQuery), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return getPurchaseInvoiceFromSolrResult(resp);
    }

    public InvoiceList getSaleQuoteData(ListingFilterParameter filterParametrs) {
        EdsUser user = roleManager.getUser();
        return getSaleQuoteDataInSolr(filterParametrs, user, false);
    }

    @Override
    public ListResultTO<OrderDto> getSaleQuoteList(ListingFilterParameter filterParametrs) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_SALEQUOTE_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(getSaleSolrQuery(filterParametrs, getSaleQuoteSolrQuery(filterParametrs, roleManager.getUser(), false)), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return getSaleQuoteFromSolrResult(resp);
    }

//    public Integer indexCompanyPurchaseInvoice(SolrReindexRpc solrReindex, Integer start, Integer limit) {
//        List<EdsPurchaseInvoice> purchaseInvoiceList = invoiceManager.getPurchaseInvoiceListForSolr(solrReindex, start, limit);
//        if (purchaseInvoiceList.isEmpty()) {
//            return -1;
//        }
//        try {
//            purchaseInvoiceSolrComponent.indexes(purchaseInvoiceList);
//        } catch (IOException | SolrServerException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        return purchaseInvoiceList.get(purchaseInvoiceList.size() - 1).getObjectID();
//    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getInvoicesForConversionBalance(boolean isSaleInvoice) {
        ListLoadConfig config = new ListLoadConfig();
        config.setStart(0);
        config.setLimit(100);
        if (isSaleInvoice) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setLimit(100);
            return getSaleInvoiceDataInSolr(fp, roleManager.getUser(), true);
        } else {
            List<EdsPurchaseInvoice> invoiceList = invoiceManager.getPurchaseInvoiceList(null, true);
            return createPurchaseInvoiceList(invoiceList);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getSaleInvoiceDataForRecurrenceJob(ListingFilterParameter filterParameters, Integer employeeId) {
        EdsUser user = employeeManager.get(employeeId);
        return getSaleInvoiceData(filterParameters, null, user, "recurOverdueInvoice");
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList[] getSaleInvoiceDataForRecurrenceJobForEveryClient(ListingFilterParameter filterParameters, Integer employeeId) {
        EdsUser user = employeeManager.get(employeeId);
        return getSaleInvoiceDataForEveryClient(filterParameters, null, user, "recurOverdueInvoice");
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<RecurringInvoiceListItem> getRecurringInvoiceData(ListingFilterParameter filterParameters) {
        EdsUser user = invoiceManager.getUser();

        if (filterParameters.getStartDateNC() != null) {
            filterParameters.setStartDate(ServerUtils.parseFilterParameterDate(filterParameters.getStartDateNC()));
        }
        if (filterParameters.getEndDateNC() != null) {
            filterParameters.setEndDate(ServerUtils.parseFilterParameterDate(filterParameters.getEndDateNC()));
        }
        List<EdsBaseSaleInvoice> saleInvList = invoiceManager.getRecurringInvoiceList(filterParameters, filterParameters.asConfig());

        //Below we are getting access only to PM.
        if (roleManager.hasRole(user, PM) && !roleManager.hasEitherRoles(user, ACCOUNTANT, DR, ADMIN)) {
            List<EdsCrmAccount> pmClients = projectManager.getPMClients();

            List<EdsBaseSaleInvoice> saleInvoiceList = new LinkedList<>();
            for (EdsCrmAccount client : pmClients) {
                for (EdsBaseSaleInvoice invoice : saleInvList) {
                    if (client.equals(invoice.getClient()) && !saleInvoiceList.contains(invoice)) {
                        saleInvoiceList.add(invoice);
                    }
                }
            }
            return createRecurringInvoiceList(saleInvoiceList, filterParameters);
        }
        return createRecurringInvoiceList(saleInvList, filterParameters);
    }

    private ListResult<RecurringInvoiceListItem> createRecurringInvoiceList(List<EdsBaseSaleInvoice> invoices, final ListingFilterParameter filterParameters) {
        EdsUser user = invoiceManager.getUser();
        Long totalCount = invoiceManager.getTotalRecurringSaleInvoiceList(filterParameters, filterParameters.asConfig(), user.getCompany(), true);

        ArrayList<RecurringInvoiceListItem> invoiceListItems = new ArrayList<>();

        List<Integer> invoiceIDList = new LinkedList<>();
        for (EdsBaseSaleInvoice inv : invoices) {
            invoiceIDList.add(inv.getObjectID());
        }
        Map<Integer, Date> nextInvoiceDates = null;
        try {
            nextInvoiceDates = recurrenceService.getNextFireTimesAsMap(invoiceIDList.toArray(new Integer[]{}), SchedulerConstant.RECURRING_INVOICE_REMINDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i = 0;
        EdsCompany company = recurrenceManager.getUser().getCompany();
        Calendar calendar = new GregorianCalendar();
        for (EdsBaseSaleInvoice inv : invoices) {
            RecurringInvoiceListItem invoiceListItem = new RecurringInvoiceListItem();
            invoiceListItem.setObjectId(inv.getObjectID());
            invoiceListItem.setClient(inv.getClient().getName());
            invoiceListItem.setAmount(inv.getTotal());
            invoiceListItem.setReference(inv.getReference());
            invoiceListItem.setAmountInInvoiceCurrency(inv.getTotalInInvoiceCurrency());
            invoiceListItem.setRepeats(recurrenceService.getRecurrenceTemplateString(inv.getObjectID(), SchedulerConstant.RECURRING_INVOICE_REMINDER));
            if (nextInvoiceDates != null) {
                invoiceListItem.setNextInvoiceDate(nextInvoiceDates.get(inv.getObjectID()));
            }
            EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.RECURRING_INVOICE_REMINDER, inv.getObjectID(), company.getObjectID());
            if (recurrence != null) {// && recurrence.getEndType() != SchedulerConstant.NO_END_DATE) {
                Date recEndDate = recurrenceManager.getTriggerEndDate(recurrence, true);
                if (recurrence.getEndType() == SchedulerConstant.END_AFTER_OCCURRENCES && recurrence.getType() == SchedulerConstant.RECURRENCE_TYPE_DAILY &&
                        recurrence.getDailyPatternOptions() == SchedulerConstant.DAILY_PATTERN_OPTION_INTERVAL) {
                }
                invoiceListItem.setEndDate(recEndDate);
                invoiceListItem.setRecurrenceStatus(recEndDate.before(calendar.getTime()) ? "Ended" : "Active");
            }
            invoiceListItem.setStatus(referenceWfmMessageSource.localizeRef(inv.getStatus()));
            invoiceListItem.setStatusCode(inv.getStatus().getCode());
            invoiceListItems.add(invoiceListItem);
        }
        if (filterParameters != null) {
            ListLoadConfig config = filterParameters.asConfig();
            if (config != null && RecurringInvoiceListItem.NEXT_IVOICE_DATE.equals(config.getSortField())) {
                invoiceListItems.sort((o1, o2) -> {
                    int t;
                    if (!filterParameters.isAscending()) {
                        if (o1.getNextInvoiceDate() != null && o2.getNextInvoiceDate() != null) {
                            t = o1.getNextInvoiceDate().compareTo(o2.getNextInvoiceDate());
                        } else {
                            t = 0;
                        }
                    } else {
                        if (o1.getNextInvoiceDate() != null && o2.getNextInvoiceDate() != null) {
                            t = o2.getNextInvoiceDate().compareTo(o1.getNextInvoiceDate());
                        } else {
                            t = 0;
                        }
                    }
                    return t;
                });

            }
            if (config != null && RecurringInvoiceListItem.END_DATE.equals(config.getSortField())) {
                invoiceListItems.sort((o1, o2) -> {
                    int t;
                    if (!filterParameters.isAscending()) {
                        if (o1.getEndDate() != null && o2.getEndDate() != null) {
                            t = o1.getEndDate().compareTo(o2.getEndDate());
                        } else {
                            t = 0;
                        }

                    } else {
                        if (o1.getEndDate() != null && o2.getEndDate() != null) {
                            t = o2.getEndDate().compareTo(o1.getEndDate());
                        } else {
                            t = 0;
                        }
                    }
                    return t;
                });
            }
        }
        return new ListResult<>(invoiceListItems, totalCount.intValue());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<RecurringInvoiceListItem> getRecurringBillData(ListingFilterParameter filterParameters) {
        EdsUser user = invoiceManager.getUser();

        if (filterParameters.getStartDateNC() != null) {
            filterParameters.setStartDate(ServerUtils.parseFilterParameterDate(filterParameters.getStartDateNC()));
        }
        if (filterParameters.getEndDateNC() != null) {
            filterParameters.setEndDate(ServerUtils.parseFilterParameterDate(filterParameters.getEndDateNC()));
        }
        List<EdsRecurringBill> edsRecurringBillList = invoiceManager.getRecurringBillList(filterParameters, filterParameters.asConfig(), user.getCompany());

        return createRecurringBillList(edsRecurringBillList, filterParameters);
    }

    private ListResult<RecurringInvoiceListItem> createRecurringBillList(List<EdsRecurringBill> edsRecurringBillList, final ListingFilterParameter filterParameters) {
        EdsUser user = invoiceManager.getUser();
        Long totalCount = invoiceManager.getTotalRecurringBillList(filterParameters, filterParameters.asConfig(), user.getCompany());

        ArrayList<RecurringInvoiceListItem> invoiceListItems = new ArrayList<>();

        List<Integer> recurringBillIDList = new LinkedList<>();
        for (EdsRecurringBill edsRecurringBill : edsRecurringBillList) {
            recurringBillIDList.add(edsRecurringBill.getObjectID());
        }
        Map<Integer, Date> nextInvoiceDates = null;
        try {
            nextInvoiceDates = recurrenceService.getNextFireTimesAsMap(recurringBillIDList.toArray(new Integer[]{}), SchedulerConstant.RECURRING_BILL_REMINDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i = 0;
        EdsCompany company = recurrenceManager.getUser().getCompany();
        Calendar calendar = new GregorianCalendar();
        for (EdsRecurringBill edsRecurringBill : edsRecurringBillList) {
            RecurringInvoiceListItem invoiceListItem = new RecurringInvoiceListItem();
            invoiceListItem.setObjectId(edsRecurringBill.getObjectID());
            invoiceListItem.setClient(edsRecurringBill.getSupplier().getName());
            invoiceListItem.setAmount(edsRecurringBill.getTotal());
            invoiceListItem.setAmountInInvoiceCurrency(edsRecurringBill.getTotalInInvoiceCurrency());
            invoiceListItem.setRepeats(recurrenceService.getRecurrenceTemplateString(edsRecurringBill.getObjectID(), SchedulerConstant.RECURRING_BILL_REMINDER));
            if (nextInvoiceDates != null) {
                invoiceListItem.setNextInvoiceDate(nextInvoiceDates.get(edsRecurringBill.getObjectID()));
            }
            EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.RECURRING_BILL_REMINDER, edsRecurringBill.getObjectID(), company.getObjectID());
            if (recurrence != null) {// && recurrence.getEndType() != SchedulerConstant.NO_END_DATE) {
                Date recEndDate = recurrenceManager.getTriggerEndDate(recurrence, true);
                if (recurrence.getEndType() == SchedulerConstant.END_AFTER_OCCURRENCES && recurrence.getType() == SchedulerConstant.RECURRENCE_TYPE_DAILY &&
                        recurrence.getDailyPatternOptions() == SchedulerConstant.DAILY_PATTERN_OPTION_INTERVAL) {
                    //End date da bir interval kam chiqargani uchun delete qilindi
                }
                invoiceListItem.setEndDate(recEndDate);
                invoiceListItem.setRecurrenceStatus(recEndDate.before(calendar.getTime()) ? "Ended" : "Active");
            }
            invoiceListItem.setStatus(referenceWfmMessageSource.localizeRef(edsRecurringBill.getStatus()));
            invoiceListItem.setStatusCode(edsRecurringBill.getStatus().getCode());
            invoiceListItems.add(invoiceListItem);
        }
        if (filterParameters != null) {
            ListLoadConfig config = filterParameters.asConfig();
            if (config != null && RecurringInvoiceListItem.NEXT_IVOICE_DATE.equals(config.getSortField())) {
                invoiceListItems.sort((o1, o2) -> {
                    int t;
                    if (!filterParameters.isAscending()) {
                        if (o1.getNextInvoiceDate() != null && o2.getNextInvoiceDate() != null) {
                            t = o1.getNextInvoiceDate().compareTo(o2.getNextInvoiceDate());
                        } else {
                            t = 0;
                        }
                    } else {
                        if (o1.getNextInvoiceDate() != null && o2.getNextInvoiceDate() != null) {
                            t = o2.getNextInvoiceDate().compareTo(o1.getNextInvoiceDate());
                        } else {
                            t = 0;
                        }
                    }
                    return t;
                });

            }
            if (config != null && RecurringInvoiceListItem.END_DATE.equals(config.getSortField())) {
                invoiceListItems.sort((o1, o2) -> {
                    int t;
                    if (!filterParameters.isAscending()) {
                        if (o1.getEndDate() != null && o2.getEndDate() != null) {
                            t = o1.getEndDate().compareTo(o2.getEndDate());
                        } else {
                            t = 0;
                        }

                    } else {
                        if (o1.getEndDate() != null && o2.getEndDate() != null) {
                            t = o2.getEndDate().compareTo(o1.getEndDate());
                        } else {
                            t = 0;
                        }
                    }
                    return t;
                });

            }

        }
        return new ListResult<>(invoiceListItems, totalCount.intValue());
    }

    /**
     * <h1>... This is method read sale invoice data in solr and fill rpc object ...</h1>
     * <br/>
     * <h2>... Changed by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Last update {16:38 10/06/2011} ...</h3>
     *
     * @param filterParametrs
     * @param user
     * @param isConversionBalance
     * @return
     */
    private InvoiceList getSaleInvoiceDataInSolr(ListingFilterParameter filterParametrs, EdsUser user, boolean isConversionBalance) {
        return getSaleInvoiceSolrResponse(filterParametrs, user, getSaleInvoiceSolrQuery(filterParametrs, user, isConversionBalance));
    }

    private String getSaleInvoiceSolrQuery(ListingFilterParameter filterParametrs, EdsUser user, boolean isConversionBalance) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }

        if (filterParametrs.getStartDateNC() != null) {
            filterParametrs.setStartDate(ServerUtils.parseFilterParameterDate(filterParametrs.getStartDateNC()));
        }
        if (filterParametrs.getEndDateNC() != null) {
            filterParametrs.setEndDate(ServerUtils.parseFilterParameterDate(filterParametrs.getEndDateNC()));
        }

        FacetFilterRpc invoiceFacetFilter = filterParametrs.getFacetFilter();
        if (invoiceFacetFilter != null && !invoiceFacetFilter.isFilterChanges()) {
            invoiceFacetFilter = commonServiceLocal.getUserFacetFilter(invoiceFacetFilter);
        }
        if (invoiceFacetFilter != null) {
            if (invoiceFacetFilter.getSearchKey() != null && !"".equals(invoiceFacetFilter.getSearchKey())) {
                filterParametrs.setSearchKey(invoiceFacetFilter.getSearchKey());
            }
            filterParametrs.setFacetFilter(invoiceFacetFilter);
        }
        String solrQuery = getSaleInvoiceSolrQuery(filterParametrs, user, isConversionBalance, null) + SolrFacetUtils.generateSaleInvoiceDuePaidAmountFacet(invoiceFacetFilter,
                FacetContentType.SaleInvoiceFacetFilter.getContentCode()[2],
                FacetContentType.SaleInvoiceFacetFilter.getContentCode()[3],
                FacetContentType.SaleInvoiceFacetFilter.getContentCode()[7],
                FacetContentType.SaleInvoiceFacetFilter.getContentCode()[9],
                FacetContentType.SaleInvoiceFacetFilter.getContentCode()[10]) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(invoiceFacetFilter, user.getCompany(), SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE, SolrSaleInvoiceRepresenter.FIELD_DUE_DATE,
                        FacetContentType.SaleInvoiceFacetFilter.getContentCode()[2],
                        FacetContentType.SaleInvoiceFacetFilter.getContentCode()[3],
                        FacetContentType.SaleInvoiceFacetFilter.getContentCode()[7],
                        FacetContentType.SaleInvoiceFacetFilter.getContentCode()[9],
                        FacetContentType.SaleInvoiceFacetFilter.getContentCode()[10]
                );
        return solrQuery;
    }

    private InvoiceList getSaleQuoteDataInSolr(ListingFilterParameter filterParametrs, EdsUser user, boolean isConversionBalance) {
        return getSaleQuoteSolrResponse(filterParametrs, user, getSaleQuoteSolrQuery(filterParametrs, user, isConversionBalance));
    }

    private String getSaleQuoteSolrQuery(ListingFilterParameter filterParametrs, EdsUser user, boolean isConversionBalance) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }

        if (filterParametrs.getStartDateNC() != null) {
            filterParametrs.setStartDate(ServerUtils.parseFilterParameterDate(filterParametrs.getStartDateNC()));
        }
        if (filterParametrs.getEndDateNC() != null) {
            filterParametrs.setEndDate(ServerUtils.parseFilterParameterDate(filterParametrs.getEndDateNC()));
        }

        FacetFilterRpc invoiceFacetFilter = filterParametrs.getFacetFilter();
        String selectedDate = null;
        if (invoiceFacetFilter != null && invoiceFacetFilter.getSelectedDateSolrCodeName() != null) {
            selectedDate = "DUE_DATE".equals(invoiceFacetFilter.getSelectedDateSolrCodeName()) ? "DUE_DATE" : "INVOICE_DATE".equals(invoiceFacetFilter.getSelectedDateSolrCodeName()) ? "INVOICE_DATE" : null;
        }
        if (invoiceFacetFilter != null && !invoiceFacetFilter.isFilterChanges()) {
            invoiceFacetFilter = commonServiceLocal.getUserFacetFilter(invoiceFacetFilter);
        }
        if (invoiceFacetFilter != null) {
            if (invoiceFacetFilter.getSearchKey() != null && !"".equals(invoiceFacetFilter.getSearchKey())) {
                filterParametrs.setSearchKey(invoiceFacetFilter.getSearchKey());
            }
            filterParametrs.setFacetFilter(invoiceFacetFilter);
        }
        String solrQuery = getSaleQuoteSolrQuery(filterParametrs, user, isConversionBalance, selectedDate) + SolrFacetUtils.generateSaleInvoiceDuePaidAmountFacet(invoiceFacetFilter,
                FacetContentType.SaleQuoteFacetFilter.getContentCode()[2]) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(
                        invoiceFacetFilter,
                        user.getCompany(),
                        SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE,
                        SolrSaleInvoiceRepresenter.FIELD_DUE_DATE,
                        FacetContentType.SaleQuoteFacetFilter.getContentCode()[2]
                );
        return solrQuery;
    }

    private InvoiceList getSaleOrderDataInSolr(ListingFilterParameter filterParametrs, EdsUser user, boolean isConversionBalance) {
        return getSaleOrderSolrResponse(filterParametrs, user, getSaleOrderSolrQuery(filterParametrs, user, isConversionBalance));
    }

    private String getSaleOrderSolrQuery(ListingFilterParameter filterParametrs, EdsUser user, boolean isConversionBalance) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }

        if (filterParametrs.getStartDateNC() != null) {
            filterParametrs.setStartDate(ServerUtils.parseFilterParameterDate(filterParametrs.getStartDateNC()));
        }
        if (filterParametrs.getEndDateNC() != null) {
            filterParametrs.setEndDate(ServerUtils.parseFilterParameterDate(filterParametrs.getEndDateNC()));
        }

        FacetFilterRpc invoiceFacetFilter = filterParametrs.getFacetFilter();
        if (filterParametrs.getStartDate() != null) {
            invoiceFacetFilter.setStartDate(filterParametrs.getStartDate());
        }
        if (filterParametrs.getEndDate() != null) {
            invoiceFacetFilter.setEndDate(filterParametrs.getEndDate());
        }
        String selectedDate = null;
        if (invoiceFacetFilter != null && invoiceFacetFilter.getSelectedDateSolrCodeName() != null) {
            selectedDate = "DUE_DATE".equals(invoiceFacetFilter.getSelectedDateSolrCodeName()) ? "DUE_DATE" : "INVOICE_DATE".equals(invoiceFacetFilter.getSelectedDateSolrCodeName()) ? "INVOICE_DATE" : null;
        }
        if (invoiceFacetFilter != null && !invoiceFacetFilter.isFilterChanges()) {
            invoiceFacetFilter = commonServiceLocal.getUserFacetFilter(invoiceFacetFilter);
        }
        if (invoiceFacetFilter != null) {
            if (invoiceFacetFilter.getSearchKey() != null && !"".equals(invoiceFacetFilter.getSearchKey())) {
                filterParametrs.setSearchKey(invoiceFacetFilter.getSearchKey());
            }
            filterParametrs.setFacetFilter(invoiceFacetFilter);
        }
        String solrQuery = getSaleOrderSolrQuery(filterParametrs, user, isConversionBalance, selectedDate) + SolrFacetUtils.generateSaleInvoiceDuePaidAmountFacet(invoiceFacetFilter,
                FacetContentType.SaleOrderFacetFilter.getContentCode()[2]) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(
                        invoiceFacetFilter,
                        user.getCompany(),
                        SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE,
                        SolrSaleInvoiceRepresenter.FIELD_DUE_DATE,
                        FacetContentType.SaleOrderFacetFilter.getContentCode()[2]
                );
        return solrQuery;
    }

    private InvoiceList getPurchaseOrderDataInSolr(ListingFilterParameter filterParametrs, EdsUser user, boolean isConversionBalance) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }

        if (filterParametrs.getStartDateNC() != null) {
            filterParametrs.setStartDate(ServerUtils.parseFilterParameterDate(filterParametrs.getStartDateNC()));
        }
        if (filterParametrs.getEndDateNC() != null) {
            filterParametrs.setEndDate(ServerUtils.parseFilterParameterDate(filterParametrs.getEndDateNC()));
        }

        FacetFilterRpc invoiceFacetFilter = filterParametrs.getFacetFilter();
        if (invoiceFacetFilter != null && !invoiceFacetFilter.isFilterChanges()) {
            invoiceFacetFilter = commonServiceLocal.getUserFacetFilter(invoiceFacetFilter);
        }
        if (invoiceFacetFilter != null) {
            if (invoiceFacetFilter.getSearchKey() != null && !"".equals(invoiceFacetFilter.getSearchKey())) {
                filterParametrs.setSearchKey(invoiceFacetFilter.getSearchKey());
            }
            filterParametrs.setFacetFilter(invoiceFacetFilter);
        }
        String solrQuery = getPurchaseOrderSolrQuery(filterParametrs, user, isConversionBalance) + SolrFacetUtils.generateSaleInvoiceDuePaidAmountFacet(invoiceFacetFilter,
                FacetContentType.PurchaseOrderFacetFilter.getContentCode()[2]) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(
                        invoiceFacetFilter,
                        user.getCompany(),
                        SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE,
                        SolrSaleInvoiceRepresenter.FIELD_DUE_DATE,
                        FacetContentType.PurchaseOrderFacetFilter.getContentCode()[2]
                );
        return getPurchaseOrderSolrResponse(filterParametrs, user, solrQuery);
    }

    /**
     * <h1>... This is method generated Sale Invoice Solr Response ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Cretade date {16:38 10/06/2011} ...</h3>
     *
     * @param filterParametrs
     * @param user
     * @param solrQuery
     * @return
     */
    private InvoiceList getSaleInvoiceSolrResponse(ListingFilterParameter filterParametrs, EdsUser user, String solrQuery) {
        Page<SaleInvoiceSolrDoc> saleInvoiceSolrDocs = saleInvoiceSolrComponent.getList(filterParametrs, solrQuery);
        return getSaleInvoiceFromSolrResult(saleInvoiceSolrDocs, user, filterParametrs);
    }

    private InvoiceList getSaleQuoteSolrResponse(ListingFilterParameter filterParameter, EdsUser user, String solrQuery) {
        Page<SaleQuoteSolrDoc> saleQuoteSolrDocPage = saleQuoteSolrComponent.getList(filterParameter, solrQuery);

        return getSaleQuoteFromSolrResult(saleQuoteSolrDocPage, user, filterParameter);
    }

    private InvoiceList getSaleOrderSolrResponse(ListingFilterParameter filterParametrs, EdsUser user, String solrQuery) {
        Page<SaleQuoteSolrDoc> saleQuoteSolrDocPage = saleQuoteSolrComponent.getList(filterParametrs, solrQuery);
        return getSaleOrderFromSolrResult(saleQuoteSolrDocPage, user, filterParametrs);
    }

    private InvoiceList getPurchaseOrderSolrResponse(ListingFilterParameter filterParametrs, EdsUser user, String solrQuery) {
        Page<PurchaseOrderSolrDoc> purchaseOrderSolrDocs = purchaseOrderSolrComponent.getList(filterParametrs, solrQuery);
        return getPurchaseOrderFromSolrResult(purchaseOrderSolrDocs, user, filterParametrs);
    }

    /**
     * <h1>... This is method generate SolrQuery ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T}...</h2>
     * <br/>
     * <h3>... Cretaed date {16:37 10/06/2011} ..</h3>
     *
     * @param filterParametrs
     * @param solrQuery
     * @return
     */
    private SolrQuery getSaleSolrQuery(ListingFilterParameter filterParametrs, String solrQuery) {
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(filterParametrs.getStart());
        if (filterParametrs.getLimit() > 0) {
            query.setParam(CommonParams.ROWS, String.valueOf(filterParametrs.getLimit()));
        } else {
            query.setParam(CommonParams.ROWS, "500");
        }

        if (!filterParametrs.isSearchButton()) {
            if (filterParametrs.getSortField() != null && !"".equals(filterParametrs.getSortField())) {
                SolrQuery.ORDER order = filterParametrs.isAscending() ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc;
                switch (filterParametrs.getSortField()) {
                    case InvoiceList.INVOICE_NUMBER ->
                            query.setSort(SolrSaleInvoiceRepresenter.SORTABLE_INVOICE_NUMBER, order);
                    case InvoiceList.INVOICE_DATE ->
                            query.setSort(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE, order);
                    case InvoiceList.DUE_DATE -> query.setSort(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE, order);
                    case InvoiceList.CLIENT -> query.setSort(SolrSaleInvoiceRepresenter.SORTABLE_CLIENT_NAME, order);
                    case InvoiceList.CURRENCY ->
                            query.setSort(SolrSaleInvoiceRepresenter.SORTABLE_CURRENCY_NAME, order);
                    case InvoiceList.PAID_AMOUNT ->
                            query.setSort(SolrSaleInvoiceRepresenter.FIELD_SORTABLE_PAID_AMOUNT, order);
                    case InvoiceList.DUE_AMOUNT -> query.setSort(SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT, order);
                    case InvoiceList.STATUS -> query.setSort(SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME, order);
                    case InvoiceList.RELATED_PROJECT ->
                            query.setSort(SolrSaleInvoiceRepresenter.SORTABLE_RELATED_PROJECT_NAME, order);
                    case InvoiceList.ORIGINAL_AMOUNT ->
                            query.setSort(SolrSaleInvoiceRepresenter.FIELD_TOTAL_INVOICE_CURRENCY, order);
                    case InvoiceList.BASE_TOTAL ->
                            query.setSort(SolrSaleInvoiceRepresenter.FIELD_TOTAL_INVOICE_BASE, order);
                    case InvoiceList.PO_NUMBER -> query.setSort(SolrSaleInvoiceRepresenter.FIELD_PO_NUMBER, order);
                    case InvoiceList.SUB_TOTAL -> query.setSort(SolrSaleInvoiceRepresenter.FIELD_SUB_TOTAL, order);
                    case InvoiceList.TAX_TOTAL -> query.setSort(SolrSaleInvoiceRepresenter.FIELD_TOTAL_TAXES, order);
                    case InvoiceList.QUOTE_NUMBER ->
                            query.setSort(SolrSaleInvoiceRepresenter.FIELD_QUOTE_NUMBER, order);
                    case InvoiceList.REFERENCE -> query.setSort(SolrSaleInvoiceRepresenter.REFERENCE, order);
                    case InvoiceList.CREATED_DATE ->
                            query.setSort(SolrSaleInvoiceRepresenter.FIELD_CREATED_DATE, order);
                    case InvoiceList.UPDATED_DATE ->
                            query.setSort(SolrSaleInvoiceRepresenter.FIELD_UPDATED_DATE, order);
                    default ->
                            CustomFieldsUtils.setCustomFieldsSortableNameToSolr(filterParametrs.getSortField(), !filterParametrs.isAscending(), query, true);
                }
            } else {
                query.setSort(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID, SolrQuery.ORDER.desc);
            }
        }
        return query;
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
            } else if (InvoiceList.CREATED_DATE.equals(filterParameter.getSortField())) {
                query.setSort(SolrSaleInvoiceRepresenter.FIELD_CREATED_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else if (InvoiceList.UPDATED_DATE.equals(filterParameter.getSortField())) {
                query.setSort(SolrSaleInvoiceRepresenter.FIELD_UPDATED_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
            } else {
                CustomFieldsUtils.setCustomFieldsSortableNameToSolr(filterParameter.getSortField(), !filterParameter.isAscending(), query, true);
            }
        } else {
            query.setSort(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID, SolrQuery.ORDER.desc);
        }
        return query;
    }

    /**
     * <h1>... This is method generated Sale Invoice solr query ...</h1>
     * <br/>
     * <h2>... Changed by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Last Updated {16:34 7/06/2011} ...</h3>
     *
     * @param filterParametrs
     * @param user
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getSaleInvoiceSolrQuery(ListingFilterParameter filterParametrs, EdsUser user, boolean isConversionBalance, String selectedDate) {
        StringBuffer sql = new StringBuffer();
        sql.append(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID).append(":").append(user.getCompany().getObjectID());
        if (user instanceof EdsClientContact) {
            Integer approvedStatusId = referenceManager.findReference(INVOICE_STATUS, APPROVE).getObjectID();
            Integer openStatusId = referenceManager.findReference(INVOICE_STATUS, OPEN).getObjectID();
            Integer paidStatusId = referenceManager.findReference(INVOICE_STATUS, PAID).getObjectID();
            Integer clientApproveStatusId = referenceManager.findReference(INVOICE_STATUS, CLIENT_APPROVE).getObjectID();
            Integer rejectedStatusId = referenceManager.findReference(INVOICE_STATUS, REJECT).getObjectID();
            Integer overDueStatusId = referenceManager.findReference(INVOICE_STATUS, OVER_DUE).getObjectID();
            Integer reversedStatusId = referenceManager.findReference(INVOICE_STATUS, REVERSED).getObjectID();

            sql.append(" AND (");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(user.getClientContact().getClientID());
            sql.append(")");
            sql.append(" AND (");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(approvedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(openStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(paidStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(clientApproveStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(overDueStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(reversedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(rejectedStatusId).append(")");
        }

        if (filterParametrs.getProductId() != null) {
            List<EdsInvoice> list = invoiceManager.getProductInvoice(filterParametrs.getProductId(), Constants.RECEIVABLE);
//            if (list != null && list.size() > 0) {
//                sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(list.stream().map(EdsInvoice::getObjectID).toList(), "0", " ")).append("))");
//            } else {
//                sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + " : " + 0).append(")");
//            }

            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_PRODUCT_ID_INVOICE + " : " + filterParametrs.getProductId()).append(")");
//            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_PRODUCT_ID).append(":(").append(filterParametrs.getProductId()).append(")) ");;
        }

        if (isConversionBalance) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date conversionDate = financialSettingsManager.getFinancialSettings().getConversionDate();
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[ * TO ").append(format.format(conversionDate)).append("]");
            sql.append(")");
        }
        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null && "DUE_DATE".equals(selectedDate)) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sql.append(" AND (((").append(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE).append(":[").append(format.format(filterParametrs.getStartDate())).append(" TO * ]) ");
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE).append(":[ * TO ").append(format.format(filterParametrs.getEndDate())).append(" ])))");
        } else {
            if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null && "INVOICE_DATE".equals(selectedDate)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                if (filterParametrs.getFacetFilter() != null && !ServerUtils.isNullOrEmpty(filterParametrs.getFacetFilter().getSelectedDateSolrCodeName())) {
                    sql.append(" AND (((").append(filterParametrs.getFacetFilter().getSelectedDateSolrCodeName()).append(":[").append(format.format(filterParametrs.getStartDate())).append(" TO * ]) ");
                    sql.append(" AND (").append(filterParametrs.getFacetFilter().getSelectedDateSolrCodeName()).append(":[ * TO ").append(format.format(filterParametrs.getEndDate())).append(" ])))");
                } else {
                    sql.append(" AND (((").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[").append(format.format(filterParametrs.getStartDate())).append(" TO * ]) ");
                    sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[ * TO ").append(format.format(filterParametrs.getEndDate())).append(" ])))");
                }

            } else {
                if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    if (filterParametrs.getFacetFilter() != null && !ServerUtils.isNullOrEmpty(filterParametrs.getFacetFilter().getSelectedDateSolrCodeName())) {
                        sql.append(" AND (((").append(filterParametrs.getFacetFilter().getSelectedDateSolrCodeName()).append(":[").append(format.format(filterParametrs.getStartDate())).append(" TO * ]) ");
                        sql.append(" AND (").append(filterParametrs.getFacetFilter().getSelectedDateSolrCodeName()).append(":[ * TO ").append(format.format(filterParametrs.getEndDate())).append(" ])))");
                    } else {
                        sql.append(" AND (((").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[").append(format.format(filterParametrs.getStartDate())).append(" TO * ]) ");
                        sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[ * TO ").append(format.format(filterParametrs.getEndDate())).append(" ])))");
                    }

                }
            }
        }

        List<String> customAccessRoles = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.ACCOUNTING_SALES_INVOICE_FULL_LIST_ACCESS);
        boolean hasCustomFullAccessToListing = customAccessRoles.size() > 0 && user.hasEitherRoles(customAccessRoles.toArray(new String[]{}));

        if (!(hasCustomFullAccessToListing)) {
            sql.append(" AND (");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID).append(":").append(user.getObjectID());
            sql.append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_FROM_QUOTE_CREATOR_ID).append(":").append(user.getObjectID());
            if (ServerUtils.hasPermission(PermissionConstants.SALES_INVOICE_SEE_OWN)) {
                sql.append(" OR ");
                sql.append(SolrSaleInvoiceRepresenter.FIELD_CUSTOMER_OWNER_ID).append(":").append(user.getObjectID());
            }


            if (roleManager.hasRole(user, PM)) { //Below we are getting access only to PM.
                List<EdsProject> managerProjects = projectManager.getProjectManagersByEmployeeId(user.getObjectID(), false);
                if (!managerProjects.isEmpty()) {
                    sql.append(" OR (");
                    StringBuilder projectIds = new StringBuilder();
                    for (EdsProject p : managerProjects) {
                        projectIds.append(" ").append(p.getObjectID());
                    }
                    sql.append(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID).append(":(").append(projectIds.toString().trim()).append(") ");
                    sql.append(" OR ").append(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_ID).append(":(").append(projectIds.toString().trim()).append(") ");
                    sql.append(")");
                }
            }
            sql.append(")");
        }
        // search key in composite


        if (filterParametrs.getClientId() != null) {
            sql.append("  AND ( ").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(filterParametrs.getClientId());

            if (filterParametrs.getRelationID() != null && filterParametrs.getRelationType() != null) {
                List<Integer> saleInvoiceIds = new ArrayList<>();
                saleInvoiceIds = relationManager.getRelationIDsByType(filterParametrs.getRelationID(), filterParametrs.getEntityID(), filterParametrs.getRelationType(), RelationItem.TYPE_SALEINVOICE);
                if (filterParametrs.getCrmContactId() != null && filterParametrs.getOpportunityID() == null) {
                    sql.append(" OR ").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_ID).append(":").append(filterParametrs.getCrmContactId());
                    sql.append(" OR (").append(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(saleInvoiceIds, "0", " ")).append("))");
                } else {
                    sql.append(" OR (").append(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(saleInvoiceIds, "0", " ")).append("))");
                }
            }
            sql.append(" ) ");

        } else if (filterParametrs.getRelationID() != null && filterParametrs.getRelationType() != null) {
            List<Integer> saleInvoiceIds = new ArrayList<>();
            saleInvoiceIds = relationManager.getRelationIDsByType(filterParametrs.getRelationID(), filterParametrs.getEntityID(), filterParametrs.getRelationType(), RelationItem.TYPE_SALEINVOICE);
            if (filterParametrs.getCrmContactId() != null && filterParametrs.getOpportunityID() == null) {
                sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_ID).append(":").append(filterParametrs.getCrmContactId());
                sql.append(" OR (").append(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(saleInvoiceIds, "0", " ")).append(")))");
            } else {
                sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(saleInvoiceIds, "0", " ")).append("))");
            }
        } else if (filterParametrs.getCrmContactId() != null) {
            sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_ID).append(":").append(filterParametrs.getCrmContactId());
        }
        if (filterParametrs.getProjectId() != null) {
            initProjectSolrQuery(filterParametrs, sql);

            Integer approvedStatusId = referenceManager.findReference(INVOICE_STATUS, APPROVE).getObjectID();
            Integer openStatusId = referenceManager.findReference(INVOICE_STATUS, OPEN).getObjectID();
            Integer overDueStatusId = referenceManager.findReference(INVOICE_STATUS, OVER_DUE).getObjectID();
            Integer paidStatusId = referenceManager.findReference(INVOICE_STATUS, PAID).getObjectID();
            sql.append(" AND (");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(approvedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(openStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(overDueStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(paidStatusId).append(" )");
        }

        if (filterParametrs.getWarehouseID() != null) {
            sql.append(" AND ").append(SolrProductServiceRepresenter.FIELD_WAREHOUSE_ID + ":").append(filterParametrs.getWarehouseID()).append(" ");
        }

        if (filterParametrs.getSearchKey() != null && !"".equals(filterParametrs.getSearchKey())) {
            sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(filterParametrs.getSearchKey()));
            if (!filterParametrs.isLookUp()) {
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(sql, getSaleInvoiceSearchFields(), filterParametrs.getSearchKey());
            }
            sql.append(")");
        }

        sql.append(" AND (");
        sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME).append(":['' TO *])");
        if (filterParametrs.getObjectsIds() != null) {
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).append(":").append(filterParametrs.getObjectsIds().replace(",", " OR " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":")).append(")");
        }
        return sql.toString();
    }

    private void initProjectSolrQuery(ListingFilterParameter filterParametrs, StringBuffer sql) {
        List<Integer> projectIDList = projectManager.getSubProjectIDs(filterParametrs.getProjectId());
        projectIDList.add(filterParametrs.getProjectId());
        sql.append(" AND ");
        if (projectIDList.size() > 1) {
            sql.append("(");
            int i = 0;
            for (Integer projectID : projectIDList) {
                if (i != 0) {
                    sql.append(" OR ");
                }
                sql.append(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID).append(":").append(projectID)
                        .append(" OR ").append(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_ID).append(":").append(projectID);
                i++;
            }
            sql.append(")");
        } else {
            sql.append(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID).append(":").append(filterParametrs.getProjectId())
                    .append(" OR ").append(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_ID).append(":").append(filterParametrs.getProjectId());
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getSaleQuoteSolrQuery(ListingFilterParameter filterParametrs, EdsUser user, boolean isConversionBalance, String selectedDate) {
        StringBuffer sql = new StringBuffer();
        sql.append(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID).append(":").append(user.getCompany().getObjectID());
        Integer revercedStatusId = referenceManager.findReference(INVOICE_STATUS, REVERSED).getObjectID();
        boolean hasContact = false;

        Integer approvedStatusId = referenceManager.findReference(INVOICE_STATUS, APPROVE).getObjectID();
        Integer openStatusId = referenceManager.findReference(INVOICE_STATUS, OPEN).getObjectID();
        Integer clientApproveStatusId = referenceManager.findReference(INVOICE_STATUS, CLIENT_APPROVE).getObjectID();
        Integer rejectedStatusId = referenceManager.findReference(INVOICE_STATUS, REJECT).getObjectID();
        Integer partialInvoicedStatusId = referenceManager.findReference(INVOICE_STATUS, PARTIAL_INVOICED).getObjectID();
        Integer invoicedStatusId = referenceManager.findReference(INVOICE_STATUS, INVOICED).getObjectID();
        Integer convertedStatusId = referenceManager.findReference(INVOICE_STATUS, CONVERTED).getObjectID();

        Integer saleOrderStatusId = referenceManager.findReference(INVOICE_STATUS, SALE_ORDER).getObjectID();
        Integer pickedStatusId = referenceManager.findReference(INVOICE_STATUS, PICKED).getObjectID();
        Integer packedStatusId = referenceManager.findReference(INVOICE_STATUS, PACKED).getObjectID();
        Integer shippedStatusId = referenceManager.findReference(INVOICE_STATUS, SHIPPED).getObjectID();
        Integer partshippedStatusId = referenceManager.findReference(INVOICE_STATUS, PARTIAL_SHIPPED).getObjectID();
        Integer closedStatusId = referenceManager.findReference(INVOICE_STATUS, INVOICE_STATUS_CLOSED).getObjectID();


        if (user instanceof EdsClientContact) {
            sql.append(" AND (");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(user.getClientContact().getClientID());
            sql.append(")");
            sql.append(" AND (");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(approvedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(openStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(clientApproveStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(invoicedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(partialInvoicedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(rejectedStatusId).append(") AND (");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_IS_SALES_ORDER).append(":").append("FALSE)");
        } else {

            sql.append(" AND (");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(convertedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(clientApproveStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(invoicedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(partialInvoicedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(approvedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(openStatusId).append(" OR ");

            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(saleOrderStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(pickedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(packedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(shippedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(partshippedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(closedStatusId).append(" OR ");

            if (filterParametrs.getProjectId() != null && filterParametrs.getProjectId() != 0) {
                sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(saleOrderStatusId).append(" OR ");
                sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(pickedStatusId).append(" OR ");
                sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(packedStatusId).append(" OR ");
                sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(shippedStatusId).append(")");
            } else {
                Integer draftStatusId = referenceManager.findReference(INVOICE_STATUS, DRAFT).getObjectID();
                Integer submittedToManagerId = referenceManager.findReference(INVOICE_STATUS, SUBMITTED_TO_MANAGER).getObjectID();
                Integer managerRejectedStatusId = referenceManager.findReference(INVOICE_STATUS, MANAGER_REJECT).getObjectID();
                sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(draftStatusId).append(" OR ");
                sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(submittedToManagerId).append(" OR ");
                sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(managerRejectedStatusId).append(" OR ");
                sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(rejectedStatusId).append(") AND (");
                sql.append(SolrSaleInvoiceRepresenter.FIELD_IS_SALES_ORDER).append(":FALSE)");
            }
        }

        if (isConversionBalance) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date conversionDate = financialSettingsManager.getFinancialSettings().getConversionDate();
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[ * TO ").append(format.format(conversionDate)).append("]");
            sql.append(")");
        }
        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null && "DUE_DATE".equals(selectedDate)) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sql.append(" AND (((").append(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE).append(":[").append(format.format(filterParametrs.getStartDate())).append(" TO * ]) ");
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE).append(":[ * TO ").append(format.format(filterParametrs.getEndDate())).append(" ])))");
        }
        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null && "INVOICE_DATE".equals(selectedDate)) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sql.append(" AND (((").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[").append(format.format(filterParametrs.getStartDate())).append(" TO * ]) ");
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[ * TO ").append(format.format(filterParametrs.getEndDate())).append(" ])))");
        }
        List<String> customAccessRoles = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.ACCOUNTING_SALES_QUOTE_FULL_LIST_ACCESS);
        boolean hasCustomFullAccessToListing = customAccessRoles.size() > 0 && user.hasEitherRoles(customAccessRoles.toArray(new String[]{}));

        if (!hasCustomFullAccessToListing && !(user instanceof EdsClientContact)) {
            sql.append(" AND (");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID).append(":").append(user.getObjectID());
            sql.append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_CUSTOMER_OWNER_ID).append(":").append(user.getObjectID());

            //Below we are getting access only to PM.

            if (roleManager.hasRole(user, PM)) {
//                List<EdsProject> managerProjects = projectManager.getProjectManagersByEmployeeId(user.getObjectID(), false);
//                StringBuilder projectIds = new StringBuilder();
//                if (!managerProjects.isEmpty()) {
//                    for (EdsProject p : managerProjects) {
//                        projectIds.append(" ").append(p.getObjectID());
//                    }

                    sql.append(" OR (");
                    sql.append(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_MANAGER_ID).append(":(").append(user.getObjectID()).append(") ");
//                    sql.append(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID).append(":(").append(projectIds.toString().trim()).append(") ");
//                    sql.append(" OR ").append(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_ID).append(":(").append(projectIds.toString().trim()).append(") ");
                    sql.append(")");
//                }
            }
            sql.append(")");
        }

        // search key in composite
        if (filterParametrs.getCrmContactId() != null) {
            hasContact = true;
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_ID).append(":").append(filterParametrs.getCrmContactId());
        } else if (filterParametrs.getContactID() != null) {
            hasContact = true;
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_ID).append(":").append(filterParametrs.getContactID());
        } else if (filterParametrs.getLeadID() != null) {
            hasContact = true;
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_ID).append(":").append(filterParametrs.getLeadID());
        } else if (filterParametrs.getClientId() != null) {
            hasContact = true;
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(filterParametrs.getClientId());
        } else if (filterParametrs.getAccountID() != null) {
            hasContact = true;
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(filterParametrs.getAccountID());
        }

        if (filterParametrs.getRelationID() != null && filterParametrs.getRelationType() != null) {
            sql.append(hasContact ? " OR (" : " AND (");
            List<Integer> saleQuoteIDs = relationManager.getRelationIDsByType(filterParametrs.getRelationID(), filterParametrs.getEntityID(), filterParametrs.getRelationType(), RelationItem.TYPE_SALEQUOTE);
            sql.append(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(saleQuoteIDs, "0", " ")).append(")");
            sql.append(hasContact ? "))" : ")");
        } else if (hasContact) {
            sql.append(")");
        }

        if (filterParametrs.getOpportunityID() != null) {
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_ID).append(":").append(filterParametrs.getOpportunityID());
            if (filterParametrs.isConvertedLead() && filterParametrs.getConvertedLeadId() != null) {
                sql.append(" OR ").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_ID).append(":").append(filterParametrs.getConvertedLeadId());
            }
            sql.append(")");
        }

        if (filterParametrs.getProjectId() != null) {
            initProjectSolrQuery(filterParametrs, sql);
        }
        if (filterParametrs.getSearchKey() != null && !"".equals(filterParametrs.getSearchKey())) {
            sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(filterParametrs.getSearchKey()));
            if (!filterParametrs.isLookUp()) {
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(sql, getSalesQuoteSearchFields(), filterParametrs.getSearchKey());
            }
            sql.append(")");
        }

        if (filterParametrs.getProductId() != null) {
            sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_ITEM_ID).append(":").append(filterParametrs.getProductId());
        }
        sql.append(" AND (");
        sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME).append(":['' TO *])");
        sql.append(" AND ");
        sql.append("-" + SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(revercedStatusId);

        if (filterParametrs.getObjectsIds() != null) {
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).append(":").append(filterParametrs.getObjectsIds().replace(",", " OR " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":")).append(")");
        }
        return sql.toString();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getSaleOrderSolrQuery(ListingFilterParameter filterParametrs, EdsUser user, boolean isConversionBalance, String selectedDate) {
        StringBuffer sql = new StringBuffer();
        sql.append(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID).append(":").append(user.getCompany().getObjectID());
        Integer reversedStatusId = referenceManager.findReference(INVOICE_STATUS, REVERSED).getObjectID();

        if (user instanceof EdsClientContact) {
            sql.append(" AND (");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(user.getClientContact().getClientID());
            sql.append(")");
        }

        sql.append(" AND (");
        sql.append(SolrSaleInvoiceRepresenter.FIELD_IS_SALES_ORDER).append(":TRUE)");

        if (isConversionBalance) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date conversionDate = financialSettingsManager.getFinancialSettings().getConversionDate();
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[ * TO ").append(format.format(conversionDate)).append("]");
            sql.append(")");
        }
        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null && "DUE_DATE".equals(selectedDate)) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sql.append(" AND (((").append(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE).append(":[").append(format.format(filterParametrs.getStartDate())).append(" TO * ]) ");
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE).append(":[ * TO ").append(format.format(filterParametrs.getEndDate())).append(" ])))");
        }
        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null && "INVOICE_DATE".equals(selectedDate)) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sql.append(" AND (((").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[").append(format.format(filterParametrs.getStartDate())).append(" TO * ]) ");
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[ * TO ").append(format.format(filterParametrs.getEndDate())).append(" ])))");
        }

        List<String> customAccessRoles = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.ACCOUNTING_SALES_ORDER_FULL_LIST_ACCESS);
        boolean hasCustomFullAccessToListing = customAccessRoles.size() > 0 && user.hasEitherRoles(customAccessRoles.toArray(new String[]{}));

        if (!hasCustomFullAccessToListing && !(user instanceof EdsClientContact)) {
            sql.append(" AND (");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID).append(":").append(user.getObjectID());
            sql.append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_CUSTOMER_OWNER_ID).append(":").append(user.getObjectID());

            //Below we are getting access only to PM.
            if (roleManager.hasRole(user, PM)) {
                sql.append(" OR ");
                sql.append(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_MANAGER_ID).append(":").append(user.getObjectID()).append("");
            }
            sql.append(")");
        }


        // search key in composite
        if (filterParametrs.getCrmContactId() != null) {
            sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_ID).append(":").append(filterParametrs.getCrmContactId());
        }
        if (filterParametrs.getClientId() != null) {
            sql.append(" AND ( ").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(filterParametrs.getClientId());

            if (filterParametrs.getRelationID() != null && filterParametrs.getRelationType() != null) {
                List<Integer> saleOrderIds = new ArrayList<>();
                saleOrderIds = relationManager.getRelationIDsByType(filterParametrs.getRelationID(), filterParametrs.getEntityID(), filterParametrs.getRelationType(), RelationItem.TYPE_SALEORDER);
                sql.append(" OR (").append(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(saleOrderIds, "0", " ")).append("))");
            }

            sql.append(" ) ");
        } else if (filterParametrs.getAccountID() != null) {
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(filterParametrs.getAccountID());

            if (filterParametrs.getRelationID() != null && filterParametrs.getRelationType() != null) {
                List<Integer> saleOrderIds = new ArrayList<>();
                saleOrderIds = relationManager.getRelationIDsByType(filterParametrs.getRelationID(), filterParametrs.getEntityID(), filterParametrs.getRelationType(), RelationItem.TYPE_SALEORDER);
                sql.append(" OR (").append(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(saleOrderIds, "0", " ")).append("))");
            }

            sql.append(" ) ");
        }

        if (filterParametrs.getProductId() != null) {
            sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_ITEM_ID).append(":").append(filterParametrs.getProductId());
        }

        if (filterParametrs.getOpportunityID() != null) {
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_ID).append(":").append(filterParametrs.getOpportunityID());
            if (filterParametrs.isConvertedLead() && filterParametrs.getConvertedLeadId() != null) {
                sql.append(" OR ").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_ID).append(":").append(filterParametrs.getConvertedLeadId());
            }
            sql.append(")");
        }
        if (filterParametrs.getSearchKey() != null && !"".equals(filterParametrs.getSearchKey())) {
            sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(filterParametrs.getSearchKey()));
            if (!filterParametrs.isLookUp()) {
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(sql, getSalesOrderSearchFields(), filterParametrs.getSearchKey());
            }
            sql.append(")");
        }

        sql.append(" AND (");
        sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME).append(":['' TO *])");
        sql.append(" AND ");
        sql.append("-" + SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(reversedStatusId);

        if (filterParametrs.getObjectsIds() != null) {
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).append(":").append(filterParametrs.getObjectsIds().replace(",", " OR " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":")).append(")");
        }
        return sql.toString();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getPurchaseInvoiceCoreSolrQuery(ListingFilterParameter filterParameter, EdsUser user, String selectedDate) {
        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrPurchaseInvoiceRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());

        List<String> customAccessRoles = rolePermissionManager.getRolesByPermissionCode(LayoutRPC.LOGISTICS_SECTION.equals(filterParameter.getModule()) ?
                PermissionConstants.LOGISTICS_PURCHASE_INVOICE_FULL_LIST_ACCESS : PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_FULL_LIST_ACCESS);
        boolean hasCustomFullAccessToListing = customAccessRoles.size() > 0 && user.hasEitherRoles(customAccessRoles.toArray(new String[]{}));

        if (filterParameter.getStartDate() != null && filterParameter.getEndDate() != null && "DUE_DATE".equals(selectedDate)) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            solrQuery.append(" AND (((").append(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE).append(":[").append(format.format(filterParameter.getStartDate())).append(" TO * ]) ");
            solrQuery.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE).append(":[ * TO ").append(format.format(filterParameter.getEndDate())).append(" ])))");
        } else {
            if (filterParameter.getStartDate() != null && filterParameter.getEndDate() != null && "INVOICE_DATE".equals(selectedDate)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                if (filterParameter.getFacetFilter() != null && !ServerUtils.isNullOrEmpty(filterParameter.getFacetFilter().getSelectedDateSolrCodeName())) {
                    solrQuery.append(" AND (((").append(filterParameter.getFacetFilter().getSelectedDateSolrCodeName()).append(":[").append(format.format(filterParameter.getStartDate())).append(" TO * ]) ");
                    solrQuery.append(" AND (").append(filterParameter.getFacetFilter().getSelectedDateSolrCodeName()).append(":[ * TO ").append(format.format(filterParameter.getEndDate())).append(" ])))");
                } else {
                    solrQuery.append(" AND (((").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[").append(format.format(filterParameter.getStartDate())).append(" TO * ]) ");
                    solrQuery.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[ * TO ").append(format.format(filterParameter.getEndDate())).append(" ])))");
                }

            } else {
                if (filterParameter.getStartDate() != null && filterParameter.getEndDate() != null) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    if (filterParameter.getFacetFilter() != null && !ServerUtils.isNullOrEmpty(filterParameter.getFacetFilter().getSelectedDateSolrCodeName())) {
                        solrQuery.append(" AND (((").append(filterParameter.getFacetFilter().getSelectedDateSolrCodeName()).append(":[").append(format.format(filterParameter.getStartDate())).append(" TO * ]) ");
                        solrQuery.append(" AND (").append(filterParameter.getFacetFilter().getSelectedDateSolrCodeName()).append(":[ * TO ").append(format.format(filterParameter.getEndDate())).append(" ])))");
                    } else {
                        solrQuery.append(" AND (((").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[").append(format.format(filterParameter.getStartDate())).append(" TO * ]) ");
                        solrQuery.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[ * TO ").append(format.format(filterParameter.getEndDate())).append(" ])))");
                    }

                }
            }
        }

        if (!hasCustomFullAccessToListing) {
            if (!(user.hasRole(roleManager.getByCode(SUPPLIER)) && user instanceof EdsClientContact)) {
                solrQuery.append(" AND (");
                solrQuery.append(SolrPurchaseInvoiceRepresenter.FIELD_CLIENT_OWNER_ID).append(":").append(user.getObjectID());
                solrQuery.append(" OR ");
                solrQuery.append(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID).append(":").append(user.getObjectID());
                solrQuery.append(")");
            }


            if (user.hasRole(roleManager.getByCode(SUPPLIER)) && user instanceof EdsClientContact) {
                Integer approveStatusId = referenceManager.findReference(INVOICE_STATUS, APPROVE).getObjectID();
                Integer openStatusId = referenceManager.findReference(INVOICE_STATUS, OPEN).getObjectID();
                Integer paidStatusId = referenceManager.findReference(INVOICE_STATUS, PAID).getObjectID();
                Integer overdueStatusId = referenceManager.findReference(INVOICE_STATUS, OVER_DUE).getObjectID();
                Integer reversedStatusId = referenceManager.findReference(INVOICE_STATUS, REVERSED).getObjectID();

                solrQuery.append(" AND (");
                solrQuery.append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(user.getClientContact().getClientID());
                solrQuery.append(")");
                solrQuery.append(" AND (");
                solrQuery.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(approveStatusId).append(" OR ");
                solrQuery.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(openStatusId).append(" OR ");
                solrQuery.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(paidStatusId).append(" OR ");
                solrQuery.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(reversedStatusId).append(" OR ");
                solrQuery.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(overdueStatusId).append(")");
            }
        }
        if (filterParameter.getProductId() != null) {
            solrQuery.append(" AND ").append(SolrPurchaseInvoiceRepresenter.FIELD_ITEM_ID).append(":").append(filterParameter.getProductId()).append(" ");
        }
        if (filterParameter.getClientId() != null) {
            solrQuery.append(" AND ( ").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(filterParameter.getClientId());

            if (filterParameter.getRelationID() != null && filterParameter.getRelationType() != null) {
                List<Integer> purchaseInvoiceIds = new ArrayList<>();
                purchaseInvoiceIds = relationManager.getRelationIDsByType(filterParameter.getRelationID(), filterParameter.getEntityID(), filterParameter.getRelationType(), RelationItem.TYPE_PURCHASE_INVOICE);
                if (purchaseInvoiceIds != null && purchaseInvoiceIds.size() > 0) {
                    solrQuery.append(" OR (").append(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(purchaseInvoiceIds, "0", " ")).append("))");
                }
            }
            solrQuery.append(" ) ");

        } else if (filterParameter.getRelationID() != null && filterParameter.getRelationType() != null) {
            List<Integer> purchaseInvoiceIds = new ArrayList<>();
            purchaseInvoiceIds = relationManager.getRelationIDsByType(filterParameter.getRelationID(), filterParameter.getEntityID(), filterParameter.getRelationType(), RelationItem.TYPE_PURCHASE_INVOICE);
            solrQuery.append(" AND (").append(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(purchaseInvoiceIds, "0", " ")).append("))");
        }

        if (filterParameter.getWarehouseID() != null) {
            solrQuery.append(" AND ").append(SolrProductServiceRepresenter.FIELD_WAREHOUSE_ID + ":").append(filterParameter.getWarehouseID()).append(" ");
        }

        if (filterParameter.getSearchKey() != null && !"".equals(filterParameter.getSearchKey())) {
            solrQuery.append(" AND ").append(SolrPurchaseInvoiceRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(filterParameter.getSearchKey()));
            if (!filterParameter.isLookUp()) {
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(solrQuery, getPurchaseInvoiceSearchFields(), filterParameter.getSearchKey());
            }
            solrQuery.append(")");
        }
        if (filterParameter.getObjectsIds() != null) {
            solrQuery.append(" AND (").append(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID).append(":").append(filterParameter.getObjectsIds().replace(",", " OR " + SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID + ":")).append(")");
        }
        return solrQuery.toString();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getPurchaseOrderSolrQuery(ListingFilterParameter filterParametrs, EdsUser user, boolean isConversionBalance) {
        StringBuffer sql = new StringBuffer();
        sql.append(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        Integer reversedStatusId = referenceManager.findReference(INVOICE_STATUS, REVERSED).getObjectID();

        if (isConversionBalance) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date conversionDate = financialSettingsManager.getFinancialSettings().getConversionDate();
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[ * TO ").append(format.format(conversionDate)).append("]");
            sql.append(")");
        }
        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sql.append(" AND (((").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[").append(format.format(filterParametrs.getStartDate())).append(" TO * ]) ");
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE).append(":[ * TO ").append(format.format(filterParametrs.getEndDate())).append(" ])))");
        }
        if (user.hasRole(roleManager.getByCode(Constants.SUPPLIER)) && user instanceof EdsClientContact) {
            Integer approveStatusId = referenceManager.findReference(INVOICE_STATUS, APPROVE).getObjectID();
            Integer openStatusId = referenceManager.findReference(INVOICE_STATUS, OPEN).getObjectID();
            Integer convertedStatusId = referenceManager.findReference(INVOICE_STATUS, CONVERTED).getObjectID();
            Integer invoicedStatusId = referenceManager.findReference(INVOICE_STATUS, INVOICED).getObjectID();

            sql.append(" AND (");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(user.getClientContact().getClientID());
            sql.append(")");
            sql.append(" AND (");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(approveStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(openStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(convertedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(invoicedStatusId).append(")");
        }

        List<String> customAccessRoles = rolePermissionManager.getRolesByPermissionCode(LayoutRPC.LOGISTICS_SECTION.equals(filterParametrs.getModule()) ?
                PermissionConstants.LOGISTICS_PURCHASE_ORDER_FULL_LIST_ACCESS : PermissionConstants.ACCOUNTING_PURCHASE_ORDER_FULL_LIST_ACCESS);
        boolean hasCustomFullAccessToListing = customAccessRoles.size() > 0 && user.hasEitherRoles(customAccessRoles.toArray(new String[]{}));

        //Below we are getting access only to PM.
        if (!(roleManager.hasEitherRoles(user, ACCOUNTANT, DR, ADMIN) || hasCustomFullAccessToListing) && !(user.hasRole(roleManager.getByCode(Constants.SUPPLIER)) && user instanceof EdsClientContact)) {
            sql.append(" AND (");
            sql.append(SolrPurchaseInvoiceRepresenter.FIELD_CLIENT_OWNER_ID).append(":").append(user.getObjectID());
            sql.append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID).append(":").append(user.getObjectID());
            sql.append(")");

            List<EdsProject> managerProjects = projectManager.getProjectManagersByEmployeeId(user.getObjectID(), false);
            if (!managerProjects.isEmpty()) {
                sql.append(" OR (");
            }
            for (EdsProject p : managerProjects) {
                sql.append(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID).append(":").append(p.getObjectID())
                        .append(" OR ").append(SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_ID).append(":").append(p.getObjectID());

                if (!p.getObjectID().equals(managerProjects.get(managerProjects.size() - 1).getObjectID())) {
                    sql.append(" OR ");
                }
            }
            if (!managerProjects.isEmpty()) {
                sql.append(")");
            }
        }
        // search key in composite
        if (filterParametrs.getCrmContactId() != null) {
            sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_CONTACT_ID).append(":").append(filterParametrs.getCrmContactId());
        }
        if (filterParametrs.getProductId() != null) {
            sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_ITEM_ID).append(":").append(filterParametrs.getProductId());
        }

        if (filterParametrs.getClientId() != null) {
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID).append(":").append(filterParametrs.getClientId());

            if (filterParametrs.getRelationID() != null && filterParametrs.getRelationType() != null) {
                List<Integer> purchaseorderIds = new ArrayList<>();
                purchaseorderIds = relationManager.getRelationIDsByType(filterParametrs.getRelationID(), filterParametrs.getEntityID(), filterParametrs.getRelationType(), RelationItem.TYPE_PURCHASE_ORDER);
                if (purchaseorderIds != null && purchaseorderIds.size() > 0) {
                    sql.append(" OR (").append(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(purchaseorderIds, "0", " ")).append("))");
                }
            }
            sql.append(" ) ");

        } else if (filterParametrs.getRelationID() != null && filterParametrs.getRelationType() != null) {
            List<Integer> purchaseorderIds = new ArrayList<>();
            purchaseorderIds = relationManager.getRelationIDsByType(filterParametrs.getRelationID(), filterParametrs.getEntityID(), filterParametrs.getRelationType(), RelationItem.TYPE_PURCHASE_ORDER);
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(purchaseorderIds, "0", " ")).append("))");
        }

        if (filterParametrs.getProjectId() != null) {
            initProjectSolrQuery(filterParametrs, sql);

            Integer convertedStatusId = referenceManager.findReference(INVOICE_STATUS, CONVERTED).getObjectID();
            Integer receivedStatusId = referenceManager.findReference(INVOICE_STATUS, RECEIVED).getObjectID();
            Integer partialReceivedStatusId = referenceManager.findReference(INVOICE_STATUS, PARTIAL_RECEIVED).getObjectID();
            Integer approveStatusId = referenceManager.findReference(INVOICE_STATUS, APPROVE).getObjectID();
            Integer invoicedStatusId = referenceManager.findReference(INVOICE_STATUS, INVOICED).getObjectID();
            Integer openStatusId = referenceManager.findReference(INVOICE_STATUS, OPEN).getObjectID();

            sql.append(" AND (");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(convertedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(invoicedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(partialReceivedStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(approveStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(openStatusId).append(" OR ");
            sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(receivedStatusId).append(" )");
        }

        if (StringUtils.isNotBlank(filterParametrs.getSearchKey())) {
            if (filterParametrs.isFromMobile()) {
                sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_INVOICE_NUMBER).append(":(").append(QueryBuilderForSolr.normalaizeKeyword(filterParametrs.getSearchKey())).append(")");
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateApiSearchQuery(sql, QueryBuilderForSolr.getApiSearchFields(), filterParametrs.getSearchKey());
                sql.append(")");
            } else {
                sql.append(" AND ").append(SolrSaleInvoiceRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(filterParametrs.getSearchKey()));
                if (!filterParametrs.isLookUp()) {
                    SolrSearchUtils searchUtils = new SolrSearchUtils();
                    searchUtils.generateSearchQuery(sql, getPurchaseOrderSearchFields(), filterParametrs.getSearchKey());
                }
                sql.append(")");
            }
        }

        sql.append(" AND (");
        sql.append(SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME).

                append(":['' TO *])");
        sql.append(" AND ");
        sql.append("-" + SolrSaleInvoiceRepresenter.FIELD_STATUS_ID).append(":").append(reversedStatusId);
        if (filterParametrs.getObjectsIds() != null) {
            sql.append(" AND (").append(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).append(":").append(filterParametrs.getObjectsIds().replace(",", " OR " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":")).append(")");
        }
        return sql.toString();
    }

    private InvoiceList getSaleQuoteFromSolrResult(Page<SaleQuoteSolrDoc> saleQuoteSolrDocPage, EdsUser edsUser, com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter filterParametrs) {
        EdsUser user = roleManager.getUser();
        boolean hasOnlySalesPersonRole = roleManager.hasOnlySalesPersonRole(user);
        Set<GenericSettingsEnum> genericSettings = genericSettingsManager.getEnabledGenericSettings();
        boolean isWarehouseAllocationEnabled = genericSettings.contains(GenericSettingsEnum.WAREHOUSE_ALLOCATION_ENABLE);
        boolean isProjectInLine = genericSettings.contains(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);
        boolean isMultiQuoteConvertEnabled = genericSettings.contains(GenericSettingsEnum.ENABLE_MULTI_QUOTE_CONVERT);
        boolean isFromListing = filterParametrs.isFromListing();
        int totalCount = isWarehouseAllocationEnabled && hasOnlySalesPersonRole ? 0 : (int) saleQuoteSolrDocPage.getTotalElements();

        // adding solr collapsed results to map
        ListPanelToolRpc panelSettings = filterParametrs.getListPanelTool();
        ArrayList<NewInvoice> quoteList = new ArrayList<>();

        EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(user.getCompany());
        boolean isConvertInvoiceBtnShow = false;
        if (invSettings != null) {
            isConvertInvoiceBtnShow = invSettings.isConvertInvoiceBtnShow();
        }
        for (SaleQuoteSolrDoc relevantDoc : saleQuoteSolrDocPage.getContent()) {
            NewInvoice quote = new NewInvoice();

            if (relevantDoc.getSaleInvoiceId() != null) {
                if (filterParametrs.isWithEncryptedLink()) {
                    quote.setEncryptedLink(EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("salequote|summary/" + relevantDoc.getSaleInvoiceId())));
                }
                quote.setID(relevantDoc.getSaleInvoiceId());
                quote.setInvoiceNumber(relevantDoc.getInvoiceNumber());
                quote.setInvoiceDate(new DateNonConvertable(relevantDoc.getInvoiceDate()));
                quote.setDueDate(new DateNonConvertable(relevantDoc.getDueDate()));
                quote.setClientID(relevantDoc.getClientId());
                quote.setClientName(relevantDoc.getClientName());
                quote.setClientContactID(relevantDoc.getClientContactId());
                quote.setClientContactEmail(relevantDoc.getClientContactEmail());
                quote.setCurrencyID(relevantDoc.getCurrencyId());
                quote.setCurrencyName(relevantDoc.getCurrencyName());
                quote.setRelatedProjectID(relevantDoc.getRelatedProjectId());

                quote.setRelatedProjectName(getSQProjectName(isProjectInLine, relevantDoc));

                quote.setProjectStatusCode(relevantDoc.getRelatedProjectCode());
                quote.setProgressInvoicing(relevantDoc.getProgressInvoicing());
                quote.setConvertInvoiceBtnShow(isConvertInvoiceBtnShow);
                quote.setTotalInInvoiceCurrency(BigDecimal.valueOf(relevantDoc.getTotalInvoiceCurrency()));
                quote.setTotal(BigDecimal.valueOf(relevantDoc.getTotalInvoiceBase()));
                quote.setTotalTaxes(BigDecimal.valueOf(relevantDoc.getTotalTaxes()));
                quote.setSubtotal(BigDecimal.valueOf(relevantDoc.getSubTotal()));
                quote.setExchageRate(BigDecimal.valueOf(relevantDoc.getExchargeRate()));
                quote.setDuePayments(BigDecimal.valueOf(relevantDoc.getDueAmount()));
                quote.setOpportunityNumber(relevantDoc.getOpportunityNumber());
                quote.setStatus(referenceWfmMessageSource.localize(relevantDoc.getStatusCode(), relevantDoc.getStatusName()));
                quote.setStatusCode(relevantDoc.getStatusCode());
                quote.setStatusID(relevantDoc.getStatusId());

                quote.setOpportunityID(relevantDoc.getOpportunityId());
                quote.setStatus(referenceWfmMessageSource.localize(relevantDoc.getStatusCode(), relevantDoc.getStatusName()));
                quote.setStatusCode(relevantDoc.getStatusCode());
                quote.setStatusID(relevantDoc.getStatusId());
                quote.setCreationDate(relevantDoc.getCreatedDate());
                Integer approverId = relevantDoc.getCurrentApproverId();
                if (approverId != null) {
                    quote.setCurrentApproverSelectItem(new SelectItem(approverId, relevantDoc.getCurrentApproverName()));
                }

                quote.setIntroduction(relevantDoc.getIntroduction());
                quote.setReference(relevantDoc.getReference());
                quote.setPoNumber(relevantDoc.getPoNumber());

                //Need to discuss whether to show in listing or not
//                if (!isFromListing && SolrUtils.asBoolean(relevantDoc, SolrSaleInvoiceRepresenter.IS_PROGRESS_INVOICING)) {
//                    NewInvoice quoteObject = getQuote((Integer) relevantDoc.getFieldValue(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID), null);
//                    NewInvoiceItem[] quoteItems = quoteObject != null ? quoteObject.getItems() : null;
//                    quote.setItems(quoteItems);
//                }
//                if (isFromListing && SolrUtils.asBoolean(relevantDoc, SolrSaleInvoiceRepresenter.IS_PROGRESS_INVOICING)) {
//                    if (!isMultiQuoteConvertEnabled) {
//                        quote.setInvoicedItemsExist(invoiceManager.hasConvertedItems(quote.getID()));
//                    }
//                }
                quote.setPdfTemplateID(relevantDoc.getPdfTemplateId());
                Integer creatorID = relevantDoc.getCreatorId();
                if (creatorID != null) {
                    quote.setCreator(new SelectItem(creatorID, relevantDoc.getCreatorName()));
                }
                Integer taxCalculationType = relevantDoc.getTaxCalculationType();
                quote.setAmount(quote.getTotalInInvoiceCurrency() != null && taxCalculationType != null && taxCalculationType.equals(TAX_CALCULATION_INCLUSIVE) ? quote.getTotalInInvoiceCurrency().subtract(quote.getTotalTaxes() != null ? quote.getTotalTaxes() : BigDecimal.ZERO) : quote.getTotalInInvoiceCurrency());
                if (edsUser instanceof EdsClientContact) {
                    quote.setClient(true);
                }

                //init sale-quote custom fields
                if (panelSettings != null) {
                    quote.setCustomFieldMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(relevantDoc, panelSettings.getColumnCodeName()));
                }
                if (isWarehouseAllocationEnabled && hasOnlySalesPersonRole) {
                    if (quote.getCreator() != null && user.getObjectID().equals(quote.getCreator().getId())) {
                        quoteList.add(quote);
                        totalCount++;
                    }
                } else {
                    quoteList.add(quote);
                }

            }
        }
        return new InvoiceList(quoteList, totalCount);
    }

    private ListResultTO<OrderDto> getSaleQuoteFromSolrResult(QueryResponse resp) {
        String Ids = resp.getResults().stream().map(doc -> String.valueOf(SolrUtils.asInteger(doc, SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID))).collect(Collectors.joining(","));
        ArrayList<OrderDto> items = new ArrayList<>();
        if (StringUtils.isNotBlank(Ids)) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = commonServiceLocal.getCompanyCustomFields(ViewName.SaleQuote);
            ArrayList<CompanyCustomFieldItem> lineItemCustomFieldItems = commonServiceLocal.getCompanyCustomFields(ViewName.SaleQuoteItem);

            List<EdsSaleQuote> invoiceList = quoteManager.getSaleQuotesByIds(Ids);
            for (EdsSaleQuote saleQuote : invoiceList) {
                saleQuote.setItemCustomFields(lineItemCustomFieldItems);
                items.add(wrapQuoteToDto(saleQuote, customFieldItems));
            }
        }
        return new ListResultTO<>((int) resp.getResults().getNumFound(), items);
    }


    private InvoiceList getSaleOrderFromSolrResult(Page<SaleQuoteSolrDoc> saleQuoteSolrDocPage, EdsUser user, ListingFilterParameter filterParametrs) {
        boolean hasOnlySalesPersonRole = roleManager.hasOnlySalesPersonRole(user);
        boolean isWarehouseAllocationEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.WAREHOUSE_ALLOCATION_ENABLE);
        boolean isProjectInLine = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);
        String pickedStatusId = referenceManager.findReference(INVOICE_STATUS, PICKED).getCode();
        String packedStatusId = referenceManager.findReference(INVOICE_STATUS, PACKED).getCode();
        String shippedStatusId = referenceManager.findReference(INVOICE_STATUS, SHIPPED).getCode();
        String saleOrderStatusId = referenceManager.findReference(INVOICE_STATUS, SALE_ORDER).getCode();

        // adding solr collapsed results to map
        ListPanelToolRpc panelSettings = filterParametrs.getListPanelTool();

        int totalCount = isWarehouseAllocationEnabled && hasOnlySalesPersonRole ? 0 : (int) saleQuoteSolrDocPage.getTotalElements();

        // adding solr collapsed results to map
        ArrayList<NewInvoice> orderList = new ArrayList<>();
        String quoteIds = saleQuoteSolrDocPage.stream().filter(doc -> doc.getSaleInvoiceId() != null)
                .map(doc -> doc.getSaleInvoiceId().toString()).collect(Collectors.joining(","));
        HashMap<Integer, BigDecimal> invoicedAmounts = invoiceManager.getConvertedInvoiceAmountsForListing(quoteIds);

        for (SaleQuoteSolrDoc relevantDoc : saleQuoteSolrDocPage.getContent()) {
            NewInvoice order = new NewInvoice();
            if (relevantDoc != null) {
                if (filterParametrs.isWithEncryptedLink()) {
                    order.setEncryptedLink(EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("salequote|summary/" + relevantDoc.getSaleInvoiceId())));
                }
                String creator = "";
                Integer creatorId = relevantDoc.getCreatorId();
                if (creatorId != null) {
                    creator = relevantDoc.getCreatorName();
                    order.setCreator(new SelectItem(creatorId, creator));
                }
                order.setID(relevantDoc.getSaleInvoiceId());
                order.setInvoiceNumber(relevantDoc.getInvoiceNumber());
                order.setInvoiceDate(new DateNonConvertable(relevantDoc.getInvoiceDate()));
                order.setDueDate(new DateNonConvertable(relevantDoc.getDueDate()));
                order.setClientID(relevantDoc.getClientId());
                order.setClientName(relevantDoc.getClientName());
                order.setClientContactID(relevantDoc.getClientContactId());
                order.setClientContactEmail(relevantDoc.getClientContactEmail());
                order.setCurrencyID(relevantDoc.getCurrencyId());
                order.setCurrencyName(relevantDoc.getCurrencyName());
                order.setNetAmountTotal(BigDecimal.valueOf(relevantDoc.getNetAmountTotal()));
                order.setCreatorName(creator);
                order.setRelatedProjectID(relevantDoc.getRelatedProjectId());
                if (isProjectInLine) {
                    order.setRelatedProjectName(ServerUtils.asListToString(relevantDoc.getMultiProjectNumberName()));
                } else {
                    String number = relevantDoc.getRelatedProjectNumber();
                    String name = relevantDoc.getRelatedProjectName();
                    if (!ServerUtils.isNullOrEmpty(number) && !ServerUtils.isNullOrEmpty(name)) {
                        String projectNumberName = number + SolrSaleInvoiceRepresenter.ARROW + name;
                        order.setRelatedProjectName(projectNumberName);
                    }
                }
                if (relevantDoc.getRelatedProjectId() != null && !isProjectInLine) {
                    order.setProjectStatusCode(relevantDoc.getRelatedProjectCode());
                }
                order.setOpportunity(relevantDoc.getOpportunityNumber());
                order.setOpportunityID(relevantDoc.getOpportunityId());
                order.setReference(relevantDoc.getReference());
                order.setTotalInInvoiceCurrency(BigDecimal.valueOf(relevantDoc.getTotalInvoiceCurrency()));
                order.setDuePayments(BigDecimal.valueOf(relevantDoc.getDueAmount()));
                String statusCode = relevantDoc.getStatusCode();
                String statusName = relevantDoc.getStatusName();
                order.setStatus(referenceWfmMessageSource.localize(statusCode, statusName));
                order.setStatusCode(statusCode);
                order.setStatusID(relevantDoc.getStatusId());
                order.setPickListID(relevantDoc.getPicklistId());
                order.setPoNumber(relevantDoc.getPoNumber());
                order.setSalesOrder(relevantDoc.getSalesOrder());
                order.setCreationDate(relevantDoc.getCreatedDate());
                if (user instanceof EdsClientContact) {
                    order.setClient(true);
                }
                Integer approverId = relevantDoc.getCurrentApproverId();
                if (approverId != null) {
                    order.setCurrentApproverSelectItem(new SelectItem(approverId, relevantDoc.getCurrentApproverName()));
                }
                order.setTotal(BigDecimal.valueOf(relevantDoc.getTotalInvoiceBase()));
                order.setTotalTaxes(BigDecimal.valueOf(relevantDoc.getTotalTaxes()));
                order.setSubtotal(BigDecimal.valueOf(relevantDoc.getSubTotal()));
                Integer taxCalculationType = relevantDoc.getTaxCalculationType();
                order.setAmount(order.getTotalInInvoiceCurrency() != null && taxCalculationType != null && taxCalculationType.equals(TAX_CALCULATION_INCLUSIVE) ? order.getTotalInInvoiceCurrency().subtract(order.getTotalTaxes() != null ? order.getTotalTaxes() : BigDecimal.ZERO) : order.getTotalInInvoiceCurrency());
                if ((order.getStatus() != null && (pickedStatusId.equals(order.getStatusCode())
                        || packedStatusId.equals(order.getStatusCode())
                        || shippedStatusId.equals(order.getStatusCode())
                        || saleOrderStatusId.equals(order.getStatusCode())))
                        || order.isSalesOrder()) {
                    if (isWarehouseAllocationEnabled && hasOnlySalesPersonRole) {
                        if (order.getCreator() != null && user.getObjectID().equals(order.getCreator().getId())) {
                            orderList.add(order);
                            totalCount++;
                        }
                    } else {
                        orderList.add(order);
                    }
                }

                order.setProgressInvoicing(relevantDoc.getProgressInvoicing());
                //Need to discuss whether to show in list or not
                if (invoicedAmounts != null) {
                    order.setInvoicedAmount(invoicedAmounts.get(relevantDoc.getSaleInvoiceId()));
                }
                if (panelSettings != null) {
                    order.setCustomFieldMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(relevantDoc, panelSettings.getColumnCodeName()));
                }
            }
        }
        return new InvoiceList(orderList, totalCount);
    }

    private ListResultTO<OrderDto> getSaleOrderFromSolrResult(QueryResponse resp) {
        String Ids = resp.getResults().stream().map(doc -> String.valueOf(SolrUtils.asInteger(doc, SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID))).collect(Collectors.joining(","));
        ArrayList<OrderDto> items = new ArrayList<>();
        if (StringUtils.isNotBlank(Ids)) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = commonServiceLocal.getCompanyCustomFields(ViewName.SaleOrder);
            ArrayList<CompanyCustomFieldItem> lineItemCustomFieldItems = commonServiceLocal.getCompanyCustomFields(ViewName.SaleOrderItem);

            List<EdsSaleQuote> invoiceList = quoteManager.getSaleQuotesByIds(Ids);
            for (EdsSaleQuote saleOrder : invoiceList) {
                saleOrder.setItemCustomFields(lineItemCustomFieldItems);
                items.add(wrapQuoteToDto(saleOrder, customFieldItems));
            }
        }
        return new ListResultTO<>((int) resp.getResults().getNumFound(), items);
    }

    private InvoiceList getPurchaseOrderFromSolrResult(Page<PurchaseOrderSolrDoc> purchaseOrderSolrDocs, EdsUser edsUser, com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter filterParametrs) {
        EdsUser user = roleManager.getUser();
        boolean hasOnlySalesPersonRole = roleManager.hasOnlySalesPersonRole(user);
        boolean isWarehouseAllocationEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.WAREHOUSE_ALLOCATION_ENABLE);
        boolean isProjectInLine = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);

        int totalCount = isWarehouseAllocationEnabled && hasOnlySalesPersonRole ? 0 : (int) purchaseOrderSolrDocs.getTotalElements();

        // adding solr collapsed results to map
        ListPanelToolRpc panelSettings = filterParametrs.getListPanelTool();
        ArrayList<NewInvoice> orderList = new ArrayList<>();

        for (PurchaseOrderSolrDoc relevantDoc : purchaseOrderSolrDocs.getContent()) {
            NewInvoice order = new NewInvoice();
            if (relevantDoc != null) {
                if (filterParametrs.isWithEncryptedLink()) {
                    order.setEncryptedLink(EncryptionHelper.encodeURL(EncryptionHelper.encryptURL(PURCHASE_ORDER + "|summary/" + relevantDoc.getSaleInvoiceId())));
                }
                Integer clientId = relevantDoc.getCustomerId();
                if (clientId != null) {
                    EdsCrmAccount crmAccount = crmAccountManager.get(clientId);
                    order.setCustomerName(crmAccount != null ? crmAccount.getName() : "");
                }

                order.setID(relevantDoc.getSaleInvoiceId());
                order.setInvoiceNumber(relevantDoc.getInvoiceNumber());
                order.setInvoiceDate(new DateNonConvertable(relevantDoc.getInvoiceDate()));
                order.setDueDate(new DateNonConvertable(relevantDoc.getDueDate()));
                order.setClientID(relevantDoc.getClientId());
                order.setClientName(relevantDoc.getClientName());
                order.setCurrencyID(relevantDoc.getCurrencyId());
                order.setCurrencyName(relevantDoc.getCurrencyName());
                order.setRelatedProjectID(relevantDoc.getRelatedProjectId());
                order.setRelatedProjectName(getProjectNameOrder(isProjectInLine, relevantDoc));
                order.setTotalTaxesInInvoiceCurrency(BigDecimal.valueOf(relevantDoc.getTotalTaxes()));
                order.setTotal(BigDecimal.valueOf(relevantDoc.getTotalInvoiceBase()));
                order.setReference(relevantDoc.getReference());

                if (relevantDoc.getRelatedProjectId() != null && !isProjectInLine) {
                    String projectNumber = relevantDoc.getRelatedProjectNumber();
                    String projectName = relevantDoc.getRelatedProjectName();
                    if (projectNumber != null) {
                        order.setRelatedProjectName(projectNumber + " -> " + projectName);
                    } else {
                        order.setRelatedProjectName(projectName);
                    }
                    order.setProjectStatusCode(relevantDoc.getRelatedProjectCode());
                }
                order.setTotalInInvoiceCurrency(BigDecimal.valueOf(relevantDoc.getTotalInvoiceCurrency()));
                order.setDuePayments(BigDecimal.valueOf(relevantDoc.getDueAmount()));
                String statusCode = relevantDoc.getStatusCode();
                order.setStatus(referenceWfmMessageSource.localize(statusCode, relevantDoc.getStatusName()));
                order.setStatusCode(statusCode);
                order.setQuoteNumber(relevantDoc.getQuoteNumber());

                order.setOpportunityID(relevantDoc.getOpportunityId());
                order.setOpportunityNumber(relevantDoc.getOpportunityNumber());

                Integer approverId = relevantDoc.getCurrentApproverId();
                if (approverId != null) {
                    order.setCurrentApproverSelectItem(new SelectItem(approverId, relevantDoc.getCurrentApproverName()));
                }

                if (edsUser instanceof EdsClientContact) {
                    order.setClient(true);
                }
                //order.setTotal(purchaseOrder.getTotal());
                order.setSubtotal(BigDecimal.valueOf(relevantDoc.getSubTotal()));
                order.setTotalTaxes(BigDecimal.valueOf(relevantDoc.getTotalTaxes()));
                order.setExchageRate(BigDecimal.valueOf(relevantDoc.getExchargeRate()));
                Integer creatorId = relevantDoc.getCreatorId();
                if (creatorId != null) {
                    order.setCreator(new SelectItem(creatorId, relevantDoc.getCreatorName()));
                }
                Integer managerId = relevantDoc.getManagerId();
                if (managerId != null) {
                    order.setPurchaseOrderManager(new SelectItem(managerId, relevantDoc.getManagerName()));
                }
                if (panelSettings != null) {
                    order.setCustomFieldMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(relevantDoc, panelSettings.getColumnCodeName()));
                }
                Integer taxCalculationType = relevantDoc.getTaxCalculationType();
                order.setAmount(order.getTotalInInvoiceCurrency() != null && taxCalculationType != null && taxCalculationType.equals(TAX_CALCULATION_INCLUSIVE) ? order.getTotalInInvoiceCurrency().subtract(order.getTotalTaxes() != null ? order.getTotalTaxes() : BigDecimal.ZERO) : order.getTotalInInvoiceCurrency());

                if (isWarehouseAllocationEnabled && hasOnlySalesPersonRole) {
                    if (order.getCreator() != null && user.getObjectID().equals(order.getCreator().getId())) {
                        orderList.add(order);
                        totalCount++;
                    }
                } else {
                    orderList.add(order);
                }
            }
        }
        return new InvoiceList(orderList, totalCount);
    }

    private ListResultTO<OrderDto> getPurchaseOrderFromSolrResult(QueryResponse resp) {
        String Ids = resp.getResults().stream().map(doc -> String.valueOf(SolrUtils.asInteger(doc, SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID))).collect(Collectors.joining(","));
        ArrayList<OrderDto> items = new ArrayList<>();
        if (StringUtils.isNotBlank(Ids)) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = commonServiceLocal.getCompanyCustomFields(ViewName.PurchaseOrder);
            ArrayList<CompanyCustomFieldItem> lineItemCustomFieldItems = commonServiceLocal.getCompanyCustomFields(ViewName.PurchaseOrderItem);

            List<EdsPurchaseOrder> invoiceList = quoteManager.getPurchaseOrdersByIds(Ids);
            for (EdsPurchaseOrder purchaseOrder : invoiceList) {
                purchaseOrder.setItemCustomFields(lineItemCustomFieldItems);
                items.add(wrapPurchaseOrderToDto(purchaseOrder, customFieldItems));
            }
        }
        return new ListResultTO<>((int) resp.getResults().getNumFound(), items);
    }

    private InvoiceList getSaleInvoiceFromSolrResult(Page<SaleInvoiceSolrDoc> saleInvoiceSolrDoc, EdsUser edsUser, ListingFilterParameter filterParametrs) {

        boolean isWarehouseAllocationEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.WAREHOUSE_ALLOCATION_ENABLE);
        boolean hasOnlySalesPersonRole = roleManager.hasOnlySalesPersonRole(edsUser);
        boolean isProjectInLine = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);

        int totalCount = isWarehouseAllocationEnabled && hasOnlySalesPersonRole ? 0 : (int) saleInvoiceSolrDoc.getTotalElements();

        // adding solr collapsed reusults to map
        ListPanelToolRpc panelSettings = filterParametrs.getListPanelTool();
        ArrayList<NewInvoice> invoiceList = new ArrayList<>();

        for (SaleInvoiceSolrDoc invoiceSolrDoc : saleInvoiceSolrDoc) {
            Integer idFromSolr = invoiceSolrDoc.getSaleInvoiceId();
            NewInvoice invoice = new NewInvoice();
            if (filterParametrs.isWithEncryptedLink()) {
                invoice.setEncryptedLink(EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("saleinvoice|summary/" + idFromSolr)));
            }
            invoice.setID(invoiceSolrDoc.getSaleInvoiceId());
            invoice.setInvoiceNumber(invoiceSolrDoc.getInvoiceNumber());
            invoice.setInvoiceDate(new DateNonConvertable(invoiceSolrDoc.getInvoiceDate()));
            invoice.setDueDate(new DateNonConvertable(invoiceSolrDoc.getDueDate()));
            invoice.setOpportunityNumber(invoiceSolrDoc.getOpportunityNumber());
            invoice.setOpportunityID(invoiceSolrDoc.getOpportunityId());
            invoice.setDueDate(new DateNonConvertable(invoiceSolrDoc.getDueDate()));
            invoice.setClientID(invoiceSolrDoc.getClientId());
            invoice.setClientName(invoiceSolrDoc.getClientName());
            invoice.setClientContactID(invoiceSolrDoc.getClientContactId());
            invoice.setClientContactEmail(invoiceSolrDoc.getClientContactEmail());
            invoice.setCurrencyID(invoiceSolrDoc.getCurrencyId());
            invoice.setCurrencyName(invoiceSolrDoc.getCurrencyName());
            invoice.setCreditNote(invoiceSolrDoc.getCreditNode());
            invoice.setTotalInInvoiceCurrency(BigDecimal.valueOf(invoiceSolrDoc.getTotalInvoiceCurrency()));
            invoice.setTotal(BigDecimal.valueOf(invoiceSolrDoc.getTotalInvoiceBase()));
            invoice.setPaidAmount(BigDecimal.valueOf(invoiceSolrDoc.getPaidAmount()));
            invoice.setDuePayments(BigDecimal.valueOf(invoiceSolrDoc.getDueAmount()));
            String statusCode = invoiceSolrDoc.getStatusCode();
            invoice.setStatus(referenceWfmMessageSource.localize(statusCode, invoiceSolrDoc.getStatusName()));
            invoice.setRelatedProjectID(invoiceSolrDoc.getRelatedProjectId());
            if (isProjectInLine) {
                invoice.setRelatedProjectName(ServerUtils.asListToString(invoiceSolrDoc.getMultiProjectNumberName()));
            } else {
                String number = invoiceSolrDoc.getRelatedProjectNumber();
                String name = invoiceSolrDoc.getRelatedProjectName();
                if (!ServerUtils.isNullOrEmpty(number) && !ServerUtils.isNullOrEmpty(name)) {
                    String projectNumberName = number + SolrSaleInvoiceRepresenter.ARROW + name;
                    invoice.setRelatedProjectName(projectNumberName);
                }
            }
            invoice.setProjectStatusCode(invoiceSolrDoc.getRelatedProjectCode());
            invoice.setReference(invoiceSolrDoc.getReference());
            invoice.setQuoteNumber(invoiceSolrDoc.getQuoteNumber());
            invoice.setInTarget(invoiceSolrDoc.getInTarget());
            invoice.setAnyPaymentExists(invoiceSolrDoc.getHasPayment());
            invoice.setCreationDate(invoiceSolrDoc.getCreatedDate());
            Integer creatorId = invoiceSolrDoc.getCreatorId();
            if (creatorId != null) {
                String creatorName = invoiceSolrDoc.getCreatorName();
                invoice.setCreator(new SelectItem(creatorId, creatorName));
                invoice.setCreatorName(creatorName);
            }
            invoice.setClientVatNumber(invoiceSolrDoc.getClientVat());
            invoice.setClientTrnNumber(invoiceSolrDoc.getClientTrn());
            //Need to discuss whether to show in listing or not
//            if (saleInvoice.getConvertedQuotes() != null) {
//                for (EdsQuote quote : saleInvoice.getConvertedQuotes()) {
//                    if (quote instanceof EdsSaleQuote && ((EdsSaleQuote) quote).getOpportunityID() != null) {
//                        EdsOpportunity opportunity = opportunityManager.get(((EdsSaleQuote) quote).getOpportunityID());
//                        invoice.setOpportunity(opportunity.getName());
//                        invoice.setOpportunityNumber(opportunity.getNumber());
//                        invoice.setOpportunityID(opportunity.getObjectID());
//                    }
//                }
//            }
            invoice.setZatcaStatus(invoiceSolrDoc.getZatcaStatus());
            invoice.setStatusCode(statusCode);
            invoice.setStatusID(invoiceSolrDoc.getStatusId());
            invoice.setIntroduction(invoiceSolrDoc.getIntroduction());
            invoice.setPoNumber(invoiceSolrDoc.getPoNumber());
            if (edsUser instanceof EdsClientContact) {
                invoice.setClient(true);
            }
            invoice.setProgressInvoicing(invoiceSolrDoc.getQuotePercent() != null);
            invoice.setProjectBasedInvoice(invoiceSolrDoc.getProjectBased());
            invoice.setTotalTaxes(BigDecimal.valueOf(invoiceSolrDoc.getTotalTaxes()));
            invoice.setSubtotal(BigDecimal.valueOf(invoiceSolrDoc.getSubTotal()));

            invoice.setPdfTemplateID(invoiceSolrDoc.getPdfTemplateId());
            Integer taxCalculationType = invoiceSolrDoc.getTaxCalculationType();
            invoice.setAmount(invoice.getTotalInInvoiceCurrency() != null && taxCalculationType != null && taxCalculationType.equals(TAX_CALCULATION_INCLUSIVE) ? invoice.getTotalInInvoiceCurrency().subtract(invoice.getTotalTaxes() != null ? invoice.getTotalTaxes() : BigDecimal.ZERO) : invoice.getTotalInInvoiceCurrency());

            if (panelSettings != null) {
                invoice.setCustomFieldMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(invoiceSolrDoc, panelSettings.getColumnCodeName()));
            }
            Integer approverId = invoiceSolrDoc.getCurrentApproverId();
            if (approverId != null) {
                invoice.setCurrentApproverSelectItem(new SelectItem(approverId, invoiceSolrDoc.getCurrentApproverName()));
            }

            if (isWarehouseAllocationEnabled && hasOnlySalesPersonRole) {
                if (invoice.getCreator() != null && edsUser.getObjectID().equals(invoice.getCreator().getId())) {
                    invoiceList.add(invoice);
                    totalCount++;
                }
            } else {
                invoiceList.add(invoice);
            }
        }
        InvoiceList listData = new InvoiceList(invoiceList, totalCount);
        listData.setNimbleCommerceEnabled(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.NIMBLE_ECOMMERCE_ENABLED));
        listData.setCustomInvoiceImportEnabled(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CUSTOM_INVOICE_IMPORT_ENABLED));
        return listData;
    }

    private ListResultTO<InvoiceDto> getSaleInvoiceFromSolrResult(QueryResponse resp, ListingFilterParameter filterParameter) {
        String Ids = resp.getResults().stream().map(doc -> String.valueOf(SolrUtils.asInteger(doc, SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID))).collect(Collectors.joining(","));
        ArrayList<InvoiceDto> items = new ArrayList<>();
        if (StringUtils.isNotBlank(Ids)) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = commonServiceLocal.getCompanyCustomFields(ViewName.SaleInvoice);
            ArrayList<CompanyCustomFieldItem> lineItemCustomFieldItems = commonServiceLocal.getCompanyCustomFields(ViewName.SaleInvoiceItem);

            List<EdsSaleInvoice> invoiceList = invoiceManager.getSaleInvoiceListByIDs(Ids, filterParameter.isAscending() ? "asc" : "desc");
            for (EdsSaleInvoice saleInvoice : invoiceList) {
                ArrayList<CompanyCustomFieldItem> clonedLineItems = new ArrayList<>();
                for (CompanyCustomFieldItem cf : lineItemCustomFieldItems) {
                    clonedLineItems.add(cf.cloneObject());
                }
                saleInvoice.setItemCustomFields(clonedLineItems);
                items.add(wrapInvoiceToDto(saleInvoice, customFieldItems));
            }
        }
        return new ListResultTO<>((int) resp.getResults().getNumFound(), items);
    }

    private ListResultTO<InvoiceDto> getPurchaseInvoiceFromSolrResult(QueryResponse resp) {
        String Ids = resp.getResults().stream().map(doc -> String.valueOf(SolrUtils.asInteger(doc, SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID))).collect(Collectors.joining(","));
        ArrayList<InvoiceDto> items = new ArrayList<>();
        if (StringUtils.isNotBlank(Ids)) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = commonServiceLocal.getCompanyCustomFields(ViewName.PurchaseInvoice);
            ArrayList<CompanyCustomFieldItem> lineItemCustomFieldItems = commonServiceLocal.getCompanyCustomFields(ViewName.PurchaseInvoiceItem);

            List<EdsPurchaseInvoice> invoiceList = invoiceManager.getPurchaseInvoiceByIds(Ids);
            for (EdsPurchaseInvoice purchaseInvoice : invoiceList) {
                purchaseInvoice.setItemCustomFields(lineItemCustomFieldItems);
                items.add(wrapInvoiceToDto(purchaseInvoice, customFieldItems));
            }
        }
        return new ListResultTO<>((int) resp.getResults().getNumFound(), items);
    }

//    private String getProjectName(Boolean isProjectInLine, SolrDocument relevantDoc) {
//        if (isProjectInLine) {
//            return ServerUtils.asListToString(SolrUtils.asListString(relevantDoc, SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_NUMBER_NAME));
//        } else {
//            String number = SolrUtils.asString(relevantDoc, SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NUMBER);
//            String name = SolrUtils.asString(relevantDoc, SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NAME);
//            if (number != null && !"".equals(number) && name != null && !"".equals(name)) {
//                return number + SolrSaleInvoiceRepresenter.ARROW + name;
//            } else {
//                return "";
//            }
//        }
//    }

    private String getSQProjectName(Boolean isProjectInLine, SaleQuoteSolrDoc relevantDoc) {
        if (isProjectInLine) {
            return ServerUtils.asListToString(relevantDoc.getMultiProjectNumberName());
        } else {
            String number = relevantDoc.getRelatedProjectNumber();
            String name = relevantDoc.getRelatedProjectName();
            if (number != null && !"".equals(number) && name != null && !"".equals(name)) {
                return number + SolrSaleInvoiceRepresenter.ARROW + name;
            } else {
                return "";
            }
        }
    }

    private String getProjectNameOrder(Boolean isProjectInLine, PurchaseOrderSolrDoc relevantDoc) {
        if (isProjectInLine) {
            return ServerUtils.asListToString(relevantDoc.getMultiProjectNumberName());
        } else {
            String number = relevantDoc.getRelatedProjectNumber();
            String name = relevantDoc.getRelatedProjectName();
            if (number != null && !"".equals(number) && name != null && !"".equals(name)) {
                return number + SolrSaleInvoiceRepresenter.ARROW + name;
            } else {
                return "";
            }
        }
    }

    public InvoiceList getSaleInvoiceData(ListingFilterParameter filterParameters, ListLoadConfig config, EdsUser user, String param) {
        if (filterParameters == null) {
            filterParameters = new ListingFilterParameter();
        }

        List<EdsBaseSaleInvoice> invoiceList;

        if (user instanceof EdsClientContact) {
            Integer approvedStatusId = referenceManager.findReference(INVOICE_STATUS, APPROVE).getObjectID();
            Integer openStatusId = referenceManager.findReference(INVOICE_STATUS, OPEN).getObjectID();
            Integer paidStatusId = referenceManager.findReference(INVOICE_STATUS, PAID).getObjectID();
            Integer clientApproveStatusId = referenceManager.findReference(INVOICE_STATUS, CLIENT_APPROVE).getObjectID();
            Integer rejectedStatusId = referenceManager.findReference(INVOICE_STATUS, REJECT).getObjectID();

            filterParameters.setInvoiceClientId(user.getClientContact().getClientID());
            invoiceList = invoiceManager.getSaleInvoiceList(filterParameters);
            List<EdsBaseSaleInvoice> temp = new LinkedList<>();
            for (EdsBaseSaleInvoice i : invoiceList) {
                if (i.getStatus() != null && (approvedStatusId.equals(i.getStatus().getObjectID())
                        || openStatusId.equals(i.getStatus().getObjectID())
                        || paidStatusId.equals(i.getStatus().getObjectID())
                        || clientApproveStatusId.equals(i.getStatus().getObjectID())
                        || rejectedStatusId.equals(i.getStatus().getObjectID()))) {
                    temp.add(i);
                }
            }
            invoiceList = temp;
        } else {
            if (param != null && param.equals("recurOverdueInvoice")) {
                invoiceList = invoiceManager.getSaleInvoiceList(filterParameters);
            } else {
                invoiceList = invoiceManager.getSaleInvoiceList(filterParameters);
            }
        }
        //Below we are getting access only to PM.
        if (!filterParameters.isFromBudgetSheet() && roleManager.hasRole(user, PM) && !roleManager.hasEitherRoles(user, ACCOUNTANT, DR, ADMIN)) {
            List<EdsCrmAccount> pmClients = projectManager.getPMClients();

            List<EdsBaseSaleInvoice> saleInvoiceList = new LinkedList<>();
            for (EdsCrmAccount client : pmClients) {
                for (EdsBaseSaleInvoice invoice : invoiceList) {
                    if (client.equals(invoice.getClient()) && !saleInvoiceList.contains(invoice)) {
                        saleInvoiceList.add(invoice);
                    }
                }
            }

            return createSaleInvoiceList(config, saleInvoiceList, param);
        }

        return createSaleInvoiceList(config, invoiceList, param);
    }

    private InvoiceList[] getSaleInvoiceDataForEveryClient(ListingFilterParameter filterParameters, ListLoadConfig config, EdsUser user, String param) {
        if (filterParameters == null) {
            filterParameters = new ListingFilterParameter();
        }

        List<EdsBaseSaleInvoice> invoiceList_ = null;
        List<InvoiceList> invoiceList = new ArrayList<>();
        List<EdsCrmAccount> clients = clientManager.getClientsWithInvoice(user.getCompany());
        if (clients != null && clients.size() > 0) {
            for (EdsCrmAccount client : clients) {
                filterParameters.setClientId(client.getObjectID());
                invoiceList_ = invoiceManager.getSaleInvoiceList(filterParameters);
                if (invoiceList_ != null && invoiceList_.size() > 0) {
                    invoiceList.add(createSaleInvoiceList(config, invoiceList_, param));
                }
            }
        }

        return invoiceList.toArray(new InvoiceList[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getPurchaseInvoiceData(ListingFilterParameter filterParameters) {

        if (filterParameters != null) {
            if (filterParameters.getStartDateNC() != null) {
                filterParameters.setStartDate(ServerUtils.parseFilterParameterDate(filterParameters.getStartDateNC()));
            }
            if (filterParameters.getEndDateNC() != null) {
                filterParameters.setEndDate(ServerUtils.parseFilterParameterDate(filterParameters.getEndDateNC()));
            }
        }
        List<EdsPurchaseInvoice> invoiceList = invoiceManager.getPurchaseInvoiceList(filterParameters, false, false);
        return createPurchaseInvoiceList(invoiceList);
    }

    private InvoiceList createSaleInvoiceList(ListLoadConfig config, List<EdsBaseSaleInvoice> invoices, String param) {
        int totalCount = invoices.size();
        if (config != null && config.getLimit() != 0) {
            invoices = ListUtils.getSublist(invoices, config.getStart(), config.getLimit());
        } else {
            invoices = ListUtils.getSublist(invoices, 0, totalCount);
        }     // for recurrence invoice
        return getList(invoices.toArray(new EdsBaseInvoice[]{}), totalCount, param, null);
    }

    private InvoiceList createPurchaseInvoiceList(final List<EdsPurchaseInvoice> invoices) {
        int totalCount = invoices.size();
        return getList(invoices.toArray(new EdsBaseInvoice[]{}), totalCount, null, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getSaleQuoteData(ListingFilterParameter filterParameters, ListLoadConfig config) {
        if (filterParameters == null) {
            filterParameters = new ListingFilterParameter();
        }

        EdsUser user = roleManager.getUser();
        List<EdsSaleQuote> quotes;

        if (user instanceof EdsClientContact) {
            Integer openStatusId = referenceManager.findReference(INVOICE_STATUS, OPEN).getObjectID();
            Integer rejectedStatusId = referenceManager.findReference(INVOICE_STATUS, REJECT).getObjectID();
            Integer approvedStatusId = referenceManager.findReference(INVOICE_STATUS, CLIENT_APPROVE).getObjectID();

            filterParameters.setInvoiceClientId(user.getClientContact().getClientID());

            quotes = quoteManager.getSaleQuoteList(filterParameters, config);
            List<EdsSaleQuote> temp = new LinkedList<>();
            for (EdsSaleQuote sq : quotes) {
                if (sq.getStatus() != null && (openStatusId.equals(sq.getStatus().getObjectID())
                        || rejectedStatusId.equals(sq.getStatus().getObjectID())
                        || approvedStatusId.equals(sq.getStatus().getObjectID()))) {
                    temp.add(sq);
                }
            }
            quotes = temp;
        } else {
            Integer pickedStatusId = referenceManager.findReference(INVOICE_STATUS, PICKED).getObjectID();
            Integer packedStatusId = referenceManager.findReference(INVOICE_STATUS, PACKED).getObjectID();
            Integer shippedStatusId = referenceManager.findReference(INVOICE_STATUS, SHIPPED).getObjectID();
            Integer saleOrderStatusId = referenceManager.findReference(INVOICE_STATUS, SALE_ORDER).getObjectID();
            Integer reject = referenceManager.findReference(INVOICE_STATUS, REJECT).getObjectID();
            Integer managerReject = referenceManager.findReference(INVOICE_STATUS, MANAGER_REJECT).getObjectID();
            Integer converted = referenceManager.findReference(INVOICE_STATUS, CONVERTED).getObjectID();
            Integer clientApproveStatusId = referenceManager.findReference(INVOICE_STATUS, CLIENT_APPROVE).getObjectID();
            Integer approveStatusId = referenceManager.findReference(INVOICE_STATUS, APPROVE).getObjectID();
            Integer invoicedStatusId = referenceManager.findReference(INVOICE_STATUS, INVOICED).getObjectID();
            Integer partiallyInvoicedStatusId = referenceManager.findReference(INVOICE_STATUS, PARTIAL_INVOICED).getObjectID();

            quotes = quoteManager.getSaleQuoteList(filterParameters, config);
            List<EdsSaleQuote> temp = new LinkedList<>();
            for (EdsSaleQuote sq : quotes) {
                if ((sq.getStatus() != null && converted.equals(sq.getStatus().getObjectID()))
                        || (sq.isSalesOrder() != null && sq.isSalesOrder())) {
                    continue;
                }
                if (sq.getStatus() != null &&
                        (!pickedStatusId.equals(sq.getStatus().getObjectID())
                                && !packedStatusId.equals(sq.getStatus().getObjectID())
                                && !shippedStatusId.equals(sq.getStatus().getObjectID())
                                && !reject.equals(sq.getStatus().getObjectID())
                                && !managerReject.equals(sq.getStatus().getObjectID())
                                && !saleOrderStatusId.equals(sq.getStatus().getObjectID()))
                        && ((sq.getConvertedToProject() != null && sq.getConvertedToProject()) || sq.getConvertedToInvoice())
                ) {
                    temp.add(sq);
                } else if (sq.getStatus() != null && (clientApproveStatusId.equals(sq.getStatus().getObjectID())
                        || approveStatusId.equals(sq.getStatus().getObjectID())
                        || partiallyInvoicedStatusId.equals(sq.getStatus().getObjectID())
                        || invoicedStatusId.equals(sq.getStatus().getObjectID()))) {
                    temp.add(sq);
                }
            }
            quotes = temp;
        }

        //Below we are getting access only to PM.
        if (!filterParameters.isFromBudgetSheet() && roleManager.hasRole(user, PM) && !roleManager.hasEitherRoles(user, ACCOUNTANT, DR, ADMIN)) {
            List<EdsCrmAccount> pmClients = projectManager.getPMClients();

            List<EdsSaleQuote> saleQuoteList = new LinkedList<>();
            for (EdsCrmAccount client : pmClients) {
                for (EdsSaleQuote quote : quotes) {
                    if ((client.equals(quote.getClient()) || (quote.getCreator() != null && user.getObjectID().equals(quote.getCreator().getObjectID())))
                            && !saleQuoteList.contains(quote)) {
                        saleQuoteList.add(quote);
                    }
                }
            }
            return createSaleQuoteList(config, saleQuoteList, filterParameters);
        }
        return createSaleQuoteList(config, quotes, filterParameters);
    }

    public InvoiceList getSaleOrderData(ListingFilterParameter filterParametrs) {
        EdsUser user = roleManager.getUser();
        return getSaleOrderDataInSolr(filterParametrs, user, false);
    }

    @Override
    public ListResultTO<OrderDto> getSaleOrderList(ListingFilterParameter filterParametrs) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_SALEQUOTE_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(getSaleSolrQuery(filterParametrs, getSaleOrderSolrQuery(filterParametrs, roleManager.getUser(), false)), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return getSaleOrderFromSolrResult(resp);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getSaleOrderData(ListingFilterParameter filterParameters, ListLoadConfig config) {
        if (filterParameters == null) {
            filterParameters = new ListingFilterParameter();
        }

        EdsUser user = roleManager.getUser();
        List<EdsSaleQuote> quotes;
        Integer pickedStatusId = referenceManager.findReference(INVOICE_STATUS, PICKED).getObjectID();
        Integer packedStatusId = referenceManager.findReference(INVOICE_STATUS, PACKED).getObjectID();
        Integer shippedStatusId = referenceManager.findReference(INVOICE_STATUS, SHIPPED).getObjectID();
        Integer saleOrderStatusId = referenceManager.findReference(INVOICE_STATUS, SALE_ORDER).getObjectID();
        if (user instanceof EdsClientContact) {
            filterParameters.setInvoiceClientId(user.getClientContact().getClientID());
            quotes = quoteManager.getSaleQuoteList(filterParameters, config);
        } else {
            quotes = quoteManager.getSaleQuoteList(filterParameters, config);
        }
        List<EdsSaleQuote> temp = new LinkedList<>();
        for (EdsSaleQuote sq : quotes) {
            if (sq.getStatus() != null && (pickedStatusId.equals(sq.getStatus().getObjectID())
                    || packedStatusId.equals(sq.getStatus().getObjectID())
                    || shippedStatusId.equals(sq.getStatus().getObjectID())
                    || saleOrderStatusId.equals(sq.getStatus().getObjectID())
                    || sq.isSalesOrder())) {
                temp.add(sq);
            }
        }
        quotes = temp;

        //Below we are getting access only to PM.
        if (!filterParameters.isFromBudgetSheet() && roleManager.hasRole(user, PM) && !roleManager.hasEitherRoles(user, ACCOUNTANT, DR, ADMIN)) {
            List<EdsCrmAccount> pmClients = projectManager.getPMClients();

            List<EdsSaleQuote> saleQuoteList = new LinkedList<>();
            for (EdsCrmAccount client : pmClients) {
                for (EdsSaleQuote quote : quotes) {
                    if (client.equals(quote.getClient()) && !saleQuoteList.contains(quote)) {
                        saleQuoteList.add(quote);
                    }
                }
            }
            return createSaleQuoteList(config, saleQuoteList, filterParameters);
        }
        return createSaleQuoteList(config, quotes, filterParameters);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceList getPurchaseOrderData(ListingFilterParameter filterParameters, ListLoadConfig config) {
        if (filterParameters == null) {
            filterParameters = new ListingFilterParameter();
        }

        List<EdsPurchaseOrder> quotes = quoteManager.getPurchaseOrderList(filterParameters, config);

        return createPurchaseOrderList(config, quotes);
    }

    public InvoiceList getPurchaseOrderData(ListingFilterParameter filterParametrs) {
        EdsUser user = roleManager.getUser();
        return getPurchaseOrderDataInSolr(filterParametrs, user, false);
    }

    @Override
    public ListResultTO<OrderDto> getPurchaseOrderList(ListingFilterParameter filterParametrs) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_PURCHASE_ORDER_CORE);
        QueryResponse resp = null;
        try {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }

            if (filterParametrs.getStartDateNC() != null) {
                filterParametrs.setStartDate(ServerUtils.parseFilterParameterDate(filterParametrs.getStartDateNC()));
            }
            if (filterParametrs.getEndDateNC() != null) {
                filterParametrs.setEndDate(ServerUtils.parseFilterParameterDate(filterParametrs.getEndDateNC()));
            }

            FacetFilterRpc invoiceFacetFilter = filterParametrs.getFacetFilter();
            if (invoiceFacetFilter != null && !invoiceFacetFilter.isFilterChanges()) {
                invoiceFacetFilter = commonServiceLocal.getUserFacetFilter(invoiceFacetFilter);
            }
            if (invoiceFacetFilter != null) {
                if (invoiceFacetFilter.getSearchKey() != null && !"".equals(invoiceFacetFilter.getSearchKey())) {
                    filterParametrs.setSearchKey(invoiceFacetFilter.getSearchKey());
                }
                filterParametrs.setFacetFilter(invoiceFacetFilter);
            }
            EdsUser user = roleManager.getUser();
            String solrQuery = getPurchaseOrderSolrQuery(filterParametrs, user, false) + SolrFacetUtils.generateSaleInvoiceDuePaidAmountFacet(invoiceFacetFilter,
                    FacetContentType.PurchaseOrderFacetFilter.getContentCode()[2]) +
                    SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(
                            invoiceFacetFilter,
                            user.getCompany(),
                            SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE,
                            SolrSaleInvoiceRepresenter.FIELD_DUE_DATE,
                            FacetContentType.PurchaseOrderFacetFilter.getContentCode()[2]
                    );
            resp = server.query(getSaleSolrQuery(filterParametrs, solrQuery), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return getPurchaseOrderFromSolrResult(resp);
    }

    private InvoiceList createSaleQuoteList(final ListLoadConfig config, List<EdsSaleQuote> quotes, ListingFilterParameter filterParametrs) {
        int totalCount = quotes.size();
        if (config != null && config.getSortField() != null && DUE_AMOUNT_COLUMN.equals(config.getSortField())) {//Prospect amount
            quotes.sort((o1, o2) -> {
                if (config.getSortDir() == 2) {
                    return o2.getTotalInInvoiceCurrency().compareTo(o1.getTotalInInvoiceCurrency());
                } else {
                    return o1.getTotalInInvoiceCurrency().compareTo(o2.getTotalInInvoiceCurrency());
                }
            });
        }
        if (config != null) {
            quotes = ListUtils.getSublist(quotes, config.getStart(), config.getLimit());
        }
        return getList(quotes.toArray(new EdsSaleQuote[]{}), totalCount, null, filterParametrs);
    }

    private InvoiceList createPurchaseOrderList(final ListLoadConfig config, List<EdsPurchaseOrder> orders) {
        int totalCount = orders.size();
        if (config != null && config.getSortField() != null && DUE_AMOUNT_COLUMN.equals(config.getSortField())) {//Prospect amount
            orders.sort((o1, o2) -> {
                if (config.getSortDir() == 2) {
                    return o2.getTotalInInvoiceCurrency().compareTo(o1.getTotalInInvoiceCurrency());
                } else {
                    return o1.getTotalInInvoiceCurrency().compareTo(o2.getTotalInInvoiceCurrency());
                }
            });
        }
        if (config != null) {
            orders = ListUtils.getSublist(orders, config.getStart(), config.getLimit());
        }
        return getList(orders.toArray(new EdsPurchaseOrder[]{}), totalCount, null, null);
    }

    private InvoiceList getList(EdsBaseInvoice[] baseInvoices, int totalCount, final String param, ListingFilterParameter filterParametrs) {
        ArrayList<NewInvoice> invoiceList = new ArrayList<>();
        EdsUser user = companyManager.getUser();
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        for (EdsBaseInvoice baseInvoice : baseInvoices) {
            boolean isInvoiceInstance = baseInvoice instanceof EdsInvoice;
//            if (isInvoiceInstance && param == null) {
//                setOverdue(baseInvoice);
//            }
            NewInvoice result = isInvoiceInstance ? EdsInvoice.getInvoiceData((EdsInvoice) baseInvoice) : EdsQuote.getQuoteData((EdsQuote) baseInvoice);
            if (user instanceof EdsClientContact) {
                result.setClient(true);
            }
            result.setID(baseInvoice.getObjectID());
            if (filterParametrs.isWithEncryptedLink()) {
                result.setEncryptedLink(EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("salequote|summary/" + baseInvoice.getObjectID())));
            }
            result.setInvoiceNumber(baseInvoice.getNumber());
            result.setInvoiceDate(new DateNonConvertable(baseInvoice.getInvoiceDate()));
            result.setDueDate(new DateNonConvertable(baseInvoice.getDueDate()));
            result.setClientID(baseInvoice.getClientOrSupplier() != null ? baseInvoice.getClientOrSupplier().getObjectID() : Integer.valueOf(0));
            result.setClientName(baseInvoice.getClientOrSupplier() != null ? baseInvoice.getClientOrSupplier().getName() : "N/A");
            result.setClientContactEmail(baseInvoice.getClientOrSupplier() != null ? baseInvoice.getClientOrSupplier().getPrimaryContact() != null ? baseInvoice.getClientOrSupplier().getPrimaryContact().getPrimaryEmail() : "" : "");
            result.setStatus(baseInvoice.getStatus() != null ? referenceWfmMessageSource.localizeRef(baseInvoice.getStatus()) : "");
            result.setStatusCode(baseInvoice.getStatus() != null ? baseInvoice.getStatus().getCode() : "");
            result.setCurrencyName(baseInvoice.getCurrency() != null ? baseInvoice.getCurrency().getName() : null);
            result.setTotal(baseInvoice.getTotal());
            result.setTotalTaxes(baseInvoice.getTotalTaxes());
            if (baseInvoice instanceof EdsInvoice) {
                result.setZatcaStatus(((EdsInvoice) baseInvoice).getZatcaStatus());
            }
            result.setTotalInInvoiceCurrency(baseInvoice.getTotalInInvoiceCurrency());
            result.setExchageRate(baseInvoice.getExchangeRate());
            if (baseInvoice.getCreator() != null) {
                result.setCreator(new SelectItem(baseInvoice.getCreator().getObjectID(), baseInvoice.getCreator().getFullName()));
            }
            if (baseInvoice.getRelatedProject() != null) {
                result.setRelatedProjectName(baseInvoice.getRelatedProject().getName());
            }
            if (baseInvoice instanceof EdsSaleQuote) {
                if (baseInvoice.getStatus() != null && (baseInvoice.getStatus().getCode().equals(SALE_ORDER)
                        || baseInvoice.getStatus().getCode().equals(PICKED)
                        || baseInvoice.getStatus().getCode().equals(PACKED)
                        || baseInvoice.getStatus().getCode().equals(SHIPPED)
                        || baseInvoice.getStatus().getCode().equals(PARTIAL_SHIPPED)
                        || baseInvoice.getStatus().getCode().equals(INVOICED))) {
                    EdsPickList pickList = pickListManager.getPickListBySaleQuoteID(baseInvoice.getObjectID());
                    if (pickList != null) {
                        result.setPickListID(pickList.getObjectID());
                    }

                    if (((EdsSaleQuote) baseInvoice).getShippingMethod() != null) {
                        result.setShippingMethodID(((EdsSaleQuote) baseInvoice).getShippingMethod().getObjectID());
                        result.setShippingMethodName(((EdsSaleQuote) baseInvoice).getShippingMethod().getName());
                    }
                }
            } else if (baseInvoice instanceof EdsPurchaseOrder) {
                EdsUser approver = ((EdsPurchaseOrder) baseInvoice).getApprover();
                if (approver != null) {
                    result.setPurchaseOrderManager(new SelectItem(approver.getObjectID(), approver.getName()));
                    result.setDoubleApprovalEnabled(true);
                }
            }
            if (isInvoiceInstance) {
                if (baseInvoice instanceof EdsSaleInvoice) {
                    result.setCreditNote(((EdsSaleInvoice) baseInvoice).isCreditNote());
                } else if (baseInvoice instanceof EdsPurchaseInvoice) {
                    result.setCreditNote(((EdsPurchaseInvoice) baseInvoice).isCreditNote());
                }
                result.setPaidAmount(baseInvoice.getFullPayments());
            }
            invoiceList.add(result);
        }
        return new InvoiceList(invoiceList, totalCount);
    }

//    public void setOverdue(EdsBaseInvoice baseInvoice) {
//        if (getCompanyDate().after(baseInvoice.getDueDate()) && !baseInvoice.getStatus().getCode().equals(PAID) &&
//                !baseInvoice.getStatus().getCode().equals(DRAFT) && !baseInvoice.getStatus().getCode().equals(OVER_DUE)) {
//            baseInvoice.setStatus(referenceManager.findReference(INVOICE_STATUS, OVER_DUE));
//        }
//    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Date getCompanyDate() {
        EdsUser user = invoiceManager.getUser();
        Calendar companyTime = new GregorianCalendar(TimeZone.getTimeZone(user.getCompany().getCountryZone().getZone().getZoneID()));
        return companyTime.getTime();
    }

//    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
//    public TypeItem[] getTypeArray() {
//        List<EdsReference> typeList = referenceManager.listReferences(ReferenceManager._PRODUCT_TYPE);
//        TypeItem[] result = new TypeItem[typeList.size()];
//
//        int i = 0;
//        for (EdsReference item : typeList) {
//            result[i] = new TypeItem(item.getObjectID(), referenceWfmMessageSource.localize(item.getCode(), item.getName()), item.getCode());
//            i++;
//        }
//
//        return result;
//    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public String getInvoiceGoogleCheckoutMerchantId(Integer companyID) {
        String merchantId = null;
        EdsCompany company = companyID != null ? companyManager.get(companyID) : invoicingSettingsManager.getUser().getCompany();
        EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);
        if (invSettings != null && invSettings.getMerchantId() != null) {
            merchantId = invSettings.getMerchantId();
            return merchantId;
        } else {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getInvoicePaymentLink(Integer invoiceId, NewInvoice newInvoice, Integer companyID) {

        if (invoiceId == null || invoiceId <= 0) {
            return null;
        }

        String companyPayPalAccount = null;
        EdsCompany company = companyID != null ? companyManager.get(companyID) : invoicingSettingsManager.getUser().getCompany();
        EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);
        if (invSettings != null && invSettings.getPayPalAccount() != null) {
            companyPayPalAccount = invSettings.getPayPalAccount();
        }

        if (companyPayPalAccount == null) {
            return null;
        }

        NewInvoice invoiceData = null;
        String invoiceCurrency = null;
        String t3_Value = "";
        int p3Value = 1;
        String srcValue = "0";
        String cmdValue = "_xclick";
        if (newInvoice != null) {
            invoiceData = newInvoice;
            EdsCurrency edsCurrency = currencyManager.get(invoiceData.getCurrencyID());
            invoiceCurrency = edsCurrency != null ? edsCurrency.getName() : null;
            EdsInvoice invoice = invoiceManager.get(invoiceId);
            if (invoice instanceof EdsSaleInvoice inv) {
                if (inv.getRecurringInvoiceID() != null) {
                    EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.RECURRING_INVOICE_REMINDER, inv.getRecurringInvoiceID(), company.getObjectID());
                    if (recurrence != null) {
                        if (recurrence.getType().equals(SchedulerConstant.RECURRENCE_TYPE_DAILY)) {
                            t3_Value = "D";
                        } else if (recurrence.getType().equals(SchedulerConstant.RECURRENCE_TYPE_WEEKLY)) {
                            t3_Value = "W";
                        } else if (recurrence.getType().equals(SchedulerConstant.RECURRENCE_TYPE_MONTHLY)) {
                            t3_Value = "M";
                        } else if (recurrence.getType().equals(SchedulerConstant.RECURRENCE_TYPE_YEARLY)) {
                            t3_Value = "Y";
                        }
                        srcValue = "1";
                        cmdValue = "_xclick-subscriptions";
                    }
                }
            }
        } else {
            EdsInvoice invoice = invoiceManager.get(invoiceId);
            invoiceData = EdsInvoice.getInvoiceData(invoice);
            invoiceCurrency = invoiceData.getCurrencyName();
            if (invoice instanceof EdsSaleInvoice inv) {
                if (inv.getRecurringInvoiceID() != null) {
                    EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.RECURRING_INVOICE_REMINDER, inv.getRecurringInvoiceID(), company.getObjectID());
                    if (recurrence != null) {
                        if (recurrence.getType().equals(SchedulerConstant.RECURRENCE_TYPE_DAILY)) {
                            t3_Value = "D";
                        } else if (recurrence.getType().equals(SchedulerConstant.RECURRENCE_TYPE_WEEKLY)) {
                            t3_Value = "W";
                        } else if (recurrence.getType().equals(SchedulerConstant.RECURRENCE_TYPE_MONTHLY)) {
                            t3_Value = "M";
                        } else if (recurrence.getType().equals(SchedulerConstant.RECURRENCE_TYPE_YEARLY)) {
                            t3_Value = "Y";
                        }
                        srcValue = "1";
                        cmdValue = "_xclick-subscriptions";
                    }
                }
            }
        }

        StringBuilder paypalLink = new StringBuilder();

        // PayPal supported currencies
        List<String> suppurtedCurrencies = new ArrayList<>();
        suppurtedCurrencies.add("EUR");
        suppurtedCurrencies.add("GBP");
        suppurtedCurrencies.add("USD");
        suppurtedCurrencies.add("AUD");
        suppurtedCurrencies.add("NZD");
        suppurtedCurrencies.add("CHF");
        suppurtedCurrencies.add("HKD");
        suppurtedCurrencies.add("SGD");
        suppurtedCurrencies.add("SEK");
        suppurtedCurrencies.add("DKK");
        suppurtedCurrencies.add("NOK");
        suppurtedCurrencies.add("HUF");
        suppurtedCurrencies.add("CZK");
        suppurtedCurrencies.add("ILS");
        suppurtedCurrencies.add("BRL");
        suppurtedCurrencies.add("MYR");
        suppurtedCurrencies.add("PHP");
        suppurtedCurrencies.add("THB");
        suppurtedCurrencies.add("RUB");


        if (invoiceCurrency == null || !suppurtedCurrencies.contains(invoiceCurrency)) {
            return null;
        }

        paypalLink.append("https://").append(ServerUtils.getPayPalLink());
        paypalLink.append("?cmd=").append(cmdValue);
        paypalLink.append("&business=").append(URLEncoder.encode(companyPayPalAccount, StandardCharsets.UTF_8));
        paypalLink.append("&item_name=").append(URLEncoder.encode(getItemNames(invoiceData), StandardCharsets.UTF_8));
        paypalLink.append("&amount=").append(URLEncoder.encode(getInvoiceDueAmount(invoiceId).toString(), StandardCharsets.UTF_8));
        paypalLink.append("&currency_code=").append(URLEncoder.encode(invoiceCurrency, StandardCharsets.UTF_8));
        paypalLink.append("&a3=").append(URLEncoder.encode(getInvoiceDueAmount(invoiceId).toString(), StandardCharsets.UTF_8)).append("&p3=").append(p3Value).append("&t3=").append(t3_Value);
        paypalLink.append("&src=").append(srcValue).append("&custom=").append("_").append(EncryptionHelper.encryptURL(company.getObjectID() + "_" + ServerSecurityContext.getInstance().getDatabase() + "_" + invoiceData.getID())).append("&notify_url=").append(URLEncoder.encode(EdsContextParams.getFullHost() + CommandConstants.COMMON_URL + "/invoicePaypalNotification", StandardCharsets.UTF_8));
        return paypalLink.toString();

    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getOrderPaymentLink(Integer invoiceId, NewInvoice newInvoice, Integer companyID) {

        if (invoiceId == null || invoiceId <= 0) {
            return null;
        }

        String companyPayPalAccount = null;
        EdsCompany company = companyID != null ? companyManager.get(companyID) : invoicingSettingsManager.getUser().getCompany();
        EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);
        if (invSettings != null && invSettings.getPayPalAccount() != null) {
            companyPayPalAccount = invSettings.getPayPalAccount();
        }

        if (companyPayPalAccount == null) {
            return null;
        }

        NewInvoice invoiceData = null;
        String invoiceCurrency = null;
        String t3_Value = "";
        int p3Value = 1;
        String srcValue = "0";
        String cmdValue = "_xclick";
        if (newInvoice != null) {
            invoiceData = newInvoice;
            EdsCurrency edsCurrency = currencyManager.get(invoiceData.getCurrencyID());
            invoiceCurrency = edsCurrency != null ? edsCurrency.getName() : null;
            EdsQuote edsQuote = quoteManager.get(invoiceId);
        } else {
            EdsQuote edsQuote = quoteManager.get(invoiceId);
            invoiceData = EdsQuote.getQuoteData(edsQuote);
            invoiceCurrency = invoiceData.getCurrencyName();
        }

        StringBuilder paypalLink = new StringBuilder();

        // PayPal supported currencies
        List<String> suppurtedCurrencies = new ArrayList<>();
        suppurtedCurrencies.add("EUR");
        suppurtedCurrencies.add("GBP");
        suppurtedCurrencies.add("USD");
        suppurtedCurrencies.add("AUD");
        suppurtedCurrencies.add("NZD");
        suppurtedCurrencies.add("CHF");
        suppurtedCurrencies.add("HKD");
        suppurtedCurrencies.add("SGD");
        suppurtedCurrencies.add("SEK");
        suppurtedCurrencies.add("DKK");
        suppurtedCurrencies.add("NOK");
        suppurtedCurrencies.add("HUF");
        suppurtedCurrencies.add("CZK");
        suppurtedCurrencies.add("ILS");
        suppurtedCurrencies.add("BRL");
        suppurtedCurrencies.add("MYR");
        suppurtedCurrencies.add("PHP");
        suppurtedCurrencies.add("THB");
        suppurtedCurrencies.add("RUB");


        if (invoiceCurrency == null || !suppurtedCurrencies.contains(invoiceCurrency)) {
            return null;
        }

        paypalLink.append("https://").append(ServerUtils.getPayPalLink());
        paypalLink.append("?cmd=").append(cmdValue);
        paypalLink.append("&business=").append(URLEncoder.encode(companyPayPalAccount, StandardCharsets.UTF_8));
        paypalLink.append("&item_name=").append(URLEncoder.encode(getItemNames(invoiceData), StandardCharsets.UTF_8));
        paypalLink.append("&amount=").append(URLEncoder.encode(getOrderDueAmount(invoiceId).toString(), StandardCharsets.UTF_8));
        paypalLink.append("&currency_code=").append(URLEncoder.encode(invoiceCurrency, StandardCharsets.UTF_8));
        paypalLink.append("&a3=").append(URLEncoder.encode(getOrderDueAmount(invoiceId).toString(), StandardCharsets.UTF_8)).append("&p3=").append(p3Value).append("&t3=").append(t3_Value);
        paypalLink.append("&src=").append(srcValue).append("&custom=").append("_").append(EncryptionHelper.encryptURL(company.getObjectID() + "_" + ServerSecurityContext.getInstance().getDatabase() + "_" + invoiceData.getID())).append("&notify_url=").append(URLEncoder.encode(EdsContextParams.getFullHost() + CommandConstants.COMMON_URL + "/invoicePaypalNotification", StandardCharsets.UTF_8));
        return paypalLink.toString();

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getPayMeInvoicePaymentLink(NewInvoice invoice) {
        if (invoice != null && "UZS".equals(invoice.getCurrencyName())) {
            EdsUser user = invoiceManager.getUser();
            EdsCompany company = user.getCompany();
            EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);
            if (invoice.getBankAccount() == null) {
                return null;
            }
            EdsBankAccount invoiceBankAccount = bankAccountManager.get(invoice.getBankAccount().getId());
            // Check every bank account of integration app for creating link and generating QR in pdf
            //The payment link is generated when the invoice's bank account matches the Payme bank account set in Settings > Payment Gateway.
            if (invoiceBankAccount.getAccount() == null || invSettings.getPaymePaymentAccount() == null) {
                return null;
            }
            if (invoiceBankAccount.getAccount().getObjectID().equals(invSettings.getPaymePaymentAccount().getObjectID())) {
                String merchantId = invSettings.getPayMeMerchantId();
                if (StringUtils.isBlank(merchantId)) {
                    return null;
                }
                StringBuilder payMeValues = new StringBuilder();
                payMeValues.append("m=").append(merchantId).append(";");
                payMeValues.append("a=").append(getDueAmount(invoice).multiply(new BigDecimal(100))).append(";");
                payMeValues.append("ac.invoice_id=").append(invoice.getID()).append(";");

//            String data = "type=" + PAYMENT_TYPES.PAYME + "&" +
//                    "session=" + SecurityContext.getInstance().getSessionId() + "&" +
//                    "invoiceId=" + invoice.getID();
//
//            payMeValues.append("c=").append(EdsContextParams.getHost()).append("/payment/callback/").append(EncryptionHelper.encodeBase64(data)).append(";");

                StringBuilder paymentLink = new StringBuilder(ServerUtils.getPayMeDomain());
                paymentLink.append("/").append(EncryptionHelper.encodeBase64(payMeValues.toString()));
                return paymentLink.toString();
            }
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getClickInvoicePaymentLink(NewInvoice invoice) {
        if (invoice != null && "UZS".equals(invoice.getCurrencyName())) {
            EdsUser user = invoiceManager.getUser();
            EdsCompany company = user.getCompany();
            EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);
            String merchantId = invSettings.getClickMerchantId();
            String clickServiceId = invSettings.getClickServiceId();
            if (StringUtils.isBlank(merchantId)) {
                return null;
            }
            StringBuilder clickValues = new StringBuilder();
            clickValues.append("service_id=").append(clickServiceId).append("&");
            clickValues.append("merchant_id=").append(merchantId).append("&");
            clickValues.append("amount=").append(getDueAmount(invoice)).append("&");
            clickValues.append("transaction_param=").append(invoice.getInvoiceNumber()).append("&");

            String data = "invoiceId=" + invoice.getID() + "&" +
                    "session=" + SecurityContext.getInstance().getSessionId() + "&" +
                    "type=" + PAYMENT_TYPES.CLICK;
            clickValues.append("return_url=").append(EdsContextParams.getHost()).append("/payment/callback/").append(EncryptionHelper.encodeBase64(data));

            return ServerUtils.getClickDomain() + "?" + clickValues;
        }
        return null;
    }

    public String getRevolutInvoicePaymentLink(NewInvoice invoice) {
        if (invoice != null) {
            if (invoice.getRevolutUrl() != null) {
                return invoice.getRevolutUrl();
            }
            //Revolut Supported Currencies
            List<String> suppurtedCurrencies = new ArrayList<>();
            suppurtedCurrencies.add("AED");
            suppurtedCurrencies.add("AUD");
            suppurtedCurrencies.add("CAD");
            suppurtedCurrencies.add("CHF");
            suppurtedCurrencies.add("CZK");
            suppurtedCurrencies.add("DKK");
            suppurtedCurrencies.add("EUR");
            suppurtedCurrencies.add("GBP");
            suppurtedCurrencies.add("HKD");
            suppurtedCurrencies.add("HUF");
            suppurtedCurrencies.add("ILS");
            suppurtedCurrencies.add("JPY");
            suppurtedCurrencies.add("MAD");
            suppurtedCurrencies.add("NOK");
            suppurtedCurrencies.add("NZD");
            suppurtedCurrencies.add("PLN");
            suppurtedCurrencies.add("QAR");
            suppurtedCurrencies.add("RON");
            suppurtedCurrencies.add("SEK");
            suppurtedCurrencies.add("SGD");
            suppurtedCurrencies.add("THB");
            suppurtedCurrencies.add("TRY");
            suppurtedCurrencies.add("USD");
            suppurtedCurrencies.add("ZAR");
            if (invoice.getCurrencyName() == null || !suppurtedCurrencies.contains(invoice.getCurrencyName())) {
                return null;
            }
            RevolutResponseDto revolutResponse = revolutService.createOrder(getDueAmount(invoice).multiply(BigDecimal.valueOf(100)).intValue(), invoice.getCurrencyName(), false, invoice.getInvoiceNumber());
            if (revolutResponse != null) {
                invoiceManager.insertRevolutUrl(invoice.getID(), revolutResponse.getCheckout_url(), revolutResponse.getId(), SecurityContext.getCompanyID());
                return revolutResponse.getCheckout_url();
            }
        }
        return null;
    }
    public BigDecimal getDueAmount(NewInvoice invoice) {
        if (invoice.getTotalInInvoiceCurrency() == null) {
            return BigDecimal.ZERO;
        }
        return invoice.getTotalInInvoiceCurrency()
                .subtract(invoice.getPaidAmount() == null ? BigDecimal.ZERO : invoice.getPaidAmount())
                .setScale(invoice.getCalcScale() != null ? invoice.getCalcScale() : ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getStripeInvoicePaymentLink(Integer invoiceId, NewInvoice newInvoice, Integer companyID) {

        if (invoiceId == null || invoiceId <= 0) {
            return null;
        }
        //The payment link is generated when the invoice's bank account matches the Stripe bank account set in Settings > Payment Gateway.
        EdsAccount companyStripeAccount = null;
        EdsCompany company = companyID != null ? companyManager.get(companyID) : invoicingSettingsManager.getUser().getCompany();
        EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);
        if (newInvoice.getBankAccount() == null) {
            return null;
        }
        EdsBankAccount invoiceBankAccount = bankAccountManager.get(newInvoice.getBankAccount().getId());
        if (invSettings != null && invSettings.getStripePaymentAccount() != null) {
            if (!invoiceBankAccount.getAccount().getObjectID().equals(invSettings.getStripePaymentAccount().getObjectID())) {
                return null;
            }
            companyStripeAccount = invSettings.getStripePaymentAccount();
        }

        if (companyStripeAccount == null) {
            return null;
        }
// agar to'lab bo'lgan bo'lsa chiqish kerakmi yoki yo'q ?
//        if (getInvoiceDueAmount(invoiceId).doubleValue() <= 0) {
//            return null;
//        }
        if (new Date().before(newInvoice.getInvoiceDate().getDate())) {
            return null;
        }

        EdsInvoice invoice = invoiceManager.get(invoiceId);
        NewInvoice invoiceData = EdsInvoice.getInvoiceData(invoice);

        String stripeLink = EdsContextParams.getFullHost() + "stripe-payment.html?customtoken=_" +
                EncryptionHelper.encryptURL(company.getObjectID() + "_" + ServerSecurityContext.getInstance().getDatabase() + "_" + invoiceData.getID() + "_true");

        return stripeLink;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getStripeOrderPaymentLink(Integer invoiceId, NewInvoice newInvoice, Integer companyID, boolean isInvoice) {

        if (invoiceId == null || invoiceId <= 0) {
            return null;
        }

        EdsAccount companyStripeAccount = null;
        EdsCompany company = companyID != null ? companyManager.get(companyID) : invoicingSettingsManager.getUser().getCompany();
        EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);
        if (invSettings != null && invSettings.getStripePaymentAccount() != null) {
            companyStripeAccount = invSettings.getStripePaymentAccount();
        }

        if (companyStripeAccount == null) {
            return null;
        }

        if (getOrderDueAmount(invoiceId).doubleValue() <= 0) {
            return null;
        }
        if (new Date().before(newInvoice.getInvoiceDate().getDate())) {
            return null;
        }

        String stripeLink = EdsContextParams.getFullHost() + "stripe-payment.html?customtoken=_" +
                EncryptionHelper.encryptURL(company.getObjectID() + "_" + ServerSecurityContext.getInstance().getDatabase() + "_" + invoiceId + "_" + isInvoice);

        return stripeLink;
    }

    private BigDecimal getInvoiceDueAmount(Integer invoiceID) {
        EdsInvoice invoice = invoiceManager.get(invoiceID);
        return invoice != null ? invoice.getDueAmount().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    private BigDecimal getOrderDueAmount(Integer invoiceID) {
        EdsQuote edsQuote = quoteManager.get(invoiceID);
        return edsQuote != null ? edsQuote.getTotal().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    private String getItemNames(NewInvoice invoiceData) {
        StringBuilder buffer = new StringBuilder();
        if (invoiceData.getInvoiceNumber() != null && !invoiceData.getInvoiceNumber().trim().isEmpty()) {
            buffer.append("Invoice Number:").append(invoiceData.getInvoiceNumber()).append(",");
        }
        for (NewInvoiceItem invoiceItem : invoiceData.getItems()) {
            buffer.append(invoiceItem.getItemName());
            buffer.append(",");
        }
        buffer.deleteCharAt(buffer.lastIndexOf(","));
        return buffer.toString();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getInvoiceLogoUrl(EdsCompany company) {
        if (company == null) {
            company = invoicingSettingsManager.getUser().getCompany();
            return companyAttachmentManager.getCompanyLogoUrl(company, CommandConstants.FOR_INVOICEPDF);
        }
        String url = companyAttachmentManager.getCompanyLogoUrl(company, CommandConstants.FOR_INVOICEPDF);
        if (StringUtil.isEmpty(url) && Constants.LOCAL.equals(EdsContextParams.getUploadType())) {
            SelectItem item = companyAttachmentManager.getCompanyLogo(company, CommandConstants.FOR_INVOICEPDF);
            if (item != null) {
                EdsCompanyAttachment logo = companyAttachmentManager.get(item.getId());
                url = logo.getLocalPath() + logo.getObjectID();
            }
        }

        return url;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HistoryListItem[] getInvoiceNotes(Integer invoiceId) {
        List<EdsInvoiceQuoteNote> records = invoiceQuoteNoteManager.getInvoiceNotes(invoiceId);
        List<HistoryListItem> result = mapToHistoryListItems(records);
        return result.toArray(new HistoryListItem[]{});
    }

    private List<HistoryListItem> mapToHistoryListItems(List<EdsInvoiceQuoteNote> records) {
        List<HistoryListItem> result = new ArrayList<>();
        for (EdsInvoiceQuoteNote row : records) {
            HistoryListItem item = new HistoryListItem();
            item.setObjectID(row.getObjectID());
            if (row.isSuperUser()) {
                item.setEmployee(Constants.defaultSupportName);
            } else {
                item.setEmployee(row.getCommentator() != null ? row.getCommentator().getFullName() : null);
            }
            item.setComment(row.getComment());
            item.setEventDate(row.getDate());
            result.add(item);
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HistoryListItem[] getQuoteNotes(Integer objectID) {
        List<EdsInvoiceQuoteNote> records = invoiceQuoteNoteManager.getQuoteNotes(objectID);
        List<HistoryListItem> result = mapToHistoryListItems(records);
        return result.toArray(new HistoryListItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getRelatedProjects(ListingFilterParameter filterParametrs) {
        EdsEmployee employee = employeeManager.get(employeeManager.getUser().getObjectID());
        EdsReference all = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ALL);
        if (all != null) {
            filterParametrs.setProjectStatusId(all.getObjectID());
        }
        filterParametrs.setHasOnlyClientAccess(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_LOOKUP_WITHOUT_CUSTOMER));//all projects without clients or client related projects
        filterParametrs.setLimit(60);
        List<EdsProject> projectList = projectManager.list(filterParametrs, employee);
        if (projectList != null && projectList.isEmpty()) {
            filterParametrs.setHasOnlyClientAccess(true);
            projectList = projectManager.list(filterParametrs, employee);
        }

        if (projectList == null) {
            return null;
        }

        SelectItem[] result = new SelectItem[projectList.size()];
        int i = 0;
        for (EdsProject project : projectList) {
            result[i++] = new SelectItem(project.getObjectID(),
                    (project.getNumber() != null && !"".equals(project.getNumber().trim()) ? project.getNumber() + " -> " : "") + project.getName(),
                    project.getNumber());
        }

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewInvoice getQuote(Integer id, Integer externalFormID) {
        EdsQuote mainQuote = quoteManager.get(id);
        if (mainQuote == null) {
            return null;
        }

        ArrayList<CompanyCustomFieldItem> itemCustomFields = null;

        if (mainQuote instanceof EdsSaleQuote) {
            itemCustomFields = (ArrayList<CompanyCustomFieldItem>) commonServiceLocal.getCompanyAllCustomFields(((EdsSaleQuote) mainQuote).isSalesOrder() ? ViewName.SaleOrderItem : ViewName.SaleQuoteItem);
        } else if (mainQuote instanceof EdsPurchaseOrder) {
            itemCustomFields = (ArrayList<CompanyCustomFieldItem>) commonServiceLocal.getCompanyAllCustomFields(ViewName.PurchaseOrderItem);
        }
        mainQuote.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));

        NewInvoice quote = EdsQuote.getQuoteData(mainQuote);
        Map<Integer, ShippingDataItem> itemsUsedInGdn = null;
        if ((mainQuote instanceof EdsPurchaseOrder || (mainQuote instanceof EdsSaleQuote && ((EdsSaleQuote) mainQuote).isSalesOrder())) &&
                quote.getStatusCode() != null &&
                (quote.getStatusCode().equals(RECEIVED) || quote.getStatusCode().equals(PARTIAL_RECEIVED) || quote.getStatusCode().equals(INVOICED))) {
            itemsUsedInGdn = shippingDataManager.getQuoteItemIdsUsedInGrnOrGdn(quote.getID());
            Date latestGrnDate = shippingDataManager.getLatestGrnDate(quote.getID());
            if (latestGrnDate != null) {
                quote.setLastGrnDate(new DateNonConvertable(latestGrnDate));
            }
        }
        for (NewInvoiceItem qItem : quote.getItems()) {
            List<FileResource> attachments = attachmentUtilsManager.getAttachments(F_SALE_QUOTE_ITEM, qItem.getID(), qItem.getID(), crmAccountManager.getUser());
            if (attachments != null && !attachments.isEmpty()) {
                for (FileResource fileResource : attachments) {
                    FileItem fileItem = new FileItem();
                    fileItem.setId(fileResource.getObjectId());
                    fileItem.setFileName(fileResource.getFileName());
                    fileItem.setDescription(fileResource.getDescription());
                    fileItem.setDate(fileResource.getCreationDate());
                    fileItem.setContentType(fileResource.getContentType());
                    fileItem.setSize(fileResource.getContentLength());
                    fileItem.setUploadType(fileResource.getUploadType());
                    fileItem.setAmazonLink(fileResource.getAmazonLink());
                    fileItem.setGoogleDocumentLink(fileResource.getGoogleDownloadLink());
                    fileItem.setOfficeDocumentLink(fileResource.getOfficeDownloadLink());
                    fileItem.setBodyId(fileResource.getBodyId());
                    qItem.getAttachments().add(fileItem);
                }
            }

            if (mainQuote instanceof EdsSaleQuote && COPY_FROM_SO_TO_SQ.equals(externalFormID) && qItem.getCustomFieldItems() != null && qItem.getCustomFieldItems().size() > 0) {

                List<CompanyCustomFieldItem> saleQuoteItemCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.SaleQuoteItem);
                List<CompanyCustomFieldItem> soItemCustomValues = CustomFieldsUtils.setRPCCustomFieldItems(invoiceItemCFManager.get(qItem.getCustomFieldItems().get(0).getObjectId()), qItem.getCustomFieldItems());

                ArrayList<CompanyCustomFieldItem> sqItemCustomFields = new ArrayList<>();

                for (CompanyCustomFieldItem sq : saleQuoteItemCustomFields) {
                    sq.setObjectId(null);
                    for (CompanyCustomFieldItem so : soItemCustomValues) {
                        if (sq.getDataType().equals(so.getDataType())
                                && sq.getUiType().equals(so.getUiType())
                                && sq.getAliasName().equals(so.getAliasName())) {
                            sq.setPredefinedValues(so.getPredefinedValues());
                            sq.setPredefinedValuesWithSorting(so.getPredefinedValuesWithSorting());
                            sq.setQuery(so.getQuery());
                            sq.setQueryItems(so.getQueryItems());
                            sq.setFieldStringValue(so.getFieldStringValue());
                            sq.setFieldDateNonConvertedValue(so.getFieldDateNonConvertedValue());
                            sq.setAttachments(so.getAttachments());
                            sq.setLookUpTypeEnum(so.getLookUpTypeEnum());
                            sq.setSelectedId(so.getSelectedId());
                            sq.setDefaultValue(so.getDefaultValue());
                            sq.setPrefix(so.getPrefix());
                            sq.setItem(so.getItem());
                            sq.setSelectItems(so.getSelectItems());
                        }
                    }
                    sqItemCustomFields.add(sq);
                }
                qItem.setCustomFieldItems(sqItemCustomFields);
            } else if (mainQuote instanceof EdsSaleQuote && COPY_FROM_EXISTING_DATA.equals(externalFormID) && qItem.getCustomFieldItems() != null && qItem.getCustomFieldItems().size() > 0) {
                List<CompanyCustomFieldItem> soOrSqItemCustomValues = CustomFieldsUtils.setRPCCustomFieldItems(invoiceItemCFManager.get(qItem.getCustomFieldItems().get(0).getObjectId()), qItem.getCustomFieldItems());
                for (CompanyCustomFieldItem customFieldItem : soOrSqItemCustomValues) {
                    customFieldItem.setObjectId(null);
                }
            } else if ((mainQuote instanceof EdsPurchaseOrder || (mainQuote instanceof EdsSaleQuote && ((EdsSaleQuote) mainQuote).isSalesOrder()))
                    && itemsUsedInGdn != null && itemsUsedInGdn.containsKey(qItem.getID())) {
                qItem.setUsedInGrn(itemsUsedInGdn.get(qItem.getID()));
            }
        }
        quote.setBankAccount(mainQuote.getBankAccount() != null ? mainQuote.getBankAccount().getAsSelectItem() : null);
        quote.setTaxCalculationType(mainQuote.getTaxCalculationType());
        if (mainQuote instanceof EdsPurchaseOrder order) {
            if (order.getClientID() != null) {
                EdsCrmAccount client = crmAccountManager.get(order.getClientID());
                if (client != null) {
                    TypeItem clientItem = new TypeItem(client.getObjectID(), client.getName(), null);
                    clientItem.setMailAddressID(order.getClientMailAddressID());
                    quote.setClientItem(clientItem);
                }
            }
            if (order.getOrderTerms() != null) {
                quote.setInvoiceTermsItem(order.getOrderTerms().getAsRPC());
            }

            if (order.getShippingMethod() != null) {
                EdsShippingMethod shippingMethod = order.getShippingMethod();
                quote.setShippingMethodID(shippingMethod.getObjectID());
                quote.setShippingMethodName(shippingMethod.getName());

                ShippingMethod shm = shippingMethod.getRPC();
                shm.setCurrencyId(order.getCurrency().getObjectID());
                shm.setExchangeRate(order.getExchangeRate());

                if (order.getShippingAmount() != null && order.getShippingAmount().compareTo(BigDecimal.ZERO) > 0) {
                    shm.setPrice(order.getShippingAmount());
                }
                quote.setShippingPrice(shm.getPrice());
                quote.setShippingMethod(shm);
            }
            if (Objects.equals(PARTIAL_RECEIVED, quote.getStatusCode()) || Objects.equals(APPROVE, quote.getStatusCode())) {
                quote.setGrnNumberData(this.parseGrnNumberData());
            }
            quote.setCurrentApproverSelectItem(order.getCurrentApprover() != null
                    && order.getCurrentApprover().getExactEmployee() != null
                    ? order.getCurrentApprover().getExactEmployee().getAsSelectItem()
                    : null);
            quote.setNumberData(parseOrderNumberData(order));
            quote.setRequisitionedBy(order.getRequisitionedBy() != null ? order.getRequisitionedBy().getAsSelectItem() : null);
            quote.setPaymentMethodID(order.getPaymentMethod() != null ? order.getPaymentMethod().getObjectID() : null);
            quote.setPaymentMethod(order.getPaymentMethod() != null ? commonLocalizer.localize(order.getPaymentMethod().getCode(), order.getPaymentMethod().getName()) : "");
            quote.setReference(order.getReference());
            quote.setPaymentTerms(order.getPaymentTerms());
            quote.setShippingTerms(order.getShippingTerms());
            quote.setReceiveDate(order.getReceiveDate() != null ? new DateNonConvertable(order.getReceiveDate()) : null);
            quote.setReversechargeApplicable(order.isReverseChargeApplicable());

            if (order.getQuoteId() != null) {
                EdsQuote saleQuote = quoteManager.get(order.getQuoteId());
                quote.setQuoteId(order.getQuoteId());
                quote.setQuoteNumber(saleQuote.getNumber());
            }


            quote.setPurchaseOrderManager(order.getApprover() != null ? new SelectItem(order.getApprover().getObjectID(), order.getApprover().getName()) : null);
            quote.setCancelDate(order.getCancelDate() != null ? new DateNonConvertable(order.getCancelDate()) : null);
            quote.setAllocatedExpenses(expenseReportManager.getExpensesAllocatedToPO(order.getObjectID()));
            quote.setPoRelatedExpenseExist(expenseReportManager.getPurchaseOrderRelatedExpenseItems(order.getObjectID()).size() > 0);
            FileItem[] orderAttachments = getAttachments(order.getObjectID(), F_PUR_ORDER);
            quote.setAttachments((orderAttachments != null && orderAttachments.length > 0) ? orderAttachments : new FileItem[0]);

            //init invoice custom fields
            EdsInvoiceCustomFields customFields = order.getCustomFields();
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonServiceLocal.getCompanyCustomFields(ViewName.PurchaseOrder);
            quote.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(customFields, customFieldsItems));

            for (NewInvoiceItem orderItem : quote.getItems()) {
                orderItem.setAssignedSerials(productSerialManager.getOrderItemSerialsAsSelectItem(orderItem.getID()));
            }
            quote.setGrnCount(quoteServiceLocal.getGdnGrnCount(id, false));
        }

        quote.setBaseCurrencyName(getBaseCurrency().getName());
        quote.setExchageRate(quote.getExchageRate());
        quote.setSaasuGUID(quote.getSaasuGUID());
        quote.setSaasuLastUpdateDate(quote.getSaasuLastUpdateDate());
        quote.setSaasuLastUpdatedUid(quote.getSaasuLastUpdatedUid());
        if (mainQuote instanceof EdsSaleQuote) {
            EdsSaleQuote saleQuote = (EdsSaleQuote) mainQuote;
            EdsCrmContact primContact = clientContactManager.getPrimaryClientContact(saleQuote.getClient().getObjectID());
            if (primContact != null) {
                EdsClientContact clientContact = clientContactManager.getClientContactByCrmContact(primContact.getObjectID());
                if (clientContact != null) {
                    quote.setClient(quoteManager.getUser().getObjectID().equals(clientContact.getObjectID()));
                }
            }
            quote.setNumberData(parseQuoteNumberData(saleQuote));
            quote.setTotalDiscount(saleQuote.getTotalDiscount());
            quote.setReference(saleQuote.getReference());
            if (quote.getStatusCode() != null
                    && (quote.getStatusCode().equals(SALE_ORDER) || quote.getStatusCode().equals(PICKED)
                    || quote.getStatusCode().equals(PACKED) || quote.getStatusCode().equals(SHIPPED)
                    || quote.getStatusCode().equals(PARTIAL_SHIPPED) || quote.getStatusCode().equals(INVOICED))) {
                EdsPickList pickList = pickListManager.getPickListBySaleQuoteID(id);
                if (pickList != null) {
                    quote.setPickListID(pickList.getObjectID());
                }
            }
            if (saleQuote.getShippingMethod() != null) {
                EdsShippingMethod shippingMethod = saleQuote.getShippingMethod();
                quote.setShippingMethodID(shippingMethod.getObjectID());
                quote.setShippingMethodName(shippingMethod.getName());

                ShippingMethod shm = shippingMethod.getRPC();
                shm.setCurrencyId(saleQuote.getCurrency().getObjectID());
                shm.setExchangeRate(saleQuote.getExchangeRate());

                if (saleQuote.getShippingAmount() != null && saleQuote.getShippingAmount().compareTo(BigDecimal.ZERO) > 0) {
                    shm.setPrice(saleQuote.getShippingAmount());
                }
                quote.setShippingPrice(shm.getPrice());
                quote.setShippingMethod(shm);
            }

            quote.setIntroduction(saleQuote.getIntroduction());
            quote.setPaymentInstructionID(saleQuote.getTermsConditionsID());
//            if (saleQuote.getSupplier() != null) {
//                quote.setSupplierID(saleQuote.getSupplier().getObjectID());
//                quote.setSupplierName(saleQuote.getSupplier().getName());
//            }

            quote.setCurrentApproverSelectItem(saleQuote.getCurrentApprover() != null
                    && saleQuote.getCurrentApprover().getExactEmployee() != null
                    ? saleQuote.getCurrentApprover().getExactEmployee().getAsSelectItem()
                    : null);
            quote.setCreator(saleQuote.getCreator() != null ? saleQuote.getCreator().getAsSelectItem() : null);

            quote.setProgressInvoicing(saleQuote.isProgressInvoicing());
            quote.setProgressInvoicingType(saleQuote.getProgressInvoicingType());
            quote.setConvertedPercent(saleQuote.getConvertedPercent());
            quote.setConvertedAmount(saleQuote.getConvertedAmount());
            if (quote.isProgressInvoicing()) {
                quote.setInvoicedAmount(invoiceManager.getConvertedInvoiceAmount(saleQuote.getObjectID(), null));
            }

            if (mainQuote.getPriceLevelID() != null) {
                EdsPriceLevel priceLevel = priceLevelManager.get(mainQuote.getPriceLevelID());
                quote.setPriceLevel(new SelectItem(priceLevel.getObjectID(), priceLevel.getName()));
            }

            if (saleQuote.getComissionAllocateItems() != null && saleQuote.getComissionAllocateItems().size() > 0) {
                AllocateComissionItem allocateComissionItem;
                ArrayList<AllocateComissionItem> allocateComissionItems = new ArrayList<>();
                for (EdsComissionAllocateItem item : saleQuote.getComissionAllocateItems()) {
                    allocateComissionItem = new AllocateComissionItem();
                    allocateComissionItem.setQuoteId(saleQuote.getObjectID());
                    if (item.getSalesMan() != null) {
                        allocateComissionItem.setSalesMan(new SelectItem(item.getSalesMan().getObjectID(), item.getSalesMan().getFullName()));
                    }
                    allocateComissionItem.setAllocatePercent(item.getComissionPercent());
                    allocateComissionItem.setAllocateTotal(item.getAllocateAmount());
                    allocateComissionItems.add(allocateComissionItem);
                }
                quote.setAllocateComissionItems(allocateComissionItems);
            }

            FileItem[] quoteAttachments = getAttachments(saleQuote.getObjectID(), F_SALE_QUOTE);
            quote.setAttachments((quoteAttachments != null && quoteAttachments.length > 0) ? quoteAttachments : new FileItem[0]);

            //init invoice custom fields
            EdsInvoiceCustomFields customFields = saleQuote.getCustomFields();

            if (COPY_FROM_SO_TO_SQ.equals(externalFormID)) {

                ArrayList<CompanyCustomFieldItem> saleOrderCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.SaleOrder);
                List<CompanyCustomFieldItem> saleQuoteCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.SaleQuote);

                List<CompanyCustomFieldItem> soCustomValues = CustomFieldsUtils.setRPCCustomFieldItems(customFields, saleOrderCustomFields);

                ArrayList<CompanyCustomFieldItem> sqCustomFields = new ArrayList<>();

                for (CompanyCustomFieldItem sq : saleQuoteCustomFields) {
                    sq.setObjectId(null);
                    for (CompanyCustomFieldItem so : soCustomValues) {
                        if (sq.getDataType().equals(so.getDataType())
                                && sq.getUiType().equals(so.getUiType())
                                && sq.getAliasName().equals(so.getAliasName())) {
                            sq.setPredefinedValues(so.getPredefinedValues());
                            sq.setPredefinedValuesWithSorting(so.getPredefinedValuesWithSorting());
                            sq.setQuery(so.getQuery());
                            sq.setQueryItems(so.getQueryItems());
                            sq.setFieldStringValue(so.getFieldStringValue());
                            sq.setFieldDateNonConvertedValue(so.getFieldDateNonConvertedValue());
                            sq.setAttachments(so.getAttachments());
                            sq.setLookUpTypeEnum(so.getLookUpTypeEnum());
                            sq.setSelectedId(so.getSelectedId());
                            sq.setDefaultValue(so.getDefaultValue());
                            sq.setPrefix(so.getPrefix());
                            sq.setItem(so.getItem());
                            sq.setSelectItems(so.getSelectItems());
                        }
                    }
                    sqCustomFields.add(sq);
                }
                quote.setCustomFieldItems(sqCustomFields);
            } else {
                ArrayList<CompanyCustomFieldItem> customFieldsItems = commonServiceLocal.getCompanyCustomFields(((EdsSaleQuote) mainQuote).isSalesOrder() ? ViewName.SaleOrder : ViewName.SaleQuote);
                quote.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(customFields, customFieldsItems));
            }
            if (saleQuote.getInvoiceTerms() != null) {
                quote.setInvoiceTermsItem(saleQuote.getInvoiceTerms().getAsRPC());
            }
            if (saleQuote.isSalesOrder() != null) {
                quote.setSalesOrder(saleQuote.isSalesOrder());
            }
            quote.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(saleQuote.isSalesOrder() ? RelationItem.TYPE_SALEORDER : RelationItem.TYPE_SALEQUOTE, saleQuote.getObjectID())));
            ArrayList<CompanyCustomFieldItem> companyCustomFieldItems = commonServiceLocal.getCompanyCustomFields(saleQuote.isSalesOrder() ? ViewName.SaleOrderSystem : ViewName.SaleQuoteSystem);
            quote.setSystemCustomFields(companyCustomFieldItems);
        }

        if (mainQuote.getPriceLevelID() != null) {
            EdsPriceLevel priceLevel = priceLevelManager.get(mainQuote.getPriceLevelID());
            quote.setPriceLevel(new SelectItem(priceLevel.getObjectID(), priceLevel.getName()));
        }
        if (mainQuote.getPdfTemplate() != null) {
            quote.setPdfTemplateID(mainQuote.getPdfTemplate().getObjectID());
        }
        if (mainQuote.getPlaceOfSupplyId() != null) {
            if (PLACEOFSUPPLY_CATEGORY.REGION.equals(mainQuote.getPlaceOfSupplyCategory())) {
                EdsRegion region = regionManager.get(mainQuote.getPlaceOfSupplyId());
                SelectItem placeOfSupply = region.getAsSelectItem();
                placeOfSupply.setCode(region.getCode());
                placeOfSupply.setCategory(PLACEOFSUPPLY_CATEGORY.REGION);
                quote.setPlaceOfSupply(placeOfSupply);
            } else if (PLACEOFSUPPLY_CATEGORY.COUNTRY.equals(mainQuote.getPlaceOfSupplyCategory())) {
                EdsCountry country = countryManager.get(mainQuote.getPlaceOfSupplyId());
                SelectItem placeOfSupply = country.getAsSelectItem();
                placeOfSupply.setCode(country.getCode());
                placeOfSupply.setCategory(PLACEOFSUPPLY_CATEGORY.COUNTRY);
                quote.setPlaceOfSupply(placeOfSupply);
            }
        }
        quote.setHistoryList(getQuoteNotes(id));

        if (quote.isProgressInvoicing() && !quote.isMultiQuoteConvertEnabled()) {
            quote.setInvoicedItems(invoiceService.getInvoicesByConvertedQuote(quote.getID()));
        }
        if (mainQuote instanceof EdsSaleQuote && ((EdsSaleQuote) mainQuote).getOpportunityID() != null) {
            EdsOpportunity opportunity = opportunityManager.get(((EdsSaleQuote) mainQuote).getOpportunityID());
            quote.setOpportunity(opportunity.getName());
            quote.setOpportunityID(opportunity.getObjectID());
            quote.setOpportunityNumber(opportunity.getNumber());
        }


        quote.setDiscountType(mainQuote.getDiscountType());
        quote.setDiscountAmount(mainQuote.getDiscountAmount());
        quote.setTotalDiscount(mainQuote.getTotalDiscount());
        quote.setDoubleApprovalEnabled(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PO_DOUBLE_APPROVER_ENABLED));
        quote.setBillAddressID(mainQuote.getBillAddressID());
        quote.setMailAddressID(mainQuote.getMailAddressID());
        if (mainQuote instanceof EdsSaleQuote) {
            if (((EdsSaleQuote) mainQuote).isSalesOrder()) {
                quote.setApproverSaved(approverManager.isExistApproverByEntityTypeAndEntityId(RelationItem.TYPE_SALEORDER, mainQuote.getObjectID()));
            } else {
                quote.setApproverSaved(approverManager.isExistApproverByEntityTypeAndEntityId(RelationItem.TYPE_SALEQUOTE, mainQuote.getObjectID()));
            }
        } else if (mainQuote instanceof EdsPurchaseOrder) {
            quote.setApproverSaved(approverManager.isExistApproverByEntityTypeAndEntityId(RelationItem.TYPE_PURCHASE_ORDER, mainQuote.getObjectID()));
            quote.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_PURCHASE_ORDER, mainQuote.getObjectID())));

        }
        if (quote != null && quote.getItems() != null) {
            EdsUser currentUser = invoiceManager.getUser();
            EdsCompany currentCompany = currentUser.getCompany();
            boolean isArabicCompany = ServerUtils.isArabicCompany(currentCompany);
            if (isArabicCompany) {
                for (NewInvoiceItem item : quote.getItems()) {
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
        return quote;
    }

    @Override
    public BankTransferNumberData parseGrnNumberData() {
        final BankTransferNumberData numberData = new BankTransferNumberData();
        final Integer intNumber = this.shippingDataManager.getLastIntNumberData(ShippingDataType.IN);
        final EdsNumberingSettings settings = this.numberingSettingsManager.getNumberingSetting();

        if (settings == null || StringUtil.isEmpty(settings.getGrnNumberFormat())) {
            final NumberData defaultData = EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_GRN_PREFIX);
            final String[] numberParts = defaultData.getNumberFormat().split("_");
            if (numberParts != null && numberParts.length > 1) {
                numberData.setPrefix(numberParts[0]);
                numberData.setFourDigitNumber(String.valueOf(numberParts[1]));
                numberData.setWithDate(numberParts[1].split("-").length == 2);
            }
            return numberData;
        }
        final String[] mainPartNumbers = settings.getGrnNumberFormat().split("_");

        if (mainPartNumbers != null && mainPartNumbers.length > 1) {
            numberData.setPrefix(mainPartNumbers[0]);
            final String[] datePartNumbers = mainPartNumbers[1].split("-");

            numberData.setWithDate(datePartNumbers.length == 2);
            if (intNumber != null) {
                numberData.setFourDigitNumber(new DecimalFormat("0000").format(intNumber + 1));
            } else {
                numberData.setFourDigitNumber(datePartNumbers[0]);
            }
        }
        if (numberData.isWithDate()) {
            numberData.setDate(ServerUtils.getBankTransferDateNumber(new Date()));
        }
        return numberData;
    }

    @Override
    public BankTransferNumberData parseGdnNumberData() {
        final BankTransferNumberData numberData = new BankTransferNumberData();
        final Integer intNumber = this.shippingDataManager.getLastIntNumberData(ShippingDataType.OUT);
        final EdsNumberingSettings settings = this.numberingSettingsManager.getNumberingSetting();

        if (settings == null || StringUtil.isEmpty(settings.getGdnNumberFormat())) {
            final NumberData defaultData = EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_GDN_PREFIX);
            final String[] numberParts = defaultData.getNumberFormat().split("_");
            if (numberParts != null && numberParts.length > 1) {
                numberData.setPrefix(numberParts[0]);
                numberData.setFourDigitNumber(String.valueOf(numberParts[1]));
                numberData.setWithDate(numberParts[1].split("-").length == 2);
            }
            return numberData;
        }
        final String[] mainPartNumbers = settings.getGdnNumberFormat().split("_");

        if (mainPartNumbers != null && mainPartNumbers.length > 1) {
            numberData.setPrefix(mainPartNumbers[0]);
            final String[] datePartNumbers = mainPartNumbers[1].split("-");

            numberData.setWithDate(datePartNumbers.length == 2);
            if (intNumber != null) {
                numberData.setFourDigitNumber(new DecimalFormat("0000").format(intNumber + 1));
            } else {
                numberData.setFourDigitNumber(datePartNumbers[0]);
            }
        }
        if (numberData.isWithDate()) {
            numberData.setDate(ServerUtils.getBankTransferDateNumber(new Date()));
        }
        return numberData;
    }

    private InvoiceNumberData parseQuoteNumberData(EdsSaleQuote quote) {
        DecimalFormat format = new DecimalFormat("0000");
        InvoiceNumberData numberData = getQuoteOrderNumberData(SALE_QUOTE);
        numberData.setFourDigitNumber(quote.getFourDigitNumber() != null ? format.format(quote.getFourDigitNumber()) : "");
        numberData.setWithClient(quote.getClient().getNumber() != null && quote.getNumber().contains(quote.getClient().getNumber()));
        numberData.setClientCode(numberData.isWithClient() ? quote.getClient().getNumber() : "");
        if (quote.getRelatedProject() != null && quote.getRelatedProject().getNumber() != null && !"".equals(quote.getRelatedProject().getNumber().trim())) {
            numberData.setWithProject(quote.getNumber().contains(quote.getRelatedProject().getNumber()));
            if (numberData.isWithProject()) {
                numberData.setProjectCode(quote.getRelatedProject().getNumber());
            }
        }
        return numberData;
    }

    private InvoiceNumberData parseOrderNumberData(EdsPurchaseOrder order) {
        DecimalFormat format = new DecimalFormat("0000");
        InvoiceNumberData numberData = getQuoteOrderNumberData(PURCHASE_ORDER);
        numberData.setFourDigitNumber(order.getFourDigitNumber() != null ? format.format(order.getFourDigitNumber()) : "");
        numberData.setWithClient(order.getClientOrSupplier().getNumber() != null && order.getNumber().contains(order.getClientOrSupplier().getNumber()));
        numberData.setClientCode(numberData.isWithClient() ? order.getClientOrSupplier().getNumber() : "");
        if (order.getRelatedProject() != null && order.getRelatedProject().getNumber() != null && !"".equals(order.getRelatedProject().getNumber().trim())) {
            numberData.setWithProject(order.getNumber().contains(order.getRelatedProject().getNumber()));
            if (numberData.isWithProject()) {
                numberData.setProjectCode(order.getRelatedProject().getNumber());
            }
        }
        return numberData;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceNumberData getQuoteOrderNumberData(String type) {
        return getQuoteOrderNumberData(type, null);
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceNumberData getQuoteOrderNumberData(String type, DateNonConvertable quoteOrderDate) {
        InvoiceNumberData numberData = new InvoiceNumberData();
        EdsInvoicingSettings setting = invoicingSettingsManager.getInvoiceSettings(quoteManager.getUser().getCompany());
        if (SALE_QUOTE.equals(type)) {
            parseNumber(setting.getSalesQuoteNumberingFormat(), numberData, quoteManager.getQuoteFourDigitNumber(false, quoteOrderDate), "clientcode");
        } else if (SALE_ORDER.equals(type)) {
            parseNumber(setting.getSalesOrderNumberingFormat(), numberData, quoteManager.getQuoteFourDigitNumber(true, quoteOrderDate), "clientcode");
        } else if (PURCHASE_ORDER.equals(type)) {
            parseNumber(setting.getPurchaseOrderNumberingFormat(), numberData, quoteManager.getOrderFourDigitNumber(quoteOrderDate), "suppliercode");
        }
        return numberData;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceNumberData getInvoiceNumberData(EdsCompany company, String customPrefix) {
        return getInvoiceNumberData(company, customPrefix, null);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceNumberData getInvoiceNumberData(EdsCompany company, String customPrefix, DateNonConvertable invoiceDate) {
        InvoiceNumberData numberData = new InvoiceNumberData();
        EdsInvoicingSettings setting = invoicingSettingsManager.getInvoiceSettings(company);//for ex.: "INV_data_clientcode_0001";
        Integer fourDigitNumber = invoiceManager.getSaleInvoiceFourDigitNumber(invoiceDate);
        String numberFormat = setting.getInvoiceNumberingFormat();
        //it will be run for only enable invoice custom type
        if (customPrefix != null && !customPrefix.isEmpty() && numberFormat != null) {
            numberFormat = customPrefix + numberFormat.substring(numberFormat.indexOf("_"));
        }
        parseNumber(numberFormat, numberData, fourDigitNumber, "clientcode");
        return numberData;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceNumberData getPurchaseInvoiceNumberData(boolean isPICreditNote) { // isPICreditNote false busa Purchase Invoice aks holda PI Credit Note number
        return getPurchaseInvoiceNumberData(isPICreditNote, true);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceNumberData getPurchaseInvoiceNumberData(boolean isPICreditNote, boolean isDebitNote) {
        return getPurchaseInvoiceNumberData(isPICreditNote, isDebitNote, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InvoiceNumberData getPurchaseInvoiceNumberData(boolean isPICreditNote, boolean isDebitNote, DateNonConvertable invoiceDate) { // isPICreditNote false busa Purchase Invoice aks holda PI Credit Note number
        InvoiceNumberData numberData = new InvoiceNumberData();
        EdsInvoicingSettings numberingSettings = invoicingSettingsManager.getInvoiceSettings(invoiceManager.getUser().getCompany());
        Integer fourDigitNumber = invoiceManager.getPurchaseInvoiceFourDigitNumber(isPICreditNote, invoiceDate);
        String numberFormat = "";
        if (numberingSettings != null && numberingSettings.getCnNumberingFormat() != null && numberingSettings.getPiNumberingFormat() != null) {
            if (isPICreditNote) {
                if (isDebitNote) {
                    numberFormat = numberingSettings.getDnNumberingFormat();
                } else {
                    numberFormat = numberingSettings.getCnNumberingFormat();
                }
            } else {
                numberFormat = numberingSettings.getPiNumberingFormat();
            }
        } else {
            if (isPICreditNote) {
                if (isDebitNote) {
                    numberFormat = "DN_0001";
                } else {
                    numberFormat = "CN_0001";
                }
            } else {
                numberFormat = "PI_0001";
            }
        }
        parseNumber(numberFormat, numberData, fourDigitNumber, "clientcode");
        return numberData;
    }

    @Override
    public InvoiceNumberData generatePurchaseInvoiceNumber(boolean isPICreditNote) {
        DecimalFormat format = new DecimalFormat("0000");
        //EdsCompany edsCompany = invoiceManager.getUser().getCompany();
        InvoiceNumberData invoiceNumberData = getPurchaseInvoiceNumberData(isPICreditNote);
        while (invoiceManager.getPurchaseInvoiceByNumberGlobal(invoiceNumberData.getInvoiceNumber()).size() > 0) {
            invoiceNumberData.setFourDigitNumber(format.format(Integer.parseInt(invoiceNumberData.getFourDigitNumber()) + 1));
        }
        return invoiceNumberData;
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

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FileItem[] getAttachments(Integer id, int folderType) {
        List<FileResource> saleQuoteAttachments = attachmentUtilsManager.getAttachments(folderType, id, id);
        FileItem[] fileItems = new FileItem[saleQuoteAttachments.size()];
        for (int i = 0; i < saleQuoteAttachments.size(); i++) {
            FileResource fileResource = saleQuoteAttachments.get(i);
            if (fileResource != null) {
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
        }
        return fileItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CurrencyItem getBaseCurrency() {
        return returnBaseCurrency(null).createCurrencyItem();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CurrencyItem getBaseCurrency(Integer companyID) {
        return returnBaseCurrency(companyManager.get(companyID)).createCurrencyItem();
    }

    public EdsCurrency returnBaseCurrency(EdsCompany company) {

        if (company == null) {
            if (companyManager.getUser() != null) {
                company = companyManager.getUser().getCompany();
            }

            if (company == null && ServerSecurityContext.getInstance().getCompanyId() != null && !ServerSecurityContext.getInstance().getCompanyId().isEmpty()) {
                company = companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
            }
        }

        //If company already has base currency. (You can set it in the Settings menu)
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        EdsCurrency currency = fs != null ? fs.getCurrency() : null;
        //company does not has base currency. Hm...
        if (currency == null) {
            //Then try to get country currency.
            currency = company.getCountryZone().getCountry().getCurrency();
            //For this country there is no currency.
            if (currency == null) {
                //Hey, I know universal currency! God bless America!
                currency = currencyManager.getCurrency(CurrencyManager.USD);
            }
        }
        return currency;
    }

    public String getMasterCardPaymentURL(Integer keyID, Integer companyID, BigDecimal paymentAmount, String paymentType, String userDefinedUrl) {

        ArrayList<String> supportedCurrencies = new ArrayList<>();
        supportedCurrencies.add("OMR");

        EdsCompany company = companyManager.get(companyID);
        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(company);

        String currencyName = "", numberReference = "";
        if (MastercardPaymentHandler.COURSE_BOOKING.equals(paymentType)) {
            EdsCourseBooking courseBooking = courseBookingManager.get(keyID);
            currencyName = returnBaseCurrency(company).getName();
            numberReference = courseBooking.getNumber();
        } else if (MastercardPaymentHandler.INVOICE.equals(paymentType)) {
            EdsInvoice invoice = invoiceManager.get(keyID);
            currencyName = invoice.getCurrency().getName();
            numberReference = invoice.getNumber();
        }
        boolean isValidParameters = supportedCurrencies.contains(currencyName) && isValid(invoicingSettings.getMasterCardMerchandID()) && isValid(invoicingSettings.getMasterCardAccessCode()) && isValid(invoicingSettings.getMasterCardSecretKey());
        if (isValidParameters && keyID != null) {
            SortedMap<String, String> sortedParamMap = new TreeMap<>();

            String amountString = String.valueOf(paymentAmount.multiply(new BigDecimal(1000)).intValue());

            sortedParamMap.put("user_amount", EncryptionHelper.encrypt(amountString));
            sortedParamMap.put("user_cid", EncryptionHelper.encrypt(companyID.toString()));
            sortedParamMap.put("user_key", EncryptionHelper.encrypt(keyID.toString()));
            sortedParamMap.put("user_type", EncryptionHelper.encrypt(paymentType));

            if (userDefinedUrl != null && !userDefinedUrl.isEmpty()) {
                sortedParamMap.put("user_url", userDefinedUrl);
            }
            sortedParamMap.put("vpc_AccessCode", invoicingSettings.getMasterCardAccessCode());
            sortedParamMap.put("vpc_Amount", amountString);
            sortedParamMap.put("vpc_Command", "pay");
            sortedParamMap.put("vpc_Currency", currencyName);
            sortedParamMap.put("vpc_Locale", "en");

            numberReference = numberReference + "_" + new Date().getTime();
            sortedParamMap.put("vpc_MerchTxnRef", numberReference);
            sortedParamMap.put("vpc_Merchant", invoicingSettings.getMasterCardMerchandID());
            sortedParamMap.put("vpc_OrderInfo", "ORDER_" + numberReference);
            sortedParamMap.put("vpc_ReturnURL", EdsContextParams.getFullHost() + CommandConstants.COMMON_URL + "/mastercardPaymentNotification");
            sortedParamMap.put("vpc_Version", "1");

            return new MasterCardSecureHashGenerator(invoicingSettings.getMasterCardSecretKey(), sortedParamMap, true).getGeneratedURL();
        }
        return null;
    }

    @Override
    public boolean isMasterCardParametersValid(Integer invoiceID, Integer companyID) {
        if (invoiceID == null) {
            return false;
        }
        ArrayList<String> supportedCurrencies = new ArrayList<>();
        supportedCurrencies.add("OMR");

        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(companyManager.get(companyID));
        EdsInvoice invoice = invoiceManager.get(invoiceID);

        return supportedCurrencies.contains(invoice.getCurrency().getName()) && isValid(invoicingSettings.getMasterCardMerchandID()) && isValid(invoicingSettings.getMasterCardAccessCode()) && isValid(invoicingSettings.getMasterCardSecretKey());
    }

    @Override
    public boolean isElavonParametersValid(Integer invoiceID, Integer companyID) {
        if (invoiceID == null) {
            return false;
        }
        ArrayList<String> supportedCurrencies = new ArrayList<>();
        supportedCurrencies.add("USD");

        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(companyManager.get(companyID));
        EdsInvoice invoice = invoiceManager.get(invoiceID);

        return supportedCurrencies.contains(invoice.getCurrency().getName()) && isValid(invoicingSettings.getElavonMerchandID()) && isValid(invoicingSettings.getElavonUserID()) && isValid(invoicingSettings.getElavonPIN());
    }

    @Override
    public InvoiceList getPurchaseInvoiceListFromSolr(ListingFilterParameter filterParameter) {
        ListResult<NewInvoice> invoiceListResult = invoiceService.getPurchaseInvoiceDataFromSolr(filterParameter);
        return new InvoiceList(invoiceListResult.getList(), invoiceListResult.getTotal());
    }

    private boolean isValid(String value) {
        return value != null && !value.isEmpty();
    }

    @Override
    public String validateMasterCardLinkParameters(Integer companyID, Integer keyID, BigDecimal paymentAmount, String paymentType) {
        if (MastercardPaymentHandler.INVOICE.equals(paymentType)) {
            EdsInvoice invoice = invoiceManager.get(keyID);
            if (PAID.equals(invoice.getStatus().getCode())) {
                return "Invoice is already paid";
            }

            if (!(APPROVE.equals(invoice.getStatus().getCode()) || OPEN.equals(invoice.getStatus().getCode()) || OVER_DUE.equals(invoice.getStatus().getCode()))) {
                return "Invoice is not approved";
            }

            if (paymentAmount.setScale(2, RoundingMode.HALF_UP).compareTo(invoice.getDueAmount().setScale(2, RoundingMode.HALF_UP)) > 0) {
                return "Payment amount more than due amount";
            }
        } else if (MastercardPaymentHandler.COURSE_BOOKING.equals(paymentType)) {
            EdsCourseBooking courseBooking = courseBookingManager.get(keyID);

            if (courseBooking.getStatus() != null && PAID.equals(courseBooking.getStatus().getCode())) {
                return "This course booking is already paid";
            }

            if (paymentAmount.compareTo(courseBooking.getCalculatedAmount()) > 0) {
                return "Payment amount more than course booking calculated amount";
            }
        }
        return null;
    }

    @Override
    public SelectItem[] getInvoiceAndQuoteLookUpItems(ListingFilterParameter fp, CustomFieldLookUpTypeEnum typeEnum) {
        StringBuilder solrQuery = new StringBuilder(this.getSaleInvoiceSolrQuery(fp, employeeManager.getUser(), false, null));
        String fieldId = SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID;
        String fieldNumber = SolrSaleInvoiceRepresenter.FIELD_INVOICE_NUMBER;
        SolrClient server = null;
        if (CustomFieldLookUpTypeEnum.SALES_INVOICE.equals(typeEnum)) {
            server = WfmJpaTemplate.getSolrServerForCore(SOLR_SALEINVOICE_CORE);

        } else if (CustomFieldLookUpTypeEnum.SALES_QUOTE.equals(typeEnum)) {
            solrQuery = new StringBuilder(this.getSaleQuoteSolrQuery(fp, employeeManager.getUser(), false, null));
            server = WfmJpaTemplate.getSolrServerForCore(SOLR_SALEQUOTE_CORE);

        } else if (CustomFieldLookUpTypeEnum.PURCHASE_INVOICE.equals(typeEnum)) {
            solrQuery = new StringBuilder(this.getPurchaseInvoiceCoreSolrQuery(fp, employeeManager.getUser(), null));
            fieldId = SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID;
            fieldNumber = SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_NUMBER;
            server = WfmJpaTemplate.getSolrServerForCore(SOLR_PURCHASE_INVOICE_CORE);

        } else if (CustomFieldLookUpTypeEnum.PURCHASE_ORDER.equals(typeEnum)) {
            solrQuery = new StringBuilder(this.getPurchaseOrderSolrQuery(fp, employeeManager.getUser(), false));
            server = WfmJpaTemplate.getSolrServerForCore(SOLR_PURCHASE_ORDER_CORE);
        } else if (CustomFieldLookUpTypeEnum.SALES_ORDER.equals(typeEnum)) {
            solrQuery = new StringBuilder(this.getSaleOrderSolrQuery(fp, employeeManager.getUser(), false, null));
            server = WfmJpaTemplate.getSolrServerForCore(SOLR_SALEQUOTE_CORE);
        }
        QueryResponse resp = null;
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(0);
        query.setParam(CommonParams.ROWS, "10");
        try {
            resp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        SolrDocumentList solrDocumentList = resp.getResults();
        List<SelectItem> items = Lists.newArrayList();
        for (SolrDocument relevantDoc : solrDocumentList) {
            SelectItem item = new SelectItem();
            item.setId(SolrUtils.asInteger(relevantDoc, fieldId));
            item.setName(SolrUtils.asString(relevantDoc, fieldNumber));
            items.add(item);
        }

        return items.toArray(new SelectItem[]{});
    }

//    public InvoiceDto wrapInvoiceToDto(EdsInvoice invoice) {
//        return wrapInvoiceToDto(invoice, null);
//    }

    public InvoiceDto wrapInvoiceToDto(EdsInvoice invoice, ArrayList<CompanyCustomFieldItem> customFieldsItems) {
        InvoiceDto dto = new InvoiceDto();
        //wrap base data
        wrapToDto(invoice, dto);
        dto.setCreditNote(invoice.isCreditNote());
        dto.setTaxCalcType(getTaxCalcTypeAsString(invoice.getTaxCalculationType()));

        if (invoice.getReceivablePayable() != null) {
            EdsAccount rpAccount = invoice.getReceivablePayable();
            dto.setAccountsReceivable(new IdCode(rpAccount.getObjectID(), rpAccount.getAccountCode()));
        }
        if (invoice.getPriceLevelID() != null) {
            Optional.ofNullable(priceLevelManager.get(invoice.getPriceLevelID())).ifPresent(priceLevel -> dto.setPriceLevel(new ItemDto(priceLevel.getObjectID(), priceLevel.getName())));
        }
        if (invoice instanceof EdsSaleInvoice saleInvoice) {
            dto.setQuoteNumber(saleInvoice.getQuoteNumber());

            if (saleInvoice.getBankAccount() != null) {
                dto.setBankAccount(new IdDTO(saleInvoice.getBankAccount().getObjectID()));
            }
            if (saleInvoice.getOpportunityID() != null) {
                Optional.ofNullable(opportunityManager.get(saleInvoice.getOpportunityID())).ifPresent(opp -> dto.setOpportunity(new ItemDto(opp.getObjectID(), opp.getName(), opp.getNumber())));
            }
            if (saleInvoice.getStatus() != null) {
                Optional.ofNullable(referenceManager.get(saleInvoice.getStatus().getObjectID())).ifPresent(ref -> dto.setStatusColor(ref.getColor()));
            }
        }
        if (!CollectionUtils.isEmpty(invoice.getConvertedQuotes())) {
            EdsQuote quote = invoice.getConvertedQuotes().iterator().next();
            dto.setConvertedItem(new IdCode(quote.getObjectID(), quote.getNumber(), quote.getObjectKey()));
        }
        if (customFieldsItems == null) {
            customFieldsItems = commonServiceLocal.getCompanyCustomFields(invoice instanceof EdsSaleInvoice ? ViewName.SaleInvoice : ViewName.PurchaseInvoice);
        } else
            customFieldsItems = new ArrayList<>(customFieldsItems);

        if (invoice.getCustomFields() != null && !CollectionUtils.isEmpty(customFieldsItems)) {
            customFieldsItems = CustomFieldsUtils.setRPCCustomFieldItems(invoice.getCustomFields(), customFieldsItems);
            dto.setCustomFields(customFieldsItems.stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
        }

        List<LineItemDto> lineItemDtoList = new ArrayList<>();
        for (EdsInvoiceItem invoiceItem : invoice.getInvoiceItems()) {
            LineItemDto lineItemDto = wrapListItemToDto(invoiceItem, invoice.getItemCustomFields(), invoice instanceof EdsSaleInvoice ? ApiConstants.SALES_INVOICE : ApiConstants.PURCHASE_INVOICE);
            lineItemDtoList.add(lineItemDto);
        }
        dto.setItems(lineItemDtoList);
        dto.setDiscountTotal(invoice.getTotalDiscount());

        BigDecimal paidAmount = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(invoice.getPayments())) {
            dto.setPayments(invoice.getPayments().stream().filter(payment -> !payment.isDeleted() && (payment.getStatus() == null || !REVERSED.equals(payment.getStatus().getCode()))).map(this::wrapPaymentToDto).collect(Collectors.toList()));
            dto.setPaidAmount(invoice.getPayments().stream()
                    .filter(payment -> !payment.isDeleted() && (payment.getStatus() == null || !REVERSED.equals(payment.getStatus().getCode())))
                    .reduce(BigDecimal.ZERO, (total, payment) -> total.add(Optional.ofNullable(payment.getAmountInInvoiceCurrency()).orElse(payment.getAmount())), BigDecimal::add));
        }
        if (!CollectionUtils.isEmpty(invoice.getRefunds())) {
            dto.setRefunds(invoice.getRefunds().stream().filter(refund -> !refund.isDeleted() && (refund.getStatus() == null || !REVERSED.equals(refund.getStatus().getCode()))).map(this::wrapPaymentToDto).collect(Collectors.toList()));
            dto.setPaidAmount(invoice.getRefunds().stream()
                    .filter(refund -> !refund.isDeleted() && (refund.getStatus() == null || !REVERSED.equals(refund.getStatus().getCode())))
                    .reduce(BigDecimal.ZERO, (total, refund) -> total.add(Optional.ofNullable(refund.getAmountInInvoiceCurrency()).orElse(refund.getAmount())), BigDecimal::add));
        }
        dto.setDueAmount(invoice.getDueAmount());
        if (invoice.getCreditNoteInvoice() != null) {
            dto.setCreditedInvoice(new IdCode(invoice.getCreditNoteInvoice().getObjectID(), invoice.getCreditNoteInvoice().getNumber(), invoice.getCreditNoteInvoice().getObjectKey()));
        }
        return dto;
    }

    public OrderDto wrapQuoteToDto(EdsSaleQuote quote, ArrayList<CompanyCustomFieldItem> customFieldsItems) {
        OrderDto dto = new OrderDto();
        //wrap base data
        wrapToDto(quote, dto);
        dto.setTaxCalcType(getTaxCalcTypeAsString(quote.getTaxCalculationType()));
        if (quote.getInvoiceTerms() != null) {
            dto.setTerms(new IdName(quote.getInvoiceTerms().getObjectID(), quote.getInvoiceTerms().getName()));
            dto.setDueDateType("TERMS");
        }

        if (quote.getPriceLevelID() != null) {
            Optional.ofNullable(priceLevelManager.get(quote.getPriceLevelID())).ifPresent(priceLevel -> dto.setPriceLevel(new ItemDto(priceLevel.getObjectID(), priceLevel.getName())));
        }
        if (customFieldsItems == null) {
            customFieldsItems = commonServiceLocal.getCompanyCustomFields(quote.isSalesOrder() ? ViewName.SaleOrder : ViewName.SaleQuote);
        } else
            customFieldsItems = new ArrayList<>(customFieldsItems);

        if (quote.getCustomFields() != null && !CollectionUtils.isEmpty(customFieldsItems)) {
            customFieldsItems = CustomFieldsUtils.setRPCCustomFieldItems(quote.getCustomFields(), customFieldsItems);
            dto.setCustomFields(customFieldsItems.stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
        }

        List<LineItemDto> lineItemDtoList = new ArrayList<>();
        for (EdsQuoteItem quoteItem : quote.getQuoteItems()) {
            LineItemDto lineItemDto = wrapListItemToDto(quoteItem, quote.getItemCustomFields(), quote.isSalesOrder() ? ApiConstants.SALES_ORDER : ApiConstants.SALES_QUOTE);
            lineItemDtoList.add(lineItemDto);
        }
        dto.setItems(lineItemDtoList);
        dto.setDiscountTotal(quote.getTotalDiscount());

        return dto;
    }

    public OrderDto wrapPurchaseOrderToDto(EdsPurchaseOrder order, ArrayList<CompanyCustomFieldItem> customFieldsItems) {
        OrderDto dto = new OrderDto();
        //wrap base data
        wrapToDto(order, dto);
        dto.setTaxCalcType(getTaxCalcTypeAsString(order.getTaxCalculationType()));

        if (order.getPriceLevelID() != null) {
            Optional.ofNullable(priceLevelManager.get(order.getPriceLevelID())).ifPresent(priceLevel -> dto.setPriceLevel(new ItemDto(priceLevel.getObjectID(), priceLevel.getName())));
        }
        if (customFieldsItems == null) {
            customFieldsItems = commonServiceLocal.getCompanyCustomFields(ViewName.PurchaseOrder);
        } else
            customFieldsItems = new ArrayList<>(customFieldsItems);

        if (order.getCustomFields() != null && !CollectionUtils.isEmpty(customFieldsItems)) {
            customFieldsItems = CustomFieldsUtils.setRPCCustomFieldItems(order.getCustomFields(), customFieldsItems);
            dto.setCustomFields(customFieldsItems.stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
        }

        List<LineItemDto> lineItemDtoList = new ArrayList<>();
        for (EdsQuoteItem quoteItem : order.getQuoteItems()) {
            LineItemDto lineItemDto = wrapListItemToDto(quoteItem, order.getItemCustomFields(), ApiConstants.PURCHASE_ORDER_ITEM);
            lineItemDtoList.add(lineItemDto);
        }
        dto.setItems(lineItemDtoList);
        dto.setDiscountTotal(order.getTotalDiscount());

        return dto;
    }

    void wrapToDto(EdsBaseInvoice baseInvoice, BaseInvoiceDto dto) {
        dto.setId(baseInvoice.getObjectID());
        dto.setObjectKey(baseInvoice.getObjectKey());
        dto.setNumber(baseInvoice.getNumber());
        dto.setReference(baseInvoice.getReference());

        EdsCrmAccount client = baseInvoice.getClientOrSupplier();
        ItemDto customerOrSupplier = new ItemDto(client.getObjectID(), client.getName(), client.getNumber());
        if (RECEIVABLE.equals(baseInvoice.getType())) {
            dto.setCustomer(customerOrSupplier);
        } else {
            dto.setSupplier(customerOrSupplier);
        }

        EdsCrmContact clientContact = baseInvoice.getClientContact();
        if (clientContact != null) {
            ItemDto contactDto = new ItemDto(clientContact.getObjectID(), clientContact.getFullName(), clientContact.getNumber());
            contactDto.addProperty("email", clientContact.getPrimaryEmail());
            dto.setContact(contactDto);
        }
        if (baseInvoice.getRelatedProject() != null) {
            EdsProject project = baseInvoice.getRelatedProject();
            IdCode projDto = new IdCode(project.getObjectID(), project.getNumber());
            projDto.addProperty("name", project.getName());
            dto.setProject(projDto);
        }
        dto.setDate(baseInvoice.getInvoiceDate());
        dto.setDueDate(baseInvoice.getDueDate());

        EdsReference status = baseInvoice.getStatus();
        dto.setStatus(status.getName());
        dto.setStatusCode(status.getCode());
        dto.setCurrencyCode(baseInvoice.getCurrency().getName());
        dto.setExchangeRate(baseInvoice.getExchangeRate());

        if (baseInvoice.getBillAddressID() != null) {
            Optional.ofNullable(addressManager.get(baseInvoice.getBillAddressID())).ifPresent(address -> dto.setBillingAddress(ConvertUtils.toDto(address.getRPC())));
        }
        if (baseInvoice.getMailAddressID() != null) {
            Optional.ofNullable(addressManager.get(baseInvoice.getMailAddressID())).ifPresent(address -> dto.setShippingAddress(ConvertUtils.toDto(address.getRPC())));
        }
        if (baseInvoice.getCurrentApprover() != null) {
            Optional.ofNullable(baseInvoice.getCurrentApprover().getExactEmployee()).ifPresent(approver -> dto.setCurrentApprover(new ItemDto(approver.getObjectID(), approver.getFullName())));
        }

        dto.setSubTotal(baseInvoice.getSubtotal());
        dto.setTaxTotal(baseInvoice.getTotalTaxes());
        dto.setTotal(baseInvoice.getTotalInInvoiceCurrency());
        dto.setTotalInBase(baseInvoice.getTotal());
        dto.setCreatedDate(ServerUtils.getDateAsString(baseInvoice.getCreationDate(), true));
        dto.setUpdatedDate(ServerUtils.getDateAsString(baseInvoice.getUpdatedDate(), true));
    }

    public LineItemDto wrapListItemToDto(EdsBaseInvoiceItem invoiceItem, List<CompanyCustomFieldItem> customFieldsItems, String transactionType) {
        LineItemDto lineItemDto = new LineItemDto();
        if (invoiceItem.getItem() != null) {
            EdsItem item = invoiceItem.getItem();
            ItemDto itemDto = new ItemDto(item.getObjectID(), item.getName(), item.getProductNumber());
            itemDto.addProperty("type", item.getTypeName());
            itemDto.addProperty("objectKey", item.getObjectKey());
            lineItemDto.setProduct(itemDto);
        } else {
            lineItemDto.setProduct(new ItemDto(null, invoiceItem.getItemName()));
        }
        lineItemDto.setDescription(invoiceItem.getDescription());
        lineItemDto.setQuantity(invoiceItem.getQty());
        lineItemDto.setUnitPrice(invoiceItem.getUnitPrice());
        Optional.ofNullable(invoiceItem.getAccount()).ifPresent(account -> lineItemDto.setAccount(new ItemDto(account.getObjectID(), account.getName(), account.getAccountCode())));
        Optional.ofNullable(invoiceItem.getVat()).ifPresent(vat -> {
            ItemDto taxItem = new ItemDto(vat.getObjectID(), vat.getTaxNameAndRateAsString());
            taxItem.addProperty("amount", String.valueOf(invoiceItem.getTaxAmount()));
            lineItemDto.setTaxItem(taxItem);
        });
        Optional.ofNullable(invoiceItem.getDiscount()).ifPresent(lineItemDto::setDiscount);

        if (customFieldsItems == null) {
            customFieldsItems = getLineItemCustomFields(transactionType);
        } else {
            customFieldsItems = new ArrayList<>(customFieldsItems);
        }
        if (invoiceItem.getCustomFields() != null && !CollectionUtils.isEmpty(customFieldsItems)) {
            customFieldsItems = CustomFieldsUtils.setRPCCustomFieldItems(invoiceItem.getCustomFields(), (ArrayList<CompanyCustomFieldItem>) customFieldsItems);
            lineItemDto.setCustomFields(customFieldsItems.stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
        }
        return lineItemDto;
    }

    public InvoicePaymentDto wrapPaymentToDto(EdsInvoicePayment payment) {
        InvoicePaymentDto dto = new InvoicePaymentDto();
        dto.setDate(payment.getPaymentDate());
        dto.setAmount(Optional.ofNullable(payment.getAmountInInvoiceCurrency()).orElse(payment.getAmount()));
        Optional.ofNullable(payment.getCurrencyID()).ifPresent(currencyId -> dto.setCurrency(currencyManager.get(currencyId).getName()));
        dto.setExchangeRate(payment.getExchangeRate());
        EdsAccount edsAccount = payment.getAccount();

        IdCode paymentAccount = new IdCode(edsAccount.getObjectID(), edsAccount.getAccountCode());
        paymentAccount.addProperty("name", edsAccount.getName());
        dto.setAccount(paymentAccount);
        dto.setReference(payment.getReference());
        return dto;
    }

    ArrayList<CompanyCustomFieldItem> getLineItemCustomFields(String transactionType) {
        return switch (transactionType) {
            case ApiConstants.SALES_QUOTE ->
                    CustomFieldsUtils.setRPCCustomFieldItems(null, commonServiceLocal.getCompanyCustomFields(ViewName.SaleQuoteItem));
            case ApiConstants.SALES_ORDER ->
                    CustomFieldsUtils.setRPCCustomFieldItems(null, commonServiceLocal.getCompanyCustomFields(ViewName.SaleOrderItem));
            case ApiConstants.SALES_INVOICE ->
                    CustomFieldsUtils.setRPCCustomFieldItems(null, commonServiceLocal.getCompanyCustomFields(ViewName.SaleInvoiceItem));
            case ApiConstants.PURCHASE_INVOICE ->
                    CustomFieldsUtils.setRPCCustomFieldItems(null, commonServiceLocal.getCompanyCustomFields(ViewName.PurchaseInvoiceItem));
            case ApiConstants.PURCHASE_ORDER ->
                    CustomFieldsUtils.setRPCCustomFieldItems(null, commonServiceLocal.getCompanyCustomFields(ViewName.PurchaseOrderItem));
            default -> null;
        };
    }

    String getTaxCalcTypeAsString(Integer taxCalcType) {
        TaxTypeEnum type = TaxTypeEnum.getTaxTypeById(taxCalcType);
        return type != null ? type.getName() : null;
    }
}
