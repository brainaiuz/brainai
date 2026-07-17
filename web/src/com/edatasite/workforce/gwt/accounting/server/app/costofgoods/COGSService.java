package com.edatasite.workforce.gwt.accounting.server.app.costofgoods;

import com.edatasite.workforce.core.domain.EdsAdjustmentItem;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsProductKitItems;
import com.edatasite.workforce.core.domain.accounting.EdsBaseInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.core.domain.accounting.IApplyingStockItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFOItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FifoItem;
import com.google.common.collect.HashBasedTable;
import org.springframework.plugin.core.Plugin;

import java.math.BigDecimal;

public interface COGSService extends Plugin<String> {

    BigDecimal createCogsTransaction(FifoItem fifoItem);

    BigDecimal createCogsTransaction(FifoItem fifoItem, boolean fromProductGroup);

    BigDecimal createCogsTransaction(FIFOItemMQ item, Integer transactionId);

    BigDecimal getCOGSValue(EdsItem product, BigDecimal itemQty, EdsTransaction transaction, Integer warehouseId, Integer transactionItemId);

    BigDecimal applyPurchasesToItemStock(IApplyingStockItem applyingItem, EdsTransaction transaction, EdsItem item, BigDecimal productKitItemQty);

    void applyReturnedItemsToStock(EdsBaseInvoiceItem invoiceItem, EdsTransaction transaction);

    void applyAdjustedItemsToStock(EdsAdjustmentItem adjustmentItem, EdsTransaction transaction);

    void applyStockTransferToItemStock(EdsItem item, BigDecimal COGS, BigDecimal itemQty, EdsTransaction transaction, Integer warehouseId, Integer transactionItemId);

    void applyStockTransferToItemStock(EdsItem item, BigDecimal COGS, BigDecimal itemQty, EdsTransaction transaction, Integer warehouseId, Integer transactionItemId, boolean runPostProcessor);

    void applyStockTransferToItemStock(EdsItem item, BigDecimal COGS, BigDecimal itemQty, EdsTransaction transaction, Integer warehouseId, Integer transactionItemId, boolean runPostProcessor, boolean fromProductGroup);

    static HashBasedTable<Integer, Integer, BigDecimal> mapRequestedItems(EdsItem item, BigDecimal qty, Integer warehouseId, HashBasedTable<Integer, Integer, BigDecimal> itemsMap) {
        if (item != null && item.getType() != null) {
            if (item.getType().equals(AccountingConstants.INVENTORY_ITEM) || item.getType().equals(AccountingConstants.ASSEMBLY_ITEM)) {
                if (itemsMap.get(item.getObjectID(), warehouseId) != null) {
                    itemsMap.put(item.getObjectID(), warehouseId, itemsMap.get(item.getObjectID(), warehouseId).add(qty));
                } else {
                    itemsMap.put(item.getObjectID(), warehouseId, qty);
                }
            } else if (item.getType().equals(AccountingConstants.PRODUCT_KIT)) {
                mapProductKitItems(item, qty, warehouseId, itemsMap);
            }
        }
        return itemsMap;
    }

    static void mapProductKitItems(EdsItem item, BigDecimal qty, Integer warehouseId, HashBasedTable<Integer, Integer, BigDecimal> itemsMap) {

        if (item.getProductKitItems() == null || item.getProductKitItems().isEmpty()) {
            return;
        }
        for (EdsProductKitItems kitItem : item.getProductKitItems()) {
            EdsItem pkItem = kitItem.getItem();

            if (AccountingConstants.PRODUCT_KIT.equals(pkItem.getType())) {
                mapProductKitItems(pkItem, qty.multiply(kitItem.getQuantity()), warehouseId, itemsMap);

            } else if (pkItem.getType().equals(AccountingConstants.INVENTORY_ITEM) || pkItem.getType().equals(AccountingConstants.ASSEMBLY_ITEM)) {
                BigDecimal itemQty = qty.multiply(kitItem.getQuantity());

                if (itemsMap.get(pkItem.getObjectID(), warehouseId) != null) {
                    itemsMap.put(pkItem.getObjectID(), warehouseId, itemsMap.get(pkItem.getObjectID(), warehouseId).add(itemQty));
                } else {
                    itemsMap.put(pkItem.getObjectID(), warehouseId, itemQty);
                }
            }
        }
    }
}
