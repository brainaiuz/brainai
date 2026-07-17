package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsItemBatch;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ItemBatchManager extends Manager<EdsItemBatch> {

    EdsItemBatch getBatch(Integer itemID, String serial);

    void deleteBatches(Integer itemID);

    void deleteBatchesByEntity(Integer entityId, Integer itemId,  String entityType);

    void deleteBatchesByEntityAndType(Integer entityId, String entityType);

    List<EdsItemBatch> getBatches(Integer lineItemId, Integer itemId,  Integer entityId, String entityType);

    List<Object> getBatchesOnHand(ListingFilterParameter filterParametrs);

    ArrayList<ProductTrackBatchItem> getBatchesOnHandByItemId(Integer itemId);

    List<EdsItemBatch> getList(ListingFilterParameter fp);

    void updateStockTransferBatchItemsStatus(Integer objectId);

    List<ProductTrackBatchItem> getBatchesForOut(Integer entityId, String entityType);

    BigDecimal getOnHandQtyByBatchItem(ProductTrackBatchItem batchItem);

    List<EdsItemBatch> getSerialsSeparated(ProductTrackBatchItem batchItem, Integer warehouseId, Integer itemId);

    void deleteOldBatches(Integer lineItemId, Integer itemId, Integer entityId, String entityType);

    Map<Integer, List<ProductTrackBatchItem>> getBatchItemsByInvoiceItemIds(Set<Integer> invoiceItemIds, Set<Integer> itemIds, Integer id, String entityType);
}
