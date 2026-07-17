package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsItemStock;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Mar 3, 2011
 * Time: 12:23:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ItemStockManager extends Manager<EdsItemStock> {

    List<EdsItemStock> getItemStockByTransaction(Integer transactionID, Integer itemID);

    List<EdsItemStock> getItemStockByTransaction(Integer transactionID, Integer itemID, EdsWarehouse warehouse, Integer productIdentifier);

    List<EdsItemStock> getItemStockListByProductAndWarehouse(Integer productID, Integer warehouseID);

    List<StockItem> getItemStocksByWarehouse(Integer itemID, Integer warehouseID);

    Map<Integer, BigDecimal> getItemStocksForSync();

    List<StockItem> getItemStocks(Integer transactionID, Integer itemID, Integer warehouseID);

    BigDecimal getOutItemQtyFromStock(Integer transactionID, Integer itemID, Integer warehouseID, Integer productIdentifier);

    Object getInventoryTransactionBalanceToDate(Integer objectID, Date toDate, Integer warehouseID);

    HashMap<Integer, BigDecimal> getInventoryTransactionBalance(List<Integer> itemIds, Integer warehouseID);

    LinkedHashMap<Integer, List<StockItem>> getInventoryTransaction(ListingFilterParameter fp);

    LinkedHashMap<Integer, EdsTransaction> getInventoryTransactionMap(ListingFilterParameter fp);

    void deleteItemStocksByTransaction(Integer transactionID);

    void deleteItemOutStocksByTransaction(Integer transactionID);

    void deleteItemStocksByTransaction(Integer transactionID, String transactionCode, Integer itemId, Integer warehouseId, Integer transactionItemId);

    void deleteItemStocksByProduct(Integer productID);

    boolean isUsedInTransactions(Integer itemId);

    void unBuildAssemblyItemStocks(Integer productID);

    void deleteByTransaction(Integer transactionID, Integer itemID);

    void deleteByID(Integer objectID);

    Integer getNextOrder(Integer objectID);

    BigDecimal getItemQtyInStockByWarehouse(Integer itemID, Integer warehouseID, Date date);

    BigDecimal getItemLastInStockPrice(Integer itemID, Date date);

    BigDecimal getItemLastInStockTranValue(Integer itemID);

    List<Integer> getItemsInWarehouse(Integer warehouseID);

    List<Object> getProductsStock(Integer companyID, String tranCode);

    BigDecimal getTransactionValueByTransactionIdAndItemId(Integer transactionID, Integer itemID);

    List<Integer> getItemsByUpsNumber(String productNumber);

    boolean hasOutTransactionsOfItemWithChosenIn(List<Integer> transactionIds);

    List<StockItem> getRemainedQuantitiesOfItemWithChosenIn(Integer transactionId);

    BigDecimal getAvailableStock(Integer itemId, Integer warehouseId, List<Integer> excludedTransactionIds);

    BigDecimal getAvailableStock(Integer itemId, Integer warehouseId, List<Integer> excludedTransactionIds, DateNonConvertable tillDate);

    Map<Integer, BigDecimal> getAvailableStockAtWarehouse(String itemIds, Integer warehouseId);

    void deleteItemStocksByTransactionIds(List<Integer> transactionIds);

    List<StockItem> getWarehouseStocks(Integer itemId);

    LinkedHashMap<Integer, List<StockItem>> getWarehouseStocksMap(String itemIds);

    List<QuantityItem> getInStocksByTransactions(List<Integer> transactionIds);
}
