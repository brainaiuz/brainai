package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.approving.EdsApproverRoles;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.settings.EdsRestHook;
import com.edatasite.workforce.core.solr.component.ProductsServicesSolrComponent;
import com.edatasite.workforce.core.solr.document.ProductsServicesSolrDoc;
import com.edatasite.workforce.gwt.accounting.client.rpc.*;
import com.edatasite.workforce.gwt.accounting.client.rpc.LogHistoryItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountMultiRangeItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.*;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.AiImageResult;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.AiSavedPrompt;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.ProductPicture;
import com.edatasite.workforce.gwt.accounting.server.app.itemBatches.ItemBatchServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.itemserials.ItemSerialServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProductServiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.MessageCommand;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.core.server.app.*;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.*;
import com.edatasite.workforce.gwt.core.server.db.currency.ExchangeCurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.ItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.RestHookManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.ProductEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.StockAdjustmentEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.StockTransferEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.TransactionCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.ItemSerialEntityType;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.profile.client.ui.EmailNotificationConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductCategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductListItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductTypeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ZapierProductVariantTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
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
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Feb 3, 2011
 * Time: 11:48:45 AM
 */
@Transactional
@Service("productService")
public class ProductServiceImpl implements ProductService, ProductServiceLocal, AccountingConstants, Constants {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    @Qualifier("accountingService")
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private ItemCommentManager itemCommentManager;
    @Autowired
    private UnitMeasurementManager unitMeasurementManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private ProductCategoryManager productCategoryManager;
    @Autowired
    private DiscountManager discountManager;
    @Autowired
    private BrandManager brandManager;
    @Autowired
    private VatManager vatManager;
    @Autowired
    private WarehouseManager warehouseManager;
    @Autowired
    private ProductLocationManager productLocationManager;
    @Autowired
    private ProductWarehouseLocationManager productWarehouseLocationManager;
    @Autowired
    private ProductKitItemManager productKitItemManager;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private ItemStockManager itemStockManager;
    @Autowired
    private ItemCFManager itemCFManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    protected SolrManager solrManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    @Qualifier("accountingService")
    private AccountingService accountingService;
    @Autowired
    private SavedAssemblyItemManager savedAssemblyItemManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private StockAdjustmentManager stockAdjustmentManager;
    @Autowired
    private StockAdjustmentItemManager stockAdjustmentItemManager;
    @Autowired
    private AssemblyItemManager assemblyItemManager;
    @Autowired
    private ProductPictureManager productPictureManager;
    @Autowired
    private NanoBananaClient nanoBananaClient;
    @Autowired
    private AiSavedPromptManager aiSavedPromptManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private SubsidiaryProductManager subsidiaryProductManager;
    @Autowired
    private AssemblyHistoryManager assemblyHistoryManager;
    @Autowired
    private LocaleManager localeManager;
    @Autowired
    private ExpenseCategoryManager categoryManager;
    @Autowired
    private ExpenseManager expenseManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private ProjectBudgetManager projectBudgetManager;
    @Autowired
    private StockTransferManager stockTransferManager;
    @Autowired
    private ProductSerialManager productSerialManager;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private ItemMultiPriceManager itemMultiPriceManager;
    @Autowired
    protected InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private ExchangeCurrencyManager exchangeCurrencyManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private ApproverManager approverManager;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private ConsignmentManager consignmentManager;
    @Autowired
    private EmailNotificationSettingsManager emailNotificationSettingsManager;
    @Autowired
    private QuoteService quoteService;
    @Autowired
    private ItemSerialServiceLocal itemSerialService;
    @Autowired
    private RestHookManager restHookManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private ItemBatchServiceLocal itemBatchService;
    @Autowired
    protected ItemBatchManager itemBatchManager;
    @Autowired
    @Qualifier("invoiceService")
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    @Qualifier("allInOneService")
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private CompanyPdfTemplateManager companyPdfTemplateManager;
    @Autowired
    private StockTransferNoteManager stockTransferNoteManager;
    @Autowired
    private StockAdjustmentNoteManager stockAdjustmentNoteManager;
    @Autowired
    private AttachmentManager attachmentsManager;
    @Autowired
    private ProductsServicesSolrComponent productsServicesSolrComponent;
    @Autowired
    private LocationManager locationManager;

    RestTemplate restTemplate = new RestTemplate();


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ProductItem> getProductsList(ListingFilterParameter filterParametrs) {
        List<EdsItem> productItems = itemManager.getCompanyItemList(filterParametrs);

        int totalCount = productItems.size();
        if (filterParametrs != null) {
            productItems = ListUtils.getSublist(productItems, filterParametrs.getStart(), filterParametrs.getLimit());
        }

        ArrayList<ProductItem> items = new ArrayList<>();
        for (EdsItem pi : productItems) {
            ProductItem item = new ProductItem();
            item.setObjectId(pi.getObjectID());
            item.setType(pi.getType());
            item.setProductRentalItemId(pi.getRentItem() != null ? pi.getRentItem().getObjectID() : null);
            item.setTypeName(pi.getTypeName());
            item.setName(pi.getName());
            item.setDescription(pi.getDescription());
            item.setUnitpPrice(pi.getSellingPrice());
            item.setStorefrontEnable(pi.isStorefrontEnable());
            item.setItemsInStock(pi.getQty());

            if (pi.getParent() != null) {
                item.setParentId(pi.getParent().getObjectID());
            }

            if (pi.getAccount() != null) {
                item.setAccount(pi.getAccount().getName());
                item.setAccountID(pi.getAccount().getObjectID());
            }

            if (pi.getCogsAccount() != null) {
                item.setCogsAccount(pi.getCogsAccount().getName());
                item.setCogsAccountID(pi.getCogsAccount().getObjectID());
            }

            if (pi.getAssetAccount() != null) {
                item.setAssetAccount(pi.getAssetAccount().getName());
                item.setAssetAccountID(pi.getAssetAccount().getObjectID());
            }

            if (pi.getVat() != null) {
                item.setTaxRate(pi.getVat().getName());
                item.setTaxRateID(pi.getVat().getObjectID());
            }

            if (PRODUCT_KIT.equals(pi.getType())) {
                item.setProductKitItems(wrapProductKitItems(pi.getProductKitItems()));
            }
            items.add(item);
        }
        return new ListResult<>(items, totalCount);
    }

    @Override
    public ListResult<ProductItem> getProductsListFromSolr(ListingFilterParameter filterParametrs) {
        return getProductsListFromSolrGeneric(filterParametrs, Collections.emptyList());
    }

    public ListResult<ProductItem> getProductsListFromSolrGeneric(ListingFilterParameter filterParametrs, List<String> searchInCustomFields) {
        return getProductsServicesListResponse(filterParametrs, getProductListSolrQuery(filterParametrs, searchInCustomFields));
    }

    public String getProductListSolrQuery(ListingFilterParameter filterParametrs, List<String> searchInCustomFields) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsItem.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get product list");
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        FacetFilterRpc productsFacetFilter = filterParametrs.getFacetFilter();
        if (productsFacetFilter != null && !productsFacetFilter.isFilterChanges()) {
            productsFacetFilter = commonServiceLocal.getUserFacetFilter(productsFacetFilter);
        }
        EdsUser edsUser = employeeManager.getUser();
        StringBuilder solrQuery = new StringBuilder();
        if (CollectionUtils.isNotEmpty(searchInCustomFields)) {
            solrQuery.append(QueryBuilderForSolr.getProductsServicesCoreSolrQueryCF(filterParametrs, searchInCustomFields));
        } else {
            solrQuery.append(QueryBuilderForSolr.getProductsServicesCoreSolrQuery(filterParametrs));
        }
        if (!ServerUtils.hasPermission(PermissionConstants.PRODUCT_SEE_ALL)) {
            if (ServerUtils.hasPermission(PermissionConstants.PRODUCT_SEE_OWN_LOCATION) && edsUser.getLocation() != null) {
                solrQuery.append(" AND ").append(SolrProductServiceRepresenter.FIELD_MULTI_LOCATION_ID).append(":").append(edsUser.getLocation().getObjectID());
            } else if (!(edsUser.hasRole(roleManager.getByCode(SUPPLIER)) && edsUser instanceof EdsClientContact)) {
                    solrQuery.append(" AND ").append(SolrProductServiceRepresenter.FIELD_CREATOR_ID).append(":").append(edsUser.getObjectID());
            }
        }
        if (productsFacetFilter != null) {
            Set<String> keySet = productsFacetFilter.getShowSolrFieldMap().keySet();
            for (String key : keySet) {
                if (key.equals("status") && productsFacetFilter.getFacetContentMap().containsKey(key)) {
                    if (productsFacetFilter.getShowSolrFieldMap().get(key) != null) {
                        productsFacetFilter.getShowSolrFieldMap().get(key).setWithID(false);
                    }
                }
            }
        }
        if (filterParametrs.getObjectIDs() != null && !filterParametrs.getObjectIDs().isEmpty()) {
            solrQuery.append(" AND " + SolrProductServiceRepresenter.FIELD_PRODUCT_ID).append(": (").append(ServerUtils.getAsCommoDelimited(filterParametrs.getObjectIDs(), "0", " ")).append(")");
        }
        if (productsFacetFilter != null && productsFacetFilter.getFacetContentMap() != null && productsFacetFilter.getFacetContentMap().get(FacetContentType.ProductsServicesFacetFilter.getContentCode()[11]) != null
                && productsFacetFilter.getFacetContentMap().get(FacetContentType.ProductsServicesFacetFilter.getContentCode()[11]).getFacetItems() != null && productsFacetFilter.getFacetContentMap().get(FacetContentType.ProductsServicesFacetFilter.getContentCode()[11]).getFacetItems().length > 0) {
            filterParametrs.setWarehouseID(productsFacetFilter.getFacetContentMap().get(FacetContentType.ProductsServicesFacetFilter.getContentCode()[11]).getFacetItems()[0].getId());
            productsFacetFilter.getFacetContentMap().get(FacetContentType.ProductsServicesFacetFilter.getContentCode()[11]).setFacetItems(null);
        }
        solrQuery.append(SolrFacetUtils.generateForPricesFacet(productsFacetFilter,
                FacetContentType.ProductsServicesFacetFilter.getContentCode()[2]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(productsFacetFilter, edsUser.getCompany(),
                null, null,
                FacetContentType.ProductsServicesFacetFilter.getContentCode()[2]));

        if (filterParametrs.getWarehouseID() != null && filterParametrs.getWarehouseID() > 0) {
            solrQuery.append(" AND ({!parent which=" + SolrProductServiceRepresenter.FIELD_DOC_TYPE + ":" + SolrProductServiceRepresenter.PRODUCT_SOLR_DOC +
                    "}" + SolrProductServiceRepresenter.FIELD_WAREHOUSE_ID + ":" + filterParametrs.getWarehouseID() + ")");
        }

        if (filterParametrs.getParentID() != null && filterParametrs.getParentID() > 0) {
            EdsItem item = itemManager.get(filterParametrs.getParentID());
            if (item != null) {
                solrQuery.append(" AND " + SolrProductServiceRepresenter.FIELD_PRODUCT_PARENT_ID);
                solrQuery.append(":(");
                solrQuery.append(filterParametrs.getParentID());
                if (item.getParent() != null && item.getParent().getObjectID() > 0) {
                    solrQuery.append(" ").append(item.getParent().getObjectID());
                }
                solrQuery.append(")");
            }
        }
        if (filterParametrs.getProductType() != null) {
            Integer productType = filterParametrs.getProductType();
            solrQuery.append(" AND " + SolrProductServiceRepresenter.FIELD_PRODUCT_TYPE_ID).append(":(").append(productType).append(")");
        } else {
            solrQuery.append(" AND NOT (" + SolrProductServiceRepresenter.FIELD_PRODUCT_TYPE_ID).append(":(").append(RENTAL_ITEM).append("))");
        }

        if (CollectionUtils.isEmpty(searchInCustomFields) && filterParametrs.isFromMobile() && filterParametrs.getSearchKey() != null && !filterParametrs.getSearchKey().isEmpty()) {
            List<Integer> itemIDList = itemStockManager.getItemsByUpsNumber(filterParametrs.getSearchKey());
            if (!itemIDList.isEmpty()) {
                solrQuery.append((filterParametrs.getWarehouseID() != null ? "  AND ( " : " OR (") + SolrProductServiceRepresenter.FIELD_PRODUCT_ID).append(":(").append(ServerUtils.getAsCommoDelimited(itemIDList, "0", " ")).append("))");
            }
            solrQuery.append(" ) ");
        }
        if (filterParametrs.getSupplierId() != null) {
            solrQuery.append(" AND " + SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_ID).append(":").append(filterParametrs.getSupplierId());
        } else if ((edsUser.hasRole(roleManager.getByCode(SUPPLIER)) && edsUser instanceof EdsClientContact)) {
            solrQuery.append(" AND " + SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_ID).append(":").append(edsUser.getClientContact().getClientID());
        }
        if (filterParametrs.getCategoryID() != null) {
            if (filterParametrs.isFromMobile()) {
                if (filterParametrs.isShowChild() != null && filterParametrs.isShowChild()) {
                    List<Integer> categoryIDs = productCategoryManager.getAllSubCategoryIDsByCategoryId(filterParametrs.getCategoryID());
                    solrQuery.append(" AND (" + SolrProductServiceRepresenter.FIELD_CATEGORY_ID).append(":(").append(ServerUtils.getAsCommoDelimited(categoryIDs, "0", " ")).append("))");
                } else {
                    solrQuery.append(" AND (" + SolrProductServiceRepresenter.FIELD_CATEGORY_ID).append(":(").append(filterParametrs.getCategoryID()).append("))");
                }
            } else {
                List<Integer> categoryIDs = productCategoryManager.getAllSubCategoryIDsByCategoryId(filterParametrs.getCategoryID());
                solrQuery.append(" AND (" + SolrProductServiceRepresenter.FIELD_CATEGORY_ID).append(":(").append(ServerUtils.getAsCommoDelimited(categoryIDs, "0", " ")).append("))");
            }
        }
        if (filterParametrs.getBrandID() != null) {
            solrQuery.append(" AND (" + SolrProductServiceRepresenter.FIELD_BRAND_ID).append(":(").append(filterParametrs.getBrandID()).append("))");
        }
        if (filterParametrs.getProductId() != null) {
            solrQuery.append(" AND (" + SolrProductServiceRepresenter.FIELD_PRODUCT_RENTAL_ITEM_ID).append(":(").append(filterParametrs.getProductId()).append("))");
        }
        //if (CollectionUtils.isNotEmpty(searchInCustomFields)) {
        System.out.println("PRODUCT SOLR QUERY: " + solrQuery);
        //}
        return solrQuery.toString();
    }

    private ListResult<ProductItem> getProductsServicesListResponse(ListingFilterParameter filterParameter, String solrQuery) {
        Page<ProductsServicesSolrDoc> productsServicesSolrDocs = productsServicesSolrComponent.getList(filterParameter, solrQuery);
        return getProductsServicesFromSolrResult(productsServicesSolrDocs, filterParameter);
    }

    private ListResult<ProductItem> getProductsServicesFromSolrResult(Page<ProductsServicesSolrDoc> productsServicesSolrDocs, ListingFilterParameter filterParameter) {

        boolean hasOnlySalesPersonRole = roleManager.hasOnlySalesPersonRole(userManager.getUser());
        boolean isWarehouseAllocationEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.WAREHOUSE_ALLOCATION_ENABLE);

        int totalNumber = isWarehouseAllocationEnabled && hasOnlySalesPersonRole ? 0 : (int) productsServicesSolrDocs.getTotalElements();

        ArrayList<ProductItem> productItemList = new ArrayList<>();
        ListPanelToolRpc panelSettings = filterParameter.getListPanelTool();
        boolean isCustomSubItemsEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CUSTOM_INVOICE_ITEM_PRODUCT_DESCRIPTION_ENABLED);

        if (productsServicesSolrDocs != null && productsServicesSolrDocs.getContent() != null && !productsServicesSolrDocs.getContent().isEmpty()) {
            String itemIds = productsServicesSolrDocs.getContent().stream().map(ProductsServicesSolrDoc::getProductId).map(String::valueOf).collect(Collectors.joining(","));
            itemIds = itemIds.isEmpty() ? "-1" : itemIds;
            HashMap<Integer, Integer> defaultItemPicturesMap = null, defaultItemMiniPicturesMap = null;

            ArrayList<String> activeColumns = filterParameter.getListPanelTool() != null ? filterParameter.getListPanelTool().getColumnCodeName() : new ArrayList<>();
            if (activeColumns.contains(ProductItem.PICTURE)) {
                defaultItemPicturesMap = productPictureManager.getProductPicturesForListing(itemIds, 0);
                defaultItemMiniPicturesMap = productPictureManager.getProductPicturesForListing(itemIds, 2);
            }
            HashMap<Integer, BigDecimal> onSaleOrderQtyMap = quoteManager.getInventoryItemOrders(itemIds);
            HashMap<Integer, BigDecimal> onPurchaseOrderMap = quoteManager.getOnPurchaseOrderCountByItem(itemIds);
            Map<Integer, BigDecimal> availableStocksAtWarehouse = null;
            Map<Integer, BigDecimal> onHand = new LinkedHashMap<>();
            if (filterParameter.getWarehouseID() != null) {
                availableStocksAtWarehouse = itemStockManager.getAvailableStockAtWarehouse(itemIds, filterParameter.getWarehouseID());
            } else {
                onHand = itemStockManager.getAvailableStockAtWarehouse(itemIds, null);
            }
            for (ProductsServicesSolrDoc doc : productsServicesSolrDocs) {

                if (doc != null) {
                    ProductItem productItem = getProductServicesRpc(doc);
                    if (availableStocksAtWarehouse != null) {
                        productItem.setItemsInWarehouse(availableStocksAtWarehouse.get(doc.getProductId()));
                    }

                    productItem.setOnSaleOrderQty(onSaleOrderQtyMap.get(doc.getProductId()));
                    productItem.setOnPurchaseOrder(onPurchaseOrderMap.get(doc.getProductId()));

                    if(onHand.get(doc.getProductId()) != null){
                        productItem.setItemsInStock(onHand.get(doc.getProductId()));
                    }

                    if (doc.getParentCategory() != null) {
                        productItem.setParentCategory(new SelectItem(doc.getParentCategoryId(), doc.getParentCategory()));
                    }
                    //Added for Javlon's Apteka
                    if (doc.getAccountId() != null) {
                        productItem.setAccountID(doc.getAccountId());
                    }

                    if (filterParameter.getWarehouseID() != null) {
                        productItem.setWarehouseId(filterParameter.getWarehouseID());
                    }
                    productItem.setInventoryTrackingEnabled(doc.getInventoryTrackingEnabled());
                    productItem.setTrackBatchesEnabled(doc.getTrackBatchesEnabled());
                    //For Javlons Apteka: include product batches
                    if (filterParameter.isShowProductBatches() && doc.getTrackBatchesEnabled()) {
                        productItem.setBatchItems(itemBatchManager.getBatchesOnHandByItemId(doc.getProductId()));
                    }

                    if (ASSEMBLY_ITEM.equals(doc.getProductTypeId())) {
                        if (isCustomSubItemsEnabled) {
                            productItem.setSubItemsData((ArrayList<NewProductCustomDescription>) mapToSubItemsData(doc));
                        }
                        productItem.setBuilt(assemblyHistoryManager.isAssemblyBuilded(doc.getProductId()));
                    }
                    if (defaultItemPicturesMap != null && defaultItemPicturesMap.get(productItem.getObjectId()) != null) {
                        productItem.setDefaultPictureUrl(getDefaultProductPictureUrlByID(defaultItemPicturesMap.get(productItem.getObjectId())));
                    }
                    if (defaultItemMiniPicturesMap != null && defaultItemMiniPicturesMap.get(productItem.getObjectId()) != null) {
                        productItem.setDefaultPictureMiniUrl(getDefaultProductPictureUrlByID(defaultItemMiniPicturesMap.get(productItem.getObjectId())));
                    }
                    if (panelSettings != null) {
                        productItem.setCustomFieldMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(doc, panelSettings.getColumnCodeName()));
                    }
                    productItemList.add(productItem);
                }
            }
        }
        return new ListResult<>(productItemList, totalNumber);
    }

    private List<NewProductCustomDescription> mapToSubItemsData(ProductsServicesSolrDoc doc) {
        return doc.getNewProductCustomDescriptions().stream()
                .map(i -> {
                    NewProductCustomDescription description = new NewProductCustomDescription();
                    description.setId(i.getId());
                    description.setName(i.getProductName());
                    description.setPrice(BigDecimal.valueOf(i.getPrice()));
                    description.setQty(BigDecimal.valueOf(i.getQuantity()));
                    return description;
                })
                .toList();
    }

    private ProductItem getProductServicesRpc(ProductsServicesSolrDoc doc) {
        ProductItem item = new ProductItem();
        item.setObjectId(doc.getProductId());
        item.setParentId(doc.getProductParentId());
        item.setProductNumber(doc.getProductNumber());
        item.setName(doc.getProductName());
        item.setType(doc.getProductTypeId());
        item.setProductRentalItemId(doc.getProductRentalItemId());
        item.setTypeName(doc.getProductTypeName());
        item.setDiscountType(doc.getProductDiscountTypeId());
        item.setDiscountTypeName(doc.getProductDiscountTypeName());
        item.setAccount(doc.getAccountName());
        item.setCogsAccount(doc.getCogsAccountName());
        item.setAssetAccount(doc.getAssetAccountName());
        item.setDescription(doc.getDescription());
        item.setUnitpPrice(BigDecimal.valueOf(doc.getUnitprice()));
        item.setCostPrice(BigDecimal.valueOf(doc.getCostprice() != null ? doc.getCostprice() : 0));
        item.setTaxAmountId(doc.getTaxrateId());
        item.setTaxRate(doc.getTaxrate());
        item.setTaxAmount(doc.getTaxEffectiveRate() != null ? BigDecimal.valueOf(doc.getTaxEffectiveRate()) : null);
        item.setActive(doc.getProductActive());
        item.setStorefrontEnable(doc.getProductStorefrontEnable());
        item.setSuppliers(ServerUtils.asListToSelectItem(doc.getMultiSupplierId(), doc.getMultiSupplierName(), doc.getMultiSupplierNumber()));
        item.setVendor(ServerUtils.asListToString(doc.getMultiSupplierName()));
        item.setCategory(doc.getCategory());
        item.setCategoryId(doc.getCategoryId());
        item.setPartNumber(doc.getPartNumber());
        item.setBarCodeString(doc.getBarcode());
        item.setManufacturer(doc.getManufacturer());
        item.setSkuNumber(doc.getSkuNumber());
        item.setUpcNumber(doc.getUpsNumber());
        item.setSubsidiaryProductUniqNum(doc.getSubsidiaryProductUniqNum());
        item.setUnitMeasurementName(doc.getUnitMeasureMentName());
        item.setUnitMeasurementId(doc.getUnitMeasureMentId());
        item.setBrand(doc.getBrandName());
        String averageCost = doc.getAverageCost();
        item.setAverageCost(averageCost != null ? new BigDecimal(averageCost) : null);
        item.setItemsInStock(BigDecimal.valueOf(doc.getQuantityOnHand()));
        item.setCreatedDate(doc.getCreatedDate());
        item.setUpdatedDate(doc.getUpdatedDate());

        String rentStatusCode = doc.getRentStatusCode();
        if (!ServerUtils.isNullOrEmpty(rentStatusCode)) {
            item.setRentStatus(referenceWfmMessageSource.localize(rentStatusCode, doc.getRentStatus()));
        }

        Integer creatorID = doc.getCreatorId();
        if (creatorID != null) {
            item.setCreator(new SelectItem(creatorID, doc.getCreatorName()));
        }

        Integer updaterID = doc.getUpdaterId();
        if (updaterID != null) {
            item.setUpdater(new SelectItem(updaterID, doc.getUpdaterName()));
        }

        return item;
    }

    @Override
    public SolrQuery getProductsServicesSolrQuery(ListingFilterParameter filterParameter, String solrQuery) {
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(filterParameter.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(filterParameter.getLimit()));

        if (!filterParameter.isSearchButton()) {
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                boolean desc = !filterParameter.isAscending();
                if (ProductItem.NAME.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.SORTABLE_PRODUCT_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.DISCRIPTION.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.SORTABLE_DESCRIPTION, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.STATUS.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.SORTABLE_STATUS, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.SELING_PRICE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.FIELD_UNITPRICE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.COST_PRICE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.FIELD_COSTPRICE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.TYPE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.SORTABLE_PRODUCT_TYPE_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.DISCOUNT_TYPE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.SORTABLE_PRODUCT_DISCOUNT_TYPE_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.ACCOUND.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.SORTABLE_ACCOUNT_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.TAX_RATE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.SORTABLE_TAXRATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.Vendor.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.SORTABLE_VENDOR, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.Category.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.SORTABLE_CATEGORY, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.PRODUCT_NUMBER.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.SORTABLE_PRODUCT_NUMBER, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.CREATOR.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.SORTABLE_CREATOR_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.UPDATED_DATE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.FIELD_UPDATED_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.ITEMS_IN_STOCK.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.FIELD_QUANTITY_ON_HAND, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.AVERAGE_COST.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.FIELD_AVERAGE_COST, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.UNIT_MEASUREMENT_NAME.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.FIELD_UNIT_MEASUREMENT_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ProductItem.CREATED_DATE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrProductServiceRepresenter.FIELD_CREATED_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else {
                    CustomFieldsUtils.setCustomFieldsSortableNameToSolr(filterParameter.getSortField(), desc, query, true);
                }
            } else {
                query.setSort(SolrProductServiceRepresenter.FIELD_PRODUCT_ID, SolrQuery.ORDER.desc);
            }
        }
        return query;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ProductItem> getStockProductsList(ListingFilterParameter filterParametrs) {
        filterParametrs.setType(INVENTORY_ITEM);
        List<EdsProductWarehouseLocation> productLocationItems = itemManager.getItemsFromStock(filterParametrs);
        int totalCount = productLocationItems.size();
        productLocationItems = ListUtils.getSublist(productLocationItems, filterParametrs.getStart(), filterParametrs.getLimit());
        ArrayList<ProductItem> items = new ArrayList<>();
        for (EdsProductWarehouseLocation pl : productLocationItems) {
            items.add(pl.getRPC());
        }
        return new ListResult<>(items, totalCount);
    }

    public ArrayList<Integer> deleteSelectedProductServices(ArrayList<Integer> ids) {
        ArrayList<Integer> result = new ArrayList<>();
        for (Integer objectID : ids) {
            boolean deleted = deleteProduct(objectID);
            if (deleted) {
            } else {
                result.add(objectID);
            }
        }

        return result;
    }

    public ProductItem getProductByID(Integer objectID) {
        EdsItem item = itemManager.get(objectID);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsItem.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(objectID);
        ServerUtils.kpiLog(log, kpiLog, "View product");

        ProductItem productItem = new ProductItem();
        productItem.setObjectId(objectID);
        productItem.setAccount(item.getAccount() != null ? item.getAccount().getName() : PA_NOT_AVAILABLE_STRING);
        productItem.setAccountID(item.getAccount() != null ? item.getAccount().getObjectID() : null);
        productItem.setCogsAccount(item.getCogsAccount() != null ? item.getCogsAccount().getName() : PA_NOT_AVAILABLE_STRING);
        productItem.setCogsAccountID(item.getCogsAccount() != null ? item.getCogsAccount().getObjectID() : null);
        productItem.setAssetAccount(item.getAssetAccount() != null ? item.getAssetAccount().getName() : PA_NOT_AVAILABLE_STRING);
        productItem.setAssetAccountID(item.getAssetAccount() != null ? item.getAssetAccount().getObjectID() : null);
        productItem.setDescription(item.getDescription());
        productItem.setName(item.getName());
        productItem.setCategory(item.getCategory() != null ? item.getCategory().getName() : PA_NOT_AVAILABLE_STRING);
        productItem.setCategoryId(item.getCategory() != null ? item.getCategory().getObjectID() : null);
        BigDecimal cost = getAverageCost(item);
        productItem.setUnitpPrice(cost != null ? cost : item.getUnitPrice());
        productItem.setType(item.getType());
        productItem.setProductRentalItemId(item.getRentItem() != null ? item.getRentItem().getObjectID() : null);
        productItem.setTypeName(item.getTypeName());
        productItem.setDiscountType(item.getDiscountType());
        productItem.setDiscountTypeName(item.getDiscountTypeName());
        productItem.setDiscountAmount(item.getDiscountAmount());
        productItem.setTaxRate(item.getVat() != null ? item.getVat().getTaxNameAndRateAsString() : PA_NOT_AVAILABLE_STRING);
        productItem.setTaxRateID(item.getVat() != null ? item.getVat().getObjectID() : null);
        productItem.setCostPrice(item.getSellingPrice());
        productItem.setProductNumber(item.getProductNumber());
        productItem.setItemsInStock(item.getQty());
        productItem.setDeleted(item.getDeleted());
        productItem.setDefaultPictureUrl(getDefaultProductPictureUrlByID(productPictureManager.getDefaultProductPictureByFileSizeType(objectID, 0)));
        productItem.setDefaultPictureMiniUrl(getDefaultProductPictureUrlByID(productPictureManager.getDefaultProductPictureByFileSizeType(objectID, 2)));
        productItem.setBarCodeString(item.getBarCode());
        productItem.setSkuNumber(item.getInternalSKUNumber());
        productItem.setUpcNumber(item.getUpcNumber());
        if (item.getUnitMeasurement() != null) {
            productItem.setUnitMeasurementName(item.getUnitMeasurement().getName());
            productItem.setUnitMeasurementId(item.getUnitMeasurement().getObjectID());
        }
        productItem.setVendor(item.getVendor() != null ? item.getVendor().getName() : "");
        productItem.setManufacturer(item.getManufacturer());
        productItem.setPartNumber(item.getPartNumber());
        productItem.setAsOf(item.getAsOf() != null ? new DateNonConvertable(item.getAsOf()) : null);
        productItem.setTotalValue(item.getTotalValue());
        /* set product category custom fields */
        if (item.getCategory() != null) {
            EdsItemCustomFields itemCustomFields = item.getItemCustomFields();
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonServiceLocal.getCompanyCustomFieldsByRelationship(ViewName.ProductCategory, item.getCategory().getObjectID(), null);
            productItem.setCategoryCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(itemCustomFields, customFieldsItems));
        }
        if (item.getMultiPrices() != null && !item.getMultiPrices().isEmpty()) {
            for (EdsItemMultiPrice itemMultiPrice : item.getMultiPrices()) {
                productItem.getMultiPrices().put(itemMultiPrice.getType() + itemMultiPrice.getCurrency().getName(), itemMultiPrice.getSellingPrice());
            }
        }
        /* set product custom fields */
        ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.ProductServiceView);
        productItem.setProductCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(item.getCustomFields(), customFieldItems));
        NewProduct product = new NewProduct();
        initProductWarehouseLocations(product, item, null);
        productItem.setProductLocations(product.getProductLocations());
        if (ASSEMBLY_ITEM.equals(item.getType())) {
            productItem.setAssemblyItems(wrapAssemblyItems(item.getAssemblyItems()));
        }
        productItem.setProductSerialsEnabled(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PRODUCT_SERIAL_ENABLED));
        if (productItem.isProductSerialsEnabled()) {
            productItem.setProductSerialItems(productSerialManager.getProductSerialsByItemID(objectID));
        }
        return productItem;
    }

    private String getDefaultProductPictureUrlByID(Integer productPictureId) {
        String url = null;
        if (productPictureId != null) {
            url = uploadManager.getFileURL(productPictureId);
        }
        return url;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InventoryStockData getStockValuations(ListingFilterParameter filterParametrs, DateNonConvertable fromDate, DateNonConvertable toDate) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        filterParametrs.setType(INVENTORY_ITEM);
        filterParametrs.setItemId(ASSEMBLY_ITEM);
        if (fromDate != null) {
            filterParametrs.setStartDate(fromDate.getNonConvertedDate());
        }
        if (toDate != null) {
            filterParametrs.setEndDate(toDate.getNonConvertedDate());
        }
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (!financialSettings.getEnableMultiWarehouse()) {
            filterParametrs.setWarehouseID(warehouseManager.getDefaultWarehouse().getObjectID());
        }
        List<Object[]> inventoryItems = itemManager.getStockValuation(filterParametrs);

        ArrayList<InventoryStockValuation> stockValuations = new ArrayList<>();

        InventoryStockData stockData = new InventoryStockData();
        if (!filterParametrs.isFromExcelPDF()) {
            stockData.setTotalCount(itemManager.getStockValuationCount(filterParametrs).intValue());
        }

        BigDecimal beginningBalance = itemManager.getStockValuationBalanceSum(filterParametrs, false);
        BigDecimal endingBalance = itemManager.getStockValuationBalanceSum(filterParametrs, true);
        stockData.setBeginningBalance(beginningBalance);
        stockData.setEndingBalance(endingBalance.add(beginningBalance));
        stockData.setCurrency(financialSettingsManager.getFinancialSettings().getCurrency().getAsSelectItem());

        LinkedHashMap<Integer, BigDecimal> itemsBeginningQty = itemManager.getStockValuationQTY(filterParametrs, false);
        LinkedHashMap<Integer, BigDecimal> itemsBeginningBalance = itemManager.getStockValuationBalance(filterParametrs, false);
        LinkedHashMap<Integer, List<StockItem>> stockItemListMap = itemStockManager.getInventoryTransaction(filterParametrs);
        LinkedHashMap<Integer, EdsTransaction> stockTransactionMap = itemStockManager.getInventoryTransactionMap(filterParametrs);

        if (inventoryItems != null && !inventoryItems.isEmpty()) {
            for (Object[] item : inventoryItems) {
                InventoryStockValuation stockValuation = new InventoryStockValuation();
                stockValuation.setName(item[2] == null ? "" : item[2].toString());
                stockValuation.setProductCode(item[1] == null ? "" : item[1].toString());
                filterParametrs.setCaseID((Integer) item[0]);
                stockValuation.setBeginningQty(itemsBeginningQty.get(filterParametrs.getCaseID()));
                stockValuation.setBeginningBalance(itemsBeginningBalance.get(filterParametrs.getCaseID()));
                stockValuation.setStockValuationItems(getInventoryItemTransactionValuation((Integer) item[0], filterParametrs, stockItemListMap, stockTransactionMap));
                stockValuations.add(stockValuation);
            }
        } else if (filterParametrs.getCaseID() != null) {
            EdsItem item = itemManager.get(filterParametrs.getCaseID());

            if (item != null) {
                InventoryStockValuation stockValuation = new InventoryStockValuation();
                stockValuation.setName(item.getName());
                stockValuation.setProductCode(item.getProductNumber());
                stockValuation.setBeginningQty(BigDecimal.ZERO);
                stockValuation.setBeginningBalance(BigDecimal.ZERO);
                stockValuation.setStockValuationItems(getInventoryItemTransactionValuation(item.getObjectID(), filterParametrs, stockItemListMap, stockTransactionMap));
                stockValuations.add(stockValuation);
            }
        }

        stockData.setStockValuations(stockValuations.toArray(new InventoryStockValuation[0]));

        return stockData;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewProduct[] getProductsForReport(ListingFilterParameter filterParametrs) {
        List<EdsItem> items = itemManager.getProductsForInventoryReport(filterParametrs);

        List<NewProduct> products = new ArrayList<>();

        if (items != null && !items.isEmpty()) {
            for (EdsItem item : items) {
                NewProduct product = new NewProduct();
                product.setObjectId(item.getObjectID());
                product.setType(item.getType());
                product.setNumberData(new NumberData(item.getProductNumber(), item.getIntNumber()));
                product.setItemName(item.getName());
                product.setUnitPrice(item.getUnitPrice());
                product.setSellingPrice(item.getSellingPrice());

                initProductWarehouseLocations(product, item, null);

                products.add(product);
            }
        }

        return products.toArray(new NewProduct[]{});
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProductItem getInventoryStock(ListingFilterParameter filterParametrs) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (!financialSettings.getEnableMultiWarehouse() || genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            filterParametrs.setWarehouseID(warehouseManager.getDefaultWarehouse().getObjectID());
        }

        ProductItem productItem = new ProductItem();
        if (filterParametrs.getObjectId() != null) {
            EdsItem edsItem = itemManager.get(filterParametrs.getObjectId());
            productItem.setObjectId(edsItem.getObjectID());
            productItem.setDescription(edsItem.getDescription());
            productItem.setInventoryTrackingEnabled(edsItem.getInventoryTrackingEnabled());
            productItem.setTrackBatchesEnabled(edsItem.getTrackBatchesEnabled());
            if (edsItem.getUnitMeasurement() != null && edsItem.getUnitMeasurement().getObjectID() != null) {
                productItem.setUnitMeasurementName(edsItem.getUnitMeasurement().getName());
                productItem.setUnitMeasurementId(edsItem.getUnitMeasurement().getObjectID());
            }
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
                productItem.setWarehouseId(warehouseManager.getDefaultWarehouse().getObjectID());
                productItem.setWarehouseName(warehouseManager.getDefaultWarehouse().getName());
            }
        }

        if (filterParametrs.getObjectId() != null && filterParametrs.getWarehouseID() != null) {
            List<EdsItemStock> itemStockList = itemStockManager.getItemStockListByProductAndWarehouse(filterParametrs.getObjectId(), filterParametrs.getWarehouseID());
            BigDecimal itemsInStock = BigDecimal.ZERO;
            for (EdsItemStock itemStock : itemStockList) {
                if (itemStock.getTransaction() != null && !itemStock.getTransaction().isDeleted()) {
                    if (itemStock.getTranCode().equals(TC_OUT)) {
                        itemsInStock = itemsInStock.subtract(itemStock.getQuantity());
                    } else {
                        itemsInStock = itemsInStock.add(itemStock.getQuantity());
                    }
                }
            }
            productItem.setItemsInStock(itemsInStock);
        }

        return productItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewProduct getProduct(Integer productId) {
        EdsItemCustomFields customFields;
        NewProduct product = new NewProduct();
        product.setNumberData(generateProductNumber());

        //Filling Data
        product.setUnitMeasurements(getUnitMeasurementsAsSelectItem());
        product.setBrands(accountingServiceLocal.getBrandsAsSelectItem());
        product.setTaxList(getCompanyTaxList(new ListingFilterParameter()));

        EdsAccount sales = accountingManager.getAccountTypeWithMinCode(EdsAccountType.SALES);
        EdsAccount costOfSales = accountingManager.getAccountTypeWithMinCode(EdsAccountType.COST_OF_SALES);

        List<EdsAccount> costOfSalesList = accountingManager.getAccountsByType(EdsAccountType.COST_OF_SALES);
        List<EdsAccount> edsAccountList = accountingManager.getAccountsByCategory(EdsAccountType.EXPENSES, EdsAccountType.FIXED_ASSET);
        if (sales != null && sales.getObjectID() != null) {
            product.setDefaultReceivableAccount(sales.createAccountItem());
        }
        if (costOfSales != null && costOfSales.getObjectID() != null) {
            product.setDefaultPayableAccount(costOfSales.createAccountItem());
        }
        ArrayList<SelectItem> accountItemList = new ArrayList<>();
        if (edsAccountList != null && !edsAccountList.isEmpty()) {
            for (EdsAccount edsAccount : edsAccountList) {
                accountItemList.add(edsAccount.createAccountItem());
            }
        }
        product.setAccountItemList(accountItemList);

        ArrayList<SelectItem> costOfSalesAccountItemList = new ArrayList<>();
        if (costOfSalesList != null && !costOfSalesList.isEmpty()) {
            for (EdsAccount edsAccount : costOfSalesList) {
                costOfSalesAccountItemList.add(edsAccount.createAccountItem());
            }
        }
        product.setCostOfSalesAccountItemList(costOfSalesAccountItemList);

        EdsFinancialSettings fsettings = financialSettingsManager.getFinancialSettings();
        if (fsettings != null) {
            product.setEnableCompanyIT(fsettings.enableIT());
            product.setEnableIT(fsettings.enableIT());
        }

        EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();
        if (numberingSettings != null) {
            product.setBarcodeNumberingEnabled(numberingSettings.getBarcodeNumbering());
        }

        if (productId != null) {
            EdsItem item = itemManager.getItem(productId);

            customFields = item.getCustomFields() != null ? item.getCustomFields() : null;
            product.setObjectId(productId);
            product.setObjectKey(item.getObjectKey());
            product.setType(item.getType());
            product.setTypeName(item.getTypeName());
            product.setDiscountType(item.getDiscountType());
            product.setDiscountTypeName(item.getTypeName());
            product.setDiscountAmount(item.getDiscountAmount());
            if (item.getRentStatus() != null) {
                product.setRentStatus(item.getRentStatus().getAsSelectItem());
            }
            if (item.getRentItem() != null) {
                product.setRentItem(item.getRentItem().getAsSelectItem());
            }
            String productNumber = item.getProductNumber();
            product.getNumberData().setNumberString(productNumber);
            if (product.isBarcodeNumberingEnabled()) {
                product.setBarcodeChecksum(item.getBarcodeChecksum() != null ? item.getBarcodeChecksum() : "");
                productNumber = productNumber.substring(0, productNumber.length() - product.getBarcodeChecksum().length());
            }
            product.getNumberData().setIntNumber(item.getIntNumber());
            if (item.getIntNumber() != null) {
                DecimalFormat numberFormatForParse = new DecimalFormat("0000");
                String fourDigitNumberAsString = numberFormatForParse.format(item.getIntNumber());
                String lastFourCharacter = "";
                if (productNumber.length() >= fourDigitNumberAsString.length()) {
                    lastFourCharacter = productNumber.substring(productNumber.length() - fourDigitNumberAsString.length());
                }
                if (productNumber != null && productNumber.length() > 4 && fourDigitNumberAsString.equals(lastFourCharacter)) {
                    product.getNumberData().setFirstNumberString(productNumber.substring(0, productNumber.length() - fourDigitNumberAsString.length()));
                } else {
                    product.getNumberData().setFirstNumberString(item.getProductNumber());
                }
            } else {
                product.getNumberData().setFirstNumberString(item.getProductNumber());
            }
            product.setIntNumber(item.getIntNumber());
            product.setItemName(item.getName());
            product.setDescription(item.getDescription());
            product.setUnitPrice(item.getUnitPrice());
            product.setSellingPrice(item.getSellingPrice());
            product.setComission(item.getComission());
            product.setInternalSKUNumber(item.getInternalSKUNumber());
            product.setManufacturer(item.getManufacturer());
            product.setPartNumber(item.getPartNumber());
            product.setBarCodeText(item.getBarCode());
            product.setSaasuGUID(item.getSaasuGUID());
            product.setSasuuLastUpdatedDate(item.getSasuuLastUpdatedDate());
            product.setHasUsed(itemStockManager.isUsedInTransactions(item.getObjectID()));
            product.setActive(item.isActive());
            product.setSentToTextileFinds(item.isSentToTextileFinds());
            product.setMagentoEntityID(item.getMagentoEntityID());
            product.setMagentoLastSyncDate(item.getMagentoSyncDate());
            product.setLastUpdateTime(item.getLastUpdateTime());
            product.setCreatedDate(item.getCreationTime());
            product.setCurrencyId(item.getCurrency() != null ? item.getCurrency().getObjectID() : null);
            product.setCustomer(item.getCustomer() != null ? item.getCustomer().getAsSelectItem() : null);
            BigDecimal averageCost = getAverageCost(item);
            product.setAverageCost(averageCost != null ? averageCost : item.getUnitPrice());

            if (item.getQRCodeSizeID() != null) {
                product.setQRCodeSizeID(item.getQRCodeSizeID());
            }
            if (item.getBarcodeFile() != null) {
                product.setBarcodeID(item.getBarcodeFile().getObjectID());
            }
            product.setUpcNumber(item.getUpcNumber());
//            product.setShowOnOpportunity(item.isShowOnOpportunity());
            if (item.getCategory() != null) {
                product.setCategoryID(item.getCategory().getObjectID());
                product.setCategoryName(item.getCategory().getName());
            }

            if (item.getParent() != null) {
                product.setParentId(item.getParent().getObjectID());
            }

            if (item.getUnitMeasurement() != null) {
                product.setUnitMeasurementID(item.getUnitMeasurement().getObjectID());
                product.setUnitMeasurement(new SelectItem(item.getUnitMeasurement().getObjectID(), item.getUnitMeasurement().getName()));
            }
            if (item.getVendor() != null) {
                product.setVendorItem(new SelectItem(item.getVendor().getObjectID(), item.getVendor().getName()));
                if (item.getVendor().getCurrency() != null) {
                    product.setVendorCurrencyID(item.getVendor().getCurrency().getObjectID());
                }
            }
            product.setPurchasedFromSupplier(item.isPurchasedFromSupplier());
            product.setSoldToCustomer(item.isSoldToCustomer());
            product.setWeightPerUnit(item.getWeightPerUnit());

            if (item.getAccount() != null) {
                EdsAccount account = item.getAccount();
                product.setAccountId(account.getObjectID());
                product.setAccountItem(account.createAccountItem());
            }

            if (item.getCogsAccount() != null) {
                EdsAccount account = item.getCogsAccount();
                product.setCogsAccountID(account.getObjectID());
                product.setCogsAccount(account.createAccountItem());
            }

            if (item.getAssetAccount() != null) {
                EdsAccount account = item.getAssetAccount();
                product.setAssetAccountID(account.getObjectID());
                product.setAssetAccount(account.createAccountItem());
            }

            if (item.getBrand() != null) {
                product.setBrandID(item.getBrand().getObjectID());
                product.setBrandName(item.getBrand().getName());
            }
            if (item.getDiscounts() != null) {
                List<DiscountItem> appliedClients = new ArrayList<>(item.getDiscounts().size());
                for (EdsDiscount discount : item.getDiscounts()) {
                    appliedClients.add(new DiscountItem(discount.getObjectID(), discount.getName()));
                }
                product.setDiscountItems(appliedClients.toArray(new DiscountItem[0]));
            }

            if (item.getVat() != null) {
                product.setTaxItem(item.getVat().createTaxItem());
                product.setTaxIDs(new Integer[]{item.getVat().getObjectID()});
                product.setVatId(item.getVat().getObjectID());
                product.setEffectiveTaxRate(item.getVat().getEffectiveTaxRate());
            } else {
                product.setEffectiveTaxRate(Double.valueOf("0"));
            }

            if (item.getDoubleVat() != null) {
                product.setDoubleTaxItem(item.getDoubleVat().createTaxItem());
                product.setDoubleVatId(item.getDoubleVat().getObjectID());
            }

            if (item.getVariationses() != null && !item.getVariationses().isEmpty()) {//if has product variations
                if (product.getVariationCombinate() != null) {
                    product.setVariationCombinate(new ArrayList<>());
                }

                for (EdsItemVariations variation : item.getVariationses()) {
                    product.getVariationCombinate().add(variation.getCombination());
                }
            }

            if (item.getMultiPrices() != null && !item.getMultiPrices().isEmpty()) {
                for (EdsItemMultiPrice itemMultiPrice : item.getMultiPrices()) {
                    product.getMultiPrices().add(new MultiPriceItem(new SelectItem(itemMultiPrice.getCurrency().getObjectID(), itemMultiPrice.getCurrency().getName()), itemMultiPrice.getSellingPrice(), itemMultiPrice.getType()));
                }
            }

            if (item.getSuppliers() != null && !item.getSuppliers().isEmpty()) {
                ArrayList<SelectItem> suppliers = new ArrayList<>();
                for (EdsCrmAccount supplier : item.getSuppliers()) {
                    suppliers.add(supplier.getAsSelectItem());
                }
                product.setSuppliers(suppliers.toArray(new SelectItem[]{}));
            }
            if (item.getCostAllocationType() != null) {
                product.setCostAllocationType(item.getCostAllocationType().getAsSelectItem());
            }
            product.setGlobalReorderPoint(item.getGlobalReorderPoint());
            product.setTotalValue(item.getTotalValue());
            product.setAsOf(item.getAsOf() != null ? new DateNonConvertable(item.getAsOf()) : null);

            product.setOrder(item.getOrder());
            product.setStorefrontEnable(item.isStorefrontEnable());
            if (item.isStorefrontEnable() != null && item.isStorefrontEnable()) {
                initStorefrontOptions(product, item);
            }

            if (PRODUCT_KIT.equals(product.getType())) {
                product.setProductKitItems(wrapProductKitItems(item.getProductKitItems()));
            } else if (RENTAL_ITEM.equals(product.getType())) {
                initRentalItemOptions(product, item);
            } else if (ASSEMBLY_ITEM.equals(product.getType())) {
                product.setAssemblyItems(wrapAssemblyItems(item.getAssemblyItems()));
                product.setQuantity(item.getQty());
            }

            if (item.getCategory() != null) {
                EdsItemCustomFields itemCustomFields = item.getItemCustomFields();
                ArrayList<CompanyCustomFieldItem> customFieldsItems = commonServiceLocal.getCompanyCustomFieldsByRelationship(ViewName.ProductCategory, item.getCategory().getObjectID(), null);
                product.setCategoryCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(itemCustomFields, customFieldsItems));
            }

            initProductWarehouseLocations(product, item, null);
            ArrayList<ProductSerialItem> productSerialItems = new ArrayList<>();
            boolean isAlmadarCompany = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ALMADAR_PRODUCT_SERIAL_ENABLED);
            if (isAlmadarCompany) {
                productSerialItems = productSerialManager.getProductSerialsWithQtyByItemID(item.getObjectID());
            } else {
                productSerialItems = productSerialManager.getProductSerialsByItemID(item.getObjectID());
            }
            product.setProductSerialItems(productSerialItems);
            product.setTrackBatchesEnabled(item.getTrackBatchesEnabled());
            if (item.getTrackBatchesEnabled()) {
                ArrayList<ProductTrackBatchItem> trackBatchItems = itemBatchManager.getBatchesOnHandByItemId(item.getObjectID());
                for (ProductTrackBatchItem trackBatchItem : trackBatchItems) {
                    if (trackBatchItem.getWarehouseId() != null) {
                        EdsWarehouse edsWarehouse = warehouseManager.get(trackBatchItem.getWarehouseId());
                        trackBatchItem.setWarehouseName(edsWarehouse.getName());
                    }
                }
                product.setTrackBatchItems(trackBatchItems);
            }
            product.setDefaultItemWarehouse(item.getDefaultWarehouse() != null ? item.getDefaultWarehouse().getAsSelectItem() : null);
            product.setInventoryTrackingEnabled(item.getInventoryTrackingEnabled());
            product.setBatchTrackingEnabled(item.getBatchTrackingEnabled());
            if (item.getLocations() != null) {
                product.setLocations(new ArrayList<>(item.getLocations().stream().map(l -> new SelectItem(l.getObjectID(), l.getName())).collect(Collectors.toList())));
            }
        } else {
            customFields = new EdsItemCustomFields();
            EdsAccount defaultAssetAccount = accountingManager.getAccountByCode("10");
            product.setDefaultAssetAccount(defaultAssetAccount != null ? defaultAssetAccount.createAccountItem() : null);
            product.setSoldToCustomer(true);
        }
        product.setCurrencies(currencyService.getCurrencies(true, false));
        product.setProductCategories(accountingService.getCategoriesAsSelectItem());

        //init product custom fields
        if (customFields != null) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.ProductServiceView);
            product.setProductCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(customFields, customFieldItems));
        }

        return product;
    }

    @Override
    public NewProduct getProductEditData(Integer productId, Boolean isFromExisting) {
        NewProduct product = getProduct(productId);
//        product.setLayoutHTML(layoutManager.getLayoutHTML(LayoutRPC.PRODUCT));
        if (productId == null) {
            product.setPurchasedFromSupplier(null);
            product.setLocations(new ArrayList<>());
        }
        product.setDoubleTaxEnabled(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DOUBLE_TAX_ENABLED));
        product.setProductSerialsEnabled(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PRODUCT_SERIAL_ENABLED));
        EdsWarehouse edsDefaultWarehouse = warehouseManager.getDefaultWarehouse();
        if (edsDefaultWarehouse != null) {
            product.setWarehouse(edsDefaultWarehouse.getAsSelectItem());
        } else {
            List<EdsWarehouse> warehouseList = warehouseManager.getWarehouseList(null);
            if (warehouseList != null && !warehouseList.isEmpty()) {
                product.setWarehouse(warehouseList.get(0).getAsSelectItem());
            }
        }
        if (isFromExisting) {
            product.setObjectId(null);
            product.setNumberData(generateProductNumber());
            product.setProductLocations(null);
            product.setTotalValue(null);

            if (ASSEMBLY_ITEM.equals(product.getType())) {
                for (AssemblyItem item : product.getAssemblyItems()) {
                    item.setAssemblyItemId(null);
                }
            }
            if (product.getProductCustomFieldItems() != null && !product.getProductCustomFieldItems().isEmpty()) {
                for (CompanyCustomFieldItem customFieldItem : product.getProductCustomFieldItems()) {
                    customFieldItem.setObjectId(null);
                }
            }
        }
        return product;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<LogHistoryItem> getProductLogHistoryList(ListingFilterParameter listingFilterParameter) {
        List<LogHistoryItem> items = new ArrayList<>();
        ArrayList<LogHistoryItem> subItems = new ArrayList<>();
        EdsItem item = itemManager.get(listingFilterParameter.getEntityID());
        if (item != null && item.getLogHistories() != null) {
            if (listingFilterParameter.getSearchKey() != null) {
                items = item.getLogHistories().stream().map(EdsHistoryLog::toRpc).filter(historyItem -> historyItem.getField().toLowerCase().contains(listingFilterParameter.getSearchKey().toLowerCase()) || historyItem.getUserName().toLowerCase().contains(listingFilterParameter.getSearchKey().toLowerCase()))
                        .collect(Collectors.toList());
            } else {
                items = item.getLogHistories().stream().map(EdsHistoryLog::toRpc).collect(Collectors.toList());
            }

            if (items != null && !items.isEmpty()) {
                if ("MODIFIED_DATE".equals(listingFilterParameter.getSortField()) && listingFilterParameter.isAscending()) {
                    items.sort(Comparator.comparing(LogHistoryItem::getUpdatedDate));
                } else {
                    items.sort((o1, o2) -> o2.getUpdatedDate().compareTo(o1.getUpdatedDate()));
                }
            }
        }
        if (items != null && !items.isEmpty()) {
            if (items.size() - (listingFilterParameter.getStart() + listingFilterParameter.getLimit()) >= 0) {
                subItems.addAll(items.subList(listingFilterParameter.getStart(), listingFilterParameter.getStart() + listingFilterParameter.getLimit()));
            } else {
                subItems.addAll(items.subList(listingFilterParameter.getStart(), items.size()));
            }
        }

        return new ListResult<>(subItems, items != null && !items.isEmpty() ? items.size() : 0);
    }

    @Override
    public NumberData regenerateProductNumber(SelectItem[] supplierIds, Integer categoryId, NumberData productCurrentNumber) {
        boolean customProductNumber = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PRODUCT_NUMBER_GENERATE_WITH_SUPPLIER_AND_CATEGORY);
        if (!customProductNumber) {
            return productCurrentNumber;
        } else {
            Integer intNumber = accountingManager.getProductLastIntNumber();
            StringBuilder prefix = new StringBuilder();
            String delimeter = "";
            for (SelectItem supplier : supplierIds) {
                EdsCrmAccount supp = crmAccountManager.get(supplier.getId());
                prefix.append(delimeter);
                prefix.append(supp.getNumber().replace(EdsNumberingSettings.decimalFormat.format(supp.getNumberInteger()), ""));
                delimeter = "_";
            }
            EdsProductCategory ct = productCategoryManager.get(categoryId);
            if (ct != null && ct.getCode() != null && !ct.getCode().isEmpty()) {
                prefix.append(delimeter);
                if (ct.getPrefix() != null) {
                    prefix.append(ct.getPrefix());
                } else {
                    prefix.append(ct.getCode().replace(EdsNumberingSettings.decimalFormat.format(ct.getIntNumber()), ""));
                }
            }
            return EdsNumberingSettings.getDefaultData(intNumber, prefix.toString());

        }
    }

    private NewProduct getProductBaseData(EdsProductKitItems pKitItem) {
        NewProduct result = getProductBaseData(pKitItem.getItem().getObjectID());
        result.setPkItemQty(pKitItem.getQuantity());
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewProduct getProductBaseData(Integer productId) {
        return getProductBaseData(productId, false);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewProduct getProductBaseData(Integer productId, boolean fromOpporunity) {
        EdsItem item = itemManager.get(productId);
        NewProduct product = new NewProduct();
        if (item != null) {
            product.setNumberData(generateProductNumber());
            product.getNumberData().setNumberString(item.getProductNumber());
            product.getNumberData().setIntNumber(item.getIntNumber());
            product.setObjectId(item.getObjectID());
            product.setItemName(item.getName());
            product.setDescription(item.getDescription());
            product.setHasInventoryInProductKit(false);
            if (ASSEMBLY_ITEM.equals(item.getType()) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CUSTOM_INVOICE_ITEM_PRODUCT_DESCRIPTION_ENABLED)) {
                product.setCustomDescription(item.getCustomDescriptionData());
            }
            if (PRODUCT_KIT.equals(item.getType())) {
                product.setHasInventoryInProductKit(item.isHasInventoryInProductKit());
            }
            product.setType(item.getType());
            product.setTypeName(item.getTypeName());
            product.setDiscountType(item.getDiscountType());
            product.setDiscountTypeName(item.getDiscountTypeName());
            product.setDiscountAmount(item.getDiscountAmount());
            product.setInternalSKUNumber(item.getInternalSKUNumber());
            product.setUpcNumber(item.getUpcNumber());
            product.setPartNumber(item.getPartNumber());
            Set<EdsLocation> locations = item.getLocations();
            ArrayList<Integer> locationIds = new ArrayList<>();
            if (!locations.isEmpty()) {
                locationIds = locations.stream()
                        .map(EdsLocation::getObjectID)
                        .collect(Collectors.toCollection(ArrayList::new));
                product.setLocationIds(locationIds);
            }
            if (item.getAsOf() != null) {
                product.setAsOf(new DateNonConvertable(item.getAsOf()));
            }

            product.setPurchasedFromSupplier(item.isPurchasedFromSupplier() != null ? item.isPurchasedFromSupplier() : false);
            product.setSoldToCustomer(item.isSoldToCustomer());
            product.setUnitPrice(item.getUnitPrice());
            product.setUnitCost(item.getUnitPrice());
            product.setSellingPrice(item.getSellingPrice());
            product.setQuantity(item.getQty());
            product.setInventoryTrackingEnabled(item.getInventoryTrackingEnabled());
            product.setBatchTrackingEnabled(item.getBatchTrackingEnabled());
            product.setTrackBatchesEnabled(item.getTrackBatchesEnabled());
            BigDecimal averageCost = getAverageCost(item);
            product.setAverageCost(averageCost != null ? averageCost : BigDecimal.ZERO);

            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CONSIGNMENT_FUNCTION_ENABLE)) {
                product.setConsignedQty(consignmentManager.getConsignmentQtyToPurchase(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()), productId, null));
                product.setConsignedQtyToSell(consignmentManager.getConsignmentQtyToSell(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()), productId, null));
            }
            product.setComission(item.getComission());
            product.setTotalValue(item.getTotalValue());
            if (item.getSubsidiaryProductUniqNum() != null && !item.getSubsidiaryProductUniqNum().trim().isEmpty()) {
                product.setSubsidiaryProductUniqueID(item.getSubsidiaryProductUniqNum());
                EdsSubsidiaryProduct subsidiaryProduct = subsidiaryProductManager.getSubsidiaryByUniqueID(item.getSubsidiaryProductUniqNum());
                product.setItemNameID(subsidiaryProduct != null ? subsidiaryProduct.getObjectID() : null);
            }
            if (item.getUnitMeasurement() != null) {
                product.setUnitMeasurement(new SelectItem(item.getUnitMeasurement().getObjectID(), item.getUnitMeasurement().getName(), item.getUnitMeasurement().getDescription()));
            }
            if (item.getAccount() != null) {
                EdsAccount account = item.getAccount();
                product.setAccountId(account.getObjectID());
                product.setAccountItem(account.createAccountItem());
            }

            if (item.getCogsAccount() != null) {
                EdsAccount account = item.getCogsAccount();
                product.setCogsAccountID(account.getObjectID());
                product.setCogsAccount(account.createAccountItem());
            }

            if (item.getAssetAccount() != null) {
                EdsAccount account = item.getAssetAccount();
                product.setAssetAccountID(account.getObjectID());
                product.setAssetAccount(account.createAccountItem());
            }

            if (item.getName() != null) {
                product.setItemName(item.getName());
            }
            if (item.getDefaultWarehouse() != null) {
                product.setDefaultItemWarehouse(item.getDefaultWarehouse().getAsSelectItem());
            } else if (item.getProductWarehouseLocations() != null && item.getProductWarehouseLocations().size() == 1) {
                // if product has only one warehouse, set it as default
                EdsWarehouse warehouse = item.getProductWarehouseLocations().get(0).getWarehouse();
                if (warehouse != null) {
                    product.setDefaultItemWarehouse(warehouse.getAsSelectItem());
                }
            }

            // check default warehouse owner
            EdsUser user = userManager.getUser();
            if (!fromOpporunity
                    && user != null
                    && product.getDefaultItemWarehouse() != null
                    && !warehouseManager.hasAccessToWarehouse(
                    user.getObjectID(),
                    product.getDefaultItemWarehouse().getId())
            ) {
                product.setDefaultItemWarehouse(null);
            }


            EdsVat defaultVat = vatManager.getDefaultVat();

            if (defaultVat != null && item.getVat() == null) {
                product.setTaxItem(defaultVat.createTaxItem());
                product.setTaxIDs(new Integer[]{defaultVat.getObjectID()});
                product.setVatId(defaultVat.getObjectID());
                product.setEffectiveTaxRate(defaultVat.getEffectiveTaxRate());
            } else if (item.getVat() != null) {
                product.setTaxItem(item.getVat().createTaxItem());
                product.setTaxIDs(new Integer[]{item.getVat().getObjectID()});
                product.setVatId(item.getVat().getObjectID());
                product.setEffectiveTaxRate(item.getVat().getEffectiveTaxRate());
            } else {
                product.setEffectiveTaxRate(Double.valueOf("0"));
            }

            if (item.getDoubleVat() != null) {
                product.setDoubleTaxItem(item.getDoubleVat().createTaxItem());
                product.setDoubleVatId(item.getDoubleVat().getObjectID());
            }

            if (item.getBrand() != null) {
                product.setBrandID(item.getBrand().getObjectID());
                product.setBrandName(item.getBrand().getName());
            }

            if (item.getCategory() != null) {
                product.setCategoryID(item.getCategory().getObjectID());
                product.setCategoryName(item.getCategory().getName());
            }

            if (item.getMultiPrices() != null && !item.getMultiPrices().isEmpty()) {
                for (EdsItemMultiPrice itemMultiPrice : item.getMultiPrices()) {
                    product.getMultiPricesMap().put(itemMultiPrice.getType() + itemMultiPrice.getCurrency().getObjectID(), itemMultiPrice.getSellingPrice());
                }
                /* selling/cost price in base currency */
                product.getMultiPricesMap().put(RECEIVABLE + "-1", item.getSellingPrice());
                product.getMultiPricesMap().put(PAYABLE + "-1", item.getUnitPrice());
                product.getMultiPricesMap().put(ProductItem.AVERAGE_COST, averageCost);
            }

            if (item.getPictures() != null && !item.getPictures().isEmpty()) {
                product.setImageGallery(getProductPictures(item));
            }
            product.setManufacturer(item.getManufacturer());
            if (item.getSuppliers() != null && !item.getSuppliers().isEmpty()) {
                ArrayList<SelectItem> suppliers = new ArrayList<>();
                for (EdsCrmAccount supplier : item.getSuppliers()) {
                    suppliers.add(supplier.getAsSelectItem());
                }
                product.setSuppliers(suppliers.toArray(new SelectItem[]{}));
            }

            initProductWarehouseLocations(product, item, null);
            initProductDiscounts(product, item);

            product.setHasVariations(item.getHasVariations());

            List<EdsItem> childProducts = itemManager.getChildProducts(productId);
            if (childProducts != null) {
                List<NewProduct> childItems = new ArrayList<>();
                for (EdsItem childProduct : childProducts) {
                    childItems.add(getProductBaseData(childProduct.getObjectID()));
                }
                product.setChildProducts(childItems.toArray(new NewProduct[]{}));
            } else {
                product.setHasVariations(false);
            }

            //init item category custom fields
            if (item.getCategory() != null && item.getItemCustomFields() != null) {
                EdsItemCustomFields itemCustomFields = item.getItemCustomFields();
                ArrayList<CompanyCustomFieldItem> customFieldsItems = commonServiceLocal.getCompanyCustomFieldsByRelationship(ViewName.ProductCategory, item.getCategory().getObjectID(), null);
                product.setCategoryCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(itemCustomFields, customFieldsItems));
            }

            //init item custom fields
            EdsItemCustomFields customFields = item.getCustomFields();
            ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.ProductServiceView);
            ArrayList<CompanyCustomFieldItem> itemCFs = CustomFieldsUtils.setRPCCustomFieldItems(customFields, customFieldItems);

            if (fromOpporunity && item.getCategory() != null) {
                ArrayList<CompanyCustomFieldItem> categoryFormCF = null;
                EdsProductCategory productCategory = productCategoryManager.get(item.getCategory().getObjectID());
                if (productCategory != null && productCategory.getCustomFields() != null) {
                    ArrayList<CompanyCustomFieldItem> categoryCustomFieldItems = commonService.getCompanyCustomFields(ViewName.ProductCategoryStoreFront);
                    categoryFormCF = CustomFieldsUtils.setRPCCustomFieldItems(productCategory.getCustomFields(), categoryCustomFieldItems);
                }

                if (categoryFormCF != null && !categoryFormCF.isEmpty()) {
                    if (itemCFs != null && !itemCFs.isEmpty()) {
                        for (CompanyCustomFieldItem categoryCF : categoryFormCF) {
                            boolean isHave = false;
                            for (CompanyCustomFieldItem productCF : itemCFs) {
                                if (categoryCF.getDataType().equals(productCF.getDataType()) &&
                                        categoryCF.getUiType().equals(productCF.getUiType()) &&
                                        categoryCF.getAliasName().equals(productCF.getAliasName())) {
                                    if (UI_TYPE_LOOKUP.equals(categoryCF.getUiType()) && categoryCF.getLookUpTypeEnum().equals(productCF.getLookUpTypeEnum())) {
                                        isHave = true;
                                        break;
                                    } else {
                                        isHave = true;
                                        break;
                                    }
                                }
                            }
                            if (!isHave) {
                                itemCFs.add(categoryCF);
                            }
                        }
                    } else {
                        itemCFs = categoryFormCF;
                    }
                }


            }

            product.setProductCustomFieldItems(itemCFs);

            SelectItem departmentForInvoice = allInOneService.getDepartmentForInvoice();
            if (departmentForInvoice != null) {
                product.setDefaultDepartment(departmentForInvoice);
            }
            SelectItem selectedWarehouseForTransactions = accountingService.getSelectedWarehouseForTransactions();
            if (selectedWarehouseForTransactions != null) {
                product.setWarehouseByOwner(selectedWarehouseForTransactions);
            }
        }
        if (product.getTaxItem() != null && product.getTaxItem().getId() != null) {
            EdsVat edsVat = vatManager.get(product.getTaxItem().getId());
            if (edsVat != null && edsVat.getFaiCategorieIds() != null) {
                List<SelectItem> categories = new LinkedList<>();
                for (Integer categoryId : edsVat.getFaiCategorieIds()) {
                    EdsReference cat = referenceManager.get(categoryId);
                    if (cat == null) {
                        continue;
                    }
                    categories.add(new SelectItem(cat.getObjectID(), cat.getName()));
                }
                List<SelectItem> purchaseCategories = new LinkedList<>();
                for (Integer categoryId : edsVat.getFaiPurchaseCategoryIds()) {
                    EdsReference cat = referenceManager.get(categoryId);
                    if (cat == null) {
                        continue;
                    }
                    purchaseCategories.add(new SelectItem(cat.getObjectID(), cat.getName()));
                }
                TaxItem taxItem = product.getTaxItem();
                taxItem.setFaiCategories(categories.toArray(SelectItem[]::new));
                taxItem.setFaiPurchaseCategories(purchaseCategories.toArray(SelectItem[]::new));
                product.setTaxItem(taxItem);
            }
        }

        return product;
    }

    public ArrayList<CompanyCustomFieldItem> getProductCategoryCF(Integer productCategoryId) {
        EdsProductCategory productCategory = productCategoryManager.get(productCategoryId);
        if (productCategory != null) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.ProductCategoryStoreFront);
            return CustomFieldsUtils.setRPCCustomFieldItems(productCategory.getCustomFields(), customFieldItems);
        }
        return null;
    }

    public Integer getSerialsQty(Integer productId, String serialNumber, Date expirationDate) {
        return productSerialManager.getProductSerialsQty(productId, serialNumber, expirationDate);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewProduct[] getProductKitProducts(Integer productId, boolean checkForPurchasedFromSupplier) {
        ArrayList<NewProduct> kitItems = new ArrayList<>();
        EdsItem prodG = itemManager.get(productId);
        for (EdsProductKitItems kitItem : prodG.getProductKitItems()) {
            NewProduct product = getProductBaseData(kitItem);
            if (!checkForPurchasedFromSupplier || product.isPurchasedFromSupplier() || INVENTORY_ITEM.equals(product.getType()))
                kitItems.add(product);
        }
        return kitItems.toArray(new NewProduct[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getVendorsAsSelectItem() {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setAccountType(EdsCrmAccount.SUPPLIER);
        List<EdsCrmAccount> suppliers = crmAccountManager.list(filterParametrs);
        if (suppliers != null && !suppliers.isEmpty()) {
            List<SelectItem> items = new ArrayList<>();
            for (EdsCrmAccount s : suppliers) {
                items.add(s.getAsSelectItem());
            }
            return items.toArray(new SelectItem[]{});
        }

        return new SelectItem[0];
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getUnitMeasurementsAsSelectItem() {
        List<EdsUnitMeasurement> measurements = unitMeasurementManager.getUnitMeasurements(null, accountingManager.getUser().getCompany().getObjectID());
        if (measurements == null || measurements.isEmpty()) {
            return new SelectItem[0];
        } else {
            SelectItem[] result = new SelectItem[measurements.size()];
            int i = 0;
            for (EdsUnitMeasurement m : measurements) {
                result[i] = new SelectItem(m.getObjectID(), m.getName());
                i++;
            }
            return result;
        }
    }

    public Boolean updateProductCategory(Integer productID, Integer categoryID) {
        if (productID != null && categoryID != null) {
            try {
                EdsItem item = itemManager.get(productID);
                if (item != null) {
                    item.setCategory(productCategoryManager.get(categoryID));
                    itemManager.update(item);
                }
                productsServicesSolrComponent.index(item);

                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, item, itemManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_PRODUCT);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    public Boolean updateProductMeasurement(Integer productID, Integer unitMeasurementId) {
        if (productID != null && unitMeasurementId != null) {
            try {
                EdsItem item = itemManager.get(productID);
                if (item != null) {
                    item.setUnitMeasurement(unitMeasurementManager.get(unitMeasurementId));
                    itemManager.update(item);
                }
                productsServicesSolrComponent.index(item);

                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, item, itemManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_PRODUCT);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    @Override
    public ProductSelectItem saveProduct(NewProduct product) {
        return saveProduct(product, true);
    }

    @Override
    public ProductSelectItem saveProduct(NewProduct product, boolean runWebhook) {
        ProductSelectItem result;
        Integer transactionID = null;
        if (product != null) {
            boolean newCreated = false;
            NumberData numberData = product.getNumberData() != null ? product.getNumberData() : generateProductNumber();

            boolean isCustomFormat = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION);
            if (isCustomFormat) {
                Integer intNumber = numberData != null ? numberData.getIntNumber() : 0;
                numberData = buildCustomNumberData(intNumber, product.getCategoryID());
            }

            Integer numberResult = itemManager.numberAlreadyExist(numberData, product.getObjectId());
            if (numberResult == -1) {
                return new ProductSelectItem(numberResult, numberData.getNumberString());
            }

            EdsItem item = new EdsItem();
            EdsCompany company = itemManager.getUser().getCompany();

            if (product.getObjectId() != null) {
                item = itemManager.get(product.getObjectId());
                List<EdsAssemblyItem> assItems = assemblyItemManager.getItemsByProduct(product.getObjectId());
                if (assItems != null && ASSEMBLY_ITEM.equals(product.getType()) && product.getUnitPrice() != null) {
                    for (EdsAssemblyItem assItem : assItems) {
                        EdsItem productItem = assItem.getItem();
                        BigDecimal exchangeRate = productItem.getExchangeRate() != null ? productItem.getExchangeRate() : BigDecimal.ONE;
                        assItem.setCostPrice(exchangeRate.multiply(product.getUnitPrice()));
                        assemblyItemManager.createOrUpdate(assItem);
                    }
                } else {
                    if (product.getUnitPrice() != null) {
                        assemblyItemManager.updateCostPriceOfAssemblyItem(product.getObjectId(), product.getUnitPrice());
                    }
                }
            }
            item.setObjectKey(product.getObjectKey());
            item.setType(product.getType());
            if (product.getRentStatus() != null) {
                item.setRentStatus(referenceManager.get(product.getRentStatus().getId()));
            } else {
                item.setRentStatus(null);
            }

            if (product.getRentItem() != null) {
                item.setRentItem(itemManager.getItem(product.getRentItem().getId()));
            } else  {
                item.setRentItem(null);
            }
            product.setTypeName(item.getTypeName());

            item.setDiscountAmount(product.getDiscountAmount());
            item.setDiscountType(product.getDiscountType());
            product.setDiscountTypeName(item.getDiscountTypeName());

            if (numberData.getIntNumber() != null) {
                if (!accountingManager.isProductNumberExists(numberData.getNumberString(), product.getObjectId())) {
                    item.setProductNumber(numberData.getNumberString());
                    item.setIntNumber(numberData.getIntNumber());
                } else {
                    if (isCustomFormat) {
                        Integer lastInt = accountingManager.getProductLastIntNumber();
                        numberData = buildCustomNumberData(lastInt, product.getCategoryID());
                    } else {
                        numberData = generateProductNumber();
                    }
                    item.setProductNumber(numberData.getNumberString());
                    item.setIntNumber(numberData.getIntNumber());
                }
            } else {
                item.setProductNumber(numberData.getNumberString());
                item.setIntNumber(null);
            }
            item.setName(product.getItemName());
            item.setDescription(product.getDescription());

            if (product.getZapiervariantid() != null) {
                item.setZapiervariantid(product.getZapiervariantid());
            }

            if (product.getCategoryID() != null) {
                item.setCategory(productCategoryManager.get(product.getCategoryID()));
            }

            if (product.getVatId() != null) {
                item.setVat(vatManager.get(product.getVatId()));
            }
            if (product.getDoubleVatId() != null) {
                item.setDoubleVat(vatManager.get(product.getDoubleVatId()));
            }

            item.setInternalSKUNumber(product.getInternalSKUNumber());
            item.setManufacturer(product.getManufacturer());
            item.setPartNumber(product.getPartNumber());
            item.setBarCode(product.getBarCodeText());
            item.setActive(product.isActive());
            item.setSentToTextileFinds(product.isSentToTextileFinds());
            if (product.getCustomer() != null && product.getCustomer().getId() != null) {
                item.setCustomer(crmAccountManager.get(product.getCustomer().getId()));
            }

            if (product.getQRCodeSizeID() != null) {
                item.setQRCodeSizeID(product.getQRCodeSizeID());
            }
            item.setUpcNumber(product.getUpcNumber());
            if (product.getUnitMeasurementID() != null) {
                item.setUnitMeasurement(unitMeasurementManager.get(product.getUnitMeasurementID()));
            } else {
                item.setUnitMeasurement(null);
            }

            if (product.getVendorItem() != null && product.getVendorItem().getId() != null) {
                item.setVendor(crmAccountManager.get(product.getVendorItem().getId()));
            }
            item.setWeightPerUnit(product.getWeightPerUnit());
            item.setUnitPrice(product.getUnitPrice());
            item.setSellingPrice(product.getSellingPrice());
            item.setComission(product.getComission());
            item.setEnableIT(product.enableIT());
//            item.setShowOnOpportunity(product.isShowOnOpportunity());
            if (product.getBrandID() != null && product.getBrandID() > 0) {
                item.setBrand(brandManager.get(product.getBrandID()));
            } else {
                item.setBrand(null);
            }

            //set Income account
            if (product.getAccountId() != null) {
                item.setAccount(accountingManager.get(product.getAccountId()));
            }

            //set Cogs account
            if (product.getCogsAccountID() != null) {
                item.setCogsAccount(accountingManager.get(product.getCogsAccountID()));
            }

            //Inventory Item Total information
            //set asset account
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
                // Textile uchun — default account "10" kod bilan
                EdsAccount defaultAssetAccount = accountingManager.getAccountByCode("10");
                if (defaultAssetAccount != null) {
                    item.setAssetAccount(defaultAssetAccount);
                }
            } else if (product.getAssetAccountID() != null) {
                item.setAssetAccount(accountingManager.get(product.getAssetAccountID()));
            }

            item.setGlobalReorderPoint(product.getGlobalReorderPoint());
            item.setTotalValue(product.getTotalValue());
            item.setAsOf(product.getAsOf() != null ? product.getAsOf().getNonConvertedDate() : null);
            item.setPurchasedFromSupplier(product.isPurchasedFromSupplier());
            item.setSoldToCustomer(product.isSoldToCustomer());

            if (product.getNimbleOfferID() != null) {
                item.setNimbleOfferID(product.getNimbleOfferID());
            }

            item.getSuppliers().clear();
            if (product.getSuppliers() != null) {
                product.getSuppliers();
                for (SelectItem supplier : product.getSuppliers()) {
                    item.getSuppliers().add(crmAccountManager.get(supplier.getId()));
                }
            }

            //product category custom fields
            EdsItemCustomFields itemCustomFields = createProductCustomFields(product.getCategoryCustomFieldItems(), product.getObjectId() != null, item);
            item.setItemCustomFields(itemCustomFields);

            if (product.getProductCustomFieldItems() != null && !product.getProductCustomFieldItems().isEmpty()) {
                StringBuilder changesBuilder = new StringBuilder();
                for (CompanyCustomFieldItem cit : product.getProductCustomFieldItems()) {
                    changesBuilder.append(item.getCustomFields() != null && CustomFieldsUtils.getObjectValue(item.getCustomFields(), cit.getColumnCode()) != null ? getChanges(CustomFieldsUtils.getObjectValue(item.getCustomFields(), cit.getColumnCode()), cit) : (cit.getColumnCode() + ","));
                }
                String changes = changesBuilder.toString();
                if (!changes.isEmpty()) {
                    item.addCustomFieldChanges(changes);
                }
            }

            //product custom fields
            EdsItemCustomFields customFields = createProductCustomFields(product.getProductCustomFieldItems(), product.getObjectId() != null, item);
            item.setCustomFields(customFields);

            if (product.getParentId() != null) {
                item.setParent(itemManager.get(product.getParentId()));
            }

            item.setStorefrontEnable(product.isStorefrontEnable() != null ? product.isStorefrontEnable() : false);
            if (item.isStorefrontEnable()) {
                applyStorefrontOptions(product, item);
            }

            item.setOrder(product.getOrder());

            if (item.getType().equals(RENTAL_ITEM)) {
                item.setExtraHour(product.getExtraHour());
                item.setExtraDay(product.getExtraDay());
                item.setSecurityTime(product.getSecurityTime());

                if (!CollectionUtils.isEmpty(product.getRentalProductItems())) {
                    if (item.getRentalItems() != null && !item.getRentalItems().isEmpty()) {
                        itemManager.deleteRentalItems(item);
                    }
                    for (RentalProductItem rentalProductItem : product.getRentalProductItems()) {
                        EdsRentalProductItem edsRentalProductItem = new EdsRentalProductItem();
                        edsRentalProductItem.setDescription(rentalProductItem.getDescription());
                        edsRentalProductItem.setUnitCode(rentalProductItem.getUnitCode());
                        edsRentalProductItem.setPrice(rentalProductItem.getPrice());
                        item.addRentalItem(edsRentalProductItem);
                    }
                }
            }
            if (product.getSaasuGUID() != null) {
                item.setSaasuGUID(product.getSaasuGUID());
            }
            if (product.getSasuuLastUpdatedDate() != null) {
                item.setSasuuLastUpdatedDate(product.getSasuuLastUpdatedDate());
            }

            if (product.isFromSaasu()) {
                item.setLastUpdateTime(product.getSasuuLastUpdatedDate());
            } else {
                item.setLastUpdateTime(new Date());
            }
            item.setUpdater(userManager.getUser());
            if (product.getQuickbookItemID() != null) {
                item.setQuickbookItemID(product.getQuickbookItemID());
                item.setQuickbookEditSequence(product.getQuickbookEditSequence());
            }
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsItem.class.getSimpleName());

            if (product.getSubsidiaryProductUniqueID() != null && !"".equals(product.getSubsidiaryProductUniqueID())) {
                item.setSubsidiaryProductUniqNum(product.getSubsidiaryProductUniqueID());
            } else if (product.getItemNameID() != null) {
                EdsSubsidiaryProduct subsidiaryProduct = subsidiaryProductManager.get(product.getItemNameID());
                if (subsidiaryProduct != null) {
                    item.setSubsidiaryProductUniqNum(subsidiaryProduct.getUniqNumber());
                }
            }
            if (product.getCurrencyId() != null) {
                item.setCurrency(currencyManager.getCurrency(product.getCurrencyId()));
                item.setExchangeRate(product.getExchangeRate());
            } else {
                item.setCurrency(null);
                item.setExchangeRate(null);
            }
            if (product.isBarcodeNumberingEnabled()) {
                item.setBarcodeFile((EdsUpload) uploadManager.get(product.getBarcodeID()));
                item.setBarcodeChecksum(product.getBarcodeChecksum());
            }
            if (product.getDefaultItemWarehouse() != null) {
                item.setDefaultWarehouse(warehouseManager.get(product.getDefaultItemWarehouse()));
            } else {
                item.setDefaultWarehouse(null);
            }
            item.setBarcodeChecksum(product.getBarcodeChecksum());
            item.setTrackBatchesEnabled(product.getTrackBatchesEnabled());
            EdsAttachment photo = null;
            if (product.getImageId() != null && item.getPhoto() == null) {
                photo = attachmentsManager.get(product.getImageId());
            } else if (item.getPhoto() != null) {
                photo = attachmentsManager.get(item.getPhoto().getObjectID());
            }
            item.setPhoto(photo);

            item.getLocations().clear();
            if (product.getLocations() != null) {
                item.setLocations(product.getLocations().stream().map(l -> locationManager.get(l.getId())).collect(Collectors.toSet()));
            }

            if (item.getObjectID() != null) {
                kpiLog.setActionType(KpiLog.ActionType.UPDATE);
                kpiLog.setEntityId(product.getObjectId());
                ServerUtils.kpiLog(log, kpiLog, "Product updated");

                itemManager.update(item);
                baseEventPostProcessor.registerEvent(ProductEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, item, itemManager.getUser());

                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, item, itemManager.getUser());
                workflowEvent.setEntityType(item.getRentalItem() != null && item.getRentalItem() ? RelationItem.TYPE_RENTAL_PRODUCT : RelationItem.TYPE_PRODUCT);
            } else {
                item.setCreationTime(new Date());
                item.setCreator(this.userManager.getUser());
                UUID externalGUID = UUID.randomUUID();
                item.setExternalGUID(externalGUID.toString());
                itemManager.create(item);
                baseEventPostProcessor.registerEvent(ProductEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, item, itemManager.getUser());
                newCreated = true;
                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, item, itemManager.getUser());
                workflowEvent.setEntityType(item.getRentalItem() != null && item.getRentalItem() ? RelationItem.TYPE_RENTAL_PRODUCT : RelationItem.TYPE_PRODUCT);

                kpiLog.setActionType(KpiLog.ActionType.ADD);
                kpiLog.setEntityId(item.getObjectID());
                ServerUtils.kpiLog(log, kpiLog, "New product added");
                item.setCreationTime(new Date());
                item.setCreator(userManager.getUser());


                if (item.getSubsidiaryProductUniqNum() == null || item.getSubsidiaryProductUniqNum().trim().isEmpty()) {
                    item.setSubsidiaryProductUniqNum(item.getObjectID() + EdsItem.PRODUCT_UNIQ_NUM + company.getObjectID());
                    boolean showMultiCompanyManagement = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP);
                    if (company.getParentCompanyId() != null || showMultiCompanyManagement) {
                        SelectItem productItem = new SelectItem(company.getObjectID(), item.getName(), item.getSubsidiaryProductUniqNum());
                        List<SelectItem> productItemList = new ArrayList<>();
                        productItemList.add(productItem);
//                        rabbitMQService.sendSubsidiariesProduct(productItemList, showMultiCompanyManagement ? company.getObjectID() : company.getParentCompanyId());
                    }
                }
            }

            if (item.getObjectID() != null) {
                for (EdsItem child : itemManager.getChildProducts(item.getObjectID())) {
                    activateProduct(child.getObjectID(), item.isActive());
                }
            }

            //On hand is equal ZERO, then batch tracking enabled
            item.setInventoryTrackingEnabled(product.getInventoryTrackingEnabled());
            item.setBatchTrackingEnabled(product.getBatchTrackingEnabled());
            if ((item.getType().equals(INVENTORY_ITEM) || item.getType().equals(ASSEMBLY_ITEM)) && item.enableIT() && (product.getQuantity().compareTo(BigDecimal.ZERO) > 0 || !runWebhook)) {
                if (item.getObjectID() == null || (item.getObjectID() != null && !itemStockManager.isUsedInTransactions(item.getObjectID()))) {
                    transactionID = accountingServiceLocal.createTransactionsForInventory(item, itemManager.getUser());

                    if (!runWebhook && (product.getProductLocations() == null || product.getProductLocations().length == 0)) {
                        ProductLocationItem locationItem = new ProductLocationItem();
                        locationItem.setQty(BigDecimal.ZERO);
                        product.setProductLocations(new ProductLocationItem[]{locationItem});
                    }
                    updateInventoryStockPerWarehouse(product, item, transactionID);
                }
            }
            setLocationOnly(product, item, transactionID);

            if (!ASSEMBLY_ITEM.equals(item.getType())) {
                itemManager.deleteProductAssemblyItems(item.getObjectID());
            } else {
                List<EdsAssemblyItem> assemblyItems = creatAssemblyItems(item, product.getAssemblyItems());
                item.setAssemblyItems(assemblyItems);
            }
            item.setCostAllocationType(null);
            if (item.getType().equals(PRODUCT_KIT)) {
                updateProductKitItems(product.getProductKitItems(), item);
            }

            if (product.getImageGallery() != null) {
                List<ProductPicture> reversedGallery = new ArrayList<>(List.of(product.getImageGallery()));
                Collections.reverse(reversedGallery);
                for (ProductPicture picture : reversedGallery) {
                    Integer newParentid = 0;
                    EdsProductPicture productPicture = productPictureManager.get(picture.getPictureID());
                    if (productPicture != null) {
                        if (product.isCopied()) {
                            EdsProductPicture copyPicture = productPicture.cloneShallow();
                            copyPicture.setProduct(item);
                            copyPicture.setDefaultPicture(picture.isDefaultPicture());
                            productPictureManager.createBlank(copyPicture);
                            if (copyPicture.getObjectID() != null) {
                                newParentid = copyPicture.getObjectID();
                            }
                            item.setPicture(copyPicture);
                            itemManager.update(item);
                            uploadManager.createCopy(productPicture.getObjectID(), copyPicture);
                        } else {
                            productPicture.setProduct(item);
                            productPicture.setDefaultPicture(picture.isDefaultPicture());
                            productPictureManager.update(productPicture);
                            item.setPicture(productPicture);
                            itemManager.update(item);
                        }

                        List<EdsProductPicture> productSubPictures = productPictureManager.getProductSubPictures(picture.getPictureID());
                        for (EdsProductPicture subPicture : productSubPictures) {
                            if (product.isCopied()) {
                                EdsProductPicture copySubProductPicture = subPicture.cloneShallow();
                                copySubProductPicture.setProduct(item);
                                copySubProductPicture.setDefaultPicture(picture.isDefaultPicture());
                                copySubProductPicture.setParentId(newParentid);
                                productPictureManager.createBlank(copySubProductPicture);
                                uploadManager.createCopy(subPicture.getObjectID(), copySubProductPicture);
                            } else {
                                subPicture.setProduct(item);
                                subPicture.setDefaultPicture(picture.isDefaultPicture());
                            }
                        }

                    }
                }
            }

            if (product.getAttachments() != null && product.getAttachments().length > 0) {
                attachmentUtilsManager.saveAttachments(F_PRODUCTS_SERVICES, item.getObjectID(), item.getObjectID(), product.getAttachments());
            }
            if (product.getProductSerialItems() != null && !product.getProductSerialItems().isEmpty()) {
                EdsProductSerial edsProductSerial;
                for (ProductSerialItem serialItem : product.getProductSerialItems()) {
                    edsProductSerial = new EdsProductSerial();
                    if (serialItem.getObjectID() != null) {
                        edsProductSerial = productSerialManager.get(serialItem.getObjectID());
                    }
                    edsProductSerial.setObjectID(serialItem.getObjectID());
                    edsProductSerial.setSerial(serialItem.getSerial());
                    edsProductSerial.setExpirationDate(serialItem.getExpirationDate());
                    edsProductSerial.setLotNumber(serialItem.getLotNumber());
                    edsProductSerial.setRefNumber(serialItem.getRefNumber());
                    edsProductSerial.setItemID(item.getObjectID());
                    productSerialManager.createOrUpdate(edsProductSerial);
                }
            }
            itemMultiPriceManager.deleteItemMultiPrices(item.getObjectID());
            if (!product.getMultiPrices().isEmpty()) {
                EdsItemMultiPrice itemMultiPrice;
                for (MultiPriceItem multiPriceItem : product.getMultiPrices()) {
                    itemMultiPrice = new EdsItemMultiPrice();
                    if (multiPriceItem.getCurrency() != null) {
                        itemMultiPrice.setCurrency(currencyManager.get(multiPriceItem.getCurrency().getId()));
                    }
                    itemMultiPrice.setItem(item);
                    itemMultiPrice.setSellingPrice(multiPriceItem.getPrice());
                    itemMultiPrice.setType(multiPriceItem.getType());
                    itemMultiPriceManager.create(itemMultiPrice);
                }
            }
            //clear and set discounts
            if (product.getDiscountItems() != null && product.getDiscountItems().length > 0) {
                item.getDiscounts().clear();
                List<EdsDiscount> newDiscounts = new ArrayList<>();
                for (DiscountItem discountItem : product.getDiscountItems()) {
                    newDiscounts.add(discountManager.get(discountItem.getId()));
                }
                item.getDiscounts().addAll(newDiscounts);
                itemManager.update(item);
            }
            try {
                productsServicesSolrComponent.index(item);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (item.getCategory() != null) {
                result = new ProductSelectItem(item.getObjectID(), ((item.getProductNumber() != null && !"".equals(item.getProductNumber())) ? item.getProductNumber() + " -> " : "") + item.getName(), item.getCategory().getName(),
                        item.getProductType(), item.isPurchasedFromSupplier());
            } else {
                result = new ProductSelectItem(item.getObjectID(), ((item.getProductNumber() != null && !"".equals(item.getProductNumber())) ? item.getProductNumber() + " -> " : "") + item.getName(), item.getProductType(), item.isPurchasedFromSupplier());
            }


            if (runWebhook) {
                try {
                    String entityName = product.getParentId() != null ? "productvariant" : "product";

                    List<EdsRestHook> webhooks = restHookManager.getByEventName(entityName + (newCreated ? ".create" : ".update"));
                    if (!webhooks.isEmpty()) {
                        for (EdsRestHook webhook : webhooks) {
                            try {
                                if (!"https://hooks.zapier.com/fake-subscription-url".equalsIgnoreCase(webhook.getTargetUrl())) {
                                    log.info("Triggering webhook {}: {}", webhook.getEventName(), webhook.getTargetUrl());

                                    //set Item in stock
                                    product.setItemsInStock(item.getItemsInStock());

                                    HttpHeaders httpHeaders = new HttpHeaders();
                                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                                    if (product.getParentId() != null) {
                                        HttpEntity<ZapierProductVariantTO> httpRequest = new HttpEntity<>(convertToZapierProductVariant(product), httpHeaders);
                                        String resp = restTemplate.postForObject(webhook.getTargetUrl(), httpRequest, String.class);
                                        log.info("ZAPIER WEBHOOK PRODUCT VARIANT: {}", resp);
                                    } else {
                                        HttpEntity<ProductListItemTO> httpRequest = new HttpEntity<>(convertToZapierProduct(product), httpHeaders);
                                        String resp = restTemplate.postForObject(webhook.getTargetUrl(), httpRequest, String.class);
                                        log.info("ZAPIER WEBHOOK PRODUCT: {}", resp);
                                    }
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
            product.setNumberData(new NumberData(item.getProductNumber(), item.getIntNumber()));
            product.setObjectId(item.getObjectID());
            product.setLastUpdateTime(item.getLastUpdateTime());
            product.setCreatedDate(item.getCreationTime());
            return result;
        }

        return new ProductSelectItem();
    }

    private NumberData buildCustomNumberData(Integer intNumber, Integer categoryId) {
        EdsProductCategory cat = productCategoryManager.get(categoryId);
        String sequence = String.format("%04d", intNumber != null ? intNumber : 1);
        String companyId = ServerSecurityContext.getInstance().getCompanyId();
        String categoryCode = cat != null ? cat.getCode() : "GEN";
        String numberString = categoryCode + "-" + companyId + "-" + sequence;

        NumberData numberData = new NumberData();
        numberData.setNumberString(numberString);
        numberData.setIntNumber(intNumber);
        numberData.setSavedNumberFormula(categoryCode + SAV_NUM_DEL + companyId + SAV_NUM_DEL + sequence);
        numberData.setFirstNumberString(categoryCode);
        return numberData;
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
                Date date = (Date) ob;
                return !date.equals(item.getFieldDateNonConvertedValue() != null ? item.getFieldDateNonConvertedValue().getNonConvertedDate() : null) ? (item.getColumnCode() + ",") : "";
            }
        }
        return "";
    }

    public ProductListItemTO convertToZapierProduct(NewProduct item) {
        ProductListItemTO product = new ProductListItemTO();
        product.setId(item.getObjectId());
        product.setDescription(item.getDescription());
        if (item.getVendorItem() != null) {
            product.setVendor(item.getVendorItem().getName());
        } else if (item.getSuppliers() != null && item.getSuppliers().length > 0) {
            product.setVendor(item.getSuppliers()[0].getName());
        }
        product.setQuantity(item.getItemsInStock());
        product.setInventory_policy("Yes");
        product.setImage_url(getDefaultProductPictureUrlByID(productPictureManager.getDefaultProductPictureByFileSizeType(item.getObjectId(), 0)));
        product.setName(item.getItemName());
        product.setNumber(item.getNumberData().getNumberString());
        if (StringUtils.isNotBlank(item.getInternalSKUNumber())) {
            product.setSku_number(item.getInternalSKUNumber());
        }
        if (StringUtils.isNotBlank(item.getBarCodeText())) {
            product.setBarcode(item.getBarCodeText());
        }
        product.setProduct_type(new ProductTypeTO(item.getType(), item.getTypeName()));
        if (item.getCategoryID() != null) {
            product.setCategory(new ProductCategoryTO(item.getCategoryID(), item.getCategoryName()));
        }

        if (AccountingConstants.INVENTORY_ITEM.equals(item.getType()) || AccountingConstants.ASSEMBLY_ITEM.equals(item.getType())
                || AccountingConstants.PRODUCT_KIT.equals(item.getType())) {
            product.setUnit_price(item.getSellingPrice());
            product.setCost_price(item.getUnitPrice());
        } else if (AccountingConstants.NON_INVENTORY_ITEM.equals(item.getType()) || AccountingConstants.SERVICE.equals(item.getType())
                || AccountingConstants.OTHER_CHARGE.equals(item.getType())) {
            product.setRate(item.getSellingPrice());
        }
        return product;
    }

    public ZapierProductVariantTO convertToZapierProductVariant(NewProduct productVariant) {
        if (productVariant != null && productVariant.getParentId() != null) {

            ZapierProductVariantTO zapierProductVariant = new ZapierProductVariantTO();
            zapierProductVariant.setId(productVariant.getObjectId());
            zapierProductVariant.setName(productVariant.getItemName());
            zapierProductVariant.setUnit_price(productVariant.getUnitPrice());
            zapierProductVariant.setSku_number(productVariant.getInternalSKUNumber());
            zapierProductVariant.setQuantity(productVariant.getItemsInStock());

            NewProduct parentProduct = getProduct(productVariant.getParentId());

            zapierProductVariant.setParent_id(productVariant.getParentId());
            zapierProductVariant.setParent_description(parentProduct.getDescription());
            if (parentProduct.getVendorItem() != null) {
                zapierProductVariant.setParent_vendor(parentProduct.getVendorItem().getName());
            } else if (parentProduct.getSuppliers() != null && parentProduct.getSuppliers().length > 0) {
                zapierProductVariant.setParent_vendor(parentProduct.getSuppliers()[0].getName());
            }
            zapierProductVariant.setParent_quantity(parentProduct.getItemsInStock());
            zapierProductVariant.setParent_inventory_policy("Yes");
            zapierProductVariant.setParent_image_url(getDefaultProductPictureUrlByID(productPictureManager.getDefaultProductPictureByFileSizeType(parentProduct.getObjectId(), 0)));
            zapierProductVariant.setParent_name(parentProduct.getItemName());
            zapierProductVariant.setParent_number(parentProduct.getNumberData().getNumberString());
            if (StringUtils.isNotBlank(parentProduct.getInternalSKUNumber())) {
                zapierProductVariant.setParent_sku_number(parentProduct.getInternalSKUNumber());
            }
            if (StringUtils.isNotBlank(parentProduct.getBarCodeText())) {
                zapierProductVariant.setParent_barcode(parentProduct.getBarCodeText());
            }
            zapierProductVariant.setParent_product_type(new ProductTypeTO(parentProduct.getType(), parentProduct.getTypeName()));
            if (parentProduct.getCategoryID() != null) {
                zapierProductVariant.setParent_category(new ProductCategoryTO(parentProduct.getCategoryID(), parentProduct.getCategoryName()));
            }

            if (AccountingConstants.INVENTORY_ITEM.equals(parentProduct.getType()) || AccountingConstants.ASSEMBLY_ITEM.equals(parentProduct.getType())
                    || AccountingConstants.PRODUCT_KIT.equals(parentProduct.getType())) {
                zapierProductVariant.setParent_unit_price(parentProduct.getSellingPrice());
                zapierProductVariant.setParent_cost_price(parentProduct.getUnitPrice());
            } else if (AccountingConstants.NON_INVENTORY_ITEM.equals(parentProduct.getType()) || AccountingConstants.SERVICE.equals(parentProduct.getType())
                    || AccountingConstants.OTHER_CHARGE.equals(parentProduct.getType())) {
                zapierProductVariant.setParent_rate(parentProduct.getSellingPrice());
            }
            return zapierProductVariant;
        } else {
            return null;
        }
    }

    private void setLocationOnly(NewProduct product, EdsItem item, Integer transactionID) {
        if (product.getProductLocations() != null) {
            EdsWarehouse defaultWarehouse = warehouseManager.getDefaultWarehouse();
            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            boolean isMultiWarehouseEnabled = financialSettings.getEnableMultiWarehouse();

            ProductLocationItem[] productLocations = product.getProductLocations();
            for (int i = 0; i < productLocations.length; i++) {
                EdsWarehouse warehouse = (isMultiWarehouseEnabled && productLocations[i].getWarehouseID() != null) ? warehouseManager.get(productLocations[i].getWarehouseID()) : defaultWarehouse;
                EdsProductWarehouseLocation productWarehouseLocation;

                if (productLocations[i].getObjectID() != null) {
                    productWarehouseLocation = productWarehouseLocationManager.get(productLocations[i].getObjectID());
                    if (productWarehouseLocation == null) {
                        productWarehouseLocation = new EdsProductWarehouseLocation();
                    }
                } else {
                    productWarehouseLocation = new EdsProductWarehouseLocation();
                }

                productWarehouseLocation.setProduct(item);
                productWarehouseLocation.setWarehouse(warehouse);
                productWarehouseLocation.setProductLocation(productLocations[i].getProductLocationID() != null ? productLocationManager.get(productLocations[i].getProductLocationID()) : null);
                productWarehouseLocation.setMinReorderPoint(productLocations[i].getMinReorderPoint());
                productWarehouseLocationManager.createOrUpdate(productWarehouseLocation);

                if (product.getInventoryTrackingEnabled()) {
                    productWarehouseLocation.setItemSerials(productLocations[i].getSerials());
                    itemSerialService.createForOpeningBalance(productWarehouseLocation, transactionID);
                }
                if (product.getTrackBatchesEnabled()) {
                    productWarehouseLocation.setTrackBatchItems(productLocations[i].getTrackBatchItems());
                    itemBatchService.createForOpeningBalance(productWarehouseLocation);
                }
            }
        }
    }

    private List<EdsAssemblyItem> creatAssemblyItems(EdsItem pItem, List<AssemblyItem> assemblyItems) {
        List<EdsAssemblyItem> result = new ArrayList<>();
        List<Integer> assemblyItemIDs = assemblyItemManager.getItemsIdByProduct(pItem.getObjectID());
        if (assemblyItems != null && !assemblyItems.isEmpty()) {
            for (AssemblyItem item : assemblyItems) {
                EdsAssemblyItem assemblyItem = null;
                if (item.getAssemblyItemId() != null) {
                    assemblyItem = assemblyItemManager.get(item.getAssemblyItemId());
                    assemblyItemIDs.remove(assemblyItem.getObjectID());
                }
                if (assemblyItem == null) {
                    assemblyItem = new EdsAssemblyItem();
                }
                if (pItem != null) {
                    assemblyItem.setItem(pItem);
                }
                if (item.getProduct() != null && item.getProduct().getId() != null) {
                    assemblyItem.setProductItem(itemManager.get(item.getProduct().getId()));
                }
//                assemblyItem.setType(item.getProductType());
                assemblyItem.setDescription(item.getDescription());
                assemblyItem.setQty(item.getQuantity());
                assemblyItem.setCostPrice(item.getCostPrice());
                assemblyItem.setTotalValue(item.getTotal());
                assemblyItemManager.createOrUpdate(assemblyItem);
                result.add(assemblyItem);
            }
            if (assemblyItemIDs != null && !assemblyItemIDs.isEmpty()) {
                String ids = ServerUtils.getAsCommoDelimited(assemblyItemIDs, "0", ",");
                assemblyItemManager.deleteBatchAssemblyItems(ids);
            }
        }
        return result;
    }

    private ArrayList<AssemblyItem> wrapAssemblyItems(List<EdsAssemblyItem> assemblyItems) {
        ArrayList<AssemblyItem> result = new ArrayList<>();
        if (assemblyItems != null && !assemblyItems.isEmpty()) {
            AssemblyItem item = null;
            for (EdsAssemblyItem assemblyItem : assemblyItems) {
                item = new AssemblyItem();
                EdsItem productItem = assemblyItem.getProductItem();
                if (productItem != null) {
                    if (productItem.getProductNumber() != null && !"".equals(productItem.getProductNumber())) {
                        item.setProduct(new SelectItem(productItem.getObjectID(), productItem.getProductNumber() + " -> " + productItem.getName()));
                    } else {
                        item.setProduct(new SelectItem(productItem.getObjectID(), productItem.getName()));
                    }
                    ArrayList<MultiPriceItem> multiPriceItems = new ArrayList<>();
                    for (EdsItemMultiPrice mulPriceItem : productItem.getMultiPrices()) {
                        if (PAYABLE.equals(mulPriceItem.getType())) {
                            MultiPriceItem multiPriceItem = new MultiPriceItem();
                            multiPriceItem.setCurrency(mulPriceItem.getCurrency() != null ? mulPriceItem.getCurrency().getAsSelectItem() : null);
                            multiPriceItem.setPrice(mulPriceItem.getSellingPrice());
                            multiPriceItems.add(multiPriceItem);
                        }
                    }
                    item.setProductDefaultWarehouse(productItem.getDefaultWarehouse() != null ? productItem.getDefaultWarehouse().getAsSelectItem() : null);
                    item.setMultiPriceItems(multiPriceItems);
                    item.setItemsInStock(productItem.getQty());
                    item.setActive(productItem.isActive());
                    item.setProductPrice(productItem.getUnitPrice());
                    item.setProductSellingPrice(productItem.getSellingPrice());
                    Set<EdsLocation> locations = productItem.getLocations();
                    ArrayList<Integer> locationIds = new ArrayList<>();
                    if (!locations.isEmpty()) {
                        locationIds = locations.stream()
                                .map(EdsLocation::getObjectID)
                                .collect(Collectors.toCollection(ArrayList::new));
                        item.setLocationIds(locationIds);
                    }
                }
                if (assemblyItem.getObjectID() != null) {
                    item.setAssemblyItemId(assemblyItem.getObjectID());
                }

                item.setDescription(assemblyItem.getDescription());
                item.setQuantity(assemblyItem.getQty());
                item.setCostPrice(assemblyItem.getCostPrice());
                item.setSellingPrice(assemblyItem.getItem().getSellingPrice());
                item.setProductType(assemblyItem.getType());
                item.setCategory(productItem.getCategory() != null ? productItem.getCategory().getName() : null);
                result.add(item);
            }
        }

        result.sort(Comparator
                .comparing(AssemblyItem::getCategory, Comparator.nullsLast(String::compareToIgnoreCase))
                .thenComparing(item -> item.getProduct() != null ? item.getProduct().getName() : "", String.CASE_INSENSITIVE_ORDER));

        return result;
    }


    public Integer[] saveProducts(NewProduct[] products) {
        if (products != null) {
            Integer[] result = new Integer[products.length];
            for (int i = 0; i < products.length; i++) {
                result[i] = saveProduct(products[i]).getId();
            }

            return result;
        }

        return new Integer[0];
    }

    @Override
    public VariationItem getProductForVariation(Integer productID) {
        VariationItem variationItem = new VariationItem();

        NewProduct product = getProduct(productID);
        variationItem.setProduct(product);

        //{super functional o'ylangan edi no fail bo'ldi :)}
        return variationItem;
    }

    public Integer[] saveVariationProducts(NewProduct[] products, Integer parentProductId) {
        return this.saveVariationProducts(products, parentProductId, true);
    }

    public Integer[] saveVariationProducts(NewProduct[] products, Integer parentProductId, boolean runWebhook) {

        if (products != null) {
            EdsItem product = itemManager.get(parentProductId);

            Integer[] result = new Integer[products.length];
            for (int i = 0; i < products.length; i++) {
                NewProduct _product = getProduct(parentProductId);
                _product.setObjectId(null);
                _product.setNumberData(null);
                _product.setCategoryCustomFieldItems(products[i].getCategoryCustomFieldItems());
                _product.setItemName(products[i].getItemName());
                _product.setSellingPrice(products[i].getSellingPrice());
                _product.setQuantity(products[i].getQuantity());
                _product.setParentId(parentProductId);
                _product.setEnableIT(product.enableIT());

                _product.setActive(Boolean.TRUE);
                _product.setAsOf(new DateNonConvertable());

                _product.setZapiervariantid(products[i].getZapiervariantid());
                _product.setInternalSKUNumber(products[i].getInternalSKUNumber());
                _product.setBarCodeText(products[i].getBarCodeText());
                if (products[i].getUnitPrice() != null) {

                    _product.setUnitPrice(products[i].getUnitPrice());
                    _product.setTotalValue(Optional.ofNullable(products[i].getUnitPrice()).orElse(BigDecimal.ZERO).multiply(Optional.ofNullable(products[i].getQuantity()).orElse(BigDecimal.ZERO)));
                }
                if (products[i].getAssetAccountID() != null) {
                    _product.setAssetAccountID(products[i].getAssetAccountID());
                }
                if (products[i].getCogsAccountID() != null) {
                    _product.setCogsAccountID(products[i].getCogsAccountID());
                }
                if (products[i].getCategoryID() != null) {
                    _product.setCategoryID(products[i].getCategoryID());
                }

                if (_product.getProductLocations() != null && _product.getProductLocations().length > 0) {
                    for (int pl = 0; pl < _product.getProductLocations().length; pl++) {
                        _product.getProductLocations()[pl].setObjectID(null);
                        _product.getProductLocations()[pl].setQty(products[i].getQuantity());
                    }
                } else {
                    List<ProductLocationItem> locationItems = new ArrayList<>();
                    ProductLocationItem locationItem = new ProductLocationItem();
                    locationItem.setQty(products[i].getQuantity());
                    locationItem.setMinReorderPoint(BigDecimal.ONE);
                    locationItem.setMinReorderQty(BigDecimal.ONE);
                    locationItems.add(locationItem);

                    _product.setProductLocations(locationItems.toArray(new ProductLocationItem[]{}));
                }
                if (_product.getAssemblyItems() != null && !_product.getAssemblyItems().isEmpty()) {
                    ArrayList<AssemblyItem> assemblyItems = _product.getAssemblyItems();
                    for (AssemblyItem assemblyItem : assemblyItems) {
                        assemblyItem.setAssemblyItemId(null);
                    }
                    _product.setAssemblyItems(assemblyItems);
                }
                if (_product.getProductKitItems() != null && _product.getProductKitItems().length > 0) {
                    ProductKitItem[] kitItems = _product.getProductKitItems();
                    for (ProductKitItem kitItem : kitItems) {
                        kitItem.setProductKitID(null);
                    }
                    _product.setProductKitItems(kitItems);
                }

                result[i] = saveProduct(_product, runWebhook).getId();

                if (products[i].getVariationCombinate() != null && !products[i].getVariationCombinate().isEmpty()) {
                    EdsItemVariations variation = new EdsItemVariations();
                    variation.setCombination(products[i].getVariationCombinate().get(0));
                    variation.setVariationID(result[i]);
                    variation.setProduct(product);
                    product.getVariationses().add(variation);
                }
            }

            product.setHasVariations(true);
            itemManager.update(product);
            return result;
        }

        return new Integer[0];
    }

    @Override
    public TestRPC saveStockAdjustment(AdjustmentItem adjustmentItem) {
        TestRPC result = new TestRPC();

        if (adjustmentItem != null) {
            boolean numberExists = stockAdjustmentManager.numberExists(adjustmentItem.getNumber(), adjustmentItem.getObjectID());

            if (numberExists) {
                result.setMessageCommand(MessageCommand.isNumberExists);
                result.setMessage(generateStockAdjustmentNumberFormat().getTransferNumber());
                return result;
            }
        }

        EdsStockAdjustment stockAdjustment = null;
        EdsUser currentUser = userManager.getUser();
        if (adjustmentItem.getObjectID() != null) {
            stockAdjustment = stockAdjustmentManager.get(adjustmentItem.getObjectID());
            stockAdjustment.setUpdater(currentUser);
            stockAdjustment.setLastUpdateTime();
            itemBatchService.deleteBatchSerialsForAdjustment(stockAdjustment.getObjectID());
        } else {
            stockAdjustment = new EdsStockAdjustment();
            stockAdjustment.setCreator(currentUser);
            stockAdjustment.setCreationTime();
        }
        if (adjustmentItem.getIntNumber() != null) {
            stockAdjustment.setIntNumber(adjustmentItem.getIntNumber());
        }
        stockAdjustment.setNumber(adjustmentItem.getNumber());
        stockAdjustment.setDate(adjustmentItem.getDate() != null ? adjustmentItem.getDate().getNonConvertedDate() : new Date());
        stockAdjustment.setType(adjustmentItem.getType());

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            // Textile uchun — default account "10" kod bilan
            EdsAccount defaultAssetAccount = accountingManager.getAccountByCode("10");
            if (defaultAssetAccount != null) {
                stockAdjustment.setAccount(defaultAssetAccount);
            }
        } else if (adjustmentItem.getAccount() != null && adjustmentItem.getAccount().getId() != null) {
            stockAdjustment.setAccount(accountingManager.get(adjustmentItem.getAccount().getId()));
        }
        stockAdjustment.setMemo(adjustmentItem.getMemo());

        if (adjustmentItem.isStockTransfer()) {
            stockAdjustment.setStockTransfer(true);
            stockAdjustment.setFromWarehouse(warehouseManager.get(adjustmentItem.getFromWarehouseID()));
            stockAdjustment.setToWarehouse(warehouseManager.get(adjustmentItem.getToWarehouseID()));
            stockAdjustment.setFromAccount(accountingManager.get(adjustmentItem.getProductItems()[0].getAccountID()));
            stockAdjustment.setToAccount(accountingManager.get(adjustmentItem.getProductItems()[1].getAccountID()));
        }
        if (!isOk(adjustmentItem.getApprovers())) {
            stockAdjustment.setEntityStatus(referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, adjustmentItem.getStatusCode()));
        }
        boolean isNew = false;
        if (stockAdjustment.getObjectID() == null) {
            isNew = true;
            stockAdjustmentManager.create(stockAdjustment);
        }

        if (isOk(adjustmentItem.getApprovers())) {
            adjustmentItem.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirstApprover = true;
            for (ApproverItemMini approverItem : adjustmentItem.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (stockAdjustment.getCurrentApprover() != null && adjustmentItem.getStatusCode() != null && isFirstApprover) {
                        stockAdjustment.getCurrentApprover().setStatus(referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, adjustmentItem.getStatusCode()));
                        stockAdjustment.setEntityStatus(referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, Constants.STOCK_ADJUSTMENT_SUBMITTED));
                        isFirstApprover = false;
                    } else if (stockAdjustment.getCurrentApprover() != null && adjustmentItem.getStatusCode() != null) {
                        stockAdjustment.getCurrentApprover().setStatus(referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, Constants.STOCK_ADJUSTMENT_SUBMITTED));
                    }
                    if (adjustmentItem.getStatusCode() != null && !APPROVE.equals(adjustmentItem.getStatusCode())) {
                        stockAdjustment.setEntityStatus(referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, adjustmentItem.getStatusCode()));
                    }
                    if (stockAdjustment.isCurrentApproverRejected()) {
                        stockAdjustment.setEntityStatus(stockAdjustment.getCurrentApprover().getStatus());
                    }
                    continue;
                }
                EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(stockAdjustment.getObjectID());
                edsApprover.setIs_default(false);

                if (adjustmentItem.getStatusCode() != null && isFirstApprover) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, adjustmentItem.getStatusCode()));
                    if (Constants.DRAFT.equals(adjustmentItem.getStatusCode())) {
                        stockAdjustment.setEntityStatus(referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, adjustmentItem.getStatusCode()));
                    } else {
                        stockAdjustment.setEntityStatus(referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, STOCK_ADJUSTMENT_SUBMITTED));
                    }
                    isFirstApprover = false;
                } else if (adjustmentItem.getStatusCode() != null) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, STOCK_ADJUSTMENT_SUBMITTED));
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
                    edsApprover.getApproverRoles().add(roleapp);
                }

                for (EdsApproverEmployees ucerapp : _edsApprover.getApproverEmployees()) {
                    edsApprover.getApproverEmployees().add(ucerapp);
                }

                if (stockAdjustment.getCurrentApprover() == null) {
                    stockAdjustment.setCurrentApprover(edsApprover);
                }
                stockAdjustment.getApprovers().add(edsApprover);
            }
        }
        stockAdjustmentManager.create(stockAdjustment);
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        boolean isMultiWarehouseEnabled = financialSettings.getEnableMultiWarehouse();
        EdsWarehouse defaultWarehouse = warehouseManager.getDefaultWarehouse();
        convertRFPstoPO(adjustmentItem.getRfpIds());

        stockAdjustment.getAdjustmentItemList().clear();
        final Set<EdsItem> itemSet = Sets.newHashSet();

        if (adjustmentItem.getProductItems() != null) {
            for (ProductItem item : adjustmentItem.getProductItems()) {
                EdsAdjustmentItem edsAdjustmentItem;
                if (item.getLineItemID() != null) {
                    edsAdjustmentItem = stockAdjustmentItemManager.get(item.getLineItemID());
                } else {
                    edsAdjustmentItem = new EdsAdjustmentItem();
                }
                final EdsItem edsItem = itemManager.get(item.getObjectId());

                if (edsItem != null) {
                    itemSet.add(edsItem);
                }
                edsAdjustmentItem.setAdjustment(stockAdjustment);
                edsAdjustmentItem.setItem(edsItem);
                edsAdjustmentItem.setWarehouse(isMultiWarehouseEnabled ? warehouseManager.get(item.getWarehouseId()) : defaultWarehouse);
                if (item.getUnitMeasurementId() != null) {
                    edsAdjustmentItem.setMeasurement(unitMeasurementManager.get(item.getUnitMeasurementId()));
                }
                if (item.getDepartmentId() != null) {
                    edsAdjustmentItem.setDepartment(departmentManager.get(item.getDepartmentId()));
                }
                if (item.getAccountID() != null) {
                    edsAdjustmentItem.setAccount(accountingManager.get(item.getAccountID()));
                } else {
                    edsAdjustmentItem.setAccount(stockAdjustment.getAccount());
                }
                if (item.getCurrentQty() != null) {
                    edsAdjustmentItem.setCurrentQty(item.getCurrentQty());
                }
                edsAdjustmentItem.setUsedQty(item.getUsedQty());
                edsAdjustmentItem.setNewQty(item.getNewQty());
                edsAdjustmentItem.setQty(item.getTotalQty());
                edsAdjustmentItem.setPrice(item.getUnitpPrice() != null ? item.getUnitpPrice() : item.getNewQty() != null && item.getNewQty().compareTo(BigDecimal.ZERO) > 0 ? edsItem.getUnitPrice() : null);

                if (item.getProjectID() != null) {
                    edsAdjustmentItem.setProject(projectManager.get(item.getProjectID()));
                }
                if (edsItem != null && edsItem.getInventoryTrackingEnabled()) {
                    edsAdjustmentItem.setSerials(item.getSerials());
                    edsAdjustmentItem.setAssignedSerials(item.getAssignedSerials());
                }
                if (edsItem != null && edsItem.getTrackBatchesEnabled()) {
                    edsAdjustmentItem.setBatchItems(item.getBatchItems());
                    edsAdjustmentItem.setAssignedBatchItems(item.getAssignedBatchItems());
                }
                stockAdjustment.getAdjustmentItemList().add(edsAdjustmentItem);

                if (item.getProjectID() != null && item.getUsedQty().intValue() > 0) {//add expense only if diffQty() < 0
                    EdsProjectBudgetItem budgetItem = new EdsProjectBudgetItem();
                    budgetItem.setAccount(edsAdjustmentItem.getAccount());
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(new Date());
                    budgetItem.setMonth(calendar.get(Calendar.MONTH) + 1);
                    budgetItem.setYear(calendar.get(Calendar.YEAR) + 1900);
                    budgetItem.setType(EdsProjectBudgetItem.EXPENSE);

                    EdsProjectBudget projectBudget = projectBudgetManager.getBudgetByProject(item.getProjectID());
                    if (projectBudget == null) {
                        projectBudget = new EdsProjectBudget();
                        projectBudget.setProject(projectManager.get(item.getProjectID()));
                        projectBudgetManager.create(projectBudget);
                    }
                    budgetItem.setProjectBudget(projectBudget);
                    projectBudget.getItems().add(budgetItem);
                }
            }
        }
        if (adjustmentItem.getAttachments() != null && adjustmentItem.getAttachments().length > 0) {
            attachmentUtilsManager.saveAttachments(F_STOCK_ADJUSTMENT,
                    stockAdjustment.getObjectID(),
                    stockAdjustment.getObjectID(),
                    adjustmentItem.getAttachments());
        }
        createStockAdjustmentNote(adjustmentItem.getHistoryList(), stockAdjustment);
        if (!isOk(adjustmentItem.getApprovers())) {
            if (stockAdjustment.getOverallStatus() != null && STOCK_ADJUSTMENT_APPROVED.equals(stockAdjustment.getOverallStatus().getCode())) {
                Integer transactionId = accountingServiceLocal.createTransactionsForStockAdjustment(stockAdjustment);
                baseEventPostProcessor.registerEvent(StockAdjustmentEventListenerImpl.TYPE, StockAdjustmentEventListenerImpl.STOCK_ADJUSTMENT_APPROVED, stockAdjustment, userManager.getUser());

                for (EdsAdjustmentItem edsAdjustmentItem : stockAdjustment.getAdjustmentItemList()) {
                    if (edsAdjustmentItem.getItem() != null && edsAdjustmentItem.getItem().getInventoryTrackingEnabled()) {
                        itemSerialService.createForStockAdjustment(edsAdjustmentItem, transactionId);
                        itemSerialService.assignForStockAdjustment(edsAdjustmentItem, transactionId);
                    }
                }
            }
        }

        for (EdsAdjustmentItem edsAdjustmentItem : stockAdjustment.getAdjustmentItemList()) {
            if (edsAdjustmentItem.getItem() != null && edsAdjustmentItem.getItem().getTrackBatchesEnabled()) {
                itemBatchService.createBatchForStockAdjustment(stockAdjustment.getObjectID(), edsAdjustmentItem);
                itemBatchService.assignBatchForStockAdjustment(stockAdjustment.getObjectID(), edsAdjustmentItem);
            }
        }

        if (isNew) {
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                    BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, stockAdjustment, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_STOCK_ADJUSTMENT);
        } else {
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                    BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, stockAdjustment, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_STOCK_ADJUSTMENT);
        }

        /* Run workflow approval process */
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(),
                stockAdjustment, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_STOCK_ADJUSTMENT);


        //Register event in MyUpdate
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        if (adjustmentItem.getObjectID() != null) {
            baseEventPostProcessor.registerEvent(StockAdjustmentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, stockAdjustment, userManager.getUser());

            kpiLog.setEntityName(EdsStockAdjustment.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(stockAdjustment.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Update Stock adjustment");
        } else {
            baseEventPostProcessor.registerEvent(StockAdjustmentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, stockAdjustment, userManager.getUser());
            kpiLog.setEntityName(EdsStockAdjustment.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            kpiLog.setEntityId(stockAdjustment.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Add Stock adjustment");
        }

        result.setMessage(stockAdjustment.getNumber());
        result.setId(stockAdjustment.getObjectID());
        return result;
    }

    @Override
    public void updateStockAdjustmentStatus(final Integer objectId, final String statusCode, final String rejectionReason) {
        EdsStockAdjustment edsStockAdjustment = stockAdjustmentManager.get(objectId);
        if (edsStockAdjustment != null) {
            final EdsUser user = this.employeeManager.getUser();
            final EdsReference edsReference = this.referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, statusCode);

            if (!Constants.STOCK_ADJUSTMENT_APPROVED.equals(edsReference.getCode())) {
                edsStockAdjustment.setOverallStatus(edsReference);
            } else if (Constants.STOCK_ADJUSTMENT_APPROVED.equals(edsReference.getCode()) && edsStockAdjustment.getOverallStatus() != null
                    && Constants.STOCK_ADJUSTMENT_DRAFT.equals(edsStockAdjustment.getOverallStatus().getCode())) {
                edsStockAdjustment.setOverallStatus(this.referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, Constants.STOCK_ADJUSTMENT_SUBMITTED));
            }
            edsStockAdjustment.setUpdater(user);
            edsStockAdjustment.setLastUpdateTime();
            edsStockAdjustment.updateStatus(edsReference);
            if (Constants.STOCK_ADJUSTMENT_DECLINED.equals(statusCode)) {
                edsStockAdjustment.setOverallStatus(this.referenceManager.findReference(Constants.INVOICE_STATUS, Constants.REJECT));
            }

            stockAdjustmentManager.update(edsStockAdjustment);
            allInOneServiceLocal.approvedOrRejected(RelationItem.TYPE_STOCK_ADJUSTMENT, edsStockAdjustment.getObjectID(), null);
            EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), edsStockAdjustment, user);
            workflowEvent.setEntityType(RelationItem.TYPE_STOCK_ADJUSTMENT);

            if (Constants.STOCK_ADJUSTMENT_SUBMITTED.equals(statusCode)) {
                this.baseEventPostProcessor.registerEvent(StockAdjustmentEventListenerImpl.TYPE, StockAdjustmentEventListenerImpl.STOCK_ADJUSTMENT_SUBMITTED, edsStockAdjustment, user);
            } else if (Constants.STOCK_ADJUSTMENT_APPROVED.equals(statusCode)) {
                this.baseEventPostProcessor.registerEvent(StockAdjustmentEventListenerImpl.TYPE, StockAdjustmentEventListenerImpl.STOCK_ADJUSTMENT_APPROVED, edsStockAdjustment, user);
            } else if (Constants.STOCK_ADJUSTMENT_DECLINED.equals(statusCode)) {
                this.baseEventPostProcessor.registerEvent(StockAdjustmentEventListenerImpl.TYPE, StockAdjustmentEventListenerImpl.STOCK_ADJUSTMENT_DECLINED, edsStockAdjustment, user);
            }
        }
    }

    public void convertRFPstoPO(List<Integer> ids) {
        for (Integer objectID : ids) {
            quoteService.changeRFPstatus(objectID, CONVERTED, null, false);
        }
    }

    @Override
    public Integer saveStockTransfer(StockTransferItem stockTransferItem) {
        EdsUser user = userManager.getUser();

        if (stockTransferItem != null && stockTransferItem.getObjectId() == null) {
            if (stockTransferManager.numberExists(stockTransferItem.getNumber(), stockTransferItem.getObjectId())) {
                BankTransferNumberData numberDa = generateStockTransferNumberFormat();
                stockTransferItem.setNumber(numberDa.getTransferNumber());
                stockTransferItem.setIntNumber(Integer.parseInt(numberDa.getFourDigitNumber()));
            }
        }

        EdsStockTransfer stockTransfer;
        if (stockTransferItem.getObjectId() != null) {
            stockTransfer = stockTransferManager.get(stockTransferItem.getObjectId());
            itemBatchService.deleteBatchSerialsForTransfer(stockTransfer.getObjectID());
        } else {
            stockTransfer = new EdsStockTransfer();
        }
        stockTransfer.setTransferName(stockTransferItem.getTransferName());
        stockTransfer.setDate(stockTransferItem.getDate() != null ? stockTransferItem.getDate().getNonConvertedDate() : user.getUserDate(new Date()));
        stockTransfer.setNumber(stockTransferItem.getNumber());
        stockTransfer.setIntNumber(stockTransferItem.getIntNumber());
        if (!isOk(stockTransferItem.getApprovers())) {
            stockTransfer.setEntityStatus(referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, stockTransferItem.getStatusCode()));
        }
        boolean isNew = false;
        if (stockTransfer.getObjectID() == null) {
            isNew = true;
            stockTransfer.setCreator(user);
            stockTransferManager.create(stockTransfer);
        }

        if (isOk(stockTransferItem.getApprovers())) {
            stockTransferItem.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirstApprover = true;
            for (ApproverItemMini approverItem : stockTransferItem.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (stockTransfer.getCurrentApprover() != null && stockTransferItem.getStatusCode() != null && isFirstApprover) {
                        stockTransfer.getCurrentApprover().setStatus(referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, stockTransferItem.getStatusCode()));
                        stockTransfer.setEntityStatus(referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, Constants.STOCK_TRANSFER_SUBMITTED));
                        isFirstApprover = false;
                    } else if (stockTransfer.getCurrentApprover() != null && stockTransferItem.getStatusCode() != null) {
                        stockTransfer.getCurrentApprover().setStatus(referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, Constants.STOCK_TRANSFER_SUBMITTED));
                    }
                    if (stockTransferItem.getStatusCode() != null && !APPROVE.equals(stockTransferItem.getStatusCode())) {
                        stockTransfer.setEntityStatus(referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, stockTransferItem.getStatusCode()));
                    }
                    if (stockTransfer.isCurrentApproverRejected()) {
                        stockTransfer.setEntityStatus(stockTransfer.getCurrentApprover().getStatus());
                    }
                    continue;
                }
                EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(stockTransfer.getObjectID());
                edsApprover.setIs_default(false);

                if (stockTransferItem.getStatusCode() != null && isFirstApprover) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, stockTransferItem.getStatusCode()));
                    if (Constants.DRAFT.equals(stockTransferItem.getStatusCode())) {
                        stockTransfer.setEntityStatus(referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, stockTransferItem.getStatusCode()));
                    } else {
                        stockTransfer.setEntityStatus(referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, STOCK_TRANSFER_SUBMITTED));
                    }
                    isFirstApprover = false;
                } else if (stockTransferItem.getStatusCode() != null) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, STOCK_TRANSFER_SUBMITTED));
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
                    edsApprover.getApproverRoles().add(roleapp);
                }

                for (EdsApproverEmployees ucerapp : _edsApprover.getApproverEmployees()) {
                    edsApprover.getApproverEmployees().add(ucerapp);
                }

                if (stockTransfer.getCurrentApprover() == null) {
                    stockTransfer.setCurrentApprover(edsApprover);
                }
                stockTransfer.getApprovers().add(edsApprover);
            }
        }
        stockTransferManager.createOrUpdate(stockTransfer);

        if (stockTransferItem.getAttachments() != null && stockTransferItem.getAttachments().length > 0 && stockTransfer.getObjectID() != null) {
            attachmentUtilsManager.saveAttachments(F_STOCK_TRANSFER, stockTransfer.getObjectID(), stockTransfer.getObjectID(), stockTransferItem.getAttachments());
        }
        createStockTransferNote(stockTransferItem.getHistoryList(), stockTransfer);

        HashMap<Integer, EdsStockAdjustment> adjustmentsToRemove = new HashMap<>();
        if (stockTransfer.getItems() != null && !stockTransfer.getItems().isEmpty()) {
            for (EdsStockAdjustment edsStockAdjustment : stockTransfer.getItems()) {
                adjustmentsToRemove.put(edsStockAdjustment.getObjectID(), edsStockAdjustment);
            }
            stockTransfer.getItems().clear();
        }

        for (AdjustmentItem adjustmentItem : stockTransferItem.getAdjustmentItemList()) {
            EdsStockAdjustment edsStockAdjustment;
            if (adjustmentItem.getObjectID() != null) {
                edsStockAdjustment = stockAdjustmentManager.get(adjustmentItem.getObjectID());
            } else {
                edsStockAdjustment = new EdsStockAdjustment();
            }
            edsStockAdjustment.setNumber(adjustmentItem.getNumber());
            edsStockAdjustment.setDate(adjustmentItem.getDate() != null ? adjustmentItem.getDate().getNonConvertedDate() : new Date());
            if (adjustmentItem.getAccount() != null && adjustmentItem.getAccount().getId() != null) {
                edsStockAdjustment.setAccount(accountingManager.get(adjustmentItem.getAccount().getId()));
            }
            edsStockAdjustment.setMemo(adjustmentItem.getMemo());

            if (adjustmentItem.isStockTransfer()) {
                edsStockAdjustment.setStockTransfer(true);
                edsStockAdjustment.setDate(stockTransfer.getDate());
                edsStockAdjustment.setFromWarehouse(warehouseManager.get(adjustmentItem.getFromWarehouseID()));
                edsStockAdjustment.setToWarehouse(warehouseManager.get(adjustmentItem.getToWarehouseID()));
                EdsItem productAssetAccount = itemManager.getItem(adjustmentItem.getProductItems()[0].getObjectId());
                edsStockAdjustment.setFromAccount(productAssetAccount.getAssetAccount());
                edsStockAdjustment.setToAccount(productAssetAccount.getAssetAccount());
            }
            stockTransfer.getItems().add(edsStockAdjustment);
            stockAdjustmentManager.createOrUpdate(edsStockAdjustment);

            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            boolean isMultiWarehouseEnabled = financialSettings.getEnableMultiWarehouse();
            EdsWarehouse defaultWarehouse = warehouseManager.getDefaultWarehouse();
            edsStockAdjustment.getAdjustmentItemList().clear();

            if (adjustmentItem.getProductItems() != null) {
                for (ProductItem item : adjustmentItem.getProductItems()) {
                    EdsAdjustmentItem edsAdjustmentItem;

                    if (item.getLineItemID() != null) {
                        edsAdjustmentItem = stockAdjustmentItemManager.get(item.getLineItemID());
                    } else {
                        edsAdjustmentItem = new EdsAdjustmentItem();
                    }
                    edsAdjustmentItem.setAdjustment(edsStockAdjustment);
                    EdsItem edsItem = itemManager.get(item.getObjectId());
                    edsAdjustmentItem.setItem(edsItem);
                    edsAdjustmentItem.setWarehouse(isMultiWarehouseEnabled ? warehouseManager.get(item.getWarehouseId()) : defaultWarehouse);

                    if (item.getAccountID() != null) {
                        edsAdjustmentItem.setAccount(accountingManager.get(item.getAccountID()));
                    } else {
                        edsAdjustmentItem.setAccount(edsStockAdjustment.getAccount());
                    }
                    if (item.getCurrentQty() != null) {
                        edsAdjustmentItem.setCurrentQty(item.getCurrentQty());
                    }
                    edsAdjustmentItem.setUsedQty(item.getUsedQty());
                    edsAdjustmentItem.setNewQty(item.getNewQty());
                    if (item.getTotalQty() != null) {
                        edsAdjustmentItem.setQty(item.getTotalQty());
                    }
                    edsAdjustmentItem.setPrice(item.getUnitpPrice());
                    if (item.getProjectID() != null) {
                        edsAdjustmentItem.setProject(projectManager.get(item.getProjectID()));
                    }
                    if (item.getUnitMeasurementId() != null) {
                        edsAdjustmentItem.setMeasurement(unitMeasurementManager.get(item.getUnitMeasurementId()));
                    }
                    edsStockAdjustment.getAdjustmentItemList().add(edsAdjustmentItem);
                    if (edsItem.getTrackBatchesEnabled()) {
                        edsAdjustmentItem.setBatchItems(item.getBatchItems());
                    }
                }
            }
            adjustmentsToRemove.remove(edsStockAdjustment.getObjectID());
        }

        if (STOCK_TRANSFER_TRANSFERRED.equals(stockTransferItem.getStatusCode())) {
            accountingServiceLocal.createTransactionForStockTransfer(stockTransfer);
            baseEventPostProcessor.registerEvent(StockTransferEventListenerImpl.TYPE, StockTransferEventListenerImpl.STOCK_TRANSFER_TRANSFERRED, stockTransfer, user);
        }

        for (EdsStockAdjustment adjustment : stockTransfer.getItems()) {
            int i = 0;
            for (EdsAdjustmentItem edsAdjustmentItem : adjustment.getAdjustmentItemList()) {
                EdsItem edsItem = edsAdjustmentItem.getItem();
                if (edsItem.getTrackBatchesEnabled()) {
                    if (i == 0) { //from warehouse
                        itemBatchService.assignBatchForStockTransferOut(stockTransfer.getObjectID(), edsAdjustmentItem, stockTransferItem.getStatusCode());
                    } else if (i == 1) {//to warehouse
                        itemBatchService.createBatchForStockTransferIn(stockTransfer.getObjectID(), edsAdjustmentItem, stockTransferItem.getStatusCode());
                    }
                }
                i++;
            }
        }
        for (Integer key : adjustmentsToRemove.keySet()) {
            EdsStockAdjustment edsStockAdjustment = adjustmentsToRemove.get(key);
            edsStockAdjustment.getAdjustmentItemList().clear();
            EdsTransaction edsTransaction = transactionManager.getTransactionByStockAdjustment(edsStockAdjustment);
            if (edsTransaction != null) {
                itemStockManager.deleteItemStocksByTransaction(edsTransaction.getObjectID());
                edsTransaction.setDeleted(true);
            }
            edsStockAdjustment.setDeleted(true);
            stockAdjustmentManager.delete(edsStockAdjustment);
        }
        if (isNew) {
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                    BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, stockTransfer, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_STOCK_TRANSFER);
        } else {
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                    BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, stockTransfer, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_STOCK_TRANSFER);
        }

        /* Run workflow approval process */
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(),
                stockTransfer, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_STOCK_TRANSFER);


        //Register event in MyUpdate
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        if (stockTransferItem.getObjectId() != null) {
            baseEventPostProcessor.registerEvent(StockTransferEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, stockTransfer, user);

            kpiLog.setEntityName(EdsStockTransfer.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(stockTransfer.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Update Stock transfer");
        } else {
            baseEventPostProcessor.registerEvent(StockTransferEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, stockTransfer, user);
            kpiLog.setEntityName(EdsStockTransfer.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            kpiLog.setEntityId(stockTransfer.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Add Stock transfer");
        }

        //add qilganda oldinroq ishlab ketayapti: avval Submitted keyin Added bolib qolayapti

        if (stockTransfer != null) {
            return stockTransfer.getObjectID();
        } else
            return null;
    }

    private void createStockTransferNote(HistoryListItem[] noteItems, EdsStockTransfer stockTransfer) {
        List<EdsStockTransferNote> stNotes = stockTransferNoteManager.getStockTransferNotes(stockTransfer.getObjectID());
        if (noteItems != null && noteItems.length > 0) {
            HashMap<Integer, Integer> existingNotesMap = new HashMap<>();
            for (HistoryListItem noteItem : noteItems) {
                if (noteItem.getObjectID() == null && noteItem.getComment() != null && !"".equals(noteItem.getComment())) {
                    EdsStockTransferNote note = new EdsStockTransferNote();
                    note.setComment(noteItem.getComment());
                    note.setCommentator(invoiceManager.getUser());
                    note.setDate(new Date());
                    note.setSuperUser(ServerUtils.isSuperUser());
                    stockTransferNoteManager.create(note);
                }
                if (noteItem.getObjectID() != null) {
                    existingNotesMap.put(noteItem.getObjectID(), noteItem.getObjectID());
                }
            }

            for (EdsStockTransferNote quoteNote : stNotes) {
                if (!existingNotesMap.containsKey(quoteNote.getObjectID())) {
                    stockTransferNoteManager.delete(quoteNote);
                }
            }
        } else {
            for (EdsStockTransferNote noteForDelete : stNotes) {
                stockTransferNoteManager.delete(noteForDelete);
            }
        }
    }

    private void createStockAdjustmentNote(HistoryListItem[] noteItems, EdsStockAdjustment stockAdjustment) {
        List<EdsStockAdjustmentNote> stNotes = stockAdjustmentNoteManager.getStockAdjustmentNotes(stockAdjustment.getObjectID());
        if (noteItems != null && noteItems.length > 0) {
            HashMap<Integer, Integer> existingNotesMap = new HashMap<>();
            for (HistoryListItem noteItem : noteItems) {
                if (noteItem.getObjectID() == null && noteItem.getComment() != null && !"".equals(noteItem.getComment())) {
                    EdsStockAdjustmentNote note = new EdsStockAdjustmentNote();
                    note.setComment(noteItem.getComment());
                    note.setCommentator(invoiceManager.getUser());
                    note.setDate(new Date());
                    note.setSuperUser(ServerUtils.isSuperUser());
                    stockAdjustmentNoteManager.create(note);
                }
                if (noteItem.getObjectID() != null) {
                    existingNotesMap.put(noteItem.getObjectID(), noteItem.getObjectID());
                }
            }

            for (EdsStockAdjustmentNote quoteNote : stNotes) {
                if (!existingNotesMap.containsKey(quoteNote.getObjectID())) {
                    stockAdjustmentNoteManager.delete(quoteNote);
                }
            }
        } else {
            for (EdsStockAdjustmentNote noteForDelete : stNotes) {
                stockAdjustmentNoteManager.delete(noteForDelete);
            }
        }
    }

    @Override
    public ListResult<StockTransferItem> getStockTransferList(ListingFilterParameter fp) {
        ArrayList<EdsStockTransfer> stockTransferList = stockTransferManager.getList(fp);
        ArrayList<StockTransferItem> stockTransferItems = new ArrayList<>(stockTransferList.size());
        for (EdsStockTransfer item : stockTransferList) {
            StockTransferItem stockTransferItem = new StockTransferItem();
            EdsStockTransfer stockTransfer = stockTransferManager.get(item.getObjectID());
            String statusCode = stockTransfer != null && stockTransfer.getOverallStatus() != null ? stockTransfer.getOverallStatus().getCode() : null;
            stockTransferItem.setStatusCode(statusCode);
            stockTransferItem.setObjectId(item.getObjectID());
            stockTransferItem.setTransferName(item.getTransferName());
            stockTransferItem.setNumber(item.getNumber());
            if (item.getCurrentApprover() != null && item.getCurrentApprover().getExactEmployee() != null) {
                if (item.getCurrentApprover().getExactEmployee().isEmployee()) {
                    EdsEmployee edsEmployee = item.getCurrentApprover().getExactEmployee().getEmployee();
                    if (edsEmployee.getProfile() != null && edsEmployee.getProfile().getEmployeeCode() != null) {
                        stockTransferItem.setApprover(new SelectItem(edsEmployee.getObjectID(), edsEmployee.getProfile().getEmployeeCode() + " - " + edsEmployee.getFullName()));
                    } else {
                        stockTransferItem.setApprover(item.getCurrentApprover().getExactEmployee().getAsSelectItem());
                    }
                } else {
                    stockTransferItem.setApprover(item.getCurrentApprover().getExactEmployee().getAsSelectItem());
                }
            }
            stockTransferItem.setDate(new DateNonConvertable(item.getDate()));
            stockTransferItems.add(stockTransferItem);
        }
        Integer totalCount = stockTransferManager.getTotalCount(fp);
        return new ListResult<>(stockTransferItems, totalCount);
    }

    @Override
    @Transactional(readOnly = true)
    public StockTransferItem getStockTransfer(Integer objectID) {
        StockTransferItem result = new StockTransferItem();
        if (objectID != null) {
            EdsStockTransfer edsStockTransfer = stockTransferManager.get(objectID);
            result = edsStockTransfer.getAsRPC();
            if (!result.getAdjustmentItemList().isEmpty()) {
                for (AdjustmentItem item : result.getAdjustmentItemList()) {
                    for (ProductItem productItem : item.getProductItems()) {
                        if (productItem.getTrackBatchesEnabled()) {
                            productItem.setBatchItems(itemBatchService.getBatchItems(
                                    productItem.getLineItemID(),
                                    item.getProduct().getId(),
                                    objectID,
                                    ItemSerialEntityType.STOCK_TRANSFER_OUT.name()));
                        }

                    }
                }
            }
            if (edsStockTransfer.getCurrentApprover() != null && edsStockTransfer.getCurrentApprover().getExactEmployee() != null) {
                if (edsStockTransfer.getCurrentApprover().getExactEmployee().isEmployee()) {
                    EdsEmployee edsEmployee = edsStockTransfer.getCurrentApprover().getExactEmployee().getEmployee();
                    if (edsEmployee.getProfile() != null && edsEmployee.getProfile().getEmployeeCode() != null) {
                        result.setApprover(new SelectItem(edsEmployee.getObjectID(), edsEmployee.getProfile().getEmployeeCode() + " - " + edsEmployee.getFullName()));
                    } else {
                        result.setApprover(edsStockTransfer.getCurrentApprover().getExactEmployee().getAsSelectItem());
                    }
                } else {
                    result.setApprover(edsStockTransfer.getCurrentApprover().getExactEmployee().getAsSelectItem());
                }
            }
            result.setApproverSaved(approverManager.isExistApproverByEntityTypeAndEntityId(RelationItem.TYPE_STOCK_TRANSFER, edsStockTransfer.getObjectID()));
        } else {
            BankTransferNumberData btnd = generateStockTransferNumberFormat();
            result.setNumber(btnd.getTransferNumber());
            result.setIntNumber(Integer.parseInt(btnd.getFourDigitNumber()));
        }
        result.setLayoutHtml(PathFinder.getLayoutHTML(LayoutRPC.STOCK_TRANSFER_FORM));
        result.setCurrentUserId(userManager.getUser().getObjectID());
        result.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_STOCK_TRANSFER));
        return result;
    }

    private BankTransferNumberData generateStockTransferNumberFormat() {
        BankTransferNumberData transferNumberData = new BankTransferNumberData();
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer fourDigitNumber = stockTransferManager.getStockTranferNumberInt();
        String format = null;
        if (settings != null) {
            format = settings.getStockTransferNumberingFormat();
        }
        if (format != null) {
            parseNumber(format, transferNumberData, fourDigitNumber);
        } else {
            String prefix = EdsNumberingSettings.DEF_STOCK_TRANSFER;
            NumberData numberData = EdsNumberingSettings.getDefaultData(fourDigitNumber, prefix);
            String[] numberParts = numberData.getNumberFormat().split("_");
            transferNumberData.setPrefix(numberParts[0]);
            transferNumberData.setFourDigitNumber(String.valueOf(numberParts[1]));
            transferNumberData.setWithDate(numberParts[1].split("-").length == 2);
        }
        return transferNumberData;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public StockTransferItem getStockTransferSummaryData(Integer objectID) {
        StockTransferItem stockTransferItem = new StockTransferItem();
        if (objectID != null) {
            EdsStockTransfer stockTransfer = stockTransferManager.get(objectID);
            if (stockTransfer != null && !stockTransfer.getDeleted()) {
                if (stockTransfer.getOverallStatus() != null) {
                    stockTransferItem.setStatusCode(stockTransfer.getOverallStatus().getCode());
                }
                if (stockTransfer.getCurrentApprover() != null && stockTransfer.getCurrentApprover().getExactEmployee() != null) {
                    if (stockTransfer.getCurrentApprover().getExactEmployee().isEmployee()) {
                        EdsEmployee edsEmployee = stockTransfer.getCurrentApprover().getExactEmployee().getEmployee();
                        if (edsEmployee.getProfile() != null && edsEmployee.getProfile().getEmployeeCode() != null) {
                            stockTransferItem.setApprover(new SelectItem(edsEmployee.getObjectID(), edsEmployee.getProfile().getEmployeeCode() + " - " + edsEmployee.getFullName()));
                        } else {
                            stockTransferItem.setApprover(stockTransfer.getCurrentApprover().getExactEmployee().getAsSelectItem());
                        }
                    } else {
                        stockTransferItem.setApprover(stockTransfer.getCurrentApprover().getExactEmployee().getAsSelectItem());
                    }
                }
                stockTransferItem.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_STOCK_TRANSFER));
                stockTransferItem.setTransferName(stockTransfer.getTransferName());
                stockTransferItem.setNumber(stockTransfer.getNumber());
                stockTransferItem.setIntNumber(stockTransfer.getIntNumber());
                stockTransferItem.setObjectId(stockTransfer.getObjectID());
                stockTransferItem.setDate(new DateNonConvertable(stockTransfer.getDate()));
                stockTransferItem.setLayoutHtml(PathFinder.getLayout(LayoutRPC.STOCK_TRANSFER_FORM, "viewForm").getLayout());
                ArrayList<AdjustmentItem> adjustmentItemList = new ArrayList<>();
                List<EdsStockAdjustment> stockAdjustmentList = stockAdjustmentManager.getStockAdjustmentsByStockTransfer(stockTransfer.getObjectID());
                if (stockAdjustmentList != null && !stockAdjustmentList.isEmpty()) {
                    for (EdsStockAdjustment stockAdjustment : stockAdjustmentList) {
                        if (stockAdjustment.getAdjustmentItemList() != null && !stockAdjustment.getAdjustmentItemList().isEmpty()) {
                            boolean isFrom = true;
                            for (EdsAdjustmentItem adjustmentItem : stockAdjustment.getAdjustmentItemList()) {
                                AdjustmentItem item = new AdjustmentItem();
                                ProductItem[] productItems = new ProductItem[1];
                                if (isFrom) {
                                    productItems[0] = new ProductItem();
                                    productItems[0].setObjectId(adjustmentItem.getItem().getObjectID());
                                    productItems[0].setName(adjustmentItem.getItem().getName());
                                    productItems[0].setProductNumber(adjustmentItem.getItem().getProductNumber());
                                    productItems[0].setFromWarehouseName(adjustmentItem.getWarehouse().getName());
                                    productItems[0].setFromWarehouseId(adjustmentItem.getWarehouse().getObjectID());
                                    productItems[0].setQty(adjustmentItem.getUsedQty());
                                    if (adjustmentItem.getMeasurement() != null && adjustmentItem.getMeasurement().getName() != null) {
                                        productItems[0].setUnitMeasurementName(adjustmentItem.getMeasurement().getName());
                                    }
                                    if (adjustmentItem.getItem().getTrackBatchesEnabled()) {
                                        productItems[0].setTrackBatchesEnabled(true);
                                        productItems[0].setAssignedBatchItems(itemBatchService.getBatchItems(
                                                adjustmentItem.getObjectID(),
                                                adjustmentItem.getItem().getObjectID(),
                                                objectID,
                                                ItemSerialEntityType.STOCK_TRANSFER_OUT.name()));
                                    }
                                    item.setProductItems(productItems);
                                    isFrom = false;
                                } else {
                                    productItems[0] = new ProductItem();
                                    productItems[0].setObjectId(adjustmentItem.getItem().getObjectID());
                                    productItems[0].setName(adjustmentItem.getItem().getName());
                                    productItems[0].setProductNumber(adjustmentItem.getItem().getProductNumber());
                                    productItems[0].setToWarehouseName(adjustmentItem.getWarehouse().getName());
                                    productItems[0].setQty(adjustmentItem.getNewQty());
                                    if (adjustmentItem.getMeasurement() != null && adjustmentItem.getMeasurement().getName() != null) {
                                        productItems[0].setUnitMeasurementName(adjustmentItem.getMeasurement().getName());
                                    }
                                    if (adjustmentItem.getItem().getTrackBatchesEnabled()) {
                                        productItems[0].setTrackBatchesEnabled(true);
                                        productItems[0].setBatchItems(itemBatchService.getBatchItems(
                                                adjustmentItem.getObjectID(),
                                                adjustmentItem.getItem().getObjectID(),
                                                objectID,
                                                ItemSerialEntityType.STOCK_TRANSFER_IN.name()));
                                    }
                                    item.setProductItems(productItems);
                                }
                                adjustmentItemList.add(item);
                            }
                        }
                    }
                    stockTransferItem.setAdjustmentItemList(adjustmentItemList);
                }
                FileResource[] attachmentResources = accountingServiceLocal.getAttachmentResources(F_STOCK_TRANSFER, stockTransfer.getObjectID());
                if (attachmentResources != null && attachmentResources.length > 0) {
                    stockTransferItem.setAttachmentResources(attachmentResources);
                } else {
                    stockTransferItem.setAttachmentResources(new FileResource[0]);
                }
                stockTransferItem.setTemplates(invoiceServiceLocal.getCompanyPdfTemplates(STOCK_TRANSFER).getItems());
                EdsCompanyPdfTemplate template = companyPdfTemplateManager.getDefaultCompanyPdfTemplateByType(STOCK_TRANSFER);
                if (template != null) {
                    stockTransferItem.setSelectedTemplateId(template.getObjectID());
                }
            }
            stockTransferItem.setCurrentUserId(userManager.getUser().getObjectID());
        }
        return stockTransferItem;
    }

    @Override
    public boolean enableComission() {
        return genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.INVENTORY_COMISSION_ENABLED);
    }


    public boolean deleteProduct(Integer productId) {
        boolean isOpeningBalance = false;
        EdsItem item = itemManager.get(productId);
        if (item != null && item.isUsedInItems()) {
            return false;
        }

        boolean result = deleteProductChilds(productId);
        if (!result) {
            return false;
        }

        result = assemblyItemManager.isUsedInAssemblyItems(productId);

        if (result) {
            return false;
        }

        result = productKitItemManager.isUsedInProductKit(productId);
        if (result) {
            return false;
        }

        result = stockAdjustmentItemManager.isUsedInStockAdjustment(productId);
        if (result) {
            return false;
        }

        List<EdsInventoryTransaction> inventoryTransactions = transactionManager.getInventoryTransactions(productId);
        if (inventoryTransactions != null && inventoryTransactions.size() > 1) {
            return false;
        } else if (inventoryTransactions != null && inventoryTransactions.size() == 1) {
            EdsInventoryTransaction transaction = inventoryTransactions.get(0);
            if (transaction.getTransactionItems() != null) {
                for (EdsTransactionItem transactionItem : transaction.getTransactionItems()) {
                    if (transactionItem.getAccount() != null && Integer.valueOf(EdsAccount.OPENING_BALANCE).equals(transactionItem.getAccount().getKey())) {
                        if (BigDecimal.ZERO.compareTo(transactionItem.getCredit()) <= 0) {
                            isOpeningBalance = true;
                            break;
                        }
                    }
                }
                if (!isOpeningBalance) {
                    return false;
                }
            }
        }

        if (ASSEMBLY_ITEM.equals(item.getType())) {
            itemStockManager.unBuildAssemblyItemStocks(productId);
        }
        if (PRODUCT_KIT.equals(item.getType())) {
            productKitItemManager.deleteProductKitItems(productId);
        }
        itemStockManager.deleteItemStocksByProduct(productId);
        productWarehouseLocationManager.deleteProductLocations(productId);
        transactionManager.deleteInventoryTransaction(productId);
        itemManager.deleteProductVariationCombinate(productId);
        itemSerialService.deleteSerials(productId);

        item.setDeleted(true);
        item.setLastUpdateTime(new Date());
        item.setUpdater(userManager.getUser());
        itemManager.update(item);

        if (item.getParent() != null) {
            List<EdsItem> child = itemManager.getChildProducts(item.getParent().getObjectID());
            if (child == null || child.isEmpty()) {
                EdsItem parent = itemManager.get(item.getParent().getObjectID());
                parent.setHasVariations(false);
                itemManager.update(parent);
            }
        }
        baseEventPostProcessor.registerEvent(ProductEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, item, itemManager.getUser());

        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, item, itemManager.getUser());
        workflowEvent.setEntityType(item.getRentalItem() != null && item.getRentalItem() ? RelationItem.TYPE_RENTAL_ORDER : RelationItem.TYPE_PRODUCT);

        try {
            solrManager.removeProductsServicesByIds(productId);
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }
        //itemManager.delete(itemManager.get(productId));
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsItem.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(productId);
        ServerUtils.kpiLog(log, kpiLog, "Delete product");
        return true;
    }

    public boolean deleteProductChilds(Integer productId) {
        boolean result = true;
        List<EdsItem> items = itemManager.getChildProducts(productId);
        if (items != null && !items.isEmpty()) {
            for (EdsItem item : items) {
                result = deleteProduct(item.getObjectID());

                if (!result) {
                    return false;
                }
            }
            EdsItem parent = itemManager.get(productId);
            parent.setHasVariations(false);
            itemManager.deleteProductVariations(parent.getObjectID());
            itemManager.update(parent);
        }

        return result;
    }

    public ProductCommentList getProductCommentList(Integer productId) {
        ProductCommentList commentList = new ProductCommentList();
        commentList.setObjectId(productId);
        commentList.setUserId(itemManager.getUser().getObjectID());
        commentList.setUserFullName(itemManager.getUser().getFullName());
        List<EdsItemComment> comments = itemCommentManager.getComments(productId);
        ProductCommentItem[] items = new ProductCommentItem[comments.size()];
        int i = 0;
        for (EdsItemComment comment : comments) {
            items[i] = new ProductCommentItem();
            items[i].setObjectId(comment.getObjectID());
            items[i].setProductId(comment.getItem().getObjectID());
            items[i].setText(comment.getComment());
            items[i].setDate(comment.getDate());
            items[i].setUserId(comment.getUser().getObjectID());
            items[i].setUserFullName(comment.getUser().getFullName());
            items[i].setUserPictureUrl(commonService.getEmployeeImageURL(comment.getUser().getObjectID()));
            i++;
        }
        commentList.setItems(items);
        return commentList;
    }

    @Override
    public Integer saveProductComment(ProductCommentItem productCommentItem) {
        EdsItemComment itemComment = new EdsItemComment();
        itemComment.setItem(itemManager.get(productCommentItem.getProductId()));
        itemComment.setComment(productCommentItem.getText());
        itemComment.setDate(productCommentItem.getDate());
        itemComment.setUser(itemManager.getUser());
        itemCommentManager.create(itemComment);
        return itemComment.getObjectID();
    }

    @Override
    public void updateProductComment(ProductCommentItem productCommentItem) {
        EdsItemComment itemComment = itemCommentManager.get(productCommentItem.getObjectId());
        itemComment.setComment(productCommentItem.getText());
        itemCommentManager.update(itemComment);
    }

    @Override
    public NewProduct getProductWithLocale(Integer objectID) {
        NewProduct result;
        EdsCustomFields productCustomField, categoryCustomField;
        result = new NewProduct();
        EdsItem parent = itemManager.get(objectID);
        result.setObjectId(objectID);
        result.setDescription(parent.getDescription());
        result.setItemName(parent.getName());
        productCustomField = new EdsCustomFields();
        categoryCustomField = new EdsCustomFields();
        ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.ProductServiceView);
        result.setProductCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(productCustomField, customFieldItems));
        ArrayList<CompanyCustomFieldItem> categoryCustomFieldItems = commonService.getCompanyCustomFields(ViewName.ProductCategory);
        result.setCategoryCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(categoryCustomField, categoryCustomFieldItems));
        List<EdsLocale> edsLocaleList = localeManager.list();
        SelectItem[] items = new SelectItem[edsLocaleList.size()];
        int i = 0;
        for (EdsLocale edsLocale : edsLocaleList) {
            items[i++] = new SelectItem(edsLocale.getId(), edsLocale.getCountry(), edsLocale.getLanguageCode());
        }
        result.setLocaleList(items);
        return result;
    }

    @Override
    public void saveProductLocalization(NewProduct product) {
        EdsItem item;
        if (product.getSubItemID() != null) {
            item = itemManager.get(product.getSubItemID());
        } else {
            item = new EdsItem();
        }
        EdsLocale locale = localeManager.get(product.getLocaleID());
        if (locale != null) {
            item.setItemLocale(locale);
        }
        EdsItem parent = itemManager.get(product.getObjectId());
        item.setLocaleParent(parent);
        item.setDescription(product.getDescription());
        item.setName(product.getItemName());
        EdsItemCustomFields customFields = createProductCustomFields(product.getProductCustomFieldItems(), product.getObjectId() != null, parent);
        item.setCustomFields(customFields);
        itemManager.createOrUpdate(item);
    }

    @Override
    public NewProduct getSubLocaleProduct(Integer parentID, Integer localeID) {
        NewProduct result = null;
        EdsItem subItem = itemManager.getSubLocaleItem(parentID, localeID);
        if (subItem != null) {
            result = new NewProduct();
            result.setObjectId(parentID);
            result.setSubItemID(subItem.getObjectID());
            result.setItemName(subItem.getName());
            result.setDescription(subItem.getDescription());
            result.setLocaleID(localeID);
            EdsItemCustomFields customFields = subItem.getCustomFields();
            ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.ProductServiceView);
            result.setProductCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(customFields, customFieldItems));
        }
        return result;
    }

    @Override
    public void deleteProductComment(Integer objectId) {
        itemCommentManager.delete(itemCommentManager.get(objectId));
    }

    @Override
    public NumberData generateProductNumber() {
        Integer intNumber = accountingManager.getProductLastIntNumber();
        return generateProductNumber(intNumber);
    }

    @Override
    public NumberData generateProductNumber(Integer intNumber) {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        if (settings != null && settings.getProductNumberingFormat() != null) {
            return settings.parseNumberData(intNumber, settings.getProductNumberingFormat());
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_PROD_PREFIX);
        }
    }

    @Override
    public boolean[] validateProduct(ListingFilterParameter filterParametrs) {
        boolean[] result = new boolean[4];

        if (filterParametrs.isCheckNumber()) {
            result[0] = accountingManager.isProductNumberExists(filterParametrs.getNumber(), filterParametrs.getCaseID());
        }
        if (filterParametrs.isNewType() && filterParametrs.getCaseID() != null) {
            result[1] = deleteProduct(filterParametrs.getCaseID());
        }
        if (filterParametrs.getColumn() != null && !"".equals(filterParametrs.getColumn())) {
            result[2] = accountingManager.isProductUpcNumberExists(filterParametrs.getColumn(), filterParametrs.getCaseID());
        }
        result[3] = accountingManager.isProductNameExists(filterParametrs.getName().trim(), filterParametrs.getCaseID());
        return result;
    }



    @Override
    public void updateInventoryItemsAfterExportSaasu(Integer objectId, Date lastUpdateDate, String saasuLastUpdatedUid, Integer saasuGUID) {
        EdsItem item = itemManager.getItem(objectId);
        if (item != null) {
            if (saasuGUID != null) {
                item.setSaasuGUID(saasuGUID.toString());
            }
            item.setSasuuLastUpdatedDate(lastUpdateDate);
            item.setSaasuLastUpdatedUid(saasuLastUpdatedUid);
            itemManager.update(item);
        }
    }

    @Transactional
    public EdsItemCustomFields createProductCustomFields(List<CompanyCustomFieldItem> customFieldItems, boolean editForm, EdsItem edsItem) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            EdsItemCustomFields productCustomFields = null;
            if (customFieldItems.get(0).getObjectId() != null) {
                productCustomFields = itemCFManager.get(customFieldItems.get(0).getObjectId());
                if (productCustomFields == null) {
                    productCustomFields = new EdsItemCustomFields();
                    itemCFManager.create(productCustomFields);
                }
            } else {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null
                            || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty())) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                productCustomFields = new EdsItemCustomFields();
                itemCFManager.create(productCustomFields);
            }

            if (editForm && productCustomFields != null) {
                for (CompanyCustomFieldItem cfItem : customFieldItems) {
                    if (DATA_TYPE_TEXT.equals(cfItem.getDataType())) {
                        String oldString = productCustomFields.getStringValue(cfItem.getColumnCode()) != null ? productCustomFields.getStringValue(cfItem.getColumnCode()) : "";
                        if (!oldString.equals(cfItem.getFieldStringValue())) {
                            edsItem.addHistoryChange(cfItem.getFieldName(), oldString, cfItem.getFieldStringValue(), oldString == null ? userManager.getUser() : edsItem.getCreator());
                        }
                    } else if (DATA_TYPE_NUMBER.equals(cfItem.getDataType())) {
                        Double oldNumber = productCustomFields.getDoubleValue(cfItem.getColumnCode());
                        String oldNumberString = oldNumber != null ? String.valueOf(oldNumber) : "";
                        if (cfItem != null && cfItem.getFieldStringValue() != null) {
                            String newNumber = cfItem.getFieldStringValue().isEmpty() ? "" : String.valueOf(Double.valueOf(cfItem.getFieldStringValue()));
                            if (!oldNumberString.equals(newNumber)) {
                                edsItem.addHistoryChange(cfItem.getFieldName(), oldNumberString, cfItem.getFieldStringValue(), oldNumber == null ? userManager.getUser() : edsItem.getCreator());
                            }
                        }
                    } else if (DATA_TYPE_DATE.equals(cfItem.getDataType())) {
                        Date oldDate = productCustomFields.getDateValue(cfItem.getColumnCode());
                        DateNonConvertable cnc = cfItem.getFieldDateNonConvertedValue();
                        Date newDate = cnc != null ? cnc.getNonConvertedDate() : null;
                        if ((oldDate != null && newDate != null && (oldDate.after(newDate) || oldDate.before(newDate))) ||
                                (oldDate != null && newDate == null) || (oldDate == null && newDate != null)) {
                            edsItem.addHistoryChange(cfItem.getFieldName(), oldDate, newDate, oldDate == null ? userManager.getUser() : edsItem.getCreator());
                        }
                    }
                }

            }
            CustomFieldsUtils.setDomenObjectCustomFields(productCustomFields, customFieldItems);
            return productCustomFields;
        }
        return null;
    }

    private TaxList getCompanyTaxList(ListingFilterParameter filterParametrs) {
        List<EdsVat> taxList = accountingServiceLocal.companyVatList(filterParametrs, null);
        return accountingServiceLocal.createCompanyTaxList(taxList);
    }

    private ProductKitItem[] wrapProductKitItems(List<EdsProductKitItems> productKitItems) {
        if (productKitItems != null && !productKitItems.isEmpty()) {
            ProductKitItem[] kitItems = new ProductKitItem[productKitItems.size()];

            int i = 0;
            for (EdsProductKitItems productKitItem : productKitItems) {
                kitItems[i] = new ProductKitItem();

                EdsItem productItem = productKitItem.getItem();
                kitItems[i].setProductItem(productItem.getAsProductSelectItem());
                kitItems[i].setQuantity(productKitItem.getQuantity());

                BigDecimal sellingPrice = productItem.getSellingPrice() != null ? productItem.getSellingPrice().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                BigDecimal costPrice = productItem.getUnitPrice() != null ? productItem.getUnitPrice().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                kitItems[i].setPrice(sellingPrice.toString());
                kitItems[i].setCost(costPrice.toString());

                BigDecimal tax = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

                if (productItem.getVat() != null) {
                    tax = BigDecimal.valueOf(productItem.getVat().getEffectiveTaxRate()).setScale(2, RoundingMode.HALF_UP);
                }

                kitItems[i].setTax(tax.toString());

                BigDecimal net = sellingPrice.multiply(productKitItem.getQuantity()).setScale(2, RoundingMode.HALF_UP);
                //BigDecimal taxAmount = net.multiply(tax.divide(HUNDRED, 4, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);

                kitItems[i].setSubtotal(net.toString()); //sellingPrice.multiply(new BigDecimal(productKitItem.getQuantity())).add(taxAmount).toString()
                if (productKitItem.getWarehouse() != null) {
                    kitItems[i].setWarehouse(productKitItem.getWarehouse().getAsSelectItem());
                }
                i++;
            }

            return kitItems;
        }

        return null;
    }

    private void applyStorefrontOptions(NewProduct product, EdsItem item) {
        //Storefront options
        item.setFeatured(product.getFeatured());
        item.setSpecial(product.getSpecial());
        item.setShowOnHomePage(product.getShowOnHomepage());
        item.setVirtual(product.getVirtual());
        item.setFreeShipping(product.getFreeShipping());
        item.setCondition(product.getCondition());
        itemManager.update(item);
    }


    private void updateInventoryStockPerWarehouse(NewProduct product, EdsItem item, Integer transactionID) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = financialSettings.getAccountingCalculationScale();
        EdsWarehouse defaultWarehouse = warehouseManager.getDefaultWarehouse();
        boolean isMultiWarehouseEnabled = financialSettings.getEnableMultiWarehouse();

        if (transactionID != null) {
            itemStockManager.deleteByTransaction(transactionID, item.getObjectID());
        }

        if (product.getProductLocations() != null) {
            ProductLocationItem[] productLocations = product.getProductLocations();
            for (int i = 0; i < productLocations.length; i++) {
                EdsWarehouse warehouse = (isMultiWarehouseEnabled && productLocations[i].getWarehouseID() != null) ? warehouseManager.get(productLocations[i].getWarehouseID()) : defaultWarehouse;

                EdsItemStock itemStock = getWrappedItemStock(productLocations[i], item, product.getQuantity(), calculationScale, transactionID);
                itemStock.setWarehouse(warehouse);
                itemStock.setOrder(i + 1);
                itemStockManager.create(itemStock);
                item.getItemStockList().add(itemStock);
            }
        }
    }

    private EdsItemStock getWrappedItemStock(ProductLocationItem plItem, EdsItem item, BigDecimal productTotalQty, Integer calculationScale, Integer transactionID) {
        EdsItemStock itemStock = new EdsItemStock();
        itemStock.setItemId(item.getObjectID());
        itemStock.setQuantity(plItem.getQty());
        itemStock.setPrice((productTotalQty != null && productTotalQty.intValue() > 0) ? item.getTotalValue().divide(productTotalQty, calculationScale, RoundingMode.HALF_UP) : item.getUnitPrice());
        itemStock.setTranCode(TC_IN);
        itemStock.setDate(item.getAsOf());
        itemStock.setTranDate(item.getAsOf());
        if (itemStock.getQuantity() != null && itemStock.getPrice() != null) {
            itemStock.setTranValue(itemStock.getQuantity().multiply(itemStock.getPrice()));
        } else {
            itemStock.setTranValue(BigDecimal.ZERO);
        }
        if (transactionID != null) {
            itemStock.setTransaction(transactionManager.get(transactionID));
        }
        return itemStock;
    }

    private void updateProductKitItems(ProductKitItem[] kitItems, EdsItem productKit) {

        //before clear old product kit items
        productKitItemManager.deleteProductKitItems(productKit.getObjectID());

        if (kitItems == null) {
            return;
        }

        for (ProductKitItem item_ : kitItems) {

            if (item_.getProductItem() == null) {
                continue;
            }

            EdsItem product = itemManager.get(item_.getProductItem().getId());
            if (product == null) {
                continue;
            }

            EdsProductKitItems kitItem = new EdsProductKitItems();
            kitItem.setItem(product);
            kitItem.setProductKit(productKit);
            kitItem.setQuantity(item_.getQuantity());
            if (item_.getWarehouse() != null) {
                kitItem.setWarehouse(warehouseManager.get(item_.getWarehouse().getId()));
            }
            productKit.getProductKitItems().add(kitItem);
        }
    }

    private void initProductDiscounts(NewProduct product, EdsItem item) {
        List<EdsDiscount> discounts = item.getDiscounts();

        if (discounts != null && !discounts.isEmpty()) {
            DiscountItem[] discountItems = new DiscountItem[discounts.size() + 2];
            discountItems[0] = new DiscountItem(ONE_OFF_DISCOUNT, ONE_OFF_DISCOUNT_STR);
            discountItems[1] = new DiscountItem(ONE_OFF_FIXED_AMOUNT, ONE_OFF_FIXED_AMOUNT_STR);

            int i = 2;
            for (EdsDiscount discount : discounts) {
                discountItems[i] = new DiscountItem();

                discountItems[i].setId(discount.getObjectID());
                discountItems[i].setName(discount.getName());
                discountItems[i].setCode(discount.getCode());
                discountItems[i].setDescription(discount.getDescription());
                discountItems[i].setActive(discount.isActive());
                discountItems[i].setType(discount.getType());
                discountItems[i].setPercentage(discount.getPercentage());
                discountItems[i].setFixedAmount(discount.getFixedAmount());

                //init discount multi range values
                initDiscountMultiRangeData(discountItems[i], discount.getMultiRangeValueList());

                i++;
            }
            product.setDiscountItems(discountItems);
        }
    }

    private void initDiscountMultiRangeData(DiscountItem discountItem, List<EdsDiscountMultiRangeValue> multiRangeValues) {

        List<DiscountMultiRangeItem> multiRangeItems = new ArrayList<>();

        if (multiRangeValues != null && !multiRangeValues.isEmpty()) {
            for (EdsDiscountMultiRangeValue multiRangeValue : multiRangeValues) {
                DiscountMultiRangeItem multiRangeItem = new DiscountMultiRangeItem();
                multiRangeItem.setId(multiRangeValue.getObjectID());
                multiRangeItem.setType(multiRangeValue.getType());
                multiRangeItem.setFromQty(multiRangeValue.getFromQty());
                multiRangeItem.setToQty(multiRangeValue.getToQty());
                multiRangeItem.setFromAmount(multiRangeValue.getFromAmount());
                multiRangeItem.setToAmount(multiRangeValue.getToAmount());
                multiRangeItem.setPercentage(multiRangeValue.getPercentage());
                multiRangeItem.setFixedAmount(multiRangeValue.getFixedAmount());

                discountItem.setMultiRangeDiscountType(multiRangeValue.getType());

                multiRangeItems.add(multiRangeItem);
            }
        }

        discountItem.setMultiRangeItems(multiRangeItems.toArray(new DiscountMultiRangeItem[]{}));
    }

    private void initRentalItemOptions(NewProduct product, EdsItem item) {
        product.setRentalPeriod(item.getRentalPeriod());
        product.setRentalRate(item.getRentalRate());
        product.setOverdueRate(item.getOverdueRate());
        product.setCancelationPeriod(item.getCancelationPeriod());
        product.setCancelationPeriodType(item.getCancelationPeriodType());
        product.setCancelationFee(item.getCancelationFee());
    }

    private void initStorefrontOptions(NewProduct product, EdsItem item) {
        product.setFeatured(item.getFeatured());
        product.setSpecial(item.getSpecial());
        product.setShowOnHomepage(item.getShowOnHomePage());
        product.setVirtual(item.getVirtual());
        product.setFreeShipping(item.getFreeShipping());
        product.setCondition(item.getCondition());
    }

    public void initProductWarehouseLocations(NewProduct product, EdsItem item, LinkedHashMap<Integer, List<StockItem>> stockItemsMap) {
        LinkedHashMap<Integer, ProductLocationItem> pLocations = new LinkedHashMap<>();
        List<EdsProductWarehouseLocation> productWarehouseLocations = item.getProductWarehouseLocations();

        for (EdsProductWarehouseLocation pwl : productWarehouseLocations) {
            ProductLocationItem plItem = new ProductLocationItem();
            plItem.setObjectID(pwl.getObjectID());
            if (pwl.getWarehouse() != null) {
                plItem.setWarehouseID(pwl.getWarehouse().getObjectID());
                plItem.setWarehouseName(pwl.getWarehouse().getName());
            }
            plItem.setProductID(pwl.getProduct().getObjectID());
            if (pwl.getProductLocation() != null) {
                plItem.setProductLocationID(pwl.getProductLocation().getObjectID());
                plItem.setProductLocationName(pwl.getProductLocation().getName());
                plItem.setProductLocationDescription(pwl.getProductLocation().getAsSelectItem() != null ? pwl.getProductLocation().getAsSelectItem().getName() : "");
            }
            plItem.setMinReorderPoint(pwl.getMinReorderPoint());
            plItem.setQty(BigDecimal.ZERO);

            pLocations.put(plItem.getWarehouseID(), plItem);
        }
        List<StockItem> stockItems = new ArrayList<>();
        stockItems = stockItemsMap != null && stockItemsMap.size() > 1 ? stockItemsMap.get(item.getObjectID()) : itemStockManager.getWarehouseStocks(item.getObjectID());

        if (CollectionUtils.isNotEmpty(stockItems)) {
            for (StockItem stockItem : stockItems) {
                ProductLocationItem plItem = pLocations.get(stockItem.getWarehouseID());
                if (plItem == null) {
                    plItem = new ProductLocationItem();
                    plItem.setWarehouseID(stockItem.getWarehouseID());
                    plItem.setWarehouseName(stockItem.getWarehouseName());
                }
                plItem.setQty(stockItem.getQuantity());

                pLocations.put(stockItem.getWarehouseID(), plItem);
            }
        }
        product.setProductLocations(pLocations.values().toArray(new ProductLocationItem[]{}));
    }

    public BigDecimal getAverageCost(Integer itemID) {
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SHOW_LAST_PURCHASE_PRICE_IN_PRICE_COLUMN)) {
            return itemStockManager.getItemLastInStockTranValue(itemID);
        } else {
            EdsItem item = itemManager.getItem(itemID);
            if (item != null) {
                return getAverageCost(item);
            }
        }
        return null;
    }

    private BigDecimal getAverageCost(EdsItem item) {

        Integer calculationScale = financialSettingsManager.getFinancialSettings().getAccountingCalculationScale();

        Object beginningBalance = itemStockManager.getInventoryTransactionBalanceToDate(item.getObjectID(), null, null);

        BigDecimal bBalance = null;
        BigDecimal bQty = null;
        BigDecimal bResult = null;
        if (beginningBalance != null) {
            bQty = (((Object[]) beginningBalance)[0] != null && !((Object[]) beginningBalance)[0].equals(ZERO)) ? (BigDecimal) ((Object[]) beginningBalance)[0] : null;
            bBalance = (((Object[]) beginningBalance)[1] != null && !((Object[]) beginningBalance)[1].equals(ZERO)) ? (BigDecimal) ((Object[]) beginningBalance)[1] : null;
        }

        if (bBalance != null && bQty != null && bQty.compareTo(BigDecimal.ZERO) != 0) {
            bResult = bBalance.divide(bQty, calculationScale, RoundingMode.HALF_UP);
        }
        return bResult;
    }

    private InventoryStockValuationItem[] getInventoryItemTransactionValuation(Integer itemid,
                                                                               ListingFilterParameter fp,
                                                                               LinkedHashMap<Integer, List<StockItem>> stockItemListMap,
                                                                               LinkedHashMap<Integer, EdsTransaction> stockTransactionMap) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        fp.setCaseID(itemid);

        ArrayList<InventoryStockValuationItem> stockValuationItems = new ArrayList<>();

        List<StockItem> stockItemList = stockItemListMap.get(itemid);
        if (stockItemList != null && !stockItemList.isEmpty()) {
            for (StockItem stockItem : stockItemList) {
                EdsTransaction transaction = stockTransactionMap.get(stockItem.getTransactionID());
                InventoryStockValuationItem valuationItem = new InventoryStockValuationItem();
                valuationItem.setEntryDate(new DateNonConvertable(transaction.getPostedDate()));
                valuationItem.setTransactionDate(new DateNonConvertable(transaction.getJournalDate()));

                if (transaction instanceof EdsInventoryTransaction inventoryTransaction) {
                    if (TT_OPENING_BALANCE.equals(inventoryTransaction.getTransactionType())) {
                        valuationItem.setTransactionType(TT_OPENING_BALANCE);
                    } else if (TT_BUILD_ASSEMBLY.equals(inventoryTransaction.getTransactionType())) {
                        valuationItem.setTransactionType(TT_BUILD_ASSEMBLY);
                        EdsSavedAssemblyItem buildAssembly = inventoryTransaction.getBuildAssembly();
                        if (buildAssembly != null) {
                            valuationItem.setItemId(buildAssembly.getObjectID());
                            valuationItem.setName(buildAssembly.getItemName());
                        }
                    } else {
                        valuationItem.setTransactionType(TT_STOCK_ADJUSTMENT);
                    }
                    valuationItem.setNumber(inventoryTransaction.getInventory().getProductNumber());
                } else if (transaction instanceof EdsGoodsReceivedTransaction goodsReceivedTransaction) {
                    valuationItem.setTransactionType(TT_GOODS_RECEIVED);
                    EdsQuote purchaseOrder = goodsReceivedTransaction.getPurchaseOrder();

                    if (purchaseOrder == null &&
                            goodsReceivedTransaction.getShippingData() != null &&
                            goodsReceivedTransaction.getShippingData().getQuote() != null) {
                        valuationItem.setShippingDataId(goodsReceivedTransaction.getShippingData().getObjectID());
                        valuationItem.setNumber(goodsReceivedTransaction.getShippingData().getQuote().getNumber());
                        if (goodsReceivedTransaction.getShippingData().getQuote().getClientOrSupplier() != null) {
                            valuationItem.setName(goodsReceivedTransaction.getShippingData().getQuote().getClientOrSupplier().getName());
                        }
                        valuationItem.setItemId(goodsReceivedTransaction.getShippingData().getQuote().getObjectID());
                        valuationItem.setShippingDataNumber(goodsReceivedTransaction.getShippingData().getNumber());
                    }
                    if (purchaseOrder != null) {
                        valuationItem.setNumber(purchaseOrder.getNumber());
                        valuationItem.setName(purchaseOrder.getClientOrSupplier().getName());
                        valuationItem.setItemId(purchaseOrder.getObjectID());
                    }
                } else if (transaction instanceof EdsGoodsDeliveredTransaction goodsDeliveredTransaction) {
                    valuationItem.setTransactionType(TT_GOODS_DELIVERED);
                    EdsQuote saleQuote = goodsDeliveredTransaction.getSaleOrder();

                    if (saleQuote == null &&
                            goodsDeliveredTransaction.getShippingData() != null &&
                            goodsDeliveredTransaction.getShippingData().getQuote() != null) {
                        valuationItem.setShippingDataId(goodsDeliveredTransaction.getShippingData().getObjectID());
                        valuationItem.setNumber(goodsDeliveredTransaction.getShippingData().getQuote().getNumber());
                        if (goodsDeliveredTransaction.getShippingData().getQuote().getClientOrSupplier() != null) {
                            valuationItem.setName(goodsDeliveredTransaction.getShippingData().getQuote().getClientOrSupplier().getName());
                        }
                        valuationItem.setItemId(goodsDeliveredTransaction.getShippingData().getQuote().getObjectID());
                        valuationItem.setShippingDataNumber(goodsDeliveredTransaction.getShippingData().getNumber());
                    }
                    if (saleQuote != null) {
                        valuationItem.setNumber(saleQuote.getNumber());
                        valuationItem.setName(saleQuote.getClientOrSupplier().getName());
                        valuationItem.setItemId(saleQuote.getObjectID());
                    }

                } else if (transaction instanceof EdsStockAdjustmentTransaction adjustmentTransaction) {
                    EdsStockAdjustment adjustment = adjustmentTransaction.getAdjustment();

                    valuationItem.setTransactionType(TT_STOCK_ADJUSTMENT);
                    valuationItem.setItemId(adjustment.getObjectID());
                    valuationItem.setNumber(adjustment.getNumber());

                } else if (transaction instanceof EdsInvoiceTransaction invoiceTransaction) {
                    EdsInvoice invoice = invoiceTransaction.getInvoice();
                    valuationItem.setNumber(invoice.getNumber());
                    valuationItem.setName(invoice.getClientOrSupplier().getName());
                    if (invoice.getType().equals(PAYABLE)) {
                        valuationItem.setTransactionType(TT_PURCHASE);
                        if (invoice.isCreditNote()) {
                            valuationItem.setTransactionType(TT_SUPPLIER_CREDIT_NOTE);
                        }
                        valuationItem.setItemId(invoice.getObjectID());
                    } else {
                        valuationItem.setTransactionType(TT_INVOICE);
                        if (invoice.isCreditNote()) {
                            valuationItem.setTransactionType(TT_CUSTOMER_CREDIT_NOTE);
                        }
                        valuationItem.setItemId(invoice.getObjectID());
                    }
                } else if (transaction instanceof EdsStockTransferTransaction tempT) {
                    EdsStockTransfer stockTransfer = tempT.getStockTransfer();
                    valuationItem.setName(stockTransfer.getTransferName());
                    valuationItem.setNumber(stockTransfer.getNumber());
                    valuationItem.setTransactionType(TT_STOCK_TRANSFER);
                    valuationItem.setEntryDate(new DateNonConvertable(stockTransfer.getDate()));
                    valuationItem.setItemId(stockTransfer.getObjectID());
                }

                if (stockItem.getTransactionCode().equals(TC_IN)) {
                    valuationItem.setTransactionValue(stockItem.getTransactionValue());
                    valuationItem.setQty(stockItem.getQuantity());
                } else {
                    valuationItem.setTransactionValue(stockItem.getTransactionValue().multiply(new BigDecimal(-1)));
                    valuationItem.setQty(stockItem.getQuantity().multiply(new BigDecimal(-1)));

                }

                valuationItem.setQuantityPerPriceList(stockItem.getQuantityPerPriceList());
                valuationItem.setPriceListWithoutScaling(stockItem.getPriceListWithoutScaling());
                stockValuationItems.add(valuationItem);
            }
        }

        return stockValuationItems.toArray(new InventoryStockValuationItem[0]);
    }

    private ProductPicture[] getProductPictures(EdsItem item) {
        boolean hasDefaultPicture = false;

        ProductPicture[] pictures = new ProductPicture[item.getPictures().size()];

        int index = 0;
        for (EdsProductPicture picture : item.getPictures()) {
            if (picture.isDefaultPicture()) {
                pictures[index] = new ProductPicture();
                pictures[index].setProductID(item.getObjectID());
                pictures[index].setPictureID(picture.getObjectID());
                pictures[index].setUrl(getImageUrl(picture));

                List<EdsProductPicture> subPictures = productPictureManager.getProductSubPictures(picture.getObjectID());
                for (EdsProductPicture spicture : subPictures) {
                    if (FILE_SIZE_MEDIUM.equals(spicture.getFileSizeType())) {
                        pictures[index].setUrlMedium(getImageUrl(spicture));
                    } else if (FILE_SIZE_SMALL.equals(spicture.getFileSizeType())) {
                        pictures[index].setUrlSmall(getImageUrl(spicture));
                    } else if (FILE_SIZE_ORIGINAL.equals(spicture.getFileSizeType())) {
                        pictures[index].setUrlOriginal(getImageUrl(spicture));
                    }
                }

                hasDefaultPicture = true;
                index++;

                break;
            }
        }

        for (EdsProductPicture picture : item.getPictures()) {
            if (!hasDefaultPicture || !picture.isDefaultPicture()) {
                pictures[index] = new ProductPicture();
                pictures[index].setProductID(item.getObjectID());
                pictures[index].setPictureID(picture.getObjectID());
                pictures[index].setUrl(getImageUrl(picture));

                List<EdsProductPicture> subPictures = productPictureManager.getProductSubPictures(picture.getObjectID());
                for (EdsProductPicture spicture : subPictures) {
                    if (FILE_SIZE_MEDIUM.equals(spicture.getFileSizeType())) {
                        pictures[index].setUrlMedium(getImageUrl(spicture));
                    } else if (FILE_SIZE_SMALL.equals(spicture.getFileSizeType())) {
                        pictures[index].setUrlSmall(getImageUrl(spicture));
                    } else if (FILE_SIZE_ORIGINAL.equals(spicture.getFileSizeType())) {
                        pictures[index].setUrlOriginal(getImageUrl(spicture));
                    }
                }

                index++;
            }
        }

        return pictures;
    }

    public String getImageUrl(EdsUpload upload) {
        if (upload != null) {
            return uploadManager.getFileURL(upload);
        }
        return "";
    }

    @Override
    public List<NewProduct> getInterCompanyTransactionProducts(NewInvoiceItem[] invoiceItems) {
        List<NewProduct> products = new LinkedList<>();
        HashMap<Integer, Integer> productIDsMap = new HashMap<>();
        if (invoiceItems != null) {
            for (NewInvoiceItem nii : invoiceItems) {
                if (nii.getItemID() != null && nii.getItemID() > 0 && !productIDsMap.containsKey(nii.getItemID())) {
                    NewProduct product = getProductBaseData(nii.getItemID());
                    product.setProductLocations(null);
                    products.add(product);
                    productIDsMap.put(nii.getItemID(), nii.getItemID());
                }
            }
        }
        return products;
    }

    @Override
    public HashMap<Integer, Integer> convertInterCompanyProducts(List<NewProduct> products) {
        HashMap<Integer, Integer> conversionIDSMap = new HashMap<>();
        NumberData numberData = generateProductNumber();
        if (products != null && !products.isEmpty()) {
            Date currentDate = new Date();
            for (NewProduct pd : products) {
                String productUniqueID = pd.getSubsidiaryProductUniqueID();
                EdsItem product = itemManager.getInterCompanyProductByUniqueID(productUniqueID);
                if (product != null) {
                    conversionIDSMap.put(pd.getObjectId(), product.getObjectID());
                } else {
                    pd.setTaxIDs(null);
                    pd.setTaxItem(null);

                    Integer externalProductID = pd.getObjectId();
                    pd.setObjectId(null);
                    pd.setNumberData(generateNonExistingProductNumberData(numberData));
                    pd.setAsOf(new DateNonConvertable(currentDate));
                    pd.setTotalValue(BigDecimal.ZERO);
                    pd.setAttachments(null);
                    pd.setImageGallery(null);
                    Integer productID = saveProduct(pd).getId();

                    if (productID != null && productID > 0) {
                        product = itemManager.get(productID);
                        conversionIDSMap.put(externalProductID, product.getObjectID());
                    } else {
                        return null;
                    }
                }
            }
        }
        return conversionIDSMap;
    }

    private NumberData generateNonExistingProductNumberData(NumberData numberData) {
        while (accountingManager.isProductNumberExists(numberData.getNumberString(), null)) {
            numberData = generateProductNumber(numberData.getIntNumber());
        }
        return numberData;
    }

    @Override
    public SelectItem[] getInterCompanyProducts(ListingFilterParameter filterParametrs) {
        filterParametrs.setStart(0);
        filterParametrs.setLimit(20);
        List<EdsSubsidiaryProduct> interCompanyProducts = itemManager.getInterCompanyProducts(filterParametrs);
        SelectItem[] spItems = new SelectItem[interCompanyProducts.size()];
        int i = 0;
        for (EdsSubsidiaryProduct sp : interCompanyProducts) {
            spItems[i++] = new SelectItem(sp.getObjectID(), sp.getProductName());
        }
        return spItems;
    }

    @Override
    public ProductCustomSettings getProductServiceListCustomSettings() {
        ProductCustomSettings settings = new ProductCustomSettings();
        HashMap<String, String> customSettings = new HashMap<>();
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CUSTOM_INVOICE_ITEM_PRODUCT_DESCRIPTION_ENABLED)) {
            customSettings.put(CUSTOM_ASSEMBLY_ITEMS_DESCRIPTION, "YES");
        }
        settings.setCustomSettings(customSettings);
        settings.setProductCategory(accountingService.getCategoriesAsTreeSelectItem());
        EdsFinancialSettings fsettings = financialSettingsManager.getFinancialSettings();
        if (fsettings != null && fsettings.isEnableMultipleSalesPrice()) {
            List<EdsCurrency> currencyList = exchangeCurrencyManager.getCurrencyList();
            for (EdsCurrency currency : currencyList) {
                settings.getCompanyCurrencyList().add(currency.getName());
            }
        }
        List<EdsUnitMeasurement> unitMeasurements = unitMeasurementManager.getUnitMeasurements(null, userManager.getUser().getCompany().getObjectID());
        ArrayList<TreeSelectItem> ums = new ArrayList<>();
        for (EdsUnitMeasurement unitMeasurement : unitMeasurements) {
            TreeSelectItem tsi = new TreeSelectItem(unitMeasurement.getObjectID(), unitMeasurement.getName());
            tsi.setShowInDropDown(true);
            ums.add(tsi);
        }
        settings.setUnitMeasurementItems(ums.toArray(new TreeSelectItem[]{}));
        return settings;
    }

    @Override
    public boolean inActiveProduct(Integer objectId) {
        return activateProduct(objectId, false);
    }

    @Override
    public Boolean inActiveProducts(ArrayList<Integer> ids) {
        boolean message = false;
        for (Integer objectId : ids) {
            message = activateProduct(objectId, false);
        }

        return message;
    }

    private boolean activateProduct(Integer objectId, boolean active) {
        EdsItem item = itemManager.get(objectId);
        if (item != null) {
            item.setActive(active);
            item.setLastUpdateTime(new Date());
            item.setUpdater(userManager.getUser());
            for (EdsItem child : itemManager.getChildProducts(item.getObjectID())) {
                activateProduct(child.getObjectID(), active);
            }
            try {
                productsServicesSolrComponent.index(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    @Override
    public ListResult<StockAdjustmentListItem> getStockAdjustments(ListingFilterParameter filterParametrs) {
        List<EdsStockAdjustment> adjustmentsData = stockAdjustmentManager.getList(filterParametrs);
        ArrayList<StockAdjustmentListItem> stockAdjustmentListItems = new ArrayList<>();
        for (EdsStockAdjustment item : adjustmentsData) {
            StockAdjustmentListItem stockAdjustmentItem = new StockAdjustmentListItem();
            EdsStockAdjustment stockAdjustment = stockAdjustmentManager.get(item.getObjectID());
            stockAdjustmentItem.setObjectID(item.getObjectID());
            stockAdjustmentItem.setNumber(item.getNumber());
            stockAdjustmentItem.setDate(item.getDate());
            stockAdjustmentItem.setMemo(item.getMemo());
            stockAdjustmentItem.setCreator(stockAdjustment.getCreatorAsSelectItem());
            stockAdjustmentItem.setUpdator(stockAdjustment.getUpdatorAsSelectItem());

            if (stockAdjustment.getOverallStatus() != null) {
                stockAdjustmentItem.setStatusCode(stockAdjustment.getOverallStatus().getCode());
            }
            if (stockAdjustment != null && stockAdjustment.getAccount() != null) {
                stockAdjustmentItem.setAccountName(stockAdjustment.getAccount().getName());
            }
            stockAdjustmentListItems.add(stockAdjustmentItem);
        }

        return new ListResult<>(stockAdjustmentListItems, stockAdjustmentManager.getTotalCount(filterParametrs));
    }

    @Override
    public AdjustmentItem getStockAdjustmentData(Integer objectID) {
        AdjustmentItem result;
        if (objectID != null) {
            EdsStockAdjustment stockAdjustment = stockAdjustmentManager.get(objectID);
            result = stockAdjustment.getDataAsRPC();
            result.setApproverSaved(approverManager.isExistApproverByEntityTypeAndEntityId(RelationItem.TYPE_STOCK_ADJUSTMENT, stockAdjustment.getObjectID()));
            EdsStockAdjustmentTransaction transaction = transactionManager.getTransactionByStockAdjustment(stockAdjustment);
            if (transaction != null) {
                result.setJournalID(transaction.getJournalId());
            }
            for (ProductItem productItem : result.getProductItems()) {
                EdsItem item = itemManager.get(productItem.getObjectId());
                if (item != null && item.getInventoryTrackingEnabled()) {
                    productItem.setSerials(itemSerialService.getSerials(productItem.getLineItemID(), ItemSerialEntityType.STOCK_ADJUSTMENT_IN));
                    productItem.setAssignedSerials(itemSerialService.getSerials(productItem.getLineItemID(), ItemSerialEntityType.STOCK_ADJUSTMENT_OUT));
                }
                if (item != null && item.getTrackBatchesEnabled()) {
                    productItem.setBatchItems(itemBatchService.getBatchItems(productItem.getLineItemID(), item.getObjectID(), objectID, ItemSerialEntityType.STOCK_ADJUSTMENT_IN.name()));
                    productItem.setAssignedBatchItems(itemBatchService.getBatchItems(productItem.getLineItemID(), item.getObjectID(), objectID, ItemSerialEntityType.STOCK_ADJUSTMENT_OUT.name()));
                }
            }
        } else {
            result = new AdjustmentItem();
            BankTransferNumberData btnd = generateStockAdjustmentNumberFormat();
            result.setBankTransferNumberData(btnd);
            result.setNumber(btnd.getTransferNumber());
            result.setIntNumber(Integer.parseInt(btnd.getFourDigitNumber()));
        }
        result.setCurrentUserId(userManager.getUser().getObjectID());
        result.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_STOCK_ADJUSTMENT));
        return result;
    }

    @Override
    public Integer deleteStockAdjustment(Integer objectID) {
        final EdsStockAdjustment stockAdjustment = stockAdjustmentManager.get(objectID);

        if (stockAdjustment == null) {
            return null;
        }
        EdsTransaction transaction = transactionManager.getTransactionByStockAdjustment(stockAdjustment);

        if (transaction != null) {
            itemStockManager.deleteItemStocksByTransaction(transaction.getObjectID());
            transaction.setDeleted(true);
            transactionManager.setChangedAccountsForRecalculate(transaction.getObjectID());
            transactionManager.update(transaction);
            baseEventPostProcessor.registerEvent(TransactionCustomEventListenerImpl.TYPE, TransactionCustomEventListenerImpl.EVENT_REMOVE_STOCK_ADJUSTMENT_TRANSACTION, stockAdjustment, userManager.getUser());
        }
        EdsUser user = userManager.getUser();
        stockAdjustment.setDeleted(true);
        stockAdjustment.setUpdater(user);
        stockAdjustment.setLastUpdateTime();
        stockAdjustmentManager.update(stockAdjustment);
        final List<FileResource> attachments = attachmentUtilsManager.getAttachments(F_STOCK_ADJUSTMENT, objectID, objectID);
        final List<Integer> attachmentIds = Lists.newArrayList();

        for (FileResource attachment : attachments) {
            attachmentIds.add(attachment.getObjectId());
        }
        commonServiceLocal.deleteFiles(attachmentIds);
        final Set<EdsItem> itemSet = Sets.newHashSet();

        for (EdsAdjustmentItem edsAdjustmentItem : stockAdjustment.getAdjustmentItemList()) {
            if (edsAdjustmentItem.getItem() != null) {
                itemSet.add(edsAdjustmentItem.getItem());
                if (edsAdjustmentItem.getItem().getInventoryTrackingEnabled()) {
                    itemSerialService.deleteSerialRelation(edsAdjustmentItem.getObjectID(), ItemSerialEntityType.STOCK_ADJUSTMENT_IN.name());
                    itemSerialService.deleteSerialRelation(edsAdjustmentItem.getObjectID(), ItemSerialEntityType.STOCK_ADJUSTMENT_OUT.name());
                }
                if (edsAdjustmentItem.getItem().getTrackBatchesEnabled()) {
                    itemBatchManager.deleteBatchesByEntity(objectID, edsAdjustmentItem.getItem().getObjectID(), ItemSerialEntityType.STOCK_ADJUSTMENT_IN.name());
                    itemBatchManager.deleteBatchesByEntity(objectID, edsAdjustmentItem.getItem().getObjectID(), ItemSerialEntityType.STOCK_ADJUSTMENT_OUT.name());
                }
            }
        }
        if (!itemSet.isEmpty()) {
            try {
                List<EdsItem> items = new ArrayList<>(itemSet);
                productsServicesSolrComponent.indexes(items);
            } catch (Exception ignored) {
            }
        }
        return stockAdjustment.getObjectID();
    }

    @Override
    public Integer deleteStockTransfer(Integer objectID) {
        EdsStockTransfer stockTransfer = stockTransferManager.get(objectID);
        EdsTransaction transaction = transactionManager.getTransactionByStockTransfer(stockTransfer);
        List<EdsStockAdjustment> stockAdjustments = stockAdjustmentManager.getStockAdjustmentsByStockTransfer(stockTransfer.getObjectID());
        for (EdsStockAdjustment stockAdjustment : stockAdjustments) {
            Integer result = deleteStockAdjustment(stockAdjustment.getObjectID());
            for (EdsAdjustmentItem edsAdjustmentItem : stockAdjustment.getAdjustmentItemList()) {
                if (edsAdjustmentItem.getItem() != null && edsAdjustmentItem.getItem().getTrackBatchesEnabled()) {
                    itemBatchManager.deleteBatchesByEntity(objectID, edsAdjustmentItem.getItem().getObjectID(), ItemSerialEntityType.STOCK_TRANSFER_IN.name());
                    itemBatchManager.deleteBatchesByEntity(objectID, edsAdjustmentItem.getItem().getObjectID(), ItemSerialEntityType.STOCK_TRANSFER_OUT.name());
                }
            }
            if (result < 0) {
                return -1;
            }
        }

        if (transaction != null) {
            itemStockManager.deleteItemStocksByTransaction(transaction.getObjectID());
            transaction.setDeleted(true);
            transactionManager.setChangedAccountsForRecalculate(transaction.getObjectID());
            transactionManager.update(transaction);
            baseEventPostProcessor.registerEvent(TransactionCustomEventListenerImpl.TYPE, TransactionCustomEventListenerImpl.EVENT_REMOVE_STOCK_TRANSFER_TRANSACTION, stockTransfer, userManager.getUser());
        }

        stockTransfer.setDeleted(true);
        stockTransferManager.update(stockTransfer);
        return stockTransfer.getObjectID();
    }

    @Override
    public void reorderPointEmail(Integer companyId) {
        List<String[]> stockProducts;
        List<EdsEmployee> userList = userManager.getUsersByROLES(companyId, EdsRole.ADMIN);
        if (userList == null || userList.isEmpty()) {
            System.out.println("Product Stock :CompanyID=" + companyId + "NO Admins");
            return;
        }
        for (EdsEmployee user : userList) {
            boolean emailNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(user.getObjectID(), EmailNotificationConstants.PRODUCT_STOCK_NOTIFICATION);
            if (!emailNotificationSettings) {
                System.out.println("Product Stock :CompanyID=" + companyId + ": UserID=" + user.getObjectID() + " disabled sending notification");
            } else {
                stockProducts = new ArrayList<>();
                List<Object> inResult = itemStockManager.getProductsStock(companyId, TC_IN);
                if (inResult != null && !inResult.isEmpty()) {
                    List<Object> outResult = itemStockManager.getProductsStock(companyId, TC_OUT);
                    for (Object anInResult : inResult) {
                        Object[] itemIn = (Object[]) anInResult;
                        if (outResult != null && !outResult.isEmpty()) {
                            for (Object anOutResult : outResult) {
                                Object[] itemOut = (Object[]) anOutResult;
                                if (Integer.parseInt(String.valueOf(itemIn[0])) == Integer.parseInt(String.valueOf(itemOut[0]))) {
                                    Double qtyIn = Double.parseDouble(String.valueOf(itemIn[1]));
                                    Double qtyOut = Double.parseDouble(String.valueOf(itemOut[1]));
                                    double diff = qtyIn - qtyOut;
                                    double minreorderpoint = 0.0;
                                    if (itemIn[3] != null) {
                                        minreorderpoint = Double.parseDouble(String.valueOf(itemIn[3]));
                                    } else {
                                        break;
                                    }
                                    if (diff <= minreorderpoint) {
                                        Integer productID = Integer.parseInt(String.valueOf(itemIn[0]));
                                        String[] stockItem = new String[5];
                                        stockItem[0] = "";
                                        stockItem[0] = itemIn[6] != null ? String.valueOf(itemIn[6]) : "";

                                        stockItem[1] = "";
                                        stockItem[1] = itemIn[4] != null ? String.valueOf(itemIn[4]) : "";

                                        stockItem[2] = "";
                                        stockItem[2] = itemIn[5] != null ? String.valueOf(itemIn[5]) : "";

                                        stockItem[3] = "";
                                        stockItem[3] = Double.toString(minreorderpoint);

                                        stockItem[4] = "";
                                        stockItem[4] = itemIn[2] != null ? String.valueOf(itemIn[2]) : "";

                                        stockProducts.add(stockItem);

                                    }
                                }
                            }
                        }
                    }
                    try {
                        if (!stockProducts.isEmpty()) {
                            messageManager.sendProductStockNotification(new EdsItem(), user, companyId, stockProducts);
                        } else {
                            System.out.println("No Product ReorderPoint CompanyID=" + companyId);
                        }
                    } catch (EdsDbException ignored) {

                    }
                }
            }
        }
    }

    public BankTransferNumberData generateStockAdjustmentNumberFormat() {
        BankTransferNumberData transferNumberData = new BankTransferNumberData();
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer fourDigitNumber = stockAdjustmentManager.getStockAdjustmentIntNumber();
        String format = null;
        if (settings != null) {
            format = settings.getStockAdjustmentNumberingFormat();
        }
        if (format != null) {
            parseNumber(format, transferNumberData, fourDigitNumber);
        } else {
            String prefix = EdsNumberingSettings.DEF_STOCK_ADJUMENT;
            NumberData numberData = EdsNumberingSettings.getDefaultData(fourDigitNumber, prefix);
            String[] numberParts = numberData.getNumberFormat().split("_");
            transferNumberData.setPrefix(numberParts[0]);
            transferNumberData.setFourDigitNumber(String.valueOf(numberParts[1]));
            transferNumberData.setWithDate(numberParts[1].split("-").length == 2);
        }
        return transferNumberData;
    }

    private void parseNumber(String numberFormat, BankTransferNumberData numberData, Integer fourDigitNumber) {
        String[] mainPartNumbers = numberFormat.split("_");  // e.g SA_0001-05/2015
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

    public ProductSelectItem getProductAsSelectItem(Integer id) {
        EdsItem product = itemManager.get(id);
        return product != null ? product.getAsProductSelectItem() : null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getProductsAsSelectItem(ListingFilterParameter fp) {
        List<Object[]> products = itemManager.getCompanyItems();
        if (products != null && !products.isEmpty()) {
            SelectItem[] items = new SelectItem[products.size()];
            int i = 0;
            for (Object[] product : products) {
                items[i] = new SelectItem((Integer) product[0], (String) product[1]);
                i++;
            }
            return items;
        }
        return new SelectItem[0];
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProductSelectItem[] getCompanyProductsByType(ListingFilterParameter filterParameters) {
        EdsUser user = userManager.getUser();
        boolean isDescriptionIncluded = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PRODUCT_LOOKUP_DESCRIPTION_INCLUDED);
        boolean isBarcodeIncluded = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PRODUCT_LOOKUP_BARCODE_INCLUDED);
        filterParameters.setEmployeeId(user.getObjectID());
        filterParameters.setRoles(user.getRolesCodeAsString());

        filterParameters.setLookUp(false);
        List<EdsItem> itemList = itemManager.getCompanyProductsByType(filterParameters, isDescriptionIncluded, isBarcodeIncluded);
        LinkedList<EdsItem> temp = new LinkedList<>();

        if ((PRODUCT_KIT.equals(filterParameters.getWithoutType()) || ASSEMBLY_ITEM.equals(filterParameters.getWithoutType())) && filterParameters.getObjectId() != null) {
            for (EdsItem product : itemList) {
                if (product.getName() != null && !product.getObjectID().equals(filterParameters.getObjectId())) {
                    HashMap<Integer, Integer> subItemsMap = product.getProductSubItems(new HashMap<>());
                    if (!subItemsMap.containsKey(filterParameters.getObjectId())) {
                        temp.add(product);
                    }
                }
            }
        } else {
            for (EdsItem item : itemList) {
                if (item.getName() != null) {
                    temp.add(item);
                }
            }
        }

        itemList = temp;

        itemList = ListUtils.getSublist(itemList, filterParameters.getStart(), filterParameters.getLimit());

        ArrayList<ProductSelectItem> items = new ArrayList<>();
        if (isDescriptionIncluded) {
            for (EdsItem item : itemList) {
                ProductSelectItem pItem = new ProductSelectItem(item.getObjectID(), item.getName() + ((item.getDescription() != null && !item.getDescription().trim().isEmpty()) ?
                        " - " + (item.getDescription().length() > 100 ? item.getDescription().substring(0, 100) + "..." : item.getDescription()) : ""), item.getProductType(), item.isPurchasedFromSupplier(), item.getBrand() != null ? item.getBrand().getObjectID() : null, item.getBrand() != null ? item.getBrand().getName() : "", item.getSellingPrice());
                pItem.setQtyOnHand(item.getItemsInStock());
                pItem.setDescription(item.getDescription());
                pItem.setInventoryTrackingEnabled(item.getInventoryTrackingEnabled());
                pItem.setDiscountAmount(item.getDiscountAmount());
                pItem.setDiscountType(item.getDiscountType());
                Set<EdsLocation> locations = item.getLocations();
                ArrayList<Integer> locationIds = new ArrayList<>();
                if (!locations.isEmpty()) {
                    locationIds = locations.stream()
                            .map(EdsLocation::getObjectID)
                            .collect(Collectors.toCollection(ArrayList::new));
                    if (!locations.isEmpty()) {
                        pItem.setLocationIds(locationIds);
                    }
                }
                items.add(pItem);
            }
        } else if (isBarcodeIncluded) {
            for (EdsItem item : itemList) {
                StringBuilder name = new StringBuilder();
                if (StringUtils.isNotEmpty(item.getProductNumber())) {
                    name.append(item.getProductNumber()).append(" -> ");
                }
                name.append(item.getName());
                if (StringUtils.isNotBlank(item.getBarCode())) {
                    name.append(" - ").append(item.getBarCode());
                }
                ProductSelectItem pItem = new ProductSelectItem(
                        item.getObjectID()
                        , name.toString()
                        , item.getProductType()
                        , item.isPurchasedFromSupplier()
                        , item.getBrand() != null ? item.getBrand().getObjectID() : null
                        , item.getBrand() != null ? item.getBrand().getName() : ""
                        , item.getSellingPrice());
                pItem.setQtyOnHand(item.getItemsInStock());
                pItem.setDescription(item.getDescription());
                pItem.setInventoryTrackingEnabled(item.getInventoryTrackingEnabled());
                pItem.setDiscountAmount(item.getDiscountAmount());
                pItem.setDiscountType(item.getDiscountType());
                items.add(pItem);
            }
        } else {
            boolean isLookUpProductFromCF = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.LOOK_UP_PRODUCT_FROM_CF);
            String cfields = null;

            if (isLookUpProductFromCF) {
                cfields = genericSettingsManager.getValueByKey(GenericSettingsEnum.PRODUCT_CUSTOM_FIELDS);
            }


            for (EdsItem item : itemList) {
                String name;
                if (filterParameters.getWarehouseID() != null && STOCK_TRANSFER.equals(filterParameters.getInvoiceType())) {
                    StringBuilder nameWithQTY = new StringBuilder();
                    if (item.getProductNumber() != null && !"".equals(item.getProductNumber())) {
                        nameWithQTY.append(item.getProductNumber()).append(" -> ");
                    }
                    nameWithQTY.append(item.getName());

                    nameWithQTY.append(" (");
                    BigDecimal qtyInWarehouse = itemStockManager.getAvailableStock(item.getObjectID(), filterParameters.getWarehouseID(), null);
                    nameWithQTY.append(qtyInWarehouse.setScale(ServerUtils.getCalculationScale(), RoundingMode.HALF_UP)).append(")");

                    name = nameWithQTY.toString();
                } else {
                    name = ((item.getProductNumber() != null && !"".equals(item.getProductNumber()))
                            ? item.getProductNumber() + " -> "
                            : "") + item.getName();
                }

                if (isLookUpProductFromCF && cfields != null && !cfields.isEmpty()) {
                    ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.ProductServiceView);
                    ArrayList<CompanyCustomFieldItem> cfList = CustomFieldsUtils.setRPCCustomFieldItems(item.getCustomFields(), customFieldItems);

                    StringBuilder cfBuild = new StringBuilder();
                    for (String columnCode : cfields.split(",")) {
                        String v = getCustomFieldValueByCode(columnCode, cfList);
                        if (v != null && !v.isEmpty()) {
                            cfBuild.append(" ").append(v);
                        }
                    }

                    if (!cfBuild.toString().isEmpty()) {
                        name += cfBuild.toString();
                    }
                }

                Set<EdsLocation> locations = item.getLocations();
                ArrayList<Integer> locationIds = new ArrayList<>();
                if (!locations.isEmpty()) {
                    locationIds = locations.stream()
                            .map(EdsLocation::getObjectID)
                            .collect(Collectors.toCollection(ArrayList::new));
                }
                if (item.getCategory() != null) {
                    ProductSelectItem pItem = new ProductSelectItem(item.getObjectID(), name, item.getDescription(),
                            item.getProductType(), item.isPurchasedFromSupplier(), item.getBrand() != null ? item.getBrand().getObjectID() : null, item.getBrand() != null ? item.getBrand().getName() : "", item.getSellingPrice());
                    pItem.setQtyOnHand(item.getItemsInStock());
                    pItem.setInventoryTrackingEnabled(item.getInventoryTrackingEnabled());
                    pItem.setDiscountAmount(item.getDiscountAmount());
                    pItem.setDiscountType(item.getDiscountType());
                    pItem.setLocationIds(locationIds);
                    items.add(pItem);
                } else {
                    ProductSelectItem pItem = new ProductSelectItem(item.getObjectID(), name, item.getProductType(), item.isPurchasedFromSupplier(), item.getBrand() != null ? item.getBrand().getObjectID() : null, item.getBrand() != null ? item.getBrand().getName() : "", item.getSellingPrice());
                    pItem.setDescription(item.getDescription());
                    pItem.setQtyOnHand(item.getItemsInStock());
                    pItem.setDiscountAmount(item.getDiscountAmount());
                    pItem.setDiscountType(item.getDiscountType());
                    pItem.setInventoryTrackingEnabled(item.getInventoryTrackingEnabled());
                    pItem.setLocationIds(locationIds);
                    items.add(pItem);
                }
            }
        }

        ProductSelectItem[] productSelectItems = new ProductSelectItem[items.size()];
        int i = 0;
        ArrayList<String> existNames = new ArrayList<>();
        for (ProductSelectItem item : items) {
            if (existNames.contains(item.getName())) {
                item.setName(item.getName() + " (" + item.getId() + ")");
            }
            existNames.add(item.getName());
            productSelectItems[i++] = item;

        }

        return productSelectItems;
    }

    private String getCustomFieldValueByCode(String columnCode, ArrayList<CompanyCustomFieldItem> list) {
        for (CompanyCustomFieldItem ccfi : list) {
            if (ccfi.getColumnCode().equals(columnCode)) {
                return ccfi.getFieldStringValue();
            }
        }

        return null;
    }


    @Transactional
    public Boolean deleteProductPicture(Integer productPictureId) {
        EdsProductPicture picture = productPictureManager.get(productPictureId);

        if (picture.getProduct() != null)
            picture.getProduct().setPicture(null);

        if (picture.isDefaultPicture() != null && picture.isDefaultPicture()) {
            return true;
        }

        if (picture.getProduct() != null) {
            EdsItem item = itemManager.get(picture.getProduct().getObjectID());
            Long count = productPictureManager.getProductPictureCount(item);
            if (count != null && count == 1) {
                EdsProductPicture productPicture = productPictureManager.getProductPictures(item).get(0);
                if (Objects.equals(picture.getObjectID(), productPicture.getObjectID())) {
                    List<Integer> subPictures = productPictureManager.getProductSubPictures2(productPictureId);
                    for (Integer subPicture : subPictures) {
                        productPictureManager.delete(productPictureManager.get(subPicture));

                    }
                    productPictureManager.delete(picture);
                }
            } else {
                List<Integer> subPictures = productPictureManager.getProductSubPictures2(productPictureId);
                for (Integer subPicture : subPictures) {
                    productPictureManager.deleteButKeepFile(productPictureManager.get(subPicture));
                }
                productPictureManager.deleteButKeepFile(picture);
            }
        } else {
            List<Integer> subPictures = productPictureManager.getProductSubPictures2(productPictureId);
            for (Integer subPicture : subPictures) {
                productPictureManager.delete(productPictureManager.get(subPicture));
            }
            productPictureManager.delete(picture);
        }

        return false;
    }


    public ProductPicture[] getProductPictures(Integer productID, Integer fileSizeType) {
        List<EdsProductPicture> pictures = productPictureManager.getProductPictures(productID != null ? itemManager.get(productID) : null, fileSizeType);
        ProductPicture[] result = new ProductPicture[pictures.size()];
        int i = 0;
        for (EdsProductPicture picture : pictures) {
            result[i] = getPictureAsRPC(picture);
            i++;
        }
        return result;
    }

    @Override
    public ProductPicture getProductPictureByID(Integer productPictureID) {
        return getPictureAsRPC(productPictureManager.get(productPictureID));
    }

    private ProductPicture getPictureAsRPC(EdsProductPicture picture) {
        ProductPicture result = new ProductPicture();
        result.setName(!"".equals(picture.getName()) && picture.getName() != null ? picture.getName() : "picture");
        result.setPictureID(picture.getObjectID());
        if (picture.getProduct() != null) {
            result.setProductID(picture.getProduct().getObjectID());
        }
        result.setDefaultPicture(picture.isDefaultPicture() != null ? picture.isDefaultPicture() : false);
        String url = uploadManager.getFileURL(picture);
        result.setUrl(url);
        return result;
    }


    public Boolean setDefaultProductPicture(Integer productPictureId, Integer productID) {
        List<EdsProductPicture> pictures = productPictureManager.getProductPictures(itemManager.get(productID), null);
        EdsProductPicture edsProductPicture = productPictureManager.get(productPictureId);
        for (EdsProductPicture picture : pictures) {
            if (picture.getOriginalName().equals(edsProductPicture.getOriginalName())) {
                picture.setDefaultPicture(true);
                picture.setLastUpdateTime(new Date());
            } else {
                picture.setDefaultPicture(false);
            }
            productPictureManager.update(picture);

            if (picture.getProduct() != null) {
                picture.getProduct().setPicture(picture);
            }
        }
        return true;
    }


    @Override
    public SelectItem[] getProductColumns() {
        LinkedList<SelectItem> columns = new LinkedList<>();
        int i = 0;

        columns.add(new SelectItem(i++, "Number", "PRODUCT_NUMBER"));
        columns.add(new SelectItem(i++, "Name", "PRODUCT_NAME"));
        columns.add(new SelectItem(i++, "Description", "DESCRIPTION"));
        columns.add(new SelectItem(i++, "Product Type", "PRODUCT_TYPE_NAME"));
        columns.add(new SelectItem(i++, "Category", "CATEGORY"));
        columns.add(new SelectItem(i++, "Tax Rate", "TAXRATE"));
        columns.add(new SelectItem(i++, "Internal SKU Number", "SKU_NUMBER"));
        columns.add(new SelectItem(i++, "UPC Number", "UPC_NUMBER"));
        columns.add(new SelectItem(i++, "Part Number", "PART_NUMBER"));
        columns.add(new SelectItem(i++, "Unit Measurement"));
        columns.add(new SelectItem(i++, "Vendor", "VENDOR"));
        columns.add(new SelectItem(i++, "Manufacturer", "MANUFACTURER"));
        columns.add(new SelectItem(i++, "Cost Price", "COSTPRICE"));
        columns.add(new SelectItem(i++, "Selling Price", "UNITPRICE"));
        columns.add(new SelectItem(i++, "Income Account", "ACCOUNT_NAME"));
        columns.add(new SelectItem(i++, "COGS Account"));
        columns.add(new SelectItem(i++, "Asset Account"));

        EdsCompany company = userManager.getUser().getCompany();

        ServerSecurityContext.getInstance().setCompanyId(company.getParentCompanyId());
        List<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.ProductServiceView);

        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            for (CompanyCustomFieldItem customFieldItem : customFieldItems) {
                columns.add(new SelectItem(i++, customFieldItem.getFieldName(), customFieldItem.getColumnCode()));
            }
        }

        ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());
        return columns.toArray(new SelectItem[]{});
    }

    @Override
    public boolean saveProductItemCellValue(ProductItem rowValue, String columnCodeName) {
        EdsItem item = itemManager.get(rowValue.getObjectId());
        try {
            EdsItemCustomFields productCF = item.getCustomFields();
            if (productCF == null) {
                productCF = new EdsItemCustomFields();
                itemCFManager.create(productCF);
                item.setCustomFields(productCF);
            }

            if (rowValue.getCustomFieldMap() != null) {
                List<CompanyCustomFieldItem> companyCustomFieldsSettings = commonService.getCompanyCustomFieldsByColumnCode(ViewName.ProductServiceView, columnCodeName);
                if (companyCustomFieldsSettings != null && !companyCustomFieldsSettings.isEmpty()) {
                    CompanyCustomFieldItem cfItem = companyCustomFieldsSettings.get(0);
                    if (DATA_TYPE_TEXT.equals(cfItem.getDataType())) {
                        String oldString = productCF.getStringValue(cfItem.getColumnCode()) != null ? productCF.getStringValue(cfItem.getColumnCode()) : "";
                        if (!oldString.equals(rowValue.getCustomFieldMap().get(columnCodeName))) {
                            item.addHistoryChange(cfItem.getFieldName(), oldString, rowValue.getCustomFieldMap().get(columnCodeName), oldString == null ? userManager.getUser() : item.getCreator());
                        }
                    } else if (DATA_TYPE_NUMBER.equals(cfItem.getDataType())) {
                        Double oldNumber = productCF.getDoubleValue(cfItem.getColumnCode());
                        String oldNumberString = oldNumber != null ? String.valueOf(oldNumber) : "";
                        String newNumber = rowValue.getCustomFieldMap().get(columnCodeName) == null ? "" : String.valueOf(Double.parseDouble((String) rowValue.getCustomFieldMap().get(columnCodeName)));
                        if (!oldNumberString.equals(newNumber)) {
                            item.addHistoryChange(cfItem.getFieldName(), oldNumberString, newNumber, oldNumber == null ? userManager.getUser() : item.getCreator());
                        }
                    } else if (DATA_TYPE_DATE.equals(cfItem.getDataType())) {
                        Date oldDate = productCF.getDateValue(cfItem.getColumnCode());
                        DateNonConvertable cnc = (DateNonConvertable) rowValue.getCustomFieldMap().get(columnCodeName);
                        Date newDate = cnc != null ? cnc.getNonConvertedDate() : null;
                        if ((oldDate != null && newDate != null && (oldDate.after(newDate) || oldDate.before(newDate))) ||
                                (oldDate != null && newDate == null) || (oldDate == null && newDate != null)) {
                            item.addHistoryChange(cfItem.getFieldName(), oldDate, newDate, oldDate == null ? userManager.getUser() : item.getCreator());
                        }
                    }
                }
            }

            CustomFieldsUtils.setDomenObjectFieldChange(productCF, rowValue.getCustomFieldMap(), columnCodeName);

            try {
                itemManager.createOrUpdate(item);
                productsServicesSolrComponent.index(item);
            } catch (SolrException e) {
                log.error(e.getMessage());
            }

            return true;
        } catch (Exception e) {
            log.error("Product/Service List Edit Cell Column Code :" + columnCodeName, e);
            return false;
        }
    }


    @Override
    public boolean hasServicesIncluded(QuantityItem[] quantityItems) {
        boolean hasServicesIncluded = false;
        if (quantityItems == null || quantityItems.length == 0) {
            return hasServicesIncluded;
        }

        List<Integer> ids = Arrays.stream(quantityItems).filter(quantityItem -> quantityItem.getQuantity().compareTo(BigDecimal.ZERO) > 0).map(QuantityItem::getId).toList();
        List<EdsItem> products = itemManager.getItemsByIds(StringUtils.join(ids, ",")); // todo as ids can be null

        if (products.isEmpty()) {
            return hasServicesIncluded;
        }

        for (EdsItem product : products) {
            if (!(INVENTORY_ITEM.equals(product.getType()) || ASSEMBLY_ITEM.equals(product.getType()))) {
                if (PRODUCT_GROUP.equals(product.getType())) {
                    hasServicesInProductKit(product, hasServicesIncluded);
                } else {
                    hasServicesIncluded = true;
                    break;
                }
            }
        }
        return hasServicesIncluded;
    }

    @Override
    public NewProduct getRentalProductEditData(Integer productId, Boolean isFromExisting) {

        NewProduct product = new NewProduct();
        product.setNumberData(generateProductNumber());

        if (isFromExisting) {
            product.setObjectId(null);
            product.setNumberData(generateProductNumber());
        }
        product.setBrands(accountingServiceLocal.getBrandsAsSelectItem());
        product.setTaxList(getCompanyTaxList(new ListingFilterParameter()));


        EdsAccount sales = accountingManager.getAccountTypeWithMinCode(EdsAccountType.SALES);
        EdsAccount costOfSales = accountingManager.getAccountTypeWithMinCode(EdsAccountType.COST_OF_SALES);

        List<EdsAccount> costOfSalesList = accountingManager.getAccountsByType(EdsAccountType.COST_OF_SALES);
        List<EdsAccount> edsAccountList = accountingManager.getAccountsByCategory(EdsAccountType.EXPENSES, EdsAccountType.FIXED_ASSET);
        if (sales != null && sales.getObjectID() != null) {
            product.setDefaultReceivableAccount(sales.createAccountItem());
        }
        if (costOfSales != null && costOfSales.getObjectID() != null) {
            product.setDefaultPayableAccount(costOfSales.createAccountItem());
        }
        ArrayList<SelectItem> accountItemList = new ArrayList<>();
        if (edsAccountList != null && !edsAccountList.isEmpty()) {
            for (EdsAccount edsAccount : edsAccountList) {
                accountItemList.add(edsAccount.createAccountItem());
            }
        }
        product.setAccountItemList(accountItemList);

        ArrayList<SelectItem> costOfSalesAccountItemList = new ArrayList<>();
        if (costOfSalesList != null && !costOfSalesList.isEmpty()) {
            for (EdsAccount edsAccount : costOfSalesList) {
                costOfSalesAccountItemList.add(edsAccount.createAccountItem());
            }
        }
        product.setCostOfSalesAccountItemList(costOfSalesAccountItemList);
        EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();
        if (numberingSettings != null) {
            product.setBarcodeNumberingEnabled(numberingSettings.getBarcodeNumbering());
        }
        product.setCurrencies(currencyService.getCurrencies(true, false));
        product.setProductCategories(accountingService.getCategoriesAsSelectItem());


        if (productId != null) {
            EdsItem item = itemManager.get(productId);

            product.setObjectId(productId);
            product.setObjectKey(item.getObjectKey());
            product.setType(item.getType());
            product.setTypeName(item.getTypeName());
            String productNumber = item.getProductNumber();
            product.getNumberData().setNumberString(productNumber);
            if (product.isBarcodeNumberingEnabled()) {
                product.setBarcodeChecksum(item.getBarcodeChecksum() != null ? item.getBarcodeChecksum() : "");
                productNumber = productNumber.substring(0, productNumber.length() - product.getBarcodeChecksum().length());
            }
            product.getNumberData().setIntNumber(item.getIntNumber());
            if (item.getIntNumber() != null) {
                DecimalFormat numberFormatForParse = new DecimalFormat("0000");
                String fourDigitNumberAsString = numberFormatForParse.format(item.getIntNumber());
                String lastFourCharacter = "";
                if (productNumber.length() >= fourDigitNumberAsString.length()) {
                    lastFourCharacter = productNumber.substring(productNumber.length() - fourDigitNumberAsString.length());
                }
                if (productNumber != null && productNumber.length() > 4 && fourDigitNumberAsString.equals(lastFourCharacter)) {
                    product.getNumberData().setFirstNumberString(productNumber.substring(0, productNumber.length() - fourDigitNumberAsString.length()));
                } else {
                    product.getNumberData().setFirstNumberString(item.getProductNumber());
                }
            } else {
                product.getNumberData().setFirstNumberString(item.getProductNumber());
            }
            product.setIntNumber(item.getIntNumber());
            product.setItemName(item.getName());
            product.setDescription(item.getDescription());
            product.setUnitPrice(item.getUnitPrice());
            product.setSellingPrice(item.getSellingPrice());
            product.setBarCodeText(item.getBarCode());
            product.setActive(item.isActive());
            product.setLastUpdateTime(item.getLastUpdateTime());
            product.setCreatedDate(item.getCreationTime());
            product.setCurrencyId(item.getCurrency() != null ? item.getCurrency().getObjectID() : null);
            BigDecimal cost = getAverageCost(item);
            product.setAverageCost(cost != null ? cost : item.getUnitPrice());
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.RentalProductsView);
            product.setProductCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(item.getCustomFields(), customFieldsItems));

            product.setTemplates(getPdfTemplates(PdfReferenceCodeNameEnum.RENTAL_PRODUCT.name()).getItems());

            if (item.getQRCodeSizeID() != null) {
                product.setQRCodeSizeID(item.getQRCodeSizeID());
            }
            if (item.getBarcodeFile() != null) {
                product.setBarcodeID(item.getBarcodeFile().getObjectID());
            }
            if (item.getCategory() != null) {
                product.setCategoryID(item.getCategory().getObjectID());
                product.setCategoryName(item.getCategory().getName());
            }

            if (item.getParent() != null) {
                product.setParentId(item.getParent().getObjectID());
            }

            if (item.getAccount() != null) {
                EdsAccount account = item.getAccount();
                product.setAccountId(account.getObjectID());
                product.setAccountItem(account.createAccountItem());
            }

            if (item.getCogsAccount() != null) {
                EdsAccount account = item.getCogsAccount();
                product.setCogsAccountID(account.getObjectID());
                product.setCogsAccount(account.createAccountItem());
            }

            if (item.getAssetAccount() != null) {
                EdsAccount account = item.getAssetAccount();
                product.setAssetAccountID(account.getObjectID());
                product.setAssetAccount(account.createAccountItem());
            }

            if (item.getBrand() != null) {
                product.setBrandID(item.getBrand().getObjectID());
                product.setBrandName(item.getBrand().getName());
            }

            if (item.getVat() != null) {
                product.setTaxItem(item.getVat().createTaxItem());
                product.setTaxIDs(new Integer[]{item.getVat().getObjectID()});
                product.setVatId(item.getVat().getObjectID());
                product.setEffectiveTaxRate(item.getVat().getEffectiveTaxRate());
            } else {
                product.setEffectiveTaxRate(Double.valueOf("0"));
            }

            if (item.getSuppliers() != null && !item.getSuppliers().isEmpty()) {
                ArrayList<SelectItem> suppliers = new ArrayList<>();
                for (EdsCrmAccount supplier : item.getSuppliers()) {
                    suppliers.add(supplier.getAsSelectItem());
                }
                product.setSuppliers(suppliers.toArray(new SelectItem[]{}));
            }
            product.setExtraHour(item.getExtraHour());
            product.setExtraDay(item.getExtraDay());
            product.setSecurityTime(item.getSecurityTime());
            if (!CollectionUtils.isEmpty(item.getRentalItems())) {
                product.setRentalProductItems(new ArrayList<>(item.getRentalItems().stream().map(EdsRentalProductItem::toDTO).collect(Collectors.toList())));
            }
        }
        return product;
    }

    private CustomFormItemPdfTemplateList getPdfTemplates(String type) {
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


    private void hasServicesInProductKit(EdsItem productKit, Boolean hasServicesIncluded) {

        for (EdsProductKitItems kitItem : productKit.getProductKitItems()) {
            EdsItem product = kitItem.getItem();

            if (!(INVENTORY_ITEM.equals(product.getType()) || ASSEMBLY_ITEM.equals(product.getType()))) {

                if (PRODUCT_KIT.equals(product.getType())) {
                    hasServicesInProductKit(product, hasServicesIncluded);
                } else {
                    hasServicesIncluded = true;
                    return;
                }
            }
        }
    }

    @Override
    public void updateActive(List<Integer> ids, boolean active) {
        itemManager.updateActive(ids, active);
        String commaSeparatedIds = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        List<EdsItem> items = itemManager.getItemsByIds(commaSeparatedIds);
        try {
            productsServicesSolrComponent.indexes(items);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    // ── AI Image Studio (Nano Banana) ────────────────────────────────────────

    @Override
    public boolean isAiAvailable() {
        return nanoBananaClient.isAvailable();
    }

    @Override
    public AiImageResult processAiImage(Integer pictureId, String prompt, String actionType, String aspectRatio) {
        AiImageResult result = new AiImageResult();

        if (!nanoBananaClient.isAvailable()) {
            result.setAiAvailable(false);
            result.setErrorMessage("Currently unavailable, our team is working on resolving the case");
            return result;
        }

        EdsProductPicture picture = productPictureManager.get(pictureId);
        if (picture == null) {
            result.setAiAvailable(false);
            result.setErrorMessage("Image not found");
            return result;
        }

        var imageUrl = getImageUrl(picture);
        byte[] imageBytes;
        try {
            imageBytes = IOUtils.toByteArray(new java.net.URL(imageUrl).openStream());
        } catch (Exception e) {
            log.error("Failed to read image bytes from url=" + imageUrl, e);
            result.setAiAvailable(false);
            result.setErrorMessage("Failed to read image data");
            return result;
        }

        // crop to the selected aspect ratio before sending to AI
        if (aspectRatio != null && !aspectRatio.isEmpty() && !"free".equals(aspectRatio)) {
            try {
                imageBytes = cropToAspectRatio(imageBytes, aspectRatio);
            } catch (Exception e) {
                log.warn("Failed to crop image to ratio " + aspectRatio + ": " + e.getMessage());
            }
        }

        String filename = picture.getOriginalName() != null ? picture.getOriginalName() : "image.jpg";
        List<ProductPicture> generatedPictures = new ArrayList<>();

        try {
            if ("ENHANCE".equals(actionType)) {
                byte[] resultBytes = nanoBananaClient.enhanceImage(imageBytes, filename);
                if (resultBytes == null) {
                    result.setAiAvailable(false);
                    result.setErrorMessage("Currently unavailable, our team is working on resolving the case");
                    return result;
                }
                generatedPictures.add(createGeneratedPicture(picture, resultBytes, mimeFromFilename(filename)));
            } else if ("REMOVE_BG".equals(actionType)) {
                List<String> dataUrls = nanoBananaClient.processWithPrompt(imageBytes, filename,
                        "Remove the background from this product image and make it transparent or white. Keep only the product.");
                if (dataUrls == null) {
                    result.setAiAvailable(false);
                    result.setErrorMessage("Currently unavailable, our team is working on resolving the case");
                    return result;
                }
                for (String dataUrl : dataUrls) {
                    generatedPictures.add(createGeneratedPicture(picture, bytesFromDataUrl(dataUrl), mimeFromDataUrl(dataUrl)));
                }
            } else if ("PROMPT".equals(actionType) && prompt != null && !prompt.isEmpty()) {
                List<String> dataUrls = nanoBananaClient.processWithPrompt(imageBytes, filename, prompt);
                if (dataUrls == null) {
                    result.setAiAvailable(false);
                    result.setErrorMessage("Currently unavailable, our team is working on resolving the case");
                    return result;
                }
                for (String dataUrl : dataUrls) {
                    generatedPictures.add(createGeneratedPicture(picture, bytesFromDataUrl(dataUrl), mimeFromDataUrl(dataUrl)));
                }
            } else if ("ROTATE_RIGHT".equals(actionType)) {
                byte[] resultBytes = nanoBananaClient.rotateRight(imageBytes, filename);
                if (resultBytes == null) {
                    result.setAiAvailable(false);
                    result.setErrorMessage("Currently unavailable, our team is working on resolving the case");
                    return result;
                }
                generatedPictures.add(createGeneratedPicture(picture, resultBytes, mimeFromFilename(filename)));
            } else if ("ROTATE_LEFT".equals(actionType)) {
                byte[] resultBytes = nanoBananaClient.rotateLeft(imageBytes, filename);
                if (resultBytes == null) {
                    result.setAiAvailable(false);
                    result.setErrorMessage("Currently unavailable, our team is working on resolving the case");
                    return result;
                }
                generatedPictures.add(createGeneratedPicture(picture, resultBytes, mimeFromFilename(filename)));
            } else if ("ROTATE_180".equals(actionType)) {
                byte[] once = nanoBananaClient.rotateRight(imageBytes, filename);
                if (once == null) {
                    result.setAiAvailable(false);
                    result.setErrorMessage("Currently unavailable, our team is working on resolving the case");
                    return result;
                }
                byte[] twice = nanoBananaClient.rotateRight(once, filename);
                if (twice == null) {
                    result.setAiAvailable(false);
                    result.setErrorMessage("Currently unavailable, our team is working on resolving the case");
                    return result;
                }
                generatedPictures.add(createGeneratedPicture(picture, twice, mimeFromFilename(filename)));
            }
        } catch (Exception e) {
            log.error("Nano Banana processing failed for pictureId=" + pictureId, e);
            result.setAiAvailable(false);
            result.setErrorMessage("Currently unavailable, our team is working on resolving the case");
            return result;
        }

        result.setGeneratedPictures(generatedPictures);
        return result;
    }

    @Override
    public ProductPicture saveAiGeneratedImage(Integer pictureId) {
        EdsProductPicture picture = productPictureManager.get(pictureId);
        if (picture == null) return null;
        return getPictureAsRPC(picture);
    }

    private ProductPicture createGeneratedPicture(EdsProductPicture original, byte[] bytes, String mimeType) {
        String ext = mimeToExt(mimeType);
        String originalName = original.getOriginalName() != null ? original.getOriginalName() : "image" + ext;
        int dotIndex = originalName.lastIndexOf('.');
        String baseName = dotIndex >= 0 ? originalName.substring(0, dotIndex) : originalName;
        String newName = baseName + "_kpi.com_ai_generated" + ext;

        EdsProductPicture newPicture = new EdsProductPicture();
        newPicture.setProduct(original.getProduct());
        newPicture.setOriginalName(newName);
        newPicture.setName(newName);
        newPicture.setContentType(mimeType);
        newPicture.setFileSizeType(FILE_SIZE_DEFAULT);
        newPicture.setDefaultPicture(false);
        newPicture.setInputStream(new ByteArrayInputStream(bytes));
        newPicture.setCreatedBy(productPictureManager.getUser());

        productPictureManager.create(newPicture);
        return getPictureAsRPC(newPicture);
    }

    private byte[] cropToAspectRatio(byte[] imageBytes, String aspectRatio) throws Exception {
        String[] parts = aspectRatio.split(":");
        if (parts.length != 2) return imageBytes;
        double targetW = Double.parseDouble(parts[0].trim());
        double targetH = Double.parseDouble(parts[1].trim());
        if (targetW <= 0 || targetH <= 0) return imageBytes;

        java.awt.image.BufferedImage src = javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(imageBytes));
        if (src == null) return imageBytes;

        int srcW = src.getWidth();
        int srcH = src.getHeight();
        double srcRatio = (double) srcW / srcH;
        double dstRatio = targetW / targetH;

        int cropW, cropH;
        if (srcRatio > dstRatio) {
            // original is wider than target — trim sides
            cropH = srcH;
            cropW = (int) Math.round(srcH * dstRatio);
        } else {
            // original is taller than target — trim top/bottom
            cropW = srcW;
            cropH = (int) Math.round(srcW / dstRatio);
        }

        int x = (srcW - cropW) / 2;
        int y = (srcH - cropH) / 2;
        java.awt.image.BufferedImage cropped = src.getSubimage(x, y, cropW, cropH);

        String formatName = "jpg";
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        javax.imageio.ImageIO.write(cropped, formatName, out);
        return out.toByteArray();
    }

    private byte[] bytesFromDataUrl(String dataUrl) {
        String data = dataUrl.contains(",") ? dataUrl.substring(dataUrl.indexOf(',') + 1) : dataUrl;
        return java.util.Base64.getDecoder().decode(data);
    }

    private String mimeFromDataUrl(String dataUrl) {
        if (dataUrl.startsWith("data:") && dataUrl.contains(";")) {
            return dataUrl.substring(5, dataUrl.indexOf(';'));
        }
        return "image/jpeg";
    }

    private String mimeToExt(String mimeType) {
        if (mimeType == null) return ".jpg";
        return switch (mimeType) {
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };
    }

    @Override
    public AiSavedPrompt[] getSavedAiPrompts() {
        EdsUser user = aiSavedPromptManager.getUser();
        if (user == null) return new AiSavedPrompt[0];
        List<EdsAiSavedPrompt> entities = aiSavedPromptManager.getByUser(user);
        AiSavedPrompt[] result = new AiSavedPrompt[entities.size()];
        for (int i = 0; i < entities.size(); i++) {
            EdsAiSavedPrompt e = entities.get(i);
            result[i] = new AiSavedPrompt(e.getObjectID(), e.getPromptText());
        }
        return result;
    }

    @Override
    public void saveAiPrompt(String promptText) {
        if (promptText == null || promptText.trim().isEmpty()) return;
        EdsUser user = aiSavedPromptManager.getUser();
        EdsAiSavedPrompt entity = new EdsAiSavedPrompt();
        entity.setPromptText(promptText.trim());
        entity.setUser(user);
        aiSavedPromptManager.create(entity);
    }

    @Override
    public void deleteAiPrompt(Integer promptId) {
        if (promptId == null) return;
        EdsAiSavedPrompt entity = aiSavedPromptManager.get(promptId);
        if (entity != null) {
            entity.setDeleted(true);
            aiSavedPromptManager.update(entity);
        }
    }

    @Override
    public String[] getAiSuggestedPrompts() {
        return new String[]{
                "Remove background and place on white",
                "Remove background and make transparent",
                "Improve lighting and make it look professional",
                "Enhance image quality and sharpness",
                "Make the product stand out with better contrast",
                "Add a clean studio background",
                "Fix shadows and improve overall quality"
        };
    }

    private String mimeFromFilename(String filename) {
        if (filename != null) {
            String lower = filename.toLowerCase();
            if (lower.endsWith(".png")) return "image/png";
            if (lower.endsWith(".gif")) return "image/gif";
            if (lower.endsWith(".webp")) return "image/webp";
        }
        return "image/jpeg";
    }
}
