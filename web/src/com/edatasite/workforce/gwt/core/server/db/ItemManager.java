package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.accounting.EdsProductWarehouseLocation;
import com.edatasite.workforce.core.domain.accounting.EdsSubsidiaryProduct;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ItemManager extends Manager<EdsItem> {

    List<EdsItem> getVariantsForZapier(Integer warehouseId);

    List<EdsItem> getItems(ListingFilterParameter fp);

    EdsItem getItem(Integer itemID);

    EdsItem getItemByObjectKey(String objectKey);

    EdsItem getItem(Integer itemID, Integer storefrontID);

    List<EdsItem> getCompanyItemList(ListingFilterParameter filterParametrs);

    List<Integer> getCompanyDeletedItemListForSolr(SolrReindexRpc solrReindex);

    List getCompanyItems();

    List<EdsItem> getCompanyItemListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<Integer> getProductsIDsByIDs(String IDs);

    List<Integer> getProductsServicesIdsWithLimit(Integer startat, Integer limit);

    List<EdsItem> getCompanyProductsByType(ListingFilterParameter filterParametrs, boolean isDescriptionIncluded, boolean isBarcodeIncluded);

    List<SelectItem> getAvailableItems(ListingFilterParameter filterParametrs);

    List<EdsProductWarehouseLocation> getProductLocationsByWarehouseID(Integer warehouseId);

    List<EdsItem> getChildProducts(Integer productId);

    List<EdsItem> getProductsByCategoryID(Integer categoryID, boolean isActive);

    List<EdsItem> getProductsByCategoryIds(Set<Integer> categoryIds, boolean isActive);

    List<EdsItem> getProductsByIds(List<Integer> products);

    HashMap<Integer, EdsItem> getProductsByIdsMap(String ids);

    HashMap<String, EdsItem> getProductsMapByNumber();

    List<EdsItem> getProductsByCategoryID(Integer categoryID, Integer start, Integer limit);

    List<EdsProductWarehouseLocation> getItemsFromStock(ListingFilterParameter filterParametrs);

    List<EdsProductCategory> getCategoryListByProducts();

    List<EdsItem> getProductsWithoutCategory();

    List<EdsItem> getProductsWithoutCategoryByProductIds(List<Integer> ids);

    List<EdsItem> getRentalProducts(ListingFilterParameter filterParametrs, ListLoadConfig config);

    List<EdsItem> getProductsForInventoryReport(ListingFilterParameter fp);

    void deleteProductVariationCombinate(Integer variationID); //variationID - Variation Product ID

    void deleteProductVariations(Integer productID);

    void deleteProductAssemblyItems(Integer productID);

    Map<String, String> getProductsNumberAndNameAsMap();

    HashMap<String, String> getProductsSSUniqNumberAndNameAsMap();

    List<EdsItem> getItemsByIds(String ids);

    HashMap<String, EdsItem> getOffersMapForNimble();

    EdsItem getInterCompanyProductByUniqueID(String productUniqueID);

    Long getProductsCountByAccountCOGSAccountAssetAccount(Integer accountID);

    List<String> getProductsNumberByAccountCOGSAccountAssetAccount(Integer accountID);

    List<EdsSubsidiaryProduct> getInterCompanyProducts(ListingFilterParameter filterParametrs);

    List<Object[]> getStockValuation(ListingFilterParameter filter);

    LinkedHashMap<Integer, BigDecimal> getStockValuationBalance(ListingFilterParameter filterParameter, boolean calculateEndingBalance);

    BigDecimal getStockValuationBalanceSum(ListingFilterParameter filterParameter, boolean calculateEndingBalance);

    Long getStockValuationCount(ListingFilterParameter filter);

    EdsItem getSubLocaleItem(Integer parentID, Integer localeID);

    EdsItem getItemByNumber(String number);

    EdsItem getItemByNumber(String number, boolean onlyActiveProduct);

    boolean checkBrandUseStorefront(Integer brandID);

    EdsItem getItemByName(String name);

    List<Integer> getItemsForSync(String syncType);

    void updateItemsAfterReset();

    List<EdsItem> getChildProducts(String productNumber);

    Integer getProductBarcode(Integer itemId);

    LinkedHashMap<Integer, BigDecimal> getStockValuationQTY(ListingFilterParameter filterParametrs, boolean beginnningBalance);

    List<EdsItem> getStockItemProductByTransaction(Integer transactionID);

    List<EdsWarehouse> getProductWarehouses(ListingFilterParameter fp);

    Integer numberAlreadyExist(NumberData numberData, Integer objectId);

    List<EdsItem> getActiveProducts();

    void deleteRentalItems(EdsItem item);

    void updateActive(List<Integer> ids, boolean active);
}
