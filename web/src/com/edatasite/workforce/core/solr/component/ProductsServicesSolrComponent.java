package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsItemCustomFields;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.solr.document.ProductsServicesSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.ProductsServicesSolrDocRepository;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSolrItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProductServiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunitySolrItem;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollAsyncService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem.DISCOUNT_TYPE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_PRODUCTS_SERVICES_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.WAREHOUSE_ID;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:30.
 */
@Component
public class ProductsServicesSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(ProductsServicesSolrComponent.class);

    @Autowired
    private ProductsServicesSolrDocRepository productsServicesSolrDocRepository;
    @Autowired
    private ItemStockManager itemStockManager;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private PayrollAsyncService payrollAsyncService;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsItem edsItem) throws Exception {
        this.indexes(Collections.singletonList(edsItem));
    }

    @Transactional
    public void indexes(List<EdsItem> edsItems) throws Exception {

        Integer companyId = SecurityContext.getCompanyID();
        boolean isCustomSubItemsEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CUSTOM_INVOICE_ITEM_PRODUCT_DESCRIPTION_ENABLED);
        if (!CollectionUtils.isEmpty(edsItems)) {
            List<ProductsServicesSolrDoc> productsServicesSolrDocs = new ArrayList<>();

            for (EdsItem edsItem : edsItems) {
                if (edsItem != null) {
                    try {
                        ProductsServicesSolrDoc productServicesDocument = createProductServicesDocument(edsItem, companyId, isCustomSubItemsEnabled);
                        ItemStockManager itemStockManager = ApplicationContextProvider.applicationContext.getBean("itemStockManager", ItemStockManager.class);
                        List<StockItem> stockItems = itemStockManager.getWarehouseStocks(edsItem.getObjectID());

                        if (!CollectionUtils.isEmpty(stockItems)) {
                            stockItems.forEach(stock -> {
                                ProductsServicesSolrDoc warehouseDoc = new ProductsServicesSolrDoc();
                                warehouseDoc.setDoctype(SolrProductServiceRepresenter.WAREHOUSE_SOLR_DOC);
                                warehouseDoc.setCompanyId(SecurityContext.getCompanyID());
                                warehouseDoc.setOid(SecurityContext.getCompanyID() + "_" + edsItem.getObjectID() + "_" + (stock.getWarehouseID() != null ? stock.getWarehouseID() : 1));
                                warehouseDoc.setProductId(edsItem.getObjectID());
                                warehouseDoc.setWarehouseId(stock.getWarehouseID());
                                warehouseDoc.setWarehouseName(stock.getWarehouseName());
                                warehouseDoc.setWarehouseStock(stock.getQuantity().doubleValue());
                                productServicesDocument.getWarehouses().add(warehouseDoc);
                            });
                        }
                        productsServicesSolrDocs.add(productServicesDocument);

                        log.info("Indexed ProductServices Core CID - {}, objId - {}", companyId, edsItem.getObjectID());
                    } catch (Exception e) {
                        log.error("********************* Item = {} **********************", edsItem.getName());
                        throw e;
                    }
                }
            }
            if (!productsServicesSolrDocs.isEmpty()) {
                log.info("========= Create products services solr docs for company {} with size {} =========", companyId, productsServicesSolrDocs.size());
                productsServicesSolrDocRepository.saveAll(productsServicesSolrDocs);
            }
        }

    }

    @Transactional
    public void indexConcurrently(List<EdsItem> edsItems) throws InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        boolean isCustomSubItemsEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CUSTOM_INVOICE_ITEM_PRODUCT_DESCRIPTION_ENABLED);
        if (!CollectionUtils.isEmpty(edsItems)) {
            ConcurrentLinkedQueue<ProductsServicesSolrDoc> productsServicesSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();

            for (EdsItem edsItem : edsItems) {
                if (edsItem != null) {
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companId);
                            synchronized (this) {
                                ProductsServicesSolrDoc productServicesDocument = createProductServicesDocument(edsItem, companyId, isCustomSubItemsEnabled);
                                        ItemStockManager itemStockManager = ApplicationContextProvider.applicationContext.getBean("itemStockManager", ItemStockManager.class);
                                        List<StockItem> stockItems = itemStockManager.getWarehouseStocks(edsItem.getObjectID());

                                        if (!CollectionUtils.isEmpty(stockItems)) {
                                            stockItems.forEach(stock -> {
                                                ProductsServicesSolrDoc warehouseDoc = new ProductsServicesSolrDoc();
                                                warehouseDoc.setDoctype(SolrProductServiceRepresenter.WAREHOUSE_SOLR_DOC);
                                                warehouseDoc.setCompanyId(SecurityContext.getCompanyID());
                                                warehouseDoc.setOid(SecurityContext.getCompanyID() + "_" + edsItem.getObjectID() + "_" + (stock.getWarehouseID() != null ? stock.getWarehouseID() : 1));
                                                warehouseDoc.setProductId(edsItem.getObjectID());
                                                warehouseDoc.setWarehouseId(stock.getWarehouseID());
                                                warehouseDoc.setWarehouseName(stock.getWarehouseName());
                                                warehouseDoc.setWarehouseStock(stock.getQuantity().doubleValue());
                                                productServicesDocument.getWarehouses().add(warehouseDoc);
                                            });
                                        }
                                        productsServicesSolrDocs.add(productServicesDocument);

                                        log.info("Indexed ProductServices Core CID - {}, objId - {}", companyId, edsItem.getObjectID());
                                    }
                        } catch (Exception e) {
                            log.error("********************* Item = {} **********************", edsItem.getName());
                        }
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
                log.error("Error on loading Product Items list", e);
            }

            if (!productsServicesSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create products services solr docs for company {} with size {} =========", companyId, productsServicesSolrDocs.size());
                    productsServicesSolrDocRepository.saveAll(productsServicesSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving products list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(EdsItem item) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + item.getObjectID();
    }

    private ProductsServicesSolrDoc createProductServicesDocument(EdsItem edsItem, Integer companyId, boolean isCustomSubItemsEnabled) {
        ProductsServicesSolrDoc productsServicesSolrDoc = new ProductsServicesSolrDoc();
        productsServicesSolrDoc.setDoctype(SolrProductServiceRepresenter.PRODUCT_SOLR_DOC);
        productsServicesSolrDoc.setOid(SolrUtils.generatedOId(companyId, edsItem.getObjectID()));
        productsServicesSolrDoc.setCompanyId(companyId);
        productsServicesSolrDoc.setProductId(edsItem.getObjectID());
        productsServicesSolrDoc.setProductNumber(edsItem.getProductNumber());
        productsServicesSolrDoc.setProductName(edsItem.getName());
        productsServicesSolrDoc.setProductTypeId(edsItem.getType());
        productsServicesSolrDoc.setProductTypeName(edsItem.getTypeName());
        productsServicesSolrDoc.setProductTypeIdName(SolrUtils.getIdName(edsItem.getType(), edsItem.getTypeName()));
        productsServicesSolrDoc.setProductActive(edsItem.isActive());
        productsServicesSolrDoc.setProductStorefrontEnable(edsItem.isStorefrontEnable());
        productsServicesSolrDoc.setPartNumber(edsItem.getPartNumber());
        productsServicesSolrDoc.setSkuNumber(edsItem.getInternalSKUNumber());
        productsServicesSolrDoc.setUpsNumber(edsItem.getUpcNumber());
        productsServicesSolrDoc.setManufacturer(edsItem.getManufacturer());
        productsServicesSolrDoc.setSubsidiaryProductUniqNum(edsItem.getSubsidiaryProductUniqNum());
        productsServicesSolrDoc.setBarcode(edsItem.getBarCode());
        productsServicesSolrDoc.setCreatedDate(edsItem.getCreationTime());
        productsServicesSolrDoc.setUpdatedDate(edsItem.getLastUpdateTime());
        productsServicesSolrDoc.setQuantityOnHand(edsItem.getItemsInStock() != null ? edsItem.getItemsInStock().doubleValue() : null);
        productsServicesSolrDoc.setAverageCost(edsItem.getAverageCost() != null ? edsItem.getAverageCost().toString() : null);
        productsServicesSolrDoc.setProductParentId(edsItem.getParent() != null ? edsItem.getParent().getObjectID() : null);
        productsServicesSolrDoc.setProductDiscountAmount(edsItem.getDiscountAmount() != null ? edsItem.getDiscountAmount().doubleValue() : 0.0);
        productsServicesSolrDoc.setProductDiscountTypeId(edsItem.getDiscountType());
        productsServicesSolrDoc.setProductDiscountTypeName(edsItem.getDiscountTypeName());

        if (edsItem.getAccount() != null) {
            productsServicesSolrDoc.setAccountId(edsItem.getAccount().getObjectID());
            productsServicesSolrDoc.setAccountName(edsItem.getAccount().getName());
            productsServicesSolrDoc.setAccountIdName(SolrUtils.getIdName(edsItem.getAccount().getObjectID(), edsItem.getAccount().getName()));
        }

        if (edsItem.getCogsAccount() != null) {
            productsServicesSolrDoc.setCogsAccountId(edsItem.getCogsAccount().getObjectID());
            productsServicesSolrDoc.setCogsAccountName(edsItem.getCogsAccount().getName());
            productsServicesSolrDoc.setCogsAccountIdName(SolrUtils.getIdName(edsItem.getCogsAccount().getObjectID(), edsItem.getCogsAccount().getName()));
        }

        if (edsItem.getAssetAccount() != null) {
            productsServicesSolrDoc.setAssetAccountId(edsItem.getAssetAccount().getObjectID());
            productsServicesSolrDoc.setAssetAccountName(edsItem.getAssetAccount().getName());
            productsServicesSolrDoc.setAssetAccountIdName(SolrUtils.getIdName(edsItem.getAssetAccount().getObjectID(), edsItem.getAssetAccount().getName()));
        }
        productsServicesSolrDoc.setDescription(edsItem.getDescription());
        if (edsItem.getSellingPrice() != null) {
            productsServicesSolrDoc.setUnitprice(edsItem.getSellingPrice().doubleValue());
        }
        if (edsItem.getUnitPrice() != null) {
            productsServicesSolrDoc.setCostprice(edsItem.getUnitPrice().doubleValue());
        }
        if (edsItem.getVat() != null) {
            productsServicesSolrDoc.setTaxrateId(edsItem.getVat().getObjectID());
            productsServicesSolrDoc.setTaxrate(edsItem.getVat().getName());
            productsServicesSolrDoc.setTaxEffectiveRate(edsItem.getVat().getEffectiveTaxRate());
        }
        if (edsItem.getUnitMeasurement() != null) {
            productsServicesSolrDoc.setUnitMeasureMentId(edsItem.getUnitMeasurement().getObjectID());
            productsServicesSolrDoc.setUnitMeasureMentName(edsItem.getUnitMeasurement().getName());
            productsServicesSolrDoc.setUnitMeasurementIdName(SolrUtils.getIdName(edsItem.getUnitMeasurement().getObjectID(), edsItem.getUnitMeasurement().getName()));
        }
        if (edsItem.getBrand() != null) {
            productsServicesSolrDoc.setBrandId(edsItem.getBrand().getObjectID());
            productsServicesSolrDoc.setBrandName(edsItem.getBrand().getName());
            productsServicesSolrDoc.setBrandIdName(SolrUtils.getIdName(edsItem.getBrand().getObjectID(), edsItem.getBrand().getName()));
        }
        if (edsItem.getSuppliers() != null) {
            edsItem.getSuppliers().forEach(edsCrmAccount -> {
                productsServicesSolrDoc.getMultiSupplierId().add(edsCrmAccount.getObjectID());

                String supplierName = edsCrmAccount.getName() != null ? edsCrmAccount.getName() : "N/A";
                productsServicesSolrDoc.getMultiSupplierName().add(supplierName);

                productsServicesSolrDoc.getMultiSupplierIdName().add(SolrUtils.getIdName(edsCrmAccount.getObjectID(), supplierName));

                String supplierNumber = edsCrmAccount.getNumber() != null ? edsCrmAccount.getNumber() : "N/A";
                productsServicesSolrDoc.getMultiSupplierNumber().add(supplierNumber);
            });

            int size = productsServicesSolrDoc.getMultiSupplierId().size();
            for (int i = 0; i < size; i++) {
                if (productsServicesSolrDoc.getMultiSupplierName().size() < size) {
                    productsServicesSolrDoc.getMultiSupplierName().add("N/A");
                }
                if (productsServicesSolrDoc.getMultiSupplierIdName().size() < size) {
                    productsServicesSolrDoc.getMultiSupplierIdName().add("N/A");
                }
                if (productsServicesSolrDoc.getMultiSupplierNumber().size() < size) {
                    productsServicesSolrDoc.getMultiSupplierNumber().add("N/A");
                }
            }
        }
        if (edsItem.getLocations() != null) {
            edsItem.getLocations().forEach(location -> {
                productsServicesSolrDoc.getMultiLocationId().add(location.getObjectID());
                productsServicesSolrDoc.getMultiLocationName().add(location.getName());
                productsServicesSolrDoc.getMultiLocationIdName().add(SolrUtils.getIdName(location.getObjectID(), location.getName()));
            });
        }
        if (edsItem.getCategory() != null) {
            productsServicesSolrDoc.setCategory(edsItem.getCategory().getName());
            productsServicesSolrDoc.setCategoryId(edsItem.getCategory().getObjectID());
        }
        if (edsItem.getCategory() != null && edsItem.getCategory().getParent() != null) {
            productsServicesSolrDoc.setParentCategory(edsItem.getCategory().getParent().getName());
            productsServicesSolrDoc.setParentCategoryId(edsItem.getCategory().getParent().getObjectID());
        }
        productsServicesSolrDoc.setInventoryTrackingEnabled(edsItem.getInventoryTrackingEnabled() != null ? edsItem.getInventoryTrackingEnabled() : false);
        productsServicesSolrDoc.setTrackBatchesEnabled(edsItem.getTrackBatchesEnabled() != null ? edsItem.getTrackBatchesEnabled() : false);
        if (edsItem.getCreator() != null) {
            productsServicesSolrDoc.setCreatorId(edsItem.getCreator().getObjectID());
            productsServicesSolrDoc.setCreatorName(edsItem.getCreator().getName());
            productsServicesSolrDoc.setCreatorIdName(SolrUtils.getIdName(edsItem.getCreator().getObjectID(), edsItem.getCreator().getName()));
        }
        if (edsItem.getUpdater() != null) {
            productsServicesSolrDoc.setCreatorId(edsItem.getUpdater().getObjectID());
            productsServicesSolrDoc.setCreatorName(edsItem.getUpdater().getName());
            productsServicesSolrDoc.setCreatorIdName(SolrUtils.getIdName(edsItem.getUpdater().getObjectID(), edsItem.getUpdater().getName()));
        }

        if (isCustomSubItemsEnabled) {
            productsServicesSolrDoc.setNewProductCustomDescriptions(getCustomDescriptionSolrData(edsItem));
        }
        CustomFieldsUtils.setSolrDocDynamicFields(productsServicesSolrDoc, edsItem.getCustomFields());
        return productsServicesSolrDoc;
    }

    private List<ProductsServicesSolrDoc.NewProductCustomDescription> getCustomDescriptionSolrData(EdsItem item) {
        return item.getCustomDescriptionData().stream()
                .map(i -> {
                    ProductsServicesSolrDoc.NewProductCustomDescription description = new ProductsServicesSolrDoc.NewProductCustomDescription();
                    description.setId(i.getId());
                    description.setProductName(i.getName());
                    description.setPrice(i.getPrice().doubleValue());
                    description.setQuantity(i.getQty().doubleValue());
                    return description;
                })
                .toList();
    }

    public FacetFilterRpc getProductServicesFacetFilterData(FacetFilterRpc productsFacet) {
        if (!productsFacet.isFilterChanges()) {
            productsFacet = commonServiceLocal.getUserFacetFilter(productsFacet);
        }
        StringBuilder solrQuery = new StringBuilder();
        EdsUser edsUser = employeeManager.getUser();
        com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(productsFacet.getSearchKey());

        if (productsFacet != null) {
            for (String key : productsFacet.getShowSolrFieldMap().keySet()) {
                if (("status").equals(key) && productsFacet.getFacetContentMap().containsKey(key)) {
                    productsFacet.getShowSolrFieldMap().get(key).setWithID(false);
                }
            }
        }

//        fp.setActive(!productsFacet.getActive());
        solrQuery.append(QueryBuilderForSolr.getProductsServicesCoreSolrQuery(fp));
        solrQuery.append(SolrFacetUtils.generateForPricesFacet(productsFacet, FacetContentType.ProductsServicesFacetFilter.getContentCode()[2]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(productsFacet, edsUser.getCompany(),
                null, null,
                FacetContentType.ProductsServicesFacetFilter.getContentCode()[2]
        ));

        String warehouseIDStr = productsFacet.getCustomData().get(WAREHOUSE_ID);
        if (warehouseIDStr != null && !"".equals(warehouseIDStr.trim())) {
            Integer warehouseID = Integer.parseInt(warehouseIDStr);
            fp.setWarehouseID(warehouseID);
            List<Integer> itemIDList = itemStockManager.getItemsInWarehouse(warehouseID);
            solrQuery.append(" AND (" + SolrProductServiceRepresenter.FIELD_PRODUCT_ID).append(":").append(ServerUtils.getAsCommoDelimited(itemIDList, "0", " ")).append(")");
        }

        String productType = productsFacet.getCustomData().get(Constants.PRODUCT_TYPE);
        if (productType != null && !"".equals(productType.trim())) {
            Integer productTypeID = Integer.parseInt(productType);
            fp.setProductType(productTypeID);
            solrQuery.append(" AND ").append(SolrProductServiceRepresenter.FIELD_PRODUCT_TYPE_ID).append(":(").append(productTypeID).append(")");
        } else {
            solrQuery.append(" AND NOT ").append(SolrProductServiceRepresenter.FIELD_PRODUCT_TYPE_ID).append(":(").append(AccountingConstants.RENTAL_ITEM).append(")");
        }

        String uniMId = productsFacet.getCustomData().get(SolrProductServiceRepresenter.FIELD_UNIT_MEASUREMENT_ID);
        if (uniMId != null && !"".equals(uniMId.trim())) {
            Integer unitMeasuremnetID = Integer.parseInt(uniMId);
            fp.setUnitMeasurementId(unitMeasuremnetID);
            solrQuery.append(" AND ").append(SolrProductServiceRepresenter.FIELD_UNIT_MEASUREMENT_ID).append(":(").append(unitMeasuremnetID).append(")");
        }

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_PRODUCTS_SERVICES_CORE, solrQuery.toString(), productsFacet, ProductsServicesSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, productsFacet);

        if (productsFacet.getFacetContentMap().containsKey(FacetContentType.ProductsServicesFacetFilter.getContentCode()[2])) {
            getProductsServicesFacetResultFromSolr(facetPage, productsFacet);
        }
        return productsFacet;
    }

    private FacetFilterRpc getProductsServicesFacetResultFromSolr(QueryResponse resp, FacetFilterRpc productsFacet) {
        FacetField amountFacet = resp.getFacetField(SolrProductServiceRepresenter.FIELD_UNITPRICE);
        if (amountFacet != null && amountFacet.getValues() != null) {
            int lessThan100 = 0, from100To1000 = 0, from1000To10000 = 0, from10000To50000 = 0, moreThan50000 = 0;
            for (FacetField.Count count : amountFacet.getValues()) {
                if (count.getName() != null) {
                    Double total = Double.parseDouble(count.getName());
                    if (total < 100) {
                        lessThan100 += count.getCount();
                    } else if (100 <= total && total <= 1000) {
                        from100To1000 += count.getCount();
                    } else if (1001 <= total && total <= 10000) {
                        from1000To10000 += count.getCount();
                    } else if (10001 <= total && total <= 50000) {
                        from10000To50000 += count.getCount();
                    } else {
                        moreThan50000 += count.getCount();
                    }
                }
            }
            SelectItem[] price = new SelectItem[5];
            price[0] = new SelectItem("[ * TO 99 ]".hashCode(), "[ * TO 99 ]");
            price[0].setDescription(commonLocalizer.localize("lessThan", "Less than") + " 100.00  ( <b>" + lessThan100 + "</b> )");

            price[1] = new SelectItem("[100 TO 1000]".hashCode(), "[100 TO 1000]");
            price[1].setDescription("100.00 - 1,000.00 ( <b>" + from100To1000 + "</b> )");

            price[2] = new SelectItem("[1001 TO 10000]".hashCode(), "[1001 TO 10000]");
            price[2].setDescription("1,001.00 - 10,000.00 ( <b>" + from1000To10000 + "</b> )");

            price[3] = new SelectItem("[10001 TO 50000]".hashCode(), "[10001 TO 50000]");
            price[3].setDescription("10,001.00 - 50,000.00 ( <b>" + from10000To50000 + "</b> )");

            price[4] = new SelectItem("[50001 TO *]".hashCode(), "[50001 TO *]");
            price[4].setDescription(commonLocalizer.localize("moreThan", "More than") + " 50,000.00 ( <b>" + moreThan50000 + "</b> )");

            productsFacet.getFacetContentMap().get(FacetContentType.ProductsServicesFacetFilter.getContentCode()[2]).setFacetItems(price);
        } else {
            productsFacet.getFacetContentMap().get(FacetContentType.ProductsServicesFacetFilter.getContentCode()[2]).setFacetItems(new SelectItem[0]);
        }
        return productsFacet;
    }

    public Page<ProductsServicesSolrDoc> getList(ListingFilterParameter filterParameter, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));
        int limit = filterParameter.getLimit() > 0 ? filterParameter.getLimit() : SOLR_LIMIT;
        if (!filterParameter.isSearchButton() && !ServerUtils.isNullOrEmpty(filterParameter.getSortField())) {
            Sort.Direction sortDirection = filterParameter.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
            Sort solrSort = Sort.by(Sort.Direction.DESC, SolrProductServiceRepresenter.FIELD_PRODUCT_ID);
            solrSort = switch (filterParameter.getSortField()) {
                case ProductItem.NAME -> Sort.by(sortDirection, SolrProductServiceRepresenter.SORTABLE_PRODUCT_NAME);
                case ProductItem.DISCRIPTION ->
                        Sort.by(sortDirection, SolrProductServiceRepresenter.SORTABLE_DESCRIPTION);
                case ProductItem.SELING_PRICE -> Sort.by(sortDirection, SolrProductServiceRepresenter.FIELD_UNITPRICE);
                case ProductItem.COST_PRICE -> Sort.by(sortDirection, SolrProductServiceRepresenter.FIELD_COSTPRICE);
                case ProductItem.TYPE ->
                        Sort.by(sortDirection, SolrProductServiceRepresenter.SORTABLE_PRODUCT_TYPE_NAME);
                case DISCOUNT_TYPE ->
                        Sort.by(sortDirection, SolrProductServiceRepresenter.SORTABLE_PRODUCT_DISCOUNT_TYPE_NAME);
                case ProductItem.ACCOUND -> Sort.by(sortDirection, SolrProductServiceRepresenter.SORTABLE_ACCOUNT_NAME);
                case ProductItem.Vendor -> Sort.by(sortDirection, SolrProductServiceRepresenter.SORTABLE_VENDOR);
                case ProductItem.Category -> Sort.by(sortDirection, SolrProductServiceRepresenter.SORTABLE_CATEGORY);
                case ProductItem.PRODUCT_NUMBER ->
                        Sort.by(sortDirection, SolrProductServiceRepresenter.SORTABLE_PRODUCT_NUMBER);
                case ProductItem.AVERAGE_COST ->
                        Sort.by(sortDirection, SolrProductServiceRepresenter.FIELD_AVERAGE_COST);
                case ProductItem.UNIT_MEASUREMENT_NAME ->
                        Sort.by(sortDirection, SolrProductServiceRepresenter.FIELD_UNIT_MEASUREMENT_NAME);
                case ProductItem.ITEMS_IN_STOCK ->
                        Sort.by(sortDirection, SolrProductServiceRepresenter.FIELD_QUANTITY_ON_HAND);
                case ProductItem.UPDATED_DATE ->
                        Sort.by(sortDirection, SolrProductServiceRepresenter.FIELD_UPDATED_DATE);
                default ->
                        CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(filterParameter.getSortField(), !filterParameter.isAscending(), true);
            };
            query.setPageRequest(PageRequest.of(filterParameter.getCurrentPage(), limit, solrSort));
        } else {
            query.setPageRequest(PageRequest.of(filterParameter.getCurrentPage(), limit));
        }

        return solrTemplate.query(SOLR_PRODUCTS_SERVICES_CORE, query, ProductsServicesSolrDoc.class);
    }
}
