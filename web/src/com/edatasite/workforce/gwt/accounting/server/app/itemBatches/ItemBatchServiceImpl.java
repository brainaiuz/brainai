package com.edatasite.workforce.gwt.accounting.server.app.itemBatches;

import com.edatasite.workforce.core.domain.EdsAdjustmentItem;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.gwt.accounting.client.rpc.itemBatch.ItemBatchService;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataType;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemBatchManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.ItemSerialEntityType;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.STOCK_TRANSFER_TRANSFERRED;

@Service("itemBatchService")
public class ItemBatchServiceImpl implements ItemBatchService, ItemBatchServiceLocal {

    @Autowired
    private ItemBatchManager itemBatchManager;
    @Autowired
    private QuoteItemManager quoteItemManager;
    @Autowired
    private WarehouseManager warehouseManager;
    @Autowired
    protected ItemManager itemManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private ShippingDataManager shippingDataManager;
    @Autowired
    private StockAdjustmentManager stockAdjustmentManager;
    @Autowired
    private StockTransferManager stockTransferManager;

    private static final Logger log = LoggerFactory.getLogger(ItemBatchServiceImpl.class);

    @Transactional
    public void createForOpeningBalance(EdsProductWarehouseLocation location) {
        if (location == null || location.getTrackBatchItems() == null || location.getTrackBatchItems().size() == 0) {
            return;
        }
        EdsItem item = location.getProduct();
        for (ProductTrackBatchItem trackBatchItem : location.getTrackBatchItems()) {
            EdsItemBatch itemBatch = new EdsItemBatch();
            itemBatch.setItem(item);
            itemBatch.setSerial(trackBatchItem.getSerial());
            itemBatch.setQty(trackBatchItem.getQty());
            itemBatch.setExpiryDate(trackBatchItem.getExpirationDate());
            itemBatch.setEntityId(item.getObjectID());
            itemBatch.setEntityType(ItemSerialEntityType.OPENING_BALANCE.name());
            itemBatch.setBatchType(ShippingDataType.IN.name());
            itemBatch.setWarehouse(location.getWarehouse());

            itemBatchManager.createOrUpdate(itemBatch);
        }
    }

    @Transactional
    public void createForGoodsReceived(Integer grnId, EdsShippingDataItem shippingDataItem) {
        if (shippingDataItem.getBatchItems() == null || shippingDataItem.getBatchItems().size() == 0) {
            return;
        }
        EdsQuoteItem quoteItem = quoteItemManager.get(shippingDataItem.getQuoteItemId());
        EdsWarehouse warehouse = warehouseManager.getDefaultWarehouse();
        if (shippingDataItem.getWarehouseId() != null) {
            warehouse = warehouseManager.get(shippingDataItem.getWarehouseId());
        } else if (shippingDataItem.getWarehouse() != null) {
            warehouse = shippingDataItem.getWarehouse();
        }
        List<ProductTrackBatchItem> batchItems = shippingDataItem.getBatchItems();
        for (ProductTrackBatchItem batchItem : batchItems) {
            EdsItemBatch itemBatch = new EdsItemBatch();
            itemBatch.setItem(quoteItem.getItem());
            itemBatch.setLineItemId(quoteItem.getObjectID());
            itemBatch.setSerial(batchItem.getSerial());
            itemBatch.setQty(batchItem.getQty());
            itemBatch.setExpiryDate(batchItem.getExpirationDate());
            itemBatch.setEntityId(grnId);
            itemBatch.setEntityType(ItemSerialEntityType.GOODS_RECEIVED.name());
            itemBatch.setBatchType(ShippingDataType.IN.name());
            itemBatch.setWarehouse(warehouse);
            itemBatch.setCost(quoteItem.getUnitPrice());

            itemBatchManager.createOrUpdate(itemBatch);
        }
    }

    @Transactional
    public void assignForGoodsDelivered(Integer gdnId, EdsShippingDataItem shippingDataItem) {
        if (shippingDataItem.getBatchItems() == null || shippingDataItem.getBatchItems().size() == 0) {
            return;
        }
        EdsQuoteItem quoteItem = quoteItemManager.get(shippingDataItem.getQuoteItemId());
        EdsWarehouse warehouse = warehouseManager.getDefaultWarehouse();
        if (shippingDataItem.getWarehouseId() != null) {
            warehouse = warehouseManager.get(shippingDataItem.getWarehouseId());
        } else if (shippingDataItem.getWarehouse() != null) {
            warehouse = shippingDataItem.getWarehouse();
        }
        List<ProductTrackBatchItem> batchItems = shippingDataItem.getBatchItems();

        for (ProductTrackBatchItem batchItem : batchItems) {
            EdsItemBatch itemBatch = new EdsItemBatch();

            itemBatch.setQty(batchItem.getQty());
            itemBatch.setRelatedId(batchItem.getObjectID());
            itemBatch.setItem(quoteItem.getItem());
            itemBatch.setLineItemId(quoteItem.getObjectID());
            itemBatch.setSerial(batchItem.getSerial());
            itemBatch.setExpiryDate(batchItem.getExpirationDate());
            itemBatch.setEntityId(gdnId);
            itemBatch.setEntityType(ItemSerialEntityType.GOODS_DELIVERED.name());
            itemBatch.setBatchType(ShippingDataType.OUT.name());
            itemBatch.setWarehouse(warehouse);

            itemBatchManager.createOrUpdate(itemBatch);
        }
    }

    @Transactional
    public void createBatchForPurchaseInvoice(Integer piId, NewInvoiceItem invoiceItem, Integer lineItemId) {
        if (invoiceItem.getBatchItems() == null || invoiceItem.getBatchItems().size() == 0) {
            return;
        }
        EdsItem item = itemManager.get(invoiceItem.getItemID());

        if (invoiceItem.getID() != null) {
            itemBatchManager.deleteOldBatches(invoiceItem.getID(), item.getObjectID(), piId, ItemSerialEntityType.PURCHASE_INVOICE.name());
        }

        EdsWarehouse warehouse = invoiceItem.getWarehouse() != null && invoiceItem.getWarehouse().getId() != null ? warehouseManager.get(invoiceItem.getWarehouse().getId()) : warehouseManager.getDefaultWarehouse();
        for (ProductTrackBatchItem trackBatchItem : invoiceItem.getBatchItems()) {
            if (!trackBatchItem.getSerial().isEmpty() && trackBatchItem.getQty() != null) {
                EdsItemBatch itemBatch = new EdsItemBatch();
                itemBatch.setItem(item);
                itemBatch.setSerial(trackBatchItem.getSerial());
                itemBatch.setQty(trackBatchItem.getQty());
                itemBatch.setExpiryDate(trackBatchItem.getExpirationDate());
                itemBatch.setEntityId(piId);
                itemBatch.setEntityType(ItemSerialEntityType.PURCHASE_INVOICE.name());
                itemBatch.setBatchType(ShippingDataType.IN.name());
                itemBatch.setWarehouse(warehouse);
                itemBatch.setLineItemId(lineItemId);
                itemBatch.setCost(invoiceItem.getUnitPrice());

                itemBatchManager.createOrUpdate(itemBatch);
            }
        }
    }

    @Transactional
    public void createBatchForConvertedPurchaseInvoice(Integer piId, NewInvoiceItem invoiceItem, Integer lineItemId) {
        if (invoiceItem.getBatchItems() == null || invoiceItem.getBatchItems().size() == 0) {
            return;
        }
        EdsItem item = itemManager.get(invoiceItem.getItemID());

        if (invoiceItem.getID() != null) {
            itemBatchManager.deleteOldBatches(invoiceItem.getID(), item.getObjectID(), piId, ItemSerialEntityType.PURCHASE_INVOICE.name());
        }

        EdsWarehouse warehouse = invoiceItem.getWarehouse() != null && invoiceItem.getWarehouse().getId() != null ? warehouseManager.get(invoiceItem.getWarehouse().getId()) : warehouseManager.getDefaultWarehouse();
        for (ProductTrackBatchItem trackBatchItem : invoiceItem.getBatchItems()) {
            if (!trackBatchItem.getSerial().isEmpty() && trackBatchItem.getQty() != null) {
                EdsItemBatch itemBatch = new EdsItemBatch();
                itemBatch.setItem(item);
                itemBatch.setSerial(trackBatchItem.getSerial());
                itemBatch.setQty(trackBatchItem.getQty());
                itemBatch.setExpiryDate(trackBatchItem.getExpirationDate());
                itemBatch.setEntityId(piId);
                itemBatch.setEntityType(ItemSerialEntityType.PURCHASE_INVOICE.name());
                itemBatch.setBatchType(ShippingDataType.IN.name());
                itemBatch.setWarehouse(warehouse);
                itemBatch.setLineItemId(lineItemId);
                itemBatch.setConverted(true);
                itemBatch.setCost(invoiceItem.getUnitPrice());

                itemBatchManager.createOrUpdate(itemBatch);
            }
        }
    }

    @Transactional
    public void createBatchForSaleInvoice(Integer siId, NewInvoiceItem invoiceItem, Integer lineItemId) {

        if (invoiceItem.getBatchItems() == null || invoiceItem.getBatchItems().size() == 0) {
            return;
        }
        EdsItem item = itemManager.get(invoiceItem.getItemID());

        if (invoiceItem.getID() != null) {
            itemBatchManager.deleteOldBatches(invoiceItem.getID(), item.getObjectID(), siId, ItemSerialEntityType.SALES_INVOICE.name());
        }

        EdsWarehouse warehouse = invoiceItem.getWarehouse() != null && invoiceItem.getWarehouse().getId() != null ? warehouseManager.get(invoiceItem.getWarehouse().getId()) : warehouseManager.getDefaultWarehouse();
        for (ProductTrackBatchItem batchItem : invoiceItem.getBatchItems()) {
            if (!batchItem.getSerial().isEmpty() && batchItem.getQty() != null) {

                EdsItemBatch itemBatch = new EdsItemBatch();
                itemBatch.setItem(item);
                itemBatch.setQty(batchItem.getQty());
                itemBatch.setRelatedId(batchItem.getObjectID());
                itemBatch.setLineItemId(lineItemId);
                itemBatch.setSerial(batchItem.getSerial());
                itemBatch.setExpiryDate(batchItem.getExpirationDate());
                itemBatch.setEntityId(siId);
                itemBatch.setEntityType(ItemSerialEntityType.SALES_INVOICE.name());
                itemBatch.setBatchType(ShippingDataType.OUT.name());
                itemBatch.setWarehouse(warehouse);
                itemBatchManager.createOrUpdate(itemBatch);
            }
        }
    }

    @Transactional
    public void createBatchForConvertedSaleInvoice(Integer siId, NewInvoiceItem invoiceItem, Integer lineItemId) {
        if (invoiceItem.getBatchItems() == null || invoiceItem.getBatchItems().size() == 0) {
            return;
        }
        EdsItem item = itemManager.get(invoiceItem.getItemID());

        if (invoiceItem.getID() != null) {
            itemBatchManager.deleteOldBatches(invoiceItem.getID(), item.getObjectID(), siId, ItemSerialEntityType.SALES_INVOICE.name());
        }

        EdsWarehouse warehouse = invoiceItem.getWarehouse() != null && invoiceItem.getWarehouse().getId() != null ? warehouseManager.get(invoiceItem.getWarehouse().getId()) : warehouseManager.getDefaultWarehouse();

        for (ProductTrackBatchItem batchItem : invoiceItem.getBatchItems()) {
            if (!batchItem.getSerial().isEmpty() && batchItem.getQty() != null) {
                EdsItemBatch itemBatch = new EdsItemBatch();
                itemBatch.setQty(batchItem.getQty());
                itemBatch.setItem(item);
                itemBatch.setRelatedId(batchItem.getObjectID());
                itemBatch.setLineItemId(lineItemId);
                itemBatch.setSerial(batchItem.getSerial());
                itemBatch.setExpiryDate(batchItem.getExpirationDate());
                itemBatch.setEntityId(siId);
                itemBatch.setEntityType(ItemSerialEntityType.SALES_INVOICE.name());
                itemBatch.setBatchType(ShippingDataType.OUT.name());
                itemBatch.setWarehouse(warehouse);
                itemBatch.setConverted(true);

                itemBatchManager.createOrUpdate(itemBatch);
            }
        }
    }

    public void createBatchForStockAdjustment(Integer adjustmentId, EdsAdjustmentItem adjustmentItem) {
        if (adjustmentItem.getBatchItems() == null || adjustmentItem.getBatchItems().size() == 0) {
            return;
        }
        EdsItem item = adjustmentItem.getItem();

        if (adjustmentItem.getObjectID() != null) {
            itemBatchManager.deleteOldBatches(adjustmentItem.getObjectID(), item.getObjectID(), adjustmentId, ItemSerialEntityType.STOCK_ADJUSTMENT_IN.name());
        }

        EdsWarehouse warehouse = adjustmentItem.getWarehouse() != null ? adjustmentItem.getWarehouse() : warehouseManager.getDefaultWarehouse();
        for (ProductTrackBatchItem trackBatchItem : adjustmentItem.getBatchItems()) {
            if (!trackBatchItem.getSerial().isEmpty() && trackBatchItem.getQty() != null) {
                EdsItemBatch itemBatch = new EdsItemBatch();
                itemBatch.setItem(item);
                itemBatch.setSerial(trackBatchItem.getSerial());
                itemBatch.setQty(trackBatchItem.getQty());
                itemBatch.setExpiryDate(trackBatchItem.getExpirationDate());
                itemBatch.setEntityId(adjustmentId);
                itemBatch.setEntityType(ItemSerialEntityType.STOCK_ADJUSTMENT_IN.name());
                itemBatch.setBatchType(ShippingDataType.IN.name());
                itemBatch.setWarehouse(warehouse);
                itemBatch.setLineItemId(adjustmentItem.getObjectID());

                itemBatchManager.createOrUpdate(itemBatch);
            }
        }
    }

    public void assignBatchForStockAdjustment(Integer adjustmentId, EdsAdjustmentItem adjustmentItem) {
        if (adjustmentItem.getAssignedBatchItems() == null || adjustmentItem.getAssignedBatchItems().size() == 0) {
            return;
        }
        EdsItem item = adjustmentItem.getItem();

        if (adjustmentItem.getObjectID() != null) {
            itemBatchManager.deleteOldBatches(adjustmentItem.getObjectID(), item.getObjectID(), adjustmentId, ItemSerialEntityType.STOCK_ADJUSTMENT_OUT.name());
        }

        EdsWarehouse warehouse = adjustmentItem.getWarehouse() != null ? adjustmentItem.getWarehouse() : warehouseManager.getDefaultWarehouse();
        for (ProductTrackBatchItem batchItem : adjustmentItem.getAssignedBatchItems()) {
            if (!batchItem.getSerial().isEmpty() && batchItem.getQty() != null) {
                List<EdsItemBatch> list = itemBatchManager.getSerialsSeparated(batchItem, warehouse.getObjectID(), item.getObjectID());
                BigDecimal outQty = batchItem.getQty();
                for (EdsItemBatch edsItemBatch : list) {
                    EdsItemBatch itemBatch = new EdsItemBatch();
                    if (outQty.compareTo(edsItemBatch.getQty()) > 0) {
                        itemBatch.setQty(edsItemBatch.getQty());
                        outQty = outQty.subtract(edsItemBatch.getQty());
                    } else {
                        itemBatch.setQty(outQty);
                        outQty = BigDecimal.ZERO;
                    }
                    itemBatch.setRelatedId(edsItemBatch.getObjectID());
                    itemBatch.setItem(item);
                    itemBatch.setLineItemId(adjustmentItem.getObjectID());
                    itemBatch.setSerial(batchItem.getSerial());
                    itemBatch.setExpiryDate(batchItem.getExpirationDate());
                    itemBatch.setEntityId(adjustmentId);
                    itemBatch.setEntityType(ItemSerialEntityType.STOCK_ADJUSTMENT_OUT.name());
                    itemBatch.setBatchType(ShippingDataType.OUT.name());
                    itemBatch.setWarehouse(warehouse);

                    itemBatchManager.createOrUpdate(itemBatch);
                    if (outQty.equals(BigDecimal.ZERO)) break;
                }
            }
        }
    }

    public void assignBatchForStockTransferOut(Integer adjustmentId, EdsAdjustmentItem adjustmentItem, String transferStatus) {
        if (adjustmentItem.getBatchItems() == null || adjustmentItem.getBatchItems().size() == 0) {
            return;
        }
        EdsItem item = adjustmentItem.getItem();

        if (adjustmentItem.getObjectID() != null) {
            itemBatchManager.deleteOldBatches(adjustmentItem.getObjectID(), item.getObjectID(), adjustmentId, ItemSerialEntityType.STOCK_TRANSFER_OUT.name());
        }

        EdsWarehouse warehouse = adjustmentItem.getWarehouse() != null ? adjustmentItem.getWarehouse() : warehouseManager.getDefaultWarehouse();
        String status = STOCK_TRANSFER_TRANSFERRED.equals(transferStatus) ? ItemSerialEntityType.APPROVED.name() : ItemSerialEntityType.PENDING.name();

        for (ProductTrackBatchItem batchItem : adjustmentItem.getBatchItems()) {
            if (!batchItem.getSerial().isEmpty() && batchItem.getQty() != null) {
                List<EdsItemBatch> list = itemBatchManager.getSerialsSeparated(batchItem, warehouse.getObjectID(), item.getObjectID());
                BigDecimal outQty = batchItem.getQty();
                for (EdsItemBatch edsItemBatch : list) {
                    EdsItemBatch itemBatch = new EdsItemBatch();
                    if (list.size() > 1) {
                        if (outQty.compareTo(edsItemBatch.getQty()) > 0) {
                            itemBatch.setQty(edsItemBatch.getQty());
                            outQty = outQty.subtract(edsItemBatch.getQty());
                        } else {
                            itemBatch.setQty(outQty);
                            outQty = BigDecimal.ZERO;
                        }
                    } else {
                        itemBatch.setQty(outQty);
                    }
                    itemBatch.setRelatedId(edsItemBatch.getObjectID());
                    itemBatch.setItem(item);
                    itemBatch.setLineItemId(adjustmentItem.getObjectID());
                    itemBatch.setSerial(batchItem.getSerial());
                    itemBatch.setExpiryDate(batchItem.getExpirationDate());
                    itemBatch.setEntityId(adjustmentId);
                    itemBatch.setEntityType(ItemSerialEntityType.STOCK_TRANSFER_OUT.name());
                    itemBatch.setBatchType(ShippingDataType.OUT.name());
                    itemBatch.setWarehouse(warehouse);
                    itemBatch.setStatus(status);

                    itemBatchManager.createOrUpdate(itemBatch);
                    itemBatch.setLineItemId(adjustmentItem.getObjectID());
                    itemBatchManager.update(itemBatch);
                    if (outQty.equals(BigDecimal.ZERO)) break;
                }
            }
        }
    }

    public void createBatchForStockTransferIn(Integer adjustmentId, EdsAdjustmentItem adjustmentItem, String transferStatus) {
        if (adjustmentItem.getBatchItems() == null || adjustmentItem.getBatchItems().size() == 0) {
            return;
        }
        EdsItem item = adjustmentItem.getItem();

        if (adjustmentItem.getObjectID() != null) {
            itemBatchManager.deleteOldBatches(adjustmentItem.getObjectID(), item.getObjectID(), adjustmentId, ItemSerialEntityType.STOCK_TRANSFER_IN.name());
        }

        EdsWarehouse warehouse = adjustmentItem.getWarehouse() != null ? adjustmentItem.getWarehouse() : warehouseManager.getDefaultWarehouse();
        String status = STOCK_TRANSFER_TRANSFERRED.equals(transferStatus) ? ItemSerialEntityType.APPROVED.name() : ItemSerialEntityType.PENDING.name();

        for (ProductTrackBatchItem trackBatchItem : adjustmentItem.getBatchItems()) {
            if (!trackBatchItem.getSerial().isEmpty() && trackBatchItem.getQty() != null) {
                EdsItemBatch itemBatch = new EdsItemBatch();
                itemBatch.setItem(item);
                itemBatch.setSerial(trackBatchItem.getSerial());
                itemBatch.setQty(trackBatchItem.getQty());
                itemBatch.setExpiryDate(trackBatchItem.getExpirationDate());
                itemBatch.setEntityId(adjustmentId);
                itemBatch.setEntityType(ItemSerialEntityType.STOCK_TRANSFER_IN.name());
                itemBatch.setBatchType(ShippingDataType.IN.name());
                itemBatch.setWarehouse(warehouse);
                itemBatch.setLineItemId(adjustmentItem.getObjectID());
                itemBatch.setStatus(status);

                itemBatchManager.createOrUpdate(itemBatch);
            }
        }
    }

    @Override
    public void createBatchCreditNote(Integer noteId, EdsBaseInvoiceItem invoiceItem) {
        if (invoiceItem.getBatchItems() == null || invoiceItem.getBatchItems().size() == 0) {
            return;
        }
        EdsItem item = invoiceItem.getItem();

        if (invoiceItem.getObjectID() != null) {
            itemBatchManager.deleteOldBatches(invoiceItem.getObjectID(), item.getObjectID(), noteId, ItemSerialEntityType.CREDIT_NOTE.name());
        }

        EdsWarehouse warehouse = invoiceItem.getWarehouse() != null ? invoiceItem.getWarehouse() : warehouseManager.getDefaultWarehouse();
        for (ProductTrackBatchItem trackBatchItem : invoiceItem.getBatchItems()) {
            if (!trackBatchItem.getSerial().isEmpty() && trackBatchItem.getQty() != null) {
                EdsItemBatch itemBatch = new EdsItemBatch();
                itemBatch.setItem(item);
                itemBatch.setSerial(trackBatchItem.getSerial());
                itemBatch.setQty(trackBatchItem.getQty());
                itemBatch.setExpiryDate(trackBatchItem.getExpirationDate());
                itemBatch.setEntityId(noteId);
                itemBatch.setEntityType(ItemSerialEntityType.CREDIT_NOTE.name());
                itemBatch.setBatchType(ShippingDataType.IN.name());
                itemBatch.setWarehouse(warehouse);
                itemBatch.setLineItemId(invoiceItem.getObjectID());

                itemBatchManager.createOrUpdate(itemBatch);
            }
        }
    }

    @Override
    public void createBatchDebitNote(Integer noteId, EdsBaseInvoiceItem invoiceItem) {
        if (invoiceItem.getBatchItems() == null || invoiceItem.getBatchItems().size() == 0) {
            return;
        }
        EdsItem item = invoiceItem.getItem();

        if (invoiceItem.getObjectID() != null) {
            itemBatchManager.deleteOldBatches(invoiceItem.getObjectID(), item.getObjectID(), noteId, ItemSerialEntityType.DEBIT_NOTE.name());
        }

        EdsWarehouse warehouse = invoiceItem.getWarehouse() != null ? invoiceItem.getWarehouse() : warehouseManager.getDefaultWarehouse();
        for (ProductTrackBatchItem batchItem : invoiceItem.getBatchItems()) {
            if (!batchItem.getSerial().isEmpty() && batchItem.getQty() != null) {
                List<EdsItemBatch> list = itemBatchManager.getSerialsSeparated(batchItem, warehouse.getObjectID(), item.getObjectID());
                BigDecimal outQty = batchItem.getQty();
                for (EdsItemBatch edsItemBatch : list) {
                    EdsItemBatch itemBatch = new EdsItemBatch();
                    if (outQty.compareTo(edsItemBatch.getQty()) > 0) {
                        itemBatch.setQty(edsItemBatch.getQty());
                        outQty = outQty.subtract(edsItemBatch.getQty());
                    } else {
                        itemBatch.setQty(outQty);
                        outQty = BigDecimal.ZERO;
                    }
                    itemBatch.setRelatedId(edsItemBatch.getObjectID());
                    itemBatch.setItem(item);
                    itemBatch.setLineItemId(invoiceItem.getObjectID());
                    itemBatch.setSerial(batchItem.getSerial());
                    itemBatch.setExpiryDate(batchItem.getExpirationDate());
                    itemBatch.setEntityId(noteId);
                    itemBatch.setEntityType(ItemSerialEntityType.DEBIT_NOTE.name());
                    itemBatch.setBatchType(ShippingDataType.OUT.name());
                    itemBatch.setWarehouse(warehouse);

                    itemBatchManager.createOrUpdate(itemBatch);
                    if (outQty.equals(BigDecimal.ZERO)) break;
                }
            }
        }
    }

    @Override
    public ArrayList<ProductTrackBatchItem> getBatchItems(Integer lineItemId, Integer itemID, Integer entityId, String entityType) {
        ArrayList<ProductTrackBatchItem> resultList = Lists.newArrayList();
        List<EdsItemBatch> batches = itemBatchManager.getBatches(lineItemId, itemID, entityId, entityType);
        for (EdsItemBatch batch : batches) {
            ProductTrackBatchItem item = new ProductTrackBatchItem();
            item.setObjectID(batch.getObjectID());
            item.setSerial(batch.getSerial());
            item.setExpirationDate(batch.getExpiryDate());
            item.setQty(batch.getQty());
            item.setCost(batch.getCost());
            item.setItemID(itemID);
            resultList.add(item);
        }
        return resultList;
    }

    @Override
    public ArrayList<ProductTrackBatchItem> getBatchItemsOfGrnOrGdn(Integer quoteItemId, Integer itemID, Integer entityId, String entityType) {

        final List<EdsShippingData> list = shippingDataManager.getByQuoteId(entityId);
        ArrayList<ProductTrackBatchItem> resultList = Lists.newArrayList();
        for (EdsShippingData edsShippingData : list) {
            for (EdsShippingDataItem item : edsShippingData.getItems()) {
                if (item.getQuoteItemId().equals(quoteItemId)) {
                    List<EdsItemBatch> batches = itemBatchManager.getBatches(quoteItemId, itemID, edsShippingData.getObjectID(), entityType);
                    for (EdsItemBatch batch : batches) {
                        ProductTrackBatchItem batchItem = new ProductTrackBatchItem();
                        batchItem.setObjectID(batch.getObjectID());
                        batchItem.setSerial(batch.getSerial());
                        batchItem.setExpirationDate(batch.getExpiryDate());
                        batchItem.setQty(batch.getQty());
                        batchItem.setItemID(itemID);
                        resultList.add(batchItem);
                    }
                }
            }
        }
        return resultList;
    }

    public ListResult<ProductTrackBatchItem> getAllBatchesHistory(ListingFilterParameter fp) {
        List<EdsItemBatch> result = itemBatchManager.getList(fp);
        ArrayList<ProductTrackBatchItem> trackBatchItems = Lists.newArrayList();
        for (EdsItemBatch edsItemBatch : result) {
            ProductTrackBatchItem batchItem = new ProductTrackBatchItem();
            batchItem.setObjectID(edsItemBatch.getObjectID());
            batchItem.setSerial(edsItemBatch.getSerial());
            batchItem.setExpirationDate(edsItemBatch.getExpiryDate());
            batchItem.setQty(edsItemBatch.getQty());
            batchItem.setBatchType(edsItemBatch.getBatchType());
            batchItem.setEntityId(edsItemBatch.getEntityId());
            batchItem.setEntityType(edsItemBatch.getEntityType());
            batchItem.setWarehouseName(edsItemBatch.getWarehouse() != null ? edsItemBatch.getWarehouse().getName() : "");
            String link = "";
            String relatedTo = "";
            if (ItemSerialEntityType.SALES_INVOICE.name().equals(edsItemBatch.getEntityType())) {
                link = "saleinvoice/" + edsItemBatch.getEntityId();
                relatedTo = invoiceManager.get(edsItemBatch.getEntityId()).getNumber();
            } else if (ItemSerialEntityType.PURCHASE_INVOICE.name().equals(edsItemBatch.getEntityType())) {
                link = "purchaseinvoice/" + edsItemBatch.getEntityId();
                relatedTo = invoiceManager.get(edsItemBatch.getEntityId()).getNumber();
            } else if (ItemSerialEntityType.GOODS_DELIVERED.name().equals(edsItemBatch.getEntityType())) {
                link = "gdn|summary/" + edsItemBatch.getEntityId();
                relatedTo = shippingDataManager.get(edsItemBatch.getEntityId()).getNumber();
            } else if (ItemSerialEntityType.GOODS_RECEIVED.name().equals(edsItemBatch.getEntityType())) {
                link = "grn|summary/" + edsItemBatch.getEntityId();
                relatedTo = shippingDataManager.get(edsItemBatch.getEntityId()).getNumber();
            } else if (ItemSerialEntityType.CREDIT_NOTE.name().equals(edsItemBatch.getEntityType())) {
                link = "receivablecreditnote/" + edsItemBatch.getEntityId();
                relatedTo = invoiceManager.get(edsItemBatch.getEntityId()).getNumber();
            } else if (ItemSerialEntityType.DEBIT_NOTE.name().equals(edsItemBatch.getEntityType())) {
                link = "payablecreditnote/" + edsItemBatch.getEntityId();
                relatedTo = invoiceManager.get(edsItemBatch.getEntityId()).getNumber();
            } else if (ItemSerialEntityType.STOCK_ADJUSTMENT_IN.name().equals(edsItemBatch.getEntityType())) {
                link = "stockadjustment|summary/" + edsItemBatch.getEntityId();
                relatedTo = stockAdjustmentManager.get(edsItemBatch.getEntityId()).getNumber();
            } else if (ItemSerialEntityType.STOCK_ADJUSTMENT_OUT.name().equals(edsItemBatch.getEntityType())) {
                link = "stockadjustment|summary/" + edsItemBatch.getEntityId();
                relatedTo = stockAdjustmentManager.get(edsItemBatch.getEntityId()).getNumber();
            } else if (ItemSerialEntityType.STOCK_TRANSFER_IN.name().equals(edsItemBatch.getEntityType())) {
                link = "stocktransfer|summary/" + edsItemBatch.getEntityId();
                relatedTo = stockTransferManager.get(edsItemBatch.getEntityId()).getNumber();
            } else if (ItemSerialEntityType.STOCK_TRANSFER_OUT.name().equals(edsItemBatch.getEntityType())) {
                link = "stocktransfer|summary/" + edsItemBatch.getEntityId();
                relatedTo = stockTransferManager.get(edsItemBatch.getEntityId()).getNumber();
            } else if (ItemSerialEntityType.OPENING_BALANCE.name().equals(edsItemBatch.getEntityType())) {
                link = "product|summary/" + edsItemBatch.getEntityId() + "/Inventory Item/BATCH_TRACKING";
                relatedTo = "Opening Balance";
            }
            batchItem.setRelatedTo(relatedTo);
            batchItem.setLink(link);
            trackBatchItems.add(batchItem);
        }
        return new ListResult<>((ArrayList<ProductTrackBatchItem>) trackBatchItems, trackBatchItems.size());
    }

    @Override
    public void updateStockTransferBatchItemsStatus(Integer objectId) {
        itemBatchManager.updateStockTransferBatchItemsStatus(objectId);
    }

    @Override
    public SelectItem[] validateBatchSerialsOnHand(Integer entityId) {
        List<ProductTrackBatchItem> batches = itemBatchManager.getBatchesForOut(entityId, ItemSerialEntityType.STOCK_TRANSFER_OUT.name());

        if (batches == null || batches.size() == 0) {
            return new SelectItem[]{};
        }
        List<SelectItem> errors = new ArrayList<>();
        int i = 1;
        for (ProductTrackBatchItem batch : batches) {
            BigDecimal onhanQty = itemBatchManager.getOnHandQtyByBatchItem(batch);
            if (batch.getQty().compareTo(onhanQty) > 0) {
                errors.add(new SelectItem(i++, batch.getSerial()));
            }
        }
        return errors.toArray(new SelectItem[]{});
    }

    @Override
    public void deleteBatchSerialsForInvoice(EdsInvoice invoice) {
        String entityType = "";
        if (invoice instanceof EdsSaleInvoice) {
            if (invoice.isCreditNote()) {
                entityType = ItemSerialEntityType.CREDIT_NOTE.name();
            } else {
                entityType = ItemSerialEntityType.SALES_INVOICE.name();
            }
        } else {
            if (invoice.isCreditNote()) {
                entityType = ItemSerialEntityType.DEBIT_NOTE.name();
            } else {
                entityType = ItemSerialEntityType.PURCHASE_INVOICE.name();
            }
        }

        itemBatchManager.deleteBatchesByEntityAndType(invoice.getObjectID(), entityType);

    }

    @Override
    public void deleteBatchSerialsForAdjustment(Integer adjustmentId) {
        itemBatchManager.deleteBatchesByEntityAndType(adjustmentId, ItemSerialEntityType.STOCK_ADJUSTMENT_IN.name());
        itemBatchManager.deleteBatchesByEntityAndType(adjustmentId, ItemSerialEntityType.STOCK_ADJUSTMENT_OUT.name());
    }

    @Override
    public void deleteBatchSerialsForTransfer(Integer transferId) {
        itemBatchManager.deleteBatchesByEntityAndType(transferId, ItemSerialEntityType.STOCK_TRANSFER_IN.name());
        itemBatchManager.deleteBatchesByEntityAndType(transferId, ItemSerialEntityType.STOCK_TRANSFER_OUT.name());
    }
}
